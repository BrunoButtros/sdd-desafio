package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-007 / CA-016 (parcial — spec 4.5, 5, 7, 8.2, 8.4): categoria
 * normalizada fora do conjunto fechado ({@code alimentacao},
 * {@code transporte_urbano}, {@code hospedagem}) recebe
 * {@code CATEGORIA_FORA_POLITICA} e fica inelegível, com
 * {@code valorReembolsavel} {@code 0.00}. RN-007 depende exclusivamente de
 * {@code categoriaNormalizada} — não é bloqueada por motivo estrutural em
 * outro campo, nem por {@code ID_DUPLICADO} ou {@code VALOR_NAO_POSITIVO},
 * e não é avaliada quando a própria categoria é estruturalmente inválida.
 */
@DisplayName("Categoria fora da política — RN-007 / CA-016 (parcial)")
class CategoriaForaPoliticaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static JsonNode despesas(String json) {
        try {
            return MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<ItemValidado> validar(String json) {
        return ValidadorItem.validarLista(despesas(json));
    }

    private static List<ItemNormalizado> normalizar(String json) {
        return Normalizador.normalizarLista(validar(json));
    }

    private static Motivo categoriaForaPolitica() {
        return new Motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null);
    }

    private static Motivo valorNaoPositivo() {
        return new Motivo(MotivoCodigo.VALOR_NAO_POSITIVO, RegraNegocio.RN_006, null);
    }

    private static String itemComCategoriaEValor(String categoria, String valorJson, boolean temNotaFiscal) {
        return """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-07", "categoria": "%s",
                      "descricao": "A", "fornecedor": "F1", "valor": %s, "tem_nota_fiscal": %s }
                  ]
                }
                """.formatted(categoria, valorJson, temNotaFiscal);
    }

