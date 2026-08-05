package com.desafio.reembolso;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre o contrato de CLI (T-002, T-019, T-034) e DT-003/DT-018 — os três
 * códigos de saída (0, 2 e 3) e o parser de quatro flags obrigatórias num
 * único comando {@code mvn test}. Não atribui RN, salvo DT-018/CA-041/CA-042
 * (contrato de execução).
 */
@DisplayName("Contrato de CLI — T-002 / T-019 / T-034 / DT-003 / DT-018")
class CliContratoTest {

    private static final String POLITICA = Path.of("exemplos", "envelope", "politica-v4.json").toString();
    private static final String CAMBIO = Path.of("exemplos", "envelope", "cambio.json").toString();

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

    private Resultado executar(String... args) {
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

    // ---- Contrato de quatro flags — sucesso -----------------------------

    @Test
    @DisplayName("sucesso com as quatro flags em ordem arbitrária retorna exit 0")
    void sucesso_quatroFlagsOrdemArbitraria(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, """
                {
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": []
                }
                """, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--cambio", CAMBIO,
                "--input", input.toString(),
                "--politica", POLITICA,
                "--output", output.toString());

        assertEquals(0, resultado.codigo);
        assertEquals("", resultado.stderr);
        assertEquals("", resultado.stdout);
        assertTrue(Files.exists(output), "--output deve ser criado em caso de sucesso");

        JsonNode raiz = new ObjectMapper().readTree(output.toFile());
        assertTrue(raiz.has("total_reembolsavel"), "JSON de saída deve ser parseável e conter total_reembolsavel");
        assertEquals(0, new BigDecimal("0.00").compareTo(raiz.get("total_reembolsavel").decimalValue()));
    }

    // ---- Subcomando ------------------------------------------------------

    @Test
    @DisplayName("subcomando ausente (nenhum argumento) retorna exit 2")
    void subcomandoAusente() {
        Resultado resultado = executar();

        assertEquals(2, resultado.codigo);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de uso");
        assertEquals("", resultado.stdout);
    }

