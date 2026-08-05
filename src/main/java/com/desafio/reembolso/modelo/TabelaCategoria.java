package com.desafio.reembolso.modelo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Categoria dentro de uma tabela de política (`padrao` ou de um centro de
 * custo — RN-019, DT-011): limite financeiro e a periodicidade que determina
 * se o limite é um teto diário compartilhado ou um teto individual por
 * lançamento.
 */
public record TabelaCategoria(
        BigDecimal limite,
        Periodicidade periodicidade
) {
    public TabelaCategoria {
        Objects.requireNonNull(limite, "limite");
        Objects.requireNonNull(periodicidade, "periodicidade");
    }
}
