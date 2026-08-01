# Spec — Motor de Cálculo de Reembolso

**Versão:** 1.1 · **Status:** aprovada · **Última alteração:** 2026-07-30

> **Regra de ouro deste arquivo:** ele descreve o QUÊ e o PORQUÊ. Nenhuma linha
> aqui pode citar linguagem, biblioteca, classe, função ou estrutura de pasta.
> Se apareceu solução, o lugar dela é o `plan.md`.
>
> **Teste de aceitação da própria spec:** uma pessoa que nunca viu o projeto
> consegue, lendo só este arquivo, verificar se o sistema está correto?

---

## 1. Problema

Hoje o financeiro confere despesa por despesa contra a política de reembolso, manualmente. O processo é lento e produz resultados diferentes para casos iguais, porque a política v3 é ambígua em pontos que mudam o valor pago — e cada conferente resolve a ambiguidade do seu jeito, sem registrar qual leitura adotou. O custo não é só o tempo: é a impossibilidade de explicar ao colaborador por que a despesa dele foi cortada.

## 2. Objetivo

Dado o conjunto de despesas de um colaborador num período, produzir de forma reprodutível o valor reembolsável de cada item e a justificativa da decisão, de modo que dois processamentos da mesma entrada sempre produzam o mesmo resultado e que cada decisão seja rastreável até uma regra escrita.

## 3. Fora de escopo

Cada item abaixo corresponde a um caso que **existe nos dados ou na política** e que este sistema deliberadamente não trata. A ausência não é esquecimento.

- **Não identifica condição de viagem** nem aplica a ampliação de 50% do item 6 da política. A entrada não possui campo estruturado que informe viagem. (AMB-006, AMB-007 · RN-016)
- **Não extrai quantidade de diárias ou noites** do texto da descrição. Cada lançamento de hospedagem vale uma diária. (AMB-008 · RN-013)
- **Não detecta fragmentação de hospedagem** por descrição, fornecedor ou proximidade de datas. Uma estadia lançada em vários itens recebe um teto por item.
- **Não processa créditos, estornos ou compensações.** Valores não positivos são recusados e não abatem nada. (AMB-013 · RN-006)
- **Não distingue contexto de consumo:** alimentação em fim de semana, plantão, com cliente, de representação, ou consumida dentro da hospedagem seguem exclusivamente a regra geral da categoria. Os três casos existem no arquivo de exemplo e foram identificados.
- **Não converte nem identifica moedas.** Todos os valores são interpretados como reais brasileiros (BRL).
- **Não agrega entre arquivos.** A apuração ocorre dentro de uma única entrada; `colaborador.nome` e `colaborador.centro_custo` nunca são usados para cruzar dados.
- **Não usa `periodo.competencia`** para elegibilidade temporal. (AMB-009)
- **Não faz correspondência aproximada de categoria:** sem correção ortográfica, sinônimos ou substituição de espaço por sublinhado. `transporte urbano` não se torna `transporte_urbano`. (AMB-015)
- **Não normaliza `descricao` nem `fornecedor`.** Diferenças de caixa, acento ou espaço são diferenças reais. (AMB-015)
- **Não interpreta o conteúdo semântico de texto livre.** `descricao` e `fornecedor` não são lidos para inferir viagem, quantidade de diárias, estorno, categoria ou qualquer outro tratamento financeiro. Eles são usados exclusivamente em comparação literal de igualdade na chave de duplicidade (RN-010). A fronteira é essa: comparar duas descrições exatamente iguais é permitido; interpretar o significado da palavra "hotel" não é.
- **Não coage tipos.** `"72,50"`, `"31/07/2026"` e `"sim"` não são convertidos.
- **Não presume valores padrão** para campos obrigatórios ausentes.
- **Não reage a campos desconhecidos.** Um campo fora do contrato — inclusive um eventual `em_viagem` — é ignorado e não ativa comportamento algum.

---

## 4. Entrada e saída

### 4.1 Envelope da entrada

| Campo | Tipo | Significado | Obrigatório |
|---|---|---|---|
| (raiz) | objeto | Documento de apuração | Sim |
| `colaborador` | objeto | Metadados de rastreabilidade | Não |
| `colaborador.id` | texto | Identificador do colaborador. **Sem exigência de unicidade** e sem uso em regra alguma | Não |
| `colaborador.nome` | texto | Nome do colaborador | Não |
| `colaborador.centro_custo` | texto | Centro de custo | Não |
| `periodo` | objeto | Janela de apuração | Sim |
| `periodo.competencia` | texto `AAAA-MM` | Rótulo informativo. Não participa de nenhuma decisão | Não |
| `periodo.inicio` | texto `AAAA-MM-DD` | Primeiro dia da janela, inclusive | Sim |
| `periodo.fim` | texto `AAAA-MM-DD` | Último dia da janela, inclusive | Sim |
| `despesas` | lista | Itens a apurar. Pode ser vazia | Sim |

**O envelope é inválido** — e nenhuma apuração, nem parcial, é produzida — quando: a raiz não é objeto; `periodo` está ausente ou não é objeto; `periodo.inicio` ou `periodo.fim` estão ausentes, não são texto, não seguem `AAAA-MM-DD` ou não representam data real do calendário; `periodo.inicio` é posterior a `periodo.fim`; `despesas` está ausente, é nulo ou não é lista.

**Tolerância dos metadados opcionais.** Nenhum defeito no bloco `colaborador` invalida o arquivo nem qualquer item, porque o bloco não participa de regra financeira alguma:

| Situação | Resultado na saída |
|---|---|
| `colaborador` ausente ou nulo | Os três metadados são nulos |
| `colaborador` presente, mas não é objeto | Ignorado; os três metadados são nulos |
| `colaborador` é objeto | Cada um de `colaborador.id`, `colaborador.nome` e `colaborador.centro_custo` é preservado **apenas quando for texto** |
| Campo ausente, nulo ou de outro tipo dentro de `colaborador` | Representado como nulo |
| Campo desconhecido dentro de `colaborador` | Ignorado |

`periodo.competencia` segue o comportamento opcional já descrito: preservado quando presente no formato `AAAA-MM`, nulo em qualquer outro caso, sem efeito sobre decisão alguma.

### 4.2 Item de despesa

Os sete campos abaixo são obrigatórios. A sequência é a **ordem canônica de contrato**, usada para ordenar motivos estruturais na saída.

| # | Campo | Tipo | Significado | Restrição adicional |
|---|---|---|---|---|
| 1 | `despesa.id` | texto | Identificador do lançamento. **Único no arquivo** | Texto não vazio |
| 2 | `despesa.data` | texto | Data do fato gerador | Formato `AAAA-MM-DD` representando data real do calendário |
| 3 | `despesa.categoria` | texto | Categoria informada, antes da normalização | Texto não vazio |
| 4 | `despesa.descricao` | texto | Descrição livre. Informativa; integra a chave de duplicidade como recebida | Texto, podendo ser vazio |
| 5 | `despesa.fornecedor` | texto | Fornecedor. Integra a chave de duplicidade como recebido | Texto, podendo ser vazio |
| 6 | `despesa.valor` | número | Valor monetário em BRL. Inteiro é aceito (`72` → `72,00`) | Nenhuma quanto ao sinal: zero e negativos são estruturalmente válidos, avaliados depois por RN-006 |
| 7 | `despesa.tem_nota_fiscal` | booleano | Existência de nota fiscal | Nenhuma além do tipo booleano — um valor booleano recebido não é aceito como número, nem o inverso |

**Nomes qualificados.** A forma `despesa.<campo>` é o nome canônico do campo em toda esta spec, e é exatamente o conjunto de valores não nulos aceitos em `motivo.campo` (4.3). A chave correspondente dentro do objeto de despesa é o segmento à direita do ponto: o campo canônico `despesa.valor` corresponde à chave `valor`.

Campo **ausente**, **nulo**, de **tipo inválido** ou de **formato inválido** produz **erro estrutural no item**: o item é recusado e o restante do arquivo continua sendo processado.

**Classificação do erro estrutural.** A escolha entre os três motivos abaixo é fechada e não admite outra leitura:

- **`CAMPO_AUSENTE`** — a chave obrigatória não existe no objeto, ou existe com valor `nulo`.
- **`CAMPO_TIPO_INVALIDO`** — a chave existe, não é nula, mas seu tipo JSON diverge do exigido na coluna "Tipo" da tabela acima. Um valor booleano nunca é aceito como número, mesmo em linguagens de implementação que tratam booleano como subtipo numérico — e o inverso também não é aceito.
- **`CAMPO_FORMATO_INVALIDO`** — o tipo JSON está correto, mas o conteúdo viola a "Restrição adicional" da tabela acima: texto vazio onde não é permitido, texto que não segue `AAAA-MM-DD`, ou texto nesse padrão que não representa uma data real do calendário.

