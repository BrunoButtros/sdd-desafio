package com.desafio.reembolso.escritor;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.CompositorSaida.ResultadoItem;
import com.desafio.reembolso.pipeline.SomadorTotal;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DecimalNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre a materialização do documento JSON de saída (spec 4.3) por
 * {@link EscritorResultado}. Constrói {@link Envelope}, {@link ResultadoItem}
 * e {@link Motivo} diretamente, com dados controlados — nunca executa o
 * pipeline completo (isso pertence a T-020, junto com o fechamento de R$
 * 585,43). Toda verificação estrutural usa um {@link ObjectMapper}
 * independente do escritor; verificações de grafia monetária (duas casas,
 * nunca notação científica, nunca string) usam extração localizada do texto
 * serializado, ancorada no nome do campo, para não confundir "60.00" com
 * ocorrências como "160.00".
 */
class EscritorResultadoTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    // ---- Infraestrutura de construção ------------------------------------------

    private static JsonNode ler(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Envelope envelope(String id, String nome, String centroCusto,
                                      String competencia, String inicio, String fim) {
        return new Envelope(id, nome, centroCusto, competencia,
                LocalDate.parse(inicio), LocalDate.parse(fim), MAPPER.createArrayNode());
    }

    private static Envelope envelopePadrao() {
        return envelope("c-001", "Pessoa", "CC-01", "2026-07", "2026-07-01", "2026-07-31");
    }

    private static ResultadoItem resultado(int indice, String id, JsonNode valorInformado,
                                            BigDecimal valorNormalizado, BigDecimal valorReembolsavel,
                                            Decisao decisao, List<Motivo> motivos) {
        return new ResultadoItem(indice, id, valorInformado, valorNormalizado,
                valorReembolsavel, decisao, motivos);
    }

    private static Motivo motivo(MotivoCodigo codigo, RegraNegocio regra, CampoCanonico campo) {
        return new Motivo(codigo, regra, campo);
    }

    /**
     * Confirma que {@code campo} aparece como número JSON com exatamente o
     * texto {@code valorEsperado} — nunca como string, e nunca como prefixo
     * de um valor com mais casas (o lookahead por delimitador JSON impede
     * que "60.00" seja considerado presente dentro de "60.000").
     */
    private static void assertMonetarioExato(String json, String campo, String valorEsperado) {
        String regex = "\"" + Pattern.quote(campo) + "\":" + Pattern.quote(valorEsperado) + "(?=[,}\\]])";
        Matcher casador = Pattern.compile(regex).matcher(json);
        assertTrue(casador.find(),
                () -> "esperado \"" + campo + "\":" + valorEsperado
                        + " seguido de delimitador JSON (, } ou ]), ausente em: " + json);
        assertFalse(json.contains("\"" + campo + "\":\"" + valorEsperado + "\""),
                campo + " não deve aparecer como string");
    }

    private static int contarOcorrencias(String texto, String trecho) {
        int contagem = 0;
        int indice = 0;
        while ((indice = texto.indexOf(trecho, indice)) != -1) {
            contagem++;
            indice += trecho.length();
        }
        return contagem;
    }

    private static List<String> nomesDeCampo(JsonNode objeto) {
        List<String> nomes = new ArrayList<>();
        objeto.fieldNames().forEachRemaining(nomes::add);
        return nomes;
    }

    // ---- 1. Schema completo -----------------------------------------------------

    @Test
    @DisplayName("1 — schema completo: raiz tem exatamente colaborador, periodo, resultados, total_reembolsavel, nesta ordem")
    void schemaCompleto_raizComQuatroCamposNaOrdem() {
        Envelope envelope = envelopePadrao();
        List<ResultadoItem> resultados = List.of(
                resultado(1, "d-001", DecimalNode.valueOf(new BigDecimal("60.00")), new BigDecimal("60.00"),
                        new BigDecimal("60.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()),
                resultado(2, "d-002", DecimalNode.valueOf(new BigDecimal("0.00")), new BigDecimal("0.00"),
                        new BigDecimal("0.00"), Decisao.RECUSADO,
                        List.of(motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null))),
                resultado(3, "d-003", DecimalNode.valueOf(new BigDecimal("20.00")), new BigDecimal("20.00"),
                        new BigDecimal("20.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of())
        );

        String json = EscritorResultado.serializar(envelope, resultados, new BigDecimal("80.00"));
        JsonNode raiz = ler(json);

        assertEquals(List.of("colaborador", "periodo", "resultados", "total_reembolsavel"), nomesDeCampo(raiz));
        assertTrue(raiz.get("colaborador").isObject());
        assertTrue(raiz.get("periodo").isObject());
        assertTrue(raiz.get("resultados").isArray());
        assertTrue(raiz.get("total_reembolsavel").isNumber());
    }

    // ---- 2. Metadados completos ---------------------------------------------------

    @Test
    @DisplayName("2 — metadados completos: colaborador e periodo preservados com os valores da entrada")
    void metadadosCompletos_colaboradorEPeriodoPreservados() {
        Envelope envelope = envelopePadrao();

        String json = EscritorResultado.serializar(envelope, List.of(), new BigDecimal("0.00"));
        JsonNode raiz = ler(json);

        JsonNode colaborador = raiz.get("colaborador");
        assertEquals(List.of("id", "nome", "centro_custo"), nomesDeCampo(colaborador));
        assertEquals("c-001", colaborador.get("id").asText());
        assertEquals("Pessoa", colaborador.get("nome").asText());
        assertEquals("CC-01", colaborador.get("centro_custo").asText());

        JsonNode periodo = raiz.get("periodo");
        assertEquals(List.of("competencia", "inicio", "fim"), nomesDeCampo(periodo));
        assertEquals("2026-07", periodo.get("competencia").asText());
        assertEquals("2026-07-01", periodo.get("inicio").asText());
        assertEquals("2026-07-31", periodo.get("fim").asText());
    }

    // ---- 3. Metadados nulos continuam presentes ------------------------------------

    @Test
    @DisplayName("3 — metadados nulos continuam presentes como chave com valor JSON nulo")
    void metadadosNulos_chavesPermanecemComValorNulo() {
        Envelope envelope = envelope(null, null, null, null, "2026-07-01", "2026-07-31");

        String json = EscritorResultado.serializar(envelope, List.of(), new BigDecimal("0.00"));
        JsonNode raiz = ler(json);

        JsonNode colaborador = raiz.get("colaborador");
        assertTrue(colaborador.has("id"));
        assertTrue(colaborador.get("id").isNull());
        assertTrue(colaborador.has("nome"));
        assertTrue(colaborador.get("nome").isNull());
        assertTrue(colaborador.has("centro_custo"));
        assertTrue(colaborador.get("centro_custo").isNull());

        JsonNode periodo = raiz.get("periodo");
        assertTrue(periodo.has("competencia"));
        assertTrue(periodo.get("competencia").isNull());
        assertEquals("2026-07-01", periodo.get("inicio").asText());
        assertEquals("2026-07-31", periodo.get("fim").asText());
    }

    // ---- 4. Registro completo por item ---------------------------------------------

    @Test
    @DisplayName("4 — cada registro de resultados tem exatamente os sete campos, nesta ordem")
    void registroPorItem_seteCamposNaOrdem() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", DecimalNode.valueOf(new BigDecimal("60.00")),
                new BigDecimal("60.00"), new BigDecimal("60.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("60.00"));
        JsonNode registro = ler(json).get("resultados").get(0);

        assertEquals(List.of("indice_entrada", "id", "valor_informado", "valor_normalizado",
                "valor_reembolsavel", "decisao", "motivos"), nomesDeCampo(registro));
    }

    // ---- 5. Valor 60.00 -------------------------------------------------------------

    @Test
    @DisplayName("5 — valor 60.00: número JSON, texto exato 60.00, nunca string, nunca 60.0, nunca notação científica")
    void valor60_00_numeroExatoSemVariacoes() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", DecimalNode.valueOf(new BigDecimal("60.00")),
                new BigDecimal("60.00"), new BigDecimal("60.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("60.00"));

        assertMonetarioExato(json, "valor_normalizado", "60.00");
        assertMonetarioExato(json, "valor_reembolsavel", "60.00");
        assertMonetarioExato(json, "total_reembolsavel", "60.00");
    }

    // ---- 6. Entrada científica apresentada em notação simples -----------------------

    @Test
    @DisplayName("6 — BigDecimal(\"6E+1\") é apresentado como 60.00, nunca 6E+1/6E1/60.0")
    void entradaCientifica_apresentadaComoNotacaoSimples() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", DecimalNode.valueOf(new BigDecimal("6E+1")),
                new BigDecimal("6E+1"), new BigDecimal("6E+1"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("6E+1"));

        assertMonetarioExato(json, "valor_normalizado", "60.00");
        assertMonetarioExato(json, "valor_reembolsavel", "60.00");
        assertMonetarioExato(json, "total_reembolsavel", "60.00");
    }

    // ---- 7. 33.33 preservado ---------------------------------------------------------

    @Test
    @DisplayName("7 — 33.33 é serializado como número JSON 33.33")
    void valor33_33_preservado() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-011", DecimalNode.valueOf(new BigDecimal("33.33")),
                new BigDecimal("33.33"), new BigDecimal("33.33"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("33.33"));

        assertMonetarioExato(json, "valor_normalizado", "33.33");
        assertMonetarioExato(json, "valor_reembolsavel", "33.33");
        assertMonetarioExato(json, "total_reembolsavel", "33.33");
    }

    // ---- 8. Zero com duas casas -------------------------------------------------------

    @Test
    @DisplayName("8 — recusado, esgotado e lista vazia apresentam 0.00 com duas casas")
    void valorZero_duasCasas() {
        Envelope envelope = envelopePadrao();
        ResultadoItem recusado = resultado(1, "d-001", DecimalNode.valueOf(new BigDecimal("-45.00")),
                new BigDecimal("-45.00"), new BigDecimal("0.00"), Decisao.RECUSADO,
                List.of(motivo(MotivoCodigo.VALOR_NAO_POSITIVO, RegraNegocio.RN_006, null)));
        ResultadoItem esgotado = resultado(2, "d-002", DecimalNode.valueOf(new BigDecimal("38.00")),
                new BigDecimal("38.00"), new BigDecimal("0.00"), Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO,
                List.of(motivo(MotivoCodigo.TETO_DIARIO_ESGOTADO, RegraNegocio.RN_015, null)));

        String json = EscritorResultado.serializar(envelope, List.of(recusado, esgotado), new BigDecimal("0.00"));

        assertMonetarioExato(json, "total_reembolsavel", "0.00");
        assertEquals(2, contarOcorrencias(json, "\"valor_reembolsavel\":0.00"));

        String jsonListaVazia = EscritorResultado.serializar(envelope, List.of(), new BigDecimal("0.00"));
        assertMonetarioExato(jsonListaVazia, "total_reembolsavel", "0.00");
    }

    // ---- 9. valor_normalizado nulo -----------------------------------------------------

    @Test
    @DisplayName("9 — valor_normalizado nulo: a chave permanece presente com valor JSON nulo")
    void valorNormalizadoNulo_chavePermanece() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", null, null, new BigDecimal("0.00"), Decisao.RECUSADO,
                List.of(motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.VALOR)));

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode registro = ler(json).get("resultados").get(0);

        assertTrue(registro.has("valor_normalizado"));
        assertTrue(registro.get("valor_normalizado").isNull());
    }

    // ---- 10. id nulo ------------------------------------------------------------------

    @Test
    @DisplayName("10 — id nulo: a chave permanece presente com valor JSON nulo")
    void idNulo_chavePermanece() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, null, null, null, new BigDecimal("0.00"), Decisao.RECUSADO,
                List.of(motivo(MotivoCodigo.ITEM_TIPO_INVALIDO, RegraNegocio.RN_002, null)));

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode registro = ler(json).get("resultados").get(0);

        assertTrue(registro.has("id"));
        assertTrue(registro.get("id").isNull());
    }

    // ---- 11. valor_informado preservado por tipo ------------------------------------

    @Test
    @DisplayName("11a — valor_informado número 33.333 preservado exatamente, sem virar 33.33")
    void valorInformadoNumero_preservadoExatamente() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-011", DecimalNode.valueOf(new BigDecimal("33.333")),
                new BigDecimal("33.33"), new BigDecimal("33.33"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("33.33"));
        JsonNode valorInformado = ler(json).get("resultados").get(0).get("valor_informado");

        assertTrue(valorInformado.isNumber());
        assertEquals(0, new BigDecimal("33.333").compareTo(valorInformado.decimalValue()));
    }

    @Test
    @DisplayName("11b — valor_informado texto \"72,50\" preservado como string")
    void valorInformadoTexto_preservado() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", ler("\"72,50\""), null, new BigDecimal("0.00"), Decisao.RECUSADO,
                List.of(motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.VALOR)));

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode valorInformado = ler(json).get("resultados").get(0).get("valor_informado");

        assertTrue(valorInformado.isTextual());
        assertEquals("72,50", valorInformado.asText());
    }

    @Test
    @DisplayName("11c — valor_informado booleano true preservado, sem virar texto")
    void valorInformadoBooleano_preservado() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", ler("true"), null, new BigDecimal("0.00"), Decisao.RECUSADO,
                List.of(motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.VALOR)));

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode valorInformado = ler(json).get("resultados").get(0).get("valor_informado");

        assertTrue(valorInformado.isBoolean());
        assertTrue(valorInformado.asBoolean());
    }

    @Test
    @DisplayName("11d — valor_informado lista [1, 2] preservada")
    void valorInformadoLista_preservada() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", ler("[1, 2]"), null, new BigDecimal("0.00"), Decisao.RECUSADO,
                List.of(motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.VALOR)));

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode valorInformado = ler(json).get("resultados").get(0).get("valor_informado");

        assertTrue(valorInformado.isArray());
        assertEquals(2, valorInformado.size());
        assertEquals(1, valorInformado.get(0).asInt());
        assertEquals(2, valorInformado.get(1).asInt());
    }

    @Test
    @DisplayName("11e — valor_informado objeto {\"origem\": \"entrada\"} preservado")
    void valorInformadoObjeto_preservado() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", ler("{\"origem\": \"entrada\"}"), null, new BigDecimal("0.00"),
                Decisao.RECUSADO,
                List.of(motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.VALOR)));

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode valorInformado = ler(json).get("resultados").get(0).get("valor_informado");

        assertTrue(valorInformado.isObject());
        assertEquals("entrada", valorInformado.get("origem").asText());
    }

    @Test
    @DisplayName("11f — valor_informado nulo (chave valor ausente ou nula) preservado como JSON nulo")
    void valorInformadoNulo_preservado() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", null, null, new BigDecimal("0.00"), Decisao.RECUSADO,
                List.of(motivo(MotivoCodigo.CAMPO_AUSENTE, RegraNegocio.RN_002, CampoCanonico.VALOR)));

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode registro = ler(json).get("resultados").get(0);

        assertTrue(registro.has("valor_informado"));
        assertTrue(registro.get("valor_informado").isNull());
    }

    // ---- 12. Decisões canônicas ---------------------------------------------------

    @ParameterizedTest(name = "12 — decisao {0} serializa para o texto canônico de 4.4")
    @EnumSource(Decisao.class)
    @DisplayName("12 — as quatro decisões serializam para o texto canônico")
    void decisoesCanonicasNaSaida(Decisao decisao) {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", null, null, new BigDecimal("0.00"), decisao, List.of());

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode registro = ler(json).get("resultados").get(0);

        assertEquals(decisao.textoCanonico(), registro.get("decisao").asText());
    }

    // ---- 13. Motivos: campos exatos --------------------------------------------------

    @Test
    @DisplayName("13 — motivos serializam codigo/regra/campo exatamente, com apenas esses três campos")
    void motivos_camposExatos() {
        Envelope envelope = envelopePadrao();
        List<Motivo> motivos = List.of(
                motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, CampoCanonico.VALOR),
                motivo(MotivoCodigo.ID_DUPLICADO, RegraNegocio.RN_003, CampoCanonico.ID),
                motivo(MotivoCodigo.TETO_DIARIO_APLICADO, RegraNegocio.RN_011, null)
        );
        ResultadoItem item = resultado(1, "d-001", null, null, new BigDecimal("0.00"), Decisao.RECUSADO, motivos);

        String json = EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("0.00"));
        JsonNode motivosNode = ler(json).get("resultados").get(0).get("motivos");

        assertEquals(3, motivosNode.size());

        JsonNode m1 = motivosNode.get(0);
        assertEquals(List.of("codigo", "regra", "campo"), nomesDeCampo(m1));
        assertEquals("CAMPO_TIPO_INVALIDO", m1.get("codigo").asText());
        assertEquals("RN-002", m1.get("regra").asText());
        assertEquals("despesa.valor", m1.get("campo").asText());

        JsonNode m2 = motivosNode.get(1);
        assertEquals("ID_DUPLICADO", m2.get("codigo").asText());
        assertEquals("RN-003", m2.get("regra").asText());
        assertEquals("despesa.id", m2.get("campo").asText());

        JsonNode m3 = motivosNode.get(2);
        assertEquals("TETO_DIARIO_APLICADO", m3.get("codigo").asText());
        assertEquals("RN-011", m3.get("regra").asText());
        assertTrue(m3.get("campo").isNull());
    }

    // ---- 14. Motivos vazios e ordem preservada -----------------------------------

    @Test
    @DisplayName("14 — item integral produz motivos vazio; item com vários motivos preserva a ordem recebida, sem reordenar")
    void motivosVaziosEOrdemPreservada() {
        Envelope envelope = envelopePadrao();
        ResultadoItem integral = resultado(1, "d-001", null, new BigDecimal("30.00"),
                new BigDecimal("30.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        // Ordem deliberadamente fora da precedência de 8.3 — a ordenação já
        // pertence a T-016 (CompositorSaida); o escritor apenas materializa
        // a lista exatamente como recebida.
        List<Motivo> motivosForaDaPrecedencia = List.of(
                motivo(MotivoCodigo.NOTA_FISCAL_AUSENTE, RegraNegocio.RN_009, null),
                motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null),
                motivo(MotivoCodigo.FORA_COMPETENCIA, RegraNegocio.RN_008, null)
        );
        ResultadoItem comVariosMotivos = resultado(2, "d-002", null, null, new BigDecimal("0.00"),
                Decisao.RECUSADO, motivosForaDaPrecedencia);

        String json = EscritorResultado.serializar(
                envelope, List.of(integral, comVariosMotivos), new BigDecimal("30.00"));
        JsonNode resultados = ler(json).get("resultados");

        assertEquals(0, resultados.get(0).get("motivos").size());

        JsonNode motivos = resultados.get(1).get("motivos");
        assertEquals(3, motivos.size());
        assertEquals("NOTA_FISCAL_AUSENTE", motivos.get(0).get("codigo").asText());
        assertEquals("CATEGORIA_FORA_POLITICA", motivos.get(1).get("codigo").asText());
        assertEquals("FORA_COMPETENCIA", motivos.get(2).get("codigo").asText());
    }

    // ---- 15. Ordem dos resultados preservada -----------------------------------------

    @Test
    @DisplayName("15 — resultados fornecidos com índices 3, 1, 2 permanecem exatamente nessa ordem na saída")
    void ordemDosResultados_preservada() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item3 = resultado(3, "d-003", null, new BigDecimal("10.00"), new BigDecimal("10.00"),
                Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());
        ResultadoItem item1 = resultado(1, "d-001", null, new BigDecimal("20.00"), new BigDecimal("20.00"),
                Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());
        ResultadoItem item2 = resultado(2, "d-002", null, new BigDecimal("30.00"), new BigDecimal("30.00"),
                Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        String json = EscritorResultado.serializar(
                envelope, List.of(item3, item1, item2), new BigDecimal("60.00"));
        JsonNode resultados = ler(json).get("resultados");

        assertEquals(3, resultados.get(0).get("indice_entrada").asInt());
        assertEquals(1, resultados.get(1).get("indice_entrada").asInt());
        assertEquals(2, resultados.get(2).get("indice_entrada").asInt());
    }

    // ---- 16. Lista vazia --------------------------------------------------------------

    @Test
    @DisplayName("16 — lista vazia: documento válido com resultados: [] e total_reembolsavel: 0.00")
    void listaVazia_documentoValido() {
        Envelope envelope = envelopePadrao();

        String json = EscritorResultado.serializar(envelope, List.of(), new BigDecimal("0.00"));
        JsonNode raiz = ler(json);

        assertTrue(raiz.get("resultados").isArray());
        assertEquals(0, raiz.get("resultados").size());
        assertMonetarioExato(json, "total_reembolsavel", "0.00");
    }

    // ---- 17. Valor incompatível com duas casas ---------------------------------------

    @Test
    @DisplayName("17a — apenas valor_reembolsavel 60.001 (normalizado e total válidos): lança IllegalArgumentException isolando o campo inválido")
    void valorReembolsavelIncompativelComDuasCasas_lancaIllegalArgumentException() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", null, new BigDecimal("60.00"), new BigDecimal("60.001"),
                Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("60.00")));
    }

    @Test
    @DisplayName("17b — total_reembolsavel 60.001 também lança IllegalArgumentException")
    void totalIncompativelComDuasCasas_lancaIllegalArgumentException() {
        Envelope envelope = envelopePadrao();

        assertThrows(IllegalArgumentException.class,
                () -> EscritorResultado.serializar(envelope, List.of(), new BigDecimal("60.001")));
    }

    @Test
    @DisplayName("17c — apenas valor_normalizado 60.001 (reembolsável e total válidos): lança IllegalArgumentException isolando o campo inválido")
    void valorNormalizadoIncompativelComDuasCasas_lancaIllegalArgumentException() {
        Envelope envelope = envelopePadrao();
        ResultadoItem item = resultado(1, "d-001", null, new BigDecimal("60.001"), new BigDecimal("60.00"),
                Decisao.INTEGRALMENTE_REEMBOLSADO, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> EscritorResultado.serializar(envelope, List.of(item), new BigDecimal("60.00")));
    }

    // ---- 18. Argumentos e elementos nulos ---------------------------------------------

    @Test
    @DisplayName("18a — envelope nulo lança NullPointerException")
    void envelopeNulo_lancaNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> EscritorResultado.serializar(null, List.of(), new BigDecimal("0.00")));
    }

    @Test
    @DisplayName("18b — lista de resultados nula lança NullPointerException")
    void listaNula_lancaNullPointerException() {
        Envelope envelope = envelopePadrao();

        assertThrows(NullPointerException.class,
                () -> EscritorResultado.serializar(envelope, null, new BigDecimal("0.00")));
    }

    @Test
    @DisplayName("18c — total nulo lança NullPointerException")
    void totalNulo_lancaNullPointerException() {
        Envelope envelope = envelopePadrao();

        assertThrows(NullPointerException.class,
                () -> EscritorResultado.serializar(envelope, List.of(), null));
    }

    @Test
    @DisplayName("18d — elemento nulo dentro da lista de resultados lança NullPointerException")
    void elementoNuloNaLista_lancaNullPointerException() {
        Envelope envelope = envelopePadrao();
        List<ResultadoItem> comElementoNulo = new ArrayList<>();
        comElementoNulo.add(resultado(1, "d-001", null, new BigDecimal("10.00"), new BigDecimal("10.00"),
                Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()));
        comElementoNulo.add(null);

        assertThrows(NullPointerException.class,
                () -> EscritorResultado.serializar(envelope, comElementoNulo, new BigDecimal("10.00")));
    }

    @Test
    @DisplayName("18e — motivo nulo dentro da lista de motivos já é impedido pelo próprio ResultadoItem (List.copyOf), antes de alcançar o escritor")
    void motivoNulo_impedidoPeloProprioResultadoItem() {
        List<Motivo> motivosComNulo = new ArrayList<>();
        motivosComNulo.add(motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null));
        motivosComNulo.add(null);

        assertThrows(NullPointerException.class,
                () -> resultado(1, "d-001", null, null, new BigDecimal("0.00"), Decisao.RECUSADO, motivosComNulo));
    }

    // ---- 19. Imutabilidade -------------------------------------------------------------

    @Test
    @DisplayName("19 — após serializar, listas de entrada permanecem inalteradas e nenhuma entrada é modificada")
    void imutabilidade_entradasPreservadas() {
        Envelope envelope = envelopePadrao();
        JsonNode valorInformadoOriginal = ler("{\"origem\": \"entrada\"}");
        List<Motivo> motivos = List.of(motivo(MotivoCodigo.CATEGORIA_FORA_POLITICA, RegraNegocio.RN_007, null));
        ResultadoItem item = resultado(1, "d-001", valorInformadoOriginal, null, new BigDecimal("0.00"),
                Decisao.RECUSADO, motivos);
        List<ResultadoItem> resultados = List.of(item);

        List<ResultadoItem> resultadosAntes = List.copyOf(resultados);
        List<Motivo> motivosAntes = List.copyOf(item.motivos());
        JsonNode valorInformadoAntes = valorInformadoOriginal.deepCopy();

        EscritorResultado.serializar(envelope, resultados, new BigDecimal("0.00"));

        assertEquals(resultadosAntes, resultados);
        assertEquals(motivosAntes, item.motivos());
        assertEquals(valorInformadoAntes, item.valorInformado());
        assertEquals(valorInformadoOriginal, item.valorInformado());
    }

    // ---- 20. Determinismo ---------------------------------------------------------------

    @Test
    @DisplayName("20 — duas chamadas com as mesmas entradas produzem texto idêntico")
    void determinismo_mesmaEntradaMesmoTexto() {
        Envelope envelope = envelopePadrao();
        List<ResultadoItem> resultados = List.of(
                resultado(1, "d-001", DecimalNode.valueOf(new BigDecimal("60.00")), new BigDecimal("60.00"),
                        new BigDecimal("60.00"), Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()));

        String json1 = EscritorResultado.serializar(envelope, resultados, new BigDecimal("60.00"));
        String json2 = EscritorResultado.serializar(envelope, resultados, new BigDecimal("60.00"));

        assertEquals(json1, json2);
    }

    // ---- Extra — total produzido via SomadorTotal ----------------------------------------

    @Test
    @DisplayName("Extra — total produzido via SomadorTotal a partir da própria lista de resultados")
    void totalProduzidoViaSomadorTotal() {
        Envelope envelope = envelopePadrao();
        List<ResultadoItem> resultados = List.of(
                resultado(1, "d-001", null, new BigDecimal("60.00"), new BigDecimal("60.00"),
                        Decisao.INTEGRALMENTE_REEMBOLSADO, List.of()),
                resultado(2, "d-002", null, new BigDecimal("20.00"), new BigDecimal("20.00"),
                        Decisao.INTEGRALMENTE_REEMBOLSADO, List.of())
        );
        BigDecimal total = SomadorTotal.somar(resultados);

        String json = EscritorResultado.serializar(envelope, resultados, total);

        assertMonetarioExato(json, "total_reembolsavel", "80.00");
    }
}
