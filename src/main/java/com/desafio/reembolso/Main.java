package com.desafio.reembolso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {

    private static final String USO = "Uso: java -jar motor-reembolso.jar calcular --input <arquivo> --output <arquivo>";

    private Main() {
    }

    public static void main(String[] args) {
        System.exit(run(args, System.out, System.err));
    }

    static int run(String[] args, PrintStream out, PrintStream err) {
        if (args.length == 0 || !"calcular".equals(args[0])) {
            err.println(USO);
            return 2;
        }

        String inputPath = null;
        String outputPath = null;

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--input":
                    if (i + 1 >= args.length) {
                        err.println("Argumento obrigatório ausente: valor de --input");
                        return 2;
                    }
                    inputPath = args[++i];
                    break;
                case "--output":
                    if (i + 1 >= args.length) {
                        err.println("Argumento obrigatório ausente: valor de --output");
                        return 2;
                    }
                    outputPath = args[++i];
                    break;
                default:
                    err.println("Argumento desconhecido: " + args[i]);
                    return 2;
            }
        }

        if (inputPath == null) {
            err.println("Argumento obrigatório ausente: --input");
            return 2;
        }
        if (outputPath == null) {
            err.println("Argumento obrigatório ausente: --output");
            return 2;
        }

        Path input = Path.of(inputPath);
        if (!Files.isRegularFile(input)) {
            err.println("Arquivo de entrada não encontrado: " + inputPath);
            return 2;
        }

        ObjectMapper mapper = new ObjectMapper()
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        JsonNode raiz;
        try {
            raiz = mapper.readTree(input.toFile());
        } catch (JsonProcessingException e) {
            err.println("JSON de entrada sintaticamente inválido: " + e.getOriginalMessage());
            return 2;
        } catch (IOException e) {
            err.println("Não foi possível ler o arquivo de entrada: " + e.getMessage());
            return 2;
        }

        if (raiz == null || raiz.isMissingNode()) {
            err.println("JSON de entrada sintaticamente inválido: arquivo vazio ou sem conteúdo JSON");
            return 2;
        }

        return 0;
    }
}
