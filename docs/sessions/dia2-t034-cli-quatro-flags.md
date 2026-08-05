 ▐▛███▜▌   Claude Code v2.1.222
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Implemente exclusivamente a T-034 do backlog aprovado.                        
                                                                                
  Leia integralmente:                                                           
                                                                                
  - CLAUDE.md;                                                                  
  - a descrição completa da T-034 em specs/001-motor-reembolso/tasks.md;        
  - src/main/java/com/desafio/reembolso/Main.java;                              
  - todos os testes que invocam Main.run(...).                                  
                                                                                
  Altere somente os arquivos necessários previstos pela T-034:                  
                                                                                
  - src/main/java/com/desafio/reembolso/Main.java;                              
  - tests/java/com/desafio/reembolso/CliContratoTest.java;                      
  - tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTest.java;              
  - tests/java/com/desafio/reembolso/ExemploCompletoTest.java.                  
                                                                                
  Não implemente T-035.                                                         
  Não chame LeitorPolitica ou LeitorCambio nesta task.                          
  As flags --politica e --cambio serão apenas reconhecidas, validadas e         
  armazenadas.                                                                  
                                                                                
  ## Contrato definitivo da CLI                                                 
                                                                                
  A execução válida passa a ser:                                                
                                                                                
  calcular --input <entrada.json> --output <saida.json> --politica              
  <politica.json> --cambio <cambio.json>                                        
                                                                                
  As quatro flags são obrigatórias:                                             
                                                                                
  - --input                                                                     
  - --output                                                                    
  - --politica                                                                  
  - --cambio                                                                    
                                                                                
  Elas podem aparecer em qualquer ordem, mas cada uma deve aparecer exatamente  
  uma vez.                                                                      
                                                                                
  ## Parser                                                                     
                                                                                
  Reescreva apenas a parte de parsing de argumentos de Main.run(...).           
                                                                                
  Regras:                                                                       
                                                                                
  1. o primeiro argumento deve ser exatamente "calcular";                       
                                                                                
  2. subcomando ausente ou diferente de "calcular" retorna exit 2;              
                                                                                
  3. depois de "calcular", os argumentos são consumidos estritamente aos pares: 
     flag valor;                                                                
                                                                                
  4. quantidade ímpar de tokens ou flag sem valor retorna exit 2;               
                                                                                
  5. somente estas flags são aceitas:                                           
     --input, --output, --politica e --cambio;                                  
                                                                                
  6. flag desconhecida retorna exit 2;                                          
                                                                                
  7. flag repetida retorna exit 2;                                              
                                                                                
  8. ausência de qualquer uma das quatro flags retorna exit 2;                  
                                                                                
  9. token posicional extra depois de "calcular" retorna exit 2;                
                                                                                
  10. o comando antigo contendo somente --input e --output deve ser rejeitado   
  com exit 2.                                                                   
                                                                                
  Acumule as opções num Map<String, String> ou estrutura equivalente que        
  permita detectar repetições.                                                  
                                                                                
  Após a validação, extraia:                                                    
                                                                                
  - inputPath;                                                                  
  - outputPath;                                                                 
  - politicaPath;                                                               
  - cambioPath.                                                                 
                                                                                
  Nesta task, politicaPath e cambioPath ainda não são lidos nem usados pelo     
  pipeline.                                                                     
                                                                                
  Não valide existência ou conteúdo dos arquivos de política e câmbio nesta     
  task. Isso pertence à T-035.                                                  
                                                                                
  ## Uso                                                                        
                                                                                
  Atualize a constante de uso exatamente para:                                  
                                                                                
  Uso: java -jar motor-reembolso.jar calcular --input <arquivo> --output        
  <arquivo> --politica <arquivo> --cambio <arquivo>                             
                                                                                
  ## Preservação do fluxo existente                                             
                                                                                
  Depois do parsing, mantenha inalterado o restante do comportamento atual:     
                                                                                
  - leitura de --input;                                                         
  - validação do envelope;                                                      
  - execução do pipeline histórico;                                             
  - escrita atômica;                                                            
  - códigos de saída já existentes.                                             
                                                                                
  Não altere regras financeiras ou resultados históricos.                       
                                                                                
  ## Migração dos consumidores de Main.run                                      
                                                                                
  Localize todas as chamadas a Main.run(...) dentro de tests/java.              
                                                                                
  Migre toda chamada que pretende ultrapassar o parser e testar leitura,        
  envelope, pipeline ou escrita para fornecer também:                           
                                                                                
  --politica exemplos/envelope/politica-v4.json                                 
  --cambio exemplos/envelope/cambio.json                                        
                                                                                
  Regras:                                                                       
                                                                                
  - testes de sucesso devem usar as quatro flags;                               
  - ExemploCompletoTest deve usar as quatro flags;                              
  - cenários de EscritaAtomicaSaidaTest que precisam ultrapassar o parser devem 
  usar as quatro flags;                                                         
  - testes de entrada inexistente ou envelope inválido devem receber política e 
  câmbio válidos para continuarem falhando na camada correta;                   
  - não migre um cenário que deliberadamente testa flag ausente ou o comando    
  antigo de duas flags;                                                         
  - somente o teste específico do comando antigo deve continuar chamando a CLI  
  apenas com --input e --output.                                                
                                                                                
  Não deixe nenhum consumidor histórico usando acidentalmente o contrato        
  antigo.                                                                       
                                                                                
  ## CliContratoTest                                                            
                                                                                
  Cubra explicitamente:                                                         
                                                                                
  - sucesso com as quatro flags em ordem arbitrária;                            
  - subcomando ausente;                                                         
  - subcomando diferente de calcular;                                           
  - token posicional extra;                                                     
  - flag na última posição sem valor;                                           
  - quantidade ímpar de tokens;                                                 
  - flag repetida;                                                              
  - flag desconhecida;                                                          
  - ausência isolada de --input;                                                
  - ausência isolada de --output;                                               
  - ausência isolada de --politica;                                             
  - ausência isolada de --cambio;                                               
  - comando antigo apenas com --input e --output;                               
  - nenhum cenário de erro de parsing modifica o arquivo de saída;              
  - falhas imprimem mensagem em stderr e não produzem saída indevida em stdout, 
  conforme o contrato atual.                                                    
                                                                                
  Use as convenções e helpers já existentes nos testes. Não reescreva a classe  
  inteira sem necessidade.                                                      
                                                                                
  ## Verificação                                                                
                                                                                
  Execute:                                                                      
                                                                                
  mvn -q test                                                                   
  "-Dtest=CliContratoTest,EscritaAtomicaSaidaTest,ExemploCompletoTest"          
  mvn -q test                                                                   
                                                                                
  Não altere tasks.md ainda.                                                    
  Não execute /export, git add, commit ou push.                                 
  Não execute git diff ou git status.                                           
  Não imprima os arquivos completos no terminal.                                
                                                                                
  Ao final, informe somente:                                                    
                                                                                
  - se os dois comandos Maven passaram;                                         
  - total de testes da suíte completa;                                          
  - quais dos quatro arquivos previstos foram efetivamente alterados.           

  Searched for 5 patterns, read 1 file, listed 1 directory (ctrl+o to expand)

