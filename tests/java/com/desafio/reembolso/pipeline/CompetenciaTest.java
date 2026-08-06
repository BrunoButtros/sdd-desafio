package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Envelope;
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
 * Cobre RN-008 / CA-011 / CA-012 (spec 4.5, 7, 8.2, 8.4): item com
 * {@code despesa.data} fora de {@code [periodo.inicio, periodo.fim]} recebe
 * {@code FORA_COMPETENCIA}, com bordas inclusivas. RN-008 depende
 * exclusivamente de {@code despesa.data}, {@code periodo.inicio} e
 * {@code periodo.fim} — não é bloqueada por motivo estrutural em outro
 * campo, nem por {@code ID_DUPLICADO}, {@code VALOR_NAO_POSITIVO} ou
 * {@code CATEGORIA_FORA_POLITICA}, e não é avaliada quando a própria data é
 * estruturalmente inválida.
 */
@DisplayName("Elegibilidade temporal — RN-008 / CA-011 / CA-012")
class CompetenciaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final String JANELA_JULHO =
            "\"periodo\": { \"inicio\": \"2026-07-01\", \"fim\": \"2026-07-31\" },";

    private static JsonNode raiz(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Envelope envelope(String json) {
        return ValidadorEnvelope.validar(raiz(json));
    }

    private static List<ItemValidado> validar(String json) {
        return ValidadorItem.validarLista(envelope(json).getDespesas());
    }

    private static List<ItemNormalizado> normalizar(String json) {
        return Normalizador.normalizarLista(CambioTesteSupport.resolverLista(validar(json)));
    }

    private static List<ItemNormalizado> normalizarDespesas(Envelope envelope) {
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        return Normalizador.normalizarLista(CambioTesteSupport.resolverLista(validados));
    }

    private static Motivo foraCompetencia() {
        return new Motivo(MotivoCodigo.FORA_COMPETENCIA, RegraNegocio.RN_008, null);
    }

    private static String envelopeComItem(String data, String valorJson, String categoria, boolean temNotaFiscal) {
        return """
                {
                  %s
                  "despesas": [
                    { "id": "d-001", "data": "%s", "categoria": "%s",
                      "descricao": "A", "fornecedor": "F1", "valor": %s, "tem_nota_fiscal": %s }
                  ]
                }
                """.formatted(JANELA_JULHO, data, categoria, valorJson, temNotaFiscal);
    }

    private static String envelopeComItem(String data) {
        return envelopeComItem(data, "50.00", "alimentacao", true);
    }

    @Test
    @DisplayName("1 — data anterior ao início: recebe exatamente FORA_COMPETENCIA, RN-008, campo nulo, inelegível, reembolsável 0.00 escala 2")
    void dataAnteriorAoInicio_recebeMotivoEFicaInelegivel() {
        String json = envelopeComItem("2026-04-15");
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(1, avaliado.motivos().size());
        Motivo motivo = avaliado.motivos().get(0);
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, motivo.codigo());
        assertEquals(RegraNegocio.RN_008, motivo.regra());
        assertNull(motivo.campo());
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());
        assertEquals(2, avaliado.valorReembolsavel().scale());
    }

    @Test
    @DisplayName("2 — data posterior ao fim: recebe FORA_COMPETENCIA e fica inelegível")
    void dataPosteriorAoFim_recebeMotivoEFicaInelegivel() {
        String json = envelopeComItem("2026-08-01");
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertTrue(avaliado.motivos().contains(foraCompetencia()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("3 — borda inicial inclusiva: não recebe FORA_COMPETENCIA, permanece elegível, reembolsável nulo")
    void bordaInicial_naoRecebeMotivoEPermaneceElegivel() {
        String json = envelopeComItem("2026-07-01");
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertFalse(avaliado.motivos().contains(foraCompetencia()));
        assertTrue(avaliado.elegivel());
        assertNull(avaliado.valorReembolsavel());
    }

    @Test
    @DisplayName("4 — borda final inclusiva: não recebe FORA_COMPETENCIA, permanece elegível")
    void bordaFinal_naoRecebeMotivoEPermaneceElegivel() {
        String json = envelopeComItem("2026-07-31");
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertFalse(avaliado.motivos().contains(foraCompetencia()));
        assertTrue(avaliado.elegivel());
    }

    @Test
    @DisplayName("5 — data interna: permanece elegível e não recebe RN-008")
    void dataInterna_permaneceElegivel() {
        String json = envelopeComItem("2026-07-15");
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertTrue(avaliado.elegivel());
        assertFalse(avaliado.motivos().contains(foraCompetencia()));
    }

    @Test
    @DisplayName("6 — data estruturalmente inválida: getData nulo, mantém CAMPO_FORMATO_INVALIDO, não recebe FORA_COMPETENCIA, inelegível")
    void dataEstruturalmenteInvalida_naoAvaliaRn008() {
        String json = """
                {
                  %s
                  "despesas": [
                    { "id": "d-001", "data": "31/07/2026", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": 50.00, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(JANELA_JULHO);
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);
        assertNull(item.item().getData());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.DATA, avaliado.motivos().get(0).campo());
        assertFalse(avaliado.motivos().contains(foraCompetencia()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("7 — erro estrutural em outro campo (valor) com data fora: mantém motivo estrutural de valor e também recebe FORA_COMPETENCIA")
    void valorInvalidoComDataFora_naoBloqueiaRn008() {
        String json = """
                {
                  %s
                  "despesas": [
                    { "id": "d-001", "data": "2026-04-15", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": "72,50", "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(JANELA_JULHO);
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.VALOR, avaliado.motivos().get(0).campo());
        assertTrue(avaliado.motivos().contains(foraCompetencia()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("8 — ID_DUPLICADO com data fora: preserva ID_DUPLICADO e acrescenta FORA_COMPETENCIA, nessa ordem")
    void idDuplicadoComDataFora_preservaEAcrescentaMotivo() {
        String json = """
                {
                  %s
                  "despesas": [
                    { "id": "d-100", "data": "2026-04-15", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-100", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(JANELA_JULHO);
        Envelope envelope = envelope(json);
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> comIdDuplicado = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(comIdDuplicado);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);

        ItemAvaliado primeiro = avaliados.get(0);
        assertEquals(2, primeiro.motivos().size());
        assertEquals(MotivoCodigo.ID_DUPLICADO, primeiro.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, primeiro.motivos().get(1).codigo());
        assertFalse(primeiro.elegivel());
    }

    @Test
    @DisplayName("9 — valor negativo com data fora: recebe VALOR_NAO_POSITIVO seguido de FORA_COMPETENCIA, sem motivo de categoria")
    void valorNegativoComDataFora_recebeAmbosMotivosNaOrdem() {
        String json = envelopeComItem("2026-04-15", "-10.00", "alimentacao", true);
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, avaliado.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, avaliado.motivos().get(1).codigo());
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("10 — categoria fora da política com data fora: recebe CATEGORIA_FORA_POLITICA seguido de FORA_COMPETENCIA")
    void categoriaForaPoliticaComDataFora_recebeAmbosMotivosNaOrdem() {
        String json = envelopeComItem("2026-04-15", "50.00", "coworking", true);
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, avaliado.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, avaliado.motivos().get(1).codigo());
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("11 — três regras simultâneas: VALOR_NAO_POSITIVO, CATEGORIA_FORA_POLITICA, FORA_COMPETENCIA, nessa ordem exata")
    void tresRegrasSimultaneas_ordemExata() {
        String json = envelopeComItem("2026-04-15", "-10.00", "coworking", true);
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(3, avaliado.motivos().size());
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, avaliado.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, avaliado.motivos().get(1).codigo());
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, avaliado.motivos().get(2).codigo());
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("12 — periodo.competencia não decide a regra: item dentro da janela de inicio/fim permanece elegível mesmo com competencia divergente")
    void competenciaDivergente_naoAlteraElegibilidadeTemporal() {
        String json = """
                {
                  "periodo": { "competencia": "2026-04", "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-15", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": 50.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        Envelope envelope = envelope(json);
        assertEquals("2026-04", envelope.getPeriodoCompetencia());
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertFalse(avaliado.motivos().contains(foraCompetencia()));
        assertTrue(avaliado.elegivel());
    }

    @Test
    @DisplayName("13 — população elegível simulada: só os itens dentro da janela fechada permanecem elegíveis")
    void populacaoElegivelSimulada_filtraPorJanelaFechada() {
        String json = """
                {
                  %s
                  "despesas": [
                    { "id": "d-001", "data": "2026-04-15", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-002", "data": "2026-08-01", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-003", "data": "2026-07-01", "categoria": "alimentacao", "descricao": "C", "fornecedor": "F3", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-004", "data": "2026-07-31", "categoria": "alimentacao", "descricao": "D", "fornecedor": "F4", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-005", "data": "2026-07-15", "categoria": "alimentacao", "descricao": "E", "fornecedor": "F5", "valor": 10.00, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(JANELA_JULHO);
        Envelope envelope = envelope(json);
        List<ItemNormalizado> normalizados =
                normalizarDespesas(envelope);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);

        List<String> idsElegiveis = avaliados.stream()
                .filter(ItemAvaliado::elegivel)
                .map(a -> a.itemNormalizado().item().getId())
                .collect(Collectors.toList());

        assertEquals(List.of("d-003", "d-004", "d-005"), idsElegiveis);
    }

    @Test
    @DisplayName("14 — lista retornada por avaliarLista(itens, envelope) preserva ordem, indiceEntrada, referência e é não modificável")
    void avaliarListaComEnvelope_preservaOrdemIndiceENaoModificavel() {
        String json = """
                {
                  %s
                  "despesas": [
                    { "id": "d-001", "data": "2026-04-15", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-002", "data": "2026-07-15", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(JANELA_JULHO);
        Envelope envelope = envelope(json);
        List<ItemNormalizado> normalizados =
                normalizarDespesas(envelope);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);

        assertEquals(2, avaliados.size());
        assertEquals(1, avaliados.get(0).itemNormalizado().item().getIndiceEntrada());
        assertEquals(2, avaliados.get(1).itemNormalizado().item().getIndiceEntrada());
        assertSame(normalizados.get(0), avaliados.get(0).itemNormalizado());
        assertSame(normalizados.get(1), avaliados.get(1).itemNormalizado());

        assertThrows(UnsupportedOperationException.class, () -> avaliados.add(avaliados.get(0)));
        assertThrows(UnsupportedOperationException.class,
                () -> avaliados.get(0).motivos().add(foraCompetencia()));
    }

    @Test
    @DisplayName("15 — reaplicação sobre o mesmo item e envelope não duplica FORA_COMPETENCIA")
    void reaplicacao_naoDuplicaMotivo() {
        String json = envelopeComItem("2026-04-15");
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado primeiraAplicacao = AvaliadorRegrasIndividuais.avaliar(item, envelope);
        ItemAvaliado segundaAplicacao = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(1, primeiraAplicacao.motivos().size());
        assertEquals(1, segundaAplicacao.motivos().size());
        assertEquals(primeiraAplicacao.motivos(), segundaAplicacao.motivos());
    }
}
