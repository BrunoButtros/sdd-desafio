package com.desafio.reembolso.leitor;

import com.desafio.reembolso.modelo.TabelaCambio;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Lê e valida integralmente o arquivo de câmbio externo (spec 4.1.1,
 * RN-020, RN-022, plan §7), usando a mesma técnica de árvore {@code JsonNode}
 * de {@link LeitorPolitica} (DT-004/DT-005). API pública única e fechada:
 * {@link #ler(Path)}. Todo o documento é validado antes de qualquer entrada
 * do mapa invertido ser construída; não existe caminho pelo qual um câmbio
 * parcialmente válido alcance o núcleo.
 */
public final class LeitorCambio {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final Pattern PADRAO_DATA = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final Pattern PADRAO_MOEDA = Pattern.compile("[A-Z]{3}");

    private LeitorCambio() {
    }

    public static TabelaCambio ler(Path caminho) {
        JsonNode raiz;
        try {
            raiz = MAPPER.readTree(caminho.toFile());
        } catch (IOException e) {
            throw new CambioInvalidoException(
                    "Não foi possível ler o arquivo de câmbio: " + caminho, e);
        }

        if (raiz == null || raiz.getNodeType() != JsonNodeType.OBJECT) {
            throw new CambioInvalidoException("A raiz do documento de câmbio deve ser um objeto JSON");
        }

        validarMoedaBase(raiz.get("moeda_base"));
        validarTextoOpcional(raiz.get("fonte"), "fonte");
        validarTextoOpcional(raiz.get("observacao"), "observacao");

        JsonNode taxasNode = raiz.get("taxas");
        if (taxasNode == null || taxasNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new CambioInvalidoException("\"taxas\" está ausente ou não é um objeto");
        }

        // Fase 1: valida o documento inteiro antes de construir qualquer
        // entrada do mapa invertido.
        for (Iterator<Map.Entry<String, JsonNode>> it = taxasNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entradaData = it.next();
            validarData(entradaData.getKey());

            JsonNode cotacoesDaData = entradaData.getValue();
            if (cotacoesDaData == null || cotacoesDaData.getNodeType() != JsonNodeType.OBJECT) {
                throw new CambioInvalidoException(
                        "O valor associado à data \"" + entradaData.getKey() + "\" não é um objeto");
            }

            for (Iterator<Map.Entry<String, JsonNode>> itMoeda = cotacoesDaData.fields(); itMoeda.hasNext(); ) {
                Map.Entry<String, JsonNode> entradaMoeda = itMoeda.next();
                validarMoeda(entradaMoeda.getKey());
                validarTaxa(entradaMoeda.getValue(), entradaData.getKey(), entradaMoeda.getKey());
            }
        }

        // Fase 2: documento inteiro validado — agora inverte
        // data → moeda → taxa para moeda → NavigableMap<data, taxa>.
        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = taxasNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entradaData = it.next();
            LocalDate data = LocalDate.parse(entradaData.getKey(), FORMATO_DATA);

            for (Iterator<Map.Entry<String, JsonNode>> itMoeda = entradaData.getValue().fields(); itMoeda.hasNext(); ) {
                Map.Entry<String, JsonNode> entradaMoeda = itMoeda.next();
                BigDecimal taxa = entradaMoeda.getValue().decimalValue();
                cotacoesPorMoeda
                        .computeIfAbsent(entradaMoeda.getKey(), chave -> new TreeMap<>())
                        .put(data, taxa);
            }
        }

        return new TabelaCambio("BRL", cotacoesPorMoeda);
    }

    private static void validarMoedaBase(JsonNode moedaBaseNode) {
        if (moedaBaseNode == null || moedaBaseNode.isNull() || moedaBaseNode.getNodeType() != JsonNodeType.STRING
                || !"BRL".equals(moedaBaseNode.asText())) {
            throw new CambioInvalidoException("\"moeda_base\" deve ser exatamente \"BRL\"");
        }
    }

    private static void validarTextoOpcional(JsonNode node, String nomeCampo) {
        if (node == null) {
            return;
        }
        if (node.isNull() || node.getNodeType() != JsonNodeType.STRING) {
            throw new CambioInvalidoException("\"" + nomeCampo + "\" deve ser texto quando presente");
        }
    }

    private static void validarData(String chaveData) {
        if (!PADRAO_DATA.matcher(chaveData).matches()) {
            throw new CambioInvalidoException(
                    "Chave de data \"" + chaveData + "\" em \"taxas\" não segue o formato AAAA-MM-DD");
        }
        try {
            LocalDate.parse(chaveData, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new CambioInvalidoException(
                    "Chave de data \"" + chaveData + "\" em \"taxas\" não representa uma data real do calendário");
        }
    }

    private static void validarMoeda(String chaveMoeda) {
        if (!PADRAO_MOEDA.matcher(chaveMoeda).matches()) {
            throw new CambioInvalidoException(
                    "Chave de moeda \"" + chaveMoeda + "\" não segue o formato [A-Z]{3}");
        }
    }

    private static void validarTaxa(JsonNode taxaNode, String data, String moeda) {
        if (taxaNode == null || taxaNode.isNull() || !taxaNode.isNumber()) {
            throw new CambioInvalidoException(
                    "Taxa de \"" + moeda + "\" em \"" + data + "\" ausente ou não numérica");
        }
        BigDecimal taxa = taxaNode.decimalValue();
        if (taxa.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CambioInvalidoException(
                    "Taxa de \"" + moeda + "\" em \"" + data + "\" deve ser estritamente maior que zero");
        }
    }

    /**
     * Arquivo de câmbio inválido — inexistente, ilegível, sintaticamente
     * inválido ou estruturalmente inválido (RN-022). Corresponde ao código
     * de saída {@code 2} da CLI (plan §3), mesma classe de gravidade de
     * argumento ausente/repetido/desconhecido.
     */
    public static final class CambioInvalidoException extends RuntimeException {

        public static final int CODIGO_SAIDA = 2;

        public CambioInvalidoException(String mensagem) {
            super(mensagem);
        }

        public CambioInvalidoException(String mensagem, Throwable causa) {
            super(mensagem, causa);
        }

        public int codigoSaida() {
            return CODIGO_SAIDA;
        }
    }
}
