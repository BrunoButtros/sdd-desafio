package com.desafio.reembolso.modelo;

/**
 * Periodicidade de uma categoria numa tabela de política (spec 4.1.1, RN-019,
 * AMB-036): teto compartilhado por data ({@code DIA}) ou teto individual por
 * lançamento ({@code DIARIA}). Consumida internamente, nunca escrita na saída
 * — sem serialização JSON própria.
 */
public enum Periodicidade {
    DIA,
    DIARIA
}
