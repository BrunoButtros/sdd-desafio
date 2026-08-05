 ▐▛███▜▌   Claude Code v2.1.222
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Implemente exclusivamente a T-032 do backlog aprovado.                        
                                                                                
  Leia:                                                                         
                                                                                
  - CLAUDE.md;                                                                  
  - spec.md;                                                                    
  - plan.md §7;                                                                 
  - a descrição completa da T-032 em tasks.md;                                  
  - LeitorPolitica.java, como padrão de leitor e exceção;                       
  - TabelaCambio.java e TabelaCambioTest.java.                                  
                                                                                
  Crie somente:                                                                 
                                                                                
  - src/main/java/com/desafio/reembolso/leitor/LeitorCambio.java;               
  - tests/java/com/desafio/reembolso/leitor/LeitorCambioTest.java;              
  - tests/resources/fixtures/cambio-valido-teste.json.                          
                                                                                
  Não altere TabelaCambio.java.                                                 
  Não implemente T-033 ou tasks posteriores.                                    
                                                                                
  ## API                                                                        
                                                                                
  Crie somente esta API pública:                                                
                                                                                
  public static TabelaCambio ler(Path caminho)                                  
                                                                                
  Métodos auxiliares devem ser privados.                                        
                                                                                
  Use Jackson com JsonNode, USE_BIG_DECIMAL_FOR_FLOATS e                        
  FAIL_ON_TRAILING_TOKENS.                                                      
                                                                                
  Não desserialize diretamente para POJO.                                       
  Não use double ou float.                                                      
                                                                                
  ## Exceção                                                                    
                                                                                
  Crie CambioInvalidoException como classe estática aninhada em LeitorCambio,   
  seguindo o padrão de PoliticaInvalidaException:                               
                                                                                
  - extends RuntimeException;                                                   
  - CODIGO_SAIDA = 2;                                                           
  - codigoSaida();                                                              
  - construtor com mensagem;                                                    
  - construtor com mensagem e causa;                                            
  - preserve a causa em falhas de leitura e parsing.                            
                                                                                
  ## Validação integral                                                         
                                                                                
  Valide todo o arquivo antes de construir TabelaCambio.                        
                                                                                
  Regras:                                                                       
                                                                                
  1. raiz obrigatoriamente objeto;                                              
                                                                                
  2. moeda_base:                                                                
     - obrigatória;                                                             
     - texto;                                                                   
     - exatamente "BRL";                                                        
                                                                                
  3. taxas:                                                                     
     - obrigatório;                                                             
     - objeto;                                                                  
     - pode ser vazio;                                                          
                                                                                
  4. cada chave diretamente dentro de taxas:                                    
     - formato exato AAAA-MM-DD;                                                
     - data real do calendário;                                                 
                                                                                
  5. o valor de cada data deve ser objeto;                                      
                                                                                
  6. cada chave de moeda dentro de uma data:                                    
     - exatamente [A-Z]{3};                                                     
     - sem trim ou conversão de caixa;                                          
                                                                                
  7. cada taxa:                                                                 
     - obrigatória como valor da chave;                                         
     - numérica;                                                                
     - estritamente maior que zero;                                             
                                                                                
  8. fonte e observacao na raiz:                                                
     - ausentes são válidos;                                                    
     - presentes como texto são válidos e descartados;                          
     - null explícito ou qualquer tipo não textual invalidam o arquivo;         
                                                                                
  9. campos desconhecidos na raiz são ignorados;                                
                                                                                
  10. chaves inválidas dentro de taxas não são campos desconhecidos:            
      - data malformada invalida o arquivo;                                     
      - moeda malformada invalida o arquivo.                                    
                                                                                
  Faça a validação completa antes de criar o mapa invertido.                    
                                                                                
  ## Construção                                                                 
                                                                                
  Após toda a validação:                                                        
                                                                                
  - inverta data → moeda → taxa para moeda → NavigableMap<data, taxa>;          
  - use TreeMap para cada moeda;                                                
  - obtenha BigDecimal com decimalValue();                                      
  - devolva new TabelaCambio("BRL", mapaInvertido);                             
  - deixe a cópia defensiva final a cargo do construtor já existente de         
  TabelaCambio.                                                                 
                                                                                
  Não faça conversão monetária, arredondamento, fallback ou resolução de        
  cotação no leitor.                                                            
                                                                                
  ## Testes                                                                     
                                                                                
  LeitorCambioTest deve cobrir:                                                 
                                                                                
  - arquivo inexistente;                                                        
  - caminho ilegível;                                                           
  - JSON inválido;                                                              
  - raiz não objeto;                                                            
  - moeda_base ausente, tipo errado e diferente de BRL;                         
  - taxas ausente e não objeto;                                                 
  - taxas vazio válido;                                                         
  - data malformada;                                                            
  - data inexistente;                                                           
  - valor associado à data não objeto;                                          
  - moeda fora de [A-Z]{3};                                                     
  - taxa não numérica;                                                          
  - taxa zero;                                                                  
  - taxa negativa;                                                              
  - fonte e observacao ausentes válidos;                                        
  - fonte e observacao como texto válidos e descartados;                        
  - pelo menos um tipo não textual para fonte e para observacao sendo           
  rejeitado;                                                                    
  - campo desconhecido na raiz ignorado;                                        
  - fixture válida produz TabelaCambio com estrutura invertida;                 
  - consulta exata e fallback anterior funcionam com os dados lidos da fixture. 
                                                                                
  A T-033 ampliará exaustivamente os sete tipos de fonte e observacao e a       
  imutabilidade. Não antecipe testes duplicados além do necessário para         
  comprovar que a validação já existe.                                          
                                                                                
  Execute:                                                                      
                                                                                
  mvn -q test -Dtest=LeitorCambioTest                                           
  mvn -q test                                                                   
                                                                                
  Não altere tasks.md.                                                          
  Não execute /export, git add, commit ou push.                                 
  Não execute git diff ou git status.                                           
                                                                                
  Ao final, informe somente:                                                    
                                                                                
  - se os dois comandos Maven passaram;                                         
  - quantos casos de teste foram executados.                                    
                                                                                
  Não imprima os arquivos no terminal.                                          

  Searched for 3 patterns, read 3 files (ctrl+o to expand)

