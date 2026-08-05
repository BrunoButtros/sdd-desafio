 ▐▛███▜▌   Claude Code v2.1.222
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Os cinco arquivos oficiais do Dia 2 estão em:                                 
                                                                                
  `exemplos/envelope/`                                                          
                                                                                
  Continue somente em modo de análise.                                          
                                                                                
  Não altere arquivos.                                                          
  Não escreva código.                                                           
  Não atualize `spec.md`, `DECISIONS.md`, `plan.md` ou `tasks.md`.              
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  Primeiro leia integralmente:                                                  
                                                                                
  * `exemplos/envelope/00-ENVELOPE-LACRADO.md`;                                 
  * `exemplos/envelope/politica-v4.json`;                                       
  * `exemplos/envelope/cambio.json`;                                            
  * `exemplos/envelope/despesas-envelope.json`;                                 
  * `exemplos/envelope/despesas-envelope-cc-desconhecido.json`;                 
  * `specs/001-motor-reembolso/spec.md`;                                        
  * `specs/001-motor-reembolso/DECISIONS.md`;                                   
  * `specs/001-motor-reembolso/plan.md`;                                        
  * `specs/001-motor-reembolso/tasks.md`;                                       
  * implementação atual relacionada a política, envelope, valores, regras,      
  agregadores, composição da saída e CLI;                                       
  * testes atuais relacionados a essas áreas.                                   
                                                                                
  Depois apresente:                                                             
                                                                                
  1. resumo objetivo do envelope;                                               
  2. mudanças obrigatórias em relação à spec 1.1;                               
  3. regras de negócio atuais afetadas ou invalidadas;                          
  4. critérios de aceite afetados;                                              
  5. decisões técnicas do plan afetadas;                                        
  6. classes de produção provavelmente afetadas;                                
  7. testes atuais que precisarão mudar ou ganhar regressão;                    
  8. riscos de regressão;                                                       
  9. ambiguidades novas que precisam de decisão humana;                         
  10. proposta preliminar de tasks começando em `preliminar de tasks começando  
  em`T-022`, sem editar `tasks.md`.                                             
                                                                                
  Para cada ambiguidade, apresente separadamente:                               
                                                                                
  * trecho exato ou referência ao arquivo do envelope;                          
  * por que o requisito não determina uma única resposta;                       
  * opções possíveis;                                                           
  * impacto financeiro e técnico de cada opção;                                 
  * recomendação técnica separada da decisão de negócio;                        
  * pergunta objetiva para eu responder.                                        
                                                                                
  Analise obrigatoriamente:                                                     
                                                                                
  * como a CLI recebe `politica-v4.json` e `cambio.json`;                       
  * centro de custo cadastrado com categoria ausente;                           
  * centro de custo ausente, nulo, inválido ou desconhecido;                    
  * significado de limite igual a zero;                                         
  * se moeda estrangeira ativa ou não a regra de viagem;                        
  * data da despesa sem cotação exata;                                          
  * moeda sem qualquer cotação;                                                 
  * ordem da conversão e do arredondamento;                                     
  * uso do valor convertido no gatilho de nota fiscal;                          
  * chave de duplicidade entre valores em moedas diferentes;                    
  * campos necessários na saída para auditoria da conversão;                    
  * validação de `vigencia`;                                                    
  * erros de estrutura nos arquivos externos;                                   
  * classificação entre erro global e recusa individual;                        
  * manutenção da regressão completa da política anterior;                      
  * item C opcional do comunicado.                                              
                                                                                
  Calcule também os resultados possíveis dos dois exemplos do envelope sob cada 
  interpretação materialmente diferente. Não escolha silenciosamente uma        
  interpretação.                                                                
                                                                                
  Ao final, execute somente:                                                    
                                                                                
  ```powershell                                                                 
  git status --short                                                            
  ```                                                                           
                                                                                
  Confirme que:                                                                 
                                                                                
  * nenhum arquivo existente foi modificado;                                    
  * somente `exemplos/envelope/` aparece como conteúdo novo;                    
  * nenhum commit foi criado.                                                   
                                                                                
  Pare para revisão.                                                            

  Searched for 2 patterns, read 30 files (ctrl+o to expand)

● Li os cinco arquivos do envelope, a spec 1.1 completa (incluindo seções 8–11
  que não vieram na primeira leitura), o DECISIONS.md, o plan.md, o tasks.md e
  todo o código de produção relevante (política, envelope, item, normalização,
  regras individuais, agregadores de teto, duplicidade, composição de saída,
  CLI). Também recomputei manualmente os dois arquivos de despesas do envelope
  contra politica-v4.json e cambio.json, e reprocessei
  exemplos/despesas-exemplo.json (o fixture de regressão do Dia 1) sob a
  política v4 para verificar se a regressão se mantém. Nenhum arquivo foi
  alterado — isto é só leitura e análise.

  ---
  1. Resumo objetivo do envelope

  A política de reembolso deixa de ser um valor único hardcoded e passa a ter
  três dimensões novas:

  - A. Limites por centro de custo — politica-v4.json traz um bloco padrao (os
  mesmos 60/80/250 de hoje) e um bloco centros_custo com overrides para
  CC-ENG-PLATAFORMA, CC-COMERCIAL e CC-ADM. CC-COMERCIAL ganha uma quarta
  categoria (representacao, R$300/dia). CC-ENG-PLATAFORMA zera hospedagem.
  CC-ADM não define hospedagem. Centros de custo fora da tabela usam padrao.
  - B. Despesas internacionais — despesa pode trazer moeda (ISO 4217); ausente =
  BRL. Conversão usa a taxa da data da despesa, lida de cambio.json, que só
  cobre dias úteis de duas semanas de julho (13–17, 20–24, 27–28). Os limites da
  política são sempre em BRL.
  - C. (opcional) Fila de aprovação manual para item com reembolsável > R$500 —
  explicitamente descartável se faltar tempo.

  Os dois arquivos de despesas do envelope são desenhados para forçar decisões,
  não para serem triviais: despesas-envelope.json (Rafael, CC-COMERCIAL) tem uma
  despesa em dia sem cotação (EUR, sábado 18/07), uma em moeda sem cotação
  alguma (GBP), e duas categorias novas testadas (representacao, coworking
  continua fora). despesas-envelope-cc-desconhecido.json (Dani, CC-SUPORTE-N2)
  testa centro de custo totalmente ausente da tabela e a categoria representacao
  fora do seu contexto de origem.

  ---
  2. Mudanças obrigatórias em relação à spec 1.1

  A spec 1.1 afirma hoje, textualmente, em três lugares que isto deixa de ser
  verdade:

  Trecho da spec 1.1: §3, fora de escopo: "Não converte nem identifica moedas.
  Todos os valores são interpretados como reais brasileiros (BRL)."
  Por que quebra: Item B do comunicado introduz moeda e conversão — contradição
  direta, não extensão.
  ────────────────────────────────────────
  Trecho da spec 1.1: §5, DT-007 do plan: "estrutura única e imutável... sem
  arquivo externo de configuração"
  Por que quebra: Item A exige leitura de politica-v4.json de fora do código.
  ────────────────────────────────────────
  Trecho da spec 1.1: RN-007: "apenas alimentacao, transporte_urbano e
  hospedagem
  são reembolsáveis" (conjunto fechado, hardcoded)
  Por que quebra: representacao é uma quarta categoria, mas só para quem tem
  entrada — o conjunto reembolsável passa a depender do centro de custo.
  ────────────────────────────────────────
  Trecho da spec 1.1: §11 "O que fica em aberto": "Apenas BRL é suportado... Um
  campo de moeda e uma política de conversão cambial [resolveria isso]"
  Por que quebra: Exatamente o gatilho que o comunicado agora fornece — a
  limitação documentada deixa de ser limitação e vira requisito.

  Isso não é ajuste de redação — é mudança de axioma. A spec 1.1 precisa de uma
  seção nova (política externa + câmbio), não de um adendo dentro de
  RN-007/RN-009.

  ---
  3. Regras de negócio atuais afetadas ou invalidadas

  Regra: RN-004 (normalização monetária)
  Efeito: Precisa de um passo anterior — conversão cambial — cuja ordem em
  relação ao arredondamento de 2 casas é ela mesma ambígua (AMB-ENV-08).
  ────────────────────────────────────────
  Regra: RN-005/RN-007 (categoria)
  Efeito: O conjunto fechado de 3 categorias deixa de ser universal;
  representacao é condicional ao centro de custo. RN-007 vira "depende de qual
  tabela de política se aplica", não mais uma constante.
  ────────────────────────────────────────
  Regra: RN-009 (nota fiscal)
  Efeito: Continua comparando "valor individual normalizado", mas agora esse
  valor pode ser pós-conversão — precisa de decisão explícita (AMB-ENV-09).
  ────────────────────────────────────────
  Regra: RN-010 (duplicidade)
  Efeito: A chave (data, categoria, valor, fornecedor, descricao) não previa
  moeda. Precisa decidir se moeda entra na chave e se valor é o valor original
  ou o convertido (AMB-ENV-10).
  ────────────────────────────────────────
  Regra: RN-011/RN-012/RN-013/RN-014/RN-015 (tetos)
  Efeito: Os limites numéricos (60/80/250) deixam de ser constantes do código —
  viram parâmetros de lookup por centro de custo. RN-013 precisa tratar limite
  = 0 (CC-ENG-PLATAFORMA), caso que o código atual rejeita com  exceção (ver
  §8).
  ────────────────────────────────────────
  Regra: RN-016 (viagem, efeito nulo)
  Efeito: Ganha um candidato a gatilho estruturado (moeda != BRL) que não
  existia
  quando a decisão "sem campo estruturado, sem efeito" foi tomada. Precisa
  reafirmar ou revisar (AMB-ENV-05).
  ────────────────────────────────────────
  Regra: Nenhuma RN é tecnicamente "removida"
  Efeito: Mas RN-007, RN-009, RN-010, RN-011/12/13 mudam de "regra com números
  fixos" para "regra parametrizada por uma tabela externa" — mudança de forma,
  não só de valor.

  Regras novas necessárias (não existem hoje): resolução de política por centro
  de custo; resolução de taxa de câmbio por moeda+data; teto diário de
  representacao; tratamento de moeda sem cotação/data sem cotação.

  ---
  4. Critérios de aceite afetados

  - CA-001/CA-002/CA-003 — dependem do total R$585,43 de
  exemplos/despesas-exemplo.json. Ver achado crítico no §8: esse total não 
  sobrevive à aplicação retroativa da política por centro de custo.
  - CA-008/CA-009 (nota fiscal) — o enunciado ainda vale, mas silenciosamente
  assume BRL; precisa de uma cláusula "valor convertido" explícita.
  - CA-016 (categoria fora da política) — o enunciado usa coworking, que
  continua fora em qualquer centro de custo; sobrevive, mas o texto "apenas
  alimentacao/transporte_urbano/hospedagem são reembolsáveis" que o sustenta
  (RN-007) não é mais universal.
  - CA-007/CA-013 (teto de hospedagem) — nenhum CA cobre limite = 0; é um caso
  novo.
  - Os demais CA-004 a CA-023 continuam válidos como comportamento do caminho 
  "sem centro de custo" / "política padrão" — mas isso precisa virar uma frase
  explícita na spec 1.2, porque hoje eles são apresentados como comportamento
  universal.

  ---
  5. Decisões técnicas do plan afetadas

  - DT-007 invalidada por completo. "Sem mecanismo genérico de regras... sem
  arquivo externo de configuração" era a decisão certa para a spec 1.1 e está
  tecnicamente errada para a v4. Precisa de nova DT: leitura de politica-v4.json
  como estrutura de lookup (centro de custo → categoria → limite), mantendo a
  filosofia "sem DSL" (é uma tabela, não uma engine de regras).
  - DT-005 (parsing por árvore) se estende a cambio.json — mesma justificativa
  (preservar valor bruto, distinguir ausência de zero).
  - DT-003 (contrato de CLI) precisa de novos argumentos (--politica, --cambio)
  e provavelmente novos códigos de saída para arquivo externo malformado/ausente
  — hoje só existem 0/2/3, nenhum deles nomeado para "política externa
  inválida".
  - plan §2 (pipeline linear de 11 passos) precisa de um passo novo entre
  normalização e avaliação de regras individuais (conversão cambial), e o passo
  de agregação de tetos (passo 9) precisa consultar a tabela de política por
  centro de custo em vez de uma constante.
  - plan §11 (estrutura física) precisa de pacotes novos (leitor de política,
  leitor de câmbio, conversor de moeda).

  ---
  6. Classes de produção provavelmente afetadas

  Classe: PoliticaReembolso
  Mudança: Deixa de ser singleton com 4 constantes. Precisa modelar padrao +
  centros_custo (mapa centro→categoria→limite), mais os campos hoje ausentes
  (vigencia, moeda_base, nota_fiscal_obrigatoria_acima_de como dado lido, não
  constante).
  ────────────────────────────────────────
  Classe: ValidadorEnvelope / Envelope
  Mudança: colaborador.centro_custo deixa de ser puramente decorativo e passa a
  alimentar lookup de política — mas sua tolerância a ausência/tipo errado
  (RN-001) precisa continuar valendo.
  ────────────────────────────────────────
  Classe: ValidadorItem / ItemValidado
  Mudança: Novo campo opcional moeda — precisa de validação estrutural própria e

  de um oitavo CampoCanonico.
  ────────────────────────────────────────
  Classe: Normalizador
  Mudança: Precisa de um passo de conversão cambial antes (ou entrelaçado com) o

  arredondamento de 2 casas.
  ────────────────────────────────────────
  Classe: AvaliadorRegrasIndividuais
  Mudança: CATEGORIAS_REEMBOLSAVEIS hoje é um Set.of(...) estático de 3 itens —
  precisa virar dependente do centro de custo (via política injetada). RN-009
  precisa saber se compara valor convertido.
  ────────────────────────────────────────
  Classe: AgregadorTetoDiario / AgregadorTetoHospedagem
  Mudança: Limites vêm de PoliticaReembolso.padrao() hardcoded hoje — precisam
  vir de lookup. aplicarCorte lança IllegalArgumentException se 
  limiteDisponivel <= 0 (linha 121–124 de AgregadorTetoDiario.java) — isso
  quebra literalmente com o limite de hospedagem = 0 de CC-ENG-PLATAFORMA. É o
  achado técnico mais concreto desta auditoria: o  código atual não sobrevive à
   v4 sem uma mudança de comportamento  aqui, não é hipotético.
  ────────────────────────────────────────
  Classe: MotivoCodigo / RegraNegocio / CampoCanonico
  Mudança: Precisam de valores novos (moeda sem cotação, campo moeda) — mas são
  enums fechados por design (DT-008), então isso é extensão limpa, não
  reescrita.
  ────────────────────────────────────────
  Classe: CompositorSaida / EscritorResultado
  Mudança: Saída 4.3 não tem campo para moeda/taxa aplicada/data de cotação —
  auditoria da conversão exige esquema novo.
  ────────────────────────────────────────
  Classe: Main
  Mudança: Novos argumentos de CLI, novo(s) exit code(s) para arquivos externos
  inválidos.

  ---
  7. Testes atuais que precisarão mudar ou ganhar regressão

  - ExemploCompletoTest — o mais crítico. Ver §8: o total R$585,43 muda para
  R$351,43 se a política for aplicada retroativamente ao centro de custo do
  fixture (CC-ENG-PLATAFORMA). Precisa de decisão explícita antes de qualquer
  código.
  - NotaFiscalTest, TetoDiarioTest, TetoHospedagemTest, ReembolsoParcialTest,
  DistribuicaoTetoTest, CategoriaForaPoliticaTest — todos constroem
  ItemAvaliado/chamam agregadores contra PoliticaReembolso.padrao() fixo.
  Continuam válidos como teste do caminho "política padrão", mas precisam de
  irmãos parametrizados por centro de custo.
  - EnvelopeValidoTest — precisa de casos novos: centro de custo desconhecido
  (usa padrão), centro de custo nulo/nome errado (mesmo tratamento que hoje dá a
  colaborador malformado).
  - CliContratoTest / EscritaAtomicaSaidaTest — precisam de casos para
  --politica/--cambio ausentes ou malformados.
  - Testes novos necessários (nenhum existe hoje): conversão de moeda (data
  com/sem cotação exata, moeda sem cotação alguma), ordem
  conversão→arredondamento, chave de duplicidade com moeda, teto de hospedagem =
  0, categoria representacao (presente e ausente por CC), centro de custo
  desconhecido caindo em padrao.

  ---
  8. Riscos de regressão

  Achado principal — a regressão do Dia 1 não sobrevive silenciosamente.
  exemplos/despesas-exemplo.json usa colaborador.centro_custo = 
  "CC-ENG-PLATAFORMA", que existe em politica-v4.json com alimentacao 75,00 (era
  60) e hospedagem 0,00 (era 250). Reprocessando os mesmos 14 itens com lookup
  por centro de custo (mantendo todas as outras regras iguais):

  ┌────────┬─────────────┬──────────────┬─────────────────────┬────────────┐
  │        │             │ Reembolsável │ Reembolsável sob v4 │ Motivo da  │
  │  Item  │  Categoria  │     hoje     │                     │  mudança   │
  │        │             │ (v3/padrão)  │ (CC-ENG-PLATAFORMA) │            │
  ├────────┼─────────────┼──────────────┼─────────────────────┼────────────┤
  │        │             │ 60,00        │                     │ limite     │
  │ d-001  │ alimentacao │ (PARCIAL)    │ 72,50 (INTEGRAL)    │ diário     │
  │        │             │              │                     │ 60→75      │
  ├────────┼─────────────┼──────────────┼─────────────────────┼────────────┤
  │        │             │              │                     │ saldo      │
  │ d-002  │ alimentacao │ 0,00         │ 2,50 (PARCIAL)      │ residual   │
  │        │             │ (ESGOTADO)   │                     │ do dia     │
  │        │             │              │                     │ sobe       │
  ├────────┼─────────────┼──────────────┼─────────────────────┼────────────┤
  │        │             │ 250,00       │                     │ limite     │
  │ d-010  │ hospedagem  │ (PARCIAL)    │ 0,00                │ hospedagem │
  │        │             │              │                     │  250→0     │
  ├────────┼─────────────┼──────────────┼─────────────────────┼────────────┤
  │        │             │ 60,00        │                     │ limite     │
  │ d-014  │ alimentacao │ (PARCIAL)    │ 61,00 (INTEGRAL)    │ diário     │
  │        │             │              │                     │ 60→75      │
  ├────────┼─────────────┼──────────────┼─────────────────────┼────────────┤
  │ demais │             │              │                     │            │
  │  10    │ —           │ inalterado   │ inalterado          │ —          │
  │ itens  │             │              │                     │            │
  └────────┴─────────────┴──────────────┴─────────────────────┴────────────┘

  Total: R$ 585,43 → R$ 351,43 (delta de −R$234,00, dominado pelo corte de
  hospedagem).

  Isso significa: CA-001/CA-002/CA-003 e o fixture de ExemploCompletoTest não 
  podem continuar afirmando R$585,43 se a política por centro de custo for 
  aplicada de forma consistente e retroativa ao arquivo histórico. É uma decisão
  de produto genuína (não técnica): o fixture do Dia 1 congela o comportamento
  sob a política padrão, ou é reprocessado sob a v4 e sua asserção muda? Nenhuma
  leitura é "óbvia" — trato como ambiguidade formal no §9 (AMB-ENV-15).

  Outros riscos:
  - AgregadorTetoDiario.aplicarCorte lança exceção hoje para limite ≤ 0 — não é
  um "possível bug futuro", é uma linha de código existente que quebra no
  primeiro item de hospedagem de CC-ENG-PLATAFORMA processado sob a v4.
  - Tentação de "aproveitar" acrescimo_em_viagem_percentual: 50, que agora está
  sentado como dado pronto em politica-v4.json, para implementar RN-016 sem uma
  decisão explícita de gatilho — o número existir no arquivo não resolve a
  ambiguidade de quando ele se aplica.
  - CATEGORIAS_REEMBOLSAVEIS é hoje um Set estático — qualquer implementação
  apressada de representacao que só adicione ao set global (em vez de
  condicionar por centro de custo) vaza a categoria para todo mundo, incluindo
  CC-SUPORTE-N2 e CC-ADM, o que o comunicado não autoriza.

  ---
  9. Ambiguidades novas que precisam de decisão humana

  Formato por item: trecho, por que não determina resposta única, opções,
  impacto, recomendação técnica (separada da decisão de negócio), e a pergunta
  objetiva.

  AMB-ENV-01 — Como a CLI recebe politica-v4.json e cambio.json

  Trecho: nenhum — o comunicado só diz que os arquivos existem e "o motor
  precisa ler a política de fora, não de dentro do código." Não especifica
  contrato de CLI.
  Por que é ambíguo: poderia ser argumento posicional, flag nomeada, caminho
  fixo por convenção, ou variável de ambiente.
  Opções: (a) --politica <arquivo> --cambio <arquivo> obrigatórios, seguindo o
  padrão já existente de --input/--output; (b) caminhos com default fixo (ex.
  politica.json/cambio.json no diretório de trabalho) com override opcional; (c)
  um único --config <diretorio> contendo ambos.
  Impacto: (a) é mais verboso mas explícito e testável; (b) quebra
  silenciosamente se o avaliador rodar de outro diretório; (c) exige uma
  convenção de nomes de arquivo dentro da pasta, não documentada em lugar
  nenhum.
  Recomendação técnica: (a) — consistente com o contrato já estabelecido
  (--input/--output são obrigatórios e nomeados), sem introduzir convenção de
  caminho implícita.
  Pergunta: os dois arquivos externos devem ser argumentos obrigatórios e
  nomeados (--politica, --cambio), tornando erro de ausência equivalente a
  --input ausente hoje?

  AMB-ENV-02 — Centro de custo cadastrado com categoria ausente

  Trecho: CC-ADM tem alimentacao e transporte_urbano, mas não tem hospedagem.
  "Alguns centros de custo não têm entrada na tabela. Nesse caso, aplica-se a
  política padrão" fala de CC ausente da tabela inteira — não resolve categoria
  ausente dentro de um CC presente.
  Por que é ambíguo: duas leituras igualmente literais do texto.
  Opções: (a) categoria ausente dentro de um CC cadastrado cai para o valor de
  padrao daquela categoria (fallback por categoria); (b) categoria ausente
  dentro de um CC cadastrado significa que aquele CC não reembolsa a categoria
  (equivalente a limite 0/fora de política).
  Impacto: para CC-ADM, (a) reembolsaria hospedagem a R$250/diária; (b)
  reembolsaria R$0. Diferença financeira direta em qualquer despesa de
  hospedagem desse CC. Nenhum dos dois arquivos do envelope tem despesa de
  CC-ADM, então não é observável nos exemplos — mas é exatamente o tipo de
  ambiguidade que aparece na correção com dado oculto.
  Recomendação técnica: (b) — um CC que aparece na tabela é, pela própria
  existência da tabela, uma declaração explícita das categorias que ele cobre;
  presumir fallback por categoria tornaria a ausência de uma entrada
  indistinguível de um esquecimento do financeiro, e a política já usa limite 0
  explícito noutro lugar para dizer "não reembolsa"
  (CC-ENG-PLATAFORMA/hospedagem) — mas aqui a ausência é silenciosa, não um 0
  explícito, então cabe dúvida razoável na direção contrária.
  Pergunta: categoria ausente dentro de um centro de custo cadastrado cai para o
  padrão da categoria, ou significa "não reembolsável" para aquele centro de
  custo?

  AMB-ENV-03 — Centro de custo ausente, nulo, inválido ou desconhecido

  Trecho: "Alguns centros de custo não têm entrada na tabela. Nesse caso,
  aplica-se a política padrão." Cobre "desconhecido" (ex.: CC-SUPORTE-N2,
  exercitado no envelope). Não cobre ausente/nulo/tipo errado.
  Por que é ambíguo: colaborador.centro_custo é campo opcional e tolerante
  (RN-001) — hoje um colaborador malformado nunca invalida o arquivo. A v4
  promove esse campo a insumo financeiro, mas não redefine sua tolerância.
  Opções: (a) estender a mesma regra — ausente/nulo/tipo errado tratam-se como
  "desconhecido", caem em padrao, sem invalidar arquivo nem item; (b) exigir
  centro_custo válido quando qualquer item depende de política de valor
  específico, sob risco de invalidar o envelope.
  Impacto: (b) mudaria RN-001 (regra de tolerância do bloco colaborador) e
  quebraria a garantia atual de que metadados nunca invalidam o arquivo —
  mudança estrutural grande por uma decisão pequena.
  Recomendação técnica: (a) — preserva RN-001 intacta, e é a extensão natural do
  próprio texto do comunicado (que já trata "desconhecido" assim).
  Pergunta: confirma que centro de custo ausente/nulo/de tipo inválido é tratado
  exatamente como "desconhecido" — cai em padrao, sem invalidar arquivo ou
  item?

  AMB-ENV-04 — Significado de limite igual a zero

  Trecho: CC-ENG-PLATAFORMA.hospedagem.limite = 0.00, com "observacao": "nao 
  reembolsavel".
  Por que é ambíguo: o mecanismo de teto hoje (RN-013/014) sempre presume item
  elegível com teto > 0 sendo cortado; nunca modela "esta categoria simplesmente
  não é paga para este CC". Tecnicamente, AgregadorTetoDiario.aplicarCorte
  lança exceção para limite ≤ 0 (ver §8) — não há comportamento definido hoje.
  Opções: (a) tratar como teto normal de valor zero — item permanece elegível,
  decisão PARCIALMENTE_REEMBOLSADO, valor_reembolsavel 0,00, motivo
  TETO_HOSPEDAGEM_APLICADO (extensão mínima do mecanismo existente); (b) tratar
  como recusa de categoria por política — novo motivo (ex.
  CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO), decisão RECUSADO.
  Impacto financeiro: nenhum — valor_reembolsavel é 0,00 em ambos os casos.
  Impacto de auditoria/semântico: grande — (a) diz "existe um teto, e ele foi
  atingido"; (b) diz "esta despesa nunca foi elegível". Um colaborador de
  CC-ENG-PLATAFORMA lendo o motivo teria leituras bem diferentes do porquê.
  Recomendação técnica: (a) — reaproveita a mecânica existente (aplicarCorte),
  exige só remover a guarda de limiteDisponivel > 0; (b) exigiria um estado novo
  espalhado por Decisao, MotivoCodigo e CompositorSaida, para uma diferença que
  não muda nenhum valor pago.
  Pergunta: limite igual a zero é um teto normal que reduz a zero (PARCIAL,
  motivo de teto), ou uma recusa de política (RECUSADO, motivo novo)?

  AMB-ENV-05 — Moeda estrangeira ativa ou não a regra de viagem (item 6/RN-016)

  Trecho: comunicado B só fala de conversão; RN-016 hoje declara efeito nulo
  porque "a entrada não possui campo estruturado que informe viagem" (AMB-006
  original). Agora existe moeda, um campo estruturado.
  Por que é ambíguo: moeda != BRL é evidência estrutural, mas não é o mesmo
  conceito que "colaborador em viagem" — pode haver despesa em moeda estrangeira
  sem viagem (assinatura de SaaS cobrada em USD) e viagem sem moeda estrangeira
  (voo doméstico pago em BRL). O comunicado não conecta os dois explicitamente
  em nenhuma frase.
  Opções: (a) manter RN-016 inerte — moeda não é evidência de viagem, é só uma
  unidade monetária; (b) ativar a ampliação de 50% para qualquer item em moeda
  estrangeira; (c) ativar a ampliação de 50% para todos os itens do colaborador
  no dia em que houver ao menos um item em moeda estrangeira; (d) ativar para
  todo o período de apuração se houver qualquer item em moeda estrangeira.
  Impacto: para Rafael (CC-COMERCIAL), sob (b), o limite de alimentacao passa de
  90 para 135 em dias com item EUR/USD — isso mudaria e-002 de PARCIAL 90,00
  para INTEGRAL ~130,46 (+40,46), e mudanças similares em outros itens em moeda
  estrangeira. Sob (c)/(d) o escopo se alarga ainda mais (potencialmente
  afetando e-001, em BRL, se caísse no mesmo dia de um item estrangeiro — o que
  não ocorre nos dados atuais, mas ocorreria em outros). A escolha muda o total
  em dezenas a centenas de reais dependendo do escopo.
  Recomendação técnica: (a) — o princípio transversal já registrado em
  DECISIONS.md (D-001, princípio 2: "campo estruturado ausente torna a regra
  inerte, não inventada") se aplica por analogia: moeda é estruturado, mas não é
  o campo que a política pede (ela pede uma condição de "estar em viagem", não
  uma moeda). Inventar essa ponte seria decisão de produto disfarçada de leitura
  técnica.
  Pergunta: a presença de moeda diferente de BRL em uma despesa deve ativar a
  ampliação de 50% do item 6 da política — e, se sim, com que escopo (só aquele
  item, aquele dia, ou todo o período)?

  AMB-ENV-06 — Data da despesa sem cotação exata

  Trecho: cambio.json só publica dias úteis; despesa e-004 (Rafael) é datada
  2026-07-18, um sábado, sem entrada em cambio.json (a mais próxima anterior é
  2026-07-17, a mais próxima posterior é 2026-07-20).
  Por que é ambíguo: "a taxa da data da despesa" pressupõe que sempre existe uma
  cotação exata — o próprio arquivo de câmbio ("observacao": "Cotacoes 
  publicadas apenas em dias uteis bancarios") mostra que isso é falso por
  construção.
  Opções: (a) usar a cotação do último dia útil anterior disponível (convenção
  financeira usual — é o que "PTAX de fechamento" sugere: a cotação de sexta
  continua valendo no fim de semana); (b) usar a próxima cotação disponível; (c)
  recusar o item (motivo novo, ex. COTACAO_CAMBIAL_AUSENTE), sem inventar taxa;
  (d) invalidar o arquivo inteiro (erro global).
  Impacto (calculado): e-004, EUR 30,00 — (a) taxa 5,96 → R$178,80; (b) taxa
  6,01 → R$180,30; ambos ultrapassam o teto de alimentacao (90,00) e resultam no
  mesmo valor_reembolsavel de R$90,00 PARCIAL neste caso específico (a
  diferença entre (a)/(b) não muda o resultado aqui, mas mudaria em outros
  valores); (c) resulta em R$0,00 RECUSADO — diferença de R$90,00 em relação a
  (a)/(b); (d) invalida as 10 despesas do arquivo inteiro de Rafael.
  Recomendação técnica: (a) — é a leitura financeira convencional de "cotação de
  fechamento" e a mais próxima do texto ("a taxa da data da despesa" lido como
  "a taxa vigente naquela data", não "a taxa publicada exatamente naquele dia");
  (d) é desproporcional (um item sem cotação não deveria invalidar despesas em
  BRL do mesmo arquivo).
  Pergunta: na ausência de cotação exata para a data da despesa, usa-se a
  cotação do último dia útil anterior disponível, a próxima disponível, ou o
  item é recusado por falta de dado?

  AMB-ENV-07 — Moeda sem qualquer cotação

  Trecho: despesa e-006 (Rafael) está em GBP; cambio.json só tem USD e EUR em
  todas as datas — GBP não aparece em nenhuma entrada.
  Por que é ambíguo: distinto de AMB-ENV-06 — aqui não existe nenhuma cotação
  para interpolar, em nenhuma data.
  Opções: (a) recusar o item com motivo novo (ex. MOEDA_SEM_COTACAO),
  valor_normalizado nulo, análogo ao que já acontece hoje quando despesa.valor é
  estruturalmente inválido; (b) tratar como erro global (arquivo inteiro
  inválido); (c) presumir 1:1 com BRL como fallback silencioso (rejeitada por
  todos os princípios já registrados na spec contra inferência silenciosa).
  Impacto: e-006 é R$0,00 sob (a) ou (b) de qualquer forma para aquele item —
  mas (b) derruba as outras 9 despesas de Rafael. (c) seria uma corrupção de
  dado grave e contraria diretamente o princípio "não presume valores padrão" já
  em §3 da spec.
  Recomendação técnica: (a) — mesmo padrão arquitetural de "campo cujo valor
  dependente não pôde ser calculado → regras que dependem dele não rodam"
  (matriz 8.2), só que a causa é ausência de dado externo, não erro estrutural
  do item.
  Pergunta: moeda sem cotação em cambio.json deve recusar apenas o item (motivo
  novo) ou invalidar o arquivo inteiro?

  AMB-ENV-08 — Ordem da conversão e do arredondamento

  Trecho: RN-004 normaliza para 2 casas com HALF_UP; o comunicado B não diz se a
  conversão acontece antes ou depois disso.
  Por que é ambíguo: "valor normalizado" hoje significa uma coisa (BRL, 2
  casas); com conversão, é preciso decidir se primeiro converte (moeda
  estrangeira × taxa) e depois arredonda o resultado em BRL, ou se arredonda o
  valor estrangeiro primeiro e converte depois.
  Opções: (a) converter primeiro (valor bruto na moeda original × taxa), depois
  arredondar o resultado em BRL para 2 casas — um único arredondamento; (b)
  arredondar o valor original na moeda estrangeira para 2 casas primeiro, depois
  converter, depois arredondar de novo.
  Impacto: diferença tipicamente de centavos, mas determinística — ex. e-002
  (EUR 22,00 × 5,93 = 130,46 exato) não diverge aqui porque o valor original já
  tem 2 casas: mas para valores com 3+ casas nas moedas de origem, (a) e (b)
  divergiriam.
  Recomendação técnica: (a) — um único arredondamento no final evita erro de
  arredondamento composto (double rounding), e é consistente com o princípio de
  RN-004 de que "todas as regras posteriores usam o valor normalizado" —
  normalização é o último passo antes de qualquer regra usar o número, não um
  passo intermediário repetido.
  Pergunta: confirma que a conversão cambial acontece sobre o valor bruto na
  moeda original, com arredondamento para 2 casas aplicado uma única vez, sobre
  o resultado já em BRL?

  AMB-ENV-09 — Uso do valor convertido no gatilho de nota fiscal

  Trecho: "Nota fiscal é obrigatória acima de R$100" (regra existente, RN-009);
  comunicado B diz "os limites da política são sempre em BRL... convertida antes
  de ser comparada ao limite" — mas fala de limites de categoria, não nomeia
  explicitamente o gatilho de nota fiscal.
  Por que é ambíguo: o gatilho de R$100 é tecnicamente um campo separado
  (nota_fiscal_obrigatoria_acima_de) em politica-v4.json, fora dos blocos
  padrao/centros_custo — dá para ler "os limites" como não incluindo-o.
  Opções: (a) o gatilho de nota fiscal compara o valor convertido (BRL); (b)
  compara o valor original na moeda estrangeira, sem conversão.
  Impacto (calculado): e-005 (Rafael), USD 40,00, convertido R$220,00, sem nota
  fiscal. Sob (a): 220,00 > 100 e sem nota → RECUSADO, R$0,00. Sob (b): 40,00 <
  100 → elegível, segue para teto de transporte (150,00 no CC-COMERCIAL) →
  convertido 220,00 > 150 → PARCIAL R$150,00. Diferença de R$150,00 num único 
  item, isolada e clara nos dados do envelope — exatamente o tipo de caso que a
  correção provavelmente usa para testar essa decisão.
  Recomendação técnica: (a) — mesmo raciocínio de AMB-005 já registrado na spec
  ("a nota documenta a despesa como ocorreu"; aqui, a despesa "ocorreu" em BRL
  do ponto de vista da empresa que reembolsa em BRL) e coerência com a mesma
  comparação usada nos tetos de categoria.
  Pergunta: o gatilho de R$100 para nota fiscal compara o valor convertido em
  BRL ou o valor original na moeda da despesa?

  AMB-ENV-10 — Chave de duplicidade entre valores em moedas diferentes

  Trecho: RN-010 usa data, categoria, valor normalizado, fornecedor, descricao
  como recebidos. Nunca prevê moeda.
  Por que é ambíguo: com conversão, dois itens em moedas diferentes podem
  coincidir por acaso no valor convertido (falso positivo de duplicidade); e
  dois itens na mesma moeda e mesmo valor original, mas com taxas diferentes por
  causa de datas diferentes, teriam valores convertidos diferentes (não
  detectados como duplicata mesmo sendo economicamente idênticos, se a data
  também mudasse — mas nesse caso data já diferiria, então nem seriam
  candidatos).
  Opções: (a) incluir moeda na chave e usar o valor normalizado na moeda 
  original (antes da conversão) como componente de valor — dois lançamentos só
  colidem se forem literalmente a mesma transação relatada duas vezes, na mesma
  moeda; (b) manter a chave como está, usando o valor convertido em BRL, sem
  incluir moeda — risco de colisão espúria entre moedas diferentes.
  Impacto: não observável nos dois arquivos do envelope (nenhum par de itens
  colide sob nenhuma das duas leituras nos dados fornecidos), mas é um risco
  real de falso positivo/negativo em dados futuros.
  Recomendação técnica: (a) — duplicidade é sobre "o mesmo lançamento relatado
  duas vezes"; comparar por valor convertido introduziria dependência da taxa de
  câmbio numa regra que deveria ser sobre identidade do lançamento, não sobre
  seu resultado financeiro.
  Pergunta: a chave de duplicidade passa a incluir moeda, comparando o valor na
  moeda original (não o convertido)?

  AMB-ENV-11 — Campos necessários na saída para auditoria da conversão

  Trecho: nenhum — a spec atual (4.3) não tem qualquer campo relacionado a moeda
  ou câmbio.
  Por que é ambíguo: a spec não define esquema de saída para isso, e não pode
  ser inferido do comunicado.
  Opções (não mutuamente exclusivas): moeda informada; taxa_cambio_aplicada;
  data_cotacao_utilizada (relevante especialmente pela decisão de AMB-ENV-06,
  para tornar auditável qual data de fallback foi usada); valor_convertido (se
  diferente de valor_normalizado por alguma razão de arredondamento).
  Impacto: sem esses campos, um resultado de item convertido é opaco — o
  financeiro não consegue conferir a conta sem reabrir cambio.json manualmente,
  o que contraria o objetivo §2 da spec ("cada decisão seja rastreável até uma
  regra escrita").
  Recomendação técnica: adicionar ao registro de item, quando moeda != BRL:
  moeda, taxa_cambio_aplicada, data_cotacao_utilizada. Não duplicar
  valor_normalizado como valor_convertido — são o mesmo número sob a decisão de
  AMB-ENV-08.
  Pergunta: quais desses campos de auditoria devem entrar na saída, e com que
  nome?

  AMB-ENV-12 — Validação de vigencia

  Trecho: politica-v4.json tem "vigencia": "2026-07-01". Comunicado: "Vigência
  imediata, retroativa à competência atual."
  Por que é ambíguo: não existe arquivo de política anterior à v4 no sistema (a
  v3 sempre foi hardcoded) — não há "outra política" para aplicar antes da
  vigência, então o campo pode ser puramente informativo ou pode ser uma
  validação estrutural sem efeito prático hoje.
  Opções: (a) campo ignorado — informativo, nunca validado, como
  periodo.competencia hoje; (b) validar que periodo.inicio do envelope ≥
  vigencia da política, e recusar (erro global, tipo RN-001) senão; (c) validar
  por item — item com data anterior à vigencia seria recusado individualmente.
  Impacto: nos dois arquivos do envelope, periodo.inicio é 2026-07-01,
  exatamente igual a vigencia — não força a decisão pelos dados fornecidos, mas
  qualquer prova oculta poderia usar uma data anterior.
  Recomendação técnica: (a) — mesmo tratamento de periodo.competencia hoje
  (rótulo informativo, RN-001): não há política alternativa definida para "antes
  da vigência", então validar contra ela seria uma regra sem consequência
  definida — recusaria ou invalidaria sem que a spec diga o que fazer com o item
  nesse caso.
  Pergunta: vigencia é um campo informativo (nunca validado) ou deve ser
  verificado contra periodo/data — e, se sim, com que consequência?

  AMB-ENV-13 — Erros de estrutura nos arquivos externos

  Trecho: nenhum — o comunicado não antecipa arquivo malformado.
  Por que é ambíguo: politica-v4.json/cambio.json inválidos (JSON sintaticamente
  quebrado, chave obrigatória ausente, tipo errado) não têm classificação de
  erro definida — hoje só --input tem esse tratamento (exit 2).
  Opções: (a) tratar como infraestrutura, reaproveitando exit 2 (mesma classe de
  "arquivo ilegível" que já existe para --input); (b) criar exit code(s)
  dedicado(s) (ex. 4 para política, 5 para câmbio), separando diagnóstico.
  Impacto: puramente de qualidade de diagnóstico — não muda nenhum valor
  financeiro, mas afeta testabilidade e mensagem de erro.
  Recomendação técnica: (b) — o contrato de CLI já usa códigos numéricos para
  distinguir classes de falha (2 infraestrutura, 3 envelope); manter essa
  granularidade evita que um erro de política pareça um erro de leitura de
  --input.
  Pergunta: arquivo de política ou câmbio malformado usa o exit code 2
  existente, ou ganha código(s) próprio(s)?

  AMB-ENV-14 — Classificação entre erro global e recusa individual

  Trecho: decorre de AMB-ENV-06, 07 e 13 combinadas — não há uma frase única no
  comunicado que resolva isso de uma vez.
  Por que é ambíguo: o sistema hoje já tem essa distinção (item malformado =
  recusa individual; envelope malformado = erro global, RN-001) mas ela nunca
  precisou lidar com uma dependência de dado externo por item (câmbio) antes.
  Opções: já cobertas item a item em AMB-ENV-06/07 (recomendação: recusa
  individual para dado de câmbio ausente por item) e AMB-ENV-13 (recomendação:
  erro global/infraestrutura para arquivo externo malformado). Este item existe
  para deixar explícita a régua geral: arquivo externo malformado = global; dado
  de negócio ausente para um item específico (data sem cotação, moeda sem 
  cotação) = individual, espelhando a distinção já estabelecida entre RN-001
  (envelope) e RN-002 (item).
  Impacto: consistência arquitetural — evita decisões ad hoc regra a regra.
  Recomendação técnica: formalizar essa régua na spec 1.2 como princípio
  transversal, do mesmo jeito que os "três princípios transversais" de D-001 em
  DECISIONS.md.
  Pergunta: confirma a régua "arquivo externo malformado = erro global; dado
  externo ausente para um item específico = recusa individual", como
  consequência das decisões de AMB-ENV-06/07/13?

  AMB-ENV-15 — Manutenção da regressão completa da política anterior

  Trecho: implícito no processo descrito no próprio envelope ("Registre... quais
  requisitos foram invalidados") e no princípio geral de que spec que muda
  precisa de rastreabilidade — não há frase que descarte a regressão do Dia 1.
  Por que é ambíguo: ver cálculo completo no §8 — exemplos/despesas-exemplo.json
  usa CC-ENG-PLATAFORMA, que tem entrada na tabela v4, e essa entrada muda o
  resultado de R$585,43 para R$351,43 se a política por centro de custo for
  aplicada de forma consistente e retroativa.
  Opções: (a) ExemploCompletoTest é atualizado para esperar R$351,43, com nota
  explícita em DECISIONS.md de que o total mudou por decisão de política (não
  por bug); (b) o fixture original é preservado como está (R$585,43), processado
  deliberadamente com a política padrão (não a específica de CC-ENG-PLATAFORMA)
  — ou seja, o centro de custo do fixture histórico é tratado como fora do
  escopo da nova tabela por decisão de congelamento de baseline; (c) as duas
  coisas coexistem como testes distintos (um "legado, política padrão", outro
  "v4, política por CC").
  Impacto: é a decisão de maior impacto financeiro isolado desta auditoria —
  R$234,00 em um único arquivo de 14 itens. Numa avaliação real, errar essa
  decisão silenciosamente (deixar o código divergir do fixture sem registrar por
  quê) é exatamente o tipo de falha que o processo SDD deste projeto foi
  desenhado para capturar.
  Recomendação técnica: (c) — preserva o valor histórico como documentação viva
  do comportamento "política padrão" (que continua existindo e sendo testável),
  e adiciona a asserção nova como prova de que a resolução por centro de custo
  funciona — sem escolher entre "manter" e "atualizar" o fixture original, que é
  uma falsa dicotomia técnica escondendo uma decisão de produto.
  Pergunta: exemplos/despesas-exemplo.json deve ser reprocessado com a política
  específica de CC-ENG-PLATAFORMA (total passa a R$351,43) ou o teste de
  regressão do Dia 1 é preservado deliberadamente sob a política padrão, com a
  divergência documentada?

  ---
  Achados adicionais (fora da lista obrigatória, mas materiais)

  - representacao é exclusiva de quem tem entrada, ou reembolsável globalmente 
  com fallback indefinido? O comunicado diz "CC-COMERCIAL tem uma categoria
  nova... que não existia na v3" — leitura natural é que representacao só é
  reembolsável para quem a define explicitamente. Sob essa leitura, f-003 (Dani,
  CC-SUPORTE-N2, representacao R$190,00) é CATEGORIA_FORA_POLITICA, R$0,00. Sob
  a leitura alternativa (categoria válida globalmente, sem teto quando ausente
  do CC = paga integral), seria R$190,00 INTEGRAL — diferença de R$190,00 num
  único item, e o arquivo de Dani parece desenhado exatamente para forçar essa
  pergunta. Recomendo a primeira leitura pelo mesmo motivo de AMB-ENV-02.
  - Comparação de centro_custo contra a tabela é exata ou normalizada? RN-005
  normaliza categoria (trim/caixa/acento) mas a spec já registra explicitamente
  (AMB-015/ESCOPO) que essa normalização não se estende a outros campos de
  texto. Por precedente, recomendo comparação exata (sensível a caixa/espaço)
  para centro_custo — mas vale uma frase explícita na spec 1.2 para não deixar
  implícito.

  ---
  Cálculo dos dois arquivos do envelope sob a interpretação recomendada

  Premissas: conversão antes do arredondamento único (AMB-ENV-08a); cotação
  ausente usa o dia útil anterior mais próximo (AMB-ENV-06a); moeda sem cotação
  recusa o item (AMB-ENV-07a); gatilho de nota fiscal compara valor convertido
  (AMB-ENV-09a); moeda não ativa RN-016 (AMB-ENV-05a); representacao só é
  reembolsável com entrada explícita no CC (achado adicional).

  despesas-envelope.json (Rafael, CC-COMERCIAL: alimentação 90/dia, transporte 
  150/dia, hospedagem 400/diária, representação 300/dia)

  Item: e-001
  Categoria: representacao
  Moeda/valor: BRL 340,00
  Convertido (BRL): —
  Decisão: PARCIAL (teto 300)
  Reembolsável: 300,00
  ────────────────────────────────────────
  Item: e-002
  Categoria: alimentacao
  Moeda/valor: EUR 22,00 @5,93
  Convertido (BRL): 130,46
  Decisão: PARCIAL (teto 90)
  Reembolsável: 90,00
  ────────────────────────────────────────
  Item: e-003
  Categoria: alimentacao
  Moeda/valor: EUR 14,50 @5,88
  Convertido (BRL): 85,26
  Decisão: INTEGRAL
  Reembolsável: 85,26
  ────────────────────────────────────────
  Item: e-004
  Categoria: alimentacao
  Moeda/valor: EUR 30,00 @5,96 (fallback 17/07)
  Convertido (BRL): 178,80
  Decisão: PARCIAL (teto 90)
  Reembolsável: 90,00
  ────────────────────────────────────────
  Item: e-005
  Categoria: transporte_urbano
  Moeda/valor: USD 40,00 @5,50
  Convertido (BRL): 220,00
  Decisão: RECUSADO (nota ausente, convertido>100)
  Reembolsável: 0,00
  ────────────────────────────────────────
  Item: e-006
  Categoria: representacao
  Moeda/valor: GBP 55,00 (sem cotação)
  Convertido (BRL): —
  Decisão: RECUSADO (câmbio ausente)
  Reembolsável: 0,00
  ────────────────────────────────────────
  Item: e-007
  Categoria: hospedagem
  Moeda/valor: BRL 1200,00
  Convertido (BRL): —
  Decisão: PARCIAL (teto 400)
  Reembolsável: 400,00
  ────────────────────────────────────────
  Item: e-008
  Categoria: alimentacao
  Moeda/valor: BRL 95,00
  Convertido (BRL): —
  Decisão: PARCIAL (teto 90)
  Reembolsável: 90,00
  ────────────────────────────────────────
  Item: e-009
  Categoria: coworking
  Moeda/valor: BRL 120,00
  Convertido (BRL): —
  Decisão: RECUSADO (fora política)
  Reembolsável: 0,00
  ────────────────────────────────────────
  Item: e-010
  Categoria: alimentacao
  Moeda/valor: BRL 88,00 (sem moeda)
  Convertido (BRL): —
  Decisão: INTEGRAL
  Reembolsável: 88,00

  Total Rafael: R$ 1.143,26

  despesas-envelope-cc-desconhecido.json (Dani, CC-SUPORTE-N2 → política padrão:
  alimentação 60/dia, transporte 80/dia, hospedagem 250/diária)

  Item: f-001
  Categoria: alimentacao
  Moeda/valor: BRL 58,00
  Convertido: —
  Decisão: INTEGRAL
  Reembolsável: 58,00
  ────────────────────────────────────────
  Item: f-002
  Categoria: hospedagem
  Moeda/valor: BRL 310,00
  Convertido: —
  Decisão: PARCIAL (teto 250)
  Reembolsável: 250,00
  ────────────────────────────────────────
  Item: f-003
  Categoria: representacao
  Moeda/valor: BRL 190,00
  Convertido: —
  Decisão: RECUSADO (fora política p/ este CC)
  Reembolsável: 0,00
  ────────────────────────────────────────
  Item: f-004
  Categoria: transporte_urbano
  Moeda/valor: USD 12,00 @5,48
  Convertido: 65,76
  Decisão: INTEGRAL
  Reembolsável: 65,76

  Total Dani: R$ 373,76

  Onde a interpretação alternativa muda o resultado (mesmos arquivos):

  ┌───────────────────────┬─────────────────────┬───────────────────┬───────┐
  │      Ambiguidade      │   Interpretação     │   Item afetado    │ Delta │
  │                       │     alternativa     │                   │       │
  ├───────────────────────┼─────────────────────┼───────────────────┼───────┤
  │ AMB-ENV-06 (recusa em │ e-004 recusado por  │                   │       │
  │  vez de fallback)     │ falta de cotação    │ Rafael −R$90,00   │       │
  │                       │ exata               │                   │       │
  ├───────────────────────┼─────────────────────┼───────────────────┼───────┤
  │ AMB-ENV-09 (gatilho   │ e-005 elegível,     │                   │       │
  │ compara valor         │ paga teto de        │ Rafael +R$150,00  │       │
  │ original)             │ transporte          │                   │       │
  ├───────────────────────┼─────────────────────┼───────────────────┼───────┤
  │ representacao global  │                     │                   │       │
  │ sem teto quando CC    │ f-003 paga integral │ Dani +R$190,00    │       │
  │ não define            │                     │                   │       │
  ├───────────────────────┼─────────────────────┼───────────────────┼───────┤
  │ AMB-ENV-05 (moeda     │                     │ Rafael +R$40,46   │       │
  │ ativa viagem, escopo  │ e-002 vira INTEGRAL │ (mínimo; escopo   │       │
  │ por item)             │  (teto 90→135)      │ maior amplia      │       │
  │                       │                     │ mais)             │       │
  └───────────────────────┴─────────────────────┴───────────────────┴───────┘

  Nenhuma interpretação alternativa testada muda a classificação de e-006 (GBP)
  — está sempre recusado, porque não existe cotação de GBP em nenhuma data do
  arquivo.

  ---
  10. Proposta preliminar de tasks (a partir de T-022, sem editar tasks.md)

  - T-022 — docs(spec): spec 1.2 incorporando bloco RH v4, ambiguidades
  AMB-ENV-01 a 17, e a régua explícita de "padrão universal" vs "resolvido por
  centro de custo".
  - T-023 — docs(tasks)/DECISIONS.md: registrar D-003 com todas as RNs afetadas
  e o achado de regressão (R$585,43 → R$351,43 sob CC-ENG-PLATAFORMA).
  - T-024 — Leitor de política externa (politica-v4.json): estrutura padrao +
  centros_custo, validação estrutural do arquivo.
  - T-025 — Leitor de câmbio externo (cambio.json) + resolução de taxa por
  moeda/data com a regra de fallback decidida.
  - T-026 — CLI: argumentos --politica/--cambio, exit code(s) para arquivo
  externo malformado/ausente.
  - T-027 — Campo despesa.moeda: validação estrutural (opcional, default BRL),
  novo CampoCanonico.
  - T-028 — Conversão cambial no pipeline (ordem: converter → arredondar uma
  vez) + campos de auditoria na saída (moeda, taxa aplicada, data de cotação
  usada).
  - T-029 — Resolução de política por centro de custo (lookup com fallback para
  padrao; ausente/nulo/desconhecido → padrao).
  - T-030 — Categoria representacao: nova regra de teto diário, condicionada a
  entrada explícita no CC.
  - T-031 — Teto de hospedagem igual a zero: remover a guarda que hoje lança
  exceção em AgregadorTetoDiario.aplicarCorte, com o comportamento decidido em
  AMB-ENV-04.
  - T-032 — Motivo(s) novos para falha de câmbio (data sem cotação exata tratada
  por fallback; moeda sem cotação alguma → recusa) + nova RN.
  - T-033 — Regressão: decisão de AMB-ENV-15 materializada — atualizar e/ou
  duplicar ExemploCompletoTest conforme escolhido.
  - T-034 — Testes de integração dos dois arquivos do envelope, com fixtures
  esperados (as tabelas acima, sob a interpretação escolhida).
  - T-035 (opcional, item C) — Estado AGUARDANDO_APROVACAO para
  valor_reembolsavel > R$500 — nenhum item dos dois arquivos do envelope
  ultrapassa R$500 sob a interpretação recomendada, então não é forçado pelos
  dados atuais.

  ---

  Ran 1 shell command (ctrl+o to expand)

● Confirmado:

  - Nenhum arquivo existente foi modificado (git status --short mostra só o
  mesmo estado do início da sessão).
  - Só exemplos/envelope/ aparece como conteúdo novo — nenhum outro caminho.
  - Nenhum commit foi criado.

  Parando aqui para revisão, conforme pedido.

✻ Worked for 10m 10s

※ recap: We're auditing how the Day 2 policy envelope (cost-center limits, foreign currency) breaks the reimbursement engine's spec, code, and R$585.43 regression baseline, in analysis-only mode with no files changed. I've delivered the full report with ambiguities and a preliminary task list; next step is waiting for your decisions on the flagged open questions. (disable recaps in /config)

> Vamos executar somente a atualização normativa do envelope do Dia 2.          
                                                                                
  Nesta rodada, altere exclusivamente:                                          
                                                                                
  * `specs/001-motor-reembolso/spec.md`                                         
  * `specs/001-motor-reembolso/DECISIONS.md`                                    
                                                                                
  Os cinco arquivos oficiais já estão em `exemplos/envelope/` e devem           
  permanecer intactos.                                                          
                                                                                
  Não altere:                                                                   
                                                                                
  * `plan.md`;                                                                  
  * `tasks.md`;                                                                 
  * `README.md`;                                                                
  * código de produção;                                                         
  * testes;                                                                     
  * fixtures existentes;                                                        
  * arquivos do envelope.                                                       
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  ## 1. Atualização geral da spec                                               
                                                                                
  Atualize a spec:                                                              
                                                                                
  * versão `1.1` → `1.2`;                                                       
  * status para `em revisão`;                                                   
  * data da atualização: `2026-08-05`;                                          
  * origem da mudança: envelope oficial do Dia 2, política v4.                  
                                                                                
  Não renumere nem remova:                                                      
                                                                                
  * `AMB-001` a `AMB-018`;                                                      
  * `RN-001` a `RN-018`;                                                        
  * `CA-001` a `CA-023`.                                                        
                                                                                
  As novas ambiguidades devem continuar a numeração existente, começando em     
  `AMB-019`.                                                                    
                                                                                
  As novas regras devem começar em `RN-019`.                                    
                                                                                
  Os novos critérios de aceite devem começar em `CA-024`.                       
                                                                                
  Regras e critérios antigos que mudaram não devem desaparecer silenciosamente: 
  atualize seus textos e registre em `DECISIONS.md` o comportamento anterior    
  invalidado.                                                                   
                                                                                
  ## 2. Fontes oficiais                                                         
                                                                                
  A spec deve citar como fontes do envelope:                                    
                                                                                
  * `exemplos/envelope/00-ENVELOPE-LACRADO.md`;                                 
  * `exemplos/envelope/politica-v4.json`;                                       
  * `exemplos/envelope/cambio.json`;                                            
  * `exemplos/envelope/despesas-envelope.json`;                                 
  * `exemplos/envelope/despesas-envelope-cc-desconhecido.json`.                 
                                                                                
  Registre que esses arquivos são artefatos oficiais e não devem ser            
  modificados.                                                                  
                                                                                
  ## 3. Política externa por centro de custo                                    
                                                                                
  Documente:                                                                    
                                                                                
  * a política deixa de ser hardcoded;                                          
  * a execução recebe um arquivo externo de política;                           
  * o bloco `padrao` representa a política completa de fallback;                
  * `centros_custo` contém políticas específicas;                               
  * comparação de `colaborador.centro_custo` é textual e exata, sem trim,       
  caixa, acento ou aproximação;                                                 
  * centro desconhecido, ausente, nulo ou de tipo inválido usa integralmente    
  `padrao`;                                                                     
  * centro cadastrado usa somente as categorias declaradas nele;                
  * categoria ausente em centro cadastrado não faz fallback por categoria: é    
  não reembolsável;                                                             
  * `representacao` só é reembolsável em centros que a declaram explicitamente; 
  * limite explicitamente igual a `0.00`, acompanhado da indicação “não         
  reembolsável”, recusa o item por política do centro de custo.                 
                                                                                
  Crie vocabulário canônico para esse último caso:                              
                                                                                
  ```text                                                                       
  codigo: CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO                               
  decisao: RECUSADO                                                             
  valor_reembolsavel: 0.00                                                      
  campo: null                                                                   
  ```                                                                           
                                                                                
  Não classifique limite zero c                                                 
  ──── (280 lines hidden) ───────────────────────────────────────────────────── 
  mo fora de escopo:                                                            
                                                                                
  * não implementar `AGUARDANDO_APROVACAO`;                                     
  * não implementar fila manual;                                                
  * não alterar decisões por valor acima de R$ 500;                             
  * justificativa: item C é explicitamente opcional e o escopo escolhido        
  prioriza os requisitos obrigatórios A e B.                                    
                                                                                
  ## 16. DECISIONS.md                                                           
                                                                                
  Crie no topo uma nova entrada:                                                
                                                                                
  ```text                                                                       
  D-003 — Envelope do Dia 2 e política v4 · 2026-08-05                          
  ```                                                                           
                                                                                
  Ela deve registrar honestamente:                                              
                                                                                
  * gatilho da mudança;                                                         
  * arquivos oficiais recebidos;                                                
  * comportamento anterior invalidado;                                          
  * ambiguidades identificadas;                                                 
  * decisões dos quatro grupos;                                                 
  * RN e CA antigos afetados;                                                   
  * novas RN/CA criadas;                                                        
  * DT-007 invalidada;                                                          
  * regressão de `585.43` para `351.43` sob a v4;                               
  * preservação paralela da baseline histórica;                                 
  * item C deliberadamente fora do escopo;                                      
  * custo documental e técnico previsto;                                        
  * tasks afetadas ainda não definidas, pois `plan.md` e `tasks.md` serão       
  atualizados em etapas posteriores.                                            
                                                                                
  Não reescreva D-001 ou D-002, salvo correção de links internos estritamente   
  necessária. A ordem permanece cronológica inversa.                            
                                                                                
  ## 17. Consistência obrigatória                                               
                                                                                
  Revise integralmente:                                                         
                                                                                
  * cabeçalho e contagens;                                                      
  * problema e objetivo;                                                        
  * fora de escopo;                                                             
  * contrato de entrada;                                                        
  * contrato de saída;                                                          
  * vocabulários;                                                               
  * regras;                                                                     
  * ambiguidades;                                                               
  * casos de borda;                                                             
  * ordem do pipeline;                                                          
  * ordem de motivos;                                                           
  * matriz regra × campos;                                                      
  * critérios de aceite;                                                        
  * resultado dos exemplos;                                                     
  * perguntas em aberto;                                                        
  * rastreabilidade.                                                            
                                                                                
  Não deixe afirmações antigas contradizendo a v4, como:                        
                                                                                
  * “somente BRL”;                                                              
  * “centro de custo não influencia regras”;                                    
  * “política possui quatro valores fixos no código”;                           
  * “moeda é campo desconhecido ignorado”;                                      
  * “CLI exige apenas input e output”;                                          
  * “o exemplo sempre totaliza 585.43”.                                         
                                                                                
  ## Verificação                                                                
                                                                                
  Depois execute:                                                               
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * somente `spec.md` e `DECISIONS.md` foram modificados;                       
  * `exemplos/envelope/` continua como conteúdo novo e intacto;                 
  * `plan.md`, `tasks.md`, README, código e testes não foram alterados;         
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Ao final, apresente:                                                          
                                                                                
  * versão e status novos da spec;                                              
  * contagem final de AMB, RN e CA;                                             
  * lista das novas ambiguidades, regras e critérios;                           
  * resumo de D-003;                                                            
  * seções alteradas;                                                           
  * resultado de `git status --short`;                                          
  * qualquer conflito ou decisão que não tenha conseguido representar sem       
  inventar requisito.                                                           
                                                                                
  Pare para revisão.                                                            

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1  # Spec — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.1 · **Status:** aprovada · **Última alteração:** 2
        -026-07-30                                                       
      3 +**Versão:** 1.2 · **Status:** em revisão · **Última alteração:**
        + 2026-08-05                                                     
      4  
      5  > **Regra de ouro deste arquivo:** ele descreve o QUÊ e o PORQUÊ
         . Nenhuma linha
      6  > aqui pode citar linguagem, biblioteca, classe, função ou estru
         tura de pasta.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 14 lines
      11  
      12  ---
      13  
      14 +## 0. Fontes oficiais do Dia 2                                 
      15 +                                                               
      16 +Esta versão incorpora o envelope oficial do Dia 2 — política de
         + reembolso v4 —, recebido em `2026-08-05`. As fontes normativas
         + dessa mudança são exclusivamente os cinco arquivos abaixo, em 
         +`exemplos/envelope/`, e são artefatos oficiais: não devem ser m
         +odificados por este ou por qualquer processo de especificação s
         +ubsequente.                                                    
      17 +                                                               
      18 +- `exemplos/envelope/00-ENVELOPE-LACRADO.md` — comunicado do RH
         +, política v4.                                                 
      19 +- `exemplos/envelope/politica-v4.json` — tabela de limites por 
         +centro de custo.                                               
      20 +- `exemplos/envelope/cambio.json` — taxas de câmbio por data.  
      21 +- `exemplos/envelope/despesas-envelope.json` — colaborador com 
         +centro de custo cadastrado e despesas em moeda estrangeira (Raf
         +ael Nkemelu, `CC-COMERCIAL`).                                  
      22 +- `exemplos/envelope/despesas-envelope-cc-desconhecido.json` — 
         +colaborador com centro de custo fora da tabela de política (Dan
         +i Okonkwo, `CC-SUPORTE-N2`).                                   
      23 +                                                               
      24 +Onde o texto desta spec e o conteúdo desses arquivos divergirem
         +, os arquivos são a fonte da verdade: a spec está errada e deve
         + ser corrigida, com o registro correspondente em `DECISIONS.md`
         +.                                                              
      25 +                                                               
      26 +---                                                            
      27 +                                                               
      28  ## 1. Problema
      29  
      30  Hoje o financeiro confere despesa por despesa contra a política
           de reembolso, manualmente. O processo é lento e produz resulta
          dos diferentes para casos iguais, porque a política v3 é ambígu
          a em pontos que mudam o valor pago — e cada conferente resolve 
          a ambiguidade do seu jeito, sem registrar qual leitura adotou. 
          O custo não é só o tempo: é a impossibilidade de explicar ao co
          laborador por que a despesa dele foi cortada.

● Agora a seção 3 (fora de escopo):

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      42  - **Não detecta fragmentação de hospedagem** por descrição, for
          necedor ou proximidade de datas. Uma estadia lançada em vários 
          itens recebe um teto por item.
      43  - **Não processa créditos, estornos ou compensações.** Valores 
          não positivos são recusados e não abatem nada. (AMB-013 · RN-00
          6)
      44  - **Não distingue contexto de consumo:** alimentação em fim de 
          semana, plantão, com cliente, de representação, ou consumida de
          ntro da hospedagem seguem exclusivamente a regra geral da categ
          oria. Os três casos existem no arquivo de exemplo e foram ident
          ificados.
      45 -- **Não converte nem identifica moedas.** Todos os valores são 
         -interpretados como reais brasileiros (BRL).                    
      45 +- **Não infere condição de viagem a partir da moeda da despesa.
         +** `despesa.moeda` diferente de `BRL` (política v4, Dia 2) é so
         +mente a unidade monetária do lançamento — não é evidência de de
         +slocamento do colaborador, e não ativa a ampliação de 50% do it
         +em 6 da política. RN-016 continua sem efeito (AMB-023).        
      46  - **Não agrega entre arquivos.** A apuração ocorre dentro de um
          a única entrada; `colaborador.nome` e `colaborador.centro_custo
          ` nunca são usados para cruzar dados.
      47  - **Não usa `periodo.competencia`** para elegibilidade temporal
          . (AMB-009)
      48  - **Não faz correspondência aproximada de categoria:** sem corr
          eção ortográfica, sinônimos ou substituição de espaço por subli
          nhado. `transporte urbano` não se torna `transporte_urbano`. (A
          MB-015)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines
      51  - **Não coage tipos.** `"72,50"`, `"31/07/2026"` e `"sim"` não 
          são convertidos.
      52  - **Não presume valores padrão** para campos obrigatórios ausen
          tes.
      53  - **Não reage a campos desconhecidos.** Um campo fora do contra
          to — inclusive um eventual `em_viagem` — é ignorado e não ativa
           comportamento algum.
      54 +- **Não implementa fila de aprovação manual.** O item C (opcion
         +al) do comunicado do Dia 2 — pendência de aprovação de gestor p
         +ara itens com reembolsável acima de R$500 — está deliberadament
         +e fora de escopo nesta rodada (AMB-033).                       
      55 +- **Não seleciona automaticamente política histórica por vigênc
         +ia.** `politica.vigencia` é validada e preservada como metadado
         + (RN-021), mas nenhuma política externa anterior à v4 foi forne
         +cida — não há, hoje, entre o quê escolher.                     
      56  
      57  ---
      58  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 21 lines
       87  
       88  `periodo.competencia` segue o comportamento opcional já descri
           to: preservado quando presente no formato `AAAA-MM`, nulo em q
           ualquer outro caso, sem efeito sobre decisão alguma.
       89  
       90 +**`colaborador.centro_custo` a partir da política v4 (Dia 2).*
          +* Deixa de ser puramente decorativo: participa da resolução da
          + política aplicável a cada item (RN-019). Sua tolerância estru
          +tural, porém, não muda — ausência, nulo, tipo inválido ou valo
          +r sem entrada na tabela de política continuam sem invalidar o 
          +arquivo nem qualquer item; em todos esses casos, resolve-se in
          +tegralmente para a política `padrao` (RN-019, AMB-019). A comp
          +aração contra a tabela de política é textual e exata — sem tri
          +m, sem normalização de caixa ou acento, sem correspondência ap
          +roximada, pelo mesmo princípio já registrado em AMB-015/ESCOPO
          + para campos de texto livre fora de `categoria`.              
       91 +                                                              
       92 +### 4.1.1 Arquivos externos: política de reembolso e câmbio   
       93 +                                                              
       94 +A partir da política v4, a execução passa a depender de dois d
          +ocumentos adicionais, externos ao arquivo de despesas e obriga
          +tórios em toda execução.                                      
       95 +                                                              
       96 +**Arquivo de política.**                                      
       97 +                                                              
       98 +| Campo | Tipo | Significado |                                
       99 +|---|---|---|                                                 
      100 +| `vigencia` | texto `AAAA-MM-DD` | Data de entrada em vigor d
          +a política. Obrigatória e validada estruturalmente (RN-021) | 
      101 +| `padrao` | objeto | Tabela de limites por categoria, aplicáv
          +el a qualquer centro de custo sem entrada própria em `centros_
          +custo` |                                                      
      102 +| `centros_custo` | objeto | Mapa de centro de custo para a re
          +spectiva tabela de limites por categoria — substitui integralm
          +ente `padrao` para as categorias que declarar (RN-019) |      
      103 +| `nota_fiscal_obrigatoria_acima_de` | número | Gatilho de not
          +a fiscal (RN-009), em BRL, único para toda a execução |       
      104 +                                                              
      105 +Cada entrada de `padrao` ou de um centro de custo específico a
          +ssocia uma categoria a `{ limite, periodicidade }`. Qual tabel
          +a — e, dentro dela, qual limite — se aplica a cada item é RN-0
          +19.                                                           
      106 +                                                              
      107 +**Arquivo de câmbio.** Um mapa de data (`AAAA-MM-DD`) para um 
          +objeto de moeda (código ISO 4217) para taxa (número). Cobre ap
          +enas dias úteis bancários — a ausência de uma data é esperada,
          + não um defeito do arquivo. A resolução de taxa é RN-020.     
      108 +                                                              
      109 +Política e câmbio são entrada obrigatória da execução, na mesm
          +a categoria de "arquivo de entrada" que hoje é o arquivo de de
          +spesas: ausência, ilegibilidade ou invalidade estrutural de qu
          +alquer um dos dois impede toda a apuração (RN-022) — erro glob
          +al, mais grave que um envelope de despesas inválido (RN-001), 
          +que ainda pressupõe política e câmbio utilizáveis. Os mecanism
          +os exatos de como a execução recebe esses arquivos (nomes de a
          +rgumento, códigos de saída) são contrato de execução e pertenc
          +em ao `plan.md`, atualizado em etapa posterior a esta.        
      110 +                                                              
      111  ### 4.2 Item de despesa
      112  
      113  Os sete campos abaixo são obrigatórios. A sequência é a **orde
           m canônica de contrato**, usada para ordenar motivos estrutura
           is na saída.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 6 lines, removed 3 lines
      110  
      111  ### 4.2 Item de despesa
      112  
      113 -Os sete campos abaixo são obrigatórios. A sequência é a **orde
          -m canônica de contrato**, usada para ordenar motivos estrutura
          -is na saída.                                                  
      113 +Os sete primeiros campos abaixo são obrigatórios; o oitavo, `d
          +espesa.moeda`, é opcional (política v4, Dia 2). A sequência é 
          +a **ordem canônica de contrato**, usada para ordenar motivos e
          +struturais na saída — `despesa.moeda` ocupa a sexta posição, e
          +ntre `despesa.valor` e `despesa.tem_nota_fiscal`.             
      114  
      115  | # | Campo | Tipo | Significado | Restrição adicional |
      116  |---|---|---|---|---|
     ...
      119  | 3 | `despesa.categoria` | texto | Categoria informada, antes
            da normalização | Texto não vazio |
      120  | 4 | `despesa.descricao` | texto | Descrição livre. Informati
           va; integra a chave de duplicidade como recebida | Texto, pode
           ndo ser vazio |
      121  | 5 | `despesa.fornecedor` | texto | Fornecedor. Integra a cha
           ve de duplicidade como recebido | Texto, podendo ser vazio |
      122 -| 6 | `despesa.valor` | número | Valor monetário em BRL. Intei
          -ro é aceito (`72` → `72,00`) | Nenhuma quanto ao sinal: zero e
          - negativos são estruturalmente válidos, avaliados depois por R
          -N-006 |                                                       
      123 -| 7 | `despesa.tem_nota_fiscal` | booleano | Existência de not
          -a fiscal | Nenhuma além do tipo booleano — um valor booleano r
          -ecebido não é aceito como número, nem o inverso |             
      122 +| 6 | `despesa.valor` | número | Valor monetário na moeda da d
          +espesa. Inteiro é aceito (`72` → `72,00`) | Nenhuma quanto ao 
          +sinal: zero e negativos são estruturalmente válidos, avaliados
          + depois por RN-006 |                                          
      123 +| 7 | `despesa.moeda` | texto | Moeda ISO 4217 da despesa (pol
          +ítica v4, Dia 2). Opcional: ausente ou nulo resolve para `BRL`
          + sem produzir motivo algum | Quando presente, texto de exatame
          +nte três letras maiúsculas (`[A-Z]{3}`) — sem trim e sem conve
          +rsão de caixa. `"usd"`, `" USD"`, `"US"` e `"USDX"` são format
          +o inválido |                                                  
      124 +| 8 | `despesa.tem_nota_fiscal` | booleano | Existência de not
          +a fiscal | Nenhuma além do tipo booleano — um valor booleano r
          +ecebido não é aceito como número, nem o inverso |             
      125  
      126 +**Regra de opcionalidade de `despesa.moeda`.** É o único dos o
          +ito campos cuja ausência não produz motivo algum: chave ausent
          +e ou valor nulo resolve silenciosamente para `BRL`, exatamente
          + como a ausência de moeda era tratada antes de `despesa.moeda`
          + existir. A tolerância termina aí — presente e de tipo ou form
          +ato inválido, `despesa.moeda` recusa o item como qualquer outr
          +o campo estrutural (RN-002), com `CAMPO_TIPO_INVALIDO` ou `CAM
          +PO_FORMATO_INVALIDO` conforme o caso. Isso o distingue de `col
          +aborador.centro_custo` (4.1), cuja tolerância cobre também val
          +ores malformados, não só a ausência.                          
      127 +                                                              
      128  **Nomes qualificados.** A forma `despesa.<campo>` é o nome can
           ônico do campo em toda esta spec, e é exatamente o conjunto de
            valores não nulos aceitos em `motivo.campo` (4.3). A chave co
           rrespondente dentro do objeto de despesa é o segmento à direit
           a do ponto: o campo canônico `despesa.valor` corresponde à cha
           ve `valor`.
      129  
      130  Campo **ausente**, **nulo**, de **tipo inválido** ou de **form
           ato inválido** produz **erro estrutural no item**: o item é re
           cusado e o restante do arquivo continua sendo processado.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      127  
      128  **Nomes qualificados.** A forma `despesa.<campo>` é o nome can
           ônico do campo em toda esta spec, e é exatamente o conjunto de
            valores não nulos aceitos em `motivo.campo` (4.3). A chave co
           rrespondente dentro do objeto de despesa é o segmento à direit
           a do ponto: o campo canônico `despesa.valor` corresponde à cha
           ve `valor`.
      129  
      130 -Campo **ausente**, **nulo**, de **tipo inválido** ou de **form
          -ato inválido** produz **erro estrutural no item**: o item é re
          -cusado e o restante do arquivo continua sendo processado.     
      130 +Campo **ausente**, **nulo**, de **tipo inválido** ou de **form
          +ato inválido** produz **erro estrutural no item**: o item é re
          +cusado e o restante do arquivo continua sendo processado. Essa
          + regra vale integralmente para os sete campos obrigatórios; pa
          +ra `despesa.moeda`, vale apenas quando o campo está **presente
          +** — sua ausência não é erro estrutural (parágrafo acima).    
      131  
      132  **Classificação do erro estrutural.** A escolha entre os três 
           motivos abaixo é fechada e não admite outra leitura:
      133  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      131  
      132  **Classificação do erro estrutural.** A escolha entre os três 
           motivos abaixo é fechada e não admite outra leitura:
      133  
      134 -- **`CAMPO_AUSENTE`** — a chave obrigatória não existe no obje
          -to, ou existe com valor `nulo`.                               
      134 +- **`CAMPO_AUSENTE`** — a chave obrigatória não existe no obje
          +to, ou existe com valor `nulo`. Não se aplica a `despesa.moeda
          +`, que é opcional (4.2) — sua ausência resolve para `BRL` sem 
          +motivo algum.                                                 
      135  - **`CAMPO_TIPO_INVALIDO`** — a chave existe, não é nula, mas 
           seu tipo JSON diverge do exigido na coluna "Tipo" da tabela ac
           ima. Um valor booleano nunca é aceito como número, mesmo em li
           nguagens de implementação que tratam booleano como subtipo num
           érico — e o inverso também não é aceito.
      136  - **`CAMPO_FORMATO_INVALIDO`** — o tipo JSON está correto, mas
            o conteúdo viola a "Restrição adicional" da tabela acima: tex
           to vazio onde não é permitido, texto que não segue `AAAA-MM-DD
           `, ou texto nesse padrão que não representa uma data real do c
           alendário.
      137  
● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines
      147  | `despesa.valor` igual a `"72,50"` | `CAMPO_TIPO_INVALIDO` |
      148  | `despesa.tem_nota_fiscal` igual a `"sim"` | `CAMPO_TIPO_INVA
           LIDO` |
      149  | `despesa.valor` igual a `0` ou a um número negativo | Estrut
           uralmente válido — nenhum motivo estrutural. Tratado depois po
           r RN-006 (`VALOR_NAO_POSITIVO`) |
      150 +| `despesa.moeda` ausente | Estruturalmente válido — nenhum mo
          +tivo. Resolve para `BRL` (política v4, Dia 2) |               
      151 +| `despesa.moeda` igual a `"usd"` | `CAMPO_FORMATO_INVALIDO` —
          + minúsculo |                                                  
      152 +| `despesa.moeda` igual a um número | `CAMPO_TIPO_INVALIDO` | 
      153  
      154  **Elemento que não é objeto.** Cada elemento de `despesas` dev
           e ser um objeto. Quando não for — texto, número, lista, nulo —
           , aquela posição é recusada com o motivo único `ITEM_TIPO_INVA
           LIDO`. Não se produzem sete motivos de campo ausente e nenhuma
            regra de negócio é avaliada. O registro correspondente é exat
           amente:
      155  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines, removed 1 line
      151  | `despesa.moeda` igual a `"usd"` | `CAMPO_FORMATO_INVALIDO` —
            minúsculo |
      152  | `despesa.moeda` igual a um número | `CAMPO_TIPO_INVALIDO` |
      153  
      154 -**Elemento que não é objeto.** Cada elemento de `despesas` dev
          -e ser um objeto. Quando não for — texto, número, lista, nulo —
          -, aquela posição é recusada com o motivo único `ITEM_TIPO_INVA
          -LIDO`. Não se produzem sete motivos de campo ausente e nenhuma
          - regra de negócio é avaliada. O registro correspondente é exat
          -amente:                                                       
      154 +**Elemento que não é objeto.** Cada elemento de `despesas` dev
          +e ser um objeto. Quando não for — texto, número, lista, nulo —
          +, aquela posição é recusada com o motivo único `ITEM_TIPO_INVA
          +LIDO`. Não se produzem os motivos de campo ausente dos oito ca
          +mpos de 4.2, e nenhuma regra de negócio é avaliada. O registro
          + correspondente é exatamente:                                 
      155  
      156  | Campo da saída | Valor |
      157  |---|---|
      158  | `indice_entrada` | a posição original, preservada |
      159  | `id` | nulo |
      160  | `valor_informado` | nulo |
      161 +| `moeda` | nulo |                                            
      162 +| `taxa_cambio_aplicada` | nulo |                             
      163 +| `data_cotacao_utilizada` | nulo |                           
      164  | `valor_normalizado` | nulo |
      165  | `valor_reembolsavel` | `0,00` |
      166  | `decisao` | `RECUSADO` |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 42 lines, removed 1 line
      185  | `indice_entrada` | inteiro ≥ 1 | Posição original do item na
            lista, **base 1**, atribuída antes de qualquer validação e im
           utável |
      186  | `id` | texto ou nulo | `despesa.id` original. Nulo quando au
           sente ou inválido |
      187  | `valor_informado` | qualquer valor JSON ou nulo | O conteúdo
            de `despesa.valor` exatamente como recebido, preservado para 
           auditoria — inclusive quando o tipo é inválido |
      188 -| `valor_normalizado` | monetário ou nulo | Valor após normali
          -zação monetária. Nulo quando não calculável |                 
      188 +| `moeda` | texto ou nulo | Moeda v4, Dia 2. Moeda efetivament
          +e usada no item: `despesa.moeda` quando estruturalmente válida
          +, ou `"BRL"` quando ausente/nula. Nula apenas quando `despesa.
          +moeda` é estruturalmente inválida, ou o elemento de `despesas`
          + não é objeto |                                               
      189 +| `taxa_cambio_aplicada` | número ou nulo | v4, Dia 2. `1` par
          +a BRL; a taxa efetivamente usada na conversão, quando resolvid
          +a (RN-020); nulo quando não há conversão possível |           
      190 +| `data_cotacao_utilizada` | texto `AAAA-MM-DD` ou nulo | v4, 
          +Dia 2. Data da cotação efetivamente usada — pode divergir de `
          +despesa.data` (RN-020); nulo para BRL e sempre que `taxa_cambi
          +o_aplicada` for nulo |                                        
      191 +| `valor_normalizado` | monetário ou nulo | Valor após convers
          +ão para BRL (RN-020, quando aplicável) e normalização monetári
          +a (RN-004). Nulo quando não calculável |                      
      192  | `valor_reembolsavel` | monetário | Valor aprovado. Sempre `0
           ,00` para item recusado |
      193  | `decisao` | enumeração | Ver 4.4 |
      194  | `motivos` | lista de objetos, possivelmente vazia | Objetos 
           de motivo, na ordem definida em 8.3 |
      195  
      196 +**Comportamento de `moeda` / `taxa_cambio_aplicada` / `data_co
          +tacao_utilizada` por cenário (RN-020):**                      
      197 +                                                              
      198 +BRL (padrão ou informada):                                    
      199 +                                                              
      200 +```json                                                       
      201 +"moeda": "BRL",                                               
      202 +"taxa_cambio_aplicada": 1,                                    
      203 +"data_cotacao_utilizada": null                                
      204 +```                                                           
      205 +                                                              
      206 +Moeda estrangeira convertida (exemplo: EUR sem cotação exata, 
          +usando a mais recente anterior):                              
      207 +                                                              
      208 +```json                                                       
      209 +"moeda": "EUR",                                               
      210 +"taxa_cambio_aplicada": 5.93,                                 
      211 +"data_cotacao_utilizada": "2026-07-17"                        
      212 +```                                                           
      213 +                                                              
      214 +Moeda estruturalmente inválida (`CAMPO_TIPO_INVALIDO` ou `CAMP
          +O_FORMATO_INVALIDO`):                                         
      215 +                                                              
      216 +```json                                                       
      217 +"moeda": null,                                                
      218 +"taxa_cambio_aplicada": null,                                 
      219 +"data_cotacao_utilizada": null                                
      220 +```                                                           
      221 +                                                              
      222 +Moeda estruturalmente válida, mas sem cotação utilizável (`MOE
          +DA_SEM_COTACAO`, RN-020):                                     
      223 +                                                              
      224 +```json                                                       
      225 +"moeda": "GBP",                                               
      226 +"taxa_cambio_aplicada": null,                                 
      227 +"data_cotacao_utilizada": null,                               
      228 +"valor_normalizado": null,                                    
      229 +"valor_reembolsavel": 0.00                                    
      230 +```                                                           
      231 +                                                              
      232 +`taxa_cambio_aplicada` é sempre número JSON, nunca texto, e pr
          +eserva a precisão do arquivo de câmbio. Não existe campo separ
          +ado `valor_convertido`: esse papel é de `valor_normalizado`, q
          +ue já é o valor em BRL após a conversão.                      
      233 +                                                              
      234  **Preservação de `valor_informado`.** Quando a chave `valor` e
           xiste no objeto de despesa, `valor_informado` é exatamente o v
           alor JSON recebido — número, texto, booleano, lista ou objeto 
           —, mesmo quando esse tipo é inválido para o contrato: `valor: 
           "72,50"` produz `valor_informado` igual a `"72,50"`; `valor: t
           rue` produz `valor_informado` igual a `true`. `valor_informado
           ` é nulo quando a chave `valor` está ausente ou é nula, e tamb
           ém quando o elemento de `despesas` não é um objeto (4.2). `val
           or_normalizado` permanece nulo sempre que `despesa.valor` não 
           for um número válido, independentemente do que `valor_informad
           o` contenha.
      235  
      236  Cada objeto de `motivos` tem três campos:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      241  | `regra` | texto | Identificador da regra de negócio que prod
           uziu o motivo, no formato `RN-NNN` |
      242  | `campo` | texto ou nulo | Nome qualificado do campo. Restrit
           o à lista fechada abaixo. Nulo quando o motivo não se refere a
            um campo específico |
      243  
      244 -**Valores aceitos em `campo`.** Apenas os sete nomes canônicos
          - de 4.2, e nenhum outro:                                      
      244 +**Valores aceitos em `campo`.** Apenas os oito nomes canônicos
          + de 4.2, e nenhum outro:                                      
      245  
      246 -`despesa.id` · `despesa.data` · `despesa.categoria` · `despesa
          -.descricao` · `despesa.fornecedor` · `despesa.valor` · `despes
          -a.tem_nota_fiscal`                                            
      246 +`despesa.id` · `despesa.data` · `despesa.categoria` · `despesa
          +.descricao` · `despesa.fornecedor` · `despesa.valor` · `despes
          +a.moeda` · `despesa.tem_nota_fiscal`                          
      247  
      248  Qualquer outro motivo traz `campo` nulo. Há uma única exceção 
           a "estrutural implica campo preenchido": `ITEM_TIPO_INVALIDO` 
           traz `campo` nulo, porque o defeito é do elemento inteiro e nã
           o de um campo dele. E há uma única exceção a "não estrutural i
           mplica campo nulo": `ID_DUPLICADO` traz `campo` igual a `despe
           sa.id`, porque a violação é dele.
      249  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 6 lines, removed 4 lines
      275  | Código | Regra associada | `campo` | Significado |
      276  |---|---|---|---|
      277  | `ITEM_TIPO_INVALIDO` | RN-002 | nulo | Elemento de `despesas
           ` que não é objeto. Motivo único da posição |
      278 -| `CAMPO_AUSENTE` | RN-002 | um dos sete nomes canônicos | A c
          -have obrigatória não existe no objeto, ou existe com valor nul
          -o |                                                           
      279 -| `CAMPO_TIPO_INVALIDO` | RN-002 | um dos sete nomes canônicos
          - | A chave existe, não é nula, mas seu tipo JSON diverge do ex
          -igido pelo contrato |                                         
      280 -| `CAMPO_FORMATO_INVALIDO` | RN-002 | um dos sete nomes canôni
          -cos | O tipo JSON está correto, mas o conteúdo viola a restriç
          -ão adicional do campo |                                       
      278 +| `CAMPO_AUSENTE` | RN-002 | um dos oito nomes canônicos | A c
          +have obrigatória não existe no objeto, ou existe com valor nul
          +o. Não se aplica a `despesa.moeda`, que é opcional |          
      279 +| `CAMPO_TIPO_INVALIDO` | RN-002 | um dos oito nomes canônicos
          + | A chave existe, não é nula, mas seu tipo JSON diverge do ex
          +igido pelo contrato |                                         
      280 +| `CAMPO_FORMATO_INVALIDO` | RN-002 | um dos oito nomes canôni
          +cos | O tipo JSON está correto, mas o conteúdo viola a restriç
          +ão adicional do campo |                                       
      281  | `ID_DUPLICADO` | RN-003 | `despesa.id` | `despesa.id` válido
            repetido no arquivo |
      282 +| `MOEDA_SEM_COTACAO` | RN-020 | `despesa.moeda` | v4, Dia 2. 
          +Moeda estruturalmente válida, mas sem cotação exata ou anterio
          +r utilizável em `cambio.json` na data da despesa |            
      283  | `VALOR_NAO_POSITIVO` | RN-006 | nulo | Valor normalizado men
           or ou igual a zero |
      283 -| `CATEGORIA_FORA_POLITICA` | RN-007 | nulo | Categoria normal
          -izada fora da lista canônica |                                
      284 +| `CATEGORIA_FORA_POLITICA` | RN-007 | nulo | Categoria normal
          +izada ausente de toda tabela de política aplicável (padrão e, 
          +quando houver, específica do centro de custo) |               
      285 +| `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` | RN-019 | nulo | 
          +v4, Dia 2. Categoria com limite de política igual a `0,00`, ex
          +plicitamente não reembolsável para o centro de custo do colabo
          +rador |                                                       
      286  | `FORA_COMPETENCIA` | RN-008 | nulo | `data` fora da janela |
      287  | `NOTA_FISCAL_AUSENTE` | RN-009 | nulo | Nota obrigatória e a
           usente |
      288  | `DUPLICIDADE` | RN-010 | nulo | Ocorrência posterior de desp
           esa economicamente idêntica |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines
      255  { "codigo": "ID_DUPLICADO",         "regra": "RN-003", "campo"
           : "despesa.id" }
      256  { "codigo": "NOTA_FISCAL_AUSENTE",  "regra": "RN-009", "campo"
           : null }
      257  { "codigo": "TETO_DIARIO_APLICADO", "regra": "RN-011", "campo"
           : null }
      258 +{ "codigo": "MOEDA_SEM_COTACAO",    "regra": "RN-020", "campo"
          +: "despesa.moeda" }                                           
      259 +{ "codigo": "CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO", "regra"
          +: "RN-019", "campo": null }                                   
      260  ```
      261  
      262  Item integralmente reembolsado tem `motivos: []`.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 11 lines, removed 3 lines
      305  
      306  ```
      307  "resultados": [
      308 -  { "indice_entrada": 1, "id": "d-001", "valor_informado": 72.
          -50, "valor_normalizado": 72.50,                               
      308 +  { "indice_entrada": 1, "id": "d-001", "valor_informado": 72.
          +50,                                                           
      309 +    "moeda": "BRL", "taxa_cambio_aplicada": 1, "data_cotacao_u
          +tilizada": null,                                              
      310 +    "valor_normalizado": 72.50,                               
      311      "valor_reembolsavel": 60.00, "decisao": "PARCIALMENTE_REEM
           BOLSADO",
      312      "motivos": [ { "codigo": "TETO_DIARIO_APLICADO", "regra": 
           "RN-011", "campo": null } ] },
      313  
      312 -  { "indice_entrada": 2, "id": "d-002", "valor_informado": 38.
          -00, "valor_normalizado": 38.00,                               
      314 +  { "indice_entrada": 2, "id": "d-002", "valor_informado": 38.
          +00,                                                           
      315 +    "moeda": "BRL", "taxa_cambio_aplicada": 1, "data_cotacao_u
          +tilizada": null,                                              
      316 +    "valor_normalizado": 38.00,                               
      317      "valor_reembolsavel": 0.00, "decisao": "NAO_REEMBOLSADO_TE
           TO_ESGOTADO",
      318      "motivos": [ { "codigo": "TETO_DIARIO_ESGOTADO", "regra": 
           "RN-015", "campo": null } ] },
      319  
      316 -  { "indice_entrada": 11, "id": "d-011", "valor_informado": 33
          -.333, "valor_normalizado": 33.33,                             
      320 +  { "indice_entrada": 11, "id": "d-011", "valor_informado": 33
          +.333,                                                         
      321 +    "moeda": "BRL", "taxa_cambio_aplicada": 1, "data_cotacao_u
          +tilizada": null,                                              
      322 +    "valor_normalizado": 33.33,                               
      323      "valor_reembolsavel": 33.33, "decisao": "INTEGRALMENTE_REE
           MBOLSADO",
      324      "motivos": [] }
      325  ]
      326  ```
      327  
      328 +Este fragmento antecede a política v4 e ilustra apenas a forma
          + dos campos previamente existentes mais os três campos de audi
          +toria de câmbio (4.3), sob valores `BRL` — não exercita conver
          +são. Exemplos de conversão real estão em `§12`.               
      329 +                                                              
      330  Os metadados do envelope acompanham a lista no documento compl
           eto:
      331  
      332  ```

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines
      336  
      337  ### 4.7 Resultado esperado para `exemplos/despesas-exemplo.jso
           n`
      338  
      339 +Esta tabela documenta o comportamento sob a política **padrão*
          +* (alimentação R$60/dia, transporte R$80/dia, hospedagem R$250
          +/diária, gatilho de nota fiscal R$100 — os mesmos valores hard
          +coded desta spec até a v1.1). É a baseline de regressão histór
          +ica, preservada como cenário próprio (CA-037). O mesmo arquivo
          +, processado com a política **oficial v4** e o centro de custo
          + real do colaborador (`CC-ENG-PLATAFORMA`, cadastrado na tabel
          +a), produz um resultado diferente — ver `§12`.                
      340 +                                                              
      341  | # | `id` | Data | Categoria canônica | Informado | Normaliza
           do | Decisão | Reembolsável | Motivos |
      342  |---|---|---|---|---|---|---|---|---|
      343  | 1 | d-001 | 2026-07-03 | alimentacao | 72.50 | 72,50 | PARCI
           AL | **60,00** | `TETO_DIARIO_APLICADO` |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      373  
      374  ### RN-002 — Contrato do item
      375  
      376 -**Regra:** cada elemento de `despesas` deve ser um objeto cont
          -endo os sete campos de 4.2 com os tipos e restrições declarado
          -s. Elemento que não é objeto é recusado com o motivo único `IT
          -EM_TIPO_INVALIDO`, sem que nenhuma regra de negócio seja avali
          -ada e sem gerar motivos de campo ausente; o registro resultant
          -e traz `id`, `valor_informado` e `valor_normalizado` nulos, `v
          -alor_reembolsavel` `0,00` e `indice_entrada` preservado. Sendo
          - objeto, campo **ausente**, **nulo**, de **tipo inválido** ou 
          -de **formato inválido** recusa o item, com um motivo por campo
          - defeituoso, classificado pela regra fechada da subseção "Clas
          -sificação do erro estrutural" (4.2). Em qualquer caso o arquiv
          -o não é interrompido.                                         
      376 +**Regra:** cada elemento de `despesas` deve ser um objeto. Set
          +e dos seus oito campos de 4.2 são obrigatórios, com os tipos e
          + restrições declarados; `despesa.moeda` (v4, Dia 2) é o único 
          +opcional, e sua ausência não produz motivo algum — resolve par
          +a `BRL` (RN-020). Elemento que não é objeto é recusado com o m
          +otivo único `ITEM_TIPO_INVALIDO`, sem que nenhuma regra de neg
          +ócio seja avaliada e sem gerar motivos de campo ausente; o reg
          +istro resultante traz `id`, `valor_informado`, `moeda`, `taxa_
          +cambio_aplicada`, `data_cotacao_utilizada` e `valor_normalizad
          +o` nulos, `valor_reembolsavel` `0,00` e `indice_entrada` prese
          +rvado. Sendo objeto, qualquer campo obrigatório **ausente**, *
          +*nulo**, de **tipo inválido** ou de **formato inválido** recus
          +a o item, com um motivo por campo defeituoso, classificado pel
          +a regra fechada da subseção "Classificação do erro estrutural"
          + (4.2); `despesa.moeda`, quando presente, segue a mesma classi
          +ficação de tipo/formato dos demais campos. Em qualquer caso o 
          +arquivo não é interrompido.                                   
      377  **Origem:** contrato de entrada.
      378 -**Aceite:** item com `data: "31/07/2026"` e `valor: "72,50"` é
          - recusado com dois motivos, nesta ordem — `CAMPO_FORMATO_INVAL
          -IDO` com `campo` igual a `despesa.data`, depois `CAMPO_TIPO_IN
          -VALIDO` com `campo` igual a `despesa.valor` —, porque `despesa
          -.data` precede `despesa.valor` na ordem canônica de contrato. 
          -`despesa.categoria` igual a um número é recusado com `CAMPO_TI
          -PO_INVALIDO` e `campo` igual a `despesa.categoria`; `despesa.i
          -d` igual a texto vazio é recusado com `CAMPO_FORMATO_INVALIDO`
          - e `campo` igual a `despesa.id`. Um elemento de `despesas` igu
          -al ao texto `"despesa"` produz um único registro recusado, com
          - motivo único `ITEM_TIPO_INVALIDO`, `campo` nulo, `id` nulo, `
          -valor_informado` nulo e `indice_entrada` preservado.          
      378 +**Aceite:** item com `data: "31/07/2026"` e `valor: "72,50"` é
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
          +valor_informado` nulo e `indice_entrada` preservado. Um item c
          +om `moeda: "usd"` é recusado com `CAMPO_FORMATO_INVALIDO` e `c
          +ampo` igual a `despesa.moeda`; um item sem a chave `moeda` não
          + recebe motivo algum por causa disso.                         
      379  
      380  ### RN-003 — Unicidade de `despesa.id`
      381  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      385  
      386  ### RN-004 — Normalização monetária
      387  
      388 -**Regra:** todo valor válido é normalizado para duas casas dec
          -imais, arredondamento decimal **meio para cima**. Todas as reg
          -ras posteriores usam o valor normalizado.                     
      389 -**Origem:** política do RH, itens 1 a 5 (limites com duas casa
          -s).                                                           
      390 -**Aceite:** `33.333` → `33,33`; `33.335` → `33,34`; `33.345` →
          - `33,35`; `100.004` → `100,00`; `100.005` → `100,01`.         
      388 +**Regra:** todo valor válido é normalizado para duas casas dec
          +imais, arredondamento decimal **meio para cima**. Quando `desp
          +esa.moeda` for estrangeira e tiver cotação resolvida (RN-020),
          + a normalização opera sobre o valor **já convertido** para BRL
          +: a multiplicação pela taxa acontece antes do arredondamento, 
          +e há um único arredondamento — nunca um arredondamento do valo
          +r na moeda original seguido de outro após a conversão. Todas a
          +s regras posteriores usam o valor normalizado (e, quando aplic
          +ável, convertido).                                            
      389 +**Origem:** política do RH, itens 1 a 5 (limites com duas casa
          +s); conversão cambial acrescentada pela política v4 (Dia 2, RN
          +-020).                                                        
      390 +**Aceite:** `33.333` → `33,33`; `33.335` → `33,34`; `33.345` →
          + `33,35`; `100.004` → `100,00`; `100.005` → `100,01`. USD `40,
          +00` convertido pela taxa `5,50` normaliza para `220,00`, num ú
          +nico arredondamento.                                          
      391  
      392  ### RN-005 — Normalização de categoria
      393  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      403  
      404  ### RN-007 — Categorias reembolsáveis
      405  
      406 -**Regra:** apenas `alimentacao`, `transporte_urbano` e `hosped
          -agem` são reembolsáveis. Qualquer outra categoria normalizada 
          -é recusada com `CATEGORIA_FORA_POLITICA`.                     
      407 -**Origem:** política do RH, item 9.                           
      408 -**Aceite:** `coworking` de R$ 89,00 com nota é recusado, reemb
          -olsável `0,00`.                                               
      406 +**Regra:** é reembolsável a categoria normalizada presente na 
          +política aplicável ao centro de custo do colaborador (RN-019) 
          +— no mínimo `alimentacao`, `transporte_urbano` e `hospedagem`,
          + sempre presentes na política padrão, mais qualquer categoria 
          +adicional que o centro de custo declare explicitamente (ex.: `
          +representacao` para `CC-COMERCIAL`, política v4, Dia 2). Categ
          +oria normalizada ausente de toda tabela aplicável — padrão e, 
          +quando houver, específica do centro de custo — é recusada com 
          +`CATEGORIA_FORA_POLITICA`.                                    
      407 +**Origem:** política do RH, item 9; conjunto de categorias var
          +iável por centro de custo acrescentado pela política v4 (Dia 2
          +, RN-019).                                                    
      408 +**Aceite:** `coworking` de R$ 89,00 com nota é recusado em qua
          +lquer centro de custo, reembolsável `0,00`, porque nenhuma tab
          +ela de política — padrão ou específica — o declara. `represent
          +acao` de R$190,00 com nota, para um colaborador de `CC-SUPORTE
          +-N2` (fora da tabela de centros de custo), é recusado com `CAT
          +EGORIA_FORA_POLITICA`, porque nem a política padrão nem `CC-SU
          +PORTE-N2` declaram essa categoria.                            
      409  
      410  ### RN-008 — Elegibilidade temporal
      411  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      415  
      416  ### RN-009 — Nota fiscal obrigatória
      417  
      418 -**Regra:** quando o valor normalizado do item for **estritamen
          -te maior** que R$ 100,00 e `tem_nota_fiscal` for falso, o item
          - é recusado com `NOTA_FISCAL_AUSENTE`. A comparação usa o valo
          -r individual normalizado, antes de qualquer corte por teto.   
      419 -**Origem:** política do RH, item 5.                           
      420 -**Aceite:** `100,00` sem nota é elegível; `100,01` sem nota é 
          -recusado; `690,00` sem nota é recusado sem que qualquer teto s
          -eja calculado.                                                
      418 +**Regra:** quando o valor normalizado do item — já convertido 
          +para BRL quando `despesa.moeda` for estrangeira (RN-020) — for
          + **estritamente maior** que o gatilho da política (`nota_fisca
          +l_obrigatoria_acima_de`, R$ 100,00) e `tem_nota_fiscal` for fa
          +lso, o item é recusado com `NOTA_FISCAL_AUSENTE`. A comparação
          + usa o valor individual normalizado e convertido, antes de qua
          +lquer corte por teto — nunca o valor original na moeda da desp
          +esa.                                                          
      419 +**Origem:** política do RH, item 5; comparação pelo valor conv
          +ertido acrescentada pela política v4 (Dia 2, RN-020).         
      420 +**Aceite:** `100,00` sem nota é elegível; `100,01` sem nota é 
          +recusado; `690,00` sem nota é recusado sem que qualquer teto s
          +eja calculado. USD `40,00` convertido pela taxa `5,50` resulta
          + em `220,00`; sem nota fiscal, o item é recusado com `NOTA_FIS
          +CAL_AUSENTE`, mesmo que o valor original (`40,00`) esteja abai
          +xo do gatilho.                                                
      421  
      422  ### RN-010 — Duplicidade econômica
      423  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      421  
      422  ### RN-010 — Duplicidade econômica
      423  
      424 -**Regra:** dois itens são duplicados quando coincidem exatamen
          -te em `data`, `categoria` normalizada, `valor` normalizado, `f
          -ornecedor` como recebido e `descricao` como recebida. `despesa
          -.id` e `tem_nota_fiscal` não integram a chave. Avaliada apenas
          - entre itens sem nenhum motivo anterior de recusa. A primeira 
          -ocorrência em ordem de `indice_entrada` é mantida; as posterio
          -res recebem `DUPLICIDADE`.                                    
      425 -**Origem:** política do RH, item 8.                           
      426 -**Aceite:** dois itens iguais de R$ 54,90 em `2026-07-09` prod
          -uzem 54,90 no primeiro e 0,00 no segundo. Itens de R$ 100,00 e
          - R$ 100,01 do mesmo dia e fornecedor **não** são duplicados.  
      424 +**Regra:** dois itens são duplicados quando coincidem exatamen
          +te em `data`, `categoria` normalizada, `moeda` (a informada, o
          +u `BRL` quando ausente), `valor` normalizado em BRL, `forneced
          +or` como recebido e `descricao` como recebida. `despesa.id` e 
          +`tem_nota_fiscal` não integram a chave. Avaliada apenas entre 
          +itens sem nenhum motivo anterior de recusa. A primeira ocorrên
          +cia em ordem de `indice_entrada` é mantida; as posteriores rec
          +ebem `DUPLICIDADE`. Itens em moedas diferentes nunca são dupli
          +cados entre si, mesmo quando `data`, categoria, fornecedor, de
          +scrição e o valor **já convertido** coincidem exatamente — a m
          +oeda diferente já basta para distingui-los.                   
      425 +**Origem:** política do RH, item 8; `moeda` acrescentada à cha
          +ve pela política v4 (Dia 2, RN-020).                          
      426 +**Aceite:** dois itens iguais de R$ 54,90 em `2026-07-09` prod
          +uzem 54,90 no primeiro e 0,00 no segundo. Itens de R$ 100,00 e
          + R$ 100,01 do mesmo dia e fornecedor **não** são duplicados. U
          +ma despesa de EUR 100,00 e uma de BRL 100,00, no mesmo dia, ca
          +tegoria, fornecedor e descrição, **não** são duplicatas — aind
          +a que a conversão faça os valores em BRL coincidirem por acaso
          +.                                                             
      427  
      428  ### RN-011 — Limite diário de alimentação
      429  
● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      427  
      428  ### RN-011 — Limite diário de alimentação
      429  
      430 -**Regra:** R$ 60,00 por data, aplicado ao conjunto dos itens e
          -legíveis de `alimentacao` daquela data.                       
      431 -**Origem:** política do RH, item 1.                           
      432 -**Aceite:** R$ 72,50 e R$ 38,00 na mesma data rendem R$ 60,00 
          -no total daquela data.                                        
      430 +**Regra:** o limite de `alimentacao` definido pela política ap
          +licável ao centro de custo do colaborador (RN-019) — R$ 60,00 
          +por data na política padrão — aplicado ao conjunto dos itens e
          +legíveis de `alimentacao` daquela data.                       
      431 +**Origem:** política do RH, item 1; valor parametrizado por ce
          +ntro de custo pela política v4 (Dia 2, RN-019).               
      432 +**Aceite:** sob a política padrão, R$ 72,50 e R$ 38,00 na mesm
          +a data rendem R$ 60,00 no total daquela data.                 
      433  
      434  ### RN-012 — Limite diário de transporte urbano
      435  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      433  
      434  ### RN-012 — Limite diário de transporte urbano
      435  
      436 -**Regra:** R$ 80,00 por data, aplicado ao conjunto dos itens e
          -legíveis de `transporte_urbano` daquela data.                 
      437 -**Origem:** política do RH, item 2.                           
      438 -**Aceite:** item elegível de R$ 100,00, sozinho na data, rende
          - R$ 80,00.                                                    
      436 +**Regra:** o limite de `transporte_urbano` definido pela polít
          +ica aplicável ao centro de custo do colaborador (RN-019) — R$ 
          +80,00 por data na política padrão — aplicado ao conjunto dos i
          +tens elegíveis de `transporte_urbano` daquela data.           
      437 +**Origem:** política do RH, item 2; valor parametrizado por ce
          +ntro de custo pela política v4 (Dia 2, RN-019).               
      438 +**Aceite:** sob a política padrão, item elegível de R$ 100,00,
          + sozinho na data, rende R$ 80,00.                             
      439  
      440  ### RN-013 — Limite individual de hospedagem
      441  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      439  
      440  ### RN-013 — Limite individual de hospedagem
      441  
      442 -**Regra:** R$ 250,00 por lançamento. Hospedagem **não** partic
          -ipa da agregação por data: cada lançamento é avaliado isoladam
          -ente. O conteúdo da descrição não altera o teto.              
      443 -**Origem:** política do RH, item 3.                           
      444 -**Aceite:** lançamento de R$ 480,00 descrito como "2 diarias" 
          -rende R$ 250,00. Alterar o texto da `descricao` desse lançamen
          -to não altera o teto individual aplicado, que permanece R$ 250
          -,00. Dois lançamentos de hospedagem na mesma data podem render
          - até R$ 500,00.                                               
      442 +**Regra:** o limite de `hospedagem` definido pela política apl
          +icável ao centro de custo do colaborador (RN-019) — R$ 250,00 
          +por lançamento na política padrão —, aplicável somente quando 
          +esse limite for maior que zero (limite igual a `0,00` é RN-019
          +, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não este teto). H
          +ospedagem **não** participa da agregação por data: cada lançam
          +ento é avaliado isoladamente. O conteúdo da descrição não alte
          +ra o teto.                                                    
      443 +**Origem:** política do RH, item 3; valor parametrizado por ce
          +ntro de custo pela política v4 (Dia 2, RN-019).               
      444 +**Aceite:** sob a política padrão, lançamento de R$ 480,00 des
          +crito como "2 diarias" rende R$ 250,00. Alterar o texto da `de
          +scricao` desse lançamento não altera o teto individual aplicad
          +o, que permanece R$ 250,00. Dois lançamentos de hospedagem na 
          +mesma data podem render até R$ 500,00 sob a política padrão.  
      445  
      446  ### RN-014 — Reembolso parcial
      447  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      463  
      464  ### RN-017 — Composição da saída
      465  
      466 -**Regra:** toda posição da lista `despesas` produz exatamente 
          -um registro de saída, na ordem da entrada, com os campos de 4.
          -3, uma única decisão final e a lista de objetos de motivo orde
          -nada conforme 8.3. Cada objeto de motivo declara `codigo`, a `
          -regra` que o produziu e o `campo` quando o motivo for estrutur
          -al.                                                           
      467 -**Origem:** auditabilidade (lacuna da política).              
      468 -**Aceite:** entrada com 14 itens produz 14 registros; nenhum i
          -tem desaparece, inclusive os recusados. Todo objeto de motivo 
          -apresentado traz um `codigo` do vocabulário de 4.5 e a `regra`
          - correspondente indicada naquela tabela.                      
      466 +**Regra:** toda posição da lista `despesas` produz exatamente 
          +um registro de saída, na ordem da entrada, com os campos de 4.
          +3 — incluindo, a partir da política v4 (Dia 2), `moeda`, `taxa
          +_cambio_aplicada` e `data_cotacao_utilizada` —, uma única deci
          +são final e a lista de objetos de motivo ordenada conforme 8.3
          +. Cada objeto de motivo declara `codigo`, a `regra` que o prod
          +uziu e o `campo` quando o motivo for estrutural.              
      467 +**Origem:** auditabilidade (lacuna da política); campos de aud
          +itoria de câmbio acrescentados pela política v4 (Dia 2).      
      468 +**Aceite:** entrada com 14 itens produz 14 registros; nenhum i
          +tem desaparece, inclusive os recusados. Todo objeto de motivo 
          +apresentado traz um `codigo` do vocabulário de 4.5 e a `regra`
          + correspondente indicada naquela tabela. Um item em EUR traz `
          +moeda: "EUR"`, `taxa_cambio_aplicada` igual à taxa efetivament
          +e usada e `data_cotacao_utilizada` igual à data da cotação (qu
          +e pode divergir de `despesa.data` — RN-020); um item em BRL tr
          +az `moeda: "BRL"`, `taxa_cambio_aplicada: 1` e `data_cotacao_u
          +tilizada: null`.                                              
      469  
      470  ### RN-018 — Total do período
      471  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 25 lines, removed 1 line
      471  
      472  **Regra:** `total_reembolsavel` é a soma dos `valor_reembolsav
           el` apresentados nos registros de saída.
      473  **Origem:** auditabilidade.
      474 -**Aceite:** para `exemplos/despesas-exemplo.json`, o total é R
          -$ 585,43 e coincide com a soma da coluna de 4.7.              
      474 +**Aceite:** para `exemplos/despesas-exemplo.json`, sob a polít
          +ica padrão, o total é R$ 585,43 e coincide com a soma da colun
          +a de 4.7. O mesmo arquivo, sob a política v4 e `CC-ENG-PLATAFO
          +RMA`, totaliza R$ 351,43 — ver `§12`.                         
      475  
      476 +### RN-019 — Política de reembolso por centro de custo        
      477 +                                                              
      478 +**Regra:** a política de limites deixa de ser um valor único: 
          +cada categoria é avaliada contra a tabela do centro de custo d
          +o colaborador (`colaborador.centro_custo`), quando cadastrada,
          + ou contra a tabela `padrao` caso contrário. A comparação de `
          +centro_custo` contra a tabela é textual e exata — sem trim, se
          +m normalização de caixa ou acento, sem correspondência aproxim
          +ada. Centro de custo ausente, nulo, de tipo inválido, ou prese
          +nte mas sem entrada na tabela, usa integralmente `padrao` — nu
          +nca uma mistura entre as duas tabelas. Um centro de custo cada
          +strado usa **somente** as categorias que declara explicitament
          +e: categoria da política padrão que o centro de custo cadastra
          +do não declara **não** recebe o valor de `padrao` por fallback
          + — é tratada como não reembolsável para aquele centro de custo
          +. Categoria com limite explicitamente igual a `0,00` na tabela
          + aplicável é recusada com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_C
          +USTO`, nunca como reembolso parcial de valor zero. Categoria a
          +dicional que só existe em algum centro de custo específico (ex
          +.: `representacao`) é reembolsável apenas para os centros que 
          +a declaram; para os demais, é `CATEGORIA_FORA_POLITICA` (RN-00
          +7) — ausência da categoria em toda tabela aplicável —, não `CA
          +TEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, que pressupõe a catego
          +ria presente com limite zero.                                 
      479 +**Origem:** comunicado do RH, política v4, item A (Dia 2).    
      480 +**Aceite:** colaborador de `CC-ENG-PLATAFORMA` com hospedagem 
          +de R$480,00 é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_
          +CUSTO`, reembolsável `0,00` — não `PARCIALMENTE_REEMBOLSADO`. 
          +Colaborador de `CC-ADM` com hospedagem — categoria ausente da 
          +entrada de `CC-ADM`, presente em `padrao` — também é recusado,
          + com o mesmo motivo, e não recebe o limite de `padrao`. Colabo
          +rador de `CC-SUPORTE-N2` (fora da tabela) com alimentação de R
          +$58,00 usa o limite de `padrao` (R$60,00) e é `INTEGRALMENTE_R
          +EEMBOLSADO`.                                                  
      481 +                                                              
      482 +### RN-020 — Resolução de câmbio e conversão monetária        
      483 +                                                              
      484 +**Regra:** `despesa.moeda` igual a `BRL`, ou ausente, tem taxa
          + implícita `1` e não consulta a tabela de câmbio. Para moeda e
          +strangeira estruturalmente válida, procura-se primeiro a cotaç
          +ão exata na data da despesa; sem cotação exata, usa-se a cotaç
          +ão mais recente cuja data seja **anterior** à data da despesa 
          +— nunca uma cotação futura, e sem interpolação entre datas. Se
          + não existir cotação exata nem anterior utilizável para aquela
          + moeda — porque ela não aparece em `cambio.json` em nenhuma da
          +ta, ou porque sua primeira cotação disponível é posterior à da
          +ta da despesa —, o item é recusado com `MOEDA_SEM_COTACAO`; `v
          +alor_normalizado` permanece nulo, e nenhuma regra financeira p
          +osterior (RN-006 em diante) é avaliada. A conversão multiplica
          + o valor bruto pela taxa resolvida; o arredondamento para duas
          + casas (RN-004) acontece uma única vez, sobre o resultado dess
          +a multiplicação.                                              
      485 +**Origem:** comunicado do RH, política v4, item B (Dia 2).    
      486 +**Aceite:** EUR `30,00` datado num sábado sem cotação própria 
          +usa a cotação do último dia útil anterior disponível — ex.: `2
          +026-07-17` para uma despesa de `2026-07-18`. GBP sem nenhuma e
          +ntrada em `cambio.json`, em qualquer data, é recusado com `MOE
          +DA_SEM_COTACAO`. USD `40,00` × `5,50` normaliza para `220,00`,
          + com um único arredondamento.                                 
      487 +                                                              
      488 +### RN-021 — Vigência do arquivo de política                  
      489 +                                                              
      490 +**Regra:** `politica.vigencia` é obrigatória, deve ser texto n
          +o formato `AAAA-MM-DD` e representar uma data real do calendár
          +io; sua ausência, nulo, tipo inválido, formato inválido ou dat
          +a inexistente invalidam o **arquivo de política** por inteiro 
          +— nenhuma apuração é produzida, o mesmo tratamento que RN-001 
          +dá ao envelope de despesas. Quando válida, `vigencia` é metada
          +do informativo: a política carregada vale integralmente para t
          +oda a execução, e nenhum item é recusado ou filtrado por sua `
          +data` ser anterior a `vigencia`. Esta versão não seleciona aut
          +omaticamente entre políticas históricas por data, porque nenhu
          +ma política externa anterior à v4 foi fornecida ao motor.     
      491 +**Origem:** comunicado do RH, política v4 — "vigência imediata
          +, retroativa à competência atual" (Dia 2).                    
      492 +**Aceite:** arquivo de política sem `vigencia` não produz apur
          +ação alguma. Arquivo de política com `vigencia: "2026-07-01"` 
          +processa normalmente despesas de `2026-04-15` sem recusá-las p
          +or causa da vigência — a competência (RN-008) continua sendo a
          + única regra temporal que afeta itens individuais.            
      493 +                                                              
      494 +### RN-022 — Processabilidade dos arquivos externos de polític
          +a e câmbio                                                    
      495 +                                                              
      496 +**Regra:** a apuração só ocorre quando os arquivos de política
          + e de câmbio são, cada um, legíveis e sintaticamente válidos c
          +omo JSON, e estruturalmente válidos conforme 4.1.1 e RN-021. F
          +alha em qualquer um dos dois arquivos — ausência, ilegibilidad
          +e, JSON sintaticamente inválido, ou estrutura que não satisfaç
          +a o contrato — impede toda a apuração do arquivo de despesas, 
          +mesmo que este esteja perfeitamente válido; nenhum resultado p
          +arcial é produzido. É erro global, na mesma classe de gravidad
          +e de um arquivo de entrada ilegível — mais grave que um envelo
          +pe de despesas inválido (RN-001), que ainda pressupõe política
          + e câmbio utilizáveis.                                        
      497 +**Origem:** contrato de entrada (lacuna do comunicado, decorre
          +nte da introdução dos arquivos externos — política v4, Dia 2).
      498 +**Aceite:** arquivo de câmbio com JSON sintaticamente inválido
          + não produz apuração alguma, mesmo que o arquivo de despesas e
          + o arquivo de política sejam válidos. Arquivo de política sem 
          +o bloco `padrao` não produz apuração alguma.                  
      499 +                                                              
      500  ---
      501  
      502  ## 6. Ambiguidades identificadas e decisões

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      504  > Esta seção é o coração da spec. Uma ambiguidade resolvida no
            código sem registro
      505  > aqui conta como não resolvida.
      506  
      507 -Dezoito ambiguidades e as subdecisões que cada uma gerou. Clas
          -sificação: **U** unidade de aplicação · **F** fronteira · **D*
          -* dado ausente · **O** outra.                                 
      507 +Dezoito ambiguidades da linha de base (`AMB-001` a `AMB-018`) 
          +e as subdecisões que cada uma gerou, mais quinze ambiguidades 
          +novas do envelope do Dia 2, política v4 (`AMB-019` a `AMB-033`
          +) — trinta e três no total. Classificação: **U** unidade de ap
          +licação · **F** fronteira · **D** dado ausente · **O** outra. 
      508  
      509  ### AMB-001 — Unidade do limite diário · U
      510  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 121 lines, removed 1 line
      663  **Decisão:** toda posição da lista `despesas` aparece exatamen
           te uma vez, com `indice_entrada`, o `id` original quando dispo
           nível, valor informado, valor normalizado quando calculável, v
           alor reembolsável, uma única decisão final e uma ou mais justi
           ficativas vinculadas às regras. Cada justificativa é um **obje
           to** com `codigo`, `regra` e `campo` — não um código solto —, 
           porque um código sozinho não diz de qual campo o defeito veio 
           nem qual regra o produziu, e as duas informações são exatament
           e o que torna a decisão auditável.
      664  **Justificativa:** nenhum lançamento pode desaparecer do resul
           tado; colaborador e financeiro precisam auditar individualment
           e cada decisão e chegar, a partir dela, à regra que a gerou.
      665  **Regra afetada:** RN-017
      666 -**Subdecisão AMB-018/ESCOPO:** `colaborador.id`, `colaborador.
          -nome`, `colaborador.centro_custo` e `periodo.competencia` são 
          -metadados opcionais, preservados na saída para rastreabilidade
          - e nulos quando ausentes, malformados ou de tipo inesperado; n
          -unca alteram resultado financeiro nem invalidam arquivo ou ite
          -m. BRL é a moeda presumida.                                   
      666 +**Subdecisão AMB-018/ESCOPO:** `colaborador.id`, `colaborador.
          +nome`, `colaborador.centro_custo` e `periodo.competencia` são 
          +metadados opcionais, preservados na saída para rastreabilidade
          + e nulos quando ausentes, malformados ou de tipo inesperado; n
          +unca alteram resultado financeiro nem invalidam arquivo ou ite
          +m. BRL é a moeda presumida quando `despesa.moeda` está ausente
          + (RN-020; política v4, Dia 2, alterou o que acontece quando a 
          +moeda **está** presente — ver AMB-023 a AMB-029).             
      667  
      668 +### AMB-019 — Centro de custo desconhecido, ausente, nulo ou d
          +e tipo inválido · D                                           
      669 +                                                              
      670 +**Texto original do RH:** "Alguns centros de custo não têm ent
          +rada na tabela. Nesse caso, aplica-se a política padrão."     
      671 +**O que não está claro:** o texto cobre explicitamente "descon
          +hecido" (fora da tabela), mas `colaborador.centro_custo` é cam
          +po opcional e tolerante (RN-001) — o comunicado não fala de au
          +sência, nulo ou tipo inválido do próprio campo.               
      672 +**Decisão:** ausente, nulo, de tipo inválido, ou presente mas 
          +sem entrada na tabela, recebem o mesmo tratamento — política `
          +padrao` integral, sem invalidar arquivo ou item.              
      673 +**Justificativa:** estende o princípio de tolerância já estabe
          +lecido para o bloco `colaborador` (RN-001) e é a leitura mais 
          +direta de "aplica-se a política padrão" quando não há centro d
          +e custo identificável.                                        
      674 +**Regra afetada:** RN-019                                     
      675 +                                                              
      676 +### AMB-020 — Categoria ausente em centro de custo cadastrado 
          +· F                                                           
      677 +                                                              
      678 +**Texto original do RH:** "Alguns centros de custo não têm ent
          +rada na tabela. Nesse caso, aplica-se a política padrão." `CC-
          +ADM`, em `politica-v4.json`, não declara `hospedagem`.        
      679 +**O que não está claro:** o texto resolve centro de custo ause
          +nte **da tabela inteira**; não resolve categoria ausente **den
          +tro** de um centro de custo que está cadastrado.              
      680 +**Decisão:** categoria ausente da tabela de um centro de custo
          + cadastrado não recebe o valor de `padrao` por fallback — é tr
          +atada como não reembolsável para aquele centro de custo (`CATE
          +GORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`).                        
      681 +**Justificativa:** um centro de custo que aparece na tabela é,
          + pela própria existência da entrada, uma declaração explícita 
          +das categorias que cobre; presumir fallback por categoria torn
          +aria a ausência de uma entrada indistinguível de um esquecimen
          +to do financeiro.                                             
      682 +**Regra afetada:** RN-019                                     
      683 +                                                              
      684 +### AMB-021 — Categoria `representacao` exclusiva de quem a de
          +clara · F                                                     
      685 +                                                              
      686 +**Texto original do RH:** "`CC-COMERCIAL` tem uma categoria no
          +va, `representacao`, que não existia na v3."                  
      687 +**O que não está claro:** "categoria nova" poderia significar 
          +reembolsável globalmente (com algum limite implícito nos centr
          +os que não a declaram) ou reembolsável apenas onde declarada. 
      688 +**Decisão:** `representacao` é reembolsável somente nos centro
          +s de custo que a declaram explicitamente; nos demais — incluin
          +do a política padrão —, é `CATEGORIA_FORA_POLITICA`.          
      689 +**Justificativa:** o texto atribui a categoria especificamente
          + a `CC-COMERCIAL`; inventar um limite implícito para os demais
          + centros de custo criaria uma regra financeira sem base no com
          +unicado.                                                      
      690 +**Regra afetada:** RN-007, RN-019                             
      691 +                                                              
      692 +### AMB-022 — Significado de limite igual a zero · O          
      693 +                                                              
      694 +**Texto original do RH:** `CC-ENG-PLATAFORMA.hospedagem.limite
          + = 0.00`, com `"observacao": "nao reembolsavel"`.             
      695 +**O que não está claro:** poderia ser um teto normal de valor 
          +zero (item elegível, cortado a zero, `PARCIALMENTE_REEMBOLSADO
          +`) ou uma recusa de política (item nunca elegível, `RECUSADO`)
          +.                                                             
      696 +**Decisão:** limite explicitamente igual a `0,00` recusa o ite
          +m com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`. Nunca é class
          +ificado como reembolso parcial de valor zero.                 
      697 +**Justificativa:** a observação do próprio financeiro ("não re
          +embolsável") descreve uma condição de elegibilidade, não um te
          +to — e um teto de valor zero é uma contradição em termos: teto
          + pressupõe algo a cortar.                                     
      698 +**Regra afetada:** RN-019                                     
      699 +                                                              
      700 +### AMB-023 — Moeda estrangeira como indício de viagem · D    
      701 +                                                              
      702 +**Texto original do RH:** comunicado B trata apenas de convers
          +ão; RN-016 (linha de base) declara efeito nulo porque "a entra
          +da não possui campo estruturado que informe viagem".          
      703 +**O que não está claro:** `despesa.moeda` é, agora, um campo e
          +struturado — poderia ser lido como o campo que faltava para at
          +ivar o item 6 da política (ampliação de 50%).                 
      704 +**Decisão:** `despesa.moeda` diferente de `BRL` não ativa a am
          +pliação de 50%, não afeta outros itens do mesmo dia, não afeta
          + o período, e RN-016 continua sem efeito algum.               
      705 +**Justificativa:** `moeda` é a unidade monetária do lançamento
          +, não uma condição de deslocamento do colaborador — pode haver
          + despesa em moeda estrangeira sem viagem (assinatura cobrada e
          +m USD) e viagem sem moeda estrangeira; o comunicado nunca cone
          +cta os dois conceitos explicitamente. Conectar seria decisão d
          +e produto, não leitura técnica.                               
      706 +**Regra afetada:** RN-016                                     
      707 +                                                              
      708 +### AMB-024 — Data da despesa sem cotação exata · F           
      709 +                                                              
      710 +**Texto original do RH:** "A conversão usa a taxa da data da d
          +espesa." `cambio.json` só publica dias úteis bancários.       
      711 +**O que não está claro:** o texto pressupõe cotação exata semp
          +re disponível — o próprio arquivo de câmbio mostra que isso é 
          +falso por construção (fins de semana, feriados).              
      712 +**Decisão:** sem cotação exata, usa-se a cotação mais recente 
          +cuja data seja anterior à data da despesa. Nunca uma cotação f
          +utura. Sem interpolação entre datas.                          
      713 +**Justificativa:** é a leitura financeira convencional de "cot
          +ação de fechamento" ("PTAX de fechamento", nota do próprio `ca
          +mbio.json`) — a cotação de sexta-feira permanece vigente no fi
          +m de semana.                                                  
      714 +**Regra afetada:** RN-020                                     
      715 +                                                              
      716 +### AMB-025 — Moeda sem qualquer cotação utilizável · D       
      717 +                                                              
      718 +**Texto original do RH:** nenhum — decorre da ausência de dado
          +s em `cambio.json` para determinadas moedas em qualquer data. 
      719 +**O que não está claro:** o comunicado não prevê moeda sem nen
          +huma cotação, exata ou anterior, disponível.                  
      720 +**Decisão:** o item é recusado com `MOEDA_SEM_COTACAO`. Moeda 
          +ausente de toda a tabela e moeda cuja primeira cotação disponí
          +vel é posterior à despesa recebem o mesmo tratamento — em ambo
          +s os casos não existe cotação anterior utilizável.            
      721 +**Justificativa:** inventar uma taxa (ex.: presumir paridade 1
          +:1) corromperia o valor reembolsável silenciosamente; recusar 
          +o item preserva o restante do arquivo, no mesmo espírito de to
          +do erro estrutural por item.                                  
      722 +**Regra afetada:** RN-020                                     
      723 +                                                              
      724 +### AMB-026 — Ordem de conversão e arredondamento · F         
      725 +                                                              
      726 +**Texto original do RH:** nenhum — decorre da combinação entre
          + RN-004 (arredondamento de duas casas) e a conversão cambial i
          +ntroduzida pelo item B.                                       
      727 +**O que não está claro:** se a conversão acontece antes ou dep
          +ois do arredondamento de RN-004, e quantas vezes o arredondame
          +nto ocorre.                                                   
      728 +**Decisão:** multiplica-se o valor bruto pela taxa resolvida; 
          +o arredondamento para duas casas acontece uma única vez, sobre
          + o resultado dessa multiplicação.                             
      729 +**Justificativa:** um único arredondamento, no fim da cadeia, 
          +evita erro de arredondamento composto e mantém RN-004 como o ú
          +ltimo passo antes de qualquer regra financeira usar o número —
          + não um passo intermediário repetido.                         
      730 +**Regra afetada:** RN-004, RN-020                             
      731 +                                                              
      732 +### AMB-027 — Base de comparação do gatilho de nota fiscal com
          + moeda estrangeira · U                                        
      733 +                                                              
      734 +**Texto original do RH:** "Os limites da política são sempre e
          +m BRL. Uma despesa em EUR é convertida antes de ser comparada 
          +ao limite."                                                   
      735 +**O que não está claro:** o texto fala de "limites da política
          +" (categorias); o gatilho de nota fiscal é um campo separado (
          +`nota_fiscal_obrigatoria_acima_de`) — cabe dúvida se ele está 
          +incluído.                                                     
      736 +**Decisão:** o gatilho de R$100 compara o valor normalizado, j
          +á convertido para BRL — nunca o valor original na moeda da des
          +pesa.                                                         
      737 +**Justificativa:** mesmo raciocínio já registrado em AMB-005 p
          +ara a linha de base — a obrigatoriedade documental não pode de
          +pender da moeda em que a despesa foi lançada; USD 40,00 (abaix
          +o do gatilho) e R$220,00 convertidos são economicamente a mesm
          +a despesa.                                                    
      738 +**Regra afetada:** RN-009, RN-020                             
      739 +                                                              
      740 +### AMB-028 — Chave de duplicidade entre moedas diferentes · U
      741 +                                                              
      742 +**Texto original do RH:** nenhum — decorre da combinação entre
          + RN-010 (linha de base) e a introdução de `moeda`.            
      743 +**O que não está claro:** se dois itens em moedas diferentes, 
          +com valor convertido coincidente, deveriam ser tratados como d
          +uplicata.                                                     
      744 +**Decisão:** `moeda` entra na chave de duplicidade. Itens em m
          +oedas diferentes nunca são duplicados entre si, mesmo que o va
          +lor convertido coincida exatamente.                           
      745 +**Justificativa:** duplicidade é sobre o mesmo lançamento rela
          +tado duas vezes; comparar por valor convertido tornaria a dupl
          +icidade dependente da taxa de câmbio do dia, em vez de depende
          +r só da identidade do lançamento.                             
      746 +**Regra afetada:** RN-010                                     
      747 +                                                              
      748 +### AMB-029 — Campos de auditoria da conversão na saída · O   
      749 +                                                              
      750 +**Texto original do RH:** nenhum — a spec 1.1 não previa esque
          +ma de saída para conversão cambial.                           
      751 +**O que não está claro:** quais campos tornam a conversão audi
          +tável sem exigir reabrir `cambio.json` manualmente.           
      752 +**Decisão:** três campos novos por registro — `moeda`, `taxa_c
          +ambio_aplicada` e `data_cotacao_utilizada` — entre `valor_info
          +rmado` e `valor_normalizado`. Não existe campo separado `valor
          +_convertido`: esse papel é de `valor_normalizado`.            
      753 +**Justificativa:** sem esses campos, um item convertido é opac
          +o para quem audita — contraria o objetivo de rastreabilidade d
          +a spec (`§2`).                                                
      754 +**Regra afetada:** RN-017, RN-020                             
      755 +                                                              
      756 +### AMB-030 — Validação de `politica.vigencia` · F            
      757 +                                                              
      758 +**Texto original do RH:** "Vigência imediata, retroativa à com
          +petência atual."                                              
      759 +**O que não está claro:** se `vigencia` é puramente informativ
          +a ou se deve ser validada estruturalmente, e se afeta a elegib
          +ilidade de itens por data.                                    
      760 +**Decisão:** `vigencia` é obrigatória e validada estruturalmen
          +te no arquivo de política (formato `AAAA-MM-DD`, data real) — 
          +sua ausência ou malformação invalida o arquivo de política int
          +eiro. Uma vez válida, é metadado informativo: não recusa itens
          + anteriores à vigência, e esta versão não seleciona automatica
          +mente entre políticas históricas.                             
      761 +**Justificativa:** não existe política externa anterior à v4 f
          +ornecida ao motor — validar `vigencia` contra os itens exigiri
          +a uma política alternativa que não existe; mas o campo em si é
          + obrigatório porque compõe o contrato mínimo do arquivo de pol
          +ítica.                                                        
      762 +**Regra afetada:** RN-021                                     
      763 +                                                              
      764 +### AMB-031 — Erros estruturais nos arquivos externos: erro gl
          +obal versus recusa individual · O                             
      765 +                                                              
      766 +**Texto original do RH:** nenhum — decorre da introdução dos a
          +rquivos externos de política e câmbio.                        
      767 +**O que não está claro:** se um arquivo externo malformado inv
          +alida toda a execução, ou se cada situação decorrente dele (ex
          +.: moeda sem cotação) é tratada item a item.                  
      768 +**Decisão:** arquivo de política ou de câmbio ausente, ilegíve
          +l, sintaticamente inválido ou estruturalmente inválido é erro 
          +global — nenhuma apuração é produzida. Ausência de cotação uti
          +lizável para uma despesa específica, e `despesa.moeda` estrutu
          +ralmente inválida, recusam somente o item.                    
      769 +**Justificativa:** espelha a distinção já estabelecida entre R
          +N-001 (envelope, erro global) e RN-002 (item, recusa individua
          +l) — um arquivo externo quebrado compromete toda a execução; u
          +m dado de negócio ausente para um item específico compromete s
          +ó aquele item.                                                
      770 +**Regra afetada:** RN-020, RN-022                             
      771 +                                                              
      772 +### AMB-032 — Regressão da baseline histórica sob a política v
          +4 · O                                                         
      773 +                                                              
      774 +**Texto original do RH:** nenhum — decorre de `exemplos/despes
          +as-exemplo.json` usar `colaborador.centro_custo = "CC-ENG-PLAT
          +AFORMA"`, que tem entrada própria em `politica-v4.json`.      
      775 +**O que não está claro:** se o fixture histórico (R$585,43) de
          +veria ser atualizado para refletir a política v4 aplicada ao c
          +entro de custo real do colaborador, ou preservado como está.  
      776 +**Decisão:** as duas coisas coexistem como cenários distintos.
          + A baseline histórica, sob a política padrão, continua totaliz
          +ando R$585,43 (CA-037). O mesmo arquivo, sob a política v4 ofi
          +cial e `CC-ENG-PLATAFORMA`, totaliza R$351,43 (CA-038) — mudan
          +ça correta e esperada, não uma regressão de código.           
      777 +**Justificativa:** escolher entre "manter" e "atualizar" o fix
          +ture original é uma falsa dicotomia técnica escondendo uma dec
          +isão de produto; preservar ambos documenta tanto o comportamen
          +to histórico quanto o comportamento correto sob a v4, sem que 
          +a mudança de total seja confundida com um bug quando o código 
          +for atualizado.                                               
      778 +**Regra afetada:** RN-019 (nenhuma regra da linha de base foi 
          +invalidada em si — o resultado muda porque a política aplicada
          + muda)                                                        
      779 +                                                              
      780 +### AMB-033 — Escopo do item C (fila de aprovação manual) · O 
      781 +                                                              
      782 +**Texto original do RH:** "(Opcional — só se sobrar tempo) Fil
          +a de aprovação manual. Itens cujo valor reembolsável passe de 
          +R$500 não são mais aprovados automaticamente."                
      783 +**O que não está claro:** nada de interpretação — o próprio co
          +municado marca o item como opcional; a decisão aqui é de escop
          +o, não de leitura de texto.                                   
      784 +**Decisão:** não implementado nesta rodada. Nenhum estado `AGU
          +ARDANDO_APROVACAO`, nenhuma fila, nenhuma alteração de decisão
          + por valor acima de R$500.                                    
      785 +**Justificativa:** o comunicado prioriza explicitamente os ite
          +ns A e B; nenhum item dos dois arquivos de exemplo do envelope
          + ultrapassa R$500 sob a interpretação adotada, então nada nos 
          +dados obriga essa decisão agora.                              
      786 +**Regra afetada:** nenhuma (fora de escopo)                   
      787 +                                                              
      788  ---
      789  
      790  ## 7. Casos de borda

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 11 lines
      828  | Período invertido | `inicio` > `fim` | Envelope inválido; ne
           nhuma apuração | RN-001 |
      829  | Competência ausente | Sem `periodo.competencia` | Arquivo vá
           lido; metadado nulo na saída | RN-001 |
      830  | Colaborador ausente | Sem bloco `colaborador` | Arquivo váli
           do; metadados nulos | RN-001 |
      831 +| Limite de categoria igual a `0,00` no centro de custo | Hosp
          +edagem em `CC-ENG-PLATAFORMA` | Recusado, `CATEGORIA_NAO_REEMB
          +OLSAVEL_CENTRO_CUSTO` — nunca `PARCIALMENTE_REEMBOLSADO` | RN-
          +019 |                                                         
      832 +| Categoria ausente na tabela do centro de custo cadastrado | 
          +Hospedagem em `CC-ADM` | Recusado, `CATEGORIA_NAO_REEMBOLSAVEL
          +_CENTRO_CUSTO` — sem fallback para `padrao` | RN-019 |        
      833 +| Centro de custo fora da tabela | `CC-SUPORTE-N2` | Usa `padr
          +ao` integralmente | RN-019 |                                  
      834 +| Categoria só existente em outro centro de custo | `represent
          +acao` fora de `CC-COMERCIAL` | Recusado, `CATEGORIA_FORA_POLIT
          +ICA` | RN-007, RN-019 |                                       
      835 +| Data da despesa sem cotação exata | EUR num sábado sem cotaç
          +ão própria | Usa a cotação do último dia útil anterior | RN-02
          +0 |                                                           
      836 +| Moeda sem qualquer cotação em `cambio.json` | GBP | Recusado
          +, `MOEDA_SEM_COTACAO` | RN-020 |                              
      837 +| Moeda estrangeira acima do gatilho de nota, sem nota | USD `
          +40,00` convertido a `220,00`, sem nota | Recusado, `NOTA_FISCA
          +L_AUSENTE` — compara o valor convertido | RN-009, RN-020 |    
      838 +| Moedas diferentes, mesmo valor convertido | EUR e BRL coinci
          +dentes após conversão | **Não** são duplicatas | RN-010 |     
      839 +| Moeda ausente | `despesa.moeda` não informado | Assume `BRL`
          +; sem motivo estrutural | RN-002 |                            
      840 +| Moeda em caixa baixa | `"usd"` | `CAMPO_FORMATO_INVALIDO` | 
          +RN-002 |                                                      
      841 +| Vigência da política ausente | Arquivo de política sem `vige
          +ncia` | Arquivo de política inválido; nenhuma apuração | RN-02
          +1 |                                                           
      842  
      843  ---
      844  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 15 lines, removed 11 lines
      850  
      851  | # | Passo |
      852  |---|---|
      853 -| 1 | Validar o envelope do arquivo |                         
      854 -| 2 | Validar presença, tipo e formato dos campos de cada item
          - |                                                            
      855 -| 3 | Identificar `despesa.id` válidos repetidos e recusar tod
          -as as respectivas ocorrências |                               
      856 -| 4 | Normalizar os campos estruturalmente válidos: valor e ca
          -tegoria |                                                     
      857 -| 5 | Avaliar as regras individuais de negócio aplicáveis |   
      858 -| 6 | Selecionar os itens aprovados em todas as validações ind
          -ividuais |                                                    
      859 -| 7 | Detectar e tratar a duplicidade econômica nessa populaçã
          -o |                                                           
      860 -| 8 | Selecionar os itens elegíveis após a duplicidade |      
      861 -| 9 | Aplicar agregação e tetos |                             
      862 -| 10 | Produzir uma saída por item, na ordem da entrada |     
      863 -| 11 | Somar os valores reembolsáveis apresentados |          
      853 +| 1 | Validar os arquivos externos de política e câmbio (RN-02
          +1, RN-022) |                                                  
      854 +| 2 | Validar o envelope do arquivo de despesas (RN-001) |    
      855 +| 3 | Validar presença, tipo e formato dos campos de cada item
          +, incluindo `despesa.moeda` (RN-002) |                        
      856 +| 4 | Identificar `despesa.id` válidos repetidos e recusar tod
          +as as respectivas ocorrências (RN-003) |                      
      857 +| 5 | Resolver a taxa de câmbio aplicável e converter o valor 
          +para BRL quando `despesa.moeda` for estrangeira (RN-020) |    
      858 +| 6 | Normalizar os campos estruturalmente válidos: valor (já 
          +convertido) e categoria (RN-004, RN-005) |                    
      859 +| 7 | Avaliar as regras individuais de negócio aplicáveis, inc
          +luindo a elegibilidade por política do centro de custo (RN-006
          + a RN-009, RN-019) |                                          
      860 +| 8 | Selecionar os itens aprovados em todas as validações ind
          +ividuais |                                                    
      861 +| 9 | Detectar e tratar a duplicidade econômica nessa populaçã
          +o (RN-010) |                                                  
      862 +| 10 | Selecionar os itens elegíveis após a duplicidade |     
      863 +| 11 | Aplicar agregação e tetos, com os limites resolvidos pe
          +la política do centro de custo (RN-011 a RN-015, RN-019) |    
      864 +| 12 | Produzir uma saída por item, na ordem da entrada, inclu
          +indo os campos de auditoria de câmbio (RN-017) |              
      865 +| 13 | Somar os valores reembolsáveis apresentados (RN-018) | 
      866  
      867 +Os passos 1 e 2 a 4 são heranças diretas de RN-001/RN-002/RN-0
          +03 (linha de base); os passos 5, 7 (parcialmente) e 11 (parcia
          +lmente) incorporam a política v4 (Dia 2, câmbio e política por
          + centro de custo). Os passos 4 e 5 podem ser vistos como paral
          +elos entre si (nenhum depende do resultado do outro), mas são 
          +apresentados em sequência para manter uma única ordem determin
          +ística e testável.                                            
      868 +                                                              
      869  ### 8.2 Matriz de dependências
      870  
      871  Cada regra depende dos campos abaixo. Uma regra não é avaliada
            quando algum campo de que depende está estruturalmente inváli
           do.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 6 lines, removed 5 lines
      873  | Regra | Campos necessários |
      874  |---|---|
      875  | Unicidade de `despesa.id` | `despesa.id` |
      876 -| Normalização monetária | `despesa.valor` |                  
      876 +| Resolução de câmbio / conversão monetária | `despesa.valor`,
          + `despesa.moeda`, `despesa.data` |                            
      877 +| Normalização monetária | `despesa.valor` (já convertido, qua
          +ndo aplicável) |                                              
      878  | Normalização de categoria | `despesa.categoria` |
      879  | Valor não positivo | `despesa.valor` |
      879 -| Categoria fora da política | `despesa.categoria` |          
      880 +| Categoria fora da política / política por centro de custo | 
          +`despesa.categoria`, `colaborador.centro_custo` |             
      881  | Competência | `despesa.data`, `periodo.inicio`, `periodo.fim
           ` |
      882  | Nota fiscal obrigatória | `despesa.valor`, `despesa.tem_nota
           _fiscal` |
      882 -| Duplicidade econômica | `despesa.data`, `despesa.categoria`,
          - `despesa.valor`, `despesa.fornecedor`, `despesa.descricao` | 
      883 -| Agregação e teto diário | `despesa.data`, `despesa.categoria
          -`, `despesa.valor` |                                          
      884 -| Teto individual de hospedagem | `despesa.categoria`, `despes
          -a.valor` |                                                    
      883 +| Duplicidade econômica | `despesa.data`, `despesa.categoria`,
          + `despesa.valor`, `despesa.moeda`, `despesa.fornecedor`, `desp
          +esa.descricao` |                                              
      884 +| Agregação e teto diário | `despesa.data`, `despesa.categoria
          +`, `despesa.valor`, `colaborador.centro_custo` |              
      885 +| Teto individual de hospedagem | `despesa.categoria`, `despes
          +a.valor`, `colaborador.centro_custo` |                        
      886  
      887  ### 8.3 Ordem de apresentação dos motivos
      888  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 10 lines, removed 6 lines
      889  A ordem de processamento e a ordem de apresentação são **duas 
           ordens determinísticas distintas**, com finalidades diferentes
           : a primeira determina quando cada regra é avaliada; a segunda
           , como os resultados são apresentados.
      890  
      891  1. `ITEM_TIPO_INVALIDO` — quando presente, é o motivo único da
            posição
      892 -2. Erros de campos estruturais, na ordem canônica de contrato:
          - `despesa.id`, `despesa.data`, `despesa.categoria`, `despesa.d
          -escricao`, `despesa.fornecedor`, `despesa.valor`, `despesa.tem
          -_nota_fiscal`                                                 
      892 +2. Erros de campos estruturais, na ordem canônica de contrato:
          + `despesa.id`, `despesa.data`, `despesa.categoria`, `despesa.d
          +escricao`, `despesa.fornecedor`, `despesa.valor`, `despesa.moe
          +da`, `despesa.tem_nota_fiscal`                                
      893  3. `ID_DUPLICADO`
      894 -4. `VALOR_NAO_POSITIVO`                                       
      895 -5. `CATEGORIA_FORA_POLITICA`                                  
      896 -6. `FORA_COMPETENCIA`                                         
      897 -7. `NOTA_FISCAL_AUSENTE`                                      
      898 -8. `DUPLICIDADE`                                              
      894 +4. `MOEDA_SEM_COTACAO` (política v4, Dia 2)                   
      895 +5. `VALOR_NAO_POSITIVO`                                       
      896 +6. `CATEGORIA_FORA_POLITICA`                                  
      897 +7. `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (política v4, Dia
          + 2)                                                           
      898 +8. `FORA_COMPETENCIA`                                         
      899 +9. `NOTA_FISCAL_AUSENTE`                                      
      900 +10. `DUPLICIDADE`                                             
      901  
      902 +Os motivos de limitação de teto (`TETO_DIARIO_APLICADO`, `TETO
          +_DIARIO_ESGOTADO`, `TETO_HOSPEDAGEM_APLICADO`) vêm depois de t
          +odos os anteriores, quando aplicáveis — na prática, aparecem s
          +ozinhos, porque um item que chega à etapa de teto já não carre
          +ga nenhum motivo de recusa.                                   
      903 +                                                              
      904  ### 8.4 Lista fechada de exclusões de aplicabilidade
      905  
      906  **Nenhuma outra exclusão pode ser inferida.**

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines
      918  11. Item recusado nas validações individuais não participa da 
           detecção de duplicidade econômica.
      919  12. Ocorrência recusada como duplicidade econômica não partici
           pa da agregação e não consome teto.
      920  13. Regras de teto alcançam somente os itens que permanecerem 
           elegíveis após todas as validações e a deduplicação.
      921 +14. Item com `MOEDA_SEM_COTACAO` não participa de nenhuma regr
          +a financeira posterior (RN-006 em diante), porque `valor_norma
          +lizado` é nulo — mesmo tratamento de dependência de campo estr
          +uturalmente inválido (item 3 acima), agora por ausência de dad
          +o externo em vez de defeito de contrato (política v4, Dia 2). 
      922 +15. Limite de política igual a `0,00` nunca produz `PARCIALMEN
          +TE_REEMBOLSADO`: o item é recusado com `CATEGORIA_NAO_REEMBOLS
          +AVEL_CENTRO_CUSTO` antes de qualquer teto ser avaliado — não é
          + uma variante de RN-013/RN-014 com teto zero (política v4, Dia
          + 2).                                                          
      923 +16. Arquivo de política ou de câmbio estruturalmente inválido 
          +impede toda a apuração, inclusive de despesas em BRL que não d
          +ependeriam de câmbio algum — o erro é do arquivo externo, não 
          +do item (política v4, Dia 2, RN-022).                         
      924  
      925  **Exemplo de motivos múltiplos:** item de R$ 500,00, categoria
            `coworking`, sem nota, datado fora da janela, com todos os ca
           mpos estruturalmente válidos, apresenta três motivos — `CATEGO
           RIA_FORA_POLITICA`, `FORA_COMPETENCIA` e `NOTA_FISCAL_AUSENTE`
           .
      926  
      927  **Exemplo de exclusão:** item de −R$ 500,00, categoria `cowork
           ing`, sem nota, apresenta `VALOR_NAO_POSITIVO` e `CATEGORIA_FO
           RA_POLITICA`, mas **não** apresenta `NOTA_FISCAL_AUSENTE`.
      928  
      929 +**Exemplo de motivo por política de centro de custo (Dia 2):**
          + um lançamento de hospedagem de R$480,00 em `CC-ENG-PLATAFORMA
          +`, com nota fiscal e dentro da competência, apresenta apenas `
          +CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — nenhum motivo de te
          +to é avaliado, porque a recusa por política do centro de custo
          + antecede a etapa de agregação (item 15 acima).               
      930 +                                                              
      931  ### 8.5 Estados possíveis por categoria
      932  
      933  | Categoria | Estados possíveis |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line
      935  | `alimentacao`, `transporte_urbano` | `INTEGRALMENTE_REEMBOLS
           ADO`, `PARCIALMENTE_REEMBOLSADO` (teto diário), `NAO_REEMBOLSA
           DO_TETO_ESGOTADO`, `RECUSADO` |
      936  | `hospedagem` | `INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_RE
           EMBOLSADO` (teto individual), `RECUSADO`. **Nunca** `NAO_REEMB
           OLSADO_TETO_ESGOTADO`, por não participar de saldo compartilha
           do |
      937  | Fora da política | Apenas `RECUSADO`; não alcança a etapa de
            teto |
      938 +| Com limite de política igual a `0,00` no centro de custo (Di
          +a 2) | Apenas `RECUSADO` (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_C
          +USTO`); não alcança a etapa de teto, mesmo sendo uma categoria
          + com estados de teto em outros centros de custo |             
      939  
      940  ---
      941  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 10 lines
      956  | 9 — Categorias fora da política | AMB-015 | RN-005, RN-007 |
            CA-015, CA-016 |
      957  | — (fora da política) | AMB-013, AMB-014, AMB-016, AMB-018 | 
           RN-001, RN-002, RN-003, RN-004, RN-006, RN-017, RN-018 | CA-00
           1, CA-002, CA-003, CA-017 a CA-023 |
      958  
      959 +**Comunicado do RH — Política v4 (Dia 2).** Mesma lógica, apli
          +cada ao envelope de `2026-08-05`:                             
      960 +                                                              
      961 +| Item do comunicado | Ambiguidades | Regras | Critérios de ac
          +eite |                                                        
      962 +|---|---|---|---|                                             
      963 +| A — Limites por centro de custo | AMB-019, AMB-020, AMB-021,
          + AMB-022 | RN-019 | CA-024 a CA-027, CA-038 a CA-040 |        
      964 +| B — Despesas internacionais (câmbio e conversão) | AMB-023, 
          +AMB-024, AMB-025, AMB-026, AMB-027, AMB-028, AMB-029 | RN-020,
          + RN-004, RN-009, RN-010 (atualizadas) | CA-028 a CA-034 |     
      965 +| — (vigência e arquivos externos) | AMB-030, AMB-031 | RN-021
          +, RN-022 | CA-035, CA-036 |                                   
      966 +| — (regressão) | AMB-032 | RN-019 | CA-037, CA-038 |         
      967 +| C — Fila de aprovação manual (opcional, fora de escopo) | AM
          +B-033 | — | — |                                               
      968 +                                                              
      969  ---
      970  
      971  ## 10. Critérios de aceite
● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 20 lines
       996  - [ ] **CA-022** — Um elemento de `despesas` que não seja obj
            eto produz exatamente um registro `RECUSADO`, com `indice_ent
            rada` preservado, `id` nulo, `valor_informado` nulo, `valor_n
            ormalizado` nulo, `valor_reembolsavel` R$ 0,00 e um único mot
            ivo `ITEM_TIPO_INVALIDO` com `campo` nulo — sem os sete motiv
            os de campo ausente —, e o restante do arquivo continua sendo
             apurado. Todo objeto de motivo da saída traz `campo` nulo ou
             um dos sete nomes canônicos de 4.2, e nenhum outro valor.
       997  - [ ] **CA-023** — Um item com `despesa.id` igual a texto vaz
            io, `despesa.data` igual a `"31/07/2026"`, `despesa.categoria
            ` igual a um número, `despesa.valor` igual a `"72,50"` e `des
            pesa.tem_nota_fiscal` igual a `"sim"` (demais campos válidos)
             é recusado com cinco motivos, na ordem canônica de contrato 
            — `CAMPO_FORMATO_INVALIDO` (`despesa.id`), `CAMPO_FORMATO_INV
            ALIDO` (`despesa.data`), `CAMPO_TIPO_INVALIDO` (`despesa.cate
            goria`), `CAMPO_TIPO_INVALIDO` (`despesa.valor`), `CAMPO_TIPO
            _INVALIDO` (`despesa.tem_nota_fiscal`). `valor_informado` pre
            serva `"72,50"`; `valor_normalizado` é nulo; `valor_reembolsa
            vel` é `0,00`.
       998  
       999 +**Envelope do Dia 2 — política v4:**                         
      1000 +                                                             
      1001 +- [ ] **CA-024** — Um colaborador de centro de custo desconhe
           +cido, ausente, nulo ou de tipo inválido usa integralmente a p
           +olítica `padrao` — nenhuma mistura com nenhuma tabela específ
           +ica.                                                         
      1002 +- [ ] **CA-025** — Categoria ausente na tabela de um centro d
           +e custo cadastrado (ex.: `hospedagem` em `CC-ADM`) é recusada
           + com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, sem receber o
           + limite de `padrao`.                                         
      1003 +- [ ] **CA-026** — `representacao` é reembolsável apenas nos 
           +centros de custo que a declaram explicitamente (`CC-COMERCIAL
           +`); em qualquer outro centro de custo, inclusive `padrao`, é 
           +`CATEGORIA_FORA_POLITICA`.                                   
      1004 +- [ ] **CA-027** — Categoria com limite de política igual a `
           +0,00` (ex.: `hospedagem` em `CC-ENG-PLATAFORMA`) é recusada c
           +om `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e `valor_reembol
           +savel` `0,00` — nunca `PARCIALMENTE_REEMBOLSADO`.            
      1005 +- [ ] **CA-028** — Numa despesa em moeda estrangeira, `moeda 
           +!= BRL` não amplia teto algum, não afeta outros itens do mesm
           +o dia nem do período — RN-016 continua sem efeito.           
      1006 +- [ ] **CA-029** — Uma despesa datada num dia sem cotação exa
           +ta usa a cotação do último dia útil anterior disponível (ex.:
           + EUR de `2026-07-18`, sábado, usa a cotação de `2026-07-17`).
      1007 +- [ ] **CA-030** — Uma despesa em moeda sem qualquer cotação 
           +em `cambio.json` (ex.: GBP) é recusada com `MOEDA_SEM_COTACAO
           +`, `valor_normalizado` nulo e `valor_reembolsavel` `0,00`.   
      1008 +- [ ] **CA-031** — A conversão multiplica o valor bruto pela 
           +taxa e arredonda uma única vez: USD `40,00` × `5,50` normaliz
           +a para `220,00`.                                             
      1009 +- [ ] **CA-032** — O gatilho de nota fiscal compara o valor c
           +onvertido, não o original: USD `40,00` (abaixo de R$100 na mo
           +eda original) convertido para R$220,00, sem nota fiscal, é re
           +cusado com `NOTA_FISCAL_AUSENTE`.                            
      1010 +- [ ] **CA-033** — Dois itens com mesma data, categoria, forn
           +ecedor, descrição e valor convertido coincidente, mas em moed
           +as diferentes, **não** são tratados como duplicata.          
      1011 +- [ ] **CA-034** — A saída traz `moeda`, `taxa_cambio_aplicad
           +a` e `data_cotacao_utilizada` nos quatro formatos: BRL (`1`, 
           +`null`), moeda estrangeira convertida (taxa e data da cotação
           + usada), moeda estruturalmente inválida (os três campos nulos
           +) e moeda válida sem cotação (os três campos nulos, mais `val
           +or_normalizado` nulo e `valor_reembolsavel` `0,00`).         
      1012 +- [ ] **CA-035** — Um arquivo de política sem `politica.vigen
           +cia`, ou com `vigencia` malformada, não produz apuração algum
           +a.                                                           
      1013 +- [ ] **CA-036** — Um arquivo de política ou de câmbio ausent
           +e, ilegível, sintaticamente inválido ou estruturalmente invál
           +ido impede toda a apuração, mesmo com um arquivo de despesas 
           +válido.                                                      
      1014 +- [ ] **CA-037** — `exemplos/despesas-exemplo.json`, processa
           +do com uma política externa equivalente aos limites histórico
           +s (política padrão: alimentação R$60/dia, transporte R$80/dia
           +, hospedagem R$250/diária, gatilho de nota R$100), totaliza `
           +total_reembolsavel` igual a **R$585,43** — baseline de regres
           +são histórica preservada.                                    
      1015 +- [ ] **CA-038** — O mesmo `exemplos/despesas-exemplo.json`, 
           +processado com a política oficial `politica-v4.json` e o cent
           +ro de custo real do colaborador (`CC-ENG-PLATAFORMA`), totali
           +za `total_reembolsavel` igual a **R$351,43**, com `d-001` int
           +egral (`72,50`), `d-002` parcial (`2,50`), `d-010` recusado (
           +`0,00`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`) e `d-014` 
           +integral (`61,00`).                                          
      1016 +- [ ] **CA-039** — `exemplos/envelope/despesas-envelope.json`
           + (Rafael Nkemelu, `CC-COMERCIAL`), processado com `politica-v
           +4.json` e `cambio.json`, totaliza `total_reembolsavel` igual 
           +a **R$1.143,26**, conforme a tabela de `§12`.                
      1017 +- [ ] **CA-040** — `exemplos/envelope/despesas-envelope-cc-de
           +sconhecido.json` (Dani Okonkwo, `CC-SUPORTE-N2`), processado 
           +com `politica-v4.json` e `cambio.json`, totaliza `total_reemb
           +olsavel` igual a **R$373,76**, conforme a tabela de `§12`.   
      1018 +                                                             
      1019  ---
      1020  
      1021  ## 11. O que fica em aberto

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines, removed 4 lines
      1028  | Hospedagem de várias diárias é limitada como se fosse uma |
             Um campo estruturado de quantidade de diárias, ou datas de e
            ntrada e saída |
      1029  | Estadia fragmentada em vários lançamentos recebe mais do qu
            e a mesma estadia em um lançamento | Um identificador de esta
            dia que permita reconhecer fragmentação com segurança |
      1030  | Estornos e créditos não compensam despesas | Uma política d
            e compensação que defina contra o quê o crédito abate e em qu
            e ordem |
      1031 -| Contexto de consumo é ignorado — fim de semana, plantão, re
           -presentação, refeição inclusa na hospedagem | Regras na polít
           -ica que criem limite, proibição ou tratamento distinto para e
           -sses contextos |                                             
      1032 -| Apenas BRL é suportado | Um campo de moeda e uma política d
           -e conversão cambial |                                        
      1031 +| Contexto de consumo é ignorado — fim de semana, plantão, re
           +feição inclusa na hospedagem | Regras na política que criem l
           +imite, proibição ou tratamento distinto para esses contextos.
           + Não se confunde com a categoria formal `representacao` (polí
           +tica v4, RN-019) — esta linha trata do contexto informal de c
           +onsumo dentro de uma categoria já reembolsável, não da catego
           +ria em si |                                                  
      1032  | A apuração não cruza arquivos nem colaboradores | Um contra
            to de entrada multi-arquivo e definição de qual limite é comp
            artilhado |
      1034 -| `coworking` não é reembolsável | Alteração da política, que
           - hoje não prevê a categoria |                                
      1033 +| `coworking` não é reembolsável em nenhum centro de custo | 
           +Alteração da política, que hoje não prevê a categoria em `pad
           +rao` nem em nenhum centro de custo cadastrado |              
      1034 +| Fila de aprovação manual (item C do comunicado do Dia 2) nã
           +o está implementada | Um estado novo (`AGUARDANDO_APROVACAO`)
           +, critérios de fila e de gestor responsável — deliberadamente
           + fora de escopo nesta rodada (AMB-033) |                     
      1035 +| Não há seleção automática de política histórica por vigênci
           +a | Uma segunda política externa anterior, com sua própria ja
           +nela de vigência, e uma regra de escolha entre elas quando ma
           +is de uma existir (RN-021 trata `vigencia` como informativa n
           +esta versão, porque só uma política existe) |                
      1036  
      1036 -**Perguntas que não puderam ser respondidas por ausência de i
           -nterlocutor.** A política v3 foi escrita pelo RH e não houve 
           -canal para esclarecimento. As leituras adotadas nas dezoito a
           -mbiguidades da seção 6 são decisões desta spec, não interpret
           -ações confirmadas pela área de origem. As que mais mudariam o
           - resultado se a área decidisse diferente: AMB-002 (corte no t
           -eto contra recusa integral), AMB-008 (uma diária por lançamen
           -to) e AMB-004 (ausência de nota recusa o item inteiro).      
      1037 +**Perguntas que não puderam ser respondidas por ausência de i
           +nterlocutor.** A política v3 foi escrita pelo RH e não houve 
           +canal para esclarecimento; o mesmo vale para a política v4. A
           +s leituras adotadas nas trinta e três ambiguidades da seção 6
           + são decisões desta spec, não interpretações confirmadas pela
           + área de origem. As que mais mudariam o resultado se a área d
           +ecidisse diferente: AMB-002 (corte no teto contra recusa inte
           +gral), AMB-008 (uma diária por lançamento) e AMB-004 (ausênci
           +a de nota recusa o item inteiro), da linha de base; e, do env
           +elope do Dia 2, AMB-020 (categoria ausente em centro de custo
           + cadastrado), AMB-022 (limite igual a zero) e AMB-024 (data s
           +em cotação exata) — as três com maior impacto financeiro obse
           +rvado nos arquivos de exemplo.                               

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 75 lines
      1035  | Não há seleção automática de política histórica por vigênci
            a | Uma segunda política externa anterior, com sua própria ja
            nela de vigência, e uma regra de escolha entre elas quando ma
            is de uma existir (RN-021 trata `vigencia` como informativa n
            esta versão, porque só uma política existe) |
      1036  
      1037  **Perguntas que não puderam ser respondidas por ausência de i
            nterlocutor.** A política v3 foi escrita pelo RH e não houve 
            canal para esclarecimento; o mesmo vale para a política v4. A
            s leituras adotadas nas trinta e três ambiguidades da seção 6
             são decisões desta spec, não interpretações confirmadas pela
             área de origem. As que mais mudariam o resultado se a área d
            ecidisse diferente: AMB-002 (corte no teto contra recusa inte
            gral), AMB-008 (uma diária por lançamento) e AMB-004 (ausênci
            a de nota recusa o item inteiro), da linha de base; e, do env
            elope do Dia 2, AMB-020 (categoria ausente em centro de custo
             cadastrado), AMB-022 (limite igual a zero) e AMB-024 (data s
            em cotação exata) — as três com maior impacto financeiro obse
            rvado nos arquivos de exemplo.
      1038 +                                                             
      1039 +---                                                          
      1040 +                                                             
      1041 +## 12. Resultados esperados — Envelope do Dia 2 (política v4)
      1042 +                                                             
      1043 +Esta seção documenta, de forma normativa e verificável sem le
           +itura de código, os quatro cenários financeiros completos do 
           +envelope do Dia 2 — a contraparte da `§4.7` para a política v
           +4. Todos os valores abaixo foram calculados manualmente a par
           +tir das regras desta spec (RN-019 a RN-022 e as regras da lin
           +ha de base atualizadas), não gerados por execução do motor, q
           +ue ainda não implementa esta versão.                         
      1044 +                                                             
      1045 +### 12.1 Baseline histórica (política padrão)                
      1046 +                                                             
      1047 +`exemplos/despesas-exemplo.json`, processado com uma política
           + externa de teste equivalente aos limites históricos (`padrao
           +`: alimentação R$60/dia, transporte R$80/dia, hospedagem R$25
           +0/diária; gatilho de nota R$100) — mesmos valores da `§4.7`: 
      1048 +                                                             
      1049 +```text                                                      
      1050 +total_reembolsavel = 585.43                                  
      1051 +```                                                          
      1052 +                                                             
      1053 +Essa baseline continua existindo como regressão histórica (CA
           +-037) e não depende de nenhum centro de custo cadastrado em `
           +politica-v4.json`.                                           
      1054 +                                                             
      1055 +### 12.2 `exemplos/despesas-exemplo.json` sob a política ofic
           +ial v4                                                       
      1056 +                                                             
      1057 +O mesmo arquivo, com `politica-v4.json` e o centro de custo r
           +eal do colaborador (`CC-ENG-PLATAFORMA`, cadastrado na tabela
           +: `alimentacao` R$75/dia, `transporte_urbano` R$80/dia, `hosp
           +edagem` R$0,00 — não reembolsável):                          
      1058 +                                                             
      1059 +```text                                                      
      1060 +total_reembolsavel = 351.43                                  
      1061 +```                                                          
      1062 +                                                             
      1063 +Mudanças obrigatórias em relação a `12.1`, todas decorrentes 
           +de RN-019:                                                   
      1064 +                                                             
      1065 +| Item | Categoria | `12.1` (padrão) | `12.2` (v4, `CC-ENG-PL
           +ATAFORMA`) | Motivo da mudança |                             
      1066 +|---|---|---|---|---|                                        
      1067 +| `d-001` | alimentacao | `60,00` (parcial) | **`72,50`** (in
           +tegral) | limite diário `60` → `75` |                        
      1068 +| `d-002` | alimentacao | `0,00` (esgotado) | **`2,50`** (par
           +cial) | saldo residual do dia sobe |                         
      1069 +| `d-010` | hospedagem | `250,00` (parcial) | **`0,00`** (rec
           +usado, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`) | limite ho
           +spedagem `250` → `0` |                                       
      1070 +| `d-014` | alimentacao | `60,00` (parcial) | **`61,00`** (in
           +tegral) | limite diário `60` → `75` |                        
      1071 +                                                             
      1072 +Os demais dez itens não mudam de decisão nem de valor entre o
           +s dois cenários. A diferença de R$234,00 é consequência diret
           +a e correta da política v4 aplicada ao centro de custo real d
           +o colaborador — não é regressão de código (AMB-032, CA-038). 
      1073 +                                                             
      1074 +### 12.3 Rafael Nkemelu — `CC-COMERCIAL` (`despesas-envelope.
           +json`)                                                       
      1075 +                                                             
      1076 +Política aplicável: `alimentacao` R$90/dia, `transporte_urban
           +o` R$150/dia, `hospedagem` R$400/diária, `representacao` R$30
           +0/dia (todas específicas de `CC-COMERCIAL`); gatilho de nota 
           +R$100 (nível política, único para toda a execução).          
      1077 +                                                             
      1078 +```text                                                      
      1079 +total_reembolsavel = 1143.26                                 
      1080 +```                                                          
      1081 +                                                             
      1082 +| Item | Categoria | Moeda / valor | Convertido (BRL) | Decis
           +ão | Reembolsável |                                          
      1083 +|---|---|---|---|---|---|                                    
      1084 +| `e-001` | representacao | BRL `340,00` | — | parcial (teto 
           +`300`) | `300,00` |                                          
      1085 +| `e-002` | alimentacao | EUR `22,00` @ `5,93` (`2026-07-14`)
           + | `130,46` | parcial (teto `90`) | `90,00` |                
      1086 +| `e-003` | alimentacao | EUR `14,50` @ `5,88` (`2026-07-15`)
           + | `85,26` | integral | `85,26` |                            
      1087 +| `e-004` | alimentacao | EUR `30,00`, cotação de `2026-07-17
           +` (última anterior a `2026-07-18`, sábado) | `178,80` | parci
           +al (teto `90`) | `90,00` |                                   
      1088 +| `e-005` | transporte_urbano | USD `40,00` @ `5,50` (`2026-0
           +7-20`) | `220,00` | recusado (`NOTA_FISCAL_AUSENTE`, valor co
           +nvertido > R$100 sem nota) | `0,00` |                        
      1089 +| `e-006` | representacao | GBP `55,00`, sem cotação em `camb
           +io.json` | — | recusado (`MOEDA_SEM_COTACAO`) | `0,00` |     
      1090 +| `e-007` | hospedagem | BRL `1200,00` | — | parcial (teto `4
           +00`) | `400,00` |                                            
      1091 +| `e-008` | alimentacao | BRL `95,00` | — | parcial (teto `90
           +`) | `90,00` |                                               
      1092 +| `e-009` | coworking | BRL `120,00` | — | recusado (`CATEGOR
           +IA_FORA_POLITICA`) | `0,00` |                                
      1093 +| `e-010` | alimentacao | BRL `88,00`, `moeda` ausente → assu
           +me BRL | — | integral | `88,00` |                            
      1094 +                                                             
      1095 +### 12.4 Dani Okonkwo — centro de custo desconhecido (`despes
           +as-envelope-cc-desconhecido.json`)                           
      1096 +                                                             
      1097 +`colaborador.centro_custo = "CC-SUPORTE-N2"`, fora da tabela 
           +de `politica-v4.json` → usa `padrao` integralmente (RN-019): 
           +`alimentacao` R$60/dia, `transporte_urbano` R$80/dia, `hosped
           +agem` R$250/diária.                                          
      1098 +                                                             
      1099 +```text                                                      
      1100 +total_reembolsavel = 373.76                                  
      1101 +```                                                          
      1102 +                                                             
      1103 +| Item | Categoria | Moeda / valor | Convertido (BRL) | Decis
           +ão | Reembolsável |                                          
      1104 +|---|---|---|---|---|---|                                    
      1105 +| `f-001` | alimentacao | BRL `58,00`, `moeda` ausente → assu
           +me BRL | — | integral | `58,00` |                            
      1106 +| `f-002` | hospedagem | BRL `310,00` | — | parcial (teto `25
           +0`, padrão) | `250,00` |                                     
      1107 +| `f-003` | representacao | BRL `190,00` | — | recusado (`CAT
           +EGORIA_FORA_POLITICA` — `padrao` não declara `representacao`)
           + | `0,00` |                                                  
      1108 +| `f-004` | transporte_urbano | USD `12,00` @ `5,48` (`2026-0
           +7-21`) | `65,76` | integral | `65,76` |                      
      1109 +                                                             
      1110 +### 12.5 Como usar esta seção                                
      1111 +                                                             
      1112 +Os quatro cenários acima são a fonte normativa de `CA-024` a 
           +`CA-040`. Divergência entre uma implementação e os valores de
           +sta seção é bug de código, não ambiguidade de spec — as ambig
           +uidades relevantes já foram decididas em `AMB-019` a `AMB-033
           +` e as regras correspondentes (`RN-019` a `RN-022`, mais `RN-
           +004`, `RN-007`, `RN-009`, `RN-010` e `RN-017` atualizadas). F
           +ixtures de teste automatizado para estes quatro cenários são 
           +trabalho de `tasks.md`, ainda não criado para esta versão.   

  Searched for 1 pattern (ctrl+o to expand)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      993  - [ ] **CA-019** — Dois itens com o mesmo `despesa.id` válido 
           são **ambos** recusados com `ID_DUPLICADO`.
      994  - [ ] **CA-020** — Uma entrada com `periodo.inicio` posterior 
           a `periodo.fim` não produz apuração alguma; uma entrada com `d
           espesas: []` produz resultados vazio e total R$ 0,00.
      995  - [ ] **CA-021** — Um item com `data: "31/07/2026"` e `valor: 
           "72,50"` apresenta dois motivos estruturais na ordem canônica 
           de contrato: `CAMPO_FORMATO_INVALIDO` com `campo` igual a `des
           pesa.data`, depois `CAMPO_TIPO_INVALIDO` com `campo` igual a `
           despesa.valor`.
      996 -- [ ] **CA-022** — Um elemento de `despesas` que não seja obje
          -to produz exatamente um registro `RECUSADO`, com `indice_entra
          -da` preservado, `id` nulo, `valor_informado` nulo, `valor_norm
          -alizado` nulo, `valor_reembolsavel` R$ 0,00 e um único motivo 
          -`ITEM_TIPO_INVALIDO` com `campo` nulo — sem os sete motivos de
          - campo ausente —, e o restante do arquivo continua sendo apura
          -do. Todo objeto de motivo da saída traz `campo` nulo ou um dos
          - sete nomes canônicos de 4.2, e nenhum outro valor.           
      996 +- [ ] **CA-022** — Um elemento de `despesas` que não seja obje
          +to produz exatamente um registro `RECUSADO`, com `indice_entra
          +da` preservado, `id` nulo, `valor_informado` nulo, `valor_norm
          +alizado` nulo, `valor_reembolsavel` R$ 0,00 e um único motivo 
          +`ITEM_TIPO_INVALIDO` com `campo` nulo — sem os sete motivos de
          + campo ausente —, e o restante do arquivo continua sendo apura
          +do. Todo objeto de motivo da saída traz `campo` nulo ou um dos
          + oito nomes canônicos de 4.2, e nenhum outro valor.           
      997  - [ ] **CA-023** — Um item com `despesa.id` igual a texto vazi
           o, `despesa.data` igual a `"31/07/2026"`, `despesa.categoria` 
           igual a um número, `despesa.valor` igual a `"72,50"` e `despes
           a.tem_nota_fiscal` igual a `"sim"` (demais campos válidos) é r
           ecusado com cinco motivos, na ordem canônica de contrato — `CA
           MPO_FORMATO_INVALIDO` (`despesa.id`), `CAMPO_FORMATO_INVALIDO`
            (`despesa.data`), `CAMPO_TIPO_INVALIDO` (`despesa.categoria`)
           , `CAMPO_TIPO_INVALIDO` (`despesa.valor`), `CAMPO_TIPO_INVALID
           O` (`despesa.tem_nota_fiscal`). `valor_informado` preserva `"7
           2,50"`; `valor_normalizado` é nulo; `valor_reembolsavel` é `0,
           00`.
      998  
      999  **Envelope do Dia 2 — política v4:**

  Searched for 1 pattern, read 1 file, ran 1 shell command (ctrl+o to expand)

● This looks solid. Let me continue reviewing the rest of the file for
  structural integrity.

  Read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      391  
      392  ### RN-005 — Normalização de categoria
      393  
      394 -**Regra:** `categoria` é normalizada por remoção de espaços da
          -s pontas, insensibilidade a caixa e insensibilidade a acentos.
          - O resultado deve corresponder exatamente a `alimentacao`, `tr
          -ansporte_urbano` ou `hospedagem`. Nenhuma outra transformação 
          -ocorre.                                                       
      395 -**Origem:** política do RH, item 9.                           
      394 +**Regra:** `categoria` é normalizada por remoção de espaços da
          +s pontas, insensibilidade a caixa e insensibilidade a acentos.
          + O resultado normalizado é comparado, por igualdade exata, con
          +tra o vocabulário de categorias reembolsáveis — `alimentacao`,
          + `transporte_urbano` e `hospedagem` em qualquer centro de cust
          +o, mais `representacao` e qualquer outra categoria que a polít
          +ica aplicável declarar (RN-007, RN-019, política v4, Dia 2). N
          +enhuma outra transformação ocorre.                            
      395 +**Origem:** política do RH, item 9; vocabulário de categorias 
          +variável por centro de custo acrescentado pela política v4 (Di
          +a 2).                                                         
      396  **Aceite:** `ALIMENTACAO`, `Alimentação` e ` alimentacao ` rec
           onhecem `alimentacao`. `transporte urbano` **não** reconhece `
           transporte_urbano`.
      397  
      398  ### RN-006 — Valor não positivo

● Now let me review the remaining sections (RN-007 through the end) to verify
  consistency.

  Read 1 file (ctrl+o to expand)

● This all reads consistently. Let me verify the remainder of the new AMB
  entries and the tail sections (§8, §9, §10, §11, §12).

  Read 1 file (ctrl+o to expand)

● All consistent. Let me check the final tail of the file (§8 onward through
  §12) for structural integrity.

  Read 1 file (ctrl+o to expand)

● Good — structurally sound throughout. Let me check the very end (§11 and §12).

  Read 1 file (ctrl+o to expand)

● spec.md is complete and consistent. Now let's update DECISIONS.md with D-003.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 50 lines
      10  
      11  ---
      12  
      13 +## D-003 — Envelope do Dia 2 e política v4 · `2026-08-05`      
      14 +                                                               
      15 +**Gatilho:** recebimento do envelope oficial do Dia 2, liberado
         + às 10h — mudança de requisito do RH, política de reembolso v4,
         + com vigência imediata e retroativa à competência atual. Cinco 
         +arquivos oficiais acompanham o envelope e foram commitados em `
         +exemplos/envelope/` sem alteração: `00-ENVELOPE-LACRADO.md` (co
         +municado do RH), `politica-v4.json` (tabela de limites por cent
         +ro de custo), `cambio.json` (taxas de câmbio por data), `despes
         +as-envelope.json` (Rafael Nkemelu, `CC-COMERCIAL`, exercitando 
         +moeda estrangeira) e `despesas-envelope-cc-desconhecido.json` (
         +Dani Okonkwo, `CC-SUPORTE-N2`, exercitando centro de custo fora
         + da tabela). Uma sessão de análise anterior a esta (modo leitur
         +a, sem alteração de arquivo) já havia mapeado o comunicado item
         + a item, calculado os quatro cenários financeiros abaixo manual
         +mente e identificado as ambiguidades candidatas contra a lista 
         +de análise obrigatória do próprio processo. Esta entrada formal
         +iza as decisões tomadas sobre essas ambiguidades e atualiza `sp
         +ec.md` para a versão 1.2.                                      
      16 +                                                               
      17 +**O que mudou na spec:**                                       
      18 +                                                               
      19 +1. **Política deixa de ser um valor único (RN-019, nova).** A s
         +pec 1.1 não previa política externa nem variação por centro de 
         +custo — RN-007 e RN-011/012/013 tratavam os limites como consta
         +ntes universais. A v1.2 introduz resolução por `colaborador.cen
         +tro_custo`: tabela `padrao` como fallback integral; centro de c
         +usto cadastrado usa somente as categorias que declara, sem fall
         +back por categoria; limite explicitamente `0,00` recusa a categ
         +oria (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`), nunca reembol
         +so parcial de valor zero; categoria adicional (`representacao`)
         + só é reembolsável nos centros que a declaram.                 
      20 +2. **Câmbio e conversão monetária (RN-020, nova).** A spec 1.1 
         +declarava explicitamente, em `§3`, que o sistema "não converte 
         +nem identifica moedas" e que "todos os valores são interpretado
         +s como reais brasileiros" — essa frase foi removida e substituí
         +da (ver "comportamento anterior invalidado" abaixo). `despesa.m
         +oeda` é novo campo opcional (oitavo campo de 4.2, entre `valor`
         + e `tem_nota_fiscal`); ausência resolve para `BRL` sem produzir
         + motivo; presença malformada recusa o item como qualquer campo 
         +estrutural. A resolução de taxa usa cotação exata ou a mais rec
         +ente anterior disponível, nunca futura, sem interpolação; moeda
         + sem cotação utilizável recusa o item (`MOEDA_SEM_COTACAO`). Co
         +nversão e arredondamento têm ordem única: multiplicar, depois a
         +rredondar uma vez (RN-004 atualizada).                         
      21 +3. **RN-009 (nota fiscal) atualizada.** O gatilho de R$100 agor
         +a compara explicitamente o valor já convertido para BRL, não o 
         +valor original na moeda da despesa — caso contrário, USD 40,00 
         +(abaixo do gatilho na moeda original) escaparia da obrigatoried
         +ade de nota apesar de valer R$220,00 convertido.               
      22 +4. **RN-010 (duplicidade) atualizada.** A chave econômica ganha
         + `moeda` como componente: dois itens em moedas diferentes nunca
         + são duplicatas entre si, mesmo que o valor convertido coincida
         + — a chave usa moeda mais valor, não o valor já convertido, par
         +a não tornar a duplicidade dependente da taxa de câmbio do dia.
      23 +5. **RN-017 e o contrato de saída (4.3) atualizados.** Todo reg
         +istro de resultado ganha três campos novos, entre `valor_inform
         +ado` e `valor_normalizado`: `moeda`, `taxa_cambio_aplicada` e `
         +data_cotacao_utilizada` — necessários para que a conversão seja
         + auditável sem reabrir `cambio.json` manualmente.              
      24 +6. **RN-021 (nova) — vigência do arquivo de política.** `politi
         +ca.vigencia` é obrigatória e validada estruturalmente (mesma se
         +veridade de RN-001, mas sobre o arquivo de política, não o de d
         +espesas); uma vez válida, é metadado informativo — não filtra i
         +tens por data, porque nenhuma política anterior à v4 foi fornec
         +ida para justificar seleção histórica.                         
      25 +7. **RN-022 (nova) — processabilidade dos arquivos externos.** 
         +Política ou câmbio ausente, ilegível, sintaticamente inválido o
         +u estruturalmente inválido é erro global: nenhuma apuração é pr
         +oduzida, mesmo com um arquivo de despesas perfeitamente válido.
      26 +8. **RN-002, RN-004, RN-005, RN-007 atualizadas** para refletir
         + o oitavo campo (`despesa.moeda`), a ordem conversão-então-arre
         +dondamento, e o conjunto de categorias reembolsáveis deixando d
         +e ser uma constante de três valores.                           
      27 +9. **Quinze ambiguidades novas registradas (`AMB-019` a `AMB-03
         +3`)**, cobrindo: centro de custo desconhecido/ausente (AMB-019)
         +, categoria ausente em centro cadastrado (AMB-020), `representa
         +cao` exclusiva de quem a declara (AMB-021), limite igual a zero
         + (AMB-022), moeda como indício de viagem (AMB-023), data sem co
         +tação exata (AMB-024), moeda sem cotação alguma (AMB-025), orde
         +m de conversão/arredondamento (AMB-026), base de comparação do 
         +gatilho de nota fiscal (AMB-027), chave de duplicidade entre mo
         +edas (AMB-028), campos de auditoria da conversão (AMB-029), val
         +idação de vigência (AMB-030), erro global versus recusa individ
         +ual nos arquivos externos (AMB-031), regressão da baseline hist
         +órica (AMB-032) e escopo do item C (AMB-033).                  
      28 +10. **Dezessete critérios de aceite novos (`CA-024` a `CA-040`)
         +**, incluindo os quatro cenários financeiros completos do `§12`
         + (nova seção).                                                 
      29 +                                                               
      30 +**Comportamento anterior invalidado:**                         
      31 +                                                               
      32 +- A frase de `§3` "Não converte nem identifica moedas. Todos os
         + valores são interpretados como reais brasileiros (BRL)" deixa 
         +de ser verdadeira e foi removida — substituída por uma declaraç
         +ão sobre o que continua fora de escopo (inferência de viagem a 
         +partir da moeda).                                              
      33 +- RN-007 na forma "apenas `alimentacao`, `transporte_urbano` e 
         +`hospedagem` são reembolsáveis", como conjunto fechado e univer
         +sal, deixa de valer sem qualificação — o conjunto reembolsável 
         +agora depende da política aplicável ao centro de custo (RN-019)
         +.                                                              
      34 +- RN-011, RN-012 e RN-013 deixam de tratar R$60/R$80/R$250 como
         + constantes absolutas da regra — passam a ser os valores da pol
         +ítica **padrão**, com centros de custo cadastrados podendo decl
         +arar valores próprios ou tornar a categoria não reembolsável.  
      35 +- A garantia de que `colaborador.centro_custo` "nunca é usado p
         +ara cruzar dados" (§3, sobre agregação entre arquivos) permanec
         +e verdadeira, mas a leitura mais ampla de que o campo não tem "
         +uso em regra alguma" deixa de valer: `centro_custo` passa a det
         +erminar a política aplicável dentro do próprio arquivo (RN-019)
         +. A tolerância estrutural do bloco `colaborador` (RN-001) não m
         +uda — só o efeito financeiro de um `centro_custo` presente e re
         +conhecido.                                                     
      36 +- `§11`, "o que fica em aberto", removeu a linha "Apenas BRL é 
         +suportado" — deixou de ser uma limitação de produto e passou a 
         +ser requisito atendido.                                        
      37 +                                                               
      38 +**Ambiguidades identificadas:** as quinze listadas no item 9 ac
         +ima, com decisão, justificativa e regra afetada registradas em 
         +`spec.md §6` (`AMB-019` a `AMB-033`).                          
      39 +                                                               
      40 +**Decisões dos quatro grupos** (política por centro de custo; c
         +âmbio; conversão e regras financeiras; saída e vigência):      
      41 +                                                               
      42 +- **Política por centro de custo:** `padrao` como fallback inte
         +gral; centro cadastrado usa só suas categorias declaradas, sem 
         +fallback por categoria; limite zero recusa (não corta a zero); 
         +comparação de `centro_custo` é textual e exata.                
      43 +- **Câmbio:** cotação exata ou a mais recente anterior; nunca f
         +utura; sem interpolação; sem cotação utilizável recusa o item. 
      44 +- **Conversão e regras financeiras:** um único arredondamento a
         +pós a multiplicação; nota fiscal e duplicidade operam sobre o v
         +alor convertido (duplicidade com moeda como componente adiciona
         +l da chave, não substituindo o valor).                         
      45 +- **Saída e vigência:** três campos de auditoria novos na saída
         +; vigência obrigatória e validada, mas informativa quanto a ite
         +ns individuais.                                                
      46 +                                                               
      47 +**RN e CA antigos afetados:** RN-002, RN-004, RN-005, RN-007, R
         +N-009, RN-010, RN-017 (textos atualizados, sem troca de número)
         +. Nenhum RN ou CA de `1.1` foi removido ou renumerado — `RN-001
         +` a `RN-018` e `CA-001` a `CA-023` permanecem intactos onde não
         + listados acima.                                               
      48 +                                                               
      49 +**RN e CA novos:** `RN-019` a `RN-022`; `CA-024` a `CA-040`.   
      50 +                                                               
      51 +**DT-007 invalidada.** `plan.md` ainda declara, em DT-007, "est
         +rutura única e imutável... sem mecanismo genérico de regras... 
         +sem arquivo externo de configuração" para `PoliticaReembolso` —
         + essa decisão técnica está tecnicamente incorreta a partir dest
         +a versão da spec e será revista quando `plan.md` for atualizado
         +, em etapa posterior. Esta entrada apenas formaliza a invalidaç
         +ão do lado da spec; nenhuma linha de `plan.md` foi tocada nesta
         + rodada.                                                       
      52 +                                                               
      53 +**Regressão financeira sob a v4:** processar `exemplos/despesas
         +-exemplo.json` com a política **padrão** (equivalente aos valor
         +es hardcoded até a v1.1) continua totalizando R$585,43 — baseli
         +ne histórica preservada e testável (`CA-037`). O mesmo arquivo,
         + processado com a política **oficial v4** e o centro de custo r
         +eal do colaborador (`CC-ENG-PLATAFORMA`, que tem entrada própri
         +a na tabela), totaliza **R$351,43** — `d-001` passa a integral 
         +(R$72,50), `d-002` a parcial (R$2,50), `d-010` a recusado por `
         +CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (R$0,00, era R$250,00 
         +parcial), `d-014` a integral (R$61,00). A diferença de R$234,00
         + é consequência direta e correta da política v4 aplicada ao cen
         +tro de custo real do colaborador do arquivo — não é um bug, mas
         + precisa estar documentada para que ninguém confunda a mudança 
         +de total com uma regressão de código quando `plan.md`/`tasks.md
         +`/os testes forem atualizados. Os dois cenários coexistem como 
         +critérios de aceite distintos (`CA-037` e `CA-038`), preservand
         +o a baseline histórica em vez de escolher entre "manter" e "atu
         +alizar" o fixture original.                                    
      54 +                                                               
      55 +**Item C fora de escopo.** A fila de aprovação manual (`AGUARDA
         +NDO_APROVACAO` para itens acima de R$500) não foi especificada 
         +nesta rodada — é explicitamente opcional no próprio comunicado 
         +do RH, e nenhum item dos dois arquivos de exemplo do envelope u
         +ltrapassa R$500 sob a interpretação adotada, então nada nos dad
         +os obriga essa decisão agora (`AMB-033`).                      
      56 +                                                               
      57 +**Custo documental e técnico previsto:** esta entrada e a atual
         +ização de `spec.md` (cabeçalho, `§0` nova, `§3`, `§4.1`/`4.1.1`
         + nova, `§4.2`, `§4.3`, `§4.5`, `§4.7`, `§5` com sete RN atualiz
         +adas e quatro novas, `§6` com quinze AMB novas, `§7`, `§8.1` a 
         +`8.5`, `§9`, `§10` com dezessete CA novas, `§11`, `§12` nova) —
         + nenhum outro arquivo tocado nesta rodada. `plan.md` precisará 
         +de: nova DT substituindo DT-007, extensão de DT-003 (CLI) e DT-
         +005 (parsing) para os arquivos externos, e um novo passo no pip
         +eline (§2). `tasks.md` precisará de tasks novas a partir de `T-
         +022` para: leitor de política, leitor de câmbio, campo `moeda` 
         +no contrato do item, conversão cambial no pipeline, resolução d
         +e política por centro de custo, motivo `CATEGORIA_NAO_REEMBOLSA
         +VEL_CENTRO_CUSTO`, motivo `MOEDA_SEM_COTACAO`, três campos novo
         +s de saída, validação de `vigencia`, novos argumentos de CLI, e
         + os quatro cenários de regressão do `§12` como fixtures de test
         +e.                                                             
      58 +                                                               
      59 +**Tasks afetadas:** nenhuma ainda — `plan.md` e `tasks.md` serã
         +o atualizados em etapas posteriores a esta, que é estritamente 
         +normativa (`spec.md` e este arquivo).                          
      60 +                                                               
      61 +---                                                            
      62 +                                                               
      63  ## D-002 — Ajustes de contrato após auditoria independente · `2
          026-07-30`
      64  
      65  **Gatilho:** auditoria independente da spec 1.0 aprovada, reali
          zada em sessão separada da elaboração original, com recomputaçã
          o manual dos 14 itens de `exemplos/despesas-exemplo.json` a par
          tir das regras (não da tabela 4.7) e leitura cruzada de `spec.m
          d` com este arquivo. A auditoria confirmou o total de R$ 585,43
           e as 14 decisões, sem nenhuma divergência de cálculo, mas enco
          ntrou três problemas de gravidade média, todos de precisão de c
          ontrato — nenhum deles altera resultado do arquivo de exemplo.

● Now let's run the verification commands.

  Ran 2 shell commands (ctrl+o to expand)

● Confirmed — only the two intended files show diffs; plan.md, tasks.md,
  README.md, src, and tests produce no diff output at all.

  Ran 1 shell command (ctrl+o to expand)
● All five envelope files are present and untouched (still shown as
  new/untracked, exactly as before).

  Resumo

  Versão/status da spec: 1.1 → 1.2, status em revisão, última alteração
  2026-08-05.

  Contagem final: AMB-001 a AMB-033 (33 total, 15 novas), RN-001 a RN-022 (22 
  total, 4 novas: RN-019 a RN-022), CA-001 a CA-040 (40 total, 17 novas: CA-024
  a CA-040).

  Novidades:
  - RN-019 política por centro de custo · RN-020 câmbio e conversão · RN-021
  vigência do arquivo de política · RN-022 processabilidade dos arquivos
  externos.
  - RN-002, RN-004, RN-005, RN-007, RN-009, RN-010, RN-017 — texto atualizado no
  lugar, sem renumerar.
  - AMB-019 a AMB-033 — centro de custo desconhecido/ausente, categoria ausente
  em CC cadastrado, representacao exclusiva, limite zero, moeda×viagem, cotação
  ausente/exata, ordem de conversão, gatilho de NF convertido, chave de
  duplicidade com moeda, campos de auditoria, vigência, erro global×individual,
  regressão da baseline, escopo do item C.
  - CA-024 a CA-040, incluindo os quatro cenários financeiros do novo §12.
  - Novos motivos: CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO e MOEDA_SEM_COTACAO.
  Novo campo despesa.moeda (8º campo, opcional). Três campos novos de saída:
  moeda, taxa_cambio_aplicada, data_cotacao_utilizada.

  D-003 (DECISIONS.md): registra o gatilho, as mudanças de RN, o comportamento
  anterior invalidado (§3 "não converte moedas", RN-007 fechado em 3 categorias,
  RN-011/012/013 com valores fixos), a invalidação de DT-007, e a regressão
  financeira R$585,43 → R$351,43 sob a v4/CC-ENG-PLATAFORMA, com a baseline
  histórica preservada em paralelo (CA-037/CA-038). Item C registrado como fora
  de escopo. D-001 e D-002 não foram tocados.

  Seções alteradas em spec.md: cabeçalho; nova §0 (fontes oficiais); §3;
  §4.1/nova §4.1.1; §4.2; §4.3; §4.5; §4.6; §4.7; §5
  (RN-002/004/005/007/009/010/017 atualizadas + RN-019 a RN-022 novas); §6
  (AMB-019 a AMB-033 novas); §7; §8.1 a 8.5; §9; §10; §11; nova §12.

  git status --short:
   M specs/001-motor-reembolso/DECISIONS.md
   M specs/001-motor-reembolso/spec.md
  ?? exemplos/envelope/
  plan.md, tasks.md, README.md, código e testes: sem diff. Nenhum /export, git 
  add, commit ou push foi executado.

  Decisão que não consegui representar sem risco de inventar requisito: o
  contrato exato da CLI (nomes de flag, exit codes para política/câmbio
  inválidos) pedido na sua instrução §12 — coloquei a exigência de negócio em
  §4.1.1 (política e câmbio são entrada obrigatória; falha neles é erro global,
  mesma severidade que arquivo de entrada ilegível) mas não cravei números de
  exit code em spec.md, porque exit codes nunca apareceram nesse arquivo (são
  vocabulário exclusivo de plan.md, hoje intocado). Fica registrado em D-003
  como pendência explícita para a próxima atualização de plan.md.

  Parando para revisão.

✻ Baked for 19m 57s

※ recap: We're updating the reembolso spec for the Day 2 currency/policy envelope; spec.md and DECISIONS.md are now fully revised to v1.2. Next: you review the changes before we touch plan.md and tasks.md. (disable recaps in /config)

> Corrija a atualização normativa da spec 1.2 após revisão independente.        
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  * `specs/001-motor-reembolso/spec.md`                                         
  * `specs/001-motor-reembolso/DECISIONS.md`                                    
                                                                                
  Não altere:                                                                   
                                                                                
  * `plan.md`;                                                                  
  * `tasks.md`;                                                                 
  * `README.md`;                                                                
  * código;                                                                     
  * testes;                                                                     
  * fixtures;                                                                   
  * os cinco arquivos de `exemplos/envelope/`.                                  
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  ## 1. Corrigir a ordem e a obrigatoriedade dos campos                         
                                                                                
  O contrato possui oito campos:                                                
                                                                                
  1. `despesa.id`                                                               
  2. `despesa.data`                                                             
  3. `despesa.categoria`                                                        
  4. `despesa.descricao`                                                        
  5. `despesa.fornecedor`                                                       
  6. `despesa.valor`                                                            
  7. `despesa.moeda`                                                            
  8. `despesa.tem_nota_fiscal`                                                  
                                                                                
  Há sete campos obrigatórios, mas eles não são “os sete primeiros”, pois o     
  campo opcional `despesa.moeda` ocupa a sétima posição.                        
                                                                                
  Substitua qualquer afirmação de que:                                          
                                                                                
  * `moeda` é o oitavo campo;                                                   
  * `moeda` ocupa a sexta posição;                                              
  * os sete primeiros campos são obrigatórios.                                  
                                                                                
  Use sempre:                                                                   
                                                                                
  * `despesa.moeda` é o sétimo campo;                                           
  * fica depois de `despesa.valor` e antes de `despesa.tem_nota_fiscal`;        
  * é o único campo opcional.                                                   
                                                                                
  Corrija também D-003, que atualmente chama `moeda` de oitavo campo.           
                                                                                
  ## 2. Corrigir o tratamento de `despesa.moeda: null`                          
                                                                                
  Somente a **ausência da chave** `moeda` assume `BRL`.                         
                                                                                
  Comportamento fechado:                                                        
                                                                                
  * chave `moeda` ausente → `BRL`, sem motivo;                                  
  * `"moeda": null` → item recusado com `CAMPO_AUSENTE`, `campo:                
  "despesa.moeda"`;                                                             
  * tipo não textual → `CAMPO_TIPO_INVALIDO`;                                   
  * texto fora de `[A-Z]{3}` → `CAMPO_FORMATO_INVALIDO`.                        
                                                                                
  Atualize:                                                                     
                                                                                
  * 4.2;                                                                        
  * RN-002;                                                                     
  * RN-020;                                                                     
  * contrato da saída;                                                          
  * vocabulário de motivos;                                                     
  * matriz de dependências;                                                     
  * casos de borda;                                                             
  * critérios de aceite.                                                        
                                                                                
  Adicione um critério específico para `moeda: null`.                           
                                                                                
  ## 3. Incluir o contrato exato da CLI na spec                                 
                                                                                
  O contrato de execução é comportamento observável do produto e deve estar na  
  spec, não somente no plan.                                                    
                                                                                
  Crie uma nova ambiguidade:                                                    
                                                                                
  ```text                                                                       
  AMB-034 — Forma de fornecimento da política e do câmbio à CLI                 
  ```                                                                           
                                                                                
  Decisão:                                                                      
                                                                                
  ```text                                                                       
  calcular --input <entrada.json> --output <saida.json> --politica              
  <politica.json> --cambio <cambio.json>                                        
  ```                                                                           
                                                                                
  Documente:                                                                    
                                                                                
  * os quatro argumentos são obrigatórios;                                      
  * podem aparecer em qualquer ordem;                                           
  * cada argumento aparece exatamente uma vez;                                  
  * argumento ausente, repetido ou desconhecido → exit `2`;                     
  * o comando antigo, somente com input/output, retorna exit `2`;               
  * mesmo entrada somente BRL exige `--cambio`;                                 
  * política ou câmbio inexistente, ilegível, sintaticamente inválido ou        
  estruturalmente inválido → exit `2`;                                          
  * envelope de despesas estrutur                                               
  ──── (185 lines hidden) ───────────────────────────────────────────────────── 
  ica são simultaneamente aplicáveis.                                           
                                                                                
  ## 9. Corrigir o bloco `colaborador`                                          
                                                                                
  Substitua a afirmação:                                                        
                                                                                
  ```text                                                                       
  o bloco não participa de regra financeira alguma                              
  ```                                                                           
                                                                                
  por uma redação precisa:                                                      
                                                                                
  * defeitos do bloco `colaborador` não invalidam o arquivo;                    
  * `colaborador.id` e `colaborador.nome` continuam apenas informativos;        
  * `colaborador.centro_custo`, quando texto e reconhecido, seleciona a         
  política;                                                                     
  * ausente, nulo, inválido ou desconhecido usa `padrao`.                       
                                                                                
  Mantenha a tolerância estrutural já aprovada.                                 
                                                                                
  ## 10. Corrigir D-003                                                         
                                                                                
  Os arquivos ainda não foram commitados.                                       
                                                                                
  Substitua:                                                                    
                                                                                
  ```text                                                                       
  foram commitados em exemplos/envelope/                                        
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  foram recebidos e colocados em exemplos/envelope/ sem alteração; serão        
  versionados no commit documental correspondente                               
  ```                                                                           
                                                                                
  Atualize também em D-003:                                                     
                                                                                
  * `moeda` é o sétimo campo, não o oitavo;                                     
  * RN-010 usa moeda + valor normalizado em BRL;                                
  * contrato da CLI não está pendente para decisão;                             
  * novas AMB e CA desta correção;                                              
  * contagens finais.                                                           
                                                                                
  Não altere D-001 ou D-002.                                                    
                                                                                
  ## 11. Contagens finais                                                       
                                                                                
  Após as correções, a numeração deve ficar:                                    
                                                                                
  ```text                                                                       
  AMB-001 a AMB-036                                                             
  RN-001 a RN-022                                                               
  ```                                                                           
                                                                                
  Não crie nova RN além de RN-022; atualize RN-019 e RN-022.                    
                                                                                
  Continue a numeração dos critérios a partir de CA-041, criando os critérios   
  necessários para:                                                             
                                                                                
  * contrato da CLI;                                                            
  * falhas das flags e preservação de saída;                                    
  * estrutura da política;                                                      
  * estrutura do câmbio;                                                        
  * periodicidade diária de `representacao`;                                    
  * `despesa.moeda: null`.                                                      
                                                                                
  A contagem final de CA deve refletir exatamente o número realmente criado.    
  Não declare uma contagem antes de conferir o arquivo.                         
                                                                                
  Atualize:                                                                     
                                                                                
  * cabeçalho e contagens;                                                      
  * D-003;                                                                      
  * rastreabilidade;                                                            
  * tabela do comunicado v4;                                                    
  * §12.5;                                                                      
  * referências a intervalos de AMB e CA;                                       
  * perguntas em aberto.                                                        
                                                                                
  ## 12. Verificação                                                            
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * somente `spec.md` e `DECISIONS.md` foram modificados;                       
  * `exemplos/envelope/` continua novo e intacto;                               
  * nenhum outro arquivo foi alterado;                                          
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Ao final, informe:                                                            
                                                                                
  * contagem final de AMB, RN e CA;                                             
  * como ficaram AMB-034, AMB-035 e AMB-036;                                    
  * contrato final da CLI;                                                      
  * contrato final de política e câmbio;                                        
  * semântica final de `periodicidade`;                                         
  * tratamento final de `moeda` ausente versus nula;                            
  * correções feitas em RN-010 e D-003;                                         
  * `git status --short`.                                                       
                                                                                
  Pare para revisão.                                                            

  Searched for 2 patterns, read 2 files (ctrl+o to expand)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      110  
      111  ### 4.2 Item de despesa
      112  
      113 -Os sete primeiros campos abaixo são obrigatórios; o oitavo, `d
          -espesa.moeda`, é opcional (política v4, Dia 2). A sequência é 
          -a **ordem canônica de contrato**, usada para ordenar motivos e
          -struturais na saída — `despesa.moeda` ocupa a sexta posição, e
          -ntre `despesa.valor` e `despesa.tem_nota_fiscal`.             
      113 +O contrato tem oito campos. Sete são obrigatórios, mas **não s
          +ão "os sete primeiros"**: o único campo opcional, `despesa.moe
          +da`, ocupa a **sétima** posição — depois de `despesa.valor` (6
          +º) e antes de `despesa.tem_nota_fiscal` (8º, também obrigatóri
          +o). A sequência é a **ordem canônica de contrato**, usada para
          + ordenar motivos estruturais na saída.                        
      114  
      115  | # | Campo | Tipo | Significado | Restrição adicional |
      116  |---|---|---|---|---|

  Searched for 1 pattern (ctrl+o to expand)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      120  | 4 | `despesa.descricao` | texto | Descrição livre. Informati
           va; integra a chave de duplicidade como recebida | Texto, pode
           ndo ser vazio |
      121  | 5 | `despesa.fornecedor` | texto | Fornecedor. Integra a cha
           ve de duplicidade como recebido | Texto, podendo ser vazio |
      122  | 6 | `despesa.valor` | número | Valor monetário na moeda da d
           espesa. Inteiro é aceito (`72` → `72,00`) | Nenhuma quanto ao 
           sinal: zero e negativos são estruturalmente válidos, avaliados
            depois por RN-006 |
      123 -| 7 | `despesa.moeda` | texto | Moeda ISO 4217 da despesa (pol
          -ítica v4, Dia 2). Opcional: ausente ou nulo resolve para `BRL`
          - sem produzir motivo algum | Quando presente, texto de exatame
          -nte três letras maiúsculas (`[A-Z]{3}`) — sem trim e sem conve
          -rsão de caixa. `"usd"`, `" USD"`, `"US"` e `"USDX"` são format
          -o inválido |                                                  
      123 +| 7 | `despesa.moeda` | texto | Moeda ISO 4217 da despesa (pol
          +ítica v4, Dia 2). Opcional: apenas a **ausência da chave** no 
          +objeto resolve para `BRL` sem produzir motivo algum | Quando a
          + chave existe — inclusive com valor `null` —, segue o contrato
          + normalmente: `null` é `CAMPO_AUSENTE`; tipo não textual é `CA
          +MPO_TIPO_INVALIDO`; texto fora de `[A-Z]{3}` é `CAMPO_FORMATO_
          +INVALIDO` (sem trim, sem conversão de caixa: `"usd"`, `" USD"`
          +, `"US"` e `"USDX"` são formato inválido) |                   
      124  | 8 | `despesa.tem_nota_fiscal` | booleano | Existência de not
           a fiscal | Nenhuma além do tipo booleano — um valor booleano r
           ecebido não é aceito como número, nem o inverso |
      125  
      126  **Regra de opcionalidade de `despesa.moeda`.** É o único dos o
           ito campos cuja ausência não produz motivo algum: chave ausent
           e ou valor nulo resolve silenciosamente para `BRL`, exatamente
            como a ausência de moeda era tratada antes de `despesa.moeda`
            existir. A tolerância termina aí — presente e de tipo ou form
           ato inválido, `despesa.moeda` recusa o item como qualquer outr
           o campo estrutural (RN-002), com `CAMPO_TIPO_INVALIDO` ou `CAM
           PO_FORMATO_INVALIDO` conforme o caso. Isso o distingue de `col
           aborador.centro_custo` (4.1), cuja tolerância cobre também val
           ores malformados, não só a ausência.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 10 lines, removed 1 line
      123  | 7 | `despesa.moeda` | texto | Moeda ISO 4217 da despesa (pol
           ítica v4, Dia 2). Opcional: apenas a **ausência da chave** no 
           objeto resolve para `BRL` sem produzir motivo algum | Quando a
            chave existe — inclusive com valor `null` —, segue o contrato
            normalmente: `null` é `CAMPO_AUSENTE`; tipo não textual é `CA
           MPO_TIPO_INVALIDO`; texto fora de `[A-Z]{3}` é `CAMPO_FORMATO_
           INVALIDO` (sem trim, sem conversão de caixa: `"usd"`, `" USD"`
           , `"US"` e `"USDX"` são formato inválido) |
      124  | 8 | `despesa.tem_nota_fiscal` | booleano | Existência de not
           a fiscal | Nenhuma além do tipo booleano — um valor booleano r
           ecebido não é aceito como número, nem o inverso |
      125  
      126 -**Regra de opcionalidade de `despesa.moeda`.** É o único dos o
          -ito campos cuja ausência não produz motivo algum: chave ausent
          -e ou valor nulo resolve silenciosamente para `BRL`, exatamente
          - como a ausência de moeda era tratada antes de `despesa.moeda`
          - existir. A tolerância termina aí — presente e de tipo ou form
          -ato inválido, `despesa.moeda` recusa o item como qualquer outr
          -o campo estrutural (RN-002), com `CAMPO_TIPO_INVALIDO` ou `CAM
          -PO_FORMATO_INVALIDO` conforme o caso. Isso o distingue de `col
          -aborador.centro_custo` (4.1), cuja tolerância cobre também val
          -ores malformados, não só a ausência.                          
      126 +**Regra de opcionalidade de `despesa.moeda`.** É o único dos o
          +ito campos cuja ausência não produz motivo algum — mas a toler
          +ância é estritamente sobre a **ausência da chave**, não sobre 
          +o seu conteúdo. Comportamento fechado:                        
      127  
      128 +| Situação | Resultado |                                      
      129 +|---|---|                                                     
      130 +| Chave `moeda` **ausente** do objeto | Resolve silenciosament
          +e para `BRL`, sem motivo algum |                              
      131 +| `"moeda": null` (chave presente, valor `null`) | Item recusa
          +do com `CAMPO_AUSENTE`, `campo` igual a `despesa.moeda` |     
      132 +| Tipo não textual (número, booleano, lista, objeto) | `CAMPO_
          +TIPO_INVALIDO` |                                              
      133 +| Texto fora do formato `[A-Z]{3}` | `CAMPO_FORMATO_INVALIDO` 
          +|                                                             
      134 +                                                              
      135 +Uma vez que a chave existe no objeto, `despesa.moeda` segue ex
          +atamente o mesmo contrato estrutural que os sete campos obriga
          +tórios (RN-002) — inclusive a classificação `CAMPO_AUSENTE` pa
          +ra valor `null`, que normalmente soa contraditória com "campo 
          +opcional" mas não é: a opcionalidade é sobre a chave não preci
          +sar existir, não sobre um valor `null` explícito ser tolerado 
          +quando ela existe. Isso o distingue de `colaborador.centro_cus
          +to` (4.1), cuja tolerância cobre também valores malformados, n
          +ão só a ausência da chave.                                    
      136 +                                                              
      137  **Nomes qualificados.** A forma `despesa.<campo>` é o nome can
           ônico do campo em toda esta spec, e é exatamente o conjunto de
            valores não nulos aceitos em `motivo.campo` (4.3). A chave co
           rrespondente dentro do objeto de despesa é o segmento à direit
           a do ponto: o campo canônico `despesa.valor` corresponde à cha
           ve `valor`.
      138  
      139  Campo **ausente**, **nulo**, de **tipo inválido** ou de **form
           ato inválido** produz **erro estrutural no item**: o item é re
           cusado e o restante do arquivo continua sendo processado. Essa
            regra vale integralmente para os sete campos obrigatórios; pa
           ra `despesa.moeda`, vale apenas quando o campo está **presente
           ** — sua ausência não é erro estrutural (parágrafo acima).

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      136  
      137  **Nomes qualificados.** A forma `despesa.<campo>` é o nome can
           ônico do campo em toda esta spec, e é exatamente o conjunto de
            valores não nulos aceitos em `motivo.campo` (4.3). A chave co
           rrespondente dentro do objeto de despesa é o segmento à direit
           a do ponto: o campo canônico `despesa.valor` corresponde à cha
           ve `valor`.
      138  
      139 -Campo **ausente**, **nulo**, de **tipo inválido** ou de **form
          -ato inválido** produz **erro estrutural no item**: o item é re
          -cusado e o restante do arquivo continua sendo processado. Essa
          - regra vale integralmente para os sete campos obrigatórios; pa
          -ra `despesa.moeda`, vale apenas quando o campo está **presente
          -** — sua ausência não é erro estrutural (parágrafo acima).    
      139 +Campo **ausente**, **nulo**, de **tipo inválido** ou de **form
          +ato inválido** produz **erro estrutural no item**: o item é re
          +cusado e o restante do arquivo continua sendo processado. Essa
          + regra vale integralmente para os sete campos obrigatórios; pa
          +ra `despesa.moeda`, vale sempre que a **chave existir no objet
          +o** — inclusive com valor `null`, que é `CAMPO_AUSENTE` como e
          +m qualquer campo obrigatório. Só a ausência da própria chave n
          +o objeto foge da regra (parágrafo acima).                     
      140  
      141  **Classificação do erro estrutural.** A escolha entre os três 
           motivos abaixo é fechada e não admite outra leitura:
      142  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      140  
      141  **Classificação do erro estrutural.** A escolha entre os três 
           motivos abaixo é fechada e não admite outra leitura:
      142  
      143 -- **`CAMPO_AUSENTE`** — a chave obrigatória não existe no obje
          -to, ou existe com valor `nulo`. Não se aplica a `despesa.moeda
          -`, que é opcional (4.2) — sua ausência resolve para `BRL` sem 
          -motivo algum.                                                 
      143 +- **`CAMPO_AUSENTE`** — a chave obrigatória não existe no obje
          +to, ou existe com valor `nulo`. Aplica-se normalmente a `despe
          +sa.moeda` quando a chave **existe com valor `null`** — só não 
          +se aplica quando a chave `moeda` está inteiramente ausente do 
          +objeto, caso em que não há motivo algum e o valor resolve para
          + `BRL` (4.2).                                                 
      144  - **`CAMPO_TIPO_INVALIDO`** — a chave existe, não é nula, mas 
           seu tipo JSON diverge do exigido na coluna "Tipo" da tabela ac
           ima. Um valor booleano nunca é aceito como número, mesmo em li
           nguagens de implementação que tratam booleano como subtipo num
           érico — e o inverso também não é aceito.
      145  - **`CAMPO_FORMATO_INVALIDO`** — o tipo JSON está correto, mas
            o conteúdo viola a "Restrição adicional" da tabela acima: tex
           to vazio onde não é permitido, texto que não segue `AAAA-MM-DD
           `, ou texto nesse padrão que não representa uma data real do c
           alendário.
      146  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
      156  | `despesa.valor` igual a `"72,50"` | `CAMPO_TIPO_INVALIDO` |
      157  | `despesa.tem_nota_fiscal` igual a `"sim"` | `CAMPO_TIPO_INVA
           LIDO` |
      158  | `despesa.valor` igual a `0` ou a um número negativo | Estrut
           uralmente válido — nenhum motivo estrutural. Tratado depois po
           r RN-006 (`VALOR_NAO_POSITIVO`) |
      159 -| `despesa.moeda` ausente | Estruturalmente válido — nenhum mo
          -tivo. Resolve para `BRL` (política v4, Dia 2) |               
      159 +| `despesa.moeda` — chave ausente do objeto | Estruturalmente 
          +válido — nenhum motivo. Resolve para `BRL` (política v4, Dia 2
          +) |                                                           
      160 +| `despesa.moeda` igual a `null` | `CAMPO_AUSENTE` — chave pre
          +sente, valor `null` |                                         
      161  | `despesa.moeda` igual a `"usd"` | `CAMPO_FORMATO_INVALIDO` —
            minúsculo |
      162  | `despesa.moeda` igual a um número | `CAMPO_TIPO_INVALIDO` |
      163  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      195  | `indice_entrada` | inteiro ≥ 1 | Posição original do item na
            lista, **base 1**, atribuída antes de qualquer validação e im
           utável |
      196  | `id` | texto ou nulo | `despesa.id` original. Nulo quando au
           sente ou inválido |
      197  | `valor_informado` | qualquer valor JSON ou nulo | O conteúdo
            de `despesa.valor` exatamente como recebido, preservado para 
           auditoria — inclusive quando o tipo é inválido |
      198 -| `moeda` | texto ou nulo | Moeda v4, Dia 2. Moeda efetivament
          -e usada no item: `despesa.moeda` quando estruturalmente válida
          -, ou `"BRL"` quando ausente/nula. Nula apenas quando `despesa.
          -moeda` é estruturalmente inválida, ou o elemento de `despesas`
          - não é objeto |                                               
      198 +| `moeda` | texto ou nulo | Moeda v4, Dia 2. Moeda efetivament
          +e usada no item: `despesa.moeda` quando estruturalmente válida
          +, ou `"BRL"` quando a **chave** `despesa.moeda` está ausente d
          +o objeto. Nula quando `despesa.moeda` é estruturalmente inváli
          +da — inclusive `null` explícito, que é `CAMPO_AUSENTE` (4.2) —
          + ou o elemento de `despesas` não é objeto |                   
      199  | `taxa_cambio_aplicada` | número ou nulo | v4, Dia 2. `1` par
           a BRL; a taxa efetivamente usada na conversão, quando resolvid
           a (RN-020); nulo quando não há conversão possível |
      200  | `data_cotacao_utilizada` | texto `AAAA-MM-DD` ou nulo | v4, 
           Dia 2. Data da cotação efetivamente usada — pode divergir de `
           despesa.data` (RN-020); nulo para BRL e sempre que `taxa_cambi
           o_aplicada` for nulo |
      201  | `valor_normalizado` | monetário ou nulo | Valor após convers
           ão para BRL (RN-020, quando aplicável) e normalização monetári
           a (RN-004). Nulo quando não calculável |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      213  "data_cotacao_utilizada": null
      214  ```
      215  
      216 -Moeda estrangeira convertida (exemplo: EUR sem cotação exata, 
          -usando a mais recente anterior):                              
      216 +Moeda estrangeira convertida (exemplo: EUR datada `2026-07-18`
          +, sábado sem cotação própria, usando a mais recente anterior —
          + `2026-07-17`, cuja cotação de EUR é `5.96`):                 
      217  
      218  ```json
      219  "moeda": "EUR",
      220 -"taxa_cambio_aplicada": 5.93,                                 
      220 +"taxa_cambio_aplicada": 5.96,                                 
      221  "data_cotacao_utilizada": "2026-07-17"
      222  ```
      223  
      224 -Moeda estruturalmente inválida (`CAMPO_TIPO_INVALIDO` ou `CAMP
          -O_FORMATO_INVALIDO`):                                         
      224 +Moeda estruturalmente inválida — `null` explícito (`CAMPO_AUSE
          +NTE`), tipo não textual (`CAMPO_TIPO_INVALIDO`) ou formato for
          +a de `[A-Z]{3}` (`CAMPO_FORMATO_INVALIDO`); os três casos prod
          +uzem a mesma saída:                                           
      225  
      226  ```json
      227  "moeda": null,

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      287  | Código | Regra associada | `campo` | Significado |
      288  |---|---|---|---|
      289  | `ITEM_TIPO_INVALIDO` | RN-002 | nulo | Elemento de `despesas
           ` que não é objeto. Motivo único da posição |
      290 -| `CAMPO_AUSENTE` | RN-002 | um dos oito nomes canônicos | A c
          -have obrigatória não existe no objeto, ou existe com valor nul
          -o. Não se aplica a `despesa.moeda`, que é opcional |          
      290 +| `CAMPO_AUSENTE` | RN-002 | um dos oito nomes canônicos | A c
          +have obrigatória não existe no objeto, ou existe com valor nul
          +o. Para `despesa.moeda` (opcional), aplica-se quando a chave e
          +xiste com valor `null`; não se aplica quando a chave está inte
          +iramente ausente do objeto |                                  
      291  | `CAMPO_TIPO_INVALIDO` | RN-002 | um dos oito nomes canônicos
            | A chave existe, não é nula, mas seu tipo JSON diverge do ex
           igido pelo contrato |
      292  | `CAMPO_FORMATO_INVALIDO` | RN-002 | um dos oito nomes canôni
           cos | O tipo JSON está correto, mas o conteúdo viola a restriç
           ão adicional do campo |
      293  | `ID_DUPLICADO` | RN-003 | `despesa.id` | `despesa.id` válido
            repetido no arquivo |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      383  
      384  ### RN-002 — Contrato do item
      385  
      386 -**Regra:** cada elemento de `despesas` deve ser um objeto. Set
          -e dos seus oito campos de 4.2 são obrigatórios, com os tipos e
          - restrições declarados; `despesa.moeda` (v4, Dia 2) é o único 
          -opcional, e sua ausência não produz motivo algum — resolve par
          -a `BRL` (RN-020). Elemento que não é objeto é recusado com o m
          -otivo único `ITEM_TIPO_INVALIDO`, sem que nenhuma regra de neg
          -ócio seja avaliada e sem gerar motivos de campo ausente; o reg
          -istro resultante traz `id`, `valor_informado`, `moeda`, `taxa_
          -cambio_aplicada`, `data_cotacao_utilizada` e `valor_normalizad
          -o` nulos, `valor_reembolsavel` `0,00` e `indice_entrada` prese
          -rvado. Sendo objeto, qualquer campo obrigatório **ausente**, *
          -*nulo**, de **tipo inválido** ou de **formato inválido** recus
          -a o item, com um motivo por campo defeituoso, classificado pel
          -a regra fechada da subseção "Classificação do erro estrutural"
          - (4.2); `despesa.moeda`, quando presente, segue a mesma classi
          -ficação de tipo/formato dos demais campos. Em qualquer caso o 
          -arquivo não é interrompido.                                   
      386 +**Regra:** cada elemento de `despesas` deve ser um objeto. Set
          +e dos seus oito campos de 4.2 são obrigatórios, com os tipos e
          + restrições declarados; `despesa.moeda` (v4, Dia 2) é o único 
          +opcional — mas a opcionalidade é só sobre a **ausência da chav
          +e**: quando a chave `moeda` existe no objeto, inclusive com va
          +lor `null`, ela segue o contrato normalmente, e `null` é `CAMP
          +O_AUSENTE` como qualquer campo obrigatório. Só a ausência tota
          +l da chave resolve silenciosamente para `BRL` (RN-020), sem pr
          +oduzir motivo. Elemento que não é objeto é recusado com o moti
          +vo único `ITEM_TIPO_INVALIDO`, sem que nenhuma regra de negóci
          +o seja avaliada e sem gerar motivos de campo ausente; o regist
          +ro resultante traz `id`, `valor_informado`, `moeda`, `taxa_cam
          +bio_aplicada`, `data_cotacao_utilizada` e `valor_normalizado` 
          +nulos, `valor_reembolsavel` `0,00` e `indice_entrada` preserva
          +do. Sendo objeto, qualquer campo obrigatório **ausente**, **nu
          +lo**, de **tipo inválido** ou de **formato inválido** recusa o
          + item, com um motivo por campo defeituoso, classificado pela r
          +egra fechada da subseção "Classificação do erro estrutural" (4
          +.2); `despesa.moeda`, quando a chave existe, segue a mesma cla
          +ssificação dos demais campos. Em qualquer caso o arquivo não é
          + interrompido.                                                
      387  **Origem:** contrato de entrada.
      388 -**Aceite:** item com `data: "31/07/2026"` e `valor: "72,50"` é
          - recusado com dois motivos, nesta ordem — `CAMPO_FORMATO_INVAL
          -IDO` com `campo` igual a `despesa.data`, depois `CAMPO_TIPO_IN
          -VALIDO` com `campo` igual a `despesa.valor` —, porque `despesa
          -.data` precede `despesa.valor` na ordem canônica de contrato. 
          -`despesa.categoria` igual a um número é recusado com `CAMPO_TI
          -PO_INVALIDO` e `campo` igual a `despesa.categoria`; `despesa.i
          -d` igual a texto vazio é recusado com `CAMPO_FORMATO_INVALIDO`
          - e `campo` igual a `despesa.id`. Um elemento de `despesas` igu
          -al ao texto `"despesa"` produz um único registro recusado, com
          - motivo único `ITEM_TIPO_INVALIDO`, `campo` nulo, `id` nulo, `
          -valor_informado` nulo e `indice_entrada` preservado. Um item c
          -om `moeda: "usd"` é recusado com `CAMPO_FORMATO_INVALIDO` e `c
          -ampo` igual a `despesa.moeda`; um item sem a chave `moeda` não
          - recebe motivo algum por causa disso.                         
      388 +**Aceite:** item com `data: "31/07/2026"` e `valor: "72,50"` é
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
          +valor_informado` nulo e `indice_entrada` preservado. Um item c
          +om `moeda: "usd"` é recusado com `CAMPO_FORMATO_INVALIDO` e `c
          +ampo` igual a `despesa.moeda`; um item com `moeda: null` é rec
          +usado com `CAMPO_AUSENTE` e `campo` igual a `despesa.moeda`; u
          +m item sem a chave `moeda` no objeto não recebe motivo algum p
          +or causa disso.                                               
      389  
      390  ### RN-003 — Unicidade de `despesa.id`
      391  
● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      491  
      492  ### RN-020 — Resolução de câmbio e conversão monetária
      493  
      494 -**Regra:** `despesa.moeda` igual a `BRL`, ou ausente, tem taxa
          - implícita `1` e não consulta a tabela de câmbio. Para moeda e
          -strangeira estruturalmente válida, procura-se primeiro a cotaç
          -ão exata na data da despesa; sem cotação exata, usa-se a cotaç
          -ão mais recente cuja data seja **anterior** à data da despesa 
          -— nunca uma cotação futura, e sem interpolação entre datas. Se
          - não existir cotação exata nem anterior utilizável para aquela
          - moeda — porque ela não aparece em `cambio.json` em nenhuma da
          -ta, ou porque sua primeira cotação disponível é posterior à da
          -ta da despesa —, o item é recusado com `MOEDA_SEM_COTACAO`; `v
          -alor_normalizado` permanece nulo, e nenhuma regra financeira p
          -osterior (RN-006 em diante) é avaliada. A conversão multiplica
          - o valor bruto pela taxa resolvida; o arredondamento para duas
          - casas (RN-004) acontece uma única vez, sobre o resultado dess
          -a multiplicação.                                              
      494 +**Regra:** `despesa.moeda` igual a `"BRL"`, ou com a chave int
          +eiramente ausente do objeto (RN-002), tem taxa implícita `1` e
          + não consulta a tabela de câmbio. `despesa.moeda: null` nunca 
          +chega a esta regra — é `CAMPO_AUSENTE` (RN-002), recusado ante
          +s da resolução de câmbio. Para moeda estrangeira estruturalmen
          +te válida, procura-se primeiro a cotação exata na data da desp
          +esa; sem cotação exata, usa-se a cotação mais recente cuja dat
          +a seja **anterior** à data da despesa — nunca uma cotação futu
          +ra, e sem interpolação entre datas. Se não existir cotação exa
          +ta nem anterior utilizável para aquela moeda — porque ela não 
          +aparece em `cambio.json` em nenhuma data, ou porque sua primei
          +ra cotação disponível é posterior à data da despesa —, o item 
          +é recusado com `MOEDA_SEM_COTACAO`; `valor_normalizado` perman
          +ece nulo, e nenhuma regra financeira posterior (RN-006 em dian
          +te) é avaliada. A conversão multiplica o valor bruto pela taxa
          + resolvida; o arredondamento para duas casas (RN-004) acontece
          + uma única vez, sobre o resultado dessa multiplicação.        
      495  **Origem:** comunicado do RH, política v4, item B (Dia 2).
      496  **Aceite:** EUR `30,00` datado num sábado sem cotação própria 
           usa a cotação do último dia útil anterior disponível — ex.: `2
           026-07-17` para uma despesa de `2026-07-18`. GBP sem nenhuma e
           ntrada em `cambio.json`, em qualquer data, é recusado com `MOE
           DA_SEM_COTACAO`. USD `40,00` × `5,50` normaliza para `220,00`,
            com um único arredondamento.
      497  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      673  **Decisão:** toda posição da lista `despesas` aparece exatamen
           te uma vez, com `indice_entrada`, o `id` original quando dispo
           nível, valor informado, valor normalizado quando calculável, v
           alor reembolsável, uma única decisão final e uma ou mais justi
           ficativas vinculadas às regras. Cada justificativa é um **obje
           to** com `codigo`, `regra` e `campo` — não um código solto —, 
           porque um código sozinho não diz de qual campo o defeito veio 
           nem qual regra o produziu, e as duas informações são exatament
           e o que torna a decisão auditável.
      674  **Justificativa:** nenhum lançamento pode desaparecer do resul
           tado; colaborador e financeiro precisam auditar individualment
           e cada decisão e chegar, a partir dela, à regra que a gerou.
      675  **Regra afetada:** RN-017
      676 -**Subdecisão AMB-018/ESCOPO:** `colaborador.id`, `colaborador.
          -nome`, `colaborador.centro_custo` e `periodo.competencia` são 
          -metadados opcionais, preservados na saída para rastreabilidade
          - e nulos quando ausentes, malformados ou de tipo inesperado; n
          -unca alteram resultado financeiro nem invalidam arquivo ou ite
          -m. BRL é a moeda presumida quando `despesa.moeda` está ausente
          - (RN-020; política v4, Dia 2, alterou o que acontece quando a 
          -moeda **está** presente — ver AMB-023 a AMB-029).             
      676 +**Subdecisão AMB-018/ESCOPO:** `colaborador.id`, `colaborador.
          +nome`, `colaborador.centro_custo` e `periodo.competencia` são 
          +metadados opcionais, preservados na saída para rastreabilidade
          + e nulos quando ausentes, malformados ou de tipo inesperado; n
          +unca alteram resultado financeiro nem invalidam arquivo ou ite
          +m. BRL é a moeda presumida quando a **chave** `despesa.moeda` 
          +está inteiramente ausente do objeto (RN-020; política v4, Dia 
          +2, alterou o que acontece quando a chave existe — inclusive co
          +m valor `null` — ver AMB-023 a AMB-029).                      
      677  
      678  ### AMB-019 — Centro de custo desconhecido, ausente, nulo ou d
           e tipo inválido · D
      679  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
      846  | Moeda sem qualquer cotação em `cambio.json` | GBP | Recusado
           , `MOEDA_SEM_COTACAO` | RN-020 |
      847  | Moeda estrangeira acima do gatilho de nota, sem nota | USD `
           40,00` convertido a `220,00`, sem nota | Recusado, `NOTA_FISCA
           L_AUSENTE` — compara o valor convertido | RN-009, RN-020 |
      848  | Moedas diferentes, mesmo valor convertido | EUR e BRL coinci
           dentes após conversão | **Não** são duplicatas | RN-010 |
      849 -| Moeda ausente | `despesa.moeda` não informado | Assume `BRL`
          -; sem motivo estrutural | RN-002 |                            
      849 +| Moeda ausente | Chave `despesa.moeda` não presente no objeto
          + | Assume `BRL`; sem motivo estrutural | RN-002 |             
      850 +| Moeda nula | `"moeda": null` (chave presente, valor `null`) 
          +| Recusado, `CAMPO_AUSENTE`, `campo` igual a `despesa.moeda` |
          + RN-002 |                                                     
      851  | Moeda em caixa baixa | `"usd"` | `CAMPO_FORMATO_INVALIDO` | 
           RN-002 |
      852  | Vigência da política ausente | Arquivo de política sem `vige
           ncia` | Arquivo de política inválido; nenhuma apuração | RN-02
           1 |
      853  

● Now let's rewrite §4.1.1 to add the missing moeda_base field, the CLI
  contract, and the corrected câmbio structure.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 55 lines, removed 10 lines
       93  
       94  A partir da política v4, a execução passa a depender de dois d
           ocumentos adicionais, externos ao arquivo de despesas e obriga
           tórios em toda execução.
       95  
       96 -**Arquivo de política.**                                      
       96 +**Contrato de execução (CLI, AMB-034).** O contrato de execuçã
          +o é comportamento observável do produto e é normativo desta sp
          +ec, não apenas do `plan.md`:                                  
       97  
       98 -| Campo | Tipo | Significado |                                
       99 -|---|---|---|                                                 
      100 -| `vigencia` | texto `AAAA-MM-DD` | Data de entrada em vigor d
          -a política. Obrigatória e validada estruturalmente (RN-021) | 
      101 -| `padrao` | objeto | Tabela de limites por categoria, aplicáv
          -el a qualquer centro de custo sem entrada própria em `centros_
          -custo` |                                                      
      102 -| `centros_custo` | objeto | Mapa de centro de custo para a re
          -spectiva tabela de limites por categoria — substitui integralm
          -ente `padrao` para as categorias que declarar (RN-019) |      
      103 -| `nota_fiscal_obrigatoria_acima_de` | número | Gatilho de not
          -a fiscal (RN-009), em BRL, único para toda a execução |       
       98 +```text                                                       
       99 +calcular --input <entrada.json> --output <saida.json> --politi
          +ca <politica.json> --cambio <cambio.json>                     
      100 +```                                                           
      101  
      105 -Cada entrada de `padrao` ou de um centro de custo específico a
          -ssocia uma categoria a `{ limite, periodicidade }`. Qual tabel
          -a — e, dentro dela, qual limite — se aplica a cada item é RN-0
          -19.                                                           
      102 +- Os quatro argumentos são obrigatórios; podem aparecer em qua
          +lquer ordem; cada um aparece exatamente uma vez.              
      103 +- Argumento ausente, repetido ou desconhecido → código de saíd
          +a `2`. O comando anterior à política v4, só com `--input`/`--o
          +utput`, agora também retorna `2` — falta `--politica` e `--cam
          +bio`, mesmo quando a entrada só tem despesas em BRL.          
      104 +- Política ou câmbio inexistente, ilegível, sintaticamente inv
          +álido ou estruturalmente inválido (AMB-035) → código `2` — mes
          +ma classe já usada para `--input` ilegível, sem código novo.  
      105 +- Envelope de despesas estruturalmente inválido (RN-001) conti
          +nua código `3`.                                               
      106 +- Sucesso continua código `0`; stdout permanece vazio em qualq
          +uer cenário; mensagens de erro vão para stderr.               
      107 +- Uma saída (`--output`) preexistente é preservada intacta em 
          +qualquer falha global.                                        
      108 +- Nenhum código de saída além de `0`, `2` e `3` é criado por e
          +sta versão.                                                   
      109  
      107 -**Arquivo de câmbio.** Um mapa de data (`AAAA-MM-DD`) para um 
          -objeto de moeda (código ISO 4217) para taxa (número). Cobre ap
          -enas dias úteis bancários — a ausência de uma data é esperada,
          - não um defeito do arquivo. A resolução de taxa é RN-020.     
      110 +**Arquivo de política (AMB-035).** Contrato estrutural fechado
          + — "estruturalmente inválido" (RN-022) significa violar qualqu
          +er regra abaixo:                                              
      111  
      109 -Política e câmbio são entrada obrigatória da execução, na mesm
          -a categoria de "arquivo de entrada" que hoje é o arquivo de de
          -spesas: ausência, ilegibilidade ou invalidade estrutural de qu
          -alquer um dos dois impede toda a apuração (RN-022) — erro glob
          -al, mais grave que um envelope de despesas inválido (RN-001), 
          -que ainda pressupõe política e câmbio utilizáveis. Os mecanism
          -os exatos de como a execução recebe esses arquivos (nomes de a
          -rgumento, códigos de saída) são contrato de execução e pertenc
          -em ao `plan.md`, atualizado em etapa posterior a esta.        
      112 +| Campo | Tipo | Obrigatório | Restrição |                    
      113 +|---|---|---|---|                                             
      114 +| (raiz) | objeto | Sim | — |                                 
      115 +| `vigencia` | texto `AAAA-MM-DD` | Sim | Representa data real
          + do calendário (RN-021) |                                     
      116 +| `moeda_base` | texto | Sim | Exatamente `"BRL"` |           
      117 +| `padrao` | objeto | Sim | Tabela de limites por categoria, a
          +plicável a qualquer centro de custo sem entrada própria em `ce
          +ntros_custo`. Pode ser vazio |                                
      118 +| `centros_custo` | objeto | Sim | Mapa de centro de custo par
          +a a respectiva tabela de limites por categoria — substitui int
          +egralmente `padrao` para as categorias que declarar (RN-019). 
          +Pode ser vazio |                                              
      119 +| `nota_fiscal_obrigatoria_acima_de` | número | Sim | Não nega
          +tivo |                                                        
      120 +| `versao`, `acrescimo_em_viagem_percentual`, outros | qualque
          +r | Não | Metadados aceitos e ignorados pelas regras atuais. `
          +acrescimo_em_viagem_percentual` **não** ativa RN-016 |        
      121  
      122 +Cada tabela de categoria — dentro de `padrao` ou de um centro 
          +de custo — é um objeto que associa uma categoria de nome não v
          +azio a um objeto com:                                         
      123 +                                                              
      124 +| Campo | Tipo | Obrigatório | Restrição |                    
      125 +|---|---|---|---|                                             
      126 +| `limite` | número | Sim | Maior ou igual a zero |           
      127 +| `periodicidade` | texto | Sim | Exatamente `"dia"` ou `"diar
          +ia"` (RN-019, AMB-036) |                                      
      128 +| `observacao` | texto | Não | Informativa; nunca lida por reg
          +ra alguma |                                                   
      129 +| outros | qualquer | Não | Ignorados |                       
      130 +                                                              
      131 +**Arquivo de câmbio (AMB-035).** A raiz **não** é um mapa dire
          +to de data para moeda — as cotações estão aninhadas sob `taxas
          +`:                                                            
      132 +                                                              
      133 +```text                                                       
      134 +raiz                                                          
      135 +├── moeda_base       (obrigatório, exatamente "BRL")          
      136 +├── fonte            (opcional, informativo)                  
      137 +├── observacao       (opcional, informativo)                  
      138 +└── taxas            (objeto, obrigatório — pode ser vazio)   
      139 +    └── AAAA-MM-DD    (chave: data real do calendário)        
      140 +        └── USD/EUR/… (chave: moeda em `[A-Z]{3}`)            
      141 +            └── taxa  (número estritamente positivo)          
      142 +```                                                           
      143 +                                                              
      144 +| Campo | Tipo | Obrigatório | Restrição |                    
      145 +|---|---|---|---|                                             
      146 +| (raiz) | objeto | Sim | — |                                 
      147 +| `moeda_base` | texto | Sim | Exatamente `"BRL"` |           
      148 +| `taxas` | objeto | Sim | Cada chave é uma data `AAAA-MM-DD` 
          +real; cada valor é um objeto cujas chaves seguem `[A-Z]{3}` e 
          +cujos valores são número estritamente positivo. Pode ser vazio
          + — nesse caso toda despesa em moeda estrangeira é recusada ind
          +ividualmente com `MOEDA_SEM_COTACAO` (RN-020), sem invalidar o
          + arquivo |                                                    
      149 +| `fonte`, `observacao` | texto | Não | Metadados informativos
          +, nunca lidos por regra alguma |                              
      150 +| outros | qualquer | Não | Ignorados |                       
      151 +                                                              
      152 +`taxas` cobre apenas dias úteis bancários por natureza — a aus
          +ência de uma data específica é esperada (RN-020), não um defei
          +to estrutural do arquivo.                                     
      153 +                                                              
      154 +Política e câmbio são entrada obrigatória da execução, na mesm
          +a categoria de "arquivo de entrada" que hoje é o arquivo de de
          +spesas: ausência, ilegibilidade ou qualquer violação dos contr
          +atos estruturais acima impede toda a apuração (RN-022) — erro 
          +global, mais grave que um envelope de despesas inválido (RN-00
          +1), que ainda pressupõe política e câmbio utilizáveis.        
      155 +                                                              
      156  ### 4.2 Item de despesa
      157  
      158  O contrato tem oito campos. Sete são obrigatórios, mas **não s
           ão "os sete primeiros"**: o único campo opcional, `despesa.moe
           da`, ocupa a **sétima** posição — depois de `despesa.valor` (6
           º) e antes de `despesa.tem_nota_fiscal` (8º, também obrigatóri
           o). A sequência é a **ordem canônica de contrato**, usada para
            ordenar motivos estruturais na saída.

● Now let's fix the "single applicable table" logic in RN-007, RN-005, and
  RN-019 — a real inconsistency the padrão+específica wording created.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      458  
      459  ### RN-007 — Categorias reembolsáveis
      460  
      461 -**Regra:** é reembolsável a categoria normalizada presente na 
          -política aplicável ao centro de custo do colaborador (RN-019) 
          -— no mínimo `alimentacao`, `transporte_urbano` e `hospedagem`,
          - sempre presentes na política padrão, mais qualquer categoria 
          -adicional que o centro de custo declare explicitamente (ex.: `
          -representacao` para `CC-COMERCIAL`, política v4, Dia 2). Categ
          -oria normalizada ausente de toda tabela aplicável — padrão e, 
          -quando houver, específica do centro de custo — é recusada com 
          -`CATEGORIA_FORA_POLITICA`.                                    
      461 +**Regra:** é reembolsável a categoria normalizada presente na 
          +**única** tabela de política efetivamente aplicável ao item (R
          +N-019). Essa tabela é `padrao` quando o centro de custo do col
          +aborador é desconhecido, ausente, nulo ou de tipo inválido; ou
          + é **exclusivamente** a tabela do centro de custo cadastrado, 
          +quando houver — nunca as duas tabelas somadas. Categoria norma
          +lizada ausente dessa única tabela aplicável é recusada com `CA
          +TEGORIA_FORA_POLITICA` quando a tabela aplicável é `padrao`; q
          +uando a tabela aplicável é a de um centro de custo cadastrado,
          + a mesma ausência é `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` 
          +(RN-019), não `CATEGORIA_FORA_POLITICA` — a distinção entre os
          + dois motivos está em RN-019, não nesta regra.                
      462  **Origem:** política do RH, item 9; conjunto de categorias var
           iável por centro de custo acrescentado pela política v4 (Dia 2
           , RN-019).
      463 -**Aceite:** `coworking` de R$ 89,00 com nota é recusado em qua
          -lquer centro de custo, reembolsável `0,00`, porque nenhuma tab
          -ela de política — padrão ou específica — o declara. `represent
          -acao` de R$190,00 com nota, para um colaborador de `CC-SUPORTE
          --N2` (fora da tabela de centros de custo), é recusado com `CAT
          -EGORIA_FORA_POLITICA`, porque nem a política padrão nem `CC-SU
          -PORTE-N2` declaram essa categoria.                            
      463 +**Aceite:** `coworking` de R$ 89,00 com nota, para um colabora
          +dor de centro de custo desconhecido (tabela aplicável `padrao`
          +), é recusado com `CATEGORIA_FORA_POLITICA`, reembolsável `0,0
          +0`, porque `padrao` não a declara. O mesmo `coworking`, para u
          +m colaborador de `CC-COMERCIAL` (cadastrado — tabela aplicável
          + é exclusivamente a de `CC-COMERCIAL`, que também não declara 
          +`coworking`), é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTR
          +O_CUSTO`, não `CATEGORIA_FORA_POLITICA` — mesmo reembolsável `
          +0,00`, motivo diferente porque a tabela aplicável é diferente.
          + `representacao` de R$190,00 com nota, para um colaborador de 
          +`CC-SUPORTE-N2` (fora da tabela de centros de custo, logo sob 
          +`padrao`), é recusado com `CATEGORIA_FORA_POLITICA`, porque `p
          +adrao` não declara essa categoria.                            
      464  
      465  ### RN-008 — Elegibilidade temporal
      466  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      446  
      447  ### RN-005 — Normalização de categoria
      448  
      449 -**Regra:** `categoria` é normalizada por remoção de espaços da
          -s pontas, insensibilidade a caixa e insensibilidade a acentos.
          - O resultado normalizado é comparado, por igualdade exata, con
          -tra o vocabulário de categorias reembolsáveis — `alimentacao`,
          - `transporte_urbano` e `hospedagem` em qualquer centro de cust
          -o, mais `representacao` e qualquer outra categoria que a polít
          -ica aplicável declarar (RN-007, RN-019, política v4, Dia 2). N
          -enhuma outra transformação ocorre.                            
      449 +**Regra:** `categoria` é normalizada por remoção de espaços da
          +s pontas, insensibilidade a caixa e insensibilidade a acentos.
          + O resultado normalizado é comparado, por igualdade exata, con
          +tra as categorias presentes na única tabela de política efetiv
          +amente aplicável ao item — `padrao`, ou a tabela do centro de 
          +custo cadastrado, nunca as duas somadas (RN-007, RN-019, polít
          +ica v4, Dia 2). Nenhuma categoria é reembolsável "em qualquer 
          +centro de custo": mesmo `alimentacao`, `transporte_urbano` e `
          +hospedagem` dependem de estarem presentes na tabela efetivamen
          +te aplicável ao item. Nenhuma outra transformação ocorre.     
      450  **Origem:** política do RH, item 9; vocabulário de categorias 
           variável por centro de custo acrescentado pela política v4 (Di
           a 2).
      451  **Aceite:** `ALIMENTACAO`, `Alimentação` e ` alimentacao ` rec
           onhecem `alimentacao`. `transporte urbano` **não** reconhece `
           transporte_urbano`.
      452  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 15 lines, removed 2 lines
      530  
      531  ### RN-019 — Política de reembolso por centro de custo
      532  
      533 -**Regra:** a política de limites deixa de ser um valor único: 
          -cada categoria é avaliada contra a tabela do centro de custo d
          -o colaborador (`colaborador.centro_custo`), quando cadastrada,
          - ou contra a tabela `padrao` caso contrário. A comparação de `
          -centro_custo` contra a tabela é textual e exata — sem trim, se
          -m normalização de caixa ou acento, sem correspondência aproxim
          -ada. Centro de custo ausente, nulo, de tipo inválido, ou prese
          -nte mas sem entrada na tabela, usa integralmente `padrao` — nu
          -nca uma mistura entre as duas tabelas. Um centro de custo cada
          -strado usa **somente** as categorias que declara explicitament
          -e: categoria da política padrão que o centro de custo cadastra
          -do não declara **não** recebe o valor de `padrao` por fallback
          - — é tratada como não reembolsável para aquele centro de custo
          -. Categoria com limite explicitamente igual a `0,00` na tabela
          - aplicável é recusada com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_C
          -USTO`, nunca como reembolso parcial de valor zero. Categoria a
          -dicional que só existe em algum centro de custo específico (ex
          -.: `representacao`) é reembolsável apenas para os centros que 
          -a declaram; para os demais, é `CATEGORIA_FORA_POLITICA` (RN-00
          -7) — ausência da categoria em toda tabela aplicável —, não `CA
          -TEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, que pressupõe a catego
          -ria presente com limite zero.                                 
      533 +**Regra:** a política de limites deixa de ser um valor único. 
          +A cada item se aplica **uma única** tabela: a do centro de cus
          +to do colaborador (`colaborador.centro_custo`), quando cadastr
          +ada em `centros_custo`, **ou** a tabela `padrao`, quando o cen
          +tro de custo é desconhecido, ausente, nulo ou de tipo inválido
          + — nunca as duas tabelas somadas ou misturadas. A comparação d
          +e `centro_custo` contra as chaves de `centros_custo` é textual
          + e exata — sem trim, sem normalização de caixa ou acento, sem 
          +correspondência aproximada.                                   
      534 +                                                              
      535 +Dentro da tabela efetivamente aplicável, cada categoria declar
          +ada tem um `limite` e uma `periodicidade` (contrato estrutural
          + em 4.1.1, AMB-035):                                          
      536 +                                                              
      537 +- `periodicidade: "dia"` — teto **compartilhado** entre os ite
          +ns elegíveis da mesma categoria e data, consumido em ordem cre
          +scente de `indice_entrada` (mesmo mecanismo de RN-011/RN-012/R
          +N-015). É o caso de `alimentacao`, `transporte_urbano` e, quan
          +do declarada, `representacao`.                                
      538 +- `periodicidade: "diaria"` — teto **individual** por lançamen
          +to, sem saldo compartilhado (mesmo mecanismo de RN-013). É o c
          +aso de `hospedagem`.                                          
      539 +- Categoria com `periodicidade` diferente de `"dia"` e `"diari
          +a"` torna o **arquivo de política** inválido (AMB-035, RN-022)
          + — não é um defeito do item, é um defeito do arquivo externo. 
      540 +                                                              
      541 +Duas classificações de recusa, sempre com `valor_reembolsavel`
          + `0,00` e nunca como reembolso parcial:                       
      542 +                                                              
      543 +- **`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`** — quando a tab
          +ela aplicável é a de um **centro de custo cadastrado**: (a) a 
          +categoria está ausente dessa tabela (o centro de custo existe,
          + mas não a declara), ou (b) a categoria está presente na tabel
          +a com `limite` igual a `0,00`. As duas situações são, na práti
          +ca, a mesma decisão de política — o centro de custo não reembo
          +lsa aquela categoria — e nunca produzem teto parcial de valor 
          +zero.                                                         
      544 +- **`CATEGORIA_FORA_POLITICA`** (RN-007) — quando a tabela apl
          +icável é `padrao` (centro de custo desconhecido, ausente, nulo
          + ou de tipo inválido) e a categoria está ausente dela.        
      545 +                                                              
      546 +Categoria adicional que só existe em algum centro de custo esp
          +ecífico (ex.: `representacao`) segue a mesma régua acima, sem 
          +tratamento especial: reembolsável quando a tabela aplicável a 
          +declara; `CATEGORIA_FORA_POLITICA` quando a tabela aplicável é
          + `padrao` e não a declara; `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_
          +CUSTO` quando a tabela aplicável é de um centro cadastrado que
          + não a declara.                                               
      547  **Origem:** comunicado do RH, política v4, item A (Dia 2).
      535 -**Aceite:** colaborador de `CC-ENG-PLATAFORMA` com hospedagem 
          -de R$480,00 é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_
          -CUSTO`, reembolsável `0,00` — não `PARCIALMENTE_REEMBOLSADO`. 
          -Colaborador de `CC-ADM` com hospedagem — categoria ausente da 
          -entrada de `CC-ADM`, presente em `padrao` — também é recusado,
          - com o mesmo motivo, e não recebe o limite de `padrao`. Colabo
          -rador de `CC-SUPORTE-N2` (fora da tabela) com alimentação de R
          -$58,00 usa o limite de `padrao` (R$60,00) e é `INTEGRALMENTE_R
          -EEMBOLSADO`.                                                  
      548 +**Aceite:** colaborador de `CC-ENG-PLATAFORMA` com hospedagem 
          +de R$480,00 é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_
          +CUSTO`, reembolsável `0,00` — não `PARCIALMENTE_REEMBOLSADO` (
          +categoria presente, limite `0,00`). Colaborador de `CC-ADM` co
          +m hospedagem — categoria ausente da tabela de `CC-ADM`, presen
          +te em `padrao` — também é recusado com `CATEGORIA_NAO_REEMBOLS
          +AVEL_CENTRO_CUSTO` e não recebe o limite de `padrao` (categori
          +a ausente da tabela do centro cadastrado). Colaborador de `CC-
          +COMERCIAL` com `coworking` — categoria ausente da tabela de `C
          +C-COMERCIAL`, que também não existe em `padrao` — é recusado c
          +om `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_F
          +ORA_POLITICA`, porque a tabela aplicável a esse colaborador é 
          +a de `CC-COMERCIAL`, não `padrao`. Colaborador de `CC-SUPORTE-
          +N2` (fora da tabela) com alimentação de R$58,00 usa o limite d
          +e `padrao` (R$60,00) e é `INTEGRALMENTE_REEMBOLSADO`. Dois ite
          +ns de `representacao` de `CC-COMERCIAL` (`periodicidade: "dia"
          +`, limite R$300,00) na mesma data compartilham o mesmo saldo d
          +e R$300,00, consumido em ordem de `indice_entrada` — exatament
          +e como `alimentacao` e `transporte_urbano`.                   
      549  
      550  ### RN-020 — Resolução de câmbio e conversão monetária
      551  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      338  | `ID_DUPLICADO` | RN-003 | `despesa.id` | `despesa.id` válido
            repetido no arquivo |
      339  | `MOEDA_SEM_COTACAO` | RN-020 | `despesa.moeda` | v4, Dia 2. 
           Moeda estruturalmente válida, mas sem cotação exata ou anterio
           r utilizável em `cambio.json` na data da despesa |
      340  | `VALOR_NAO_POSITIVO` | RN-006 | nulo | Valor normalizado men
           or ou igual a zero |
      341 -| `CATEGORIA_FORA_POLITICA` | RN-007 | nulo | Categoria normal
          -izada ausente de toda tabela de política aplicável (padrão e, 
          -quando houver, específica do centro de custo) |               
      342 -| `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` | RN-019 | nulo | 
          -v4, Dia 2. Categoria com limite de política igual a `0,00`, ex
          -plicitamente não reembolsável para o centro de custo do colabo
          -rador |                                                       
      341 +| `CATEGORIA_FORA_POLITICA` | RN-007 | nulo | Categoria normal
          +izada ausente da única tabela de política aplicável, quando es
          +sa tabela é `padrao` (centro de custo desconhecido, ausente, n
          +ulo ou de tipo inválido) |                                    
      342 +| `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` | RN-019 | nulo | 
          +v4, Dia 2. Quando a tabela aplicável é a de um centro de custo
          + cadastrado: categoria ausente dessa tabela, ou presente nela 
          +com `limite` igual a `0,00` |                                 
      343  | `FORA_COMPETENCIA` | RN-008 | nulo | `data` fora da janela |
      344  | `NOTA_FISCAL_AUSENTE` | RN-009 | nulo | Nota obrigatória e a
           usente |
      345  | `DUPLICIDADE` | RN-010 | nulo | Ocorrência posterior de desp
           esa economicamente idêntica |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      348  
      349  | Código | Regra associada | `campo` | Aparece em |
      350  |---|---|---|---|
      351 -| `TETO_DIARIO_APLICADO` | RN-011 (alimentação) ou RN-012 (tra
          -nsporte) | nulo | `PARCIALMENTE_REEMBOLSADO` |                
      351 +| `TETO_DIARIO_APLICADO` | RN-011 (alimentação), RN-012 (trans
          +porte) ou RN-019 (categoria de periodicidade `"dia"` introduzi
          +da exclusivamente pela política externa, ex.: `representacao`)
          + | nulo | `PARCIALMENTE_REEMBOLSADO` |                        
      352  | `TETO_DIARIO_ESGOTADO` | RN-015 | nulo | `NAO_REEMBOLSADO_TE
           TO_ESGOTADO` |
      353  | `TETO_HOSPEDAGEM_APLICADO` | RN-013 | nulo | `PARCIALMENTE_R
           EEMBOLSADO` de hospedagem |
      354  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      753  
      754  **Texto original do RH:** "`CC-COMERCIAL` tem uma categoria no
           va, `representacao`, que não existia na v3."
      755  **O que não está claro:** "categoria nova" poderia significar 
           reembolsável globalmente (com algum limite implícito nos centr
           os que não a declaram) ou reembolsável apenas onde declarada.
      756 -**Decisão:** `representacao` é reembolsável somente nos centro
          -s de custo que a declaram explicitamente; nos demais — incluin
          -do a política padrão —, é `CATEGORIA_FORA_POLITICA`.          
      757 -**Justificativa:** o texto atribui a categoria especificamente
          - a `CC-COMERCIAL`; inventar um limite implícito para os demais
          - centros de custo criaria uma regra financeira sem base no com
          -unicado.                                                      
      756 +**Decisão:** `representacao` é reembolsável somente onde a tab
          +ela efetivamente aplicável a declara — e não é um caso especia
          +l: segue exatamente a régua de RN-019 que vale para qualquer c
          +ategoria. Quando o centro de custo é desconhecido, ausente, nu
          +lo ou de tipo inválido, a tabela aplicável é `padrao`, que não
          + declara `representacao` — `CATEGORIA_FORA_POLITICA`. Quando o
          + centro de custo é cadastrado mas não declara `representacao` 
          +(ex.: `CC-ADM`), a categoria está ausente **da tabela daquele 
          +centro** — `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CAT
          +EGORIA_FORA_POLITICA`, pelo mesmo motivo que qualquer outra ca
          +tegoria ausente de um centro cadastrado (AMB-020).            
      757 +**Justificativa:** o texto atribui a categoria especificamente
          + a `CC-COMERCIAL`; inventar um limite implícito para os demais
          + centros de custo criaria uma regra financeira sem base no com
          +unicado. Tratar `representacao` como caso especial de classifi
          +cação de motivo — em vez de aplicar a mesma régua de RN-019 — 
          +criaria uma exceção sem justificativa própria.                
      758  **Regra afetada:** RN-007, RN-019
      759  
      760  ### AMB-022 — Significado de limite igual a zero · O

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 1 line
      899  | Limite de categoria igual a `0,00` no centro de custo | Hosp
           edagem em `CC-ENG-PLATAFORMA` | Recusado, `CATEGORIA_NAO_REEMB
           OLSAVEL_CENTRO_CUSTO` — nunca `PARCIALMENTE_REEMBOLSADO` | RN-
           019 |
      900  | Categoria ausente na tabela do centro de custo cadastrado | 
           Hospedagem em `CC-ADM` | Recusado, `CATEGORIA_NAO_REEMBOLSAVEL
           _CENTRO_CUSTO` — sem fallback para `padrao` | RN-019 |
      901  | Centro de custo fora da tabela | `CC-SUPORTE-N2` | Usa `padr
           ao` integralmente | RN-019 |
      902 -| Categoria só existente em outro centro de custo | `represent
          -acao` fora de `CC-COMERCIAL` | Recusado, `CATEGORIA_FORA_POLIT
          -ICA` | RN-007, RN-019 |                                       
      902 +| Categoria fora de qualquer tabela, centro de custo desconhec
          +ido | `representacao` para colaborador de centro de custo desc
          +onhecido (tabela aplicável `padrao`) | Recusado, `CATEGORIA_FO
          +RA_POLITICA` | RN-007, RN-019 |                               
      903 +| Categoria ausente da tabela de um centro cadastrado, mas ine
          +xistente em toda a política | `coworking` para colaborador de 
          +`CC-COMERCIAL` (cadastrado; `coworking` não existe em nenhuma 
          +tabela) | Recusado, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` 
          +— não `CATEGORIA_FORA_POLITICA`, porque a tabela aplicável é a
          + de `CC-COMERCIAL` | RN-007, RN-019 |                         
      904 +| Categoria de periodicidade `"dia"` introduzida pela política
          + externa | Dois itens de `representacao` (`CC-COMERCIAL`) na m
          +esma data | Saldo compartilhado de R$300,00, mesmo mecanismo d
          +e RN-011/RN-012/RN-015 | RN-019 |                             
      905  | Data da despesa sem cotação exata | EUR num sábado sem cotaç
           ão própria | Usa a cotação do último dia útil anterior | RN-02
           0 |
      906  | Moeda sem qualquer cotação em `cambio.json` | GBP | Recusado
           , `MOEDA_SEM_COTACAO` | RN-020 |
      907  | Moeda estrangeira acima do gatilho de nota, sem nota | USD `
           40,00` convertido a `220,00`, sem nota | Recusado, `NOTA_FISCA
           L_AUSENTE` — compara o valor convertido | RN-009, RN-020 |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1160  | `e-006` | representacao | GBP `55,00`, sem cotação em `camb
            io.json` | — | recusado (`MOEDA_SEM_COTACAO`) | `0,00` |
      1161  | `e-007` | hospedagem | BRL `1200,00` | — | parcial (teto `4
            00`) | `400,00` |
      1162  | `e-008` | alimentacao | BRL `95,00` | — | parcial (teto `90
            `) | `90,00` |
      1163 -| `e-009` | coworking | BRL `120,00` | — | recusado (`CATEGOR
           -IA_FORA_POLITICA`) | `0,00` |                                
      1163 +| `e-009` | coworking | BRL `120,00` | — | recusado (`CATEGOR
           +IA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — `CC-COMERCIAL` é cadastra
           +do e não declara `coworking`; a tabela `padrao` não é consult
           +ada) | `0,00` |                                              
      1164  | `e-010` | alimentacao | BRL `88,00`, `moeda` ausente → assu
            me BRL | — | integral | `88,00` |
      1165  
      1166  ### 12.4 Dani Okonkwo — centro de custo desconhecido (`despes
            as-envelope-cc-desconhecido.json`)

● Now let's expand RN-022 to reference the closed structural contract, and add
  AMB-034/035/036 to §6.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      561  
      562  ### RN-022 — Processabilidade dos arquivos externos de polític
           a e câmbio
      563  
      564 -**Regra:** a apuração só ocorre quando os arquivos de política
          - e de câmbio são, cada um, legíveis e sintaticamente válidos c
          -omo JSON, e estruturalmente válidos conforme 4.1.1 e RN-021. F
          -alha em qualquer um dos dois arquivos — ausência, ilegibilidad
          -e, JSON sintaticamente inválido, ou estrutura que não satisfaç
          -a o contrato — impede toda a apuração do arquivo de despesas, 
          -mesmo que este esteja perfeitamente válido; nenhum resultado p
          -arcial é produzido. É erro global, na mesma classe de gravidad
          -e de um arquivo de entrada ilegível — mais grave que um envelo
          -pe de despesas inválido (RN-001), que ainda pressupõe política
          - e câmbio utilizáveis.                                        
      564 +**Regra:** a apuração só ocorre quando os arquivos de política
          + e de câmbio são, cada um, legíveis e sintaticamente válidos c
          +omo JSON, e estruturalmente válidos conforme o contrato fechad
          +o de 4.1.1 (AMB-035) — que define exaustivamente o que "estrut
          +uralmente inválido" significa para cada arquivo: tipo de cada 
          +campo, obrigatoriedade, e as restrições de `limite`/`periodici
          +dade` das tabelas de categoria (política) e de `taxas` (câmbio
          +). Falha em qualquer um dos dois arquivos — ausência, ilegibil
          +idade, JSON sintaticamente inválido, ou qualquer violação dess
          +e contrato — impede toda a apuração do arquivo de despesas, me
          +smo que este esteja perfeitamente válido; nenhum resultado par
          +cial é produzido. É erro global (código de saída `2`, AMB-034)
          +, na mesma classe de gravidade de um arquivo de entrada ilegív
          +el — mais grave que um envelope de despesas inválido (RN-001, 
          +código `3`), que ainda pressupõe política e câmbio utilizáveis
          +. `taxas` vazio em `cambio.json` **não** é violação estrutural
          + — é um arquivo de câmbio válido que simplesmente não resolve 
          +nenhuma moeda estrangeira, recusando essas despesas individual
          +mente com `MOEDA_SEM_COTACAO` (RN-020).                       
      565  **Origem:** contrato de entrada (lacuna do comunicado, decorre
           nte da introdução dos arquivos externos — política v4, Dia 2).
      566 -**Aceite:** arquivo de câmbio com JSON sintaticamente inválido
          - não produz apuração alguma, mesmo que o arquivo de despesas e
          - o arquivo de política sejam válidos. Arquivo de política sem 
          -o bloco `padrao` não produz apuração alguma.                  
      566 +**Aceite:** arquivo de câmbio com JSON sintaticamente inválido
          + não produz apuração alguma, mesmo que o arquivo de despesas e
          + o arquivo de política sejam válidos. Arquivo de política sem 
          +o bloco `padrao`, ou com `moeda_base` diferente de `"BRL"`, ou
          + com uma tabela de categoria cuja `periodicidade` não seja `"d
          +ia"` nem `"diaria"`, não produz apuração alguma. Arquivo de câ
          +mbio com `taxas: {}` produz apuração normal do arquivo de desp
          +esas, com toda despesa em moeda estrangeira recusada individua
          +lmente.                                                       
      567  
      568  ---
      569  
● Now let's add AMB-034, AMB-035, and AMB-036 to §6, right after AMB-033.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 24 lines
      853  **Justificativa:** o comunicado prioriza explicitamente os ite
           ns A e B; nenhum item dos dois arquivos de exemplo do envelope
            ultrapassa R$500 sob a interpretação adotada, então nada nos 
           dados obriga essa decisão agora.
      854  **Regra afetada:** nenhuma (fora de escopo)
      855  
      856 +### AMB-034 — Forma de fornecimento da política e do câmbio à 
          +CLI · O                                                       
      857 +                                                              
      858 +**Texto original do RH:** nenhum — o comunicado não descreve m
          +ecanismo de execução, só a existência dos arquivos `politica-v
          +4.json` e `cambio.json`.                                      
      859 +**O que não está claro:** como a execução recebe os dois arqui
          +vos externos — argumento posicional, flag nomeada, caminho fix
          +o por convenção, variável de ambiente — e como o comportamento
          + de erro se manifesta quando eles estão ausentes ou inválidos.
          + O contrato de execução é comportamento observável do produto:
          + sem uma decisão explícita e normativa, duas implementações ig
          +ualmente corretas quanto às regras de negócio poderiam divergi
          +r na forma de invocação.                                      
      860 +**Decisão:** contrato fixo, com quatro argumentos nomeados e o
          +brigatórios, em qualquer ordem, cada um uma única vez: `calcul
          +ar --input <entrada.json> --output <saida.json> --politica <po
          +litica.json> --cambio <cambio.json>`. Argumento ausente, repet
          +ido ou desconhecido, ou política/câmbio inexistente, ilegível,
          + sintaticamente inválido ou estruturalmente inválido (AMB-035)
          + → código de saída `2` — a mesma classe já usada para `--input
          +` ilegível, sem código novo. Envelope de despesas estruturalme
          +nte inválido continua código `3`; sucesso continua `0`. stdout
          + permanece vazio em qualquer cenário; erros vão para stderr; u
          +ma saída preexistente é preservada intacta em qualquer falha g
          +lobal. Detalhado por completo em 4.1.1.                       
      861 +**Justificativa:** reaproveitar o código `2` (em vez de criar 
          +`4`/`5` para política/câmbio) mantém a régua já estabelecida —
          + `2` é "problema de infraestrutura ou de entrada", `3` é "enve
          +lope de despesas semanticamente inválido" — sem multiplicar có
          +digos para uma distinção que stderr já comunica em texto. Exig
          +ir `--cambio` mesmo para entradas só em BRL evita que o contra
          +to de execução dependa do conteúdo da entrada, o que tornaria 
          +o comando variável e mais difícil de automatizar.             
      862 +**Regra afetada:** RN-022 (formaliza o contrato de execução qu
          +e a regra pressupõe)                                          
      863 +                                                              
      864 +### AMB-035 — Contrato estrutural dos arquivos externos · O   
      865 +                                                              
      866 +**Texto original do RH:** nenhum — os arquivos `politica-v4.js
          +on` e `cambio.json` do envelope são exemplos concretos, não um
          +a descrição de esquema.                                       
      867 +**O que não está claro:** RN-022 (na forma anterior a esta cor
          +reção) dizia "estruturalmente inválido" sem definir precisamen
          +te o que isso significa para cada arquivo — sem um contrato fe
          +chado, a expressão não é verificável sem ler o código, contrar
          +iando o próprio critério de aceite da spec ("verificável sem l
          +eitura de código").                                           
      868 +**Decisão:** contrato fechado, documentado por completo em 4.1
          +.1: raiz de ambos os arquivos deve ser objeto; política exige 
          +`vigencia`, `moeda_base` (exatamente `"BRL"`), `padrao` (objet
          +o, pode ser vazio), `centros_custo` (objeto, pode ser vazio) e
          + `nota_fiscal_obrigatoria_acima_de` (número não negativo), com
          + `versao` e `acrescimo_em_viagem_percentual` aceitos como meta
          +dados ignorados (o segundo **não** ativa RN-016); cada tabela 
          +de categoria exige `limite` (número ≥ 0) e `periodicidade` (ex
          +atamente `"dia"` ou `"diaria"` — AMB-036); câmbio exige `moeda
          +_base` (exatamente `"BRL"`) e `taxas` (objeto, podendo ser vaz
          +io, aninhando data → moeda → taxa estritamente positiva), com 
          +`fonte` e `observacao` como metadados informativos.           
      869 +**Justificativa:** a correção também revela que a raiz de `cam
          +bio.json` **não** é um mapa direto de data para moeda, como um
          +a versão anterior desta spec presumiu por engano — as cotações
          + estão aninhadas sob a chave `taxas`, junto de `moeda_base`, `
          +fonte` e `observacao`. Um contrato fechado e correto evita ess
          +a classe de erro se repetir na implementação, e torna "estrutu
          +ralmente inválido" checável item a item, sem ambiguidade.     
      870 +**Regra afetada:** RN-019, RN-020, RN-021, RN-022 (esclarecido
          +s por este contrato — nenhuma decisão de negócio nova além do 
          +que já estava implícito)                                      
      871 +                                                              
      872 +### AMB-036 — Semântica de `periodicidade` das categorias exte
          +rnas · U                                                      
      873 +                                                              
      874 +**Texto original do RH:** nenhum — `periodicidade: "dia"` e `p
          +eriodicidade: "diaria"` aparecem em `politica-v4.json` sem que
          + o comunicado defina o que cada valor significa operacionalmen
          +te; a spec 1.1 só conhecia essa distinção de forma implícita, 
          +hardcoded por categoria (alimentação/transporte "por dia", hos
          +pedagem "por diária").                                        
      875 +**O que não está claro:** com a política externa, a periodicid
          +ade passa a ser dado, não mais uma propriedade fixa de cada ca
          +tegoria — inclusive uma categoria nova como `representacao` tr
          +az a sua própria periodicidade declarada (`"dia"`). É preciso 
          +fixar o que cada valor de `periodicidade` implica mecanicament
          +e.                                                            
      876 +**Decisão:** `periodicidade: "dia"` — teto compartilhado pelos
          + itens elegíveis da mesma categoria e data, consumido em ordem
          + de `indice_entrada` (RN-015). `periodicidade: "diaria"` — tet
          +o individual por lançamento, sem saldo compartilhado. Limite `
          +0,00` continua sendo recusa por política (`CATEGORIA_NAO_REEMB
          +OLSAVEL_CENTRO_CUSTO`) antes de qualquer teto, independentemen
          +te da periodicidade. Categoria com `periodicidade` diferente d
          +esses dois valores torna o **arquivo de política** inválido — 
          +não é um defeito do item.                                     
      877 +**Justificativa:** os dois valores já correspondem exatamente 
          +aos dois mecanismos de teto que a spec 1.1 já implementava par
          +a alimentação/transporte (compartilhado) e hospedagem (individ
          +ual) — a política externa só nomeia explicitamente o que já er
          +a a regra. Tratar um valor de `periodicidade` fora do vocabulá
          +rio fechado como erro de arquivo (não de item) é consistente c
          +om RN-022: é a política que está malformada, não a despesa.   
      878 +**Regra afetada:** RN-019                                     
      879 +                                                              
      880  ---
      881  
      882  ## 7. Casos de borda

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      572  > Esta seção é o coração da spec. Uma ambiguidade resolvida no
            código sem registro
      573  > aqui conta como não resolvida.
      574  
      575 -Dezoito ambiguidades da linha de base (`AMB-001` a `AMB-018`) 
          -e as subdecisões que cada uma gerou, mais quinze ambiguidades 
          -novas do envelope do Dia 2, política v4 (`AMB-019` a `AMB-033`
          -) — trinta e três no total. Classificação: **U** unidade de ap
          -licação · **F** fronteira · **D** dado ausente · **O** outra. 
      575 +Dezoito ambiguidades da linha de base (`AMB-001` a `AMB-018`) 
          +e as subdecisões que cada uma gerou, mais dezoito ambiguidades
          + novas do envelope do Dia 2, política v4 (`AMB-019` a `AMB-036
          +`) — trinta e seis no total. `AMB-034` a `AMB-036` foram acres
          +centadas numa correção após revisão independente da primeira l
          +eitura do envelope, para fechar o contrato de execução e os co
          +ntratos estruturais dos arquivos externos. Classificação: **U*
          +* unidade de aplicação · **F** fronteira · **D** dado ausente 
          +· **O** outra.                                                
      576  
      577  ### AMB-001 — Unidade do limite diário · U
      578  

  Searched for 1 pattern (ctrl+o to expand)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      809  
      810  **Texto original do RH:** nenhum — decorre da combinação entre
            RN-010 (linha de base) e a introdução de `moeda`.
      811  **O que não está claro:** se dois itens em moedas diferentes, 
           com valor convertido coincidente, deveriam ser tratados como d
           uplicata.
      812 -**Decisão:** `moeda` entra na chave de duplicidade. Itens em m
          -oedas diferentes nunca são duplicados entre si, mesmo que o va
          -lor convertido coincida exatamente.                           
      813 -**Justificativa:** duplicidade é sobre o mesmo lançamento rela
          -tado duas vezes; comparar por valor convertido tornaria a dupl
          -icidade dependente da taxa de câmbio do dia, em vez de depende
          -r só da identidade do lançamento.                             
      812 +**Decisão:** a chave passa a ser `data`, `categoria` normaliza
          +da, `moeda`, `valor` normalizado **em BRL** (já convertido, RN
          +-020), `fornecedor` original e `descricao` original. `moeda` e
          +ntra como componente adicional da chave, ao lado do valor já c
          +onvertido — não como substituto dele. Itens em moedas diferent
          +es nunca são duplicados entre si, mesmo que o valor convertido
          + coincida exatamente.                                         
      813 +**Justificativa:** `moeda` impede colisões entre unidades mone
          +tárias diferentes — sem ela, um item de EUR 100,00 e um de BRL
          + 100,00 no mesmo dia poderiam colidir por coincidência aritmét
          +ica, mesmo sendo transações distintas. Usar o valor **em BRL**
          + (não o valor bruto na moeda original) mantém a chave alinhada
          + ao mesmo valor financeiro que todas as regras posteriores usa
          +m (RN-006, RN-009, os tetos) — não haveria razão para duplicid
          +ade ser a única regra a operar sobre um número diferente. Dent
          +ro da mesma moeda e mesma data, a taxa aplicada é sempre a mes
          +ma (RN-020), então o valor convertido não introduz variação es
          +púria para duas ocorrências do mesmo lançamento. `moeda` na ch
          +ave já garante, por si só, que moedas diferentes nunca colidem
          + — o valor convertido coincidir por acaso não basta para gerar
          + falso positivo.                                              
      814  **Regra afetada:** RN-010
      815  
      816  ### AMB-029 — Campos de auditoria da conversão na saída · O

  Searched for 2 patterns (ctrl+o to expand)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 8 lines, removed 1 line
      75  
      76  **O envelope é inválido** — e nenhuma apuração, nem parcial, é 
          produzida — quando: a raiz não é objeto; `periodo` está ausente
           ou não é objeto; `periodo.inicio` ou `periodo.fim` estão ausen
          tes, não são texto, não seguem `AAAA-MM-DD` ou não representam 
          data real do calendário; `periodo.inicio` é posterior a `period
          o.fim`; `despesas` está ausente, é nulo ou não é lista.
      77  
      78 -**Tolerância dos metadados opcionais.** Nenhum defeito no bloco
         - `colaborador` invalida o arquivo nem qualquer item, porque o b
         -loco não participa de regra financeira alguma:                 
      78 +**Tolerância dos metadados opcionais.** Nenhum defeito no bloco
         + `colaborador` invalida o arquivo nem qualquer item — mas isso 
         +não significa que o bloco é irrelevante para regra alguma (deix
         +ou de ser verdade a partir da política v4: `centro_custo` selec
         +iona política, RN-019). Precisamente:                          
      79  
      80 +- Defeitos no bloco `colaborador` — ausência, nulo, tipo errado
         +, campos malformados — nunca invalidam o arquivo nem qualquer i
         +tem.                                                           
      81 +- `colaborador.id` e `colaborador.nome` permanecem apenas infor
         +mativos: preservados para rastreabilidade, sem uso em regra alg
         +uma.                                                           
      82 +- `colaborador.centro_custo`, quando é texto e corresponde a um
         +a entrada de `centros_custo` na política aplicável, seleciona a
         + tabela de limites daquele centro (RN-019).                    
      83 +- `colaborador.centro_custo` ausente, nulo, de tipo inválido, o
         +u de valor desconhecido na tabela de política, usa integralment
         +e a política `padrao` — sem invalidar arquivo ou item.         
      84 +                                                               
      85 +Resumo da tolerância estrutural (RN-001), que continua valendo 
         +à risca:                                                       
      86 +                                                               
      87  | Situação | Resultado na saída |
      88  |---|---|
      89  | `colaborador` ausente ou nulo | Os três metadados são nulos |

● Now let's add the new acceptance criteria (CA-041 onward) after CA-040.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 11 lines
      1118  - [ ] **CA-039** — `exemplos/envelope/despesas-envelope.json`
             (Rafael Nkemelu, `CC-COMERCIAL`), processado com `politica-v
            4.json` e `cambio.json`, totaliza `total_reembolsavel` igual 
            a **R$1.143,26**, conforme a tabela de `§12`.
      1119  - [ ] **CA-040** — `exemplos/envelope/despesas-envelope-cc-de
            sconhecido.json` (Dani Okonkwo, `CC-SUPORTE-N2`), processado 
            com `politica-v4.json` e `cambio.json`, totaliza `total_reemb
            olsavel` igual a **R$373,76**, conforme a tabela de `§12`.
      1120  
      1121 +**Correção após revisão independente:**                      
      1122 +                                                             
      1123 +- [ ] **CA-041** — `calcular --input <e> --output <s> --polit
           +ica <p> --cambio <c>`, com as quatro flags em qualquer ordem 
           +e cada uma presente exatamente uma vez, processa normalmente 
           +e retorna código de saída `0`.                               
      1124 +- [ ] **CA-042** — Argumento ausente, repetido ou desconhecid
           +o retorna código `2` — inclusive o comando anterior à polític
           +a v4, com apenas `--input`/`--output`, mesmo quando a entrada
           + só tem despesas em BRL.                                     
      1125 +- [ ] **CA-043** — Arquivo de política ou de câmbio inexisten
           +te, ilegível, sintaticamente inválido ou estruturalmente invá
           +lido (conforme o contrato fechado de 4.1.1) retorna código `2
           +`, mesmo com um arquivo de despesas perfeitamente válido.    
      1126 +- [ ] **CA-044** — Em qualquer cenário de código `2` ou `3`, 
           +um arquivo em `--output` que já existisse antes da execução p
           +ermanece intacto, byte a byte; stdout permanece vazio e a men
           +sagem de erro vai para stderr.                               
      1127 +- [ ] **CA-045** — Um arquivo de política que satisfaça integ
           +ralmente o contrato de 4.1.1 (`vigencia` real, `moeda_base` e
           +xatamente `"BRL"`, `padrao` e `centros_custo` objetos, `nota_
           +fiscal_obrigatoria_acima_de` não negativo, cada tabela de cat
           +egoria com `limite` ≥ 0 e `periodicidade` igual a `"dia"` ou 
           +`"diaria"`) é aceito; qualquer violação de um desses pontos i
           +nvalida o arquivo de política por inteiro.                   
      1128 +- [ ] **CA-046** — Um arquivo de câmbio que satisfaça o contr
           +ato de 4.1.1 (`moeda_base` exatamente `"BRL"`, `taxas` objeto
           + aninhando data real → moeda `[A-Z]{3}` → taxa estritamente p
           +ositiva) é aceito, inclusive com `taxas: {}` — nesse caso, to
           +da despesa em moeda estrangeira é recusada individualmente co
           +m `MOEDA_SEM_COTACAO`, sem invalidar o arquivo.              
      1129 +- [ ] **CA-047** — Dois itens de `representacao` (`CC-COMERCI
           +AL`, `periodicidade: "dia"`, limite R$300,00) na mesma data c
           +onsomem conjuntamente o mesmo saldo de R$300,00, distribuído 
           +em ordem de `indice_entrada` — o mesmo mecanismo de RN-011/RN
           +-012/RN-015, não um teto individual por item.                
      1130 +- [ ] **CA-048** — Um item com `"moeda": null` é recusado com
           + `CAMPO_AUSENTE` e `campo` igual a `despesa.moeda`; um item s
           +em a chave `moeda` no objeto não recebe motivo algum e assume
           + `BRL`.                                                      
      1131 +                                                             
      1132  ---
      1133  
      1134  ## 11. O que fica em aberto

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines
      1067  | — (vigência e arquivos externos) | AMB-030, AMB-031 | RN-02
            1, RN-022 | CA-035, CA-036 |
      1068  | — (regressão) | AMB-032 | RN-019 | CA-037, CA-038 |
      1069  | C — Fila de aprovação manual (opcional, fora de escopo) | A
            MB-033 | — | — |
      1070 +| — (contrato de execução, correção pós-revisão) | AMB-034 | 
           +RN-022 | CA-041 a CA-044 |                                   
      1071 +| — (contrato estrutural dos arquivos externos, correção pós-
           +revisão) | AMB-035 | RN-019, RN-020, RN-021, RN-022 | CA-045,
           + CA-046 |                                                    
      1072 +| — (periodicidade das categorias externas, correção pós-revi
           +são) | AMB-036 | RN-019 | CA-047 |                           
      1073  
      1074  ---
      1075  

  Searched for 1 pattern, read 1 file (ctrl+o to expand)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1150  | Fila de aprovação manual (item C do comunicado do Dia 2) nã
            o está implementada | Um estado novo (`AGUARDANDO_APROVACAO`)
            , critérios de fila e de gestor responsável — deliberadamente
             fora de escopo nesta rodada (AMB-033) |
      1151  | Não há seleção automática de política histórica por vigênci
            a | Uma segunda política externa anterior, com sua própria ja
            nela de vigência, e uma regra de escolha entre elas quando ma
            is de uma existir (RN-021 trata `vigencia` como informativa n
            esta versão, porque só uma política existe) |
      1152  
      1153 -**Perguntas que não puderam ser respondidas por ausência de i
           -nterlocutor.** A política v3 foi escrita pelo RH e não houve 
           -canal para esclarecimento; o mesmo vale para a política v4. A
           -s leituras adotadas nas trinta e três ambiguidades da seção 6
           - são decisões desta spec, não interpretações confirmadas pela
           - área de origem. As que mais mudariam o resultado se a área d
           -ecidisse diferente: AMB-002 (corte no teto contra recusa inte
           -gral), AMB-008 (uma diária por lançamento) e AMB-004 (ausênci
           -a de nota recusa o item inteiro), da linha de base; e, do env
           -elope do Dia 2, AMB-020 (categoria ausente em centro de custo
           - cadastrado), AMB-022 (limite igual a zero) e AMB-024 (data s
           -em cotação exata) — as três com maior impacto financeiro obse
           -rvado nos arquivos de exemplo.                               
      1153 +**Perguntas que não puderam ser respondidas por ausência de i
           +nterlocutor.** A política v3 foi escrita pelo RH e não houve 
           +canal para esclarecimento; o mesmo vale para a política v4. A
           +s leituras adotadas nas trinta e seis ambiguidades da seção 6
           + são decisões desta spec, não interpretações confirmadas pela
           + área de origem. As que mais mudariam o resultado se a área d
           +ecidisse diferente: AMB-002 (corte no teto contra recusa inte
           +gral), AMB-008 (uma diária por lançamento) e AMB-004 (ausênci
           +a de nota recusa o item inteiro), da linha de base; e, do env
           +elope do Dia 2, AMB-020 (categoria ausente em centro de custo
           + cadastrado), AMB-022 (limite igual a zero) e AMB-024 (data s
           +em cotação exata) — as três com maior impacto financeiro obse
           +rvado nos arquivos de exemplo. `AMB-034` a `AMB-036`, acresce
           +ntadas na correção pós-revisão, fecham contratos de execução 
           +e de arquivo externo em vez de resolver ambiguidade de leitur
           +a de texto — têm decisão técnica, mas não dependem de interlo
           +cutor do RH.                                                 
      1154  
      1155  ---
      1156  

  Read 1 file (ctrl+o to expand)

● Let's update §12.5 to reflect the correction round.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1225  
      1226  ### 12.5 Como usar esta seção
      1227  
      1228 -Os quatro cenários acima são a fonte normativa de `CA-024` a 
           -`CA-040`. Divergência entre uma implementação e os valores de
           -sta seção é bug de código, não ambiguidade de spec — as ambig
           -uidades relevantes já foram decididas em `AMB-019` a `AMB-033
           -` e as regras correspondentes (`RN-019` a `RN-022`, mais `RN-
           -004`, `RN-007`, `RN-009`, `RN-010` e `RN-017` atualizadas). F
           -ixtures de teste automatizado para estes quatro cenários são 
           -trabalho de `tasks.md`, ainda não criado para esta versão.   
      1228 +Os quatro cenários acima são a fonte normativa de `CA-024` a 
           +`CA-040`; o contrato de execução e os contratos estruturais d
           +os arquivos externos (`CA-041` a `CA-048`) são verificáveis i
           +ndependentemente de qualquer um desses quatro cenários financ
           +eiros. Divergência entre uma implementação e os valores desta
           + seção é bug de código, não ambiguidade de spec — as ambiguid
           +ades relevantes já foram decididas em `AMB-019` a `AMB-036` e
           + as regras correspondentes (`RN-019` a `RN-022`, mais `RN-002
           +`, `RN-004`, `RN-005`, `RN-007`, `RN-009`, `RN-010` e `RN-017
           +` atualizadas). A tabela de `e-009` (12.3) usa `CATEGORIA_NAO
           +_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITICA` — 
           +correção da mesma revisão que fechou RN-007/RN-019 (`coworkin
           +g` está ausente da tabela de `CC-COMERCIAL`, que é a única ta
           +bela aplicável a um centro cadastrado; `padrao` nunca é consu
           +ltada). Fixtures de teste automatizado para estes cenários sã
           +o trabalho de `tasks.md`, ainda não criado para esta versão. 

  Searched for 5 patterns, read 1 file (ctrl+o to expand)
● Now let's correct DECISIONS.md's D-003 entry. First, the "commitados" claim
  and the field numbering.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      12  
      13  ## D-003 — Envelope do Dia 2 e política v4 · `2026-08-05`
      14  
      15 -**Gatilho:** recebimento do envelope oficial do Dia 2, liberado
         - às 10h — mudança de requisito do RH, política de reembolso v4,
         - com vigência imediata e retroativa à competência atual. Cinco 
         -arquivos oficiais acompanham o envelope e foram commitados em `
         -exemplos/envelope/` sem alteração: `00-ENVELOPE-LACRADO.md` (co
         -municado do RH), `politica-v4.json` (tabela de limites por cent
         -ro de custo), `cambio.json` (taxas de câmbio por data), `despes
         -as-envelope.json` (Rafael Nkemelu, `CC-COMERCIAL`, exercitando 
         -moeda estrangeira) e `despesas-envelope-cc-desconhecido.json` (
         -Dani Okonkwo, `CC-SUPORTE-N2`, exercitando centro de custo fora
         - da tabela). Uma sessão de análise anterior a esta (modo leitur
         -a, sem alteração de arquivo) já havia mapeado o comunicado item
         - a item, calculado os quatro cenários financeiros abaixo manual
         -mente e identificado as ambiguidades candidatas contra a lista 
         -de análise obrigatória do próprio processo. Esta entrada formal
         -iza as decisões tomadas sobre essas ambiguidades e atualiza `sp
         -ec.md` para a versão 1.2.                                      
      15 +**Gatilho:** recebimento do envelope oficial do Dia 2, liberado
         + às 10h — mudança de requisito do RH, política de reembolso v4,
         + com vigência imediata e retroativa à competência atual. Cinco 
         +arquivos oficiais acompanham o envelope e foram recebidos e col
         +ocados em `exemplos/envelope/` sem alteração; serão versionados
         + no commit documental correspondente: `00-ENVELOPE-LACRADO.md` 
         +(comunicado do RH), `politica-v4.json` (tabela de limites por c
         +entro de custo), `cambio.json` (taxas de câmbio por data), `des
         +pesas-envelope.json` (Rafael Nkemelu, `CC-COMERCIAL`, exercitan
         +do moeda estrangeira) e `despesas-envelope-cc-desconhecido.json
         +` (Dani Okonkwo, `CC-SUPORTE-N2`, exercitando centro de custo f
         +ora da tabela). Uma sessão de análise anterior a esta (modo lei
         +tura, sem alteração de arquivo) já havia mapeado o comunicado i
         +tem a item, calculado os quatro cenários financeiros abaixo man
         +ualmente e identificado as ambiguidades candidatas contra a lis
         +ta de análise obrigatória do próprio processo. Esta entrada for
         +maliza as decisões tomadas sobre essas ambiguidades e atualiza 
         +`spec.md` para a versão 1.2. Uma revisão independente subsequen
         +te, na mesma data, encontrou seis problemas de precisão de cont
         +rato — nenhum deles muda um total financeiro já registrado — e 
         +as correções estão incorporadas nesta mesma entrada, na seção "
         +Correção após revisão independente" ao final.                  
      16  
      17  **O que mudou na spec:**
      18  

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      17  **O que mudou na spec:**
      18  
      19  1. **Política deixa de ser um valor único (RN-019, nova).** A s
          pec 1.1 não previa política externa nem variação por centro de 
          custo — RN-007 e RN-011/012/013 tratavam os limites como consta
          ntes universais. A v1.2 introduz resolução por `colaborador.cen
          tro_custo`: tabela `padrao` como fallback integral; centro de c
          usto cadastrado usa somente as categorias que declara, sem fall
          back por categoria; limite explicitamente `0,00` recusa a categ
          oria (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`), nunca reembol
          so parcial de valor zero; categoria adicional (`representacao`)
           só é reembolsável nos centros que a declaram.
      20 -2. **Câmbio e conversão monetária (RN-020, nova).** A spec 1.1 
         -declarava explicitamente, em `§3`, que o sistema "não converte 
         -nem identifica moedas" e que "todos os valores são interpretado
         -s como reais brasileiros" — essa frase foi removida e substituí
         -da (ver "comportamento anterior invalidado" abaixo). `despesa.m
         -oeda` é novo campo opcional (oitavo campo de 4.2, entre `valor`
         - e `tem_nota_fiscal`); ausência resolve para `BRL` sem produzir
         - motivo; presença malformada recusa o item como qualquer campo 
         -estrutural. A resolução de taxa usa cotação exata ou a mais rec
         -ente anterior disponível, nunca futura, sem interpolação; moeda
         - sem cotação utilizável recusa o item (`MOEDA_SEM_COTACAO`). Co
         -nversão e arredondamento têm ordem única: multiplicar, depois a
         -rredondar uma vez (RN-004 atualizada).                         
      20 +2. **Câmbio e conversão monetária (RN-020, nova).** A spec 1.1 
         +declarava explicitamente, em `§3`, que o sistema "não converte 
         +nem identifica moedas" e que "todos os valores são interpretado
         +s como reais brasileiros" — essa frase foi removida e substituí
         +da (ver "comportamento anterior invalidado" abaixo). `despesa.m
         +oeda` é novo campo opcional — o **sétimo** dos oito campos de 4
         +.2, entre `valor` (6º) e `tem_nota_fiscal` (8º); a ausência da 
         +**chave** resolve para `BRL` sem produzir motivo, mas `"moeda":
         + null` (chave presente, valor `null`) é `CAMPO_AUSENTE`, e pres
         +ença malformada de outro jeito recusa o item como qualquer camp
         +o estrutural (correção pós-revisão, ver ao final). A resolução 
         +de taxa usa cotação exata ou a mais recente anterior disponível
         +, nunca futura, sem interpolação; moeda sem cotação utilizável 
         +recusa o item (`MOEDA_SEM_COTACAO`). Conversão e arredondamento
         + têm ordem única: multiplicar, depois arredondar uma vez (RN-00
         +4 atualizada).                                                 
      21  3. **RN-009 (nota fiscal) atualizada.** O gatilho de R$100 agor
          a compara explicitamente o valor já convertido para BRL, não o 
          valor original na moeda da despesa — caso contrário, USD 40,00 
          (abaixo do gatilho na moeda original) escaparia da obrigatoried
          ade de nota apesar de valer R$220,00 convertido.
      22  4. **RN-010 (duplicidade) atualizada.** A chave econômica ganha
           `moeda` como componente: dois itens em moedas diferentes nunca
           são duplicatas entre si, mesmo que o valor convertido coincida
           — a chave usa moeda mais valor, não o valor já convertido, par
          a não tornar a duplicidade dependente da taxa de câmbio do dia.
      23  5. **RN-017 e o contrato de saída (4.3) atualizados.** Todo reg
          istro de resultado ganha três campos novos, entre `valor_inform
          ado` e `valor_normalizado`: `moeda`, `taxa_cambio_aplicada` e `
          data_cotacao_utilizada` — necessários para que a conversão seja
           auditável sem reabrir `cambio.json` manualmente.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      19  1. **Política deixa de ser um valor único (RN-019, nova).** A s
          pec 1.1 não previa política externa nem variação por centro de 
          custo — RN-007 e RN-011/012/013 tratavam os limites como consta
          ntes universais. A v1.2 introduz resolução por `colaborador.cen
          tro_custo`: tabela `padrao` como fallback integral; centro de c
          usto cadastrado usa somente as categorias que declara, sem fall
          back por categoria; limite explicitamente `0,00` recusa a categ
          oria (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`), nunca reembol
          so parcial de valor zero; categoria adicional (`representacao`)
           só é reembolsável nos centros que a declaram.
      20  2. **Câmbio e conversão monetária (RN-020, nova).** A spec 1.1 
          declarava explicitamente, em `§3`, que o sistema "não converte 
          nem identifica moedas" e que "todos os valores são interpretado
          s como reais brasileiros" — essa frase foi removida e substituí
          da (ver "comportamento anterior invalidado" abaixo). `despesa.m
          oeda` é novo campo opcional — o **sétimo** dos oito campos de 4
          .2, entre `valor` (6º) e `tem_nota_fiscal` (8º); a ausência da 
          **chave** resolve para `BRL` sem produzir motivo, mas `"moeda":
           null` (chave presente, valor `null`) é `CAMPO_AUSENTE`, e pres
          ença malformada de outro jeito recusa o item como qualquer camp
          o estrutural (correção pós-revisão, ver ao final). A resolução 
          de taxa usa cotação exata ou a mais recente anterior disponível
          , nunca futura, sem interpolação; moeda sem cotação utilizável 
          recusa o item (`MOEDA_SEM_COTACAO`). Conversão e arredondamento
           têm ordem única: multiplicar, depois arredondar uma vez (RN-00
          4 atualizada).
      21  3. **RN-009 (nota fiscal) atualizada.** O gatilho de R$100 agor
          a compara explicitamente o valor já convertido para BRL, não o 
          valor original na moeda da despesa — caso contrário, USD 40,00 
          (abaixo do gatilho na moeda original) escaparia da obrigatoried
          ade de nota apesar de valer R$220,00 convertido.
      22 -4. **RN-010 (duplicidade) atualizada.** A chave econômica ganha
         - `moeda` como componente: dois itens em moedas diferentes nunca
         - são duplicatas entre si, mesmo que o valor convertido coincida
         - — a chave usa moeda mais valor, não o valor já convertido, par
         -a não tornar a duplicidade dependente da taxa de câmbio do dia.
      22 +4. **RN-010 (duplicidade) atualizada.** A chave econômica passa
         + a ser `data`, `categoria` normalizada, `moeda`, `valor` normal
         +izado **em BRL** (já convertido, RN-020), `fornecedor` original
         + e `descricao` original — `moeda` entra como componente **adici
         +onal**, ao lado do valor já convertido, não em lugar dele. Dois
         + itens em moedas diferentes nunca são duplicatas entre si, mesm
         +o que o valor convertido coincida exatamente: é `moeda` que imp
         +ede a colisão, não o fato de o valor estar ou não convertido. U
         +sar o valor em BRL mantém a chave de duplicidade alinhada ao me
         +smo número que todas as regras financeiras posteriores usam.   
      23  5. **RN-017 e o contrato de saída (4.3) atualizados.** Todo reg
          istro de resultado ganha três campos novos, entre `valor_inform
          ado` e `valor_normalizado`: `moeda`, `taxa_cambio_aplicada` e `
          data_cotacao_utilizada` — necessários para que a conversão seja
           auditável sem reabrir `cambio.json` manualmente.
      24  6. **RN-021 (nova) — vigência do arquivo de política.** `politi
          ca.vigencia` é obrigatória e validada estruturalmente (mesma se
          veridade de RN-001, mas sobre o arquivo de política, não o de d
          espesas); uma vez válida, é metadado informativo — não filtra i
          tens por data, porque nenhuma política anterior à v4 foi fornec
          ida para justificar seleção histórica.
      25  7. **RN-022 (nova) — processabilidade dos arquivos externos.** 
          Política ou câmbio ausente, ilegível, sintaticamente inválido o
          u estruturalmente inválido é erro global: nenhuma apuração é pr
          oduzida, mesmo com um arquivo de despesas perfeitamente válido.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 3 lines, removed 3 lines
      23  5. **RN-017 e o contrato de saída (4.3) atualizados.** Todo reg
          istro de resultado ganha três campos novos, entre `valor_inform
          ado` e `valor_normalizado`: `moeda`, `taxa_cambio_aplicada` e `
          data_cotacao_utilizada` — necessários para que a conversão seja
           auditável sem reabrir `cambio.json` manualmente.
      24  6. **RN-021 (nova) — vigência do arquivo de política.** `politi
          ca.vigencia` é obrigatória e validada estruturalmente (mesma se
          veridade de RN-001, mas sobre o arquivo de política, não o de d
          espesas); uma vez válida, é metadado informativo — não filtra i
          tens por data, porque nenhuma política anterior à v4 foi fornec
          ida para justificar seleção histórica.
      25  7. **RN-022 (nova) — processabilidade dos arquivos externos.** 
          Política ou câmbio ausente, ilegível, sintaticamente inválido o
          u estruturalmente inválido é erro global: nenhuma apuração é pr
          oduzida, mesmo com um arquivo de despesas perfeitamente válido.
      26 -8. **RN-002, RN-004, RN-005, RN-007 atualizadas** para refletir
         - o oitavo campo (`despesa.moeda`), a ordem conversão-então-arre
         -dondamento, e o conjunto de categorias reembolsáveis deixando d
         -e ser uma constante de três valores.                           
      27 -9. **Quinze ambiguidades novas registradas (`AMB-019` a `AMB-03
         -3`)**, cobrindo: centro de custo desconhecido/ausente (AMB-019)
         -, categoria ausente em centro cadastrado (AMB-020), `representa
         -cao` exclusiva de quem a declara (AMB-021), limite igual a zero
         - (AMB-022), moeda como indício de viagem (AMB-023), data sem co
         -tação exata (AMB-024), moeda sem cotação alguma (AMB-025), orde
         -m de conversão/arredondamento (AMB-026), base de comparação do 
         -gatilho de nota fiscal (AMB-027), chave de duplicidade entre mo
         -edas (AMB-028), campos de auditoria da conversão (AMB-029), val
         -idação de vigência (AMB-030), erro global versus recusa individ
         -ual nos arquivos externos (AMB-031), regressão da baseline hist
         -órica (AMB-032) e escopo do item C (AMB-033).                  
      28 -10. **Dezessete critérios de aceite novos (`CA-024` a `CA-040`)
         -**, incluindo os quatro cenários financeiros completos do `§12`
         - (nova seção).                                                 
      26 +8. **RN-002, RN-004, RN-005, RN-007 atualizadas** para refletir
         + o sétimo campo (`despesa.moeda`), a ordem conversão-então-arre
         +dondamento, e o conjunto de categorias reembolsáveis deixando d
         +e ser uma constante de três valores.                           
      27 +9. **Dezoito ambiguidades novas registradas (`AMB-019` a `AMB-0
         +36`)**, cobrindo: centro de custo desconhecido/ausente (AMB-019
         +), categoria ausente em centro cadastrado (AMB-020), `represent
         +acao` exclusiva de quem a declara (AMB-021), limite igual a zer
         +o (AMB-022), moeda como indício de viagem (AMB-023), data sem c
         +otação exata (AMB-024), moeda sem cotação alguma (AMB-025), ord
         +em de conversão/arredondamento (AMB-026), base de comparação do
         + gatilho de nota fiscal (AMB-027), chave de duplicidade entre m
         +oedas (AMB-028), campos de auditoria da conversão (AMB-029), va
         +lidação de vigência (AMB-030), erro global versus recusa indivi
         +dual nos arquivos externos (AMB-031), regressão da baseline his
         +tórica (AMB-032), escopo do item C (AMB-033), e as três acresce
         +ntadas na correção pós-revisão: forma de fornecimento de políti
         +ca/câmbio à CLI (AMB-034), contrato estrutural dos arquivos ext
         +ernos (AMB-035) e semântica de `periodicidade` (AMB-036).      
      28 +10. **Vinte e cinco critérios de aceite novos (`CA-024` a `CA-0
         +48`)**: `CA-024` a `CA-040` da primeira leitura do envelope, in
         +cluindo os quatro cenários financeiros completos do `§12` (nova
         + seção); `CA-041` a `CA-048` da correção pós-revisão, cobrindo 
         +o contrato de execução, os contratos estruturais dos arquivos e
         +xternos, a periodicidade diária de `representacao` e `despesa.m
         +oeda: null`.                                                   
      29  
      30  **Comportamento anterior invalidado:**
      31  

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 2 lines, removed 2 lines
      44  - **Conversão e regras financeiras:** um único arredondamento a
          pós a multiplicação; nota fiscal e duplicidade operam sobre o v
          alor convertido (duplicidade com moeda como componente adiciona
          l da chave, não substituindo o valor).
      45  - **Saída e vigência:** três campos de auditoria novos na saída
          ; vigência obrigatória e validada, mas informativa quanto a ite
          ns individuais.
      46  
      47 -**RN e CA antigos afetados:** RN-002, RN-004, RN-005, RN-007, R
         -N-009, RN-010, RN-017 (textos atualizados, sem troca de número)
         -. Nenhum RN ou CA de `1.1` foi removido ou renumerado — `RN-001
         -` a `RN-018` e `CA-001` a `CA-023` permanecem intactos onde não
         - listados acima.                                               
      47 +**RN e CA antigos afetados:** RN-002, RN-004, RN-005, RN-007, R
         +N-009, RN-010, RN-017 (textos atualizados, sem troca de número;
         + RN-007 recebeu correção adicional na revisão — ver ao final). 
         +Nenhum RN ou CA de `1.1` foi removido ou renumerado — `RN-001` 
         +a `RN-018` e `CA-001` a `CA-023` permanecem intactos onde não l
         +istados acima.                                                 
      48  
      49 -**RN e CA novos:** `RN-019` a `RN-022`; `CA-024` a `CA-040`.   
      49 +**RN e CA novos:** `RN-019` a `RN-022` (nenhuma RN além de `RN-
         +022` foi criada — a correção pós-revisão só atualizou RN-019 e 
         +RN-022, sem numeração nova); `CA-024` a `CA-048`.              
      50  
      51  **DT-007 invalidada.** `plan.md` ainda declara, em DT-007, "est
          rutura única e imutável... sem mecanismo genérico de regras... 
          sem arquivo externo de configuração" para `PoliticaReembolso` —
           essa decisão técnica está tecnicamente incorreta a partir dest
          a versão da spec e será revista quando `plan.md` for atualizado
          , em etapa posterior. Esta entrada apenas formaliza a invalidaç
          ão do lado da spec; nenhuma linha de `plan.md` foi tocada nesta
           rodada.
      52  

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 16 lines, removed 1 line
      54  
      55  **Item C fora de escopo.** A fila de aprovação manual (`AGUARDA
          NDO_APROVACAO` para itens acima de R$500) não foi especificada 
          nesta rodada — é explicitamente opcional no próprio comunicado 
          do RH, e nenhum item dos dois arquivos de exemplo do envelope u
          ltrapassa R$500 sob a interpretação adotada, então nada nos dad
          os obriga essa decisão agora (`AMB-033`).
      56  
      57 -**Custo documental e técnico previsto:** esta entrada e a atual
         -ização de `spec.md` (cabeçalho, `§0` nova, `§3`, `§4.1`/`4.1.1`
         - nova, `§4.2`, `§4.3`, `§4.5`, `§4.7`, `§5` com sete RN atualiz
         -adas e quatro novas, `§6` com quinze AMB novas, `§7`, `§8.1` a 
         -`8.5`, `§9`, `§10` com dezessete CA novas, `§11`, `§12` nova) —
         - nenhum outro arquivo tocado nesta rodada. `plan.md` precisará 
         -de: nova DT substituindo DT-007, extensão de DT-003 (CLI) e DT-
         -005 (parsing) para os arquivos externos, e um novo passo no pip
         -eline (§2). `tasks.md` precisará de tasks novas a partir de `T-
         -022` para: leitor de política, leitor de câmbio, campo `moeda` 
         -no contrato do item, conversão cambial no pipeline, resolução d
         -e política por centro de custo, motivo `CATEGORIA_NAO_REEMBOLSA
         -VEL_CENTRO_CUSTO`, motivo `MOEDA_SEM_COTACAO`, três campos novo
         -s de saída, validação de `vigencia`, novos argumentos de CLI, e
         - os quatro cenários de regressão do `§12` como fixtures de test
         -e.                                                             
      57 +**Custo documental e técnico previsto:** esta entrada e a atual
         +ização de `spec.md` (cabeçalho, `§0` nova, `§3`, `§4.1`/`4.1.1`
         + nova e depois reescrita na correção, `§4.2`, `§4.3`, `§4.5`, `
         +§4.7`, `§5` com sete RN atualizadas e quatro novas — duas delas
         + corrigidas de novo na revisão —, `§6` com dezoito AMB novas, `
         +§7`, `§8.1` a `8.5`, `§9`, `§10` com vinte e cinco CA novas, `§
         +11`, `§12` nova) — nenhum outro arquivo tocado nesta rodada. `p
         +lan.md` precisará de: nova DT substituindo DT-007, extensão de 
         +DT-003 (CLI, agora com o contrato exato já fixado em `spec.md` 
         +`§4.1.1`) e DT-005 (parsing) para os arquivos externos, e um no
         +vo passo no pipeline (§2). `tasks.md` precisará de tasks novas 
         +a partir de `T-022` para: leitor de política, leitor de câmbio,
         + campo `moeda` no contrato do item (incluindo a distinção ausên
         +cia-de-chave versus `null`), conversão cambial no pipeline, res
         +olução de política por centro de custo (incluindo a semântica d
         +e `periodicidade`), motivo `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_C
         +USTO`, motivo `MOEDA_SEM_COTACAO`, três campos novos de saída, 
         +validação de `vigencia`, os quatro argumentos de CLI com seus c
         +ódigos de saída, validação estrutural dos dois arquivos externo
         +s, e os quatro cenários de regressão do `§12` como fixtures de 
         +teste.                                                         
      58  
      59  **Tasks afetadas:** nenhuma ainda — `plan.md` e `tasks.md` serã
          o atualizados em etapas posteriores a esta, que é estritamente 
          normativa (`spec.md` e este arquivo).
      60  
      61 +### Correção após revisão independente (mesma data, `2026-08-05
         +`)                                                             
      62 +                                                               
      63 +Uma revisão independente da primeira leitura do envelope — ante
         +s de qualquer código ser escrito — encontrou seis problemas de 
         +precisão de contrato. Nenhum deles altera um total financeiro j
         +á registrado nesta entrada (R$585,43 / R$351,43 / R$1.143,26 / 
         +R$373,76 permanecem exatamente como acima); todos são correções
         + de exatidão normativa.                                        
      64 +                                                               
      65 +1. **`despesa.moeda` é o sétimo campo do contrato, não o oitavo
         +.** O contrato tem oito campos; sete são obrigatórios, mas não 
         +são "os sete primeiros" — `despesa.moeda`, o único opcional, oc
         +upa a sétima posição, entre `despesa.valor` (6º) e `despesa.tem
         +_nota_fiscal` (8º, também obrigatório). Texto corrigido em `spe
         +c.md` `§4.2` e nesta entrada (item 2 acima).                   
      66 +2. **`despesa.moeda: null` não é mais tolerado como a ausência 
         +da chave.** Só a **ausência da própria chave** `moeda` no objet
         +o resolve para `BRL` sem motivo. `"moeda": null` — chave presen
         +te, valor `null` — é `CAMPO_AUSENTE`, `campo` igual a `despesa.
         +moeda`, como qualquer campo obrigatório. Tipo não textual conti
         +nua `CAMPO_TIPO_INVALIDO`; formato fora de `[A-Z]{3}` continua 
         +`CAMPO_FORMATO_INVALIDO`. Novo critério `CA-048`.              
      67 +3. **Contrato exato da CLI incorporado à spec (`AMB-034`, nova)
         +.** `spec.md` `§4.1.1` passa a declarar, normativamente: `calcu
         +lar --input <e> --output <s> --politica <p> --cambio <c>`, quat
         +ro argumentos obrigatórios em qualquer ordem, cada um uma única
         + vez; argumento ausente/repetido/desconhecido, ou política/câmb
         +io inválidos, retornam código `2` (sem código `4`/`5` novo); en
         +velope de despesas inválido continua código `3`; sucesso contin
         +ua `0`; saída preexistente preservada em qualquer falha. A fras
         +e anterior, que reservava nomes de argumento e códigos de saída
         + exclusivamente ao `plan.md`, foi removida — o contrato de exec
         +ução é comportamento observável do produto e é normativo da spe
         +c. Novos critérios `CA-041` a `CA-044`.                        
      68 +4. **Contrato estrutural fechado dos dois arquivos externos (`A
         +MB-035`, nova).** A versão anterior desta correção descrevia `c
         +ambio.json` como um mapa direto de data para moeda — **incorret
         +o**: a raiz real tem `moeda_base`, `fonte`, `observacao` e as c
         +otações aninhadas sob `taxas` (`taxas.AAAA-MM-DD.USD` etc.). `s
         +pec.md` `§4.1.1` agora documenta o contrato completo de ambos o
         +s arquivos — que campos são obrigatórios, seus tipos e restriçõ
         +es (`limite` ≥ 0, `periodicidade` exatamente `"dia"` ou `"diari
         +a"`, taxas estritamente positivas) — tornando "estruturalmente 
         +inválido" (RN-022) verificável sem leitura de código. `taxas: {
         +}` é explicitamente válido (recusa despesas estrangeiras item a
         + item, não invalida o arquivo). Novos critérios `CA-045`, `CA-0
         +46`.                                                           
      69 +5. **Semântica de `periodicidade` formalizada (`AMB-036`, nova;
         + RN-019 atualizada).** `"dia"` é teto compartilhado entre itens
         + da mesma categoria e data (mecanismo de RN-011/RN-012/RN-015);
         + `"diaria"` é teto individual por lançamento (mecanismo de RN-0
         +13). `representacao` em `CC-COMERCIAL` usa `periodicidade: "dia
         +"`, logo dois itens de `representacao` na mesma data compartilh
         +am o saldo de R$300,00 — não são tetos individuais de R$300,00 
         +cada. `periodicidade` fora desse vocabulário fechado invalida o
         + **arquivo de política**, não o item. Novo critério `CA-047`.  
      70 +6. **RN-007 e RN-019 corrigidas: nunca há duas tabelas de polít
         +ica simultaneamente aplicáveis.** O texto anterior desta entrad
         +a, ao dizer que a categoria era verificada contra "padrão e, qu
         +ando houver, específica do centro de custo", sugeria as duas ta
         +belas ativas ao mesmo tempo — o que a própria RN-019 já contrad
         +izia (centro cadastrado usa **somente** a sua tabela). Corrigid
         +o para: exatamente uma tabela é aplicável a cada item — `padrao
         +` quando o centro de custo é desconhecido/ausente/nulo/inválido
         +, ou exclusivamente a tabela do centro cadastrado, quando houve
         +r. Consequência: quando o centro é cadastrado, **toda** ausênci
         +a de categoria nessa tabela — inclusive uma categoria que não e
         +xiste em lugar nenhum da política, como `coworking` — é `CATEGO
         +RIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITIC
         +A`; esta última passa a significar exclusivamente "ausente de `
         +padrao`, quando `padrao` é a tabela aplicável". Isso muda o **m
         +otivo** do item `e-009` (`coworking`, Rafael/`CC-COMERCIAL`) no
         + cenário do `§12.3`, de `CATEGORIA_FORA_POLITICA` para `CATEGOR
         +IA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — o `valor_reembolsavel` (`0,
         +00`) e o total de Rafael (`R$1.143,26`) **não mudam**. `AMB-021
         +` (`representacao`) também foi corrigida para não tratar essa c
         +ategoria como caso especial: segue a mesma régua de qualquer ca
         +tegoria ausente de uma tabela cadastrada.                      
      71 +7. **Exemplo de conversão cambial corrigido.** O exemplo de "mo
         +eda estrangeira convertida" em `spec.md` `§4.3` pareava `taxa_c
         +ambio_aplicada: 5.93` com `data_cotacao_utilizada: "2026-07-17"
         +` — errado: `5.93` é a cotação de EUR em `2026-07-14`; a cotaçã
         +o de `2026-07-17` é `5.96`. Corrigido para `5.96`/`2026-07-17`.
         + O cálculo de `e-004` no `§12.3` (`30,00 × 5,96 = 178,80`) já u
         +sava o valor correto desde a primeira leitura — o erro estava i
         +solado no fragmento ilustrativo de `§4.3`, não nos totais.     
      72 +8. **Bloco `colaborador`: precisão sobre "regra financeira".** 
         +A frase "o bloco não participa de regra financeira alguma" (`§4
         +.1`) deixou de ser universalmente verdadeira desde que `centro_
         +custo` passou a selecionar política (item 1 desta entrada) — su
         +bstituída por uma descrição precisa: `colaborador.id`/`colabora
         +dor.nome` continuam sem uso em regra alguma; `colaborador.centr
         +o_custo`, quando texto e reconhecido, seleciona a política (RN-
         +019); ausente/nulo/inválido/desconhecido usa `padrao`; nenhum d
         +efeito do bloco invalida o arquivo.                            
      73 +                                                               
      74 +**Ambiguidades e critérios desta correção:** `AMB-034`, `AMB-03
         +5`, `AMB-036`; `CA-041` a `CA-048`. Contagem final: `AMB-001` a
         + `AMB-036` (36), `RN-001` a `RN-022` (22, nenhuma nova além de 
         +`RN-022`), `CA-001` a `CA-048` (48).                           
      75 +                                                               
      76  ---
      77  
      78  ## D-002 — Ajustes de contrato após auditoria independente · `2
          026-07-30`

  Searched for 1 pattern (ctrl+o to expand)

● Good, no residual conflicting phrasing. Let's verify counts and consistency
  across the whole file once more, then run the git verification commands.

  Ran 1 shell command (ctrl+o to expand)

● All counts confirmed: 36 AMB, 22 RN, 48 CA. Now let's run the full
  verification sequence.

  Ran 2 shell commands (ctrl+o to expand)

● Confirmed — no diff output for plan.md, tasks.md, README.md, src, tests, or
  exemplos/envelope; the five envelope files are present and untouched.

  Resumo

  Contagem final: AMB-001 a AMB-036 (36) · RN-001 a RN-022 (22, nenhuma nova
  além de RN-022) · CA-001 a CA-048 (48).

  AMB-034, AMB-035, AMB-036:
  - AMB-034 — contrato exato da CLI incorporado à spec (não mais delegado ao
  plan.md): calcular --input <e> --output <s> --politica <p> --cambio <c>,
  quatro flags obrigatórias em qualquer ordem, cada uma uma vez.
  - AMB-035 — contrato estrutural fechado dos dois arquivos externos, corrigindo
  o erro de estrutura do cambio.json (a raiz real aninha as cotações sob taxas,
  não é um mapa direto de data).
  - AMB-036 — semântica de periodicidade: "dia" = teto compartilhado
  (RN-011/012/015); "diaria" = teto individual (RN-013); fora desse vocabulário
  invalida o arquivo de política.

  Contrato final da CLI: --input, --output, --politica, --cambio — quatro
  obrigatórios, qualquer ordem, um cada; ausente/repetido/desconhecido ou
  política/câmbio inválidos → exit 2 (sem código 4/5 novo); envelope inválido →
  exit 3; sucesso → 0; stdout vazio; erro em stderr; saída preexistente sempre
  preservada.

  Contrato final de política e câmbio: documentado por completo em §4.1.1 —
  política exige vigencia, moeda_base="BRL", padrao, centros_custo,
  nota_fiscal_obrigatoria_acima_de; cada tabela de categoria exige limite ≥ 0 e
  periodicidade em {"dia","diaria"}. Câmbio exige moeda_base="BRL" e taxas
  (aninhado data → moeda → taxa positiva), podendo taxas ser vazio.

  Semântica final de periodicidade: "dia" compartilha saldo entre itens da mesma
  categoria/data (ex.: representacao); "diaria" é teto por lançamento
  (hospedagem).

  Tratamento final de moeda ausente vs. nula: só a ausência da chave resolve
  para BRL sem motivo; "moeda": null é CAMPO_AUSENTE.

  Correções em RN-010/D-003: RN-010 já usava corretamente moeda + valor
  normalizado em BRL — a contradição estava em AMB-028 e em D-003, que diziam
  "não o valor já convertido"; ambos corrigidos para afirmar consistentemente
  que a chave usa moeda e o valor convertido em BRL.

  Achado adicional durante a correção (não pedido explicitamente, mas 
  consequência direta das instruções da seção 8): RN-007/RN-019 tinham uma
  inconsistência real — diziam que padrão e a tabela específica eram verificadas
  "juntas". Corrigido para "uma única tabela aplicável por vez". Consequência
  prática: coworking de Rafael (CC-COMERCIAL, cadastrado) agora é
  CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO, não CATEGORIA_FORA_POLITICA — motivo
  diferente, valor_reembolsavel e total de Rafael inalterados (R$1.143,26).

  git status --short:
   M specs/001-motor-reembolso/DECISIONS.md
   M specs/001-motor-reembolso/spec.md
  ?? exemplos/envelope/

  Decisão que tomei sem instrução explícita, para registro: mantive a spec em
  versão 1.2/em revisão (não bumpei para 1.3), já que D-003 foi corrigida no
  lugar, não substituída por uma nova entrada — mas o projeto tem precedente
  (D-001→D-002) de bump de versão em correção pós-auditoria, então sinalizo essa
  escolha caso você prefira 1.3.

  Parando para revisão.

✻ Cogitated for 14m 49s

> Faça uma última correção textual exclusivamente em:                           
                                                                                
  `specs/001-motor-reembolso/DECISIONS.md`                                      
                                                                                
  Não altere `spec.md` nem qualquer outro arquivo.                              
                                                                                
  Corrija somente estes dois pontos:                                            
                                                                                
  1. Na seção `Ambiguidades identificadas`, substitua a afirmação antiga sobre  
  quinze ambiguidades e `AMB-019` a `AMB-033` por:                              
                                                                                
  ```text                                                                       
  Ambiguidades identificadas: as dezoito listadas no item 9 acima, com decisão, 
  justificativa e regra afetada registradas em `spec.md §6` (`AMB-019` a        
  `AMB-036`).                                                                   
  ```                                                                           
                                                                                
  2. No início de `Correção após revisão independente`, substitua:              
                                                                                
  ```text                                                                       
  encontrou seis problemas de precisão de contrato                              
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  encontrou oito problemas de precisão de contrato                              
  ```                                                                           
                                                                                
  Não altere a versão da spec: permanece `1.2`, status `em revisão`.            
                                                                                
  Depois execute:                                                               
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  ```                                                                           
                                                                                
  Confirme que:                                                                 
                                                                                
  * somente `spec.md` e `DECISIONS.md` permanecem modificados;                  
  * `exemplos/envelope/` permanece novo e intacto;                              
  * nenhum outro arquivo foi alterado;                                          
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Ao final, apresente as duas linhas corrigidas e pare para revisão.            

  Searched for 1 pattern (ctrl+o to expand)

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      35  - A garantia de que `colaborador.centro_custo` "nunca é usado p
          ara cruzar dados" (§3, sobre agregação entre arquivos) permanec
          e verdadeira, mas a leitura mais ampla de que o campo não tem "
          uso em regra alguma" deixa de valer: `centro_custo` passa a det
          erminar a política aplicável dentro do próprio arquivo (RN-019)
          . A tolerância estrutural do bloco `colaborador` (RN-001) não m
          uda — só o efeito financeiro de um `centro_custo` presente e re
          conhecido.
      36  - `§11`, "o que fica em aberto", removeu a linha "Apenas BRL é 
          suportado" — deixou de ser uma limitação de produto e passou a 
          ser requisito atendido.
      37  
      38 -**Ambiguidades identificadas:** as quinze listadas no item 9 ac
         -ima, com decisão, justificativa e regra afetada registradas em 
         -`spec.md §6` (`AMB-019` a `AMB-033`).                          
      38 +**Ambiguidades identificadas:** as dezoito listadas no item 9 a
         +cima, com decisão, justificativa e regra afetada registradas em
         + `spec.md §6` (`AMB-019` a `AMB-036`).                         
      39  
      40  **Decisões dos quatro grupos** (política por centro de custo; c
          âmbio; conversão e regras financeiras; saída e vigência):
      41  

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      60  
      61  ### Correção após revisão independente (mesma data, `2026-08-05
          `)
      62  
      63 -Uma revisão independente da primeira leitura do envelope — ante
         -s de qualquer código ser escrito — encontrou seis problemas de 
         -precisão de contrato. Nenhum deles altera um total financeiro j
         -á registrado nesta entrada (R$585,43 / R$351,43 / R$1.143,26 / 
         -R$373,76 permanecem exatamente como acima); todos são correções
         - de exatidão normativa.                                        
      63 +Uma revisão independente da primeira leitura do envelope — ante
         +s de qualquer código ser escrito — encontrou oito problemas de 
         +precisão de contrato. Nenhum deles altera um total financeiro j
         +á registrado nesta entrada (R$585,43 / R$351,43 / R$1.143,26 / 
         +R$373,76 permanecem exatamente como acima); todos são correções
         + de exatidão normativa.                                        
      64  
      65  1. **`despesa.moeda` é o sétimo campo do contrato, não o oitavo
          .** O contrato tem oito campos; sete são obrigatórios, mas não 
          são "os sete primeiros" — `despesa.moeda`, o único opcional, oc
          upa a sétima posição, entre `despesa.valor` (6º) e `despesa.tem
          _nota_fiscal` (8º, também obrigatório). Texto corrigido em `spe
          c.md` `§4.2` e nesta entrada (item 2 acima).
      66  2. **`despesa.moeda: null` não é mais tolerado como a ausência 
          da chave.** Só a **ausência da própria chave** `moeda` no objet
          o resolve para `BRL` sem motivo. `"moeda": null` — chave presen
          te, valor `null` — é `CAMPO_AUSENTE`, `campo` igual a `despesa.
          moeda`, como qualquer campo obrigatório. Tipo não textual conti
          nua `CAMPO_TIPO_INVALIDO`; formato fora de `[A-Z]{3}` continua 
          `CAMPO_FORMATO_INVALIDO`. Novo critério `CA-048`.

  Ran 1 shell command (ctrl+o to expand)

> /compact                                                                      

● Confirmed — both corrections landed, and the diff shows only the DECISIONS.md
  changes (the full D-003 block appears as "new" simply because it was never
  committed in this session, not because anything else changed). git status 
  --short still shows exactly spec.md and DECISIONS.md modified, plus
  exemplos/envelope/ untracked and intact. No other files touched, no /export,
  no git add, no commit, no push.

  The two corrected lines:

  1.
  **Ambiguidades identificadas:** as dezoito listadas no item 9 acima, com
  decisão, justificativa e regra afetada registradas em `spec.md §6` (`AMB-019`
  a `AMB-036`).

  2.
  Uma revisão independente da primeira leitura do envelope — antes de qualquer
  código ser escrito — encontrou oito problemas de precisão de contrato.

  Spec version unchanged: 1.2, status em revisão.

  Parando para revisão.

✻ Churned for 29s

※ recap: Updating specs/001-motor-reembolso/spec.md and DECISIONS.md for the Dia 2 v4 policy envelope; just finished a two-line correction to DECISIONS.md (AMB-019 to AMB-036 count, eight contract-precision problems). Next: waiting on your review before any further changes. (disable recaps in /config)

> /compact                                                                        ⎿  Compacted (ctrl+o to see full summary)
  ⎿  Referenced file specs\001-motor-reembolso\DECISIONS.md
  ⎿  Referenced file specs\001-motor-reembolso\spec.md
  ⎿  Read exemplos\despesas-exemplo.json (141 lines)
  ⎿  Read src\main\java\com\desafio\reembolso\modelo\RegraNegocio.java (34 
     lines)
  ⎿  Read src\main\java\com\desafio\reembolso\modelo\Decisao.java (19 lines)

> A execução anterior foi interrompida porque o computador desligou.            
                                                                                
  Retome exatamente a última tarefa solicitada nesta conversa: a correção       
  normativa final da spec 1.2 após a segunda revisão independente.              
                                                                                
  Antes de continuar:                                                           
                                                                                
  1. releia minha última instrução completa;                                    
  2. inspecione o estado atual dos arquivos;                                    
  3. verifique quais alterações já foram aplicadas antes da interrupção;        
  4. não repita nem reverta alterações corretas já realizadas;                  
  5. conclua somente o que ainda estiver pendente.                              
                                                                                
  Os únicos arquivos autorizados para alteração continuam sendo:                
                                                                                
  * `specs/001-motor-reembolso/spec.md`                                         
  * `specs/001-motor-reembolso/DECISIONS.md`                                    
                                                                                
  Não altere `plan.md`, `tasks.md`, README, código, testes, fixtures ou os      
  arquivos de `exemplos/envelope/`.                                             
                                                                                
  A versão continua 1.2 e o status continua em revisão.                         
                                                                                
  Ao concluir, execute:                                                         
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  ```                                                                           
                                                                                
  Confirme as contagens finais esperadas:                                       
                                                                                
  * AMB-001 a AMB-037;                                                          
  * RN-001 a RN-022;                                                            
  * CA-001 a CA-049.                                                            
                                                                                
  Confirme também que os quatro totais permanecem:                              
                                                                                
  * baseline histórica: 585.43;                                                 
  * exemplo antigo sob v4: 351.43;                                              
  * Rafael: 1143.26;                                                            
  * Dani: 373.76.                                                               
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  Pare para revisão ao terminar.                                                

  Ran 4 shell commands (ctrl+o to expand)

● Checked the current state before touching anything.

  What's already in place (confirmed via git diff --stat, grep -c, and reading
  both files): spec.md sits at 36 AMB / 22 RN / 48 CA, and DECISIONS.md carries
  the two literal fixes from your last completed instruction ("dezoito...
  AMB-036" and "encontrou oito problemas de precisão de contrato"). That matches
  exactly the end state of the round before the shutdown — nothing from a
  "segunda revisão independente" (which would push counts to AMB-037/CA-049) has
  been applied yet.

  Two things I can't safely proceed on without your input:

  1. I have no record of what the second independent review actually found. The
  conversation history available to me ends at the "dezoito/oito" fix — there's
  no file, commit, reflog entry, or draft anywhere in the repo that captures the
  substantive finding that would justify a new AMB-037 and CA-049 (I checked:
  no stray files, no new commits, nothing modified in the last two hours besides
  what's already accounted for). Per the project's own rule in CLAUDE.md, I
  won't invent a business-rule ambiguity or acceptance criterion that wasn't
  actually specified — that would be exactly the "regra que não está na spec...
  explicada no chat" problem the project is designed to avoid. I need you to
  restate what the second review flagged.
  2. Separately, while checking I found a real leftover inconsistency your last
  instruction didn't reach: DECISIONS.md still has two occurrences of the
  problem count, not one. Line 63 (start of "Correção após revisão
  independente") now correctly says "oito problemas de precisão de contrato" —
  that's the one your instruction targeted. But line 15, in the Gatilho
  paragraph, still says "encontrou seis problemas de precisão de contrato" — it
  wasn't part of your literal replacement text, so it's now inconsistent with
  line 63.

  Can you give me the substance of the second independent review's finding
  (what's wrong, in AMB-037/CA-049), and confirm whether you also want line 15's
  stale "seis" fixed to "oito" for internal consistency? I'll hold off on any
  edit until then.

✻ Crunched for 1m 5s

※ recap: We're updating the reimbursement-engine spec through a staged SDD review process. Right now I'm blocked waiting for you to tell me what the second independent review actually found, since I can't locate that content anywhere in the repo. (disable recaps in /config)

> Sim, corrija também a ocorrência antiga da linha 15 de `DECISIONS.md`:        
                                                                                
  ```text                                                                       
  encontrou seis problemas de precisão de contrato                              
  ```                                                                           
                                                                                
  deve se tornar:                                                               
                                                                                
  ```text                                                                       
  encontrou oito problemas de precisão de contrato                              
  ```                                                                           
                                                                                
  A segunda revisão independente encontrou os pontos abaixo. Retome a correção  
  normativa com base neles.                                                     
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  * `specs/001-motor-reembolso/spec.md`                                         
  * `specs/001-motor-reembolso/DECISIONS.md`                                    
                                                                                
  Não altere `plan.md`, `tasks.md`, README, código, testes, fixtures ou os      
  arquivos de `exemplos/envelope/`.                                             
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  A spec permanece na versão `1.2`, status `em revisão`.                        
                                                                                
  ## 1. Critérios históricos sem política explícita                             
                                                                                
  Os critérios antigos ainda apresentam os valores históricos como se fossem    
  universais, embora toda execução agora dependa de política externa.           
                                                                                
  Atualize, sem renumerar:                                                      
                                                                                
  ### CA-001 e CA-002                                                           
                                                                                
  Declare explicitamente que usam:                                              
                                                                                
  * `exemplos/despesas-exemplo.json`;                                           
  * uma política externa de teste equivalente à política histórica/padrão;      
  * alimentação `60/dia`;                                                       
  * transporte urbano `80/dia`;                                                 
  * hospedagem `250/diaria`;                                                    
  * gatilho de nota fiscal `100`.                                               
                                                                                
  CA-001 continua totalizando `585.43`.                                         
                                                                                
  CA-002 continua verificando os 14 registros da tabela 4.7 nesse mesmo cenário 
  histórico.                                                                    
                                                                                
  ### CA-004 a CA-007                                                           
                                                                                
  Qualifique todos como comportamento sob essa política histórica/padrão.       
                                                                                
  ### CA-008 e CA-009                                                           
                                                                                
  Declare que os exemplos usam:                                                 
                                                                                
  ```text                                                                       
  nota_fiscal_obrigatoria_acima_de = 100.00                                     
  ```                                                                           
                                                                                
  O valor `100` não é uma constante interna universal: é o valor configurado na 
  política usada no cenário.                                                    
                                                                                
  Atualize RN-009 com a mesma precisão.                                         
                                                                                
  ### CA-013                                                                    
                                                                                
  Declare que a primeira ocorrência é elegível para reembolso integral antes da 
  avaliação de duplicidade: categoria, política, competência, nota e saldo      
  devem permiti-lo.                                                             
                                                                                
  ### CA-016                                                                    
                                                                                
  Declare explicitamente:                                                       
                                                                                
  * centro desconhecido, ausente, nulo ou inválido;                             
  * tabela aplicável `padrao`;                                                  
  * `padrao` não declara `coworking`.                                           
                                                                                
  Nesse cenário o motivo é `CATEGORIA_FORA_POLITICA`.                           
                                                                                
  Não use esse motivo como regra geral para centros cadastrados.                
                                                                                
  ### CA-018                                                                    
                                                                                
  Declare que o item está elegível, dentro da competência, com nota quando      
  necessária e com saldo suficiente sob a política histórica.                   
                                                                                
  Revise também os aceites de RN-011 a RN-015 e os casos de borda que usam      
  `60`, `80`, `250` ou `100`, indicando a política usada.                       
                                                                                
  ## 2. Contradição em CA-026                                                   
                                                                                
  CA-026 atualmente afirma que `representacao`, fora de `CC-COMERCIAL`, sempre  
  recebe `CATEGORIA_FORA_POLITICA`. Isso contradiz AMB-021 e RN-019.            
                                                                                
  Substitua CA-026 por:                                                         
                                                                                
  ```text                                                                       
  CA-026 — `representacao` é reembolsável somente quando a única tabela         
  aplicável a declara. Em `CC-COMERCIAL`, aplica-se o limite configurado.       
  Quando a tabela aplicável é `padrao` e ela não declara `representacao`, o     
  item recebe `CATEGORIA_FORA_POLITICA`. Quando a tabela aplicável é a de outro 
  centro de custo cadastrado que não declara `representacao`, o item recebe     
  `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`.                                    
  ```                                                                           
                                                                                
  Não renumere o critério.                                                      
                                                                                
  ## 3. Redação da tabela `centros_custo`                                       
                                                                                
  Na seção 4.1.1, substitua a redação:                                          
                                                                                
  ```text                                                                       
  substitui integralmente `padrao` para as categorias que declarar              
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  Cada centro cadastrado possui uma tabela completa e exclusiva para aquele     
  centro. Quando selecionada, ela substitui integralmente `padrao`; categorias  
  que não estiverem declaradas nela não recebem fallback.                       
  ```                                                                           
                                                                                
  Revise qualquer frase equivalente em D-003.                                   
                                                                                
  ## 4. Limite zero na política `padrao`                                        
                                                                                
  O contrato atual permite `limite: 0` em qualquer tabela, mas o motivo         
  existente é específico de centro cadastrado.                                  
                                                                                
  Feche o contrato:                                                             
                                                                                
  * em `padrao`, todo `limite` deve ser **estritamente maior que zero**;        
  * em uma tabela de `centros_custo`, `limite` pode ser maior ou igual a zero;  
  * limite zero em centro cadastrado continua significando categoria não        
  reembolsável;                                                                 
  * limite zero em `padrao` torna o arquivo de política estruturalmente         
  inválido;                                                                     
  * falha global com exit `2`.                                                  
                                                                                
  Atualize:                                                                     
                                                                                
  * 4.1.1;                                                                      
  * RN-019;                                                                     
  * RN-022;                                                                     
  * AMB-035;                                                                    
  * CA-045;                                                                     
  * casos de borda;                                                             
  * rastreabilidade;                                                            
  * D-003.                                                                      
                                                                                
  Não crie motivo novo para limite zero em `padrao`.                            
                                                                                
  ## 5. Nova AMB-037                                                            
                                                                                
  Crie:                                                                         
                                                                                
  ```text                                                                       
  AMB-037 — Motivo de limitação para categoria externa com periodicidade        
  individual                                                                    
  ```                                                                           
                                                                                
  Problema:                                                                     
                                                                                
  AMB-036 permite qualquer categoria com `periodicidade: "diaria"`, mas o único 
  motivo de teto individual existente é `TETO_HOSPEDAGEM_APLICADO`,             
  semanticamente específico de hospedagem.                                      
                                                                                
  Decisão:                                                                      
                                                                                
  Crie o motivo:                                                                
                                                                                
  ```text                                                                       
  TETO_INDIVIDUAL_APLICADO                                                      
  ```                                                                           
                                                                                
  Contrato:                                                                     
                                                                                
  ```text                                                                       
  codigo: TETO_INDIVIDUAL_APLICADO                                              
  regra: RN-019                                                                 
  campo: null                                                                   
  decisao: PARCIALMENTE_REEMBOLSADO                                             
  ```                                                                           
                                                                                
  Aplica-se quando:                                                             
                                                                                
  * categoria diferente de `hospedagem`;                                        
  * presente na tabela efetivamente aplicável;                                  
  * `limite > 0`;                                                               
  * `periodicidade: "diaria"`;                                                  
  * valor normalizado maior que o limite.                                       
                                                                                
  O item é reembolsado até o limite individual.                                 
                                                                                
  Preserve:                                                                     
                                                                                
  * `TETO_HOSPEDAGEM_APLICADO` somente para `hospedagem` com periodicidade      
  `"diaria"`;                                                                   
  * `TETO_DIARIO_APLICADO` para qualquer categoria com periodicidade `"dia"`;   
  * `TETO_DIARIO_ESGOTADO` para itens posteriores ao esgotamento de teto        
  `"dia"`.                                                                      
                                                                                
  ## 6. Novo CA-049                                                             
                                                                                
  Crie:                                                                         
                                                                                
  ```text                                                                       
  CA-049                                                                        
  ```                                                                           
                                                                                
  Cenário:                                                                      
                                                                                
  * política válida com categoria externa `estacionamento`;                     
  * `limite: 50.00`;                                                            
  * `periodicidade: "diaria"`;                                                  
  * despesa elegível de `80.00`;                                                
  * decisão `PARCIALMENTE_REEMBOLSADO`;                                         
  * `valor_reembolsavel: 50.00`;                                                
  * motivo `TETO_INDIVIDUAL_APLICADO`;                                          
  * regra `RN-019`;                                                             
  * `campo: null`.                                                              
                                                                                
  ## 7. Periodicidade deve determinar o mecanismo                               
                                                                                
  Atualize as regras para que o mecanismo seja determinado pela `periodicidade` 
  da política, e não apenas pelo nome histórico da categoria.                   
                                                                                
  ### RN-011                                                                    
                                                                                
  O comportamento histórico de alimentação diária aplica-se quando:             
                                                                                
  ```text                                                                       
  alimentacao.periodicidade = "dia"                                             
  ```                                                                           
                                                                                
  Se uma política válida declarar outra periodicidade, RN-019 determina o       
  mecanismo configurado.                                                        
                                                                                
  ### RN-012                                                                    
                                                                                
  Mesmo tratamento para `transporte_urbano`.                                    
                                                                                
  ### RN-013                                                                    
                                                                                
  `TETO_HOSPEDAGEM_APLICADO` aplica-se a `hospedagem` quando:                   
                                                                                
  ```text                                                                       
  hospedagem.periodicidade = "diaria"                                           
  ```                                                                           
                                                                                
  ### RN-014                                                                    
                                                                                
  Generalize reembolso parcial para:                                            
                                                                                
  * teto compartilhado de periodicidade `"dia"`;                                
  * teto individual de periodicidade `"diaria"`;                                
  * pagamento até o saldo ou limite disponível.                                 
                                                                                
  ### RN-015                                                                    
                                                                                
  Generalize:                                                                   
                                                                                
  * aplica-se a qualquer categoria com periodicidade `"dia"`;                   
  * não se aplica a categorias com periodicidade `"diaria"`;                    
  * não use “não se aplica a hospedagem” como regra universal.                  
                                                                                
  ## 8. Estados possíveis                                                       
                                                                                
  Substitua a tabela 8.5 baseada apenas nos nomes históricos por estados        
  baseados na tabela e periodicidade efetivamente aplicáveis:                   
                                                                                
  * categoria presente, limite positivo, periodicidade `"dia"`;                 
  * categoria presente, limite positivo, periodicidade `"diaria"`, sendo        
  `hospedagem`;                                                                 
  * categoria presente, limite positivo, periodicidade `"diaria"`, sendo outra  
  categoria;                                                                    
  * categoria ausente de `padrao`;                                              
  * categoria ausente da tabela de centro cadastrado;                           
  * categoria com limite zero em centro cadastrado.                             
                                                                                
  Inclua `TETO_INDIVIDUAL_APLICADO` no cenário correspondente.                  
                                                                                
  ## 9. Exemplos que dependem da tabela selecionada                             
                                                                                
  Na seção 8.4, qualquer exemplo que use `CATEGORIA_FORA_POLITICA` deve         
  declarar:                                                                     
                                                                                
  ```text                                                                       
  centro desconhecido ou ausente → tabela aplicável `padrao`                    
  ```                                                                           
                                                                                
  Não deixe um exemplo de `coworking` sem indicar a tabela aplicável.           
                                                                                
  `coworking` em `CC-COMERCIAL` continua produzindo:                            
                                                                                
  ```text                                                                       
  CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO                                       
  ```                                                                           
                                                                                
  ## 10. Rastreabilidade                                                        
                                                                                
  Atualize a tabela do comunicado v4:                                           
                                                                                
  * CA-048 deve aparecer associado a RN-002 e ao contrato de moeda;             
  * AMB-037 e CA-049 devem aparecer numa linha própria sobre limitação          
  individual de categoria externa;                                              
  * AMB-036 continua associado a CA-047;                                        
  * nenhuma CA pode ficar sem destino.                                          
                                                                                
  Atualize §12.5 para os novos intervalos.                                      
                                                                                
  ## 11. DECISIONS.md                                                           
                                                                                
  Não altere D-001 ou D-002.                                                    
                                                                                
  Dentro de D-003, adicione:                                                    
                                                                                
  ```text                                                                       
  ### Ajustes finais da segunda revisão independente                            
  ```                                                                           
                                                                                
  Registre:                                                                     
                                                                                
  1. os critérios históricos passaram a declarar a política externa usada;      
  2. CA-026 foi alinhado a AMB-021 e RN-019;                                    
  3. limite zero em `padrao` passou a invalidar o arquivo;                      
  4. AMB-037 resolveu categoria externa com periodicidade `"diaria"`;           
  5. periodicidade passou a governar o mecanismo independentemente do nome da   
  categoria;                                                                    
  6. foi criado `TETO_INDIVIDUAL_APLICADO`;                                     
  7. foi criado CA-049;                                                         
  8. os quatro totais permaneceram inalterados.                                 
                                                                                
  Corrija também a ocorrência antiga da linha 15:                               
                                                                                
  ```text                                                                       
  encontrou seis problemas de precisão de contrato                              
  ```                                                                           
                                                                                
  para:                                                                         
                                                                                
  ```text                                                                       
  encontrou oito problemas de precisão de contrato                              
  ```                                                                           
                                                                                
  ## 12. Contagens finais                                                       
                                                                                
  ```text                                                                       
  AMB-001 a AMB-037 = 37                                                        
  RN-001 a RN-022 = 22                                                          
  CA-001 a CA-049 = 49                                                          
  ```                                                                           
                                                                                
  Não crie nova RN.                                                             
                                                                                
  Mantenha:                                                                     
                                                                                
  ```text                                                                       
  versão 1.2                                                                    
  status em revisão                                                             
  ```                                                                           
                                                                                
  ## 13. Totais imutáveis                                                       
                                                                                
  ```text                                                                       
  baseline histórica = 585.43                                                   
  exemplo antigo sob v4 = 351.43                                                
  Rafael = 1143.26                                                              
  Dani = 373.76                                                                 
  ```                                                                           
                                                                                
  ## 14. Verificação                                                            
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * somente `spec.md` e `DECISIONS.md` estão modificados;                       
  * `exemplos/envelope/` permanece novo e intacto;                              
  * `plan.md`, `tasks.md`, README, código e testes continuam sem alterações;    
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Ao final, informe:                                                            
                                                                                
  * contagens finais;                                                           
  * conteúdo de AMB-037;                                                        
  * contrato de `TETO_INDIVIDUAL_APLICADO`;                                     
  * conteúdo de CA-049;                                                         
  * correção de CA-026;                                                         
  * tratamento de limite zero em `padrao`;                                      
  * critérios históricos qualificados;                                          
  * resultado de `git status --short`.                                          
                                                                                
  Pare para revisão.                                                            

  Read 2 files (ctrl+o to expand)
  ⎿  Invalid tool parameters

● I'll skip formal task tracking and proceed directly with the edits — this is a
  single focused editing pass I can track directly. Starting with §4.1.1.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      122  | `vigencia` | texto `AAAA-MM-DD` | Sim | Representa data real
            do calendário (RN-021) |
      123  | `moeda_base` | texto | Sim | Exatamente `"BRL"` |
      124  | `padrao` | objeto | Sim | Tabela de limites por categoria, a
           plicável a qualquer centro de custo sem entrada própria em `ce
           ntros_custo`. Pode ser vazio |
      125 -| `centros_custo` | objeto | Sim | Mapa de centro de custo par
          -a a respectiva tabela de limites por categoria — substitui int
          -egralmente `padrao` para as categorias que declarar (RN-019). 
          -Pode ser vazio |                                              
      125 +| `centros_custo` | objeto | Sim | Mapa de centro de custo par
          +a a respectiva tabela de limites por categoria. Cada centro ca
          +dastrado possui uma tabela completa e exclusiva para aquele ce
          +ntro; quando selecionada, ela substitui integralmente `padrao`
          + (RN-019), e categorias que não estiverem declaradas nela não 
          +recebem fallback. Pode ser vazio |                            
      126  | `nota_fiscal_obrigatoria_acima_de` | número | Sim | Não nega
           tivo |
      127  | `versao`, `acrescimo_em_viagem_percentual`, outros | qualque
           r | Não | Metadados aceitos e ignorados pelas regras atuais. `
           acrescimo_em_viagem_percentual` **não** ativa RN-016 |
      128  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      130  
      131  | Campo | Tipo | Obrigatório | Restrição |
      132  |---|---|---|---|
      133 -| `limite` | número | Sim | Maior ou igual a zero |           
      133 +| `limite` | número | Sim | Maior ou igual a zero dentro de um
          +a tabela de `centros_custo`; **estritamente maior que zero** d
          +entro de `padrao` (AMB-035, correção da segunda revisão) — `li
          +mite: 0` em `padrao` torna o arquivo de política estruturalmen
          +te inválido (RN-022), não uma decisão de política sobre um ite
          +m |                                                           
      134  | `periodicidade` | texto | Sim | Exatamente `"dia"` ou `"diar
           ia"` (RN-019, AMB-036) |
      135  | `observacao` | texto | Não | Informativa; nunca lida por reg
           ra alguma |
      136  | outros | qualquer | Não | Ignorados |

● Now RN-009:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      477  
      478  ### RN-009 — Nota fiscal obrigatória
      479  
      480 -**Regra:** quando o valor normalizado do item — já convertido 
          -para BRL quando `despesa.moeda` for estrangeira (RN-020) — for
          - **estritamente maior** que o gatilho da política (`nota_fisca
          -l_obrigatoria_acima_de`, R$ 100,00) e `tem_nota_fiscal` for fa
          -lso, o item é recusado com `NOTA_FISCAL_AUSENTE`. A comparação
          - usa o valor individual normalizado e convertido, antes de qua
          -lquer corte por teto — nunca o valor original na moeda da desp
          -esa.                                                          
      480 +**Regra:** quando o valor normalizado do item — já convertido 
          +para BRL quando `despesa.moeda` for estrangeira (RN-020) — for
          + **estritamente maior** que o gatilho configurado na política 
          +(`nota_fiscal_obrigatoria_acima_de`, dado do arquivo de políti
          +ca, não uma constante interna do sistema — R$ 100,00 é o valor
          + da política padrão/histórica usada nos exemplos desta spec) e
          + `tem_nota_fiscal` for falso, o item é recusado com `NOTA_FISC
          +AL_AUSENTE`. A comparação usa o valor individual normalizado e
          + convertido, antes de qualquer corte por teto — nunca o valor 
          +original na moeda da despesa.                                 
      481  **Origem:** política do RH, item 5; comparação pelo valor conv
           ertido acrescentada pela política v4 (Dia 2, RN-020).
      482  **Aceite:** `100,00` sem nota é elegível; `100,01` sem nota é 
           recusado; `690,00` sem nota é recusado sem que qualquer teto s
           eja calculado. USD `40,00` convertido pela taxa `5,50` resulta
            em `220,00`; sem nota fiscal, o item é recusado com `NOTA_FIS
           CAL_AUSENTE`, mesmo que o valor original (`40,00`) esteja abai
           xo do gatilho.
      483  

● Now RN-011, RN-012, RN-013:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      489  
      490  ### RN-011 — Limite diário de alimentação
      491  
      492 -**Regra:** o limite de `alimentacao` definido pela política ap
          -licável ao centro de custo do colaborador (RN-019) — R$ 60,00 
          -por data na política padrão — aplicado ao conjunto dos itens e
          -legíveis de `alimentacao` daquela data.                       
      493 -**Origem:** política do RH, item 1; valor parametrizado por ce
          -ntro de custo pela política v4 (Dia 2, RN-019).               
      492 +**Regra:** o comportamento histórico "por dia" aplica-se quand
          +o a tabela de política efetivamente aplicável declara `aliment
          +acao.periodicidade = "dia"` — R$ 60,00 por data na política pa
          +drão. O limite em si é definido pela política aplicável ao cen
          +tro de custo do colaborador (RN-019), aplicado ao conjunto dos
          + itens elegíveis de `alimentacao` daquela data. Se uma polític
          +a válida declarar outra `periodicidade` para `alimentacao`, é 
          +RN-019 — não este número fixo — que determina o mecanismo conf
          +igurado.                                                      
      493 +**Origem:** política do RH, item 1; valor e mecanismo parametr
          +izados por centro de custo pela política v4 (Dia 2, RN-019, AM
          +B-036).                                                       
      494  **Aceite:** sob a política padrão, R$ 72,50 e R$ 38,00 na mesm
           a data rendem R$ 60,00 no total daquela data.
      495  
      496  ### RN-012 — Limite diário de transporte urbano

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      495  
      496  ### RN-012 — Limite diário de transporte urbano
      497  
      498 -**Regra:** o limite de `transporte_urbano` definido pela polít
          -ica aplicável ao centro de custo do colaborador (RN-019) — R$ 
          -80,00 por data na política padrão — aplicado ao conjunto dos i
          -tens elegíveis de `transporte_urbano` daquela data.           
      499 -**Origem:** política do RH, item 2; valor parametrizado por ce
          -ntro de custo pela política v4 (Dia 2, RN-019).               
      498 +**Regra:** o comportamento histórico "por dia" aplica-se quand
          +o a tabela de política efetivamente aplicável declara `transpo
          +rte_urbano.periodicidade = "dia"` — R$ 80,00 por data na polít
          +ica padrão. O limite em si é definido pela política aplicável 
          +ao centro de custo do colaborador (RN-019), aplicado ao conjun
          +to dos itens elegíveis de `transporte_urbano` daquela data. Se
          + uma política válida declarar outra `periodicidade` para `tran
          +sporte_urbano`, é RN-019 — não este número fixo — que determin
          +a o mecanismo configurado.                                    
      499 +**Origem:** política do RH, item 2; valor e mecanismo parametr
          +izados por centro de custo pela política v4 (Dia 2, RN-019, AM
          +B-036).                                                       
      500  **Aceite:** sob a política padrão, item elegível de R$ 100,00,
            sozinho na data, rende R$ 80,00.
      501  
      502  ### RN-013 — Limite individual de hospedagem
● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      501  
      502  ### RN-013 — Limite individual de hospedagem
      503  
      504 -**Regra:** o limite de `hospedagem` definido pela política apl
          -icável ao centro de custo do colaborador (RN-019) — R$ 250,00 
          -por lançamento na política padrão —, aplicável somente quando 
          -esse limite for maior que zero (limite igual a `0,00` é RN-019
          -, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não este teto). H
          -ospedagem **não** participa da agregação por data: cada lançam
          -ento é avaliado isoladamente. O conteúdo da descrição não alte
          -ra o teto.                                                    
      505 -**Origem:** política do RH, item 3; valor parametrizado por ce
          -ntro de custo pela política v4 (Dia 2, RN-019).               
      504 +**Regra:** `TETO_HOSPEDAGEM_APLICADO` aplica-se a `hospedagem`
          + quando a tabela de política efetivamente aplicável declara `h
          +ospedagem.periodicidade = "diaria"` — o caso normal, inclusive
          + na política padrão. O limite em si é definido pela política a
          +plicável ao centro de custo do colaborador (RN-019) — R$ 250,0
          +0 por lançamento na política padrão —, aplicável somente quand
          +o esse limite for maior que zero (limite igual a `0,00` é RN-0
          +19, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não este teto).
          + Hospedagem **não** participa da agregação por data: cada lanç
          +amento é avaliado isoladamente. O conteúdo da descrição não al
          +tera o teto.                                                  
      505 +**Origem:** política do RH, item 3; valor e mecanismo parametr
          +izados por centro de custo pela política v4 (Dia 2, RN-019, AM
          +B-036).                                                       
      506  **Aceite:** sob a política padrão, lançamento de R$ 480,00 des
           crito como "2 diarias" rende R$ 250,00. Alterar o texto da `de
           scricao` desse lançamento não altera o teto individual aplicad
           o, que permanece R$ 250,00. Dois lançamentos de hospedagem na 
           mesma data podem render até R$ 500,00 sob a política padrão.
      507  
      508  ### RN-014 — Reembolso parcial

● Now RN-014 and RN-015:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      507  
      508  ### RN-014 — Reembolso parcial
      509  
      510 -**Regra:** ultrapassado o teto, reembolsa-se até o teto e o ex
          -cedente não é reembolsado. O agregado diário nunca é recusado 
          -integralmente por ultrapassagem, nem o lançamento de hospedage
          -m.                                                            
      510 +**Regra:** ultrapassado o teto, reembolsa-se até o teto e o ex
          +cedente não é reembolsado. Vale tanto para o teto compartilhad
          +o de categorias com `periodicidade: "dia"` quanto para o teto 
          +individual de categorias com `periodicidade: "diaria"` (RN-019
          +) — em ambos os casos, paga-se até o saldo ou limite disponíve
          +l, nunca zero por ultrapassagem. O agregado diário nunca é rec
          +usado integralmente por ultrapassagem, nem o lançamento de tet
          +o individual.                                                 
      511  **Origem:** política do RH, item 4.
      512 -**Aceite:** R$ 61,00 de alimentação, sozinho na data, rende R$
          - 60,00 — não R$ 0,00.                                         
      512 +**Aceite:** sob a política padrão, R$ 61,00 de alimentação, so
          +zinho na data, rende R$ 60,00 — não R$ 0,00.                  
      513  
      514  ### RN-015 — Distribuição do teto diário
      515  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      513  
      514  ### RN-015 — Distribuição do teto diário
      515  
      516 -**Regra:** dentro de uma data e categoria com teto diário, os 
          -itens elegíveis consomem o saldo em ordem crescente de `indice
          -_entrada`. Cada item é pago integralmente enquanto houver sald
          -o; o item que ultrapassa o saldo é pago parcialmente; os poste
          -riores recebem `NAO_REEMBOLSADO_TETO_ESGOTADO`. Não se aplica 
          -a hospedagem, que não possui saldo compartilhado.             
      516 +**Regra:** aplica-se a qualquer categoria cuja `periodicidade`
          +, na tabela de política efetivamente aplicável, seja `"dia"` (
          +RN-019) — não a um conjunto fixo de nomes de categoria. Dentro
          + de uma data e categoria com esse teto compartilhado, os itens
          + elegíveis consomem o saldo em ordem crescente de `indice_entr
          +ada`. Cada item é pago integralmente enquanto houver saldo; o 
          +item que ultrapassa o saldo é pago parcialmente; os posteriore
          +s recebem `NAO_REEMBOLSADO_TETO_ESGOTADO`. Não se aplica a cat
          +egorias com `periodicidade: "diaria"`, que não possuem saldo c
          +ompartilhado — `hospedagem` é um exemplo desse caso, não a reg
          +ra em si.                                                     
      517  **Origem:** política do RH, itens 1 e 2 (unidade "por dia").
      518 -**Aceite:** com itens de R$ 72,50 e R$ 38,00 nessa ordem, o pr
          -imeiro rende R$ 60,00 e o segundo R$ 0,00 com decisão `NAO_REE
          -MBOLSADO_TETO_ESGOTADO`.                                      
      518 +**Aceite:** sob a política padrão, com itens de R$ 72,50 e R$ 
          +38,00 nessa ordem, o primeiro rende R$ 60,00 e o segundo R$ 0,
          +00 com decisão `NAO_REEMBOLSADO_TETO_ESGOTADO`.               
      519  
      520  ### RN-016 — Ampliação por viagem (efeito nulo nesta versão)
      521  

● Now RN-019 — the limite-zero split and TETO_INDIVIDUAL_APLICADO mechanism:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
      542  Dentro da tabela efetivamente aplicável, cada categoria declar
           ada tem um `limite` e uma `periodicidade` (contrato estrutural
            em 4.1.1, AMB-035):
      543  
      544  - `periodicidade: "dia"` — teto **compartilhado** entre os ite
           ns elegíveis da mesma categoria e data, consumido em ordem cre
           scente de `indice_entrada` (mesmo mecanismo de RN-011/RN-012/R
           N-015). É o caso de `alimentacao`, `transporte_urbano` e, quan
           do declarada, `representacao`.
      545 -- `periodicidade: "diaria"` — teto **individual** por lançamen
          -to, sem saldo compartilhado (mesmo mecanismo de RN-013). É o c
          -aso de `hospedagem`.                                          
      545 +- `periodicidade: "diaria"` — teto **individual** por lançamen
          +to, sem saldo compartilhado (mesmo mecanismo de RN-013). `hosp
          +edagem` sob esse mecanismo produz motivo `TETO_HOSPEDAGEM_APLI
          +CADO`; qualquer outra categoria sob `periodicidade: "diaria"` 
          +(ex.: `estacionamento`) usa o mesmo mecanismo individual, mas 
          +produz motivo `TETO_INDIVIDUAL_APLICADO` (AMB-037) — `TETO_HOS
          +PEDAGEM_APLICADO` é exclusivo de `hospedagem`.                
      546  - Categoria com `periodicidade` diferente de `"dia"` e `"diari
           a"` torna o **arquivo de política** inválido (AMB-035, RN-022)
            — não é um defeito do item, é um defeito do arquivo externo.
      547 +- Dentro de `padrao`, todo `limite` declarado é estritamente m
          +aior que zero (AMB-035, correção da segunda revisão): `limite:
          + 0` em `padrao` não é uma decisão de política sobre uma catego
          +ria — é defeito estrutural do **arquivo** de política (RN-022)
          +, porque `padrao` é o fallback universal, e um teto zero ali r
          +etiraria a categoria de todo centro de custo desconhecido sem 
          +que o comunicado expresse essa decisão dessa forma. Dentro de 
          +uma tabela de `centros_custo`, `limite: 0` continua sendo deci
          +são válida do próprio centro (`CATEGORIA_NAO_REEMBOLSAVEL_CENT
          +RO_CUSTO`, abaixo) — não um defeito. Nenhum motivo de item nov
          +o é criado para o caso de `padrao`: a falha é do arquivo intei
          +ro, código de saída `2` (RN-022, AMB-034), antes de qualquer i
          +tem ser avaliado.                                             
      548  
      549  Duas classificações de recusa, sempre com `valor_reembolsavel`
            `0,00` e nunca como reembolso parcial:
      550  

● Now RN-022:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      569  
      570  ### RN-022 — Processabilidade dos arquivos externos de polític
           a e câmbio
      571  
      572 -**Regra:** a apuração só ocorre quando os arquivos de política
          - e de câmbio são, cada um, legíveis e sintaticamente válidos c
          -omo JSON, e estruturalmente válidos conforme o contrato fechad
          -o de 4.1.1 (AMB-035) — que define exaustivamente o que "estrut
          -uralmente inválido" significa para cada arquivo: tipo de cada 
          -campo, obrigatoriedade, e as restrições de `limite`/`periodici
          -dade` das tabelas de categoria (política) e de `taxas` (câmbio
          -). Falha em qualquer um dos dois arquivos — ausência, ilegibil
          -idade, JSON sintaticamente inválido, ou qualquer violação dess
          -e contrato — impede toda a apuração do arquivo de despesas, me
          -smo que este esteja perfeitamente válido; nenhum resultado par
          -cial é produzido. É erro global (código de saída `2`, AMB-034)
          -, na mesma classe de gravidade de um arquivo de entrada ilegív
          -el — mais grave que um envelope de despesas inválido (RN-001, 
          -código `3`), que ainda pressupõe política e câmbio utilizáveis
          -. `taxas` vazio em `cambio.json` **não** é violação estrutural
          - — é um arquivo de câmbio válido que simplesmente não resolve 
          -nenhuma moeda estrangeira, recusando essas despesas individual
          -mente com `MOEDA_SEM_COTACAO` (RN-020).                       
      572 +**Regra:** a apuração só ocorre quando os arquivos de política
          + e de câmbio são, cada um, legíveis e sintaticamente válidos c
          +omo JSON, e estruturalmente válidos conforme o contrato fechad
          +o de 4.1.1 (AMB-035) — que define exaustivamente o que "estrut
          +uralmente inválido" significa para cada arquivo: tipo de cada 
          +campo, obrigatoriedade, e as restrições de `limite`/`periodici
          +dade` das tabelas de categoria (política) e de `taxas` (câmbio
          +). Em particular, `limite` igual a `0,00` dentro de `padrao` é
          + violação estrutural do arquivo de política (correção da segun
          +da revisão) — não uma recusa de item; o mesmo valor dentro de 
          +uma tabela de `centros_custo` é estruturalmente válido e produ
          +z `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` por item (RN-019).
          + Falha em qualquer um dos dois arquivos — ausência, ilegibilid
          +ade, JSON sintaticamente inválido, ou qualquer violação desse 
          +contrato — impede toda a apuração do arquivo de despesas, mesm
          +o que este esteja perfeitamente válido; nenhum resultado parci
          +al é produzido. É erro global (código de saída `2`, AMB-034), 
          +na mesma classe de gravidade de um arquivo de entrada ilegível
          + — mais grave que um envelope de despesas inválido (RN-001, có
          +digo `3`), que ainda pressupõe política e câmbio utilizáveis. 
          +`taxas` vazio em `cambio.json` **não** é violação estrutural —
          + é um arquivo de câmbio válido que simplesmente não resolve ne
          +nhuma moeda estrangeira, recusando essas despesas individualme
          +nte com `MOEDA_SEM_COTACAO` (RN-020).                         
      573  **Origem:** contrato de entrada (lacuna do comunicado, decorre
           nte da introdução dos arquivos externos — política v4, Dia 2).
      574  **Aceite:** arquivo de câmbio com JSON sintaticamente inválido
            não produz apuração alguma, mesmo que o arquivo de despesas e
            o arquivo de política sejam válidos. Arquivo de política sem 
           o bloco `padrao`, ou com `moeda_base` diferente de `"BRL"`, ou
            com uma tabela de categoria cuja `periodicidade` não seja `"d
           ia"` nem `"diaria"`, não produz apuração alguma. Arquivo de câ
           mbio com `taxas: {}` produz apuração normal do arquivo de desp
           esas, com toda despesa em moeda estrangeira recusada individua
           lmente.
      575  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      571  
      572  **Regra:** a apuração só ocorre quando os arquivos de política
            e de câmbio são, cada um, legíveis e sintaticamente válidos c
           omo JSON, e estruturalmente válidos conforme o contrato fechad
           o de 4.1.1 (AMB-035) — que define exaustivamente o que "estrut
           uralmente inválido" significa para cada arquivo: tipo de cada 
           campo, obrigatoriedade, e as restrições de `limite`/`periodici
           dade` das tabelas de categoria (política) e de `taxas` (câmbio
           ). Em particular, `limite` igual a `0,00` dentro de `padrao` é
            violação estrutural do arquivo de política (correção da segun
           da revisão) — não uma recusa de item; o mesmo valor dentro de 
           uma tabela de `centros_custo` é estruturalmente válido e produ
           z `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` por item (RN-019).
            Falha em qualquer um dos dois arquivos — ausência, ilegibilid
           ade, JSON sintaticamente inválido, ou qualquer violação desse 
           contrato — impede toda a apuração do arquivo de despesas, mesm
           o que este esteja perfeitamente válido; nenhum resultado parci
           al é produzido. É erro global (código de saída `2`, AMB-034), 
           na mesma classe de gravidade de um arquivo de entrada ilegível
            — mais grave que um envelope de despesas inválido (RN-001, có
           digo `3`), que ainda pressupõe política e câmbio utilizáveis. 
           `taxas` vazio em `cambio.json` **não** é violação estrutural —
            é um arquivo de câmbio válido que simplesmente não resolve ne
           nhuma moeda estrangeira, recusando essas despesas individualme
           nte com `MOEDA_SEM_COTACAO` (RN-020).
      573  **Origem:** contrato de entrada (lacuna do comunicado, decorre
           nte da introdução dos arquivos externos — política v4, Dia 2).
      574 -**Aceite:** arquivo de câmbio com JSON sintaticamente inválido
          - não produz apuração alguma, mesmo que o arquivo de despesas e
          - o arquivo de política sejam válidos. Arquivo de política sem 
          -o bloco `padrao`, ou com `moeda_base` diferente de `"BRL"`, ou
          - com uma tabela de categoria cuja `periodicidade` não seja `"d
          -ia"` nem `"diaria"`, não produz apuração alguma. Arquivo de câ
          -mbio com `taxas: {}` produz apuração normal do arquivo de desp
          -esas, com toda despesa em moeda estrangeira recusada individua
          -lmente.                                                       
      574 +**Aceite:** arquivo de câmbio com JSON sintaticamente inválido
          + não produz apuração alguma, mesmo que o arquivo de despesas e
          + o arquivo de política sejam válidos. Arquivo de política sem 
          +o bloco `padrao`, com `moeda_base` diferente de `"BRL"`, com u
          +ma tabela de categoria cuja `periodicidade` não seja `"dia"` n
          +em `"diaria"`, ou com qualquer categoria de `padrao` tendo `li
          +mite` igual a `0,00`, não produz apuração alguma. Arquivo de c
          +âmbio com `taxas: {}` produz apuração normal do arquivo de des
          +pesas, com toda despesa em moeda estrangeira recusada individu
          +almente.                                                      
      575  
      576  ---
      577  

● Now §6 header counts and AMB-035 limite split:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      580  > Esta seção é o coração da spec. Uma ambiguidade resolvida no
            código sem registro
      581  > aqui conta como não resolvida.
      582  
      583 -Dezoito ambiguidades da linha de base (`AMB-001` a `AMB-018`) 
          -e as subdecisões que cada uma gerou, mais dezoito ambiguidades
          - novas do envelope do Dia 2, política v4 (`AMB-019` a `AMB-036
          -`) — trinta e seis no total. `AMB-034` a `AMB-036` foram acres
          -centadas numa correção após revisão independente da primeira l
          -eitura do envelope, para fechar o contrato de execução e os co
          -ntratos estruturais dos arquivos externos. Classificação: **U*
          -* unidade de aplicação · **F** fronteira · **D** dado ausente 
          -· **O** outra.                                                
      583 +Dezoito ambiguidades da linha de base (`AMB-001` a `AMB-018`) 
          +e as subdecisões que cada uma gerou, mais dezenove ambiguidade
          +s novas do envelope do Dia 2, política v4 (`AMB-019` a `AMB-03
          +7`) — trinta e sete no total. `AMB-034` a `AMB-036` foram acre
          +scentadas numa correção após revisão independente da primeira 
          +leitura do envelope, para fechar o contrato de execução e os c
          +ontratos estruturais dos arquivos externos; `AMB-037` foi acre
          +scentada numa segunda revisão independente, para fechar o voca
          +bulário de motivos de teto individual. Classificação: **U** un
          +idade de aplicação · **F** fronteira · **D** dado ausente · **
          +O** outra.                                                    
      584  
      585  ### AMB-001 — Unidade do limite diário · U
      586  

● Now update AMB-035's decision text for the limite split:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      873  
      874  **Texto original do RH:** nenhum — os arquivos `politica-v4.js
           on` e `cambio.json` do envelope são exemplos concretos, não um
           a descrição de esquema.
      875  **O que não está claro:** RN-022 (na forma anterior a esta cor
           reção) dizia "estruturalmente inválido" sem definir precisamen
           te o que isso significa para cada arquivo — sem um contrato fe
           chado, a expressão não é verificável sem ler o código, contrar
           iando o próprio critério de aceite da spec ("verificável sem l
           eitura de código").
      876 -**Decisão:** contrato fechado, documentado por completo em 4.1
          -.1: raiz de ambos os arquivos deve ser objeto; política exige 
          -`vigencia`, `moeda_base` (exatamente `"BRL"`), `padrao` (objet
          -o, pode ser vazio), `centros_custo` (objeto, pode ser vazio) e
          - `nota_fiscal_obrigatoria_acima_de` (número não negativo), com
          - `versao` e `acrescimo_em_viagem_percentual` aceitos como meta
          -dados ignorados (o segundo **não** ativa RN-016); cada tabela 
          -de categoria exige `limite` (número ≥ 0) e `periodicidade` (ex
          -atamente `"dia"` ou `"diaria"` — AMB-036); câmbio exige `moeda
          -_base` (exatamente `"BRL"`) e `taxas` (objeto, podendo ser vaz
          -io, aninhando data → moeda → taxa estritamente positiva), com 
          -`fonte` e `observacao` como metadados informativos.           
      876 +**Decisão:** contrato fechado, documentado por completo em 4.1
          +.1: raiz de ambos os arquivos deve ser objeto; política exige 
          +`vigencia`, `moeda_base` (exatamente `"BRL"`), `padrao` (objet
          +o, pode ser vazio), `centros_custo` (objeto, pode ser vazio) e
          + `nota_fiscal_obrigatoria_acima_de` (número não negativo), com
          + `versao` e `acrescimo_em_viagem_percentual` aceitos como meta
          +dados ignorados (o segundo **não** ativa RN-016); cada tabela 
          +de categoria exige `limite` (número ≥ 0 dentro de `centros_cus
          +to`; estritamente > 0 dentro de `padrao` — correção da segunda
          + revisão, ver ao final de `DECISIONS.md` `D-003`) e `periodici
          +dade` (exatamente `"dia"` ou `"diaria"` — AMB-036); câmbio exi
          +ge `moeda_base` (exatamente `"BRL"`) e `taxas` (objeto, podend
          +o ser vazio, aninhando data → moeda → taxa estritamente positi
          +va), com `fonte` e `observacao` como metadados informativos.  
      877  **Justificativa:** a correção também revela que a raiz de `cam
           bio.json` **não** é um mapa direto de data para moeda, como um
           a versão anterior desta spec presumiu por engano — as cotações
            estão aninhadas sob a chave `taxas`, junto de `moeda_base`, `
           fonte` e `observacao`. Um contrato fechado e correto evita ess
           a classe de erro se repetir na implementação, e torna "estrutu
           ralmente inválido" checável item a item, sem ambiguidade.
      878  **Regra afetada:** RN-019, RN-020, RN-021, RN-022 (esclarecido
           s por este contrato — nenhuma decisão de negócio nova além do 
           que já estava implícito)
      879  

● Now insert the new AMB-037 section, right after AMB-036 and before the ---
  separator:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 8 lines
      885  **Justificativa:** os dois valores já correspondem exatamente 
           aos dois mecanismos de teto que a spec 1.1 já implementava par
           a alimentação/transporte (compartilhado) e hospedagem (individ
           ual) — a política externa só nomeia explicitamente o que já er
           a a regra. Tratar um valor de `periodicidade` fora do vocabulá
           rio fechado como erro de arquivo (não de item) é consistente c
           om RN-022: é a política que está malformada, não a despesa.
      886  **Regra afetada:** RN-019
      887  
      888 +### AMB-037 — Motivo de limitação para categoria externa com p
          +eriodicidade individual · U                                   
      889 +                                                              
      890 +**Texto original do RH:** nenhum — decorre de `AMB-036` permit
          +ir qualquer categoria externa sob `periodicidade: "diaria"`, e
          +nquanto o vocabulário de motivos herdado da linha de base só p
          +revia `TETO_HOSPEDAGEM_APLICADO`, semanticamente específico de
          + hospedagem.                                                  
      891 +**O que não está claro:** com a política externa podendo decla
          +rar qualquer categoria — não apenas `hospedagem` — sob `period
          +icidade: "diaria"` (ex.: uma categoria `estacionamento`), o mo
          +tivo de teto individual carecia de nome genérico: usar `TETO_H
          +OSPEDAGEM_APLICADO` fora de `hospedagem` produziria saída audi
          +tável, porém semanticamente incorreta — quem lê o motivo presu
          +miria hospedagem mesmo quando a categoria é outra.            
      892 +**Decisão:** cria-se o motivo `TETO_INDIVIDUAL_APLICADO`, com 
          +contrato `codigo: TETO_INDIVIDUAL_APLICADO`, `regra: RN-019`, 
          +`campo: null`, aparecendo em itens `PARCIALMENTE_REEMBOLSADO`.
          + Aplica-se quando, simultaneamente: a categoria é diferente de
          + `hospedagem`; está presente na tabela de política efetivament
          +e aplicável; essa tabela declara `limite > 0` para a categoria
          +; a `periodicidade` declarada é `"diaria"`; e o valor normaliz
          +ado excede esse limite. O item é reembolsado até o limite indi
          +vidual. Preservados sem alteração: `TETO_HOSPEDAGEM_APLICADO` 
          +continua exclusivo de `hospedagem` sob `periodicidade: "diaria
          +"`; `TETO_DIARIO_APLICADO` continua o motivo de qualquer categ
          +oria sob `periodicidade: "dia"`; `TETO_DIARIO_ESGOTADO` contin
          +ua o motivo dos itens posteriores ao esgotamento de um teto `"
          +dia"`.                                                        
      893 +**Justificativa:** um motivo genérico fecha o vocabulário de t
          +eto individual sem exigir um código novo a cada categoria futu
          +ra que a política venha a declarar sob `periodicidade: "diaria
          +"`, e evita que a saída sugira hospedagem onde não há.        
      894 +**Regra afetada:** RN-019                                     
      895 +                                                              
      896  ---
      897  
      898  ## 7. Casos de borda

● Now §7 casos de borda — prefatory note plus new rows:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines
      897  
      898  ## 7. Casos de borda
      899  
      900 +Salvo indicação em contrário, os valores `60`, `80`, `250` e `
          +100` usados nesta tabela são os da política externa de teste e
          +quivalente à política histórica/padrão (`alimentacao` R$60,00/
          +dia, `transporte_urbano` R$80,00/dia, `hospedagem` R$250,00/di
          +ária, `nota_fiscal_obrigatoria_acima_de` R$100,00 — CA-037): s
          +ão dados de uma política, não constantes internas do sistema, 
          +e um centro de custo cadastrado pode declarar valores diferent
          +es (RN-019).                                                  
      901 +                                                              
      902  | Caso | Entrada | Comportamento esperado | Regra |
      903  |---|---|---|---|
      904  | Valor igual ao gatilho de nota | `100.00`, sem nota | Elegív
           el; nota não exigida | RN-009 |

● Now add new casos de borda rows (limite zero em padrão,
  TETO_INDIVIDUAL_APLICADO) — inserting near the existing política-related rows:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines
      944  | Categoria fora de qualquer tabela, centro de custo desconhec
           ido | `representacao` para colaborador de centro de custo desc
           onhecido (tabela aplicável `padrao`) | Recusado, `CATEGORIA_FO
           RA_POLITICA` | RN-007, RN-019 |
      945  | Categoria ausente da tabela de um centro cadastrado, mas ine
           xistente em toda a política | `coworking` para colaborador de 
           `CC-COMERCIAL` (cadastrado; `coworking` não existe em nenhuma 
           tabela) | Recusado, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` 
           — não `CATEGORIA_FORA_POLITICA`, porque a tabela aplicável é a
            de `CC-COMERCIAL` | RN-007, RN-019 |
      946  | Categoria de periodicidade `"dia"` introduzida pela política
            externa | Dois itens de `representacao` (`CC-COMERCIAL`) na m
           esma data | Saldo compartilhado de R$300,00, mesmo mecanismo d
           e RN-011/RN-012/RN-015 | RN-019 |
      947 +| Categoria externa não-hospedagem com periodicidade `"diaria"
          +` acima do limite | `estacionamento`, limite R$50,00, despesa 
          +elegível de R$80,00 | Parcial, `50,00`, motivo `TETO_INDIVIDUA
          +L_APLICADO` — não `TETO_HOSPEDAGEM_APLICADO` | RN-019 |       
      948 +| Limite de categoria igual a `0,00` em `padrao` | `padrao.<ca
          +tegoria>.limite = 0` | Arquivo de política estruturalmente inv
          +álido; nenhuma apuração, código de saída `2` | RN-019, RN-022 
          +|                                                             
      949  | Data da despesa sem cotação exata | EUR num sábado sem cotaç
           ão própria | Usa a cotação do último dia útil anterior | RN-02
           0 |
      950  | Moeda sem qualquer cotação em `cambio.json` | GBP | Recusado
           , `MOEDA_SEM_COTACAO` | RN-020 |
      951  | Moeda estrangeira acima do gatilho de nota, sem nota | USD `
           40,00` convertido a `220,00`, sem nota | Recusado, `NOTA_FISCA
           L_AUSENTE` — compara o valor convertido | RN-009, RN-020 |

● Now §4.5 motivos de limitação table — add TETO_INDIVIDUAL_APLICADO:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line
      358  | `TETO_DIARIO_APLICADO` | RN-011 (alimentação), RN-012 (trans
           porte) ou RN-019 (categoria de periodicidade `"dia"` introduzi
           da exclusivamente pela política externa, ex.: `representacao`)
            | nulo | `PARCIALMENTE_REEMBOLSADO` |
      359  | `TETO_DIARIO_ESGOTADO` | RN-015 | nulo | `NAO_REEMBOLSADO_TE
           TO_ESGOTADO` |
      360  | `TETO_HOSPEDAGEM_APLICADO` | RN-013 | nulo | `PARCIALMENTE_R
           EEMBOLSADO` de hospedagem |
      361 +| `TETO_INDIVIDUAL_APLICADO` | RN-019 (categoria de periodicid
          +ade `"diaria"` diferente de `hospedagem`, ex.: `estacionamento
          +` — AMB-037) | nulo | `PARCIALMENTE_REEMBOLSADO` |            
      362  
      363  Item `INTEGRALMENTE_REEMBOLSADO` tem lista de motivos vazia.
      364  

● Now §8.3 ordering list:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1015  9. `NOTA_FISCAL_AUSENTE`
      1016  10. `DUPLICIDADE`
      1017  
      1018 -Os motivos de limitação de teto (`TETO_DIARIO_APLICADO`, `TET
           -O_DIARIO_ESGOTADO`, `TETO_HOSPEDAGEM_APLICADO`) vêm depois de
           - todos os anteriores, quando aplicáveis — na prática, aparece
           -m sozinhos, porque um item que chega à etapa de teto já não c
           -arrega nenhum motivo de recusa.                              
      1018 +Os motivos de limitação de teto (`TETO_DIARIO_APLICADO`, `TET
           +O_DIARIO_ESGOTADO`, `TETO_HOSPEDAGEM_APLICADO`, `TETO_INDIVID
           +UAL_APLICADO`) vêm depois de todos os anteriores, quando apli
           +cáveis — na prática, aparecem sozinhos, porque um item que ch
           +ega à etapa de teto já não carrega nenhum motivo de recusa.  
      1019  
      1020  ### 8.4 Lista fechada de exclusões de aplicabilidade
      1021  

● Now §8.4 coworking examples:

● Update(specs\001-motor-reembolso\spec.md)  ⎿  Added 2 lines, removed 2 lines
      1038  15. Limite de política igual a `0,00` nunca produz `PARCIALME
            NTE_REEMBOLSADO`: o item é recusado com `CATEGORIA_NAO_REEMBO
            LSAVEL_CENTRO_CUSTO` antes de qualquer teto ser avaliado — nã
            o é uma variante de RN-013/RN-014 com teto zero (política v4,
             Dia 2).
      1039  16. Arquivo de política ou de câmbio estruturalmente inválido
             impede toda a apuração, inclusive de despesas em BRL que não
             dependeriam de câmbio algum — o erro é do arquivo externo, n
            ão do item (política v4, Dia 2, RN-022).
      1040  
      1041 -**Exemplo de motivos múltiplos:** item de R$ 500,00, categori
           -a `coworking`, sem nota, datado fora da janela, com todos os 
           -campos estruturalmente válidos, apresenta três motivos — `CAT
           -EGORIA_FORA_POLITICA`, `FORA_COMPETENCIA` e `NOTA_FISCAL_AUSE
           -NTE`.                                                        
      1041 +**Exemplo de motivos múltiplos:** item de R$ 500,00, categori
           +a `coworking`, sem nota, datado fora da janela, de um colabor
           +ador de centro de custo desconhecido ou ausente (tabela aplic
           +ável `padrao`, que não declara `coworking`), com todos os cam
           +pos estruturalmente válidos, apresenta três motivos — `CATEGO
           +RIA_FORA_POLITICA`, `FORA_COMPETENCIA` e `NOTA_FISCAL_AUSENTE
           +`.                                                           
      1042  
      1043 -**Exemplo de exclusão:** item de −R$ 500,00, categoria `cowor
           -king`, sem nota, apresenta `VALOR_NAO_POSITIVO` e `CATEGORIA_
           -FORA_POLITICA`, mas **não** apresenta `NOTA_FISCAL_AUSENTE`. 
      1043 +**Exemplo de exclusão:** item de −R$ 500,00, categoria `cowor
           +king`, sem nota, de um colaborador de centro de custo desconh
           +ecido ou ausente (tabela aplicável `padrao`), apresenta `VALO
           +R_NAO_POSITIVO` e `CATEGORIA_FORA_POLITICA`, mas **não** apre
           +senta `NOTA_FISCAL_AUSENTE`.                                 
      1044  
      1045  **Exemplo de motivo por política de centro de custo (Dia 2):*
            * um lançamento de hospedagem de R$480,00 em `CC-ENG-PLATAFOR
            MA`, com nota fiscal e dentro da competência, apresenta apena
            s `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — nenhum motivo d
            e teto é avaliado, porque a recusa por política do centro de 
            custo antecede a etapa de agregação (item 15 acima).
      1046  

● Now §8.5 states table — full replace:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 11 lines, removed 5 lines
      1046  
      1047  ### 8.5 Estados possíveis por categoria
      1048  
      1049 -| Categoria | Estados possíveis |                            
      1049 +Os estados dependem da tabela de política e da `periodicidade
           +` efetivamente aplicáveis ao item (RN-019) — não do nome hist
           +órico da categoria:                                          
      1050 +                                                             
      1051 +| Situação na tabela efetivamente aplicável | Estados possíve
           +is |                                                         
      1052  |---|---|
      1051 -| `alimentacao`, `transporte_urbano` | `INTEGRALMENTE_REEMBOL
           -SADO`, `PARCIALMENTE_REEMBOLSADO` (teto diário), `NAO_REEMBOL
           -SADO_TETO_ESGOTADO`, `RECUSADO` |                            
      1052 -| `hospedagem` | `INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_R
           -EEMBOLSADO` (teto individual), `RECUSADO`. **Nunca** `NAO_REE
           -MBOLSADO_TETO_ESGOTADO`, por não participar de saldo comparti
           -lhado |                                                      
      1053 -| Fora da política | Apenas `RECUSADO`; não alcança a etapa d
           -e teto |                                                     
      1054 -| Com limite de política igual a `0,00` no centro de custo (D
           -ia 2) | Apenas `RECUSADO` (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO
           -_CUSTO`); não alcança a etapa de teto, mesmo sendo uma catego
           -ria com estados de teto em outros centros de custo |         
      1053 +| Categoria presente, `limite` positivo, `periodicidade: "dia
           +"` (ex.: `alimentacao`, `transporte_urbano`, `representacao` 
           +quando declarada) | `INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENT
           +E_REEMBOLSADO` (`TETO_DIARIO_APLICADO`), `NAO_REEMBOLSADO_TET
           +O_ESGOTADO` (`TETO_DIARIO_ESGOTADO`), `RECUSADO` (por outra r
           +egra individual) |                                           
      1054 +| Categoria presente, `limite` positivo, `periodicidade: "dia
           +ria"`, sendo `hospedagem` | `INTEGRALMENTE_REEMBOLSADO`, `PAR
           +CIALMENTE_REEMBOLSADO` (`TETO_HOSPEDAGEM_APLICADO`), `RECUSAD
           +O` (por outra regra individual). **Nunca** `NAO_REEMBOLSADO_T
           +ETO_ESGOTADO`, por não participar de saldo compartilhado |   
      1055 +| Categoria presente, `limite` positivo, `periodicidade: "dia
           +ria"`, sendo outra categoria que não `hospedagem` (ex.: `esta
           +cionamento`, AMB-037) | `INTEGRALMENTE_REEMBOLSADO`, `PARCIAL
           +MENTE_REEMBOLSADO` (`TETO_INDIVIDUAL_APLICADO`), `RECUSADO` (
           +por outra regra individual). **Nunca** `NAO_REEMBOLSADO_TETO_
           +ESGOTADO`, pelo mesmo motivo de `hospedagem` |               
      1056 +| Categoria ausente de `padrao` (tabela aplicável é `padrao`)
           + | Apenas `RECUSADO` (`CATEGORIA_FORA_POLITICA`); não alcança
           + a etapa de teto |                                           
      1057 +| Categoria ausente da tabela de um centro de custo cadastrad
           +o (tabela aplicável é a do centro) | Apenas `RECUSADO` (`CATE
           +GORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`); não alcança a etapa de
           + teto |                                                      
      1058 +| Categoria presente na tabela de um centro de custo cadastra
           +do, com `limite` igual a `0,00` | Apenas `RECUSADO` (`CATEGOR
           +IA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`); não alcança a etapa de te
           +to, mesmo sendo uma categoria com estados de teto em outros c
           +entros de custo |                                            
      1059  
      1060 +`limite` igual a `0,00` dentro de `padrao` não gera um estado
           + de item: invalida o arquivo de política inteiro (RN-019, RN-
           +022) antes de qualquer item ser avaliado.                    
      1061 +                                                             
      1062  ---
      1063  
      1064  ## 9. Rastreabilidade da política do RH

● Now §9 rastreabilidade table — add CA-048 row and AMB-037/CA-049 row:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines
      1090  | — (contrato de execução, correção pós-revisão) | AMB-034 | 
            RN-022 | CA-041 a CA-044 |
      1091  | — (contrato estrutural dos arquivos externos, correção pós-
            revisão) | AMB-035 | RN-019, RN-020, RN-021, RN-022 | CA-045,
             CA-046 |
      1092  | — (periodicidade das categorias externas, correção pós-revi
            são) | AMB-036 | RN-019 | CA-047 |
      1093 +| — (contrato de `despesa.moeda: null`, correção pós-revisão)
           + | — | RN-002 | CA-048 |                                     
      1094 +| — (motivo de teto individual para categoria externa, segund
           +a correção pós-revisão) | AMB-037 | RN-019 | CA-049 |        
      1095  
      1096  ---
      1097  

● Now §10 — CA-001/002:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      1099  
      1100  O sistema está pronto quando todos os itens abaixo forem veri
            ficáveis sem leitura de código.
      1101  
      1102 -- [ ] **CA-001** — Processar `exemplos/despesas-exemplo.json`
           - produz `total_reembolsavel` igual a **R$ 585,43**.          
      1103 -- [ ] **CA-002** — A saída daquele arquivo contém 14 registro
           -s, na ordem da entrada, com `indice_entrada` de 1 a 14, e cad
           -a linha coincide com a tabela 4.7 em decisão, valor reembolsá
           -vel e motivos.                                               
      1102 +- [ ] **CA-001** — Processar `exemplos/despesas-exemplo.json`
           + com uma política externa de teste equivalente à política his
           +tórica/padrão (`alimentacao` R$60,00/dia, `transporte_urbano`
           + R$80,00/dia, `hospedagem` R$250,00/diária, `nota_fiscal_obri
           +gatoria_acima_de` R$100,00) produz `total_reembolsavel` igual
           + a **R$ 585,43**.                                            
      1103 +- [ ] **CA-002** — Sob esse mesmo cenário histórico, a saída 
           +daquele arquivo contém 14 registros, na ordem da entrada, com
           + `indice_entrada` de 1 a 14, e cada linha coincide com a tabe
           +la 4.7 em decisão, valor reembolsável e motivos.             
      1104  - [ ] **CA-003** — A soma dos `valor_reembolsavel` apresentad
            os é igual ao `total_reembolsavel` apresentado.
      1105  - [ ] **CA-004** — Duas despesas de alimentação na mesma data
             somando mais de R$ 60,00 rendem exatamente R$ 60,00 naquela 
            data.
      1106  - [ ] **CA-005** — Uma despesa de R$ 61,00 de alimentação, so
            zinha na data, rende R$ 60,00 e não R$ 0,00.

● Now CA-004 to CA-007:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines, removed 4 lines
      1102  - [ ] **CA-001** — Processar `exemplos/despesas-exemplo.json`
             com uma política externa de teste equivalente à política his
            tórica/padrão (`alimentacao` R$60,00/dia, `transporte_urbano`
             R$80,00/dia, `hospedagem` R$250,00/diária, `nota_fiscal_obri
            gatoria_acima_de` R$100,00) produz `total_reembolsavel` igual
             a **R$ 585,43**.
      1103  - [ ] **CA-002** — Sob esse mesmo cenário histórico, a saída 
            daquele arquivo contém 14 registros, na ordem da entrada, com
             `indice_entrada` de 1 a 14, e cada linha coincide com a tabe
            la 4.7 em decisão, valor reembolsável e motivos.
      1104  - [ ] **CA-003** — A soma dos `valor_reembolsavel` apresentad
            os é igual ao `total_reembolsavel` apresentado.
      1105 -- [ ] **CA-004** — Duas despesas de alimentação na mesma data
           - somando mais de R$ 60,00 rendem exatamente R$ 60,00 naquela 
           -data.                                                        
      1106 -- [ ] **CA-005** — Uma despesa de R$ 61,00 de alimentação, so
           -zinha na data, rende R$ 60,00 e não R$ 0,00.                 
      1107 -- [ ] **CA-006** — Com itens de R$ 72,50 e R$ 38,00 nessa ord
           -em no mesmo dia, o primeiro rende R$ 60,00 com `PARCIALMENTE_
           -REEMBOLSADO` e o segundo rende R$ 0,00 com `NAO_REEMBOLSADO_T
           -ETO_ESGOTADO` — não `RECUSADO`.                              
      1108 -- [ ] **CA-007** — Uma hospedagem de R$ 480,00 descrita como 
           -"2 diarias" rende R$ 250,00, e alterar o texto da `descricao`
           - não altera o teto individual de R$ 250,00 aplicado a esse la
           -nçamento.                                                    
      1105 +- [ ] **CA-004** — Sob a política padrão (`alimentacao` limit
           +e R$60,00/dia, `periodicidade: "dia"`), duas despesas de alim
           +entação na mesma data somando mais de R$ 60,00 rendem exatame
           +nte R$ 60,00 naquela data.                                   
      1106 +- [ ] **CA-005** — Sob a política padrão, uma despesa de R$ 6
           +1,00 de alimentação, sozinha na data, rende R$ 60,00 e não R$
           + 0,00.                                                       
      1107 +- [ ] **CA-006** — Sob a política padrão, com itens de R$ 72,
           +50 e R$ 38,00 nessa ordem no mesmo dia, o primeiro rende R$ 6
           +0,00 com `PARCIALMENTE_REEMBOLSADO` e o segundo rende R$ 0,00
           + com `NAO_REEMBOLSADO_TETO_ESGOTADO` — não `RECUSADO`.       
      1108 +- [ ] **CA-007** — Sob a política padrão (`hospedagem` limite
           + R$250,00/diária, `periodicidade: "diaria"`), uma hospedagem 
           +de R$ 480,00 descrita como "2 diarias" rende R$ 250,00, e alt
           +erar o texto da `descricao` não altera o teto individual de R
           +$ 250,00 aplicado a esse lançamento.                         
      1109  - [ ] **CA-008** — Uma despesa de exatamente R$ 100,00 sem no
            ta é elegível; uma de R$ 100,01 sem nota é recusada.
      1110  - [ ] **CA-009** — Um valor informado de `100.004` sem nota é
             elegível e um de `100.005` sem nota é recusado.
      1111  - [ ] **CA-010** — Numa entrada com um único item elegível, t
            rocar na `descricao` um texto neutro por termos como "aeropor
            to" ou "hotel" não amplia teto algum e não altera o `valor_re
            embolsavel`; um campo `em_viagem: true` também não altera res
            ultado algum. O cenário usa item único para isolar a ausência
             de inferência de viagem, já que `descricao` integra a chave 
            de duplicidade.

● Now CA-008/009, CA-013, CA-016, CA-018:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      1106  - [ ] **CA-005** — Sob a política padrão, uma despesa de R$ 6
            1,00 de alimentação, sozinha na data, rende R$ 60,00 e não R$
             0,00.
      1107  - [ ] **CA-006** — Sob a política padrão, com itens de R$ 72,
            50 e R$ 38,00 nessa ordem no mesmo dia, o primeiro rende R$ 6
            0,00 com `PARCIALMENTE_REEMBOLSADO` e o segundo rende R$ 0,00
             com `NAO_REEMBOLSADO_TETO_ESGOTADO` — não `RECUSADO`.
      1108  - [ ] **CA-007** — Sob a política padrão (`hospedagem` limite
             R$250,00/diária, `periodicidade: "diaria"`), uma hospedagem 
            de R$ 480,00 descrita como "2 diarias" rende R$ 250,00, e alt
            erar o texto da `descricao` não altera o teto individual de R
            $ 250,00 aplicado a esse lançamento.
      1109 -- [ ] **CA-008** — Uma despesa de exatamente R$ 100,00 sem no
           -ta é elegível; uma de R$ 100,01 sem nota é recusada.         
      1110 -- [ ] **CA-009** — Um valor informado de `100.004` sem nota é
           - elegível e um de `100.005` sem nota é recusado.             
      1109 +- [ ] **CA-008** — Sob uma política com `nota_fiscal_obrigato
           +ria_acima_de = 100.00` (o valor da política padrão/histórica 
           +usada nos exemplos desta spec; não é uma constante interna do
           + sistema — RN-009), uma despesa de exatamente R$ 100,00 sem n
           +ota é elegível; uma de R$ 100,01 sem nota é recusada.        
      1110 +- [ ] **CA-009** — Sob a mesma política (`nota_fiscal_obrigat
           +oria_acima_de = 100.00`), um valor informado de `100.004` sem
           + nota é elegível e um de `100.005` sem nota é recusado.      
      1111  - [ ] **CA-010** — Numa entrada com um único item elegível, t
            rocar na `descricao` um texto neutro por termos como "aeropor
            to" ou "hotel" não amplia teto algum e não altera o `valor_re
            embolsavel`; um campo `em_viagem: true` também não altera res
            ultado algum. O cenário usa item único para isolar a ausência
             de inferência de viagem, já que `descricao` integra a chave 
            de duplicidade.
      1112  - [ ] **CA-011** — Uma despesa datada em `2026-04-15`, com ja
            nela de julho, é recusada com `FORA_COMPETENCIA`.
      1113  - [ ] **CA-012** — Despesas datadas exatamente em `periodo.in
            icio` e em `periodo.fim` são temporalmente elegíveis.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1111  - [ ] **CA-010** — Numa entrada com um único item elegível, t
            rocar na `descricao` um texto neutro por termos como "aeropor
            to" ou "hotel" não amplia teto algum e não altera o `valor_re
            embolsavel`; um campo `em_viagem: true` também não altera res
            ultado algum. O cenário usa item único para isolar a ausência
             de inferência de viagem, já que `descricao` integra a chave 
            de duplicidade.
      1112  - [ ] **CA-011** — Uma despesa datada em `2026-04-15`, com ja
            nela de julho, é recusada com `FORA_COMPETENCIA`.
      1113  - [ ] **CA-012** — Despesas datadas exatamente em `periodo.in
            icio` e em `periodo.fim` são temporalmente elegíveis.
      1114 -- [ ] **CA-013** — Dois itens idênticos exceto pelo `despesa.
           -id` produzem o primeiro integral e o segundo recusado com `DU
           -PLICIDADE`.                                                  
      1114 +- [ ] **CA-013** — Dois itens idênticos exceto pelo `despesa.
           +id`, ambos elegíveis para reembolso integral antes da avaliaç
           +ão de duplicidade — categoria presente na tabela de política 
           +aplicável, dentro da competência, com nota fiscal quando exig
           +ida, e saldo de teto suficiente —, produzem o primeiro integr
           +al e o segundo recusado com `DUPLICIDADE`; duplicidade não co
           +nfere elegibilidade que o item não teria por si.             
      1115  - [ ] **CA-014** — Itens de R$ 100,00 e R$ 100,01, mesmo dia 
            e mesmo fornecedor, não são tratados como duplicatas.
      1116  - [ ] **CA-015** — Uma despesa com categoria `ALIMENTACAO` é 
            processada como `alimentacao`.
      1117  - [ ] **CA-016** — Uma despesa com categoria `coworking` é re
            cusada com `CATEGORIA_FORA_POLITICA` e reembolsável R$ 0,00.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1114  - [ ] **CA-013** — Dois itens idênticos exceto pelo `despesa.
            id`, ambos elegíveis para reembolso integral antes da avaliaç
            ão de duplicidade — categoria presente na tabela de política 
            aplicável, dentro da competência, com nota fiscal quando exig
            ida, e saldo de teto suficiente —, produzem o primeiro integr
            al e o segundo recusado com `DUPLICIDADE`; duplicidade não co
            nfere elegibilidade que o item não teria por si.
      1115  - [ ] **CA-014** — Itens de R$ 100,00 e R$ 100,01, mesmo dia 
            e mesmo fornecedor, não são tratados como duplicatas.
      1116  - [ ] **CA-015** — Uma despesa com categoria `ALIMENTACAO` é 
            processada como `alimentacao`.
      1117 -- [ ] **CA-016** — Uma despesa com categoria `coworking` é re
           -cusada com `CATEGORIA_FORA_POLITICA` e reembolsável R$ 0,00. 
      1117 +- [ ] **CA-016** — Para um colaborador de centro de custo des
           +conhecido, ausente, nulo ou de tipo inválido (tabela aplicáve
           +l `padrao`, que não declara `coworking`), uma despesa com cat
           +egoria `coworking` é recusada com `CATEGORIA_FORA_POLITICA` e
           + reembolsável R$ 0,00 — este critério não generaliza para cen
           +tro de custo cadastrado, onde a mesma ausência produz `CATEGO
           +RIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (CA-025).                 
      1118  - [ ] **CA-017** — Uma despesa de −R$ 45,00 é recusada com `V
            ALOR_NAO_POSITIVO` e não reduz o total do período.
      1119  - [ ] **CA-018** — Um valor informado de `33.333` aparece com
            o `valor_informado` `33.333`, `valor_normalizado` `33,33` e `
            valor_reembolsavel` `33,33`.
      1120  - [ ] **CA-019** — Dois itens com o mesmo `despesa.id` válido
             são **ambos** recusados com `ID_DUPLICADO`.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1116  - [ ] **CA-015** — Uma despesa com categoria `ALIMENTACAO` é 
            processada como `alimentacao`.
      1117  - [ ] **CA-016** — Para um colaborador de centro de custo des
            conhecido, ausente, nulo ou de tipo inválido (tabela aplicáve
            l `padrao`, que não declara `coworking`), uma despesa com cat
            egoria `coworking` é recusada com `CATEGORIA_FORA_POLITICA` e
             reembolsável R$ 0,00 — este critério não generaliza para cen
            tro de custo cadastrado, onde a mesma ausência produz `CATEGO
            RIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (CA-025).
      1118  - [ ] **CA-017** — Uma despesa de −R$ 45,00 é recusada com `V
            ALOR_NAO_POSITIVO` e não reduz o total do período.
      1119 -- [ ] **CA-018** — Um valor informado de `33.333` aparece com
           -o `valor_informado` `33.333`, `valor_normalizado` `33,33` e `
           -valor_reembolsavel` `33,33`.                                 
      1119 +- [ ] **CA-018** — Sob a política padrão, com o item elegível
           + (categoria presente na tabela, dentro da competência, com no
           +ta fiscal quando exigida, e saldo de teto suficiente), um val
           +or informado de `33.333` aparece como `valor_informado` `33.3
           +33`, `valor_normalizado` `33,33` e `valor_reembolsavel` `33,3
           +3`.                                                          
      1120  - [ ] **CA-019** — Dois itens com o mesmo `despesa.id` válido
             são **ambos** recusados com `ID_DUPLICADO`.
      1121  - [ ] **CA-020** — Uma entrada com `periodo.inicio` posterior
             a `periodo.fim` não produz apuração alguma; uma entrada com 
            `despesas: []` produz resultados vazio e total R$ 0,00.
      1122  - [ ] **CA-021** — Um item com `data: "31/07/2026"` e `valor:
             "72,50"` apresenta dois motivos estruturais na ordem canônic
            a de contrato: `CAMPO_FORMATO_INVALIDO` com `campo` igual a `
            despesa.data`, depois `CAMPO_TIPO_INVALIDO` com `campo` igual
             a `despesa.valor`.

● Now CA-026 replace, CA-045 update, and insert CA-049:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1127  
      1128  - [ ] **CA-024** — Um colaborador de centro de custo desconhe
            cido, ausente, nulo ou de tipo inválido usa integralmente a p
            olítica `padrao` — nenhuma mistura com nenhuma tabela específ
            ica.
      1129  - [ ] **CA-025** — Categoria ausente na tabela de um centro d
            e custo cadastrado (ex.: `hospedagem` em `CC-ADM`) é recusada
             com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, sem receber o
             limite de `padrao`.
      1130 -- [ ] **CA-026** — `representacao` é reembolsável apenas nos 
           -centros de custo que a declaram explicitamente (`CC-COMERCIAL
           -`); em qualquer outro centro de custo, inclusive `padrao`, é 
           -`CATEGORIA_FORA_POLITICA`.                                   
      1130 +- [ ] **CA-026** — `representacao` é reembolsável somente qua
           +ndo a única tabela aplicável a declara. Em `CC-COMERCIAL`, ap
           +lica-se o limite configurado. Quando a tabela aplicável é `pa
           +drao` e ela não declara `representacao`, o item recebe `CATEG
           +ORIA_FORA_POLITICA`. Quando a tabela aplicável é a de outro c
           +entro de custo cadastrado que não declara `representacao`, o 
           +item recebe `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`.       
      1131  - [ ] **CA-027** — Categoria com limite de política igual a `
            0,00` (ex.: `hospedagem` em `CC-ENG-PLATAFORMA`) é recusada c
            om `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e `valor_reembol
            savel` `0,00` — nunca `PARCIALMENTE_REEMBOLSADO`.
      1132  - [ ] **CA-028** — Numa despesa em moeda estrangeira, `moeda 
            != BRL` não amplia teto algum, não afeta outros itens do mesm
            o dia nem do período — RN-016 continua sem efeito.
      1133  - [ ] **CA-029** — Uma despesa datada num dia sem cotação exa
            ta usa a cotação do último dia útil anterior disponível (ex.:
             EUR de `2026-07-18`, sábado, usa a cotação de `2026-07-17`).

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1149  - [ ] **CA-042** — Argumento ausente, repetido ou desconhecid
            o retorna código `2` — inclusive o comando anterior à polític
            a v4, com apenas `--input`/`--output`, mesmo quando a entrada
             só tem despesas em BRL.
      1150  - [ ] **CA-043** — Arquivo de política ou de câmbio inexisten
            te, ilegível, sintaticamente inválido ou estruturalmente invá
            lido (conforme o contrato fechado de 4.1.1) retorna código `2
            `, mesmo com um arquivo de despesas perfeitamente válido.
      1151  - [ ] **CA-044** — Em qualquer cenário de código `2` ou `3`, 
            um arquivo em `--output` que já existisse antes da execução p
            ermanece intacto, byte a byte; stdout permanece vazio e a men
            sagem de erro vai para stderr.
      1152 -- [ ] **CA-045** — Um arquivo de política que satisfaça integ
           -ralmente o contrato de 4.1.1 (`vigencia` real, `moeda_base` e
           -xatamente `"BRL"`, `padrao` e `centros_custo` objetos, `nota_
           -fiscal_obrigatoria_acima_de` não negativo, cada tabela de cat
           -egoria com `limite` ≥ 0 e `periodicidade` igual a `"dia"` ou 
           -`"diaria"`) é aceito; qualquer violação de um desses pontos i
           -nvalida o arquivo de política por inteiro.                   
      1152 +- [ ] **CA-045** — Um arquivo de política que satisfaça integ
           +ralmente o contrato de 4.1.1 (`vigencia` real, `moeda_base` e
           +xatamente `"BRL"`, `padrao` e `centros_custo` objetos, `nota_
           +fiscal_obrigatoria_acima_de` não negativo, cada tabela de cat
           +egoria com `periodicidade` igual a `"dia"` ou `"diaria"`, `li
           +mite` ≥ 0 dentro de `centros_custo` e `limite` estritamente >
           + 0 dentro de `padrao`) é aceito; qualquer violação de um dess
           +es pontos — inclusive `limite` igual a `0,00` em `padrao` — i
           +nvalida o arquivo de política por inteiro.                   
      1153  - [ ] **CA-046** — Um arquivo de câmbio que satisfaça o contr
            ato de 4.1.1 (`moeda_base` exatamente `"BRL"`, `taxas` objeto
             aninhando data real → moeda `[A-Z]{3}` → taxa estritamente p
            ositiva) é aceito, inclusive com `taxas: {}` — nesse caso, to
            da despesa em moeda estrangeira é recusada individualmente co
            m `MOEDA_SEM_COTACAO`, sem invalidar o arquivo.
      1154  - [ ] **CA-047** — Dois itens de `representacao` (`CC-COMERCI
            AL`, `periodicidade: "dia"`, limite R$300,00) na mesma data c
            onsomem conjuntamente o mesmo saldo de R$300,00, distribuído 
            em ordem de `indice_entrada` — o mesmo mecanismo de RN-011/RN
            -012/RN-015, não um teto individual por item.
      1155  - [ ] **CA-048** — Um item com `"moeda": null` é recusado com
             `CAMPO_AUSENTE` e `campo` igual a `despesa.moeda`; um item s
            em a chave `moeda` no objeto não recebe motivo algum e assume
             `BRL`.

● Now insert new CA-049 after CA-048:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines
      1154  - [ ] **CA-047** — Dois itens de `representacao` (`CC-COMERCI
            AL`, `periodicidade: "dia"`, limite R$300,00) na mesma data c
            onsomem conjuntamente o mesmo saldo de R$300,00, distribuído 
            em ordem de `indice_entrada` — o mesmo mecanismo de RN-011/RN
            -012/RN-015, não um teto individual por item.
      1155  - [ ] **CA-048** — Um item com `"moeda": null` é recusado com
             `CAMPO_AUSENTE` e `campo` igual a `despesa.moeda`; um item s
            em a chave `moeda` no objeto não recebe motivo algum e assume
             `BRL`.
      1156  
      1157 +**Segunda correção após revisão independente:**              
      1158 +                                                             
      1159 +- [ ] **CA-049** — Sob uma política válida em que a categoria
           + `estacionamento` tenha `limite: 50.00` e `periodicidade: "di
           +aria"`, uma despesa elegível de R$ 80,00 nessa categoria é `P
           +ARCIALMENTE_REEMBOLSADO`, com `valor_reembolsavel` `50,00` e 
           +motivo único `{ "codigo": "TETO_INDIVIDUAL_APLICADO", "regra"
           +: "RN-019", "campo": null }`.                                
      1160 +                                                             
      1161  ---
      1162  
      1163  ## 11. O que fica em aberto

● Now §11 counts and §12.5 ranges:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1176  | Fila de aprovação manual (item C do comunicado do Dia 2) nã
            o está implementada | Um estado novo (`AGUARDANDO_APROVACAO`)
            , critérios de fila e de gestor responsável — deliberadamente
             fora de escopo nesta rodada (AMB-033) |
      1177  | Não há seleção automática de política histórica por vigênci
            a | Uma segunda política externa anterior, com sua própria ja
            nela de vigência, e uma regra de escolha entre elas quando ma
            is de uma existir (RN-021 trata `vigencia` como informativa n
            esta versão, porque só uma política existe) |
      1178  
      1179 -**Perguntas que não puderam ser respondidas por ausência de i
           -nterlocutor.** A política v3 foi escrita pelo RH e não houve 
           -canal para esclarecimento; o mesmo vale para a política v4. A
           -s leituras adotadas nas trinta e seis ambiguidades da seção 6
           - são decisões desta spec, não interpretações confirmadas pela
           - área de origem. As que mais mudariam o resultado se a área d
           -ecidisse diferente: AMB-002 (corte no teto contra recusa inte
           -gral), AMB-008 (uma diária por lançamento) e AMB-004 (ausênci
           -a de nota recusa o item inteiro), da linha de base; e, do env
           -elope do Dia 2, AMB-020 (categoria ausente em centro de custo
           - cadastrado), AMB-022 (limite igual a zero) e AMB-024 (data s
           -em cotação exata) — as três com maior impacto financeiro obse
           -rvado nos arquivos de exemplo. `AMB-034` a `AMB-036`, acresce
           -ntadas na correção pós-revisão, fecham contratos de execução 
           -e de arquivo externo em vez de resolver ambiguidade de leitur
           -a de texto — têm decisão técnica, mas não dependem de interlo
           -cutor do RH.                                                 
      1179 +**Perguntas que não puderam ser respondidas por ausência de i
           +nterlocutor.** A política v3 foi escrita pelo RH e não houve 
           +canal para esclarecimento; o mesmo vale para a política v4. A
           +s leituras adotadas nas trinta e sete ambiguidades da seção 6
           + são decisões desta spec, não interpretações confirmadas pela
           + área de origem. As que mais mudariam o resultado se a área d
           +ecidisse diferente: AMB-002 (corte no teto contra recusa inte
           +gral), AMB-008 (uma diária por lançamento) e AMB-004 (ausênci
           +a de nota recusa o item inteiro), da linha de base; e, do env
           +elope do Dia 2, AMB-020 (categoria ausente em centro de custo
           + cadastrado), AMB-022 (limite igual a zero) e AMB-024 (data s
           +em cotação exata) — as três com maior impacto financeiro obse
           +rvado nos arquivos de exemplo. `AMB-034` a `AMB-037`, acresce
           +ntadas nas correções pós-revisão, fecham contratos de execuçã
           +o, de arquivo externo e o vocabulário de motivos de teto indi
           +vidual em vez de resolver ambiguidade de leitura de texto — t
           +êm decisão técnica, mas não dependem de interlocutor do RH.  
      1180  
      1181  ---
      1182  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1251  
      1252  ### 12.5 Como usar esta seção
      1253  
      1254 -Os quatro cenários acima são a fonte normativa de `CA-024` a 
           -`CA-040`; o contrato de execução e os contratos estruturais d
           -os arquivos externos (`CA-041` a `CA-048`) são verificáveis i
           -ndependentemente de qualquer um desses quatro cenários financ
           -eiros. Divergência entre uma implementação e os valores desta
           - seção é bug de código, não ambiguidade de spec — as ambiguid
           -ades relevantes já foram decididas em `AMB-019` a `AMB-036` e
           - as regras correspondentes (`RN-019` a `RN-022`, mais `RN-002
           -`, `RN-004`, `RN-005`, `RN-007`, `RN-009`, `RN-010` e `RN-017
           -` atualizadas). A tabela de `e-009` (12.3) usa `CATEGORIA_NAO
           -_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITICA` — 
           -correção da mesma revisão que fechou RN-007/RN-019 (`coworkin
           -g` está ausente da tabela de `CC-COMERCIAL`, que é a única ta
           -bela aplicável a um centro cadastrado; `padrao` nunca é consu
           -ltada). Fixtures de teste automatizado para estes cenários sã
           -o trabalho de `tasks.md`, ainda não criado para esta versão. 
      1254 +Os quatro cenários acima são a fonte normativa de `CA-024` a 
           +`CA-040`; o contrato de execução, os contratos estruturais do
           +s arquivos externos e o vocabulário de motivos de teto (`CA-0
           +41` a `CA-049`) são verificáveis independentemente de qualque
           +r um desses quatro cenários financeiros. Divergência entre um
           +a implementação e os valores desta seção é bug de código, não
           + ambiguidade de spec — as ambiguidades relevantes já foram de
           +cididas em `AMB-019` a `AMB-037` e as regras correspondentes 
           +(`RN-019` a `RN-022`, mais `RN-002`, `RN-004`, `RN-005`, `RN-
           +007`, `RN-009`, `RN-010`, `RN-011` a `RN-015` e `RN-017` atua
           +lizadas). A tabela de `e-009` (12.3) usa `CATEGORIA_NAO_REEMB
           +OLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITICA` — correç
           +ão da mesma revisão que fechou RN-007/RN-019 (`coworking` est
           +á ausente da tabela de `CC-COMERCIAL`, que é a única tabela a
           +plicável a um centro cadastrado; `padrao` nunca é consultada)
           +. Fixtures de teste automatizado para estes cenários são trab
           +alho de `tasks.md`, ainda não criado para esta versão.       

  Ran 1 shell command (ctrl+o to expand)

● Counts match: 37 AMB, 22 RN, 49 CA. Now let's move to DECISIONS.md edits.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      12  
      13  ## D-003 — Envelope do Dia 2 e política v4 · `2026-08-05`
      14  
      15 -**Gatilho:** recebimento do envelope oficial do Dia 2, liberado
         - às 10h — mudança de requisito do RH, política de reembolso v4,
         - com vigência imediata e retroativa à competência atual. Cinco 
         -arquivos oficiais acompanham o envelope e foram recebidos e col
         -ocados em `exemplos/envelope/` sem alteração; serão versionados
         - no commit documental correspondente: `00-ENVELOPE-LACRADO.md` 
         -(comunicado do RH), `politica-v4.json` (tabela de limites por c
         -entro de custo), `cambio.json` (taxas de câmbio por data), `des
         -pesas-envelope.json` (Rafael Nkemelu, `CC-COMERCIAL`, exercitan
         -do moeda estrangeira) e `despesas-envelope-cc-desconhecido.json
         -` (Dani Okonkwo, `CC-SUPORTE-N2`, exercitando centro de custo f
         -ora da tabela). Uma sessão de análise anterior a esta (modo lei
         -tura, sem alteração de arquivo) já havia mapeado o comunicado i
         -tem a item, calculado os quatro cenários financeiros abaixo man
         -ualmente e identificado as ambiguidades candidatas contra a lis
         -ta de análise obrigatória do próprio processo. Esta entrada for
         -maliza as decisões tomadas sobre essas ambiguidades e atualiza 
         -`spec.md` para a versão 1.2. Uma revisão independente subsequen
         -te, na mesma data, encontrou seis problemas de precisão de cont
         -rato — nenhum deles muda um total financeiro já registrado — e 
         -as correções estão incorporadas nesta mesma entrada, na seção "
         -Correção após revisão independente" ao final.                  
      15 +**Gatilho:** recebimento do envelope oficial do Dia 2, liberado
         + às 10h — mudança de requisito do RH, política de reembolso v4,
         + com vigência imediata e retroativa à competência atual. Cinco 
         +arquivos oficiais acompanham o envelope e foram recebidos e col
         +ocados em `exemplos/envelope/` sem alteração; serão versionados
         + no commit documental correspondente: `00-ENVELOPE-LACRADO.md` 
         +(comunicado do RH), `politica-v4.json` (tabela de limites por c
         +entro de custo), `cambio.json` (taxas de câmbio por data), `des
         +pesas-envelope.json` (Rafael Nkemelu, `CC-COMERCIAL`, exercitan
         +do moeda estrangeira) e `despesas-envelope-cc-desconhecido.json
         +` (Dani Okonkwo, `CC-SUPORTE-N2`, exercitando centro de custo f
         +ora da tabela). Uma sessão de análise anterior a esta (modo lei
         +tura, sem alteração de arquivo) já havia mapeado o comunicado i
         +tem a item, calculado os quatro cenários financeiros abaixo man
         +ualmente e identificado as ambiguidades candidatas contra a lis
         +ta de análise obrigatória do próprio processo. Esta entrada for
         +maliza as decisões tomadas sobre essas ambiguidades e atualiza 
         +`spec.md` para a versão 1.2. Uma revisão independente subsequen
         +te, na mesma data, encontrou oito problemas de precisão de cont
         +rato — nenhum deles muda um total financeiro já registrado — e 
         +as correções estão incorporadas nesta mesma entrada, na seção "
         +Correção após revisão independente" ao final. Uma segunda revis
         +ão independente, também na mesma data, encontrou pontos adicion
         +ais de precisão de contrato e uma lacuna de vocabulário de moti
         +vos — incorporados na seção "Ajustes finais da segunda revisão 
         +independente", ao final desta entrada.                         
      16  
      17  **O que mudou na spec:**
      18  
● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 2 lines, removed 2 lines
      24  6. **RN-021 (nova) — vigência do arquivo de política.** `politi
          ca.vigencia` é obrigatória e validada estruturalmente (mesma se
          veridade de RN-001, mas sobre o arquivo de política, não o de d
          espesas); uma vez válida, é metadado informativo — não filtra i
          tens por data, porque nenhuma política anterior à v4 foi fornec
          ida para justificar seleção histórica.
      25  7. **RN-022 (nova) — processabilidade dos arquivos externos.** 
          Política ou câmbio ausente, ilegível, sintaticamente inválido o
          u estruturalmente inválido é erro global: nenhuma apuração é pr
          oduzida, mesmo com um arquivo de despesas perfeitamente válido.
      26  8. **RN-002, RN-004, RN-005, RN-007 atualizadas** para refletir
           o sétimo campo (`despesa.moeda`), a ordem conversão-então-arre
          dondamento, e o conjunto de categorias reembolsáveis deixando d
          e ser uma constante de três valores.
      27 -9. **Dezoito ambiguidades novas registradas (`AMB-019` a `AMB-0
         -36`)**, cobrindo: centro de custo desconhecido/ausente (AMB-019
         -), categoria ausente em centro cadastrado (AMB-020), `represent
         -acao` exclusiva de quem a declara (AMB-021), limite igual a zer
         -o (AMB-022), moeda como indício de viagem (AMB-023), data sem c
         -otação exata (AMB-024), moeda sem cotação alguma (AMB-025), ord
         -em de conversão/arredondamento (AMB-026), base de comparação do
         - gatilho de nota fiscal (AMB-027), chave de duplicidade entre m
         -oedas (AMB-028), campos de auditoria da conversão (AMB-029), va
         -lidação de vigência (AMB-030), erro global versus recusa indivi
         -dual nos arquivos externos (AMB-031), regressão da baseline his
         -tórica (AMB-032), escopo do item C (AMB-033), e as três acresce
         -ntadas na correção pós-revisão: forma de fornecimento de políti
         -ca/câmbio à CLI (AMB-034), contrato estrutural dos arquivos ext
         -ernos (AMB-035) e semântica de `periodicidade` (AMB-036).      
      28 -10. **Vinte e cinco critérios de aceite novos (`CA-024` a `CA-0
         -48`)**: `CA-024` a `CA-040` da primeira leitura do envelope, in
         -cluindo os quatro cenários financeiros completos do `§12` (nova
         - seção); `CA-041` a `CA-048` da correção pós-revisão, cobrindo 
         -o contrato de execução, os contratos estruturais dos arquivos e
         -xternos, a periodicidade diária de `representacao` e `despesa.m
         -oeda: null`.                                                   
      27 +9. **Dezenove ambiguidades novas registradas (`AMB-019` a `AMB-
         +037`)**, cobrindo: centro de custo desconhecido/ausente (AMB-01
         +9), categoria ausente em centro cadastrado (AMB-020), `represen
         +tacao` exclusiva de quem a declara (AMB-021), limite igual a ze
         +ro (AMB-022), moeda como indício de viagem (AMB-023), data sem 
         +cotação exata (AMB-024), moeda sem cotação alguma (AMB-025), or
         +dem de conversão/arredondamento (AMB-026), base de comparação d
         +o gatilho de nota fiscal (AMB-027), chave de duplicidade entre 
         +moedas (AMB-028), campos de auditoria da conversão (AMB-029), v
         +alidação de vigência (AMB-030), erro global versus recusa indiv
         +idual nos arquivos externos (AMB-031), regressão da baseline hi
         +stórica (AMB-032), escopo do item C (AMB-033), as três acrescen
         +tadas na correção pós-revisão — forma de fornecimento de políti
         +ca/câmbio à CLI (AMB-034), contrato estrutural dos arquivos ext
         +ernos (AMB-035) e semântica de `periodicidade` (AMB-036) —, e a
         + acrescentada na segunda correção pós-revisão: motivo de teto i
         +ndividual para categoria externa de periodicidade `"diaria"` di
         +ferente de hospedagem (AMB-037).                               
      28 +10. **Vinte e seis critérios de aceite novos (`CA-024` a `CA-04
         +9`)**: `CA-024` a `CA-040` da primeira leitura do envelope, inc
         +luindo os quatro cenários financeiros completos do `§12` (nova 
         +seção); `CA-041` a `CA-048` da correção pós-revisão, cobrindo o
         + contrato de execução, os contratos estruturais dos arquivos ex
         +ternos, a periodicidade diária de `representacao` e `despesa.mo
         +eda: null`; `CA-049` da segunda correção pós-revisão, cobrindo 
         +o motivo `TETO_INDIVIDUAL_APLICADO`.                           
      29  
      30  **Comportamento anterior invalidado:**
      31  

● Now line 38 "Ambiguidades identificadas" and the política bullet, RN antigos
  afetados, RN e CA novos:

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      35  - A garantia de que `colaborador.centro_custo` "nunca é usado p
          ara cruzar dados" (§3, sobre agregação entre arquivos) permanec
          e verdadeira, mas a leitura mais ampla de que o campo não tem "
          uso em regra alguma" deixa de valer: `centro_custo` passa a det
          erminar a política aplicável dentro do próprio arquivo (RN-019)
          . A tolerância estrutural do bloco `colaborador` (RN-001) não m
          uda — só o efeito financeiro de um `centro_custo` presente e re
          conhecido.
      36  - `§11`, "o que fica em aberto", removeu a linha "Apenas BRL é 
          suportado" — deixou de ser uma limitação de produto e passou a 
          ser requisito atendido.
      37  
      38 -**Ambiguidades identificadas:** as dezoito listadas no item 9 a
         -cima, com decisão, justificativa e regra afetada registradas em
         - `spec.md §6` (`AMB-019` a `AMB-036`).                         
      38 +**Ambiguidades identificadas:** as dezenove listadas no item 9 
         +acima, com decisão, justificativa e regra afetada registradas e
         +m `spec.md §6` (`AMB-019` a `AMB-037`).                        
      39  
      40  **Decisões dos quatro grupos** (política por centro de custo; c
          âmbio; conversão e regras financeiras; saída e vigência):
      41  

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      39  
      40  **Decisões dos quatro grupos** (política por centro de custo; c
          âmbio; conversão e regras financeiras; saída e vigência):
      41  
      42 -- **Política por centro de custo:** `padrao` como fallback inte
         -gral; centro cadastrado usa só suas categorias declaradas, sem 
         -fallback por categoria; limite zero recusa (não corta a zero); 
         -comparação de `centro_custo` é textual e exata.                
      42 +- **Política por centro de custo:** `padrao` como fallback inte
         +gral; centro cadastrado usa só suas categorias declaradas, sem 
         +fallback por categoria; limite zero em centro cadastrado recusa
         + a categoria (não corta a zero); limite zero em `padrao` invali
         +da o arquivo de política inteiro, código de saída `2` (correção
         + da segunda revisão); comparação de `centro_custo` é textual e 
         +exata.                                                         
      43  - **Câmbio:** cotação exata ou a mais recente anterior; nunca f
          utura; sem interpolação; sem cotação utilizável recusa o item.
      44  - **Conversão e regras financeiras:** um único arredondamento a
          pós a multiplicação; nota fiscal e duplicidade operam sobre o v
          alor convertido (duplicidade com moeda como componente adiciona
          l da chave, não substituindo o valor).
      45  - **Saída e vigência:** três campos de auditoria novos na saída
          ; vigência obrigatória e validada, mas informativa quanto a ite
          ns individuais.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 2 lines, removed 2 lines
      44  - **Conversão e regras financeiras:** um único arredondamento a
          pós a multiplicação; nota fiscal e duplicidade operam sobre o v
          alor convertido (duplicidade com moeda como componente adiciona
          l da chave, não substituindo o valor).
      45  - **Saída e vigência:** três campos de auditoria novos na saída
          ; vigência obrigatória e validada, mas informativa quanto a ite
          ns individuais.
      46  
      47 -**RN e CA antigos afetados:** RN-002, RN-004, RN-005, RN-007, R
         -N-009, RN-010, RN-017 (textos atualizados, sem troca de número;
         - RN-007 recebeu correção adicional na revisão — ver ao final). 
         -Nenhum RN ou CA de `1.1` foi removido ou renumerado — `RN-001` 
         -a `RN-018` e `CA-001` a `CA-023` permanecem intactos onde não l
         -istados acima.                                                 
      47 +**RN e CA antigos afetados:** RN-002, RN-004, RN-005, RN-007, R
         +N-009, RN-010, RN-011, RN-012, RN-013, RN-014, RN-015, RN-017 (
         +textos atualizados, sem troca de número; RN-007 recebeu correçã
         +o adicional na revisão, e RN-011 a RN-015 receberam correção ad
         +icional na segunda revisão, generalizando o mecanismo de teto p
         +or `periodicidade` — ver ao final). Nenhum RN ou CA de `1.1` fo
         +i removido ou renumerado — `RN-001` a `RN-018` e `CA-001` a `CA
         +-023` permanecem intactos onde não listados acima.             
      48  
      49 -**RN e CA novos:** `RN-019` a `RN-022` (nenhuma RN além de `RN-
         -022` foi criada — a correção pós-revisão só atualizou RN-019 e 
         -RN-022, sem numeração nova); `CA-024` a `CA-048`.              
      49 +**RN e CA novos:** `RN-019` a `RN-022` (nenhuma RN além de `RN-
         +022` foi criada — as correções pós-revisão só atualizaram RN-01
         +9 e RN-022, sem numeração nova); `CA-024` a `CA-049`.          
      50  
      51  **DT-007 invalidada.** `plan.md` ainda declara, em DT-007, "est
          rutura única e imutável... sem mecanismo genérico de regras... 
          sem arquivo externo de configuração" para `PoliticaReembolso` —
           essa decisão técnica está tecnicamente incorreta a partir dest
          a versão da spec e será revista quando `plan.md` for atualizado
          , em etapa posterior. Esta entrada apenas formaliza a invalidaç
          ão do lado da spec; nenhuma linha de `plan.md` foi tocada nesta
           rodada.
      52  

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      54  
      55  **Item C fora de escopo.** A fila de aprovação manual (`AGUARDA
          NDO_APROVACAO` para itens acima de R$500) não foi especificada 
          nesta rodada — é explicitamente opcional no próprio comunicado 
          do RH, e nenhum item dos dois arquivos de exemplo do envelope u
          ltrapassa R$500 sob a interpretação adotada, então nada nos dad
          os obriga essa decisão agora (`AMB-033`).
      56  
      57 -**Custo documental e técnico previsto:** esta entrada e a atual
         -ização de `spec.md` (cabeçalho, `§0` nova, `§3`, `§4.1`/`4.1.1`
         - nova e depois reescrita na correção, `§4.2`, `§4.3`, `§4.5`, `
         -§4.7`, `§5` com sete RN atualizadas e quatro novas — duas delas
         - corrigidas de novo na revisão —, `§6` com dezoito AMB novas, `
         -§7`, `§8.1` a `8.5`, `§9`, `§10` com vinte e cinco CA novas, `§
         -11`, `§12` nova) — nenhum outro arquivo tocado nesta rodada. `p
         -lan.md` precisará de: nova DT substituindo DT-007, extensão de 
         -DT-003 (CLI, agora com o contrato exato já fixado em `spec.md` 
         -`§4.1.1`) e DT-005 (parsing) para os arquivos externos, e um no
         -vo passo no pipeline (§2). `tasks.md` precisará de tasks novas 
         -a partir de `T-022` para: leitor de política, leitor de câmbio,
         - campo `moeda` no contrato do item (incluindo a distinção ausên
         -cia-de-chave versus `null`), conversão cambial no pipeline, res
         -olução de política por centro de custo (incluindo a semântica d
         -e `periodicidade`), motivo `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_C
         -USTO`, motivo `MOEDA_SEM_COTACAO`, três campos novos de saída, 
         -validação de `vigencia`, os quatro argumentos de CLI com seus c
         -ódigos de saída, validação estrutural dos dois arquivos externo
         -s, e os quatro cenários de regressão do `§12` como fixtures de 
         -teste.                                                         
      57 +**Custo documental e técnico previsto:** esta entrada e a atual
         +ização de `spec.md` (cabeçalho, `§0` nova, `§3`, `§4.1`/`4.1.1`
         + nova e depois reescrita nas duas correções, `§4.2`, `§4.3`, `§
         +4.5`, `§4.7`, `§5` com onze RN atualizadas e quatro novas — vár
         +ias delas corrigidas de novo nas revisões —, `§6` com dezenove 
         +AMB novas, `§7`, `§8.1` a `8.5`, `§9`, `§10` com vinte e seis C
         +A novas, `§11`, `§12` nova) — nenhum outro arquivo tocado nesta
         + rodada. `plan.md` precisará de: nova DT substituindo DT-007, e
         +xtensão de DT-003 (CLI, agora com o contrato exato já fixado em
         + `spec.md` `§4.1.1`) e DT-005 (parsing) para os arquivos extern
         +os, e um novo passo no pipeline (§2). `tasks.md` precisará de t
         +asks novas a partir de `T-022` para: leitor de política, leitor
         + de câmbio, campo `moeda` no contrato do item (incluindo a dist
         +inção ausência-de-chave versus `null`), conversão cambial no pi
         +peline, resolução de política por centro de custo (incluindo a 
         +semântica de `periodicidade`), motivo `CATEGORIA_NAO_REEMBOLSAV
         +EL_CENTRO_CUSTO`, motivo `MOEDA_SEM_COTACAO`, três campos novos
         + de saída, validação de `vigencia`, os quatro argumentos de CLI
         + com seus códigos de saída, validação estrutural dos dois arqui
         +vos externos, e os quatro cenários de regressão do `§12` como f
         +ixtures de teste.                                              
      58  
      59  **Tasks afetadas:** nenhuma ainda — `plan.md` e `tasks.md` serã
          o atualizados em etapas posteriores a esta, que é estritamente 
          normativa (`spec.md` e este arquivo).
      60  

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 16 lines, removed 1 line
      71  7. **Exemplo de conversão cambial corrigido.** O exemplo de "mo
          eda estrangeira convertida" em `spec.md` `§4.3` pareava `taxa_c
          ambio_aplicada: 5.93` com `data_cotacao_utilizada: "2026-07-17"
          ` — errado: `5.93` é a cotação de EUR em `2026-07-14`; a cotaçã
          o de `2026-07-17` é `5.96`. Corrigido para `5.96`/`2026-07-17`.
           O cálculo de `e-004` no `§12.3` (`30,00 × 5,96 = 178,80`) já u
          sava o valor correto desde a primeira leitura — o erro estava i
          solado no fragmento ilustrativo de `§4.3`, não nos totais.
      72  8. **Bloco `colaborador`: precisão sobre "regra financeira".** 
          A frase "o bloco não participa de regra financeira alguma" (`§4
          .1`) deixou de ser universalmente verdadeira desde que `centro_
          custo` passou a selecionar política (item 1 desta entrada) — su
          bstituída por uma descrição precisa: `colaborador.id`/`colabora
          dor.nome` continuam sem uso em regra alguma; `colaborador.centr
          o_custo`, quando texto e reconhecido, seleciona a política (RN-
          019); ausente/nulo/inválido/desconhecido usa `padrao`; nenhum d
          efeito do bloco invalida o arquivo.
      73  
      74 -**Ambiguidades e critérios desta correção:** `AMB-034`, `AMB-03
         -5`, `AMB-036`; `CA-041` a `CA-048`. Contagem final: `AMB-001` a
         - `AMB-036` (36), `RN-001` a `RN-022` (22, nenhuma nova além de 
         -`RN-022`), `CA-001` a `CA-048` (48).                           
      74 +**Ambiguidades e critérios desta correção:** `AMB-034`, `AMB-03
         +5`, `AMB-036`; `CA-041` a `CA-048`. Contagem final desta correç
         +ão: `AMB-001` a `AMB-036` (36), `RN-001` a `RN-022` (22, nenhum
         +a nova além de `RN-022`), `CA-001` a `CA-048` (48) — superada p
         +ela segunda correção abaixo.                                   
      75  
      76 +### Ajustes finais da segunda revisão independente (mesma data,
         + `2026-08-05`)                                                 
      77 +                                                               
      78 +Uma segunda revisão independente, posterior à correção acima, e
         +ncontrou pontos adicionais de precisão de contrato e uma lacuna
         + no vocabulário de motivos de teto. Nenhum deles altera um tota
         +l financeiro já registrado nesta entrada (R$585,43 / R$351,43 /
         + R$1.143,26 / R$373,76 permanecem exatamente como acima); todos
         + são correções de exatidão normativa ou fechamento de vocabulár
         +io.                                                            
      79 +                                                               
      80 +1. **Critérios históricos passaram a declarar a política extern
         +a usada.** `CA-001`, `CA-002`, `CA-004` a `CA-009`, `CA-013`, `
         +CA-016` e `CA-018`, os aceites de `RN-011` a `RN-015` e os caso
         +s de borda de `spec.md` `§7` que usam `60`, `80`, `250` ou `100
         +` agora declaram explicitamente que esses valores vêm da políti
         +ca externa histórica/padrão usada no cenário — não são constant
         +es internas do sistema. `RN-009` foi atualizada com a mesma pre
         +cisão sobre `nota_fiscal_obrigatoria_acima_de`.                
      81 +2. **`CA-026` foi alinhado a `AMB-021` e `RN-019`.** A redação 
         +anterior afirmava que `representacao` fora de `CC-COMERCIAL` se
         +mpre recebe `CATEGORIA_FORA_POLITICA` — contradizia a régua de 
         +tabela única de `RN-019`/`AMB-021`, que distingue `padrao` (sem
         + `representacao` → `CATEGORIA_FORA_POLITICA`) de um centro cada
         +strado que não a declara (→ `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_
         +CUSTO`). Corrigido.                                            
      82 +3. **Limite zero em `padrao` passou a invalidar o arquivo de po
         +lítica.** O contrato de `4.1.1` (`AMB-035`) permitia `limite: 0
         +` em qualquer tabela. Fechado: em `padrao`, todo `limite` deve 
         +ser estritamente maior que zero — `padrao` é o fallback univers
         +al, e um teto zero ali retiraria uma categoria de todo centro d
         +e custo desconhecido sem que o comunicado expresse essa decisão
         + dessa forma; `limite: 0` em `padrao` torna o arquivo de políti
         +ca estruturalmente inválido (RN-022), falha global, código de s
         +aída `2`. Em `centros_custo`, `limite: 0` continua sendo decisã
         +o válida do próprio centro (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_
         +CUSTO`). Nenhum motivo de item novo foi criado para o caso de `
         +padrao` — a falha é do arquivo inteiro, antes de qualquer item 
         +ser avaliado.                                                  
      83 +4. **`AMB-037` (nova) resolveu categoria externa com `periodici
         +dade: "diaria"` fora de `hospedagem`.** `AMB-036` já permitia q
         +ualquer categoria com `periodicidade: "diaria"`, mas o único mo
         +tivo de teto individual existente, `TETO_HOSPEDAGEM_APLICADO`, 
         +é semanticamente específico de hospedagem.                     
      84 +5. **Periodicidade passou a governar o mecanismo, não mais o no
         +me histórico da categoria.** `RN-011` a `RN-015` foram reescrit
         +as para que o mecanismo — teto compartilhado por data, ou teto 
         +individual por lançamento — decorra de `periodicidade` na tabel
         +a de política efetivamente aplicável; o comportamento histórico
         + de `alimentacao`/`transporte_urbano` (compartilhado) e `hosped
         +agem` (individual) continua valendo porque é o que a política p
         +adrão declara, não porque o nome da categoria é especial.      
      85 +6. **Foi criado o motivo `TETO_INDIVIDUAL_APLICADO`** (contrato
         + completo em `AMB-037`: `codigo: TETO_INDIVIDUAL_APLICADO`, `re
         +gra: RN-019`, `campo: null`), para qualquer categoria diferente
         + de `hospedagem` com `periodicidade: "diaria"` cujo valor norma
         +lizado exceda o limite individual.                             
      86 +7. **Foi criado `CA-049`**, cenário de `estacionamento` (`limit
         +e` R$50,00, `periodicidade: "diaria"`, despesa elegível de R$80
         +,00, `PARCIALMENTE_REEMBOLSADO`, `valor_reembolsavel` R$50,00, 
         +motivo `TETO_INDIVIDUAL_APLICADO`).                            
      87 +8. **Os quatro totais financeiros já registrados nesta entrada 
         +permaneceram inalterados** — R$585,43 (baseline histórica), R$3
         +51,43 (exemplo antigo sob v4), R$1.143,26 (Rafael) e R$373,76 (
         +Dani) — nenhuma das correções acima altera cálculo de item ou d
         +e total já publicado.                                          
      88 +                                                               
      89 +**Ambiguidades e critérios desta segunda correção:** `AMB-037`;
         + `CA-049`. Contagem final: `AMB-001` a `AMB-037` (37), `RN-001`
         + a `RN-022` (22, nenhuma nova), `CA-001` a `CA-049` (49).      
      90 +                                                               
      91  ---
      92  
      93  ## D-002 — Ajustes de contrato após auditoria independente · `2
          026-07-30`

● Now let's run the full verification suite.

  Ran 1 shell command (ctrl+o to expand)

● Only the two authorized files are modified, exemplos/envelope/ remains
  untracked/intact, and no other file changed. Now let's verify final counts and
  the four totals are unchanged.

  Ran 2 shell commands (ctrl+o to expand)

● All four totals confirmed unchanged everywhere. Let's do a final diff review
  of both files to eyeball for correctness before reporting.

  Ran 1 shell command (ctrl+o to expand)

● Version stays 1.2, status em revisão. All checks pass. Here's the summary you
  asked for.

  Contagens finais (verificadas via grep, não declaradas de memória):
  - AMB-001 a AMB-037 = 37
  - RN-001 a RN-022 = 22 (nenhuma nova)
  - CA-001 a CA-049 = 49

  AMB-037 — Motivo de limitação para categoria externa com periodicidade
  individual · U. Decisão: cria o motivo TETO_INDIVIDUAL_APLICADO para qualquer
  categoria diferente de hospedagem, presente na tabela aplicável com limite > 0
  e periodicidade: "diaria", cujo valor normalizado exceda o limite —
  reembolsado até o limite. TETO_HOSPEDAGEM_APLICADO permanece exclusivo de
  hospedagem.

  Contrato de TETO_INDIVIDUAL_APLICADO: codigo: TETO_INDIVIDUAL_APLICADO, regra:
  RN-019, campo: null, decisao: PARCIALMENTE_REEMBOLSADO.

  CA-049 — cenário estacionamento, limite: 50.00, periodicidade: "diaria",
  despesa elegível de R$80,00 → PARCIALMENTE_REEMBOLSADO, valor_reembolsavel: 
  50,00, motivo TETO_INDIVIDUAL_APLICADO/RN-019/campo: null.

  CA-026 substituído pelo texto literal fornecido, alinhando representacao a
  AMB-021/RN-019 (tabela padrao sem a categoria → CATEGORIA_FORA_POLITICA;
  centro cadastrado sem a categoria → CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO).

  Limite zero em padrao: fechado o contrato — padrao exige limite estritamente >
  0 (limite 0,00 ali invalida o arquivo de política inteiro, código de saída 2,
  RN-022); centros_custo mantém limite ≥ 0 com 0,00 =
  CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO. Nenhum motivo de item novo criado
  para o caso padrao. Atualizado em 4.1.1, RN-019, RN-022, AMB-035, CA-045,
  casos de borda (§7), rastreabilidade (§9) e D-003.

  Critérios históricos qualificados: CA-001/002 (declaram a política externa de
  teste e os quatro valores), CA-004 a CA-007, CA-008/009 (gatilho de nota como
  dado de política, não constante), CA-013 (elegibilidade prévia à duplicidade),
  CA-016 (motivo restrito a centro desconhecido/padrao), CA-018, mais
  RN-014/015 (aceites) e §7 (nota prefaciando a tabela).

  git status --short:
   M specs/001-motor-reembolso/DECISIONS.md
   M specs/001-motor-reembolso/spec.md
  ?? exemplos/envelope/

  Somente spec.md e DECISIONS.md modificados; exemplos/envelope/ intacto;
  plan.md/tasks.md/README/código/testes sem alteração; nenhum /export, git add,
  commit ou push executado. git diff --check só mostra os avisos de CRLF/LF
  esperados no Windows, sem erros reais. Os quatro totais (585.43 / 351.43 /
  1143.26 / 373.76) permanecem idênticos em ambos os arquivos.

  Parando para revisão.

✻ Cooked for 10m 48s

※ recap: We're normalizing the reimbursement engine spec (v1.2) after two independent reviews, and I just finished applying all the corrections plus verification. Next: waiting for your go-ahead to review the changes before anything else happens. (disable recaps in /config)

> Faça uma correção final de consistência interna, sem criar decisão nova.      
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  * `specs/001-motor-reembolso/spec.md`                                         
  * `specs/001-motor-reembolso/DECISIONS.md`                                    
                                                                                
  Não altere nenhum outro arquivo.                                              
                                                                                
  Não crie nem renumere AMB, RN ou CA.                                          
                                                                                
  As contagens permanecem:                                                      
                                                                                
  ```text                                                                       
  AMB-001 a AMB-037 = 37                                                        
  RN-001 a RN-022 = 22                                                          
  CA-001 a CA-049 = 49                                                          
  ```                                                                           
                                                                                
  A versão permanece `1.2` e o status permanece `em revisão`.                   
                                                                                
  Os quatro totais permanecem:                                                  
                                                                                
  ```text                                                                       
  585.43                                                                        
  351.43                                                                        
  1143.26                                                                       
  373.76                                                                        
  ```                                                                           
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  ## 1. Corrigir RN-020 e 8.4, item 14                                          
                                                                                
  A ausência de cotação torna `valor_normalizado` incalculável, mas não impede  
  regras independentes do valor.                                                
                                                                                
  Substitua a afirmação ampla:                                                  
                                                                                
  ```text                                                                       
  nenhuma regra financeira posterior (RN-006 em diante) é avaliada              
  ```                                                                           
                                                                                
  por comportamento fechado:                                                    
                                                                                
  * `MOEDA_SEM_COTACAO` mantém `valor_normalizado: null`;                       
  * não avaliar RN-006, RN-009, RN-010 nem qualquer teto, pois dependem do      
  valor normalizado calculável;                                                 
  * continuar avaliando a elegibilidade da categoria pela tabela aplicável —    
  RN-007 e a parte correspondente de RN-019 — quando `categoria` e              
  `centro_custo` estiverem utilizáveis;                                         
  * continuar avaliando RN-008 quando `despesa.data` e o período estiverem      
  válidos;                                                                      
  * o mesmo item pode, portanto, apresentar `MOEDA_SEM_COTACAO` junto de        
  `CATEGORIA_FORA_POLITICA`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou     
  `FORA_COMPETENCIA`;                                                           
  * não avaliar duplicidade ou tetos porque o componente financeiro da chave e  
  do corte é incalculável.                                                      
                                                                                
  Aplique a mesma correção em 8.4, item 14.                                     
                                                                                
  Não altere a ordem de apresentação dos motivos.                               
                                                                                
  ## 2. Corrigir RN-013                                                         
                                                                                
  Substitua a afirmação absoluta:                                               
                                                                                
  ```text                                                                       
  Hospedagem não participa da agregação por data                                
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  Quando `hospedagem.periodicidade` for `"diaria"`, cada lançamento é avaliado  
  isoladamente e não participa da agregação por data. Se uma política válida    
  declarar `hospedagem.periodicidade = "dia"`, aplica-se o mecanismo            
  compartilhado de RN-019/RN-015, com `TETO_DIARIO_APLICADO` ou                 
  `TETO_DIARIO_ESGOTADO` conforme o saldo.                                      
  ```                                                                           
                                                                                
  Mantenha `TETO_HOSPEDAGEM_APLICADO` exclusivo de hospedagem configurada como  
  `"diaria"`.                                                                   
                                                                                
  ## 3. Corrigir a matriz 8.2                                                   
                                                                                
  Atualize as dependências financeiras para considerar `valor_normalizado`      
  calculável.                                                                   
                                                                                
  Use uma redação equivalente a:                                                
                                                                                
  | Regra                      | Dependências                                   
                                                |                               
  | -------------------------- | ---------------------------------------------- 
  ---------------------------------------------- |                              
  | Valor não positivo         | `valor_normalizado` calculável                 
                                                |                               
  | Nota fiscal obrigatória    | `valor_normalizado` calculável e               
  `despesa.tem_nota_fiscal`                                   |                 
  | Duplicidade econômica      | data, categoria normalizada, moeda,            
  `valor_normalizado` calculável, fornecedor e descrição   |                    
  | Teto compartilhado `"dia"` | data, categoria, `valor_normalizado`           
  calculável, centro de custo e periodicidade da política |                     
  | Teto individual `"diaria"` | categoria, `valor_normalizado` calculável,     
  centro de custo e periodicidade da política       |                           
                                                                                
  Substitua:                                                                    
                                                                                
  ```text                                                                       
  Teto individual de hospedagem                                                 
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  Teto individual de periodicidade `"diaria"`                                   
  ```                                                                           
                                                                                
  Deixe explícito que isso inclui:                                              
                                                                                
  * hospedagem → `TETO_HOSPEDAGEM_APLICADO`;                                    
  * outra categoria → `TETO_INDIVIDUAL_APLICADO`.                               
                                                                                
  ## 4. Corrigir a associação de `TETO_DIARIO_APLICADO`                         
                                                                                
  Na tabela de motivos, use:                                                    
                                                                                
  ```text                                                                       
  RN-011 quando `alimentacao` estiver configurada com `"dia"`;                  
  RN-012 quando `transporte_urbano` estiver configurado com `"dia"`;            
  RN-019 para qualquer outro caso de periodicidade `"dia"`, incluindo           
  categorias externas e categorias históricas configuradas com mecanismo        
  diferente do histórico.                                                       
  ```                                                                           
                                                                                
  Exemplos cobertos por RN-019:                                                 
                                                                                
  * `representacao` com `"dia"`;                                                
  * `hospedagem` com `"dia"`.                                                   
                                                                                
  ## 5. Corrigir 8.4, item 15                                                   
                                                                                
  Qualifique limite zero:                                                       
                                                                                
  ```text                                                                       
  Limite igual a `0,00` dentro da tabela de um centro de custo cadastrado nunca 
  produz `PARCIALMENTE_REEMBOLSADO`: o item é recusado com                      
  `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`.                                    
  ```                                                                           
                                                                                
  Acrescente na mesma linha ou no item seguinte:                                
                                                                                
  ```text                                                                       
  Limite igual a `0,00` dentro de `padrao` não chega à avaliação do item:       
  invalida o arquivo de política inteiro, com código de saída `2`.              
  ```                                                                           
                                                                                
  ## 6. Corrigir DECISIONS.md                                                   
                                                                                
  Em D-003:                                                                     
                                                                                
  ### Contagem de regras atualizadas                                            
                                                                                
  Substitua:                                                                    
                                                                                
  ```text                                                                       
  §5 com onze RN atualizadas e quatro novas                                     
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  §5 com doze RN atualizadas e quatro novas                                     
  ```                                                                           
                                                                                
  As doze atualizadas são:                                                      
                                                                                
  ```text                                                                       
  RN-002, RN-004, RN-005, RN-007, RN-009, RN-010,                               
  RN-011, RN-012, RN-013, RN-014, RN-015 e RN-017                               
  ```                                                                           
                                                                                
  ### Resumo de limite zero                                                     
                                                                                
  No item inicial sobre política por centro de custo, substitua a frase         
  genérica por:                                                                 
                                                                                
  ```text                                                                       
  limite explicitamente `0,00` em uma tabela de centro cadastrado recusa a      
  categoria com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`; limite `0,00` em     
  `padrao` invalida estruturalmente o arquivo de política                       
  ```                                                                           
                                                                                
  ### Registro da conferência                                                   
                                                                                
  Ao final da subseção `Ajustes finais da segunda revisão independente`,        
  acrescente um parágrafo curto:                                                
                                                                                
  ```text                                                                       
  Uma conferência final de consistência alinhou RN-020 e a matriz de            
  dependências ao tratamento de `MOEDA_SEM_COTACAO`, tornou a redação de RN-013 
  condicional à periodicidade configurada, generalizou as linhas de teto        
  diário e individual e qualificou os dois comportamentos distintos de limite   
  zero. Nenhuma decisão, ambiguidade, regra, critério ou total foi alterado.    
  ```                                                                           
                                                                                
  ## 7. Verificação                                                             
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * somente `spec.md` e `DECISIONS.md` estão modificados;                       
  * `exemplos/envelope/` permanece novo e intacto;                              
  * nenhum outro arquivo foi alterado;                                          
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Ao final, apresente somente:                                                  
                                                                                
  1. os trechos corrigidos;                                                     
  2. o `git status --short`;                                                    
  3. a confirmação de que as contagens e os quatro totais não mudaram.          
                                                                                
  Pare para revisão.                                                            

  Ran 3 shell commands (ctrl+o to expand)

● Now I have all anchors. Let's apply edit 1: RN-020 and 8.4 item 14.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      558  
      559  ### RN-020 — Resolução de câmbio e conversão monetária
      560  
      561 -**Regra:** `despesa.moeda` igual a `"BRL"`, ou com a chave int
          -eiramente ausente do objeto (RN-002), tem taxa implícita `1` e
          - não consulta a tabela de câmbio. `despesa.moeda: null` nunca 
          -chega a esta regra — é `CAMPO_AUSENTE` (RN-002), recusado ante
          -s da resolução de câmbio. Para moeda estrangeira estruturalmen
          -te válida, procura-se primeiro a cotação exata na data da desp
          -esa; sem cotação exata, usa-se a cotação mais recente cuja dat
          -a seja **anterior** à data da despesa — nunca uma cotação futu
          -ra, e sem interpolação entre datas. Se não existir cotação exa
          -ta nem anterior utilizável para aquela moeda — porque ela não 
          -aparece em `cambio.json` em nenhuma data, ou porque sua primei
          -ra cotação disponível é posterior à data da despesa —, o item 
          -é recusado com `MOEDA_SEM_COTACAO`; `valor_normalizado` perman
          -ece nulo, e nenhuma regra financeira posterior (RN-006 em dian
          -te) é avaliada. A conversão multiplica o valor bruto pela taxa
          - resolvida; o arredondamento para duas casas (RN-004) acontece
          - uma única vez, sobre o resultado dessa multiplicação.        
      561 +**Regra:** `despesa.moeda` igual a `"BRL"`, ou com a chave int
          +eiramente ausente do objeto (RN-002), tem taxa implícita `1` e
          + não consulta a tabela de câmbio. `despesa.moeda: null` nunca 
          +chega a esta regra — é `CAMPO_AUSENTE` (RN-002), recusado ante
          +s da resolução de câmbio. Para moeda estrangeira estruturalmen
          +te válida, procura-se primeiro a cotação exata na data da desp
          +esa; sem cotação exata, usa-se a cotação mais recente cuja dat
          +a seja **anterior** à data da despesa — nunca uma cotação futu
          +ra, e sem interpolação entre datas. Se não existir cotação exa
          +ta nem anterior utilizável para aquela moeda — porque ela não 
          +aparece em `cambio.json` em nenhuma data, ou porque sua primei
          +ra cotação disponível é posterior à data da despesa —, o item 
          +é recusado com `MOEDA_SEM_COTACAO` e `valor_normalizado` perma
          +nece nulo. Isso impede especificamente as regras que dependem 
          +de `valor_normalizado` calculável — RN-006 (valor não positivo
          +), RN-009 (nota fiscal), RN-010 (duplicidade) e qualquer teto 
          +(RN-011 a RN-015, RN-019) —, mas **não** impede as regras cujo
          +s campos de dependência permanecem utilizáveis: a elegibilidad
          +e de categoria pela tabela aplicável (RN-007 e a parte corresp
          +ondente de RN-019, quando `categoria` e `centro_custo` estiver
          +em estruturalmente válidos) e a elegibilidade temporal (RN-008
          +, quando `despesa.data` e o período estiverem válidos) continu
          +am sendo avaliadas normalmente. Um mesmo item pode, portanto, 
          +apresentar `MOEDA_SEM_COTACAO` junto de `CATEGORIA_FORA_POLITI
          +CA`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMP
          +ETENCIA` — mas nunca junto de `VALOR_NAO_POSITIVO`, `NOTA_FISC
          +AL_AUSENTE`, `DUPLICIDADE` ou de um motivo de teto, porque ess
          +as dependem do componente financeiro incalculável. A conversão
          + multiplica o valor bruto pela taxa resolvida; o arredondament
          +o para duas casas (RN-004) acontece uma única vez, sobre o res
          +ultado dessa multiplicação.                                   
      562  **Origem:** comunicado do RH, política v4, item B (Dia 2).
      563  **Aceite:** EUR `30,00` datado num sábado sem cotação própria 
           usa a cotação do último dia útil anterior disponível — ex.: `2
           026-07-17` para uma despesa de `2026-07-18`. GBP sem nenhuma e
           ntrada em `cambio.json`, em qualquer data, é recusado com `MOE
           DA_SEM_COTACAO`. USD `40,00` × `5,50` normaliza para `220,00`,
            com um único arredondamento.
      564  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1034  11. Item recusado nas validações individuais não participa da
             detecção de duplicidade econômica.
      1035  12. Ocorrência recusada como duplicidade econômica não partic
            ipa da agregação e não consome teto.
      1036  13. Regras de teto alcançam somente os itens que permanecerem
             elegíveis após todas as validações e a deduplicação.
      1037 -14. Item com `MOEDA_SEM_COTACAO` não participa de nenhuma reg
           -ra financeira posterior (RN-006 em diante), porque `valor_nor
           -malizado` é nulo — mesmo tratamento de dependência de campo e
           -struturalmente inválido (item 3 acima), agora por ausência de
           - dado externo em vez de defeito de contrato (política v4, Dia
           - 2).                                                         
      1037 +14. Item com `MOEDA_SEM_COTACAO` tem `valor_normalizado` nulo
           + e por isso não participa das regras que dependem dele — RN-0
           +06, RN-009, RN-010 e qualquer teto (RN-011 a RN-015, RN-019) 
           +—, mesmo tratamento de dependência de campo estruturalmente i
           +nválido (item 3 acima), agora por ausência de dado externo em
           + vez de defeito de contrato (política v4, Dia 2). A elegibili
           +dade de categoria (RN-007, RN-019) e a elegibilidade temporal
           + (RN-008) não dependem de `valor_normalizado` e continuam sen
           +do avaliadas normalmente quando seus próprios campos são util
           +izáveis — um item com `MOEDA_SEM_COTACAO` pode apresentar, ju
           +nto dele, `CATEGORIA_FORA_POLITICA`, `CATEGORIA_NAO_REEMBOLSA
           +VEL_CENTRO_CUSTO` e/ou `FORA_COMPETENCIA`.                   
      1038  15. Limite de política igual a `0,00` nunca produz `PARCIALME
            NTE_REEMBOLSADO`: o item é recusado com `CATEGORIA_NAO_REEMBO
            LSAVEL_CENTRO_CUSTO` antes de qualquer teto ser avaliado — nã
            o é uma variante de RN-013/RN-014 com teto zero (política v4,
             Dia 2).
      1039  16. Arquivo de política ou de câmbio estruturalmente inválido
             impede toda a apuração, inclusive de despesas em BRL que não
             dependeriam de câmbio algum — o erro é do arquivo externo, n
            ão do item (política v4, Dia 2, RN-022).
      1040  

● Now edit 2: RN-013.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      502  
      503  ### RN-013 — Limite individual de hospedagem
      504  
      505 -**Regra:** `TETO_HOSPEDAGEM_APLICADO` aplica-se a `hospedagem`
          - quando a tabela de política efetivamente aplicável declara `h
          -ospedagem.periodicidade = "diaria"` — o caso normal, inclusive
          - na política padrão. O limite em si é definido pela política a
          -plicável ao centro de custo do colaborador (RN-019) — R$ 250,0
          -0 por lançamento na política padrão —, aplicável somente quand
          -o esse limite for maior que zero (limite igual a `0,00` é RN-0
          -19, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não este teto).
          - Hospedagem **não** participa da agregação por data: cada lanç
          -amento é avaliado isoladamente. O conteúdo da descrição não al
          -tera o teto.                                                  
      505 +**Regra:** `TETO_HOSPEDAGEM_APLICADO` aplica-se a `hospedagem`
          + quando a tabela de política efetivamente aplicável declara `h
          +ospedagem.periodicidade = "diaria"` — o caso normal, inclusive
          + na política padrão. O limite em si é definido pela política a
          +plicável ao centro de custo do colaborador (RN-019) — R$ 250,0
          +0 por lançamento na política padrão —, aplicável somente quand
          +o esse limite for maior que zero (limite igual a `0,00` é RN-0
          +19, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não este teto).
          + Quando `hospedagem.periodicidade` for `"diaria"`, cada lançam
          +ento é avaliado isoladamente e não participa da agregação por 
          +data. Se uma política válida declarar `hospedagem.periodicidad
          +e = "dia"`, aplica-se o mecanismo compartilhado de RN-019/RN-0
          +15, com `TETO_DIARIO_APLICADO` ou `TETO_DIARIO_ESGOTADO` confo
          +rme o saldo — `TETO_HOSPEDAGEM_APLICADO` permanece exclusivo d
          +e hospedagem configurada como `"diaria"`. O conteúdo da descri
          +ção não altera o teto.                                        
      506  **Origem:** política do RH, item 3; valor e mecanismo parametr
           izados por centro de custo pela política v4 (Dia 2, RN-019, AM
           B-036).
      507  **Aceite:** sob a política padrão, lançamento de R$ 480,00 des
           crito como "2 diarias" rende R$ 250,00. Alterar o texto da `de
           scricao` desse lançamento não altera o teto individual aplicad
           o, que permanece R$ 250,00. Dois lançamentos de hospedagem na 
           mesma data podem render até R$ 500,00 sob a política padrão.
      508  
● Now edit 3: matriz 8.2. Let me view the current table precisely.

  Ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines, removed 5 lines
       992  | Resolução de câmbio / conversão monetária | `despesa.valor`
            , `despesa.moeda`, `despesa.data` |
       993  | Normalização monetária | `despesa.valor` (já convertido, qu
            ando aplicável) |
       994  | Normalização de categoria | `despesa.categoria` |
       995 -| Valor não positivo | `despesa.valor` |                     
       995 +| Valor não positivo | `valor_normalizado` calculável |      
       996  | Categoria fora da política / política por centro de custo |
             `despesa.categoria`, `colaborador.centro_custo` |
       997  | Competência | `despesa.data`, `periodo.inicio`, `periodo.fi
            m` |
       998 -| Nota fiscal obrigatória | `despesa.valor`, `despesa.tem_not
           -a_fiscal` |                                                  
       999 -| Duplicidade econômica | `despesa.data`, `despesa.categoria`
           -, `despesa.valor`, `despesa.moeda`, `despesa.fornecedor`, `de
           -spesa.descricao` |                                           
      1000 -| Agregação e teto diário | `despesa.data`, `despesa.categori
           -a`, `despesa.valor`, `colaborador.centro_custo` |            
      1001 -| Teto individual de hospedagem | `despesa.categoria`, `despe
           -sa.valor`, `colaborador.centro_custo` |                      
       998 +| Nota fiscal obrigatória | `valor_normalizado` calculável e 
           +`despesa.tem_nota_fiscal` |                                  
       999 +| Duplicidade econômica | `despesa.data`, `despesa.categoria`
           + normalizada, `despesa.moeda`, `valor_normalizado` calculável
           +, `despesa.fornecedor`, `despesa.descricao` |                
      1000 +| Teto compartilhado `"dia"` | `despesa.data`, `despesa.categ
           +oria`, `valor_normalizado` calculável, `colaborador.centro_cu
           +sto` e a `periodicidade` declarada na política aplicável |   
      1001 +| Teto individual `"diaria"` | `despesa.categoria`, `valor_no
           +rmalizado` calculável, `colaborador.centro_custo` e a `period
           +icidade` declarada na política aplicável — inclui `hospedagem
           +` (`TETO_HOSPEDAGEM_APLICADO`) e qualquer outra categoria sob
           + esse mecanismo (`TETO_INDIVIDUAL_APLICADO`) |               
      1002  
      1003  ### 8.3 Ordem de apresentação dos motivos
      1004  

● Now edit 4: TETO_DIARIO_APLICADO association in §4.5.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      355  
      356  | Código | Regra associada | `campo` | Aparece em |
      357  |---|---|---|---|
      358 -| `TETO_DIARIO_APLICADO` | RN-011 (alimentação), RN-012 (trans
          -porte) ou RN-019 (categoria de periodicidade `"dia"` introduzi
          -da exclusivamente pela política externa, ex.: `representacao`)
          - | nulo | `PARCIALMENTE_REEMBOLSADO` |                        
      358 +| `TETO_DIARIO_APLICADO` | RN-011 quando `alimentacao` estiver
          + configurada com `periodicidade: "dia"`; RN-012 quando `transp
          +orte_urbano` estiver configurado com `periodicidade: "dia"`; R
          +N-019 para qualquer outro caso de `periodicidade: "dia"`, incl
          +uindo categorias externas (ex.: `representacao`) e categorias 
          +históricas configuradas com mecanismo diferente do histórico (
          +ex.: `hospedagem` com `"dia"`) | nulo | `PARCIALMENTE_REEMBOLS
          +ADO` |                                                        
      359  | `TETO_DIARIO_ESGOTADO` | RN-015 | nulo | `NAO_REEMBOLSADO_TE
           TO_ESGOTADO` |
      360  | `TETO_HOSPEDAGEM_APLICADO` | RN-013 | nulo | `PARCIALMENTE_R
           EEMBOLSADO` de hospedagem |
      361  | `TETO_INDIVIDUAL_APLICADO` | RN-019 (categoria de periodicid
           ade `"diaria"` diferente de `hospedagem`, ex.: `estacionamento
           ` — AMB-037) | nulo | `PARCIALMENTE_REEMBOLSADO` |

● Now edit 5: 8.4 item 15.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1035  12. Ocorrência recusada como duplicidade econômica não partic
            ipa da agregação e não consome teto.
      1036  13. Regras de teto alcançam somente os itens que permanecerem
             elegíveis após todas as validações e a deduplicação.
      1037  14. Item com `MOEDA_SEM_COTACAO` tem `valor_normalizado` nulo
             e por isso não participa das regras que dependem dele — RN-0
            06, RN-009, RN-010 e qualquer teto (RN-011 a RN-015, RN-019) 
            —, mesmo tratamento de dependência de campo estruturalmente i
            nválido (item 3 acima), agora por ausência de dado externo em
             vez de defeito de contrato (política v4, Dia 2). A elegibili
            dade de categoria (RN-007, RN-019) e a elegibilidade temporal
             (RN-008) não dependem de `valor_normalizado` e continuam sen
            do avaliadas normalmente quando seus próprios campos são util
            izáveis — um item com `MOEDA_SEM_COTACAO` pode apresentar, ju
            nto dele, `CATEGORIA_FORA_POLITICA`, `CATEGORIA_NAO_REEMBOLSA
            VEL_CENTRO_CUSTO` e/ou `FORA_COMPETENCIA`.
      1038 -15. Limite de política igual a `0,00` nunca produz `PARCIALME
           -NTE_REEMBOLSADO`: o item é recusado com `CATEGORIA_NAO_REEMBO
           -LSAVEL_CENTRO_CUSTO` antes de qualquer teto ser avaliado — nã
           -o é uma variante de RN-013/RN-014 com teto zero (política v4,
           - Dia 2).                                                     
      1038 +15. Limite igual a `0,00` dentro da tabela de um centro de cu
           +sto cadastrado nunca produz `PARCIALMENTE_REEMBOLSADO`: o ite
           +m é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` an
           +tes de qualquer teto ser avaliado — não é uma variante de RN-
           +013/RN-014 com teto zero (política v4, Dia 2). Limite igual a
           + `0,00` dentro de `padrao` não chega sequer à avaliação do it
           +em: invalida o arquivo de política inteiro, com código de saí
           +da `2` (RN-019, RN-022, correção da segunda revisão).        
      1039  16. Arquivo de política ou de câmbio estruturalmente inválido
             impede toda a apuração, inclusive de despesas em BRL que não
             dependeriam de câmbio algum — o erro é do arquivo externo, n
            ão do item (política v4, Dia 2, RN-022).
      1040  
      1041  **Exemplo de motivos múltiplos:** item de R$ 500,00, categori
            a `coworking`, sem nota, datado fora da janela, de um colabor
            ador de centro de custo desconhecido ou ausente (tabela aplic
            ável `padrao`, que não declara `coworking`), com todos os cam
            pos estruturalmente válidos, apresenta três motivos — `CATEGO
            RIA_FORA_POLITICA`, `FORA_COMPETENCIA` e `NOTA_FISCAL_AUSENTE
            `.

● Now edit 6: DECISIONS.md — RN count phrase, limite-zero summary phrase, and
  the new closing paragraph.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      54  
      55  **Item C fora de escopo.** A fila de aprovação manual (`AGUARDA
          NDO_APROVACAO` para itens acima de R$500) não foi especificada 
          nesta rodada — é explicitamente opcional no próprio comunicado 
          do RH, e nenhum item dos dois arquivos de exemplo do envelope u
          ltrapassa R$500 sob a interpretação adotada, então nada nos dad
          os obriga essa decisão agora (`AMB-033`).
      56  
      57 -**Custo documental e técnico previsto:** esta entrada e a atual
         -ização de `spec.md` (cabeçalho, `§0` nova, `§3`, `§4.1`/`4.1.1`
         - nova e depois reescrita nas duas correções, `§4.2`, `§4.3`, `§
         -4.5`, `§4.7`, `§5` com onze RN atualizadas e quatro novas — vár
         -ias delas corrigidas de novo nas revisões —, `§6` com dezenove 
         -AMB novas, `§7`, `§8.1` a `8.5`, `§9`, `§10` com vinte e seis C
         -A novas, `§11`, `§12` nova) — nenhum outro arquivo tocado nesta
         - rodada. `plan.md` precisará de: nova DT substituindo DT-007, e
         -xtensão de DT-003 (CLI, agora com o contrato exato já fixado em
         - `spec.md` `§4.1.1`) e DT-005 (parsing) para os arquivos extern
         -os, e um novo passo no pipeline (§2). `tasks.md` precisará de t
         -asks novas a partir de `T-022` para: leitor de política, leitor
         - de câmbio, campo `moeda` no contrato do item (incluindo a dist
         -inção ausência-de-chave versus `null`), conversão cambial no pi
         -peline, resolução de política por centro de custo (incluindo a 
         -semântica de `periodicidade`), motivo `CATEGORIA_NAO_REEMBOLSAV
         -EL_CENTRO_CUSTO`, motivo `MOEDA_SEM_COTACAO`, três campos novos
         - de saída, validação de `vigencia`, os quatro argumentos de CLI
         - com seus códigos de saída, validação estrutural dos dois arqui
         -vos externos, e os quatro cenários de regressão do `§12` como f
         -ixtures de teste.                                              
      57 +**Custo documental e técnico previsto:** esta entrada e a atual
         +ização de `spec.md` (cabeçalho, `§0` nova, `§3`, `§4.1`/`4.1.1`
         + nova e depois reescrita nas duas correções, `§4.2`, `§4.3`, `§
         +4.5`, `§4.7`, `§5` com doze RN atualizadas e quatro novas (as d
         +oze atualizadas: `RN-002`, `RN-004`, `RN-005`, `RN-007`, `RN-00
         +9`, `RN-010`, `RN-011`, `RN-012`, `RN-013`, `RN-014`, `RN-015` 
         +e `RN-017`) — várias delas corrigidas de novo nas revisões —, `
         +§6` com dezenove AMB novas, `§7`, `§8.1` a `8.5`, `§9`, `§10` c
         +om vinte e seis CA novas, `§11`, `§12` nova) — nenhum outro arq
         +uivo tocado nesta rodada. `plan.md` precisará de: nova DT subst
         +ituindo DT-007, extensão de DT-003 (CLI, agora com o contrato e
         +xato já fixado em `spec.md` `§4.1.1`) e DT-005 (parsing) para o
         +s arquivos externos, e um novo passo no pipeline (§2). `tasks.m
         +d` precisará de tasks novas a partir de `T-022` para: leitor de
         + política, leitor de câmbio, campo `moeda` no contrato do item 
         +(incluindo a distinção ausência-de-chave versus `null`), conver
         +são cambial no pipeline, resolução de política por centro de cu
         +sto (incluindo a semântica de `periodicidade`), motivo `CATEGOR
         +IA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, motivo `MOEDA_SEM_COTACAO`, 
         +três campos novos de saída, validação de `vigencia`, os quatro 
         +argumentos de CLI com seus códigos de saída, validação estrutur
         +al dos dois arquivos externos, e os quatro cenários de regressã
         +o do `§12` como fixtures de teste.                             
      58  
      59  **Tasks afetadas:** nenhuma ainda — `plan.md` e `tasks.md` serã
          o atualizados em etapas posteriores a esta, que é estritamente 
          normativa (`spec.md` e este arquivo).
      60  

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      39  
      40  **Decisões dos quatro grupos** (política por centro de custo; c
          âmbio; conversão e regras financeiras; saída e vigência):
      41  
      42 -- **Política por centro de custo:** `padrao` como fallback inte
         -gral; centro cadastrado usa só suas categorias declaradas, sem 
         -fallback por categoria; limite zero em centro cadastrado recusa
         - a categoria (não corta a zero); limite zero em `padrao` invali
         -da o arquivo de política inteiro, código de saída `2` (correção
         - da segunda revisão); comparação de `centro_custo` é textual e 
         -exata.                                                         
      42 +- **Política por centro de custo:** `padrao` como fallback inte
         +gral; centro cadastrado usa só suas categorias declaradas, sem 
         +fallback por categoria; limite explicitamente `0,00` em uma tab
         +ela de centro cadastrado recusa a categoria com `CATEGORIA_NAO_
         +REEMBOLSAVEL_CENTRO_CUSTO`; limite `0,00` em `padrao` invalida 
         +estruturalmente o arquivo de política (código de saída `2`, cor
         +reção da segunda revisão); comparação de `centro_custo` é textu
         +al e exata.                                                    
      43  - **Câmbio:** cotação exata ou a mais recente anterior; nunca f
          utura; sem interpolação; sem cotação utilizável recusa o item.
      44  - **Conversão e regras financeiras:** um único arredondamento a
          pós a multiplicação; nota fiscal e duplicidade operam sobre o v
          alor convertido (duplicidade com moeda como componente adiciona
          l da chave, não substituindo o valor).
      45  - **Saída e vigência:** três campos de auditoria novos na saída
          ; vigência obrigatória e validada, mas informativa quanto a ite
          ns individuais.

● Now append the closing paragraph at the end of "Ajustes finais da segunda
  revisão independente" subsection:

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 2 lines
      88  
      89  **Ambiguidades e critérios desta segunda correção:** `AMB-037`;
           `CA-049`. Contagem final: `AMB-001` a `AMB-037` (37), `RN-001`
           a `RN-022` (22, nenhuma nova), `CA-001` a `CA-049` (49).
      90  
      91 +Uma conferência final de consistência alinhou RN-020 e a matriz
         + de dependências ao tratamento de `MOEDA_SEM_COTACAO`, tornou a
         + redação de RN-013 condicional à periodicidade configurada, gen
         +eralizou as linhas de teto diário e individual e qualificou os 
         +dois comportamentos distintos de limite zero. Nenhuma decisão, 
         +ambiguidade, regra, critério ou total foi alterado.            
      92 +                                                               
      93  ---
      94  
      95  ## D-002 — Ajustes de contrato após auditoria independente · `2
          026-07-30`

● Now let's run full verification.

  Ran 1 shell command (ctrl+o to expand)

● Counts, totals, and version/status all unchanged. Now the requested
  verification commands:

  Ran 1 shell command (ctrl+o to expand)

● Only spec.md and DECISIONS.md are modified, exemplos/envelope/ remains
  untracked/intact, no other file touched, no /export/add/commit/push executed.

  1. Trechos corrigidos:

  RN-020 (moeda sem cotação): agora fecha o comportamento — MOEDA_SEM_COTACAO
  mantém valor_normalizado: null; bloqueia RN-006, RN-009, RN-010 e qualquer
  teto; não bloqueia RN-007/RN-019 (categoria) nem RN-008 (competência) quando
  os campos delas forem utilizáveis. 8.4-item 14 espelha o mesmo texto.

  RN-013: "hospedagem não participa da agregação" virou condicional — vale
  quando periodicidade: "diaria"; se a política declarar "dia", usa o mecanismo
  compartilhado de RN-019/RN-015.

  8.2: "Valor não positivo", "Nota fiscal obrigatória" e "Duplicidade econômica"
  agora dependem de valor_normalizado calculável (não mais despesa.valor
  bruto); "Agregação e teto diário" virou "Teto compartilhado "dia""; "Teto
  individual de hospedagem" virou "Teto individual "diaria"", cobrindo
  hospedagem e TETO_INDIVIDUAL_APLICADO.

  §4.5, TETO_DIARIO_APLICADO: associação reescrita — RN-011/RN-012 apenas quando
  alimentação/transporte estiverem configurados com "dia"; RN-019 para todo o
  resto (representacao, hospedagem com "dia", etc.).

  8.4-item 15: separa explicitamente limite 0,00 em centro cadastrado (recusa o
  item) de limite 0,00 em padrao (invalida o arquivo, código 2).

  DECISIONS.md: "onze RN atualizadas" → "doze RN atualizadas" (lista as doze);
  bullet de política por centro de custo qualifica os dois comportamentos de
  limite zero com a redação pedida; novo parágrafo de fechamento ao final de
  "Ajustes finais da segunda revisão independente".

  2. git status --short:
   M specs/001-motor-reembolso/DECISIONS.md
   M specs/001-motor-reembolso/spec.md
  ?? exemplos/envelope/

  3. Contagens confirmadas por grep: AMB = 37, RN = 22, CA = 49. Os quatro
  totais (585.43 / 351.43 / 1143.26 / 373.76) permanecem idênticos. Versão 1.2,
  status em revisão, inalterados.

  Parando para revisão.

✻ Cooked for 1m 59s

※ recap: We've been normalizing the reimbursement engine spec after the policy v4 envelope changes, and just finished a final internal-consistency pass on spec.md and DECISIONS.md. Everything's verified and stopped for your review; next step is whatever correction or approval you give. (disable recaps in /config)

> Faça somente três ajustes textuais finais.                                    
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  * `specs/001-motor-reembolso/spec.md`                                         
  * `specs/001-motor-reembolso/DECISIONS.md`                                    
                                                                                
  Não altere nenhum outro arquivo.                                              
                                                                                
  Não crie ou renumere AMB, RN ou CA.                                           
                                                                                
  Permanecem:                                                                   
                                                                                
  ```text                                                                       
  versão 1.2                                                                    
  status em revisão                                                             
  37 AMB                                                                        
  22 RN                                                                         
  49 CA                                                                         
  ```                                                                           
                                                                                
  Os quatro totais permanecem inalterados.                                      
                                                                                
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  ## 1. RN-020 — tolerância de `centro_custo`                                   
                                                                                
  Na RN-020, substitua:                                                         
                                                                                
  ```text                                                                       
  quando `categoria` e `centro_custo` estiverem estruturalmente válidos         
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  quando `despesa.categoria` estiver estruturalmente válida;                    
  `colaborador.centro_custo` é sempre resolvido conforme a tolerância de RN-019 
  — valor reconhecido seleciona a tabela específica, enquanto ausência, nulo,   
  tipo inválido ou valor desconhecido seleciona `padrao`                        
  ```                                                                           
                                                                                
  O objetivo é deixar explícito que um `centro_custo` ausente ou malformado não 
  bloqueia a avaliação da categoria.                                            
                                                                                
  ## 2. Matriz de dependências — exceção de `centro_custo`                      
                                                                                
  Em `§8.2`, substitua a introdução:                                            
                                                                                
  ```text                                                                       
  Cada regra depende dos campos abaixo. Uma regra não é avaliada quando algum   
  campo de que depende está estruturalmente inválido.                           
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  Cada regra depende dos dados abaixo. Uma regra não é avaliada quando um campo 
  obrigatório do item de que dependa estiver estruturalmente inválido ou        
  quando um valor derivado necessário não puder ser calculado.                  
  `colaborador.centro_custo` é uma exceção explícita: ausência, nulo, tipo      
  inválido ou valor desconhecido não bloqueiam regra alguma e resolvem para a   
  tabela `padrao`, conforme RN-019.                                             
  ```                                                                           
                                                                                
  Nas linhas que atualmente listam `colaborador.centro_custo`, use a expressão: 
                                                                                
  ```text                                                                       
  tabela de política resolvida por `colaborador.centro_custo`, incluindo        
  fallback para `padrao`                                                        
  ```                                                                           
                                                                                
  Aplique isso às linhas:                                                       
                                                                                
  * categoria fora da política/política por centro de custo;                    
  * teto compartilhado `"dia"`;                                                 
  * teto individual `"diaria"`.                                                 
                                                                                
  Não altere nenhuma outra dependência.                                         
                                                                                
  ## 3. DECISIONS.md — resumo inicial do limite zero                            
                                                                                
  No item 1 de `O que mudou na spec`, substitua:                                
                                                                                
  ```text                                                                       
  limite explicitamente `0,00` recusa a categoria                               
  (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`), nunca reembolso parcial de valor 
  zero                                                                          
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  limite explicitamente `0,00` em uma tabela de centro cadastrado recusa a      
  categoria (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`), nunca reembolso        
  parcial de valor zero; limite `0,00` em `padrao` invalida estruturalmente o   
  arquivo de política                                                           
  ```                                                                           
                                                                                
  ## Verificação                                                                
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff -- specs/001-motor-reembolso/spec.md                                 
  git diff -- specs/001-motor-reembolso/DECISIONS.md                            
  ```                                                                           
                                                                                
  Ao final, apresente somente:                                                  
                                                                                
  1. os três trechos finais;                                                    
  2. `git status --short`;                                                      
  3. confirmação de que versão, contagens e totais não mudaram.                 
                                                                                
  Pare para revisão.                                                            

  Ran 1 shell command (ctrl+o to expand)

● Found both anchors. Let's apply the three edits.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      558  
      559  ### RN-020 — Resolução de câmbio e conversão monetária
      560  
      561 -**Regra:** `despesa.moeda` igual a `"BRL"`, ou com a chave int
          -eiramente ausente do objeto (RN-002), tem taxa implícita `1` e
          - não consulta a tabela de câmbio. `despesa.moeda: null` nunca 
          -chega a esta regra — é `CAMPO_AUSENTE` (RN-002), recusado ante
          -s da resolução de câmbio. Para moeda estrangeira estruturalmen
          -te válida, procura-se primeiro a cotação exata na data da desp
          -esa; sem cotação exata, usa-se a cotação mais recente cuja dat
          -a seja **anterior** à data da despesa — nunca uma cotação futu
          -ra, e sem interpolação entre datas. Se não existir cotação exa
          -ta nem anterior utilizável para aquela moeda — porque ela não 
          -aparece em `cambio.json` em nenhuma data, ou porque sua primei
          -ra cotação disponível é posterior à data da despesa —, o item 
          -é recusado com `MOEDA_SEM_COTACAO` e `valor_normalizado` perma
          -nece nulo. Isso impede especificamente as regras que dependem 
          -de `valor_normalizado` calculável — RN-006 (valor não positivo
          -), RN-009 (nota fiscal), RN-010 (duplicidade) e qualquer teto 
          -(RN-011 a RN-015, RN-019) —, mas **não** impede as regras cujo
          -s campos de dependência permanecem utilizáveis: a elegibilidad
          -e de categoria pela tabela aplicável (RN-007 e a parte corresp
          -ondente de RN-019, quando `categoria` e `centro_custo` estiver
          -em estruturalmente válidos) e a elegibilidade temporal (RN-008
          -, quando `despesa.data` e o período estiverem válidos) continu
          -am sendo avaliadas normalmente. Um mesmo item pode, portanto, 
          -apresentar `MOEDA_SEM_COTACAO` junto de `CATEGORIA_FORA_POLITI
          -CA`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMP
          -ETENCIA` — mas nunca junto de `VALOR_NAO_POSITIVO`, `NOTA_FISC
          -AL_AUSENTE`, `DUPLICIDADE` ou de um motivo de teto, porque ess
          -as dependem do componente financeiro incalculável. A conversão
          - multiplica o valor bruto pela taxa resolvida; o arredondament
          -o para duas casas (RN-004) acontece uma única vez, sobre o res
          -ultado dessa multiplicação.                                   
      561 +**Regra:** `despesa.moeda` igual a `"BRL"`, ou com a chave int
          +eiramente ausente do objeto (RN-002), tem taxa implícita `1` e
          + não consulta a tabela de câmbio. `despesa.moeda: null` nunca 
          +chega a esta regra — é `CAMPO_AUSENTE` (RN-002), recusado ante
          +s da resolução de câmbio. Para moeda estrangeira estruturalmen
          +te válida, procura-se primeiro a cotação exata na data da desp
          +esa; sem cotação exata, usa-se a cotação mais recente cuja dat
          +a seja **anterior** à data da despesa — nunca uma cotação futu
          +ra, e sem interpolação entre datas. Se não existir cotação exa
          +ta nem anterior utilizável para aquela moeda — porque ela não 
          +aparece em `cambio.json` em nenhuma data, ou porque sua primei
          +ra cotação disponível é posterior à data da despesa —, o item 
          +é recusado com `MOEDA_SEM_COTACAO` e `valor_normalizado` perma
          +nece nulo. Isso impede especificamente as regras que dependem 
          +de `valor_normalizado` calculável — RN-006 (valor não positivo
          +), RN-009 (nota fiscal), RN-010 (duplicidade) e qualquer teto 
          +(RN-011 a RN-015, RN-019) —, mas **não** impede as regras cujo
          +s campos de dependência permanecem utilizáveis: a elegibilidad
          +e de categoria pela tabela aplicável (RN-007 e a parte corresp
          +ondente de RN-019, quando `despesa.categoria` estiver estrutur
          +almente válida; `colaborador.centro_custo` é sempre resolvido 
          +conforme a tolerância de RN-019 — valor reconhecido seleciona 
          +a tabela específica, enquanto ausência, nulo, tipo inválido ou
          + valor desconhecido seleciona `padrao`) e a elegibilidade temp
          +oral (RN-008, quando `despesa.data` e o período estiverem váli
          +dos) continuam sendo avaliadas normalmente. Um mesmo item pode
          +, portanto, apresentar `MOEDA_SEM_COTACAO` junto de `CATEGORIA
          +_FORA_POLITICA`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/o
          +u `FORA_COMPETENCIA` — mas nunca junto de `VALOR_NAO_POSITIVO`
          +, `NOTA_FISCAL_AUSENTE`, `DUPLICIDADE` ou de um motivo de teto
          +, porque essas dependem do componente financeiro incalculável.
          + A conversão multiplica o valor bruto pela taxa resolvida; o a
          +rredondamento para duas casas (RN-004) acontece uma única vez,
          + sobre o resultado dessa multiplicação.                       
      562  **Origem:** comunicado do RH, política v4, item B (Dia 2).
      563  **Aceite:** EUR `30,00` datado num sábado sem cotação própria 
           usa a cotação do último dia útil anterior disponível — ex.: `2
           026-07-17` para uma despesa de `2026-07-18`. GBP sem nenhuma e
           ntrada em `cambio.json`, em qualquer data, é recusado com `MOE
           DA_SEM_COTACAO`. USD `40,00` × `5,50` normaliza para `220,00`,
            com um único arredondamento.
      564  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      984  
      985  ### 8.2 Matriz de dependências
      986  
      987 -Cada regra depende dos campos abaixo. Uma regra não é avaliada
          - quando algum campo de que depende está estruturalmente inváli
          -do.                                                           
      987 +Cada regra depende dos dados abaixo. Uma regra não é avaliada 
          +quando um campo obrigatório do item de que dependa estiver est
          +ruturalmente inválido ou quando um valor derivado necessário n
          +ão puder ser calculado. `colaborador.centro_custo` é uma exceç
          +ão explícita: ausência, nulo, tipo inválido ou valor desconhec
          +ido não bloqueiam regra alguma e resolvem para a tabela `padra
          +o`, conforme RN-019.                                          
      988  
      989  | Regra | Campos necessários |
      990  |---|---|

● Now update the three matriz rows using colaborador.centro_custo:

  Ran 1 shell command (ctrl+o to expand)

● Now update lines 996, 1000, 1001 with the specified phrasing:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      993  | Normalização monetária | `despesa.valor` (já convertido, qua
           ndo aplicável) |
      994  | Normalização de categoria | `despesa.categoria` |
      995  | Valor não positivo | `valor_normalizado` calculável |
      996 -| Categoria fora da política / política por centro de custo | 
          -`despesa.categoria`, `colaborador.centro_custo` |             
      996 +| Categoria fora da política / política por centro de custo | 
          +`despesa.categoria`, tabela de política resolvida por `colabor
          +ador.centro_custo`, incluindo fallback para `padrao` |        
      997  | Competência | `despesa.data`, `periodo.inicio`, `periodo.fim
           ` |
      998  | Nota fiscal obrigatória | `valor_normalizado` calculável e `
           despesa.tem_nota_fiscal` |
      999  | Duplicidade econômica | `despesa.data`, `despesa.categoria` 
           normalizada, `despesa.moeda`, `valor_normalizado` calculável, 
           `despesa.fornecedor`, `despesa.descricao` |
● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
       997  | Competência | `despesa.data`, `periodo.inicio`, `periodo.fi
            m` |
       998  | Nota fiscal obrigatória | `valor_normalizado` calculável e 
            `despesa.tem_nota_fiscal` |
       999  | Duplicidade econômica | `despesa.data`, `despesa.categoria`
             normalizada, `despesa.moeda`, `valor_normalizado` calculável
            , `despesa.fornecedor`, `despesa.descricao` |
      1000 -| Teto compartilhado `"dia"` | `despesa.data`, `despesa.categ
           -oria`, `valor_normalizado` calculável, `colaborador.centro_cu
           -sto` e a `periodicidade` declarada na política aplicável |   
      1001 -| Teto individual `"diaria"` | `despesa.categoria`, `valor_no
           -rmalizado` calculável, `colaborador.centro_custo` e a `period
           -icidade` declarada na política aplicável — inclui `hospedagem
           -` (`TETO_HOSPEDAGEM_APLICADO`) e qualquer outra categoria sob
           - esse mecanismo (`TETO_INDIVIDUAL_APLICADO`) |               
      1000 +| Teto compartilhado `"dia"` | `despesa.data`, `despesa.categ
           +oria`, `valor_normalizado` calculável, tabela de política res
           +olvida por `colaborador.centro_custo`, incluindo fallback par
           +a `padrao`, e a `periodicidade` declarada nela |             
      1001 +| Teto individual `"diaria"` | `despesa.categoria`, `valor_no
           +rmalizado` calculável, tabela de política resolvida por `cola
           +borador.centro_custo`, incluindo fallback para `padrao`, e a 
           +`periodicidade` declarada nela — inclui `hospedagem` (`TETO_H
           +OSPEDAGEM_APLICADO`) e qualquer outra categoria sob esse meca
           +nismo (`TETO_INDIVIDUAL_APLICADO`) |                         
      1002  
      1003  ### 8.3 Ordem de apresentação dos motivos
      1004  

● Now edit 3: DECISIONS.md limite-zero summary in item 1 of "O que mudou na
  spec".

  Ran 1 shell command (ctrl+o to expand)

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 1 line, removed 1 line
      16  
      17  **O que mudou na spec:**
      18  
      19 -1. **Política deixa de ser um valor único (RN-019, nova).** A s
         -pec 1.1 não previa política externa nem variação por centro de 
         -custo — RN-007 e RN-011/012/013 tratavam os limites como consta
         -ntes universais. A v1.2 introduz resolução por `colaborador.cen
         -tro_custo`: tabela `padrao` como fallback integral; centro de c
         -usto cadastrado usa somente as categorias que declara, sem fall
         -back por categoria; limite explicitamente `0,00` recusa a categ
         -oria (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`), nunca reembol
         -so parcial de valor zero; categoria adicional (`representacao`)
         - só é reembolsável nos centros que a declaram.                 
      19 +1. **Política deixa de ser um valor único (RN-019, nova).** A s
         +pec 1.1 não previa política externa nem variação por centro de 
         +custo — RN-007 e RN-011/012/013 tratavam os limites como consta
         +ntes universais. A v1.2 introduz resolução por `colaborador.cen
         +tro_custo`: tabela `padrao` como fallback integral; centro de c
         +usto cadastrado usa somente as categorias que declara, sem fall
         +back por categoria; limite explicitamente `0,00` em uma tabela 
         +de centro cadastrado recusa a categoria (`CATEGORIA_NAO_REEMBOL
         +SAVEL_CENTRO_CUSTO`), nunca reembolso parcial de valor zero; li
         +mite `0,00` em `padrao` invalida estruturalmente o arquivo de p
         +olítica; categoria adicional (`representacao`) só é reembolsáve
         +l nos centros que a declaram.                                  
      20  2. **Câmbio e conversão monetária (RN-020, nova).** A spec 1.1 
          declarava explicitamente, em `§3`, que o sistema "não converte 
          nem identifica moedas" e que "todos os valores são interpretado
          s como reais brasileiros" — essa frase foi removida e substituí
          da (ver "comportamento anterior invalidado" abaixo). `despesa.m
          oeda` é novo campo opcional — o **sétimo** dos oito campos de 4
          .2, entre `valor` (6º) e `tem_nota_fiscal` (8º); a ausência da 
          **chave** resolve para `BRL` sem produzir motivo, mas `"moeda":
           null` (chave presente, valor `null`) é `CAMPO_AUSENTE`, e pres
          ença malformada de outro jeito recusa o item como qualquer camp
          o estrutural (correção pós-revisão, ver ao final). A resolução 
          de taxa usa cotação exata ou a mais recente anterior disponível
          , nunca futura, sem interpolação; moeda sem cotação utilizável 
          recusa o item (`MOEDA_SEM_COTACAO`). Conversão e arredondamento
           têm ordem única: multiplicar, depois arredondar uma vez (RN-00
          4 atualizada).
      21  3. **RN-009 (nota fiscal) atualizada.** O gatilho de R$100 agor
          a compara explicitamente o valor já convertido para BRL, não o 
          valor original na moeda da despesa — caso contrário, USD 40,00 
          (abaixo do gatilho na moeda original) escaparia da obrigatoried
          ade de nota apesar de valer R$220,00 convertido.
      22  4. **RN-010 (duplicidade) atualizada.** A chave econômica passa
           a ser `data`, `categoria` normalizada, `moeda`, `valor` normal
          izado **em BRL** (já convertido, RN-020), `fornecedor` original
           e `descricao` original — `moeda` entra como componente **adici
          onal**, ao lado do valor já convertido, não em lugar dele. Dois
           itens em moedas diferentes nunca são duplicatas entre si, mesm
          o que o valor convertido coincida exatamente: é `moeda` que imp
          ede a colisão, não o fato de o valor estar ou não convertido. U
          sar o valor em BRL mantém a chave de duplicidade alinhada ao me
          smo número que todas as regras financeiras posteriores usam.

● Now verification:

  Ran 1 shell command (ctrl+o to expand)

● All checks pass — only the two authorized files changed, no other file
  touched.

  1. Os três trechos finais:

  RN-020 (dentro do parágrafo de MOEDA_SEM_COTACAO): "...a elegibilidade de
  categoria pela tabela aplicável (RN-007 e a parte correspondente de RN-019,
  quando despesa.categoria estiver estruturalmente válida;
  colaborador.centro_custo é sempre resolvido conforme a tolerância de RN-019 —
  valor reconhecido seleciona a tabela específica, enquanto ausência, nulo, tipo
  inválido ou valor desconhecido seleciona padrao) e a elegibilidade temporal
  (RN-008...)"

  §8.2, introdução: "Cada regra depende dos dados abaixo. Uma regra não é
  avaliada quando um campo obrigatório do item de que dependa estiver
  estruturalmente inválido ou quando um valor derivado necessário não puder ser
  calculado. colaborador.centro_custo é uma exceção explícita: ausência, nulo,
  tipo inválido ou valor desconhecido não bloqueiam regra alguma e resolvem para
  a tabela padrao, conforme RN-019." — e as três linhas ("Categoria fora da
  política...", "Teto compartilhado \"dia\"", "Teto individual \"diaria\"")
  agora usam "tabela de política resolvida por colaborador.centro_custo,
  incluindo fallback para padrao".

  DECISIONS.md, item 1 de "O que mudou na spec": "...limite explicitamente 0,00
  em uma tabela de centro cadastrado recusa a categoria
  (CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO), nunca reembolso parcial de valor
  zero; limite 0,00 em padrao invalida estruturalmente o arquivo de política;
  categoria adicional (representacao) só é reembolsável nos centros que a
  declaram."

  2. git status --short:
   M specs/001-motor-reembolso/DECISIONS.md
   M specs/001-motor-reembolso/spec.md
  ?? exemplos/envelope/

  3. Versão 1.2, status em revisão, contagens (AMB=37, RN=22, CA=49) e os quatro
  totais (585.43 / 351.43 / 1143.26 / 373.76) — todos verificados por grep,
  inalterados.

  Parando para revisão.

✻ Worked for 55s

※ recap: We've been normatively updating the reimbursement spec (v1.2) through multiple independent-review correction passes to spec.md and DECISIONS.md, keeping counts, totals, and version locked. The last three textual fixes are done and verified; I'm now paused waiting for your review before any further changes.

> A revisão normativa da spec 1.2 foi concluída e aprovada.                     
                                                                                
  Altere exclusivamente:                                                        
                                                                                
  `specs/001-motor-reembolso/spec.md`                                           
                                                                                
  Faça somente esta mudança no cabeçalho:                                       
                                                                                
  ```text                                                                       
  Status: em revisão                                                            
  ```                                                                           
                                                                                
  para:                                                                         
                                                                                
  ```text                                                                       
  Status: aprovado                                                              
  ```                                                                           
                                                                                
  Mantenha inalterados:                                                         
                                                                                
  * versão `1.2`;                                                               
  * data `2026-08-05`;                                                          
  * todo o restante de `spec.md`;                                               
  * `DECISIONS.md`;                                                             
  * `plan.md`;                                                                  
  * `tasks.md`;                                                                 
  * README;                                                                     
  * código;                                                                     
  * testes;                                                                     
  * fixtures;                                                                   
  * arquivos de `exemplos/envelope/`.                                           
                                                                                
  Não crie nem renumere AMB, RN ou CA.                                          
                                                                                
  As contagens permanecem:                                                      
                                                                                
  ```text                                                                       
  AMB-001 a AMB-037 = 37                                                        
  RN-001 a RN-022 = 22                                                          
  CA-001 a CA-049 = 49                                                          
  ```                                                                           
                                                                                
  Os quatro totais permanecem:                                                  
                                                                                
  ```text                                                                       
  585.43                                                                        
  351.43                                                                        
  1143.26                                                                       
  373.76                                                                        
  ```                                                                           
                                                                                
  Depois execute:                                                               
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  Select-String -Path specs/001-motor-reembolso/spec.md -Pattern                
  "Versão:|Status:|Última alteração:"                                           
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * status final `aprovado`;                                                    
  * versão ainda `1.2`;                                                         
  * somente `spec.md` e `DECISIONS.md` permanecem modificados;                  
  * `exemplos/envelope/` permanece novo e intacto;                              
  * nenhum outro arquivo foi alterado;                                          
  * nenhum `/export`, git add, commit ou push foi executado.                    
                                                                                
  Pare para revisão.                                                            

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1  # Spec — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.2 · **Status:** em revisão · **Última alteração:**
        - 2026-08-05                                                     
      3 +**Versão:** 1.2 · **Status:** aprovado · **Última alteração:** 2
        +026-08-05                                                       
      4  
      5  > **Regra de ouro deste arquivo:** ele descreve o QUÊ e o PORQUÊ
         . Nenhuma linha
      6  > aqui pode citar linguagem, biblioteca, classe, função ou estru
         tura de pasta.

  Ran 1 shell command (ctrl+o to expand)

● Confirmado:

  - Status final: aprovado · Versão: 1.2 · Última alteração: 2026-08-05 — linha
  do cabeçalho: **Versão:** 1.2 · **Status:** aprovado · **Última alteração:** 
  2026-08-05
  - Somente spec.md e DECISIONS.md permanecem modificados (DECISIONS.md reflete
  edições de rodadas anteriores, não tocado nesta rodada); exemplos/envelope/
  continua ??, novo e intacto; nenhum outro arquivo alterado.
  - Contagens inalteradas: AMB = 37, RN = 22, CA = 49. Os quatro totais (585.43
  / 351.43 / 1143.26 / 373.76) inalterados.
  - Nenhum /export, git add, commit ou push executado.

  Parando para revisão.

✻ Worked for 14s

※ recap: We finished the SDD normative update for spec 1.2 (política v4 envelope), which is now marked approved (37 AMB, 22 RN, 49 CA, totals unchanged). No next action pending — awaiting your review or next instruction.