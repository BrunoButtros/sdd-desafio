package com.desafio.reembolso;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integração ponta a ponta (T-052 / CA-039, RN-019, RN-020): executa {@link Main#run}
 * sobre {@code exemplos/envelope/despesas-envelope.json} (Rafael Nkemelu, {@code CC-COMERCIAL})
 * com os arquivos reais {@code exemplos/envelope/politica-v4.json} e
 * {@code exemplos/envelope/cambio.json}, e confirma que o resultado coincide estruturalmente
 * com o fixture manual {@code despesas-envelope-esperado.json} (10 registros, spec §12.3),
 * incluindo {@code total_reembolsavel = 1143.26}.
 */
@DisplayName("Integração envelope — T-052 / CA-039, RN-019, RN-020")
class IntegracaoEnvelopeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final Path ENTRADA = Path.of("exemplos", "envelope", "despesas-envelope.json");
    private static final Path POLITICA = Path.of("exemplos", "envelope", "politica-v4.json");
    private static final Path CAMBIO = Path.of("exemplos", "envelope", "cambio.json");
    private static final Path FIXTURE_ESPERADO =
            Path.of("tests", "resources", "fixtures", "despesas-envelope-esperado.json");

    @Test
    @DisplayName("processa despesas-envelope.json (Rafael/CC-COMERCIAL) com politica-v4.json e cambio.json reais "
            + "e coincide estruturalmente com o fixture esperado (10 registros, total 1143,26)")
    void integracaoEnvelope_rafaelCcComercial_coincideEstruturalmenteComFixtureEsperado(@TempDir Path tempDir)
            throws Exception {
        Path saida = tempDir.resolve("resultado-envelope.json");

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        int codigo;
        try (PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);
             PrintStream err = new PrintStream(errBuffer, true, StandardCharsets.UTF_8)) {
            codigo = Main.run(
                    new String[]{
                            "calcular",
                            "--input", ENTRADA.toString(),
                            "--output", saida.toString(),
                            "--politica", POLITICA.toString(),
                            "--cambio", CAMBIO.toString()},
                    out, err);
            out.flush();
            err.flush();
        }

        assertEquals(0, codigo, "processamento deve terminar com sucesso");
        assertEquals("", outBuffer.toString(StandardCharsets.UTF_8), "stdout deve estar vazio");
        assertEquals("", errBuffer.toString(StandardCharsets.UTF_8), "stderr deve estar vazio");
        assertTrue(saida.toFile().isFile(), "arquivo de saida deve ter sido criado");

        JsonNode real = MAPPER.readTree(saida.toFile());
        JsonNode esperado = MAPPER.readTree(FIXTURE_ESPERADO.toFile());

        assertEquals(esperado.get("colaborador"), real.get("colaborador"),
                "colaborador deve coincidir com o fixture esperado");
        assertEquals(esperado.get("periodo"), real.get("periodo"),
                "periodo deve coincidir com o fixture esperado");

        JsonNode resultados = real.get("resultados");
        assertEquals(10, resultados.size(), "exatamente dez resultados");

        JsonNode resultadosEsperados = esperado.get("resultados");
        for (int i = 0; i < 10; i++) {
            assertEquals(resultadosEsperados.get(i).get("id").asText(), resultados.get(i).get("id").asText(),
                    "ordem dos resultados deve ser preservada, e-001 a e-010 (posicao " + i + ")");
        }

        assertEquals(esperado, real, "saida real deve ser estruturalmente igual ao fixture esperado, campo a campo");

        assertEquals(0, new BigDecimal("1143.26").compareTo(real.get("total_reembolsavel").decimalValue()),
                "total_reembolsavel deve ser 1143.26");

        // Conversões cambiais explícitas (RN-020).
        JsonNode e002 = porId(resultados, "e-002");
        assertEquals("EUR", e002.get("moeda").asText());
        assertEquals(0, new BigDecimal("5.93").compareTo(e002.get("taxa_cambio_aplicada").decimalValue()));
        assertEquals("2026-07-14", e002.get("data_cotacao_utilizada").asText());
        assertEquals(0, new BigDecimal("130.46").compareTo(e002.get("valor_normalizado").decimalValue()));

        JsonNode e003 = porId(resultados, "e-003");
        assertEquals("EUR", e003.get("moeda").asText());
        assertEquals(0, new BigDecimal("5.88").compareTo(e003.get("taxa_cambio_aplicada").decimalValue()));
        assertEquals("2026-07-15", e003.get("data_cotacao_utilizada").asText());
        assertEquals(0, new BigDecimal("85.26").compareTo(e003.get("valor_normalizado").decimalValue()));

        JsonNode e004 = porId(resultados, "e-004");
        assertEquals("EUR", e004.get("moeda").asText());
        assertEquals(0, new BigDecimal("5.96").compareTo(e004.get("taxa_cambio_aplicada").decimalValue()));
        assertEquals("2026-07-17", e004.get("data_cotacao_utilizada").asText());
        assertEquals(0, new BigDecimal("178.80").compareTo(e004.get("valor_normalizado").decimalValue()));

        JsonNode e005 = porId(resultados, "e-005");
        assertEquals("USD", e005.get("moeda").asText());
        assertEquals(0, new BigDecimal("5.50").compareTo(e005.get("taxa_cambio_aplicada").decimalValue()));
        assertEquals("2026-07-20", e005.get("data_cotacao_utilizada").asText());
        assertEquals(0, new BigDecimal("220.00").compareTo(e005.get("valor_normalizado").decimalValue()));

        // e-001: representacao em BRL, teto diario compartilhado (RN-019, nao RN-011/RN-012).
        JsonNode e001 = porId(resultados, "e-001");
        assertEquals("PARCIALMENTE_REEMBOLSADO", e001.get("decisao").asText());
        assertEquals(0, new BigDecimal("300.00").compareTo(e001.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, e001.get("motivos").size());
        JsonNode motivoE001 = e001.get("motivos").get(0);
        assertEquals("TETO_DIARIO_APLICADO", motivoE001.get("codigo").asText());
        assertEquals("RN-019", motivoE001.get("regra").asText());
        assertTrue(motivoE001.get("campo").isNull());

        // e-005: nota fiscal ausente apos conversao — nenhum motivo de teto e emitido.
        assertEquals("RECUSADO", e005.get("decisao").asText());
        assertEquals(0, BigDecimal.ZERO.compareTo(e005.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, e005.get("motivos").size());
        JsonNode motivoE005 = e005.get("motivos").get(0);
        assertEquals("NOTA_FISCAL_AUSENTE", motivoE005.get("codigo").asText());
        assertEquals("RN-009", motivoE005.get("regra").asText());
        assertTrue(motivoE005.get("campo").isNull());

        // e-006: GBP sem cotacao — preserva a moeda, campos cambiais nulos, motivo com campo despesa.moeda.
        JsonNode e006 = porId(resultados, "e-006");
        assertEquals("GBP", e006.get("moeda").asText());
        assertTrue(e006.get("taxa_cambio_aplicada").isNull());
        assertTrue(e006.get("data_cotacao_utilizada").isNull());
        assertTrue(e006.get("valor_normalizado").isNull());
        assertEquals("RECUSADO", e006.get("decisao").asText());
        assertEquals(0, BigDecimal.ZERO.compareTo(e006.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, e006.get("motivos").size());
        JsonNode motivoE006 = e006.get("motivos").get(0);
        assertEquals("MOEDA_SEM_COTACAO", motivoE006.get("codigo").asText());
        assertEquals("RN-020", motivoE006.get("regra").asText());
        assertEquals("despesa.moeda", motivoE006.get("campo").asText());

        // e-007: hospedagem — teto individual.
        JsonNode e007 = porId(resultados, "e-007");
        assertEquals("PARCIALMENTE_REEMBOLSADO", e007.get("decisao").asText());
        assertEquals(0, new BigDecimal("400.00").compareTo(e007.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, e007.get("motivos").size());
        JsonNode motivoE007 = e007.get("motivos").get(0);
        assertEquals("TETO_HOSPEDAGEM_APLICADO", motivoE007.get("codigo").asText());
        assertEquals("RN-013", motivoE007.get("regra").asText());
        assertTrue(motivoE007.get("campo").isNull());

        // e-009: coworking ausente da tabela exclusiva de CC-COMERCIAL — nunca CATEGORIA_FORA_POLITICA.
        JsonNode e009 = porId(resultados, "e-009");
        assertEquals("RECUSADO", e009.get("decisao").asText());
        assertEquals(0, BigDecimal.ZERO.compareTo(e009.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, e009.get("motivos").size());
        JsonNode motivoE009 = e009.get("motivos").get(0);
        assertEquals("CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO", motivoE009.get("codigo").asText());
        assertEquals("RN-019", motivoE009.get("regra").asText());
        assertTrue(motivoE009.get("campo").isNull());
        assertTrue(!"CATEGORIA_FORA_POLITICA".equals(motivoE009.get("codigo").asText()),
                "e-009 nunca deve trazer CATEGORIA_FORA_POLITICA");

        // e-010: chave moeda ausente assume BRL silenciosamente, sem motivo estrutural.
        JsonNode e010 = porId(resultados, "e-010");
        assertEquals("BRL", e010.get("moeda").asText());
        assertEquals(0, BigDecimal.ONE.compareTo(e010.get("taxa_cambio_aplicada").decimalValue()));
        assertTrue(e010.get("data_cotacao_utilizada").isNull());
        assertEquals("INTEGRALMENTE_REEMBOLSADO", e010.get("decisao").asText());
        assertEquals(0, new BigDecimal("88.00").compareTo(e010.get("valor_reembolsavel").decimalValue()));
        assertEquals(0, e010.get("motivos").size(), "e-010 nao deve produzir CAMPO_AUSENTE nem qualquer outro motivo");
    }

    private static JsonNode porId(JsonNode resultados, String id) {
        for (JsonNode registro : resultados) {
            if (id.equals(registro.get("id").asText())) {
                return registro;
            }
        }
        throw new AssertionError("nenhum resultado encontrado para id " + id);
    }
}
