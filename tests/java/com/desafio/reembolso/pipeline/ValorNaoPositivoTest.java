package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
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
 * Cobre RN-006 / CA-017 (parcial — spec 4.5, 7, 8.2, 8.4): item cujo valor
 * normalizado seja menor ou igual a zero recebe {@code VALOR_NAO_POSITIVO}
 * e fica inelegível, com {@code valorReembolsavel} {@code 0.00}. RN-006
 * depende exclusivamente de {@code valorNormalizado} — não é bloqueada por
 * motivo estrutural em outro campo, nem por {@code ID_DUPLICADO}, e não é
 * avaliada quando o próprio valor é estruturalmente inválido.
 */
@DisplayName("Valor não positivo — RN-006 / CA-017 (parcial)")
class ValorNaoPositivoTest {

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

    private static Motivo valorNaoPositivo() {
        return new Motivo(MotivoCodigo.VALOR_NAO_POSITIVO, RegraNegocio.RN_006, null);
    }

    private static String itemComValor(String valorJson) {
        return """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": %s, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(valorJson);
    }

    @Test
    @DisplayName("1 — -45.00 recebe exatamente um VALOR_NAO_POSITIVO, RN-006, campo nulo, inelegível, reembolsável 0.00")
    void valorNegativo_recebeMotivoEFicaInelegivel() {
        ItemNormalizado item = normalizar(itemComValor("-45.00")).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        Motivo motivo = avaliado.motivos().get(0);
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, motivo.codigo());
        assertEquals(RegraNegocio.RN_006, motivo.regra());
        assertNull(motivo.campo());
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());
        assertEquals(2, avaliado.valorReembolsavel().scale());
    }

    @Test
    @DisplayName("2 — 0 normaliza para 0.00, recebe VALOR_NAO_POSITIVO e fica inelegível")
    void valorZero_recebeMotivoEFicaInelegivel() {
        ItemNormalizado item = normalizar(itemComValor("0")).get(0);
        assertEquals(new BigDecimal("0.00"), item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertTrue(avaliado.motivos().contains(valorNaoPositivo()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("3 — 0.004 normaliza para 0.00 e recebe VALOR_NAO_POSITIVO")
    void valorArredondaParaZero_recebeMotivo() {
        ItemNormalizado item = normalizar(itemComValor("0.004")).get(0);
        assertEquals(new BigDecimal("0.00"), item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertTrue(avaliado.motivos().contains(valorNaoPositivo()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("4 — 0.005 normaliza para 0.01, não recebe VALOR_NAO_POSITIVO e permanece elegível")
    void valorArredondaParaPositivo_naoRecebeMotivo() {
        ItemNormalizado item = normalizar(itemComValor("0.005")).get(0);
        assertEquals(new BigDecimal("0.01"), item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertFalse(avaliado.motivos().contains(valorNaoPositivo()));
        assertTrue(avaliado.elegivel());
    }

    @Test
    @DisplayName("5 — valor positivo (72) normaliza para 72.00, permanece elegível, valorReembolsavel ainda nulo")
    void valorPositivo_permaneceElegivelSemValorReembolsavelAinda() {
        ItemNormalizado item = normalizar(itemComValor("72")).get(0);
        assertEquals(new BigDecimal("72.00"), item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertTrue(avaliado.motivos().isEmpty());
        assertTrue(avaliado.elegivel());
        assertNull(avaliado.valorReembolsavel());
    }

    @Test
    @DisplayName("6 — valor estruturalmente inválido: valorNormalizado nulo, mantém motivo estrutural, não recebe VALOR_NAO_POSITIVO, inelegível, reembolsável 0.00")
    void valorEstruturalmenteInvalido_naoAvaliaRn006() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": "72,50", "tem_nota_fiscal": true }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);
        assertNull(item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, avaliado.motivos().get(0).codigo());
        assertFalse(avaliado.motivos().contains(valorNaoPositivo()));
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());
    }

    @Test
    @DisplayName("7 — data malformada com valor negativo: mantém CAMPO_FORMATO_INVALIDO de despesa.data e também recebe VALOR_NAO_POSITIVO")
    void dataInvalidaComValorNegativo_naoBloqueiaRn006() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "31/07/2026", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": -10.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.DATA, avaliado.motivos().get(0).campo());
        assertTrue(avaliado.motivos().contains(valorNaoPositivo()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("8 — ID_DUPLICADO com valor negativo: preserva ID_DUPLICADO e acrescenta VALOR_NAO_POSITIVO, nessa ordem")
    void idDuplicadoComValorNegativo_preservaEAcrescentaMotivo() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-100", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": -10.00, "tem_nota_fiscal": true },
                    { "id": "d-100", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
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
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, primeiro.motivos().get(1).codigo());
        assertFalse(primeiro.elegivel());
    }

    @Test
    @DisplayName("9 — id duplicado com valor positivo não recebe VALOR_NAO_POSITIVO, continua inelegível pelo ID_DUPLICADO")
    void idDuplicadoComValorPositivo_naoRecebeMotivoMasContinuaInelegivel() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-100", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-100", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        List<ItemValidado> validados = validar(json);
        List<ItemValidado> comIdDuplicado = DetectorIdDuplicado.detectar(validados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comIdDuplicado);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados);

        ItemAvaliado primeiro = avaliados.get(0);
        assertEquals(1, primeiro.motivos().size());
        assertEquals(MotivoCodigo.ID_DUPLICADO, primeiro.motivos().get(0).codigo());
        assertFalse(primeiro.motivos().contains(valorNaoPositivo()));
        assertFalse(primeiro.elegivel());
    }

    @Test
    @DisplayName("10 — população elegível simulada: só o positivo sem outros motivos permanece elegível")
    void populacaoElegivelSimulada_filtraPorElegivel() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": -10.00, "tem_nota_fiscal": true },
                    { "id": "d-002", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 0, "tem_nota_fiscal": true },
                    { "id": "d-003", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "C", "fornecedor": "F3", "valor": 25.00, "tem_nota_fiscal": true }
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
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": -10.00, "tem_nota_fiscal": true },
                    { "id": "d-002", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
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
                () -> avaliados.get(0).motivos().add(valorNaoPositivo()));
    }

    @Test
    @DisplayName("12 — reaplicação não duplica VALOR_NAO_POSITIVO")
    void reaplicacao_naoDuplicaMotivo() {
        ItemNormalizado item = normalizar(itemComValor("-45.00")).get(0);

        ItemAvaliado primeiraAplicacao = AvaliadorRegrasIndividuais.avaliar(item);
        ItemAvaliado segundaAplicacao = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, primeiraAplicacao.motivos().size());
        assertEquals(1, segundaAplicacao.motivos().size());
        assertEquals(primeiraAplicacao.motivos(), segundaAplicacao.motivos());
    }
}
