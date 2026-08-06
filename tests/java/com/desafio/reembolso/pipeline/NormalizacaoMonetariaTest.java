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

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-004 / CA-009 / CA-018 (spec 4.2, 5): todo {@code despesa.valor}
 * estruturalmente válido normaliza para escala 2 com arredondamento
 * {@code HALF_UP}, sem passar por {@code double} em nenhum ponto do
 * caminho. {@code 100.005 → 100.01} é o teste-canário de precisão
 * decimal-exata.
 */
@DisplayName("Normalização monetária — RN-004 / CA-009 / CA-018")
class NormalizacaoMonetariaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static ItemValidado validarUnico(String valorJson) {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": %s, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(valorJson);
        try {
            JsonNode despesas = MAPPER.readTree(json).get("despesas");
            return ValidadorItem.validarLista(despesas).get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest(name = "{0} → {1}")
    @DisplayName("RN-004 / CA-009 / CA-018 — fronteiras normativas de arredondamento HALF_UP")
    @CsvSource({
            "33.333, 33.33",
            "33.335, 33.34",
            "33.345, 33.35",
            "100.004, 100.00",
            "100.005, 100.01",
            "72, 72.00",
            "0, 0.00",
            "-45.005, -45.01"
    })
    void normalizaConformeFronteirasNormativas(String informado, String esperado) {
        ItemValidado item = validarUnico(informado);
        ItemNormalizado normalizado = CambioTesteSupport.resolverENormalizar(item);

        assertEquals(2, normalizado.valorNormalizado().scale(), "escala deve ser exatamente 2");
        assertEquals(new BigDecimal(esperado), normalizado.valorNormalizado());
        assertEquals(esperado, normalizado.valorNormalizado().toPlainString());

        assertEquals(new BigDecimal(informado), item.getValorInformado().decimalValue(),
                "valor_informado não pode ser alterado pela normalização");

        assertTrue(item.getMotivos().isEmpty(), "nenhum motivo VALOR_NAO_POSITIVO ou outro deve ser criado aqui");
    }

    @Test
    @DisplayName("RN-004 — valor estruturalmente inválido (nulo) produz valor_normalizado nulo")
    void valorEstruturalmenteInvalido_valorNormalizadoNulo() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": "72,50", "tem_nota_fiscal": true }
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
        assertNull(item.getValor(), "pré-condição: valor estruturalmente inválido");
        assertFalse(item.getMotivos().isEmpty(), "item já traz motivo estrutural CAMPO_TIPO_INVALIDO");

        ItemValidado resolvido = CambioTesteSupport.resolver(item);
        ItemNormalizado normalizado = Normalizador.normalizar(resolvido);

        assertNull(normalizado.valorNormalizado());
        assertSame(item, normalizado.item(),
                "valor estruturalmente inválido não passa por conversão: ResolutorCambio devolve a mesma referência");
        assertEquals(1, item.getMotivos().size(), "normalização não deve acrescentar nem remover motivos");
    }

    @Test
    @DisplayName("lista retornada por normalizarLista preserva ordem, indiceEntrada, e é não modificável")
    void normalizarLista_preservaOrdemIndiceENaoModificavel() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 33.333, "tem_nota_fiscal": true },
                    { "id": "d-002", "data": "2026-07-04", "categoria": "hospedagem", "descricao": "B", "fornecedor": "F2", "valor": 100.005, "tem_nota_fiscal": true }
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
        List<ItemNormalizado> resultado = CambioTesteSupport.resolverENormalizarLista(itens);

        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).item().getIndiceEntrada());
        assertEquals(2, resultado.get(1).item().getIndiceEntrada());
        assertEquals(new BigDecimal("33.33"), resultado.get(0).valorNormalizado());
        assertEquals(new BigDecimal("100.01"), resultado.get(1).valorNormalizado());

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(resultado.get(0)));
    }
}
