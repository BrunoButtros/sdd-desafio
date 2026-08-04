package com.desafio.reembolso.modelo;

import java.math.BigDecimal;

/**
 * Estrutura imutável com os quatro valores fixos da política de reembolso
 * (spec §5 do plan, DT-007): os três limites de categoria — ainda não
 * consumidos por nenhuma regra nesta task — e o gatilho de nota fiscal de
 * RN-009. Sem mecanismo genérico de regras, sem DSL, sem configuração
 * externa: mudar um valor é editar esta classe.
 */
public final class PoliticaReembolso {

    private static final PoliticaReembolso PADRAO = new PoliticaReembolso(
            new BigDecimal("60.00"),
            new BigDecimal("80.00"),
            new BigDecimal("250.00"),
            new BigDecimal("100.00")
    );

    private final BigDecimal limiteDiarioAlimentacao;
    private final BigDecimal limiteDiarioTransporteUrbano;
    private final BigDecimal limiteIndividualHospedagem;
    private final BigDecimal gatilhoNotaFiscal;

    private PoliticaReembolso(BigDecimal limiteDiarioAlimentacao,
                               BigDecimal limiteDiarioTransporteUrbano,
                               BigDecimal limiteIndividualHospedagem,
                               BigDecimal gatilhoNotaFiscal) {
        this.limiteDiarioAlimentacao = limiteDiarioAlimentacao;
        this.limiteDiarioTransporteUrbano = limiteDiarioTransporteUrbano;
        this.limiteIndividualHospedagem = limiteIndividualHospedagem;
        this.gatilhoNotaFiscal = gatilhoNotaFiscal;
    }

    public static PoliticaReembolso padrao() {
        return PADRAO;
    }

    public BigDecimal getLimiteDiarioAlimentacao() {
        return limiteDiarioAlimentacao;
    }

    public BigDecimal getLimiteDiarioTransporteUrbano() {
        return limiteDiarioTransporteUrbano;
    }

    public BigDecimal getLimiteIndividualHospedagem() {
        return limiteIndividualHospedagem;
    }

    public BigDecimal getGatilhoNotaFiscal() {
        return gatilhoNotaFiscal;
    }
}