● Now let me read the reference files: LeitorPolitica.java, TabelaCambio.java,
  and TabelaCambioTest.java.

  Searched for 1 pattern, read 5 files (ctrl+o to expand)

● Now let me check the pom.xml briefly to confirm test resource paths and
  Jackson dependency.

  Read 1 file (ctrl+o to expand)

● Now I have everything I need. Let me write LeitorCambio.java, mirroring
  LeitorPolitica's structure and API.

● Write(src\main\java\com\desafio\reembolso\leitor\LeitorCambio.java)
  ⎿  Wrote 176 lines to
     src\main\java\com\desafio\reembolso\leitor\LeitorCambio.java
       1 package com.desafio.reembolso.leitor;
       2 
       3 import com.desafio.reembolso.modelo.TabelaCambio;
       4 import com.fasterxml.jackson.databind.DeserializationFeature;
       5 import com.fasterxml.jackson.databind.JsonNode;
       6 import com.fasterxml.jackson.databind.ObjectMapper;
       7 import com.fasterxml.jackson.databind.node.JsonNodeType;
       8 
       9 import java.io.IOException;
      10 import java.math.BigDecimal;
     … +212 lines (ctrl+o to expand)

● Now the fixture file:

● Write(tests\resources\fixtures\cambio-valido-teste.json)
  ⎿  Wrote 10 lines to tests\resources\fixtures\cambio-valido-teste.json
       1 {
       2   "moeda_base": "BRL",
       3   "fonte": "Fixture de teste (T-032)",
       4   "observacao": "Dados fixos para consulta exata e fallback ante
         rior",
       6     "2026-07-15": { "USD": 5.40, "EUR": 5.88 },
       7     "2026-07-17": { "USD": 5.47, "EUR": 5.96 },
       8     "2026-07-20": { "USD": 5.50, "EUR": 6.01 }
       9   }
      10 }
     … +1 line (ctrl+o to expand)

