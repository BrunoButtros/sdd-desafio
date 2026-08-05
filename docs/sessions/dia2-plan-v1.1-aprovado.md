 ▐▛███▜▌   Claude Code v2.1.222
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Vamos atualizar o plano técnico do Dia 2 com base na spec 1.2 já aprovada.    
                                                                                
  Nesta tarefa, altere exclusivamente:                                          
                                                                                
  ```text                                                                       
  specs/001-motor-reembolso/plan.md                                             
  ```                                                                           
                                                                                
  Não altere:                                                                   
                                                                                
  * `specs/001-motor-reembolso/spec.md`;                                        
  * `specs/001-motor-reembolso/DECISIONS.md`;                                   
  * `specs/001-motor-reembolso/tasks.md`;                                       
  * `README.md`;                                                                
  * código;                                                                     
  * testes;                                                                     
  * fixtures;                                                                   
  * os cinco arquivos de `exemplos/envelope/`.                                  
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  ## Leitura obrigatória                                                        
                                                                                
  Antes de editar, leia integralmente:                                          
                                                                                
  * `CLAUDE.md`;                                                                
  * `specs/001-motor-reembolso/spec.md`;                                        
  * `specs/001-motor-reembolso/DECISIONS.md`;                                   
  * `specs/001-motor-reembolso/plan.md`;                                        
  * `specs/001-motor-reembolso/tasks.md`;                                       
  * os cinco arquivos em `exemplos/envelope/`.                                  
                                                                                
  Inspecione também a estrutura e os arquivos relevantes de:                    
                                                                                
  * `src/main`;                                                                 
  * `src/test`.                                                                 
                                                                                
  Essa inspeção é somente para usar nomes, pacotes e componentes reais no       
  plano. Não modifique código ou testes.                                        
                                                                                
  Antes de editar, informe brevemente:                                          
                                                                                
  1. versão e status atuais do `plan.md`;                                       
  2. maior identificador DT existente;                                          
  3. decisões técnicas incompatíveis com a spec 1.2;                            
  4. situação atual da DT-007;                                                  
  5. componentes existentes que precisarão ser estendidos, substituídos ou      
  preservados.                                                                  
                                                                                
  Depois continue a edição sem esperar nova confirmação.                        
                                                                                
  ## Cabeçalho                                                                  
                                                                                
  Atualize o `plan.md` para:                                                    
                                                                                
  * próxima versão minor;                                                       
  * se estiver em `1.0`, usar `1.1`;                                            
  * status `em revisão`;                                                        
  * última alteração `2026-08-05`;                                              
  * referência normativa à spec `1.2`, status `aprovado`.                       
                                                                                
  ## Preservação das decisões técnicas                                          
                                                                                
  Não apague decisões técnicas antigas silenciosamente.                         
                                                                                
  Quando uma DT deixar de valer:                                                
                                                                                
  * preserve seu registro histórico;                                            
  * marque-a como `SUPERADA` ou `INVALIDADA`;                                   
  * informe qual DT nova a substitui;                                           
  * indique quais requisitos da spec 1.2 provocaram a substituição.             
                                                                                
  A DT-007 deve ser explicitamente superada, pois a política não é mais fixa,   
  interna e imutável.                                                           
                                                                                
  Crie novas DTs em sequência ao maior identificador existente. Não reutilize   
  números.                                                                      
                                                                                
  ## Contrato da CLI                                                            
                                                                                
  Incorpore ao desenho técnico o contrato normativo:                            
                                                                                
  ```text                                                                       
  calcular --input <entrada.json> --output <saida.json> --politica              
  <politica.json> --cambio <cambio.json>                                        
  ```                                                                           
                                                                                
  Planeje:                                                                      
                                                                                
  * quatro flags obrigatórias;                                                  
  * qualquer ordem;                                                             
  * cada flag exatamente uma vez;                                               
  * flag ausente, repetida ou desconhecida → exit `2`;                          
  * política ou câmbio inexistente, ilegível, JSON inválido ou estruturalmente  
  inválido → exit `2`;                                                          
  * envelope de despesas estruturalmente inválido → exit `3`;                   
  * sucesso → exit `0`;                                                         
  * nenhum exit `4` ou `                                                        
  ──── (243 lines hidden) ───────────────────────────────────────────────────── 
                                                                                
                                                                                
  ## Estratégia de testes                                                       
                                                                                
  Planeje testes unitários, de integração e ponta a ponta/CLI para:             
                                                                                
  * flags e códigos `0`, `2` e `3`;                                             
  * preservação do output;                                                      
  * política válida e inválida;                                                 
  * câmbio válido e inválido;                                                   
  * `taxas: {}`;                                                                
  * limite zero em `padrao`;                                                    
  * limite zero em centro cadastrado;                                           
  * centro conhecido, desconhecido, ausente, nulo e de tipo inválido;           
  * comparação exata de centro de custo;                                        
  * moeda ausente versus `null`;                                                
  * moeda com tipo ou formato inválido;                                         
  * moeda sem cotação;                                                          
  * fallback para cotação anterior;                                             
  * proibição de cotação futura;                                                
  * arredondamento único;                                                       
  * nota fiscal sobre o valor convertido;                                       
  * duplicidade entre moedas diferentes;                                        
  * categoria externa com `"dia"`;                                              
  * categoria externa com `"diaria"`;                                           
  * `TETO_INDIVIDUAL_APLICADO`;                                                 
  * os quatro totais normativos;                                                
  * regressões anteriores usando a política externa histórica.                  
                                                                                
  Não crie testes nesta tarefa.                                                 
                                                                                
  ## Impacto em arquivos                                                        
                                                                                
  Inclua uma seção indicando, com nomes e pacotes reais encontrados no          
  repositório, quais componentes provavelmente serão:                           
                                                                                
  * criados;                                                                    
  * substituídos;                                                               
  * estendidos;                                                                 
  * mantidos sem alteração.                                                     
                                                                                
  Não afirme que um arquivo existe sem verificar.                               
                                                                                
  Não implemente nenhuma alteração agora.                                       
                                                                                
  ## Item C                                                                     
                                                                                
  Registre que a aprovação manual continua fora de escopo:                      
                                                                                
  * sem `AGUARDANDO_APROVACAO`;                                                 
  * sem fila;                                                                   
  * sem serviço de aprovação;                                                   
  * sem task nesta versão.                                                      
                                                                                
  ## Preparação para tasks                                                      
                                                                                
  O futuro `tasks.md` continuará em `T-022`.                                    
                                                                                
  Não altere `tasks.md`.                                                        
                                                                                
  Inclua blocos técnicos futuros, ainda sem atribuir números:                   
                                                                                
  * CLI;                                                                        
  * política externa;                                                           
  * câmbio externo;                                                             
  * contrato do campo moeda;                                                    
  * conversão;                                                                  
  * resolução por centro de custo;                                              
  * periodicidade e tetos;                                                      
  * novos motivos e saída;                                                      
  * regressões;                                                                 
  * integração;                                                                 
  * documentação.                                                               
                                                                                
  ## Verificação final                                                          
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- README.md                                                         
  git diff -- src                                                               
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * somente `plan.md` foi alterado nesta tarefa;                                
  * spec, decisions, tasks, README, código, testes e envelope não foram         
  alterados;                                                                    
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Ao terminar, informe:                                                         
                                                                                
  1. versão e status final do plan;                                             
  2. DTs superadas e novas;                                                     
  3. arquitetura para política e câmbio;                                        
  4. pipeline final;                                                            
  5. estratégia de regressão histórica;                                         
  6. impacto previsto em arquivos;                                              
  7. `git status --short`.                                                      
                                                                                
  Pare para revisão.                                                            

  Searched for 6 patterns, read 20 files (ctrl+o to expand)

● Now I have a complete picture. Before editing, here's the required pre-edit
  briefing:

  1. Versão/status atuais do plan.md: 1.0, baseado na spec 1.1. Não tem uma
  linha de "Status" homóloga à do spec.md — só o cabeçalho Versão: 1.0 · Baseado
  na spec: 1.1.

  2. Maior identificador DT existente: DT-010 (escrita atômica do arquivo de
  saída).

  3. Decisões técnicas incompatíveis com a spec 1.2:
  - DT-007 — política como estrutura única de 4 constantes fixas
  (60/80/250/100). Incompatível: RN-019 exige política externa, dinâmica, por
  centro de custo.
  - DT-003 — contrato de CLI com só --input/--output e dois motivos de exit 2.
  Incompatível: §4.1.1 normativo exige 4 flags e novos gatilhos de exit 2
  (política/câmbio inválidos).
  - §5 do plan ("Como a política é representada") e a classe real
  PoliticaReembolso.java — ambas hardcoded, incompatíveis com RN-019.
  - DT-005/DT-006 continuam válidas em espírito, mas precisam de extensão (7º
  campo moeda; pipeline de 11 → 13 passos).

  4. Situação da DT-007: o código real (PoliticaReembolso.java) hoje é
  exatamente o que a DT descreve — uma única estrutura imutável com
  60.00/80.00/250.00/100.00. DECISIONS.md (D-003) já a invalidou do lado da
  spec; plan.md ainda não foi tocado — é isto que esta tarefa resolve.

  5. Componentes existentes a estender/substituir/preservar:
  - Substituir: PoliticaReembolso.java (4 constantes → modelo externo por centro
  de custo).
  - Estender: Main.java (2→4 flags), ItemValidado.java/ValidadorItem.java (campo
  moeda), Normalizador.java (conversão + arredondamento único),
  AvaliadorRegrasIndividuais.java (RN-019/RN-020/RN-009),
  DetectorDuplicidadeEconomica.java (chave com moeda),
  AgregadorTetoDiario.java/AgregadorTetoHospedagem.java (periodicidade em vez de
  nome fixo de categoria), CompositorSaida.java (3 campos novos, nova ordem de
  motivos), EscritorResultado.java (serialização dos 3 campos),
  MotivoCodigo.java/RegraNegocio.java/CampoCanonico.java (novos valores de
  enum).
  - Preservar: Decisao.java, DetectorIdDuplicado.java, SeletorElegiveis.java,
  SomadorTotal.java, ValidadorEnvelope.java, estrutura
  tests/java+tests/resources (DT-009/DT-010).

  Prosseguindo com a edição.

● Write(specs\001-motor-reembolso\plan.md)
  ⎿  Added 458 lines, removed 210 lines
      1  # Plano Técnico — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.0 · **Baseado na spec:** 1.1                      
      3 +**Versão:** 1.1 · **Status:** em revisão · **Baseado na spec:** 
        +1.2 (aprovado) · **Última alteração:** 2026-08-05               
      4  
      5  > Aqui mora o COMO. Este arquivo pode e deve falar de linguagem,
          biblioteca e
      6  > arquitetura. O que ele **não** pode é introduzir regra de negó
         cio nova — se
     ...
       8  
       9  ---
      10  
      11 +## 0. O que mudou nesta versão (Dia 2 — política v4)           
      12 +                                                               
      13 +Esta revisão incorpora, do lado do COMO, a mudança de requisito
         + já normatizada em `spec.md` 1.2 e registrada em `DECISIONS.md`
         + (D-003): política de reembolso externa e variável por centro d
         +e custo (RN-019), câmbio e conversão monetária (RN-020), vigênc
         +ia de política (RN-021) e processabilidade dos arquivos externo
         +s (RN-022). Nenhuma regra de negócio é decidida aqui — cada RN 
         +citada já está fechada em `spec.md`; este arquivo só desenha os
         + componentes, estruturas de dados e decisões técnicas que a mat
         +erializam.                                                     
      14 +                                                               
      15 +Nenhuma alteração de código foi feita nesta tarefa. Este arquiv
         +o é exclusivamente planejamento — a implementação real fica par
         +a as tasks a partir de `T-022` (`tasks.md`, não alterado aqui).
      16 +                                                               
      17 +---                                                            
      18 +                                                               
      19  ## 1. Stack
      20  
      21  | Escolha | O quê | Por quê | O que descartei e por quê |
     ...
       23  | Linguagem | Java 21 (LTS) | Familiaridade declarada de desen
           volvedor back-end Java; JDK 21.0.2 já verificado funcionando n
           esta máquina sem instalação adicional; `java.math.BigDecimal` 
           nativo resolve a exigência de aritmética decimal exata sem bib
           lioteca externa. | Python e Node — nenhum dos dois está instal
           ado nesta máquina (verificado via `python --version` e `node -
           -version`); instalar agora consome prazo curto e adiciona risc
           o de "funciona aqui, não funciona na correção". Kotlin — mesma
            JVM, mas introduziria sintaxe nova sob pressão de tempo sem g
           anho que a spec exija. |
       24  | Build | Maven 3.9 ou superior | Maven 3.9.6 já verificado fu
           ncionando, casado ao JDK 21 instalado; convenção de projeto pa
           drão para quem já é back-end Java. | Gradle — funcionalmente e
           quivalente, mas sem motivo para trocar de ferramenta já domina
           da e já disponível. |
       25  | Testes | JUnit 5 (Jupiter) | Parametrização nativa (`@Parame
           terizedTest`) essencial para as matrizes de fronteira monetári
           a e de nota fiscal; integração direta com Maven Surefire, sem 
           configuração extra. | TestNG — capacidade equivalente, sem mot
           ivo para introduzir dependência adicional. |
       18 -| Parsing/validação | Jackson Databind, lido via árvore `JsonN
          -ode` na camada de entrada | `JsonNode` permite inspecionar o t
          -ipo JSON bruto de cada campo (necessário para distinguir `CAMP
          -O_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` conf
          -orme RN-002) e preservar `valor_informado` exatamente como rec
          -ebido, inclusive quando o tipo é inválido. Suporta leitura de 
          -números como `BigDecimal` exato via configuração (ver §7). | G
          -son — exigiria a mesma configuração de números decimais, porém
          - menos familiar ao perfil declarado. `org.json` — comportament
          -o de conversão numérica para `BigDecimal` historicamente menos
          - documentado/confiável. Parser JSON escrito à mão — risco desn
          -ecessário de bugs de parsing sob prazo de dois dias, sem ganho
          - que a spec exija. |                                          
       19 -| Aritmética monetária | `java.math.BigDecimal`, construído se
          -mpre a partir de texto/`decimalValue()`, nunca de `double` | É
          - exatamente o mecanismo que garante `100.005 → 100,01` (RN-004
          -): ponto flutuante binário representaria `100.005` como `100.0
          -0499999999999...` e arredondaria para o lado errado. Nativo do
          - JDK, sem dependência extra. | `double`/`float` — fonte de bug
          - previsível e documentada pelo próprio `CLAUDE.md`. Biblioteca
          -s de dinheiro de terceiros (ex. Joda-Money) — desnecessárias; 
          -`BigDecimal` com escala e modo de arredondamento explícitos já
          - cobre tudo que a spec exige. |                               
       20 -| Empacotamento | Maven Shade Plugin, produzindo um único JAR 
          -executável | Gera `target/motor-reembolso.jar` autocontido (de
          -pendências + `Main-Class` no manifesto), executável só com `ja
          -va -jar`, sem exigir classpath externo na máquina do avaliador
          -. | `maven-assembly-plugin` — resultado equivalente, Shade é m
          -ais direto para o caso de um único fat jar sem relocations. Sp
          -ring Boot — framework pesado e servidor HTTP desnecessários pa
          -ra uma CLI que lê um arquivo e escreve outro. Wrapper `.sh`/`.
          -bat` — descartado por decisão explícita: mais uma superfície p
          -ara manter e testar em duas plataformas sem necessidade, já qu
          -e `java -jar` funciona identicamente em qualquer sistema com J
          -DK. |                                                         
       26 +| Parsing/validação | Jackson Databind, lido via árvore `JsonN
          +ode` na camada de entrada | `JsonNode` permite inspecionar o t
          +ipo JSON bruto de cada campo (necessário para distinguir `CAMP
          +O_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` conf
          +orme RN-002) e preservar `valor_informado` exatamente como rec
          +ebido. A partir da política v4, a mesma técnica de árvore é re
          +aproveitada para os dois arquivos externos (`--politica`, `--c
          +ambio`) e para distinguir, no 7º campo `despesa.moeda`, a ausê
          +ncia da chave do valor `null` explícito (RN-002, RN-020 — ver 
          +§8). | Gson/`org.json` — mesma configuração adicional exigida,
          + menos familiares. Parser escrito à mão — risco desnecessário 
          +sob prazo curto. |                                            
       27 +| Aritmética monetária | `java.math.BigDecimal`, construído se
          +mpre a partir de texto/`decimalValue()`, nunca de `double` | É
          + exatamente o mecanismo que garante `100.005 → 100,01` (RN-004
          +) e, a partir da v4, também garante que a multiplicação `valor
          + bruto × taxa` (RN-020) seja decimal-exata antes do único arre
          +dondamento. | `double`/`float` — fonte de bug documentada. Bib
          +liotecas de dinheiro de terceiros — desnecessárias. |         
       28 +| Empacotamento | Maven Shade Plugin, produzindo um único JAR 
          +executável | Gera `target/motor-reembolso.jar` autocontido, ex
          +ecutável só com `java -jar`. | `maven-assembly-plugin` — equiv
          +alente, Shade mais direto. Spring Boot — desnecessário. Wrappe
          +r `.sh`/`.bat` — descartado por decisão explícita. |          
       29  
       30  ---
       31  
       32  ## 2. Arquitetura
       33  
       26 -A arquitetura é um pipeline linear que segue **literalmente** 
          -os onze passos da seção 8.1 da spec. Cada passo é um estágio q
          -ue recebe a lista de itens (na ordem da entrada) e devolve a m
          -esma lista enriquecida — nenhum estágio reordena a lista mestr
          -a.                                                            
       34 +A arquitetura é um pipeline linear que segue **literalmente** 
          +os treze passos da seção 8.1 da spec 1.2 (eram onze na spec 1.
          +1 — os dois passos novos são a validação dos arquivos externos
          +, à frente de tudo, e a resolução de câmbio, entre a detecção 
          +de ID duplicado e a normalização). Cada passo é um estágio que
          + recebe a lista de itens (na ordem da entrada) e devolve a mes
          +ma lista enriquecida — nenhum estágio reordena a lista mestra.
       35  
       36  ```
       29 -entrada JSON                                                  
       30 -  │                                                           
       31 -  ▼                                                           
       32 -[Leitor]  → JsonNode da árvore completa                       
       33 -  │                                                           
       34 -  ▼                                                           
       35 -[1] Validador de envelope (RN-001)              — fatal se fal
          -har, nada mais executa                                        
       36 -  │                                                           
       37 -  ▼                                                           
       38 -[2] Validador de item / classificador estrutural (RN-002)     
       39 -  │      atribui indice_entrada (base 1, imutável) antes de qu
          -alquer validação                                              
       40 -  ▼                                                           
       41 -[3] Detector de despesa.id duplicado (RN-003)                 
       42 -  │                                                           
       43 -  ▼                                                           
       44 -[4] Normalizador (RN-004 valor · RN-005 categoria)            
       45 -  │                                                           
       46 -  ▼                                                           
       47 -[5] Avaliador de regras individuais (RN-006 · RN-007 · RN-008 
          -· RN-009)                                                     
       48 -  │                                                           
       49 -  ▼                                                           
       50 -[6] Seletor de itens aprovados em todas as validações individu
          -ais                                                           
       51 -  │                                                           
       52 -  ▼                                                           
       53 -[7] Detector de duplicidade econômica (RN-010)                
       54 -  │                                                           
       55 -  ▼                                                           
       56 -[8] Seletor de itens elegíveis após a duplicidade             
       57 -  │                                                           
       58 -  ▼                                                           
       59 -[9] Agregador de tetos (RN-011 · RN-012 · RN-013 · RN-014 · RN
          --015)                                                         
       60 -  │                                                           
       61 -  ▼                                                           
       62 -[10] Compositor de saída — decisão final + motivos na ordem de
          - apresentação (8.3)                                           
       63 -  │                                                           
       64 -  ▼                                                           
       65 -[11] Somador do total (RN-018)                                
       66 -  │                                                           
       67 -  ▼                                                           
       68 -[Escritor] → JSON de saída                                    
       37 +--politica          --cambio              entrada JSON (--inpu
          +t)                                                            
       38 +   │                    │                        │            
       39 +   ▼                    ▼                        ▼            
       40 +[Leitor+Validador   [Leitor+Validador       [Leitor] → JsonNod
          +e                                                             
       41 + de Política]        de Câmbio]                   │           
       42 +   │                    │                         ▼           
       43 +   │                    │            [2] Validador de envelope
          + (RN-001) — fatal se falhar                                   
       44 +   │                    │                         │           
       45 +   ▼                    ▼                         ▼           
       46 +PoliticaExterna    TabelaCambio          [3] Validador de item
          + / classificador estrutural                                   
       47 +(imutável)         (imutável)                (RN-002, incluind
          +o despesa.moeda — 7º campo)                                   
       48 +   │                    │                         │           
       49 +   │                    │                         ▼           
       50 +   │                    │            [4] Detector de despesa.i
          +d duplicado (RN-003)                                          
       51 +   │                    │                         │           
       52 +   │                    └────────────►[5] Resolutor de câmbio 
          +/ conversão para BRL (RN-020)                                 
       53 +   │                                              │           
       54 +   ▼                                              ▼           
       55 +   └───────────────────────────────►[6] Normalizador (RN-004 v
          +alor já convertido · RN-005 categoria)                        
       56 +                                                   │          
       57 +                                                   ▼          
       58 +                          [7] Avaliador de regras individuais,
          + incluindo política do                                        
       59 +                              centro de custo (RN-006 · RN-007
          + · RN-008 · RN-009 · RN-019)                                  
       60 +                                                   │          
       61 +                                                   ▼          
       62 +                          [8] Seletor de itens aprovados em to
          +das as validações individuais                                 
       63 +                                                   │          
       64 +                                                   ▼          
       65 +                          [9] Detector de duplicidade econômic
          +a (RN-010, chave com moeda)                                   
       66 +                                                   │          
       67 +                                                   ▼          
       68 +                          [10] Seletor de itens elegíveis após
          + a duplicidade                                                
       69 +                                                   │          
       70 +                                                   ▼          
       71 +                          [11] Agregador de tetos por periodic
          +idade (RN-011 a RN-015, RN-019)                               
       72 +                                                   │          
       73 +                                                   ▼          
       74 +                          [12] Compositor de saída — decisão f
          +inal + motivos (8.3),                                         
       75 +                               incluindo moeda/taxa_cambio_apl
          +icada/data_cotacao_utilizada                                  
       76 +                                                   │          
       77 +                                                   ▼          
       78 +                          [13] Somador do total (RN-018)      
       79 +                                                   │          
       80 +                                                   ▼          
       81 +                                          [Escritor] → JSON de
          + saída                                                        
       82  ```
       83  
       71 -**Como a avaliação de regras funciona (evita parar no primeiro
          - motivo, sem executar tudo indiscriminadamente):**            
       84 +O passo `[1]` (validar política e câmbio, RN-021/RN-022) acont
          +ece **antes** do passo `[2]` (validar envelope) — é a ordem no
          +rmativa de `spec.md` §8.1. Uma falha no passo `[1]` é código d
          +e saída `2` e nem chega a abrir o arquivo de despesas para val
          +idação de envelope.                                           
       85  
       73 -- Cada regra é avaliada quando **todos os campos de que ela de
          -pende** estão estruturalmente válidos, conforme a matriz 8.2 —
          - não quando o item inteiro está livre de qualquer defeito.    
       74 -- Um erro em um campo que a regra **não usa** não impede essa 
          -regra de rodar. Exemplo: `despesa.data` malformada não impede 
          -a avaliação de `NOTA_FISCAL_AUSENTE`, que depende só de `despe
          -sa.valor` e `despesa.tem_nota_fiscal`.                        
       75 -- Motivos aplicáveis são **acumulados** num único acumulador p
          -or item ao longo do pipeline — nenhuma etapa substitui ou desc
          -arta motivos de etapas anteriores.                            
       76 -- Somente as **exclusões expressamente listadas em 8.4** inter
          -rompem etapas posteriores (ex.: item com `ID_DUPLICADO` não en
          -tra na detecção de duplicidade econômica nem na agregação; ite
          -m recusado nas validações individuais não entra na duplicidade
          - econômica). Fora dessas exclusões fechadas, nenhuma outra é i
          -nferida.                                                      
       77 -- A **ordem de processamento** (8.1, quando cada regra roda) e
          - a **ordem de apresentação dos motivos** (8.3, como a lista fi
          -nal de motivos de um item é ordenada antes de serializar) são 
          -duas ordens distintas. O acumulador guarda os motivos na ordem
          - em que foram detectados; o compositor (passo 10) os reordena 
          -conforme 8.3 só no momento de montar a saída.                 
       86 +**Como a avaliação de regras funciona (sem mudança de princípi
          +o, só de escopo):**                                           
       87  
       79 -**Fronteiras:**                                               
       88 +- Continua valendo integralmente o texto da versão anterior de
          +ste plano: cada regra roda quando os campos de que depende est
          +ão válidos (matriz 8.2), motivos são acumulados sem descarte p
          +or etapas anteriores, e só as exclusões fechadas de 8.4 interr
          +ompem etapas posteriores.                                     
       89 +- **Nova exclusão relevante (8.4, item 14):** `MOEDA_SEM_COTAC
          +AO` deixa `valor_normalizado` nulo e por isso bloqueia RN-006,
          + RN-009, RN-010 e qualquer teto (RN-011 a RN-015, RN-019) — me
          +smo tratamento de dependência que um campo estruturalmente inv
          +álido já recebia, agora por ausência de dado externo. RN-007/R
          +N-019 (categoria) e RN-008 (competência) **não** dependem de `
          +valor_normalizado` e continuam avaliadas normalmente. Um item 
          +pode, portanto, sair com `MOEDA_SEM_COTACAO` **e** `CATEGORIA_
          +FORA_POLITICA`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`FORA
          +_COMPETENCIA` simultaneamente, mas nunca com `VALOR_NAO_POSITI
          +VO`, `NOTA_FISCAL_AUSENTE`, `DUPLICIDADE` ou motivo de teto.  
       90 +- A **ordem de processamento** (8.1) e a **ordem de apresentaç
          +ão dos motivos** (8.3) continuam sendo duas ordens distintas, 
          +com o mesmo mecanismo de acumulador + reordenação isolada no p
          +asso de composição.                                           
       91  
       81 -- **Núcleo puro** (passos 1 a 11, exceto leitura/escrita de ar
          -quivo): opera inteiramente sobre estruturas em memória. "Puro"
          - aqui significa **sem leitura/escrita de arquivo, sem CLI, sem
          - estado global e sem efeitos colaterais** — não significa ausê
          -ncia física do tipo `JsonNode` no modelo. O `JsonNode` de cada
          - item pode acompanhar o item como fotografia de auditoria de `
          -valor_informado` (ver §6) sem que isso quebre a pureza do núcl
          -eo.                                                           
       82 -- **I/O** (Leitor, Escritor, CLI/Main): isolado nas pontas do 
          -pipeline. O CLI/Main só orquestra — chama o leitor, invoca o n
          -úcleo, chama o escritor, e traduz o resultado (ou a falha) em 
          -código de saída (§3).                                         
       92 +**Fronteiras (sem mudança de princípio):** núcleo puro (passos
          + 2 a 13, sem I/O) e I/O isolado nas pontas (leitores de políti
          +ca/câmbio/despesas, escritor, CLI/Main). O CLI/Main orquestra:
          + valida arquivos externos, invoca o núcleo, escreve o resultad
          +o, traduz falha em código de saída.                           
       93  
       84 -Essa separação entre regra de negócio e CLI/I/O é mantida deli
          -beradamente para a versão atual, sem antecipar ou supor o cont
          -eúdo de qualquer mudança futura de requisito.                 
       85 -                                                              
       94  ---
       95  
       96  ## 3. Contrato da CLI
       97  
       98  ```
       91 -java -jar target/motor-reembolso.jar calcular --input <arquivo
          -> --output <arquivo>                                          
       99 +java -jar target/motor-reembolso.jar calcular --input <entrada
          +.json> --output <saida.json> --politica <politica.json> --camb
          +io <cambio.json>                                              
      100  ```
      101  
       94 -**Códigos de saída:**                                         
      102 +As quatro flags são **normativas de `spec.md` §4.1.1** (AMB-03
          +4) — este plano só descreve como a CLI as implementa, não rede
          +fine o contrato.                                              
      103  
      104 +- **Ordem:** qualquer. **Repetição:** cada flag aparece exatam
          +ente uma vez. **Ausência:** as quatro são obrigatórias.       
      105 +- **Parsing:** o parser de argumentos deixa de ser um `switch`
          + com dois casos fixos e passa a acumular as flags reconhecidas
          + num mapa (`Map<String, String>`), contando ocorrências por ch
          +ave. Ao final: se alguma das quatro chaves obrigatórias está a
          +usente do mapa, se alguma chave aparece mais de uma vez, ou se
          + um argumento não reconhecido (`--xyz`) aparece na linha de co
          +mando → exit `2`, mensagem em stderr, nada em stdout, `--outpu
          +t` preexistente preservado.                                   
      106 +- **Política e câmbio inválidos:** ausência do arquivo, ilegib
          +ilidade, JSON sintaticamente inválido, ou qualquer violação do
          + contrato estrutural fechado de `spec.md` §4.1.1 (política ou 
          +câmbio) → exit `2` — mesma classe de gravidade que argumento a
          +usente/repetido/desconhecido e que `--input` ilegível. Nenhum 
          +código novo é criado para esse caso.                          
      107 +- **Envelope de despesas inválido** (RN-001) continua exit `3`
          +.                                                             
      108 +- **Sucesso** continua exit `0`. Stdout permanece vazio em qua
          +lquer cenário; mensagens de erro vão para stderr.             
      109 +                                                              
      110 +**Códigos de saída (tabela atualizada):**                     
      111 +                                                              
      112  | Código | Significado |
      113  |---|---|
      114  | `0` | Processamento concluído e arquivo de resultado escrito
            em `--output`. |
       99 -| `2` | Erro de uso ou de infraestrutura: argumento ausente, a
          -rquivo de entrada inexistente, arquivo ilegível, JSON sintatic
          -amente inválido, ou falha ao escrever `--output`. |           
      100 -| `3` | JSON sintaticamente legível, mas envelope inválido con
          -forme RN-001 (ex.: `periodo` ausente, `periodo.inicio` posteri
          -or a `periodo.fim`, `despesas` não é lista). |                
      115 +| `2` | Erro de uso ou de infraestrutura: flag ausente/repetid
          +a/desconhecida; arquivo de `--input`, `--politica` ou `--cambi
          +o` inexistente, ilegível, sintaticamente inválido, ou (para po
          +lítica/câmbio) estruturalmente inválido conforme `spec.md` §4.
          +1.1; falha ao escrever `--output`. |                          
      116 +| `3` | JSON sintaticamente legível, mas envelope de despesas 
          +inválido conforme RN-001. |                                   
      117  
      102 -Para os códigos `2` e `3`:                                    
      118 +Nenhum código além de `0`, `2` e `3` é criado nesta versão (co
          +nfirma DT-003/AMB-034).                                       
      119  
      104 -- uma mensagem curta em **texto simples** é escrita em **stder
          -r** — não em stdout, e não como JSON estruturado. Não existe v
          -ocabulário JSON de erro nesta versão porque a spec não define 
          -um; inventar um agora seria vazar decisão de produto para o pl
          -ano sem base na spec.                                         
      105 -- nada é escrito em stdout;                                   
      106 -- o arquivo indicado em `--output` **não é criado nem sobrescr
          -ito** — inclusive quando já existe um resultado anterior nesse
          - caminho, ele permanece intacto.                              
      120 +**Ordem de validação e escrita segura (extensão de DT-010):** 
          +o processo valida política e câmbio primeiro (passo 1 de 8.1);
          + só então lê e valida o envelope de despesas (passo 2); só ent
          +ão processa o núcleo inteiro. O arquivo de destino (`--output`
          +) só é criado ou substituído **depois que toda a apuração e se
          +rialização terminaram com sucesso** — a estratégia de arquivo 
          +temporário no mesmo diretório do destino, seguida de `Files.mo
          +ve` com `ATOMIC_MOVE`/`REPLACE_EXISTING` (DT-010), é preservad
          +a sem alteração e passa a cobrir também as falhas de política/
          +câmbio: qualquer uma delas retorna antes de o processo sequer 
          +tentar abrir um temporário, então um `--output` preexistente n
          +unca é tocado.                                                
      121  
      108 -**A validação do envelope acontece antes de qualquer escrita n
          -o caminho de `--output`.** O leitor primeiro faz o parsing sin
          -tático (falha → código `2`) e o validador de envelope roda em 
          -seguida (falha → código `3`), ambos antes de o processo tocar 
          -no destino. Só depois de o envelope ser confirmado válido o re
          -sultado completo é serializado para um **arquivo temporário no
          - mesmo diretório do destino**; o destino em si só é tocado no 
          -passo final, por substituição atômica (ver DT-010). Uma falha 
          -nesse passo final — caminho não gravável, disco cheio — é infr
          -aestrutura, não regra de negócio, e também cai no código `2`, 
          -com o destino anterior preservado intacto.                    
      109 -                                                              
      122  ---
      123  
      124  ## 4. Modelo de dados
      125  
      114 -Estruturas internas do núcleo (descrição de responsabilidade, 
          -não implementação):                                           
      126 +Estruturas internas do núcleo, estendidas em relação à v1.0 de
          +ste plano (descrição de responsabilidade, não implementação): 
      127  
      128  | Estrutura | Conteúdo |
      129  |---|---|
      118 -| **Item de entrada** | `indiceEntrada` (inteiro base 1, atrib
          -uído antes de qualquer validação, imutável) + `raw` (o `JsonNo
          -de` do elemento original, usado somente como fotografia de aud
          -itoria para `valor_informado`, nunca consultado por regra algu
          -ma). |                                                        
      119 -| **Campos estruturalmente validados** | Um mapa/estrutura com
          - os sete campos canônicos de 4.2, populado apenas para os camp
          -os que passaram na validação de RN-002. Campo inválido fica au
          -sente aqui — nenhuma coerção, nenhum valor padrão. |          
      120 -| **`valor_informado`** | O valor JSON bruto de `despesa.valor
          -` exatamente como recebido (número, texto, booleano, lista, ob
          -jeto ou nulo), extraído do `JsonNode`. Nenhuma regra financeir
          -a o consulta — existe só para reaparecer na saída (4.3). |    
      121 -| **`valor_normalizado`** | `BigDecimal` de escala 2, resultad
          -o de RN-004. Nulo quando `despesa.valor` não é um número estru
          -turalmente válido. |                                          
      122 -| **Categoria normalizada** | Texto resultante de RN-005 (trim
          -, minúsculas, sem acento), produzido **sempre** que `despesa.c
          -ategoria` é estruturalmente válido — inclusive quando o result
          -ado é `coworking` ou qualquer outro valor fora da política. Nu
          -la **apenas** quando `despesa.categoria` é estruturalmente inv
          -álido, ausente ou nulo. RN-007 compara esse texto normalizado 
          -contra o conjunto fechado `alimentacao`/`transporte_urbano`/`h
          -ospedagem` e produz `CATEGORIA_FORA_POLITICA` quando não há co
          -rrespondência — a normalização nunca converte uma categoria de
          -sconhecida em nulo; se convertesse, RN-007 não teria texto alg
          -um para comparar. |                                           
      123 -| **Acumulador de motivos** | Lista de `Motivo`, que só cresce
          - ao longo do pipeline — nenhuma etapa remove um motivo já acum
          -ulado por outra. Reordenada apenas no passo 10, conforme 8.3. 
          -|                                                             
      124 -| **`Motivo`** | Três campos, cada um representado por um tipo
          - fechado (enumeração), não por texto livre espalhado pelo códi
          -go: `codigo` (enumeração com os treze valores de 4.5 — `ITEM_T
          -IPO_INVALIDO`, `CAMPO_AUSENTE`, `CAMPO_TIPO_INVALIDO`, `CAMPO_
          -FORMATO_INVALIDO`, `ID_DUPLICADO`, `VALOR_NAO_POSITIVO`, `CATE
          -GORIA_FORA_POLITICA`, `FORA_COMPETENCIA`, `NOTA_FISCAL_AUSENTE
          -`, `DUPLICIDADE`, `TETO_DIARIO_APLICADO`, `TETO_DIARIO_ESGOTAD
          -O`, `TETO_HOSPEDAGEM_APLICADO`), `regra` (enumeração `RN_001`.
          -.`RN_018`, cada valor carregando o texto canônico `"RN-NNN"`),
          - `campo` (enumeração dos sete nomes canônicos de 4.2, cada val
          -or carregando o texto canônico `"despesa.<campo>"`, mais a pos
          -sibilidade de nulo). A serialização de cada enum para o texto 
          -exigido pela spec é feita num único ponto por enum — não há st
          -ring `"RN-004"` ou `"despesa.valor"` repetida em vários lugare
          -s do código. |                                                
      125 -| **Decisão final** | Enumeração com os quatro valores de 4.4 
          -(`INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO`, `NAO
          -_REEMBOLSADO_TETO_ESGOTADO`, `RECUSADO`). |                   
      126 -| **`valor_reembolsavel`** | `BigDecimal` de escala 2, sempre 
          -`0,00` para item recusado ou esgotado. |                      
      127 -| **Resultado por item** | Agrega `indiceEntrada`, `id` (ou nu
          -lo), `valor_informado`, `valor_normalizado`, `valor_reembolsav
          -el`, decisão final e a lista de motivos já ordenada conforme 8
          -.3. |                                                         
      128 -| **Resultado geral** | `colaborador` (três campos texto-ou-nu
          -lo), `periodo` (competência texto-ou-nulo, início e fim), a li
          -sta de resultados por item na ordem da entrada, e `total_reemb
          -olsavel` (RN-018). |                                          
      130 +| **Item de entrada** | Igual à v1.0: `indiceEntrada` + `raw` 
          +(fotografia de auditoria). |                                  
      131 +| **Campos estruturalmente validados** | Passa a ter **oito** 
          +campos candidatos em vez de sete: os sete já existentes, mais 
          +`moeda` — mas `moeda` é populado de forma diferente dos demais
          + (ver §8, "Campo `despesa.moeda`"): resolve para `"BRL"` quand
          +o a chave está ausente, sem produzir motivo; fica ausente (nul
          +o) quando a chave existe e é estruturalmente inválida (mesma r
          +egra dos demais campos nesse caso). |                         
      132 +| **`valor_informado`** | Sem mudança — continua o valor JSON 
          +bruto de `despesa.valor`. |                                   
      133 +| **Item com câmbio resolvido (novo, passo 5 de 8.1)** | Estru
          +tura intermediária entre "campos validados" e "normalização": 
          +carrega `moedaResolvida` (`"BRL"` ou a moeda estrangeira valid
          +ada), `taxaCambioAplicada` (`1` para BRL, a taxa resolvida par
          +a estrangeira, nulo se `MOEDA_SEM_COTACAO`), `dataCotacaoUtili
          +zada` (nulo para BRL, a data efetivamente usada para estrangei
          +ra, nulo se sem cotação), `valorConvertidoBruto` (o produto `v
          +alor × taxa`, **ainda sem arredondamento** — o arredondamento 
          +é RN-004, que acontece no passo seguinte) e, quando aplicável,
          + o motivo `MOEDA_SEM_COTACAO`. |                              
      134 +| **`valor_normalizado`** | Continua `BigDecimal` de escala 2,
          + mas agora resultado de RN-004 aplicado sobre `valorConvertido
          +Bruto` (BRL: o próprio valor original, já que a taxa é `1`) — 
          +um único arredondamento, nunca dois. Nulo quando `despesa.valo
          +r` é estruturalmente inválido **ou** quando há `MOEDA_SEM_COTA
          +CAO`. |                                                       
      135 +| **Categoria normalizada** | Sem mudança de mecanismo (RN-005
          +) — mas a partir de RN-019 é comparada contra a tabela de polí
          +tica **efetivamente aplicável** (resolvida por centro de custo
          +), não contra um conjunto fixo de três nomes. |               
      136 +| **Tabela de política resolvida (nova)** | Estrutura devolvid
          +a pelo resolutor de centro de custo (§6): a tabela efetivament
          +e aplicável (mapa categoria → `{limite, periodicidade}`), a or
          +igem (`PADRAO` ou o nome do centro cadastrado), e um método de
          + consulta por categoria normalizada que devolve presença/ausên
          +cia, `limite`, `periodicidade` e o motivo correto quando a cat
          +egoria está ausente ou tem `limite` zero (RN-019). |          
      137 +| **Acumulador de motivos** | Sem mudança de mecanismo — só o 
          +vocabulário de `MotivoCodigo` cresce (§10). |                 
      138 +| **`Motivo`** | Mesmos três campos (`codigo`, `regra`, `campo
          +`), mas os três enums fechados que os representam ganham valor
          +es novos: `MotivoCodigo` ganha `MOEDA_SEM_COTACAO`, `CATEGORIA
          +_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, `TETO_INDIVIDUAL_APLICADO` (d
          +ezesseis valores no total); `RegraNegocio` ganha `RN_019` a `R
          +N_022`; `CampoCanonico` ganha `MOEDA`, inserido na posição cor
          +reta da ordem canônica de contrato — entre `VALOR` e `TEM_NOTA
          +_FISCAL` (oito valores no total, mesma técnica de serialização
          + num único ponto — DT-008/DT-019). |                          
      139 +| **Resultado por item** | Ganha três campos novos entre `valo
          +r_informado` e `valor_normalizado`: `moeda`, `taxaCambioAplica
          +da`, `dataCotacaoUtilizada` — os mesmos três valores carregado
          +s pelo "item com câmbio resolvido", propagados sem recálculo a
          +té a composição final. |                                      
      140 +| **Resultado geral** | Sem mudança de forma — `colaborador`, 
          +`periodo`, `resultados`, `total_reembolsavel`. |              
      141  
      142  ---
      143  
      132 -## 5. Como a política é representada                          
      144 +## 5. Arquivo externo: Política de reembolso                  
      145  
      134 -Uma única estrutura simples e imutável, `PoliticaReembolso`, c
          -om quatro valores fixos:                                      
      146 +**Responsabilidade:** um componente de leitura (`LeitorPolitic
          +a`, no pacote `leitor`, ao lado de `ValidadorEnvelope`) que ab
          +re o arquivo de `--politica`, faz parsing JSON via `JsonNode` 
          +(mesma técnica de DT-005), valida **integralmente** o contrato
          + estrutural de `spec.md` §4.1.1 antes de produzir qualquer saí
          +da, e devolve um modelo imutável (`PoliticaExterna`) — ou lanç
          +a uma exceção de arquivo externo inválido, que o `Main` traduz
          + em exit `2`. Não existe caminho pelo qual uma política parcia
          +lmente válida alcance o núcleo: a validação é tudo-ou-nada, co
          +mo a de `ValidadorEnvelope` para o envelope de despesas, mas c
          +om gravidade maior (RN-022 é mais grave que RN-001 — nem o env
          +elope chega a ser lido se política ou câmbio falharem).       
      147  
      148 +**Modelo (`PoliticaExterna`, imutável):**                     
      149 +                                                              
      150  ```
      137 -PoliticaReembolso {                                           
      138 -  limiteDiarioAlimentacao        = 60.00                      
      139 -  limiteDiarioTransporteUrbano   = 80.00                      
      140 -  limiteIndividualHospedagem     = 250.00                     
      141 -  gatilhoNotaFiscal              = 100.00   // estritamente ma
          -ior que                                                       
      151 +PoliticaExterna {                                             
      152 +  vigencia: LocalDate                              // RN-021, 
          +validada e preservada como metadado informativo               
      153 +  moedaBase: String                                // sempre "
          +BRL" após validação                                           
      154 +  notaFiscalObrigatoriaAcimaDe: BigDecimal          // RN-009,
          + dado do arquivo — não mais constante de código               
      155 +  padrao: Map<String, TabelaCategoria>              // pode se
          +r vazio                                                       
      156 +  centrosCusto: Map<String, Map<String, TabelaCategoria>>  // 
          +pode ser vazio; cada valor é uma tabela completa e exclusiva  
      157  }
      158 +                                                              
      159 +TabelaCategoria {                                             
      160 +  limite: BigDecimal                                          
      161 +  periodicidade: Periodicidade   // enum fechado: DIA, DIARIA 
          +— nunca outro valor chega aqui                                
      162 +}                                                             
      163  ```
      164  
      145 -Instanciada uma vez, e passada por construtor comum aos compon
          -entes do núcleo que precisam desses valores (avaliador de regr
          -as individuais, agregador de tetos). Sem mecanismo genérico de
          - regras, sem DSL, sem banco de dados, sem framework de injeção
          - de dependência, sem arquivo externo de configuração. A políti
          -ca tem quatro números fixos definidos pela spec 1.1; mudá-los 
          -é editar uma estrutura, não uma feature. Construir generalidad
          -e para requisitos futuros desconhecidos seria exatamente o exc
          -esso de arquitetura que o FAQ do desafio adverte a evitar.    
      165 +`versao` e `acrescimo_em_viagem_percentual` (e qualquer outro 
          +campo desconhecido) são lidos pelo `JsonNode` bruto só para ef
          +eito de "ignorar sem erro" — não entram no modelo `PoliticaExt
          +erna`, porque nenhuma regra os consome (RN-016 continua sem ef
          +eito; `acrescimo_em_viagem_percentual` nunca ativa comportamen
          +to).                                                          
      166  
      167 +**Regras estruturais que o `LeitorPolitica` aplica antes de de
          +volver o modelo (RN-022, AMB-035):**                          
      168 +                                                              
      169 +- `padrao` e `centros_custo` são objetos obrigatórios, cada um
          + podendo ser vazio.                                           
      170 +- Dentro de `padrao`: todo `limite` deve ser **estritamente ma
          +ior que zero** — `limite: 0` em `padrao` é falha estrutural do
          + arquivo inteiro (não chega a produzir um `TabelaCategoria`, p
          +orque o arquivo inteiro é rejeitado antes).                   
      171 +- Dentro de qualquer tabela de `centros_custo`: `limite` maior
          + ou igual a zero é estruturalmente válido (o `0,00` vira uma d
          +ecisão de negócio — `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` 
          +— não um defeito de arquivo).                                 
      172 +- `periodicidade` deve ser exatamente `"dia"` ou `"diaria"` — 
          +qualquer outro texto invalida o **arquivo**.                  
      173 +- `moeda_base` deve ser exatamente `"BRL"`; `vigencia` deve se
          +r `AAAA-MM-DD` real.                                          
      174 +- Campos desconhecidos dentro de uma categoria ou na raiz são 
          +ignorados sem afetar a validade.                              
      175 +                                                              
      176 +Não existem mais as constantes `60`, `80`, `250`, `100` no cód
          +igo de produção depois desta mudança: `PoliticaExterna` é o ún
          +ico lugar de onde valores financeiros de política se originam,
          + e ela sempre vem de um arquivo (nunca de um literal fixo). A 
          +política histórica equivalente (usada na regressão de `§12.1`/
          +`§12.2`) é uma **fixture externa de teste** — um JSON no mesmo
          + formato, versionado em `tests/resources/` — nunca um valor ha
          +rdcoded nem um fallback interno de produção (ver §16).        
      177 +                                                              
      178  ---
      179  
      149 -## 6. Fronteira entre Jackson e o núcleo de regras            
      180 +## 6. Política por centro de custo                            
      181  
      151 -- A camada de **entrada** (leitor) usa `JsonNode` para reconhe
          -cer o tipo JSON bruto de cada campo — é o que permite distingu
          -ir `CAMPO_AUSENTE` de `CAMPO_TIPO_INVALIDO` de `CAMPO_FORMATO_
          -INVALIDO` (RN-002) e preservar `valor_informado` tal como rece
          -bido, inclusive quando o tipo é inválido.                     
      152 -- O valor monetário efetivamente usado por qualquer regra é se
          -mpre `BigDecimal` — nunca `JsonNode`, nunca `double`.         
      153 -- O `JsonNode` do item pode acompanhar o item ao longo do mode
          -lo **somente** como a fotografia de auditoria que produz `valo
          -r_informado` na saída. Isso não compromete a pureza do núcleo 
          -(§2): pureza aqui é sobre I/O e efeitos colaterais, não sobre 
          -quais tipos aparecem no modelo de dados.                      
      154 -- **Nenhuma regra financeira consulta `valor_informado`.** As 
          -regras (RN-004 em diante) consultam exclusivamente os campos j
          -á validados e normalizados (`valor_normalizado`, categoria nor
          -malizada, etc.).                                              
      155 -- Não será criada uma abstração genérica de "valor JSON" só pa
          -ra eliminar o tipo `JsonNode` do modelo de auditoria — seria c
          -omplexidade extra sem necessidade, dado que o uso do `JsonNode
          -` já está contido a um único propósito (auditoria) e nunca vaz
          -a para decisão financeira.                                    
      182 +**Responsabilidade:** um componente (`ResolutorPoliticaCentroC
          +usto`, no pacote `pipeline`) com uma única função: dado `colab
          +orador.centro_custo` (como veio do envelope — string, nulo, au
          +sente, ou de outro tipo) e a `PoliticaExterna` já validada, re
          +solver **exatamente uma** tabela aplicável.                   
      183  
      184 +- **Centro textual reconhecido** (presente como chave em `cent
          +rosCusto`) → devolve a tabela exclusiva daquele centro, com or
          +igem `CENTRO_CUSTO` e o nome do centro.                       
      185 +- **Centro ausente, nulo, de tipo inválido, ou textual mas des
          +conhecido** → devolve `padrao`, com origem `PADRAO`.          
      186 +- **Comparação:** `String.equals` puro contra as chaves de `ce
          +ntrosCusto` — sem `trim()`, sem `toLowerCase()`, sem normaliza
          +ção de acento, sem correspondência aproximada (RN-019). Isso é
          + uma decisão deliberada de não reaproveitar a normalização de 
          +categoria (RN-005) aqui: os dois campos têm regras de comparaç
          +ão diferentes por design da spec.                             
      187 +- **Nunca mistura:** o resolutor devolve um único `Map<String,
          + TabelaCategoria>` — nunca a união de `padrao` com a tabela do
          + centro. Categorias ausentes da tabela de um centro cadastrado
          + **não** caem de volta em `padrao` (RN-019, RN-007).          
      188 +                                                              
      189 +**Consulta por categoria (método do próprio resolutor ou de um
          + objeto de resultado que ele devolve):** dado a tabela resolvi
          +da e a categoria normalizada, devolve uma de três respostas fe
          +chadas:                                                       
      190 +1. Categoria ausente da tabela → motivo é `CATEGORIA_FORA_POLI
          +TICA` se a origem for `PADRAO`, ou `CATEGORIA_NAO_REEMBOLSAVEL
          +_CENTRO_CUSTO` se a origem for `CENTRO_CUSTO` (RN-007/RN-019 —
          + a distinção de motivo depende só da origem da tabela, nunca d
          +o nome da categoria).                                         
      191 +2. Categoria presente com `limite == 0` → sempre `CATEGORIA_NA
          +O_REEMBOLSAVEL_CENTRO_CUSTO` (só ocorre quando a origem é `CEN
          +TRO_CUSTO`, porque `padrao` com limite zero já foi rejeitado n
          +a leitura do arquivo, §5).                                    
      192 +3. Categoria presente com `limite > 0` → devolve `limite` e `p
          +eriodicidade`, para uso pelo avaliador de regras individuais (
          +elegibilidade) e pelo agregador de tetos (§9).                
      193 +                                                              
      194  ---
      195  
      159 -## 7. Estratégia monetária                                    
      196 +## 7. Arquivo externo: Câmbio                                 
      197  
      161 -- `ObjectMapper` configurado para ler números JSON como `BigDe
          -cimal` exato (habilitar `USE_BIG_DECIMAL_FOR_FLOATS` na leitur
          -a), de modo que a árvore (`JsonNode`) represente números decim
          -ais como `DecimalNode`/`BigDecimal`, nunca como `double`.     
      162 -- Números são obtidos via `decimalValue()` — nunca via `double
          -Value()`.                                                     
      163 -- Nunca construir um `BigDecimal` a partir de um `double` (iss
          -o reintroduziria o erro binário que a configuração acima exist
          -e para evitar).                                               
      164 -- Normalização (RN-004): `valor.setScale(2, RoundingMode.HALF_
          -UP)`.                                                         
      165 -- Toda comparação de valores monetários usa `compareTo` — nunc
          -a `equals` (que também compara escala) nem `==`.              
      166 -- Na serialização de saída, valores monetários são escritos co
          -mo **números JSON decimais em notação simples**, com exatament
          -e duas casas — nunca em notação científica e nunca como string
          -.                                                             
      167 -- **Testes obrigatórios de fronteira monetária** (detalhados n
          -a matriz de rastreabilidade, §9): `33.333` → `33,33`; `33.335`
          - → `33,34`; `33.345` → `33,35`; `100.004` → `100,00`; `100.005
          -` → `100,01`. Este último é o teste-canário que comprova que a
          - leitura é decimal-exata e não passou por `double` em nenhum p
          -onto do caminho.                                              
      198 +**Responsabilidade:** um componente de leitura (`LeitorCambio`
          +, ao lado de `LeitorPolitica`) que abre `--cambio`, faz parsin
          +g e valida o contrato estrutural de `spec.md` §4.1.1, e devolv
          +e um modelo imutável (`TabelaCambio`) — mesma política de tudo
          +-ou-nada e mesma tradução para exit `2` em caso de falha.     
      199  
      200 +**Estrutura real do arquivo** (raiz com `moeda_base`, `fonte`,
          + `observacao`, `taxas` aninhado por data e depois por moeda) é
          + lida e **invertida** para uma estrutura de consulta eficiente
          +:                                                             
      201 +                                                              
      202 +```                                                           
      203 +TabelaCambio {                                                
      204 +  moedaBase: String                                       // s
          +empre "BRL"                                                   
      205 +  cotacoesPorMoeda: Map<String, NavigableMap<LocalDate, BigDec
          +imal>>                                                        
      206 +}                                                             
      207 +```                                                           
      208 +                                                              
      209 +A inversão (de "data → moeda → taxa" para "moeda → data → taxa
          +", com a submapa ordenada por data) é o que torna a consulta d
          +eterminística e eficiente:                                    
      210 +                                                              
      211 +1. **Cotação exata na data da despesa:** `NavigableMap.get(dat
          +a)`.                                                          
      212 +2. **Ausência de cotação exata:** `NavigableMap.floorEntry(dat
          +a)` — a entrada de chave igual ou, na ausência, imediatamente 
          +**anterior**. `floorEntry` nunca devolve uma data posterior, e
          +ntão a garantia "nunca cotação futura" é estrutural (decorre d
          +o próprio método escolhido), não de uma checagem manual que po
          +deria ser esquecida.                                          
      213 +3. **Sem interpolação:** o resolutor não faz nenhuma média nem
          + cálculo entre duas cotações — devolve exatamente o valor de `
          +floorEntry`, ou "sem cotação" quando o resultado é nulo (moeda
          + nunca apareceu em `taxas`, ou a primeira cotação disponível é
          + posterior à data da despesa).                                
      214 +4. **Moeda ausente de `cotacoesPorMoeda`:** resultado "sem cot
          +ação" (`MOEDA_SEM_COTACAO`), o mesmo caminho do item 3.       
      215 +                                                              
      216 +**Regras estruturais que o `LeitorCambio` aplica (RN-022, AMB-
          +035):**                                                       
      217 +                                                              
      218 +- `moeda_base` exatamente `"BRL"`; `taxas` objeto obrigatório,
          + podendo ser `{}` (válido — recusa despesas estrangeiras item 
          +a item via `MOEDA_SEM_COTACAO`, não invalida o arquivo).      
      219 +- Cada chave de `taxas` é uma data `AAAA-MM-DD` real; cada cha
          +ve dentro de uma data casa com `[A-Z]{3}`; cada taxa é estrita
          +mente positiva.                                               
      220 +- `fonte` e `observacao` são lidos e descartados (informativos
          +, nunca usados por regra alguma).                             
      221 +                                                              
      222  ---
      223  
      171 -## 8. Decisões técnicas                                       
      224 +## 8. Campo `despesa.moeda`: representação e estratégia de par
          +sing                                                          
      225  
      173 -### DT-001 — Linguagem e ambiente de execução                 
      226 +Este é o único campo cujo contrato de ausência-de-chave difere
          + de todos os outros sete — e a técnica de parsing já em uso (D
          +T-005, `JsonNode`) já distingue exatamente os dois casos que a
          + spec exige distinguir, sem exigir nenhuma técnica nova:      
      227  
      175 -**Contexto:** prazo de dois dias, desenvolvedor com familiarid
          -ade declarada em Java, ambiente desta máquina já inspecionado 
          -(Java 21.0.2 e Maven 3.9.6 funcionando; Python e Node ausentes
          -).                                                            
      176 -**Decisão:** Java 21 como linguagem única do projeto.         
      177 -**Alternativa descartada:** Python/Node — não instalados nesta
          - máquina, custariam tempo de setup sob prazo curto; Kotlin — m
          -esma JVM, mas sintaxe nova sem necessidade.                   
      178 -**Consequência:** compilar o projeto exige JDK 21 e Maven 3.9+
          - instalados na máquina; executar o JAR já compilado exige apen
          -as um Java 21 (JRE ou JDK), porque o fat jar já contém as depe
          -ndências. Na primeira compilação, o Maven pode precisar baixar
          - dependências (Jackson, JUnit) de um repositório remoto — exig
          -e acesso à rede nessa primeira vez; builds subsequentes reapro
          -veitam o cache local do Maven.                                
      228 +- `elemento.get("moeda")` devolve a referência Java `null` qua
          +ndo a **chave não existe no objeto** — não confundir com `Null
          +Node`, que é o que Jackson devolve quando a chave existe com v
          +alor JSON `null`.                                             
      229 +- `elemento.get("moeda").isNull()` é verdadeiro quando a chave
          + existe com valor `null` — distinto do caso anterior.         
      230  
      180 -### DT-002 — Empacotamento em JAR único via Maven Shade Plugin
      231 +**Estratégia de validação (`validarMoeda`, mesma classe/pacote
          + de `ValidadorItem`, mas com um ramo extra no topo que os dema
          +is campos não têm):**                                         
      232  
      182 -**Contexto:** o contrato de execução exige `java -jar target/m
          -otor-reembolso.jar ...` funcionando sem passos adicionais e se
          -m wrapper de shell.                                           
      183 -**Decisão:** configurar o Maven Shade Plugin para produzir, a 
          -partir de `mvn package`, exatamente `target/motor-reembolso.ja
          -r` — um único artefato contendo todas as dependências (Jackson
          -) e o `Main-Class` no manifesto. Fixar `<finalName>motor-reemb
          -olso</finalName>` para que o nome do artefato não dependa da v
          -ersão do projeto.                                             
      184 -**Alternativa descartada:** `maven-assembly-plugin` (resultado
          - equivalente, Shade é mais direto para este caso de fat jar si
          -mples sem relocations); wrapper `.sh`/`.bat` (descartado por i
          -nstrução explícita — superfície de manutenção extra em duas pl
          -ataformas sem necessidade, já que `java -jar` funciona igual e
          -m qualquer SO com JDK).                                       
      185 -**Consequência:** um único comando de build (`mvn package`) e 
          -um único comando de execução, sem classpath manual, sem script
          - adicional para manter ou testar.                             
      233 +```                                                           
      234 +valor = elemento.get("moeda")                                 
      235 +se valor == null (chave ausente):           → devolve "BRL", n
          +enhum motivo                                                  
      236 +senão se valor.isNull() (chave = null):     → CAMPO_AUSENTE, c
          +ampo = despesa.moeda                                          
      237 +senão se tipo != STRING:                    → CAMPO_TIPO_INVAL
          +IDO, campo = despesa.moeda                                    
      238 +senão se texto não casa com [A-Z]{3}:       → CAMPO_FORMATO_IN
          +VALIDO, campo = despesa.moeda                                 
      239 +                                               (sem trim, sem 
          +conversão de caixa — RN-002)                                  
      240 +senão:                                       → devolve o texto
          + validado                                                     
      241 +```                                                           
      242  
      187 -### DT-003 — Contrato de CLI e códigos de saída               
      243 +Nenhuma desserialização direta para POJO tipado é usada aqui —
          + o mesmo motivo já registrado em DT-005: um acessor permissivo
          + perderia justamente a distinção entre "chave ausente" e "chav
          +e presente com `null`" que este campo exige preservar. A repre
          +sentação intermediária (o retorno de `validarMoeda`) é sempre 
          +uma `String` ou `null` — o "silêncio" da ausência de chave e o
          + "motivo" da presença de `null` são decisões tomadas **antes**
          + desse retorno, nunca depois, para que o restante do pipeline 
          +não precise saber por que o campo é nulo.                     
      244  
      189 -**Contexto:** a interface é fixa (`--input`/`--output`), mas a
          - spec não define o que acontece na CLI quando o processamento 
          -não pode ocorrer — isso é contrato de execução, não regra de n
          -egócio.                                                       
      190 -**Decisão:** três códigos de saída (`0`, `2`, `3`), mensagem e
          -m texto simples em stderr para os códigos de erro, nenhuma esc
          -rita em `--output` quando o código não é `0`, e validação de e
          -nvelope executada antes de qualquer abertura do arquivo de saí
          -da.                                                           
      191 -**Alternativa descartada:** vocabulário de erro em JSON estrut
          -urado (introduziria um esquema que a spec não define); escreve
          -r um JSON de erro no próprio `--output` (arriscaria confundir 
          -"resultado" com "estado de erro" no mesmo arquivo, e violaria 
          -a garantia de não sobrescrever `--output` em falha).          
      192 -**Consequência:** contrato simples e verificável por teste de 
          -CLI; separa claramente "processamento não ocorreu" de "process
          -amento ocorreu com itens recusados" — este último ainda é códi
          -go `0`, porque recusa de item é resultado válido, não falha de
          - processo.                                                    
      245 +---                                                           
      246  
      194 -### DT-004 — `BigDecimal` com parsing decimal-exato           
      247 +## 9. Conversão monetária                                     
      248  
      196 -**Contexto:** a spec exige `100.005 → 100,01`; ponto flutuante
          - binário (`double`) representaria `100.005` como `100.00499999
          -999999...` e arredondaria para o lado errado sob `HALF_UP`.   
      197 -**Decisão:** `BigDecimal` de ponta a ponta, com `ObjectMapper`
          - configurado para `USE_BIG_DECIMAL_FOR_FLOATS`, valores obtido
          -s via `decimalValue()`, nunca via `doubleValue()` ou construçã
          -o a partir de `double`.                                       
      198 -**Alternativa descartada:** `double`/`float` (fonte de bug doc
          -umentada); bibliotecas de dinheiro de terceiros (desnecessária
          -s — `BigDecimal` nativo já cobre a exigência).                
      199 -**Consequência:** os cinco valores de fronteira exigidos (33.3
          -33, 33.335, 33.345, 100.004, 100.005) arredondam corretamente 
          -por construção, não por sorte; qualquer regressão futura que r
          -eintroduza `double` em algum ponto do caminho é detectável pel
          -o teste-canário de 100.005.                                   
      249 +**Fórmula normativa (RN-020, RN-004):** `valor bruto × taxa`, 
          +seguido de **um único** arredondamento `HALF_UP` para duas cas
          +as — nunca dois arredondamentos (um na moeda original, outro a
          +pós a conversão).                                             
      250  
      201 -### DT-005 — Parsing por árvore (`JsonNode`) na camada de entr
          -ada                                                           
      251 +**Componente (estágio novo do pipeline, passo 5 de 8.1 — entre
          + o detector de ID duplicado e o normalizador):** para cada ite
          +m com campos estruturalmente válidos:                         
      252  
      203 -**Contexto:** RN-002 exige distinguir três classes de erro est
          -rutural por campo (ausência, tipo, formato) e 4.3 exige preser
          -var `valor_informado` exatamente como recebido, mesmo com tipo
          - inválido — isso exige inspecionar o JSON bruto, não um objeto
          - já desserializado e coagido.                                 
      204 -**Decisão:** ler cada item como `JsonNode`, inspecionar `JsonN
          -odeType` explicitamente por campo (nunca usar acessores permis
          -sivos como `asBoolean()`/`asInt()`, que fariam coerção que RN-
          -002 proíbe), e só então produzir os campos validados e normali
          -zados que o núcleo consome.                                   
      205 -**Alternativa descartada:** desserialização direta para um obj
          -eto POJO tipado (perderia a distinção entre "campo ausente" e 
          -"campo de tipo errado", e perderia o valor bruto para `valor_i
          -nformado` quando o tipo é inválido).                          
      206 -**Consequência:** classificação estrutural fiel à spec, ao cus
          -to de uma camada de leitura mais verbosa que uma desserializaç
          -ão direta.                                                    
      253 +- `moeda` resolvida (§8) igual a `"BRL"`, ou chave ausente (re
          +solvida para `"BRL"` sem motivo) → taxa implícita `1`, sem con
          +sultar `TabelaCambio`; `data_cotacao_utilizada` fica nula.    
      254 +- `moeda` estrangeira estruturalmente válida → consulta `Tabel
          +aCambio` (§7) pela data da despesa. Cotação resolvida (exata o
          +u anterior) → `valorConvertidoBruto = valor × taxa` (sem arred
          +ondar ainda); `taxaCambioAplicada` e `dataCotacaoUtilizada` pr
          +eenchidos com os valores efetivamente usados. Sem cotação → mo
          +tivo `MOEDA_SEM_COTACAO` (RN-020), `taxaCambioAplicada`/`dataC
          +otacaoUtilizada`/`valorConvertidoBruto` nulos.                
      255 +- `moeda` estruturalmente inválida (`CAMPO_AUSENTE`/`CAMPO_TIP
          +O_INVALIDO`/`CAMPO_FORMATO_INVALIDO`) → item já recusado por R
          +N-002; este estágio não tenta resolver câmbio para ele (mesma 
          +exclusão de dependência de campo inválido já existente).      
      256  
      208 -### DT-006 — Arquitetura em pipeline linear seguindo a seção 8
          -.1                                                            
      257 +O **normalizador** (RN-004, estágio seguinte) aplica `setScale
          +(2, RoundingMode.HALF_UP)` sobre `valorConvertidoBruto` (ou so
          +bre o valor original, para BRL, que é o caso `taxa = 1` sem co
          +nversão real) — produzindo `valor_normalizado`. Nenhum outro p
          +onto do pipeline arredonda.                                   
      258  
      210 -**Contexto:** a ordem de processamento é normativa (seção 8 da
          - spec) e distinta da ordem de apresentação de motivos (8.3); u
          -ma arquitetura que não espelhe isso arrisca produzir resultado
          -s corretos por acidente, não por construção.                  
      211 -**Decisão:** onze estágios lineares, um por passo de 8.1, cada
          - um operando sobre a lista completa de itens sem jamais reorde
          -ná-la; motivos acumulados por item ao longo dos estágios; reor
          -denação para apresentação isolada no passo 10.                
      212 -**Alternativa descartada:** um único método monolítico avalian
          -do tudo por item em qualquer ordem interna conveniente — mais 
          -difícil de auditar linha a linha contra a seção 8, e mais fáci
          -l de violar sem perceber a regra "erros em campos não usados n
          -ão impedem outras regras".                                    
      213 -**Consequência:** cada estágio é testável isoladamente e mapei
          -a 1:1 para uma linha da seção 8.1, o que facilita tanto a impl
          -ementação quanto a auditoria da rastreabilidade.              
      259 +**Saída:** os três campos de auditoria (`moeda`, `taxa_cambio_
          +aplicada`, `data_cotacao_utilizada`) são propagados sem recálc
          +ulo desde este estágio até a composição final (§4) e a seriali
          +zação (§13). Não existe campo `valor_convertido` separado — `v
          +alor_normalizado` já é o valor final em BRL.                  
      260  
      215 -### DT-007 — Representação da política como estrutura imutável
          - simples                                                      
      261 +---                                                           
      262  
      217 -**Contexto:** a política tem quatro valores numéricos fixos ne
          -sta versão da spec.                                           
      218 -**Decisão:** uma estrutura única e imutável, `PoliticaReembols
          -o`, sem mecanismo de configuração externa.                    
      219 -**Alternativa descartada:** motor de regras genérico, DSL, arq
          -uivo de configuração externo, banco de dados, framework de inj
          -eção de dependência — todos resolveriam um problema de flexibi
          -lidade que a spec atual não tem.                              
      220 -**Consequência:** mudar um teto é uma edição de quatro linhas;
          - em troca, qualquer flexibilidade não prevista pela spec 1.1 e
          -xigiria refatoração explícita, o que é aceitável porque não há
          - evidência de que essa flexibilidade seja necessária agora.   
      263 +## 10. Motivos, decisões e ordem de apresentação              
      264  
      222 -### DT-008 — `Motivo` como três enumerações fechadas          
      265 +**Extensão dos vocabulários fechados (DT-008/DT-019, ver §14):
          +**                                                            
      266  
      224 -**Contexto:** 4.5 define um vocabulário fechado para `codigo`,
          - `regra` e `campo`; strings livres repetidas pelo código são u
          -ma fonte comum de divergência de grafia entre o motivo emitido
          - e o exigido pela spec.                                       
      225 -**Decisão:** três enumerações (`MotivoCodigo`, `RegraNegocio`,
          - `CampoCanonico`), cada uma com o texto canônico correspondent
          -e definido em um único lugar.                                 
      226 -**Alternativa descartada:** strings soltas (`"RN-004"`, `"desp
          -esa.valor"`) espalhadas pelas classes que emitem motivos — fun
          -ciona, mas cada ocorrência é uma chance de erro de digitação n
          -ão detectado por compilação.                                  
      227 -**Consequência:** erro de grafia num código de motivo vira err
          -o de compilação, não uma divergência silenciosa só visível em 
          -teste ou na correção.                                         
      267 +- `MotivoCodigo` ganha `MOEDA_SEM_COTACAO`, `CATEGORIA_NAO_REE
          +MBOLSAVEL_CENTRO_CUSTO`, `TETO_INDIVIDUAL_APLICADO` — todos os
          + treze códigos anteriores são preservados sem alteração de nom
          +e ou de serialização.                                         
      268 +- `RegraNegocio` ganha `RN_019` a `RN_022`.                   
      269 +- `CampoCanonico` ganha `MOEDA`, inserida na posição correta d
          +a ordem canônica (entre `VALOR` e `TEM_NOTA_FISCAL`) — isso de
          +sloca `TEM_NOTA_FISCAL` de ordinal 6 para 7 na tabela de desem
          +pate de `CompositorSaida`, mas não afeta a posição relativa do
          +s seis campos anteriores.                                     
      270  
      229 -### DT-009 — Estratégia de testes em três níveis              
      271 +**Ordem de apresentação (8.3), tabela de estágios em `Composit
          +orSaida` estendida:**                                         
      272  
      231 -**Contexto:** a rubrica avalia rastreabilidade `spec → tasks →
          - commits → testes`; cada RN e cada CA precisa de destino verif
          -icável.                                                       
      232 -**Decisão:** testes unitários por regra (maioria), poucos test
          -es de integração de pipeline completo (o arquivo de exemplo e 
          -fixtures adicionais), e um teste de contrato/CLI cobrindo códi
          -gos de saída e comportamento de arquivo. Nomenclatura de class
          -e/método referenciando o `RN-NNN` e o `CA-NNN` correspondentes
          -. Todo teste — inclusive o de integração (`ExemploCompletoTest
          -`) e os de contrato/CLI (`CliContratoTest`, `EscritaAtomicaSai
          -daTest`) — usa o sufixo `*Test`, reconhecido pelo Maven Surefi
          -re por padrão, para que `mvn test` execute a suíte inteira num
          - único comando.                                               
      233 -**Alternativa descartada:** cobertura só por teste de integraç
          -ão ponta a ponta — esconderia qual regra especificamente falho
          -u e dificultaria o grep de rastreabilidade que a rubrica valor
          -iza. Nomear o teste de integração com o sufixo `*IT` (convençã
          -o do Maven Failsafe) — exigiria configurar e invocar um plugin
          - de build adicional (`mvn verify`) só por causa de nomenclatur
          -a, e o teste deixaria de rodar em `mvn test`, contrariando a s
          -implicidade pedida.                                           
      234 -**Consequência:** qualquer regra da spec é localizável no códi
          -go de teste por busca textual do próprio identificador `RN-NNN
          -` ou `CA-NNN`; `mvn test` sozinho executa toda a suíte, sem ex
          -igir um segundo comando ou plugin para os testes de integração
          -/CLI; ver matriz completa em §9.                              
      273 +| Estágio | Motivo(s) |                                       
      274 +|---|---|                                                     
      275 +| 0 | `ITEM_TIPO_INVALIDO` |                                  
      276 +| 1 | Erros de campo estrutural, na ordem canônica (`id, data,
          + categoria, descricao, fornecedor, valor, moeda, tem_nota_fisc
          +al`) |                                                        
      277 +| 2 | `ID_DUPLICADO` |                                        
      278 +| 3 | `MOEDA_SEM_COTACAO` (**novo**) |                        
      279 +| 4 | `VALOR_NAO_POSITIVO` |                                  
      280 +| 5 | `CATEGORIA_FORA_POLITICA` |                             
      281 +| 6 | `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (**novo**) |  
      282 +| 7 | `FORA_COMPETENCIA` |                                    
      283 +| 8 | `NOTA_FISCAL_AUSENTE` |                                 
      284 +| 9 | `DUPLICIDADE` |                                         
      285 +| 10 | Motivos de teto: `TETO_DIARIO_APLICADO`, `TETO_DIARIO_E
          +SGOTADO`, `TETO_HOSPEDAGEM_APLICADO`, `TETO_INDIVIDUAL_APLICAD
          +O` (**novo**) |                                               
      286  
      236 -### DT-010 — Escrita atômica do arquivo de saída              
      287 +**Garantias de coerência (mecanismo já existente em `Composito
          +rSaida`, apenas com tabela maior):**                          
      288  
      238 -**Contexto:** a garantia de que `--output` nunca é criado nem 
          -sobrescrito nos códigos `2` e `3` só é verdadeira se a escrita
          - do resultado nunca tocar o destino antes de o resultado estar
          - completo — escrever progressivamente e diretamente no destino
          - deixaria um arquivo truncado no caminho oficial caso o proces
          -so falhe no meio da escrita.                                  
      239 -**Decisão:** serializar o resultado completo para um arquivo t
          -emporário no mesmo diretório do destino (mesmo sistema de arqu
          -ivos, condição para substituição atômica); fechar e concluir t
          -otalmente a escrita do temporário; só então mover/substituir o
          - destino, preferindo `Files.move(temp, destino, StandardCopyOp
          -tion.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)`. Se o 
          -movimento/substituição falhar, o processo retorna código `2` e
          - o destino anterior permanece intacto; o arquivo temporário é 
          -removido quando possível. O arquivo de destino nunca é aberto 
          -diretamente para escrever o JSON progressivamente.            
      240 -**Alternativa descartada:** abrir `--output` diretamente e esc
          -rever o JSON incrementalmente — mais simples de codar, mas dei
          -xa uma janela em que uma falha no meio da escrita (processo in
          -terrompido, disco cheio) produz um arquivo parcialmente escrit
          -o exatamente no caminho que o resto do contrato promete preser
          -var intacto.                                                  
      241 -**Consequência:** a garantia de não corromper `--output` passa
          - a ser verdadeira por construção, não por sorte de nunca falha
          -r no meio da escrita; o custo é uma etapa extra de escrita-e-m
          -ovimentação em vez de uma escrita única, e a exigência de que 
          -o temporário fique no mesmo diretório do destino para que `ATO
          -MIC_MOVE` seja viável na maioria dos sistemas de arquivos.    
      289 +- **Motivo associado à RN correta:** cada `Motivo` carrega sua
          + própria `RegraNegocio` no ponto onde é criado — a tabela de e
          +stágios ordena por `codigo`, nunca precisa inferir a regra a p
          +artir do estágio. Para `TETO_DIARIO_APLICADO`, a `regra` efeti
          +vamente gravada no motivo difere por categoria (`RN_011` para 
          +`alimentacao`, `RN_012` para `transporte_urbano`, `RN_019` par
          +a qualquer outra categoria com `periodicidade: "dia"` — spec 4
          +.5) — quem decide isso é o agregador de teto (§9 do pipeline, 
          +ver §11 abaixo), não o compositor.                            
      290 +- **`campo` correto:** só os motivos estruturais (estágio 1) e
          + `ID_DUPLICADO` carregam `campo` não nulo — mecanismo inaltera
          +do.                                                           
      291 +- **Ordem determinística dos motivos:** `Comparator` explícito
          + por estágio, nunca por `enum.ordinal()` — mecanismo inalterad
          +o, só a tabela cresce.                                        
      292 +- **Decisão final coerente e um resultado por posição:** mecan
          +ismo de `CompositorSaida` inalterado — cada `indiceEntrada` pr
          +oduz exatamente um `ResultadoItem`, agora com os três campos d
          +e câmbio adicionais.                                          
      293  
      294  ---
      295  
      245 -## 9. Estratégia de testes                                    
      296 +## 11. Tetos por periodicidade                                
      297  
      247 -- **Nível e proporção:** majoritariamente unitário (uma regra 
          -de negócio = um grupo de testes isolado, sem I/O real); um tes
          -te de integração de pipeline completo contra `exemplos/despesa
          -s-exemplo.json`; testes de contrato/CLI cobrindo códigos de sa
          -ída, comportamento de stdout/stderr e a escrita atômica de `--
          -output`.                                                      
      248 -- **Cada `RN-NNN` tem teste?** Garantido pela matriz de rastre
          -abilidade abaixo — todo RN-001 a RN-018 aparece em pelo menos 
          -uma linha; conferência manual cruzada com a spec ao fechar o D
          -ia 1, antes do envelope do Dia 2.                             
      249 -- **Casos de borda da seção 7:** cobertos pelos mesmos grupos 
          -de teste da matriz, via os `CA-NNN` correspondentes, que deriv
          -am diretamente da tabela de casos de borda.                   
      250 -- **Nomenclatura:** classe/grupo nomeado pelo identificador da
          - regra (`RN004NormalizacaoMonetariaTest`); método nomeado pelo
          - cenário e resultado esperado, carregando também o `CA-NNN` ap
          -licável — por nome de método (`rn004_ca009_100_005_arredondaPa
          -raCima_100_01()`), `@DisplayName` (`"RN-004 / CA-009 — 100.005
          - arredonda para 100,01"`) ou comentário imediatamente acima do
          - caso. O objetivo é permitir busca textual direta tanto por `R
          -N-004` quanto por `CA-009` e chegar ao mesmo teste.           
      251 -- **Fixture de saída esperada:** o fixture usado por `ExemploC
          -ompletoTest` é **escrito e revisado manualmente** a partir do 
          -schema completo das seções 4.3 a 4.5 da spec — nunca gerado pe
          -lo próprio motor em teste, o que tornaria o teste circular. Us
          -a a tabela 4.7 como fonte de decisões e valores, e RN-017 mais
          - a ordem de 8.3 para montar o objeto completo de cada motivo (
          -`codigo`, `regra`, `campo`) — a tabela 4.7 é uma representação
          - abreviada (só o `codigo`) e não basta sozinha para montar o f
          -ixture. O fixture contém metadados do envelope, `valor_informa
          -do`, `valor_normalizado`, decisão, motivos completos e `total_
          -reembolsavel`. A comparação é **estrutural** (JSON contra JSON
          -, campo a campo), nunca textual — não depende de espaços, inde
          -ntação ou ordem de chaves na serialização.                    
      252 -- **Comando único de execução:** `mvn test` executa **todos** 
          -os testes planejados desta matriz, inclusive o de integração (
          -`ExemploCompletoTest`) e os de contrato/CLI (`CliContratoTest`
          -, `EscritaAtomicaSaidaTest`) — todos seguem o sufixo `*Test`, 
          -reconhecido pelo Maven Surefire por padrão, em vez do sufixo `
          -*IT` (que exigiria o Maven Failsafe e uma fase de build adicio
          -nal só por causa de nomenclatura). `mvn package` executa esses
          - mesmos testes antes de gerar o JAR, porque a fase `test` prec
          -ede `package` no ciclo de vida padrão do Maven; pular essa eta
          -pa exige a flag explícita e não recomendada `-DskipTests`.    
      298 +**Princípio normativo (RN-019, AMB-036/037):** o mecanismo de 
          +teto é determinado pela `periodicidade` declarada na tabela de
          + política efetivamente aplicável — nunca pelo nome histórico d
          +a categoria. Isso muda o design dos dois agregadores existente
          +s: em vez de um `Set<String>` fixo de categorias (`AgregadorTe
          +toDiario`) e um agregador exclusivo de `hospedagem` (`Agregado
          +rTetoHospedagem`), cada agregador passa a decidir sua aplicabi
          +lidade consultando a `periodicidade` resolvida (§6) para a cat
          +egoria do item, não o nome dela.                              
      299  
      254 -### Matriz de rastreabilidade                                 
      300 +**Periodicidade `"dia"` (generaliza o `AgregadorTetoDiario` at
          +ual):**                                                       
      301  
      256 -| RN / CA | Grupo de teste planejado | Nível |                
      302 +- Saldo **compartilhado** por `(data, categoria normalizada)`,
          + consumido em ordem crescente de `indiceEntrada` — mecanismo j
          +á implementado, agora parametrizado pelo `limite` resolvido pe
          +la tabela de política do centro de custo em vez de uma constan
          +te de `PoliticaReembolso`.                                    
      303 +- Item que excede o saldo disponível recebe o saldo restante (
          +`PARCIALMENTE_REEMBOLSADO`, `TETO_DIARIO_APLICADO`); itens pos
          +teriores ao esgotamento recebem `NAO_REEMBOLSADO_TETO_ESGOTADO
          +`/`TETO_DIARIO_ESGOTADO`.                                     
      304 +- A `regra` do motivo `TETO_DIARIO_APLICADO` é `RN_011` quando
          + a categoria for `alimentacao`, `RN_012` quando for `transport
          +e_urbano`, `RN_019` para qualquer outra categoria sob esse mec
          +anismo (ex.: `representacao`) — uma pequena tabela de exceção 
          +dentro do agregador, não um novo estágio de compositor.       
      305 +                                                              
      306 +**Periodicidade `"diaria"` (generaliza o `AgregadorTetoHospeda
          +gem` atual):**                                                
      307 +                                                              
      308 +- Teto **individual** por lançamento, sem saldo compartilhado.
      309 +- `hospedagem` sob esse mecanismo usa `TETO_HOSPEDAGEM_APLICAD
          +O`/`RN_013`; qualquer outra categoria (ex.: `estacionamento`) 
          +usa `TETO_INDIVIDUAL_APLICADO`/`RN_019` (AMB-037) — mesma lógi
          +ca de pequena tabela de exceção por nome de categoria, desta v
          +ez dentro do agregador individual.                            
      310 +- Nunca produz `NAO_REEMBOLSADO_TETO_ESGOTADO` — não há saldo 
          +compartilhado para esgotar.                                   
      311 +                                                              
      312 +**Exclusões que antecedem o teto (mecanismo já existente, `Sel
          +etorElegiveis`, sem mudança de classe):**                     
      313 +                                                              
      314 +- `limite == 0` numa tabela de centro cadastrado já recusou o 
          +item com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` na etapa de
          + regras individuais (RN-019) — o item nunca chega à população 
          +que os agregadores de teto recebem.                           
      315 +- `limite == 0` em `padrao` nunca chega ao motor: invalida o a
          +rquivo de política inteiro na leitura (§5), antes de qualquer 
          +item ser processado.                                          
      316 +                                                              
      317 +O reaproveitamento do método `aplicarCorte` (já existente em `
          +AgregadorTetoDiario`, documentado como "não conhece categoria,
          + para ser reaproveitável pelo teto individual") continua válid
          +o e é, na prática, o mecanismo que os dois agregadores general
          +izados compartilham — a mesma decisão de design da v1.0 deste 
          +plano, agora exercida por mais categorias.                    
      318 +                                                              
      319 +---                                                           
      320 +                                                              
      321 +## 12. Duplicidade econômica                                  
      322 +                                                              
      323 +**Nova chave (RN-010, política v4):** `data`, categoria normal
          +izada, `moeda` (a efetivamente usada — `"BRL"` quando a chave 
          +está ausente ou quando informada como tal), `valor_normalizado
          +` em BRL (já convertido), `fornecedor` como recebido, `descric
          +ao` como recebida.                                            
      324 +                                                              
      325 +**Extensão de `DetectorDuplicidadeEconomica`:**               
      326 +                                                              
      327 +- `id` e `tem_nota_fiscal` seguem fora da chave — sem mudança.
      328 +- `moeda` entra como componente **adicional** da chave — nunca
          + substitui `valor_normalizado`, que já é o valor convertido. D
          +ois itens com o mesmo valor convertido, mas em moedas diferent
          +es, nunca colidem: a chave de comparação (um `record` ou tupla
          + equivalente) inclui `moeda` como campo próprio, então `equals
          +`/`hashCode` já os distingue sem necessidade de lógica condici
          +onal adicional.                                               
      329 +- Itens com `MOEDA_SEM_COTACAO` têm `valor_normalizado` nulo e
          + por isso já chegam recusados (`RECUSADO`) na etapa anterior —
          + o `SeletorElegiveis` já os exclui da população que entra em `
          +DetectorDuplicidadeEconomica`, mecanismo idêntico ao de qualqu
          +er outra recusa individual, sem necessidade de tratamento espe
          +cial aqui.                                                    
      330 +- Primeira ocorrência por `indiceEntrada` mantida; posteriores
          + recebem `DUPLICIDADE` — mecanismo inalterado.                
      331 +                                                              
      332 +---                                                           
      333 +                                                              
      334 +## 13. Fronteira entre Jackson e o núcleo (extensão)          
      335 +                                                              
      336 +Tudo que a v1.0 deste plano já declarava continua valendo (Jso
          +nNode na entrada para distinguir ausência/tipo/formato; `BigDe
          +cimal` para todo valor efetivamente usado por regra; nenhuma r
          +egra financeira consulta `valor_informado`). A extensão da pol
          +ítica v4:                                                     
      337 +                                                              
      338 +- A mesma técnica de árvore `JsonNode` é usada para os dois ar
          +quivos externos (`LeitorPolitica`, `LeitorCambio`), não só par
          +a o envelope de despesas — é a mesma justificativa de DT-005 a
          +plicada a mais dois arquivos.                                 
      339 +- O 7º campo (`moeda`) introduz o único caso em que a **ausênc
          +ia da chave** e a **presença com `null`** produzem resultados 
          +diferentes (§8) — resolvido inteiramente na camada de leitura,
          + sem vazar a distinção para o núcleo de regras (o núcleo só vê
          + `"BRL"`/moeda válida, ou um motivo já decidido).             
      340 +- `taxaCambioAplicada`/`dataCotacaoUtilizada` seguem o mesmo p
          +rincípio de `valor_informado`: acompanham o item como dado de 
          +auditoria, mas depois do estágio de resolução de câmbio (§9) j
          +á são valores concretos (`BigDecimal`/`LocalDate`), não mais `
          +JsonNode` — a árvore bruta nunca entra na composição final.   
      341 +                                                              
      342 +---                                                           
      343 +                                                              
      344 +## 14. Estratégia monetária (extensão)                        
      345 +                                                              
      346 +Tudo que a v1.0 já declarava (parsing decimal-exato via `USE_B
          +IG_DECIMAL_FOR_FLOATS`, `decimalValue()`, nunca `doubleValue()
          +`/`double`, comparação via `compareTo`, serialização em notaçã
          +o simples) continua valendo sem alteração. Extensão:          
      347 +                                                              
      348 +- A multiplicação `valor × taxa` (RN-020) usa `BigDecimal.mult
          +iply`, preservando a escala plena do produto — o arredondament
          +o para duas casas (`setScale(2, RoundingMode.HALF_UP)`) aconte
          +ce **uma única vez**, depois da multiplicação, nunca antes.   
      349 +- **Teste-canário adicional (planejado, ver §16):** USD `40,00
          + × 5,50 = 220,00` deve produzir exatamente `220,00` num único 
          +arredondamento — o equivalente, para conversão cambial, do tes
          +te-canário `100.005 → 100,01` já existente para RN-004.       
      350 +                                                              
      351 +---                                                           
      352 +                                                              
      353 +## 15. Decisões técnicas                                      
      354 +                                                              
      355 +### DT-001 a DT-006, DT-008 a DT-010 — preservadas sem alteraç
          +ão                                                            
      356 +                                                              
      357 +O texto integral das decisões DT-001 (linguagem/ambiente), DT-
          +002 (empacotamento), DT-004 (`BigDecimal` decimal-exato), DT-0
          +05 (parsing por árvore), DT-006 (pipeline linear seguindo 8.1)
          +, DT-008 (`Motivo` como enumerações fechadas), DT-009 (testes 
          +em três níveis) e DT-010 (escrita atômica) da v1.0 deste plano
          + continua valendo integralmente e não é repetido aqui para não
          + duplicar conteúdo — nenhuma delas foi invalidada pela spec 1.
          +2. DT-005 e DT-006 recebem, respectivamente, o §8 (campo `moed
          +a`) e o §2 (pipeline de treze passos) como **extensão**, não s
          +ubstituição: a técnica e a motivação originais continuam corre
          +tas, só o escopo cresceu.                                     
      358 +                                                              
      359 +### DT-003 — Contrato de CLI e códigos de saída (estendida, nã
          +o superada)                                                   
      360 +                                                              
      361 +**Contexto original:** três códigos de saída, mensagem em stde
          +rr, sem escrita em `--output` em falha (v1.0 deste plano, aind
          +a válido).                                                    
      362 +**Extensão (Dia 2, spec 1.2):** o contrato de CLI passa de dua
          +s para quatro flags obrigatórias (`--input`, `--output`, `--po
          +litica`, `--cambio`), quaisquer ordem, cada uma exatamente uma
          + vez; o código `2` passa a cobrir também flag repetida/desconh
          +ecida e política/câmbio estruturalmente inválidos — casos que 
          +não existiam quando DT-003 foi escrita, porque os arquivos ext
          +ernos não existiam.                                           
      363 +**Por que não é superada:** a decisão de fundo — taxonomia de 
          +três códigos, mensagem simples em stderr, ausência de vocabulá
          +rio JSON de erro, não tocar `--output` em falha — continua cor
          +reta e não precisa ser refeita; só a superfície de validação d
          +e argumentos cresce.                                          
      364 +**Consequência:** o parser de argumentos deixa de ser um `swit
          +ch` de dois casos e passa a validar um conjunto fechado de qua
          +tro chaves obrigatórias sem repetição e sem chave desconhecida
          + (§3).                                                        
      365 +                                                              
      366 +### DT-007 — SUPERADA por DT-011                              
      367 +                                                              
      368 +**Texto original (histórico, preservado para rastreabilidade):
          +** "uma estrutura única e imutável, `PoliticaReembolso`, sem m
          +ecanismo de configuração externa [...] sem arquivo externo de 
          +configuração [...] Construir generalidade para requisitos futu
          +ros desconhecidos seria exatamente o excesso de arquitetura qu
          +e o FAQ do desafio adverte a evitar."                         
      369 +**Por que deixou de valer:** a política v4 (Dia 2) introduz ex
          +atamente a generalidade que DT-007 declarava desnecessária — R
          +N-019 exige política externa, lida de arquivo, variável por ce
          +ntro de custo, com categorias dinâmicas e periodicidade config
          +urável. A premissa de DT-007 ("a política tem quatro valores n
          +uméricos fixos nesta versão da spec") deixou de ser verdadeira
          +; a decisão em si, não só sua consequência, está errada para a
          + spec 1.2.                                                    
      370 +**Requisitos da spec 1.2 que provocaram a substituição:** RN-0
          +19 (política por centro de custo), RN-021 (vigência), RN-022 (
          +processabilidade do arquivo externo) — nenhum deles existia na
          + spec 1.1 que fundamentava DT-007.                            
      371 +**Substituída por:** DT-011.                                  
      372 +**Nota:** esta supersessão já estava registrada do lado da spe
          +c em `DECISIONS.md` (D-003, "DT-007 invalidada"); esta entrada
          + é a materialização formal do lado do `plan.md`, adiada até es
          +ta revisão como o próprio `DECISIONS.md` previa.              
      373 +                                                              
      374 +### DT-011 — Política como modelo externo imutável, resolvido 
          +por centro de custo (substitui DT-007)                        
      375 +                                                              
      376 +**Contexto:** RN-019 exige política lida de arquivo (`--politi
          +ca`), com uma tabela `padrao` e um mapa de tabelas por centro 
          +de custo, cada uma com categorias dinâmicas (`limite` + `perio
          +dicidade`).                                                   
      377 +**Decisão:** `PoliticaExterna` (§5) como estrutura imutável co
          +nstruída inteiramente a partir do `JsonNode` do arquivo de pol
          +ítica, após validação estrutural completa (RN-022) — nunca par
          +cialmente populada. Resolução de tabela por centro de custo is
          +olada num componente próprio (`ResolutorPoliticaCentroCusto`, 
          +§6), que não conhece nem `LeitorPolitica` nem regras de negóci
          +o individuais — só resolve "qual tabela" e "o que essa tabela 
          +diz sobre esta categoria".                                    
      378 +**Alternativa descartada:** motor de regras genérico ou DSL — 
          +RN-019 já é um contrato estrutural fechado e finito (duas peri
          +odicidades, tabela plana categoria→limite), não exige um mecan
          +ismo de regras arbitrário; construir um seria o mesmo excesso 
          +de arquitetura que DT-007 evitava, agora aplicado a um problem
          +a levemente maior, mas ainda finito.                          
      379 +**Consequência:** nenhuma constante financeira (`60`/`80`/`250
          +`/`100`) permanece no código de produção; toda mudança de limi
          +te é edição de um arquivo JSON externo, nunca recompilação.   
      380 +                                                              
      381 +### DT-012 — Leitura e validação de política externa isolada e
          +m componente próprio                                          
      382 +                                                              
      383 +**Contexto:** RN-022 exige que política estruturalmente inváli
          +da impeça toda a apuração, com a mesma severidade de arquivo i
          +legível.                                                      
      384 +**Decisão:** `LeitorPolitica` (pacote `leitor`) segue o mesmo 
          +padrão já estabelecido por `ValidadorEnvelope`: uma função est
          +ática que recebe `JsonNode`, valida every contrato estrutural 
          +de §4.1.1, e devolve `PoliticaExterna` ou lança uma exceção de
          +dicada (`PoliticaInvalidaException`) que o `Main` traduz para 
          +exit `2`.                                                     
      385 +**Alternativa descartada:** validar campo a campo dentro do pr
          +óprio `Main` — replicaria a mistura de responsabilidades (I/O 
          ++ regra de validação) que `ValidadorEnvelope` já evita para o 
          +envelope de despesas.                                         
      386 +**Consequência:** o `Main` ganha mais uma chamada de leitor no
          + início do fluxo, sem crescer em complexidade de validação — a
          + mesma forma de `ValidadorEnvelope.validar(raiz)` já em uso.  
      387 +                                                              
      388 +### DT-013 — Câmbio como tabela invertida para consulta eficie
          +nte por data                                                  
      389 +                                                              
      390 +**Contexto:** RN-020 exige, para cada despesa estrangeira, a c
          +otação exata na data ou a mais recente anterior, nunca futura,
          + sem interpolação — potencialmente muitas consultas (uma por i
          +tem em moeda estrangeira) contra um arquivo estruturado por da
          +ta primeiro, moeda depois.                                    
      391 +**Decisão:** `LeitorCambio` inverte a estrutura do arquivo (da
          +ta→moeda→taxa) para `Map<String, NavigableMap<LocalDate, BigDe
          +cimal>>` (moeda→data→taxa) no momento da leitura, uma única ve
          +z. A consulta usa `NavigableMap.floorEntry(data)`, que devolve
          + a entrada de chave igual ou imediatamente anterior — nunca po
          +sterior — em tempo logarítmico.                               
      392 +**Alternativa descartada:** percorrer `taxas` linearmente a ca
          +da consulta, filtrando datas ≤ data da despesa e escolhendo o 
          +máximo — funcionalmente equivalente, mas repetiria o trabalho 
          +de ordenação a cada chamada em vez de uma única vez na leitura
          +; também exigiria checagem manual de "não é futura", que `floo
          +rEntry` já garante estruturalmente.                           
      393 +**Consequência:** a garantia "nunca cotação futura" decorre da
          + escolha do método (`floorEntry` nunca olha para a frente), nã
          +o de uma condição escrita à mão que poderia ser esquecida ou i
          +nvertida por engano.                                          
      394 +                                                              
      395 +### DT-014 — Parsing de `despesa.moeda` distinguindo ausência 
          +de chave e valor `null`                                       
      396 +                                                              
      397 +**Contexto:** RN-002 exige que a ausência da chave `moeda` res
          +olva silenciosamente para `BRL`, mas que a chave presente com 
          +valor `null` seja `CAMPO_AUSENTE` — o único campo do contrato 
          +com essa assimetria.                                          
      398 +**Decisão:** reaproveitar a semântica já presente em `JsonNode
          +.get(chave)` (DT-005): referência Java `null` quando a chave n
          +ão existe, instância de `NullNode` (`isNull() == true`) quando
          + a chave existe com valor `null`. Um método de validação dedic
          +ado (`validarMoeda`, §8) checa `== null` antes de checar `.isN
          +ull()` — a ordem dos dois `if` é o que preserva a distinção.  
      399 +**Alternativa descartada:** usar `elemento.path("moeda")` (que
          + colapsa ausência e `null` em `MissingNode`) ou um acessor per
          +missivo (`asText()` com valor default) — qualquer um dos dois 
          +destruiria a distinção antes que o código de validação pudesse
          + vê-la.                                                       
      400 +**Consequência:** nenhuma técnica nova é introduzida além da j
          +á usada para os outros sete campos — só a ordem dos `if` e a i
          +nterpretação do primeiro caso (`null` → retorno silencioso em 
          +vez de motivo) mudam.                                         
      401 +                                                              
      402 +### DT-015 — Conversão cambial com arredondamento único (esten
          +de DT-004)                                                    
      403 +                                                              
      404 +**Contexto:** RN-004/RN-020 proíbem dois arredondamentos (um a
          +ntes e um depois da conversão) — só um, depois da multiplicaçã
          +o.                                                            
      405 +**Decisão:** o estágio de resolução de câmbio (§9) produz `val
          +orConvertidoBruto` **sem** chamar `setScale` — só o normalizad
          +or (RN-004), estágio seguinte do pipeline, arredonda. Nenhum p
          +onto intermediário do código chama `setScale`/`round` sobre um
          + valor monetário.                                             
      406 +**Alternativa descartada:** arredondar dentro do próprio resol
          +utor de câmbio, antes de devolver o valor — introduziria o ris
          +co de um segundo arredondamento acidental se o normalizador ta
          +mbém arredondasse (dupla aplicação de `HALF_UP` pode, em casos
          + de fronteira, produzir resultado diferente de uma única aplic
          +ação sobre o produto não arredondado).                        
      407 +**Consequência:** o teste-canário de conversão (USD `40,00 × 5
          +,50 = 220,00`, §14) só passa se exatamente um estágio arredond
          +ar — qualquer regressão que introduza um segundo arredondament
          +o é detectável por esse teste.                                
      408 +                                                              
      409 +### DT-016 — Comparação textual exata de `centro_custo` (forma
          +liza a técnica de RN-019)                                     
      410 +                                                              
      411 +**Contexto:** RN-019 exige comparação exata, sem trim, sem nor
          +malização de caixa ou acento — deliberadamente diferente da no
          +rmalização de categoria (RN-005).                             
      412 +**Decisão:** `ResolutorPoliticaCentroCusto` usa `Map.get(centr
          +oCusto)` diretamente (equivalente a `String.equals`) contra as
          + chaves de `centrosCusto` — nenhuma transformação aplicada ao 
          +valor de `colaborador.centro_custo` antes da consulta.        
      413 +**Alternativa descartada:** reaproveitar a mesma normalização 
          +de RN-005 "por consistência" — rejeitada porque a spec declara
          + explicitamente que a comparação de centro de custo segue regr
          +a própria, distinta da de categoria; unificá-las seria introdu
          +zir comportamento não pedido.                                 
      414 +**Consequência:** `"CC-COMERCIAL"` e `"cc-comercial"` são cent
          +ros de custo diferentes para efeito de resolução de política —
          + coerente com o texto normativo de RN-019.                    
      415 +                                                              
      416 +### DT-017 — Tetos generalizados por periodicidade, não por no
          +me de categoria                                               
      417 +                                                              
      418 +**Contexto:** RN-019/AMB-036/AMB-037 exigem que o mecanismo de
          + teto (compartilhado vs. individual) e o motivo emitido depend
          +am da `periodicidade` configurada, não de uma lista fixa de no
          +mes de categoria — uma política válida pode declarar `represen
          +tacao` como `"dia"` ou `estacionamento` como `"diaria"`, categ
          +orias que não existiam na v1.1.                               
      419 +**Decisão:** os dois agregadores de teto (generalizações de `A
          +gregadorTetoDiario`/`AgregadorTetoHospedagem`, §11) consultam 
          +a `periodicidade` resolvida pelo `ResolutorPoliticaCentroCusto
          +` para decidir a qual mecanismo um item pertence — o `Set<Stri
          +ng>` fixo de categorias do agregador diário atual é removido. 
          +A escolha de `regra`/`codigo` do motivo (`RN_011`/`RN_012`/`RN
          +_019` para `"dia"`; `TETO_HOSPEDAGEM_APLICADO`/`TETO_INDIVIDUA
          +L_APLICADO` para `"diaria"`) continua sendo uma pequena tabela
          + de exceção por nome de categoria **dentro** do agregador — po
          +rque é isso que a spec pede (nomes históricos continuam recebe
          +ndo seus códigos históricos), não uma contradição com a genera
          +lização do mecanismo.                                         
      420 +**Alternativa descartada:** manter dois conjuntos fixos de nom
          +es e simplesmente adicionar `representacao`/`estacionamento` a
          + eles — funcionaria para os quatro cenários do envelope, mas q
          +uebraria na primeira política externa futura que declarasse um
          +a quinta categoria com qualquer periodicidade, exatamente o ti
          +po de acoplamento que RN-019 elimina.                         
      421 +**Consequência:** o agregador de teto passa a receber, por ite
          +m, a `periodicidade` e o `limite` já resolvidos (não os lê de 
          +`PoliticaReembolso`), tornando `AgregadorTetoDiario`/`Agregado
          +rTetoHospedagem` (ou seus sucessores) independentes de qualque
          +r lista de nomes fixa para decidir aplicabilidade.            
      422 +                                                              
      423 +### DT-018 — `CLI` valida quatro flags como conjunto, não como
          + sequência de casos (estende DT-003)                          
      424 +                                                              
      425 +**Contexto:** o contrato de execução (§4.1.1, AMB-034) exige q
          +uatro flags obrigatórias, em qualquer ordem, cada uma exatamen
          +te uma vez, com exit `2` para ausência, repetição ou flag desc
          +onhecida.                                                     
      426 +**Decisão:** o parser acumula argumentos num mapa e valida o c
          +onjunto resultante contra o conjunto fechado `{--input, --outp
          +ut, --politica, --cambio}` **depois** de percorrer todos os ar
          +gumentos — nunca decide por posição.                          
      427 +**Alternativa descartada:** manter o `switch` posicional de do
          +is casos e simplesmente adicionar dois `case` novos — não dete
          +ctaria repetição (`--input a --input b`) nem cobriria a valida
          +ção "exatamente quatro, nem mais nem menos" de forma natural. 
      428 +**Consequência:** `CliContratoTest` ganha casos novos (flag re
          +petida, flag desconhecida, ordem embaralhada) sem exigir um pa
          +rser mais complexo que uma contagem por chave.                
      429 +                                                              
      430 +### DT-019 — Extensão dos três enums fechados de motivo (esten
          +de DT-008)                                                    
      431 +                                                              
      432 +**Contexto:** DT-008 já estabelecia que `codigo`/`regra`/`camp
          +o` são enumerações fechadas, cada uma serializando para o text
          +o canônico num único ponto — a spec 1.2 só acrescenta valores 
          +a esse vocabulário, não muda o mecanismo.                     
      433 +**Decisão:** `MotivoCodigo` ganha três valores (`MOEDA_SEM_COT
          +ACAO`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, `TETO_INDIVI
          +DUAL_APLICADO`); `RegraNegocio` ganha quatro (`RN_019` a `RN_0
          +22`); `CampoCanonico` ganha um (`MOEDA`), inserido na posição 
          +correta da ordem canônica — nenhum enum existente perde um val
          +or ou muda de nome.                                           
      434 +**Alternativa descartada:** nenhuma — é a aplicação direta e s
          +em alternativa do mecanismo já decidido em DT-008 a um vocabul
          +ário maior.                                                   
      435 +**Consequência:** a tabela de estágios de `CompositorSaida` (`
          +ESTAGIO_POR_CODIGO`, `ORDEM_CAMPO`) precisa de entradas novas 
          +para os valores novos — sem isso, o compositor lançaria `Illeg
          +alArgumentException` ("fora do vocabulário fechado de precedên
          +cia") ao encontrar um motivo novo, o que é o comportamento cor
          +reto de falha rápida caso a extensão seja esquecida em algum p
          +onto.                                                         
      436 +                                                              
      437 +---                                                           
      438 +                                                              
      439 +## 16. Regressão e compatibilidade                            
      440 +                                                              
      441 +Não há modo legado, flag especial ou política interna nesta ve
          +rsão. A compatibilidade com o comportamento histórico é determ
          +inada **inteiramente pelos arquivos fornecidos em cada execuçã
          +o** — nunca por um caminho de código diferente:               
      442 +                                                              
      443 +1. **Política externa equivalente à baseline histórica** (`pad
          +rao`: alimentação R$60/dia, transporte R$80/dia, hospedagem R$
          +250/diária; gatilho de nota R$100) processando `exemplos/despe
          +sas-exemplo.json` → `585.43` (`§12.1` da spec, CA-037).       
      444 +2. **Política oficial v4** (`politica-v4.json`) com `CC-ENG-PL
          +ATAFORMA` processando o mesmo arquivo → `351.43` (`§12.2`, CA-
          +038).                                                         
      445 +3. **Rafael Nkemelu**, `CC-COMERCIAL`, moeda estrangeira (`des
          +pesas-envelope.json`) → `1143.26` (`§12.3`).                  
      446 +4. **Dani Okonkwo**, centro de custo desconhecido (`despesas-e
          +nvelope-cc-desconhecido.json`) → `373.76` (`§12.4`).          
      447 +                                                              
      448 +A política histórica do cenário 1 é uma **fixture externa de t
          +este** (um arquivo JSON no formato de `politica-v4.json`, mas 
          +com os quatro valores históricos, mais um `cambio.json` mínimo
          + ou vazio, já que o arquivo de exemplo original não tem despes
          +as em moeda estrangeira) — versionada em `tests/resources/`, n
          +unca uma constante nem um fallback interno de produção. Isso é
          + uma mudança de forma em relação à v1.0 deste plano, onde `Pol
          +iticaReembolso.padrao()` *era* essa mesma baseline histórica, 
          +embutida no código: a partir desta versão, o motor não tem mai
          +s noção de "política padrão de fábrica" — toda execução, inclu
          +sive a de regressão, depende de um `--politica` real.         
      449 +                                                              
      450 +---                                                           
      451 +                                                              
      452 +## 17. Estratégia de testes (planejada — nenhum teste criado n
          +esta tarefa)                                                  
      453 +                                                              
      454 +A estrutura de três níveis de DT-009 (unitário majoritário, po
          +ucos testes de integração de pipeline completo, contrato/CLI) 
          +continua sendo a estratégia adotada. Esta seção só **planeja**
          + a extensão da matriz de rastreabilidade — nenhum arquivo de t
          +este é criado ou modificado nesta tarefa; a criação é trabalho
          + de `tasks.md`, a partir de `T-022`.                          
      455 +                                                              
      456 +**Extensão planejada da matriz de rastreabilidade (nomes de cl
          +asse provisórios, sujeitos a ajuste na task real):**          
      457 +                                                              
      458 +| RN / CA (planejado) | Grupo de teste planejado | Nível |    
      459  |---|---|---|
      258 -| RN-001 · CA-020 | `EnvelopeValidoTest` — período invertido, 
          -`despesas` vazia, bloco `colaborador` malformado tolerado | Un
          -idade |                                                       
      259 -| RN-002 · CA-021 · CA-022 · CA-023 | `ContratoDoItemTest` — `
          -CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` 
          -por campo, `ITEM_TIPO_INVALIDO`, ordem canônica de múltiplos m
          -otivos | Unidade |                                            
      260 -| RN-002 (valor_informado) | `ValorInformadoTest` — preservaçã
          -o do valor bruto para tipos válidos e inválidos (string, boole
          -ano, ausente, elemento não-objeto) | Unidade |                
      261 -| RN-003 · CA-019 | `IdDuplicadoTest` — todas as ocorrências r
          -ecusadas, ID inválido não entra na verificação | Unidade |    
      262 -| RN-004 · CA-009 · CA-018 | `NormalizacaoMonetariaTest` (para
          -metrizado) — `33.333`, `33.335`, `33.345`, `100.004`, `100.005
          -` | Unidade |                                                 
      263 -| RN-005 · CA-015 | `NormalizacaoCategoriaTest` — caixa, acent
          -o, espaço nas pontas; `transporte urbano` não reconhecido | Un
          -idade |                                                       
      264 -| RN-006 · CA-017 | `ValorNaoPositivoTest` — negativo, zero, v
          -alor que normaliza para zero; total do período não reduz | Uni
          -dade |                                                        
      265 -| RN-007 · CA-016 | `CategoriaForaPoliticaTest` — `coworking` 
          -recusado antes de qualquer teto | Unidade |                   
      266 -| RN-008 · CA-011 · CA-012 | `CompetenciaTest` — bordas inclus
          -ivas do período, data fora da janela | Unidade |              
      267 -| RN-009 · CA-008 · CA-009 | `NotaFiscalTest` (parametrizado) 
          -— `100,00` elegível, `100,01` recusado, deslocamento de fronte
          -ira por arredondamento | Unidade |                            
      268 -| RN-010 · CA-013 · CA-014 | `DuplicidadeEconomicaTest` — chav
          -e exata, primeira ocorrência mantida, `100.00`/`100.01` não sã
          -o duplicata | Unidade |                                       
      269 -| RN-011 · RN-012 · CA-004 | `TetoDiarioTest` — agregação por 
          -data e categoria (alimentação e transporte urbano) | Unidade |
      270 -| RN-013 · CA-007 | `TetoHospedagemTest` — teto por lançamento
          -, independente de descrição, duas hospedagens no mesmo dia som
          -ando até R$ 500,00 | Unidade |                                
      271 -| RN-014 · CA-005 | `ReembolsoParcialTest` — corte no teto, nu
          -nca recusa integral por ultrapassagem | Unidade |             
      272 -| RN-015 · CA-006 | `DistribuicaoTetoTest` — consumo de saldo 
          -em ordem de `indice_entrada`, estado `NAO_REEMBOLSADO_TETO_ESG
          -OTADO` distinto de `RECUSADO` | Unidade |                     
      273 -| RN-016 · CA-010 | `RegraViagemEfeitoNuloTest` — troca de des
          -crição/campo `em_viagem` desconhecido não altera resultado, it
          -em único para isolar de RN-010 | Unidade |                    
      274 -| — (campos desconhecidos) | `CamposDesconhecidosTest` — campo
          - fora do contrato em `despesa` e em `colaborador` é ignorado s
          -ilenciosamente | Unidade |                                    
      275 -| RN-017 · CA-002 | `ComposicaoSaidaTest` — toda posição produ
          -z um registro, ordem da entrada preservada, nenhum item desapa
          -rece | Unidade |                                              
      276 -| — (ordem de apresentação, 8.3/8.4) | `OrdemMotivosTest` — os
          - dois exemplos normativos de 8.4 (três motivos simultâneos; ex
          -clusão de nota por valor não positivo) | Unidade |            
      277 -| RN-018 · CA-001 · CA-003 | `TotalPeriodoTest` — soma dos `va
          -lor_reembolsavel` apresentados igual ao total | Unidade/Integr
          -ação |                                                        
      278 -| CA-001 · CA-002 · CA-003 (integral) | `ExemploCompletoTest` 
          -— os 14 itens de `exemplos/despesas-exemplo.json` processados 
          -de ponta a ponta, comparados estruturalmente contra o fixture 
          -esperado descrito acima (escrito à mão a partir de 4.3–4.5, nã
          -o gerado pelo motor); `total_reembolsavel` = R$ 585,43 | Integ
          -ração (executado por `mvn test`) |                            
      279 -| — (contrato de execução) | `CliContratoTest` — código `0` em
          - sucesso; código `2` para argumento ausente/arquivo inexistent
          -e/JSON sintaticamente inválido/falha de escrita; código `3` pa
          -ra envelope inválido; mensagem em stderr, nada em stdout | Con
          -trato/CLI |                                                   
      280 -| — (escrita atômica de `--output`, DT-010) | `EscritaAtomicaS
          -aidaTest` — envelope inválido não altera um arquivo preexisten
          -te em `--output`; JSON sintaticamente inválido não altera um a
          -rquivo preexistente; falha simulada antes da substituição fina
          -l não altera um arquivo preexistente; sucesso substitui o dest
          -ino pelo resultado completo; nenhum arquivo temporário/parcial
          - permanece no caminho oficial de `--output` após qualquer cená
          -rio | Contrato/CLI |                                          
      460 +| RN-021, RN-022 · CA-035, CA-036 | `LeitorPoliticaTest`, `Lei
          +torCambioTest` — arquivo ausente/ilegível/JSON inválido/estrut
          +uralmente inválido → falha; `taxas: {}` válido; `vigencia` aus
          +ente invalida o arquivo | Unidade |                           
      461 +| RN-019 · CA-024 a CA-027, CA-038 a CA-040 | `ResolutorPoliti
          +caCentroCustoTest` — centro cadastrado, desconhecido, ausente,
          + nulo, tipo inválido; comparação textual exata (sem trim/caixa
          +/acento); limite zero em centro cadastrado vs. em `padrao` | U
          +nidade |                                                      
      462 +| RN-020 · CA-028 a CA-034 | `ResolucaoCambioTest` — cotação e
          +xata, fallback para anterior, proibição de futura, `MOEDA_SEM_
          +COTACAO`, arredondamento único | Unidade |                    
      463 +| RN-002 (moeda) · CA-048 | `CampoMoedaTest` — ausência de cha
          +ve vs. `null` explícito vs. tipo inválido vs. formato inválido
          + | Unidade |                                                  
      464 +| RN-009 (atualizada) | `NotaFiscalConvertidaTest` — gatilho a
          +plicado sobre o valor já convertido, não o original | Unidade 
          +|                                                             
      465 +| RN-010 (atualizada) · CA-028 | `DuplicidadeEntreMoedasTest` 
          +— mesma data/categoria/fornecedor/descrição/valor convertido, 
          +moedas diferentes → não são duplicatas | Unidade |            
      466 +| RN-019 (periodicidade) · CA-047, CA-049 | `TetoPorPeriodicid
          +adeTest` — categoria externa com `"dia"` (saldo compartilhado)
          +, categoria externa com `"diaria"` diferente de hospedagem (`T
          +ETO_INDIVIDUAL_APLICADO`) | Unidade |                         
      467 +| — (contrato de execução) · CA-041 a CA-044 | `CliContratoTes
          +t` (estendido) — quatro flags em qualquer ordem, flag repetida
          +/desconhecida, política/câmbio inválidos → exit `2` | Contrato
          +/CLI |                                                        
      468 +| — (regressão) · CA-037, CA-038 | `RegressaoHistoricaTest` / 
          +extensão de `ExemploCompletoTest` — os quatro totais normativo
          +s (`585.43`, `351.43`, `1143.26`, `373.76`) contra as fixtures
          + reais de `exemplos/envelope/` | Integração |                 
      469  
      282 -Todo identificador de RN-001 a RN-018 e de CA-001 a CA-023 apa
          -rece em pelo menos uma linha desta matriz.                    
      470 +Todo identificador novo (`RN-019` a `RN-022`, `CA-024` a `CA-0
          +49`) precisará aparecer em pelo menos uma linha desta matriz q
          +uando as tasks reais forem criadas — a mesma garantia que a v1
          +.0 já dava para `RN-001` a `RN-018`/`CA-001` a `CA-023`.      
      471  
      472  ---
      473  
      286 -## 10. Riscos                                                 
      474 +## 18. Riscos (extensão)                                      
      475  
      476 +Os sete riscos da v1.0 deste plano continuam válidos e não são
          + repetidos aqui. Riscos novos, específicos da política v4:    
      477 +                                                              
      478  | Risco | Probabilidade | O que faço se acontecer |
      479  |---|---|---|
      290 -| Parsing de número via `double` reintroduzido em algum ponto 
          -(ex. troca de biblioteca, refactor apressado) | Média | Teste-
          -canário de `100.005` (RN-004) falha imediatamente e aponta o p
          -onto exato da regressão. |                                    
      291 -| Coerção implícita de tipo pelo Jackson (`asBoolean()`/`asInt
          -()` em vez de checagem explícita de `JsonNodeType`) | Média | 
          -Revisão de código restrita a essa camada de leitura antes de f
          -echar RN-002; teste dedicado por campo cobrindo booleano-como-
          -número e o inverso. |                                         
      292 -| Regra parando no primeiro motivo encontrado (short-circuit i
          -ndevido) | Média | `OrdemMotivosTest` e os testes de RN-002 co
          -m múltiplos motivos (`CA-021`, `CA-023`) capturam isso diretam
          -ente. |                                                       
      293 -| Excesso de arquitetura (motor de regras genérico, configuraç
          -ão externa) sob tentação de "preparar para o Dia 2" | Baixa ma
          -s real — o FAQ do desafio avisa explicitamente contra isso | M
          -anter `PoliticaReembolso` simples (§5) e a arquitetura de pipe
          -line linear (§2); qualquer generalização é adicionada só quand
          -o um requisito real a exigir, nunca antes. |                  
      294 -| Mudança de requisito do Dia 2 exigir tocar código de I/O alé
          -m do núcleo | Desconhecida — não antecipada nesta versão | A f
          -ronteira núcleo/CLI (§2) já isola regra de negócio de I/O; se 
          -a mudança for de regra, o núcleo absorve; se for de contrato d
          -e execução, o CLI absorve. Nenhum conteúdo do Dia 2 é suposto 
          -aqui. |                                                       
      295 -| Teste de integração não ser descoberto pelo Maven por nomenc
          -latura inadequada (ex. sufixo `*IT` sem o Maven Failsafe confi
          -gurado) | Média — foi um problema real encontrado na auditoria
          - deste plano | Todo teste, inclusive integração e CLI, usa o s
          -ufixo `*Test` (DT-009/DT-010); `mvn test` sozinho é o comando 
          -de verificação antes de qualquer commit, nunca um plugin adici
          -onal só por nomenclatura. |                                   
      296 -| Escrita direta e progressiva em `--output` corromper ou trun
          -car o arquivo em caso de falha no meio da serialização | Média
          - | Escrever sempre em arquivo temporário no mesmo diretório do
          - destino e mover atomicamente (DT-010); o destino nunca é aber
          -to diretamente para escrita incremental. |                    
      297 -| Categoria estruturalmente válida, mas fora do vocabulário fe
          -chado, ser descartada (virar nula) antes de RN-007 conseguir c
          -ompará-la | Média — foi um erro real encontrado na auditoria d
          -este plano | Modelo corrigido em §4: a categoria normalizada é
          - sempre produzida quando o campo é estruturalmente válido, inc
          -lusive quando o resultado é `coworking` ou qualquer outro valo
          -r fora da política; só RN-007 decide `CATEGORIA_FORA_POLITICA`
          -. |                                                           
      480 +| Confundir "ausência de chave" com "`null` explícito" no camp
          +o `moeda`, tratando os dois igualmente | Média — é o único cam
          +po do contrato com essa assimetria, fácil de esquecer sob pres
          +são de copiar o padrão dos outros sete campos | `CampoMoedaTes
          +t` cobre os quatro casos (§17) separadamente; DT-014 documenta
          + a ordem exata dos `if` que preserva a distinção. |           
      481 +| Arredondar duas vezes na conversão cambial (uma vez no resol
          +utor de câmbio, outra no normalizador) | Média | Teste-canário
          + `40,00 × 5,50 = 220,00` (§14); DT-015 declara explicitamente 
          +que só um estágio arredonda. |                                
      482 +| Reintroduzir um `Set<String>` fixo de categorias nos agregad
          +ores de teto, quebrando a generalização por `periodicidade` ex
          +igida por RN-019 | Média — os agregadores atuais já têm esse `
          +Set` hardcoded, e generalizá-los é a mudança mais invasiva des
          +ta revisão | DT-017 documenta a decisão; `TetoPorPeriodicidade
          +Test` exercita uma categoria externa (não `alimentacao`/`trans
          +porte_urbano`/`hospedagem`) sob cada periodicidade. |         
      483 +| Misturar `padrao` com a tabela de um centro cadastrado (fall
          +back por categoria) | Média — é o erro mais fácil de cometer a
          +o "ajudar" um centro cadastrado incompleto | RN-019/DT-011 são
          + explícitos: nunca a união das duas tabelas; `ResolutorPolitic
          +aCentroCustoTest` cobre categoria ausente do centro cadastrado
          + mas presente em `padrao`, esperando `CATEGORIA_NAO_REEMBOLSAV
          +EL_CENTRO_CUSTO`, não o limite de `padrao`. |                 
      484 +| `floorEntry` usado incorretamente (ex.: `ceilingEntry`, que 
          +permitiria cotação futura) | Baixa, mas gravidade alta se ocor
          +rer | DT-013 documenta o método exato exigido; `ResolucaoCambi
          +oTest` inclui um caso de data sem cotação exata cuja única cot
          +ação disponível é posterior — deve resultar em `MOEDA_SEM_COTA
          +CAO`, nunca numa cotação futura usada por engano. |           
      485 +| Reintroduzir constantes financeiras (`60`/`80`/`250`/`100`) 
          +em código de produção "temporariamente", sob pressão de prazo 
          +| Baixa mas real — é exatamente o padrão que `PoliticaReembols
          +o.java` já tem hoje | DT-011/§5 são explícitos: toda política 
          +vem de arquivo; a fixture histórica (§16) é externa, nunca emb
          +utida. |                                                      
      486  
      487  ---
      488  
      301 -## 11. Estrutura física do projeto                            
      489 +## 19. Impacto em arquivos                                    
      490  
      303 -Documentada aqui para respeitar a estrutura de entrega exigida
          - pelo `DESAFIO.md` (pasta de nível superior `tests/`, e não a 
          -convenção padrão do Maven `src/test/java`). **Os diretórios nã
          -o são criados nesta etapa** — isto é só planejamento.         
      491 +Nomes e pacotes reais verificados no repositório nesta sessão 
          +(`src/main/java/com/desafio/reembolso/...`). Nenhuma alteração
          + foi feita — esta tabela é só levantamento para orientar `task
          +s.md`.                                                        
      492  
      305 -```                                                           
      306 -sdd-desafio/                                                  
      307 -├── pom.xml                                                   
      308 -├── src/                                                      
      309 -│   └── main/                                                 
      310 -│       └── java/...        # código de produção              
      311 -├── tests/                                                    
      312 -│   ├── java/...             # testes Java (JUnit 5) — RN*Test
          -, CA*Test, ExemploCompletoTest, CliContratoTest, EscritaAtomic
          -aSaidaTest                                                    
      313 -│   └── resources/...        # fixtures de teste, incluindo o 
          -fixture esperado do exemplo completo                          
      314 -└── target/                  # artefatos gerados pelo Maven — 
          -nunca versionado, nunca escrito à mão                         
      315 -```                                                           
      493 +**Criados (novos, ainda não existem):**                       
      494  
      317 -Como `tests/` não é o caminho padrão reconhecido pelo Maven, o
          - `pom.xml` precisa apontar explicitamente para ele:           
      495 +| Arquivo planejado | Papel |                                 
      496 +|---|---|                                                     
      497 +| `leitor/LeitorPolitica.java` | Lê e valida `--politica`, dev
          +olve `PoliticaExterna` (§5). |                                
      498 +| `leitor/LeitorCambio.java` | Lê e valida `--cambio`, devolve
          + `TabelaCambio` (§7). |                                       
      499 +| `modelo/PoliticaExterna.java` | Modelo imutável da política 
          +(§5). |                                                       
      500 +| `modelo/TabelaCategoria.java` | `limite` + `periodicidade` d
          +e uma categoria numa tabela. |                                
      501 +| `modelo/Periodicidade.java` | Enum fechado `DIA`/`DIARIA`. |
      502 +| `modelo/TabelaCambio.java` | Modelo imutável de cotações, já
          + invertido para consulta eficiente (§7). |                    
      503 +| `pipeline/ResolutorPoliticaCentroCusto.java` | Resolve a tab
          +ela aplicável e consulta categorias (§6). |                   
      504 +| `pipeline/ResolutorCambio.java` (ou `ConversorCambio.java`) 
          +| Estágio novo do pipeline: resolve taxa e produz valor conver
          +tido bruto (§9). |                                            
      505  
      319 -- `<build><testSourceDirectory>tests/java</testSourceDirectory
          -></build>` — direciona o compilador de testes para `tests/java
          -` em vez do padrão `src/test/java`.                           
      320 -- `<build><testResources><testResource><directory>tests/resour
          -ces</directory></testResource></testResources></build>` — dire
          -ciona os recursos de teste (fixtures) para `tests/resources`. 
      506 +**Substituídos:**                                             
      507  
      322 -Com essa configuração, `mvn test` continua funcionando exatame
          -nte como esperado — compila e executa tudo em `tests/java`, co
          -m `tests/resources` no classpath de teste — sem exigir que os 
          -testes fiquem em `src/test/java`. Essa é também a razão pela q
          -ual nenhum teste desta spec depende do caminho padrão do Maven
          - para ser descoberto: a descoberta depende só do sufixo `*Test
          -` (via Surefire) e do `testSourceDirectory` configurado, não d
          -a localização convencional.                                   
      508 +| Arquivo | Motivo |                                          
      509 +|---|---|                                                     
      510 +| `modelo/PoliticaReembolso.java` | Estrutura de quatro consta
          +ntes fixas (DT-007) — substituída por `PoliticaExterna` + `Tab
          +elaCategoria` (DT-011). Todo consumidor atual (`AvaliadorRegra
          +sIndividuais`, `AgregadorTetoDiario`) precisa trocar a fonte d
          +o limite/gatilho. |                                           
      511 +                                                              
      512 +**Estendidos (arquivo existente, lógica adicionada sem reescri
          +ta total):**                                                  
      513 +                                                              
      514 +| Arquivo | Extensão |                                        
      515 +|---|---|                                                     
      516 +| `Main.java` | Parser de argumentos para quatro flags (§3, DT
          +-018); chamadas a `LeitorPolitica`/`LeitorCambio` antes de `Va
          +lidadorEnvelope`. |                                           
      517 +| `modelo/ItemValidado.java` | Campo `moeda` (String, resolvid
          +o conforme §8) e possivelmente os três campos de câmbio, se de
          +cidido carregá-los no mesmo registro em vez de numa estrutura 
          +intermediária separada (decisão de implementação, não de plano
          +). |                                                          
      518 +| `pipeline/ValidadorItem.java` | Método `validarMoeda` (§8, D
          +T-014). |                                                     
      519 +| `pipeline/Normalizador.java` | Passa a normalizar sobre `val
          +orConvertidoBruto`, não sobre `despesa.valor` bruto, quando há
          + conversão (§9). |                                            
      520 +| `pipeline/AvaliadorRegrasIndividuais.java` | RN-019 (categor
          +ia via `ResolutorPoliticaCentroCusto`, não conjunto fixo), RN-
          +009 (gatilho de `PoliticaExterna`, não `PoliticaReembolso`), e
          +xclusão por `MOEDA_SEM_COTACAO`. |                            
      521 +| `pipeline/DetectorDuplicidadeEconomica.java` | Chave estendi
          +da com `moeda` (§12). |                                       
      522 +| `pipeline/AgregadorTetoDiario.java` | Generalização por `per
          +iodicidade` em vez de `Set<String>` fixo (§11, DT-017); limite
          + vindo da tabela resolvida, não de `PoliticaReembolso`. |     
      523 +| `pipeline/AgregadorTetoHospedagem.java` | Generalização para
          + qualquer categoria com `periodicidade: "diaria"`, com `TETO_I
          +NDIVIDUAL_APLICADO` para categorias diferentes de `hospedagem`
          + (§11, DT-017). |                                             
      524 +| `pipeline/CompositorSaida.java` | Três campos novos no `Resu
          +ltadoItem`; `ESTAGIO_POR_CODIGO`/`ORDEM_CAMPO` estendidos (§10
          +, DT-019). |                                                  
      525 +| `escritor/EscritorResultado.java` | Serialização de `moeda`,
          + `taxa_cambio_aplicada`, `data_cotacao_utilizada` (§9). |     
      526 +| `modelo/MotivoCodigo.java` | Três valores novos (§10, DT-019
          +). |                                                          
      527 +| `modelo/RegraNegocio.java` | Quatro valores novos (§10, DT-0
          +19). |                                                        
      528 +| `modelo/CampoCanonico.java` | Um valor novo, `MOEDA`, na pos
          +ição correta (§10, DT-019). |                                 
      529 +                                                              
      530 +**Mantidos sem alteração:**                                   
      531 +                                                              
      532 +| Arquivo | Por quê |                                         
      533 +|---|---|                                                     
      534 +| `leitor/ValidadorEnvelope.java` | RN-001 não muda na spec 1.
          +2. |                                                          
      535 +| `modelo/Envelope.java` | `colaborador.centro_custo` já exist
          +e e já é preservado — só passa a ser *usado* por um componente
          + novo (`ResolutorPoliticaCentroCusto`), não a mudar de forma. 
          +|                                                             
      536 +| `modelo/Decisao.java` | Vocabulário de decisão (4.4) não mud
          +a. |                                                          
      537 +| `pipeline/DetectorIdDuplicado.java` | RN-003 não muda. |    
      538 +| `pipeline/SeletorElegiveis.java` | Mecanismo de seleção de e
          +legíveis não muda — só passa a filtrar mais motivos de recusa 
          +possíveis. |                                                  
      539 +| `pipeline/SomadorTotal.java` | RN-018 não muda. |           
      540 +| Estrutura `tests/java` / `tests/resources`, `pom.xml` (`test
          +SourceDirectory`, `testResources`) | DT-009/DT-010 preservadas
          + — nenhuma mudança de convenção de teste é exigida pela políti
          +ca v4. |                                                      
      541 +                                                              
      542 +---                                                           
      543 +                                                              
      544 +## 20. Item C — fora de escopo (confirmação técnica)          
      545 +                                                              
      546 +A fila de aprovação manual (item C do comunicado, `AGUARDANDO_
          +APROVACAO` para reembolsável acima de R$500) permanece fora de
          + escopo nesta revisão, confirmando `spec.md` §3/AMB-033: nenhu
          +m novo estado de decisão, nenhuma fila, nenhum serviço de apro
          +vação e nenhuma task são planejados para ela nesta versão do p
          +lano. Caso venha a ser especificada em versão futura, exigirá 
          +pelo menos um novo valor em `Decisao` (hoje fechado em quatro 
          +valores) e um componente de fila — nenhum dos dois é antecipad
          +o ou esboçado aqui.                                           
      547 +                                                              
      548 +---                                                           
      549 +                                                              
      550 +## 21. Preparação para tasks (blocos técnicos futuros, sem num
          +eração)                                                       
      551 +                                                              
      552 +`tasks.md` não é alterado nesta tarefa e continuará a numeraçã
          +o a partir de `T-022`. Os blocos abaixo descrevem, em nível té
          +cnico, o que cada task futura precisará cobrir — sem atribuir 
          +números, sem estimar commits e sem criar arquivo de teste algu
          +m:                                                            
      553 +                                                              
      554 +- **CLI:** estender `Main.java` para quatro flags (§3, DT-018)
          +; testes de exit `2` para flag ausente/repetida/desconhecida e
          + para política/câmbio inválidos.                              
      555 +- **Política externa:** `LeitorPolitica` + `PoliticaExterna` +
          + `TabelaCategoria` + `Periodicidade` (§5, DT-011/DT-012); test
          +es de estrutura válida/inválida, incluindo `limite: 0` em `pad
          +rao` vs. em `centros_custo`.                                  
      556 +- **Câmbio externo:** `LeitorCambio` + `TabelaCambio` invertid
          +a (§7, DT-013); testes de cotação exata, fallback anterior, pr
          +oibição de futura, `taxas: {}`.                               
      557 +- **Contrato do campo `moeda`:** `validarMoeda` em `ValidadorI
          +tem` (§8, DT-014); testes dos quatro casos (ausente, `null`, t
          +ipo inválido, formato inválido).                              
      558 +- **Conversão:** estágio novo do pipeline entre detector de ID
          + duplicado e normalizador (§9, DT-015); teste-canário de arred
          +ondamento único.                                              
      559 +- **Resolução por centro de custo:** `ResolutorPoliticaCentroC
          +usto` (§6, DT-016); testes de centro cadastrado/desconhecido/a
          +usente/nulo/tipo inválido e de comparação textual exata.      
      560 +- **Periodicidade e tetos:** generalização de `AgregadorTetoDi
          +ario`/`AgregadorTetoHospedagem` (§11, DT-017); testes de categ
          +oria externa sob cada periodicidade.                          
      561 +- **Novos motivos e saída:** extensão de `MotivoCodigo`/`Regra
          +Negocio`/`CampoCanonico`, `CompositorSaida`, `EscritorResultad
          +o` (§10, §4, DT-019); testes de ordem de apresentação com os m
          +otivos novos.                                                 
      562 +- **Regressões:** fixture histórica externa (§16) e os quatro 
          +cenários financeiros do envelope como testes de integração.   
      563 +- **Integração:** teste ponta a ponta com `politica-v4.json` +
          + `cambio.json` + os dois arquivos de despesas do envelope, com
          +parando contra `§12` da spec.                                 
      564 +- **Documentação:** atualização do `README.md` com o novo cont
          +rato de CLI de quatro flags (trabalho de task, não desta taref
          +a).                                                           
      565 +                                                              
      566 +---                                                           
      567 +                                                              
      568 +## 22. Estrutura física do projeto                            
      569 +                                                              
      570 +Sem alteração em relação à v1.0 deste plano — `pom.xml`, `src/
          +main/java`, `tests/java`, `tests/resources`, `target/`. Os doi
          +s arquivos de política/câmbio (`--politica`, `--cambio`) são a
          +rgumentos de linha de comando, não arquivos de projeto fixos —
          + nenhuma nova pasta de nível superior é exigida pela política 
          +v4. As fixtures de teste da política histórica e dos cenários 
          +do envelope (§16, §17) pertencem a `tests/resources/`, seguind
          +o a mesma convenção já configurada em `pom.xml` (`testResource
          +s` apontando para `tests/resources`).                         

● Now running the required verification commands.

  Ran 1 shell command (ctrl+o to expand)

● Verificação concluída — apenas plan.md foi alterado; git diff --check não
  apontou erro (só o aviso padrão de CRLF do Windows); spec, DECISIONS, tasks,
  README e src/ estão com diff vazio. Nenhum git add/commit/push//export foi
  executado.

  1. Versão/status final: 1.1 · em revisão · baseado na spec 1.2 (aprovado) ·
  última alteração 2026-08-05.

  2. DTs: DT-007 marcada SUPERADA (substituída por DT-011, com o texto histórico
  preservado e os requisitos RN-019/RN-021/RN-022 citados como gatilho).
  DT-003, DT-005 e DT-006 marcadas como estendidas (não superadas — a decisão de
  fundo continua válida, só o escopo cresceu). Novas: DT-011 a DT-019 (política
  externa por centro de custo, leitura de política, câmbio invertido para
  consulta eficiente, parsing de moeda, arredondamento único na conversão,
  comparação textual exata de centro de custo, tetos generalizados por
  periodicidade, CLI de quatro flags como conjunto, extensão dos três enums
  fechados).

  3. Arquitetura política/câmbio: LeitorPolitica/LeitorCambio (padrão
  ValidadorEnvelope) produzindo modelos imutáveis
  (PoliticaExterna+TabelaCategoria+Periodicidade; TabelaCambio já invertida para
  moeda→NavigableMap<data,taxa> com floorEntry); ResolutorPoliticaCentroCusto
  isolando a resolução de tabela e a consulta por categoria.

  4. Pipeline final: 13 passos (validar arquivos externos → envelope → item
  incl. moeda → ID duplicado → resolução de câmbio/conversão → normalização →
  regras individuais incl. RN-019 → seleção → duplicidade → seleção → tetos por 
  periodicidade → composição incl. campos de câmbio → total).

  5. Regressão: os quatro totais (585.43/351.43/1143.26/373.76) documentados
  como verificação normativa; a baseline histórica passa a ser fixture externa
  de teste, nunca constante/fallback interno — PoliticaReembolso.java deixa de
  existir como fonte de valores financeiros.

  6. Impacto em arquivos: tabela completa no plan.md §19 — criados
  (LeitorPolitica, LeitorCambio, PoliticaExterna, TabelaCategoria,
  Periodicidade, TabelaCambio, ResolutorPoliticaCentroCusto, resolutor de
  câmbio), substituído (PoliticaReembolso.java), estendidos (Main, ItemValidado,
  ValidadorItem, Normalizador, AvaliadorRegrasIndividuais,
  DetectorDuplicidadeEconomica, os dois agregadores de teto, CompositorSaida,
  EscritorResultado, os três enums), mantidos (ValidadorEnvelope, Envelope,
  Decisao, DetectorIdDuplicado, SeletorElegiveis, SomadorTotal, estrutura de
  testes).

  7. git status --short:
   M specs/001-motor-reembolso/plan.md

  Parando para revisão.

✻ Worked for 8m 27s

※ recap: We updated plan.md to align the Dia 2 technical plan with spec 1.2 (political v4, câmbio, DT-007 superseded by DT-011-19), and only that file changed. Next: waiting on your review before any commit. (disable recaps in /config)

> Faça uma correção estrutural final do plano técnico.                          
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  ```text                                                                       
  specs/001-motor-reembolso/plan.md                                             
  ```                                                                           
                                                                                
  Não altere:                                                                   
                                                                                
  * `spec.md`;                                                                  
  * `DECISIONS.md`;                                                             
  * `tasks.md`;                                                                 
  * README;                                                                     
  * código;                                                                     
  * testes;                                                                     
  * fixtures;                                                                   
  * arquivos de `exemplos/envelope/`.                                           
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  Mantenha:                                                                     
                                                                                
  ```text                                                                       
  Versão: 1.1                                                                   
  Status: em revisão                                                            
  Baseado na spec: 1.2 (aprovado)                                               
  Última alteração: 2026-08-05                                                  
  ```                                                                           
                                                                                
  Não crie novas regras de negócio, AMB, RN ou CA.                              
                                                                                
  ## 1. Recupere o histórico completo da versão 1.0                             
                                                                                
  Antes de editar, leia a versão anterior do plano diretamente do último        
  commit:                                                                       
                                                                                
  ```powershell                                                                 
  git show HEAD:specs/001-motor-reembolso/plan.md                               
  ```                                                                           
                                                                                
  Use essa versão como fonte histórica.                                         
                                                                                
  O `plan.md` atual precisa ser autossuficiente. Não use frases como:           
                                                                                
  ```text                                                                       
  o texto integral não é repetido aqui                                          
  ```                                                                           
                                                                                
  quando o próprio arquivo já não contém esse texto.                            
                                                                                
  ### Decisões técnicas                                                         
                                                                                
  Restaure no documento o texto integral original de:                           
                                                                                
  * DT-001;                                                                     
  * DT-002;                                                                     
  * DT-003;                                                                     
  * DT-004;                                                                     
  * DT-005;                                                                     
  * DT-006;                                                                     
  * DT-007;                                                                     
  * DT-008;                                                                     
  * DT-009;                                                                     
  * DT-010.                                                                     
                                                                                
  Não duplique identificadores.                                                 
                                                                                
  Organize assim:                                                               
                                                                                
  * DT-001, DT-002 e DT-004: texto original preservado integralmente;           
  * DT-003, DT-005, DT-006, DT-008, DT-009 e DT-010: texto original integral    
  seguido de uma subseção `Extensão — Dia 2`, com as mudanças da spec 1.2;      
  * DT-007: texto original integral preservado e claramente marcado como        
  `SUPERADA por DT-011`;                                                        
  * DT-011 a DT-019: preservar as novas decisões já escritas, após as           
  anteriores.                                                                   
                                                                                
  O leitor deve conseguir compreender todo o histórico sem consultar Git ou uma 
  versão antiga do arquivo.                                                     
                                                                                
  ## 2. Restaure e atualize a estratégia completa de testes                     
                                                                                
  Restaure a estratégia e a matriz completas da versão 1.0, que cobriam:        
                                                                                
  ```text                                                                       
  RN-001 a RN-018                                                               
  CA-001 a CA-023                                                               
  ```                                                                           
                                                                                
  Depois atualize as linhas afetadas pela política externa e acrescente as      
  linhas da spec 1.2.                                                           
                                                                                
  A matriz final deve cobrir, no mesmo documento:                               
                                                                                
  ```text                                                                       
  RN-001 a RN-022                                                               
  CA-001 a CA-049                                                               
  ```                                                                           
                                                                                
  Nenhuma RN ou CA pode depender apenas da frase “continua valendo”.            
                                                                                
  Corrija especificamente:                                                      
                                                                                
  * duplicidade entre moedas diferentes corresponde a `CA-033`, não `CA-028`;   
  * `CA-028` deve cobrir que moeda estrangeira não implica viagem nem amplia    
  teto;                                                                         
  * `CA-045` deve aparecer nos testes estruturais da política;                  
  * `CA-046` deve aparecer nos testes estruturais do câmbio;                    
  * os quatro cenários financeiros de integração devem citar `CA-037`,          
  `CA-038`, `CA-039` e `CA-040`;                                                
  * retire `CA-038` a `CA-040` da linha exclusivamente unitária de              
  `ResolutorPoliticaCentroCustoTest`;                                           
  * `CA-039` e `CA-040` são cenários de integração, não testes unitários do     
  resolutor;                                                                    
  * inclua teste de que `MOEDA_SEM_COTACAO` carrega `campo = despesa.moeda`;    
  * inclua teste de coexistência de `MOEDA_SEM_COTACAO` com motivo de categoria 
  e/ou `FORA_COMPETENCIA`.                                                      
                                                                                
  Ao final da matriz, declare somente após conferir:                            
                                                                                
  ```text                                                                       
  Todo identificador de RN-001 a RN-022 e CA-001 a CA-049 aparece em pelo menos 
  uma linha.                                                                    
  ```                                                                           
                                                                                
  ## 3. Corrija o contrato de `campo` dos motivos                               
                                                                                
  Na seção de motivos e composição, substitua a afirmação de que apenas motivos 
  estruturais e `ID_DUPLICADO` têm campo não nulo.                              
                                                                                
  O contrato correto é:                                                         
                                                                                
  * erros estruturais de campo carregam o campo correspondente;                 
  * `ID_DUPLICADO` carrega `CampoCanonico.ID`;                                  
  * `MOEDA_SEM_COTACAO` carrega `CampoCanonico.MOEDA`, serializado como         
  `despesa.moeda`;                                                              
  * os demais motivos não estruturais carregam `campo` nulo;                    
  * `ITEM_TIPO_INVALIDO` continua com `campo` nulo.                             
                                                                                
  Atualize:                                                                     
                                                                                
  * modelo de dados;                                                            
  * seção de motivos;                                                           
  * DT-019;                                                                     
  * impacto em `CompositorSaida`;                                               
  * estratégia de testes.                                                       
                                                                                
  ## 4. Corrija a explicação de Jackson                                         
                                                                                
  Preserve a decisão de usar:                                                   
                                                                                
  ```java                                                                       
  elemento.get("moeda")                                                         
  ```                                                                           
                                                                                
  Mas remova a afirmação de que `path("moeda")` colapsa ausência e `null`.      
                                                                                
  Registre corretamente:                                                        
                                                                                
  * `get("moeda")` retorna referência Java `null` quando a chave não existe;    
  * `get("moeda")` retorna `NullNode` quando a chave existe com valor JSON      
  `null`;                                                                       
  * `path("moeda")` retornaria `MissingNode` para chave ausente e `NullNode`    
  para `null` explícito, portanto também permitiria distinguir os casos por     
  `isMissingNode()` e `isNull()`;                                               
  * `get()` é escolhido por coerência com a camada existente e por tornar o     
  ramo de chave ausente explícito antes de acessar o nó;                        
  * acessores permissivos com valor padrão continuam proibidos porque podem     
  ocultar a distinção.                                                          
                                                                                
  ## 5. Substitua o falso teste-canário de conversão                            
                                                                                
  Mantenha:                                                                     
                                                                                
  ```text                                                                       
  USD 40,00 × 5,50 = 220,00                                                     
  ```                                                                           
                                                                                
  como exemplo funcional de `CA-031`, mas não o chame de teste-canário contra   
  arredondamento prematuro ou duplo.                                            
                                                                                
  Adicione um teste-canário que realmente diferencie a ordem:                   
                                                                                
  ```text                                                                       
  valor bruto = 1.005                                                           
  taxa = 1.005                                                                  
  produto exato = 1.010025                                                      
  resultado correto, arredondando somente depois da multiplicação = 1.01        
  resultado incorreto, arredondando o valor bruto antes da multiplicação:       
  1.01 × 1.005 = 1.01505 → 1.02                                                 
  ```                                                                           
                                                                                
  Registre com precisão:                                                        
                                                                                
  * esse caso detecta arredondamento prematuro antes da multiplicação;          
  * repetir `setScale(2, HALF_UP)` sobre um valor já arredondado pode ser       
  idempotente e não é necessariamente detectável por um teste de saída;         
  * a garantia de existir apenas um ponto de arredondamento também depende da   
  responsabilidade arquitetural de DT-015 e de revisão do código.               
                                                                                
  Atualize §14, DT-015, riscos e estratégia de testes.                          
                                                                                
  ## 6. Feche as decisões arquiteturais que ficaram abertas                     
                                                                                
  Não deixe alternativas “ou” ou “possivelmente” para `tasks.md`.               
                                                                                
  Adote definitivamente:                                                        
                                                                                
  ```text                                                                       
  pipeline/ResolutorCambio.java                                                 
  ```                                                                           
                                                                                
  Não use `ConversorCambio.java` como alternativa.                              
                                                                                
  Adote também definitivamente que `modelo/ItemValidado.java` será estendido    
  com:                                                                          
                                                                                
  * `moeda`;                                                                    
  * `taxaCambioAplicada`;                                                       
  * `dataCotacaoUtilizada`;                                                     
  * `valorConvertidoBruto`.                                                     
                                                                                
  O estágio `ResolutorCambio` enriquece esse modelo antes de `Normalizador`.    
                                                                                
  Não crie uma estrutura intermediária alternativa nesta versão do plano.       
                                                                                
  Para o teto individual, adote o nome semântico:                               
                                                                                
  ```text                                                                       
  pipeline/AgregadorTetoIndividual.java                                         
  ```                                                                           
                                                                                
  Esse novo componente substitui `AgregadorTetoHospedagem.java`, porque passa a 
  processar qualquer categoria com periodicidade `"diaria"`.                    
                                                                                
  Registre no impacto:                                                          
                                                                                
  * `AgregadorTetoIndividual.java` como criado;                                 
  * `AgregadorTetoHospedagem.java` como substituído;                            
  * `AgregadorTetoDiario.java` como estendido.                                  
                                                                                
  ## 7. Feche a API dos leitores externos                                       
                                                                                
  Hoje o documento alterna entre “abrir o arquivo” e “função que recebe         
  `JsonNode`”.                                                                  
                                                                                
  Adote contratos únicos:                                                       
                                                                                
  ```java                                                                       
  LeitorPolitica.ler(Path caminho): PoliticaExterna                             
  LeitorCambio.ler(Path caminho): TabelaCambio                                  
  ```                                                                           
                                                                                
  Cada leitor é responsável por:                                                
                                                                                
  1. abrir o arquivo;                                                           
  2. fazer parsing sintático com Jackson;                                       
  3. validar o contrato estrutural completo;                                    
  4. construir o modelo imutável;                                               
  5. lançar a exceção dedicada em qualquer falha.                               
                                                                                
  Métodos privados internos podem receber `JsonNode`, mas essa não é a API      
  pública planejada.                                                            
                                                                                
  Corrija também o texto:                                                       
                                                                                
  ```text                                                                       
  valida every contrato                                                         
  ```                                                                           
                                                                                
  para português correto.                                                       
                                                                                
  ## 8. Complete a validação estrutural da política no plano                    
                                                                                
  Na lista explícita de validações de `LeitorPolitica`, inclua:                 
                                                                                
  * raiz obrigatoriamente objeto;                                               
  * `vigencia` obrigatória, texto ISO e data real;                              
  * `moeda_base` obrigatória e exatamente `"BRL"`;                              
  * `nota_fiscal_obrigatoria_acima_de` obrigatória, número e não negativa;      
  * `padrao` obrigatório e objeto;                                              
  * `centros_custo` obrigatório e objeto;                                       
  * cada valor de `centros_custo` deve ser objeto;                              
  * nome de categoria deve ser texto/chave não vazia;                           
  * configuração de categoria deve ser objeto;                                  
  * `limite` obrigatório e numérico;                                            
  * limite de `padrao` estritamente maior que zero;                             
  * limite de centro cadastrado maior ou igual a zero;                          
  * `periodicidade` obrigatória e exatamente `"dia"` ou `"diaria"`;             
  * campos desconhecidos ignorados;                                             
  * mapas e modelos construídos com cópias defensivas imutáveis.                
                                                                                
  Faça o equivalente para o contrato de câmbio, incluindo raiz objeto, tipos,   
  datas reais, moedas e taxas positivas.                                        
                                                                                
  ## 9. Corrija a seção de riscos                                               
                                                                                
  A versão 1.0 possuía oito riscos, não sete.                                   
                                                                                
  Restaure o histórico dos oito:                                                
                                                                                
  * preserve os que ainda continuam válidos;                                    
  * o risco que tratava configuração externa como excesso de arquitetura deve   
  ser marcado como superado pela ocorrência real do Dia 2 e reformulado para    
  evitar apenas DSL/motor de regras além do contrato fechado;                   
  * o risco de o Dia 2 exigir mudanças de I/O deve ser registrado como          
  materializado e tratado pela extensão da CLI e pelos leitores externos;       
  * mantenha os demais riscos válidos;                                          
  * acrescente os riscos novos da política v4.                                  
                                                                                
  Não diga que riscos continuam válidos sem apresentá-los no próprio documento. 
                                                                                
  ## 10. Conferência final                                                      
                                                                                
  Verifique que o plano não contém mais:                                        
                                                                                
  ```text                                                                       
  não é repetido aqui                                                           
  Os sete riscos                                                                
  ResolutorCambio.java (ou ConversorCambio.java)                                
  possivelmente os três campos de câmbio                                        
  só os motivos estruturais e ID_DUPLICADO carregam campo não nulo              
  path("moeda") colapsa ausência e null                                         
  RN-010 (atualizada) · CA-028                                                  
  ```                                                                           
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- README.md                                                         
  git diff -- src                                                               
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * somente `plan.md` foi alterado nesta tarefa;                                
  * spec, decisions, tasks, README, código, testes e envelope permanecem        
  intactos;                                                                     
  * nenhuma decisão arquitetural relevante foi deixada para `tasks.md`;         
  * nenhuma RN ou CA ficou sem destino na matriz;                               
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Ao final, apresente:                                                          
                                                                                
  1. como as DTs históricas foram restauradas;                                  
  2. as decisões arquiteturais fechadas;                                        
  3. a correção de `MOEDA_SEM_COTACAO.campo`;                                   
  4. o novo teste-canário;                                                      
  5. a cobertura final da matriz;                                               
  6. o tratamento dos oito riscos históricos;                                   
  7. `git status --short`.                                                      
                                                                                
  Pare para revisão.                                                            

  Read 3 files, ran 2 shell commands (ctrl+o to expand)

● Breaking this into pieces. Starting with the localized fixes (§4, §5, §7, §8)
  before tackling the large §15/§17/§18 rewrites.

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      130  | **Item de entrada** | Igual à v1.0: `indiceEntrada` + `raw` 
           (fotografia de auditoria). |
      131  | **Campos estruturalmente validados** | Passa a ter **oito** 
           campos candidatos em vez de sete: os sete já existentes, mais 
           `moeda` — mas `moeda` é populado de forma diferente dos demais
            (ver §8, "Campo `despesa.moeda`"): resolve para `"BRL"` quand
           o a chave está ausente, sem produzir motivo; fica ausente (nul
           o) quando a chave existe e é estruturalmente inválida (mesma r
           egra dos demais campos nesse caso). |
      132  | **`valor_informado`** | Sem mudança — continua o valor JSON 
           bruto de `despesa.valor`. |
      133 -| **Item com câmbio resolvido (novo, passo 5 de 8.1)** | Estru
          -tura intermediária entre "campos validados" e "normalização": 
          -carrega `moedaResolvida` (`"BRL"` ou a moeda estrangeira valid
          -ada), `taxaCambioAplicada` (`1` para BRL, a taxa resolvida par
          -a estrangeira, nulo se `MOEDA_SEM_COTACAO`), `dataCotacaoUtili
          -zada` (nulo para BRL, a data efetivamente usada para estrangei
          -ra, nulo se sem cotação), `valorConvertidoBruto` (o produto `v
          -alor × taxa`, **ainda sem arredondamento** — o arredondamento 
          -é RN-004, que acontece no passo seguinte) e, quando aplicável,
          - o motivo `MOEDA_SEM_COTACAO`. |                              
      133 +| **`ItemValidado` enriquecido pelo `ResolutorCambio` (passo 5
          + de 8.1)** | Decisão fechada (não uma alternativa em aberto): 
          +`ResolutorCambio` **estende o próprio `ItemValidado`** com qua
          +tro campos novos — `moeda` (`"BRL"` ou a moeda estrangeira val
          +idada), `taxaCambioAplicada` (`1` para BRL, a taxa resolvida p
          +ara estrangeira, nulo se `MOEDA_SEM_COTACAO`), `dataCotacaoUti
          +lizada` (nulo para BRL, a data efetivamente usada para estrang
          +eira, nulo se sem cotação) e `valorConvertidoBruto` (o produto
          + `valor × taxa`, **ainda sem arredondamento** — o arredondamen
          +to é RN-004, que acontece no passo seguinte). Nenhuma estrutur
          +a intermediária alternativa (um "item com câmbio" à parte de `
          +ItemValidado`) é criada nesta versão do plano — ver §9 e DT-01
          +1x/§19. Quando não há cotação, o próprio `ResolutorCambio` gra
          +va o motivo `MOEDA_SEM_COTACAO` com `campo = CampoCanonico.MOE
          +DA` (§10). |                                                  
      134  | **`valor_normalizado`** | Continua `BigDecimal` de escala 2,
            mas agora resultado de RN-004 aplicado sobre `valorConvertido
           Bruto` (BRL: o próprio valor original, já que a taxa é `1`) — 
           um único arredondamento, nunca dois. Nulo quando `despesa.valo
           r` é estruturalmente inválido **ou** quando há `MOEDA_SEM_COTA
           CAO`. |
      135  | **Categoria normalizada** | Sem mudança de mecanismo (RN-005
           ) — mas a partir de RN-019 é comparada contra a tabela de polí
           tica **efetivamente aplicável** (resolvida por centro de custo
           ), não contra um conjunto fixo de três nomes. |
      136  | **Tabela de política resolvida (nova)** | Estrutura devolvid
           a pelo resolutor de centro de custo (§6): a tabela efetivament
           e aplicável (mapa categoria → `{limite, periodicidade}`), a or
           igem (`PADRAO` ou o nome do centro cadastrado), e um método de
            consulta por categoria normalizada que devolve presença/ausên
           cia, `limite`, `periodicidade` e o motivo correto quando a cat
           egoria está ausente ou tem `limite` zero (RN-019). |

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      136  | **Tabela de política resolvida (nova)** | Estrutura devolvid
           a pelo resolutor de centro de custo (§6): a tabela efetivament
           e aplicável (mapa categoria → `{limite, periodicidade}`), a or
           igem (`PADRAO` ou o nome do centro cadastrado), e um método de
            consulta por categoria normalizada que devolve presença/ausên
           cia, `limite`, `periodicidade` e o motivo correto quando a cat
           egoria está ausente ou tem `limite` zero (RN-019). |
      137  | **Acumulador de motivos** | Sem mudança de mecanismo — só o 
           vocabulário de `MotivoCodigo` cresce (§10). |
      138  | **`Motivo`** | Mesmos três campos (`codigo`, `regra`, `campo
           `), mas os três enums fechados que os representam ganham valor
           es novos: `MotivoCodigo` ganha `MOEDA_SEM_COTACAO`, `CATEGORIA
           _NAO_REEMBOLSAVEL_CENTRO_CUSTO`, `TETO_INDIVIDUAL_APLICADO` (d
           ezesseis valores no total); `RegraNegocio` ganha `RN_019` a `R
           N_022`; `CampoCanonico` ganha `MOEDA`, inserido na posição cor
           reta da ordem canônica de contrato — entre `VALOR` e `TEM_NOTA
           _FISCAL` (oito valores no total, mesma técnica de serialização
            num único ponto — DT-008/DT-019). |
      139 -| **Resultado por item** | Ganha três campos novos entre `valo
          -r_informado` e `valor_normalizado`: `moeda`, `taxaCambioAplica
          -da`, `dataCotacaoUtilizada` — os mesmos três valores carregado
          -s pelo "item com câmbio resolvido", propagados sem recálculo a
          -té a composição final. |                                      
      139 +| **Resultado por item** | Ganha três campos novos entre `valo
          +r_informado` e `valor_normalizado`: `moeda`, `taxaCambioAplica
          +da`, `dataCotacaoUtilizada` — os mesmos três valores gravados 
          +em `ItemValidado` pelo `ResolutorCambio`, propagados sem recál
          +culo até a composição final. |                                
      140  | **Resultado geral** | Sem mudança de forma — `colaborador`, 
           `periodo`, `resultados`, `total_reembolsavel`. |
      141  
      142  ---

● Now §5 (LeitorPolitica API + validation checklist) and §7 (LeitorCambio API +
  validation checklist):

● Update(specs\001-motor-reembolso\plan.md)  ⎿  Added 17 lines, removed 1 line
      143  
      144  ## 5. Arquivo externo: Política de reembolso
      145  
      146 -**Responsabilidade:** um componente de leitura (`LeitorPolitic
          -a`, no pacote `leitor`, ao lado de `ValidadorEnvelope`) que ab
          -re o arquivo de `--politica`, faz parsing JSON via `JsonNode` 
          -(mesma técnica de DT-005), valida **integralmente** o contrato
          - estrutural de `spec.md` §4.1.1 antes de produzir qualquer saí
          -da, e devolve um modelo imutável (`PoliticaExterna`) — ou lanç
          -a uma exceção de arquivo externo inválido, que o `Main` traduz
          - em exit `2`. Não existe caminho pelo qual uma política parcia
          -lmente válida alcance o núcleo: a validação é tudo-ou-nada, co
          -mo a de `ValidadorEnvelope` para o envelope de despesas, mas c
          -om gravidade maior (RN-022 é mais grave que RN-001 — nem o env
          -elope chega a ser lido se política ou câmbio falharem).       
      146 +**Responsabilidade:** um componente de leitura (`LeitorPolitic
          +a`, no pacote `leitor`, ao lado de `ValidadorEnvelope`) com um
          +a **API pública única e fechada**:                            
      147  
      148 +```java                                                       
      149 +LeitorPolitica.ler(Path caminho): PoliticaExterna             
      150 +```                                                           
      151 +                                                              
      152 +Não existe uma segunda forma pública de invocar o leitor (ex.:
          + passando `JsonNode` diretamente de fora) — `ler(Path)` é o ún
          +ico ponto de entrada, e é responsável, nesta ordem, por:      
      153 +                                                              
      154 +1. abrir o arquivo no caminho recebido;                       
      155 +2. fazer parsing sintático com Jackson (`JsonNode`, mesma técn
          +ica de DT-005);                                               
      156 +3. validar o contrato estrutural completo de `spec.md` §4.1.1 
          +(lista exaustiva abaixo);                                     
      157 +4. construir o modelo imutável `PoliticaExterna`;             
      158 +5. lançar uma exceção dedicada (`PoliticaInvalidaException`) e
          +m qualquer falha de qualquer um dos passos 1 a 3 — arquivo ine
          +xistente, ilegível, JSON sintaticamente inválido, ou violação 
          +de qualquer regra estrutural.                                 
      159 +                                                              
      160 +Métodos privados internos do leitor podem receber `JsonNode` (
          +é assim que o parsing é feito passo a passo), mas isso é detal
          +he de implementação — não é a API pública planejada. `Main` ch
          +ama exclusivamente `LeitorPolitica.ler(caminhoDaFlagPolitica)`
          + e traduz qualquer `PoliticaInvalidaException` em exit `2`.   
      161 +                                                              
      162 +Não existe caminho pelo qual uma política parcialmente válida 
          +alcance o núcleo: a validação é tudo-ou-nada, como a de `Valid
          +adorEnvelope` para o envelope de despesas, mas com gravidade m
          +aior (RN-022 é mais grave que RN-001 — nem o envelope chega a 
          +ser lido se política ou câmbio falharem).                     
      163 +                                                              
      164  **Modelo (`PoliticaExterna`, imutável):**
      165  
      166  ```

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 18 lines, removed 7 lines
      180  
      181  `versao` e `acrescimo_em_viagem_percentual` (e qualquer outro 
           campo desconhecido) são lidos pelo `JsonNode` bruto só para ef
           eito de "ignorar sem erro" — não entram no modelo `PoliticaExt
           erna`, porque nenhuma regra os consome (RN-016 continua sem ef
           eito; `acrescimo_em_viagem_percentual` nunca ativa comportamen
           to).
      182  
      183 -**Regras estruturais que o `LeitorPolitica` aplica antes de de
          -volver o modelo (RN-022, AMB-035):**                          
      183 +**Lista explícita e exaustiva de validações que o `LeitorPolit
          +ica` aplica antes de devolver o modelo (RN-022, AMB-035, CA-04
          +5) — qualquer uma falhando lança `PoliticaInvalidaException`:*
          +*                                                             
      184  
      185 -- `padrao` e `centros_custo` são objetos obrigatórios, cada um
          - podendo ser vazio.                                           
      186 -- Dentro de `padrao`: todo `limite` deve ser **estritamente ma
          -ior que zero** — `limite: 0` em `padrao` é falha estrutural do
          - arquivo inteiro (não chega a produzir um `TabelaCategoria`, p
          -orque o arquivo inteiro é rejeitado antes).                   
      187 -- Dentro de qualquer tabela de `centros_custo`: `limite` maior
          - ou igual a zero é estruturalmente válido (o `0,00` vira uma d
          -ecisão de negócio — `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` 
          -— não um defeito de arquivo).                                 
      188 -- `periodicidade` deve ser exatamente `"dia"` ou `"diaria"` — 
          -qualquer outro texto invalida o **arquivo**.                  
      189 -- `moeda_base` deve ser exatamente `"BRL"`; `vigencia` deve se
          -r `AAAA-MM-DD` real.                                          
      190 -- Campos desconhecidos dentro de uma categoria ou na raiz são 
          -ignorados sem afetar a validade.                              
      185 +1. A raiz do documento é obrigatoriamente um objeto.          
      186 +2. `vigencia` é obrigatória, texto no formato `AAAA-MM-DD`, re
          +presentando uma data real do calendário (RN-021).             
      187 +3. `moeda_base` é obrigatória e é exatamente `"BRL"`.         
      188 +4. `nota_fiscal_obrigatoria_acima_de` é obrigatória, numérica,
          + e não negativa.                                              
      189 +5. `padrao` é obrigatório e é um objeto (pode ser vazio).     
      190 +6. `centros_custo` é obrigatório e é um objeto (pode ser vazio
          +).                                                            
      191 +7. Cada valor dentro de `centros_custo` (uma tabela por centro
          +) é, ele próprio, um objeto.                                  
      192 +8. Dentro de `padrao` e de cada tabela de `centros_custo`, tod
          +o nome de categoria é uma chave de texto não vazia.           
      193 +9. A configuração de cada categoria é um objeto (não um número
          +, texto ou lista solta).                                      
      194 +10. `limite` é obrigatório dentro de cada configuração de cate
          +goria, e é numérico.                                          
      195 +11. Dentro de `padrao`: todo `limite` é **estritamente maior q
          +ue zero** — `limite: 0` em `padrao` é falha estrutural do arqu
          +ivo inteiro (não chega a produzir um `TabelaCategoria`, porque
          + o arquivo inteiro é rejeitado antes).                        
      196 +12. Dentro de qualquer tabela de `centros_custo`: `limite` mai
          +or ou igual a zero é estruturalmente válido (o `0,00` vira uma
          + decisão de negócio — `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO
          +` — não um defeito de arquivo).                               
      197 +13. `periodicidade` é obrigatória dentro de cada configuração 
          +de categoria, e é exatamente `"dia"` ou `"diaria"` — qualquer 
          +outro texto (ou tipo) invalida o **arquivo** inteiro, não só a
          + categoria.                                                   
      198 +14. Campos desconhecidos — na raiz, dentro de uma categoria, o
          +u em qualquer outro nível — são ignorados sem afetar a validad
          +e (`versao`, `acrescimo_em_viagem_percentual`, `observacao`, e
          +tc.).                                                         
      199 +15. O modelo `PoliticaExterna` resultante é construído com cóp
          +ias defensivas imutáveis dos mapas (`Map.copyOf` ou equivalent
          +e) — nenhuma referência ao `JsonNode` de origem, nem ao mapa m
          +utável intermediário usado durante a validação, escapa para fo
          +ra do leitor.                                                 
      200  
      201 +Todo o arquivo é validado — os quinze pontos acima — **antes**
          + de qualquer `TabelaCategoria` ser construída; não existe um c
          +aminho onde parte do modelo já foi montada quando uma violação
          + tardia é detectada.                                          
      202 +                                                              
      203  Não existem mais as constantes `60`, `80`, `250`, `100` no cód
           igo de produção depois desta mudança: `PoliticaExterna` é o ún
           ico lugar de onde valores financeiros de política se originam,
            e ela sempre vem de um arquivo (nunca de um literal fixo). A 
           política histórica equivalente (usada na regressão de `§12.1`/
           `§12.2`) é uma **fixture externa de teste** — um JSON no mesmo
            formato, versionado em `tests/resources/` — nunca um valor ha
           rdcoded nem um fallback interno de produção (ver §16).
      204  
      205  ---

● Now §7 (LeitorCambio API + expanded validation):

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 7 lines, removed 1 line
      222  
      223  ## 7. Arquivo externo: Câmbio
      224  
      225 -**Responsabilidade:** um componente de leitura (`LeitorCambio`
          -, ao lado de `LeitorPolitica`) que abre `--cambio`, faz parsin
          -g e valida o contrato estrutural de `spec.md` §4.1.1, e devolv
          -e um modelo imutável (`TabelaCambio`) — mesma política de tudo
          --ou-nada e mesma tradução para exit `2` em caso de falha.     
      225 +**Responsabilidade:** um componente de leitura (`LeitorCambio`
          +, ao lado de `LeitorPolitica`) com a mesma forma de API públic
          +a única e fechada:                                            
      226  
      227 +```java                                                       
      228 +LeitorCambio.ler(Path caminho): TabelaCambio                  
      229 +```                                                           
      230 +                                                              
      231 +As mesmas cinco responsabilidades de `LeitorPolitica` (§5) se 
          +aplicam aqui: abrir o arquivo, fazer parsing sintático, valida
          +r o contrato estrutural completo, construir o modelo imutável,
          + lançar `CambioInvalidoException` em qualquer falha. Métodos p
          +rivados internos podem trabalhar com `JsonNode`; a API pública
          + é só `ler(Path)`. Mesma política de tudo-ou-nada e mesma trad
          +ução para exit `2` em caso de falha.                          
      232 +                                                              
      233  **Estrutura real do arquivo** (raiz com `moeda_base`, `fonte`,
            `observacao`, `taxas` aninhado por data e depois por moeda) é
            lida e **invertida** para uma estrutura de consulta eficiente
           :
      234  
      235  ```

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 11 lines, removed 4 lines
      246  3. **Sem interpolação:** o resolutor não faz nenhuma média nem
            cálculo entre duas cotações — devolve exatamente o valor de `
           floorEntry`, ou "sem cotação" quando o resultado é nulo (moeda
            nunca apareceu em `taxas`, ou a primeira cotação disponível é
            posterior à data da despesa).
      247  4. **Moeda ausente de `cotacoesPorMoeda`:** resultado "sem cot
           ação" (`MOEDA_SEM_COTACAO`), o mesmo caminho do item 3.
      248  
      249 -**Regras estruturais que o `LeitorCambio` aplica (RN-022, AMB-
          -035):**                                                       
      249 +**Lista explícita e exaustiva de validações que o `LeitorCambi
          +o` aplica (RN-022, AMB-035, CA-046) — qualquer uma falhando la
          +nça `CambioInvalidoException`:**                              
      250  
      251 -- `moeda_base` exatamente `"BRL"`; `taxas` objeto obrigatório,
          - podendo ser `{}` (válido — recusa despesas estrangeiras item 
          -a item via `MOEDA_SEM_COTACAO`, não invalida o arquivo).      
      252 -- Cada chave de `taxas` é uma data `AAAA-MM-DD` real; cada cha
          -ve dentro de uma data casa com `[A-Z]{3}`; cada taxa é estrita
          -mente positiva.                                               
      253 -- `fonte` e `observacao` são lidos e descartados (informativos
          -, nunca usados por regra alguma).                             
      251 +1. A raiz do documento é obrigatoriamente um objeto.          
      252 +2. `moeda_base` é obrigatória e é exatamente `"BRL"`.         
      253 +3. `taxas` é obrigatório e é um objeto — pode ser `{}` (válido
          +: recusa despesas estrangeiras item a item via `MOEDA_SEM_COTA
          +CAO`, não invalida o arquivo).                                
      254 +4. Cada chave de primeiro nível dentro de `taxas` é uma data n
          +o formato `AAAA-MM-DD`, representando uma data real do calendá
          +rio.                                                          
      255 +5. O valor associado a cada data é, ele próprio, um objeto (ma
          +pa de moeda para taxa).                                       
      256 +6. Cada chave dentro de uma data casa com o padrão `[A-Z]{3}`.
      257 +7. Cada taxa é numérica e estritamente positiva (zero e negati
          +vos são estruturalmente inválidos).                           
      258 +8. `fonte` e `observacao`, quando presentes, são texto informa
          +tivo — lidos e descartados, nunca usados por regra alguma; sua
          + ausência ou tipo incorreto não invalida o arquivo (são opcion
          +ais).                                                         
      259 +9. Campos desconhecidos em qualquer nível são ignorados sem af
          +etar a validade.                                              
      260 +10. O modelo `TabelaCambio` resultante — já invertido para `mo
          +eda → NavigableMap<data, taxa>` (§7, DT-013) — é construído co
          +m cópias defensivas imutáveis; nenhum `Map`/`NavigableMap` mut
          +ável escapa do leitor.                                        
      261  
      262  ---
      263  

● Now §8 — fixing the Jackson path() explanation:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 5 lines, removed 2 lines
      265  
      266  Este é o único campo cujo contrato de ausência-de-chave difere
            de todos os outros sete — e a técnica de parsing já em uso (D
           T-005, `JsonNode`) já distingue exatamente os dois casos que a
            spec exige distinguir, sem exigir nenhuma técnica nova:
      267  
      268 -- `elemento.get("moeda")` devolve a referência Java `null` qua
          -ndo a **chave não existe no objeto** — não confundir com `Null
          -Node`, que é o que Jackson devolve quando a chave existe com v
          -alor JSON `null`.                                             
      269 -- `elemento.get("moeda").isNull()` é verdadeiro quando a chave
          - existe com valor `null` — distinto do caso anterior.         
      268 +- `elemento.get("moeda")` devolve a referência Java `null` qua
          +ndo a **chave não existe no objeto**.                         
      269 +- `elemento.get("moeda")` devolve uma instância de `NullNode` 
          +(`.isNull() == true`) quando a **chave existe com valor JSON `
          +null`**.                                                      
      270 +- `elemento.path("moeda")` também distinguiria os dois casos, 
          +por um caminho equivalente: devolveria `MissingNode` (`.isMiss
          +ingNode() == true`) para chave ausente e `NullNode` (`.isNull(
          +) == true`) para `null` explícito — `path()` **não** colapsa o
          +s dois casos em um só; ele só evita lançar exceção ao encadear
          + acessos em profundidade, o que não é o problema deste campo. 
      271 +- **Por que `get()` e não `path()`:** `get()` é escolhido por 
          +coerência com a camada já existente (os outros sete campos já 
          +usam `elemento.get(chave)`, ver `ValidadorItem`) e porque `get
          +()` torna o ramo de "chave ausente" explícito e imediato — com
          +parar contra a referência `null` — **antes** de acessar qualqu
          +er propriedade do nó, em vez de exigir uma chamada adicional (
          +`isMissingNode()`) sobre um nó que `path()` já teria produzido
          +. A escolha é estilística/de coerência de camada, não uma dife
          +rença de capacidade entre os dois métodos.                    
      272 +- Acessores permissivos com valor padrão (ex.: `elemento.path(
          +"moeda").asText("BRL")`) continuam proibidos: eles calculariam
          + `"BRL"` tanto para chave ausente quanto para qualquer outro c
          +aso que não produza texto, ocultando exatamente a distinção qu
          +e este campo exige preservar.                                 
      273  
      274  **Estratégia de validação (`validarMoeda`, mesma classe/pacote
            de `ValidadorItem`, mas com um ramo extra no topo que os dema
           is campos não têm):**
      275  

● Now §9 — renaming the conversion component definitively to ResolutorCambio:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      291  
      292  **Fórmula normativa (RN-020, RN-004):** `valor bruto × taxa`, 
           seguido de **um único** arredondamento `HALF_UP` para duas cas
           as — nunca dois arredondamentos (um na moeda original, outro a
           pós a conversão).
      293  
      294 -**Componente (estágio novo do pipeline, passo 5 de 8.1 — entre
          - o detector de ID duplicado e o normalizador):** para cada ite
          -m com campos estruturalmente válidos:                         
      294 +**Componente (`pipeline/ResolutorCambio.java`, estágio novo do
          + pipeline, passo 5 de 8.1 — entre o detector de ID duplicado e
          + o normalizador):** para cada item com campos estruturalmente 
          +válidos, `ResolutorCambio` enriquece o próprio `ItemValidado` 
          +(§4) com `moeda`, `taxaCambioAplicada`, `dataCotacaoUtilizada`
          + e `valorConvertidoBruto` — nenhuma estrutura intermediária al
          +ternativa é criada:                                           
      295  
      296  - `moeda` resolvida (§8) igual a `"BRL"`, ou chave ausente (re
           solvida para `"BRL"` sem motivo) → taxa implícita `1`, sem con
           sultar `TabelaCambio`; `data_cotacao_utilizada` fica nula.
      297  - `moeda` estrangeira estruturalmente válida → consulta `Tabel
           aCambio` (§7) pela data da despesa. Cotação resolvida (exata o
           u anterior) → `valorConvertidoBruto = valor × taxa` (sem arred
           ondar ainda); `taxaCambioAplicada` e `dataCotacaoUtilizada` pr
           eenchidos com os valores efetivamente usados. Sem cotação → mo
           tivo `MOEDA_SEM_COTACAO` (RN-020), `taxaCambioAplicada`/`dataC
           otacaoUtilizada`/`valorConvertidoBruto` nulos.

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      294  **Componente (`pipeline/ResolutorCambio.java`, estágio novo do
            pipeline, passo 5 de 8.1 — entre o detector de ID duplicado e
            o normalizador):** para cada item com campos estruturalmente 
           válidos, `ResolutorCambio` enriquece o próprio `ItemValidado` 
           (§4) com `moeda`, `taxaCambioAplicada`, `dataCotacaoUtilizada`
            e `valorConvertidoBruto` — nenhuma estrutura intermediária al
           ternativa é criada:
      295  
      296  - `moeda` resolvida (§8) igual a `"BRL"`, ou chave ausente (re
           solvida para `"BRL"` sem motivo) → taxa implícita `1`, sem con
           sultar `TabelaCambio`; `data_cotacao_utilizada` fica nula.
      297 -- `moeda` estrangeira estruturalmente válida → consulta `Tabel
          -aCambio` (§7) pela data da despesa. Cotação resolvida (exata o
          -u anterior) → `valorConvertidoBruto = valor × taxa` (sem arred
          -ondar ainda); `taxaCambioAplicada` e `dataCotacaoUtilizada` pr
          -eenchidos com os valores efetivamente usados. Sem cotação → mo
          -tivo `MOEDA_SEM_COTACAO` (RN-020), `taxaCambioAplicada`/`dataC
          -otacaoUtilizada`/`valorConvertidoBruto` nulos.                
      297 +- `moeda` estrangeira estruturalmente válida → consulta `Tabel
          +aCambio` (§7) pela data da despesa. Cotação resolvida (exata o
          +u anterior) → `valorConvertidoBruto = valor × taxa` (sem arred
          +ondar ainda); `taxaCambioAplicada` e `dataCotacaoUtilizada` pr
          +eenchidos com os valores efetivamente usados. Sem cotação → `R
          +esolutorCambio` grava o motivo `MOEDA_SEM_COTACAO` com `regra 
          += RN_020` e `campo = CampoCanonico.MOEDA` (serializado `"despe
          +sa.moeda"`, §10) — não `campo` nulo, porque a causa é especifi
          +camente essa chave; `taxaCambioAplicada`/`dataCotacaoUtilizada
          +`/`valorConvertidoBruto` ficam nulos.                         
      298  - `moeda` estruturalmente inválida (`CAMPO_AUSENTE`/`CAMPO_TIP
           O_INVALIDO`/`CAMPO_FORMATO_INVALIDO`) → item já recusado por R
           N-002; este estágio não tenta resolver câmbio para ele (mesma 
           exclusão de dependência de campo inválido já existente).
      299  
      300  O **normalizador** (RN-004, estágio seguinte) aplica `setScale
           (2, RoundingMode.HALF_UP)` sobre `valorConvertidoBruto` (ou so
           bre o valor original, para BRL, que é o caso `taxa = 1` sem co
           nversão real) — produzindo `valor_normalizado`. Nenhum outro p
           onto do pipeline arredonda.

● Now §10 — fixing the campo contract statement:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 8 lines, removed 2 lines
      329  
      330  **Garantias de coerência (mecanismo já existente em `Composito
           rSaida`, apenas com tabela maior):**
      331  
      332 -- **Motivo associado à RN correta:** cada `Motivo` carrega sua
          - própria `RegraNegocio` no ponto onde é criado — a tabela de e
          -stágios ordena por `codigo`, nunca precisa inferir a regra a p
          -artir do estágio. Para `TETO_DIARIO_APLICADO`, a `regra` efeti
          -vamente gravada no motivo difere por categoria (`RN_011` para 
          -`alimentacao`, `RN_012` para `transporte_urbano`, `RN_019` par
          -a qualquer outra categoria com `periodicidade: "dia"` — spec 4
          -.5) — quem decide isso é o agregador de teto (§9 do pipeline, 
          -ver §11 abaixo), não o compositor.                            
      333 -- **`campo` correto:** só os motivos estruturais (estágio 1) e
          - `ID_DUPLICADO` carregam `campo` não nulo — mecanismo inaltera
          -do.                                                           
      332 +- **Motivo associado à RN correta:** cada `Motivo` carrega sua
          + própria `RegraNegocio` no ponto onde é criado — a tabela de e
          +stágios ordena por `codigo`, nunca precisa inferir a regra a p
          +artir do estágio. Para `TETO_DIARIO_APLICADO`, a `regra` efeti
          +vamente gravada no motivo difere por categoria (`RN_011` para 
          +`alimentacao`, `RN_012` para `transporte_urbano`, `RN_019` par
          +a qualquer outra categoria com `periodicidade: "dia"` — spec 4
          +.5) — quem decide isso é o agregador de teto (§11), não o comp
          +ositor.                                                       
      333 +- **`campo` correto — contrato completo, não só "estruturais e
          + `ID_DUPLICADO`":**                                           
      334 +  1. Erros estruturais de campo (estágio 1: `CAMPO_AUSENTE`/`C
          +AMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO`) carregam o `Camp
          +oCanonico` correspondente ao campo defeituoso.                
      335 +  2. `ID_DUPLICADO` carrega `CampoCanonico.ID` (serializado `"
          +despesa.id"`) — é a violação desse campo especificamente.     
      336 +  3. `MOEDA_SEM_COTACAO` carrega `CampoCanonico.MOEDA` (serial
          +izado `"despesa.moeda"`, §9) — mesmo padrão de `ID_DUPLICADO`:
          + a causa é uma chave específica, não o item inteiro.          
      337 +  4. Todos os demais motivos não estruturais (`VALOR_NAO_POSIT
          +IVO`, `CATEGORIA_FORA_POLITICA`, `CATEGORIA_NAO_REEMBOLSAVEL_C
          +ENTRO_CUSTO`, `FORA_COMPETENCIA`, `NOTA_FISCAL_AUSENTE`, `DUPL
          +ICIDADE`, e os quatro motivos de teto) carregam `campo` nulo —
          + a causa não é uma chave específica, é uma decisão sobre o ite
          +m como um todo.                                               
      338 +  5. `ITEM_TIPO_INVALIDO` continua com `campo` nulo — a única 
          +exceção "estrutural implica campo preenchido" (spec 4.3), porq
          +ue o defeito é do elemento inteiro, não de uma chave dele.    
      339 +  Nenhuma mudança de mecanismo em `CompositorSaida` é exigida 
          +por isso além de já ter `CampoCanonico.ID` e `CampoCanonico.MO
          +EDA` disponíveis em `ORDEM_CAMPO` (DT-019) — os construtores d
          +e `Motivo` em `DetectorIdDuplicado` e `ResolutorCambio` já pas
          +sam o valor correto; o compositor só consome o que recebe.    
      340  - **Ordem determinística dos motivos:** `Comparator` explícito
            por estágio, nunca por `enum.ordinal()` — mecanismo inalterad
           o, só a tabela cresce.
      341  - **Decisão final coerente e um resultado por posição:** mecan
           ismo de `CompositorSaida` inalterado — cada `indiceEntrada` pr
           oduz exatamente um `ResultadoItem`, agora com os três campos d
           e câmbio adicionais.
      342  

● Now §11 — renaming AgregadorTetoHospedagem to the new AgregadorTetoIndividual:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      344  
      345  ## 11. Tetos por periodicidade
      346  
      347 -**Princípio normativo (RN-019, AMB-036/037):** o mecanismo de 
          -teto é determinado pela `periodicidade` declarada na tabela de
          - política efetivamente aplicável — nunca pelo nome histórico d
          -a categoria. Isso muda o design dos dois agregadores existente
          -s: em vez de um `Set<String>` fixo de categorias (`AgregadorTe
          -toDiario`) e um agregador exclusivo de `hospedagem` (`Agregado
          -rTetoHospedagem`), cada agregador passa a decidir sua aplicabi
          -lidade consultando a `periodicidade` resolvida (§6) para a cat
          -egoria do item, não o nome dela.                              
      347 +**Princípio normativo (RN-019, AMB-036/037):** o mecanismo de 
          +teto é determinado pela `periodicidade` declarada na tabela de
          + política efetivamente aplicável — nunca pelo nome histórico d
          +a categoria. Isso muda o design dos dois agregadores: `Agregad
          +orTetoDiario` é **estendido** (deixa de usar um `Set<String>` 
          +fixo de categorias); o agregador exclusivo de `hospedagem` (`A
          +gregadorTetoHospedagem`) é **substituído** por um componente n
          +ovo, `pipeline/AgregadorTetoIndividual.java` (decisão fechada 
          +— ver §19), porque deixa de ser exclusivo de uma categoria e p
          +assa a processar qualquer categoria com `periodicidade: "diari
          +a"`. Cada um dos dois agregadores decide sua aplicabilidade co
          +nsultando a `periodicidade` resolvida (§6) para a categoria do
          + item, não o nome dela.                                       
      348  
      349  **Periodicidade `"dia"` (generaliza o `AgregadorTetoDiario` at
           ual):**
      350  

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      352  - Item que excede o saldo disponível recebe o saldo restante (
           `PARCIALMENTE_REEMBOLSADO`, `TETO_DIARIO_APLICADO`); itens pos
           teriores ao esgotamento recebem `NAO_REEMBOLSADO_TETO_ESGOTADO
           `/`TETO_DIARIO_ESGOTADO`.
      353  - A `regra` do motivo `TETO_DIARIO_APLICADO` é `RN_011` quando
            a categoria for `alimentacao`, `RN_012` quando for `transport
           e_urbano`, `RN_019` para qualquer outra categoria sob esse mec
           anismo (ex.: `representacao`) — uma pequena tabela de exceção 
           dentro do agregador, não um novo estágio de compositor.
      354  
      355 -**Periodicidade `"diaria"` (generaliza o `AgregadorTetoHospeda
          -gem` atual):**                                                
      355 +**Periodicidade `"diaria"` (`AgregadorTetoIndividual`, novo co
          +mponente que substitui `AgregadorTetoHospedagem`):**          
      356  
      357  - Teto **individual** por lançamento, sem saldo compartilhado.
      358  - `hospedagem` sob esse mecanismo usa `TETO_HOSPEDAGEM_APLICAD
           O`/`RN_013`; qualquer outra categoria (ex.: `estacionamento`) 
           usa `TETO_INDIVIDUAL_APLICADO`/`RN_019` (AMB-037) — mesma lógi
           ca de pequena tabela de exceção por nome de categoria, desta v
           ez dentro do agregador individual.

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      363  - `limite == 0` numa tabela de centro cadastrado já recusou o 
           item com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` na etapa de
            regras individuais (RN-019) — o item nunca chega à população 
           que os agregadores de teto recebem.
      364  - `limite == 0` em `padrao` nunca chega ao motor: invalida o a
           rquivo de política inteiro na leitura (§5), antes de qualquer 
           item ser processado.
      365  
      366 -O reaproveitamento do método `aplicarCorte` (já existente em `
          -AgregadorTetoDiario`, documentado como "não conhece categoria,
          - para ser reaproveitável pelo teto individual") continua válid
          -o e é, na prática, o mecanismo que os dois agregadores general
          -izados compartilham — a mesma decisão de design da v1.0 deste 
          -plano, agora exercida por mais categorias.                    
      366 +O reaproveitamento do método `aplicarCorte` (já existente em `
          +AgregadorTetoDiario`, documentado como "não conhece categoria,
          + para ser reaproveitável pelo teto individual") continua válid
          +o: é o mecanismo que `AgregadorTetoDiario` (estendido) e `Agre
          +gadorTetoIndividual` (novo, em substituição a `AgregadorTetoHo
          +spedagem`) compartilham — a mesma decisão de design da v1.0 de
          +ste plano, agora exercida por mais categorias e por um compone
          +nte com nome que reflete seu escopo real.                     
      367  
      368  ---
      369  

● Now §12 — adding the CA-033 citation:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      374  **Extensão de `DetectorDuplicidadeEconomica`:**
      375  
      376  - `id` e `tem_nota_fiscal` seguem fora da chave — sem mudança.
      377 -- `moeda` entra como componente **adicional** da chave — nunca
          - substitui `valor_normalizado`, que já é o valor convertido. D
          -ois itens com o mesmo valor convertido, mas em moedas diferent
          -es, nunca colidem: a chave de comparação (um `record` ou tupla
          - equivalente) inclui `moeda` como campo próprio, então `equals
          -`/`hashCode` já os distingue sem necessidade de lógica condici
          -onal adicional.                                               
      377 +- `moeda` entra como componente **adicional** da chave — nunca
          + substitui `valor_normalizado`, que já é o valor convertido. D
          +ois itens com o mesmo valor convertido, mas em moedas diferent
          +es, nunca colidem (CA-033): a chave de comparação (um `record`
          + ou tupla equivalente) inclui `moeda` como campo próprio, entã
          +o `equals`/`hashCode` já os distingue sem necessidade de lógic
          +a condicional adicional.                                      
      378  - Itens com `MOEDA_SEM_COTACAO` têm `valor_normalizado` nulo e
            por isso já chegam recusados (`RECUSADO`) na etapa anterior —
            o `SeletorElegiveis` já os exclui da população que entra em `
           DetectorDuplicidadeEconomica`, mecanismo idêntico ao de qualqu
           er outra recusa individual, sem necessidade de tratamento espe
           cial aqui.
      379  - Primeira ocorrência por `indiceEntrada` mantida; posteriores
            recebem `DUPLICIDADE` — mecanismo inalterado.
      380  

● Now §14 — replacing the false canary claim with the real one:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 13 lines, removed 1 line
      395  Tudo que a v1.0 já declarava (parsing decimal-exato via `USE_B
           IG_DECIMAL_FOR_FLOATS`, `decimalValue()`, nunca `doubleValue()
           `/`double`, comparação via `compareTo`, serialização em notaçã
           o simples) continua valendo sem alteração. Extensão:
      396  
      397  - A multiplicação `valor × taxa` (RN-020) usa `BigDecimal.mult
           iply`, preservando a escala plena do produto — o arredondament
           o para duas casas (`setScale(2, RoundingMode.HALF_UP)`) aconte
           ce **uma única vez**, depois da multiplicação, nunca antes.
      398 -- **Teste-canário adicional (planejado, ver §16):** USD `40,00
          - × 5,50 = 220,00` deve produzir exatamente `220,00` num único 
          -arredondamento — o equivalente, para conversão cambial, do tes
          -te-canário `100.005 → 100,01` já existente para RN-004.       
      398 +- **Exemplo funcional (CA-031, não é teste-canário):** USD `40
          +,00 × 5,50 = 220,00` é o cenário normativo da spec para a fórm
          +ula de conversão — mas, como o valor bruto (`40,00`) e a taxa 
          +(`5,50`) já têm no máximo duas e duas casas decimais respectiv
          +amente, esse exemplo **não** distingue "arredondar só depois d
          +a multiplicação" de "arredondar o valor bruto antes e multipli
          +car depois": os dois caminhos produzem `220,00` neste caso esp
          +ecífico, então ele não detecta arredondamento prematuro.      
      399 +- **Teste-canário real de ordem de arredondamento (planejado, 
          +ver §17):** o caso que efetivamente diferencia as duas ordens 
          +exige um valor bruto com mais de duas casas decimais:         
      400 +  ```                                                         
      401 +  valor bruto = 1.005                                         
      402 +  taxa        = 1.005                                         
      403 +  produto exato (sem arredondar antes) = 1.005 × 1.005 = 1.010
          +025                                                           
      404 +  resultado correto — arredondar só uma vez, sobre o produto e
          +xato: 1.010025 → 1.01                                         
      405 +  resultado incorreto — arredondar o valor bruto antes de mult
          +iplicar:                                                      
      406 +    1.005 → 1.01 (HALF_UP)                                    
      407 +    1.01 × 1.005 = 1.01505 → 1.02 (HALF_UP)                   
      408 +  ```                                                         
      409 +  Uma implementação que arredonde `valor bruto` para duas casa
          +s **antes** de multiplicar pela taxa produz `1,02`; a implemen
          +tação correta (RN-004/RN-020, DT-015) produz `1,01`. Esse é o 
          +caso que detecta arredondamento prematuro **antes** da multipl
          +icação — o exemplo `40,00 × 5,50` não detecta.                
      410 +  **Limite dessa garantia:** este teste-canário só detecta arr
          +edondamento *antes* da multiplicação. Ele **não** detecta, nec
          +essariamente, um segundo arredondamento *idempotente* aplicado
          + *depois* — `setScale(2, RoundingMode.HALF_UP)` repetido sobre
          + um valor que já tem escala 2 devolve o mesmo valor, então um 
          +`setScale` redundante no fim de um pipeline que já arredondou 
          +corretamente uma vez pode passar despercebido por um teste que
          + só observa a saída final. A garantia de que existe **exatamen
          +te um** ponto de arredondamento no código depende, além deste 
          +teste, da responsabilidade arquitetural definida em DT-015 (só
          + o normalizador arredonda) e de revisão de código nas tasks qu
          +e tocarem `ResolutorCambio`/`Normalizador`.                   
      411  
      412  ---
      413  
● Now the largest piece — restoring full DT-001 through DT-010 with the original
  text, plus extensions, and fixing DT-012/DT-014/DT-015:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 99 lines, removed 9 lines
      413  
      414  ## 15. Decisões técnicas
      415  
      416 -### DT-001 a DT-006, DT-008 a DT-010 — preservadas sem alteraç
          -ão                                                            
      416 +Esta seção restaura o texto integral de cada decisão técnica d
          +a v1.0 deste plano (commit anterior, `git show HEAD:specs/001-
          +motor-reembolso/plan.md`), para que o documento seja compreens
          +ível sem consultar Git ou uma versão antiga do arquivo. Onde a
          + spec 1.2 mudou algo, o texto original é seguido de uma subseç
          +ão `#### Extensão — Dia 2` — nunca substituído silenciosamente
          +.                                                             
      417  
      418 -O texto integral das decisões DT-001 (linguagem/ambiente), DT-
          -002 (empacotamento), DT-004 (`BigDecimal` decimal-exato), DT-0
          -05 (parsing por árvore), DT-006 (pipeline linear seguindo 8.1)
          -, DT-008 (`Motivo` como enumerações fechadas), DT-009 (testes 
          -em três níveis) e DT-010 (escrita atômica) da v1.0 deste plano
          - continua valendo integralmente e não é repetido aqui para não
          - duplicar conteúdo — nenhuma delas foi invalidada pela spec 1.
          -2. DT-005 e DT-006 recebem, respectivamente, o §8 (campo `moed
          -a`) e o §2 (pipeline de treze passos) como **extensão**, não s
          -ubstituição: a técnica e a motivação originais continuam corre
          -tas, só o escopo cresceu.                                     
      418 +### DT-001 — Linguagem e ambiente de execução                 
      419  
      420 -### DT-003 — Contrato de CLI e códigos de saída (estendida, nã
          -o superada)                                                   
      420 +**Contexto:** prazo de dois dias, desenvolvedor com familiarid
          +ade declarada em Java, ambiente desta máquina já inspecionado 
          +(Java 21.0.2 e Maven 3.9.6 funcionando; Python e Node ausentes
          +).                                                            
      421 +**Decisão:** Java 21 como linguagem única do projeto.         
      422 +**Alternativa descartada:** Python/Node — não instalados nesta
          + máquina, custariam tempo de setup sob prazo curto; Kotlin — m
          +esma JVM, mas sintaxe nova sem necessidade.                   
      423 +**Consequência:** compilar o projeto exige JDK 21 e Maven 3.9+
          + instalados na máquina; executar o JAR já compilado exige apen
          +as um Java 21 (JRE ou JDK), porque o fat jar já contém as depe
          +ndências. Na primeira compilação, o Maven pode precisar baixar
          + dependências (Jackson, JUnit) de um repositório remoto — exig
          +e acesso à rede nessa primeira vez; builds subsequentes reapro
          +veitam o cache local do Maven.                                
      424  
      422 -**Contexto original:** três códigos de saída, mensagem em stde
          -rr, sem escrita em `--output` em falha (v1.0 deste plano, aind
          -a válido).                                                    
      423 -**Extensão (Dia 2, spec 1.2):** o contrato de CLI passa de dua
          -s para quatro flags obrigatórias (`--input`, `--output`, `--po
          -litica`, `--cambio`), quaisquer ordem, cada uma exatamente uma
          - vez; o código `2` passa a cobrir também flag repetida/desconh
          -ecida e política/câmbio estruturalmente inválidos — casos que 
          -não existiam quando DT-003 foi escrita, porque os arquivos ext
          -ernos não existiam.                                           
      424 -**Por que não é superada:** a decisão de fundo — taxonomia de 
          -três códigos, mensagem simples em stderr, ausência de vocabulá
          -rio JSON de erro, não tocar `--output` em falha — continua cor
          -reta e não precisa ser refeita; só a superfície de validação d
          -e argumentos cresce.                                          
      425 -**Consequência:** o parser de argumentos deixa de ser um `swit
          -ch` de dois casos e passa a validar um conjunto fechado de qua
          -tro chaves obrigatórias sem repetição e sem chave desconhecida
          - (§3).                                                        
      425 +Sem extensão nesta revisão — a política v4 não muda linguagem 
          +nem ambiente de execução.                                     
      426  
      427 -### DT-007 — SUPERADA por DT-011                              
      427 +### DT-002 — Empacotamento em JAR único via Maven Shade Plugin
      428  
      429 -**Texto original (histórico, preservado para rastreabilidade):
          -** "uma estrutura única e imutável, `PoliticaReembolso`, sem m
          -ecanismo de configuração externa [...] sem arquivo externo de 
          -configuração [...] Construir generalidade para requisitos futu
          -ros desconhecidos seria exatamente o excesso de arquitetura qu
          -e o FAQ do desafio adverte a evitar."                         
      429 +**Contexto:** o contrato de execução exige `java -jar target/m
          +otor-reembolso.jar ...` funcionando sem passos adicionais e se
          +m wrapper de shell.                                           
      430 +**Decisão:** configurar o Maven Shade Plugin para produzir, a 
          +partir de `mvn package`, exatamente `target/motor-reembolso.ja
          +r` — um único artefato contendo todas as dependências (Jackson
          +) e o `Main-Class` no manifesto. Fixar `<finalName>motor-reemb
          +olso</finalName>` para que o nome do artefato não dependa da v
          +ersão do projeto.                                             
      431 +**Alternativa descartada:** `maven-assembly-plugin` (resultado
          + equivalente, Shade é mais direto para este caso de fat jar si
          +mples sem relocations); wrapper `.sh`/`.bat` (descartado por i
          +nstrução explícita — superfície de manutenção extra em duas pl
          +ataformas sem necessidade, já que `java -jar` funciona igual e
          +m qualquer SO com JDK).                                       
      432 +**Consequência:** um único comando de build (`mvn package`) e 
          +um único comando de execução, sem classpath manual, sem script
          + adicional para manter ou testar.                             
      433 +                                                              
      434 +Sem extensão nesta revisão — nenhum novo artefato de build é e
          +xigido pela política v4; os dois arquivos externos são argumen
          +tos de linha de comando, não dependências de empacotamento.   
      435 +                                                              
      436 +### DT-003 — Contrato de CLI e códigos de saída               
      437 +                                                              
      438 +**Contexto:** a interface é fixa (`--input`/`--output`), mas a
          + spec não define o que acontece na CLI quando o processamento 
          +não pode ocorrer — isso é contrato de execução, não regra de n
          +egócio.                                                       
      439 +**Decisão:** três códigos de saída (`0`, `2`, `3`), mensagem e
          +m texto simples em stderr para os códigos de erro, nenhuma esc
          +rita em `--output` quando o código não é `0`, e validação de e
          +nvelope executada antes de qualquer abertura do arquivo de saí
          +da.                                                           
      440 +**Alternativa descartada:** vocabulário de erro em JSON estrut
          +urado (introduziria um esquema que a spec não define); escreve
          +r um JSON de erro no próprio `--output` (arriscaria confundir 
          +"resultado" com "estado de erro" no mesmo arquivo, e violaria 
          +a garantia de não sobrescrever `--output` em falha).          
      441 +**Consequência:** contrato simples e verificável por teste de 
          +CLI; separa claramente "processamento não ocorreu" de "process
          +amento ocorreu com itens recusados" — este último ainda é códi
          +go `0`, porque recusa de item é resultado válido, não falha de
          + processo.                                                    
      442 +                                                              
      443 +#### Extensão — Dia 2                                         
      444 +                                                              
      445 +O contrato de CLI passa de duas para quatro flags obrigatórias
          + (`--input`, `--output`, `--politica`, `--cambio`), em qualque
          +r ordem, cada uma exatamente uma vez; o código `2` passa a cob
          +rir também flag repetida/desconhecida e política/câmbio estrut
          +uralmente inválidos — casos que não existiam quando DT-003 foi
          + escrita, porque os arquivos externos não existiam. A decisão 
          +de fundo — taxonomia de três códigos, mensagem simples em stde
          +rr, ausência de vocabulário JSON de erro, não tocar `--output`
          + em falha — continua correta e não é refeita; só a superfície 
          +de validação de argumentos cresce (§3, DT-018 abaixo formaliza
          + como).                                                       
      446 +                                                              
      447 +### DT-004 — `BigDecimal` com parsing decimal-exato           
      448 +                                                              
      449 +**Contexto:** a spec exige `100.005 → 100,01`; ponto flutuante
          + binário (`double`) representaria `100.005` como `100.00499999
          +999999...` e arredondaria para o lado errado sob `HALF_UP`.   
      450 +**Decisão:** `BigDecimal` de ponta a ponta, com `ObjectMapper`
          + configurado para `USE_BIG_DECIMAL_FOR_FLOATS`, valores obtido
          +s via `decimalValue()`, nunca via `doubleValue()` ou construçã
          +o a partir de `double`.                                       
      451 +**Alternativa descartada:** `double`/`float` (fonte de bug doc
          +umentada); bibliotecas de dinheiro de terceiros (desnecessária
          +s — `BigDecimal` nativo já cobre a exigência).                
      452 +**Consequência:** os cinco valores de fronteira exigidos (33.3
          +33, 33.335, 33.345, 100.004, 100.005) arredondam corretamente 
          +por construção, não por sorte; qualquer regressão futura que r
          +eintroduza `double` em algum ponto do caminho é detectável pel
          +o teste-canário de 100.005.                                   
      453 +                                                              
      454 +Sem subseção de extensão aqui: a extensão real desta decisão p
          +ara a conversão cambial (ordem única de arredondamento sobre o
          + produto `valor × taxa`) é grande o bastante para merecer uma 
          +decisão própria — ver DT-015 abaixo, que estende DT-004 explic
          +itamente.                                                     
      455 +                                                              
      456 +### DT-005 — Parsing por árvore (`JsonNode`) na camada de entr
          +ada                                                           
      457 +                                                              
      458 +**Contexto:** RN-002 exige distinguir três classes de erro est
          +rutural por campo (ausência, tipo, formato) e 4.3 exige preser
          +var `valor_informado` exatamente como recebido, mesmo com tipo
          + inválido — isso exige inspecionar o JSON bruto, não um objeto
          + já desserializado e coagido.                                 
      459 +**Decisão:** ler cada item como `JsonNode`, inspecionar `JsonN
          +odeType` explicitamente por campo (nunca usar acessores permis
          +sivos como `asBoolean()`/`asInt()`, que fariam coerção que RN-
          +002 proíbe), e só então produzir os campos validados e normali
          +zados que o núcleo consome.                                   
      460 +**Alternativa descartada:** desserialização direta para um obj
          +eto POJO tipado (perderia a distinção entre "campo ausente" e 
          +"campo de tipo errado", e perderia o valor bruto para `valor_i
          +nformado` quando o tipo é inválido).                          
      461 +**Consequência:** classificação estrutural fiel à spec, ao cus
          +to de uma camada de leitura mais verbosa que uma desserializaç
          +ão direta.                                                    
      462 +                                                              
      463 +#### Extensão — Dia 2                                         
      464 +                                                              
      465 +A mesma técnica de árvore `JsonNode` é reaproveitada para os d
          +ois arquivos externos (`LeitorPolitica`, `LeitorCambio`, §5/§7
          +), não só para o envelope de despesas — mesma justificativa or
          +iginal, aplicada a mais dois arquivos. O 7º campo (`despesa.mo
          +eda`) exige, adicionalmente, distinguir "chave ausente" de "ch
          +ave presente com `null`" — uma distinção que `JsonNode.get()` 
          +já oferece sem técnica nova; o detalhamento fica em DT-014.   
      466 +                                                              
      467 +### DT-006 — Arquitetura em pipeline linear seguindo a seção 8
          +.1                                                            
      468 +                                                              
      469 +**Contexto:** a ordem de processamento é normativa (seção 8 da
          + spec) e distinta da ordem de apresentação de motivos (8.3); u
          +ma arquitetura que não espelhe isso arrisca produzir resultado
          +s corretos por acidente, não por construção.                  
      470 +**Decisão:** onze estágios lineares, um por passo de 8.1, cada
          + um operando sobre a lista completa de itens sem jamais reorde
          +ná-la; motivos acumulados por item ao longo dos estágios; reor
          +denação para apresentação isolada no passo 10.                
      471 +**Alternativa descartada:** um único método monolítico avalian
          +do tudo por item em qualquer ordem interna conveniente — mais 
          +difícil de auditar linha a linha contra a seção 8, e mais fáci
          +l de violar sem perceber a regra "erros em campos não usados n
          +ão impedem outras regras".                                    
      472 +**Consequência:** cada estágio é testável isoladamente e mapei
          +a 1:1 para uma linha da seção 8.1, o que facilita tanto a impl
          +ementação quanto a auditoria da rastreabilidade.              
      473 +                                                              
      474 +#### Extensão — Dia 2                                         
      475 +                                                              
      476 +A spec 1.2 (§8.1) tem treze passos, não onze — os dois passos 
          +novos são a validação dos arquivos externos (à frente de tudo)
          + e a resolução de câmbio/conversão (entre a detecção de ID dup
          +licado e a normalização). A decisão de fundo (um estágio por l
          +inha de 8.1, motivos acumulados, reordenação isolada na compos
          +ição) não muda — só o número de estágios. O diagrama completo 
          +de treze passos está em §2.                                   
      477 +                                                              
      478 +### DT-007 — Representação da política como estrutura imutável
          + simples — SUPERADA por DT-011                                
      479 +                                                              
      480 +**Texto original (histórico, preservado integralmente para ras
          +treabilidade):**                                              
      481 +                                                              
      482 +**Contexto:** a política tem quatro valores numéricos fixos ne
          +sta versão da spec.                                           
      483 +**Decisão:** uma estrutura única e imutável, `PoliticaReembols
          +o`, sem mecanismo de configuração externa.                    
      484 +**Alternativa descartada:** motor de regras genérico, DSL, arq
          +uivo de configuração externo, banco de dados, framework de inj
          +eção de dependência — todos resolveriam um problema de flexibi
          +lidade que a spec atual não tem.                              
      485 +**Consequência:** mudar um teto é uma edição de quatro linhas;
          + em troca, qualquer flexibilidade não prevista pela spec 1.1 e
          +xigiria refatoração explícita, o que é aceitável porque não há
          + evidência de que essa flexibilidade seja necessária agora.   
      486 +                                                              
      487  **Por que deixou de valer:** a política v4 (Dia 2) introduz ex
           atamente a generalidade que DT-007 declarava desnecessária — R
           N-019 exige política externa, lida de arquivo, variável por ce
           ntro de custo, com categorias dinâmicas e periodicidade config
           urável. A premissa de DT-007 ("a política tem quatro valores n
           uméricos fixos nesta versão da spec") deixou de ser verdadeira
           ; a decisão em si, não só sua consequência, está errada para a
            spec 1.2.
      488  **Requisitos da spec 1.2 que provocaram a substituição:** RN-0
           19 (política por centro de custo), RN-021 (vigência), RN-022 (
           processabilidade do arquivo externo) — nenhum deles existia na
            spec 1.1 que fundamentava DT-007.
      489  **Substituída por:** DT-011.
      490  **Nota:** esta supersessão já estava registrada do lado da spe
           c em `DECISIONS.md` (D-003, "DT-007 invalidada"); esta entrada
            é a materialização formal do lado do `plan.md`, adiada até es
           ta revisão como o próprio `DECISIONS.md` previa.
      491  
      492 +### DT-008 — `Motivo` como três enumerações fechadas          
      493 +                                                              
      494 +**Contexto:** 4.5 define um vocabulário fechado para `codigo`,
          + `regra` e `campo`; strings livres repetidas pelo código são u
          +ma fonte comum de divergência de grafia entre o motivo emitido
          + e o exigido pela spec.                                       
      495 +**Decisão:** três enumerações (`MotivoCodigo`, `RegraNegocio`,
          + `CampoCanonico`), cada uma com o texto canônico correspondent
          +e definido em um único lugar.                                 
      496 +**Alternativa descartada:** strings soltas (`"RN-004"`, `"desp
          +esa.valor"`) espalhadas pelas classes que emitem motivos — fun
          +ciona, mas cada ocorrência é uma chance de erro de digitação n
          +ão detectado por compilação.                                  
      497 +**Consequência:** erro de grafia num código de motivo vira err
          +o de compilação, não uma divergência silenciosa só visível em 
          +teste ou na correção.                                         
      498 +                                                              
      499 +#### Extensão — Dia 2                                         
      500 +                                                              
      501 +O vocabulário cresce (três `MotivoCodigo` novos, quatro `Regra
          +Negocio` novos, um `CampoCanonico` novo) — mesmo mecanismo, se
          +m enum novo criado do zero. O detalhamento de quais valores en
          +tram e onde é DT-019 abaixo.                                  
      502 +                                                              
      503 +### DT-009 — Estratégia de testes em três níveis              
      504 +                                                              
      505 +**Contexto:** a rubrica avalia rastreabilidade `spec → tasks →
          + commits → testes`; cada RN e cada CA precisa de destino verif
          +icável.                                                       
      506 +**Decisão:** testes unitários por regra (maioria), poucos test
          +es de integração de pipeline completo (o arquivo de exemplo e 
          +fixtures adicionais), e um teste de contrato/CLI cobrindo códi
          +gos de saída e comportamento de arquivo. Nomenclatura de class
          +e/método referenciando o `RN-NNN` e o `CA-NNN` correspondentes
          +. Todo teste — inclusive o de integração (`ExemploCompletoTest
          +`) e os de contrato/CLI (`CliContratoTest`, `EscritaAtomicaSai
          +daTest`) — usa o sufixo `*Test`, reconhecido pelo Maven Surefi
          +re por padrão, para que `mvn test` execute a suíte inteira num
          + único comando.                                               
      507 +**Alternativa descartada:** cobertura só por teste de integraç
          +ão ponta a ponta — esconderia qual regra especificamente falho
          +u e dificultaria o grep de rastreabilidade que a rubrica valor
          +iza. Nomear o teste de integração com o sufixo `*IT` (convençã
          +o do Maven Failsafe) — exigiria configurar e invocar um plugin
          + de build adicional (`mvn verify`) só por causa de nomenclatur
          +a, e o teste deixaria de rodar em `mvn test`, contrariando a s
          +implicidade pedida.                                           
      508 +**Consequência:** qualquer regra da spec é localizável no códi
          +go de teste por busca textual do próprio identificador `RN-NNN
          +` ou `CA-NNN`; `mvn test` sozinho executa toda a suíte, sem ex
          +igir um segundo comando ou plugin para os testes de integração
          +/CLI; ver matriz completa em §9 da v1.0 (§17 nesta versão).   
      509 +                                                              
      510 +#### Extensão — Dia 2                                         
      511 +                                                              
      512 +A mesma estratégia de três níveis (unitário majoritário, pouco
          +s testes de integração de pipeline completo, contrato/CLI) é a
          +dotada para RN-019 a RN-022 e CA-024 a CA-049 — nenhum nível n
          +ovo é criado. A matriz de rastreabilidade estendida está em §1
          +7.                                                            
      513 +                                                              
      514 +### DT-010 — Escrita atômica do arquivo de saída              
      515 +                                                              
      516 +**Contexto:** a garantia de que `--output` nunca é criado nem 
          +sobrescrito nos códigos `2` e `3` só é verdadeira se a escrita
          + do resultado nunca tocar o destino antes de o resultado estar
          + completo — escrever progressivamente e diretamente no destino
          + deixaria um arquivo truncado no caminho oficial caso o proces
          +so falhe no meio da escrita.                                  
      517 +**Decisão:** serializar o resultado completo para um arquivo t
          +emporário no mesmo diretório do destino (mesmo sistema de arqu
          +ivos, condição para substituição atômica); fechar e concluir t
          +otalmente a escrita do temporário; só então mover/substituir o
          + destino, preferindo `Files.move(temp, destino, StandardCopyOp
          +tion.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)`. Se o 
          +movimento/substituição falhar, o processo retorna código `2` e
          + o destino anterior permanece intacto; o arquivo temporário é 
          +removido quando possível. O arquivo de destino nunca é aberto 
          +diretamente para escrever o JSON progressivamente.            
      518 +**Alternativa descartada:** abrir `--output` diretamente e esc
          +rever o JSON incrementalmente — mais simples de codar, mas dei
          +xa uma janela em que uma falha no meio da escrita (processo in
          +terrompido, disco cheio) produz um arquivo parcialmente escrit
          +o exatamente no caminho que o resto do contrato promete preser
          +var intacto.                                                  
      519 +**Consequência:** a garantia de não corromper `--output` passa
          + a ser verdadeira por construção, não por sorte de nunca falha
          +r no meio da escrita; o custo é uma etapa extra de escrita-e-m
          +ovimentação em vez de uma escrita única, e a exigência de que 
          +o temporário fique no mesmo diretório do destino para que `ATO
          +MIC_MOVE` seja viável na maioria dos sistemas de arquivos.    
      520 +                                                              
      521 +#### Extensão — Dia 2                                         
      522 +                                                              
      523 +A mesma estratégia passa a cobrir também as falhas de política
          +/câmbio: como essas falhas retornam antes de o processo sequer
          + tentar abrir um arquivo temporário (§3), um `--output` preexi
          +stente nunca é tocado por elas — a garantia original se estend
          +e sem exigir nenhuma mudança na mecânica de escrita em si.    
      524 +                                                              
      525  ### DT-011 — Política como modelo externo imutável, resolvido 
           por centro de custo (substitui DT-007)
      526  
      527  **Contexto:** RN-019 exige política lida de arquivo (`--politi
           ca`), com uma tabela `padrao` e um mapa de tabelas por centro 
           de custo, cada uma com categorias dinâmicas (`limite` + `perio
           dicidade`).

● Now fixing the DT-012 typo and DT-014/DT-015 content:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      532  ### DT-012 — Leitura e validação de política externa isolada e
           m componente próprio
      533  
      534  **Contexto:** RN-022 exige que política estruturalmente inváli
           da impeça toda a apuração, com a mesma severidade de arquivo i
           legível.
      535 -**Decisão:** `LeitorPolitica` (pacote `leitor`) segue o mesmo 
          -padrão já estabelecido por `ValidadorEnvelope`: uma função est
          -ática que recebe `JsonNode`, valida every contrato estrutural 
          -de §4.1.1, e devolve `PoliticaExterna` ou lança uma exceção de
          -dicada (`PoliticaInvalidaException`) que o `Main` traduz para 
          -exit `2`.                                                     
      535 +**Decisão:** `LeitorPolitica` (pacote `leitor`) segue o mesmo 
          +padrão já estabelecido por `ValidadorEnvelope`: uma API públic
          +a `ler(Path)` (§5) que internamente faz parsing via `JsonNode`
          +, valida todo o contrato estrutural de §4.1.1, e devolve `Poli
          +ticaExterna` ou lança uma exceção dedicada (`PoliticaInvalidaE
          +xception`) que o `Main` traduz para exit `2`.                 
      536  **Alternativa descartada:** validar campo a campo dentro do pr
           óprio `Main` — replicaria a mistura de responsabilidades (I/O 
           + regra de validação) que `ValidadorEnvelope` já evita para o 
           envelope de despesas.
      537  **Consequência:** o `Main` ganha mais uma chamada de leitor no
            início do fluxo, sem crescer em complexidade de validação — a
            mesma forma de `ValidadorEnvelope.validar(raiz)` já em uso.
      538  

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 2 lines, removed 2 lines
      546  ### DT-014 — Parsing de `despesa.moeda` distinguindo ausência 
           de chave e valor `null`
      547  
      548  **Contexto:** RN-002 exige que a ausência da chave `moeda` res
           olva silenciosamente para `BRL`, mas que a chave presente com 
           valor `null` seja `CAMPO_AUSENTE` — o único campo do contrato 
           com essa assimetria.
      549 -**Decisão:** reaproveitar a semântica já presente em `JsonNode
          -.get(chave)` (DT-005): referência Java `null` quando a chave n
          -ão existe, instância de `NullNode` (`isNull() == true`) quando
          - a chave existe com valor `null`. Um método de validação dedic
          -ado (`validarMoeda`, §8) checa `== null` antes de checar `.isN
          -ull()` — a ordem dos dois `if` é o que preserva a distinção.  
      550 -**Alternativa descartada:** usar `elemento.path("moeda")` (que
          - colapsa ausência e `null` em `MissingNode`) ou um acessor per
          -missivo (`asText()` com valor default) — qualquer um dos dois 
          -destruiria a distinção antes que o código de validação pudesse
          - vê-la.                                                       
      549 +**Decisão:** reaproveitar a semântica já presente em `JsonNode
          +.get(chave)` (DT-005): referência Java `null` quando a chave n
          +ão existe, instância de `NullNode` (`isNull() == true`) quando
          + a chave existe com valor `null`. Um método de validação dedic
          +ado (`validarMoeda`, §8) checa `== null` antes de checar `.isN
          +ull()` — a ordem dos dois `if` é o que preserva a distinção. `
          +get()`, não `path()`, é escolhido por coerência com a camada e
          +xistente e por deixar o ramo de ausência explícito antes de ac
          +essar qualquer propriedade do nó (§8 detalha por que `path()` 
          +também distinguiria os casos, mas não é a escolha feita).     
      550 +**Alternativa descartada:** um acessor permissivo com valor pa
          +drão (ex.: `asText("BRL")`) — colapsaria ausência, `null` e qu
          +alquer outro caso que não produza texto num único resultado `"
          +BRL"`, destruindo a distinção antes que o código de validação 
          +pudesse vê-la.                                                
      551  **Consequência:** nenhuma técnica nova é introduzida além da j
           á usada para os outros sete campos — só a ordem dos `if` e a i
           nterpretação do primeiro caso (`null` → retorno silencioso em 
           vez de motivo) mudam.
      552  
      553  ### DT-015 — Conversão cambial com arredondamento único (esten
           de DT-004)

● Now DT-015 consequence update to match the real canary test:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      555  **Contexto:** RN-004/RN-020 proíbem dois arredondamentos (um a
           ntes e um depois da conversão) — só um, depois da multiplicaçã
           o.
      556  **Decisão:** o estágio de resolução de câmbio (§9) produz `val
           orConvertidoBruto` **sem** chamar `setScale` — só o normalizad
           or (RN-004), estágio seguinte do pipeline, arredonda. Nenhum p
           onto intermediário do código chama `setScale`/`round` sobre um
            valor monetário.
      557  **Alternativa descartada:** arredondar dentro do próprio resol
           utor de câmbio, antes de devolver o valor — introduziria o ris
           co de um segundo arredondamento acidental se o normalizador ta
           mbém arredondasse (dupla aplicação de `HALF_UP` pode, em casos
            de fronteira, produzir resultado diferente de uma única aplic
           ação sobre o produto não arredondado).
      558 -**Consequência:** o teste-canário de conversão (USD `40,00 × 5
          -,50 = 220,00`, §14) só passa se exatamente um estágio arredond
          -ar — qualquer regressão que introduza um segundo arredondament
          -o é detectável por esse teste.                                
      558 +**Consequência:** o teste-canário real (`1.005 × 1.005`, §14) 
          +detecta arredondamento do valor bruto **antes** da multiplicaç
          +ão — produziria `1,02` em vez do `1,01` correto. Ele **não** g
          +arante, sozinho, que não exista um segundo arredondamento idem
          +potente depois (um `setScale` redundante sobre um valor que já
          + tem escala 2 é indetectável só pela saída); por isso essa gar
          +antia depende também da responsabilidade arquitetural desta DT
          + (só o normalizador arredonda) e de revisão de código nas task
          +s futuras, não só do teste automatizado.                      
      559  
      560  ### DT-016 — Comparação textual exata de `centro_custo` (forma
           liza a técnica de RN-019)
      561  

● Now the second large piece — §17 Estratégia de testes, restoring the full
  original matrix and adding the corrected extension:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 56 lines, removed 12 lines
      602  
      603  ## 17. Estratégia de testes (planejada — nenhum teste criado n
           esta tarefa)
      604  
      605 -A estrutura de três níveis de DT-009 (unitário majoritário, po
          -ucos testes de integração de pipeline completo, contrato/CLI) 
          -continua sendo a estratégia adotada. Esta seção só **planeja**
          - a extensão da matriz de rastreabilidade — nenhum arquivo de t
          -este é criado ou modificado nesta tarefa; a criação é trabalho
          - de `tasks.md`, a partir de `T-022`.                          
      605 +Esta seção restaura a estratégia e a matriz completas da v1.0 
          +deste plano (equivalente ao antigo §9), e as estende para a sp
          +ec 1.2. Nenhum arquivo de teste é criado ou modificado nesta t
          +arefa — a criação é trabalho de `tasks.md`, a partir de `T-022
          +`.                                                            
      606  
      607 -**Extensão planejada da matriz de rastreabilidade (nomes de cl
          -asse provisórios, sujeitos a ajuste na task real):**          
      607 +- **Nível e proporção:** majoritariamente unitário (uma regra 
          +de negócio = um grupo de testes isolado, sem I/O real); testes
          + de integração de pipeline completo contra `exemplos/despesas-
          +exemplo.json` **e**, a partir da spec 1.2, contra os dois arqu
          +ivos de `exemplos/envelope/`; testes de contrato/CLI cobrindo 
          +códigos de saída, comportamento de stdout/stderr e a escrita a
          +tômica de `--output`.                                         
      608 +- **Cada `RN-NNN` tem teste?** Garantido pela matriz de rastre
          +abilidade abaixo — todo `RN-001` a `RN-022` aparece em pelo me
          +nos uma linha.                                                
      609 +- **Casos de borda da seção 7:** cobertos pelos mesmos grupos 
          +de teste da matriz, via os `CA-NNN` correspondentes, que deriv
          +am diretamente da tabela de casos de borda e, a partir da spec
          + 1.2, também de `§12`.                                        
      610 +- **Nomenclatura:** classe/grupo nomeado pelo identificador da
          + regra (`RN004NormalizacaoMonetariaTest`); método nomeado pelo
          + cenário e resultado esperado, carregando também o `CA-NNN` ap
          +licável — por nome de método (`rn004_ca009_100_005_arredondaPa
          +raCima_100_01()`), `@DisplayName` (`"RN-004 / CA-009 — 100.005
          + arredonda para 100,01"`) ou comentário imediatamente acima do
          + caso. O objetivo é permitir busca textual direta tanto por `R
          +N-004` quanto por `CA-009` e chegar ao mesmo teste.           
      611 +- **Fixture de saída esperada:** o fixture usado por `ExemploC
          +ompletoTest` é **escrito e revisado manualmente** a partir do 
          +schema completo das seções 4.3 a 4.5 da spec — nunca gerado pe
          +lo próprio motor em teste, o que tornaria o teste circular. Us
          +a a tabela 4.7 (ou, para os cenários da política v4, `§12`) co
          +mo fonte de decisões e valores, e RN-017 mais a ordem de 8.3 p
          +ara montar o objeto completo de cada motivo (`codigo`, `regra`
          +, `campo`). A comparação é **estrutural** (JSON contra JSON, c
          +ampo a campo), nunca textual.                                 
      612 +- **Comando único de execução:** `mvn test` executa **todos** 
          +os testes planejados desta matriz, inclusive os de integração 
          +e os de contrato/CLI — todos seguem o sufixo `*Test` (Maven Su
          +refire), nunca `*IT` (que exigiria o Maven Failsafe). `mvn pac
          +kage` executa esses mesmos testes antes de gerar o JAR, porque
          + a fase `test` precede `package` no ciclo de vida padrão do Ma
          +ven.                                                          
      613  
      609 -| RN / CA (planejado) | Grupo de teste planejado | Nível |    
      614 +### Matriz de rastreabilidade — restaurada da v1.0 (RN-001 a R
          +N-018, CA-001 a CA-023)                                       
      615 +                                                              
      616 +| RN / CA | Grupo de teste planejado | Nível |                
      617  |---|---|---|
      611 -| RN-021, RN-022 · CA-035, CA-036 | `LeitorPoliticaTest`, `Lei
          -torCambioTest` — arquivo ausente/ilegível/JSON inválido/estrut
          -uralmente inválido → falha; `taxas: {}` válido; `vigencia` aus
          -ente invalida o arquivo | Unidade |                           
      612 -| RN-019 · CA-024 a CA-027, CA-038 a CA-040 | `ResolutorPoliti
          -caCentroCustoTest` — centro cadastrado, desconhecido, ausente,
          - nulo, tipo inválido; comparação textual exata (sem trim/caixa
          -/acento); limite zero em centro cadastrado vs. em `padrao` | U
          -nidade |                                                      
      613 -| RN-020 · CA-028 a CA-034 | `ResolucaoCambioTest` — cotação e
          -xata, fallback para anterior, proibição de futura, `MOEDA_SEM_
          -COTACAO`, arredondamento único | Unidade |                    
      618 +| RN-001 · CA-020 | `EnvelopeValidoTest` — período invertido, 
          +`despesas` vazia, bloco `colaborador` malformado tolerado | Un
          +idade |                                                       
      619 +| RN-002 · CA-021 · CA-022 · CA-023 | `ContratoDoItemTest` — `
          +CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` 
          +por campo, `ITEM_TIPO_INVALIDO`, ordem canônica de múltiplos m
          +otivos | Unidade |                                            
      620 +| RN-002 (valor_informado) | `ValorInformadoTest` — preservaçã
          +o do valor bruto para tipos válidos e inválidos (string, boole
          +ano, ausente, elemento não-objeto) | Unidade |                
      621 +| RN-003 · CA-019 | `IdDuplicadoTest` — todas as ocorrências r
          +ecusadas, ID inválido não entra na verificação | Unidade |    
      622 +| RN-004 · CA-009 · CA-018 | `NormalizacaoMonetariaTest` (para
          +metrizado) — `33.333`, `33.335`, `33.345`, `100.004`, `100.005
          +` | Unidade |                                                 
      623 +| RN-005 · CA-015 | `NormalizacaoCategoriaTest` — caixa, acent
          +o, espaço nas pontas; `transporte urbano` não reconhecido | Un
          +idade |                                                       
      624 +| RN-006 · CA-017 | `ValorNaoPositivoTest` — negativo, zero, v
          +alor que normaliza para zero; total do período não reduz | Uni
          +dade |                                                        
      625 +| RN-007 · CA-016 | `CategoriaForaPoliticaTest` — `coworking` 
          +recusado antes de qualquer teto | Unidade |                   
      626 +| RN-008 · CA-011 · CA-012 | `CompetenciaTest` — bordas inclus
          +ivas do período, data fora da janela | Unidade |              
      627 +| RN-009 · CA-008 · CA-009 | `NotaFiscalTest` (parametrizado) 
          +— `100,00` elegível, `100,01` recusado, deslocamento de fronte
          +ira por arredondamento | Unidade |                            
      628 +| RN-010 · CA-013 · CA-014 | `DuplicidadeEconomicaTest` — chav
          +e exata, primeira ocorrência mantida, `100.00`/`100.01` não sã
          +o duplicata | Unidade |                                       
      629 +| RN-011 · RN-012 · CA-004 | `TetoDiarioTest` — agregação por 
          +data e categoria (alimentação e transporte urbano) | Unidade |
      630 +| RN-013 · CA-007 | `TetoHospedagemTest` — teto por lançamento
          +, independente de descrição, duas hospedagens no mesmo dia som
          +ando até R$ 500,00 | Unidade |                                
      631 +| RN-014 · CA-005 | `ReembolsoParcialTest` — corte no teto, nu
          +nca recusa integral por ultrapassagem | Unidade |             
      632 +| RN-015 · CA-006 | `DistribuicaoTetoTest` — consumo de saldo 
          +em ordem de `indice_entrada`, estado `NAO_REEMBOLSADO_TETO_ESG
          +OTADO` distinto de `RECUSADO` | Unidade |                     
      633 +| RN-016 · CA-010 | `RegraViagemEfeitoNuloTest` — troca de des
          +crição/campo `em_viagem` desconhecido não altera resultado, it
          +em único para isolar de RN-010 | Unidade |                    
      634 +| — (campos desconhecidos) | `CamposDesconhecidosTest` — campo
          + fora do contrato em `despesa` e em `colaborador` é ignorado s
          +ilenciosamente | Unidade |                                    
      635 +| RN-017 · CA-002 | `ComposicaoSaidaTest` — toda posição produ
          +z um registro, ordem da entrada preservada, nenhum item desapa
          +rece | Unidade |                                              
      636 +| — (ordem de apresentação, 8.3/8.4) | `OrdemMotivosTest` — os
          + dois exemplos normativos de 8.4 (três motivos simultâneos; ex
          +clusão de nota por valor não positivo) | Unidade |            
      637 +| RN-018 · CA-001 · CA-003 | `TotalPeriodoTest` — soma dos `va
          +lor_reembolsavel` apresentados igual ao total | Unidade/Integr
          +ação |                                                        
      638 +| CA-001 · CA-002 · CA-003 (integral) | `ExemploCompletoTest` 
          +— os 14 itens de `exemplos/despesas-exemplo.json` processados 
          +de ponta a ponta, comparados estruturalmente contra o fixture 
          +esperado (escrito à mão a partir de 4.3–4.5); `total_reembolsa
          +vel` = R$ 585,43 | Integração |                               
      639 +| — (contrato de execução) | `CliContratoTest` — código `0` em
          + sucesso; código `2` para argumento ausente/arquivo inexistent
          +e/JSON sintaticamente inválido/falha de escrita; código `3` pa
          +ra envelope inválido; mensagem em stderr, nada em stdout | Con
          +trato/CLI |                                                   
      640 +| — (escrita atômica de `--output`, DT-010) | `EscritaAtomicaS
          +aidaTest` — envelope inválido, JSON sintaticamente inválido e 
          +falha simulada antes da substituição não alteram um arquivo pr
          +eexistente; sucesso substitui o destino; nenhum temporário rem
          +anescente | Contrato/CLI |                                    
      641 +                                                              
      642 +Todo identificador de `RN-001` a `RN-018` e de `CA-001` a `CA-
          +023` aparece em pelo menos uma linha da tabela acima.         
      643 +                                                              
      644 +### Extensão da matriz — spec 1.2 (RN-019 a RN-022, CA-024 a C
          +A-049)                                                        
      645 +                                                              
      646 +| RN / CA | Grupo de teste planejado | Nível |                
      647 +|---|---|---|                                                 
      648 +| RN-021, RN-022 (política) · CA-035, CA-036, CA-045 | `Leitor
          +PoliticaTest` — arquivo ausente/ilegível/JSON inválido; cada u
          +ma das quinze validações estruturais de §5 (raiz não objeto, `
          +vigencia` ausente/malformada, `moeda_base` diferente de `"BRL"
          +`, `limite` zero em `padrao`, `periodicidade` fora de `"dia"`/
          +`"diaria"`, etc.); um arquivo que satisfaz integralmente o con
          +trato (CA-045) é aceito | Unidade |                           
      649 +| RN-022 (câmbio) · CA-036, CA-046 | `LeitorCambioTest` — arqu
          +ivo ausente/ilegível/JSON inválido; `taxas: {}` válido; `moeda
          +_base` errada, data/moeda/taxa inválidas → falha; um arquivo q
          +ue satisfaz integralmente o contrato (CA-046) é aceito | Unida
          +de |                                                          
      650 +| RN-019 · CA-024, CA-025, CA-026, CA-027 | `ResolutorPolitica
          +CentroCustoTest` — centro cadastrado, desconhecido, ausente, n
          +ulo, tipo inválido; comparação textual exata (sem trim/caixa/a
          +cento); categoria ausente do centro cadastrado não recebe o li
          +mite de `padrao` (CA-025); `representacao` reembolsável só ond
          +e declarada (CA-026); limite `0,00` em centro cadastrado → `CA
          +TEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (CA-027) | Unidade |   
      651 +| RN-020 · CA-029, CA-030, CA-031 | `ResolucaoCambioTest` — co
          +tação exata, fallback para a mais recente anterior (CA-029), p
          +roibição de cotação futura, `MOEDA_SEM_COTACAO` para moeda nun
          +ca cotada (CA-030), conversão com arredondamento único (CA-031
          +, exemplo funcional — não é o teste-canário, ver §14) | Unidad
          +e |                                                           
      652 +| RN-020 (motivo, 8.4 item 14) | `MoedaSemCotacaoTest` — o mot
          +ivo `MOEDA_SEM_COTACAO` carrega `campo = despesa.moeda` (§10);
          + coexistência de `MOEDA_SEM_COTACAO` com `CATEGORIA_FORA_POLIT
          +ICA`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMP
          +ETENCIA` na mesma posição — nunca com `VALOR_NAO_POSITIVO`/`NO
          +TA_FISCAL_AUSENTE`/`DUPLICIDADE`/teto | Unidade |             
      653  | RN-002 (moeda) · CA-048 | `CampoMoedaTest` — ausência de cha
           ve vs. `null` explícito vs. tipo inválido vs. formato inválido
            | Unidade |
      615 -| RN-009 (atualizada) | `NotaFiscalConvertidaTest` — gatilho a
          -plicado sobre o valor já convertido, não o original | Unidade 
          -|                                                             
      616 -| RN-010 (atualizada) · CA-028 | `DuplicidadeEntreMoedasTest` 
          -— mesma data/categoria/fornecedor/descrição/valor convertido, 
          -moedas diferentes → não são duplicatas | Unidade |            
      617 -| RN-019 (periodicidade) · CA-047, CA-049 | `TetoPorPeriodicid
          -adeTest` — categoria externa com `"dia"` (saldo compartilhado)
          -, categoria externa com `"diaria"` diferente de hospedagem (`T
          -ETO_INDIVIDUAL_APLICADO`) | Unidade |                         
      618 -| — (contrato de execução) · CA-041 a CA-044 | `CliContratoTes
          -t` (estendido) — quatro flags em qualquer ordem, flag repetida
          -/desconhecida, política/câmbio inválidos → exit `2` | Contrato
          -/CLI |                                                        
      619 -| — (regressão) · CA-037, CA-038 | `RegressaoHistoricaTest` / 
          -extensão de `ExemploCompletoTest` — os quatro totais normativo
          -s (`585.43`, `351.43`, `1143.26`, `373.76`) contra as fixtures
          - reais de `exemplos/envelope/` | Integração |                 
      654 +| RN-009 (atualizada) · CA-032 | `NotaFiscalConvertidaTest` — 
          +gatilho aplicado sobre o valor já convertido (USD 40,00 → R$22
          +0,00 sem nota → recusado), não o valor original na moeda da de
          +spesa | Unidade |                                             
      655 +| RN-010 (atualizada) · CA-033 | `DuplicidadeEntreMoedasTest` 
          +— mesma data/categoria/fornecedor/descrição/valor convertido, 
          +moedas diferentes → **não** são duplicatas | Unidade |        
      656 +| RN-016 (extensão a moeda) · CA-028 | `RegraViagemEfeitoNuloT
          +est` (estendido) — `despesa.moeda` diferente de `BRL` não ampl
          +ia teto algum nem afeta outros itens do mesmo dia/período; RN-
          +016 continua sem efeito | Unidade |                           
      657 +| RN-017 (atualizada) · CA-034 | `SaidaCambioTest` — os quatro
          + formatos de `moeda`/`taxa_cambio_aplicada`/`data_cotacao_util
          +izada` (BRL; estrangeira convertida; estruturalmente inválida;
          + válida sem cotação) | Unidade |                              
      658 +| RN-019 (periodicidade) · CA-047, CA-049 | `TetoPorPeriodicid
          +adeTest` — categoria externa com `"dia"` compartilhando saldo 
          +(`representacao`, CA-047), categoria externa com `"diaria"` di
          +ferente de hospedagem (`estacionamento`, `TETO_INDIVIDUAL_APLI
          +CADO`, CA-049) | Unidade |                                    
      659 +| — (contrato de execução) · CA-041 a CA-044 | `CliContratoTes
          +t` (estendido) — quatro flags em qualquer ordem, flag repetida
          +/desconhecida, política/câmbio inválidos → exit `2`; `--output
          +` preexistente preservado em qualquer falha | Contrato/CLI |  
      660 +| — (regressão histórica) · CA-037, CA-038 | `RegressaoHistori
          +caTest` (extensão de `ExemploCompletoTest`) — `exemplos/despes
          +as-exemplo.json` sob política externa histórica (`585.43`, CA-
          +037) e sob `politica-v4.json`/`CC-ENG-PLATAFORMA` (`351.43`, C
          +A-038), incluindo as quatro mudanças de item declaradas em `§1
          +2.2` | Integração |                                           
      661 +| — (integração do envelope) · CA-039, CA-040 | `IntegracaoEnv
          +elopeTest` — `despesas-envelope.json`/Rafael/`CC-COMERCIAL` co
          +m `politica-v4.json`+`cambio.json` reais (`1143.26`, CA-039) e
          + `despesas-envelope-cc-desconhecido.json`/Dani/`CC-SUPORTE-N2`
          + (`373.76`, CA-040), ambos comparados contra `§12.3`/`§12.4` |
          + Integração |                                                 
      662  
      621 -Todo identificador novo (`RN-019` a `RN-022`, `CA-024` a `CA-0
          -49`) precisará aparecer em pelo menos uma linha desta matriz q
          -uando as tasks reais forem criadas — a mesma garantia que a v1
          -.0 já dava para `RN-001` a `RN-018`/`CA-001` a `CA-023`.      
      663 +Todo identificador de `RN-019` a `RN-022` e de `CA-024` a `CA-
          +049` aparece em pelo menos uma linha da tabela acima — conferi
          +do linha a linha ao escrever esta seção: `CA-024`–`027` (Resol
          +utorPoliticaCentroCustoTest), `CA-028` (RegraViagemEfeitoNuloT
          +est), `CA-029`–`031` (ResolucaoCambioTest), `CA-032` (NotaFisc
          +alConvertidaTest), `CA-033` (DuplicidadeEntreMoedasTest), `CA-
          +034` (SaidaCambioTest), `CA-035`/`036`/`045` (LeitorPoliticaTe
          +st), `CA-036`/`046` (LeitorCambioTest), `CA-037`/`038` (Regres
          +saoHistoricaTest), `CA-039`/`040` (IntegracaoEnvelopeTest), `C
          +A-041`–`044` (CliContratoTest), `CA-047`/`049` (TetoPorPeriodi
          +cidadeTest), `CA-048` (CampoMoedaTest).                       
      664  
      665 +**Declaração final, só depois da conferência acima:** todo ide
          +ntificador de `RN-001` a `RN-022` e de `CA-001` a `CA-049` apa
          +rece em pelo menos uma linha desta matriz (as duas tabelas des
          +ta seção, juntas).                                            
      666 +                                                              
      667  ---
      668  
      669  ## 18. Riscos (extensão)

● Now §18 — restoring the eight original risks with the two required revisions:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 17 lines, removed 2 lines
      666  
      667  ---
      668  
      669 -## 18. Riscos (extensão)                                      
      669 +## 18. Riscos                                                 
      670  
      671 -Os sete riscos da v1.0 deste plano continuam válidos e não são
          - repetidos aqui. Riscos novos, específicos da política v4:    
      671 +Esta seção restaura os **oito** riscos da v1.0 deste plano (nã
          +o sete) e os estende. Dois deles — o de "excesso de arquitetur
          +a" e o de "mudança do Dia 2 exigir I/O" — se materializaram de
          + fato nesta revisão e são reformulados abaixo, sem apagar o re
          +gistro original.                                              
      672  
      673 +### Riscos da v1.0 — preservados, com dois marcados como mater
          +ializados                                                     
      674 +                                                              
      675  | Risco | Probabilidade | O que faço se acontecer |
      676  |---|---|---|
      677 +| Parsing de número via `double` reintroduzido em algum ponto 
          +(ex. troca de biblioteca, refactor apressado) | Média | Teste-
          +canário de `100.005` (RN-004) falha imediatamente e aponta o p
          +onto exato da regressão. |                                    
      678 +| Coerção implícita de tipo pelo Jackson (`asBoolean()`/`asInt
          +()` em vez de checagem explícita de `JsonNodeType`) | Média | 
          +Revisão de código restrita a essa camada de leitura antes de f
          +echar RN-002; teste dedicado por campo cobrindo booleano-como-
          +número e o inverso. |                                         
      679 +| Regra parando no primeiro motivo encontrado (short-circuit i
          +ndevido) | Média | `OrdemMotivosTest` e os testes de RN-002 co
          +m múltiplos motivos (`CA-021`, `CA-023`) capturam isso diretam
          +ente. |                                                       
      680 +| **[MATERIALIZADO]** Excesso de arquitetura (motor de regras 
          +genérico, configuração externa) sob tentação de "preparar para
          + o Dia 2" | Era "baixa mas real" na v1.0 — o Dia 2 realmente i
          +ntroduziu política externa por centro de custo (RN-019), então
          + a premissa original ("a spec atual não tem essa necessidade")
          + deixou de valer tal como escrita | O risco remanescente é mai
          +s estreito, não desapareceu: mesmo com a política agora extern
          +a e dinâmica, **não construir** um motor de regras genérico ou
          + DSL além do contrato fechado que RN-019 já define (duas perio
          +dicidades, tabela plana categoria→limite) — DT-011 documenta e
          +ssa fronteira explicitamente; qualquer generalização além diss
          +o só quando um requisito real (não hipotético) a exigir, nunca
          + antes. |                                                     
      681 +| **[MATERIALIZADO]** Mudança de requisito do Dia 2 exigir toc
          +ar código de I/O além do núcleo | Era "desconhecida — não ante
          +cipada" na v1.0 — o Dia 2 realmente exigiu isso: dois leitores
          + externos novos e extensão do parser de CLI | A fronteira núcl
          +eo/CLI (§2) absorveu a mudança como previsto: `LeitorPolitica`
          +/`LeitorCambio` (DT-012/DT-013) isolam a leitura externa, e o 
          +`Main` estendido (DT-018) isola a extensão de flags — nenhuma 
          +regra de negócio migrou para a camada de I/O, confirmando que 
          +a separação original era a decisão correta. |                 
      682 +| Teste de integração não ser descoberto pelo Maven por nomenc
          +latura inadequada (ex. sufixo `*IT` sem o Maven Failsafe confi
          +gurado) | Média — foi um problema real encontrado na auditoria
          + deste plano | Todo teste, inclusive integração e CLI, usa o s
          +ufixo `*Test` (DT-009/DT-010); `mvn test` sozinho é o comando 
          +de verificação antes de qualquer commit, nunca um plugin adici
          +onal só por nomenclatura. |                                   
      683 +| Escrita direta e progressiva em `--output` corromper ou trun
          +car o arquivo em caso de falha no meio da serialização | Média
          + | Escrever sempre em arquivo temporário no mesmo diretório do
          + destino e mover atomicamente (DT-010); o destino nunca é aber
          +to diretamente para escrita incremental. |                    
      684 +| Categoria estruturalmente válida, mas fora do vocabulário fe
          +chado, ser descartada (virar nula) antes de RN-007 conseguir c
          +ompará-la | Média — foi um erro real encontrado na auditoria d
          +este plano | Modelo corrigido em §4: a categoria normalizada é
          + sempre produzida quando o campo é estruturalmente válido, inc
          +lusive quando o resultado é `coworking` ou qualquer outro valo
          +r fora da política; só RN-007/RN-019 decidem o motivo de recus
          +a. |                                                          
      685 +                                                              
      686 +### Riscos novos, específicos da política v4                  
      687 +                                                              
      688 +| Risco | Probabilidade | O que faço se acontecer |           
      689 +|---|---|---|                                                 
      690  | Confundir "ausência de chave" com "`null` explícito" no camp
           o `moeda`, tratando os dois igualmente | Média — é o único cam
           po do contrato com essa assimetria, fácil de esquecer sob pres
           são de copiar o padrão dos outros sete campos | `CampoMoedaTes
           t` cobre os quatro casos (§17) separadamente; DT-014 documenta
            a ordem exata dos `if` que preserva a distinção. |
      691  | Arredondar duas vezes na conversão cambial (uma vez no resol
           utor de câmbio, outra no normalizador) | Média | Teste-canário
            `40,00 × 5,50 = 220,00` (§14); DT-015 declara explicitamente 
           que só um estágio arredonda. |
      692  | Reintroduzir um `Set<String>` fixo de categorias nos agregad
           ores de teto, quebrando a generalização por `periodicidade` ex
           igida por RN-019 | Média — os agregadores atuais já têm esse `
           Set` hardcoded, e generalizá-los é a mudança mais invasiva des
           ta revisão | DT-017 documenta a decisão; `TetoPorPeriodicidade
           Test` exercita uma categoria externa (não `alimentacao`/`trans
           porte_urbano`/`hospedagem`) sob cada periodicidade. |

● Now §19 — closing the open naming decisions in the file-impact table:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 3 lines, removed 1 line
      711  | `modelo/Periodicidade.java` | Enum fechado `DIA`/`DIARIA`. |
      712  | `modelo/TabelaCambio.java` | Modelo imutável de cotações, já
            invertido para consulta eficiente (§7). |
      713  | `pipeline/ResolutorPoliticaCentroCusto.java` | Resolve a tab
           ela aplicável e consulta categorias (§6). |
      714 -| `pipeline/ResolutorCambio.java` (ou `ConversorCambio.java`) 
          -| Estágio novo do pipeline: resolve taxa e produz valor conver
          -tido bruto (§9). |                                            
      714 +| `pipeline/ResolutorCambio.java` | Estágio novo do pipeline: 
          +resolve taxa, enriquece `ItemValidado` com `moeda`/`taxaCambio
          +Aplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto`, e gra
          +va `MOEDA_SEM_COTACAO` (`campo = CampoCanonico.MOEDA`) quando 
          +aplicável (§9). Nome definitivo — não há alternativa em aberto
          + para `tasks.md`. |                                           
      715 +| `pipeline/AgregadorTetoIndividual.java` | Substitui `Agregad
          +orTetoHospedagem.java` (ver "Substituídos" abaixo): processa q
          +ualquer categoria com `periodicidade: "diaria"`, não só `hospe
          +dagem` (§11, DT-017). |                                       
      716  
      717  **Substituídos:**
      718  
      719  | Arquivo | Motivo |
      720  |---|---|
      721  | `modelo/PoliticaReembolso.java` | Estrutura de quatro consta
           ntes fixas (DT-007) — substituída por `PoliticaExterna` + `Tab
           elaCategoria` (DT-011). Todo consumidor atual (`AvaliadorRegra
           sIndividuais`, `AgregadorTetoDiario`) precisa trocar a fonte d
           o limite/gatilho. |
      722 +| `pipeline/AgregadorTetoHospedagem.java` | Exclusivo de `hosp
          +edagem` (nome e escopo fixos) — substituído por `pipeline/Agre
          +gadorTetoIndividual.java` (ver "Criados" acima), que processa 
          +qualquer categoria com `periodicidade: "diaria"`, escolhendo `
          +TETO_HOSPEDAGEM_APLICADO` para `hospedagem` e `TETO_INDIVIDUAL
          +_APLICADO` para as demais (§11, DT-017). |                    
      723  
      724  **Estendidos (arquivo existente, lógica adicionada sem reescri
           ta total):**
      725  

● Now removing AgregadorTetoHospedagem from the "Estendidos" table (it moved to
  "Substituídos") and closing the ItemValidado ambiguity:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 3 lines, removed 4 lines
      726  | Arquivo | Extensão |
      727  |---|---|
      728  | `Main.java` | Parser de argumentos para quatro flags (§3, DT
           -018); chamadas a `LeitorPolitica`/`LeitorCambio` antes de `Va
           lidadorEnvelope`. |
      729 -| `modelo/ItemValidado.java` | Campo `moeda` (String, resolvid
          -o conforme §8) e possivelmente os três campos de câmbio, se de
          -cidido carregá-los no mesmo registro em vez de numa estrutura 
          -intermediária separada (decisão de implementação, não de plano
          -). |                                                          
      729 +| `modelo/ItemValidado.java` | Decisão fechada (§4): ganha qua
          +tro campos — `moeda`, `taxaCambioAplicada`, `dataCotacaoUtiliz
          +ada`, `valorConvertidoBruto` — populados pelo estágio `Resolut
          +orCambio` antes do `Normalizador`. Nenhuma estrutura intermedi
          +ária alternativa é criada. |                                  
      730  | `pipeline/ValidadorItem.java` | Método `validarMoeda` (§8, D
           T-014). |
      731  | `pipeline/Normalizador.java` | Passa a normalizar sobre `val
           orConvertidoBruto`, não sobre `despesa.valor` bruto, quando há
            conversão (§9). |
      732  | `pipeline/AvaliadorRegrasIndividuais.java` | RN-019 (categor
           ia via `ResolutorPoliticaCentroCusto`, não conjunto fixo), RN-
           009 (gatilho de `PoliticaExterna`, não `PoliticaReembolso`), e
           xclusão por `MOEDA_SEM_COTACAO`. |
      733 -| `pipeline/DetectorDuplicidadeEconomica.java` | Chave estendi
          -da com `moeda` (§12). |                                       
      733 +| `pipeline/DetectorDuplicidadeEconomica.java` | Chave estendi
          +da com `moeda` (§12, CA-033). |                               
      734  | `pipeline/AgregadorTetoDiario.java` | Generalização por `per
           iodicidade` em vez de `Set<String>` fixo (§11, DT-017); limite
            vindo da tabela resolvida, não de `PoliticaReembolso`. |
      735 -| `pipeline/AgregadorTetoHospedagem.java` | Generalização para
          - qualquer categoria com `periodicidade: "diaria"`, com `TETO_I
          -NDIVIDUAL_APLICADO` para categorias diferentes de `hospedagem`
          - (§11, DT-017). |                                             
      736 -| `pipeline/CompositorSaida.java` | Três campos novos no `Resu
          -ltadoItem`; `ESTAGIO_POR_CODIGO`/`ORDEM_CAMPO` estendidos (§10
          -, DT-019). |                                                  
      735 +| `pipeline/CompositorSaida.java` | Três campos novos no `Resu
          +ltadoItem`; `ESTAGIO_POR_CODIGO`/`ORDEM_CAMPO` estendidos (§10
          +, DT-019) — inclui `CampoCanonico.MOEDA` já disponível para o 
          +`campo` de `MOEDA_SEM_COTACAO`, sem exigir mecanismo novo além
          + dos mapas já estendidos. |                                   
      736  | `escritor/EscritorResultado.java` | Serialização de `moeda`,
            `taxa_cambio_aplicada`, `data_cotacao_utilizada` (§9). |
      737  | `modelo/MotivoCodigo.java` | Três valores novos (§10, DT-019
           ). |
      738  | `modelo/RegraNegocio.java` | Quatro valores novos (§10, DT-0
           19). |

● Now updating §21 (Preparação para tasks) for naming consistency:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 2 lines, removed 2 lines
      766  - **Política externa:** `LeitorPolitica` + `PoliticaExterna` +
            `TabelaCategoria` + `Periodicidade` (§5, DT-011/DT-012); test
           es de estrutura válida/inválida, incluindo `limite: 0` em `pad
           rao` vs. em `centros_custo`.
      767  - **Câmbio externo:** `LeitorCambio` + `TabelaCambio` invertid
           a (§7, DT-013); testes de cotação exata, fallback anterior, pr
           oibição de futura, `taxas: {}`.
      768  - **Contrato do campo `moeda`:** `validarMoeda` em `ValidadorI
           tem` (§8, DT-014); testes dos quatro casos (ausente, `null`, t
           ipo inválido, formato inválido).
      769 -- **Conversão:** estágio novo do pipeline entre detector de ID
          - duplicado e normalizador (§9, DT-015); teste-canário de arred
          -ondamento único.                                              
      769 +- **Conversão:** `ResolutorCambio`, estágio novo do pipeline e
          +ntre detector de ID duplicado e normalizador (§9, DT-015); tes
          +te-canário real de ordem de arredondamento (`1.005 × 1.005`, §
          +14) — não o exemplo funcional `40,00 × 5,50`.                 
      770  - **Resolução por centro de custo:** `ResolutorPoliticaCentroC
           usto` (§6, DT-016); testes de centro cadastrado/desconhecido/a
           usente/nulo/tipo inválido e de comparação textual exata.
      771 -- **Periodicidade e tetos:** generalização de `AgregadorTetoDi
          -ario`/`AgregadorTetoHospedagem` (§11, DT-017); testes de categ
          -oria externa sob cada periodicidade.                          
      771 +- **Periodicidade e tetos:** extensão de `AgregadorTetoDiario`
          + e criação de `AgregadorTetoIndividual` em substituição a `Agr
          +egadorTetoHospedagem` (§11, DT-017); testes de categoria exter
          +na sob cada periodicidade.                                    
      772  - **Novos motivos e saída:** extensão de `MotivoCodigo`/`Regra
           Negocio`/`CampoCanonico`, `CompositorSaida`, `EscritorResultad
           o` (§10, §4, DT-019); testes de ordem de apresentação com os m
           otivos novos.
      773  - **Regressões:** fixture histórica externa (§16) e os quatro 
           cenários financeiros do envelope como testes de integração.
      774  - **Integração:** teste ponta a ponta com `politica-v4.json` +
            `cambio.json` + os dois arquivos de despesas do envelope, com
           parando contra `§12` da spec.

● Now let's verify no banned phrases remain and check overall consistency:

  Searched for 1 pattern (ctrl+o to expand)

● All banned phrases are gone. Now checking for AgregadorTetoHospedagem
  references that should have been updated, and confirming DT numbering has no
  duplicates:

  Searched for 1 pattern, read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 2 lines, removed 2 lines
      567  ### DT-017 — Tetos generalizados por periodicidade, não por no
           me de categoria
      568  
      569  **Contexto:** RN-019/AMB-036/AMB-037 exigem que o mecanismo de
            teto (compartilhado vs. individual) e o motivo emitido depend
           am da `periodicidade` configurada, não de uma lista fixa de no
           mes de categoria — uma política válida pode declarar `represen
           tacao` como `"dia"` ou `estacionamento` como `"diaria"`, categ
           orias que não existiam na v1.1.
      570 -**Decisão:** os dois agregadores de teto (generalizações de `A
          -gregadorTetoDiario`/`AgregadorTetoHospedagem`, §11) consultam 
          -a `periodicidade` resolvida pelo `ResolutorPoliticaCentroCusto
          -` para decidir a qual mecanismo um item pertence — o `Set<Stri
          -ng>` fixo de categorias do agregador diário atual é removido. 
          -A escolha de `regra`/`codigo` do motivo (`RN_011`/`RN_012`/`RN
          -_019` para `"dia"`; `TETO_HOSPEDAGEM_APLICADO`/`TETO_INDIVIDUA
          -L_APLICADO` para `"diaria"`) continua sendo uma pequena tabela
          - de exceção por nome de categoria **dentro** do agregador — po
          -rque é isso que a spec pede (nomes históricos continuam recebe
          -ndo seus códigos históricos), não uma contradição com a genera
          -lização do mecanismo.                                         
      570 +**Decisão:** os dois agregadores de teto — `AgregadorTetoDiari
          +o` (estendido) e `AgregadorTetoIndividual` (novo, em substitui
          +ção a `AgregadorTetoHospedagem` — decisão fechada, §11/§19) — 
          +consultam a `periodicidade` resolvida pelo `ResolutorPoliticaC
          +entroCusto` para decidir a qual mecanismo um item pertence — o
          + `Set<String>` fixo de categorias do agregador diário atual é 
          +removido. A escolha de `regra`/`codigo` do motivo (`RN_011`/`R
          +N_012`/`RN_019` para `"dia"`; `TETO_HOSPEDAGEM_APLICADO`/`TETO
          +_INDIVIDUAL_APLICADO` para `"diaria"`) continua sendo uma pequ
          +ena tabela de exceção por nome de categoria **dentro** do agre
          +gador — porque é isso que a spec pede (nomes históricos contin
          +uam recebendo seus códigos históricos), não uma contradição co
          +m a generalização do mecanismo.                               
      571  **Alternativa descartada:** manter dois conjuntos fixos de nom
           es e simplesmente adicionar `representacao`/`estacionamento` a
            eles — funcionaria para os quatro cenários do envelope, mas q
           uebraria na primeira política externa futura que declarasse um
           a quinta categoria com qualquer periodicidade, exatamente o ti
           po de acoplamento que RN-019 elimina.
      572 -**Consequência:** o agregador de teto passa a receber, por ite
          -m, a `periodicidade` e o `limite` já resolvidos (não os lê de 
          -`PoliticaReembolso`), tornando `AgregadorTetoDiario`/`Agregado
          -rTetoHospedagem` (ou seus sucessores) independentes de qualque
          -r lista de nomes fixa para decidir aplicabilidade.            
      572 +**Consequência:** o agregador de teto passa a receber, por ite
          +m, a `periodicidade` e o `limite` já resolvidos (não os lê de 
          +`PoliticaReembolso`), tornando `AgregadorTetoDiario` e `Agrega
          +dorTetoIndividual` independentes de qualquer lista de nomes fi
          +xa para decidir aplicabilidade.                               
      573  
      574  ### DT-018 — `CLI` valida quatro flags como conjunto, não como
            sequência de casos (estende DT-003)
      575  
  Read 1 file (ctrl+o to expand)

● Header preserved. Now running the required final verification:

  Searched for 1 pattern, read 1 file, ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      130  | **Item de entrada** | Igual à v1.0: `indiceEntrada` + `raw` 
           (fotografia de auditoria). |
      131  | **Campos estruturalmente validados** | Passa a ter **oito** 
           campos candidatos em vez de sete: os sete já existentes, mais 
           `moeda` — mas `moeda` é populado de forma diferente dos demais
            (ver §8, "Campo `despesa.moeda`"): resolve para `"BRL"` quand
           o a chave está ausente, sem produzir motivo; fica ausente (nul
           o) quando a chave existe e é estruturalmente inválida (mesma r
           egra dos demais campos nesse caso). |
      132  | **`valor_informado`** | Sem mudança — continua o valor JSON 
           bruto de `despesa.valor`. |
      133 -| **`ItemValidado` enriquecido pelo `ResolutorCambio` (passo 5
          - de 8.1)** | Decisão fechada (não uma alternativa em aberto): 
          -`ResolutorCambio` **estende o próprio `ItemValidado`** com qua
          -tro campos novos — `moeda` (`"BRL"` ou a moeda estrangeira val
          -idada), `taxaCambioAplicada` (`1` para BRL, a taxa resolvida p
          -ara estrangeira, nulo se `MOEDA_SEM_COTACAO`), `dataCotacaoUti
          -lizada` (nulo para BRL, a data efetivamente usada para estrang
          -eira, nulo se sem cotação) e `valorConvertidoBruto` (o produto
          - `valor × taxa`, **ainda sem arredondamento** — o arredondamen
          -to é RN-004, que acontece no passo seguinte). Nenhuma estrutur
          -a intermediária alternativa (um "item com câmbio" à parte de `
          -ItemValidado`) é criada nesta versão do plano — ver §9 e DT-01
          -1x/§19. Quando não há cotação, o próprio `ResolutorCambio` gra
          -va o motivo `MOEDA_SEM_COTACAO` com `campo = CampoCanonico.MOE
          -DA` (§10). |                                                  
      133 +| **`ItemValidado` enriquecido pelo `ResolutorCambio` (passo 5
          + de 8.1)** | Decisão fechada (não uma alternativa em aberto): 
          +`ResolutorCambio` **estende o próprio `ItemValidado`** com qua
          +tro campos novos — `moeda` (`"BRL"` ou a moeda estrangeira val
          +idada), `taxaCambioAplicada` (`1` para BRL, a taxa resolvida p
          +ara estrangeira, nulo se `MOEDA_SEM_COTACAO`), `dataCotacaoUti
          +lizada` (nulo para BRL, a data efetivamente usada para estrang
          +eira, nulo se sem cotação) e `valorConvertidoBruto` (o produto
          + `valor × taxa`, **ainda sem arredondamento** — o arredondamen
          +to é RN-004, que acontece no passo seguinte). Nenhuma estrutur
          +a intermediária alternativa (um "item com câmbio" à parte de `
          +ItemValidado`) é criada nesta versão do plano — ver §9 e §19. 
          +Quando não há cotação, o próprio `ResolutorCambio` grava o mot
          +ivo `MOEDA_SEM_COTACAO` com `campo = CampoCanonico.MOEDA` (§10
          +). |                                                          
      134  | **`valor_normalizado`** | Continua `BigDecimal` de escala 2,
            mas agora resultado de RN-004 aplicado sobre `valorConvertido
           Bruto` (BRL: o próprio valor original, já que a taxa é `1`) — 
           um único arredondamento, nunca dois. Nulo quando `despesa.valo
           r` é estruturalmente inválido **ou** quando há `MOEDA_SEM_COTA
           CAO`. |
      135  | **Categoria normalizada** | Sem mudança de mecanismo (RN-005
           ) — mas a partir de RN-019 é comparada contra a tabela de polí
           tica **efetivamente aplicável** (resolvida por centro de custo
           ), não contra um conjunto fixo de três nomes. |
      136  | **Tabela de política resolvida (nova)** | Estrutura devolvid
           a pelo resolutor de centro de custo (§6): a tabela efetivament
           e aplicável (mapa categoria → `{limite, periodicidade}`), a or
           igem (`PADRAO` ou o nome do centro cadastrado), e um método de
            consulta por categoria normalizada que devolve presença/ausên
           cia, `limite`, `periodicidade` e o motivo correto quando a cat
           egoria está ausente ou tem `limite` zero (RN-019). |

● Fixed the typo. Re-confirming only plan.md is touched:

  Ran 1 shell command (ctrl+o to expand)

● Correção estrutural concluída. Resumo:

  1. DTs históricas restauradas: DT-001 a DT-010 têm agora o texto original
  completo do commit anterior (git show HEAD:...), verbatim. DT-001, DT-002 e
  DT-004 ficam só com o texto original (sem subseção de extensão, conforme
  pedido). DT-003, DT-005, DT-006, DT-008, DT-009 e DT-010 ganham #### Extensão 
  — Dia 2 logo após o texto original. DT-007 preserva o texto original integral
  e é explicitamente marcada SUPERADA por DT-011. DT-011 a DT-019 seguem depois,
  sem renumeração.

  2. Decisões arquiteturais fechadas: pipeline/ResolutorCambio.java (nome único,
  sem alternativa "ou ConversorCambio"); modelo/ItemValidado.java estendido
  definitivamente com
  moeda/taxaCambioAplicada/dataCotacaoUtilizada/valorConvertidoBruto (sem
  estrutura intermediária alternativa); pipeline/AgregadorTetoIndividual.java
  como componente novo que substitui AgregadorTetoHospedagem.java;
  LeitorPolitica.ler(Path)/LeitorCambio.ler(Path) como API pública única.

  3. MOEDA_SEM_COTACAO.campo: corrigido em §10 — carrega CampoCanonico.MOEDA
  ("despesa.moeda"), no mesmo padrão de ID_DUPLICADO (CampoCanonico.ID); o
  contrato completo de campo (estruturais, ID_DUPLICADO, MOEDA_SEM_COTACAO,
  demais nulos, ITEM_TIPO_INVALIDO nulo) está explícito, sem precisar de mudança
  em CompositorSaida além dos mapas já estendidos em DT-019.

  4. Novo teste-canário: 1.005 × 1.005 substitui a falsa alegação sobre 40,00 × 
  5,50 (mantido só como exemplo funcional de CA-031). O canário real detecta
  arredondamento prematuro (1,02 incorreto vs. 1,01 correto) e o texto registra
  explicitamente que ele não garante, sozinho, ausência de um segundo
  arredondamento idempotente — isso depende também de DT-015 e de revisão de
  código.

  5. Cobertura final da matriz (§17): restaurada a matriz completa
  RN-001–018/CA-001–023 da v1.0, com a extensão corrigida: CA-028 (moeda não
  amplia teto), CA-033 (duplicidade entre moedas), CA-045/046 (testes
  estruturais de política/câmbio), CA-037–040 nos dois testes de integração (não
  mais citados na linha unitária do resolutor). Conferência linha a linha
  confirma RN-001 a RN-022 e CA-001 a CA-049 cobertos.

  6. Riscos: restaurados os oito originais (não sete); os dois relacionados a
  "excesso de arquitetura" e "Dia 2 exigir I/O" foram marcados [MATERIALIZADO] e
  reformulados; os cinco riscos novos da v4 seguem depois.

  7. git status --short:
   M specs/001-motor-reembolso/plan.md

  git diff --check sem erros (só o aviso de CRLF do Windows); spec, DECISIONS,
  tasks, README, src/ e exemplos/envelope/ com diff vazio. Nenhum /export, git
  add, commit ou push executado.

  Parando para revisão.

✻ Baked for 20m 22s

※ recap: We've been updating the Dia 2 technical plan (plan.md) for the reimbursement engine to match spec 1.2's external policy/exchange-rate rules. The structural correction is done and verified as the only file changed; next step is your review before any further work. (disable recaps in /config)

> Faça uma última correção de consistência e fechamento arquitetural no plano.  
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  ```text                                                                       
  specs/001-motor-reembolso/plan.md                                             
  ```                                                                           
                                                                                
  Não altere spec, DECISIONS, tasks, README, código, testes, fixtures ou        
  `exemplos/envelope/`.                                                         
                                                                                
  Mantenha:                                                                     
                                                                                
  ```text                                                                       
  Versão: 1.1                                                                   
  Status: em revisão                                                            
  Baseado na spec: 1.2 (aprovado)                                               
  Última alteração: 2026-08-05                                                  
  ```                                                                           
                                                                                
  Não crie novas DTs, RN, AMB ou CA.                                            
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  ## 1. Corrigir campos opcionais conhecidos da política                        
                                                                                
  Na validação de cada configuração de categoria:                               
                                                                                
  * `observacao` é um campo conhecido;                                          
  * é opcional;                                                                 
  * quando presente, deve ser texto;                                            
  * quando presente com qualquer outro tipo, o arquivo de política é            
  estruturalmente inválido;                                                     
  * o valor é validado e depois descartado, pois não entra no modelo e não      
  participa de regra.                                                           
                                                                                
  Não liste `observacao` entre campos desconhecidos.                            
                                                                                
  Campos realmente desconhecidos continuam ignorados:                           
                                                                                
  * na raiz da política;                                                        
  * dentro da configuração de uma categoria.                                    
                                                                                
  `versao` e `acrescimo_em_viagem_percentual` continuam aceitos como metadados  
  de qualquer tipo e ignorados.                                                 
                                                                                
  Atualize a lista exaustiva de `LeitorPolitica`, os testes planejados e        
  qualquer contagem fixa de validações.                                         
                                                                                
  ## 2. Corrigir campos opcionais conhecidos do câmbio                          
                                                                                
  Substitua a regra segundo a qual tipo incorreto de `fonte` ou `observacao` é  
  tolerado.                                                                     
                                                                                
  Contrato correto:                                                             
                                                                                
  * `fonte` e `observacao` são opcionais;                                       
  * quando presentes, devem ser texto;                                          
  * tipo diferente de texto invalida estruturalmente o arquivo;                 
  * os valores são validados e descartados.                                     
                                                                                
  Restrinja a tolerância de campos desconhecidos à raiz de `cambio.json`.       
                                                                                
  Não diga que campos desconhecidos são ignorados “em qualquer nível”:          
                                                                                
  * toda chave diretamente dentro de `taxas` deve ser uma data ISO real;        
  * toda chave dentro do objeto de uma data deve ser uma moeda `[A-Z]{3}`;      
  * nesses dois mapas, uma chave malformada não é “campo desconhecido”: é       
  violação estrutural.                                                          
                                                                                
  Atualize `LeitorCambioTest` para cobrir `fonte` e `observacao` presentes com  
  tipo inválido.                                                                
                                                                                
  ## 3. Fechar o resultado do resolutor de política                             
                                                                                
  Remova:                                                                       
                                                                                
  ```text                                                                       
  método do próprio resolutor ou de um objeto de resultado que ele devolve      
  ```                                                                           
                                                                                
  Adote definitivamente:                                                        
                                                                                
  ```java                                                                       
  ResolutorPoliticaCentroCusto.resolver(                                        
      String centroCusto,                                                       
      PoliticaExterna politica                                                  
  ): TabelaPoliticaResolvida                                                    
  ```                                                                           
                                                                                
  Crie no plano:                                                                
                                                                                
  ```text                                                                       
  modelo/TabelaPoliticaResolvida.java                                           
  ```                                                                           
                                                                                
  Modelo imutável contendo:                                                     
                                                                                
  * mapa imutável de categorias da única tabela selecionada;                    
  * origem, usando enum interno fechado:                                        
                                                                                
    * `PADRAO`;                                                                 
    * `CENTRO_CUSTO`;                                                           
  * nome do centro cadastrado, preenchido somente na origem `CENTRO_CUSTO`.     
                                                                                
  A entrada `String centroCusto` pode ser nula:                                 
                                                                                
  * nulo representa ausência, `null` explícito ou tipo inválido já tolerado     
  pelo envelope;                                                                
  * texto reconhecido seleciona o centro;                                       
  * texto desconhecido seleciona `padrao`.                                      
                                                                                
  `AvaliadorRegrasIndividuais` consulta o mapa de `TabelaPoliticaResolvida`:    
                                                                                
  * categoria ausente + origem `PADRAO` → `CATEGORIA_FORA_POLITICA`;            
  * categoria ausente + origem `CENTRO_CUSTO` →                                 
  `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`;                                    
  * limite zero em centro cadastrado →                                          
  `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`;                                    
  * limite positivo → usa limite e periodicidade.                               
                                                                                
  Atualize modelo de dados, DT-011, DT-016 e impacto em arquivos.               
                                                                                
  ## 4. Fechar a responsabilidade de `ItemValidado` e `ResolutorCambio`         
                                                                                
  A ordem definitiva é:                                                         
                                                                                
  ### `ValidadorItem`                                                           
                                                                                
  Popula `ItemValidado.moeda`:                                                  
                                                                                
  * chave ausente → `"BRL"`;                                                    
  * moeda válida → texto validado;                                              
  * moeda inválida → nulo e motivo estrutural correspondente.                   
                                                                                
  ### `ResolutorCambio`                                                         
                                                                                
  Não popula novamente `moeda`.                                                 
                                                                                
  Popula somente:                                                               
                                                                                
  * `taxaCambioAplicada`;                                                       
  * `dataCotacaoUtilizada`;                                                     
  * `valorConvertidoBruto`.                                                     
                                                                                
  O estágio é avaliado quando estes três campos necessários estiverem           
  utilizáveis:                                                                  
                                                                                
  ```text                                                                       
  despesa.valor                                                                 
  despesa.moeda                                                                 
  despesa.data                                                                  
  ```                                                                           
                                                                                
  Erro estrutural em campo não utilizado por RN-020 — por exemplo `descricao`,  
  `fornecedor`, `categoria` ou `tem_nota_fiscal` — não impede a resolução       
  cambial.                                                                      
                                                                                
  ### BRL                                                                       
                                                                                
  Para BRL estruturalmente válido:                                              
                                                                                
  ```text                                                                       
  taxaCambioAplicada = 1                                                        
  dataCotacaoUtilizada = null                                                   
  valorConvertidoBruto = valor original                                         
  ```                                                                           
                                                                                
  Isso também se aplica quando a chave `moeda` estava ausente e foi resolvida   
  para BRL.                                                                     
                                                                                
  ### Moeda estrangeira com cotação                                             
                                                                                
  ```text                                                                       
  valorConvertidoBruto = valor original × taxa                                  
  ```                                                                           
                                                                                
  sem arredondamento nesse estágio.                                             
                                                                                
  ### Moeda sem cotação                                                         
                                                                                
  Os três campos derivados ficam nulos, com `MOEDA_SEM_COTACAO`.                
                                                                                
  ### Normalizador                                                              
                                                                                
  O `Normalizador` usa exclusivamente:                                          
                                                                                
  ```text                                                                       
  valorConvertidoBruto                                                          
  ```                                                                           
                                                                                
  Não mantenha a alternativa:                                                   
                                                                                
  ```text                                                                       
  ou o valor original, para BRL                                                 
  ```                                                                           
                                                                                
  O mesmo caminho de normalização deve servir para BRL e moeda estrangeira.     
                                                                                
  Atualize §4, §9, DT-015, impacto em arquivos e preparação de tasks.           
                                                                                
  ## 5. Fechar o contrato do subcomando e dos pares de argumentos               
                                                                                
  Na CLI e em DT-018, registre:                                                 
                                                                                
  * o primeiro token deve ser exatamente `calcular`;                            
  * `calcular` é o único token posicional permitido;                            
  * subcomando ausente, diferente ou token posicional extra → exit `2`;         
  * depois de `calcular`, os argumentos são pares `flag valor`;                 
  * flag sem valor → exit `2`;                                                  
  * quantidade ímpar de tokens depois do subcomando → exit `2`;                 
  * cada uma das quatro flags aparece exatamente uma vez;                       
  * flag desconhecida ou repetida → exit `2`;                                   
  * o comando antigo com somente `--input` e `--output` → exit `2`;             
  * `--cambio` permanece obrigatório mesmo para entrada somente BRL.            
                                                                                
  Atualize a linha da matriz para escrever explicitamente:                      
                                                                                
  ```text                                                                       
  CA-041 · CA-042 · CA-043 · CA-044                                             
  ```                                                                           
                                                                                
  Não use apenas o intervalo `CA-041 a CA-044`.                                 
                                                                                
  A linha de `CliContratoTest` deve mencionar:                                  
                                                                                
  * sucesso `0` do comando completo;                                            
  * ordem embaralhada válida;                                                   
  * subcomando ausente/incorreto;                                               
  * flag ausente, repetida ou desconhecida;                                     
  * flag sem valor;                                                             
  * comando antigo;                                                             
  * política/câmbio inválidos;                                                  
  * preservação byte a byte do output;                                          
  * stdout vazio e stderr em falha.                                             
                                                                                
  ## 6. Corrigir o risco e a matriz de arredondamento                           
                                                                                
  Na tabela de riscos, remova como mitigação:                                   
                                                                                
  ```text                                                                       
  Teste-canário 40,00 × 5,50 = 220,00                                           
  ```                                                                           
                                                                                
  Use:                                                                          
                                                                                
  ```text                                                                       
  O teste 1.005 × 1.005 detecta arredondamento prematuro antes da               
  multiplicação: resultado correto 1,01; resultado incorreto 1,02. A existência 
  de um único ponto de arredondamento também é garantida por DT-015 e por       
  revisão de código, porque um segundo setScale idempotente pode não ser        
  observável na saída.                                                          
  ```                                                                           
                                                                                
  Em `ResolucaoCambioTest`, mantenha:                                           
                                                                                
  * `40,00 × 5,50 = 220,00` como cenário normativo de CA-031;                   
  * `1.005 × 1.005` como teste técnico adicional de ordem de arredondamento.    
                                                                                
  ## 7. Reforçar os testes da periodicidade                                     
                                                                                
  Na linha de `TetoPorPeriodicidadeTest`, além de `representacao` e             
  `estacionamento`, inclua:                                                     
                                                                                
  * `hospedagem` configurada com `"dia"`:                                       
                                                                                
    * teto compartilhado;                                                       
    * `TETO_DIARIO_APLICADO`/`TETO_DIARIO_ESGOTADO`;                            
    * regra `RN-019`, não RN-013;                                               
                                                                                
  * `alimentacao` configurada com `"diaria"`:                                   
                                                                                
    * teto individual;                                                          
    * `TETO_INDIVIDUAL_APLICADO`;                                               
    * regra `RN-019`, não RN-011.                                               
                                                                                
  Isso comprova que o algoritmo realmente depende da periodicidade, não do nome 
  histórico.                                                                    
                                                                                
  ## 8. Atualizar testes estruturais                                            
                                                                                
  Em `LeitorPoliticaTest`, inclua:                                              
                                                                                
  * `observacao` de categoria presente como texto → válido e ignorado;          
  * `observacao` presente com tipo não textual → política inválida.             
                                                                                
  Em `LeitorCambioTest`, inclua:                                                
                                                                                
  * `fonte`/`observacao` ausentes → válido;                                     
  * presentes como texto → válido e ignorado;                                   
  * presentes com tipo não textual → câmbio inválido;                           
  * chave malformada dentro de `taxas` não é campo desconhecido e invalida o    
  arquivo.                                                                      
                                                                                
  ## 9. Verificação textual                                                     
                                                                                
  Confirme que não resta nenhuma ocorrência de:                                 
                                                                                
  ```text                                                                       
  sua ausência ou tipo incorreto não invalida                                   
  Campos desconhecidos em qualquer nível                                        
  método do próprio resolutor ou de um objeto                                   
  para cada item com campos estruturalmente válidos                             
  ou sobre o valor original, para BRL                                           
  Teste-canário `40,00 × 5,50 = 220,00`                                         
  CA-041 a CA-044                                                               
  ```                                                                           
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/plan.md                                 
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  git diff -- specs/001-motor-reembolso/tasks.md                                
  git diff -- README.md                                                         
  git diff -- src                                                               
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * somente `plan.md` foi alterado;                                             
  * nenhuma decisão relevante ficou aberta;                                     
  * RN-001 a RN-022 e CA-001 a CA-049 continuam cobertas;                       
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Ao final, apresente:                                                          
                                                                                
  1. contrato final dos dois leitores;                                          
  2. API final do resolutor de centro de custo;                                 
  3. divisão de responsabilidade entre `ValidadorItem`, `ResolutorCambio` e     
  `Normalizador`;                                                               
  4. contrato final da CLI;                                                     
  5. correção do teste-canário e dos riscos;                                    
  6. alterações na matriz;                                                      
  7. `git status --short`.                                                      
                                                                                
  Pare para revisão.                                                            

  Read 1 file (ctrl+o to expand)

● Starting with §5 — fixing the observacao field treatment in política
  validation:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 4 lines, removed 3 lines
      195  11. Dentro de `padrao`: todo `limite` é **estritamente maior q
           ue zero** — `limite: 0` em `padrao` é falha estrutural do arqu
           ivo inteiro (não chega a produzir um `TabelaCategoria`, porque
            o arquivo inteiro é rejeitado antes).
      196  12. Dentro de qualquer tabela de `centros_custo`: `limite` mai
           or ou igual a zero é estruturalmente válido (o `0,00` vira uma
            decisão de negócio — `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO
           ` — não um defeito de arquivo).
      197  13. `periodicidade` é obrigatória dentro de cada configuração 
           de categoria, e é exatamente `"dia"` ou `"diaria"` — qualquer 
           outro texto (ou tipo) invalida o **arquivo** inteiro, não só a
            categoria.
      198 -14. Campos desconhecidos — na raiz, dentro de uma categoria, o
          -u em qualquer outro nível — são ignorados sem afetar a validad
          -e (`versao`, `acrescimo_em_viagem_percentual`, `observacao`, e
          -tc.).                                                         
      199 -15. O modelo `PoliticaExterna` resultante é construído com cóp
          -ias defensivas imutáveis dos mapas (`Map.copyOf` ou equivalent
          -e) — nenhuma referência ao `JsonNode` de origem, nem ao mapa m
          -utável intermediário usado durante a validação, escapa para fo
          -ra do leitor.                                                 
      198 +14. `observacao`, dentro de uma configuração de categoria, é u
          +m campo **conhecido** (não desconhecido) e **opcional**: quand
          +o ausente, é válido; quando presente, deve ser texto; quando p
          +resente com qualquer outro tipo (número, booleano, lista, obje
          +to), o arquivo de política é estruturalmente inválido. O valor
          +, quando válido, é lido e **descartado** — não entra no modelo
          + `TabelaCategoria` (§5) nem participa de regra alguma (spec 4.
          +1.1: "informativa; nunca lida por regra alguma").             
      199 +15. Campos **verdadeiramente desconhecidos** — não previstos p
          +elo contrato — continuam ignorados sem afetar a validade: na r
          +aiz (`versao`, `acrescimo_em_viagem_percentual`, aceitos como 
          +metadados de qualquer tipo), ou dentro de uma configuração de 
          +categoria (qualquer chave além de `limite`/`periodicidade`/`ob
          +servacao`). `observacao` (item 14) não é um desses campos — é 
          +conhecido e validado quanto ao tipo.                          
      200 +16. O modelo `PoliticaExterna` resultante é construído com cóp
          +ias defensivas imutáveis dos mapas (`Map.copyOf` ou equivalent
          +e) — nenhuma referência ao `JsonNode` de origem, nem ao mapa m
          +utável intermediário usado durante a validação, escapa para fo
          +ra do leitor.                                                 
      201  
      201 -Todo o arquivo é validado — os quinze pontos acima — **antes**
          - de qualquer `TabelaCategoria` ser construída; não existe um c
          -aminho onde parte do modelo já foi montada quando uma violação
          - tardia é detectada.                                          
      202 +Todo o arquivo é validado — os dezesseis pontos acima — **ante
          +s** de qualquer `TabelaCategoria` ser construída; não existe u
          +m caminho onde parte do modelo já foi montada quando uma viola
          +ção tardia é detectada.                                       
      203  
      204  Não existem mais as constantes `60`, `80`, `250`, `100` no cód
           igo de produção depois desta mudança: `PoliticaExterna` é o ún
           ico lugar de onde valores financeiros de política se originam,
            e ela sempre vem de um arquivo (nunca de um literal fixo). A 
           política histórica equivalente (usada na regressão de `§12.1`/
           `§12.2`) é uma **fixture externa de teste** — um JSON no mesmo
            formato, versionado em `tests/resources/` — nunca um valor ha
           rdcoded nem um fallback interno de produção (ver §16).
      205  

  Searched for 1 pattern (ctrl+o to expand)

● Good, only câmbio (line 259) still needs the fix. Now fixing §7 (câmbio's
  fonte/observacao and the "any level" claim):

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 2 lines, removed 2 lines
      256  5. O valor associado a cada data é, ele próprio, um objeto (ma
           pa de moeda para taxa).
      257  6. Cada chave dentro de uma data casa com o padrão `[A-Z]{3}`.
      258  7. Cada taxa é numérica e estritamente positiva (zero e negati
           vos são estruturalmente inválidos).
      259 -8. `fonte` e `observacao`, quando presentes, são texto informa
          -tivo — lidos e descartados, nunca usados por regra alguma; sua
          - ausência ou tipo incorreto não invalida o arquivo (são opcion
          -ais).                                                         
      260 -9. Campos desconhecidos em qualquer nível são ignorados sem af
          -etar a validade.                                              
      259 +8. `fonte` e `observacao`, na raiz, são campos **conhecidos** 
          +e **opcionais**: quando ausentes, o arquivo é válido; quando p
          +resentes, devem ser texto; presentes com qualquer outro tipo i
          +nvalida o arquivo estruturalmente. Os valores, quando válidos,
          + são lidos e **descartados** — nunca usados por regra alguma. 
      260 +9. Campos verdadeiramente desconhecidos são ignorados sem afet
          +ar a validade **apenas na raiz** de `cambio.json` (um campo ex
          +tra ao lado de `moeda_base`/`fonte`/`observacao`/`taxas`). Ess
          +a tolerância **não** se estende aos dois mapas internos de `ta
          +xas`: toda chave de primeiro nível dentro de `taxas` deve ser 
          +uma data ISO real (item 4) e toda chave dentro do objeto de um
          +a data deve ser uma moeda `[A-Z]{3}` (item 6) — nesses dois ní
          +veis, uma chave que não satisfaça o formato exigido **não é um
          + "campo desconhecido"**: é violação estrutural, e invalida o a
          +rquivo inteiro, exatamente como qualquer outra violação desta 
          +lista.                                                        
      261  10. O modelo `TabelaCambio` resultante — já invertido para `mo
           eda → NavigableMap<data, taxa>` (§7, DT-013) — é construído co
           m cópias defensivas imutáveis; nenhum `Map`/`NavigableMap` mut
           ável escapa do leitor.
      262  
      263  ---

● Now §6 — closing the resolver's API and result model:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 31 lines, removed 9 lines
      207  
      208  ## 6. Política por centro de custo
      209  
      210 -**Responsabilidade:** um componente (`ResolutorPoliticaCentroC
          -usto`, no pacote `pipeline`) com uma única função: dado `colab
          -orador.centro_custo` (como veio do envelope — string, nulo, au
          -sente, ou de outro tipo) e a `PoliticaExterna` já validada, re
          -solver **exatamente uma** tabela aplicável.                   
      210 +**Responsabilidade e API pública fechada:**                   
      211  
      212 -- **Centro textual reconhecido** (presente como chave em `cent
          -rosCusto`) → devolve a tabela exclusiva daquele centro, com or
          -igem `CENTRO_CUSTO` e o nome do centro.                       
      213 -- **Centro ausente, nulo, de tipo inválido, ou textual mas des
          -conhecido** → devolve `padrao`, com origem `PADRAO`.          
      214 -- **Comparação:** `String.equals` puro contra as chaves de `ce
          -ntrosCusto` — sem `trim()`, sem `toLowerCase()`, sem normaliza
          -ção de acento, sem correspondência aproximada (RN-019). Isso é
          - uma decisão deliberada de não reaproveitar a normalização de 
          -categoria (RN-005) aqui: os dois campos têm regras de comparaç
          -ão diferentes por design da spec.                             
      215 -- **Nunca mistura:** o resolutor devolve um único `Map<String,
          - TabelaCategoria>` — nunca a união de `padrao` com a tabela do
          - centro. Categorias ausentes da tabela de um centro cadastrado
          - **não** caem de volta em `padrao` (RN-019, RN-007).          
      212 +```java                                                       
      213 +ResolutorPoliticaCentroCusto.resolver(                        
      214 +    String centroCusto,                                       
      215 +    PoliticaExterna politica                                  
      216 +): TabelaPoliticaResolvida                                    
      217 +```                                                           
      218  
      217 -**Consulta por categoria (método do próprio resolutor ou de um
          - objeto de resultado que ele devolve):** dado a tabela resolvi
          -da e a categoria normalizada, devolve uma de três respostas fe
          -chadas:                                                       
      218 -1. Categoria ausente da tabela → motivo é `CATEGORIA_FORA_POLI
          -TICA` se a origem for `PADRAO`, ou `CATEGORIA_NAO_REEMBOLSAVEL
          -_CENTRO_CUSTO` se a origem for `CENTRO_CUSTO` (RN-007/RN-019 —
          - a distinção de motivo depende só da origem da tabela, nunca d
          -o nome da categoria).                                         
      219 -2. Categoria presente com `limite == 0` → sempre `CATEGORIA_NA
          -O_REEMBOLSAVEL_CENTRO_CUSTO` (só ocorre quando a origem é `CEN
          -TRO_CUSTO`, porque `padrao` com limite zero já foi rejeitado n
          -a leitura do arquivo, §5).                                    
      220 -3. Categoria presente com `limite > 0` → devolve `limite` e `p
          -eriodicidade`, para uso pelo avaliador de regras individuais (
          -elegibilidade) e pelo agregador de tetos (§9).                
      219 +Não existe um "método do próprio resolutor ou de um objeto de 
          +resultado que ele devolve" como alternativa em aberto — a deci
          +são é fechada: `resolver(...)` é um método estático (ou de ins
          +tância de um resolutor sem estado) que devolve diretamente o m
          +odelo imutável `modelo/TabelaPoliticaResolvida.java`:         
      220  
      221 +```java                                                       
      222 +TabelaPoliticaResolvida {                                     
      223 +  categorias: Map<String, TabelaCategoria>   // imutável — a ú
          +nica tabela selecionada, nunca a união de duas                
      224 +  origem: Origem                              // enum interno 
          +fechado: PADRAO, CENTRO_CUSTO                                 
      225 +  nomeCentroCusto: String                     // preenchido so
          +mente quando origem == CENTRO_CUSTO; nulo quando origem == PAD
          +RAO                                                           
      226 +}                                                             
      227 +```                                                           
      228 +                                                              
      229 +**Resolução da entrada `String centroCusto`** — o parâmetro po
          +de ser `null`:                                                
      230 +                                                              
      231 +- `null` representa, de forma unificada, os três casos que o e
          +nvelope já tolera antes de chegar aqui (RN-001/RN-019): ausênc
          +ia do bloco `colaborador`, `colaborador.centro_custo` ausente 
          +ou nulo, ou de tipo inválido — a camada que lê o envelope (`En
          +velope`/`ValidadorEnvelope`) já reduz todos esses casos a `nul
          +l` antes de chamar `resolver`.                                
      232 +- **Texto reconhecido** (presente como chave em `politica.cent
          +rosCusto`) → seleciona a tabela exclusiva daquele centro: `ori
          +gem = CENTRO_CUSTO`, `nomeCentroCusto` preenchido, `categorias
          +` = a tabela exclusiva do centro.                             
      233 +- **Texto desconhecido, ou `centroCusto == null`** → seleciona
          + `padrao`: `origem = PADRAO`, `nomeCentroCusto = null`, `categ
          +orias` = `politica.padrao`.                                   
      234 +- **Comparação:** `String.equals` puro contra as chaves de `ce
          +ntrosCusto` — sem `trim()`, sem `toLowerCase()`, sem normaliza
          +ção de acento, sem correspondência aproximada (RN-019, DT-016)
          +. Decisão deliberada de não reaproveitar a normalização de cat
          +egoria (RN-005): os dois campos têm regras de comparação difer
          +entes por design da spec.                                     
      235 +- **Nunca mistura:** `categorias` é sempre um único `Map<Strin
          +g, TabelaCategoria>` — nunca a união de `padrao` com a tabela 
          +de um centro. Categorias ausentes da tabela de um centro cadas
          +trado **não** caem de volta em `padrao` (RN-019, RN-007).     
      236 +                                                              
      237 +**Consumo pelo `AvaliadorRegrasIndividuais`** — o avaliador co
          +nsulta diretamente `tabelaResolvida.categorias` (sem um método
          + de consulta adicional no resolutor) e aplica a régua fechada 
          +de RN-019:                                                    
      238 +                                                              
      239 +1. Categoria ausente do mapa + `origem == PADRAO` → `CATEGORIA
          +_FORA_POLITICA`.                                              
      240 +2. Categoria ausente do mapa + `origem == CENTRO_CUSTO` → `CAT
          +EGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`.                        
      241 +3. Categoria presente com `limite == 0` (só ocorre com `origem
          + == CENTRO_CUSTO`, porque `padrao` com limite zero já foi reje
          +itado na leitura do arquivo, §5) → `CATEGORIA_NAO_REEMBOLSAVEL
          +_CENTRO_CUSTO`.                                               
      242 +4. Categoria presente com `limite > 0` → usa `limite` e `perio
          +dicidade` dessa entrada, para a elegibilidade individual (RN-0
          +19/RN-007) e para o agregador de tetos (§11).                 
      243 +                                                              
      244  ---
      245  
      246  ## 7. Arquivo externo: Câmbio

● Now updating §4 to reference TabelaPoliticaResolvida concretely:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      133  | **`ItemValidado` enriquecido pelo `ResolutorCambio` (passo 5
            de 8.1)** | Decisão fechada (não uma alternativa em aberto): 
           `ResolutorCambio` **estende o próprio `ItemValidado`** com qua
           tro campos novos — `moeda` (`"BRL"` ou a moeda estrangeira val
           idada), `taxaCambioAplicada` (`1` para BRL, a taxa resolvida p
           ara estrangeira, nulo se `MOEDA_SEM_COTACAO`), `dataCotacaoUti
           lizada` (nulo para BRL, a data efetivamente usada para estrang
           eira, nulo se sem cotação) e `valorConvertidoBruto` (o produto
            `valor × taxa`, **ainda sem arredondamento** — o arredondamen
           to é RN-004, que acontece no passo seguinte). Nenhuma estrutur
           a intermediária alternativa (um "item com câmbio" à parte de `
           ItemValidado`) é criada nesta versão do plano — ver §9 e §19. 
           Quando não há cotação, o próprio `ResolutorCambio` grava o mot
           ivo `MOEDA_SEM_COTACAO` com `campo = CampoCanonico.MOEDA` (§10
           ). |
      134  | **`valor_normalizado`** | Continua `BigDecimal` de escala 2,
            mas agora resultado de RN-004 aplicado sobre `valorConvertido
           Bruto` (BRL: o próprio valor original, já que a taxa é `1`) — 
           um único arredondamento, nunca dois. Nulo quando `despesa.valo
           r` é estruturalmente inválido **ou** quando há `MOEDA_SEM_COTA
           CAO`. |
      135  | **Categoria normalizada** | Sem mudança de mecanismo (RN-005
           ) — mas a partir de RN-019 é comparada contra a tabela de polí
           tica **efetivamente aplicável** (resolvida por centro de custo
           ), não contra um conjunto fixo de três nomes. |
      136 -| **Tabela de política resolvida (nova)** | Estrutura devolvid
          -a pelo resolutor de centro de custo (§6): a tabela efetivament
          -e aplicável (mapa categoria → `{limite, periodicidade}`), a or
          -igem (`PADRAO` ou o nome do centro cadastrado), e um método de
          - consulta por categoria normalizada que devolve presença/ausên
          -cia, `limite`, `periodicidade` e o motivo correto quando a cat
          -egoria está ausente ou tem `limite` zero (RN-019). |          
      136 +| **`TabelaPoliticaResolvida` (nova, `modelo/TabelaPoliticaRes
          +olvida.java`)** | Devolvida por `ResolutorPoliticaCentroCusto.
          +resolver(String, PoliticaExterna)` (§6): `categorias` (mapa im
          +utável categoria → `TabelaCategoria`, a única tabela seleciona
          +da), `origem` (enum interno fechado `PADRAO`/`CENTRO_CUSTO`), 
          +`nomeCentroCusto` (preenchido só quando `origem == CENTRO_CUST
          +O`). `AvaliadorRegrasIndividuais` consulta `categorias` direta
          +mente para decidir `CATEGORIA_FORA_POLITICA`/`CATEGORIA_NAO_RE
          +EMBOLSAVEL_CENTRO_CUSTO`/limite e periodicidade (RN-019) — nen
          +hum método de consulta adicional é criado no resolutor. |     
      137  | **Acumulador de motivos** | Sem mudança de mecanismo — só o 
           vocabulário de `MotivoCodigo` cresce (§10). |
      138  | **`Motivo`** | Mesmos três campos (`codigo`, `regra`, `campo
           `), mas os três enums fechados que os representam ganham valor
           es novos: `MotivoCodigo` ganha `MOEDA_SEM_COTACAO`, `CATEGORIA
           _NAO_REEMBOLSAVEL_CENTRO_CUSTO`, `TETO_INDIVIDUAL_APLICADO` (d
           ezesseis valores no total); `RegraNegocio` ganha `RN_019` a `R
           N_022`; `CampoCanonico` ganha `MOEDA`, inserido na posição cor
           reta da ordem canônica de contrato — entre `VALOR` e `TEM_NOTA
           _FISCAL` (oito valores no total, mesma técnica de serialização
            num único ponto — DT-008/DT-019). |
      139  | **Resultado por item** | Ganha três campos novos entre `valo
           r_informado` e `valor_normalizado`: `moeda`, `taxaCambioAplica
           da`, `dataCotacaoUtilizada` — os mesmos três valores gravados 
           em `ItemValidado` pelo `ResolutorCambio`, propagados sem recál
           culo até a composição final. |

● Now updating DT-011 and DT-016 to reference the closed API:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      548  ### DT-011 — Política como modelo externo imutável, resolvido 
           por centro de custo (substitui DT-007)
      549  
      550  **Contexto:** RN-019 exige política lida de arquivo (`--politi
           ca`), com uma tabela `padrao` e um mapa de tabelas por centro 
           de custo, cada uma com categorias dinâmicas (`limite` + `perio
           dicidade`).
      551 -**Decisão:** `PoliticaExterna` (§5) como estrutura imutável co
          -nstruída inteiramente a partir do `JsonNode` do arquivo de pol
          -ítica, após validação estrutural completa (RN-022) — nunca par
          -cialmente populada. Resolução de tabela por centro de custo is
          -olada num componente próprio (`ResolutorPoliticaCentroCusto`, 
          -§6), que não conhece nem `LeitorPolitica` nem regras de negóci
          -o individuais — só resolve "qual tabela" e "o que essa tabela 
          -diz sobre esta categoria".                                    
      551 +**Decisão:** `PoliticaExterna` (§5) como estrutura imutável co
          +nstruída inteiramente a partir do `JsonNode` do arquivo de pol
          +ítica, após validação estrutural completa (RN-022) — nunca par
          +cialmente populada. Resolução de tabela por centro de custo is
          +olada num componente próprio, com API fechada `ResolutorPoliti
          +caCentroCusto.resolver(String centroCusto, PoliticaExterna pol
          +itica): TabelaPoliticaResolvida` (§6), que não conhece nem `Le
          +itorPolitica` nem regras de negócio individuais — só resolve "
          +qual tabela" e devolve o modelo imutável que diz o que essa ta
          +bela declara.                                                 
      552  **Alternativa descartada:** motor de regras genérico ou DSL — 
           RN-019 já é um contrato estrutural fechado e finito (duas peri
           odicidades, tabela plana categoria→limite), não exige um mecan
           ismo de regras arbitrário; construir um seria o mesmo excesso 
           de arquitetura que DT-007 evitava, agora aplicado a um problem
           a levemente maior, mas ainda finito.
      553  **Consequência:** nenhuma constante financeira (`60`/`80`/`250
           `/`100`) permanece no código de produção; toda mudança de limi
           te é edição de um arquivo JSON externo, nunca recompilação.
      554  

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      583  ### DT-016 — Comparação textual exata de `centro_custo` (forma
           liza a técnica de RN-019)
      584  
      585  **Contexto:** RN-019 exige comparação exata, sem trim, sem nor
           malização de caixa ou acento — deliberadamente diferente da no
           rmalização de categoria (RN-005).
      586 -**Decisão:** `ResolutorPoliticaCentroCusto` usa `Map.get(centr
          -oCusto)` diretamente (equivalente a `String.equals`) contra as
          - chaves de `centrosCusto` — nenhuma transformação aplicada ao 
          -valor de `colaborador.centro_custo` antes da consulta.        
      586 +**Decisão:** `ResolutorPoliticaCentroCusto.resolver(String cen
          +troCusto, PoliticaExterna politica)` usa `Map.get(centroCusto)
          +` diretamente (equivalente a `String.equals`) contra as chaves
          + de `centrosCusto` — nenhuma transformação aplicada ao valor d
          +e `colaborador.centro_custo` antes da consulta; `centroCusto =
          += null` (que já representa ausência/nulo/tipo inválido, resolv
          +idos antes por quem lê o envelope) resolve diretamente para `p
          +adrao` sem tentar a busca no mapa.                            
      587  **Alternativa descartada:** reaproveitar a mesma normalização 
           de RN-005 "por consistência" — rejeitada porque a spec declara
            explicitamente que a comparação de centro de custo segue regr
           a própria, distinta da de categoria; unificá-las seria introdu
           zir comportamento não pedido.
      588  **Consequência:** `"CC-COMERCIAL"` e `"cc-comercial"` são cent
           ros de custo diferentes para efeito de resolução de política —
            coerente com o texto normativo de RN-019.
      589  

● Now updating §19 (impacto em arquivos) for the resolver's closed API and new
  model file:
● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 2 lines, removed 1 line
      733  | `modelo/TabelaCategoria.java` | `limite` + `periodicidade` d
           e uma categoria numa tabela. |
      734  | `modelo/Periodicidade.java` | Enum fechado `DIA`/`DIARIA`. |
      735  | `modelo/TabelaCambio.java` | Modelo imutável de cotações, já
            invertido para consulta eficiente (§7). |
      736 -| `pipeline/ResolutorPoliticaCentroCusto.java` | Resolve a tab
          -ela aplicável e consulta categorias (§6). |                   
      736 +| `modelo/TabelaPoliticaResolvida.java` | Modelo imutável devo
          +lvido por `ResolutorPoliticaCentroCusto.resolver(...)` (§6): `
          +categorias`, `origem` (`PADRAO`/`CENTRO_CUSTO`), `nomeCentroCu
          +sto`. |                                                       
      737 +| `pipeline/ResolutorPoliticaCentroCusto.java` | API pública ú
          +nica: `resolver(String centroCusto, PoliticaExterna politica):
          + TabelaPoliticaResolvida` (§6). |                             
      738  | `pipeline/ResolutorCambio.java` | Estágio novo do pipeline: 
           resolve taxa, enriquece `ItemValidado` com `moeda`/`taxaCambio
           Aplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto`, e gra
           va `MOEDA_SEM_COTACAO` (`campo = CampoCanonico.MOEDA`) quando 
           aplicável (§9). Nome definitivo — não há alternativa em aberto
            para `tasks.md`. |
      739  | `pipeline/AgregadorTetoIndividual.java` | Substitui `Agregad
           orTetoHospedagem.java` (ver "Substituídos" abaixo): processa q
           ualquer categoria com `periodicidade: "diaria"`, não só `hospe
           dagem` (§11, DT-017). |
      740  

● Now Task 4 — fixing the ValidadorItem/ResolutorCambio/Normalizador
  responsibility split. Starting with §4:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      130  | **Item de entrada** | Igual à v1.0: `indiceEntrada` + `raw` 
           (fotografia de auditoria). |
      131  | **Campos estruturalmente validados** | Passa a ter **oito** 
           campos candidatos em vez de sete: os sete já existentes, mais 
           `moeda` — mas `moeda` é populado de forma diferente dos demais
            (ver §8, "Campo `despesa.moeda`"): resolve para `"BRL"` quand
           o a chave está ausente, sem produzir motivo; fica ausente (nul
           o) quando a chave existe e é estruturalmente inválida (mesma r
           egra dos demais campos nesse caso). |
      132  | **`valor_informado`** | Sem mudança — continua o valor JSON 
           bruto de `despesa.valor`. |
      133 -| **`ItemValidado` enriquecido pelo `ResolutorCambio` (passo 5
          - de 8.1)** | Decisão fechada (não uma alternativa em aberto): 
          -`ResolutorCambio` **estende o próprio `ItemValidado`** com qua
          -tro campos novos — `moeda` (`"BRL"` ou a moeda estrangeira val
          -idada), `taxaCambioAplicada` (`1` para BRL, a taxa resolvida p
          -ara estrangeira, nulo se `MOEDA_SEM_COTACAO`), `dataCotacaoUti
          -lizada` (nulo para BRL, a data efetivamente usada para estrang
          -eira, nulo se sem cotação) e `valorConvertidoBruto` (o produto
          - `valor × taxa`, **ainda sem arredondamento** — o arredondamen
          -to é RN-004, que acontece no passo seguinte). Nenhuma estrutur
          -a intermediária alternativa (um "item com câmbio" à parte de `
          -ItemValidado`) é criada nesta versão do plano — ver §9 e §19. 
          -Quando não há cotação, o próprio `ResolutorCambio` grava o mot
          -ivo `MOEDA_SEM_COTACAO` com `campo = CampoCanonico.MOEDA` (§10
          -). |                                                          
      133 +| **`ItemValidado` enriquecido pelo `ResolutorCambio` (passo 5
          + de 8.1)** | Decisão fechada, com responsabilidade dividida en
          +tre dois estágios (ver §9 para o detalhamento completo): `Vali
          +dadorItem` (passo 3) já populou `ItemValidado.moeda` — `"BRL"`
          + quando a chave está ausente, o texto validado quando a moeda 
          +estrangeira é estruturalmente válida, ou nulo com motivo estru
          +tural quando inválida (§8). `ResolutorCambio` (passo 5) **não 
          +repopula `moeda`** — consome o valor já resolvido e **estende 
          +`ItemValidado`** com apenas três campos novos: `taxaCambioApli
          +cada` (`1` para BRL, a taxa resolvida para estrangeira, nulo s
          +e `MOEDA_SEM_COTACAO`), `dataCotacaoUtilizada` (nulo para BRL,
          + a data efetivamente usada para estrangeira, nulo se sem cotaç
          +ão) e `valorConvertidoBruto` (o produto `valor × taxa`, **aind
          +a sem arredondamento** — o arredondamento é RN-004, que aconte
          +ce no passo seguinte). Nenhuma estrutura intermediária alterna
          +tiva (um "item com câmbio" à parte de `ItemValidado`) é criada
          + nesta versão do plano — ver §19. Quando não há cotação, o pró
          +prio `ResolutorCambio` grava o motivo `MOEDA_SEM_COTACAO` com 
          +`campo = CampoCanonico.MOEDA` (§10). |                        
      134  | **`valor_normalizado`** | Continua `BigDecimal` de escala 2,
            mas agora resultado de RN-004 aplicado sobre `valorConvertido
           Bruto` (BRL: o próprio valor original, já que a taxa é `1`) — 
           um único arredondamento, nunca dois. Nulo quando `despesa.valo
           r` é estruturalmente inválido **ou** quando há `MOEDA_SEM_COTA
           CAO`. |
      135  | **Categoria normalizada** | Sem mudança de mecanismo (RN-005
           ) — mas a partir de RN-019 é comparada contra a tabela de polí
           tica **efetivamente aplicável** (resolvida por centro de custo
           ), não contra um conjunto fixo de três nomes. |
      136  | **`TabelaPoliticaResolvida` (nova, `modelo/TabelaPoliticaRes
           olvida.java`)** | Devolvida por `ResolutorPoliticaCentroCusto.
           resolver(String, PoliticaExterna)` (§6): `categorias` (mapa im
           utável categoria → `TabelaCategoria`, a única tabela seleciona
           da), `origem` (enum interno fechado `PADRAO`/`CENTRO_CUSTO`), 
           `nomeCentroCusto` (preenchido só quando `origem == CENTRO_CUST
           O`). `AvaliadorRegrasIndividuais` consulta `categorias` direta
           mente para decidir `CATEGORIA_FORA_POLITICA`/`CATEGORIA_NAO_RE
           EMBOLSAVEL_CENTRO_CUSTO`/limite e periodicidade (RN-019) — nen
           hum método de consulta adicional é criado no resolutor. |

● Now updating §9 (Conversão monetária) completely to reflect the closed
  responsibility split:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 22 lines, removed 5 lines
      314  
      315  **Fórmula normativa (RN-020, RN-004):** `valor bruto × taxa`, 
           seguido de **um único** arredondamento `HALF_UP` para duas cas
           as — nunca dois arredondamentos (um na moeda original, outro a
           pós a conversão).
      316  
      317 -**Componente (`pipeline/ResolutorCambio.java`, estágio novo do
          - pipeline, passo 5 de 8.1 — entre o detector de ID duplicado e
          - o normalizador):** para cada item com campos estruturalmente 
          -válidos, `ResolutorCambio` enriquece o próprio `ItemValidado` 
          -(§4) com `moeda`, `taxaCambioAplicada`, `dataCotacaoUtilizada`
          - e `valorConvertidoBruto` — nenhuma estrutura intermediária al
          -ternativa é criada:                                           
      317 +**Divisão de responsabilidade fechada entre três componentes —
          + ordem definitiva, sem alternativa em aberto:**               
      318  
      319 -- `moeda` resolvida (§8) igual a `"BRL"`, ou chave ausente (re
          -solvida para `"BRL"` sem motivo) → taxa implícita `1`, sem con
          -sultar `TabelaCambio`; `data_cotacao_utilizada` fica nula.    
      320 -- `moeda` estrangeira estruturalmente válida → consulta `Tabel
          -aCambio` (§7) pela data da despesa. Cotação resolvida (exata o
          -u anterior) → `valorConvertidoBruto = valor × taxa` (sem arred
          -ondar ainda); `taxaCambioAplicada` e `dataCotacaoUtilizada` pr
          -eenchidos com os valores efetivamente usados. Sem cotação → `R
          -esolutorCambio` grava o motivo `MOEDA_SEM_COTACAO` com `regra 
          -= RN_020` e `campo = CampoCanonico.MOEDA` (serializado `"despe
          -sa.moeda"`, §10) — não `campo` nulo, porque a causa é especifi
          -camente essa chave; `taxaCambioAplicada`/`dataCotacaoUtilizada
          -`/`valorConvertidoBruto` ficam nulos.                         
      321 -- `moeda` estruturalmente inválida (`CAMPO_AUSENTE`/`CAMPO_TIP
          -O_INVALIDO`/`CAMPO_FORMATO_INVALIDO`) → item já recusado por R
          -N-002; este estágio não tenta resolver câmbio para ele (mesma 
          -exclusão de dependência de campo inválido já existente).      
      319 +### `ValidadorItem` (passo 3 de 8.1)                          
      320  
      323 -O **normalizador** (RN-004, estágio seguinte) aplica `setScale
          -(2, RoundingMode.HALF_UP)` sobre `valorConvertidoBruto` (ou so
          -bre o valor original, para BRL, que é o caso `taxa = 1` sem co
          -nversão real) — produzindo `valor_normalizado`. Nenhum outro p
          -onto do pipeline arredonda.                                   
      321 +Popula `ItemValidado.moeda` (§8) — e **somente** `moeda`, nenh
          +um dos três campos de câmbio:                                 
      322  
      323 +- chave `moeda` ausente do objeto → `"BRL"`, sem motivo;      
      324 +- chave presente com moeda estruturalmente válida → o texto va
          +lidado (`"BRL"` ou a moeda estrangeira em `[A-Z]{3}`);        
      325 +- chave presente e estruturalmente inválida (`null`, tipo erra
          +do, formato errado) → `moeda` fica nulo, com o motivo estrutur
          +al correspondente (`CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMP
          +O_FORMATO_INVALIDO`, `campo = CampoCanonico.MOEDA`).          
      326 +                                                              
      327 +### `ResolutorCambio` (`pipeline/ResolutorCambio.java`, estági
          +o novo do pipeline, passo 5 de 8.1 — entre o detector de ID du
          +plicado e o normalizador)                                     
      328 +                                                              
      329 +**Não popula `moeda` novamente** — consome o `ItemValidado.moe
          +da` já resolvido por `ValidadorItem` como entrada. Popula **so
          +mente** três campos: `taxaCambioAplicada`, `dataCotacaoUtiliza
          +da`, `valorConvertidoBruto`.                                  
      330 +                                                              
      331 +O estágio é avaliado quando os três campos de que RN-020 depen
          +de estiverem estruturalmente utilizáveis: `despesa.valor`, `de
          +spesa.moeda`, `despesa.data`. Um erro estrutural num campo que
          + RN-020 **não** usa — `descricao`, `fornecedor`, `categoria` o
          +u `tem_nota_fiscal` — não impede a resolução cambial: o item p
          +ode ter, por exemplo, `categoria` inválida e ainda assim ter `
          +taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBru
          +to` calculados normalmente.                                   
      332 +                                                              
      333 +- **BRL** (`ItemValidado.moeda == "BRL"`, estruturalmente váli
          +do — inclusive quando a chave estava ausente e foi resolvida p
          +ara `"BRL"` por `ValidadorItem`): `taxaCambioAplicada = 1`; `d
          +ataCotacaoUtilizada = null`; `valorConvertidoBruto = valor` or
          +iginal, sem multiplicação nem arredondamento — o mesmo valor, 
          +apenas copiado para o campo de saída deste estágio. Isso vale 
          +identicamente para BRL informado e para BRL assumido por ausên
          +cia de chave; não há um caminho diferente para os dois.       
      334 +- **Moeda estrangeira com cotação resolvida** (exata ou mais r
          +ecente anterior, `TabelaCambio`, §7): `valorConvertidoBruto = 
          +valor original × taxa`, **sem arredondamento nesse estágio**; 
          +`taxaCambioAplicada` e `dataCotacaoUtilizada` preenchidos com 
          +os valores efetivamente usados.                               
      335 +- **Moeda estrangeira sem cotação:** os três campos derivados 
          +(`taxaCambioAplicada`, `dataCotacaoUtilizada`, `valorConvertid
          +oBruto`) ficam nulos; `ResolutorCambio` grava o motivo `MOEDA_
          +SEM_COTACAO` com `regra = RN_020` e `campo = CampoCanonico.MOE
          +DA` (serializado `"despesa.moeda"`, §10).                     
      336 +- **`moeda` estruturalmente inválida** (`ItemValidado.moeda` j
          +á nulo, vindo de `ValidadorItem`) → item já recusado por RN-00
          +2; este estágio não tenta resolver câmbio para ele (mesma excl
          +usão de dependência de campo inválido já existente).          
      337 +                                                              
      338 +### `Normalizador` (RN-004, estágio seguinte)                 
      339 +                                                              
      340 +Usa **exclusivamente** `valorConvertidoBruto` — nunca `despesa
          +.valor` bruto, nunca um caminho separado "ou o valor original,
          + para BRL": `setScale(2, RoundingMode.HALF_UP)` é aplicado sob
          +re `valorConvertidoBruto`, e para BRL isso já produz o resulta
          +do correto porque `ResolutorCambio` já copiou o valor original
          + para `valorConvertidoBruto` no passo anterior. **O mesmo cami
          +nho de normalização serve para BRL e para moeda estrangeira** 
          +— não existe um `if` de BRL dentro do `Normalizador`. Nenhum o
          +utro ponto do pipeline arredonda.                             
      341 +                                                              
      342  **Saída:** os três campos de auditoria (`moeda`, `taxa_cambio_
           aplicada`, `data_cotacao_utilizada`) são propagados sem recálc
           ulo desde este estágio até a composição final (§4) e a seriali
           zação (§13). Não existe campo `valor_convertido` separado — `v
           alor_normalizado` já é o valor final em BRL.
      343  
      344  ---

● Now updating DT-015 for consistency with the responsibility split:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      593  ### DT-015 — Conversão cambial com arredondamento único (esten
           de DT-004)
      594  
      595  **Contexto:** RN-004/RN-020 proíbem dois arredondamentos (um a
           ntes e um depois da conversão) — só um, depois da multiplicaçã
           o.
      596 -**Decisão:** o estágio de resolução de câmbio (§9) produz `val
          -orConvertidoBruto` **sem** chamar `setScale` — só o normalizad
          -or (RN-004), estágio seguinte do pipeline, arredonda. Nenhum p
          -onto intermediário do código chama `setScale`/`round` sobre um
          - valor monetário.                                             
      596 +**Decisão:** o estágio de resolução de câmbio (§9) produz `val
          +orConvertidoBruto` **sem** chamar `setScale` — só o normalizad
          +or (RN-004), estágio seguinte do pipeline, arredonda. Nenhum p
          +onto intermediário do código chama `setScale`/`round` sobre um
          + valor monetário. Isso vale igualmente para BRL: `ResolutorCam
          +bio` copia o valor original para `valorConvertidoBruto` sem to
          +car sua escala, e é só o `Normalizador` — o mesmo caminho usad
          +o para moeda estrangeira — que aplica o arredondamento único (
          +§9 detalha a divisão de responsabilidade completa entre `Valid
          +adorItem`, `ResolutorCambio` e `Normalizador`).               
      597  **Alternativa descartada:** arredondar dentro do próprio resol
           utor de câmbio, antes de devolver o valor — introduziria o ris
           co de um segundo arredondamento acidental se o normalizador ta
           mbém arredondasse (dupla aplicação de `HALF_UP` pode, em casos
            de fronteira, produzir resultado diferente de uma única aplic
           ação sobre o produto não arredondado).
      598  **Consequência:** o teste-canário real (`1.005 × 1.005`, §14) 
           detecta arredondamento do valor bruto **antes** da multiplicaç
           ão — produziria `1,02` em vez do `1,01` correto. Ele **não** g
           arante, sozinho, que não exista um segundo arredondamento idem
           potente depois (um `setScale` redundante sobre um valor que já
            tem escala 2 é indetectável só pela saída); por isso essa gar
           antia depende também da responsabilidade arquitetural desta DT
            (só o normalizador arredonda) e de revisão de código nas task
           s futuras, não só do teste automatizado.
      599  

● Now updating §19's ItemValidado and ResolutorCambio rows:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      752  | `modelo/TabelaCambio.java` | Modelo imutável de cotações, já
            invertido para consulta eficiente (§7). |
      753  | `modelo/TabelaPoliticaResolvida.java` | Modelo imutável devo
           lvido por `ResolutorPoliticaCentroCusto.resolver(...)` (§6): `
           categorias`, `origem` (`PADRAO`/`CENTRO_CUSTO`), `nomeCentroCu
           sto`. |
      754  | `pipeline/ResolutorPoliticaCentroCusto.java` | API pública ú
           nica: `resolver(String centroCusto, PoliticaExterna politica):
            TabelaPoliticaResolvida` (§6). |
      755 -| `pipeline/ResolutorCambio.java` | Estágio novo do pipeline: 
          -resolve taxa, enriquece `ItemValidado` com `moeda`/`taxaCambio
          -Aplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto`, e gra
          -va `MOEDA_SEM_COTACAO` (`campo = CampoCanonico.MOEDA`) quando 
          -aplicável (§9). Nome definitivo — não há alternativa em aberto
          - para `tasks.md`. |                                           
      755 +| `pipeline/ResolutorCambio.java` | Estágio novo do pipeline: 
          +consome `ItemValidado.moeda` (já populado por `ValidadorItem`,
          + não recalculado aqui) e enriquece `ItemValidado` apenas com `
          +taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBru
          +to`; grava `MOEDA_SEM_COTACAO` (`campo = CampoCanonico.MOEDA`)
          + quando aplicável (§9). Nome e responsabilidade definitivos — 
          +não há alternativa em aberto para `tasks.md`. |               
      756  | `pipeline/AgregadorTetoIndividual.java` | Substitui `Agregad
           orTetoHospedagem.java` (ver "Substituídos" abaixo): processa q
           ualquer categoria com `periodicidade: "diaria"`, não só `hospe
           dagem` (§11, DT-017). |
      757  
      758  **Substituídos:**

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 3 lines, removed 3 lines
      767  | Arquivo | Extensão |
      768  |---|---|
      769  | `Main.java` | Parser de argumentos para quatro flags (§3, DT
           -018); chamadas a `LeitorPolitica`/`LeitorCambio` antes de `Va
           lidadorEnvelope`. |
      770 -| `modelo/ItemValidado.java` | Decisão fechada (§4): ganha qua
          -tro campos — `moeda`, `taxaCambioAplicada`, `dataCotacaoUtiliz
          -ada`, `valorConvertidoBruto` — populados pelo estágio `Resolut
          -orCambio` antes do `Normalizador`. Nenhuma estrutura intermedi
          -ária alternativa é criada. |                                  
      771 -| `pipeline/ValidadorItem.java` | Método `validarMoeda` (§8, D
          -T-014). |                                                     
      772 -| `pipeline/Normalizador.java` | Passa a normalizar sobre `val
          -orConvertidoBruto`, não sobre `despesa.valor` bruto, quando há
          - conversão (§9). |                                            
      770 +| `modelo/ItemValidado.java` | Decisão fechada (§4, §9): ganha
          + quatro campos — `moeda` (populado por `ValidadorItem`, junto 
          +com os sete campos já existentes), `taxaCambioAplicada`, `data
          +CotacaoUtilizada`, `valorConvertidoBruto` (estes três, e só es
          +tes três, populados por `ResolutorCambio`). Nenhuma estrutura 
          +intermediária alternativa é criada. |                         
      771 +| `pipeline/ValidadorItem.java` | Método `validarMoeda` (§8, D
          +T-014) — popula `ItemValidado.moeda`; não popula os três campo
          +s de câmbio. |                                                
      772 +| `pipeline/Normalizador.java` | Passa a normalizar **exclusiv
          +amente** sobre `valorConvertidoBruto` — o mesmo caminho para B
          +RL e moeda estrangeira, sem `if` de BRL dentro do `Normalizado
          +r` (§9). |                                                    
      773  | `pipeline/AvaliadorRegrasIndividuais.java` | RN-019 (categor
           ia via `ResolutorPoliticaCentroCusto`, não conjunto fixo), RN-
           009 (gatilho de `PoliticaExterna`, não `PoliticaReembolso`), e
           xclusão por `MOEDA_SEM_COTACAO`. |
      774  | `pipeline/DetectorDuplicidadeEconomica.java` | Chave estendi
           da com `moeda` (§12, CA-033). |
      775  | `pipeline/AgregadorTetoDiario.java` | Generalização por `per
           iodicidade` em vez de `Set<String>` fixo (§11, DT-017); limite
            vindo da tabela resolvida, não de `PoliticaReembolso`. |

● Now updating §21 for consistency:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      807  - **Política externa:** `LeitorPolitica` + `PoliticaExterna` +
            `TabelaCategoria` + `Periodicidade` (§5, DT-011/DT-012); test
           es de estrutura válida/inválida, incluindo `limite: 0` em `pad
           rao` vs. em `centros_custo`.
      808  - **Câmbio externo:** `LeitorCambio` + `TabelaCambio` invertid
           a (§7, DT-013); testes de cotação exata, fallback anterior, pr
           oibição de futura, `taxas: {}`.
      809  - **Contrato do campo `moeda`:** `validarMoeda` em `ValidadorI
           tem` (§8, DT-014); testes dos quatro casos (ausente, `null`, t
           ipo inválido, formato inválido).
      810 -- **Conversão:** `ResolutorCambio`, estágio novo do pipeline e
          -ntre detector de ID duplicado e normalizador (§9, DT-015); tes
          -te-canário real de ordem de arredondamento (`1.005 × 1.005`, §
          -14) — não o exemplo funcional `40,00 × 5,50`.                 
      810 +- **Conversão:** `ResolutorCambio`, estágio novo do pipeline e
          +ntre detector de ID duplicado e normalizador (§9, DT-015) — co
          +nsome `ItemValidado.moeda` já populado por `ValidadorItem`, se
          +m recalculá-lo, e só acrescenta `taxaCambioAplicada`/`dataCota
          +caoUtilizada`/`valorConvertidoBruto`; teste-canário real de or
          +dem de arredondamento (`1.005 × 1.005`, §14) — não o exemplo f
          +uncional `40,00 × 5,50`.                                      
      811  - **Resolução por centro de custo:** `ResolutorPoliticaCentroC
           usto` (§6, DT-016); testes de centro cadastrado/desconhecido/a
           usente/nulo/tipo inválido e de comparação textual exata.
      812  - **Periodicidade e tetos:** extensão de `AgregadorTetoDiario`
            e criação de `AgregadorTetoIndividual` em substituição a `Agr
           egadorTetoHospedagem` (§11, DT-017); testes de categoria exter
           na sob cada periodicidade.
      813  - **Novos motivos e saída:** extensão de `MotivoCodigo`/`Regra
           Negocio`/`CampoCanonico`, `CompositorSaida`, `EscritorResultad
           o` (§10, §4, DT-019); testes de ordem de apresentação com os m
           otivos novos.

● Now Task 5 — closing the CLI subcommand and argument-pairs contract. Updating
  §3:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 10 lines, removed 2 lines
      101  
      102  As quatro flags são **normativas de `spec.md` §4.1.1** (AMB-03
           4) — este plano só descreve como a CLI as implementa, não rede
           fine o contrato.
      103  
      104 -- **Ordem:** qualquer. **Repetição:** cada flag aparece exatam
          -ente uma vez. **Ausência:** as quatro são obrigatórias.       
      105 -- **Parsing:** o parser de argumentos deixa de ser um `switch`
          - com dois casos fixos e passa a acumular as flags reconhecidas
          - num mapa (`Map<String, String>`), contando ocorrências por ch
          -ave. Ao final: se alguma das quatro chaves obrigatórias está a
          -usente do mapa, se alguma chave aparece mais de uma vez, ou se
          - um argumento não reconhecido (`--xyz`) aparece na linha de co
          -mando → exit `2`, mensagem em stderr, nada em stdout, `--outpu
          -t` preexistente preservado.                                   
      104 +**Contrato do subcomando (fechado, sem alternativa em aberto):
          +**                                                            
      105 +                                                              
      106 +- O **primeiro token** da linha de comando deve ser exatamente
          + `calcular`. `calcular` é o **único token posicional** permiti
          +do em toda a linha de comando — todo token seguinte é obrigato
          +riamente uma flag ou o valor de uma flag, nunca outro posicion
          +al.                                                           
      107 +- Subcomando ausente (nenhum argumento), diferente de `calcula
          +r` (ex.: `computar`), ou qualquer token posicional extra (ex.:
          + `calcular extra --input ...`) → exit `2`.                    
      108 +- **Depois de `calcular`**, os argumentos restantes são consum
          +idos estritamente aos pares `flag valor` — nunca uma flag "sol
          +ta" seguida de outra flag.                                    
      109 +- **Flag sem valor** (a flag é o último token da linha, ou o p
          +róximo token também começa com `--`) → exit `2`.              
      110 +- **Quantidade ímpar de tokens depois do subcomando** → exit `
          +2` (é a mesma violação que "flag sem valor", vista pelo total:
          + um número ímpar de tokens não fecha em pares completos).     
      111 +- **Ordem:** qualquer, entre as quatro flags. **Repetição:** c
          +ada uma das quatro flags aparece **exatamente uma vez**. **Aus
          +ência:** as quatro são obrigatórias.                          
      112 +- **Parsing:** o parser de argumentos deixa de ser um `switch`
          + com dois casos fixos e passa a acumular os pares reconhecidos
          + num mapa (`Map<String, String>`), contando ocorrências por ch
          +ave. Ao final: se alguma das quatro chaves obrigatórias está a
          +usente do mapa, se alguma chave aparece mais de uma vez, ou se
          + uma flag não reconhecida (`--xyz`) aparece na linha de comand
          +o → exit `2`, mensagem em stderr, nada em stdout, `--output` p
          +reexistente preservado.                                       
      113 +- **O comando anterior à política v4** — só `calcular --input 
          +<e> --output <s>` — retorna exit `2`: faltam `--politica` e `-
          +-cambio`, mesma classe de erro de flag ausente. Isso vale mesm
          +o quando toda despesa da entrada é BRL: `--cambio` é obrigatór
          +io independentemente do conteúdo do arquivo de despesas — não 
          +há isenção por conteúdo.                                      
      114  - **Política e câmbio inválidos:** ausência do arquivo, ilegib
           ilidade, JSON sintaticamente inválido, ou qualquer violação do
            contrato estrutural fechado de `spec.md` §4.1.1 (política ou 
           câmbio) → exit `2` — mesma classe de gravidade que argumento a
           usente/repetido/desconhecido e que `--input` ilegível. Nenhum 
           código novo é criado para esse caso.
      115  - **Envelope de despesas inválido** (RN-001) continua exit `3`
           .
      116  - **Sucesso** continua exit `0`. Stdout permanece vazio em qua
           lquer cenário; mensagens de erro vão para stderr.

● Now updating DT-018 to match:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 4 lines, removed 4 lines
      621  
      622  ### DT-018 — `CLI` valida quatro flags como conjunto, não como
            sequência de casos (estende DT-003)
      623  
      624 -**Contexto:** o contrato de execução (§4.1.1, AMB-034) exige q
          -uatro flags obrigatórias, em qualquer ordem, cada uma exatamen
          -te uma vez, com exit `2` para ausência, repetição ou flag desc
          -onhecida.                                                     
      625 -**Decisão:** o parser acumula argumentos num mapa e valida o c
          -onjunto resultante contra o conjunto fechado `{--input, --outp
          -ut, --politica, --cambio}` **depois** de percorrer todos os ar
          -gumentos — nunca decide por posição.                          
      626 -**Alternativa descartada:** manter o `switch` posicional de do
          -is casos e simplesmente adicionar dois `case` novos — não dete
          -ctaria repetição (`--input a --input b`) nem cobriria a valida
          -ção "exatamente quatro, nem mais nem menos" de forma natural. 
      627 -**Consequência:** `CliContratoTest` ganha casos novos (flag re
          -petida, flag desconhecida, ordem embaralhada) sem exigir um pa
          -rser mais complexo que uma contagem por chave.                
      624 +**Contexto:** o contrato de execução (§4.1.1, AMB-034) exige u
          +m subcomando fixo (`calcular`) seguido de quatro flags obrigat
          +órias em pares `flag valor`, em qualquer ordem, cada uma exata
          +mente uma vez, com exit `2` para subcomando ausente/incorreto,
          + token posicional extra, flag sem valor, ausência, repetição o
          +u flag desconhecida.                                          
      625 +**Decisão:** o parser valida em duas etapas fechadas. (1) o pr
          +imeiro token deve ser exatamente `calcular` — único posicional
          + aceito; qualquer outro valor, ausência dele, ou um token posi
          +cional adicional depois dele, é exit `2` antes mesmo de olhar 
          +para as flags. (2) os tokens restantes são consumidos estritam
          +ente aos pares `flag valor`; uma quantidade ímpar de tokens, o
          +u uma flag como último token (sem valor seguinte), é exit `2`.
          + Os pares reconhecidos são acumulados num mapa (`Map<String, S
          +tring>`), contando ocorrências por chave; o conjunto resultant
          +e é validado contra o conjunto fechado `{--input, --output, --
          +politica, --cambio}` **depois** de percorrer todos os pares — 
          +nunca por posição.                                            
      626 +**Alternativa descartada:** manter o `switch` posicional de do
          +is casos e simplesmente adicionar dois `case` novos — não dete
          +ctaria repetição (`--input a --input b`), token posicional ext
          +ra, flag sem valor, nem cobriria a validação "exatamente quatr
          +o, nem mais nem menos" de forma natural.                      
      627 +**Consequência:** `CliContratoTest` ganha casos novos (subcoma
          +ndo ausente/incorreto, token posicional extra, flag sem valor,
          + flag repetida, flag desconhecida, ordem embaralhada válida, c
          +omando antigo só com `--input`/`--output`) sem exigir um parse
          +r mais complexo que uma verificação do primeiro token seguida 
          +de uma contagem por chave.                                    
      628  
      629  ### DT-019 — Extensão dos três enums fechados de motivo (esten
           de DT-008)
      630  

● Now updating the matrix row for CliContratoTest (explicit CA list + expanded
  scenarios) and the risk/canary items (Tasks 6-8) plus periodicity tests (Task
  7):

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      704  | RN-016 (extensão a moeda) · CA-028 | `RegraViagemEfeitoNuloT
           est` (estendido) — `despesa.moeda` diferente de `BRL` não ampl
           ia teto algum nem afeta outros itens do mesmo dia/período; RN-
           016 continua sem efeito | Unidade |
      705  | RN-017 (atualizada) · CA-034 | `SaidaCambioTest` — os quatro
            formatos de `moeda`/`taxa_cambio_aplicada`/`data_cotacao_util
           izada` (BRL; estrangeira convertida; estruturalmente inválida;
            válida sem cotação) | Unidade |
      706  | RN-019 (periodicidade) · CA-047, CA-049 | `TetoPorPeriodicid
           adeTest` — categoria externa com `"dia"` compartilhando saldo 
           (`representacao`, CA-047), categoria externa com `"diaria"` di
           ferente de hospedagem (`estacionamento`, `TETO_INDIVIDUAL_APLI
           CADO`, CA-049) | Unidade |
      707 -| — (contrato de execução) · CA-041 a CA-044 | `CliContratoTes
          -t` (estendido) — quatro flags em qualquer ordem, flag repetida
          -/desconhecida, política/câmbio inválidos → exit `2`; `--output
          -` preexistente preservado em qualquer falha | Contrato/CLI |  
      707 +| — (contrato de execução) · CA-041 · CA-042 · CA-043 · CA-044
          + | `CliContratoTest` (estendido) — sucesso `0` do comando comp
          +leto com as quatro flags; ordem embaralhada válida; subcomando
          + ausente ou incorreto; token posicional extra; flag ausente, r
          +epetida ou desconhecida; flag sem valor (quantidade ímpar de t
          +okens); comando antigo só `--input`/`--output`; política/câmbi
          +o inválidos; preservação byte a byte de um `--output` preexist
          +ente em qualquer cenário de falha; stdout vazio e mensagem em 
          +stderr em qualquer falha | Contrato/CLI |                     
      708  | — (regressão histórica) · CA-037, CA-038 | `RegressaoHistori
           caTest` (extensão de `ExemploCompletoTest`) — `exemplos/despes
           as-exemplo.json` sob política externa histórica (`585.43`, CA-
           037) e sob `politica-v4.json`/`CC-ENG-PLATAFORMA` (`351.43`, C
           A-038), incluindo as quatro mudanças de item declaradas em `§1
           2.2` | Integração |
      709  | — (integração do envelope) · CA-039, CA-040 | `IntegracaoEnv
           elopeTest` — `despesas-envelope.json`/Rafael/`CC-COMERCIAL` co
           m `politica-v4.json`+`cambio.json` reais (`1143.26`, CA-039) e
            `despesas-envelope-cc-desconhecido.json`/Dani/`CC-SUPORTE-N2`
            (`373.76`, CA-040), ambos comparados contra `§12.3`/`§12.4` |
            Integração |
      710  

● Now the ResolucaoCambioTest row and periodicity row (Tasks 6 & 7):

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      696  | RN-021, RN-022 (política) · CA-035, CA-036, CA-045 | `Leitor
           PoliticaTest` — arquivo ausente/ilegível/JSON inválido; cada u
           ma das quinze validações estruturais de §5 (raiz não objeto, `
           vigencia` ausente/malformada, `moeda_base` diferente de `"BRL"
           `, `limite` zero em `padrao`, `periodicidade` fora de `"dia"`/
           `"diaria"`, etc.); um arquivo que satisfaz integralmente o con
           trato (CA-045) é aceito | Unidade |
      697  | RN-022 (câmbio) · CA-036, CA-046 | `LeitorCambioTest` — arqu
           ivo ausente/ilegível/JSON inválido; `taxas: {}` válido; `moeda
           _base` errada, data/moeda/taxa inválidas → falha; um arquivo q
           ue satisfaz integralmente o contrato (CA-046) é aceito | Unida
           de |
      698  | RN-019 · CA-024, CA-025, CA-026, CA-027 | `ResolutorPolitica
           CentroCustoTest` — centro cadastrado, desconhecido, ausente, n
           ulo, tipo inválido; comparação textual exata (sem trim/caixa/a
           cento); categoria ausente do centro cadastrado não recebe o li
           mite de `padrao` (CA-025); `representacao` reembolsável só ond
           e declarada (CA-026); limite `0,00` em centro cadastrado → `CA
           TEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (CA-027) | Unidade |
      699 -| RN-020 · CA-029, CA-030, CA-031 | `ResolucaoCambioTest` — co
          -tação exata, fallback para a mais recente anterior (CA-029), p
          -roibição de cotação futura, `MOEDA_SEM_COTACAO` para moeda nun
          -ca cotada (CA-030), conversão com arredondamento único (CA-031
          -, exemplo funcional — não é o teste-canário, ver §14) | Unidad
          -e |                                                           
      699 +| RN-020 · CA-029, CA-030, CA-031 | `ResolucaoCambioTest` — co
          +tação exata, fallback para a mais recente anterior (CA-029), p
          +roibição de cotação futura, `MOEDA_SEM_COTACAO` para moeda nun
          +ca cotada (CA-030); mantém **os dois** casos de arredondamento
          +: `40,00 × 5,50 = 220,00` como cenário normativo de CA-031 (ex
          +emplo funcional, não distingue ordem de arredondamento) **e**,
          + adicionalmente, `1.005 × 1.005` como teste técnico de ordem d
          +e arredondamento (§14) — resultado correto `1,01`, incorreto `
          +1,02` se o valor bruto for arredondado antes da multiplicação 
          +| Unidade |                                                   
      700  | RN-020 (motivo, 8.4 item 14) | `MoedaSemCotacaoTest` — o mot
           ivo `MOEDA_SEM_COTACAO` carrega `campo = despesa.moeda` (§10);
            coexistência de `MOEDA_SEM_COTACAO` com `CATEGORIA_FORA_POLIT
           ICA`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMP
           ETENCIA` na mesma posição — nunca com `VALOR_NAO_POSITIVO`/`NO
           TA_FISCAL_AUSENTE`/`DUPLICIDADE`/teto | Unidade |
      701  | RN-002 (moeda) · CA-048 | `CampoMoedaTest` — ausência de cha
           ve vs. `null` explícito vs. tipo inválido vs. formato inválido
            | Unidade |
      702  | RN-009 (atualizada) · CA-032 | `NotaFiscalConvertidaTest` — 
           gatilho aplicado sobre o valor já convertido (USD 40,00 → R$22
           0,00 sem nota → recusado), não o valor original na moeda da de
           spesa | Unidade |

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      703  | RN-010 (atualizada) · CA-033 | `DuplicidadeEntreMoedasTest` 
           — mesma data/categoria/fornecedor/descrição/valor convertido, 
           moedas diferentes → **não** são duplicatas | Unidade |
      704  | RN-016 (extensão a moeda) · CA-028 | `RegraViagemEfeitoNuloT
           est` (estendido) — `despesa.moeda` diferente de `BRL` não ampl
           ia teto algum nem afeta outros itens do mesmo dia/período; RN-
           016 continua sem efeito | Unidade |
      705  | RN-017 (atualizada) · CA-034 | `SaidaCambioTest` — os quatro
            formatos de `moeda`/`taxa_cambio_aplicada`/`data_cotacao_util
           izada` (BRL; estrangeira convertida; estruturalmente inválida;
            válida sem cotação) | Unidade |
      706 -| RN-019 (periodicidade) · CA-047, CA-049 | `TetoPorPeriodicid
          -adeTest` — categoria externa com `"dia"` compartilhando saldo 
          -(`representacao`, CA-047), categoria externa com `"diaria"` di
          -ferente de hospedagem (`estacionamento`, `TETO_INDIVIDUAL_APLI
          -CADO`, CA-049) | Unidade |                                    
      706 +| RN-019 (periodicidade) · CA-047, CA-049 | `TetoPorPeriodicid
          +adeTest` — quatro cenários, provando que o mecanismo depende d
          +e `periodicidade`, não do nome histórico da categoria: (1) cat
          +egoria externa `representacao` com `"dia"`, saldo compartilhad
          +o (CA-047); (2) categoria externa `estacionamento` com `"diari
          +a"`, teto individual, `TETO_INDIVIDUAL_APLICADO` (CA-049); (3)
          + `hospedagem` reconfigurada com `"dia"` — teto **compartilhado
          +** entre lançamentos da mesma data, `TETO_DIARIO_APLICADO`/`TE
          +TO_DIARIO_ESGOTADO`, `regra = RN-019` (não `RN-013`); (4) `ali
          +mentacao` reconfigurada com `"diaria"` — teto **individual** p
          +or lançamento, `TETO_INDIVIDUAL_APLICADO`, `regra = RN-019` (n
          +ão `RN-011`) | Unidade |                                      
      707  | — (contrato de execução) · CA-041 · CA-042 · CA-043 · CA-044
            | `CliContratoTest` (estendido) — sucesso `0` do comando comp
           leto com as quatro flags; ordem embaralhada válida; subcomando
            ausente ou incorreto; token posicional extra; flag ausente, r
           epetida ou desconhecida; flag sem valor (quantidade ímpar de t
           okens); comando antigo só `--input`/`--output`; política/câmbi
           o inválidos; preservação byte a byte de um `--output` preexist
           ente em qualquer cenário de falha; stdout vazio e mensagem em 
           stderr em qualquer falha | Contrato/CLI |
      708  | — (regressão histórica) · CA-037, CA-038 | `RegressaoHistori
           caTest` (extensão de `ExemploCompletoTest`) — `exemplos/despes
           as-exemplo.json` sob política externa histórica (`585.43`, CA-
           037) e sob `politica-v4.json`/`CC-ENG-PLATAFORMA` (`351.43`, C
           A-038), incluindo as quatro mudanças de item declaradas em `§1
           2.2` | Integração |
      709  | — (integração do envelope) · CA-039, CA-040 | `IntegracaoEnv
           elopeTest` — `despesas-envelope.json`/Rafael/`CC-COMERCIAL` co
           m `politica-v4.json`+`cambio.json` reais (`1143.26`, CA-039) e
            `despesas-envelope-cc-desconhecido.json`/Dani/`CC-SUPORTE-N2`
            (`373.76`, CA-040), ambos comparados contra `§12.3`/`§12.4` |
            Integração |

● Now the LeitorPoliticaTest/LeitorCambioTest rows (Task 8):

● Update(specs\001-motor-reembolso\plan.md)  ⎿  Added 2 lines, removed 2 lines
      693  
      694  | RN / CA | Grupo de teste planejado | Nível |
      695  |---|---|---|
      696 -| RN-021, RN-022 (política) · CA-035, CA-036, CA-045 | `Leitor
          -PoliticaTest` — arquivo ausente/ilegível/JSON inválido; cada u
          -ma das quinze validações estruturais de §5 (raiz não objeto, `
          -vigencia` ausente/malformada, `moeda_base` diferente de `"BRL"
          -`, `limite` zero em `padrao`, `periodicidade` fora de `"dia"`/
          -`"diaria"`, etc.); um arquivo que satisfaz integralmente o con
          -trato (CA-045) é aceito | Unidade |                           
      697 -| RN-022 (câmbio) · CA-036, CA-046 | `LeitorCambioTest` — arqu
          -ivo ausente/ilegível/JSON inválido; `taxas: {}` válido; `moeda
          -_base` errada, data/moeda/taxa inválidas → falha; um arquivo q
          -ue satisfaz integralmente o contrato (CA-046) é aceito | Unida
          -de |                                                          
      696 +| RN-021, RN-022 (política) · CA-035, CA-036, CA-045 | `Leitor
          +PoliticaTest` — arquivo ausente/ilegível/JSON inválido; cada u
          +ma das dezesseis validações estruturais de §5 (raiz não objeto
          +, `vigencia` ausente/malformada, `moeda_base` diferente de `"B
          +RL"`, `limite` zero em `padrao`, `periodicidade` fora de `"dia
          +"`/`"diaria"`, etc.); `observacao` de categoria presente como 
          +texto → válido, ignorado; `observacao` presente com tipo não t
          +extual → política inválida; um arquivo que satisfaz integralme
          +nte o contrato (CA-045) é aceito | Unidade |                  
      697 +| RN-022 (câmbio) · CA-036, CA-046 | `LeitorCambioTest` — arqu
          +ivo ausente/ilegível/JSON inválido; `taxas: {}` válido; `moeda
          +_base` errada, data/moeda/taxa inválidas → falha; `fonte`/`obs
          +ervacao` ausentes → válido; presentes como texto → válido, ign
          +orado; presentes com tipo não textual → câmbio inválido; uma c
          +have malformada dentro de `taxas` (data não-ISO ou moeda fora 
          +de `[A-Z]{3}`) não é tratada como campo desconhecido — invalid
          +a o arquivo; um arquivo que satisfaz integralmente o contrato 
          +(CA-046) é aceito | Unidade |                                 
      698  | RN-019 · CA-024, CA-025, CA-026, CA-027 | `ResolutorPolitica
           CentroCustoTest` — centro cadastrado, desconhecido, ausente, n
           ulo, tipo inválido; comparação textual exata (sem trim/caixa/a
           cento); categoria ausente do centro cadastrado não recebe o li
           mite de `padrao` (CA-025); `representacao` reembolsável só ond
           e declarada (CA-026); limite `0,00` em centro cadastrado → `CA
           TEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (CA-027) | Unidade |
      699  | RN-020 · CA-029, CA-030, CA-031 | `ResolucaoCambioTest` — co
           tação exata, fallback para a mais recente anterior (CA-029), p
           roibição de cotação futura, `MOEDA_SEM_COTACAO` para moeda nun
           ca cotada (CA-030); mantém **os dois** casos de arredondamento
           : `40,00 × 5,50 = 220,00` como cenário normativo de CA-031 (ex
           emplo funcional, não distingue ordem de arredondamento) **e**,
            adicionalmente, `1.005 × 1.005` como teste técnico de ordem d
           e arredondamento (§14) — resultado correto `1,01`, incorreto `
           1,02` se o valor bruto for arredondado antes da multiplicação 
           | Unidade |
      700  | RN-020 (motivo, 8.4 item 14) | `MoedaSemCotacaoTest` — o mot
           ivo `MOEDA_SEM_COTACAO` carrega `campo = despesa.moeda` (§10);
            coexistência de `MOEDA_SEM_COTACAO` com `CATEGORIA_FORA_POLIT
           ICA`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMP
           ETENCIA` na mesma posição — nunca com `VALOR_NAO_POSITIVO`/`NO
           TA_FISCAL_AUSENTE`/`DUPLICIDADE`/teto | Unidade |

● Now fixing the risk table's canary reference (Task 6):

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      736  | Risco | Probabilidade | O que faço se acontecer |
      737  |---|---|---|
      738  | Confundir "ausência de chave" com "`null` explícito" no camp
           o `moeda`, tratando os dois igualmente | Média — é o único cam
           po do contrato com essa assimetria, fácil de esquecer sob pres
           são de copiar o padrão dos outros sete campos | `CampoMoedaTes
           t` cobre os quatro casos (§17) separadamente; DT-014 documenta
            a ordem exata dos `if` que preserva a distinção. |
      739 -| Arredondar duas vezes na conversão cambial (uma vez no resol
          -utor de câmbio, outra no normalizador) | Média | Teste-canário
          - `40,00 × 5,50 = 220,00` (§14); DT-015 declara explicitamente 
          -que só um estágio arredonda. |                                
      739 +| Arredondar duas vezes na conversão cambial (uma vez no resol
          +utor de câmbio, outra no normalizador), ou arredondar o valor 
          +bruto antes da multiplicação | Média | O teste `1.005 × 1.005`
          + detecta arredondamento prematuro antes da multiplicação: resu
          +ltado correto `1,01`; resultado incorreto `1,02`. A existência
          + de um único ponto de arredondamento também é garantida por DT
          +-015 e por revisão de código, porque um segundo `setScale` ide
          +mpotente pode não ser observável na saída. |                  
      740  | Reintroduzir um `Set<String>` fixo de categorias nos agregad
           ores de teto, quebrando a generalização por `periodicidade` ex
           igida por RN-019 | Média — os agregadores atuais já têm esse `
           Set` hardcoded, e generalizá-los é a mudança mais invasiva des
           ta revisão | DT-017 documenta a decisão; `TetoPorPeriodicidade
           Test` exercita uma categoria externa (não `alimentacao`/`trans
           porte_urbano`/`hospedagem`) sob cada periodicidade. |
      741  | Misturar `padrao` com a tabela de um centro cadastrado (fall
           back por categoria) | Média — é o erro mais fácil de cometer a
           o "ajudar" um centro cadastrado incompleto | RN-019/DT-011 são
            explícitos: nunca a união das duas tabelas; `ResolutorPolitic
           aCentroCustoTest` cobre categoria ausente do centro cadastrado
            mas presente em `padrao`, esperando `CATEGORIA_NAO_REEMBOLSAV
           EL_CENTRO_CUSTO`, não o limite de `padrao`. |
      742  | `floorEntry` usado incorretamente (ex.: `ceilingEntry`, que 
           permitiria cotação futura) | Baixa, mas gravidade alta se ocor
           rer | DT-013 documenta o método exato exigido; `ResolucaoCambi
           oTest` inclui um caso de data sem cotação exata cuja única cot
           ação disponível é posterior — deve resultar em `MOEDA_SEM_COTA
           CAO`, nunca numa cotação futura usada por engano. |

● Now let's verify all banned phrases are gone:

  Searched for 1 pattern (ctrl+o to expand)

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      224  ): TabelaPoliticaResolvida
      225  ```
      226  
      227 -Não existe um "método do próprio resolutor ou de um objeto de 
          -resultado que ele devolve" como alternativa em aberto — a deci
          -são é fechada: `resolver(...)` é um método estático (ou de ins
          -tância de um resolutor sem estado) que devolve diretamente o m
          -odelo imutável `modelo/TabelaPoliticaResolvida.java`:         
      227 +Decisão fechada, sem alternativa de projeto em aberto: `resolv
          +er(...)` é um método estático (ou de instância de um resolutor
          + sem estado) que devolve diretamente o modelo imutável `modelo
          +/TabelaPoliticaResolvida.java`:                               
      228  
      229  ```java
      230  TabelaPoliticaResolvida {

● Now re-checking all banned phrases once more, plus a broader check for
  remnants like "para cada item com campos estruturalmente válidos" and
  "ResolutorCambio... quatro campos":

  Searched for 1 pattern (ctrl+o to expand)

● All banned phrases are removed. Now let's do the final verification:

  Read 1 file, ran 1 shell command (ctrl+o to expand)

● Header intact. Let me confirm no new DT/RN/CA identifiers were introduced and
  that the matrix coverage line is still present and accurate:

  Searched for 1 pattern (ctrl+o to expand)

● Exactly DT-001 through DT-019, no new identifiers created. Coverage
  declaration intact.

  Resumo final

  1. Contrato final dos dois leitores: LeitorPolitica.ler(Path): PoliticaExterna
  e LeitorCambio.ler(Path): TabelaCambio — API pública única. Política agora
  com 16 validações explícitas (observacao de categoria é campo conhecido,
  opcional, texto-ou-inválido-recusa-arquivo). Câmbio com fonte/observacao
  seguindo a mesma régua, e a tolerância a "campo desconhecido" restrita à raiz
  — chaves malformadas dentro de taxas (data ou moeda) são violação estrutural,
  não campo desconhecido.

  2. API final do resolutor: ResolutorPoliticaCentroCusto.resolver(String 
  centroCusto, PoliticaExterna): TabelaPoliticaResolvida — modelo novo
  (categorias, origem PADRAO/CENTRO_CUSTO, nomeCentroCusto), sem método de
  consulta alternativo em aberto.

  3. Divisão de responsabilidade: ValidadorItem popula só ItemValidado.moeda;
  ResolutorCambio não a repopula — consome-a e acrescenta só
  taxaCambioAplicada/dataCotacaoUtilizada/valorConvertidoBruto (avaliado quando
  valor/moeda/data estão utilizáveis, independente de
  categoria/descricao/fornecedor/tem_nota_fiscal); Normalizador usa
  exclusivamente valorConvertidoBruto, mesmo caminho para BRL e moeda
  estrangeira.

  4. Contrato final da CLI: primeiro token exatamente calcular (único
  posicional); depois, pares flag valor estritos; quantidade ímpar ou flag sem
  valor → exit 2; subcomando ausente/incorreto ou posicional extra → exit 2;
  comando antigo (só --input/--output) → exit 2; --cambio obrigatório mesmo com
  despesas só em BRL.

  5. Teste-canário e riscos: 1.005 × 1.005 (correto 1,01, incorreto 1,02)
  substitui 40,00 × 5,50 como mitigação do risco de arredondamento
  duplo/prematuro; 40,00 × 5,50 preservado só como cenário funcional de CA-031
  dentro de ResolucaoCambioTest, ao lado do canário técnico.

  6. Matriz: CA-041 · CA-042 · CA-043 · CA-044 explícitos (não mais intervalo);
  TetoPorPeriodicidadeTest ganhou os casos hospedagem/"dia" e
  alimentacao/"diaria"; LeitorPoliticaTest/LeitorCambioTest ganharam os casos de
  observacao/fonte e chave malformada em taxas. Cobertura RN-001–022/CA-001–049
  confirmada intacta.

  7. git status --short:
   M specs/001-motor-reembolso/plan.md

  git diff --check sem erros (só aviso de CRLF); spec, DECISIONS, tasks, README,
  src/ e exemplos/envelope/ com diff vazio. Nenhum /export, git add, commit ou
  push executado.

  Parando para revisão.

✻ Cooked for 6m 0s

※ recap: We're updating plan.md's technical design for the Day 2 currency/policy features. Last step closed remaining open decisions (reader APIs, resolver split, CLI contract) and fixed the rounding test; verification passed with only plan.md changed. Next: awaiting your review before any further edits. (disable recaps in /config)

> A revisão independente do plan 1.1 foi concluída e aprovada.                  
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  specs/001-motor-reembolso/plan.md                                             
                                                                                
  Faça somente esta mudança no cabeçalho:                                       
                                                                                
  **Status:** em revisão                                                        
                                                                                
  para:                                                                         
                                                                                
  **Status:** aprovado                                                          
                                                                                
  Mantenha inalterados:                                                         
                                                                                
  - versão 1.1;                                                                 
  - referência à spec 1.2 aprovada;                                             
  - data 2026-08-05;                                                            
  - todo o restante de plan.md;                                                 
  - spec.md;                                                                    
  - DECISIONS.md;                                                               
  - tasks.md;                                                                   
  - README;                                                                     
  - código;                                                                     
  - testes;                                                                     
  - fixtures;                                                                   
  - exemplos/envelope/.                                                         
                                                                                
  Não crie, remova ou renumere DTs.                                             
                                                                                
  Permanecem DT-001 a DT-019.                                                   
                                                                                
  Depois execute:                                                               
                                                                                
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  Select-String -Path specs/001-motor-reembolso/plan.md -Pattern                
  "Versão:|Status:|Baseado na spec:|Última alteração:"                          
                                                                                
  Confirme:                                                                     
                                                                                
  - status final aprovado;                                                      
  - versão ainda 1.1;                                                           
  - base normativa ainda spec 1.2 aprovada;                                     
  - somente plan.md foi alterado nesta tarefa;                                  
  - nenhum git add, commit ou push foi executado.                               
                                                                                
  Pare.                                                                         

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      1  # Plano Técnico — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.1 · **Status:** em revisão · **Baseado na spec:** 
        -1.2 (aprovado) · **Última alteração:** 2026-08-05               
      3 +**Versão:** 1.1 · **Status:** aprovado · **Baseado na spec:** 1.
        +2 (aprovado) · **Última alteração:** 2026-08-05                 
      4  
      5  > Aqui mora o COMO. Este arquivo pode e deve falar de linguagem,
          biblioteca e
      6  > arquitetura. O que ele **não** pode é introduzir regra de negó
         cio nova — se

  Ran 2 shell commands (ctrl+o to expand)

● Confirmado.

  - Status final: aprovado
  - Versão: ainda 1.1
  - Base normativa: ainda spec 1.2 (aprovado)
  - Última alteração: 2026-08-05 (inalterada)
  - Somente specs/001-motor-reembolso/plan.md foi alterado nesta tarefa (git 
  status --short mostra uma única linha); nenhum git add, commit ou push foi
  executado.

  Parando.

✻ Brewed for 18s