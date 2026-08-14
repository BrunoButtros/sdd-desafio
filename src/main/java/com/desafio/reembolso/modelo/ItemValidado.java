package com.desafio.reembolso.modelo;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Resultado da validação estrutural de um elemento de {@code despesas}
 * (spec 4.2, RN-002): o {@code indiceEntrada} atribuído antes de qualquer
 * validação, os sete campos canônicos já tipados quando estruturalmente
 * válidos (plan §4, "Campos estruturalmente validados" — nulo quando o
 * campo é ausente, de tipo inválido ou de formato inválido, sem coerção e
 * sem valor padrão), o {@code valorInformado} preservado exatamente como
 * recebido (4.3) e os motivos estruturais acumulados, já na ordem canônica
 * de contrato. Nenhum campo tipado aqui é normalizado — isso pertence a
 * T-007 em diante.
 */
public final class ItemValidado {

    private final int indiceEntrada;
    private final String id;
    private final LocalDate data;
    private final String categoria;
    private final String descricao;
    private final String fornecedor;
    private final BigDecimal valor;
    private final Boolean temNotaFiscal;
    private final JsonNode valorInformado;
    private final List<Motivo> motivos;
    private final String moeda;
    private final BigDecimal taxaCambioAplicada;
    private final LocalDate dataCotacaoUtilizada;
    private final BigDecimal valorConvertidoBruto;

    public ItemValidado(int indiceEntrada,
                         String id,
                         LocalDate data,
                         String categoria,
                         String descricao,
                         String fornecedor,
                         BigDecimal valor,
                         Boolean temNotaFiscal,
                         JsonNode valorInformado,
                         List<Motivo> motivos,
                         String moeda,
                         BigDecimal taxaCambioAplicada,
                         LocalDate dataCotacaoUtilizada,
                         BigDecimal valorConvertidoBruto) {
        this.indiceEntrada = indiceEntrada;
        this.id = id;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.fornecedor = fornecedor;
        this.valor = valor;
        this.temNotaFiscal = temNotaFiscal;
        this.valorInformado = valorInformado;
        this.motivos = List.copyOf(Objects.requireNonNull(motivos, "motivos"));
        this.moeda = moeda;
        this.taxaCambioAplicada = taxaCambioAplicada;
        this.dataCotacaoUtilizada = dataCotacaoUtilizada;
        this.valorConvertidoBruto = valorConvertidoBruto;
    }

    public int getIndiceEntrada() {
        return indiceEntrada;
    }

    public String getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Boolean getTemNotaFiscal() {
        return temNotaFiscal;
    }

    public JsonNode getValorInformado() {
        return valorInformado;
    }

    public List<Motivo> getMotivos() {
        return motivos;
    }

    public String getMoeda() {
        return moeda;
    }

    public BigDecimal getTaxaCambioAplicada() {
        return taxaCambioAplicada;
    }

    public LocalDate getDataCotacaoUtilizada() {
        return dataCotacaoUtilizada;
    }

    public BigDecimal getValorConvertidoBruto() {
        return valorConvertidoBruto;
    }

    /**
     * Um motivo estrutural (spec 4.3): código, regra de negócio que o
     * produziu e o campo canônico associado — nulo quando o motivo não se
     * refere a um campo específico (ex.: {@code ITEM_TIPO_INVALIDO}).
     */
    public record Motivo(MotivoCodigo codigo, RegraNegocio regra, CampoCanonico campo) {

        public Motivo {
            Objects.requireNonNull(codigo, "codigo");
            Objects.requireNonNull(regra, "regra");
        }
    }
}
