 ▐▛███▜▌   Claude Code v2.1.223
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Implemente exclusivamente a T-043 do backlog aprovado.                        
                                                                                
  Leia integralmente:                                                           
                                                                                
  - CLAUDE.md;                                                                  
  - specs/001-motor-reembolso/tasks.md, especialmente a descrição completa da   
  T-043;                                                                        
  - specs/001-motor-reembolso/spec.md, especialmente RN-011, RN-012, RN-014,    
  RN-015, RN-019 e CA-047;                                                      
  - specs/001-motor-reembolso/plan.md, especialmente §11 e DT-017;              
  - src/main/java/com/desafio/reembolso/pipeline/AgregadorTetoDiario.java;      
  -                                                                             
  src/main/java/com/desafio/reembolso/pipeline/AvaliadorRegrasIndividuais.java; 
  - src/main/java/com/desafio/reembolso/modelo/TabelaPoliticaResolvida.java;    
  - src/main/java/com/desafio/reembolso/modelo/TabelaCategoria.java;            
  - src/main/java/com/desafio/reembolso/modelo/Periodicidade.java;              
  - src/main/java/com/desafio/reembolso/modelo/PoliticaReembolso.java;          
  - src/main/java/com/desafio/reembolso/modelo/ItemValidado.java;               
  - tests/java/com/desafio/reembolso/pipeline/TetoDiarioTest.java;              
  - tests/java/com/desafio/reembolso/pipeline/CategoriaCentroCustoTest.java.    
                                                                                
  Altere somente:                                                               
                                                                                
  - src/main/java/com/desafio/reembolso/pipeline/AgregadorTetoDiario.java.      
                                                                                
  Crie somente:                                                                 
                                                                                
  - tests/java/com/desafio/reembolso/pipeline/TetoPorPeriodicidadeTest.java.    
                                                                                
  Não implemente T-044 ou qualquer task posterior.                              
  Não altere Main.java.                                                         
  Não conecte ainda a nova sobrecarga ao pipeline principal.                    
  Não altere AgregadorTetoHospedagem.                                           
  Não altere modelos, avaliadores, compositor ou SomadorTotal.                  
  Não altere nenhum teste histórico.                                            
                                                                                
  ## Preservação obrigatória da API histórica                                   
                                                                                
  Mantenha intacta a sobrecarga existente:                                      
                                                                                
  public static List<ResultadoTeto> aplicar(List<ItemAvaliado> itens)           
                                                                                
  Ela deve continuar:                                                           
                                                                                
  - usando o conjunto histórico CATEGORIAS_TETO_DIARIO;                         
  - usando PoliticaReembolso;                                                   
  - produzindo os mesmos resultados históricos;                                 
  - passando toda a suíte anterior sem alteração.                               
                                                                                
  Não substitua sua implementação pela política externa.                        
  Não remova CATEGORIAS_TETO_DIARIO, PoliticaReembolso, PADRAO ou               
  aplicarInterno.                                                               
                                                                                
  ## Nova sobrecarga                                                            
                                                                                
  Acrescente exatamente a API pública:                                          
                                                                                
  public static List<ResultadoTeto> aplicar(                                    
      List<ItemAvaliado> itens,                                                 
      TabelaPoliticaResolvida tabela                                            
  )                                                                             
                                                                                
  Essa sobrecarga deve implementar exclusivamente o caminho de política         
  externa.                                                                      
                                                                                
  Ela deve:                                                                     
                                                                                
  - preservar a quantidade e a ordem relativa dos itens aplicáveis na lista     
  retornada;                                                                    
  - não alterar a lista de entrada;                                             
  - devolver lista não modificável;                                             
  - usar a ordem crescente de indiceEntrada somente para o consumo do saldo;    
  - não ordenar permanentemente a lista recebida;                               
  - não produzir resultado para itens que não participam do teto diário.        
                                                                                
  ## Seleção por periodicidade                                                  
                                                                                
  Para cada item recebido:                                                      
                                                                                
  1. confirme que item.elegivel() é true;                                       
  2. obtenha a categoria normalizada;                                           
  3. obtenha explicitamente:                                                    
                                                                                
  TabelaCategoria configuracao =                                                
      tabela.getCategorias().get(categoria);                                    
                                                                                
  4. o item participa do teto compartilhado somente quando:                     
                                                                                
  configuracao != null                                                          
  && configuracao.periodicidade() == Periodicidade.DIA                          
                                                                                
  Não use CATEGORIAS_TETO_DIARIO na nova sobrecarga.                            
                                                                                
  Não determine participação pelo nome da categoria.                            
                                                                                
  Consequências obrigatórias:                                                   
                                                                                
  - categoria dinâmica representacao com periodicidade DIA participa;           
  - uma categoria histórica com periodicidade DIARIA não participa;             
  - categoria ausente da tabela não participa;                                  
  - categoriaNormalizada null não participa;                                    
  - item inelegível não participa.                                              
                                                                                
  Não produza motivos novos para itens que não participam: apenas não os inclua 
  no resultado do agregador.                                                    
                                                                                
  ## Chave e saldo compartilhado                                                
                                                                                
  O saldo deve continuar sendo compartilhado por:                               
                                                                                
  - data;                                                                       
  - categoria normalizada.                                                      
                                                                                
  Use a estrutura ChaveTetoDiario já existente.                                 
                                                                                
  Categorias diferentes não compartilham saldo.                                 
  Datas diferentes não compartilham saldo.                                      
                                                                                
  O limite inicial de cada chave deve vir exclusivamente de:                    
                                                                                
  configuracao.limite()                                                         
                                                                                
  Não consulte PoliticaReembolso na nova sobrecarga.                            
  Não faça fallback para outra tabela ou categoria.                             
                                                                                
  O consumo deve continuar em ordem crescente de:                               
                                                                                
  item.itemNormalizado().item().getIndiceEntrada()                              
                                                                                
  Mesmo quando a lista recebida estiver fora dessa ordem.                       
                                                                                
  ## Aplicação do corte                                                         
                                                                                
  Reutilize exatamente o método existente:                                      
                                                                                
  aplicarCorte(...)                                                             
                                                                                
  Não altere sua assinatura, implementação, arredondamento, decisões ou         
  semântica.                                                                    
                                                                                
  Preserve:                                                                     
                                                                                
  - valor menor ou igual ao saldo:                                              
    - INTEGRALMENTE_REEMBOLSADO;                                                
    - valor integral;                                                           
    - sem motivo de teto;                                                       
                                                                                
  - valor maior que o saldo positivo:                                           
    - PARCIALMENTE_REEMBOLSADO;                                                 
    - reembolso igual ao saldo;                                                 
    - motivo TETO_DIARIO_APLICADO;                                              
                                                                                
  - saldo zero:                                                                 
    - NAO_REEMBOLSADO_TETO_ESGOTADO;                                            
    - reembolso 0.00;                                                           
    - motivo TETO_DIARIO_ESGOTADO / RN-015.                                     
                                                                                
  Não aplique limite zero como recusa de categoria nesta classe. Na execução    
  real, essa recusa pertence ao AvaliadorRegrasIndividuais e impede o item de   
  chegar elegível ao agregador.                                                 
                                                                                
  ## Regra associada a TETO_DIARIO_APLICADO                                     
                                                                                
  Na nova sobrecarga, escolha a RegraNegocio pelo nome da categoria somente     
  para o motivo de corte parcial:                                               
                                                                                
  - categoria "alimentacao" → RegraNegocio.RN_011;                              
  - categoria "transporte_urbano" → RegraNegocio.RN_012;                        
  - qualquer outra categoria → RegraNegocio.RN_019.                             
                                                                                
  O código deve continuar sendo:                                                
                                                                                
  MotivoCodigo.TETO_DIARIO_APLICADO                                             
                                                                                
  Essa pequena seleção de regra não pode ser usada para decidir se a categoria  
  participa do teto.                                                            
                                                                                
  Para saldo esgotado, preserve:                                                
                                                                                
  - MotivoCodigo.TETO_DIARIO_ESGOTADO;                                          
  - RegraNegocio.RN_015.                                                        
                                                                                
  Não crie novos códigos ou regras.                                             
                                                                                
  ## TetoPorPeriodicidadeTest                                                   
                                                                                
  Crie testes focados na nova sobrecarga.                                       
                                                                                
  Use as APIs reais de:                                                         
                                                                                
  - ItemValidado;                                                               
  - ItemNormalizado;                                                            
  - ItemAvaliado;                                                               
  - TabelaCategoria;                                                            
  - TabelaPoliticaResolvida.                                                    
                                                                                
  Quando construir ItemValidado diretamente, use explicitamente o construtor de 
  catorze argumentos e prepare:                                                 
                                                                                
  - moeda = "BRL";                                                              
  - taxaCambioAplicada = BigDecimal.ONE;                                        
  - dataCotacaoUtilizada = null;                                                
  - valorConvertidoBruto = valor.                                               
                                                                                
  Não dependa do construtor histórico para preencher campos cambiais.           
                                                                                
  Cubra pelo menos:                                                             
                                                                                
  ### 1. CA-047 — representacao compartilha saldo diário                        
                                                                                
  Tabela resolvida:                                                             
                                                                                
  - origem CENTRO_CUSTO;                                                        
  - categoria representacao;                                                    
  - limite 300.00;                                                              
  - periodicidade DIA.                                                          
                                                                                
  Crie dois itens elegíveis de representacao na mesma data, com índices         
  diferentes, cujo total ultrapasse 300.00.                                     
                                                                                
  Exemplo:                                                                      
                                                                                
  - índice 1, valor 220.00;                                                     
  - índice 2, valor 150.00.                                                     
                                                                                
  Confirme:                                                                     
                                                                                
  - primeiro item recebe 220.00 integralmente;                                  
  - segundo item recebe 80.00 parcialmente;                                     
  - segundo possui exatamente:                                                  
    - TETO_DIARIO_APLICADO;                                                     
    - RN_019;                                                                   
    - campo null;                                                               
  - nenhum motivo histórico RN-011 ou RN-012 é usado para representacao.        
                                                                                
  ### 2. Consumo por indiceEntrada                                              
                                                                                
  Passe os dois itens à nova sobrecarga em ordem inversa.                       
                                                                                
  Confirme:                                                                     
                                                                                
  - o saldo é consumido pelo menor indiceEntrada;                               
  - não pela ordem física da lista recebida;                                    
  - a lista resultante mantém a ordem relativa dos itens aplicáveis conforme    
  recebida;                                                                     
  - cada resultado continua associado ao ItemAvaliado correto.                  
                                                                                
  ### 3. Saldo esgotado                                                         
                                                                                
  Adicione um terceiro item de representacao na mesma data após o saldo ter     
  sido totalmente consumido.                                                    
                                                                                
  Confirme:                                                                     
                                                                                
  - valorReembolsavel 0.00;                                                     
  - decisão NAO_REEMBOLSADO_TETO_ESGOTADO;                                      
  - motivo TETO_DIARIO_ESGOTADO;                                                
  - regra RN_015.                                                               
                                                                                
  Estruture os valores para que um item anterior consuma exatamente o saldo     
  restante antes do terceiro.                                                   
                                                                                
  ### 4. Separação por data                                                     
                                                                                
  Dois itens de representacao em datas diferentes devem receber saldos          
  independentes de 300.00.                                                      
                                                                                
  ### 5. Separação por categoria                                                
                                                                                
  Duas categorias com periodicidade DIA devem possuir saldos independentes,     
  mesmo na mesma data.                                                          
                                                                                
  ### 6. Periodicidade DIARIA não participa                                     
                                                                                
  Uma categoria presente na tabela, mas com:                                    
                                                                                
  periodicidade = Periodicidade.DIARIA                                          
                                                                                
  não deve aparecer no resultado da nova sobrecarga diária.                     
                                                                                
  ### 7. Categoria ausente e item inelegível                                    
                                                                                
  Confirme que não participam:                                                  
                                                                                
  - categoria ausente de tabela.getCategorias();                                
  - categoriaNormalizada null;                                                  
  - item com elegivel false, ainda que sua categoria tenha periodicidade DIA.   
                                                                                
  ### 8. Regras históricas preservadas                                          
                                                                                
  Na nova sobrecarga, confirme corte parcial de:                                
                                                                                
  - alimentacao → RN_011;                                                       
  - transporte_urbano → RN_012.                                                 
                                                                                
  Não altere TetoDiarioTest.                                                    
  A suíte histórica deve comprovar que a sobrecarga antiga continua igual.      
                                                                                
  ### 9. Imutabilidade                                                          
                                                                                
  Confirme que:                                                                 
                                                                                
  - a lista recebida não é alterada;                                            
  - a lista devolvida é não modificável;                                        
  - os ItemAvaliado originais não são alterados;                                
  - as listas de motivos originais permanecem intactas;                         
  - a tabela resolvida não é alterada.                                          
                                                                                
  Use BigDecimal.compareTo quando a escala não for parte do requisito.          
                                                                                
  ## Limites                                                                    
                                                                                
  Não altere:                                                                   
                                                                                
  -                                                                             
  src/main/java/com/desafio/reembolso/pipeline/AvaliadorRegrasIndividuais.java; 
  - src/main/java/com/desafio/reembolso/pipeline/AgregadorTetoHospedagem.java;  
  - src/main/java/com/desafio/reembolso/Main.java;                              
  - qualquer modelo ou enum;                                                    
  - qualquer teste histórico;                                                   
  - specs/001-motor-reembolso/tasks.md;                                         
  - qualquer arquivo fora dos dois autorizados.                                 
                                                                                
  Não implemente:                                                               
                                                                                
  - teto individual genérico;                                                   
  - wiring dos novos agregadores no Main;                                       
  - remoção de AgregadorTetoHospedagem;                                         
  - composição de novos campos;                                                 
  - T-044 ou posteriores.                                                       
                                                                                
  Execute:                                                                      
                                                                                
  mvn -q test -Dtest=TetoPorPeriodicidadeTest                                   
  mvn -q test                                                                   
                                                                                
  Não execute:                                                                  
                                                                                
  - /export;                                                                    
  - git add;                                                                    
  - git commit;                                                                 
  - git push;                                                                   
  - git diff;                                                                   
  - git status.                                                                 
                                                                                
  Não imprima os arquivos completos no terminal.                                
                                                                                
  Ao final, informe somente:                                                    
                                                                                
  - se os dois comandos Maven passaram;                                         
  - quantidade de testes em TetoPorPeriodicidadeTest;                           
  - total de testes da suíte completa;                                          
  - caminhos completos dos arquivos criados ou alterados.                       

