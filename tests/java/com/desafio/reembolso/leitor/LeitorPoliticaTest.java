package com.desafio.reembolso.leitor;

import com.desafio.reembolso.modelo.Periodicidade;
import com.desafio.reembolso.modelo.PoliticaExterna;
import com.desafio.reembolso.modelo.TabelaCategoria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre {@link LeitorPolitica} — RN-021 / RN-022 / AMB-035 (spec 4.1.1,
 * plan §5): as dezesseis validações estruturais exaustivas antes de
 * qualquer {@code TabelaCategoria} ser construída, e a fixture válida
 * (CA-045) produzindo exatamente o {@code PoliticaExterna} esperado.
 */
@DisplayName("LeitorPolitica — RN-021 / RN-022 / plan §5")
class LeitorPoliticaTest {

    @TempDir
    Path tempDir;

    private Path escrever(String conteudo) throws IOException {
        Path arquivo = tempDir.resolve("politica.json");
        Files.writeString(arquivo, conteudo, StandardCharsets.UTF_8);
        return arquivo;
    }

    private LeitorPolitica.PoliticaInvalidaException esperarInvalida(String conteudo) throws IOException {
        Path arquivo = escrever(conteudo);
        LeitorPolitica.PoliticaInvalidaException excecao = assertThrows(
                LeitorPolitica.PoliticaInvalidaException.class, () -> LeitorPolitica.ler(arquivo));
        assertEquals(2, excecao.codigoSaida(), "PoliticaInvalidaException deve corresponder ao exit code 2");
        return excecao;
    }

    /**
     * Compara por valor decimal (DT-004: {@code compareTo}, nunca {@code equals})
     * — {@code JsonNode.decimalValue()} pode devolver uma representação sem
     * escala fixa (ex.: {@code 60.00} → {@code 6E+1}), matematicamente igual
     * mas não igual por {@code BigDecimal.equals}.
     */
    private static void assertTabelaCategoria(String limiteEsperado, Periodicidade periodicidadeEsperada, TabelaCategoria real) {
        assertNotNull(real);
        assertEquals(0, new BigDecimal(limiteEsperado).compareTo(real.limite()),
                "limite esperado " + limiteEsperado + " mas foi " + real.limite());
        assertEquals(periodicidadeEsperada, real.periodicidade());
    }

    // ---- Documentos-base, cada um variando um único campo por vez ---------

    private static String documentoComVigencia(String vigenciaJsonOuNulo) {
        String campo = vigenciaJsonOuNulo == null ? "" : "\"vigencia\": " + vigenciaJsonOuNulo + ",";
        return """
                {
                  %s
                  "moeda_base": "BRL",
                  "nota_fiscal_obrigatoria_acima_de": 100.00,
                  "padrao": {},
                  "centros_custo": {}
                }
                """.formatted(campo);
    }

    private static String documentoComMoedaBase(String moedaBaseJsonOuNulo) {
        String campo = moedaBaseJsonOuNulo == null ? "" : "\"moeda_base\": " + moedaBaseJsonOuNulo + ",";
        return """
                {
                  "vigencia": "2026-08-01",
                  %s
                  "nota_fiscal_obrigatoria_acima_de": 100.00,
                  "padrao": {},
                  "centros_custo": {}
                }
                """.formatted(campo);
    }

    private static String documentoComNotaFiscal(String notaFiscalJsonOuNulo) {
        String campo = notaFiscalJsonOuNulo == null ? "" : "\"nota_fiscal_obrigatoria_acima_de\": " + notaFiscalJsonOuNulo + ",";
        return """
                {
                  "vigencia": "2026-08-01",
                  "moeda_base": "BRL",
                  %s
                  "padrao": {},
                  "centros_custo": {}
                }
                """.formatted(campo);
    }

    private static String documentoComPadrao(String padraoJsonOuNulo) {
        String campo = padraoJsonOuNulo == null ? "" : "\"padrao\": " + padraoJsonOuNulo + ",";
        return """
                {
                  "vigencia": "2026-08-01",
                  "moeda_base": "BRL",
                  "nota_fiscal_obrigatoria_acima_de": 100.00,
                  %s
                  "centros_custo": {}
                }
                """.formatted(campo);
    }