Exemplos normativos:

| Campo recebido | Classificação |
|---|---|
| `despesa.id` igual a texto vazio | `CAMPO_FORMATO_INVALIDO` |
| `despesa.data` igual a `"31/07/2026"` | `CAMPO_FORMATO_INVALIDO` |
| `despesa.data` igual a `"2026-02-30"` | `CAMPO_FORMATO_INVALIDO` — formato correto, data inexistente no calendário |
| `despesa.categoria` igual a um número | `CAMPO_TIPO_INVALIDO` |
| `despesa.categoria` igual a texto vazio | `CAMPO_FORMATO_INVALIDO` |
| `despesa.valor` igual a `"72,50"` | `CAMPO_TIPO_INVALIDO` |
| `despesa.tem_nota_fiscal` igual a `"sim"` | `CAMPO_TIPO_INVALIDO` |
| `despesa.valor` igual a `0` ou a um número negativo | Estruturalmente válido — nenhum motivo estrutural. Tratado depois por RN-006 (`VALOR_NAO_POSITIVO`) |

**Elemento que não é objeto.** Cada elemento de `despesas` deve ser um objeto. Quando não for — texto, número, lista, nulo —, aquela posição é recusada com o motivo único `ITEM_TIPO_INVALIDO`. Não se produzem sete motivos de campo ausente e nenhuma regra de negócio é avaliada. O registro correspondente é exatamente:

| Campo da saída | Valor |
|---|---|
| `indice_entrada` | a posição original, preservada |
| `id` | nulo |
| `valor_informado` | nulo |
| `valor_normalizado` | nulo |
| `valor_reembolsavel` | `0,00` |
| `decisao` | `RECUSADO` |
| `motivos` | um único objeto, com `codigo` `ITEM_TIPO_INVALIDO`, `regra` `RN-002` e `campo` nulo |

O restante do arquivo continua sendo processado.

### 4.3 Saída

| Campo | Tipo | Significado |
|---|---|---|
| `colaborador.id` / `.nome` / `.centro_custo` | texto ou nulo | Metadados preservados para rastreabilidade. Nulos quando ausentes na entrada |
| `periodo.competencia` | texto ou nulo | Preservado quando presente e no formato `AAAA-MM`; nulo caso contrário |
| `periodo.inicio` / `periodo.fim` | texto | A janela efetivamente aplicada |
| `resultados` | lista | Um registro por item da entrada, **na ordem da entrada** |
| `total_reembolsavel` | monetário | Soma dos `valor_reembolsavel` apresentados |

Cada registro de `resultados`:

| Campo | Tipo | Significado |
|---|---|---|
| `indice_entrada` | inteiro ≥ 1 | Posição original do item na lista, **base 1**, atribuída antes de qualquer validação e imutável |
| `id` | texto ou nulo | `despesa.id` original. Nulo quando ausente ou inválido |
| `valor_informado` | qualquer valor JSON ou nulo | O conteúdo de `despesa.valor` exatamente como recebido, preservado para auditoria — inclusive quando o tipo é inválido |
| `valor_normalizado` | monetário ou nulo | Valor após normalização monetária. Nulo quando não calculável |
| `valor_reembolsavel` | monetário | Valor aprovado. Sempre `0,00` para item recusado |
| `decisao` | enumeração | Ver 4.4 |
| `motivos` | lista de objetos, possivelmente vazia | Objetos de motivo, na ordem definida em 8.3 |

**Preservação de `valor_informado`.** Quando a chave `valor` existe no objeto de despesa, `valor_informado` é exatamente o valor JSON recebido — número, texto, booleano, lista ou objeto —, mesmo quando esse tipo é inválido para o contrato: `valor: "72,50"` produz `valor_informado` igual a `"72,50"`; `valor: true` produz `valor_informado` igual a `true`. `valor_informado` é nulo quando a chave `valor` está ausente ou é nula, e também quando o elemento de `despesas` não é um objeto (4.2). `valor_normalizado` permanece nulo sempre que `despesa.valor` não for um número válido, independentemente do que `valor_informado` contenha.

Cada objeto de `motivos` tem três campos:

| Campo | Tipo | Significado |
|---|---|---|
| `codigo` | enumeração | Código do motivo, conforme 4.5 |
| `regra` | texto | Identificador da regra de negócio que produziu o motivo, no formato `RN-NNN` |
| `campo` | texto ou nulo | Nome qualificado do campo. Restrito à lista fechada abaixo. Nulo quando o motivo não se refere a um campo específico |

**Valores aceitos em `campo`.** Apenas os sete nomes canônicos de 4.2, e nenhum outro:

`despesa.id` · `despesa.data` · `despesa.categoria` · `despesa.descricao` · `despesa.fornecedor` · `despesa.valor` · `despesa.tem_nota_fiscal`

Qualquer outro motivo traz `campo` nulo. Há uma única exceção a "estrutural implica campo preenchido": `ITEM_TIPO_INVALIDO` traz `campo` nulo, porque o defeito é do elemento inteiro e não de um campo dele. E há uma única exceção a "não estrutural implica campo nulo": `ID_DUPLICADO` traz `campo` igual a `despesa.id`, porque a violação é dele.

Exemplos:

```
{ "codigo": "CAMPO_TIPO_INVALIDO",  "regra": "RN-002", "campo": "despesa.valor" }
{ "codigo": "ITEM_TIPO_INVALIDO",   "regra": "RN-002", "campo": null }
{ "codigo": "ID_DUPLICADO",         "regra": "RN-003", "campo": "despesa.id" }
{ "codigo": "NOTA_FISCAL_AUSENTE",  "regra": "RN-009", "campo": null }
{ "codigo": "TETO_DIARIO_APLICADO", "regra": "RN-011", "campo": null }
```

Item integralmente reembolsado tem `motivos: []`.

### 4.4 Vocabulário de decisão

| Código | Significado |
|---|---|
| `INTEGRALMENTE_REEMBOLSADO` | Item elegível pago pelo valor normalizado integral |
| `PARCIALMENTE_REEMBOLSADO` | Item elegível pago abaixo do valor normalizado, por incidência de teto |
| `NAO_REEMBOLSADO_TETO_ESGOTADO` | Item elegível que recebeu zero porque o teto diário já havia sido consumido. **Não é recusa** |
| `RECUSADO` | Item que violou ao menos uma regra de elegibilidade ou de contrato |

### 4.5 Vocabulário de motivos

**Motivos de recusa** — presentes apenas em `RECUSADO`:

| Código | Regra associada | `campo` | Significado |
|---|---|---|---|
| `ITEM_TIPO_INVALIDO` | RN-002 | nulo | Elemento de `despesas` que não é objeto. Motivo único da posição |
| `CAMPO_AUSENTE` | RN-002 | um dos sete nomes canônicos | A chave obrigatória não existe no objeto, ou existe com valor nulo |
| `CAMPO_TIPO_INVALIDO` | RN-002 | um dos sete nomes canônicos | A chave existe, não é nula, mas seu tipo JSON diverge do exigido pelo contrato |
| `CAMPO_FORMATO_INVALIDO` | RN-002 | um dos sete nomes canônicos | O tipo JSON está correto, mas o conteúdo viola a restrição adicional do campo |
| `ID_DUPLICADO` | RN-003 | `despesa.id` | `despesa.id` válido repetido no arquivo |
| `VALOR_NAO_POSITIVO` | RN-006 | nulo | Valor normalizado menor ou igual a zero |
| `CATEGORIA_FORA_POLITICA` | RN-007 | nulo | Categoria normalizada fora da lista canônica |
| `FORA_COMPETENCIA` | RN-008 | nulo | `data` fora da janela |
| `NOTA_FISCAL_AUSENTE` | RN-009 | nulo | Nota obrigatória e ausente |
| `DUPLICIDADE` | RN-010 | nulo | Ocorrência posterior de despesa economicamente idêntica |

**Motivos de limitação** — presentes em itens não recusados:

| Código | Regra associada | `campo` | Aparece em |
|---|---|---|---|
| `TETO_DIARIO_APLICADO` | RN-011 (alimentação) ou RN-012 (transporte) | nulo | `PARCIALMENTE_REEMBOLSADO` |
| `TETO_DIARIO_ESGOTADO` | RN-015 | nulo | `NAO_REEMBOLSADO_TETO_ESGOTADO` |
| `TETO_HOSPEDAGEM_APLICADO` | RN-013 | nulo | `PARCIALMENTE_REEMBOLSADO` de hospedagem |

