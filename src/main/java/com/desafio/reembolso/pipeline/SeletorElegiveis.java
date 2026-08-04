package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;

import java.util.ArrayList;
import java.util.List;

/**
 * Seleciona, preservando ordem e referências, os {@link ItemAvaliado} cujo
 * {@code elegivel()} seja {@code true} (spec 8.1, passos 6 e 8). Não
 * inspeciona códigos de motivo individualmente — a elegibilidade já foi
 * decidida pela etapa anterior do pipeline. Reutilizável tanto antes quanto
 * depois de {@link DetectorDuplicidadeEconomica}.
 */
public final class SeletorElegiveis {

    private SeletorElegiveis() {
    }

    public static List<ItemAvaliado> selecionar(List<ItemAvaliado> itens) {
        List<ItemAvaliado> resultado = new ArrayList<>();
        for (ItemAvaliado item : itens) {
            if (item.elegivel()) {
                resultado.add(item);
            }
        }
        return List.copyOf(resultado);
    }
}
