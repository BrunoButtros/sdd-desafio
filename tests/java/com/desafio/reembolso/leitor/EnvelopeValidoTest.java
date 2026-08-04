package com.desafio.reembolso.leitor;

import com.desafio.reembolso.modelo.Envelope;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-001 / CA-020 (spec 4.1): validação do envelope e tolerância dos
 * metadados opcionais de {@code colaborador} e {@code periodo.competencia},
 * usando o {@code ObjectMapper} de produção de {@link ValidadorEnvelope}
 * (DT-004: {@code USE_BIG_DECIMAL_FOR_FLOATS}; DT-005: leitura por árvore
 * {@code JsonNode}).
 */
@DisplayName("Envelope — RN-001 / CA-020")
class EnvelopeValidoTest {

    @Test
    @DisplayName("RN-001 / CA-020 — periodo.inicio posterior a periodo.fim é envelope inválido (exit 3), sem tocar --output")
    void periodoInicioPosteriorAFimEnvelopeInvalido(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, """
                {
                  "periodo": { "inicio": "2026-07-31", "fim": "2026-07-01" },
                  "despesas": []
                }
                """, StandardCharsets.UTF_8);

        Path outputPreexistente = tempDir.resolve("resultado-preexistente.json");
        String conteudoPreexistente = "{\"sentinela\":true}";
        Files.writeString(outputPreexistente, conteudoPreexistente, StandardCharsets.UTF_8);

        Path outputInexistente = tempDir.resolve("resultado-novo.json");

        ValidadorEnvelope.EnvelopeInvalidoException excecao = assertThrows(
                ValidadorEnvelope.EnvelopeInvalidoException.class,
                () -> ValidadorEnvelope.lerEValidar(input));

        assertEquals(3, excecao.codigoSaida(),
                "erro de envelope deve corresponder ao exit code 3 (DT-003), distinto do exit 2");

        assertEquals(conteudoPreexistente, Files.readString(outputPreexistente, StandardCharsets.UTF_8),
                "um --output preexistente não deve ser modificado quando o envelope é inválido");
        assertFalse(Files.exists(outputInexistente),
                "--output não deve ser criado quando o envelope é inválido");
    }

    @Test
    @DisplayName("RN-001 / CA-020 — despesas: [] é envelope válido, com coleção de despesas vazia")
    void despesasVaziaEnvelopeValido(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, """
                {
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": []
                }
                """, StandardCharsets.UTF_8);

        Envelope envelope = ValidadorEnvelope.lerEValidar(input);

        assertEquals(LocalDate.of(2026, 7, 1), envelope.getPeriodoInicio());
        assertEquals(LocalDate.of(2026, 7, 31), envelope.getPeriodoFim());
        assertTrue(envelope.getDespesas().isArray(), "despesas deve ser representado como lista");
        assertEquals(0, envelope.getDespesas().size(), "despesas: [] produz uma coleção vazia");
        // RN-018 (total_reembolsavel = soma dos valor_reembolsavel) só é
        // implementada em T-017 e fechada ponta a ponta em T-020. No nível de
        // modelo desta task, uma coleção de despesas vazia é exatamente a
        // premissa que torna "resultados vazio" e "total 0,00" triviais.
    }

    @Test
    @DisplayName("RN-001 / CA-020 — colaborador recebido como texto mantém o arquivo processável, com os três metadados nulos")
    void colaboradorComoTextoMantemArquivoProcessavelComMetadadosNulos(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, """
                {
                  "colaborador": "c-0417",
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": []
                }
                """, StandardCharsets.UTF_8);

        Envelope envelope = ValidadorEnvelope.lerEValidar(input);

        assertNull(envelope.getColaboradorId(),
                "colaborador.id deve ser nulo quando colaborador não é objeto — sem coerção do texto recebido");
        assertNull(envelope.getColaboradorNome(),
                "colaborador.nome deve ser nulo quando colaborador não é objeto — sem coerção do texto recebido");
        assertNull(envelope.getColaboradorCentroCusto(),
                "colaborador.centro_custo deve ser nulo quando colaborador não é objeto — sem coerção do texto recebido");
    }

    @Test
    @DisplayName("DT-004 — ObjectMapper de produção preserva valor decimal exato (BigDecimal), não double")
    void objectMapperDeProducaoPreservaValorComoBigDecimal(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("entrada.json");
        Files.writeString(input, """
                {
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": [
                    { "valor": 33.333 }
                  ]
                }
                """, StandardCharsets.UTF_8);

        Envelope envelope = ValidadorEnvelope.lerEValidar(input);

        JsonNode valor = envelope.getDespesas().get(0).get("valor");

        assertTrue(valor.isBigDecimal(),
                "USE_BIG_DECIMAL_FOR_FLOATS deve preservar o número como BigDecimal, não como double");
        assertEquals(0, new BigDecimal("33.333").compareTo(valor.decimalValue()),
                "o valor decimal exato deve ser preservado sem perda de precisão binária");
    }
}
