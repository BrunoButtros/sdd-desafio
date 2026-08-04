package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Cobre RN-002 / CA-021 / CA-022 / CA-023 (spec 4.2): classificação fechada
 * de erro estrutural por campo, motivo único {@code ITEM_TIPO_INVALIDO}
 * para elemento que não é objeto, ordem canônica de contrato, ausência de
 * short-circuit e preenchimento dos sete campos tipados de
 * {@code ItemValidado} (plan §4, "Campos estruturalmente validados").
 */
@DisplayName("Contrato do item — RN-002 / CA-021 / CA-022 / CA-023")
class ContratoDoItemTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static JsonNode despesas(String json) {
        try {
            return MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Despesa estruturalmente válida em todos os sete campos. */
    private static ObjectNode itemValidoBase() {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("id", "d-001");
        node.put("data", "2026-07-03");
        node.put("categoria", "alimentacao");
        node.put("descricao", "Almoço");
        node.put("fornecedor", "Restaurante X");
        node.put("valor", new BigDecimal("33.333"));
        node.put("tem_nota_fiscal", true);
        return node;
    }

    private static ItemValidado validarUnico(ObjectNode despesa) {
        ArrayNode lista = MAPPER.createArrayNode();
        lista.add(despesa);
        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);
        return resultado.get(0);
    }

    private static Stream<Arguments> camposCanonicos() {
        return Stream.of(
                Arguments.of("id", CampoCanonico.ID),
                Arguments.of("data", CampoCanonico.DATA),
                Arguments.of("categoria", CampoCanonico.CATEGORIA),
                Arguments.of("descricao", CampoCanonico.DESCRICAO),
                Arguments.of("fornecedor", CampoCanonico.FORNECEDOR),
                Arguments.of("valor", CampoCanonico.VALOR),
                Arguments.of("tem_nota_fiscal", CampoCanonico.TEM_NOTA_FISCAL)
        );
    }

    private static Stream<Arguments> camposComTipoInvalido() {
        return Stream.of(
                Arguments.of("id", CampoCanonico.ID, IntNode.valueOf(123)),
                Arguments.of("data", CampoCanonico.DATA, IntNode.valueOf(20260703)),
                Arguments.of("categoria", CampoCanonico.CATEGORIA, IntNode.valueOf(42)),
                Arguments.of("descricao", CampoCanonico.DESCRICAO, IntNode.valueOf(1)),
                Arguments.of("fornecedor", CampoCanonico.FORNECEDOR, IntNode.valueOf(1)),
                Arguments.of("valor", CampoCanonico.VALOR, TextNode.valueOf("72,50")),
                Arguments.of("tem_nota_fiscal", CampoCanonico.TEM_NOTA_FISCAL, TextNode.valueOf("sim"))
        );
    }

    @Test
    @DisplayName("Item integralmente válido expõe os sete campos tipados, sem motivo estrutural — descrição e fornecedor vazios são aceitos")
    void itemIntegralmenteValido_todosOsCamposTipadosDisponiveis() {
        ObjectNode despesa = itemValidoBase();
        despesa.put("descricao", "");
        despesa.put("fornecedor", "");

        ItemValidado item = validarUnico(despesa);

        assertEquals(0, item.getMotivos().size(), "item integralmente válido não produz motivo estrutural");
        assertEquals("d-001", item.getId());
        assertEquals(LocalDate.of(2026, 7, 3), item.getData());
        assertEquals("alimentacao", item.getCategoria());
        assertEquals("", item.getDescricao(), "descrição vazia é aceita");
        assertEquals("", item.getFornecedor(), "fornecedor vazio é aceito");
        assertEquals(0, new BigDecimal("33.333").compareTo(item.getValor()),
                "valor deve ser preservado com precisão decimal exata, sem passar por double");
        assertEquals(Boolean.TRUE, item.getTemNotaFiscal());
        assertEquals(0, new BigDecimal("33.333").compareTo(item.getValorInformado().decimalValue()),
                "valorInformado deve preservar o mesmo número exato");
    }

