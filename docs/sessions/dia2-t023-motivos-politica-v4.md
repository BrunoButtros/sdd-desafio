 ▐▛███▜▌   Claude Code v2.1.222
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Vamos implementar exclusivamente a task T-023 do backlog aprovado.            
                                                                                
  Base normativa:                                                               
                                                                                
  - spec.md versão 1.2, aprovada;                                               
  - plan.md versão 1.1, aprovado;                                               
  - tasks.md versão 1.1, aprovado;                                              
  - T-001 a T-022 concluídas;                                                   
  - T-023 é a próxima task pendente do Dia 2.                                   
                                                                                
  Nesta sessão, implemente somente T-023.                                       
                                                                                
  Não implemente T-024 ou qualquer task posterior.                              
                                                                                
  ## 1. Leitura obrigatória                                                     
                                                                                
  Leia integralmente:                                                           
                                                                                
  - CLAUDE.md;                                                                  
  - specs/001-motor-reembolso/spec.md;                                          
  - specs/001-motor-reembolso/plan.md;                                          
  - specs/001-motor-reembolso/tasks.md.                                         
                                                                                
  Leia especificamente a descrição completa de T-023.                           
                                                                                
  Inspecione os arquivos reais antes de editar:                                 
                                                                                
  - src/main/java/com/desafio/reembolso/modelo/MotivoCodigo.java;               
  - src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java;          
  - tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java;         
  - tests/java/com/desafio/reembolso/pipeline/OrdemMotivosTest.java.            
                                                                                
  Antes de editar, informe brevemente:                                          
                                                                                
  1. os treze valores atuais de MotivoCodigo;                                   
  2. a implementação atual de criarEstagios();                                  
  3. os testes existentes que serão preservados;                                
  4. os três arquivos exatos que serão alterados.                               
                                                                                
  Depois continue sem esperar confirmação.                                      
                                                                                
  ## 2. Escopo permitido                                                        
                                                                                
  Altere somente:                                                               
                                                                                
  - src/main/java/com/desafio/reembolso/modelo/MotivoCodigo.java;               
  - src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java;          
  - tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java.         
                                                                                
  Não altere OrdemMotivosTest nesta task; ele deve apenas ser executado como    
  regressão.                                                                    
                                                                                
  Não altere:                                                                   
                                                                                
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - tasks.md;                                                                   
  - README.md;                                                                  
  - outros arquivos de produção;                                                
  - outros testes;                                                              
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Não marque T-023 como concluída ainda.                                        
                                                                                
  Não execute /export, git add, commit ou push.                                 
                                                                                
  ## 3. Estender MotivoCodigo                                                   
                                                                                
  Acrescente exatamente estes três valores:                                     
                                                                                
  MOEDA_SEM_COTACAO                                                             
  CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO                                       
  TETO_INDIVIDUAL_APLICADO                                                      
                                                                                
  Não remova, renomeie ou altere nenhum dos treze valores históricos.           
                                                                                
  Cada novo valor deve continuar usando o mecanismo atual de serialização do    
  enum e produzir exatamente o próprio nome:                                    
                                                                                
  "MOEDA_SEM_COTACAO"                                                           
  "CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO"                                     
  "TETO_INDIVIDUAL_APLICADO"                                                    
                                                                                
  Não espalhe essas strings por código de produção fora do enum.                
                                                                                
  ## 4. Atualizar imediatamente criarEstagios()                                 
                                                                                
  Atualize CompositorSaida.criarEstagios() para a tabela final de onze          
  estágios, numerados de 0 a 10:                                                
                                                                                
  ### Estágio 0                                                                 
                                                                                
  ITEM_TIPO_INVALIDO                                                            
                                                                                
  ### Estágio 1 — motivos estruturais                                           
                                                                                
  CAMPO_AUSENTE                                                                 
  CAMPO_TIPO_INVALIDO                                                           
  CAMPO_FORMATO_INVALIDO                                                        
                                                                                
  ### Estágio 2                                                                 
                                                                                
  ID_DUPLICADO                                                                  
                                                                                
  ### Estágio 3                                                                 
                                                                                
  MOEDA_SEM_COTACAO                                                             
                                                                                
  ### Estágio 4                                                                 
                                                                                
  VALOR_NAO_POSITIVO                                                            
                                                                                
  ### Estágio 5                                                                 
                                                                                
  CATEGORIA_FORA_POLITICA                                                       
                                                                                
  ### Estágio 6                                                                 
                                                                                
  CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO                                       
                                                                                
  ### Estágio 7                                                                 
                                                                                
  FORA_COMPETENCIA                                                              
                                                                                
  ### Estágio 8                                                                 
                                                                                
  NOTA_FISCAL_AUSENTE                                                           
                                                                                
  ### Estágio 9                                                                 
                                                                                
  DUPLICIDADE                                                                   
                                                                                
  ### Estágio 10 — motivos de teto                                              
                                                                                
  TETO_DIARIO_APLICADO                                                          
  TETO_DIARIO_ESGOTADO                                                          
  TETO_HOSPEDAGEM_APLICADO                                                      
  TETO_INDIVIDUAL_APLICADO                                                      
                                                                                
  Preserve:                                                                     
                                                                                
  - EnumMap;                                                                    
  - mapa imutável;                                                              
  - validação fechada de estagioDe();                                           
  - ordem relativa de todos os motivos históricos;                              
  - criarOrdemCampo() intacto;                                                  
  - record ResultadoItem intacto;                                               
  - composição da saída intacta.                                                
                                                                                
  Atualize também o comentário JavaDoc de criarEstagios(), caso ele ainda       
  descreva somente os estágios históricos 0 a 8.                                
                                                                                
  O comentário deve refletir a existência dos onze estágios finais, sem alterar 
  regra de negócio.                                                             
                                                                                
  ## 5. Testes                                                                  
                                                                                
  Atualize VocabularioMotivoTest para comprovar:                                
                                                                                
  - os três novos MotivoCodigo serializam exatamente para seus próprios nomes;  
  - os treze valores históricos continuam presentes e com a mesma serialização; 
  - MotivoCodigo agora possui exatamente dezesseis valores;                     
  - não existe colisão de texto canônico entre os dezesseis valores.            
                                                                                
  Execute:                                                                      
                                                                                
  mvn -q test "-Dtest=VocabularioMotivoTest,OrdemMotivosTest"                   
  mvn -q test                                                                   
                                                                                
  Os dois comandos precisam passar integralmente.                               
                                                                                
  Não enfraqueça, remova ou ignore testes existentes para obter sucesso.        
                                                                                
  ## 6. Verificação do diff                                                     
                                                                                
  Execute:                                                                      
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- src/main/java/com/desafio/reembolso/modelo/MotivoCodigo.java      
  git diff -- src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java 
  git diff --                                                                   
  tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java            
  git diff -- tests/java/com/desafio/reembolso/pipeline/OrdemMotivosTest.java   
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- README.md                                                         
                                                                                
  Confirme:                                                                     
                                                                                
  - somente os três arquivos permitidos foram alterados;                        
  - MotivoCodigo contém exatamente dezesseis valores;                           
  - os três códigos novos estão presentes;                                      
  - criarEstagios() contém entrada para todos os dezesseis códigos;             
  - existem exatamente onze números de estágio possíveis, de 0 a 10;            
  - todos os motivos de teto usam estágio 10;                                   
  - criarOrdemCampo() permaneceu intacto;                                       
  - ResultadoItem permaneceu intacto;                                           
  - OrdemMotivosTest permaneceu intacto;                                        
  - tasks.md continua com T-023 pendente;                                       
  - nenhuma task posterior foi iniciada;                                        
  - nenhum /export, git add, commit ou push foi executado.                      
                                                                                
  ## 7. Entrega para revisão                                                    
                                                                                
  Ao final, apresente:                                                          
                                                                                
  1. resumo objetivo da implementação;                                          
  2. resultado dos dois comandos Maven;                                         
  3. lista exata dos arquivos alterados;                                        
  4. git status --short;                                                        
  5. conteúdo completo de MotivoCodigo.java;                                    
  6. função completa criarEstagios() após a alteração;                          
  7. JavaDoc completo relacionado à tabela de estágios;                         
  8. trechos completos alterados em VocabularioMotivoTest.                      
                                                                                
  Pare para revisão.                                                            

  Searched for 2 patterns, read 5 files (ctrl+o to expand)