    private static String documentoComCentrosCusto(String centrosCustoJsonOuNulo) {
        String campo = centrosCustoJsonOuNulo == null ? "" : "\"centros_custo\": " + centrosCustoJsonOuNulo + ",";
        return """
                {
                  "vigencia": "2026-08-01",
                  "moeda_base": "BRL",
                  "nota_fiscal_obrigatoria_acima_de": 100.00,
                  "padrao": {},
                  %s
                  "dummy": null
                }
                """.formatted(campo);
    }

    private static String documentoComConfigCategoriaPadrao(String configJson) {
        return """
                {
                  "vigencia": "2026-08-01",
                  "moeda_base": "BRL",
                  "nota_fiscal_obrigatoria_acima_de": 100.00,
                  "padrao": { "alimentacao": %s },
                  "centros_custo": {}
                }
                """.formatted(configJson);
    }

    // ---- Falhas de leitura (arquivo, não validação estrutural) ------------

    @Test
    @DisplayName("arquivo inexistente lança PoliticaInvalidaException com causa preservada")
    void arquivoInexistente() {
        Path arquivo = tempDir.resolve("nao-existe.json");

        LeitorPolitica.PoliticaInvalidaException excecao = assertThrows(
                LeitorPolitica.PoliticaInvalidaException.class, () -> LeitorPolitica.ler(arquivo));

        assertEquals(2, excecao.codigoSaida());
        assertNotNull(excecao.getCause(), "causa (IOException) deve ser preservada para arquivo inexistente");
    }

    @Test
    @DisplayName("caminho ilegível (aponta para um diretório, não um arquivo) lança PoliticaInvalidaException com causa preservada")
    void caminhoIlegivel() throws IOException {
        // Um diretório no lugar do arquivo é uma forma determinística e
        // multiplataforma de reproduzir "caminho ilegível": tentar abri-lo
        // como FileInputStream sempre falha com IOException, tanto no
        // Windows quanto em sistemas POSIX, sem depender de permissões de
        // filesystem que nem sempre são respeitadas de forma previsível.
        Path diretorio = tempDir.resolve("politica-ilegivel.json");
        Files.createDirectory(diretorio);

        LeitorPolitica.PoliticaInvalidaException excecao = assertThrows(
                LeitorPolitica.PoliticaInvalidaException.class, () -> LeitorPolitica.ler(diretorio));

        assertEquals(2, excecao.codigoSaida());
        assertNotNull(excecao.getCause(), "causa (IOException) deve ser preservada para caminho ilegível");
    }

    @Test
    @DisplayName("JSON sintaticamente inválido lança PoliticaInvalidaException com causa preservada")
    void jsonSintaticamenteInvalido() throws IOException {
        Path arquivo = escrever("{ \"vigencia\": ");

        LeitorPolitica.PoliticaInvalidaException excecao = assertThrows(
                LeitorPolitica.PoliticaInvalidaException.class, () -> LeitorPolitica.ler(arquivo));

        assertEquals(2, excecao.codigoSaida());
        assertNotNull(excecao.getCause(), "causa (JsonProcessingException) deve ser preservada para JSON sintaticamente inválido");
    }

    @Test
    @DisplayName("raiz que não é objeto é rejeitada")
    void raizNaoObjeto() throws IOException {
        esperarInvalida("[]");
    }

    // ---- vigencia (RN-021) --------------------------------------------------

    static Stream<Arguments> casosVigenciaInvalida() {
        return Stream.of(
                Arguments.of("ausente", null),
                Arguments.of("tipo errado (número)", "20260801"),
                Arguments.of("formato errado", "\"31/07/2026\""),
                Arguments.of("data inexistente", "\"2026-02-30\"")
        );
    }

    @ParameterizedTest(name = "vigencia {0}")
    @MethodSource("casosVigenciaInvalida")
    @DisplayName("vigencia inválida rejeita o arquivo inteiro")
    void vigenciaInvalida(String rotulo, String valorJson) throws IOException {
        esperarInvalida(documentoComVigencia(valorJson));
    }

    // ---- moeda_base -----------------------------------------------------

    static Stream<Arguments> casosMoedaBaseInvalida() {
        return Stream.of(
                Arguments.of("ausente", null),
                Arguments.of("tipo errado (número)", "123"),
                Arguments.of("diferente de BRL", "\"USD\"")
        );
    }

    @ParameterizedTest(name = "moeda_base {0}")
    @MethodSource("casosMoedaBaseInvalida")
    @DisplayName("moeda_base inválida rejeita o arquivo inteiro")
    void moedaBaseInvalida(String rotulo, String valorJson) throws IOException {
        esperarInvalida(documentoComMoedaBase(valorJson));
    }

