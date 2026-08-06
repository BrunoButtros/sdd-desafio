package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-010 (atualizada), CA-033 e AMB-028 (spec §12, plan §12): {@code moeda}
 * entra como componente adicional da chave de duplicidade, ao lado do valor já
 * convertido para BRL — nunca como substituto dele. Itens em moedas diferentes
 * nunca colidem, mesmo com valor convertido coincidente; o comportamento
 * histórico (mesma moeda, chave idêntica) permanece inalterado. Os itens são
 * construídos diretamente no estado de {@link ItemNormalizado}/{@link ItemAvaliado}
 * desta etapa, via o construtor de catorze argumentos de {@link ItemValidado},
 * para isolar exclusivamente a chave de duplicidade.
 */
@DisplayName("Duplicidade entre moedas diferentes — RN-010 (atualizada) / CA-033 / AMB-028")
class DuplicidadeEntreMoedasTest {

    private static final LocalDate DATA = LocalDate.of(2026, 7, 9);
    private static final String CATEGORIA_NORMALIZADA = "alimentacao";
    private static final String FORNECEDOR = "Restaurante X";
    private static final String DESCRICAO = "Almoco";
    private static final BigDecimal VALOR_NORMALIZADO = new BigDecimal("100.00");

    private static ItemValidado itemValidado(int indiceEntrada, String id, String moeda,
                                              BigDecimal valor, BigDecimal taxaCambioAplicada,
                                              LocalDate dataCotacaoUtilizada, BigDecimal valorConvertidoBruto) {
        return new ItemValidado(
                indiceEntrada, id, DATA, "alimentacao", DESCRICAO, FORNECEDOR, valor, true,
                null, List.of(), moeda, taxaCambioAplicada, dataCotacaoUtilizada, valorConvertidoBruto);
    }

    private static ItemNormalizado normalizado(ItemValidado item, BigDecimal valorNormalizado) {
        return new ItemNormalizado(item, valorNormalizado, CATEGORIA_NORMALIZADA);
    }

    private static ItemAvaliado elegivel(ItemNormalizado itemNormalizado) {
        return new ItemAvaliado(itemNormalizado, List.of(), true, null);
    }

    private static ItemAvaliado itemEurElegivel(int indiceEntrada, String id) {
        ItemValidado validado = itemValidado(indiceEntrada, id, "EUR",
                new BigDecimal("20.00"), new BigDecimal("5.00"), DATA, new BigDecimal("100.0000"));
        return elegivel(normalizado(validado, VALOR_NORMALIZADO));
    }

    private static ItemAvaliado itemBrlElegivel(int indiceEntrada, String id) {
        ItemValidado validado = itemValidado(indiceEntrada, id, "BRL",
                new BigDecimal("100.00"), BigDecimal.ONE, null, new BigDecimal("100.00"));
        return elegivel(normalizado(validado, VALOR_NORMALIZADO));
    }

    private static Motivo duplicidade() {
        return new Motivo(MotivoCodigo.DUPLICIDADE, RegraNegocio.RN_010, null);
    }

    // ---- 1. Moedas diferentes não são duplicatas (CA-033) ----------------------------------

    @Test
    @DisplayName("1 — EUR e BRL com mesmo valor convertido, mesma data/categoria/fornecedor/descricao: não são duplicatas")
    void cenario1_moedasDiferentesNaoSaoDuplicatas() {
        ItemAvaliado itemEur = itemEurElegivel(1, "d-eur");
        ItemAvaliado itemBrl = itemBrlElegivel(2, "d-brl");

        List<ItemAvaliado> resultado = DetectorDuplicidadeEconomica.detectar(List.of(itemEur, itemBrl));

        assertEquals(2, resultado.size());

        assertTrue(resultado.get(0).elegivel());
        assertTrue(resultado.get(1).elegivel());

        assertFalse(resultado.get(0).motivos().contains(duplicidade()));
        assertFalse(resultado.get(1).motivos().contains(duplicidade()));
        assertTrue(resultado.get(0).motivos().stream().noneMatch(m -> m.regra() == RegraNegocio.RN_010));
        assertTrue(resultado.get(1).motivos().stream().noneMatch(m -> m.regra() == RegraNegocio.RN_010));

        assertNull(resultado.get(0).valorReembolsavel());
        assertNull(resultado.get(1).valorReembolsavel());

        assertSame(itemEur, resultado.get(0));
        assertSame(itemBrl, resultado.get(1));
    }

    // ---- 2. Mesma moeda continua duplicando -------------------------------------------------

