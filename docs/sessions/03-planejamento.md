 ▐▛███▜▌   Claude Code v2.1.220
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


❯ Estamos iniciando uma NOVA sessão do desafio, dedicada exclusivamente ao      
  planejamento técnico.                                                         
                                                                                
  A spec 1.1 já foi aprovada após auditoria independente. Nesta primeira etapa, 
  não modifique nenhum arquivo.                                                 
                                                                                
  Leia integralmente, nesta ordem:                                              
                                                                                
  1. CLAUDE.md                                                                  
  2. DESAFIO.md                                                                 
  3. RUBRICA.md                                                                 
  4. FAQ.md                                                                     
  5. exemplos/despesas-exemplo.json                                             
  6. specs/001-motor-reembolso/spec.md                                          
  7. specs/001-motor-reembolso/DECISIONS.md                                     
  8. template/specs/001-motor-reembolso/plan.md                                 
  9. specs/001-motor-reembolso/plan.md                                          
                                                                                
  Depois:                                                                       
                                                                                
  1. Confirme que a spec está na versão 1.1 e aprovada.                         
  2. Identifique todas as perguntas que o template de plan.md exige responder.  
  3. Execute somente verificações de leitura para conhecer o ambiente           
  disponível, incluindo:                                                        
     - git status --short                                                       
     - java --version                                                           
     - mvn --version                                                            
     - python --version                                                         
     - node --version                                                           
     Falhas de ferramentas devem apenas ser registradas, sem instalar nada.     
  4. Proponha uma única stack principal para o projeto, justificando a escolha  
  considerando:                                                                 
     - prazo curto do desafio;                                                  
     - facilidade para rodar e testar em outra máquina;                         
     - familiaridade de um desenvolvedor back-end Java;                         
     - necessidade de processar JSON;                                           
     - aritmética monetária decimal exata;                                      
     - simplicidade, sem servidor HTTP ou framework pesado se não forem         
  necessários.                                                                  
  5. Se houver alternativa técnica realmente competitiva, apresente-a           
  brevemente, mas termine com uma recomendação objetiva.                        
  6. Proponha o contrato de execução do produto:                                
     - como o arquivo JSON será informado;                                      
     - como o resultado será entregue;                                          
     - comportamento para sucesso;                                              
     - comportamento para erro fatal no envelope;                               
     - códigos de saída do processo, se aplicáveis.                             
  7. Proponha a decomposição interna do sistema e a responsabilidade de cada    
  componente, sem implementar.                                                  
  8. Descreva como a ordem de processamento definida na seção 8 da spec será    
  preservada.                                                                   
  9. Defina a estratégia de aritmética monetária sem ponto flutuante binário.   
  Considere obrigatoriamente:                                                   
     - 33.333 → 33,33;                                                          
     - 33.345 → 33,35;                                                          
     - 100.005 → 100,01;                                                        
     - arredondamento HALF_UP;                                                  
     - comparação da nota fiscal usando o valor normalizado.                    
  10. Proponha a estratégia de testes com rastreabilidade para:                 
      - RN-001 a RN-018;                                                        
      - CA-001 a CA-023;                                                        
      - os 14 itens do arquivo de exemplo;                                      
      - total final de R$ 585,43;                                               
      - erros estruturais;                                                      
      - duplicidades;                                                           
      - precedência e ordem dos motivos;                                        
      - agregação e consumo dos tetos;                                          
      - casos monetários de fronteira.                                          
  11. Identifique riscos técnicos, decisões irreversíveis e possíveis excessos  
  de arquitetura.                                                               
  12. Verifique se existe alguma contradição entre spec.md, DECISIONS.md e o    
  template de plan.md.                                                          
                                                                                
  Ao final, apresente:                                                          
                                                                                
  - stack recomendada;                                                          
  - proposta de arquitetura;                                                    
  - contrato de execução;                                                       
  - estratégia monetária;                                                       
  - estratégia de testes;                                                       
  - dúvidas que realmente exigem decisão humana;                                
  - conteúdo que pretende colocar em cada seção de plan.md;                     
  - recomendação objetiva: podemos escrever o plan.md ou precisamos decidir     
  algo antes?                                                                   
                                                                                
  Não modifique plan.md nem qualquer outro arquivo.                             
  Não execute git add.                                                          
  Não faça commit.                                                              
  Não implemente código.                                                        
  Não crie tasks ainda.                                                         

● Vou ler os documentos na ordem solicitada, começando pelos que já não tenho em
  contexto.

  Read 5 files (ctrl+o to expand)

● Agora vou ler a spec, DECISIONS.md e os dois templates/plan.md.

  Read 4 files (ctrl+o to expand)
  ⎿  Loaded template\CLAUDE.md

● Agora vou verificar o ambiente disponível (somente leitura, sem instalar
  nada).

  Ran 5 shell commands (ctrl+o to expand)

