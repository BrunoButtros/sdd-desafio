package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.PoliticaExterna;
import com.desafio.reembolso.modelo.Periodicidade;
import com.desafio.reembolso.modelo.TabelaCategoria;
import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * T-040 / spec RN-019, CA-024 a CA-027; plan §6, DT-011, DT-016:
 * {@code ResolutorPoliticaCentroCusto} escolhe a única tabela de política
 * aplicável — a de um centro de custo cadastrado, com comparação textual
 * exata, ou {@code padrao} — sem jamais unir as duas nem avaliar categoria,
 * teto ou limite zero.
 */
class ResolutorPoliticaCentroCustoTest {

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
    @DisplayName("Centro cadastrado: origem CENTRO_CUSTO, nome exato, categorias exclusivas do centro")
    void centroCadastradoResolveParaTabelaDoCentro() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaComercial = new HashMap<>();
        tabelaComercial.put("representacao", new TabelaCategoria(new BigDecimal("300.00"), Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-COMERCIAL", tabelaComercial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        TabelaPoliticaResolvida resultado = ResolutorPoliticaCentroCusto.resolver("CC-COMERCIAL", politica);

        assertEquals(TabelaPoliticaResolvida.Origem.CENTRO_CUSTO, resultado.getOrigem());
        assertEquals("CC-COMERCIAL", resultado.getNomeCentroCusto());
        assertEquals(tabelaComercial, resultado.getCategorias());
    }

    @Test
    @DisplayName("Centro desconhecido: origem PADRAO, nome nulo, categorias iguais à tabela padrão")
    void centroDesconhecidoResolveParaPadrao() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        PoliticaExterna politica = construirPolitica(padrao, new HashMap<>());

        TabelaPoliticaResolvida resultado = ResolutorPoliticaCentroCusto.resolver("CC-SUPORTE-N2", politica);

        assertEquals(TabelaPoliticaResolvida.Origem.PADRAO, resultado.getOrigem());
        assertNull(resultado.getNomeCentroCusto());
        assertEquals(padrao, resultado.getCategorias());
    }

    @Test
    @DisplayName("centroCusto null: origem PADRAO, nome nulo, categorias iguais à tabela padrão")
    void centroCustoNuloResolveParaPadrao() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        PoliticaExterna politica = construirPolitica(padrao, new HashMap<>());

        TabelaPoliticaResolvida resultado = ResolutorPoliticaCentroCusto.resolver(null, politica);