Item `INTEGRALMENTE_REEMBOLSADO` tem lista de motivos vazia.

### 4.6 Fragmento de saída

O bloco abaixo é um **fragmento não executável** da lista `resultados`: mostra três dos catorze registros, para ilustrar a forma. Por ser parcial, não traz `total_reembolsavel` — o total só faz sentido sobre a lista inteira. A apuração completa dos catorze itens e o total estão em 4.7.

```
"resultados": [
  { "indice_entrada": 1, "id": "d-001", "valor_informado": 72.50, "valor_normalizado": 72.50,
    "valor_reembolsavel": 60.00, "decisao": "PARCIALMENTE_REEMBOLSADO",
    "motivos": [ { "codigo": "TETO_DIARIO_APLICADO", "regra": "RN-011", "campo": null } ] },

  { "indice_entrada": 2, "id": "d-002", "valor_informado": 38.00, "valor_normalizado": 38.00,
    "valor_reembolsavel": 0.00, "decisao": "NAO_REEMBOLSADO_TETO_ESGOTADO",
    "motivos": [ { "codigo": "TETO_DIARIO_ESGOTADO", "regra": "RN-015", "campo": null } ] },

  { "indice_entrada": 11, "id": "d-011", "valor_informado": 33.333, "valor_normalizado": 33.33,
    "valor_reembolsavel": 33.33, "decisao": "INTEGRALMENTE_REEMBOLSADO",
    "motivos": [] }
]
```

Os metadados do envelope acompanham a lista no documento completo:

```
"colaborador": { "id": "c-0417", "nome": "Marina Volpi", "centro_custo": "CC-ENG-PLATAFORMA" },
"periodo":     { "competencia": "2026-07", "inicio": "2026-07-01", "fim": "2026-07-31" }
```

### 4.7 Resultado esperado para `exemplos/despesas-exemplo.json`

| # | `id` | Data | Categoria canônica | Informado | Normalizado | Decisão | Reembolsável | Motivos |
|---|---|---|---|---|---|---|---|---|
| 1 | d-001 | 2026-07-03 | alimentacao | 72.50 | 72,50 | PARCIAL | **60,00** | `TETO_DIARIO_APLICADO` |
| 2 | d-002 | 2026-07-03 | alimentacao | 38.00 | 38,00 | ESGOTADO | **0,00** | `TETO_DIARIO_ESGOTADO` |
| 3 | d-003 | 2026-07-06 | transporte_urbano | 100.00 | 100,00 | PARCIAL | **80,00** | `TETO_DIARIO_APLICADO` |
| 4 | d-004 | 2026-07-06 | transporte_urbano | 100.01 | 100,01 | RECUSADO | **0,00** | `NOTA_FISCAL_AUSENTE` |
| 5 | d-005 | 2026-07-07 | coworking | 89.00 | 89,00 | RECUSADO | **0,00** | `CATEGORIA_FORA_POLITICA` |
| 6 | d-006 | 2026-07-09 | alimentacao | 54.90 | 54,90 | INTEGRAL | **54,90** | — |
| 7 | d-007 | 2026-07-09 | alimentacao | 54.90 | 54,90 | RECUSADO | **0,00** | `DUPLICIDADE` |
| 8 | d-008 | 2026-04-15 | alimentacao | 41.00 | 41,00 | RECUSADO | **0,00** | `FORA_COMPETENCIA` |
| 9 | d-009 | 2026-07-11 | transporte_urbano | -45.00 | −45,00 | RECUSADO | **0,00** | `VALOR_NAO_POSITIVO` |
| 10 | d-010 | 2026-07-14 | hospedagem | 480.00 | 480,00 | PARCIAL | **250,00** | `TETO_HOSPEDAGEM_APLICADO` |
| 11 | d-011 | 2026-07-15 | alimentacao | 33.333 | **33,33** | INTEGRAL | **33,33** | — |
| 12 | d-012 | 2026-07-18 | alimentacao | 47.20 | 47,20 | INTEGRAL | **47,20** | — |
| 13 | d-013 | 2026-07-22 | hospedagem | 690.00 | 690,00 | RECUSADO | **0,00** | `NOTA_FISCAL_AUSENTE` |
| 14 | d-014 | 2026-07-31 | **alimentacao** | 61.00 | 61,00 | PARCIAL | **60,00** | `TETO_DIARIO_APLICADO` |

**`total_reembolsavel` = R$ 585,43**

A coluna "Motivos" desta tabela é uma **representação abreviada**: exibe apenas o `codigo` de cada objeto de motivo. Na saída real, cada motivo é um objeto com `codigo`, `regra` e `campo`, conforme 4.3.

Único item cujo valor normalizado difere do informado: d-011. Único cuja categoria normalizada difere da informada: d-014 (`ALIMENTACAO`).

---

## 5. Regras de negócio

### RN-001 — Envelope processável

**Regra:** o arquivo só é apurado quando satisfaz integralmente os requisitos de 4.1. Envelope inválido não produz apuração parcial. Defeitos no bloco opcional `colaborador` — ausente, nulo, de tipo errado ou com campos de tipo errado — nunca invalidam o arquivo nem qualquer item: os metadados correspondentes tornam-se nulos na saída.
**Origem:** contrato de entrada (lacuna da política).
**Aceite:** entrada com `periodo.inicio` `2026-07-31` e `periodo.fim` `2026-07-01` não produz resultado algum. Entrada com `despesas: []` produz `resultados` vazio e `total_reembolsavel` `0,00`. Entrada com `colaborador` igual a um texto é processada normalmente, com os três metadados nulos na saída.

### RN-002 — Contrato do item

**Regra:** cada elemento de `despesas` deve ser um objeto contendo os sete campos de 4.2 com os tipos e restrições declarados. Elemento que não é objeto é recusado com o motivo único `ITEM_TIPO_INVALIDO`, sem que nenhuma regra de negócio seja avaliada e sem gerar motivos de campo ausente; o registro resultante traz `id`, `valor_informado` e `valor_normalizado` nulos, `valor_reembolsavel` `0,00` e `indice_entrada` preservado. Sendo objeto, campo **ausente**, **nulo**, de **tipo inválido** ou de **formato inválido** recusa o item, com um motivo por campo defeituoso, classificado pela regra fechada da subseção "Classificação do erro estrutural" (4.2). Em qualquer caso o arquivo não é interrompido.
**Origem:** contrato de entrada.
**Aceite:** item com `data: "31/07/2026"` e `valor: "72,50"` é recusado com dois motivos, nesta ordem — `CAMPO_FORMATO_INVALIDO` com `campo` igual a `despesa.data`, depois `CAMPO_TIPO_INVALIDO` com `campo` igual a `despesa.valor` —, porque `despesa.data` precede `despesa.valor` na ordem canônica de contrato. `despesa.categoria` igual a um número é recusado com `CAMPO_TIPO_INVALIDO` e `campo` igual a `despesa.categoria`; `despesa.id` igual a texto vazio é recusado com `CAMPO_FORMATO_INVALIDO` e `campo` igual a `despesa.id`. Um elemento de `despesas` igual ao texto `"despesa"` produz um único registro recusado, com motivo único `ITEM_TIPO_INVALIDO`, `campo` nulo, `id` nulo, `valor_informado` nulo e `indice_entrada` preservado.

### RN-003 — Unicidade de `despesa.id`

**Regra:** `despesa.id` é único no arquivo. Verificada apenas entre IDs estruturalmente válidos, **todas** as ocorrências que compartilham um mesmo valor recebem `ID_DUPLICADO`. Não se preserva "primeira ocorrência".
**Origem:** rastreabilidade (lacuna da política).
**Aceite:** três itens com `despesa.id` `"d-100"` produzem três registros recusados com `ID_DUPLICADO` e reembolsável `0,00`.

### RN-004 — Normalização monetária

**Regra:** todo valor válido é normalizado para duas casas decimais, arredondamento decimal **meio para cima**. Todas as regras posteriores usam o valor normalizado.
**Origem:** política do RH, itens 1 a 5 (limites com duas casas).
**Aceite:** `33.333` → `33,33`; `33.335` → `33,34`; `33.345` → `33,35`; `100.004` → `100,00`; `100.005` → `100,01`.

### RN-005 — Normalização de categoria

