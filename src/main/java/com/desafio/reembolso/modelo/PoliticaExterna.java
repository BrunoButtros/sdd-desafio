package com.desafio.reembolso.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Política de reembolso lida de `politica.json` (RN-019, RN-021, DT-011):
 * vigência, moeda base, gatilho de nota fiscal e as duas tabelas de
 * categoria (padrão e por centro de custo). Estrutura de dados pura — a
 * leitura e validação a partir do arquivo é `LeitorPolitica` (T-030); a
 * resolução por centro de custo é `ResolutorPoliticaCentroCusto` (T-040).
 */
public final class PoliticaExterna {

    private final LocalDate vigencia;
    private final String moedaBase;
    private final BigDecimal notaFiscalObrigatoriaAcimaDe;
    private final Map<String, TabelaCategoria> padrao;
    private final Map<String, Map<String, TabelaCategoria>> centrosCusto;

    public PoliticaExterna(LocalDate vigencia,
                            String moedaBase,
                            BigDecimal notaFiscalObrigatoriaAcimaDe,
                            Map<String, TabelaCategoria> padrao,
                            Map<String, Map<String, TabelaCategoria>> centrosCusto) {
        this.vigencia = vigencia;
        this.moedaBase = moedaBase;
        this.notaFiscalObrigatoriaAcimaDe = notaFiscalObrigatoriaAcimaDe;
        this.padrao = Map.copyOf(padrao);

        Map<String, Map<String, TabelaCategoria>> centrosCustoCopiado = new HashMap<>();
        for (Map.Entry<String, Map<String, TabelaCategoria>> entrada : centrosCusto.entrySet()) {
            centrosCustoCopiado.put(entrada.getKey(), Map.copyOf(entrada.getValue()));
        }
        this.centrosCusto = Map.copyOf(centrosCustoCopiado);
    }

    public LocalDate getVigencia() {
        return vigencia;
    }

    public String getMoedaBase() {
        return moedaBase;
    }

    public BigDecimal getNotaFiscalObrigatoriaAcimaDe() {
        return notaFiscalObrigatoriaAcimaDe;
    }

    public Map<String, TabelaCategoria> getPadrao() {
        return padrao;
    }

    public Map<String, Map<String, TabelaCategoria>> getCentrosCusto() {
        return centrosCusto;
    }
}
