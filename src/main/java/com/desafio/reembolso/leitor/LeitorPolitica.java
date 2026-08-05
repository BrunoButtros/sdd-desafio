package com.desafio.reembolso.leitor;

import com.desafio.reembolso.modelo.Periodicidade;
import com.desafio.reembolso.modelo.PoliticaExterna;
import com.desafio.reembolso.modelo.TabelaCategoria;
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
import java.util.regex.Pattern;

/**
 * Lê e valida integralmente o arquivo de política externa (spec 4.1.1,
 * RN-021, RN-022, plan §5), usando a mesma técnica de árvore {@code JsonNode}
 * de {@link ValidadorEnvelope} (DT-004/DT-005). API pública única e fechada:
 * {@link #ler(Path)}. Todo o documento é validado — os dezesseis pontos de
 * plan §5 — antes de qualquer {@code TabelaCategoria} ser construída; não
 * existe caminho pelo qual uma política parcialmente válida alcance o núcleo.
 */
public final class LeitorPolitica {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final Pattern PADRAO_DATA = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    private LeitorPolitica() {
    }

    public static PoliticaExterna ler(Path caminho) {
        JsonNode raiz;
        try {
            raiz = MAPPER.readTree(caminho.toFile());
        } catch (IOException e) {
            throw new PoliticaInvalidaException(
                    "Não foi possível ler o arquivo de política: " + caminho, e);
        }

        if (raiz == null || raiz.getNodeType() != JsonNodeType.OBJECT) {
            throw new PoliticaInvalidaException("A raiz do documento de política deve ser um objeto JSON");
        }

        LocalDate vigencia = validarVigencia(raiz.get("vigencia"));
        validarMoedaBase(raiz.get("moeda_base"));
        BigDecimal notaFiscalObrigatoriaAcimaDe =
                validarNotaFiscalObrigatoriaAcimaDe(raiz.get("nota_fiscal_obrigatoria_acima_de"));

        JsonNode padraoNode = raiz.get("padrao");
        if (padraoNode == null || padraoNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new PoliticaInvalidaException("\"padrao\" está ausente ou não é um objeto");
        }

        JsonNode centrosCustoNode = raiz.get("centros_custo");
        if (centrosCustoNode == null || centrosCustoNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new PoliticaInvalidaException("\"centros_custo\" está ausente ou não é um objeto");
        }

        // Fase 1: valida o documento inteiro (padrao + todas as tabelas de
        // centros_custo) antes de construir qualquer TabelaCategoria.
        validarTabelaCategorias(padraoNode, true, "padrao");
        for (Iterator<Map.Entry<String, JsonNode>> it = centrosCustoNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entrada = it.next();
            JsonNode tabelaNode = entrada.getValue();
            if (tabelaNode == null || tabelaNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new PoliticaInvalidaException(
                        "A tabela do centro de custo \"" + entrada.getKey() + "\" não é um objeto");
            }
            validarTabelaCategorias(tabelaNode, false, "centros_custo." + entrada.getKey());
        }

        // Fase 2: documento inteiro validado — agora sim constrói o modelo.
        Map<String, TabelaCategoria> padrao = construirTabelaCategorias(padraoNode);
        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = centrosCustoNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entrada = it.next();
            centrosCusto.put(entrada.getKey(), construirTabelaCategorias(entrada.getValue()));
        }