● Good, only 3 files call Main.run, matching what's expected. Let me read all
  three test files fully.

  Read 3 files (ctrl+o to expand)

● Now I have full context. Let me modify Main.java first — rewriting only the
  parsing section and the USO constant.

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 3 lines
      31  import java.nio.file.Files;
      32  import java.nio.file.Path;
      33  import java.nio.file.StandardCopyOption;
      34 +import java.util.LinkedHashMap;                                
      35  import java.util.List;
      36 +import java.util.Map;                                          
      37 +import java.util.Set;                                          
      38  
      39  /**
      40   * Orquestra a CLI (spec §3, plan §3, DT-003, DT-010): interpre
          ta {@code

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 3 lines, removed 1 line
      45   */
      46  public final class Main {
      47  
      48 -    private static final String USO = "Uso: java -jar motor-ree
         -mbolso.jar calcular --input <arquivo> --output <arquivo>";     
      48 +    private static final String USO = "Uso: java -jar motor-ree
         +mbolso.jar calcular --input <arquivo> --output <arquivo> --poli
         +tica <arquivo> --cambio <arquivo>";                            
      49  
      50 +    private static final Set<String> FLAGS_ACEITAS = Set.of("--
         +input", "--output", "--politica", "--cambio");                 
      51 +                                                               
      52      /**
      53       * Ponto de simulação de falha, exclusivo de teste, para ex
          ercitar a
      54       * preservação do destino quando a escrita falha imediatame
          nte antes da

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 33 lines, removed 23 lines
       69              return 2;
       70          }
       71  
       72 -        String inputPath = null;                              
       73 -        String outputPath = null;                             
       72 +        int restantes = args.length - 1;                      
       73 +        if (restantes % 2 != 0) {                             
       74 +            err.println("Quantidade inválida de argumentos: fl
          +ags devem vir em pares flag valor");                          
       75 +            return 2;                                         
       76 +        }                                                     
       77  
       75 -        for (int i = 1; i < args.length; i++) {               
       76 -            switch (args[i]) {                                
       77 -                case "--input":                               
       78 -                    if (i + 1 >= args.length) {               
       79 -                        err.println("Argumento obrigatório aus
          -ente: valor de --input");                                     
       80 -                        return 2;                             
       81 -                    }                                         
       82 -                    inputPath = args[++i];                    
       83 -                    break;                                    
       84 -                case "--output":                              
       85 -                    if (i + 1 >= args.length) {               
       86 -                        err.println("Argumento obrigatório aus
          -ente: valor de --output");                                    
       87 -                        return 2;                             
       88 -                    }                                         
       89 -                    outputPath = args[++i];                   
       90 -                    break;                                    
       91 -                default:                                      
       92 -                    err.println("Argumento desconhecido: " + a
          -rgs[i]);                                                      
       93 -                    return 2;                                 
       78 +        Map<String, String> opcoes = new LinkedHashMap<>();   
       79 +        for (int i = 1; i < args.length; i += 2) {            
       80 +            String flag = args[i];                            
       81 +            String valor = args[i + 1];                       
       82 +                                                              
       83 +            if (!FLAGS_ACEITAS.contains(flag)) {              
       84 +                err.println("Argumento desconhecido: " + flag)
          +;                                                             
       85 +                return 2;                                     
       86              }
       87 +            if (opcoes.containsKey(flag)) {                   
       88 +                err.println("Argumento repetido: " + flag);   
       89 +                return 2;                                     
       90 +            }                                                 
       91 +            opcoes.put(flag, valor);                          
       92          }
       93  
       97 -        if (inputPath == null) {                              
       94 +        if (!opcoes.containsKey("--input")) {                 
       95              err.println("Argumento obrigatório ausente: --inpu
           t");
       96              return 2;
       97          }
      101 -        if (outputPath == null) {                             
       98 +        if (!opcoes.containsKey("--output")) {                
       99              err.println("Argumento obrigatório ausente: --outp
           ut");
      100              return 2;
      101          }
      102 +        if (!opcoes.containsKey("--politica")) {              
      103 +            err.println("Argumento obrigatório ausente: --poli
          +tica");                                                       
      104 +            return 2;                                         
      105 +        }                                                     
      106 +        if (!opcoes.containsKey("--cambio")) {                
      107 +            err.println("Argumento obrigatório ausente: --camb
          +io");                                                         
      108 +            return 2;                                         
      109 +        }                                                     
      110  
      111 +        String inputPath = opcoes.get("--input");             
      112 +        String outputPath = opcoes.get("--output");           
      113 +        String politicaPath = opcoes.get("--politica");       
      114 +        String cambioPath = opcoes.get("--cambio");           
      115 +                                                              
      116          Path input = Path.of(inputPath);
      117          if (!Files.isRegularFile(input)) {
      118              err.println("Arquivo de entrada não encontrado: " 
           + inputPath);

  Read 1 file (ctrl+o to expand)

● Now let's rewrite CliContratoTest.java with the migrated scenarios and the new
  parser coverage required by T-034.

● Write(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 279 lines, removed 15 lines
      18  import static org.junit.jupiter.api.Assertions.assertTrue;
      19  
      20  /**
      21 - * Cobre o contrato de CLI (T-002, T-019) e DT-003 — os três có
         -digos de saída                                                 
      22 - * (0, 2 e 3) num único comando {@code mvn test}. Não atribui R
         -N ou CA, porque                                                
      23 - * nenhum existe para contrato de execução.                    
      21 + * Cobre o contrato de CLI (T-002, T-019, T-034) e DT-003/DT-01
         +8 — os três                                                    
      22 + * códigos de saída (0, 2 e 3) e o parser de quatro flags obrig
         +atórias num                                                    
      23 + * único comando {@code mvn test}. Não atribui RN, salvo DT-018
         +/CA-041/CA-042                                                 
      24 + * (contrato de execução).                                     
      25   */
      25 -@DisplayName("Contrato de CLI — T-002 / T-019 / DT-003")       
      26 +@DisplayName("Contrato de CLI — T-002 / T-019 / T-034 / DT-003 
         +/ DT-018")                                                     
      27  class CliContratoTest {
      28  
      29 +    private static final String POLITICA = Path.of("exemplos", 
         +"envelope", "politica-v4.json").toString();                    
      30 +    private static final String CAMBIO = Path.of("exemplos", "e
         +nvelope", "cambio.json").toString();                           
      31 +                                                               
      32      private static final class Resultado {
      33          final int codigo;
      34          final String stdout;
     ...
       53          }
       54      }
       55  
       56 +    // ---- Contrato de quatro flags — sucesso ---------------
          +--------------                                                
       57 +                                                              
       58      @Test
       53 -    @DisplayName("argumento obrigatório ausente (--input não i
          -nformado) retorna exit 2")                                    
       54 -    void argumentoObrigatorioAusente(@TempDir Path tempDir) { 
       59 +    @DisplayName("sucesso com as quatro flags em ordem arbitrá
          +ria retorna exit 0")                                          
       60 +    void sucesso_quatroFlagsOrdemArbitraria(@TempDir Path temp
          +Dir) throws Exception {                                       
       61 +        Path input = tempDir.resolve("entrada.json");         
       62 +        Files.writeString(input, """                          
       63 +                {                                             
       64 +                  "periodo": { "inicio": "2026-07-01", "fim": 
          +"2026-07-31" },                                               
       65 +                  "despesas": []                              
       66 +                }                                             
       67 +                """, StandardCharsets.UTF_8);                 
       68          Path output = tempDir.resolve("resultado.json");
       69  
       57 -        Resultado resultado = executar("calcular", "--output",
          - output.toString());                                          
       70 +        Resultado resultado = executar(                       
       71 +                "calcular",                                   
       72 +                "--cambio", CAMBIO,                           
       73 +                "--input", input.toString(),                  
       74 +                "--politica", POLITICA,                       
       75 +                "--output", output.toString());               
       76  
       77 +        assertEquals(0, resultado.codigo);                    
       78 +        assertEquals("", resultado.stderr);                   
       79 +        assertEquals("", resultado.stdout);                   
       80 +        assertTrue(Files.exists(output), "--output deve ser cr
          +iado em caso de sucesso");                                    
       81 +                                                              
       82 +        JsonNode raiz = new ObjectMapper().readTree(output.toF
          +ile());                                                       
       83 +        assertTrue(raiz.has("total_reembolsavel"), "JSON de sa
          +ída deve ser parseável e conter total_reembolsavel");         
       84 +        assertEquals(0, new BigDecimal("0.00").compareTo(raiz.
          +get("total_reembolsavel").decimalValue()));                   
       85 +    }                                                         
       86 +                                                              
       87 +    // ---- Subcomando ---------------------------------------
          +---------------                                               
       88 +                                                              
       89 +    @Test                                                     
       90 +    @DisplayName("subcomando ausente (nenhum argumento) retorn
          +a exit 2")                                                    
       91 +    void subcomandoAusente() {                                
       92 +        Resultado resultado = executar();                     
       93 +                                                              
       94          assertEquals(2, resultado.codigo);
       60 -        assertTrue(resultado.stderr.contains("Argumento obriga
          -tório ausente"),                                              
       61 -                "stderr deveria conter 'Argumento obrigatório 
          -ausente', mas foi: " + resultado.stderr);                     
       95 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de uso");                                   
       96          assertEquals("", resultado.stdout);
       63 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando um argumento obrigatório está ausente");     
       97      }
       98  
       99      @Test
      100 +    @DisplayName("subcomando diferente de 'calcular' retorna e
          +xit 2")                                                       
      101 +    void subcomandoDiferenteDeCalcular(@TempDir Path tempDir) 
          +{                                                             
      102 +        Path output = tempDir.resolve("resultado.json");      
      103 +                                                              
      104 +        Resultado resultado = executar(                       
      105 +                "executar", "--input", "entrada.json", "--outp
          +ut", output.toString(),                                       
      106 +                "--politica", POLITICA, "--cambio", CAMBIO);  
      107 +                                                              
      108 +        assertEquals(2, resultado.codigo);                    
      109 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de uso");                                   
      110 +        assertEquals("", resultado.stdout);                   
      111 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando o subcomando é inválido");                   
      112 +    }                                                         
      113 +                                                              
      114 +    // ---- Formato dos tokens após 'calcular' ---------------
          +----------------                                              
      115 +                                                              
      116 +    @Test                                                     
      117 +    @DisplayName("token posicional extra depois de 'calcular' 
          +retorna exit 2")                                              
      118 +    void tokenPosicionalExtra(@TempDir Path tempDir) {        
      119 +        Path input = tempDir.resolve("entrada.json");         
      120 +        Path output = tempDir.resolve("resultado.json");      
      121 +                                                              
      122 +        Resultado resultado = executar(                       
      123 +                "calcular",                                   
      124 +                "--input", input.toString(),                  
      125 +                "--output", output.toString(),                
      126 +                "--politica", POLITICA,                       
      127 +                "--cambio", CAMBIO,                           
      128 +                "extra");                                     
      129 +                                                              
      130 +        assertEquals(2, resultado.codigo);                    
      131 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      132 +        assertEquals("", resultado.stdout);                   
      133 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando há token posicional extra");                 
      134 +    }                                                         
      135 +                                                              
      136 +    @Test                                                     
      137 +    @DisplayName("flag na última posição sem valor retorna exi
          +t 2")                                                         
      138 +    void flagUltimaPosicaoSemValor(@TempDir Path tempDir) {   
      139 +        Path input = tempDir.resolve("entrada.json");         
      140 +        Path output = tempDir.resolve("resultado.json");      
      141 +                                                              
      142 +        Resultado resultado = executar(                       
      143 +                "calcular",                                   
      144 +                "--input", input.toString(),                  
      145 +                "--output", output.toString(),                
      146 +                "--politica", POLITICA,                       
      147 +                "--cambio");                                  
      148 +                                                              
      149 +        assertEquals(2, resultado.codigo);                    
      150 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      151 +        assertEquals("", resultado.stdout);                   
      152 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando uma flag fica sem valor");                   
      153 +    }                                                         
      154 +                                                              
      155 +    @Test                                                     
      156 +    @DisplayName("quantidade ímpar de tokens depois de 'calcul
          +ar' retorna exit 2")                                          
      157 +    void quantidadeImparDeTokens(@TempDir Path tempDir) {     
      158 +        Path output = tempDir.resolve("resultado.json");      
      159 +                                                              
      160 +        Resultado resultado = executar("calcular", "--input", 
          +"entrada.json", "--output");                                  
      161 +                                                              
      162 +        assertEquals(2, resultado.codigo);                    
      163 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      164 +        assertEquals("", resultado.stdout);                   
      165 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando a quantidade de tokens é ímpar");            
      166 +    }                                                         
      167 +                                                              
      168 +    @Test                                                     
      169 +    @DisplayName("flag repetida retorna exit 2")              
      170 +    void flagRepetida(@TempDir Path tempDir) {                
      171 +        Path input = tempDir.resolve("entrada.json");         
      172 +        Path output = tempDir.resolve("resultado.json");      
      173 +                                                              
      174 +        Resultado resultado = executar(                       
      175 +                "calcular",                                   
      176 +                "--input", input.toString(),                  
      177 +                "--output", output.toString(),                
      178 +                "--politica", POLITICA,                       
      179 +                "--cambio", CAMBIO,                           
      180 +                "--input", "outra-entrada.json");             
      181 +                                                              
      182 +        assertEquals(2, resultado.codigo);                    
      183 +        assertTrue(resultado.stderr.contains("repetido"),     
      184 +                "stderr deveria indicar flag repetida, mas foi
          +: " + resultado.stderr);                                      
      185 +        assertEquals("", resultado.stdout);                   
      186 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando uma flag é repetida");                       
      187 +    }                                                         
      188 +                                                              
      189 +    @Test                                                     
      190 +    @DisplayName("flag desconhecida retorna exit 2")          
      191 +    void flagDesconhecida(@TempDir Path tempDir) {            
      192 +        Path input = tempDir.resolve("entrada.json");         
      193 +        Path output = tempDir.resolve("resultado.json");      
      194 +                                                              
      195 +        Resultado resultado = executar(                       
      196 +                "calcular",                                   
      197 +                "--input", input.toString(),                  
      198 +                "--output", output.toString(),                
      199 +                "--politica", POLITICA,                       
      200 +                "--cambio", CAMBIO,                           
      201 +                "--extra", "valor");                          
      202 +                                                              
      203 +        assertEquals(2, resultado.codigo);                    
      204 +        assertTrue(resultado.stderr.contains("desconhecido"), 
      205 +                "stderr deveria indicar flag desconhecida, mas
          + foi: " + resultado.stderr);                                  
      206 +        assertEquals("", resultado.stdout);                   
      207 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando há flag desconhecida");                      
      208 +    }                                                         
      209 +                                                              
      210 +    // ---- Ausência isolada de cada flag obrigatória --------
          +----------------                                              
      211 +                                                              
      212 +    @Test                                                     
      213 +    @DisplayName("ausência isolada de --input retorna exit 2")
      214 +    void ausenciaIsoladaDeInput(@TempDir Path tempDir) {      
      215 +        Path output = tempDir.resolve("resultado.json");      
      216 +                                                              
      217 +        Resultado resultado = executar(                       
      218 +                "calcular",                                   
      219 +                "--output", output.toString(),                
      220 +                "--politica", POLITICA,                       
      221 +                "--cambio", CAMBIO);                          
      222 +                                                              
      223 +        assertEquals(2, resultado.codigo);                    
      224 +        assertTrue(resultado.stderr.contains("--input"),      
      225 +                "stderr deveria indicar ausência de --input, m
          +as foi: " + resultado.stderr);                                
      226 +        assertEquals("", resultado.stdout);                   
      227 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --input está ausente");                      
      228 +    }                                                         
      229 +                                                              
      230 +    @Test                                                     
      231 +    @DisplayName("ausência isolada de --output retorna exit 2"
          +)                                                             
      232 +    void ausenciaIsoladaDeOutput(@TempDir Path tempDir) {     
      233 +        Path input = tempDir.resolve("entrada.json");         
      234 +        Path output = tempDir.resolve("resultado.json");      
      235 +                                                              
      236 +        Resultado resultado = executar(                       
      237 +                "calcular",                                   
      238 +                "--input", input.toString(),                  
      239 +                "--politica", POLITICA,                       
      240 +                "--cambio", CAMBIO);                          
      241 +                                                              
      242 +        assertEquals(2, resultado.codigo);                    
      243 +        assertTrue(resultado.stderr.contains("--output"),     
      244 +                "stderr deveria indicar ausência de --output, 
          +mas foi: " + resultado.stderr);                               
      245 +        assertEquals("", resultado.stdout);                   
      246 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --output está ausente");                     
      247 +    }                                                         
      248 +                                                              
      249 +    @Test                                                     
      250 +    @DisplayName("ausência isolada de --politica retorna exit 
          +2")                                                           
      251 +    void ausenciaIsoladaDePolitica(@TempDir Path tempDir) {   
      252 +        Path input = tempDir.resolve("entrada.json");         
      253 +        Path output = tempDir.resolve("resultado.json");      
      254 +                                                              
      255 +        Resultado resultado = executar(                       
      256 +                "calcular",                                   
      257 +                "--input", input.toString(),                  
      258 +                "--output", output.toString(),                
      259 +                "--cambio", CAMBIO);                          
      260 +                                                              
      261 +        assertEquals(2, resultado.codigo);                    
      262 +        assertTrue(resultado.stderr.contains("--politica"),   
      263 +                "stderr deveria indicar ausência de --politica
          +, mas foi: " + resultado.stderr);                             
      264 +        assertEquals("", resultado.stdout);                   
      265 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --politica está ausente");                   
      266 +    }                                                         
      267 +                                                              
      268 +    @Test                                                     
      269 +    @DisplayName("ausência isolada de --cambio retorna exit 2"
          +)                                                             
      270 +    void ausenciaIsoladaDeCambio(@TempDir Path tempDir) {     
      271 +        Path input = tempDir.resolve("entrada.json");         
      272 +        Path output = tempDir.resolve("resultado.json");      
      273 +                                                              
      274 +        Resultado resultado = executar(                       
      275 +                "calcular",                                   
      276 +                "--input", input.toString(),                  
      277 +                "--output", output.toString(),                
      278 +                "--politica", POLITICA);                      
      279 +                                                              
      280 +        assertEquals(2, resultado.codigo);                    
      281 +        assertTrue(resultado.stderr.contains("--cambio"),     
      282 +                "stderr deveria indicar ausência de --cambio, 
          +mas foi: " + resultado.stderr);                               
      283 +        assertEquals("", resultado.stdout);                   
      284 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --cambio está ausente");                     
      285 +    }                                                         
      286 +                                                              
      287 +    // ---- Comando antigo (duas flags) — único cenário autori
          +zado a usá-lo -                                               
      288 +                                                              
      289 +    @Test                                                     
      290 +    @DisplayName("comando antigo apenas com --input e --output
          + retorna exit 2 (CA-042)")                                    
      291 +    void comandoAntigoApenasInputEOutput(@TempDir Path tempDir
          +) {                                                           
      292 +        Path input = tempDir.resolve("entrada.json");         
      293 +        Path output = tempDir.resolve("resultado.json");      
      294 +                                                              
      295 +        Resultado resultado = executar("calcular", "--input", 
          +input.toString(), "--output", output.toString());             
      296 +                                                              
      297 +        assertEquals(2, resultado.codigo);                    
      298 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      299 +        assertEquals("", resultado.stdout);                   
      300 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado pelo comando antigo de duas flags");                
      301 +    }                                                         
      302 +                                                              
      303 +    // ---- Camadas posteriores ao parser (migradas para as qu
          +atro flags) ---                                               
      304 +                                                              
      305 +    @Test                                                     
      306      @DisplayName("arquivo de entrada inexistente retorna exit 
           2")
      307      void arquivoDeEntradaInexistente(@TempDir Path tempDir) {
      308          Path input = tempDir.resolve("nao-existe.json");
      309          Path output = tempDir.resolve("resultado.json");
      310  
       72 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      311 +        Resultado resultado = executar(                       
      312 +                "calcular",                                   
      313 +                "--input", input.toString(),                  
      314 +                "--output", output.toString(),                
      315 +                "--politica", POLITICA,                       
      316 +                "--cambio", CAMBIO);                          
      317  
      318          assertEquals(2, resultado.codigo);
      319          assertTrue(resultado.stderr.contains("Arquivo de entra
           da não encontrado"),
     ...
      329          Files.writeString(input, "{ \"despesas\": [ ", Standar
           dCharsets.UTF_8);
      330          Path output = tempDir.resolve("resultado.json");
      331  
       88 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      332 +        Resultado resultado = executar(                       
      333 +                "calcular",                                   
      334 +                "--input", input.toString(),                  
      335 +                "--output", output.toString(),                
      336 +                "--politica", POLITICA,                       
      337 +                "--cambio", CAMBIO);                          
      338  
      339          assertEquals(2, resultado.codigo);
      340          assertTrue(resultado.stderr.contains("JSON de entrada 
           sintaticamente inválido"),
     ...
      350          Files.writeString(input, "   \n\t  ", StandardCharsets
           .UTF_8);
      351          Path output = tempDir.resolve("resultado.json");
      352  
      104 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      353 +        Resultado resultado = executar(                       
      354 +                "calcular",                                   
      355 +                "--input", input.toString(),                  
      356 +                "--output", output.toString(),                
      357 +                "--politica", POLITICA,                       
      358 +                "--cambio", CAMBIO);                          
      359  
      360          assertEquals(2, resultado.codigo);
      361          assertTrue(resultado.stderr.contains("JSON de entrada 
           sintaticamente inválido"),
     ...
      371          Files.writeString(input, "{ } { }", StandardCharsets.U
           TF_8);
      372          Path output = tempDir.resolve("resultado.json");
      373  
      120 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      374 +        Resultado resultado = executar(                       
      375 +                "calcular",                                   
      376 +                "--input", input.toString(),                  
      377 +                "--output", output.toString(),                
      378 +                "--politica", POLITICA,                       
      379 +                "--cambio", CAMBIO);                          
      380  
      381          assertEquals(2, resultado.codigo);
      382          assertTrue(resultado.stderr.contains("JSON de entrada 
           sintaticamente inválido"),
     ...
      397                  """, StandardCharsets.UTF_8);
      398          Path output = tempDir.resolve("resultado.json");
      399  
      141 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      400 +        Resultado resultado = executar(                       
      401 +                "calcular",                                   
      402 +                "--input", input.toString(),                  
      403 +                "--output", output.toString(),                
      404 +                "--politica", POLITICA,                       
      405 +                "--cambio", CAMBIO);                          
      406  
      407          assertEquals(0, resultado.codigo);
      408          assertEquals("", resultado.stderr);

● Now let's migrate EscritaAtomicaSaidaTest.java to use the four flags.

● Update(tests\java\com\desafio\reembolso\EscritaAtomicaSaidaTest.java)
  ⎿  Added 2 lines
      31  class EscritaAtomicaSaidaTest {
      32  
      33      private static final String CONTEUDO_PREEXISTENTE = "{\"res
          ultado\":\"antigo\"}";
      34 +    private static final String POLITICA = Path.of("exemplos", 
         +"envelope", "politica-v4.json").toString();                    
      35 +    private static final String CAMBIO = Path.of("exemplos", "e
         +nvelope", "cambio.json").toString();                           
      36  
      37      @AfterEach
      38      void restaurarGatilhoDeFalha() {

● Update(tests\java\com\desafio\reembolso\EscritaAtomicaSaidaTest.java)
  ⎿  Added 3 lines, removed 1 line
       96          Path output = tempDir.resolve("resultado.json");
       97          escreverOutputPreexistente(output);
       98  
       99 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
       99 +        Resultado resultado = executar(                       
      100 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      101 +                "--politica", POLITICA, "--cambio", CAMBIO);  
      102  
      103          assertEquals(2, resultado.codigo);
      104          assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(o
           utput, StandardCharsets.UTF_8));

● Update(tests\java\com\desafio\reembolso\EscritaAtomicaSaidaTest.java)
  ⎿  Added 3 lines, removed 1 line
      120          Path output = tempDir.resolve("resultado.json");
      121          escreverOutputPreexistente(output);
      122  
      123 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      123 +        Resultado resultado = executar(                       
      124 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      125 +                "--politica", POLITICA, "--cambio", CAMBIO);  
      126  
      127          assertEquals(3, resultado.codigo);
      128          assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(o
           utput, StandardCharsets.UTF_8));

● Update(tests\java\com\desafio\reembolso\EscritaAtomicaSaidaTest.java)
  ⎿  Added 3 lines, removed 1 line
      140          escreverOutputPreexistente(output);
      141  
      142          Main.simularFalhaAntesDaSubstituicao = true;
      143 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      143 +        Resultado resultado = executar(                       
      144 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      145 +                "--politica", POLITICA, "--cambio", CAMBIO);  
      146  
      147          assertEquals(2, resultado.codigo);
      148          assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(o
           utput, StandardCharsets.UTF_8));

● Update(tests\java\com\desafio\reembolso\EscritaAtomicaSaidaTest.java)
  ⎿  Added 3 lines, removed 1 line
      159          Path output = tempDir.resolve("resultado.json");
      160          escreverOutputPreexistente(output);
      161  
      162 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      162 +        Resultado resultado = executar(                       
      163 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      164 +                "--politica", POLITICA, "--cambio", CAMBIO);  
      165  
      166          assertEquals(0, resultado.codigo);
      167          String conteudoFinal = Files.readString(output, Standa
           rdCharsets.UTF_8);

● Update(tests\java\com\desafio\reembolso\EscritaAtomicaSaidaTest.java)
  ⎿  Added 3 lines, removed 1 line
      181          Files.writeString(input, jsonEnvelopeValido(), Standar
           dCharsets.UTF_8);
      182          Path output = tempDir.resolve("resultado-novo.json");
      183  
      184 -        Resultado resultado = executar("calcular", "--input", 
          -input.toString(), "--output", output.toString());             
      184 +        Resultado resultado = executar(                       
      185 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      186 +                "--politica", POLITICA, "--cambio", CAMBIO);  
      187  
      188          assertEquals(0, resultado.codigo);
      189          assertTrue(Files.exists(output));

● Now ExemploCompletoTest.java:

● Update(tests\java\com\desafio\reembolso\ExemploCompletoTest.java)
  ⎿  Added 2 lines
      34  
      35      private static final Path ENTRADA = Path.of("exemplos", "de
          spesas-exemplo.json");
      36      private static final Path FIXTURE = Path.of("tests", "resou
          rces", "fixtures", "despesas-exemplo-esperado.json");
      37 +    private static final Path POLITICA = Path.of("exemplos", "e
         +nvelope", "politica-v4.json");                                 
      38 +    private static final Path CAMBIO = Path.of("exemplos", "env
         +elope", "cambio.json");                                        
      39  
      40      @Test
      41      @DisplayName("processa o arquivo de exemplo e coincide estr
          uturalmente com o fixture manual (14 registros, total 585,43)")

● Update(tests\java\com\desafio\reembolso\ExemploCompletoTest.java)  ⎿  Added 6 lines, removed 1 line
      48          try (PrintStream out = new PrintStream(outBuffer, true,
           StandardCharsets.UTF_8);
      49               PrintStream err = new PrintStream(errBuffer, true,
           StandardCharsets.UTF_8)) {
      50              codigo = Main.run(
      51 -                    new String[]{"calcular", "--input", ENTRADA
         -.toString(), "--output", saida.toString()},                    
      51 +                    new String[]{                              
      52 +                            "calcular",                        
      53 +                            "--input", ENTRADA.toString(),     
      54 +                            "--output", saida.toString(),      
      55 +                            "--politica", POLITICA.toString(), 
      56 +                            "--cambio", CAMBIO.toString()},    
      57                      out, err);
      58              out.flush();
      59              err.flush();

● All four files edited. Now let's run the verification commands.

  Ran 1 shell command (ctrl+o to expand)

● No output from Maven usually means success under -q. Let's run the full suite
  next.

  Ran 1 shell command (ctrl+o to expand)

● Both commands passed silently under -q (no output = success). Let me get the
  total test count from the surefire reports.

  Ran 2 shell commands (ctrl+o to expand)

● Both Maven commands passed cleanly, and the full suite reports 492 tests, 0 
  failures, 0 errors (BUILD SUCCESS).

  Files effectively altered (as scoped by T-034):
  - src/main/java/com/desafio/reembolso/Main.java
  - tests/java/com/desafio/reembolso/CliContratoTest.java
  - tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTest.java
  - tests/java/com/desafio/reembolso/ExemploCompletoTest.java

✻ Sautéed for 4m 48s

※ recap: Implementing T-034 (four-flag CLI parser for the reembolso engine) — done, both Maven verification commands passed with 492 tests green. Next: awaiting your review before touching anything else (T-035 not started). (disable recaps in /config)

> Antes de fechar a T-034, faça uma única correção documental em:               
                                                                                
  src/main/java/com/desafio/reembolso/Main.java                                 
                                                                                
  O JavaDoc da classe ainda descreve o contrato antigo:                         
                                                                                
  calcular --input ... --output ...                                             
                                                                                
  e afirma que o pipeline possui exatamente onze passos.                        
                                                                                
  Atualize somente esse JavaDoc para:                                           
                                                                                
  - apresentar o contrato atual com as quatro flags:                            
    calcular --input ... --output ... --politica ... --cambio ...;              
  - evitar fixar uma quantidade exata de passos, pois novos estágios ainda      
  serão integrados nas próximas tasks;                                          
  - manter a descrição de que Main apenas orquestra a CLI, o pipeline e os      
  códigos de saída, sem implementar regras de negócio.                          
                                                                                
  Não altere nenhuma linha de comportamento.                                    
  Não altere os testes.                                                         
  Não altere tasks.md.                                                          
  Não execute git add, commit, push ou /export.                                 
                                                                                
  Depois execute:                                                               
                                                                                
  mvn -q test                                                                   
  "-Dtest=CliContratoTest,EscritaAtomicaSaidaTest,ExemploCompletoTest"          
  mvn -q test                                                                   
                                                                                
  Ao final, informe somente:                                                    
  - se os dois comandos passaram;                                               
  - o total de testes da suíte completa.                                        
                                                                                
  Não imprima o arquivo completo no terminal.                                   

  Read 1 file (ctrl+o to expand)

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 5 lines, removed 5 lines
      37  import java.util.Set;
      38  
      39  /**
      40 - * Orquestra a CLI (spec §3, plan §3, DT-003, DT-010): interpre
         -ta {@code                                                      
      41 - * calcular --input ... --output ...}, executa os onze passos d
         -o pipeline                                                     
      42 - * (plan §2) na ordem canônica e escreve o resultado atomicamen
         -te. Não                                                        
      43 - * implementa nenhuma regra de negócio — apenas encadeia os est
         -ágios já                                                       
      44 - * prontos e traduz sucesso/falha em código de saída.          
      40 + * Orquestra a CLI (spec §3, plan §3, DT-003, DT-010, DT-018): 
         +interpreta                                                     
      41 + * {@code calcular --input ... --output ... --politica ... --ca
         +mbio ...},                                                     
      42 + * executa os estágios do pipeline (plan §2) na ordem canônica 
         +e escreve o                                                    
      43 + * resultado atomicamente. Não implementa nenhuma regra de negó
         +cio — apenas                                                   
      44 + * encadeia os estágios já prontos e traduz sucesso/falha em có
         +digo de saída.                                                 
      45   */
      46  public final class Main {
      47  

  Ran 2 shell commands (ctrl+o to expand)

● Ambos os comandos passaram. Total da suíte completa: 492 testes, 0 falhas, 0
  erros.

✻ Cogitated for 27s

※ recap: T-034 is done: the CLI now requires four flags (input, output, politica, cambio) and the JavaDoc is updated to match, with all 492 tests passing. Next: awaiting your go-ahead to start T-035. (disable recaps in /config)

> A T-034 foi revisada e aprovada.                                              
                                                                                
  Em specs/001-motor-reembolso/tasks.md, altere somente a T-034:                
                                                                                
  - checkbox do título de [ ] para [x];                                         
  - Status de [ ] pendente para [x] concluída.                                  
                                                                                
  Não altere nenhuma outra task ou arquivo.                                     
  Não execute git add, commit ou push.                                          

  Searched for 1 pattern, read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      808  
      809  ### Bloco D — CLI de quatro flags
      810  
      811 -- [ ] **T-034** — `Main.java`: parser de quatro flags         
      811 +- [x] **T-034** — `Main.java`: parser de quatro flags         
      812    - **O que faz:** reescreve o parser de argumentos de `Main.r
           un(...)` conforme `plan.md` §3/DT-018: primeiro token deve ser
            exatamente `"calcular"` (único posicional aceito); tokens res
           tantes consumidos aos pares `flag valor`; pares acumulados num
            `Map<String, String>` contando ocorrências por chave; ao fina
           l, valida que as quatro chaves `--input`/`--output`/`--politic
           a`/`--cambio` estão presentes, cada uma exatamente uma vez, e 
           que nenhuma chave desconhecida apareceu. Qualquer violação → e
           xit `2`. **Não** carrega `--politica`/`--cambio` ainda (isso é
            T-035) — nesta task, as duas flags só são reconhecidas e arma
           zenadas. Como o contrato de execução muda de duas para quatro 
           flags obrigatórias, **todo** consumidor histórico de `Main.run
           (...)` que pretende alcançar leitura, envelope, pipeline ou es
           crita — não só `CliContratoTest` — precisa passar a fornecer a
           s quatro flags nesta mesma task, ou seu cenário passa a falhar
            no parser por um motivo alheio ao que o teste pretende verifi
           car.
      813    - **RN atendidas:** RN-022 (parcial — contrato de execução).
      814    - **CA atendidos:** CA-041, CA-042.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      837      mvn -q test
      838      ```
      839    - **Commit sugerido:** `feat(T-034): reescreve parser da CLI
            para quatro flags obrigatorias e migra consumidores historico
           s de Main.run`
      840 -  - **Status:** [ ] pendente                                  
      840 +  - **Status:** [x] concluída                                 
      841  
      842  - [ ] **T-035** — `Main.java`: carregar e validar política e c
           âmbio antes do envelope
      843    - **O que faz:** `Main.run(...)` passa a chamar `LeitorPolit
           ica.ler(politicaPath)` e `LeitorCambio.ler(cambioPath)` **ante
           s** de ler e validar o envelope de despesas (spec 8.1, passo 1
            antes do passo 2) — qualquer `PoliticaInvalidaException`/`Cam
           bioInvalidoException` retorna exit `2`, sem sequer abrir o arq
           uivo de entrada. A própria construção dos `Path` (`Path.of(pol
           iticaPath)`, `Path.of(cambioPath)`, e também `Path.of(inputPat
           h)`/`Path.of(outputPath)`) acontece **dentro** do mesmo bloco 
           protegido — `Path.of(...)` pode lançar `InvalidPathException` 
           quando o texto recebido não é um caminho válido no sistema ope
           racional (ex.: caracteres proibidos no Windows), e esse cenári
           o é tratado exatamente como as demais falhas de arquivo: exit 
           `2`, mensagem apenas em stderr, stdout vazio, e um `--output` 
           preexistente preservado intacto (nenhuma tentativa de escrita 
           ocorre antes da validação de política/câmbio). Os objetos `Pol
           iticaExterna`/`TabelaCambio` resultantes ainda não são usados 
           pelo pipeline de regras nesta task (isso começa em T-038/T-042
           /T-046) — aqui o objetivo é só o contrato de execução (AMB-034
           ) e a ordem de validação.

● T-034 marcada como concluída em tasks.md (checkbox e Status). Nenhuma outra
  task ou arquivo foi tocado.

✻ Cooked for 14s

> A T-034 foi revisada e aprovada.                                              
                                                                                
  Em specs/001-motor-reembolso/tasks.md, altere somente a T-034:                
                                                                                
  - checkbox do título de [ ] para [x];                                         
  - Status de [ ] pendente para [x] concluída.                                  
                                                                                
  Não altere nenhuma outra task ou arquivo.                                     
  Não execute git add, commit ou push.                                          

● Isso já foi feito no turno anterior. Conferindo o estado atual do arquivo:

  Ran 1 shell command (ctrl+o to expand)

● A T-034 já está com o checkbox [x] e Status [x] concluída — essa alteração já
  havia sido aplicada. Nenhuma outra mudança foi feita agora.

✻ Crunched for 8s