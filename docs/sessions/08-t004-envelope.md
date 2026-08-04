 ▐▛███▜▌   Claude Code v2.1.221
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


❯ Vamos executar exclusivamente a T-004 — Validação do envelope (RN-001) e      
  metadados opcionais.                                                          
                                                                                
  A T-003 está concluída, commitada e enviada ao repositório. Não revise nem    
  altere novamente tasks anteriores.                                            
                                                                                
  Antes de implementar, leia:                                                   
                                                                                
  * `CLAUDE.md`;                                                                
  * a seção 4.1, a RN-001 e o CA-020 de `specs/001-motor-reembolso/spec.md`;    
  * as partes relacionadas ao schema de saída necessárias para representar os   
  metadados tolerados;                                                          
  * DT-003, DT-004, DT-005 e as seções 3 e 4.1 de                               
  `specs/001-motor-reembolso/plan.md`;                                          
  * o trecho completo da T-004 em `specs/001-motor-reembolso/tasks.md`.         
                                                                                
  Confirme somente:                                                             
                                                                                
  * T-003 concluída;                                                            
  * T-004 pendente;                                                             
  * working tree limpo.                                                         
                                                                                
  Depois implemente somente a T-004.                                            
                                                                                
  ## Arquivos previstos                                                         
                                                                                
  Criar:                                                                        
                                                                                
  * `src/main/java/com/desafio/reembolso/leitor/ValidadorEnvelope.java`;        
  * `src/main/java/com/desafio/reembolso/modelo/Envelope.java`;                 
  * `tests/java/com/desafio/reembolso/leitor/EnvelopeValidoTest.java`.          
                                                                                
  Use os pacotes e caminhos definidos pelo projeto.                             
                                                                                
  Se a arquitetura descrita no plan exigir alteração em qualquer arquivo        
  adicional, não faça essa alteração automaticamente: pare e explique qual      
  arquivo seria necessário e por quê.                                           
                                                                                
  ## Escopo                                                                     
                                                                                
  1. Ler o envelope por árvore usando `JsonNode`.                               
  2. Configurar o `ObjectMapper` com `USE_BIG_DECIMAL_FOR_FLOATS`.              
  3. Implementar exatamente as regras do envelope definidas na spec 4.1.        
  4. Envelope inválido deve ser representado como erro de processamento         
  correspondente ao exit code `3`.                                              
  5. Não tratar erro de envelope como exit code `2`.                            
  6. Metadados opcionais devem seguir integralmente a tabela de tolerância da   
  spec.                                                                         
  7. `colaborador` recebido como texto deve manter o arquivo processável e      
  resultar nos três metadados correspondentes como nulos.                       
  8. `despesas: []` deve ser aceito e representar resultado vazio com total     
  `0,00`, no nível de modelo/teste previsto nesta task.                         
  9. `periodo.inicio` posterior a `periodo.fim` deve invalidar o envelope.      
                                                                                
  ## Testes obrigatórios                                                        
                                                                                
  Em `EnvelopeValidoTest`, cobrir no mínimo:                                    
                                                                                
  1. `periodo.inicio` posterior a `periodo.fim`:                                
                                                                                
     * erro de envelope;                                                        
     * código correspondente a exit `3`;                                        
     * nenhum output criado ou modificado.                                      
                                                                                
  2. `despesas: []`:                                                            
                                                                                
     * envelope aceito;                                                         
     * coleção de despesas vazia;                                               
     * resultado vazio;                                                         
     * total `0,00`.                                                            
                                                                                
  3. `colaborador` recebido como texto:                                         
                                                                                
     * arquivo aceito;                                                          
     * três metadados de colaborador nulos;                                     
     * nenhuma coerção ou inferência do texto recebido.                         
                                                                                
  Os testes devem usar arquivos temporários e não depender de caminhos          
  absolutos.                                                                    
                                                                                
  ## Não fazer                                                                  
                                                                                
  * não validar a estrutura interna dos itens de `despesas`;                    
  * não implementar RN-002;                                                     
  * não criar `ValidadorItem` ou `ItemValidado`;                                
  * não implementar regras financeiras;                                         
  * não escrever o JSON final;                                                  
  * não implementar escrita atômica;                                            
  * não antecipar T-005 ou posteriores;                                         
  * não alterar os enums da T-003;                                              
  * não alterar `Main.java`, `CliContratoTest.java` ou `pom.xml` sem parar e    
  justificar;                                                                   
  * não alterar spec.md, plan.md ou DECISIONS.md;                               
  * não marcar T-004 como concluída;                                            
  * não executar `/export`;                                                     
  * não executar git add, commit ou push.                                       
                                                                                
  ## Verificação                                                                
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  mvn test -Dtest=EnvelopeValidoTest                                            
  mvn test                                                                      
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  ```                                                                           
                                                                                
  Ao final, informe:                                                            
                                                                                
  1. arquivos criados;                                                          
  2. estrutura de `Envelope`;                                                   
  3. regras de envelope implementadas;                                          
  4. comportamento dos metadados opcionais;                                     
  5. testes executados e resultados;                                            
  6. confirmação de que itens de despesa e regras posteriores não foram         
  implementados;                                                                
  7. qualquer divergência encontrada entre spec, plan e tasks.                  
                                                                                
  Não faça commit. Pare para revisão.                                           

● I'll start by confirming the prerequisites and reading the required
  spec/plan/tasks sections.

  Searched for 1 pattern, read 1 file, ran 1 shell command (ctrl+o to expand)

● T-003 concluída, T-004 pendente, working tree limpo confirmados. Agora vou ler
  as seções relevantes da spec e do plan.

  Searched for 1 pattern, read 2 files (ctrl+o to expand)

● Now let me look at existing source files to understand conventions before
  implementing.

  Searched for 2 patterns, read 7 files, ran 1 shell command (ctrl+o to expand)

● Confirmed the import path. Now creating the files.

● Write(src\main\java\com\desafio\reembolso\modelo\Envelope.java)
  ⎿  Wrote 66 lines to
     src\main\java\com\desafio\reembolso\modelo\Envelope.java
       1 package com.desafio.reembolso.modelo;
       2 
       3 import com.fasterxml.jackson.databind.JsonNode;
       5 import java.time.LocalDate;
       6 import java.util.Objects;
       7 
       8 /**
         cionais de
      10  * {@code colaborador} e {@code periodo}, e a lista bruta de {@c
         ode despesas}
     … +61 lines (ctrl+o to expand)

● Write(src\main\java\com\desafio\reembolso\leitor\ValidadorEnvelope.java)
  ⎿  Wrote 111 lines to
     src\main\java\com\desafio\reembolso\leitor\ValidadorEnvelope.java
       1 package com.desafio.reembolso.leitor;
       2 
       3 import com.desafio.reembolso.modelo.Envelope;
       4 import com.fasterxml.jackson.databind.JsonNode;
       5 import com.fasterxml.jackson.databind.node.JsonNodeType;
       6 
       7 import java.time.LocalDate;
       8 import java.time.format.DateTimeFormatter;
       9 import java.time.format.DateTimeParseException;
      10 import java.time.format.ResolverStyle;
     … +134 lines (ctrl+o to expand)

● Now the test file:

● Write(tests\java\com\desafio\reembolso\leitor\EnvelopeValidoTest.java)
  ⎿  Wrote 120 lines to
     tests\java\com\desafio\reembolso\leitor\EnvelopeValidoTest.java
      1 package com.desafio.reembolso.leitor;
      2 
      3 import com.desafio.reembolso.modelo.Envelope;
      4 import com.fasterxml.jackson.databind.DeserializationFeature;
      5 import com.fasterxml.jackson.databind.JsonNode;
      6 import com.fasterxml.jackson.databind.ObjectMapper;
      7 import org.junit.jupiter.api.DisplayName;
      8 import org.junit.jupiter.api.Test;
      9 import org.junit.jupiter.api.io.TempDir;
     … +143 lines (ctrl+o to expand)

● Now let's run the tests.

  Ran 1 shell command (ctrl+o to expand)

● No output with -q typically means success. Let's run without quiet to confirm
  and then the full suite.

  Ran 1 shell command (ctrl+o to expand)

● All 3 tests pass. Now the full suite.

  Ran 1 shell command (ctrl+o to expand)

● Full suite green (55/55). Now the remaining verification commands.

  Ran 1 shell command (ctrl+o to expand)

● Verificação completa. Only new untracked files were created — no tracked file
  (Main.java, CliContratoTest.java, pom.xml) was touched, and git diff 
  --check/--stat are clean (no modifications to diff against).

  Resumo

  1. Arquivos criados
  - src/main/java/com/desafio/reembolso/leitor/ValidadorEnvelope.java
  - src/main/java/com/desafio/reembolso/modelo/Envelope.java
  - tests/java/com/desafio/reembolso/leitor/EnvelopeValidoTest.java

  2. Estrutura de Envelope
  Imutável, com colaboradorId/colaboradorNome/colaboradorCentroCusto (texto ou
  nulo), periodoCompetencia (texto ou nulo), periodoInicio/periodoFim
  (LocalDate, obrigatórios) e despesas (o JsonNode bruto da lista, não validado
  item a item — isso é T-005).

  3. Regras de envelope implementadas (RN-001/4.1)
  Raiz precisa ser objeto; periodo precisa existir e ser objeto;
  periodo.inicio/periodo.fim precisam ser texto no formato AAAA-MM-DD
  representando data real do calendário (validado com DateTimeFormatter +
  ResolverStyle.STRICT, então 2026-02-30 é rejeitado); periodo.inicio não pode
  ser posterior a periodo.fim; despesas precisa existir, não ser nulo e ser
  lista. Qualquer violação lança ValidadorEnvelope.EnvelopeInvalidoException,
  cujo codigoSaida() retorna 3 (distinto do exit 2 de infraestrutura) — a
  exceção só representa o erro no domínio; o mapeamento para o exit code real da
  JVM fica para o CLI (T-019, fora do escopo desta task).

  4. Comportamento dos metadados opcionais
  Segue a tabela de tolerância 4.1 à risca: colaborador ausente, nulo ou
  não-objeto → os três metadados nulos, sem exceção; dentro de colaborador
  objeto, cada campo é preservado apenas quando é texto (senão nulo), e campos
  desconhecidos são ignorados por não serem lidos. periodo.competencia é
  preservado só quando texto no formato AAAA-MM, senão nulo, sem qualquer efeito
  de validação.

  5. Testes executados
  mvn test -Dtest=EnvelopeValidoTest  → 3/3 passaram
  mvn test (suíte completa)            → 55/55 passaram (5 CliContratoTest + 3
  EnvelopeValidoTest + 47 VocabularioMotivoTest)
  Os três cenários obrigatórios: período invertido (exceção com
  codigoSaida()==3, --output preexistente inalterado e nenhum novo --output
  criado), despesas: [] (aceito, coleção vazia — com comentário no teste
  explicando que "resultado vazio"/"total 0,00" são consequência trivial,
  formalmente calculados em T-017/T-020), e colaborador como texto (arquivo
  processável, três metadados nulos, sem coerção).

  6. Confirmação de escopo
  Nenhuma validação de item de despesas foi implementada (nenhum
  ValidadorItem/ItemValidado), nenhuma regra financeira (RN-003 em diante),
  nenhuma escrita de JSON de saída, nenhuma escrita atômica.

  7. Divergência encontrada
  DT-004/DT-005 atribuem a T-004 a "configuração do ObjectMapper com
  USE_BIG_DECIMAL_FOR_FLOATS" — mas o único lugar hoje onde texto JSON vira
  JsonNode para consumo do ValidadorEnvelope é dentro de EnvelopeValidoTest (o
  ObjectMapper de produção mora em Main.java, que é explicitamente fora de
  escopo desta sessão). Configurei o mapper com essa flag apenas no teste.
  Parando para avisar: o ObjectMapper de Main.java ainda não tem
  USE_BIG_DECIMAL_FOR_FLOATS habilitado nem chama ValidadorEnvelope — isso é
  necessário para fechar DT-004/DT-005 na CLI de fato, mas alterar Main.java
  está fora do que esta task autoriza. Fica como ponto explícito para uma
  próxima task (provavelmente T-019, onde a CLI é fechada com os três exit codes
  juntos) ou para uma decisão sua sobre quando essa integração deve ocorrer.

  Nada foi commitado — aguardando sua revisão.

✻ Baked for 5m 24s

※ recap: Implementing T-004 (envelope validation, RN-001) for the reembolso engine — done and tests pass, but not committed yet. Next: you review the changes and confirm before I commit. (disable recaps in /config)

❯ Continue exclusivamente a T-004.                                              
                                                                                
  Não altere Main.java, CliContratoTest.java, pom.xml, documentos normativos ou 
  tasks posteriores.                                                            
  Não execute export, git add, commit ou push.                                  
                                                                                
  A revisão encontrou uma lacuna: o `ObjectMapper` com                          
  `USE_BIG_DECIMAL_FOR_FLOATS` existe somente em `EnvelopeValidoTest`. A DT-004 
  exige que essa configuração exista no código de produção.                     
                                                                                
  ## Correção                                                                   
                                                                                
  Em `ValidadorEnvelope.java`:                                                  
                                                                                
  1. Crie um `ObjectMapper` privado e estático configurado com:                 
                                                                                
     * `DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS`;                     
     * `DeserializationFeature.FAIL_ON_TRAILING_TOKENS`, preservando o contrato 
  sintático da T-002.                                                           
                                                                                
  2. Crie um método público de produção, com nome claro como:                   
                                                                                
  ```java                                                                       
  public static Envelope lerEValidar(Path arquivo) throws IOException           
  ```                                                                           
                                                                                
  Esse método deve:                                                             
                                                                                
  * ler o arquivo com o `ObjectMapper` configurado;                             
  * obter a árvore `JsonNode`;                                                  
  * chamar a validação de envelope já existente;                                
  * não escrever nem criar arquivos de saída.                                   
                                                                                
  3. Preserve `validar(JsonNode)` para a validação isolada do envelope.         
                                                                                
  Em `EnvelopeValidoTest.java`:                                                 
                                                                                
  1. Remova o `ObjectMapper` próprio do teste e o método auxiliar de leitura.   
  2. Faça os três testes existentes usarem                                      
  `ValidadorEnvelope.lerEValidar(input)`.                                       
  3. Adicione um quarto teste que comprove a DT-004:                            
                                                                                
     * use um envelope válido com um item bruto contendo `"valor": 33.333`;     
     * confirme apenas que o `JsonNode` preservado em `despesas` representa     
  esse valor como decimal baseado em `BigDecimal`;                              
     * não valide a estrutura do item e não implemente RN-002.                  
                                                                                
  A exceção de envelope continua representando o código `3`. O mapeamento para  
  o exit code real da JVM permanece para a integração da CLI prevista           
  posteriormente.                                                               
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  mvn test -Dtest=EnvelopeValidoTest                                            
  mvn test                                                                      
  git diff --check                                                              
  git status --short                                                            
  ```                                                                           
                                                                                
  Resultado esperado:                                                           
                                                                                
  * 4 testes em `EnvelopeValidoTest`;                                           
  * 56 testes na suíte completa;                                                
  * somente os três arquivos originais da T-004 presentes no working tree;      
  * nenhum arquivo de task anterior ou posterior alterado.                      
                                                                                
  Não faça commit.                                                              
  Pare para revisão.                                                            

  Read 1 file (ctrl+o to expand)

