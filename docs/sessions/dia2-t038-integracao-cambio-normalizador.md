 ▐▛███▜▌   Claude Code v2.1.223
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Implemente exclusivamente a T-038 do backlog aprovado.                        
                                                                                
  Leia integralmente:                                                           
                                                                                
  - CLAUDE.md;                                                                  
  - specs/001-motor-reembolso/tasks.md, especialmente a T-038;                  
  - specs/001-motor-reembolso/spec.md, especialmente RN-004, RN-009, RN-016,    
  RN-020 e §8.1;                                                                
  - specs/001-motor-reembolso/plan.md, especialmente §9 e §14;                  
  - src/main/java/com/desafio/reembolso/Main.java;                              
  - src/main/java/com/desafio/reembolso/pipeline/Normalizador.java;             
  - src/main/java/com/desafio/reembolso/pipeline/ResolutorCambio.java;          
  - src/main/java/com/desafio/reembolso/pipeline/ValidadorItem.java;            
  - src/main/java/com/desafio/reembolso/modelo/ItemValidado.java;               
  - src/main/java/com/desafio/reembolso/modelo/TabelaCambio.java;               
  - tests/java/com/desafio/reembolso/pipeline/RegraViagemEfeitoNuloTest.java;   
  - todos os testes encontrados pelo inventário obrigatório descrito abaixo.    
                                                                                
  Não implemente T-039 ou qualquer task posterior.                              
  Não integre ainda a política por centro de custo.                             
  Não altere regras de teto ou o vocabulário de motivos.                        
                                                                                
  ## Arquivos autorizados                                                       
                                                                                
  Produção:                                                                     
                                                                                
  - src/main/java/com/desafio/reembolso/pipeline/Normalizador.java;             
  - src/main/java/com/desafio/reembolso/Main.java.                              
                                                                                
  Novos testes/helpers:                                                         
                                                                                
  - tests/java/com/desafio/reembolso/pipeline/ConversaoCambialIntegracaoTest.ja 
  va;                                                                           
  - tests/java/com/desafio/reembolso/pipeline/CambioTesteSupport.java.          
                                                                                
  Teste existente a estender:                                                   
                                                                                
  - tests/java/com/desafio/reembolso/pipeline/RegraViagemEfeitoNuloTest.java.   
                                                                                
  Também podem ser alterados somente os testes históricos do pacote pipeline    
  que o inventário obrigatório comprovar que chamam Normalizador com entrada    
  originada de ValidadorItem.                                                   
                                                                                
  A lista prevista no backlog inclui:                                           
                                                                                
  - tests/java/com/desafio/reembolso/pipeline/CamposDesconhecidosTest.java;     
  - tests/java/com/desafio/reembolso/pipeline/CategoriaForaPoliticaTest.java;   
  - tests/java/com/desafio/reembolso/pipeline/CompetenciaTest.java;             
  - tests/java/com/desafio/reembolso/pipeline/ComposicaoSaidaTest.java;         
  - tests/java/com/desafio/reembolso/pipeline/DistribuicaoTetoTest.java;        
  - tests/java/com/desafio/reembolso/pipeline/DuplicidadeEconomicaTest.java;    
  - tests/java/com/desafio/reembolso/pipeline/NormalizacaoCategoriaTest.java;   
  - tests/java/com/desafio/reembolso/pipeline/NormalizacaoMonetariaTest.java;   
  - tests/java/com/desafio/reembolso/pipeline/NotaFiscalTest.java;              
  - tests/java/com/desafio/reembolso/pipeline/OrdemMotivosTest.java;            
  - tests/java/com/desafio/reembolso/pipeline/ReembolsoParcialTest.java;        
  - tests/java/com/desafio/reembolso/pipeline/TetoDiarioTest.java;              
  - tests/java/com/desafio/reembolso/pipeline/TetoHospedagemTest.java;          
  - tests/java/com/desafio/reembolso/pipeline/ValorNaoPositivoTest.java.        
                                                                                
  Não altere automaticamente todos: use o inventário como fonte da verdade.     
                                                                                
  ## 1. Normalizador                                                            
                                                                                
  Em:                                                                           
                                                                                
  src/main/java/com/desafio/reembolso/pipeline/Normalizador.java                
                                                                                
  altere a normalização monetária para usar exclusivamente:                     
                                                                                
  item.getValorConvertidoBruto()                                                
                                                                                
  em vez de:                                                                    
                                                                                
  item.getValor()                                                               
                                                                                
  O único arredondamento deve continuar sendo:                                  
                                                                                
  setScale(2, RoundingMode.HALF_UP)                                             
                                                                                
  Regras obrigatórias:                                                          
                                                                                
  - não crie fallback para item.getValor();                                     
  - não crie tratamento especial para BRL;                                      
  - se valorConvertidoBruto for null, valorNormalizado deve ser null;           
  - não arredonde em nenhum estágio anterior;                                   
  - atualize o JavaDoc somente onde necessário para deixar claro que o valor    
  normalizado vem do valor convertido bruto.                                    
                                                                                
  ## 2. Wiring no Main                                                          
                                                                                
  Em:                                                                           
                                                                                
  src/main/java/com/desafio/reembolso/Main.java                                 
                                                                                
  mantenha a ordem de validação já aprovada:                                    
                                                                                
  1. construir os quatro Path;                                                  
  2. validar política;                                                          
  3. carregar e validar câmbio;                                                 
  4. somente depois consultar e ler o input.                                    
                                                                                
  Capture o retorno do leitor de câmbio:                                        
                                                                                
  TabelaCambio tabelaCambio = LeitorCambio.ler(cambio)                          
                                                                                
  Adapte a declaração para que tabelaCambio continue disponível depois do bloco 
  try.                                                                          
                                                                                
  A política continua sendo validada, mas seu objeto ainda não entra no         
  pipeline nesta task.                                                          
                                                                                
  Altere a chamada:                                                             
                                                                                
  executarPipeline(envelope)                                                    
                                                                                
  para:                                                                         
                                                                                
  executarPipeline(envelope, tabelaCambio)                                      
                                                                                
  Altere a assinatura privada conforme necessário.                              
                                                                                
  Dentro de executarPipeline, a ordem deve ser exatamente:                      
                                                                                
  1. ValidadorItem.validarLista(...);                                           
  2. DetectorIdDuplicado.detectar(...);                                         
  3. ResolutorCambio.resolverLista(idsVerificados, tabelaCambio);               
  4. Normalizador.normalizarLista(comCambio);                                   
  5. restante do pipeline histórico, sem alteração.                             
                                                                                
  Não conecte PoliticaExterna ao avaliador.                                     
  Não altere os agregadores.                                                    
  Não mude códigos de saída ou escrita atômica.                                 
                                                                                
  Atualize o JavaDoc do método somente se ele continuar omitindo o estágio      
  cambial.                                                                      
                                                                                
  ## 3. CambioTesteSupport                                                      
                                                                                
  Crie:                                                                         
                                                                                
  tests/java/com/desafio/reembolso/pipeline/CambioTesteSupport.java             
                                                                                
  Requisitos:                                                                   
                                                                                
  - classe package-private;                                                     
  - final;                                                                      
  - sem construtor público;                                                     
  - exclusiva de testes;                                                        
  - nenhuma regra de produção;                                                  
  - possuir uma TabelaCambio válida com moeda base BRL e mapa de cotações       
  vazio;                                                                        
  - oferecer helpers que executem ResolutorCambio antes de Normalizador;        
  - permitir resolver/normalizar um ItemValidado e uma List<ItemValidado>;      
  - pode oferecer sobrecargas que recebam uma TabelaCambio específica para      
  cenários estrangeiros;                                                        
  - listas retornadas devem continuar seguindo as garantias dos componentes     
  reais.                                                                        
                                                                                
  Não duplique a lógica cambial dentro do helper: ele deve apenas chamar        
  ResolutorCambio e Normalizador.                                               
                                                                                
  ## 4. Migração obrigatória dos testes históricos                              
                                                                                
  Execute no início:                                                            
                                                                                
  git grep "Normalizador.normalizar" --                                         
  tests/java/com/desafio/reembolso/pipeline                                     
                                                                                
  Esse grep é necessário para inventariar os consumidores; não execute git      
  status ou git diff.                                                           
                                                                                
  Para cada ocorrência:                                                         
                                                                                
  ### Entrada originada de ValidadorItem                                        
                                                                                
  Migre o pipeline para:                                                        
                                                                                
  ValidadorItem                                                                 
  → DetectorIdDuplicado, quando já utilizado pelo teste                         
  → ResolutorCambio, preferencialmente via CambioTesteSupport                   
  → Normalizador                                                                
                                                                                
  Nenhum teste que produza ItemValidado por ValidadorItem pode chegar           
  diretamente ao Normalizador.                                                  
                                                                                
  ### Teste unitário isolado do Normalizador                                    
                                                                                
  Quando o teste constrói ItemValidado diretamente e pretende testar somente    
  Normalizador:                                                                 
                                                                                
  - não force a execução do pipeline inteiro;                                   
  - use explicitamente o construtor de catorze argumentos;                      
  - preencha valorConvertidoBruto com o valor bruto que deverá ser normalizado; 
  - deixe taxa/data de cotação conforme o objetivo do teste;                    
  - não dependa do construtor histórico de dez argumentos para preencher        
  implicitamente os derivados.                                                  
                                                                                
  Preserve os objetivos e asserções históricas.                                 
  Para cenários BRL, o resultado financeiro deve permanecer idêntico.           
                                                                                
  Ao final, repita:                                                             
                                                                                
  git grep "Normalizador.normalizar" --                                         
  tests/java/com/desafio/reembolso/pipeline                                     
                                                                                
  Confirme por leitura que:                                                     
                                                                                
  - toda entrada vinda de ValidadorItem passou por ResolutorCambio;             
  - testes unitários diretos usam ItemValidado já preparado;                    
  - Normalizador.java não contém fallback para item.getValor().                 
                                                                                
  ## 5. ConversaoCambialIntegracaoTest                                          
                                                                                
  Crie:                                                                         
                                                                                
  tests/java/com/desafio/reembolso/pipeline/ConversaoCambialIntegracaoTest.java 
                                                                                
  Cubra pelo menos:                                                             
                                                                                
  ### Ordem real de arredondamento                                              
                                                                                
  - valor informado = 1.005;                                                    
  - taxa = 1.005;                                                               
  - ResolutorCambio produz exatamente 1.010025;                                 
  - Normalizador produz 1.01 com HALF_UP;                                       
  - nunca produzir 1.02;                                                        
  - o teste deve passar pelos dois componentes reais, sem reproduzir a fórmula  
  manual como implementação alternativa.                                        
                                                                                
  ### Conversão normativa                                                       
                                                                                
  - moeda USD;                                                                  
  - valor 40.00;                                                                
  - taxa 5.50;                                                                  
  - valorConvertidoBruto = 220.000;                                             
  - valorNormalizado = 220.00.                                                  
                                                                                
  Use compareTo quando a escala não for parte do requisito.                     
                                                                                
  ### Nota fiscal sobre valor convertido                                        
                                                                                
  Monte o pipeline real necessário:                                             
                                                                                
  ValidadorItem                                                                 
  → DetectorIdDuplicado                                                         
  → ResolutorCambio                                                             
  → Normalizador                                                                
  → AvaliadorRegrasIndividuais                                                  
                                                                                
  Cenário:                                                                      
                                                                                
  - USD 40.00;                                                                  
  - taxa 5.50;                                                                  
  - resultado normalizado 220.00;                                               
  - tem_nota_fiscal = false;                                                    
  - o valor original 40.00 está abaixo de R$100;                                
  - o valor convertido 220.00 está acima do gatilho;                            
  - deve produzir NOTA_FISCAL_AUSENTE;                                          
  - comprovar que RN-009 consulta o valor convertido/normalizado.               
                                                                                
  Não use ainda PoliticaExterna no avaliador; preserve a sobrecarga histórica   
  existente nesta etapa.                                                        
                                                                                
  ## 6. RegraViagemEfeitoNuloTest                                               
                                                                                
  Estenda:                                                                      
                                                                                
  tests/java/com/desafio/reembolso/pipeline/RegraViagemEfeitoNuloTest.java      
                                                                                
  Migre o helper de pipeline já existente para incluir ResolutorCambio antes de 
  Normalizador.                                                                 
                                                                                
  Adicione cobertura CA-028 comprovando que:                                    
                                                                                
  - um item elegível em moeda estrangeira com cotação válida não amplia teto;   
  - não altera o saldo ou resultado de outro item;                              
  - trocar a moeda de um item de BRL para EUR, mantendo equivalente o valor     
  convertido em BRL, não altera o comportamento de RN-016;                      
  - nenhuma inferência de viagem ocorre por moeda estrangeira.                  
                                                                                
  Use uma TabelaCambio explícita para o cenário EUR.                            
  Não altere as regras históricas dos agregadores.                              
                                                                                
  ## Limites                                                                    
                                                                                
  Não altere:                                                                   
                                                                                
  - src/main/java/com/desafio/reembolso/pipeline/ResolutorCambio.java;          
  - src/main/java/com/desafio/reembolso/pipeline/ValidadorItem.java;            
  - src/main/java/com/desafio/reembolso/modelo/ItemValidado.java;               
  - src/main/java/com/desafio/reembolso/modelo/TabelaCambio.java;               
  - specs/001-motor-reembolso/tasks.md;                                         
  - qualquer arquivo fora do inventário comprovadamente necessário.             
                                                                                
  Não implemente:                                                               
                                                                                
  - política por centro de custo;                                               
  - periodicidade externa;                                                      
  - composição final dos novos campos de saída;                                 
  - testes de coexistência da T-039.                                            
                                                                                
  ## Verificação                                                                
                                                                                
  Execute:                                                                      
                                                                                
  git grep "Normalizador.normalizar" --                                         
  tests/java/com/desafio/reembolso/pipeline                                     
  mvn -q test "-Dtest=ConversaoCambialIntegracaoTest,RegraViagemEfeitoNuloTest" 
  mvn -q test                                                                   
                                                                                
  Não execute:                                                                  
                                                                                
  - git status;                                                                 
  - git diff;                                                                   
  - /export;                                                                    
  - git add;                                                                    
  - git commit;                                                                 
  - git push.                                                                   
                                                                                
  Não imprima os arquivos completos no terminal.                                
                                                                                
  Ao final, informe somente:                                                    
                                                                                
  - se os testes específicos passaram;                                          
  - se a suíte completa passou;                                                 
  - total de testes da suíte completa;                                          
  - lista exata, com caminhos completos, de todos os arquivos criados ou        
  alterados.                                                                    

  Searched for 3 patterns, read 1 file, ran 1 shell command (ctrl+o to expand)

