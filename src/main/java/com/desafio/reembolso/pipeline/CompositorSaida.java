package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Passo 10 da ordem canônica (spec 8.1): produz exatamente um registro final
 * por posição original da entrada, na ordem da entrada, combinando os
 * estados já calculados pelas etapas anteriores do pipeline (RN-017,
 * CA-002). Não reexecuta nenhuma validação, normalização ou regra de
 * negócio — apenas localiza, por {@code indiceEntrada}, o estado final de
 * cada posição entre {@code avaliados}, {@code aposDuplicidade} e os dois
 * agregadores de teto, e ordena os motivos apresentados conforme 8.3.
 */
public final class CompositorSaida {

    private static final Map<MotivoCodigo, Integer> ESTAGIO_POR_CODIGO = criarEstagios();
    private static final Map<CampoCanonico, Integer> ORDEM_CAMPO = criarOrdemCampo();

    private CompositorSaida() {
    }

    public static List<ResultadoItem> compor(
            List<ItemAvaliado> avaliados,
            List<ItemAvaliado> aposDuplicidade,
            List<ResultadoTeto> resultadosTetoDiario,
            List<ResultadoTeto> resultadosTetoHospedagem
    ) {
        Objects.requireNonNull(avaliados, "avaliados");
        Objects.requireNonNull(aposDuplicidade, "aposDuplicidade");
        Objects.requireNonNull(resultadosTetoDiario, "resultadosTetoDiario");
        Objects.requireNonNull(resultadosTetoHospedagem, "resultadosTetoHospedagem");

        Map<Integer, ItemAvaliado> mapaAvaliados = mapaAvaliadosPorIndice(avaliados, "avaliados");
        Map<Integer, ItemAvaliado> mapaAposDuplicidade = mapaAvaliadosPorIndice(aposDuplicidade, "aposDuplicidade");
        Map<Integer, ResultadoTeto> mapaTetoDiario = mapaTetoPorIndice(resultadosTetoDiario, "resultadosTetoDiario");
        Map<Integer, ResultadoTeto> mapaTetoHospedagem = mapaTetoPorIndice(resultadosTetoHospedagem, "resultadosTetoHospedagem");

        for (Integer indice : mapaTetoDiario.keySet()) {
            if (mapaTetoHospedagem.containsKey(indice)) {
                throw new IllegalArgumentException(
                        "índice " + indice + " presente em resultadosTetoDiario e resultadosTetoHospedagem simultaneamente");
            }
        }

        for (ItemAvaliado item : avaliados) {
            if (!item.elegivel()) {
                continue;
            }
            int indice = item.itemNormalizado().item().getIndiceEntrada();
            if (!mapaAposDuplicidade.containsKey(indice)) {
                throw new IllegalArgumentException(
                        "item elegível (índice " + indice + ") em avaliados sem correspondente em aposDuplicidade");
            }
        }

        for (Integer indice : mapaAposDuplicidade.keySet()) {
            validarAposDuplicidadeCorrespondeAItemElegivelEmAvaliados(indice, mapaAvaliados);
        }

        for (ItemAvaliado item : aposDuplicidade) {
            if (!item.elegivel()) {
                continue;
            }
            int indice = item.itemNormalizado().item().getIndiceEntrada();
            boolean temResultadoDeTeto = mapaTetoDiario.containsKey(indice) || mapaTetoHospedagem.containsKey(indice);
            if (!temResultadoDeTeto) {
                throw new IllegalArgumentException(
                        "item elegível após duplicidade (índice " + indice + ") sem resultado de teto correspondente");
            }
        }

        validarResultadosTetoCorrespondemAItemElegivel(mapaTetoDiario, mapaAposDuplicidade, "resultadosTetoDiario");
        validarResultadosTetoCorrespondemAItemElegivel(mapaTetoHospedagem, mapaAposDuplicidade, "resultadosTetoHospedagem");

        List<ItemAvaliado> ordenados = new ArrayList<>(avaliados);
        ordenados.sort(Comparator.comparingInt(item -> item.itemNormalizado().item().getIndiceEntrada()));

        List<ResultadoItem> resultado = new ArrayList<>(ordenados.size());
        for (ItemAvaliado item : ordenados) {
            resultado.add(componentesDoRegistro(item, mapaAposDuplicidade, mapaTetoDiario, mapaTetoHospedagem));
        }

        if (resultado.size() != avaliados.size()) {
            throw new IllegalArgumentException(
                    "quantidade final (" + resultado.size() + ") difere da quantidade de avaliados (" + avaliados.size() + ")");
        }

        return List.copyOf(resultado);
    }