● I'll start by reading all the required files to understand the task fully.

  Searched for 3 patterns, read 3 files (ctrl+o to expand)

● Now let me read T-043 in full detail, plan §11, and CA-047 context in spec.md.

  Read 2 files (ctrl+o to expand)

● Now let me look at RN-011, RN-012, RN-014, RN-015, RN-019 and CA-047 in
  spec.md, plus the source files.

  Searched for 1 pattern, read 2 files (ctrl+o to expand)

● Now let me read the remaining required source files and tests.

  Read 5 files (ctrl+o to expand)

● Now let me check the ItemValidado constructor argument count (14 args in
  second constructor confirmed) and read the existing test files.

  Searched for 1 pattern, read 3 files (ctrl+o to expand)

● Now I have a complete picture. Let me implement the new overload in
  AgregadorTetoDiario.java.

● Update(src\main\java\com\desafio\reembolso\pipeline\AgregadorTetoDiario.java)
  ⎿  Added 3 lines
       3  import com.desafio.reembolso.modelo.Decisao;
       4  import com.desafio.reembolso.modelo.ItemValidado.Motivo;
       5  import com.desafio.reembolso.modelo.MotivoCodigo;
       6 +import com.desafio.reembolso.modelo.Periodicidade;             
       7  import com.desafio.reembolso.modelo.PoliticaReembolso;
       8  import com.desafio.reembolso.modelo.RegraNegocio;
       9 +import com.desafio.reembolso.modelo.TabelaCategoria;           
      10 +import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;   
      11  import com.desafio.reembolso.pipeline.AvaliadorRegrasIndividuai
          s.ItemAvaliado;
      12  
      13  import java.math.BigDecimal;

