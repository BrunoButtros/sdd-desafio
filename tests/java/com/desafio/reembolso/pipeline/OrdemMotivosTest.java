package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.CompositorSaida.ResultadoItem;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DecimalNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre a ordem de apresentação dos motivos (spec 8.3, 8.4) produzida por
 * {@link CompositorSaida}: independente da ordem de detecção pelo pipeline,
 * a saída final segue exclusivamente a tabela de precedência de 8.3.
 */
@DisplayName("Ordem de apresentação dos motivos — spec 8.3 / 8.4")
class OrdemMotivosTest {

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

    private static List<ResultadoItem> pipelineCompleto(String json) {
        Envelope envelope = ValidadorEnvelope.validar(raiz(json));

        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(idsVerificados);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);

        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);

        List<ItemAvaliado> elegiveisParaTetos = SeletorElegiveis.selecionar(aposDuplicidade);
        List<ResultadoTeto> resultadosDiarios = AgregadorTetoDiario.aplicar(elegiveisParaTetos);
        List<ResultadoTeto> resultadosHospedagem = AgregadorTetoHospedagem.aplicar(elegiveisParaTetos);

        return CompositorSaida.compor(avaliados, aposDuplicidade, resultadosDiarios, resultadosHospedagem);
    }

    private static String item(String id, String data, String categoria, String descricao,
                                String fornecedor, String valorJson, boolean temNotaFiscal) {
        return """
                { "id": "%s", "data": "%s", "categoria": "%s", "descricao": "%s",
                  "fornecedor": "%s", "valor": %s, "tem_nota_fiscal": %s }""".formatted(
                id, data, categoria, descricao, fornecedor, valorJson, temNotaFiscal);
    }

    private static String envelopeComItens(String... despesasJson) {
        return """
                {
                  %s
                  "despesas": [
                    %s
                  ]
                }
                """.formatted(JANELA_JULHO, String.join(",\n", despesasJson));
    }

    private static List<MotivoCodigo> codigos(ResultadoItem resultado) {
        return resultado.motivos().stream().map(Motivo::codigo).toList();
    }

    // ---- 1. Primeiro exemplo normativo de 8.4 ----------------------------------

    @Test
    @DisplayName("1 — R$ 500,00 coworking sem nota fora da janela: CATEGORIA_FORA_POLITICA, FORA_COMPETENCIA, NOTA_FISCAL_AUSENTE, nesta ordem")
    void primeiroExemploNormativo_tresMotivosNaOrdemDe83() {
        String json = envelopeComItens(
                item("d-001", "2026-04-01", "coworking", "Diaria", "HubOffice", "500.00", false));

        List<ResultadoItem> resultados = pipelineCompleto(json);
        ResultadoItem r = resultados.get(0);

        assertEquals(Decisao.RECUSADO, r.decisao());
        assertEquals(new BigDecimal("0.00"), r.valorReembolsavel());
        assertEquals(3, r.motivos().size());

        Motivo m1 = r.motivos().get(0);
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, m1.codigo());
        assertEquals(RegraNegocio.RN_007, m1.regra());

        Motivo m2 = r.motivos().get(1);
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, m2.codigo());
        assertEquals(RegraNegocio.RN_008, m2.regra());

        Motivo m3 = r.motivos().get(2);
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, m3.codigo());
        assertEquals(RegraNegocio.RN_009, m3.regra());
    }

    // ---- 2. Segundo exemplo normativo de 8.4 ------------------------------------

    @Test
    @DisplayName("2 — R$ -500,00 coworking sem nota, dentro da janela: VALOR_NAO_POSITIVO, CATEGORIA_FORA_POLITICA, sem NOTA_FISCAL_AUSENTE")
    void segundoExemploNormativo_semNotaFiscalAusente() {
        String json = envelopeComItens(
                item("d-001", "2026-07-10", "coworking", "Diaria", "HubOffice", "-500.00", false));

        List<ResultadoItem> resultados = pipelineCompleto(json);
        ResultadoItem r = resultados.get(0);

        assertEquals(Decisao.RECUSADO, r.decisao());
        assertEquals(new BigDecimal("0.00"), r.valorReembolsavel());
        assertEquals(List.of(MotivoCodigo.VALOR_NAO_POSITIVO, MotivoCodigo.CATEGORIA_FORA_POLITICA), codigos(r));
        assertTrue(r.motivos().stream().noneMatch(m -> m.codigo() == MotivoCodigo.NOTA_FISCAL_AUSENTE),
                "valor não positivo torna a regra de nota fiscal não aplicável (8.4.10)");
    }

    // ---- 3. Ordem independente da ordem de detecção -----------------------------

    private static ItemValidado itemValidadoMinimo(int indiceEntrada, String id, LocalDate data,
                                                     String categoria, BigDecimal valor, boolean temNotaFiscal) {
        return new ItemValidado(indiceEntrada, id, data, categoria, "descricao", "fornecedor",
                valor, temNotaFiscal, DecimalNode.valueOf(valor), List.of());
    }

    @Test
    @DisplayName("3 — motivos fornecidos fora de ordem ao compositor são reorganizados conforme 8.3, prova de que a ordenação é do compositor")
    void ordemIndependeDaDeteccao_compositorReorganiza() {
        ItemValidado validado = itemValidadoMinimo(
                1, "d-001", LocalDate.of(2026, 4, 1), "coworking", new BigDecimal("500.00"), false);
        ItemNormalizado normalizado = Normalizador.normalizar(validado);

        List<Motivo> motivosForaDeOrdem = List.of(
                new Motivo(MotivoCodigo.NOTA_FISCAL_AUSENTE, RegraNegocio.RN_009, null),
                new Motivo(MotivoCodigo.FORA_COMPETENCIA, RegraNegocio.RN_008, null),
                new Motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null)
        );
        ItemAvaliado itemAvaliado = new ItemAvaliado(normalizado, motivosForaDeOrdem, false, new BigDecimal("0.00"));

        List<ResultadoItem> resultado = CompositorSaida.compor(
                List.of(itemAvaliado), List.of(), List.of(), List.of());

        assertEquals(
                List.of(MotivoCodigo.CATEGORIA_FORA_POLITICA, MotivoCodigo.FORA_COMPETENCIA, MotivoCodigo.NOTA_FISCAL_AUSENTE),
                codigos(resultado.get(0)));
    }

    // ---- 4. Ordem estrutural por campo -------------------------------------------

    @Test
    @DisplayName("4 — vários defeitos estruturais aparecem na ordem canônica de campo: id, data, categoria, valor, tem_nota_fiscal")
    void ordemEstruturalPorCampo() {
        String json = envelopeComItens("""
                { "id": "", "data": "31/07/2026", "categoria": 123, "descricao": "A",
                  "fornecedor": "F1", "valor": "72,50", "tem_nota_fiscal": "sim" }""");

        List<ResultadoItem> resultados = pipelineCompleto(json);
        ResultadoItem r = resultados.get(0);

        assertEquals(Decisao.RECUSADO, r.decisao());
        assertEquals(5, r.motivos().size());

        assertEquals(CampoCanonico.ID, r.motivos().get(0).campo());
        assertEquals(CampoCanonico.DATA, r.motivos().get(1).campo());
        assertEquals(CampoCanonico.CATEGORIA, r.motivos().get(2).campo());
        assertEquals(CampoCanonico.VALOR, r.motivos().get(3).campo());
        assertEquals(CampoCanonico.TEM_NOTA_FISCAL, r.motivos().get(4).campo());

        assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, r.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, r.motivos().get(1).codigo());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, r.motivos().get(2).codigo());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, r.motivos().get(3).codigo());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, r.motivos().get(4).codigo());
    }

    // ---- 5. ITEM_TIPO_INVALIDO permanece motivo único ----------------------------

    @Test
    @DisplayName("5 — elemento que não é objeto: ITEM_TIPO_INVALIDO permanece motivo único após a ordenação")
    void itemTipoInvalido_motivoUnico() {
        String json = envelopeComItens("\"despesa\"");

        List<ResultadoItem> resultados = pipelineCompleto(json);
        ResultadoItem r = resultados.get(0);

        assertEquals(1, r.motivos().size());
        assertEquals(MotivoCodigo.ITEM_TIPO_INVALIDO, r.motivos().get(0).codigo());
        assertNull(r.motivos().get(0).campo());
    }

    // ---- 6. ID_DUPLICADO combinado com regras individuais -------------------------

    @Test
    @DisplayName("6 — ID_DUPLICADO combinado com regras individuais aparece antes delas: ID_DUPLICADO, VALOR_NAO_POSITIVO, CATEGORIA_FORA_POLITICA")
    void idDuplicadoCombinadoComRegrasIndividuais() {
        String json = envelopeComItens(
                item("d-100", "2026-07-10", "coworking", "X", "F1", "-50.00", true),
                item("d-100", "2026-07-11", "alimentacao", "Y", "F2", "20.00", true)
        );

        List<ResultadoItem> resultados = pipelineCompleto(json);
        ResultadoItem primeiro = resultados.get(0);

        assertEquals(Decisao.RECUSADO, primeiro.decisao());
        assertEquals(
                List.of(MotivoCodigo.ID_DUPLICADO, MotivoCodigo.VALOR_NAO_POSITIVO, MotivoCodigo.CATEGORIA_FORA_POLITICA),
                codigos(primeiro));
    }

    // ---- 7. Motivos de teto -------------------------------------------------------

    @Test
    @DisplayName("7a — item integral não tem motivo algum")
    void tetoIntegral_semMotivo() {
        String json = envelopeComItens(
                item("d-001", "2026-07-10", "alimentacao", "Almoco", "F1", "30.00", true));

        ResultadoItem r = pipelineCompleto(json).get(0);

        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, r.decisao());
        assertTrue(r.motivos().isEmpty());
    }

    @Test
    @DisplayName("7b — parcial por teto diário apresenta somente TETO_DIARIO_APLICADO")
    void tetoDiarioParcial_somenteTetoDiarioAplicado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-10", "alimentacao", "Almoco", "F1", "61.00", true));

        ResultadoItem r = pipelineCompleto(json).get(0);

        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, r.decisao());
        assertEquals(List.of(MotivoCodigo.TETO_DIARIO_APLICADO), codigos(r));
    }

    @Test
    @DisplayName("7c — esgotado apresenta somente TETO_DIARIO_ESGOTADO")
    void tetoDiarioEsgotado_somenteTetoDiarioEsgotado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-10", "alimentacao", "Almoco", "F1", "72.50", true),
                item("d-002", "2026-07-10", "alimentacao", "Jantar", "F2", "38.00", true)
        );

        ResultadoItem segundo = pipelineCompleto(json).get(1);

        assertEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, segundo.decisao());
        assertEquals(List.of(MotivoCodigo.TETO_DIARIO_ESGOTADO), codigos(segundo));
    }

    @Test
    @DisplayName("7d — parcial de hospedagem apresenta somente TETO_HOSPEDAGEM_APLICADO")
    void tetoHospedagemParcial_somenteTetoHospedagemAplicado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-10", "hospedagem", "Hotel", "F1", "480.00", true));

        ResultadoItem r = pipelineCompleto(json).get(0);

        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, r.decisao());
        assertEquals(List.of(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO), codigos(r));
    }
}
