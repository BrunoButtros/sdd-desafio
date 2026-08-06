package com.desafio.reembolso;

import com.desafio.reembolso.escritor.EscritorResultado;
import com.desafio.reembolso.leitor.LeitorCambio;
import com.desafio.reembolso.leitor.LeitorCambio.CambioInvalidoException;
import com.desafio.reembolso.leitor.LeitorPolitica;
import com.desafio.reembolso.leitor.LeitorPolitica.PoliticaInvalidaException;
import com.desafio.reembolso.leitor.ValidadorEnvelope;
import com.desafio.reembolso.leitor.ValidadorEnvelope.EnvelopeInvalidoException;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.TabelaCambio;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario;
import com.desafio.reembolso.pipeline.AgregadorTetoDiario.ResultadoTeto;
import com.desafio.reembolso.pipeline.AgregadorTetoHospedagem;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais;
import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais.ItemAvaliado;
import com.desafio.reembolso.pipeline.CompositorSaida;
import com.desafio.reembolso.pipeline.CompositorSaida.ResultadoItem;
import com.desafio.reembolso.pipeline.DetectorDuplicidadeEconomica;
import com.desafio.reembolso.pipeline.DetectorIdDuplicado;
import com.desafio.reembolso.pipeline.Normalizador;
import com.desafio.reembolso.pipeline.Normalizador.ItemNormalizado;
import com.desafio.reembolso.pipeline.ResolutorCambio;
import com.desafio.reembolso.pipeline.SeletorElegiveis;
import com.desafio.reembolso.pipeline.SomadorTotal;
import com.desafio.reembolso.pipeline.ValidadorItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Orquestra a CLI (spec §3, plan §3, DT-003, DT-010, DT-018): interpreta
 * {@code calcular --input ... --output ... --politica ... --cambio ...},
 * executa os estágios do pipeline (plan §2) na ordem canônica e escreve o
 * resultado atomicamente. Não implementa nenhuma regra de negócio — apenas
 * encadeia os estágios já prontos e traduz sucesso/falha em código de saída.
 */
public final class Main {

    private static final String USO = "Uso: java -jar motor-reembolso.jar calcular --input <arquivo> --output <arquivo> --politica <arquivo> --cambio <arquivo>";

    private static final Set<String> FLAGS_ACEITAS = Set.of("--input", "--output", "--politica", "--cambio");

    /**
     * Ponto de simulação de falha, exclusivo de teste, para exercitar a
     * preservação do destino quando a escrita falha imediatamente antes da
     * substituição atômica (DT-010). Nunca ativado em execução real.
     */
    static boolean simularFalhaAntesDaSubstituicao = false;

    private Main() {
    }

    public static void main(String[] args) {
        System.exit(run(args, System.out, System.err));
    }

