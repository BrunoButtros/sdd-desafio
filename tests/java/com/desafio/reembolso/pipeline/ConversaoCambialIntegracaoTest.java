package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.TabelaCambio;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-004, RN-009 e RN-020 (spec §9, §14; plan §9, §14; CA-031, CA-032;
 * DT-015): a ordem real de arredondamento da conversão cambial — um único
 * arredondamento, no fim da cadeia, nunca um na moeda original seguido de
 * outro após a conversão — e a consulta ao valor já convertido por RN-009.
 * Sempre através dos componentes reais ({@link ResolutorCambio},
 * {@link Normalizador}, {@link AvaliadorRegrasIndividuais}), nunca
 * reproduzindo a fórmula de conversão manualmente como implementação
 * alternativa.
 */
@DisplayName("Conversão cambial — integração ResolutorCambio + Normalizador (RN-004, RN-009, RN-020)")
class ConversaoCambialIntegracaoTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static TabelaCambio tabelaComCotacao(String moeda, LocalDate data, BigDecimal taxa) {
        NavigableMap<LocalDate, BigDecimal> cotacoes = new TreeMap<>();
        cotacoes.put(data, taxa);
        return new TabelaCambio("BRL", Map.of(moeda, cotacoes));
    }

    private static ItemValidado itemValidadoUnico(String moeda, BigDecimal valor, LocalDate data,
                                                    boolean temNotaFiscal) {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "%s", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": %s, "moeda": "%s",
                      "tem_nota_fiscal": %s }
                  ]
                }
                """.formatted(data, valor.toPlainString(), moeda, temNotaFiscal);
        JsonNode despesas;
        try {
            despesas = MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ValidadorItem.validarLista(despesas).get(0);
    }

    // ---- 1. Ordem real de arredondamento (canário 1.005 x 1.005) -------------------------

    @Test
    @DisplayName("1 — canário de arredondamento: 1.005 x 1.005 produz ResolutorCambio=1.010025 e Normalizador=1.01, nunca 1.02")
    void canarioArredondamento_1005x1005_produzUmUnicoArredondamento() {
        LocalDate data = LocalDate.of(2026, 7, 3);
        TabelaCambio cambio = tabelaComCotacao("USD", data, new BigDecimal("1.005"));
        ItemValidado item = itemValidadoUnico("USD", new BigDecimal("1.005"), data, true);

        ItemValidado resolvido = ResolutorCambio.resolver(item, cambio);
        assertEquals(0, new BigDecimal("1.010025").compareTo(resolvido.getValorConvertidoBruto()),
                "ResolutorCambio não arredonda: produto exato de 1.005 x 1.005");

        ItemNormalizado normalizado = Normalizador.normalizar(resolvido);
        assertEquals(new BigDecimal("1.01"), normalizado.valorNormalizado(),
                "Normalizador aplica HALF_UP uma única vez, sobre o produto exato");
        assertNotEquals(0, new BigDecimal("1.02").compareTo(normalizado.valorNormalizado()),
                "arredondar a moeda original antes de converter produziria 1.02 — nunca deve ocorrer");
    }

    // ---- 2. Conversão normativa (CA-031) --------------------------------------------------

    @Test
    @DisplayName("2 — CA-031: USD 40,00 x taxa 5,50 produz valorConvertidoBruto 220.000 e valorNormalizado 220.00")
    void conversaoNormativa_usd40Vezes550_produz220() {
        LocalDate data = LocalDate.of(2026, 7, 3);
        TabelaCambio cambio = tabelaComCotacao("USD", data, new BigDecimal("5.50"));
        ItemValidado item = itemValidadoUnico("USD", new BigDecimal("40.00"), data, true);

        ItemValidado resolvido = ResolutorCambio.resolver(item, cambio);
        assertEquals(0, new BigDecimal("220.000").compareTo(resolvido.getValorConvertidoBruto()),
                "produto exato de 40.00 x 5.50, sem arredondamento neste estágio");

        ItemNormalizado normalizado = Normalizador.normalizar(resolvido);
        assertEquals(0, new BigDecimal("220.00").compareTo(normalizado.valorNormalizado()));
    }

    // ---- 3. Nota fiscal sobre o valor convertido (CA-032) ---------------------------------

    @Test
    @DisplayName("3 — CA-032: USD 40,00 (abaixo de R$100 na moeda original) convertido a R$220,00, sem nota fiscal, é recusado com NOTA_FISCAL_AUSENTE — RN-009 consulta o valor convertido")
    void notaFiscalSobreValorConvertido_usd40SemNota_recebeNotaFiscalAusente() {
        String json = """
                {
                  "periodo": { "inicio": "2026-07-01", "fim": "2026-07-31" },
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": 40.00, "moeda": "USD",
                      "tem_nota_fiscal": false }
                  ]
                }
                """;
        JsonNode raiz;
        try {
            raiz = MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Envelope envelope = ValidadorEnvelope.validar(raiz);
        TabelaCambio cambio = tabelaComCotacao("USD", LocalDate.of(2026, 7, 3), new BigDecimal("5.50"));

        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = ResolutorCambio.resolverLista(idsVerificados, cambio);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);

        assertEquals(0, new BigDecimal("40.00").compareTo(normalizados.get(0).item().getValor()),
                "valor original na moeda da despesa permanece abaixo do gatilho de R$100");
        assertEquals(0, new BigDecimal("220.00").compareTo(normalizados.get(0).valorNormalizado()),
                "valor convertido/normalizado está acima do gatilho de nota fiscal");

        List<ItemAvaliado> avaliados = CambioTesteSupport.avaliarLista(normalizados);
        ItemAvaliado avaliado = avaliados.get(0);

        assertFalse(avaliado.elegivel());
        assertTrue(avaliado.motivos().stream().anyMatch(m -> m.codigo() == MotivoCodigo.NOTA_FISCAL_AUSENTE),
                "RN-009 deve comparar pelo valor convertido/normalizado (220.00), não pelo valor original (40.00)");
    }
}
