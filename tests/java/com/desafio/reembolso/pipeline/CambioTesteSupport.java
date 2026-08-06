package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.TabelaCambio;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;

import java.util.List;
import java.util.Map;

/**
 * Helper exclusivo de teste (T-038): encadeia {@link ResolutorCambio} antes
 * de {@link Normalizador} para os testes históricos do pacote {@code
 * pipeline} cuja entrada vem de {@link ValidadorItem} — desde T-036,
 * {@code valorConvertidoBruto} só é preenchido por {@link ResolutorCambio},
 * inclusive para BRL. Não duplica nenhuma regra de câmbio: apenas chama
 * {@link ResolutorCambio} e {@link Normalizador}.
 */
final class CambioTesteSupport {

    /**
     * Tabela válida com moeda base BRL e cotações vazias — suficiente para
     * qualquer cenário BRL, já que {@link ResolutorCambio} não consulta a
     * tabela de taxas para BRL.
     */
    static final TabelaCambio TABELA_BRL = new TabelaCambio("BRL", Map.of());

    private CambioTesteSupport() {
    }

    static ItemValidado resolver(ItemValidado item) {
        return resolver(item, TABELA_BRL);
    }

    static ItemValidado resolver(ItemValidado item, TabelaCambio cambio) {
        return ResolutorCambio.resolver(item, cambio);
    }

    static List<ItemValidado> resolverLista(List<ItemValidado> itens) {
        return resolverLista(itens, TABELA_BRL);
    }

    static List<ItemValidado> resolverLista(List<ItemValidado> itens, TabelaCambio cambio) {
        return ResolutorCambio.resolverLista(itens, cambio);
    }

    static ItemNormalizado resolverENormalizar(ItemValidado item) {
        return resolverENormalizar(item, TABELA_BRL);
    }

    static ItemNormalizado resolverENormalizar(ItemValidado item, TabelaCambio cambio) {
        return Normalizador.normalizar(resolver(item, cambio));
    }

    static List<ItemNormalizado> resolverENormalizarLista(List<ItemValidado> itens) {
        return resolverENormalizarLista(itens, TABELA_BRL);
    }

    static List<ItemNormalizado> resolverENormalizarLista(List<ItemValidado> itens, TabelaCambio cambio) {
        return Normalizador.normalizarLista(resolverLista(itens, cambio));
    }
}