    private static ResultadoItem componentesDoRegistro(
            ItemAvaliado item,
            Map<Integer, ItemAvaliado> mapaAposDuplicidade,
            Map<Integer, ResultadoTeto> mapaTetoDiario,
            Map<Integer, ResultadoTeto> mapaTetoHospedagem
    ) {
        ItemValidado itemValidado = item.itemNormalizado().item();
        int indiceEntrada = itemValidado.getIndiceEntrada();
        String id = itemValidado.getId();
        JsonNode valorInformado = itemValidado.getValorInformado();
        BigDecimal valorNormalizado = item.itemNormalizado().valorNormalizado();

        if (!item.elegivel()) {
            return new ResultadoItem(indiceEntrada, id, valorInformado, valorNormalizado,
                    item.valorReembolsavel(), Decisao.RECUSADO, ordenarMotivos(item.motivos()));
        }

        ItemAvaliado itemAposDuplicidade = mapaAposDuplicidade.get(indiceEntrada);
        if (!itemAposDuplicidade.elegivel()) {
            return new ResultadoItem(indiceEntrada, id, valorInformado, valorNormalizado,
                    itemAposDuplicidade.valorReembolsavel(), Decisao.RECUSADO, ordenarMotivos(itemAposDuplicidade.motivos()));
        }

        ResultadoTeto resultadoTeto = mapaTetoDiario.get(indiceEntrada);
        if (resultadoTeto == null) {
            resultadoTeto = mapaTetoHospedagem.get(indiceEntrada);
        }

        return new ResultadoItem(indiceEntrada, id, valorInformado, valorNormalizado,
                resultadoTeto.valorReembolsavel(), resultadoTeto.decisao(), ordenarMotivos(resultadoTeto.motivos()));
    }

    private static Map<Integer, ItemAvaliado> mapaAvaliadosPorIndice(List<ItemAvaliado> lista, String nomeLista) {
        Map<Integer, ItemAvaliado> mapa = new HashMap<>();
        for (ItemAvaliado item : lista) {
            int indice = item.itemNormalizado().item().getIndiceEntrada();
            ItemAvaliado anterior = mapa.putIfAbsent(indice, item);
            if (anterior != null) {
                throw new IllegalArgumentException("índice " + indice + " duplicado em " + nomeLista);
            }
        }
        return mapa;
    }

    private static Map<Integer, ResultadoTeto> mapaTetoPorIndice(List<ResultadoTeto> lista, String nomeLista) {
        Map<Integer, ResultadoTeto> mapa = new HashMap<>();
        for (ResultadoTeto resultado : lista) {
            int indice = resultado.itemAvaliado().itemNormalizado().item().getIndiceEntrada();
            ResultadoTeto anterior = mapa.putIfAbsent(indice, resultado);
            if (anterior != null) {
                throw new IllegalArgumentException("índice " + indice + " duplicado em " + nomeLista);
            }
        }
        return mapa;
    }

    private static void validarResultadosTetoCorrespondemAItemElegivel(
            Map<Integer, ResultadoTeto> mapaTeto, Map<Integer, ItemAvaliado> mapaAposDuplicidade, String nomeLista) {
        for (Integer indice : mapaTeto.keySet()) {
            ItemAvaliado item = mapaAposDuplicidade.get(indice);
            if (item == null || !item.elegivel()) {
                throw new IllegalArgumentException(
                        "resultado de teto em " + nomeLista + " (índice " + indice
                                + ") não corresponde a item elegível pós-duplicidade");
            }
        }
    }

    /**
     * Valida a direção inversa de {@code aposDuplicidade -> avaliados}: só
     * itens aprovados nas regras individuais entram em {@code
     * aposDuplicidade} (spec 8.1, passos 6 e 7), então todo índice ali
     * presente precisa corresponder a um item elegível em {@code avaliados}
     * — nunca a um índice ausente nem a um item inelegível. Sem essa
     * checagem, um item excedente em {@code aposDuplicidade} passaria pelas
     * demais validações e seria descartado silenciosamente na composição,
     * porque a lista final é construída iterando somente {@code avaliados}.
     */
    private static void validarAposDuplicidadeCorrespondeAItemElegivelEmAvaliados(
            int indice, Map<Integer, ItemAvaliado> mapaAvaliados) {
        ItemAvaliado original = mapaAvaliados.get(indice);

        if (original == null) {
            throw new IllegalArgumentException(
                    "item em aposDuplicidade (índice " + indice + ") sem correspondente em avaliados");
        }

        if (!original.elegivel()) {
            throw new IllegalArgumentException(
                    "item em aposDuplicidade (índice " + indice + ") corresponde a item inelegível em avaliados");
        }
    }

    /**
     * Cópia ordenada conforme 8.3 — nunca altera a lista recebida. A ordem
     * de detecção (a ordem em que os motivos foram acumulados pelo
     * pipeline) é irrelevante aqui: a apresentação depende exclusivamente
     * da tabela de precedência explícita abaixo.
     */
    private static List<Motivo> ordenarMotivos(List<Motivo> motivos) {
        List<Motivo> copia = new ArrayList<>(motivos);
        copia.sort(Comparator
                .comparingInt((Motivo motivo) -> estagioDe(motivo.codigo()))
                .thenComparingInt(motivo -> ordemCampo(motivo.campo())));
        return List.copyOf(copia);
    }

