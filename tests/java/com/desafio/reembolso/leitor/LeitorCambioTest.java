package com.desafio.reembolso.leitor;

import com.desafio.reembolso.modelo.TabelaCambio;
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
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre {@link LeitorCambio} — RN-020 / RN-022 / AMB-035 (spec 4.1.1,
 * plan §7): as validações estruturais exaustivas antes de qualquer entrada
 * do mapa invertido ser construída, e a fixture válida produzindo
 * exatamente a {@code TabelaCambio} esperada (CA-046).
 */
@DisplayName("LeitorCambio — RN-020 / RN-022 / plan §7")
class LeitorCambioTest {

    @TempDir
    Path tempDir;

    private Path escrever(String conteudo) throws IOException {
        Path arquivo = tempDir.resolve("cambio.json");
        Files.writeString(arquivo, conteudo, StandardCharsets.UTF_8);
        return arquivo;
    }

    private LeitorCambio.CambioInvalidoException esperarInvalida(String conteudo) throws IOException {
        Path arquivo = escrever(conteudo);
        LeitorCambio.CambioInvalidoException excecao = assertThrows(
                LeitorCambio.CambioInvalidoException.class, () -> LeitorCambio.ler(arquivo));
        assertEquals(2, excecao.codigoSaida(), "CambioInvalidoException deve corresponder ao exit code 2");
        return excecao;
    }

    // ---- Documentos-base, cada um variando um único campo por vez ---------

    private static String documentoComMoedaBase(String moedaBaseJsonOuNulo) {
        String campo = moedaBaseJsonOuNulo == null ? "" : "\"moeda_base\": " + moedaBaseJsonOuNulo + ",";
        return """
                {
                  %s
                  "taxas": {}
                }
                """.formatted(campo);
    }

    private static String documentoComTaxas(String taxasJsonOuNulo) {
        String campo = taxasJsonOuNulo == null ? "" : "\"taxas\": " + taxasJsonOuNulo;
        return """
                {
                  "moeda_base": "BRL"%s
                  %s
                }
                """.formatted(taxasJsonOuNulo == null ? "" : ",", campo);
    }

    private static String documentoComFonte(String fonteJsonOuNulo) {
        String campo = fonteJsonOuNulo == null ? "" : "\"fonte\": " + fonteJsonOuNulo + ",";
        return """
                {
                  "moeda_base": "BRL",
                  %s
                  "taxas": {}
                }
                """.formatted(campo);
    }

    private static String documentoComObservacao(String observacaoJsonOuNulo) {
        String campo = observacaoJsonOuNulo == null ? "" : "\"observacao\": " + observacaoJsonOuNulo + ",";
        return """
                {
                  "moeda_base": "BRL",
                  %s
                  "taxas": {}
                }
                """.formatted(campo);
    }

    private static String documentoComTaxasConteudo(String taxasJson) {
        return """
                {
                  "moeda_base": "BRL",
                  "taxas": %s
                }
                """.formatted(taxasJson);
    }

    // ---- Falhas de leitura (arquivo, não validação estrutural) ------------

    @Test
    @DisplayName("arquivo inexistente lança CambioInvalidoException com causa preservada")
    void arquivoInexistente() {
        Path arquivo = tempDir.resolve("nao-existe.json");

        LeitorCambio.CambioInvalidoException excecao = assertThrows(
                LeitorCambio.CambioInvalidoException.class, () -> LeitorCambio.ler(arquivo));

        assertEquals(2, excecao.codigoSaida());
        assertNotNull(excecao.getCause(), "causa (IOException) deve ser preservada para arquivo inexistente");
    }

    @Test
    @DisplayName("caminho ilegível (aponta para um diretório, não um arquivo) lança CambioInvalidoException com causa preservada")
    void caminhoIlegivel() throws IOException {
        // Um diretório no lugar do arquivo é uma forma determinística e
        // multiplataforma de reproduzir "caminho ilegível": tentar abri-lo
        // como FileInputStream sempre falha com IOException, tanto no
        // Windows quanto em sistemas POSIX.
        Path diretorio = tempDir.resolve("cambio-ilegivel.json");
        Files.createDirectory(diretorio);

        LeitorCambio.CambioInvalidoException excecao = assertThrows(
                LeitorCambio.CambioInvalidoException.class, () -> LeitorCambio.ler(diretorio));

        assertEquals(2, excecao.codigoSaida());
        assertNotNull(excecao.getCause(), "causa (IOException) deve ser preservada para caminho ilegível");
    }

