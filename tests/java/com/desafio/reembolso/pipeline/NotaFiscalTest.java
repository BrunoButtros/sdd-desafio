package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.PoliticaReembolso;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre {@link PoliticaReembolso} (plan §5, DT-007) e RN-009 / CA-008 / CA-009
 * (spec 4.5, 7, 8.2, 8.4): valor normalizado estritamente maior que o
 * gatilho de nota fiscal, sem {@code tem_nota_fiscal}, recebe
 * {@code NOTA_FISCAL_AUSENTE} e fica inelegível, com {@code valorReembolsavel}
 * {@code 0.00}. RN-009 depende exclusivamente de {@code valorNormalizado} e
 * {@code temNotaFiscal} — não é bloqueada por motivo estrutural em outro
 * campo, nem por {@code ID_DUPLICADO}, {@code VALOR_NAO_POSITIVO} ou
 * {@code CATEGORIA_FORA_POLITICA}, e não é avaliada quando valor ou nota
 * fiscal são estruturalmente inválidos.
 */
@DisplayName("Nota fiscal obrigatória — RN-009 / CA-008 / CA-009 e PoliticaReembolso")
class NotaFiscalTest {

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

    private static JsonNode despesas(String json) {
        return raiz(json).get("despesas");
    }

    private static List<ItemValidado> validar(String json) {
        return ValidadorItem.validarLista(despesas(json));
    }

    private static List<ItemNormalizado> normalizar(String json) {
        return Normalizador.normalizarLista(CambioTesteSupport.resolverLista(validar(json)));
    }

    private static List<ItemNormalizado> normalizarDespesas(Envelope envelope) {
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        return Normalizador.normalizarLista(CambioTesteSupport.resolverLista(validados));
    }

    private static Motivo notaFiscalAusente() {
        return new Motivo(MotivoCodigo.NOTA_FISCAL_AUSENTE, RegraNegocio.RN_009, null);
    }

    private static Motivo valorNaoPositivo() {
        return new Motivo(MotivoCodigo.VALOR_NAO_POSITIVO, RegraNegocio.RN_006, null);
    }

    private static String itemUnico(String valorJson, String notaJson) {
        return """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": %s, "tem_nota_fiscal": %s }
                  ]
                }
                """.formatted(valorJson, notaJson);
    }

    // ---- PoliticaReembolso ----------------------------------------------

    @Test
    @DisplayName("PoliticaReembolso 1 — padrao() retorna os quatro valores fixados pela spec")
    void padrao_retornaValoresFixados() {
        PoliticaReembolso politica = PoliticaReembolso.padrao();

        assertEquals(new BigDecimal("60.00"), politica.getLimiteDiarioAlimentacao());
        assertEquals(new BigDecimal("80.00"), politica.getLimiteDiarioTransporteUrbano());
        assertEquals(new BigDecimal("250.00"), politica.getLimiteIndividualHospedagem());
        assertEquals(new BigDecimal("100.00"), politica.getGatilhoNotaFiscal());
    }

    @Test
    @DisplayName("PoliticaReembolso 2 — os quatro valores têm escala exatamente 2")
    void padrao_valoresComEscala2() {
        PoliticaReembolso politica = PoliticaReembolso.padrao();

        assertEquals(2, politica.getLimiteDiarioAlimentacao().scale());
        assertEquals(2, politica.getLimiteDiarioTransporteUrbano().scale());
        assertEquals(2, politica.getLimiteIndividualHospedagem().scale());
        assertEquals(2, politica.getGatilhoNotaFiscal().scale());
    }

    @Test
    @DisplayName("PoliticaReembolso 3 — chamadas repetidas a padrao() retornam a mesma instância")
    void padrao_retornaMesmaInstancia() {
        assertSame(PoliticaReembolso.padrao(), PoliticaReembolso.padrao());
    }

    @Test
    @DisplayName("PoliticaReembolso 4 — não expõe setters nem estado mutável")
    void naoExpoeSettersNemEstadoMutavel() {
        for (Method metodo : PoliticaReembolso.class.getMethods()) {
            assertFalse(metodo.getName().startsWith("set"),
                    "PoliticaReembolso não deve expor setters: " + metodo.getName());
        }
        for (Field campo : PoliticaReembolso.class.getDeclaredFields()) {
            if (!campo.isSynthetic()) {
                assertTrue(Modifier.isFinal(campo.getModifiers()),
                        "Campo de PoliticaReembolso deve ser final: " + campo.getName());
            }
        }
    }

