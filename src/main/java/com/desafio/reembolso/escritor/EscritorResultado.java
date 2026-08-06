package com.desafio.reembolso.escritor;

import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.pipeline.CompositorSaida.ResultadoItem;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Materializa o documento JSON completo de saída (spec 4.3) a partir do
 * {@link Envelope} já validado, da lista final de {@link ResultadoItem} já
 * composta e ordenada (T-016) e do total já calculado (T-017). Não executa
 * o pipeline, não valida despesas, não normaliza valores, não avalia regras
 * e não recalcula o total — apenas serializa o que recebe.
 */
public final class EscritorResultado {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);

    private EscritorResultado() {
    }

    public static String serializar(
            Envelope envelope,
            List<ResultadoItem> resultados,
            BigDecimal totalReembolsavel
    ) {
        Objects.requireNonNull(envelope, "envelope");
        Objects.requireNonNull(resultados, "resultados");
        Objects.requireNonNull(totalReembolsavel, "totalReembolsavel");

        ObjectNode raiz = MAPPER.createObjectNode();
        raiz.set("colaborador", colaborador(envelope));
        raiz.set("periodo", periodo(envelope));
        raiz.set("resultados", resultados(resultados));
        raiz.put("total_reembolsavel", monetario(totalReembolsavel, "total_reembolsavel"));

        try {
            return MAPPER.writeValueAsString(raiz);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("falha inesperada ao serializar o resultado", e);
        }
    }

    private static ObjectNode colaborador(Envelope envelope) {
        ObjectNode no = MAPPER.createObjectNode();
        no.put("id", envelope.getColaboradorId());
        no.put("nome", envelope.getColaboradorNome());
        no.put("centro_custo", envelope.getColaboradorCentroCusto());
        return no;
    }

    private static ObjectNode periodo(Envelope envelope) {
        ObjectNode no = MAPPER.createObjectNode();
        no.put("competencia", envelope.getPeriodoCompetencia());
        no.put("inicio", envelope.getPeriodoInicio().toString());
        no.put("fim", envelope.getPeriodoFim().toString());
        return no;
    }

    private static ArrayNode resultados(List<ResultadoItem> resultados) {
        ArrayNode lista = MAPPER.createArrayNode();
        for (ResultadoItem resultado : resultados) {
            Objects.requireNonNull(resultado, "resultado");
            lista.add(registro(resultado));
        }
        return lista;
    }

    private static ObjectNode registro(ResultadoItem resultado) {
        BigDecimal valorNormalizado = resultado.valorNormalizado();
        JsonNode valorInformado = resultado.valorInformado();
        BigDecimal taxaCambioAplicada = resultado.taxaCambioAplicada();
        LocalDate dataCotacaoUtilizada = resultado.dataCotacaoUtilizada();

        ObjectNode no = MAPPER.createObjectNode();
        no.put("indice_entrada", resultado.indiceEntrada());
        no.put("id", resultado.id());
        no.set("valor_informado", valorInformado == null ? null : valorInformado.deepCopy());
        no.put("moeda", resultado.moeda());
        no.set("taxa_cambio_aplicada",
                taxaCambioAplicada == null ? null : MAPPER.getNodeFactory().numberNode(taxaCambioAplicada));
        no.put("data_cotacao_utilizada",
                dataCotacaoUtilizada == null ? null : dataCotacaoUtilizada.toString());
        no.put("valor_normalizado",
                valorNormalizado == null ? null : monetario(valorNormalizado, "valor_normalizado"));
        no.put("valor_reembolsavel", monetario(resultado.valorReembolsavel(), "valor_reembolsavel"));
        no.put("decisao", resultado.decisao().textoCanonico());
        no.set("motivos", motivos(resultado.motivos()));
        return no;
    }

    private static ArrayNode motivos(List<Motivo> motivos) {
        ArrayNode lista = MAPPER.createArrayNode();
        for (Motivo motivo : motivos) {
            Objects.requireNonNull(motivo, "motivo");
            ObjectNode no = MAPPER.createObjectNode();
            no.put("codigo", motivo.codigo().textoCanonico());
            no.put("regra", motivo.regra().textoCanonico());
            no.put("campo", motivo.campo() == null ? null : motivo.campo().textoCanonico());
            lista.add(no);
        }
        return lista;
    }

    /**
     * Prepara a representação de um campo monetário, sem arredondar: eleva
     * a escala para 2 quando isso não perde informação e rejeita, em vez de
     * corrigir silenciosamente, um valor que exigiria arredondamento.
     */
    private static BigDecimal monetario(BigDecimal valor, String nomeCampo) {
        Objects.requireNonNull(valor, nomeCampo);

        try {
            return valor.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(
                    nomeCampo + " não pode ser representado com exatamente duas casas sem arredondamento",
                    e);
        }
    }
}
