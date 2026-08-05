# Spec — Motor de Cálculo de Reembolso

**Versão:** 1.2 · **Status:** aprovado · **Última alteração:** 2026-08-05

> **Regra de ouro deste arquivo:** ele descreve o QUÊ e o PORQUÊ. Nenhuma linha
> aqui pode citar linguagem, biblioteca, classe, função ou estrutura de pasta.
> Se apareceu solução, o lugar dela é o `plan.md`.
>
> **Teste de aceitação da própria spec:** uma pessoa que nunca viu o projeto
> consegue, lendo só este arquivo, verificar se o sistema está correto?

---

## 0. Fontes oficiais do Dia 2

Esta versão incorpora o envelope oficial do Dia 2 — política de reembolso v4 —, recebido em `2026-08-05`. As fontes normativas dessa mudança são exclusivamente os cinco arquivos abaixo, em `exemplos/envelope/`, e são artefatos oficiais: não devem ser modificados por este ou por qualquer processo de especificação subsequente.

- `exemplos/envelope/00-ENVELOPE-LACRADO.md` — comunicado do RH, política v4.
- `exemplos/envelope/politica-v4.json` — tabela de limites por centro de custo.
- `exemplos/envelope/cambio.json` — taxas de câmbio por data.
- `exemplos/envelope/despesas-envelope.json` — colaborador com centro de custo cadastrado e despesas em moeda estrangeira (Rafael Nkemelu, `CC-COMERCIAL`).
- `exemplos/envelope/despesas-envelope-cc-desconhecido.json` — colaborador com centro de custo fora da tabela de política (Dani Okonkwo, `CC-SUPORTE-N2`).

Onde o texto desta spec e o conteúdo desses arquivos divergirem, os arquivos são a fonte da verdade: a spec está errada e deve ser corrigida, com o registro correspondente em `DECISIONS.md`.

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
- **Não infere condição de viagem a partir da moeda da despesa.** `despesa.moeda` diferente de `BRL` (política v4, Dia 2) é somente a unidade monetária do lançamento — não é evidência de deslocamento do colaborador, e não ativa a ampliação de 50% do item 6 da política. RN-016 continua sem efeito (AMB-023).
- **Não agrega entre arquivos.** A apuração ocorre dentro de uma única entrada; `colaborador.nome` e `colaborador.centro_custo` nunca são usados para cruzar dados.
- **Não usa `periodo.competencia`** para elegibilidade temporal. (AMB-009)
- **Não faz correspondência aproximada de categoria:** sem correção ortográfica, sinônimos ou substituição de espaço por sublinhado. `transporte urbano` não se torna `transporte_urbano`. (AMB-015)
- **Não normaliza `descricao` nem `fornecedor`.** Diferenças de caixa, acento ou espaço são diferenças reais. (AMB-015)
- **Não interpreta o conteúdo semântico de texto livre.** `descricao` e `fornecedor` não são lidos para inferir viagem, quantidade de diárias, estorno, categoria ou qualquer outro tratamento financeiro. Eles são usados exclusivamente em comparação literal de igualdade na chave de duplicidade (RN-010). A fronteira é essa: comparar duas descrições exatamente iguais é permitido; interpretar o significado da palavra "hotel" não é.
- **Não coage tipos.** `"72,50"`, `"31/07/2026"` e `"sim"` não são convertidos.
- **Não presume valores padrão** para campos obrigatórios ausentes.
- **Não reage a campos desconhecidos.** Um campo fora do contrato — inclusive um eventual `em_viagem` — é ignorado e não ativa comportamento algum.
- **Não implementa fila de aprovação manual.** O item C (opcional) do comunicado do Dia 2 — pendência de aprovação de gestor para itens com reembolsável acima de R$500 — está deliberadamente fora de escopo nesta rodada (AMB-033).
- **Não seleciona automaticamente política histórica por vigência.** `politica.vigencia` é validada e preservada como metadado (RN-021), mas nenhuma política externa anterior à v4 foi fornecida — não há, hoje, entre o quê escolher.

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

**Tolerância dos metadados opcionais.** Nenhum defeito no bloco `colaborador` invalida o arquivo nem qualquer item — mas isso não significa que o bloco é irrelevante para regra alguma (deixou de ser verdade a partir da política v4: `centro_custo` seleciona política, RN-019). Precisamente:

- Defeitos no bloco `colaborador` — ausência, nulo, tipo errado, campos malformados — nunca invalidam o arquivo nem qualquer item.
- `colaborador.id` e `colaborador.nome` permanecem apenas informativos: preservados para rastreabilidade, sem uso em regra alguma.
- `colaborador.centro_custo`, quando é texto e corresponde a uma entrada de `centros_custo` na política aplicável, seleciona a tabela de limites daquele centro (RN-019).
- `colaborador.centro_custo` ausente, nulo, de tipo inválido, ou de valor desconhecido na tabela de política, usa integralmente a política `padrao` — sem invalidar arquivo ou item.

Resumo da tolerância estrutural (RN-001), que continua valendo à risca:

| Situação | Resultado na saída |
|---|---|
| `colaborador` ausente ou nulo | Os três metadados são nulos |
| `colaborador` presente, mas não é objeto | Ignorado; os três metadados são nulos |
| `colaborador` é objeto | Cada um de `colaborador.id`, `colaborador.nome` e `colaborador.centro_custo` é preservado **apenas quando for texto** |
| Campo ausente, nulo ou de outro tipo dentro de `colaborador` | Representado como nulo |
| Campo desconhecido dentro de `colaborador` | Ignorado |

`periodo.competencia` segue o comportamento opcional já descrito: preservado quando presente no formato `AAAA-MM`, nulo em qualquer outro caso, sem efeito sobre decisão alguma.

**`colaborador.centro_custo` a partir da política v4 (Dia 2).** Deixa de ser puramente decorativo: participa da resolução da política aplicável a cada item (RN-019). Sua tolerância estrutural, porém, não muda — ausência, nulo, tipo inválido ou valor sem entrada na tabela de política continuam sem invalidar o arquivo nem qualquer item; em todos esses casos, resolve-se integralmente para a política `padrao` (RN-019, AMB-019). A comparação contra a tabela de política é textual e exata — sem trim, sem normalização de caixa ou acento, sem correspondência aproximada, pelo mesmo princípio já registrado em AMB-015/ESCOPO para campos de texto livre fora de `categoria`.

### 4.1.1 Arquivos externos: política de reembolso e câmbio

A partir da política v4, a execução passa a depender de dois documentos adicionais, externos ao arquivo de despesas e obrigatórios em toda execução.

**Contrato de execução (CLI, AMB-034).** O contrato de execução é comportamento observável do produto e é normativo desta spec, não apenas do `plan.md`:

```text
calcular --input <entrada.json> --output <saida.json> --politica <politica.json> --cambio <cambio.json>
```

- Os quatro argumentos são obrigatórios; podem aparecer em qualquer ordem; cada um aparece exatamente uma vez.
- Argumento ausente, repetido ou desconhecido → código de saída `2`. O comando anterior à política v4, só com `--input`/`--output`, agora também retorna `2` — falta `--politica` e `--cambio`, mesmo quando a entrada só tem despesas em BRL.
- Política ou câmbio inexistente, ilegível, sintaticamente inválido ou estruturalmente inválido (AMB-035) → código `2` — mesma classe já usada para `--input` ilegível, sem código novo.
- Envelope de despesas estruturalmente inválido (RN-001) continua código `3`.
- Sucesso continua código `0`; stdout permanece vazio em qualquer cenário; mensagens de erro vão para stderr.
- Uma saída (`--output`) preexistente é preservada intacta em qualquer falha global.
- Nenhum código de saída além de `0`, `2` e `3` é criado por esta versão.

**Arquivo de política (AMB-035).** Contrato estrutural fechado — "estruturalmente inválido" (RN-022) significa violar qualquer regra abaixo:

| Campo | Tipo | Obrigatório | Restrição |
|---|---|---|---|
| (raiz) | objeto | Sim | — |
| `vigencia` | texto `AAAA-MM-DD` | Sim | Representa data real do calendário (RN-021) |
| `moeda_base` | texto | Sim | Exatamente `"BRL"` |
| `padrao` | objeto | Sim | Tabela de limites por categoria, aplicável a qualquer centro de custo sem entrada própria em `centros_custo`. Pode ser vazio |
| `centros_custo` | objeto | Sim | Mapa de centro de custo para a respectiva tabela de limites por categoria. Cada centro cadastrado possui uma tabela completa e exclusiva para aquele centro; quando selecionada, ela substitui integralmente `padrao` (RN-019), e categorias que não estiverem declaradas nela não recebem fallback. Pode ser vazio |
| `nota_fiscal_obrigatoria_acima_de` | número | Sim | Não negativo |
| `versao`, `acrescimo_em_viagem_percentual`, outros | qualquer | Não | Metadados aceitos e ignorados pelas regras atuais. `acrescimo_em_viagem_percentual` **não** ativa RN-016 |

Cada tabela de categoria — dentro de `padrao` ou de um centro de custo — é um objeto que associa uma categoria de nome não vazio a um objeto com:

| Campo | Tipo | Obrigatório | Restrição |
|---|---|---|---|
| `limite` | número | Sim | Maior ou igual a zero dentro de uma tabela de `centros_custo`; **estritamente maior que zero** dentro de `padrao` (AMB-035, correção da segunda revisão) — `limite: 0` em `padrao` torna o arquivo de política estruturalmente inválido (RN-022), não uma decisão de política sobre um item |
| `periodicidade` | texto | Sim | Exatamente `"dia"` ou `"diaria"` (RN-019, AMB-036) |
| `observacao` | texto | Não | Informativa; nunca lida por regra alguma |
| outros | qualquer | Não | Ignorados |

**Arquivo de câmbio (AMB-035).** A raiz **não** é um mapa direto de data para moeda — as cotações estão aninhadas sob `taxas`:

```text
raiz
├── moeda_base       (obrigatório, exatamente "BRL")
├── fonte            (opcional, informativo)
├── observacao       (opcional, informativo)
└── taxas            (objeto, obrigatório — pode ser vazio)
    └── AAAA-MM-DD    (chave: data real do calendário)
        └── USD/EUR/… (chave: moeda em `[A-Z]{3}`)
            └── taxa  (número estritamente positivo)
```

| Campo | Tipo | Obrigatório | Restrição |
|---|---|---|---|
| (raiz) | objeto | Sim | — |
| `moeda_base` | texto | Sim | Exatamente `"BRL"` |
| `taxas` | objeto | Sim | Cada chave é uma data `AAAA-MM-DD` real; cada valor é um objeto cujas chaves seguem `[A-Z]{3}` e cujos valores são número estritamente positivo. Pode ser vazio — nesse caso toda despesa em moeda estrangeira é recusada individualmente com `MOEDA_SEM_COTACAO` (RN-020), sem invalidar o arquivo |
| `fonte`, `observacao` | texto | Não | Metadados informativos, nunca lidos por regra alguma |
| outros | qualquer | Não | Ignorados |

`taxas` cobre apenas dias úteis bancários por natureza — a ausência de uma data específica é esperada (RN-020), não um defeito estrutural do arquivo.

Política e câmbio são entrada obrigatória da execução, na mesma categoria de "arquivo de entrada" que hoje é o arquivo de despesas: ausência, ilegibilidade ou qualquer violação dos contratos estruturais acima impede toda a apuração (RN-022) — erro global, mais grave que um envelope de despesas inválido (RN-001), que ainda pressupõe política e câmbio utilizáveis.

### 4.2 Item de despesa

O contrato tem oito campos. Sete são obrigatórios, mas **não são "os sete primeiros"**: o único campo opcional, `despesa.moeda`, ocupa a **sétima** posição — depois de `despesa.valor` (6º) e antes de `despesa.tem_nota_fiscal` (8º, também obrigatório). A sequência é a **ordem canônica de contrato**, usada para ordenar motivos estruturais na saída.

| # | Campo | Tipo | Significado | Restrição adicional |
|---|---|---|---|---|
| 1 | `despesa.id` | texto | Identificador do lançamento. **Único no arquivo** | Texto não vazio |
| 2 | `despesa.data` | texto | Data do fato gerador | Formato `AAAA-MM-DD` representando data real do calendário |
| 3 | `despesa.categoria` | texto | Categoria informada, antes da normalização | Texto não vazio |
| 4 | `despesa.descricao` | texto | Descrição livre. Informativa; integra a chave de duplicidade como recebida | Texto, podendo ser vazio |
| 5 | `despesa.fornecedor` | texto | Fornecedor. Integra a chave de duplicidade como recebido | Texto, podendo ser vazio |
| 6 | `despesa.valor` | número | Valor monetário na moeda da despesa. Inteiro é aceito (`72` → `72,00`) | Nenhuma quanto ao sinal: zero e negativos são estruturalmente válidos, avaliados depois por RN-006 |
| 7 | `despesa.moeda` | texto | Moeda ISO 4217 da despesa (política v4, Dia 2). Opcional: apenas a **ausência da chave** no objeto resolve para `BRL` sem produzir motivo algum | Quando a chave existe — inclusive com valor `null` —, segue o contrato normalmente: `null` é `CAMPO_AUSENTE`; tipo não textual é `CAMPO_TIPO_INVALIDO`; texto fora de `[A-Z]{3}` é `CAMPO_FORMATO_INVALIDO` (sem trim, sem conversão de caixa: `"usd"`, `" USD"`, `"US"` e `"USDX"` são formato inválido) |
| 8 | `despesa.tem_nota_fiscal` | booleano | Existência de nota fiscal | Nenhuma além do tipo booleano — um valor booleano recebido não é aceito como número, nem o inverso |

**Regra de opcionalidade de `despesa.moeda`.** É o único dos oito campos cuja ausência não produz motivo algum — mas a tolerância é estritamente sobre a **ausência da chave**, não sobre o seu conteúdo. Comportamento fechado:

| Situação | Resultado |
|---|---|
| Chave `moeda` **ausente** do objeto | Resolve silenciosamente para `BRL`, sem motivo algum |
| `"moeda": null` (chave presente, valor `null`) | Item recusado com `CAMPO_AUSENTE`, `campo` igual a `despesa.moeda` |
| Tipo não textual (número, booleano, lista, objeto) | `CAMPO_TIPO_INVALIDO` |
| Texto fora do formato `[A-Z]{3}` | `CAMPO_FORMATO_INVALIDO` |

Uma vez que a chave existe no objeto, `despesa.moeda` segue exatamente o mesmo contrato estrutural que os sete campos obrigatórios (RN-002) — inclusive a classificação `CAMPO_AUSENTE` para valor `null`, que normalmente soa contraditória com "campo opcional" mas não é: a opcionalidade é sobre a chave não precisar existir, não sobre um valor `null` explícito ser tolerado quando ela existe. Isso o distingue de `colaborador.centro_custo` (4.1), cuja tolerância cobre também valores malformados, não só a ausência da chave.

