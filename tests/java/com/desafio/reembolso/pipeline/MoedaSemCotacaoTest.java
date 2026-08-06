package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.modelo.CampoCanonico;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre a exclusão de dependência de {@code MOEDA_SEM_COTACAO} declarada em
 * spec 8.4, item 14 (RN-020), no ponto do backlog em que T-039 acontece —
 * antes da política por centro de custo (Bloco G). Todos os cenários
 * executam os componentes reais do pipeline, na ordem de spec 8.1:
 * {@link ValidadorItem} → {@link DetectorIdDuplicado} → {@link ResolutorCambio}
 * → {@link Normalizador} → {@link AvaliadorRegrasIndividuais} (sobrecarga
 * histórica com {@link Envelope}) e, quando o cenário exige provar exclusão
 * antes da duplicidade ou dos tetos, também {@link SeletorElegiveis},
 * {@link DetectorDuplicidadeEconomica} e os agregadores históricos
 * ({@link AgregadorTetoDiario}, {@link AgregadorTetoHospedagem}). Nenhuma
 * lógica de componente é reproduzida manualmente.
 */
@DisplayName("MOEDA_SEM_COTACAO — coexistência e exclusão de motivos (8.4, item 14, RN-020)")
class MoedaSemCotacaoTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final String PERIODO_INICIO = "2026-07-01";
    private static final String PERIODO_FIM = "2026-07-31";

    private static String despesa(String id, String data, String categoria, String descricao,
                                   String fornecedor, String valor, String moeda, boolean temNotaFiscal) {
        return """
                { "id": "%s", "data": "%s", "categoria": "%s", "descricao": "%s",
                  "fornecedor": "%s", "valor": %s, "moeda": "%s", "tem_nota_fiscal": %s }
                """.formatted(id, data, categoria, descricao, fornecedor, valor, moeda, temNotaFiscal);
    }

    private static Envelope envelopeDe(String... despesasJson) {
        String json = """
                {
                  "periodo": { "inicio": "%s", "fim": "%s" },
                  "despesas": [%s]
                }
                """.formatted(PERIODO_INICIO, PERIODO_FIM, String.join(",", despesasJson));
        JsonNode raiz;
        try {
            raiz = MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ValidadorEnvelope.validar(raiz);
    }

    /** Válida, moeda base BRL, mas sem nenhuma cotação utilizável (RN-022: {@code taxas} vazio é válido). */
    private static TabelaCambio cambioSemCotacao() {
        return new TabelaCambio("BRL", Map.of());
    }

    private static List<ItemAvaliado> avaliarPipeline(Envelope envelope, TabelaCambio cambio) {
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope);
        List<ItemValidado> semIdDuplicado = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = ResolutorCambio.resolverLista(semIdDuplicado, cambio);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        return AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);
    }

    private static boolean contemCodigo(List<Motivo> motivos, MotivoCodigo codigo) {
        return motivos.stream().anyMatch(m -> m.codigo() == codigo);
    }

    private static long contagemCodigo(List<Motivo> motivos, MotivoCodigo codigo) {
        return motivos.stream().filter(m -> m.codigo() == codigo).count();
    }

    private static ItemAvaliado porId(List<ItemAvaliado> itens, String id) {
        return itens.stream()
                .filter(i -> id.equals(i.itemNormalizado().item().getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("id não encontrado: " + id));
    }

    private static boolean contemId(List<ItemAvaliado> itens, String id) {
        return itens.stream().anyMatch(i -> id.equals(i.itemNormalizado().item().getId()));
    }

    // ---- 1. Cenário-base -------------------------------------------------------------------

    @Test
    @DisplayName("1 — USD estruturalmente válido, sem cotação: derivados de câmbio e valor normalizado nulos, "
            + "motivo único MOEDA_SEM_COTACAO com codigo/regra/campo corretos")
    void cenarioBase_moedaSemCotacao() {
        Envelope envelope = envelopeDe(
                despesa("d-001", "2026-07-10", "alimentacao", "Almoco", "Restaurante X",
                        "100.00", "USD", true));
        TabelaCambio cambio = cambioSemCotacao();

        List<ItemValidado> validados = ValidadorItem.validarLista(envelope);
        List<ItemValidado> semIdDuplicado = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = ResolutorCambio.resolverLista(semIdDuplicado, cambio);

        ItemValidado resolvido = comCambio.get(0);
        assertNull(resolvido.getTaxaCambioAplicada());
        assertNull(resolvido.getDataCotacaoUtilizada());
        assertNull(resolvido.getValorConvertidoBruto());
        assertEquals(1, resolvido.getMotivos().size(), "estágio cambial produz exatamente um motivo");
        Motivo motivo = resolvido.getMotivos().get(0);
        assertEquals(MotivoCodigo.MOEDA_SEM_COTACAO, motivo.codigo());
        assertEquals(RegraNegocio.RN_020, motivo.regra());
        assertEquals(CampoCanonico.MOEDA, motivo.campo());

        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        assertNull(normalizados.get(0).valorNormalizado());
    }

    // ---- 2/3/4. Coexistência com motivos independentes ------------------------------------

    @Test
    @DisplayName("2 — MOEDA_SEM_COTACAO coexiste com CATEGORIA_FORA_POLITICA (categoria fora do vocabulário histórico)")
    void coexisteComCategoriaForaPolitica() {
        Envelope envelope = envelopeDe(
                despesa("d-001", "2026-07-10", "coworking", "Sala", "Fornecedor X",
                        "100.00", "USD", true));

        List<ItemAvaliado> avaliados = avaliarPipeline(envelope, cambioSemCotacao());
        ItemAvaliado item = avaliados.get(0);

        assertFalse(item.elegivel());
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.CATEGORIA_FORA_POLITICA));
        assertEquals(2, item.motivos().size(), "nenhum outro motivo deve surgir neste cenário");
    }

    @Test
    @DisplayName("3 — MOEDA_SEM_COTACAO coexiste com FORA_COMPETENCIA (data fora do período do envelope)")
    void coexisteComForaCompetencia() {
        Envelope envelope = envelopeDe(
                despesa("d-001", "2026-08-05", "alimentacao", "Almoco", "Restaurante X",
                        "100.00", "USD", true));

        List<ItemAvaliado> avaliados = avaliarPipeline(envelope, cambioSemCotacao());
        ItemAvaliado item = avaliados.get(0);

        assertFalse(item.elegivel());
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.FORA_COMPETENCIA));
        assertEquals(2, item.motivos().size(), "nenhum outro motivo deve surgir neste cenário");
    }

    @Test
    @DisplayName("4 — MOEDA_SEM_COTACAO coexiste simultaneamente com CATEGORIA_FORA_POLITICA e FORA_COMPETENCIA, "
            + "sem que nenhum elimine o outro")
    void coexisteComCategoriaForaPoliticaEForaCompetenciaSimultaneamente() {
        Envelope envelope = envelopeDe(
                despesa("d-001", "2026-08-05", "coworking", "Sala", "Fornecedor X",
                        "100.00", "USD", true));

        List<ItemAvaliado> avaliados = avaliarPipeline(envelope, cambioSemCotacao());
        ItemAvaliado item = avaliados.get(0);

        assertFalse(item.elegivel());
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.CATEGORIA_FORA_POLITICA));
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.FORA_COMPETENCIA));
        assertEquals(3, item.motivos().size(), "os três motivos coexistem, nenhum suprime o outro");
        assertEquals(1, contagemCodigo(item.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO),
                "MOEDA_SEM_COTACAO não é duplicado pelas avaliações subsequentes");
    }

    // ---- 5/6. Motivos que não podem coexistir ----------------------------------------------

    @Test
    @DisplayName("5 — valor negativo em moeda sem cotação: valor_normalizado permanece nulo, "
            + "VALOR_NAO_POSITIVO não é avaliado, apenas MOEDA_SEM_COTACAO")
    void valorNegativoComMoedaSemCotacao_naoRecebeValorNaoPositivo() {
        Envelope envelope = envelopeDe(
                despesa("d-001", "2026-07-10", "alimentacao", "Almoco", "Restaurante X",
                        "-50.00", "USD", true));

        List<ItemValidado> validados = ValidadorItem.validarLista(envelope);
        List<ItemValidado> comCambio = ResolutorCambio.resolverLista(
                DetectorIdDuplicado.detectar(validados), cambioSemCotacao());
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        assertNull(normalizados.get(0).valorNormalizado(), "pré-condição: sem cotação, sem valor normalizado");

        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);
        ItemAvaliado item = avaliados.get(0);

        assertFalse(item.elegivel());
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertFalse(contemCodigo(item.motivos(), MotivoCodigo.VALOR_NAO_POSITIVO),
                "RN-006 não é aplicável sem valor_normalizado calculável");
        assertEquals(List.of(new Motivo(MotivoCodigo.MOEDA_SEM_COTACAO, RegraNegocio.RN_020, CampoCanonico.MOEDA)),
                item.motivos());
    }

    @Test
    @DisplayName("6 — valor original alto, sem nota fiscal, em moeda sem cotação: valor_normalizado permanece nulo, "
            + "NOTA_FISCAL_AUSENTE não é avaliado, apenas MOEDA_SEM_COTACAO")
    void valorAltoSemNotaFiscalComMoedaSemCotacao_naoRecebeNotaFiscalAusente() {
        Envelope envelope = envelopeDe(
                despesa("d-001", "2026-07-10", "alimentacao", "Jantar", "Restaurante X",
                        "900.00", "USD", false));

        List<ItemValidado> validados = ValidadorItem.validarLista(envelope);
        List<ItemValidado> comCambio = ResolutorCambio.resolverLista(
                DetectorIdDuplicado.detectar(validados), cambioSemCotacao());
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        assertNull(normalizados.get(0).valorNormalizado(), "pré-condição: sem cotação, sem valor normalizado");

        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);
        ItemAvaliado item = avaliados.get(0);

        assertFalse(item.elegivel());
        assertTrue(contemCodigo(item.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertFalse(contemCodigo(item.motivos(), MotivoCodigo.NOTA_FISCAL_AUSENTE),
                "RN-009 não é aplicável sem valor_normalizado calculável");
        assertEquals(List.of(new Motivo(MotivoCodigo.MOEDA_SEM_COTACAO, RegraNegocio.RN_020, CampoCanonico.MOEDA)),
                item.motivos());
    }

    // ---- 7. Exclusão antes da duplicidade ---------------------------------------------------

    @Test
    @DisplayName("7 — dois itens economicamente equivalentes em moeda sem cotação são excluídos antes do "
            + "DetectorDuplicidadeEconomica; nenhum recebe DUPLICIDADE e a população não os contém")
    void exclusaoAntesDaDuplicidadeEconomica() {
        Envelope envelope = envelopeDe(
                despesa("d-101", "2026-07-10", "alimentacao", "Almoco", "Restaurante X",
                        "100.00", "USD", true),
                despesa("d-102", "2026-07-10", "alimentacao", "Almoco", "Restaurante X",
                        "100.00", "USD", true),
                despesa("d-103", "2026-07-10", "alimentacao", "Cafe", "Padaria Y",
                        "30.00", "BRL", true));

        List<ItemAvaliado> avaliados = avaliarPipeline(envelope, cambioSemCotacao());
        assertEquals(3, avaliados.size(), "nenhum item desaparece antes do primeiro SeletorElegiveis");

        ItemAvaliado d101 = porId(avaliados, "d-101");
        ItemAvaliado d102 = porId(avaliados, "d-102");
        ItemAvaliado d103 = porId(avaliados, "d-103");

        assertFalse(d101.elegivel());
        assertTrue(contemCodigo(d101.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertFalse(d102.elegivel());
        assertTrue(contemCodigo(d102.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertTrue(d103.elegivel());
        assertTrue(d103.motivos().isEmpty());

        // Snapshots imutáveis dos motivos de d101/d102 antes de qualquer seleção — usados depois
        // para provar que os estágios de duplicidade não alteram retroativamente estes objetos.
        List<Motivo> snapshotMotivosD101 = List.copyOf(d101.motivos());
        List<Motivo> snapshotMotivosD102 = List.copyOf(d102.motivos());
        assertEquals(1, contagemCodigo(snapshotMotivosD101, MotivoCodigo.MOEDA_SEM_COTACAO));
        assertEquals(1, contagemCodigo(snapshotMotivosD102, MotivoCodigo.MOEDA_SEM_COTACAO));
        assertFalse(contemCodigo(snapshotMotivosD101, MotivoCodigo.DUPLICIDADE));
        assertFalse(contemCodigo(snapshotMotivosD102, MotivoCodigo.DUPLICIDADE));

        List<ItemAvaliado> elegiveis1 = SeletorElegiveis.selecionar(avaliados);
        assertEquals(1, elegiveis1.size(), "apenas o item d-103 chega à população entregue ao detector de duplicidade");
        assertFalse(contemId(elegiveis1, "d-101"));
        assertFalse(contemId(elegiveis1, "d-102"));
        assertTrue(contemId(elegiveis1, "d-103"));

        List<ItemAvaliado> semDuplicidade = DetectorDuplicidadeEconomica.detectar(elegiveis1);
        assertEquals(1, semDuplicidade.size());
        assertFalse(contemId(semDuplicidade, "d-101"));
        assertFalse(contemId(semDuplicidade, "d-102"));
        ItemAvaliado d103PosDuplicidade = porId(semDuplicidade, "d-103");
        assertFalse(contemCodigo(d103PosDuplicidade.motivos(), MotivoCodigo.DUPLICIDADE));
        assertTrue(d103PosDuplicidade.elegivel());
        assertSame(d103, d103PosDuplicidade,
                "item sem duplicidade preserva a mesma referência de ItemAvaliado — DetectorDuplicidadeEconomica "
                        + "não altera retroativamente o item original");

        // Provas sobre os objetos ORIGINAIS d101/d102, depois de SeletorElegiveis → DetectorDuplicidadeEconomica:
        // eles nunca entraram na comparação econômica, então nada nesses objetos pode ter mudado.
        assertFalse(contemCodigo(d101.motivos(), MotivoCodigo.DUPLICIDADE),
                "d101 nunca chegou ao detector de duplicidade — não pode ter recebido DUPLICIDADE");
        assertFalse(contemCodigo(d102.motivos(), MotivoCodigo.DUPLICIDADE),
                "d102 nunca chegou ao detector de duplicidade — não pode ter recebido DUPLICIDADE");
        assertEquals(1, contagemCodigo(d101.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertEquals(1, contagemCodigo(d102.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertEquals(snapshotMotivosD101, d101.motivos(), "lista de motivos de d101 idêntica ao snapshot anterior");
        assertEquals(snapshotMotivosD102, d102.motivos(), "lista de motivos de d102 idêntica ao snapshot anterior");

        ItemValidado d101Validado = d101.itemNormalizado().item();
        ItemValidado d102Validado = d102.itemNormalizado().item();
        assertNull(d101Validado.getTaxaCambioAplicada());
        assertNull(d101Validado.getDataCotacaoUtilizada());
        assertNull(d101Validado.getValorConvertidoBruto());
        assertNull(d101.itemNormalizado().valorNormalizado());
        assertNull(d102Validado.getTaxaCambioAplicada());
        assertNull(d102Validado.getDataCotacaoUtilizada());
        assertNull(d102Validado.getValorConvertidoBruto());
        assertNull(d102.itemNormalizado().valorNormalizado());
    }

    // ---- 8. Exclusão antes dos tetos --------------------------------------------------------

    @Test
    @DisplayName("8 — itens de moeda sem cotação em alimentacao e hospedagem são excluídos antes dos agregadores "
            + "de teto; não aparecem nos ResultadoTeto e não recebem motivo de teto")
    void exclusaoAntesDosAgregadoresDeTeto() {
        Envelope envelope = envelopeDe(
                despesa("d-201", "2026-07-10", "alimentacao", "Almoco", "Restaurante X",
                        "100.00", "USD", true),
                despesa("d-202", "2026-07-10", "hospedagem", "Hotel", "Hotel Y",
                        "300.00", "USD", true),
                despesa("d-203", "2026-07-10", "alimentacao", "Cafe", "Padaria Z",
                        "30.00", "BRL", true),
                despesa("d-204", "2026-07-10", "hospedagem", "Hospedagem", "Hotel W",
                        "200.00", "BRL", true));

        List<ItemAvaliado> avaliados = avaliarPipeline(envelope, cambioSemCotacao());

        ItemAvaliado d201 = porId(avaliados, "d-201");
        ItemAvaliado d202 = porId(avaliados, "d-202");
        assertTrue(contemCodigo(d201.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertTrue(contemCodigo(d202.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertTrue(porId(avaliados, "d-203").elegivel());
        assertTrue(porId(avaliados, "d-204").elegivel());

        // Snapshots imutáveis dos motivos de d201/d202 antes de qualquer seleção — usados depois
        // para provar que seletores, detector de duplicidade e agregadores de teto não alteram
        // retroativamente estes objetos.
        List<Motivo> snapshotMotivosD201 = List.copyOf(d201.motivos());
        List<Motivo> snapshotMotivosD202 = List.copyOf(d202.motivos());

        List<ItemAvaliado> elegiveis1 = SeletorElegiveis.selecionar(avaliados);
        assertFalse(contemId(elegiveis1, "d-201"));
        assertFalse(contemId(elegiveis1, "d-202"));
        assertEquals(2, elegiveis1.size());

        List<ItemAvaliado> semDuplicidade = DetectorDuplicidadeEconomica.detectar(elegiveis1);
        List<ItemAvaliado> elegiveis2 = SeletorElegiveis.selecionar(semDuplicidade);
        assertEquals(2, elegiveis2.size());
        assertFalse(contemId(elegiveis2, "d-201"));
        assertFalse(contemId(elegiveis2, "d-202"));

        List<ResultadoTeto> resultadoDiario = AgregadorTetoDiario.aplicar(elegiveis2);
        List<ResultadoTeto> resultadoHospedagem = AgregadorTetoHospedagem.aplicar(elegiveis2);

        assertTrue(resultadoDiario.stream()
                .noneMatch(r -> "d-201".equals(r.itemAvaliado().itemNormalizado().item().getId())),
                "item de MOEDA_SEM_COTACAO não aparece no ResultadoTeto de AgregadorTetoDiario");
        assertTrue(resultadoHospedagem.stream()
                .noneMatch(r -> "d-202".equals(r.itemAvaliado().itemNormalizado().item().getId())),
                "item de MOEDA_SEM_COTACAO não aparece no ResultadoTeto de AgregadorTetoHospedagem");

        assertEquals(1, resultadoDiario.size(),
                "único item que chega ao AgregadorTetoDiario é o controle BRL — o de MOEDA_SEM_COTACAO já foi excluído");
        assertEquals("d-203", resultadoDiario.get(0).itemAvaliado().itemNormalizado().item().getId());
        assertEquals(1, resultadoHospedagem.size());
        assertEquals("d-204", resultadoHospedagem.get(0).itemAvaliado().itemNormalizado().item().getId());

        // Provas sobre os objetos ORIGINAIS d201/d202, depois de todo o fluxo real (SeletorElegiveis →
        // DetectorDuplicidadeEconomica → SeletorElegiveis → AgregadorTetoDiario → AgregadorTetoHospedagem):
        // eles nunca entraram nessas etapas, então nada nesses objetos pode ter mudado.
        assertEquals(1, contagemCodigo(d201.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertEquals(1, contagemCodigo(d202.motivos(), MotivoCodigo.MOEDA_SEM_COTACAO));
        assertFalse(contemCodigo(d201.motivos(), MotivoCodigo.DUPLICIDADE));
        assertFalse(contemCodigo(d202.motivos(), MotivoCodigo.DUPLICIDADE));
        assertFalse(contemCodigo(d201.motivos(), MotivoCodigo.TETO_DIARIO_APLICADO));
        assertFalse(contemCodigo(d202.motivos(), MotivoCodigo.TETO_DIARIO_APLICADO));
        assertFalse(contemCodigo(d201.motivos(), MotivoCodigo.TETO_DIARIO_ESGOTADO));
        assertFalse(contemCodigo(d202.motivos(), MotivoCodigo.TETO_DIARIO_ESGOTADO));
        assertFalse(contemCodigo(d201.motivos(), MotivoCodigo.TETO_HOSPEDAGEM_APLICADO));
        assertFalse(contemCodigo(d202.motivos(), MotivoCodigo.TETO_HOSPEDAGEM_APLICADO));
        assertEquals(snapshotMotivosD201, d201.motivos(), "lista de motivos de d201 idêntica ao snapshot anterior");
        assertEquals(snapshotMotivosD202, d202.motivos(), "lista de motivos de d202 idêntica ao snapshot anterior");

        ItemValidado d201Validado = d201.itemNormalizado().item();
        ItemValidado d202Validado = d202.itemNormalizado().item();
        assertNull(d201Validado.getTaxaCambioAplicada());
        assertNull(d201Validado.getDataCotacaoUtilizada());
        assertNull(d201Validado.getValorConvertidoBruto());
        assertNull(d201.itemNormalizado().valorNormalizado());
        assertNull(d202Validado.getTaxaCambioAplicada());
        assertNull(d202Validado.getDataCotacaoUtilizada());
        assertNull(d202Validado.getValorConvertidoBruto());
        assertNull(d202.itemNormalizado().valorNormalizado());
    }
}
