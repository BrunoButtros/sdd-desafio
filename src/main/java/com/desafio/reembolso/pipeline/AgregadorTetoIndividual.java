package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.Periodicidade;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.modelo.TabelaCategoria;
import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aplica o teto individual por lançamento, sem saldo compartilhado, a
 * qualquer categoria configurada com {@link Periodicidade#DIARIA} na tabela
 * de política resolvida (spec RN-013, RN-019, AMB-037; plan §11, DT-017) —
 * não apenas {@code hospedagem}. Reaproveita o corte parcial de
 * {@link AgregadorTetoDiario#aplicarCorte}, em substituição a
 * {@link AgregadorTetoHospedagem} (mantido para a suíte histórica até
 * T-055/T-056). {@code hospedagem} produz
 * {@link MotivoCodigo#TETO_HOSPEDAGEM_APLICADO}/{@link RegraNegocio#RN_013};
 * qualquer outra categoria sob esse mecanismo produz
 * {@link MotivoCodigo#TETO_INDIVIDUAL_APLICADO}/{@link RegraNegocio#RN_019}.
 */
public final class AgregadorTetoIndividual {

    private static final Motivo MOTIVO_TETO_HOSPEDAGEM =
            new Motivo(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, RegraNegocio.RN_013, null);
    private static final Motivo MOTIVO_TETO_INDIVIDUAL =
            new Motivo(MotivoCodigo.TETO_INDIVIDUAL_APLICADO, RegraNegocio.RN_019, null);

    private AgregadorTetoIndividual() {
    }

    public static List<ResultadoTeto> aplicar(List<ItemAvaliado> itens, TabelaPoliticaResolvida tabela) {
        Objects.requireNonNull(itens, "itens");
        Objects.requireNonNull(tabela, "tabela");

        List<ResultadoTeto> resultado = new ArrayList<>();
        for (ItemAvaliado item : itens) {
            if (!item.elegivel()) {
                continue;
            }
            String categoria = item.itemNormalizado().categoriaNormalizada();
            if (categoria == null) {
                continue;
            }
            TabelaCategoria configuracao = tabela.getCategorias().get(categoria);
            if (configuracao == null || configuracao.periodicidade() != Periodicidade.DIARIA) {
                continue;
            }
            Motivo motivo = "hospedagem".equals(categoria) ? MOTIVO_TETO_HOSPEDAGEM : MOTIVO_TETO_INDIVIDUAL;
            resultado.add(AgregadorTetoDiario.aplicarCorte(item, configuracao.limite(), motivo));
        }
        return List.copyOf(resultado);
    }
}
