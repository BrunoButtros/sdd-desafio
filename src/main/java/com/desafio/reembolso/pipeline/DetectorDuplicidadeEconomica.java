package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Detecta duplicidade econômica (spec RN-010, CA-013, CA-014) entre itens
 * elegíveis: mesma {@code data}, categoria normalizada, valor normalizado,
 * fornecedor e descrição como recebidos ({@code despesa.id} e
 * {@code tem_nota_fiscal} não integram a chave). A ocorrência de menor
 * {@code indiceEntrada} por chave permanece elegível; as demais recebem
 * {@code DUPLICIDADE}. Itens já inelegíveis, se recebidos diretamente, são
 * preservados sem alteração e não participam da chave (8.4.11).
 */
public final class DetectorDuplicidadeEconomica {

    private static final Motivo DUPLICIDADE =
            new Motivo(MotivoCodigo.DUPLICIDADE, RegraNegocio.RN_010, null);
    private static final BigDecimal ZERO_ESCALA_2 = new BigDecimal("0.00");

    private DetectorDuplicidadeEconomica() {
    }

    public static List<ItemAvaliado> detectar(List<ItemAvaliado> itens) {
        Map<ChaveDuplicidade, Integer> menorIndicePorChave = new HashMap<>();
        for (ItemAvaliado item : itens) {
            if (!item.elegivel()) {
                continue;
            }
            ChaveDuplicidade chave = chaveDe(item);
            int indiceEntrada = item.itemNormalizado().item().getIndiceEntrada();
            menorIndicePorChave.merge(chave, indiceEntrada, Math::min);
        }

        List<ItemAvaliado> resultado = new ArrayList<>(itens.size());
        for (ItemAvaliado item : itens) {
            if (!item.elegivel()) {
                resultado.add(item);
                continue;
            }
            ChaveDuplicidade chave = chaveDe(item);
            int indiceEntrada = item.itemNormalizado().item().getIndiceEntrada();
            int menorIndice = menorIndicePorChave.get(chave);
            resultado.add(indiceEntrada == menorIndice ? item : comDuplicidade(item));
        }
        return List.copyOf(resultado);
    }

    private static ChaveDuplicidade chaveDe(ItemAvaliado item) {
        return new ChaveDuplicidade(
                item.itemNormalizado().item().getData(),
                item.itemNormalizado().categoriaNormalizada(),
                item.itemNormalizado().valorNormalizado(),
                item.itemNormalizado().item().getFornecedor(),
                item.itemNormalizado().item().getDescricao()
        );
    }

    private static ItemAvaliado comDuplicidade(ItemAvaliado item) {
        List<Motivo> motivos = new ArrayList<>(item.motivos());
        if (!motivos.contains(DUPLICIDADE)) {
            motivos.add(DUPLICIDADE);
        }
        return new ItemAvaliado(item.itemNormalizado(), motivos, false, ZERO_ESCALA_2);
    }

    private record ChaveDuplicidade(
            LocalDate data,
            String categoriaNormalizada,
            BigDecimal valorNormalizado,
            String fornecedor,
            String descricao
    ) {
    }
}