    @Test
    @DisplayName("subcomando diferente de 'calcular' retorna exit 2")
    void subcomandoDiferenteDeCalcular(@TempDir Path tempDir) {
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "executar", "--input", "entrada.json", "--output", output.toString(),
                "--politica", POLITICA, "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de uso");
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando o subcomando é inválido");
    }

    // ---- Formato dos tokens após 'calcular' -------------------------------

    @Test
    @DisplayName("token posicional extra depois de 'calcular' retorna exit 2")
    void tokenPosicionalExtra(@TempDir Path tempDir) {
        Path input = tempDir.resolve("entrada.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO,
                "extra");

        assertEquals(2, resultado.codigo);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando há token posicional extra");
    }

    @Test
    @DisplayName("flag na última posição sem valor retorna exit 2")
    void flagUltimaPosicaoSemValor(@TempDir Path tempDir) {
        Path input = tempDir.resolve("entrada.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio");

        assertEquals(2, resultado.codigo);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando uma flag fica sem valor");
    }

    @Test
    @DisplayName("quantidade ímpar de tokens depois de 'calcular' retorna exit 2")
    void quantidadeImparDeTokens(@TempDir Path tempDir) {
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar("calcular", "--input", "entrada.json", "--output");

        assertEquals(2, resultado.codigo);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando a quantidade de tokens é ímpar");
    }

    @Test
    @DisplayName("flag repetida retorna exit 2")
    void flagRepetida(@TempDir Path tempDir) {
        Path input = tempDir.resolve("entrada.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO,
                "--input", "outra-entrada.json");

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("repetido"),
                "stderr deveria indicar flag repetida, mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando uma flag é repetida");
    }

    @Test
    @DisplayName("flag desconhecida retorna exit 2")
    void flagDesconhecida(@TempDir Path tempDir) {
        Path input = tempDir.resolve("entrada.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO,
                "--extra", "valor");

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("desconhecido"),
                "stderr deveria indicar flag desconhecida, mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando há flag desconhecida");
    }

    // ---- Ausência isolada de cada flag obrigatória ------------------------

    @Test
    @DisplayName("ausência isolada de --input retorna exit 2")
    void ausenciaIsoladaDeInput(@TempDir Path tempDir) {
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("--input"),
                "stderr deveria indicar ausência de --input, mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando --input está ausente");
    }

    @Test
    @DisplayName("ausência isolada de --output retorna exit 2")
    void ausenciaIsoladaDeOutput(@TempDir Path tempDir) {
        Path input = tempDir.resolve("entrada.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("--output"),
                "stderr deveria indicar ausência de --output, mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando --output está ausente");
    }

    @Test
    @DisplayName("ausência isolada de --politica retorna exit 2")
    void ausenciaIsoladaDePolitica(@TempDir Path tempDir) {
        Path input = tempDir.resolve("entrada.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("--politica"),
                "stderr deveria indicar ausência de --politica, mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando --politica está ausente");
    }

    @Test
    @DisplayName("ausência isolada de --cambio retorna exit 2")
    void ausenciaIsoladaDeCambio(@TempDir Path tempDir) {
        Path input = tempDir.resolve("entrada.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA);

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("--cambio"),
                "stderr deveria indicar ausência de --cambio, mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando --cambio está ausente");
    }

    // ---- Comando antigo (duas flags) — único cenário autorizado a usá-lo -

    @Test
    @DisplayName("comando antigo apenas com --input e --output retorna exit 2 (CA-042)")
    void comandoAntigoApenasInputEOutput(@TempDir Path tempDir) {
        Path input = tempDir.resolve("entrada.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

        assertEquals(2, resultado.codigo);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado pelo comando antigo de duas flags");
    }

    // ---- Camadas posteriores ao parser (migradas para as quatro flags) ---

    @Test
    @DisplayName("arquivo de entrada inexistente retorna exit 2")
    void arquivoDeEntradaInexistente(@TempDir Path tempDir) {
        Path input = tempDir.resolve("nao-existe.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("Arquivo de entrada não encontrado"),
                "stderr deveria conter 'Arquivo de entrada não encontrado', mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando o arquivo de entrada não existe");
    }

    @Test
    @DisplayName("JSON de entrada sintaticamente inválido retorna exit 2")
    void jsonSintaticamenteInvalido(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("invalido.json");
        Files.writeString(input, "{ \"despesas\": [ ", StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("JSON de entrada sintaticamente inválido"),
                "stderr deveria conter 'JSON de entrada sintaticamente inválido', mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando o JSON de entrada é sintaticamente inválido");
    }

    @Test
    @DisplayName("arquivo de entrada vazio ou só com whitespace retorna exit 2")
    void arquivoDeEntradaVazio(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("vazio.json");
        Files.writeString(input, "   \n\t  ", StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("JSON de entrada sintaticamente inválido"),
                "stderr deveria conter 'JSON de entrada sintaticamente inválido', mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando o arquivo de entrada está vazio");
    }

    @Test
    @DisplayName("tokens extras após a raiz JSON retornam exit 2")
    void tokensExtrasAposRaiz(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("tokens-extras.json");
        Files.writeString(input, "{ } { }", StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("JSON de entrada sintaticamente inválido"),
                "stderr deveria conter 'JSON de entrada sintaticamente inválido', mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando há tokens extras após a raiz JSON");
    }

    @Test
    @DisplayName("processamento com sucesso retorna exit 0, escreve o resultado e não escreve em stderr/stdout")
    void sucesso_exit0EArquivoEscrito(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, """
                {
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": []
                }
                """, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", CAMBIO);

        assertEquals(0, resultado.codigo);
        assertEquals("", resultado.stderr);
        assertEquals("", resultado.stdout);
        assertTrue(Files.exists(output), "--output deve ser criado em caso de sucesso");

        JsonNode raiz = new ObjectMapper().readTree(output.toFile());
        assertTrue(raiz.has("total_reembolsavel"), "JSON de saída deve ser parseável e conter total_reembolsavel");
        assertEquals(0, new BigDecimal("0.00").compareTo(raiz.get("total_reembolsavel").decimalValue()));
    }
}
