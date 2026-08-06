package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.Decisao;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.modelo.TabelaCambio;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Cobre RN-016 / CA-010 (spec 3, 5, 6 AMB-006/AMB-007/AMB-015, 8.1, 8.2): a
 * ampliação por viagem não produz efeito algum nesta versão. Nenhuma
 * despesa é identificada como "em viagem" a partir de {@code descricao},
 * {@code fornecedor}, categoria, existência de hospedagem ou de um eventual
 * campo desconhecido {@code em_viagem}, em qualquer nível do documento.
 * Cada cenário roda o pipeline real já implementado até o agregador de teto
 * correspondente (T-004 a T-014) e compara o resultado financeiro contra o
 * cenário-base, nunca contra um orquestrador ou compositor — que ainda não
 * existem (T-016).
 */
@DisplayName("Viagem sem efeito — RN-016 / CA-010")
class RegraViagemEfeitoNuloTest {

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

    /**
     * Item de alimentação de R$ 70,00 — valor escolhido porque, sem
     * ampliação, o teto de R$ 60,00 corta o item; uma ampliação indevida
     * mudaria o valor ou a decisão.
     */
    private static String itemAlimentacao(String descricao, String fornecedor, String camposExtrasJson) {
        StringBuilder json = new StringBuilder();
        json.append("{ \"id\": \"d-001\", \"data\": \"2026-07-03\", \"categoria\": \"alimentacao\", ");
        json.append("\"descricao\": \"").append(descricao).append("\", ");
        json.append("\"fornecedor\": \"").append(fornecedor).append("\", ");
        json.append("\"valor\": 70.00, \"tem_nota_fiscal\": true");
        if (!camposExtrasJson.isEmpty()) {
            json.append(", ").append(camposExtrasJson);
        }
        json.append(" }");
        return json.toString();
    }

    private static String itemAlimentacao(String descricao, String fornecedor) {
        return itemAlimentacao(descricao, fornecedor, "");
    }

    private static String itemHospedagem(String id, String data, String descricao, String fornecedor,
                                          String valorJson) {
        return "{ \"id\": \"" + id + "\", \"data\": \"" + data + "\", \"categoria\": \"hospedagem\", "
                + "\"descricao\": \"" + descricao + "\", \"fornecedor\": \"" + fornecedor + "\", "
                + "\"valor\": " + valorJson + ", \"tem_nota_fiscal\": true }";
    }

    /** Item de alimentação em moeda estrangeira — usado nos cenários CA-028. */
    private static String itemAlimentacaoMoeda(String id, String data, String descricao, String fornecedor,
                                                String valorJson, String moeda) {
        return "{ \"id\": \"" + id + "\", \"data\": \"" + data + "\", \"categoria\": \"alimentacao\", "
                + "\"descricao\": \"" + descricao + "\", \"fornecedor\": \"" + fornecedor + "\", "
                + "\"valor\": " + valorJson + ", \"moeda\": \"" + moeda + "\", \"tem_nota_fiscal\": true }";
    }

    /** Item de hospedagem em moeda estrangeira — usado nos cenários CA-028. */
    private static String itemHospedagemMoeda(String id, String data, String descricao, String fornecedor,
                                               String valorJson, String moeda) {
        return "{ \"id\": \"" + id + "\", \"data\": \"" + data + "\", \"categoria\": \"hospedagem\", "
                + "\"descricao\": \"" + descricao + "\", \"fornecedor\": \"" + fornecedor + "\", "
                + "\"valor\": " + valorJson + ", \"moeda\": \"" + moeda + "\", \"tem_nota_fiscal\": true }";
    }

    private static String envelopeComItens(String extraRaizJson, String colaboradorJson, String... itensJson) {
        List<String> camposRaiz = new ArrayList<>();
        if (!colaboradorJson.isEmpty()) {
            camposRaiz.add("\"colaborador\": " + colaboradorJson);
        }
        camposRaiz.add(JANELA_JULHO);
        if (!extraRaizJson.isEmpty()) {
            camposRaiz.add(extraRaizJson);
        }
        camposRaiz.add("\"despesas\": [ " + String.join(", ", itensJson) + " ]");
        return "{ " + String.join(", ", camposRaiz) + " }";
    }

