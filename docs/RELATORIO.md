# Relatório — Desafio SDD

**Aluno:** Bruno Buttros · **Repositório:** https://github.com/BrunoButtros/sdd-desafio · **Data:** 14/08/2026

Este relatório separa minhas respostas pessoais das evidências verificáveis do repositório. Quando o processo teve uma falha ou deixou uma lacuna, eu a registro sem reconstruir evidência retroativa.

---

## Delegação

Minha responsabilidade principal foi guiar a IA por meio das especificações sobre o que deveria ser realizado e controlar os ajustes relacionados ao Git. A IA ficou com a parte pesada de revisão, escrita dos arquivos, implementação, criação e execução de testes, indicação dos arquivos a incluir nos commits e apoio operacional.

Escolhi trabalhar assim porque considero que a IA consegue gerar código eficiente e seguir instruções com qualidade suficiente para produzir um bom resultado. No Git, porém, não me senti seguro para delegar completamente: mantive a revisão e a autorização do commit como pontos de controle humanos antes de versionar cada entrega. Isso não significa que eu tenha lido integralmente todos os diffs.

**A divisão:**

| Atividade | Quem | Por quê |
|---|---|---|
| Identificar ambiguidades | IA, sob orientação e revisão humana | O agente fez a leitura cruzada da política, exemplos e documentos; eu conduzi o escopo e aceitei ou pedi ajustes. |
| Decidir as ambiguidades | Bruno, apoiado pelas propostas da IA | As interpretações viraram decisões normativas que eu precisava aceitar e conseguir defender. |
| Escrever a spec | IA, sob orientação e revisões | A escrita extensa e as auditorias foram delegadas; as sessões mostram correções antes da aprovação. |
| Desenhar a arquitetura | IA, com aprovação humana | O agente elaborou o plan e eu preservei o aceite como ponto de controle. |
| Implementar | IA | A implementação foi executada task a task, com escopo e critérios definidos em `specs/001-motor-reembolso/tasks.md`. |
| Escrever testes | Majoritariamente a IA | Cada task associou regra e teste; eu verificava periodicamente a suíte completa no terminal. |
| Absorver o envelope | IA realizou as alterações; Bruno orientou e revisou | A mudança entrou primeiro nos documentos normativos e depois nas tasks e no código. |

O histórico materializa essa divisão. A baseline foi consolidada em T-001 (`59ea786`), seguida por commits pequenos de T-002 a T-021. No Dia 2, a spec v1.2, o plan v1.1 e o backlog foram aprovados antes da implementação (`f5e30f4`, `b67bbd5`, `73e6f3e`), e então T-022 a T-058 foram executadas. As sessões mostram pedidos de aprovação antes do Git; por exemplo, `docs/sessions/07-t003-vocabularios.md` e `docs/sessions/08-t004-envelope.md` encerram a execução aguardando revisão/autorização.

Até T-053 predominam exports nativos do Claude Code. Quando o Claude Code ficou indisponível por limitação de cota, T-054 foi documentada em `docs/sessions/dia2-t054-execucao-real-jar.md:3`, e T-055 a T-058 em registros do ChatGPT Codex. Esses cinco arquivos declaram expressamente que não são `/export` nativo nem reconstrução literal da conversa.

**Onde deleguei e me arrependi:** não identifiquei um ponto específico. Minha percepção foi que os prompts orientaram bem o agente e que o trabalho delegado foi executado com eficiência.

**Onde não deleguei e deveria ter delegado:** também não identifiquei um ponto específico. Preferi manter o Git como controle humano e não me arrependo dessa divisão.

**Ferramentas utilizadas:** Claude Code, ChatGPT e ChatGPT Codex. Não usei subagentes adicionais, MCP, skills ou hooks. Ferramentas automáticas do ambiente que eu não escolhi conscientemente não são apresentadas aqui como parte da minha estratégia.

---

## Descrição

O exemplo principal é a transformação dos limites de alimentação e transporte em um algoritmo verificável.

**Versão 1 — texto inicial da política:**

> “Alimentação tem limite de R$ 60 por dia.”
>
> “Despesas acima do limite são reembolsadas parcialmente.”

Esses textos estão em `DESAFIO.md:44-47`. A própria descrição do desafio pergunta se “por dia” significa por dia ou por despesa (`DESAFIO.md:56`). A redação ainda deixava quatro dúvidas relevantes: unidade por lançamento ou por agregado; significado operacional de “parcialmente”; distribuição do saldo entre lançamentos; e diferença entre teto diário e hospedagem por diária.

**Versão final:**

