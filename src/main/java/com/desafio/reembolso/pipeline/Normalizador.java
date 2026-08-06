package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Normaliza os campos estruturalmente válidos de valor e categoria (spec
 * RN-004, RN-005). Não avalia elegibilidade, não decide vocabulário fechado
 * de categoria (RN-007, T-009) e não produz motivos novos — apenas carrega
 * o valor e a categoria normalizados ao lado do {@link ItemValidado}
 * original, sem alterá-lo. O valor normalizado parte de
 * {@link ItemValidado#getValorConvertidoBruto()} — já resolvido por
 * {@link ResolutorCambio} (BRL ou moeda estrangeira convertida) —, nunca de
 * {@link ItemValidado#getValor()} diretamente (RN-020, plan §9).
 */
public final class Normalizador {

    private static final Pattern MARCAS_DIACRITICAS = Pattern.compile("\\p{M}");

    private Normalizador() {
    }

    public static ItemNormalizado normalizar(ItemValidado item) {
        BigDecimal valorNormalizado = normalizarValor(item.getValorConvertidoBruto());
        String categoriaNormalizada = normalizarCategoria(item.getCategoria());
        return new ItemNormalizado(item, valorNormalizado, categoriaNormalizada);
    }

    public static List<ItemNormalizado> normalizarLista(List<ItemValidado> itens) {
        List<ItemNormalizado> resultado = new ArrayList<>(itens.size());
        for (ItemValidado item : itens) {
            resultado.add(normalizar(item));
        }
        return List.copyOf(resultado);
    }

    private static BigDecimal normalizarValor(BigDecimal valor) {
        if (valor == null) {
            return null;
        }
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private static String normalizarCategoria(String categoria) {
        if (categoria == null) {
            return null;
        }
        String semEspacosNasPontas = categoria.trim();
        String minusculo = semEspacosNasPontas.toLowerCase(Locale.ROOT);
        String formaDecomposta = Normalizer.normalize(minusculo, Normalizer.Form.NFD);
        return MARCAS_DIACRITICAS.matcher(formaDecomposta).replaceAll("");
    }

    /**
     * Item validado acompanhado do valor e da categoria normalizados
     * (RN-004, RN-005). Mantém referência ao {@link ItemValidado} original
     * em vez de duplicar seus campos.
     */
    public record ItemNormalizado(
            ItemValidado item,
            BigDecimal valorNormalizado,
            String categoriaNormalizada
    ) {
    }
}
