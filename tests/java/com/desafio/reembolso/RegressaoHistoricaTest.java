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
 * Regressão histórica ponta a ponta (T-050 / CA-037, RN-019): executa
 * {@link Main#run} sobre {@code exemplos/despesas-exemplo.json} usando os
 * fixtures externos permanentes {@code tests/resources/fixtures/politica-historica.json}
 * e {@code tests/resources/fixtures/cambio-historico.json} — equivalentes aos
 * valores hardcoded pré-Dia 2 — e confirma que o resultado coincide
 * estruturalmente com {@code despesas-exemplo-esperado.json} (schema 1.2,
 * já migrado em T-049), incluindo {@code total_reembolsavel = 585.43}.
 */
@DisplayName("Regressão histórica — T-050 / CA-037, RN-019")
class RegressaoHistoricaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final Path ENTRADA = Path.of("exemplos", "despesas-exemplo.json");
    private static final Path POLITICA = Path.of("tests", "resources", "fixtures", "politica-historica.json");
    private static final Path CAMBIO = Path.of("tests", "resources", "fixtures", "cambio-historico.json");
    private static final Path FIXTURE_ESPERADO =
            Path.of("tests", "resources", "fixtures", "despesas-exemplo-esperado.json");

    @Test
    @DisplayName("processa despesas-exemplo.json com os fixtures históricos e coincide estruturalmente "
            + "com o fixture esperado (14 registros, total 585,43)")
    void regressaoHistorica_coincideEstruturalmenteComFixtureEsperado(@TempDir Path tempDir) throws Exception {
        Path saida = tempDir.resolve("resultado.json");

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
        assertTrue(saida.toFile().isFile(), "arquivo de saída deve ter sido criado");

        JsonNode real = MAPPER.readTree(saida.toFile());
        JsonNode esperado = MAPPER.readTree(FIXTURE_ESPERADO.toFile());

        assertEquals(esperado.get("colaborador"), real.get("colaborador"),
                "colaborador deve coincidir com o fixture esperado");
        assertEquals(esperado.get("periodo"), real.get("periodo"),
                "período deve coincidir com o fixture esperado");

        JsonNode resultados = real.get("resultados");
        assertEquals(14, resultados.size(), "exatamente 14 resultados");

        JsonNode resultadosEsperados = esperado.get("resultados");
        for (int i = 0; i < 14; i++) {
            assertEquals(resultadosEsperados.get(i).get("id").asText(), resultados.get(i).get("id").asText(),
                    "ordem dos resultados deve ser preservada (posição " + i + ")");
        }

        assertEquals(esperado, real, "saída real deve ser estruturalmente igual ao fixture esperado, campo a campo");

        assertEquals(0, new BigDecimal("585.43").compareTo(real.get("total_reembolsavel").decimalValue()),
                "total_reembolsavel deve ser 585.43");

        // Canário d-005: categoria fora da política (padrao, centro de custo desconhecido no fixture).
        JsonNode d005 = porId(resultados, "d-005");
        assertEquals("RECUSADO", d005.get("decisao").asText());
        assertEquals(0, BigDecimal.ZERO.compareTo(d005.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, d005.get("motivos").size());
        JsonNode motivoD005 = d005.get("motivos").get(0);
        assertEquals("CATEGORIA_FORA_POLITICA", motivoD005.get("codigo").asText());
        assertEquals("RN-007", motivoD005.get("regra").asText());
        assertTrue(motivoD005.get("campo").isNull());

        // Canário d-010: teto individual de hospedagem.
        JsonNode d010 = porId(resultados, "d-010");
        assertEquals("PARCIALMENTE_REEMBOLSADO", d010.get("decisao").asText());
        assertEquals(0, new BigDecimal("250.00").compareTo(d010.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, d010.get("motivos").size());
        JsonNode motivoD010 = d010.get("motivos").get(0);
        assertEquals("TETO_HOSPEDAGEM_APLICADO", motivoD010.get("codigo").asText());
        assertEquals("RN-013", motivoD010.get("regra").asText());
        assertTrue(motivoD010.get("campo").isNull());

        // Canário d-013: nota fiscal ausente.
        JsonNode d013 = porId(resultados, "d-013");
        assertEquals("RECUSADO", d013.get("decisao").asText());
        assertEquals(0, BigDecimal.ZERO.compareTo(d013.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, d013.get("motivos").size());
        JsonNode motivoD013 = d013.get("motivos").get(0);
        assertEquals("NOTA_FISCAL_AUSENTE", motivoD013.get("codigo").asText());
        assertEquals("RN-009", motivoD013.get("regra").asText());
        assertTrue(motivoD013.get("campo").isNull());

        // Campos cambiais: todos os 14 registros são BRL, taxa 1, sem data de cotação.
        for (JsonNode registro : resultados) {
            assertEquals("BRL", registro.get("moeda").asText(),
                    "moeda deve ser BRL em todos os registros (id " + registro.get("id").asText() + ")");
            assertEquals(0, BigDecimal.ONE.compareTo(registro.get("taxa_cambio_aplicada").decimalValue()),
                    "taxa_cambio_aplicada deve ser 1 (id " + registro.get("id").asText() + ")");
            assertTrue(registro.get("data_cotacao_utilizada").isNull(),
                    "data_cotacao_utilizada deve ser nula (id " + registro.get("id").asText() + ")");
        }
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
