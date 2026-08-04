package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre a preservação de {@code valor_informado} (spec 4.3, RN-002): o
 * {@code JsonNode} bruto de {@code despesa.valor} é preservado exatamente
 * como recebido — número, texto, booleano, lista ou objeto —, comparado
 * estruturalmente, nunca convertido para texto.
 */
@DisplayName("valor_informado — preservação do JsonNode bruto (spec 4.3, RN-002)")
class ValorInformadoTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static JsonNode despesas(String json) {
        try {
            return MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonNode primeiroValorInformado(String json) {
        List<ItemValidado> resultado = ValidadorItem.validarLista(despesas(json));
        return resultado.get(0).getValorInformado();
    }

    private static String itemBase(String valorJson) {
        return """
                {
                  "despesas": [
                    {
                      "id": "d-001",
                      "data": "2026-07-03",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": %s,
                      "tem_nota_fiscal": true
                    }
                  ]
                }
                """.formatted(valorJson);
    }

    @Test
    @DisplayName("valor string é preservado como JsonNode textual, sem conversão")
    void valorString_preservado() throws Exception {
        JsonNode valorInformado = primeiroValorInformado(itemBase("\"72,50\""));

        assertTrue(valorInformado.isTextual());
        assertEquals(MAPPER.readTree("\"72,50\""), valorInformado);
    }

    @Test
    @DisplayName("valor booleano é preservado como JsonNode booleano")
    void valorBooleano_preservado() throws Exception {
        JsonNode valorInformado = primeiroValorInformado(itemBase("true"));

        assertTrue(valorInformado.isBoolean());
        assertEquals(MAPPER.readTree("true"), valorInformado);
    }

    @Test
    @DisplayName("valor como lista é preservado estruturalmente")
    void valorLista_preservado() throws Exception {
        JsonNode valorInformado = primeiroValorInformado(itemBase("[1, 2, 3]"));

        assertTrue(valorInformado.isArray());
        assertEquals(MAPPER.readTree("[1, 2, 3]"), valorInformado);
    }

    @Test
    @DisplayName("valor como objeto é preservado estruturalmente")
    void valorObjeto_preservado() throws Exception {
        JsonNode valorInformado = primeiroValorInformado(itemBase("{\"moeda\": \"BRL\", \"quantia\": 72.5}"));

        assertTrue(valorInformado.isObject());
        assertEquals(MAPPER.readTree("{\"moeda\": \"BRL\", \"quantia\": 72.5}"), valorInformado);
    }

    @Test
    @DisplayName("chave valor ausente produz valorInformado nulo")
    void valorAusente_valorInformadoNulo() {
        JsonNode lista = despesas("""
                {
                  "despesas": [
                    {
                      "id": "d-001",
                      "data": "2026-07-03",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "tem_nota_fiscal": true
                    }
                  ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);

        assertNull(resultado.get(0).getValorInformado());
    }

    @Test
    @DisplayName("chave valor com valor JSON nulo produz valorInformado nulo")
    void valorNulo_valorInformadoNulo() {
        JsonNode valorInformado = primeiroValorInformado(itemBase("null"));

        assertNull(valorInformado);
    }

    @Test
    @DisplayName("elemento de despesas que não é objeto produz valorInformado nulo")
    void elementoNaoObjeto_valorInformadoNulo() {
        JsonNode lista = despesas("""
                {
                  "despesas": [ 42 ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);

        assertNull(resultado.get(0).getValorInformado());
    }

    @Test
    @DisplayName("a lista retornada por validarLista é não modificável")
    void listaRetornada_naoModificavel() {
        JsonNode lista = despesas(itemBase("72.50"));

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(resultado.get(0)));
    }
}