● Now let me read the full T-038 task definition (lines 897-970) and the
  surrounding context.

  Searched for 1 pattern, read 1 file (ctrl+o to expand)

● Let me read the full RN-004, RN-009, RN-016, RN-020 and §8.1 sections in
  detail.

  Read 1 file (ctrl+o to expand)

● Now let me read §8.1 of spec.md and §9, §14 of plan.md.

  Searched for 2 patterns, read 2 files (ctrl+o to expand)

● Now let me read the source files: Main.java, Normalizador.java,
  ResolutorCambio.java, ValidadorItem.java, ItemValidado.java,
  TabelaCambio.java.

  Read 3 files (ctrl+o to expand)

● Now let's look at ItemValidado.java and TabelaCambio.java, plus
  ValidadorItem.java quickly for context.

  Searched for 1 pattern, read 3 files (ctrl+o to expand)

● Now let's look at RegraViagemEfeitoNuloTest.java and the historical tests I'll
  need to migrate.

  Read 15 files (ctrl+o to expand)

● Let me check for private helper method usages precisely with grep across the
  affected files.

  Searched for 3 patterns (ctrl+o to expand)
● Now let's start implementing. First, Normalizador.java.

● Update(src\main\java\com\desafio\reembolso\pipeline\Normalizador.java)
  ⎿  User rejected update src\main\java\com\desafio\reembolso\pipeline\Normaliza
     to                   dor.java

✻ Cooked for 11m 7s

> Continue a implementação da T-038 a partir do ponto atual.                    
                                                                                
  Execute todas as tarefas restantes do plano, respeitando integralmente o      
  escopo, a ordem e os arquivos autorizados no prompt original.                 
                                                                                
  Não pare após cada subtarefa. Prossiga até:                                   
                                                                                
  - concluir a alteração do Normalizador;                                       
  - integrar ResolutorCambio ao Main;                                           
  - criar CambioTesteSupport;                                                   
  - migrar todos os testes históricos identificados pelo inventário;            
  - criar ConversaoCambialIntegracaoTest;                                       
  - atualizar RegraViagemEfeitoNuloTest;                                        
  - executar novamente o inventário com git grep;                               
  - executar os testes específicos;                                             
  - executar a suíte completa;                                                  
  - apresentar o relatório final solicitado com a lista exata dos caminhos      
  completos dos arquivos criados ou alterados.                                  
                                                                                
  Não altere tasks.md.                                                          
  Não execute /export, git add, git commit, git push, git diff ou git status.   

