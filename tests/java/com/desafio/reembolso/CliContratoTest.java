package com.desafio.reembolso;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    // ---- Validação de --politica antes do envelope (T-035) ---------------

    private static String envelopeValidoInline() {
        return """
                {
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": []
                }
                """;
    }

    private static final String POLITICA_SEM_PADRAO = """
            {
              "vigencia": "2026-07-01",
              "moeda_base": "BRL",
              "nota_fiscal_obrigatoria_acima_de": 100
            }
            """;

    private static final String CAMBIO_SEM_TAXAS = """
            {
              "moeda_base": "BRL"
            }
            """;

    private static final String CONTEUDO_OUTPUT_PREEXISTENTE = "{\"resultado\":\"preexistente\"}";

    private static void escreverOutputPreexistente(Path output) throws IOException {
        Files.writeString(output, CONTEUDO_OUTPUT_PREEXISTENTE, StandardCharsets.UTF_8);
    }

    private static void assertOutputPreexistentePreservado(Path output) throws IOException {
        assertEquals(CONTEUDO_OUTPUT_PREEXISTENTE, Files.readString(output, StandardCharsets.UTF_8),
                "--output preexistente deveria permanecer intacto byte a byte");
    }

    @Test
    @DisplayName("--politica apontando para arquivo inexistente retorna exit 2, mesmo com --input válido")
    void politicaArquivoInexistente(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);
        Path politicaInexistente = tempDir.resolve("politica-nao-existe.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", politicaInexistente.toString(),
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    @Test
    @DisplayName("--politica apontando para diretório (caminho ilegível) retorna exit 2")
    void politicaCaminhoIlegivel(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);
        Path diretorioComoPolitica = tempDir.resolve("diretorio-politica");
        Files.createDirectory(diretorioComoPolitica);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", diretorioComoPolitica.toString(),
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    @Test
    @DisplayName("--politica com JSON sintaticamente inválido retorna exit 2")
    void politicaJsonSintaticamenteInvalido(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);
        Path politicaInvalida = tempDir.resolve("politica-invalida.json");
        Files.writeString(politicaInvalida, "{ \"vigencia\": ", StandardCharsets.UTF_8);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", politicaInvalida.toString(),
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    @Test
    @DisplayName("--politica estruturalmente inválida retorna exit 2")
    void politicaEstruturalmenteInvalida(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);
        Path politicaInvalida = tempDir.resolve("politica-sem-padrao.json");
        Files.writeString(politicaInvalida, POLITICA_SEM_PADRAO, StandardCharsets.UTF_8);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", politicaInvalida.toString(),
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    @Test
    @DisplayName("--politica com texto que causa InvalidPathException retorna exit 2")
    void politicaCausaInvalidPathException(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", "politica\u0000invalida.json",
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    // ---- Validação de --cambio antes do envelope (T-035) ------------------

    @Test
    @DisplayName("--cambio apontando para arquivo inexistente retorna exit 2, mesmo com --input válido")
    void cambioArquivoInexistente(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);
        Path cambioInexistente = tempDir.resolve("cambio-nao-existe.json");

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", cambioInexistente.toString());

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    @Test
    @DisplayName("--cambio apontando para diretório (caminho ilegível) retorna exit 2")
    void cambioCaminhoIlegivel(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);
        Path diretorioComoCambio = tempDir.resolve("diretorio-cambio");
        Files.createDirectory(diretorioComoCambio);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", diretorioComoCambio.toString());

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    @Test
    @DisplayName("--cambio com JSON sintaticamente inválido retorna exit 2")
    void cambioJsonSintaticamenteInvalido(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);
        Path cambioInvalido = tempDir.resolve("cambio-invalido.json");
        Files.writeString(cambioInvalido, "{ \"moeda_base\": ", StandardCharsets.UTF_8);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", cambioInvalido.toString());

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    @Test
    @DisplayName("--cambio estruturalmente inválido retorna exit 2")
    void cambioEstruturalmenteInvalido(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);
        Path cambioInvalido = tempDir.resolve("cambio-sem-taxas.json");
        Files.writeString(cambioInvalido, CAMBIO_SEM_TAXAS, StandardCharsets.UTF_8);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", cambioInvalido.toString());

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    @Test
    @DisplayName("--cambio com texto que causa InvalidPathException retorna exit 2")
    void cambioCausaInvalidPathException(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        escreverOutputPreexistente(output);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", POLITICA,
                "--cambio", "cambio\u0000invalido.json");

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.isBlank(), "stderr deveria conter mensagem de erro");
        assertOutputPreexistentePreservado(output);
    }

    // ---- Ordem de validação: política/câmbio antes do input (T-035) -------

    @Test
    @DisplayName("política é validada antes do input: --input inexistente + --politica inválida prevalece a falha de política")
    void ordemValidacao_politicaAntesDeInput(@TempDir Path tempDir) throws Exception {
        Path inputInexistente = tempDir.resolve("nao-existe.json");
        Path output = tempDir.resolve("resultado.json");
        Path politicaInvalida = tempDir.resolve("politica-sem-padrao.json");
        Files.writeString(politicaInvalida, POLITICA_SEM_PADRAO, StandardCharsets.UTF_8);

        Resultado resultado = executar(
                "calcular",
                "--input", inputInexistente.toString(),
                "--output", output.toString(),
                "--politica", politicaInvalida.toString(),
                "--cambio", CAMBIO);

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertFalse(resultado.stderr.contains("Arquivo de entrada não encontrado"),
                "a falha deveria vir da política, e o input ainda não deveria ter sido consultado, mas stderr foi: "
                        + resultado.stderr);
        assertFalse(Files.exists(output));
    }

    @Test
    @DisplayName("política é validada antes do câmbio: ambos inválidos, prevalece a falha de política")
    void ordemValidacao_politicaAntesDeCambio(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeValidoInline(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        Path politicaInvalida = tempDir.resolve("politica-sem-padrao.json");
        Files.writeString(politicaInvalida, POLITICA_SEM_PADRAO, StandardCharsets.UTF_8);
        Path cambioInvalido = tempDir.resolve("cambio-sem-taxas.json");
        Files.writeString(cambioInvalido, CAMBIO_SEM_TAXAS, StandardCharsets.UTF_8);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", politicaInvalida.toString(),
                "--cambio", cambioInvalido.toString());

        assertEquals(2, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertTrue(resultado.stderr.contains("Política inválida"),
                "a falha deveria vir da política antes do câmbio, mas stderr foi: " + resultado.stderr);
        assertFalse(Files.exists(output));
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

    // ---- Wiring real da política externa no pipeline (T-042) --------------

    private static final String POLITICA_COM_CENTRO_CUSTO = """
            {
              "vigencia": "2026-07-01",
              "moeda_base": "BRL",
              "nota_fiscal_obrigatoria_acima_de": 100.00,
              "padrao": {
                "alimentacao": { "limite": 60.00, "periodicidade": "dia" }
              },
              "centros_custo": {
                "CC-ENG-PLATAFORMA": {
                  "hospedagem": { "limite": 250.00, "periodicidade": "diaria" }
                }
              }
            }
            """;

    private static final String CAMBIO_SEM_TAXAS_VALIDO = """
            {
              "moeda_base": "BRL",
              "taxas": {}
            }
            """;

    private static String envelopeCentroCustoSemAlimentacao() {
        return """
                {
                  "colaborador": { "centro_custo": "CC-ENG-PLATAFORMA" },
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-10", "categoria": "alimentacao",
                      "descricao": "Almoço", "fornecedor": "Restaurante X", "valor": 50.00,
                      "tem_nota_fiscal": true }
                  ]
                }
                """;
    }

    @Test
    @DisplayName("execução real usa a política externa carregada: categoria ausente da tabela do centro de custo "
            + "cadastrado produz CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO/RN-019, nunca CATEGORIA_FORA_POLITICA (T-042)")
    void integracaoReal_politicaExternaUsadaPeloMain_produzMotivoDeCentroCusto(@TempDir Path tempDir) throws Exception {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, envelopeCentroCustoSemAlimentacao(), StandardCharsets.UTF_8);
        Path output = tempDir.resolve("resultado.json");
        Path politica = tempDir.resolve("politica-centro-custo.json");
        Files.writeString(politica, POLITICA_COM_CENTRO_CUSTO, StandardCharsets.UTF_8);
        Path cambio = tempDir.resolve("cambio-sem-taxas.json");
        Files.writeString(cambio, CAMBIO_SEM_TAXAS_VALIDO, StandardCharsets.UTF_8);

        Resultado resultado = executar(
                "calcular",
                "--input", input.toString(),
                "--output", output.toString(),
                "--politica", politica.toString(),
                "--cambio", cambio.toString());

        assertEquals(0, resultado.codigo);
        assertEquals("", resultado.stdout);
        assertEquals("", resultado.stderr);
        assertTrue(Files.exists(output), "--output deve ser criado em caso de sucesso");

        JsonNode raiz = new ObjectMapper().readTree(output.toFile());
        JsonNode resultados = raiz.get("resultados");
        assertEquals(1, resultados.size(), "deve haver exatamente um item no resultado");

        JsonNode item = resultados.get(0);
        assertEquals("RECUSADO", item.get("decisao").asText());
        assertEquals(0, new BigDecimal("0.00").compareTo(item.get("valor_reembolsavel").decimalValue()));

        JsonNode motivos = item.get("motivos");
        assertEquals(1, motivos.size(), "deve conter exatamente um motivo");
        JsonNode motivo = motivos.get(0);
        assertEquals("CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO", motivo.get("codigo").asText());
        assertEquals("RN-019", motivo.get("regra").asText());
        assertTrue(motivo.get("campo").isNull(), "campo deve ser nulo");

        for (JsonNode m : motivos) {
            assertFalse("CATEGORIA_FORA_POLITICA".equals(m.get("codigo").asText()),
                    "não deve conter CATEGORIA_FORA_POLITICA — prova de que a política externa foi usada");
        }
        for (JsonNode m : motivos) {
            String codigo = m.get("codigo").asText();
            assertFalse(codigo.contains("TETO"), "não deve receber motivo de teto: " + codigo);
        }
    }
}
