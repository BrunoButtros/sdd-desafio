package com.desafio.reembolso.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Cobre T-029 (RN-002 / RN-020, plan §4/§9, DT-014): compatibilidade do
 * construtor de dez argumentos de {@link ItemValidado} via delegação para o
 * novo construtor de catorze argumentos, e preservação exata dos quatro
 * campos de câmbio quando o construtor de catorze argumentos é usado
 * diretamente.
 */
@DisplayName("ItemValidado — campos de câmbio (T-029)")
class ItemValidadoCambioTest {

    private static ItemValidado construirComConstrutorAntigo(BigDecimal valor) {
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
                List.of());
    }

    @Test
    @DisplayName("construtor de dez argumentos assume moeda BRL, taxa 1, data nula e valorConvertidoBruto igual ao valor recebido")
    void construtorAntigo_assumeValoresPadraoDeBrl() {
        BigDecimal valor = new BigDecimal("33.33");

        ItemValidado item = construirComConstrutorAntigo(valor);

        assertEquals("BRL", item.getMoeda());
        assertEquals(0, BigDecimal.ONE.compareTo(item.getTaxaCambioAplicada()));
        assertNull(item.getDataCotacaoUtilizada());
        assertEquals(0, valor.compareTo(item.getValorConvertidoBruto()));
    }

    @Test
    @DisplayName("construtor de dez argumentos com valor nulo produz valorConvertidoBruto nulo")
    void construtorAntigo_comValorNulo_valorConvertidoBrutoNulo() {
        ItemValidado item = construirComConstrutorAntigo(null);

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
