package com.desafio.reembolso.modelo;

import java.util.Map;

/**
 * Resultado da resolução de política por centro de custo (RN-019, DT-011):
 * a única tabela de categorias efetivamente aplicável — nunca a união entre
 * `padrao` e a tabela de um centro de custo — junto da origem dessa escolha.
 * Estrutura de dados pura, devolvida por
 * `ResolutorPoliticaCentroCusto.resolver(...)` (T-040); esta task cria só a
 * estrutura.
 */
public final class TabelaPoliticaResolvida {

    private final Map<String, TabelaCategoria> categorias;
    private final Origem origem;
    private final String nomeCentroCusto;

    public TabelaPoliticaResolvida(Map<String, TabelaCategoria> categorias,
                                    Origem origem,
                                    String nomeCentroCusto) {
        if (origem == Origem.CENTRO_CUSTO && nomeCentroCusto == null) {
            throw new IllegalArgumentException(
                    "nomeCentroCusto nao pode ser nulo quando origem == CENTRO_CUSTO");
        }
        if (origem == Origem.PADRAO && nomeCentroCusto != null) {
            throw new IllegalArgumentException(
                    "nomeCentroCusto deve ser nulo quando origem == PADRAO");
        }

        this.categorias = Map.copyOf(categorias);
        this.origem = origem;
        this.nomeCentroCusto = nomeCentroCusto;
    }

    public Map<String, TabelaCategoria> getCategorias() {
        return categorias;
    }

    public Origem getOrigem() {
        return origem;
    }

    public String getNomeCentroCusto() {
        return nomeCentroCusto;
    }

    public enum Origem {
        PADRAO,
        CENTRO_CUSTO
    }
}
