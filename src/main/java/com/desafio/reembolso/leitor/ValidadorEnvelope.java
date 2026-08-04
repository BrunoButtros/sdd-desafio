package com.desafio.reembolso.leitor;

import com.desafio.reembolso.modelo.Envelope;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

/**
 * Valida o envelope da entrada (spec 4.1, RN-001) a partir da árvore
 * {@code JsonNode} já lida pelo leitor (DT-005), e aplica a tolerância dos
 * metadados opcionais de {@code colaborador} e {@code periodo.competencia}.
 * Não valida a estrutura interna de {@code despesas} — isso é RN-002,
 * implementada a partir de T-005.
 */
public final class ValidadorEnvelope {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final Pattern PADRAO_DATA = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final Pattern PADRAO_COMPETENCIA = Pattern.compile("\\d{4}-\\d{2}");

    private ValidadorEnvelope() {
    }

    /**
     * Lê o arquivo de entrada com o {@code ObjectMapper} de produção
     * (DT-004: {@code USE_BIG_DECIMAL_FOR_FLOATS}; DT-005: leitura por
     * árvore {@code JsonNode}, com o contrato sintático de T-002 preservado
     * via {@code FAIL_ON_TRAILING_TOKENS}) e valida o envelope resultante.
     * Não escreve nem cria arquivo de saída.
     */
    public static Envelope lerEValidar(Path arquivo) throws IOException {
        JsonNode raiz = MAPPER.readTree(arquivo.toFile());
        return validar(raiz);
    }

    public static Envelope validar(JsonNode raiz) {
        if (raiz == null || raiz.getNodeType() != JsonNodeType.OBJECT) {
            throw new EnvelopeInvalidoException("A raiz do documento deve ser um objeto JSON");
        }

        JsonNode periodo = raiz.get("periodo");
        if (periodo == null || periodo.getNodeType() != JsonNodeType.OBJECT) {
            throw new EnvelopeInvalidoException("\"periodo\" está ausente ou não é um objeto");
        }

        LocalDate inicio = validarData(periodo.get("inicio"), "periodo.inicio");
        LocalDate fim = validarData(periodo.get("fim"), "periodo.fim");
        if (inicio.isAfter(fim)) {
            throw new EnvelopeInvalidoException("\"periodo.inicio\" é posterior a \"periodo.fim\"");
        }

        JsonNode despesas = raiz.get("despesas");
        if (despesas == null || despesas.isNull() || !despesas.isArray()) {
            throw new EnvelopeInvalidoException("\"despesas\" está ausente, é nulo ou não é uma lista");
        }

        String competencia = extrairTextoOuNulo(periodo.get("competencia"));
        if (competencia != null && !PADRAO_COMPETENCIA.matcher(competencia).matches()) {
            competencia = null;
        }

        JsonNode colaborador = raiz.get("colaborador");
        String colaboradorId = null;
        String colaboradorNome = null;
        String colaboradorCentroCusto = null;
        if (colaborador != null && colaborador.getNodeType() == JsonNodeType.OBJECT) {
            colaboradorId = extrairTextoOuNulo(colaborador.get("id"));
            colaboradorNome = extrairTextoOuNulo(colaborador.get("nome"));
            colaboradorCentroCusto = extrairTextoOuNulo(colaborador.get("centro_custo"));
        }

        return new Envelope(colaboradorId, colaboradorNome, colaboradorCentroCusto,
                competencia, inicio, fim, despesas);
    }

    private static LocalDate validarData(JsonNode campo, String nomeCampo) {
        if (campo == null || campo.isNull() || campo.getNodeType() != JsonNodeType.STRING) {
            throw new EnvelopeInvalidoException("\"" + nomeCampo + "\" está ausente ou não é texto");
        }
        String texto = campo.asText();
        if (!PADRAO_DATA.matcher(texto).matches()) {
            throw new EnvelopeInvalidoException("\"" + nomeCampo + "\" não segue o formato AAAA-MM-DD");
        }
        try {
            return LocalDate.parse(texto, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new EnvelopeInvalidoException("\"" + nomeCampo + "\" não representa uma data real do calendário");
        }
    }

    private static String extrairTextoOuNulo(JsonNode campo) {
        if (campo == null || campo.isNull() || campo.getNodeType() != JsonNodeType.STRING) {
            return null;
        }
        return campo.asText();
    }

    /**
     * Erro de envelope inválido (RN-001). Corresponde ao código de saída
     * {@code 3} da CLI (plan §3, DT-003) — o mapeamento efetivo para o exit
     * code da JVM é responsabilidade do CLI (T-019); esta exceção apenas o
     * representa no domínio.
     */
    public static final class EnvelopeInvalidoException extends RuntimeException {

        public static final int CODIGO_SAIDA = 3;

        public EnvelopeInvalidoException(String mensagem) {
            super(mensagem);
        }

        public int codigoSaida() {
            return CODIGO_SAIDA;
        }
    }
}
