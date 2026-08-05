package com.desafio.reembolso;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre a escrita atômica do destino (spec plan §3, DT-010, T-019): o
 * resultado é sempre escrito primeiro num arquivo temporário no mesmo
 * diretório de {@code --output}, e só então movido/substituído
 * atomicamente. Um {@code --output} preexistente permanece intacto em
 * qualquer cenário que não termine em sucesso (exit 2 ou 3), e nenhum
 * temporário sobra no diretório de destino após nenhum cenário.
 */
@DisplayName("Escrita atômica de --output — T-019 / DT-010")
class EscritaAtomicaSaidaTest {

    private static final String CONTEUDO_PREEXISTENTE = "{\"resultado\":\"antigo\"}";

    @AfterEach
    void restaurarGatilhoDeFalha() {
        Main.simularFalhaAntesDaSubstituicao = false;
    }

    private static final class Resultado {
        final int codigo;
        final String stdout;
        final String stderr;

        Resultado(int codigo, String stdout, String stderr) {
            this.codigo = codigo;
            this.stdout = stdout;
            this.stderr = stderr;
        }
    }

    private static Resultado executar(String... args) {
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);
             PrintStream err = new PrintStream(errBuffer, true, StandardCharsets.UTF_8)) {
            int codigo = Main.run(args, out, err);
            out.flush();
            err.flush();
            return new Resultado(codigo, outBuffer.toString(StandardCharsets.UTF_8), errBuffer.toString(StandardCharsets.UTF_8));
        }
    }

    private static void escreverOutputPreexistente(Path output) throws IOException {
        Files.writeString(output, CONTEUDO_PREEXISTENTE, StandardCharsets.UTF_8);
    }

    private static String jsonEnvelopeValido() {
        return """
                {
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": []
                }
                """;
    }

    private static void assertNenhumTemporarioNoDiretorio(Path diretorio) throws IOException {
        try (Stream<Path> arquivos = Files.list(diretorio)) {
            List<String> restantes = arquivos
                    .map(p -> p.getFileName().toString())
                    .filter(nome -> nome.startsWith("motor-reembolso-") && nome.endsWith(".tmp"))
                    .toList();
            assertTrue(restantes.isEmpty(), "nenhum temporário deveria sobrar no diretório de destino: " + restantes);
        }
    }

    // ---- 1. JSON sintaticamente inválido preserva output preexistente --------

    @Test
    @DisplayName("1 — JSON sintaticamente inválido preserva um --output preexistente")
    void jsonSintaticamenteInvalido_preservaOutputPreexistente(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("invalido.json");
        Files.writeString(input, "{ \"despesas\": [ ", StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

        assertEquals(2, resultado.codigo);
        assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(output, StandardCharsets.UTF_8));
        assertNenhumTemporarioNoDiretorio(tempDir);
    }

    // ---- 2. Envelope inválido preserva output preexistente -------------------

    @Test
    @DisplayName("2 — envelope inválido preserva um --output preexistente")
    void envelopeInvalido_preservaOutputPreexistente(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("envelope-invalido.json");
        Files.writeString(input, """
                {
                  "periodo": { "inicio": "2026-08-01", "fim": "2026-07-01" },
                  "despesas": []
                }
                """, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

        assertEquals(3, resultado.codigo);
        assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(output, StandardCharsets.UTF_8));
        assertNenhumTemporarioNoDiretorio(tempDir);
    }

    // ---- 3. Falha simulada antes da substituição preserva output -------------

    @Test
    @DisplayName("3 — falha simulada antes da substituição final preserva um --output preexistente")
    void falhaSimuladaAntesDaSubstituicao_preservaOutputPreexistente(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("valido.json");
        Files.writeString(input, jsonEnvelopeValido(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);

        Main.simularFalhaAntesDaSubstituicao = true;
        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

        assertEquals(2, resultado.codigo);
        assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(output, StandardCharsets.UTF_8));
        assertNenhumTemporarioNoDiretorio(tempDir);
    }

    // ---- 4. Sucesso substitui o conteúdo antigo -------------------------------

    @Test
    @DisplayName("4 — sucesso substitui o conteúdo antigo de --output pelo resultado completo")
    void sucesso_substituiConteudoAntigo(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("valido.json");
        Files.writeString(input, jsonEnvelopeValido(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

        assertEquals(0, resultado.codigo);
        String conteudoFinal = Files.readString(output, StandardCharsets.UTF_8);
        assertTrue(!conteudoFinal.equals(CONTEUDO_PREEXISTENTE), "conteúdo antigo deveria ter sido substituído");

        JsonNode raiz = new ObjectMapper().readTree(conteudoFinal);
        assertTrue(raiz.has("total_reembolsavel"), "resultado escrito deve ser o JSON completo, parseável");
        assertNenhumTemporarioNoDiretorio(tempDir);
    }

    // ---- 5. Nenhum temporário permanece após sucesso sem output preexistente -

    @Test
    @DisplayName("5 — sucesso sem --output preexistente não deixa temporário no diretório de destino")
    void sucessoSemOutputPreexistente_semTemporarioRestante(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("valido.json");
        Files.writeString(input, jsonEnvelopeValido(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado-novo.json");

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

        assertEquals(0, resultado.codigo);
        assertTrue(Files.exists(output));
        assertNenhumTemporarioNoDiretorio(tempDir);
    }
}
