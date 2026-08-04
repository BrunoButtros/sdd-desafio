package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.PoliticaReembolso;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Agrega o saldo diário de {@code alimentacao} e {@code transporte_urbano}
 * por {@code data} e categoria normalizada (spec RN-011, RN-012, RN-014,
 * RN-015; plan §2, passo 9), consumindo-o em ordem crescente de
 * {@code indiceEntrada} e devolvendo os resultados na ordem em que os itens
 * aplicáveis foram recebidos. Hospedagem e itens inelegíveis não participam
 * (spec 8.4, 8.5) — hospedagem é tratada pela T-014.
 */
public final class AgregadorTetoDiario {

    private static final Set<String> CATEGORIAS_TETO_DIARIO = Set.of("alimentacao", "transporte_urbano");
    private static final BigDecimal ZERO_ESCALA_2 = new BigDecimal("0.00");

    private static final Motivo MOTIVO_TETO_ALIMENTACAO =
            new Motivo(MotivoCodigo.TETO_DIARIO_APLICADO, RegraNegocio.RN_011, null);
    private static final Motivo MOTIVO_TETO_TRANSPORTE =
            new Motivo(MotivoCodigo.TETO_DIARIO_APLICADO, RegraNegocio.RN_012, null);
    private static final Motivo MOTIVO_ESGOTADO =
            new Motivo(MotivoCodigo.TETO_DIARIO_ESGOTADO, RegraNegocio.RN_015, null);

    private static final AgregadorTetoDiario PADRAO =
            new AgregadorTetoDiario(PoliticaReembolso.padrao());

    private final PoliticaReembolso politica;

    private AgregadorTetoDiario(PoliticaReembolso politica) {
        this.politica = Objects.requireNonNull(politica);
    }

    public static List<ResultadoTeto> aplicar(List<ItemAvaliado> itens) {
        return PADRAO.aplicarInterno(itens);
    }

    private List<ResultadoTeto> aplicarInterno(List<ItemAvaliado> itens) {
        List<ItemAvaliado> aplicaveis = new ArrayList<>();
        for (ItemAvaliado item : itens) {
            if (item.elegivel() && CATEGORIAS_TETO_DIARIO.contains(item.itemNormalizado().categoriaNormalizada())) {
                aplicaveis.add(item);
            }
        }

        List<ItemAvaliado> ordenadosPorIndice = new ArrayList<>(aplicaveis);
        ordenadosPorIndice.sort(Comparator.comparingInt(i -> i.itemNormalizado().item().getIndiceEntrada()));

        Map<ChaveTetoDiario, BigDecimal> saldos = new HashMap<>();
        Map<ItemAvaliado, ResultadoTeto> resultadosPorItem = new IdentityHashMap<>();

        for (ItemAvaliado item : ordenadosPorIndice) {
            String categoria = item.itemNormalizado().categoriaNormalizada();
            ChaveTetoDiario chave = chaveDe(item);
            BigDecimal saldo = saldos.computeIfAbsent(chave, k -> limiteInicial(categoria));

            if (saldo.compareTo(BigDecimal.ZERO) == 0) {
                resultadosPorItem.put(item,
                        new ResultadoTeto(item, ZERO_ESCALA_2, Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, List.of(MOTIVO_ESGOTADO)));
                continue;
            }

            Motivo motivoAplicado = "alimentacao".equals(categoria) ? MOTIVO_TETO_ALIMENTACAO : MOTIVO_TETO_TRANSPORTE;
            ResultadoTeto resultado = aplicarCorte(item, saldo, motivoAplicado);
            resultadosPorItem.put(item, resultado);

            saldos.put(chave, resultado.decisao() == Decisao.INTEGRALMENTE_REEMBOLSADO
                    ? saldo.subtract(resultado.valorReembolsavel())
                    : ZERO_ESCALA_2);
        }

        List<ResultadoTeto> resultado = new ArrayList<>(aplicaveis.size());
        for (ItemAvaliado item : aplicaveis) {
            resultado.add(resultadosPorItem.get(item));
        }
        return List.copyOf(resultado);
    }

    private BigDecimal limiteInicial(String categoriaNormalizada) {
        return "alimentacao".equals(categoriaNormalizada)
                ? politica.getLimiteDiarioAlimentacao()
                : politica.getLimiteDiarioTransporteUrbano();
    }

    private static ChaveTetoDiario chaveDe(ItemAvaliado item) {
        return new ChaveTetoDiario(
                item.itemNormalizado().item().getData(),
                item.itemNormalizado().categoriaNormalizada()
        );
    }

    /**
     * Corta um item no limite disponível informado: integral quando o valor
     * normalizado não ultrapassa o limite, parcial com o motivo fornecido
     * caso contrário. Não decide saldo esgotado — quem chama já garantiu
     * {@code limiteDisponivel} positivo. Não conhece categoria nem consulta
     * {@link PoliticaReembolso} diretamente, para ser reaproveitável pelo
     * teto individual de hospedagem (T-014).
     */
    static ResultadoTeto aplicarCorte(ItemAvaliado item, BigDecimal limiteDisponivel, Motivo motivoTetoAplicado) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(limiteDisponivel, "limiteDisponivel");
        Objects.requireNonNull(motivoTetoAplicado, "motivoTetoAplicado");

        if (limiteDisponivel.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "limiteDisponivel deve ser estritamente positivo");
        }

        BigDecimal valorNormalizado = item.itemNormalizado().valorNormalizado();
        if (valorNormalizado.compareTo(limiteDisponivel) <= 0) {
            return new ResultadoTeto(item, valorNormalizado, Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());
        }
        return new ResultadoTeto(item, limiteDisponivel, Decisao.PARCIALMENTE_REEMBOLSADO, List.of(motivoTetoAplicado));
    }

    private record ChaveTetoDiario(
            LocalDate data,
            String categoriaNormalizada
    ) {
    }

    /**
     * Resultado imutável de uma etapa de teto (diário ou, futuramente,
     * individual de hospedagem): o {@link ItemAvaliado} original preservado
     * por referência, o valor efetivamente reembolsável, a decisão final
     * dessa etapa e os motivos de limitação associados.
     */
    public record ResultadoTeto(
            ItemAvaliado itemAvaliado,
            BigDecimal valorReembolsavel,
            Decisao decisao,
            List<Motivo> motivos
    ) {
        public ResultadoTeto {
            Objects.requireNonNull(itemAvaliado, "itemAvaliado");
            Objects.requireNonNull(valorReembolsavel, "valorReembolsavel");
            Objects.requireNonNull(decisao, "decisao");
            motivos = List.copyOf(motivos);
        }
    }
}
