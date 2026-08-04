package com.desafio.reembolso.modelo;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Envelope de entrada validado (spec 4.1, RN-001): metadados opcionais de
 * {@code colaborador} e {@code periodo}, e a lista bruta de {@code despesas}
 * — ainda não validada item a item (RN-002, a partir de T-005).
 */
public final class Envelope {

    private final String colaboradorId;
    private final String colaboradorNome;
    private final String colaboradorCentroCusto;
    private final String periodoCompetencia;
    private final LocalDate periodoInicio;
    private final LocalDate periodoFim;
    private final JsonNode despesas;

    public Envelope(String colaboradorId,
                     String colaboradorNome,
                     String colaboradorCentroCusto,
                     String periodoCompetencia,
                     LocalDate periodoInicio,
                     LocalDate periodoFim,
                     JsonNode despesas) {
        this.colaboradorId = colaboradorId;
        this.colaboradorNome = colaboradorNome;
        this.colaboradorCentroCusto = colaboradorCentroCusto;
        this.periodoCompetencia = periodoCompetencia;
        this.periodoInicio = Objects.requireNonNull(periodoInicio, "periodoInicio");
        this.periodoFim = Objects.requireNonNull(periodoFim, "periodoFim");
        this.despesas = Objects.requireNonNull(despesas, "despesas");
    }

    public String getColaboradorId() {
        return colaboradorId;
    }

    public String getColaboradorNome() {
        return colaboradorNome;
    }

    public String getColaboradorCentroCusto() {
        return colaboradorCentroCusto;
    }

    public String getPeriodoCompetencia() {
        return periodoCompetencia;
    }

    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public LocalDate getPeriodoFim() {
        return periodoFim;
    }

    public JsonNode getDespesas() {
        return despesas;
    }
}
