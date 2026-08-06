package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-016 / CA-010, na parte de campos fora do contrato (spec 3, 4.1,
 * 4.2, 6 AMB-015/CONTRATO, 8.2, 8.4): campos desconhecidos — em
 * {@code despesa}, em {@code colaborador} ou na raiz do documento — são
 * ignorados silenciosamente. Não provocam recusa, não criam motivos, não
 * alteram normalização, elegibilidade ou teto. Cada cenário compara uma
 * versão-base sem o campo desconhecido contra uma variante com o campo,
 * rodando o pipeline real (T-004 a T-014) até o agregador correspondente.
 */
@DisplayName("Campos desconhecidos são ignorados — RN-016 / CA-010")
class CamposDesconhecidosTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final String JANELA_JULHO =
            "\"periodo\": { \"inicio\": \"2026-07-01\", \"fim\": \"2026-07-31\" }";

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

    // ---- Construção de JSON --------------------------------------------------

    /** Item de alimentação de R$ 70,00, cortado pelo teto de R$ 60,00 (RN-011). */
    private static String itemAlimentacao(String camposExtrasJson) {
        StringBuilder json = new StringBuilder();
        json.append("{ \"id\": \"d-001\", \"data\": \"2026-07-03\", \"categoria\": \"alimentacao\", ");
        json.append("\"descricao\": \"Almoco com cliente\", \"fornecedor\": \"Restaurante Sabor\", ");
        json.append("\"valor\": 70.00, \"tem_nota_fiscal\": true");
        if (!camposExtrasJson.isEmpty()) {
            json.append(", ").append(camposExtrasJson);
        }
        json.append(" }");
        return json.toString();
    }

    /** Item de alimentação sem a chave "valor" — fronteira para CAMPO_AUSENTE/RN-002. */
    private static String itemAlimentacaoSemValor(String camposExtrasJson) {
        StringBuilder json = new StringBuilder();
        json.append("{ \"id\": \"d-001\", \"data\": \"2026-07-03\", \"categoria\": \"alimentacao\", ");
        json.append("\"descricao\": \"Almoco com cliente\", \"fornecedor\": \"Restaurante Sabor\", ");
        json.append("\"tem_nota_fiscal\": true");
        if (!camposExtrasJson.isEmpty()) {
            json.append(", ").append(camposExtrasJson);
        }
        json.append(" }");
        return json.toString();
    }

    /** Item de hospedagem de R$ 480,00, cortado pelo teto de R$ 250,00 (RN-013). */
    private static String itemHospedagem(String camposExtrasJson) {
        StringBuilder json = new StringBuilder();
        json.append("{ \"id\": \"d-001\", \"data\": \"2026-07-14\", \"categoria\": \"hospedagem\", ");
        json.append("\"descricao\": \"Estadia\", \"fornecedor\": \"Hotel Central\", ");
        json.append("\"valor\": 480.00, \"tem_nota_fiscal\": true");
        if (!camposExtrasJson.isEmpty()) {
            json.append(", ").append(camposExtrasJson);
        }
        json.append(" }");
        return json.toString();
    }

    private static String envelopeComItem(String itemJson, String extraRaizJson, String colaboradorJson) {
        List<String> camposRaiz = new ArrayList<>();
        if (!colaboradorJson.isEmpty()) {
            camposRaiz.add("\"colaborador\": " + colaboradorJson);
        }
        camposRaiz.add(JANELA_JULHO);
        if (!extraRaizJson.isEmpty()) {
            camposRaiz.add(extraRaizJson);
        }
        camposRaiz.add("\"despesas\": [ " + itemJson + " ]");
        return "{ " + String.join(", ", camposRaiz) + " }";
    }

    private static String envelopeComItem(String itemJson) {
        return envelopeComItem(itemJson, "", "");
    }

    // ---- Pipeline real obrigatório ---------------------------------------------

    private static List<ItemValidado> validados(String json) {
        Envelope envelope = envelope(json);
        return ValidadorItem.validarLista(envelope.getDespesas());
    }

    private static List<ItemAvaliado> elegiveisParaTetos(String json) {
        Envelope envelope = envelope(json);
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(idsVerificados);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);
        return SeletorElegiveis.selecionar(aposDuplicidade);
    }

    private static ResultadoTeto resultadoDiarioUnico(String json) {
        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(elegiveisParaTetos(json));
        assertEquals(1, resultados.size());
        return resultados.get(0);
    }

    private static ResultadoTeto resultadoHospedagemUnico(String json) {
        List<ResultadoTeto> resultados = AgregadorTetoHospedagem.aplicar(elegiveisParaTetos(json));
        assertEquals(1, resultados.size());
        return resultados.get(0);
    }

    private static void assertResultadoBaseAlimentacao(ResultadoTeto resultado) {
        assertEquals(new BigDecimal("60.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(1, resultado.motivos().size());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_DIARIO_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_011, motivo.regra());
        assertNull(motivo.campo());
    }

    private static void assertResultadoBaseHospedagem(ResultadoTeto resultado) {
        assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(1, resultado.motivos().size());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_013, motivo.regra());
        assertNull(motivo.campo());
    }

    // ---- 0. Cenário-base (referência de comparação para os testes seguintes) ------------

    @Test
    @DisplayName("0 — cenário-base sem campo desconhecido: alimentação de R$ 70,00 rende R$ 60,00 por RN-011")
    void cenarioBaseSemCampoDesconhecido() {
        String json = envelopeComItem(itemAlimentacao(""));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 1. Campo desconhecido simples em despesa -------------------------------------

    @Test
    @DisplayName("1 — campo desconhecido \"projeto\" (texto) em despesa é ignorado, resultado idêntico ao base")
    void campoDesconhecidoTextoEmDespesa_ignorado() {
        String json = envelopeComItem(itemAlimentacao("\"projeto\": \"Apollo\""));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 2. Campo desconhecido booleano em despesa -------------------------------------

    @Test
    @DisplayName("2 — campo desconhecido \"urgente\" (booleano) em despesa é ignorado, resultado idêntico ao base")
    void campoDesconhecidoBooleanoEmDespesa_ignorado() {
        String json = envelopeComItem(itemAlimentacao("\"urgente\": true"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 3. Campo desconhecido numérico em despesa (hospedagem) -------------------------

    @Test
    @DisplayName("3 — campo desconhecido \"quantidade_diarias\" (numérico) não multiplica o teto de hospedagem")
    void campoDesconhecidoNumericoEmHospedagem_naoMultiplicaTeto() {
        String json = envelopeComItem(itemHospedagem("\"quantidade_diarias\": 9"));
        assertResultadoBaseHospedagem(resultadoHospedagemUnico(json));
    }

    // ---- 4. Campo desconhecido como objeto -----------------------------------------------

    @Test
    @DisplayName("4 — campo desconhecido \"dados_viagem\" (objeto, contendo em_viagem) é ignorado por inteiro")
    void campoDesconhecidoObjetoEmDespesa_ignoradoPorInteiro() {
        String camposExtras = "\"dados_viagem\": { \"origem\": \"Sao Paulo\", \"destino\": \"Recife\", "
                + "\"em_viagem\": true }";
        String json = envelopeComItem(itemAlimentacao(camposExtras));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 5. Campo desconhecido como lista -----------------------------------------------

    @Test
    @DisplayName("5 — campo desconhecido \"tags\" (lista) é ignorado, resultado idêntico ao base")
    void campoDesconhecidoListaEmDespesa_ignorado() {
        String json = envelopeComItem(
                itemAlimentacao("\"tags\": [\"viagem\", \"hotel\", \"aeroporto\"]"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 6. Campo desconhecido em colaborador --------------------------------------------

    @Test
    @DisplayName("6 — campo desconhecido \"cargo\" em colaborador não altera os três metadados conhecidos nem o resultado financeiro")
    void campoDesconhecidoEmColaborador_naoAlteraMetadadosNemResultado() {
        String colaborador = "{ \"id\": \"c-001\", \"nome\": \"Pessoa\", \"centro_custo\": \"CC-01\", "
                + "\"cargo\": \"Engenheiro\" }";
        String json = envelopeComItem(itemAlimentacao(""), "", colaborador);

        Envelope envelope = envelope(json);
        assertEquals("c-001", envelope.getColaboradorId());
        assertEquals("Pessoa", envelope.getColaboradorNome());
        assertEquals("CC-01", envelope.getColaboradorCentroCusto());

        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 7. Objeto desconhecido em colaborador ---------------------------------------------

    @Test
    @DisplayName("7 — objeto desconhecido \"preferencias\" (contendo em_viagem) em colaborador não é consultado por regra alguma")
    void objetoDesconhecidoEmColaborador_naoConsultado() {
        String colaborador = "{ \"id\": \"c-001\", \"nome\": \"Pessoa\", \"centro_custo\": \"CC-01\", "
                + "\"preferencias\": { \"limite_extra\": 9999, \"em_viagem\": true } }";
        String json = envelopeComItem(itemAlimentacao(""), "", colaborador);

        Envelope envelope = envelope(json);
        assertEquals("c-001", envelope.getColaboradorId());
        assertEquals("Pessoa", envelope.getColaboradorNome());
        assertEquals("CC-01", envelope.getColaboradorCentroCusto());

        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 8. Campo desconhecido na raiz -------------------------------------------------------

    @Test
    @DisplayName("8 — campo desconhecido \"configuracao\" (contendo ampliar_limites) na raiz não altera o resultado")
    void campoDesconhecidoNaRaiz_ignorado() {
        String extraRaiz = "\"configuracao\": { \"ampliar_limites\": true, \"percentual\": 50 }";
        String json = envelopeComItem(itemAlimentacao(""), extraRaiz, "");
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 9. Campos desconhecidos simultâneos -------------------------------------------------

    @Test
    @DisplayName("9 — campos desconhecidos simultâneos na raiz, em colaborador e na despesa não somam efeito algum")
    void camposDesconhecidosSimultaneos_semEfeitoAcumulado() {
        String extraRaiz = "\"configuracao\": { \"ampliar_limites\": true, \"percentual\": 50 }";
        String colaborador = "{ \"id\": \"c-001\", \"nome\": \"Pessoa\", \"centro_custo\": \"CC-01\", "
                + "\"em_viagem\": true, \"cargo\": \"Engenheiro\" }";
        String item = itemAlimentacao("\"em_viagem\": true, \"projeto\": \"Apollo\", \"tags\": [\"viagem\"]");
        String json = envelopeComItem(item, extraRaiz, colaborador);

        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        assertEquals(1, elegiveis.size(), "o item continua elegível apesar dos campos desconhecidos");
        assertTrue(elegiveis.get(0).motivos().isEmpty(), "nenhum motivo adicional deve aparecer nesta etapa");

        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 10. Campo conhecido continua obrigatório (fronteira) ------------------------------------

    @Test
    @DisplayName("10 — fronteira: campos desconhecidos não suprem a ausência de despesa.valor, item continua recusado por CAMPO_AUSENTE/RN-002")
    void camposDesconhecidosNaoSupremCampoObrigatorioAusente() {
        String camposExtras = "\"projeto\": \"Apollo\", \"urgente\": true, \"em_viagem\": true, "
                + "\"quantidade_diarias\": 9";
        String json = envelopeComItem(itemAlimentacaoSemValor(camposExtras));

        List<ItemValidado> validados = validados(json);
        assertEquals(1, validados.size());
        ItemValidado item = validados.get(0);

        Motivo esperado = new Motivo(MotivoCodigo.CAMPO_AUSENTE, RegraNegocio.RN_002, CampoCanonico.VALOR);
        assertTrue(item.getMotivos().contains(esperado),
                "campo desconhecido não substitui nem supre a ausência do campo obrigatório despesa.valor");
        assertNull(item.getValor(), "valor estruturalmente inválido/ausente permanece nulo");
    }
}
