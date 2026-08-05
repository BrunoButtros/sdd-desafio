package com.desafio.reembolso.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * T-025 / spec 4.1.1, RN-019: `TabelaCategoria` preserva os dois campos e
 * falha rápido, sem estado parcial, quando qualquer um deles é nulo.
 */
class TabelaCategoriaTest {

    @Test
    @DisplayName("Construção válida preserva limite e periodicidade")
    void construcaoValidaPreservaCampos() {
        BigDecimal limite = new BigDecimal("60.00");
        TabelaCategoria tabela = new TabelaCategoria(limite, Periodicidade.DIA);

        assertEquals(limite, tabela.limite());
        assertEquals(Periodicidade.DIA, tabela.periodicidade());
    }

    @Test
    @DisplayName("Limite nulo lança NullPointerException")
    void limiteNuloLancaNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new TabelaCategoria(null, Periodicidade.DIARIA));
    }

    @Test
    @DisplayName("Periodicidade nula lança NullPointerException")
    void periodicidadeNulaLancaNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new TabelaCategoria(new BigDecimal("250.00"), null));
    }

    @Test
    @DisplayName("Periodicidade possui exatamente DIA e DIARIA")
    void periodicidadeTemExatamenteDoisValores() {
        assertEquals(2, Periodicidade.values().length);
        assertEquals(Periodicidade.DIA, Periodicidade.valueOf("DIA"));
        assertEquals(Periodicidade.DIARIA, Periodicidade.valueOf("DIARIA"));
    }
}
