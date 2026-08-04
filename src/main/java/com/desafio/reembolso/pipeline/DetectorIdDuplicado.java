package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Identifica {@code despesa.id} estruturalmente válidos e repetidos (spec
 * 4.2, RN-003, CA-019) e recusa **todas** as ocorrências correspondentes com
 * {@code ID_DUPLICADO} — não existe "primeira ocorrência preservada" (8.4.7).
 * Um {@code id} estruturalmente inválido ({@link ItemValidado#getId()} nulo)
 * não participa da contagem e não contamina outros itens com {@code id}
 * nulo entre si.
 */
public final class DetectorIdDuplicado {

    private DetectorIdDuplicado() {
    }

    public static List<ItemValidado> detectar(List<ItemValidado> itens) {
        Map<String, Long> ocorrenciasPorId = new HashMap<>();
        for (ItemValidado item : itens) {
            if (item.getId() != null) {
                ocorrenciasPorId.merge(item.getId(), 1L, Long::sum);
            }
        }

        List<ItemValidado> resultado = new ArrayList<>(itens.size());
        for (ItemValidado item : itens) {
            String id = item.getId();
            if (id != null && ocorrenciasPorId.get(id) > 1) {
                resultado.add(comIdDuplicado(item));
            } else {
                resultado.add(item);
            }
        }
        return List.copyOf(resultado);
    }

    private static ItemValidado comIdDuplicado(ItemValidado item) {
        List<Motivo> motivos = new ArrayList<>(item.getMotivos());
        motivos.add(new Motivo(MotivoCodigo.ID_DUPLICADO, RegraNegocio.RN_003, CampoCanonico.ID));
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
                motivos);
    }
}
