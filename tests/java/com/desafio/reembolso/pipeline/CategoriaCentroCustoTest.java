package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.Periodicidade;
import com.desafio.reembolso.modelo.PoliticaExterna;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.desafio.reembolso.modelo.TabelaCambio;
import com.desafio.reembolso.modelo.TabelaCategoria;
import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * T-041 / spec RN-007, RN-009 (atualizada), RN-019, RN-020, 8.4 item 14;
 * plan §6, §19: cobre a nova sobrecarga de {@link AvaliadorRegrasIndividuais}
 * que consome {@link TabelaPoliticaResolvida} e {@link PoliticaExterna},
 * avaliando categoria exclusivamente pela tabela resolvida — nunca pelo
 * conjunto histórico fixo — e o gatilho de nota fiscal exclusivamente por
 * {@link PoliticaExterna#getNotaFiscalObrigatoriaAcimaDe()}.
 */
@DisplayName("Categoria e nota fiscal por centro de custo — RN-007, RN-009, RN-019, RN-020 (T-041)")
class CategoriaCentroCustoTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final String PERIODO_INICIO = "2026-07-01";
    private static final String PERIODO_FIM = "2026-07-31";

    private static TabelaCategoria categoria(String limite, Periodicidade periodicidade) {
        return new TabelaCategoria(new BigDecimal(limite), periodicidade);
    }

    private static PoliticaExterna politica(BigDecimal gatilhoNotaFiscal,
                                             Map<String, TabelaCategoria> padrao,
                                             Map<String, Map<String, TabelaCategoria>> centrosCusto) {
        return new PoliticaExterna(LocalDate.of(2026, 7, 1), "BRL", gatilhoNotaFiscal, padrao, centrosCusto);
    }

    private static String despesa(String id, String data, String categoria, String valor, boolean temNotaFiscal) {
        return """
                { "id": "%s", "data": "%s", "categoria": "%s", "descricao": "D",
                  "fornecedor": "F", "valor": %s, "tem_nota_fiscal": %s }
                """.formatted(id, data, categoria, valor, temNotaFiscal);
    }

    private static String despesaComMoeda(String id, String data, String categoria, String valor,
                                           String moeda, boolean temNotaFiscal) {
        return """
                { "id": "%s", "data": "%s", "categoria": "%s", "descricao": "D",
                  "fornecedor": "F", "valor": %s, "moeda": "%s", "tem_nota_fiscal": %s }
                """.formatted(id, data, categoria, valor, moeda, temNotaFiscal);
    }

    private static String despesaSemCategoria(String id, String data, String valor, boolean temNotaFiscal) {
        return """
                { "id": "%s", "data": "%s", "descricao": "D",
                  "fornecedor": "F", "valor": %s, "tem_nota_fiscal": %s }
                """.formatted(id, data, valor, temNotaFiscal);
    }

    private static Envelope envelopeDe(String centroCusto, String... despesasJson) {
        String colaborador = centroCusto == null
                ? ""
                : "\"colaborador\": { \"centro_custo\": \"%s\" },".formatted(centroCusto);
        String json = """
                {
                  %s
                  "periodo": { "inicio": "%s", "fim": "%s" },
                  "despesas": [%s]
                }
                """.formatted(colaborador, PERIODO_INICIO, PERIODO_FIM, String.join(",", despesasJson));
        JsonNode raiz;
        try {
            raiz = MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ValidadorEnvelope.validar(raiz);
    }

    private static List<ItemNormalizado> normalizar(Envelope envelope) {
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope);
        List<ItemValidado> semIdDuplicado = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(semIdDuplicado);
        return Normalizador.normalizarLista(comCambio);
    }

    private static List<ItemNormalizado> normalizar(Envelope envelope, TabelaCambio cambio) {
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope);
        List<ItemValidado> semIdDuplicado = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = CambioTesteSupport.resolverLista(semIdDuplicado, cambio);
        return Normalizador.normalizarLista(comCambio);
    }

    private static boolean contemCodigo(List<Motivo> motivos, MotivoCodigo codigo) {
        return motivos.stream().anyMatch(m -> m.codigo() == codigo);
    }

    // ---- 1. Centro desconhecido resolvido para PADRAO ---------------------------------------

    @Test
    @DisplayName("1 — centro desconhecido usa PADRAO: categoria ausente de padrao recebe CATEGORIA_FORA_POLITICA/RN-007, "
            + "nunca motivo de centro de custo")
    void centroDesconhecidoUsaPadrao_categoriaAusenteRecebeCategoriaForaPolitica() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("60.00", Periodicidade.DIA));
        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, new HashMap<>());

        Envelope envelope = envelopeDe("CC-DESCONHECIDO",
                despesa("d-001", "2026-07-10", "transporte_urbano", "50.00", true));
        ItemNormalizado item = normalizar(envelope).get(0);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);
        assertEquals(TabelaPoliticaResolvida.Origem.PADRAO, tabela.getOrigem());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertEquals(1, avaliado.motivos().size());
        Motivo motivo = avaliado.motivos().get(0);
        assertEquals(MotivoCodigo.CATEGORIA_FORA_POLITICA, motivo.codigo());
        assertEquals(RegraNegocio.RN_007, motivo.regra());
        assertNull(motivo.campo());
        assertFalse(contemCodigo(avaliado.motivos(), MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO));
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());
    }

    // ---- 2. Centro cadastrado sem a categoria ------------------------------------------------

    @Test
    @DisplayName("2 — centro cadastrado sem a categoria: CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO/RN-019, "
            + "nunca CATEGORIA_FORA_POLITICA")
    void centroCadastradoSemCategoria_recebeMotivoDeCentroCusto() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("60.00", Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaEngenharia = new HashMap<>();
        tabelaEngenharia.put("hospedagem", categoria("250.00", Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ENG-PLATAFORMA", tabelaEngenharia);

        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, centrosCusto);

        Envelope envelope = envelopeDe("CC-ENG-PLATAFORMA",
                despesa("d-001", "2026-07-10", "alimentacao", "50.00", true));
        ItemNormalizado item = normalizar(envelope).get(0);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);
        assertEquals(TabelaPoliticaResolvida.Origem.CENTRO_CUSTO, tabela.getOrigem());

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertEquals(1, avaliado.motivos().size());
        Motivo motivo = avaliado.motivos().get(0);
        assertEquals(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, motivo.codigo());
        assertEquals(RegraNegocio.RN_019, motivo.regra());
        assertNull(motivo.campo());
        assertFalse(contemCodigo(avaliado.motivos(), MotivoCodigo.CATEGORIA_FORA_POLITICA));
        assertFalse(avaliado.elegivel());
    }

    // ---- 3. Centro cadastrado com limite zero ------------------------------------------------

    @Test
    @DisplayName("3 — centro cadastrado com limite zero: RN-019, inelegível, valorReembolsavel 0.00, nunca parcial")
    void centroCadastradoComLimiteZero_recebeMotivoEFicaInelegivel() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("hospedagem", categoria("250.00", Periodicidade.DIARIA));

        Map<String, TabelaCategoria> tabelaEngenharia = new HashMap<>();
        tabelaEngenharia.put("hospedagem", categoria("0", Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ENG-PLATAFORMA", tabelaEngenharia);

        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, centrosCusto);

        Envelope envelope = envelopeDe("CC-ENG-PLATAFORMA",
                despesa("d-001", "2026-07-10", "hospedagem", "480.00", true));
        ItemNormalizado item = normalizar(envelope).get(0);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertEquals(1, avaliado.motivos().size());
        Motivo motivo = avaliado.motivos().get(0);
        assertEquals(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, motivo.codigo());
        assertEquals(RegraNegocio.RN_019, motivo.regra());
        assertFalse(avaliado.elegivel());
        assertEquals(0, new BigDecimal("0.00").compareTo(avaliado.valorReembolsavel()),
                "limite zero recusa o item — nunca produz reembolso parcial");
    }

    // ---- 4. Centro cadastrado com limite positivo --------------------------------------------

    @Test
    @DisplayName("4 — centro cadastrado com limite positivo: categoria permitida, nenhum motivo de categoria")
    void centroCadastradoComLimitePositivo_categoriaPermitida() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("60.00", Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaEngenharia = new HashMap<>();
        tabelaEngenharia.put("alimentacao", categoria("70.00", Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ENG-PLATAFORMA", tabelaEngenharia);

        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, centrosCusto);

        Envelope envelope = envelopeDe("CC-ENG-PLATAFORMA",
                despesa("d-001", "2026-07-10", "alimentacao", "50.00", true));
        ItemNormalizado item = normalizar(envelope).get(0);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertTrue(avaliado.motivos().isEmpty());
        assertTrue(avaliado.elegivel());
        assertNull(avaliado.valorReembolsavel());
    }

    // ---- 5. Categoria dinâmica (representacao) -----------------------------------------------

    @Test
    @DisplayName("5 — representacao dinâmica com limite positivo é aceita: prova de independência de "
            + "CATEGORIAS_REEMBOLSAVEIS")
    void representacaoDinamica_aceitaQuandoPresenteComLimitePositivo() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("60.00", Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaComercial = new HashMap<>();
        tabelaComercial.put("representacao", categoria("300.00", Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-COMERCIAL", tabelaComercial);

        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, centrosCusto);

        Envelope envelope = envelopeDe("CC-COMERCIAL",
                despesa("d-001", "2026-07-10", "representacao", "190.00", true));
        ItemNormalizado item = normalizar(envelope).get(0);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertTrue(avaliado.motivos().isEmpty(),
                "representacao não pode ser recusada pelo conjunto histórico fixo, que não a reconhece");
        assertTrue(avaliado.elegivel());
    }

    // ---- 6. Ausência de fallback ---------------------------------------------------------------

    @Test
    @DisplayName("6 — ausência de fallback: padrao declara alimentacao, centro cadastrado não a declara, "
            + "nova sobrecarga produz RN-019 mesmo assim")
    void ausenciaDeFallback_naoUsaLimiteDePadrao() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("60.00", Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaAdm = new HashMap<>();
        tabelaAdm.put("transporte_urbano", categoria("80.00", Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ADM", tabelaAdm);

        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, centrosCusto);

        Envelope envelope = envelopeDe("CC-ADM",
                despesa("d-001", "2026-07-10", "alimentacao", "50.00", true));
        ItemNormalizado item = normalizar(envelope).get(0);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);
        assertFalse(tabela.getCategorias().containsKey("alimentacao"));

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, avaliado.motivos().get(0).codigo());
        assertEquals(RegraNegocio.RN_019, avaliado.motivos().get(0).regra());
        assertFalse(avaliado.elegivel());
    }

    // ---- 7/8/9. Gatilho de nota fiscal via PoliticaExterna -----------------------------------

    @Test
    @DisplayName("7 — gatilho externo mais alto que o histórico (500.00): 200.00 sem nota não recebe "
            + "NOTA_FISCAL_AUSENTE")
    void gatilhoExternoMaisAlto_naoExigeNota() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("1000.00", Periodicidade.DIA));
        PoliticaExterna politica = politica(new BigDecimal("500.00"), padrao, new HashMap<>());

        Envelope envelope = envelopeDe(null,
                despesa("d-001", "2026-07-10", "alimentacao", "200.00", false));
        ItemNormalizado item = normalizar(envelope).get(0);
        assertEquals(new BigDecimal("200.00"), item.valorNormalizado());
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertFalse(contemCodigo(avaliado.motivos(), MotivoCodigo.NOTA_FISCAL_AUSENTE));
        assertTrue(avaliado.elegivel());
    }

    @Test
    @DisplayName("8 — gatilho externo mais baixo que o histórico (50.00): 80.00 sem nota recebe "
            + "NOTA_FISCAL_AUSENTE")
    void gatilhoExternoMaisBaixo_exigeNota() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("1000.00", Periodicidade.DIA));
        PoliticaExterna politica = politica(new BigDecimal("50.00"), padrao, new HashMap<>());

        Envelope envelope = envelopeDe(null,
                despesa("d-001", "2026-07-10", "alimentacao", "80.00", false));
        ItemNormalizado item = normalizar(envelope).get(0);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertTrue(contemCodigo(avaliado.motivos(), MotivoCodigo.NOTA_FISCAL_AUSENTE));
        assertFalse(avaliado.elegivel());
    }

    @Test
    @DisplayName("9 — fronteira exata: valor normalizado igual ao gatilho externo não recebe "
            + "NOTA_FISCAL_AUSENTE")
    void fronteiraExataDoGatilhoExterno_naoExigeNota() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("1000.00", Periodicidade.DIA));
        PoliticaExterna politica = politica(new BigDecimal("75.00"), padrao, new HashMap<>());

        Envelope envelope = envelopeDe(null,
                despesa("d-001", "2026-07-10", "alimentacao", "75.00", false));
        ItemNormalizado item = normalizar(envelope).get(0);
        assertEquals(new BigDecimal("75.00"), item.valorNormalizado());
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertFalse(contemCodigo(avaliado.motivos(), MotivoCodigo.NOTA_FISCAL_AUSENTE));
        assertTrue(avaliado.elegivel());
    }

    // ---- 10. Categoria estruturalmente inválida ----------------------------------------------

    @Test
    @DisplayName("10 — categoria estruturalmente inválida (ausente): preserva motivo estrutural, não acrescenta "
            + "RN-007 nem RN-019")
    void categoriaEstruturalmenteInvalida_naoAvaliaCategoria() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("60.00", Periodicidade.DIA));
        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, new HashMap<>());

        Envelope envelope = envelopeDe(null,
                despesaSemCategoria("d-001", "2026-07-10", "50.00", true));
        ItemNormalizado item = normalizar(envelope).get(0);
        assertNull(item.categoriaNormalizada());
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertEquals(1, avaliado.motivos().size());
        assertEquals(MotivoCodigo.CAMPO_AUSENTE, avaliado.motivos().get(0).codigo());
        assertEquals(CampoCanonico.CATEGORIA, avaliado.motivos().get(0).campo());
        assertFalse(contemCodigo(avaliado.motivos(), MotivoCodigo.CATEGORIA_FORA_POLITICA));
        assertFalse(contemCodigo(avaliado.motivos(), MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO));
        assertFalse(avaliado.elegivel());
    }

    // ---- 11. Coexistência com MOEDA_SEM_COTACAO ----------------------------------------------

    @Test
    @DisplayName("11 — coexistência real de pipeline: MOEDA_SEM_COTACAO (RN-020) e "
            + "CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO (RN-019) no mesmo item")
    void coexistenciaComMoedaSemCotacao() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("60.00", Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaEngenharia = new HashMap<>();
        tabelaEngenharia.put("hospedagem", categoria("250.00", Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ENG-PLATAFORMA", tabelaEngenharia);

        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, centrosCusto);
        TabelaCambio cambioSemCotacao = new TabelaCambio("BRL", Map.of());

        Envelope envelope = envelopeDe("CC-ENG-PLATAFORMA",
                despesaComMoeda("d-001", "2026-07-10", "alimentacao", "100.00", "USD", true));
        ItemNormalizado item = normalizar(envelope, cambioSemCotacao).get(0);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        ItemAvaliado avaliado = AvaliadorRegrasIndividuais.avaliar(item, envelope, tabela, politica);

        assertNull(item.valorNormalizado());
        assertFalse(avaliado.elegivel());
        assertEquals(new BigDecimal("0.00"), avaliado.valorReembolsavel());

        assertEquals(2, avaliado.motivos().size());
        Motivo moedaSemCotacao = avaliado.motivos().get(0);
        assertEquals(MotivoCodigo.MOEDA_SEM_COTACAO, moedaSemCotacao.codigo());
        assertEquals(RegraNegocio.RN_020, moedaSemCotacao.regra());
        assertEquals(CampoCanonico.MOEDA, moedaSemCotacao.campo());

        Motivo categoriaCentroCusto = avaliado.motivos().get(1);
        assertEquals(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, categoriaCentroCusto.codigo());
        assertEquals(RegraNegocio.RN_019, categoriaCentroCusto.regra());
        assertNull(categoriaCentroCusto.campo());

        assertFalse(contemCodigo(avaliado.motivos(), MotivoCodigo.VALOR_NAO_POSITIVO));
        assertFalse(contemCodigo(avaliado.motivos(), MotivoCodigo.NOTA_FISCAL_AUSENTE));
        assertEquals(1, avaliado.motivos().stream()
                .filter(m -> m.codigo() == MotivoCodigo.MOEDA_SEM_COTACAO)
                .count(), "MOEDA_SEM_COTACAO não é duplicado");
    }

    // ---- 12. Ordem dos motivos ------------------------------------------------------------------

    @Test
    @DisplayName("12 — ordem dos motivos: existentes (ID_DUPLICADO), categoria (RN-019), competência, nota fiscal")
    void ordemDosMotivos_seguemAOrdemNormativa() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("1000.00", Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaEngenharia = new HashMap<>();
        tabelaEngenharia.put("hospedagem", categoria("250.00", Periodicidade.DIARIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-ENG-PLATAFORMA", tabelaEngenharia);

        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, centrosCusto);

        Envelope envelope = envelopeDe("CC-ENG-PLATAFORMA",
                despesa("d-100", "2026-04-15", "alimentacao", "600.00", false),
                despesa("d-100", "2026-07-05", "hospedagem", "50.00", true));
        List<ItemNormalizado> normalizados = normalizar(envelope);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(
                normalizados, envelope, tabela, politica);

        ItemAvaliado primeiro = avaliados.get(0);
        assertEquals(4, primeiro.motivos().size());
        assertEquals(MotivoCodigo.ID_DUPLICADO, primeiro.motivos().get(0).codigo());
        assertEquals(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, primeiro.motivos().get(1).codigo());
        assertEquals(MotivoCodigo.FORA_COMPETENCIA, primeiro.motivos().get(2).codigo());
        assertEquals(MotivoCodigo.NOTA_FISCAL_AUSENTE, primeiro.motivos().get(3).codigo());
        assertFalse(primeiro.elegivel());
    }

    // ---- 13. avaliarLista -----------------------------------------------------------------------

    @Test
    @DisplayName("13 — avaliarLista preserva ordem e quantidade, aplica a mesma política a todos os itens "
            + "e devolve lista não modificável")
    void avaliarLista_preservaOrdemQuantidadeEUsaMesmaPolitica() {
        Map<String, TabelaCategoria> padrao = new HashMap<>();
        padrao.put("alimentacao", categoria("60.00", Periodicidade.DIA));

        Map<String, TabelaCategoria> tabelaComercial = new HashMap<>();
        tabelaComercial.put("representacao", categoria("300.00", Periodicidade.DIA));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = new HashMap<>();
        centrosCusto.put("CC-COMERCIAL", tabelaComercial);

        PoliticaExterna politica = politica(new BigDecimal("100.00"), padrao, centrosCusto);

        Envelope envelope = envelopeDe("CC-COMERCIAL",
                despesa("d-001", "2026-07-10", "representacao", "190.00", true),
                despesa("d-002", "2026-07-11", "coworking", "80.00", true));
        List<ItemNormalizado> normalizados = normalizar(envelope);
        TabelaPoliticaResolvida tabela = ResolutorPoliticaCentroCusto.resolver(
                envelope.getColaboradorCentroCusto(), politica);

        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(
                normalizados, envelope, tabela, politica);

        assertEquals(2, avaliados.size());
        assertSame(normalizados.get(0), avaliados.get(0).itemNormalizado());
        assertSame(normalizados.get(1), avaliados.get(1).itemNormalizado());
        assertEquals(1, avaliados.get(0).itemNormalizado().item().getIndiceEntrada());
        assertEquals(2, avaliados.get(1).itemNormalizado().item().getIndiceEntrada());

        assertTrue(avaliados.get(0).elegivel(), "representacao é permitida pela tabela de CC-COMERCIAL");
        assertFalse(avaliados.get(1).elegivel(), "coworking não existe em nenhuma tabela aplicável a CC-COMERCIAL");
        assertEquals(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO,
                avaliados.get(1).motivos().get(0).codigo());

        assertThrows(UnsupportedOperationException.class, () -> avaliados.add(avaliados.get(0)));
        assertThrows(UnsupportedOperationException.class,
                () -> avaliados.get(1).motivos().add(avaliados.get(1).motivos().get(0)));
    }
}