    // ---- nota_fiscal_obrigatoria_acima_de --------------------------------

    static Stream<Arguments> casosNotaFiscalInvalida() {
        return Stream.of(
                Arguments.of("ausente", null),
                Arguments.of("tipo errado (texto)", "\"100\""),
                Arguments.of("negativo", "-5")
        );
    }

    @ParameterizedTest(name = "nota_fiscal_obrigatoria_acima_de {0}")
    @MethodSource("casosNotaFiscalInvalida")
    @DisplayName("nota_fiscal_obrigatoria_acima_de inválida rejeita o arquivo inteiro")
    void notaFiscalInvalida(String rotulo, String valorJson) throws IOException {
        esperarInvalida(documentoComNotaFiscal(valorJson));
    }

    // ---- padrao / centros_custo (obrigatórios, objetos) ------------------

    static Stream<Arguments> casosPadraoInvalido() {
        return Stream.of(
                Arguments.of("ausente", null),
                Arguments.of("não objeto", "\"nao-e-objeto\"")
        );
    }

    @ParameterizedTest(name = "padrao {0}")
    @MethodSource("casosPadraoInvalido")
    @DisplayName("padrao ausente ou não objeto rejeita o arquivo inteiro")
    void padraoInvalido(String rotulo, String valorJson) throws IOException {
        esperarInvalida(documentoComPadrao(valorJson));
    }

    static Stream<Arguments> casosCentrosCustoInvalido() {
        return Stream.of(
                Arguments.of("ausente", null),
                Arguments.of("não objeto", "\"nao-e-objeto\"")
        );
    }

    @ParameterizedTest(name = "centros_custo {0}")
    @MethodSource("casosCentrosCustoInvalido")
    @DisplayName("centros_custo ausente ou não objeto rejeita o arquivo inteiro")
    void centrosCustoInvalido(String rotulo, String valorJson) throws IOException {
        esperarInvalida(documentoComCentrosCusto(valorJson));
    }

    @Test
    @DisplayName("tabela de um centro de custo que não é objeto rejeita o arquivo inteiro")
    void tabelaDeCentroCustoNaoObjeto() throws IOException {
        esperarInvalida(documentoComCentrosCusto("{ \"CC-X\": \"nao-e-objeto\" }"));
    }

    // ---- nome de categoria / configuração de categoria --------------------

    @Test
    @DisplayName("nome de categoria vazio em padrao rejeita o arquivo inteiro")
    void nomeDeCategoriaVazio() throws IOException {
        esperarInvalida(documentoComPadrao("{ \"\": { \"limite\": 60.00, \"periodicidade\": \"dia\" } }"));
    }

    @Test
    @DisplayName("configuração de categoria que não é objeto rejeita o arquivo inteiro")
    void configuracaoDeCategoriaNaoObjeto() throws IOException {
        esperarInvalida(documentoComPadrao("{ \"alimentacao\": 60.00 }"));
    }

    // ---- limite / periodicidade / observacao dentro da configuração -------

    static Stream<Arguments> casosConfiguracaoCategoriaInvalida() {
        return Stream.of(
                Arguments.of("limite ausente", "{ \"periodicidade\": \"dia\" }"),
                Arguments.of("limite não numérico", "{ \"limite\": \"60.00\", \"periodicidade\": \"dia\" }"),
                Arguments.of("periodicidade ausente", "{ \"limite\": 60.00 }"),
                Arguments.of("periodicidade tipo errado", "{ \"limite\": 60.00, \"periodicidade\": 5 }"),
                Arguments.of("periodicidade fora de dia/diaria", "{ \"limite\": 60.00, \"periodicidade\": \"mensal\" }"),
                Arguments.of("observacao null explícito", "{ \"limite\": 60.00, \"periodicidade\": \"dia\", \"observacao\": null }"),
                Arguments.of("observacao número", "{ \"limite\": 60.00, \"periodicidade\": \"dia\", \"observacao\": 5 }"),
                Arguments.of("observacao booleano", "{ \"limite\": 60.00, \"periodicidade\": \"dia\", \"observacao\": true }"),
                Arguments.of("observacao lista", "{ \"limite\": 60.00, \"periodicidade\": \"dia\", \"observacao\": [] }"),
                Arguments.of("observacao objeto", "{ \"limite\": 60.00, \"periodicidade\": \"dia\", \"observacao\": {} }")
        );
    }