    @Test
    @DisplayName("JSON sintaticamente inválido lança CambioInvalidoException com causa preservada")
    void jsonSintaticamenteInvalido() throws IOException {
        Path arquivo = escrever("{ \"moeda_base\": ");

        LeitorCambio.CambioInvalidoException excecao = assertThrows(
                LeitorCambio.CambioInvalidoException.class, () -> LeitorCambio.ler(arquivo));

        assertEquals(2, excecao.codigoSaida());
        assertNotNull(excecao.getCause(), "causa (JsonProcessingException) deve ser preservada para JSON sintaticamente inválido");
    }

    @Test
    @DisplayName("raiz que não é objeto é rejeitada")
    void raizNaoObjeto() throws IOException {
        esperarInvalida("[]");
    }

    // ---- moeda_base -------------------------------------------------------

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

    // ---- taxas (obrigatório, objeto, pode ser vazio) -----------------------

    static Stream<Arguments> casosTaxasInvalidas() {
        return Stream.of(
                Arguments.of("ausente", null),
                Arguments.of("não objeto", "\"nao-e-objeto\"")
        );
    }

    @ParameterizedTest(name = "taxas {0}")
    @MethodSource("casosTaxasInvalidas")
    @DisplayName("taxas ausente ou não objeto rejeita o arquivo inteiro")
    void taxasInvalida(String rotulo, String valorJson) throws IOException {
        esperarInvalida(documentoComTaxas(valorJson));
    }

    @Test
    @DisplayName("taxas vazio é válido e produz TabelaCambio sem nenhuma moeda")
    void taxasVazioValido() throws IOException {
        Path arquivo = escrever(documentoComTaxas("{}"));

        TabelaCambio tabela = LeitorCambio.ler(arquivo);

        assertEquals("BRL", tabela.getMoedaBase());
        assertTrue(tabela.getCotacoesPorMoeda().isEmpty());
    }

    // ---- chaves de data dentro de taxas ------------------------------------

    @Test
    @DisplayName("data malformada como chave de taxas rejeita o arquivo inteiro")
    void dataMalformada() throws IOException {
        esperarInvalida(documentoComTaxasConteudo(
                "{ \"2026/07/13\": { \"USD\": 5.40 } }"));
    }

    @Test
    @DisplayName("data inexistente no calendário como chave de taxas rejeita o arquivo inteiro")
    void dataInexistente() throws IOException {
        esperarInvalida(documentoComTaxasConteudo(
                "{ \"2026-02-30\": { \"USD\": 5.40 } }"));
    }

    @Test
    @DisplayName("valor associado a uma data que não é objeto rejeita o arquivo inteiro")
    void valorDaDataNaoObjeto() throws IOException {
        esperarInvalida(documentoComTaxasConteudo(
                "{ \"2026-07-15\": 5.40 }"));
    }

    // ---- chaves de moeda dentro de uma data --------------------------------

    static Stream<Arguments> casosMoedaMalformada() {
        return Stream.of(
                Arguments.of("minúsculo", "usd"),
                Arguments.of("comprimento errado (2)", "US"),
                Arguments.of("comprimento errado (4)", "USDX")
        );
    }

    @ParameterizedTest(name = "moeda {0}")
    @MethodSource("casosMoedaMalformada")
    @DisplayName("moeda fora de [A-Z]{3} como chave dentro de uma data rejeita o arquivo inteiro")
    void moedaMalformada(String rotulo, String chaveMoeda) throws IOException {
        esperarInvalida(documentoComTaxasConteudo(
                "{ \"2026-07-15\": { \"" + chaveMoeda + "\": 5.40 } }"));
    }

    // ---- taxa numérica, estritamente positiva ------------------------------

    @Test
    @DisplayName("taxa não numérica rejeita o arquivo inteiro")
    void taxaNaoNumerica() throws IOException {
        esperarInvalida(documentoComTaxasConteudo(
                "{ \"2026-07-15\": { \"USD\": \"5.40\" } }"));
    }