> Para categorias com periodicidade `dia`, os itens elegíveis da mesma categoria e data compartilham o teto e consomem o saldo em ordem de `indice_entrada`. O item que ultrapassa o saldo é pago parcialmente; os posteriores recebem `NAO_REEMBOLSADO_TETO_ESGOTADO`. Categorias com periodicidade `diaria`, como hospedagem na política padrão, recebem limite individual por lançamento e não compartilham saldo.

A decisão da unidade aparece em `specs/001-motor-reembolso/spec.md:586-592` (AMB-001) e a semântica do corte em `specs/001-motor-reembolso/spec.md:594-600` (AMB-002). O algoritmo final está em RN-011 a RN-015, especialmente `specs/001-motor-reembolso/spec.md:491-519`:

- alimentação e transporte usam o agregado da categoria/data quando a política declara periodicidade `dia`;
- RN-014 paga até o teto e corta o excedente, sem recusar o agregado inteiro;
- RN-015 consome o saldo em ordem e distingue item parcial de item posterior sem saldo;
- RN-013 trata hospedagem com periodicidade `diaria` como limite individual por lançamento.

**Como percebi:** a ambiguidade apareceu ao cruzar a política com os exemplos e tentar obter um resultado único para mais de um lançamento no mesmo dia. A auditoria registrada em `docs/sessions/02-especificacao-inicial.md:313-330` confirma que AMB-001/AMB-002, as regras e as correções R-1/R-2 estavam refletidas na spec. `specs/001-motor-reembolso/DECISIONS.md:154-158` preserva a evolução: R-1 retirou hospedagem da agregação diária; R-2 resolveu a aparente contradição entre reembolso parcial e item posterior com valor zero, criando a distinção entre `NAO_REEMBOLSADO_TETO_ESGOTADO` e `RECUSADO`.

**Commits relacionados:** `59ea786` consolidou a especificação e o backlog da baseline; `b4b08e8` implementou os tetos diários e a distribuição do saldo; `2edc932` implementou o teto individual de hospedagem; `36d5d9b` confirmou o exemplo completo e o total de 585,43.

---

## Discernimento

### Caso 1 — interpretação incorreta da estrutura de `cambio.json`

**O que o agente propôs:** na primeira leitura do envelope, descreveu `cambio.json` como um mapa direto de data para moeda.

**Por que estava errado:** o arquivo real tem uma raiz com `moeda_base`, `fonte`, `observacao` e `taxas`; as cotações ficam aninhadas em `taxas`, por data e moeda. A interpretação proposta era incompatível com o próprio arquivo fornecido e levaria leitor, validação e modelo a usar um schema inexistente.

**Como detectei:** a revisão comparou a redação da spec com `exemplos/envelope/cambio.json`. A divergência aparece literalmente em `docs/sessions/dia2-spec-v1.2-aprovada.md:4800-4806` e volta a ser classificada como “incorreto” nas linhas 5478-5490.

**O que fiz:** a spec passou a documentar o contrato estrutural completo em §4.1.1 e AMB-035. A versão final afirma que a raiz não é um mapa direto (`specs/001-motor-reembolso/spec.md:138`) e registra por que a correção evita que o erro chegue à implementação (`specs/001-motor-reembolso/spec.md:873-879`). `specs/001-motor-reembolso/DECISIONS.md:68` preserva a versão incorreta, a estrutura real e o efeito da correção. Tudo entrou no commit `f5e30f4`, antes da implementação de `LeitorCambio`.

Detectar isso na spec impediu que o erro se propagasse para modelo, leitor, fixtures e testes. Esse é o caso principal porque não foi uma mudança normal de requisito: foi uma afirmação factual do agente que não correspondia ao artefato recebido.

### Caso 2 — falso teste-canário de arredondamento

**O que o agente propôs:** o plan tratava `USD 40,00 × 5,50 = 220,00` como teste-canário da ordem de arredondamento.

**Por que estava errado:** esse cálculo dá o mesmo resultado com o fluxo correto e com várias formas de arredondamento prematuro. Portanto, ele era um bom exemplo funcional de conversão, mas não distinguia o defeito que pretendia detectar.

**Como detectei:** a revisão analisou matematicamente as duas ordens de cálculo. O pedido de correção está em `docs/sessions/dia2-plan-v1.1-aprovado.md:2549-2568`.

**O que fiz:** mantive `40,00 × 5,50` somente como exemplo funcional de CA-031 e substituí o canário por `1.005 × 1.005`. O produto exato é `1.010025`, que arredonda ao final para `1,01`; arredondar o valor bruto antes da multiplicação pode levar a `1,02`. O plan final registra essa distinção em `specs/001-motor-reembolso/plan.md:451-458` e DT-015 (`specs/001-motor-reembolso/plan.md:601-606`), no commit `b67bbd5`.