    @ParameterizedTest(name = "{1} ausente produz CAMPO_AUSENTE")
    @MethodSource("camposCanonicos")
    @DisplayName("RN-002 — chave ausente produz CAMPO_AUSENTE com o CampoCanonico correto")
    void chaveAusente_produzCampoAusente(String chave, CampoCanonico campo) {
        ObjectNode despesa = itemValidoBase();
        despesa.remove(chave);

        ItemValidado item = validarUnico(despesa);

        assertEquals(1, item.getMotivos().size());
        assertEquals(new Motivo(MotivoCodigo.CAMPO_AUSENTE, RegraNegocio.RN_002, campo), item.getMotivos().get(0));
    }

    @ParameterizedTest(name = "{1} com valor JSON nulo produz CAMPO_AUSENTE")
    @MethodSource("camposCanonicos")
    @DisplayName("RN-002 — valor JSON nulo produz CAMPO_AUSENTE com o CampoCanonico correto")
    void valorJsonNulo_produzCampoAusente(String chave, CampoCanonico campo) {
        ObjectNode despesa = itemValidoBase();
        despesa.putNull(chave);

        ItemValidado item = validarUnico(despesa);

        assertEquals(1, item.getMotivos().size());
        assertEquals(new Motivo(MotivoCodigo.CAMPO_AUSENTE, RegraNegocio.RN_002, campo), item.getMotivos().get(0));
    }

