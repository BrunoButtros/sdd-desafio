package com.desafio.reembolso.modelo;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * Vocabulário fechado dos oito campos canônicos de {@code despesa} (spec 4.2),
 * cada valor serializando para o texto canônico {@code "despesa.<campo>"}.
 */
public enum CampoCanonico {
    ID,
    DATA,
    CATEGORIA,
    DESCRICAO,
    FORNECEDOR,
    VALOR,
    MOEDA,
    TEM_NOTA_FISCAL;

    @JsonValue
    public String textoCanonico() {
        return "despesa." + name().toLowerCase(Locale.ROOT);
    }
}
