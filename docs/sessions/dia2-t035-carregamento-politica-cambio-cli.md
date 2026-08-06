 ▐▛███▜▌   Claude Code v2.1.222
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Implemente exclusivamente a T-035 do backlog aprovado.                        
                                                                                
  Leia integralmente:                                                           
                                                                                
  - CLAUDE.md;                                                                  
  - a descrição completa da T-035 em specs/001-motor-reembolso/tasks.md;        
  - src/main/java/com/desafio/reembolso/Main.java;                              
  - src/main/java/com/desafio/reembolso/leitor/LeitorPolitica.java;             
  - src/main/java/com/desafio/reembolso/leitor/LeitorCambio.java;               
  - tests/java/com/desafio/reembolso/CliContratoTest.java;                      
  - tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTest.java.              
                                                                                
  Altere somente:                                                               
                                                                                
  - src/main/java/com/desafio/reembolso/Main.java;                              
  - tests/java/com/desafio/reembolso/CliContratoTest.java;                      
  - tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTest.java.              
                                                                                
  Não implemente T-036 ou qualquer task posterior.                              
  Não conecte ainda PoliticaExterna ou TabelaCambio ao pipeline de regras.      
                                                                                
  ## Ordem obrigatória                                                          
                                                                                
  Depois que o parser extrair os quatro textos:                                 
                                                                                
  - inputPath;                                                                  
  - outputPath;                                                                 
  - politicaPath;                                                               
  - cambioPath;                                                                 
                                                                                
  construa os quatro objetos Path dentro do mesmo bloco protegido:              
                                                                                
  - Path input;                                                                 
  - Path output;                                                                
  - Path politica;                                                              
  - Path cambio.                                                                
                                                                                
  Dentro desse mesmo bloco, após construir os Paths:                            
                                                                                
  1. chame LeitorPolitica.ler(politica);                                        
  2. chame LeitorCambio.ler(cambio).                                            
                                                                                
  Essas duas validações devem acontecer antes de:                               
                                                                                
  - verificar se o arquivo de entrada existe;                                   
  - abrir ou ler o arquivo de entrada;                                          
  - validar o envelope;                                                         
  - criar arquivo temporário;                                                   
  - escrever ou alterar o output.                                               
                                                                                
  A política deve ser validada antes do câmbio, e ambos antes do envelope.      
                                                                                
  Os objetos PoliticaExterna e TabelaCambio ainda não devem ser usados pelo     
  pipeline nesta task.                                                          
                                                                                
  ## Tratamento de erros                                                        
                                                                                
  Capture no mesmo fluxo protegido:                                             
                                                                                
  - InvalidPathException;                                                       
  - LeitorPolitica.PoliticaInvalidaException;                                   
  - LeitorCambio.CambioInvalidoException.                                       
                                                                                
  Qualquer uma dessas falhas deve:                                              
                                                                                
  - imprimir mensagem somente em stderr;                                        
  - manter stdout vazio;                                                        
  - retornar exit 2;                                                            
  - não abrir nem processar o arquivo de entrada;                               
  - não criar, truncar ou alterar o arquivo de saída;                           
  - preservar byte a byte um output preexistente.                               
                                                                                
  Também mova Path.of(inputPath) e Path.of(outputPath) para esse bloco          
  protegido, pois eles também podem lançar InvalidPathException.                
                                                                                
  Não deixe Path.of(...) fora do tratamento correspondente.                     
                                                                                
  ## Preservação do fluxo                                                       
                                                                                
  Após política e câmbio serem validados com sucesso:                           
                                                                                
  - mantenha a verificação de Files.isRegularFile(input);                       
  - mantenha a leitura do JSON de entrada;                                      
  - mantenha ValidadorEnvelope;                                                 
  - mantenha o pipeline histórico;                                              
  - mantenha a escrita atômica;                                                 
  - mantenha os códigos de saída existentes.                                    
                                                                                
  Não altere regras financeiras.                                                
  Não use ainda os modelos carregados no pipeline.                              
                                                                                
  ## Testes de política                                                         
                                                                                
  Em CliContratoTest, cubra --politica apontando para:                          
                                                                                
  - arquivo inexistente;                                                        
  - caminho ilegível, usando diretório no lugar de arquivo;                     
  - JSON sintaticamente inválido;                                               
  - política estruturalmente inválida;                                          
  - texto que cause InvalidPathException de forma determinística, como uma      
  String contendo o caractere NUL.                                              
                                                                                
  Em todos os casos:                                                            
                                                                                
  - exit 2;                                                                     
  - stdout vazio;                                                               
  - stderr não vazio;                                                           
  - input pode ser válido;                                                      
  - output não deve ser criado ou alterado.                                     
                                                                                
  ## Testes de câmbio                                                           
                                                                                
  Cubra --cambio apontando para:                                                
                                                                                
  - arquivo inexistente;                                                        
  - caminho ilegível, usando diretório no lugar de arquivo;                     
  - JSON sintaticamente inválido;                                               
  - câmbio estruturalmente inválido;                                            
  - texto que cause InvalidPathException de forma determinística.               
                                                                                
  Aplique as mesmas verificações:                                               
                                                                                
  - exit 2;                                                                     
  - stdout vazio;                                                               
  - stderr não vazio;                                                           
  - output intacto.                                                             
                                                                                
  ## Ordem de validação                                                         
                                                                                
  Adicione pelo menos um teste comprovando que política/câmbio são validados    
  antes do input.                                                               
                                                                                
  Exemplo:                                                                      
                                                                                
  - input inexistente;                                                          
  - política inválida;                                                          
  - deve prevalecer a mensagem/falha de política, demonstrando que o input      
  ainda não foi consultado.                                                     
                                                                                
  Mantenha a ordem normativa:                                                   
                                                                                
  1. política;                                                                  
  2. câmbio;                                                                    
  3. input/envelope.                                                            
                                                                                
  ## Escrita atômica                                                            
                                                                                
  Em EscritaAtomicaSaidaTest, adicione cobertura garantindo que um output       
  preexistente permanece byte a byte intacto quando:                            
                                                                                
  - a política é inválida;                                                      
  - o câmbio é inválido;                                                        
  - politicaPath ou cambioPath causa InvalidPathException.                      
                                                                                
  Nenhum arquivo temporário deve permanecer no diretório.                       
                                                                                
  Reutilize helpers existentes quando possível.                                 
                                                                                
  ## Verificação                                                                
                                                                                
  Execute:                                                                      
                                                                                
  mvn -q test "-Dtest=CliContratoTest,EscritaAtomicaSaidaTest"                  
  mvn -q test                                                                   
                                                                                
  Não altere tasks.md ainda.                                                    
  Não execute /export, git add, commit ou push.                                 
  Não execute git diff ou git status.                                           
  Não imprima os arquivos completos no terminal.                                
                                                                                
  Ao final, informe somente:                                                    
                                                                                
  - se os dois comandos Maven passaram;                                         
  - total de testes da suíte completa;                                          
  - quantos testes existem agora em CliContratoTest;                            
  - quantos testes existem agora em EscritaAtomicaSaidaTest.                    

  Searched for 1 pattern, read 4 files (ctrl+o to expand)