    @ParameterizedTest(name = "{1} com tipo incorreto produz CAMPO_TIPO_INVALIDO")
    @MethodSource("camposComTipoInvalido")
    @DisplayName("RN-002 — tipo JSON incorreto produz CAMPO_TIPO_INVALIDO com o CampoCanonico correto")
    void tipoIncorreto_produzCampoTipoInvalido(String chave, CampoCanonico campo, JsonNode valorErrado) {
        ObjectNode despesa = itemValidoBase();
        despesa.set(chave, valorErrado);

        ItemValidado item = validarUnico(despesa);

        assertEquals(1, item.getMotivos().size());
        assertEquals(new Motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, campo), item.getMotivos().get(0));
    }

    @Test
    @DisplayName("RN-002 — categoria vazia produz CAMPO_FORMATO_INVALIDO em despesa.categoria")
    void categoriaVazia_campoFormatoInvalido() {
        ObjectNode despesa = itemValidoBase();
        despesa.put("categoria", "");

        ItemValidado item = validarUnico(despesa);

        assertEquals(1, item.getMotivos().size());
        assertEquals(new Motivo(MotivoCodigo.CAMPO_FORMATO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.CATEGORIA),
                item.getMotivos().get(0));
        assertNull(item.getCategoria(), "categoria inválida não deve ser preservada");
    }

    @Test
    @DisplayName("RN-002 — data fora do padrão AAAA-MM-DD produz CAMPO_FORMATO_INVALIDO")
    void dataForaDoPadrao_campoFormatoInvalido() {
        ObjectNode despesa = itemValidoBase();
        despesa.put("data", "31/07/2026");

        ItemValidado item = validarUnico(despesa);

        assertEquals(1, item.getMotivos().size());
        assertEquals(new Motivo(MotivoCodigo.CAMPO_FORMATO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.DATA),
                item.getMotivos().get(0));
        assertNull(item.getData(), "data inválida não deve ser preservada");
    }

    @Test
    @DisplayName("RN-002 — data com formato correto mas calendário impossível (2026-02-30) produz CAMPO_FORMATO_INVALIDO")
    void dataImpossivelNoCalendario_campoFormatoInvalido() {
        ObjectNode despesa = itemValidoBase();
        despesa.put("data", "2026-02-30");

        ItemValidado item = validarUnico(despesa);

        assertEquals(1, item.getMotivos().size());
        assertEquals(new Motivo(MotivoCodigo.CAMPO_FORMATO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.DATA),
                item.getMotivos().get(0));
        assertNull(item.getData(), "data inválida não deve ser preservada");
    }

    @Test
    @DisplayName("RN-002 / CA-021 — data \"31/07/2026\" e valor \"72,50\" produzem dois motivos, nesta ordem")
    void dataFormatoInvalidoEValorTipoInvalido_doisMotivosNaOrdemCanonica() {
        JsonNode lista = despesas("""
                {
                  "despesas": [
                    {
                      "id": "d-001",
                      "data": "31/07/2026",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": "72,50",
                      "tem_nota_fiscal": true
                    }
                  ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);

        assertEquals(1, resultado.size());
        ItemValidado item = resultado.get(0);

        assertEquals(2, item.getMotivos().size(), "devem existir exatamente dois motivos");

        Motivo primeiro = item.getMotivos().get(0);
        assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, primeiro.codigo());
        assertEquals(CampoCanonico.DATA, primeiro.campo());
        assertEquals(RegraNegocio.RN_002, primeiro.regra());

        Motivo segundo = item.getMotivos().get(1);
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, segundo.codigo());
        assertEquals(CampoCanonico.VALOR, segundo.campo());
        assertEquals(RegraNegocio.RN_002, segundo.regra());
    }

    @Test
    @DisplayName("RN-002 — categoria numérica produz CAMPO_TIPO_INVALIDO em despesa.categoria")
    void categoriaNumerica_campoTipoInvalido() {
        JsonNode lista = despesas("""
                {
                  "despesas": [
                    {
                      "id": "d-002",
                      "data": "2026-07-03",
                      "categoria": 42,
                      "descricao": "Táxi",
                      "fornecedor": "Uber",
                      "valor": 50.00,
                      "tem_nota_fiscal": true
                    }
                  ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);
        ItemValidado item = resultado.get(0);

        assertEquals(1, item.getMotivos().size());
        Motivo motivo = item.getMotivos().get(0);
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, motivo.codigo());
        assertEquals(CampoCanonico.CATEGORIA, motivo.campo());
    }

    @Test
    @DisplayName("RN-002 — id vazio produz CAMPO_FORMATO_INVALIDO em despesa.id")
    void idVazio_campoFormatoInvalido() {
        JsonNode lista = despesas("""
                {
                  "despesas": [
                    {
                      "id": "",
                      "data": "2026-07-03",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": 50.00,
                      "tem_nota_fiscal": true
                    }
                  ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);
        ItemValidado item = resultado.get(0);

        assertEquals(1, item.getMotivos().size());
        Motivo motivo = item.getMotivos().get(0);
        assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, motivo.codigo());
        assertEquals(CampoCanonico.ID, motivo.campo());
        assertNull(item.getId(), "id inválido não deve ser preservado");
    }

    @Test
    @DisplayName("RN-002 / CA-022 — elemento que não é objeto produz ITEM_TIPO_INVALIDO único, todos os campos tipados nulos, e o restante do arquivo continua sendo processado")
    void elementoNaoObjeto_itemTipoInvalidoUnicoEProcessamentoContinua() {
        JsonNode lista = despesas("""
                {
                  "despesas": [
                    "despesa",
                    {
                      "id": "d-003",
                      "data": "2026-07-03",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": 50.00,
                      "tem_nota_fiscal": true
                    }
                  ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);

        assertEquals(2, resultado.size(), "o restante do arquivo continua sendo processado");

        ItemValidado primeiro = resultado.get(0);
        assertEquals(1, primeiro.getIndiceEntrada(), "indiceEntrada deve ser preservado");
        assertEquals(1, primeiro.getMotivos().size(), "motivo único");
        Motivo motivo = primeiro.getMotivos().get(0);
        assertEquals(MotivoCodigo.ITEM_TIPO_INVALIDO, motivo.codigo());
        assertEquals(RegraNegocio.RN_002, motivo.regra());
        assertNull(motivo.campo(), "campo deve ser nulo para ITEM_TIPO_INVALIDO");
        assertNull(primeiro.getId());
        assertNull(primeiro.getData());
        assertNull(primeiro.getCategoria());
        assertNull(primeiro.getDescricao());
        assertNull(primeiro.getFornecedor());
        assertNull(primeiro.getValor());
        assertNull(primeiro.getTemNotaFiscal());
        assertNull(primeiro.getValorInformado());

        ItemValidado segundo = resultado.get(1);
        assertEquals(2, segundo.getIndiceEntrada());
        assertEquals(0, segundo.getMotivos().size(), "o segundo item é estruturalmente válido");
        assertEquals("d-003", segundo.getId());
    }

    @Test
    @DisplayName("RN-002 / CA-023 — cinco campos malformados produzem cinco motivos na ordem canônica de contrato")
    void ca023_cincoCamposMalformados_cincoMotivosNaOrdemCanonica() {
        JsonNode lista = despesas("""
                {
                  "despesas": [
                    {
                      "id": "",
                      "data": "31/07/2026",
                      "categoria": 42,
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": "72,50",
                      "tem_nota_fiscal": "sim"
                    }
                  ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);
        ItemValidado item = resultado.get(0);

        List<Motivo> motivos = item.getMotivos();
        assertEquals(5, motivos.size(), "devem existir exatamente cinco motivos");

        assertEquals(new Motivo(MotivoCodigo.CAMPO_FORMATO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.ID),
                motivos.get(0));
        assertEquals(new Motivo(MotivoCodigo.CAMPO_FORMATO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.DATA),
                motivos.get(1));
        assertEquals(new Motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.CATEGORIA),
                motivos.get(2));
        assertEquals(new Motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.VALOR),
                motivos.get(3));
        assertEquals(new Motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.TEM_NOTA_FISCAL),
                motivos.get(4));

        assertEquals("72,50", item.getValorInformado().asText(),
                "valorInformado deve preservar a string \"72,50\" mesmo com tipo inválido");
        assertNull(item.getId(), "id vazio não deve ser preservado");
        assertNull(item.getValor(), "valor de tipo inválido não deve ser preservado no campo tipado");
    }

    @Test
    @DisplayName("RN-002 — valor zero e valor negativo são estruturalmente válidos (VALOR_NAO_POSITIVO pertence à T-008)")
    void valorZeroENegativo_estruturalmenteValidos() {
        JsonNode lista = despesas("""
                {
                  "despesas": [
                    {
                      "id": "d-004",
                      "data": "2026-07-03",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": 0,
                      "tem_nota_fiscal": true
                    },
                    {
                      "id": "d-005",
                      "data": "2026-07-03",
                      "categoria": "transporte_urbano",
                      "descricao": "Estorno",
                      "fornecedor": "Uber",
                      "valor": -45.00,
                      "tem_nota_fiscal": false
                    }
                  ]
                }
                """);

        List<ItemValidado> resultado = ValidadorItem.validarLista(lista);

        assertEquals(0, resultado.get(0).getMotivos().size(),
                "valor zero não produz motivo estrutural — VALOR_NAO_POSITIVO é responsabilidade de T-008");
        assertEquals(0, resultado.get(1).getMotivos().size(),
                "valor negativo não produz motivo estrutural — VALOR_NAO_POSITIVO é responsabilidade de T-008");
        assertEquals(0, BigDecimal.ZERO.compareTo(resultado.get(0).getValor()));
        assertEquals(0, new BigDecimal("-45.00").compareTo(resultado.get(1).getValor()));

        for (Motivo motivo : resultado.get(0).getMotivos()) {
            assertNotEquals(MotivoCodigo.VALOR_NAO_POSITIVO, motivo.codigo());
        }
    }
}
