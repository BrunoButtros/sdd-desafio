package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre {@code validarMoeda} em {@link ValidadorItem} — sétimo campo do
 * contrato do item (spec 4.2, RN-002, plan §8): ausência da chave resolve
 * silenciosamente para {@code "BRL"}; a presença da chave, mesmo com valor
 * {@code null}, segue o mesmo contrato estrutural fechado dos demais campos
 * (T-036). Não cobre resolução ou conversão cambial — isso pertence a
 * T-037/T-038.
 */
@DisplayName("Campo despesa.moeda — validarMoeda (spec 4.2, RN-002, CA-048)")
class CampoMoedaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static JsonNode despesas(String json) {
        try {
            return MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String itemBase(String blocoMoeda) {
        return """
                {
                  "despesas": [
                    {
                      "id": "d-001",
                      "data": "2026-07-03",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": 72.50%s,
                      "tem_nota_fiscal": true
                    }
                  ]
                }
                """.formatted(blocoMoeda);
    }

    private static ItemValidado primeiroItem(String json) {
        List<ItemValidado> resultado = ValidadorItem.validarLista(despesas(json));
        return resultado.get(0);
    }

    @Test
    @DisplayName("1. chave moeda ausente do objeto resolve para BRL, sem motivo de moeda")
    void chaveMoedaAusente_resolveParaBrlSemMotivo() {
        ItemValidado item = primeiroItem(itemBase(""));

        assertEquals("BRL", item.getMoeda());
        for (Motivo motivo : item.getMotivos()) {
            assertFalse(campoEhMoeda(motivo), "nenhum motivo deve referenciar despesa.moeda");
        }
    }

    @Test
    @DisplayName("2. moeda explicitamente null produz CAMPO_AUSENTE em despesa.moeda")
    void moedaExplicitamenteNull_campoAusente() {
        ItemValidado item = primeiroItem(itemBase(", \"moeda\": null"));

        assertNull(item.getMoeda());
        assertTrue(item.getMotivos().contains(
                new Motivo(MotivoCodigo.CAMPO_AUSENTE, RegraNegocio.RN_002, CampoCanonico.MOEDA)));
    }

    @Test
    @DisplayName("3. moeda com tipo não textual produz CAMPO_TIPO_INVALIDO em despesa.moeda")
    void moedaTipoNaoTextual_campoTipoInvalido() {
        ItemValidado item = primeiroItem(itemBase(", \"moeda\": 123"));

        assertNull(item.getMoeda());
        assertTrue(item.getMotivos().contains(
                new Motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.MOEDA)));
    }

    @ParameterizedTest(name = "moeda \"{0}\" produz CAMPO_FORMATO_INVALIDO")
    @ValueSource(strings = {"usd", "USD ", "US", "USDX", "12A"})
    @DisplayName("4. moeda com formato inválido produz CAMPO_FORMATO_INVALIDO em despesa.moeda")
    void moedaFormatoInvalido_campoFormatoInvalido(String textoInvalido) {
        ItemValidado item = primeiroItem(itemBase(", \"moeda\": \"%s\"".formatted(textoInvalido)));

        assertNull(item.getMoeda());
        assertTrue(item.getMotivos().contains(
                new Motivo(MotivoCodigo.CAMPO_FORMATO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.MOEDA)));
    }

    @Test
    @DisplayName("5. moeda estrangeira válida é preservada exatamente, sem motivo de moeda")
    void moedaEstrangeiraValida_preservadaExatamente() {
        ItemValidado item = primeiroItem(itemBase(", \"moeda\": \"EUR\""));

        assertEquals("EUR", item.getMoeda());
        for (Motivo motivo : item.getMotivos()) {
            assertFalse(campoEhMoeda(motivo), "nenhum motivo deve referenciar despesa.moeda");
        }
    }

    @Test
    @DisplayName("6. BRL assumido por ausência da chave mantém os três campos derivados nulos")
    void brlAssumidoPorAusencia_derivadosNulos() {
        ItemValidado item = primeiroItem(itemBase(""));

        assertEquals("BRL", item.getMoeda());
        assertNull(item.getTaxaCambioAplicada());
        assertNull(item.getDataCotacaoUtilizada());
        assertNull(item.getValorConvertidoBruto());
    }

    @Test
    @DisplayName("7. moeda estrangeira válida também mantém os três campos derivados nulos nesta etapa")
    void moedaEstrangeiraValida_derivadosNulos() {
        ItemValidado item = primeiroItem(itemBase(", \"moeda\": \"EUR\""));

        assertEquals("EUR", item.getMoeda());
        assertNull(item.getTaxaCambioAplicada());
        assertNull(item.getDataCotacaoUtilizada());
        assertNull(item.getValorConvertidoBruto());
    }

    @Test
    @DisplayName("8. elemento da lista que não é objeto: moeda e derivados nulos, motivo ITEM_TIPO_INVALIDO preservado")
    void elementoNaoObjeto_moedaEDerivadosNulos() {
        JsonNode lista = despesas("""
                {
                  "despesas": [ "despesa-invalida" ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);
        ItemValidado item = resultado.get(0);

        assertNull(item.getMoeda());
        assertNull(item.getTaxaCambioAplicada());
        assertNull(item.getDataCotacaoUtilizada());
        assertNull(item.getValorConvertidoBruto());

        assertEquals(1, item.getMotivos().size());
        Motivo motivo = item.getMotivos().get(0);
        assertEquals(MotivoCodigo.ITEM_TIPO_INVALIDO, motivo.codigo());
        assertEquals(RegraNegocio.RN_002, motivo.regra());
        assertNull(motivo.campo());
    }

    @Test
    @DisplayName("9. valor, moeda e tem_nota_fiscal simultaneamente inválidos: motivo de moeda entre valor e tem_nota_fiscal")
    void ordemCanonica_motivoDeMoedaEntreValorETemNotaFiscal() {
        JsonNode lista = despesas("""
                {
                  "despesas": [
                    {
                      "id": "d-001",
                      "data": "2026-07-03",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": "72,50",
                      "moeda": "usd",
                      "tem_nota_fiscal": "sim"
                    }
                  ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);
        List<Motivo> motivos = resultado.get(0).getMotivos();

        assertEquals(3, motivos.size(), "devem existir exatamente três motivos");

        assertEquals(new Motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.VALOR),
                motivos.get(0));
        assertEquals(new Motivo(MotivoCodigo.CAMPO_FORMATO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.MOEDA),
                motivos.get(1));
        assertEquals(new Motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.TEM_NOTA_FISCAL),
                motivos.get(2));
    }

    private static boolean campoEhMoeda(Motivo motivo) {
        return motivo.campo() == CampoCanonico.MOEDA;
    }
}
