package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.modelo.TabelaCambio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Resolve a conversão cambial de cada item (spec 8.1, passo 5; RN-020;
 * plan §9): consome {@code valor}, {@code moeda} e {@code data} já
 * estruturalmente validados por {@link ValidadorItem} e popula os três
 * campos derivados de câmbio de {@link ItemValidado} — sem arredondar
 * {@code valorConvertidoBruto} (DT-015; o arredondamento é responsabilidade
 * exclusiva do {@code Normalizador}, T-038).
 */
public final class ResolutorCambio {

    private ResolutorCambio() {
    }

    public static ItemValidado resolver(ItemValidado item, TabelaCambio cambio) {
        BigDecimal valor = item.getValor();
        String moeda = item.getMoeda();
        LocalDate data = item.getData();

        if (valor == null || moeda == null || data == null) {
            return item;
        }

        if ("BRL".equals(moeda)) {
            return copiarComDerivados(item, BigDecimal.ONE, null, valor, item.getMotivos());
        }

        Optional<TabelaCambio.CotacaoResolvida> cotacao = cambio.cotacaoEm(moeda, data);
        if (cotacao.isPresent()) {
            TabelaCambio.CotacaoResolvida resolvida = cotacao.get();
            BigDecimal valorConvertidoBruto = valor.multiply(resolvida.taxa());
            return copiarComDerivados(item, resolvida.taxa(), resolvida.data(), valorConvertidoBruto,
                    item.getMotivos());
        }

        List<Motivo> motivos = new ArrayList<>(item.getMotivos());
        motivos.add(new Motivo(MotivoCodigo.MOEDA_SEM_COTACAO, RegraNegocio.RN_020, CampoCanonico.MOEDA));
        return copiarComDerivados(item, null, null, null, motivos);
    }

    public static List<ItemValidado> resolverLista(List<ItemValidado> itens, TabelaCambio cambio) {
        List<ItemValidado> resultado = new ArrayList<>();
        for (ItemValidado item : itens) {
            resultado.add(resolver(item, cambio));
        }
        return List.copyOf(resultado);
    }

    private static ItemValidado copiarComDerivados(ItemValidado item,
                                                     BigDecimal taxaCambioAplicada,
                                                     LocalDate dataCotacaoUtilizada,
                                                     BigDecimal valorConvertidoBruto,
                                                     List<Motivo> motivos) {
        return new ItemValidado(
                item.getIndiceEntrada(),
                item.getId(),
                item.getData(),
                item.getCategoria(),
                item.getDescricao(),
                item.getFornecedor(),
                item.getValor(),
                item.getTemNotaFiscal(),
                item.getValorInformado(),
                motivos,
                item.getMoeda(),
                taxaCambioAplicada,
                dataCotacaoUtilizada,
                valorConvertidoBruto);
    }
}
