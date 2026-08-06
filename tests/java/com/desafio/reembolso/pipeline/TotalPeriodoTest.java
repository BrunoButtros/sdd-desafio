package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.CompositorSaida.ResultadoItem;
import com.fasterxml.jackson.databind.node.DecimalNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Cobre RN-018 / CA-003 (spec 4.3, 4.7 excluído, 8.1 passo 11): o total do
 * período é exatamente a soma dos {@code valorReembolsavel} apresentados na
 * lista final, independentemente de decisão, motivo ou ordem. Constrói
 * {@link ResultadoItem} diretamente, sem executar o pipeline completo — o
 * fechamento ponta a ponta com R$ 585,43 pertence a T-020.
 */
@DisplayName("Total do período — RN-018 / CA-003")
class TotalPeriodoTest {

    private static ResultadoItem resultado(
            int indice,
            String valorReembolsavel,
            Decisao decisao,
            List<Motivo> motivos
    ) {
        return new ResultadoItem(
                indice,
                "d-" + indice,
                DecimalNode.valueOf(new BigDecimal(valorReembolsavel)),
                "BRL",
                BigDecimal.ONE,
                null,
                new BigDecimal(valorReembolsavel),
                new BigDecimal(valorReembolsavel),
                decisao,
                motivos
        );
    }

    private static ResultadoItem resultadoValorNaoPositivo(int indice, String valorNormalizado) {
        return new ResultadoItem(
                indice,
                "d-" + indice,
                DecimalNode.valueOf(new BigDecimal(valorNormalizado)),
                "BRL",
                BigDecimal.ONE,
                null,
                new BigDecimal(valorNormalizado),
                new BigDecimal("0.00"),
                Decisao.RECUSADO,
                List.of(new Motivo(MotivoCodigo.VALOR_NAO_POSITIVO, RegraNegocio.RN_006, null))
        );
    }

    // ---- 1. Lista arbitrária mista ---------------------------------------------

