package com.desafio.reembolso.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Câmbio lido de `cambio.json` (RN-020, DT-013), já invertido para consulta
 * eficiente (moeda → data → taxa). Estrutura de dados pura — a leitura e
 * inversão a partir do arquivo é `LeitorCambio` (T-032).
 */
public final class TabelaCambio {

    private final String moedaBase;
    private final Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda;

    public TabelaCambio(String moedaBase,
                         Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda) {
        this.moedaBase = moedaBase;

        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesCopiadas = new HashMap<>();
        for (Map.Entry<String, NavigableMap<LocalDate, BigDecimal>> entrada : cotacoesPorMoeda.entrySet()) {
            NavigableMap<LocalDate, BigDecimal> copia = new TreeMap<>(entrada.getValue());
            cotacoesCopiadas.put(entrada.getKey(), Collections.unmodifiableNavigableMap(copia));
        }
        this.cotacoesPorMoeda = Collections.unmodifiableMap(cotacoesCopiadas);
    }

    public String getMoedaBase() {
        return moedaBase;
    }

    public Map<String, NavigableMap<LocalDate, BigDecimal>> getCotacoesPorMoeda() {
        return cotacoesPorMoeda;
    }

    public Optional<CotacaoResolvida> cotacaoEm(String moeda, LocalDate dataDespesa) {
        NavigableMap<LocalDate, BigDecimal> cotacoesDaMoeda = cotacoesPorMoeda.get(moeda);
        if (cotacoesDaMoeda == null) {
            return Optional.empty();
        }

        Map.Entry<LocalDate, BigDecimal> entrada = cotacoesDaMoeda.floorEntry(dataDespesa);
        if (entrada == null) {
            return Optional.empty();
        }

        return Optional.of(new CotacaoResolvida(entrada.getKey(), entrada.getValue()));
    }

    public record CotacaoResolvida(
            LocalDate data,
            BigDecimal taxa
    ) {}
}