**Regra:** `categoria` é normalizada por remoção de espaços das pontas, insensibilidade a caixa e insensibilidade a acentos. O resultado deve corresponder exatamente a `alimentacao`, `transporte_urbano` ou `hospedagem`. Nenhuma outra transformação ocorre.
**Origem:** política do RH, item 9.
**Aceite:** `ALIMENTACAO`, `Alimentação` e ` alimentacao ` reconhecem `alimentacao`. `transporte urbano` **não** reconhece `transporte_urbano`.

### RN-006 — Valor não positivo

**Regra:** item cujo valor normalizado seja menor ou igual a zero é recusado com `VALOR_NAO_POSITIVO`. Não abate nada, não agrega, não consome teto e não altera o total.
**Origem:** lacuna da política (nenhum item prevê valores negativos).
**Aceite:** `-45.00` é recusado com reembolsável `0,00` e o total do período não é reduzido em 45,00.

### RN-007 — Categorias reembolsáveis

**Regra:** apenas `alimentacao`, `transporte_urbano` e `hospedagem` são reembolsáveis. Qualquer outra categoria normalizada é recusada com `CATEGORIA_FORA_POLITICA`.
**Origem:** política do RH, item 9.
**Aceite:** `coworking` de R$ 89,00 com nota é recusado, reembolsável `0,00`.

### RN-008 — Elegibilidade temporal

**Regra:** item é temporalmente elegível quando `periodo.inicio ≤ data ≤ periodo.fim`, com ambas as bordas inclusivas. Fora disso, `FORA_COMPETENCIA`.
**Origem:** política do RH, item 7.
**Aceite:** `2026-04-15` na janela de julho é recusado. `2026-07-01` e `2026-07-31` são elegíveis.

### RN-009 — Nota fiscal obrigatória

**Regra:** quando o valor normalizado do item for **estritamente maior** que R$ 100,00 e `tem_nota_fiscal` for falso, o item é recusado com `NOTA_FISCAL_AUSENTE`. A comparação usa o valor individual normalizado, antes de qualquer corte por teto.
**Origem:** política do RH, item 5.
**Aceite:** `100,00` sem nota é elegível; `100,01` sem nota é recusado; `690,00` sem nota é recusado sem que qualquer teto seja calculado.

### RN-010 — Duplicidade econômica

**Regra:** dois itens são duplicados quando coincidem exatamente em `data`, `categoria` normalizada, `valor` normalizado, `fornecedor` como recebido e `descricao` como recebida. `despesa.id` e `tem_nota_fiscal` não integram a chave. Avaliada apenas entre itens sem nenhum motivo anterior de recusa. A primeira ocorrência em ordem de `indice_entrada` é mantida; as posteriores recebem `DUPLICIDADE`.
**Origem:** política do RH, item 8.
**Aceite:** dois itens iguais de R$ 54,90 em `2026-07-09` produzem 54,90 no primeiro e 0,00 no segundo. Itens de R$ 100,00 e R$ 100,01 do mesmo dia e fornecedor **não** são duplicados.

### RN-011 — Limite diário de alimentação

**Regra:** R$ 60,00 por data, aplicado ao conjunto dos itens elegíveis de `alimentacao` daquela data.
**Origem:** política do RH, item 1.
**Aceite:** R$ 72,50 e R$ 38,00 na mesma data rendem R$ 60,00 no total daquela data.

### RN-012 — Limite diário de transporte urbano

**Regra:** R$ 80,00 por data, aplicado ao conjunto dos itens elegíveis de `transporte_urbano` daquela data.
**Origem:** política do RH, item 2.
**Aceite:** item elegível de R$ 100,00, sozinho na data, rende R$ 80,00.

### RN-013 — Limite individual de hospedagem

**Regra:** R$ 250,00 por lançamento. Hospedagem **não** participa da agregação por data: cada lançamento é avaliado isoladamente. O conteúdo da descrição não altera o teto.
**Origem:** política do RH, item 3.
**Aceite:** lançamento de R$ 480,00 descrito como "2 diarias" rende R$ 250,00. Alterar o texto da `descricao` desse lançamento não altera o teto individual aplicado, que permanece R$ 250,00. Dois lançamentos de hospedagem na mesma data podem render até R$ 500,00.

### RN-014 — Reembolso parcial

**Regra:** ultrapassado o teto, reembolsa-se até o teto e o excedente não é reembolsado. O agregado diário nunca é recusado integralmente por ultrapassagem, nem o lançamento de hospedagem.
**Origem:** política do RH, item 4.
**Aceite:** R$ 61,00 de alimentação, sozinho na data, rende R$ 60,00 — não R$ 0,00.

### RN-015 — Distribuição do teto diário

**Regra:** dentro de uma data e categoria com teto diário, os itens elegíveis consomem o saldo em ordem crescente de `indice_entrada`. Cada item é pago integralmente enquanto houver saldo; o item que ultrapassa o saldo é pago parcialmente; os posteriores recebem `NAO_REEMBOLSADO_TETO_ESGOTADO`. Não se aplica a hospedagem, que não possui saldo compartilhado.
**Origem:** política do RH, itens 1 e 2 (unidade "por dia").
**Aceite:** com itens de R$ 72,50 e R$ 38,00 nessa ordem, o primeiro rende R$ 60,00 e o segundo R$ 0,00 com decisão `NAO_REEMBOLSADO_TETO_ESGOTADO`.

### RN-016 — Ampliação por viagem (efeito nulo nesta versão)

**Regra:** o item 6 da política não produz efeito. Nenhuma despesa é classificada como de colaborador em viagem, e nenhum limite é ampliado. A condição não é inferida a partir do conteúdo semântico de `descricao` ou `fornecedor`, nem da categoria, da existência de hospedagem ou de qualquer campo desconhecido.
**Origem:** política do RH, item 6.
**Aceite:** numa entrada com **um único item elegível**, substituir na `descricao` um texto neutro por termos como "aeroporto" ou "hotel" não amplia teto algum e não altera o `valor_reembolsavel`. Um campo `em_viagem: true` na entrada não altera resultado algum. O cenário usa um item único justamente para isolar a ausência de inferência de viagem: `descricao` integra a chave de duplicidade de RN-010 e, com dois ou mais itens, alterá-la pode legitimamente mudar o resultado por outro caminho.

### RN-017 — Composição da saída

**Regra:** toda posição da lista `despesas` produz exatamente um registro de saída, na ordem da entrada, com os campos de 4.3, uma única decisão final e a lista de objetos de motivo ordenada conforme 8.3. Cada objeto de motivo declara `codigo`, a `regra` que o produziu e o `campo` quando o motivo for estrutural.
**Origem:** auditabilidade (lacuna da política).
**Aceite:** entrada com 14 itens produz 14 registros; nenhum item desaparece, inclusive os recusados. Todo objeto de motivo apresentado traz um `codigo` do vocabulário de 4.5 e a `regra` correspondente indicada naquela tabela.

### RN-018 — Total do período

**Regra:** `total_reembolsavel` é a soma dos `valor_reembolsavel` apresentados nos registros de saída.
**Origem:** auditabilidade.
**Aceite:** para `exemplos/despesas-exemplo.json`, o total é R$ 585,43 e coincide com a soma da coluna de 4.7.

---

## 6. Ambiguidades identificadas e decisões

> Esta seção é o coração da spec. Uma ambiguidade resolvida no código sem registro
> aqui conta como não resolvida.

Dezoito ambiguidades e as subdecisões que cada uma gerou. Classificação: **U** unidade de aplicação · **F** fronteira · **D** dado ausente · **O** outra.

### AMB-001 — Unidade do limite diário · U

**Texto original do RH:** "Alimentação tem limite de R$ 60 por dia." / "Transporte urbano tem limite de R$ 80 por dia."
**O que não está claro:** teto do total do dia, ou teto de cada despesa individual?
**Decisão:** teto do total dos itens elegíveis da mesma categoria na mesma data, independentemente da quantidade de lançamentos e de fornecedores. Restrito a alimentação e transporte urbano; hospedagem é avaliada por lançamento. A agregação não cruza arquivos.
**Justificativa:** a política diz "por dia"; a leitura por despesa tornaria a palavra "dia" inerte.
**Regra afetada:** RN-011, RN-012, RN-013

### AMB-002 — Significado de "reembolsadas parcialmente" · O

