package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.PoliticaExterna;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.modelo.TabelaCategoria;
import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Avalia as regras individuais de negócio (spec 8.1, passo 5) sobre um
 * {@link ItemNormalizado}, cobrindo RN-006 (valor não positivo), RN-007
 * (categoria fora da política), RN-009 (nota fiscal obrigatória, spec 8.2)
 * e, nas sobrecargas que recebem {@link Envelope}, RN-008 (elegibilidade
 * temporal). Cada regra desta classe só acrescenta motivos ao acumulador já
 * existente (plan §4, "Acumulador de motivos") — nunca remove os motivos
 * estruturais, os de {@code ID_DUPLICADO} ou os de regras anteriores já
 * produzidos pelas etapas anteriores do pipeline. As sobrecargas de um
 * argumento não avaliam RN-008, por não terem janela temporal disponível.
 * RN-010 em diante entram nas tasks seguintes, em outras classes.
 */
public final class AvaliadorRegrasIndividuais {

    private static final Motivo VALOR_NAO_POSITIVO =
            new Motivo(MotivoCodigo.VALOR_NAO_POSITIVO, RegraNegocio.RN_006, null);
    private static final Motivo CATEGORIA_FORA_POLITICA =
            new Motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null);
    private static final Motivo CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO =
            new Motivo(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, RegraNegocio.RN_019, null);
    private static final Motivo FORA_COMPETENCIA =
            new Motivo(MotivoCodigo.FORA_COMPETENCIA, RegraNegocio.RN_008, null);
    private static final Motivo NOTA_FISCAL_AUSENTE =
            new Motivo(MotivoCodigo.NOTA_FISCAL_AUSENTE, RegraNegocio.RN_009, null);
    private static final BigDecimal ZERO_ESCALA_2 = new BigDecimal("0.00");

    public static ItemAvaliado avaliar(ItemNormalizado item, Envelope envelope,
            TabelaPoliticaResolvida tabela, PoliticaExterna politica) {
        return avaliarInterno(item, envelope, tabela, politica);
    }

    public static List<ItemAvaliado> avaliarLista(List<ItemNormalizado> itens, Envelope envelope,
            TabelaPoliticaResolvida tabela, PoliticaExterna politica) {
        return avaliarListaInterno(itens, envelope, tabela, politica);
    }

    private static ItemAvaliado avaliarInterno(ItemNormalizado item, Envelope envelope,
            TabelaPoliticaResolvida tabela, PoliticaExterna politica) {
        List<Motivo> motivos = avaliarRn006(item);

        avaliarCategoriaPorTabela(item, tabela, motivos);

        LocalDate data = item.item().getData();
        boolean foraCompetencia = data != null
                && (data.isBefore(envelope.getPeriodoInicio()) || data.isAfter(envelope.getPeriodoFim()));
        if (foraCompetencia && !motivos.contains(FORA_COMPETENCIA)) {
            motivos.add(FORA_COMPETENCIA);
        }

        avaliarRn009(item, motivos, politica.getNotaFiscalObrigatoriaAcimaDe());

        return finalizar(item, motivos);
    }

    private static List<ItemAvaliado> avaliarListaInterno(List<ItemNormalizado> itens, Envelope envelope,
            TabelaPoliticaResolvida tabela, PoliticaExterna politica) {
        List<ItemAvaliado> resultado = new ArrayList<>(itens.size());
        for (ItemNormalizado item : itens) {
            resultado.add(avaliarInterno(item, envelope, tabela, politica));
        }
        return List.copyOf(resultado);
    }

    private static List<Motivo> avaliarRn006(ItemNormalizado item) {
        List<Motivo> motivos = new ArrayList<>(item.item().getMotivos());

        boolean valorNaoPositivo = item.valorNormalizado() != null
                && item.valorNormalizado().compareTo(BigDecimal.ZERO) <= 0;
        if (valorNaoPositivo && !motivos.contains(VALOR_NAO_POSITIVO)) {
            motivos.add(VALOR_NAO_POSITIVO);
        }

        return motivos;
    }

    private static void avaliarCategoriaPorTabela(ItemNormalizado item, TabelaPoliticaResolvida tabela,
            List<Motivo> motivos) {
        String categoriaNormalizada = item.categoriaNormalizada();
        if (categoriaNormalizada == null) {
            return;
        }

        TabelaCategoria configuracao = tabela.getCategorias().get(categoriaNormalizada);
        if (configuracao == null) {
            Motivo motivo = tabela.getOrigem() == TabelaPoliticaResolvida.Origem.PADRAO
                    ? CATEGORIA_FORA_POLITICA
                    : CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO;
            if (!motivos.contains(motivo)) {
                motivos.add(motivo);
            }
            return;
        }

        if (configuracao.limite().compareTo(BigDecimal.ZERO) == 0
                && !motivos.contains(CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO)) {
            motivos.add(CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO);
        }
    }

    private static void avaliarRn009(ItemNormalizado item, List<Motivo> motivos, BigDecimal gatilho) {
        BigDecimal valorNormalizado = item.valorNormalizado();
        Boolean temNotaFiscal = item.item().getTemNotaFiscal();

        boolean notaFiscalAusente = valorNormalizado != null
                && temNotaFiscal != null
                && valorNormalizado.compareTo(BigDecimal.ZERO) > 0
                && valorNormalizado.compareTo(gatilho) > 0
                && !temNotaFiscal;

        if (notaFiscalAusente && !motivos.contains(NOTA_FISCAL_AUSENTE)) {
            motivos.add(NOTA_FISCAL_AUSENTE);
        }
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