    @Test
    @DisplayName("2 — mesma moeda (EUR), mesma chave econômica: menor indiceEntrada permanece elegível, "
            + "mesmo com a lista recebida em ordem física inversa")
    void cenario2_mesmaMoedaContinuaDuplicando() {
        ItemAvaliado menorIndice = itemEurElegivel(1, "d-001");
        ItemAvaliado maiorIndice = itemEurElegivel(2, "d-002");

        List<ItemAvaliado> resultado = DetectorDuplicidadeEconomica.detectar(List.of(maiorIndice, menorIndice));

        assertEquals(2, resultado.size());

        ItemAvaliado primeiroFisico = resultado.get(0);
        assertEquals(2, primeiroFisico.itemNormalizado().item().getIndiceEntrada());
        assertFalse(primeiroFisico.elegivel());
        assertEquals(1, primeiroFisico.motivos().size());
        Motivo motivo = primeiroFisico.motivos().get(0);
        assertEquals(MotivoCodigo.DUPLICIDADE, motivo.codigo());
        assertEquals(RegraNegocio.RN_010, motivo.regra());
        assertNull(motivo.campo());
        assertEquals(new BigDecimal("0.00"), primeiroFisico.valorReembolsavel());
        assertEquals(2, primeiroFisico.valorReembolsavel().scale());

        ItemAvaliado segundoFisico = resultado.get(1);
        assertEquals(1, segundoFisico.itemNormalizado().item().getIndiceEntrada());
        assertTrue(segundoFisico.elegivel());
        assertFalse(segundoFisico.motivos().contains(duplicidade()));
        assertSame(menorIndice, segundoFisico);
    }

    // ---- 3. Moedas iguais em BRL preservam o comportamento histórico -------------------------

    @Test
    @DisplayName("3 — BRL e BRL com chave econômica idêntica continuam sendo tratados como duplicatas")
    void cenario3_moedasIguaisEmBrlPreservamComportamentoHistorico() {
        ItemAvaliado primeiro = itemBrlElegivel(1, "d-brl-1");
        ItemAvaliado segundo = itemBrlElegivel(2, "d-brl-2");

        List<ItemAvaliado> resultado = DetectorDuplicidadeEconomica.detectar(List.of(primeiro, segundo));

        assertEquals(2, resultado.size());
        assertTrue(resultado.get(0).elegivel());
        assertFalse(resultado.get(0).motivos().contains(duplicidade()));
        assertSame(primeiro, resultado.get(0));

        assertFalse(resultado.get(1).elegivel());
        assertTrue(resultado.get(1).motivos().contains(duplicidade()));
        assertEquals(new BigDecimal("0.00"), resultado.get(1).valorReembolsavel());
    }

    // ---- 4. Moeda é o único componente diferente ---------------------------------------------

    @Test
    @DisplayName("4 — data, categoria normalizada, valorNormalizado, fornecedor e descricao iguais; "
            + "apenas moeda difere: ainda assim nenhum item recebe DUPLICIDADE")
    void cenario4_moedaEhOUnicoComponenteDiferente() {
        ItemAvaliado itemEur = itemEurElegivel(1, "d-eur");
        ItemAvaliado itemBrl = itemBrlElegivel(2, "d-brl");

        ItemNormalizado normalizadoEur = itemEur.itemNormalizado();
        ItemNormalizado normalizadoBrl = itemBrl.itemNormalizado();

        assertEquals(normalizadoEur.item().getData(), normalizadoBrl.item().getData());
        assertEquals(normalizadoEur.categoriaNormalizada(), normalizadoBrl.categoriaNormalizada());
        assertEquals(0, normalizadoEur.valorNormalizado().compareTo(normalizadoBrl.valorNormalizado()));
        assertEquals(normalizadoEur.item().getFornecedor(), normalizadoBrl.item().getFornecedor());
        assertEquals(normalizadoEur.item().getDescricao(), normalizadoBrl.item().getDescricao());
        assertFalse(normalizadoEur.item().getMoeda().equals(normalizadoBrl.item().getMoeda()));

        List<ItemAvaliado> resultado = DetectorDuplicidadeEconomica.detectar(List.of(itemEur, itemBrl));

        assertFalse(resultado.get(0).motivos().contains(duplicidade()));
        assertFalse(resultado.get(1).motivos().contains(duplicidade()));
    }

    // ---- 5. Imutabilidade ----------------------------------------------------------------------

    @Test
    @DisplayName("5 — imutabilidade: lista recebida preservada, lista retornada não modificável, "
            + "item não duplicado preservado por referência, motivos originais não alterados")
    void cenario5_imutabilidade() {
        ItemAvaliado menorIndice = itemEurElegivel(1, "d-001");
        ItemAvaliado maiorIndice = itemEurElegivel(2, "d-002");
        List<Motivo> motivosOriginaisMenorIndice = menorIndice.motivos();
        List<Motivo> motivosOriginaisMaiorIndice = maiorIndice.motivos();

        List<ItemAvaliado> entrada = List.of(maiorIndice, menorIndice);

        List<ItemAvaliado> resultado = DetectorDuplicidadeEconomica.detectar(entrada);

        assertEquals(2, entrada.size());
        assertSame(maiorIndice, entrada.get(0));
        assertSame(menorIndice, entrada.get(1));

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(menorIndice));

        assertEquals(2, resultado.size());
        assertSame(menorIndice, resultado.get(1));

        assertEquals(motivosOriginaisMenorIndice, menorIndice.motivos());
        assertEquals(motivosOriginaisMaiorIndice, maiorIndice.motivos());
        assertTrue(motivosOriginaisMaiorIndice.isEmpty());
    }
}