        assertEquals(TabelaPoliticaResolvida.Origem.PADRAO, resultado.getOrigem());
        assertNull(resultado.getNomeCentroCusto());
        assertEquals(padrao, resultado.getCategorias());
    }

    @Test
    @DisplayName("Comparação sensível a caixa: 'cc-comercial' não casa com 'CC-COMERCIAL' cadastrado")
    void comparacaoSensivelACaixaCaiEmPadrao() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaComercial = new HashMap<>();
        tabelaComercial.put("representacao", new TabelaCategoria(new BigDecimal("300.00"), Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-COMERCIAL", tabelaComercial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        TabelaPoliticaResolvida resultado = ResolutorPoliticaCentroCusto.resolver("cc-comercial", politica);

        assertEquals(TabelaPoliticaResolvida.Origem.PADRAO, resultado.getOrigem());
        assertNull(resultado.getNomeCentroCusto());
        assertEquals(padrao, resultado.getCategorias());
    }

    @Test
    @DisplayName("Comparação sem trim: espaços nas pontas não casam com a chave cadastrada")
    void comparacaoSemTrimCaiEmPadrao() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaComercial = new HashMap<>();
        tabelaComercial.put("representacao", new TabelaCategoria(new BigDecimal("300.00"), Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-COMERCIAL", tabelaComercial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        TabelaPoliticaResolvida comEspacoNasDuasPontas =
                ResolutorPoliticaCentroCusto.resolver(" CC-COMERCIAL ", politica);
        TabelaPoliticaResolvida comEspacoNoFim =
                ResolutorPoliticaCentroCusto.resolver("CC-COMERCIAL ", politica);

        assertEquals(TabelaPoliticaResolvida.Origem.PADRAO, comEspacoNasDuasPontas.getOrigem());
        assertNull(comEspacoNasDuasPontas.getNomeCentroCusto());
        assertEquals(padrao, comEspacoNasDuasPontas.getCategorias());

        assertEquals(TabelaPoliticaResolvida.Origem.PADRAO, comEspacoNoFim.getOrigem());
        assertNull(comEspacoNoFim.getNomeCentroCusto());
        assertEquals(padrao, comEspacoNoFim.getCategorias());
    }

    @Test
    @DisplayName("Ausência de fallback por categoria: alimentacao de padrao não aparece no centro que não a declara")
    void ausenciaDeFallbackPorCategoria() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaAdm = new HashMap<>();
        tabelaAdm.put("transporte_urbano", new TabelaCategoria(new BigDecimal("80.00"), Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ADM", tabelaAdm);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        TabelaPoliticaResolvida resultado = ResolutorPoliticaCentroCusto.resolver("CC-ADM", politica);

        assertFalse(resultado.getCategorias().containsKey("alimentacao"));
    }

    @Test
    @DisplayName("Tabela exclusiva do centro: resultado não mistura categorias de padrão e do centro")
    void tabelaExclusivaDoCentro() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));
        padrao.put("transporte_urbano", new TabelaCategoria(new BigDecimal("80.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaComercial = new HashMap<>();
        tabelaComercial.put("representacao", new TabelaCategoria(new BigDecimal("300.00"), Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-COMERCIAL", tabelaComercial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        TabelaPoliticaResolvida resultado = ResolutorPoliticaCentroCusto.resolver("CC-COMERCIAL", politica);

        assertEquals(1, resultado.getCategorias().size());
        assertTrue(resultado.getCategorias().containsKey("representacao"));
        assertFalse(resultado.getCategorias().containsKey("alimentacao"));
        assertFalse(resultado.getCategorias().containsKey("transporte_urbano"));
    }

    @Test
    @DisplayName("representacao: ausente no centro que não declara, preservada no centro que declara")
    void representacaoAusenteOuPreservadaConformeCentro() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("representacao", new TabelaCategoria(new BigDecimal("300.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaAdm = new HashMap<>();
        tabelaAdm.put("transporte_urbano", new TabelaCategoria(new BigDecimal("80.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaComercial = new HashMap<>();
        tabelaComercial.put("representacao", new TabelaCategoria(new BigDecimal("300.00"), Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ADM", tabelaAdm);
        centrosCusto.put("CC-COMERCIAL", tabelaComercial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        TabelaPoliticaResolvida resultadoAdm = ResolutorPoliticaCentroCusto.resolver("CC-ADM", politica);
        assertFalse(resultadoAdm.getCategorias().containsKey("representacao"));

        TabelaPoliticaResolvida resultadoComercial = ResolutorPoliticaCentroCusto.resolver("CC-COMERCIAL", politica);
        assertTrue(resultadoComercial.getCategorias().containsKey("representacao"));
        assertEquals(new BigDecimal("300.00"), resultadoComercial.getCategorias().get("representacao").limite());
    }

    @Test
    @DisplayName("Limite zero num centro cadastrado permanece presente, intacto, sem decisão de recusa")
    void limiteZeroPermaneceIntacto() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("hospedagem", new TabelaCategoria(new BigDecimal("250.00"), Periodicidade.DIARIA));

        Map<String, TabelaCategoria> tabelaEngenharia = new HashMap<>();
        tabelaEngenharia.put("hospedagem", new TabelaCategoria(BigDecimal.ZERO, Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ENG-PLATAFORMA", tabelaEngenharia);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        TabelaPoliticaResolvida resultado = ResolutorPoliticaCentroCusto.resolver("CC-ENG-PLATAFORMA", politica);

        assertTrue(resultado.getCategorias().containsKey("hospedagem"));
        TabelaCategoria hospedagem = resultado.getCategorias().get("hospedagem");
        assertEquals(0, BigDecimal.ZERO.compareTo(hospedagem.limite()));
        assertEquals(Periodicidade.DIARIA, hospedagem.periodicidade());
    }

    @Test
    @DisplayName("Imutabilidade: a resolução não altera os mapas de PoliticaExterna")
    void resolucaoNaoAlteraMapasDaPolitica() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", new TabelaCategoria(new BigDecimal("60.00"), Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaComercial = new HashMap<>();
        tabelaComercial.put("representacao", new TabelaCategoria(new BigDecimal("300.00"), Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-COMERCIAL", tabelaComercial);

        PoliticaExterna politica = construirPolitica(padrao, centrosCusto);

        int tamanhoPadraoAntes = politica.getPadrao().size();
        int tamanhoCentroAntes = politica.getCentrosCusto().get("CC-COMERCIAL").size();

        ResolutorPoliticaCentroCusto.resolver("CC-COMERCIAL", politica);
        ResolutorPoliticaCentroCusto.resolver(null, politica);
        ResolutorPoliticaCentroCusto.resolver("CC-DESCONHECIDO", politica);

        assertEquals(tamanhoPadraoAntes, politica.getPadrao().size());
        assertEquals(tamanhoCentroAntes, politica.getCentrosCusto().get("CC-COMERCIAL").size());
        assertTrue(politica.getPadrao().containsKey("alimentacao"));
        assertFalse(politica.getPadrao().containsKey("representacao"));
        assertFalse(politica.getCentrosCusto().get("CC-COMERCIAL").containsKey("alimentacao"));
    }
}