● Update(src\main\java\com\desafio\reembolso\pipeline\Normalizador.java)
  ⎿  Added 5 lines, removed 2 lines
      15   * RN-004, RN-005). Não avalia elegibilidade, não decide vocabu
          lário fechado
      16   * de categoria (RN-007, T-009) e não produz motivos novos — ap
          enas carrega
      17   * o valor e a categoria normalizados ao lado do {@link ItemVal
          idado}
      18 - * original, sem alterá-lo.                                    
      18 + * original, sem alterá-lo. O valor normalizado parte de       
      19 + * {@link ItemValidado#getValorConvertidoBruto()} — já resolvid
         +o por                                                          
      20 + * {@link ResolutorCambio} (BRL ou moeda estrangeira convertida
         +) —, nunca de                                                  
      21 + * {@link ItemValidado#getValor()} diretamente (RN-020, plan §9
         +).                                                             
      22   */
      23  public final class Normalizador {
      24  
     ...
      28      }
      29  
      30      public static ItemNormalizado normalizar(ItemValidado item)
           {
      28 -        BigDecimal valorNormalizado = normalizarValor(item.getV
         -alor());                                                       
      31 +        BigDecimal valorNormalizado = normalizarValor(item.getV
         +alorConvertidoBruto());                                        
      32          String categoriaNormalizada = normalizarCategoria(item.
          getCategoria());
      33          return new ItemNormalizado(item, valorNormalizado, cate
          goriaNormalizada);
      34      }

● Now let's update the normalizarValor javadoc for clarity.

  Read 1 file (ctrl+o to expand)

● Auto mode lets Claude handle permission prompts automatically — Claude
  checks each tool call for risky actions and prompt injection before 
  executing. Actions Claude identifies as safe are executed, while 
  actions Claude identifies as risky are blocked and Claude may try a 
  different approach. Ideal for long-running tasks. Sessions are 
  slightly more expensive. Claude can make mistakes that allow harmful 
  commands to run, it's recommended to only use in isolated 
  environments. Shift+Tab to change mode.

● The normalizarValor javadoc is already implicit; the class-level doc covers
  it, so no further edit needed there. Now let's update Main.java.

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 2 lines
       9  import com.desafio.reembolso.leitor.ValidadorEnvelope.EnvelopeI
          nvalidoException;
      10  import com.desafio.reembolso.modelo.Envelope;
      11  import com.desafio.reembolso.modelo.ItemValidado;
      12 +import com.desafio.reembolso.modelo.TabelaCambio;              
      13  import com.desafio.reembolso.pipeline.AgregadorTetoDiario;
      14  import com.desafio.reembolso.pipeline.AgregadorTetoDiario.Resul
          tadoTeto;
      15  import com.desafio.reembolso.pipeline.AgregadorTetoHospedagem;
     ...
      21  import com.desafio.reembolso.pipeline.DetectorIdDuplicado;
      22  import com.desafio.reembolso.pipeline.Normalizador;
      23  import com.desafio.reembolso.pipeline.Normalizador.ItemNormaliz
          ado;
      24 +import com.desafio.reembolso.pipeline.ResolutorCambio;         
      25  import com.desafio.reembolso.pipeline.SeletorElegiveis;
      26  import com.desafio.reembolso.pipeline.SomadorTotal;
      27  import com.desafio.reembolso.pipeline.ValidadorItem;

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 2 lines, removed 1 line
      124          Path output;
      125          Path politica;
      126          Path cambio;
      127 +        TabelaCambio tabelaCambio;                            
      128          try {
      129              input = Path.of(inputPath);
      130              output = Path.of(outputPath);
     ...
      132              cambio = Path.of(cambioPath);
      133  
      134              LeitorPolitica.ler(politica);
      134 -            LeitorCambio.ler(cambio);                         
      135 +            tabelaCambio = LeitorCambio.ler(cambio);          
      136          } catch (InvalidPathException e) {
      137              err.println("Caminho inválido: " + e.getMessage())
           ;
      138              return 2;

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 1 line, removed 1 line
      176              return 3;
      177          }
      178  
      179 -        List<ResultadoItem> resultados = executarPipeline(enve
          -lope);                                                        
      179 +        List<ResultadoItem> resultados = executarPipeline(enve
          +lope, tabelaCambio);                                          
      180          BigDecimal total = SomadorTotal.somar(resultados);
      181          String json = EscritorResultado.serializar(envelope, r
           esultados, total);
      182  

● Update(src\main\java\com\desafio\reembolso\Main.java)
  ⎿  Added 7 lines, removed 6 lines
      192  
      193      /**
      194       * Passos 2 a 10 da ordem canônica (plan §2): valida os it
           ens, detecta
      195 -     * {@code id} duplicado, normaliza, avalia as regras indiv
          -iduais, separa                                                
      196 -     * os elegíveis, detecta duplicidade econômica, aplica os 
          -tetos e compõe                                                
      197 -     * a saída final — na mesma sequência já comprovada pelos 
          -testes de                                                     
      198 -     * pipeline (T-004 a T-016).                              
      195 +     * {@code id} duplicado, resolve a conversão cambial, norm
          +aliza, avalia                                                 
      196 +     * as regras individuais, separa os elegíveis, detecta dup
          +licidade                                                      
      197 +     * econômica, aplica os tetos e compõe a saída final — na 
          +mesma sequência                                               
      198 +     * já comprovada pelos testes de pipeline (T-004 a T-016, 
          +T-037, T-038).                                                
      199       */
      200 -    private static List<ResultadoItem> executarPipeline(Envelo
          -pe envelope) {                                                
      200 +    private static List<ResultadoItem> executarPipeline(Envelo
          +pe envelope, TabelaCambio cambio) {                           
      201          List<ItemValidado> validados = ValidadorItem.validarLi
           sta(envelope.getDespesas());
      202          List<ItemValidado> idsVerificados = DetectorIdDuplicad
           o.detectar(validados);
      203 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(idsVerificados);                                  
      203 +        List<ItemValidado> comCambio = ResolutorCambio.resolve
          +rLista(idsVerificados, cambio);                               
      204 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      205          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      206  
      207          List<ItemAvaliado> aprovados = SeletorElegiveis.seleci
           onar(avaliados);

● Main.java is done. Let's mark task 2 complete and create
  CambioTesteSupport.java.
● Write(tests\java\com\desafio\reembolso\pipeline\CambioTesteSupport.java)
  ⎿  Wrote 61 lines to
     tests\java\com\desafio\reembolso\pipeline\CambioTesteSupport.java
       1 package com.desafio.reembolso.pipeline;
       2 
       3 import com.desafio.reembolso.modelo.ItemValidado;
       4 import com.desafio.reembolso.modelo.TabelaCambio;
       5 import com.desafio.reembolso.pipeline.Normalizador.ItemNormaliza
       6 
       7 import java.util.List;
       8 import java.util.Map;
       9 
      10 /**
     … +66 lines (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\CamposDesconhecidosTest.java)
  ⎿  Added 2 lines, removed 1 line
      124          Envelope envelope = envelope(json);
      125          List<ItemValidado> validados = ValidadorItem.validarLi
           sta(envelope.getDespesas());
      126          List<ItemValidado> idsVerificados = DetectorIdDuplicad
           o.detectar(validados);
      127 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(idsVerificados);                                  
      127 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(idsVerificados);                                    
      128 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      129          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      130          List<ItemAvaliado> aprovados = SeletorElegiveis.seleci
           onar(avaliados);
      131          List<ItemAvaliado> aposDuplicidade = DetectorDuplicida
           deEconomica.detectar(aprovados);

● Update(tests\java\com\desafio\reembolso\pipeline\CategoriaForaPoliticaTest.jav
        a)
  ⎿  Added 1 line, removed 1 line
      55      }
      56  
      57      private static List<ItemNormalizado> normalizar(String json
          ) {
      58 -        return Normalizador.normalizarLista(validar(json));    
      58 +        return Normalizador.normalizarLista(CambioTesteSupport.
         +resolverLista(validar(json)));                                 
      59      }
      60  
      61      private static Motivo categoriaForaPolitica() {

● Update(tests\java\com\desafio\reembolso\pipeline\CategoriaForaPoliticaTest.jav
        a)
  ⎿  Added 2 lines, removed 1 line
      188                  """;
      189          List<ItemValidado> validados = validar(json);
      190          List<ItemValidado> comIdDuplicado = DetectorIdDuplicad
           o.detectar(validados);
      191 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(comIdDuplicado);                                  
      191 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(comIdDuplicado);                                    
      192 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      193          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados);
      194  
      195          ItemAvaliado primeiro = avaliados.get(0);

● Update(tests\java\com\desafio\reembolso\pipeline\CompetenciaTest.java)
  ⎿  Added 6 lines, removed 1 line
      62      }
      63  
      64      private static List<ItemNormalizado> normalizar(String json
          ) {
      65 -        return Normalizador.normalizarLista(validar(json));    
      65 +        return Normalizador.normalizarLista(CambioTesteSupport.
         +resolverLista(validar(json)));                                 
      66      }
      67  
      68 +    private static List<ItemNormalizado> normalizarDespesas(Env
         +elope envelope) {                                              
      69 +        List<ItemValidado> validados = ValidadorItem.validarLis
         +ta(envelope.getDespesas());                                    
      70 +        return Normalizador.normalizarLista(CambioTesteSupport.
         +resolverLista(validados));                                     
      71 +    }                                                          
      72 +                                                               
      73      private static Motivo foraCompetencia() {
      74          return new Motivo(MotivoCodigo.FORA_COMPETENCIA, RegraN
          egocio.RN_008, null);
      75      }

● Now replace all inline occurrences of the chain with the new helper.

● Update(tests\java\com\desafio\reembolso\pipeline\CompetenciaTest.java)
  ⎿  Added 14 lines, removed 14 lines
       95      void dataAnteriorAoInicio_recebeMotivoEFicaInelegivel() {
       96          String json = envelopeComItem("2026-04-15");
       97          Envelope envelope = envelope(json);
       98 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
       98 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
       99  
      100          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      101  
     ...
      114      void dataPosteriorAoFim_recebeMotivoEFicaInelegivel() {
      115          String json = envelopeComItem("2026-08-01");
      116          Envelope envelope = envelope(json);
      117 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      117 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      118  
      119          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      120  
     ...
      127      void bordaInicial_naoRecebeMotivoEPermaneceElegivel() {
      128          String json = envelopeComItem("2026-07-01");
      129          Envelope envelope = envelope(json);
      130 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      130 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      131  
      132          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      133  
     ...
      141      void bordaFinal_naoRecebeMotivoEPermaneceElegivel() {
      142          String json = envelopeComItem("2026-07-31");
      143          Envelope envelope = envelope(json);
      144 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      144 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      145  
      146          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      147  
     ...
      154      void dataInterna_permaneceElegivel() {
      155          String json = envelopeComItem("2026-07-15");
      156          Envelope envelope = envelope(json);
      157 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      157 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      158  
      159          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      160  
     ...
      175                  }
      176                  """.formatted(JANELA_JULHO);
      177          Envelope envelope = envelope(json);
      178 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      178 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      179          assertNull(item.item().getData());
      180  
      181          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
     ...
      200                  }
      201                  """.formatted(JANELA_JULHO);
      202          Envelope envelope = envelope(json);
      203 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      203 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      204  
      205          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      206  
     ...
      241      void valorNegativoComDataFora_recebeAmbosMotivosNaOrdem() 
           {
      242          String json = envelopeComItem("2026-04-15", "-10.00", 
           "alimentacao", true);
      243          Envelope envelope = envelope(json);
      244 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      244 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      245  
      246          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      247  
     ...
      256      void categoriaForaPoliticaComDataFora_recebeAmbosMotivosNa
           Ordem() {
      257          String json = envelopeComItem("2026-04-15", "50.00", "
           coworking", true);
      258          Envelope envelope = envelope(json);
      259 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      259 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      260  
      261          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      262  
     ...
      271      void tresRegrasSimultaneas_ordemExata() {
      272          String json = envelopeComItem("2026-04-15", "-10.00", 
           "coworking", true);
      273          Envelope envelope = envelope(json);
      274 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      274 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      275  
      276          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      277  
     ...
      296                  """;
      297          Envelope envelope = envelope(json);
      298          assertEquals("2026-04", envelope.getPeriodoCompetencia
           ());
      299 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      299 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      300  
      301          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      302  
     ...
      321                  """.formatted(JANELA_JULHO);
      322          Envelope envelope = envelope(json);
      323          List<ItemNormalizado> normalizados =
      324 -                Normalizador.normalizarLista(ValidadorItem.val
          -idarLista(envelope.getDespesas()));                           
      324 +                normalizarDespesas(envelope);                 
      325          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      326  
      327          List<String> idsElegiveis = avaliados.stream()
     ...
      346                  """.formatted(JANELA_JULHO);
      347          Envelope envelope = envelope(json);
      348          List<ItemNormalizado> normalizados =
      349 -                Normalizador.normalizarLista(ValidadorItem.val
          -idarLista(envelope.getDespesas()));                           
      349 +                normalizarDespesas(envelope);                 
      350          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      351  
      352          assertEquals(2, avaliados.size());
     ...
      365      void reaplicacao_naoDuplicaMotivo() {
      366          String json = envelopeComItem("2026-04-15");
      367          Envelope envelope = envelope(json);
      368 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      368 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      369  
      370          ItemAvaliado primeiraAplicacao = AvaliadorRegrasIndivi
           duais.avaliar(item, envelope);
      371          ItemAvaliado segundaAplicacao = AvaliadorRegrasIndivid
           uais.avaliar(item, envelope);

● Now handle the comIdDuplicado chain in CompetenciaTest (test #8).

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\CompetenciaTest.java)
  ⎿  Added 2 lines, removed 1 line
      226          Envelope envelope = envelope(json);
      227          List<ItemValidado> validados = ValidadorItem.validarLi
           sta(envelope.getDespesas());
      228          List<ItemValidado> comIdDuplicado = DetectorIdDuplicad
           o.detectar(validados);
      229 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(comIdDuplicado);                                  
      229 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(comIdDuplicado);                                    
      230 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      231          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      232  
      233          ItemAvaliado primeiro = avaliados.get(0);

  Searched for 1 pattern (ctrl+o to expand)

● CompetenciaTest.java migration complete. Now let's handle
  ComposicaoSaidaTest.java (3 occurrences of the full pipeline chain).

  Searched for 1 pattern (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\ComposicaoSaidaTest.java)
  ⎿  Added 6 lines, removed 3 lines
      73  
      74          List<ItemValidado> validados = ValidadorItem.validarLis
          ta(envelope.getDespesas());
      75          List<ItemValidado> idsVerificados = DetectorIdDuplicado
          .detectar(validados);
      76 -        List<ItemNormalizado> normalizados = Normalizador.norma
         -lizarLista(idsVerificados);                                    
      76 +        List<ItemValidado> comCambio = CambioTesteSupport.resol
         +verLista(idsVerificados);                                      
      77 +        List<ItemNormalizado> normalizados = Normalizador.norma
         +lizarLista(comCambio);                                         
      78          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividua
          is.avaliarLista(normalizados, envelope);
      79  
      80          List<ItemAvaliado> aprovados = SeletorElegiveis.selecio
          nar(avaliados);
     ...
      417          Envelope envelope = ValidadorEnvelope.validar(lerArqui
           voExemplo());
      418          List<ItemValidado> validados = ValidadorItem.validarLi
           sta(envelope.getDespesas());
      419          List<ItemValidado> idsVerificados = DetectorIdDuplicad
           o.detectar(validados);
      419 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(idsVerificados);                                  
      420 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(idsVerificados);                                    
      421 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      422          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      423          List<ItemAvaliado> aprovados = SeletorElegiveis.seleci
           onar(avaliados);
      424          List<ItemAvaliado> aposDuplicidade = DetectorDuplicida
           deEconomica.detectar(aprovados);
     ...
      456          Envelope envelope = ValidadorEnvelope.validar(lerArqui
           voExemplo());
      457          List<ItemValidado> validados = ValidadorItem.validarLi
           sta(envelope.getDespesas());
      458          List<ItemValidado> idsVerificados = DetectorIdDuplicad
           o.detectar(validados);
      457 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(idsVerificados);                                  
      459 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(idsVerificados);                                    
      460 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      461          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      462          List<ItemAvaliado> aprovados = SeletorElegiveis.seleci
           onar(avaliados);
      463          List<ItemAvaliado> aposDuplicidade = DetectorDuplicida
           deEconomica.detectar(aprovados);

  Searched for 1 pattern (ctrl+o to expand)

● Good, those two remaining are direct-construction unit calls (10-arg
  constructor already sets valorConvertidoBruto = valor), which is correct and
  unaffected. Now DistribuicaoTetoTest.java and ReembolsoParcialTest.java (same
  elegiveisParaTetos pattern).

● Update(tests\java\com\desafio\reembolso\pipeline\DistribuicaoTetoTest.java)
  ⎿  Added 2 lines, removed 1 line
      74          Envelope envelope = envelope(json);
      75          List<ItemValidado> validados = ValidadorItem.validarLis
          ta(envelope.getDespesas());
      76          List<ItemValidado> idsVerificados = DetectorIdDuplicado
          .detectar(validados);
      77 -        List<ItemNormalizado> normalizados = Normalizador.norma
         -lizarLista(idsVerificados);                                    
      77 +        List<ItemValidado> comCambio = CambioTesteSupport.resol
         +verLista(idsVerificados);                                      
      78 +        List<ItemNormalizado> normalizados = Normalizador.norma
         +lizarLista(comCambio);                                         
      79          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividua
          is.avaliarLista(normalizados, envelope);
      80          List<ItemAvaliado> aprovados = SeletorElegiveis.selecio
          nar(avaliados);
      81          List<ItemAvaliado> aposDuplicidade = DetectorDuplicidad
          eEconomica.detectar(aprovados);

● Update(tests\java\com\desafio\reembolso\pipeline\ReembolsoParcialTest.java)
  ⎿  Added 2 lines, removed 1 line
      75          Envelope envelope = envelope(json);
      76          List<ItemValidado> validados = ValidadorItem.validarLis
          ta(envelope.getDespesas());
      77          List<ItemValidado> idsVerificados = DetectorIdDuplicado
          .detectar(validados);
      78 -        List<ItemNormalizado> normalizados = Normalizador.norma
         -lizarLista(idsVerificados);                                    
      78 +        List<ItemValidado> comCambio = CambioTesteSupport.resol
         +verLista(idsVerificados);                                      
      79 +        List<ItemNormalizado> normalizados = Normalizador.norma
         +lizarLista(comCambio);                                         
      80          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividua
          is.avaliarLista(normalizados, envelope);
      81          List<ItemAvaliado> aprovados = SeletorElegiveis.selecio
          nar(avaliados);
      82          List<ItemAvaliado> aposDuplicidade = DetectorDuplicidad
          eEconomica.detectar(aprovados);

● Now TetoDiarioTest.java and TetoHospedagemTest.java (same pattern; note both
  also have direct-construction sections that don't need changes).
● Update(tests\java\com\desafio\reembolso\pipeline\TetoDiarioTest.java)
  ⎿  Added 2 lines, removed 1 line
      75          Envelope envelope = envelope(json);
      76          List<ItemValidado> validados = ValidadorItem.validarLis
          ta(envelope.getDespesas());
      77          List<ItemValidado> idsVerificados = DetectorIdDuplicado
          .detectar(validados);
      78 -        List<ItemNormalizado> normalizados = Normalizador.norma
         -lizarLista(idsVerificados);                                    
      78 +        List<ItemValidado> comCambio = CambioTesteSupport.resol
         +verLista(idsVerificados);                                      
      79 +        List<ItemNormalizado> normalizados = Normalizador.norma
         +lizarLista(comCambio);                                         
      80          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividua
          is.avaliarLista(normalizados, envelope);
      81          List<ItemAvaliado> aprovados = SeletorElegiveis.selecio
          nar(avaliados);
      82          List<ItemAvaliado> aposDuplicidade = DetectorDuplicidad
          eEconomica.detectar(aprovados);

● Update(tests\java\com\desafio\reembolso\pipeline\TetoHospedagemTest.java)
  ⎿  Added 2 lines, removed 1 line
      78          Envelope envelope = envelope(json);
      79          List<ItemValidado> validados = ValidadorItem.validarLis
          ta(envelope.getDespesas());
      80          List<ItemValidado> idsVerificados = DetectorIdDuplicado
          .detectar(validados);
      81 -        List<ItemNormalizado> normalizados = Normalizador.norma
         -lizarLista(idsVerificados);                                    
      81 +        List<ItemValidado> comCambio = CambioTesteSupport.resol
         +verLista(idsVerificados);                                      
      82 +        List<ItemNormalizado> normalizados = Normalizador.norma
         +lizarLista(comCambio);                                         
      83          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividua
          is.avaliarLista(normalizados, envelope);
      84          List<ItemAvaliado> aprovados = SeletorElegiveis.selecio
          nar(avaliados);
      85          List<ItemAvaliado> aposDuplicidade = DetectorDuplicidad
          eEconomica.detectar(aprovados);

● Update(tests\java\com\desafio\reembolso\pipeline\NotaFiscalTest.java)
  ⎿  Added 6 lines, removed 1 line
      70      }
      71  
      72      private static List<ItemNormalizado> normalizar(String json
          ) {
      73 -        return Normalizador.normalizarLista(validar(json));    
      73 +        return Normalizador.normalizarLista(CambioTesteSupport.
         +resolverLista(validar(json)));                                 
      74      }
      75  
      76 +    private static List<ItemNormalizado> normalizarDespesas(Env
         +elope envelope) {                                              
      77 +        List<ItemValidado> validados = ValidadorItem.validarLis
         +ta(envelope.getDespesas());                                    
      78 +        return Normalizador.normalizarLista(CambioTesteSupport.
         +resolverLista(validados));                                     
      79 +    }                                                          
      80 +                                                               
      81      private static Motivo notaFiscalAusente() {
      82          return new Motivo(MotivoCodigo.NOTA_FISCAL_AUSENTE, Reg
          raNegocio.RN_009, null);
      83      }

● Update(tests\java\com\desafio\reembolso\pipeline\NotaFiscalTest.java)
  ⎿  Added 3 lines, removed 3 lines
      415                  }
      416                  """.formatted(JANELA_JULHO);
      417          Envelope envelope = envelope(json);
      418 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      418 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      419  
      420          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      421  
     ...
      438                  }
      439                  """.formatted(JANELA_JULHO);
      440          Envelope envelope = envelope(json);
      441 -        ItemNormalizado item = Normalizador.normalizarLista(Va
          -lidadorItem.validarLista(envelope.getDespesas())).get(0);     
      441 +        ItemNormalizado item = normalizarDespesas(envelope).ge
          +t(0);                                                         
      442  
      443          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item, envelope);
      444  
     ...
      510                  """.formatted(JANELA_JULHO);
      511          Envelope envelope = envelope(json);
      512          List<ItemNormalizado> normalizados =
      513 -                Normalizador.normalizarLista(ValidadorItem.val
          -idarLista(envelope.getDespesas()));                           
      513 +                normalizarDespesas(envelope);                 
      514          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      515  
      516          assertEquals(2, avaliados.size());

● Now the comIdDuplicado chain in NotaFiscalTest (RN-009 caso 13).

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\NotaFiscalTest.java)
  ⎿  Added 2 lines, removed 1 line
      371                  """;
      372          List<ItemValidado> validados = validar(json);
      373          List<ItemValidado> comIdDuplicado = DetectorIdDuplicad
           o.detectar(validados);
      374 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(comIdDuplicado);                                  
      374 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(comIdDuplicado);                                    
      375 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      376          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados);
      377  
      378          ItemAvaliado primeiro = avaliados.get(0);

● Let's verify NotaFiscalTest.java grep now.

  Searched for 2 patterns (ctrl+o to expand)

● Now OrdemMotivosTest.java (pipelineCompleto helper).

● Update(tests\java\com\desafio\reembolso\pipeline\OrdemMotivosTest.java)
  ⎿  Added 2 lines, removed 1 line
      54  
      55          List<ItemValidado> validados = ValidadorItem.validarLis
          ta(envelope.getDespesas());
      56          List<ItemValidado> idsVerificados = DetectorIdDuplicado
          .detectar(validados);
      57 -        List<ItemNormalizado> normalizados = Normalizador.norma
         -lizarLista(idsVerificados);                                    
      57 +        List<ItemValidado> comCambio = CambioTesteSupport.resol
         +verLista(idsVerificados);                                      
      58 +        List<ItemNormalizado> normalizados = Normalizador.norma
         +lizarLista(comCambio);                                         
      59          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividua
          is.avaliarLista(normalizados, envelope);
      60  
      61          List<ItemAvaliado> aprovados = SeletorElegiveis.selecio
          nar(avaliados);

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\DuplicidadeEconomicaTest.java
        )
  ⎿  Added 4 lines, removed 2 lines
      79          Envelope envelope = envelope(json);
      80          List<ItemValidado> validados = ValidadorItem.validarLis
          ta(envelope.getDespesas());
      81          List<ItemValidado> comIdsVerificados = DetectorIdDuplic
          ado.detectar(validados);
      82 -        return Normalizador.normalizarLista(comIdsVerificados);
      82 +        List<ItemValidado> comCambio = CambioTesteSupport.resol
         +verLista(comIdsVerificados);                                   
      83 +        return Normalizador.normalizarLista(comCambio);        
      84      }
      85  
      86      private static List<ItemAvaliado> avaliar(String json) {
      87          Envelope envelope = envelope(json);
      88          List<ItemValidado> validados = ValidadorItem.validarLis
          ta(envelope.getDespesas());
      89          List<ItemValidado> comIdsVerificados = DetectorIdDuplic
          ado.detectar(validados);
      89 -        List<ItemNormalizado> normalizados = Normalizador.norma
         -lizarLista(comIdsVerificados);                                 
      90 +        List<ItemValidado> comCambio = CambioTesteSupport.resol
         +verLista(comIdsVerificados);                                   
      91 +        List<ItemNormalizado> normalizados = Normalizador.norma
         +lizarLista(comCambio);                                         
      92          return AvaliadorRegrasIndividuais.avaliarLista(normaliz
          ados, envelope);
      93      }
      94  

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\NormalizacaoCategoriaTest.jav
        a)
  ⎿  Added 3 lines, removed 3 lines
      62      })
      63      void normalizaConformeCasosNormativos(String categoriaInfor
          mada, String esperado) {
      64          ItemValidado item = validarUnicoComCategoria('"' + cate
          goriaInformada + '"');
      65 -        ItemNormalizado normalizado = Normalizador.normalizar(i
         -tem);                                                          
      65 +        ItemNormalizado normalizado = CambioTesteSupport.resolv
         +erENormalizar(item);                                           
      66  
      67          assertEquals(esperado, normalizado.categoriaNormalizada
          ());
      68      }
     ...
      71      @DisplayName("RN-005 — 'transporte urbano' permanece com es
          paço interno, não vira 'transporte_urbano'")
      72      void transporteUrbanoComEspaco_permaneceComEspaco() {
      73          ItemValidado item = validarUnicoComCategoria("\"transpo
          rte urbano\"");
      74 -        ItemNormalizado normalizado = Normalizador.normalizar(i
         -tem);                                                          
      74 +        ItemNormalizado normalizado = CambioTesteSupport.resolv
         +erENormalizar(item);                                           
      75  
      76          assertEquals("transporte urbano", normalizado.categoria
          Normalizada());
      77      }
     ...
      80      @DisplayName("RN-005 — espaços internos não são alterados n
          em colapsados")
      81      void espacosInternos_naoSaoAlterados() {
      82          ItemValidado item = validarUnicoComCategoria("\"transpo
          rte   urbano\"");
      83 -        ItemNormalizado normalizado = Normalizador.normalizar(i
         -tem);                                                          
      83 +        ItemNormalizado normalizado = CambioTesteSupport.resolv
         +erENormalizar(item);                                           
      84  
      85          assertEquals("transporte   urbano", normalizado.categor
          iaNormalizada());
      86      }

● Update(tests\java\com\desafio\reembolso\pipeline\NormalizacaoCategoriaTest.jav
        a)
  ⎿  Added 4 lines, removed 2 lines
      106          assertNull(item.getCategoria(), "pré-condição: categor
           ia estruturalmente inválida (ausente)");
      107          assertFalse(item.getMotivos().isEmpty(), "item já traz
            motivo estrutural CAMPO_AUSENTE");
      108  
      109 -        ItemNormalizado normalizado = Normalizador.normalizar(
          -item);                                                        
      109 +        ItemValidado resolvido = CambioTesteSupport.resolver(i
          +tem);                                                         
      110 +        ItemNormalizado normalizado = Normalizador.normalizar(
          +resolvido);                                                   
      111  
      112          assertNull(normalizado.categoriaNormalizada());
      112 -        assertSame(item, normalizado.item(), "ItemNormalizado 
          -deve apontar para o mesmo ItemValidado");                     
      113 +        assertSame(resolvido, normalizado.item(),             
      114 +                "ItemNormalizado deve apontar para o mesmo Ite
          +mValidado (já resolvido pelo câmbio)");                       
      115          assertEquals(1, item.getMotivos().size(), "normalizaçã
           o não deve acrescentar nem remover motivos");
      116      }
      117  

● Update(tests\java\com\desafio\reembolso\pipeline\NormalizacaoCategoriaTest.jav
        a)
  ⎿  Added 3 lines, removed 2 lines
      119      @DisplayName("item original permanece inalterado após a no
           rmalização")
      120      void itemOriginal_permaneceInalterado() {
      121          ItemValidado item = validarUnicoComCategoria("\"ALIMEN
           TACAO\"");
      122 -        ItemNormalizado normalizado = Normalizador.normalizar(
          -item);                                                        
      122 +        ItemValidado resolvido = CambioTesteSupport.resolver(i
          +tem);                                                         
      123 +        ItemNormalizado normalizado = Normalizador.normalizar(
          +resolvido);                                                   
      124  
      125          assertEquals("ALIMENTACAO", item.getCategoria(), "cate
           goria original não pode ser alterada");
      125 -        assertSame(item, normalizado.item());                 
      126 +        assertSame(resolvido, normalizado.item());            
      127      }
      128  
      129      @Test

● Now the remaining two occurrences (motivosAnteriores_permanecemPresentes and
  normalizarLista_naoModificavel).

● Update(tests\java\com\desafio\reembolso\pipeline\NormalizacaoCategoriaTest.jav
        a)
  ⎿  Added 1 line, removed 1 line
      147          int motivosAntes = item.getMotivos().size();
      148          assertTrue(motivosAntes > 0, "pré-condição: item traz 
           motivo estrutural de data malformada");
      149  
      150 -        ItemNormalizado normalizado = Normalizador.normalizar(
          -item);                                                        
      150 +        ItemNormalizado normalizado = CambioTesteSupport.resol
          +verENormalizar(item);                                         
      151  
      152          assertEquals("alimentacao", normalizado.categoriaNorma
           lizada());
      153          assertEquals(motivosAntes, item.getMotivos().size(), "
           motivos estruturais anteriores devem permanecer intactos");

● Update(tests\java\com\desafio\reembolso\pipeline\NormalizacaoCategoriaTest.jav
        a)
  ⎿  Added 1 line, removed 1 line
      170              throw new RuntimeException(e);
      171          }
      172          List<ItemValidado> itens = ValidadorItem.validarLista(
           despesas);
      173 -        List<ItemNormalizado> resultado = Normalizador.normali
          -zarLista(itens);                                              
      173 +        List<ItemNormalizado> resultado = CambioTesteSupport.r
          +esolverENormalizarLista(itens);                               
      174  
      175          assertThrows(UnsupportedOperationException.class, () -
           > resultado.add(resultado.get(0)));
      176      }

● Let's verify NormalizacaoCategoriaTest.java grep results now.

  Searched for 1 pattern (ctrl+o to expand)
● All ValidadorItem-sourced calls now pass through ResolutorCambio. Now let's
  handle NormalizacaoMonetariaTest.java similarly, checking for assertSame
  conflicts.

  Read 1 file (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\NormalizacaoMonetariaTest.jav
        a)
  ⎿  Added 1 line, removed 1 line
      64      })
      65      void normalizaConformeFronteirasNormativas(String informado
          , String esperado) {
      66          ItemValidado item = validarUnico(informado);
      67 -        ItemNormalizado normalizado = Normalizador.normalizar(i
         -tem);                                                          
      67 +        ItemNormalizado normalizado = CambioTesteSupport.resolv
         +erENormalizar(item);                                           
      68  
      69          assertEquals(2, normalizado.valorNormalizado().scale(),
           "escala deve ser exatamente 2");
      70          assertEquals(new BigDecimal(esperado), normalizado.valo
          rNormalizado());

● Update(tests\java\com\desafio\reembolso\pipeline\NormalizacaoMonetariaTest.jav
        a)
  ⎿  Added 4 lines, removed 2 lines
       97          assertNull(item.getValor(), "pré-condição: valor estru
           turalmente inválido");
       98          assertFalse(item.getMotivos().isEmpty(), "item já traz
            motivo estrutural CAMPO_TIPO_INVALIDO");
       99  
      100 -        ItemNormalizado normalizado = Normalizador.normalizar(
          -item);                                                        
      100 +        ItemValidado resolvido = CambioTesteSupport.resolver(i
          +tem);                                                         
      101 +        ItemNormalizado normalizado = Normalizador.normalizar(
          +resolvido);                                                   
      102  
      103          assertNull(normalizado.valorNormalizado());
      103 -        assertSame(item, normalizado.item(), "ItemNormalizado 
          -deve apontar para o mesmo ItemValidado");                     
      104 +        assertSame(item, normalizado.item(),                  
      105 +                "valor estruturalmente inválido não passa por 
          +conversão: ResolutorCambio devolve a mesma referência");      
      106          assertEquals(1, item.getMotivos().size(), "normalizaçã
           o não deve acrescentar nem remover motivos");
      107      }
      108  

● Update(tests\java\com\desafio\reembolso\pipeline\NormalizacaoMonetariaTest.jav
        a)
  ⎿  Added 1 line, removed 1 line
      124              throw new RuntimeException(e);
      125          }
      126          List<ItemValidado> itens = ValidadorItem.validarLista(
           despesas);
      127 -        List<ItemNormalizado> resultado = Normalizador.normali
          -zarLista(itens);                                              
      127 +        List<ItemNormalizado> resultado = CambioTesteSupport.r
          +esolverENormalizarLista(itens);                               
      128  
      129          assertEquals(2, resultado.size());
      130          assertEquals(1, resultado.get(0).item().getIndiceEntra
           da());

● Update(tests\java\com\desafio\reembolso\pipeline\ValorNaoPositivoTest.java)
  ⎿  Added 1 line, removed 1 line
      51      }
      52  
      53      private static List<ItemNormalizado> normalizar(String json
          ) {
      54 -        return Normalizador.normalizarLista(validar(json));    
      54 +        return Normalizador.normalizarLista(CambioTesteSupport.
         +resolverLista(validar(json)));                                 
      55      }
      56  
      57      private static Motivo valorNaoPositivo() {

● Update(tests\java\com\desafio\reembolso\pipeline\ValorNaoPositivoTest.java)
  ⎿  Added 4 lines, removed 2 lines
      193                  """;
      194          List<ItemValidado> validados = validar(json);
      195          List<ItemValidado> comIdDuplicado = DetectorIdDuplicad
           o.detectar(validados);
      196 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(comIdDuplicado);                                  
      196 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(comIdDuplicado);                                    
      197 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      198          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados);
      199  
      200          ItemAvaliado primeiro = avaliados.get(0);
     ...
      217                  """;
      218          List<ItemValidado> validados = validar(json);
      219          List<ItemValidado> comIdDuplicado = DetectorIdDuplicad
           o.detectar(validados);
      219 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(comIdDuplicado);                                  
      220 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(comIdDuplicado);                                    
      221 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      222          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados);
      223  
      224          ItemAvaliado primeiro = avaliados.get(0);

● Now let's do a full grep sweep across the pipeline test directory to confirm
  every Normalizador.normalizar call is preceded by
  ResolutorCambio/CambioTesteSupport (except RegraViagemEfeitoNuloTest which
  we'll handle next).

  Searched for 1 pattern (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\RegraViagemEfeitoNuloTest.jav
        a)
  ⎿  Added 6 lines
       7  import com.desafio.reembolso.modelo.ItemValidado.Motivo;
       8  import com.desafio.reembolso.modelo.MotivoCodigo;
       9  import com.desafio.reembolso.modelo.RegraNegocio;
      10 +import com.desafio.reembolso.modelo.TabelaCambio;              
      11  import com.desafio.reembolso.pipeline.AgregadorTetoDiario.Resul
          tadoTeto;
      12 +import com.desafio.reembolso.pipeline.AgregadorTetoHospedagem; 
      13  import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuai
          s.ItemAvaliado;
      14  import com.desafio.reembolso.pipeline.Normalizador.ItemNormaliz
          ado;
      15  import com.fasterxml.jackson.databind.DeserializationFeature;
     ...
      19  import org.junit.jupiter.api.Test;
      20  
      21  import java.math.BigDecimal;
      22 +import java.time.LocalDate;                                    
      23  import java.util.ArrayList;
      24  import java.util.List;
      25 +import java.util.Map;                                          
      26 +import java.util.NavigableMap;                                 
      27 +import java.util.TreeMap;                                      
      28  
      29  import static org.junit.jupiter.api.Assertions.assertEquals;
      30  import static org.junit.jupiter.api.Assertions.assertNull;

● Update(tests\java\com\desafio\reembolso\pipeline\RegraViagemEfeitoNuloTest.jav
        a)
  ⎿  Removed 1 line
       9  import com.desafio.reembolso.modelo.RegraNegocio;
      10  import com.desafio.reembolso.modelo.TabelaCambio;
      11  import com.desafio.reembolso.pipeline.AgregadorTetoDiario.Resul
          tadoTeto;
      12 -import com.desafio.reembolso.pipeline.AgregadorTetoHospedagem; 
      12  import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuai
          s.ItemAvaliado;
      13  import com.desafio.reembolso.pipeline.Normalizador.ItemNormaliz
          ado;
      14  import com.fasterxml.jackson.databind.DeserializationFeature;

● Now let's update the pipeline helpers to insert ResolutorCambio and support an
  explicit TabelaCambio overload.

● Update(tests\java\com\desafio\reembolso\pipeline\RegraViagemEfeitoNuloTest.jav
        a)
  ⎿  Added 21 lines, removed 2 lines
      112      // ---- Pipeline real obrigatório ------------------------
           ---------------------
      113  
      114      private static List<ItemAvaliado> elegiveisParaTetos(Strin
           g json) {
      115 +        return elegiveisParaTetos(json, CambioTesteSupport.TAB
          +ELA_BRL);                                                     
      116 +    }                                                         
      117 +                                                              
      118 +    private static List<ItemAvaliado> elegiveisParaTetos(Strin
          +g json, TabelaCambio cambio) {                                
      119          Envelope envelope = envelope(json);
      120          List<ItemValidado> validados = ValidadorItem.validarLi
           sta(envelope.getDespesas());
      121          List<ItemValidado> idsVerificados = DetectorIdDuplicad
           o.detectar(validados);
      118 -        List<ItemNormalizado> normalizados = Normalizador.norm
          -alizarLista(idsVerificados);                                  
      122 +        List<ItemValidado> comCambio = CambioTesteSupport.reso
          +lverLista(idsVerificados, cambio);                            
      123 +        List<ItemNormalizado> normalizados = Normalizador.norm
          +alizarLista(comCambio);                                       
      124          List<ItemAvaliado> avaliados = AvaliadorRegrasIndividu
           ais.avaliarLista(normalizados, envelope);
      125          List<ItemAvaliado> aprovados = SeletorElegiveis.seleci
           onar(avaliados);
      126          List<ItemAvaliado> aposDuplicidade = DetectorDuplicida
           deEconomica.detectar(aprovados);
     ...
      128      }
      129  
      130      private static ResultadoTeto resultadoDiarioUnico(String j
           son) {
      126 -        List<ResultadoTeto> resultados = AgregadorTetoDiario.a
          -plicar(elegiveisParaTetos(json));                             
      131 +        return resultadoDiarioUnico(json, CambioTesteSupport.T
          +ABELA_BRL);                                                   
      132 +    }                                                         
      133 +                                                              
      134 +    private static ResultadoTeto resultadoDiarioUnico(String j
          +son, TabelaCambio cambio) {                                   
      135 +        List<ResultadoTeto> resultados = AgregadorTetoDiario.a
          +plicar(elegiveisParaTetos(json, cambio));                     
      136          assertEquals(1, resultados.size(), "cenário deve produ
           zir exatamente um item elegível de teto diário");
      137          return resultados.get(0);
      138      }
     ...
      143          return resultados.get(0);
      144      }
      145  
      146 +    /**                                                       
      147 +     * Tabela de câmbio explícita para os cenários CA-028: moe
          +da base BRL,                                                  
      148 +     * uma única cotação de EUR na data usada pelos cenários d
          +este teste.                                                   
      149 +     */                                                       
      150 +    private static TabelaCambio tabelaComEur(LocalDate data, B
          +igDecimal taxa) {                                             
      151 +        NavigableMap<LocalDate, BigDecimal> cotacoesEur = new 
          +TreeMap<>();                                                  
      152 +        cotacoesEur.put(data, taxa);                          
      153 +        return new TabelaCambio("BRL", Map.of("EUR", cotacoesE
          +ur));                                                         
      154 +    }                                                         
      155 +                                                              
      156      private static void assertResultadoBaseAlimentacao(Resulta
           doTeto resultado) {
      157          assertEquals(new BigDecimal("60.00"), resultado.valorR
           eembolsavel());
      158          assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, resulta
           do.decisao());

  Read 1 file (ctrl+o to expand)

● Let's add an EUR item builder near itemHospedagem, and add the new tests at
  the end of the file.

● Update(tests\java\com\desafio\reembolso\pipeline\RegraViagemEfeitoNuloTest.jav
        a)
  ⎿  Added 16 lines
       92                  + "\"valor\": " + valorJson + ", \"tem_nota_fi
           scal\": true }";
       93      }
       94  
       95 +    /** Item de alimentação em moeda estrangeira — usado nos c
          +enários CA-028. */                                            
       96 +    private static String itemAlimentacaoMoeda(String id, Stri
          +ng data, String descricao, String fornecedor,                 
       97 +                                                String valorJs
          +on, String moeda) {                                           
       98 +        return "{ \"id\": \"" + id + "\", \"data\": \"" + data
          + + "\", \"categoria\": \"alimentacao\", "                     
       99 +                + "\"descricao\": \"" + descricao + "\", \"for
          +necedor\": \"" + fornecedor + "\", "                          
      100 +                + "\"valor\": " + valorJson + ", \"moeda\": \"
          +" + moeda + "\", \"tem_nota_fiscal\": true }";                
      101 +    }                                                         
      102 +                                                              
      103 +    /** Item de hospedagem em moeda estrangeira — usado nos ce
          +nários CA-028. */                                             
      104 +    private static String itemHospedagemMoeda(String id, Strin
          +g data, String descricao, String fornecedor,                  
      105 +                                               String valorJso
          +n, String moeda) {                                            
      106 +        return "{ \"id\": \"" + id + "\", \"data\": \"" + data
          + + "\", \"categoria\": \"hospedagem\", "                      
      107 +                + "\"descricao\": \"" + descricao + "\", \"for
          +necedor\": \"" + fornecedor + "\", "                          
      108 +                + "\"valor\": " + valorJson + ", \"moeda\": \"
          +" + moeda + "\", \"tem_nota_fiscal\": true }";                
      109 +    }                                                         
      110 +                                                              
      111      private static String envelopeComItens(String extraRaizJso
           n, String colaboradorJson, String... itensJson) {
      112          List<String> camposRaiz = new ArrayList<>();
      113          if (!colaboradorJson.isEmpty()) {

  Read 1 file (ctrl+o to expand)

● Now let's append the CA-028 tests before the closing brace.

● Update(tests\java\com\desafio\reembolso\pipeline\RegraViagemEfeitoNuloTest.jav
        a)  ⎿  Added 82 lines
      348          assertEquals(RegraNegocio.RN_013, motivo.regra());
      349          assertNull(motivo.campo());
      350      }
      351 +                                                              
      352 +    // ---- 14. CA-028: moeda estrangeira não amplia teto ----
          +-----------------------------------                           
      353 +                                                              
      354 +    @Test                                                     
      355 +    @DisplayName("14 — CA-028: item elegível em EUR com cotaçã
          +o válida não amplia o teto diário, resultado idêntico ao cenár
          +io-base BRL")                                                 
      356 +    void moedaEstrangeiraComCotacaoValida_naoAmpliaTetoDiario(
          +) {                                                           
      357 +        LocalDate data = LocalDate.of(2026, 7, 3);            
      358 +        TabelaCambio tabelaEur = tabelaComEur(data, new BigDec
          +imal("2.00"));                                                
      359 +        String json = envelopeComItem(                        
      360 +                itemAlimentacaoMoeda("d-001", "2026-07-03", "A
          +lmoco com cliente", "Restaurante Sabor",                      
      361 +                        "35.00", "EUR"));                     
      362 +                                                              
      363 +        assertResultadoBaseAlimentacao(resultadoDiarioUnico(js
          +on, tabelaEur));                                              
      364 +    }                                                         
      365 +                                                              
      366 +    // ---- 15. CA-028: moeda estrangeira não afeta outro item
          + do mesmo dia --------------------------                      
      367 +                                                              
      368 +    @Test                                                     
      369 +    @DisplayName("15 — CA-028: hospedagem em EUR não amplia te
          +to próprio nem afeta o teto diário de um item de alimentação d
          +istinto do mesmo período")                                    
      370 +    void moedaEstrangeiraEmHospedagem_naoAfetaOutroItem() {   
      371 +        LocalDate dataAlimentacao = LocalDate.of(2026, 7, 3); 
      372 +        LocalDate dataHospedagem = LocalDate.of(2026, 7, 14); 
      373 +        NavigableMap<LocalDate, BigDecimal> cotacoesEur = new 
          +TreeMap<>();                                                  
      374 +        cotacoesEur.put(dataAlimentacao, new BigDecimal("2.00"
          +));                                                           
      375 +        cotacoesEur.put(dataHospedagem, new BigDecimal("2.00")
          +);                                                            
      376 +        TabelaCambio tabelaEur = new TabelaCambio("BRL", Map.o
          +f("EUR", cotacoesEur));                                       
      377 +                                                              
      378 +        String json = envelopeComItens("", "",                
      379 +                itemAlimentacao("Almoco com cliente", "Restaur
          +ante Sabor"),                                                 
      380 +                itemHospedagemMoeda("d-002", "2026-07-14", "Es
          +tadia corporativa", "Hotel Central",                          
      381 +                        "240.00", "EUR"));                    
      382 +                                                              
      383 +        List<ItemAvaliado> elegiveis = elegiveisParaTetos(json
          +, tabelaEur);                                                 
      384 +        assertEquals(2, elegiveis.size(), "alimentação (BRL) e
          + hospedagem (EUR) não devem colidir por duplicidade econômica"
          +);                                                            
      385 +                                                              
      386 +        List<ResultadoTeto> resultadosDiario = AgregadorTetoDi
          +ario.aplicar(elegiveis);                                      
      387 +        assertEquals(1, resultadosDiario.size());             
      388 +        assertResultadoBaseAlimentacao(resultadosDiario.get(0)
          +);                                                            
      389 +                                                              
      390 +        List<ResultadoTeto> resultadosHospedagem = AgregadorTe
          +toHospedagem.aplicar(elegiveis);                              
      391 +        assertEquals(1, resultadosHospedagem.size());         
      392 +        ResultadoTeto hospedagem = resultadosHospedagem.get(0)
          +;                                                             
      393 +        assertEquals(new BigDecimal("250.00"), hospedagem.valo
          +rReembolsavel(),                                              
      394 +                "hospedagem em EUR (240.00 x 2.00 = 480.00 con
          +vertido) segue o mesmo teto de R$250,00, sem ampliação");     
      395 +        assertEquals(Decisao.PARCIALMENTE_REEMBOLSADO, hospeda
          +gem.decisao());                                               
      396 +        assertEquals(RegraNegocio.RN_013, hospedagem.motivos()
          +.get(0).regra());                                             
      397 +    }                                                         
      398 +                                                              
      399 +    // ---- 16. CA-028: trocar BRL por EUR com valor convertid
          +o equivalente não altera RN-016 --------                      
      400 +                                                              
      401 +    @Test                                                     
      402 +    @DisplayName("16 — CA-028: trocar a moeda de um item de BR
          +L para EUR, mantendo equivalente o valor convertido em BRL, nã
          +o altera o comportamento de RN-016")                          
      403 +    void trocarMoedaDeBrlParaEurComValorEquivalente_resultadoI
          +dentico() {                                                   
      404 +        LocalDate data = LocalDate.of(2026, 7, 3);            
      405 +        String jsonBrl = envelopeComItem(itemAlimentacao("Almo
          +co com cliente", "Restaurante Sabor"));                       
      406 +        ResultadoTeto resultadoBrl = resultadoDiarioUnico(json
          +Brl);                                                         
      407 +                                                              
      408 +        TabelaCambio tabelaEur = tabelaComEur(data, new BigDec
          +imal("2.00"));                                                
      409 +        String jsonEur = envelopeComItem(                     
      410 +                itemAlimentacaoMoeda("d-001", "2026-07-03", "A
          +lmoco com cliente", "Restaurante Sabor",                      
      411 +                        "35.00", "EUR"));                     
      412 +        ResultadoTeto resultadoEur = resultadoDiarioUnico(json
          +Eur, tabelaEur);                                              
      413 +                                                              
      414 +        assertEquals(resultadoBrl.valorReembolsavel(), resulta
          +doEur.valorReembolsavel(),                                    
      415 +                "valor convertido equivalente (35.00 EUR x 2.0
          +0 = 70.00) deve produzir o mesmo resultado que 70.00 BRL");   
      416 +        assertEquals(resultadoBrl.decisao(), resultadoEur.deci
          +sao());                                                       
      417 +        assertEquals(resultadoBrl.motivos(), resultadoEur.moti
          +vos());                                                       
      418 +    }                                                         
      419 +                                                              
      420 +    // ---- 17. CA-028: nenhuma inferência de viagem por moeda
          + estrangeira, mesmo com texto sugestivo --                    
      421 +                                                              
      422 +    @Test                                                     
      423 +    @DisplayName("17 — CA-028: descrição sugestiva de viagem c
          +ombinada com moeda estrangeira ainda não amplia teto algum")  
      424 +    void moedaEstrangeiraComDescricaoSugestiva_naoAmpliaTeto()
          + {                                                            
      425 +        LocalDate data = LocalDate.of(2026, 7, 3);            
      426 +        TabelaCambio tabelaEur = tabelaComEur(data, new BigDec
          +imal("2.00"));                                                
      427 +        String json = envelopeComItem(                        
      428 +                itemAlimentacaoMoeda("d-001", "2026-07-03", "A
          +lmoco no aeroporto", "Restaurante Sabor",                     
      429 +                        "35.00", "EUR"));                     
      430 +                                                              
      431 +        assertResultadoBaseAlimentacao(resultadoDiarioUnico(js
          +on, tabelaEur));                                              
      432 +    }                                                         
      433  }

  Read 1 file (ctrl+o to expand)

● Write(tests\java\com\desafio\reembolso\pipeline\ConversaoCambialIntegracaoTest
       .java)
  ⎿  Wrote 148 lines to tests\java\com\desafio\reembolso\pipeline\Conversao
     CambialIntegracaoTest.java
       1 package com.desafio.reembolso.pipeline;
       2 
       3 import com.desafio.reembolso.leitor.ValidadorEnvelope;
       5 import com.desafio.reembolso.modelo.ItemValidado;
       6 import com.desafio.reembolso.modelo.MotivoCodigo;
       7 import com.desafio.reembolso.modelo.TabelaCambio;
       8 import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuais
       9 import com.desafio.reembolso.pipeline.Normalizador.ItemNormaliza
         do;
      10 import com.fasterxml.jackson.databind.DeserializationFeature;
     … +194 lines (ctrl+o to expand)

● Now let's run the required grep inventory again.

  Ran 1 shell command (ctrl+o to expand)

● Now let's run the specific tests requested.

  Ran 2 shell commands (ctrl+o to expand)

● Both target test classes pass. Now let's run the full suite.

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(tests\java\com\desafio\reembolso\pipeline\ValorNaoPositivoTest.java)
  ⎿  Added 7 lines, removed 4 lines
      159      }
      160  
      161      @Test
      162 -    @DisplayName("7 — data malformada com valor negativo: mant
          -ém CAMPO_FORMATO_INVALIDO de despesa.data e também recebe VALO
          -R_NAO_POSITIVO")                                              
      163 -    void dataInvalidaComValorNegativo_naoBloqueiaRn006() {    
      162 +    @DisplayName("7 — data malformada com valor negativo: mant
          +ém CAMPO_FORMATO_INVALIDO de despesa.data; RN-006 não é avalia
          +da porque, desde RN-020/T-038, valor_normalizado não é calculá
          +vel sem data estruturalmente válida (ResolutorCambio exige val
          +or/moeda/data)")                                              
      163 +    void dataInvalidaComValorNegativo_bloqueiaRn006PorValorNao
          +Calculavel() {                                                
      164          String json = """
      165                  {
      166                    "despesas": [
     ...
      170                  }
      171                  """;
      172          ItemNormalizado item = normalizar(json).get(0);
      173 +        assertNull(item.valorNormalizado(),                   
      174 +                "sem data estruturalmente válida, ResolutorCam
          +bio não resolve o item e valorConvertidoBruto permanece nulo (
          +RN-020)");                                                    
      175  
      176          ItemAvaliado avaliado = AvaliadorRegrasIndividuais.ava
           liar(item);
      177  
      176 -        assertEquals(2, avaliado.motivos().size());           
      178 +        assertEquals(1, avaliado.motivos().size());           
      179          assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, aval
           iado.motivos().get(0).codigo());
      180          assertEquals(CampoCanonico.DATA, avaliado.motivos().ge
           t(0).campo());
      179 -        assertTrue(avaliado.motivos().contains(valorNaoPositiv
          -o()));                                                        
      181 +        assertFalse(avaliado.motivos().contains(valorNaoPositi
          +vo()),                                                        
      182 +                "RN-006 depende de valor_normalizado calculáve
          +l — data inválida impede o cálculo, mesmo com valor negativo e
          +struturalmente válido");                                      
      183          assertFalse(avaliado.elegivel());
      184      }
      185  

  Read 1 file (ctrl+o to expand)

● Let's update that stale claim in the class javadoc to reflect the RN-020
  dependency correctly.

● Update(tests\java\com\desafio\reembolso\pipeline\ValorNaoPositivoTest.java)
  ⎿  Added 7 lines, removed 3 lines
      28   * Cobre RN-006 / CA-017 (parcial — spec 4.5, 7, 8.2, 8.4): ite
          m cujo valor
      29   * normalizado seja menor ou igual a zero recebe {@code VALOR_N
          AO_POSITIVO}
      30   * e fica inelegível, com {@code valorReembolsavel} {@code 0.00
          }. RN-006
      31 - * depende exclusivamente de {@code valorNormalizado} — não é b
         -loqueada por                                                   
      32 - * motivo estrutural em outro campo, nem por {@code ID_DUPLICAD
         -O}, e não é                                                    
      33 - * avaliada quando o próprio valor é estruturalmente inválido. 
      31 + * depende de {@code valorNormalizado} — não é bloqueada por   
      32 + * {@code ID_DUPLICADO}, e não é avaliada quando o próprio valo
         +r é                                                            
      33 + * estruturalmente inválido. Desde RN-020/T-038, {@code valorNo
         +rmalizado}                                                     
      34 + * também não é calculável quando {@code despesa.data} (ou {@co
         +de moeda})                                                     
      35 + * for estruturalmente inválida, porque {@link ResolutorCambio}
         + exige os                                                      
      36 + * três campos — {@code valor}, {@code moeda}, {@code data} — p
         +ara produzir                                                   
      37 + * {@code valorConvertidoBruto}, que {@link Normalizador} conso
         +me.                                                            
      38   */
      39  @DisplayName("Valor não positivo — RN-006 / CA-017 (parcial)")
      40  class ValorNaoPositivoTest {

● Fixed the one stale assertion. Let's re-run the full suite to confirm.

  Ran 2 shell commands (ctrl+o to expand)

● Both specific tests and the full suite pass. Let's mark the final task
  complete and gather the exact list of changed/created files.
● Relatório final — T-038

  Testes específicos (ConversaoCambialIntegracaoTest,
  RegraViagemEfeitoNuloTest): passaram.

  Suíte completa (mvn test): passou — BUILD SUCCESS.

  Total de testes da suíte completa: 542 (542 executados, 0 falhas, 0 erros, 0
  pulados).

  Durante a migração, um teste histórico (ValorNaoPositivoTest, cenário "data
  malformada com valor negativo") quebrou porque sua premissa ficou
  desatualizada pela própria RN-020: com data estruturalmente inválida,
  ResolutorCambio não resolve o item (mesma exigência de valor/moeda/data já
  valendo desde T-037) e valorConvertidoBruto permanece nulo — logo
  valorNormalizado também fica nulo e RN-006 deixa de ser avaliada. Corrigi a
  asserção e o nome do teste para refletir esse comportamento, agora correto
  pela spec (RN-020/plan §9), em vez de reverter a mudança de produção.

  Arquivos criados ou alterados

  Produção:
  - C:\Users\bruno\Desktop\sdd-desafio\src\main\java\com\desafio\reembolso\pipel
  ine\Normalizador.java
  - C:\Users\bruno\Desktop\sdd-desafio\src\main\java\com\desafio\reembolso\Main.
  java

  Novos (testes/helpers):
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \ConversaoCambialIntegracaoTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \CambioTesteSupport.java

  Estendido:
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \RegraViagemEfeitoNuloTest.java

  Migrados (inventário ValidadorItem → ResolutorCambio → Normalizador):
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \CamposDesconhecidosTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \CategoriaForaPoliticaTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \CompetenciaTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \ComposicaoSaidaTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \DistribuicaoTetoTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \DuplicidadeEconomicaTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \NormalizacaoCategoriaTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \NormalizacaoMonetariaTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \NotaFiscalTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \OrdemMotivosTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \ReembolsoParcialTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \TetoDiarioTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \TetoHospedagemTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \ValorNaoPositivoTest.java

  tasks.md não foi alterado; nenhum comando de git (add/commit/push/diff/status)
  nem /export foi executado.

✻ Cogitated for 11m 2s

※ recap: T-038 (câmbio no Normalizador/pipeline) is done: all files migrated, full suite green (542 tests). Next: wait for review before any git commit, since none was made yet. (disable recaps in /config)

> Antes de fechar a T-038, faça uma correção pontual exclusivamente nestes      
  arquivos:                                                                     
                                                                                
  tests/java/com/desafio/reembolso/pipeline/OrdemMotivosTest.java               
  tests/java/com/desafio/reembolso/pipeline/ComposicaoSaidaTest.java            
                                                                                
  Problema:                                                                     
                                                                                
  Os helpers itemValidadoMinimo ainda usam o construtor histórico de dez        
  argumentos de ItemValidado e depois entregam esse objeto diretamente a        
  Normalizador.normalizar(...).                                                 
                                                                                
  A T-038 exige que testes isolados, que constroem ItemValidado diretamente e   
  não executam ResolutorCambio, usem explicitamente o construtor de catorze     
  argumentos e preparem valorConvertidoBruto.                                   
                                                                                
  Em ambos os helpers, substitua o uso do construtor de dez argumentos pelo     
  construtor de catorze argumentos, preservando os dez valores atuais e         
  acrescentando:                                                                
                                                                                
  - moeda = "BRL";                                                              
  - taxaCambioAplicada = BigDecimal.ONE;                                        
  - dataCotacaoUtilizada = null;                                                
  - valorConvertidoBruto = valor.                                               
                                                                                
  A forma final deve ser equivalente a:                                         
                                                                                
  new ItemValidado(                                                             
      indiceEntrada,                                                            
      id,                                                                       
      data,                                                                     
      categoria,                                                                
      "descricao",                                                              
      "fornecedor",                                                             
      valor,                                                                    
      temNotaFiscal ou true, conforme o helper atual,                           
      DecimalNode.valueOf(valor),                                               
      List.of(),                                                                
      "BRL",                                                                    
      BigDecimal.ONE,                                                           
      null,                                                                     
      valor                                                                     
  )                                                                             
                                                                                
  Não inclua ResolutorCambio nesses testes, pois eles constroem objetos         
  diretamente para testar CompositorSaida e cenários defensivos.                
                                                                                
  Não altere nenhuma asserção ou objetivo dos testes.                           
  Não altere qualquer outro arquivo.                                            
  Não altere tasks.md.                                                          
  Não execute /export, git add, commit, push, git diff ou git status.           
                                                                                
  Execute:                                                                      
                                                                                
  mvn -q test "-Dtest=OrdemMotivosTest,ComposicaoSaidaTest,ConversaoCambialInte 
  gracaoTest,RegraViagemEfeitoNuloTest"                                         
  mvn -q test                                                                   
                                                                                
  Ao final, informe somente:                                                    
  - se os testes específicos passaram;                                          
  - se a suíte completa passou;                                                 
  - total de testes da suíte completa;                                          
  - caminhos completos dos arquivos alterados.                                  
                                                                                
  Não imprima os arquivos completos no terminal.                                

  Searched for 2 patterns (ctrl+o to expand)

● Now applying the fix to both files.

● Update(tests\java\com\desafio\reembolso\pipeline\OrdemMotivosTest.java)
  ⎿  Added 2 lines, removed 1 line
      142      private static ItemValidado itemValidadoMinimo(int indiceE
           ntrada, String id, LocalDate data,
      143                                                       String ca
           tegoria, BigDecimal valor, boolean temNotaFiscal) {
      144          return new ItemValidado(indiceEntrada, id, data, categ
           oria, "descricao", "fornecedor",
      145 -                valor, temNotaFiscal, DecimalNode.valueOf(valo
          -r), List.of());                                               
      145 +                valor, temNotaFiscal, DecimalNode.valueOf(valo
          +r), List.of(),                                                
      146 +                "BRL", BigDecimal.ONE, null, valor);          
      147      }
      148  
      149      @Test

● Update(tests\java\com\desafio\reembolso\pipeline\ComposicaoSaidaTest.java)
  ⎿  Added 2 lines, removed 1 line
      505      private static ItemValidado itemValidadoMinimo(int indiceE
           ntrada, String id, LocalDate data,
      506                                                       String ca
           tegoria, BigDecimal valor) {
      507          return new ItemValidado(indiceEntrada, id, data, categ
           oria, "descricao", "fornecedor",
      508 -                valor, true, DecimalNode.valueOf(valor), List.
          -of());                                                        
      508 +                valor, true, DecimalNode.valueOf(valor), List.
          +of(),                                                         
      509 +                "BRL", BigDecimal.ONE, null, valor);          
      510      }
      511  
      512      private static ItemAvaliado itemAvaliadoElegivel(int indic
           eEntrada, String categoria, BigDecimal valor) {

● Now running the requested verification commands.

  Ran 2 shell commands (ctrl+o to expand)

● - Testes específicos: passaram.
  - Suíte completa: passou (BUILD SUCCESS).
  - Total de testes da suíte completa: 542 (0 falhas, 0 erros, 0 pulados).

  Arquivos alterados:
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \OrdemMotivosTest.java
  - C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\pipeline
  \ComposicaoSaidaTest.java

✻ Crunched for 34s

> A T-038 foi revisada e aprovada.                                              
                                                                                
  Em:                                                                           
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  altere somente a T-038:                                                       
                                                                                
  - checkbox do título de [ ] para [x];                                         
  - Status de [ ] pendente para [x] concluída.                                  
                                                                                
  Não altere nenhuma outra task ou arquivo.                                     
  Não execute git add, commit ou push.                                          

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      917    - **Commit sugerido:** `feat(T-037): implementa ResolutorCam
           bio com verificacao de valor/moeda/data e CotacaoResolvida`
      918    - **Status:** [x] concluída
      919  
      920 -- [ ] **T-038** — `Normalizador` sobre `valorConvertidoBruto` 
          -+ wiring da conversão no pipeline + migração dos pipelines de 
          -teste históricos                                              
      920 +- [x] **T-038** — `Normalizador` sobre `valorConvertidoBruto` 
          ++ wiring da conversão no pipeline + migração dos pipelines de 
          +teste históricos                                              
      921    - **O que faz:** `Normalizador` passa a normalizar `item.get
           ValorConvertidoBruto()` em vez de `item.getValor()` diretament
           e — mesmo caminho para BRL e moeda estrangeira, sem `if` de BR
           L dentro do `Normalizador` (plan §9), e **sem nenhum fallback*
           * para `item.getValor()`. `Main.executarPipeline` ganha o está
           gio `ResolutorCambio.resolverLista(...)`, inserido entre `Dete
           ctorIdDuplicado.detectar(...)` e `Normalizador.normalizarLista
           (...)` (spec 8.1, passo 5), usando a `TabelaCambio` já carrega
           da em T-035. Como T-036 já deixa `valorConvertidoBruto` nulo e
           m todo `ItemValidado` produzido por `ValidadorItem` (só `Resol
           utorCambio` o preenche, inclusive para BRL), **qualquer teste 
           histórico** que exercite `ValidadorItem` → (opcionalmente `Det
           ectorIdDuplicado`) → `Normalizador` sem passar por `ResolutorC
           ambio` quebraria ou passaria a obter `valor_normalizado` nulo 
           assim que esta task mudar a leitura do `Normalizador`. Por iss
           o, esta task também migra esses pipelines de teste e cria o he
           lper `CambioTesteSupport`, no mesmo commit — não é aceitável t
           rocar a fonte de leitura do `Normalizador` e deixar a suíte hi
           stórica quebrada até uma task futura arrumar.
      922    - **RN atendidas:** RN-004 (atualizada), RN-009 (atualizada)
           , RN-016 (extensão a moeda), RN-020.
      923    - **CA atendidos:** CA-028, CA-031, CA-032.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      960      mvn -q test
      961      ```
      962    - **Commit sugerido:** `feat(T-038): normaliza sobre valorCo
           nvertidoBruto e integra ResolutorCambio no pipeline`
      963 -  - **Status:** [ ] pendente                                  
      963 +  - **Status:** [x] concluída                                 
      964  
      965  - [ ] **T-039** — `MoedaSemCotacaoTest` — coexistência de moti
           vos (8.4, item 14)
      966    - **O que faz:** confirma, **operacionalmente**, a exclusão 
           de dependência declarada em `spec.md` 8.4 item 14, no ponto do
            backlog em que esta task acontece — **antes** de a política p
           or centro de custo estar integrada ao avaliador (Bloco G, T-04
           0/T-041). Nesta altura do backlog, T-039 ainda utiliza os agre
           gadores e sobrecargas históricas (`SeletorElegiveis`, `Detecto
           rDuplicidadeEconomica`, `AgregadorTetoDiario`, `AgregadorTetoH
           ospedagem`), porque os componentes novos dos blocos G e H aind
           a não foram implementados. Para os cenários de coexistência de
            motivos individuais, o teste executa até `AvaliadorRegrasIndi
           viduais` — mas, para comprovar ausência de duplicidade e de te
           to, o teste **continua** pelos estágios reais: `SeletorElegive
           is` → `DetectorDuplicidadeEconomica` → `SeletorElegiveis` → `A
           gregadorTetoDiario`/`AgregadorTetoHospedagem`. Um item com `MO
           EDA_SEM_COTACAO` pode coexistir com `CATEGORIA_FORA_POLITICA` 
           e/ou `FORA_COMPETENCIA` (produzidos pela sobrecarga histórica 
           de `AvaliadorRegrasIndividuais`, que não dependem de `valor_no
           rmalizado`), mas **nunca** recebe `VALOR_NAO_POSITIVO`, `NOTA_
           FISCAL_AUSENTE`, `DUPLICIDADE` ou qualquer motivo de teto — e 
           essas ausências são comprovadas fazendo o item atravessar de f
           ato os estágios reais de seleção, duplicidade e agregação, não
            apenas inspecionando a lista de motivos parada em `AvaliadorR
           egrasIndividuais`. Esta task **não** cobre a coexistência com 
           `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — esse motivo só exi
           ste a partir de T-041; a coexistência com ele é fechada em T-0
           41, depois que o avaliador de centro de custo passar a existir
           .

● T-038 marcada como concluída em specs/001-motor-reembolso/tasks.md (checkbox
  do título e linha Status). Nenhum outro arquivo ou task foi tocado, e nenhum
  comando git foi executado.

✻ Crunched for 12s