    @Test
    @DisplayName("1 — coworking, 89.00, com nota: recebe exatamente um CATEGORIA_FORA_POLITICA, RN-007, campo nulo, inelegível, reembolsável 0.00 escala 2")
    void coworking_recebeMotivoEFicaInelegivel() {
        ItemNormalizado item = normalizar(itemComCategoriaEValor("coworking", "89.00", true)).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        Motivo motivo = avaliado.motivos().get(0);
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, motivo.codigo());
        assertEquals(RegraNegocio.RN_007, motivo.regra());
        assertNull(motivo.campo());
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());
        assertEquals(2, avaliado.valorReembolsavel().scale());
    }

    @ParameterizedTest
    @ValueSource(strings = {"alimentacao", "transporte_urbano", "hospedagem"})
    @DisplayName("2 — categorias canônicas não recebem RN-007 e permanecem elegíveis, reembolsável ainda nulo")
    void categoriasCanonicas_naoRecebemMotivoEPermanecemElegiveis(String categoria) {
        ItemNormalizado item = normalizar(itemComCategoriaEValor(categoria, "50.00", true)).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertFalse(avaliado.motivos().contains(categoriaForaPolitica()));
        assertTrue(avaliado.elegivel());
        assertNull(avaliado.valorReembolsavel());
    }

    @ParameterizedTest
    @CsvSource({"ALIMENTACAO", "Alimentação", "alimentacao", "' alimentacao '"})
    @DisplayName("3 — categorias normalizáveis para alimentacao não recebem RN-007")
    void categoriasNormalizaveis_naoRecebemMotivo(String categoriaInformada) {
        ItemNormalizado item = normalizar(itemComCategoriaEValor(categoriaInformada, "50.00", true)).get(0);
        assertEquals("alimentacao", item.categoriaNormalizada());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertFalse(avaliado.motivos().contains(categoriaForaPolitica()));
        assertTrue(avaliado.elegivel());
    }

    @Test
    @DisplayName("4 — 'transporte urbano' permanece com espaço após RN-005 e recebe CATEGORIA_FORA_POLITICA")
    void transporteUrbanoComEspaco_recebeMotivo() {
        ItemNormalizado item = normalizar(itemComCategoriaEValor("transporte urbano", "50.00", true)).get(0);
        assertEquals("transporte urbano", item.categoriaNormalizada());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertTrue(avaliado.motivos().contains(categoriaForaPolitica()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("5 — categoria estruturalmente inválida (ausente): categoriaNormalizada nula, mantém só motivo estrutural, não recebe RN-007, inelegível")
    void categoriaEstruturalmenteInvalida_naoAvaliaRn007() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-07",
                      "descricao": "A", "fornecedor": "F1", "valor": 50.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);
        assertNull(item.categoriaNormalizada());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_AUSENTE, avaliado.motivos().get(0).codigo());
        assertFalse(avaliado.motivos().contains(categoriaForaPolitica()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("6 — data malformada com categoria coworking: mantém motivo estrutural de data e também recebe CATEGORIA_FORA_POLITICA")
    void dataInvalidaComCoworking_naoBloqueiaRn007() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "31/07/2026", "categoria": "coworking",
                      "descricao": "A", "fornecedor": "F1", "valor": 50.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, avaliado.motivos().get(0).codigo());
        assertTrue(avaliado.motivos().contains(categoriaForaPolitica()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("7 — ID_DUPLICADO com categoria coworking: preserva ID_DUPLICADO e acrescenta CATEGORIA_FORA_POLITICA, nessa ordem")
    void idDuplicadoComCoworking_preservaEAcrescentaMotivo() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-100", "data": "2026-07-07", "categoria": "coworking", "descricao": "A", "fornecedor": "F1", "valor": 50.00, "tem_nota_fiscal": true },
                    { "id": "d-100", "data": "2026-07-08", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        List<ItemValidado> validados = validar(json);
        List<ItemValidado> comIdDuplicado = DetectorIdDuplicado.detectar(validados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comIdDuplicado);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados);

        ItemAvaliado primeiro = avaliados.get(0);
        assertEquals(2, primeiro.motivos().size());
        assertEquals(MotivoCodigo.ID_DUPLICADO, primeiro.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, primeiro.motivos().get(1).codigo());
        assertFalse(primeiro.elegivel());
    }

    @Test
    @DisplayName("8 — valor negativo com categoria coworking: recebe VALOR_NAO_POSITIVO seguido de CATEGORIA_FORA_POLITICA, inelegível, reembolsável 0.00")
    void valorNegativoComCoworking_recebeAmbosMotivosNaOrdem() {
        ItemNormalizado item = normalizar(itemComCategoriaEValor("coworking", "-10.00", true)).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, avaliado.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, avaliado.motivos().get(1).codigo());
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());
    }

    @Test
    @DisplayName("9 — valor negativo com categoria válida: recebe apenas VALOR_NAO_POSITIVO, não recebe CATEGORIA_FORA_POLITICA")
    void valorNegativoComCategoriaValida_naoRecebeCategoriaForaPolitica() {
        ItemNormalizado item = normalizar(itemComCategoriaEValor("alimentacao", "-10.00", true)).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, avaliado.motivos().get(0).codigo());
        assertFalse(avaliado.motivos().contains(categoriaForaPolitica()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("10 — população elegível simulada: só itens de categoria válida sem outro motivo permanecem elegíveis")
    void populacaoElegivelSimulada_filtraPorElegivel() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-07", "categoria": "coworking", "descricao": "A", "fornecedor": "F1", "valor": 50.00, "tem_nota_fiscal": true },
                    { "id": "d-002", "data": "2026-07-07", "categoria": "transporte urbano", "descricao": "B", "fornecedor": "F2", "valor": 30.00, "tem_nota_fiscal": true },
                    { "id": "d-003", "data": "2026-07-07", "categoria": "alimentacao", "descricao": "C", "fornecedor": "F3", "valor": 25.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizar(json));

        List<ItemAvaliado> elegiveis = avaliados.stream()
                .filter(ItemAvaliado::elegivel)
                .collect(Collectors.toList());

        assertEquals(1, elegiveis.size());
        assertEquals("d-003", elegiveis.get(0).itemNormalizado().item().getId());
    }

    @Test
    @DisplayName("11 — lista retornada por avaliarLista preserva ordem, indiceEntrada, e é não modificável")
    void avaliarLista_preservaOrdemIndiceENaoModificavel() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-07", "categoria": "coworking", "descricao": "A", "fornecedor": "F1", "valor": 50.00, "tem_nota_fiscal": true },
                    { "id": "d-002", "data": "2026-07-08", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        List<ItemNormalizado> normalizados = normalizar(json);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados);

        assertEquals(2, avaliados.size());
        assertEquals(1, avaliados.get(0).itemNormalizado().item().getIndiceEntrada());
        assertEquals(2, avaliados.get(1).itemNormalizado().item().getIndiceEntrada());
        assertSame(normalizados.get(0), avaliados.get(0).itemNormalizado());
        assertSame(normalizados.get(1), avaliados.get(1).itemNormalizado());

        assertThrows(UnsupportedOperationException.class, () -> avaliados.add(avaliados.get(0)));
        assertThrows(UnsupportedOperationException.class,
                () -> avaliados.get(0).motivos().add(categoriaForaPolitica()));
    }

    @Test
    @DisplayName("12 — reaplicação não duplica CATEGORIA_FORA_POLITICA")
    void reaplicacao_naoDuplicaMotivo() {
        ItemNormalizado item = normalizar(itemComCategoriaEValor("coworking", "89.00", true)).get(0);

        ItemAvaliado primeiraAplicacao = AvaliadorRegrasIndividuais.avaliar(item);
        ItemAvaliado segundaAplicacao = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, primeiraAplicacao.motivos().size());
        assertEquals(1, segundaAplicacao.motivos().size());
        assertEquals(primeiraAplicacao.motivos(), segundaAplicacao.motivos());
    }
}
