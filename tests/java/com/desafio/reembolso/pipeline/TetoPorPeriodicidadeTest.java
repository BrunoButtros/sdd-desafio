package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.Periodicidade;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.modelo.TabelaCategoria;
import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * T-043 / spec RN-011, RN-012, RN-014, RN-015, RN-019, CA-047; plan §11,
 * DT-017: cobre a nova sobrecarga de {@link AgregadorTetoDiario} que decide
 * participação no teto compartilhado exclusivamente por
 * {@code periodicidade == DIA} na {@link TabelaPoliticaResolvida}, nunca
 * pelo conjunto histórico fixo de nomes de categoria.
 */
@DisplayName("Teto diário por política externa — RN-011 / RN-012 / RN-015 / RN-019 / CA-047 (T-043)")
class TetoPorPeriodicidadeTest {

    private static final LocalDate DATA_A = LocalDate.of(2026, 7, 10);
    private static final LocalDate DATA_B = LocalDate.of(2026, 7, 11);

    private static TabelaCategoria cat(String limite, Periodicidade periodicidade) {
        return new TabelaCategoria(new BigDecimal(limite), periodicidade);
    }

    private static TabelaPoliticaResolvida tabelaCentroCusto(String nomeCentroCusto,
                                                               Map<String, TabelaCategoria> categorias) {
        return new TabelaPoliticaResolvida(categorias, TabelaPoliticaResolvida.Origem.CENTRO_CUSTO, nomeCentroCusto);
    }

    private static void assertMonetario(String esperado, BigDecimal atual) {
        assertEquals(0, new BigDecimal(esperado).compareTo(atual),
                () -> "esperado " + esperado + " mas foi " + atual);
    }

    private static ItemAvaliado itemElegivel(int indiceEntrada, LocalDate data, String categoria, String valor) {
        BigDecimal valorBd = new BigDecimal(valor);
        ItemValidado validado = new ItemValidado(
                indiceEntrada, "d-" + indiceEntrada, data, categoria, "Descricao", "Fornecedor",
                valorBd, true, null, List.of(),
                "BRL", BigDecimal.ONE, null, valorBd);
        ItemNormalizado normalizado = new ItemNormalizado(validado, valorBd, categoria);
        return new ItemAvaliado(normalizado, List.of(), true, null);
    }

