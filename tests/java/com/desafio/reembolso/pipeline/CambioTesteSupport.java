package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.PoliticaExterna;
import com.desafio.reembolso.modelo.TabelaCambio;
import com.desafio.reembolso.modelo.TabelaCategoria;
import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;
import com.desafio.reembolso.leitor.LeitorPolitica;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.nio.file.Path;
import java.time.LocalDate;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper exclusivo de teste (T-038): encadeia {@link ResolutorCambio} antes
 * de {@link Normalizador} para os testes históricos do pacote {@code
 * pipeline} cuja entrada vem de {@link ValidadorItem} — desde T-036,
 * {@code valorConvertidoBruto} só é preenchido por {@link ResolutorCambio},
 * inclusive para BRL. Não duplica nenhuma regra de câmbio: apenas chama
 * {@link ResolutorCambio} e {@link Normalizador}.
 */
final class CambioTesteSupport {

    /**
     * Tabela válida com moeda base BRL e cotações vazias — suficiente para
     * qualquer cenário BRL, já que {@link ResolutorCambio} não consulta a
     * tabela de taxas para BRL.
     */
    static final TabelaCambio TABELA_BRL = new TabelaCambio("BRL", Map.of());
    static final PoliticaExterna POLITICA_HISTORICA = carregarPoliticaHistorica();
    static final TabelaPoliticaResolvida TABELA_HISTORICA = tabelaHistoricaComEscalaMonetaria();

    private CambioTesteSupport() {
    }

    private static PoliticaExterna carregarPoliticaHistorica() {
        return LeitorPolitica.ler(Path.of("tests", "resources", "fixtures", "politica-historica.json"));
    }

    private static TabelaPoliticaResolvida tabelaHistoricaComEscalaMonetaria() {
        Map<String, TabelaCategoria> categorias = new LinkedHashMap<>();
        POLITICA_HISTORICA.getPadrao().forEach((nome, configuracao) -> categorias.put(nome,
                new TabelaCategoria(configuracao.limite().setScale(2, RoundingMode.UNNECESSARY),
                        configuracao.periodicidade())));
        return new TabelaPoliticaResolvida(
                categorias, TabelaPoliticaResolvida.Origem.PADRAO, null);
    }

    static TabelaPoliticaResolvida tabelaHistorica(Envelope envelope) {
        return TABELA_HISTORICA;
    }

    private static Envelope envelopeSemRestricaoTemporal(List<ItemNormalizado> itens) {
        return new Envelope(null, null, null, null, LocalDate.MIN, LocalDate.MAX,
                JsonNodeFactory.instance.arrayNode());
    }

    static ItemAvaliado avaliar(ItemNormalizado item) {
        Envelope envelope = envelopeSemRestricaoTemporal(List.of(item));
        return AvaliadorRegrasIndividuais.avaliar(
                item, envelope, tabelaHistorica(envelope), POLITICA_HISTORICA);
    }

    static ItemAvaliado avaliar(ItemNormalizado item, Envelope envelope) {
        return AvaliadorRegrasIndividuais.avaliar(
                item, envelope, tabelaHistorica(envelope), POLITICA_HISTORICA);
    }

    static List<ItemAvaliado> avaliarLista(List<ItemNormalizado> itens) {
        Envelope envelope = envelopeSemRestricaoTemporal(itens);
        return AvaliadorRegrasIndividuais.avaliarLista(
                itens, envelope, tabelaHistorica(envelope), POLITICA_HISTORICA);
    }

    static List<ItemAvaliado> avaliarLista(List<ItemNormalizado> itens, Envelope envelope) {
        return AvaliadorRegrasIndividuais.avaliarLista(
                itens, envelope, tabelaHistorica(envelope), POLITICA_HISTORICA);
    }

    static List<ResultadoTeto> aplicarTetoDiario(List<ItemAvaliado> itens) {
        return AgregadorTetoDiario.aplicar(itens, TABELA_HISTORICA);
    }

    static List<ResultadoTeto> aplicarTetoIndividual(List<ItemAvaliado> itens) {
        return AgregadorTetoIndividual.aplicar(itens, TABELA_HISTORICA);
    }

    static ItemValidado resolver(ItemValidado item) {
        return resolver(item, TABELA_BRL);
    }

    static ItemValidado resolver(ItemValidado item, TabelaCambio cambio) {
        return ResolutorCambio.resolver(item, cambio);
    }

    static List<ItemValidado> resolverLista(List<ItemValidado> itens) {
        return resolverLista(itens, TABELA_BRL);
    }

    static List<ItemValidado> resolverLista(List<ItemValidado> itens, TabelaCambio cambio) {
        return ResolutorCambio.resolverLista(itens, cambio);
    }

    static ItemNormalizado resolverENormalizar(ItemValidado item) {
        return resolverENormalizar(item, TABELA_BRL);
    }

    static ItemNormalizado resolverENormalizar(ItemValidado item, TabelaCambio cambio) {
        return Normalizador.normalizar(resolver(item, cambio));
    }

    static List<ItemNormalizado> resolverENormalizarLista(List<ItemValidado> itens) {
        return resolverENormalizarLista(itens, TABELA_BRL);
    }

    static List<ItemNormalizado> resolverENormalizarLista(List<ItemValidado> itens, TabelaCambio cambio) {
        return Normalizador.normalizarLista(resolverLista(itens, cambio));
    }
}