    @Test
    @DisplayName("taxa igual a zero rejeita o arquivo inteiro")
    void taxaZero() throws IOException {
        esperarInvalida(documentoComTaxasConteudo(
                "{ \"2026-07-15\": { \"USD\": 0 } }"));
    }

    @Test
    @DisplayName("taxa negativa rejeita o arquivo inteiro")
    void taxaNegativa() throws IOException {
        esperarInvalida(documentoComTaxasConteudo(
                "{ \"2026-07-15\": { \"USD\": -5.40 } }"));
    }

    // ---- fonte / observacao (opcionais, texto quando presentes) -----------

    @Test
    @DisplayName("fonte ausente é válida")
    void fonteAusenteValida() throws IOException {
        Path arquivo = escrever(documentoComFonte(null));

        TabelaCambio tabela = LeitorCambio.ler(arquivo);

        assertEquals("BRL", tabela.getMoedaBase());
    }

    @Test
    @DisplayName("observacao ausente é válida")
    void observacaoAusenteValida() throws IOException {
        Path arquivo = escrever(documentoComObservacao(null));

        TabelaCambio tabela = LeitorCambio.ler(arquivo);

        assertEquals("BRL", tabela.getMoedaBase());
    }

    @Test
    @DisplayName("fonte como texto é válida e descartada")
    void fonteTextoValidaEDescartada() throws IOException {
        Path arquivo = escrever(documentoComFonte("\"Banco Central\""));

        TabelaCambio tabela = LeitorCambio.ler(arquivo);

        assertEquals("BRL", tabela.getMoedaBase());
    }

    @Test
    @DisplayName("observacao como texto é válida e descartada")
    void observacaoTextoValidaEDescartada() throws IOException {
        Path arquivo = escrever(documentoComObservacao("\"cotações apenas em dias úteis\""));

        TabelaCambio tabela = LeitorCambio.ler(arquivo);

        assertEquals("BRL", tabela.getMoedaBase());
    }

    @Test
    @DisplayName("fonte com tipo não textual (número) rejeita o arquivo inteiro")
    void fonteTipoNaoTextualRejeitada() throws IOException {
        esperarInvalida(documentoComFonte("123"));
    }

    @Test
    @DisplayName("observacao com tipo não textual (booleano) rejeita o arquivo inteiro")
    void observacaoTipoNaoTextualRejeitada() throws IOException {
        esperarInvalida(documentoComObservacao("true"));
    }

    // ---- fonte / observacao — demais estados fechados de tipo inválido ----
    // (completa, junto dos testes acima, os sete estados de cada campo:
    // ausente, texto, null explícito, número, booleano, lista, objeto)

    static Stream<Arguments> casosFonteTipoInvalidoRestante() {
        return Stream.of(
                Arguments.of("null explícito", "null"),
                Arguments.of("booleano", "true"),
                Arguments.of("lista", "[]"),
                Arguments.of("objeto", "{}")
        );
    }

    @ParameterizedTest(name = "fonte {0}")
    @MethodSource("casosFonteTipoInvalidoRestante")
    @DisplayName("fonte com null explícito, booleano, lista ou objeto rejeita o arquivo inteiro")
    void fonteTipoInvalidoRestanteRejeitada(String rotulo, String valorJson) throws IOException {
        esperarInvalida(documentoComFonte(valorJson));
    }

    static Stream<Arguments> casosObservacaoTipoInvalidoRestante() {
        return Stream.of(
                Arguments.of("null explícito", "null"),
                Arguments.of("número", "123"),
                Arguments.of("lista", "[]"),
                Arguments.of("objeto", "{}")
        );
    }

    @ParameterizedTest(name = "observacao {0}")
    @MethodSource("casosObservacaoTipoInvalidoRestante")
    @DisplayName("observacao com null explícito, número, lista ou objeto rejeita o arquivo inteiro")
    void observacaoTipoInvalidoRestanteRejeitada(String rotulo, String valorJson) throws IOException {
        esperarInvalida(documentoComObservacao(valorJson));
    }

