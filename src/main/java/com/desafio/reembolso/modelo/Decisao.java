package com.desafio.reembolso.modelo;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Vocabulário fechado de decisões finais de item (spec 4.4).
 */
public enum Decisao {
    INTEGRALMENTE_REEMBOLSADO,
    PARCIALMENTE_REEMBOLSADO,
    NAO_REEMBOLSADO_TETO_ESGOTADO,
    RECUSADO;

    @JsonValue
    public String textoCanonico() {
        return name();
    }
}