● Update(src\main\java\com\desafio\reembolso\leitor\ValidadorEnvelope.java)  ⎿  Added 20 lines
       1  package com.desafio.reembolso.leitor;
       2  
       3  import com.desafio.reembolso.modelo.Envelope;
       4 +import com.fasterxml.jackson.databind.DeserializationFeature;  
       5  import com.fasterxml.jackson.databind.JsonNode;
       6 +import com.fasterxml.jackson.databind.ObjectMapper;            
       7  import com.fasterxml.jackson.databind.node.JsonNodeType;
       8  
       9 +import java.io.IOException;                                    
      10 +import java.nio.file.Path;                                     
      11  import java.time.LocalDate;
      12  import java.time.format.DateTimeFormatter;
      13  import java.time.format.DateTimeParseException;
     ...
      23   */
      24  public final class ValidadorEnvelope {
      25  
      26 +    private static final ObjectMapper MAPPER = new ObjectMapper
         +()                                                             
      27 +            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_
         +FLOATS)                                                        
      28 +            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOK
         +ENS);                                                          
      29 +                                                               
      30      private static final DateTimeFormatter FORMATO_DATA = DateT
          imeFormatter
      31              .ofPattern("uuuu-MM-dd")
      32              .withResolverStyle(ResolverStyle.STRICT);
     ...
      36      private ValidadorEnvelope() {
      37      }
      38  
      39 +    /**                                                        
      40 +     * Lê o arquivo de entrada com o {@code ObjectMapper} de pr
         +odução                                                         
      41 +     * (DT-004: {@code USE_BIG_DECIMAL_FOR_FLOATS}; DT-005: lei
         +tura por                                                       
      42 +     * árvore {@code JsonNode}, com o contrato sintático de T-0
         +02 preservado                                                  
      43 +     * via {@code FAIL_ON_TRAILING_TOKENS}) e valida o envelope
         + resultante.                                                   
      44 +     * Não escreve nem cria arquivo de saída.                  
      45 +     */                                                        
      46 +    public static Envelope lerEValidar(Path arquivo) throws IOE
         +xception {                                                     
      47 +        JsonNode raiz = MAPPER.readTree(arquivo.toFile());     
      48 +        return validar(raiz);                                  
      49 +    }                                                          
      50 +                                                               
      51      public static Envelope validar(JsonNode raiz) {
      52          if (raiz == null || raiz.getNodeType() != JsonNodeType.
          OBJECT) {
      53              throw new EnvelopeInvalidoException("A raiz do docu
          mento deve ser um objeto JSON");

● Now let's rewrite the test file.

  Read 1 file (ctrl+o to expand)

● Write(tests\java\com\desafio\reembolso\leitor\EnvelopeValidoTest.java)
  ⎿  Added 31 lines, removed 22 lines
       1  package com.desafio.reembolso.leitor;
       2  
       3  import com.desafio.reembolso.modelo.Envelope;
       4 -import com.fasterxml.jackson.databind.DeserializationFeature;  
       4  import com.fasterxml.jackson.databind.JsonNode;
       6 -import com.fasterxml.jackson.databind.ObjectMapper;            
       5  import org.junit.jupiter.api.DisplayName;
       6  import org.junit.jupiter.api.Test;
       7  import org.junit.jupiter.api.io.TempDir;
       8  
       9  import java.io.IOException;
      10 +import java.math.BigDecimal;                                   
      11  import java.nio.charset.StandardCharsets;
      12  import java.nio.file.Files;
      13  import java.nio.file.Path;
     ...
      21  
      22  /**
      23   * Cobre RN-001 / CA-020 (spec 4.1): validação do envelope e to
          lerância dos
      25 - * metadados opcionais de {@code colaborador} e {@code periodo.
         -competencia}.                                                  
      26 - * O {@code ObjectMapper} é configurado com {@code USE_BIG_DECI
         -MAL_FOR_FLOATS}                                                
      27 - * (DT-004), padrão adotado a partir desta task para toda leitu
         -ra de árvore                                                   
      28 - * {@code JsonNode} (DT-005).                                  
      24 + * metadados opcionais de {@code colaborador} e {@code periodo.
         +competencia},                                                  
      25 + * usando o {@code ObjectMapper} de produção de {@link Validado
         +rEnvelope}                                                     
      26 + * (DT-004: {@code USE_BIG_DECIMAL_FOR_FLOATS}; DT-005: leitura
         + por árvore                                                    
      27 + * {@code JsonNode}).                                          
      28   */
      29  @DisplayName("Envelope — RN-001 / CA-020")
      30  class EnvelopeValidoTest {
      31  
      33 -    private static final ObjectMapper MAPPER = new ObjectMapper
         -()                                                             
      34 -            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_
         -FLOATS);                                                       
      35 -                                                               
      36 -    private JsonNode lerArvore(Path arquivo) throws IOException
         - {                                                             
      37 -        return MAPPER.readTree(arquivo.toFile());              
      38 -    }                                                          
      39 -                                                               
      32      @Test
      33      @DisplayName("RN-001 / CA-020 — periodo.inicio posterior a 
          periodo.fim é envelope inválido (exit 3), sem tocar --output")
      34      void periodoInicioPosteriorAFimEnvelopeInvalido(@TempDir Pa
          th tempDir) throws IOException {
     ...
      46  
      47          Path outputInexistente = tempDir.resolve("resultado-nov
          o.json");
      48  
      57 -        JsonNode raiz = lerArvore(input);                      
      58 -                                                               
      49          ValidadorEnvelope.EnvelopeInvalidoException excecao = a
          ssertThrows(
      50                  ValidadorEnvelope.EnvelopeInvalidoException.cla
          ss,
      61 -                () -> ValidadorEnvelope.validar(raiz));        
      51 +                () -> ValidadorEnvelope.lerEValidar(input));   
      52  
      53          assertEquals(3, excecao.codigoSaida(),
      54                  "erro de envelope deve corresponder ao exit cod
          e 3 (DT-003), distinto do exit 2");
     ...
      70                  }
      71                  """, StandardCharsets.UTF_8);
      72  
      83 -        JsonNode raiz = lerArvore(input);                      
      73 +        Envelope envelope = ValidadorEnvelope.lerEValidar(input
         +);                                                             
      74  
      85 -        Envelope envelope = ValidadorEnvelope.validar(raiz);   
      86 -                                                               
      75          assertEquals(LocalDate.of(2026, 7, 1), envelope.getPeri
          odoInicio());
      76          assertEquals(LocalDate.of(2026, 7, 31), envelope.getPer
          iodoFim());
      77          assertTrue(envelope.getDespesas().isArray(), "despesas 
          deve ser representado como lista");
     ...
       94                  }
       95                  """, StandardCharsets.UTF_8);
       96  
      109 -        JsonNode raiz = lerArvore(input);                     
       97 +        Envelope envelope = ValidadorEnvelope.lerEValidar(inpu
          +t);                                                           
       98  
      111 -        Envelope envelope = ValidadorEnvelope.validar(raiz);  
      112 -                                                              
       99          assertNull(envelope.getColaboradorId(),
      100                  "colaborador.id deve ser nulo quando colaborad
           or não é objeto — sem coerção do texto recebido");
      101          assertNull(envelope.getColaboradorNome(),
     ...
      103          assertNull(envelope.getColaboradorCentroCusto(),
      104                  "colaborador.centro_custo deve ser nulo quando
            colaborador não é objeto — sem coerção do texto recebido");
      105      }
      106 +                                                              
      107 +    @Test                                                     
      108 +    @DisplayName("DT-004 — ObjectMapper de produção preserva v
          +alor decimal exato (BigDecimal), não double")                 
      109 +    void objectMapperDeProducaoPreservaValorComoBigDecimal(@Te
          +mpDir Path tempDir) throws IOException {                      
      110 +        Path input = tempDir.resolve("entrada.json");         
      111 +        Files.writeString(input, """                          
      112 +                {                                             
      113 +                  "periodo": { "inicio": "2026-07-01", "fim": 
          +"2026-07-31" },                                               
      114 +                  "despesas": [                               
      115 +                    { "valor": 33.333 }                       
      116 +                  ]                                           
      117 +                }                                             
      118 +                """, StandardCharsets.UTF_8);                 
      119 +                                                              
      120 +        Envelope envelope = ValidadorEnvelope.lerEValidar(inpu
          +t);                                                           
      121 +                                                              
      122 +        JsonNode valor = envelope.getDespesas().get(0).get("va
          +lor");                                                        
      123 +                                                              
      124 +        assertTrue(valor.isBigDecimal(),                      
      125 +                "USE_BIG_DECIMAL_FOR_FLOATS deve preservar o n
          +úmero como BigDecimal, não como double");                     
      126 +        assertEquals(0, new BigDecimal("33.333").compareTo(val
          +or.decimalValue()),                                           
      127 +                "o valor decimal exato deve ser preservado sem
          + perda de precisão binária");                                 
      128 +    }                                                         
      129  }

● Now let's run the verification commands.

  Ran 1 shell command (ctrl+o to expand)

● 4/4 as expected. Now full suite + diff checks.

  Ran 1 shell command (ctrl+o to expand)

● Todos os resultados esperados foram alcançados.

  Correção aplicada:
  - ValidadorEnvelope.java agora tem um ObjectMapper estático de produção com
  USE_BIG_DECIMAL_FOR_FLOATS + FAIL_ON_TRAILING_TOKENS, e o método público
  lerEValidar(Path arquivo) que lê o arquivo, obtém a árvore JsonNode e delega
  para validar(JsonNode) — que permanece disponível para validação isolada. Não
  escreve nem cria arquivo de saída.
  - EnvelopeValidoTest.java não tem mais ObjectMapper próprio; os três testes
  originais agora chamam ValidadorEnvelope.lerEValidar(input) diretamente. Um
  quarto teste (objectMapperDeProducaoPreservaValorComoBigDecimal) comprova
  DT-004: um item bruto com "valor": 33.333 é preservado em despesas como nó
  BigDecimal (isBigDecimal() verdadeiro, decimalValue() igual a 33.333 exato) —
  sem validar a estrutura do item ou implementar RN-002.

  Verificação:
  mvn test -Dtest=EnvelopeValidoTest  → 4/4 passaram
  mvn test (suíte completa)            → 56/56 passaram
  git diff --check                     → limpo
  git status --short                   → apenas os três arquivos originais da
  T-004 (não rastreados), nenhum arquivo de task anterior/posterior tocado

  Nada foi commitado. Aguardando revisão.

✻ Sautéed for 1m 23s

※ recap: Implementing T-004 (envelope validation) for the reembolso engine; just fixed the ObjectMapper/BigDecimal gap and all 56 tests pass. Waiting on your review before any commit. (disable recaps in /config)

❯ Vamos finalizar exclusivamente a T-004.                                       
                                                                                
  A implementação e os testes foram revisados e aprovados.                      
                                                                                
  Não altere novamente os arquivos Java.                                        
  Não inicie a T-005.                                                           
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  `specs/001-motor-reembolso/tasks.md`                                          
                                                                                
  Na T-004:                                                                     
                                                                                
  * marque o título como `[x]`;                                                 
  * altere o Status para `[x] concluída`;                                       
  * não altere nenhuma outra task.                                              
                                                                                
  Depois execute:                                                               
                                                                                
  ```powershell                                                                 
  mvn test -Dtest=EnvelopeValidoTest                                            
  mvn test                                                                      
  git diff --check                                                              
  git status --short                                                            
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * 4 testes de `EnvelopeValidoTest` verdes;                                    
  * 56 testes da suíte completa verdes;                                         
  * somente os três arquivos da T-004 e `tasks.md` alterados;                   
  * T-005 permanece pendente.                                                   
                                                                                
  Pare após a verificação.                                                      

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      119  
      120  ## Fase 2 — Envelope e contrato do item
      121  
      122 -- [ ] **T-004** — Validação do envelope (RN-001) e metadados o
          -pcionais                                                      
      122 +- [x] **T-004** — Validação do envelope (RN-001) e metadados o
          +pcionais                                                      
      123    - **O que faz:** arquivo com envelope inválido (4.1) é recus
           ado com exit `3`, sem escrever nada em `--output`; envelope vá
           lido segue com `colaborador` e `periodo.competencia` tolerante
           s, conforme a tabela de tolerância de 4.1.
      124    - **RN atendidas:** RN-001.
      125    - **CA atendidos:** CA-020.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      136      mvn test -Dtest=EnvelopeValidoTest
      137      ```
      138    - **Commit:** `feat(T-004): valida envelope e metadados opci
           onais com testes` (1 commit).
      139 -  - **Status:** [ ] pendente                                  
      139 +  - **Status:** [x] concluída                                 
      140  
      141  - [ ] **T-005** — Contrato estrutural do item (RN-002) e prese
           rvação de `valor_informado`
      142    - **O que faz:** cada elemento de `despesas` vira um registr
           o estruturalmente validado: `indice_entrada` atribuído (base 1
           , antes de qualquer validação, imutável); classificação fechad
           a entre `CAMPO_AUSENTE`, `CAMPO_TIPO_INVALIDO` e `CAMPO_FORMAT
           O_INVALIDO` por campo, conforme a regra fechada de 4.2; `ITEM_
           TIPO_INVALIDO` como motivo único quando o elemento não é objet
           o; motivos estruturais múltiplos emitidos na ordem canônica de
            contrato (id, data, categoria, descricao, fornecedor, valor, 
           tem_nota_fiscal); `valor_informado` preservado exatamente como
            recebido, inclusive quando o tipo é inválido.

● Now let's run the verification commands.

  Searched for 1 pattern, ran 1 shell command (ctrl+o to expand)

● Verificação concluída, T-004 finalizada.

  - mvn test -Dtest=EnvelopeValidoTest → 4/4 verdes
  - mvn test (suíte completa) → 56/56 verdes
  - git diff --check → limpo (apenas aviso de LF/CRLF do Git no Windows, sem
  erro real)
  - git status --short → alterados: specs/001-motor-reembolso/tasks.md (T-004
  marcada [x]) + os três arquivos novos da T-004 (ValidadorEnvelope.java,
  Envelope.java, EnvelopeValidoTest.java), nenhum outro arquivo tocado
  - T-005 confirmada [ ] pendente (linha 141)

  Nada foi commitado. Parando aqui.

✻ Crunched for 31s