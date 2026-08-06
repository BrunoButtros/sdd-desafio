package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.modelo.TabelaCambio;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre {@link ResolutorCambio} (spec 8.1, passo 5; RN-020; plan §9): BRL
 * (informado e assumido), moeda estrangeira com cotação exata ou por
 * fallback, ausência de cotação (inclusive cotação futura), e a exigência
 * de que apenas {@code valor}/{@code moeda}/{@code data} controlam a
 * resolução — sem arredondar {@code valorConvertidoBruto} (DT-015).
 */
@DisplayName("ResolutorCambio — resolução cambial (spec 8.1 passo 5, RN-020)")
class ResolucaoCambioTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static ItemValidado item(String moeda, BigDecimal valor, LocalDate data, List<Motivo> motivos) {
        return new ItemValidado(
                1,
                "d-001",
                data,
                "alimentacao",
                "Almoço",
                "Restaurante X",
                valor,
                true,
                null,
                motivos,
                moeda,
                null,
                null,
                null);
    }

    private static TabelaCambio tabelaCom(String moeda, Map<LocalDate, BigDecimal> cotacoes) {
        NavigableMap<LocalDate, BigDecimal> ordenadas = new TreeMap<>(cotacoes);
        Map<String, NavigableMap<LocalDate, BigDecimal>> cotacoesPorMoeda = new HashMap<>();
        cotacoesPorMoeda.put(moeda, ordenadas);
        return new TabelaCambio("BRL", cotacoesPorMoeda);
    }

    private static TabelaCambio tabelaVazia() {
        return new TabelaCambio("BRL", Map.of());
    }

    @Test
    @DisplayName("1. BRL informado: taxa 1, sem data de cotação, valorConvertidoBruto igual ao valor, sem novo motivo")
    void brlInformado() {
        BigDecimal valor = new BigDecimal("72.50");
        ItemValidado original = item("BRL", valor, LocalDate.of(2026, 7, 3), List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, tabelaVazia());

        assertEquals(0, BigDecimal.ONE.compareTo(resolvido.getTaxaCambioAplicada()));
        assertNull(resolvido.getDataCotacaoUtilizada());
        assertEquals(0, valor.compareTo(resolvido.getValorConvertidoBruto()));
        assertTrue(resolvido.getMotivos().isEmpty());
    }

    @Test
    @DisplayName("2. BRL assumido por ausência da chave moeda produz os mesmos derivados do BRL informado")
    void brlAssumidoPorAusencia_mesmosDerivadosDoBrlInformado() throws Exception {
        JsonNode despesas = MAPPER.readTree("""
                {
                  "despesas": [
                    {
                      "id": "d-001",
                      "data": "2026-07-03",
                      "categoria": "alimentacao",
                      "descricao": "Almoço",
                      "fornecedor": "Restaurante X",
                      "valor": 72.50,
                      "tem_nota_fiscal": true
                    }
                  ]
                }
                """).get("despesas");

        ItemValidado itemValidado = ValidadorItem.validarLista(despesas).get(0);
        assertEquals("BRL", itemValidado.getMoeda());

        ItemValidado resolvido = ResolutorCambio.resolver(itemValidado, tabelaVazia());

        ItemValidado brlInformado = ResolutorCambio.resolver(
                item("BRL", new BigDecimal("72.50"), LocalDate.of(2026, 7, 3), List.of()), tabelaVazia());

        assertEquals(0, brlInformado.getTaxaCambioAplicada().compareTo(resolvido.getTaxaCambioAplicada()));
        assertEquals(brlInformado.getDataCotacaoUtilizada(), resolvido.getDataCotacaoUtilizada());
        assertEquals(0, brlInformado.getValorConvertidoBruto().compareTo(resolvido.getValorConvertidoBruto()));
        assertTrue(resolvido.getMotivos().isEmpty());
    }

    @Test
    @DisplayName("3. moeda estrangeira com cotação exata: taxa correta, data de cotação igual à data da despesa, produto exato")
    void moedaEstrangeiraComCotacaoExata() {
        LocalDate dataDespesa = LocalDate.of(2026, 7, 17);
        BigDecimal taxa = new BigDecimal("5.45");
        TabelaCambio cambio = tabelaCom("USD", Map.of(dataDespesa, taxa));

        BigDecimal valor = new BigDecimal("100.00");
        ItemValidado original = item("USD", valor, dataDespesa, List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, cambio);

        assertEquals(0, taxa.compareTo(resolvido.getTaxaCambioAplicada()));
        assertEquals(dataDespesa, resolvido.getDataCotacaoUtilizada());
        assertEquals(0, valor.multiply(taxa).compareTo(resolvido.getValorConvertidoBruto()));
        assertTrue(resolvido.getMotivos().isEmpty());
    }

    @Test
    @DisplayName("4. fallback: despesa de sábado usa a cotação de sexta-feira; dataCotacaoUtilizada é a sexta, não o sábado")
    void fallbackParaCotacaoAnterior() {
        LocalDate sexta = LocalDate.of(2026, 7, 17);
        LocalDate sabado = LocalDate.of(2026, 7, 18);
        BigDecimal taxaSexta = new BigDecimal("5.96");
        TabelaCambio cambio = tabelaCom("EUR", Map.of(sexta, taxaSexta));

        BigDecimal valor = new BigDecimal("50.00");
        ItemValidado original = item("EUR", valor, sabado, List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, cambio);

        assertEquals(sexta, resolvido.getDataCotacaoUtilizada());
        assertEquals(0, taxaSexta.compareTo(resolvido.getTaxaCambioAplicada()));
        assertEquals(0, valor.multiply(taxaSexta).compareTo(resolvido.getValorConvertidoBruto()));
    }

    @Test
    @DisplayName("5. cotação futura proibida: só existe cotação posterior à data da despesa → MOEDA_SEM_COTACAO")
    void cotacaoFuturaProibida() {
        LocalDate dataDespesa = LocalDate.of(2026, 7, 15);
        LocalDate dataFutura = LocalDate.of(2026, 7, 20);
        TabelaCambio cambio = tabelaCom("USD", Map.of(dataFutura, new BigDecimal("5.50")));

        ItemValidado original = item("USD", new BigDecimal("100.00"), dataDespesa, List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, cambio);

        assertNull(resolvido.getTaxaCambioAplicada());
        assertNull(resolvido.getDataCotacaoUtilizada());
        assertNull(resolvido.getValorConvertidoBruto());
        assertTrue(resolvido.getMotivos().contains(
                new Motivo(MotivoCodigo.MOEDA_SEM_COTACAO, RegraNegocio.RN_020, CampoCanonico.MOEDA)));
    }

    @Test
    @DisplayName("6. moeda nunca presente na tabela: derivados nulos e motivo MOEDA_SEM_COTACAO")
    void moedaNuncaPresenteNaTabela() {
        TabelaCambio cambio = tabelaCom("USD", Map.of(LocalDate.of(2026, 7, 15), new BigDecimal("5.40")));

        ItemValidado original = item("GBP", new BigDecimal("30.00"), LocalDate.of(2026, 7, 15), List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, cambio);

        assertNull(resolvido.getTaxaCambioAplicada());
        assertNull(resolvido.getDataCotacaoUtilizada());
        assertNull(resolvido.getValorConvertidoBruto());
        assertTrue(resolvido.getMotivos().contains(
                new Motivo(MotivoCodigo.MOEDA_SEM_COTACAO, RegraNegocio.RN_020, CampoCanonico.MOEDA)));
    }

    @Test
    @DisplayName("7. motivo MOEDA_SEM_COTACAO carrega codigo, RegraNegocio.RN_020 e CampoCanonico.MOEDA")
    void motivoMoedaSemCotacao_camposCorretos() {
        ItemValidado original = item("GBP", new BigDecimal("30.00"), LocalDate.of(2026, 7, 15), List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, tabelaVazia());

        Motivo motivo = resolvido.getMotivos().get(resolvido.getMotivos().size() - 1);
        assertEquals(MotivoCodigo.MOEDA_SEM_COTACAO, motivo.codigo());
        assertEquals(RegraNegocio.RN_020, motivo.regra());
        assertEquals(CampoCanonico.MOEDA, motivo.campo());
    }

    @Test
    @DisplayName("8. valor null: devolve o item sem resolução, sem novo motivo, sem NullPointerException")
    void valorNull_semResolucao() {
        ItemValidado original = item("USD", null, LocalDate.of(2026, 7, 15), List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, tabelaVazia());

        assertSame(original, resolvido);
        assertNull(resolvido.getTaxaCambioAplicada());
        assertNull(resolvido.getDataCotacaoUtilizada());
        assertNull(resolvido.getValorConvertidoBruto());
        assertTrue(resolvido.getMotivos().isEmpty());
    }

    @Test
    @DisplayName("9. data null: devolve o item sem resolução, sem novo motivo, sem NullPointerException")
    void dataNull_semResolucao() {
        ItemValidado original = item("USD", new BigDecimal("30.00"), null, List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, tabelaVazia());

        assertSame(original, resolvido);
        assertNull(resolvido.getTaxaCambioAplicada());
        assertNull(resolvido.getDataCotacaoUtilizada());
        assertNull(resolvido.getValorConvertidoBruto());
        assertTrue(resolvido.getMotivos().isEmpty());
    }

    @Test
    @DisplayName("10. moeda null: devolve o item sem resolução, sem novo motivo")
    void moedaNull_semResolucao() {
        ItemValidado original = item(null, new BigDecimal("30.00"), LocalDate.of(2026, 7, 15), List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, tabelaVazia());

        assertSame(original, resolvido);
        assertNull(resolvido.getTaxaCambioAplicada());
        assertNull(resolvido.getDataCotacaoUtilizada());
        assertNull(resolvido.getValorConvertidoBruto());
        assertTrue(resolvido.getMotivos().isEmpty());
    }

    @Test
    @DisplayName("11. campo não financeiro inválido não bloqueia a conversão")
    void campoNaoFinanceiroInvalido_naoBloqueiaConversao() {
        LocalDate dataDespesa = LocalDate.of(2026, 7, 17);
        BigDecimal taxa = new BigDecimal("5.45");
        TabelaCambio cambio = tabelaCom("USD", Map.of(dataDespesa, taxa));

        BigDecimal valor = new BigDecimal("100.00");
        ItemValidado original = new ItemValidado(
                1,
                "d-001",
                dataDespesa,
                null,
                null,
                null,
                valor,
                null,
                null,
                List.of(),
                "USD",
                null,
                null,
                null);

        ItemValidado resolvido = ResolutorCambio.resolver(original, cambio);

        assertEquals(0, taxa.compareTo(resolvido.getTaxaCambioAplicada()));
        assertEquals(dataDespesa, resolvido.getDataCotacaoUtilizada());
        assertEquals(0, valor.multiply(taxa).compareTo(resolvido.getValorConvertidoBruto()));
    }

    @Test
    @DisplayName("12. precisão total: valor 1.005 x taxa 1.005 = 1.010025 exatos, sem arredondamento")
    void precisaoTotal_semArredondamento() {
        LocalDate dataDespesa = LocalDate.of(2026, 7, 17);
        BigDecimal taxa = new BigDecimal("1.005");
        TabelaCambio cambio = tabelaCom("USD", Map.of(dataDespesa, taxa));

        BigDecimal valor = new BigDecimal("1.005");
        ItemValidado original = item("USD", valor, dataDespesa, List.of());

        ItemValidado resolvido = ResolutorCambio.resolver(original, cambio);

        assertEquals(new BigDecimal("1.010025"), resolvido.getValorConvertidoBruto());
    }

    @Test
    @DisplayName("13. motivos anteriores permanecem na mesma ordem; MOEDA_SEM_COTACAO é acrescentado ao final; lista original intacta")
    void motivosAnteriores_ordemPreservadaEListaOriginalIntacta() {
        Motivo motivoAnterior = new Motivo(MotivoCodigo.FORA_COMPETENCIA, RegraNegocio.RN_008, null);
        List<Motivo> motivosOriginais = List.of(motivoAnterior);

        ItemValidado original = item("GBP", new BigDecimal("30.00"), LocalDate.of(2026, 7, 15), motivosOriginais);

        ItemValidado resolvido = ResolutorCambio.resolver(original, tabelaVazia());

        assertEquals(List.of(motivoAnterior), motivosOriginais, "lista original não deve ser modificada");
        assertEquals(2, resolvido.getMotivos().size());
        assertEquals(motivoAnterior, resolvido.getMotivos().get(0));
        assertEquals(new Motivo(MotivoCodigo.MOEDA_SEM_COTACAO, RegraNegocio.RN_020, CampoCanonico.MOEDA),
                resolvido.getMotivos().get(1));
    }

    @Test
    @DisplayName("14. resolverLista preserva ordem e quantidade, resolve cada item conforme sua moeda, e devolve lista não modificável")
    void resolverLista_preservaOrdemEDevolveListaNaoModificavel() {
        LocalDate dataDespesa = LocalDate.of(2026, 7, 17);
        TabelaCambio cambio = tabelaCom("USD", Map.of(dataDespesa, new BigDecimal("5.45")));

        ItemValidado brl = item("BRL", new BigDecimal("10.00"), dataDespesa, List.of());
        ItemValidado usd = item("USD", new BigDecimal("20.00"), dataDespesa, List.of());
        ItemValidado semCotacao = item("EUR", new BigDecimal("30.00"), dataDespesa, List.of());

        List<ItemValidado> resolvidos = ResolutorCambio.resolverLista(List.of(brl, usd, semCotacao), cambio);

        assertEquals(3, resolvidos.size());

        assertEquals(0, BigDecimal.ONE.compareTo(resolvidos.get(0).getTaxaCambioAplicada()));

        assertEquals(0, new BigDecimal("5.45").compareTo(resolvidos.get(1).getTaxaCambioAplicada()));
        assertEquals(0, new BigDecimal("109.00").compareTo(resolvidos.get(1).getValorConvertidoBruto()));

        assertNull(resolvidos.get(2).getTaxaCambioAplicada());
        assertTrue(resolvidos.get(2).getMotivos().contains(
                new Motivo(MotivoCodigo.MOEDA_SEM_COTACAO, RegraNegocio.RN_020, CampoCanonico.MOEDA)));

        assertThrows(UnsupportedOperationException.class,
                () -> resolvidos.add(item("BRL", BigDecimal.ONE, dataDespesa, List.of())));
    }
}