    private static int estagioDe(MotivoCodigo codigo) {
        Integer estagio = ESTAGIO_POR_CODIGO.get(codigo);
        if (estagio == null) {
            throw new IllegalArgumentException("Código de motivo fora do vocabulário fechado de precedência: " + codigo);
        }
        return estagio;
    }

    private static int ordemCampo(CampoCanonico campo) {
        if (campo == null) {
            return -1;
        }
        Integer ordem = ORDEM_CAMPO.get(campo);
        if (ordem == null) {
            throw new IllegalArgumentException("Campo canônico fora do vocabulário fechado de precedência: " + campo);
        }
        return ordem;
    }

    /**
     * Tabela explícita de precedência de apresentação (spec 8.3) — nunca
     * {@code enum.ordinal()}. Estágio 0: {@code ITEM_TIPO_INVALIDO}
     * (sempre único). Estágio 1: os três motivos estruturais, desempatados
     * por {@link #ORDEM_CAMPO}. Estágios 2 a 9: a sequência fixa de
     * {@code ID_DUPLICADO} a {@code DUPLICIDADE}, incluindo os dois motivos
     * de política v4 ({@code MOEDA_SEM_COTACAO} no estágio 3 e
     * {@code CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO} no estágio 6).
     * Estágio 10: os motivos de limitação de teto, que na prática aparecem
     * sozinhos.
     */
    private static Map<MotivoCodigo, Integer> criarEstagios() {
        Map<MotivoCodigo, Integer> mapa = new EnumMap<>(MotivoCodigo.class);
        mapa.put(MotivoCodigo.ITEM_TIPO_INVALIDO, 0);
        mapa.put(MotivoCodigo.CAMPO_AUSENTE, 1);
        mapa.put(MotivoCodigo.CAMPO_TIPO_INVALIDO, 1);
        mapa.put(MotivoCodigo.CAMPO_FORMATO_INVALIDO, 1);
        mapa.put(MotivoCodigo.ID_DUPLICADO, 2);
        mapa.put(MotivoCodigo.MOEDA_SEM_COTACAO, 3);
        mapa.put(MotivoCodigo.VALOR_NAO_POSITIVO, 4);
        mapa.put(MotivoCodigo.CATEGORIA_FORA_POLITICA, 5);
        mapa.put(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, 6);
        mapa.put(MotivoCodigo.FORA_COMPETENCIA, 7);
        mapa.put(MotivoCodigo.NOTA_FISCAL_AUSENTE, 8);
        mapa.put(MotivoCodigo.DUPLICIDADE, 9);
        mapa.put(MotivoCodigo.TETO_DIARIO_APLICADO, 10);
        mapa.put(MotivoCodigo.TETO_DIARIO_ESGOTADO, 10);
        mapa.put(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, 10);
        mapa.put(MotivoCodigo.TETO_INDIVIDUAL_APLICADO, 10);
        return Collections.unmodifiableMap(mapa);
    }

    /** Ordem canônica de contrato (spec 4.2), explícita — nunca alfabética nem por ordinal. */
    private static Map<CampoCanonico, Integer> criarOrdemCampo() {
        Map<CampoCanonico, Integer> mapa = new EnumMap<>(CampoCanonico.class);
        mapa.put(CampoCanonico.ID, 0);
        mapa.put(CampoCanonico.DATA, 1);
        mapa.put(CampoCanonico.CATEGORIA, 2);
        mapa.put(CampoCanonico.DESCRICAO, 3);
        mapa.put(CampoCanonico.FORNECEDOR, 4);
        mapa.put(CampoCanonico.VALOR, 5);
        mapa.put(CampoCanonico.MOEDA, 6);
        mapa.put(CampoCanonico.TEM_NOTA_FISCAL, 7);
        return Collections.unmodifiableMap(mapa);
    }

    /**
     * Registro final por posição de entrada (spec 4.3, RN-017): os campos
     * comuns vêm exclusivamente da cadeia {@code ItemAvaliado ->
     * itemNormalizado() -> item()} já calculada pelas etapas anteriores —
     * nada aqui é recalculado a partir do JSON original.
     */
    public record ResultadoItem(
            int indiceEntrada,
            String id,
            JsonNode valorInformado,
            BigDecimal valorNormalizado,
            BigDecimal valorReembolsavel,
            Decisao decisao,
            List<Motivo> motivos
    ) {
        public ResultadoItem {
            if (indiceEntrada < 1) {
                throw new IllegalArgumentException("indiceEntrada deve ser >= 1, recebido: " + indiceEntrada);
            }
            Objects.requireNonNull(valorReembolsavel, "valorReembolsavel");
            Objects.requireNonNull(decisao, "decisao");
            Objects.requireNonNull(motivos, "motivos");
            motivos = List.copyOf(motivos);
        }
    }
}