**Nomes qualificados.** A forma `despesa.<campo>` é o nome canônico do campo em toda esta spec, e é exatamente o conjunto de valores não nulos aceitos em `motivo.campo` (4.3). A chave correspondente dentro do objeto de despesa é o segmento à direita do ponto: o campo canônico `despesa.valor` corresponde à chave `valor`.

Campo **ausente**, **nulo**, de **tipo inválido** ou de **formato inválido** produz **erro estrutural no item**: o item é recusado e o restante do arquivo continua sendo processado. Essa regra vale integralmente para os sete campos obrigatórios; para `despesa.moeda`, vale sempre que a **chave existir no objeto** — inclusive com valor `null`, que é `CAMPO_AUSENTE` como em qualquer campo obrigatório. Só a ausência da própria chave no objeto foge da regra (parágrafo acima).

**Classificação do erro estrutural.** A escolha entre os três motivos abaixo é fechada e não admite outra leitura:

- **`CAMPO_AUSENTE`** — a chave obrigatória não existe no objeto, ou existe com valor `nulo`. Aplica-se normalmente a `despesa.moeda` quando a chave **existe com valor `null`** — só não se aplica quando a chave `moeda` está inteiramente ausente do objeto, caso em que não há motivo algum e o valor resolve para `BRL` (4.2).
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
| `despesa.moeda` — chave ausente do objeto | Estruturalmente válido — nenhum motivo. Resolve para `BRL` (política v4, Dia 2) |
| `despesa.moeda` igual a `null` | `CAMPO_AUSENTE` — chave presente, valor `null` |
| `despesa.moeda` igual a `"usd"` | `CAMPO_FORMATO_INVALIDO` — minúsculo |
| `despesa.moeda` igual a um número | `CAMPO_TIPO_INVALIDO` |

**Elemento que não é objeto.** Cada elemento de `despesas` deve ser um objeto. Quando não for — texto, número, lista, nulo —, aquela posição é recusada com o motivo único `ITEM_TIPO_INVALIDO`. Não se produzem os motivos de campo ausente dos oito campos de 4.2, e nenhuma regra de negócio é avaliada. O registro correspondente é exatamente:

| Campo da saída | Valor |
|---|---|
| `indice_entrada` | a posição original, preservada |
| `id` | nulo |
| `valor_informado` | nulo |
| `moeda` | nulo |
| `taxa_cambio_aplicada` | nulo |
| `data_cotacao_utilizada` | nulo |
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
| `moeda` | texto ou nulo | Moeda v4, Dia 2. Moeda efetivamente usada no item: `despesa.moeda` quando estruturalmente válida, ou `"BRL"` quando a **chave** `despesa.moeda` está ausente do objeto. Nula quando `despesa.moeda` é estruturalmente inválida — inclusive `null` explícito, que é `CAMPO_AUSENTE` (4.2) — ou o elemento de `despesas` não é objeto |
| `taxa_cambio_aplicada` | número ou nulo | v4, Dia 2. `1` para BRL; a taxa efetivamente usada na conversão, quando resolvida (RN-020); nulo quando não há conversão possível |
| `data_cotacao_utilizada` | texto `AAAA-MM-DD` ou nulo | v4, Dia 2. Data da cotação efetivamente usada — pode divergir de `despesa.data` (RN-020); nulo para BRL e sempre que `taxa_cambio_aplicada` for nulo |
| `valor_normalizado` | monetário ou nulo | Valor após conversão para BRL (RN-020, quando aplicável) e normalização monetária (RN-004). Nulo quando não calculável |
| `valor_reembolsavel` | monetário | Valor aprovado. Sempre `0,00` para item recusado |
| `decisao` | enumeração | Ver 4.4 |
| `motivos` | lista de objetos, possivelmente vazia | Objetos de motivo, na ordem definida em 8.3 |

**Comportamento de `moeda` / `taxa_cambio_aplicada` / `data_cotacao_utilizada` por cenário (RN-020):**

BRL (padrão ou informada):

```json
"moeda": "BRL",
"taxa_cambio_aplicada": 1,
"data_cotacao_utilizada": null
```

Moeda estrangeira convertida (exemplo: EUR datada `2026-07-18`, sábado sem cotação própria, usando a mais recente anterior — `2026-07-17`, cuja cotação de EUR é `5.96`):

```json
"moeda": "EUR",
"taxa_cambio_aplicada": 5.96,
"data_cotacao_utilizada": "2026-07-17"
```

Moeda estruturalmente inválida — `null` explícito (`CAMPO_AUSENTE`), tipo não textual (`CAMPO_TIPO_INVALIDO`) ou formato fora de `[A-Z]{3}` (`CAMPO_FORMATO_INVALIDO`); os três casos produzem a mesma saída:

```json
"moeda": null,
"taxa_cambio_aplicada": null,
"data_cotacao_utilizada": null
```

Moeda estruturalmente válida, mas sem cotação utilizável (`MOEDA_SEM_COTACAO`, RN-020):

```json
"moeda": "GBP",
"taxa_cambio_aplicada": null,
"data_cotacao_utilizada": null,
"valor_normalizado": null,
"valor_reembolsavel": 0.00
```

`taxa_cambio_aplicada` é sempre número JSON, nunca texto, e preserva a precisão do arquivo de câmbio. Não existe campo separado `valor_convertido`: esse papel é de `valor_normalizado`, que já é o valor em BRL após a conversão.

**Preservação de `valor_informado`.** Quando a chave `valor` existe no objeto de despesa, `valor_informado` é exatamente o valor JSON recebido — número, texto, booleano, lista ou objeto —, mesmo quando esse tipo é inválido para o contrato: `valor: "72,50"` produz `valor_informado` igual a `"72,50"`; `valor: true` produz `valor_informado` igual a `true`. `valor_informado` é nulo quando a chave `valor` está ausente ou é nula, e também quando o elemento de `despesas` não é um objeto (4.2). `valor_normalizado` permanece nulo sempre que `despesa.valor` não for um número válido, independentemente do que `valor_informado` contenha.

Cada objeto de `motivos` tem três campos:

| Campo | Tipo | Significado |
|---|---|---|
| `codigo` | enumeração | Código do motivo, conforme 4.5 |
| `regra` | texto | Identificador da regra de negócio que produziu o motivo, no formato `RN-NNN` |
| `campo` | texto ou nulo | Nome qualificado do campo. Restrito à lista fechada abaixo. Nulo quando o motivo não se refere a um campo específico |

**Valores aceitos em `campo`.** Apenas os oito nomes canônicos de 4.2, e nenhum outro:

`despesa.id` · `despesa.data` · `despesa.categoria` · `despesa.descricao` · `despesa.fornecedor` · `despesa.valor` · `despesa.moeda` · `despesa.tem_nota_fiscal`

Qualquer outro motivo traz `campo` nulo. Há uma única exceção a "estrutural implica campo preenchido": `ITEM_TIPO_INVALIDO` traz `campo` nulo, porque o defeito é do elemento inteiro e não de um campo dele. E há uma única exceção a "não estrutural implica campo nulo": `ID_DUPLICADO` traz `campo` igual a `despesa.id`, porque a violação é dele.

Exemplos:

