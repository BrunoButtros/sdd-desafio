package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.PoliticaExterna;
import com.desafio.reembolso.modelo.TabelaCategoria;
import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;

import java.util.Map;

/**
 * Resolve a tabela de política aplicável a um item, a partir do centro de
 * custo do colaborador (spec RN-019, plan §6). Consulta exclusivamente
 * {@code politica.getCentrosCusto().get(centroCusto)} — comparação textual
 * exata, sem trim, sem normalização de caixa ou acento (DT-016) — e nunca
 * mistura a tabela do centro com {@code padrao}. Não avalia categoria, não
 * aplica teto e não decide elegibilidade: apenas escolhe a tabela.
 */
public final class ResolutorPoliticaCentroCusto {

    private ResolutorPoliticaCentroCusto() {
    }

    public static TabelaPoliticaResolvida resolver(String centroCusto, PoliticaExterna politica) {
        if (centroCusto == null) {
            return resolverPadrao(politica);
        }

        Map<String, TabelaCategoria> tabelaCentro = politica.getCentrosCusto().get(centroCusto);
        if (tabelaCentro == null) {
            return resolverPadrao(politica);
        }

        return new TabelaPoliticaResolvida(
                tabelaCentro,
                TabelaPoliticaResolvida.Origem.CENTRO_CUSTO,
                centroCusto
        );
    }

    private static TabelaPoliticaResolvida resolverPadrao(PoliticaExterna politica) {
        return new TabelaPoliticaResolvida(
                politica.getPadrao(),
                TabelaPoliticaResolvida.Origem.PADRAO,
                null
        );
    }
}
