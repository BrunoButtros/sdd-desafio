package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.pipeline.CompositorSaida.ResultadoItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Passo 11 da ordem canônica (spec 8.1, RN-018): soma os {@code
 * valorReembolsavel} já apresentados na lista final produzida por
 * {@link CompositorSaida#compor}. Não reavalia elegibilidade, não consulta
 * decisão ou motivos, e não filtra por nenhum critério — a lista recebida
 * já é a fonte da verdade do total (CA-003).
 */
public final class SomadorTotal {

    private static final BigDecimal ZERO_ESCALA_2 =
            new BigDecimal("0.00");

    private SomadorTotal() {
    }

    public static BigDecimal somar(
            List<ResultadoItem> resultados
    ) {
        Objects.requireNonNull(resultados, "resultados");

        BigDecimal total = ZERO_ESCALA_2;

        for (ResultadoItem resultado : resultados) {
            Objects.requireNonNull(resultado, "resultado");
            total = total.add(resultado.valorReembolsavel());
        }

        return total;
    }
}