    @ParameterizedTest(name = "configuração de categoria — {0}")
    @MethodSource("casosConfiguracaoCategoriaInvalida")
    @DisplayName("configuração de categoria inválida rejeita o arquivo inteiro")
    void configuracaoDeCategoriaInvalida(String rotulo, String configJson) throws IOException {
        esperarInvalida(documentoComConfigCategoriaPadrao(configJson));
    }

    @Test
    @DisplayName("limite igual a zero em padrao rejeita o arquivo inteiro (estritamente maior que zero)")
    void limiteZeroEmPadraoInvalido() throws IOException {
        esperarInvalida(documentoComConfigCategoriaPadrao("{ \"limite\": 0, \"periodicidade\": \"dia\" }"));
    }

    @Test
    @DisplayName("observacao ausente é válida")
    void observacaoAusenteValida() throws IOException {
        Path arquivo = escrever(documentoComConfigCategoriaPadrao("{ \"limite\": 60.00, \"periodicidade\": \"dia\" }"));

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);

        assertTabelaCategoria("60.00", Periodicidade.DIA, politica.getPadrao().get("alimentacao"));
    }

    @Test
    @DisplayName("observacao como texto é válida e descartada — não entra em TabelaCategoria")
    void observacaoTextoValidaEDescartada() throws IOException {
        Path arquivo = escrever(documentoComConfigCategoriaPadrao(
                "{ \"limite\": 60.00, \"periodicidade\": \"dia\", \"observacao\": \"teto diário de alimentação\" }"));

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);

        assertTabelaCategoria("60.00", Periodicidade.DIA, politica.getPadrao().get("alimentacao"));
    }

    // ---- campos desconhecidos, ignorados sem invalidar o arquivo ----------

    @Test
    @DisplayName("campos desconhecidos na raiz são ignorados sem invalidar o arquivo")
    void camposDesconhecidosNaRaizIgnorados() throws IOException {
        Path arquivo = escrever("""
                {
                  "vigencia": "2026-08-01",
                  "moeda_base": "BRL",
                  "nota_fiscal_obrigatoria_acima_de": 100.00,
                  "versao": "v4",
                  "acrescimo_em_viagem_percentual": 50,
                  "campo_totalmente_desconhecido": { "qualquer": "coisa" },
                  "padrao": {},
                  "centros_custo": {}
                }
                """);

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);

        assertEquals(LocalDate.of(2026, 8, 1), politica.getVigencia());
        assertTrue(politica.getPadrao().isEmpty());
    }

    @Test
    @DisplayName("campos desconhecidos dentro de uma configuração de categoria são ignorados sem invalidar o arquivo")
    void camposDesconhecidosNaCategoriaIgnorados() throws IOException {
        Path arquivo = escrever(documentoComConfigCategoriaPadrao(
                "{ \"limite\": 60.00, \"periodicidade\": \"dia\", \"campo_desconhecido\": 123 }"));

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);

        assertTabelaCategoria("60.00", Periodicidade.DIA, politica.getPadrao().get("alimentacao"));
    }

    // ---- fixture válida — CA-045 -------------------------------------------

    @Test
    @DisplayName("fixture válida produz exatamente o PoliticaExterna esperado (CA-045)")
    void fixtureValidaProduzPoliticaExternaEsperado() {
        Path fixture = Path.of("tests", "resources", "fixtures", "politica-valida-teste.json");

        PoliticaExterna politica = LeitorPolitica.ler(fixture);

        assertEquals(LocalDate.of(2026, 8, 1), politica.getVigencia());
        assertEquals("BRL", politica.getMoedaBase());
        assertEquals(0, new BigDecimal("100.00").compareTo(politica.getNotaFiscalObrigatoriaAcimaDe()));

        Map<String, TabelaCategoria> padrao = politica.getPadrao();
        assertEquals(3, padrao.size());
        assertTabelaCategoria("60.00", Periodicidade.DIA, padrao.get("alimentacao"));
        assertTabelaCategoria("80.00", Periodicidade.DIA, padrao.get("transporte_urbano"));
        assertTabelaCategoria("250.00", Periodicidade.DIARIA, padrao.get("hospedagem"));

        Map<String, Map<String, TabelaCategoria>> centrosCusto = politica.getCentrosCusto();
        assertEquals(2, centrosCusto.size());

        Map<String, TabelaCategoria> engPlataforma = centrosCusto.get("CC-ENG-PLATAFORMA");
        assertEquals(4, engPlataforma.size());
        assertTabelaCategoria("60.00", Periodicidade.DIA, engPlataforma.get("alimentacao"));
        assertTabelaCategoria("80.00", Periodicidade.DIA, engPlataforma.get("transporte_urbano"));
        assertTabelaCategoria("250.00", Periodicidade.DIARIA, engPlataforma.get("hospedagem"));
        assertTabelaCategoria("40.00", Periodicidade.DIARIA, engPlataforma.get("estacionamento"));

        Map<String, TabelaCategoria> comercial = centrosCusto.get("CC-COMERCIAL");
        assertEquals(1, comercial.size());
        assertTabelaCategoria("0.00", Periodicidade.DIA, comercial.get("alimentacao"));
    }

    // ---- T-031: limite zero em centros_custo e imutabilidade --------------

    @Test
    @DisplayName("limite zero em centros_custo é aceito e produz TabelaCategoria com limite 0.00")
    void limiteZeroEmCentroCustoValido() throws IOException {
        Path arquivo = escrever(documentoComCentrosCusto(
                "{ \"CENTRO\": { \"categoria\": { \"limite\": 0, \"periodicidade\": \"dia\" } } }"));

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);

        assertEquals(0, BigDecimal.ZERO.compareTo(
                politica.getCentrosCusto().get("CENTRO").get("categoria").limite()));
    }

    @Test
    @DisplayName("padrao retornado por uma política lida de arquivo é imutável")
    void padraoRetornadoEImutavel() throws IOException {
        Path arquivo = escrever(documentoComConfigCategoriaPadrao(
                "{ \"limite\": 60.00, \"periodicidade\": \"dia\" }"));

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);
        Map<String, TabelaCategoria> padrao = politica.getPadrao();

        assertThrows(UnsupportedOperationException.class,
                () -> padrao.put("nova", new TabelaCategoria(BigDecimal.TEN, Periodicidade.DIA)));
    }

    @Test
    @DisplayName("mapa externo de centrosCusto retornado é imutável")
    void mapaExternoDeCentrosCustoEImutavel() throws IOException {
        Path arquivo = escrever(documentoComCentrosCusto(
                "{ \"CENTRO\": { \"categoria\": { \"limite\": 60.00, \"periodicidade\": \"dia\" } } }"));

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);
        Map<String, Map<String, TabelaCategoria>> centrosCusto = politica.getCentrosCusto();

        assertThrows(UnsupportedOperationException.class,
                () -> centrosCusto.put("OUTRO", Map.of()));
    }

    @Test
    @DisplayName("mapa interno de categorias de um centro de custo é imutável")
    void mapaInternoDeCategoriasDoCentroCustoEImutavel() throws IOException {
        Path arquivo = escrever(documentoComCentrosCusto(
                "{ \"CENTRO\": { \"categoria\": { \"limite\": 60.00, \"periodicidade\": \"dia\" } } }"));

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);
        Map<String, TabelaCategoria> categoriasDoCentro = politica.getCentrosCusto().get("CENTRO");

        assertThrows(UnsupportedOperationException.class,
                () -> categoriasDoCentro.put("nova", new TabelaCategoria(BigDecimal.TEN, Periodicidade.DIA)));
    }

    @Test
    @DisplayName("nenhuma alteração feita nos mapas obtidos pelos getters consegue modificar o modelo")
    void alteracaoViaGettersNaoModificaOModelo() throws IOException {
        Path arquivo = escrever(documentoComCentrosCusto(
                "{ \"CENTRO\": { \"categoria\": { \"limite\": 60.00, \"periodicidade\": \"dia\" } } }"));

        PoliticaExterna politica = LeitorPolitica.ler(arquivo);

        assertThrows(UnsupportedOperationException.class, () -> politica.getPadrao().clear());
        assertThrows(UnsupportedOperationException.class, () -> politica.getCentrosCusto().clear());
        assertThrows(UnsupportedOperationException.class,
                () -> politica.getCentrosCusto().get("CENTRO").clear());

        assertTabelaCategoria("60.00", Periodicidade.DIA,
                politica.getCentrosCusto().get("CENTRO").get("categoria"));
    }
}
