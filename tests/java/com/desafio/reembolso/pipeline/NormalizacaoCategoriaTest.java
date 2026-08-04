package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-005 / CA-015 (spec 4.2, 5): {@code despesa.categoria}
 * estruturalmente válida normaliza por remoção de espaços das pontas,
 * conversão para minúsculas em {@code Locale.ROOT} e remoção de
 * diacríticos — nenhuma outra transformação. A responsabilidade de
 * comparar contra o vocabulário fechado pertence a RN-007/T-009.
 */
@DisplayName("Normalização de categoria — RN-005 / CA-015")
class NormalizacaoCategoriaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static ItemValidado validarUnicoComCategoria(String categoriaJson) {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": %s,
                      "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(categoriaJson);
        try {
            JsonNode despesas = MAPPER.readTree(json).get("despesas");
            return ValidadorItem.validarLista(despesas).get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest(name = "\"{0}\" → \"{1}\"")
    @DisplayName("RN-005 / CA-015 — caixa, acento e espaços nas pontas")
    @CsvSource(value = {
            "ALIMENTACAO, alimentacao",
            "Alimentação, alimentacao",
            "alimentacao, alimentacao",
            "' alimentacao ', alimentacao",
            "HOSPEDAGEM, hospedagem",
            "TRANSPORTE_URBANO, transporte_urbano",
            "coworking, coworking"
    })
    void normalizaConformeCasosNormativos(String categoriaInformada, String esperado) {
        ItemValidado item = validarUnicoComCategoria('"' + categoriaInformada + '"');
        ItemNormalizado normalizado = Normalizador.normalizar(item);

        assertEquals(esperado, normalizado.categoriaNormalizada());
    }

    @Test
    @DisplayName("RN-005 — 'transporte urbano' permanece com espaço interno, não vira 'transporte_urbano'")
    void transporteUrbanoComEspaco_permaneceComEspaco() {
        ItemValidado item = validarUnicoComCategoria("\"transporte urbano\"");
        ItemNormalizado normalizado = Normalizador.normalizar(item);

        assertEquals("transporte urbano", normalizado.categoriaNormalizada());
    }

    @Test
    @DisplayName("RN-005 — espaços internos não são alterados nem colapsados")
    void espacosInternos_naoSaoAlterados() {
        ItemValidado item = validarUnicoComCategoria("\"transporte   urbano\"");
        ItemNormalizado normalizado = Normalizador.normalizar(item);

        assertEquals("transporte   urbano", normalizado.categoriaNormalizada());
    }

    @Test
    @DisplayName("RN-005 — categoria estruturalmente inválida (ausente) produz categoria_normalizada nula, nunca 'coworking' ou outro texto")
    void categoriaEstruturalmenteInvalida_categoriaNormalizadaNula() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03",
                      "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        JsonNode despesas;
        try {
            despesas = MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ItemValidado item = ValidadorItem.validarLista(despesas).get(0);
        assertNull(item.getCategoria(), "pré-condição: categoria estruturalmente inválida (ausente)");
        assertFalse(item.getMotivos().isEmpty(), "item já traz motivo estrutural CAMPO_AUSENTE");

        ItemNormalizado normalizado = Normalizador.normalizar(item);

        assertNull(normalizado.categoriaNormalizada());
        assertSame(item, normalizado.item(), "ItemNormalizado deve apontar para o mesmo ItemValidado");
        assertEquals(1, item.getMotivos().size(), "normalização não deve acrescentar nem remover motivos");
    }

    @Test
    @DisplayName("item original permanece inalterado após a normalização")
    void itemOriginal_permaneceInalterado() {
        ItemValidado item = validarUnicoComCategoria("\"ALIMENTACAO\"");
        ItemNormalizado normalizado = Normalizador.normalizar(item);

        assertEquals("ALIMENTACAO", item.getCategoria(), "categoria original não pode ser alterada");
        assertSame(item, normalizado.item());
    }

    @Test
    @DisplayName("motivos estruturais anteriores permanecem presentes após a normalização")
    void motivosAnteriores_permanecemPresentes() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "31/07/2026", "categoria": "ALIMENTACAO",
                      "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        JsonNode despesas;
        try {
            despesas = MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ItemValidado item = ValidadorItem.validarLista(despesas).get(0);
        int motivosAntes = item.getMotivos().size();
        assertTrue(motivosAntes > 0, "pré-condição: item traz motivo estrutural de data malformada");

        ItemNormalizado normalizado = Normalizador.normalizar(item);

        assertEquals("alimentacao", normalizado.categoriaNormalizada());
        assertEquals(motivosAntes, item.getMotivos().size(), "motivos estruturais anteriores devem permanecer intactos");
    }

    @Test
    @DisplayName("lista retornada por normalizarLista é não modificável")
    void normalizarLista_naoModificavel() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "ALIMENTACAO", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        JsonNode despesas;
        try {
            despesas = MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<ItemValidado> itens = ValidadorItem.validarLista(despesas);
        List<ItemNormalizado> resultado = Normalizador.normalizarLista(itens);

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(resultado.get(0)));
    }
}