● Update(src\main\java\com\desafio\reembolso\pipeline\AgregadorTetoDiario.java)
  ⎿  Added 2 lines
      40              new Motivo(MotivoCodigo.TETO_DIARIO_APLICADO, Regra
          Negocio.RN_012, null);
      41      private static final Motivo MOTIVO_ESGOTADO =
      42              new Motivo(MotivoCodigo.TETO_DIARIO_ESGOTADO, Regra
          Negocio.RN_015, null);
      43 +    private static final Motivo MOTIVO_TETO_RN019 =            
      44 +            new Motivo(MotivoCodigo.TETO_DIARIO_APLICADO, Regra
         +Negocio.RN_019, null);                                         
      45  
      46      private static final AgregadorTetoDiario PADRAO =
      47              new AgregadorTetoDiario(PoliticaReembolso.padrao())
          ;

● Now let's add the new overload method after aplicar(List<ItemAvaliado> itens):

● Update(src\main\java\com\desafio\reembolso\pipeline\AgregadorTetoDiario.java)
  ⎿  Added 71 lines
       56          return PADRAO.aplicarInterno(itens);
       57      }
       58  
       59 +    /**                                                       
       60 +     * Sobrecarga por política externa (RN-011, RN-012, RN-015
          +, RN-019;                                                     
       61 +     * DT-017): participa quem tem {@code periodicidade == DIA
          +} na tabela                                                   
       62 +     * resolvida, nunca pelo nome da categoria — {@link #CATEG
          +ORIAS_TETO_DIARIO}                                            
       63 +     * e {@link PoliticaReembolso} não são consultados aqui.  
       64 +     */                                                       
       65 +    public static List<ResultadoTeto> aplicar(List<ItemAvaliad
          +o> itens, TabelaPoliticaResolvida tabela) {                   
       66 +        Objects.requireNonNull(itens, "itens");               
       67 +        Objects.requireNonNull(tabela, "tabela");             
       68 +                                                              
       69 +        List<ItemAvaliado> aplicaveis = new ArrayList<>();    
       70 +        Map<ItemAvaliado, TabelaCategoria> configuracoesPorIte
          +m = new IdentityHashMap<>();                                  
       71 +        for (ItemAvaliado item : itens) {                     
       72 +            if (!item.elegivel()) {                           
       73 +                continue;                                     
       74 +            }                                                 
       75 +            String categoria = item.itemNormalizado().categori
          +aNormalizada();                                               
       76 +            if (categoria == null) {                          
       77 +                continue;                                     
       78 +            }                                                 
       79 +            TabelaCategoria configuracao = tabela.getCategoria
          +s().get(categoria);                                           
       80 +            if (configuracao != null && configuracao.periodici
          +dade() == Periodicidade.DIA) {                                
       81 +                aplicaveis.add(item);                         
       82 +                configuracoesPorItem.put(item, configuracao); 
       83 +            }                                                 
       84 +        }                                                     
       85 +                                                              
       86 +        List<ItemAvaliado> ordenadosPorIndice = new ArrayList<
          +>(aplicaveis);                                                
       87 +        ordenadosPorIndice.sort(Comparator.comparingInt(i -> i
          +.itemNormalizado().item().getIndiceEntrada()));               
       88 +                                                              
       89 +        Map<ChaveTetoDiario, BigDecimal> saldos = new HashMap<
          +>();                                                          
       90 +        Map<ItemAvaliado, ResultadoTeto> resultadosPorItem = n
          +ew IdentityHashMap<>();                                       
       91 +                                                              
       92 +        for (ItemAvaliado item : ordenadosPorIndice) {        
       93 +            String categoria = item.itemNormalizado().categori
          +aNormalizada();                                               
       94 +            ChaveTetoDiario chave = chaveDe(item);            
       95 +            TabelaCategoria configuracao = configuracoesPorIte
          +m.get(item);                                                  
       96 +            BigDecimal saldo = saldos.computeIfAbsent(chave, k
          + -> configuracao.limite());                                   
       97 +                                                              
       98 +            if (saldo.compareTo(BigDecimal.ZERO) == 0) {      
       99 +                resultadosPorItem.put(item,                   
      100 +                        new ResultadoTeto(item, ZERO_ESCALA_2,
          + Decisao.NAO_REEMBOLSADO_TETO_ESGOTADO, List.of(MOTIVO_ESGOTAD
          +O)));                                                         
      101 +                continue;                                     
      102 +            }                                                 
      103 +                                                              
      104 +            Motivo motivoAplicado = motivoTetoDiarioAplicado(c
          +ategoria);                                                    
      105 +            ResultadoTeto resultado = aplicarCorte(item, saldo
          +, motivoAplicado);                                            
      106 +            resultadosPorItem.put(item, resultado);           
      107 +                                                              
      108 +            saldos.put(chave, resultado.decisao() == Decisao.I
          +NTEGRALMENTE_REEMBOLSADO                                      
      109 +                    ? saldo.subtract(resultado.valorReembolsav
          +el())                                                         
      110 +                    : ZERO_ESCALA_2);                         
      111 +        }                                                     
      112 +                                                              
      113 +        List<ResultadoTeto> resultado = new ArrayList<>(aplica
          +veis.size());                                                 
      114 +        for (ItemAvaliado item : aplicaveis) {                
      115 +            resultado.add(resultadosPorItem.get(item));       
      116 +        }                                                     
      117 +        return List.copyOf(resultado);                        
      118 +    }                                                         
      119 +                                                              
      120 +    private static Motivo motivoTetoDiarioAplicado(String cate
          +goria) {                                                      
      121 +        if ("alimentacao".equals(categoria)) {                
      122 +            return MOTIVO_TETO_ALIMENTACAO;                   
      123 +        }                                                     
      124 +        if ("transporte_urbano".equals(categoria)) {          
      125 +            return MOTIVO_TETO_TRANSPORTE;                    
      126 +        }                                                     
      127 +        return MOTIVO_TETO_RN019;                             
      128 +    }                                                         
      129 +                                                              
      130      private List<ResultadoTeto> aplicarInterno(List<ItemAvalia
           do> itens) {
      131          List<ItemAvaliado> aplicaveis = new ArrayList<>();
      132          for (ItemAvaliado item : itens) {

● Now let's write the test file TetoPorPeriodicidadeTest.java.

● Write(tests\java\com\desafio\reembolso\pipeline\TetoPorPeriodicidadeTest.java)
  ⎿  Wrote 308 lines to tests\java\com\desafio\reembolso\pipeline\TetoPorPe
     riodicidadeTest.java
       1 package com.desafio.reembolso.pipeline;
       2 
       3 import com.desafio.reembolso.modelo.Decisao;
       4 import com.desafio.reembolso.modelo.ItemValidado;
       5 import com.desafio.reembolso.modelo.ItemValidado.Motivo;
       6 import com.desafio.reembolso.modelo.MotivoCodigo;
       7 import com.desafio.reembolso.modelo.Periodicidade;
       8 import com.desafio.reembolso.modelo.RegraNegocio;
       9 import com.desafio.reembolso.modelo.TabelaCategoria;
      10 import com.desafio.reembolso.modelo.TabelaPoliticaResolvida;
     … +424 lines (ctrl+o to expand)

● Now let's run the specific test, then the full suite.

  Ran 1 shell command (ctrl+o to expand)

● No output means the quiet Maven run passed. Let me confirm with an explicit
  check and then run the full suite.

  Ran 1 shell command (ctrl+o to expand)

● Exit code 0 confirms all tests passed. Now the full suite:

  Ran 1 shell command (ctrl+o to expand)

● Both commands passed cleanly. Let me get the exact test counts for the report
  by checking the Surefire reports.

  Searched for 1 pattern (ctrl+o to expand)

● T-043 implemented and verified.

  Resultados:
  - mvn -q test -Dtest=TetoPorPeriodicidadeTest — passou.
  - mvn -q test (suíte completa) — passou.
  - Testes em TetoPorPeriodicidadeTest: 9.
  - Total de testes na suíte completa: 583 (0 falhas, 0 erros, 0 pulados).

  Arquivos alterados/criados:
  - Alterado: C:\Users\bruno\Desktop\sdd-desafio\src\main\java\com\desafio\reemb
  olso\pipeline\AgregadorTetoDiario.java
  - Criado: C:\Users\bruno\Desktop\sdd-desafio\tests\java\com\desafio\reembolso\
  pipeline\TetoPorPeriodicidadeTest.java

✻ Cogitated for 4m 29s

> A T-043 foi revisada e aprovada.                                              
                                                                                
  Em:                                                                           
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  altere somente a T-043:                                                       
                                                                                
  - checkbox do título de [ ] para [x];                                         
  - Status de [ ] pendente para [x] concluída.                                  
                                                                                
  Não altere nenhuma outra task ou arquivo.                                     
  Não execute git add, commit ou push.                                          
● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1061  
      1062  ### Bloco H — Periodicidade e tetos
      1063  
      1064 -- [ ] **T-043** — Generalizar `AgregadorTetoDiario` por perio
           -dicidade                                                     
      1064 +- [x] **T-043** — Generalizar `AgregadorTetoDiario` por perio
           +dicidade                                                     
      1065    - **O que faz:** acrescenta a `AgregadorTetoDiario` uma nov
            a sobrecarga que recebe, por item, a `TabelaPoliticaResolvida
            ` aplicável, e decide participação no teto compartilhado cons
            ultando `periodicidade == DIA` na categoria resolvida — não m
            ais o `Set<String> CATEGORIAS_TETO_DIARIO` fixo (DT-017). O m
            otivo `TETO_DIARIO_APLICADO` carrega `RN_011` para `alimentac
            ao`, `RN_012` para `transporte_urbano`, `RN_019` para qualque
            r outra categoria (pequena tabela de exceção por nome, dentro
             do agregador — não contradiz a generalização do mecanismo). 
            A sobrecarga antiga (`Set` fixo + `PoliticaReembolso`) perman
            ece intacta para a suíte histórica.
      1066    - **RN atendidas:** RN-011, RN-012, RN-015, RN-019.
      1067    - **CA atendidos:** CA-047 (parcial — mecanismo compartilha
            do).

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1082      mvn -q test -Dtest=TetoPorPeriodicidadeTest
      1083      ```
      1084    - **Commit sugerido:** `feat(T-043): generaliza AgregadorTe
            toDiario por periodicidade`
      1085 -  - **Status:** [ ] pendente                                 
      1085 +  - **Status:** [x] concluída                                
      1086  
      1087  - [ ] **T-044** — Criar `AgregadorTetoIndividual`
      1088    - **O que faz:** cria `pipeline/AgregadorTetoIndividual.jav
            a`, que processa qualquer categoria com `periodicidade == DIA
            RIA` na tabela resolvida (não só `hospedagem`): teto individu
            al por lançamento, sem saldo compartilhado, reaproveitando `A
            gregadorTetoDiario.aplicarCorte(...)`. `hospedagem` produz `T
            ETO_HOSPEDAGEM_APLICADO`/`RN_013`; qualquer outra categoria p
            roduz `TETO_INDIVIDUAL_APLICADO`/`RN_019` (AMB-037). `Agregad
            orTetoHospedagem.java` **não é removido** nesta task — contin
            ua existindo e sendo usado pela suíte histórica até T-055/T-0
            56.

● T-043 marcada como [x] concluída no título e no campo Status, em
  specs/001-motor-reembolso/tasks.md. Nenhum outro arquivo ou task foi tocado;
  nenhum comando git foi executado.

✻ Cooked for 14s