**Padrão observado:** uma proposta pode parecer tecnicamente plausível e ainda não comprovar o que afirma. Nos dois casos, a conferência precisou voltar ao artefato real — JSON ou aritmética — em vez de aceitar a explicação do agente pela forma convincente do texto.

---

## Diligência

Meu procedimento real combinou execução periódica da suíte completa no terminal, revisão antes dos commits e verificações adicionais nos pontos que considerei críticos. Eu não li integralmente a maioria dos diffs e arquivos; por causa do prazo curto, estimo que li integralmente **menos da metade**. Não transformo isso em percentual exato porque não fiz essa medição.

Os testes foram majoritariamente escritos pelo agente. Minha verificação recorrente era executar a suíte completa e confirmar que permanecia verde. Isso dava segurança de regressão, mas não garantia sozinho que o teste representava a regra correta. Por isso, os fechamentos mais sensíveis adicionaram conferências independentes do simples “teste passou”:

- T-054 executou `mvn test`, empacotou o JAR e rodou os quatro cenários reais. `docs/sessions/dia2-t054-execucao-real-jar.md:31-127` registra 585.43, 351.43, 1143.26 e 373.76, além da soma item a item.
- T-055 inventariou consumidores por `git grep`, migrou exatamente 18 arquivos de teste e submeteu esses arquivos à revisão externa. O registro confirma 628 testes verdes e a aprovação em `docs/sessions/dia2-t055-migracao-legado-codex.md:89-108`.
- T-056 executou compilação antes da suíte. A compilação revelou o consumidor residual em `DetectorIdDuplicado`; ele foi migrado, a compilação repetida e os greps finais ficaram sem referências ao legado. A evidência está em `docs/sessions/dia2-t056-remocao-legado-codex.md:54-112`.
- T-058 conferiu RN-019 a RN-022 e CA-024 a CA-049 identificador por identificador, contra as tasks e os testes reais. Também confirmou a ausência de `AGUARDANDO_APROVACAO`, fila manual e gatilho de R$500 (`docs/sessions/dia2-t058-rastreabilidade-final-codex.md:29-60`).

**O que aceitei sem verificar integralmente e o custo observado:** a combinação de prazo curto e leitura parcial fez com que eu dependesse bastante da suíte e das revisões pontuais. O produto continuou verde, mas a documentação/rastreabilidade teve falhas que o teste automatizado não poderia detectar:

- T-053 teve checkbox/status esquecidos no commit principal `45503b8`; a correção veio em `a3560b1`.
- T-055 repetiu o problema no commit `714080f`; a correção veio em `f9ed571`.
- Em T-002, o agente executou `git add pom.xml` apesar da proibição. Ele desfez imediatamente com `git reset` e registrou o erro em `docs/sessions/05-t002-maven.md:188-192`.
- T-005, T-010, T-018 e T-024 não possuem sessão própria em `docs/sessions/`.
- Os commits normativos `f5e30f4`, `b67bbd5` e `73e6f3e` não contêm identificador de task na mensagem.

Não criei exports retroativos para preencher essas lacunas. Elas demonstram uma limitação concreta do processo: suíte verde e código correto não substituem a conferência da trilha documental, dos status e das evidências de sessão.

---

## O envelope

**Quantos arquivos toquei manualmente:** 0. As alterações foram realizadas pelos agentes sob minha orientação e revisão. Isso não significa ausência de participação: eu conduzi o escopo, revisei pontos críticos e mantive o aceite/Git como controle humano.

**Quanto tempo levou:** aproximadamente 2 horas revendo os arquivos para absorver o envelope.

**Diff de absorção:** do commit imediatamente anterior ao envelope (`8c1241f`) até o fechamento de T-058 (`72fd5f9`), `git diff 8c1241f..72fd5f9 --stat` registra 124 arquivos, 55.462 inserções e 764 remoções. Nesse intervalo foram afetados 25 caminhos Java de produção, 41 caminhos Java de teste e 9 recursos/fixtures de teste.

**Ordem em que fiz:**

1. baseline concluída até T-021 (`8c1241f`);
2. recebimento e leitura do envelope;
3. spec v1.2 e atualização de `DECISIONS.md`, no mesmo commit normativo `f5e30f4`;
4. plan v1.1 (`b67bbd5`);
5. backlog do Dia 2 (`73e6f3e`);
6. implementação de T-022 a T-058;
7. regressões, execução real do JAR, remoção do legado e auditoria final.