● Task confirmed and matches the user's instructions exactly. Now let me check
  spec.md section 4.5 and 8.3 for the vocabulary/stage table to confirm
  consistency.

  Searched for 1 pattern, read 1 file (ctrl+o to expand)

● Everything matches the user's instructions and spec/tasks. Now let me report
  the pre-edit summary before making changes.

  1. Os treze valores atuais de MotivoCodigo: ITEM_TIPO_INVALIDO, CAMPO_AUSENTE,
  CAMPO_TIPO_INVALIDO, CAMPO_FORMATO_INVALIDO, ID_DUPLICADO,
  VALOR_NAO_POSITIVO, CATEGORIA_FORA_POLITICA, FORA_COMPETENCIA,
  NOTA_FISCAL_AUSENTE, DUPLICIDADE, TETO_DIARIO_APLICADO, TETO_DIARIO_ESGOTADO,
  TETO_HOSPEDAGEM_APLICADO.

  2. Implementação atual de criarEstagios(): nove estágios (0–8) num EnumMap
  imutável: 0 ITEM_TIPO_INVALIDO; 1 os três motivos estruturais; 2 ID_DUPLICADO;
  3 VALOR_NAO_POSITIVO; 4 CATEGORIA_FORA_POLITICA; 5 FORA_COMPETENCIA; 6
  NOTA_FISCAL_AUSENTE; 7 DUPLICIDADE; 8 os três motivos de teto históricos.

  3. Testes existentes preservados: todos os de VocabularioMotivoTest
  (RegraNegocio, CampoCanonico, Decisao) além de
  motivoCodigoSerializaTextoCanonico, motivoCodigoSemColisaoDeTexto (atualizados
  para os 16 valores); e OrdemMotivosTest inteiro, sem nenhuma modificação —
  roda só como regressão.

  4. Arquivos exatos a alterar:
  - src/main/java/com/desafio/reembolso/modelo/MotivoCodigo.java
  - src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java
  - tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java

  Prosseguindo com a implementação.