    // ---- campos desconhecidos na raiz, ignorados sem invalidar o arquivo --

    @Test
    @DisplayName("campo desconhecido na raiz é ignorado sem invalidar o arquivo")
    void campoDesconhecidoNaRaizIgnorado() throws IOException {
        Path arquivo = escrever("""
                {
                  "moeda_base": "BRL",
                  "campo_totalmente_desconhecido": { "qualquer": "coisa" },
                  "taxas": {}
                }
                """);

        TabelaCambio tabela = LeitorCambio.ler(arquivo);

        assertEquals("BRL", tabela.getMoedaBase());
        assertTrue(tabela.getCotacoesPorMoeda().isEmpty());
    }

    // ---- fixture válida — CA-046 -------------------------------------------

    @Test
    @DisplayName("fixture válida produz TabelaCambio com a estrutura invertida esperada")
    void fixtureValidaProduzTabelaCambioComEstruturaInvertida() {
        Path fixture = Path.of("tests", "resources", "fixtures", "cambio-valido-teste.json");

        TabelaCambio tabela = LeitorCambio.ler(fixture);

        assertEquals("BRL", tabela.getMoedaBase());
        assertEquals(2, tabela.getCotacoesPorMoeda().size());
        assertTrue(tabela.getCotacoesPorMoeda().containsKey("USD"));
        assertTrue(tabela.getCotacoesPorMoeda().containsKey("EUR"));
        assertEquals(3, tabela.getCotacoesPorMoeda().get("USD").size());
        assertEquals(3, tabela.getCotacoesPorMoeda().get("EUR").size());
    }

    @Test
    @DisplayName("consulta exata e fallback anterior funcionam com os dados lidos da fixture")
    void consultaExataEFallbackFuncionamComDadosDaFixture() {
        Path fixture = Path.of("tests", "resources", "fixtures", "cambio-valido-teste.json");

        TabelaCambio tabela = LeitorCambio.ler(fixture);

        Optional<TabelaCambio.CotacaoResolvida> exata = tabela.cotacaoEm("USD", LocalDate.of(2026, 7, 17));
        assertTrue(exata.isPresent());
        assertEquals(LocalDate.of(2026, 7, 17), exata.get().data());
        assertEquals(0, new BigDecimal("5.47").compareTo(exata.get().taxa()));

        Optional<TabelaCambio.CotacaoResolvida> fallback = tabela.cotacaoEm("EUR", LocalDate.of(2026, 7, 18));
        assertTrue(fallback.isPresent());
        assertEquals(LocalDate.of(2026, 7, 17), fallback.get().data());
        assertEquals(0, new BigDecimal("5.96").compareTo(fallback.get().taxa()));
    }

    // ---- imutabilidade da TabelaCambio produzida pelo leitor ---------------

    @Test
    @DisplayName("mapa externo de cotacoesPorMoeda é imutável")
    void mapaExternoDeCotacoesPorMoedaImutavel() {
        Path fixture = Path.of("tests", "resources", "fixtures", "cambio-valido-teste.json");
        TabelaCambio tabela = LeitorCambio.ler(fixture);

        assertThrows(UnsupportedOperationException.class,
                () -> tabela.getCotacoesPorMoeda().put("GBP", new TreeMap<>()));
    }

    @Test
    @DisplayName("cada NavigableMap interno de cotacoesPorMoeda é imutável")
    void navigableMapInternoDeCotacoesPorMoedaImutavel() {
        Path fixture = Path.of("tests", "resources", "fixtures", "cambio-valido-teste.json");
        TabelaCambio tabela = LeitorCambio.ler(fixture);

        NavigableMap<LocalDate, BigDecimal> cotacoesUsd = tabela.getCotacoesPorMoeda().get("USD");
        assertThrows(UnsupportedOperationException.class,
                () -> cotacoesUsd.put(LocalDate.of(2099, 1, 1), BigDecimal.TEN));

        NavigableMap<LocalDate, BigDecimal> cotacoesEur = tabela.getCotacoesPorMoeda().get("EUR");
        assertThrows(UnsupportedOperationException.class,
                () -> cotacoesEur.put(LocalDate.of(2099, 1, 1), BigDecimal.TEN));
    }
}
