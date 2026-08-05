package com.desafio.reembolso.modelo;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Vocabulário fechado de códigos de motivo (spec 4.5).
 */
public enum MotivoCodigo {
    ITEM_TIPO_INVALIDO,
    CAMPO_AUSENTE,
    CAMPO_TIPO_INVALIDO,
    CAMPO_FORMATO_INVALIDO,
    ID_DUPLICADO,
    MOEDA_SEM_COTACAO,
    VALOR_NAO_POSITIVO,
    CATEGORIA_FORA_POLITICA,
    CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO,
    FORA_COMPETENCIA,
    NOTA_FISCAL_AUSENTE,
    DUPLICIDADE,
    TETO_DIARIO_APLICADO,
    TETO_DIARIO_ESGOTADO,
    TETO_HOSPEDAGEM_APLICADO,
    TETO_INDIVIDUAL_APLICADO;

    @JsonValue
    public String textoCanonico() {
        return name();
    }
}