```
{ "codigo": "CAMPO_TIPO_INVALIDO",  "regra": "RN-002", "campo": "despesa.valor" }
{ "codigo": "ITEM_TIPO_INVALIDO",   "regra": "RN-002", "campo": null }
{ "codigo": "ID_DUPLICADO",         "regra": "RN-003", "campo": "despesa.id" }
{ "codigo": "NOTA_FISCAL_AUSENTE",  "regra": "RN-009", "campo": null }
{ "codigo": "TETO_DIARIO_APLICADO", "regra": "RN-011", "campo": null }
{ "codigo": "MOEDA_SEM_COTACAO",    "regra": "RN-020", "campo": "despesa.moeda" }
{ "codigo": "CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO", "regra": "RN-019", "campo": null }
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
| `CAMPO_AUSENTE` | RN-002 | um dos oito nomes canônicos | A chave obrigatória não existe no objeto, ou existe com valor nulo. Para `despesa.moeda` (opcional), aplica-se quando a chave existe com valor `null`; não se aplica quando a chave está inteiramente ausente do objeto |
| `CAMPO_TIPO_INVALIDO` | RN-002 | um dos oito nomes canônicos | A chave existe, não é nula, mas seu tipo JSON diverge do exigido pelo contrato |
| `CAMPO_FORMATO_INVALIDO` | RN-002 | um dos oito nomes canônicos | O tipo JSON está correto, mas o conteúdo viola a restrição adicional do campo |
| `ID_DUPLICADO` | RN-003 | `despesa.id` | `despesa.id` válido repetido no arquivo |
| `MOEDA_SEM_COTACAO` | RN-020 | `despesa.moeda` | v4, Dia 2. Moeda estruturalmente válida, mas sem cotação exata ou anterior utilizável em `cambio.json` na data da despesa |
| `VALOR_NAO_POSITIVO` | RN-006 | nulo | Valor normalizado menor ou igual a zero |
| `CATEGORIA_FORA_POLITICA` | RN-007 | nulo | Categoria normalizada ausente da única tabela de política aplicável, quando essa tabela é `padrao` (centro de custo desconhecido, ausente, nulo ou de tipo inválido) |
| `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` | RN-019 | nulo | v4, Dia 2. Quando a tabela aplicável é a de um centro de custo cadastrado: categoria ausente dessa tabela, ou presente nela com `limite` igual a `0,00` |
| `FORA_COMPETENCIA` | RN-008 | nulo | `data` fora da janela |
| `NOTA_FISCAL_AUSENTE` | RN-009 | nulo | Nota obrigatória e ausente |
| `DUPLICIDADE` | RN-010 | nulo | Ocorrência posterior de despesa economicamente idêntica |

**Motivos de limitação** — presentes em itens não recusados:

| Código | Regra associada | `campo` | Aparece em |
|---|---|---|---|
| `TETO_DIARIO_APLICADO` | RN-011 quando `alimentacao` estiver configurada com `periodicidade: "dia"`; RN-012 quando `transporte_urbano` estiver configurado com `periodicidade: "dia"`; RN-019 para qualquer outro caso de `periodicidade: "dia"`, incluindo categorias externas (ex.: `representacao`) e categorias históricas configuradas com mecanismo diferente do histórico (ex.: `hospedagem` com `"dia"`) | nulo | `PARCIALMENTE_REEMBOLSADO` |
| `TETO_DIARIO_ESGOTADO` | RN-015 | nulo | `NAO_REEMBOLSADO_TETO_ESGOTADO` |
| `TETO_HOSPEDAGEM_APLICADO` | RN-013 | nulo | `PARCIALMENTE_REEMBOLSADO` de hospedagem |
| `TETO_INDIVIDUAL_APLICADO` | RN-019 (categoria de periodicidade `"diaria"` diferente de `hospedagem`, ex.: `estacionamento` — AMB-037) | nulo | `PARCIALMENTE_REEMBOLSADO` |

Item `INTEGRALMENTE_REEMBOLSADO` tem lista de motivos vazia.

### 4.6 Fragmento de saída

O bloco abaixo é um **fragmento não executável** da lista `resultados`: mostra três dos catorze registros, para ilustrar a forma. Por ser parcial, não traz `total_reembolsavel` — o total só faz sentido sobre a lista inteira. A apuração completa dos catorze itens e o total estão em 4.7.

```
"resultados": [
  { "indice_entrada": 1, "id": "d-001", "valor_informado": 72.50,
    "moeda": "BRL", "taxa_cambio_aplicada": 1, "data_cotacao_utilizada": null,
    "valor_normalizado": 72.50,
    "valor_reembolsavel": 60.00, "decisao": "PARCIALMENTE_REEMBOLSADO",
    "motivos": [ { "codigo": "TETO_DIARIO_APLICADO", "regra": "RN-011", "campo": null } ] },

  { "indice_entrada": 2, "id": "d-002", "valor_informado": 38.00,
    "moeda": "BRL", "taxa_cambio_aplicada": 1, "data_cotacao_utilizada": null,
    "valor_normalizado": 38.00,
    "valor_reembolsavel": 0.00, "decisao": "NAO_REEMBOLSADO_TETO_ESGOTADO",
    "motivos": [ { "codigo": "TETO_DIARIO_ESGOTADO", "regra": "RN-015", "campo": null } ] },

  { "indice_entrada": 11, "id": "d-011", "valor_informado": 33.333,
    "moeda": "BRL", "taxa_cambio_aplicada": 1, "data_cotacao_utilizada": null,
    "valor_normalizado": 33.33,
    "valor_reembolsavel": 33.33, "decisao": "INTEGRALMENTE_REEMBOLSADO",
    "motivos": [] }
]
```

Este fragmento antecede a política v4 e ilustra apenas a forma dos campos previamente existentes mais os três campos de auditoria de câmbio (4.3), sob valores `BRL` — não exercita conversão. Exemplos de conversão real estão em `§12`.

Os metadados do envelope acompanham a lista no documento completo:

```
"colaborador": { "id": "c-0417", "nome": "Marina Volpi", "centro_custo": "CC-ENG-PLATAFORMA" },
"periodo":     { "competencia": "2026-07", "inicio": "2026-07-01", "fim": "2026-07-31" }
```

### 4.7 Resultado esperado para `exemplos/despesas-exemplo.json`

Esta tabela documenta o comportamento sob a política **padrão** (alimentação R$60/dia, transporte R$80/dia, hospedagem R$250/diária, gatilho de nota fiscal R$100 — os mesmos valores hardcoded desta spec até a v1.1). É a baseline de regressão histórica, preservada como cenário próprio (CA-037). O mesmo arquivo, processado com a política **oficial v4** e o centro de custo real do colaborador (`CC-ENG-PLATAFORMA`, cadastrado na tabela), produz um resultado diferente — ver `§12`.

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

**Regra:** cada elemento de `despesas` deve ser um objeto. Sete dos seus oito campos de 4.2 são obrigatórios, com os tipos e restrições declarados; `despesa.moeda` (v4, Dia 2) é o único opcional — mas a opcionalidade é só sobre a **ausência da chave**: quando a chave `moeda` existe no objeto, inclusive com valor `null`, ela segue o contrato normalmente, e `null` é `CAMPO_AUSENTE` como qualquer campo obrigatório. Só a ausência total da chave resolve silenciosamente para `BRL` (RN-020), sem produzir motivo. Elemento que não é objeto é recusado com o motivo único `ITEM_TIPO_INVALIDO`, sem que nenhuma regra de negócio seja avaliada e sem gerar motivos de campo ausente; o registro resultante traz `id`, `valor_informado`, `moeda`, `taxa_cambio_aplicada`, `data_cotacao_utilizada` e `valor_normalizado` nulos, `valor_reembolsavel` `0,00` e `indice_entrada` preservado. Sendo objeto, qualquer campo obrigatório **ausente**, **nulo**, de **tipo inválido** ou de **formato inválido** recusa o item, com um motivo por campo defeituoso, classificado pela regra fechada da subseção "Classificação do erro estrutural" (4.2); `despesa.moeda`, quando a chave existe, segue a mesma classificação dos demais campos. Em qualquer caso o arquivo não é interrompido.
**Origem:** contrato de entrada.
**Aceite:** item com `data: "31/07/2026"` e `valor: "72,50"` é recusado com dois motivos, nesta ordem — `CAMPO_FORMATO_INVALIDO` com `campo` igual a `despesa.data`, depois `CAMPO_TIPO_INVALIDO` com `campo` igual a `despesa.valor` —, porque `despesa.data` precede `despesa.valor` na ordem canônica de contrato. `despesa.categoria` igual a um número é recusado com `CAMPO_TIPO_INVALIDO` e `campo` igual a `despesa.categoria`; `despesa.id` igual a texto vazio é recusado com `CAMPO_FORMATO_INVALIDO` e `campo` igual a `despesa.id`. Um elemento de `despesas` igual ao texto `"despesa"` produz um único registro recusado, com motivo único `ITEM_TIPO_INVALIDO`, `campo` nulo, `id` nulo, `valor_informado` nulo e `indice_entrada` preservado. Um item com `moeda: "usd"` é recusado com `CAMPO_FORMATO_INVALIDO` e `campo` igual a `despesa.moeda`; um item com `moeda: null` é recusado com `CAMPO_AUSENTE` e `campo` igual a `despesa.moeda`; um item sem a chave `moeda` no objeto não recebe motivo algum por causa disso.

### RN-003 — Unicidade de `despesa.id`

**Regra:** `despesa.id` é único no arquivo. Verificada apenas entre IDs estruturalmente válidos, **todas** as ocorrências que compartilham um mesmo valor recebem `ID_DUPLICADO`. Não se preserva "primeira ocorrência".
**Origem:** rastreabilidade (lacuna da política).
**Aceite:** três itens com `despesa.id` `"d-100"` produzem três registros recusados com `ID_DUPLICADO` e reembolsável `0,00`.

### RN-004 — Normalização monetária

**Regra:** todo valor válido é normalizado para duas casas decimais, arredondamento decimal **meio para cima**. Quando `despesa.moeda` for estrangeira e tiver cotação resolvida (RN-020), a normalização opera sobre o valor **já convertido** para BRL: a multiplicação pela taxa acontece antes do arredondamento, e há um único arredondamento — nunca um arredondamento do valor na moeda original seguido de outro após a conversão. Todas as regras posteriores usam o valor normalizado (e, quando aplicável, convertido).
**Origem:** política do RH, itens 1 a 5 (limites com duas casas); conversão cambial acrescentada pela política v4 (Dia 2, RN-020).
**Aceite:** `33.333` → `33,33`; `33.335` → `33,34`; `33.345` → `33,35`; `100.004` → `100,00`; `100.005` → `100,01`. USD `40,00` convertido pela taxa `5,50` normaliza para `220,00`, num único arredondamento.

### RN-005 — Normalização de categoria

**Regra:** `categoria` é normalizada por remoção de espaços das pontas, insensibilidade a caixa e insensibilidade a acentos. O resultado normalizado é comparado, por igualdade exata, contra as categorias presentes na única tabela de política efetivamente aplicável ao item — `padrao`, ou a tabela do centro de custo cadastrado, nunca as duas somadas (RN-007, RN-019, política v4, Dia 2). Nenhuma categoria é reembolsável "em qualquer centro de custo": mesmo `alimentacao`, `transporte_urbano` e `hospedagem` dependem de estarem presentes na tabela efetivamente aplicável ao item. Nenhuma outra transformação ocorre.
**Origem:** política do RH, item 9; vocabulário de categorias variável por centro de custo acrescentado pela política v4 (Dia 2).
**Aceite:** `ALIMENTACAO`, `Alimentação` e ` alimentacao ` reconhecem `alimentacao`. `transporte urbano` **não** reconhece `transporte_urbano`.

### RN-006 — Valor não positivo

**Regra:** item cujo valor normalizado seja menor ou igual a zero é recusado com `VALOR_NAO_POSITIVO`. Não abate nada, não agrega, não consome teto e não altera o total.
**Origem:** lacuna da política (nenhum item prevê valores negativos).
**Aceite:** `-45.00` é recusado com reembolsável `0,00` e o total do período não é reduzido em 45,00.

### RN-007 — Categorias reembolsáveis

**Regra:** é reembolsável a categoria normalizada presente na **única** tabela de política efetivamente aplicável ao item (RN-019). Essa tabela é `padrao` quando o centro de custo do colaborador é desconhecido, ausente, nulo ou de tipo inválido; ou é **exclusivamente** a tabela do centro de custo cadastrado, quando houver — nunca as duas tabelas somadas. Categoria normalizada ausente dessa única tabela aplicável é recusada com `CATEGORIA_FORA_POLITICA` quando a tabela aplicável é `padrao`; quando a tabela aplicável é a de um centro de custo cadastrado, a mesma ausência é `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (RN-019), não `CATEGORIA_FORA_POLITICA` — a distinção entre os dois motivos está em RN-019, não nesta regra.
**Origem:** política do RH, item 9; conjunto de categorias variável por centro de custo acrescentado pela política v4 (Dia 2, RN-019).
**Aceite:** `coworking` de R$ 89,00 com nota, para um colaborador de centro de custo desconhecido (tabela aplicável `padrao`), é recusado com `CATEGORIA_FORA_POLITICA`, reembolsável `0,00`, porque `padrao` não a declara. O mesmo `coworking`, para um colaborador de `CC-COMERCIAL` (cadastrado — tabela aplicável é exclusivamente a de `CC-COMERCIAL`, que também não declara `coworking`), é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITICA` — mesmo reembolsável `0,00`, motivo diferente porque a tabela aplicável é diferente. `representacao` de R$190,00 com nota, para um colaborador de `CC-SUPORTE-N2` (fora da tabela de centros de custo, logo sob `padrao`), é recusado com `CATEGORIA_FORA_POLITICA`, porque `padrao` não declara essa categoria.

### RN-008 — Elegibilidade temporal

**Regra:** item é temporalmente elegível quando `periodo.inicio ≤ data ≤ periodo.fim`, com ambas as bordas inclusivas. Fora disso, `FORA_COMPETENCIA`.
**Origem:** política do RH, item 7.
**Aceite:** `2026-04-15` na janela de julho é recusado. `2026-07-01` e `2026-07-31` são elegíveis.

### RN-009 — Nota fiscal obrigatória

**Regra:** quando o valor normalizado do item — já convertido para BRL quando `despesa.moeda` for estrangeira (RN-020) — for **estritamente maior** que o gatilho configurado na política (`nota_fiscal_obrigatoria_acima_de`, dado do arquivo de política, não uma constante interna do sistema — R$ 100,00 é o valor da política padrão/histórica usada nos exemplos desta spec) e `tem_nota_fiscal` for falso, o item é recusado com `NOTA_FISCAL_AUSENTE`. A comparação usa o valor individual normalizado e convertido, antes de qualquer corte por teto — nunca o valor original na moeda da despesa.
**Origem:** política do RH, item 5; comparação pelo valor convertido acrescentada pela política v4 (Dia 2, RN-020).
**Aceite:** `100,00` sem nota é elegível; `100,01` sem nota é recusado; `690,00` sem nota é recusado sem que qualquer teto seja calculado. USD `40,00` convertido pela taxa `5,50` resulta em `220,00`; sem nota fiscal, o item é recusado com `NOTA_FISCAL_AUSENTE`, mesmo que o valor original (`40,00`) esteja abaixo do gatilho.

### RN-010 — Duplicidade econômica

**Regra:** dois itens são duplicados quando coincidem exatamente em `data`, `categoria` normalizada, `moeda` (a informada, ou `BRL` quando ausente), `valor` normalizado em BRL, `fornecedor` como recebido e `descricao` como recebida. `despesa.id` e `tem_nota_fiscal` não integram a chave. Avaliada apenas entre itens sem nenhum motivo anterior de recusa. A primeira ocorrência em ordem de `indice_entrada` é mantida; as posteriores recebem `DUPLICIDADE`. Itens em moedas diferentes nunca são duplicados entre si, mesmo quando `data`, categoria, fornecedor, descrição e o valor **já convertido** coincidem exatamente — a moeda diferente já basta para distingui-los.
**Origem:** política do RH, item 8; `moeda` acrescentada à chave pela política v4 (Dia 2, RN-020).
**Aceite:** dois itens iguais de R$ 54,90 em `2026-07-09` produzem 54,90 no primeiro e 0,00 no segundo. Itens de R$ 100,00 e R$ 100,01 do mesmo dia e fornecedor **não** são duplicados. Uma despesa de EUR 100,00 e uma de BRL 100,00, no mesmo dia, categoria, fornecedor e descrição, **não** são duplicatas — ainda que a conversão faça os valores em BRL coincidirem por acaso.

### RN-011 — Limite diário de alimentação

**Regra:** o comportamento histórico "por dia" aplica-se quando a tabela de política efetivamente aplicável declara `alimentacao.periodicidade = "dia"` — R$ 60,00 por data na política padrão. O limite em si é definido pela política aplicável ao centro de custo do colaborador (RN-019), aplicado ao conjunto dos itens elegíveis de `alimentacao` daquela data. Se uma política válida declarar outra `periodicidade` para `alimentacao`, é RN-019 — não este número fixo — que determina o mecanismo configurado.
**Origem:** política do RH, item 1; valor e mecanismo parametrizados por centro de custo pela política v4 (Dia 2, RN-019, AMB-036).
**Aceite:** sob a política padrão, R$ 72,50 e R$ 38,00 na mesma data rendem R$ 60,00 no total daquela data.

### RN-012 — Limite diário de transporte urbano

**Regra:** o comportamento histórico "por dia" aplica-se quando a tabela de política efetivamente aplicável declara `transporte_urbano.periodicidade = "dia"` — R$ 80,00 por data na política padrão. O limite em si é definido pela política aplicável ao centro de custo do colaborador (RN-019), aplicado ao conjunto dos itens elegíveis de `transporte_urbano` daquela data. Se uma política válida declarar outra `periodicidade` para `transporte_urbano`, é RN-019 — não este número fixo — que determina o mecanismo configurado.
**Origem:** política do RH, item 2; valor e mecanismo parametrizados por centro de custo pela política v4 (Dia 2, RN-019, AMB-036).
**Aceite:** sob a política padrão, item elegível de R$ 100,00, sozinho na data, rende R$ 80,00.

### RN-013 — Limite individual de hospedagem

**Regra:** `TETO_HOSPEDAGEM_APLICADO` aplica-se a `hospedagem` quando a tabela de política efetivamente aplicável declara `hospedagem.periodicidade = "diaria"` — o caso normal, inclusive na política padrão. O limite em si é definido pela política aplicável ao centro de custo do colaborador (RN-019) — R$ 250,00 por lançamento na política padrão —, aplicável somente quando esse limite for maior que zero (limite igual a `0,00` é RN-019, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não este teto). Quando `hospedagem.periodicidade` for `"diaria"`, cada lançamento é avaliado isoladamente e não participa da agregação por data. Se uma política válida declarar `hospedagem.periodicidade = "dia"`, aplica-se o mecanismo compartilhado de RN-019/RN-015, com `TETO_DIARIO_APLICADO` ou `TETO_DIARIO_ESGOTADO` conforme o saldo — `TETO_HOSPEDAGEM_APLICADO` permanece exclusivo de hospedagem configurada como `"diaria"`. O conteúdo da descrição não altera o teto.
**Origem:** política do RH, item 3; valor e mecanismo parametrizados por centro de custo pela política v4 (Dia 2, RN-019, AMB-036).
**Aceite:** sob a política padrão, lançamento de R$ 480,00 descrito como "2 diarias" rende R$ 250,00. Alterar o texto da `descricao` desse lançamento não altera o teto individual aplicado, que permanece R$ 250,00. Dois lançamentos de hospedagem na mesma data podem render até R$ 500,00 sob a política padrão.

### RN-014 — Reembolso parcial

**Regra:** ultrapassado o teto, reembolsa-se até o teto e o excedente não é reembolsado. Vale tanto para o teto compartilhado de categorias com `periodicidade: "dia"` quanto para o teto individual de categorias com `periodicidade: "diaria"` (RN-019) — em ambos os casos, paga-se até o saldo ou limite disponível, nunca zero por ultrapassagem. O agregado diário nunca é recusado integralmente por ultrapassagem, nem o lançamento de teto individual.
**Origem:** política do RH, item 4.
**Aceite:** sob a política padrão, R$ 61,00 de alimentação, sozinho na data, rende R$ 60,00 — não R$ 0,00.

### RN-015 — Distribuição do teto diário

**Regra:** aplica-se a qualquer categoria cuja `periodicidade`, na tabela de política efetivamente aplicável, seja `"dia"` (RN-019) — não a um conjunto fixo de nomes de categoria. Dentro de uma data e categoria com esse teto compartilhado, os itens elegíveis consomem o saldo em ordem crescente de `indice_entrada`. Cada item é pago integralmente enquanto houver saldo; o item que ultrapassa o saldo é pago parcialmente; os posteriores recebem `NAO_REEMBOLSADO_TETO_ESGOTADO`. Não se aplica a categorias com `periodicidade: "diaria"`, que não possuem saldo compartilhado — `hospedagem` é um exemplo desse caso, não a regra em si.
**Origem:** política do RH, itens 1 e 2 (unidade "por dia").
**Aceite:** sob a política padrão, com itens de R$ 72,50 e R$ 38,00 nessa ordem, o primeiro rende R$ 60,00 e o segundo R$ 0,00 com decisão `NAO_REEMBOLSADO_TETO_ESGOTADO`.

### RN-016 — Ampliação por viagem (efeito nulo nesta versão)

**Regra:** o item 6 da política não produz efeito. Nenhuma despesa é classificada como de colaborador em viagem, e nenhum limite é ampliado. A condição não é inferida a partir do conteúdo semântico de `descricao` ou `fornecedor`, nem da categoria, da existência de hospedagem ou de qualquer campo desconhecido.
**Origem:** política do RH, item 6.
**Aceite:** numa entrada com **um único item elegível**, substituir na `descricao` um texto neutro por termos como "aeroporto" ou "hotel" não amplia teto algum e não altera o `valor_reembolsavel`. Um campo `em_viagem: true` na entrada não altera resultado algum. O cenário usa um item único justamente para isolar a ausência de inferência de viagem: `descricao` integra a chave de duplicidade de RN-010 e, com dois ou mais itens, alterá-la pode legitimamente mudar o resultado por outro caminho.

### RN-017 — Composição da saída

**Regra:** toda posição da lista `despesas` produz exatamente um registro de saída, na ordem da entrada, com os campos de 4.3 — incluindo, a partir da política v4 (Dia 2), `moeda`, `taxa_cambio_aplicada` e `data_cotacao_utilizada` —, uma única decisão final e a lista de objetos de motivo ordenada conforme 8.3. Cada objeto de motivo declara `codigo`, a `regra` que o produziu e o `campo` quando o motivo for estrutural.
**Origem:** auditabilidade (lacuna da política); campos de auditoria de câmbio acrescentados pela política v4 (Dia 2).
**Aceite:** entrada com 14 itens produz 14 registros; nenhum item desaparece, inclusive os recusados. Todo objeto de motivo apresentado traz um `codigo` do vocabulário de 4.5 e a `regra` correspondente indicada naquela tabela. Um item em EUR traz `moeda: "EUR"`, `taxa_cambio_aplicada` igual à taxa efetivamente usada e `data_cotacao_utilizada` igual à data da cotação (que pode divergir de `despesa.data` — RN-020); um item em BRL traz `moeda: "BRL"`, `taxa_cambio_aplicada: 1` e `data_cotacao_utilizada: null`.

### RN-018 — Total do período

**Regra:** `total_reembolsavel` é a soma dos `valor_reembolsavel` apresentados nos registros de saída.
**Origem:** auditabilidade.
**Aceite:** para `exemplos/despesas-exemplo.json`, sob a política padrão, o total é R$ 585,43 e coincide com a soma da coluna de 4.7. O mesmo arquivo, sob a política v4 e `CC-ENG-PLATAFORMA`, totaliza R$ 351,43 — ver `§12`.

### RN-019 — Política de reembolso por centro de custo

**Regra:** a política de limites deixa de ser um valor único. A cada item se aplica **uma única** tabela: a do centro de custo do colaborador (`colaborador.centro_custo`), quando cadastrada em `centros_custo`, **ou** a tabela `padrao`, quando o centro de custo é desconhecido, ausente, nulo ou de tipo inválido — nunca as duas tabelas somadas ou misturadas. A comparação de `centro_custo` contra as chaves de `centros_custo` é textual e exata — sem trim, sem normalização de caixa ou acento, sem correspondência aproximada.

Dentro da tabela efetivamente aplicável, cada categoria declarada tem um `limite` e uma `periodicidade` (contrato estrutural em 4.1.1, AMB-035):

- `periodicidade: "dia"` — teto **compartilhado** entre os itens elegíveis da mesma categoria e data, consumido em ordem crescente de `indice_entrada` (mesmo mecanismo de RN-011/RN-012/RN-015). É o caso de `alimentacao`, `transporte_urbano` e, quando declarada, `representacao`.
- `periodicidade: "diaria"` — teto **individual** por lançamento, sem saldo compartilhado (mesmo mecanismo de RN-013). `hospedagem` sob esse mecanismo produz motivo `TETO_HOSPEDAGEM_APLICADO`; qualquer outra categoria sob `periodicidade: "diaria"` (ex.: `estacionamento`) usa o mesmo mecanismo individual, mas produz motivo `TETO_INDIVIDUAL_APLICADO` (AMB-037) — `TETO_HOSPEDAGEM_APLICADO` é exclusivo de `hospedagem`.
- Categoria com `periodicidade` diferente de `"dia"` e `"diaria"` torna o **arquivo de política** inválido (AMB-035, RN-022) — não é um defeito do item, é um defeito do arquivo externo.
- Dentro de `padrao`, todo `limite` declarado é estritamente maior que zero (AMB-035, correção da segunda revisão): `limite: 0` em `padrao` não é uma decisão de política sobre uma categoria — é defeito estrutural do **arquivo** de política (RN-022), porque `padrao` é o fallback universal, e um teto zero ali retiraria a categoria de todo centro de custo desconhecido sem que o comunicado expresse essa decisão dessa forma. Dentro de uma tabela de `centros_custo`, `limite: 0` continua sendo decisão válida do próprio centro (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, abaixo) — não um defeito. Nenhum motivo de item novo é criado para o caso de `padrao`: a falha é do arquivo inteiro, código de saída `2` (RN-022, AMB-034), antes de qualquer item ser avaliado.

Duas classificações de recusa, sempre com `valor_reembolsavel` `0,00` e nunca como reembolso parcial:

- **`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`** — quando a tabela aplicável é a de um **centro de custo cadastrado**: (a) a categoria está ausente dessa tabela (o centro de custo existe, mas não a declara), ou (b) a categoria está presente na tabela com `limite` igual a `0,00`. As duas situações são, na prática, a mesma decisão de política — o centro de custo não reembolsa aquela categoria — e nunca produzem teto parcial de valor zero.
- **`CATEGORIA_FORA_POLITICA`** (RN-007) — quando a tabela aplicável é `padrao` (centro de custo desconhecido, ausente, nulo ou de tipo inválido) e a categoria está ausente dela.

Categoria adicional que só existe em algum centro de custo específico (ex.: `representacao`) segue a mesma régua acima, sem tratamento especial: reembolsável quando a tabela aplicável a declara; `CATEGORIA_FORA_POLITICA` quando a tabela aplicável é `padrao` e não a declara; `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` quando a tabela aplicável é de um centro cadastrado que não a declara.
**Origem:** comunicado do RH, política v4, item A (Dia 2).
**Aceite:** colaborador de `CC-ENG-PLATAFORMA` com hospedagem de R$480,00 é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, reembolsável `0,00` — não `PARCIALMENTE_REEMBOLSADO` (categoria presente, limite `0,00`). Colaborador de `CC-ADM` com hospedagem — categoria ausente da tabela de `CC-ADM`, presente em `padrao` — também é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e não recebe o limite de `padrao` (categoria ausente da tabela do centro cadastrado). Colaborador de `CC-COMERCIAL` com `coworking` — categoria ausente da tabela de `CC-COMERCIAL`, que também não existe em `padrao` — é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITICA`, porque a tabela aplicável a esse colaborador é a de `CC-COMERCIAL`, não `padrao`. Colaborador de `CC-SUPORTE-N2` (fora da tabela) com alimentação de R$58,00 usa o limite de `padrao` (R$60,00) e é `INTEGRALMENTE_REEMBOLSADO`. Dois itens de `representacao` de `CC-COMERCIAL` (`periodicidade: "dia"`, limite R$300,00) na mesma data compartilham o mesmo saldo de R$300,00, consumido em ordem de `indice_entrada` — exatamente como `alimentacao` e `transporte_urbano`.