    @Test
    @DisplayName("1 — lista arbitrária mista: total exato, escala 2, igual à soma manual")
    void listaArbitrariaMista_totalExato() {
        List<ResultadoItem> resultados = List.of(
                resultado(1, "60.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()),
                resultado(2, "0.00", Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, List.of()),
                resultado(3, "80.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()),
                resultado(4, "0.00", Decisao.RECUSADO, List.of()),
                resultado(5, "54.90", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()),
                resultado(6, "250.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()),
                resultado(7, "33.33", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of())
        );

        BigDecimal total = SomadorTotal.somar(resultados);

        BigDecimal somaManual = new BigDecimal("60.00")
                .add(new BigDecimal("0.00"))
                .add(new BigDecimal("80.00"))
                .add(new BigDecimal("0.00"))
                .add(new BigDecimal("54.90"))
                .add(new BigDecimal("250.00"))
                .add(new BigDecimal("33.33"));

        assertEquals(new BigDecimal("478.23"), total);
        assertEquals(2, total.scale());
        assertEquals(somaManual, total);
    }

    // ---- 2. CA-003 independente de decisões ------------------------------------

    @Test
    @DisplayName("2 — decisões diferentes não filtram a soma: total é a soma literal dos valores apresentados")
    void decisoesDiferentes_naoFiltramASoma() {
        List<ResultadoItem> resultados = List.of(
                resultado(1, "10.00", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()),
                resultado(2, "20.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()),
                resultado(3, "0.00", Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, List.of()),
                resultado(4, "0.00", Decisao.RECUSADO, List.of()),
                resultado(5, "30.00", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of())
        );

        BigDecimal total = SomadorTotal.somar(resultados);

        assertEquals(new BigDecimal("60.00"), total);
    }

    // ---- 3. Valor não positivo não reduz o total -------------------------------

    @Test
    @DisplayName("3 — valor normalizado negativo (VALOR_NAO_POSITIVO) não reduz o total: 60 + 0 + 20 = 80, nunca 35")
    void valorNaoPositivo_naoReduzOTotal() {
        List<ResultadoItem> resultados = List.of(
                resultado(1, "60.00", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()),
                resultadoValorNaoPositivo(2, "-45.00"),
                resultado(3, "20.00", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of())
        );

        BigDecimal total = SomadorTotal.somar(resultados);

        assertEquals(new BigDecimal("80.00"), total);
        assertNotEqualsBigDecimal(new BigDecimal("35.00"), total);
    }

    private static void assertNotEqualsBigDecimal(BigDecimal naoEsperado, BigDecimal real) {
        if (naoEsperado.compareTo(real) == 0) {
            throw new AssertionError("total não deveria ser " + naoEsperado + ", mas foi " + real);
        }
    }

    // ---- 4. Lista vazia ---------------------------------------------------------

    @Test
    @DisplayName("4 — lista vazia retorna 0.00 com escala 2")
    void listaVazia_retornaZeroEscala2() {
        BigDecimal total = SomadorTotal.somar(List.of());

        assertEquals(new BigDecimal("0.00"), total);
        assertEquals(2, total.scale());
    }

    // ---- 5. Um único item ---------------------------------------------------------

    @Test
    @DisplayName("5 — um único item: total igual ao próprio valor")
    void umUnicoItem_totalIgualAoValor() {
        List<ResultadoItem> resultados = List.of(
                resultado(1, "33.33", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()));

        BigDecimal total = SomadorTotal.somar(resultados);

        assertEquals(new BigDecimal("33.33"), total);
    }

    // ---- 6. Precisão decimal ------------------------------------------------------

    @Test
    @DisplayName("6 — 0.01 + 0.02 + 0.10 + 0.20 = 0.33, sem erro binário")
    void precisaoDecimal_semErroBinario() {
        List<ResultadoItem> resultados = List.of(
                resultado(1, "0.01", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()),
                resultado(2, "0.02", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()),
                resultado(3, "0.10", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()),
                resultado(4, "0.20", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of())
        );

        BigDecimal total = SomadorTotal.somar(resultados);

        assertEquals(new BigDecimal("0.33"), total);
    }

    // ---- 7. Quantidade maior de parcelas --------------------------------------

    @Test
    @DisplayName("7 — cem resultados de 0.01 somam exatamente 1.00")
    void cemParcelasDeUmCentavo_somamUmReal() {
        List<ResultadoItem> resultados = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            resultados.add(resultado(i, "0.01", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()));
        }

        BigDecimal total = SomadorTotal.somar(resultados);

        assertEquals(new BigDecimal("1.00"), total);
    }

    // ---- 8. Ordem não interfere -----------------------------------------------

    @Test
    @DisplayName("8 — mesma lista em ordens diferentes produz o mesmo total")
    void ordemNaoInterfere_mesmoTotal() {
        List<ResultadoItem> ordemOriginal = List.of(
                resultado(1, "60.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()),
                resultado(2, "0.00", Decisao.RECUSADO, List.of()),
                resultado(3, "80.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()));

        List<ResultadoItem> ordemInvertida = new ArrayList<>(ordemOriginal);
        Collections.reverse(ordemInvertida);

        BigDecimal totalOriginal = SomadorTotal.somar(ordemOriginal);
        BigDecimal totalInvertido = SomadorTotal.somar(ordemInvertida);

        assertEquals(totalOriginal, totalInvertido);
        assertEquals(new BigDecimal("140.00"), totalOriginal);
    }

    // ---- 9. Lista não modificada ------------------------------------------------

    @Test
    @DisplayName("9 — a lista recebida não é alterada por somar")
    void listaNaoModificada() {
        List<ResultadoItem> resultados = new ArrayList<>(List.of(
                resultado(1, "60.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()),
                resultado(2, "80.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of())));
        List<ResultadoItem> copiaAntes = new ArrayList<>(resultados);

        SomadorTotal.somar(resultados);

        assertEquals(copiaAntes.size(), resultados.size());
        assertEquals(copiaAntes, resultados);
    }

    // ---- 10. Reaplicação sem estado compartilhado --------------------------------

    @Test
    @DisplayName("10 — chamadas independentes não compartilham estado")
    void reaplicacaoSemEstadoCompartilhado() {
        List<ResultadoItem> listaA = List.of(
                resultado(1, "60.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()));
        List<ResultadoItem> listaB = List.of(
                resultado(1, "80.00", Decisao.PARCIALMENTE_REEMBOLSADO, List.of()));

        BigDecimal resultadoA = SomadorTotal.somar(listaA);
        BigDecimal resultadoB = SomadorTotal.somar(listaB);
        BigDecimal resultadoANovamente = SomadorTotal.somar(listaA);

        assertEquals(new BigDecimal("60.00"), resultadoA);
        assertEquals(new BigDecimal("80.00"), resultadoB);
        assertEquals(resultadoA, resultadoANovamente);
    }

    // ---- 11. Lista nula -----------------------------------------------------------

    @Test
    @DisplayName("11 — lista nula lança NullPointerException")
    void listaNula_lancaNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> SomadorTotal.somar(null)
        );
    }

    // ---- 12. Elemento nulo ----------------------------------------------------------

    @Test
    @DisplayName("12 — elemento nulo dentro da lista lança NullPointerException, sem ser ignorado")
    void elementoNulo_lancaNullPointerException() {
        List<ResultadoItem> comElementoNulo = new ArrayList<>();
        comElementoNulo.add(resultado(1, "60.00", Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()));
        comElementoNulo.add(null);

        assertThrows(
                NullPointerException.class,
                () -> SomadorTotal.somar(comElementoNulo)
        );
    }
}