    // ---- RN-009 — casos de borda ------------------------------------------

    @Test
    @DisplayName("RN-009 caso 1 — 100.00 sem nota: normaliza para 100.00, não recebe NOTA_FISCAL_AUSENTE, elegível, reembolsável nulo")
    void gatilhoExato_naoExigeNota() {
        ItemNormalizado item = normalizar(itemUnico("100.00", "false")).get(0);
        assertEquals(new BigDecimal("100.00"), item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
        assertTrue(avaliado.elegivel());
        assertNull(avaliado.valorReembolsavel());
    }

    @Test
    @DisplayName("RN-009 caso 2 — 100.01 sem nota: recebe exatamente NOTA_FISCAL_AUSENTE, RN-009, campo nulo, inelegível, reembolsável 0.00 escala 2")
    void umCentavoAcimaDoGatilho_recebeMotivoEFicaInelegivel() {
        ItemNormalizado item = normalizar(itemUnico("100.01", "false")).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        Motivo motivo = avaliado.motivos().get(0);
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, motivo.codigo());
        assertEquals(RegraNegocio.RN_009, motivo.regra());
        assertNull(motivo.campo());
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());
        assertEquals(2, avaliado.valorReembolsavel().scale());
    }

    @Test
    @DisplayName("RN-009 caso 3 — 100.004 sem nota: normaliza para 100.00, permanece elegível, não recebe RN-009")
    void arredondamentoParaBaixoDoGatilho_naoExigeNota() {
        ItemNormalizado item = normalizar(itemUnico("100.004", "false")).get(0);
        assertEquals(new BigDecimal("100.00"), item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
        assertTrue(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 4 — 100.005 sem nota: normaliza para 100.01, recebe NOTA_FISCAL_AUSENTE")
    void arredondamentoParaCimaDoGatilho_exigeNota() {
        ItemNormalizado item = normalizar(itemUnico("100.005", "false")).get(0);
        assertEquals(new BigDecimal("100.01"), item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertTrue(avaliado.motivos().contains(notaFiscalAusente()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 5 — 690.00 com nota: não recebe NOTA_FISCAL_AUSENTE, permanece elegível")
    void valorAcimaDoGatilhoComNota_naoRecebeMotivo() {
        ItemNormalizado item = normalizar(itemUnico("690.00", "true")).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
        assertTrue(avaliado.elegivel());
        assertNull(avaliado.valorReembolsavel());
    }

    @Test
    @DisplayName("RN-009 caso 6 — 690.00 sem nota: recebe NOTA_FISCAL_AUSENTE, sem motivo de teto, reembolsável 0.00")
    void valorAcimaDoGatilhoSemNota_recebeMotivoSemTeto() {
        ItemNormalizado item = normalizar(itemUnico("690.00", "false")).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, avaliado.motivos().get(0).codigo());
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());
    }

    @Test
    @DisplayName("RN-009 caso 7 — -500.00 sem nota: recebe VALOR_NAO_POSITIVO, não recebe NOTA_FISCAL_AUSENTE (sem valor absoluto)")
    void valorNegativoSemNota_naoExigeNota() {
        ItemNormalizado item = normalizar(itemUnico("-500.00", "false")).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, avaliado.motivos().get(0).codigo());
        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 8 — valor zero sem nota: recebe RN-006, não recebe RN-009")
    void valorZeroSemNota_naoExigeNota() {
        ItemNormalizado item = normalizar(itemUnico("0", "false")).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertTrue(avaliado.motivos().contains(valorNaoPositivo()));
        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 9 — valor estruturalmente inválido (\"150,00\"): valorNormalizado nulo, mantém motivo estrutural, não recebe RN-009")
    void valorEstruturalmenteInvalido_naoAvaliaRn009() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": "150,00", "tem_nota_fiscal": false }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);
        assertNull(item.valorNormalizado());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.VALOR, avaliado.motivos().get(0).campo());
        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
    }

    @Test
    @DisplayName("RN-009 caso 10 — tem_nota_fiscal \"sim\": mantém motivo estrutural, getTemNotaFiscal nulo, não recebe RN-009")
    void notaFiscalEstruturalmenteInvalida_naoAvaliaRn009() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": 150.00, "tem_nota_fiscal": "sim" }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);
        assertNull(item.item().getTemNotaFiscal());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.TEM_NOTA_FISCAL, avaliado.motivos().get(0).campo());
        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
    }

    @Test
    @DisplayName("RN-009 caso 11 — tem_nota_fiscal ausente: mantém motivo estrutural CAMPO_AUSENTE, não recebe RN-009")
    void notaFiscalAusenteDoObjeto_naoAvaliaRn009() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": 150.00 }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);
        assertNull(item.item().getTemNotaFiscal());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_AUSENTE, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.TEM_NOTA_FISCAL, avaliado.motivos().get(0).campo());
        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
    }

    @Test
    @DisplayName("RN-009 caso 11b — tem_nota_fiscal nulo: mantém motivo estrutural CAMPO_AUSENTE, não recebe RN-009")
    void notaFiscalNula_naoAvaliaRn009() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": 150.00, "tem_nota_fiscal": null }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);
        assertNull(item.item().getTemNotaFiscal());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_AUSENTE, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.TEM_NOTA_FISCAL, avaliado.motivos().get(0).campo());
        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
    }

    @Test
    @DisplayName("RN-009 caso 12 — fornecedor de tipo inválido com valor 150.00 e nota false: mantém motivo estrutural de fornecedor e também recebe NOTA_FISCAL_AUSENTE")
    void erroEstruturalEmCampoNaoDependente_naoBloqueiaRn009() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": 123, "valor": 150.00, "tem_nota_fiscal": false }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_TIPO_INVALIDO, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.FORNECEDOR, avaliado.motivos().get(0).campo());
        assertTrue(avaliado.motivos().contains(notaFiscalAusente()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 13 — ID_DUPLICADO com valor acima do gatilho sem nota: preserva ID_DUPLICADO e acrescenta NOTA_FISCAL_AUSENTE, nessa ordem, inelegível")
    void idDuplicadoComNotaAusente_preservaEAcrescentaMotivo() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-100", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 150.00, "tem_nota_fiscal": false },
                    { "id": "d-100", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        List<ItemValidado> validados = validar(json);
        List<ItemValidado> comIdDuplicado = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(comIdDuplicado);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados);

        ItemAvaliado primeiro = avaliados.get(0);
        assertEquals(2, primeiro.motivos().size());
        assertEquals(MotivoCodigo.ID_DUPLICADO, primeiro.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, primeiro.motivos().get(1).codigo());
        assertFalse(primeiro.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 14 — categoria coworking com nota ausente: recebe CATEGORIA_FORA_POLITICA seguido de NOTA_FISCAL_AUSENTE")
    void categoriaForaPoliticaComNotaAusente_recebeAmbosMotivosNaOrdem() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "coworking",
                      "descricao": "A", "fornecedor": "F1", "valor": 150.00, "tem_nota_fiscal": false }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, avaliado.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, avaliado.motivos().get(1).codigo());
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 15 — data fora da competência com nota ausente: recebe FORA_COMPETENCIA seguido de NOTA_FISCAL_AUSENTE")
    void dataForaDaCompetenciaComNotaAusente_recebeAmbosMotivosNaOrdem() {
        String json = """
                {
                  %s
                  "despesas": [
                    { "id": "d-001", "data": "2026-04-15", "categoria": "alimentacao",
                      "descricao": "A", "fornecedor": "F1", "valor": 150.00, "tem_nota_fiscal": false }
                  ]
                }
                """.formatted(JANELA_JULHO);
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, avaliado.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, avaliado.motivos().get(1).codigo());
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 16 — exemplo normativo de três motivos: CATEGORIA_FORA_POLITICA, FORA_COMPETENCIA, NOTA_FISCAL_AUSENTE, nessa ordem exata")
    void exemploNormativoDeTresMotivos_ordemExata() {
        String json = """
                {
                  %s
                  "despesas": [
                    { "id": "d-001", "data": "2026-04-15", "categoria": "coworking",
                      "descricao": "A", "fornecedor": "F1", "valor": 500.00, "tem_nota_fiscal": false }
                  ]
                }
                """.formatted(JANELA_JULHO);
        Envelope envelope = envelope(json);
        ItemNormalizado item = normalizarDespesas(envelope).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope);

        assertEquals(3, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, avaliado.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, avaliado.motivos().get(1).codigo());
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, avaliado.motivos().get(2).codigo());
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 17 — exemplo normativo de exclusão: -500.00, coworking, sem nota: VALOR_NAO_POSITIVO e CATEGORIA_FORA_POLITICA, nunca NOTA_FISCAL_AUSENTE")
    void valorNegativoComCategoriaForaEsemNota_naoRecebeNotaFiscalAusente() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "coworking",
                      "descricao": "A", "fornecedor": "F1", "valor": -500.00, "tem_nota_fiscal": false }
                  ]
                }
                """;
        ItemNormalizado item = normalizar(json).get(0);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(2, avaliado.motivos().size());
        assertEquals(MotivoCodigo.VALOR_NAO_POSITIVO, avaliado.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, avaliado.motivos().get(1).codigo());
        assertFalse(avaliado.motivos().contains(notaFiscalAusente()));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("RN-009 caso 18a — avaliarLista(itens) preserva ordem, indiceEntrada, referência e é não modificável")
    void avaliarLista_semEnvelope_preservaOrdemIndiceENaoModificavel() {
        String json = """
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 100.01, "tem_nota_fiscal": false },
                    { "id": "d-002", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """;
        List<ItemNormalizado> normalizados = normalizar(json);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados);

        assertEquals(2, avaliados.size());
        assertEquals(1, avaliados.get(0).itemNormalizado().item().getIndiceEntrada());
        assertEquals(2, avaliados.get(1).itemNormalizado().item().getIndiceEntrada());
        assertSame(normalizados.get(0), avaliados.get(0).itemNormalizado());
        assertSame(normalizados.get(1), avaliados.get(1).itemNormalizado());

        assertThrows(UnsupportedOperationException.class, () -> avaliados.add(avaliados.get(0)));
        assertThrows(UnsupportedOperationException.class,
                () -> avaliados.get(0).motivos().add(notaFiscalAusente()));
    }

    @Test
    @DisplayName("RN-009 caso 18b — avaliarLista(itens, envelope) preserva ordem, indiceEntrada, referência e é não modificável")
    void avaliarLista_comEnvelope_preservaOrdemIndiceENaoModificavel() {
        String json = """
                {
                  %s
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 100.01, "tem_nota_fiscal": false },
                    { "id": "d-002", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """.formatted(JANELA_JULHO);
        Envelope envelope = envelope(json);
        List<ItemNormalizado> normalizados =
                normalizarDespesas(envelope);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);

        assertEquals(2, avaliados.size());
        assertEquals(1, avaliados.get(0).itemNormalizado().item().getIndiceEntrada());
        assertEquals(2, avaliados.get(1).itemNormalizado().item().getIndiceEntrada());
        assertSame(normalizados.get(0), avaliados.get(0).itemNormalizado());
        assertSame(normalizados.get(1), avaliados.get(1).itemNormalizado());

        assertThrows(UnsupportedOperationException.class, () -> avaliados.add(avaliados.get(0)));
        assertThrows(UnsupportedOperationException.class,
                () -> avaliados.get(0).motivos().add(notaFiscalAusente()));
    }

    @Test
    @DisplayName("RN-009 caso 19 — reaplicação não duplica NOTA_FISCAL_AUSENTE")
    void reaplicacao_naoDuplicaMotivo() {
        ItemNormalizado item = normalizar(itemUnico("100.01", "false")).get(0);

        ItemAvaliado primeiraAplicacao = AvaliadorRegrasIndividuais.avaliar(item);
        ItemAvaliado segundaAplicacao = AvaliadorRegrasIndividuais.avaliar(item);

        assertEquals(1, primeiraAplicacao.motivos().size());
        assertEquals(1, segundaAplicacao.motivos().size());
        assertEquals(primeiraAplicacao.motivos(), segundaAplicacao.motivos());
    }
}
