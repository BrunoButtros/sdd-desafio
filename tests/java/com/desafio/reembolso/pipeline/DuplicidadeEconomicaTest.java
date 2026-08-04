package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-010 / CA-013 / CA-014 (spec 4.5, 8.1, 8.2, 8.4) e a seleção de
 * itens elegíveis (spec 8.1, passos 6 e 8): duplicidade econômica exata —
 * mesma {@code data}, categoria normalizada, valor normalizado, fornecedor e
 * descrição como recebidos — mantendo elegível somente a ocorrência de menor
 * {@code indiceEntrada} por chave. Nesta task, tetos e composição de saída
 * ainda não existem: os cenários verificam apenas motivos, elegibilidade e
 * {@code valorReembolsavel} no nível de duplicidade.
 */
@DisplayName("Duplicidade econômica e seleção de elegíveis — RN-010 / CA-013 / CA-014")
class DuplicidadeEconomicaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final String JANELA_JULHO =
            "\"periodo\": { \"inicio\": \"2026-07-01\", \"fim\": \"2026-07-31\" },";

    private static JsonNode raiz(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Envelope envelope(String json) {
        return ValidadorEnvelope.validar(raiz(json));
    }

    private static String item(String id, String data, String categoria, String descricao,
                                String fornecedor, String valorJson, boolean temNotaFiscal) {
        return """
                { "id": "%s", "data": "%s", "categoria": "%s", "descricao": "%s",
                  "fornecedor": "%s", "valor": %s, "tem_nota_fiscal": %s }""".formatted(
                id, data, categoria, descricao, fornecedor, valorJson, temNotaFiscal);
    }

    private static String envelopeComItens(String... despesasJson) {
        return """
                {
                  %s
                  "despesas": [
                    %s
                  ]
                }
                """.formatted(JANELA_JULHO, String.join(",\n", despesasJson));
    }

    private static List<ItemNormalizado> normalizados(String json) {
        Envelope envelope = envelope(json);
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> comIdsVerificados = DetectorIdDuplicado.detectar(validados);
        return Normalizador.normalizarLista(comIdsVerificados);
    }

    private static List<ItemAvaliado> avaliar(String json) {
        Envelope envelope = envelope(json);
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> comIdsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comIdsVerificados);
        return AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);
    }

    private static List<ItemAvaliado> detectarAposSelecao(String json) {
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliar(json));
        return DetectorDuplicidadeEconomica.detectar(aprovados);
    }

    private static Motivo duplicidade() {
        return new Motivo(MotivoCodigo.DUPLICIDADE, RegraNegocio.RN_010, null);
    }

    // ---- 1. Par normativo de R$ 54,90 --------------------------------------

    @Test
    @DisplayName("1 — par normativo de R$ 54,90: primeira ocorrência elegível, segunda recebe DUPLICIDADE")
    void parNormativo5490_primeiraElegivelSegundaDuplicidade() {
        String json = envelopeComItens(
                item("d-006", "2026-07-09", "alimentacao", "Almoco", "Restaurante X", "54.90", true),
                item("d-007", "2026-07-09", "alimentacao", "Almoco", "Restaurante X", "54.90", true)
        );
        List<ItemAvaliado> avaliados = avaliar(json);
        assertTrue(avaliados.get(0).elegivel());
        assertTrue(avaliados.get(1).elegivel());

        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);

        ItemAvaliado primeiro = aposDuplicidade.get(0);
        assertFalse(primeiro.motivos().contains(duplicidade()));
        assertTrue(primeiro.elegivel());
        assertNull(primeiro.valorReembolsavel());

        ItemAvaliado segundo = aposDuplicidade.get(1);
        assertEquals(1, segundo.motivos().size());
        Motivo motivo = segundo.motivos().get(0);
        assertEquals(MotivoCodigo.DUPLICIDADE, motivo.codigo());
        assertEquals(RegraNegocio.RN_010, motivo.regra());
        assertNull(motivo.campo());
        assertFalse(segundo.elegivel());
        assertEquals(new BigDecimal("0.00"), segundo.valorReembolsavel());
        assertEquals(2, segundo.valorReembolsavel().scale());
    }

    // ---- 2. id não integra a chave ------------------------------------------

    @Test
    @DisplayName("2 — id não integra a chave econômica: ids diferentes, demais componentes iguais, ainda são duplicados")
    void idNaoIntegraChave_idsDiferentesAindaSaoDuplicados() {
        String json = envelopeComItens(
                item("d-aaa", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-bbb", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertTrue(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 3. Nota fiscal não integra a chave ---------------------------------

    @Test
    @DisplayName("3 — tem_nota_fiscal não integra a chave econômica")
    void notaFiscalNaoIntegraChave_segundaOcorrenciaEhDuplicada() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "54.90", false)
        );
        List<ItemAvaliado> avaliados = avaliar(json);
        assertTrue(avaliados.get(0).elegivel());
        assertTrue(avaliados.get(1).elegivel());

        List<ItemAvaliado> aposDuplicidade =
                DetectorDuplicidadeEconomica.detectar(SeletorElegiveis.selecionar(avaliados));

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertTrue(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 4. Valores 100.00 e 100.01 -----------------------------------------

    @Test
    @DisplayName("4 — 100.00 e 100.01: não são duplicados, ambos permanecem elegíveis")
    void valoresDistintosPorUmCentavo_naoSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "100.00", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "100.01", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertEquals(2, aposDuplicidade.size());
        assertTrue(aposDuplicidade.get(0).elegivel());
        assertTrue(aposDuplicidade.get(1).elegivel());
        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 5. Normalização monetária ------------------------------------------

    @Test
    @DisplayName("5 — normalização monetária: 54.9 e 54.900 normalizam para 54.90 e são duplicados")
    void normalizacaoMonetaria_valoresEquivalentesSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.9", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "54.900", true)
        );
        List<ItemNormalizado> normalizados = normalizados(json);
        assertEquals(new BigDecimal("54.90"), normalizados.get(0).valorNormalizado());
        assertEquals(new BigDecimal("54.90"), normalizados.get(1).valorNormalizado());

        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);
        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertTrue(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 6. Normalização de categoria ---------------------------------------

    @Test
    @DisplayName("6 — normalização de categoria: ALIMENTAÇÃO e alimentacao normalizam igual e são duplicados")
    void normalizacaoCategoria_valoresEquivalentesSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "ALIMENTAÇÃO", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true)
        );
        List<ItemNormalizado> normalizados = normalizados(json);
        assertEquals("alimentacao", normalizados.get(0).categoriaNormalizada());
        assertEquals("alimentacao", normalizados.get(1).categoriaNormalizada());

        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);
        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertTrue(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 7. Data diferente ---------------------------------------------------

    @Test
    @DisplayName("7 — data diferente: demais componentes iguais, não são duplicados")
    void dataDiferente_naoSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-10", "alimentacao", "Item", "F1", "54.90", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 8. Categoria diferente -----------------------------------------------

    @Test
    @DisplayName("8 — categoria diferente: categorias válidas distintas não são duplicadas")
    void categoriaDiferente_naoSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "transporte_urbano", "Item", "F1", "54.90", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 9. Valor diferente ----------------------------------------------------

    @Test
    @DisplayName("9 — valor diferente: valores normalizados distintos não são duplicados")
    void valorDiferente_naoSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "50.00", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "60.00", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 10. Fornecedor diferente -----------------------------------------------

    @Test
    @DisplayName("10 — fornecedor diferente: demais componentes iguais, não são duplicados")
    void fornecedorDiferente_naoSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F2", "54.90", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 11. Descrição diferente -----------------------------------------------

    @Test
    @DisplayName("11 — descrição diferente: demais componentes iguais, não são duplicados")
    void descricaoDiferente_naoSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item A", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item B", "F1", "54.90", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 12. Comparação textual exata --------------------------------------

    @Test
    @DisplayName("12a — comparação textual exata: fornecedor com diferença apenas de caixa não é normalizado para RN-010")
    void fornecedorComDiferencaDeCaixa_naoSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Almoco", "Restaurante X", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Almoco", "restaurante x", "54.90", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    @Test
    @DisplayName("12b — comparação textual exata: descrição com diferença apenas de acento não é normalizada para RN-010")
    void descricaoComDiferencaDeAcento_naoSaoDuplicados() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Almoço", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Almoco", "F1", "54.90", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).motivos().contains(duplicidade()));
    }

    // ---- 13. Item recusado por RN-009 não contamina item aprovado ----------

    @Test
    @DisplayName("13 — item recusado por RN-009 (nota fiscal ausente) não contamina item aprovado com a mesma chave")
    void itemRecusadoPorNotaFiscal_naoContaminaItemAprovado() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "150.00", false),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "150.00", true)
        );
        List<ItemAvaliado> avaliados = avaliar(json);
        assertFalse(avaliados.get(0).elegivel());
        assertTrue(avaliados.get(1).elegivel());

        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        assertEquals(1, aprovados.size());
        assertSame(avaliados.get(1), aprovados.get(0));

        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);
        assertEquals(1, aposDuplicidade.size());
        assertTrue(aposDuplicidade.get(0).elegivel());
        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));

        List<ItemAvaliado> mistos = List.of(avaliados.get(0), avaliados.get(1));
        List<ItemAvaliado> aposDuplicidadeMista = DetectorDuplicidadeEconomica.detectar(mistos);

        assertSame(avaliados.get(0), aposDuplicidadeMista.get(0));
        assertFalse(aposDuplicidadeMista.get(0).elegivel());
        assertTrue(aposDuplicidadeMista.get(1).elegivel());
        assertFalse(aposDuplicidadeMista.get(1).motivos().contains(duplicidade()));
    }

    // ---- 14. ID_DUPLICADO não contamina item elegível -----------------------

    @Test
    @DisplayName("14 — ID_DUPLICADO não contamina item elegível com a mesma chave econômica")
    void idDuplicado_naoContaminaItemElegivel() {
        String json = envelopeComItens(
                item("d-100", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-100", "2026-07-10", "alimentacao", "Outro", "F2", "20.00", true),
                item("d-200", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true)
        );
        List<ItemAvaliado> avaliados = avaliar(json);

        assertFalse(avaliados.get(0).elegivel());
        assertTrue(avaliados.get(0).motivos().stream().anyMatch(m -> m.codigo() == MotivoCodigo.ID_DUPLICADO));
        assertFalse(avaliados.get(1).elegivel());
        assertTrue(avaliados.get(1).motivos().stream().anyMatch(m -> m.codigo() == MotivoCodigo.ID_DUPLICADO));
        assertTrue(avaliados.get(2).elegivel());

        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        assertEquals(1, aprovados.size());
        assertSame(avaliados.get(2), aprovados.get(0));

        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);
        assertEquals(1, aposDuplicidade.size());
        assertTrue(aposDuplicidade.get(0).elegivel());
        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
    }

    // ---- 15. Três ocorrências iguais ------------------------------------------

    @Test
    @DisplayName("15 — três ocorrências economicamente iguais: só a de menor indiceEntrada permanece elegível")
    void tresOcorrenciasIguais_somenteMenorIndicePermaneceElegivel() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-003", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true)
        );
        List<ItemAvaliado> aposDuplicidade = detectarAposSelecao(json);

        assertTrue(aposDuplicidade.get(0).elegivel());
        assertFalse(aposDuplicidade.get(0).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(1).elegivel());
        assertTrue(aposDuplicidade.get(1).motivos().contains(duplicidade()));
        assertFalse(aposDuplicidade.get(2).elegivel());
        assertTrue(aposDuplicidade.get(2).motivos().contains(duplicidade()));

        List<ItemAvaliado> elegiveis = SeletorElegiveis.selecionar(aposDuplicidade);
        assertEquals(1, elegiveis.size());
        assertSame(aposDuplicidade.get(0), elegiveis.get(0));
    }

    // ---- 16. Lista recebida fora de ordem --------------------------------------

    @Test
    @DisplayName("16 — lista fora de ordem: menor indiceEntrada vence independentemente da posição na lista recebida")
    void listaForaDeOrdem_menorIndiceVenceIndependenteDaPosicao() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-003", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true)
        );
        List<ItemAvaliado> avaliados = avaliar(json);
        ItemAvaliado entrada1 = avaliados.get(0);
        ItemAvaliado entrada2 = avaliados.get(1);
        ItemAvaliado entrada3 = avaliados.get(2);

        List<ItemAvaliado> foraDeOrdem = List.of(entrada3, entrada1, entrada2);
        List<ItemAvaliado> resultado = DetectorDuplicidadeEconomica.detectar(foraDeOrdem);

        assertEquals(3, resultado.size());

        assertEquals(3, resultado.get(0).itemNormalizado().item().getIndiceEntrada());
        assertFalse(resultado.get(0).elegivel());
        assertTrue(resultado.get(0).motivos().contains(duplicidade()));

        assertEquals(1, resultado.get(1).itemNormalizado().item().getIndiceEntrada());
        assertTrue(resultado.get(1).elegivel());
        assertFalse(resultado.get(1).motivos().contains(duplicidade()));

        assertEquals(2, resultado.get(2).itemNormalizado().item().getIndiceEntrada());
        assertFalse(resultado.get(2).elegivel());
        assertTrue(resultado.get(2).motivos().contains(duplicidade()));
    }

    // ---- 17. Seletor antes das duplicidades ------------------------------------

    @Test
    @DisplayName("17 — seletor antes da duplicidade: só o item elegível é retornado, ordem e referência preservadas")
    void seletorAntesDaDuplicidade_retornaSomenteElegivel() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "-10.00", true),
                item("d-002", "2026-07-09", "coworking", "Item", "F1", "50.00", true),
                item("d-003", "2026-04-15", "alimentacao", "Item", "F1", "50.00", true),
                item("d-004", "2026-07-09", "alimentacao", "Item", "F1", "50.00", true),
                item("d-005", "2026-07-09", "alimentacao", "Item", "F1", "150.00", false)
        );
        List<ItemAvaliado> avaliados = avaliar(json);
        assertFalse(avaliados.get(0).elegivel());
        assertFalse(avaliados.get(1).elegivel());
        assertFalse(avaliados.get(2).elegivel());
        assertTrue(avaliados.get(3).elegivel());
        assertFalse(avaliados.get(4).elegivel());

        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);

        assertEquals(1, aprovados.size());
        assertSame(avaliados.get(3), aprovados.get(0));
        assertThrows(UnsupportedOperationException.class, () -> aprovados.add(avaliados.get(0)));
    }

    // ---- 18. Seletor depois das duplicidades -----------------------------------

    @Test
    @DisplayName("18 — seletor depois da duplicidade: ocorrência duplicada é removida, ordem preservada")
    void seletorDepoisDaDuplicidade_removeOcorrenciaDuplicada() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-003", "2026-07-10", "alimentacao", "Outro", "F2", "30.00", true)
        );
        List<ItemAvaliado> avaliados = avaliar(json);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);

        List<ItemAvaliado> elegiveis = SeletorElegiveis.selecionar(aposDuplicidade);

        assertEquals(2, elegiveis.size());
        assertSame(aposDuplicidade.get(0), elegiveis.get(0));
        assertSame(aposDuplicidade.get(2), elegiveis.get(1));
    }

    // ---- 19. Lista vazia -----------------------------------------------------

    @Test
    @DisplayName("19 — lista vazia: selecionar e detectar retornam lista vazia e não modificável")
    void listaVazia_retornaVaziaENaoModificavel() {
        List<ItemAvaliado> vazio = List.of();

        List<ItemAvaliado> selecionado = SeletorElegiveis.selecionar(vazio);
        assertTrue(selecionado.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> selecionado.add(null));

        List<ItemAvaliado> detectado = DetectorDuplicidadeEconomica.detectar(vazio);
        assertTrue(detectado.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> detectado.add(null));
    }

    // ---- 20. Imutabilidade e referências ---------------------------------------

    @Test
    @DisplayName("20 — imutabilidade e referências: entrada preservada, referências mantidas, motivos não modificáveis")
    void imutabilidadeEReferencias() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true)
        );
        List<ItemAvaliado> avaliados = avaliar(json);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> copiaAntes = new ArrayList<>(aprovados);

        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);

        assertEquals(copiaAntes, aprovados);
        assertSame(copiaAntes.get(0), aprovados.get(0));
        assertSame(copiaAntes.get(1), aprovados.get(1));

        assertThrows(UnsupportedOperationException.class, () -> aposDuplicidade.add(aprovados.get(0)));

        assertSame(aprovados.get(0), aposDuplicidade.get(0));
        assertNotSame(aprovados.get(1), aposDuplicidade.get(1));
        assertSame(aprovados.get(1).itemNormalizado(), aposDuplicidade.get(1).itemNormalizado());

        assertThrows(UnsupportedOperationException.class,
                () -> aposDuplicidade.get(1).motivos().add(duplicidade()));
    }

    // ---- 21. Reaplicação -------------------------------------------------------

    @Test
    @DisplayName("21 — reaplicação do detector sobre o próprio resultado não duplica DUPLICIDADE nem altera a ocorrência mantida")
    void reaplicacao_naoDuplicaMotivoNemAlteraOcorrenciaMantida() {
        String json = envelopeComItens(
                item("d-001", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true),
                item("d-002", "2026-07-09", "alimentacao", "Item", "F1", "54.90", true)
        );
        List<ItemAvaliado> avaliados = avaliar(json);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> primeiraExecucao = DetectorDuplicidadeEconomica.detectar(aprovados);

        List<ItemAvaliado> segundaExecucao = DetectorDuplicidadeEconomica.detectar(primeiraExecucao);

        assertEquals(2, segundaExecucao.size());
        assertSame(primeiraExecucao.get(0), segundaExecucao.get(0));
        assertSame(primeiraExecucao.get(1), segundaExecucao.get(1));
        assertEquals(1, segundaExecucao.get(1).motivos().size());

        List<ItemAvaliado> elegiveisFinal = SeletorElegiveis.selecionar(segundaExecucao);
        assertEquals(1, elegiveisFinal.size());
        assertSame(primeiraExecucao.get(0), elegiveisFinal.get(0));
    }
}
