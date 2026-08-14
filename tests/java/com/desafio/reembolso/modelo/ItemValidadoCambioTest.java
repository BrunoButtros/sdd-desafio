package com.desafio.reembolso.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Cobre T-029/T-055 (RN-002 / RN-020, plan §4/§9, DT-014): preservação
 * exata dos quatro campos de câmbio pelo construtor completo de catorze
 * argumentos, inclusive para estados BRL e para nulos explícitos.
 */
@DisplayName("ItemValidado — campos de câmbio (T-029)")
class ItemValidadoCambioTest {

    private static ItemValidado construirBrlResolvido(BigDecimal valor) {
        return new ItemValidado(
                1,
                "d-001",
                LocalDate.of(2026, 7, 3),
                "alimentacao",
                "Almoço",
                "Restaurante X",
                valor,
                true,
                null,
                List.of(),
                "BRL",
                BigDecimal.ONE,
                null,
                valor);
    }

    @Test
    @DisplayName("construtor completo preserva estado BRL resolvido com taxa 1, data nula e valor convertido bruto")
    void construtorCompleto_preservaEstadoBrlResolvido() {
        BigDecimal valor = new BigDecimal("33.33");

        ItemValidado item = construirBrlResolvido(valor);

        assertEquals("BRL", item.getMoeda());
        assertEquals(0, BigDecimal.ONE.compareTo(item.getTaxaCambioAplicada()));
        assertNull(item.getDataCotacaoUtilizada());
        assertEquals(0, valor.compareTo(item.getValorConvertidoBruto()));
    }

    @Test
    @DisplayName("construtor completo preserva estado BRL resolvido com valor bruto nulo")
    void construtorCompleto_comValorNulo_preservaValorConvertidoBrutoNulo() {
        ItemValidado item = construirBrlResolvido(null);

        assertEquals("BRL", item.getMoeda());
        assertEquals(0, BigDecimal.ONE.compareTo(item.getTaxaCambioAplicada()));
        assertNull(item.getDataCotacaoUtilizada());
        assertNull(item.getValorConvertidoBruto());
    }

    @Test
    @DisplayName("construtor de catorze argumentos preserva exatamente moeda, taxa, data e valorConvertidoBruto recebidos")
    void construtorNovo_preservaExatamenteOsQuatroValoresRecebidos() {
        String moeda = "USD";
        BigDecimal taxa = new BigDecimal("5.4321");
        LocalDate dataCotacao = LocalDate.of(2026, 7, 2);
        BigDecimal valorConvertidoBruto = new BigDecimal("181.11726");

        ItemValidado item = new ItemValidado(
                1,
                "d-001",
                LocalDate.of(2026, 7, 3),
                "alimentacao",
                "Almoço",
                "Restaurante X",
                new BigDecimal("33.33"),
                true,
                null,
                List.of(),
                moeda,
                taxa,
                dataCotacao,
                valorConvertidoBruto);

        assertEquals(moeda, item.getMoeda());
        assertEquals(0, taxa.compareTo(item.getTaxaCambioAplicada()));
        assertEquals(dataCotacao, item.getDataCotacaoUtilizada());
        assertEquals(0, valorConvertidoBruto.compareTo(item.getValorConvertidoBruto()));
    }

    @Test
    @DisplayName("construtor de catorze argumentos preserva nulos explícitos, sem fallback")
    void construtorNovo_preservaNulosExplicitosSemFallback() {
        ItemValidado item = new ItemValidado(
                1,
                "d-001",
                LocalDate.of(2026, 7, 3),
                "alimentacao",
                "Almoço",
                "Restaurante X",
                new BigDecimal("33.33"),
                true,
                null,
                List.of(),
                null,
                null,
                null,
                null);

        assertNull(item.getMoeda());
        assertNull(item.getTaxaCambioAplicada());
        assertNull(item.getDataCotacaoUtilizada());
        assertNull(item.getValorConvertidoBruto());
    }
}
