package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Cobre RN-015 / CA-006 (spec 4.4, 4.5, 8.1, 8.4): o consumo do saldo diário
 * em ordem crescente de {@code indiceEntrada}, independentemente da posição
 * dos itens na lista recebida, preservando a ordem de entrada na lista de
 * resultados devolvida.
 */
@DisplayName("Distribuição do teto diário — RN-015 / CA-006")
class DistribuicaoTetoTest {

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
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(idsVerificados);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);
        return SeletorElegiveis.selecionar(aposDuplicidade);
    }

    private static List<ResultadoTeto> resultados(String json) {
        return AgregadorTetoDiario.aplicar(elegiveisParaTetos(json));
    }

    // ---- 1. Cenário normativo --------------------------------------------------

    @Test
    @DisplayName("1 — CA-006: R$ 72,50 rende R$ 60,00 parcial RN-011; R$ 38,00 rende R$ 0,00 esgotado RN-015, não RECUSADO")
    void cenarioNormativo_72_50e38_00() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "72.50", true),
                item("d-002", "2026-07-03", "alimentacao", "Jantar", "F1", "38.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto primeiro = resultados.get(0);
        assertEquals(new BigDecimal("60.00"), primeiro.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, primeiro.decisao());
        assertEquals(RegraNegocio.RN_011, primeiro.motivos().get(0).regra());

        ResultadoTeto segundo = resultados.get(1);
        assertEquals(new BigDecimal("0.00"), segundo.valorReembolsavel());
        assertEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, segundo.decisao());
        assertEquals(RegraNegocio.RN_015, segundo.motivos().get(0).regra());
        assertNotEquals(Decisao.RECUSADO, segundo.decisao());
    }

    // ---- 2. Três itens ----------------------------------------------------------

    @Test
    @DisplayName("2 — distribuição com três itens: 30 integral, 30 parcial (de 40), 0 esgotado (de 10)")
    void tresItens_30_40_10() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "A", "F1", "30.00", true),
                item("d-002", "2026-07-03", "alimentacao", "B", "F1", "40.00", true),
                item("d-003", "2026-07-03", "alimentacao", "C", "F1", "10.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(new BigDecimal("30.00"), resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());

        assertEquals(new BigDecimal("30.00"), resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultados.get(1).decisao());

        assertEquals(new BigDecimal("0.00"), resultados.get(2).valorReembolsavel());
        assertEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, resultados.get(2).decisao());
    }

    // ---- 3. Vários itens após o esgotamento --------------------------------------

    @Test
    @DisplayName("3 — vários itens após o esgotamento recebem zero e apenas TETO_DIARIO_ESGOTADO")
    void variosItensAposEsgotamento_recebemApenasEsgotado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "A", "F1", "60.00", true),
                item("d-002", "2026-07-03", "alimentacao", "B", "F1", "5.00", true),
                item("d-003", "2026-07-03", "alimentacao", "C", "F1", "5.00", true),
                item("d-004", "2026-07-03", "alimentacao", "D", "F1", "5.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        for (int i = 1; i < 4; i++) {
            ResultadoTeto resultado = resultados.get(i);
            assertEquals(new BigDecimal("0.00"), resultado.valorReembolsavel());
            assertEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, resultado.decisao());
            assertEquals(1, resultado.motivos().size());
            assertEquals(MotivoCodigo.TETO_DIARIO_ESGOTADO, resultado.motivos().get(0).codigo());
        }
    }

    // ---- 4. Lista recebida fora de ordem ------------------------------------------

    @Test
    @DisplayName("4 — lista fora de ordem: consumo por indiceEntrada, saída preserva a ordem recebida")
    void listaForaDeOrdem_consomePorIndicePreservaOrdemRecebida() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "A", "F1", "20.00", true),
                item("d-002", "2026-07-03", "alimentacao", "B", "F1", "25.00", true),
                item("d-003", "2026-07-03", "alimentacao", "C", "F1", "25.00", true)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        ItemAvaliado indice1 = elegiveis.get(0);
        ItemAvaliado indice2 = elegiveis.get(1);
        ItemAvaliado indice3 = elegiveis.get(2);

        List<ItemAvaliado> foraDeOrdem = List.of(indice3, indice1, indice2);
        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(foraDeOrdem);

        assertEquals(3, resultados.size());

        assertEquals(3, resultados.get(0).itemAvaliado().itemNormalizado().item().getIndiceEntrada());
        assertEquals(new BigDecimal("15.00"), resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultados.get(0).decisao());

        assertEquals(1, resultados.get(1).itemAvaliado().itemNormalizado().item().getIndiceEntrada());
        assertEquals(new BigDecimal("20.00"), resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());

        assertEquals(2, resultados.get(2).itemAvaliado().itemNormalizado().item().getIndiceEntrada());
        assertEquals(new BigDecimal("25.00"), resultados.get(2).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(2).decisao());
    }

    // ---- 5. Grupos independentes intercalados --------------------------------------

    @Test
    @DisplayName("5 — grupos independentes intercalados na lista mantêm saldos próprios")
    void gruposIndependentesIntercalados_mantemSaldosProprios() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "A", "F1", "30.00", true),
                item("d-002", "2026-07-03", "transporte_urbano", "B", "F1", "40.00", true),
                item("d-003", "2026-07-04", "alimentacao", "C", "F1", "10.00", true),
                item("d-004", "2026-07-03", "transporte_urbano", "D", "F1", "50.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(4, resultados.size());
        assertEquals(new BigDecimal("30.00"), resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());

        assertEquals(new BigDecimal("40.00"), resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());

        assertEquals(new BigDecimal("10.00"), resultados.get(2).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(2).decisao());

        assertEquals(new BigDecimal("40.00"), resultados.get(3).valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultados.get(3).decisao());
    }

    // ---- 6. Reaplicação -------------------------------------------------------------

    @Test
    @DisplayName("6 — reaplicação: chamadas independentes com a mesma entrada produzem resultados equivalentes, sem estado compartilhado")
    void reaplicacao_resultadosEquivalentesSemEstadoCompartilhado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "A", "F1", "72.50", true),
                item("d-002", "2026-07-03", "alimentacao", "B", "F1", "38.00", true)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);

        List<ResultadoTeto> primeiraExecucao = AgregadorTetoDiario.aplicar(elegiveis);
        List<ResultadoTeto> segundaExecucao = AgregadorTetoDiario.aplicar(elegiveis);

        assertEquals(primeiraExecucao.size(), segundaExecucao.size());
        for (int i = 0; i < primeiraExecucao.size(); i++) {
            assertEquals(primeiraExecucao.get(i).valorReembolsavel(), segundaExecucao.get(i).valorReembolsavel());
            assertEquals(primeiraExecucao.get(i).decisao(), segundaExecucao.get(i).decisao());
            assertSame(primeiraExecucao.get(i).itemAvaliado(), segundaExecucao.get(i).itemAvaliado());
        }
    }

    // ---- 7. Imutabilidade -------------------------------------------------------------

    @Test
    @DisplayName("7 — imutabilidade: entrada preservada, saída e motivos não modificáveis, referências preservadas")
    void imutabilidade_entradaPreservadaSaidaNaoModificavel() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "A", "F1", "72.50", true),
                item("d-002", "2026-07-03", "alimentacao", "B", "F1", "38.00", true)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        List<ItemAvaliado> copiaAntes = new ArrayList<>(elegiveis);

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(elegiveis);

        assertEquals(copiaAntes, elegiveis);
        assertSame(copiaAntes.get(0), elegiveis.get(0));
        assertSame(copiaAntes.get(1), elegiveis.get(1));

        assertThrows(UnsupportedOperationException.class, () -> resultados.add(resultados.get(0)));
        assertThrows(UnsupportedOperationException.class, () -> resultados.get(0).motivos().add(null));

        assertSame(elegiveis.get(0), resultados.get(0).itemAvaliado());
        assertSame(elegiveis.get(1), resultados.get(1).itemAvaliado());
    }
}
