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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-017 / CA-002 (spec 4.3, 4.7, 8.1 passo 10): toda posição da
 * entrada produz exatamente um registro de saída, na ordem da entrada, sem
 * que nenhum item desapareça — inclusive os recusados, os esgotados e as
 * ocorrências de duplicidade. Exercita o {@link CompositorSaida} sempre
 * através do pipeline real (T-004 a T-014), nunca construindo o resultado
 * esperado a partir do próprio motor.
 */
@DisplayName("Composição da saída — RN-017 / CA-002")
class ComposicaoSaidaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final String JANELA_JULHO =
            "\"periodo\": { \"inicio\": \"2026-07-01\", \"fim\": \"2026-07-31\" },";

    // ---- Infraestrutura de leitura e pipeline ---------------------------------

    private static JsonNode raiz(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonNode lerArquivoExemplo() throws IOException {
        return MAPPER.readTree(Path.of("exemplos", "despesas-exemplo.json").toFile());
    }

    /**
     * Pipeline completo, exatamente conforme descrito para os testes de
     * composição: leitura já validada até {@link CompositorSaida#compor}.
     */
    private static List<ResultadoItem> pipelineCompleto(JsonNode raizJson) {
        Envelope envelope = ValidadorEnvelope.validar(raizJson);

        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(idsVerificados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = CambioTesteSupport.avaliarLista(normalizados, envelope);

        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);

        List<ItemAvaliado> elegiveisParaTetos = SeletorElegiveis.selecionar(aposDuplicidade);
        List<ResultadoTeto> resultadosDiarios = CambioTesteSupport.aplicarTetoDiario(elegiveisParaTetos);
        List<ResultadoTeto> resultadosHospedagem = CambioTesteSupport.aplicarTetoIndividual(elegiveisParaTetos);

        return CompositorSaida.compor(avaliados, aposDuplicidade, resultadosDiarios, resultadosHospedagem);
    }

    private static List<ResultadoItem> pipelineCompleto(String json) {
        return pipelineCompleto(raiz(json));
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

    private static ResultadoItem porIndice(List<ResultadoItem> resultados, int indiceEntrada) {
        return resultados.stream()
                .filter(r -> r.indiceEntrada() == indiceEntrada)
                .findFirst()
                .orElseThrow(() -> new AssertionError("nenhum resultado para índice " + indiceEntrada));
    }

    // ---- 1. Arquivo normativo com 14 posições ---------------------------------

    @Test
    @DisplayName("1 — exemplos/despesas-exemplo.json produz 14 registros, ordem 1..14, coincidindo com a tabela 4.7")
    void arquivoNormativo_14Posicoes_coincideComTabela47() throws IOException {
        List<ResultadoItem> resultados = pipelineCompleto(lerArquivoExemplo());

        assertEquals(14, resultados.size(), "exatamente 14 resultados, nenhum item desaparece");
        for (int i = 0; i < resultados.size(); i++) {
            assertEquals(i + 1, resultados.get(i).indiceEntrada(), "ordem crescente de indiceEntrada");
        }

        // índice 1 — d-001 — PARCIAL 60,00 — TETO_DIARIO_APLICADO
        ResultadoItem r1 = porIndice(resultados, 1);
        assertEquals("d-001", r1.id());
        assertEquals(new BigDecimal("60.00"), r1.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, r1.decisao());
        assertEquals(1, r1.motivos().size());
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, r1.motivos().get(0).codigo());

        // índice 2 — d-002 — ESGOTADO 0,00 — TETO_DIARIO_ESGOTADO (não é RECUSADO)
        ResultadoItem r2 = porIndice(resultados, 2);
        assertEquals("d-002", r2.id());
        assertEquals(new BigDecimal("0.00"), r2.valorReembolsavel());
        assertEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, r2.decisao());
        assertEquals(1, r2.motivos().size());
        assertEquals(MotivoCodigo.TETO_DIARIO_ESGOTADO, r2.motivos().get(0).codigo());

        // índice 3 — d-003 — PARCIAL 80,00 — TETO_DIARIO_APLICADO
        ResultadoItem r3 = porIndice(resultados, 3);
        assertEquals("d-003", r3.id());
        assertEquals(new BigDecimal("80.00"), r3.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, r3.decisao());
        assertEquals(1, r3.motivos().size());
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, r3.motivos().get(0).codigo());

        // índice 4 — d-004 — RECUSADO 0,00 — NOTA_FISCAL_AUSENTE
        ResultadoItem r4 = porIndice(resultados, 4);
        assertEquals("d-004", r4.id());
        assertEquals(new BigDecimal("0.00"), r4.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, r4.decisao());
        assertEquals(1, r4.motivos().size());
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, r4.motivos().get(0).codigo());

        // índice 5 — d-005 — RECUSADO 0,00 — CATEGORIA_FORA_POLITICA
        ResultadoItem r5 = porIndice(resultados, 5);
        assertEquals("d-005", r5.id());
        assertEquals(new BigDecimal("0.00"), r5.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, r5.decisao());
        assertEquals(1, r5.motivos().size());
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, r5.motivos().get(0).codigo());

        // índice 6 — d-006 — INTEGRAL 54,90 — motivos vazios
        ResultadoItem r6 = porIndice(resultados, 6);
        assertEquals("d-006", r6.id());
        assertEquals(new BigDecimal("54.90"), r6.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, r6.decisao());
        assertTrue(r6.motivos().isEmpty(), "item integral não tem motivo artificial de aprovação");

        // índice 7 — d-007 — RECUSADO 0,00 — DUPLICIDADE
        ResultadoItem r7 = porIndice(resultados, 7);
        assertEquals("d-007", r7.id());
        assertEquals(new BigDecimal("0.00"), r7.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, r7.decisao());
        assertEquals(1, r7.motivos().size());
        assertEquals(MotivoCodigo.DUPLICIDADE, r7.motivos().get(0).codigo());

        // índice 8 — d-008 — RECUSADO 0,00 — FORA_COMPETENCIA
        ResultadoItem r8 = porIndice(resultados, 8);
        assertEquals("d-008", r8.id());
        assertEquals(new BigDecimal("0.00"), r8.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, r8.decisao());
        assertEquals(1, r8.motivos().size());
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, r8.motivos().get(0).codigo());

        // índice 9 — d-009 — RECUSADO 0,00 — VALOR_NAO_POSITIVO
        ResultadoItem r9 = porIndice(resultados, 9);
        assertEquals("d-009", r9.id());
        assertEquals(new BigDecimal("0.00"), r9.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, r9.decisao());
        assertEquals(1, r9.motivos().size());
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, r9.motivos().get(0).codigo());

        // índice 10 — d-010 — PARCIAL 250,00 — TETO_HOSPEDAGEM_APLICADO
        ResultadoItem r10 = porIndice(resultados, 10);
        assertEquals("d-010", r10.id());
        assertEquals(new BigDecimal("250.00"), r10.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, r10.decisao());
        assertEquals(1, r10.motivos().size());
        assertEquals(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, r10.motivos().get(0).codigo());

        // índice 11 — d-011 — informado 33.333, normalizado 33,33, reembolsável 33,33
        ResultadoItem r11 = porIndice(resultados, 11);
        assertEquals("d-011", r11.id());
        assertEquals(0, new BigDecimal("33.333").compareTo(r11.valorInformado().decimalValue()));
        assertEquals(new BigDecimal("33.33"), r11.valorNormalizado());
        assertEquals(new BigDecimal("33.33"), r11.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, r11.decisao());
        assertTrue(r11.motivos().isEmpty());

        // índice 12 — d-012 — INTEGRAL 47,20
        ResultadoItem r12 = porIndice(resultados, 12);
        assertEquals("d-012", r12.id());
        assertEquals(new BigDecimal("47.20"), r12.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, r12.decisao());
        assertTrue(r12.motivos().isEmpty());

        // índice 13 — d-013 — RECUSADO 0,00 — NOTA_FISCAL_AUSENTE
        ResultadoItem r13 = porIndice(resultados, 13);
        assertEquals("d-013", r13.id());
        assertEquals(new BigDecimal("0.00"), r13.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, r13.decisao());
        assertEquals(1, r13.motivos().size());
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, r13.motivos().get(0).codigo());

        // índice 14 — d-014 — PARCIAL 60,00 — TETO_DIARIO_APLICADO
        ResultadoItem r14 = porIndice(resultados, 14);
        assertEquals("d-014", r14.id());
        assertEquals(new BigDecimal("60.00"), r14.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, r14.decisao());
        assertEquals(1, r14.motivos().size());
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, r14.motivos().get(0).codigo());
    }

    // ---- 2. Elemento que não é objeto -----------------------------------------

    @Test
    @DisplayName("2 — elemento que não é objeto produz um único registro RECUSADO com ITEM_TIPO_INVALIDO, restante processado")
    void elementoNaoObjeto_registroUnicoItemTipoInvalido() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "Restaurante X", "50.00", true),
                "\"despesa\"",
                item("d-003", "2026-07-04", "alimentacao", "Jantar", "Restaurante Y", "30.00", true)
        );

        List<ResultadoItem> resultados = pipelineCompleto(json);

        assertEquals(3, resultados.size());

        ResultadoItem meio = porIndice(resultados, 2);
        assertNull(meio.id());
        assertNull(meio.valorInformado());
        assertNull(meio.valorNormalizado());
        assertEquals(new BigDecimal("0.00"), meio.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, meio.decisao());
        assertEquals(1, meio.motivos().size());
        Motivo motivo = meio.motivos().get(0);
        assertEquals(MotivoCodigo.ITEM_TIPO_INVALIDO, motivo.codigo());
        assertEquals(RegraNegocio.RN_002, motivo.regra());
        assertNull(motivo.campo());

        assertEquals("d-001", porIndice(resultados, 1).id());
        assertEquals("d-003", porIndice(resultados, 3).id());
    }

    // ---- 3. Valor informado de tipo inválido ----------------------------------

    @Test
    @DisplayName("3a — valor \"72,50\" (texto): valorInformado preserva o texto, decisão RECUSADO, motivo estrutural correto")
    void valorTextoInvalido_valorInformadoPreservado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "Restaurante X", "\"72,50\"", true));

        List<ResultadoItem> resultados = pipelineCompleto(json);

        ResultadoItem r = porIndice(resultados, 1);
        assertTrue(r.valorInformado().isTextual());
        assertEquals("72,50", r.valorInformado().asText());
        assertNull(r.valorNormalizado());
        assertEquals(new BigDecimal("0.00"), r.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, r.decisao());
        assertEquals(1, r.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, r.motivos().get(0).codigo());
        assertEquals(CampoCanonico.VALOR, r.motivos().get(0).campo());
    }

    @Test
    @DisplayName("3b — valor true (booleano): valorInformado continua booleano, decisão RECUSADO")
    void valorBooleano_valorInformadoPreservado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-03", "alimentacao", "Almoco", "Restaurante X", "true", true));

        List<ResultadoItem> resultados = pipelineCompleto(json);

        ResultadoItem r = porIndice(resultados, 1);
        assertTrue(r.valorInformado().isBoolean());
        assertTrue(r.valorInformado().asBoolean());
        assertNull(r.valorNormalizado());
        assertEquals(new BigDecimal("0.00"), r.valorReembolsavel());
        assertEquals(Decisao.RECUSADO, r.decisao());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, r.motivos().get(0).codigo());
        assertEquals(CampoCanonico.VALOR, r.motivos().get(0).campo());
    }

    // ---- 4. ID nulo ou inválido ------------------------------------------------

    @Test
    @DisplayName("4 — id ausente: item permanece na posição original, id nulo, RECUSADO")
    void idAusente_permaneceNaPosicaoComIdNulo() {
        String json = raizComItemSemId();

        List<ResultadoItem> resultados = pipelineCompleto(json);

        assertEquals(1, resultados.size());
        ResultadoItem r = resultados.get(0);
        assertEquals(1, r.indiceEntrada());
        assertNull(r.id());
        assertEquals(Decisao.RECUSADO, r.decisao());
        assertEquals(new BigDecimal("0.00"), r.valorReembolsavel());
    }

    private static String raizComItemSemId() {
        return """
                {
                  %s
                  "despesas": [
                    { "data": "2026-07-03", "categoria": "alimentacao", "descricao": "Almoco",
                      "fornecedor": "Restaurante X", "valor": 50.00, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(JANELA_JULHO);
    }

    // ---- 5. IDs duplicados ------------------------------------------------------

    @Test
    @DisplayName("5 — três ocorrências do mesmo id válido: todas permanecem, todas RECUSADO com ID_DUPLICADO, índices distintos")
    void idsDuplicados_todasPermanecemRecusadasComIdDuplicado() {
        String json = envelopeComItens(
                item("d-100", "2026-07-03", "alimentacao", "A", "F1", "50.00", true),
                item("d-100", "2026-07-04", "transporte_urbano", "B", "F2", "30.00", true),
                item("d-100", "2026-07-05", "hospedagem", "C", "F3", "200.00", true)
        );

        List<ResultadoItem> resultados = pipelineCompleto(json);

        assertEquals(3, resultados.size());
        for (int i = 1; i <= 3; i++) {
            ResultadoItem r = porIndice(resultados, i);
            assertEquals(i, r.indiceEntrada());
            assertEquals("d-100", r.id());
            assertEquals(Decisao.RECUSADO, r.decisao());
            assertEquals(new BigDecimal("0.00"), r.valorReembolsavel());
            assertTrue(r.motivos().stream().anyMatch(m -> m.codigo() == MotivoCodigo.ID_DUPLICADO));
        }
    }

    // ---- 6. Duplicidade econômica ------------------------------------------------

    @Test
    @DisplayName("6 — dois itens economicamente idênticos exceto o id: primeiro elegível com teto, segundo RECUSADO com DUPLICIDADE")
    void duplicidadeEconomica_primeiroElegivelSegundoRecusado() {
        String json = envelopeComItens(
                item("d-006", "2026-07-09", "alimentacao", "Almoco", "Bistro Central", "54.90", true),
                item("d-007", "2026-07-09", "alimentacao", "Almoco", "Bistro Central", "54.90", true)
        );

        List<ResultadoItem> resultados = pipelineCompleto(json);

        assertEquals(2, resultados.size());

        ResultadoItem primeiro = porIndice(resultados, 1);
        assertEquals("d-006", primeiro.id());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, primeiro.decisao());
        assertEquals(new BigDecimal("54.90"), primeiro.valorReembolsavel());

        ResultadoItem segundo = porIndice(resultados, 2);
        assertEquals("d-007", segundo.id());
        assertEquals(Decisao.RECUSADO, segundo.decisao());
        assertEquals(new BigDecimal("0.00"), segundo.valorReembolsavel());
        assertEquals(1, segundo.motivos().size());
        assertEquals(MotivoCodigo.DUPLICIDADE, segundo.motivos().get(0).codigo());
    }

    // ---- 7. Lista vazia -----------------------------------------------------------

    @Test
    @DisplayName("7 — despesas vazia: resultado vazio e não modificável")
    void listaVazia_resultadoVazioENaoModificavel() {
        String json = """
                {
                  %s
                  "despesas": []
                }
                """.formatted(JANELA_JULHO);

        List<ResultadoItem> resultados = pipelineCompleto(json);

        assertTrue(resultados.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> resultados.add(null));
    }

    // ---- 8. Entrada auxiliar fora de ordem -----------------------------------------

    @Test
    @DisplayName("8 — listas auxiliares recebidas fora de ordem produzem a mesma saída, na ordem original")
    void entradaAuxiliarForaDeOrdem_composicaoUsaIndiceEntrada() throws IOException {
        Envelope envelope = ValidadorEnvelope.validar(lerArquivoExemplo());
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(idsVerificados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = CambioTesteSupport.avaliarLista(normalizados, envelope);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);
        List<ItemAvaliado> elegiveisParaTetos = SeletorElegiveis.selecionar(aposDuplicidade);
        List<ResultadoTeto> resultadosDiarios = CambioTesteSupport.aplicarTetoDiario(elegiveisParaTetos);
        List<ResultadoTeto> resultadosHospedagem = CambioTesteSupport.aplicarTetoIndividual(elegiveisParaTetos);

        List<ResultadoItem> canonico =
                CompositorSaida.compor(avaliados, aposDuplicidade, resultadosDiarios, resultadosHospedagem);

        List<ItemAvaliado> avaliadosForaDeOrdem = new ArrayList<>(avaliados);
        Collections.reverse(avaliadosForaDeOrdem);
        List<ItemAvaliado> aposDuplicidadeForaDeOrdem = new ArrayList<>(aposDuplicidade);
        Collections.reverse(aposDuplicidadeForaDeOrdem);
        List<ResultadoTeto> resultadosDiariosForaDeOrdem = new ArrayList<>(resultadosDiarios);
        Collections.reverse(resultadosDiariosForaDeOrdem);
        List<ResultadoTeto> resultadosHospedagemForaDeOrdem = new ArrayList<>(resultadosHospedagem);
        Collections.reverse(resultadosHospedagemForaDeOrdem);

        List<ResultadoItem> comAuxiliaresForaDeOrdem = CompositorSaida.compor(
                avaliadosForaDeOrdem, aposDuplicidadeForaDeOrdem,
                resultadosDiariosForaDeOrdem, resultadosHospedagemForaDeOrdem);

        assertEquals(canonico, comAuxiliaresForaDeOrdem);
        for (int i = 0; i < comAuxiliaresForaDeOrdem.size(); i++) {
            assertEquals(i + 1, comAuxiliaresForaDeOrdem.get(i).indiceEntrada());
        }
    }

    // ---- 9. Imutabilidade -----------------------------------------------------------

    @Test
    @DisplayName("9 — nenhuma lista de entrada é alterada; saída e motivos não são modificáveis")
    void imutabilidade_entradasPreservadasSaidaNaoModificavel() throws IOException {
        Envelope envelope = ValidadorEnvelope.validar(lerArquivoExemplo());
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(idsVerificados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = CambioTesteSupport.avaliarLista(normalizados, envelope);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);
        List<ItemAvaliado> elegiveisParaTetos = SeletorElegiveis.selecionar(aposDuplicidade);
        List<ResultadoTeto> resultadosDiarios = CambioTesteSupport.aplicarTetoDiario(elegiveisParaTetos);
        List<ResultadoTeto> resultadosHospedagem = CambioTesteSupport.aplicarTetoIndividual(elegiveisParaTetos);

        List<ItemAvaliado> avaliadosAntes = new ArrayList<>(avaliados);
        List<ItemAvaliado> aposDuplicidadeAntes = new ArrayList<>(aposDuplicidade);
        List<ResultadoTeto> diariosAntes = new ArrayList<>(resultadosDiarios);
        List<ResultadoTeto> hospedagemAntes = new ArrayList<>(resultadosHospedagem);

        List<ResultadoItem> resultados =
                CompositorSaida.compor(avaliados, aposDuplicidade, resultadosDiarios, resultadosHospedagem);

        assertEquals(avaliadosAntes, avaliados);
        assertEquals(aposDuplicidadeAntes, aposDuplicidade);
        assertEquals(diariosAntes, resultadosDiarios);
        assertEquals(hospedagemAntes, resultadosHospedagem);

        assertThrows(UnsupportedOperationException.class, () -> resultados.add(resultados.get(0)));

        ResultadoItem r1 = porIndice(resultados, 1);
        assertFalse(r1.motivos().isEmpty(), "índice 1 tem TETO_DIARIO_APLICADO");
        assertThrows(UnsupportedOperationException.class,
                () -> r1.motivos().add(new Motivo(MotivoCodigo.DUPLICIDADE, RegraNegocio.RN_010, null)));

        assertEquals(0, new BigDecimal("72.50").compareTo(r1.valorInformado().decimalValue()),
                "valorInformado preserva tipo e conteúdo do JSON original");
    }

    // ---- 10. Reaplicação --------------------------------------------------------------

    @Test
    @DisplayName("10 — duas execuções independentes produzem resultados equivalentes")
    void reaplicacao_resultadosEquivalentes() throws IOException {
        List<ResultadoItem> primeira = pipelineCompleto(lerArquivoExemplo());
        List<ResultadoItem> segunda = pipelineCompleto(lerArquivoExemplo());

        assertEquals(primeira, segunda);
    }

    // ---- 11. Inconsistências ------------------------------------------------------------

    private static ItemValidado itemValidadoMinimo(int indiceEntrada, String id, LocalDate data,
                                                     String categoria, BigDecimal valor) {
        return new ItemValidado(indiceEntrada, id, data, categoria, "descricao", "fornecedor",
                valor, true, DecimalNode.valueOf(valor), List.of(),
                "BRL", BigDecimal.ONE, null, valor);
    }

    private static ItemAvaliado itemAvaliadoElegivel(int indiceEntrada, String categoria, BigDecimal valor) {
        ItemValidado validado = itemValidadoMinimo(
                indiceEntrada, "d-" + indiceEntrada, LocalDate.of(2026, 7, 10), categoria, valor);
        ItemNormalizado normalizado = Normalizador.normalizar(validado);
        return new ItemAvaliado(normalizado, List.of(), true, null);
    }

    private static ItemAvaliado itemAvaliadoInelegivel(int indiceEntrada, String categoria, BigDecimal valor) {
        ItemValidado validado = itemValidadoMinimo(
                indiceEntrada, "d-" + indiceEntrada, LocalDate.of(2026, 7, 10), categoria, valor);
        ItemNormalizado normalizado = Normalizador.normalizar(validado);
        List<Motivo> motivos = List.of(new Motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null));
        return new ItemAvaliado(normalizado, motivos, false, new BigDecimal("0.00"));
    }

    @Test
    @DisplayName("11a — item elegível pós-duplicidade sem resultado de teto correspondente lança IllegalArgumentException")
    void inconsistencia_itemElegivelSemResultadoDeTeto() {
        ItemAvaliado item = itemAvaliadoElegivel(1, "alimentacao", new BigDecimal("50.00"));
        List<ItemAvaliado> avaliados = List.of(item);
        List<ItemAvaliado> aposDuplicidade = List.of(item);

        assertThrows(IllegalArgumentException.class,
                () -> CompositorSaida.compor(avaliados, aposDuplicidade, List.of(), List.of()));
    }

    @Test
    @DisplayName("11b — mesmo índice presente em resultadosTetoDiario e resultadosTetoHospedagem lança IllegalArgumentException")
    void inconsistencia_mesmoIndiceNosDoisAgregadores() {
        ItemAvaliado item = itemAvaliadoElegivel(1, "alimentacao", new BigDecimal("50.00"));
        List<ItemAvaliado> avaliados = List.of(item);
        List<ItemAvaliado> aposDuplicidade = List.of(item);
        ResultadoTeto tetoDiario = new ResultadoTeto(item, new BigDecimal("50.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());
        ResultadoTeto tetoHospedagem = new ResultadoTeto(item, new BigDecimal("50.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> CompositorSaida.compor(avaliados, aposDuplicidade, List.of(tetoDiario), List.of(tetoHospedagem)));
    }

    @Test
    @DisplayName("11c — índice duplicado dentro de avaliados lança IllegalArgumentException")
    void inconsistencia_indiceDuplicadoEmAvaliados() {
        ItemAvaliado item1 = itemAvaliadoElegivel(1, "alimentacao", new BigDecimal("50.00"));
        ItemAvaliado item1Duplicado = itemAvaliadoElegivel(1, "alimentacao", new BigDecimal("60.00"));
        List<ItemAvaliado> avaliados = List.of(item1, item1Duplicado);
        List<ItemAvaliado> aposDuplicidade = List.of(item1);

        assertThrows(IllegalArgumentException.class,
                () -> CompositorSaida.compor(avaliados, aposDuplicidade, List.of(), List.of()));
    }

    @Test
    @DisplayName("11d — resultado de teto excedente sem item correspondente em aposDuplicidade lança IllegalArgumentException")
    void inconsistencia_resultadoDeTetoExcedente() {
        ItemAvaliado item1 = itemAvaliadoElegivel(1, "alimentacao", new BigDecimal("50.00"));
        List<ItemAvaliado> avaliados = List.of(item1);
        List<ItemAvaliado> aposDuplicidade = List.of(item1);
        ResultadoTeto tetoItem1 = new ResultadoTeto(item1, new BigDecimal("50.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        ItemAvaliado item2SemCorrespondencia = itemAvaliadoElegivel(2, "alimentacao", new BigDecimal("30.00"));
        ResultadoTeto tetoExcedente = new ResultadoTeto(
                item2SemCorrespondencia, new BigDecimal("30.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        List<ResultadoTeto> resultadosTetoDiario = List.of(tetoItem1, tetoExcedente);

        assertThrows(IllegalArgumentException.class,
                () -> CompositorSaida.compor(avaliados, aposDuplicidade, resultadosTetoDiario, List.of()));
    }

    @Test
    @DisplayName("11e — item elegível excedente em aposDuplicidade, sem correspondente em avaliados, lança IllegalArgumentException identificando o índice e a lista")
    void inconsistencia_itemElegivelExcedenteEmAposDuplicidade() {
        ItemAvaliado item1 = itemAvaliadoElegivel(1, "alimentacao", new BigDecimal("50.00"));
        ItemAvaliado item2Excedente = itemAvaliadoElegivel(2, "alimentacao", new BigDecimal("30.00"));

        List<ItemAvaliado> avaliados = List.of(item1);
        List<ItemAvaliado> aposDuplicidade = List.of(item1, item2Excedente);

        ResultadoTeto tetoItem1 = new ResultadoTeto(item1, new BigDecimal("50.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());
        ResultadoTeto tetoItem2 = new ResultadoTeto(item2Excedente, new BigDecimal("30.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());
        List<ResultadoTeto> resultadosTetoDiario = List.of(tetoItem1, tetoItem2);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
                () -> CompositorSaida.compor(avaliados, aposDuplicidade, resultadosTetoDiario, List.of()));

        assertTrue(excecao.getMessage().contains("2"), "mensagem deve identificar o índice 2: " + excecao.getMessage());
        assertTrue(excecao.getMessage().contains("aposDuplicidade"),
                "mensagem deve identificar aposDuplicidade: " + excecao.getMessage());
    }

    @Test
    @DisplayName("11f — item inelegível excedente em aposDuplicidade, sem correspondente em avaliados, também lança IllegalArgumentException")
    void inconsistencia_itemInelegivelExcedenteEmAposDuplicidade() {
        ItemAvaliado item1 = itemAvaliadoElegivel(1, "alimentacao", new BigDecimal("50.00"));
        ItemAvaliado item2ExcedenteInelegivel = itemAvaliadoInelegivel(2, "coworking", new BigDecimal("30.00"));

        List<ItemAvaliado> avaliados = List.of(item1);
        List<ItemAvaliado> aposDuplicidade = List.of(item1, item2ExcedenteInelegivel);

        ResultadoTeto tetoItem1 = new ResultadoTeto(item1, new BigDecimal("50.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());
        List<ResultadoTeto> resultadosTetoDiario = List.of(tetoItem1);

        assertThrows(IllegalArgumentException.class,
                () -> CompositorSaida.compor(avaliados, aposDuplicidade, resultadosTetoDiario, List.of()));
    }
}
