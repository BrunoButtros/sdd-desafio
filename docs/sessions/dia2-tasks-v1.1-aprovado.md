 ▐▛███▜▌   Claude Code v2.1.222
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Vamos atualizar o planejamento de tarefas do Dia 2.                           
                                                                                
  A base normativa e técnica já está aprovada e versionada:                     
                                                                                
  - spec.md: versão 1.2, status aprovado;                                       
  - plan.md: versão 1.1, status aprovado;                                       
  - DECISIONS.md atualizado;                                                    
  - implementação anterior concluída até T-021;                                 
  - a numeração do Dia 2 deve continuar obrigatoriamente em T-022.              
                                                                                
  Nesta tarefa, altere exclusivamente:                                          
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Não altere:                                                                   
                                                                                
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - README.md;                                                                  
  - código de produção;                                                         
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Não execute /export, git add, commit ou push.                                 
                                                                                
  ## 1. Leitura obrigatória                                                     
                                                                                
  Leia integralmente:                                                           
                                                                                
  - CLAUDE.md;                                                                  
  - specs/001-motor-reembolso/spec.md;                                          
  - specs/001-motor-reembolso/plan.md;                                          
  - specs/001-motor-reembolso/DECISIONS.md;                                     
  - specs/001-motor-reembolso/tasks.md;                                         
  - exemplos/envelope/00-ENVELOPE-LACRADO.md;                                   
  - exemplos/envelope/politica-v4.json;                                         
  - exemplos/envelope/cambio.json;                                              
  - exemplos/envelope/despesas-envelope.json;                                   
  - exemplos/envelope/despesas-envelope-cc-desconhecido.json.                   
                                                                                
  Inspecione também todos os arquivos existentes em:                            
                                                                                
  - src/main/java;                                                              
  - tests/java;                                                                 
  - tests/resources.                                                            
                                                                                
  A inspeção serve para usar nomes e dependências reais. Não modifique          
  implementação ou testes.                                                      
                                                                                
  Antes de editar, informe brevemente:                                          
                                                                                
  1. versão e status atuais de tasks.md;                                        
  2. última task existente e seu estado;                                        
  3. quantidade total de tasks já concluídas;                                   
  4. componentes reais que serão afetados;                                      
  5. proposta inicial de blocos para T-022 em diante.                           
                                                                                
  Depois continue sem esperar nova confirmação.                                 
                                                                                
  ## 2. Cabeçalho                                                               
                                                                                
  Atualize tasks.md para:                                                       
                                                                                
  - próxima versão minor;                                                       
  - se estiver em 1.0, usar 1.1;                                                
  - status em revisão;                                                          
  - última alteração 2026-08-05;                                                
  - baseado na spec 1.2 aprovada;                                               
  - baseado no plan 1.1 aprovado.                                               
                                                                                
  Preserve integralmente o histórico de T-001 a T-021.                          
                                                                                
  Não renumere, reescreva ou marque novamente tarefas antigas.                  
                                                                                
  ## 3. Regras de decomposição                                                  
                                                                                
  Crie tarefas novas a partir de T-022, em sequência contínua.                  
                                                                                
  Cada task deve conter:                                                        
                                                                                
  - identificador;                                                              
  - título objetivo;                                                            
  - objetivo;                                                                   
  - arquivos previstos;                                                         
  - dependências;                                                               
  - passos de implementação;                                                    
  - testes obrigatórios;                                                        
  - critérios de conclusão;                                                     
  - referências a RN, CA, DT e seções relevantes;                               
  - comando de verificação;                                                     
  - sugestão de mensagem de commit.                                             
                                                                                
  Cada task deve ser pequena o suficiente para:                                 
                                                                                
  - ser implementada numa sessão independente do Claude;                        
  - ser revisada antes do commit;                                               
  - produzir um commit coerente;                                                
  - não misturar vários blocos arquiteturais grandes.                           
                                                                                
  Não crie tasks vagas como “implementar política                               
  ──── (194 lines hidden) ───────────────────────────────────────────────────── 
  cada commit deixe a suíte existente compilando e passando.                    
                                                                                
  Quando uma alteração intermediária quebraria compilação, agrupe os arquivos   
  mínimos necessários na mesma task e explique o motivo.                        
                                                                                
  ## 6. PolíticaReembolso e AgregadorTetoHospedagem                             
                                                                                
  Planeje explicitamente a remoção ou substituição de:                          
                                                                                
  - modelo/PoliticaReembolso.java;                                              
  - pipeline/AgregadorTetoHospedagem.java.                                      
                                                                                
  Não remova esses arquivos antes de todos os consumidores terem sido migrados. 
                                                                                
  A task deve indicar:                                                          
                                                                                
  1. migrar consumidores;                                                       
  2. executar testes;                                                           
  3. remover o componente superado;                                             
  4. verificar que não restam referências por grep.                             
                                                                                
  ## 7. Testes e rastreabilidade                                                
                                                                                
  Toda RN-019 a RN-022 e todo CA-024 a CA-049 precisam aparecer em pelo menos   
  uma task.                                                                     
                                                                                
  As tasks também devem preservar regressões relacionadas a RN-001 a RN-018 e   
  CA-001 a CA-023.                                                              
                                                                                
  Inclua uma matriz final:                                                      
                                                                                
  | RN/CA | Tasks responsáveis | Testes previstos |                             
                                                                                
  Não afirme cobertura total sem conferir identificador por identificador.      
                                                                                
  ## 8. Comandos de verificação                                                 
                                                                                
  Cada task deve informar comandos adequados, escolhidos entre:                 
                                                                                
  mvn -q -DskipTests compile                                                    
  mvn -q test                                                                   
  mvn -q -Dtest=NomeDoTeste test                                                
  mvn -q package                                                                
  java -jar target/motor-reembolso.jar calcular ...                             
  git grep "PoliticaReembolso"                                                  
  git grep "AgregadorTetoHospedagem"                                            
                                                                                
  Não use comandos inexistentes no ambiente.                                    
                                                                                
  ## 9. Estado inicial das tarefas                                              
                                                                                
  Todas as novas tasks devem ser criadas inicialmente como pendentes.           
                                                                                
  Não marque nenhuma task do Dia 2 como concluída ou em andamento.              
                                                                                
  Não implemente qualquer task nesta sessão.                                    
                                                                                
  ## 10. Verificação final                                                      
                                                                                
  Execute:                                                                      
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- README.md                                                         
  git diff -- src                                                               
  git diff -- tests                                                             
                                                                                
  Confirme:                                                                     
                                                                                
  - somente tasks.md foi alterado;                                              
  - T-001 a T-021 permanecem intactas;                                          
  - a primeira nova task é T-022;                                               
  - numeração contínua, sem duplicata ou lacuna;                                
  - todas as novas tasks estão pendentes;                                       
  - spec, plan, DECISIONS, README, código, testes e envelope permanecem         
  intactos;                                                                     
  - nenhum /export, git add, commit ou push foi executado.                      
                                                                                
  Ao final, informe:                                                            
                                                                                
  1. versão/status final de tasks.md;                                           
  2. intervalo completo das novas tasks;                                        
  3. quantidade de novas tasks;                                                 
  4. blocos e dependências;                                                     
  5. tasks que removem PoliticaReembolso e AgregadorTetoHospedagem;             
  6. cobertura de RN-019 a RN-022 e CA-024 a CA-049;                            
  7. git status --short.                                                        
                                                                                
  Pare para revisão.                                                            

  Read 4 files (ctrl+o to expand)

● Now let me inspect the envelope files and the actual codebase structure to use
  real names and dependencies.

  Read 5 files (ctrl+o to expand)

● Now let me inspect the actual source code and test structure to use real
  names/packages.

  Searched for 3 patterns, read 17 files (ctrl+o to expand)

● Now I have a complete picture. Before editing, here's the required summary:

  1. Estado atual de tasks.md: sem linha de versão própria; cabeçalho diz
  "Baseado em: spec 1.1 (aprovada) · plan 1.0 (aprovado)", 21 tasks totais.

  2. Última task e estado: T-021 (README com instruções de
  build/execução/testes), [x] concluída.

  3. Tasks concluídas: 21 de 21 (T-001 a T-021, todas [x]).

  4. Componentes reais afetados (verificados em
  src/main/java/com/desafio/reembolso/... e plan.md §19): criar
  leitor/LeitorPolitica.java, leitor/LeitorCambio.java,
  modelo/PoliticaExterna.java, modelo/TabelaCategoria.java,
  modelo/Periodicidade.java, modelo/TabelaCambio.java,
  modelo/TabelaPoliticaResolvida.java,
  pipeline/ResolutorPoliticaCentroCusto.java, pipeline/ResolutorCambio.java,
  pipeline/AgregadorTetoIndividual.java; substituir
  modelo/PoliticaReembolso.java e pipeline/AgregadorTetoHospedagem.java;
  estender Main.java, modelo/ItemValidado.java, pipeline/ValidadorItem.java,
  pipeline/Normalizador.java, pipeline/AvaliadorRegrasIndividuais.java,
  pipeline/DetectorDuplicidadeEconomica.java, pipeline/AgregadorTetoDiario.java,
  pipeline/CompositorSaida.java, escritor/EscritorResultado.java,
  modelo/MotivoCodigo.java, modelo/RegraNegocio.java, modelo/CampoCanonico.java.

  5. Proposta de blocos T-022→T-058 (37 tasks): A) vocabulário/modelos
  (T-022–T-029) · B) LeitorPolitica (T-030–T-031) · C) LeitorCambio
  (T-032–T-033) · D) CLI 4 flags (T-034–T-035) · E) campo moeda (T-036) · F)
  conversão cambial (T-037–T-039) · G) política por centro de custo
  (T-040–T-042) · H) tetos por periodicidade (T-043–T-046) · I)
  duplicidade/saída (T-047–T-049) · J) regressões/integração (T-050–T-054) · K)
  remoção de PoliticaReembolso/AgregadorTetoHospedagem + docs (T-055–T-058).

  Prosseguindo com a edição, sem esperar confirmação.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 4 lines, removed 2 lines
       6  > Marque `[x]` conforme conclui — ao longo do caminho, não tudo
           no fim. O histórico
       7  > de quando cada task foi marcada é lido na correção.
       8  
       9 -**Baseado em:** spec `1.1` (aprovada) · plan `1.0` (aprovado)  
      10 -**Total de tasks:** 21 (`T-001` a `T-021`) · **Estimativa princ
         -ipal de commits:** 22                                          
       9 +**Versão:** 1.1 · **Status:** em revisão · **Última alteração:*
         +* 2026-08-05                                                   
      10 +**Baseado em:** spec `1.2` (aprovada) · plan `1.1` (aprovado)  
      11 +**Total de tasks:** 58 (`T-001` a `T-058`) — `T-001` a `T-021` 
         +concluídas (Dia 1, spec 1.1/plan 1.0); `T-022` a `T-058` penden
         +tes (Dia 2, política v4, spec 1.2/plan 1.1).                   
      12 +**Estimativa de commits — Dia 1:** 22 (já realizados, ver histó
         +rico abaixo). **Dia 2:** 37 (uma task, um commit — nenhuma exce
         +ção nesta rodada).                                             
      13  
      14  ---
      15  

● Now the large insertion — replacing the Fase 8 placeholder with the full Dia 2
  task breakdown.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 865 lines, removed 2 lines
       495  
       496  ---
       497  
       498 -## Fase 8 — Envelope (Dia 2)                                 
       498 +## Fase 8 — Envelope do Dia 2 (política v4)                  
       499  
       500 -Nenhuma task desta fase é criada agora. Quando a mudança de r
           -equisito oficial do Dia 2 chegar, novas tasks continuam a num
           -eração a partir de **`T-022`** — a numeração nunca reinicia n
           -em renumeia `T-001` a `T-021`, porque a numeração é o eixo da
           - rastreabilidade. Nada do conteúdo dessa mudança é antecipado
           - ou suposto neste arquivo.                                   
       500 +Base normativa: `spec.md` `1.2` (aprovada) e `plan.md` `1.1` 
           +(aprovado), ambos já registrando RN-019 a RN-022 e as decisõe
           +s técnicas DT-011 a DT-019. Nenhuma regra de negócio é decidi
           +da aqui — cada task abaixo só decompõe em passos executáveis 
           +o que a spec e o plano já fecharam. Numeração contínua a part
           +ir de `T-022`, sem lacuna nem duplicata. Todas as tasks desta
           + fase nascem **pendentes**; nenhuma é implementada nesta sess
           +ão.                                                          
       501  
       502 +**Convenção de nomes usada abaixo** (verificada em `src/main/
           +java/com/desafio/reembolso/...` nesta sessão): pacotes `model
           +o`, `leitor`, `pipeline`, `escritor`; `Main.java` na raiz do 
           +pacote `com.desafio.reembolso`. Os testes espelham a mesma ár
           +vore sob `tests/java/com/desafio/reembolso/...`, com sufixo `
           +*Test` (Maven Surefire, DT-009).                             
       503 +                                                             
       504 +**Estratégia de compatibilidade adotada nesta fase (decisão d
           +e decomposição, não de negócio):** várias classes hoje têm as
           +sinatura pública fixa (`ItemValidado`, `AvaliadorRegrasIndivi
           +duais.avaliar(...)`, `AgregadorTetoDiario.aplicar(...)`) e sã
           +o consumidas por dezenas de testes já verdes (T-001 a T-021).
           + Para que **cada task deixe a suíte inteira compilando e pass
           +ando** (regra geral de sessão, topo deste arquivo) sem forçar
           + uma reescrita massiva de testes históricos num único commit,
           + as tasks que estendem essas classes preservam as assinaturas
           + antigas (sobrecarga, nunca remoção) e só acrescentam as nova
           +s — a migração dos consumidores para as APIs novas e a remoçã
           +o do que ficou superado (`PoliticaReembolso`, `AgregadorTetoH
           +ospedagem`) é o próprio Bloco K, no fim desta fase, conforme 
           +`CLAUDE.md` §6.                                              
       505 +                                                             
       506  ---
       507  
       508 +### Bloco A — Modelos e vocabulários fundamentais            
       509 +                                                             
       510 +- [ ] **T-022** — Estender `CampoCanonico` com `MOEDA`       
       511 +  - **O que faz:** acrescenta o valor `MOEDA` ao vocabulário 
           +fechado de campos canônicos, serializando para `"despesa.moed
           +a"` (mesmo mecanismo de `textoCanonico()` já existente — nome
           + do enum em minúsculo, prefixado por `despesa.`). Não altera 
           +a ordem de uso em `CompositorSaida` nesta task (isso é T-048)
           + — só adiciona o valor ao enum.                              
       512 +  - **RN atendidas:** RN-002 (7º campo do contrato).         
       513 +  - **CA atendidos:** base para CA-048 (fechado em T-036).   
       514 +  - **DT/seções do plan:** DT-019; plan §4 (`CampoCanonico` g
           +anha `MOEDA`), plan §10.                                     
       515 +  - **Dependências:** nenhuma (extensão isolada de enum já ex
           +istente, T-003).                                             
       516 +  - **Arquivos que cria/modifica:**                          
       517 +    - `src/main/java/com/desafio/reembolso/modelo/CampoCanoni
           +co.java`                                                     
       518 +    - `tests/java/com/desafio/reembolso/modelo/VocabularioMot
           +ivoTest.java`                                                
       519 +  - **Passos de implementação:**                             
       520 +    1. Adicionar `MOEDA` à enumeração, na posição que reflete
           + a ordem canônica de contrato (após `VALOR`, antes de `TEM_NO
           +TA_FISCAL` — a task de reordenar `ORDEM_CAMPO` em `Compositor
           +Saida` é T-048; aqui só o enum ganha o valor).               
       521 +    2. Nenhuma outra classe é tocada — o valor novo ainda não
           + é produzido por nenhum validador nesta task.                
       522 +  - **Testes obrigatórios:** `VocabularioMotivoTest` — parame
           +trizar (ou acrescentar caso) confirmando que `CampoCanonico.M
           +OEDA.textoCanonico()` é exatamente `"despesa.moeda"`.        
       523 +  - **Critério de conclusão:** teste novo verde; suíte comple
           +ta (`mvn test`) continua verde, porque nenhum código de produ
           +ção fora do enum foi alterado.                               
       524 +  - **Comando de verificação:**                              
       525 +    ```                                                      
       526 +    mvn -q test -Dtest=VocabularioMotivoTest                 
       527 +    ```                                                      
       528 +  - **Commit sugerido:** `feat(T-022): estende CampoCanonico 
           +com despesa.moeda`                                           
       529 +  - **Status:** [ ] pendente                                 
       530 +                                                             
       531 +- [ ] **T-023** — Estender `MotivoCodigo` com os três códigos
           + novos                                                       
       532 +  - **O que faz:** acrescenta `MOEDA_SEM_COTACAO`, `CATEGORIA
           +_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e `TETO_INDIVIDUAL_APLICADO` 
           +ao vocabulário fechado de códigos de motivo (spec 4.5), cada 
           +um serializando para o próprio nome do enum (mesmo mecanismo 
           +já existente).                                               
       533 +  - **RN atendidas:** RN-019, RN-020.                        
       534 +  - **CA atendidos:** base para CA-027, CA-030, CA-049 (fecha
           +dos nos blocos G/F/H).                                       
       535 +  - **DT/seções do plan:** DT-019; plan §10.                 
       536 +  - **Dependências:** nenhuma.                               
       537 +  - **Arquivos que cria/modifica:**                          
       538 +    - `src/main/java/com/desafio/reembolso/modelo/MotivoCodig
           +o.java`                                                      
       539 +    - `tests/java/com/desafio/reembolso/modelo/VocabularioMot
           +ivoTest.java`                                                
       540 +  - **Passos de implementação:**                             
       541 +    1. Adicionar os três valores ao enum, sem remover nem ren
           +omear nenhum dos treze já existentes.                        
       542 +    2. Não alterar `CompositorSaida` nesta task — a tabela de
           + estágios (`ESTAGIO_POR_CODIGO`) só ganha as entradas novas e
           +m T-048; até lá, esses códigos simplesmente não são produzido
           +s por nenhuma classe.                                        
       543 +  - **Testes obrigatórios:** `VocabularioMotivoTest` — cada u
           +m dos três novos valores serializa para o próprio nome (`"MOE
           +DA_SEM_COTACAO"`, `"CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO"`
           +, `"TETO_INDIVIDUAL_APLICADO"`).                             
       544 +  - **Critério de conclusão:** teste novo verde; `mvn test` c
           +ompleto continua verde.                                      
       545 +  - **Comando de verificação:**                              
       546 +    ```                                                      
       547 +    mvn -q test -Dtest=VocabularioMotivoTest                 
       548 +    ```                                                      
       549 +  - **Commit sugerido:** `feat(T-023): estende MotivoCodigo c
           +om os tres codigos da politica v4`                           
       550 +  - **Status:** [ ] pendente                                 
       551 +                                                             
       552 +- [ ] **T-024** — Estender `RegraNegocio` com RN-019 a RN-022
       553 +  - **O que faz:** acrescenta `RN_019`, `RN_020`, `RN_021`, `
           +RN_022` ao vocabulário fechado de regras, cada uma serializan
           +do para `"RN-NNN"` (mesmo mecanismo já existente: `name().rep
           +lace('_', '-')`).                                            
       554 +  - **RN atendidas:** RN-019 a RN-022 (o próprio vocabulário 
           +que as representa).                                          
       555 +  - **CA atendidos:** base para todos os CA do Dia 2 que cita
           +m essas RN em `motivo.regra`.                                
       556 +  - **DT/seções do plan:** DT-019; plan §10.                 
       557 +  - **Dependências:** nenhuma.                               
       558 +  - **Arquivos que cria/modifica:**                          
       559 +    - `src/main/java/com/desafio/reembolso/modelo/RegraNegoci
           +o.java`                                                      
       560 +    - `tests/java/com/desafio/reembolso/modelo/VocabularioMot
           +ivoTest.java`                                                
       561 +  - **Passos de implementação:**                             
       562 +    1. Adicionar os quatro valores ao enum, na sequência `RN_
           +019, RN_020, RN_021, RN_022`, sem tocar os dezoito já existen
           +tes.                                                         
       563 +  - **Testes obrigatórios:** `VocabularioMotivoTest` — os qua
           +tro novos valores serializam para `"RN-019"`, `"RN-020"`, `"R
           +N-021"`, `"RN-022"`.                                         
       564 +  - **Critério de conclusão:** teste novo verde; `mvn test` c
           +ompleto continua verde.                                      
       565 +  - **Comando de verificação:**                              
       566 +    ```                                                      
       567 +    mvn -q test -Dtest=VocabularioMotivoTest                 
       568 +    ```                                                      
       569 +  - **Commit sugerido:** `feat(T-024): estende RegraNegocio c
           +om RN-019 a RN-022`                                          
       570 +  - **Status:** [ ] pendente                                 
       571 +                                                             
       572 +- [ ] **T-025** — Criar `Periodicidade` e `TabelaCategoria`  
       573 +  - **O que faz:** cria o enum fechado `Periodicidade` (`DIA`
           +, `DIARIA` — AMB-036) e a estrutura imutável `TabelaCategoria
           +` (`limite: BigDecimal`, `periodicidade: Periodicidade`), que
           + representa uma categoria dentro de uma tabela de política (`
           +padrao` ou de um centro de custo). Agrupadas na mesma task po
           +r serem pequenas e fortemente acopladas: `TabelaCategoria` nã
           +o existe sem `Periodicidade`.                                
       574 +  - **RN atendidas:** RN-019.                                
       575 +  - **CA atendidos:** base estrutural para CA-024 a CA-027, C
           +A-045, CA-047, CA-049.                                       
       576 +  - **DT/seções do plan:** DT-011; plan §5 (modelo `TabelaCat
           +egoria`).                                                    
       577 +  - **Dependências:** nenhuma.                               
       578 +  - **Arquivos que cria/modifica:**                          
       579 +    - `src/main/java/com/desafio/reembolso/modelo/Periodicida
           +de.java`                                                     
       580 +    - `src/main/java/com/desafio/reembolso/modelo/TabelaCateg
           +oria.java`                                                   
       581 +    - `tests/java/com/desafio/reembolso/modelo/TabelaCategori
           +aTest.java`                                                  
       582 +  - **Passos de implementação:**                             
       583 +    1. `Periodicidade`: enum com dois valores, `DIA` e `DIARI
           +A` — sem serialização JSON própria (é consumido internamente,
           + nunca escrito na saída).                                    
       584 +    2. `TabelaCategoria`: `record` ou classe imutável com `li
           +mite` (`BigDecimal`) e `periodicidade` (`Periodicidade`), val
           +idando no construtor que nenhum dos dois é nulo.             
       585 +  - **Testes obrigatórios:** `TabelaCategoriaTest` — construç
           +ão válida preserva os dois campos; `limite` ou `periodicidade
           +` nulos lançam `NullPointerException` (falha rápida, sem esta
           +do parcial).                                                 
       586 +  - **Critério de conclusão:** teste novo verde; nenhuma outr
           +a classe referencia `TabelaCategoria` ainda (isso começa em T
           +-026).                                                       
       587 +  - **Comando de verificação:**                              
       588 +    ```                                                      
       589 +    mvn -q test -Dtest=TabelaCategoriaTest                   
       590 +    ```                                                      
       591 +  - **Commit sugerido:** `feat(T-025): cria Periodicidade e T
           +abelaCategoria`                                              
       592 +  - **Status:** [ ] pendente                                 
       593 +                                                             
       594 +- [ ] **T-026** — Criar `PoliticaExterna`                    
       595 +  - **O que faz:** cria a estrutura imutável `PoliticaExterna
           +` (`vigencia: LocalDate`, `moedaBase: String`, `notaFiscalObr
           +igatoriaAcimaDe: BigDecimal`, `padrao: Map<String, TabelaCate
           +goria>`, `centrosCusto: Map<String, Map<String, TabelaCategor
           +ia>>`), construída sempre com cópias defensivas imutáveis dos
           + dois mapas (`Map.copyOf`). Nesta task o modelo é só a estrut
           +ura de dados — a leitura e validação a partir de `politica.js
           +on` é `LeitorPolitica` (T-030).                              
       596 +  - **RN atendidas:** RN-019, RN-021.                        
       597 +  - **CA atendidos:** base estrutural para CA-024 a CA-027, C
           +A-035, CA-045.                                               
       598 +  - **DT/seções do plan:** DT-011; plan §5.                  
       599 +  - **Dependências:** T-025 (`TabelaCategoria`).             
       600 +  - **Arquivos que cria/modifica:**                          
       601 +    - `src/main/java/com/desafio/reembolso/modelo/PoliticaExt
           +erna.java`                                                   
       602 +    - `tests/java/com/desafio/reembolso/modelo/PoliticaExtern
           +aTest.java`                                                  
       603 +  - **Passos de implementação:**                             
       604 +    1. Construtor recebe os cinco campos e aplica `Map.copyOf
           +` em `padrao` e em cada valor de `centrosCusto` (mapa de mapa
           +s — cada tabela interna também precisa ser copiada defensivam
           +ente, não só o mapa externo).                                
       605 +    2. Getters simples, sem lógica de resolução (isso é `Reso
           +lutorPoliticaCentroCusto`, T-040).                           
       606 +  - **Testes obrigatórios:** `PoliticaExternaTest` — construç
           +ão preserva os cinco campos; tentar modificar `padrao` ou uma
           + tabela de `centrosCusto` obtida via getter lança `Unsupporte
           +dOperationException` (garante DT-011, "nenhuma referência mut
           +ável escapa").                                               
       607 +  - **Critério de conclusão:** teste novo verde; `mvn test` c
           +ompleto continua verde.                                      
       608 +  - **Comando de verificação:**                              
       609 +    ```                                                      
       610 +    mvn -q test -Dtest=PoliticaExternaTest                   
       611 +    ```                                                      
       612 +  - **Commit sugerido:** `feat(T-026): cria modelo imutavel P
           +oliticaExterna`                                              
       613 +  - **Status:** [ ] pendente                                 
       614 +                                                             
       615 +- [ ] **T-027** — Criar `TabelaCambio`                       
       616 +  - **O que faz:** cria a estrutura imutável `TabelaCambio` (
           +`moedaBase: String`, `cotacoesPorMoeda: Map<String, Navigable
           +Map<LocalDate, BigDecimal>>`), já na forma invertida de consu
           +lta eficiente (moeda → data → taxa, DT-013). Nesta task o mod
           +elo é só a estrutura — a leitura/inversão a partir de `cambio
           +.json` é `LeitorCambio` (T-032).                             
       617 +  - **RN atendidas:** RN-020.                                
       618 +  - **CA atendidos:** base estrutural para CA-029, CA-030, CA
           +-046.                                                        
       619 +  - **DT/seções do plan:** DT-013; plan §7.                  
       620 +  - **Dependências:** nenhuma.                               
       621 +  - **Arquivos que cria/modifica:**                          
       622 +    - `src/main/java/com/desafio/reembolso/modelo/TabelaCambi
           +o.java`                                                      
       623 +    - `tests/java/com/desafio/reembolso/modelo/TabelaCambioTe
           +st.java`                                                     
       624 +  - **Passos de implementação:**                             
       625 +    1. Construtor recebe `moedaBase` e `Map<String, Navigable
           +Map<LocalDate, BigDecimal>>`, copiando defensivamente o mapa 
           +externo e cada `NavigableMap` interno para uma implementação 
           +imutável (ex.: `Collections.unmodifiableNavigableMap` sobre u
           +ma cópia em `TreeMap`).                                      
       626 +    2. Método de consulta `cotacaoEm(String moeda, LocalDate 
           +data): BigDecimal` (ou `Optional<BigDecimal>`) usando `Naviga
           +bleMap.floorEntry(data)` — devolve vazio/nulo quando a moeda 
           +não existe no mapa ou quando `floorEntry` não encontra entrad
           +a (nenhuma cotação anterior ou igual disponível).            
       627 +  - **Testes obrigatórios:** `TabelaCambioTest` — cotação exa
           +ta; fallback para a mais recente anterior; ausência de cotaçã
           +o anterior utilizável (moeda nunca cotada, ou só cotada em da
           +ta posterior) devolve vazio; imutabilidade dos mapas internos
           + e externo.                                                  
       628 +  - **Critério de conclusão:** teste novo verde.             
       629 +  - **Comando de verificação:**                              
       630 +    ```                                                      
       631 +    mvn -q test -Dtest=TabelaCambioTest                      
       632 +    ```                                                      
       633 +  - **Commit sugerido:** `feat(T-027): cria TabelaCambio com 
           +consulta por floorEntry`                                     
       634 +  - **Status:** [ ] pendente                                 
       635 +                                                             
       636 +- [ ] **T-028** — Criar `TabelaPoliticaResolvida`            
       637 +  - **O que faz:** cria a estrutura imutável `TabelaPoliticaR
           +esolvida` (`categorias: Map<String, TabelaCategoria>`, `orige
           +m: Origem` — enum interno `PADRAO`/`CENTRO_CUSTO` —, `nomeCen
           +troCusto: String`, nulo quando `origem == PADRAO`). É o tipo 
           +de retorno de `ResolutorPoliticaCentroCusto.resolver(...)` (T
           +-040) — nesta task só a estrutura.                           
       638 +  - **RN atendidas:** RN-019.                                
       639 +  - **CA atendidos:** base estrutural para CA-024 a CA-027.  
       640 +  - **DT/seções do plan:** DT-011; plan §4, §6.              
       641 +  - **Dependências:** T-025 (`TabelaCategoria`).             
       642 +  - **Arquivos que cria/modifica:**                          
       643 +    - `src/main/java/com/desafio/reembolso/modelo/TabelaPolit
           +icaResolvida.java`                                           
       644 +    - `tests/java/com/desafio/reembolso/modelo/TabelaPolitica
           +ResolvidaTest.java`                                          
       645 +  - **Passos de implementação:**                             
       646 +    1. Enum interno `Origem { PADRAO, CENTRO_CUSTO }`.       
       647 +    2. Construtor valida: `origem == CENTRO_CUSTO` exige `nom
           +eCentroCusto` não nulo; `origem == PADRAO` exige `nomeCentroC
           +usto == null` — falha rápida (`IllegalArgumentException`) em 
           +qualquer combinação inconsistente, porque essa invariante nun
           +ca deve ser violada por quem constrói o objeto (T-040).      
       648 +    3. `categorias` copiado defensivamente (`Map.copyOf`).   
       649 +  - **Testes obrigatórios:** `TabelaPoliticaResolvidaTest` — 
           +construção válida para `PADRAO` (sem nome) e para `CENTRO_CUS
           +TO` (com nome); as duas combinações inconsistentes lançam `Il
           +legalArgumentException`; `categorias` é imutável.            
       650 +  - **Critério de conclusão:** teste novo verde.             
       651 +  - **Comando de verificação:**                              
       652 +    ```                                                      
       653 +    mvn -q test -Dtest=TabelaPoliticaResolvidaTest           
       654 +    ```                                                      
       655 +  - **Commit sugerido:** `feat(T-028): cria TabelaPoliticaRes
           +olvida`                                                      
       656 +  - **Status:** [ ] pendente                                 
       657 +                                                             
       658 +- [ ] **T-029** — Estender `ItemValidado` com campos de moeda
           + e câmbio                                                    
       659 +  - **O que faz:** `ItemValidado` ganha quatro campos novos (
           +plan §4, §9): `moeda` (populado por `ValidadorItem`, T-036), 
           +`taxaCambioAplicada`, `dataCotacaoUtilizada`, `valorConvertid
           +oBruto` (estes três, e só estes três, populados por `Resoluto
           +rCambio`, T-037). Para não quebrar `ValidadorItem` nem os ~20
           + arquivos de teste que hoje constroem `ItemValidado` pelo con
           +strutor de dez argumentos, o construtor **antigo é preservado
           +** e passa a delegar para um construtor novo de catorze argum
           +entos, assumindo `moeda = "BRL"`, `taxaCambioAplicada = BigDe
           +cimal.ONE`, `dataCotacaoUtilizada = null` e `valorConvertidoB
           +ruto = valor` (cópia do próprio parâmetro `valor` recebido) —
           + exatamente o comportamento correto para um item BRL sem conv
           +ersão (spec 4.3, "BRL: taxa 1, data nula"), então nenhum test
           +e histórico muda de resultado.                               
       660 +  - **RN atendidas:** RN-002 (campo `moeda`), RN-020 (campos 
           +de câmbio).                                                  
       661 +  - **CA atendidos:** base estrutural para CA-034, CA-048.   
       662 +  - **DT/seções do plan:** DT-014, DT-015; plan §4 (`ItemVali
           +dado` enriquecido pelo `ResolutorCambio`), §9.               
       663 +  - **Dependências:** nenhuma (extensão de classe já existent
           +e desde T-005).                                              
       664 +  - **Arquivos que cria/modifica:**                          
       665 +    - `src/main/java/com/desafio/reembolso/modelo/ItemValidad
           +o.java`                                                      
       666 +    - `tests/java/com/desafio/reembolso/modelo/ItemValidadoCa
           +mbioTest.java`                                               
       667 +  - **Passos de implementação:**                             
       668 +    1. Adicionar os quatro campos privados finais e os getter
           +s correspondentes (`getMoeda()`, `getTaxaCambioAplicada()`, `
           +getDataCotacaoUtilizada()`, `getValorConvertidoBruto()`).    
       669 +    2. Criar o construtor de catorze argumentos (os dez atuai
           +s + os quatro novos).                                        
       670 +    3. Reescrever o construtor de dez argumentos para delegar
           + (`this(...)`) no de catorze, com os valores padrão de BRL de
           +scritos acima.                                               
       671 +    4. Não alterar `ValidadorItem` nesta task — ele continua 
           +chamando o construtor de dez argumentos até T-036.           
       672 +  - **Testes obrigatórios:** `ItemValidadoCambioTest` — const
           +rutor de dez argumentos produz `moeda = "BRL"`, `taxaCambioAp
           +licada = 1`, `dataCotacaoUtilizada = null`, `valorConvertidoB
           +ruto` igual ao `valor` recebido (inclusive quando `valor` é `
           +null`, caso em que `valorConvertidoBruto` também é `null`); c
           +onstrutor de catorze argumentos preserva exatamente os quatro
           + valores recebidos.                                          
       673 +  - **Critério de conclusão:** teste novo verde; `mvn test` c
           +ompleto continua 100% verde sem nenhuma outra classe tocada (
           +confirma que a compatibilidade funciona).                    
       674 +  - **Comando de verificação:**                              
       675 +    ```                                                      
       676 +    mvn -q test                                              
       677 +    ```                                                      
       678 +  - **Commit sugerido:** `feat(T-029): estende ItemValidado c
           +om campos de cambio preservando compatibilidade`             
       679 +  - **Status:** [ ] pendente                                 
       680 +                                                             
       681 +---                                                          
       682 +                                                             
       683 +### Bloco B — Leitor de política externa                     
       684 +                                                             
       685 +- [ ] **T-030** — Implementar `LeitorPolitica.ler(Path)`     
       686 +  - **O que faz:** lê e valida integralmente `politica.json` 
           +(spec 4.1.1, RN-021, RN-022, AMB-035), aplicando as dezesseis
           + validações estruturais de `plan.md` §5 antes de construir qu
           +alquer `TabelaCategoria`. Sucesso devolve `PoliticaExterna`; 
           +qualquer falha (arquivo inexistente, ilegível, JSON sintatica
           +mente inválido, ou violação de qualquer uma das dezesseis reg
           +ras) lança `PoliticaInvalidaException` — classe estática anin
           +hada em `LeitorPolitica`, no mesmo padrão já usado por `Valid
           +adorEnvelope.EnvelopeInvalidoException` (respondendo à pergun
           +ta de `CLAUDE.md`: o padrão atual do projeto já é o de exceçã
           +o aninhada com `codigoSaida()`, então `PoliticaInvalidaExcept
           +ion` segue esse mesmo molde, com `CODIGO_SAIDA = 2`).        
       687 +  - **RN atendidas:** RN-021, RN-022.                        
       688 +  - **CA atendidos:** CA-035 (parcial — vigência), CA-036 (pa
           +rcial — política), CA-045.                                   
       689 +  - **DT/seções do plan:** DT-012; plan §5.                  
       690 +  - **Dependências:** T-026 (`PoliticaExterna`), T-025 (`Tabe
           +laCategoria`, `Periodicidade`).                              
       691 +  - **Arquivos que cria/modifica:**                          
       692 +    - `src/main/java/com/desafio/reembolso/leitor/LeitorPolit
           +ica.java`                                                    
       693 +    - `tests/java/com/desafio/reembolso/leitor/LeitorPolitica
           +Test.java`                                                   
       694 +    - `tests/resources/fixtures/politica-valida-teste.json` (
           +fixture mínima válida, para o caso de sucesso)               
       695 +  - **Passos de implementação:**                             
       696 +    1. `ler(Path caminho): PoliticaExterna` — abre o arquivo,
           + faz parsing sintático com `ObjectMapper` (`USE_BIG_DECIMAL_F
           +OR_FLOATS`, mesma técnica de DT-005), captura `IOException`/`
           +JsonProcessingException` e relança como `PoliticaInvalidaExce
           +ption`.                                                      
       697 +    2. Validar, nesta ordem, os pontos 1 a 13 de `plan.md` §5
           + (raiz objeto; `vigencia` obrigatória/formato/data real; `moe
           +da_base` exatamente `"BRL"`; `nota_fiscal_obrigatoria_acima_d
           +e` obrigatório/numérico/não negativo; `padrao`/`centros_custo
           +` obrigatórios e objetos; cada tabela de `centros_custo` é ob
           +jeto; nome de categoria não vazio; configuração de categoria 
           +é objeto; `limite` obrigatório/numérico, estritamente `> 0` e
           +m `padrao` e `>= 0` em `centros_custo`; `periodicidade` obrig
           +atória e exatamente `"dia"`/`"diaria"`).                     
       698 +    3. Construir `PoliticaExterna` só depois de toda a valida
           +ção passar — nenhuma `TabelaCategoria` é criada antes da vali
           +dação completa terminar.                                     
       699 +    4. Campos desconhecidos na raiz (`versao`, `acrescimo_em_
           +viagem_percentual`) e dentro de uma configuração de categoria
           + (além de `limite`/`periodicidade`/`observacao`) são lidos e 
           +ignorados, sem invalidar o arquivo (pontos 14 item "campos ve
           +rdadeiramente desconhecidos"/15 de `plan.md` §5 — a validação
           + completa de `observacao` fica em T-031).                    
       700 +  - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo i
           +nexistente; arquivo ilegível; JSON sintaticamente inválido; r
           +aiz não objeto; `vigencia` ausente/tipo errado/formato errado
           +/data inexistente; `moeda_base` diferente de `"BRL"`; `padrao
           +` ausente ou não objeto; `centros_custo` ausente ou não objet
           +o; `nota_fiscal_obrigatoria_acima_de` ausente/negativo; categ
           +oria com `limite` ausente ou não numérico; `periodicidade` fo
           +ra de `"dia"`/`"diaria"`; um arquivo que satisfaz integralmen
           +te o contrato é aceito e produz o `PoliticaExterna` esperado 
           +(CA-045).                                                    
       701 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; nenhuma `TabelaCategoria` construída num arquivo rejeitado 
           +(verificável indiretamente pela exceção lançada antes de qual
           +quer retorno parcial).                                       
       702 +  - **Comando de verificação:**                              
       703 +    ```                                                      
       704 +    mvn -q test -Dtest=LeitorPoliticaTest                    
       705 +    ```                                                      
       706 +  - **Commit sugerido:** `feat(T-030): implementa LeitorPolit
           +ica com validacao estrutural completa`                       
       707 +  - **Status:** [ ] pendente                                 
       708 +                                                             
       709 +- [ ] **T-031** — Testes de fronteira do `LeitorPolitica`    
       710 +  - **O que faz:** completa a cobertura de `plan.md` §5 que T
           +-030 deixou em aberto: `observacao` opcional dentro de uma co
           +nfiguração de categoria (texto válido é lido e descartado do 
           +modelo; qualquer outro tipo invalida o arquivo), `limite: 0` 
           +em `padrao` (inválido, arquivo inteiro rejeitado) versus `lim
           +ite: 0` numa tabela de `centros_custo` (estruturalmente válid
           +o, produz `TabelaCategoria` com `limite = 0.00`), e a garanti
           +a de imutabilidade/isolamento de `PoliticaExterna` a partir d
           +o `JsonNode`/mapas mutáveis intermediários do leitor (ponto 1
           +6 de `plan.md` §5).                                          
       711 +  - **RN atendidas:** RN-019, RN-022.                        
       712 +  - **CA atendidos:** CA-045.                                
       713 +  - **DT/seções do plan:** DT-011, DT-012; plan §5, pontos 14
           + e 16.                                                       
       714 +  - **Dependências:** T-030.                                 
       715 +  - **Arquivos que cria/modifica:**                          
       716 +    - `tests/java/com/desafio/reembolso/leitor/LeitorPolitica
           +Test.java` (mesma classe de T-030, casos adicionais)         
       717 +  - **Passos de implementação:**                             
       718 +    1. Sem alteração de produção esperada nesta task — se alg
           +um dos cenários abaixo revelar um defeito real em `LeitorPoli
           +tica`, corrigir `LeitorPolitica.java` **nesta mesma task**, a
           +ntes do commit (mesma contingência de T-015: um defeito encon
           +trado na cobertura de fronteira não vira uma task nova).     
       719 +    2. Escrever os casos de teste listados abaixo.           
       720 +  - **Testes obrigatórios:** `LeitorPoliticaTest` — `observac
           +ao` ausente (válido); `observacao` como texto (válido, e o va
           +lor não aparece em nenhum campo de `TabelaCategoria`); `obser
           +vacao` como número/booleano/lista (arquivo inválido); `limite
           +: 0` em `padrao` (arquivo inválido — CA-045 negativo); `limit
           +e: 0` numa tabela de `centros_custo` (arquivo válido, `Tabela
           +Categoria.getLimite() == 0.00`); campo desconhecido dentro de
           + uma configuração de categoria (ignorado, arquivo válido); um
           + `PoliticaExterna` construído a partir de um arquivo válido t
           +em `padrao`/`centrosCusto` imutáveis (tentativa de modificaçã
           +o via getter lança `UnsupportedOperationException`).         
       721 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; `mvn test` completo verde.                                 
       722 +  - **Comando de verificação:**                              
       723 +    ```                                                      
       724 +    mvn -q test -Dtest=LeitorPoliticaTest                    
       725 +    ```                                                      
       726 +  - **Commit sugerido:** `test(T-031): cobre observacao, limi
           +te zero e imutabilidade do LeitorPolitica`                   
       727 +  - **Status:** [ ] pendente                                 
       728 +                                                             
       729 +---                                                          
       730 +                                                             
       731 +### Bloco C — Leitor de câmbio externo                       
       732 +                                                             
       733 +- [ ] **T-032** — Implementar `LeitorCambio.ler(Path)`       
       734 +  - **O que faz:** lê e valida integralmente `cambio.json` (s
           +pec 4.1.1, RN-020, RN-022, AMB-035), invertendo a estrutura `
           +data → moeda → taxa` para `moeda → NavigableMap<data, taxa>` 
           +(DT-013) na própria leitura. Sucesso devolve `TabelaCambio`; 
           +qualquer falha lança `CambioInvalidoException` (aninhada em `
           +LeitorCambio`, mesmo padrão de `EnvelopeInvalidoException`/`P
           +oliticaInvalidaException`, `CODIGO_SAIDA = 2`).              
       735 +  - **RN atendidas:** RN-020, RN-022.                        
       736 +  - **CA atendidos:** CA-036 (parcial — câmbio), CA-046.     
       737 +  - **DT/seções do plan:** DT-013; plan §7.                  
       738 +  - **Dependências:** T-027 (`TabelaCambio`).                
       739 +  - **Arquivos que cria/modifica:**                          
       740 +    - `src/main/java/com/desafio/reembolso/leitor/LeitorCambi
           +o.java`                                                      
       741 +    - `tests/java/com/desafio/reembolso/leitor/LeitorCambioTe
           +st.java`                                                     
       742 +    - `tests/resources/fixtures/cambio-valido-teste.json`    
       743 +  - **Passos de implementação:**                             
       744 +    1. `ler(Path caminho): TabelaCambio` — parsing sintático 
           +(mesma técnica de T-030), erro de I/O ou JSON malformado vira
           + `CambioInvalidoException`.                                  
       745 +    2. Validar os pontos 1 a 9 de `plan.md` §7: raiz objeto; 
           +`moeda_base` exatamente `"BRL"`; `taxas` obrigatório e objeto
           + (pode ser `{}`); cada chave de primeiro nível de `taxas` é d
           +ata `AAAA-MM-DD` real; valor associado é objeto; cada chave i
           +nterna casa com `[A-Z]{3}`; cada taxa é numérica e estritamen
           +te positiva; `fonte`/`observacao` opcionais e, quando present
           +es, texto.                                                   
       746 +    3. Construir a estrutura invertida (`Map<String, Navigabl
           +eMap<LocalDate, BigDecimal>>`) só depois da validação complet
           +a — percorrer `taxas` uma vez para validar, e (na mesma passa
           +da ou numa segunda) popular o mapa invertido.                
       747 +    4. Devolver `TabelaCambio` com cópias defensivas (reaprov
           +eita o construtor de T-027).                                 
       748 +  - **Testes obrigatórios:** `LeitorCambioTest` — arquivo ine
           +xistente/ilegível/JSON inválido; raiz não objeto; `moeda_base
           +` errada; `taxas` ausente ou não objeto; `taxas: {}` (válido)
           +; data malformada como chave de primeiro nível; moeda fora de
           + `[A-Z]{3}` como chave interna; taxa zero ou negativa; um arq
           +uivo que satisfaz integralmente o contrato é aceito e a consu
           +lta invertida devolve os valores esperados (CA-046).         
       749 +  - **Critério de conclusão:** todos os cenários acima verdes
           +.                                                            
       750 +  - **Comando de verificação:**                              
       751 +    ```                                                      
       752 +    mvn -q test -Dtest=LeitorCambioTest                      
       753 +    ```                                                      
       754 +  - **Commit sugerido:** `feat(T-032): implementa LeitorCambi
           +o com inversao para consulta por data`                       
       755 +  - **Status:** [ ] pendente                                 
       756 +                                                             
       757 +- [ ] **T-033** — Testes de fronteira do `LeitorCambio`      
       758 +  - **O que faz:** completa a cobertura de `plan.md` §7, pont
           +o 9: `fonte`/`observacao` presentes com tipo não textual inva
           +lidam o arquivo (são campos **conhecidos**, não "desconhecido
           +s"); uma chave malformada dentro de `taxas` — data fora de `A
           +AAA-MM-DD` ou moeda fora de `[A-Z]{3}` — **não** é tratada co
           +mo campo desconhecido, e invalida o arquivo inteiro, mesmo qu
           +e a tolerância a campos desconhecidos valha na raiz; e a gara
           +ntia de imutabilidade da estrutura invertida.                
       759 +  - **RN atendidas:** RN-020, RN-022.                        
       760 +  - **CA atendidos:** CA-046.                                
       761 +  - **DT/seções do plan:** DT-013; plan §7, ponto 9.         
       762 +  - **Dependências:** T-032.                                 
       763 +  - **Arquivos que cria/modifica:**                          
       764 +    - `tests/java/com/desafio/reembolso/leitor/LeitorCambioTe
           +st.java` (mesma classe de T-032, casos adicionais)           
       765 +  - **Passos de implementação:**                             
       766 +    1. Sem alteração de produção esperada — mesma contingênci
           +a de T-015 se um defeito real aparecer.                      
       767 +    2. Escrever os casos de teste listados abaixo.           
       768 +  - **Testes obrigatórios:** `LeitorCambioTest` — `fonte`/`ob
           +servacao` ausentes (válido); presentes como texto (válido, va
           +lor descartado); presentes como número/booleano/lista (arquiv
           +o inválido); campo desconhecido na raiz, ao lado de `moeda_ba
           +se`/`fonte`/`observacao`/`taxas` (ignorado, arquivo válido); 
           +chave de data malformada dentro de `taxas` (ex.: `"2026/07/13
           +"`) — arquivo inválido, não um "campo desconhecido"; chave de
           + moeda malformada dentro de uma data (ex.: `"US"` ou `"usd"`)
           + — arquivo inválido; a `TabelaCambio` resultante de um arquiv
           +o válido tem o mapa externo e cada `NavigableMap` interno imu
           +táveis.                                                      
       769 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; `mvn test` completo verde.                                 
       770 +  - **Comando de verificação:**                              
       771 +    ```                                                      
       772 +    mvn -q test -Dtest=LeitorCambioTest                      
       773 +    ```                                                      
       774 +  - **Commit sugerido:** `test(T-033): cobre fonte/observacao
           +, chaves malformadas e imutabilidade do LeitorCambio`        
       775 +  - **Status:** [ ] pendente                                 
       776 +                                                             
       777 +---                                                          
       778 +                                                             
       779 +### Bloco D — CLI de quatro flags                            
       780 +                                                             
       781 +- [ ] **T-034** — `Main.java`: parser de quatro flags        
       782 +  - **O que faz:** reescreve o parser de argumentos de `Main.
           +run(...)` conforme `plan.md` §3/DT-018: primeiro token deve s
           +er exatamente `"calcular"` (único posicional aceito); tokens 
           +restantes consumidos aos pares `flag valor`; pares acumulados
           + num `Map<String, String>` contando ocorrências por chave; ao
           + final, valida que as quatro chaves `--input`/`--output`/`--p
           +olitica`/`--cambio` estão presentes, cada uma exatamente uma 
           +vez, e que nenhuma chave desconhecida apareceu. Qualquer viol
           +ação → exit `2`. **Não** carrega `--politica`/`--cambio` aind
           +a (isso é T-035) — nesta task, as duas flags só são reconheci
           +das e armazenadas.                                           
       783 +  - **RN atendidas:** RN-022 (parcial — contrato de execução)
           +.                                                            
       784 +  - **CA atendidos:** CA-041, CA-042.                        
       785 +  - **DT/seções do plan:** DT-003 (extensão), DT-018; plan §3
           +.                                                            
       786 +  - **Dependências:** nenhuma (`Main.java` já existe desde T-
           +002/T-019).                                                  
       787 +  - **Arquivos que cria/modifica:**                          
       788 +    - `src/main/java/com/desafio/reembolso/Main.java`        
       789 +    - `tests/java/com/desafio/reembolso/CliContratoTest.java`
       790 +  - **Passos de implementação:**                             
       791 +    1. Substituir o `switch` de dois casos por: (a) checagem 
           +do primeiro token == `"calcular"`, com exit `2` se ausente, d
           +iferente, ou se sobrar um token posicional depois das flags; 
           +(b) laço que consome os tokens restantes aos pares — quantida
           +de ímpar, ou uma flag como último token, é exit `2`.         
       792 +    2. Popular um `Map<String, String>` com os pares reconhec
           +idos; uma chave repetida ou uma flag fora do conjunto `{--inp
           +ut, --output, --politica, --cambio}` é exit `2`.             
       793 +    3. Depois de percorrer todos os pares, verificar que as q
           +uatro chaves obrigatórias estão presentes — qualquer ausência
           + é exit `2`.                                                 
       794 +    4. Extrair `inputPath`/`outputPath`/`politicaPath`/`cambi
           +oPath` do mapa; **manter o restante do método exatamente como
           + está** (leitura de `--input`, validação de envelope, pipelin
           +e, escrita) — `politicaPath`/`cambioPath` ficam sem uso nesta
           + task (variável local não referenciada além da extração, ou p
           +assada adiante sem efeito ainda).                            
       795 +  - **Testes obrigatórios:** `CliContratoTest` — sucesso com 
           +as quatro flags em ordem arbitrária (ainda sem `LeitorPolitic
           +a`/`LeitorCambio` reais entrando no caminho de erro — resulta
           +do de negócio é irrelevante aqui, só o exit code); subcomando
           + ausente; subcomando diferente de `calcular`; token posiciona
           +l extra depois de `calcular`; flag sem valor (última posição)
           +; quantidade ímpar de tokens; flag repetida; flag desconhecid
           +a; cada uma das quatro flags obrigatórias faltando isoladamen
           +te; comando antigo só com `--input`/`--output` (exit `2`, por
           +que faltam `--politica`/`--cambio`).                         
       796 +  - **Critério de conclusão:** todos os cenários acima com o 
           +exit code esperado; nenhuma escrita em `--output` nos cenário
           +s de exit `2`.                                               
       797 +  - **Comando de verificação:**                              
       798 +    ```                                                      
       799 +    mvn -q test -Dtest=CliContratoTest                       
       800 +    ```                                                      
       801 +  - **Commit sugerido:** `feat(T-034): reescreve parser da CL
           +I para quatro flags obrigatorias`                            
       802 +  - **Status:** [ ] pendente                                 
       803 +                                                             
       804 +- [ ] **T-035** — `Main.java`: carregar e validar política e 
           +câmbio antes do envelope                                     
       805 +  - **O que faz:** `Main.run(...)` passa a chamar `LeitorPoli
           +tica.ler(politicaPath)` e `LeitorCambio.ler(cambioPath)` **an
           +tes** de ler e validar o envelope de despesas (spec 8.1, pass
           +o 1 antes do passo 2) — qualquer `PoliticaInvalidaException`/
           +`CambioInvalidoException` retorna exit `2`, sem sequer abrir 
           +o arquivo de entrada. Os objetos `PoliticaExterna`/`TabelaCam
           +bio` resultantes ainda não são usados pelo pipeline de regras
           + nesta task (isso começa em T-038/T-042/T-046) — aqui o objet
           +ivo é só o contrato de execução (AMB-034) e a ordem de valida
           +ção.                                                         
       806 +  - **RN atendidas:** RN-021, RN-022.                        
       807 +  - **CA atendidos:** CA-043, CA-044.                        
       808 +  - **DT/seções do plan:** DT-003 (extensão), DT-010 (extensã
           +o); plan §3.                                                 
       809 +  - **Dependências:** T-030 (`LeitorPolitica`), T-032 (`Leito
           +rCambio`), T-034 (flags reconhecidas).                       
       810 +  - **Arquivos que cria/modifica:**                          
       811 +    - `src/main/java/com/desafio/reembolso/Main.java`        
       812 +    - `tests/java/com/desafio/reembolso/CliContratoTest.java`
       813 +    - `tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTe
           +st.java`                                                     
       814 +  - **Passos de implementação:**                             
       815 +    1. Logo após extrair os quatro caminhos (T-034) e antes d
           +e qualquer leitura do arquivo de entrada, chamar `LeitorPolit
           +ica.ler(Path.of(politicaPath))` e `LeitorCambio.ler(Path.of(c
           +ambioPath))` dentro de blocos `try/catch` que traduzem a exce
           +ção dedicada em `err.println(...)` + `return 2`.             
       816 +    2. Preservar o restante do fluxo inalterado (leitura de `
           +--input`, `ValidadorEnvelope`, pipeline, escrita atômica) — a
           + ordem "política/câmbio antes do envelope" é garantida só pel
           +a posição das duas chamadas novas no método.                 
       817 +  - **Testes obrigatórios:** `CliContratoTest` — `--politica`
           + apontando para arquivo inexistente/ilegível/JSON inválido/es
           +truturalmente inválido → exit `2`, mesmo com `--input` perfei
           +tamente válido; mesmo conjunto de cenários para `--cambio`; u
           +m `--output` preexistente permanece intacto byte a byte em qu
           +alquer um desses cenários (estendendo `EscritaAtomicaSaidaTes
           +t` ou o próprio `CliContratoTest`, verificando conteúdo antes
           +/depois); stdout vazio e mensagem em stderr em todos os casos
           + de falha.                                                   
       818 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; `mvn test` completo verde.                                 
       819 +  - **Comando de verificação:**                              
       820 +    ```                                                      
       821 +    mvn -q test "-Dtest=CliContratoTest,EscritaAtomicaSaidaTe
           +st"                                                          
       822 +    ```                                                      
       823 +  - **Commit sugerido:** `feat(T-035): carrega politica e cam
           +bio antes do envelope na CLI`                                
       824 +  - **Status:** [ ] pendente                                 
       825 +                                                             
       826 +---                                                          
       827 +                                                             
       828 +### Bloco E — Campo `despesa.moeda`                          
       829 +                                                             
       830 +- [ ] **T-036** — `validarMoeda` em `ValidadorItem`          
       831 +  - **O que faz:** implementa a validação do sétimo campo do 
           +contrato (spec 4.2, RN-002, DT-014): chave `moeda` ausente do
           + objeto → `"BRL"`, sem motivo; chave presente com valor `null
           +` → `CAMPO_AUSENTE`; tipo não textual → `CAMPO_TIPO_INVALIDO`
           +; texto fora de `[A-Z]{3}` → `CAMPO_FORMATO_INVALIDO` (sem tr
           +im, sem conversão de caixa). `ValidadorItem.validarItem(...)`
           + passa a chamar `validarMoeda` e a usar o construtor de cator
           +ze argumentos de `ItemValidado` (T-029) para popular `moeda`,
           + mantendo `taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorC
           +onvertidoBruto` com os valores padrão de BRL até `ResolutorCa
           +mbio` (T-037) os recalcular.                                 
       832 +  - **RN atendidas:** RN-002 (7º campo).                     
       833 +  - **CA atendidos:** CA-048.                                
       834 +  - **DT/seções do plan:** DT-014; plan §8.                  
       835 +  - **Dependências:** T-022 (`CampoCanonico.MOEDA`), T-029 (c
           +onstrutor de catorze argumentos).                            
       836 +  - **Arquivos que cria/modifica:**                          
       837 +    - `src/main/java/com/desafio/reembolso/pipeline/Validador
           +Item.java`                                                   
       838 +    - `tests/java/com/desafio/reembolso/pipeline/CampoMoedaTe
           +st.java`                                                     
       839 +  - **Passos de implementação:**                             
       840 +    1. Método privado `validarMoeda(JsonNode elemento, List<M
           +otivo> motivos): String`, seguindo exatamente a ordem de `if`
           + de `plan.md` §8: `elemento.get("moeda") == null` (referência
           + Java nula, chave ausente) → devolve `"BRL"` sem tocar `motiv
           +os`; `valor.isNull()` → adiciona `CAMPO_AUSENTE`/`CampoCanoni
           +co.MOEDA`, devolve `null`; tipo != `STRING` → `CAMPO_TIPO_INV
           +ALIDO`; texto que não casa com o padrão `[A-Z]{3}` (regex, se
           +m `trim()`/`toUpperCase()`) → `CAMPO_FORMATO_INVALIDO`; caso 
           +contrário devolve o texto como recebido.                     
       841 +    2. Chamar `validarMoeda` dentro de `validarItem`, na posi
           +ção correspondente ao 7º campo (entre `valor` e `tem_nota_fis
           +cal`), e passar o resultado para o construtor de catorze argu
           +mentos de `ItemValidado` (os três campos de câmbio continuam 
           +com os valores padrão de BRL descritos em T-029 até T-037 os 
           +substituir).                                                 
       842 +    3. Não alterar a ordem em que os motivos estruturais são 
           +adicionados à lista além da inserção de `moeda` na posição co
           +rreta — isso é o que faz a ordem canônica de contrato já refl
           +etir o 7º campo (a reordenação de apresentação em `Compositor
           +Saida` é T-048).                                             
       843 +  - **Testes obrigatórios:** `CampoMoedaTest` — os quatro cen
           +ários fechados (ausência de chave → `"BRL"`, sem motivo; `nul
           +l` explícito → `CAMPO_AUSENTE`; tipo não textual → `CAMPO_TIP
           +O_INVALIDO`; formato fora de `[A-Z]{3}`, incluindo caixa baix
           +a `"usd"` → `CAMPO_FORMATO_INVALIDO`, sem correção automática
           + de caixa).                                                  
       844 +  - **Critério de conclusão:** os quatro cenários verdes (CA-
           +048); `mvn test` completo continua verde — nenhum teste histó
           +rico que constrói despesas sem o campo `moeda` muda de result
           +ado, porque a ausência da chave já resolvia para o comportame
           +nto BRL implícito em T-029.                                  
       845 +  - **Comando de verificação:**                              
       846 +    ```                                                      
       847 +    mvn -q test -Dtest=CampoMoedaTest                        
       848 +    ```                                                      
       849 +  - **Commit sugerido:** `feat(T-036): implementa validarMoed
           +a no ValidadorItem`                                          
       850 +  - **Status:** [ ] pendente                                 
       851 +                                                             
       852 +---                                                          
       853 +                                                             
       854 +### Bloco F — Conversão cambial                              
       855 +                                                             
       856 +- [ ] **T-037** — Implementar `ResolutorCambio`              
       857 +  - **O que faz:** cria o novo estágio de pipeline `Resolutor
           +Cambio` (spec 8.1, passo 5; plan §9), que consome `ItemValida
           +do.getMoeda()` (já resolvido por `ValidadorItem`, T-036) e um
           +a `TabelaCambio`, e devolve um novo `ItemValidado` com `taxaC
           +ambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto` 
           +recalculados — **sem arredondar** `valorConvertidoBruto` (DT-
           +015, o arredondamento é responsabilidade exclusiva do `Normal
           +izador`, T-038). BRL: taxa `1`, data nula, `valorConvertidoBr
           +uto` igual ao valor original. Moeda estrangeira com cotação r
           +esolvida: `valorConvertidoBruto = valor × taxa` (produto exat
           +o, sem `setScale`). Sem cotação utilizável: os três campos fi
           +cam nulos e o motivo `MOEDA_SEM_COTACAO` (`campo = CampoCanon
           +ico.MOEDA`) é acrescentado aos motivos do item.              
       858 +  - **RN atendidas:** RN-020.                                
       859 +  - **CA atendidos:** CA-029, CA-030.                        
       860 +  - **DT/seções do plan:** DT-013, DT-015; plan §9.          
       861 +  - **Dependências:** T-023 (`MotivoCodigo.MOEDA_SEM_COTACAO`
           +), T-024 (`RegraNegocio.RN_020`), T-027 (`TabelaCambio`), T-0
           +29 (`ItemValidado` de catorze argumentos), T-036 (`moeda` já 
           +populado).                                                   
       862 +  - **Arquivos que cria/modifica:**                          
       863 +    - `src/main/java/com/desafio/reembolso/pipeline/Resolutor
           +Cambio.java`                                                 
       864 +    - `tests/java/com/desafio/reembolso/pipeline/ResolucaoCam
           +bioTest.java`                                                
       865 +  - **Passos de implementação:**                             
       866 +    1. `resolver(ItemValidado item, TabelaCambio cambio): Ite
           +mValidado` — se `item.getMoeda() == null` (moeda estruturalme
           +nte inválida, já recusada por RN-002), devolve o item sem ten
           +tar resolver câmbio (exclusão de dependência de campo inválid
           +o, spec 8.4 item 3).                                         
       867 +    2. Se `"BRL".equals(item.getMoeda())`, devolve uma cópia 
           +do item com `taxaCambioAplicada = BigDecimal.ONE`, `dataCotac
           +aoUtilizada = null`, `valorConvertidoBruto = item.getValor()`
           + — mesmo caminho para BRL informado e para BRL assumido por a
           +usência de chave (não há `if` separando os dois casos, porque
           + `ValidadorItem` já unificou os dois em `"BRL"`).            
       868 +    3. Caso contrário, consulta `cambio.cotacaoEm(item.getMoe
           +da(), item.getData())` (T-027); se presente, `valorConvertido
           +Bruto = item.getValor().multiply(taxa)` (sem `setScale`), `ta
           +xaCambioAplicada = taxa`, `dataCotacaoUtilizada = dataDaCotac
           +aoEfetivamenteUsada`; se ausente, os três campos ficam nulos 
           +e o motivo `MOEDA_SEM_COTACAO` é acrescentado à lista de moti
           +vos do item (nova lista, item permanece imutável).           
       869 +    4. `resolverLista(List<ItemValidado> itens, TabelaCambio 
           +cambio): List<ItemValidado>` — aplica `resolver` a cada item,
           + preservando ordem.                                          
       870 +    5. Nenhuma chamada a `Main.java` ainda — o wiring no pipe
           +line é T-038.                                                
       871 +  - **Testes obrigatórios:** `ResolucaoCambioTest` — BRL info
           +rmado e BRL por ausência de chave (idêntico); cotação exata n
           +a data da despesa; fallback para a cotação mais recente anter
           +ior (CA-029, ex.: EUR de sábado sem cotação própria usa a de 
           +sexta); proibição de cotação futura (data cuja única cotação 
           +disponível é posterior — resultado é `MOEDA_SEM_COTACAO`, nun
           +ca a cotação futura); moeda nunca presente em `cambio.json` →
           + `MOEDA_SEM_COTACAO` (CA-030); o motivo `MOEDA_SEM_COTACAO` c
           +arrega `campo = CampoCanonico.MOEDA`; `valorConvertidoBruto` 
           +preserva a escala plena do produto, sem arredondamento — veri
           +ficado diretamente comparando o `BigDecimal` resultante de `1
           +.005 × 1.005` contra o literal exato `"1.010025"` (não contra
           + um valor já arredondado), o que isola a garantia de "nenhum 
           +arredondamento neste estágio" da garantia de "um único arredo
           +ndamento no fim", que só T-038 fecha.                        
       872 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; nenhuma chamada a `setScale`/`round` em `ResolutorCambio.ja
           +va` (confirmável por leitura, já que não há teste automatizad
           +o que prove ausência de arredondamento em todos os caminhos p
           +ossíveis — DT-015).                                          
       873 +  - **Comando de verificação:**                              
       874 +    ```                                                      
       875 +    mvn -q test -Dtest=ResolucaoCambioTest                   
       876 +    ```                                                      
       877 +  - **Commit sugerido:** `feat(T-037): implementa ResolutorCa
           +mbio sem arredondamento`                                     
       878 +  - **Status:** [ ] pendente                                 
       879 +                                                             
       880 +- [ ] **T-038** — `Normalizador` sobre `valorConvertidoBruto`
           + + wiring da conversão no pipeline                           
       881 +  - **O que faz:** `Normalizador` passa a normalizar `item.ge
           +tValorConvertidoBruto()` em vez de `item.getValor()` diretame
           +nte — mesmo caminho para BRL e moeda estrangeira, sem `if` de
           + BRL dentro do `Normalizador` (plan §9). `Main.executarPipeli
           +ne` ganha o estágio `ResolutorCambio.resolverLista(...)`, ins
           +erido entre `DetectorIdDuplicado.detectar(...)` e `Normalizad
           +or.normalizarLista(...)` (spec 8.1, passo 5), usando a `Tabel
           +aCambio` já carregada em T-035. Esta task fecha, de ponta a p
           +onta, a garantia de arredondamento único (RN-004/RN-020) e a 
           +comparação do gatilho de nota fiscal pelo valor convertido (R
           +N-009 já lê `item.valorNormalizado()`, que passa a ser o valo
           +r convertido sem nenhuma mudança na própria `AvaliadorRegrasI
           +ndividuais`).                                                
       882 +  - **RN atendidas:** RN-004 (atualizada), RN-009 (atualizada
           +), RN-016 (extensão a moeda), RN-020.                        
       883 +  - **CA atendidos:** CA-028, CA-031, CA-032.                
       884 +  - **DT/seções do plan:** DT-004 (extensão), DT-015; plan §9
           +, §14.                                                       
       885 +  - **Dependências:** T-035 (`TabelaCambio` carregada no `Mai
           +n`), T-037 (`ResolutorCambio`).                              
       886 +  - **Arquivos que cria/modifica:**                          
       887 +    - `src/main/java/com/desafio/reembolso/pipeline/Normaliza
           +dor.java`                                                    
       888 +    - `src/main/java/com/desafio/reembolso/Main.java`        
       889 +    - `tests/java/com/desafio/reembolso/pipeline/ConversaoCam
           +bialIntegracaoTest.java`                                     
       890 +    - `tests/java/com/desafio/reembolso/pipeline/RegraViagemE
           +feitoNuloTest.java` (estendido)                              
       891 +  - **Passos de implementação:**                             
       892 +    1. Em `Normalizador.normalizarValor(...)`, trocar a leitu
           +ra de `item.getValor()` por `item.getValorConvertidoBruto()` 
           +como entrada do `setScale(2, RoundingMode.HALF_UP)` — único p
           +onto de arredondamento de todo o pipeline (DT-015).          
       893 +    2. Em `Main.executarPipeline`, inserir `List<ItemValidado
           +> comCambio = ResolutorCambio.resolverLista(idsVerificados, t
           +abelaCambio);` entre a chamada a `DetectorIdDuplicado.detecta
           +r(...)` e `Normalizador.normalizarLista(...)`, e passar `comC
           +ambio` (não mais `idsVerificados`) ao normalizador.          
       894 +    3. `tabelaCambio` precisa estar acessível em `executarPip
           +eline` — repassar como parâmetro do método (assinatura muda d
           +e `executarPipeline(Envelope envelope)` para `executarPipelin
           +e(Envelope envelope, TabelaCambio cambio)`, com a instância o
           +btida em T-035).                                             
       895 +  - **Testes obrigatórios:** `ConversaoCambialIntegracaoTest`
           + — teste-canário real de ordem de arredondamento (`valor = 1.
           +005`, `taxa = 1.005`, esperado `1,01` via pipeline completo `
           +ResolutorCambio` + `Normalizador`, nunca `1,02`); cenário nor
           +mativo USD `40,00 × 5,50 = 220,00` (CA-031); nota fiscal apli
           +cada sobre o valor convertido — USD `40,00` (abaixo de R$100 
           +na moeda original) convertido para `220,00`, sem nota fiscal,
           + recusado com `NOTA_FISCAL_AUSENTE` (CA-032). `RegraViagemEfe
           +itoNuloTest` (estendido) — um item elegível em moeda estrange
           +ira não amplia teto algum nem afeta outros itens do mesmo dia
           +/período; trocar a moeda de um item de `BRL` para `EUR` (com 
           +cotação válida) não altera o comportamento de RN-016 (CA-028)
           +.                                                            
       896 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; `mvn test` completo verde, incluindo toda a suíte histórica
           + de T-001 a T-021 (regressão: para despesas BRL, `valorConver
           +tidoBruto == valor`, então o resultado normalizado não muda).
       897 +  - **Comando de verificação:**                              
       898 +    ```                                                      
       899 +    mvn -q test                                              
       900 +    ```                                                      
       901 +  - **Commit sugerido:** `feat(T-038): normaliza sobre valorC
           +onvertidoBruto e integra ResolutorCambio no pipeline`        
       902 +  - **Status:** [ ] pendente                                 
       903 +                                                             
       904 +- [ ] **T-039** — `MoedaSemCotacaoTest` — coexistência de mot
           +ivos (8.4, item 14)                                          
       905 +  - **O que faz:** confirma, com teste dedicado, a exclusão d
           +e dependência declarada em `spec.md` 8.4 item 14: um item com
           + `MOEDA_SEM_COTACAO` pode coexistir com `CATEGORIA_FORA_POLIT
           +ICA`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COM
           +PETENCIA` (que não dependem de `valor_normalizado`), mas **nu
           +nca** com `VALOR_NAO_POSITIVO`, `NOTA_FISCAL_AUSENTE`, `DUPLI
           +CIDADE` ou qualquer motivo de teto (que dependem). Comportame
           +nto já decorre da checagem `valorNormalizado != null` já exis
           +tente em `AvaliadorRegrasIndividuais` (T-008/T-011) — esta ta
           +sk é predominantemente de verificação, sem produção nova espe
           +rada (mesma contingência de T-015: se um cenário revelar um d
           +efeito real, corrigir nesta mesma task, antes do commit `test
           +(T-039)`, e nesse caso o commit passa a `fix(T-039)` seguido 
           +de `test(T-039)`, como já autorizado para T-015).            
       906 +  - **RN atendidas:** RN-020.                                
       907 +  - **CA atendidos:** base de rastreabilidade para o item 14 
           +de 8.4 (não tem CA numerado próprio — é comportamento de comp
           +osição, já coberto indiretamente por CA-030 e pelos cenários 
           +de `§12.3`/`§12.4`).                                         
       908 +  - **DT/seções do plan:** plan §2 (exclusão nova, item 14 da
           + lista de 8.4), §9.                                          
       909 +  - **Dependências:** T-038 (pipeline de câmbio completo).   
       910 +  - **Arquivos que cria/modifica:**                          
       911 +    - `tests/java/com/desafio/reembolso/pipeline/MoedaSemCota
           +caoTest.java`                                                
       912 +  - **Passos de implementação:**                             
       913 +    1. Escrever os cenários abaixo, executando o pipeline até
           + `AvaliadorRegrasIndividuais` (ainda sem a `TabelaPoliticaRes
           +olvida` do Bloco G — usar as sobrecargas históricas de `Avali
           +adorRegrasIndividuais`, que continuam existindo por compatibi
           +lidade até T-041/T-055, e adaptar o teste quando as sobrecarg
           +as novas estiverem disponíveis, se necessário revisar em T-04
           +1).                                                          
       914 +  - **Testes obrigatórios:** `MoedaSemCotacaoTest` — moeda es
           +trangeira sem cotação, categoria fora da política → ambos os 
           +motivos presentes; moeda estrangeira sem cotação, data fora d
           +a competência → ambos presentes; moeda estrangeira sem cotaçã
           +o, valor que seria não positivo se calculável → **apenas** `M
           +OEDA_SEM_COTACAO` (não `VALOR_NAO_POSITIVO`, porque `valorNor
           +malizado` é nulo); moeda estrangeira sem cotação, item que te
           +ria excedido o gatilho de nota fiscal se convertido → **apena
           +s** `MOEDA_SEM_COTACAO` (não `NOTA_FISCAL_AUSENTE`).         
       915 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; `mvn test` completo verde.                                 
       916 +  - **Comando de verificação:**                              
       917 +    ```                                                      
       918 +    mvn -q test -Dtest=MoedaSemCotacaoTest                   
       919 +    ```                                                      
       920 +  - **Commit sugerido:** `test(T-039): comprova coexistencia 
           +e exclusao de motivos com MOEDA_SEM_COTACAO`                 
       921 +  - **Status:** [ ] pendente                                 
       922 +                                                             
       923 +---                                                          
       924 +                                                             
       925 +### Bloco G — Política por centro de custo                   
       926 +                                                             
       927 +- [ ] **T-040** — Implementar `ResolutorPoliticaCentroCusto` 
       928 +  - **O que faz:** cria `ResolutorPoliticaCentroCusto.resolve
           +r(String centroCusto, PoliticaExterna politica): TabelaPoliti
           +caResolvida` (RN-019, DT-011, DT-016): `centroCusto == null` 
           +(já representando ausência/nulo/tipo inválido, reduzidos pela
           + camada de envelope) ou não presente em `politica.centrosCust
           +o` → resolve para `padrao`; presente → resolve exclusivamente
           + para a tabela daquele centro. Comparação textual exata (`Map
           +.get`, sem `trim`/`toLowerCase`/normalização de acento) — nun
           +ca a união das duas tabelas.                                 
       929 +  - **RN atendidas:** RN-019.                                
       930 +  - **CA atendidos:** CA-024, CA-025, CA-026, CA-027.        
       931 +  - **DT/seções do plan:** DT-011, DT-016; plan §6.          
       932 +  - **Dependências:** T-026 (`PoliticaExterna`), T-028 (`Tabe
           +laPoliticaResolvida`).                                       
       933 +  - **Arquivos que cria/modifica:**                          
       934 +    - `src/main/java/com/desafio/reembolso/pipeline/Resolutor
           +PoliticaCentroCusto.java`                                    
       935 +    - `tests/java/com/desafio/reembolso/pipeline/ResolutorPol
           +iticaCentroCustoTest.java`                                   
       936 +  - **Passos de implementação:**                             
       937 +    1. `centroCusto == null` → devolve `TabelaPoliticaResolvi
           +da` com `origem = PADRAO`, `categorias = politica.getPadrao()
           +`, `nomeCentroCusto = null`.                                 
       938 +    2. `politica.getCentrosCusto().get(centroCusto)` — se pre
           +sente, devolve `origem = CENTRO_CUSTO`, `categorias` = a tabe
           +la exclusiva daquele centro, `nomeCentroCusto = centroCusto`;
           + se ausente (centro desconhecido), mesmo caminho do passo 1 (
           +`padrao`).                                                   
       939 +    3. Nenhuma lógica de comparação aproximada, trim ou norma
           +lização — `Map.get` puro.                                    
       940 +  - **Testes obrigatórios:** `ResolutorPoliticaCentroCustoTes
           +t` — centro cadastrado (usa exclusivamente a tabela dele); ce
           +ntro desconhecido (usa `padrao`); `centroCusto == null` (usa 
           +`padrao`); comparação sensível a caixa (`"CC-COMERCIAL"` != `
           +"cc-comercial"`, o segundo cai em `padrao`); categoria ausent
           +e da tabela de um centro cadastrado **não** recebe fallback p
           +ara `padrao` mesmo quando `padrao` a declara (CA-025); `repre
           +sentacao` reembolsável só onde a tabela aplicável a declara (
           +CA-026); categoria com `limite == 0` numa tabela de centro ca
           +dastrado é preservada em `categorias` (a decisão de recusar p
           +or limite zero é de `AvaliadorRegrasIndividuais`, T-041 — aqu
           +i só se confirma que `TabelaCategoria` chega intacta ao resol
           +vedor de regras, CA-027).                                    
       941 +  - **Critério de conclusão:** todos os cenários acima verdes
           +.                                                            
       942 +  - **Comando de verificação:**                              
       943 +    ```                                                      
       944 +    mvn -q test -Dtest=ResolutorPoliticaCentroCustoTest      
       945 +    ```                                                      
       946 +  - **Commit sugerido:** `feat(T-040): implementa ResolutorPo
           +liticaCentroCusto`                                           
       947 +  - **Status:** [ ] pendente                                 
       948 +                                                             
       949 +- [ ] **T-041** — `AvaliadorRegrasIndividuais` consome políti
           +ca externa                                                   
       950 +  - **O que faz:** acrescenta a `AvaliadorRegrasIndividuais` 
           +uma nova sobrecarga que recebe `TabelaPoliticaResolvida` e `P
           +oliticaExterna` (para o gatilho de nota fiscal, RN-009 atuali
           +zada) e implementa RN-019: categoria ausente da tabela aplicá
           +vel → `CATEGORIA_FORA_POLITICA` quando `origem == PADRAO`, `C
           +ATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` quando `origem == CEN
           +TRO_CUSTO`; categoria presente com `limite == 0` → `CATEGORIA
           +_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (nunca `PARCIALMENTE_REEMBOLS
           +ADO`). As sobrecargas antigas (`avaliar(item)`, `avaliar(item
           +, envelope)`, com `Set<String>` fixo e `PoliticaReembolso`) *
           +*permanecem intactas**, porque a suíte histórica (T-006 a T-0
           +21) as usa — a migração delas para as novas é T-055.         
       951 +  - **RN atendidas:** RN-019, RN-009 (atualizada — gatilho de
           + `PoliticaExterna`, não de `PoliticaReembolso`).             
       952 +  - **CA atendidos:** CA-024 a CA-027 (uso real dentro do ava
           +liador de regras).                                           
       953 +  - **DT/seções do plan:** DT-011; plan §6, §19.             
       954 +  - **Dependências:** T-023 (`CATEGORIA_NAO_REEMBOLSAVEL_CENT
           +RO_CUSTO`), T-024 (`RN_019`), T-040 (`ResolutorPoliticaCentro
           +Custo`).                                                     
       955 +  - **Arquivos que cria/modifica:**                          
       956 +    - `src/main/java/com/desafio/reembolso/pipeline/Avaliador
           +RegrasIndividuais.java`                                      
       957 +    - `tests/java/com/desafio/reembolso/pipeline/CategoriaCen
           +troCustoTest.java`                                           
       958 +  - **Passos de implementação:**                             
       959 +    1. Nova sobrecarga `avaliar(ItemNormalizado item, Envelop
           +e envelope, TabelaPoliticaResolvida tabela, PoliticaExterna p
           +olitica): ItemAvaliado` e a correspondente `avaliarLista(...)
           +`.                                                           
       960 +    2. Dentro dela, substituir a checagem de `CATEGORIAS_REEM
           +BOLSAVEIS.contains(...)` por: se `item.categoriaNormalizada()
           +` ausente de `tabela.getCategorias()`, escolher o motivo conf
           +orme `tabela.getOrigem()`; se presente com `TabelaCategoria.g
           +etLimite().compareTo(BigDecimal.ZERO) == 0`, `CATEGORIA_NAO_R
           +EEMBOLSAVEL_CENTRO_CUSTO` (só pode ocorrer com `origem == CEN
           +TRO_CUSTO`, porque `padrao` com limite zero já foi rejeitado 
           +na leitura, T-030).                                          
       961 +    3. Substituir `politica.getGatilhoNotaFiscal()` (o `Polit
           +icaReembolso` fixo) por `politica.getNotaFiscalObrigatoriaAci
           +maDe()` (o `PoliticaExterna` recebido) na avaliação de RN-009
           +, dentro desta nova sobrecarga.                              
       962 +    4. Manter `avaliarRn006ERn007` (valor não positivo) inalt
           +erado — não depende de política.                             
       963 +  - **Testes obrigatórios:** `CategoriaCentroCustoTest` — os 
           +quatro cenários de CA-024 a CA-027 exercitados diretamente co
           +ntra `AvaliadorRegrasIndividuais` (não só contra `ResolutorPo
           +liticaCentroCusto` isoladamente): centro desconhecido usa `pa
           +drao` e produz `CATEGORIA_FORA_POLITICA` para categoria ausen
           +te; centro cadastrado sem a categoria produz `CATEGORIA_NAO_R
           +EEMBOLSAVEL_CENTRO_CUSTO`; `limite == 0` num centro cadastrad
           +o produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` com `valor
           +_reembolsavel` `0,00` (nunca parcial); gatilho de nota fiscal
           + lido de `PoliticaExterna.getNotaFiscalObrigatoriaAcimaDe()`,
           + não de `PoliticaReembolso`.                                 
       964 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; `mvn test` completo verde (sobrecargas antigas intocadas, s
           +uíte histórica passa sem alteração).                         
       965 +  - **Comando de verificação:**                              
       966 +    ```                                                      
       967 +    mvn -q test -Dtest=CategoriaCentroCustoTest              
       968 +    ```                                                      
       969 +  - **Commit sugerido:** `feat(T-041): AvaliadorRegrasIndivid
           +uais aplica RN-019 com politica externa`                     
       970 +  - **Status:** [ ] pendente                                 
       971 +                                                             
       972 +- [ ] **T-042** — Wiring da política externa no `Main`       
       973 +  - **O que faz:** `Main.executarPipeline` passa a resolver, 
           +por item, a `TabelaPoliticaResolvida` a partir de `envelope.g
           +etColaboradorCentroCusto()` e da `PoliticaExterna` carregada 
           +em T-035, e a chamar a nova sobrecarga de `AvaliadorRegrasInd
           +ividuais` (T-041) em vez da antiga. Agregação de tetos (Bloco
           + H) ainda usa `PoliticaReembolso` nesta task — só a elegibili
           +dade de categoria e o gatilho de nota fiscal passam a vir da 
           +política externa real.                                       
       974 +  - **RN atendidas:** RN-019, RN-009 (atualizada).           
       975 +  - **CA atendidos:** confirma CA-024 a CA-027 ponta a ponta 
           +via CLI.                                                     
       976 +  - **DT/seções do plan:** plan §2 (passo 7), §6.            
       977 +  - **Dependências:** T-038 (câmbio já integrado ao pipeline)
           +, T-041 (nova sobrecarga do avaliador).                      
       978 +  - **Arquivos que cria/modifica:**                          
       979 +    - `src/main/java/com/desafio/reembolso/Main.java`        
       980 +    - `tests/java/com/desafio/reembolso/CliContratoTest.java`
       981 +  - **Passos de implementação:**                             
       982 +    1. `Main.executarPipeline` ganha o parâmetro `PoliticaExt
           +erna politica` (junto com `TabelaCambio cambio`, já adicionad
           +o em T-038).                                                 
       983 +    2. Para cada `ItemNormalizado`, resolver `TabelaPoliticaR
           +esolvida` via `ResolutorPoliticaCentroCusto.resolver(envelope
           +.getColaboradorCentroCusto(), politica)` — a mesma tabela val
           +e para todos os itens de uma execução (o centro de custo é do
           + envelope, não do item), então a resolução pode acontecer uma
           + única vez antes do laço de avaliação, não por item.         
       984 +    3. Chamar `AvaliadorRegrasIndividuais.avaliarLista(normal
           +izados, envelope, tabelaResolvida, politica)` (nova sobrecarg
           +a).                                                          
       985 +  - **Testes obrigatórios:** `CliContratoTest` — um cenário d
           +e integração leve confirmando que uma execução real com `--po
           +litica` apontando para uma política de teste com um centro de
           + custo cadastrado produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_C
           +USTO` (não `CATEGORIA_FORA_POLITICA`) para uma categoria ause
           +nte daquele centro — prova de que o `Main` está de fato usand
           +o a política carregada, não mais o `Set` fixo.               
       986 +  - **Critério de conclusão:** cenário acima verde; `mvn test
           +` completo verde.                                            
       987 +  - **Comando de verificação:**                              
       988 +    ```                                                      
       989 +    mvn -q test -Dtest=CliContratoTest                       
       990 +    ```                                                      
       991 +  - **Commit sugerido:** `feat(T-042): liga politica externa 
           +ao pipeline real via Main`                                   
       992 +  - **Status:** [ ] pendente                                 
       993 +                                                             
       994 +---                                                          
       995 +                                                             
       996 +### Bloco H — Periodicidade e tetos                          
       997 +                                                             
       998 +- [ ] **T-043** — Generalizar `AgregadorTetoDiario` por perio
           +dicidade                                                     
       999 +  - **O que faz:** acrescenta a `AgregadorTetoDiario` uma nov
           +a sobrecarga que recebe, por item, a `TabelaPoliticaResolvida
           +` aplicável, e decide participação no teto compartilhado cons
           +ultando `periodicidade == DIA` na categoria resolvida — não m
           +ais o `Set<String> CATEGORIAS_TETO_DIARIO` fixo (DT-017). O m
           +otivo `TETO_DIARIO_APLICADO` carrega `RN_011` para `alimentac
           +ao`, `RN_012` para `transporte_urbano`, `RN_019` para qualque
           +r outra categoria (pequena tabela de exceção por nome, dentro
           + do agregador — não contradiz a generalização do mecanismo). 
           +A sobrecarga antiga (`Set` fixo + `PoliticaReembolso`) perman
           +ece intacta para a suíte histórica.                          
      1000 +  - **RN atendidas:** RN-011, RN-012, RN-015, RN-019.        
      1001 +  - **CA atendidos:** CA-047 (parcial — mecanismo compartilha
           +do).                                                         
      1002 +  - **DT/seções do plan:** DT-017; plan §11.                 
      1003 +  - **Dependências:** T-028 (`TabelaPoliticaResolvida`), T-02
           +5 (`Periodicidade`).                                         
      1004 +  - **Arquivos que cria/modifica:**                          
      1005 +    - `src/main/java/com/desafio/reembolso/pipeline/Agregador
           +TetoDiario.java`                                             
      1006 +    - `tests/java/com/desafio/reembolso/pipeline/TetoPorPerio
           +dicidadeTest.java`                                           
      1007 +  - **Passos de implementação:**                             
      1008 +    1. Nova sobrecarga `aplicar(List<ItemAvaliado> itens, Tab
           +elaPoliticaResolvida tabela): List<ResultadoTeto>` — filtra i
           +tens elegíveis cuja categoria normalizada exista em `tabela.g
           +etCategorias()` com `periodicidade == Periodicidade.DIA`.    
      1009 +    2. Limite inicial do saldo por `(data, categoria)` vem de
           + `tabela.getCategorias().get(categoria).getLimite()`, não mai
           +s de `PoliticaReembolso`.                                    
      1010 +    3. Escolha do `RegraNegocio` do motivo `TETO_DIARIO_APLIC
           +ADO`: `RN_011` se `"alimentacao".equals(categoria)`, `RN_012`
           + se `"transporte_urbano".equals(categoria)`, `RN_019` caso co
           +ntrário.                                                     
      1011 +    4. Reaproveitar `aplicarCorte(...)` sem alteração (já não
           + conhece categoria, DT-017 confirma que continua reaproveitáv
           +el).                                                         
      1012 +  - **Testes obrigatórios:** `TetoPorPeriodicidadeTest` — cat
           +egoria externa `representacao` (`CC-COMERCIAL`, `periodicidad
           +e: "dia"`, limite R$300,00) com dois itens na mesma data comp
           +artilhando o mesmo saldo, consumido em ordem de `indiceEntrad
           +a`, motivo `TETO_DIARIO_APLICADO`/`RN_019` (CA-047).         
      1013 +  - **Critério de conclusão:** cenário acima verde; `mvn test
           +` completo verde (sobrecarga antiga intocada).               
      1014 +  - **Comando de verificação:**                              
      1015 +    ```                                                      
      1016 +    mvn -q test -Dtest=TetoPorPeriodicidadeTest              
      1017 +    ```                                                      
      1018 +  - **Commit sugerido:** `feat(T-043): generaliza AgregadorTe
           +toDiario por periodicidade`                                  
      1019 +  - **Status:** [ ] pendente                                 
      1020 +                                                             
      1021 +- [ ] **T-044** — Criar `AgregadorTetoIndividual`            
      1022 +  - **O que faz:** cria `pipeline/AgregadorTetoIndividual.jav
           +a`, que processa qualquer categoria com `periodicidade == DIA
           +RIA` na tabela resolvida (não só `hospedagem`): teto individu
           +al por lançamento, sem saldo compartilhado, reaproveitando `A
           +gregadorTetoDiario.aplicarCorte(...)`. `hospedagem` produz `T
           +ETO_HOSPEDAGEM_APLICADO`/`RN_013`; qualquer outra categoria p
           +roduz `TETO_INDIVIDUAL_APLICADO`/`RN_019` (AMB-037). `Agregad
           +orTetoHospedagem.java` **não é removido** nesta task — contin
           +ua existindo e sendo usado pela suíte histórica até T-055/T-0
           +56.                                                          
      1023 +  - **RN atendidas:** RN-013, RN-019.                        
      1024 +  - **CA atendidos:** CA-049.                                
      1025 +  - **DT/seções do plan:** DT-017; plan §11, §19.            
      1026 +  - **Dependências:** T-023 (`TETO_INDIVIDUAL_APLICADO`), T-0
           +28 (`TabelaPoliticaResolvida`), T-043 (`aplicarCorte` já reap
           +roveitado por outra generalização — confirma o padrão).      
      1027 +  - **Arquivos que cria/modifica:**                          
      1028 +    - `src/main/java/com/desafio/reembolso/pipeline/Agregador
           +TetoIndividual.java`                                         
      1029 +    - `tests/java/com/desafio/reembolso/pipeline/AgregadorTet
           +oIndividualTest.java`                                        
      1030 +  - **Passos de implementação:**                             
      1031 +    1. `aplicar(List<ItemAvaliado> itens, TabelaPoliticaResol
           +vida tabela): List<ResultadoTeto>` — filtra itens elegíveis c
           +uja categoria esteja em `tabela.getCategorias()` com `periodi
           +cidade == DIARIA`.                                           
      1032 +    2. Para cada item aplicável, chama `AgregadorTetoDiario.a
           +plicarCorte(item, limite, motivo)`, com `limite = tabela.getC
           +ategorias().get(categoria).getLimite()` e `motivo` escolhido 
           +por `"hospedagem".equals(categoria) ? TETO_HOSPEDAGEM_APLICAD
           +O/RN_013 : TETO_INDIVIDUAL_APLICADO/RN_019`.                 
      1033 +    3. Nunca produz `NAO_REEMBOLSADO_TETO_ESGOTADO` — não há 
           +saldo compartilhado a esgotar (cada item é avaliado isoladame
           +nte).                                                        
      1034 +  - **Testes obrigatórios:** `AgregadorTetoIndividualTest` — 
           +hospedagem sob `periodicidade: "diaria"` reproduz o comportam
           +ento histórico (R$480,00 → R$250,00, `TETO_HOSPEDAGEM_APLICAD
           +O`/`RN_013`, descrição não altera o teto); categoria externa 
           +`estacionamento` (limite R$50,00, `periodicidade: "diaria"`) 
           +com despesa elegível de R$80,00 → parcial R$50,00, motivo úni
           +co `TETO_INDIVIDUAL_APLICADO`/`RN_019` (CA-049); duas hospeda
           +gens elegíveis na mesma data rendem até o dobro do limite ind
           +ividual (nunca compartilham saldo).                          
      1035 +  - **Critério de conclusão:** todos os cenários acima verdes
           +.                                                            
      1036 +  - **Comando de verificação:**                              
      1037 +    ```                                                      
      1038 +    mvn -q test -Dtest=AgregadorTetoIndividualTest           
      1039 +    ```                                                      
      1040 +  - **Commit sugerido:** `feat(T-044): cria AgregadorTetoIndi
           +vidual para periodicidade diaria`                            
      1041 +  - **Status:** [ ] pendente                                 
      1042 +                                                             
      1043 +- [ ] **T-045** — `TetoPorPeriodicidadeTest` — categoria exte
           +rna sob cada periodicidade                                   
      1044 +  - **O que faz:** completa a prova de que o mecanismo de tet
           +o depende exclusivamente da `periodicidade` declarada na polí
           +tica, não do nome histórico da categoria (AMB-036): `hospedag
           +em` reconfigurada com `periodicidade: "dia"` usa o teto **com
           +partilhado** de `AgregadorTetoDiario` (`TETO_DIARIO_APLICADO`
           +/`TETO_DIARIO_ESGOTADO`, `regra = RN_019`, não `RN_013`); `al
           +imentacao` reconfigurada com `periodicidade: "diaria"` usa o 
           +teto **individual** de `AgregadorTetoIndividual` (`TETO_INDIV
           +IDUAL_APLICADO`, `regra = RN_019`, não `RN_011`).            
      1045 +  - **RN atendidas:** RN-019.                                
      1046 +  - **CA atendidos:** CA-047 (fechamento).                   
      1047 +  - **DT/seções do plan:** DT-017; plan §11, §17 (`TetoPorPer
           +iodicidadeTest`, quatro cenários).                           
      1048 +  - **Dependências:** T-043, T-044.                          
      1049 +  - **Arquivos que cria/modifica:**                          
      1050 +    - `tests/java/com/desafio/reembolso/pipeline/TetoPorPerio
           +dicidadeTest.java` (mesma classe de T-043, casos adicionais) 
      1051 +  - **Passos de implementação:**                             
      1052 +    1. Sem alteração de produção esperada — os dois agregador
           +es já leem `periodicidade` da tabela resolvida (T-043/T-044);
           + esta task só prova, com uma política de teste que inverte as
           + periodicidades históricas, que o nome da categoria não é har
           +dcoded em lugar nenhum além da pequena tabela de escolha do c
           +ódigo do motivo.                                             
      1053 +  - **Testes obrigatórios:** `TetoPorPeriodicidadeTest` — os 
           +quatro cenários completos: (1) `representacao` com `"dia"` (j
           +á coberto em T-043); (2) `estacionamento` com `"diaria"` (já 
           +coberto em T-044, referenciado aqui para fechar a matriz); (3
           +) `hospedagem` com `"dia"` — teto compartilhado, `TETO_DIARIO
           +_APLICADO`/`RN_019`; (4) `alimentacao` com `"diaria"` — teto 
           +individual, `TETO_INDIVIDUAL_APLICADO`/`RN_019`.             
      1054 +  - **Critério de conclusão:** os quatro cenários verdes na m
           +esma classe de teste.                                        
      1055 +  - **Comando de verificação:**                              
      1056 +    ```                                                      
      1057 +    mvn -q test -Dtest=TetoPorPeriodicidadeTest              
      1058 +    ```                                                      
      1059 +  - **Commit sugerido:** `test(T-045): prova que o mecanismo 
           +de teto depende de periodicidade, nao do nome da categoria`  
      1060 +  - **Status:** [ ] pendente                                 
      1061 +                                                             
      1062 +- [ ] **T-046** — Wiring dos agregadores por periodicidade no
           + `Main`                                                      
      1063 +  - **O que faz:** `Main.executarPipeline` passa a chamar as 
           +novas sobrecargas de `AgregadorTetoDiario.aplicar(elegiveis, 
           +tabelaResolvida)` e `AgregadorTetoIndividual.aplicar(elegivei
           +s, tabelaResolvida)`, em vez de `AgregadorTetoDiario`(antigo)
           +/`AgregadorTetoHospedagem` — usando a mesma `TabelaPoliticaRe
           +solvida` já calculada em T-042.                              
      1064 +  - **RN atendidas:** RN-011 a RN-015, RN-019.               
      1065 +  - **CA atendidos:** confirma CA-027, CA-047, CA-049 ponta a
           + ponta via CLI.                                              
      1066 +  - **DT/seções do plan:** DT-017; plan §2 (passo 9), §11.   
      1067 +  - **Dependências:** T-042 (`TabelaPoliticaResolvida` dispon
           +ível no `Main`), T-043, T-044.                               
      1068 +  - **Arquivos que cria/modifica:**                          
      1069 +    - `src/main/java/com/desafio/reembolso/Main.java`        
      1070 +    - `tests/java/com/desafio/reembolso/CliContratoTest.java`
      1071 +  - **Passos de implementação:**                             
      1072 +    1. Substituir, em `executarPipeline`, as chamadas a `Agre
           +gadorTetoDiario.aplicar(elegiveisParaTetos)` e `AgregadorTeto
           +Hospedagem.aplicar(elegiveisParaTetos)` pelas novas sobrecarg
           +as, passando `tabelaResolvida`.                              
      1073 +    2. `CompositorSaida.compor(...)` continua recebendo duas 
           +listas de `ResultadoTeto` (uma do agregador de `"dia"`, outra
           + do de `"diaria"`) — a assinatura de `compor` não muda, só a 
           +origem das listas.                                           
      1074 +  - **Testes obrigatórios:** `CliContratoTest` — um cenário d
           +e integração leve confirmando, via execução real com `--polit
           +ica` apontando para `CC-ENG-PLATAFORMA` (hospedagem limite `0
           +,00`), que o item de hospedagem é recusado com `CATEGORIA_NAO
           +_REEMBOLSAVEL_CENTRO_CUSTO` **antes** de qualquer agregador d
           +e teto ser exercitado (spec 8.4, item 15) — prova de que a re
           +cusa por política antecede a agregação também no caminho real
           +.                                                            
      1075 +  - **Critério de conclusão:** cenário acima verde; `mvn test
           +` completo verde.                                            
      1076 +  - **Comando de verificação:**                              
      1077 +    ```                                                      
      1078 +    mvn -q test -Dtest=CliContratoTest                       
      1079 +    ```                                                      
      1080 +  - **Commit sugerido:** `feat(T-046): liga agregadores por p
           +eriodicidade ao pipeline real via Main`                      
      1081 +  - **Status:** [ ] pendente                                 
      1082 +                                                             
      1083 +---                                                          
      1084 +                                                             
      1085 +### Bloco I — Duplicidade e saída                            
      1086 +                                                             
      1087 +- [ ] **T-047** — Estender `DetectorDuplicidadeEconomica` com
           + `moeda` na chave                                            
      1088 +  - **O que faz:** `ChaveDuplicidade` ganha o campo `moeda` (
           +RN-010 atualizada, AMB-028): dois itens com mesma `data`/cate
           +goria normalizada/valor normalizado (já convertido, desde T-0
           +38)/fornecedor/descrição, mas em moedas diferentes, nunca são
           + tratados como duplicata. Itens com `MOEDA_SEM_COTACAO` já ch
           +egam inelegíveis a este estágio (T-037/T-039 garantem isso) —
           + nenhuma exclusão adicional é necessária aqui.               
      1089 +  - **RN atendidas:** RN-010 (atualizada).                   
      1090 +  - **CA atendidos:** CA-033.                                
      1091 +  - **DT/seções do plan:** plan §12.                         
      1092 +  - **Dependências:** T-029 (`ItemValidado.getMoeda()`), T-03
           +8 (valor normalizado já convertido).                         
      1093 +  - **Arquivos que cria/modifica:**                          
      1094 +    - `src/main/java/com/desafio/reembolso/pipeline/DetectorD
           +uplicidadeEconomica.java`                                    
      1095 +    - `tests/java/com/desafio/reembolso/pipeline/DuplicidadeE
           +ntreMoedasTest.java`                                         
      1096 +  - **Passos de implementação:**                             
      1097 +    1. Adicionar `String moeda` ao `record ChaveDuplicidade`,
           + populado a partir de `item.itemNormalizado().item().getMoeda
           +()`.                                                         
      1098 +    2. Nenhuma outra mudança de mecanismo — `equals`/`hashCod
           +e` do `record` já passam a diferenciar por moeda automaticame
           +nte.                                                         
      1099 +  - **Testes obrigatórios:** `DuplicidadeEntreMoedasTest` — d
           +ois itens com mesma data/categoria/fornecedor/descrição e val
           +or convertido coincidente, mas em moedas diferentes (ex.: EUR
           + e BRL), **não** são tratados como duplicata (CA-033); o comp
           +ortamento histórico (mesma moeda, chave idêntica) permanece i
           +nalterado.                                                   
      1100 +  - **Critério de conclusão:** cenário acima verde; `mvn test
           +` completo verde (suíte histórica de `DuplicidadeEconomicaTes
           +t`, T-012, usa BRL implícito em todos os itens, então a chave
           + estendida não muda nenhum resultado existente).             
      1101 +  - **Comando de verificação:**                              
      1102 +    ```                                                      
      1103 +    mvn -q test "-Dtest=DuplicidadeEntreMoedasTest,Duplicidad
           +eEconomicaTest"                                              
      1104 +    ```                                                      
      1105 +  - **Commit sugerido:** `feat(T-047): adiciona moeda a chave
           + de duplicidade economica`                                   
      1106 +  - **Status:** [ ] pendente                                 
      1107 +                                                             
      1108 +- [ ] **T-048** — Estender `CompositorSaida` com campos de câ
           +mbio e nova ordem de motivos                                 
      1109 +  - **O que faz:** `ResultadoItem` ganha três campos (`moeda`
           +, `taxaCambioAplicada`, `dataCotacaoUtilizada`), populados se
           +m recálculo a partir do `ItemValidado` de cada posição. `ORDE
           +M_CAMPO` ganha `CampoCanonico.MOEDA` entre `VALOR` e `TEM_NOT
           +A_FISCAL` (deslocando `TEM_NOTA_FISCAL` de 6 para 7). `ESTAGI
           +O_POR_CODIGO` ganha `MOEDA_SEM_COTACAO` (estágio 3, entre `ID
           +_DUPLICADO` e `VALOR_NAO_POSITIVO`) e `CATEGORIA_NAO_REEMBOLS
           +AVEL_CENTRO_CUSTO` (estágio 6, entre `CATEGORIA_FORA_POLITICA
           +` e `FORA_COMPETENCIA`) e `TETO_INDIVIDUAL_APLICADO` (mesmo e
           +stágio dos demais motivos de teto), com a renumeração complet
           +a de 8.3: `0` `ITEM_TIPO_INVALIDO`, `1` estruturais, `2` `ID_
           +DUPLICADO`, `3` `MOEDA_SEM_COTACAO`, `4` `VALOR_NAO_POSITIVO`
           +, `5` `CATEGORIA_FORA_POLITICA`, `6` `CATEGORIA_NAO_REEMBOLSA
           +VEL_CENTRO_CUSTO`, `7` `FORA_COMPETENCIA`, `8` `NOTA_FISCAL_A
           +USENTE`, `9` `DUPLICIDADE`, `10` motivos de teto.            
      1110 +  - **RN atendidas:** RN-017 (atualizada).                   
      1111 +  - **CA atendidos:** CA-034.                                
      1112 +  - **DT/seções do plan:** DT-019; plan §10.                 
      1113 +  - **Dependências:** T-022 (`CampoCanonico.MOEDA`), T-023 (t
           +rês `MotivoCodigo` novos), T-029 (getters de câmbio em `ItemV
           +alidado`).                                                   
      1114 +  - **Arquivos que cria/modifica:**                          
      1115 +    - `src/main/java/com/desafio/reembolso/pipeline/Composito
           +rSaida.java`                                                 
      1116 +    - `tests/java/com/desafio/reembolso/pipeline/OrdemMotivos
           +Test.java` (estendido)                                       
      1117 +    - `tests/java/com/desafio/reembolso/pipeline/SaidaCambioT
           +est.java`                                                    
      1118 +  - **Passos de implementação:**                             
      1119 +    1. Adicionar os três campos ao `record ResultadoItem`, co
           +m construção populada em `componentesDoRegistro(...)` a parti
           +r de `itemValidado.getMoeda()`/`getTaxaCambioAplicada()`/`get
           +DataCotacaoUtilizada()` — nos três pontos de retorno do métod
           +o (recusado antes da duplicidade, recusado por duplicidade, r
           +esultado de teto).                                           
      1120 +    2. Atualizar `criarOrdemCampo()`: inserir `CampoCanonico.
           +MOEDA` com valor `5`, deslocar `TEM_NOTA_FISCAL` de `6` para 
           +`6` → **atenção:** a posição correta é `MOEDA = 5`? Não — `VA
           +LOR` já é `5`; a ordem correta é `ID=0, DATA=1, CATEGORIA=2, 
           +DESCRICAO=3, FORNECEDOR=4, VALOR=5, MOEDA=6, TEM_NOTA_FISCAL=
           +7`. Renumerar exatamente assim.                              
      1121 +    3. Atualizar `criarEstagios()` com a tabela completa de o
           +nze entradas descrita acima (0 a 10).                        
      1122 +  - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) —
           + um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` 
           +simultâneos aparece nessa ordem; um item com erro estrutural 
           +em `despesa.moeda` aparece na posição correta da ordem canôni
           +ca de campo (entre `valor` e `tem_nota_fiscal`). `SaidaCambio
           +Test` — os quatro formatos de `moeda`/`taxa_cambio_aplicada`/
           +`data_cotacao_utilizada` no `ResultadoItem` (BRL: `"BRL"`/`1`
           +/`null`; estrangeira convertida: moeda/taxa/data da cotação; 
           +estruturalmente inválida: os três nulos; válida sem cotação: 
           +os três nulos, mais `valor_normalizado` nulo e `valor_reembol
           +savel` `0,00`) — CA-034.                                     
      1123 +  - **Critério de conclusão:** todos os cenários acima verdes
           +; `mvn test` completo verde (a suíte histórica não usa `MOEDA
           +_SEM_COTACAO` nem `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, 
           +então a renumeração de estágios não muda nenhuma ordem já tes
           +tada).                                                       
      1124 +  - **Comando de verificação:**                              
      1125 +    ```                                                      
      1126 +    mvn -q test "-Dtest=OrdemMotivosTest,SaidaCambioTest"    
      1127 +    ```                                                      
      1128 +  - **Commit sugerido:** `feat(T-048): estende CompositorSaid
           +a com campos de cambio e nova ordem de motivos`              
      1129 +  - **Status:** [ ] pendente                                 
      1130 +                                                             
      1131 +- [ ] **T-049** — Serializar campos de câmbio em `EscritorRes
           +ultado`                                                      
      1132 +  - **O que faz:** `EscritorResultado.registro(...)` passa a 
           +escrever `moeda`, `taxa_cambio_aplicada` e `data_cotacao_util
           +izada` no JSON de saída, entre `valor_informado` e `valor_nor
           +malizado` (spec 4.3). `taxa_cambio_aplicada` é sempre número 
           +JSON (nunca texto), preservando a precisão do arquivo de câmb
           +io — mesma técnica de `monetario(...)` já usada para os demai
           +s campos decimais, mas sem forçar duas casas (a taxa pode ter
           + mais precisão que um valor monetário).                      
      1133 +  - **RN atendidas:** RN-017 (atualizada).                   
      1134 +  - **CA atendidos:** CA-034 (nível de serialização).        
      1135 +  - **DT/seções do plan:** plan §9, §13.                     
      1136 +  - **Dependências:** T-048 (`ResultadoItem` com os três camp
           +os).                                                         
      1137 +  - **Arquivos que cria/modifica:**                          
      1138 +    - `src/main/java/com/desafio/reembolso/escritor/EscritorR
           +esultado.java`                                               
      1139 +    - `tests/java/com/desafio/reembolso/escritor/EscritorResu
           +ltadoTest.java` (estendido)                                  
      1140 +  - **Passos de implementação:**                             
      1141 +    1. Em `registro(ResultadoItem resultado)`, adicionar `no.
           +put("moeda", resultado.moeda())` (nulo-seguro — Jackson seria
           +liza `String` nulo como `null` JSON), `no.set("taxa_cambio_ap
           +licada", resultado.taxaCambioAplicada() == null ? null : MAPP
           +ER.getNodeFactory().numberNode(resultado.taxaCambioAplicada()
           +))` (ou equivalente, preservando a escala do `BigDecimal` sem
           + `setScale` forçado), `no.put("data_cotacao_utilizada", resul
           +tado.dataCotacaoUtilizada() == null ? null : resultado.dataCo
           +tacaoUtilizada().toString())`.                               
      1142 +    2. Posicionar as três chamadas entre `valor_informado` e 
           +`valor_normalizado`, respeitando a ordem de campos de 4.3.   
      1143 +  - **Testes obrigatórios:** `EscritorResultadoTest` (estendi
           +do) — os quatro formatos de saída (mesmos cenários de `SaidaC
           +ambioTest`, agora verificados no JSON serializado, não no `Re
           +sultadoItem`); `taxa_cambio_aplicada` nunca aparece como stri
           +ng nem em notação científica.                                
      1144 +  - **Critério de conclusão:** cenários acima verdes; `mvn te
           +st` completo verde.                                          
      1145 +  - **Comando de verificação:**                              
      1146 +    ```                                                      
      1147 +    mvn -q test -Dtest=EscritorResultadoTest                 
      1148 +    ```                                                      
      1149 +  - **Commit sugerido:** `feat(T-049): serializa moeda, taxa_
           +cambio_aplicada e data_cotacao_utilizada`                    
      1150 +  - **Status:** [ ] pendente                                 
      1151 +                                                             
      1152 +---                                                          
      1153 +                                                             
      1154 +### Bloco J — Regressões e integração                        
      1155 +                                                             
      1156 +- [ ] **T-050** — Fixture externa de política histórica + reg
           +ressão R$585,43                                              
      1157 +  - **O que faz:** cria `tests/resources/fixtures/politica-hi
           +storica.json` (equivalente aos valores hardcoded pré-Dia 2: `
           +alimentacao` R$60/dia, `transporte_urbano` R$80/dia, `hospeda
           +gem` R$250/diária, `nota_fiscal_obrigatoria_acima_de` R$100, 
           +`vigencia` qualquer data real válida) e `tests/resources/fixt
           +ures/cambio-historico.json` (um `cambio.json` mínimo/vazio, j
           +á que `despesas-exemplo.json` não tem despesas em moeda estra
           +ngeira). Processa `exemplos/despesas-exemplo.json` de ponta a
           + ponta com esses dois arquivos externos e confirma `total_ree
           +mbolsavel = 585.43` (CA-037), comparando estruturalmente cont
           +ra o fixture já existente `tests/resources/fixtures/despesas-
           +exemplo-esperado.json` (que continua válido, porque a polític
           +a histórica reproduz exatamente os valores antes hardcoded). 
      1158 +  - **RN atendidas:** RN-019 (confirmação de que a política e
           +xterna equivalente reproduz o comportamento histórico).      
      1159 +  - **CA atendidos:** CA-037.                                
      1160 +  - **DT/seções do plan:** plan §16.                         
      1161 +  - **Dependências:** T-042, T-046 (pipeline real usando polí
           +tica e câmbio externos de ponta a ponta), T-030/T-032 (leitor
           +es).                                                         
      1162 +  - **Arquivos que cria/modifica:**                          
      1163 +    - `tests/resources/fixtures/politica-historica.json`     
      1164 +    - `tests/resources/fixtures/cambio-historico.json`       
      1165 +    - `tests/java/com/desafio/reembolso/RegressaoHistoricaTes
           +t.java`                                                      
      1166 +  - **Passos de implementação:**                             
      1167 +    1. Escrever os dois arquivos JSON de fixture, manualmente
           +, seguindo o contrato de `politica.json`/`cambio.json` de spe
           +c 4.1.1.                                                     
      1168 +    2. Escrever `RegressaoHistoricaTest`, invocando `Main.run
           +(...)` (ou `executarPipeline` diretamente, se mais direto par
           +a o teste) com `--input exemplos/despesas-exemplo.json --poli
           +tica <fixture> --cambio <fixture> --output <temp>`.          
      1169 +    3. Comparar o JSON de saída, campo a campo, contra `despe
           +sas-exemplo-esperado.json` já existente.                     
      1170 +  - **Testes obrigatórios:** `RegressaoHistoricaTest` — os 14
           + registros e `total_reembolsavel = 585.43` coincidem com o fi
           +xture histórico já existente (CA-037).                       
      1171 +  - **Critério de conclusão:** teste verde; nenhuma alteração
           + em `despesas-exemplo-esperado.json` nem em código de produçã
           +o.                                                           
      1172 +  - **Comando de verificação:**                              
      1173 +    ```                                                      
      1174 +    mvn -q test -Dtest=RegressaoHistoricaTest                
      1175 +    ```                                                      
      1176 +  - **Commit sugerido:** `test(T-050): fixture de politica hi
           +storica e regressao dos 585,43`                              
      1177 +  - **Status:** [ ] pendente                                 
      1178 +                                                             
      1179 +- [ ] **T-051** — Regressão política v4 / `CC-ENG-PLATAFORMA`
           + — R$351,43                                                  
      1180 +  - **O que faz:** processa o mesmo `exemplos/despesas-exempl
           +o.json` com `politica-v4.json` real e um envelope cujo `colab
           +orador.centro_custo` é `"CC-ENG-PLATAFORMA"` (cadastrado na t
           +abela), confirmando `total_reembolsavel = 351.43` (CA-038) e 
           +as quatro mudanças de item declaradas em `spec.md` §12.2 (`d-
           +001` integral `72,50`; `d-002` parcial `2,50`; `d-010` recusa
           +do `0,00`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`; `d-014` 
           +integral `61,00`), comparado estruturalmente contra um fixtur
           +e novo escrito manualmente a partir de §12.2.                
      1181 +  - **RN atendidas:** RN-019.                                
      1182 +  - **CA atendidos:** CA-038.                                
      1183 +  - **DT/seções do plan:** plan §16.                         
      1184 +  - **Dependências:** T-050 (mesma classe de teste, `Regressa
           +oHistoricaTest`, estendida) ou arquivo próprio — ver passo 1.
      1185 +  - **Arquivos que cria/modifica:**                          
      1186 +    - `tests/resources/fixtures/despesas-exemplo-v4-esperado.
           +json`                                                        
      1187 +    - `tests/resources/fixtures/envelope-cc-eng-plataforma.js
           +on` (envelope de teste com `centro_custo = "CC-ENG-PLATAFORMA
           +"` e as mesmas despesas de `exemplos/despesas-exemplo.json`, 
           +já que o arquivo original não traz `colaborador.centro_custo`
           +)                                                            
      1188 +    - `tests/java/com/desafio/reembolso/RegressaoHistoricaTes
           +t.java` (mesma classe de T-050, caso adicional)              
      1189 +  - **Passos de implementação:**                             
      1190 +    1. Verificar se `exemplos/despesas-exemplo.json` já traz 
           +`colaborador.centro_custo`; se não trouxer, criar o fixture d
           +e envelope equivalente com o centro de custo adicionado, sem 
           +alterar o arquivo original em `exemplos/` (que é lido por T-0
           +50/T-021 e não deve mudar).                                  
      1191 +    2. Escrever `despesas-exemplo-v4-esperado.json` manualmen
           +te, a partir da tabela de `spec.md` §12.2 combinada com os de
           +z itens inalterados de §4.7.                                 
      1192 +    3. Escrever o caso de teste, processando o fixture de env
           +elope com `politica-v4.json` real.                           
      1193 +  - **Testes obrigatórios:** `RegressaoHistoricaTest` — `tota
           +l_reembolsavel = 351.43`; os quatro itens mudados coincidem e
           +xatamente com §12.2; os dez itens restantes coincidem com o c
           +enário histórico (CA-038).                                   
      1194 +  - **Critério de conclusão:** teste verde.                  
      1195 +  - **Comando de verificação:**                              
      1196 +    ```                                                      
      1197 +    mvn -q test -Dtest=RegressaoHistoricaTest                
      1198 +    ```                                                      
      1199 +  - **Commit sugerido:** `test(T-051): regressao da politica 
           +v4 sobre CC-ENG-PLATAFORMA — total 351,43`                   
      1200 +  - **Status:** [ ] pendente                                 
      1201 +                                                             
      1202 +- [ ] **T-052** — Integração envelope — Rafael / `CC-COMERCIA
           +L` — R$1.143,26                                              
      1203 +  - **O que faz:** processa `exemplos/envelope/despesas-envel
           +ope.json` (Rafael Nkemelu, `CC-COMERCIAL`) com `exemplos/enve
           +lope/politica-v4.json` e `exemplos/envelope/cambio.json` reai
           +s, confirmando `total_reembolsavel = 1143.26` (CA-039), compa
           +rado estruturalmente contra um fixture manual construído a pa
           +rtir da tabela de `spec.md` §12.3 (os dez itens `e-001` a `e-
           +010`, incluindo as três conversões cambiais e o motivo `CATEG
           +ORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` de `e-009`).             
      1204 +  - **RN atendidas:** RN-019, RN-020.                        
      1205 +  - **CA atendidos:** CA-039.                                
      1206 +  - **DT/seções do plan:** plan §16, §17.                    
      1207 +  - **Dependências:** T-042, T-046 (pipeline completo com pol
           +ítica e câmbio reais).                                       
      1208 +  - **Arquivos que cria/modifica:**                          
      1209 +    - `tests/resources/fixtures/despesas-envelope-esperado.js
           +on`                                                          
      1210 +    - `tests/java/com/desafio/reembolso/IntegracaoEnvelopeTes
           +t.java`                                                      
      1211 +  - **Passos de implementação:**                             
      1212 +    1. Escrever o fixture manualmente, campo a campo, a parti
           +r da tabela de §12.3 (moeda, taxa aplicada, data de cotação, 
           +decisão, reembolsável e motivo de cada um dos dez itens).    
      1213 +    2. Escrever o caso de teste, processando `exemplos/envelo
           +pe/despesas-envelope.json` com os dois arquivos reais de `exe
           +mplos/envelope/`.                                            
      1214 +  - **Testes obrigatórios:** `IntegracaoEnvelopeTest` — os de
           +z registros e `total_reembolsavel = 1143.26` coincidem com o 
           +fixture (CA-039); em particular, `e-009` (`coworking`) traz `
           +CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA
           +_POLITICA`.                                                  
      1215 +  - **Critério de conclusão:** teste verde.                  
      1216 +  - **Comando de verificação:**                              
      1217 +    ```                                                      
      1218 +    mvn -q test -Dtest=IntegracaoEnvelopeTest                
      1219 +    ```                                                      
      1220 +  - **Commit sugerido:** `test(T-052): integra despesas-envel
           +ope.json (Rafael/CC-COMERCIAL) — total 1.143,26`             
      1221 +  - **Status:** [ ] pendente                                 
      1222 +                                                             
      1223 +- [ ] **T-053** — Integração envelope — Dani / centro de cust
           +o desconhecido — R$373,76                                    
      1224 +  - **O que faz:** processa `exemplos/envelope/despesas-envel
           +ope-cc-desconhecido.json` (Dani Okonkwo, `CC-SUPORTE-N2`, for
           +a da tabela) com `exemplos/envelope/politica-v4.json` e `exem
           +plos/envelope/cambio.json` reais, confirmando `total_reembols
           +avel = 373.76` (CA-040), comparado estruturalmente contra um 
           +fixture manual construído a partir de `spec.md` §12.4 (quatro
           + itens `f-001` a `f-004`, todos sob a política `padrao`).    
      1225 +  - **RN atendidas:** RN-019, RN-020.                        
      1226 +  - **CA atendidos:** CA-040.                                
      1227 +  - **DT/seções do plan:** plan §16, §17.                    
      1228 +  - **Dependências:** T-042, T-046.                          
      1229 +  - **Arquivos que cria/modifica:**                          
      1230 +    - `tests/resources/fixtures/despesas-envelope-cc-desconhe
           +cido-esperado.json`                                          
      1231 +    - `tests/java/com/desafio/reembolso/IntegracaoEnvelopeTes
           +t.java` (mesma classe de T-052, caso adicional)              
      1232 +  - **Passos de implementação:**                             
      1233 +    1. Escrever o fixture manualmente a partir de §12.4.     
      1234 +    2. Escrever o caso de teste, processando o segundo arquiv
           +o de envelope.                                               
      1235 +  - **Testes obrigatórios:** `IntegracaoEnvelopeTest` — os qu
           +atro registros e `total_reembolsavel = 373.76` coincidem com 
           +o fixture (CA-040); confirma que `CC-SUPORTE-N2` usa integral
           +mente `padrao`.                                              
      1236 +  - **Critério de conclusão:** teste verde.                  
      1237 +  - **Comando de verificação:**                              
      1238 +    ```                                                      
      1239 +    mvn -q test -Dtest=IntegracaoEnvelopeTest                
      1240 +    ```                                                      
      1241 +  - **Commit sugerido:** `test(T-053): integra despesas-envel
           +ope-cc-desconhecido.json (Dani) — total 373,76`              
      1242 +  - **Status:** [ ] pendente                                 
      1243 +                                                             
      1244 +- [ ] **T-054** — Execução real da suíte e do JAR com as quat
           +ro flags                                                     
      1245 +  - **O que faz:** verificação manual, documentada, de que a 
           +suíte inteira passa (`mvn test`) e que o JAR empacotado (`mvn
           + package`) executa de ponta a ponta com as quatro flags reais
           + contra os quatro cenários financeiros do Dia 2 (baseline his
           +tórica, política v4/`CC-ENG-PLATAFORMA`, Rafael, Dani), produ
           +zindo os totais já confirmados em T-050 a T-053 também pelo b
           +inário real, não só pelos testes JUnit. Se algo divergir entr
           +e o teste automatizado e a execução real do JAR, o defeito é 
           +corrigido nesta mesma task antes do commit (mesma contingênci
           +a de T-015).                                                 
      1246 +  - **RN atendidas:** nenhuma nova — é verificação, não regra
           + de negócio.                                                 
      1247 +  - **CA atendidos:** confirma CA-037 a CA-040 via binário re
           +al, fora do JUnit.                                           
      1248 +  - **DT/seções do plan:** DT-002, DT-009; plan §16.         
      1249 +  - **Dependências:** T-050, T-051, T-052, T-053.            
      1250 +  - **Arquivos que cria/modifica:** nenhum arquivo de produçã
           +o ou teste esperado — task de verificação manual. Se um defei
           +to real for encontrado, o arquivo afetado é corrigido e lista
           +do no commit.                                                
      1251 +  - **Passos de implementação:**                             
      1252 +    1. `mvn package` e confirmar que `target/motor-reembolso.
           +jar` é produzido.                                            
      1253 +    2. Executar `java -jar target/motor-reembolso.jar calcula
           +r --input exemplos/despesas-exemplo.json --output <tmp> --pol
           +itica <fixture-historica> --cambio <fixture-historica>` e con
           +ferir `total_reembolsavel = 585.43` no arquivo de saída real.
      1254 +    3. Repetir para os outros três cenários (v4/`CC-ENG-PLATA
           +FORMA`, Rafael, Dani), com os arquivos reais de `exemplos/env
           +elope/`.                                                     
      1255 +  - **Testes obrigatórios:** nenhum teste JUnit novo — a veri
           +ficação é a execução manual documentada acima.               
      1256 +  - **Critério de conclusão:** os quatro totais conferem, lid
           +os diretamente do arquivo de saída produzido pelo JAR real; `
           +mvn test` inteiro verde.                                     
      1257 +  - **Comando de verificação:**                              
      1258 +    ```                                                      
      1259 +    mvn -q test                                              
      1260 +    mvn -q package                                           
      1261 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/despesas-exemplo.json --output resultado-585.json --pol
           +itica tests/resources/fixtures/politica-historica.json --camb
           +io tests/resources/fixtures/cambio-historico.json            
      1262 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/envelope/despesas-envelope.json --output resultado-rafa
           +el.json --politica exemplos/envelope/politica-v4.json --cambi
           +o exemplos/envelope/cambio.json                              
      1263 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/envelope/despesas-envelope-cc-desconhecido.json --outpu
           +t resultado-dani.json --politica exemplos/envelope/politica-v
           +4.json --cambio exemplos/envelope/cambio.json                
      1264 +    ```                                                      
      1265 +  - **Commit sugerido:** `test(T-054): confirma execucao real
           + do jar com as quatro flags nos quatro cenarios` (ou nenhum c
           +ommit, se nada foi alterado além da verificação — nesse caso 
           +a task se encerra sem commit de código, mas o relatório da se
           +ssão registra a verificação feita)                           
      1266 +  - **Status:** [ ] pendente                                 
      1267 +                                                             
      1268 +---                                                          
      1269 +                                                             
      1270 +### Bloco K — Remoção do legado e documentação final         
      1271 +                                                             
      1272 +- [ ] **T-055** — Migrar consumidores restantes de `PoliticaR
           +eembolso` e `AgregadorTetoHospedagem`                        
      1273 +  - **O que faz:** primeiro dos quatro passos exigidos por `C
           +LAUDE.md` §6 antes de remover os dois componentes superados: 
           +localizar e migrar todo teste que ainda chama diretamente `Ag
           +regadorTetoHospedagem.aplicar(...)` ou as sobrecargas antigas
           + de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` que de
           +pendem de `PoliticaReembolso.padrao()`, redirecionando-os par
           +a as APIs novas (com a fixture de política histórica de T-050
           + fazendo o papel de `PoliticaReembolso.padrao()`) ou para `Ag
           +regadorTetoIndividual`, conforme o caso.                     
      1274 +  - **RN atendidas:** nenhuma nova — é migração de consumidor
           +es internos, não regra de negócio.                           
      1275 +  - **DT/seções do plan:** DT-011 (nota de supersessão); plan
           + §19 ("Substituídos").                                       
      1276 +  - **Dependências:** T-041 (sobrecargas novas de `AvaliadorR
           +egrasIndividuais`), T-043 (sobrecargas novas de `AgregadorTet
           +oDiario`), T-044 (`AgregadorTetoIndividual`), T-050 (fixture 
           +de política histórica).                                      
      1277 +  - **Arquivos que cria/modifica:**                          
      1278 +    - `git grep "PoliticaReembolso"` e `git grep "AgregadorTe
           +toHospedagem"` primeiro, para levantar a lista exata de arqui
           +vos afetados (provavelmente `TetoHospedagemTest.java` e qualq
           +uer teste que ainda construa `AvaliadorRegrasIndividuais`/`Ag
           +regadorTetoDiario` via as sobrecargas de dois/um argumento se
           +m política externa).                                         
      1279 +    - Os arquivos de teste identificados pelo `git grep` acim
           +a.                                                           
      1280 +  - **Passos de implementação:**                             
      1281 +    1. Rodar `git grep "PoliticaReembolso"` e `git grep "Agre
           +gadorTetoHospedagem"` e listar cada ocorrência fora dos dois 
           +arquivos que serão removidos em T-056.                       
      1282 +    2. Para cada teste que chama `AgregadorTetoHospedagem.apl
           +icar(...)` diretamente, decidir: se o cenário já está coberto
           + por `AgregadorTetoIndividualTest` (T-044), remover a duplica
           +ção; se cobre algo novo, portar o cenário para `AgregadorTeto
           +IndividualTest` usando a política de teste equivalente.      
      1283 +    3. Para testes que dependem implicitamente de `PoliticaRe
           +embolso.padrao()` via as sobrecargas antigas de `AvaliadorReg
           +rasIndividuais`/`AgregadorTetoDiario` (T-006 a T-021), decidi
           +r, caso a caso, entre manter a sobrecarga antiga como está (e
           +la não depende de arquivo externo e continua sendo um teste u
           +nitário legítimo do mecanismo, independente da fonte da polít
           +ica) ou migrá-la para a política de teste externa — a decisão
           + default é **manter**, porque essas sobrecargas testam o meca
           +nismo de teto/elegibilidade em si, não a fonte dos valores; s
           +ó migrar se o teste especificamente afirmar algo sobre `Polit
           +icaReembolso` como classe.                                   
      1284 +    4. Rodar `mvn test` completo após cada migração.         
      1285 +  - **Testes obrigatórios:** suíte completa (`mvn test`) verd
           +e após a migração; nenhum teste órfão (que testava algo espec
           +ífico de `AgregadorTetoHospedagem` e não tem equivalente em `
           +AgregadorTetoIndividual`) foi perdido silenciosamente.       
      1286 +  - **Critério de conclusão:** `git grep "AgregadorTetoHosped
           +agem"` só encontra ocorrências dentro do próprio `AgregadorTe
           +toHospedagem.java` (a ser removido em T-056); `mvn test` verd
           +e.                                                           
      1287 +  - **Comando de verificação:**                              
      1288 +    ```                                                      
      1289 +    git grep "PoliticaReembolso"                             
      1290 +    git grep "AgregadorTetoHospedagem"                       
      1291 +    mvn -q test                                              
      1292 +    ```                                                      
      1293 +  - **Commit sugerido:** `refactor(T-055): migra consumidores
           + de AgregadorTetoHospedagem para AgregadorTetoIndividual`    
      1294 +  - **Status:** [ ] pendente                                 
      1295 +                                                             
      1296 +- [ ] **T-056** — Remover `PoliticaReembolso` e `AgregadorTet
           +oHospedagem`                                                 
      1297 +  - **O que faz:** segundo, terceiro e quarto passos de `CLAU
           +DE.md` §6: com todos os consumidores já migrados (T-055), rem
           +ove `src/main/java/com/desafio/reembolso/modelo/PoliticaReemb
           +olso.java` e `src/main/java/com/desafio/reembolso/pipeline/Ag
           +regadorTetoHospedagem.java`, e as sobrecargas de compatibilid
           +ade que dependiam exclusivamente deles (as sobrecargas antiga
           +s de `AvaliadorRegrasIndividuais.avaliar(item)`/`avaliar(item
           +, envelope)` e `AgregadorTetoDiario.aplicar(itens)` só são re
           +movidas se T-055 confirmou que nenhum teste as usa mais — cas
           +o contrário, permanecem, e esta task remove só os dois arquiv
           +os-alvo).                                                    
      1298 +  - **RN atendidas:** nenhuma nova — remoção de código supera
           +do (DT-007 → DT-011).                                        
      1299 +  - **DT/seções do plan:** DT-007 (supersessão), DT-011; plan
           + §19.                                                        
      1300 +  - **Dependências:** T-055.                                 
      1301 +  - **Arquivos que cria/modifica:**                          
      1302 +    - Remove `src/main/java/com/desafio/reembolso/modelo/Poli
           +ticaReembolso.java`                                          
      1303 +    - Remove `src/main/java/com/desafio/reembolso/pipeline/Ag
           +regadorTetoHospedagem.java`                                  
      1304 +    - Qualquer sobrecarga de compatibilidade que ficou sem us
           +o após T-055 (verificado por `git grep`, não removido "por pr
           +ecaução" sem confirmação).                                   
      1305 +  - **Passos de implementação:**                             
      1306 +    1. Rodar `git grep "PoliticaReembolso"` e `git grep "Agre
           +gadorTetoHospedagem"` — confirmar que a única ocorrência rest
           +ante de cada um é o próprio arquivo.                         
      1307 +    2. Remover os dois arquivos.                             
      1308 +    3. Remover import não utilizado em qualquer classe que ai
           +nda os referenciasse só por import (não deveria haver nenhuma
           +, se T-055 foi completa).                                    
      1309 +    4. Rodar `mvn -q -DskipTests compile` para confirmar que 
           +a remoção não quebra a compilação (nenhuma referência residua
           +l).                                                          
      1310 +    5. Rodar `mvn -q test` completo.                         
      1311 +  - **Testes obrigatórios:** nenhum teste novo — a garantia é
           + a compilação limpa e a suíte inteira verde após a remoção.  
      1312 +  - **Critério de conclusão:** `git grep "PoliticaReembolso"`
           + e `git grep "AgregadorTetoHospedagem"` não retornam nenhuma 
           +ocorrência; `mvn -q -DskipTests compile` e `mvn -q test` verd
           +es.                                                          
      1313 +  - **Comando de verificação:**                              
      1314 +    ```                                                      
      1315 +    git grep "PoliticaReembolso"                             
      1316 +    git grep "AgregadorTetoHospedagem"                       
      1317 +    mvn -q -DskipTests compile                               
      1318 +    mvn -q test                                              
      1319 +    ```                                                      
      1320 +  - **Commit sugerido:** `refactor(T-056): remove PoliticaRee
           +mbolso e AgregadorTetoHospedagem`                            
      1321 +  - **Status:** [ ] pendente                                 
      1322 +                                                             
      1323 +- [ ] **T-057** — README com CLI de quatro flags e documentaç
           +ão de política/câmbio                                        
      1324 +  - **O que faz:** atualiza `README.md` (criado em T-021) par
           +a refletir o contrato de execução do Dia 2: as quatro flags o
           +brigatórias (`--input`/`--output`/`--politica`/`--cambio`), e
           +xemplos de execução reais contra os quatro cenários financeir
           +os (baseline histórica, política v4, Rafael, Dani), a tabela 
           +de códigos de saída atualizada (`0`/`2`/`3`, com a nota de qu
           +e `2` agora também cobre política/câmbio inválidos), e como r
           +odar a suíte de regressão (`mvn test`, incluindo `RegressaoHi
           +storicaTest`/`IntegracaoEnvelopeTest`).                      
      1325 +  - **RN atendidas:** nenhuma nova — mesmo escopo de T-021, e
           +stendido.                                                    
      1326 +  - **DT/seções do plan:** plan §3, §16.                     
      1327 +  - **Dependências:** T-054 (execução real já verificada, par
           +a documentar comandos testados de fato).                     
      1328 +  - **Arquivos que cria/modifica:**                          
      1329 +    - `README.md`                                            
      1330 +  - **Passos de implementação:**                             
      1331 +    1. Atualizar a seção de execução do README, trocando o co
           +mando de duas flags pelo de quatro, em linhas separadas (sem 
           +`&&`), com caminhos relativos.                               
      1332 +    2. Acrescentar uma seção curta explicando `politica.json`
           +/`cambio.json` (o que são, onde encontrar exemplos — `exemplo
           +s/envelope/`) e os códigos de saída atualizados.             
      1333 +    3. Seguir o próprio README do zero, manualmente, conferin
           +do que os comandos documentados produzem os totais esperados.
      1334 +  - **Testes obrigatórios:** nenhum automatizado — verificaçã
           +o manual, seguindo o README do zero (mesmo critério de T-021)
           +.                                                            
      1335 +  - **Critério de conclusão:** seguindo somente o README, os 
           +quatro comandos de execução (um por cenário financeiro) produ
           +zem os totais corretos, e `mvn test` roda a suíte inteira sem
           + falhas.                                                     
      1336 +  - **Comando de verificação:**                              
      1337 +    ```                                                      
      1338 +    mvn -q package                                           
      1339 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/despesas-exemplo.json --output resultado.json --politic
           +a tests/resources/fixtures/politica-historica.json --cambio t
           +ests/resources/fixtures/cambio-historico.json                
      1340 +    mvn -q test                                              
      1341 +    ```                                                      
      1342 +  - **Commit sugerido:** `docs(readme): [T-057] documenta CLI
           + de quatro flags e politica/cambio externos`                 
      1343 +  - **Status:** [ ] pendente                                 
      1344 +                                                             
      1345 +- [ ] **T-058** — Revisão final de rastreabilidade e confirma
           +ção do item C fora de escopo                                 
      1346 +  - **O que faz:** revisão documental de fechamento do Dia 2:
           + confere, identificador por identificador, que RN-019 a RN-02
           +2 e CA-024 a CA-049 aparecem em pelo menos uma task deste arq
           +uivo (matriz ao final deste documento) e em pelo menos um tes
           +te executável; confirma, por leitura de `spec.md` §3/AMB-033 
           +e `plan.md` §20, que o item C (fila de aprovação manual) perm
           +anece fora de escopo — nenhum código, task ou teste o antecip
           +a. Task documental, sem código de produção.                  
      1347 +  - **RN atendidas:** nenhuma nova — é auditoria de rastreabi
           +lidade.                                                      
      1348 +  - **DT/seções do plan:** plan §17 (matriz de rastreabilidad
           +e), §20.                                                     
      1349 +  - **Dependências:** todas as tasks anteriores desta fase (T
           +-022 a T-057).                                               
      1350 +  - **Arquivos que cria/modifica:**                          
      1351 +    - `specs/001-motor-reembolso/tasks.md` (só a matriz de co
           +bertura ao final do arquivo, se algum identificador precisar 
           +de correção — a matriz já é criada nesta mesma revisão de pla
           +nejamento, então esta task é sobretudo conferência).         
      1352 +  - **Passos de implementação:**                             
      1353 +    1. Percorrer a matriz "Cobertura — RN-019 a RN-022" e "Co
           +bertura — CA-024 a CA-049" (ao final deste arquivo) e confirm
           +ar, para cada linha, que a task e o teste citados realmente e
           +xistem e passam.                                             
      1354 +    2. Confirmar, por leitura, que nenhuma task deste arquivo
           + criou `Decisao.AGUARDANDO_APROVACAO`, fila de aprovação, ou 
           +qualquer menção a "R$500" como gatilho de comportamento.     
      1355 +  - **Testes obrigatórios:** nenhum novo — a suíte completa (
           +`mvn test`) já cobre todos os identificadores citados na matr
           +iz.                                                          
      1356 +  - **Critério de conclusão:** matriz de cobertura sem lacuna
           +s; `git grep -i "AGUARDANDO_APROVACAO"` não encontra nenhuma 
           +ocorrência em `src/main/java`.                               
      1357 +  - **Comando de verificação:**                              
      1358 +    ```                                                      
      1359 +    mvn -q test                                              
      1360 +    git grep -i "AGUARDANDO_APROVACAO"                       
      1361 +    ```                                                      
      1362 +  - **Commit sugerido:** `docs(tasks): [T-058] confirma rastr
           +eabilidade completa do Dia 2 e item C fora de escopo`        
      1363 +  - **Status:** [ ] pendente                                 
      1364 +                                                             
      1365 +---                                                          
      1366 +                                                             
      1367  ## Cobertura — RN-001 a RN-018
      1368  
      1369  | Regra | Task dona | Teste |

● Now let me append the required coverage matrices at the end of the file.

  Read 1 file (ctrl+o to expand)

● Now I'll append the new coverage matrices (RN-019–022, CA-024–049, DT-011–019,
  and the final consolidated matrix) at the end of the file.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 85 lines
      1429  | DT-008 | T-003 | `VocabularioMotivoTest` |
      1430  | DT-009 | T-002 (convenção `*Test` adotada desde o primeiro 
            teste), T-020 (`mvn test` executa toda a suíte, inclusive int
            egração) | Suíte completa executada por `mvn test` |
      1431  | DT-010 | T-019 | `EscritaAtomicaSaidaTest` |
      1432 +                                                             
      1433 +---                                                          
      1434 +                                                             
      1435 +## Fase 8 (Dia 2) — Matrizes de cobertura                    
      1436 +                                                             
      1437 +> As três tabelas abaixo são conferidas identificador por ide
           +ntificador (não por amostragem) — regra explícita desta rodad
           +a: "não afirme cobertura total sem conferir identificador por
           + identificador". `PoliticaReembolso.java`/`AgregadorTetoHospe
           +dagem.java` permanecem em uso até T-055/T-056 (migração e rem
           +oção controladas, `CLAUDE.md` §6) — por isso aparecem como de
           +pendência em várias tasks intermediárias sem que isso seja um
           +a inconsistência.                                            
      1438 +                                                             
      1439 +## Cobertura — RN-019 a RN-022                               
      1440 +                                                             
      1441 +| Regra | Task(s) dona(s) | Teste(s) |                       
      1442 +|---|---|---|                                                
      1443 +| RN-019 | T-025, T-026, T-028 (modelos) · T-040 (`ResolutorP
           +oliticaCentroCusto`) · T-041, T-042 (avaliador + wiring) · T-
           +043 a T-046 (tetos por periodicidade) | `TabelaCategoriaTest`
           +, `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentroCust
           +oTest`, `CategoriaCentroCustoTest`, `TetoPorPeriodicidadeTest
           +`, `AgregadorTetoIndividualTest` |                           
      1444 +| RN-020 | T-027 (`TabelaCambio`) · T-032, T-033 (`LeitorCamb
           +io`) · T-036 (campo `moeda`) · T-037, T-038, T-039 (`Resoluto
           +rCambio` + integração) | `TabelaCambioTest`, `LeitorCambioTes
           +t`, `CampoMoedaTest`, `ResolucaoCambioTest`, `ConversaoCambia
           +lIntegracaoTest`, `MoedaSemCotacaoTest` |                    
      1445 +| RN-021 | T-026 (`PoliticaExterna.vigencia`) · T-030 (valida
           +ção de `vigencia` no `LeitorPolitica`) | `PoliticaExternaTest
           +`, `LeitorPoliticaTest` |                                    
      1446 +| RN-022 | T-030, T-031 (`LeitorPolitica`) · T-032, T-033 (`L
           +eitorCambio`) · T-035 (CLI trata falha como exit `2`) | `Leit
           +orPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |     
      1447 +                                                             
      1448 +## Cobertura — CA-024 a CA-049                               
      1449 +                                                             
      1450 +| Critério | Task(s) | Teste(s) |                            
      1451 +|---|---|---|                                                
      1452 +| CA-024 | T-040, T-041 | `ResolutorPoliticaCentroCustoTest`,
           + `CategoriaCentroCustoTest` |                                
      1453 +| CA-025 | T-040, T-041 | `ResolutorPoliticaCentroCustoTest`,
           + `CategoriaCentroCustoTest` |                                
      1454 +| CA-026 | T-040, T-041 | `ResolutorPoliticaCentroCustoTest`,
           + `CategoriaCentroCustoTest` |                                
      1455 +| CA-027 | T-040, T-041, T-046 (confirmação ponta a ponta) | 
           +`ResolutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest
           +`, `CliContratoTest` |                                       
      1456 +| CA-028 | T-038 | `RegraViagemEfeitoNuloTest` (estendido) | 
      1457 +| CA-029 | T-037 | `ResolucaoCambioTest` |                   
      1458 +| CA-030 | T-037 | `ResolucaoCambioTest` |                   
      1459 +| CA-031 | T-038 | `ConversaoCambialIntegracaoTest` |        
      1460 +| CA-032 | T-038 | `ConversaoCambialIntegracaoTest` |        
      1461 +| CA-033 | T-047 | `DuplicidadeEntreMoedasTest` |            
      1462 +| CA-034 | T-048 (modelo), T-049 (serialização) | `SaidaCambi
           +oTest`, `EscritorResultadoTest` |                            
      1463 +| CA-035 | T-030 | `LeitorPoliticaTest` |                    
      1464 +| CA-036 | T-030, T-031 (política), T-032, T-033 (câmbio), T-
           +035 (CLI) | `LeitorPoliticaTest`, `LeitorCambioTest`, `CliCon
           +tratoTest` |                                                 
      1465 +| CA-037 | T-050 | `RegressaoHistoricaTest` |                
      1466 +| CA-038 | T-051 | `RegressaoHistoricaTest` |                
      1467 +| CA-039 | T-052 | `IntegracaoEnvelopeTest` |                
      1468 +| CA-040 | T-053 | `IntegracaoEnvelopeTest` |                
      1469 +| CA-041 | T-034 | `CliContratoTest` |                       
      1470 +| CA-042 | T-034 | `CliContratoTest` |                       
      1471 +| CA-043 | T-035 | `CliContratoTest` |                       
      1472 +| CA-044 | T-035 | `CliContratoTest`, `EscritaAtomicaSaidaTes
           +t` |                                                         
      1473 +| CA-045 | T-030, T-031 | `LeitorPoliticaTest` |             
      1474 +| CA-046 | T-032, T-033 | `LeitorCambioTest` |               
      1475 +| CA-047 | T-043, T-045 | `TetoPorPeriodicidadeTest` |       
      1476 +| CA-048 | T-036 | `CampoMoedaTest` |                        
      1477 +| CA-049 | T-044, T-045 | `AgregadorTetoIndividualTest`, `Tet
           +oPorPeriodicidadeTest` |                                     
      1478 +                                                             
      1479 +Todo identificador de `RN-019` a `RN-022` e de `CA-024` a `CA
           +-049` aparece em pelo menos uma linha das duas tabelas acima 
           +— conferido linha a linha ao escrever esta seção (a mesma con
           +ferência é reexecutada em T-058, depois que as tasks tiverem 
           +sido implementadas, sobre o estado real do código e da suíte,
           + não só sobre o planejamento).                               
      1480 +                                                             
      1481 +## Cobertura — DT-011 a DT-019                               
      1482 +                                                             
      1483 +| Decisão técnica | Task(s) | Evidência de materialização pla
           +nejada |                                                     
      1484 +|---|---|---|                                                
      1485 +| DT-011 | T-026, T-028, T-040 | `PoliticaExternaTest`, `Tabe
           +laPoliticaResolvidaTest`, `ResolutorPoliticaCentroCustoTest` 
           +|                                                            
      1486 +| DT-012 | T-030 | `LeitorPoliticaTest` |                    
      1487 +| DT-013 | T-027, T-032 | `TabelaCambioTest`, `LeitorCambioTe
           +st` |                                                        
      1488 +| DT-014 | T-029, T-036 | `ItemValidadoCambioTest`, `CampoMoe
           +daTest` |                                                    
      1489 +| DT-015 | T-037, T-038 | `ResolucaoCambioTest` (produto sem 
           +arredondar), `ConversaoCambialIntegracaoTest` (canário `1.005
           + × 1.005`) |                                                 
      1490 +| DT-016 | T-040 | `ResolutorPoliticaCentroCustoTest` (compar
           +ação textual exata) |                                        
      1491 +| DT-017 | T-043, T-044, T-045 | `TetoPorPeriodicidadeTest`, 
           +`AgregadorTetoIndividualTest` |                              
      1492 +| DT-018 | T-034 | `CliContratoTest` |                       
      1493 +| DT-019 | T-022, T-023, T-024, T-048 | `VocabularioMotivoTes
           +t`, `OrdemMotivosTest` |                                     
      1494 +                                                             
      1495 +## Matriz final — RN/CA do Dia 2 → Tasks → Testes previstos  
      1496 +                                                             
      1497 +| RN/CA | Tasks responsáveis | Testes previstos |            
      1498 +|---|---|---|                                                
      1499 +| RN-019 | T-025, T-026, T-028, T-040, T-041, T-042, T-043, T
           +-044, T-045, T-046 | `TabelaCategoriaTest`, `TabelaPoliticaRe
           +solvidaTest`, `ResolutorPoliticaCentroCustoTest`, `CategoriaC
           +entroCustoTest`, `TetoPorPeriodicidadeTest`, `AgregadorTetoIn
           +dividualTest`, `CliContratoTest` |                           
      1500 +| RN-020 | T-027, T-029, T-032, T-033, T-036, T-037, T-038, T
           +-039 | `TabelaCambioTest`, `LeitorCambioTest`, `ItemValidadoC
           +ambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`, `Convers
           +aoCambialIntegracaoTest`, `MoedaSemCotacaoTest` |            
      1501 +| RN-021 | T-026, T-030 | `PoliticaExternaTest`, `LeitorPolit
           +icaTest` |                                                   
      1502 +| RN-022 | T-030, T-031, T-032, T-033, T-035 | `LeitorPolitic
           +aTest`, `LeitorCambioTest`, `CliContratoTest` |              
      1503 +| CA-024 a CA-027 | T-040, T-041, T-046 | `ResolutorPoliticaC
           +entroCustoTest`, `CategoriaCentroCustoTest`, `CliContratoTest
           +` |                                                          
      1504 +| CA-028 a CA-034 | T-037, T-038, T-047, T-048, T-049 | `Reso
           +lucaoCambioTest`, `ConversaoCambialIntegracaoTest`, `RegraVia
           +gemEfeitoNuloTest`, `DuplicidadeEntreMoedasTest`, `SaidaCambi
           +oTest`, `EscritorResultadoTest` |                            
      1505 +| CA-035, CA-036 | T-030, T-031, T-032, T-033, T-035 | `Leito
           +rPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |      
      1506 +| CA-037, CA-038 | T-050, T-051 | `RegressaoHistoricaTest` | 
      1507 +| CA-039, CA-040 | T-052, T-053 | `IntegracaoEnvelopeTest` | 
      1508 +| CA-041 a CA-044 | T-034, T-035 | `CliContratoTest`, `Escrit
           +aAtomicaSaidaTest` |                                         
      1509 +| CA-045, CA-046 | T-030, T-031, T-032, T-033 | `LeitorPoliti
           +caTest`, `LeitorCambioTest` |                                
      1510 +| CA-047 | T-043, T-045 | `TetoPorPeriodicidadeTest` |       
      1511 +| CA-048 | T-036 | `CampoMoedaTest` |                        
      1512 +| CA-049 | T-044, T-045 | `AgregadorTetoIndividualTest`, `Tet
           +oPorPeriodicidadeTest` |                                     
      1513 +                                                             
      1514 +**Remoção do legado (`CLAUDE.md` §6):** `PoliticaReembolso.ja
           +va` e `AgregadorTetoHospedagem.java` são migrados em T-055 e 
           +removidos em T-056, com verificação por `git grep` nos comand
           +os de ambas as tasks — nenhuma das duas classes é tocada ante
           +s de todos os consumidores terem migrado.                    
      1515 +                                                             
      1516 +**Item C:** confirmado fora de escopo em T-058, sem nenhuma t
           +ask deste arquivo antecipando `AGUARDANDO_APROVACAO` ou qualq
           +uer comportamento de fila de aprovação — consistente com `spe
           +c.md` §3/AMB-033 e `plan.md` §20.                            

● Now let's run the mandated verification commands.

  Ran 3 shell commands (ctrl+o to expand)

● Good — only tasks.md touched, no whitespace-check errors (the CRLF note is
  just a line-ending normalization notice, not an error). Now confirm the other
  tracked files are untouched.

  Ran 1 shell command (ctrl+o to expand)

● All empty — nothing outside tasks.md changed. Let me do a final sanity check
  on the numbering continuity and old-task preservation.

  Searched for 1 pattern (ctrl+o to expand)

● No gaps or duplicates: T-001 through T-058, continuous. Let me also verify the
  checkbox headers for T-022–T-058 are all pending and T-001–T-021 remain [x].

  Searched for 1 pattern (ctrl+o to expand)
● All 37 new tasks (T-022–T-058) are pending, and all 21 original tasks remain
  marked [x] concluída. Verification complete.

  Resumo final

  1. Versão/status de tasks.md: 1.1 · em revisão · última alteração 2026-08-05 ·
  baseado em spec 1.2 (aprovada) e plan 1.1 (aprovado).
  2. Intervalo das novas tasks: T-022 a T-058 (contínuo, sem lacuna nem
  duplicata).
  3. Quantidade de novas tasks: 37.
  4. Blocos e dependências: A) vocabulário/modelos → B) LeitorPolitica → C)
  LeitorCambio → D) CLI 4 flags → E) campo moeda → F) conversão cambial → G)
  política por centro de custo → H) tetos por periodicidade → I)
  duplicidade/saída → J) regressões e integração (4 cenários financeiros) → K)
  remoção do legado + documentação. Cada bloco depende dos modelos do Bloco A;
  wiring no Main fica sempre em tasks separadas da implementação da classe, para
  não misturar "implementação grande" com "toda a integração".
  5. Remoção de PoliticaReembolso/AgregadorTetoHospedagem: T-055 (migra
  consumidores) e T-056 (remove + verifica via git grep) — nenhuma remoção antes
  da migração completa, conforme CLAUDE.md §6.
  6. Cobertura RN-019–022 / CA-024–049: confirmada identificador por
  identificador nas três matrizes novas ao final do arquivo — nenhuma lacuna.
  7. git status --short:
   M specs/001-motor-reembolso/tasks.md

  Nenhum outro arquivo foi tocado (spec.md, plan.md, DECISIONS.md, README.md,
  src/, tests/, exemplos/ — todos com diff vazio); git diff --check sem erros
  reais; nenhum /export, git add, commit ou push foi executado. Fica parado para
  sua revisão.

✻ Cooked for 14m 8s

※ recap: Updating tasks.md to plan Day 2 (política v4) work: added 37 pending tasks, T-022–T-058, covering the full spec/plan scope with coverage matrices. Everything's verified and staged for your review — no commits made yet. (disable recaps in /config)

> Faça uma correção final de consistência operacional no backlog do Dia 2.      
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Não altere:                                                                   
                                                                                
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - README.md;                                                                  
  - código;                                                                     
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Mantenha:                                                                     
                                                                                
  - versão 1.1;                                                                 
  - status em revisão;                                                          
  - data 2026-08-05;                                                            
  - base spec 1.2 aprovada;                                                     
  - base plan 1.1 aprovado;                                                     
  - T-001 a T-021 integralmente intactas;                                       
  - T-022 a T-058;                                                              
  - 58 tasks no total;                                                          
  - 37 tasks novas;                                                             
  - todas as tasks T-022 a T-058 pendentes.                                     
                                                                                
  Não crie, remova ou renumere tasks.                                           
                                                                                
  Não execute /export, git add, commit ou push.                                 
                                                                                
  ## 1. Regra de um commit por task                                             
                                                                                
  Mantenha a estimativa:                                                        
                                                                                
  Dia 2: 37 tasks e 37 commits.                                                 
                                                                                
  Acrescente, na regra geral, uma qualificação específica para T-022 em diante: 
                                                                                
  - tasks explicitamente dedicadas a testes, regressão, auditoria ou            
  verificação manual podem produzir um commit de teste/documentação sem         
  alteração de produção;                                                        
  - isso não é um “teste seco” omitido da task anterior, porque cada uma cobre  
  um comportamento, fronteira ou evidência própria declarada no backlog;        
  - caso uma task de teste encontre um defeito, código e teste entram juntos no 
  único commit daquela task;                                                    
  - nesse caso, a mensagem pode mudar de `test(T-NNN)` para `fix(T-NNN)`;       
  - nunca criar um commit de correção seguido por outro commit de teste para a  
  mesma task;                                                                   
  - toda task, inclusive verificação manual, gera um commit contendo ao menos:  
    - atualização do checkbox/status da task;                                   
    - export da sessão;                                                         
    - qualquer evidência ou arquivo alterado aplicável.                         
                                                                                
  Remova de T-039 a possibilidade de:                                           
                                                                                
  fix(T-039) seguido de test(T-039)                                             
                                                                                
  Substitua por um único commit:                                                
                                                                                
  - `test(T-039)` se somente testes forem adicionados;                          
  - `fix(T-039)` se código e testes precisarem ser corrigidos.                  
                                                                                
  Remova de T-054 a possibilidade de concluir sem commit.                       
                                                                                
  ## 2. T-025 — fechar o tipo de TabelaCategoria                                
                                                                                
  Não deixe:                                                                    
                                                                                
  record ou classe imutável                                                     
                                                                                
  Adote definitivamente:                                                        
                                                                                
  public record TabelaCategoria(                                                
      BigDecimal limite,                                                        
      Periodicidade periodicidade                                               
  )                                                                             
                                                                                
  O construtor compacto deve rejeitar nulos.                                    
                                                                                
  ## 3. T-027 — devolver taxa e data da cotação                                 
                                                                                
  Substitua a API aberta que devolve somente BigDecimal/Optional<BigDecimal).   
                                                                                
  Adote definitivamente, dentro de TabelaCambio:                                
                                                                                
  public record CotacaoResolvida(                                               
      LocalDate data,                                                           
      BigDecimal taxa                                                           
  ) {}                                                                          
                                                                                
  public Optional<CotacaoResolvida> cotacaoEm(                                  
      String moeda,                                                             
      LocalDate dataDespesa                                                     
  )                                                                             
                                                                                
  O método usa floorEntry e devolve conjuntamente:                              
                                                                                
  - a data da entrada encontrada;                                               
  - a taxa correspondent                                                        
  ──── (283 lines hidden) ───────────────────────────────────────────────────── 
  :                                                                             
                                                                                
  - 585.43;                                                                     
  - 351.43;                                                                     
  - 1143.26;                                                                    
  - 373.76.                                                                     
                                                                                
  Use os mesmos arquivos verificados em T-054.                                  
                                                                                
  Mantenha também:                                                              
                                                                                
  mvn -q package                                                                
  mvn -q test                                                                   
                                                                                
  ## 14. T-058 — grep escopado                                                  
                                                                                
  Substitua:                                                                    
                                                                                
  git grep -i "AGUARDANDO_APROVACAO"                                            
                                                                                
  por:                                                                          
                                                                                
  git grep -i "AGUARDANDO_APROVACAO" -- src/main/java                           
                                                                                
  O critério é ausência de implementação em produção.                           
                                                                                
  Menções históricas/documentais em spec.md, plan.md ou tasks.md não são        
  defeito.                                                                      
                                                                                
  ## 15. Matrizes                                                               
                                                                                
  Atualize as matrizes somente onde as correções acima alterarem responsáveis   
  ou testes:                                                                    
                                                                                
  - T-027 passa a fornecer CotacaoResolvida;                                    
  - T-041 cobre coexistência com CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO;       
  - T-049 passa a incluir ExemploCompletoTest e o fixture histórico;            
  - T-054 continua confirmação de CA-037 a CA-040 pelo JAR real.                
                                                                                
  Não altere RN, CA ou DT.                                                      
                                                                                
  Confirme novamente:                                                           
                                                                                
  - RN-001 a RN-022 cobertas;                                                   
  - CA-001 a CA-049 cobertos;                                                   
  - DT-001 a DT-019 cobertas;                                                   
  - T-001 a T-058 contínuas;                                                    
  - T-022 a T-058 pendentes.                                                    
                                                                                
  ## 16. Verificação final                                                      
                                                                                
  Procure e confirme que não restam estas expressões problemáticas:             
                                                                                
  BigDecimal` (ou `Optional<BigDecimal>                                         
  record` ou classe imutável                                                    
  validação completa de `observacao` fica em T-031                              
  valores padrão de BRL descritos em T-029 até T-037                            
  se `item.getMoeda() == null` como única condição                              
  ou `executarPipeline` diretamente                                             
  ou arquivo próprio                                                            
  nenhuma alteração em `despesas-exemplo-esperado.json`                         
  fix(T-039) seguido de `test(T-039)                                            
  ou nenhum commit                                                              
  a decisão default é manter                                                    
  git grep "PoliticaReembolso" sem pathspec                                     
  git grep "AgregadorTetoHospedagem" sem pathspec                               
  git grep -i "AGUARDANDO_APROVACAO" sem pathspec                               
                                                                                
  Execute:                                                                      
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- README.md                                                         
  git diff -- src                                                               
  git diff -- tests                                                             
                                                                                
  Confirme:                                                                     
                                                                                
  - somente tasks.md foi alterado;                                              
  - T-001 a T-021 permanecem byte a byte intactas;                              
  - T-022 a T-058 continuam contínuas e pendentes;                              
  - nenhuma outra task foi criada;                                              
  - nenhum arquivo de implementação/teste foi alterado;                         
  - nenhum /export, git add, commit ou push foi executado.                      
                                                                                
  Ao final, informe:                                                            
                                                                                
  1. API final de TabelaCambio;                                                 
  2. responsabilidades finais de ValidadorItem e ResolutorCambio;               
  3. migração do fixture histórico;                                             
  4. correção de T-039/T-041;                                                   
  5. quatro comandos de T-054;                                                  
  6. estratégia final de remoção do legado;                                     
  7. regra final de um commit por task;                                         
  8. git status --short.                                                        
                                                                                
  Pare para revisão.                                                            

  Read 1 file (ctrl+o to expand)

● I have the full current content. I'll now apply the corrections
  systematically, starting with the general commit rule.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines
      33  
      34  **Regra de commit único por task:** implementação e os testes q
          ue a acompanham entram **no mesmo commit**, e somente depois de
           `mvn test` passar por inteiro — nunca um commit `feat` seguido
           de um commit `test` seco seguido. Não se commitam testes falha
          ndo. `T-002` é a única exceção autorizada (dois commits, ver ta
          sk). Não criar commits artificiais só para engordar o `git log`
          .
      35  
      36 +**Qualificação específica para `T-022` em diante (Dia 2):** tas
         +ks explicitamente dedicadas a testes, regressão, auditoria ou v
         +erificação manual (ex.: T-031, T-033, T-039, T-045, T-050, T-05
         +4, T-058) podem produzir um commit de teste/documentação sem al
         +teração de código de produção — isso **não** é um "teste seco" 
         +omitido da task anterior, porque cada uma dessas tasks cobre um
         + comportamento, uma fronteira ou uma evidência própria, declara
         +da neste backlog, que a task anterior não cobria. Caso uma dess
         +as tasks encontre um defeito real durante a escrita dos testes,
         + código e teste entram juntos no **único** commit daquela task,
         + e a mensagem muda de `test(T-NNN)` para `fix(T-NNN)` — nunca u
         +m commit de correção seguido por outro commit de teste para a m
         +esma task (essa possibilidade, aberta para `T-015` na v1.0 dest
         +e arquivo, não se repete para nenhuma task do Dia 2). Toda task
         + desta fase, inclusive as de verificação manual, gera **exatame
         +nte um commit**, sem exceção — nunca "task concluída sem commit
         +" —, contendo no mínimo: a atualização do checkbox/status da ta
         +sk; o export da sessão que a executou; e qualquer evidência ou 
         +arquivo alterado aplicável ao escopo dela.                     
      37 +                                                               
      38  ---
      39  
      40  ## Fase 0 — Baseline

● Now T-025 (record fix for TabelaCategoria):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 14 lines, removed 1 line
      583      - `tests/java/com/desafio/reembolso/modelo/TabelaCategoria
           Test.java`
      584    - **Passos de implementação:**
      585      1. `Periodicidade`: enum com dois valores, `DIA` e `DIARIA
           ` — sem serialização JSON própria (é consumido internamente, n
           unca escrito na saída).
      586 -    2. `TabelaCategoria`: `record` ou classe imutável com `lim
          -ite` (`BigDecimal`) e `periodicidade` (`Periodicidade`), valid
          -ando no construtor que nenhum dos dois é nulo.                
      586 +    2. `TabelaCategoria`: adotado definitivamente como `record
          +`, sem alternativa em aberto:                                 
      587 +       ```java                                                
      588 +       public record TabelaCategoria(                         
      589 +           BigDecimal limite,                                 
      590 +           Periodicidade periodicidade                        
      591 +       )                                                      
      592 +       ```                                                    
      593 +       com construtor compacto que rejeita nulos:             
      594 +       ```java                                                
      595 +       public TabelaCategoria {                               
      596 +           Objects.requireNonNull(limite, "limite");          
      597 +           Objects.requireNonNull(periodicidade, "periodicidad
          +e");                                                          
      598 +       }                                                      
      599 +       ```                                                    
      600    - **Testes obrigatórios:** `TabelaCategoriaTest` — construçã
           o válida preserva os dois campos; `limite` ou `periodicidade` 
           nulos lançam `NullPointerException` (falha rápida, sem estado 
           parcial).
      601    - **Critério de conclusão:** teste novo verde; nenhuma outra
            classe referencia `TabelaCategoria` ainda (isso começa em T-0
           26).
      602    - **Comando de verificação:**

● Now T-027 — full rewrite for CotacaoResolvida:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 20 lines, removed 6 lines
      628    - **Status:** [ ] pendente
      629  
      630  - [ ] **T-027** — Criar `TabelaCambio`
      631 -  - **O que faz:** cria a estrutura imutável `TabelaCambio` (`
          -moedaBase: String`, `cotacoesPorMoeda: Map<String, NavigableMa
          -p<LocalDate, BigDecimal>>`), já na forma invertida de consulta
          - eficiente (moeda → data → taxa, DT-013). Nesta task o modelo 
          -é só a estrutura — a leitura/inversão a partir de `cambio.json
          -` é `LeitorCambio` (T-032).                                   
      631 +  - **O que faz:** cria a estrutura imutável `TabelaCambio` (`
          +moedaBase: String`, `cotacoesPorMoeda: Map<String, NavigableMa
          +p<LocalDate, BigDecimal>>`), já na forma invertida de consulta
          + eficiente (moeda → data → taxa, DT-013), com uma API de consu
          +lta fechada que devolve, num único objeto, a data da cotação e
          +fetivamente usada **e** a taxa correspondente — nunca a taxa i
          +solada, porque `data_cotacao_utilizada` (spec 4.3) é campo de 
          +auditoria própria e não pode ser recalculada fora do ponto de 
          +consulta que já sabe qual entrada foi usada. Nesta task o mode
          +lo é só a estrutura — a leitura/inversão a partir de `cambio.j
          +son` é `LeitorCambio` (T-032).                                
      632    - **RN atendidas:** RN-020.
      633    - **CA atendidos:** base estrutural para CA-029, CA-030, CA-
           046.
      634    - **DT/seções do plan:** DT-013; plan §7.
      635    - **Dependências:** nenhuma.
      636    - **Arquivos que cria/modifica:**
      637 -    - `src/main/java/com/desafio/reembolso/modelo/TabelaCambio
          -.java`                                                        
      637 +    - `src/main/java/com/desafio/reembolso/modelo/TabelaCambio
          +.java` (inclui o `record` aninhado `CotacaoResolvida` — nenhum
          + arquivo separado é criado só para ele)                       
      638      - `tests/java/com/desafio/reembolso/modelo/TabelaCambioTes
           t.java`
      639    - **Passos de implementação:**
      640      1. Construtor recebe `moedaBase` e `Map<String, NavigableM
           ap<LocalDate, BigDecimal>>`, copiando defensivamente o mapa ex
           terno e cada `NavigableMap` interno para uma implementação imu
           tável (ex.: `Collections.unmodifiableNavigableMap` sobre uma c
           ópia em `TreeMap`).
      641 -    2. Método de consulta `cotacaoEm(String moeda, LocalDate d
          -ata): BigDecimal` (ou `Optional<BigDecimal>`) usando `Navigabl
          -eMap.floorEntry(data)` — devolve vazio/nulo quando a moeda não
          - existe no mapa ou quando `floorEntry` não encontra entrada (n
          -enhuma cotação anterior ou igual disponível).                 
      642 -  - **Testes obrigatórios:** `TabelaCambioTest` — cotação exat
          -a; fallback para a mais recente anterior; ausência de cotação 
          -anterior utilizável (moeda nunca cotada, ou só cotada em data 
          -posterior) devolve vazio; imutabilidade dos mapas internos e e
          -xterno.                                                       
      643 -  - **Critério de conclusão:** teste novo verde.              
      641 +    2. `record` aninhado, público e definitivo, sem alternativ
          +a em aberto:                                                  
      642 +       ```java                                                
      643 +       public record CotacaoResolvida(                        
      644 +           LocalDate data,                                    
      645 +           BigDecimal taxa                                    
      646 +       ) {}                                                   
      647 +       ```                                                    
      648 +    3. Método de consulta definitivo, também sem alternativa e
          +m aberto:                                                     
      649 +       ```java                                                
      650 +       public Optional<CotacaoResolvida> cotacaoEm(           
      651 +           String moeda,                                      
      652 +           LocalDate dataDespesa                              
      653 +       )                                                      
      654 +       ```                                                    
      655 +       Implementação: busca o `NavigableMap<LocalDate, BigDeci
          +mal>` de `moeda` em `cotacoesPorMoeda` — se ausente, `Optional
          +.empty()`; caso contrário, `NavigableMap.floorEntry(dataDespes
          +a)` — se `null` (nenhuma cotação igual ou anterior disponível,
          + inclusive quando a única cotação existente é posterior a `dat
          +aDespesa`), `Optional.empty()`; caso contrário, `Optional.of(n
          +ew CotacaoResolvida(entry.getKey(), entry.getValue()))`, com o
          +s dois valores vindos da **mesma** entrada — nunca recalculado
          +s separadamente.                                              
      656 +  - **Testes obrigatórios:** `TabelaCambioTest` — cotação exat
          +a devolve `CotacaoResolvida` com a **própria data** da despesa
          +; fallback devolve a **data anterior efetivamente usada** (não
          + a data da despesa consultada) junto com a taxa correspondente
          +; cotação exclusivamente futura (a única cotação disponível pa
          +ra a moeda é posterior à data consultada) devolve `Optional.em
          +pty()`; moeda ausente de `cotacoesPorMoeda` devolve `Optional.
          +empty()`; o mapa externo e cada `NavigableMap` interno são imu
          +táveis (tentativa de modificação lança `UnsupportedOperationEx
          +ception`).                                                    
      657 +  - **Critério de conclusão:** os cinco cenários acima verdes.
      658    - **Comando de verificação:**
      659      ```
      660      mvn -q test -Dtest=TabelaCambioTest
      661      ```
      648 -  - **Commit sugerido:** `feat(T-027): cria TabelaCambio com c
          -onsulta por floorEntry`                                       
      662 +  - **Commit sugerido:** `feat(T-027): cria TabelaCambio com C
          +otacaoResolvida via floorEntry`                               
      663    - **Status:** [ ] pendente
      664  
      665  - [ ] **T-028** — Criar `TabelaPoliticaResolvida`

● Now T-030 (full validation including observacao) and T-031 (reframed as
  exhaustive-testing task):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 4 lines, removed 4 lines
      723      - `tests/resources/fixtures/politica-valida-teste.json` (f
           ixture mínima válida, para o caso de sucesso)
      724    - **Passos de implementação:**
      725      1. `ler(Path caminho): PoliticaExterna` — abre o arquivo, 
           faz parsing sintático com `ObjectMapper` (`USE_BIG_DECIMAL_FOR
           _FLOATS`, mesma técnica de DT-005), captura `IOException`/`Jso
           nProcessingException` e relança como `PoliticaInvalidaExceptio
           n`.
      726 -    2. Validar, nesta ordem, os pontos 1 a 13 de `plan.md` §5 
          -(raiz objeto; `vigencia` obrigatória/formato/data real; `moeda
          -_base` exatamente `"BRL"`; `nota_fiscal_obrigatoria_acima_de` 
          -obrigatório/numérico/não negativo; `padrao`/`centros_custo` ob
          -rigatórios e objetos; cada tabela de `centros_custo` é objeto;
          - nome de categoria não vazio; configuração de categoria é obje
          -to; `limite` obrigatório/numérico, estritamente `> 0` em `padr
          -ao` e `>= 0` em `centros_custo`; `periodicidade` obrigatória e
          - exatamente `"dia"`/`"diaria"`).                              
      726 +    2. Validar, nesta ordem, os **dezesseis** pontos de `plan.
          +md` §5 — a validação completa e integral, sem nenhum ponto adi
          +ado para outra task: raiz objeto; `vigencia` obrigatória/forma
          +to/data real; `moeda_base` exatamente `"BRL"`; `nota_fiscal_ob
          +rigatoria_acima_de` obrigatório/numérico/não negativo; `padrao
          +`/`centros_custo` obrigatórios e objetos; cada tabela de `cent
          +ros_custo` é objeto; nome de categoria não vazio; configuração
          + de categoria é objeto; `limite` obrigatório/numérico, estrita
          +mente `> 0` em `padrao` e `>= 0` em `centros_custo`; `periodic
          +idade` obrigatória e exatamente `"dia"`/`"diaria"`; `observaca
          +o`, dentro de uma configuração de categoria — **ausente** (vál
          +ido), **presente como texto** (válido, lido e descartado, nunc
          +a populado em `TabelaCategoria`), **presente com qualquer outr
          +o tipo** — número, booleano, lista, objeto — (política estrutu
          +ralmente inválida); campos verdadeiramente desconhecidos — na 
          +raiz (`versao`, `acrescimo_em_viagem_percentual`) e dentro de 
          +uma configuração de categoria (além de `limite`/`periodicidade
          +`/`observacao`) — continuam ignorados sem invalidar o arquivo;
          + cópias defensivas imutáveis do modelo final (ponto 16).      
      727      3. Construir `PoliticaExterna` só depois de toda a validaç
           ão passar — nenhuma `TabelaCategoria` é criada antes da valida
           ção completa terminar.
      728 -    4. Campos desconhecidos na raiz (`versao`, `acrescimo_em_v
          -iagem_percentual`) e dentro de uma configuração de categoria (
          -além de `limite`/`periodicidade`/`observacao`) são lidos e ign
          -orados, sem invalidar o arquivo (pontos 14 item "campos verdad
          -eiramente desconhecidos"/15 de `plan.md` §5 — a validação comp
          -leta de `observacao` fica em T-031).                          
      729 -  - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo in
          -existente; arquivo ilegível; JSON sintaticamente inválido; rai
          -z não objeto; `vigencia` ausente/tipo errado/formato errado/da
          -ta inexistente; `moeda_base` diferente de `"BRL"`; `padrao` au
          -sente ou não objeto; `centros_custo` ausente ou não objeto; `n
          -ota_fiscal_obrigatoria_acima_de` ausente/negativo; categoria c
          -om `limite` ausente ou não numérico; `periodicidade` fora de `
          -"dia"`/`"diaria"`; um arquivo que satisfaz integralmente o con
          -trato é aceito e produz o `PoliticaExterna` esperado (CA-045).
      730 -  - **Critério de conclusão:** todos os cenários acima verdes;
          - nenhuma `TabelaCategoria` construída num arquivo rejeitado (v
          -erificável indiretamente pela exceção lançada antes de qualque
          -r retorno parcial).                                           
      728 +    4. Não há passo adiado: `observacao` e os campos desconhec
          +idos já estão integralmente cobertos no passo 2 — T-031 amplia
          + a matriz de testes sobre este mesmo comportamento, já impleme
          +ntado aqui, não introduz validação nova.                      
      729 +  - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo in
          +existente; arquivo ilegível; JSON sintaticamente inválido; rai
          +z não objeto; `vigencia` ausente/tipo errado/formato errado/da
          +ta inexistente; `moeda_base` diferente de `"BRL"`; `padrao` au
          +sente ou não objeto; `centros_custo` ausente ou não objeto; `n
          +ota_fiscal_obrigatoria_acima_de` ausente/negativo; categoria c
          +om `limite` ausente ou não numérico; `periodicidade` fora de `
          +"dia"`/`"diaria"`; `observacao` ausente (válido); `observacao`
          + como texto (válido e descartado); `observacao` como número/bo
          +oleano/lista (arquivo inválido); um arquivo que satisfaz integ
          +ralmente o contrato é aceito e produz o `PoliticaExterna` espe
          +rado (CA-045).                                                
      730 +  - **Critério de conclusão:** todos os cenários acima verdes,
          + inclusive os três de `observacao`; nenhuma `TabelaCategoria` 
          +construída num arquivo rejeitado (verificável indiretamente pe
          +la exceção lançada antes de qualquer retorno parcial).        
      731    - **Comando de verificação:**
      732      ```
      733      mvn -q test -Dtest=LeitorPoliticaTest

● Now T-031 reframing:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 4 lines, removed 5 lines
      736    - **Status:** [ ] pendente
      737  
      738  - [ ] **T-031** — Testes de fronteira do `LeitorPolitica`
      739 -  - **O que faz:** completa a cobertura de `plan.md` §5 que T-
          -030 deixou em aberto: `observacao` opcional dentro de uma conf
          -iguração de categoria (texto válido é lido e descartado do mod
          -elo; qualquer outro tipo invalida o arquivo), `limite: 0` em `
          -padrao` (inválido, arquivo inteiro rejeitado) versus `limite: 
          -0` numa tabela de `centros_custo` (estruturalmente válido, pro
          -duz `TabelaCategoria` com `limite = 0.00`), e a garantia de im
          -utabilidade/isolamento de `PoliticaExterna` a partir do `JsonN
          -ode`/mapas mutáveis intermediários do leitor (ponto 16 de `pla
          -n.md` §5).                                                    
      739 +  - **O que faz:** amplia exaustivamente a matriz de testes de
          + fronteira sobre comportamentos que `LeitorPolitica` (T-030) *
          +*já implementa integralmente** — `limite: 0` em `padrao` (invá
          +lido, arquivo inteiro rejeitado) versus `limite: 0` numa tabel
          +a de `centros_custo` (estruturalmente válido, produz `TabelaCa
          +tegoria` com `limite = 0.00`); campo desconhecido dentro de um
          +a configuração de categoria; e a garantia de imutabilidade/iso
          +lamento de `PoliticaExterna` a partir do `JsonNode`/mapas mutá
          +veis intermediários do leitor (ponto 16 de `plan.md` §5). Esta
          + task **não** introduz nenhuma validação normativa pela primei
          +ra vez — a validação de `observacao` e os demais quinze pontos
          + de `plan.md` §5 já estão fechados desde T-030; aqui só se com
          +prova exaustivamente esse comportamento já existente, sob mais
          + combinações de entrada.                                      
      740    - **RN atendidas:** RN-019, RN-022.
      741    - **CA atendidos:** CA-045.
      742 -  - **DT/seções do plan:** DT-011, DT-012; plan §5, pontos 14 
          -e 16.                                                         
      742 +  - **DT/seções do plan:** DT-011, DT-012; plan §5, pontos 11/
          +12 e 16.                                                      
      743    - **Dependências:** T-030.
      744    - **Arquivos que cria/modifica:**
      745      - `tests/java/com/desafio/reembolso/leitor/LeitorPoliticaT
           est.java` (mesma classe de T-030, casos adicionais)
      746    - **Passos de implementação:**
      747 -    1. Sem alteração de produção esperada nesta task — se algu
          -m dos cenários abaixo revelar um defeito real em `LeitorPoliti
          -ca`, corrigir `LeitorPolitica.java` **nesta mesma task**, ante
          -s do commit (mesma contingência de T-015: um defeito encontrad
          -o na cobertura de fronteira não vira uma task nova).          
      748 -    2. Escrever os casos de teste listados abaixo.            
      749 -  - **Testes obrigatórios:** `LeitorPoliticaTest` — `observaca
          -o` ausente (válido); `observacao` como texto (válido, e o valo
          -r não aparece em nenhum campo de `TabelaCategoria`); `observac
          -ao` como número/booleano/lista (arquivo inválido); `limite: 0`
          - em `padrao` (arquivo inválido — CA-045 negativo); `limite: 0`
          - numa tabela de `centros_custo` (arquivo válido, `TabelaCatego
          -ria.getLimite() == 0.00`); campo desconhecido dentro de uma co
          -nfiguração de categoria (ignorado, arquivo válido); um `Politi
          -caExterna` construído a partir de um arquivo válido tem `padra
          -o`/`centrosCusto` imutáveis (tentativa de modificação via gett
          -er lança `UnsupportedOperationException`).                    
      747 +    1. Escrever os casos de teste listados abaixo, comprovando
          + o comportamento já implementado em T-030 — se algum cenário r
          +evelar um defeito real em `LeitorPolitica` (comportamento dife
          +rente do que T-030 já deveria garantir), corrigir `LeitorPolit
          +ica.java` e o teste **no mesmo e único commit** desta task, e 
          +nesse caso a mensagem passa de `test(T-031)` para `fix(T-031)`
          + — nunca dois commits para a mesma task (ver a qualificação de
          + commit único de tasks de teste, no topo deste arquivo).      
      748 +  - **Testes obrigatórios:** `LeitorPoliticaTest` — `limite: 0
          +` em `padrao` (arquivo inválido — CA-045 negativo); `limite: 0
          +` numa tabela de `centros_custo` (arquivo válido, `TabelaCateg
          +oria.getLimite() == 0.00`); campo desconhecido dentro de uma c
          +onfiguração de categoria (ignorado, arquivo válido); campo des
          +conhecido na raiz, além de `versao`/`acrescimo_em_viagem_perce
          +ntual` (ignorado, arquivo válido); um `PoliticaExterna` constr
          +uído a partir de um arquivo válido tem `padrao`/`centrosCusto`
          + imutáveis (tentativa de modificação via getter lança `Unsuppo
          +rtedOperationException`).                                     
      749    - **Critério de conclusão:** todos os cenários acima verdes;
            `mvn test` completo verde.
      750    - **Comando de verificação:**
      751      ```

● Now T-035 (InvalidPathException handling):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 5 lines, removed 5 lines
      830    - **Status:** [ ] pendente
      831  
      832  - [ ] **T-035** — `Main.java`: carregar e validar política e c
           âmbio antes do envelope
      833 -  - **O que faz:** `Main.run(...)` passa a chamar `LeitorPolit
          -ica.ler(politicaPath)` e `LeitorCambio.ler(cambioPath)` **ante
          -s** de ler e validar o envelope de despesas (spec 8.1, passo 1
          - antes do passo 2) — qualquer `PoliticaInvalidaException`/`Cam
          -bioInvalidoException` retorna exit `2`, sem sequer abrir o arq
          -uivo de entrada. Os objetos `PoliticaExterna`/`TabelaCambio` r
          -esultantes ainda não são usados pelo pipeline de regras nesta 
          -task (isso começa em T-038/T-042/T-046) — aqui o objetivo é só
          - o contrato de execução (AMB-034) e a ordem de validação.     
      833 +  - **O que faz:** `Main.run(...)` passa a chamar `LeitorPolit
          +ica.ler(politicaPath)` e `LeitorCambio.ler(cambioPath)` **ante
          +s** de ler e validar o envelope de despesas (spec 8.1, passo 1
          + antes do passo 2) — qualquer `PoliticaInvalidaException`/`Cam
          +bioInvalidoException` retorna exit `2`, sem sequer abrir o arq
          +uivo de entrada. A própria construção dos `Path` (`Path.of(pol
          +iticaPath)`, `Path.of(cambioPath)`, e também `Path.of(inputPat
          +h)`/`Path.of(outputPath)`) acontece **dentro** do mesmo bloco 
          +protegido — `Path.of(...)` pode lançar `InvalidPathException` 
          +quando o texto recebido não é um caminho válido no sistema ope
          +racional (ex.: caracteres proibidos no Windows), e esse cenári
          +o é tratado exatamente como as demais falhas de arquivo: exit 
          +`2`, mensagem apenas em stderr, stdout vazio, e um `--output` 
          +preexistente preservado intacto (nenhuma tentativa de escrita 
          +ocorre antes da validação de política/câmbio). Os objetos `Pol
          +iticaExterna`/`TabelaCambio` resultantes ainda não são usados 
          +pelo pipeline de regras nesta task (isso começa em T-038/T-042
          +/T-046) — aqui o objetivo é só o contrato de execução (AMB-034
          +) e a ordem de validação.                                     
      834    - **RN atendidas:** RN-021, RN-022.
      835    - **CA atendidos:** CA-043, CA-044.
      836    - **DT/seções do plan:** DT-003 (extensão), DT-010 (extensão
           ); plan §3.
     ...
      840      - `tests/java/com/desafio/reembolso/CliContratoTest.java`
      841      - `tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTes
           t.java`
      842    - **Passos de implementação:**
      843 -    1. Logo após extrair os quatro caminhos (T-034) e antes de
          - qualquer leitura do arquivo de entrada, chamar `LeitorPolitic
          -a.ler(Path.of(politicaPath))` e `LeitorCambio.ler(Path.of(camb
          -ioPath))` dentro de blocos `try/catch` que traduzem a exceção 
          -dedicada em `err.println(...)` + `return 2`.                  
      844 -    2. Preservar o restante do fluxo inalterado (leitura de `-
          --input`, `ValidadorEnvelope`, pipeline, escrita atômica) — a o
          -rdem "política/câmbio antes do envelope" é garantida só pela p
          -osição das duas chamadas novas no método.                     
      845 -  - **Testes obrigatórios:** `CliContratoTest` — `--politica` 
          -apontando para arquivo inexistente/ilegível/JSON inválido/estr
          -uturalmente inválido → exit `2`, mesmo com `--input` perfeitam
          -ente válido; mesmo conjunto de cenários para `--cambio`; um `-
          --output` preexistente permanece intacto byte a byte em qualque
          -r um desses cenários (estendendo `EscritaAtomicaSaidaTest` ou 
          -o próprio `CliContratoTest`, verificando conteúdo antes/depois
          -); stdout vazio e mensagem em stderr em todos os casos de falh
          -a.                                                            
      846 -  - **Critério de conclusão:** todos os cenários acima verdes;
          - `mvn test` completo verde.                                   
      843 +    1. Logo após extrair os quatro caminhos (T-034) e antes de
          + qualquer leitura do arquivo de entrada, envolver a construção
          + dos quatro `Path` **e** as chamadas a `LeitorPolitica.ler(...
          +)`/`LeitorCambio.ler(...)` no mesmo bloco `try/catch`, captura
          +ndo tanto `InvalidPathException` (da construção do `Path`) qua
          +nto `PoliticaInvalidaException`/`CambioInvalidoException` (da 
          +leitura), todos traduzidos em `err.println(...)` + `return 2`.
      844 +    2. Preservar o restante do fluxo inalterado (leitura de `-
          +-input`, `ValidadorEnvelope`, pipeline, escrita atômica) — a o
          +rdem "política/câmbio antes do envelope" é garantida só pela p
          +osição das chamadas novas no método.                          
      845 +  - **Testes obrigatórios:** `CliContratoTest` — `--politica` 
          +apontando para arquivo inexistente/ilegível/JSON inválido/estr
          +uturalmente inválido → exit `2`, mesmo com `--input` perfeitam
          +ente válido; mesmo conjunto de cenários para `--cambio`; `--po
          +litica` ou `--cambio` recebendo um valor que não é um caminho 
          +válido no sistema operacional (`InvalidPathException` na const
          +rução do `Path`) → exit `2`, stdout vazio, mensagem em stderr,
          + `--output` preexistente intacto; um `--output` preexistente p
          +ermanece intacto byte a byte em qualquer um desses cenários (e
          +stendendo `EscritaAtomicaSaidaTest` ou o próprio `CliContratoT
          +est`, verificando conteúdo antes/depois); stdout vazio e mensa
          +gem em stderr em todos os casos de falha.                     
      846 +  - **Critério de conclusão:** todos os cenários acima verdes,
          + inclusive o de `InvalidPathException`; `mvn test` completo ve
          +rde.                                                          
      847    - **Comando de verificação:**
      848      ```
      849      mvn -q test "-Dtest=CliContratoTest,EscritaAtomicaSaidaTes
           t"

● Now T-036 (full rewrite — exclusive responsibility, all production paths):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 9 lines, removed 7 lines
      856  ### Bloco E — Campo `despesa.moeda`
      857  
      858  - [ ] **T-036** — `validarMoeda` em `ValidadorItem`
      859 -  - **O que faz:** implementa a validação do sétimo campo do c
          -ontrato (spec 4.2, RN-002, DT-014): chave `moeda` ausente do o
          -bjeto → `"BRL"`, sem motivo; chave presente com valor `null` →
          - `CAMPO_AUSENTE`; tipo não textual → `CAMPO_TIPO_INVALIDO`; te
          -xto fora de `[A-Z]{3}` → `CAMPO_FORMATO_INVALIDO` (sem trim, s
          -em conversão de caixa). `ValidadorItem.validarItem(...)` passa
          - a chamar `validarMoeda` e a usar o construtor de catorze argu
          -mentos de `ItemValidado` (T-029) para popular `moeda`, mantend
          -o `taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertido
          -Bruto` com os valores padrão de BRL até `ResolutorCambio` (T-0
          -37) os recalcular.                                            
      859 +  - **O que faz:** implementa a validação do sétimo campo do c
          +ontrato (spec 4.2, RN-002, DT-014): chave `moeda` ausente do o
          +bjeto → `"BRL"`, sem motivo; chave presente com valor `null` →
          + `CAMPO_AUSENTE`; tipo não textual → `CAMPO_TIPO_INVALIDO`; te
          +xto fora de `[A-Z]{3}` → `CAMPO_FORMATO_INVALIDO` (sem trim, s
          +em conversão de caixa). A responsabilidade de `ValidadorItem` 
          +é **exclusivamente** popular `ItemValidado.moeda` — nunca os t
          +rês campos derivados de câmbio. Em **todo** caminho de produçã
          +o de `ValidadorItem` (objeto válido, objeto com campos inválid
          +os, e elemento que não é objeto), o construtor de catorze argu
          +mentos (T-029) passa a ser chamado explicitamente com `taxaCam
          +bioAplicada = null`, `dataCotacaoUtilizada = null` e `valorCon
          +vertidoBruto = null` — **inclusive para BRL**, informado ou as
          +sumido por ausência de chave: só `ResolutorCambio` (T-037) pre
          +enche os três derivados, mesmo para BRL (`taxaCambioAplicada =
          + 1`, `valorConvertidoBruto = valor`). Para o elemento que **nã
          +o é objeto** (`ITEM_TIPO_INVALIDO`), os quatro campos ficam to
          +dos nulos — `moeda = null`, `taxaCambioAplicada = null`, `data
          +CotacaoUtilizada = null`, `valorConvertidoBruto = null` —, nun
          +ca `"BRL"`, porque não há despesa estruturada da qual inferir 
          +moeda alguma (spec 4.2, tabela de "Elemento que não é objeto")
          +. O construtor de dez argumentos criado em T-029 permanece exi
          +stindo, mas **exclusivamente como compatibilidade para os test
          +es históricos** (T-001 a T-021) que ainda o chamam diretamente
          + — `ValidadorItem` não volta a usá-lo depois desta task.      
      860    - **RN atendidas:** RN-002 (7º campo).
      861    - **CA atendidos:** CA-048.
      862 -  - **DT/seções do plan:** DT-014; plan §8.                   
      862 +  - **DT/seções do plan:** DT-014; plan §8, §9 (divisão de res
          +ponsabilidade entre `ValidadorItem` e `ResolutorCambio`).     
      863    - **Dependências:** T-022 (`CampoCanonico.MOEDA`), T-029 (co
           nstrutor de catorze argumentos).
      864    - **Arquivos que cria/modifica:**
      865      - `src/main/java/com/desafio/reembolso/pipeline/ValidadorI
           tem.java`
      866      - `tests/java/com/desafio/reembolso/pipeline/CampoMoedaTes
           t.java`
      867    - **Passos de implementação:**
      868      1. Método privado `validarMoeda(JsonNode elemento, List<Mo
           tivo> motivos): String`, seguindo exatamente a ordem de `if` d
           e `plan.md` §8: `elemento.get("moeda") == null` (referência Ja
           va nula, chave ausente) → devolve `"BRL"` sem tocar `motivos`;
            `valor.isNull()` → adiciona `CAMPO_AUSENTE`/`CampoCanonico.MO
           EDA`, devolve `null`; tipo != `STRING` → `CAMPO_TIPO_INVALIDO`
           ; texto que não casa com o padrão `[A-Z]{3}` (regex, sem `trim
           ()`/`toUpperCase()`) → `CAMPO_FORMATO_INVALIDO`; caso contrári
           o devolve o texto como recebido.
      869 -    2. Chamar `validarMoeda` dentro de `validarItem`, na posiç
          -ão correspondente ao 7º campo (entre `valor` e `tem_nota_fisca
          -l`), e passar o resultado para o construtor de catorze argumen
          -tos de `ItemValidado` (os três campos de câmbio continuam com 
          -os valores padrão de BRL descritos em T-029 até T-037 os subst
          -ituir).                                                       
      870 -    3. Não alterar a ordem em que os motivos estruturais são a
          -dicionados à lista além da inserção de `moeda` na posição corr
          -eta — isso é o que faz a ordem canônica de contrato já refleti
          -r o 7º campo (a reordenação de apresentação em `CompositorSaid
          -a` é T-048).                                                  
      871 -  - **Testes obrigatórios:** `CampoMoedaTest` — os quatro cená
          -rios fechados (ausência de chave → `"BRL"`, sem motivo; `null`
          - explícito → `CAMPO_AUSENTE`; tipo não textual → `CAMPO_TIPO_I
          -NVALIDO`; formato fora de `[A-Z]{3}`, incluindo caixa baixa `"
          -usd"` → `CAMPO_FORMATO_INVALIDO`, sem correção automática de c
          -aixa).                                                        
      872 -  - **Critério de conclusão:** os quatro cenários verdes (CA-0
          -48); `mvn test` completo continua verde — nenhum teste históri
          -co que constrói despesas sem o campo `moeda` muda de resultado
          -, porque a ausência da chave já resolvia para o comportamento 
          -BRL implícito em T-029.                                       
      869 +    2. Chamar `validarMoeda` dentro de `validarItem`, na posiç
          +ão correspondente ao 7º campo (entre `valor` e `tem_nota_fisca
          +l`).                                                          
      870 +    3. No retorno de `validarItem` para um elemento **objeto**
          + (válido ou com campos inválidos), trocar a chamada do constru
          +tor de dez argumentos pelo de catorze, passando `moeda` (resul
          +tado de `validarMoeda`) e **explicitamente** `null` para `taxa
          +CambioAplicada`, `dataCotacaoUtilizada` e `valorConvertidoBrut
          +o` — nunca os valores de compatibilidade de BRL de T-029.     
      871 +    4. No retorno de `validarItem` para um elemento que **não 
          +é objeto** (`ITEM_TIPO_INVALIDO`), trocar a chamada do constru
          +tor de dez argumentos pelo de catorze, passando `moeda = null`
          + (não `"BRL"`) e os três derivados também `null` — reflete exa
          +tamente a tabela de `spec.md` 4.2 para essa situação.         
      872 +    5. Não alterar a ordem em que os motivos estruturais são a
          +dicionados à lista além da inserção de `moeda` na posição corr
          +eta — isso é o que faz a ordem canônica de contrato já refleti
          +r o 7º campo (a reordenação de apresentação em `CompositorSaid
          +a` é T-048).                                                  
      873 +  - **Testes obrigatórios:** `CampoMoedaTest` — os quatro cená
          +rios fechados de `moeda` (ausência de chave → `"BRL"`, sem mot
          +ivo; `null` explícito → `CAMPO_AUSENTE`; tipo não textual → `C
          +AMPO_TIPO_INVALIDO`; formato fora de `[A-Z]{3}`, incluindo cai
          +xa baixa `"usd"` → `CAMPO_FORMATO_INVALIDO`); **e**, adicional
          +mente: um item BRL assumido por ausência de chave tem `taxaCam
          +bioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto` tod
          +os `null` **antes** de `ResolutorCambio` rodar (prova de que `
          +ValidadorItem` não antecipa os derivados, nem para BRL); um it
          +em com moeda estrangeira estruturalmente válida (ex.: `"EUR"`)
          + também tem os três derivados `null` neste estágio; um element
          +o que não é objeto tem `moeda`, `taxaCambioAplicada`, `dataCot
          +acaoUtilizada` e `valorConvertidoBruto` todos `null`.         
      874 +  - **Critério de conclusão:** os quatro cenários de `moeda` v
          +erdes (CA-048), mais os três cenários novos de derivados nulos
          + verdes; `mvn test` completo continua verde — os testes histór
          +icos que constroem `ItemValidado` diretamente pelo construtor 
          +de dez argumentos (sem passar por `ValidadorItem`) continuam i
          +ntactos, porque a mudança é exclusivamente dentro de `Validado
          +rItem.validarItem`.                                           
      875    - **Comando de verificação:**
      876      ```
      877      mvn -q test -Dtest=CampoMoedaTest
      878      ```
      877 -  - **Commit sugerido:** `feat(T-036): implementa validarMoeda
          - no ValidadorItem`                                            
      879 +  - **Commit sugerido:** `feat(T-036): implementa validarMoeda
          + com uso exclusivo do construtor de catorze argumentos`       
      880    - **Status:** [ ] pendente
      881  
      882  ---

● Now T-037 (full rewrite — check valor/moeda/data, use CotacaoResolvida):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 8 lines, removed 8 lines
      884  ### Bloco F — Conversão cambial
      885  
      886  - [ ] **T-037** — Implementar `ResolutorCambio`
      887 -  - **O que faz:** cria o novo estágio de pipeline `ResolutorC
          -ambio` (spec 8.1, passo 5; plan §9), que consome `ItemValidado
          -.getMoeda()` (já resolvido por `ValidadorItem`, T-036) e uma `
          -TabelaCambio`, e devolve um novo `ItemValidado` com `taxaCambi
          -oAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto` recal
          -culados — **sem arredondar** `valorConvertidoBruto` (DT-015, o
          - arredondamento é responsabilidade exclusiva do `Normalizador`
          -, T-038). BRL: taxa `1`, data nula, `valorConvertidoBruto` igu
          -al ao valor original. Moeda estrangeira com cotação resolvida:
          - `valorConvertidoBruto = valor × taxa` (produto exato, sem `se
          -tScale`). Sem cotação utilizável: os três campos ficam nulos e
          - o motivo `MOEDA_SEM_COTACAO` (`campo = CampoCanonico.MOEDA`) 
          -é acrescentado aos motivos do item.                           
      887 +  - **O que faz:** cria o novo estágio de pipeline `ResolutorC
          +ambio` (spec 8.1, passo 5; plan §9), que consome os três campo
          +s de que RN-020 depende — `ItemValidado.getValor()`, `ItemVali
          +dado.getMoeda()` **e** `ItemValidado.getData()` (plan §9: "o e
          +stágio é avaliado quando os três campos de que RN-020 depende 
          +estiverem estruturalmente utilizáveis") — e uma `TabelaCambio`
          +, devolvendo um novo `ItemValidado` com `taxaCambioAplicada`/`
          +dataCotacaoUtilizada`/`valorConvertidoBruto` recalculados — **
          +sem arredondar** `valorConvertidoBruto` (DT-015, o arredondame
          +nto é responsabilidade exclusiva do `Normalizador`, T-038). Se
          + **qualquer um** dos três — `valor`, `moeda` ou `data` — for `
          +null`, o item é devolvido sem tentativa de resolução e sem mot
          +ivo novo: não basta checar só `moeda`, porque um item com `des
          +pesa.valor` ou `despesa.data` estruturalmente inválidos, mesmo
          + com `moeda` válida, também não tem como ser convertido. BRL: 
          +taxa `1`, data nula, `valorConvertidoBruto` igual ao valor ori
          +ginal. Moeda estrangeira com cotação resolvida (via `TabelaCam
          +bio.cotacaoEm(...)`, que devolve `Optional<TabelaCambio.Cotaca
          +oResolvida>`, T-027): `valorConvertidoBruto = valor × taxa` (p
          +roduto exato, sem `setScale`), com `taxaCambioAplicada`/`dataC
          +otacaoUtilizada` vindos da mesma `CotacaoResolvida`. Sem cotaç
          +ão utilizável: os três campos ficam nulos e o motivo `MOEDA_SE
          +M_COTACAO` (`campo = CampoCanonico.MOEDA`) é acrescentado aos 
          +motivos do item. Erro estrutural em `categoria`/`descricao`/`f
          +ornecedor`/`tem_nota_fiscal` **não** impede a conversão — só `
          +valor`, `moeda` e `data` são checados.                        
      888    - **RN atendidas:** RN-020.
      889    - **CA atendidos:** CA-029, CA-030.
      890    - **DT/seções do plan:** DT-013, DT-015; plan §9.
      891 -  - **Dependências:** T-023 (`MotivoCodigo.MOEDA_SEM_COTACAO`)
          -, T-024 (`RegraNegocio.RN_020`), T-027 (`TabelaCambio`), T-029
          - (`ItemValidado` de catorze argumentos), T-036 (`moeda` já pop
          -ulado).                                                       
      891 +  - **Dependências:** T-023 (`MotivoCodigo.MOEDA_SEM_COTACAO`)
          +, T-024 (`RegraNegocio.RN_020`), T-027 (`TabelaCambio.cotacaoE
          +m(...)`/`CotacaoResolvida`), T-029 (`ItemValidado` de catorze 
          +argumentos), T-036 (`moeda` já populado, derivados nulos).    
      892    - **Arquivos que cria/modifica:**
      893      - `src/main/java/com/desafio/reembolso/pipeline/ResolutorC
           ambio.java`
      894      - `tests/java/com/desafio/reembolso/pipeline/ResolucaoCamb
           ioTest.java`
      895    - **Passos de implementação:**
      896 -    1. `resolver(ItemValidado item, TabelaCambio cambio): Item
          -Validado` — se `item.getMoeda() == null` (moeda estruturalment
          -e inválida, já recusada por RN-002), devolve o item sem tentar
          - resolver câmbio (exclusão de dependência de campo inválido, s
          -pec 8.4 item 3).                                              
      897 -    2. Se `"BRL".equals(item.getMoeda())`, devolve uma cópia d
          -o item com `taxaCambioAplicada = BigDecimal.ONE`, `dataCotacao
          -Utilizada = null`, `valorConvertidoBruto = item.getValor()` — 
          -mesmo caminho para BRL informado e para BRL assumido por ausên
          -cia de chave (não há `if` separando os dois casos, porque `Val
          -idadorItem` já unificou os dois em `"BRL"`).                  
      898 -    3. Caso contrário, consulta `cambio.cotacaoEm(item.getMoed
          -a(), item.getData())` (T-027); se presente, `valorConvertidoBr
          -uto = item.getValor().multiply(taxa)` (sem `setScale`), `taxaC
          -ambioAplicada = taxa`, `dataCotacaoUtilizada = dataDaCotacaoEf
          -etivamenteUsada`; se ausente, os três campos ficam nulos e o m
          -otivo `MOEDA_SEM_COTACAO` é acrescentado à lista de motivos do
          - item (nova lista, item permanece imutável).                  
      896 +    1. `resolver(ItemValidado item, TabelaCambio cambio): Item
          +Validado` — se `item.getValor() == null` **ou** `item.getMoeda
          +() == null` **ou** `item.getData() == null`, devolve o item se
          +m alteração e sem motivo novo (os três campos derivados perman
          +ecem `null`, como `ValidadorItem`/T-036 já os deixou). Checar 
          +os três, nunca só `moeda`.                                    
      897 +    2. Se os três passaram no passo 1 e `"BRL".equals(item.get
          +Moeda())`, devolve uma cópia do item com `taxaCambioAplicada =
          + BigDecimal.ONE`, `dataCotacaoUtilizada = null`, `valorConvert
          +idoBruto = item.getValor()` — mesmo caminho para BRL informado
          + e para BRL assumido por ausência de chave.                   
      898 +    3. Caso contrário, `Optional<TabelaCambio.CotacaoResolvida
          +> cotacao = cambio.cotacaoEm(item.getMoeda(), item.getData())`
          +; se presente, `taxaCambioAplicada = cotacao.get().taxa()`, `d
          +ataCotacaoUtilizada = cotacao.get().data()`, `valorConvertidoB
          +ruto = item.getValor().multiply(cotacao.get().taxa())` (sem `s
          +etScale`); se `Optional.empty()`, os três campos derivados fic
          +am nulos e o motivo `MOEDA_SEM_COTACAO` é acrescentado à lista
          + de motivos do item (nova lista, item permanece imutável).    
      899      4. `resolverLista(List<ItemValidado> itens, TabelaCambio c
           ambio): List<ItemValidado>` — aplica `resolver` a cada item, p
           reservando ordem.
      900      5. Nenhuma chamada a `Main.java` ainda — o wiring no pipel
           ine é T-038.
      901 -  - **Testes obrigatórios:** `ResolucaoCambioTest` — BRL infor
          -mado e BRL por ausência de chave (idêntico); cotação exata na 
          -data da despesa; fallback para a cotação mais recente anterior
          - (CA-029, ex.: EUR de sábado sem cotação própria usa a de sext
          -a); proibição de cotação futura (data cuja única cotação dispo
          -nível é posterior — resultado é `MOEDA_SEM_COTACAO`, nunca a c
          -otação futura); moeda nunca presente em `cambio.json` → `MOEDA
          -_SEM_COTACAO` (CA-030); o motivo `MOEDA_SEM_COTACAO` carrega `
          -campo = CampoCanonico.MOEDA`; `valorConvertidoBruto` preserva 
          -a escala plena do produto, sem arredondamento — verificado dir
          -etamente comparando o `BigDecimal` resultante de `1.005 × 1.00
          -5` contra o literal exato `"1.010025"` (não contra um valor já
          - arredondado), o que isola a garantia de "nenhum arredondament
          -o neste estágio" da garantia de "um único arredondamento no fi
          -m", que só T-038 fecha.                                       
      902 -  - **Critério de conclusão:** todos os cenários acima verdes;
          - nenhuma chamada a `setScale`/`round` em `ResolutorCambio.java
          -` (confirmável por leitura, já que não há teste automatizado q
          -ue prove ausência de arredondamento em todos os caminhos possí
          -veis — DT-015).                                               
      901 +  - **Testes obrigatórios:** `ResolucaoCambioTest` — BRL infor
          +mado e BRL por ausência de chave (idêntico); cotação exata na 
          +data da despesa (`CotacaoResolvida.data()` devolvida é a própr
          +ia data da despesa); fallback para a cotação mais recente ante
          +rior (CA-029, ex.: EUR de sábado sem cotação própria usa a de 
          +sexta, verificando que `CotacaoResolvida.data()` é a data de s
          +exta, não a do sábado consultado); proibição de cotação futura
          +; moeda nunca presente em `cambio.json` → `MOEDA_SEM_COTACAO` 
          +(CA-030); `item.getValor() == null` (campo `valor` estruturalm
          +ente inválido) não causa `NullPointerException` — item devolvi
          +do sem resolução e sem motivo novo; `item.getData() == null` (
          +campo `data` estruturalmente inválido) não causa `NullPointerE
          +xception` — mesmo comportamento; erro estrutural em `categoria
          +`, `descricao`, `fornecedor` ou `tem_nota_fiscal` **não** impe
          +de a conversão — um item com esses campos inválidos, mas `valo
          +r`/`moeda`/`data` válidos, ainda tem os três derivados calcula
          +dos normalmente; o motivo `MOEDA_SEM_COTACAO` carrega `campo =
          + CampoCanonico.MOEDA`; `valorConvertidoBruto` preserva a escal
          +a plena do produto, sem arredondamento — verificado diretament
          +e comparando o `BigDecimal` resultante de `1.005 × 1.005` cont
          +ra o literal exato `"1.010025"`.                              
      902 +  - **Critério de conclusão:** todos os cenários acima verdes,
          + inclusive os dois de `NullPointerException` e o de campo não-
          +financeiro inválido; nenhuma chamada a `setScale`/`round` em `
          +ResolutorCambio.java` (confirmável por leitura, já que não há 
          +teste automatizado que prove ausência de arredondamento em tod
          +os os caminhos possíveis — DT-015).                           
      903    - **Comando de verificação:**
      904      ```
      905      mvn -q test -Dtest=ResolucaoCambioTest
      906      ```
      907 -  - **Commit sugerido:** `feat(T-037): implementa ResolutorCam
          -bio sem arredondamento`                                       
      907 +  - **Commit sugerido:** `feat(T-037): implementa ResolutorCam
          +bio com verificacao de valor/moeda/data e CotacaoResolvida`   
      908    - **Status:** [ ] pendente
      909  
      910  - [ ] **T-038** — `Normalizador` sobre `valorConvertidoBruto` 
           + wiring da conversão no pipeline

● Now T-039 (remove CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO obligation, fix
  commit rule):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 5 lines, removed 4 lines
      932    - **Status:** [ ] pendente
      933  
      934  - [ ] **T-039** — `MoedaSemCotacaoTest` — coexistência de moti
           vos (8.4, item 14)
      935 -  - **O que faz:** confirma, com teste dedicado, a exclusão de
          - dependência declarada em `spec.md` 8.4 item 14: um item com `
          -MOEDA_SEM_COTACAO` pode coexistir com `CATEGORIA_FORA_POLITICA
          -`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMPETE
          -NCIA` (que não dependem de `valor_normalizado`), mas **nunca**
          - com `VALOR_NAO_POSITIVO`, `NOTA_FISCAL_AUSENTE`, `DUPLICIDADE
          -` ou qualquer motivo de teto (que dependem). Comportamento já 
          -decorre da checagem `valorNormalizado != null` já existente em
          - `AvaliadorRegrasIndividuais` (T-008/T-011) — esta task é pred
          -ominantemente de verificação, sem produção nova esperada (mesm
          -a contingência de T-015: se um cenário revelar um defeito real
          -, corrigir nesta mesma task, antes do commit `test(T-039)`, e 
          -nesse caso o commit passa a `fix(T-039)` seguido de `test(T-03
          -9)`, como já autorizado para T-015).                          
      935 +  - **O que faz:** confirma, com teste dedicado, a exclusão de
          + dependência declarada em `spec.md` 8.4 item 14, no ponto do b
          +acklog em que esta task acontece — **antes** de a política por
          + centro de custo estar integrada ao avaliador (Bloco G, T-040/
          +T-041): um item com `MOEDA_SEM_COTACAO` pode coexistir com `CA
          +TEGORIA_FORA_POLITICA` e/ou `FORA_COMPETENCIA` (que não depend
          +em de `valor_normalizado`, e que já são produzidos pelas sobre
          +cargas históricas de `AvaliadorRegrasIndividuais`), mas **nunc
          +a** com `VALOR_NAO_POSITIVO`, `NOTA_FISCAL_AUSENTE`, `DUPLICID
          +ADE` ou qualquer motivo de teto (que dependem de `valor_normal
          +izado`). Esta task **não** cobre a coexistência com `CATEGORIA
          +_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — esse motivo só existe a part
          +ir de T-041; a coexistência com ele é fechada em T-041, depois
          + que o avaliador de centro de custo passar a existir. Comporta
          +mento já decorre da checagem `valorNormalizado != null` já exi
          +stente em `AvaliadorRegrasIndividuais` (T-008/T-011) — esta ta
          +sk é predominantemente de verificação, sem produção nova esper
          +ada.                                                          
      936    - **RN atendidas:** RN-020.
      937    - **CA atendidos:** base de rastreabilidade para o item 14 d
           e 8.4 (não tem CA numerado próprio — é comportamento de compos
           ição, já coberto indiretamente por CA-030 e pelos cenários de 
           `§12.3`/`§12.4`).
      938    - **DT/seções do plan:** plan §2 (exclusão nova, item 14 da 
           lista de 8.4), §9.
     ...
      940    - **Arquivos que cria/modifica:**
      941      - `tests/java/com/desafio/reembolso/pipeline/MoedaSemCotac
           aoTest.java`
      942    - **Passos de implementação:**
      943 -    1. Escrever os cenários abaixo, executando o pipeline até 
          -`AvaliadorRegrasIndividuais` (ainda sem a `TabelaPoliticaResol
          -vida` do Bloco G — usar as sobrecargas históricas de `Avaliado
          -rRegrasIndividuais`, que continuam existindo por compatibilida
          -de até T-041/T-055, e adaptar o teste quando as sobrecargas no
          -vas estiverem disponíveis, se necessário revisar em T-041).   
      944 -  - **Testes obrigatórios:** `MoedaSemCotacaoTest` — moeda est
          -rangeira sem cotação, categoria fora da política → ambos os mo
          -tivos presentes; moeda estrangeira sem cotação, data fora da c
          -ompetência → ambos presentes; moeda estrangeira sem cotação, v
          -alor que seria não positivo se calculável → **apenas** `MOEDA_
          -SEM_COTACAO` (não `VALOR_NAO_POSITIVO`, porque `valorNormaliza
          -do` é nulo); moeda estrangeira sem cotação, item que teria exc
          -edido o gatilho de nota fiscal se convertido → **apenas** `MOE
          -DA_SEM_COTACAO` (não `NOTA_FISCAL_AUSENTE`).                  
      943 +    1. Escrever os cenários abaixo, executando o pipeline até 
          +`AvaliadorRegrasIndividuais` usando as sobrecargas históricas 
          +(`avaliar(item, envelope)`), que já produzem `CATEGORIA_FORA_P
          +OLITICA`/`FORA_COMPETENCIA`/`VALOR_NAO_POSITIVO`/`NOTA_FISCAL_
          +AUSENTE` sem depender de política externa.                    
      944 +    2. Se algum cenário revelar um defeito real, corrigir o có
          +digo de produção **no único commit** desta task — ver a qualif
          +icação de commit único de tasks de teste, no topo deste arquiv
          +o: nunca `fix(T-039)` seguido de um segundo commit `test(T-039
          +)`.                                                           
      945 +  - **Testes obrigatórios:** `MoedaSemCotacaoTest` — moeda est
          +rangeira sem cotação, categoria fora da política → `MOEDA_SEM_
          +COTACAO` **e** `CATEGORIA_FORA_POLITICA` coexistem; moeda estr
          +angeira sem cotação, data fora da competência → `MOEDA_SEM_COT
          +ACAO` **e** `FORA_COMPETENCIA` coexistem; moeda estrangeira se
          +m cotação, valor que seria não positivo se calculável → **ausê
          +ncia** de `VALOR_NAO_POSITIVO` (só `MOEDA_SEM_COTACAO`, porque
          + `valorNormalizado` é nulo); moeda estrangeira sem cotação, it
          +em que teria excedido o gatilho de nota fiscal se convertido →
          + **ausência** de `NOTA_FISCAL_AUSENTE`; moeda estrangeira sem 
          +cotação, item que seria duplicata econômica se o valor fosse c
          +alculável → **ausência** de `DUPLICIDADE`; moeda estrangeira s
          +em cotação, item que estaria sujeito a teto se elegível → **au
          +sência** de qualquer motivo de teto.                          
      946    - **Critério de conclusão:** todos os cenários acima verdes;
            `mvn test` completo verde.
      947    - **Comando de verificação:**
      948      ```
      949      mvn -q test -Dtest=MoedaSemCotacaoTest
      950      ```
      950 -  - **Commit sugerido:** `test(T-039): comprova coexistencia e
          - exclusao de motivos com MOEDA_SEM_COTACAO`                   
      951 +  - **Commit sugerido:** `test(T-039): comprova coexistencia e
          + exclusao de motivos com MOEDA_SEM_COTACAO` — único commit da 
          +task; se um defeito real exigir correção, a mensagem passa a `
          +fix(T-039)`, sem um segundo commit `test(T-039)` depois.      
      952    - **Status:** [ ] pendente
      953  
      954  ---

● Now T-041 (add T-038 dependency and coexistence case):

● Update(specs\001-motor-reembolso\tasks.md)  ⎿  Added 4 lines, removed 4 lines
      978    - **Status:** [ ] pendente
      979  
      980  - [ ] **T-041** — `AvaliadorRegrasIndividuais` consome polític
           a externa
      981 -  - **O que faz:** acrescenta a `AvaliadorRegrasIndividuais` u
          -ma nova sobrecarga que recebe `TabelaPoliticaResolvida` e `Pol
          -iticaExterna` (para o gatilho de nota fiscal, RN-009 atualizad
          -a) e implementa RN-019: categoria ausente da tabela aplicável 
          -→ `CATEGORIA_FORA_POLITICA` quando `origem == PADRAO`, `CATEGO
          -RIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` quando `origem == CENTRO_CU
          -STO`; categoria presente com `limite == 0` → `CATEGORIA_NAO_RE
          -EMBOLSAVEL_CENTRO_CUSTO` (nunca `PARCIALMENTE_REEMBOLSADO`). A
          -s sobrecargas antigas (`avaliar(item)`, `avaliar(item, envelop
          -e)`, com `Set<String>` fixo e `PoliticaReembolso`) **permanece
          -m intactas**, porque a suíte histórica (T-006 a T-021) as usa 
          -— a migração delas para as novas é T-055.                     
      981 +  - **O que faz:** acrescenta a `AvaliadorRegrasIndividuais` u
          +ma nova sobrecarga que recebe `TabelaPoliticaResolvida` e `Pol
          +iticaExterna` (para o gatilho de nota fiscal, RN-009 atualizad
          +a) e implementa RN-019: categoria ausente da tabela aplicável 
          +→ `CATEGORIA_FORA_POLITICA` quando `origem == PADRAO`, `CATEGO
          +RIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` quando `origem == CENTRO_CU
          +STO`; categoria presente com `limite == 0` → `CATEGORIA_NAO_RE
          +EMBOLSAVEL_CENTRO_CUSTO` (nunca `PARCIALMENTE_REEMBOLSADO`). A
          +s sobrecargas antigas (`avaliar(item)`, `avaliar(item, envelop
          +e)`, com `Set<String>` fixo e `PoliticaReembolso`) **permanece
          +m intactas**, porque a suíte histórica (T-006 a T-021) as usa 
          +— a migração delas para as novas é T-055. Esta task também **f
          +echa** a coexistência de motivos que T-039 deixou pendente: co
          +m o avaliador de centro de custo agora existindo, um item com 
          +`MOEDA_SEM_COTACAO` **e** categoria ausente da tabela de um ce
          +ntro de custo cadastrado coexiste com `CATEGORIA_NAO_REEMBOLSA
          +VEL_CENTRO_CUSTO`, pelo mesmo princípio de 8.4 item 14 (a cate
          +goria não depende de `valor_normalizado`).                    
      982    - **RN atendidas:** RN-019, RN-009 (atualizada — gatilho de 
           `PoliticaExterna`, não de `PoliticaReembolso`).
      983    - **CA atendidos:** CA-024 a CA-027 (uso real dentro do aval
           iador de regras).
      984    - **DT/seções do plan:** DT-011; plan §6, §19.
      985 -  - **Dependências:** T-023 (`CATEGORIA_NAO_REEMBOLSAVEL_CENTR
          -O_CUSTO`), T-024 (`RN_019`), T-040 (`ResolutorPoliticaCentroCu
          -sto`).                                                        
      985 +  - **Dependências:** T-023 (`CATEGORIA_NAO_REEMBOLSAVEL_CENTR
          +O_CUSTO`), T-024 (`RN_019`), T-038 (pipeline de câmbio complet
          +o, necessário para o cenário de coexistência com `MOEDA_SEM_CO
          +TACAO`), T-040 (`ResolutorPoliticaCentroCusto`).              
      986    - **Arquivos que cria/modifica:**
      987      - `src/main/java/com/desafio/reembolso/pipeline/AvaliadorR
           egrasIndividuais.java`
      988      - `tests/java/com/desafio/reembolso/pipeline/CategoriaCent
           roCustoTest.java`
     ...
      991      2. Dentro dela, substituir a checagem de `CATEGORIAS_REEMB
           OLSAVEIS.contains(...)` por: se `item.categoriaNormalizada()` 
           ausente de `tabela.getCategorias()`, escolher o motivo conform
           e `tabela.getOrigem()`; se presente com `TabelaCategoria.getLi
           mite().compareTo(BigDecimal.ZERO) == 0`, `CATEGORIA_NAO_REEMBO
           LSAVEL_CENTRO_CUSTO` (só pode ocorrer com `origem == CENTRO_CU
           STO`, porque `padrao` com limite zero já foi rejeitado na leit
           ura, T-030).
      992      3. Substituir `politica.getGatilhoNotaFiscal()` (o `Politi
           caReembolso` fixo) por `politica.getNotaFiscalObrigatoriaAcima
           De()` (o `PoliticaExterna` recebido) na avaliação de RN-009, d
           entro desta nova sobrecarga.
      993      4. Manter `avaliarRn006ERn007` (valor não positivo) inalte
           rado — não depende de política.
      994 -  - **Testes obrigatórios:** `CategoriaCentroCustoTest` — os q
          -uatro cenários de CA-024 a CA-027 exercitados diretamente cont
          -ra `AvaliadorRegrasIndividuais` (não só contra `ResolutorPolit
          -icaCentroCusto` isoladamente): centro desconhecido usa `padrao
          -` e produz `CATEGORIA_FORA_POLITICA` para categoria ausente; c
          -entro cadastrado sem a categoria produz `CATEGORIA_NAO_REEMBOL
          -SAVEL_CENTRO_CUSTO`; `limite == 0` num centro cadastrado produ
          -z `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` com `valor_reembol
          -savel` `0,00` (nunca parcial); gatilho de nota fiscal lido de 
          -`PoliticaExterna.getNotaFiscalObrigatoriaAcimaDe()`, não de `P
          -oliticaReembolso`.                                            
      995 -  - **Critério de conclusão:** todos os cenários acima verdes;
          - `mvn test` completo verde (sobrecargas antigas intocadas, suí
          -te histórica passa sem alteração).                            
      994 +  - **Testes obrigatórios:** `CategoriaCentroCustoTest` — os q
          +uatro cenários de CA-024 a CA-027 exercitados diretamente cont
          +ra `AvaliadorRegrasIndividuais` (não só contra `ResolutorPolit
          +icaCentroCusto` isoladamente): centro desconhecido usa `padrao
          +` e produz `CATEGORIA_FORA_POLITICA` para categoria ausente; c
          +entro cadastrado sem a categoria produz `CATEGORIA_NAO_REEMBOL
          +SAVEL_CENTRO_CUSTO`; `limite == 0` num centro cadastrado produ
          +z `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` com `valor_reembol
          +savel` `0,00` (nunca parcial); gatilho de nota fiscal lido de 
          +`PoliticaExterna.getNotaFiscalObrigatoriaAcimaDe()`, não de `P
          +oliticaReembolso`; **e** um caso de coexistência — moeda estra
          +ngeira sem cotação (`MOEDA_SEM_COTACAO`, via `ResolutorCambio`
          +) combinada com categoria ausente da tabela de um centro de cu
          +sto cadastrado (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`) pro
          +duz os dois motivos simultaneamente no mesmo item.            
      995 +  - **Critério de conclusão:** todos os cenários acima verdes,
          + inclusive o de coexistência; `mvn test` completo verde (sobre
          +cargas antigas intocadas, suíte histórica passa sem alteração)
          +.                                                             
      996    - **Comando de verificação:**
      997      ```
      998      mvn -q test -Dtest=CategoriaCentroCustoTest

● Now T-049 (fixture migration to schema 1.2):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 10 lines, removed 7 lines
      1159    - **Commit sugerido:** `feat(T-048): estende CompositorSaid
            a com campos de cambio e nova ordem de motivos`
      1160    - **Status:** [ ] pendente
      1161  
      1162 -- [ ] **T-049** — Serializar campos de câmbio em `EscritorRes
           -ultado`                                                      
      1163 -  - **O que faz:** `EscritorResultado.registro(...)` passa a 
           -escrever `moeda`, `taxa_cambio_aplicada` e `data_cotacao_util
           -izada` no JSON de saída, entre `valor_informado` e `valor_nor
           -malizado` (spec 4.3). `taxa_cambio_aplicada` é sempre número 
           -JSON (nunca texto), preservando a precisão do arquivo de câmb
           -io — mesma técnica de `monetario(...)` já usada para os demai
           -s campos decimais, mas sem forçar duas casas (a taxa pode ter
           - mais precisão que um valor monetário).                      
      1162 +- [ ] **T-049** — Serializar campos de câmbio em `EscritorRes
           +ultado` e migrar o fixture histórico para o schema 1.2       
      1163 +  - **O que faz:** `EscritorResultado.registro(...)` passa a 
           +escrever `moeda`, `taxa_cambio_aplicada` e `data_cotacao_util
           +izada` no JSON de saída, entre `valor_informado` e `valor_nor
           +malizado` (spec 4.3). `taxa_cambio_aplicada` é sempre número 
           +JSON (nunca texto), preservando a precisão do arquivo de câmb
           +io. Como a saída real passa a ter três campos que `tests/reso
           +urces/fixtures/despesas-exemplo-esperado.json` (T-020) ainda 
           +não conhece, este mesmo fixture é atualizado **manualmente** 
           +nesta task — nunca gerado pelo próprio motor — para incluir o
           +s três campos novos nas 14 posições, conforme o contrato de 4
           +.3 (BRL em todas: `moeda: "BRL"`, `taxa_cambio_aplicada: 1`, 
           +`data_cotacao_utilizada: null`, já que `exemplos/despesas-exe
           +mplo.json` não tem despesas em moeda estrangeira). Nenhuma de
           +cisão, valor reembolsável ou motivo histórico muda — só o sch
           +ema de auditoria ganha os três campos da spec 1.2.           
      1164    - **RN atendidas:** RN-017 (atualizada).
      1165 -  - **CA atendidos:** CA-034 (nível de serialização).        
      1165 +  - **CA atendidos:** CA-034 (nível de serialização); confirm
           +a CA-001/CA-002/CA-003 sob o schema 1.2.                     
      1166    - **DT/seções do plan:** plan §9, §13.
      1167    - **Dependências:** T-048 (`ResultadoItem` com os três camp
            os).
      1168    - **Arquivos que cria/modifica:**
      1169      - `src/main/java/com/desafio/reembolso/escritor/EscritorR
            esultado.java`
      1170      - `tests/java/com/desafio/reembolso/escritor/EscritorResu
            ltadoTest.java` (estendido)
      1171 +    - `tests/resources/fixtures/despesas-exemplo-esperado.jso
           +n` (migrado manualmente para o schema 1.2, campo a campo, nas
           + 14 posições)                                                
      1172    - **Passos de implementação:**
      1173      1. Em `registro(ResultadoItem resultado)`, adicionar `no.
            put("moeda", resultado.moeda())` (nulo-seguro — Jackson seria
            liza `String` nulo como `null` JSON), `no.set("taxa_cambio_ap
            licada", resultado.taxaCambioAplicada() == null ? null : MAPP
            ER.getNodeFactory().numberNode(resultado.taxaCambioAplicada()
            ))` (ou equivalente, preservando a escala do `BigDecimal` sem
             `setScale` forçado), `no.put("data_cotacao_utilizada", resul
            tado.dataCotacaoUtilizada() == null ? null : resultado.dataCo
            tacaoUtilizada().toString())`.
      1174      2. Posicionar as três chamadas entre `valor_informado` e 
            `valor_normalizado`, respeitando a ordem de campos de 4.3.
      1174 -  - **Testes obrigatórios:** `EscritorResultadoTest` (estendi
           -do) — os quatro formatos de saída (mesmos cenários de `SaidaC
           -ambioTest`, agora verificados no JSON serializado, não no `Re
           -sultadoItem`); `taxa_cambio_aplicada` nunca aparece como stri
           -ng nem em notação científica.                                
      1175 -  - **Critério de conclusão:** cenários acima verdes; `mvn te
           -st` completo verde.                                          
      1175 +    3. Editar manualmente `tests/resources/fixtures/despesas-
           +exemplo-esperado.json`, inserindo `"moeda": "BRL"`, `"taxa_ca
           +mbio_aplicada": 1`, `"data_cotacao_utilizada": null` em cada 
           +uma das 14 posições de `resultados` — nunca gerar o arquivo e
           +xecutando o motor.                                           
      1176 +    4. Rodar `ExemploCompletoTest` (T-020) contra o fixture m
           +igrado, confirmando que as 14 decisões, valores reembolsáveis
           + e motivos permanecem idênticos aos já registrados, e que `to
           +tal_reembolsavel` continua `585.43` — só o schema de auditori
           +a mudou.                                                     
      1177 +  - **Testes obrigatórios:** `EscritorResultadoTest` (estendi
           +do) — os quatro formatos de saída (mesmos cenários de `SaidaC
           +ambioTest`, agora verificados no JSON serializado); `taxa_cam
           +bio_aplicada` nunca aparece como string nem em notação cientí
           +fica. `ExemploCompletoTest` — executado contra o fixture migr
           +ado, confirmando que nenhuma decisão, valor ou motivo históri
           +co mudou, e que `total_reembolsavel = 585.43` se mantém.     
      1178 +  - **Critério de conclusão:** `EscritorResultadoTest` e `Exe
           +mploCompletoTest` verdes; o fixture migrado tem os três campo
           +s novos nas 14 posições, e nenhum outro campo do fixture foi 
           +alterado.                                                    
      1179    - **Comando de verificação:**
      1180      ```
      1178 -    mvn -q test -Dtest=EscritorResultadoTest                 
      1181 +    mvn -q test "-Dtest=EscritorResultadoTest,ExemploCompleto
           +Test"                                                        
      1182      ```
      1180 -  - **Commit sugerido:** `feat(T-049): serializa moeda, taxa_
           -cambio_aplicada e data_cotacao_utilizada`                    
      1183 +  - **Commit sugerido:** `feat(T-049): serializa campos de ca
           +mbio e migra fixture historico para o schema 1.2`            
      1184    - **Status:** [ ] pendente
      1185  
      1186  ---

● Now T-050 (reuse migrated fixture, Main.run(...) definitively):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 6 lines, removed 6 lines
      1188  ### Bloco J — Regressões e integração
      1189  
      1190  - [ ] **T-050** — Fixture externa de política histórica + reg
            ressão R$585,43
      1191 -  - **O que faz:** cria `tests/resources/fixtures/politica-hi
           -storica.json` (equivalente aos valores hardcoded pré-Dia 2: `
           -alimentacao` R$60/dia, `transporte_urbano` R$80/dia, `hospeda
           -gem` R$250/diária, `nota_fiscal_obrigatoria_acima_de` R$100, 
           -`vigencia` qualquer data real válida) e `tests/resources/fixt
           -ures/cambio-historico.json` (um `cambio.json` mínimo/vazio, j
           -á que `despesas-exemplo.json` não tem despesas em moeda estra
           -ngeira). Processa `exemplos/despesas-exemplo.json` de ponta a
           - ponta com esses dois arquivos externos e confirma `total_ree
           -mbolsavel = 585.43` (CA-037), comparando estruturalmente cont
           -ra o fixture já existente `tests/resources/fixtures/despesas-
           -exemplo-esperado.json` (que continua válido, porque a polític
           -a histórica reproduz exatamente os valores antes hardcoded). 
      1191 +  - **O que faz:** cria `tests/resources/fixtures/politica-hi
           +storica.json` (equivalente aos valores hardcoded pré-Dia 2: `
           +alimentacao` R$60/dia, `transporte_urbano` R$80/dia, `hospeda
           +gem` R$250/diária, `nota_fiscal_obrigatoria_acima_de` R$100, 
           +`vigencia` qualquer data real válida) e `tests/resources/fixt
           +ures/cambio-historico.json` (um `cambio.json` mínimo/vazio, j
           +á que `despesas-exemplo.json` não tem despesas em moeda estra
           +ngeira). Processa `exemplos/despesas-exemplo.json` de ponta a
           + ponta com esses dois arquivos externos, invocando **definiti
           +vamente** `Main.run(...)` — não `executarPipeline` diretament
           +e, porque o objetivo desta task é testar a cadeia inteira (le
           +itores, parser de CLI, pipeline e escritor) sob política e câ
           +mbio externos reais, não só o núcleo isolado — e confirma `to
           +tal_reembolsavel = 585.43` (CA-037), comparando estruturalmen
           +te contra `tests/resources/fixtures/despesas-exemplo-esperado
           +.json`, **já migrado para o schema 1.2 em T-049**: esta task 
           +reutiliza esse fixture tal como T-049 o deixou, sem alterá-lo
           + de novo.                                                    
      1192    - **RN atendidas:** RN-019 (confirmação de que a política e
            xterna equivalente reproduz o comportamento histórico).
      1193    - **CA atendidos:** CA-037.
      1194    - **DT/seções do plan:** plan §16.
      1195 -  - **Dependências:** T-042, T-046 (pipeline real usando polí
           -tica e câmbio externos de ponta a ponta), T-030/T-032 (leitor
           -es).                                                         
      1195 +  - **Dependências:** T-042, T-046 (pipeline real usando polí
           +tica e câmbio externos de ponta a ponta), T-030/T-032 (leitor
           +es), T-049 (fixture já migrado para o schema 1.2).           
      1196    - **Arquivos que cria/modifica:**
      1197      - `tests/resources/fixtures/politica-historica.json`
      1198      - `tests/resources/fixtures/cambio-historico.json`
      1199      - `tests/java/com/desafio/reembolso/RegressaoHistoricaTes
            t.java`
      1200    - **Passos de implementação:**
      1201      1. Escrever os dois arquivos JSON de fixture, manualmente
            , seguindo o contrato de `politica.json`/`cambio.json` de spe
            c 4.1.1.
      1202 -    2. Escrever `RegressaoHistoricaTest`, invocando `Main.run
           -(...)` (ou `executarPipeline` diretamente, se mais direto par
           -a o teste) com `--input exemplos/despesas-exemplo.json --poli
           -tica <fixture> --cambio <fixture> --output <temp>`.          
      1203 -    3. Comparar o JSON de saída, campo a campo, contra `despe
           -sas-exemplo-esperado.json` já existente.                     
      1204 -  - **Testes obrigatórios:** `RegressaoHistoricaTest` — os 14
           - registros e `total_reembolsavel = 585.43` coincidem com o fi
           -xture histórico já existente (CA-037).                       
      1205 -  - **Critério de conclusão:** teste verde; nenhuma alteração
           - em `despesas-exemplo-esperado.json` nem em código de produçã
           -o.                                                           
      1202 +    2. Escrever `RegressaoHistoricaTest`, invocando `Main.run
           +(String[] args, PrintStream out, PrintStream err)` com `{"cal
           +cular", "--input", "exemplos/despesas-exemplo.json", "--polit
           +ica", <fixture>, "--cambio", <fixture>, "--output", <temp>}`.
      1203 +    3. Comparar o JSON de saída, campo a campo, contra `despe
           +sas-exemplo-esperado.json` (já no schema 1.2 desde T-049) — n
           +enhuma edição adicional nesse fixture dentro desta task.     
      1204 +  - **Testes obrigatórios:** `RegressaoHistoricaTest` — os 14
           + registros (já no schema 1.2) e `total_reembolsavel = 585.43`
           + coincidem com o fixture histórico migrado (CA-037).         
      1205 +  - **Critério de conclusão:** teste verde; nenhuma edição ad
           +icional em `despesas-exemplo-esperado.json` dentro desta task
           + (a migração para o schema 1.2 já ocorreu em T-049); nenhuma 
           +alteração em código de produção.                             
      1206    - **Comando de verificação:**
      1207      ```
      1208      mvn -q test -Dtest=RegressaoHistoricaTest

● Now T-051 (remove "ou arquivo próprio"):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 4 lines, removed 4 lines
      1215    - **RN atendidas:** RN-019.
      1216    - **CA atendidos:** CA-038.
      1217    - **DT/seções do plan:** plan §16.
      1218 -  - **Dependências:** T-050 (mesma classe de teste, `Regressa
           -oHistoricaTest`, estendida) ou arquivo próprio — ver passo 1.
      1218 +  - **Dependências:** T-050 (mesma classe de teste, `Regressa
           +oHistoricaTest`, estendida).                                 
      1219    - **Arquivos que cria/modifica:**
      1220      - `tests/resources/fixtures/despesas-exemplo-v4-esperado.
            json`
      1221 -    - `tests/resources/fixtures/envelope-cc-eng-plataforma.js
           -on` (envelope de teste com `centro_custo = "CC-ENG-PLATAFORMA
           -"` e as mesmas despesas de `exemplos/despesas-exemplo.json`, 
           -já que o arquivo original não traz `colaborador.centro_custo`
           -)                                                            
      1221 +    - `tests/resources/fixtures/envelope-cc-eng-plataforma.js
           +on` (fixture próprio: mesmo array `despesas` de `exemplos/des
           +pesas-exemplo.json`, com um bloco `colaborador.centro_custo =
           + "CC-ENG-PLATAFORMA"` adicionado — nunca uma alteração do arq
           +uivo original em `exemplos/`, que é lido por T-050/T-021 e nã
           +o deve mudar)                                                
      1222      - `tests/java/com/desafio/reembolso/RegressaoHistoricaTes
            t.java` (mesma classe de T-050, caso adicional)
      1223    - **Passos de implementação:**
      1224 -    1. Verificar se `exemplos/despesas-exemplo.json` já traz 
           -`colaborador.centro_custo`; se não trouxer, criar o fixture d
           -e envelope equivalente com o centro de custo adicionado, sem 
           -alterar o arquivo original em `exemplos/` (que é lido por T-0
           -50/T-021 e não deve mudar).                                  
      1224 +    1. Criar `tests/resources/fixtures/envelope-cc-eng-plataf
           +orma.json` com o mesmo array `despesas` de `exemplos/despesas
           +-exemplo.json` e `colaborador.centro_custo = "CC-ENG-PLATAFOR
           +MA"` — arquivo de fixture definitivo, sem alterar o arquivo o
           +riginal em `exemplos/`.                                      
      1225      2. Escrever `despesas-exemplo-v4-esperado.json` manualmen
            te, a partir da tabela de `spec.md` §12.2 combinada com os de
            z itens inalterados de §4.7.
      1226 -    3. Escrever o caso de teste, processando o fixture de env
           -elope com `politica-v4.json` real.                           
      1226 +    3. Escrever o caso de teste, na mesma classe `RegressaoHi
           +storicaTest`, processando o fixture de envelope com `politica
           +-v4.json` real.                                              
      1227    - **Testes obrigatórios:** `RegressaoHistoricaTest` — `tota
            l_reembolsavel = 351.43`; os quatro itens mudados coincidem e
            xatamente com §12.2; os dez itens restantes coincidem com o c
            enário histórico (CA-038).
      1228    - **Critério de conclusão:** teste verde.
      1229    - **Comando de verificação:**

● Now T-054 (four commands, target/ outputs, mandatory commit):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 14 lines, removed 10 lines
      1276    - **Status:** [ ] pendente
      1277  
      1278  - [ ] **T-054** — Execução real da suíte e do JAR com as quat
            ro flags
      1279 -  - **O que faz:** verificação manual, documentada, de que a 
           -suíte inteira passa (`mvn test`) e que o JAR empacotado (`mvn
           - package`) executa de ponta a ponta com as quatro flags reais
           - contra os quatro cenários financeiros do Dia 2 (baseline his
           -tórica, política v4/`CC-ENG-PLATAFORMA`, Rafael, Dani), produ
           -zindo os totais já confirmados em T-050 a T-053 também pelo b
           -inário real, não só pelos testes JUnit. Se algo divergir entr
           -e o teste automatizado e a execução real do JAR, o defeito é 
           -corrigido nesta mesma task antes do commit (mesma contingênci
           -a de T-015).                                                 
      1279 +  - **O que faz:** verificação manual, documentada, de que a 
           +suíte inteira passa (`mvn test`) e que o JAR empacotado (`mvn
           + package`) executa de ponta a ponta com as quatro flags reais
           + contra os **quatro** cenários financeiros do Dia 2 — (1) bas
           +eline histórica, 585.43; (2) política v4/`CC-ENG-PLATAFORMA`,
           + 351.43, usando o fixture de envelope criado em T-051; (3) Ra
           +fael, 1143.26; (4) Dani, 373.76 —, produzindo os totais já co
           +nfirmados em T-050 a T-053 também pelo binário real, não só p
           +elos testes JUnit. As quatro saídas são escritas dentro de `t
           +arget/` (diretório de build, não versionado), nunca como arqu
           +ivo solto na raiz do repositório — assim nenhum resultado man
           +ual fica pendente como arquivo não rastreado em `git status`.
           + Esta task **sempre** gera um commit, sem exceção: não há cen
           +ário de "task concluída sem commit". Se algo divergir entre o
           + teste automatizado e a execução real do JAR, o defeito é cor
           +rigido nesta mesma task, e a mensagem de commit passa de `tes
           +t(T-054)` para `fix(T-054)`.                                 
      1280    - **RN atendidas:** nenhuma nova — é verificação, não regra
             de negócio.
      1281    - **CA atendidos:** confirma CA-037 a CA-040 via binário re
            al, fora do JUnit.
      1282    - **DT/seções do plan:** DT-002, DT-009; plan §16.
      1283    - **Dependências:** T-050, T-051, T-052, T-053.
      1284 -  - **Arquivos que cria/modifica:** nenhum arquivo de produçã
           -o ou teste esperado — task de verificação manual. Se um defei
           -to real for encontrado, o arquivo afetado é corrigido e lista
           -do no commit.                                                
      1284 +  - **Arquivos que cria/modifica:** nenhum arquivo de produçã
           +o ou teste no caminho normal — os quatro arquivos de saída de
           + verificação ficam em `target/` (`target/verificacao-585.json
           +`, `target/verificacao-351.json`, `target/verificacao-rafael.
           +json`, `target/verificacao-dani.json`), fora do controle de v
           +ersão. Se um defeito real for encontrado, o arquivo de produç
           +ão afetado é corrigido e listado no commit.                  
      1285    - **Passos de implementação:**
      1286      1. `mvn package` e confirmar que `target/motor-reembolso.
            jar` é produzido.
      1287 -    2. Executar `java -jar target/motor-reembolso.jar calcula
           -r --input exemplos/despesas-exemplo.json --output <tmp> --pol
           -itica <fixture-historica> --cambio <fixture-historica>` e con
           -ferir `total_reembolsavel = 585.43` no arquivo de saída real.
      1288 -    3. Repetir para os outros três cenários (v4/`CC-ENG-PLATA
           -FORMA`, Rafael, Dani), com os arquivos reais de `exemplos/env
           -elope/`.                                                     
      1289 -  - **Testes obrigatórios:** nenhum teste JUnit novo — a veri
           -ficação é a execução manual documentada acima.               
      1290 -  - **Critério de conclusão:** os quatro totais conferem, lid
           -os diretamente do arquivo de saída produzido pelo JAR real; `
           -mvn test` inteiro verde.                                     
      1287 +    2. Executar o comando do cenário 1 (baseline histórica, 5
           +85.43), com saída em `target/verificacao-585.json`.          
      1288 +    3. Executar o comando do cenário 2 (política v4/`CC-ENG-P
           +LATAFORMA`, 351.43), usando `tests/resources/fixtures/envelop
           +e-cc-eng-plataforma.json` (T-051) como `--input` e `exemplos/
           +envelope/politica-v4.json`/`exemplos/envelope/cambio.json` re
           +ais, com saída em `target/verificacao-351.json`.             
      1289 +    4. Executar o comando do cenário 3 (Rafael/`CC-COMERCIAL`
           +, 1143.26), com saída em `target/verificacao-rafael.json`.   
      1290 +    5. Executar o comando do cenário 4 (Dani/centro de custo 
           +desconhecido, 373.76), com saída em `target/verificacao-dani.
           +json`.                                                       
      1291 +    6. Ler `total_reembolsavel` diretamente de cada um dos qu
           +atro arquivos em `target/` e conferir contra os valores esper
           +ados.                                                        
      1292 +  - **Testes obrigatórios:** nenhum teste JUnit novo — a veri
           +ficação é a execução manual documentada acima, com os quatro 
           +totais lidos diretamente dos quatro arquivos gerados em `targ
           +et/`.                                                        
      1293 +  - **Critério de conclusão:** os quatro totais conferem, lid
           +os diretamente de `target/verificacao-585.json`, `target/veri
           +ficacao-351.json`, `target/verificacao-rafael.json` e `target
           +/verificacao-dani.json`; `mvn test` inteiro verde; um commit 
           +é criado ao final, sem exceção.                              
      1294    - **Comando de verificação:**
      1295      ```
      1296      mvn -q test
      1297      mvn -q package
      1295 -    java -jar target/motor-reembolso.jar calcular --input exe
           -mplos/despesas-exemplo.json --output resultado-585.json --pol
           -itica tests/resources/fixtures/politica-historica.json --camb
           -io tests/resources/fixtures/cambio-historico.json            
      1296 -    java -jar target/motor-reembolso.jar calcular --input exe
           -mplos/envelope/despesas-envelope.json --output resultado-rafa
           -el.json --politica exemplos/envelope/politica-v4.json --cambi
           -o exemplos/envelope/cambio.json                              
      1297 -    java -jar target/motor-reembolso.jar calcular --input exe
           -mplos/envelope/despesas-envelope-cc-desconhecido.json --outpu
           -t resultado-dani.json --politica exemplos/envelope/politica-v
           -4.json --cambio exemplos/envelope/cambio.json                
      1298 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/despesas-exemplo.json --output target/verificacao-585.j
           +son --politica tests/resources/fixtures/politica-historica.js
           +on --cambio tests/resources/fixtures/cambio-historico.json   
      1299 +    java -jar target/motor-reembolso.jar calcular --input tes
           +ts/resources/fixtures/envelope-cc-eng-plataforma.json --outpu
           +t target/verificacao-351.json --politica exemplos/envelope/po
           +litica-v4.json --cambio exemplos/envelope/cambio.json        
      1300 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/envelope/despesas-envelope.json --output target/verific
           +acao-rafael.json --politica exemplos/envelope/politica-v4.jso
           +n --cambio exemplos/envelope/cambio.json                     
      1301 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/envelope/despesas-envelope-cc-desconhecido.json --outpu
           +t target/verificacao-dani.json --politica exemplos/envelope/p
           +olitica-v4.json --cambio exemplos/envelope/cambio.json       
      1302      ```
      1299 -  - **Commit sugerido:** `test(T-054): confirma execucao real
           - do jar com as quatro flags nos quatro cenarios` (ou nenhum c
           -ommit, se nada foi alterado além da verificação — nesse caso 
           -a task se encerra sem commit de código, mas o relatório da se
           -ssão registra a verificação feita)                           
      1303 +  - **Commit sugerido:** `test(T-054): confirma execucao real
           + do jar com as quatro flags nos quatro cenarios` — sempre ger
           +ado, contendo no mínimo a atualização do checkbox/status da t
           +ask e o export da sessão (os quatro arquivos de evidência fic
           +am em `target/`, fora do commit, exatamente para não deixar n
           +enhum arquivo não rastreado pendente); se um defeito real exi
           +giu correção, a mensagem passa a `fix(T-054)`.               
      1304    - **Status:** [ ] pendente
      1305  
      1306  ---

● Now T-055 (mandatory full migration, scoped greps):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 11 lines, removed 11 lines
      1308  ### Bloco K — Remoção do legado e documentação final
      1309  
      1310  - [ ] **T-055** — Migrar consumidores restantes de `PoliticaR
            eembolso` e `AgregadorTetoHospedagem`
      1311 -  - **O que faz:** primeiro dos quatro passos exigidos por `C
           -LAUDE.md` §6 antes de remover os dois componentes superados: 
           -localizar e migrar todo teste que ainda chama diretamente `Ag
           -regadorTetoHospedagem.aplicar(...)` ou as sobrecargas antigas
           - de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` que de
           -pendem de `PoliticaReembolso.padrao()`, redirecionando-os par
           -a as APIs novas (com a fixture de política histórica de T-050
           - fazendo o papel de `PoliticaReembolso.padrao()`) ou para `Ag
           -regadorTetoIndividual`, conforme o caso.                     
      1311 +  - **O que faz:** primeiro dos quatro passos exigidos por `C
           +LAUDE.md` §6 antes de remover os dois componentes superados. 
           +A migração é **obrigatória e completa** — não uma escolha cas
           +o a caso: todo teste que hoje consome, direta ou indiretament
           +e, `PoliticaReembolso` (inclusive via as sobrecargas antigas 
           +de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` que o u
           +sam internamente) é migrado para `PoliticaExterna`/`TabelaPol
           +iticaResolvida` (usando a fixture de política histórica de T-
           +050 no lugar de `PoliticaReembolso.padrao()`); todo teste que
           + hoje chama `AgregadorTetoHospedagem.aplicar(...)` diretament
           +e é migrado para `AgregadorTetoIndividual`. Ao final desta ta
           +sk, nenhum teste em `tests/java` referencia `PoliticaReembols
           +o` nem `AgregadorTetoHospedagem` — as únicas ocorrências rest
           +antes em todo o repositório, dentro de `src/main/java`, são o
           +s dois arquivos-alvo e as sobrecargas de compatibilidade que 
           +ainda os chamam internamente (a serem removidas em T-056).   
      1312    - **RN atendidas:** nenhuma nova — é migração de consumidor
            es internos, não regra de negócio.
      1313    - **DT/seções do plan:** DT-011 (nota de supersessão); plan
             §19 ("Substituídos").
      1314    - **Dependências:** T-041 (sobrecargas novas de `AvaliadorR
            egrasIndividuais`), T-043 (sobrecargas novas de `AgregadorTet
            oDiario`), T-044 (`AgregadorTetoIndividual`), T-050 (fixture 
            de política histórica).
      1315    - **Arquivos que cria/modifica:**
      1316 -    - `git grep "PoliticaReembolso"` e `git grep "AgregadorTe
           -toHospedagem"` primeiro, para levantar a lista exata de arqui
           -vos afetados (provavelmente `TetoHospedagemTest.java` e qualq
           -uer teste que ainda construa `AvaliadorRegrasIndividuais`/`Ag
           -regadorTetoDiario` via as sobrecargas de dois/um argumento se
           -m política externa).                                         
      1317 -    - Os arquivos de teste identificados pelo `git grep` acim
           -a.                                                           
      1316 +    - Todos os arquivos apontados por `git grep "PoliticaReem
           +bolso" -- tests/java` e `git grep "AgregadorTetoHospedagem" -
           +- tests/java` antes da migração (levantados no passo 1 abaixo
           +) — inclui, no mínimo, `TetoHospedagemTest.java` e qualquer t
           +este de T-006 a T-021 que ainda construa `AvaliadorRegrasIndi
           +viduais`/`AgregadorTetoDiario` via as sobrecargas de um/dois 
           +argumentos.                                                  
      1317    - **Passos de implementação:**
      1319 -    1. Rodar `git grep "PoliticaReembolso"` e `git grep "Agre
           -gadorTetoHospedagem"` e listar cada ocorrência fora dos dois 
           -arquivos que serão removidos em T-056.                       
      1320 -    2. Para cada teste que chama `AgregadorTetoHospedagem.apl
           -icar(...)` diretamente, decidir: se o cenário já está coberto
           - por `AgregadorTetoIndividualTest` (T-044), remover a duplica
           -ção; se cobre algo novo, portar o cenário para `AgregadorTeto
           -IndividualTest` usando a política de teste equivalente.      
      1321 -    3. Para testes que dependem implicitamente de `PoliticaRe
           -embolso.padrao()` via as sobrecargas antigas de `AvaliadorReg
           -rasIndividuais`/`AgregadorTetoDiario` (T-006 a T-021), decidi
           -r, caso a caso, entre manter a sobrecarga antiga como está (e
           -la não depende de arquivo externo e continua sendo um teste u
           -nitário legítimo do mecanismo, independente da fonte da polít
           -ica) ou migrá-la para a política de teste externa — a decisão
           - default é **manter**, porque essas sobrecargas testam o meca
           -nismo de teto/elegibilidade em si, não a fonte dos valores; s
           -ó migrar se o teste especificamente afirmar algo sobre `Polit
           -icaReembolso` como classe.                                   
      1322 -    4. Rodar `mvn test` completo após cada migração.         
      1318 +    1. Rodar `git grep "PoliticaReembolso" -- tests/java` e `
           +git grep "AgregadorTetoHospedagem" -- tests/java` e listar ca
           +da ocorrência.                                               
      1319 +    2. Migrar **todos** os testes que chamam `AgregadorTetoHo
           +spedagem.aplicar(...)` diretamente para `AgregadorTetoIndivid
           +ual.aplicar(...)`, usando uma `TabelaPoliticaResolvida` de te
           +ste equivalente — nenhum teste continua chamando `AgregadorTe
           +toHospedagem` depois desta task.                             
      1320 +    3. Migrar **todos** os testes que dependem, direta ou ind
           +iretamente, de `PoliticaReembolso` (inclusive via as sobrecar
           +gas antigas de `AvaliadorRegrasIndividuais.avaliar(item)`/`av
           +aliar(item, envelope)` e `AgregadorTetoDiario.aplicar(itens)`
           +) para as sobrecargas novas, com `PoliticaExterna`/`TabelaPol
           +iticaResolvida` construídas a partir da fixture de política h
           +istórica de T-050 — sem exceção "porque o teste já funciona a
           +ssim".                                                       
      1321 +    4. Rodar `mvn test` completo após a migração.            
      1322 +    5. Confirmar que `git grep "PoliticaReembolso" -- tests/j
           +ava` e `git grep "AgregadorTetoHospedagem" -- tests/java` não
           + retornam mais nenhuma ocorrência.                           
      1323    - **Testes obrigatórios:** suíte completa (`mvn test`) verd
            e após a migração; nenhum teste órfão (que testava algo espec
            ífico de `AgregadorTetoHospedagem` e não tem equivalente em `
            AgregadorTetoIndividual`) foi perdido silenciosamente.
      1324 -  - **Critério de conclusão:** `git grep "AgregadorTetoHosped
           -agem"` só encontra ocorrências dentro do próprio `AgregadorTe
           -toHospedagem.java` (a ser removido em T-056); `mvn test` verd
           -e.                                                           
      1324 +  - **Critério de conclusão:** `git grep "PoliticaReembolso" 
           +-- tests/java` e `git grep "AgregadorTetoHospedagem" -- tests
           +/java` não retornam nenhuma ocorrência; as únicas ocorrências
           + remanescentes em `src/main/java` são os dois arquivos-alvo e
           + as sobrecargas de compatibilidade que os chamam internamente
           +; `mvn test` verde.                                          
      1325    - **Comando de verificação:**
      1326      ```
      1327 -    git grep "PoliticaReembolso"                             
      1328 -    git grep "AgregadorTetoHospedagem"                       
      1327 +    git grep "PoliticaReembolso" -- tests/java               
      1328 +    git grep "AgregadorTetoHospedagem" -- tests/java         
      1329      mvn -q test
      1330      ```
      1331 -  - **Commit sugerido:** `refactor(T-055): migra consumidores
           - de AgregadorTetoHospedagem para AgregadorTetoIndividual`    
      1331 +  - **Commit sugerido:** `refactor(T-055): migra todos os con
           +sumidores de PoliticaReembolso e AgregadorTetoHospedagem`    
      1332    - **Status:** [ ] pendente
      1333  
      1334  - [ ] **T-056** — Remover `PoliticaReembolso` e `AgregadorTet
            oHospedagem`

● Now T-056 (scoped final greps, mandatory removal of dependent overloads):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 10 lines, removed 9 lines
      1332    - **Status:** [ ] pendente
      1333  
      1334  - [ ] **T-056** — Remover `PoliticaReembolso` e `AgregadorTet
            oHospedagem`
      1335 -  - **O que faz:** segundo, terceiro e quarto passos de `CLAU
           -DE.md` §6: com todos os consumidores já migrados (T-055), rem
           -ove `src/main/java/com/desafio/reembolso/modelo/PoliticaReemb
           -olso.java` e `src/main/java/com/desafio/reembolso/pipeline/Ag
           -regadorTetoHospedagem.java`, e as sobrecargas de compatibilid
           -ade que dependiam exclusivamente deles (as sobrecargas antiga
           -s de `AvaliadorRegrasIndividuais.avaliar(item)`/`avaliar(item
           -, envelope)` e `AgregadorTetoDiario.aplicar(itens)` só são re
           -movidas se T-055 confirmou que nenhum teste as usa mais — cas
           -o contrário, permanecem, e esta task remove só os dois arquiv
           -os-alvo).                                                    
      1335 +  - **O que faz:** segundo, terceiro e quarto passos de `CLAU
           +DE.md` §6: com todos os consumidores já migrados (T-055), rem
           +ove `src/main/java/com/desafio/reembolso/modelo/PoliticaReemb
           +olso.java` e `src/main/java/com/desafio/reembolso/pipeline/Ag
           +regadorTetoHospedagem.java`, e **obrigatoriamente** também to
           +das as sobrecargas e imports que dependem deles — nenhuma sob
           +recarga de compatibilidade que dependa de uma classe removida
           + é mantida: as sobrecargas antigas de `AvaliadorRegrasIndivid
           +uais.avaliar(item)`/`avaliar(item, envelope)` e `AgregadorTet
           +oDiario.aplicar(itens)` são removidas nesta task, porque T-05
           +5 já garantiu que nenhum teste as usa mais.                  
      1336    - **RN atendidas:** nenhuma nova — remoção de código supera
            do (DT-007 → DT-011).
      1337    - **DT/seções do plan:** DT-007 (supersessão), DT-011; plan
             §19.
      1338    - **Dependências:** T-055.
      1339    - **Arquivos que cria/modifica:**
      1340      - Remove `src/main/java/com/desafio/reembolso/modelo/Poli
            ticaReembolso.java`
      1341      - Remove `src/main/java/com/desafio/reembolso/pipeline/Ag
            regadorTetoHospedagem.java`
      1342 -    - Qualquer sobrecarga de compatibilidade que ficou sem us
           -o após T-055 (verificado por `git grep`, não removido "por pr
           -ecaução" sem confirmação).                                   
      1342 +    - Remove as sobrecargas de compatibilidade de `AvaliadorR
           +egrasIndividuais` e `AgregadorTetoDiario` que dependiam exclu
           +sivamente de `PoliticaReembolso`, e qualquer import órfão res
           +ultante.                                                     
      1343    - **Passos de implementação:**
      1344 -    1. Rodar `git grep "PoliticaReembolso"` e `git grep "Agre
           -gadorTetoHospedagem"` — confirmar que a única ocorrência rest
           -ante de cada um é o próprio arquivo.                         
      1344 +    1. Rodar `git grep "PoliticaReembolso" -- src/main/java t
           +ests/java` e `git grep "AgregadorTetoHospedagem" -- src/main/
           +java tests/java` — confirmar que a única ocorrência restante 
           +de cada um é o próprio arquivo-alvo (mais, no caso de `Politi
           +caReembolso`, as sobrecargas que serão removidas neste mesmo 
           +passo).                                                      
      1345      2. Remover os dois arquivos.
      1346 -    3. Remover import não utilizado em qualquer classe que ai
           -nda os referenciasse só por import (não deveria haver nenhuma
           -, se T-055 foi completa).                                    
      1347 -    4. Rodar `mvn -q -DskipTests compile` para confirmar que 
           -a remoção não quebra a compilação (nenhuma referência residua
           -l).                                                          
      1346 +    3. Remover as sobrecargas de compatibilidade que dependia
           +m deles e qualquer import não utilizado resultante — nenhuma 
           +sobrecarga dependente de uma classe removida permanece.      
      1347 +    4. Rodar `mvn -q -DskipTests compile` para confirmar que 
           +a remoção não quebra a compilação.                           
      1348      5. Rodar `mvn -q test` completo.
      1349 +    6. Rodar novamente `git grep "PoliticaReembolso" -- src/m
           +ain/java tests/java` e `git grep "AgregadorTetoHospedagem" --
           + src/main/java tests/java` — ambos devem retornar **zero** oc
           +orrências.                                                   
      1350    - **Testes obrigatórios:** nenhum teste novo — a garantia é
             a compilação limpa e a suíte inteira verde após a remoção.
      1350 -  - **Critério de conclusão:** `git grep "PoliticaReembolso"`
           - e `git grep "AgregadorTetoHospedagem"` não retornam nenhuma 
           -ocorrência; `mvn -q -DskipTests compile` e `mvn -q test` verd
           -es.                                                          
      1351 +  - **Critério de conclusão:** `git grep "PoliticaReembolso" 
           +-- src/main/java tests/java` e `git grep "AgregadorTetoHosped
           +agem" -- src/main/java tests/java` não retornam nenhuma ocorr
           +ência; `mvn -q -DskipTests compile` e `mvn -q test` verdes. O
           +s greps são sempre escopados a `src/main/java tests/java` — n
           +unca executados sem pathspec, porque `plan.md`, `tasks.md`, `
           +DECISIONS.md` e os exports de sessão preservam referências hi
           +stóricas legítimas às duas classes, que não são defeito.     
      1352    - **Comando de verificação:**
      1353      ```
      1353 -    git grep "PoliticaReembolso"                             
      1354 -    git grep "AgregadorTetoHospedagem"                       
      1354 +    git grep "PoliticaReembolso" -- src/main/java tests/java 
      1355 +    git grep "AgregadorTetoHospedagem" -- src/main/java tests
           +/java                                                        
      1356      mvn -q -DskipTests compile
      1357      mvn -q test
      1358      ```
      1358 -  - **Commit sugerido:** `refactor(T-056): remove PoliticaRee
           -mbolso e AgregadorTetoHospedagem`                            
      1359 +  - **Commit sugerido:** `refactor(T-056): remove PoliticaRee
           +mbolso, AgregadorTetoHospedagem e sobrecargas dependentes`   
      1360    - **Status:** [ ] pendente
      1361  
      1362  - [ ] **T-057** — README com CLI de quatro flags e documentaç
            ão de política/câmbio

● Now T-057 (four documented commands):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 5 lines, removed 2 lines
      1371      2. Acrescentar uma seção curta explicando `politica.json`
            /`cambio.json` (o que são, onde encontrar exemplos — `exemplo
            s/envelope/`) e os códigos de saída atualizados.
      1372      3. Seguir o próprio README do zero, manualmente, conferin
            do que os comandos documentados produzem os totais esperados.
      1373    - **Testes obrigatórios:** nenhum automatizado — verificaçã
            o manual, seguindo o README do zero (mesmo critério de T-021)
            .
      1374 -  - **Critério de conclusão:** seguindo somente o README, os 
           -quatro comandos de execução (um por cenário financeiro) produ
           -zem os totais corretos, e `mvn test` roda a suíte inteira sem
           - falhas.                                                     
      1374 +  - **Critério de conclusão:** seguindo somente o README, os 
           +**quatro** comandos de execução — um por cenário financeiro (
           +585.43, 351.43, 1143.26, 373.76), os mesmos arquivos verifica
           +dos em T-054 — produzem os totais corretos, e `mvn test` roda
           + a suíte inteira sem falhas.                                 
      1375    - **Comando de verificação:**
      1376      ```
      1377      mvn -q package
      1378 -    java -jar target/motor-reembolso.jar calcular --input exe
           -mplos/despesas-exemplo.json --output resultado.json --politic
           -a tests/resources/fixtures/politica-historica.json --cambio t
           -ests/resources/fixtures/cambio-historico.json                
      1378      mvn -q test
      1379 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/despesas-exemplo.json --output target/verificacao-585.j
           +son --politica tests/resources/fixtures/politica-historica.js
           +on --cambio tests/resources/fixtures/cambio-historico.json   
      1380 +    java -jar target/motor-reembolso.jar calcular --input tes
           +ts/resources/fixtures/envelope-cc-eng-plataforma.json --outpu
           +t target/verificacao-351.json --politica exemplos/envelope/po
           +litica-v4.json --cambio exemplos/envelope/cambio.json        
      1381 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/envelope/despesas-envelope.json --output target/verific
           +acao-rafael.json --politica exemplos/envelope/politica-v4.jso
           +n --cambio exemplos/envelope/cambio.json                     
      1382 +    java -jar target/motor-reembolso.jar calcular --input exe
           +mplos/envelope/despesas-envelope-cc-desconhecido.json --outpu
           +t target/verificacao-dani.json --politica exemplos/envelope/p
           +olitica-v4.json --cambio exemplos/envelope/cambio.json       
      1383      ```
      1384    - **Commit sugerido:** `docs(readme): [T-057] documenta CLI
             de quatro flags e politica/cambio externos`
      1385    - **Status:** [ ] pendente

● Now T-058 (scoped grep):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      1395      1. Percorrer a matriz "Cobertura — RN-019 a RN-022" e "Co
            bertura — CA-024 a CA-049" (ao final deste arquivo) e confirm
            ar, para cada linha, que a task e o teste citados realmente e
            xistem e passam.
      1396      2. Confirmar, por leitura, que nenhuma task deste arquivo
             criou `Decisao.AGUARDANDO_APROVACAO`, fila de aprovação, ou 
            qualquer menção a "R$500" como gatilho de comportamento.
      1397    - **Testes obrigatórios:** nenhum novo — a suíte completa (
            `mvn test`) já cobre todos os identificadores citados na matr
            iz.
      1398 -  - **Critério de conclusão:** matriz de cobertura sem lacuna
           -s; `git grep -i "AGUARDANDO_APROVACAO"` não encontra nenhuma 
           -ocorrência em `src/main/java`.                               
      1398 +  - **Critério de conclusão:** matriz de cobertura sem lacuna
           +s; `git grep -i "AGUARDANDO_APROVACAO" -- src/main/java` não 
           +encontra nenhuma ocorrência — o critério é ausência de implem
           +entação em produção; menções históricas/documentais em `spec.
           +md`, `plan.md` ou `tasks.md` não são defeito, e por isso o gr
           +ep é sempre escopado a `src/main/java`, nunca executado sem p
           +athspec.                                                     
      1399    - **Comando de verificação:**
      1400      ```
      1401      mvn -q test
      1402 -    git grep -i "AGUARDANDO_APROVACAO"                       
      1402 +    git grep -i "AGUARDANDO_APROVACAO" -- src/main/java      
      1403      ```
      1404    - **Commit sugerido:** `docs(tasks): [T-058] confirma rastr
            eabilidade completa do Dia 2 e item C fora de escopo`
      1405    - **Status:** [ ] pendente

● Now updating the matrices (CA-034 to reflect T-049's fixture migration and
  ExemploCompletoTest):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1501  | CA-031 | T-038 | `ConversaoCambialIntegracaoTest` |
      1502  | CA-032 | T-038 | `ConversaoCambialIntegracaoTest` |
      1503  | CA-033 | T-047 | `DuplicidadeEntreMoedasTest` |
      1504 -| CA-034 | T-048 (modelo), T-049 (serialização) | `SaidaCambi
           -oTest`, `EscritorResultadoTest` |                            
      1504 +| CA-034 | T-048 (modelo), T-049 (serialização e migração do 
           +fixture histórico para o schema 1.2) | `SaidaCambioTest`, `Es
           +critorResultadoTest`, `ExemploCompletoTest` |                
      1505  | CA-035 | T-030 | `LeitorPoliticaTest` |
      1506  | CA-036 | T-030, T-031 (política), T-032, T-033 (câmbio), T-
            035 (CLI) | `LeitorPoliticaTest`, `LeitorCambioTest`, `CliCon
            tratoTest` |
      1507  | CA-037 | T-050 | `RegressaoHistoricaTest` |

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1543  | RN-021 | T-026, T-030 | `PoliticaExternaTest`, `LeitorPolit
            icaTest` |
      1544  | RN-022 | T-030, T-031, T-032, T-033, T-035 | `LeitorPolitic
            aTest`, `LeitorCambioTest`, `CliContratoTest` |
      1545  | CA-024 a CA-027 | T-040, T-041, T-046 | `ResolutorPoliticaC
            entroCustoTest`, `CategoriaCentroCustoTest`, `CliContratoTest
            ` |
      1546 -| CA-028 a CA-034 | T-037, T-038, T-047, T-048, T-049 | `Reso
           -lucaoCambioTest`, `ConversaoCambialIntegracaoTest`, `RegraVia
           -gemEfeitoNuloTest`, `DuplicidadeEntreMoedasTest`, `SaidaCambi
           -oTest`, `EscritorResultadoTest` |                            
      1546 +| CA-028 a CA-034 | T-037, T-038, T-047, T-048, T-049 | `Reso
           +lucaoCambioTest`, `ConversaoCambialIntegracaoTest`, `RegraVia
           +gemEfeitoNuloTest`, `DuplicidadeEntreMoedasTest`, `SaidaCambi
           +oTest`, `EscritorResultadoTest`, `ExemploCompletoTest` |     
      1547  | CA-035, CA-036 | T-030, T-031, T-032, T-033, T-035 | `Leito
            rPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |
      1548  | CA-037, CA-038 | T-050, T-051 | `RegressaoHistoricaTest` |
      1549  | CA-039, CA-040 | T-052, T-053 | `IntegracaoEnvelopeTest` |

● Now let's run the mandated verification for problematic phrases and file
  integrity.

  Ran 1 shell command (ctrl+o to expand)

● Now checking the remaining problematic phrases and unscoped grep commands:

  Ran 1 shell command (ctrl+o to expand)

✻ Brewed for 14m 29s

※ recap: Updated tasks.md to add 37 new Dia 2 tasks (T-022–T-058) for the política v4 changes, following the required blocks and coverage matrices. It's done and awaiting your review before any commit. (disable recaps in /config)

> Faça uma última correção cirúrgica no backlog do Dia 2.                       
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Não altere:                                                                   
                                                                                
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - README.md;                                                                  
  - código;                                                                     
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Mantenha:                                                                     
                                                                                
  - versão 1.1;                                                                 
  - status em revisão;                                                          
  - data 2026-08-05;                                                            
  - spec 1.2 aprovada;                                                          
  - plan 1.1 aprovado;                                                          
  - T-001 a T-021 integralmente intactas;                                       
  - T-022 a T-058 contínuas;                                                    
  - 58 tasks no total;                                                          
  - 37 tasks do Dia 2;                                                          
  - todas as tasks T-022 a T-058 pendentes;                                     
  - exatamente um commit por task do Dia 2.                                     
                                                                                
  Não crie, remova ou renumere tasks.                                           
                                                                                
  Não execute /export, git add, commit ou push.                                 
                                                                                
  ## 1. T-025 — corrigir a declaração completa do record                        
                                                                                
  Substitua os dois trechos separados do exemplo por uma única declaração Java  
  válida:                                                                       
                                                                                
  ```java                                                                       
  public record TabelaCategoria(                                                
      BigDecimal limite,                                                        
      Periodicidade periodicidade                                               
  ) {                                                                           
      public TabelaCategoria {                                                  
          Objects.requireNonNull(limite, "limite");                             
          Objects.requireNonNull(periodicidade, "periodicidade");               
      }                                                                         
  }                                                                             
  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Now I'll merge these into a single valid Java declaration, exactly as
  requested.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 6 lines, removed 8 lines
      583      - `tests/java/com/desafio/reembolso/modelo/TabelaCategoria
           Test.java`
      584    - **Passos de implementação:**
      585      1. `Periodicidade`: enum com dois valores, `DIA` e `DIARIA
           ` — sem serialização JSON própria (é consumido internamente, n
           unca escrito na saída).
      586 -    2. `TabelaCategoria`: adotado definitivamente como `record
          -`, sem alternativa em aberto:                                 
      586 +    2. `TabelaCategoria`: adotado definitivamente como `record
          +`, com construtor compacto que rejeita nulos — sem alternativa
          + em aberto:                                                   
      587         ```java
      588         public record TabelaCategoria(
      589             BigDecimal limite,
      590             Periodicidade periodicidade
      591 -       )                                                      
      592 -       ```                                                    
      593 -       com construtor compacto que rejeita nulos:             
      594 -       ```java                                                
      595 -       public TabelaCategoria {                               
      596 -           Objects.requireNonNull(limite, "limite");          
      597 -           Objects.requireNonNull(periodicidade, "periodicidad
          -e");                                                          
      591 +       ) {                                                    
      592 +           public TabelaCategoria {                           
      593 +               Objects.requireNonNull(limite, "limite");      
      594 +               Objects.requireNonNull(periodicidade, "periodic
          +idade");                                                      
      595 +           }                                                  
      596         }
      597         ```
      598    - **Testes obrigatórios:** `TabelaCategoriaTest` — construçã
           o válida preserva os dois campos; `limite` ou `periodicidade` 
           nulos lançam `NullPointerException` (falha rápida, sem estado 
           parcial).

● Let me verify this was the only change needed and run the standard integrity
  checks.

  Ran 2 shell commands (ctrl+o to expand)

● All empty — nothing else touched. Now confirming task numbering, pending
  status, and header fields are all still intact.

  Ran 1 shell command (ctrl+o to expand)

● All confirmed intact:

  - Versão 1.1 · Status em revisão · 2026-08-05 · spec 1.2 aprovada · plan 1.1 
  aprovado — unchanged.
  - 58 tasks total, T-001 a T-058, sem lacunas (last=058, count=58).
  - 21 tasks concluídas (T-001–T-021), 37 pendentes (T-022–T-058) — matches
  exactly.
  - Um commit por task rule preserved (untouched by this edit).
  - Only tasks.md changed — spec.md, plan.md, DECISIONS.md, README.md, src,
  tests, exemplos all show empty diffs.
  - No /export, git add, commit, or push executed.

  Correção aplicada: em T-025, os dois blocos de código Java separados (a
  declaração do record TabelaCategoria(...) e, isolado depois, o construtor
  compacto public TabelaCategoria { ... }) foram unificados em uma única
  declaração Java sintaticamente válida — o construtor compacto agora está
  corretamente aninhado dentro do corpo { } do record, exatamente como você
  especificou. Nenhuma outra parte do arquivo foi tocada.

  git status --short:
   M specs/001-motor-reembolso/tasks.md

  Parado para revisão.

✻ Churned for 59s

※ recap: We're building out the Dia 2 (política v4) task backlog in tasks.md. Just fixed a broken code snippet in T-025's record declaration; next step is your review before any further changes. (disable recaps in /config)

> A correção anterior ficou incompleta.                                         
                                                                                
  Você alterou somente a declaração de TabelaCategoria em T-025. Essa mudança   
  está correta e deve ser preservada.                                           
                                                                                
  Agora aplique todos os demais ajustes abaixo em:                              
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Altere exclusivamente esse arquivo.                                           
                                                                                
  Não altere:                                                                   
                                                                                
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - README.md;                                                                  
  - código;                                                                     
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Mantenha:                                                                     
                                                                                
  - versão 1.1;                                                                 
  - status em revisão;                                                          
  - data 2026-08-05;                                                            
  - spec 1.2 aprovada;                                                          
  - plan 1.1 aprovado;                                                          
  - T-001 a T-021 integralmente intactas;                                       
  - T-022 a T-058 contínuas;                                                    
  - 58 tasks no total;                                                          
  - 37 tasks pendentes no Dia 2;                                                
  - regra de exatamente um commit por task;                                     
  - declaração Java válida de TabelaCategoria já corrigida em T-025.            
                                                                                
  Não execute /export, git add, commit ou push.                                 
                                                                                
  ## 1. Corrigir todos os acessos a TabelaCategoria                             
                                                                                
  TabelaCategoria é um record e seus acessores são exclusivamente:              
                                                                                
  limite()                                                                      
  periodicidade()                                                               
                                                                                
  Substitua todas as ocorrências de:                                            
                                                                                
  TabelaCategoria.getLimite()                                                   
  .getLimite()                                                                  
  getPeriodicidade()                                                            
                                                                                
  quando se referirem a TabelaCategoria.                                        
                                                                                
  Corrija especialmente:                                                        
                                                                                
  ### T-031                                                                     
                                                                                
  Use:                                                                          
                                                                                
  configuracao.limite()                                                         
                                                                                
  ou, quando o objeto vier diretamente do mapa:                                 
                                                                                
  politica.getCentrosCusto()                                                    
      .get("CENTRO")                                                            
      .get("categoria")                                                         
      .limite()                                                                 
                                                                                
  ### T-041                                                                     
                                                                                
  Use:                                                                          
                                                                                
  TabelaCategoria configuracao =                                                
      tabela.getCategorias().get(item.categoriaNormalizada());                  
                                                                                
  configuracao.limite().compareTo(BigDecimal.ZERO)                              
                                                                                
  ### T-043                                                                     
                                                                                
  Use explicitamente:                                                           
                                                                                
  TabelaCategoria configuracao =                                                
      tabela.getCategorias().get(categoria);                                    
                                                                                
  configuracao.periodicidade() == Periodicidade.DIA                             
  configuracao.limite()                                                         
                                                                                
  ### T-044                                                                     
                                                                                
  Use explicitamente:                                                           
                                                                                
  TabelaCategoria configuracao =                                                
      tabela.getCategorias().get(categoria);                                    
                                                                                
  configuracao.periodicidade() == Periodicidade.DIARIA                          
  configuracao.limite()                                                         
                                                                                
  ## 2. Corrigir T-033                                                          
                                                                                
  Remova:                                                                       
                                                                                
  Sem alteração de produção esperada — mesma contingência de T-015 se um        
  defeito real aparecer.                                                        
                                                                                
  Substitua por:                                                                
                                                                                
  Nenhuma alteração de produção é esperada. Se os novos testes revelarem um     
  defeito real em LeitorCambio, a correção de produção e os testes entram       
  juntos no único commit de T-033, cuja mensagem muda de test(T-033) para       
  fix(T-033). Nunca são criados dois commits para esta task.                    
                                                                                
  ## 3. Corrigir T-039 para executar os estágios reais                          
                                                                                
  Para os cenários de coexistência de motivos individuais, o teste pode         
  executar até:                                                                 
                                                                                
  AvaliadorRegrasIndividuais                                                    
                                                                                
  Mas, para comprovar ausência de duplicidade e teto, o teste deve continuar    
  por:                                                                          
                                                                                
  SeletorElegiveis                                                              
  DetectorDuplicidadeEconomica                                                  
  SeletorElegiveis                                                              
  AgregadorTetoDiario                                                           
  AgregadorTetoHospedagem                                                       
                                                                                
  Nesta altura do backlog, T-039 ainda utiliza os agregadores e sobrecargas     
  históricas, porque os componentes novos dos blocos G e H ainda não foram      
  implementados.                                                                
                                                                                
  O teste deve comprovar operacionalmente:                                      
                                                                                
  - item com MOEDA_SEM_COTACAO não entra na população entregue ao               
  DetectorDuplicidadeEconomica;                                                 
  - não recebe DUPLICIDADE;                                                     
  - não entra na população entregue aos agregadores;                            
  - não recebe qualquer motivo de teto;                                         
  - pode coexistir com CATEGORIA_FORA_POLITICA;                                 
  - pode coexistir com FORA_COMPETENCIA;                                        
  - não recebe VALOR_NAO_POSITIVO;                                              
  - não recebe NOTA_FISCAL_AUSENTE.                                             
                                                                                
  Não deixe a execução parar no AvaliadorRegrasIndividuais para as asserções de 
  duplicidade e teto.                                                           
                                                                                
  ## 4. Corrigir T-041                                                          
                                                                                
  A nova sobrecarga que recebe:                                                 
                                                                                
  TabelaPoliticaResolvida tabela                                                
  PoliticaExterna politica                                                      
                                                                                
  não pode chamar o método histórico avaliarRn006ERn007, pois ele consulta o    
  conjunto fixo de categorias do Dia 1.                                         
                                                                                
  Preserve avaliarRn006ERn007 somente para as sobrecargas históricas.           
                                                                                
  Para a nova sobrecarga, planeje um método separado:                           
                                                                                
  avaliarRn006(...)                                                             
                                                                                
  ou nome semântico equivalente, que:                                           
                                                                                
  - copia os motivos existentes;                                                
  - avalia somente VALOR_NAO_POSITIVO;                                          
  - não consulta CATEGORIAS_REEMBOLSAVEIS;                                      
  - não produz RN-007.                                                          
                                                                                
  A avaliação da categoria na nova sobrecarga deve ocorrer somente pela         
  TabelaPoliticaResolvida:                                                      
                                                                                
  - categoria ausente + origem PADRAO:                                          
    - CATEGORIA_FORA_POLITICA;                                                  
    - RN-007;                                                                   
                                                                                
  - categoria ausente + origem CENTRO_CUSTO:                                    
    - CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO;                                  
    - RN-019;                                                                   
                                                                                
  - categoria presente com configuracao.limite() == 0:                          
    - CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO;                                  
    - RN-019;                                                                   
                                                                                
  - categoria presente com limite positivo:                                     
    - nenhum motivo de categoria.                                               
                                                                                
  Deixe explícito que categoria dinâmica válida, como representacao, não pode   
  ser recusada pelo conjunto histórico fixo.                                    
                                                                                
  Mantenha o teste de coexistência no mesmo item:                               
                                                                                
  MOEDA_SEM_COTACAO                                                             
  CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO                                       
                                                                                
  Acrescente RN-020 à linha “RN atendidas” de T-041, indicando que se refere à  
  coexistência e à independência das regras que não dependem de                 
  valor_normalizado.                                                            
                                                                                
  ## 5. Corrigir T-048                                                          
                                                                                
  Substitua o passo de criarOrdemCampo por somente:                             
                                                                                
  ID=0                                                                          
  DATA=1                                                                        
  CATEGORIA=2                                                                   
  DESCRICAO=3                                                                   
  FORNECEDOR=4                                                                  
  VALOR=5                                                                       
  MOEDA=6                                                                       
  TEM_NOTA_FISCAL=7                                                             
                                                                                
  Remova as frases intermediárias incorretas sobre:                             
                                                                                
  MOEDA = 5                                                                     
  TEM_NOTA_FISCAL de 6 para 6                                                   
                                                                                
  Substitua:                                                                    
                                                                                
  tabela completa de onze entradas                                              
                                                                                
  por:                                                                          
                                                                                
  onze estágios, numerados de 0 a 10; o mapa contém uma entrada para cada       
  MotivoCodigo, e vários códigos podem compartilhar o mesmo estágio.            
                                                                                
  Corrija os quatro formatos de SaidaCambioTest:                                
                                                                                
  ### BRL válido                                                                
                                                                                
  moeda = "BRL"                                                                 
  taxa_cambio_aplicada = 1                                                      
  data_cotacao_utilizada = null                                                 
                                                                                
  ### Moeda estrangeira convertida                                              
                                                                                
  moeda = código original válido                                                
  taxa_cambio_aplicada = taxa utilizada                                         
  data_cotacao_utilizada = data exata ou anterior utilizada                     
                                                                                
  ### Campo moeda estruturalmente inválido                                      
                                                                                
  moeda = null                                                                  
  taxa_cambio_aplicada = null                                                   
  data_cotacao_utilizada = null                                                 
                                                                                
  ### Moeda válida sem cotação                                                  
                                                                                
  moeda = código original válido                                                
  taxa_cambio_aplicada = null                                                   
  data_cotacao_utilizada = null                                                 
  valor_normalizado = null                                                      
  valor_reembolsavel = 0.00                                                     
  motivo = MOEDA_SEM_COTACAO                                                    
  campo = despesa.moeda                                                         
                                                                                
  Não diga “os três nulos” no cenário de moeda válida sem cotação.              
                                                                                
  Inclua ainda um cenário em que um campo não utilizado por RN-020 esteja       
  estruturalmente inválido, por exemplo descricao, fornecedor, categoria ou     
  tem_nota_fiscal, enquanto valor, moeda e data são válidos.                    
                                                                                
  Nesse cenário, taxa, data da cotação e conversão devem continuar sendo        
  resolvidas normalmente.                                                       
                                                                                
  ## 6. Atualizar as duas matrizes de RN-020                                    
                                                                                
  Na tabela detalhada “Cobertura — RN-019 a RN-022”, acrescente em RN-020:      
                                                                                
  T-041 — coexistência com CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO              
                                                                                
  E acrescente aos testes:                                                      
                                                                                
  CategoriaCentroCustoTest                                                      
                                                                                
  Na “Matriz final — RN/CA do Dia 2”, a linha RN-020 também deve incluir:       
                                                                                
  T-041                                                                         
  CategoriaCentroCustoTest                                                      
                                                                                
  Preserve todas as demais tasks e testes já associados a RN-020.               
                                                                                
  ## 7. Ampliar o inventário de T-055                                           
                                                                                
  Antes da migração, T-055 deve executar:                                       
                                                                                
  git grep "AvaliadorRegrasIndividuais.avaliar" -- tests/java                   
  git grep "AvaliadorRegrasIndividuais.avaliarLista" -- tests/java              
  git grep "AgregadorTetoDiario.aplicar" -- tests/java                          
  git grep "AgregadorTetoHospedagem.aplicar" -- tests/java                      
  git grep "PoliticaReembolso" -- tests/java                                    
  git grep "AgregadorTetoHospedagem" -- tests/java                              
                                                                                
  Para cada resultado:                                                          
                                                                                
  - identificar qual sobrecarga está sendo utilizada;                           
  - migrar chamadas das sobrecargas históricas para                             
  PoliticaExterna/TabelaPoliticaResolvida;                                      
  - preservar chamadas que já usam as APIs novas;                               
  - migrar toda chamada de AgregadorTetoHospedagem para                         
  AgregadorTetoIndividual.                                                      
                                                                                
  O critério de T-055 continua exigindo:                                        
                                                                                
  git grep "PoliticaReembolso" -- tests/java                                    
                                                                                
  sem resultado;                                                                
                                                                                
  git grep "AgregadorTetoHospedagem" -- tests/java                              
                                                                                
  sem resultado.                                                                
                                                                                
  Registre que a remoção das sobrecargas em T-056, seguida por:                 
                                                                                
  mvn -q -DskipTests compile                                                    
  mvn -q test                                                                   
                                                                                
  é a verificação definitiva de que não ficou consumidor indireto.              
                                                                                
  ## 8. Verificação textual obrigatória                                         
                                                                                
  Confirme que não resta nenhuma ocorrência de:                                 
                                                                                
  .getLimite()                                                                  
  getPeriodicidade()                                                            
  mesma contingência de T-015                                                   
  Manter `avaliarRn006ERn007` (valor não positivo) inalterado                   
  válida sem cotação: os três nulos                                             
  tabela completa de onze entradas                                              
  MOEDA = 5                                                                     
  TEM_NOTA_FISCAL de 6 para 6                                                   
                                                                                
  Confirme também que:                                                          
                                                                                
  - T-039 não para no avaliador para testar duplicidade e tetos;                
  - T-041 aparece nas duas matrizes de RN-020;                                  
  - CategoriaCentroCustoTest aparece nas duas matrizes de RN-020;               
  - os seis comandos de inventário aparecem em T-055;                           
  - T-001 a T-021 permanecem intactas;                                          
  - T-022 a T-058 permanecem contínuas e pendentes.                             
                                                                                
  Execute:                                                                      
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- README.md                                                         
  git diff -- src                                                               
  git diff -- tests                                                             
                                                                                
  Confirme:                                                                     
                                                                                
  - somente tasks.md foi alterado;                                              
  - nenhum arquivo de código, teste ou fixture foi alterado;                    
  - nenhum /export, git add, commit ou push foi executado.                      
                                                                                
  Ao final, informe:                                                            
                                                                                
  1. quantas ocorrências de getLimite/getPeriodicidade foram removidas;         
  2. como T-039 passou a executar duplicidade e tetos;                          
  3. como a nova sobrecarga de T-041 separa RN-006 da RN-007 histórica;         
  4. os quatro formatos finais de T-048;                                        
  5. como RN-020 foi atualizada nas duas matrizes;                              
  6. os seis comandos de inventário de T-055;                                   
  7. git status --short.                                                        
                                                                                
  Pare para revisão.                                                            

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      743      - `tests/java/com/desafio/reembolso/leitor/LeitorPoliticaT
           est.java` (mesma classe de T-030, casos adicionais)
      744    - **Passos de implementação:**
      745      1. Escrever os casos de teste listados abaixo, comprovando
            o comportamento já implementado em T-030 — se algum cenário r
           evelar um defeito real em `LeitorPolitica` (comportamento dife
           rente do que T-030 já deveria garantir), corrigir `LeitorPolit
           ica.java` e o teste **no mesmo e único commit** desta task, e 
           nesse caso a mensagem passa de `test(T-031)` para `fix(T-031)`
            — nunca dois commits para a mesma task (ver a qualificação de
            commit único de tasks de teste, no topo deste arquivo).
      746 -  - **Testes obrigatórios:** `LeitorPoliticaTest` — `limite: 0
          -` em `padrao` (arquivo inválido — CA-045 negativo); `limite: 0
          -` numa tabela de `centros_custo` (arquivo válido, `TabelaCateg
          -oria.getLimite() == 0.00`); campo desconhecido dentro de uma c
          -onfiguração de categoria (ignorado, arquivo válido); campo des
          -conhecido na raiz, além de `versao`/`acrescimo_em_viagem_perce
          -ntual` (ignorado, arquivo válido); um `PoliticaExterna` constr
          -uído a partir de um arquivo válido tem `padrao`/`centrosCusto`
          - imutáveis (tentativa de modificação via getter lança `Unsuppo
          -rtedOperationException`).                                     
      746 +  - **Testes obrigatórios:** `LeitorPoliticaTest` — `limite: 0
          +` em `padrao` (arquivo inválido — CA-045 negativo); `limite: 0
          +` numa tabela de `centros_custo` (arquivo válido; acessado com
          +o `politica.getCentrosCusto().get("CENTRO").get("categoria").l
          +imite() == 0.00`, já que `TabelaCategoria` é um `record` cujo 
          +único acessor de limite é `limite()`); campo desconhecido dent
          +ro de uma configuração de categoria (ignorado, arquivo válido)
          +; campo desconhecido na raiz, além de `versao`/`acrescimo_em_v
          +iagem_percentual` (ignorado, arquivo válido); um `PoliticaExte
          +rna` construído a partir de um arquivo válido tem `padrao`/`ce
          +ntrosCusto` imutáveis (tentativa de modificação via getter lan
          +ça `UnsupportedOperationException`).                          
      747    - **Critério de conclusão:** todos os cenários acima verdes;
            `mvn test` completo verde.
      748    - **Comando de verificação:**
      749      ```

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      789    - **Arquivos que cria/modifica:**
      790      - `tests/java/com/desafio/reembolso/leitor/LeitorCambioTes
           t.java` (mesma classe de T-032, casos adicionais)
      791    - **Passos de implementação:**
      792 -    1. Sem alteração de produção esperada — mesma contingência
          - de T-015 se um defeito real aparecer.                        
      792 +    1. Nenhuma alteração de produção é esperada. Se os novos t
          +estes revelarem um defeito real em `LeitorCambio`, a correção 
          +de produção e os testes entram juntos no único commit de T-033
          +, cuja mensagem muda de `test(T-033)` para `fix(T-033)`. Nunca
          + são criados dois commits para esta task.                     
      793      2. Escrever os casos de teste listados abaixo.
      794    - **Testes obrigatórios:** `LeitorCambioTest` — `fonte`/`obs
           ervacao` ausentes (válido); presentes como texto (válido, valo
           r descartado); presentes como número/booleano/lista (arquivo i
           nválido); campo desconhecido na raiz, ao lado de `moeda_base`/
           `fonte`/`observacao`/`taxas` (ignorado, arquivo válido); chave
            de data malformada dentro de `taxas` (ex.: `"2026/07/13"`) — 
           arquivo inválido, não um "campo desconhecido"; chave de moeda 
           malformada dentro de uma data (ex.: `"US"` ou `"usd"`) — arqui
           vo inválido; a `TabelaCambio` resultante de um arquivo válido 
           tem o mapa externo e cada `NavigableMap` interno imutáveis.
      795    - **Critério de conclusão:** todos os cenários acima verdes;
            `mvn test` completo verde.

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Now T-039 — rewriting to execute the real pipeline stages instead of stopping
  at the evaluator:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 8 lines, removed 5 lines
      930    - **Status:** [ ] pendente
      931  
      932  - [ ] **T-039** — `MoedaSemCotacaoTest` — coexistência de moti
           vos (8.4, item 14)
      933 -  - **O que faz:** confirma, com teste dedicado, a exclusão de
          - dependência declarada em `spec.md` 8.4 item 14, no ponto do b
          -acklog em que esta task acontece — **antes** de a política por
          - centro de custo estar integrada ao avaliador (Bloco G, T-040/
          -T-041): um item com `MOEDA_SEM_COTACAO` pode coexistir com `CA
          -TEGORIA_FORA_POLITICA` e/ou `FORA_COMPETENCIA` (que não depend
          -em de `valor_normalizado`, e que já são produzidos pelas sobre
          -cargas históricas de `AvaliadorRegrasIndividuais`), mas **nunc
          -a** com `VALOR_NAO_POSITIVO`, `NOTA_FISCAL_AUSENTE`, `DUPLICID
          -ADE` ou qualquer motivo de teto (que dependem de `valor_normal
          -izado`). Esta task **não** cobre a coexistência com `CATEGORIA
          -_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — esse motivo só existe a part
          -ir de T-041; a coexistência com ele é fechada em T-041, depois
          - que o avaliador de centro de custo passar a existir. Comporta
          -mento já decorre da checagem `valorNormalizado != null` já exi
          -stente em `AvaliadorRegrasIndividuais` (T-008/T-011) — esta ta
          -sk é predominantemente de verificação, sem produção nova esper
          -ada.                                                          
      933 +  - **O que faz:** confirma, **operacionalmente**, a exclusão 
          +de dependência declarada em `spec.md` 8.4 item 14, no ponto do
          + backlog em que esta task acontece — **antes** de a política p
          +or centro de custo estar integrada ao avaliador (Bloco G, T-04
          +0/T-041). Nesta altura do backlog, T-039 ainda utiliza os agre
          +gadores e sobrecargas históricas (`SeletorElegiveis`, `Detecto
          +rDuplicidadeEconomica`, `AgregadorTetoDiario`, `AgregadorTetoH
          +ospedagem`), porque os componentes novos dos blocos G e H aind
          +a não foram implementados. Para os cenários de coexistência de
          + motivos individuais, o teste executa até `AvaliadorRegrasIndi
          +viduais` — mas, para comprovar ausência de duplicidade e de te
          +to, o teste **continua** pelos estágios reais: `SeletorElegive
          +is` → `DetectorDuplicidadeEconomica` → `SeletorElegiveis` → `A
          +gregadorTetoDiario`/`AgregadorTetoHospedagem`. Um item com `MO
          +EDA_SEM_COTACAO` pode coexistir com `CATEGORIA_FORA_POLITICA` 
          +e/ou `FORA_COMPETENCIA` (produzidos pela sobrecarga histórica 
          +de `AvaliadorRegrasIndividuais`, que não dependem de `valor_no
          +rmalizado`), mas **nunca** recebe `VALOR_NAO_POSITIVO`, `NOTA_
          +FISCAL_AUSENTE`, `DUPLICIDADE` ou qualquer motivo de teto — e 
          +essas ausências são comprovadas fazendo o item atravessar de f
          +ato os estágios reais de seleção, duplicidade e agregação, não
          + apenas inspecionando a lista de motivos parada em `AvaliadorR
          +egrasIndividuais`. Esta task **não** cobre a coexistência com 
          +`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — esse motivo só exi
          +ste a partir de T-041; a coexistência com ele é fechada em T-0
          +41, depois que o avaliador de centro de custo passar a existir
          +.                                                             
      934    - **RN atendidas:** RN-020.
      935    - **CA atendidos:** base de rastreabilidade para o item 14 d
           e 8.4 (não tem CA numerado próprio — é comportamento de compos
           ição, já coberto indiretamente por CA-030 e pelos cenários de 
           `§12.3`/`§12.4`).
      936    - **DT/seções do plan:** plan §2 (exclusão nova, item 14 da 
           lista de 8.4), §9.
     ...
      938    - **Arquivos que cria/modifica:**
      939      - `tests/java/com/desafio/reembolso/pipeline/MoedaSemCotac
           aoTest.java`
      940    - **Passos de implementação:**
      941 -    1. Escrever os cenários abaixo, executando o pipeline até 
          -`AvaliadorRegrasIndividuais` usando as sobrecargas históricas 
          -(`avaliar(item, envelope)`), que já produzem `CATEGORIA_FORA_P
          -OLITICA`/`FORA_COMPETENCIA`/`VALOR_NAO_POSITIVO`/`NOTA_FISCAL_
          -AUSENTE` sem depender de política externa.                    
      942 -    2. Se algum cenário revelar um defeito real, corrigir o có
          -digo de produção **no único commit** desta task — ver a qualif
          -icação de commit único de tasks de teste, no topo deste arquiv
          -o: nunca `fix(T-039)` seguido de um segundo commit `test(T-039
          -)`.                                                           
      943 -  - **Testes obrigatórios:** `MoedaSemCotacaoTest` — moeda est
          -rangeira sem cotação, categoria fora da política → `MOEDA_SEM_
          -COTACAO` **e** `CATEGORIA_FORA_POLITICA` coexistem; moeda estr
          -angeira sem cotação, data fora da competência → `MOEDA_SEM_COT
          -ACAO` **e** `FORA_COMPETENCIA` coexistem; moeda estrangeira se
          -m cotação, valor que seria não positivo se calculável → **ausê
          -ncia** de `VALOR_NAO_POSITIVO` (só `MOEDA_SEM_COTACAO`, porque
          - `valorNormalizado` é nulo); moeda estrangeira sem cotação, it
          -em que teria excedido o gatilho de nota fiscal se convertido →
          - **ausência** de `NOTA_FISCAL_AUSENTE`; moeda estrangeira sem 
          -cotação, item que seria duplicata econômica se o valor fosse c
          -alculável → **ausência** de `DUPLICIDADE`; moeda estrangeira s
          -em cotação, item que estaria sujeito a teto se elegível → **au
          -sência** de qualquer motivo de teto.                          
      944 -  - **Critério de conclusão:** todos os cenários acima verdes;
          - `mvn test` completo verde.                                   
      941 +    1. Para cada cenário, construir o item pelo pipeline real 
          +(`ValidadorItem` → `DetectorIdDuplicado` → `ResolutorCambio` →
          + `Normalizador` → `AvaliadorRegrasIndividuais`, sobrecarga his
          +tórica `avaliar(item, envelope)`), e verificar ali a coexistên
          +cia com `CATEGORIA_FORA_POLITICA`/`FORA_COMPETENCIA` e a ausên
          +cia de `VALOR_NAO_POSITIVO`/`NOTA_FISCAL_AUSENTE`.            
      942 +    2. Passar o resultado por `SeletorElegiveis.selecionar(...
          +)` e confirmar que o item com `MOEDA_SEM_COTACAO` **não** está
          + na população elegível devolvida (ele tem motivos, logo não é 
          +elegível).                                                    
      943 +    3. Passar essa população (sem o item de `MOEDA_SEM_COTACAO
          +`, já excluído no passo 2) por `DetectorDuplicidadeEconomica.d
          +etectar(...)` e confirmar que o item nunca recebe `DUPLICIDADE
          +`, precisamente porque nunca chega a entrar na comparação de c
          +have econômica.                                               
      944 +    4. Passar o resultado por um segundo `SeletorElegiveis.sel
          +ecionar(...)` e pelos dois agregadores de teto históricos (`Ag
          +regadorTetoDiario.aplicar(...)`, `AgregadorTetoHospedagem.apli
          +car(...)`) e confirmar que o item com `MOEDA_SEM_COTACAO` nunc
          +a aparece nas listas de `ResultadoTeto` produzidas — nenhum mo
          +tivo de teto (`TETO_DIARIO_APLICADO`, `TETO_DIARIO_ESGOTADO`, 
          +`TETO_HOSPEDAGEM_APLICADO`) é atribuído a ele.                
      945 +    5. Se algum cenário revelar um defeito real, corrigir o có
          +digo de produção **no único commit** desta task — ver a qualif
          +icação de commit único de tasks de teste, no topo deste arquiv
          +o: nunca `fix(T-039)` seguido de um segundo commit `test(T-039
          +)`.                                                           
      946 +  - **Testes obrigatórios:** `MoedaSemCotacaoTest` — comprova,
          + executando os estágios reais do pipeline (`SeletorElegiveis`,
          + `DetectorDuplicidadeEconomica`, `SeletorElegiveis`, `Agregado
          +rTetoDiario`, `AgregadorTetoHospedagem`, não apenas a saída de
          + `AvaliadorRegrasIndividuais`): item com `MOEDA_SEM_COTACAO` n
          +ão entra na população entregue ao `DetectorDuplicidadeEconomic
          +a`; não recebe `DUPLICIDADE`; não entra na população entregue 
          +aos agregadores de teto; não recebe qualquer motivo de teto; p
          +ode coexistir com `CATEGORIA_FORA_POLITICA`; pode coexistir co
          +m `FORA_COMPETENCIA`; não recebe `VALOR_NAO_POSITIVO`; não rec
          +ebe `NOTA_FISCAL_AUSENTE`.                                    
      947 +  - **Critério de conclusão:** todos os cenários acima verdes,
          + com a execução chegando de fato aos estágios de duplicidade e
          + teto (nunca parando em `AvaliadorRegrasIndividuais` para essa
          +s duas asserções); `mvn test` completo verde.                 
      948    - **Comando de verificação:**
      949      ```
      950      mvn -q test -Dtest=MoedaSemCotacaoTest

● Now let's read T-041 fully to rewrite it per item 4:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Now T-041 — full rewrite per item 4 (separating RN-006 from historical RN-007
  logic):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 8 lines, removed 7 lines
      979    - **Status:** [ ] pendente
      980  
      981  - [ ] **T-041** — `AvaliadorRegrasIndividuais` consome polític
           a externa
      982 -  - **O que faz:** acrescenta a `AvaliadorRegrasIndividuais` u
          -ma nova sobrecarga que recebe `TabelaPoliticaResolvida` e `Pol
          -iticaExterna` (para o gatilho de nota fiscal, RN-009 atualizad
          -a) e implementa RN-019: categoria ausente da tabela aplicável 
          -→ `CATEGORIA_FORA_POLITICA` quando `origem == PADRAO`, `CATEGO
          -RIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` quando `origem == CENTRO_CU
          -STO`; categoria presente com `limite == 0` → `CATEGORIA_NAO_RE
          -EMBOLSAVEL_CENTRO_CUSTO` (nunca `PARCIALMENTE_REEMBOLSADO`). A
          -s sobrecargas antigas (`avaliar(item)`, `avaliar(item, envelop
          -e)`, com `Set<String>` fixo e `PoliticaReembolso`) **permanece
          -m intactas**, porque a suíte histórica (T-006 a T-021) as usa 
          -— a migração delas para as novas é T-055. Esta task também **f
          -echa** a coexistência de motivos que T-039 deixou pendente: co
          -m o avaliador de centro de custo agora existindo, um item com 
          -`MOEDA_SEM_COTACAO` **e** categoria ausente da tabela de um ce
          -ntro de custo cadastrado coexiste com `CATEGORIA_NAO_REEMBOLSA
          -VEL_CENTRO_CUSTO`, pelo mesmo princípio de 8.4 item 14 (a cate
          -goria não depende de `valor_normalizado`).                    
      983 -  - **RN atendidas:** RN-019, RN-009 (atualizada — gatilho de 
          -`PoliticaExterna`, não de `PoliticaReembolso`).               
      982 +  - **O que faz:** acrescenta a `AvaliadorRegrasIndividuais` u
          +ma nova sobrecarga que recebe `TabelaPoliticaResolvida` e `Pol
          +iticaExterna` (para o gatilho de nota fiscal, RN-009 atualizad
          +a) e avalia categoria **exclusivamente** a partir da tabela re
          +solvida — nunca a partir do `Set<String> CATEGORIAS_REEMBOLSAV
          +EIS` fixo do Dia 1, que não reconhece categorias dinâmicas com
          +o `representacao`. Regra fechada da nova sobrecarga: categoria
          + ausente da tabela + `origem == PADRAO` → `CATEGORIA_FORA_POLI
          +TICA`/`RN-007`; categoria ausente da tabela + `origem == CENTR
          +O_CUSTO` → `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`RN-019`;
          + categoria presente com `configuracao.limite() == 0` → `CATEGO
          +RIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`RN-019` (só ocorre com `or
          +igem == CENTRO_CUSTO`, porque `padrao` com limite zero já foi 
          +rejeitado na leitura, T-030); categoria presente com limite po
          +sitivo → nenhum motivo de categoria. Uma categoria dinâmica vá
          +lida, presente na tabela resolvida com limite positivo (ex.: `
          +representacao` em `CC-COMERCIAL`), nunca pode ser recusada pel
          +o conjunto histórico fixo, porque a nova sobrecarga não o cons
          +ulta. Como o método histórico `avaliarRn006ERn007` mistura RN-
          +006 e RN-007 e consulta `CATEGORIAS_REEMBOLSAVEIS` internament
          +e, a nova sobrecarga **não** o chama — ele permanece exclusivo
          + das sobrecargas históricas (`avaliar(item)`, `avaliar(item, e
          +nvelope)`), preservadas intactas para a suíte de T-006 a T-021
          + (migração para T-055). Um método novo e separado — `avaliarRn
          +006(...)`, ou nome semântico equivalente — copia os motivos já
          + existentes do item e avalia **somente** `VALOR_NAO_POSITIVO`,
          + sem consultar `CATEGORIAS_REEMBOLSAVEIS` e sem produzir `RN-0
          +07`; a categoria é decidida integralmente pela lógica de `Tabe
          +laPoliticaResolvida` descrita acima, dentro da própria nova so
          +brecarga. Esta task também **fecha** a coexistência de motivos
          + que T-039 deixou pendente: com o avaliador de centro de custo
          + agora existindo, um item com `MOEDA_SEM_COTACAO` **e** catego
          +ria ausente da tabela de um centro de custo cadastrado coexist
          +e com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, pelo mesmo pr
          +incípio de 8.4 item 14 — categoria e competência não dependem 
          +de `valor_normalizado`, então continuam avaliadas normalmente 
          +mesmo quando o câmbio falha.                                  
      983 +  - **RN atendidas:** RN-019, RN-009 (atualizada — gatilho de 
          +`PoliticaExterna`, não de `PoliticaReembolso`), RN-020 (coexis
          +tência de `MOEDA_SEM_COTACAO` com `CATEGORIA_NAO_REEMBOLSAVEL_
          +CENTRO_CUSTO` — independência das regras de categoria e compet
          +ência, que não dependem de `valor_normalizado`).              
      984    - **CA atendidos:** CA-024 a CA-027 (uso real dentro do aval
           iador de regras).
      985    - **DT/seções do plan:** DT-011; plan §6, §19.
      986    - **Dependências:** T-023 (`CATEGORIA_NAO_REEMBOLSAVEL_CENTR
           O_CUSTO`), T-024 (`RN_019`), T-038 (pipeline de câmbio complet
           o, necessário para o cenário de coexistência com `MOEDA_SEM_CO
           TACAO`), T-040 (`ResolutorPoliticaCentroCusto`).
     ...
       989      - `tests/java/com/desafio/reembolso/pipeline/CategoriaCen
            troCustoTest.java`
       990    - **Passos de implementação:**
       991      1. Nova sobrecarga `avaliar(ItemNormalizado item, Envelop
            e envelope, TabelaPoliticaResolvida tabela, PoliticaExterna p
            olitica): ItemAvaliado` e a correspondente `avaliarLista(...)
            `.
       992 -    2. Dentro dela, substituir a checagem de `CATEGORIAS_REEM
           -BOLSAVEIS.contains(...)` por: se `item.categoriaNormalizada()
           -` ausente de `tabela.getCategorias()`, escolher o motivo conf
           -orme `tabela.getOrigem()`; se presente com `TabelaCategoria.g
           -etLimite().compareTo(BigDecimal.ZERO) == 0`, `CATEGORIA_NAO_R
           -EEMBOLSAVEL_CENTRO_CUSTO` (só pode ocorrer com `origem == CEN
           -TRO_CUSTO`, porque `padrao` com limite zero já foi rejeitado 
           -na leitura, T-030).                                          
       993 -    3. Substituir `politica.getGatilhoNotaFiscal()` (o `Polit
           -icaReembolso` fixo) por `politica.getNotaFiscalObrigatoriaAci
           -maDe()` (o `PoliticaExterna` recebido) na avaliação de RN-009
           -, dentro desta nova sobrecarga.                              
       994 -    4. Manter `avaliarRn006ERn007` (valor não positivo) inalt
           -erado — não depende de política.                             
       995 -  - **Testes obrigatórios:** `CategoriaCentroCustoTest` — os 
           -quatro cenários de CA-024 a CA-027 exercitados diretamente co
           -ntra `AvaliadorRegrasIndividuais` (não só contra `ResolutorPo
           -liticaCentroCusto` isoladamente): centro desconhecido usa `pa
           -drao` e produz `CATEGORIA_FORA_POLITICA` para categoria ausen
           -te; centro cadastrado sem a categoria produz `CATEGORIA_NAO_R
           -EEMBOLSAVEL_CENTRO_CUSTO`; `limite == 0` num centro cadastrad
           -o produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` com `valor
           -_reembolsavel` `0,00` (nunca parcial); gatilho de nota fiscal
           - lido de `PoliticaExterna.getNotaFiscalObrigatoriaAcimaDe()`,
           - não de `PoliticaReembolso`; **e** um caso de coexistência — 
           -moeda estrangeira sem cotação (`MOEDA_SEM_COTACAO`, via `Reso
           -lutorCambio`) combinada com categoria ausente da tabela de um
           - centro de custo cadastrado (`CATEGORIA_NAO_REEMBOLSAVEL_CENT
           -RO_CUSTO`) produz os dois motivos simultaneamente no mesmo it
           -em.                                                          
       996 -  - **Critério de conclusão:** todos os cenários acima verdes
           -, inclusive o de coexistência; `mvn test` completo verde (sob
           -recargas antigas intocadas, suíte histórica passa sem alteraç
           -ão).                                                         
       992 +    2. Dentro dela, chamar um novo método `avaliarRn006(ItemN
           +ormalizado item): List<Motivo>` — copia `item.item().getMotiv
           +os()` e avalia somente `VALOR_NAO_POSITIVO` (mesma lógica de 
           +valor já usada em `avaliarRn006ERn007`, extraída para não dep
           +ender de categoria); **nunca** chama `avaliarRn006ERn007` nem
           + consulta `CATEGORIAS_REEMBOLSAVEIS`.                        
       993 +    3. Avaliar a categoria separadamente, exclusivamente pela
           + `TabelaPoliticaResolvida`: `TabelaCategoria configuracao = t
           +abela.getCategorias().get(item.categoriaNormalizada());` — se
           + `configuracao == null`, motivo conforme `tabela.getOrigem()`
           + (`CATEGORIA_FORA_POLITICA`/`RN_007` para `PADRAO`, `CATEGORI
           +A_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`RN_019` para `CENTRO_CUSTO`
           +); se `configuracao.limite().compareTo(BigDecimal.ZERO) == 0`
           +, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`RN_019`; caso co
           +ntrário, nenhum motivo de categoria.                         
       994 +    4. Substituir `politica.getGatilhoNotaFiscal()` (o `Polit
           +icaReembolso` fixo) por `politica.getNotaFiscalObrigatoriaAci
           +maDe()` (o `PoliticaExterna` recebido) na avaliação de RN-009
           +, dentro desta nova sobrecarga.                              
       995 +    5. Manter `avaliarRn006ERn007` exatamente como está, sem 
           +nenhuma alteração — usado exclusivamente pelas sobrecargas hi
           +stóricas (`avaliar(item)`, `avaliar(item, envelope)`).       
       996 +  - **Testes obrigatórios:** `CategoriaCentroCustoTest` — os 
           +quatro cenários de CA-024 a CA-027 exercitados diretamente co
           +ntra a nova sobrecarga de `AvaliadorRegrasIndividuais` (não s
           +ó contra `ResolutorPoliticaCentroCusto` isoladamente): centro
           + desconhecido usa `padrao` e produz `CATEGORIA_FORA_POLITICA`
           + para categoria ausente; centro cadastrado sem a categoria pr
           +oduz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`; `limite == 0`
           + num centro cadastrado produz `CATEGORIA_NAO_REEMBOLSAVEL_CEN
           +TRO_CUSTO` com `valor_reembolsavel` `0,00` (nunca parcial); u
           +ma categoria dinâmica válida (`representacao`, presente na ta
           +bela com limite positivo) **não** é recusada — prova de que a
           + nova sobrecarga não consulta `CATEGORIAS_REEMBOLSAVEIS`; gat
           +ilho de nota fiscal lido de `PoliticaExterna.getNotaFiscalObr
           +igatoriaAcimaDe()`, não de `PoliticaReembolso`; **e** um caso
           + de coexistência — moeda estrangeira sem cotação (`MOEDA_SEM_
           +COTACAO`, via `ResolutorCambio`) combinada com categoria ause
           +nte da tabela de um centro de custo cadastrado (`CATEGORIA_NA
           +O_REEMBOLSAVEL_CENTRO_CUSTO`) produz os dois motivos simultan
           +eamente no mesmo item.                                       
       997 +  - **Critério de conclusão:** todos os cenários acima verdes
           +, inclusive o de `representacao` e o de coexistência; `mvn te
           +st` completo verde (sobrecargas antigas e `avaliarRn006ERn007
           +` intocados, suíte histórica passa sem alteração).           
       998    - **Comando de verificação:**
       999      ```
      1000      mvn -q test -Dtest=CategoriaCentroCustoTest
● Now let's find and fix T-043 and T-044's .getLimite() usages:

  Ran 1 shell command (ctrl+o to expand)

● Let's view the full T-043 and T-044 sections for context before rewriting:

  Read 1 file (ctrl+o to expand)

● Now fixing T-043's .getLimite() and explicit configuracao pattern:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      1038      - `src/main/java/com/desafio/reembolso/pipeline/Agregador
            TetoDiario.java`
      1039      - `tests/java/com/desafio/reembolso/pipeline/TetoPorPerio
            dicidadeTest.java`
      1040    - **Passos de implementação:**
      1041 -    1. Nova sobrecarga `aplicar(List<ItemAvaliado> itens, Tab
           -elaPoliticaResolvida tabela): List<ResultadoTeto>` — filtra i
           -tens elegíveis cuja categoria normalizada exista em `tabela.g
           -etCategorias()` com `periodicidade == Periodicidade.DIA`.    
      1042 -    2. Limite inicial do saldo por `(data, categoria)` vem de
           - `tabela.getCategorias().get(categoria).getLimite()`, não mai
           -s de `PoliticaReembolso`.                                    
      1041 +    1. Nova sobrecarga `aplicar(List<ItemAvaliado> itens, Tab
           +elaPoliticaResolvida tabela): List<ResultadoTeto>` — para cad
           +a item, obter explicitamente `TabelaCategoria configuracao = 
           +tabela.getCategorias().get(categoria);` e filtrar os elegívei
           +s cujo `configuracao != null && configuracao.periodicidade() 
           +== Periodicidade.DIA` (nunca `tabela.getCategorias()` inspeci
           +onado sem passar por essa variável nomeada).                 
      1042 +    2. Limite inicial do saldo por `(data, categoria)` vem de
           + `configuracao.limite()`, não mais de `PoliticaReembolso`.   
      1043      3. Escolha do `RegraNegocio` do motivo `TETO_DIARIO_APLIC
            ADO`: `RN_011` se `"alimentacao".equals(categoria)`, `RN_012`
             se `"transporte_urbano".equals(categoria)`, `RN_019` caso co
            ntrário.
      1044      4. Reaproveitar `aplicarCorte(...)` sem alteração (já não
             conhece categoria, DT-017 confirma que continua reaproveitáv
            el).
      1045    - **Testes obrigatórios:** `TetoPorPeriodicidadeTest` — cat
            egoria externa `representacao` (`CC-COMERCIAL`, `periodicidad
            e: "dia"`, limite R$300,00) com dois itens na mesma data comp
            artilhando o mesmo saldo, consumido em ordem de `indiceEntrad
            a`, motivo `TETO_DIARIO_APLICADO`/`RN_019` (CA-047).

● Now T-044:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      1061      - `src/main/java/com/desafio/reembolso/pipeline/Agregador
            TetoIndividual.java`
      1062      - `tests/java/com/desafio/reembolso/pipeline/AgregadorTet
            oIndividualTest.java`
      1063    - **Passos de implementação:**
      1064 -    1. `aplicar(List<ItemAvaliado> itens, TabelaPoliticaResol
           -vida tabela): List<ResultadoTeto>` — filtra itens elegíveis c
           -uja categoria esteja em `tabela.getCategorias()` com `periodi
           -cidade == DIARIA`.                                           
      1065 -    2. Para cada item aplicável, chama `AgregadorTetoDiario.a
           -plicarCorte(item, limite, motivo)`, com `limite = tabela.getC
           -ategorias().get(categoria).getLimite()` e `motivo` escolhido 
           -por `"hospedagem".equals(categoria) ? TETO_HOSPEDAGEM_APLICAD
           -O/RN_013 : TETO_INDIVIDUAL_APLICADO/RN_019`.                 
      1064 +    1. `aplicar(List<ItemAvaliado> itens, TabelaPoliticaResol
           +vida tabela): List<ResultadoTeto>` — para cada item, obter ex
           +plicitamente `TabelaCategoria configuracao = tabela.getCatego
           +rias().get(categoria);` e filtrar os elegíveis cujo `configur
           +acao != null && configuracao.periodicidade() == Periodicidade
           +.DIARIA`.                                                    
      1065 +    2. Para cada item aplicável, chama `AgregadorTetoDiario.a
           +plicarCorte(item, configuracao.limite(), motivo)`, com `motiv
           +o` escolhido por `"hospedagem".equals(categoria) ? TETO_HOSPE
           +DAGEM_APLICADO/RN_013 : TETO_INDIVIDUAL_APLICADO/RN_019`.    
      1066      3. Nunca produz `NAO_REEMBOLSADO_TETO_ESGOTADO` — não há 
            saldo compartilhado a esgotar (cada item é avaliado isoladame
            nte).
      1067    - **Testes obrigatórios:** `AgregadorTetoIndividualTest` — 
            hospedagem sob `periodicidade: "diaria"` reproduz o comportam
            ento histórico (R$480,00 → R$250,00, `TETO_HOSPEDAGEM_APLICAD
            O`/`RN_013`, descrição não altera o teto); categoria externa 
            `estacionamento` (limite R$50,00, `periodicidade: "diaria"`) 
            com despesa elegível de R$80,00 → parcial R$50,00, motivo úni
            co `TETO_INDIVIDUAL_APLICADO`/`RN_019` (CA-049); duas hospeda
            gens elegíveis na mesma data rendem até o dobro do limite ind
            ividual (nunca compartilham saldo).
      1068    - **Critério de conclusão:** todos os cenários acima verdes
            .

  Ran 1 shell command (ctrl+o to expand)

● Now let's move to T-048 (item 5). Let me read the full section:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Now rewriting T-048's steps and test scenarios per item 5:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 10 lines, removed 4 lines
      1150      - `tests/java/com/desafio/reembolso/pipeline/SaidaCambioT
            est.java`
      1151    - **Passos de implementação:**
      1152      1. Adicionar os três campos ao `record ResultadoItem`, co
            m construção populada em `componentesDoRegistro(...)` a parti
            r de `itemValidado.getMoeda()`/`getTaxaCambioAplicada()`/`get
            DataCotacaoUtilizada()` — nos três pontos de retorno do métod
            o (recusado antes da duplicidade, recusado por duplicidade, r
            esultado de teto).
      1153 -    2. Atualizar `criarOrdemCampo()`: inserir `CampoCanonico.
           -MOEDA` com valor `5`, deslocar `TEM_NOTA_FISCAL` de `6` para 
           -`6` → **atenção:** a posição correta é `MOEDA = 5`? Não — `VA
           -LOR` já é `5`; a ordem correta é `ID=0, DATA=1, CATEGORIA=2, 
           -DESCRICAO=3, FORNECEDOR=4, VALOR=5, MOEDA=6, TEM_NOTA_FISCAL=
           -7`. Renumerar exatamente assim.                              
      1154 -    3. Atualizar `criarEstagios()` com a tabela completa de o
           -nze entradas descrita acima (0 a 10).                        
      1155 -  - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) —
           - um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` 
           -simultâneos aparece nessa ordem; um item com erro estrutural 
           -em `despesa.moeda` aparece na posição correta da ordem canôni
           -ca de campo (entre `valor` e `tem_nota_fiscal`). `SaidaCambio
           -Test` — os quatro formatos de `moeda`/`taxa_cambio_aplicada`/
           -`data_cotacao_utilizada` no `ResultadoItem` (BRL: `"BRL"`/`1`
           -/`null`; estrangeira convertida: moeda/taxa/data da cotação; 
           -estruturalmente inválida: os três nulos; válida sem cotação: 
           -os três nulos, mais `valor_normalizado` nulo e `valor_reembol
           -savel` `0,00`) — CA-034.                                     
      1156 -  - **Critério de conclusão:** todos os cenários acima verdes
           -; `mvn test` completo verde (a suíte histórica não usa `MOEDA
           -_SEM_COTACAO` nem `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, 
           -então a renumeração de estágios não muda nenhuma ordem já tes
           -tada).                                                       
      1153 +    2. Atualizar `criarOrdemCampo()` com a ordem canônica de 
           +contrato completa, exatamente: `ID=0, DATA=1, CATEGORIA=2, DE
           +SCRICAO=3, FORNECEDOR=4, VALOR=5, MOEDA=6, TEM_NOTA_FISCAL=7`
           +.                                                            
      1154 +    3. Atualizar `criarEstagios()` com onze estágios, numerad
           +os de 0 a 10; o mapa contém uma entrada para cada `MotivoCodi
           +go`, e vários códigos podem compartilhar o mesmo estágio (a t
           +abela completa de estágio por código já está descrita em "O q
           +ue faz", acima).                                             
      1155 +  - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) —
           + um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` 
           +simultâneos aparece nessa ordem; um item com erro estrutural 
           +em `despesa.moeda` aparece na posição correta da ordem canôni
           +ca de campo (entre `valor` e `tem_nota_fiscal`). `SaidaCambio
           +Test` — os quatro formatos fechados de `moeda`/`taxa_cambio_a
           +plicada`/`data_cotacao_utilizada` no `ResultadoItem`:        
      1156 +    - **BRL válido:** `moeda = "BRL"`, `taxa_cambio_aplicada 
           += 1`, `data_cotacao_utilizada = null`.                       
      1157 +    - **Moeda estrangeira convertida:** `moeda` = código orig
           +inal válido (ex.: `"EUR"`), `taxa_cambio_aplicada` = taxa efe
           +tivamente utilizada, `data_cotacao_utilizada` = data exata ou
           + anterior efetivamente utilizada.                            
      1158 +    - **Campo `moeda` estruturalmente inválido:** `moeda = nu
           +ll`, `taxa_cambio_aplicada = null`, `data_cotacao_utilizada =
           + null`.                                                      
      1159 +    - **Moeda válida sem cotação:** `moeda` = código original
           + válido (ex.: `"GBP"`), `taxa_cambio_aplicada = null`, `data_
           +cotacao_utilizada = null`, `valor_normalizado = null`, `valor
           +_reembolsavel = 0.00`, motivo `MOEDA_SEM_COTACAO` com `campo 
           += despesa.moeda` (CA-034) — os campos nulos deste cenário são
           + verificados individualmente, não descritos genericamente com
           +o "os três nulos", porque o conjunto de campos afetados é mai
           +or que o do cenário de moeda estruturalmente inválida (inclui
           + também `valor_normalizado` e `valor_reembolsavel`).         
      1160 +                                                             
      1161 +    **E**, adicionalmente: um cenário em que um campo **não u
           +tilizado por RN-020** — `descricao`, `fornecedor`, `categoria
           +` ou `tem_nota_fiscal` — está estruturalmente inválido, enqua
           +nto `valor`, `moeda` e `data` são válidos; a taxa, a data da 
           +cotação e a conversão continuam sendo resolvidas normalmente 
           +nesse item, confirmando que o erro estrutural num campo alhei
           +o a RN-020 não interrompe a resolução cambial nem sua propaga
           +ção até `ResultadoItem`.                                     
      1162 +  - **Critério de conclusão:** todos os cenários acima verdes
           +, inclusive o de campo não-financeiro inválido; `mvn test` co
           +mpleto verde (a suíte histórica não usa `MOEDA_SEM_COTACAO` n
           +em `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, então a renumer
           +ação de estágios não muda nenhuma ordem já testada).         
      1163    - **Comando de verificação:**
      1164      ```
      1165      mvn -q test "-Dtest=OrdemMotivosTest,SaidaCambioTest"

  Ran 1 shell command (ctrl+o to expand)

● Now item 6 — updating the two RN-020 matrices:

  Ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1491  | Regra | Task(s) dona(s) | Teste(s) |
      1492  |---|---|---|
      1493  | RN-019 | T-025, T-026, T-028 (modelos) · T-040 (`ResolutorP
            oliticaCentroCusto`) · T-041, T-042 (avaliador + wiring) · T-
            043 a T-046 (tetos por periodicidade) | `TabelaCategoriaTest`
            , `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentroCust
            oTest`, `CategoriaCentroCustoTest`, `TetoPorPeriodicidadeTest
            `, `AgregadorTetoIndividualTest` |
      1494 -| RN-020 | T-027 (`TabelaCambio`) · T-032, T-033 (`LeitorCamb
           -io`) · T-036 (campo `moeda`) · T-037, T-038, T-039 (`Resoluto
           -rCambio` + integração) | `TabelaCambioTest`, `LeitorCambioTes
           -t`, `CampoMoedaTest`, `ResolucaoCambioTest`, `ConversaoCambia
           -lIntegracaoTest`, `MoedaSemCotacaoTest` |                    
      1494 +| RN-020 | T-027 (`TabelaCambio`) · T-032, T-033 (`LeitorCamb
           +io`) · T-036 (campo `moeda`) · T-037, T-038, T-039 (`Resoluto
           +rCambio` + integração) · T-041 (coexistência com `CATEGORIA_N
           +AO_REEMBOLSAVEL_CENTRO_CUSTO`) | `TabelaCambioTest`, `LeitorC
           +ambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`, `Convers
           +aoCambialIntegracaoTest`, `MoedaSemCotacaoTest`, `CategoriaCe
           +ntroCustoTest` |                                             
      1495  | RN-021 | T-026 (`PoliticaExterna.vigencia`) · T-030 (valida
            ção de `vigencia` no `LeitorPolitica`) | `PoliticaExternaTest
            `, `LeitorPoliticaTest` |
      1496  | RN-022 | T-030, T-031 (`LeitorPolitica`) · T-032, T-033 (`L
            eitorCambio`) · T-035 (CLI trata falha como exit `2`) | `Leit
            orPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |
      1497  

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1547  | RN/CA | Tasks responsáveis | Testes previstos |
      1548  |---|---|---|
      1549  | RN-019 | T-025, T-026, T-028, T-040, T-041, T-042, T-043, T
            -044, T-045, T-046 | `TabelaCategoriaTest`, `TabelaPoliticaRe
            solvidaTest`, `ResolutorPoliticaCentroCustoTest`, `CategoriaC
            entroCustoTest`, `TetoPorPeriodicidadeTest`, `AgregadorTetoIn
            dividualTest`, `CliContratoTest` |
      1550 -| RN-020 | T-027, T-029, T-032, T-033, T-036, T-037, T-038, T
           --039 | `TabelaCambioTest`, `LeitorCambioTest`, `ItemValidadoC
           -ambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`, `Convers
           -aoCambialIntegracaoTest`, `MoedaSemCotacaoTest` |            
      1550 +| RN-020 | T-027, T-029, T-032, T-033, T-036, T-037, T-038, T
           +-039, T-041 | `TabelaCambioTest`, `LeitorCambioTest`, `ItemVa
           +lidadoCambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`, `
           +ConversaoCambialIntegracaoTest`, `MoedaSemCotacaoTest`, `Cate
           +goriaCentroCustoTest` |                                      
      1551  | RN-021 | T-026, T-030 | `PoliticaExternaTest`, `LeitorPolit
            icaTest` |
      1552  | RN-022 | T-030, T-031, T-032, T-033, T-035 | `LeitorPolitic
            aTest`, `LeitorCambioTest`, `CliContratoTest` |
      1553  | CA-024 a CA-027 | T-040, T-041, T-046 | `ResolutorPoliticaC
            entroCustoTest`, `CategoriaCentroCustoTest`, `CliContratoTest
            ` |

● Now item 7 — expanding T-055's inventory with the six grep commands:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 18 lines, removed 7 lines
      1321    - **DT/seções do plan:** DT-011 (nota de supersessão); plan
             §19 ("Substituídos").
      1322    - **Dependências:** T-041 (sobrecargas novas de `AvaliadorR
            egrasIndividuais`), T-043 (sobrecargas novas de `AgregadorTet
            oDiario`), T-044 (`AgregadorTetoIndividual`), T-050 (fixture 
            de política histórica).
      1323    - **Arquivos que cria/modifica:**
      1324 -    - Todos os arquivos apontados por `git grep "PoliticaReem
           -bolso" -- tests/java` e `git grep "AgregadorTetoHospedagem" -
           -- tests/java` antes da migração (levantados no passo 1 abaixo
           -) — inclui, no mínimo, `TetoHospedagemTest.java` e qualquer t
           -este de T-006 a T-021 que ainda construa `AvaliadorRegrasIndi
           -viduais`/`AgregadorTetoDiario` via as sobrecargas de um/dois 
           -argumentos.                                                  
      1324 +    - Todos os arquivos apontados pelo inventário completo de
           + seis comandos (levantado no passo 1 abaixo) — inclui, no mín
           +imo, `TetoHospedagemTest.java` e qualquer teste de T-006 a T-
           +021 que ainda construa `AvaliadorRegrasIndividuais`/`Agregado
           +rTetoDiario` via as sobrecargas históricas.                  
      1325    - **Passos de implementação:**
      1326 -    1. Rodar `git grep "PoliticaReembolso" -- tests/java` e `
           -git grep "AgregadorTetoHospedagem" -- tests/java` e listar ca
           -da ocorrência.                                               
      1327 -    2. Migrar **todos** os testes que chamam `AgregadorTetoHo
           -spedagem.aplicar(...)` diretamente para `AgregadorTetoIndivid
           -ual.aplicar(...)`, usando uma `TabelaPoliticaResolvida` de te
           -ste equivalente — nenhum teste continua chamando `AgregadorTe
           -toHospedagem` depois desta task.                             
      1328 -    3. Migrar **todos** os testes que dependem, direta ou ind
           -iretamente, de `PoliticaReembolso` (inclusive via as sobrecar
           -gas antigas de `AvaliadorRegrasIndividuais.avaliar(item)`/`av
           -aliar(item, envelope)` e `AgregadorTetoDiario.aplicar(itens)`
           -) para as sobrecargas novas, com `PoliticaExterna`/`TabelaPol
           -iticaResolvida` construídas a partir da fixture de política h
           -istórica de T-050 — sem exceção "porque o teste já funciona a
           -ssim".                                                       
      1329 -    4. Rodar `mvn test` completo após a migração.            
      1330 -    5. Confirmar que `git grep "PoliticaReembolso" -- tests/j
           -ava` e `git grep "AgregadorTetoHospedagem" -- tests/java` não
           - retornam mais nenhuma ocorrência.                           
      1326 +    1. Rodar o inventário completo, antes de qualquer migraçã
           +o:                                                           
      1327 +       ```                                                   
      1328 +       git grep "AvaliadorRegrasIndividuais.avaliar" -- tests
           +/java                                                        
      1329 +       git grep "AvaliadorRegrasIndividuais.avaliarLista" -- 
           +tests/java                                                   
      1330 +       git grep "AgregadorTetoDiario.aplicar" -- tests/java  
      1331 +       git grep "AgregadorTetoHospedagem.aplicar" -- tests/ja
           +va                                                           
      1332 +       git grep "PoliticaReembolso" -- tests/java            
      1333 +       git grep "AgregadorTetoHospedagem" -- tests/java      
      1334 +       ```                                                   
      1335 +       Para cada resultado: identificar qual sobrecarga está 
           +sendo utilizada; migrar as chamadas das sobrecargas histórica
           +s de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` para 
           +as sobrecargas que recebem `PoliticaExterna`/`TabelaPoliticaR
           +esolvida` (usando a fixture de política histórica de T-050 no
           + lugar de `PoliticaReembolso.padrao()`); preservar sem altera
           +ção as chamadas que já usam as APIs novas; migrar toda chamad
           +a de `AgregadorTetoHospedagem.aplicar(...)` para `AgregadorTe
           +toIndividual.aplicar(...)`.                                  
      1336 +    2. Rodar `mvn test` completo após a migração.            
      1337 +    3. Confirmar que `git grep "PoliticaReembolso" -- tests/j
           +ava` e `git grep "AgregadorTetoHospedagem" -- tests/java` não
           + retornam mais nenhuma ocorrência.                           
      1338    - **Testes obrigatórios:** suíte completa (`mvn test`) verd
            e após a migração; nenhum teste órfão (que testava algo espec
            ífico de `AgregadorTetoHospedagem` e não tem equivalente em `
            AgregadorTetoIndividual`) foi perdido silenciosamente.
      1332 -  - **Critério de conclusão:** `git grep "PoliticaReembolso" 
           --- tests/java` e `git grep "AgregadorTetoHospedagem" -- tests
           -/java` não retornam nenhuma ocorrência; as únicas ocorrências
           - remanescentes em `src/main/java` são os dois arquivos-alvo e
           - as sobrecargas de compatibilidade que os chamam internamente
           -; `mvn test` verde.                                          
      1339 +  - **Critério de conclusão:** `git grep "PoliticaReembolso" 
           +-- tests/java` e `git grep "AgregadorTetoHospedagem" -- tests
           +/java` não retornam nenhuma ocorrência; as únicas ocorrências
           + remanescentes em `src/main/java` são os dois arquivos-alvo e
           + as sobrecargas de compatibilidade que os chamam internamente
           +; `mvn test` verde. Registra-se aqui, para orientar T-056: a 
           +remoção das sobrecargas históricas em T-056, seguida por `mvn
           + -q -DskipTests compile` e `mvn -q test`, é a verificação **d
           +efinitiva** de que não ficou nenhum consumidor indireto — o i
           +nventário desta task é a primeira camada de garantia, não a ú
           +nica.                                                        
      1340    - **Comando de verificação:**
      1341      ```
      1342 +    git grep "AvaliadorRegrasIndividuais.avaliar" -- tests/ja
           +va                                                           
      1343 +    git grep "AvaliadorRegrasIndividuais.avaliarLista" -- tes
           +ts/java                                                      
      1344 +    git grep "AgregadorTetoDiario.aplicar" -- tests/java     
      1345 +    git grep "AgregadorTetoHospedagem.aplicar" -- tests/java 
      1346      git grep "PoliticaReembolso" -- tests/java
      1347      git grep "AgregadorTetoHospedagem" -- tests/java
      1348      mvn -q test

● Now let's run the full textual verification requested in item 8:

  Ran 1 shell command (ctrl+o to expand)

● All eight problematic phrases are gone. Now the additional confirmations:
  Ran 1 shell command (ctrl+o to expand)

● All confirmed. Now the mandated git verification commands:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Let me fix that trailing-whitespace line:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1157      - **Moeda estrangeira convertida:** `moeda` = código orig
            inal válido (ex.: `"EUR"`), `taxa_cambio_aplicada` = taxa efe
            tivamente utilizada, `data_cotacao_utilizada` = data exata ou
             anterior efetivamente utilizada.
      1158      - **Campo `moeda` estruturalmente inválido:** `moeda = nu
            ll`, `taxa_cambio_aplicada = null`, `data_cotacao_utilizada =
             null`.
      1159      - **Moeda válida sem cotação:** `moeda` = código original
             válido (ex.: `"GBP"`), `taxa_cambio_aplicada = null`, `data_
            cotacao_utilizada = null`, `valor_normalizado = null`, `valor
            _reembolsavel = 0.00`, motivo `MOEDA_SEM_COTACAO` com `campo 
            = despesa.moeda` (CA-034) — os campos nulos deste cenário são
             verificados individualmente, não descritos genericamente com
            o "os três nulos", porque o conjunto de campos afetados é mai
            or que o do cenário de moeda estruturalmente inválida (inclui
             também `valor_normalizado` e `valor_reembolsavel`).
      1160 -                                                             
      1160 +                                                             
      1161      **E**, adicionalmente: um cenário em que um campo **não u
            tilizado por RN-020** — `descricao`, `fornecedor`, `categoria
            ` ou `tem_nota_fiscal` — está estruturalmente inválido, enqua
            nto `valor`, `moeda` e `data` são válidos; a taxa, a data da 
            cotação e a conversão continuam sendo resolvidas normalmente 
            nesse item, confirmando que o erro estrutural num campo alhei
            o a RN-020 não interrompe a resolução cambial nem sua propaga
            ção até `ResultadoItem`.
      1162    - **Critério de conclusão:** todos os cenários acima verdes
            , inclusive o de campo não-financeiro inválido; `mvn test` co
            mpleto verde (a suíte histórica não usa `MOEDA_SEM_COTACAO` n
            em `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, então a renumer
            ação de estágios não muda nenhuma ordem já testada).
      1163    - **Comando de verificação:**

  Ran 1 shell command (ctrl+o to expand)

● git diff --check is now clean (exit 0, only the LF/CRLF normalization notice,
  no real errors). Now the remaining verification commands:

  Ran 2 shell commands (ctrl+o to expand)

● Everything checks out. Here's the summary:

  1. Ocorrências de getLimite/getPeriodicidade removidas: 4 ocorrências de
  .getLimite() (T-031, T-041, T-043, T-044) corrigidas para o único acessor
  válido do record TabelaCategoria — limite() — usado via uma variável
  configuracao explícita em T-041/T-043/T-044, e via acesso direto ao mapa em
  T-031. Nenhuma ocorrência de getPeriodicidade existia; onde a periodicidade é
  consultada (T-043, T-044), passou a ser configuracao.periodicidade() == 
  Periodicidade.DIA/DIARIA.

  2. T-039 passou a executar duplicidade e tetos: deixou de parar em
  AvaliadorRegrasIndividuais. Agora o teste constrói o item pelo pipeline real e
  o leva explicitamente por SeletorElegiveis → DetectorDuplicidadeEconomica →
  SeletorElegiveis → AgregadorTetoDiario/AgregadorTetoHospedagem (os agregadores
  e sobrecargas históricos, já que os componentes dos blocos G/H ainda não
  existem neste ponto do backlog), comprovando operacionalmente a ausência de
  DUPLICIDADE e de qualquer motivo de teto — não apenas inspecionando a lista de
  motivos do avaliador.

  3. Separação RN-006/RN-007 na nova sobrecarga de T-041: a nova sobrecarga não
  chama mais avaliarRn006ERn007 (que consulta o Set fixo histórico). Um método
  novo, avaliarRn006(...), copia os motivos existentes e avalia somente
  VALOR_NAO_POSITIVO. A categoria passa a ser decidida exclusivamente pela
  TabelaPoliticaResolvida (ausente+PADRAO→CATEGORIA_FORA_POLITICA/RN-007;
  ausente+CENTRO_CUSTO→CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO/RN-019;
  limite()==0→idem; limite positivo→nenhum motivo), com um teste explícito
  provando que representacao (categoria dinâmica) não é recusada pelo conjunto
  fixo. avaliarRn006ERn007 permanece intocado, exclusivo das sobrecargas
  históricas. RN-020 foi acrescentada à linha "RN atendidas" de T-041, referente
  à coexistência/independência das regras que não dependem de
  valor_normalizado.

  4. Quatro formatos finais de T-048: BRL válido ("BRL"/1/null); moeda
  estrangeira convertida (código original/taxa utilizada/data
  exata-ou-anterior); campo moeda estruturalmente inválido (null/null/null);
  moeda válida sem cotação (código original/null/null, mais
  valor_normalizado=null, valor_reembolsavel=0.00, motivo
  MOEDA_SEM_COTACAO/campo=despesa.moeda — cada campo nulo listado
  individualmente, nunca "os três nulos"). Acrescentado um quinto cenário: campo
  alheio a RN-020 (descricao/fornecedor/categoria/tem_nota_fiscal) inválido não
  impede a conversão.

  5. RN-020 nas duas matrizes: ambas as linhas ("Cobertura — RN-019 a RN-022" e
  "Matriz final") agora incluem T-041 e CategoriaCentroCustoTest, preservando
  todas as tasks/testes já associados.

  6. Seis comandos de inventário de T-055:
  git grep "AvaliadorRegrasIndividuais.avaliar" -- tests/java
  git grep "AvaliadorRegrasIndividuais.avaliarLista" -- tests/java
  git grep "AgregadorTetoDiario.aplicar" -- tests/java
  git grep "AgregadorTetoHospedagem.aplicar" -- tests/java
  git grep "PoliticaReembolso" -- tests/java
  git grep "AgregadorTetoHospedagem" -- tests/java

  7. git status --short:
   M specs/001-motor-reembolso/tasks.md

  Somente tasks.md foi alterado; nenhum código, teste ou fixture foi tocado;
  nenhum /export, git add, commit ou push foi executado. Parado para revisão.

✻ Churned for 6m 55s

※ recap: We're building the Day 2 tasks.md backlog for the reembolso engine's policy v4 changes; I just finished a surgical fix cleaning up TabelaCategoria accessor usage, T-039 pipeline coverage, T-041's RN-006/007 split, T-048's formats, and RN-020 matrices. Next: awaiting your review before any further changes.

> Faça uma correção final e estritamente cirúrgica em:                          
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Altere exclusivamente esse arquivo.                                           
                                                                                
  Não altere:                                                                   
                                                                                
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - README.md;                                                                  
  - código;                                                                     
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Preserve integralmente:                                                       
                                                                                
  - versão 1.1;                                                                 
  - status em revisão;                                                          
  - data 2026-08-05;                                                            
  - spec 1.2 aprovada;                                                          
  - plan 1.1 aprovado;                                                          
  - T-001 a T-021 byte a byte intactas;                                         
  - T-022 a T-058 contínuas;                                                    
  - todas as tasks do Dia 2 pendentes;                                          
  - regra de um único commit por task;                                          
  - todas as correções já aplicadas em T-025, T-033, T-039, T-041, T-043,       
  T-044, T-048, T-055 e nas matrizes.                                           
                                                                                
  Não crie, remova ou renumere tasks.                                           
                                                                                
  Não execute /export, git add, commit ou push.                                 
                                                                                
  ## 1. Completar os testes de observacao em T-030                              
                                                                                
  Em T-030, mantenha que `observacao` de categoria:                             
                                                                                
  - ausente → válido;                                                           
  - texto → válido e descartado;                                                
  - número → inválido;                                                          
  - booleano → inválido;                                                        
  - lista → inválido;                                                           
  - objeto → inválido.                                                          
                                                                                
  Acrescente explicitamente o cenário de `observacao` como objeto na linha de   
  testes obrigatórios.                                                          
                                                                                
  Não mova essa validação para T-031.                                           
                                                                                
  ## 2. Corrigir a mensagem de commit de T-031                                  
                                                                                
  T-031 não testa `observacao`.                                                 
                                                                                
  Substitua a mensagem sugerida:                                                
                                                                                
  test(T-031): cobre observacao, limite zero e imutabilidade do LeitorPolitica  
                                                                                
  por:                                                                          
                                                                                
  test(T-031): cobre limites, campos desconhecidos e imutabilidade do           
  LeitorPolitica                                                                
                                                                                
  Não altere o restante da decomposição de T-031.                               
                                                                                
  ## 3. Corrigir a responsabilidade de T-042                                    
                                                                                
  Em “O que faz”, substitua a afirmação de que a tabela é resolvida “por item”. 
                                                                                
  O contrato correto é:                                                         
                                                                                
  - `centro_custo` pertence ao envelope;                                        
  - uma única `TabelaPoliticaResolvida` é calculada por execução;               
  - essa mesma instância é usada para todos os itens do envelope.               
                                                                                
  Mantenha o passo de implementação que resolve a tabela uma vez antes do laço  
  ou antes de `avaliarLista`.                                                   
                                                                                
  Não deixe simultaneamente “por item” e “uma vez por execução”.                
                                                                                
  ## 4. Tornar T-045 internamente consistente                                   
                                                                                
  T-045 modifica:                                                               
                                                                                
  tests/java/com/desafio/reembolso/pipeline/TetoPorPeriodicidadeTest.java       
                                                                                
  Logo, os quatro cenários precisam existir efetivamente nessa classe ao final  
  da task:                                                                      
                                                                                
  1. `representacao` com periodicidade `dia`;                                   
  2. `estacionamento` com periodicidade `diaria`;                               
  3. `hospedagem` com periodicidade `dia`;                                      
  4. `alimentacao` com periodicidade `diaria`.                                  
                                                                                
  Não diga apenas que o cenário de `estacionamento` está “referenciado” porque  
  foi testado em `AgregadorTetoIndividualTest`.                                 
                                                                                
  Em T-045, acrescente efetivamente o caso de `estacionamento` em               
  `TetoPorPeriodicidadeTest`, ainda que exista uma cobertura unitária           
  semelhante em T-044.                                                          
                                                                                
  Atualize:                                                                     
                                                                                
  - **CA atendidos:** `CA-047 e CA-049 — comprovação cruzada de que o algoritmo 
  é escolhido pela periodicidade`;                                              
  - passos de implementação;                                                    
  - testes obrigatórios;                                                        
  - critério de conclusão.                                                      
                                                                                
  O comando continua:                                                           
                                                                                
  mvn -q test -Dtest=TetoPorPeriodicidadeTest                                   
                                                                                
  ## 5. Fazer T-046 testar o wiring que implementa                              
                                                                                
  T-046 altera o Main para chamar os dois agregadores novos. Portanto,          
  `CliContratoTest` precisa exercer os dois caminhos pelo Main real.            
                                                                                
  Mantenha o cenário existente de limite zero em `CC-ENG-PLATAFORMA`.           
                                                                                
  Acrescente dois cenários de CLI usando política e envelope temporários de     
  teste:                                                                        
                                                                                
  ### Cenário `dia`                                                             
                                                                                
  - categoria dinâmica `representacao`;                                         
  - periodicidade `dia`;                                                        
  - limite 300.00;                                                              
  - dois itens na mesma data;                                                   
  - saldo compartilhado;                                                        
  - resultado comprova que `AgregadorTetoDiario` novo foi chamado;              
  - motivo `TETO_DIARIO_APLICADO` ou `TETO_DIARIO_ESGOTADO`;                    
  - regra RN-019;                                                               
  - CA-047.                                                                     
                                                                                
  ### Cenário `diaria`                                                          
                                                                                
  - categoria dinâmica `estacionamento`;                                        
  - periodicidade `diaria`;                                                     
  - limite 50.00;                                                               
  - item de 80.00;                                                              
  - reembolso parcial de 50.00;                                                 
  - motivo `TETO_INDIVIDUAL_APLICADO`;                                          
  - regra RN-019;                                                               
  - CA-049.                                                                     
                                                                                
  Os cenários devem executar `Main.run(...)` com as quatro flags e ler o JSON   
  de saída.                                                                     
                                                                                
  O critério de conclusão de T-046 deve exigir:                                 
                                                                                
  - limite zero recusado antes dos tetos;                                       
  - caminho `dia` funcionando pelo Main;                                        
  - caminho `diaria` funcionando pelo Main;                                     
  - suíte completa verde.                                                       
                                                                                
  Mantenha:                                                                     
                                                                                
  **CA atendidos:** confirma CA-027, CA-047 e CA-049 ponta a ponta via CLI.     
                                                                                
  ## 6. Atualizar as matrizes de CA-047 e CA-049                                
                                                                                
  Como T-046 passa a testar ambos pelo Main, atualize as duas matrizes:         
                                                                                
  ### CA-047                                                                    
                                                                                
  Tasks:                                                                        
                                                                                
  T-043, T-045, T-046                                                           
                                                                                
  Testes:                                                                       
                                                                                
  TetoPorPeriodicidadeTest, CliContratoTest                                     
                                                                                
  ### CA-049                                                                    
                                                                                
  Tasks:                                                                        
                                                                                
  T-044, T-045, T-046                                                           
                                                                                
  Testes:                                                                       
                                                                                
  AgregadorTetoIndividualTest, TetoPorPeriodicidadeTest, CliContratoTest        
                                                                                
  Aplique isso tanto na tabela detalhada quanto na matriz final.                
                                                                                
  ## 7. Corrigir dependências de T-048                                          
                                                                                
  Acrescente:                                                                   
                                                                                
  T-038 — conversão cambial e Normalizador já integrados ao pipeline            
                                                                                
  às dependências de T-048.                                                     
                                                                                
  Isso é necessário porque `SaidaCambioTest` exige:                             
                                                                                
  - BRL resolvido;                                                              
  - moeda estrangeira convertida;                                               
  - moeda sem cotação;                                                          
  - conversão preservada mesmo com outro campo estruturalmente inválido.        
                                                                                
  Preserve as dependências já existentes.                                       
                                                                                
  ## 8. Verificação final                                                       
                                                                                
  Confirme textualmente que não resta:                                          
                                                                                
  - “resolver, por item” em T-042;                                              
  - T-045 dizendo que `estacionamento` está apenas referenciado;                
  - T-045 com somente CA-047;                                                   
  - T-046 alegando CA-047/CA-049 sem executar os agregadores;                   
  - T-048 sem T-038 nas dependências;                                           
  - mensagem de T-031 contendo `observacao`.                                    
                                                                                
  Execute:                                                                      
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- README.md                                                         
  git diff -- src                                                               
  git diff -- tests                                                             
                                                                                
  Confirme:                                                                     
                                                                                
  - somente tasks.md foi alterado;                                              
  - T-001 a T-021 permanecem byte a byte intactas;                              
  - T-022 a T-058 continuam contínuas e pendentes;                              
  - nenhuma task nova foi criada;                                               
  - nenhuma alteração de código, teste ou fixture foi executada;                
  - nenhum /export, git add, commit ou push foi executado.                      
                                                                                
  Ao terminar, informe:                                                         
                                                                                
  1. cenário adicional de observacao em T-030;                                  
  2. mensagem final de T-031;                                                   
  3. resolução única por execução em T-042;                                     
  4. quatro casos efetivos de T-045;                                            
  5. três cenários CLI de T-046;                                                
  6. atualização das matrizes CA-047/CA-049;                                    
  7. dependências finais de T-048;                                              
  8. git status --short.                                                        
                                                                                
  Pare para revisão.                                                            

  Ran 1 shell command (ctrl+o to expand)

● Let me read T-030 fully first:

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      724      2. Validar, nesta ordem, os **dezesseis** pontos de `plan.
           md` §5 — a validação completa e integral, sem nenhum ponto adi
           ado para outra task: raiz objeto; `vigencia` obrigatória/forma
           to/data real; `moeda_base` exatamente `"BRL"`; `nota_fiscal_ob
           rigatoria_acima_de` obrigatório/numérico/não negativo; `padrao
           `/`centros_custo` obrigatórios e objetos; cada tabela de `cent
           ros_custo` é objeto; nome de categoria não vazio; configuração
            de categoria é objeto; `limite` obrigatório/numérico, estrita
           mente `> 0` em `padrao` e `>= 0` em `centros_custo`; `periodic
           idade` obrigatória e exatamente `"dia"`/`"diaria"`; `observaca
           o`, dentro de uma configuração de categoria — **ausente** (vál
           ido), **presente como texto** (válido, lido e descartado, nunc
           a populado em `TabelaCategoria`), **presente com qualquer outr
           o tipo** — número, booleano, lista, objeto — (política estrutu
           ralmente inválida); campos verdadeiramente desconhecidos — na 
           raiz (`versao`, `acrescimo_em_viagem_percentual`) e dentro de 
           uma configuração de categoria (além de `limite`/`periodicidade
           `/`observacao`) — continuam ignorados sem invalidar o arquivo;
            cópias defensivas imutáveis do modelo final (ponto 16).
      725      3. Construir `PoliticaExterna` só depois de toda a validaç
           ão passar — nenhuma `TabelaCategoria` é criada antes da valida
           ção completa terminar.
      726      4. Não há passo adiado: `observacao` e os campos desconhec
           idos já estão integralmente cobertos no passo 2 — T-031 amplia
            a matriz de testes sobre este mesmo comportamento, já impleme
           ntado aqui, não introduz validação nova.
      727 -  - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo in
          -existente; arquivo ilegível; JSON sintaticamente inválido; rai
          -z não objeto; `vigencia` ausente/tipo errado/formato errado/da
          -ta inexistente; `moeda_base` diferente de `"BRL"`; `padrao` au
          -sente ou não objeto; `centros_custo` ausente ou não objeto; `n
          -ota_fiscal_obrigatoria_acima_de` ausente/negativo; categoria c
          -om `limite` ausente ou não numérico; `periodicidade` fora de `
          -"dia"`/`"diaria"`; `observacao` ausente (válido); `observacao`
          - como texto (válido e descartado); `observacao` como número/bo
          -oleano/lista (arquivo inválido); um arquivo que satisfaz integ
          -ralmente o contrato é aceito e produz o `PoliticaExterna` espe
          -rado (CA-045).                                                
      728 -  - **Critério de conclusão:** todos os cenários acima verdes,
          - inclusive os três de `observacao`; nenhuma `TabelaCategoria` 
          -construída num arquivo rejeitado (verificável indiretamente pe
          -la exceção lançada antes de qualquer retorno parcial).        
      727 +  - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo in
          +existente; arquivo ilegível; JSON sintaticamente inválido; rai
          +z não objeto; `vigencia` ausente/tipo errado/formato errado/da
          +ta inexistente; `moeda_base` diferente de `"BRL"`; `padrao` au
          +sente ou não objeto; `centros_custo` ausente ou não objeto; `n
          +ota_fiscal_obrigatoria_acima_de` ausente/negativo; categoria c
          +om `limite` ausente ou não numérico; `periodicidade` fora de `
          +"dia"`/`"diaria"`; `observacao` ausente (válido); `observacao`
          + como texto (válido e descartado); `observacao` como número (a
          +rquivo inválido); `observacao` como booleano (arquivo inválido
          +); `observacao` como lista (arquivo inválido); `observacao` co
          +mo objeto (arquivo inválido); um arquivo que satisfaz integral
          +mente o contrato é aceito e produz o `PoliticaExterna` esperad
          +o (CA-045).                                                   
      728 +  - **Critério de conclusão:** todos os cenários acima verdes,
          + inclusive os seis de `observacao` (ausente, texto, número, bo
          +oleano, lista, objeto); nenhuma `TabelaCategoria` construída n
          +um arquivo rejeitado (verificável indiretamente pela exceção l
          +ançada antes de qualquer retorno parcial).                    
      729    - **Comando de verificação:**
      730      ```
      731      mvn -q test -Dtest=LeitorPoliticaTest

● Now T-031's commit message:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      749      ```
      750      mvn -q test -Dtest=LeitorPoliticaTest
      751      ```
      752 -  - **Commit sugerido:** `test(T-031): cobre observacao, limit
          -e zero e imutabilidade do LeitorPolitica`                     
      752 +  - **Commit sugerido:** `test(T-031): cobre limites, campos d
          +esconhecidos e imutabilidade do LeitorPolitica`               
      753    - **Status:** [ ] pendente
      754  
      755  ---

● Now let's fix T-042 (item 3):

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1003    - **Status:** [ ] pendente
      1004  
      1005  - [ ] **T-042** — Wiring da política externa no `Main`
      1006 -  - **O que faz:** `Main.executarPipeline` passa a resolver, 
           -por item, a `TabelaPoliticaResolvida` a partir de `envelope.g
           -etColaboradorCentroCusto()` e da `PoliticaExterna` carregada 
           -em T-035, e a chamar a nova sobrecarga de `AvaliadorRegrasInd
           -ividuais` (T-041) em vez da antiga. Agregação de tetos (Bloco
           - H) ainda usa `PoliticaReembolso` nesta task — só a elegibili
           -dade de categoria e o gatilho de nota fiscal passam a vir da 
           -política externa real.                                       
      1006 +  - **O que faz:** `Main.executarPipeline` passa a resolver a
           + `TabelaPoliticaResolvida` a partir de `envelope.getColaborad
           +orCentroCusto()` e da `PoliticaExterna` carregada em T-035, e
           + a chamar a nova sobrecarga de `AvaliadorRegrasIndividuais` (
           +T-041) em vez da antiga. `centro_custo` pertence ao envelope,
           + não ao item — por isso uma **única** `TabelaPoliticaResolvid
           +a` é calculada por execução, e essa mesma instância é reutili
           +zada para todos os itens do envelope, nunca recalculada item 
           +a item. Agregação de tetos (Bloco H) ainda usa `PoliticaReemb
           +olso` nesta task — só a elegibilidade de categoria e o gatilh
           +o de nota fiscal passam a vir da política externa real.      
      1007    - **RN atendidas:** RN-019, RN-009 (atualizada).
      1008    - **CA atendidos:** confirma CA-024 a CA-027 ponta a ponta 
            via CLI.
      1009    - **DT/seções do plan:** plan §2 (passo 7), §6.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1013      - `tests/java/com/desafio/reembolso/CliContratoTest.java`
      1014    - **Passos de implementação:**
      1015      1. `Main.executarPipeline` ganha o parâmetro `PoliticaExt
            erna politica` (junto com `TabelaCambio cambio`, já adicionad
            o em T-038).
      1016 -    2. Para cada `ItemNormalizado`, resolver `TabelaPoliticaR
           -esolvida` via `ResolutorPoliticaCentroCusto.resolver(envelope
           -.getColaboradorCentroCusto(), politica)` — a mesma tabela val
           -e para todos os itens de uma execução (o centro de custo é do
           - envelope, não do item), então a resolução pode acontecer uma
           - única vez antes do laço de avaliação, não por item.         
      1016 +    2. Resolver `TabelaPoliticaResolvida tabelaResolvida = Re
           +solutorPoliticaCentroCusto.resolver(envelope.getColaboradorCe
           +ntroCusto(), politica)` **uma única vez**, antes de chamar `a
           +valiarLista(...)` — nunca dentro de um laço por item, porque 
           +o centro de custo pertence ao envelope, não ao item, e a mesm
           +a instância de `tabelaResolvida` vale para todos os itens des
           +sa execução.                                                 
      1017      3. Chamar `AvaliadorRegrasIndividuais.avaliarLista(normal
            izados, envelope, tabelaResolvida, politica)` (nova sobrecarg
            a).
      1018    - **Testes obrigatórios:** `CliContratoTest` — um cenário d
            e integração leve confirmando que uma execução real com `--po
            litica` apontando para uma política de teste com um centro de
             custo cadastrado produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_C
            USTO` (não `CATEGORIA_FORA_POLITICA`) para uma categoria ause
            nte daquele centro — prova de que o `Main` está de fato usand
            o a política carregada, não mais o `Set` fixo.
      1019    - **Critério de conclusão:** cenário acima verde; `mvn test
            ` completo verde.

● Now let's read T-045 in full for item 4:

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 9 lines, removed 5 lines
      1074    - **Status:** [ ] pendente
      1075  
      1076  - [ ] **T-045** — `TetoPorPeriodicidadeTest` — categoria exte
            rna sob cada periodicidade
      1077 -  - **O que faz:** completa a prova de que o mecanismo de tet
           -o depende exclusivamente da `periodicidade` declarada na polí
           -tica, não do nome histórico da categoria (AMB-036): `hospedag
           -em` reconfigurada com `periodicidade: "dia"` usa o teto **com
           -partilhado** de `AgregadorTetoDiario` (`TETO_DIARIO_APLICADO`
           -/`TETO_DIARIO_ESGOTADO`, `regra = RN_019`, não `RN_013`); `al
           -imentacao` reconfigurada com `periodicidade: "diaria"` usa o 
           -teto **individual** de `AgregadorTetoIndividual` (`TETO_INDIV
           -IDUAL_APLICADO`, `regra = RN_019`, não `RN_011`).            
      1077 +  - **O que faz:** completa a prova de que o mecanismo de tet
           +o depende exclusivamente da `periodicidade` declarada na polí
           +tica, não do nome histórico da categoria (AMB-036), reunindo 
           +na mesma classe `TetoPorPeriodicidadeTest` os **quatro** cená
           +rios que demonstram essa independência: `representacao` com `
           +periodicidade: "dia"`; `estacionamento` com `periodicidade: "
           +diaria"`; `hospedagem` reconfigurada com `periodicidade: "dia
           +"` — usa o teto **compartilhado** de `AgregadorTetoDiario` (`
           +TETO_DIARIO_APLICADO`/`TETO_DIARIO_ESGOTADO`, `regra = RN_019
           +`, não `RN_013`); `alimentacao` reconfigurada com `periodicid
           +ade: "diaria"` — usa o teto **individual** de `AgregadorTetoI
           +ndividual` (`TETO_INDIVIDUAL_APLICADO`, `regra = RN_019`, não
           + `RN_011`). O cenário de `estacionamento` é efetivamente **ac
           +rescentado** a `TetoPorPeriodicidadeTest` nesta task — mesmo 
           +já existindo cobertura unitária semelhante em `AgregadorTetoI
           +ndividualTest` (T-044), essa cobertura vive numa classe difer
           +ente e não substitui a comprovação cruzada exigida aqui, onde
           + os quatro cenários precisam existir lado a lado na mesma cla
           +sse para demonstrar a independência do mecanismo em relação a
           +o nome da categoria.                                         
      1078    - **RN atendidas:** RN-019.
      1079 -  - **CA atendidos:** CA-047 (fechamento).                   
      1079 +  - **CA atendidos:** CA-047 e CA-049 — comprovação cruzada d
           +e que o algoritmo é escolhido pela periodicidade, não pelo no
           +me da categoria.                                             
      1080    - **DT/seções do plan:** DT-017; plan §11, §17 (`TetoPorPer
            iodicidadeTest`, quatro cenários).
      1081    - **Dependências:** T-043, T-044.
      1082    - **Arquivos que cria/modifica:**
      1083      - `tests/java/com/desafio/reembolso/pipeline/TetoPorPerio
            dicidadeTest.java` (mesma classe de T-043, casos adicionais)
      1084    - **Passos de implementação:**
      1085 -    1. Sem alteração de produção esperada — os dois agregador
           -es já leem `periodicidade` da tabela resolvida (T-043/T-044);
           - esta task só prova, com uma política de teste que inverte as
           - periodicidades históricas, que o nome da categoria não é har
           -dcoded em lugar nenhum além da pequena tabela de escolha do c
           -ódigo do motivo.                                             
      1086 -  - **Testes obrigatórios:** `TetoPorPeriodicidadeTest` — os 
           -quatro cenários completos: (1) `representacao` com `"dia"` (j
           -á coberto em T-043); (2) `estacionamento` com `"diaria"` (já 
           -coberto em T-044, referenciado aqui para fechar a matriz); (3
           -) `hospedagem` com `"dia"` — teto compartilhado, `TETO_DIARIO
           -_APLICADO`/`RN_019`; (4) `alimentacao` com `"diaria"` — teto 
           -individual, `TETO_INDIVIDUAL_APLICADO`/`RN_019`.             
      1087 -  - **Critério de conclusão:** os quatro cenários verdes na m
           -esma classe de teste.                                        
      1085 +    1. Confirmar que o cenário de `representacao` com `"dia"`
           + (T-043) já está presente na classe.                         
      1086 +    2. Acrescentar efetivamente, nesta task, o cenário de `es
           +tacionamento` com `"diaria"` (limite R$50,00, despesa elegíve
           +l de R$80,00 → parcial R$50,00, `TETO_INDIVIDUAL_APLICADO`/`R
           +N_019`) — ainda que `AgregadorTetoIndividualTest` (T-044) já 
           +exercite o mesmo agregador isoladamente, o cenário precisa ex
           +istir também aqui, lado a lado com os outros três, para a com
           +provação cruzada de CA-047/CA-049.                           
      1087 +    3. Acrescentar o cenário de `hospedagem` reconfigurada co
           +m `"dia"` — teto compartilhado, `TETO_DIARIO_APLICADO`/`RN_01
           +9`.                                                          
      1088 +    4. Acrescentar o cenário de `alimentacao` reconfigurada c
           +om `"diaria"` — teto individual, `TETO_INDIVIDUAL_APLICADO`/`
           +RN_019`.                                                     
      1089 +    5. Sem alteração de código de produção esperada — os dois
           + agregadores já leem `periodicidade` da tabela resolvida (T-0
           +43/T-044); esta task só monta, na mesma classe, os quatro cen
           +ários que provam que o nome da categoria não é hardcoded em l
           +ugar nenhum além da pequena tabela de escolha do código do mo
           +tivo. Se algum cenário revelar um defeito real, a correção en
           +tra no único commit desta task, e a mensagem passa de `test(T
           +-045)` para `fix(T-045)`.                                    
      1090 +  - **Testes obrigatórios:** `TetoPorPeriodicidadeTest` — os 
           +quatro cenários **efetivamente presentes** na classe ao final
           + da task: (1) `representacao` com `"dia"`; (2) `estacionament
           +o` com `"diaria"`; (3) `hospedagem` com `"dia"` — teto compar
           +tilhado, `TETO_DIARIO_APLICADO`/`RN_019`; (4) `alimentacao` c
           +om `"diaria"` — teto individual, `TETO_INDIVIDUAL_APLICADO`/`
           +RN_019`.                                                     
      1091 +  - **Critério de conclusão:** os quatro cenários existem e p
           +assam na mesma classe `TetoPorPeriodicidadeTest`, não apenas 
           +referenciados em outra classe; `mvn test` completo verde.    
      1092    - **Comando de verificação:**
      1093      ```
      1094      mvn -q test -Dtest=TetoPorPeriodicidadeTest
● Now let's read T-046 in full for item 5:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 11 lines, removed 4 lines
      1097    - **Status:** [ ] pendente
      1098  
      1099  - [ ] **T-046** — Wiring dos agregadores por periodicidade no
             `Main`
      1100 -  - **O que faz:** `Main.executarPipeline` passa a chamar as 
           -novas sobrecargas de `AgregadorTetoDiario.aplicar(elegiveis, 
           -tabelaResolvida)` e `AgregadorTetoIndividual.aplicar(elegivei
           -s, tabelaResolvida)`, em vez de `AgregadorTetoDiario`(antigo)
           -/`AgregadorTetoHospedagem` — usando a mesma `TabelaPoliticaRe
           -solvida` já calculada em T-042.                              
      1100 +  - **O que faz:** `Main.executarPipeline` passa a chamar as 
           +novas sobrecargas de `AgregadorTetoDiario.aplicar(elegiveis, 
           +tabelaResolvida)` e `AgregadorTetoIndividual.aplicar(elegivei
           +s, tabelaResolvida)`, em vez de `AgregadorTetoDiario`(antigo)
           +/`AgregadorTetoHospedagem` — usando a mesma `TabelaPoliticaRe
           +solvida` já calculada em T-042. Como esta task é o ponto em q
           +ue os dois agregadores novos passam a ser efetivamente exerci
           +tados pelo `Main` real, `CliContratoTest` precisa exercer os 
           +**dois** caminhos (`"dia"` e `"diaria"`) através do binário, 
           +não só o cenário de recusa por limite zero já coberto.       
      1101    - **RN atendidas:** RN-011 a RN-015, RN-019.
      1102 -  - **CA atendidos:** confirma CA-027, CA-047, CA-049 ponta a
           - ponta via CLI.                                              
      1102 +  - **CA atendidos:** confirma CA-027, CA-047 e CA-049 ponta 
           +a ponta via CLI.                                             
      1103    - **DT/seções do plan:** DT-017; plan §2 (passo 9), §11.
      1104    - **Dependências:** T-042 (`TabelaPoliticaResolvida` dispon
            ível no `Main`), T-043, T-044.
      1105    - **Arquivos que cria/modifica:**
      1106      - `src/main/java/com/desafio/reembolso/Main.java`
      1107      - `tests/java/com/desafio/reembolso/CliContratoTest.java`
      1108 +    - `tests/resources/fixtures/politica-teste-periodicidade.
           +json` e `tests/resources/fixtures/despesas-teste-periodicidad
           +e.json` (fixtures temporárias de política e envelope para os 
           +dois cenários novos, ou equivalentes gerados em diretório tem
           +porário pelo próprio teste)                                  
      1109    - **Passos de implementação:**
      1110      1. Substituir, em `executarPipeline`, as chamadas a `Agre
            gadorTetoDiario.aplicar(elegiveisParaTetos)` e `AgregadorTeto
            Hospedagem.aplicar(elegiveisParaTetos)` pelas novas sobrecarg
            as, passando `tabelaResolvida`.
      1111      2. `CompositorSaida.compor(...)` continua recebendo duas 
            listas de `ResultadoTeto` (uma do agregador de `"dia"`, outra
             do de `"diaria"`) — a assinatura de `compor` não muda, só a 
            origem das listas.
      1111 -  - **Testes obrigatórios:** `CliContratoTest` — um cenário d
           -e integração leve confirmando, via execução real com `--polit
           -ica` apontando para `CC-ENG-PLATAFORMA` (hospedagem limite `0
           -,00`), que o item de hospedagem é recusado com `CATEGORIA_NAO
           -_REEMBOLSAVEL_CENTRO_CUSTO` **antes** de qualquer agregador d
           -e teto ser exercitado (spec 8.4, item 15) — prova de que a re
           -cusa por política antecede a agregação também no caminho real
           -.                                                            
      1112 -  - **Critério de conclusão:** cenário acima verde; `mvn test
           -` completo verde.                                            
      1112 +    3. Construir uma política e um envelope de teste para o c
           +enário `"dia"`: categoria dinâmica `representacao`, `periodic
           +idade: "dia"`, `limite: 300.00`, e dois itens elegíveis dessa
           + categoria na mesma data no envelope, exercitando o saldo com
           +partilhado.                                                  
      1113 +    4. Construir uma política e um envelope de teste para o c
           +enário `"diaria"`: categoria dinâmica `estacionamento`, `peri
           +odicidade: "diaria"`, `limite: 50.00`, e um item elegível de 
           +`80.00` nessa categoria no envelope.                         
      1114 +    5. Em ambos os cenários novos, invocar `Main.run(...)` co
           +m as quatro flags reais (`calcular --input ... --output ... -
           +-politica ... --cambio ...`) e ler o JSON de saída produzido 
           +— nunca chamar `executarPipeline` diretamente, porque o objet
           +ivo é provar que o wiring real do `Main` está correto.       
      1115 +  - **Testes obrigatórios:** `CliContratoTest` — **três** cen
           +ários:                                                       
      1116 +    1. (já existente) limite zero em `CC-ENG-PLATAFORMA`: exe
           +cução real com `--politica` apontando para essa tabela (hospe
           +dagem limite `0,00`) recusa o item de hospedagem com `CATEGOR
           +IA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` **antes** de qualquer agreg
           +ador de teto ser exercitado (spec 8.4, item 15).             
      1117 +    2. Cenário `"dia"`: `representacao` (`periodicidade: "dia
           +"`, limite `300.00`), dois itens na mesma data no envelope, s
           +aldo compartilhado — o JSON de saída, lido depois de `Main.ru
           +n(...)` com as quatro flags, comprova que `AgregadorTetoDiari
           +o` (sobrecarga nova) foi de fato chamado: motivo `TETO_DIARIO
           +_APLICADO` ou `TETO_DIARIO_ESGOTADO`, regra `RN-019` (CA-047)
           +.                                                            
      1118 +    3. Cenário `"diaria"`: `estacionamento` (`periodicidade: 
           +"diaria"`, limite `50.00`), item de `80.00` no envelope — o J
           +SON de saída, lido depois de `Main.run(...)` com as quatro fl
           +ags, comprova reembolso parcial de `50.00`, motivo `TETO_INDI
           +VIDUAL_APLICADO`, regra `RN-019` (CA-049).                   
      1119 +  - **Critério de conclusão:** limite zero recusado antes dos
           + tetos (cenário 1); caminho `"dia"` funcionando pelo `Main` r
           +eal (cenário 2); caminho `"diaria"` funcionando pelo `Main` r
           +eal (cenário 3); `mvn test` completo verde.                  
      1120    - **Comando de verificação:**
      1121      ```
      1122      mvn -q test -Dtest=CliContratoTest

● Now item 6 — updating the CA-047/CA-049 matrix rows:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      1544  | CA-044 | T-035 | `CliContratoTest`, `EscritaAtomicaSaidaTes
            t` |
      1545  | CA-045 | T-030, T-031 | `LeitorPoliticaTest` |
      1546  | CA-046 | T-032, T-033 | `LeitorCambioTest` |
      1547 -| CA-047 | T-043, T-045 | `TetoPorPeriodicidadeTest` |       
      1547 +| CA-047 | T-043, T-045, T-046 | `TetoPorPeriodicidadeTest`, 
           +`CliContratoTest` |                                          
      1548  | CA-048 | T-036 | `CampoMoedaTest` |
      1549 -| CA-049 | T-044, T-045 | `AgregadorTetoIndividualTest`, `Tet
           -oPorPeriodicidadeTest` |                                     
      1549 +| CA-049 | T-044, T-045, T-046 | `AgregadorTetoIndividualTest
           +`, `TetoPorPeriodicidadeTest`, `CliContratoTest` |           
      1550  
      1551  Todo identificador de `RN-019` a `RN-022` e de `CA-024` a `CA
            -049` aparece em pelo menos uma linha das duas tabelas acima 
            — conferido linha a linha ao escrever esta seção (a mesma con
            ferência é reexecutada em T-058, depois que as tasks tiverem 
            sido implementadas, sobre o estado real do código e da suíte,
             não só sobre o planejamento).
      1552  

● Now the final matrix (lower table):

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      1579  | CA-039, CA-040 | T-052, T-053 | `IntegracaoEnvelopeTest` |
      1580  | CA-041 a CA-044 | T-034, T-035 | `CliContratoTest`, `Escrit
            aAtomicaSaidaTest` |
      1581  | CA-045, CA-046 | T-030, T-031, T-032, T-033 | `LeitorPoliti
            caTest`, `LeitorCambioTest` |
      1582 -| CA-047 | T-043, T-045 | `TetoPorPeriodicidadeTest` |       
      1582 +| CA-047 | T-043, T-045, T-046 | `TetoPorPeriodicidadeTest`, 
           +`CliContratoTest` |                                          
      1583  | CA-048 | T-036 | `CampoMoedaTest` |
      1584 -| CA-049 | T-044, T-045 | `AgregadorTetoIndividualTest`, `Tet
           -oPorPeriodicidadeTest` |                                     
      1584 +| CA-049 | T-044, T-045, T-046 | `AgregadorTetoIndividualTest
           +`, `TetoPorPeriodicidadeTest`, `CliContratoTest` |           
      1585  
      1586  **Remoção do legado (`CLAUDE.md` §6):** `PoliticaReembolso.ja
            va` e `AgregadorTetoHospedagem.java` são migrados em T-055 e 
            removidos em T-056, com verificação por `git grep` nos comand
            os de ambas as tasks — nenhuma das duas classes é tocada ante
            s de todos os consumidores terem migrado.
      1587  

● Now item 7 — T-048's dependencies:

  Read 1 file, ran 2 shell commands (ctrl+o to expand)

● Now adding T-038 to T-048's dependencies:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1154    - **RN atendidas:** RN-017 (atualizada).
      1155    - **CA atendidos:** CA-034.
      1156    - **DT/seções do plan:** DT-019; plan §10.
      1157 -  - **Dependências:** T-022 (`CampoCanonico.MOEDA`), T-023 (t
           -rês `MotivoCodigo` novos), T-029 (getters de câmbio em `ItemV
           -alidado`).                                                   
      1157 +  - **Dependências:** T-022 (`CampoCanonico.MOEDA`), T-023 (t
           +rês `MotivoCodigo` novos), T-029 (getters de câmbio em `ItemV
           +alidado`), T-038 (conversão cambial e `Normalizador` já integ
           +rados ao pipeline — necessário porque `SaidaCambioTest` exige
           + BRL resolvido, moeda estrangeira convertida, moeda sem cotaç
           +ão, e conversão preservada mesmo com outro campo estruturalme
           +nte inválido).                                               
      1158    - **Arquivos que cria/modifica:**
      1159      - `src/main/java/com/desafio/reembolso/pipeline/Composito
            rSaida.java`
      1160      - `tests/java/com/desafio/reembolso/pipeline/OrdemMotivos
            Test.java` (estendido)

● Now let's run the item 8 textual verification checklist:

  Ran 1 shell command (ctrl+o to expand)

● All checks pass. Now let's run the mandated git verification commands:

  Ran 4 shell commands (ctrl+o to expand)

● The hunk starts exactly at the "---" separator right before "## Fase 8",
  confirming the entire T-001–T-021 block (lines 1–494, minus the two small
  header hunks) is untouched by this edit. Now final verification:

  Ran 1 shell command (ctrl+o to expand)

● All confirmed. Summary:

  1. Cenário adicional de observacao em T-030: o teste observacao como objeto
  foi acrescentado explicitamente aos "Testes obrigatórios" (junto com número,
  booleano e lista, cada um listado separadamente, não mais agrupado), e o
  "Critério de conclusão" agora exige os seis cenários de observacao (ausente,
  texto, número, booleano, lista, objeto). A validação continua em T-030, não
  foi movida para T-031.

  2. Mensagem final de T-031: test(T-031): cobre limites, campos desconhecidos e
  imutabilidade do LeitorPolitica.

  3. Resolução única por execução em T-042: "O que faz" e o passo 2 agora dizem,
  sem ambiguidade, que centro_custo pertence ao envelope e que uma única
  TabelaPoliticaResolvida é calculada por execução (antes de avaliarLista(...),
  nunca dentro de um laço por item) — removida toda menção a "por item".

  4. Quatro casos efetivos de T-045: representacao/"dia",
  estacionamento/"diaria" (agora efetivamente acrescentado a
  TetoPorPeriodicidadeTest, não apenas citado), hospedagem/"dia" e
  alimentacao/"diaria" — os quatro existem na mesma classe ao final da task. CA 
  atendidos passou a CA-047 e CA-049.

  5. Três cenários CLI de T-046: (1) limite zero em CC-ENG-PLATAFORMA (já
  existente); (2) representacao/"dia"/limite 300.00, dois itens, saldo
  compartilhado, via Main.run(...) com as quatro flags (CA-047); (3)
  estacionamento/"diaria"/limite 50.00, item de 80.00, parcial 50.00,
  TETO_INDIVIDUAL_APLICADO (CA-049) — ambos lendo o JSON de saída real.

  6. Matrizes CA-047/CA-049: atualizadas nas duas tabelas ("Cobertura — RN-019 a
  RN-022"/"CA-024 a CA-049" e "Matriz final") — CA-047 agora com
  T-043/T-045/T-046 e TetoPorPeriodicidadeTest/CliContratoTest; CA-049 com
  T-044/T-045/T-046 e
  AgregadorTetoIndividualTest/TetoPorPeriodicidadeTest/CliContratoTest.

  7. Dependências finais de T-048: T-022, T-023, T-029, e T-038 (conversão
  cambial e Normalizador já integrados ao pipeline), com a justificativa de que
  SaidaCambioTest exige BRL resolvido, conversão real, moeda sem cotação e
  conversão preservada com outro campo inválido.

  8. git status --short:
   M specs/001-motor-reembolso/tasks.md

  Somente tasks.md foi alterado; T-001 a T-021 permanecem byte a byte intactas
  (confirmado pelos limites dos hunks do diff); T-022 a T-058 continuam
  contínuas e pendentes; nenhuma task foi criada, removida ou renumerada; nenhum
  código, teste ou fixture foi tocado; nenhum /export, git add, commit ou push
  foi executado. Parado para revisão.

✻ Sautéed for 3m 29s

※ recap: We've been building the Dia 2 (política v4) backlog in specs/001-motor-reembolso/tasks.md, adding tasks T-022 to T-058 and fixing consistency issues across several review rounds. All requested corrections are applied and verified; next step is your review before any commit.

> Faça uma correção final de executabilidade incremental do backlog.            
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Não altere:                                                                   
                                                                                
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - README.md;                                                                  
  - código de produção;                                                         
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Mantenha:                                                                     
                                                                                
  - versão 1.1;                                                                 
  - status em revisão;                                                          
  - data 2026-08-05;                                                            
  - spec 1.2 aprovada;                                                          
  - plan 1.1 aprovado;                                                          
  - T-001 a T-021 byte a byte intactas;                                         
  - T-022 a T-058 contínuas;                                                    
  - 58 tasks no total;                                                          
  - T-022 a T-058 pendentes;                                                    
  - uma task e um commit no Dia 2;                                              
  - todas as correções já aprovadas.                                            
                                                                                
  Não crie, remova ou renumere tasks.                                           
                                                                                
  Não execute /export, git add, commit ou push.                                 
                                                                                
  ## 1. T-022 — atualizar imediatamente ORDEM_CAMPO                             
                                                                                
  Quando CampoCanonico.MOEDA for criado, CompositorSaida não pode continuar com 
  o mapa antigo até T-048.                                                      
                                                                                
  Acrescente aos arquivos de T-022:                                             
                                                                                
  src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java             
                                                                                
  Em T-022, atualizar criarOrdemCampo() imediatamente para:                     
                                                                                
  ID=0                                                                          
  DATA=1                                                                        
  CATEGORIA=2                                                                   
  DESCRICAO=3                                                                   
  FORNECEDOR=4                                                                  
  VALOR=5                                                                       
  MOEDA=6                                                                       
  TEM_NOTA_FISCAL=7                                                             
                                                                                
  Justificativa:                                                                
                                                                                
  - a partir de T-036, erros estruturais de moeda já podem chegar ao pipeline;  
  - CompositorSaida precisa reconhecer CampoCanonico.MOEDA desde o momento em   
  que o campo passa a existir;                                                  
  - não é aceitável manter um intervalo em que um motivo válido cause “campo    
  fora do vocabulário fechado de precedência”.                                  
                                                                                
  T-048 continuará testando a posição de moeda, mas não introduzirá essa        
  entrada pela primeira vez.                                                    
                                                                                
  ## 2. T-023 — atualizar imediatamente ESTAGIO_POR_CODIGO                      
                                                                                
  Remova de T-023 a afirmação:                                                  
                                                                                
  esses códigos simplesmente não são produzidos por nenhuma classe até T-048    
                                                                                
  Ela é falsa:                                                                  
                                                                                
  - MOEDA_SEM_COTACAO é produzido em T-037 e integrado em T-038;                
  - CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO é produzido em T-041 e integrado em 
  T-042;                                                                        
  - TETO_INDIVIDUAL_APLICADO é produzido em T-044 e integrado em T-046.         
                                                                                
  Acrescente aos arquivos de T-023:                                             
                                                                                
  src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java             
                                                                                
  Em T-023, atualizar criarEstagios() imediatamente para a tabela final:        
                                                                                
  0 ITEM_TIPO_INVALIDO                                                          
  1 motivos estruturais                                                         
  2 ID_DUPLICADO                                                                
  3 MOEDA_SEM_COTACAO                                                           
  4 VALOR_NAO_POSITIVO                                                          
  5 CATEGORIA_FORA_POLITICA                                                     
  6 CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO                                     
  7 FORA_COMPETENCIA                                                            
  8 NOTA_FISCAL_AUSENTE                                                         
  9 DUPLICIDADE                                                                 
  10 todos os motivos de teto, incluindo TETO_INDIVIDUAL_APLICADO               
                                                                                
  A renumeração preserva a ordem relativa dos códigos históricos.               
                                                                                
  T-048 continuará ampliando OrdemMotivosTest e verificando a tabela completa,  
  mas não deixará para depois a capacidade do compos                            
  ──── (191 lines hidden) ───────────────────────────────────────────────────── 
  er obrigatoriamente:                                                          
                                                                                
  - o construtor de dez argumentos de ItemValidado;                             
  - qualquer delegação criada exclusivamente para essa compatibilidade.         
                                                                                
  Acrescente ItemValidado.java aos arquivos modificados por T-056.              
                                                                                
  A compilação e a suíte completa após a remoção são a prova definitiva.        
                                                                                
  ## 8. Matrizes                                                                
                                                                                
  Corrija a consistência entre as matrizes:                                     
                                                                                
  ### Cobertura detalhada de RN-019                                             
                                                                                
  Como T-042 e T-046 usam CliContratoTest, inclua:                              
                                                                                
  CliContratoTest                                                               
                                                                                
  na coluna de testes de RN-019.                                                
                                                                                
  ### Cobertura detalhada de RN-020                                             
                                                                                
  Inclua T-029 e ItemValidadoCambioTest, como já ocorre na matriz final.        
                                                                                
  ### DT-017                                                                    
                                                                                
  Inclua:                                                                       
                                                                                
  T-043, T-044, T-045, T-046                                                    
                                                                                
  e:                                                                            
                                                                                
  TetoPorPeriodicidadeTest                                                      
  AgregadorTetoIndividualTest                                                   
  CliContratoTest                                                               
                                                                                
  pois T-046 materializa o wiring real pelo Main.                               
                                                                                
  Não altere RN, CA ou DT existentes.                                           
                                                                                
  ## 9. Verificação textual e estrutural                                        
                                                                                
  Confirme que não resta:                                                       
                                                                                
  - T-023 afirmando que os novos motivos não são produzidos antes de T-048;     
  - T-034 listando somente CliContratoTest como consumidor afetado;             
  - ExemploCompletoTest ou EscritaAtomicaSaidaTest planejados com comando       
  antigo;                                                                       
  - T-038 afirmando que a suíte histórica passa sem migrar pipelines diretos;   
  - fallback de Normalizador para getValor();                                   
  - T-046 deixando “fixture ou arquivo temporário” como alternativa;            
  - T-048 sem EscritorResultadoTest e TotalPeriodoTest;                         
  - construtor temporário de ItemValidado sem uma task explícita de remoção;    
  - matriz detalhada de RN-019 sem CliContratoTest;                             
  - DT-017 sem T-046.                                                           
                                                                                
  Execute:                                                                      
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- README.md                                                         
  git diff -- src                                                               
  git diff -- tests                                                             
                                                                                
  Confirme:                                                                     
                                                                                
  - somente tasks.md foi alterado;                                              
  - T-001 a T-021 permanecem byte a byte intactas;                              
  - T-022 a T-058 continuam contínuas e pendentes;                              
  - nenhuma task foi criada, removida ou renumerada;                            
  - nenhum código, teste ou fixture foi alterado;                               
  - nenhum /export, git add, commit ou push foi executado.                      
                                                                                
  Ao final, informe:                                                            
                                                                                
  1. em quais tasks os mapas de ordenação passam a ser atualizados;             
  2. quais consumidores de Main.run entram em T-034;                            
  3. como T-038 migra os pipelines históricos;                                  
  4. estratégia fechada de arquivos temporários em T-046;                       
  5. consumidores diretos de ResultadoItem migrados em T-048;                   
  6. onde o construtor temporário de ItemValidado será removido;                
  7. correções nas matrizes;                                                    
  8. git status --short.                                                        
                                                                                
  Pare para revisão.                                                            

  Read 1 file, ran 1 shell command (ctrl+o to expand)
● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 9 lines, removed 7 lines
      510  ### Bloco A — Modelos e vocabulários fundamentais
      511  
      512  - [ ] **T-022** — Estender `CampoCanonico` com `MOEDA`
      513 -  - **O que faz:** acrescenta o valor `MOEDA` ao vocabulário f
          -echado de campos canônicos, serializando para `"despesa.moeda"
          -` (mesmo mecanismo de `textoCanonico()` já existente — nome do
          - enum em minúsculo, prefixado por `despesa.`). Não altera a or
          -dem de uso em `CompositorSaida` nesta task (isso é T-048) — só
          - adiciona o valor ao enum.                                    
      513 +  - **O que faz:** acrescenta o valor `MOEDA` ao vocabulário f
          +echado de campos canônicos, serializando para `"despesa.moeda"
          +` (mesmo mecanismo de `textoCanonico()` já existente — nome do
          + enum em minúsculo, prefixado por `despesa.`). No **mesmo comm
          +it**, `CompositorSaida.criarOrdemCampo()` é atualizado para re
          +conhecer o valor novo — nunca se deixa um intervalo entre "o e
          +num existe" e "o compositor sabe ordená-lo", porque a partir d
          +e T-036 um motivo estrutural com `campo = CampoCanonico.MOEDA`
          + já pode chegar ao pipeline, e `ordemCampo(...)` lançaria `Ill
          +egalArgumentException` ("campo fora do vocabulário fechado de 
          +precedência") sobre um motivo perfeitamente válido se o mapa n
          +ão reconhecesse `MOEDA` desde já.                             
      514    - **RN atendidas:** RN-002 (7º campo do contrato).
      515    - **CA atendidos:** base para CA-048 (fechado em T-036).
      516    - **DT/seções do plan:** DT-019; plan §4 (`CampoCanonico` ga
           nha `MOEDA`), plan §10.
      517    - **Dependências:** nenhuma (extensão isolada de enum já exi
           stente, T-003).
      518    - **Arquivos que cria/modifica:**
      519      - `src/main/java/com/desafio/reembolso/modelo/CampoCanonic
           o.java`
      520 +    - `src/main/java/com/desafio/reembolso/pipeline/Compositor
          +Saida.java`                                                   
      521      - `tests/java/com/desafio/reembolso/modelo/VocabularioMoti
           voTest.java`
      522    - **Passos de implementação:**
      522 -    1. Adicionar `MOEDA` à enumeração, na posição que reflete 
          -a ordem canônica de contrato (após `VALOR`, antes de `TEM_NOTA
          -_FISCAL` — a task de reordenar `ORDEM_CAMPO` em `CompositorSai
          -da` é T-048; aqui só o enum ganha o valor).                   
      523 -    2. Nenhuma outra classe é tocada — o valor novo ainda não 
          -é produzido por nenhum validador nesta task.                  
      524 -  - **Testes obrigatórios:** `VocabularioMotivoTest` — paramet
          -rizar (ou acrescentar caso) confirmando que `CampoCanonico.MOE
          -DA.textoCanonico()` é exatamente `"despesa.moeda"`.           
      525 -  - **Critério de conclusão:** teste novo verde; suíte complet
          -a (`mvn test`) continua verde, porque nenhum código de produçã
          -o fora do enum foi alterado.                                  
      523 +    1. Adicionar `MOEDA` à enumeração, na posição que reflete 
          +a ordem canônica de contrato (após `VALOR`, antes de `TEM_NOTA
          +_FISCAL`).                                                    
      524 +    2. Atualizar imediatamente `CompositorSaida.criarOrdemCamp
          +o()` para a ordem canônica final de oito campos, definitiva de
          +sde esta task — não uma tabela provisória de sete campos à esp
          +era de T-048: `ID=0, DATA=1, CATEGORIA=2, DESCRICAO=3, FORNECE
          +DOR=4, VALOR=5, MOEDA=6, TEM_NOTA_FISCAL=7`.                  
      525 +    3. Confirmar que `OrdemMotivosTest` (T-016) continua verde
          + sem alteração — a mudança é aditiva e preserva a posição rela
          +tiva dos sete campos históricos (`TEM_NOTA_FISCAL` continua se
          +ndo o último, só desloca de `6` para `7`), então nenhum cenári
          +o já testado muda de resultado.                               
      526 +  - **Testes obrigatórios:** `VocabularioMotivoTest` — paramet
          +rizar (ou acrescentar caso) confirmando que `CampoCanonico.MOE
          +DA.textoCanonico()` é exatamente `"despesa.moeda"`. `OrdemMoti
          +vosTest` (T-016, sem alteração de código de teste) — roda como
          + regressão, confirmando que a nova posição de `TEM_NOTA_FISCAL
          +` não muda nenhuma ordem já verificada.                       
      527 +  - **Critério de conclusão:** teste novo verde; `OrdemMotivos
          +Test` (T-016) continua verde sem modificação; `mvn test` compl
          +eto verde.                                                    
      528    - **Comando de verificação:**
      529      ```
      528 -    mvn -q test -Dtest=VocabularioMotivoTest                  
      530 +    mvn -q test "-Dtest=VocabularioMotivoTest,OrdemMotivosTest
          +"                                                             
      531      ```
      530 -  - **Commit sugerido:** `feat(T-022): estende CampoCanonico c
          -om despesa.moeda`                                             
      532 +  - **Commit sugerido:** `feat(T-022): estende CampoCanonico c
          +om despesa.moeda e atualiza ordem de campo no CompositorSaida`
      533    - **Status:** [ ] pendente
      534  
      535  - [ ] **T-023** — Estender `MotivoCodigo` com os três códigos 
           novos

● Now T-023 (item 2):

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 8 lines, removed 6 lines
      533    - **Status:** [ ] pendente
      534  
      535  - [ ] **T-023** — Estender `MotivoCodigo` com os três códigos 
           novos
      536 -  - **O que faz:** acrescenta `MOEDA_SEM_COTACAO`, `CATEGORIA_
          -NAO_REEMBOLSAVEL_CENTRO_CUSTO` e `TETO_INDIVIDUAL_APLICADO` ao
          - vocabulário fechado de códigos de motivo (spec 4.5), cada um 
          -serializando para o próprio nome do enum (mesmo mecanismo já e
          -xistente).                                                    
      536 +  - **O que faz:** acrescenta `MOEDA_SEM_COTACAO`, `CATEGORIA_
          +NAO_REEMBOLSAVEL_CENTRO_CUSTO` e `TETO_INDIVIDUAL_APLICADO` ao
          + vocabulário fechado de códigos de motivo (spec 4.5), cada um 
          +serializando para o próprio nome do enum (mesmo mecanismo já e
          +xistente). No **mesmo commit**, `CompositorSaida.criarEstagios
          +()` é atualizado para reconhecer os três códigos — não se pode
          + esperar até T-048, porque `MOEDA_SEM_COTACAO` já é produzido 
          +em T-037 e integrado ao pipeline em T-038, `CATEGORIA_NAO_REEM
          +BOLSAVEL_CENTRO_CUSTO` já é produzido em T-041 e integrado em 
          +T-042, e `TETO_INDIVIDUAL_APLICADO` já é produzido em T-044 e 
          +integrado em T-046 — todos antes de T-048. Sem esta atualizaçã
          +o imediata, um motivo válido produzido por qualquer uma dessas
          + tasks faria `CompositorSaida.estagioDe(...)` lançar `IllegalA
          +rgumentException` ("código de motivo fora do vocabulário fecha
          +do de precedência").                                          
      537    - **RN atendidas:** RN-019, RN-020.
      538    - **CA atendidos:** base para CA-027, CA-030, CA-049 (fechad
           os nos blocos G/F/H).
      539    - **DT/seções do plan:** DT-019; plan §10.
      540    - **Dependências:** nenhuma.
      541    - **Arquivos que cria/modifica:**
      542      - `src/main/java/com/desafio/reembolso/modelo/MotivoCodigo
           .java`
      543 +    - `src/main/java/com/desafio/reembolso/pipeline/Compositor
          +Saida.java`                                                   
      544      - `tests/java/com/desafio/reembolso/modelo/VocabularioMoti
           voTest.java`
      545    - **Passos de implementação:**
      546      1. Adicionar os três valores ao enum, sem remover nem reno
           mear nenhum dos treze já existentes.
      546 -    2. Não alterar `CompositorSaida` nesta task — a tabela de 
          -estágios (`ESTAGIO_POR_CODIGO`) só ganha as entradas novas em 
          -T-048; até lá, esses códigos simplesmente não são produzidos p
          -or nenhuma classe.                                            
      547 -  - **Testes obrigatórios:** `VocabularioMotivoTest` — cada um
          - dos três novos valores serializa para o próprio nome (`"MOEDA
          -_SEM_COTACAO"`, `"CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO"`, `
          -"TETO_INDIVIDUAL_APLICADO"`).                                 
      548 -  - **Critério de conclusão:** teste novo verde; `mvn test` co
          -mpleto continua verde.                                        
      547 +    2. Atualizar imediatamente `CompositorSaida.criarEstagios(
          +)` para a tabela final de onze estágios (0 a 10), definitiva d
          +esde esta task: `0` `ITEM_TIPO_INVALIDO`; `1` motivos estrutur
          +ais; `2` `ID_DUPLICADO`; `3` `MOEDA_SEM_COTACAO`; `4` `VALOR_N
          +AO_POSITIVO`; `5` `CATEGORIA_FORA_POLITICA`; `6` `CATEGORIA_NA
          +O_REEMBOLSAVEL_CENTRO_CUSTO`; `7` `FORA_COMPETENCIA`; `8` `NOT
          +A_FISCAL_AUSENTE`; `9` `DUPLICIDADE`; `10` todos os motivos de
          + teto, incluindo `TETO_INDIVIDUAL_APLICADO`. A renumeração pre
          +serva a ordem relativa de todos os códigos históricos.        
      548 +    3. Confirmar que `OrdemMotivosTest` (T-016) continua verde
          + sem alteração — nenhum dos códigos históricos muda de posição
          + relativa, só os três novos ganham lugar entre eles.          
      549 +  - **Testes obrigatórios:** `VocabularioMotivoTest` — cada um
          + dos três novos valores serializa para o próprio nome (`"MOEDA
          +_SEM_COTACAO"`, `"CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO"`, `
          +"TETO_INDIVIDUAL_APLICADO"`). `OrdemMotivosTest` (T-016, sem a
          +lteração de código de teste) — roda como regressão da renumera
          +ção de estágios.                                              
      550 +  - **Critério de conclusão:** teste novo verde; `OrdemMotivos
          +Test` (T-016) continua verde sem modificação; `mvn test` compl
          +eto continua verde.                                           
      551    - **Comando de verificação:**
      552      ```
      551 -    mvn -q test -Dtest=VocabularioMotivoTest                  
      553 +    mvn -q test "-Dtest=VocabularioMotivoTest,OrdemMotivosTest
          +"                                                             
      554      ```
      553 -  - **Commit sugerido:** `feat(T-023): estende MotivoCodigo co
          -m os tres codigos da politica v4`                             
      555 +  - **Commit sugerido:** `feat(T-023): estende MotivoCodigo co
          +m os tres codigos da politica v4 e atualiza estagios do Compos
          +itorSaida`                                                    
      556    - **Status:** [ ] pendente
      557  
      558  - [ ] **T-024** — Estender `RegraNegocio` com RN-019 a RN-022

● Now fixing the T-036 reference to T-048 for the "moeda" ordering (now already
  done in T-022):

  Ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      871      2. Chamar `validarMoeda` dentro de `validarItem`, na posiç
           ão correspondente ao 7º campo (entre `valor` e `tem_nota_fisca
           l`).
      872      3. No retorno de `validarItem` para um elemento **objeto**
            (válido ou com campos inválidos), trocar a chamada do constru
           tor de dez argumentos pelo de catorze, passando `moeda` (resul
           tado de `validarMoeda`) e **explicitamente** `null` para `taxa
           CambioAplicada`, `dataCotacaoUtilizada` e `valorConvertidoBrut
           o` — nunca os valores de compatibilidade de BRL de T-029.
      873      4. No retorno de `validarItem` para um elemento que **não 
           é objeto** (`ITEM_TIPO_INVALIDO`), trocar a chamada do constru
           tor de dez argumentos pelo de catorze, passando `moeda = null`
            (não `"BRL"`) e os três derivados também `null` — reflete exa
           tamente a tabela de `spec.md` 4.2 para essa situação.
      874 -    5. Não alterar a ordem em que os motivos estruturais são a
          -dicionados à lista além da inserção de `moeda` na posição corr
          -eta — isso é o que faz a ordem canônica de contrato já refleti
          -r o 7º campo (a reordenação de apresentação em `CompositorSaid
          -a` é T-048).                                                  
      874 +    5. Não alterar a ordem em que os motivos estruturais são a
          +dicionados à lista além da inserção de `moeda` na posição corr
          +eta — isso é o que faz a ordem canônica de contrato já refleti
          +r o 7º campo. A apresentação já ordena `MOEDA` corretamente de
          +sde T-022 (`CompositorSaida.criarOrdemCampo()` já reconhece o 
          +valor) — esta task não precisa (nem deve) tocar `CompositorSai
          +da`.                                                          
      875    - **Testes obrigatórios:** `CampoMoedaTest` — os quatro cená
           rios fechados de `moeda` (ausência de chave → `"BRL"`, sem mot
           ivo; `null` explícito → `CAMPO_AUSENTE`; tipo não textual → `C
           AMPO_TIPO_INVALIDO`; formato fora de `[A-Z]{3}`, incluindo cai
           xa baixa `"usd"` → `CAMPO_FORMATO_INVALIDO`); **e**, adicional
           mente: um item BRL assumido por ausência de chave tem `taxaCam
           bioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto` tod
           os `null` **antes** de `ResolutorCambio` rodar (prova de que `
           ValidadorItem` não antecipa os derivados, nem para BRL); um it
           em com moeda estrangeira estruturalmente válida (ex.: `"EUR"`)
            também tem os três derivados `null` neste estágio; um element
           o que não é objeto tem `moeda`, `taxaCambioAplicada`, `dataCot
           acaoUtilizada` e `valorConvertidoBruto` todos `null`.
      876    - **Critério de conclusão:** os quatro cenários de `moeda` v
           erdes (CA-048), mais os três cenários novos de derivados nulos
            verdes; `mvn test` completo continua verde — os testes histór
           icos que constroem `ItemValidado` diretamente pelo construtor 
           de dez argumentos (sem passar por `ValidadorItem`) continuam i
           ntactos, porque a mudança é exclusivamente dentro de `Validado
           rItem.validarItem`.
      877    - **Comando de verificação:**

● Now let's work on item 3 — T-034. Let me read the full section:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 13 lines, removed 5 lines
      809  ### Bloco D — CLI de quatro flags
      810  
      811  - [ ] **T-034** — `Main.java`: parser de quatro flags
      812 -  - **O que faz:** reescreve o parser de argumentos de `Main.r
          -un(...)` conforme `plan.md` §3/DT-018: primeiro token deve ser
          - exatamente `"calcular"` (único posicional aceito); tokens res
          -tantes consumidos aos pares `flag valor`; pares acumulados num
          - `Map<String, String>` contando ocorrências por chave; ao fina
          -l, valida que as quatro chaves `--input`/`--output`/`--politic
          -a`/`--cambio` estão presentes, cada uma exatamente uma vez, e 
          -que nenhuma chave desconhecida apareceu. Qualquer violação → e
          -xit `2`. **Não** carrega `--politica`/`--cambio` ainda (isso é
          - T-035) — nesta task, as duas flags só são reconhecidas e arma
          -zenadas.                                                      
      812 +  - **O que faz:** reescreve o parser de argumentos de `Main.r
          +un(...)` conforme `plan.md` §3/DT-018: primeiro token deve ser
          + exatamente `"calcular"` (único posicional aceito); tokens res
          +tantes consumidos aos pares `flag valor`; pares acumulados num
          + `Map<String, String>` contando ocorrências por chave; ao fina
          +l, valida que as quatro chaves `--input`/`--output`/`--politic
          +a`/`--cambio` estão presentes, cada uma exatamente uma vez, e 
          +que nenhuma chave desconhecida apareceu. Qualquer violação → e
          +xit `2`. **Não** carrega `--politica`/`--cambio` ainda (isso é
          + T-035) — nesta task, as duas flags só são reconhecidas e arma
          +zenadas. Como o contrato de execução muda de duas para quatro 
          +flags obrigatórias, **todo** consumidor histórico de `Main.run
          +(...)` que pretende alcançar leitura, envelope, pipeline ou es
          +crita — não só `CliContratoTest` — precisa passar a fornecer a
          +s quatro flags nesta mesma task, ou seu cenário passa a falhar
          + no parser por um motivo alheio ao que o teste pretende verifi
          +car.                                                          
      813    - **RN atendidas:** RN-022 (parcial — contrato de execução).
      814    - **CA atendidos:** CA-041, CA-042.
      815    - **DT/seções do plan:** DT-003 (extensão), DT-018; plan §3.
     ...
      817    - **Arquivos que cria/modifica:**
      818      - `src/main/java/com/desafio/reembolso/Main.java`
      819      - `tests/java/com/desafio/reembolso/CliContratoTest.java`
      820 +    - `tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTes
          +t.java`                                                       
      821 +    - `tests/java/com/desafio/reembolso/ExemploCompletoTest.ja
          +va`                                                           
      822    - **Passos de implementação:**
      823      1. Substituir o `switch` de dois casos por: (a) checagem d
           o primeiro token == `"calcular"`, com exit `2` se ausente, dif
           erente, ou se sobrar um token posicional depois das flags; (b)
            laço que consome os tokens restantes aos pares — quantidade í
           mpar, ou uma flag como último token, é exit `2`.
      824      2. Popular um `Map<String, String>` com os pares reconheci
           dos; uma chave repetida ou uma flag fora do conjunto `{--input
           , --output, --politica, --cambio}` é exit `2`.
      825      3. Depois de percorrer todos os pares, verificar que as qu
           atro chaves obrigatórias estão presentes — qualquer ausência é
            exit `2`.
      826      4. Extrair `inputPath`/`outputPath`/`politicaPath`/`cambio
           Path` do mapa; **manter o restante do método exatamente como e
           stá** (leitura de `--input`, validação de envelope, pipeline, 
           escrita) — `politicaPath`/`cambioPath` ficam sem uso nesta tas
           k (variável local não referenciada além da extração, ou passad
           a adiante sem efeito ainda).
      825 -  - **Testes obrigatórios:** `CliContratoTest` — sucesso com a
          -s quatro flags em ordem arbitrária (ainda sem `LeitorPolitica`
          -/`LeitorCambio` reais entrando no caminho de erro — resultado 
          -de negócio é irrelevante aqui, só o exit code); subcomando aus
          -ente; subcomando diferente de `calcular`; token posicional ext
          -ra depois de `calcular`; flag sem valor (última posição); quan
          -tidade ímpar de tokens; flag repetida; flag desconhecida; cada
          - uma das quatro flags obrigatórias faltando isoladamente; coma
          -ndo antigo só com `--input`/`--output` (exit `2`, porque falta
          -m `--politica`/`--cambio`).                                   
      826 -  - **Critério de conclusão:** todos os cenários acima com o e
          -xit code esperado; nenhuma escrita em `--output` nos cenários 
          -de exit `2`.                                                  
      827 +    5. Atualizar a constante `USO` de `Main.java` para o contr
          +ato completo: `"Uso: java -jar motor-reembolso.jar calcular --
          +input <arquivo> --output <arquivo> --politica <arquivo> --camb
          +io <arquivo>"`.                                               
      828 +    6. Rodar o inventário `git grep "Main.run" -- tests/java` 
          +e migrar toda invocação histórica que pretenda alcançar leitur
          +a do envelope, pipeline ou escrita para fornecer as quatro fla
          +gs, usando `exemplos/envelope/politica-v4.json` e `exemplos/en
          +velope/cambio.json` como arquivos externos válidos (já existem
          + no repositório; a tabela `padrao` de `politica-v4.json` prese
          +rva os mesmos limites da baseline histórica — R$60/R$80/R$250/
          +R$100 — então nenhum resultado financeiro de um teste BRL muda
          + por causa dessa migração). Regras da migração:               
      829 +       - testes de parser que **intencionalmente** verificam f
          +lag ausente, comando antigo ou argumentos inválidos **continua
          +m** construindo a linha de comando incompleta necessária ao pr
          +óprio cenário — não são migrados;                             
      830 +       - testes de entrada inexistente, envelope inválido, esc
          +rita atômica e sucesso passam a fornecer `--politica`/`--cambi
          +o` válidos, para que, a partir de T-035, a falha observada con
          +tinue vindo exatamente da camada que o teste pretende verifica
          +r (arquivo de entrada, envelope, ou escrita), não do parser;  
      831 +       - `ExemploCompletoTest` (T-020) passa a invocar com as 
          +quatro flags;                                                 
      832 +       - `EscritaAtomicaSaidaTest` (T-019) passa a invocar com
          + as quatro flags em todos os cenários que pretendem ultrapassa
          +r o parser (ou seja, todos exceto os que testam o próprio cont
          +rato de argumentos, se houver).                               
      833 +  - **Testes obrigatórios:** `CliContratoTest` — sucesso com a
          +s quatro flags em ordem arbitrária (ainda sem `LeitorPolitica`
          +/`LeitorCambio` reais entrando no caminho de erro — resultado 
          +de negócio é irrelevante aqui, só o exit code); subcomando aus
          +ente; subcomando diferente de `calcular`; token posicional ext
          +ra depois de `calcular`; flag sem valor (última posição); quan
          +tidade ímpar de tokens; flag repetida; flag desconhecida; cada
          + uma das quatro flags obrigatórias faltando isoladamente; coma
          +ndo antigo só com `--input`/`--output` (exit `2`, porque falta
          +m `--politica`/`--cambio` — este é o único cenário autorizado 
          +a continuar usando o comando de duas flags, porque é exatament
          +e ele que prova a rejeição). `EscritaAtomicaSaidaTest` e `Exem
          +ploCompletoTest` — suíte completa (`mvn test`) confirma que ne
          +nhum dos dois quebrou com a migração para quatro flags.       
      834 +  - **Critério de conclusão:** todos os cenários acima com o e
          +xit code esperado; nenhuma escrita em `--output` nos cenários 
          +de exit `2`; nenhum teste histórico continua dependendo do com
          +ando antigo de duas flags, salvo o teste específico que confir
          +ma sua rejeição (CA-042); `mvn test` completo verde.          
      835    - **Comando de verificação:**
      836      ```
      829 -    mvn -q test -Dtest=CliContratoTest                        
      837 +    mvn -q test                                               
      838      ```
      831 -  - **Commit sugerido:** `feat(T-034): reescreve parser da CLI
          - para quatro flags obrigatorias`                              
      839 +  - **Commit sugerido:** `feat(T-034): reescreve parser da CLI
          + para quatro flags obrigatorias e migra consumidores historico
          +s de Main.run`                                                
      840    - **Status:** [ ] pendente
      841  
      842  - [ ] **T-035** — `Main.java`: carregar e validar política e c
           âmbio antes do envelope

● Now item 4 — T-038. Let me read the current section fully:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 27 lines, removed 6 lines
      917    - **Commit sugerido:** `feat(T-037): implementa ResolutorCam
           bio com verificacao de valor/moeda/data e CotacaoResolvida`
      918    - **Status:** [ ] pendente
      919  
      920 -- [ ] **T-038** — `Normalizador` sobre `valorConvertidoBruto` 
          -+ wiring da conversão no pipeline                             
      921 -  - **O que faz:** `Normalizador` passa a normalizar `item.get
          -ValorConvertidoBruto()` em vez de `item.getValor()` diretament
          -e — mesmo caminho para BRL e moeda estrangeira, sem `if` de BR
          -L dentro do `Normalizador` (plan §9). `Main.executarPipeline` 
          -ganha o estágio `ResolutorCambio.resolverLista(...)`, inserido
          - entre `DetectorIdDuplicado.detectar(...)` e `Normalizador.nor
          -malizarLista(...)` (spec 8.1, passo 5), usando a `TabelaCambio
          -` já carregada em T-035. Esta task fecha, de ponta a ponta, a 
          -garantia de arredondamento único (RN-004/RN-020) e a comparaçã
          -o do gatilho de nota fiscal pelo valor convertido (RN-009 já l
          -ê `item.valorNormalizado()`, que passa a ser o valor convertid
          -o sem nenhuma mudança na própria `AvaliadorRegrasIndividuais`)
          -.                                                             
      920 +- [ ] **T-038** — `Normalizador` sobre `valorConvertidoBruto` 
          ++ wiring da conversão no pipeline + migração dos pipelines de 
          +teste históricos                                              
      921 +  - **O que faz:** `Normalizador` passa a normalizar `item.get
          +ValorConvertidoBruto()` em vez de `item.getValor()` diretament
          +e — mesmo caminho para BRL e moeda estrangeira, sem `if` de BR
          +L dentro do `Normalizador` (plan §9), e **sem nenhum fallback*
          +* para `item.getValor()`. `Main.executarPipeline` ganha o está
          +gio `ResolutorCambio.resolverLista(...)`, inserido entre `Dete
          +ctorIdDuplicado.detectar(...)` e `Normalizador.normalizarLista
          +(...)` (spec 8.1, passo 5), usando a `TabelaCambio` já carrega
          +da em T-035. Como T-036 já deixa `valorConvertidoBruto` nulo e
          +m todo `ItemValidado` produzido por `ValidadorItem` (só `Resol
          +utorCambio` o preenche, inclusive para BRL), **qualquer teste 
          +histórico** que exercite `ValidadorItem` → (opcionalmente `Det
          +ectorIdDuplicado`) → `Normalizador` sem passar por `ResolutorC
          +ambio` quebraria ou passaria a obter `valor_normalizado` nulo 
          +assim que esta task mudar a leitura do `Normalizador`. Por iss
          +o, esta task também migra esses pipelines de teste e cria o he
          +lper `CambioTesteSupport`, no mesmo commit — não é aceitável t
          +rocar a fonte de leitura do `Normalizador` e deixar a suíte hi
          +stórica quebrada até uma task futura arrumar.                 
      922    - **RN atendidas:** RN-004 (atualizada), RN-009 (atualizada)
           , RN-016 (extensão a moeda), RN-020.
      923    - **CA atendidos:** CA-028, CA-031, CA-032.
      924    - **DT/seções do plan:** DT-004 (extensão), DT-015; plan §9,
            §14.
      925 -  - **Dependências:** T-035 (`TabelaCambio` carregada no `Main
          -`), T-037 (`ResolutorCambio`).                                
      925 +  - **Dependências:** T-035 (`TabelaCambio` carregada no `Main
          +`), T-036 (`valorConvertidoBruto` nulo até `ResolutorCambio` —
          + motivo da migração), T-037 (`ResolutorCambio`).              
      926    - **Arquivos que cria/modifica:**
      927      - `src/main/java/com/desafio/reembolso/pipeline/Normalizad
           or.java`
      928      - `src/main/java/com/desafio/reembolso/Main.java`
      929      - `tests/java/com/desafio/reembolso/pipeline/ConversaoCamb
           ialIntegracaoTest.java`
      930      - `tests/java/com/desafio/reembolso/pipeline/RegraViagemEf
           eitoNuloTest.java` (estendido)
      931 +    - `tests/java/com/desafio/reembolso/pipeline/CambioTesteSu
          +pport.java` (novo — helper de teste)                          
      932 +    - `tests/java/com/desafio/reembolso/pipeline/CamposDesconh
          +ecidosTest.java`                                              
      933 +    - `tests/java/com/desafio/reembolso/pipeline/CategoriaFora
          +PoliticaTest.java`                                            
      934 +    - `tests/java/com/desafio/reembolso/pipeline/CompetenciaTe
          +st.java`                                                      
      935 +    - `tests/java/com/desafio/reembolso/pipeline/ComposicaoSai
          +daTest.java`                                                  
      936 +    - `tests/java/com/desafio/reembolso/pipeline/DistribuicaoT
          +etoTest.java`                                                 
      937 +    - `tests/java/com/desafio/reembolso/pipeline/DuplicidadeEc
          +onomicaTest.java`                                             
      938 +    - `tests/java/com/desafio/reembolso/pipeline/NormalizacaoC
          +ategoriaTest.java`                                            
      939 +    - `tests/java/com/desafio/reembolso/pipeline/NormalizacaoM
          +onetariaTest.java`                                            
      940 +    - `tests/java/com/desafio/reembolso/pipeline/NotaFiscalTes
          +t.java`                                                       
      941 +    - `tests/java/com/desafio/reembolso/pipeline/OrdemMotivosT
          +est.java`                                                     
      942 +    - `tests/java/com/desafio/reembolso/pipeline/ReembolsoParc
          +ialTest.java`                                                 
      943 +    - `tests/java/com/desafio/reembolso/pipeline/TetoDiarioTes
          +t.java`                                                       
      944 +    - `tests/java/com/desafio/reembolso/pipeline/TetoHospedage
          +mTest.java`                                                   
      945 +    - `tests/java/com/desafio/reembolso/pipeline/ValorNaoPosit
          +ivoTest.java`                                                 
      946 +                                                              
      947 +    (a lista acima reflete o estado atual do repositório nesta
          + sessão de planejamento; o inventário do passo 5 abaixo, execu
          +tado dentro da própria task, é a fonte da verdade final sobre 
          +quais arquivos precisam de fato ser tocados.)                 
      948    - **Passos de implementação:**
      932 -    1. Em `Normalizador.normalizarValor(...)`, trocar a leitur
          -a de `item.getValor()` por `item.getValorConvertidoBruto()` co
          -mo entrada do `setScale(2, RoundingMode.HALF_UP)` — único pont
          -o de arredondamento de todo o pipeline (DT-015).              
      949 +    1. Em `Normalizador.normalizarValor(...)`, trocar a leitur
          +a de `item.getValor()` por `item.getValorConvertidoBruto()` co
          +mo entrada do `setScale(2, RoundingMode.HALF_UP)` — único pont
          +o de arredondamento de todo o pipeline (DT-015). Nenhum `if`/f
          +allback que leia `item.getValor()` diretamente permanece no mé
          +todo.                                                         
      950      2. Em `Main.executarPipeline`, inserir `List<ItemValidado>
            comCambio = ResolutorCambio.resolverLista(idsVerificados, tab
           elaCambio);` entre a chamada a `DetectorIdDuplicado.detectar(.
           ..)` e `Normalizador.normalizarLista(...)`, e passar `comCambi
           o` (não mais `idsVerificados`) ao normalizador.
      951      3. `tabelaCambio` precisa estar acessível em `executarPipe
           line` — repassar como parâmetro do método (assinatura muda de 
           `executarPipeline(Envelope envelope)` para `executarPipeline(E
           nvelope envelope, TabelaCambio cambio)`, com a instância obtid
           a em T-035).
      935 -  - **Testes obrigatórios:** `ConversaoCambialIntegracaoTest` 
          -— teste-canário real de ordem de arredondamento (`valor = 1.00
          -5`, `taxa = 1.005`, esperado `1,01` via pipeline completo `Res
          -olutorCambio` + `Normalizador`, nunca `1,02`); cenário normati
          -vo USD `40,00 × 5,50 = 220,00` (CA-031); nota fiscal aplicada 
          -sobre o valor convertido — USD `40,00` (abaixo de R$100 na moe
          -da original) convertido para `220,00`, sem nota fiscal, recusa
          -do com `NOTA_FISCAL_AUSENTE` (CA-032). `RegraViagemEfeitoNuloT
          -est` (estendido) — um item elegível em moeda estrangeira não a
          -mplia teto algum nem afeta outros itens do mesmo dia/período; 
          -trocar a moeda de um item de `BRL` para `EUR` (com cotação vál
          -ida) não altera o comportamento de RN-016 (CA-028).           
      936 -  - **Critério de conclusão:** todos os cenários acima verdes;
          - `mvn test` completo verde, incluindo toda a suíte histórica d
          -e T-001 a T-021 (regressão: para despesas BRL, `valorConvertid
          -oBruto == valor`, então o resultado normalizado não muda).    
      952 +    4. Criar `tests/java/com/desafio/reembolso/pipeline/Cambio
          +TesteSupport.java` — classe/helper `package-private`, usada so
          +mente por testes do pacote `pipeline`, sem nenhuma regra de pr
          +odução: constrói uma `TabelaCambio` válida com `moedaBase = "B
          +RL"` e mapa de cotações vazio (suficiente para qualquer cenári
          +o BRL, já que `ResolutorCambio` não consulta a tabela de taxas
          + para BRL — só preenche `taxaCambioAplicada = 1`, `dataCotacao
          +Utilizada = null`, `valorConvertidoBruto = valor`); oferece mé
          +todos que aplicam `ResolutorCambio.resolver(...)`/`resolverLis
          +ta(...)` sobre um `ItemValidado` (ou uma `List<ItemValidado>`)
          + antes de repassar ao `Normalizador`, poupando cada teste de r
          +epetir esse encadeamento manualmente.                         
      953 +    5. Rodar o inventário `git grep "Normalizador.normalizar" 
          +-- tests/java/com/desafio/reembolso/pipeline` e, para cada oco
          +rrência: se o `ItemValidado`/`ItemNormalizado` de entrada vier
          + de `ValidadorItem` (isto é, o teste exercita o pipeline desde
          + a validação estrutural), inserir `ResolutorCambio` (via `Camb
          +ioTesteSupport`) entre `DetectorIdDuplicado` (quando o teste t
          +ambém o usa) e `Normalizador` — migrando o encadeamento para `
          +ValidadorItem` → `DetectorIdDuplicado` (quando aplicável) → `R
          +esolutorCambio` → `Normalizador`; se o teste constrói diretame
          +nte um `ItemValidado` já preparado para exercitar o `Normaliza
          +dor` isoladamente (unidade), **não** precisa passar pelo pipel
          +ine inteiro — só precisa que o `ItemValidado` de entrada já te
          +nha `valorConvertidoBruto` preenchido corretamente (via o cons
          +trutor de catorze argumentos, T-029).                         
      954 +    6. Antes de finalizar a task, repetir o grep do passo 5 e 
          +confirmar manualmente: (a) toda chamada a `Normalizador` cuja 
          +entrada venha de `ValidadorItem` passou antes por `ResolutorCa
          +mbio`; (b) testes que constroem diretamente um `ItemValidado` 
          +já preparado para a unidade isolada do `Normalizador` continua
          +m sem executar o pipeline inteiro, e isso é aceitável; (c) nen
          +hum fallback para `item.getValor()` foi adicionado ao `Normali
          +zador`; (d) `mvn test` completo está verde.                   
      955 +  - **Testes obrigatórios:** `ConversaoCambialIntegracaoTest` 
          +— teste-canário real de ordem de arredondamento (`valor = 1.00
          +5`, `taxa = 1.005`, esperado `1,01` via pipeline completo `Res
          +olutorCambio` + `Normalizador`, nunca `1,02`); cenário normati
          +vo USD `40,00 × 5,50 = 220,00` (CA-031); nota fiscal aplicada 
          +sobre o valor convertido — USD `40,00` (abaixo de R$100 na moe
          +da original) convertido para `220,00`, sem nota fiscal, recusa
          +do com `NOTA_FISCAL_AUSENTE` (CA-032). `RegraViagemEfeitoNuloT
          +est` (estendido) — um item elegível em moeda estrangeira não a
          +mplia teto algum nem afeta outros itens do mesmo dia/período; 
          +trocar a moeda de um item de `BRL` para `EUR` (com cotação vál
          +ida) não altera o comportamento de RN-016 (CA-028). Toda a suí
          +te histórica listada em "Arquivos que cria/modifica", migrada 
          +para usar `CambioTesteSupport` onde exercita o pipeline desde 
          +`ValidadorItem` — nenhuma delas fica com `valor_normalizado` n
          +ulo por engano.                                               
      956 +  - **Critério de conclusão:** todos os cenários acima verdes;
          + toda chamada a `Normalizador` cuja entrada venha de `Validado
          +rItem` passa antes por `ResolutorCambio` (confirmado pelo grep
          + repetido do passo 6); nenhum fallback para `item.getValor()` 
          +existe em `Normalizador.java`; `mvn test` completo verde, incl
          +uindo toda a suíte histórica de T-001 a T-021 migrada (regress
          +ão: para despesas BRL, `valorConvertidoBruto == valor`, então 
          +o resultado normalizado não muda).                            
      957    - **Comando de verificação:**
      958      ```
      959 +    git grep "Normalizador.normalizar" -- tests/java/com/desaf
          +io/reembolso/pipeline                                         
      960      mvn -q test
      961      ```
      962    - **Commit sugerido:** `feat(T-038): normaliza sobre valorCo
           nvertidoBruto e integra ResolutorCambio no pipeline`

● Now item 5 — T-046. Let me locate and read it:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 4 lines, removed 5 lines
      1130    - **Status:** [ ] pendente
      1131  
      1132  - [ ] **T-046** — Wiring dos agregadores por periodicidade no
             `Main`
      1133 -  - **O que faz:** `Main.executarPipeline` passa a chamar as 
           -novas sobrecargas de `AgregadorTetoDiario.aplicar(elegiveis, 
           -tabelaResolvida)` e `AgregadorTetoIndividual.aplicar(elegivei
           -s, tabelaResolvida)`, em vez de `AgregadorTetoDiario`(antigo)
           -/`AgregadorTetoHospedagem` — usando a mesma `TabelaPoliticaRe
           -solvida` já calculada em T-042. Como esta task é o ponto em q
           -ue os dois agregadores novos passam a ser efetivamente exerci
           -tados pelo `Main` real, `CliContratoTest` precisa exercer os 
           -**dois** caminhos (`"dia"` e `"diaria"`) através do binário, 
           -não só o cenário de recusa por limite zero já coberto.       
      1133 +  - **O que faz:** `Main.executarPipeline` passa a chamar as 
           +novas sobrecargas de `AgregadorTetoDiario.aplicar(elegiveis, 
           +tabelaResolvida)` e `AgregadorTetoIndividual.aplicar(elegivei
           +s, tabelaResolvida)`, em vez de `AgregadorTetoDiario`(antigo)
           +/`AgregadorTetoHospedagem` — usando a mesma `TabelaPoliticaRe
           +solvida` já calculada em T-042. Como esta task é o ponto em q
           +ue os dois agregadores novos passam a ser efetivamente exerci
           +tados pelo `Main` real, `CliContratoTest` precisa exercer os 
           +**dois** caminhos (`"dia"` e `"diaria"`) **por `Main.run(...)
           +`**, não só o cenário de recusa por limite zero já coberto. E
           +stratégia de dados fechada, sem alternativa: nenhum fixture p
           +ermanente novo é criado em `tests/resources` — `CliContratoTe
           +st` usa `@TempDir`; a política e o envelope de cada um dos do
           +is cenários novos (`representacao`/`"dia"` e `estacionamento`
           +/`"diaria"`) são escritos em arquivos temporários pelo própri
           +o teste, dentro do `@TempDir`; `--output` também aponta para 
           +um arquivo dentro do `@TempDir`; `--cambio` usa `exemplos/env
           +elope/cambio.json` (arquivo real já existente, mesmo quando o
           + cenário não tem despesa em moeda estrangeira). A execução re
           +al do JAR empacotado continua pertencendo exclusivamente a T-
           +054 — esta task só usa `Main.run(...)` in-process.           
      1134    - **RN atendidas:** RN-011 a RN-015, RN-019.
      1135    - **CA atendidos:** confirma CA-027, CA-047 e CA-049 ponta 
            a ponta via CLI.
      1136    - **DT/seções do plan:** DT-017; plan §2 (passo 9), §11.
     ...
      1138    - **Arquivos que cria/modifica:**
      1139      - `src/main/java/com/desafio/reembolso/Main.java`
      1140      - `tests/java/com/desafio/reembolso/CliContratoTest.java`
      1141 -    - `tests/resources/fixtures/politica-teste-periodicidade.
           -json` e `tests/resources/fixtures/despesas-teste-periodicidad
           -e.json` (fixtures temporárias de política e envelope para os 
           -dois cenários novos, ou equivalentes gerados em diretório tem
           -porário pelo próprio teste)                                  
      1141    - **Passos de implementação:**
      1142      1. Substituir, em `executarPipeline`, as chamadas a `Agre
            gadorTetoDiario.aplicar(elegiveisParaTetos)` e `AgregadorTeto
            Hospedagem.aplicar(elegiveisParaTetos)` pelas novas sobrecarg
            as, passando `tabelaResolvida`.
      1143      2. `CompositorSaida.compor(...)` continua recebendo duas 
            listas de `ResultadoTeto` (uma do agregador de `"dia"`, outra
             do de `"diaria"`) — a assinatura de `compor` não muda, só a 
            origem das listas.
      1145 -    3. Construir uma política e um envelope de teste para o c
           -enário `"dia"`: categoria dinâmica `representacao`, `periodic
           -idade: "dia"`, `limite: 300.00`, e dois itens elegíveis dessa
           - categoria na mesma data no envelope, exercitando o saldo com
           -partilhado.                                                  
      1146 -    4. Construir uma política e um envelope de teste para o c
           -enário `"diaria"`: categoria dinâmica `estacionamento`, `peri
           -odicidade: "diaria"`, `limite: 50.00`, e um item elegível de 
           -`80.00` nessa categoria no envelope.                         
      1147 -    5. Em ambos os cenários novos, invocar `Main.run(...)` co
           -m as quatro flags reais (`calcular --input ... --output ... -
           --politica ... --cambio ...`) e ler o JSON de saída produzido 
           -— nunca chamar `executarPipeline` diretamente, porque o objet
           -ivo é provar que o wiring real do `Main` está correto.       
      1144 +    3. No cenário `"dia"` de `CliContratoTest`: escrever, den
           +tro de `@TempDir`, uma política de teste com categoria dinâmi
           +ca `representacao`, `periodicidade: "dia"`, `limite: 300.00`,
           + e um envelope de teste com dois itens elegíveis dessa catego
           +ria na mesma data, exercitando o saldo compartilhado.        
      1145 +    4. No cenário `"diaria"` de `CliContratoTest`: escrever, 
           +dentro de `@TempDir`, uma política de teste com categoria din
           +âmica `estacionamento`, `periodicidade: "diaria"`, `limite: 5
           +0.00`, e um envelope de teste com um item elegível de `80.00`
           + nessa categoria.                                            
      1146 +    5. Em ambos os cenários novos, invocar `Main.run(...)` (n
           +unca `executarPipeline` diretamente — o objetivo é provar que
           + o wiring real do `Main` está correto) com as quatro flags re
           +ais, `--input`/`--politica` apontando para os arquivos tempor
           +ários escritos pelo próprio teste, `--cambio` apontando para 
           +`exemplos/envelope/cambio.json`, e `--output` apontando para 
           +um arquivo dentro do mesmo `@TempDir`; ler o JSON de saída pr
           +oduzido para as asserções.                                   
      1147    - **Testes obrigatórios:** `CliContratoTest` — **três** cen
            ários:
      1148      1. (já existente) limite zero em `CC-ENG-PLATAFORMA`: exe
            cução real com `--politica` apontando para essa tabela (hospe
            dagem limite `0,00`) recusa o item de hospedagem com `CATEGOR
            IA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` **antes** de qualquer agreg
            ador de teto ser exercitado (spec 8.4, item 15).
      1149      2. Cenário `"dia"`: `representacao` (`periodicidade: "dia
            "`, limite `300.00`), dois itens na mesma data no envelope, s
            aldo compartilhado — o JSON de saída, lido depois de `Main.ru
            n(...)` com as quatro flags, comprova que `AgregadorTetoDiari
            o` (sobrecarga nova) foi de fato chamado: motivo `TETO_DIARIO
            _APLICADO` ou `TETO_DIARIO_ESGOTADO`, regra `RN-019` (CA-047)
            .

● Now item 6 — T-048. Let me read the full current section:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 9 lines, removed 6 lines
      1181    - **Commit sugerido:** `feat(T-047): adiciona moeda a chave
             de duplicidade economica`
      1182    - **Status:** [ ] pendente
      1183  
      1184 -- [ ] **T-048** — Estender `CompositorSaida` com campos de câ
           -mbio e nova ordem de motivos                                 
      1185 -  - **O que faz:** `ResultadoItem` ganha três campos (`moeda`
           -, `taxaCambioAplicada`, `dataCotacaoUtilizada`), populados se
           -m recálculo a partir do `ItemValidado` de cada posição. `ORDE
           -M_CAMPO` ganha `CampoCanonico.MOEDA` entre `VALOR` e `TEM_NOT
           -A_FISCAL` (deslocando `TEM_NOTA_FISCAL` de 6 para 7). `ESTAGI
           -O_POR_CODIGO` ganha `MOEDA_SEM_COTACAO` (estágio 3, entre `ID
           -_DUPLICADO` e `VALOR_NAO_POSITIVO`) e `CATEGORIA_NAO_REEMBOLS
           -AVEL_CENTRO_CUSTO` (estágio 6, entre `CATEGORIA_FORA_POLITICA
           -` e `FORA_COMPETENCIA`) e `TETO_INDIVIDUAL_APLICADO` (mesmo e
           -stágio dos demais motivos de teto), com a renumeração complet
           -a de 8.3: `0` `ITEM_TIPO_INVALIDO`, `1` estruturais, `2` `ID_
           -DUPLICADO`, `3` `MOEDA_SEM_COTACAO`, `4` `VALOR_NAO_POSITIVO`
           -, `5` `CATEGORIA_FORA_POLITICA`, `6` `CATEGORIA_NAO_REEMBOLSA
           -VEL_CENTRO_CUSTO`, `7` `FORA_COMPETENCIA`, `8` `NOTA_FISCAL_A
           -USENTE`, `9` `DUPLICIDADE`, `10` motivos de teto.            
      1184 +- [ ] **T-048** — Estender `ResultadoItem` com campos de câmb
           +io, migrar construtores diretos e consolidar a ordem final do
           + `CompositorSaida`                                           
      1185 +  - **O que faz:** `ResultadoItem` ganha três campos (`moeda`
           +, `taxaCambioAplicada`, `dataCotacaoUtilizada`), populados se
           +m recálculo a partir do `ItemValidado` de cada posição, dentr
           +o de `componentesDoRegistro(...)`. `ORDEM_CAMPO` e `ESTAGIO_P
           +OR_CODIGO` **já foram atualizados** para a ordem final desde 
           +T-022/T-023 respectivamente — esta task não os introduz pela 
           +primeira vez; ela só **revisa e amplia** `OrdemMotivosTest` p
           +ara comprovar, com os motivos novos já em uso desde os blocos
           + F/G/H, que a tabela completa de 8.3 está correta de ponta a 
           +ponta. Como `ResultadoItem` é um `record` cuja assinatura mud
           +a de onze para catorze componentes, **todo** `new ResultadoIt
           +em(...)` já existente na suíte precisa ser migrado no mesmo c
           +ommit — não se cria um construtor de compatibilidade de onze 
           +argumentos: um `record` não admite sobrecarga "antiga" que ig
           +nore campos do jeito que uma classe comum admitiria, e introd
           +uzir uma fábrica de compatibilidade só para adiar a migração 
           +contrariaria a própria regra de um commit coerente por task. 
      1186    - **RN atendidas:** RN-017 (atualizada).
      1187    - **CA atendidos:** CA-034.
      1188    - **DT/seções do plan:** DT-019; plan §10.
      1189 -  - **Dependências:** T-022 (`CampoCanonico.MOEDA`), T-023 (t
           -rês `MotivoCodigo` novos), T-029 (getters de câmbio em `ItemV
           -alidado`), T-038 (conversão cambial e `Normalizador` já integ
           -rados ao pipeline — necessário porque `SaidaCambioTest` exige
           - BRL resolvido, moeda estrangeira convertida, moeda sem cotaç
           -ão, e conversão preservada mesmo com outro campo estruturalme
           -nte inválido).                                               
      1189 +  - **Dependências:** T-022 (`CampoCanonico.MOEDA` e `ORDEM_C
           +AMPO` já atualizados), T-023 (três `MotivoCodigo` novos e `ES
           +TAGIO_POR_CODIGO` já atualizado), T-029 (getters de câmbio em
           + `ItemValidado`), T-038 (conversão cambial e `Normalizador` j
           +á integrados ao pipeline — necessário porque `SaidaCambioTest
           +` exige BRL resolvido, moeda estrangeira convertida, moeda se
           +m cotação, e conversão preservada mesmo com outro campo estru
           +turalmente inválido).                                        
      1190    - **Arquivos que cria/modifica:**
      1191      - `src/main/java/com/desafio/reembolso/pipeline/Composito
            rSaida.java`
      1192      - `tests/java/com/desafio/reembolso/pipeline/OrdemMotivos
            Test.java` (estendido)
      1193      - `tests/java/com/desafio/reembolso/pipeline/SaidaCambioT
            est.java`
      1194 +    - `tests/java/com/desafio/reembolso/escritor/EscritorResu
           +ltadoTest.java`                                              
      1195 +    - `tests/java/com/desafio/reembolso/pipeline/TotalPeriodo
           +Test.java`                                                   
      1196    - **Passos de implementação:**
      1197      1. Adicionar os três campos ao `record ResultadoItem`, co
            m construção populada em `componentesDoRegistro(...)` a parti
            r de `itemValidado.getMoeda()`/`getTaxaCambioAplicada()`/`get
            DataCotacaoUtilizada()` — nos três pontos de retorno do métod
            o (recusado antes da duplicidade, recusado por duplicidade, r
            esultado de teto).
      1196 -    2. Atualizar `criarOrdemCampo()` com a ordem canônica de 
           -contrato completa, exatamente: `ID=0, DATA=1, CATEGORIA=2, DE
           -SCRICAO=3, FORNECEDOR=4, VALOR=5, MOEDA=6, TEM_NOTA_FISCAL=7`
           -.                                                            
      1197 -    3. Atualizar `criarEstagios()` com onze estágios, numerad
           -os de 0 a 10; o mapa contém uma entrada para cada `MotivoCodi
           -go`, e vários códigos podem compartilhar o mesmo estágio (a t
           -abela completa de estágio por código já está descrita em "O q
           -ue faz", acima).                                             
      1198 -  - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) —
           - um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` 
           -simultâneos aparece nessa ordem; um item com erro estrutural 
           -em `despesa.moeda` aparece na posição correta da ordem canôni
           -ca de campo (entre `valor` e `tem_nota_fiscal`). `SaidaCambio
           -Test` — os quatro formatos fechados de `moeda`/`taxa_cambio_a
           -plicada`/`data_cotacao_utilizada` no `ResultadoItem`:        
      1198 +    2. Rodar o inventário `git grep "new ResultadoItem(" -- t
           +ests/java` e migrar **todos** os construtores diretos para a 
           +assinatura nova (catorze componentes) — inclui, no mínimo, `E
           +scritorResultadoTest.java` e `TotalPeriodoTest.java`. Nos cen
           +ários históricos sintéticos em que os campos de câmbio não sã
           +o o objeto do teste, preencher explicitamente `moeda = "BRL"`
           +, `taxaCambioAplicada = BigDecimal.ONE`, `dataCotacaoUtilizad
           +a = null` — nunca deixar um construtor de compatibilidade de 
           +sete (ou onze) argumentos em `ResultadoItem` para adiar essa 
           +migração.                                                    
      1199 +    3. Confirmar, por leitura, que `criarOrdemCampo()` e `cri
           +arEstagios()` já refletem a tabela final (herdada de T-022/T-
           +023) — nenhuma alteração nesses dois métodos é esperada nesta
           + task, salvo se a conferência revelar uma divergência real, c
           +aso em que a correção entra no mesmo commit.                 
      1200 +    4. Ampliar `OrdemMotivosTest` para exercitar, com os moti
           +vos e o `ResultadoItem` de catorze componentes já disponíveis
           +, a tabela completa de 8.3 de ponta a ponta — não apenas os s
           +ete campos/motivos históricos.                               
      1201 +  - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) —
           + um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` 
           +simultâneos aparece nessa ordem; um item com erro estrutural 
           +em `despesa.moeda` aparece na posição correta da ordem canôni
           +ca de campo (entre `valor` e `tem_nota_fiscal`); a tabela com
           +pleta de estágios (0 a 10) é exercitada com pelo menos um mot
           +ivo de cada estágio. `EscritorResultadoTest` e `TotalPeriodoT
           +est` — migrados para a assinatura de catorze componentes de `
           +ResultadoItem`, continuam verdes sem mudança de comportamento
           + observável (a serialização dos três campos novos só é implem
           +entada em T-049; nesta task, `EscritorResultadoTest` ainda te
           +sta exclusivamente o que já testava antes, só com o construto
           +r atualizado). `SaidaCambioTest` — os quatro formatos fechado
           +s de `moeda`/`taxa_cambio_aplicada`/`data_cotacao_utilizada` 
           +no `ResultadoItem`:                                          
      1202      - **BRL válido:** `moeda = "BRL"`, `taxa_cambio_aplicada 
            = 1`, `data_cotacao_utilizada = null`.
      1203      - **Moeda estrangeira convertida:** `moeda` = código orig
            inal válido (ex.: `"EUR"`), `taxa_cambio_aplicada` = taxa efe
            tivamente utilizada, `data_cotacao_utilizada` = data exata ou
             anterior efetivamente utilizada.
      1204      - **Campo `moeda` estruturalmente inválido:** `moeda = nu
            ll`, `taxa_cambio_aplicada = null`, `data_cotacao_utilizada =
             null`.

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 4 lines, removed 3 lines
      1205      - **Moeda válida sem cotação:** `moeda` = código original
             válido (ex.: `"GBP"`), `taxa_cambio_aplicada = null`, `data_
            cotacao_utilizada = null`, `valor_normalizado = null`, `valor
            _reembolsavel = 0.00`, motivo `MOEDA_SEM_COTACAO` com `campo 
            = despesa.moeda` (CA-034) — os campos nulos deste cenário são
             verificados individualmente, não descritos genericamente com
            o "os três nulos", porque o conjunto de campos afetados é mai
            or que o do cenário de moeda estruturalmente inválida (inclui
             também `valor_normalizado` e `valor_reembolsavel`).
      1206  
      1207      **E**, adicionalmente: um cenário em que um campo **não u
            tilizado por RN-020** — `descricao`, `fornecedor`, `categoria
            ` ou `tem_nota_fiscal` — está estruturalmente inválido, enqua
            nto `valor`, `moeda` e `data` são válidos; a taxa, a data da 
            cotação e a conversão continuam sendo resolvidas normalmente 
            nesse item, confirmando que o erro estrutural num campo alhei
            o a RN-020 não interrompe a resolução cambial nem sua propaga
            ção até `ResultadoItem`.
      1208 -  - **Critério de conclusão:** todos os cenários acima verdes
           -, inclusive o de campo não-financeiro inválido; `mvn test` co
           -mpleto verde (a suíte histórica não usa `MOEDA_SEM_COTACAO` n
           -em `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, então a renumer
           -ação de estágios não muda nenhuma ordem já testada).         
      1208 +  - **Critério de conclusão:** todos os cenários acima verdes
           +, inclusive o de campo não-financeiro inválido; nenhum `new R
           +esultadoItem(...)` na suíte usa a assinatura antiga de onze c
           +omponentes (confirmado pelo grep repetido); nenhum construtor
           + de compatibilidade foi criado em `ResultadoItem`; `mvn -q te
           +st` completo verde — inclusive antes de T-049 alterar a seria
           +lização JSON, já que a mudança desta task é inteiramente sobr
           +e o modelo `ResultadoItem` e o `CompositorSaida`, não sobre `
           +EscritorResultado`.                                          
      1209    - **Comando de verificação:**
      1210      ```
      1211 -    mvn -q test "-Dtest=OrdemMotivosTest,SaidaCambioTest"    
      1211 +    git grep "new ResultadoItem(" -- tests/java              
      1212 +    mvn -q test                                              
      1213      ```
      1213 -  - **Commit sugerido:** `feat(T-048): estende CompositorSaid
           -a com campos de cambio e nova ordem de motivos`              
      1214 +  - **Commit sugerido:** `feat(T-048): estende ResultadoItem 
           +com campos de cambio e migra construtores diretos`           
      1215    - **Status:** [ ] pendente
      1216  
      1217  - [ ] **T-049** — Serializar campos de câmbio em `EscritorRes
            ultado` e migrar o fixture histórico para o schema 1.2

● Now item 7 — T-055 and T-056. Let me read the current sections:
  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 14 lines, removed 10 lines
      1362  
      1363  ### Bloco K — Remoção do legado e documentação final
      1364  
      1365 -- [ ] **T-055** — Migrar consumidores restantes de `PoliticaR
           -eembolso` e `AgregadorTetoHospedagem`                        
      1366 -  - **O que faz:** primeiro dos quatro passos exigidos por `C
           -LAUDE.md` §6 antes de remover os dois componentes superados. 
           -A migração é **obrigatória e completa** — não uma escolha cas
           -o a caso: todo teste que hoje consome, direta ou indiretament
           -e, `PoliticaReembolso` (inclusive via as sobrecargas antigas 
           -de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` que o u
           -sam internamente) é migrado para `PoliticaExterna`/`TabelaPol
           -iticaResolvida` (usando a fixture de política histórica de T-
           -050 no lugar de `PoliticaReembolso.padrao()`); todo teste que
           - hoje chama `AgregadorTetoHospedagem.aplicar(...)` diretament
           -e é migrado para `AgregadorTetoIndividual`. Ao final desta ta
           -sk, nenhum teste em `tests/java` referencia `PoliticaReembols
           -o` nem `AgregadorTetoHospedagem` — as únicas ocorrências rest
           -antes em todo o repositório, dentro de `src/main/java`, são o
           -s dois arquivos-alvo e as sobrecargas de compatibilidade que 
           -ainda os chamam internamente (a serem removidas em T-056).   
      1365 +- [ ] **T-055** — Migrar consumidores restantes de `PoliticaR
           +eembolso`, `AgregadorTetoHospedagem` e do construtor de compa
           +tibilidade de `ItemValidado`                                 
      1366 +  - **O que faz:** primeiro dos quatro passos exigidos por `C
           +LAUDE.md` §6 antes de remover os dois componentes superados —
           + **e** a migração final do construtor de dez argumentos de `I
           +temValidado`, criado em T-029 exclusivamente como compatibili
           +dade incremental. A migração de `PoliticaReembolso`/`Agregado
           +rTetoHospedagem` é **obrigatória e completa** — não uma escol
           +ha caso a caso: todo teste que hoje consome, direta ou indire
           +tamente, `PoliticaReembolso` (inclusive via as sobrecargas an
           +tigas de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` q
           +ue o usam internamente) é migrado para `PoliticaExterna`/`Tab
           +elaPoliticaResolvida` (usando a fixture de política histórica
           + de T-050 no lugar de `PoliticaReembolso.padrao()`); todo tes
           +te que hoje chama `AgregadorTetoHospedagem.aplicar(...)` dire
           +tamente é migrado para `AgregadorTetoIndividual`. Da mesma fo
           +rma, `ItemValidado` não pode manter permanentemente o constru
           +tor de dez argumentos: ele preenche `taxaCambioAplicada`/`val
           +orConvertidoBruto` fora de `ResolutorCambio`, o único lugar a
           +utorizado a decidir esses valores (T-037) — por isso, todo te
           +ste que ainda o chama é migrado para o construtor completo de
           + catorze argumentos nesta task. Ao final desta task, nenhum t
           +este em `tests/java` referencia `PoliticaReembolso`, `Agregad
           +orTetoHospedagem`, nem o construtor de dez argumentos de `Ite
           +mValidado` — as únicas ocorrências restantes de `PoliticaReem
           +bolso`/`AgregadorTetoHospedagem` em todo o repositório, dentr
           +o de `src/main/java`, são os dois arquivos-alvo e as sobrecar
           +gas de compatibilidade que ainda os chamam internamente (a se
           +rem removidas em T-056, junto do próprio construtor de dez ar
           +gumentos).                                                   
      1367    - **RN atendidas:** nenhuma nova — é migração de consumidor
            es internos, não regra de negócio.
      1368 -  - **DT/seções do plan:** DT-011 (nota de supersessão); plan
           - §19 ("Substituídos").                                       
      1368 +  - **DT/seções do plan:** DT-011 (nota de supersessão), DT-0
           +14/DT-015 (construtor completo de `ItemValidado`); plan §19 (
           +"Substituídos").                                             
      1369    - **Dependências:** T-041 (sobrecargas novas de `AvaliadorR
            egrasIndividuais`), T-043 (sobrecargas novas de `AgregadorTet
            oDiario`), T-044 (`AgregadorTetoIndividual`), T-050 (fixture 
            de política histórica).
      1370    - **Arquivos que cria/modifica:**
      1371 -    - Todos os arquivos apontados pelo inventário completo de
           - seis comandos (levantado no passo 1 abaixo) — inclui, no mín
           -imo, `TetoHospedagemTest.java` e qualquer teste de T-006 a T-
           -021 que ainda construa `AvaliadorRegrasIndividuais`/`Agregado
           -rTetoDiario` via as sobrecargas históricas.                  
      1371 +    - Todos os arquivos apontados pelo inventário completo de
           + sete comandos (levantado no passo 1 abaixo) — inclui, no mín
           +imo, `TetoHospedagemTest.java`, qualquer teste de T-006 a T-0
           +21 que ainda construa `AvaliadorRegrasIndividuais`/`Agregador
           +TetoDiario` via as sobrecargas históricas, e `tests/java/com/
           +desafio/reembolso/modelo/ItemValidadoCambioTest.java`.       
      1372    - **Passos de implementação:**
      1373      1. Rodar o inventário completo, antes de qualquer migraçã
            o:
      1374         ```
     ...
      1378         git grep "AgregadorTetoHospedagem.aplicar" -- tests/ja
            va
      1379         git grep "PoliticaReembolso" -- tests/java
      1380         git grep "AgregadorTetoHospedagem" -- tests/java
      1381 +       git grep "new ItemValidado(" -- tests/java            
      1382         ```
      1382 -       Para cada resultado: identificar qual sobrecarga está 
           -sendo utilizada; migrar as chamadas das sobrecargas histórica
           -s de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` para 
           -as sobrecargas que recebem `PoliticaExterna`/`TabelaPoliticaR
           -esolvida` (usando a fixture de política histórica de T-050 no
           - lugar de `PoliticaReembolso.padrao()`); preservar sem altera
           -ção as chamadas que já usam as APIs novas; migrar toda chamad
           -a de `AgregadorTetoHospedagem.aplicar(...)` para `AgregadorTe
           -toIndividual.aplicar(...)`.                                  
      1383 -    2. Rodar `mvn test` completo após a migração.            
      1384 -    3. Confirmar que `git grep "PoliticaReembolso" -- tests/j
           -ava` e `git grep "AgregadorTetoHospedagem" -- tests/java` não
           - retornam mais nenhuma ocorrência.                           
      1385 -  - **Testes obrigatórios:** suíte completa (`mvn test`) verd
           -e após a migração; nenhum teste órfão (que testava algo espec
           -ífico de `AgregadorTetoHospedagem` e não tem equivalente em `
           -AgregadorTetoIndividual`) foi perdido silenciosamente.       
      1386 -  - **Critério de conclusão:** `git grep "PoliticaReembolso" 
           --- tests/java` e `git grep "AgregadorTetoHospedagem" -- tests
           -/java` não retornam nenhuma ocorrência; as únicas ocorrências
           - remanescentes em `src/main/java` são os dois arquivos-alvo e
           - as sobrecargas de compatibilidade que os chamam internamente
           -; `mvn test` verde. Registra-se aqui, para orientar T-056: a 
           -remoção das sobrecargas históricas em T-056, seguida por `mvn
           - -q -DskipTests compile` e `mvn -q test`, é a verificação **d
           -efinitiva** de que não ficou nenhum consumidor indireto — o i
           -nventário desta task é a primeira camada de garantia, não a ú
           -nica.                                                        
      1383 +       Para cada resultado dos seis primeiros comandos: ident
           +ificar qual sobrecarga está sendo utilizada; migrar as chamad
           +as das sobrecargas históricas de `AvaliadorRegrasIndividuais`
           +/`AgregadorTetoDiario` para as sobrecargas que recebem `Polit
           +icaExterna`/`TabelaPoliticaResolvida` (usando a fixture de po
           +lítica histórica de T-050 no lugar de `PoliticaReembolso.padr
           +ao()`); preservar sem alteração as chamadas que já usam as AP
           +Is novas; migrar toda chamada de `AgregadorTetoHospedagem.apl
           +icar(...)` para `AgregadorTetoIndividual.aplicar(...)`.      
      1384 +    2. Para cada resultado de `git grep "new ItemValidado(" -
           +- tests/java`: identificar se a chamada usa o construtor anti
           +go de dez argumentos; migrar para o construtor completo de ca
           +torze. Em testes que representam uma etapa **posterior** ao c
           +âmbio no pipeline (ex.: entrada já resolvida para `Normalizad
           +or`/`AvaliadorRegrasIndividuais`), preencher explicitamente `
           +moeda`/`taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConv
           +ertidoBruto` conforme o cenário. Em testes que representam um
           +a etapa **anterior** ao câmbio (ex.: saída de `ValidadorItem`
           +/`DetectorIdDuplicado` isolada), preencher `moeda` conforme o
           + cenário do teste e deixar os três campos derivados `null` — 
           +o mesmo estado que `ValidadorItem` produziria depois de T-036
           +.                                                            
      1385 +    3. Atualizar `ItemValidadoCambioTest` (T-029) para não ex
           +igir mais a **permanência** do construtor de dez argumentos —
           + os casos que hoje provam a delegação de compatibilidade são 
           +removidos ou reescritos para provar diretamente o comportamen
           +to do construtor de catorze argumentos, já que a compatibilid
           +ade deixa de existir a partir de T-056.                      
      1386 +    4. Rodar `mvn test` completo após a migração.            
      1387 +    5. Confirmar que `git grep "PoliticaReembolso" -- tests/j
           +ava`, `git grep "AgregadorTetoHospedagem" -- tests/java` e `g
           +it grep "new ItemValidado(" -- tests/java` (restrito ao const
           +rutor de dez argumentos) não retornam mais nenhuma ocorrência
           + do padrão antigo.                                           
      1388 +  - **Testes obrigatórios:** suíte completa (`mvn test`) verd
           +e após a migração; nenhum teste órfão (que testava algo espec
           +ífico de `AgregadorTetoHospedagem` e não tem equivalente em `
           +AgregadorTetoIndividual`) foi perdido silenciosamente; nenhum
           + teste continua dependendo do construtor de dez argumentos de
           + `ItemValidado`.                                             
      1389 +  - **Critério de conclusão:** `git grep "PoliticaReembolso" 
           +-- tests/java` e `git grep "AgregadorTetoHospedagem" -- tests
           +/java` não retornam nenhuma ocorrência; nenhum teste depende 
           +mais do construtor de dez argumentos de `ItemValidado`; as ún
           +icas ocorrências remanescentes de `PoliticaReembolso`/`Agrega
           +dorTetoHospedagem` em `src/main/java` são os dois arquivos-al
           +vo e as sobrecargas de compatibilidade que os chamam internam
           +ente; `mvn test` verde. Registra-se aqui, para orientar T-056
           +: a remoção das sobrecargas históricas e do construtor de dez
           + argumentos em T-056, seguida por `mvn -q -DskipTests compile
           +` e `mvn -q test`, é a verificação **definitiva** de que não 
           +ficou nenhum consumidor indireto — o inventário desta task é 
           +a primeira camada de garantia, não a única.                  
      1390    - **Comando de verificação:**
      1391      ```
      1392      git grep "AvaliadorRegrasIndividuais.avaliar" -- tests/ja
            va
     ...
      1395      git grep "AgregadorTetoHospedagem.aplicar" -- tests/java
      1396      git grep "PoliticaReembolso" -- tests/java
      1397      git grep "AgregadorTetoHospedagem" -- tests/java
      1398 +    git grep "new ItemValidado(" -- tests/java               
      1399      mvn -q test
      1400      ```
      1397 -  - **Commit sugerido:** `refactor(T-055): migra todos os con
           -sumidores de PoliticaReembolso e AgregadorTetoHospedagem`    
      1401 +  - **Commit sugerido:** `refactor(T-055): migra todos os con
           +sumidores de PoliticaReembolso, AgregadorTetoHospedagem e do 
           +construtor de compatibilidade de ItemValidado`               
      1402    - **Status:** [ ] pendente
      1403  
      1404  - [ ] **T-056** — Remover `PoliticaReembolso` e `AgregadorTet
            oHospedagem`

● Now T-056:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 11 lines, removed 8 lines
      1401    - **Commit sugerido:** `refactor(T-055): migra todos os con
            sumidores de PoliticaReembolso, AgregadorTetoHospedagem e do 
            construtor de compatibilidade de ItemValidado`
      1402    - **Status:** [ ] pendente
      1403  
      1404 -- [ ] **T-056** — Remover `PoliticaReembolso` e `AgregadorTet
           -oHospedagem`                                                 
      1405 -  - **O que faz:** segundo, terceiro e quarto passos de `CLAU
           -DE.md` §6: com todos os consumidores já migrados (T-055), rem
           -ove `src/main/java/com/desafio/reembolso/modelo/PoliticaReemb
           -olso.java` e `src/main/java/com/desafio/reembolso/pipeline/Ag
           -regadorTetoHospedagem.java`, e **obrigatoriamente** também to
           -das as sobrecargas e imports que dependem deles — nenhuma sob
           -recarga de compatibilidade que dependa de uma classe removida
           - é mantida: as sobrecargas antigas de `AvaliadorRegrasIndivid
           -uais.avaliar(item)`/`avaliar(item, envelope)` e `AgregadorTet
           -oDiario.aplicar(itens)` são removidas nesta task, porque T-05
           -5 já garantiu que nenhum teste as usa mais.                  
      1404 +- [ ] **T-056** — Remover `PoliticaReembolso`, `AgregadorTeto
           +Hospedagem` e o construtor de compatibilidade de `ItemValidad
           +o`                                                           
      1405 +  - **O que faz:** segundo, terceiro e quarto passos de `CLAU
           +DE.md` §6: com todos os consumidores já migrados (T-055), rem
           +ove `src/main/java/com/desafio/reembolso/modelo/PoliticaReemb
           +olso.java` e `src/main/java/com/desafio/reembolso/pipeline/Ag
           +regadorTetoHospedagem.java`, e **obrigatoriamente** também to
           +das as sobrecargas e imports que dependem deles — nenhuma sob
           +recarga de compatibilidade que dependa de uma classe removida
           + é mantida: as sobrecargas antigas de `AvaliadorRegrasIndivid
           +uais.avaliar(item)`/`avaliar(item, envelope)` e `AgregadorTet
           +oDiario.aplicar(itens)` são removidas nesta task, porque T-05
           +5 já garantiu que nenhum teste as usa mais. Nesta mesma task,
           + remove-se também o construtor de dez argumentos de `ItemVali
           +dado` (criado em T-029, exclusivamente como compatibilidade i
           +ncremental) e qualquer delegação criada só para sustentá-lo —
           + `ItemValidado` passa a ter um único construtor, o completo d
           +e catorze argumentos, porque T-055 já garantiu que nenhum tes
           +te depende mais do antigo.                                   
      1406    - **RN atendidas:** nenhuma nova — remoção de código supera
            do (DT-007 → DT-011).
      1407 -  - **DT/seções do plan:** DT-007 (supersessão), DT-011; plan
           - §19.                                                        
      1407 +  - **DT/seções do plan:** DT-007 (supersessão), DT-011, DT-0
           +14 (compatibilidade de `ItemValidado` encerrada); plan §19.  
      1408    - **Dependências:** T-055.
      1409    - **Arquivos que cria/modifica:**
      1410      - Remove `src/main/java/com/desafio/reembolso/modelo/Poli
            ticaReembolso.java`
      1411      - Remove `src/main/java/com/desafio/reembolso/pipeline/Ag
            regadorTetoHospedagem.java`
      1412      - Remove as sobrecargas de compatibilidade de `AvaliadorR
            egrasIndividuais` e `AgregadorTetoDiario` que dependiam exclu
            sivamente de `PoliticaReembolso`, e qualquer import órfão res
            ultante.
      1413 +    - `src/main/java/com/desafio/reembolso/modelo/ItemValidad
           +o.java` — remove o construtor de dez argumentos criado em T-0
           +29.                                                          
      1414    - **Passos de implementação:**
      1415      1. Rodar `git grep "PoliticaReembolso" -- src/main/java t
            ests/java` e `git grep "AgregadorTetoHospedagem" -- src/main/
            java tests/java` — confirmar que a única ocorrência restante 
            de cada um é o próprio arquivo-alvo (mais, no caso de `Politi
            caReembolso`, as sobrecargas que serão removidas neste mesmo 
            passo).
      1416      2. Remover os dois arquivos.
      1417      3. Remover as sobrecargas de compatibilidade que dependia
            m deles e qualquer import não utilizado resultante — nenhuma 
            sobrecarga dependente de uma classe removida permanece.
      1417 -    4. Rodar `mvn -q -DskipTests compile` para confirmar que 
           -a remoção não quebra a compilação.                           
      1418 -    5. Rodar `mvn -q test` completo.                         
      1419 -    6. Rodar novamente `git grep "PoliticaReembolso" -- src/m
           -ain/java tests/java` e `git grep "AgregadorTetoHospedagem" --
           - src/main/java tests/java` — ambos devem retornar **zero** oc
           -orrências.                                                   
      1418 +    4. Remover o construtor de dez argumentos de `ItemValidad
           +o.java` — a classe passa a expor exclusivamente o construtor 
           +de catorze argumentos.                                       
      1419 +    5. Rodar `mvn -q -DskipTests compile` para confirmar que 
           +a remoção não quebra a compilação.                           
      1420 +    6. Rodar `mvn -q test` completo.                         
      1421 +    7. Rodar novamente `git grep "PoliticaReembolso" -- src/m
           +ain/java tests/java`, `git grep "AgregadorTetoHospedagem" -- 
           +src/main/java tests/java` e `git grep "new ItemValidado(" -- 
           +src/main/java tests/java` (conferir manualmente que todas as 
           +ocorrências restantes usam catorze argumentos) — os dois prim
           +eiros devem retornar **zero** ocorrências.                   
      1422    - **Testes obrigatórios:** nenhum teste novo — a garantia é
             a compilação limpa e a suíte inteira verde após a remoção.
      1421 -  - **Critério de conclusão:** `git grep "PoliticaReembolso" 
           --- src/main/java tests/java` e `git grep "AgregadorTetoHosped
           -agem" -- src/main/java tests/java` não retornam nenhuma ocorr
           -ência; `mvn -q -DskipTests compile` e `mvn -q test` verdes. O
           -s greps são sempre escopados a `src/main/java tests/java` — n
           -unca executados sem pathspec, porque `plan.md`, `tasks.md`, `
           -DECISIONS.md` e os exports de sessão preservam referências hi
           -stóricas legítimas às duas classes, que não são defeito.     
      1423 +  - **Critério de conclusão:** `git grep "PoliticaReembolso" 
           +-- src/main/java tests/java` e `git grep "AgregadorTetoHosped
           +agem" -- src/main/java tests/java` não retornam nenhuma ocorr
           +ência; `ItemValidado.java` tem um único construtor; `mvn -q -
           +DskipTests compile` e `mvn -q test` verdes — a compilação e a
           + suíte completa após a remoção são a prova definitiva de que 
           +não restou consumidor indireto de nenhum dos três componentes
           + removidos. Os greps são sempre escopados a `src/main/java te
           +sts/java` — nunca executados sem pathspec, porque `plan.md`, 
           +`tasks.md`, `DECISIONS.md` e os exports de sessão preservam r
           +eferências históricas legítimas a essas classes, que não são 
           +defeito.                                                     
      1424    - **Comando de verificação:**
      1425      ```
      1426      git grep "PoliticaReembolso" -- src/main/java tests/java
      1427      git grep "AgregadorTetoHospedagem" -- src/main/java tests
            /java
      1428 +    git grep "new ItemValidado(" -- src/main/java tests/java 
      1429      mvn -q -DskipTests compile
      1430      mvn -q test
      1431      ```
      1429 -  - **Commit sugerido:** `refactor(T-056): remove PoliticaRee
           -mbolso, AgregadorTetoHospedagem e sobrecargas dependentes`   
      1432 +  - **Commit sugerido:** `refactor(T-056): remove PoliticaRee
           +mbolso, AgregadorTetoHospedagem, sobrecargas dependentes e o 
           +construtor de compatibilidade de ItemValidado`               
      1433    - **Status:** [ ] pendente
      1434  
      1435  - [ ] **T-057** — README com CLI de quatro flags e documentaç
            ão de política/câmbio

● Now item 8 — matrices. Let me find the relevant rows:

  Ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      1555  
      1556  | Regra | Task(s) dona(s) | Teste(s) |
      1557  |---|---|---|
      1558 -| RN-019 | T-025, T-026, T-028 (modelos) · T-040 (`ResolutorP
           -oliticaCentroCusto`) · T-041, T-042 (avaliador + wiring) · T-
           -043 a T-046 (tetos por periodicidade) | `TabelaCategoriaTest`
           -, `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentroCust
           -oTest`, `CategoriaCentroCustoTest`, `TetoPorPeriodicidadeTest
           -`, `AgregadorTetoIndividualTest` |                           
      1559 -| RN-020 | T-027 (`TabelaCambio`) · T-032, T-033 (`LeitorCamb
           -io`) · T-036 (campo `moeda`) · T-037, T-038, T-039 (`Resoluto
           -rCambio` + integração) · T-041 (coexistência com `CATEGORIA_N
           -AO_REEMBOLSAVEL_CENTRO_CUSTO`) | `TabelaCambioTest`, `LeitorC
           -ambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`, `Convers
           -aoCambialIntegracaoTest`, `MoedaSemCotacaoTest`, `CategoriaCe
           -ntroCustoTest` |                                             
      1558 +| RN-019 | T-025, T-026, T-028 (modelos) · T-040 (`ResolutorP
           +oliticaCentroCusto`) · T-041, T-042 (avaliador + wiring) · T-
           +043 a T-046 (tetos por periodicidade) | `TabelaCategoriaTest`
           +, `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentroCust
           +oTest`, `CategoriaCentroCustoTest`, `TetoPorPeriodicidadeTest
           +`, `AgregadorTetoIndividualTest`, `CliContratoTest` |        
      1559 +| RN-020 | T-027 (`TabelaCambio`) · T-029 (campos de câmbio e
           +m `ItemValidado`) · T-032, T-033 (`LeitorCambio`) · T-036 (ca
           +mpo `moeda`) · T-037, T-038, T-039 (`ResolutorCambio` + integ
           +ração) · T-041 (coexistência com `CATEGORIA_NAO_REEMBOLSAVEL_
           +CENTRO_CUSTO`) | `TabelaCambioTest`, `ItemValidadoCambioTest`
           +, `LeitorCambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`
           +, `ConversaoCambialIntegracaoTest`, `MoedaSemCotacaoTest`, `C
           +ategoriaCentroCustoTest` |                                   
      1560  | RN-021 | T-026 (`PoliticaExterna.vigencia`) · T-030 (valida
            ção de `vigencia` no `LeitorPolitica`) | `PoliticaExternaTest
            `, `LeitorPoliticaTest` |
      1561  | RN-022 | T-030, T-031 (`LeitorPolitica`) · T-032, T-033 (`L
            eitorCambio`) · T-035 (CLI trata falha como exit `2`) | `Leit
            orPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |
      1562  

● Now DT-017:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1603  | DT-014 | T-029, T-036 | `ItemValidadoCambioTest`, `CampoMoe
            daTest` |
      1604  | DT-015 | T-037, T-038 | `ResolucaoCambioTest` (produto sem 
            arredondar), `ConversaoCambialIntegracaoTest` (canário `1.005
             × 1.005`) |
      1605  | DT-016 | T-040 | `ResolutorPoliticaCentroCustoTest` (compar
            ação textual exata) |
      1606 -| DT-017 | T-043, T-044, T-045 | `TetoPorPeriodicidadeTest`, 
           -`AgregadorTetoIndividualTest` |                              
      1606 +| DT-017 | T-043, T-044, T-045, T-046 | `TetoPorPeriodicidade
           +Test`, `AgregadorTetoIndividualTest`, `CliContratoTest` |    
      1607  | DT-018 | T-034 | `CliContratoTest` |
      1608  | DT-019 | T-022, T-023, T-024, T-048 | `VocabularioMotivoTes
            t`, `OrdemMotivosTest` |
      1609  

● Now let's run the item 9 textual verification checklist:

  Ran 1 shell command (ctrl+o to expand)

● All checks pass. Now the mandated git verification:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      943      - `tests/java/com/desafio/reembolso/pipeline/TetoDiarioTes
           t.java`
      944      - `tests/java/com/desafio/reembolso/pipeline/TetoHospedage
           mTest.java`
      945      - `tests/java/com/desafio/reembolso/pipeline/ValorNaoPosit
           ivoTest.java`
      946 -                                                              
      946 +                                                              
      947      (a lista acima reflete o estado atual do repositório nesta
            sessão de planejamento; o inventário do passo 5 abaixo, execu
           tado dentro da própria task, é a fonte da verdade final sobre 
           quais arquivos precisam de fato ser tocados.)
      948    - **Passos de implementação:**
      949      1. Em `Normalizador.normalizarValor(...)`, trocar a leitur
           a de `item.getValor()` por `item.getValorConvertidoBruto()` co
           mo entrada do `setScale(2, RoundingMode.HALF_UP)` — único pont
           o de arredondamento de todo o pipeline (DT-015). Nenhum `if`/f
           allback que leia `item.getValor()` diretamente permanece no mé
           todo.

  Ran 1 shell command (ctrl+o to expand)

● Clean now. Continuing the mandated verification:

  Ran 1 shell command (ctrl+o to expand)

● All confirmed empty except tasks.md. Now let's verify T-001–T-021 remain
  byte-for-byte intact and T-022–T-058 stay continuous/pending:

  Ran 2 shell commands (ctrl+o to expand)

● All confirmed: 58 tasks total, no gaps (T-001 to T-058), 21 done, 37 pending,
  all preserved metadata. Here's the summary:

  1. Mapas de ordenação atualizados imediatamente:
  CompositorSaida.criarOrdemCampo() passa a ser atualizado em T-022 (junto com a
  criação de CampoCanonico.MOEDA), e CompositorSaida.criarEstagios() passa a
  ser atualizado em T-023 (junto com os três novos MotivoCodigo) — ambos com a
  tabela final definitiva, não mais provisórios até T-048. OrdemMotivosTest
  (T-016) roda como regressão em ambas.

  2. Consumidores de Main.run que entram em T-034: além de CliContratoTest,
  agora EscritaAtomicaSaidaTest e ExemploCompletoTest — migrados via inventário
  git grep "Main.run" -- tests/java para fornecer as quatro flags (usando
  exemplos/envelope/politica-v4.json/cambio.json), exceto os cenários que
  intencionalmente testam flag ausente/comando antigo. Comando de verificação
  passa a mvn -q test (suíte completa).

  3. Como T-038 migra os pipelines históricos: inventário git grep 
  "Normalizador.normalizar" -- tests/java/com/desafio/reembolso/pipeline; cria o
  helper CambioTesteSupport.java (package-private, TabelaCambio BRL vazia, sem
  regra de produção); migra o encadeamento ValidadorItem → DetectorIdDuplicado 
  (quando aplicável) → ResolutorCambio → Normalizador em 16 arquivos de teste
  listados explicitamente; confirma por grep repetido que nenhum fallback para
  getValor() foi introduzido no Normalizador.

  4. Estratégia fechada de arquivos temporários em T-046: CliContratoTest usa
  @TempDir para política, envelope e --output dos dois cenários novos
  (representacao/"dia" e estacionamento/"diaria"); --cambio usa
  exemplos/envelope/cambio.json; nenhum fixture permanente novo em
  tests/resources; execução sempre por Main.run(...) — nunca "através do
  binário" (isso continua exclusivo de T-054).

  5. Consumidores diretos de ResultadoItem migrados em T-048: inventário git 
  grep "new ResultadoItem(" -- tests/java; EscritorResultadoTest.java e
  TotalPeriodoTest.java acrescentados aos arquivos previstos; todos migrados
  para catorze componentes (BRL/1/null nos cenários sintéticos históricos);
  nenhum construtor de compatibilidade criado.

  6. Onde o construtor temporário de ItemValidado é removido: migração final em
  T-055 (inventário git grep "new ItemValidado(" -- tests/java,
  ItemValidadoCambioTest atualizado) e remoção definitiva do construtor de dez
  argumentos em T-056, com ItemValidado.java acrescentado aos arquivos
  modificados.

  7. Correções nas matrizes: RN-019 (tabela detalhada) ganhou CliContratoTest;
  RN-020 (tabela detalhada) ganhou T-029/ItemValidadoCambioTest; DT-017 ganhou
  T-046/CliContratoTest. Nenhum RN/CA/DT foi criado ou removido.

  8. git status --short:
   M specs/001-motor-reembolso/tasks.md

  Somente tasks.md foi alterado; T-001 a T-021 permanecem byte a byte intactas
  (confirmado pelos limites dos hunks do diff); T-022 a T-058 continuam
  contínuas e pendentes; nenhuma task foi criada, removida ou renumerada; nenhum
  código, teste ou fixture foi alterado; nenhum /export, git add, commit ou
  push foi executado. Parado para revisão.

✻ Worked for 8m 44s

※ recap: We've been iteratively refining the Dia 2 (política v4) task backlog in tasks.md through several rounds of surgical corrections. All requested fixes are applied and verified; next step is your review before any implementation begins.

> Faça uma correção final, pequena e estritamente localizada em:                
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Altere exclusivamente esse arquivo.                                           
                                                                                
  Não altere:                                                                   
                                                                                
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - README.md;                                                                  
  - código;                                                                     
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Preserve:                                                                     
                                                                                
  - versão 1.1;                                                                 
  - status em revisão;                                                          
  - data 2026-08-05;                                                            
  - spec 1.2 aprovada;                                                          
  - plan 1.1 aprovado;                                                          
  - T-001 a T-021 byte a byte intactas;                                         
  - T-022 a T-058 contínuas;                                                    
  - 58 tasks no total;                                                          
  - 21 concluídas e 37 pendentes;                                               
  - todas as correções já realizadas;                                           
  - regra de exatamente um commit por task do Dia 2.                            
                                                                                
  Não crie, remova ou renumere tasks.                                           
                                                                                
  Não execute /export, git add, commit ou push.                                 
                                                                                
  ## 1. Corrigir a aridade de ResultadoItem em T-048                            
                                                                                
  O ResultadoItem atual possui sete componentes:                                
                                                                                
  1. indiceEntrada;                                                             
  2. id;                                                                        
  3. valorInformado;                                                            
  4. valorNormalizado;                                                          
  5. valorReembolsavel;                                                         
  6. decisao;                                                                   
  7. motivos.                                                                   
                                                                                
  T-048 acrescenta exatamente três:                                             
                                                                                
  8. moeda;                                                                     
  9. taxaCambioAplicada;                                                        
  10. dataCotacaoUtilizada.                                                     
                                                                                
  Portanto, a assinatura muda:                                                  
                                                                                
  de sete para dez componentes                                                  
                                                                                
  e nunca:                                                                      
                                                                                
  de onze para catorze componentes.                                             
                                                                                
  Corrija todas as ocorrências dentro de T-048:                                 
                                                                                
  - “onze para catorze componentes” → “sete para dez componentes”;              
  - “assinatura nova de catorze componentes” → “assinatura nova de dez          
  componentes”;                                                                 
  - “ResultadoItem de catorze componentes” → “ResultadoItem de dez              
  componentes”;                                                                 
  - “assinatura antiga de onze componentes” → “assinatura antiga de sete        
  componentes”;                                                                 
  - qualquer outra contagem incorreta relacionada a ResultadoItem.              
                                                                                
  Não altere as referências ao ItemValidado de catorze argumentos. Essas estão  
  corretas e pertencem a outro modelo.                                          
                                                                                
  ## 2. Corrigir a explicação sobre construtores de record                      
                                                                                
  Remova a afirmação de que um record não admite construtor adicional de        
  compatibilidade.                                                              
                                                                                
  Registre corretamente:                                                        
                                                                                
  - um record Java pode declarar construtores adicionais, desde que deleguem ao 
  construtor canônico;                                                          
  - nesta versão não será criado construtor adicional de sete argumentos para   
  ResultadoItem;                                                                
  - essa é uma decisão deliberada de migração imediata e contrato único;        
  - todos os consumidores diretos serão migrados no mesmo commit de T-048;      
  - não é uma limitação da linguagem Java.                                      
                                                                                
  O critério continua exigindo:                                                 
                                                                                
  - nenhum construtor de compatibilidade de sete argumentos;                    
  - todos os new ResultadoItem(...) usando os dez componentes;                  
  - mvn test completo verde.                                                    
                                                                                
  ## 3. Acrescentar o caso null explícito em T-030                              
                                                                                
  O campo observacao da configuração de categoria é opcional, mas, quando       
  presente, deve ser texto.                                                     
                                                                                
  Acrescente explicitamente:                                                    
                                                                                
  observacao presente com null → política estruturalmente inválida              
                                                                                
  Atualize os testes obrigatórios e o critério de conclusão para sete           
  situações:                                                                    
                                                                                
  1. ausente → válido;                                                          
  2. texto → válido e descartado;                                               
  3. null explícito → inválido;                                                 
  4. número → inválido;                                                         
  5. booleano → inválido;                                                       
  6. lista → inválido;                                                          
  7. objeto → inválido.                                                         
                                                                                
  Não mova esses testes para T-031.                                             
                                                                                
  ## 4. Acrescentar null e objeto em T-033                                      
                                                                                
  Para fonte e observacao do arquivo de câmbio:                                 
                                                                                
  - ausentes → válido;                                                          
  - presentes como texto → válido e descartado;                                 
  - presentes com null explícito → câmbio inválido;                             
  - presentes como número → inválido;                                           
  - presentes como booleano → inválido;                                         
  - presentes como lista → inválido;                                            
  - presentes como objeto → inválido.                                           
                                                                                
  Atualize os testes obrigatórios e o critério de conclusão de T-033.           
                                                                                
  A implementação normativa continua pertencendo a T-032; T-033 apenas amplia a 
  prova de fronteira.                                                           
                                                                                
  ## 5. Acrescentar CA atendidos em T-055 a T-058                               
                                                                                
  Cada task deve manter explicitamente RN, CA e DT/seções.                      
                                                                                
  Acrescente:                                                                   
                                                                                
  ### T-055                                                                     
                                                                                
  **CA atendidos:** nenhum novo — migração de consumidores internos sem         
  alteração de comportamento normativo.                                         
                                                                                
  ### T-056                                                                     
                                                                                
  **CA atendidos:** nenhum novo — remoção controlada de componentes superados   
  após a migração.                                                              
                                                                                
  ### T-057                                                                     
                                                                                
  **CA atendidos:** nenhum novo — documentação dos comportamentos já            
  comprovados por CA-037 a CA-044.                                              
                                                                                
  ### T-058                                                                     
                                                                                
  **CA atendidos:** nenhum novo — auditoria final da cobertura de CA-024 a      
  CA-049.                                                                       
                                                                                
  Posicione cada linha entre RN atendidas e DT/seções do plan.                  
                                                                                
  ## 6. Corrigir as duas matrizes de RN-019                                     
                                                                                
  A task T-026 cria PoliticaExterna e PoliticaExternaTest.                      
                                                                                
  Portanto, nas duas linhas de RN-019:                                          
                                                                                
  - “Cobertura — RN-019 a RN-022”;                                              
  - “Matriz final — RN/CA do Dia 2”;                                            
                                                                                
  acrescente:                                                                   
                                                                                
  PoliticaExternaTest                                                           
                                                                                
  à coluna de testes.                                                           
                                                                                
  Preserve todos os outros testes já listados.                                  
                                                                                
  A lista final de testes de RN-019 deve conter:                                
                                                                                
  - TabelaCategoriaTest;                                                        
  - PoliticaExternaTest;                                                        
  - TabelaPoliticaResolvidaTest;                                                
  - ResolutorPoliticaCentroCustoTest;                                           
  - CategoriaCentroCustoTest;                                                   
  - TetoPorPeriodicidadeTest;                                                   
  - AgregadorTetoIndividualTest;                                                
  - CliContratoTest.                                                            
                                                                                
  ## 7. Verificação textual                                                     
                                                                                
  Confirme que não resta dentro de T-048:                                       
                                                                                
  - onze para catorze componentes;                                              
  - assinatura nova de catorze componentes;                                     
  - ResultadoItem de catorze componentes;                                       
  - assinatura antiga de onze componentes;                                      
  - afirmação de que record não admite construtor adicional.                    
                                                                                
  Confirme também:                                                              
                                                                                
  - T-030 contém observacao null explícito → inválido;                          
  - T-033 contém fonte/observacao null e objeto → inválidos;                    
  - T-055, T-056, T-057 e T-058 possuem RN, CA e DT/seções;                     
  - PoliticaExternaTest aparece nas duas matrizes de RN-019;                    
  - nenhuma contagem referente ao ItemValidado de catorze argumentos foi        
  alterada.                                                                     
                                                                                
  Execute:                                                                      
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- README.md                                                         
  git diff -- src                                                               
  git diff -- tests                                                             
                                                                                
  Confirme:                                                                     
                                                                                
  - somente tasks.md foi alterado;                                              
  - T-001 a T-021 permanecem byte a byte intactas;                              
  - T-022 a T-058 continuam contínuas e pendentes;                              
  - nenhuma task foi criada, removida ou renumerada;                            
  - nenhum código, teste ou fixture foi alterado;                               
  - nenhum /export, git add, commit ou push foi executado.                      
                                                                                
  Ao final, informe:                                                            
                                                                                
  1. contagem correta dos componentes de ResultadoItem;                         
  2. nova justificativa para não criar construtor de compatibilidade;           
  3. casos null acrescentados em T-030 e T-033;                                 
  4. linhas CA acrescentadas em T-055 a T-058;                                  
  5. correção das duas matrizes de RN-019;                                      
  6. git status --short.                                                        
                                                                                
  Pare para revisão.                                                            

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Now let me fix all five occurrences in T-048 with precise edits.
● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1182    - **Status:** [ ] pendente
      1183  
      1184  - [ ] **T-048** — Estender `ResultadoItem` com campos de câmb
            io, migrar construtores diretos e consolidar a ordem final do
             `CompositorSaida`
      1185 -  - **O que faz:** `ResultadoItem` ganha três campos (`moeda`
           -, `taxaCambioAplicada`, `dataCotacaoUtilizada`), populados se
           -m recálculo a partir do `ItemValidado` de cada posição, dentr
           -o de `componentesDoRegistro(...)`. `ORDEM_CAMPO` e `ESTAGIO_P
           -OR_CODIGO` **já foram atualizados** para a ordem final desde 
           -T-022/T-023 respectivamente — esta task não os introduz pela 
           -primeira vez; ela só **revisa e amplia** `OrdemMotivosTest` p
           -ara comprovar, com os motivos novos já em uso desde os blocos
           - F/G/H, que a tabela completa de 8.3 está correta de ponta a 
           -ponta. Como `ResultadoItem` é um `record` cuja assinatura mud
           -a de onze para catorze componentes, **todo** `new ResultadoIt
           -em(...)` já existente na suíte precisa ser migrado no mesmo c
           -ommit — não se cria um construtor de compatibilidade de onze 
           -argumentos: um `record` não admite sobrecarga "antiga" que ig
           -nore campos do jeito que uma classe comum admitiria, e introd
           -uzir uma fábrica de compatibilidade só para adiar a migração 
           -contrariaria a própria regra de um commit coerente por task. 
      1185 +  - **O que faz:** `ResultadoItem` ganha três campos (`moeda`
           +, `taxaCambioAplicada`, `dataCotacaoUtilizada`), populados se
           +m recálculo a partir do `ItemValidado` de cada posição, dentr
           +o de `componentesDoRegistro(...)`. `ORDEM_CAMPO` e `ESTAGIO_P
           +OR_CODIGO` **já foram atualizados** para a ordem final desde 
           +T-022/T-023 respectivamente — esta task não os introduz pela 
           +primeira vez; ela só **revisa e amplia** `OrdemMotivosTest` p
           +ara comprovar, com os motivos novos já em uso desde os blocos
           + F/G/H, que a tabela completa de 8.3 está correta de ponta a 
           +ponta. Como `ResultadoItem` é um `record` cuja assinatura mud
           +a de sete para dez componentes, **todo** `new ResultadoItem(.
           +..)` já existente na suíte precisa ser migrado no mesmo commi
           +t. Um `record` Java pode, tecnicamente, declarar construtores
           + adicionais que deleguem ao construtor canônico — não se trat
           +a de uma limitação da linguagem —, mas esta task **decide del
           +iberadamente não criar** um construtor de compatibilidade de 
           +sete argumentos para `ResultadoItem`: é uma escolha de migraç
           +ão imediata e contrato único, não uma restrição técnica, e po
           +r isso todos os consumidores diretos são migrados no mesmo co
           +mmit desta task.                                             
      1186    - **RN atendidas:** RN-017 (atualizada).
      1187    - **CA atendidos:** CA-034.
      1188    - **DT/seções do plan:** DT-019; plan §10.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1195      - `tests/java/com/desafio/reembolso/pipeline/TotalPeriodo
            Test.java`
      1196    - **Passos de implementação:**
      1197      1. Adicionar os três campos ao `record ResultadoItem`, co
            m construção populada em `componentesDoRegistro(...)` a parti
            r de `itemValidado.getMoeda()`/`getTaxaCambioAplicada()`/`get
            DataCotacaoUtilizada()` — nos três pontos de retorno do métod
            o (recusado antes da duplicidade, recusado por duplicidade, r
            esultado de teto).
      1198 -    2. Rodar o inventário `git grep "new ResultadoItem(" -- t
           -ests/java` e migrar **todos** os construtores diretos para a 
           -assinatura nova (catorze componentes) — inclui, no mínimo, `E
           -scritorResultadoTest.java` e `TotalPeriodoTest.java`. Nos cen
           -ários históricos sintéticos em que os campos de câmbio não sã
           -o o objeto do teste, preencher explicitamente `moeda = "BRL"`
           -, `taxaCambioAplicada = BigDecimal.ONE`, `dataCotacaoUtilizad
           -a = null` — nunca deixar um construtor de compatibilidade de 
           -sete (ou onze) argumentos em `ResultadoItem` para adiar essa 
           -migração.                                                    
      1198 +    2. Rodar o inventário `git grep "new ResultadoItem(" -- t
           +ests/java` e migrar **todos** os construtores diretos para a 
           +assinatura nova (dez componentes) — inclui, no mínimo, `Escri
           +torResultadoTest.java` e `TotalPeriodoTest.java`. Nos cenário
           +s históricos sintéticos em que os campos de câmbio não são o 
           +objeto do teste, preencher explicitamente `moeda = "BRL"`, `t
           +axaCambioAplicada = BigDecimal.ONE`, `dataCotacaoUtilizada = 
           +null` — nunca deixar um construtor de compatibilidade de sete
           + argumentos em `ResultadoItem` para adiar essa migração.     
      1199      3. Confirmar, por leitura, que `criarOrdemCampo()` e `cri
            arEstagios()` já refletem a tabela final (herdada de T-022/T-
            023) — nenhuma alteração nesses dois métodos é esperada nesta
             task, salvo se a conferência revelar uma divergência real, c
            aso em que a correção entra no mesmo commit.
      1200      4. Ampliar `OrdemMotivosTest` para exercitar, com os moti
            vos e o `ResultadoItem` de catorze componentes já disponíveis
            , a tabela completa de 8.3 de ponta a ponta — não apenas os s
            ete campos/motivos históricos.
      1201    - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) —
             um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` 
            simultâneos aparece nessa ordem; um item com erro estrutural 
            em `despesa.moeda` aparece na posição correta da ordem canôni
            ca de campo (entre `valor` e `tem_nota_fiscal`); a tabela com
            pleta de estágios (0 a 10) é exercitada com pelo menos um mot
            ivo de cada estágio. `EscritorResultadoTest` e `TotalPeriodoT
            est` — migrados para a assinatura de catorze componentes de `
            ResultadoItem`, continuam verdes sem mudança de comportamento
             observável (a serialização dos três campos novos só é implem
            entada em T-049; nesta task, `EscritorResultadoTest` ainda te
            sta exclusivamente o que já testava antes, só com o construto
            r atualizado). `SaidaCambioTest` — os quatro formatos fechado
            s de `moeda`/`taxa_cambio_aplicada`/`data_cotacao_utilizada` 
            no `ResultadoItem`:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      1197      1. Adicionar os três campos ao `record ResultadoItem`, co
            m construção populada em `componentesDoRegistro(...)` a parti
            r de `itemValidado.getMoeda()`/`getTaxaCambioAplicada()`/`get
            DataCotacaoUtilizada()` — nos três pontos de retorno do métod
            o (recusado antes da duplicidade, recusado por duplicidade, r
            esultado de teto).
      1198      2. Rodar o inventário `git grep "new ResultadoItem(" -- t
            ests/java` e migrar **todos** os construtores diretos para a 
            assinatura nova (dez componentes) — inclui, no mínimo, `Escri
            torResultadoTest.java` e `TotalPeriodoTest.java`. Nos cenário
            s históricos sintéticos em que os campos de câmbio não são o 
            objeto do teste, preencher explicitamente `moeda = "BRL"`, `t
            axaCambioAplicada = BigDecimal.ONE`, `dataCotacaoUtilizada = 
            null` — nunca deixar um construtor de compatibilidade de sete
             argumentos em `ResultadoItem` para adiar essa migração.
      1199      3. Confirmar, por leitura, que `criarOrdemCampo()` e `cri
            arEstagios()` já refletem a tabela final (herdada de T-022/T-
            023) — nenhuma alteração nesses dois métodos é esperada nesta
             task, salvo se a conferência revelar uma divergência real, c
            aso em que a correção entra no mesmo commit.
      1200 -    4. Ampliar `OrdemMotivosTest` para exercitar, com os moti
           -vos e o `ResultadoItem` de catorze componentes já disponíveis
           -, a tabela completa de 8.3 de ponta a ponta — não apenas os s
           -ete campos/motivos históricos.                               
      1201 -  - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) —
           - um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` 
           -simultâneos aparece nessa ordem; um item com erro estrutural 
           -em `despesa.moeda` aparece na posição correta da ordem canôni
           -ca de campo (entre `valor` e `tem_nota_fiscal`); a tabela com
           -pleta de estágios (0 a 10) é exercitada com pelo menos um mot
           -ivo de cada estágio. `EscritorResultadoTest` e `TotalPeriodoT
           -est` — migrados para a assinatura de catorze componentes de `
           -ResultadoItem`, continuam verdes sem mudança de comportamento
           - observável (a serialização dos três campos novos só é implem
           -entada em T-049; nesta task, `EscritorResultadoTest` ainda te
           -sta exclusivamente o que já testava antes, só com o construto
           -r atualizado). `SaidaCambioTest` — os quatro formatos fechado
           -s de `moeda`/`taxa_cambio_aplicada`/`data_cotacao_utilizada` 
           -no `ResultadoItem`:                                          
      1200 +    4. Ampliar `OrdemMotivosTest` para exercitar, com os moti
           +vos e o `ResultadoItem` de dez componentes já disponíveis, a 
           +tabela completa de 8.3 de ponta a ponta — não apenas os sete 
           +campos/motivos históricos.                                   
      1201 +  - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) —
           + um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` 
           +simultâneos aparece nessa ordem; um item com erro estrutural 
           +em `despesa.moeda` aparece na posição correta da ordem canôni
           +ca de campo (entre `valor` e `tem_nota_fiscal`); a tabela com
           +pleta de estágios (0 a 10) é exercitada com pelo menos um mot
           +ivo de cada estágio. `EscritorResultadoTest` e `TotalPeriodoT
           +est` — migrados para a assinatura de dez componentes de `Resu
           +ltadoItem`, continuam verdes sem mudança de comportamento obs
           +ervável (a serialização dos três campos novos só é implementa
           +da em T-049; nesta task, `EscritorResultadoTest` ainda testa 
           +exclusivamente o que já testava antes, só com o construtor at
           +ualizado). `SaidaCambioTest` — os quatro formatos fechados de
           + `moeda`/`taxa_cambio_aplicada`/`data_cotacao_utilizada` no `
           +ResultadoItem`:                                              
      1202      - **BRL válido:** `moeda = "BRL"`, `taxa_cambio_aplicada 
            = 1`, `data_cotacao_utilizada = null`.
      1203      - **Moeda estrangeira convertida:** `moeda` = código orig
            inal válido (ex.: `"EUR"`), `taxa_cambio_aplicada` = taxa efe
            tivamente utilizada, `data_cotacao_utilizada` = data exata ou
             anterior efetivamente utilizada.
      1204      - **Campo `moeda` estruturalmente inválido:** `moeda = nu
            ll`, `taxa_cambio_aplicada = null`, `data_cotacao_utilizada =
             null`.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1205      - **Moeda válida sem cotação:** `moeda` = código original
             válido (ex.: `"GBP"`), `taxa_cambio_aplicada = null`, `data_
            cotacao_utilizada = null`, `valor_normalizado = null`, `valor
            _reembolsavel = 0.00`, motivo `MOEDA_SEM_COTACAO` com `campo 
            = despesa.moeda` (CA-034) — os campos nulos deste cenário são
             verificados individualmente, não descritos genericamente com
            o "os três nulos", porque o conjunto de campos afetados é mai
            or que o do cenário de moeda estruturalmente inválida (inclui
             também `valor_normalizado` e `valor_reembolsavel`).
      1206  
      1207      **E**, adicionalmente: um cenário em que um campo **não u
            tilizado por RN-020** — `descricao`, `fornecedor`, `categoria
            ` ou `tem_nota_fiscal` — está estruturalmente inválido, enqua
            nto `valor`, `moeda` e `data` são válidos; a taxa, a data da 
            cotação e a conversão continuam sendo resolvidas normalmente 
            nesse item, confirmando que o erro estrutural num campo alhei
            o a RN-020 não interrompe a resolução cambial nem sua propaga
            ção até `ResultadoItem`.
      1208 -  - **Critério de conclusão:** todos os cenários acima verdes
           -, inclusive o de campo não-financeiro inválido; nenhum `new R
           -esultadoItem(...)` na suíte usa a assinatura antiga de onze c
           -omponentes (confirmado pelo grep repetido); nenhum construtor
           - de compatibilidade foi criado em `ResultadoItem`; `mvn -q te
           -st` completo verde — inclusive antes de T-049 alterar a seria
           -lização JSON, já que a mudança desta task é inteiramente sobr
           -e o modelo `ResultadoItem` e o `CompositorSaida`, não sobre `
           -EscritorResultado`.                                          
      1208 +  - **Critério de conclusão:** todos os cenários acima verdes
           +, inclusive o de campo não-financeiro inválido; nenhum `new R
           +esultadoItem(...)` na suíte usa a assinatura antiga de sete c
           +omponentes (confirmado pelo grep repetido); nenhum construtor
           + de compatibilidade foi criado em `ResultadoItem`; `mvn -q te
           +st` completo verde — inclusive antes de T-049 alterar a seria
           +lização JSON, já que a mudança desta task é inteiramente sobr
           +e o modelo `ResultadoItem` e o `CompositorSaida`, não sobre `
           +EscritorResultado`.                                          
      1209    - **Comando de verificação:**
      1210      ```
      1211      git grep "new ResultadoItem(" -- tests/java

● Now item 3 — T-030's observacao null case:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      725      - `tests/resources/fixtures/politica-valida-teste.json` (f
           ixture mínima válida, para o caso de sucesso)
      726    - **Passos de implementação:**
      727      1. `ler(Path caminho): PoliticaExterna` — abre o arquivo, 
           faz parsing sintático com `ObjectMapper` (`USE_BIG_DECIMAL_FOR
           _FLOATS`, mesma técnica de DT-005), captura `IOException`/`Jso
           nProcessingException` e relança como `PoliticaInvalidaExceptio
           n`.
      728 -    2. Validar, nesta ordem, os **dezesseis** pontos de `plan.
          -md` §5 — a validação completa e integral, sem nenhum ponto adi
          -ado para outra task: raiz objeto; `vigencia` obrigatória/forma
          -to/data real; `moeda_base` exatamente `"BRL"`; `nota_fiscal_ob
          -rigatoria_acima_de` obrigatório/numérico/não negativo; `padrao
          -`/`centros_custo` obrigatórios e objetos; cada tabela de `cent
          -ros_custo` é objeto; nome de categoria não vazio; configuração
          - de categoria é objeto; `limite` obrigatório/numérico, estrita
          -mente `> 0` em `padrao` e `>= 0` em `centros_custo`; `periodic
          -idade` obrigatória e exatamente `"dia"`/`"diaria"`; `observaca
          -o`, dentro de uma configuração de categoria — **ausente** (vál
          -ido), **presente como texto** (válido, lido e descartado, nunc
          -a populado em `TabelaCategoria`), **presente com qualquer outr
          -o tipo** — número, booleano, lista, objeto — (política estrutu
          -ralmente inválida); campos verdadeiramente desconhecidos — na 
          -raiz (`versao`, `acrescimo_em_viagem_percentual`) e dentro de 
          -uma configuração de categoria (além de `limite`/`periodicidade
          -`/`observacao`) — continuam ignorados sem invalidar o arquivo;
          - cópias defensivas imutáveis do modelo final (ponto 16).      
      728 +    2. Validar, nesta ordem, os **dezesseis** pontos de `plan.
          +md` §5 — a validação completa e integral, sem nenhum ponto adi
          +ado para outra task: raiz objeto; `vigencia` obrigatória/forma
          +to/data real; `moeda_base` exatamente `"BRL"`; `nota_fiscal_ob
          +rigatoria_acima_de` obrigatório/numérico/não negativo; `padrao
          +`/`centros_custo` obrigatórios e objetos; cada tabela de `cent
          +ros_custo` é objeto; nome de categoria não vazio; configuração
          + de categoria é objeto; `limite` obrigatório/numérico, estrita
          +mente `> 0` em `padrao` e `>= 0` em `centros_custo`; `periodic
          +idade` obrigatória e exatamente `"dia"`/`"diaria"`; `observaca
          +o`, dentro de uma configuração de categoria — **ausente** (vál
          +ido), **presente como texto** (válido, lido e descartado, nunc
          +a populado em `TabelaCategoria`), **presente com valor `null` 
          +explícito** (política estruturalmente inválida — `observacao` 
          +é opcional quanto à ausência da chave, não quanto a um `null` 
          +explícito quando ela existe), **presente com qualquer outro ti
          +po** — número, booleano, lista, objeto — (política estruturalm
          +ente inválida); campos verdadeiramente desconhecidos — na raiz
          + (`versao`, `acrescimo_em_viagem_percentual`) e dentro de uma 
          +configuração de categoria (além de `limite`/`periodicidade`/`o
          +bservacao`) — continuam ignorados sem invalidar o arquivo; cóp
          +ias defensivas imutáveis do modelo final (ponto 16).          
      729      3. Construir `PoliticaExterna` só depois de toda a validaç
           ão passar — nenhuma `TabelaCategoria` é criada antes da valida
           ção completa terminar.
      730      4. Não há passo adiado: `observacao` e os campos desconhec
           idos já estão integralmente cobertos no passo 2 — T-031 amplia
            a matriz de testes sobre este mesmo comportamento, já impleme
           ntado aqui, não introduz validação nova.
      731    - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo in
           existente; arquivo ilegível; JSON sintaticamente inválido; rai
           z não objeto; `vigencia` ausente/tipo errado/formato errado/da
           ta inexistente; `moeda_base` diferente de `"BRL"`; `padrao` au
           sente ou não objeto; `centros_custo` ausente ou não objeto; `n
           ota_fiscal_obrigatoria_acima_de` ausente/negativo; categoria c
           om `limite` ausente ou não numérico; `periodicidade` fora de `
           "dia"`/`"diaria"`; `observacao` ausente (válido); `observacao`
            como texto (válido e descartado); `observacao` como número (a
           rquivo inválido); `observacao` como booleano (arquivo inválido
           ); `observacao` como lista (arquivo inválido); `observacao` co
           mo objeto (arquivo inválido); um arquivo que satisfaz integral
           mente o contrato é aceito e produz o `PoliticaExterna` esperad
           o (CA-045).

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      728      2. Validar, nesta ordem, os **dezesseis** pontos de `plan.
           md` §5 — a validação completa e integral, sem nenhum ponto adi
           ado para outra task: raiz objeto; `vigencia` obrigatória/forma
           to/data real; `moeda_base` exatamente `"BRL"`; `nota_fiscal_ob
           rigatoria_acima_de` obrigatório/numérico/não negativo; `padrao
           `/`centros_custo` obrigatórios e objetos; cada tabela de `cent
           ros_custo` é objeto; nome de categoria não vazio; configuração
            de categoria é objeto; `limite` obrigatório/numérico, estrita
           mente `> 0` em `padrao` e `>= 0` em `centros_custo`; `periodic
           idade` obrigatória e exatamente `"dia"`/`"diaria"`; `observaca
           o`, dentro de uma configuração de categoria — **ausente** (vál
           ido), **presente como texto** (válido, lido e descartado, nunc
           a populado em `TabelaCategoria`), **presente com valor `null` 
           explícito** (política estruturalmente inválida — `observacao` 
           é opcional quanto à ausência da chave, não quanto a um `null` 
           explícito quando ela existe), **presente com qualquer outro ti
           po** — número, booleano, lista, objeto — (política estruturalm
           ente inválida); campos verdadeiramente desconhecidos — na raiz
            (`versao`, `acrescimo_em_viagem_percentual`) e dentro de uma 
           configuração de categoria (além de `limite`/`periodicidade`/`o
           bservacao`) — continuam ignorados sem invalidar o arquivo; cóp
           ias defensivas imutáveis do modelo final (ponto 16).
      729      3. Construir `PoliticaExterna` só depois de toda a validaç
           ão passar — nenhuma `TabelaCategoria` é criada antes da valida
           ção completa terminar.
      730      4. Não há passo adiado: `observacao` e os campos desconhec
           idos já estão integralmente cobertos no passo 2 — T-031 amplia
            a matriz de testes sobre este mesmo comportamento, já impleme
           ntado aqui, não introduz validação nova.
      731 -  - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo in
          -existente; arquivo ilegível; JSON sintaticamente inválido; rai
          -z não objeto; `vigencia` ausente/tipo errado/formato errado/da
          -ta inexistente; `moeda_base` diferente de `"BRL"`; `padrao` au
          -sente ou não objeto; `centros_custo` ausente ou não objeto; `n
          -ota_fiscal_obrigatoria_acima_de` ausente/negativo; categoria c
          -om `limite` ausente ou não numérico; `periodicidade` fora de `
          -"dia"`/`"diaria"`; `observacao` ausente (válido); `observacao`
          - como texto (válido e descartado); `observacao` como número (a
          -rquivo inválido); `observacao` como booleano (arquivo inválido
          -); `observacao` como lista (arquivo inválido); `observacao` co
          -mo objeto (arquivo inválido); um arquivo que satisfaz integral
          -mente o contrato é aceito e produz o `PoliticaExterna` esperad
          -o (CA-045).                                                   
      732 -  - **Critério de conclusão:** todos os cenários acima verdes,
          - inclusive os seis de `observacao` (ausente, texto, número, bo
          -oleano, lista, objeto); nenhuma `TabelaCategoria` construída n
          -um arquivo rejeitado (verificável indiretamente pela exceção l
          -ançada antes de qualquer retorno parcial).                    
      731 +  - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo in
          +existente; arquivo ilegível; JSON sintaticamente inválido; rai
          +z não objeto; `vigencia` ausente/tipo errado/formato errado/da
          +ta inexistente; `moeda_base` diferente de `"BRL"`; `padrao` au
          +sente ou não objeto; `centros_custo` ausente ou não objeto; `n
          +ota_fiscal_obrigatoria_acima_de` ausente/negativo; categoria c
          +om `limite` ausente ou não numérico; `periodicidade` fora de `
          +"dia"`/`"diaria"`; `observacao` ausente (válido); `observacao`
          + como texto (válido e descartado); `observacao` com `null` exp
          +lícito (arquivo inválido); `observacao` como número (arquivo i
          +nválido); `observacao` como booleano (arquivo inválido); `obse
          +rvacao` como lista (arquivo inválido); `observacao` como objet
          +o (arquivo inválido); um arquivo que satisfaz integralmente o 
          +contrato é aceito e produz o `PoliticaExterna` esperado (CA-04
          +5).                                                           
      732 +  - **Critério de conclusão:** todos os cenários acima verdes,
          + inclusive os sete de `observacao` (ausente, texto, `null` exp
          +lícito, número, booleano, lista, objeto); nenhuma `TabelaCateg
          +oria` construída num arquivo rejeitado (verificável indiretame
          +nte pela exceção lançada antes de qualquer retorno parcial).  
      733    - **Comando de verificação:**
      734      ```
      735      mvn -q test -Dtest=LeitorPoliticaTest

● Now item 4 — T-033. Let me read it:

  Read 1 file, ran 2 shell commands (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 2 lines, removed 2 lines
      795    - **Passos de implementação:**
      796      1. Nenhuma alteração de produção é esperada. Se os novos t
           estes revelarem um defeito real em `LeitorCambio`, a correção 
           de produção e os testes entram juntos no único commit de T-033
           , cuja mensagem muda de `test(T-033)` para `fix(T-033)`. Nunca
            são criados dois commits para esta task.
      797      2. Escrever os casos de teste listados abaixo.
      798 -  - **Testes obrigatórios:** `LeitorCambioTest` — `fonte`/`obs
          -ervacao` ausentes (válido); presentes como texto (válido, valo
          -r descartado); presentes como número/booleano/lista (arquivo i
          -nválido); campo desconhecido na raiz, ao lado de `moeda_base`/
          -`fonte`/`observacao`/`taxas` (ignorado, arquivo válido); chave
          - de data malformada dentro de `taxas` (ex.: `"2026/07/13"`) — 
          -arquivo inválido, não um "campo desconhecido"; chave de moeda 
          -malformada dentro de uma data (ex.: `"US"` ou `"usd"`) — arqui
          -vo inválido; a `TabelaCambio` resultante de um arquivo válido 
          -tem o mapa externo e cada `NavigableMap` interno imutáveis.   
      799 -  - **Critério de conclusão:** todos os cenários acima verdes;
          - `mvn test` completo verde.                                   
      798 +  - **Testes obrigatórios:** `LeitorCambioTest` — `fonte`/`obs
          +ervacao`, cada um nos sete cenários fechados: ausente (válido)
          +; presente como texto (válido, valor descartado); presente com
          + `null` explícito (arquivo inválido); presente como número (ar
          +quivo inválido); presente como booleano (arquivo inválido); pr
          +esente como lista (arquivo inválido); presente como objeto (ar
          +quivo inválido). Além disso: campo desconhecido na raiz, ao la
          +do de `moeda_base`/`fonte`/`observacao`/`taxas` (ignorado, arq
          +uivo válido); chave de data malformada dentro de `taxas` (ex.:
          + `"2026/07/13"`) — arquivo inválido, não um "campo desconhecid
          +o"; chave de moeda malformada dentro de uma data (ex.: `"US"` 
          +ou `"usd"`) — arquivo inválido; a `TabelaCambio` resultante de
          + um arquivo válido tem o mapa externo e cada `NavigableMap` in
          +terno imutáveis.                                              
      799 +  - **Critério de conclusão:** todos os cenários acima verdes,
          + inclusive os sete de `fonte` e os sete de `observacao` (ausen
          +te, texto, `null` explícito, número, booleano, lista, objeto);
          + `mvn test` completo verde. A implementação normativa desses s
          +ete cenários pertence a `LeitorCambio` desde T-032 (plan §7, p
          +onto 8 — "quando presentes, devem ser texto"); esta task só am
          +plia exaustivamente a prova de fronteira sobre esse comportame
          +nto já implementado.                                          
      800    - **Comando de verificação:**
      801      ```
      802      mvn -q test -Dtest=LeitorCambioTest

● Now item 5 — adding "CA atendidos" to T-055, T-056, T-057, T-058:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Confirmed: T-055 lacks "CA atendidos". Let me insert it between RN atendidas
  and DT/seções for all four tasks.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line
      1365  - [ ] **T-055** — Migrar consumidores restantes de `PoliticaR
            eembolso`, `AgregadorTetoHospedagem` e do construtor de compa
            tibilidade de `ItemValidado`
      1366    - **O que faz:** primeiro dos quatro passos exigidos por `C
            LAUDE.md` §6 antes de remover os dois componentes superados —
             **e** a migração final do construtor de dez argumentos de `I
            temValidado`, criado em T-029 exclusivamente como compatibili
            dade incremental. A migração de `PoliticaReembolso`/`Agregado
            rTetoHospedagem` é **obrigatória e completa** — não uma escol
            ha caso a caso: todo teste que hoje consome, direta ou indire
            tamente, `PoliticaReembolso` (inclusive via as sobrecargas an
            tigas de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` q
            ue o usam internamente) é migrado para `PoliticaExterna`/`Tab
            elaPoliticaResolvida` (usando a fixture de política histórica
             de T-050 no lugar de `PoliticaReembolso.padrao()`); todo tes
            te que hoje chama `AgregadorTetoHospedagem.aplicar(...)` dire
            tamente é migrado para `AgregadorTetoIndividual`. Da mesma fo
            rma, `ItemValidado` não pode manter permanentemente o constru
            tor de dez argumentos: ele preenche `taxaCambioAplicada`/`val
            orConvertidoBruto` fora de `ResolutorCambio`, o único lugar a
            utorizado a decidir esses valores (T-037) — por isso, todo te
            ste que ainda o chama é migrado para o construtor completo de
             catorze argumentos nesta task. Ao final desta task, nenhum t
            este em `tests/java` referencia `PoliticaReembolso`, `Agregad
            orTetoHospedagem`, nem o construtor de dez argumentos de `Ite
            mValidado` — as únicas ocorrências restantes de `PoliticaReem
            bolso`/`AgregadorTetoHospedagem` em todo o repositório, dentr
            o de `src/main/java`, são os dois arquivos-alvo e as sobrecar
            gas de compatibilidade que ainda os chamam internamente (a se
            rem removidas em T-056, junto do próprio construtor de dez ar
            gumentos).
      1367    - **RN atendidas:** nenhuma nova — é migração de consumidor
            es internos, não regra de negócio.
      1368 +  - **CA atendidos:** nenhum novo — migração de consumidores 
           +internos sem alteração de comportamento normativo.           
      1369    - **DT/seções do plan:** DT-011 (nota de supersessão), DT-0
            14/DT-015 (construtor completo de `ItemValidado`); plan §19 (
            "Substituídos").
      1370    - **Dependências:** T-041 (sobrecargas novas de `AvaliadorR
            egrasIndividuais`), T-043 (sobrecargas novas de `AgregadorTet
            oDiario`), T-044 (`AgregadorTetoIndividual`), T-050 (fixture 
            de política histórica).
      1371    - **Arquivos que cria/modifica:**

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line
      1405  - [ ] **T-056** — Remover `PoliticaReembolso`, `AgregadorTeto
            Hospedagem` e o construtor de compatibilidade de `ItemValidad
            o`
      1406    - **O que faz:** segundo, terceiro e quarto passos de `CLAU
            DE.md` §6: com todos os consumidores já migrados (T-055), rem
            ove `src/main/java/com/desafio/reembolso/modelo/PoliticaReemb
            olso.java` e `src/main/java/com/desafio/reembolso/pipeline/Ag
            regadorTetoHospedagem.java`, e **obrigatoriamente** também to
            das as sobrecargas e imports que dependem deles — nenhuma sob
            recarga de compatibilidade que dependa de uma classe removida
             é mantida: as sobrecargas antigas de `AvaliadorRegrasIndivid
            uais.avaliar(item)`/`avaliar(item, envelope)` e `AgregadorTet
            oDiario.aplicar(itens)` são removidas nesta task, porque T-05
            5 já garantiu que nenhum teste as usa mais. Nesta mesma task,
             remove-se também o construtor de dez argumentos de `ItemVali
            dado` (criado em T-029, exclusivamente como compatibilidade i
            ncremental) e qualquer delegação criada só para sustentá-lo —
             `ItemValidado` passa a ter um único construtor, o completo d
            e catorze argumentos, porque T-055 já garantiu que nenhum tes
            te depende mais do antigo.
      1407    - **RN atendidas:** nenhuma nova — remoção de código supera
            do (DT-007 → DT-011).
      1408 +  - **CA atendidos:** nenhum novo — remoção controlada de com
           +ponentes superados após a migração.                          
      1409    - **DT/seções do plan:** DT-007 (supersessão), DT-011, DT-0
            14 (compatibilidade de `ItemValidado` encerrada); plan §19.
      1410    - **Dependências:** T-055.
      1411    - **Arquivos que cria/modifica:**

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line
      1437  - [ ] **T-057** — README com CLI de quatro flags e documentaç
            ão de política/câmbio
      1438    - **O que faz:** atualiza `README.md` (criado em T-021) par
            a refletir o contrato de execução do Dia 2: as quatro flags o
            brigatórias (`--input`/`--output`/`--politica`/`--cambio`), e
            xemplos de execução reais contra os quatro cenários financeir
            os (baseline histórica, política v4, Rafael, Dani), a tabela 
            de códigos de saída atualizada (`0`/`2`/`3`, com a nota de qu
            e `2` agora também cobre política/câmbio inválidos), e como r
            odar a suíte de regressão (`mvn test`, incluindo `RegressaoHi
            storicaTest`/`IntegracaoEnvelopeTest`).
      1439    - **RN atendidas:** nenhuma nova — mesmo escopo de T-021, e
            stendido.
      1440 +  - **CA atendidos:** nenhum novo — documentação dos comporta
           +mentos já comprovados por CA-037 a CA-044.                   
      1441    - **DT/seções do plan:** plan §3, §16.
      1442    - **Dependências:** T-054 (execução real já verificada, par
            a documentar comandos testados de fato).
      1443    - **Arquivos que cria/modifica:**

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line
      1463  - [ ] **T-058** — Revisão final de rastreabilidade e confirma
            ção do item C fora de escopo
      1464    - **O que faz:** revisão documental de fechamento do Dia 2:
             confere, identificador por identificador, que RN-019 a RN-02
            2 e CA-024 a CA-049 aparecem em pelo menos uma task deste arq
            uivo (matriz ao final deste documento) e em pelo menos um tes
            te executável; confirma, por leitura de `spec.md` §3/AMB-033 
            e `plan.md` §20, que o item C (fila de aprovação manual) perm
            anece fora de escopo — nenhum código, task ou teste o antecip
            a. Task documental, sem código de produção.
      1465    - **RN atendidas:** nenhuma nova — é auditoria de rastreabi
            lidade.
      1466 +  - **CA atendidos:** nenhum novo — auditoria final da cobert
           +ura de CA-024 a CA-049.                                      
      1467    - **DT/seções do plan:** plan §17 (matriz de rastreabilidad
            e), §20.
      1468    - **Dependências:** todas as tasks anteriores desta fase (T
            -022 a T-057).
      1469    - **Arquivos que cria/modifica:**

● Now item 6 — adding PoliticaExternaTest to the two RN-019 matrix rows:
  Ran 1 shell command (ctrl+o to expand)

● Both rows have identical test lists. Let me edit each with unique surrounding
  context (they're in different table sections):

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1559  
      1560  | Regra | Task(s) dona(s) | Teste(s) |
      1561  |---|---|---|
      1562 -| RN-019 | T-025, T-026, T-028 (modelos) · T-040 (`ResolutorP
           -oliticaCentroCusto`) · T-041, T-042 (avaliador + wiring) · T-
           -043 a T-046 (tetos por periodicidade) | `TabelaCategoriaTest`
           -, `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentroCust
           -oTest`, `CategoriaCentroCustoTest`, `TetoPorPeriodicidadeTest
           -`, `AgregadorTetoIndividualTest`, `CliContratoTest` |        
      1562 +| RN-019 | T-025, T-026, T-028 (modelos) · T-040 (`ResolutorP
           +oliticaCentroCusto`) · T-041, T-042 (avaliador + wiring) · T-
           +043 a T-046 (tetos por periodicidade) | `TabelaCategoriaTest`
           +, `PoliticaExternaTest`, `TabelaPoliticaResolvidaTest`, `Reso
           +lutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest`, `T
           +etoPorPeriodicidadeTest`, `AgregadorTetoIndividualTest`, `Cli
           +ContratoTest` |                                              
      1563  | RN-020 | T-027 (`TabelaCambio`) · T-029 (campos de câmbio e
            m `ItemValidado`) · T-032, T-033 (`LeitorCambio`) · T-036 (ca
            mpo `moeda`) · T-037, T-038, T-039 (`ResolutorCambio` + integ
            ração) · T-041 (coexistência com `CATEGORIA_NAO_REEMBOLSAVEL_
            CENTRO_CUSTO`) | `TabelaCambioTest`, `ItemValidadoCambioTest`
            , `LeitorCambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`
            , `ConversaoCambialIntegracaoTest`, `MoedaSemCotacaoTest`, `C
            ategoriaCentroCustoTest` |
      1564  | RN-021 | T-026 (`PoliticaExterna.vigencia`) · T-030 (valida
            ção de `vigencia` no `LeitorPolitica`) | `PoliticaExternaTest
            `, `LeitorPoliticaTest` |
      1565  | RN-022 | T-030, T-031 (`LeitorPolitica`) · T-032, T-033 (`L
            eitorCambio`) · T-035 (CLI trata falha como exit `2`) | `Leit
            orPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |

  Ran 1 shell command (ctrl+o to expand)

● Now the second (unique) row is directly editable:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      1615  
      1616  | RN/CA | Tasks responsáveis | Testes previstos |
      1617  |---|---|---|
      1618 -| RN-019 | T-025, T-026, T-028, T-040, T-041, T-042, T-043, T
           --044, T-045, T-046 | `TabelaCategoriaTest`, `TabelaPoliticaRe
           -solvidaTest`, `ResolutorPoliticaCentroCustoTest`, `CategoriaC
           -entroCustoTest`, `TetoPorPeriodicidadeTest`, `AgregadorTetoIn
           -dividualTest`, `CliContratoTest` |                           
      1618 +| RN-019 | T-025, T-026, T-028, T-040, T-041, T-042, T-043, T
           +-044, T-045, T-046 | `TabelaCategoriaTest`, `PoliticaExternaT
           +est`, `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentro
           +CustoTest`, `CategoriaCentroCustoTest`, `TetoPorPeriodicidade
           +Test`, `AgregadorTetoIndividualTest`, `CliContratoTest` |    
      1619  | RN-020 | T-027, T-029, T-032, T-033, T-036, T-037, T-038, T
            -039, T-041 | `TabelaCambioTest`, `LeitorCambioTest`, `ItemVa
            lidadoCambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`, `
            ConversaoCambialIntegracaoTest`, `MoedaSemCotacaoTest`, `Cate
            goriaCentroCustoTest` |
      1620  | RN-021 | T-026, T-030 | `PoliticaExternaTest`, `LeitorPolit
            icaTest` |
      1621  | RN-022 | T-030, T-031, T-032, T-033, T-035 | `LeitorPolitic
            aTest`, `LeitorCambioTest`, `CliContratoTest` |

● Now let's run the item 7 textual verification checklist:

  Ran 2 shell commands (ctrl+o to expand)

● Confirmed correct — the content is fully present (my earlier grep just
  undercounted due to sed boundary quirks). Now the mandated git verification:

  Ran 2 shell commands (ctrl+o to expand)

● All confirmed empty except tasks.md. Now let's verify T-001–T-021 remain
  byte-for-byte intact and task numbering stays continuous:

  Ran 1 shell command (ctrl+o to expand)

● Same hunk boundaries as prior sessions — the T-001–T-021 block (before line
  493, "## Fase 8") remains untouched. Now the final count/numbering check:

  Ran 1 shell command (ctrl+o to expand)

● All confirmed: 58 tasks total, no gaps, 21 done, 37 pending, all metadata
  preserved. Summary:

  1. Contagem correta dos componentes de ResultadoItem: o record original tem
  sete componentes (indiceEntrada, id, valorInformado, valorNormalizado,
  valorReembolsavel, decisao, motivos); T-048 acrescenta exatamente três (moeda,
  taxaCambioAplicada, dataCotacaoUtilizada), levando a assinatura de sete para 
  dez componentes — nunca "onze para catorze" (isso pertencia ao ItemValidado,
  um modelo diferente, e não foi tocado).

  2. Nova justificativa para não criar construtor de compatibilidade: T-048
  agora registra corretamente que um record Java pode declarar construtores
  adicionais que deleguem ao canônico — não é limitação da linguagem — mas que a
  decisão de não criar um construtor de compatibilidade de sete argumentos é
  deliberada: migração imediata e contrato único, com todos os consumidores
  diretos migrados no mesmo commit.

  3. Casos null acrescentados:
  - T-030: observacao com null explícito → política estruturalmente inválida
  (agora sete cenários: ausente, texto, null, número, booleano, lista, objeto),
  permanecendo em T-030, não movido para T-031.
  - T-033: fonte/observacao do câmbio, cada um nos sete cenários fechados
  (ausente, texto, null explícito, número, booleano, lista, objeto) —
  implementação normativa continua em T-032, T-033 só amplia a prova de
  fronteira.

  4. Linhas CA acrescentadas em T-055 a T-058: cada uma agora tem **CA 
  atendidos:** entre RN atendidas e DT/seções do plan — "nenhum novo", com a
  justificativa específica de cada task (migração/remoção sem alteração
  normativa, documentação de CA já comprovados, auditoria de CA-024 a CA-049).

  5. Correção das duas matrizes de RN-019: PoliticaExternaTest acrescentado à
  coluna de testes em ambas ("Cobertura — RN-019 a RN-022" e "Matriz final"),
  preservando todos os testes já listados.

  6. git status --short:
   M specs/001-motor-reembolso/tasks.md

  Somente tasks.md foi alterado; T-001 a T-021 permanecem byte a byte intactas;
  T-022 a T-058 continuam contínuas e pendentes; nenhuma task foi criada,
  removida ou renumerada; nenhum código, teste ou fixture foi alterado; nenhum
  /export, git add, commit ou push foi executado. Parado para revisão.

✻ Cooked for 4m 3s

※ recap: We're finalizing the Dia 2 backlog for the reembolso engine in tasks.md, correcting ResultadoItem arity, null-case coverage, CA lines, and matrix entries. Next step: your review of these changes before any implementation begins.

> A revisão independente de tasks.md versão 1.1 foi concluída e aprovada.       
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  specs/001-motor-reembolso/tasks.md                                            
                                                                                
  Faça somente esta mudança no cabeçalho:                                       
                                                                                
  **Status:** em revisão                                                        
                                                                                
  para:                                                                         
                                                                                
  **Status:** aprovado                                                          
                                                                                
  Mantenha rigorosamente inalterados:                                           
                                                                                
  - versão 1.1;                                                                 
  - data 2026-08-05;                                                            
  - base spec 1.2 aprovada;                                                     
  - base plan 1.1 aprovado;                                                     
  - total de 58 tasks;                                                          
  - T-001 a T-021 concluídas;                                                   
  - T-022 a T-058 pendentes;                                                    
  - todos os títulos, textos, matrizes, comandos, critérios e mensagens de      
  commit;                                                                       
  - spec.md;                                                                    
  - plan.md;                                                                    
  - DECISIONS.md;                                                               
  - README.md;                                                                  
  - código;                                                                     
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Não crie, remova ou renumere tasks.                                           
                                                                                
  Depois execute:                                                               
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  Select-String -Path specs/001-motor-reembolso/tasks.md -Pattern               
  "Versão:|Status:|Baseado em:|Total de tasks:"                                 
                                                                                
  Confirme:                                                                     
                                                                                
  - status final aprovado;                                                      
  - versão ainda 1.1;                                                           
  - data ainda 2026-08-05;                                                      
  - base ainda spec 1.2 e plan 1.1 aprovados;                                   
  - 58 tasks, de T-001 a T-058;                                                 
  - somente tasks.md permanece modificado;                                      
  - nesta intervenção, apenas o texto do status foi alterado;                   
  - nenhum /export, git add, commit ou push foi executado.                      
                                                                                
  Pare.                                                                         

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
       6  > Marque `[x]` conforme conclui — ao longo do caminho, não tudo
           no fim. O histórico
       7  > de quando cada task foi marcada é lido na correção.
       8  
       9 -**Versão:** 1.1 · **Status:** em revisão · **Última alteração:*
         -* 2026-08-05                                                   
       9 +**Versão:** 1.1 · **Status:** aprovado · **Última alteração:** 
         +2026-08-05                                                     
      10  **Baseado em:** spec `1.2` (aprovada) · plan `1.1` (aprovado)
      11  **Total de tasks:** 58 (`T-001` a `T-058`) — `T-001` a `T-021` 
          concluídas (Dia 1, spec 1.1/plan 1.0); `T-022` a `T-058` penden
          tes (Dia 2, política v4, spec 1.2/plan 1.1).
      12  **Estimativa de commits — Dia 1:** 22 (já realizados, ver histó
          rico abaixo). **Dia 2:** 37 (uma task, um commit — nenhuma exce
          ção nesta rodada).

● Now running the requested verification commands:

  Ran 3 shell commands (ctrl+o to expand)

● All verifications pass:

  - Status final: aprovado
  - Versão: ainda 1.1
  - Data: ainda 2026-08-05
  - Base: ainda spec 1.2 (aprovada) · plan 1.1 (aprovado)
  - Total: 58 tasks, T-001 a T-058 — 21 com [x] concluída (T-001–T-021) e 37 com
  [ ] pendente (T-022–T-058), confirmado pela varredura de Status:
  - Arquivos alterados: somente specs/001-motor-reembolso/tasks.md (git diff -- 
  spec.md/plan.md/DECISIONS.md/README.md/src/tests todos vazios)
  - git diff --check: limpo (exit 0)
  - Nesta intervenção, a única mudança de texto foi **Status:** em revisão →
  **Status:** aprovado, na linha 9 do cabeçalho — nada mais foi tocado
  - Nenhum /export, git add, commit ou push foi executado

  git status --short:
   M specs/001-motor-reembolso/tasks.md

  Parado.

✻ Brewed for 34s