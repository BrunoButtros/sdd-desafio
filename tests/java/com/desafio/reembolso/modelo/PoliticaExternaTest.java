package com.desafio.reembolso.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * T-026 / spec 4.1.1, RN-019, RN-021, DT-011: `PoliticaExterna` preserva os
 * cinco campos e nenhuma referência mutável recebida pelo construtor, ou
 * devolvida pelos getters, escapa para fora da estrutura.
 */
class PoliticaExternaTest {

    private static PoliticaExterna construirPolitica(Map<String, TabelaCategoria> padrao,
                                                       Map<String, Map<String, TabelaCategoria>> centrosCusto) {
        return new PoliticaExterna(
                LocalDate.of(2026, 7, 1),
                "BRL",
                new BigDecimal("100.00"),
                padrao,
                centrosCusto
        );
    }

    @Test
    @DisplayName("Construção preserva os cinco campos")
    void construcaoPreservaCincoCampos() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaFilial = new HashMap<>();
        tabelaFilial.put("hospedagem", new TabelaCategoria(new BigDecimal("250.00"), Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("filial-sp", tabelaFilial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        assertEquals(LocalDate.of(2026, 7, 1), politica.getVigencia());
        assertEquals("BRL", politica.getMoedaBase());
        assertEquals(new BigDecimal("100.00"), politica.getNotaFiscalObrigatoriaAcimaDe());
        assertEquals(padrao, politica.getPadrao());
        assertEquals(centrosCusto, politica.getCentrosCusto());
    }

    @Test
    @DisplayName("Mapa padrao retornado é imutável")
    void mapaPadraoRetornadoImutavel() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        PoliticaExterna politica = construirPolitica(padrao, new HashMap<>());

        Map<String, TabelaCategoria> padraoObtido = politica.getPadrao();
        assertThrows(UnsupportedOperationException.class,
                () -> padraoObtido.put("transporte_urbano", new TabelaCategoria(new BigDecimal("80.00"), Periodicidade.DIA)));
    }

    @Test
    @DisplayName("Mapa externo de centrosCusto retornado é imutável")
    void mapaExternoCentrosCustoRetornadoImutavel() {
        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("filial-sp", new HashMap<>());

        PoliticaExterna politica = construirPolitica(new HashMap<>(), centrosCusto);

        Map<String, Map<String, TabelaCategoria>> centrosCustoObtido = politica.getCentrosCusto();
        assertThrows(UnsupportedOperationException.class,
                () -> centrosCustoObtido.put("filial-rj", new HashMap<>()));
    }

    @Test
    @DisplayName("Cada mapa interno de categorias de um centro de custo é imutável")
    void mapaInternoDeCentroCustoImutavel() {
        Map<String, TabelaCategoria> tabelaFilial = new HashMap<>();
        tabelaFilial.put("hospedagem", new TabelaCategoria(new BigDecimal("250.00"), Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("filial-sp", tabelaFilial);

        PoliticaExterna politica = construirPolitica(new HashMap<>(), centrosCusto);

        Map<String, TabelaCategoria> tabelaObtida = politica.getCentrosCusto().get("filial-sp");
        assertThrows(UnsupportedOperationException.class,
                () -> tabelaObtida.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA)));
    }

    @Test
    @DisplayName("Alterações nos mapas originais após a construção não afetam PoliticaExterna")
    void alteracoesNosMapasOriginaisNaoAfetamPolitica() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaFilial = new HashMap<>();
        tabelaFilial.put("hospedagem", new TabelaCategoria(new BigDecimal("250.00"), Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("filial-sp", tabelaFilial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        padrao.put("transporte_urbano", new TabelaCategoria(new BigDecimal("80.00"), Periodicidade.DIA));
        tabelaFilial.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));
        centrosCusto.put("filial-rj", new HashMap<>());

        assertEquals(1, politica.getPadrao().size());
        assertEquals(1, politica.getCentrosCusto().size());
        assertEquals(1, politica.getCentrosCusto().get("filial-sp").size());
    }

    @Test
    @DisplayName("Valores de TabelaCategoria continuam acessíveis corretamente")
    void valoresDeTabelaCategoriaAcessiveis() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaFilial = new HashMap<>();
        tabelaFilial.put("hospedagem", new TabelaCategoria(new BigDecimal("250.00"), Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("filial-sp", tabelaFilial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        TabelaCategoria alimentacao = politica.getPadrao().get("alimentacao");
        assertEquals(new BigDecimal("60.00"), alimentacao.limite());
        assertEquals(Periodicidade.DIA, alimentacao.periodicidade());

        TabelaCategoria hospedagem = politica.getCentrosCusto().get("filial-sp").get("hospedagem");
        assertEquals(new BigDecimal("250.00"), hospedagem.limite());
        assertEquals(Periodicidade.DIARIA, hospedagem.periodicidade());
    }
}
