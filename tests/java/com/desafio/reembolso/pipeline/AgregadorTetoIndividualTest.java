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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * T-044 / spec RN-013, RN-014, RN-019, CA-049, AMB-037; plan §11, DT-017:
 * cobre {@link AgregadorTetoIndividual}, que processa qualquer categoria com
 * {@link Periodicidade#DIARIA} na {@link TabelaPoliticaResolvida}, teto
 * individual por lançamento, sem saldo compartilhado entre lançamentos.
 */
@DisplayName("Teto individual por periodicidade — RN-013 / RN-019 / CA-049 (T-044)")
class AgregadorTetoIndividualTest {

    private static final LocalDate DATA_A = LocalDate.of(2026, 7, 14);
    private static final LocalDate DATA_B = LocalDate.of(2026, 7, 15);

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

    private static ItemAvaliado itemElegivel(int indiceEntrada, LocalDate data, String categoria,
                                              String descricao, String valor) {
        BigDecimal valorBd = new BigDecimal(valor);
        ItemValidado validado = new ItemValidado(
                indiceEntrada, "d-" + indiceEntrada, data, categoria, descricao, "Fornecedor",
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
        Motivo motivo = new Motivo(MotivoCodigo.NOTA_FISCAL_AUSENTE, RegraNegocio.RN_009, null);
        return new ItemAvaliado(normalizado, List.of(motivo), false, new BigDecimal("0.00"));
    }

    // ---- 1. Hospedagem reproduz o comportamento histórico -------------------------

    @Test
    @DisplayName("1 — hospedagem (limite 250.00, DIARIA): 480.00 rende 250.00 parcial, TETO_HOSPEDAGEM_APLICADO/RN-013")
    void hospedagem_reproduzComportamentoHistorico() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA",
                Map.of("hospedagem", cat("250.00", Periodicidade.DIARIA)));
        ItemAvaliado item = itemElegivel(1, DATA_A, "hospedagem", "2 diarias", "480.00");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item), tabela);

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertMonetario("250.00", resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(1, resultado.motivos().size());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_013, motivo.regra());
        assertNull(motivo.campo());
    }

    // ---- 2. Descrição não altera o teto --------------------------------------------

    @Test
    @DisplayName("2 — descrições diferentes, inclusive sugerindo mais de uma diária, não alteram o teto de 250.00")
    void descricao_naoAlteraTeto() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA",
                Map.of("hospedagem", cat("250.00", Periodicidade.DIARIA)));

        for (String descricao : List.of("2 diarias", "3 noites", "uma semana no hotel", "estadia")) {
            ItemAvaliado item = itemElegivel(1, DATA_A, "hospedagem", descricao, "480.00");
            List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item), tabela);

            assertEquals(1, resultados.size());
            assertMonetario("250.00", resultados.get(0).valorReembolsavel());
            assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        }
    }

    // ---- 3. Categoria externa — CA-049 ----------------------------------------------

    @Test
    @DisplayName("3 — CA-049: estacionamento (limite 50.00, DIARIA), despesa de 80.00 rende 50.00 parcial, TETO_INDIVIDUAL_APLICADO/RN-019")
    void categoriaExterna_ca049() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL",
                Map.of("estacionamento", cat("50.00", Periodicidade.DIARIA)));
        ItemAvaliado item = itemElegivel(1, DATA_A, "estacionamento", "Estacionamento", "80.00");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item), tabela);

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertMonetario("50.00", resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(1, resultado.motivos().size());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_INDIVIDUAL_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_019, motivo.regra());
        assertNull(motivo.campo());
        assertNotEquals(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, motivo.codigo());
        assertNotEquals(RegraNegocio.RN_013, motivo.regra());
    }

    // ---- 4. Valor dentro do limite ---------------------------------------------------

    @Test
    @DisplayName("4a — hospedagem abaixo do limite (249.99) é integral, sem motivos")
    void hospedagem_abaixoDoLimite_integralSemMotivos() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA",
                Map.of("hospedagem", cat("250.00", Periodicidade.DIARIA)));
        ItemAvaliado item = itemElegivel(1, DATA_A, "hospedagem", "Estadia", "249.99");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item), tabela);

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertMonetario("249.99", resultado.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado.decisao());
        assertTrue(resultado.motivos().isEmpty());
    }

    @Test
    @DisplayName("4b — hospedagem exatamente no limite (250.00) é integral, não parcial")
    void hospedagem_exatamenteNoLimite_integral() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA",
                Map.of("hospedagem", cat("250.00", Periodicidade.DIARIA)));
        ItemAvaliado item = itemElegivel(1, DATA_A, "hospedagem", "Estadia", "250.00");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item), tabela);

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertMonetario("250.00", resultado.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado.decisao());
        assertNotEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertTrue(resultado.motivos().isEmpty());
    }

    @Test
    @DisplayName("4c — categoria dinâmica exatamente no limite (50.00 de 50.00) é integral, sem motivos")
    void categoriaDinamica_exatamenteNoLimite_integral() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL",
                Map.of("estacionamento", cat("50.00", Periodicidade.DIARIA)));
        ItemAvaliado item = itemElegivel(1, DATA_A, "estacionamento", "Estacionamento", "50.00");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item), tabela);

        assertEquals(1, resultados.size());
        ResultadoTeto resultado = resultados.get(0);
        assertMonetario("50.00", resultado.valorReembolsavel());
        assertEquals(Decisao.INTEGRALMENTE_REEMBOLSADO, resultado.decisao());
        assertTrue(resultado.motivos().isEmpty());
    }

    // ---- 5. Ausência de saldo compartilhado -------------------------------------------

    @Test
    @DisplayName("5a — duas hospedagens de 480.00 na mesma data rendem 250.00 cada, somando 500.00, nunca esgotado")
    void hospedagem_semSaldoCompartilhado() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA",
                Map.of("hospedagem", cat("250.00", Periodicidade.DIARIA)));
        ItemAvaliado item1 = itemElegivel(1, DATA_A, "hospedagem", "Estadia 1", "480.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_A, "hospedagem", "Estadia 2", "480.00");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item1, item2), tabela);

        assertEquals(2, resultados.size());
        assertMonetario("250.00", resultados.get(0).valorReembolsavel());
        assertMonetario("250.00", resultados.get(1).valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultados.get(0).decisao());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultados.get(1).decisao());
        assertMonetario("500.00", resultados.get(0).valorReembolsavel().add(resultados.get(1).valorReembolsavel()));
        for (ResultadoTeto resultado : resultados) {
            assertNotEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, resultado.decisao());
            for (Motivo motivo : resultado.motivos()) {
                assertNotEquals(MotivoCodigo.TETO_DIARIO_ESGOTADO, motivo.codigo());
                assertNotEquals(RegraNegocio.RN_015, motivo.regra());
            }
        }
    }

    @Test
    @DisplayName("5b — duas despesas de categoria dinâmica DIARIA na mesma data rendem o limite individual cada, sem saldo compartilhado")
    void categoriaDinamica_semSaldoCompartilhado() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL",
                Map.of("estacionamento", cat("50.00", Periodicidade.DIARIA)));
        ItemAvaliado item1 = itemElegivel(1, DATA_A, "estacionamento", "Estacionamento 1", "80.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_A, "estacionamento", "Estacionamento 2", "80.00");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item1, item2), tabela);

        assertEquals(2, resultados.size());
        assertMonetario("50.00", resultados.get(0).valorReembolsavel());
        assertMonetario("50.00", resultados.get(1).valorReembolsavel());
        for (ResultadoTeto resultado : resultados) {
            assertNotEquals(Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, resultado.decisao());
            for (Motivo motivo : resultado.motivos()) {
                assertNotEquals(MotivoCodigo.TETO_DIARIO_ESGOTADO, motivo.codigo());
                assertNotEquals(RegraNegocio.RN_015, motivo.regra());
            }
        }
    }

    // ---- 6. Ordem da lista -------------------------------------------------------------

    @Test
    @DisplayName("6 — itens com indiceEntrada fora de ordem preservam a ordem física recebida na saída")
    void ordemFisicaRecebida_naoOrdenaPorIndiceEntrada() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA",
                Map.of("hospedagem", cat("250.00", Periodicidade.DIARIA)));
        ItemAvaliado item3 = itemElegivel(3, DATA_A, "hospedagem", "Estadia 3", "230.00");
        ItemAvaliado item1 = itemElegivel(1, DATA_A, "hospedagem", "Estadia 1", "480.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_A, "hospedagem", "Estadia 2", "100.00");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(List.of(item3, item1, item2), tabela);

        assertEquals(3, resultados.size());
        assertSame(item3, resultados.get(0).itemAvaliado());
        assertSame(item1, resultados.get(1).itemAvaliado());
        assertSame(item2, resultados.get(2).itemAvaliado());
        assertMonetario("230.00", resultados.get(0).valorReembolsavel());
        assertMonetario("250.00", resultados.get(1).valorReembolsavel());
        assertMonetario("100.00", resultados.get(2).valorReembolsavel());
    }

    // ---- 7. Exclusões -------------------------------------------------------------------

    @Test
    @DisplayName("7 — categoria com periodicidade DIA, categoria ausente, categoriaNormalizada null e item inelegível não participam")
    void exclusoes_naoParticipam() {
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-COMERCIAL", Map.of(
                "representacao", cat("300.00", Periodicidade.DIA),
                "estacionamento", cat("50.00", Periodicidade.DIARIA)
        ));

        ItemAvaliado periodicidadeDia = itemElegivel(1, DATA_A, "representacao", "Almoco", "100.00");
        ItemAvaliado categoriaAusente = itemElegivel(2, DATA_A, "coworking", "Coworking", "100.00");
        ItemAvaliado categoriaNula = itemElegivel(3, DATA_A, null, "Sem categoria", "100.00");
        ItemAvaliado itemInelegivel = itemInelegivel(4, DATA_A, "estacionamento", "100.00");

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(
                List.of(periodicidadeDia, categoriaAusente, categoriaNula, itemInelegivel), tabela);

        assertTrue(resultados.isEmpty());
    }

    // ---- 8. Imutabilidade ----------------------------------------------------------------

    @Test
    @DisplayName("8 — imutabilidade: lista recebida, lista devolvida, ItemAvaliado, motivos originais e tabela permanecem intactos")
    void imutabilidade() {
        Map<String, TabelaCategoria> categorias = new HashMap<>();
        categorias.put("hospedagem", cat("250.00", Periodicidade.DIARIA));
        TabelaPoliticaResolvida tabela = tabelaCentroCusto("CC-ENG-PLATAFORMA", categorias);
        int tamanhoOriginalCategorias = tabela.getCategorias().size();

        ItemAvaliado item1 = itemElegivel(1, DATA_A, "hospedagem", "Estadia 1", "480.00");
        ItemAvaliado item2 = itemElegivel(2, DATA_B, "hospedagem", "Estadia 2", "100.00");
        List<ItemAvaliado> entrada = new ArrayList<>(List.of(item1, item2));
        List<ItemAvaliado> copiaEntradaAntes = List.copyOf(entrada);
        List<Motivo> motivosOriginaisItem1 = item1.motivos();

        List<ResultadoTeto> resultados = AgregadorTetoIndividual.aplicar(entrada, tabela);

        assertEquals(copiaEntradaAntes, entrada, "lista recebida não pode ser reordenada nem alterada");
        assertSame(copiaEntradaAntes.get(0), entrada.get(0));
        assertSame(copiaEntradaAntes.get(1), entrada.get(1));

        assertThrows(UnsupportedOperationException.class, () -> resultados.add(resultados.get(0)),
                "lista devolvida deve ser não modificável");
        assertThrows(UnsupportedOperationException.class, () -> resultados.get(0).motivos().add(null));

        assertSame(motivosOriginaisItem1, item1.motivos(), "motivos originais do item não são substituídos");
        assertTrue(item1.motivos().isEmpty());

        assertEquals(tamanhoOriginalCategorias, tabela.getCategorias().size(), "tabela resolvida não é alterada");
        assertSame(item1, resultados.get(0).itemAvaliado());
        assertSame(item2, resultados.get(1).itemAvaliado());
    }
}
