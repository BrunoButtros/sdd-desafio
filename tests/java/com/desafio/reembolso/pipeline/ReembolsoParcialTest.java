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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-014 / CA-005 (spec 4.4, 4.5, 8.1, 8.4): o corte parcial no teto —
 * o excedente não é reembolsado, mas o item nunca é recusado nem zerado por
 * ultrapassagem — e a preservação do {@link ItemAvaliado} original.
 */
@DisplayName("Reembolso parcial — RN-014 / CA-005")
class ReembolsoParcialTest {

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

    // ---- 1. Alimentação R$ 61,00 sozinha -------------------------------------

    @Test
    @DisplayName("1 — CA-005: alimentação de R$ 61,00 sozinha rende R$ 60,00, nunca zero")
    void alimentacao61_rendeSessentaNuncaZero() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "61.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("60.00"), resultado.valorReembolsavel());
        assertTrue(resultado.valorReembolsavel().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(1, resultado.motivos().size());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_011, motivo.regra());
        assertNull(motivo.campo());
    }

    // ---- 2. Transporte urbano R$ 81,00 sozinho --------------------------------

    @Test
    @DisplayName("2 — transporte urbano de R$ 81,00 sozinho rende R$ 80,00 com motivo RN-012")
    void transporte81_rendeOitenta() {
        String json = envelopeComItens(
                item("d-001", "2026-07-06", "transporte_urbano", "Uber", "F1", "81.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("80.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_012, motivo.regra());
    }

    // ---- 3. Escala monetária ---------------------------------------------------

    @Test
    @DisplayName("3 — escala monetária: reembolsáveis mantêm escala 2, sem terceira casa decimal")
    void escalaMonetaria_semTerceiraCasa() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "33.34", true),
                item("d-002", "2026-07-03", "alimentacao", "Jantar", "F1", "33.34", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(2, resultados.size());
        assertEquals(2, resultados.get(0).valorReembolsavel().scale());
        assertEquals(2, resultados.get(1).valorReembolsavel().scale());
        assertEquals(new BigDecimal("33.34"), resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertEquals(new BigDecimal("26.66"), resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultados.get(1).decisao());
    }

    // ---- 4. Preservação do ItemAvaliado original -------------------------------

    @Test
    @DisplayName("4 — o ItemAvaliado original permanece elegível, com a mesma referência, sem motivo de recusa e sem alteração")
    void itemAvaliadoOriginal_permaneceElegivelEInalterado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "61.00", true)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        ItemAvaliado original = elegiveis.get(0);

        List<ResultadoTeto> resultados = CambioTesteSupport.aplicarTetoDiario(elegiveis);

        assertSame(original, resultados.get(0).itemAvaliado());
        assertTrue(original.elegivel());
        assertTrue(original.motivos().isEmpty());
        assertNull(original.valorReembolsavel());
    }

    // ---- 5. Corte parcial não usa RECUSADO -------------------------------------

    @Test
    @DisplayName("5 — corte parcial nunca produz decisão RECUSADO")
    void cortePercial_naoUsaRecusado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "61.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);
        assertNotEquals(Decisao.RECUSADO, resultados.get(0).decisao());
    }

    // ---- 6. Valores exatamente iguais aos limites -------------------------------

    @Test
    @DisplayName("6 — valores exatamente iguais aos limites permanecem integrais, sem motivo")
    void valoresIguaisAosLimites_permanecemIntegrais() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "60.00", true),
                item("d-002", "2026-07-06", "transporte_urbano", "Uber", "F1", "80.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertTrue(resultados.get(0).motivos().isEmpty());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());
        assertTrue(resultados.get(1).motivos().isEmpty());
    }

    // ---- 7. Contrato de aplicarCorte: limiteDisponivel deve ser positivo ------

    @Test
    @DisplayName("7 — aplicarCorte exige limiteDisponivel estritamente positivo: zero e negativo lançam IllegalArgumentException")
    void aplicarCorte_limiteNaoPositivo_lancaIllegalArgumentException() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "50.00", true)
        );
        ItemAvaliado item = elegiveisParaTetos(json).get(0);
        Motivo motivo = new Motivo(MotivoCodigo.TETO_DIARIO_APLICADO, RegraNegocio.RN_011, null);

        assertThrows(IllegalArgumentException.class,
                () -> AgregadorTetoDiario.aplicarCorte(item, new BigDecimal("0.00"), motivo));
        assertThrows(IllegalArgumentException.class,
                () -> AgregadorTetoDiario.aplicarCorte(item, new BigDecimal("-0.01"), motivo));
    }
}
