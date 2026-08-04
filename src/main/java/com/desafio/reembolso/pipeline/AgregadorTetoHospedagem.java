package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.PoliticaReembolso;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aplica o teto individual de hospedagem (spec RN-013; plan §2, passo 9):
 * cada lançamento de {@code hospedagem} elegível é avaliado isoladamente
 * contra {@link PoliticaReembolso#getLimiteIndividualHospedagem()}, sem
 * agregação por data nem saldo compartilhado entre lançamentos. Reaproveita
 * o corte parcial de {@link AgregadorTetoDiario#aplicarCorte} (RN-014).
 */
public final class AgregadorTetoHospedagem {

    private static final Motivo MOTIVO_TETO_HOSPEDAGEM =
            new Motivo(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, RegraNegocio.RN_013, null);

    private static final AgregadorTetoHospedagem PADRAO =
            new AgregadorTetoHospedagem(PoliticaReembolso.padrao());

    private final PoliticaReembolso politica;

    private AgregadorTetoHospedagem(PoliticaReembolso politica) {
        this.politica = Objects.requireNonNull(politica);
    }

    public static List<ResultadoTeto> aplicar(List<ItemAvaliado> itens) {
        return PADRAO.aplicarInterno(itens);
    }

    private List<ResultadoTeto> aplicarInterno(List<ItemAvaliado> itens) {
        List<ResultadoTeto> resultado = new ArrayList<>();
        for (ItemAvaliado item : itens) {
            if (item.elegivel() && "hospedagem".equals(item.itemNormalizado().categoriaNormalizada())) {
                resultado.add(AgregadorTetoDiario.aplicarCorte(
                        item, politica.getLimiteIndividualHospedagem(), MOTIVO_TETO_HOSPEDAGEM));
            }
        }
        return List.copyOf(resultado);
    }
}