    static int run(String[] args, PrintStream out, PrintStream err) {
        if (args.length == 0 || !"calcular".equals(args[0])) {
            err.println(USO);
            return 2;
        }

        int restantes = args.length - 1;
        if (restantes % 2 != 0) {
            err.println("Quantidade inválida de argumentos: flags devem vir em pares flag valor");
            return 2;
        }

        Map<String, String> opcoes = new LinkedHashMap<>();
        for (int i = 1; i < args.length; i += 2) {
            String flag = args[i];
            String valor = args[i + 1];

            if (!FLAGS_ACEITAS.contains(flag)) {
                err.println("Argumento desconhecido: " + flag);
                return 2;
            }
            if (opcoes.containsKey(flag)) {
                err.println("Argumento repetido: " + flag);
                return 2;
            }
            opcoes.put(flag, valor);
        }

        if (!opcoes.containsKey("--input")) {
            err.println("Argumento obrigatório ausente: --input");
            return 2;
        }
        if (!opcoes.containsKey("--output")) {
            err.println("Argumento obrigatório ausente: --output");
            return 2;
        }
        if (!opcoes.containsKey("--politica")) {
            err.println("Argumento obrigatório ausente: --politica");
            return 2;
        }
        if (!opcoes.containsKey("--cambio")) {
            err.println("Argumento obrigatório ausente: --cambio");
            return 2;
        }

        String inputPath = opcoes.get("--input");
        String outputPath = opcoes.get("--output");
        String politicaPath = opcoes.get("--politica");
        String cambioPath = opcoes.get("--cambio");

        Path input;
        Path output;
        Path politica;
        Path cambio;
        TabelaCambio tabelaCambio;
        try {
            input = Path.of(inputPath);
            output = Path.of(outputPath);
            politica = Path.of(politicaPath);
            cambio = Path.of(cambioPath);

            LeitorPolitica.ler(politica);
            tabelaCambio = LeitorCambio.ler(cambio);
        } catch (InvalidPathException e) {
            err.println("Caminho inválido: " + e.getMessage());
            return 2;
        } catch (PoliticaInvalidaException e) {
            err.println("Política inválida: " + e.getMessage());
            return 2;
        } catch (CambioInvalidoException e) {
            err.println("Câmbio inválido: " + e.getMessage());
            return 2;
        }

        if (!Files.isRegularFile(input)) {
            err.println("Arquivo de entrada não encontrado: " + inputPath);
            return 2;
        }

        ObjectMapper mapper = new ObjectMapper()
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        JsonNode raiz;
        try {
            raiz = mapper.readTree(input.toFile());
        } catch (JsonProcessingException e) {
            err.println("JSON de entrada sintaticamente inválido: " + e.getOriginalMessage());
            return 2;
        } catch (IOException e) {
            err.println("Não foi possível ler o arquivo de entrada: " + e.getMessage());
            return 2;
        }

        if (raiz == null || raiz.isMissingNode()) {
            err.println("JSON de entrada sintaticamente inválido: arquivo vazio ou sem conteúdo JSON");
            return 2;
        }

        Envelope envelope;
        try {
            envelope = ValidadorEnvelope.validar(raiz);
        } catch (EnvelopeInvalidoException e) {
            err.println("Envelope inválido: " + e.getMessage());
            return 3;
        }

        List<ResultadoItem> resultados = executarPipeline(envelope, tabelaCambio);
        BigDecimal total = SomadorTotal.somar(resultados);
        String json = EscritorResultado.serializar(envelope, resultados, total);

        try {
            escreverAtomicamente(output, json);
        } catch (IOException e) {
            err.println("Não foi possível escrever o arquivo de saída: " + e.getMessage());
            return 2;
        }

        return 0;
    }

    /**
     * Passos 2 a 10 da ordem canônica (plan §2): valida os itens, detecta
     * {@code id} duplicado, resolve a conversão cambial, normaliza, avalia
     * as regras individuais, separa os elegíveis, detecta duplicidade
     * econômica, aplica os tetos e compõe a saída final — na mesma sequência
     * já comprovada pelos testes de pipeline (T-004 a T-016, T-037, T-038).
     */
    private static List<ResultadoItem> executarPipeline(Envelope envelope, TabelaCambio cambio) {
        List<ItemValidado> validados = ValidadorItem.validarLista(envelope.getDespesas());
        List<ItemValidado> idsVerificados = DetectorIdDuplicado.detectar(validados);
        List<ItemValidado> comCambio = ResolutorCambio.resolverLista(idsVerificados, cambio);
        List<ItemNormalizado> normalizados = Normalizador.normalizarLista(comCambio);
        List<ItemAvaliado> avaliados = AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope);

        List<ItemAvaliado> aprovados = SeletorElegiveis.selecionar(avaliados);
        List<ItemAvaliado> aposDuplicidade = DetectorDuplicidadeEconomica.detectar(aprovados);

        List<ItemAvaliado> elegiveisParaTetos = SeletorElegiveis.selecionar(aposDuplicidade);
        List<ResultadoTeto> resultadosDiarios = AgregadorTetoDiario.aplicar(elegiveisParaTetos);
        List<ResultadoTeto> resultadosHospedagem = AgregadorTetoHospedagem.aplicar(elegiveisParaTetos);

        return CompositorSaida.compor(avaliados, aposDuplicidade, resultadosDiarios, resultadosHospedagem);
    }

    /**
     * Escreve {@code conteudo} em um arquivo temporário no mesmo diretório
     * de {@code destino} e só então move/substitui atomicamente (DT-010).
     * O destino nunca é aberto diretamente para escrita: qualquer falha
     * antes da substituição final remove o temporário e deixa um destino
     * preexistente intacto.
     */
    private static void escreverAtomicamente(Path destino, String conteudo) throws IOException {
        Path destinoAbsoluto = destino.toAbsolutePath();
        Path diretorio = destinoAbsoluto.getParent();

        Path temporario = Files.createTempFile(diretorio, "motor-reembolso-", ".tmp");
        try {
            Files.writeString(temporario, conteudo, StandardCharsets.UTF_8);

            if (simularFalhaAntesDaSubstituicao) {
                throw new IOException("falha simulada antes da substituição final (uso exclusivo de teste)");
            }

            Files.move(
                    temporario,
                    destinoAbsoluto,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            Files.deleteIfExists(temporario);
            throw e;
        }
    }
}