● Update(src\main\java\com\desafio\reembolso\modelo\MotivoCodigo.java)
  ⎿  Added 4 lines, removed 1 line
      11      CAMPO_TIPO_INVALIDO,
      12      CAMPO_FORMATO_INVALIDO,
      13      ID_DUPLICADO,
      14 +    MOEDA_SEM_COTACAO,                                         
      15      VALOR_NAO_POSITIVO,
      16      CATEGORIA_FORA_POLITICA,
      17 +    CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO,                   
      18      FORA_COMPETENCIA,
      19      NOTA_FISCAL_AUSENTE,
      20      DUPLICIDADE,
      21      TETO_DIARIO_APLICADO,
      22      TETO_DIARIO_ESGOTADO,
      21 -    TETO_HOSPEDAGEM_APLICADO;                                  
      23 +    TETO_HOSPEDAGEM_APLICADO,                                  
      24 +    TETO_INDIVIDUAL_APLICADO;                                  
      25  
      26      @JsonValue
      27      public String textoCanonico() {

● Update(src\main\java\com\desafio\reembolso\pipeline\CompositorSaida.java)
  ⎿  Added 17 lines, removed 11 lines
      235       * Tabela explícita de precedência de apresentação (spec 8
           .3) — nunca
      236       * {@code enum.ordinal()}. Estágio 0: {@code ITEM_TIPO_INV
           ALIDO}
      237       * (sempre único). Estágio 1: os três motivos estruturais,
            desempatados
      238 -     * por {@link #ORDEM_CAMPO}. Estágios 2 a 7: a sequência f
          -ixa de                                                        
      239 -     * {@code ID_DUPLICADO} a {@code DUPLICIDADE}. Estágio 8: 
          -os motivos de                                                 
      240 -     * limitação de teto, que na prática aparecem sozinhos.   
      238 +     * por {@link #ORDEM_CAMPO}. Estágios 2 a 9: a sequência f
          +ixa de                                                        
      239 +     * {@code ID_DUPLICADO} a {@code DUPLICIDADE}, incluindo o
          +s dois motivos                                                
      240 +     * de política v4 ({@code MOEDA_SEM_COTACAO} no estágio 3 
          +e                                                             
      241 +     * {@code CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO} no está
          +gio 6).                                                       
      242 +     * Estágio 10: os motivos de limitação de teto, que na prá
          +tica aparecem                                                 
      243 +     * sozinhos.                                              
      244       */
      245      private static Map<MotivoCodigo, Integer> criarEstagios() 
           {
      246          Map<MotivoCodigo, Integer> mapa = new EnumMap<>(Motivo
           Codigo.class);
     ...
      249          mapa.put(MotivoCodigo.CAMPO_TIPO_INVALIDO, 1);
      250          mapa.put(MotivoCodigo.CAMPO_FORMATO_INVALIDO, 1);
      251          mapa.put(MotivoCodigo.ID_DUPLICADO, 2);
      249 -        mapa.put(MotivoCodigo.VALOR_NAO_POSITIVO, 3);         
      250 -        mapa.put(MotivoCodigo.CATEGORIA_FORA_POLITICA, 4);    
      251 -        mapa.put(MotivoCodigo.FORA_COMPETENCIA, 5);           
      252 -        mapa.put(MotivoCodigo.NOTA_FISCAL_AUSENTE, 6);        
      253 -        mapa.put(MotivoCodigo.DUPLICIDADE, 7);                
      254 -        mapa.put(MotivoCodigo.TETO_DIARIO_APLICADO, 8);       
      255 -        mapa.put(MotivoCodigo.TETO_DIARIO_ESGOTADO, 8);       
      256 -        mapa.put(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, 8);   
      252 +        mapa.put(MotivoCodigo.MOEDA_SEM_COTACAO, 3);          
      253 +        mapa.put(MotivoCodigo.VALOR_NAO_POSITIVO, 4);         
      254 +        mapa.put(MotivoCodigo.CATEGORIA_FORA_POLITICA, 5);    
      255 +        mapa.put(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTR
          +O_CUSTO, 6);                                                  
      256 +        mapa.put(MotivoCodigo.FORA_COMPETENCIA, 7);           
      257 +        mapa.put(MotivoCodigo.NOTA_FISCAL_AUSENTE, 8);        
      258 +        mapa.put(MotivoCodigo.DUPLICIDADE, 9);                
      259 +        mapa.put(MotivoCodigo.TETO_DIARIO_APLICADO, 10);      
      260 +        mapa.put(MotivoCodigo.TETO_DIARIO_ESGOTADO, 10);      
      261 +        mapa.put(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, 10);  
      262 +        mapa.put(MotivoCodigo.TETO_INDIVIDUAL_APLICADO, 10);  
      263          return Collections.unmodifiableMap(mapa);
      264      }
      265  

  Searched for 1 pattern (ctrl+o to expand)

● Good, that's the only relevant JavaDoc. Now update the test file.

● Update(tests\java\com\desafio\reembolso\modelo\VocabularioMotivoTest.java)
  ⎿  Added 6 lines, removed 3 lines
      31              "CAMPO_TIPO_INVALIDO, CAMPO_TIPO_INVALIDO",
      32              "CAMPO_FORMATO_INVALIDO, CAMPO_FORMATO_INVALIDO",
      33              "ID_DUPLICADO, ID_DUPLICADO",
      34 +            "MOEDA_SEM_COTACAO, MOEDA_SEM_COTACAO",            
      35              "VALOR_NAO_POSITIVO, VALOR_NAO_POSITIVO",
      36              "CATEGORIA_FORA_POLITICA, CATEGORIA_FORA_POLITICA",
      37 +            "CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, CATEGORIA
         +_NAO_REEMBOLSAVEL_CENTRO_CUSTO",                               
      38              "FORA_COMPETENCIA, FORA_COMPETENCIA",
      39              "NOTA_FISCAL_AUSENTE, NOTA_FISCAL_AUSENTE",
      40              "DUPLICIDADE, DUPLICIDADE",
      41              "TETO_DIARIO_APLICADO, TETO_DIARIO_APLICADO",
      42              "TETO_DIARIO_ESGOTADO, TETO_DIARIO_ESGOTADO",
      43              "TETO_HOSPEDAGEM_APLICADO, TETO_HOSPEDAGEM_APLICADO
          ",
      44 +            "TETO_INDIVIDUAL_APLICADO, TETO_INDIVIDUAL_APLICADO
         +",                                                             
      45      })
      46      @DisplayName("Spec 4.5 / DT-008 — MotivoCodigo")
      47      void motivoCodigoSerializaTextoCanonico(MotivoCodigo valor,
           String textoCanonico) throws Exception {
     ...
      49      }
      50  
      51      @Test
      49 -    @DisplayName("MotivoCodigo cobre exatamente os treze valore
         -s de 4.5, nenhum a mais nem a menos")                          
      50 -    void motivoCodigoTemTrezeValores() {                       
      51 -        assertEquals(13, MotivoCodigo.values().length);        
      52 +    @DisplayName("MotivoCodigo cobre exatamente os dezesseis va
         +lores de 4.5 (T-023: acrescenta os três da politica v4), nenhum
         + a mais nem a menos")                                          
      53 +    void motivoCodigoTemDezesseisValores() {                   
      54 +        assertEquals(16, MotivoCodigo.values().length);        
      55      }
      56  
      57      @ParameterizedTest(name = "RegraNegocio.RN_{0} serializa pa
          ra \"RN-{0}\"")

  Ran 1 shell command (ctrl+o to expand)

● No output means success (quiet mode). Now run the full test suite.

  Ran 1 shell command (ctrl+o to expand)

● Both Maven runs passed silently (quiet mode, no failures). Now let's verify
  the diff.

  Ran 1 shell command (ctrl+o to expand)

● Only the three permitted files changed (no line-ending errors, just CRLF/LF
  warnings which are pre-existing repo config, not new issues). Let's confirm
  the other files remain untouched and check the individual diffs.

  Ran 1 shell command (ctrl+o to expand)

● All empty — confirmed untouched. Let's grab the full diffs for the three
  changed files for the delivery report.

  Ran 1 shell command (ctrl+o to expand)

● Entrega para revisão — T-023

  1. Resumo: MotivoCodigo ganhou os três valores da política v4
  (MOEDA_SEM_COTACAO, CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO,
  TETO_INDIVIDUAL_APLICADO), sem tocar nos treze históricos.
  CompositorSaida.criarEstagios() foi renumerado para a tabela final de 11
  estágios (0–10), preservando a ordem relativa de todos os motivos antigos.
  VocabularioMotivoTest foi atualizado para cobrir os 16 valores.

  2. Resultado dos comandos Maven: ambos passaram integralmente, sem falhas
  (saída vazia em modo -q):
  - mvn -q test "-Dtest=VocabularioMotivoTest,OrdemMotivosTest" ✅
  - mvn -q test (suíte completa) ✅

  3. Arquivos alterados (exatamente os três permitidos):
  - src/main/java/com/desafio/reembolso/modelo/MotivoCodigo.java
  - src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java
  - tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java

  OrdemMotivosTest, spec.md, plan.md, tasks.md, DECISIONS.md, README.md — sem
  diff, confirmado.

  4. git status --short:
   M src/main/java/com/desafio/reembolso/modelo/MotivoCodigo.java
   M src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java
   M tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java

  5. MotivoCodigo.java completo:
  package com.desafio.reembolso.modelo;

  import com.fasterxml.jackson.annotation.JsonValue;

  /**
   * Vocabulário fechado de códigos de motivo (spec 4.5).
   */
  public enum MotivoCodigo {
      ITEM_TIPO_INVALIDO,
      CAMPO_AUSENTE,
      CAMPO_TIPO_INVALIDO,
      CAMPO_FORMATO_INVALIDO,
      ID_DUPLICADO,
      MOEDA_SEM_COTACAO,
      VALOR_NAO_POSITIVO,
      CATEGORIA_FORA_POLITICA,
      CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO,
      FORA_COMPETENCIA,
      NOTA_FISCAL_AUSENTE,
      DUPLICIDADE,
      TETO_DIARIO_APLICADO,
      TETO_DIARIO_ESGOTADO,
      TETO_HOSPEDAGEM_APLICADO,
      TETO_INDIVIDUAL_APLICADO;

      @JsonValue
      public String textoCanonico() {
          return name();
      }
  }

  6/7. criarEstagios() + JavaDoc completos:
      /**
       * Tabela explícita de precedência de apresentação (spec 8.3) — nunca
       * {@code enum.ordinal()}. Estágio 0: {@code ITEM_TIPO_INVALIDO}
       * (sempre único). Estágio 1: os três motivos estruturais, desempatados
       * por {@link #ORDEM_CAMPO}. Estágios 2 a 9: a sequência fixa de
       * {@code ID_DUPLICADO} a {@code DUPLICIDADE}, incluindo os dois motivos
       * de política v4 ({@code MOEDA_SEM_COTACAO} no estágio 3 e
       * {@code CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO} no estágio 6).
       * Estágio 10: os motivos de limitação de teto, que na prática aparecem
       * sozinhos.
       */
      private static Map<MotivoCodigo, Integer> criarEstagios() {
          Map<MotivoCodigo, Integer> mapa = new EnumMap<>(MotivoCodigo.class);
          mapa.put(MotivoCodigo.ITEM_TIPO_INVALIDO, 0);
          mapa.put(MotivoCodigo.CAMPO_AUSENTE, 1);
          mapa.put(MotivoCodigo.CAMPO_TIPO_INVALIDO, 1);
          mapa.put(MotivoCodigo.CAMPO_FORMATO_INVALIDO, 1);
          mapa.put(MotivoCodigo.ID_DUPLICADO, 2);
          mapa.put(MotivoCodigo.MOEDA_SEM_COTACAO, 3);
          mapa.put(MotivoCodigo.VALOR_NAO_POSITIVO, 4);
          mapa.put(MotivoCodigo.CATEGORIA_FORA_POLITICA, 5);
          mapa.put(MotivoCodigo.CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, 6);
          mapa.put(MotivoCodigo.FORA_COMPETENCIA, 7);
          mapa.put(MotivoCodigo.NOTA_FISCAL_AUSENTE, 8);
          mapa.put(MotivoCodigo.DUPLICIDADE, 9);
          mapa.put(MotivoCodigo.TETO_DIARIO_APLICADO, 10);
          mapa.put(MotivoCodigo.TETO_DIARIO_ESGOTADO, 10);
          mapa.put(MotivoCodigo.TETO_HOSPEDAGEM_APLICADO, 10);
          mapa.put(MotivoCodigo.TETO_INDIVIDUAL_APLICADO, 10);
          return Collections.unmodifiableMap(mapa);
      }

  8. Trechos alterados em VocabularioMotivoTest:
              "ID_DUPLICADO, ID_DUPLICADO",
              "MOEDA_SEM_COTACAO, MOEDA_SEM_COTACAO",
              "VALOR_NAO_POSITIVO, VALOR_NAO_POSITIVO",
              "CATEGORIA_FORA_POLITICA, CATEGORIA_FORA_POLITICA",
              "CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, 
  CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO",
              "FORA_COMPETENCIA, FORA_COMPETENCIA",
              "NOTA_FISCAL_AUSENTE, NOTA_FISCAL_AUSENTE",
              "DUPLICIDADE, DUPLICIDADE",
              "TETO_DIARIO_APLICADO, TETO_DIARIO_APLICADO",
              "TETO_DIARIO_ESGOTADO, TETO_DIARIO_ESGOTADO",
              "TETO_HOSPEDAGEM_APLICADO, TETO_HOSPEDAGEM_APLICADO",
              "TETO_INDIVIDUAL_APLICADO, TETO_INDIVIDUAL_APLICADO",
      })
      @DisplayName("Spec 4.5 / DT-008 — MotivoCodigo")
      void motivoCodigoSerializaTextoCanonico(MotivoCodigo valor, String
  textoCanonico) throws Exception {
          assertEquals("\"" + textoCanonico + "\"", serializar(valor));
      }

      @Test
      @DisplayName("MotivoCodigo cobre exatamente os dezesseis valores de 4.5 
  (T-023: acrescenta os três da politica v4), nenhum a mais nem a menos")
      void motivoCodigoTemDezesseisValores() {
          assertEquals(16, MotivoCodigo.values().length);
      }
  (motivoCodigoSemColisaoDeTexto não precisou de mudança — já itera
  dinamicamente sobre MotivoCodigo.values(), cobrindo os 16 valores
  automaticamente.)

  Nenhuma task posterior foi iniciada, tasks.md continua com T-023 [ ] pendente,
  e nenhum /export, git add, commit ou push foi executado. Parando para
  revisão.

✻ Baked for 2m 7s

> A revisão independente da T-023 foi concluída e aprovada.                     
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Na task T-023, faça somente estas duas mudanças:                              
                                                                                
  - checkbox do título: [ ] para [x];                                           
  - linha Status: [ ] pendente para [x] concluída.                              
                                                                                
  Não altere nenhum outro texto, task, matriz, código, teste ou documento.      
                                                                                
  Não execute git add, commit ou push.                                          
                                                                                
  Pare.                                                                         

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      532    - **Commit sugerido:** `feat(T-022): estende CampoCanonico c
           om despesa.moeda e atualiza ordem de campo no CompositorSaida`
      533    - **Status:** [x] concluída
      534  
      535 -- [ ] **T-023** — Estender `MotivoCodigo` com os três códigos 
          -novos                                                         
      535 +- [x] **T-023** — Estender `MotivoCodigo` com os três códigos 
          +novos                                                         
      536    - **O que faz:** acrescenta `MOEDA_SEM_COTACAO`, `CATEGORIA_
           NAO_REEMBOLSAVEL_CENTRO_CUSTO` e `TETO_INDIVIDUAL_APLICADO` ao
            vocabulário fechado de códigos de motivo (spec 4.5), cada um 
           serializando para o próprio nome do enum (mesmo mecanismo já e
           xistente). No **mesmo commit**, `CompositorSaida.criarEstagios
           ()` é atualizado para reconhecer os três códigos — não se pode
            esperar até T-048, porque `MOEDA_SEM_COTACAO` já é produzido 
           em T-037 e integrado ao pipeline em T-038, `CATEGORIA_NAO_REEM
           BOLSAVEL_CENTRO_CUSTO` já é produzido em T-041 e integrado em 
           T-042, e `TETO_INDIVIDUAL_APLICADO` já é produzido em T-044 e 
           integrado em T-046 — todos antes de T-048. Sem esta atualizaçã
           o imediata, um motivo válido produzido por qualquer uma dessas
            tasks faria `CompositorSaida.estagioDe(...)` lançar `IllegalA
           rgumentException` ("código de motivo fora do vocabulário fecha
           do de precedência").
      537    - **RN atendidas:** RN-019, RN-020.
      538    - **CA atendidos:** base para CA-027, CA-030, CA-049 (fechad
           os nos blocos G/F/H).

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      553      mvn -q test "-Dtest=VocabularioMotivoTest,OrdemMotivosTest
           "
      554      ```
      555    - **Commit sugerido:** `feat(T-023): estende MotivoCodigo co
           m os tres codigos da politica v4 e atualiza estagios do Compos
           itorSaida`
      556 -  - **Status:** [ ] pendente                                  
      556 +  - **Status:** [x] concluída                                 
      557  
      558  - [ ] **T-024** — Estender `RegraNegocio` com RN-019 a RN-022
      559    - **O que faz:** acrescenta `RN_019`, `RN_020`, `RN_021`, `R
           N_022` ao vocabulário fechado de regras, cada uma serializando
            para `"RN-NNN"` (mesmo mecanismo já existente: `name().replac
           e('_', '-')`).
  Ran 1 shell command (ctrl+o to expand)

● Feito. Apenas specs/001-motor-reembolso/tasks.md foi alterado: título de T-023
  marcado [x] e Status para [x] concluída. Nenhum outro texto foi tocado, e
  nenhum git add, commit ou push foi executado. Parando.

✻ Baked for 13s