    private static ItemAvaliado itemInelegivel(int indiceEntrada, LocalDate data, String categoria, String valor) {
        BigDecimal valorBd = new BigDecimal(valor);
        ItemValidado validado = new ItemValidado(
                indiceEntrada, "d-" + indiceEntrada, data, categoria, "Descricao", "Fornecedor",
                valorBd, true, null, List.of(),
                "BRL", BigDecimal.ONE, null, valorBd);
        ItemNormalizado normalizado = new ItemNormalizado(validado, valorBd, categoria);
        Motivo motivo = new Motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null);
        return new ItemAvaliado(normalizado, List.of(motivo), false, new BigDecimal("0.00"));
    }

    // ---- 1. CA-047 — representacao compartilha saldo diário -------------------

    @Test
    @DisplayName("1 — CA-047: representacao (CC-COMERCIAL, dia, limite 300.00) compartilha saldo entre dois itens")
    void representacaoCompartilhaSaldoDiario() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL",
                Map.of("representacao", cat("300.00", Periodicidade.DIA)));

        ItemAvaliado item1 = itemElegivel(1, DATA_A, "representacao", "220.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_A, "representacao", "150.00");

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(List.of(item1, item2), tabela);

        assertEquals(2, resultados.size());

        ResultadoTeto resultado1 = resultados.get(0);
        assertMonetario("220.00", resultado1.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado1.decisao());
        assertTrue(resultado1.motivos().isEmpty());

        ResultadoTeto resultado2 = resultados.get(1);
        assertMonetario("80.00", resultado2.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado2.decisao());
        assertEquals(1, resultado2.motivos().size());
        Motivo motivo = resultado2.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_019, motivo.regra());
        assertNull(motivo.campo());

        boolean usouMotivoHistorico = resultados.stream()
                .flatMap(r -> r.motivos().stream())
                .anyMatch(m -> m.regra() == RegraNegocio.RN_011 || m.regra() == RegraNegocio.RN_012);
        assertFalse(usouMotivoHistorico, "representacao nunca usa RN-011/RN-012");
    }

    // ---- 2. Consumo por indiceEntrada ------------------------------------------

    @Test
    @DisplayName("2 — consumo segue indiceEntrada, não a ordem física da lista recebida")
    void consumoSeguirIndiceEntradaNaoOrdemDaLista() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL",
                Map.of("representacao", cat("300.00", Periodicidade.DIA)));

        ItemAvaliado item1 = itemElegivel(1, DATA_A, "representacao", "220.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_A, "representacao", "150.00");

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(List.of(item2, item1), tabela);

        assertEquals(2, resultados.size());
        assertSame(item2, resultados.get(0).itemAvaliado());
        assertSame(item1, resultados.get(1).itemAvaliado());

        ResultadoTeto resultadoItem2 = resultados.get(0);
        ResultadoTeto resultadoItem1 = resultados.get(1);
        assertMonetario("220.00", resultadoItem1.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultadoItem1.decisao());
        assertMonetario("80.00", resultadoItem2.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultadoItem2.decisao());
    }

    // ---- 3. Saldo esgotado -------------------------------------------------------

    @Test
    @DisplayName("3 — saldo esgotado: terceiro item recebe NAO_REEMBOLSADO_TETO_ESGOTADO/TETO_DIARIO_ESGOTADO/RN-015")
    void saldoEsgotado_terceiroItemNaoRecebeNada() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL",
                Map.of("representacao", cat("300.00", Periodicidade.DIA)));

        ItemAvaliado item1 = itemElegivel(1, DATA_A, "representacao", "220.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_A, "representacao", "80.00");
        ItemAvaliado item3 = itemElegivel(3, DATA_A, "representacao", "50.00");

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(List.of(item1, item2, item3), tabela);

        assertEquals(3, resultados.size());
        assertMonetario("220.00", resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertMonetario("80.00", resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());

        ResultadoTeto terceiro = resultados.get(2);
        assertMonetario("0.00", terceiro.valorReembolsavel());
        assertEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, terceiro.decisao());
        assertEquals(1, terceiro.motivos().size());
        Motivo motivo = terceiro.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_DIARIO_ESGOTADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_015, motivo.regra());
        assertNull(motivo.campo());
    }

    // ---- 4. Separação por data -----------------------------------------------------

    @Test
    @DisplayName("4 — datas diferentes recebem saldos independentes de 300.00")
    void datasDiferentes_saldosIndependentes() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL",
                Map.of("representacao", cat("300.00", Periodicidade.DIA)));

        ItemAvaliado item1 = itemElegivel(1, DATA_A, "representacao", "300.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_B, "representacao", "300.00");

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(List.of(item1, item2), tabela);

        assertEquals(2, resultados.size());
        assertMonetario("300.00", resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertMonetario("300.00", resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());
    }

    // ---- 5. Separação por categoria -------------------------------------------------

    @Test
    @DisplayName("5 — categorias diferentes com periodicidade DIA têm saldos independentes na mesma data")
    void categoriasDiferentes_saldosIndependentes() {
        Map<String, TabelaCategoria> categorias = new HashMap<>();
        categorias.put("representacao", cat("300.00", Periodicidade.DIA));
        categorias.put("consultoria", cat("200.00", Periodicidade.DIA));
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL", categorias);

        ItemAvaliado item1 = itemElegivel(1, DATA_A, "representacao", "300.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_A, "consultoria", "200.00");

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(List.of(item1, item2), tabela);

        assertEquals(2, resultados.size());
        assertMonetario("300.00", resultados.get(0).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertMonetario("200.00", resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultados.get(1).decisao());
    }

    // ---- 6. Periodicidade DIARIA não participa --------------------------------------

    @Test
    @DisplayName("6 — categoria com periodicidade DIARIA não participa do teto diário compartilhado")
    void periodicidadeDiaria_naoParticipa() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA",
                Map.of("hospedagem", cat("250.00", Periodicidade.DIARIA)));

        ItemAvaliado item = itemElegivel(1, DATA_A, "hospedagem", "480.00");

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(List.of(item), tabela);

        assertTrue(resultados.isEmpty());
    }

    // ---- 7. Categoria ausente, categoriaNormalizada null e item inelegível ----------

    @Test
    @DisplayName("7 — categoria ausente da tabela, categoriaNormalizada null e item inelegível não participam")
    void categoriaAusenteNullEItemInelegivel_naoParticipam() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL",
                Map.of("representacao", cat("300.00", Periodicidade.DIA)));

        ItemAvaliado categoriaAusente = itemElegivel(1, DATA_A, "coworking", "100.00");
        ItemAvaliado categoriaNula = itemElegivel(2, DATA_A, null, "100.00");
        ItemAvaliado itemInelegivel = itemInelegivel(3, DATA_A, "representacao", "100.00");

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(
                List.of(categoriaAusente, categoriaNula, itemInelegivel), tabela);

        assertTrue(resultados.isEmpty());
    }

    // ---- 8. Regras históricas preservadas -----------------------------------------

    @Test
    @DisplayName("8 — regras históricas: alimentacao usa RN-011, transporte_urbano usa RN-012 na nova sobrecarga")
    void regrasHistoricasPreservadasNaNovaSobrecarga() {
        Map<String, TabelaCategoria> categorias = new HashMap<>();
        categorias.put("alimentacao", cat("60.00", Periodicidade.DIA));
        categorias.put("transporte_urbano", cat("80.00", Periodicidade.DIA));
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA", categorias);

        ItemAvaliado alimentacao = itemElegivel(1, DATA_A, "alimentacao", "72.50");
        ItemAvaliado transporte = itemElegivel(2, DATA_B, "transporte_urbano", "100.00");

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(List.of(alimentacao, transporte), tabela);

        assertEquals(2, resultados.size());

        ResultadoTeto resultadoAlimentacao = resultados.get(0);
        assertMonetario("60.00", resultadoAlimentacao.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultadoAlimentacao.decisao());
        assertEquals(1, resultadoAlimentacao.motivos().size());
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, resultadoAlimentacao.motivos().get(0).codigo());
        assertEquals(RegraNegocio.RN_011, resultadoAlimentacao.motivos().get(0).regra());

        ResultadoTeto resultadoTransporte = resultados.get(1);
        assertMonetario("80.00", resultadoTransporte.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultadoTransporte.decisao());
        assertEquals(1, resultadoTransporte.motivos().size());
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, resultadoTransporte.motivos().get(0).codigo());
        assertEquals(RegraNegocio.RN_012, resultadoTransporte.motivos().get(0).regra());
    }

    // ---- 9. Imutabilidade -----------------------------------------------------------

    @Test
    @DisplayName("9 — imutabilidade: lista recebida, lista devolvida, itens originais e tabela permanecem intactos")
    void imutabilidade_naoAlteraEntradasNemTabela() {
        Map<String, TabelaCategoria> categorias = new HashMap<>();
        categorias.put("representacao", cat("300.00", Periodicidade.DIA));
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL", categorias);
        int tamanhoOriginalCategorias = tabela.getCategorias().size();

        ItemAvaliado item1 = itemElegivel(2, DATA_A, "representacao", "150.00");
        ItemAvaliado item2 = itemElegivel(1, DATA_A, "representacao", "220.00");
        List<ItemAvaliado> entrada = new ArrayList<>(List.of(item1, item2));
        List<ItemAvaliado> copiaEntradaAntes = List.copyOf(entrada);
        List<Motivo> motivosOriginaisItem1 = item1.motivos();

        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(entrada, tabela);

        assertEquals(copiaEntradaAntes, entrada, "lista recebida não pode ser reordenada nem alterada");
        assertThrows(UnsupportedOperationException.class, () -> resultados.add(resultados.get(0)),
                "lista devolvida deve ser não modificável");

        assertSame(motivosOriginaisItem1, item1.motivos(), "motivos originais do item não são substituídos");
        assertTrue(item1.motivos().isEmpty());

        assertEquals(tamanhoOriginalCategorias, tabela.getCategorias().size(), "tabela resolvida não é alterada");
        assertSame(item1, resultados.stream()
                .filter(r -> r.itemAvaliado() == item1)
                .findFirst()
                .orElseThrow()
                .itemAvaliado());
    }
}