### RN-020 — Resolução de câmbio e conversão monetária

**Regra:** `despesa.moeda` igual a `"BRL"`, ou com a chave inteiramente ausente do objeto (RN-002), tem taxa implícita `1` e não consulta a tabela de câmbio. `despesa.moeda: null` nunca chega a esta regra — é `CAMPO_AUSENTE` (RN-002), recusado antes da resolução de câmbio. Para moeda estrangeira estruturalmente válida, procura-se primeiro a cotação exata na data da despesa; sem cotação exata, usa-se a cotação mais recente cuja data seja **anterior** à data da despesa — nunca uma cotação futura, e sem interpolação entre datas. Se não existir cotação exata nem anterior utilizável para aquela moeda — porque ela não aparece em `cambio.json` em nenhuma data, ou porque sua primeira cotação disponível é posterior à data da despesa —, o item é recusado com `MOEDA_SEM_COTACAO` e `valor_normalizado` permanece nulo. Isso impede especificamente as regras que dependem de `valor_normalizado` calculável — RN-006 (valor não positivo), RN-009 (nota fiscal), RN-010 (duplicidade) e qualquer teto (RN-011 a RN-015, RN-019) —, mas **não** impede as regras cujos campos de dependência permanecem utilizáveis: a elegibilidade de categoria pela tabela aplicável (RN-007 e a parte correspondente de RN-019, quando `despesa.categoria` estiver estruturalmente válida; `colaborador.centro_custo` é sempre resolvido conforme a tolerância de RN-019 — valor reconhecido seleciona a tabela específica, enquanto ausência, nulo, tipo inválido ou valor desconhecido seleciona `padrao`) e a elegibilidade temporal (RN-008, quando `despesa.data` e o período estiverem válidos) continuam sendo avaliadas normalmente. Um mesmo item pode, portanto, apresentar `MOEDA_SEM_COTACAO` junto de `CATEGORIA_FORA_POLITICA`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMPETENCIA` — mas nunca junto de `VALOR_NAO_POSITIVO`, `NOTA_FISCAL_AUSENTE`, `DUPLICIDADE` ou de um motivo de teto, porque essas dependem do componente financeiro incalculável. A conversão multiplica o valor bruto pela taxa resolvida; o arredondamento para duas casas (RN-004) acontece uma única vez, sobre o resultado dessa multiplicação.
**Origem:** comunicado do RH, política v4, item B (Dia 2).
**Aceite:** EUR `30,00` datado num sábado sem cotação própria usa a cotação do último dia útil anterior disponível — ex.: `2026-07-17` para uma despesa de `2026-07-18`. GBP sem nenhuma entrada em `cambio.json`, em qualquer data, é recusado com `MOEDA_SEM_COTACAO`. USD `40,00` × `5,50` normaliza para `220,00`, com um único arredondamento.

### RN-021 — Vigência do arquivo de política

**Regra:** `politica.vigencia` é obrigatória, deve ser texto no formato `AAAA-MM-DD` e representar uma data real do calendário; sua ausência, nulo, tipo inválido, formato inválido ou data inexistente invalidam o **arquivo de política** por inteiro — nenhuma apuração é produzida, o mesmo tratamento que RN-001 dá ao envelope de despesas. Quando válida, `vigencia` é metadado informativo: a política carregada vale integralmente para toda a execução, e nenhum item é recusado ou filtrado por sua `data` ser anterior a `vigencia`. Esta versão não seleciona automaticamente entre políticas históricas por data, porque nenhuma política externa anterior à v4 foi fornecida ao motor.
**Origem:** comunicado do RH, política v4 — "vigência imediata, retroativa à competência atual" (Dia 2).
**Aceite:** arquivo de política sem `vigencia` não produz apuração alguma. Arquivo de política com `vigencia: "2026-07-01"` processa normalmente despesas de `2026-04-15` sem recusá-las por causa da vigência — a competência (RN-008) continua sendo a única regra temporal que afeta itens individuais.

### RN-022 — Processabilidade dos arquivos externos de política e câmbio

**Regra:** a apuração só ocorre quando os arquivos de política e de câmbio são, cada um, legíveis e sintaticamente válidos como JSON, e estruturalmente válidos conforme o contrato fechado de 4.1.1 (AMB-035) — que define exaustivamente o que "estruturalmente inválido" significa para cada arquivo: tipo de cada campo, obrigatoriedade, e as restrições de `limite`/`periodicidade` das tabelas de categoria (política) e de `taxas` (câmbio). Em particular, `limite` igual a `0,00` dentro de `padrao` é violação estrutural do arquivo de política (correção da segunda revisão) — não uma recusa de item; o mesmo valor dentro de uma tabela de `centros_custo` é estruturalmente válido e produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` por item (RN-019). Falha em qualquer um dos dois arquivos — ausência, ilegibilidade, JSON sintaticamente inválido, ou qualquer violação desse contrato — impede toda a apuração do arquivo de despesas, mesmo que este esteja perfeitamente válido; nenhum resultado parcial é produzido. É erro global (código de saída `2`, AMB-034), na mesma classe de gravidade de um arquivo de entrada ilegível — mais grave que um envelope de despesas inválido (RN-001, código `3`), que ainda pressupõe política e câmbio utilizáveis. `taxas` vazio em `cambio.json` **não** é violação estrutural — é um arquivo de câmbio válido que simplesmente não resolve nenhuma moeda estrangeira, recusando essas despesas individualmente com `MOEDA_SEM_COTACAO` (RN-020).
**Origem:** contrato de entrada (lacuna do comunicado, decorrente da introdução dos arquivos externos — política v4, Dia 2).
**Aceite:** arquivo de câmbio com JSON sintaticamente inválido não produz apuração alguma, mesmo que o arquivo de despesas e o arquivo de política sejam válidos. Arquivo de política sem o bloco `padrao`, com `moeda_base` diferente de `"BRL"`, com uma tabela de categoria cuja `periodicidade` não seja `"dia"` nem `"diaria"`, ou com qualquer categoria de `padrao` tendo `limite` igual a `0,00`, não produz apuração alguma. Arquivo de câmbio com `taxas: {}` produz apuração normal do arquivo de despesas, com toda despesa em moeda estrangeira recusada individualmente.

---

## 6. Ambiguidades identificadas e decisões

> Esta seção é o coração da spec. Uma ambiguidade resolvida no código sem registro
> aqui conta como não resolvida.

Dezoito ambiguidades da linha de base (`AMB-001` a `AMB-018`) e as subdecisões que cada uma gerou, mais dezenove ambiguidades novas do envelope do Dia 2, política v4 (`AMB-019` a `AMB-037`) — trinta e sete no total. `AMB-034` a `AMB-036` foram acrescentadas numa correção após revisão independente da primeira leitura do envelope, para fechar o contrato de execução e os contratos estruturais dos arquivos externos; `AMB-037` foi acrescentada numa segunda revisão independente, para fechar o vocabulário de motivos de teto individual. Classificação: **U** unidade de aplicação · **F** fronteira · **D** dado ausente · **O** outra.

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
**Subdecisão AMB-018/ESCOPO:** `colaborador.id`, `colaborador.nome`, `colaborador.centro_custo` e `periodo.competencia` são metadados opcionais, preservados na saída para rastreabilidade e nulos quando ausentes, malformados ou de tipo inesperado; nunca alteram resultado financeiro nem invalidam arquivo ou item. BRL é a moeda presumida quando a **chave** `despesa.moeda` está inteiramente ausente do objeto (RN-020; política v4, Dia 2, alterou o que acontece quando a chave existe — inclusive com valor `null` — ver AMB-023 a AMB-029).

### AMB-019 — Centro de custo desconhecido, ausente, nulo ou de tipo inválido · D

**Texto original do RH:** "Alguns centros de custo não têm entrada na tabela. Nesse caso, aplica-se a política padrão."
**O que não está claro:** o texto cobre explicitamente "desconhecido" (fora da tabela), mas `colaborador.centro_custo` é campo opcional e tolerante (RN-001) — o comunicado não fala de ausência, nulo ou tipo inválido do próprio campo.
**Decisão:** ausente, nulo, de tipo inválido, ou presente mas sem entrada na tabela, recebem o mesmo tratamento — política `padrao` integral, sem invalidar arquivo ou item.
**Justificativa:** estende o princípio de tolerância já estabelecido para o bloco `colaborador` (RN-001) e é a leitura mais direta de "aplica-se a política padrão" quando não há centro de custo identificável.
**Regra afetada:** RN-019

### AMB-020 — Categoria ausente em centro de custo cadastrado · F

