package com.desafio.reembolso.modelo;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Vocabulário fechado de regras de negócio (RN-001 a RN-022), cada valor
 * serializando para o texto canônico {@code "RN-NNN"} (plan §4, DT-008).
 */
public enum RegraNegocio {
    RN_001,
    RN_002,
    RN_003,
    RN_004,
    RN_005,
    RN_006,
    RN_007,
    RN_008,
    RN_009,
    RN_010,
    RN_011,
    RN_012,
    RN_013,
    RN_014,
    RN_015,
    RN_016,
    RN_017,
    RN_018,
    RN_019,
    RN_020,
    RN_021,
    RN_022;

    @JsonValue
    public String textoCanonico() {
        return name().replace('_', '-');
    }
}