        return new PoliticaExterna(vigencia, "BRL", notaFiscalObrigatoriaAcimaDe, padrao, centrosCusto);
    }

    private static void validarTabelaCategorias(JsonNode tabelaNode, boolean exigeLimitePositivo, String contexto) {
        for (Iterator<Map.Entry<String, JsonNode>> it = tabelaNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entrada = it.next();
            String categoria = entrada.getKey();
            if (categoria.isEmpty()) {
                throw new PoliticaInvalidaException("Nome de categoria vazio em \"" + contexto + "\"");
            }
            JsonNode configuracao = entrada.getValue();
            if (configuracao == null || configuracao.getNodeType() != JsonNodeType.OBJECT) {
                throw new PoliticaInvalidaException(
                        "A configuração da categoria \"" + categoria + "\" em \"" + contexto + "\" não é um objeto");
            }
            validarConfiguracaoCategoria(configuracao, exigeLimitePositivo, contexto + "." + categoria);
        }
    }

    private static void validarConfiguracaoCategoria(JsonNode configuracao, boolean exigeLimitePositivo, String contexto) {
        JsonNode limiteNode = configuracao.get("limite");
        if (limiteNode == null || limiteNode.isNull() || !limiteNode.isNumber()) {
            throw new PoliticaInvalidaException("\"limite\" ausente ou não numérico em \"" + contexto + "\"");
        }
        BigDecimal limite = limiteNode.decimalValue();
        if (exigeLimitePositivo) {
            if (limite.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PoliticaInvalidaException(
                        "\"limite\" deve ser estritamente maior que zero em \"" + contexto + "\"");
            }
        } else if (limite.compareTo(BigDecimal.ZERO) < 0) {
            throw new PoliticaInvalidaException("\"limite\" não pode ser negativo em \"" + contexto + "\"");
        }

        JsonNode periodicidadeNode = configuracao.get("periodicidade");
        if (periodicidadeNode == null || periodicidadeNode.isNull()
                || periodicidadeNode.getNodeType() != JsonNodeType.STRING) {
            throw new PoliticaInvalidaException("\"periodicidade\" ausente ou não é texto em \"" + contexto + "\"");
        }
        String periodicidadeTexto = periodicidadeNode.asText();
        if (!"dia".equals(periodicidadeTexto) && !"diaria".equals(periodicidadeTexto)) {
            throw new PoliticaInvalidaException(
                    "\"periodicidade\" deve ser exatamente \"dia\" ou \"diaria\" em \"" + contexto + "\"");
        }

        JsonNode observacaoNode = configuracao.get("observacao");
        if (observacaoNode != null) {
            if (observacaoNode.isNull()) {
                throw new PoliticaInvalidaException("\"observacao\" não pode ser nula em \"" + contexto + "\"");
            }
            if (observacaoNode.getNodeType() != JsonNodeType.STRING) {
                throw new PoliticaInvalidaException("\"observacao\" deve ser texto em \"" + contexto + "\"");
            }
        }
    }

    private static Map<String, TabelaCategoria> construirTabelaCategorias(JsonNode tabelaNode) {
        Map<String, TabelaCategoria> resultado = new HashMap<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = tabelaNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entrada = it.next();
            JsonNode configuracao = entrada.getValue();
            BigDecimal limite = configuracao.get("limite").decimalValue();
            Periodicidade periodicidade = "dia".equals(configuracao.get("periodicidade").asText())
                    ? Periodicidade.DIA
                    : Periodicidade.DIARIA;
            resultado.put(entrada.getKey(), new TabelaCategoria(limite, periodicidade));
        }
        return resultado;
    }

    private static void validarMoedaBase(JsonNode moedaBaseNode) {
        if (moedaBaseNode == null || moedaBaseNode.isNull() || moedaBaseNode.getNodeType() != JsonNodeType.STRING
                || !"BRL".equals(moedaBaseNode.asText())) {
            throw new PoliticaInvalidaException("\"moeda_base\" deve ser exatamente \"BRL\"");
        }
    }

    private static BigDecimal validarNotaFiscalObrigatoriaAcimaDe(JsonNode node) {
        if (node == null || node.isNull() || !node.isNumber()) {
            throw new PoliticaInvalidaException(
                    "\"nota_fiscal_obrigatoria_acima_de\" está ausente ou não é numérico");
        }
        BigDecimal valor = node.decimalValue();
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new PoliticaInvalidaException("\"nota_fiscal_obrigatoria_acima_de\" não pode ser negativo");
        }
        return valor;
    }

    private static LocalDate validarVigencia(JsonNode campo) {
        if (campo == null || campo.isNull() || campo.getNodeType() != JsonNodeType.STRING) {
            throw new PoliticaInvalidaException("\"vigencia\" está ausente ou não é texto");
        }
        String texto = campo.asText();
        if (!PADRAO_DATA.matcher(texto).matches()) {
            throw new PoliticaInvalidaException("\"vigencia\" não segue o formato AAAA-MM-DD");
        }
        try {
            return LocalDate.parse(texto, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new PoliticaInvalidaException("\"vigencia\" não representa uma data real do calendário");
        }
    }

    /**
     * Arquivo de política inválido — inexistente, ilegível, sintaticamente
     * inválido ou estruturalmente inválido (RN-022). Corresponde ao código
     * de saída {@code 2} da CLI (plan §3), mesma classe de gravidade de
     * argumento ausente/repetido/desconhecido.
     */
    public static final class PoliticaInvalidaException extends RuntimeException {

        public static final int CODIGO_SAIDA = 2;

        public PoliticaInvalidaException(String mensagem) {
            super(mensagem);
        }

        public PoliticaInvalidaException(String mensagem, Throwable causa) {
            super(mensagem, causa);
        }

        public int codigoSaida() {
            return CODIGO_SAIDA;
        }
    }
}
