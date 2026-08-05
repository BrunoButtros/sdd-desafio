package com.desafio.reembolso.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * T-028 / RN-019, DT-011: `TabelaPoliticaResolvida` preserva os três campos,
 * copia `categorias` defensivamente e recusa qualquer combinação
 * inconsistente entre `origem` e `nomeCentroCusto`.
 */
class TabelaPoliticaResolvidaTest {

    private static Map<String, TabelaCategoria> categoriasDeExemplo() {
        Map<String, TabelaCategoria> categorias = new HashMap<>();
        categorias.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));
        return categorias;
    }

    @Test
    @DisplayName("Construção válida com PADRAO e nomeCentroCusto nulo")
    void construcaoValidaComPadraoENomeNulo() {
        Map<String, TabelaCategoria> categorias = categoriasDeExemplo();

        TabelaPoliticaResolvida tabela = new TabelaPoliticaResolvida(
                categorias, TabelaPoliticaResolvida.Origem.PADRAO, null);

        assertEquals(categorias, tabela.getCategorias());
        assertEquals(TabelaPoliticaResolvida.Origem.PADRAO, tabela.getOrigem());
        assertNull(tabela.getNomeCentroCusto());
    }

    @Test
    @DisplayName("Construção válida com CENTRO_CUSTO e nomeCentroCusto preenchido")
    void construcaoValidaComCentroCustoENomePreenchido() {
        Map<String, TabelaCategoria> categorias = categoriasDeExemplo();

        TabelaPoliticaResolvida tabela = new TabelaPoliticaResolvida(
                categorias, TabelaPoliticaResolvida.Origem.CENTRO_CUSTO, "filial-sp");

        assertEquals(categorias, tabela.getCategorias());
        assertEquals(TabelaPoliticaResolvida.Origem.CENTRO_CUSTO, tabela.getOrigem());
        assertEquals("filial-sp", tabela.getNomeCentroCusto());
    }

    @Test
    @DisplayName("CENTRO_CUSTO com nomeCentroCusto nulo lança IllegalArgumentException")
    void centroCustoComNomeNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new TabelaPoliticaResolvida(
                        categoriasDeExemplo(), TabelaPoliticaResolvida.Origem.CENTRO_CUSTO, null));
    }

    @Test
    @DisplayName("PADRAO com nomeCentroCusto preenchido lança IllegalArgumentException")
    void padraoComNomePreenchidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new TabelaPoliticaResolvida(
                        categoriasDeExemplo(), TabelaPoliticaResolvida.Origem.PADRAO, "filial-sp"));
    }

    @Test
    @DisplayName("Categorias retornadas são imutáveis")
    void categoriasRetornadasSaoImutaveis() {
        TabelaPoliticaResolvida tabela = new TabelaPoliticaResolvida(
                categoriasDeExemplo(), TabelaPoliticaResolvida.Origem.PADRAO, null);

        Map<String, TabelaCategoria> categoriasObtidas = tabela.getCategorias();
        assertThrows(UnsupportedOperationException.class,
                () -> categoriasObtidas.put("transporte_urbano",
                        new TabelaCategoria(new BigDecimal("80.00"), Periodicidade.DIA)));
    }

    @Test
    @DisplayName("Alterações no mapa original após a construção não afetam a tabela resolvida")
    void alteracoesNoMapaOriginalNaoAfetamTabela() {
        Map<String, TabelaCategoria> categorias = categoriasDeExemplo();

        TabelaPoliticaResolvida tabela = new TabelaPoliticaResolvida(
                categorias, TabelaPoliticaResolvida.Origem.PADRAO, null);

        categorias.put("transporte_urbano", new TabelaCategoria(new BigDecimal("80.00"), Periodicidade.DIA));

        assertEquals(1, tabela.getCategorias().size());
    }

    @Test
    @DisplayName("Campos permanecem acessíveis corretamente")
    void camposPermanecemAcessiveisCorretamente() {
        Map<String, TabelaCategoria> categorias = categoriasDeExemplo();

        TabelaPoliticaResolvida tabela = new TabelaPoliticaResolvida(
                categorias, TabelaPoliticaResolvida.Origem.CENTRO_CUSTO, "filial-sp");

        TabelaCategoria alimentacao = tabela.getCategorias().get("alimentacao");
        assertEquals(new BigDecimal("60.00"), alimentacao.limite());
        assertEquals(Periodicidade.DIA, alimentacao.periodicidade());
        assertEquals(TabelaPoliticaResolvida.Origem.CENTRO_CUSTO, tabela.getOrigem());
        assertEquals("filial-sp", tabela.getNomeCentroCusto());
    }
}