**Texto original do RH:** "Despesas acima do limite são reembolsadas parcialmente."
**O que não está claro:** paga-se o teto e corta-se o excedente, recusa-se o item inteiro, ou paga-se um percentual do excedente?
**Decisão:** reembolsa-se até o teto aplicável; o excedente não é reembolsado. O agregado diário da categoria nunca é recusado integralmente por ultrapassagem.
**Justificativa:** corresponde diretamente a reembolso parcial e evita que uma ultrapassagem de R$ 1,00 elimine todo o reembolso; o percentual exigiria um número que a política não fornece.
**Regra afetada:** RN-014

### AMB-003 — Fronteira de "acima de R$ 100" · F

**Texto original do RH:** "Nota fiscal é obrigatória acima de R$ 100."
**O que não está claro:** R$ 100,00 exatos exigem nota?
**Decisão:** não. A obrigatoriedade vale apenas para valor estritamente maior que R$ 100,00.
**Justificativa:** "acima de" exclui o próprio valor; adotar "maior ou igual" seria restringir sem base textual.
**Regra afetada:** RN-009

### AMB-004 — Consequência da ausência de nota · O

**Texto original do RH:** "Nota fiscal é obrigatória acima de R$ 100."
**O que não está claro:** recusa integral, reembolso limitado a R$ 100,00, ou pendência documental?
**Decisão:** recusa integral do item.
**Justificativa:** "obrigatória" é linguagem de condição, não de graduação; liberar os primeiros R$ 100,00 premiaria o fracionamento de despesas.
**Regra afetada:** RN-009

### AMB-005 — Base de comparação do gatilho de nota · U

**Texto original do RH:** "Nota fiscal é obrigatória acima de R$ 100."
**O que não está claro:** compara-se o item individual, o total do dia, ou o valor já cortado pelo teto?
**Decisão:** o valor individual, após a normalização monetária e antes de qualquer corte por teto.
**Justificativa:** a nota documenta a despesa como ocorreu; a obrigatoriedade não pode depender de outras despesas do dia nem do valor que a empresa decidirá pagar. Comparar contra o valor cortado seria circular, pois o corte depende de quais itens são elegíveis.
**Regra afetada:** RN-004, RN-009

### AMB-006 — Caracterização de "em viagem" · D

**Texto original do RH:** "Colaborador em viagem tem limites ampliados em 50%."
**O que não está claro:** o que caracteriza estar em viagem? A entrada não possui esse campo.
**Decisão:** nenhuma despesa é identificada como de colaborador em viagem. A condição não é inferida por descrição, fornecedor, categoria ou existência de hospedagem.
**Justificativa:** termos como "aeroporto" ou "hotel" não são evidência estruturada; inferi-los faria duas entradas estruturalmente equivalentes receberem limites diferentes por causa de texto livre.
**Regra afetada:** RN-016
**Subdecisão AMB-006/JANELA:** não aplicável — sem identificação de viagem, não há janela a calcular.

### AMB-007 — Escopo da ampliação de 50% · U

**Texto original do RH:** "Colaborador em viagem tem limites ampliados em 50%."
**O que não está claro:** quais limites seriam ampliados, e a fronteira de R$ 100 da nota acompanharia?
**Decisão:** não aplicável nesta versão. Nenhum limite é ampliado e a fronteira documental permanece em R$ 100,00 para todos os itens.
**Justificativa:** decorre de AMB-006. Especificar quais limites seriam ampliados exigiria decisão de produto e alteração explícita do contrato de entrada.
**Regra afetada:** RN-016

### AMB-008 — Quantidade de diárias de hospedagem · D

**Texto original do RH:** "Hospedagem tem limite de R$ 250 por diária."
**O que não está claro:** "diária" é a unidade da regra, e a entrada não possui campo de quantidade. O número aparece apenas em texto livre, em formatos distintos: "2 diarias" e "3 noites".
**Decisão:** cada lançamento de hospedagem representa uma única diária. Teto de R$ 250,00 por item. A descrição é informativa e não altera o teto.
**Justificativa:** extrair quantidade de texto livre exigiria regras de interpretação que a política não fornece; mantém o mesmo princípio de AMB-006.
**Regra afetada:** RN-013
**Subdecisões AMB-008/FORMATO e AMB-008/LOCAL:** não aplicáveis — nenhuma quantidade é extraída.
**Consequência aceita:** uma estadia lançada em três itens de R$ 230,00 rende R$ 690,00, enquanto a mesma estadia em um item de R$ 690,00 rende R$ 250,00. A entrada não possui identificador de estadia que permita reconhecer fragmentação com segurança, e detectá-la por descrição, fornecedor ou proximidade de datas está fora de escopo.

### AMB-009 — Qual data define a competência · D

**Texto original do RH:** "Despesas devem ser lançadas dentro do período de competência."
**O que não está claro:** a política fala em data de lançamento, que a entrada não possui — ela traz a data do fato gerador.
**Decisão:** o campo `data` é a data operacional da verificação. A descrição, inclusive expressões como "lançado com atraso", não participa.
**Justificativa:** decisão explícita de interpretação para tornar a regra operacional com o contrato disponível. Não contradiz AMB-006 nem AMB-008: aqui usam-se campos estruturados criados para representar a despesa e o período, não texto livre.
**Regra afetada:** RN-008
**Subdecisão AMB-009/FONTE:** `periodo.inicio` e `periodo.fim` são a fonte da verdade; `periodo.competencia` é rótulo informativo e não altera a janela em caso de divergência, porque início e fim expressam o intervalo com precisão e permitem períodos que não coincidam com o mês civil.

### AMB-010 — Inclusividade das bordas do período · F

**Texto original do RH:** "dentro do período de competência", com `inicio` e `fim` declarados.
**O que não está claro:** as datas de borda pertencem ao período?
**Decisão:** intervalo fechado nas duas extremidades.
**Justificativa:** convenção de período contábil; o primeiro e o último dia pertencem à apuração.
**Regra afetada:** RN-008

### AMB-011 — Definição de duplicata · U

**Texto original do RH:** "Duplicatas devem ser tratadas."
**O que não está claro:** quais campos definem que dois lançamentos são o mesmo?
**Decisão:** igualdade exata de `data`, `categoria` normalizada, `valor` normalizado, `fornecedor` e `descricao`. `despesa.id` não integra a chave porque existe para diferenciar registros; `tem_nota_fiscal` não integra porque representa situação documental, não identidade econômica — alterar apenas esse campo não pode permitir duplo reembolso. Sem correspondência aproximada, tolerância monetária ou interpretação semântica.
**Justificativa:** correspondência aproximada produziria falso positivo demonstrável — os itens de R$ 100,00 e R$ 100,01 do mesmo dia e fornecedor são corridas distintas.
**Regra afetada:** RN-010
**Subdecisão AMB-011/POPULACAO:** a detecção roda somente sobre itens aprovados em todas as validações individuais. Um lançamento já inelegível não impede o processamento de outro válido nem o contamina.

### AMB-012 — Tratamento da duplicata · O

**Texto original do RH:** "Duplicatas devem ser tratadas."
**O que não está claro:** "tratadas" como — remover uma, recusar todas, ou apenas sinalizar?
**Decisão:** mantém-se a primeira ocorrência em ordem de `indice_entrada`; todas as posteriores são recusadas com `DUPLICIDADE`. Com três ou mais ocorrências, apenas a primeira permanece.
**Justificativa:** "tratar" implica alterar o resultado, o que exclui a simples sinalização; a ordem de entrada dá critério determinístico e auditável.
**Regra afetada:** RN-010
**Subdecisão AMB-012/CLASSIFICACAO:** duplicidade é regra de elegibilidade, aplicada após as validações individuais e antes da agregação. A ocorrência recusada recebe zero, não agrega e não consome saldo.

### AMB-013 — Valores não positivos · D

**Texto original do RH:** nenhum. A política pressupõe valores positivos em todos os nove itens.
**O que não está claro:** o arquivo de exemplo contém um lançamento de −R$ 45,00 descrito como estorno.
**Decisão:** todo item com valor normalizado menor ou igual a zero é inválido por item, recusado com `VALOR_NAO_POSITIVO`. Não abate no dia, não abate no total, não participa de duplicidade e não consome teto. Depende exclusivamente do campo `valor`; a descrição não é interpretada para classificar estorno, cancelamento ou crédito.
**Justificativa:** a política define reembolso de despesas e não especifica como créditos compensam despesas; implementar abatimento criaria regra financeira sem base na política.
**Regra afetada:** RN-006
**Subdecisões:** `AMB-013/PISO` não aplicável, pois nenhum saldo devedor pode ser produzido. `AMB-013/GATILHO` não aplicável, pois a regra documental não alcança valor não positivo. `AMB-013/DUPLICIDADE` não aplicável, pois o item já sai da população por AMB-011/POPULACAO.

