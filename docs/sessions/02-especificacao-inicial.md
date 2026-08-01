 ▐▛███▜▌   Claude Code v2.1.220
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> > Esta é uma nova sessão de auditoria da especificação já existente. Não      
  modifique nenhum arquivo ainda.                                               
                                                                                
                                                                                
    a, ordem de processamento, ordem dos motivos, matriz de dependências e      
  lista fechada de exclusões.                                                   
    8. Procure contradições, comportamentos indefinidos, vazamento de solução   
  técnica, placeholders e critérios                                             

  Searched for 6 patterns, read 6 files, ran 1 shell command (ctrl+o to expand)

● Fiz uma leitura completa de spec.md (709 linhas) e cruzei com DECISIONS.md,
  plan.md, tasks.md, DESAFIO.md e RUBRICA.md para contexto — nenhum arquivo foi
  modificado. Achei poucos problemas reais; a spec está incomum bem amarrada (o
  total de R$ 585,43 bate exatamente somando os 14 itens da tabela 4.7, e as
  tabelas de rastreabilidade da seção 9 cobrem RN-001–018, AMB-001–018 e
  CA-001–022 sem lacuna). Os achados abaixo são os pontos que sobreviveram à
  conferência.

  Matriz de dependências (8.2) — incompleta

  spec.md:584-598 lista os campos dos quais cada regra depende, mas RN-003 
  (unicidade de despesa.id) não aparece na tabela, apesar de depender claramente
  de despesa.id. A dependência existe — está declarada separadamente em 8.4,
  item 6 (spec.md:622: "ID estruturalmente inválido não participa da verificação
  de IDs repetidos") — mas a tabela que se propõe a ser a matriz de
  dependências ("Cada regra depende dos campos abaixo", spec.md:586) não é
  exaustiva. Comportamento não muda, mas quem consultar só a 8.2 fica sem essa
  linha.

  Classificação CAMPO_TIPO_INVALIDO × CAMPO_FORMATO_INVALIDO — sem regra geral

  A spec distingue os dois códigos (spec.md:170-172) mas nunca define o critério
  geral de quando um defeito é "tipo" e quando é "formato" — só dá dois
  exemplos pontuais: data: "31/07/2026" → formato inválido; valor: "72,50" →
  tipo inválido (spec.md:256). Fica sem resposta explícita, por exemplo:
  despesa.id: "" (tipo certo — texto — mas viola "não vazio") é tipo ou formato
  inválido? E um despesa.categoria recebido como número? Dois implementadores
  that concordam em tudo mais podem divergir exatamente nesse código, que é
  visível na saída.

  valor_informado — tipo mal definido para despesa.valor estruturalmente 
  inválido

  spec.md:122 declara o tipo de valor_informado como "número, conteúdo recebido
  ou nulo" — uma união estranha que não deixa claro o que acontece quando
  despesa.valor chega com tipo errado (ex.: "72,50" como string). A tabela de
  casos de borda (spec.md:552) só afirma que valor_normalizado fica nulo nesse
  caso; não há exemplo, nem em 4.6/4.7 nem em 7, mostrando o valor de
  valor_informado quando o campo bruto não é numérico. Preserva o conteúdo
  recebido mesmo não sendo número, ou vira nulo por não ser "número"? Os dois
  são leituras plausíveis do texto atual.

  Achados menores (cosméticos, não bloqueiam implementação)

  - spec.md:84-86: a coluna "Vazio permitido" traz "Não" para despesa.valor
  (número) e despesa.tem_nota_fiscal (booleano), onde o conceito de "vazio" não
  se aplica de fato — provavelmente decorre de aplicar a coluna uniformemente às
  sete linhas.
  - spec.md:626: a exclusão "Valor não positivo torna a regra de nota fiscal não
  aplicável" é, na prática, uma consequência automática da própria condição de
  RN-009 (valor > 100 já exige valor > 0) — não é uma exclusão adicional, é
  redundante com a regra que ela descreve. Não é errado, só é dispensável.

  O que não encontrei

  Nenhum vazamento de solução técnica em spec.md (busquei por termos de
  linguagem/lib/estrutura — zero ocorrências), nenhum placeholder <...>
  remanescente, nenhuma contradição real na ordem de processamento (8.1) nem na
  ordem de apresentação de motivos (8.3) — o exemplo completo de 14 itens bate
  com ambas. plan.md e tasks.md seguem como templates vazios, o que é esperado
  nesta fase (ainda não é código, é auditoria da spec), mas registro porque a
  cadeia de rastreabilidade da Rubrica (critério 2) depende deles existirem
  antes da implementação.

  Sua mensagem parece ter sido cortada no meio de uma lista numerada — se havia
  mais itens específicos de checklist (além de ordem/matriz/exclusões/contradiçõ
  es/indefinição/vazamento/placeholder/critérios, que já cobri), me diga o que
  faltou.

✻ Churned for 5m 35s

> Minha mensagem anterior era >                                                 

● Parece que a mensagem cortou de novo — só chegou "Minha mensagem anterior era
  >" sem o conteúdo depois do >. Pode colar o texto completo novamente? Se for
  uma lista numerada longa, vale colar em partes menores para garantir que
  chegue inteira.

✻ Baked for 5s

> Esta é uma nova sessão de auditoria da especificação já existente. Não        
  modifique nenhum arquivo ainda.                                               
                                                                                
                                                                                
                                                                                
  Leia integralmente, nesta ordem:                                              
                                                                                
                                                                                
                                                                                
  1\. CLAUDE.md                                                                 
                                                                                
  2\. DESAFIO.md                                                                
                                                                                
  3\. RUBRICA.md                                                                
                                                                                
  4\. FAQ.md                                                                    
                                                                                
  5\. exemplos/despesas-exemplo.json                                            
                                                                                
  6\. template/specs/001-motor-reembolso/spec.md                                
                                                                                
  7\. specs/001-motor-reembolso/spec.md                                         
                                                                                
  8\. specs/001-motor-reembolso/DECISIONS.md                                    
                                                                                
                                                                                
                                                                                
  Depois faça uma auditoria independente e detalhada:                           
                                                                                
                                                                                
                                                                                
  1\. Percorra novamente os 14 itens do JSON, na ordem da entrada.              
                                                                                
  2\. Recalcule o valor reembolsável de cada um somente com base na spec.       
                                                                                
  3\. Confirme ou refute o total esperado de R$ 585,43.                         
                                                                                
  4\. Confirme a existência e coerência de AMB-001 a AMB-018.                   
                                                                                
  5\. Confirme as 15 subdecisões, RN-001 a RN-018 e CA-001 a CA-022.            
                                                                                
  6\. Verifique a rastreabilidade política → ambiguidades → regras → critérios  
  de aceite.                                                                    
                                                                                
  7\. Verifique contrato de entrada, contrato de saída, ordem de processamento, 
  ordem dos motivos, matriz de dependências e lista fechada de exclusões.       
                                                                                
  8\. Procure contradições, comportamentos indefinidos, vazamento de solução    
  técnica, placeholders e critérios impossíveis de testar.                      
                                                                                
  9\. Verifique se spec.md e DECISIONS.md contam um histórico honesto e         
  compatível entre si.                                                          
                                                                                
  10\. Execute somente verificações de leitura, incluindo git status, git diff  
  --check e git diff limitado aos documentos.                                   
                                                                                
  11\. Informe explicitamente que esta é uma auditoria independente feita       
  depois da elaboração inicial dos documentos, sem fingir que esta sessão foi a 
  conversa original de elaboração.                                              
                                                                                
                                                                                
                                                                                
  Não altere arquivos, não execute git add e não faça commit.                   
                                                                                
                                                                                
                                                                                
  Ao final, apresente:                                                          
                                                                                
                                                                                
                                                                                
  \- resultado item a item;                                                     
                                                                                
  \- total recalculado;                                                         
                                                                                
  \- cobertura encontrada;                                                      
                                                                                
  \- problemas encontrados, classificados por gravidade;                        
                                                                                
  \- recomendação objetiva: aprovar ou corrigir antes de seguir para o plan.md. 

  Read 3 files, ran 1 shell command (ctrl+o to expand)
  ⎿  Loaded template\CLAUDE.md

● Concluí a auditoria independente. Segue o relatório conforme solicitado.

  Nota de transparência (item 11 do pedido): esta é uma sessão de auditoria
  posterior à elaboração da spec — não é a conversa original em que
  spec.md/DECISIONS.md foram escritos. Recomputei tudo a partir do zero, sem
  assumir que a tabela 4.7 da própria spec está correta.

  Leitura realizada (itens 1–8)

  CLAUDE.md, DESAFIO.md, RUBRICA.md, FAQ.md, exemplos/despesas-exemplo.json,
  template/specs/001-motor-reembolso/spec.md, specs/001-motor-reembolso/spec.md
  (íntegra, 709 linhas) e DECISIONS.md (íntegra) — todos lidos por completo,
  nesta ordem.

  Resultado item a item (recalculado direto do JSON contra as regras da spec, 
  ignorando a tabela 4.7 até o fim)

  Item: d-001
  Categoria normalizada: alimentacao
  Passos aplicados: teto diário 60 (grupo 07-03, junto com d-002) — 72,50
  excede,
  consome o saldo
  Decisão: PARCIAL
  Reembolsável: 60,00
  Motivos (na ordem de 8.3): TETO_DIARIO_APLICADO
  ────────────────────────────────────────
  Item: d-002
  Categoria normalizada: alimentacao
  Passos aplicados: saldo do grupo já esgotado por d-001
  Decisão: ESGOTADO
  Reembolsável: 0,00
  Motivos (na ordem de 8.3): TETO_DIARIO_ESGOTADO
  ────────────────────────────────────────
  Item: d-003
  Categoria normalizada: transporte_urbano
  Passos aplicados: 100,00 não é >100 → nota não exigida; teto 80 (sozinho no
  grupo 07-06, d-004 já saiu antes)
  Decisão: PARCIAL
  Reembolsável: 80,00
  Motivos (na ordem de 8.3): TETO_DIARIO_APLICADO
  ────────────────────────────────────────
  Item: d-004
  Categoria normalizada: transporte_urbano
  Passos aplicados: 100,01 >100, sem nota
  Decisão: RECUSADO
  Reembolsável: 0,00
  Motivos (na ordem de 8.3): NOTA_FISCAL_AUSENTE
  ────────────────────────────────────────
  Item: d-005
  Categoria normalizada: coworking
  Passos aplicados: categoria não bate com as três canônicas
  Decisão: RECUSADO
  Reembolsável: 0,00
  Motivos (na ordem de 8.3): CATEGORIA_FORA_POLITICA
  ────────────────────────────────────────
  Item: d-006
  Categoria normalizada: alimentacao
  Passos aplicados: 54,90 ≤ 60, único sobrevivente do grupo 07-09 (d-007 cai por

  duplicidade)
  Decisão: INTEGRAL
  Reembolsável: 54,90
  Motivos (na ordem de 8.3): —
  ────────────────────────────────────────
  Item: d-007
  Categoria normalizada: alimentacao
  Passos aplicados: idêntico a d-006 em
  data/categoria/valor/fornecedor/descrição; índice maior
  Decisão: RECUSADO
  Reembolsável: 0,00
  Motivos (na ordem de 8.3): DUPLICIDADE
  ────────────────────────────────────────
  Item: d-008
  Categoria normalizada: alimentacao
  Passos aplicados: data 2026-04-15 fora de [07-01, 07-31]
  Decisão: RECUSADO
  Reembolsável: 0,00
  Motivos (na ordem de 8.3): FORA_COMPETENCIA
  ────────────────────────────────────────
  Item: d-009
  Categoria normalizada: transporte_urbano
  Passos aplicados: valor -45,00 ≤ 0
  Decisão: RECUSADO
  Reembolsável: 0,00
  Motivos (na ordem de 8.3): VALOR_NAO_POSITIVO
  ────────────────────────────────────────
  Item: d-010
  Categoria normalizada: hospedagem
  Passos aplicados: 480,00 > teto individual 250
  Decisão: PARCIAL
  Reembolsável: 250,00
  Motivos (na ordem de 8.3): TETO_HOSPEDAGEM_APLICADO
  ────────────────────────────────────────
  Item: d-011
  Categoria normalizada: alimentacao
  Passos aplicados: 33.333 → 33,33 (terceira casa <5, arredonda para baixo); ≤60
  Decisão: INTEGRAL
  Reembolsável: 33,33
  Motivos (na ordem de 8.3): —
  ────────────────────────────────────────
  Item: d-012
  Categoria normalizada: alimentacao
  Passos aplicados: 47,20 ≤ 60, grupo próprio (07-18)
  Decisão: INTEGRAL
  Reembolsável: 47,20
  Motivos (na ordem de 8.3): —
  ────────────────────────────────────────
  Item: d-013
  Categoria normalizada: hospedagem
  Passos aplicados: 690,00 >100, sem nota — recusado antes de qualquer teto
  Decisão: RECUSADO
  Reembolsável: 0,00
  Motivos (na ordem de 8.3): NOTA_FISCAL_AUSENTE
  ────────────────────────────────────────
  Item: d-014
  Categoria normalizada: ALIMENTACAO→alimentacao
  Passos aplicados: 61,00 sozinho no grupo 07-31 (= periodo.fim, elegível),
  excede teto 60
  Decisão: PARCIAL
  Reembolsável: 60,00
  Motivos (na ordem de 8.3): TETO_DIARIO_APLICADO

  Total recalculado

  60,00 + 0,00 + 80,00 + 0,00 + 0,00 + 54,90 + 0,00 + 0,00 + 0,00 + 250,00 +
  33,33 + 47,20 + 0,00 + 60,00 = R$ 585,43

  Confirma exatamente o valor declarado na spec. Todas as 14 linhas recalculadas
  batem, campo a campo, com a tabela 4.7 — decisão, valor reembolsável e
  motivos idênticos, sem nenhuma divergência.

  Cobertura encontrada

  - AMB-001 a AMB-018: as 18 existem, cada uma com texto original, leitura
  ambígua, decisão, justificativa e regra afetada. Coerentes entre si (inclusive
  as correções documentadas em R-1/R-2 do DECISIONS.md estão de fato refletidas
  no texto final).
  - 15 subdecisões: confirmei as 15 citadas em DECISIONS.md — AMB-006/JANELA,
  AMB-008/FORMATO, AMB-008/LOCAL, AMB-009/FONTE, AMB-011/POPULACAO,
  AMB-012/CLASSIFICACAO, AMB-013/PISO, AMB-013/GATILHO, AMB-013/DUPLICIDADE,
  AMB-015/ESCOPO, AMB-015/CONTRATO, AMB-015/ENVELOPE, AMB-016/NF,
  AMB-016/EXCLUSOES, AMB-018/ESCOPO — todas presentes no texto de spec.md.
  - RN-001 a RN-018: as 18 existem, cada uma com Regra/Origem/Aceite
  preenchidos.
  - CA-001 a CA-022: as 22 existem em 10., todas com cenário concreto e numérico
  — nenhuma vaga tipo "deve funcionar corretamente".
  - Rastreabilidade (seção 9): os 9 itens da política do RH + a linha "fora da
  política" cobrem, sem buraco, as 18 AMB, as 18 RN e as 22 CA.
  - spec.md × DECISIONS.md: histórico compatível — cada correção que
  DECISIONS.md diz ter feito (R-1 a R-8) está de fato refletida no texto atual
  de spec.md.
  - git status / git diff --check: só há alterações não commitadas em
  docs/sessions/01-validacao-export.md, DECISIONS.md, spec.md, mais o novo
  docs/sessions/02-especificacao-inicial.md (untracked) — nenhuma ação
  destrutiva foi tomada. O único aviso do --check é whitespace de fim de linha
  num export de sessão (01-validacao-export.md), irrelevante para os documentos
  normativos.
  - Vazamento técnico: nenhum (busquei por termos de linguagem/lib/estrutura em
  spec.md — zero ocorrências).
  - Placeholders: nenhum remanescente — comparei linha a linha com
  template/specs/001-motor-reembolso/spec.md (que ainda tem <...> por toda
  parte) e confirmei que a versão real está 100% preenchida.

  Problemas encontrados, por gravidade

  Nenhum problema alto — nenhuma contradição que altere resultado; o exemplo
  completo recalcula sem divergência.

  Médios (3):

  1. Sem regra geral para CAMPO_TIPO_INVALIDO × CAMPO_FORMATO_INVALIDO
  (spec.md:170-172, aceite em spec.md:256). Só há dois exemplos pontuais (data
  em formato errado → formato; valor como string → tipo). Não há critério geral,
  então casos não exemplificados — despesa.id: "", despesa.categoria recebido
  como número, despesa.tem_nota_fiscal recebido como string — ficam à
  interpretação de quem implementa. Como o código aparece na saída, duas
  implementações compatíveis com a spec podem divergir num teste oculto.
  2. valor_informado mal definido quando despesa.valor é estruturalmente 
  inválido (spec.md:122). O tipo declarado — "número, conteúdo recebido ou nulo"
  — não deixa claro se o conteúdo bruto não-numérico (ex.: "72,50") é
  preservado como veio ou vira nulo. Não há exemplo (nem em 4.6/4.7, nem nos
  casos de borda) mostrando esse caso especificamente para valor_informado — só
  para valor_normalizado.
  3. Matriz de dependências (8.2) incompleta (spec.md:584-598). Não lista RN-003
  (unicidade de despesa.id), embora a regra dependa claramente de despesa.id —
  dependência essa que só aparece em outro lugar (8.4, item 6). O comportamento
  está coberto, mas a tabela que se apresenta como a matriz não é exaustiva.

  Baixos (3, cosméticos):

  4. Coluna "Vazio permitido" (spec.md:78-86) aplicada também a despesa.valor
  (número) e despesa.tem_nota_fiscal (booleano), onde "vazio" não é um conceito
  aplicável.
  5. Exclusão 8.4, item 10 (spec.md:626) é redundante — já decorre
  automaticamente da própria condição de RN-009 (valor > 100 já implica valor > 
  0), não é uma exclusão nova.
  6. A afirmação em spec.md:695 ("nenhum comportamento da versão atual está
  indefinido") é um pouco otimista à luz dos achados 1 e 2 — são lacunas de
  definição, não "limitações de produto" como as listadas naquela seção.

  Recomendação objetiva

  Corrigir antes de seguir para plan.md — mas é ajuste pequeno, não retrabalho.
  A spec está estruturalmente sólida: total e as 14 decisões batem 100% com
  recomputação independente, as 18+15+18+22 entidades existem e se cruzam sem
  buraco, não há vazamento técnico nem placeholder. Os três achados médios são
  pontuais (uma frase de regra geral para tipo×formato, uma frase esclarecendo
  valor_informado no caso malformado, uma linha na matriz 8.2) e valem a pena
  fechar agora porque são exatamente o tipo de fronteira que os casos ocultos do
  instrutor tendem a explorar — deixá-los em aberto arrisca pontos no critério
  5 (produto) por divergência de código de saída, não por erro de cálculo.
  Registrar a correção como nova entrada em DECISIONS.md antes de avançar.

✻ Worked for 2m 26s

※ recap: Auditing the reembolso spec before plan.md: independent recompute of the example confirmed the R$585,43 total and all traceability tables, with three medium-severity definitional gaps found. Next: decide whether to fix those in spec.md and log them in DECISIONS.md before moving on. (disable recaps in /config)

> Autorizo corrigir os problemas encontrados nesta auditoria independente.      
                                                                                
  Modifique somente:                                                            
                                                                                
  * specs/001-motor-reembolso/spec.md;                                          
  * specs/001-motor-reembolso/DECISIONS.md.                                     
  1. Classificação dos erros estruturais                                        
                                                                                
  Defina uma regra geral e fechada:                                             
                                                                                
  * CAMPO\_AUSENTE: a chave obrigatória não existe ou seu valor é nulo.         
  * CAMPO\_TIPO\_INVALIDO: a chave existe, não é nula, mas seu tipo JSON é      
  diferente do tipo exigido pelo contrato.                                      
  * CAMPO\_FORMATO\_INVALIDO: o tipo JSON está correto, mas o conteúdo viola    
  uma restrição do campo.                                                       
                                                                                
  Exemplos normativos:                                                          
                                                                                
  * despesa.id igual a texto vazio → CAMPO\_FORMATO\_INVALIDO;                  
  * despesa.data igual a "31/07/2026" → CAMPO\_FORMATO\_INVALIDO;               
  * despesa.data igual a "2026-02-30" → CAMPO\_FORMATO\_INVALIDO;               
  * despesa.categoria igual a número → CAMPO\_TIPO\_INVALIDO;                   
  * despesa.categoria igual a texto vazio → CAMPO\_FORMATO\_INVALIDO;           
  * despesa.valor igual a "72,50" → CAMPO\_TIPO\_INVALIDO;                      
  * despesa.tem\_nota\_fiscal igual a "sim" → CAMPO\_TIPO\_INVALIDO;            
  * despesa.valor igual a 0 ou número negativo é estruturalmente válido e       
  somente depois recebe VALOR\_NAO\_POSITIVO.                                   
                                                                                
  Booleano não deve ser aceito como número, mesmo que alguma linguagem trate    
  booleano como subtipo numérico.                                               
                                                                                
  Atualize contrato, RN-002, vocabulário, casos de borda e critérios            
  relacionados.                                                                 
                                                                                
  2. valor\_informado                                                           
                                                                                
  Feche o comportamento:                                                        
                                                                                
  * se a chave despesa.valor existir, valor\_informado preserva exatamente o    
  valor JSON recebido, mesmo quando seu tipo for inválido;                      
  * valor: "72,50" produz valor\_informado igual a "72,50";                     
  * valor: true produz valor\_informado igual a true;                           
  * valor ausente ou nulo produz valor\_informado nulo;                         
  * elemento de despesas que não seja objeto produz valor\_informado nulo;      
  * valor\_normalizado permanece nulo sempre que o valor não for um número      
  válido.                                                                       
                                                                                
  Altere o tipo de valor\_informado na tabela de saída para “qualquer valor     
  JSON ou nulo”.                                                                
                                                                                
  3. Matriz de dependências                                                     
                                                                                
  Acrescente explicitamente:                                                    
                                                                                
  * Unicidade de despesa.id — depende de despesa.id.                            
                                                                                
  Mantenha as demais linhas.                                                    
                                                                                
  4. Tabela do contrato                                                         
                                                                                
  Substitua a coluna “Vazio permitido” por uma coluna chamada “Restrição        
  adicional”, ou equivalente.                                                   
                                                                                
  Preencha de maneira semanticamente correta:                                   
                                                                                
  * despesa.id: texto não vazio;                                                
  * despesa.data: data real em AAAA-MM-DD;                                      
  * despesa.categoria: texto não vazio;                                         
  * despesa.descricao: texto, podendo ser vazio;                                
  * despesa.fornecedor: texto, podendo ser vazio;                               
  * despesa.valor: número; zero e negativos são estruturalmente válidos e       
  avaliados depois;                                                             
  * despesa.tem\_nota\_fiscal: booleano.                                        
  5. Exclusão do valor não positivo                                             
                                                                                
  Não remova a declaração de que valor não positivo torna a regra documental    
  não aplicável.                                                                
                                                                                
  Acrescente uma justificativa curta dizendo que, embora RN-009 use comparação  
  maior que R$ 100, a declaração é mantida deliberadamente para excluir uma     
  interpretação por valor absoluto — por exemplo, −500 não exige nota.          
                                                                                
  6. Critério de aceite adicional                                               
                                                                                
  Crie CA-023 cobrindo conjuntamente a classificação estrutural e               
  valor\_informado.                                                             
                                                                                
  O cenário deve conter, no mínimo:                                             
                                                                                
  * despesa.id como texto vazio;                                                
  * despesa.data como "31/07/2026";                                             
  * despesa.categoria como número;                                              
  * despesa.valor como "72,50";                                                 
  * despesa.tem\_nota\_fiscal como "sim".                                       
                                                                                
  O aceite deve declarar:                                                       
                                                                                
  * os códigos corretos;                                                        
  * os campos qualificados corretos;                                            
  * a ordem canônica dos motivos;                                               
  * valor\_informado preservado como "72,50";                                   
  * valor\_normalizado nulo;                                                    
  * valor\_reembolsavel 0,00.                                                   
                                                                                
  Atualize a rastreabilidade para CA-001 a CA-023.                              
                                                                                
  7. Histórico e versão                                                         
                                                                                
  Como a spec 1.0 já foi marcada como aprovada e esta é uma auditoria           
  independente posterior, registre uma mudança real:                            
                                                                                
  * crie D-002 acima de D-001;                                                  
  * título: “Ajustes de contrato após auditoria independente”;                  
  * gatilho: auditoria posterior à elaboração inicial;                          
  * registre os três problemas médios e os ajustes de clareza;                  
  * informe que nenhum cálculo dos 14 itens foi alterado;                       
  * tasks afetadas: nenhuma, pois tasks.md ainda não foi elaborado;             
  * custo: alteração documental e acréscimo de CA-023.                          
                                                                                
  Atualize o cabeçalho da spec para:                                            
                                                                                
  * versão 1.1;                                                                 
  * status aprovada;                                                            
  * data atual.                                                                 
                                                                                
  Não altere retrospectivamente os números declarados em D-001: naquela linha   
  de base havia 22 critérios. D-002 deve registrar que a versão 1.1 passou a    
  ter 23.                                                                       
                                                                                
  8. Verificação                                                                
                                                                                
  Depois das correções:                                                         
                                                                                
  * releia integralmente os dois documentos;                                    
  * confirme 18 AMB, 15 subdecisões, 18 RN e 23 CA;                             
  * confirme o total R$ 585,43;                                                 
  * execute git diff --check limitado a spec.md e DECISIONS.md;                 
  * execute git status --short;                                                 
  * informe os caminhos exatamente como aparecerem no status;                   
  * não altere os exports de sessão;                                            
  * não modifique outros arquivos;                                              
  * não execute git add;                                                        
  * não faça commit.                                                            

● Autorizado. Vou aplicar as correções em spec.md e DECISIONS.md, na ordem dos
  pontos 1–7, e depois rodar as verificações do ponto 8.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1  # Spec — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.0 · **Status:** aprovada · **Última alteração:** 2
        -026-07-30                                                       
      3 +**Versão:** 1.1 · **Status:** aprovada · **Última alteração:** 2
        +026-07-30                                                       
      4  
      5  > **Regra de ouro deste arquivo:** ele descreve o QUÊ e o PORQUÊ
         . Nenhuma linha
      6  > aqui pode citar linguagem, biblioteca, classe, função ou estru
         tura de pasta.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 8 lines, removed 8 lines
      75  
      76  Os sete campos abaixo são obrigatórios. A sequência é a **ordem
           canônica de contrato**, usada para ordenar motivos estruturais
           na saída.
      77  
      78 -| # | Campo | Tipo | Significado | Vazio permitido |           
      78 +| # | Campo | Tipo | Significado | Restrição adicional |       
      79  |---|---|---|---|---|
      80 -| 1 | `despesa.id` | texto não vazio | Identificador do lançame
         -nto. **Único no arquivo** | Não |                              
      81 -| 2 | `despesa.data` | texto `AAAA-MM-DD` | Data do fato gerado
         -r, real no calendário | Não |                                  
      82 -| 3 | `despesa.categoria` | texto | Categoria informada, antes 
         -da normalização | Não |                                        
      83 -| 4 | `despesa.descricao` | texto | Descrição livre. Informativ
         -a; integra a chave de duplicidade como recebida | **Sim** |    
      84 -| 5 | `despesa.fornecedor` | texto | Fornecedor. Integra a chav
         -e de duplicidade como recebido | **Sim** |                     
      85 -| 6 | `despesa.valor` | número | Valor monetário em BRL. Inteir
         -o é aceito (`72` → `72,00`) | Não |                            
      86 -| 7 | `despesa.tem_nota_fiscal` | booleano | Existência de nota
         - fiscal | Não |                                                
      80 +| 1 | `despesa.id` | texto | Identificador do lançamento. **Úni
         +co no arquivo** | Texto não vazio |                            
      81 +| 2 | `despesa.data` | texto | Data do fato gerador | Formato `
         +AAAA-MM-DD` representando data real do calendário |            
      82 +| 3 | `despesa.categoria` | texto | Categoria informada, antes 
         +da normalização | Texto não vazio |                            
      83 +| 4 | `despesa.descricao` | texto | Descrição livre. Informativ
         +a; integra a chave de duplicidade como recebida | Texto, podend
         +o ser vazio |                                                  
      84 +| 5 | `despesa.fornecedor` | texto | Fornecedor. Integra a chav
         +e de duplicidade como recebido | Texto, podendo ser vazio |    
      85 +| 6 | `despesa.valor` | número | Valor monetário em BRL. Inteir
         +o é aceito (`72` → `72,00`) | Nenhuma quanto ao sinal: zero e n
         +egativos são estruturalmente válidos, avaliados depois por RN-0
         +06 |                                                           
      86 +| 7 | `despesa.tem_nota_fiscal` | booleano | Existência de nota
         + fiscal | Nenhuma além do tipo booleano — um valor booleano rec
         +ebido não é aceito como número, nem o inverso |                
      87  
      88  **Nomes qualificados.** A forma `despesa.<campo>` é o nome canô
          nico do campo em toda esta spec, e é exatamente o conjunto de v
          alores não nulos aceitos em `motivo.campo` (4.3). A chave corre
          spondente dentro do objeto de despesa é o segmento à direita do
           ponto: o campo canônico `despesa.valor` corresponde à chave `v
          alor`.
      89  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 19 lines
       89  
       90  Campo **ausente**, **nulo**, de **tipo inválido** ou de **form
           ato inválido** produz **erro estrutural no item**: o item é re
           cusado e o restante do arquivo continua sendo processado.
       91  
       92 +**Classificação do erro estrutural.** A escolha entre os três 
          +motivos abaixo é fechada e não admite outra leitura:          
       93 +                                                              
       94 +- **`CAMPO_AUSENTE`** — a chave obrigatória não existe no obje
          +to, ou existe com valor `nulo`.                               
       95 +- **`CAMPO_TIPO_INVALIDO`** — a chave existe, não é nula, mas 
          +seu tipo JSON diverge do exigido na coluna "Tipo" da tabela ac
          +ima. Um valor booleano nunca é aceito como número, mesmo em li
          +nguagens de implementação que tratam booleano como subtipo num
          +érico — e o inverso também não é aceito.                      
       96 +- **`CAMPO_FORMATO_INVALIDO`** — o tipo JSON está correto, mas
          + o conteúdo viola a "Restrição adicional" da tabela acima: tex
          +to vazio onde não é permitido, texto que não segue `AAAA-MM-DD
          +`, ou texto nesse padrão que não representa uma data real do c
          +alendário.                                                    
       97 +                                                              
       98 +Exemplos normativos:                                          
       99 +                                                              
      100 +| Campo recebido | Classificação |                            
      101 +|---|---|                                                     
      102 +| `despesa.id` igual a texto vazio | `CAMPO_FORMATO_INVALIDO` 
          +|                                                             
      103 +| `despesa.data` igual a `"31/07/2026"` | `CAMPO_FORMATO_INVAL
          +IDO` |                                                        
      104 +| `despesa.data` igual a `"2026-02-30"` | `CAMPO_FORMATO_INVAL
          +IDO` — formato correto, data inexistente no calendário |      
      105 +| `despesa.categoria` igual a um número | `CAMPO_TIPO_INVALIDO
          +` |                                                           
      106 +| `despesa.categoria` igual a texto vazio | `CAMPO_FORMATO_INV
          +ALIDO` |                                                      
      107 +| `despesa.valor` igual a `"72,50"` | `CAMPO_TIPO_INVALIDO` | 
      108 +| `despesa.tem_nota_fiscal` igual a `"sim"` | `CAMPO_TIPO_INVA
          +LIDO` |                                                       
      109 +| `despesa.valor` igual a `0` ou a um número negativo | Estrut
          +uralmente válido — nenhum motivo estrutural. Tratado depois po
          +r RN-006 (`VALOR_NAO_POSITIVO`) |                             
      110 +                                                              
      111  **Elemento que não é objeto.** Cada elemento de `despesas` dev
           e ser um objeto. Quando não for — texto, número, lista, nulo —
           , aquela posição é recusada com o motivo único `ITEM_TIPO_INVA
           LIDO`. Não se produzem sete motivos de campo ausente e nenhuma
            regra de negócio é avaliada. O registro correspondente é exat
           amente:
      112  
      113  | Campo da saída | Valor |

● Agora o comportamento de valor_informado e a tabela de saída (4.3):

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 1 line
      138  |---|---|---|
      139  | `indice_entrada` | inteiro ≥ 1 | Posição original do item na
            lista, **base 1**, atribuída antes de qualquer validação e im
           utável |
      140  | `id` | texto ou nulo | `despesa.id` original. Nulo quando au
           sente ou inválido |
      141 -| `valor_informado` | número, conteúdo recebido ou nulo | Valo
          -r como recebido, preservado para auditoria. Preserva o valor, 
          -não a formatação textual |                                    
      141 +| `valor_informado` | qualquer valor JSON ou nulo | O conteúdo
          + de `despesa.valor` exatamente como recebido, preservado para 
          +auditoria — inclusive quando o tipo é inválido |              
      142  | `valor_normalizado` | monetário ou nulo | Valor após normali
           zação monetária. Nulo quando não calculável |
      143  | `valor_reembolsavel` | monetário | Valor aprovado. Sempre `0
           ,00` para item recusado |
      144  | `decisao` | enumeração | Ver 4.4 |
      145  | `motivos` | lista de objetos, possivelmente vazia | Objetos 
           de motivo, na ordem definida em 8.3 |
      146  
      147 +**Preservação de `valor_informado`.** Quando a chave `valor` e
          +xiste no objeto de despesa, `valor_informado` é exatamente o v
          +alor JSON recebido — número, texto, booleano, lista ou objeto 
          +—, mesmo quando esse tipo é inválido para o contrato: `valor: 
          +"72,50"` produz `valor_informado` igual a `"72,50"`; `valor: t
          +rue` produz `valor_informado` igual a `true`. `valor_informado
          +` é nulo quando a chave `valor` está ausente ou é nula, e tamb
          +ém quando o elemento de `despesas` não é um objeto (4.2). `val
          +or_normalizado` permanece nulo sempre que `despesa.valor` não 
          +for um número válido, independentemente do que `valor_informad
          +o` contenha.                                                  
      148 +                                                              
      149  Cada objeto de `motivos` tem três campos:
      150  
      151  | Campo | Tipo | Significado |

● Agora o vocabulário de motivos (4.5):

● Update(specs\001-motor-reembolso\spec.md)  ⎿  Added 3 lines, removed 3 lines
      188  | Código | Regra associada | `campo` | Significado |
      189  |---|---|---|---|
      190  | `ITEM_TIPO_INVALIDO` | RN-002 | nulo | Elemento de `despesas
           ` que não é objeto. Motivo único da posição |
      191 -| `CAMPO_AUSENTE` | RN-002 | um dos sete nomes canônicos | Cam
          -po obrigatório ausente ou nulo |                              
      192 -| `CAMPO_TIPO_INVALIDO` | RN-002 | um dos sete nomes canônicos
          - | Campo presente com tipo incorreto |                        
      193 -| `CAMPO_FORMATO_INVALIDO` | RN-002 | um dos sete nomes canôni
          -cos | Campo com tipo correto e formato inválido |             
      191 +| `CAMPO_AUSENTE` | RN-002 | um dos sete nomes canônicos | A c
          +have obrigatória não existe no objeto, ou existe com valor nul
          +o |                                                           
      192 +| `CAMPO_TIPO_INVALIDO` | RN-002 | um dos sete nomes canônicos
          + | A chave existe, não é nula, mas seu tipo JSON diverge do ex
          +igido pelo contrato |                                         
      193 +| `CAMPO_FORMATO_INVALIDO` | RN-002 | um dos sete nomes canôni
          +cos | O tipo JSON está correto, mas o conteúdo viola a restriç
          +ão adicional do campo |                                       
      194  | `ID_DUPLICADO` | RN-003 | `despesa.id` | `despesa.id` válido
            repetido no arquivo |
      195  | `VALOR_NAO_POSITIVO` | RN-006 | nulo | Valor normalizado men
           or ou igual a zero |
      196  | `CATEGORIA_FORA_POLITICA` | RN-007 | nulo | Categoria normal
           izada fora da lista canônica |

● Agora RN-002:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      272  
      273  ### RN-002 — Contrato do item
      274  
      275 -**Regra:** cada elemento de `despesas` deve ser um objeto cont
          -endo os sete campos de 4.2 com os tipos e formatos declarados.
          - Elemento que não é objeto é recusado com o motivo único `ITEM
          -_TIPO_INVALIDO`, sem que nenhuma regra de negócio seja avaliad
          -a e sem gerar motivos de campo ausente; o registro resultante 
          -traz `id`, `valor_informado` e `valor_normalizado` nulos, `val
          -or_reembolsavel` `0,00` e `indice_entrada` preservado. Sendo o
          -bjeto, campo **ausente**, **nulo**, de **tipo inválido** ou de
          - **formato inválido** recusa o item, com um motivo por campo d
          -efeituoso. Em qualquer caso o arquivo não é interrompido.     
      275 +**Regra:** cada elemento de `despesas` deve ser um objeto cont
          +endo os sete campos de 4.2 com os tipos e restrições declarado
          +s. Elemento que não é objeto é recusado com o motivo único `IT
          +EM_TIPO_INVALIDO`, sem que nenhuma regra de negócio seja avali
          +ada e sem gerar motivos de campo ausente; o registro resultant
          +e traz `id`, `valor_informado` e `valor_normalizado` nulos, `v
          +alor_reembolsavel` `0,00` e `indice_entrada` preservado. Sendo
          + objeto, campo **ausente**, **nulo**, de **tipo inválido** ou 
          +de **formato inválido** recusa o item, com um motivo por campo
          + defeituoso, classificado pela regra fechada da subseção "Clas
          +sificação do erro estrutural" (4.2). Em qualquer caso o arquiv
          +o não é interrompido.                                         
      276  **Origem:** contrato de entrada.
      277 -**Aceite:** item com `data: "31/07/2026"` e `valor: "72,50"` é
          - recusado com dois motivos, nesta ordem — `CAMPO_FORMATO_INVAL
          -IDO` com `campo` igual a `despesa.data`, depois `CAMPO_TIPO_IN
          -VALIDO` com `campo` igual a `despesa.valor` —, porque `despesa
          -.data` precede `despesa.valor` na ordem canônica de contrato. 
          -Um elemento de `despesas` igual ao texto `"despesa"` produz um
          - único registro recusado, com motivo único `ITEM_TIPO_INVALIDO
          -`, `campo` nulo, `id` nulo, `valor_informado` nulo e `indice_e
          -ntrada` preservado.                                           
      277 +**Aceite:** item com `data: "31/07/2026"` e `valor: "72,50"` é
          + recusado com dois motivos, nesta ordem — `CAMPO_FORMATO_INVAL
          +IDO` com `campo` igual a `despesa.data`, depois `CAMPO_TIPO_IN
          +VALIDO` com `campo` igual a `despesa.valor` —, porque `despesa
          +.data` precede `despesa.valor` na ordem canônica de contrato. 
          +`despesa.categoria` igual a um número é recusado com `CAMPO_TI
          +PO_INVALIDO` e `campo` igual a `despesa.categoria`; `despesa.i
          +d` igual a texto vazio é recusado com `CAMPO_FORMATO_INVALIDO`
          + e `campo` igual a `despesa.id`. Um elemento de `despesas` igu
          +al ao texto `"despesa"` produz um único registro recusado, com
          + motivo único `ITEM_TIPO_INVALIDO`, `campo` nulo, `id` nulo, `
          +valor_informado` nulo e `indice_entrada` preservado.          
      278  
      279  ### RN-003 — Unicidade de `despesa.id`
      280  

● Agora os casos de borda (§7), a matriz de dependências (8.2) e a exclusão
  sobre valor não positivo (8.4):

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 6 lines, removed 2 lines
      569  | Duas hospedagens na mesma data | Dois lançamentos elegíveis 
           | Até `500,00` na data | RN-013 |
      570  | Hospedagem sem nota | `690.00`, sem nota | Recusado antes de
            qualquer teto | RN-009 |
      571  | `despesa.id` repetido | Dois itens com o mesmo ID válido | *
           *Ambos** recusados, `ID_DUPLICADO` | RN-003 |
      572 -| `despesa.id` inválido | Texto vazio | Erro estrutural; não e
          -ntra na verificação de repetição | RN-002, RN-003 |           
      573 -| Campo malformado | `valor: "72,50"` | Recusado; `valor_norma
          -lizado` nulo | RN-002 |                                       
      572 +| `despesa.id` inválido | Texto vazio | `CAMPO_FORMATO_INVALID
          +O`; não entra na verificação de repetição | RN-002, RN-003 |  
      573 +| Campo malformado | `valor: "72,50"` | Recusado, `CAMPO_TIPO_
          +INVALIDO`; `valor_normalizado` nulo; `valor_informado` preserv
          +a `"72,50"` | RN-002 |                                        
      574  | Dois campos malformados | `data: "31/07/2026"` e `valor: "72
           ,50"` | Dois motivos, `despesa.data` antes de `despesa.valor` 
           | RN-002 |
      575 +| Categoria de tipo inválido | `categoria: 123` | Recusado, `C
          +AMPO_TIPO_INVALIDO` | RN-002 |                                
      576 +| Data com formato correto e inexistente | `data: "2026-02-30"
          +` | Recusado, `CAMPO_FORMATO_INVALIDO` | RN-002 |             
      577 +| Nota fiscal de tipo inválido | `tem_nota_fiscal: "sim"` | Re
          +cusado, `CAMPO_TIPO_INVALIDO` | RN-002 |                      
      578 +| `despesa.valor` booleano | `valor: true` | Recusado, `CAMPO_
          +TIPO_INVALIDO`; `valor_informado` preserva `true`; `valor_norm
          +alizado` nulo | RN-002 |                                      
      579  | Elemento que não é objeto | `despesas` contendo o texto `"de
           spesa"` | Recusado com motivo único `ITEM_TIPO_INVALIDO` e `ca
           mpo` nulo; `id`, `valor_informado` e `valor_normalizado` nulos
           ; `indice_entrada` preservado | RN-002 |
      580  | Bloco `colaborador` malformado | `colaborador: "Marina"` | A
           rquivo válido; os três metadados nulos | RN-001 |
      581  | Campo desconhecido | `em_viagem: true` | Ignorado; nenhum ef
           eito | RN-016 |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line
      612  
      613  | Regra | Campos necessários |
      614  |---|---|
      615 +| Unicidade de `despesa.id` | `despesa.id` |                  
      616  | Normalização monetária | `despesa.valor` |
      617  | Normalização de categoria | `despesa.categoria` |
      618  | Valor não positivo | `despesa.valor` |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      649  7. Todas as ocorrências que compartilham o mesmo `despesa.id` 
           válido recebem `ID_DUPLICADO`.
      650  8. `ID_DUPLICADO` **não** impede a avaliação das regras indivi
           duais de negócio cujos campos estejam válidos — o item pode ap
           resentar `ID_DUPLICADO` junto com outros motivos.
      651  9. Item com `ID_DUPLICADO` não participa da detecção de duplic
           idade econômica, não participa da agregação, não consome teto 
           e tem reembolsável `0,00`.
      652 -10. Valor não positivo torna a regra de nota fiscal não aplicá
          -vel.                                                          
      652 +10. Valor não positivo torna a regra de nota fiscal não aplicá
          +vel. Mantido explicitamente apesar de RN-009 já comparar por "
          +maior que R$ 100": a declaração fecha a porta para uma leitura
          + por valor absoluto, que exigiria nota também para valores mui
          +to negativos — por exemplo, −R$ 500,00 não exige nota.        
      653  11. Item recusado nas validações individuais não participa da 
           detecção de duplicidade econômica.
      654  12. Ocorrência recusada como duplicidade econômica não partici
           pa da agregação e não consome teto.
      655  13. Regras de teto alcançam somente os itens que permanecerem 
           elegíveis após todas as validações e a deduplicação.

● Agora o CA-023 e a atualização da rastreabilidade (seção 9):

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      683  | 7 — Período de competência | AMB-009, AMB-010 | RN-008 | CA-
           011, CA-012 |
      684  | 8 — Duplicatas devem ser tratadas | AMB-011, AMB-012 | RN-01
           0 | CA-013, CA-014 |
      685  | 9 — Categorias fora da política | AMB-015 | RN-005, RN-007 |
            CA-015, CA-016 |
      686 -| — (fora da política) | AMB-013, AMB-014, AMB-016, AMB-018 | 
          -RN-001, RN-002, RN-003, RN-004, RN-006, RN-017, RN-018 | CA-00
          -1, CA-002, CA-003, CA-017 a CA-022 |                          
      686 +| — (fora da política) | AMB-013, AMB-014, AMB-016, AMB-018 | 
          +RN-001, RN-002, RN-003, RN-004, RN-006, RN-017, RN-018 | CA-00
          +1, CA-002, CA-003, CA-017 a CA-023 |                          
      687  
      688  ---
      689  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line
      713  - [ ] **CA-020** — Uma entrada com `periodo.inicio` posterior 
           a `periodo.fim` não produz apuração alguma; uma entrada com `d
           espesas: []` produz resultados vazio e total R$ 0,00.
      714  - [ ] **CA-021** — Um item com `data: "31/07/2026"` e `valor: 
           "72,50"` apresenta dois motivos estruturais na ordem canônica 
           de contrato: `CAMPO_FORMATO_INVALIDO` com `campo` igual a `des
           pesa.data`, depois `CAMPO_TIPO_INVALIDO` com `campo` igual a `
           despesa.valor`.
      715  - [ ] **CA-022** — Um elemento de `despesas` que não seja obje
           to produz exatamente um registro `RECUSADO`, com `indice_entra
           da` preservado, `id` nulo, `valor_informado` nulo, `valor_norm
           alizado` nulo, `valor_reembolsavel` R$ 0,00 e um único motivo 
           `ITEM_TIPO_INVALIDO` com `campo` nulo — sem os sete motivos de
            campo ausente —, e o restante do arquivo continua sendo apura
           do. Todo objeto de motivo da saída traz `campo` nulo ou um dos
            sete nomes canônicos de 4.2, e nenhum outro valor.
      716 +- [ ] **CA-023** — Um item com `despesa.id` igual a texto vazi
          +o, `despesa.data` igual a `"31/07/2026"`, `despesa.categoria` 
          +igual a um número, `despesa.valor` igual a `"72,50"` e `despes
          +a.tem_nota_fiscal` igual a `"sim"` (demais campos válidos) é r
          +ecusado com cinco motivos, na ordem canônica de contrato — `CA
          +MPO_FORMATO_INVALIDO` (`despesa.id`), `CAMPO_FORMATO_INVALIDO`
          + (`despesa.data`), `CAMPO_TIPO_INVALIDO` (`despesa.categoria`)
          +, `CAMPO_TIPO_INVALIDO` (`despesa.valor`), `CAMPO_TIPO_INVALID
          +O` (`despesa.tem_nota_fiscal`). `valor_informado` preserva `"7
          +2,50"`; `valor_normalizado` é nulo; `valor_reembolsavel` é `0,
          +00`.                                                          
      717  
      718  ---
      719  

● Agora o DECISIONS.md — inserindo D-002 acima de D-001, sem tocar em D-001:

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 22 lines
      10  
      11  ---
      12  
      13 +## D-002 — Ajustes de contrato após auditoria independente · `2
         +026-07-30`                                                     
      14 +                                                               
      15 +**Gatilho:** auditoria independente da spec 1.0 aprovada, reali
         +zada em sessão separada da elaboração original, com recomputaçã
         +o manual dos 14 itens de `exemplos/despesas-exemplo.json` a par
         +tir das regras (não da tabela 4.7) e leitura cruzada de `spec.m
         +d` com este arquivo. A auditoria confirmou o total de R$ 585,43
         + e as 14 decisões, sem nenhuma divergência de cálculo, mas enco
         +ntrou três problemas de gravidade média, todos de precisão de c
         +ontrato — nenhum deles altera resultado do arquivo de exemplo. 
      16 +                                                               
      17 +**O que mudou na spec:**                                       
      18 +                                                               
      19 +1. **Classificação dos erros estruturais (4.2, RN-002, 4.5, §7)
         +.** A spec distinguia `CAMPO_TIPO_INVALIDO` de `CAMPO_FORMATO_I
         +NVALIDO` sem enunciar o critério geral — só havia dois exemplos
         + pontuais. Passa a existir regra fechada: ausência ou nulo → `C
         +AMPO_AUSENTE`; tipo JSON errado → `CAMPO_TIPO_INVALIDO`; tipo c
         +erto com conteúdo que viola restrição → `CAMPO_FORMATO_INVALIDO
         +`. Booleano nunca é aceito como número, nem o inverso. Sete exe
         +mplos normativos foram adicionados em 4.2, e os casos de borda 
         +(§7) ganharam quatro linhas novas (categoria numérica, data com
         + formato certo e calendário inexistente, nota fiscal como texto
         +, valor booleano).                                             
      20 +2. **`valor_informado` (4.3).** O tipo era descrito como "númer
         +o, conteúdo recebido ou nulo" — união pouco clara que não dizia
         + o que acontece quando `despesa.valor` chega com tipo inválido.
         + Passa a ser "qualquer valor JSON ou nulo": preserva exatamente
         + o que foi recebido em `despesa.valor`, mesmo com tipo errado (
         +`"72,50"` continua `"72,50"`, `true` continua `true`); é nulo q
         +uando a chave está ausente, nula, ou quando o elemento de `desp
         +esas` não é objeto. `valor_normalizado` continua nulo sempre qu
         +e `despesa.valor` não for número válido — isso não mudou, só fi
         +cou explícito que é independente de `valor_informado`.         
      21 +3. **Matriz de dependências (8.2).** Faltava a linha de RN-003 
         +(unicidade de `despesa.id`), apesar de a dependência já estar i
         +mplícita em 8.4, item 6. Adicionada.                           
      22 +                                                               
      23 +Ajustes de clareza que acompanharam a correção, sem mudar compo
         +rtamento: a coluna "Vazio permitido" de 4.2 virou "Restrição ad
         +icional", com o texto da coluna "Tipo" simplificado para o tipo
         + JSON puro; a exclusão de nota fiscal por valor não positivo (8
         +.4, item 10) ganhou uma frase justificando por que é mantida me
         +smo sendo derivável da própria condição de RN-009 — fecha a por
         +ta para uma leitura por valor absoluto (ex.: −R$ 500,00 não exi
         +gir nota). Acrescentado `CA-023`, cobrindo simultaneamente a cl
         +assificação estrutural e a preservação de `valor_informado` num
         + único item com cinco campos malformados.                      
      24 +                                                               
      25 +**Por quê:** os três problemas médios eram pontos onde duas imp
         +lementações igualmente fiéis ao texto anterior da spec podiam d
         +ivergir no código de saída — exatamente o tipo de fronteira que
         + casos ocultos de avaliação costumam explorar. Fechar agora cus
         +ta uma revisão textual; deixar aberto arriscava pontos no crité
         +rio de produto por um motivo que não é erro de cálculo.        
      26 +                                                               
      27 +**O que isso invalidou:** nenhum cálculo dos 14 itens de `exemp
         +los/despesas-exemplo.json` mudou — decisão, valor reembolsável 
         +e motivos de cada um permanecem idênticos aos da linha de base,
         + e o total continua R$ 585,43. Nenhuma AMB, subdecisão ou RN fo
         +i criada, removida ou renumerada. A contagem de critérios de ac
         +eite passa de 22 (`CA-001` a `CA-022`, como registrado em D-001
         +) para 23 (`CA-001` a `CA-023`) nesta versão 1.1.              
      28 +                                                               
      29 +**Tasks afetadas:** nenhuma — `tasks.md` ainda não foi elaborad
         +o; esta auditoria antecede o Dia 1 tarde.                      
      30 +                                                               
      31 +**Custo:** alteração documental em `spec.md` (seções 4.2, 4.3, 
         +5/RN-002, 4.5, 7, 8.2, 8.4, 9 e 10) e nesta entrada de `DECISIO
         +NS.md`. Nenhum arquivo de código tocado.                       
      32 +                                                               
      33 +---                                                            
      34 +                                                               
      35  ## D-001 — Linha de base da spec 1.0 · `2026-07-30`
      36  
      37  **Gatilho:** análise da política de reembolso v3 cruzada com `e
          xemplos/despesas-exemplo.json`, item a item, antes de qualquer 
          linha de spec ser escrita. A política é ambígua em pontos que a
          lteram o valor pago, e não havia interlocutor no RH para esclar
          ecer.

  Read 2 files (ctrl+o to expand)

● D-002/D-001 e as seções 1–7 de spec.md conferem. Deixe eu ver o restante
  (8–11) para validar as edições na matriz, exclusões, rastreabilidade e CA-023.

  Searched for 4 patterns, read 1 file, ran 1 shell command (ctrl+o to expand)

● Verificação concluída. Resumo:

  Verificação (item 8)

  - 18 AMB, 18 RN, 23 CA confirmados por contagem direta (### AMB- = 18, ### RN-
  = 18, - [ ] **CA- = 23) e 15 subdecisões confirmadas por leitura (linhas 431,
  448×2, 458, 475, 484, 493×3, 511, 512, 513, 522, 523, 541).
  - Total R$ 585,43 intacto — a tabela 4.7 (linhas 240-261) não foi tocada por
  nenhuma edição; nenhuma das 14 decisões do exemplo mudou.
  - git diff --check limitado a spec.md e DECISIONS.md: exit code 0 — só avisos
  de conversão de fim de linha (LF→CRLF, normal neste ambiente Windows), nenhum
  problema real de whitespace.
  - git status --short:
   M docs/sessions/01-validacao-export.md
   M specs/001-motor-reembolso/DECISIONS.md
   M specs/001-motor-reembolso/spec.md
  ?? docs/sessions/02-especificacao-inicial.md
  - Só spec.md e DECISIONS.md foram modificados por mim nesta sessão.
  docs/sessions/01-validacao-export.md (M) e
  docs/sessions/02-especificacao-inicial.md (??) já estavam nesse estado antes
  desta sessão — não mexi neles. Nenhum git add, nenhum commit.

  O que foi corrigido

  1. Classificação estrutural — regra fechada CAMPO_AUSENTE /
  CAMPO_TIPO_INVALIDO / CAMPO_FORMATO_INVALIDO com os sete exemplos normativos,
  em 4.2, RN-002, 4.5 e §7 (4 linhas novas de casos de borda).
  2. valor_informado — tipo redefinido para "qualquer valor JSON ou nulo", com
  parágrafo fechando os cinco comportamentos pedidos.
  3. Matriz 8.2 — linha "Unicidade de despesa.id → despesa.id" adicionada.
  4. Tabela 4.2 — coluna "Restrição adicional" substituindo "Vazio permitido",
  preenchida campo a campo.
  5. Exclusão 8.4, item 10 — justificativa acrescentada, mantendo a declaração.
  6. CA-023 criado, rastreabilidade da seção 9 atualizada para CA-017 a CA-023.
  7. DECISIONS.md — D-002 inserido acima de D-001 (que permanece intocado, com
  seus "22 critérios" originais preservados como registro histórico). Cabeçalho
  de spec.md atualizado para versão 1.1, aprovada, 2026-07-30.

  Nenhum outro arquivo foi tocado.

✻ Crunched for 5m 31s

※ recap: We're auditing and hardening the reembolso engine's spec.md before coding begins; I just applied fixes for 3 medium issues found (structural error classification, valor_informado, dependency matrix) and added CA-023. Next: you review the diffs in spec.md and DECISIONS.md, then say go/no-go for plan.md. (disable recaps in /config)