● Li os nove documentos na ordem pedida. Aqui está a análise completa — nenhum
  arquivo foi tocado.

  1. Versão da spec

  Confirmado: spec.md está na versão 1.1, status aprovada, última alteração
  2026-07-30. DECISIONS.md documenta a trilha completa: D-001 é a linha de base
  1.0 (18 AMB, 18 RN, 22 CA), D-002 é a auditoria independente que gerou a 1.1
  (fechou a classificação de erro estrutural, precisou valor_informado,
  completou a matriz de dependências, e subiu para 23 CA). Os números batem
  entre spec.md e DECISIONS.md — sem incoerência de contagem.

  2. O que o template de plan.md exige responder

  ┌────────────────────┬────────────────────────────────────────────────────┐
  │ Seção do template  │          Perguntas que preciso responder           │
  ├────────────────────┼────────────────────────────────────────────────────┤
  │                    │ Linguagem, testes, parsing/validação, aritmética   │
  │ 1. Stack           │ monetária — cada um com "o quê", "por quê" e "o    │
  │                    │ que descartei"                                     │
  ├────────────────────┼────────────────────────────────────────────────────┤
  │ 2. Arquitetura     │ Diagrama em blocos; fronteira entre núcleo de      │
  │                    │ regra pura e I/O                                   │
  ├────────────────────┼────────────────────────────────────────────────────┤
  │ 3. Modelo de dados │ Como uma despesa é representada internamente; como │
  │                    │  um resultado carrega a justificativa              │
  ├────────────────────┼────────────────────────────────────────────────────┤
  │ 4. Representação   │ Constantes no código? Arquivo de config? Tabela?   │
  │ da política        │                                                    │
  ├────────────────────┼────────────────────────────────────────────────────┤
  │ 5. Decisões        │ Contexto, decisão, alternativa descartada,         │
  │ técnicas (DT-NNN)  │ consequência                                       │
  ├────────────────────┼────────────────────────────────────────────────────┤
  │ 6. Estratégia de   │ Nível e proporção; garantia de cobertura por RN;   │
  │ testes             │ nomenclatura rastreável                            │
  ├────────────────────┼────────────────────────────────────────────────────┤
  │ 7. Riscos          │ Risco, probabilidade, mitigação                    │
  └────────────────────┴────────────────────────────────────────────────────┘

  3. Ambiente disponível (somente leitura)

  Ferramenta: git status --short
  Resultado: Working tree com mudanças não commitadas:
  docs/sessions/01-validacao-export.md, DECISIONS.md, spec.md modificados, e
  docs/sessions/02-especificacao-inicial.md novo (não rastreado). Não toquei em
   nada — só registro.
  ────────────────────────────────────────
  Ferramenta: java --version
  Resultado: Java 21.0.2 LTS, funcionando
  ────────────────────────────────────────
  Ferramenta: mvn --version
  Resultado: Maven 3.9.6, casado com o JDK 21 instalado, funcionando
  ────────────────────────────────────────
  Ferramenta: python --version
  Resultado: Não encontrado — é o stub da Microsoft Store, não há Python real
  instalado
  ────────────────────────────────────────
  Ferramenta: node --version
  Resultado: Não encontrado — não está no PATH

  Isso é um dado concreto, não só preferência: nesta máquina, Java+Maven já
  funcionam sem instalar nada; Python e Node exigiriam instalação agora, sob
  prazo curto.

  4. Stack recomendada

  Java 21 + Maven + Jackson (databind) + java.math.BigDecimal + JUnit 5. Sem 
  framework, sem servidor HTTP.

  ┌──────────────────┬──────────────────────────────────────────────────────┐
  │     Critério     │                 Como a stack atende                  │
  ├──────────────────┼──────────────────────────────────────────────────────┤
  │ Prazo curto      │ JDK e Maven já verificados funcionando nesta máquina │
  │                  │  — zero setup                                        │
  ├──────────────────┼──────────────────────────────────────────────────────┤
  │ Rodar em outra   │ mvn package gera um jar único (via                   │
  │ máquina          │ maven-shade-plugin); a outra máquina só precisa de   │
  │                  │ um JDK, nada de gerenciador de versão de linguagem   │
  ├──────────────────┼──────────────────────────────────────────────────────┤
  │ Familiaridade    │ Direto — é a stack nativa do perfil declarado        │
  │ back-end Java    │                                                      │
  ├──────────────────┼──────────────────────────────────────────────────────┤
  │                  │ Jackson é maduro, e a árvore genérica (JsonNode)     │
  │ Processar JSON   │ resolve exatamente o problema de preservar           │
  │                  │ valor_informado cru mesmo quando o tipo é inválido   │
  ├──────────────────┼──────────────────────────────────────────────────────┤
  │ Aritmética       │ BigDecimal é nativo do JDK, sem biblioteca extra, e  │
  │ decimal exata    │ resolve HALF_UP diretamente                          │
  ├──────────────────┼──────────────────────────────────────────────────────┤
  │ Simplicidade     │ CLI = uma classe Main, sem Spring, sem servidor —    │
  │                  │ argumento de linha de comando lido à mão             │
  └──────────────────┴──────────────────────────────────────────────────────┘

  Alternativa competitiva considerada: Python. Tem decimal.Decimal e json na
  stdlib, seria igualmente capaz tecnicamente. Descartada por dois motivos
  concretos: (1) não está instalado nesta máquina — instalar agora consome tempo
  do prazo curto e é um passo a mais de risco de "funciona aqui, não funciona
  na correção"; (2) o desenvolvedor é back-end Java, e a política do desafio é
  explícita — "a stack livre é a que você domina", não é hora de aprender
  ferramenta nova.

  Kotlin (JVM, mais conciso) também foi descartado pelo mesmo motivo: não há
  necessidade de introduzir sintaxe nova sob pressão de tempo quando Java puro
  resolve tudo.

  Recomendação objetiva: Java 21 + Maven.

  5. Contrato de execução (proposta)

  - Entrada: flag obrigatória --input <caminho>.
  - Saída: flag obrigatória --output <caminho>; JSON indentado (facilita
  conferência manual pelo avaliador), escrito/sobrescrito no caminho indicado.
  - Sucesso: exit code 0; arquivo de saída escrito por completo, mesmo que
  contenha itens RECUSADO — recusa de item não é erro de processo.
  - Erro fatal de envelope (RN-001): nenhum arquivo é escrito em --output (não
  sobrescreve um resultado anterior válido com um estado de erro); mensagem
  curta em stderr; exit code distinto de sucesso.
  - Erro de uso da CLI (arquivo de entrada inexistente, flag faltando): também
  não escreve saída; mensagem em stderr; exit code próprio, distinto do erro de
  envelope.

  Isso não está coberto pela spec.md — a spec define o schema de saída de
  sucesso e a regra de negócio "envelope inválido não produz apuração parcial",
  mas não o formato do sinal de erro nem os exit codes. É contrato de execução,
  não regra de negócio, então pertence ao plan.md — mas como envolve duas
  decisões genuinamente abertas, vou perguntar abaixo antes de fixar.

  6. Decomposição interna (proposta, sem implementar)

  CLI/Main → Leitor JSON (JsonNode) → Validador de envelope (RN-001)
     → Validador de item / classificador estrutural (RN-002)
     → Detector de ID duplicado (RN-003)
     → Normalizador (RN-004 valor, RN-005 categoria)
     → Avaliador de regras individuais (RN-006/007/008/009)
     → Detector de duplicidade econômica (RN-010)
     → Agregador de tetos (RN-011..015)
     → Compositor de motivos (ordem 8.3)
     → Escritor de saída JSON

  Fronteira: tudo entre o validador de envelope e o compositor de motivos é
  núcleo puro — funções sobre estruturas em memória, sem I/O, sem dependência de
  Jackson além da leitura. CLI, leitor e escritor ficam isolados nas pontas.
  Essa fronteira é o que decide o quanto o "envelope lacrado" do Dia 2 vai
  custar: mudança de regra de negócio deveria tocar só o núcleo, nunca o parsing
  ou a serialização.

  Ponto de design importante: cada regra individual (RN-006 a RN-009) precisa
  ser avaliada independentemente das demais, mesmo quando o item já está
  condenado por outro campo — a seção 8.4 exige isso explicitamente (o exemplo
  de "três motivos simultâneos" e o CA-023 com cinco motivos). Isso significa
  que o modelo interno não pode fazer short-circuit no primeiro motivo
  encontrado: cada item carrega um acumulador de motivos ao longo do pipeline, e
  só decide "recusado vs. não" no fim.

  7. Preservação da ordem (seção 8)

  Um único campo indice_entrada, atribuído uma vez, antes de qualquer validação,
  imutável, atravessa todas as etapas do pipeline. A lista mestra nunca é
  reordenada — agrupamentos por (data, categoria) para os tetos são feitos em
  estruturas auxiliares (ex.: mapa de grupo → lista ordenada por
  indice_entrada), nunca a lista principal. A ordem de apresentação dos motivos
  (8.3) é uma etapa de composição separada da ordem de processamento (8.1):
  motivos são coletados na ordem em que são detectados, mas reordenados por um
  critério fixo só no compositor final, antes de serializar.

  8. Estratégia monetária

  - Todo valor é BigDecimal de ponta a ponta — nunca double.
  - Crítico: o ObjectMapper do Jackson precisa ser configurado para ler números
  JSON como BigDecimal exato (via
  DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS), não como double. Sem isso,
  100.005 chegaria como 100.00499999999999... em IEEE-754 e arredondaria para
  100,00 sob HALF_UP — o oposto do que a spec exige (100,01). Esse é o bug mais
  previsível deste projeto e o motivo de o CLAUDE.md já avisar sobre isso.
  - Normalização: valor.setScale(2, RoundingMode.HALF_UP) — verificado contra os
  quatro vetores exigidos: 33.333→33,33, 33.345→33,35, 100.005→100,01, e o
  adicional 100.004→100,00.
  - Comparações usam compareTo, nunca equals (que também compara escala) nem ==.
  - valor_informado não passa pelo BigDecimal quando o tipo é inválido — é o
  JsonNode cru reserializado como veio (string, booleano, etc.), preservando
  exatamente o que a spec exige em 4.3.

  9. Estratégia de testes com rastreabilidade

  - JUnit 5, nomes de método/classe carregando o código da regra
  (RN004NormalizacaoMonetariaTest, rn009_100_00_semNota_elegivel()) — rastreável
  por grep direto entre spec e teste.
  - Um teste de integração único rodando o pipeline completo contra
  exemplos/despesas-exemplo.json, comparado a um fixture de saída esperada
  (derivado literalmente da tabela 4.7) — cobre CA-001/002/003 e o total de R$
  585,43 de uma vez.
  - Testes parametrizados (@ParameterizedTest) para as matrizes de fronteira:
  RN-004 (arredondamento), RN-009 (gatilho de R$ 100), RN-008 (bordas do
  período).
  - Casos de RN-002 (estruturais) cobertos item a campo, incluindo CA-021/CA-023
  (múltiplos motivos, ordem canônica).
  - Casos de duplicidade: RN-003 (todas as ocorrências recusadas) e RN-010
  (chave exata, 100.00/100.01 não são duplicata).
  - Precedência (8.3/8.4): os dois exemplos já elaborados na própria spec ("três
  motivos simultâneos", "exclusão de nota por valor negativo") viram testes
  literais.
  - Agregação/tetos: consumo em ordem de indice_entrada (CA-006), hospedagem
  isolada por lançamento (CA-007), duas hospedagens no mesmo dia somando até
  R$ 500.
  - Cada RN-NNN ganha pelo menos um teste; confirmação cruzada feita manualmente
  ao fechar o Dia 1 antes do envelope do Dia 2.

  10. Riscos, decisões irreversíveis e excesso de arquitetura

  - Risco real: parsing de número via double em vez de BigDecimal exato —
  mitigado como acima, com teste-canário no valor 100.005.
  - Risco real: Jackson coage booleano↔número por padrão em métodos convenientes
  (asBoolean()/asInt()); a spec proíbe essa coerção (RN-002) — o validador
  precisa inspecionar JsonNodeType explicitamente, não usar acessores
  permissivos.
  - Excesso de arquitetura a evitar: motor de regras genérico, configurável, com
  prioridades e DSL — a política tem três categorias fixas e poucas constantes;
  um PoliticaConstantes simples resolve. Construir generalidade que a spec não
  pede é o tipo de risco que o FAQ.md avisa explicitamente ("arquitetura
  genérica no lugar errado é pior que uma simples").
  - Decisão de fato irreversível a fixar agora: o formato do modelo interno de
  item (acumulador de motivos, sem short-circuit) — é a espinha dorsal do
  pipeline; mudar isso no meio da implementação é caro. Vale travar em plan.md
  com cuidado antes de começar a codar.
  - Observação, não bloqueio: há mudanças não commitadas em
  spec.md/DECISIONS.md/sessions — presumo que sejam a auditoria que gerou a 1.1,
  ainda não commitada. Não mexi nisso.

  11. Contradições entre spec.md, DECISIONS.md e template de plan.md

  Nenhuma encontrada. Cruzei as três mudanças que D-002 alega ter feito
  (classificação estrutural, valor_informado, linha de RN-003 na matriz 8.2)
  contra o texto atual de spec.md — todas presentes e coerentes. O plan.md atual
  do projeto é idêntico ao template — ainda não foi iniciado, nenhuma solução
  vazou para lá ainda. Boa base para começar.

  ---
  Antes de escrever plan.md, duas decisões do contrato de execução são
  genuinamente abertas — a spec não resolve, e não há uma resposta "certa":

● User answered Claude's questions:
  ⎿  · Como sinalizar erro fatal de envelope (RN-001) na CLI? → Texto simples em
     stderr (Recomendado)
     · Como materializar o '<seu-comando>' da interface fixa do desafio? → java 
     -jar direto (Recomendado)

● Ambas as decisões seguem o caminho recomendado. Contrato de execução fechado:

  - Erro fatal de envelope: mensagem em texto simples em stderr, nenhum arquivo
  escrito em --output, exit code próprio (distinto de sucesso e de erro de uso
  da CLI).
  - Comando: java -jar target/motor-reembolso.jar calcular --input <arquivo> 
  --output <arquivo>, documentado no README, sem wrapper script.

  Recomendação objetiva

  Podemos escrever o plan.md. Não há decisão pendente que bloqueie: stack,
  arquitetura, modelo de dados, representação da política, estratégia monetária,
  estratégia de testes e contrato de execução estão todos definidos acima com
  justificativa. O conteúdo de cada seção do template já está esboçado neste
  turno — a próxima etapa é transcrever isso em plan.md seguindo a estrutura do
  template (Stack → Arquitetura → Modelo de dados → Representação da política →
  Decisões técnicas DT-001 em diante → Estratégia de testes → Riscos), sem
  introduzir nenhuma regra de negócio nova que não esteja já em spec.md.

  Nenhum arquivo foi modificado nesta sessão. Aguardo confirmação para
  prosseguir e escrever plan.md.

✻ Cogitated for 4m 45s

※ recap: We finished the technical plan for the reembolso engine—Java 21+Maven, architecture, and execution contract are decided. Next: write plan.md with that content, pending your go-ahead. (disable recaps in /config)

❯ A proposta técnica está aprovada. Você pode escrever o plan.md, incorporando  
  obrigatoriamente os refinamentos abaixo.                                      
                                                                                
  Edite exclusivamente:                                                         
                                                                                
  specs/001-motor-reembolso/plan.md                                             
                                                                                
  Não modifique spec.md, DECISIONS.md, tasks.md, CLAUDE.md, README.md, pom.xml, 
  código, testes, templates ou sessões.                                         
                                                                                
  1. Preserve todas as seções e perguntas do template de plan.md, substituindo  
  os placeholders com conteúdo concreto.                                        
                                                                                
  2. Registre a stack aprovada:                                                 
                                                                                
  - Java 21;                                                                    
  - Maven 3.9 ou superior;                                                      
  - Jackson Databind;                                                           
  - BigDecimal;                                                                 
  - JUnit 5;                                                                    
  - Maven Shade Plugin para produzir JAR executável;                            
  - sem Spring Boot;                                                            
  - sem servidor HTTP;                                                          
  - sem wrapper .bat ou .sh.                                                    
                                                                                
  3. Fixe o contrato da CLI:                                                    
                                                                                
  java -jar target/motor-reembolso.jar calcular --input <arquivo> --output      
  <arquivo>                                                                     
                                                                                
  Use exatamente estes códigos de saída:                                        
                                                                                
  - 0: processamento concluído e arquivo de resultado escrito;                  
  - 2: erro de uso ou infraestrutura, como argumento ausente, arquivo           
  inexistente, arquivo ilegível, JSON sintaticamente inválido ou falha de       
  escrita;                                                                      
  - 3: JSON legível, mas envelope inválido conforme RN-001.                     
                                                                                
  Para códigos 2 e 3:                                                           
                                                                                
  - escrever mensagem curta em texto simples no stderr;                         
  - não produzir resultado no stdout;                                           
  - não criar nem sobrescrever o arquivo indicado em --output.                  
                                                                                
  Explique que a validação do envelope acontece antes de abrir ou sobrescrever  
  o arquivo de saída. Não crie um vocabulário JSON de erros que não esteja na   
  spec.                                                                         
                                                                                
  4. Para o empacotamento, planeje o Maven Shade Plugin de modo que:            
                                                                                
  mvn package                                                                   
                                                                                
  produza exatamente:                                                           
                                                                                
  target/motor-reembolso.jar                                                    
                                                                                
  O JAR deve conter dependências e Main-Class, permitindo execução somente com  
  java -jar.                                                                    
                                                                                
  5. Faça a arquitetura acompanhar literalmente os onze passos da seção 8.1 da  
  spec.                                                                         
                                                                                
  A aplicação das regras não pode parar no primeiro motivo encontrado.          
  Entretanto, não escreva que todas as regras sempre executam                   
  indiscriminadamente.                                                          
                                                                                
  A formulação correta é:                                                       
                                                                                
  - cada regra é avaliada quando todos os campos de que depende estão           
  estruturalmente válidos, conforme a matriz 8.2;                               
  - erros em campos não utilizados por uma regra não impedem essa regra;        
  - motivos aplicáveis são acumulados;                                          
  - somente as exclusões expressamente listadas em 8.4 interrompem etapas       
  posteriores;                                                                  
  - a ordem de processamento 8.1 é diferente da ordem de apresentação dos       
  motivos 8.3.                                                                  
                                                                                
  6. Esclareça a fronteira com Jackson:                                         
                                                                                
  - a camada de entrada usa JsonNode para reconhecer tipos JSON e preservar     
  valor_informado;                                                              
  - o valor monetário usado pelas regras é sempre BigDecimal;                   
  - JsonNode pode acompanhar o contexto do item somente como fotografia de      
  auditoria de valor_informado;                                                 
  - nenhuma regra financeira deve consultar valor_informado;                    
  - as regras consultam apenas os campos validados e normalizados;              
  - “núcleo puro” significa sem leitura/escrita de arquivos, sem CLI, sem       
  estado global e sem efeitos colaterais, não necessariamente ausência física   
  da dependência Jackson no modelo de auditoria.                                
                                                                                
  Prefira essa solução simples a criar uma abstração genérica de valores JSON   
  apenas para eliminar o tipo JsonNode.                                         
                                                                                
  7. Na estratégia monetária, registre:                                         
                                                                                
  - habilitar USE_BIG_DECIMAL_FOR_FLOATS na leitura;                            
  - obter números por decimalValue, nunca doubleValue;                          
  - nunca construir BigDecimal a partir de double;                              
  - normalizar com setScale(2, RoundingMode.HALF_UP);                           
  - comparar com compareTo;                                                     
  - escrever valores monetários como números JSON decimais em notação simples,  
  com duas casas, sem notação científica;                                       
  - testar obrigatoriamente 33.333, 33.335, 33.345, 100.004 e 100.005.          
                                                                                
  8. Centralize os valores da política em uma estrutura simples e imutável, por 
  exemplo PoliticaReembolso, contendo os três tetos e o gatilho da nota         
  fiscal.                                                                       
                                                                                
  Não crie mecanismo genérico de regras, DSL, banco de dados, injeção de        
  dependência, arquivo externo de configuração ou arquitetura para requisitos   
  futuros desconhecidos.                                                        
                                                                                
  9. No modelo de dados, descreva concretamente:                                
                                                                                
  - item recebido e seu indice_entrada;                                         
  - campos estruturalmente validados;                                           
  - valor_informado;                                                            
  - valor_normalizado;                                                          
  - categoria normalizada;                                                      
  - acumulador de motivos;                                                      
  - decisão final;                                                              
  - valor_reembolsavel;                                                         
  - resultado geral e total_reembolsavel.                                       
                                                                                
  Defina como codigo, regra e campo de cada motivo serão representados sem      
  duplicar textos livres espalhados pelo código.                                
                                                                                
  10. Na estratégia de testes, inclua uma matriz de rastreabilidade contendo:   
                                                                                
  - cada RN-001 a RN-018;                                                       
  - cada CA-001 a CA-023;                                                       
  - classe ou grupo de teste planejado;                                         
  - nível do teste: unidade, integração ou contrato/CLI.                        
                                                                                
  Uma linha pode cobrir mais de uma RN ou CA, mas nenhum identificador pode     
  ficar sem teste planejado.                                                    
                                                                                
  Inclua obrigatoriamente:                                                      
                                                                                
  - teste integral dos 14 itens de exemplos/despesas-exemplo.json;              
  - total R$ 585,43;                                                            
  - comparação do resultado com fixture esperado;                               
  - códigos de saída 0, 2 e 3;                                                  
  - garantia de que envelope inválido não cria nem sobrescreve --output;        
  - ordem dos itens;                                                            
  - ordem dos motivos;                                                          
  - todos os estados de decisão;                                                
  - arredondamento monetário;                                                   
  - normalização de categoria;                                                  
  - IDs repetidos;                                                              
  - duplicidade econômica;                                                      
  - competência;                                                                
  - nota fiscal;                                                                
  - tetos diários;                                                              
  - hospedagem;                                                                 
  - regra de viagem com efeito nulo;                                            
  - campos desconhecidos;                                                       
  - erros estruturais e valor_informado.                                        
                                                                                
  11. Registre as decisões técnicas como DT-NNN, contendo:                      
                                                                                
  - contexto;                                                                   
  - decisão;                                                                    
  - alternativa descartada;                                                     
  - consequência.                                                               
                                                                                
  Inclua pelo menos decisões sobre stack, CLI, códigos de saída, BigDecimal,    
  parsing por árvore, arquitetura do pipeline, representação da política,       
  empacotamento e estratégia de testes.                                         
                                                                                
  12. Não invente o requisito do Dia 2 e não tente antecipar seu conteúdo.      
  Apenas mantenha as regras de negócio separadas de CLI e I/O, porque isso já é 
  uma boa decisão para a versão atual.                                          
                                                                                
  Depois de escrever, execute somente verificações de leitura:                  
                                                                                
  - git diff --check -- specs/001-motor-reembolso/plan.md                       
  - git diff -- specs/001-motor-reembolso/plan.md                               
  - git status --short                                                          
  - busca por placeholders remanescentes no plan.md;                            
  - busca por RN-001 a RN-018 e CA-001 a CA-023 para confirmar cobertura da     
  matriz.                                                                       
                                                                                
  Ao final, informe:                                                            
                                                                                
  - seções preenchidas;                                                         
  - decisões DT criadas;                                                        
  - arquitetura planejada;                                                      
  - quantidade de linhas da matriz de testes;                                   
  - cobertura encontrada de RN e CA;                                            
  - placeholders restantes;                                                     
  - arquivos modificados;                                                       
  - problemas ou pendências encontrados.                                        
                                                                                
  Não execute git add.                                                          
  Não faça commit.                                                              
  Não crie código.                                                              
  Não crie testes.                                                              
  Não escreva tasks.md.                                                         
  Não faça /export ainda.                                                       

● Write(specs\001-motor-reembolso\plan.md)
  ⎿  Added 249 lines, removed 34 lines
      1  # Plano Técnico — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.0 · **Baseado na spec:** 1.0                      
      3 +**Versão:** 1.0 · **Baseado na spec:** 1.1                      
      4  
      5  > Aqui mora o COMO. Este arquivo pode e deve falar de linguagem,
          biblioteca e
      6  > arquitetura. O que ele **não** pode é introduzir regra de negó
         cio nova — se
     ...
       12  
       13  | Escolha | O quê | Por quê | O que descartei e por quê |
       14  |---|---|---|---|
       15 -| Linguagem | | | |                                           
       16 -| Testes | | | |                                              
       17 -| Parsing/validação | | | |                                   
       18 -| Aritmética monetária | | | |                                
       15 +| Linguagem | Java 21 (LTS) | Familiaridade declarada de desen
          +volvedor back-end Java; JDK 21.0.2 já verificado funcionando n
          +esta máquina sem instalação adicional; `java.math.BigDecimal` 
          +nativo resolve a exigência de aritmética decimal exata sem bib
          +lioteca externa. | Python e Node — nenhum dos dois está instal
          +ado nesta máquina (verificado via `python --version` e `node -
          +-version`); instalar agora consome prazo curto e adiciona risc
          +o de "funciona aqui, não funciona na correção". Kotlin — mesma
          + JVM, mas introduziria sintaxe nova sob pressão de tempo sem g
          +anho que a spec exija. |                                      
       16 +| Build | Maven 3.9 ou superior | Maven 3.9.6 já verificado fu
          +ncionando, casado ao JDK 21 instalado; convenção de projeto pa
          +drão para quem já é back-end Java. | Gradle — funcionalmente e
          +quivalente, mas sem motivo para trocar de ferramenta já domina
          +da e já disponível. |                                         
       17 +| Testes | JUnit 5 (Jupiter) | Parametrização nativa (`@Parame
          +terizedTest`) essencial para as matrizes de fronteira monetári
          +a e de nota fiscal; integração direta com Maven Surefire, sem 
          +configuração extra. | TestNG — capacidade equivalente, sem mot
          +ivo para introduzir dependência adicional. |                  
       18 +| Parsing/validação | Jackson Databind, lido via árvore `JsonN
          +ode` na camada de entrada | `JsonNode` permite inspecionar o t
          +ipo JSON bruto de cada campo (necessário para distinguir `CAMP
          +O_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` conf
          +orme RN-002) e preservar `valor_informado` exatamente como rec
          +ebido, inclusive quando o tipo é inválido. Suporta leitura de 
          +números como `BigDecimal` exato via configuração (ver §7). | G
          +son — exigiria a mesma configuração de números decimais, porém
          + menos familiar ao perfil declarado. `org.json` — comportament
          +o de conversão numérica para `BigDecimal` historicamente menos
          + documentado/confiável. Parser JSON escrito à mão — risco desn
          +ecessário de bugs de parsing sob prazo de dois dias, sem ganho
          + que a spec exija. |                                          
       19 +| Aritmética monetária | `java.math.BigDecimal`, construído se
          +mpre a partir de texto/`decimalValue()`, nunca de `double` | É
          + exatamente o mecanismo que garante `100.005 → 100,01` (RN-004
          +): ponto flutuante binário representaria `100.005` como `100.0
          +0499999999999...` e arredondaria para o lado errado. Nativo do
          + JDK, sem dependência extra. | `double`/`float` — fonte de bug
          + previsível e documentada pelo próprio `CLAUDE.md`. Biblioteca
          +s de dinheiro de terceiros (ex. Joda-Money) — desnecessárias; 
          +`BigDecimal` com escala e modo de arredondamento explícitos já
          + cobre tudo que a spec exige. |                               
       20 +| Empacotamento | Maven Shade Plugin, produzindo um único JAR 
          +executável | Gera `target/motor-reembolso.jar` autocontido (de
          +pendências + `Main-Class` no manifesto), executável só com `ja
          +va -jar`, sem exigir classpath externo na máquina do avaliador
          +. | `maven-assembly-plugin` — resultado equivalente, Shade é m
          +ais direto para o caso de um único fat jar sem relocations. Sp
          +ring Boot — framework pesado e servidor HTTP desnecessários pa
          +ra uma CLI que lê um arquivo e escreve outro. Wrapper `.sh`/`.
          +bat` — descartado por decisão explícita: mais uma superfície p
          +ara manter e testar em duas plataformas sem necessidade, já qu
          +e `java -jar` funciona identicamente em qualquer sistema com J
          +DK. |                                                         
       21  
       20 -<A linha de aritmética monetária não é decoração. Ponto flutua
          -nte em dinheiro é                                             
       21 -a fonte de bug mais previsível deste projeto.>                
       22 +---                                                           
       23  
       24  ## 2. Arquitetura
       25  
       25 -<Diagrama em blocos ou lista. Quais são as peças, o que cada u
          -ma faz, como                                                  
       26 -conversam. Uma tela, não uma tese.>                           
       26 +A arquitetura é um pipeline linear que segue **literalmente** 
          +os onze passos da seção 8.1 da spec. Cada passo é um estágio q
          +ue recebe a lista de itens (na ordem da entrada) e devolve a m
          +esma lista enriquecida — nenhum estágio reordena a lista mestr
          +a.                                                            
       27  
       28  ```
       29 -entrada JSON → <...> → <...> → saída JSON                     
       29 +entrada JSON                                                  
       30 +  │                                                           
       31 +  ▼                                                           
       32 +[Leitor]  → JsonNode da árvore completa                       
       33 +  │                                                           
       34 +  ▼                                                           
       35 +[1] Validador de envelope (RN-001)              — fatal se fal
          +har, nada mais executa                                        
       36 +  │                                                           
       37 +  ▼                                                           
       38 +[2] Validador de item / classificador estrutural (RN-002)     
       39 +  │      atribui indice_entrada (base 1, imutável) antes de qu
          +alquer validação                                              
       40 +  ▼                                                           
       41 +[3] Detector de despesa.id duplicado (RN-003)                 
       42 +  │                                                           
       43 +  ▼                                                           
       44 +[4] Normalizador (RN-004 valor · RN-005 categoria)            
       45 +  │                                                           
       46 +  ▼                                                           
       47 +[5] Avaliador de regras individuais (RN-006 · RN-007 · RN-008 
          +· RN-009)                                                     
       48 +  │                                                           
       49 +  ▼                                                           
       50 +[6] Seletor de itens aprovados em todas as validações individu
          +ais                                                           
       51 +  │                                                           
       52 +  ▼                                                           
       53 +[7] Detector de duplicidade econômica (RN-010)                
       54 +  │                                                           
       55 +  ▼                                                           
       56 +[8] Seletor de itens elegíveis após a duplicidade             
       57 +  │                                                           
       58 +  ▼                                                           
       59 +[9] Agregador de tetos (RN-011 · RN-012 · RN-013 · RN-014 · RN
          +-015)                                                         
       60 +  │                                                           
       61 +  ▼                                                           
       62 +[10] Compositor de saída — decisão final + motivos na ordem de
          + apresentação (8.3)                                           
       63 +  │                                                           
       64 +  ▼                                                           
       65 +[11] Somador do total (RN-018)                                
       66 +  │                                                           
       67 +  ▼                                                           
       68 +[Escritor] → JSON de saída                                    
       69  ```
       70  
       32 -**Fronteiras:** <o que é núcleo de regra de negócio puro e o q
          -ue é I/O. Onde                                                
       33 -essa linha está desenhada determina o quanto o sistema vai res
          -istir a mudança                                               
       34 -de requisito.>                                                
       71 +**Como a avaliação de regras funciona (evita parar no primeiro
          + motivo, sem executar tudo indiscriminadamente):**            
       72  
       36 -## 3. Modelo de dados                                         
       73 +- Cada regra é avaliada quando **todos os campos de que ela de
          +pende** estão estruturalmente válidos, conforme a matriz 8.2 —
          + não quando o item inteiro está livre de qualquer defeito.    
       74 +- Um erro em um campo que a regra **não usa** não impede essa 
          +regra de rodar. Exemplo: `despesa.data` malformada não impede 
          +a avaliação de `NOTA_FISCAL_AUSENTE`, que depende só de `despe
          +sa.valor` e `despesa.tem_nota_fiscal`.                        
       75 +- Motivos aplicáveis são **acumulados** num único acumulador p
          +or item ao longo do pipeline — nenhuma etapa substitui ou desc
          +arta motivos de etapas anteriores.                            
       76 +- Somente as **exclusões expressamente listadas em 8.4** inter
          +rompem etapas posteriores (ex.: item com `ID_DUPLICADO` não en
          +tra na detecção de duplicidade econômica nem na agregação; ite
          +m recusado nas validações individuais não entra na duplicidade
          + econômica). Fora dessas exclusões fechadas, nenhuma outra é i
          +nferida.                                                      
       77 +- A **ordem de processamento** (8.1, quando cada regra roda) e
          + a **ordem de apresentação dos motivos** (8.3, como a lista fi
          +nal de motivos de um item é ordenada antes de serializar) são 
          +duas ordens distintas. O acumulador guarda os motivos na ordem
          + em que foram detectados; o compositor (passo 10) os reordena 
          +conforme 8.3 só no momento de montar a saída.                 
       78  
       38 -<Estruturas internas. Como uma despesa é representada, como um
          - resultado de                                                 
       39 -avaliação é representado, o que carrega a justificativa.>     
       79 +**Fronteiras:**                                               
       80  
       41 -## 4. Como a política é representada                          
       81 +- **Núcleo puro** (passos 1 a 11, exceto leitura/escrita de ar
          +quivo): opera inteiramente sobre estruturas em memória. "Puro"
          + aqui significa **sem leitura/escrita de arquivo, sem CLI, sem
          + estado global e sem efeitos colaterais** — não significa ausê
          +ncia física do tipo `JsonNode` no modelo. O `JsonNode` de cada
          + item pode acompanhar o item como fotografia de auditoria de `
          +valor_informado` (ver §6) sem que isso quebre a pureza do núcl
          +eo.                                                           
       82 +- **I/O** (Leitor, Escritor, CLI/Main): isolado nas pontas do 
          +pipeline. O CLI/Main só orquestra — chama o leitor, invoca o n
          +úcleo, chama o escritor, e traduz o resultado (ou a falha) em 
          +código de saída (§3).                                         
       83  
       43 -<Os limites vivem onde? Constantes no código, arquivo de confi
          -guração, tabela?                                              
       44 -Esta decisão é a que mais barato ou mais caro vai custar se a 
          -política mudar.>                                              
       84 +Essa separação entre regra de negócio e CLI/I/O é mantida deli
          +beradamente para a versão atual, sem antecipar ou supor o cont
          +eúdo de qualquer mudança futura de requisito.                 
       85  
       46 -## 5. Decisões técnicas                                       
       86 +---                                                           
       87  
       48 -### DT-001 — <decisão>                                        
       88 +## 3. Contrato da CLI                                         
       89  
       50 -**Contexto:** <o que forçou a escolha>                        
       51 -**Decisão:** <o que foi decidido>                             
       52 -**Alternativa descartada:** <e por quê>                       
       53 -**Consequência:** <o que isso torna fácil e o que torna difíci
          -l>                                                            
       90 +```                                                           
       91 +java -jar target/motor-reembolso.jar calcular --input <arquivo
          +> --output <arquivo>                                          
       92 +```                                                           
       93  
       55 -### DT-002 — ...                                              
       94 +**Códigos de saída:**                                         
       95  
       57 -## 6. Estratégia de testes                                    
       96 +| Código | Significado |                                      
       97 +|---|---|                                                     
       98 +| `0` | Processamento concluído e arquivo de resultado escrito
          + em `--output`. |                                             
       99 +| `2` | Erro de uso ou de infraestrutura: argumento ausente, a
          +rquivo de entrada inexistente, arquivo ilegível, JSON sintatic
          +amente inválido, ou falha ao escrever `--output`. |           
      100 +| `3` | JSON sintaticamente legível, mas envelope inválido con
          +forme RN-001 (ex.: `periodo` ausente, `periodo.inicio` posteri
          +or a `periodo.fim`, `despesas` não é lista). |                
      101  
       59 -- **Nível:** <unitário, integração, ponta a ponta — e a propor
          -ção entre eles>                                               
       60 -- **Cada `RN-NNN` da spec tem teste?** <como você garante isso
          ->                                                             
       61 -- **Casos de borda da seção 7 da spec:** <cobertos como>      
       62 -- **Nomenclatura:** <como o nome do teste remete ao requisito 
          -— isso é o que                                                
       63 -  fecha a rastreabilidade na correção>                        
      102 +Para os códigos `2` e `3`:                                    
      103  
       65 -## 7. Riscos                                                  
      104 +- uma mensagem curta em **texto simples** é escrita em **stder
          +r** — não em stdout, e não como JSON estruturado. Não existe v
          +ocabulário JSON de erro nesta versão porque a spec não define 
          +um; inventar um agora seria vazar decisão de produto para o pl
          +ano sem base na spec.                                         
      105 +- nada é escrito em stdout;                                   
      106 +- o arquivo indicado em `--output` **não é criado nem sobrescr
          +ito**.                                                        
      107  
      108 +**A validação do envelope acontece antes de abrir ou sobrescre
          +ver o arquivo de saída.** Isso é o que garante a regra acima: 
          +o leitor primeiro faz o parsing sintático (falha → código `2`)
          + e o validador de envelope roda antes de qualquer tentativa de
          + abertura do caminho de `--output` (falha → código `3`). Só de
          +pois de o envelope ser confirmado válido o processo abre o arq
          +uivo de saída para escrita. Um erro de escrita nesse momento (
          +caminho não gravável, disco cheio) é infraestrutura, não regra
          + de negócio — cai no código `2`.                              
      109 +                                                              
      110 +---                                                           
      111 +                                                              
      112 +## 4. Modelo de dados                                         
      113 +                                                              
      114 +Estruturas internas do núcleo (descrição de responsabilidade, 
          +não implementação):                                           
      115 +                                                              
      116 +| Estrutura | Conteúdo |                                      
      117 +|---|---|                                                     
      118 +| **Item de entrada** | `indiceEntrada` (inteiro base 1, atrib
          +uído antes de qualquer validação, imutável) + `raw` (o `JsonNo
          +de` do elemento original, usado somente como fotografia de aud
          +itoria para `valor_informado`, nunca consultado por regra algu
          +ma). |                                                        
      119 +| **Campos estruturalmente validados** | Um mapa/estrutura com
          + os sete campos canônicos de 4.2, populado apenas para os camp
          +os que passaram na validação de RN-002. Campo inválido fica au
          +sente aqui — nenhuma coerção, nenhum valor padrão. |          
      120 +| **`valor_informado`** | O valor JSON bruto de `despesa.valor
          +` exatamente como recebido (número, texto, booleano, lista, ob
          +jeto ou nulo), extraído do `JsonNode`. Nenhuma regra financeir
          +a o consulta — existe só para reaparecer na saída (4.3). |    
      121 +| **`valor_normalizado`** | `BigDecimal` de escala 2, resultad
          +o de RN-004. Nulo quando `despesa.valor` não é um número estru
          +turalmente válido. |                                          
      122 +| **Categoria normalizada** | Texto resultante de RN-005 (trim
          +, minúsculas, sem acento). Nula quando não calculável ou fora 
          +do vocabulário fechado. |                                     
      123 +| **Acumulador de motivos** | Lista de `Motivo`, que só cresce
          + ao longo do pipeline — nenhuma etapa remove um motivo já acum
          +ulado por outra. Reordenada apenas no passo 10, conforme 8.3. 
          +|                                                             
      124 +| **`Motivo`** | Três campos, cada um representado por um tipo
          + fechado (enumeração), não por texto livre espalhado pelo códi
          +go: `codigo` (enumeração com os onze valores de 4.5 — `ITEM_TI
          +PO_INVALIDO`, `CAMPO_AUSENTE`, ..., `TETO_HOSPEDAGEM_APLICADO`
          +), `regra` (enumeração `RN_001`..`RN_018`, cada valor carregan
          +do o texto canônico `"RN-NNN"`), `campo` (enumeração dos sete 
          +nomes canônicos de 4.2, cada valor carregando o texto canônico
          + `"despesa.<campo>"`, mais a possibilidade de nulo). A seriali
          +zação de cada enum para o texto exigido pela spec é feita num 
          +único ponto por enum — não há string `"RN-004"` ou `"despesa.v
          +alor"` repetida em vários lugares do código. |                
      125 +| **Decisão final** | Enumeração com os quatro valores de 4.4 
          +(`INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO`, `NAO
          +_REEMBOLSADO_TETO_ESGOTADO`, `RECUSADO`). |                   
      126 +| **`valor_reembolsavel`** | `BigDecimal` de escala 2, sempre 
          +`0,00` para item recusado ou esgotado. |                      
      127 +| **Resultado por item** | Agrega `indiceEntrada`, `id` (ou nu
          +lo), `valor_informado`, `valor_normalizado`, `valor_reembolsav
          +el`, decisão final e a lista de motivos já ordenada conforme 8
          +.3. |                                                         
      128 +| **Resultado geral** | `colaborador` (três campos texto-ou-nu
          +lo), `periodo` (competência texto-ou-nulo, início e fim), a li
          +sta de resultados por item na ordem da entrada, e `total_reemb
          +olsavel` (RN-018). |                                          
      129 +                                                              
      130 +---                                                           
      131 +                                                              
      132 +## 5. Como a política é representada                          
      133 +                                                              
      134 +Uma única estrutura simples e imutável, `PoliticaReembolso`, c
          +om quatro valores fixos:                                      
      135 +                                                              
      136 +```                                                           
      137 +PoliticaReembolso {                                           
      138 +  limiteDiarioAlimentacao        = 60.00                      
      139 +  limiteDiarioTransporteUrbano   = 80.00                      
      140 +  limiteIndividualHospedagem     = 250.00                     
      141 +  gatilhoNotaFiscal              = 100.00   // estritamente ma
          +ior que                                                       
      142 +}                                                             
      143 +```                                                           
      144 +                                                              
      145 +Instanciada uma vez, e passada por construtor comum aos compon
          +entes do núcleo que precisam desses valores (avaliador de regr
          +as individuais, agregador de tetos). Sem mecanismo genérico de
          + regras, sem DSL, sem banco de dados, sem framework de injeção
          + de dependência, sem arquivo externo de configuração. A políti
          +ca tem quatro números fixos definidos pela spec 1.1; mudá-los 
          +é editar uma estrutura, não uma feature. Construir generalidad
          +e para requisitos futuros desconhecidos seria exatamente o exc
          +esso de arquitetura que o FAQ do desafio adverte a evitar.    
      146 +                                                              
      147 +---                                                           
      148 +                                                              
      149 +## 6. Fronteira entre Jackson e o núcleo de regras            
      150 +                                                              
      151 +- A camada de **entrada** (leitor) usa `JsonNode` para reconhe
          +cer o tipo JSON bruto de cada campo — é o que permite distingu
          +ir `CAMPO_AUSENTE` de `CAMPO_TIPO_INVALIDO` de `CAMPO_FORMATO_
          +INVALIDO` (RN-002) e preservar `valor_informado` tal como rece
          +bido, inclusive quando o tipo é inválido.                     
      152 +- O valor monetário efetivamente usado por qualquer regra é se
          +mpre `BigDecimal` — nunca `JsonNode`, nunca `double`.         
      153 +- O `JsonNode` do item pode acompanhar o item ao longo do mode
          +lo **somente** como a fotografia de auditoria que produz `valo
          +r_informado` na saída. Isso não compromete a pureza do núcleo 
          +(§2): pureza aqui é sobre I/O e efeitos colaterais, não sobre 
          +quais tipos aparecem no modelo de dados.                      
      154 +- **Nenhuma regra financeira consulta `valor_informado`.** As 
          +regras (RN-004 em diante) consultam exclusivamente os campos j
          +á validados e normalizados (`valor_normalizado`, categoria nor
          +malizada, etc.).                                              
      155 +- Não será criada uma abstração genérica de "valor JSON" só pa
          +ra eliminar o tipo `JsonNode` do modelo de auditoria — seria c
          +omplexidade extra sem necessidade, dado que o uso do `JsonNode
          +` já está contido a um único propósito (auditoria) e nunca vaz
          +a para decisão financeira.                                    
      156 +                                                              
      157 +---                                                           
      158 +                                                              
      159 +## 7. Estratégia monetária                                    
      160 +                                                              
      161 +- `ObjectMapper` configurado para ler números JSON como `BigDe
          +cimal` exato (habilitar `USE_BIG_DECIMAL_FOR_FLOATS` na leitur
          +a), de modo que a árvore (`JsonNode`) represente números decim
          +ais como `DecimalNode`/`BigDecimal`, nunca como `double`.     
      162 +- Números são obtidos via `decimalValue()` — nunca via `double
          +Value()`.                                                     
      163 +- Nunca construir um `BigDecimal` a partir de um `double` (iss
          +o reintroduziria o erro binário que a configuração acima exist
          +e para evitar).                                               
      164 +- Normalização (RN-004): `valor.setScale(2, RoundingMode.HALF_
          +UP)`.                                                         
      165 +- Toda comparação de valores monetários usa `compareTo` — nunc
          +a `equals` (que também compara escala) nem `==`.              
      166 +- Na serialização de saída, valores monetários são escritos co
          +mo **números JSON decimais em notação simples**, com exatament
          +e duas casas — nunca em notação científica e nunca como string
          +.                                                             
      167 +- **Testes obrigatórios de fronteira monetária** (detalhados n
          +a matriz de rastreabilidade, §9): `33.333` → `33,33`; `33.335`
          + → `33,34`; `33.345` → `33,35`; `100.004` → `100,00`; `100.005
          +` → `100,01`. Este último é o teste-canário que comprova que a
          + leitura é decimal-exata e não passou por `double` em nenhum p
          +onto do caminho.                                              
      168 +                                                              
      169 +---                                                           
      170 +                                                              
      171 +## 8. Decisões técnicas                                       
      172 +                                                              
      173 +### DT-001 — Linguagem e ambiente de execução                 
      174 +                                                              
      175 +**Contexto:** prazo de dois dias, desenvolvedor com familiarid
          +ade declarada em Java, ambiente desta máquina já inspecionado 
          +(Java 21.0.2 e Maven 3.9.6 funcionando; Python e Node ausentes
          +).                                                            
      176 +**Decisão:** Java 21 como linguagem única do projeto.         
      177 +**Alternativa descartada:** Python/Node — não instalados nesta
          + máquina, custariam tempo de setup sob prazo curto; Kotlin — m
          +esma JVM, mas sintaxe nova sem necessidade.                   
      178 +**Consequência:** zero passo de instalação para rodar ou compi
          +lar; qualquer máquina com JDK 21+ reproduz o build sem ajuste.
      179 +                                                              
      180 +### DT-002 — Empacotamento em JAR único via Maven Shade Plugin
      181 +                                                              
      182 +**Contexto:** o contrato de execução exige `java -jar target/m
          +otor-reembolso.jar ...` funcionando sem passos adicionais e se
          +m wrapper de shell.                                           
      183 +**Decisão:** configurar o Maven Shade Plugin para produzir, a 
          +partir de `mvn package`, exatamente `target/motor-reembolso.ja
          +r` — um único artefato contendo todas as dependências (Jackson
          +) e o `Main-Class` no manifesto. Fixar `<finalName>motor-reemb
          +olso</finalName>` para que o nome do artefato não dependa da v
          +ersão do projeto.                                             
      184 +**Alternativa descartada:** `maven-assembly-plugin` (resultado
          + equivalente, Shade é mais direto para este caso de fat jar si
          +mples sem relocations); wrapper `.sh`/`.bat` (descartado por i
          +nstrução explícita — superfície de manutenção extra em duas pl
          +ataformas sem necessidade, já que `java -jar` funciona igual e
          +m qualquer SO com JDK).                                       
      185 +**Consequência:** um único comando de build (`mvn package`) e 
          +um único comando de execução, sem classpath manual, sem script
          + adicional para manter ou testar.                             
      186 +                                                              
      187 +### DT-003 — Contrato de CLI e códigos de saída               
      188 +                                                              
      189 +**Contexto:** a interface é fixa (`--input`/`--output`), mas a
          + spec não define o que acontece na CLI quando o processamento 
          +não pode ocorrer — isso é contrato de execução, não regra de n
          +egócio.                                                       
      190 +**Decisão:** três códigos de saída (`0`, `2`, `3`), mensagem e
          +m texto simples em stderr para os códigos de erro, nenhuma esc
          +rita em `--output` quando o código não é `0`, e validação de e
          +nvelope executada antes de qualquer abertura do arquivo de saí
          +da.                                                           
      191 +**Alternativa descartada:** vocabulário de erro em JSON estrut
          +urado (introduziria um esquema que a spec não define); escreve
          +r um JSON de erro no próprio `--output` (arriscaria confundir 
          +"resultado" com "estado de erro" no mesmo arquivo, e violaria 
          +a garantia de não sobrescrever `--output` em falha).          
      192 +**Consequência:** contrato simples e verificável por teste de 
          +CLI; separa claramente "processamento não ocorreu" de "process
          +amento ocorreu com itens recusados" — este último ainda é códi
          +go `0`, porque recusa de item é resultado válido, não falha de
          + processo.                                                    
      193 +                                                              
      194 +### DT-004 — `BigDecimal` com parsing decimal-exato           
      195 +                                                              
      196 +**Contexto:** a spec exige `100.005 → 100,01`; ponto flutuante
          + binário (`double`) representaria `100.005` como `100.00499999
          +999999...` e arredondaria para o lado errado sob `HALF_UP`.   
      197 +**Decisão:** `BigDecimal` de ponta a ponta, com `ObjectMapper`
          + configurado para `USE_BIG_DECIMAL_FOR_FLOATS`, valores obtido
          +s via `decimalValue()`, nunca via `doubleValue()` ou construçã
          +o a partir de `double`.                                       
      198 +**Alternativa descartada:** `double`/`float` (fonte de bug doc
          +umentada); bibliotecas de dinheiro de terceiros (desnecessária
          +s — `BigDecimal` nativo já cobre a exigência).                
      199 +**Consequência:** os cinco valores de fronteira exigidos (33.3
          +33, 33.335, 33.345, 100.004, 100.005) arredondam corretamente 
          +por construção, não por sorte; qualquer regressão futura que r
          +eintroduza `double` em algum ponto do caminho é detectável pel
          +o teste-canário de 100.005.                                   
      200 +                                                              
      201 +### DT-005 — Parsing por árvore (`JsonNode`) na camada de entr
          +ada                                                           
      202 +                                                              
      203 +**Contexto:** RN-002 exige distinguir três classes de erro est
          +rutural por campo (ausência, tipo, formato) e 4.3 exige preser
          +var `valor_informado` exatamente como recebido, mesmo com tipo
          + inválido — isso exige inspecionar o JSON bruto, não um objeto
          + já desserializado e coagido.                                 
      204 +**Decisão:** ler cada item como `JsonNode`, inspecionar `JsonN
          +odeType` explicitamente por campo (nunca usar acessores permis
          +sivos como `asBoolean()`/`asInt()`, que fariam coerção que RN-
          +002 proíbe), e só então produzir os campos validados e normali
          +zados que o núcleo consome.                                   
      205 +**Alternativa descartada:** desserialização direta para um obj
          +eto POJO tipado (perderia a distinção entre "campo ausente" e 
          +"campo de tipo errado", e perderia o valor bruto para `valor_i
          +nformado` quando o tipo é inválido).                          
      206 +**Consequência:** classificação estrutural fiel à spec, ao cus
          +to de uma camada de leitura mais verbosa que uma desserializaç
          +ão direta.                                                    
      207 +                                                              
      208 +### DT-006 — Arquitetura em pipeline linear seguindo a seção 8
          +.1                                                            
      209 +                                                              
      210 +**Contexto:** a ordem de processamento é normativa (seção 8 da
          + spec) e distinta da ordem de apresentação de motivos (8.3); u
          +ma arquitetura que não espelhe isso arrisca produzir resultado
          +s corretos por acidente, não por construção.                  
      211 +**Decisão:** onze estágios lineares, um por passo de 8.1, cada
          + um operando sobre a lista completa de itens sem jamais reorde
          +ná-la; motivos acumulados por item ao longo dos estágios; reor
          +denação para apresentação isolada no passo 10.                
      212 +**Alternativa descartada:** um único método monolítico avalian
          +do tudo por item em qualquer ordem interna conveniente — mais 
          +difícil de auditar linha a linha contra a seção 8, e mais fáci
          +l de violar sem perceber a regra "erros em campos não usados n
          +ão impedem outras regras".                                    
      213 +**Consequência:** cada estágio é testável isoladamente e mapei
          +a 1:1 para uma linha da seção 8.1, o que facilita tanto a impl
          +ementação quanto a auditoria da rastreabilidade.              
      214 +                                                              
      215 +### DT-007 — Representação da política como estrutura imutável
          + simples                                                      
      216 +                                                              
      217 +**Contexto:** a política tem quatro valores numéricos fixos ne
          +sta versão da spec.                                           
      218 +**Decisão:** uma estrutura única e imutável, `PoliticaReembols
          +o`, sem mecanismo de configuração externa.                    
      219 +**Alternativa descartada:** motor de regras genérico, DSL, arq
          +uivo de configuração externo, banco de dados, framework de inj
          +eção de dependência — todos resolveriam um problema de flexibi
          +lidade que a spec atual não tem.                              
      220 +**Consequência:** mudar um teto é uma edição de quatro linhas;
          + em troca, qualquer flexibilidade não prevista pela spec 1.1 e
          +xigiria refatoração explícita, o que é aceitável porque não há
          + evidência de que essa flexibilidade seja necessária agora.   
      221 +                                                              
      222 +### DT-008 — `Motivo` como três enumerações fechadas          
      223 +                                                              
      224 +**Contexto:** 4.5 define um vocabulário fechado para `codigo`,
          + `regra` e `campo`; strings livres repetidas pelo código são u
          +ma fonte comum de divergência de grafia entre o motivo emitido
          + e o exigido pela spec.                                       
      225 +**Decisão:** três enumerações (`MotivoCodigo`, `RegraNegocio`,
          + `CampoCanonico`), cada uma com o texto canônico correspondent
          +e definido em um único lugar.                                 
      226 +**Alternativa descartada:** strings soltas (`"RN-004"`, `"desp
          +esa.valor"`) espalhadas pelas classes que emitem motivos — fun
          +ciona, mas cada ocorrência é uma chance de erro de digitação n
          +ão detectado por compilação.                                  
      227 +**Consequência:** erro de grafia num código de motivo vira err
          +o de compilação, não uma divergência silenciosa só visível em 
          +teste ou na correção.                                         
      228 +                                                              
      229 +### DT-009 — Estratégia de testes em três níveis              
      230 +                                                              
      231 +**Contexto:** a rubrica avalia rastreabilidade `spec → tasks →
          + commits → testes`; cada RN e cada CA precisa de destino verif
          +icável.                                                       
      232 +**Decisão:** testes unitários por regra (maioria), poucos test
          +es de integração de pipeline completo (o arquivo de exemplo e 
          +fixtures adicionais), e um teste de contrato/CLI cobrindo códi
          +gos de saída e comportamento de arquivo. Nomenclatura de class
          +e/método referenciando o `RN-NNN` correspondente.             
      233 +**Alternativa descartada:** cobertura só por teste de integraç
          +ão ponta a ponta — esconderia qual regra especificamente falho
          +u e dificultaria o grep de rastreabilidade que a rubrica valor
          +iza.                                                          
      234 +**Consequência:** qualquer regra da spec é localizável no códi
          +go de teste por busca textual do próprio identificador `RN-NNN
          +`; ver matriz completa em §9.                                 
      235 +                                                              
      236 +---                                                           
      237 +                                                              
      238 +## 9. Estratégia de testes                                    
      239 +                                                              
      240 +- **Nível e proporção:** majoritariamente unitário (uma regra 
          +de negócio = um grupo de testes isolado, sem I/O real); um tes
          +te de integração de pipeline completo contra `exemplos/despesa
          +s-exemplo.json`; um teste de contrato/CLI cobrindo códigos de 
          +saída e comportamento de arquivo.                             
      241 +- **Cada `RN-NNN` tem teste?** Garantido pela matriz de rastre
          +abilidade abaixo — todo RN-001 a RN-018 aparece em pelo menos 
          +uma linha; conferência manual cruzada com a spec ao fechar o D
          +ia 1, antes do envelope do Dia 2.                             
      242 +- **Casos de borda da seção 7:** cobertos pelos mesmos grupos 
          +de teste da matriz, via os `CA-NNN` correspondentes, que deriv
          +am diretamente da tabela de casos de borda.                   
      243 +- **Nomenclatura:** classe/grupo nomeado pelo identificador da
          + regra (`RN004NormalizacaoMonetariaTest`), método nomeado pelo
          + cenário e resultado esperado (`rn004_100_005_arredondaParaCim
          +a_100_01()`), de modo que o `RN-NNN` ou `CA-NNN` seja localizá
          +vel por busca textual direta entre spec e teste.              
      244 +                                                              
      245 +### Matriz de rastreabilidade                                 
      246 +                                                              
      247 +| RN / CA | Grupo de teste planejado | Nível |                
      248 +|---|---|---|                                                 
      249 +| RN-001 · CA-020 | `EnvelopeValidoTest` — período invertido, 
          +`despesas` vazia, bloco `colaborador` malformado tolerado | Un
          +idade |                                                       
      250 +| RN-002 · CA-021 · CA-022 · CA-023 | `ContratoDoItemTest` — `
          +CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` 
          +por campo, `ITEM_TIPO_INVALIDO`, ordem canônica de múltiplos m
          +otivos | Unidade |                                            
      251 +| RN-002 (valor_informado) | `ValorInformadoTest` — preservaçã
          +o do valor bruto para tipos válidos e inválidos (string, boole
          +ano, ausente, elemento não-objeto) | Unidade |                
      252 +| RN-003 · CA-019 | `IdDuplicadoTest` — todas as ocorrências r
          +ecusadas, ID inválido não entra na verificação | Unidade |    
      253 +| RN-004 · CA-009 · CA-018 | `NormalizacaoMonetariaTest` (para
          +metrizado) — `33.333`, `33.335`, `33.345`, `100.004`, `100.005
          +` | Unidade |                                                 
      254 +| RN-005 · CA-015 | `NormalizacaoCategoriaTest` — caixa, acent
          +o, espaço nas pontas; `transporte urbano` não reconhecido | Un
          +idade |                                                       
      255 +| RN-006 · CA-017 | `ValorNaoPositivoTest` — negativo, zero, v
          +alor que normaliza para zero; total do período não reduz | Uni
          +dade |                                                        
      256 +| RN-007 · CA-016 | `CategoriaForaPoliticaTest` — `coworking` 
          +recusado antes de qualquer teto | Unidade |                   
      257 +| RN-008 · CA-011 · CA-012 | `CompetenciaTest` — bordas inclus
          +ivas do período, data fora da janela | Unidade |              
      258 +| RN-009 · CA-008 · CA-009 | `NotaFiscalTest` (parametrizado) 
          +— `100,00` elegível, `100,01` recusado, deslocamento de fronte
          +ira por arredondamento | Unidade |                            
      259 +| RN-010 · CA-013 · CA-014 | `DuplicidadeEconomicaTest` — chav
          +e exata, primeira ocorrência mantida, `100.00`/`100.01` não sã
          +o duplicata | Unidade |                                       
      260 +| RN-011 · RN-012 · CA-004 | `TetoDiarioTest` — agregação por 
          +data e categoria (alimentação e transporte urbano) | Unidade |
      261 +| RN-013 · CA-007 | `TetoHospedagemTest` — teto por lançamento
          +, independente de descrição, duas hospedagens no mesmo dia som
          +ando até R$ 500,00 | Unidade |                                
      262 +| RN-014 · CA-005 | `ReembolsoParcialTest` — corte no teto, nu
          +nca recusa integral por ultrapassagem | Unidade |             
      263 +| RN-015 · CA-006 | `DistribuicaoTetoTest` — consumo de saldo 
          +em ordem de `indice_entrada`, estado `NAO_REEMBOLSADO_TETO_ESG
          +OTADO` distinto de `RECUSADO` | Unidade |                     
      264 +| RN-016 · CA-010 | `RegraViagemEfeitoNuloTest` — troca de des
          +crição/campo `em_viagem` desconhecido não altera resultado, it
          +em único para isolar de RN-010 | Unidade |                    
      265 +| — (campos desconhecidos) | `CamposDesconhecidosTest` — campo
          + fora do contrato em `despesa` e em `colaborador` é ignorado s
          +ilenciosamente | Unidade |                                    
      266 +| RN-017 · CA-002 | `ComposicaoSaidaTest` — toda posição produ
          +z um registro, ordem da entrada preservada, nenhum item desapa
          +rece | Unidade |                                              
      267 +| — (ordem de apresentação, 8.3/8.4) | `OrdemMotivosTest` — os
          + dois exemplos normativos de 8.4 (três motivos simultâneos; ex
          +clusão de nota por valor não positivo) | Unidade |            
      268 +| RN-018 · CA-001 · CA-003 | `TotalPeriodoTest` — soma dos `va
          +lor_reembolsavel` apresentados igual ao total | Unidade/Integr
          +ação |                                                        
      269 +| CA-001 · CA-002 · CA-003 (integral) | `ExemploCompletoIT` — 
          +os 14 itens de `exemplos/despesas-exemplo.json` processados de
          + ponta a ponta, comparados campo a campo contra um fixture de 
          +saída esperada derivado da tabela 4.7; `total_reembolsavel` = 
          +R$ 585,43 | Integração |                                      
      270 +| — (contrato de execução) | `CliContratoTest` — código `0` em
          + sucesso; código `2` para argumento ausente/arquivo inexistent
          +e/JSON sintaticamente inválido/falha de escrita; código `3` pa
          +ra envelope inválido; mensagem em stderr, nada em stdout, `--o
          +utput` não criado nem sobrescrito nos códigos `2` e `3` | Cont
          +rato/CLI |                                                    
      271 +                                                              
      272 +Todo identificador de RN-001 a RN-018 e de CA-001 a CA-023 apa
          +rece em pelo menos uma linha desta matriz.                    
      273 +                                                              
      274 +---                                                           
      275 +                                                              
      276 +## 10. Riscos                                                 
      277 +                                                              
      278  | Risco | Probabilidade | O que faço se acontecer |
      279  |---|---|---|
       69 -| | | |                                                       
      280 +| Parsing de número via `double` reintroduzido em algum ponto 
          +(ex. troca de biblioteca, refactor apressado) | Média | Teste-
          +canário de `100.005` (RN-004) falha imediatamente e aponta o p
          +onto exato da regressão. |                                    
      281 +| Coerção implícita de tipo pelo Jackson (`asBoolean()`/`asInt
          +()` em vez de checagem explícita de `JsonNodeType`) | Média | 
          +Revisão de código restrita a essa camada de leitura antes de f
          +echar RN-002; teste dedicado por campo cobrindo booleano-como-
          +número e o inverso. |                                         
      282 +| Regra parando no primeiro motivo encontrado (short-circuit i
          +ndevido) | Média | `OrdemMotivosTest` e os testes de RN-002 co
          +m múltiplos motivos (`CA-021`, `CA-023`) capturam isso diretam
          +ente. |                                                       
      283 +| Excesso de arquitetura (motor de regras genérico, configuraç
          +ão externa) sob tentação de "preparar para o Dia 2" | Baixa ma
          +s real — o FAQ do desafio avisa explicitamente contra isso | M
          +anter `PoliticaReembolso` simples (§5) e a arquitetura de pipe
          +line linear (§2); qualquer generalização é adicionada só quand
          +o um requisito real a exigir, nunca antes. |                  
      284 +| Mudança de requisito do Dia 2 exigir tocar código de I/O alé
          +m do núcleo | Desconhecida — não antecipada nesta versão | A f
          +ronteira núcleo/CLI (§2) já isola regra de negócio de I/O; se 
          +a mudança for de regra, o núcleo absorve; se for de contrato d
          +e execução, o CLI absorve. Nenhum conteúdo do Dia 2 é suposto 
          +aqui. |                                                       

● Agora as verificações de leitura solicitadas.

  Searched for 1 pattern, ran 5 shell commands (ctrl+o to expand)

● plan.md escrito. Resumo das verificações:

  Seções preenchidas: todas as 7 do template — Stack, Arquitetura, Modelo de
  dados, Como a política é representada, Decisões técnicas, Estratégia de
  testes, Riscos — mais duas seções novas exigidas pelos refinamentos (Contrato
  da CLI, Fronteira Jackson/núcleo), sem remover nenhuma pergunta original do
  template.

  Decisões DT criadas: 9 — DT-001 (linguagem/ambiente), DT-002 (empacotamento
  Shade), DT-003 (contrato CLI e exit codes), DT-004 (BigDecimal decimal-exato),
  DT-005 (parsing por árvore JsonNode), DT-006 (pipeline seguindo 8.1), DT-007
  (política como estrutura imutável), DT-008 (Motivo como três enums fechados),
  DT-009 (testes em três níveis).

  Arquitetura planejada: pipeline linear de 11 estágios espelhando literalmente
  8.1, com acumulador de motivos por item (sem short-circuit), exclusões
  restritas às de 8.4, e ordem de apresentação (8.3) separada da ordem de
  processamento. Núcleo puro (sem I/O/CLI/estado global/efeitos colaterais, mas
  podendo carregar JsonNode só como auditoria de valor_informado) isolado do
  CLI/leitor/escritor.

  Matriz de testes: 22 linhas, cobrindo unidade, integração (ExemploCompletoIT)
  e contrato/CLI (CliContratoTest).

  Cobertura RN/CA: confirmada por contagem — RN-001 a RN-018 todos com ≥1
  ocorrência (mín. 1, em RN-016/RN-017; máx. 10, em RN-002); CA-001 a CA-023
  todos com ≥1 ocorrência. Nenhum identificador ausente.

  Placeholders restantes: nenhum. As únicas ocorrências de <...> são conteúdo
  legítimo (<arquivo> no exemplo de comando, <finalName> como tag XML de
  exemplo, despesa.<campo> como notação) — não são placeholders do template
  original.

  Arquivos modificados: só specs/001-motor-reembolso/plan.md (249 inserções, 34
  remoções). git status --short confirma que nenhum outro arquivo sob meu
  controle foi tocado; spec.md, DECISIONS.md e a sessão em docs/ seguem com as
  modificações não commitadas que já existiam antes desta tarefa (não mexi
  nelas).

  Problemas/pendências: nenhum bloqueante. git diff --check só acusou o aviso
  padrão de conversão LF→CRLF do Git no Windows, não um erro de whitespace real.
  Nenhum commit, git add, código, teste ou tasks.md foi criado — conforme
  instruído.

✻ Sautéed for 4m 3s

※ recap: We finished planning the reembolso engine: spec 1.1 is approved and plan.md is now written with the Java stack, CLI contract, pipeline architecture, and test matrix. Next step is writing tasks.md to break the plan into executable tasks. (disable recaps in /config)

❯ A auditoria humana do plan.md encontrou problemas que precisam ser corrigidos 
  antes da aprovação. Edite exclusivamente:                                     
                                                                                
  specs/001-motor-reembolso/plan.md                                             
                                                                                
  Não altere spec.md, DECISIONS.md, tasks.md, sessões, templates, código,       
  testes ou qualquer outro arquivo.                                             
                                                                                
  Aplique as seguintes correções:                                               
                                                                                
  1. Corrija o modelo de categoria normalizada.                                 
                                                                                
  A redação atual diz que a categoria normalizada fica nula quando está fora do 
  vocabulário fechado. Isso contradiz RN-005 e RN-007.                          
                                                                                
  O comportamento correto é:                                                    
                                                                                
  - categoria estruturalmente inválida, ausente ou nula → categoria normalizada 
  nula;                                                                         
  - categoria estruturalmente válida → sempre produzir o texto normalizado por  
  trim, caixa e acentos;                                                        
  - o texto normalizado pode ser `coworking` ou qualquer outro valor fora da    
  política;                                                                     
  - RN-007 compara esse texto com o conjunto permitido e produz                 
  CATEGORIA_FORA_POLITICA;                                                      
  - não converter categoria desconhecida para nulo.                             
                                                                                
  Atualize o modelo, a arquitetura e qualquer decisão técnica afetada.          
                                                                                
  2. Corrija a contagem de MotivoCodigo.                                        
                                                                                
  A seção 4.5 da spec contém exatamente 13 códigos:                             
                                                                                
  - ITEM_TIPO_INVALIDO                                                          
  - CAMPO_AUSENTE                                                               
  - CAMPO_TIPO_INVALIDO                                                         
  - CAMPO_FORMATO_INVALIDO                                                      
  - ID_DUPLICADO                                                                
  - VALOR_NAO_POSITIVO                                                          
  - CATEGORIA_FORA_POLITICA                                                     
  - FORA_COMPETENCIA                                                            
  - NOTA_FISCAL_AUSENTE                                                         
  - DUPLICIDADE                                                                 
  - TETO_DIARIO_APLICADO                                                        
  - TETO_DIARIO_ESGOTADO                                                        
  - TETO_HOSPEDAGEM_APLICADO                                                    
                                                                                
  Substitua “onze valores” por “treze valores” e verifique se nenhuma outra     
  contagem incorreta permanece.                                                 
                                                                                
  3. Torne verdadeira a garantia de preservação de --output.                    
                                                                                
  O plano promete que códigos 2 e 3 não criam nem sobrescrevem o destino. Para  
  falhas de escrita, essa garantia exige estratégia explícita.                  
                                                                                
  Planeje:                                                                      
                                                                                
  - serializar o resultado completo para arquivo temporário no mesmo diretório  
  do destino;                                                                   
  - fechar e concluir totalmente o temporário antes de tocar no destino;        
  - somente depois mover/substituir o destino;                                  
  - preferir Files.move com ATOMIC_MOVE e REPLACE_EXISTING;                     
  - se o movimento/substituição falhar, retornar código 2 e manter o destino    
  anterior intacto;                                                             
  - remover o temporário em caso de falha, quando possível;                     
  - nunca abrir o arquivo de destino diretamente para escrever o JSON           
  progressivamente.                                                             
                                                                                
  Atualize DT-003 ou crie uma nova DT específica, mantendo contexto, decisão,   
  alternativa e consequência.                                                   
                                                                                
  Adicione testes planejados para:                                              
                                                                                
  - envelope inválido não alterar um arquivo preexistente;                      
  - JSON inválido não alterar um arquivo preexistente;                          
  - falha antes da substituição final não alterar um arquivo preexistente;      
  - sucesso substituir o destino pelo resultado completo;                       
  - nenhum arquivo parcialmente escrito ficar no caminho oficial de --output.   
                                                                                
  4. Corrija a execução do teste integral pelo Maven.                           
                                                                                
  `ExemploCompletoIT` não é incluído pelo padrão do Maven Surefire em `mvn      
  test`.                                                                        
                                                                                
  Escolha a solução mais simples:                                               
                                                                                
  - renomear para `ExemploCompletoTest`;                                        
  - manter todos os testes, inclusive integração e CLI, executáveis por `mvn    
  test`;                                                                        
  - não introduzir Maven Failsafe somente por causa da nomenclatura.            
                                                                                
  Registre explicitamente que:                                                  
                                                                                
  mvn test                                                                      
                                                                                
  executa todos os testes planejados.                                           
                                                                                
  E que:                                                                        
                                                                                
  mvn package                                                                   
                                                                                
  executa os testes antes de gerar o JAR, salvo uso explícito e não recomendado 
  de skipTests.                                                                 
                                                                                
  5. Defina a estrutura física do projeto respeitando a entrega do DESAFIO.md.  
                                                                                
  Use:                                                                          
                                                                                
  - pom.xml                                                                     
  - src/main/java/... para código de produção;                                  
  - tests/java/... para testes Java;                                            
  - tests/resources/... para fixtures;                                          
  - configuração de testSourceDirectory e testResources no pom.xml para que     
  Maven compile e execute a pasta superior tests/;                              
  - target/ apenas para artefatos gerados.                                      
                                                                                
  Não crie os diretórios agora; apenas documente no plan.md.                    
                                                                                
  A estrutura deve manter a pasta superior tests/ exigida pelo desafio e        
  continuar permitindo `mvn test`.                                              
                                                                                
  6. Corrija a origem do fixture integral.                                      
                                                                                
  Não diga que o fixture é derivado apenas da tabela 4.7, porque ela é uma      
  representação abreviada.                                                      
                                                                                
  Declare que o fixture esperado:                                               
                                                                                
  - é escrito e revisado manualmente a partir do schema completo das seções 4.3 
  a 4.5;                                                                        
  - usa a tabela 4.7 para decisões e valores;                                   
  - usa RN-017 e a ordem 8.3 para os objetos completos de motivo;               
  - contém metadados, valores informados, valores normalizados, decisões,       
  motivos completos e total;                                                    
  - não é gerado pelo próprio motor em teste;                                   
  - é comparado estruturalmente como JSON, sem depender de espaços ou           
  formatação textual.                                                           
                                                                                
  7. Melhore a rastreabilidade dos testes.                                      
                                                                                
  Além de RN-NNN, os testes devem carregar os CA-NNN aplicáveis, por nome de    
  método, @DisplayName ou documentação próxima ao caso.                         
                                                                                
  O objetivo é permitir busca direta tanto por RN-004 quanto por CA-009, por    
  exemplo.                                                                      
                                                                                
  8. Corrija a afirmação sobre reprodução do build.                             
                                                                                
  - Compilar exige JDK 21 e Maven.                                              
  - Executar o JAR pronto exige Java 21.                                        
  - Na primeira compilação, Maven pode precisar baixar dependências.            
  - Não afirme que somente o JDK reproduz o build.                              
                                                                                
  9. Acrescente aos riscos:                                                     
                                                                                
  - teste integral não ser descoberto pelo Maven por nomenclatura inadequada;   
  - escrita direta corromper ou truncar --output;                               
  - categoria desconhecida ser descartada antes de RN-007.                      
                                                                                
  Após editar, faça somente verificações de leitura:                            
                                                                                
  - git diff --check -- specs/001-motor-reembolso/plan.md                       
  - git diff -- specs/001-motor-reembolso/plan.md                               
  - git status --short                                                          
  - confirme que “onze valores” não existe;                                     
  - confirme que `ExemploCompletoIT` não existe;                                
  - confirme que `ExemploCompletoTest` existe;                                  
  - confirme que `tests/java` e `tests/resources` estão documentados;           
  - confirme cobertura textual de RN-001 a RN-018 e CA-001 a CA-023;            
  - confirme que categoria fora da política não vira nulo;                      
  - confirme a estratégia de escrita temporária e substituição final.           
                                                                                
  Ao final, informe:                                                            
                                                                                
  - correções aplicadas;                                                        
  - decisões DT finais;                                                         
  - estrutura de diretórios planejada;                                          
  - comando único de testes;                                                    
  - cobertura RN/CA;                                                            
  - arquivos modificados;                                                       
  - qualquer pendência encontrada.                                              
                                                                                
  Não altere a versão 1.0 do plan.md: ele ainda está em elaboração, sem export  
  e sem commit.                                                                 
  Não registre isso em DECISIONS.md, pois nenhuma regra da spec mudou.          
  Não faça git add.                                                             
  Não faça commit.                                                              
  Não faça /export ainda.                                                       
  Não implemente nada.                                                          

  Read 1 file (ctrl+o to expand)

● Agora vou aplicar as correções, uma por uma, com Edit.

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      119  | **Campos estruturalmente validados** | Um mapa/estrutura com
            os sete campos canônicos de 4.2, populado apenas para os camp
           os que passaram na validação de RN-002. Campo inválido fica au
           sente aqui — nenhuma coerção, nenhum valor padrão. |
      120  | **`valor_informado`** | O valor JSON bruto de `despesa.valor
           ` exatamente como recebido (número, texto, booleano, lista, ob
           jeto ou nulo), extraído do `JsonNode`. Nenhuma regra financeir
           a o consulta — existe só para reaparecer na saída (4.3). |
      121  | **`valor_normalizado`** | `BigDecimal` de escala 2, resultad
           o de RN-004. Nulo quando `despesa.valor` não é um número estru
           turalmente válido. |
      122 -| **Categoria normalizada** | Texto resultante de RN-005 (trim
          -, minúsculas, sem acento). Nula quando não calculável ou fora 
          -do vocabulário fechado. |                                     
      122 +| **Categoria normalizada** | Texto resultante de RN-005 (trim
          +, minúsculas, sem acento), produzido **sempre** que `despesa.c
          +ategoria` é estruturalmente válido — inclusive quando o result
          +ado é `coworking` ou qualquer outro valor fora da política. Nu
          +la **apenas** quando `despesa.categoria` é estruturalmente inv
          +álido, ausente ou nulo. RN-007 compara esse texto normalizado 
          +contra o conjunto fechado `alimentacao`/`transporte_urbano`/`h
          +ospedagem` e produz `CATEGORIA_FORA_POLITICA` quando não há co
          +rrespondência — a normalização nunca converte uma categoria de
          +sconhecida em nulo; se convertesse, RN-007 não teria texto alg
          +um para comparar. |                                           
      123  | **Acumulador de motivos** | Lista de `Motivo`, que só cresce
            ao longo do pipeline — nenhuma etapa remove um motivo já acum
           ulado por outra. Reordenada apenas no passo 10, conforme 8.3. 
           |
      124  | **`Motivo`** | Três campos, cada um representado por um tipo
            fechado (enumeração), não por texto livre espalhado pelo códi
           go: `codigo` (enumeração com os onze valores de 4.5 — `ITEM_TI
           PO_INVALIDO`, `CAMPO_AUSENTE`, ..., `TETO_HOSPEDAGEM_APLICADO`
           ), `regra` (enumeração `RN_001`..`RN_018`, cada valor carregan
           do o texto canônico `"RN-NNN"`), `campo` (enumeração dos sete 
           nomes canônicos de 4.2, cada valor carregando o texto canônico
            `"despesa.<campo>"`, mais a possibilidade de nulo). A seriali
           zação de cada enum para o texto exigido pela spec é feita num 
           único ponto por enum — não há string `"RN-004"` ou `"despesa.v
           alor"` repetida em vários lugares do código. |
      125  | **Decisão final** | Enumeração com os quatro valores de 4.4 
           (`INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO`, `NAO
           _REEMBOLSADO_TETO_ESGOTADO`, `RECUSADO`). |

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      121  | **`valor_normalizado`** | `BigDecimal` de escala 2, resultad
           o de RN-004. Nulo quando `despesa.valor` não é um número estru
           turalmente válido. |
      122  | **Categoria normalizada** | Texto resultante de RN-005 (trim
           , minúsculas, sem acento), produzido **sempre** que `despesa.c
           ategoria` é estruturalmente válido — inclusive quando o result
           ado é `coworking` ou qualquer outro valor fora da política. Nu
           la **apenas** quando `despesa.categoria` é estruturalmente inv
           álido, ausente ou nulo. RN-007 compara esse texto normalizado 
           contra o conjunto fechado `alimentacao`/`transporte_urbano`/`h
           ospedagem` e produz `CATEGORIA_FORA_POLITICA` quando não há co
           rrespondência — a normalização nunca converte uma categoria de
           sconhecida em nulo; se convertesse, RN-007 não teria texto alg
           um para comparar. |
      123  | **Acumulador de motivos** | Lista de `Motivo`, que só cresce
            ao longo do pipeline — nenhuma etapa remove um motivo já acum
           ulado por outra. Reordenada apenas no passo 10, conforme 8.3. 
           |
      124 -| **`Motivo`** | Três campos, cada um representado por um tipo
          - fechado (enumeração), não por texto livre espalhado pelo códi
          -go: `codigo` (enumeração com os onze valores de 4.5 — `ITEM_TI
          -PO_INVALIDO`, `CAMPO_AUSENTE`, ..., `TETO_HOSPEDAGEM_APLICADO`
          -), `regra` (enumeração `RN_001`..`RN_018`, cada valor carregan
          -do o texto canônico `"RN-NNN"`), `campo` (enumeração dos sete 
          -nomes canônicos de 4.2, cada valor carregando o texto canônico
          - `"despesa.<campo>"`, mais a possibilidade de nulo). A seriali
          -zação de cada enum para o texto exigido pela spec é feita num 
          -único ponto por enum — não há string `"RN-004"` ou `"despesa.v
          -alor"` repetida em vários lugares do código. |                
      124 +| **`Motivo`** | Três campos, cada um representado por um tipo
          + fechado (enumeração), não por texto livre espalhado pelo códi
          +go: `codigo` (enumeração com os treze valores de 4.5 — `ITEM_T
          +IPO_INVALIDO`, `CAMPO_AUSENTE`, `CAMPO_TIPO_INVALIDO`, `CAMPO_
          +FORMATO_INVALIDO`, `ID_DUPLICADO`, `VALOR_NAO_POSITIVO`, `CATE
          +GORIA_FORA_POLITICA`, `FORA_COMPETENCIA`, `NOTA_FISCAL_AUSENTE
          +`, `DUPLICIDADE`, `TETO_DIARIO_APLICADO`, `TETO_DIARIO_ESGOTAD
          +O`, `TETO_HOSPEDAGEM_APLICADO`), `regra` (enumeração `RN_001`.
          +.`RN_018`, cada valor carregando o texto canônico `"RN-NNN"`),
          + `campo` (enumeração dos sete nomes canônicos de 4.2, cada val
          +or carregando o texto canônico `"despesa.<campo>"`, mais a pos
          +sibilidade de nulo). A serialização de cada enum para o texto 
          +exigido pela spec é feita num único ponto por enum — não há st
          +ring `"RN-004"` ou `"despesa.valor"` repetida em vários lugare
          +s do código. |                                                
      125  | **Decisão final** | Enumeração com os quatro valores de 4.4 
           (`INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO`, `NAO
           _REEMBOLSADO_TETO_ESGOTADO`, `RECUSADO`). |
      126  | **`valor_reembolsavel`** | `BigDecimal` de escala 2, sempre 
           `0,00` para item recusado ou esgotado. |
      127  | **Resultado por item** | Agrega `indiceEntrada`, `id` (ou nu
           lo), `valor_informado`, `valor_normalizado`, `valor_reembolsav
           el`, decisão final e a lista de motivos já ordenada conforme 8
           .3. |
● Agora a garantia de escrita atômica no contrato da CLI (§3):

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 2 lines, removed 2 lines
      103  
      104  - uma mensagem curta em **texto simples** é escrita em **stder
           r** — não em stdout, e não como JSON estruturado. Não existe v
           ocabulário JSON de erro nesta versão porque a spec não define 
           um; inventar um agora seria vazar decisão de produto para o pl
           ano sem base na spec.
      105  - nada é escrito em stdout;
      106 -- o arquivo indicado em `--output` **não é criado nem sobrescr
          -ito**.                                                        
      106 +- o arquivo indicado em `--output` **não é criado nem sobrescr
          +ito** — inclusive quando já existe um resultado anterior nesse
          + caminho, ele permanece intacto.                              
      107  
      108 -**A validação do envelope acontece antes de abrir ou sobrescre
          -ver o arquivo de saída.** Isso é o que garante a regra acima: 
          -o leitor primeiro faz o parsing sintático (falha → código `2`)
          - e o validador de envelope roda antes de qualquer tentativa de
          - abertura do caminho de `--output` (falha → código `3`). Só de
          -pois de o envelope ser confirmado válido o processo abre o arq
          -uivo de saída para escrita. Um erro de escrita nesse momento (
          -caminho não gravável, disco cheio) é infraestrutura, não regra
          - de negócio — cai no código `2`.                              
      108 +**A validação do envelope acontece antes de qualquer escrita n
          +o caminho de `--output`.** O leitor primeiro faz o parsing sin
          +tático (falha → código `2`) e o validador de envelope roda em 
          +seguida (falha → código `3`), ambos antes de o processo tocar 
          +no destino. Só depois de o envelope ser confirmado válido o re
          +sultado completo é serializado para um **arquivo temporário no
          + mesmo diretório do destino**; o destino em si só é tocado no 
          +passo final, por substituição atômica (ver DT-010). Uma falha 
          +nesse passo final — caminho não gravável, disco cheio — é infr
          +aestrutura, não regra de negócio, e também cai no código `2`, 
          +com o destino anterior preservado intacto.                    
      109  
      110  ---
      111  

● Agora o DT-010 (escrita atômica) e o ajuste do DT-009 (nomenclatura de
  testes):

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 10 lines, removed 3 lines
      229  ### DT-009 — Estratégia de testes em três níveis
      230  
      231  **Contexto:** a rubrica avalia rastreabilidade `spec → tasks →
            commits → testes`; cada RN e cada CA precisa de destino verif
           icável.
      232 -**Decisão:** testes unitários por regra (maioria), poucos test
          -es de integração de pipeline completo (o arquivo de exemplo e 
          -fixtures adicionais), e um teste de contrato/CLI cobrindo códi
          -gos de saída e comportamento de arquivo. Nomenclatura de class
          -e/método referenciando o `RN-NNN` correspondente.             
      233 -**Alternativa descartada:** cobertura só por teste de integraç
          -ão ponta a ponta — esconderia qual regra especificamente falho
          -u e dificultaria o grep de rastreabilidade que a rubrica valor
          -iza.                                                          
      234 -**Consequência:** qualquer regra da spec é localizável no códi
          -go de teste por busca textual do próprio identificador `RN-NNN
          -`; ver matriz completa em §9.                                 
      232 +**Decisão:** testes unitários por regra (maioria), poucos test
          +es de integração de pipeline completo (o arquivo de exemplo e 
          +fixtures adicionais), e um teste de contrato/CLI cobrindo códi
          +gos de saída e comportamento de arquivo. Nomenclatura de class
          +e/método referenciando o `RN-NNN` e o `CA-NNN` correspondentes
          +. Todo teste — inclusive o de integração (`ExemploCompletoTest
          +`) e os de contrato/CLI (`CliContratoTest`, `EscritaAtomicaSai
          +daTest`) — usa o sufixo `*Test`, reconhecido pelo Maven Surefi
          +re por padrão, para que `mvn test` execute a suíte inteira num
          + único comando.                                               
      233 +**Alternativa descartada:** cobertura só por teste de integraç
          +ão ponta a ponta — esconderia qual regra especificamente falho
          +u e dificultaria o grep de rastreabilidade que a rubrica valor
          +iza. Nomear o teste de integração com o sufixo `*IT` (convençã
          +o do Maven Failsafe) — exigiria configurar e invocar um plugin
          + de build adicional (`mvn verify`) só por causa de nomenclatur
          +a, e o teste deixaria de rodar em `mvn test`, contrariando a s
          +implicidade pedida.                                           
      234 +**Consequência:** qualquer regra da spec é localizável no códi
          +go de teste por busca textual do próprio identificador `RN-NNN
          +` ou `CA-NNN`; `mvn test` sozinho executa toda a suíte, sem ex
          +igir um segundo comando ou plugin para os testes de integração
          +/CLI; ver matriz completa em §9.                              
      235  
      236 +### DT-010 — Escrita atômica do arquivo de saída              
      237 +                                                              
      238 +**Contexto:** a garantia de que `--output` nunca é criado nem 
          +sobrescrito nos códigos `2` e `3` só é verdadeira se a escrita
          + do resultado nunca tocar o destino antes de o resultado estar
          + completo — escrever progressivamente e diretamente no destino
          + deixaria um arquivo truncado no caminho oficial caso o proces
          +so falhe no meio da escrita.                                  
      239 +**Decisão:** serializar o resultado completo para um arquivo t
          +emporário no mesmo diretório do destino (mesmo sistema de arqu
          +ivos, condição para substituição atômica); fechar e concluir t
          +otalmente a escrita do temporário; só então mover/substituir o
          + destino, preferindo `Files.move(temp, destino, StandardCopyOp
          +tion.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)`. Se o 
          +movimento/substituição falhar, o processo retorna código `2` e
          + o destino anterior permanece intacto; o arquivo temporário é 
          +removido quando possível. O arquivo de destino nunca é aberto 
          +diretamente para escrever o JSON progressivamente.            
      240 +**Alternativa descartada:** abrir `--output` diretamente e esc
          +rever o JSON incrementalmente — mais simples de codar, mas dei
          +xa uma janela em que uma falha no meio da escrita (processo in
          +terrompido, disco cheio) produz um arquivo parcialmente escrit
          +o exatamente no caminho que o resto do contrato promete preser
          +var intacto.                                                  
      241 +**Consequência:** a garantia de não corromper `--output` passa
          + a ser verdadeira por construção, não por sorte de nunca falha
          +r no meio da escrita; o custo é uma etapa extra de escrita-e-m
          +ovimentação em vez de uma escrita única, e a exigência de que 
          +o temporário fique no mesmo diretório do destino para que `ATO
          +MIC_MOVE` seja viável na maioria dos sistemas de arquivos.    
      242 +                                                              
      243  ---
      244  
      245  ## 9. Estratégia de testes

● Agora atualizo os bullets introdutórios de §9 (nomenclatura com CA, fixture,
  comando único):

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 4 lines, removed 2 lines
      244  
      245  ## 9. Estratégia de testes
      246  
      247 -- **Nível e proporção:** majoritariamente unitário (uma regra 
          -de negócio = um grupo de testes isolado, sem I/O real); um tes
          -te de integração de pipeline completo contra `exemplos/despesa
          -s-exemplo.json`; um teste de contrato/CLI cobrindo códigos de 
          -saída e comportamento de arquivo.                             
      247 +- **Nível e proporção:** majoritariamente unitário (uma regra 
          +de negócio = um grupo de testes isolado, sem I/O real); um tes
          +te de integração de pipeline completo contra `exemplos/despesa
          +s-exemplo.json`; testes de contrato/CLI cobrindo códigos de sa
          +ída, comportamento de stdout/stderr e a escrita atômica de `--
          +output`.                                                      
      248  - **Cada `RN-NNN` tem teste?** Garantido pela matriz de rastre
           abilidade abaixo — todo RN-001 a RN-018 aparece em pelo menos 
           uma linha; conferência manual cruzada com a spec ao fechar o D
           ia 1, antes do envelope do Dia 2.
      249  - **Casos de borda da seção 7:** cobertos pelos mesmos grupos 
           de teste da matriz, via os `CA-NNN` correspondentes, que deriv
           am diretamente da tabela de casos de borda.
      250 -- **Nomenclatura:** classe/grupo nomeado pelo identificador da
          - regra (`RN004NormalizacaoMonetariaTest`), método nomeado pelo
          - cenário e resultado esperado (`rn004_100_005_arredondaParaCim
          -a_100_01()`), de modo que o `RN-NNN` ou `CA-NNN` seja localizá
          -vel por busca textual direta entre spec e teste.              
      250 +- **Nomenclatura:** classe/grupo nomeado pelo identificador da
          + regra (`RN004NormalizacaoMonetariaTest`); método nomeado pelo
          + cenário e resultado esperado, carregando também o `CA-NNN` ap
          +licável — por nome de método (`rn004_ca009_100_005_arredondaPa
          +raCima_100_01()`), `@DisplayName` (`"RN-004 / CA-009 — 100.005
          + arredonda para 100,01"`) ou comentário imediatamente acima do
          + caso. O objetivo é permitir busca textual direta tanto por `R
          +N-004` quanto por `CA-009` e chegar ao mesmo teste.           
      251 +- **Fixture de saída esperada:** o fixture usado por `ExemploC
          +ompletoTest` é **escrito e revisado manualmente** a partir do 
          +schema completo das seções 4.3 a 4.5 da spec — nunca gerado pe
          +lo próprio motor em teste, o que tornaria o teste circular. Us
          +a a tabela 4.7 como fonte de decisões e valores, e RN-017 mais
          + a ordem de 8.3 para montar o objeto completo de cada motivo (
          +`codigo`, `regra`, `campo`) — a tabela 4.7 é uma representação
          + abreviada (só o `codigo`) e não basta sozinha para montar o f
          +ixture. O fixture contém metadados do envelope, `valor_informa
          +do`, `valor_normalizado`, decisão, motivos completos e `total_
          +reembolsavel`. A comparação é **estrutural** (JSON contra JSON
          +, campo a campo), nunca textual — não depende de espaços, inde
          +ntação ou ordem de chaves na serialização.                    
      252 +- **Comando único de execução:** `mvn test` executa **todos** 
          +os testes planejados desta matriz, inclusive o de integração (
          +`ExemploCompletoTest`) e os de contrato/CLI (`CliContratoTest`
          +, `EscritaAtomicaSaidaTest`) — todos seguem o sufixo `*Test`, 
          +reconhecido pelo Maven Surefire por padrão, em vez do sufixo `
          +*IT` (que exigiria o Maven Failsafe e uma fase de build adicio
          +nal só por causa de nomenclatura). `mvn package` executa esses
          + mesmos testes antes de gerar o JAR, porque a fase `test` prec
          +ede `package` no ciclo de vida padrão do Maven; pular essa eta
          +pa exige a flag explícita e não recomendada `-DskipTests`.    
      253  
      254  ### Matriz de rastreabilidade
      255  

● Agora renomeio ExemploCompletoIT na matriz e adiciono a linha de escrita
  atômica:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 3 lines, removed 2 lines
      275  | RN-017 · CA-002 | `ComposicaoSaidaTest` — toda posição produ
           z um registro, ordem da entrada preservada, nenhum item desapa
           rece | Unidade |
      276  | — (ordem de apresentação, 8.3/8.4) | `OrdemMotivosTest` — os
            dois exemplos normativos de 8.4 (três motivos simultâneos; ex
           clusão de nota por valor não positivo) | Unidade |
      277  | RN-018 · CA-001 · CA-003 | `TotalPeriodoTest` — soma dos `va
           lor_reembolsavel` apresentados igual ao total | Unidade/Integr
           ação |
      278 -| CA-001 · CA-002 · CA-003 (integral) | `ExemploCompletoIT` — 
          -os 14 itens de `exemplos/despesas-exemplo.json` processados de
          - ponta a ponta, comparados campo a campo contra um fixture de 
          -saída esperada derivado da tabela 4.7; `total_reembolsavel` = 
          -R$ 585,43 | Integração |                                      
      279 -| — (contrato de execução) | `CliContratoTest` — código `0` em
          - sucesso; código `2` para argumento ausente/arquivo inexistent
          -e/JSON sintaticamente inválido/falha de escrita; código `3` pa
          -ra envelope inválido; mensagem em stderr, nada em stdout, `--o
          -utput` não criado nem sobrescrito nos códigos `2` e `3` | Cont
          -rato/CLI |                                                    
      278 +| CA-001 · CA-002 · CA-003 (integral) | `ExemploCompletoTest` 
          +— os 14 itens de `exemplos/despesas-exemplo.json` processados 
          +de ponta a ponta, comparados estruturalmente contra o fixture 
          +esperado descrito acima (escrito à mão a partir de 4.3–4.5, nã
          +o gerado pelo motor); `total_reembolsavel` = R$ 585,43 | Integ
          +ração (executado por `mvn test`) |                            
      279 +| — (contrato de execução) | `CliContratoTest` — código `0` em
          + sucesso; código `2` para argumento ausente/arquivo inexistent
          +e/JSON sintaticamente inválido/falha de escrita; código `3` pa
          +ra envelope inválido; mensagem em stderr, nada em stdout | Con
          +trato/CLI |                                                   
      280 +| — (escrita atômica de `--output`, DT-010) | `EscritaAtomicaS
          +aidaTest` — envelope inválido não altera um arquivo preexisten
          +te em `--output`; JSON sintaticamente inválido não altera um a
          +rquivo preexistente; falha simulada antes da substituição fina
          +l não altera um arquivo preexistente; sucesso substitui o dest
          +ino pelo resultado completo; nenhum arquivo temporário/parcial
          + permanece no caminho oficial de `--output` após qualquer cená
          +rio | Contrato/CLI |                                          
      281  
      282  Todo identificador de RN-001 a RN-018 e de CA-001 a CA-023 apa
           rece em pelo menos uma linha desta matriz.
      283  

● Agora corrijo a afirmação de reprodução do build (DT-001) e adiciono os três
  novos riscos:

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 1 line, removed 1 line
      175  **Contexto:** prazo de dois dias, desenvolvedor com familiarid
           ade declarada em Java, ambiente desta máquina já inspecionado 
           (Java 21.0.2 e Maven 3.9.6 funcionando; Python e Node ausentes
           ).
      176  **Decisão:** Java 21 como linguagem única do projeto.
      177  **Alternativa descartada:** Python/Node — não instalados nesta
            máquina, custariam tempo de setup sob prazo curto; Kotlin — m
           esma JVM, mas sintaxe nova sem necessidade.
      178 -**Consequência:** zero passo de instalação para rodar ou compi
          -lar; qualquer máquina com JDK 21+ reproduz o build sem ajuste.
      178 +**Consequência:** compilar o projeto exige JDK 21 e Maven 3.9+
          + instalados na máquina; executar o JAR já compilado exige apen
          +as um Java 21 (JRE ou JDK), porque o fat jar já contém as depe
          +ndências. Na primeira compilação, o Maven pode precisar baixar
          + dependências (Jackson, JUnit) de um repositório remoto — exig
          +e acesso à rede nessa primeira vez; builds subsequentes reapro
          +veitam o cache local do Maven.                                
      179  
      180  ### DT-002 — Empacotamento em JAR único via Maven Shade Plugin
      181  

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 3 lines
      292  | Regra parando no primeiro motivo encontrado (short-circuit i
           ndevido) | Média | `OrdemMotivosTest` e os testes de RN-002 co
           m múltiplos motivos (`CA-021`, `CA-023`) capturam isso diretam
           ente. |
      293  | Excesso de arquitetura (motor de regras genérico, configuraç
           ão externa) sob tentação de "preparar para o Dia 2" | Baixa ma
           s real — o FAQ do desafio avisa explicitamente contra isso | M
           anter `PoliticaReembolso` simples (§5) e a arquitetura de pipe
           line linear (§2); qualquer generalização é adicionada só quand
           o um requisito real a exigir, nunca antes. |
      294  | Mudança de requisito do Dia 2 exigir tocar código de I/O alé
           m do núcleo | Desconhecida — não antecipada nesta versão | A f
           ronteira núcleo/CLI (§2) já isola regra de negócio de I/O; se 
           a mudança for de regra, o núcleo absorve; se for de contrato d
           e execução, o CLI absorve. Nenhum conteúdo do Dia 2 é suposto 
           aqui. |
      295 +| Teste de integração não ser descoberto pelo Maven por nomenc
          +latura inadequada (ex. sufixo `*IT` sem o Maven Failsafe confi
          +gurado) | Média — foi um problema real encontrado na auditoria
          + deste plano | Todo teste, inclusive integração e CLI, usa o s
          +ufixo `*Test` (DT-009/DT-010); `mvn test` sozinho é o comando 
          +de verificação antes de qualquer commit, nunca um plugin adici
          +onal só por nomenclatura. |                                   
      296 +| Escrita direta e progressiva em `--output` corromper ou trun
          +car o arquivo em caso de falha no meio da serialização | Média
          + | Escrever sempre em arquivo temporário no mesmo diretório do
          + destino e mover atomicamente (DT-010); o destino nunca é aber
          +to diretamente para escrita incremental. |                    
      297 +| Categoria estruturalmente válida, mas fora do vocabulário fe
          +chado, ser descartada (virar nula) antes de RN-007 conseguir c
          +ompará-la | Média — foi um erro real encontrado na auditoria d
          +este plano | Modelo corrigido em §4: a categoria normalizada é
          + sempre produzida quando o campo é estruturalmente válido, inc
          +lusive quando o resultado é `coworking` ou qualquer outro valo
          +r fora da política; só RN-007 decide `CATEGORIA_FORA_POLITICA`
          +. |                                                           

● Agora adiciono a seção de estrutura física do projeto ao final do arquivo:

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\plan.md)
  ⎿  Added 25 lines
      295  | Teste de integração não ser descoberto pelo Maven por nomenc
           latura inadequada (ex. sufixo `*IT` sem o Maven Failsafe confi
           gurado) | Média — foi um problema real encontrado na auditoria
            deste plano | Todo teste, inclusive integração e CLI, usa o s
           ufixo `*Test` (DT-009/DT-010); `mvn test` sozinho é o comando 
           de verificação antes de qualquer commit, nunca um plugin adici
           onal só por nomenclatura. |
      296  | Escrita direta e progressiva em `--output` corromper ou trun
           car o arquivo em caso de falha no meio da serialização | Média
            | Escrever sempre em arquivo temporário no mesmo diretório do
            destino e mover atomicamente (DT-010); o destino nunca é aber
           to diretamente para escrita incremental. |
      297  | Categoria estruturalmente válida, mas fora do vocabulário fe
           chado, ser descartada (virar nula) antes de RN-007 conseguir c
           ompará-la | Média — foi um erro real encontrado na auditoria d
           este plano | Modelo corrigido em §4: a categoria normalizada é
            sempre produzida quando o campo é estruturalmente válido, inc
           lusive quando o resultado é `coworking` ou qualquer outro valo
           r fora da política; só RN-007 decide `CATEGORIA_FORA_POLITICA`
           . |
      298 +                                                              
      299 +---                                                           
      300 +                                                              
      301 +## 11. Estrutura física do projeto                            
      302 +                                                              
      303 +Documentada aqui para respeitar a estrutura de entrega exigida
          + pelo `DESAFIO.md` (pasta de nível superior `tests/`, e não a 
          +convenção padrão do Maven `src/test/java`). **Os diretórios nã
          +o são criados nesta etapa** — isto é só planejamento.         
      304 +                                                              
      305 +```                                                           
      306 +sdd-desafio/                                                  
      307 +├── pom.xml                                                   
      308 +├── src/                                                      
      309 +│   └── main/                                                 
      310 +│       └── java/...        # código de produção              
      311 +├── tests/                                                    
      312 +│   ├── java/...             # testes Java (JUnit 5) — RN*Test
          +, CA*Test, ExemploCompletoTest, CliContratoTest, EscritaAtomic
          +aSaidaTest                                                    
      313 +│   └── resources/...        # fixtures de teste, incluindo o 
          +fixture esperado do exemplo completo                          
      314 +└── target/                  # artefatos gerados pelo Maven — 
          +nunca versionado, nunca escrito à mão                         
      315 +```                                                           
      316 +                                                              
      317 +Como `tests/` não é o caminho padrão reconhecido pelo Maven, o
          + `pom.xml` precisa apontar explicitamente para ele:           
      318 +                                                              
      319 +- `<build><testSourceDirectory>tests/java</testSourceDirectory
          +></build>` — direciona o compilador de testes para `tests/java
          +` em vez do padrão `src/test/java`.                           
      320 +- `<build><testResources><testResource><directory>tests/resour
          +ces</directory></testResource></testResources></build>` — dire
          +ciona os recursos de teste (fixtures) para `tests/resources`. 
      321 +                                                              
      322 +Com essa configuração, `mvn test` continua funcionando exatame
          +nte como esperado — compila e executa tudo em `tests/java`, co
          +m `tests/resources` no classpath de teste — sem exigir que os 
          +testes fiquem em `src/test/java`. Essa é também a razão pela q
          +ual nenhum teste desta spec depende do caminho padrão do Maven
          + para ser descoberto: a descoberta depende só do sufixo `*Test
          +` (via Surefire) e do `testSourceDirectory` configurado, não d
          +a localização convencional.                                   

● Agora as verificações de leitura solicitadas.

  Searched for 2 patterns, ran 4 shell commands (ctrl+o to expand)

● Correções aplicadas e verificadas. Resumo:

  Correções aplicadas:
  1. Categoria normalizada (§4, modelo de dados): corrigido — agora produz
  sempre o texto normalizado quando despesa.categoria é estruturalmente válido
  (nula só quando ausente/nula/inválida), deixando RN-007 comparar contra o
  vocabulário fechado. Nenhuma outra menção incorreta encontrada na arquitetura
  ou nas DTs.
  2. Contagem de MotivoCodigo: "onze valores" → "treze valores", com os 13
  códigos listados explicitamente.
  3. Escrita atômica de --output: nova DT-010, contrato da CLI (§3) reescrito
  para descrever arquivo temporário no mesmo diretório + Files.move com
  ATOMIC_MOVE/REPLACE_EXISTING, falha preservando o destino anterior. Testes
  novos: linha EscritaAtomicaSaidaTest na matriz.
  4. mvn test/mvn package: ExemploCompletoIT renomeado para ExemploCompletoTest;
  DT-009 e §9 agora registram explicitamente que todo teste usa sufixo *Test
  (Surefire), sem Failsafe, e que mvn package roda os testes antes do JAR
  (exceto com -DskipTests, não recomendado).
  5. Estrutura física: nova §11, com src/main/java, tests/java, tests/resources,
  target/, e a configuração de testSourceDirectory/testResources no pom.xml —
  documentada, nada criado em disco.
  6. Origem do fixture: bullet novo em §9 — escrito/revisado à mão a partir de
  4.3–4.5, usa 4.7 só para decisões/valores, usa RN-017+8.3 para os objetos
  completos de motivo, comparação estrutural (JSON vs JSON), nunca gerado pelo
  motor.
  7. Rastreabilidade RN+CA nos testes: bullet "Nomenclatura" reescrito exigindo
  RN-NNN e CA-NNN juntos por nome de método/@DisplayName/comentário.
  8. Reprodução do build: DT-001 corrigida — compilar exige JDK 21 + Maven;
  rodar o jar exige só Java 21; primeira build pode precisar de rede para baixar
  dependências.
  9. Três riscos novos em §10: teste íntegro não descoberto por nomenclatura,
  escrita direta corrompendo --output, categoria desconhecida virando nula antes
  de RN-007.

  Decisões DT finais: 10 — DT-001 a DT-009 (ajustadas onde necessário) + DT-010
  (escrita atômica), novo.

  Estrutura de diretórios planejada: pom.xml, src/main/java, tests/java,
  tests/resources, target/ — com testSourceDirectory/testResources apontando
  para tests/.

  Comando único de testes: mvn test (todos os testes, inclusive integração e
  CLI); mvn package roda os mesmos testes antes de empacotar.

  Cobertura RN/CA: confirmada por contagem — RN-001 a RN-018 todos com ≥1
  ocorrência; CA-001 a CA-023 todos com ≥1 ocorrência.

  Arquivos modificados: só specs/001-motor-reembolso/plan.md (287 inserções, 34
  remoções nesta rodada). spec.md, DECISIONS.md e a sessão em docs/ seguem com
  as modificações pré-existentes que já estavam lá — não toquei nelas, e não
  registrei nada em DECISIONS.md (nenhuma regra da spec mudou, só o plano).

  Pendências: nenhuma bloqueante. git diff --check só acusa o aviso padrão de
  LF→CRLF do Git no Windows. ExemploCompletoIT confirmado ausente;
  ExemploCompletoTest, tests/java e tests/resources confirmados presentes;
  nenhuma reincidência de "fora do vocabulário fechado" como afirmação de
  comportamento (as duas ocorrências restantes são contexto correto: uma sobre o
  enum de motivos, outra descrevendo o risco corrigido). Nenhum commit, git 
  add, código, teste ou tasks.md foi criado.

✻ Sautéed for 4m 5s

※ recap: You're planning the reembolso engine's spec-driven build: spec.md is approved at 1.1, and plan.md just got corrected per an audit (categoria bug, atomic output writes, test naming, directory layout). Next: get plan.md approved so tasks.md can be written. (disable recaps in /config)