### AMB-014 — Precisão e arredondamento monetário · F

**Texto original do RH:** todos os limites são expressos com duas casas decimais; a entrada traz um valor com três.
**O que não está claro:** arredondar na entrada ou na saída, e com qual modo de desempate?
**Decisão:** normalização para duas casas na entrada, arredondamento decimal meio para cima. Todas as regras posteriores usam o valor normalizado — não positivo, gatilho de nota, chave de duplicidade, agregação, tetos, distribuição, reembolsável e total.
**Justificativa:** evita divergência entre a soma dos itens apresentados e o total apresentado.
**Regra afetada:** RN-004
**Fronteiras deslocadas por esta decisão:** o menor valor informado capaz de exigir nota passa a ser `100.005`, e o menor valor informado que sobrevive à validação de positividade passa a ser `0.005`.

### AMB-015 — Normalização de categoria · F

**Texto original do RH:** "Categorias fora da política não são reembolsáveis."
**O que não está claro:** como uma categoria é identificada — a comparação é sensível a caixa, acento e espaço?
**Decisão:** remoção de espaços das pontas, comparação insensível a caixa e a acentos, seguida de correspondência exata contra `alimentacao`, `transporte_urbano` e `hospedagem`. Sem substituição de espaço por sublinhado, correção ortográfica, correspondência aproximada, interpretação por descrição ou sinônimos.
**Justificativa:** categoria é vocabulário controlado; reconhecer variações de escrita de um valor de lista fechada não é interpretação semântica.
**Regra afetada:** RN-005, RN-007
**Subdecisão AMB-015/ESCOPO:** a normalização textual alcança **somente** `categoria`. `fornecedor` e `descricao` são comparados exatamente como informados, porque são texto livre — dobrar caixa e acentos ali equivaleria a declarar que dois textos diferentes significam a mesma coisa. A assimetria é deliberada: `ALIMENTACAO` e `alimentacao` são a mesma categoria, mas `Almoco` e `almoço` são descrições diferentes.
**Subdecisão AMB-015/CONTRATO:** campo obrigatório ausente, nulo, de tipo inválido ou de formato inválido recusa somente o item, sem coerção de tipo e sem valores padrão. Campos desconhecidos são ignorados silenciosamente e nunca alteram o resultado.
**Subdecisão AMB-015/ENVELOPE:** erro dentro de um item recusa apenas o item; ausência ou má-formação da estrutura necessária ao arquivo invalida o arquivo inteiro, sem apuração parcial.

### AMB-016 — Ordem e precedência das validações · O

**Texto original do RH:** os nove itens da política, enunciados sem qualquer precedência entre si.
**O que não está claro:** a ordem muda o resultado — deduplicar antes ou depois do teto altera o valor pago; recusar por nota antes ou depois de agregar altera o saldo disponível.
**Decisão:** ordem canônica única, declarada na seção 8. Todas as regras aplicáveis e avaliáveis são verificadas, e o item recusado apresenta **todos** os motivos encontrados; a ordem garante determinismo, mas não serve para ocultar motivos adicionais.
**Justificativa:** só uma ordem declarada torna o resultado reprodutível e auditável.
**Regra afetada:** todas
**Subdecisão AMB-016/NF:** a verificação de nota ocorre antes da agregação; item recusado não consome saldo. Absorvida pelo enunciado geral.
**Subdecisão AMB-016/EXCLUSOES:** matriz de dependências e lista fechada de exclusões, na seção 8.

### AMB-017 — Distribuição do teto entre itens do dia · U

**Texto original do RH:** decorre dos itens 1, 2 e 4 combinados com a exigência de justificar cada decisão.
**O que não está claro:** com teto agregado e saída por item, qual item absorve o corte?
**Decisão:** os itens elegíveis consomem o saldo em ordem crescente de `indice_entrada`. Cada item é pago integralmente enquanto houver saldo; o que ultrapassa é pago parcialmente; os posteriores recebem zero com decisão `NAO_REEMBOLSADO_TETO_ESGOTADO`.
**Justificativa:** preservar a ordem da entrada fornece critério determinístico e auditável de qual item consumiu o saldo.
**Regra afetada:** RN-015
**Distinção que a saída preserva:** valor zero por esgotamento de teto **não é recusa**. São estados diferentes, com códigos diferentes, porque significam coisas diferentes para quem audita.

### AMB-018 — Representação dos itens na saída · O

**Texto original do RH:** nenhum. A política não descreve saída; o esquema é definido por esta spec.
**O que não está claro:** itens não reembolsáveis aparecem? Com qual identificação e quantas justificativas?
**Decisão:** toda posição da lista `despesas` aparece exatamente uma vez, com `indice_entrada`, o `id` original quando disponível, valor informado, valor normalizado quando calculável, valor reembolsável, uma única decisão final e uma ou mais justificativas vinculadas às regras. Cada justificativa é um **objeto** com `codigo`, `regra` e `campo` — não um código solto —, porque um código sozinho não diz de qual campo o defeito veio nem qual regra o produziu, e as duas informações são exatamente o que torna a decisão auditável.
**Justificativa:** nenhum lançamento pode desaparecer do resultado; colaborador e financeiro precisam auditar individualmente cada decisão e chegar, a partir dela, à regra que a gerou.
**Regra afetada:** RN-017
**Subdecisão AMB-018/ESCOPO:** `colaborador.id`, `colaborador.nome`, `colaborador.centro_custo` e `periodo.competencia` são metadados opcionais, preservados na saída para rastreabilidade e nulos quando ausentes, malformados ou de tipo inesperado; nunca alteram resultado financeiro nem invalidam arquivo ou item. BRL é a moeda presumida.

---

## 7. Casos de borda

| Caso | Entrada | Comportamento esperado | Regra |
|---|---|---|---|
| Valor igual ao gatilho de nota | `100.00`, sem nota | Elegível; nota não exigida | RN-009 |
| Um centavo acima do gatilho | `100.01`, sem nota | Recusado, `NOTA_FISCAL_AUSENTE` | RN-009 |
| Fronteira deslocada pelo arredondamento | `100.004`, sem nota | Normaliza `100,00`; elegível | RN-004, RN-009 |
| Fronteira deslocada pelo arredondamento | `100.005`, sem nota | Normaliza `100,01`; recusado | RN-004, RN-009 |
| Um real acima do teto diário | `61.00` de alimentação, sozinho na data | Parcial, `60,00` — não zero | RN-011, RN-014 |
| Primeiro dia do período | `data` = `periodo.inicio` | Temporalmente elegível | RN-008 |
| Último dia do período | `data` = `periodo.fim` | Temporalmente elegível | RN-008 |
| Data fora do período | `2026-04-15` em janela de julho | Recusado, `FORA_COMPETENCIA` | RN-008 |
| Terceira casa decimal | `33.333` | Normaliza `33,33`; informado preserva `33.333` | RN-004 |
| Desempate do arredondamento | `33.345` | Normaliza `33,35` | RN-004 |
| Categoria em caixa alta | `ALIMENTACAO` | Reconhecida como `alimentacao` | RN-005 |
| Categoria com espaço | `transporte urbano` | Não reconhecida; `CATEGORIA_FORA_POLITICA` | RN-005, RN-007 |
| Categoria fora da lista | `coworking` | Recusado; não chega à etapa de teto | RN-007 |
| Duas despesas idênticas | Mesma chave econômica | Primeira integral; segunda `DUPLICIDADE` | RN-010 |
| Valores próximos, mesmo dia e fornecedor | `100.00` e `100.01` | **Não** são duplicatas | RN-010 |
| Valor negativo | `-45.00` | Recusado, `VALOR_NAO_POSITIVO`; total inalterado | RN-006 |
| Valor zero | `0.00` | Recusado, `VALOR_NAO_POSITIVO` | RN-006 |
| Valor que normaliza para zero | `0.004` | Normaliza `0,00`; recusado | RN-004, RN-006 |
| Valor inteiro | `72` | Normaliza `72,00`; processado normalmente | RN-004 |
| Hospedagem multi-diária | `480.00`, descrição "2 diarias" | Parcial, `250,00` | RN-013 |
| Duas hospedagens na mesma data | Dois lançamentos elegíveis | Até `500,00` na data | RN-013 |
| Hospedagem sem nota | `690.00`, sem nota | Recusado antes de qualquer teto | RN-009 |
| `despesa.id` repetido | Dois itens com o mesmo ID válido | **Ambos** recusados, `ID_DUPLICADO` | RN-003 |
| `despesa.id` inválido | Texto vazio | `CAMPO_FORMATO_INVALIDO`; não entra na verificação de repetição | RN-002, RN-003 |
| Campo malformado | `valor: "72,50"` | Recusado, `CAMPO_TIPO_INVALIDO`; `valor_normalizado` nulo; `valor_informado` preserva `"72,50"` | RN-002 |
| Dois campos malformados | `data: "31/07/2026"` e `valor: "72,50"` | Dois motivos, `despesa.data` antes de `despesa.valor` | RN-002 |
| Categoria de tipo inválido | `categoria: 123` | Recusado, `CAMPO_TIPO_INVALIDO` | RN-002 |
| Data com formato correto e inexistente | `data: "2026-02-30"` | Recusado, `CAMPO_FORMATO_INVALIDO` | RN-002 |
| Nota fiscal de tipo inválido | `tem_nota_fiscal: "sim"` | Recusado, `CAMPO_TIPO_INVALIDO` | RN-002 |
| `despesa.valor` booleano | `valor: true` | Recusado, `CAMPO_TIPO_INVALIDO`; `valor_informado` preserva `true`; `valor_normalizado` nulo | RN-002 |
| Elemento que não é objeto | `despesas` contendo o texto `"despesa"` | Recusado com motivo único `ITEM_TIPO_INVALIDO` e `campo` nulo; `id`, `valor_informado` e `valor_normalizado` nulos; `indice_entrada` preservado | RN-002 |
| Bloco `colaborador` malformado | `colaborador: "Marina"` | Arquivo válido; os três metadados nulos | RN-001 |
| Campo desconhecido | `em_viagem: true` | Ignorado; nenhum efeito | RN-016 |
| Lista de despesas vazia | `despesas: []` | Resultados vazio, total `0,00` | RN-001 |
| Período invertido | `inicio` > `fim` | Envelope inválido; nenhuma apuração | RN-001 |
| Competência ausente | Sem `periodo.competencia` | Arquivo válido; metadado nulo na saída | RN-001 |
| Colaborador ausente | Sem bloco `colaborador` | Arquivo válido; metadados nulos | RN-001 |

