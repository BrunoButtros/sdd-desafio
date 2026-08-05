package com.desafio.reembolso.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * T-027 / spec 4.1.1, 4.3, RN-020, DT-013: `TabelaCambio` resolve cotações
 * via `floorEntry` (exata ou anterior mais recente, nunca futura) e nenhuma
 * referência mutável recebida pelo construtor, ou devolvida pelos getters,
 * escapa para fora da estrutura.
 */
class TabelaCambioTest {

    private static TabelaCambio construirTabela(Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda) {
        return new TabelaCambio("BRL", cotacoesPorMoeda);
    }

    private static NavigableMap<LocalDate, BigDecimal> cotacoesUsd() {
        NavigableMap<LocalDate, BigDecimal> cotacoes = new TreeMap<>();
        cotacoes.put(LocalDate.of(2026, 7, 15), new BigDecimal("5.40"));
        cotacoes.put(LocalDate.of(2026, 7, 17), new BigDecimal("5.45"));
        return cotacoes;
    }

    @Test
    @DisplayName("Cotação exata devolve a própria data consultada e sua taxa")
    void cotacaoExataDevolveDataConsultada() {
        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put("USD", cotacoesUsd());

        TabelaCambio tabela = construirTabela(cotacoesPorMoeda);

        Optional<TabelaCambio.CotacaoResolvida> resultado = tabela.cotacaoEm("USD", LocalDate.of(2026, 7, 17));

        assertTrue(resultado.isPresent());
        assertEquals(LocalDate.of(2026, 7, 17), resultado.get().data());
        assertEquals(new BigDecimal("5.45"), resultado.get().taxa());
    }

    @Test
    @DisplayName("Sem cotação exata, fallback devolve a data anterior realmente utilizada e sua taxa")
    void fallbackDevolveDataAnteriorUtilizada() {
        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put("USD", cotacoesUsd());

        TabelaCambio tabela = construirTabela(cotacoesPorMoeda);

        Optional<TabelaCambio.CotacaoResolvida> resultado = tabela.cotacaoEm("USD", LocalDate.of(2026, 7, 18));

        assertTrue(resultado.isPresent());
        assertEquals(LocalDate.of(2026, 7, 17), resultado.get().data());
        assertEquals(new BigDecimal("5.45"), resultado.get().taxa());
    }

    @Test
    @DisplayName("Apenas uma cotação futura disponível retorna Optional.empty()")
    void apenasCotacaoFuturaRetornaVazio() {
        NavigableMap<LocalDate, BigDecimal> cotacoes = new TreeMap<>();
        cotacoes.put(LocalDate.of(2026, 7, 20), new BigDecimal("5.50"));

        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put("USD", cotacoes);

        TabelaCambio tabela = construirTabela(cotacoesPorMoeda);

        Optional<TabelaCambio.CotacaoResolvida> resultado = tabela.cotacaoEm("USD", LocalDate.of(2026, 7, 15));

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Moeda ausente retorna Optional.empty()")
    void moedaAusenteRetornaVazio() {
        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put("USD", cotacoesUsd());

        TabelaCambio tabela = construirTabela(cotacoesPorMoeda);

        Optional<TabelaCambio.CotacaoResolvida> resultado = tabela.cotacaoEm("EUR", LocalDate.of(2026, 7, 17));

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("O mapa externo retornado é imutável")
    void mapaExternoRetornadoImutavel() {
        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put("USD", cotacoesUsd());

        TabelaCambio tabela = construirTabela(cotacoesPorMoeda);

        Map<String, NavigableMap<LocalDate, BigDecimal>> mapaObtido = tabela.getCotacoesPorMoeda();
        assertThrows(UnsupportedOperationException.class,
                () -> mapaObtido.put("EUR", new TreeMap<>()));
    }

    @Test
    @DisplayName("Cada NavigableMap interno é imutável")
    void mapaInternoImutavel() {
        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put("USD", cotacoesUsd());

        TabelaCambio tabela = construirTabela(cotacoesPorMoeda);

        NavigableMap<LocalDate, BigDecimal> mapaInternoObtido = tabela.getCotacoesPorMoeda().get("USD");
        assertThrows(UnsupportedOperationException.class,
                () -> mapaInternoObtido.put(LocalDate.of(2026, 7, 19), new BigDecimal("5.60")));
    }

    @Test
    @DisplayName("Alterações posteriores no mapa externo original não afetam TabelaCambio")
    void alteracoesNoMapaExternoOriginalNaoAfetamTabela() {
        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put("USD", cotacoesUsd());

        TabelaCambio tabela = construirTabela(cotacoesPorMoeda);

        cotacoesPorMoeda.put("EUR", new TreeMap<>());

        assertEquals(1, tabela.getCotacoesPorMoeda().size());
    }

    @Test
    @DisplayName("Alterações posteriores num NavigableMap original não afetam TabelaCambio")
    void alteracoesNoMapaInternoOriginalNaoAfetamTabela() {
        NavigableMap<LocalDate, BigDecimal> cotacoesOriginais = cotacoesUsd();

        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put("USD", cotacoesOriginais);

        TabelaCambio tabela = construirTabela(cotacoesPorMoeda);

        cotacoesOriginais.put(LocalDate.of(2026, 7, 19), new BigDecimal("5.60"));

        assertEquals(2, tabela.getCotacoesPorMoeda().get("USD").size());
    }
}
