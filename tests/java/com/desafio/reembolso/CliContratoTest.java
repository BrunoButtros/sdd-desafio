package com.desafio.reembolso;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre o contrato básico de CLI da T-002 e a parte de DT-003 referente ao
 * código de saída 2 (erro de uso ou de infraestrutura). Não atribui RN ou CA,
 * porque nenhum existe para contrato de execução nesta etapa.
 */
@DisplayName("Contrato de CLI — T-002 / DT-003 (parcial, exit 2)")
class CliContratoTest {

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

    @Test
    @DisplayName("argumento obrigatório ausente (--input não informado) retorna exit 2")
    void argumentoObrigatorioAusente(@TempDir Path tempDir) {
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar("calcular", "--output", output.toString());

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("Argumento obrigatório ausente"),
                "stderr deveria conter 'Argumento obrigatório ausente', mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando um argumento obrigatório está ausente");
    }

    @Test
    @DisplayName("arquivo de entrada inexistente retorna exit 2")
    void arquivoDeEntradaInexistente(@TempDir Path tempDir) {
        Path input = tempDir.resolve("nao-existe.json");
        Path output = tempDir.resolve("resultado.json");

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

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

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

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

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

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

        Resultado resultado = executar("calcular", "--input", input.toString(), "--output", output.toString());

        assertEquals(2, resultado.codigo);
        assertTrue(resultado.stderr.contains("JSON de entrada sintaticamente inválido"),
                "stderr deveria conter 'JSON de entrada sintaticamente inválido', mas foi: " + resultado.stderr);
        assertEquals("", resultado.stdout);
        assertFalse(Files.exists(output), "--output não deve ser criado quando há tokens extras após a raiz JSON");
    }
}