    private static String envelopeComItem(String itemJson) {
        return envelopeComItens("", "", itemJson);
    }

    // ---- Pipeline real obrigatório ---------------------------------------------

    private static List<ItemAvaliado> elegiveisParaTetos(String json) {
        return elegiveisParaTetos(json, CambioTesteSupport.TABELA_BRL);
    }

    private static List<ItemAvaliado> elegiveisParaTetos(String json, TabelaCambio cambio) {
        Envelope envelope = envelope(json);
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(idsVerificados, cambio);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);
        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);
        return SeletorElegiveis.selecionar(aposDuplicidade);
    }

    private static ResultadoTeto resultadoDiarioUnico(String json) {
        return resultadoDiarioUnico(json, CambioTesteSupport.TABELA_BRL);
    }

    private static ResultadoTeto resultadoDiarioUnico(String json, TabelaCambio cambio) {
        List<ResultadoTeto> resultados = AgregadorTetoDiario.aplicar(elegiveisParaTetos(json, cambio));
        assertEquals(1, resultados.size(), "cenário deve produzir exatamente um item elegível de teto diário");
        return resultados.get(0);
    }

    private static ResultadoTeto resultadoHospedagemUnico(String json) {
        List<ResultadoTeto> resultados = AgregadorTetoHospedagem.aplicar(elegiveisParaTetos(json));
        assertEquals(1, resultados.size(), "cenário deve produzir exatamente um item elegível de teto de hospedagem");
        return resultados.get(0);
    }

    /**
     * Tabela de câmbio explícita para os cenários CA-028: moeda base BRL,
     * uma única cotação de EUR na data usada pelos cenários deste teste.
     */
    private static TabelaCambio tabelaComEur(LocalDate data, BigDecimal taxa) {
        NavigableMap<LocalDate, BigDecimal> cotacoesEur = new TreeMap<>();
        cotacoesEur.put(data, taxa);
        return new TabelaCambio("BRL", Map.of("EUR", cotacoesEur));
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

    // ---- 1. Descrição neutra ----------------------------------------------------

    @Test
    @DisplayName("1 — cenário-base: descrição neutra, R$ 70,00 de alimentação renderiza R$ 60,00 por RN-011")
    void descricaoNeutra_cenarioBase() {
        String json = envelopeComItem(itemAlimentacao("Almoco com cliente", "Restaurante Sabor"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 2. Descrição contendo "aeroporto" --------------------------------------

    @Test
    @DisplayName("2 — descrição contendo \"aeroporto\" não amplia o teto nem altera o resultado")
    void descricaoComAeroporto_resultadoIdenticoAoBase() {
        String json = envelopeComItem(itemAlimentacao("Almoco no aeroporto", "Restaurante Sabor"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 3. Descrição contendo "hotel" -------------------------------------------

    @Test
    @DisplayName("3 — descrição contendo \"hotel\" não amplia o teto nem altera o resultado")
    void descricaoComHotel_resultadoIdenticoAoBase() {
        String json = envelopeComItem(itemAlimentacao("Almoco no hotel", "Restaurante Sabor"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 4. Outras expressões semanticamente sugestivas --------------------------

    @Test
    @DisplayName("4a — descrição \"Despesa durante viagem\" não amplia o teto nem altera o resultado")
    void descricaoDespesaDuranteViagem_resultadoIdenticoAoBase() {
        String json = envelopeComItem(itemAlimentacao("Despesa durante viagem", "Restaurante Sabor"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    @Test
    @DisplayName("4b — descrição \"Refeicao em outra cidade\" não amplia o teto nem altera o resultado")
    void descricaoRefeicaoEmOutraCidade_resultadoIdenticoAoBase() {
        String json = envelopeComItem(itemAlimentacao("Refeicao em outra cidade", "Restaurante Sabor"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 5. Fornecedor contendo "aeroporto" ---------------------------------------

    @Test
    @DisplayName("5 — fornecedor \"Restaurante Aeroporto\" (descrição neutra) não amplia o teto")
    void fornecedorComAeroporto_resultadoIdenticoAoBase() {
        String json = envelopeComItem(itemAlimentacao("Almoco com cliente", "Restaurante Aeroporto"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 6. Fornecedor contendo "hotel" ---------------------------------------------

    @Test
    @DisplayName("6 — fornecedor \"Hotel Central\" num item de alimentação não vira hospedagem nem amplia o teto")
    void fornecedorComHotel_naoTransformaEmHospedagem() {
        String json = envelopeComItem(itemAlimentacao("Almoco com cliente", "Hotel Central"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 7. Campo em_viagem: true no item -----------------------------------------

    @Test
    @DisplayName("7 — campo desconhecido em_viagem: true na despesa é ignorado, resultado idêntico ao base")
    void campoEmViagemVerdadeiroNaDespesa_ignorado() {
        String json = envelopeComItem(
                itemAlimentacao("Almoco com cliente", "Restaurante Sabor", "\"em_viagem\": true"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 8. Campo em_viagem: false no item -----------------------------------------

    @Test
    @DisplayName("8 — campo desconhecido em_viagem: false na despesa também é ignorado, resultado idêntico ao base")
    void campoEmViagemFalsoNaDespesa_ignorado() {
        String json = envelopeComItem(
                itemAlimentacao("Almoco com cliente", "Restaurante Sabor", "\"em_viagem\": false"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 9. Campo em_viagem na raiz --------------------------------------------------

    @Test
    @DisplayName("9 — campo desconhecido em_viagem: true na raiz do documento não altera o resultado do item")
    void campoEmViagemNaRaiz_ignorado() {
        String json = envelopeComItens(
                "\"em_viagem\": true", "", itemAlimentacao("Almoco com cliente", "Restaurante Sabor"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 10. Campo em_viagem dentro de colaborador -------------------------------------

    @Test
    @DisplayName("10 — campo desconhecido em_viagem dentro de colaborador não altera o resultado do item")
    void campoEmViagemDentroDeColaborador_ignorado() {
        String colaborador = "{ \"id\": \"c-001\", \"nome\": \"Pessoa\", \"centro_custo\": \"CC-01\", "
                + "\"em_viagem\": true }";
        String json = envelopeComItens(
                "", colaborador, itemAlimentacao("Almoco com cliente", "Restaurante Sabor"));
        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json));
    }

    // ---- 11. Categoria não é inferida pela descrição -------------------------------------

    @Test
    @DisplayName("11 — descrição \"Hospedagem no Hotel Central\" num item categoria=alimentacao continua alimentação, nunca hospedagem")
    void descricaoMencionandoHospedagem_categoriaContinuaAlimentacao() {
        String json = envelopeComItem(itemAlimentacao("Hospedagem no Hotel Central", "Restaurante Sabor"));

        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        assertEquals(1, elegiveis.size());
        assertEquals("alimentacao", elegiveis.get(0).itemNormalizado().categoriaNormalizada(),
                "a categoria estruturada é a fonte da categoria — texto livre não a substitui");

        ResultadoTeto resultado = resultadoDiarioUnico(json);
        assertResultadoBaseAlimentacao(resultado);
        for (Motivo motivo : resultado.motivos()) {
            assertNotEquals(RegraNegocio.RN_013, motivo.regra(),
                    "item de alimentação nunca deve receber o motivo de teto de hospedagem (RN-013)");
        }

        List<ResultadoTeto> resultadosHospedagem = AgregadorTetoHospedagem.aplicar(elegiveisParaTetos(json));
        assertEquals(0, resultadosHospedagem.size(),
                "o item de alimentação não deve ser alcançado pelo agregador de hospedagem");
    }

    // ---- 12. Hospedagem não ativa viagem para outro item ------------------------------------

    @Test
    @DisplayName("12 — presença de uma hospedagem elegível não amplia o teto diário de um item de alimentação distinto")
    void hospedagemNoMesmoArquivo_naoAmpliaTetoDeOutroItem() {
        String json = envelopeComItens("", "",
                itemAlimentacao("Almoco com cliente", "Restaurante Sabor"),
                itemHospedagem("d-002", "2026-07-14", "Estadia corporativa", "Hotel Central", "480.00"));

        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json);
        assertEquals(2, elegiveis.size(), "alimentação e hospedagem não devem colidir por duplicidade econômica");

        List<ResultadoTeto> resultadosDiario = AgregadorTetoDiario.aplicar(elegiveis);
        assertEquals(1, resultadosDiario.size());
        assertResultadoBaseAlimentacao(resultadosDiario.get(0));

        List<ResultadoTeto> resultadosHospedagem = AgregadorTetoHospedagem.aplicar(elegiveis);
        assertEquals(1, resultadosHospedagem.size());
        ResultadoTeto hospedagem = resultadosHospedagem.get(0);
        assertEquals(new BigDecimal("250.00"), hospedagem.valorReembolsavel(),
                "a hospedagem é tratada isoladamente pelo agregador próprio, sem interferência do outro item");
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, hospedagem.decisao());
        assertEquals(RegraNegocio.RN_013, hospedagem.motivos().get(0).regra());
    }

    // ---- 13. Hospedagem também não recebe ampliação -----------------------------------------

    @Test
    @DisplayName("13 — CA-010: hospedagem de R$ 480,00 com descrição sugestiva de viagem rende R$ 250,00, nunca ampliado")
    void hospedagemComDescricaoSugestiva_naoRecebeAmpliacao() {
        String json = envelopeComItem(
                itemHospedagem("d-001", "2026-07-14", "Viagem corporativa - 3 noites", "Hotel Central", "480.00"));

        ResultadoTeto resultado = resultadoHospedagemUnico(json);
        assertEquals(new BigDecimal("250.00"), resultado.valorReembolsavel());
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resultado.decisao());
        assertEquals(1, resultado.motivos().size());
        Motivo motivo = resultado.motivos().get(0);
        assertEquals(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, motivo.codigo());
        assertEquals(RegraNegocio.RN_013, motivo.regra());
        assertNull(motivo.campo());
    }

    // ---- 14. CA-028: moeda estrangeira não amplia teto ---------------------------------------

    @Test
    @DisplayName("14 — CA-028: item elegível em EUR com cotação válida não amplia o teto diário, resultado idêntico ao cenário-base BRL")
    void moedaEstrangeiraComCotacaoValida_naoAmpliaTetoDiario() {
        LocalDate data = LocalDate.of(2026, 7, 3);
        TabelaCambio tabelaEur = tabelaComEur(data, new BigDecimal("2.00"));
        String json = envelopeComItem(
                itemAlimentacaoMoeda("d-001", "2026-07-03", "Almoco com cliente", "Restaurante Sabor",
                        "35.00", "EUR"));

        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json, tabelaEur));
    }

    // ---- 15. CA-028: moeda estrangeira não afeta outro item do mesmo dia --------------------------

    @Test
    @DisplayName("15 — CA-028: hospedagem em EUR não amplia teto próprio nem afeta o teto diário de um item de alimentação distinto do mesmo período")
    void moedaEstrangeiraEmHospedagem_naoAfetaOutroItem() {
        LocalDate dataAlimentacao = LocalDate.of(2026, 7, 3);
        LocalDate dataHospedagem = LocalDate.of(2026, 7, 14);
        NavigableMap<LocalDate, BigDecimal> cotacoesEur = new TreeMap<>();
        cotacoesEur.put(dataAlimentacao, new BigDecimal("2.00"));
        cotacoesEur.put(dataHospedagem, new BigDecimal("2.00"));
        TabelaCambio tabelaEur = new TabelaCambio("BRL", Map.of("EUR", cotacoesEur));

        String json = envelopeComItens("", "",
                itemAlimentacao("Almoco com cliente", "Restaurante Sabor"),
                itemHospedagemMoeda("d-002", "2026-07-14", "Estadia corporativa", "Hotel Central",
                        "240.00", "EUR"));

        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json, tabelaEur);
        assertEquals(2, elegiveis.size(), "alimentação (BRL) e hospedagem (EUR) não devem colidir por duplicidade econômica");

        List<ResultadoTeto> resultadosDiario = AgregadorTetoDiario.aplicar(elegiveis);
        assertEquals(1, resultadosDiario.size());
        assertResultadoBaseAlimentacao(resultadosDiario.get(0));

        List<ResultadoTeto> resultadosHospedagem = AgregadorTetoHospedagem.aplicar(elegiveis);
        assertEquals(1, resultadosHospedagem.size());
        ResultadoTeto hospedagem = resultadosHospedagem.get(0);
        assertEquals(new BigDecimal("250.00"), hospedagem.valorReembolsavel(),
                "hospedagem em EUR (240.00 x 2.00 = 480.00 convertido) segue o mesmo teto de R$250,00, sem ampliação");
        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, hospedagem.decisao());
        assertEquals(RegraNegocio.RN_013, hospedagem.motivos().get(0).regra());
    }

    // ---- 16. CA-028: trocar BRL por EUR com valor convertido equivalente não altera RN-016 --------

    @Test
    @DisplayName("16 — CA-028: trocar a moeda de um item de BRL para EUR, mantendo equivalente o valor convertido em BRL, não altera o comportamento de RN-016")
    void trocarMoedaDeBrlParaEurComValorEquivalente_resultadoIdentico() {
        LocalDate data = LocalDate.of(2026, 7, 3);
        String jsonBrl = envelopeComItem(itemAlimentacao("Almoco com cliente", "Restaurante Sabor"));
        ResultadoTeto resultadoBrl = resultadoDiarioUnico(jsonBrl);

        TabelaCambio tabelaEur = tabelaComEur(data, new BigDecimal("2.00"));
        String jsonEur = envelopeComItem(
                itemAlimentacaoMoeda("d-001", "2026-07-03", "Almoco com cliente", "Restaurante Sabor",
                        "35.00", "EUR"));
        ResultadoTeto resultadoEur = resultadoDiarioUnico(jsonEur, tabelaEur);

        assertEquals(resultadoBrl.valorReembolsavel(), resultadoEur.valorReembolsavel(),
                "valor convertido equivalente (35.00 EUR x 2.00 = 70.00) deve produzir o mesmo resultado que 70.00 BRL");
        assertEquals(resultadoBrl.decisao(), resultadoEur.decisao());
        assertEquals(resultadoBrl.motivos(), resultadoEur.motivos());
    }

    // ---- 17. CA-028: nenhuma inferência de viagem por moeda estrangeira, mesmo com texto sugestivo --

    @Test
    @DisplayName("17 — CA-028: descrição sugestiva de viagem combinada com moeda estrangeira ainda não amplia teto algum")
    void moedaEstrangeiraComDescricaoSugestiva_naoAmpliaTeto() {
        LocalDate data = LocalDate.of(2026, 7, 3);
        TabelaCambio tabelaEur = tabelaComEur(data, new BigDecimal("2.00"));
        String json = envelopeComItem(
                itemAlimentacaoMoeda("d-001", "2026-07-03", "Almoco no aeroporto", "Restaurante Sabor",
                        "35.00", "EUR"));

        assertResultadoBaseAlimentacao(resultadoDiarioUnico(json, tabelaEur));
    }
}
