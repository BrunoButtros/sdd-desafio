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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-013 / CA-007 (spec 4.4, 4.5, 8.1, 8.2, 8.4, 8.5): o teto
 * individual de hospedagem, avaliado lançamento a lançamento, sem
 * agregação por data nem saldo compartilhado, e sem influência da
 * {@code descricao} sobre o limite.
 */
@DisplayName("Teto de hospedagem — RN-013 / CA-007")
class TetoHospedagemTest {

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
        return AgregadorTetoHospedagem.aplicar(elegiveisParaTetos(json));
    }

    private static ItemAvaliado itemAvaliadoHospedagem(int indiceEntrada, String id, LocalDate data,
                                                         String descricao, String fornecedor,
                                                         BigDecimal valor, boolean elegivel) {
        ItemValidado validado = new ItemValidado(
                indiceEntrada, id, data, "hospedagem", descricao, fornecedor,
                valor, true, null, List.of());
        ItemNormalizado normalizado = new ItemNormalizado(validado, valor, "hospedagem");
        List<Motivo> motivos = elegivel
                ? List.of()
                : List.of(new Motivo(MotivoCodigo.NOTA_FISCAL_AUSENTE, RegraNegocio.RN_009, null));
        BigDecimal valorReembolsavel = elegivel ? null : new BigDecimal("0.00");
        return new ItemAvaliado(normalizado, motivos, elegivel, valorReembolsavel);
    }

    // ---- 1. Cenário normativo --------------------------------------------------

    @Test
    @DisplayName("1 — CA-007: hospedagem de R$ 480,00 \"2 diarias\" rende R$ 250,00 parcial com RN-013")
    void cenarioNormativo_480_rendeDuzentosCinquenta() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "2 diarias", "Hotel A", "480.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
        assertEquals(2, resultado.valorReembolsavel().scale());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(1, resultado.motivos().size());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_013, motivo.regra());
        assertNull(motivo.campo());
        assertNotEquals(Decisao.RECUSADO, resultado.decisao());
        assertNotEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, resultado.decisao());
    }

    // ---- 2. Descrição não altera o teto -----------------------------------------

    @Test
    @DisplayName("2 — descrição \"2 diarias\" não altera o teto de R$ 250,00")
    void descricaoDuasDiarias_naoAlteraTeto() {
        assertDescricaoNaoAlteraTeto("2 diarias");
    }

    @Test
    @DisplayName("2 — descrição \"3 noites\" não altera o teto de R$ 250,00")
    void descricaoTresNoites_naoAlteraTeto() {
        assertDescricaoNaoAlteraTeto("3 noites");
    }

    @Test
    @DisplayName("2 — descrição \"uma semana no hotel\" não altera o teto de R$ 250,00")
    void descricaoUmaSemanaNoHotel_naoAlteraTeto() {
        assertDescricaoNaoAlteraTeto("uma semana no hotel");
    }

    @Test
    @DisplayName("2 — descrição \"estadia\" não altera o teto de R$ 250,00")
    void descricaoEstadia_naoAlteraTeto() {
        assertDescricaoNaoAlteraTeto("estadia");
    }

    private static void assertDescricaoNaoAlteraTeto(String descricao) {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", descricao, "Hotel A", "480.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, resultado.motivos().get(0).codigo());
        assertEquals(RegraNegocio.RN_013, resultado.motivos().get(0).regra());
    }

    // ---- 3. Abaixo do teto -------------------------------------------------------

    @Test
    @DisplayName("3 — hospedagem de R$ 249,99 é integral, sem motivos")
    void abaixoDoTeto_integralSemMotivos() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "Estadia", "Hotel A", "249.99", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("249.99"), resultado.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado.decisao());
        assertTrue(resultado.motivos().isEmpty());
    }

    // ---- 4. Exatamente no teto ----------------------------------------------------

    @Test
    @DisplayName("4 — hospedagem de R$ 250,00 é integral, não parcial, sem motivos")
    void exatamenteNoTeto_integralNaoParcial() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "Estadia", "Hotel A", "250.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado.decisao());
        assertNotEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertTrue(resultado.motivos().isEmpty());
    }

    // ---- 5. Um centavo acima -------------------------------------------------------

    @Test
    @DisplayName("5 — hospedagem de R$ 250,01 é parcial, rende R$ 250,00 com motivo RN-013")
    void umCentavoAcima_parcial() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "Estadia", "Hotel A", "250.01", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(RegraNegocio.RN_013, resultado.motivos().get(0).regra());
    }

    // ---- 6. Duas hospedagens na mesma data -----------------------------------------

    @Test
    @DisplayName("6 — duas hospedagens de R$ 480,00 na mesma data rendem R$ 250,00 cada, somando R$ 500,00")
    void duasHospedagensMesmaData_naoCompartilhamSaldo() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "Estadia 1", "Hotel A", "480.00", true),
                item("d-002", "2026-07-14", "hospedagem", "Estadia 2", "Hotel B", "480.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(2, resultados.size());
        ResultadoTeto primeiro = resultados.get(0);
        ResultadoTeto segundo = resultados.get(1);
        assertEquals(new BigDecimal("250.00"), primeiro.valorReembolsavel());
        assertEquals(new BigDecimal("250.00"), segundo.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, primeiro.decisao());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, segundo.decisao());
        assertEquals(new BigDecimal("500.00"), primeiro.valorReembolsavel().add(segundo.valorReembolsavel()));
        assertNotEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, primeiro.decisao());
        assertNotEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, segundo.decisao());
    }

    // ---- 7. Três hospedagens abaixo do teto -----------------------------------------

    @Test
    @DisplayName("7 — três hospedagens de R$ 230,00 rendem R$ 230,00 cada, todas integrais, somando R$ 690,00")
    void tresHospedagensAbaixoDoTeto_semSaldoCompartilhado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "Estadia 1", "Hotel A", "230.00", true),
                item("d-002", "2026-07-15", "hospedagem", "Estadia 2", "Hotel B", "230.00", true),
                item("d-003", "2026-07-16", "hospedagem", "Estadia 3", "Hotel C", "230.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(3, resultados.size());
        BigDecimal soma = BigDecimal.ZERO;
        for (ResultadoTeto resultado : resultados) {
            assertEquals(new BigDecimal("230.00"), resultado.valorReembolsavel());
            assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado.decisao());
            soma = soma.add(resultado.valorReembolsavel());
        }
        assertEquals(new BigDecimal("690.00"), soma);
    }

    // ---- 8. Uma hospedagem de R$ 690,00 -----------------------------------------------

    @Test
    @DisplayName("8 — uma única hospedagem de R$ 690,00 rende somente R$ 250,00, sem multiplicar pelo texto da descrição")
    void umaHospedagemDeSeiscentosNoventa_rendeSomenteODuzentosCinquenta() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "3 noites", "Hotel A", "690.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
    }

    // ---- 9. Datas não interferem ----------------------------------------------------

    @Test
    @DisplayName("9 — hospedagens em datas diferentes são avaliadas isoladamente, sem saldo transportado")
    void datasDiferentes_avaliadasIsoladamente() {
        String json = envelopeComItens(
                item("d-001", "2026-07-01", "hospedagem", "Estadia 1", "Hotel A", "480.00", true),
                item("d-002", "2026-07-31", "hospedagem", "Estadia 2", "Hotel B", "100.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(2, resultados.size());
        assertEquals(new BigDecimal("250.00"), resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertEquals(new BigDecimal("100.00"), resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());
    }

    // ---- 10. Ordem recebida ----------------------------------------------------------

    @Test
    @DisplayName("10 — lista recebida fora de ordem de indiceEntrada preserva a ordem recebida na saída")
    void listaForaDeOrdem_preservaOrdemRecebida() {
        String json = envelopeComItens(
                item("d-001", "2026-07-01", "hospedagem", "Estadia 1", "Hotel A", "480.00", true),
                item("d-002", "2026-07-02", "hospedagem", "Estadia 2", "Hotel B", "100.00", true),
                item("d-003", "2026-07-03", "hospedagem", "Estadia 3", "Hotel C", "230.00", true)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        ItemAvaliado indice1 = elegiveis.get(0);
        ItemAvaliado indice2 = elegiveis.get(1);
        ItemAvaliado indice3 = elegiveis.get(2);

        List<ItemAvaliado> foraDeOrdem = List.of(indice3, indice1, indice2);
        List<ResultadoTeto> resultados = AgregadorTetoHospedagem.aplicar(foraDeOrdem);

        assertEquals(3, resultados.size());
        assertEquals(3, resultados.get(0).itemAvaliado().itemNormalizado().item().getIndiceEntrada());
        assertEquals(new BigDecimal("230.00"), resultados.get(0).valorReembolsavel());
        assertEquals(1, resultados.get(1).itemAvaliado().itemNormalizado().item().getIndiceEntrada());
        assertEquals(new BigDecimal("250.00"), resultados.get(1).valorReembolsavel());
        assertEquals(2, resultados.get(2).itemAvaliado().itemNormalizado().item().getIndiceEntrada());
        assertEquals(new BigDecimal("100.00"), resultados.get(2).valorReembolsavel());
    }

    // ---- 11. Categorias mistas ---------------------------------------------------------

    @Test
    @DisplayName("11 — categorias mistas: somente hospedagem aparece no resultado; alimentação e transporte permanecem inalterados")
    void categoriasMistas_somenteHospedagemNoResultado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "F1", "40.00", true),
                item("d-002", "2026-07-06", "transporte_urbano", "Uber", "F1", "50.00", true),
                item("d-003", "2026-07-14", "hospedagem", "Estadia", "Hotel A", "480.00", true)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        assertEquals(3, elegiveis.size());
        List<ItemAvaliado> copiaAntes = new ArrayList<>(elegiveis);

        List<ResultadoTeto> resultados = AgregadorTetoHospedagem.aplicar(elegiveis);

        assertEquals(1, resultados.size());
        assertEquals("hospedagem", resultados.get(0).itemAvaliado().itemNormalizado().categoriaNormalizada());
        assertEquals(new BigDecimal("250.00"), resultados.get(0).valorReembolsavel());

        assertEquals(copiaAntes, elegiveis);
        assertSame(copiaAntes.get(0), elegiveis.get(0));
        assertSame(copiaAntes.get(1), elegiveis.get(1));
        assertTrue(elegiveis.get(0).motivos().isEmpty());
        assertTrue(elegiveis.get(1).motivos().isEmpty());
    }

    // ---- 12. Item inelegível ------------------------------------------------------------

    @Test
    @DisplayName("12a — no fluxo canônico, hospedagem recusada por nota fiscal é removida pelo seletor antes do teto")
    void itemInelegivelPorNotaFiscal_removidoAntesDoTeto() {
        String json = envelopeComItens(
                item("d-001", "2026-07-22", "hospedagem", "Estadia", "Hotel A", "690.00", false)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        assertTrue(elegiveis.isEmpty());

        List<ResultadoTeto> resultados = AgregadorTetoHospedagem.aplicar(elegiveis);
        assertTrue(resultados.isEmpty());
    }

    @Test
    @DisplayName("12b — DEFENSIVO: item de hospedagem inelegível recebido diretamente não aparece no resultado nem contamina hospedagem elegível")
    void itemInelegivelDireto_naoParticipaNemContamina() {
        LocalDate data = LocalDate.of(2026, 7, 22);
        ItemAvaliado inelegivel = itemAvaliadoHospedagem(
                1, "d-inel", data, "Estadia", "Hotel A", new BigDecimal("690.00"), false);
        ItemAvaliado elegivel = itemAvaliadoHospedagem(
                2, "d-eleg", data, "Estadia", "Hotel B", new BigDecimal("480.00"), true);

        List<ResultadoTeto> resultados = AgregadorTetoHospedagem.aplicar(List.of(inelegivel, elegivel));

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertSame(elegivel, resultado.itemAvaliado());
        assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
    }

    // ---- 13. Nunca esgotado ---------------------------------------------------------------

    @Test
    @DisplayName("13 — várias hospedagens acima do teto rendem R$ 250,00 cada, nunca esgotado")
    void variasHospedagensAcimaDoTeto_nuncaEsgotado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "Estadia 1", "Hotel A", "480.00", true),
                item("d-002", "2026-07-14", "hospedagem", "Estadia 2", "Hotel B", "500.00", true),
                item("d-003", "2026-07-14", "hospedagem", "Estadia 3", "Hotel C", "999.00", true)
        );
        List<ResultadoTeto> resultados = resultados(json);

        assertEquals(3, resultados.size());
        for (ResultadoTeto resultado : resultados) {
            assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
            assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
            assertNotEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, resultado.decisao());
            for (Motivo motivo : resultado.motivos()) {
                assertNotEquals(MotivoCodigo.TETO_DIARIO_ESGOTADO, motivo.codigo());
            }
        }
    }

    // ---- 14. Imutabilidade e referências -------------------------------------------------------

    @Test
    @DisplayName("14 — imutabilidade: entrada preservada, saída e motivos não modificáveis, referências preservadas")
    void imutabilidade_entradaPreservadaSaidaNaoModificavel() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "Estadia 1", "Hotel A", "480.00", true),
                item("d-002", "2026-07-15", "hospedagem", "Estadia 2", "Hotel B", "100.00", true)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        List<ItemAvaliado> copiaAntes = new ArrayList<>(elegiveis);

        List<ResultadoTeto> resultados = AgregadorTetoHospedagem.aplicar(elegiveis);

        assertEquals(copiaAntes, elegiveis);
        assertSame(copiaAntes.get(0), elegiveis.get(0));
        assertSame(copiaAntes.get(1), elegiveis.get(1));

        assertThrows(UnsupportedOperationException.class, () -> resultados.add(resultados.get(0)));
        assertThrows(UnsupportedOperationException.class, () -> resultados.get(0).motivos().add(null));

        assertSame(elegiveis.get(0), resultados.get(0).itemAvaliado());
        assertSame(elegiveis.get(1), resultados.get(1).itemAvaliado());
    }

    // ---- 15. Lista vazia --------------------------------------------------------------------------

    @Test
    @DisplayName("15 — lista vazia retorna lista vazia e não modificável")
    void listaVazia_retornaListaVaziaNaoModificavel() {
        List<ResultadoTeto> resultados = AgregadorTetoHospedagem.aplicar(List.of());

        assertTrue(resultados.isEmpty());
        assertThrows(UnsupportedOperationException.class,
                () -> resultados.add(null));
    }

    // ---- 16. Reaplicação -----------------------------------------------------------------------------

    @Test
    @DisplayName("16 — reaplicação: chamadas independentes com a mesma entrada produzem resultados equivalentes, sem estado compartilhado")
    void reaplicacao_resultadosEquivalentesSemEstadoCompartilhado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-14", "hospedagem", "Estadia 1", "Hotel A", "480.00", true),
                item("d-002", "2026-07-15", "hospedagem", "Estadia 2", "Hotel B", "230.00", true)
        );
        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);

        List<ResultadoTeto> primeiraExecucao = AgregadorTetoHospedagem.aplicar(elegiveis);
        List<ResultadoTeto> segundaExecucao = AgregadorTetoHospedagem.aplicar(elegiveis);

        assertEquals(primeiraExecucao.size(), segundaExecucao.size());
        for (int i = 0; i < primeiraExecucao.size(); i++) {
            assertEquals(primeiraExecucao.get(i).valorReembolsavel(), segundaExecucao.get(i).valorReembolsavel());
            assertEquals(primeiraExecucao.get(i).decisao(), segundaExecucao.get(i).decisao());
            assertEquals(primeiraExecucao.get(i).motivos(), segundaExecucao.get(i).motivos());
            assertSame(primeiraExecucao.get(i).itemAvaliado(), segundaExecucao.get(i).itemAvaliado());
        }
    }
}