● Now the test file:

● Write(tests\java\com\desafio\reembolso\leitor\LeitorCambioTest.java)
  ⎿  Wrote 368 lines to
     tests\java\com\desafio\reembolso\leitor\LeitorCambioTest.java
      1 package com.desafio.reembolso.leitor;
      2 
      3 import com.desafio.reembolso.modelo.TabelaCambio;
      4 import org.junit.jupiter.api.DisplayName;
      5 import org.junit.jupiter.api.Test;
      6 import org.junit.jupiter.api.io.TempDir;
      7 import org.junit.jupiter.params.ParameterizedTest;
      8 import org.junit.jupiter.params.provider.Arguments;
      9 import org.junit.jupiter.params.provider.MethodSource;
     … +423 lines (ctrl+o to expand)

● Now let's run the tests.

  Ran 2 shell commands (ctrl+o to expand)

● LeitorCambioTest passed. Now the full suite.

  Searched for 2 patterns, ran 2 shell commands (ctrl+o to expand)

● Ambos os comandos Maven passaram:

  - mvn -q test -Dtest=LeitorCambioTest — verde, 28 casos de teste.
  - mvn -q test (suíte completa) — verde, 470 casos de teste no total.

✻ Sautéed for 3m 5s

> A T-032 foi revisada e aprovada.                                              
                                                                                
  Em specs/001-motor-reembolso/tasks.md, altere somente a T-032:                
                                                                                
  - checkbox do título de [ ] para [x];                                         
  - Status de [ ] pendente para [x] concluída.                                  
                                                                                
  Não altere nenhuma outra task ou arquivo.                                     
  Não execute git add, commit ou push.                                          

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      760  
      761  ### Bloco C — Leitor de câmbio externo
      762  
      763 -- [ ] **T-032** — Implementar `LeitorCambio.ler(Path)`        
      763 +- [x] **T-032** — Implementar `LeitorCambio.ler(Path)`        
      764    - **O que faz:** lê e valida integralmente `cambio.json` (sp
           ec 4.1.1, RN-020, RN-022, AMB-035), invertendo a estrutura `da
           ta → moeda → taxa` para `moeda → NavigableMap<data, taxa>` (DT
           -013) na própria leitura. Sucesso devolve `TabelaCambio`; qual
           quer falha lança `CambioInvalidoException` (aninhada em `Leito
           rCambio`, mesmo padrão de `EnvelopeInvalidoException`/`Politic
           aInvalidaException`, `CODIGO_SAIDA = 2`).
      765    - **RN atendidas:** RN-020, RN-022.
      766    - **CA atendidos:** CA-036 (parcial — câmbio), CA-046.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      782      mvn -q test -Dtest=LeitorCambioTest
      783      ```
      784    - **Commit sugerido:** `feat(T-032): implementa LeitorCambio
            com inversao para consulta por data`
      785 -  - **Status:** [ ] pendente                                  
      785 +  - **Status:** [x] concluída                                 
      786  
      787  - [ ] **T-033** — Testes de fronteira do `LeitorCambio`
      788    - **O que faz:** completa a cobertura de `plan.md` §7, ponto
            9: `fonte`/`observacao` presentes com tipo não textual invali
           dam o arquivo (são campos **conhecidos**, não "desconhecidos")
           ; uma chave malformada dentro de `taxas` — data fora de `AAAA-
           MM-DD` ou moeda fora de `[A-Z]{3}` — **não** é tratada como ca
           mpo desconhecido, e invalida o arquivo inteiro, mesmo que a to
           lerância a campos desconhecidos valha na raiz; e a garantia de
            imutabilidade da estrutura invertida.

● T-032 atualizada: checkbox e Status marcados como concluída. Nenhum outro
  arquivo ou task foi tocado.
✻ Cogitated for 11s