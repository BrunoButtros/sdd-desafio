package com.desafio.reembolso.modelo;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * Vocabulário fechado dos sete campos canônicos de {@code despesa} (spec 4.2),
 * cada valor serializando para o texto canônico {@code "despesa.<campo>"}.
 */
public enum CampoCanonico {
    ID,
    DATA,
    CATEGORIA,
    DESCRICAO,
    FORNECEDOR,
    VALOR,
    TEM_NOTA_FISCAL;

    @JsonValue
    public String textoCanonico() {
        return "despesa." + name().toLowerCase(Locale.ROOT);
    }
}