● Now let me read the two test files.

  Searched for 5 patterns, read 2 files (ctrl+o to expand)

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 4 lines
       1  package com.desafio.reembolso;
       2  
       3  import com.desafio.reembolso.escritor.EscritorResultado;
       4 +import com.desafio.reembolso.leitor.LeitorCambio;              
       5 +import com.desafio.reembolso.leitor.LeitorCambio.CambioInvalido
         +Exception;                                                     
       6 +import com.desafio.reembolso.leitor.LeitorPolitica;            
       7 +import com.desafio.reembolso.leitor.LeitorPolitica.PoliticaInva
         +lidaException;                                                 
       8  import com.desafio.reembolso.leitor.ValidadorEnvelope;
       9  import com.desafio.reembolso.leitor.ValidadorEnvelope.EnvelopeI
          nvalidoException;
      10  import com.desafio.reembolso.modelo.Envelope;

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 1 line
      33  import java.math.BigDecimal;
      34  import java.nio.charset.StandardCharsets;
      35  import java.nio.file.Files;
      36 +import java.nio.file.InvalidPathException;                     
      37  import java.nio.file.Path;
      38  import java.nio.file.StandardCopyOption;
      39  import java.util.LinkedHashMap;

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 23 lines, removed 1 line
      118          String politicaPath = opcoes.get("--politica");
      119          String cambioPath = opcoes.get("--cambio");
      120  
      121 -        Path input = Path.of(inputPath);                      
      121 +        Path input;                                           
      122 +        Path output;                                          
      123 +        Path politica;                                        
      124 +        Path cambio;                                          
      125 +        try {                                                 
      126 +            input = Path.of(inputPath);                       
      127 +            output = Path.of(outputPath);                     
      128 +            politica = Path.of(politicaPath);                 
      129 +            cambio = Path.of(cambioPath);                     
      130 +                                                              
      131 +            LeitorPolitica.ler(politica);                     
      132 +            LeitorCambio.ler(cambio);                         
      133 +        } catch (InvalidPathException e) {                    
      134 +            err.println("Caminho inválido: " + e.getMessage())
          +;                                                             
      135 +            return 2;                                         
      136 +        } catch (PoliticaInvalidaException e) {               
      137 +            err.println("Política inválida: " + e.getMessage()
          +);                                                            
      138 +            return 2;                                         
      139 +        } catch (CambioInvalidoException e) {                 
      140 +            err.println("Câmbio inválido: " + e.getMessage());
      141 +            return 2;                                         
      142 +        }                                                     
      143 +                                                              
      144          if (!Files.isRegularFile(input)) {
      145              err.println("Arquivo de entrada não encontrado: " 
           + inputPath);
      146              return 2;

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Removed 1 line
      177          BigDecimal total = SomadorTotal.somar(resultados);
      178          String json = EscritorResultado.serializar(envelope, r
           esultados, total);
      179  
      180 -        Path output = Path.of(outputPath);                    
      180          try {
      181              escreverAtomicamente(output, json);
      182          } catch (IOException e) {

● Now let's review the final Main.java to make sure it's correct.

  Read 1 file (ctrl+o to expand)

● This matches the required order exactly: Paths built together, política
  validated before câmbio, both before input existence check/envelope. Now let's
  write the tests. First check invalid política/câmbio fixture examples already
  used elsewhere for reuse patterns (e.g., helper methods in existing tests for
  writing invalid JSON).

  Searched for 1 pattern, read 1 file (ctrl+o to expand)

● Confirmed — this pattern is already proven in LeitorPoliticaTest. Now let me
  write the CliContratoTest additions.

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 291 lines
      385          assertFalse(Files.exists(output), "--output não deve s
           er criado quando há tokens extras após a raiz JSON");
      386      }
      387  
      388 +    // ---- Validação de --politica antes do envelope (T-035) 
          +---------------                                               
      389 +                                                              
      390 +    private static String envelopeValidoInline() {            
      391 +        return """                                            
      392 +                {                                             
      393 +                  "periodo": { "inicio": "2026-07-01", "fim": 
          +"2026-07-31" },                                               
      394 +                  "despesas": []                              
      395 +                }                                             
      396 +                """;                                          
      397 +    }                                                         
      398 +                                                              
      399 +    private static final String POLITICA_SEM_PADRAO = """     
      400 +            {                                                 
      401 +              "vigencia": "2026-07-01",                       
      402 +              "moeda_base": "BRL",                            
      403 +              "nota_fiscal_obrigatoria_acima_de": 100         
      404 +            }                                                 
      405 +            """;                                              
      406 +                                                              
      407 +    private static final String CAMBIO_SEM_TAXAS = """        
      408 +            {                                                 
      409 +              "moeda_base": "BRL"                             
      410 +            }                                                 
      411 +            """;                                              
      412 +                                                              
      413      @Test
      414 +    @DisplayName("--politica apontando para arquivo inexistent
          +e retorna exit 2, mesmo com --input válido")                  
      415 +    void politicaArquivoInexistente(@TempDir Path tempDir) thr
          +ows Exception {                                               
      416 +        Path input = tempDir.resolve("entrada.json");         
      417 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      418 +        Path output = tempDir.resolve("resultado.json");      
      419 +        Path politicaInexistente = tempDir.resolve("politica-n
          +ao-existe.json");                                             
      420 +                                                              
      421 +        Resultado resultado = executar(                       
      422 +                "calcular",                                   
      423 +                "--input", input.toString(),                  
      424 +                "--output", output.toString(),                
      425 +                "--politica", politicaInexistente.toString(), 
      426 +                "--cambio", CAMBIO);                          
      427 +                                                              
      428 +        assertEquals(2, resultado.codigo);                    
      429 +        assertEquals("", resultado.stdout);                   
      430 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      431 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --politica é inexistente");                  
      432 +    }                                                         
      433 +                                                              
      434 +    @Test                                                     
      435 +    @DisplayName("--politica apontando para diretório (caminho
          + ilegível) retorna exit 2")                                   
      436 +    void politicaCaminhoIlegivel(@TempDir Path tempDir) throws
          + Exception {                                                  
      437 +        Path input = tempDir.resolve("entrada.json");         
      438 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      439 +        Path output = tempDir.resolve("resultado.json");      
      440 +        Path diretorioComoPolitica = tempDir.resolve("diretori
          +o-politica");                                                 
      441 +        Files.createDirectory(diretorioComoPolitica);         
      442 +                                                              
      443 +        Resultado resultado = executar(                       
      444 +                "calcular",                                   
      445 +                "--input", input.toString(),                  
      446 +                "--output", output.toString(),                
      447 +                "--politica", diretorioComoPolitica.toString()
          +,                                                             
      448 +                "--cambio", CAMBIO);                          
      449 +                                                              
      450 +        assertEquals(2, resultado.codigo);                    
      451 +        assertEquals("", resultado.stdout);                   
      452 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      453 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --politica é ilegível");                     
      454 +    }                                                         
      455 +                                                              
      456 +    @Test                                                     
      457 +    @DisplayName("--politica com JSON sintaticamente inválido 
          +retorna exit 2")                                              
      458 +    void politicaJsonSintaticamenteInvalido(@TempDir Path temp
          +Dir) throws Exception {                                       
      459 +        Path input = tempDir.resolve("entrada.json");         
      460 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      461 +        Path output = tempDir.resolve("resultado.json");      
      462 +        Path politicaInvalida = tempDir.resolve("politica-inva
          +lida.json");                                                  
      463 +        Files.writeString(politicaInvalida, "{ \"vigencia\": "
          +, StandardCharsets.UTF_8);                                    
      464 +                                                              
      465 +        Resultado resultado = executar(                       
      466 +                "calcular",                                   
      467 +                "--input", input.toString(),                  
      468 +                "--output", output.toString(),                
      469 +                "--politica", politicaInvalida.toString(),    
      470 +                "--cambio", CAMBIO);                          
      471 +                                                              
      472 +        assertEquals(2, resultado.codigo);                    
      473 +        assertEquals("", resultado.stdout);                   
      474 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      475 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --politica é sintaticamente inválida");      
      476 +    }                                                         
      477 +                                                              
      478 +    @Test                                                     
      479 +    @DisplayName("--politica estruturalmente inválida retorna 
          +exit 2")                                                      
      480 +    void politicaEstruturalmenteInvalida(@TempDir Path tempDir
          +) throws Exception {                                          
      481 +        Path input = tempDir.resolve("entrada.json");         
      482 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      483 +        Path output = tempDir.resolve("resultado.json");      
      484 +        Path politicaInvalida = tempDir.resolve("politica-sem-
          +padrao.json");                                                
      485 +        Files.writeString(politicaInvalida, POLITICA_SEM_PADRA
          +O, StandardCharsets.UTF_8);                                   
      486 +                                                              
      487 +        Resultado resultado = executar(                       
      488 +                "calcular",                                   
      489 +                "--input", input.toString(),                  
      490 +                "--output", output.toString(),                
      491 +                "--politica", politicaInvalida.toString(),    
      492 +                "--cambio", CAMBIO);                          
      493 +                                                              
      494 +        assertEquals(2, resultado.codigo);                    
      495 +        assertEquals("", resultado.stdout);                   
      496 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      497 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --politica é estruturalmente inválida");     
      498 +    }                                                         
      499 +                                                              
      500 +    @Test                                                     
      501 +    @DisplayName("--politica com texto que causa InvalidPathEx
          +ception retorna exit 2")                                      
      502 +    void politicaCausaInvalidPathException(@TempDir Path tempD
          +ir) throws Exception {                                        
      503 +        Path input = tempDir.resolve("entrada.json");         
      504 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      505 +        Path output = tempDir.resolve("resultado.json");      
      506 +                                                              
      507 +        Resultado resultado = executar(                       
      508 +                "calcular",                                   
      509 +                "--input", input.toString(),                  
      510 +                "--output", output.toString(),                
      511 +                "--politica", "politicainvalida.json",        
      512 +                "--cambio", CAMBIO);                          
      513 +                                                              
      514 +        assertEquals(2, resultado.codigo);                    
      515 +        assertEquals("", resultado.stdout);                   
      516 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      517 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --politica causa InvalidPathException");     
      518 +    }                                                         
      519 +                                                              
      520 +    // ---- Validação de --cambio antes do envelope (T-035) --
          +----------------                                              
      521 +                                                              
      522 +    @Test                                                     
      523 +    @DisplayName("--cambio apontando para arquivo inexistente 
          +retorna exit 2, mesmo com --input válido")                    
      524 +    void cambioArquivoInexistente(@TempDir Path tempDir) throw
          +s Exception {                                                 
      525 +        Path input = tempDir.resolve("entrada.json");         
      526 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      527 +        Path output = tempDir.resolve("resultado.json");      
      528 +        Path cambioInexistente = tempDir.resolve("cambio-nao-e
          +xiste.json");                                                 
      529 +                                                              
      530 +        Resultado resultado = executar(                       
      531 +                "calcular",                                   
      532 +                "--input", input.toString(),                  
      533 +                "--output", output.toString(),                
      534 +                "--politica", POLITICA,                       
      535 +                "--cambio", cambioInexistente.toString());    
      536 +                                                              
      537 +        assertEquals(2, resultado.codigo);                    
      538 +        assertEquals("", resultado.stdout);                   
      539 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      540 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --cambio é inexistente");                    
      541 +    }                                                         
      542 +                                                              
      543 +    @Test                                                     
      544 +    @DisplayName("--cambio apontando para diretório (caminho i
          +legível) retorna exit 2")                                     
      545 +    void cambioCaminhoIlegivel(@TempDir Path tempDir) throws E
          +xception {                                                    
      546 +        Path input = tempDir.resolve("entrada.json");         
      547 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      548 +        Path output = tempDir.resolve("resultado.json");      
      549 +        Path diretorioComoCambio = tempDir.resolve("diretorio-
          +cambio");                                                     
      550 +        Files.createDirectory(diretorioComoCambio);           
      551 +                                                              
      552 +        Resultado resultado = executar(                       
      553 +                "calcular",                                   
      554 +                "--input", input.toString(),                  
      555 +                "--output", output.toString(),                
      556 +                "--politica", POLITICA,                       
      557 +                "--cambio", diretorioComoCambio.toString());  
      558 +                                                              
      559 +        assertEquals(2, resultado.codigo);                    
      560 +        assertEquals("", resultado.stdout);                   
      561 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      562 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --cambio é ilegível");                       
      563 +    }                                                         
      564 +                                                              
      565 +    @Test                                                     
      566 +    @DisplayName("--cambio com JSON sintaticamente inválido re
          +torna exit 2")                                                
      567 +    void cambioJsonSintaticamenteInvalido(@TempDir Path tempDi
          +r) throws Exception {                                         
      568 +        Path input = tempDir.resolve("entrada.json");         
      569 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      570 +        Path output = tempDir.resolve("resultado.json");      
      571 +        Path cambioInvalido = tempDir.resolve("cambio-invalido
          +.json");                                                      
      572 +        Files.writeString(cambioInvalido, "{ \"moeda_base\": "
          +, StandardCharsets.UTF_8);                                    
      573 +                                                              
      574 +        Resultado resultado = executar(                       
      575 +                "calcular",                                   
      576 +                "--input", input.toString(),                  
      577 +                "--output", output.toString(),                
      578 +                "--politica", POLITICA,                       
      579 +                "--cambio", cambioInvalido.toString());       
      580 +                                                              
      581 +        assertEquals(2, resultado.codigo);                    
      582 +        assertEquals("", resultado.stdout);                   
      583 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      584 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --cambio é sintaticamente inválido");        
      585 +    }                                                         
      586 +                                                              
      587 +    @Test                                                     
      588 +    @DisplayName("--cambio estruturalmente inválido retorna ex
          +it 2")                                                        
      589 +    void cambioEstruturalmenteInvalido(@TempDir Path tempDir) 
          +throws Exception {                                            
      590 +        Path input = tempDir.resolve("entrada.json");         
      591 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      592 +        Path output = tempDir.resolve("resultado.json");      
      593 +        Path cambioInvalido = tempDir.resolve("cambio-sem-taxa
          +s.json");                                                     
      594 +        Files.writeString(cambioInvalido, CAMBIO_SEM_TAXAS, St
          +andardCharsets.UTF_8);                                        
      595 +                                                              
      596 +        Resultado resultado = executar(                       
      597 +                "calcular",                                   
      598 +                "--input", input.toString(),                  
      599 +                "--output", output.toString(),                
      600 +                "--politica", POLITICA,                       
      601 +                "--cambio", cambioInvalido.toString());       
      602 +                                                              
      603 +        assertEquals(2, resultado.codigo);                    
      604 +        assertEquals("", resultado.stdout);                   
      605 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      606 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --cambio é estruturalmente inválido");       
      607 +    }                                                         
      608 +                                                              
      609 +    @Test                                                     
      610 +    @DisplayName("--cambio com texto que causa InvalidPathExce
          +ption retorna exit 2")                                        
      611 +    void cambioCausaInvalidPathException(@TempDir Path tempDir
          +) throws Exception {                                          
      612 +        Path input = tempDir.resolve("entrada.json");         
      613 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      614 +        Path output = tempDir.resolve("resultado.json");      
      615 +                                                              
      616 +        Resultado resultado = executar(                       
      617 +                "calcular",                                   
      618 +                "--input", input.toString(),                  
      619 +                "--output", output.toString(),                
      620 +                "--politica", POLITICA,                       
      621 +                "--cambio", "cambioinvalido.json");           
      622 +                                                              
      623 +        assertEquals(2, resultado.codigo);                    
      624 +        assertEquals("", resultado.stdout);                   
      625 +        assertFalse(resultado.stderr.isBlank(), "stderr deveri
          +a conter mensagem de erro");                                  
      626 +        assertFalse(Files.exists(output), "--output não deve s
          +er criado quando --cambio causa InvalidPathException");       
      627 +    }                                                         
      628 +                                                              
      629 +    // ---- Ordem de validação: política/câmbio antes do input
          + (T-035) -------                                              
      630 +                                                              
      631 +    @Test                                                     
      632 +    @DisplayName("política é validada antes do input: --input 
          +inexistente + --politica inválida prevalece a falha de polític
          +a")                                                           
      633 +    void ordemValidacao_politicaAntesDeInput(@TempDir Path tem
          +pDir) throws Exception {                                      
      634 +        Path inputInexistente = tempDir.resolve("nao-existe.js
          +on");                                                         
      635 +        Path output = tempDir.resolve("resultado.json");      
      636 +        Path politicaInvalida = tempDir.resolve("politica-sem-
          +padrao.json");                                                
      637 +        Files.writeString(politicaInvalida, POLITICA_SEM_PADRA
          +O, StandardCharsets.UTF_8);                                   
      638 +                                                              
      639 +        Resultado resultado = executar(                       
      640 +                "calcular",                                   
      641 +                "--input", inputInexistente.toString(),       
      642 +                "--output", output.toString(),                
      643 +                "--politica", politicaInvalida.toString(),    
      644 +                "--cambio", CAMBIO);                          
      645 +                                                              
      646 +        assertEquals(2, resultado.codigo);                    
      647 +        assertEquals("", resultado.stdout);                   
      648 +        assertFalse(resultado.stderr.contains("Arquivo de entr
          +ada não encontrado"),                                         
      649 +                "a falha deveria vir da política, e o input ai
          +nda não deveria ter sido consultado, mas stderr foi: "        
      650 +                        + resultado.stderr);                  
      651 +        assertFalse(Files.exists(output));                    
      652 +    }                                                         
      653 +                                                              
      654 +    @Test                                                     
      655 +    @DisplayName("política é validada antes do câmbio: ambos i
          +nválidos, prevalece a falha de política")                     
      656 +    void ordemValidacao_politicaAntesDeCambio(@TempDir Path te
          +mpDir) throws Exception {                                     
      657 +        Path input = tempDir.resolve("entrada.json");         
      658 +        Files.writeString(input, envelopeValidoInline(), Stand
          +ardCharsets.UTF_8);                                           
      659 +        Path output = tempDir.resolve("resultado.json");      
      660 +        Path politicaInvalida = tempDir.resolve("politica-sem-
          +padrao.json");                                                
      661 +        Files.writeString(politicaInvalida, POLITICA_SEM_PADRA
          +O, StandardCharsets.UTF_8);                                   
      662 +        Path cambioInvalido = tempDir.resolve("cambio-sem-taxa
          +s.json");                                                     
      663 +        Files.writeString(cambioInvalido, CAMBIO_SEM_TAXAS, St
          +andardCharsets.UTF_8);                                        
      664 +                                                              
      665 +        Resultado resultado = executar(                       
      666 +                "calcular",                                   
      667 +                "--input", input.toString(),                  
      668 +                "--output", output.toString(),                
      669 +                "--politica", politicaInvalida.toString(),    
      670 +                "--cambio", cambioInvalido.toString());       
      671 +                                                              
      672 +        assertEquals(2, resultado.codigo);                    
      673 +        assertEquals("", resultado.stdout);                   
      674 +        assertTrue(resultado.stderr.contains("Política inválid
          +a"),                                                          
      675 +                "a falha deveria vir da política antes do câmb
          +io, mas stderr foi: " + resultado.stderr);                    
      676 +        assertFalse(Files.exists(output));                    
      677 +    }                                                         
      678 +                                                              
      679 +    @Test                                                     
      680      @DisplayName("processamento com sucesso retorna exit 0, es
           creve o resultado e não escreve em stderr/stdout")
      681      void sucesso_exit0EArquivoEscrito(@TempDir Path tempDir) t
           hrows Exception {
      682          Path input = tempDir.resolve("entrada.json");

● I need to fix the InvalidPathException tests — I mistakenly used a space
  instead of the NUL character the task specifies as deterministic.

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Error editing file

  Searched for 3 patterns, ran 2 shell commands (ctrl+o to expand)

● Fixed the encoding. Let me verify the two InvalidPathException test bodies now
  contain the correct literal escape text.

  Searched for 1 pattern (ctrl+o to expand)
● Good, both now use the proper \u0000 escape. Now let's add the
  EscritaAtomicaSaidaTest additions.

● Update(tests\java\com\desafio\reembolso\EscritaAtomicaSaidaTest.java)
  ⎿  Added 88 lines
      172          assertNenhumTemporarioNoDiretorio(tempDir);
      173      }
      174  
      175 +    // ---- 6. Política inválida preserva output preexistente 
          +(T-035) -----------                                           
      176 +                                                              
      177 +    @Test                                                     
      178 +    @DisplayName("6 — política estruturalmente inválida preser
          +va um --output preexistente")                                 
      179 +    void politicaInvalida_preservaOutputPreexistente(@TempDir 
          +Path tempDir) throws IOException {                            
      180 +        Path input = tempDir.resolve("valido.json");          
      181 +        Files.writeString(input, jsonEnvelopeValido(), Standar
          +dCharsets.UTF_8);                                             
      182 +        Path output = tempDir.resolve("resultado.json");      
      183 +        escreverOutputPreexistente(output);                   
      184 +        Path politicaInvalida = tempDir.resolve("politica-sem-
          +padrao.json");                                                
      185 +        Files.writeString(politicaInvalida, """               
      186 +                {                                             
      187 +                  "vigencia": "2026-07-01",                   
      188 +                  "moeda_base": "BRL",                        
      189 +                  "nota_fiscal_obrigatoria_acima_de": 100     
      190 +                }                                             
      191 +                """, StandardCharsets.UTF_8);                 
      192 +                                                              
      193 +        Resultado resultado = executar(                       
      194 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      195 +                "--politica", politicaInvalida.toString(), "--
          +cambio", CAMBIO);                                             
      196 +                                                              
      197 +        assertEquals(2, resultado.codigo);                    
      198 +        assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(o
          +utput, StandardCharsets.UTF_8));                              
      199 +        assertNenhumTemporarioNoDiretorio(tempDir);           
      200 +    }                                                         
      201 +                                                              
      202 +    // ---- 7. Câmbio inválido preserva output preexistente (T
          +-035) -------------                                           
      203 +                                                              
      204 +    @Test                                                     
      205 +    @DisplayName("7 — câmbio estruturalmente inválido preserva
          + um --output preexistente")                                   
      206 +    void cambioInvalido_preservaOutputPreexistente(@TempDir Pa
          +th tempDir) throws IOException {                              
      207 +        Path input = tempDir.resolve("valido.json");          
      208 +        Files.writeString(input, jsonEnvelopeValido(), Standar
          +dCharsets.UTF_8);                                             
      209 +        Path output = tempDir.resolve("resultado.json");      
      210 +        escreverOutputPreexistente(output);                   
      211 +        Path cambioInvalido = tempDir.resolve("cambio-sem-taxa
          +s.json");                                                     
      212 +        Files.writeString(cambioInvalido, """                 
      213 +                {                                             
      214 +                  "moeda_base": "BRL"                         
      215 +                }                                             
      216 +                """, StandardCharsets.UTF_8);                 
      217 +                                                              
      218 +        Resultado resultado = executar(                       
      219 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      220 +                "--politica", POLITICA, "--cambio", cambioInva
          +lido.toString());                                             
      221 +                                                              
      222 +        assertEquals(2, resultado.codigo);                    
      223 +        assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(o
          +utput, StandardCharsets.UTF_8));                              
      224 +        assertNenhumTemporarioNoDiretorio(tempDir);           
      225 +    }                                                         
      226 +                                                              
      227 +    // ---- 8. InvalidPathException em --politica/--cambio pre
          +serva output (T-035)                                          
      228 +                                                              
      229 +    @Test                                                     
      230 +    @DisplayName("8 — InvalidPathException em --politica prese
          +rva um --output preexistente")                                
      231 +    void politicaInvalidPathException_preservaOutputPreexisten
          +te(@TempDir Path tempDir) throws IOException {                
      232 +        Path input = tempDir.resolve("valido.json");          
      233 +        Files.writeString(input, jsonEnvelopeValido(), Standar
          +dCharsets.UTF_8);                                             
      234 +        Path output = tempDir.resolve("resultado.json");      
      235 +        escreverOutputPreexistente(output);                   
      236 +                                                              
      237 +        Resultado resultado = executar(                       
      238 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      239 +                "--politica", "politicainvalida.json", "--camb
          +io", CAMBIO);                                                 
      240 +                                                              
      241 +        assertEquals(2, resultado.codigo);                    
      242 +        assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(o
          +utput, StandardCharsets.UTF_8));                              
      243 +        assertNenhumTemporarioNoDiretorio(tempDir);           
      244 +    }                                                         
      245 +                                                              
      246 +    @Test                                                     
      247 +    @DisplayName("9 — InvalidPathException em --cambio preserv
          +a um --output preexistente")                                  
      248 +    void cambioInvalidPathException_preservaOutputPreexistente
          +(@TempDir Path tempDir) throws IOException {                  
      249 +        Path input = tempDir.resolve("valido.json");          
      250 +        Files.writeString(input, jsonEnvelopeValido(), Standar
          +dCharsets.UTF_8);                                             
      251 +        Path output = tempDir.resolve("resultado.json");      
      252 +        escreverOutputPreexistente(output);                   
      253 +                                                              
      254 +        Resultado resultado = executar(                       
      255 +                "calcular", "--input", input.toString(), "--ou
          +tput", output.toString(),                                     
      256 +                "--politica", POLITICA, "--cambio", "cambioinv
          +alido.json");                                                 
      257 +                                                              
      258 +        assertEquals(2, resultado.codigo);                    
      259 +        assertEquals(CONTEUDO_PREEXISTENTE, Files.readString(o
          +utput, StandardCharsets.UTF_8));                              
      260 +        assertNenhumTemporarioNoDiretorio(tempDir);           
      261 +    }                                                         
      262 +                                                              
      263      // ---- 5. Nenhum temporário permanece após sucesso sem ou
           tput preexistente -
      264  
      265      @Test

● I need to use the NUL character for these InvalidPathException tests too,
  matching the CliContratoTest pattern. Let me fix them with the proper escaped
  text (double backslash so JSON decodes to a literal \u0000).

● Update(tests\java\com\desafio\reembolso\EscritaAtomicaSaidaTest.java)
  ⎿  Error editing file

  Searched for 2 patterns, ran 1 shell command (ctrl+o to expand)

● Now let's verify the fixed lines directly.

  Searched for 1 pattern (ctrl+o to expand)

● Both files are fixed now. Let's run the two Maven verification commands.

  Ran 1 shell command (ctrl+o to expand)

● Targeted tests passed. Now let's run the full suite.

  Ran 1 shell command (ctrl+o to expand)

● Full suite passed with no output (quiet mode suppresses success details). Let
  me get the actual test counts from the surefire reports.

  Ran 1 shell command (ctrl+o to expand)

● Ambos os comandos Maven passaram (exit 0, sem falhas).

  - mvn -q test "-Dtest=CliContratoTest,EscritaAtomicaSaidaTest": passou
  - mvn -q test (suíte completa): passou — 508 testes, 0 falhas, 0 erros, 0
  pulados
  - CliContratoTest: 30 testes
  - EscritaAtomicaSaidaTest: 9 testes

✻ Baked for 4m 37s

※ recap: Implemented T-035 (política/câmbio validated before envelope in Main.java) and extended the CLI test suites; full Maven suite passes with 508 tests, 30 in CliContratoTest, 9 in EscritaAtomicaSaidaTest. Next: awaiting your review before touching T-036. (disable recaps in /config)

> Antes de fechar a T-035, corrija exclusivamente a cobertura de preservação do 
  output em:                                                                    
                                                                                
  tests/java/com/desafio/reembolso/CliContratoTest.java                         
                                                                                
  Não altere Main.java nem EscritaAtomicaSaidaTest.java.                        
                                                                                
  Os testes de T-035 para política e câmbio atualmente usam um caminho de       
  output inexistente e verificam apenas:                                        
                                                                                
  assertFalse(Files.exists(output))                                             
                                                                                
  Isso não comprova o requisito de que um --output preexistente permanece       
  intacto byte a byte.                                                          
                                                                                
  Para cada um destes cenários:                                                 
                                                                                
  Política:                                                                     
  - arquivo inexistente;                                                        
  - diretório no lugar do arquivo;                                              
  - JSON sintaticamente inválido;                                               
  - política estruturalmente inválida;                                          
  - InvalidPathException.                                                       
                                                                                
  Câmbio:                                                                       
  - arquivo inexistente;                                                        
  - diretório no lugar do arquivo;                                              
  - JSON sintaticamente inválido;                                               
  - câmbio estruturalmente inválido;                                            
  - InvalidPathException.                                                       
                                                                                
  Faça o teste:                                                                 
                                                                                
  1. criar previamente o arquivo de output com um conteúdo sentinela fixo;      
  2. executar Main.run(...);                                                    
  3. confirmar exit 2;                                                          
  4. confirmar stdout vazio;                                                    
  5. confirmar stderr não vazio;                                                
  6. confirmar que o conteúdo do output continua exatamente igual ao sentinela. 
                                                                                
  Crie uma constante e um helper privados para evitar repetição, por exemplo:   
                                                                                
  private static final String CONTEUDO_OUTPUT_PREEXISTENTE = "...";             
                                                                                
  e um helper que escreva ou valide o conteúdo, mantendo os testes legíveis.    
                                                                                
  Substitua as verificações de inexistência do output nesses dez testes pela    
  verificação byte a byte do conteúdo preexistente.                             
                                                                                
  Não adicione testes duplicados: ajuste os dez testes existentes.              
  Não altere os testes históricos anteriores à seção T-035.                     
  Não altere tasks.md.                                                          
  Não execute /export, git add, commit ou push.                                 
  Não execute git diff ou git status.                                           
                                                                                
  Execute:                                                                      
                                                                                
  mvn -q test "-Dtest=CliContratoTest,EscritaAtomicaSaidaTest"                  
  mvn -q test                                                                   
                                                                                
  Ao final, informe somente:                                                    
                                                                                
  - se os dois comandos passaram;                                               
  - total de testes da suíte completa;                                          
  - quantidade de testes em CliContratoTest;                                    
  - quantidade de testes em EscritaAtomicaSaidaTest.                            
                                                                                
  Não imprima o arquivo completo no terminal.                                   

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
       7  import org.junit.jupiter.api.io.TempDir;
       8  
       9  import java.io.ByteArrayOutputStream;
      10 +import java.io.IOException;                                    
      11  import java.io.PrintStream;
      12  import java.math.BigDecimal;
      13  import java.nio.charset.StandardCharsets;

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Error editing file

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Error editing file

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 11 lines
      411              }
      412              """;
      413  
      414 +    private static final String CONTEUDO_OUTPUT_PREEXISTENTE =
          + "{\"resultado\":\"preexistente\"}";                          
      415 +                                                              
      416 +    private static void escreverOutputPreexistente(Path output
          +) throws IOException {                                        
      417 +        Files.writeString(output, CONTEUDO_OUTPUT_PREEXISTENTE
          +, StandardCharsets.UTF_8);                                    
      418 +    }                                                         
      419 +                                                              
      420 +    private static void assertOutputPreexistentePreservado(Pat
          +h output) throws IOException {                                
      421 +        assertEquals(CONTEUDO_OUTPUT_PREEXISTENTE, Files.readS
          +tring(output, StandardCharsets.UTF_8),                        
      422 +                "--output preexistente deveria permanecer inta
          +cto byte a byte");                                            
      423 +    }                                                         
      424 +                                                              
      425      @Test
      426      @DisplayName("--politica apontando para arquivo inexistent
           e retorna exit 2, mesmo com --input válido")
      427      void politicaArquivoInexistente(@TempDir Path tempDir) thr
           ows Exception {
● Good, smaller edits work. Now updating each of the ten tests individually.

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      428          Path input = tempDir.resolve("entrada.json");
      429          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      430          Path output = tempDir.resolve("resultado.json");
      431 +        escreverOutputPreexistente(output);                   
      432          Path politicaInexistente = tempDir.resolve("politica-n
           ao-existe.json");
      433  
      434          Resultado resultado = executar(

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line, removed 1 line
      441          assertEquals(2, resultado.codigo);
      442          assertEquals("", resultado.stdout);
      443          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      444 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --politica é inexistente");                  
      444 +        assertOutputPreexistentePreservado(output);           
      445      }
      446  
      447      @Test

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      450          Path input = tempDir.resolve("entrada.json");
      451          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      452          Path output = tempDir.resolve("resultado.json");
      453 +        escreverOutputPreexistente(output);                   
      454          Path diretorioComoPolitica = tempDir.resolve("diretori
           o-politica");
      455          Files.createDirectory(diretorioComoPolitica);
      456  

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line, removed 1 line
      464          assertEquals(2, resultado.codigo);
      465          assertEquals("", resultado.stdout);
      466          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      467 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --politica é ilegível");                     
      467 +        assertOutputPreexistentePreservado(output);           
      468      }
      469  
      470      @Test

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      473          Path input = tempDir.resolve("entrada.json");
      474          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      475          Path output = tempDir.resolve("resultado.json");
      476 +        escreverOutputPreexistente(output);                   
      477          Path politicaInvalida = tempDir.resolve("politica-inva
           lida.json");
      478          Files.writeString(politicaInvalida, "{ \"vigencia\": "
           , StandardCharsets.UTF_8);
      479  

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line, removed 1 line
      487          assertEquals(2, resultado.codigo);
      488          assertEquals("", resultado.stdout);
      489          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      490 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --politica é sintaticamente inválida");      
      490 +        assertOutputPreexistentePreservado(output);           
      491      }
      492  
      493      @Test

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 2 lines, removed 1 line
      496          Path input = tempDir.resolve("entrada.json");
      497          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      498          Path output = tempDir.resolve("resultado.json");
      499 +        escreverOutputPreexistente(output);                   
      500          Path politicaInvalida = tempDir.resolve("politica-sem-
           padrao.json");
      501          Files.writeString(politicaInvalida, POLITICA_SEM_PADRA
           O, StandardCharsets.UTF_8);
      502  
     ...
      510          assertEquals(2, resultado.codigo);
      511          assertEquals("", resultado.stdout);
      512          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      512 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --politica é estruturalmente inválida");     
      513 +        assertOutputPreexistentePreservado(output);           
      514      }
      515  
      516      @Test

● Now the política InvalidPathException test:

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Error editing file

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      519          Path input = tempDir.resolve("entrada.json");
      520          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      521          Path output = tempDir.resolve("resultado.json");
      522 +        escreverOutputPreexistente(output);                   
      523  
      524          Resultado resultado = executar(
      525                  "calcular",

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line, removed 1 line
      531          assertEquals(2, resultado.codigo);
      532          assertEquals("", resultado.stdout);
      533          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      534 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --politica causa InvalidPathException");     
      534 +        assertOutputPreexistentePreservado(output);           
      535      }
      536  
      537      // ---- Validação de --cambio antes do envelope (T-035) --
           ----------------

● Now the câmbio section — the same five patterns:

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      542          Path input = tempDir.resolve("entrada.json");
      543          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      544          Path output = tempDir.resolve("resultado.json");
      545 +        escreverOutputPreexistente(output);                   
      546          Path cambioInexistente = tempDir.resolve("cambio-nao-e
           xiste.json");
      547  
      548          Resultado resultado = executar(

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line, removed 1 line
      555          assertEquals(2, resultado.codigo);
      556          assertEquals("", resultado.stdout);
      557          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      558 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --cambio é inexistente");                    
      558 +        assertOutputPreexistentePreservado(output);           
      559      }
      560  
      561      @Test

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      564          Path input = tempDir.resolve("entrada.json");
      565          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      566          Path output = tempDir.resolve("resultado.json");
      567 +        escreverOutputPreexistente(output);                   
      568          Path diretorioComoCambio = tempDir.resolve("diretorio-
           cambio");
      569          Files.createDirectory(diretorioComoCambio);
      570  

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line, removed 1 line
      578          assertEquals(2, resultado.codigo);
      579          assertEquals("", resultado.stdout);
      580          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      581 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --cambio é ilegível");                       
      581 +        assertOutputPreexistentePreservado(output);           
      582      }
      583  
      584      @Test

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      587          Path input = tempDir.resolve("entrada.json");
      588          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      589          Path output = tempDir.resolve("resultado.json");
      590 +        escreverOutputPreexistente(output);                   
      591          Path cambioInvalido = tempDir.resolve("cambio-invalido
           .json");
      592          Files.writeString(cambioInvalido, "{ \"moeda_base\": "
           , StandardCharsets.UTF_8);
      593  

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line, removed 1 line
      601          assertEquals(2, resultado.codigo);
      602          assertEquals("", resultado.stdout);
      603          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      604 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --cambio é sintaticamente inválido");        
      604 +        assertOutputPreexistentePreservado(output);           
      605      }
      606  
      607      @Test

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      610          Path input = tempDir.resolve("entrada.json");
      611          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      612          Path output = tempDir.resolve("resultado.json");
      613 +        escreverOutputPreexistente(output);                   
      614          Path cambioInvalido = tempDir.resolve("cambio-sem-taxa
           s.json");
      615          Files.writeString(cambioInvalido, CAMBIO_SEM_TAXAS, St
           andardCharsets.UTF_8);
      616  

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)  ⎿  Added 1 line, removed 1 line
      624          assertEquals(2, resultado.codigo);
      625          assertEquals("", resultado.stdout);
      626          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      627 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --cambio é estruturalmente inválido");       
      627 +        assertOutputPreexistentePreservado(output);           
      628      }
      629  
      630      @Test

● Now the last one, câmbio InvalidPathException test:

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line
      633          Path input = tempDir.resolve("entrada.json");
      634          Files.writeString(input, envelopeValidoInline(), Stand
           ardCharsets.UTF_8);
      635          Path output = tempDir.resolve("resultado.json");
      636 +        escreverOutputPreexistente(output);                   
      637  
      638          Resultado resultado = executar(
      639                  "calcular",

● Update(tests\java\com\desafio\reembolso\CliContratoTest.java)
  ⎿  Added 1 line, removed 1 line
      645          assertEquals(2, resultado.codigo);
      646          assertEquals("", resultado.stdout);
      647          assertFalse(resultado.stderr.isBlank(), "stderr deveri
           a conter mensagem de erro");
      648 -        assertFalse(Files.exists(output), "--output não deve s
          -er criado quando --cambio causa InvalidPathException");       
      648 +        assertOutputPreexistentePreservado(output);           
      649      }
      650  
      651      // ---- Ordem de validação: política/câmbio antes do input
            (T-035) -------

● Let's verify no raw NUL bytes got reintroduced and review the whole modified
  section for correctness.

  Searched for 1 pattern, ran 1 shell command (ctrl+o to expand)

● Good — no raw NUL bytes, and both InvalidPathException tests correctly use the
  literal \u0000 escape. Now let's read through the full modified section to
  sanity-check everything before running Maven.

  Read 1 file (ctrl+o to expand)

● All ten tests are correctly updated. Now let's run the verification commands.

  Ran 1 shell command (ctrl+o to expand)

● Targeted tests passed. Now full suite.

  Ran 1 shell command (ctrl+o to expand)

● Full suite passed too. Let's get the exact test counts.

  Ran 1 shell command (ctrl+o to expand)

● Ambos os comandos Maven passaram.

  - mvn -q test "-Dtest=CliContratoTest,EscritaAtomicaSaidaTest": passou
  - mvn -q test (suíte completa): passou — 508 testes, 0 falhas, 0 erros, 0
  pulados
  - CliContratoTest: 30 testes
  - EscritaAtomicaSaidaTest: 9 testes

✻ Cooked for 4m 33s

※ recap: You're implementing T-035 (CLI validates política/câmbio before the envelope). Both Maven test commands just passed: 508 total tests, 30 in CliContratoTest, 9 in EscritaAtomicaSaidaTest. Next: confirm with the user whether T-035 is ready to close. (disable recaps in /config)

> A T-035 foi revisada e aprovada.                                              
                                                                                
  Em specs/001-motor-reembolso/tasks.md, altere somente a T-035:                
                                                                                
  - checkbox do título de [ ] para [x];                                         
  - Status de [ ] pendente para [x] concluída.                                  
                                                                                
  Não altere nenhuma outra task ou arquivo.                                     
  Não execute git add, commit ou push.                                          

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      839    - **Commit sugerido:** `feat(T-034): reescreve parser da CLI
            para quatro flags obrigatorias e migra consumidores historico
           s de Main.run`
      840    - **Status:** [x] concluída
      841  
      842 -- [ ] **T-035** — `Main.java`: carregar e validar política e c
          -âmbio antes do envelope                                       
      842 +- [x] **T-035** — `Main.java`: carregar e validar política e c
          +âmbio antes do envelope                                       
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
      844    - **RN atendidas:** RN-021, RN-022.
      845    - **CA atendidos:** CA-043, CA-044.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      859      mvn -q test "-Dtest=CliContratoTest,EscritaAtomicaSaidaTes
           t"
      860      ```
      861    - **Commit sugerido:** `feat(T-035): carrega politica e camb
           io antes do envelope na CLI`
      862 -  - **Status:** [ ] pendente                                  
      862 +  - **Status:** [x] concluída                                 
      863  
      864  ---
      865  

● T-035 marcada como concluída em tasks.md (checkbox do título e Status). Nenhum
  outro arquivo alterado.

✻ Brewed for 9s