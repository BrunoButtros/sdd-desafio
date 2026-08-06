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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de integração ponta a ponta (T-020): executa {@link Main#run} contra
 * {@code exemplos/despesas-exemplo.json} e compara o JSON produzido,
 * estruturalmente, contra o fixture escrito manualmente em
 * {@code tests/resources/fixtures/despesas-exemplo-esperado.json} — nunca
 * gerado pelo próprio motor. Fecha CA-001, CA-002, CA-003 e confirma ponta a
 * ponta CA-013, CA-016 e CA-017.
 */
@DisplayName("Exemplo completo — T-020 / CA-001 a CA-003 (fechamento)")
class ExemploCompletoTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final Path ENTRADA = Path.of("exemplos", "despesas-exemplo.json");
    private static final Path FIXTURE = Path.of("tests", "resources", "fixtures", "despesas-exemplo-esperado.json");
    private static final Path POLITICA = Path.of("exemplos", "envelope", "politica-v4.json");
    private static final Path CAMBIO = Path.of("exemplos", "envelope", "cambio.json");

    @Test
    @DisplayName("processa o arquivo de exemplo e coincide estruturalmente com o fixture manual (14 registros, total 335,43)")
    void exemploCompleto_coincideEstruturalmenteComFixture(@TempDir Path tempDir) throws Exception {
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

        assertEquals(0, codigo, "processamento do arquivo de exemplo deve terminar com sucesso");
        assertEquals("", outBuffer.toString(StandardCharsets.UTF_8), "stdout deve estar vazio");
        assertEquals("", errBuffer.toString(StandardCharsets.UTF_8), "stderr deve estar vazio");

        JsonNode real = MAPPER.readTree(saida.toFile());
        JsonNode esperado = MAPPER.readTree(FIXTURE.toFile());

        assertEquals(esperado, real, "saída real deve coincidir estruturalmente, campo a campo, com o fixture manual");

        JsonNode resultados = real.get("resultados");
        assertEquals(14, resultados.size(), "exatamente 14 registros de resultado");

        assertEquals(0, new BigDecimal("335.43").compareTo(real.get("total_reembolsavel").decimalValue()),
                "total_reembolsavel deve ser 335.43");

        // Nenhuma despesa omitida ou duplicada: 14 índices distintos (1..14) e 14 ids distintos (d-001..d-014).
        Set<Integer> indicesVistos = new HashSet<>();
        Set<String> idsVistos = new HashSet<>();
        for (JsonNode registro : resultados) {
            int indice = registro.get("indice_entrada").asInt();
            String id = registro.get("id").asText();
            assertTrue(indicesVistos.add(indice), "indice_entrada " + indice + " não pode se repetir");
            assertTrue(idsVistos.add(id), "id " + id + " não pode se repetir");
        }
        for (int i = 1; i <= 14; i++) {
            assertTrue(indicesVistos.contains(i), "indice_entrada " + i + " não pode estar ausente");
        }

        // d-006: primeira ocorrência do par de duplicidade — INTEGRALMENTE_REEMBOLSADO, 54.90.
        JsonNode d006 = porId(resultados, "d-006");
        assertEquals("INTEGRALMENTE_REEMBOLSADO", d006.get("decisao").asText());
        assertEquals(0, new BigDecimal("54.90").compareTo(d006.get("valor_reembolsavel").decimalValue()));

        // d-007: ocorrência posterior — RECUSADO, 0.00, motivo DUPLICIDADE.
        JsonNode d007 = porId(resultados, "d-007");
        assertEquals("RECUSADO", d007.get("decisao").asText());
        assertEquals(0, new BigDecimal("0.00").compareTo(d007.get("valor_reembolsavel").decimalValue()));
        assertEquals(1, d007.get("motivos").size());
        assertEquals("DUPLICIDADE", d007.get("motivos").get(0).get("codigo").asText());

        // d-011: valor_informado 33.333 preservado, valor_normalizado 33.33.
        JsonNode d011 = porId(resultados, "d-011");
        assertEquals(0, new BigDecimal("33.333").compareTo(d011.get("valor_informado").decimalValue()));
        assertEquals(0, new BigDecimal("33.33").compareTo(d011.get("valor_normalizado").decimalValue()));

        // d-014: categoria ALIMENTACAO normaliza para alimentacao, teto diário aplicado — 60.00.
        JsonNode d014 = porId(resultados, "d-014");
        assertEquals(0, new BigDecimal("60.00").compareTo(d014.get("valor_reembolsavel").decimalValue()));
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