---

## 8. Ordem de aplicação das regras

A política enuncia nove itens sem precedência entre si, e a ordem altera o resultado. Esta seção fixa a ordem.

### 8.1 Ordem de processamento

| # | Passo |
|---|---|
| 1 | Validar o envelope do arquivo |
| 2 | Validar presença, tipo e formato dos campos de cada item |
| 3 | Identificar `despesa.id` válidos repetidos e recusar todas as respectivas ocorrências |
| 4 | Normalizar os campos estruturalmente válidos: valor e categoria |
| 5 | Avaliar as regras individuais de negócio aplicáveis |
| 6 | Selecionar os itens aprovados em todas as validações individuais |
| 7 | Detectar e tratar a duplicidade econômica nessa população |
| 8 | Selecionar os itens elegíveis após a duplicidade |
| 9 | Aplicar agregação e tetos |
| 10 | Produzir uma saída por item, na ordem da entrada |
| 11 | Somar os valores reembolsáveis apresentados |

### 8.2 Matriz de dependências

Cada regra depende dos campos abaixo. Uma regra não é avaliada quando algum campo de que depende está estruturalmente inválido.

| Regra | Campos necessários |
|---|---|
| Unicidade de `despesa.id` | `despesa.id` |
| Normalização monetária | `despesa.valor` |
| Normalização de categoria | `despesa.categoria` |
| Valor não positivo | `despesa.valor` |
| Categoria fora da política | `despesa.categoria` |
| Competência | `despesa.data`, `periodo.inicio`, `periodo.fim` |
| Nota fiscal obrigatória | `despesa.valor`, `despesa.tem_nota_fiscal` |
| Duplicidade econômica | `despesa.data`, `despesa.categoria`, `despesa.valor`, `despesa.fornecedor`, `despesa.descricao` |
| Agregação e teto diário | `despesa.data`, `despesa.categoria`, `despesa.valor` |
| Teto individual de hospedagem | `despesa.categoria`, `despesa.valor` |

### 8.3 Ordem de apresentação dos motivos

A ordem de processamento e a ordem de apresentação são **duas ordens determinísticas distintas**, com finalidades diferentes: a primeira determina quando cada regra é avaliada; a segunda, como os resultados são apresentados.

1. `ITEM_TIPO_INVALIDO` — quando presente, é o motivo único da posição
2. Erros de campos estruturais, na ordem canônica de contrato: `despesa.id`, `despesa.data`, `despesa.categoria`, `despesa.descricao`, `despesa.fornecedor`, `despesa.valor`, `despesa.tem_nota_fiscal`
3. `ID_DUPLICADO`
4. `VALOR_NAO_POSITIVO`
5. `CATEGORIA_FORA_POLITICA`
6. `FORA_COMPETENCIA`
7. `NOTA_FISCAL_AUSENTE`
8. `DUPLICIDADE`

### 8.4 Lista fechada de exclusões de aplicabilidade

**Nenhuma outra exclusão pode ser inferida.**

1. Envelope inválido impede toda a apuração.
2. Elemento de `despesas` que não é objeto recebe `ITEM_TIPO_INVALIDO` como motivo único: nenhuma validação de campo e nenhuma regra de negócio é avaliada para aquela posição, e não são gerados os sete motivos de campo ausente.
3. Campo ausente ou malformado impede somente as regras que dependem desse campo, conforme 8.2.
4. As demais regras cujos campos estejam válidos continuam sendo avaliadas e podem produzir motivos adicionais.
5. `despesa.id` ausente, nulo, vazio, de tipo inválido ou de formato inválido produz erro estrutural no item.
6. ID estruturalmente inválido não participa da verificação de IDs repetidos; a repetição é verificada somente entre valores válidos.
7. Todas as ocorrências que compartilham o mesmo `despesa.id` válido recebem `ID_DUPLICADO`.
8. `ID_DUPLICADO` **não** impede a avaliação das regras individuais de negócio cujos campos estejam válidos — o item pode apresentar `ID_DUPLICADO` junto com outros motivos.
9. Item com `ID_DUPLICADO` não participa da detecção de duplicidade econômica, não participa da agregação, não consome teto e tem reembolsável `0,00`.
10. Valor não positivo torna a regra de nota fiscal não aplicável. Mantido explicitamente apesar de RN-009 já comparar por "maior que R$ 100": a declaração fecha a porta para uma leitura por valor absoluto, que exigiria nota também para valores muito negativos — por exemplo, −R$ 500,00 não exige nota.
11. Item recusado nas validações individuais não participa da detecção de duplicidade econômica.
12. Ocorrência recusada como duplicidade econômica não participa da agregação e não consome teto.
13. Regras de teto alcançam somente os itens que permanecerem elegíveis após todas as validações e a deduplicação.

**Exemplo de motivos múltiplos:** item de R$ 500,00, categoria `coworking`, sem nota, datado fora da janela, com todos os campos estruturalmente válidos, apresenta três motivos — `CATEGORIA_FORA_POLITICA`, `FORA_COMPETENCIA` e `NOTA_FISCAL_AUSENTE`.

**Exemplo de exclusão:** item de −R$ 500,00, categoria `coworking`, sem nota, apresenta `VALOR_NAO_POSITIVO` e `CATEGORIA_FORA_POLITICA`, mas **não** apresenta `NOTA_FISCAL_AUSENTE`.

### 8.5 Estados possíveis por categoria

| Categoria | Estados possíveis |
|---|---|
| `alimentacao`, `transporte_urbano` | `INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO` (teto diário), `NAO_REEMBOLSADO_TETO_ESGOTADO`, `RECUSADO` |
| `hospedagem` | `INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO` (teto individual), `RECUSADO`. **Nunca** `NAO_REEMBOLSADO_TETO_ESGOTADO`, por não participar de saldo compartilhado |
| Fora da política | Apenas `RECUSADO`; não alcança a etapa de teto |

---

## 9. Rastreabilidade da política do RH

Cada item da política v3 aparece abaixo. Um item pode originar mais de uma ambiguidade e mais de uma regra. Regras que não derivam da política — contrato de entrada, rastreabilidade e auditabilidade — estão na última linha.

