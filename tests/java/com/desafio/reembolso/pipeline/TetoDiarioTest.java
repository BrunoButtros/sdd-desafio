package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-011 / RN-012 / CA-004 (spec 4.4, 4.5, 8.1, 8.2, 8.4, 8.5): a
 * agregação por {@code data} e categoria normalizada dos itens elegíveis de
 * {@code alimentacao} e {@code transporte_urbano}, com hospedagem e itens
 * inelegíveis excluídos do agregador diário.
 */
@DisplayName("Teto diário — RN-011 / RN-012 / CA-004")
class TetoDiarioTest {

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

    private static List<ItemAvaliado> elegiveisParaTetos(String json) {
        Envelope envelope = envelope(json);
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(idsVerificados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = CambioTesteSupport.avaliarLista(normalizados, envelope);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);
        return SeletorElegiveis.selecionar(aposDuplicidade);
    }

    private static List<ResultadoTeto> resultados(String json) {
        return CambioTesteSupport.aplicarTetoDiario(elegiveisParaTetos(json));
    }

    // ---- 1. Duas despesas de alimentação na mesma data ----------------------

    @Test
    @DisplayName("1 — CA-004: duas despesas de alimentação na mesma data somam exatamente R$ 60,00")
    void duasAlimentacaoMesmaData_somamSessenta() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "72.50", true),
                item("d-002", "2026-07-03", "alimentacao", "Jantar", "F1", "38.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(2, resultados.size());
        BigDecimal total = resultados.get(0).valorReembolsavel().add(resultados.get(1).valorReembolsavel());
        assertEquals(new BigDecimal("60.00"), total);
    }

    // ---- 2. Transporte urbano sozinho ----------------------------------------

    @Test
    @DisplayName("2 — CA-004: transporte urbano de R$ 100,00 sozinho na data rende R$ 80,00")
    void transporteUrbanoSozinho_rendeOitenta() {
        String json = envelopeComItens(
                item("d-001", "2026-07-06", "transporte_urbano", "Uber", "F1", "100.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("80.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(1, resultado.motivos().size());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_012, motivo.regra());
        assertNull(motivo.campo());
    }

    // ---- 3. Alimentação e transporte na mesma data ---------------------------

    @Test
    @DisplayName("3 — alimentação e transporte na mesma data têm saldos independentes")
    void alimentacaoETransporteMesmaData_saldosIndependentes() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "60.00", true),
                item("d-002", "2026-07-03", "transporte_urbano", "Uber", "F1", "80.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(2, resultados.size());
        assertEquals(new BigDecimal("60.00"), resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertEquals(new BigDecimal("80.00"), resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());
    }

    // ---- 4. Mesma categoria em datas diferentes ------------------------------

    @Test
    @DisplayName("4 — mesma categoria em datas diferentes não compartilha saldo")
    void mesmaCategoriaDatasDiferentes_naoCompartilhaSaldo() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "50.00", true),
                item("d-002", "2026-07-04", "alimentacao", "Almoco", "F1", "50.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(2, resultados.size());
        assertEquals(new BigDecimal("50.00"), resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertEquals(new BigDecimal("50.00"), resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());
    }

    // ---- 5. Item abaixo do teto -----------------------------------------------

    @Test
    @DisplayName("5 — item abaixo do teto é pago integralmente com motivos vazios")
    void itemAbaixoDoTeto_pagoIntegralmenteSemMotivos() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "59.99", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("59.99"), resultado.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado.decisao());
        assertTrue(resultado.motivos().isEmpty());
    }

    // ---- 6. Item exatamente no teto -------------------------------------------

    @Test
    @DisplayName("6 — item exatamente no teto é integral, não parcial; saldo esgota só para itens posteriores")
    void itemExatamenteNoTeto_integralNaoParcial() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "60.00", true),
                item("d-002", "2026-07-03", "alimentacao", "Jantar", "F1", "10.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto primeiro = resultados.get(0);
        assertEquals(new BigDecimal("60.00"), primeiro.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, primeiro.decisao());
        assertTrue(primeiro.motivos().isEmpty());

        ResultadoTeto segundo = resultados.get(1);
        assertEquals(new BigDecimal("0.00"), segundo.valorReembolsavel());
        assertEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, segundo.decisao());
    }

    // ---- 7. Hospedagem na lista -------------------------------------------------

    @Test
    @DisplayName("7 — hospedagem não aparece nos resultados, não consome saldo, permanece na lista de elegíveis")
    void hospedagemNaLista_naoParticipaDoAgregadorDiario() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "40.00", true),
                item("d-002", "2026-07-03", "hospedagem", "Hotel", "F1", "90.00", false)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        assertEquals(2, elegiveis.size());

        List<ResultadoTeto> resultados = CambioTesteSupport.aplicarTetoDiario(elegiveis);

        assertEquals(1, resultados.size());
        assertEquals("alimentacao", resultados.get(0).itemAvaliado().itemNormalizado().categoriaNormalizada());
        assertEquals(new BigDecimal("40.00"), resultados.get(0).valorReembolsavel());
    }

    // ---- 8. Item previamente inelegível recebido diretamente (defensivo) ------

    @Test
    @DisplayName("8 — DEFENSIVO: item inelegível recebido diretamente não participa, não consome saldo, não contamina item elegível da mesma chave")
    void itemInelegivelDireto_naoParticipaNemConsomeSaldo() {
        LocalDate data = LocalDate.of(2026, 7, 3);

        ItemValidado validadoInelegivel = new ItemValidado(
                1, "d-inel", data, "alimentacao", "Item", "F1",
                new BigDecimal("999.00"), true, null, List.of(),
                "BRL", BigDecimal.ONE, null, new BigDecimal("999.00"));
        ItemNormalizado normalizadoInelegivel =
                new ItemNormalizado(validadoInelegivel, new BigDecimal("999.00"), "alimentacao");
        ItemAvaliado inelegivel = new ItemAvaliado(
                normalizadoInelegivel,
                List.of(new Motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null)),
                false,
                new BigDecimal("0.00"));

        ItemValidado validadoElegivel = new ItemValidado(
                2, "d-eleg", data, "alimentacao", "Item", "F1",
                new BigDecimal("60.00"), true, null, List.of(),
                "BRL", BigDecimal.ONE, null, new BigDecimal("60.00"));
        ItemNormalizado normalizadoElegivel =
                new ItemNormalizado(validadoElegivel, new BigDecimal("60.00"), "alimentacao");
        ItemAvaliado elegivel = new ItemAvaliado(normalizadoElegivel, List.of(), true, null);

        List<ResultadoTeto> resultados = CambioTesteSupport.aplicarTetoDiario(List.of(inelegivel, elegivel));

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertSame(elegivel, resultado.itemAvaliado());
        assertEquals(new BigDecimal("60.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado.decisao());
    }
}
