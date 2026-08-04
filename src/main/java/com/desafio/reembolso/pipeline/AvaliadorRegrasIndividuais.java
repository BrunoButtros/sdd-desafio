package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Avalia as regras individuais de negócio (spec 8.1, passo 5) sobre um
 * {@link ItemNormalizado}, cobrindo RN-006 (valor não positivo), RN-007
 * (categoria fora da política) e, nas sobrecargas que recebem
 * {@link Envelope}, RN-008 (elegibilidade temporal). Cada regra desta
 * classe só acrescenta motivos ao acumulador já existente (plan §4,
 * "Acumulador de motivos") — nunca remove os motivos estruturais, os de
 * {@code ID_DUPLICADO} ou os de regras anteriores já produzidos pelas
 * etapas anteriores do pipeline. As sobrecargas de um argumento não avaliam
 * RN-008, por não terem janela temporal disponível. RN-009 em diante entram
 * nas tasks seguintes, na mesma classe.
 */
public final class AvaliadorRegrasIndividuais {

    private static final Motivo VALOR_NAO_POSITIVO =
            new Motivo(MotivoCodigo.VALOR_NAO_POSITIVO, RegraNegocio.RN_006, null);
    private static final Motivo CATEGORIA_FORA_POLITICA =
            new Motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null);
    private static final Motivo FORA_COMPETENCIA =
            new Motivo(MotivoCodigo.FORA_COMPETENCIA, RegraNegocio.RN_008, null);
    private static final Set<String> CATEGORIAS_REEMBOLSAVEIS = Set.of(
            "alimentacao",
            "transporte_urbano",
            "hospedagem"
    );
    private static final BigDecimal ZERO_ESCALA_2 = new BigDecimal("0.00");

    private AvaliadorRegrasIndividuais() {
    }

    public static ItemAvaliado avaliar(ItemNormalizado item) {
        List<Motivo> motivos = avaliarRn006ERn007(item);
        return finalizar(item, motivos);
    }

    public static List<ItemAvaliado> avaliarLista(List<ItemNormalizado> itens) {
        List<ItemAvaliado> resultado = new ArrayList<>(itens.size());
        for (ItemNormalizado item : itens) {
            resultado.add(avaliar(item));
        }
        return List.copyOf(resultado);
    }

    public static ItemAvaliado avaliar(ItemNormalizado item, Envelope envelope) {
        List<Motivo> motivos = avaliarRn006ERn007(item);

        LocalDate data = item.item().getData();
        boolean foraCompetencia = data != null
                && (data.isBefore(envelope.getPeriodoInicio()) || data.isAfter(envelope.getPeriodoFim()));
        if (foraCompetencia && !motivos.contains(FORA_COMPETENCIA)) {
            motivos.add(FORA_COMPETENCIA);
        }

        return finalizar(item, motivos);
    }

    public static List<ItemAvaliado> avaliarLista(List<ItemNormalizado> itens, Envelope envelope) {
        List<ItemAvaliado> resultado = new ArrayList<>(itens.size());
        for (ItemNormalizado item : itens) {
            resultado.add(avaliar(item, envelope));
        }
        return List.copyOf(resultado);
    }

    private static List<Motivo> avaliarRn006ERn007(ItemNormalizado item) {
        List<Motivo> motivos = new ArrayList<>(item.item().getMotivos());

        boolean valorNaoPositivo = item.valorNormalizado() != null
                && item.valorNormalizado().compareTo(BigDecimal.ZERO) <= 0;
        if (valorNaoPositivo && !motivos.contains(VALOR_NAO_POSITIVO)) {
            motivos.add(VALOR_NAO_POSITIVO);
        }

        boolean categoriaForaPolitica = item.categoriaNormalizada() != null
                && !CATEGORIAS_REEMBOLSAVEIS.contains(item.categoriaNormalizada());
        if (categoriaForaPolitica && !motivos.contains(CATEGORIA_FORA_POLITICA)) {
            motivos.add(CATEGORIA_FORA_POLITICA);
        }

        return motivos;
    }

    private static ItemAvaliado finalizar(ItemNormalizado item, List<Motivo> motivos) {
        List<Motivo> motivosFinal = List.copyOf(motivos);
        boolean elegivel = motivosFinal.isEmpty();
        BigDecimal valorReembolsavel = elegivel ? null : ZERO_ESCALA_2;

        return new ItemAvaliado(item, motivosFinal, elegivel, valorReembolsavel);
    }

    /**
     * Estado do item após as validações individuais já avaliadas até aqui
     * (plan §4, "Resultado por item" — ainda parcial: decisão final e
     * composição de saída pertencem a T-016 em diante). {@code motivos} é
     * cópia defensiva e não modificável dos motivos já acumulados;
     * {@code valorReembolsavel} é {@code 0.00} quando o item já é
     * inelegível, e permanece nulo enquanto o item segue elegível, pois o
     * valor efetivo só é decidido nas tasks de teto.
     */
    public record ItemAvaliado(
            ItemNormalizado itemNormalizado,
            List<Motivo> motivos,
            boolean elegivel,
            BigDecimal valorReembolsavel
    ) {
        public ItemAvaliado {
            motivos = List.copyOf(motivos);
        }
    }
}
