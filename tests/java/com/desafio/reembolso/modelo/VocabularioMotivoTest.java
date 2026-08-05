package com.desafio.reembolso.modelo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DT-008 / spec 4.4-4.5: cada enum de vocabulário fechado deve serializar,
 * via Jackson, exatamente para o texto canônico definido pela spec.
 */
class VocabularioMotivoTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private String serializar(Object valor) throws Exception {
        return mapper.writeValueAsString(valor);
    }

    @ParameterizedTest(name = "MotivoCodigo.{0} serializa para \"{1}\"")
    @CsvSource({
            "ITEM_TIPO_INVALIDO, ITEM_TIPO_INVALIDO",
            "CAMPO_AUSENTE, CAMPO_AUSENTE",
            "CAMPO_TIPO_INVALIDO, CAMPO_TIPO_INVALIDO",
            "CAMPO_FORMATO_INVALIDO, CAMPO_FORMATO_INVALIDO",
            "ID_DUPLICADO, ID_DUPLICADO",
            "VALOR_NAO_POSITIVO, VALOR_NAO_POSITIVO",
            "CATEGORIA_FORA_POLITICA, CATEGORIA_FORA_POLITICA",
            "FORA_COMPETENCIA, FORA_COMPETENCIA",
            "NOTA_FISCAL_AUSENTE, NOTA_FISCAL_AUSENTE",
            "DUPLICIDADE, DUPLICIDADE",
            "TETO_DIARIO_APLICADO, TETO_DIARIO_APLICADO",
            "TETO_DIARIO_ESGOTADO, TETO_DIARIO_ESGOTADO",
            "TETO_HOSPEDAGEM_APLICADO, TETO_HOSPEDAGEM_APLICADO",
    })
    @DisplayName("Spec 4.5 / DT-008 — MotivoCodigo")
    void motivoCodigoSerializaTextoCanonico(MotivoCodigo valor, String textoCanonico) throws Exception {
        assertEquals("\"" + textoCanonico + "\"", serializar(valor));
    }

    @Test
    @DisplayName("MotivoCodigo cobre exatamente os treze valores de 4.5, nenhum a mais nem a menos")
    void motivoCodigoTemTrezeValores() {
        assertEquals(13, MotivoCodigo.values().length);
    }

    @ParameterizedTest(name = "RegraNegocio.RN_{0} serializa para \"RN-{0}\"")
    @CsvSource({
            "001", "002", "003", "004", "005", "006", "007", "008", "009",
            "010", "011", "012", "013", "014", "015", "016", "017", "018",
    })
    @DisplayName("Plan §4/DT-008 — RegraNegocio serializa como RN-NNN")
    void regraNegocioSerializaComoRnNnn(String numero) throws Exception {
        RegraNegocio valor = RegraNegocio.valueOf("RN_" + numero);
        assertEquals("\"RN-" + numero + "\"", serializar(valor));
    }

    @Test
    @DisplayName("RegraNegocio cobre exatamente RN-001 a RN-018")
    void regraNegocioTemDezoitoValores() {
        assertEquals(18, RegraNegocio.values().length);
    }

    @ParameterizedTest(name = "CampoCanonico.{0} serializa para \"despesa.{1}\"")
    @CsvSource({
            "ID, id",
            "DATA, data",
            "CATEGORIA, categoria",
            "DESCRICAO, descricao",
            "FORNECEDOR, fornecedor",
            "VALOR, valor",
            "MOEDA, moeda",
            "TEM_NOTA_FISCAL, tem_nota_fiscal",
    })
    @DisplayName("Spec 4.2 — CampoCanonico serializa como despesa.<campo>")
    void campoCanonicoSerializaComoDespesaCampo(CampoCanonico valor, String sufixo) throws Exception {
        assertEquals("\"despesa." + sufixo + "\"", serializar(valor));
    }

    @Test
    @DisplayName("CampoCanonico cobre exatamente os oito campos de 4.2 (T-022: acrescenta despesa.moeda)")
    void campoCanonicoTemOitoValores() {
        assertEquals(8, CampoCanonico.values().length);
    }

    @ParameterizedTest(name = "Decisao.{0} serializa para \"{1}\"")
    @CsvSource({
            "INTEGRALMENTE_REEMBOLSADO, INTEGRALMENTE_REEMBOLSADO",
            "PARCIALMENTE_REEMBOLSADO, PARCIALMENTE_REEMBOLSADO",
            "NAO_REEMBOLSADO_TETO_ESGOTADO, NAO_REEMBOLSADO_TETO_ESGOTADO",
            "RECUSADO, RECUSADO",
    })
    @DisplayName("Spec 4.4 — Decisao serializa para o texto canônico")
    void decisaoSerializaTextoCanonico(Decisao valor, String textoCanonico) throws Exception {
        assertEquals("\"" + textoCanonico + "\"", serializar(valor));
    }

    @Test
    @DisplayName("Decisao cobre exatamente os quatro valores de 4.4")
    void decisaoTemQuatroValores() {
        assertEquals(4, Decisao.values().length);
    }

    @Test
    @DisplayName("Nenhum MotivoCodigo colide em texto canônico com outro")
    void motivoCodigoSemColisaoDeTexto() {
        Set<String> textos = EnumSet.allOf(MotivoCodigo.class).stream()
                .map(MotivoCodigo::textoCanonico)
                .collect(Collectors.toSet());
        assertEquals(MotivoCodigo.values().length, textos.size());
    }
}
