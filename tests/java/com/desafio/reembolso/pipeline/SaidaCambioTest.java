package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.modelo.TabelaCambio;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.CompositorSaida.ResultadoItem;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre os campos de auditoria cambial (spec 4.3, RN-017, RN-020; CA-034) no
 * {@link ResultadoItem} final, produzido por {@link CompositorSaida} a
 * partir do pipeline real — nunca a partir de getters isolados de
 * {@link ItemValidado}. Os três campos ({@code moeda}, {@code
 * taxaCambioAplicada}, {@code dataCotacaoUtilizada}) chegam a {@code
 * ResultadoItem} sem recálculo: apenas propagados de {@link ItemValidado},
 * já resolvidos por {@link ResolutorCambio}.
 */
@DisplayName("Saída de auditoria cambial no ResultadoItem — RN-017/RN-020, CA-034")
class SaidaCambioTest {

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

    private static List<ResultadoItem> pipelineCompleto(String json, TabelaCambio cambio) {
        Envelope envelope = ValidadorEnvelope.validar(raiz(json));

        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(idsVerificados, cambio);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);

        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);

        List<ItemAvaliado> elegiveisParaTetos = SeletorElegiveis.selecionar(aposDuplicidade);
        List<ResultadoTeto> resultadosDiarios = AgregadorTetoDiario.aplicar(elegiveisParaTetos);
        List<ResultadoTeto> resultadosHospedagem = AgregadorTetoHospedagem.aplicar(elegiveisParaTetos);

        return CompositorSaida.compor(avaliados, aposDuplicidade, resultadosDiarios, resultadosHospedagem);
    }

    private static TabelaCambio tabelaComCotacoes(String moeda, Map<LocalDate, BigDecimal> cotacoes) {
        NavigableMap<LocalDate, BigDecimal> mapa = new TreeMap<>(cotacoes);
        return new TabelaCambio("BRL", Map.of(moeda, mapa));
    }

    // ---- 1. BRL válido -------------------------------------------------------------------

    @Test
    @DisplayName("1 — BRL válido: moeda BRL, taxa 1, data de cotação nula")
    void brlValido_taxaUmDataNula() {
        String json = envelopeComItens("""
                { "id": "d-001", "data": "2026-07-10", "categoria": "alimentacao", "descricao": "Almoco",
                  "fornecedor": "Restaurante X", "valor": 30.00, "tem_nota_fiscal": true }""");

        ResultadoItem r = pipelineCompleto(json, CambioTesteSupport.TABELA_BRL).get(0);

        assertEquals("BRL", r.moeda());
        assertEquals(0, BigDecimal.ONE.compareTo(r.taxaCambioAplicada()));
        assertNull(r.dataCotacaoUtilizada());
        assertEquals(0, new BigDecimal("30.00").compareTo(r.valorNormalizado()));
    }

    // ---- 2. Moeda estrangeira com cotação exata --------------------------------------------

    @Test
    @DisplayName("2 — moeda estrangeira com cotação exata: taxa, data e valor convertido corretos")
    void moedaEstrangeiraComCotacaoExata() {
        LocalDate data = LocalDate.of(2026, 7, 10);
        String json = envelopeComItens("""
                { "id": "d-001", "data": "2026-07-10", "categoria": "alimentacao", "descricao": "Almoco",
                  "fornecedor": "Restaurante X", "valor": 40.00, "moeda": "EUR", "tem_nota_fiscal": true }""");

        TabelaCambio cambio = tabelaComCotacoes("EUR", Map.of(data, new BigDecimal("6.00")));
        ResultadoItem r = pipelineCompleto(json, cambio).get(0);

        assertEquals("EUR", r.moeda());
        assertEquals(0, new BigDecimal("6.00").compareTo(r.taxaCambioAplicada()));
        assertEquals(data, r.dataCotacaoUtilizada());
        assertEquals(0, new BigDecimal("240.00").compareTo(r.valorNormalizado()));
    }

    // ---- 3. Moeda estrangeira com cotação anterior (floorEntry) -----------------------------

    @Test
    @DisplayName("3 — moeda estrangeira sem cotação exata: usa a cotação anterior mais recente (floorEntry)")
    void moedaEstrangeiraComCotacaoAnterior_floorEntry() {
        LocalDate dataCotacaoAnterior = LocalDate.of(2026, 7, 17);
        LocalDate dataDespesa = LocalDate.of(2026, 7, 18);
        String json = envelopeComItens("""
                { "id": "d-001", "data": "2026-07-18", "categoria": "alimentacao", "descricao": "Almoco",
                  "fornecedor": "Restaurante X", "valor": 30.00, "moeda": "EUR", "tem_nota_fiscal": true }""");

        TabelaCambio cambio = tabelaComCotacoes("EUR", Map.of(dataCotacaoAnterior, new BigDecimal("5.96")));
        ResultadoItem r = pipelineCompleto(json, cambio).get(0);

        assertEquals("EUR", r.moeda());
        assertEquals(0, new BigDecimal("5.96").compareTo(r.taxaCambioAplicada()));
        assertEquals(dataCotacaoAnterior, r.dataCotacaoUtilizada());
        assertNotEquals(dataDespesa, r.dataCotacaoUtilizada());
        assertEquals(0, new BigDecimal("178.80").compareTo(r.valorNormalizado()));
    }

    // ---- 4. Moeda estruturalmente inválida ---------------------------------------------------

    @Test
    @DisplayName("4 — moeda estruturalmente inválida: moeda, taxa e data nulos por motivo estrutural, nunca MOEDA_SEM_COTACAO")
    void moedaEstruturalmenteInvalida_tresCamposNulos() {
        String json = envelopeComItens("""
                { "id": "d-001", "data": "2026-07-10", "categoria": "alimentacao", "descricao": "Almoco",
                  "fornecedor": "Restaurante X", "valor": 30.00, "moeda": "usd", "tem_nota_fiscal": true }""");

        ResultadoItem r = pipelineCompleto(json, CambioTesteSupport.TABELA_BRL).get(0);

        assertEquals(Decisao.RECUSADO, r.decisao());
        assertNull(r.moeda());
        assertNull(r.taxaCambioAplicada());
        assertNull(r.dataCotacaoUtilizada());
        assertTrue(r.motivos().stream().anyMatch(m -> m.codigo() == MotivoCodigo.CAMPO_FORMATO_INVALIDO
                && m.campo() == CampoCanonico.MOEDA));
        assertTrue(r.motivos().stream().noneMatch(m -> m.codigo() == MotivoCodigo.MOEDA_SEM_COTACAO),
                "moeda estruturalmente inválida não deve ser confundida com MOEDA_SEM_COTACAO");
    }

    // ---- 5. Moeda válida sem cotação -----------------------------------------------------------

    @Test
    @DisplayName("5 — moeda válida sem cotação: moeda preservada, taxa/data/normalizado nulos, reembolsável 0.00, motivo único MOEDA_SEM_COTACAO")
    void moedaValidaSemCotacao() {
        String json = envelopeComItens("""
                { "id": "d-001", "data": "2026-07-10", "categoria": "alimentacao", "descricao": "Almoco",
                  "fornecedor": "Restaurante X", "valor": 30.00, "moeda": "GBP", "tem_nota_fiscal": true }""");

        TabelaCambio cambioSemCotacao = new TabelaCambio("BRL", Map.of());
        ResultadoItem r = pipelineCompleto(json, cambioSemCotacao).get(0);

        assertEquals("GBP", r.moeda());
        assertNull(r.taxaCambioAplicada());
        assertNull(r.dataCotacaoUtilizada());
        assertNull(r.valorNormalizado());
        assertEquals(0, new BigDecimal("0.00").compareTo(r.valorReembolsavel()));
        assertEquals(Decisao.RECUSADO, r.decisao());

        assertEquals(1, r.motivos().size(), "MOEDA_SEM_COTACAO não deve duplicar nem coexistir com outro motivo aqui");
        Motivo motivo = r.motivos().get(0);
        assertEquals(MotivoCodigo.MOEDA_SEM_COTACAO, motivo.codigo());
        assertEquals(RegraNegocio.RN_020, motivo.regra());
        assertEquals(CampoCanonico.MOEDA, motivo.campo());
    }

    // ---- 6. Campo não financeiro inválido, conversão preservada -------------------------------

    @Test
    @DisplayName("6 — campo não financeiro (fornecedor) estruturalmente inválido: conversão cambial permanece resolvida")
    void campoNaoFinanceiroInvalido_conversaoPreservada() {
        LocalDate data = LocalDate.of(2026, 7, 10);
        String json = envelopeComItens("""
                { "id": "d-001", "data": "2026-07-10", "categoria": "alimentacao", "descricao": "Almoco",
                  "fornecedor": 123, "valor": 40.00, "moeda": "EUR", "tem_nota_fiscal": true }""");

        TabelaCambio cambio = tabelaComCotacoes("EUR", Map.of(data, new BigDecimal("6.00")));
        ResultadoItem r = pipelineCompleto(json, cambio).get(0);

        assertEquals(Decisao.RECUSADO, r.decisao());
        assertTrue(r.motivos().stream().anyMatch(m -> m.codigo() == MotivoCodigo.CAMPO_TIPO_INVALIDO
                && m.campo() == CampoCanonico.FORNECEDOR));

        assertEquals("EUR", r.moeda());
        assertEquals(0, new BigDecimal("6.00").compareTo(r.taxaCambioAplicada()));
        assertEquals(data, r.dataCotacaoUtilizada());
        assertEquals(0, new BigDecimal("240.00").compareTo(r.valorNormalizado()));
    }
}