**Texto original do RH:** "Alguns centros de custo não têm entrada na tabela. Nesse caso, aplica-se a política padrão." `CC-ADM`, em `politica-v4.json`, não declara `hospedagem`.
**O que não está claro:** o texto resolve centro de custo ausente **da tabela inteira**; não resolve categoria ausente **dentro** de um centro de custo que está cadastrado.
**Decisão:** categoria ausente da tabela de um centro de custo cadastrado não recebe o valor de `padrao` por fallback — é tratada como não reembolsável para aquele centro de custo (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`).
**Justificativa:** um centro de custo que aparece na tabela é, pela própria existência da entrada, uma declaração explícita das categorias que cobre; presumir fallback por categoria tornaria a ausência de uma entrada indistinguível de um esquecimento do financeiro.
**Regra afetada:** RN-019

### AMB-021 — Categoria `representacao` exclusiva de quem a declara · F

**Texto original do RH:** "`CC-COMERCIAL` tem uma categoria nova, `representacao`, que não existia na v3."
**O que não está claro:** "categoria nova" poderia significar reembolsável globalmente (com algum limite implícito nos centros que não a declaram) ou reembolsável apenas onde declarada.
**Decisão:** `representacao` é reembolsável somente onde a tabela efetivamente aplicável a declara — e não é um caso especial: segue exatamente a régua de RN-019 que vale para qualquer categoria. Quando o centro de custo é desconhecido, ausente, nulo ou de tipo inválido, a tabela aplicável é `padrao`, que não declara `representacao` — `CATEGORIA_FORA_POLITICA`. Quando o centro de custo é cadastrado mas não declara `representacao` (ex.: `CC-ADM`), a categoria está ausente **da tabela daquele centro** — `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITICA`, pelo mesmo motivo que qualquer outra categoria ausente de um centro cadastrado (AMB-020).
**Justificativa:** o texto atribui a categoria especificamente a `CC-COMERCIAL`; inventar um limite implícito para os demais centros de custo criaria uma regra financeira sem base no comunicado. Tratar `representacao` como caso especial de classificação de motivo — em vez de aplicar a mesma régua de RN-019 — criaria uma exceção sem justificativa própria.
**Regra afetada:** RN-007, RN-019

### AMB-022 — Significado de limite igual a zero · O

**Texto original do RH:** `CC-ENG-PLATAFORMA.hospedagem.limite = 0.00`, com `"observacao": "nao reembolsavel"`.
**O que não está claro:** poderia ser um teto normal de valor zero (item elegível, cortado a zero, `PARCIALMENTE_REEMBOLSADO`) ou uma recusa de política (item nunca elegível, `RECUSADO`).
**Decisão:** limite explicitamente igual a `0,00` recusa o item com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`. Nunca é classificado como reembolso parcial de valor zero.
**Justificativa:** a observação do próprio financeiro ("não reembolsável") descreve uma condição de elegibilidade, não um teto — e um teto de valor zero é uma contradição em termos: teto pressupõe algo a cortar.
**Regra afetada:** RN-019

### AMB-023 — Moeda estrangeira como indício de viagem · D

**Texto original do RH:** comunicado B trata apenas de conversão; RN-016 (linha de base) declara efeito nulo porque "a entrada não possui campo estruturado que informe viagem".
**O que não está claro:** `despesa.moeda` é, agora, um campo estruturado — poderia ser lido como o campo que faltava para ativar o item 6 da política (ampliação de 50%).
**Decisão:** `despesa.moeda` diferente de `BRL` não ativa a ampliação de 50%, não afeta outros itens do mesmo dia, não afeta o período, e RN-016 continua sem efeito algum.
**Justificativa:** `moeda` é a unidade monetária do lançamento, não uma condição de deslocamento do colaborador — pode haver despesa em moeda estrangeira sem viagem (assinatura cobrada em USD) e viagem sem moeda estrangeira; o comunicado nunca conecta os dois conceitos explicitamente. Conectar seria decisão de produto, não leitura técnica.
**Regra afetada:** RN-016

### AMB-024 — Data da despesa sem cotação exata · F

**Texto original do RH:** "A conversão usa a taxa da data da despesa." `cambio.json` só publica dias úteis bancários.
**O que não está claro:** o texto pressupõe cotação exata sempre disponível — o próprio arquivo de câmbio mostra que isso é falso por construção (fins de semana, feriados).
**Decisão:** sem cotação exata, usa-se a cotação mais recente cuja data seja anterior à data da despesa. Nunca uma cotação futura. Sem interpolação entre datas.
**Justificativa:** é a leitura financeira convencional de "cotação de fechamento" ("PTAX de fechamento", nota do próprio `cambio.json`) — a cotação de sexta-feira permanece vigente no fim de semana.
**Regra afetada:** RN-020

### AMB-025 — Moeda sem qualquer cotação utilizável · D

**Texto original do RH:** nenhum — decorre da ausência de dados em `cambio.json` para determinadas moedas em qualquer data.
**O que não está claro:** o comunicado não prevê moeda sem nenhuma cotação, exata ou anterior, disponível.
**Decisão:** o item é recusado com `MOEDA_SEM_COTACAO`. Moeda ausente de toda a tabela e moeda cuja primeira cotação disponível é posterior à despesa recebem o mesmo tratamento — em ambos os casos não existe cotação anterior utilizável.
**Justificativa:** inventar uma taxa (ex.: presumir paridade 1:1) corromperia o valor reembolsável silenciosamente; recusar o item preserva o restante do arquivo, no mesmo espírito de todo erro estrutural por item.
**Regra afetada:** RN-020

### AMB-026 — Ordem de conversão e arredondamento · F

**Texto original do RH:** nenhum — decorre da combinação entre RN-004 (arredondamento de duas casas) e a conversão cambial introduzida pelo item B.
**O que não está claro:** se a conversão acontece antes ou depois do arredondamento de RN-004, e quantas vezes o arredondamento ocorre.
**Decisão:** multiplica-se o valor bruto pela taxa resolvida; o arredondamento para duas casas acontece uma única vez, sobre o resultado dessa multiplicação.
**Justificativa:** um único arredondamento, no fim da cadeia, evita erro de arredondamento composto e mantém RN-004 como o último passo antes de qualquer regra financeira usar o número — não um passo intermediário repetido.
**Regra afetada:** RN-004, RN-020

### AMB-027 — Base de comparação do gatilho de nota fiscal com moeda estrangeira · U

**Texto original do RH:** "Os limites da política são sempre em BRL. Uma despesa em EUR é convertida antes de ser comparada ao limite."
**O que não está claro:** o texto fala de "limites da política" (categorias); o gatilho de nota fiscal é um campo separado (`nota_fiscal_obrigatoria_acima_de`) — cabe dúvida se ele está incluído.
**Decisão:** o gatilho de R$100 compara o valor normalizado, já convertido para BRL — nunca o valor original na moeda da despesa.
**Justificativa:** mesmo raciocínio já registrado em AMB-005 para a linha de base — a obrigatoriedade documental não pode depender da moeda em que a despesa foi lançada; USD 40,00 (abaixo do gatilho) e R$220,00 convertidos são economicamente a mesma despesa.
**Regra afetada:** RN-009, RN-020

### AMB-028 — Chave de duplicidade entre moedas diferentes · U

**Texto original do RH:** nenhum — decorre da combinação entre RN-010 (linha de base) e a introdução de `moeda`.
**O que não está claro:** se dois itens em moedas diferentes, com valor convertido coincidente, deveriam ser tratados como duplicata.
**Decisão:** a chave passa a ser `data`, `categoria` normalizada, `moeda`, `valor` normalizado **em BRL** (já convertido, RN-020), `fornecedor` original e `descricao` original. `moeda` entra como componente adicional da chave, ao lado do valor já convertido — não como substituto dele. Itens em moedas diferentes nunca são duplicados entre si, mesmo que o valor convertido coincida exatamente.
**Justificativa:** `moeda` impede colisões entre unidades monetárias diferentes — sem ela, um item de EUR 100,00 e um de BRL 100,00 no mesmo dia poderiam colidir por coincidência aritmética, mesmo sendo transações distintas. Usar o valor **em BRL** (não o valor bruto na moeda original) mantém a chave alinhada ao mesmo valor financeiro que todas as regras posteriores usam (RN-006, RN-009, os tetos) — não haveria razão para duplicidade ser a única regra a operar sobre um número diferente. Dentro da mesma moeda e mesma data, a taxa aplicada é sempre a mesma (RN-020), então o valor convertido não introduz variação espúria para duas ocorrências do mesmo lançamento. `moeda` na chave já garante, por si só, que moedas diferentes nunca colidem — o valor convertido coincidir por acaso não basta para gerar falso positivo.
**Regra afetada:** RN-010

### AMB-029 — Campos de auditoria da conversão na saída · O

**Texto original do RH:** nenhum — a spec 1.1 não previa esquema de saída para conversão cambial.
**O que não está claro:** quais campos tornam a conversão auditável sem exigir reabrir `cambio.json` manualmente.
**Decisão:** três campos novos por registro — `moeda`, `taxa_cambio_aplicada` e `data_cotacao_utilizada` — entre `valor_informado` e `valor_normalizado`. Não existe campo separado `valor_convertido`: esse papel é de `valor_normalizado`.
**Justificativa:** sem esses campos, um item convertido é opaco para quem audita — contraria o objetivo de rastreabilidade da spec (`§2`).
**Regra afetada:** RN-017, RN-020

### AMB-030 — Validação de `politica.vigencia` · F

**Texto original do RH:** "Vigência imediata, retroativa à competência atual."
**O que não está claro:** se `vigencia` é puramente informativa ou se deve ser validada estruturalmente, e se afeta a elegibilidade de itens por data.
**Decisão:** `vigencia` é obrigatória e validada estruturalmente no arquivo de política (formato `AAAA-MM-DD`, data real) — sua ausência ou malformação invalida o arquivo de política inteiro. Uma vez válida, é metadado informativo: não recusa itens anteriores à vigência, e esta versão não seleciona automaticamente entre políticas históricas.
**Justificativa:** não existe política externa anterior à v4 fornecida ao motor — validar `vigencia` contra os itens exigiria uma política alternativa que não existe; mas o campo em si é obrigatório porque compõe o contrato mínimo do arquivo de política.
**Regra afetada:** RN-021

### AMB-031 — Erros estruturais nos arquivos externos: erro global versus recusa individual · O

**Texto original do RH:** nenhum — decorre da introdução dos arquivos externos de política e câmbio.
**O que não está claro:** se um arquivo externo malformado invalida toda a execução, ou se cada situação decorrente dele (ex.: moeda sem cotação) é tratada item a item.
**Decisão:** arquivo de política ou de câmbio ausente, ilegível, sintaticamente inválido ou estruturalmente inválido é erro global — nenhuma apuração é produzida. Ausência de cotação utilizável para uma despesa específica, e `despesa.moeda` estruturalmente inválida, recusam somente o item.
**Justificativa:** espelha a distinção já estabelecida entre RN-001 (envelope, erro global) e RN-002 (item, recusa individual) — um arquivo externo quebrado compromete toda a execução; um dado de negócio ausente para um item específico compromete só aquele item.
**Regra afetada:** RN-020, RN-022

### AMB-032 — Regressão da baseline histórica sob a política v4 · O

**Texto original do RH:** nenhum — decorre de `exemplos/despesas-exemplo.json` usar `colaborador.centro_custo = "CC-ENG-PLATAFORMA"`, que tem entrada própria em `politica-v4.json`.
**O que não está claro:** se o fixture histórico (R$585,43) deveria ser atualizado para refletir a política v4 aplicada ao centro de custo real do colaborador, ou preservado como está.
**Decisão:** as duas coisas coexistem como cenários distintos. A baseline histórica, sob a política padrão, continua totalizando R$585,43 (CA-037). O mesmo arquivo, sob a política v4 oficial e `CC-ENG-PLATAFORMA`, totaliza R$351,43 (CA-038) — mudança correta e esperada, não uma regressão de código.
**Justificativa:** escolher entre "manter" e "atualizar" o fixture original é uma falsa dicotomia técnica escondendo uma decisão de produto; preservar ambos documenta tanto o comportamento histórico quanto o comportamento correto sob a v4, sem que a mudança de total seja confundida com um bug quando o código for atualizado.
**Regra afetada:** RN-019 (nenhuma regra da linha de base foi invalidada em si — o resultado muda porque a política aplicada muda)

### AMB-033 — Escopo do item C (fila de aprovação manual) · O

**Texto original do RH:** "(Opcional — só se sobrar tempo) Fila de aprovação manual. Itens cujo valor reembolsável passe de R$500 não são mais aprovados automaticamente."
**O que não está claro:** nada de interpretação — o próprio comunicado marca o item como opcional; a decisão aqui é de escopo, não de leitura de texto.
**Decisão:** não implementado nesta rodada. Nenhum estado `AGUARDANDO_APROVACAO`, nenhuma fila, nenhuma alteração de decisão por valor acima de R$500.
**Justificativa:** o comunicado prioriza explicitamente os itens A e B; nenhum item dos dois arquivos de exemplo do envelope ultrapassa R$500 sob a interpretação adotada, então nada nos dados obriga essa decisão agora.
**Regra afetada:** nenhuma (fora de escopo)

### AMB-034 — Forma de fornecimento da política e do câmbio à CLI · O

**Texto original do RH:** nenhum — o comunicado não descreve mecanismo de execução, só a existência dos arquivos `politica-v4.json` e `cambio.json`.
**O que não está claro:** como a execução recebe os dois arquivos externos — argumento posicional, flag nomeada, caminho fixo por convenção, variável de ambiente — e como o comportamento de erro se manifesta quando eles estão ausentes ou inválidos. O contrato de execução é comportamento observável do produto: sem uma decisão explícita e normativa, duas implementações igualmente corretas quanto às regras de negócio poderiam divergir na forma de invocação.
**Decisão:** contrato fixo, com quatro argumentos nomeados e obrigatórios, em qualquer ordem, cada um uma única vez: `calcular --input <entrada.json> --output <saida.json> --politica <politica.json> --cambio <cambio.json>`. Argumento ausente, repetido ou desconhecido, ou política/câmbio inexistente, ilegível, sintaticamente inválido ou estruturalmente inválido (AMB-035) → código de saída `2` — a mesma classe já usada para `--input` ilegível, sem código novo. Envelope de despesas estruturalmente inválido continua código `3`; sucesso continua `0`. stdout permanece vazio em qualquer cenário; erros vão para stderr; uma saída preexistente é preservada intacta em qualquer falha global. Detalhado por completo em 4.1.1.
**Justificativa:** reaproveitar o código `2` (em vez de criar `4`/`5` para política/câmbio) mantém a régua já estabelecida — `2` é "problema de infraestrutura ou de entrada", `3` é "envelope de despesas semanticamente inválido" — sem multiplicar códigos para uma distinção que stderr já comunica em texto. Exigir `--cambio` mesmo para entradas só em BRL evita que o contrato de execução dependa do conteúdo da entrada, o que tornaria o comando variável e mais difícil de automatizar.
**Regra afetada:** RN-022 (formaliza o contrato de execução que a regra pressupõe)

### AMB-035 — Contrato estrutural dos arquivos externos · O

**Texto original do RH:** nenhum — os arquivos `politica-v4.json` e `cambio.json` do envelope são exemplos concretos, não uma descrição de esquema.
**O que não está claro:** RN-022 (na forma anterior a esta correção) dizia "estruturalmente inválido" sem definir precisamente o que isso significa para cada arquivo — sem um contrato fechado, a expressão não é verificável sem ler o código, contrariando o próprio critério de aceite da spec ("verificável sem leitura de código").
**Decisão:** contrato fechado, documentado por completo em 4.1.1: raiz de ambos os arquivos deve ser objeto; política exige `vigencia`, `moeda_base` (exatamente `"BRL"`), `padrao` (objeto, pode ser vazio), `centros_custo` (objeto, pode ser vazio) e `nota_fiscal_obrigatoria_acima_de` (número não negativo), com `versao` e `acrescimo_em_viagem_percentual` aceitos como metadados ignorados (o segundo **não** ativa RN-016); cada tabela de categoria exige `limite` (número ≥ 0 dentro de `centros_custo`; estritamente > 0 dentro de `padrao` — correção da segunda revisão, ver ao final de `DECISIONS.md` `D-003`) e `periodicidade` (exatamente `"dia"` ou `"diaria"` — AMB-036); câmbio exige `moeda_base` (exatamente `"BRL"`) e `taxas` (objeto, podendo ser vazio, aninhando data → moeda → taxa estritamente positiva), com `fonte` e `observacao` como metadados informativos.
**Justificativa:** a correção também revela que a raiz de `cambio.json` **não** é um mapa direto de data para moeda, como uma versão anterior desta spec presumiu por engano — as cotações estão aninhadas sob a chave `taxas`, junto de `moeda_base`, `fonte` e `observacao`. Um contrato fechado e correto evita essa classe de erro se repetir na implementação, e torna "estruturalmente inválido" checável item a item, sem ambiguidade.
**Regra afetada:** RN-019, RN-020, RN-021, RN-022 (esclarecidos por este contrato — nenhuma decisão de negócio nova além do que já estava implícito)

### AMB-036 — Semântica de `periodicidade` das categorias externas · U

**Texto original do RH:** nenhum — `periodicidade: "dia"` e `periodicidade: "diaria"` aparecem em `politica-v4.json` sem que o comunicado defina o que cada valor significa operacionalmente; a spec 1.1 só conhecia essa distinção de forma implícita, hardcoded por categoria (alimentação/transporte "por dia", hospedagem "por diária").
**O que não está claro:** com a política externa, a periodicidade passa a ser dado, não mais uma propriedade fixa de cada categoria — inclusive uma categoria nova como `representacao` traz a sua própria periodicidade declarada (`"dia"`). É preciso fixar o que cada valor de `periodicidade` implica mecanicamente.
**Decisão:** `periodicidade: "dia"` — teto compartilhado pelos itens elegíveis da mesma categoria e data, consumido em ordem de `indice_entrada` (RN-015). `periodicidade: "diaria"` — teto individual por lançamento, sem saldo compartilhado. Limite `0,00` continua sendo recusa por política (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`) antes de qualquer teto, independentemente da periodicidade. Categoria com `periodicidade` diferente desses dois valores torna o **arquivo de política** inválido — não é um defeito do item.
**Justificativa:** os dois valores já correspondem exatamente aos dois mecanismos de teto que a spec 1.1 já implementava para alimentação/transporte (compartilhado) e hospedagem (individual) — a política externa só nomeia explicitamente o que já era a regra. Tratar um valor de `periodicidade` fora do vocabulário fechado como erro de arquivo (não de item) é consistente com RN-022: é a política que está malformada, não a despesa.
**Regra afetada:** RN-019

### AMB-037 — Motivo de limitação para categoria externa com periodicidade individual · U

**Texto original do RH:** nenhum — decorre de `AMB-036` permitir qualquer categoria externa sob `periodicidade: "diaria"`, enquanto o vocabulário de motivos herdado da linha de base só previa `TETO_HOSPEDAGEM_APLICADO`, semanticamente específico de hospedagem.
**O que não está claro:** com a política externa podendo declarar qualquer categoria — não apenas `hospedagem` — sob `periodicidade: "diaria"` (ex.: uma categoria `estacionamento`), o motivo de teto individual carecia de nome genérico: usar `TETO_HOSPEDAGEM_APLICADO` fora de `hospedagem` produziria saída auditável, porém semanticamente incorreta — quem lê o motivo presumiria hospedagem mesmo quando a categoria é outra.
**Decisão:** cria-se o motivo `TETO_INDIVIDUAL_APLICADO`, com contrato `codigo: TETO_INDIVIDUAL_APLICADO`, `regra: RN-019`, `campo: null`, aparecendo em itens `PARCIALMENTE_REEMBOLSADO`. Aplica-se quando, simultaneamente: a categoria é diferente de `hospedagem`; está presente na tabela de política efetivamente aplicável; essa tabela declara `limite > 0` para a categoria; a `periodicidade` declarada é `"diaria"`; e o valor normalizado excede esse limite. O item é reembolsado até o limite individual. Preservados sem alteração: `TETO_HOSPEDAGEM_APLICADO` continua exclusivo de `hospedagem` sob `periodicidade: "diaria"`; `TETO_DIARIO_APLICADO` continua o motivo de qualquer categoria sob `periodicidade: "dia"`; `TETO_DIARIO_ESGOTADO` continua o motivo dos itens posteriores ao esgotamento de um teto `"dia"`.
**Justificativa:** um motivo genérico fecha o vocabulário de teto individual sem exigir um código novo a cada categoria futura que a política venha a declarar sob `periodicidade: "diaria"`, e evita que a saída sugira hospedagem onde não há.
**Regra afetada:** RN-019

---

## 7. Casos de borda

Salvo indicação em contrário, os valores `60`, `80`, `250` e `100` usados nesta tabela são os da política externa de teste equivalente à política histórica/padrão (`alimentacao` R$60,00/dia, `transporte_urbano` R$80,00/dia, `hospedagem` R$250,00/diária, `nota_fiscal_obrigatoria_acima_de` R$100,00 — CA-037): são dados de uma política, não constantes internas do sistema, e um centro de custo cadastrado pode declarar valores diferentes (RN-019).

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
| Limite de categoria igual a `0,00` no centro de custo | Hospedagem em `CC-ENG-PLATAFORMA` | Recusado, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — nunca `PARCIALMENTE_REEMBOLSADO` | RN-019 |
| Categoria ausente na tabela do centro de custo cadastrado | Hospedagem em `CC-ADM` | Recusado, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — sem fallback para `padrao` | RN-019 |
| Centro de custo fora da tabela | `CC-SUPORTE-N2` | Usa `padrao` integralmente | RN-019 |
| Categoria fora de qualquer tabela, centro de custo desconhecido | `representacao` para colaborador de centro de custo desconhecido (tabela aplicável `padrao`) | Recusado, `CATEGORIA_FORA_POLITICA` | RN-007, RN-019 |
| Categoria ausente da tabela de um centro cadastrado, mas inexistente em toda a política | `coworking` para colaborador de `CC-COMERCIAL` (cadastrado; `coworking` não existe em nenhuma tabela) | Recusado, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — não `CATEGORIA_FORA_POLITICA`, porque a tabela aplicável é a de `CC-COMERCIAL` | RN-007, RN-019 |
| Categoria de periodicidade `"dia"` introduzida pela política externa | Dois itens de `representacao` (`CC-COMERCIAL`) na mesma data | Saldo compartilhado de R$300,00, mesmo mecanismo de RN-011/RN-012/RN-015 | RN-019 |
| Categoria externa não-hospedagem com periodicidade `"diaria"` acima do limite | `estacionamento`, limite R$50,00, despesa elegível de R$80,00 | Parcial, `50,00`, motivo `TETO_INDIVIDUAL_APLICADO` — não `TETO_HOSPEDAGEM_APLICADO` | RN-019 |
| Limite de categoria igual a `0,00` em `padrao` | `padrao.<categoria>.limite = 0` | Arquivo de política estruturalmente inválido; nenhuma apuração, código de saída `2` | RN-019, RN-022 |
| Data da despesa sem cotação exata | EUR num sábado sem cotação própria | Usa a cotação do último dia útil anterior | RN-020 |
| Moeda sem qualquer cotação em `cambio.json` | GBP | Recusado, `MOEDA_SEM_COTACAO` | RN-020 |
| Moeda estrangeira acima do gatilho de nota, sem nota | USD `40,00` convertido a `220,00`, sem nota | Recusado, `NOTA_FISCAL_AUSENTE` — compara o valor convertido | RN-009, RN-020 |
| Moedas diferentes, mesmo valor convertido | EUR e BRL coincidentes após conversão | **Não** são duplicatas | RN-010 |
| Moeda ausente | Chave `despesa.moeda` não presente no objeto | Assume `BRL`; sem motivo estrutural | RN-002 |
| Moeda nula | `"moeda": null` (chave presente, valor `null`) | Recusado, `CAMPO_AUSENTE`, `campo` igual a `despesa.moeda` | RN-002 |
| Moeda em caixa baixa | `"usd"` | `CAMPO_FORMATO_INVALIDO` | RN-002 |
| Vigência da política ausente | Arquivo de política sem `vigencia` | Arquivo de política inválido; nenhuma apuração | RN-021 |

---

## 8. Ordem de aplicação das regras

A política enuncia nove itens sem precedência entre si, e a ordem altera o resultado. Esta seção fixa a ordem.

### 8.1 Ordem de processamento

| # | Passo |
|---|---|
| 1 | Validar os arquivos externos de política e câmbio (RN-021, RN-022) |
| 2 | Validar o envelope do arquivo de despesas (RN-001) |
| 3 | Validar presença, tipo e formato dos campos de cada item, incluindo `despesa.moeda` (RN-002) |
| 4 | Identificar `despesa.id` válidos repetidos e recusar todas as respectivas ocorrências (RN-003) |
| 5 | Resolver a taxa de câmbio aplicável e converter o valor para BRL quando `despesa.moeda` for estrangeira (RN-020) |
| 6 | Normalizar os campos estruturalmente válidos: valor (já convertido) e categoria (RN-004, RN-005) |
| 7 | Avaliar as regras individuais de negócio aplicáveis, incluindo a elegibilidade por política do centro de custo (RN-006 a RN-009, RN-019) |
| 8 | Selecionar os itens aprovados em todas as validações individuais |
| 9 | Detectar e tratar a duplicidade econômica nessa população (RN-010) |
| 10 | Selecionar os itens elegíveis após a duplicidade |
| 11 | Aplicar agregação e tetos, com os limites resolvidos pela política do centro de custo (RN-011 a RN-015, RN-019) |
| 12 | Produzir uma saída por item, na ordem da entrada, incluindo os campos de auditoria de câmbio (RN-017) |
| 13 | Somar os valores reembolsáveis apresentados (RN-018) |

Os passos 1 e 2 a 4 são heranças diretas de RN-001/RN-002/RN-003 (linha de base); os passos 5, 7 (parcialmente) e 11 (parcialmente) incorporam a política v4 (Dia 2, câmbio e política por centro de custo). Os passos 4 e 5 podem ser vistos como paralelos entre si (nenhum depende do resultado do outro), mas são apresentados em sequência para manter uma única ordem determinística e testável.

### 8.2 Matriz de dependências

Cada regra depende dos dados abaixo. Uma regra não é avaliada quando um campo obrigatório do item de que dependa estiver estruturalmente inválido ou quando um valor derivado necessário não puder ser calculado. `colaborador.centro_custo` é uma exceção explícita: ausência, nulo, tipo inválido ou valor desconhecido não bloqueiam regra alguma e resolvem para a tabela `padrao`, conforme RN-019.

| Regra | Campos necessários |
|---|---|
| Unicidade de `despesa.id` | `despesa.id` |
| Resolução de câmbio / conversão monetária | `despesa.valor`, `despesa.moeda`, `despesa.data` |
| Normalização monetária | `despesa.valor` (já convertido, quando aplicável) |
| Normalização de categoria | `despesa.categoria` |
| Valor não positivo | `valor_normalizado` calculável |
| Categoria fora da política / política por centro de custo | `despesa.categoria`, tabela de política resolvida por `colaborador.centro_custo`, incluindo fallback para `padrao` |
| Competência | `despesa.data`, `periodo.inicio`, `periodo.fim` |
| Nota fiscal obrigatória | `valor_normalizado` calculável e `despesa.tem_nota_fiscal` |
| Duplicidade econômica | `despesa.data`, `despesa.categoria` normalizada, `despesa.moeda`, `valor_normalizado` calculável, `despesa.fornecedor`, `despesa.descricao` |
| Teto compartilhado `"dia"` | `despesa.data`, `despesa.categoria`, `valor_normalizado` calculável, tabela de política resolvida por `colaborador.centro_custo`, incluindo fallback para `padrao`, e a `periodicidade` declarada nela |
| Teto individual `"diaria"` | `despesa.categoria`, `valor_normalizado` calculável, tabela de política resolvida por `colaborador.centro_custo`, incluindo fallback para `padrao`, e a `periodicidade` declarada nela — inclui `hospedagem` (`TETO_HOSPEDAGEM_APLICADO`) e qualquer outra categoria sob esse mecanismo (`TETO_INDIVIDUAL_APLICADO`) |

### 8.3 Ordem de apresentação dos motivos

A ordem de processamento e a ordem de apresentação são **duas ordens determinísticas distintas**, com finalidades diferentes: a primeira determina quando cada regra é avaliada; a segunda, como os resultados são apresentados.

1. `ITEM_TIPO_INVALIDO` — quando presente, é o motivo único da posição
2. Erros de campos estruturais, na ordem canônica de contrato: `despesa.id`, `despesa.data`, `despesa.categoria`, `despesa.descricao`, `despesa.fornecedor`, `despesa.valor`, `despesa.moeda`, `despesa.tem_nota_fiscal`
3. `ID_DUPLICADO`
4. `MOEDA_SEM_COTACAO` (política v4, Dia 2)
5. `VALOR_NAO_POSITIVO`
6. `CATEGORIA_FORA_POLITICA`
7. `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (política v4, Dia 2)
8. `FORA_COMPETENCIA`
9. `NOTA_FISCAL_AUSENTE`
10. `DUPLICIDADE`

Os motivos de limitação de teto (`TETO_DIARIO_APLICADO`, `TETO_DIARIO_ESGOTADO`, `TETO_HOSPEDAGEM_APLICADO`, `TETO_INDIVIDUAL_APLICADO`) vêm depois de todos os anteriores, quando aplicáveis — na prática, aparecem sozinhos, porque um item que chega à etapa de teto já não carrega nenhum motivo de recusa.

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
14. Item com `MOEDA_SEM_COTACAO` tem `valor_normalizado` nulo e por isso não participa das regras que dependem dele — RN-006, RN-009, RN-010 e qualquer teto (RN-011 a RN-015, RN-019) —, mesmo tratamento de dependência de campo estruturalmente inválido (item 3 acima), agora por ausência de dado externo em vez de defeito de contrato (política v4, Dia 2). A elegibilidade de categoria (RN-007, RN-019) e a elegibilidade temporal (RN-008) não dependem de `valor_normalizado` e continuam sendo avaliadas normalmente quando seus próprios campos são utilizáveis — um item com `MOEDA_SEM_COTACAO` pode apresentar, junto dele, `CATEGORIA_FORA_POLITICA`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMPETENCIA`.
15. Limite igual a `0,00` dentro da tabela de um centro de custo cadastrado nunca produz `PARCIALMENTE_REEMBOLSADO`: o item é recusado com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` antes de qualquer teto ser avaliado — não é uma variante de RN-013/RN-014 com teto zero (política v4, Dia 2). Limite igual a `0,00` dentro de `padrao` não chega sequer à avaliação do item: invalida o arquivo de política inteiro, com código de saída `2` (RN-019, RN-022, correção da segunda revisão).
16. Arquivo de política ou de câmbio estruturalmente inválido impede toda a apuração, inclusive de despesas em BRL que não dependeriam de câmbio algum — o erro é do arquivo externo, não do item (política v4, Dia 2, RN-022).

**Exemplo de motivos múltiplos:** item de R$ 500,00, categoria `coworking`, sem nota, datado fora da janela, de um colaborador de centro de custo desconhecido ou ausente (tabela aplicável `padrao`, que não declara `coworking`), com todos os campos estruturalmente válidos, apresenta três motivos — `CATEGORIA_FORA_POLITICA`, `FORA_COMPETENCIA` e `NOTA_FISCAL_AUSENTE`.

**Exemplo de exclusão:** item de −R$ 500,00, categoria `coworking`, sem nota, de um colaborador de centro de custo desconhecido ou ausente (tabela aplicável `padrao`), apresenta `VALOR_NAO_POSITIVO` e `CATEGORIA_FORA_POLITICA`, mas **não** apresenta `NOTA_FISCAL_AUSENTE`.

**Exemplo de motivo por política de centro de custo (Dia 2):** um lançamento de hospedagem de R$480,00 em `CC-ENG-PLATAFORMA`, com nota fiscal e dentro da competência, apresenta apenas `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — nenhum motivo de teto é avaliado, porque a recusa por política do centro de custo antecede a etapa de agregação (item 15 acima).

### 8.5 Estados possíveis por categoria

Os estados dependem da tabela de política e da `periodicidade` efetivamente aplicáveis ao item (RN-019) — não do nome histórico da categoria:

| Situação na tabela efetivamente aplicável | Estados possíveis |
|---|---|
| Categoria presente, `limite` positivo, `periodicidade: "dia"` (ex.: `alimentacao`, `transporte_urbano`, `representacao` quando declarada) | `INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO` (`TETO_DIARIO_APLICADO`), `NAO_REEMBOLSADO_TETO_ESGOTADO` (`TETO_DIARIO_ESGOTADO`), `RECUSADO` (por outra regra individual) |
| Categoria presente, `limite` positivo, `periodicidade: "diaria"`, sendo `hospedagem` | `INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO` (`TETO_HOSPEDAGEM_APLICADO`), `RECUSADO` (por outra regra individual). **Nunca** `NAO_REEMBOLSADO_TETO_ESGOTADO`, por não participar de saldo compartilhado |
| Categoria presente, `limite` positivo, `periodicidade: "diaria"`, sendo outra categoria que não `hospedagem` (ex.: `estacionamento`, AMB-037) | `INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO` (`TETO_INDIVIDUAL_APLICADO`), `RECUSADO` (por outra regra individual). **Nunca** `NAO_REEMBOLSADO_TETO_ESGOTADO`, pelo mesmo motivo de `hospedagem` |
| Categoria ausente de `padrao` (tabela aplicável é `padrao`) | Apenas `RECUSADO` (`CATEGORIA_FORA_POLITICA`); não alcança a etapa de teto |
| Categoria ausente da tabela de um centro de custo cadastrado (tabela aplicável é a do centro) | Apenas `RECUSADO` (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`); não alcança a etapa de teto |
| Categoria presente na tabela de um centro de custo cadastrado, com `limite` igual a `0,00` | Apenas `RECUSADO` (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`); não alcança a etapa de teto, mesmo sendo uma categoria com estados de teto em outros centros de custo |

`limite` igual a `0,00` dentro de `padrao` não gera um estado de item: invalida o arquivo de política inteiro (RN-019, RN-022) antes de qualquer item ser avaliado.

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

**Comunicado do RH — Política v4 (Dia 2).** Mesma lógica, aplicada ao envelope de `2026-08-05`:

| Item do comunicado | Ambiguidades | Regras | Critérios de aceite |
|---|---|---|---|
| A — Limites por centro de custo | AMB-019, AMB-020, AMB-021, AMB-022 | RN-019 | CA-024 a CA-027, CA-038 a CA-040 |
| B — Despesas internacionais (câmbio e conversão) | AMB-023, AMB-024, AMB-025, AMB-026, AMB-027, AMB-028, AMB-029 | RN-020, RN-004, RN-009, RN-010 (atualizadas) | CA-028 a CA-034 |
| — (vigência e arquivos externos) | AMB-030, AMB-031 | RN-021, RN-022 | CA-035, CA-036 |
| — (regressão) | AMB-032 | RN-019 | CA-037, CA-038 |
| C — Fila de aprovação manual (opcional, fora de escopo) | AMB-033 | — | — |
| — (contrato de execução, correção pós-revisão) | AMB-034 | RN-022 | CA-041 a CA-044 |
| — (contrato estrutural dos arquivos externos, correção pós-revisão) | AMB-035 | RN-019, RN-020, RN-021, RN-022 | CA-045, CA-046 |
| — (periodicidade das categorias externas, correção pós-revisão) | AMB-036 | RN-019 | CA-047 |
| — (contrato de `despesa.moeda: null`, correção pós-revisão) | — | RN-002 | CA-048 |
| — (motivo de teto individual para categoria externa, segunda correção pós-revisão) | AMB-037 | RN-019 | CA-049 |

---

## 10. Critérios de aceite

O sistema está pronto quando todos os itens abaixo forem verificáveis sem leitura de código.

- [ ] **CA-001** — Processar `exemplos/despesas-exemplo.json` com uma política externa de teste equivalente à política histórica/padrão (`alimentacao` R$60,00/dia, `transporte_urbano` R$80,00/dia, `hospedagem` R$250,00/diária, `nota_fiscal_obrigatoria_acima_de` R$100,00) produz `total_reembolsavel` igual a **R$ 585,43**.
- [ ] **CA-002** — Sob esse mesmo cenário histórico, a saída daquele arquivo contém 14 registros, na ordem da entrada, com `indice_entrada` de 1 a 14, e cada linha coincide com a tabela 4.7 em decisão, valor reembolsável e motivos.
- [ ] **CA-003** — A soma dos `valor_reembolsavel` apresentados é igual ao `total_reembolsavel` apresentado.
- [ ] **CA-004** — Sob a política padrão (`alimentacao` limite R$60,00/dia, `periodicidade: "dia"`), duas despesas de alimentação na mesma data somando mais de R$ 60,00 rendem exatamente R$ 60,00 naquela data.
- [ ] **CA-005** — Sob a política padrão, uma despesa de R$ 61,00 de alimentação, sozinha na data, rende R$ 60,00 e não R$ 0,00.
- [ ] **CA-006** — Sob a política padrão, com itens de R$ 72,50 e R$ 38,00 nessa ordem no mesmo dia, o primeiro rende R$ 60,00 com `PARCIALMENTE_REEMBOLSADO` e o segundo rende R$ 0,00 com `NAO_REEMBOLSADO_TETO_ESGOTADO` — não `RECUSADO`.
- [ ] **CA-007** — Sob a política padrão (`hospedagem` limite R$250,00/diária, `periodicidade: "diaria"`), uma hospedagem de R$ 480,00 descrita como "2 diarias" rende R$ 250,00, e alterar o texto da `descricao` não altera o teto individual de R$ 250,00 aplicado a esse lançamento.
- [ ] **CA-008** — Sob uma política com `nota_fiscal_obrigatoria_acima_de = 100.00` (o valor da política padrão/histórica usada nos exemplos desta spec; não é uma constante interna do sistema — RN-009), uma despesa de exatamente R$ 100,00 sem nota é elegível; uma de R$ 100,01 sem nota é recusada.
- [ ] **CA-009** — Sob a mesma política (`nota_fiscal_obrigatoria_acima_de = 100.00`), um valor informado de `100.004` sem nota é elegível e um de `100.005` sem nota é recusado.
- [ ] **CA-010** — Numa entrada com um único item elegível, trocar na `descricao` um texto neutro por termos como "aeroporto" ou "hotel" não amplia teto algum e não altera o `valor_reembolsavel`; um campo `em_viagem: true` também não altera resultado algum. O cenário usa item único para isolar a ausência de inferência de viagem, já que `descricao` integra a chave de duplicidade.
- [ ] **CA-011** — Uma despesa datada em `2026-04-15`, com janela de julho, é recusada com `FORA_COMPETENCIA`.
- [ ] **CA-012** — Despesas datadas exatamente em `periodo.inicio` e em `periodo.fim` são temporalmente elegíveis.
- [ ] **CA-013** — Dois itens idênticos exceto pelo `despesa.id`, ambos elegíveis para reembolso integral antes da avaliação de duplicidade — categoria presente na tabela de política aplicável, dentro da competência, com nota fiscal quando exigida, e saldo de teto suficiente —, produzem o primeiro integral e o segundo recusado com `DUPLICIDADE`; duplicidade não confere elegibilidade que o item não teria por si.
- [ ] **CA-014** — Itens de R$ 100,00 e R$ 100,01, mesmo dia e mesmo fornecedor, não são tratados como duplicatas.
- [ ] **CA-015** — Uma despesa com categoria `ALIMENTACAO` é processada como `alimentacao`.
- [ ] **CA-016** — Para um colaborador de centro de custo desconhecido, ausente, nulo ou de tipo inválido (tabela aplicável `padrao`, que não declara `coworking`), uma despesa com categoria `coworking` é recusada com `CATEGORIA_FORA_POLITICA` e reembolsável R$ 0,00 — este critério não generaliza para centro de custo cadastrado, onde a mesma ausência produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (CA-025).
- [ ] **CA-017** — Uma despesa de −R$ 45,00 é recusada com `VALOR_NAO_POSITIVO` e não reduz o total do período.
- [ ] **CA-018** — Sob a política padrão, com o item elegível (categoria presente na tabela, dentro da competência, com nota fiscal quando exigida, e saldo de teto suficiente), um valor informado de `33.333` aparece como `valor_informado` `33.333`, `valor_normalizado` `33,33` e `valor_reembolsavel` `33,33`.
- [ ] **CA-019** — Dois itens com o mesmo `despesa.id` válido são **ambos** recusados com `ID_DUPLICADO`.
- [ ] **CA-020** — Uma entrada com `periodo.inicio` posterior a `periodo.fim` não produz apuração alguma; uma entrada com `despesas: []` produz resultados vazio e total R$ 0,00.
- [ ] **CA-021** — Um item com `data: "31/07/2026"` e `valor: "72,50"` apresenta dois motivos estruturais na ordem canônica de contrato: `CAMPO_FORMATO_INVALIDO` com `campo` igual a `despesa.data`, depois `CAMPO_TIPO_INVALIDO` com `campo` igual a `despesa.valor`.
- [ ] **CA-022** — Um elemento de `despesas` que não seja objeto produz exatamente um registro `RECUSADO`, com `indice_entrada` preservado, `id` nulo, `valor_informado` nulo, `valor_normalizado` nulo, `valor_reembolsavel` R$ 0,00 e um único motivo `ITEM_TIPO_INVALIDO` com `campo` nulo — sem os sete motivos de campo ausente —, e o restante do arquivo continua sendo apurado. Todo objeto de motivo da saída traz `campo` nulo ou um dos oito nomes canônicos de 4.2, e nenhum outro valor.
- [ ] **CA-023** — Um item com `despesa.id` igual a texto vazio, `despesa.data` igual a `"31/07/2026"`, `despesa.categoria` igual a um número, `despesa.valor` igual a `"72,50"` e `despesa.tem_nota_fiscal` igual a `"sim"` (demais campos válidos) é recusado com cinco motivos, na ordem canônica de contrato — `CAMPO_FORMATO_INVALIDO` (`despesa.id`), `CAMPO_FORMATO_INVALIDO` (`despesa.data`), `CAMPO_TIPO_INVALIDO` (`despesa.categoria`), `CAMPO_TIPO_INVALIDO` (`despesa.valor`), `CAMPO_TIPO_INVALIDO` (`despesa.tem_nota_fiscal`). `valor_informado` preserva `"72,50"`; `valor_normalizado` é nulo; `valor_reembolsavel` é `0,00`.

**Envelope do Dia 2 — política v4:**

- [ ] **CA-024** — Um colaborador de centro de custo desconhecido, ausente, nulo ou de tipo inválido usa integralmente a política `padrao` — nenhuma mistura com nenhuma tabela específica.
- [ ] **CA-025** — Categoria ausente na tabela de um centro de custo cadastrado (ex.: `hospedagem` em `CC-ADM`) é recusada com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, sem receber o limite de `padrao`.
- [ ] **CA-026** — `representacao` é reembolsável somente quando a única tabela aplicável a declara. Em `CC-COMERCIAL`, aplica-se o limite configurado. Quando a tabela aplicável é `padrao` e ela não declara `representacao`, o item recebe `CATEGORIA_FORA_POLITICA`. Quando a tabela aplicável é a de outro centro de custo cadastrado que não declara `representacao`, o item recebe `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`.
- [ ] **CA-027** — Categoria com limite de política igual a `0,00` (ex.: `hospedagem` em `CC-ENG-PLATAFORMA`) é recusada com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e `valor_reembolsavel` `0,00` — nunca `PARCIALMENTE_REEMBOLSADO`.
- [ ] **CA-028** — Numa despesa em moeda estrangeira, `moeda != BRL` não amplia teto algum, não afeta outros itens do mesmo dia nem do período — RN-016 continua sem efeito.
- [ ] **CA-029** — Uma despesa datada num dia sem cotação exata usa a cotação do último dia útil anterior disponível (ex.: EUR de `2026-07-18`, sábado, usa a cotação de `2026-07-17`).
- [ ] **CA-030** — Uma despesa em moeda sem qualquer cotação em `cambio.json` (ex.: GBP) é recusada com `MOEDA_SEM_COTACAO`, `valor_normalizado` nulo e `valor_reembolsavel` `0,00`.
- [ ] **CA-031** — A conversão multiplica o valor bruto pela taxa e arredonda uma única vez: USD `40,00` × `5,50` normaliza para `220,00`.
- [ ] **CA-032** — O gatilho de nota fiscal compara o valor convertido, não o original: USD `40,00` (abaixo de R$100 na moeda original) convertido para R$220,00, sem nota fiscal, é recusado com `NOTA_FISCAL_AUSENTE`.
- [ ] **CA-033** — Dois itens com mesma data, categoria, fornecedor, descrição e valor convertido coincidente, mas em moedas diferentes, **não** são tratados como duplicata.
- [ ] **CA-034** — A saída traz `moeda`, `taxa_cambio_aplicada` e `data_cotacao_utilizada` nos quatro formatos: BRL (`1`, `null`), moeda estrangeira convertida (taxa e data da cotação usada), moeda estruturalmente inválida (os três campos nulos) e moeda válida sem cotação (os três campos nulos, mais `valor_normalizado` nulo e `valor_reembolsavel` `0,00`).
- [ ] **CA-035** — Um arquivo de política sem `politica.vigencia`, ou com `vigencia` malformada, não produz apuração alguma.
- [ ] **CA-036** — Um arquivo de política ou de câmbio ausente, ilegível, sintaticamente inválido ou estruturalmente inválido impede toda a apuração, mesmo com um arquivo de despesas válido.
- [ ] **CA-037** — `exemplos/despesas-exemplo.json`, processado com uma política externa equivalente aos limites históricos (política padrão: alimentação R$60/dia, transporte R$80/dia, hospedagem R$250/diária, gatilho de nota R$100), totaliza `total_reembolsavel` igual a **R$585,43** — baseline de regressão histórica preservada.
- [ ] **CA-038** — O mesmo `exemplos/despesas-exemplo.json`, processado com a política oficial `politica-v4.json` e o centro de custo real do colaborador (`CC-ENG-PLATAFORMA`), totaliza `total_reembolsavel` igual a **R$351,43**, com `d-001` integral (`72,50`), `d-002` parcial (`2,50`), `d-010` recusado (`0,00`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`) e `d-014` integral (`61,00`).
- [ ] **CA-039** — `exemplos/envelope/despesas-envelope.json` (Rafael Nkemelu, `CC-COMERCIAL`), processado com `politica-v4.json` e `cambio.json`, totaliza `total_reembolsavel` igual a **R$1.143,26**, conforme a tabela de `§12`.
- [ ] **CA-040** — `exemplos/envelope/despesas-envelope-cc-desconhecido.json` (Dani Okonkwo, `CC-SUPORTE-N2`), processado com `politica-v4.json` e `cambio.json`, totaliza `total_reembolsavel` igual a **R$373,76**, conforme a tabela de `§12`.

**Correção após revisão independente:**

- [ ] **CA-041** — `calcular --input <e> --output <s> --politica <p> --cambio <c>`, com as quatro flags em qualquer ordem e cada uma presente exatamente uma vez, processa normalmente e retorna código de saída `0`.
- [ ] **CA-042** — Argumento ausente, repetido ou desconhecido retorna código `2` — inclusive o comando anterior à política v4, com apenas `--input`/`--output`, mesmo quando a entrada só tem despesas em BRL.
- [ ] **CA-043** — Arquivo de política ou de câmbio inexistente, ilegível, sintaticamente inválido ou estruturalmente inválido (conforme o contrato fechado de 4.1.1) retorna código `2`, mesmo com um arquivo de despesas perfeitamente válido.
- [ ] **CA-044** — Em qualquer cenário de código `2` ou `3`, um arquivo em `--output` que já existisse antes da execução permanece intacto, byte a byte; stdout permanece vazio e a mensagem de erro vai para stderr.
- [ ] **CA-045** — Um arquivo de política que satisfaça integralmente o contrato de 4.1.1 (`vigencia` real, `moeda_base` exatamente `"BRL"`, `padrao` e `centros_custo` objetos, `nota_fiscal_obrigatoria_acima_de` não negativo, cada tabela de categoria com `periodicidade` igual a `"dia"` ou `"diaria"`, `limite` ≥ 0 dentro de `centros_custo` e `limite` estritamente > 0 dentro de `padrao`) é aceito; qualquer violação de um desses pontos — inclusive `limite` igual a `0,00` em `padrao` — invalida o arquivo de política por inteiro.
- [ ] **CA-046** — Um arquivo de câmbio que satisfaça o contrato de 4.1.1 (`moeda_base` exatamente `"BRL"`, `taxas` objeto aninhando data real → moeda `[A-Z]{3}` → taxa estritamente positiva) é aceito, inclusive com `taxas: {}` — nesse caso, toda despesa em moeda estrangeira é recusada individualmente com `MOEDA_SEM_COTACAO`, sem invalidar o arquivo.
- [ ] **CA-047** — Dois itens de `representacao` (`CC-COMERCIAL`, `periodicidade: "dia"`, limite R$300,00) na mesma data consomem conjuntamente o mesmo saldo de R$300,00, distribuído em ordem de `indice_entrada` — o mesmo mecanismo de RN-011/RN-012/RN-015, não um teto individual por item.
- [ ] **CA-048** — Um item com `"moeda": null` é recusado com `CAMPO_AUSENTE` e `campo` igual a `despesa.moeda`; um item sem a chave `moeda` no objeto não recebe motivo algum e assume `BRL`.

**Segunda correção após revisão independente:**

- [ ] **CA-049** — Sob uma política válida em que a categoria `estacionamento` tenha `limite: 50.00` e `periodicidade: "diaria"`, uma despesa elegível de R$ 80,00 nessa categoria é `PARCIALMENTE_REEMBOLSADO`, com `valor_reembolsavel` `50,00` e motivo único `{ "codigo": "TETO_INDIVIDUAL_APLICADO", "regra": "RN-019", "campo": null }`.

---

## 11. O que fica em aberto

Nenhum comportamento da versão atual está indefinido. Os pontos abaixo são **limitações de produto**: cada um exigiria mudança explícita do contrato de entrada ou da política para ser resolvido.

| Limitação | O que seria necessário |
|---|---|
| A regra 6 da política não produz efeito algum | Um campo estruturado de viagem na entrada, mais decisão de produto sobre quais limites são ampliados e se a fronteira documental acompanha |
| Hospedagem de várias diárias é limitada como se fosse uma | Um campo estruturado de quantidade de diárias, ou datas de entrada e saída |
| Estadia fragmentada em vários lançamentos recebe mais do que a mesma estadia em um lançamento | Um identificador de estadia que permita reconhecer fragmentação com segurança |
| Estornos e créditos não compensam despesas | Uma política de compensação que defina contra o quê o crédito abate e em que ordem |
| Contexto de consumo é ignorado — fim de semana, plantão, refeição inclusa na hospedagem | Regras na política que criem limite, proibição ou tratamento distinto para esses contextos. Não se confunde com a categoria formal `representacao` (política v4, RN-019) — esta linha trata do contexto informal de consumo dentro de uma categoria já reembolsável, não da categoria em si |
| A apuração não cruza arquivos nem colaboradores | Um contrato de entrada multi-arquivo e definição de qual limite é compartilhado |
| `coworking` não é reembolsável em nenhum centro de custo | Alteração da política, que hoje não prevê a categoria em `padrao` nem em nenhum centro de custo cadastrado |
| Fila de aprovação manual (item C do comunicado do Dia 2) não está implementada | Um estado novo (`AGUARDANDO_APROVACAO`), critérios de fila e de gestor responsável — deliberadamente fora de escopo nesta rodada (AMB-033) |
| Não há seleção automática de política histórica por vigência | Uma segunda política externa anterior, com sua própria janela de vigência, e uma regra de escolha entre elas quando mais de uma existir (RN-021 trata `vigencia` como informativa nesta versão, porque só uma política existe) |

**Perguntas que não puderam ser respondidas por ausência de interlocutor.** A política v3 foi escrita pelo RH e não houve canal para esclarecimento; o mesmo vale para a política v4. As leituras adotadas nas trinta e sete ambiguidades da seção 6 são decisões desta spec, não interpretações confirmadas pela área de origem. As que mais mudariam o resultado se a área decidisse diferente: AMB-002 (corte no teto contra recusa integral), AMB-008 (uma diária por lançamento) e AMB-004 (ausência de nota recusa o item inteiro), da linha de base; e, do envelope do Dia 2, AMB-020 (categoria ausente em centro de custo cadastrado), AMB-022 (limite igual a zero) e AMB-024 (data sem cotação exata) — as três com maior impacto financeiro observado nos arquivos de exemplo. `AMB-034` a `AMB-037`, acrescentadas nas correções pós-revisão, fecham contratos de execução, de arquivo externo e o vocabulário de motivos de teto individual em vez de resolver ambiguidade de leitura de texto — têm decisão técnica, mas não dependem de interlocutor do RH.

---

## 12. Resultados esperados — Envelope do Dia 2 (política v4)

Esta seção documenta, de forma normativa e verificável sem leitura de código, os quatro cenários financeiros completos do envelope do Dia 2 — a contraparte da `§4.7` para a política v4. Todos os valores abaixo foram calculados manualmente a partir das regras desta spec (RN-019 a RN-022 e as regras da linha de base atualizadas), não gerados por execução do motor, que ainda não implementa esta versão.

### 12.1 Baseline histórica (política padrão)

`exemplos/despesas-exemplo.json`, processado com uma política externa de teste equivalente aos limites históricos (`padrao`: alimentação R$60/dia, transporte R$80/dia, hospedagem R$250/diária; gatilho de nota R$100) — mesmos valores da `§4.7`:

```text
total_reembolsavel = 585.43
```

Essa baseline continua existindo como regressão histórica (CA-037) e não depende de nenhum centro de custo cadastrado em `politica-v4.json`.

### 12.2 `exemplos/despesas-exemplo.json` sob a política oficial v4

O mesmo arquivo, com `politica-v4.json` e o centro de custo real do colaborador (`CC-ENG-PLATAFORMA`, cadastrado na tabela: `alimentacao` R$75/dia, `transporte_urbano` R$80/dia, `hospedagem` R$0,00 — não reembolsável):

```text
total_reembolsavel = 351.43
```

Mudanças obrigatórias em relação a `12.1`, todas decorrentes de RN-019:

| Item | Categoria | `12.1` (padrão) | `12.2` (v4, `CC-ENG-PLATAFORMA`) | Motivo da mudança |
|---|---|---|---|---|
| `d-001` | alimentacao | `60,00` (parcial) | **`72,50`** (integral) | limite diário `60` → `75` |
| `d-002` | alimentacao | `0,00` (esgotado) | **`2,50`** (parcial) | saldo residual do dia sobe |
| `d-010` | hospedagem | `250,00` (parcial) | **`0,00`** (recusado, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`) | limite hospedagem `250` → `0` |
| `d-014` | alimentacao | `60,00` (parcial) | **`61,00`** (integral) | limite diário `60` → `75` |

Os demais dez itens não mudam de decisão nem de valor entre os dois cenários. A diferença de R$234,00 é consequência direta e correta da política v4 aplicada ao centro de custo real do colaborador — não é regressão de código (AMB-032, CA-038).

### 12.3 Rafael Nkemelu — `CC-COMERCIAL` (`despesas-envelope.json`)

Política aplicável: `alimentacao` R$90/dia, `transporte_urbano` R$150/dia, `hospedagem` R$400/diária, `representacao` R$300/dia (todas específicas de `CC-COMERCIAL`); gatilho de nota R$100 (nível política, único para toda a execução).

```text
total_reembolsavel = 1143.26
```

| Item | Categoria | Moeda / valor | Convertido (BRL) | Decisão | Reembolsável |
|---|---|---|---|---|---|
| `e-001` | representacao | BRL `340,00` | — | parcial (teto `300`) | `300,00` |
| `e-002` | alimentacao | EUR `22,00` @ `5,93` (`2026-07-14`) | `130,46` | parcial (teto `90`) | `90,00` |
| `e-003` | alimentacao | EUR `14,50` @ `5,88` (`2026-07-15`) | `85,26` | integral | `85,26` |
| `e-004` | alimentacao | EUR `30,00`, cotação de `2026-07-17` (última anterior a `2026-07-18`, sábado) | `178,80` | parcial (teto `90`) | `90,00` |
| `e-005` | transporte_urbano | USD `40,00` @ `5,50` (`2026-07-20`) | `220,00` | recusado (`NOTA_FISCAL_AUSENTE`, valor convertido > R$100 sem nota) | `0,00` |
| `e-006` | representacao | GBP `55,00`, sem cotação em `cambio.json` | — | recusado (`MOEDA_SEM_COTACAO`) | `0,00` |
| `e-007` | hospedagem | BRL `1200,00` | — | parcial (teto `400`) | `400,00` |
| `e-008` | alimentacao | BRL `95,00` | — | parcial (teto `90`) | `90,00` |
| `e-009` | coworking | BRL `120,00` | — | recusado (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — `CC-COMERCIAL` é cadastrado e não declara `coworking`; a tabela `padrao` não é consultada) | `0,00` |
| `e-010` | alimentacao | BRL `88,00`, `moeda` ausente → assume BRL | — | integral | `88,00` |

### 12.4 Dani Okonkwo — centro de custo desconhecido (`despesas-envelope-cc-desconhecido.json`)

`colaborador.centro_custo = "CC-SUPORTE-N2"`, fora da tabela de `politica-v4.json` → usa `padrao` integralmente (RN-019): `alimentacao` R$60/dia, `transporte_urbano` R$80/dia, `hospedagem` R$250/diária.

```text
total_reembolsavel = 373.76
```

| Item | Categoria | Moeda / valor | Convertido (BRL) | Decisão | Reembolsável |
|---|---|---|---|---|---|
| `f-001` | alimentacao | BRL `58,00`, `moeda` ausente → assume BRL | — | integral | `58,00` |
| `f-002` | hospedagem | BRL `310,00` | — | parcial (teto `250`, padrão) | `250,00` |
| `f-003` | representacao | BRL `190,00` | — | recusado (`CATEGORIA_FORA_POLITICA` — `padrao` não declara `representacao`) | `0,00` |
| `f-004` | transporte_urbano | USD `12,00` @ `5,48` (`2026-07-21`) | `65,76` | integral | `65,76` |

### 12.5 Como usar esta seção

Os quatro cenários acima são a fonte normativa de `CA-024` a `CA-040`; o contrato de execução, os contratos estruturais dos arquivos externos e o vocabulário de motivos de teto (`CA-041` a `CA-049`) são verificáveis independentemente de qualquer um desses quatro cenários financeiros. Divergência entre uma implementação e os valores desta seção é bug de código, não ambiguidade de spec — as ambiguidades relevantes já foram decididas em `AMB-019` a `AMB-037` e as regras correspondentes (`RN-019` a `RN-022`, mais `RN-002`, `RN-004`, `RN-005`, `RN-007`, `RN-009`, `RN-010`, `RN-011` a `RN-015` e `RN-017` atualizadas). A tabela de `e-009` (12.3) usa `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITICA` — correção da mesma revisão que fechou RN-007/RN-019 (`coworking` está ausente da tabela de `CC-COMERCIAL`, que é a única tabela aplicável a um centro cadastrado; `padrao` nunca é consultada). Fixtures de teste automatizado para estes cenários são trabalho de `tasks.md`, ainda não criado para esta versão.