| Item da política | Ambiguidades | Regras | Critérios de aceite |
|---|---|---|---|
| 1 — Alimentação R$ 60/dia | AMB-001, AMB-002, AMB-017 | RN-011, RN-014, RN-015 | CA-004, CA-005, CA-006 |
| 2 — Transporte urbano R$ 80/dia | AMB-001, AMB-002, AMB-017 | RN-012, RN-014, RN-015 | CA-004, CA-005 |
| 3 — Hospedagem R$ 250/diária | AMB-001, AMB-008 | RN-013 | CA-007 |
| 4 — Acima do limite, parcial | AMB-002, AMB-017 | RN-014, RN-015 | CA-005, CA-006 |
| 5 — Nota fiscal acima de R$ 100 | AMB-003, AMB-004, AMB-005 | RN-009 | CA-008, CA-009 |
| 6 — Viagem amplia limites em 50% | AMB-006, AMB-007 | RN-016 | CA-010 |
| 7 — Período de competência | AMB-009, AMB-010 | RN-008 | CA-011, CA-012 |
| 8 — Duplicatas devem ser tratadas | AMB-011, AMB-012 | RN-010 | CA-013, CA-014 |
| 9 — Categorias fora da política | AMB-015 | RN-005, RN-007 | CA-015, CA-016 |
| — (fora da política) | AMB-013, AMB-014, AMB-016, AMB-018 | RN-001, RN-002, RN-003, RN-004, RN-006, RN-017, RN-018 | CA-001, CA-002, CA-003, CA-017 a CA-023 |

---

## 10. Critérios de aceite

O sistema está pronto quando todos os itens abaixo forem verificáveis sem leitura de código.

- [ ] **CA-001** — Processar `exemplos/despesas-exemplo.json` produz `total_reembolsavel` igual a **R$ 585,43**.
- [ ] **CA-002** — A saída daquele arquivo contém 14 registros, na ordem da entrada, com `indice_entrada` de 1 a 14, e cada linha coincide com a tabela 4.7 em decisão, valor reembolsável e motivos.
- [ ] **CA-003** — A soma dos `valor_reembolsavel` apresentados é igual ao `total_reembolsavel` apresentado.
- [ ] **CA-004** — Duas despesas de alimentação na mesma data somando mais de R$ 60,00 rendem exatamente R$ 60,00 naquela data.
- [ ] **CA-005** — Uma despesa de R$ 61,00 de alimentação, sozinha na data, rende R$ 60,00 e não R$ 0,00.
- [ ] **CA-006** — Com itens de R$ 72,50 e R$ 38,00 nessa ordem no mesmo dia, o primeiro rende R$ 60,00 com `PARCIALMENTE_REEMBOLSADO` e o segundo rende R$ 0,00 com `NAO_REEMBOLSADO_TETO_ESGOTADO` — não `RECUSADO`.
- [ ] **CA-007** — Uma hospedagem de R$ 480,00 descrita como "2 diarias" rende R$ 250,00, e alterar o texto da `descricao` não altera o teto individual de R$ 250,00 aplicado a esse lançamento.
- [ ] **CA-008** — Uma despesa de exatamente R$ 100,00 sem nota é elegível; uma de R$ 100,01 sem nota é recusada.
- [ ] **CA-009** — Um valor informado de `100.004` sem nota é elegível e um de `100.005` sem nota é recusado.
- [ ] **CA-010** — Numa entrada com um único item elegível, trocar na `descricao` um texto neutro por termos como "aeroporto" ou "hotel" não amplia teto algum e não altera o `valor_reembolsavel`; um campo `em_viagem: true` também não altera resultado algum. O cenário usa item único para isolar a ausência de inferência de viagem, já que `descricao` integra a chave de duplicidade.
- [ ] **CA-011** — Uma despesa datada em `2026-04-15`, com janela de julho, é recusada com `FORA_COMPETENCIA`.
- [ ] **CA-012** — Despesas datadas exatamente em `periodo.inicio` e em `periodo.fim` são temporalmente elegíveis.
- [ ] **CA-013** — Dois itens idênticos exceto pelo `despesa.id` produzem o primeiro integral e o segundo recusado com `DUPLICIDADE`.
- [ ] **CA-014** — Itens de R$ 100,00 e R$ 100,01, mesmo dia e mesmo fornecedor, não são tratados como duplicatas.
- [ ] **CA-015** — Uma despesa com categoria `ALIMENTACAO` é processada como `alimentacao`.
- [ ] **CA-016** — Uma despesa com categoria `coworking` é recusada com `CATEGORIA_FORA_POLITICA` e reembolsável R$ 0,00.
- [ ] **CA-017** — Uma despesa de −R$ 45,00 é recusada com `VALOR_NAO_POSITIVO` e não reduz o total do período.
- [ ] **CA-018** — Um valor informado de `33.333` aparece como `valor_informado` `33.333`, `valor_normalizado` `33,33` e `valor_reembolsavel` `33,33`.
- [ ] **CA-019** — Dois itens com o mesmo `despesa.id` válido são **ambos** recusados com `ID_DUPLICADO`.
- [ ] **CA-020** — Uma entrada com `periodo.inicio` posterior a `periodo.fim` não produz apuração alguma; uma entrada com `despesas: []` produz resultados vazio e total R$ 0,00.
- [ ] **CA-021** — Um item com `data: "31/07/2026"` e `valor: "72,50"` apresenta dois motivos estruturais na ordem canônica de contrato: `CAMPO_FORMATO_INVALIDO` com `campo` igual a `despesa.data`, depois `CAMPO_TIPO_INVALIDO` com `campo` igual a `despesa.valor`.
- [ ] **CA-022** — Um elemento de `despesas` que não seja objeto produz exatamente um registro `RECUSADO`, com `indice_entrada` preservado, `id` nulo, `valor_informado` nulo, `valor_normalizado` nulo, `valor_reembolsavel` R$ 0,00 e um único motivo `ITEM_TIPO_INVALIDO` com `campo` nulo — sem os sete motivos de campo ausente —, e o restante do arquivo continua sendo apurado. Todo objeto de motivo da saída traz `campo` nulo ou um dos sete nomes canônicos de 4.2, e nenhum outro valor.
- [ ] **CA-023** — Um item com `despesa.id` igual a texto vazio, `despesa.data` igual a `"31/07/2026"`, `despesa.categoria` igual a um número, `despesa.valor` igual a `"72,50"` e `despesa.tem_nota_fiscal` igual a `"sim"` (demais campos válidos) é recusado com cinco motivos, na ordem canônica de contrato — `CAMPO_FORMATO_INVALIDO` (`despesa.id`), `CAMPO_FORMATO_INVALIDO` (`despesa.data`), `CAMPO_TIPO_INVALIDO` (`despesa.categoria`), `CAMPO_TIPO_INVALIDO` (`despesa.valor`), `CAMPO_TIPO_INVALIDO` (`despesa.tem_nota_fiscal`). `valor_informado` preserva `"72,50"`; `valor_normalizado` é nulo; `valor_reembolsavel` é `0,00`.

---

## 11. O que fica em aberto

Nenhum comportamento da versão atual está indefinido. Os pontos abaixo são **limitações de produto**: cada um exigiria mudança explícita do contrato de entrada ou da política para ser resolvido.

| Limitação | O que seria necessário |
|---|---|
| A regra 6 da política não produz efeito algum | Um campo estruturado de viagem na entrada, mais decisão de produto sobre quais limites são ampliados e se a fronteira documental acompanha |
| Hospedagem de várias diárias é limitada como se fosse uma | Um campo estruturado de quantidade de diárias, ou datas de entrada e saída |
| Estadia fragmentada em vários lançamentos recebe mais do que a mesma estadia em um lançamento | Um identificador de estadia que permita reconhecer fragmentação com segurança |
| Estornos e créditos não compensam despesas | Uma política de compensação que defina contra o quê o crédito abate e em que ordem |
| Contexto de consumo é ignorado — fim de semana, plantão, representação, refeição inclusa na hospedagem | Regras na política que criem limite, proibição ou tratamento distinto para esses contextos |
| Apenas BRL é suportado | Um campo de moeda e uma política de conversão cambial |
| A apuração não cruza arquivos nem colaboradores | Um contrato de entrada multi-arquivo e definição de qual limite é compartilhado |
| `coworking` não é reembolsável | Alteração da política, que hoje não prevê a categoria |

**Perguntas que não puderam ser respondidas por ausência de interlocutor.** A política v3 foi escrita pelo RH e não houve canal para esclarecimento. As leituras adotadas nas dezoito ambiguidades da seção 6 são decisões desta spec, não interpretações confirmadas pela área de origem. As que mais mudariam o resultado se a área decidisse diferente: AMB-002 (corte no teto contra recusa integral), AMB-008 (uma diária por lançamento) e AMB-004 (ausência de nota recusa o item inteiro).