A mudança acrescentou RN-019 a RN-022, CA-024 a CA-049 e DT-011 a DT-019: 4 regras, 26 critérios e 9 decisões técnicas. O backlog abriu 37 tasks, T-022 a T-058. Entraram política externa, resolução por centro de custo, categorias e periodicidades dinâmicas, campo opcional de moeda, câmbio externo, conversão anterior às regras financeiras, campos cambiais na saída e CLI com `--input`, `--output`, `--politica` e `--cambio`.

**Absorveu com menor resistência:** o pipeline em etapas permitiu inserir resolução de câmbio e resolução de política em pontos definidos; os modelos imutáveis ajudaram a transportar os novos dados; o uso existente de `BigDecimal` e o ponto único de normalização permitiram preservar a regra monetária; testes por regra e fixtures estruturais forneceram uma baseline objetiva.

**Resistiu:** a política do Dia 1 estava representada por `PoliticaReembolso`, categorias fixas e agregadores específicos. A CLI tinha apenas duas flags, e `ItemValidado`/`ResultadoItem` não carregavam auditoria cambial. A migração incremental exigiu APIs de compatibilidade, depois a migração dos consumidores em T-055 e a remoção definitiva em T-056.

**Resultados finais:**

| Cenário | Total reembolsável |
|---|---:|
| Baseline histórica | 585.43 |
| Política v4 / CC-ENG-PLATAFORMA | 351.43 |
| Rafael / CC-COMERCIAL | 1143.26 |
| Dani / centro de custo desconhecido | 373.76 |

Esses valores estão documentados por testes em T-050 a T-053 e pela execução do JAR em `docs/sessions/dia2-t054-execucao-real-jar.md:118-127`.

O Item C permaneceu fora de escopo. `specs/001-motor-reembolso/spec.md:857-863` determina que não existe `AGUARDANDO_APROVACAO`, fila ou alteração por valor acima de R$500; `specs/001-motor-reembolso/plan.md:806` preserva a mesma decisão. A auditoria T-058 encontrou zero ocorrências do estado em `src/main/java` e nenhuma implementação equivalente.

**Se eu tivesse escrito a spec original sabendo da mudança:** eu deixaria política externa, centro de custo e câmbio sem definição ou cobertura antecipada e fecharia essas regras somente quando a política externa chegasse. Esta é uma reflexão retrospectiva; não foi a decisão tomada no Dia 1.

**O que a spec me poupou, em concreto:** a mudança entrou por uma revisão dos contratos e da ordem do pipeline, sem descartar a baseline. As regras históricas conservaram identificadores; os quatro resultados foram derivados e testados separadamente; e as incompatibilidades ficaram localizadas em modelos, leitores, CLI e agregadores. A trilha `spec → DECISIONS → plan → tasks → implementação` permitiu distinguir mudança de política de regressão de código.

---

## Fechamento

**Para qual tamanho de projeto isto valeu a pena?** Não vinculo a utilidade do SDD somente a projetos grandes. Considero o processo interessante de forma geral; mesmo neste desafio curto, a formalização funcionou como mecanismo preventivo e de rastreabilidade.

**Para qual não valeria?** Não identifiquei uma fronteira de tamanho em que ele deixaria de valer por princípio. Entendo o receio de o processo virar burocracia, mas considero esse nível de burocracia desejável quando serve para evitar falhas futuras. O grau de formalização pode variar conforme o risco, sem eliminar a disciplina de registrar decisões.

**O que eu faria diferente:** sabendo de antemão que política externa, centro de custo e câmbio chegariam depois, eu evitaria fechar antecipadamente essas partes e esperaria o contrato externo para defini-las e cobri-las. Também trataria o fechamento documental como uma verificação própria, porque os esquecimentos de status mostraram que a suíte não cobre o processo SDD.

**A coisa mais desconfortável que aprendi sobre como trabalho com IA:** percebi limitações práticas das ferramentas e do ambiente, especialmente na portabilidade da evidência. O fluxo de `/export` funcionava naturalmente no Claude Code CLI. Depois da mudança de ferramenta, ChatGPT e Codex não ofereciam exatamente o mesmo mecanismo nativo. Foi necessário criar registros explícitos que não fingissem ser exports do Claude. As quatro tasks sem sessão própria e a mudança de formato a partir de T-054 reforçam que um processo dependente de uma função específica da ferramenta fica vulnerável quando o ambiente muda. Preservar transparência foi mais importante do que fabricar continuidade documental.
