# Plano Técnico — Motor de Cálculo de Reembolso

**Versão:** 1.1 · **Status:** aprovado · **Baseado na spec:** 1.2 (aprovado) · **Última alteração:** 2026-08-05

> Aqui mora o COMO. Este arquivo pode e deve falar de linguagem, biblioteca e
> arquitetura. O que ele **não** pode é introduzir regra de negócio nova — se
> apareceu uma, ela pertence à `spec.md`.

---

## 0. O que mudou nesta versão (Dia 2 — política v4)

Esta revisão incorpora, do lado do COMO, a mudança de requisito já normatizada em `spec.md` 1.2 e registrada em `DECISIONS.md` (D-003): política de reembolso externa e variável por centro de custo (RN-019), câmbio e conversão monetária (RN-020), vigência de política (RN-021) e processabilidade dos arquivos externos (RN-022). Nenhuma regra de negócio é decidida aqui — cada RN citada já está fechada em `spec.md`; este arquivo só desenha os componentes, estruturas de dados e decisões técnicas que a materializam.

Nenhuma alteração de código foi feita nesta tarefa. Este arquivo é exclusivamente planejamento — a implementação real fica para as tasks a partir de `T-022` (`tasks.md`, não alterado aqui).

---

## 1. Stack

| Escolha | O quê | Por quê | O que descartei e por quê |
|---|---|---|---|
| Linguagem | Java 21 (LTS) | Familiaridade declarada de desenvolvedor back-end Java; JDK 21.0.2 já verificado funcionando nesta máquina sem instalação adicional; `java.math.BigDecimal` nativo resolve a exigência de aritmética decimal exata sem biblioteca externa. | Python e Node — nenhum dos dois está instalado nesta máquina (verificado via `python --version` e `node --version`); instalar agora consome prazo curto e adiciona risco de "funciona aqui, não funciona na correção". Kotlin — mesma JVM, mas introduziria sintaxe nova sob pressão de tempo sem ganho que a spec exija. |
| Build | Maven 3.9 ou superior | Maven 3.9.6 já verificado funcionando, casado ao JDK 21 instalado; convenção de projeto padrão para quem já é back-end Java. | Gradle — funcionalmente equivalente, mas sem motivo para trocar de ferramenta já dominada e já disponível. |
| Testes | JUnit 5 (Jupiter) | Parametrização nativa (`@ParameterizedTest`) essencial para as matrizes de fronteira monetária e de nota fiscal; integração direta com Maven Surefire, sem configuração extra. | TestNG — capacidade equivalente, sem motivo para introduzir dependência adicional. |
| Parsing/validação | Jackson Databind, lido via árvore `JsonNode` na camada de entrada | `JsonNode` permite inspecionar o tipo JSON bruto de cada campo (necessário para distinguir `CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` conforme RN-002) e preservar `valor_informado` exatamente como recebido. A partir da política v4, a mesma técnica de árvore é reaproveitada para os dois arquivos externos (`--politica`, `--cambio`) e para distinguir, no 7º campo `despesa.moeda`, a ausência da chave do valor `null` explícito (RN-002, RN-020 — ver §8). | Gson/`org.json` — mesma configuração adicional exigida, menos familiares. Parser escrito à mão — risco desnecessário sob prazo curto. |
| Aritmética monetária | `java.math.BigDecimal`, construído sempre a partir de texto/`decimalValue()`, nunca de `double` | É exatamente o mecanismo que garante `100.005 → 100,01` (RN-004) e, a partir da v4, também garante que a multiplicação `valor bruto × taxa` (RN-020) seja decimal-exata antes do único arredondamento. | `double`/`float` — fonte de bug documentada. Bibliotecas de dinheiro de terceiros — desnecessárias. |
| Empacotamento | Maven Shade Plugin, produzindo um único JAR executável | Gera `target/motor-reembolso.jar` autocontido, executável só com `java -jar`. | `maven-assembly-plugin` — equivalente, Shade mais direto. Spring Boot — desnecessário. Wrapper `.sh`/`.bat` — descartado por decisão explícita. |

---

## 2. Arquitetura

A arquitetura é um pipeline linear que segue **literalmente** os treze passos da seção 8.1 da spec 1.2 (eram onze na spec 1.1 — os dois passos novos são a validação dos arquivos externos, à frente de tudo, e a resolução de câmbio, entre a detecção de ID duplicado e a normalização). Cada passo é um estágio que recebe a lista de itens (na ordem da entrada) e devolve a mesma lista enriquecida — nenhum estágio reordena a lista mestra.

```
--politica          --cambio              entrada JSON (--input)
   │                    │                        │
   ▼                    ▼                        ▼
[Leitor+Validador   [Leitor+Validador       [Leitor] → JsonNode
 de Política]        de Câmbio]                   │
   │                    │                         ▼
   │                    │            [2] Validador de envelope (RN-001) — fatal se falhar
   │                    │                         │
   ▼                    ▼                         ▼
PoliticaExterna    TabelaCambio          [3] Validador de item / classificador estrutural
(imutável)         (imutável)                (RN-002, incluindo despesa.moeda — 7º campo)
   │                    │                         │
   │                    │                         ▼
   │                    │            [4] Detector de despesa.id duplicado (RN-003)
   │                    │                         │
   │                    └────────────►[5] Resolutor de câmbio / conversão para BRL (RN-020)
   │                                              │
   ▼                                              ▼
   └───────────────────────────────►[6] Normalizador (RN-004 valor já convertido · RN-005 categoria)
                                                   │
                                                   ▼
                          [7] Avaliador de regras individuais, incluindo política do
                              centro de custo (RN-006 · RN-007 · RN-008 · RN-009 · RN-019)
                                                   │
                                                   ▼
                          [8] Seletor de itens aprovados em todas as validações individuais
                                                   │
                                                   ▼
                          [9] Detector de duplicidade econômica (RN-010, chave com moeda)
                                                   │
                                                   ▼
                          [10] Seletor de itens elegíveis após a duplicidade
                                                   │
                                                   ▼
                          [11] Agregador de tetos por periodicidade (RN-011 a RN-015, RN-019)
                                                   │
                                                   ▼
                          [12] Compositor de saída — decisão final + motivos (8.3),
                               incluindo moeda/taxa_cambio_aplicada/data_cotacao_utilizada
                                                   │
                                                   ▼
                          [13] Somador do total (RN-018)
                                                   │
                                                   ▼
                                          [Escritor] → JSON de saída
```

O passo `[1]` (validar política e câmbio, RN-021/RN-022) acontece **antes** do passo `[2]` (validar envelope) — é a ordem normativa de `spec.md` §8.1. Uma falha no passo `[1]` é código de saída `2` e nem chega a abrir o arquivo de despesas para validação de envelope.

**Como a avaliação de regras funciona (sem mudança de princípio, só de escopo):**

- Continua valendo integralmente o texto da versão anterior deste plano: cada regra roda quando os campos de que depende estão válidos (matriz 8.2), motivos são acumulados sem descarte por etapas anteriores, e só as exclusões fechadas de 8.4 interrompem etapas posteriores.
- **Nova exclusão relevante (8.4, item 14):** `MOEDA_SEM_COTACAO` deixa `valor_normalizado` nulo e por isso bloqueia RN-006, RN-009, RN-010 e qualquer teto (RN-011 a RN-015, RN-019) — mesmo tratamento de dependência que um campo estruturalmente inválido já recebia, agora por ausência de dado externo. RN-007/RN-019 (categoria) e RN-008 (competência) **não** dependem de `valor_normalizado` e continuam avaliadas normalmente. Um item pode, portanto, sair com `MOEDA_SEM_COTACAO` **e** `CATEGORIA_FORA_POLITICA`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`FORA_COMPETENCIA` simultaneamente, mas nunca com `VALOR_NAO_POSITIVO`, `NOTA_FISCAL_AUSENTE`, `DUPLICIDADE` ou motivo de teto.
- A **ordem de processamento** (8.1) e a **ordem de apresentação dos motivos** (8.3) continuam sendo duas ordens distintas, com o mesmo mecanismo de acumulador + reordenação isolada no passo de composição.

**Fronteiras (sem mudança de princípio):** núcleo puro (passos 2 a 13, sem I/O) e I/O isolado nas pontas (leitores de política/câmbio/despesas, escritor, CLI/Main). O CLI/Main orquestra: valida arquivos externos, invoca o núcleo, escreve o resultado, traduz falha em código de saída.

---

## 3. Contrato da CLI

```
java -jar target/motor-reembolso.jar calcular --input <entrada.json> --output <saida.json> --politica <politica.json> --cambio <cambio.json>
```

As quatro flags são **normativas de `spec.md` §4.1.1** (AMB-034) — este plano só descreve como a CLI as implementa, não redefine o contrato.

**Contrato do subcomando (fechado, sem alternativa em aberto):**

- O **primeiro token** da linha de comando deve ser exatamente `calcular`. `calcular` é o **único token posicional** permitido em toda a linha de comando — todo token seguinte é obrigatoriamente uma flag ou o valor de uma flag, nunca outro posicional.
- Subcomando ausente (nenhum argumento), diferente de `calcular` (ex.: `computar`), ou qualquer token posicional extra (ex.: `calcular extra --input ...`) → exit `2`.
- **Depois de `calcular`**, os argumentos restantes são consumidos estritamente aos pares `flag valor` — nunca uma flag "solta" seguida de outra flag.
- **Flag sem valor** (a flag é o último token da linha, ou o próximo token também começa com `--`) → exit `2`.
- **Quantidade ímpar de tokens depois do subcomando** → exit `2` (é a mesma violação que "flag sem valor", vista pelo total: um número ímpar de tokens não fecha em pares completos).
- **Ordem:** qualquer, entre as quatro flags. **Repetição:** cada uma das quatro flags aparece **exatamente uma vez**. **Ausência:** as quatro são obrigatórias.
- **Parsing:** o parser de argumentos deixa de ser um `switch` com dois casos fixos e passa a acumular os pares reconhecidos num mapa (`Map<String, String>`), contando ocorrências por chave. Ao final: se alguma das quatro chaves obrigatórias está ausente do mapa, se alguma chave aparece mais de uma vez, ou se uma flag não reconhecida (`--xyz`) aparece na linha de comando → exit `2`, mensagem em stderr, nada em stdout, `--output` preexistente preservado.
- **O comando anterior à política v4** — só `calcular --input <e> --output <s>` — retorna exit `2`: faltam `--politica` e `--cambio`, mesma classe de erro de flag ausente. Isso vale mesmo quando toda despesa da entrada é BRL: `--cambio` é obrigatório independentemente do conteúdo do arquivo de despesas — não há isenção por conteúdo.
- **Política e câmbio inválidos:** ausência do arquivo, ilegibilidade, JSON sintaticamente inválido, ou qualquer violação do contrato estrutural fechado de `spec.md` §4.1.1 (política ou câmbio) → exit `2` — mesma classe de gravidade que argumento ausente/repetido/desconhecido e que `--input` ilegível. Nenhum código novo é criado para esse caso.
- **Envelope de despesas inválido** (RN-001) continua exit `3`.
- **Sucesso** continua exit `0`. Stdout permanece vazio em qualquer cenário; mensagens de erro vão para stderr.

**Códigos de saída (tabela atualizada):**

| Código | Significado |
|---|---|
| `0` | Processamento concluído e arquivo de resultado escrito em `--output`. |
| `2` | Erro de uso ou de infraestrutura: flag ausente/repetida/desconhecida; arquivo de `--input`, `--politica` ou `--cambio` inexistente, ilegível, sintaticamente inválido, ou (para política/câmbio) estruturalmente inválido conforme `spec.md` §4.1.1; falha ao escrever `--output`. |
| `3` | JSON sintaticamente legível, mas envelope de despesas inválido conforme RN-001. |

Nenhum código além de `0`, `2` e `3` é criado nesta versão (confirma DT-003/AMB-034).

**Ordem de validação e escrita segura (extensão de DT-010):** o processo valida política e câmbio primeiro (passo 1 de 8.1); só então lê e valida o envelope de despesas (passo 2); só então processa o núcleo inteiro. O arquivo de destino (`--output`) só é criado ou substituído **depois que toda a apuração e serialização terminaram com sucesso** — a estratégia de arquivo temporário no mesmo diretório do destino, seguida de `Files.move` com `ATOMIC_MOVE`/`REPLACE_EXISTING` (DT-010), é preservada sem alteração e passa a cobrir também as falhas de política/câmbio: qualquer uma delas retorna antes de o processo sequer tentar abrir um temporário, então um `--output` preexistente nunca é tocado.

---

## 4. Modelo de dados

Estruturas internas do núcleo, estendidas em relação à v1.0 deste plano (descrição de responsabilidade, não implementação):

| Estrutura | Conteúdo |
|---|---|
| **Item de entrada** | Igual à v1.0: `indiceEntrada` + `raw` (fotografia de auditoria). |
| **Campos estruturalmente validados** | Passa a ter **oito** campos candidatos em vez de sete: os sete já existentes, mais `moeda` — mas `moeda` é populado de forma diferente dos demais (ver §8, "Campo `despesa.moeda`"): resolve para `"BRL"` quando a chave está ausente, sem produzir motivo; fica ausente (nulo) quando a chave existe e é estruturalmente inválida (mesma regra dos demais campos nesse caso). |
| **`valor_informado`** | Sem mudança — continua o valor JSON bruto de `despesa.valor`. |
| **`ItemValidado` enriquecido pelo `ResolutorCambio` (passo 5 de 8.1)** | Decisão fechada, com responsabilidade dividida entre dois estágios (ver §9 para o detalhamento completo): `ValidadorItem` (passo 3) já populou `ItemValidado.moeda` — `"BRL"` quando a chave está ausente, o texto validado quando a moeda estrangeira é estruturalmente válida, ou nulo com motivo estrutural quando inválida (§8). `ResolutorCambio` (passo 5) **não repopula `moeda`** — consome o valor já resolvido e **estende `ItemValidado`** com apenas três campos novos: `taxaCambioAplicada` (`1` para BRL, a taxa resolvida para estrangeira, nulo se `MOEDA_SEM_COTACAO`), `dataCotacaoUtilizada` (nulo para BRL, a data efetivamente usada para estrangeira, nulo se sem cotação) e `valorConvertidoBruto` (o produto `valor × taxa`, **ainda sem arredondamento** — o arredondamento é RN-004, que acontece no passo seguinte). Nenhuma estrutura intermediária alternativa (um "item com câmbio" à parte de `ItemValidado`) é criada nesta versão do plano — ver §19. Quando não há cotação, o próprio `ResolutorCambio` grava o motivo `MOEDA_SEM_COTACAO` com `campo = CampoCanonico.MOEDA` (§10). |
| **`valor_normalizado`** | Continua `BigDecimal` de escala 2, mas agora resultado de RN-004 aplicado sobre `valorConvertidoBruto` (BRL: o próprio valor original, já que a taxa é `1`) — um único arredondamento, nunca dois. Nulo quando `despesa.valor` é estruturalmente inválido **ou** quando há `MOEDA_SEM_COTACAO`. |
| **Categoria normalizada** | Sem mudança de mecanismo (RN-005) — mas a partir de RN-019 é comparada contra a tabela de política **efetivamente aplicável** (resolvida por centro de custo), não contra um conjunto fixo de três nomes. |
| **`TabelaPoliticaResolvida` (nova, `modelo/TabelaPoliticaResolvida.java`)** | Devolvida por `ResolutorPoliticaCentroCusto.resolver(String, PoliticaExterna)` (§6): `categorias` (mapa imutável categoria → `TabelaCategoria`, a única tabela selecionada), `origem` (enum interno fechado `PADRAO`/`CENTRO_CUSTO`), `nomeCentroCusto` (preenchido só quando `origem == CENTRO_CUSTO`). `AvaliadorRegrasIndividuais` consulta `categorias` diretamente para decidir `CATEGORIA_FORA_POLITICA`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/limite e periodicidade (RN-019) — nenhum método de consulta adicional é criado no resolutor. |
| **Acumulador de motivos** | Sem mudança de mecanismo — só o vocabulário de `MotivoCodigo` cresce (§10). |
| **`Motivo`** | Mesmos três campos (`codigo`, `regra`, `campo`), mas os três enums fechados que os representam ganham valores novos: `MotivoCodigo` ganha `MOEDA_SEM_COTACAO`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, `TETO_INDIVIDUAL_APLICADO` (dezesseis valores no total); `RegraNegocio` ganha `RN_019` a `RN_022`; `CampoCanonico` ganha `MOEDA`, inserido na posição correta da ordem canônica de contrato — entre `VALOR` e `TEM_NOTA_FISCAL` (oito valores no total, mesma técnica de serialização num único ponto — DT-008/DT-019). |
| **Resultado por item** | Ganha três campos novos entre `valor_informado` e `valor_normalizado`: `moeda`, `taxaCambioAplicada`, `dataCotacaoUtilizada` — os mesmos três valores gravados em `ItemValidado` pelo `ResolutorCambio`, propagados sem recálculo até a composição final. |
| **Resultado geral** | Sem mudança de forma — `colaborador`, `periodo`, `resultados`, `total_reembolsavel`. |

---

## 5. Arquivo externo: Política de reembolso

**Responsabilidade:** um componente de leitura (`LeitorPolitica`, no pacote `leitor`, ao lado de `ValidadorEnvelope`) com uma **API pública única e fechada**:

```java
LeitorPolitica.ler(Path caminho): PoliticaExterna
```

Não existe uma segunda forma pública de invocar o leitor (ex.: passando `JsonNode` diretamente de fora) — `ler(Path)` é o único ponto de entrada, e é responsável, nesta ordem, por:

1. abrir o arquivo no caminho recebido;
2. fazer parsing sintático com Jackson (`JsonNode`, mesma técnica de DT-005);
3. validar o contrato estrutural completo de `spec.md` §4.1.1 (lista exaustiva abaixo);
4. construir o modelo imutável `PoliticaExterna`;
5. lançar uma exceção dedicada (`PoliticaInvalidaException`) em qualquer falha de qualquer um dos passos 1 a 3 — arquivo inexistente, ilegível, JSON sintaticamente inválido, ou violação de qualquer regra estrutural.

Métodos privados internos do leitor podem receber `JsonNode` (é assim que o parsing é feito passo a passo), mas isso é detalhe de implementação — não é a API pública planejada. `Main` chama exclusivamente `LeitorPolitica.ler(caminhoDaFlagPolitica)` e traduz qualquer `PoliticaInvalidaException` em exit `2`.

Não existe caminho pelo qual uma política parcialmente válida alcance o núcleo: a validação é tudo-ou-nada, como a de `ValidadorEnvelope` para o envelope de despesas, mas com gravidade maior (RN-022 é mais grave que RN-001 — nem o envelope chega a ser lido se política ou câmbio falharem).

**Modelo (`PoliticaExterna`, imutável):**

```
PoliticaExterna {
  vigencia: LocalDate                              // RN-021, validada e preservada como metadado informativo
  moedaBase: String                                // sempre "BRL" após validação
  notaFiscalObrigatoriaAcimaDe: BigDecimal          // RN-009, dado do arquivo — não mais constante de código
  padrao: Map<String, TabelaCategoria>              // pode ser vazio
  centrosCusto: Map<String, Map<String, TabelaCategoria>>  // pode ser vazio; cada valor é uma tabela completa e exclusiva
}

TabelaCategoria {
  limite: BigDecimal
  periodicidade: Periodicidade   // enum fechado: DIA, DIARIA — nunca outro valor chega aqui
}
```

`versao` e `acrescimo_em_viagem_percentual` (e qualquer outro campo desconhecido) são lidos pelo `JsonNode` bruto só para efeito de "ignorar sem erro" — não entram no modelo `PoliticaExterna`, porque nenhuma regra os consome (RN-016 continua sem efeito; `acrescimo_em_viagem_percentual` nunca ativa comportamento).

**Lista explícita e exaustiva de validações que o `LeitorPolitica` aplica antes de devolver o modelo (RN-022, AMB-035, CA-045) — qualquer uma falhando lança `PoliticaInvalidaException`:**

1. A raiz do documento é obrigatoriamente um objeto.
2. `vigencia` é obrigatória, texto no formato `AAAA-MM-DD`, representando uma data real do calendário (RN-021).
3. `moeda_base` é obrigatória e é exatamente `"BRL"`.
4. `nota_fiscal_obrigatoria_acima_de` é obrigatória, numérica, e não negativa.
5. `padrao` é obrigatório e é um objeto (pode ser vazio).
6. `centros_custo` é obrigatório e é um objeto (pode ser vazio).
7. Cada valor dentro de `centros_custo` (uma tabela por centro) é, ele próprio, um objeto.
8. Dentro de `padrao` e de cada tabela de `centros_custo`, todo nome de categoria é uma chave de texto não vazia.
9. A configuração de cada categoria é um objeto (não um número, texto ou lista solta).
10. `limite` é obrigatório dentro de cada configuração de categoria, e é numérico.
11. Dentro de `padrao`: todo `limite` é **estritamente maior que zero** — `limite: 0` em `padrao` é falha estrutural do arquivo inteiro (não chega a produzir um `TabelaCategoria`, porque o arquivo inteiro é rejeitado antes).
12. Dentro de qualquer tabela de `centros_custo`: `limite` maior ou igual a zero é estruturalmente válido (o `0,00` vira uma decisão de negócio — `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — não um defeito de arquivo).
13. `periodicidade` é obrigatória dentro de cada configuração de categoria, e é exatamente `"dia"` ou `"diaria"` — qualquer outro texto (ou tipo) invalida o **arquivo** inteiro, não só a categoria.
14. `observacao`, dentro de uma configuração de categoria, é um campo **conhecido** (não desconhecido) e **opcional**: quando ausente, é válido; quando presente, deve ser texto; quando presente com qualquer outro tipo (número, booleano, lista, objeto), o arquivo de política é estruturalmente inválido. O valor, quando válido, é lido e **descartado** — não entra no modelo `TabelaCategoria` (§5) nem participa de regra alguma (spec 4.1.1: "informativa; nunca lida por regra alguma").
15. Campos **verdadeiramente desconhecidos** — não previstos pelo contrato — continuam ignorados sem afetar a validade: na raiz (`versao`, `acrescimo_em_viagem_percentual`, aceitos como metadados de qualquer tipo), ou dentro de uma configuração de categoria (qualquer chave além de `limite`/`periodicidade`/`observacao`). `observacao` (item 14) não é um desses campos — é conhecido e validado quanto ao tipo.
16. O modelo `PoliticaExterna` resultante é construído com cópias defensivas imutáveis dos mapas (`Map.copyOf` ou equivalente) — nenhuma referência ao `JsonNode` de origem, nem ao mapa mutável intermediário usado durante a validação, escapa para fora do leitor.

Todo o arquivo é validado — os dezesseis pontos acima — **antes** de qualquer `TabelaCategoria` ser construída; não existe um caminho onde parte do modelo já foi montada quando uma violação tardia é detectada.

Não existem mais as constantes `60`, `80`, `250`, `100` no código de produção depois desta mudança: `PoliticaExterna` é o único lugar de onde valores financeiros de política se originam, e ela sempre vem de um arquivo (nunca de um literal fixo). A política histórica equivalente (usada na regressão de `§12.1`/`§12.2`) é uma **fixture externa de teste** — um JSON no mesmo formato, versionado em `tests/resources/` — nunca um valor hardcoded nem um fallback interno de produção (ver §16).

---

## 6. Política por centro de custo

**Responsabilidade e API pública fechada:**

```java
ResolutorPoliticaCentroCusto.resolver(
    String centroCusto,
    PoliticaExterna politica
): TabelaPoliticaResolvida
```

Decisão fechada, sem alternativa de projeto em aberto: `resolver(...)` é um método estático (ou de instância de um resolutor sem estado) que devolve diretamente o modelo imutável `modelo/TabelaPoliticaResolvida.java`:

```java
TabelaPoliticaResolvida {
  categorias: Map<String, TabelaCategoria>   // imutável — a única tabela selecionada, nunca a união de duas
  origem: Origem                              // enum interno fechado: PADRAO, CENTRO_CUSTO
  nomeCentroCusto: String                     // preenchido somente quando origem == CENTRO_CUSTO; nulo quando origem == PADRAO
}
```

**Resolução da entrada `String centroCusto`** — o parâmetro pode ser `null`:

- `null` representa, de forma unificada, os três casos que o envelope já tolera antes de chegar aqui (RN-001/RN-019): ausência do bloco `colaborador`, `colaborador.centro_custo` ausente ou nulo, ou de tipo inválido — a camada que lê o envelope (`Envelope`/`ValidadorEnvelope`) já reduz todos esses casos a `null` antes de chamar `resolver`.
- **Texto reconhecido** (presente como chave em `politica.centrosCusto`) → seleciona a tabela exclusiva daquele centro: `origem = CENTRO_CUSTO`, `nomeCentroCusto` preenchido, `categorias` = a tabela exclusiva do centro.
- **Texto desconhecido, ou `centroCusto == null`** → seleciona `padrao`: `origem = PADRAO`, `nomeCentroCusto = null`, `categorias` = `politica.padrao`.
- **Comparação:** `String.equals` puro contra as chaves de `centrosCusto` — sem `trim()`, sem `toLowerCase()`, sem normalização de acento, sem correspondência aproximada (RN-019, DT-016). Decisão deliberada de não reaproveitar a normalização de categoria (RN-005): os dois campos têm regras de comparação diferentes por design da spec.
- **Nunca mistura:** `categorias` é sempre um único `Map<String, TabelaCategoria>` — nunca a união de `padrao` com a tabela de um centro. Categorias ausentes da tabela de um centro cadastrado **não** caem de volta em `padrao` (RN-019, RN-007).

**Consumo pelo `AvaliadorRegrasIndividuais`** — o avaliador consulta diretamente `tabelaResolvida.categorias` (sem um método de consulta adicional no resolutor) e aplica a régua fechada de RN-019:

1. Categoria ausente do mapa + `origem == PADRAO` → `CATEGORIA_FORA_POLITICA`.
2. Categoria ausente do mapa + `origem == CENTRO_CUSTO` → `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`.
3. Categoria presente com `limite == 0` (só ocorre com `origem == CENTRO_CUSTO`, porque `padrao` com limite zero já foi rejeitado na leitura do arquivo, §5) → `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`.
4. Categoria presente com `limite > 0` → usa `limite` e `periodicidade` dessa entrada, para a elegibilidade individual (RN-019/RN-007) e para o agregador de tetos (§11).

---

## 7. Arquivo externo: Câmbio

**Responsabilidade:** um componente de leitura (`LeitorCambio`, ao lado de `LeitorPolitica`) com a mesma forma de API pública única e fechada:

```java
LeitorCambio.ler(Path caminho): TabelaCambio
```

As mesmas cinco responsabilidades de `LeitorPolitica` (§5) se aplicam aqui: abrir o arquivo, fazer parsing sintático, validar o contrato estrutural completo, construir o modelo imutável, lançar `CambioInvalidoException` em qualquer falha. Métodos privados internos podem trabalhar com `JsonNode`; a API pública é só `ler(Path)`. Mesma política de tudo-ou-nada e mesma tradução para exit `2` em caso de falha.

**Estrutura real do arquivo** (raiz com `moeda_base`, `fonte`, `observacao`, `taxas` aninhado por data e depois por moeda) é lida e **invertida** para uma estrutura de consulta eficiente:

```
TabelaCambio {
  moedaBase: String                                       // sempre "BRL"
  cotacoesPorMoeda: Map<String, NavigableMap<LocalDate, BigDecimal>>
}
```

A inversão (de "data → moeda → taxa" para "moeda → data → taxa", com a submapa ordenada por data) é o que torna a consulta determinística e eficiente:

1. **Cotação exata na data da despesa:** `NavigableMap.get(data)`.
2. **Ausência de cotação exata:** `NavigableMap.floorEntry(data)` — a entrada de chave igual ou, na ausência, imediatamente **anterior**. `floorEntry` nunca devolve uma data posterior, então a garantia "nunca cotação futura" é estrutural (decorre do próprio método escolhido), não de uma checagem manual que poderia ser esquecida.
3. **Sem interpolação:** o resolutor não faz nenhuma média nem cálculo entre duas cotações — devolve exatamente o valor de `floorEntry`, ou "sem cotação" quando o resultado é nulo (moeda nunca apareceu em `taxas`, ou a primeira cotação disponível é posterior à data da despesa).
4. **Moeda ausente de `cotacoesPorMoeda`:** resultado "sem cotação" (`MOEDA_SEM_COTACAO`), o mesmo caminho do item 3.

**Lista explícita e exaustiva de validações que o `LeitorCambio` aplica (RN-022, AMB-035, CA-046) — qualquer uma falhando lança `CambioInvalidoException`:**

1. A raiz do documento é obrigatoriamente um objeto.
2. `moeda_base` é obrigatória e é exatamente `"BRL"`.
3. `taxas` é obrigatório e é um objeto — pode ser `{}` (válido: recusa despesas estrangeiras item a item via `MOEDA_SEM_COTACAO`, não invalida o arquivo).
4. Cada chave de primeiro nível dentro de `taxas` é uma data no formato `AAAA-MM-DD`, representando uma data real do calendário.
5. O valor associado a cada data é, ele próprio, um objeto (mapa de moeda para taxa).
6. Cada chave dentro de uma data casa com o padrão `[A-Z]{3}`.
7. Cada taxa é numérica e estritamente positiva (zero e negativos são estruturalmente inválidos).
8. `fonte` e `observacao`, na raiz, são campos **conhecidos** e **opcionais**: quando ausentes, o arquivo é válido; quando presentes, devem ser texto; presentes com qualquer outro tipo invalida o arquivo estruturalmente. Os valores, quando válidos, são lidos e **descartados** — nunca usados por regra alguma.
9. Campos verdadeiramente desconhecidos são ignorados sem afetar a validade **apenas na raiz** de `cambio.json` (um campo extra ao lado de `moeda_base`/`fonte`/`observacao`/`taxas`). Essa tolerância **não** se estende aos dois mapas internos de `taxas`: toda chave de primeiro nível dentro de `taxas` deve ser uma data ISO real (item 4) e toda chave dentro do objeto de uma data deve ser uma moeda `[A-Z]{3}` (item 6) — nesses dois níveis, uma chave que não satisfaça o formato exigido **não é um "campo desconhecido"**: é violação estrutural, e invalida o arquivo inteiro, exatamente como qualquer outra violação desta lista.
10. O modelo `TabelaCambio` resultante — já invertido para `moeda → NavigableMap<data, taxa>` (§7, DT-013) — é construído com cópias defensivas imutáveis; nenhum `Map`/`NavigableMap` mutável escapa do leitor.

---

## 8. Campo `despesa.moeda`: representação e estratégia de parsing

Este é o único campo cujo contrato de ausência-de-chave difere de todos os outros sete — e a técnica de parsing já em uso (DT-005, `JsonNode`) já distingue exatamente os dois casos que a spec exige distinguir, sem exigir nenhuma técnica nova:

- `elemento.get("moeda")` devolve a referência Java `null` quando a **chave não existe no objeto**.
- `elemento.get("moeda")` devolve uma instância de `NullNode` (`.isNull() == true`) quando a **chave existe com valor JSON `null`**.
- `elemento.path("moeda")` também distinguiria os dois casos, por um caminho equivalente: devolveria `MissingNode` (`.isMissingNode() == true`) para chave ausente e `NullNode` (`.isNull() == true`) para `null` explícito — `path()` **não** colapsa os dois casos em um só; ele só evita lançar exceção ao encadear acessos em profundidade, o que não é o problema deste campo.
- **Por que `get()` e não `path()`:** `get()` é escolhido por coerência com a camada já existente (os outros sete campos já usam `elemento.get(chave)`, ver `ValidadorItem`) e porque `get()` torna o ramo de "chave ausente" explícito e imediato — comparar contra a referência `null` — **antes** de acessar qualquer propriedade do nó, em vez de exigir uma chamada adicional (`isMissingNode()`) sobre um nó que `path()` já teria produzido. A escolha é estilística/de coerência de camada, não uma diferença de capacidade entre os dois métodos.
- Acessores permissivos com valor padrão (ex.: `elemento.path("moeda").asText("BRL")`) continuam proibidos: eles calculariam `"BRL"` tanto para chave ausente quanto para qualquer outro caso que não produza texto, ocultando exatamente a distinção que este campo exige preservar.

**Estratégia de validação (`validarMoeda`, mesma classe/pacote de `ValidadorItem`, mas com um ramo extra no topo que os demais campos não têm):**

```
valor = elemento.get("moeda")
se valor == null (chave ausente):           → devolve "BRL", nenhum motivo
senão se valor.isNull() (chave = null):     → CAMPO_AUSENTE, campo = despesa.moeda
senão se tipo != STRING:                    → CAMPO_TIPO_INVALIDO, campo = despesa.moeda
senão se texto não casa com [A-Z]{3}:       → CAMPO_FORMATO_INVALIDO, campo = despesa.moeda
                                               (sem trim, sem conversão de caixa — RN-002)
senão:                                       → devolve o texto validado
```

Nenhuma desserialização direta para POJO tipado é usada aqui — o mesmo motivo já registrado em DT-005: um acessor permissivo perderia justamente a distinção entre "chave ausente" e "chave presente com `null`" que este campo exige preservar. A representação intermediária (o retorno de `validarMoeda`) é sempre uma `String` ou `null` — o "silêncio" da ausência de chave e o "motivo" da presença de `null` são decisões tomadas **antes** desse retorno, nunca depois, para que o restante do pipeline não precise saber por que o campo é nulo.

---

## 9. Conversão monetária

**Fórmula normativa (RN-020, RN-004):** `valor bruto × taxa`, seguido de **um único** arredondamento `HALF_UP` para duas casas — nunca dois arredondamentos (um na moeda original, outro após a conversão).

**Divisão de responsabilidade fechada entre três componentes — ordem definitiva, sem alternativa em aberto:**

### `ValidadorItem` (passo 3 de 8.1)

Popula `ItemValidado.moeda` (§8) — e **somente** `moeda`, nenhum dos três campos de câmbio:

- chave `moeda` ausente do objeto → `"BRL"`, sem motivo;
- chave presente com moeda estruturalmente válida → o texto validado (`"BRL"` ou a moeda estrangeira em `[A-Z]{3}`);
- chave presente e estruturalmente inválida (`null`, tipo errado, formato errado) → `moeda` fica nulo, com o motivo estrutural correspondente (`CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO`, `campo = CampoCanonico.MOEDA`).

### `ResolutorCambio` (`pipeline/ResolutorCambio.java`, estágio novo do pipeline, passo 5 de 8.1 — entre o detector de ID duplicado e o normalizador)

**Não popula `moeda` novamente** — consome o `ItemValidado.moeda` já resolvido por `ValidadorItem` como entrada. Popula **somente** três campos: `taxaCambioAplicada`, `dataCotacaoUtilizada`, `valorConvertidoBruto`.

O estágio é avaliado quando os três campos de que RN-020 depende estiverem estruturalmente utilizáveis: `despesa.valor`, `despesa.moeda`, `despesa.data`. Um erro estrutural num campo que RN-020 **não** usa — `descricao`, `fornecedor`, `categoria` ou `tem_nota_fiscal` — não impede a resolução cambial: o item pode ter, por exemplo, `categoria` inválida e ainda assim ter `taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto` calculados normalmente.

- **BRL** (`ItemValidado.moeda == "BRL"`, estruturalmente válido — inclusive quando a chave estava ausente e foi resolvida para `"BRL"` por `ValidadorItem`): `taxaCambioAplicada = 1`; `dataCotacaoUtilizada = null`; `valorConvertidoBruto = valor` original, sem multiplicação nem arredondamento — o mesmo valor, apenas copiado para o campo de saída deste estágio. Isso vale identicamente para BRL informado e para BRL assumido por ausência de chave; não há um caminho diferente para os dois.
- **Moeda estrangeira com cotação resolvida** (exata ou mais recente anterior, `TabelaCambio`, §7): `valorConvertidoBruto = valor original × taxa`, **sem arredondamento nesse estágio**; `taxaCambioAplicada` e `dataCotacaoUtilizada` preenchidos com os valores efetivamente usados.
- **Moeda estrangeira sem cotação:** os três campos derivados (`taxaCambioAplicada`, `dataCotacaoUtilizada`, `valorConvertidoBruto`) ficam nulos; `ResolutorCambio` grava o motivo `MOEDA_SEM_COTACAO` com `regra = RN_020` e `campo = CampoCanonico.MOEDA` (serializado `"despesa.moeda"`, §10).
- **`moeda` estruturalmente inválida** (`ItemValidado.moeda` já nulo, vindo de `ValidadorItem`) → item já recusado por RN-002; este estágio não tenta resolver câmbio para ele (mesma exclusão de dependência de campo inválido já existente).

### `Normalizador` (RN-004, estágio seguinte)

Usa **exclusivamente** `valorConvertidoBruto` — nunca `despesa.valor` bruto, nunca um caminho separado "ou o valor original, para BRL": `setScale(2, RoundingMode.HALF_UP)` é aplicado sobre `valorConvertidoBruto`, e para BRL isso já produz o resultado correto porque `ResolutorCambio` já copiou o valor original para `valorConvertidoBruto` no passo anterior. **O mesmo caminho de normalização serve para BRL e para moeda estrangeira** — não existe um `if` de BRL dentro do `Normalizador`. Nenhum outro ponto do pipeline arredonda.

**Saída:** os três campos de auditoria (`moeda`, `taxa_cambio_aplicada`, `data_cotacao_utilizada`) são propagados sem recálculo desde este estágio até a composição final (§4) e a serialização (§13). Não existe campo `valor_convertido` separado — `valor_normalizado` já é o valor final em BRL.

---

## 10. Motivos, decisões e ordem de apresentação

**Extensão dos vocabulários fechados (DT-008/DT-019, ver §14):**

- `MotivoCodigo` ganha `MOEDA_SEM_COTACAO`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, `TETO_INDIVIDUAL_APLICADO` — todos os treze códigos anteriores são preservados sem alteração de nome ou de serialização.
- `RegraNegocio` ganha `RN_019` a `RN_022`.
- `CampoCanonico` ganha `MOEDA`, inserida na posição correta da ordem canônica (entre `VALOR` e `TEM_NOTA_FISCAL`) — isso desloca `TEM_NOTA_FISCAL` de ordinal 6 para 7 na tabela de desempate de `CompositorSaida`, mas não afeta a posição relativa dos seis campos anteriores.

**Ordem de apresentação (8.3), tabela de estágios em `CompositorSaida` estendida:**

| Estágio | Motivo(s) |
|---|---|
| 0 | `ITEM_TIPO_INVALIDO` |
| 1 | Erros de campo estrutural, na ordem canônica (`id, data, categoria, descricao, fornecedor, valor, moeda, tem_nota_fiscal`) |
| 2 | `ID_DUPLICADO` |
| 3 | `MOEDA_SEM_COTACAO` (**novo**) |
| 4 | `VALOR_NAO_POSITIVO` |
| 5 | `CATEGORIA_FORA_POLITICA` |
| 6 | `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (**novo**) |
| 7 | `FORA_COMPETENCIA` |
| 8 | `NOTA_FISCAL_AUSENTE` |
| 9 | `DUPLICIDADE` |
| 10 | Motivos de teto: `TETO_DIARIO_APLICADO`, `TETO_DIARIO_ESGOTADO`, `TETO_HOSPEDAGEM_APLICADO`, `TETO_INDIVIDUAL_APLICADO` (**novo**) |

**Garantias de coerência (mecanismo já existente em `CompositorSaida`, apenas com tabela maior):**

- **Motivo associado à RN correta:** cada `Motivo` carrega sua própria `RegraNegocio` no ponto onde é criado — a tabela de estágios ordena por `codigo`, nunca precisa inferir a regra a partir do estágio. Para `TETO_DIARIO_APLICADO`, a `regra` efetivamente gravada no motivo difere por categoria (`RN_011` para `alimentacao`, `RN_012` para `transporte_urbano`, `RN_019` para qualquer outra categoria com `periodicidade: "dia"` — spec 4.5) — quem decide isso é o agregador de teto (§11), não o compositor.
- **`campo` correto — contrato completo, não só "estruturais e `ID_DUPLICADO`":**
  1. Erros estruturais de campo (estágio 1: `CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO`) carregam o `CampoCanonico` correspondente ao campo defeituoso.
  2. `ID_DUPLICADO` carrega `CampoCanonico.ID` (serializado `"despesa.id"`) — é a violação desse campo especificamente.
  3. `MOEDA_SEM_COTACAO` carrega `CampoCanonico.MOEDA` (serializado `"despesa.moeda"`, §9) — mesmo padrão de `ID_DUPLICADO`: a causa é uma chave específica, não o item inteiro.
  4. Todos os demais motivos não estruturais (`VALOR_NAO_POSITIVO`, `CATEGORIA_FORA_POLITICA`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, `FORA_COMPETENCIA`, `NOTA_FISCAL_AUSENTE`, `DUPLICIDADE`, e os quatro motivos de teto) carregam `campo` nulo — a causa não é uma chave específica, é uma decisão sobre o item como um todo.
  5. `ITEM_TIPO_INVALIDO` continua com `campo` nulo — a única exceção "estrutural implica campo preenchido" (spec 4.3), porque o defeito é do elemento inteiro, não de uma chave dele.
  Nenhuma mudança de mecanismo em `CompositorSaida` é exigida por isso além de já ter `CampoCanonico.ID` e `CampoCanonico.MOEDA` disponíveis em `ORDEM_CAMPO` (DT-019) — os construtores de `Motivo` em `DetectorIdDuplicado` e `ResolutorCambio` já passam o valor correto; o compositor só consome o que recebe.
- **Ordem determinística dos motivos:** `Comparator` explícito por estágio, nunca por `enum.ordinal()` — mecanismo inalterado, só a tabela cresce.
- **Decisão final coerente e um resultado por posição:** mecanismo de `CompositorSaida` inalterado — cada `indiceEntrada` produz exatamente um `ResultadoItem`, agora com os três campos de câmbio adicionais.

---

## 11. Tetos por periodicidade

**Princípio normativo (RN-019, AMB-036/037):** o mecanismo de teto é determinado pela `periodicidade` declarada na tabela de política efetivamente aplicável — nunca pelo nome histórico da categoria. Isso muda o design dos dois agregadores: `AgregadorTetoDiario` é **estendido** (deixa de usar um `Set<String>` fixo de categorias); o agregador exclusivo de `hospedagem` (`AgregadorTetoHospedagem`) é **substituído** por um componente novo, `pipeline/AgregadorTetoIndividual.java` (decisão fechada — ver §19), porque deixa de ser exclusivo de uma categoria e passa a processar qualquer categoria com `periodicidade: "diaria"`. Cada um dos dois agregadores decide sua aplicabilidade consultando a `periodicidade` resolvida (§6) para a categoria do item, não o nome dela.

**Periodicidade `"dia"` (generaliza o `AgregadorTetoDiario` atual):**

- Saldo **compartilhado** por `(data, categoria normalizada)`, consumido em ordem crescente de `indiceEntrada` — mecanismo já implementado, agora parametrizado pelo `limite` resolvido pela tabela de política do centro de custo em vez de uma constante de `PoliticaReembolso`.
- Item que excede o saldo disponível recebe o saldo restante (`PARCIALMENTE_REEMBOLSADO`, `TETO_DIARIO_APLICADO`); itens posteriores ao esgotamento recebem `NAO_REEMBOLSADO_TETO_ESGOTADO`/`TETO_DIARIO_ESGOTADO`.
- A `regra` do motivo `TETO_DIARIO_APLICADO` é `RN_011` quando a categoria for `alimentacao`, `RN_012` quando for `transporte_urbano`, `RN_019` para qualquer outra categoria sob esse mecanismo (ex.: `representacao`) — uma pequena tabela de exceção dentro do agregador, não um novo estágio de compositor.

**Periodicidade `"diaria"` (`AgregadorTetoIndividual`, novo componente que substitui `AgregadorTetoHospedagem`):**

- Teto **individual** por lançamento, sem saldo compartilhado.
- `hospedagem` sob esse mecanismo usa `TETO_HOSPEDAGEM_APLICADO`/`RN_013`; qualquer outra categoria (ex.: `estacionamento`) usa `TETO_INDIVIDUAL_APLICADO`/`RN_019` (AMB-037) — mesma lógica de pequena tabela de exceção por nome de categoria, desta vez dentro do agregador individual.
- Nunca produz `NAO_REEMBOLSADO_TETO_ESGOTADO` — não há saldo compartilhado para esgotar.

**Exclusões que antecedem o teto (mecanismo já existente, `SeletorElegiveis`, sem mudança de classe):**

- `limite == 0` numa tabela de centro cadastrado já recusou o item com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` na etapa de regras individuais (RN-019) — o item nunca chega à população que os agregadores de teto recebem.
- `limite == 0` em `padrao` nunca chega ao motor: invalida o arquivo de política inteiro na leitura (§5), antes de qualquer item ser processado.

O reaproveitamento do método `aplicarCorte` (já existente em `AgregadorTetoDiario`, documentado como "não conhece categoria, para ser reaproveitável pelo teto individual") continua válido: é o mecanismo que `AgregadorTetoDiario` (estendido) e `AgregadorTetoIndividual` (novo, em substituição a `AgregadorTetoHospedagem`) compartilham — a mesma decisão de design da v1.0 deste plano, agora exercida por mais categorias e por um componente com nome que reflete seu escopo real.

---

## 12. Duplicidade econômica

**Nova chave (RN-010, política v4):** `data`, categoria normalizada, `moeda` (a efetivamente usada — `"BRL"` quando a chave está ausente ou quando informada como tal), `valor_normalizado` em BRL (já convertido), `fornecedor` como recebido, `descricao` como recebida.

**Extensão de `DetectorDuplicidadeEconomica`:**

- `id` e `tem_nota_fiscal` seguem fora da chave — sem mudança.
- `moeda` entra como componente **adicional** da chave — nunca substitui `valor_normalizado`, que já é o valor convertido. Dois itens com o mesmo valor convertido, mas em moedas diferentes, nunca colidem (CA-033): a chave de comparação (um `record` ou tupla equivalente) inclui `moeda` como campo próprio, então `equals`/`hashCode` já os distingue sem necessidade de lógica condicional adicional.
- Itens com `MOEDA_SEM_COTACAO` têm `valor_normalizado` nulo e por isso já chegam recusados (`RECUSADO`) na etapa anterior — o `SeletorElegiveis` já os exclui da população que entra em `DetectorDuplicidadeEconomica`, mecanismo idêntico ao de qualquer outra recusa individual, sem necessidade de tratamento especial aqui.
- Primeira ocorrência por `indiceEntrada` mantida; posteriores recebem `DUPLICIDADE` — mecanismo inalterado.

---

## 13. Fronteira entre Jackson e o núcleo (extensão)

Tudo que a v1.0 deste plano já declarava continua valendo (JsonNode na entrada para distinguir ausência/tipo/formato; `BigDecimal` para todo valor efetivamente usado por regra; nenhuma regra financeira consulta `valor_informado`). A extensão da política v4:

- A mesma técnica de árvore `JsonNode` é usada para os dois arquivos externos (`LeitorPolitica`, `LeitorCambio`), não só para o envelope de despesas — é a mesma justificativa de DT-005 aplicada a mais dois arquivos.
- O 7º campo (`moeda`) introduz o único caso em que a **ausência da chave** e a **presença com `null`** produzem resultados diferentes (§8) — resolvido inteiramente na camada de leitura, sem vazar a distinção para o núcleo de regras (o núcleo só vê `"BRL"`/moeda válida, ou um motivo já decidido).
- `taxaCambioAplicada`/`dataCotacaoUtilizada` seguem o mesmo princípio de `valor_informado`: acompanham o item como dado de auditoria, mas depois do estágio de resolução de câmbio (§9) já são valores concretos (`BigDecimal`/`LocalDate`), não mais `JsonNode` — a árvore bruta nunca entra na composição final.

---

## 14. Estratégia monetária (extensão)

Tudo que a v1.0 já declarava (parsing decimal-exato via `USE_BIG_DECIMAL_FOR_FLOATS`, `decimalValue()`, nunca `doubleValue()`/`double`, comparação via `compareTo`, serialização em notação simples) continua valendo sem alteração. Extensão:

- A multiplicação `valor × taxa` (RN-020) usa `BigDecimal.multiply`, preservando a escala plena do produto — o arredondamento para duas casas (`setScale(2, RoundingMode.HALF_UP)`) acontece **uma única vez**, depois da multiplicação, nunca antes.
- **Exemplo funcional (CA-031, não é teste-canário):** USD `40,00 × 5,50 = 220,00` é o cenário normativo da spec para a fórmula de conversão — mas, como o valor bruto (`40,00`) e a taxa (`5,50`) já têm no máximo duas e duas casas decimais respectivamente, esse exemplo **não** distingue "arredondar só depois da multiplicação" de "arredondar o valor bruto antes e multiplicar depois": os dois caminhos produzem `220,00` neste caso específico, então ele não detecta arredondamento prematuro.
- **Teste-canário real de ordem de arredondamento (planejado, ver §17):** o caso que efetivamente diferencia as duas ordens exige um valor bruto com mais de duas casas decimais:
  ```
  valor bruto = 1.005
  taxa        = 1.005
  produto exato (sem arredondar antes) = 1.005 × 1.005 = 1.010025
  resultado correto — arredondar só uma vez, sobre o produto exato: 1.010025 → 1.01
  resultado incorreto — arredondar o valor bruto antes de multiplicar:
    1.005 → 1.01 (HALF_UP)
    1.01 × 1.005 = 1.01505 → 1.02 (HALF_UP)
  ```
  Uma implementação que arredonde `valor bruto` para duas casas **antes** de multiplicar pela taxa produz `1,02`; a implementação correta (RN-004/RN-020, DT-015) produz `1,01`. Esse é o caso que detecta arredondamento prematuro **antes** da multiplicação — o exemplo `40,00 × 5,50` não detecta.
  **Limite dessa garantia:** este teste-canário só detecta arredondamento *antes* da multiplicação. Ele **não** detecta, necessariamente, um segundo arredondamento *idempotente* aplicado *depois* — `setScale(2, RoundingMode.HALF_UP)` repetido sobre um valor que já tem escala 2 devolve o mesmo valor, então um `setScale` redundante no fim de um pipeline que já arredondou corretamente uma vez pode passar despercebido por um teste que só observa a saída final. A garantia de que existe **exatamente um** ponto de arredondamento no código depende, além deste teste, da responsabilidade arquitetural definida em DT-015 (só o normalizador arredonda) e de revisão de código nas tasks que tocarem `ResolutorCambio`/`Normalizador`.

---

## 15. Decisões técnicas

Esta seção restaura o texto integral de cada decisão técnica da v1.0 deste plano (commit anterior, `git show HEAD:specs/001-motor-reembolso/plan.md`), para que o documento seja compreensível sem consultar Git ou uma versão antiga do arquivo. Onde a spec 1.2 mudou algo, o texto original é seguido de uma subseção `#### Extensão — Dia 2` — nunca substituído silenciosamente.

### DT-001 — Linguagem e ambiente de execução

**Contexto:** prazo de dois dias, desenvolvedor com familiaridade declarada em Java, ambiente desta máquina já inspecionado (Java 21.0.2 e Maven 3.9.6 funcionando; Python e Node ausentes).
**Decisão:** Java 21 como linguagem única do projeto.
**Alternativa descartada:** Python/Node — não instalados nesta máquina, custariam tempo de setup sob prazo curto; Kotlin — mesma JVM, mas sintaxe nova sem necessidade.
**Consequência:** compilar o projeto exige JDK 21 e Maven 3.9+ instalados na máquina; executar o JAR já compilado exige apenas um Java 21 (JRE ou JDK), porque o fat jar já contém as dependências. Na primeira compilação, o Maven pode precisar baixar dependências (Jackson, JUnit) de um repositório remoto — exige acesso à rede nessa primeira vez; builds subsequentes reaproveitam o cache local do Maven.

Sem extensão nesta revisão — a política v4 não muda linguagem nem ambiente de execução.

### DT-002 — Empacotamento em JAR único via Maven Shade Plugin

**Contexto:** o contrato de execução exige `java -jar target/motor-reembolso.jar ...` funcionando sem passos adicionais e sem wrapper de shell.
**Decisão:** configurar o Maven Shade Plugin para produzir, a partir de `mvn package`, exatamente `target/motor-reembolso.jar` — um único artefato contendo todas as dependências (Jackson) e o `Main-Class` no manifesto. Fixar `<finalName>motor-reembolso</finalName>` para que o nome do artefato não dependa da versão do projeto.
**Alternativa descartada:** `maven-assembly-plugin` (resultado equivalente, Shade é mais direto para este caso de fat jar simples sem relocations); wrapper `.sh`/`.bat` (descartado por instrução explícita — superfície de manutenção extra em duas plataformas sem necessidade, já que `java -jar` funciona igual em qualquer SO com JDK).
**Consequência:** um único comando de build (`mvn package`) e um único comando de execução, sem classpath manual, sem script adicional para manter ou testar.

Sem extensão nesta revisão — nenhum novo artefato de build é exigido pela política v4; os dois arquivos externos são argumentos de linha de comando, não dependências de empacotamento.

### DT-003 — Contrato de CLI e códigos de saída

**Contexto:** a interface é fixa (`--input`/`--output`), mas a spec não define o que acontece na CLI quando o processamento não pode ocorrer — isso é contrato de execução, não regra de negócio.
**Decisão:** três códigos de saída (`0`, `2`, `3`), mensagem em texto simples em stderr para os códigos de erro, nenhuma escrita em `--output` quando o código não é `0`, e validação de envelope executada antes de qualquer abertura do arquivo de saída.
**Alternativa descartada:** vocabulário de erro em JSON estruturado (introduziria um esquema que a spec não define); escrever um JSON de erro no próprio `--output` (arriscaria confundir "resultado" com "estado de erro" no mesmo arquivo, e violaria a garantia de não sobrescrever `--output` em falha).
**Consequência:** contrato simples e verificável por teste de CLI; separa claramente "processamento não ocorreu" de "processamento ocorreu com itens recusados" — este último ainda é código `0`, porque recusa de item é resultado válido, não falha de processo.

#### Extensão — Dia 2

O contrato de CLI passa de duas para quatro flags obrigatórias (`--input`, `--output`, `--politica`, `--cambio`), em qualquer ordem, cada uma exatamente uma vez; o código `2` passa a cobrir também flag repetida/desconhecida e política/câmbio estruturalmente inválidos — casos que não existiam quando DT-003 foi escrita, porque os arquivos externos não existiam. A decisão de fundo — taxonomia de três códigos, mensagem simples em stderr, ausência de vocabulário JSON de erro, não tocar `--output` em falha — continua correta e não é refeita; só a superfície de validação de argumentos cresce (§3, DT-018 abaixo formaliza como).

### DT-004 — `BigDecimal` com parsing decimal-exato

**Contexto:** a spec exige `100.005 → 100,01`; ponto flutuante binário (`double`) representaria `100.005` como `100.00499999999999...` e arredondaria para o lado errado sob `HALF_UP`.
**Decisão:** `BigDecimal` de ponta a ponta, com `ObjectMapper` configurado para `USE_BIG_DECIMAL_FOR_FLOATS`, valores obtidos via `decimalValue()`, nunca via `doubleValue()` ou construção a partir de `double`.
**Alternativa descartada:** `double`/`float` (fonte de bug documentada); bibliotecas de dinheiro de terceiros (desnecessárias — `BigDecimal` nativo já cobre a exigência).
**Consequência:** os cinco valores de fronteira exigidos (33.333, 33.335, 33.345, 100.004, 100.005) arredondam corretamente por construção, não por sorte; qualquer regressão futura que reintroduza `double` em algum ponto do caminho é detectável pelo teste-canário de 100.005.

Sem subseção de extensão aqui: a extensão real desta decisão para a conversão cambial (ordem única de arredondamento sobre o produto `valor × taxa`) é grande o bastante para merecer uma decisão própria — ver DT-015 abaixo, que estende DT-004 explicitamente.

### DT-005 — Parsing por árvore (`JsonNode`) na camada de entrada

**Contexto:** RN-002 exige distinguir três classes de erro estrutural por campo (ausência, tipo, formato) e 4.3 exige preservar `valor_informado` exatamente como recebido, mesmo com tipo inválido — isso exige inspecionar o JSON bruto, não um objeto já desserializado e coagido.
**Decisão:** ler cada item como `JsonNode`, inspecionar `JsonNodeType` explicitamente por campo (nunca usar acessores permissivos como `asBoolean()`/`asInt()`, que fariam coerção que RN-002 proíbe), e só então produzir os campos validados e normalizados que o núcleo consome.
**Alternativa descartada:** desserialização direta para um objeto POJO tipado (perderia a distinção entre "campo ausente" e "campo de tipo errado", e perderia o valor bruto para `valor_informado` quando o tipo é inválido).
**Consequência:** classificação estrutural fiel à spec, ao custo de uma camada de leitura mais verbosa que uma desserialização direta.

#### Extensão — Dia 2

A mesma técnica de árvore `JsonNode` é reaproveitada para os dois arquivos externos (`LeitorPolitica`, `LeitorCambio`, §5/§7), não só para o envelope de despesas — mesma justificativa original, aplicada a mais dois arquivos. O 7º campo (`despesa.moeda`) exige, adicionalmente, distinguir "chave ausente" de "chave presente com `null`" — uma distinção que `JsonNode.get()` já oferece sem técnica nova; o detalhamento fica em DT-014.

### DT-006 — Arquitetura em pipeline linear seguindo a seção 8.1

**Contexto:** a ordem de processamento é normativa (seção 8 da spec) e distinta da ordem de apresentação de motivos (8.3); uma arquitetura que não espelhe isso arrisca produzir resultados corretos por acidente, não por construção.
**Decisão:** onze estágios lineares, um por passo de 8.1, cada um operando sobre a lista completa de itens sem jamais reordená-la; motivos acumulados por item ao longo dos estágios; reordenação para apresentação isolada no passo 10.
**Alternativa descartada:** um único método monolítico avaliando tudo por item em qualquer ordem interna conveniente — mais difícil de auditar linha a linha contra a seção 8, e mais fácil de violar sem perceber a regra "erros em campos não usados não impedem outras regras".
**Consequência:** cada estágio é testável isoladamente e mapeia 1:1 para uma linha da seção 8.1, o que facilita tanto a implementação quanto a auditoria da rastreabilidade.

#### Extensão — Dia 2

A spec 1.2 (§8.1) tem treze passos, não onze — os dois passos novos são a validação dos arquivos externos (à frente de tudo) e a resolução de câmbio/conversão (entre a detecção de ID duplicado e a normalização). A decisão de fundo (um estágio por linha de 8.1, motivos acumulados, reordenação isolada na composição) não muda — só o número de estágios. O diagrama completo de treze passos está em §2.

### DT-007 — Representação da política como estrutura imutável simples — SUPERADA por DT-011

**Texto original (histórico, preservado integralmente para rastreabilidade):**

**Contexto:** a política tem quatro valores numéricos fixos nesta versão da spec.
**Decisão:** uma estrutura única e imutável, `PoliticaReembolso`, sem mecanismo de configuração externa.
**Alternativa descartada:** motor de regras genérico, DSL, arquivo de configuração externo, banco de dados, framework de injeção de dependência — todos resolveriam um problema de flexibilidade que a spec atual não tem.
**Consequência:** mudar um teto é uma edição de quatro linhas; em troca, qualquer flexibilidade não prevista pela spec 1.1 exigiria refatoração explícita, o que é aceitável porque não há evidência de que essa flexibilidade seja necessária agora.

**Por que deixou de valer:** a política v4 (Dia 2) introduz exatamente a generalidade que DT-007 declarava desnecessária — RN-019 exige política externa, lida de arquivo, variável por centro de custo, com categorias dinâmicas e periodicidade configurável. A premissa de DT-007 ("a política tem quatro valores numéricos fixos nesta versão da spec") deixou de ser verdadeira; a decisão em si, não só sua consequência, está errada para a spec 1.2.
**Requisitos da spec 1.2 que provocaram a substituição:** RN-019 (política por centro de custo), RN-021 (vigência), RN-022 (processabilidade do arquivo externo) — nenhum deles existia na spec 1.1 que fundamentava DT-007.
**Substituída por:** DT-011.
**Nota:** esta supersessão já estava registrada do lado da spec em `DECISIONS.md` (D-003, "DT-007 invalidada"); esta entrada é a materialização formal do lado do `plan.md`, adiada até esta revisão como o próprio `DECISIONS.md` previa.

### DT-008 — `Motivo` como três enumerações fechadas

**Contexto:** 4.5 define um vocabulário fechado para `codigo`, `regra` e `campo`; strings livres repetidas pelo código são uma fonte comum de divergência de grafia entre o motivo emitido e o exigido pela spec.
**Decisão:** três enumerações (`MotivoCodigo`, `RegraNegocio`, `CampoCanonico`), cada uma com o texto canônico correspondente definido em um único lugar.
**Alternativa descartada:** strings soltas (`"RN-004"`, `"despesa.valor"`) espalhadas pelas classes que emitem motivos — funciona, mas cada ocorrência é uma chance de erro de digitação não detectado por compilação.
**Consequência:** erro de grafia num código de motivo vira erro de compilação, não uma divergência silenciosa só visível em teste ou na correção.

#### Extensão — Dia 2

O vocabulário cresce (três `MotivoCodigo` novos, quatro `RegraNegocio` novos, um `CampoCanonico` novo) — mesmo mecanismo, sem enum novo criado do zero. O detalhamento de quais valores entram e onde é DT-019 abaixo.

### DT-009 — Estratégia de testes em três níveis

**Contexto:** a rubrica avalia rastreabilidade `spec → tasks → commits → testes`; cada RN e cada CA precisa de destino verificável.
**Decisão:** testes unitários por regra (maioria), poucos testes de integração de pipeline completo (o arquivo de exemplo e fixtures adicionais), e um teste de contrato/CLI cobrindo códigos de saída e comportamento de arquivo. Nomenclatura de classe/método referenciando o `RN-NNN` e o `CA-NNN` correspondentes. Todo teste — inclusive o de integração (`ExemploCompletoTest`) e os de contrato/CLI (`CliContratoTest`, `EscritaAtomicaSaidaTest`) — usa o sufixo `*Test`, reconhecido pelo Maven Surefire por padrão, para que `mvn test` execute a suíte inteira num único comando.
**Alternativa descartada:** cobertura só por teste de integração ponta a ponta — esconderia qual regra especificamente falhou e dificultaria o grep de rastreabilidade que a rubrica valoriza. Nomear o teste de integração com o sufixo `*IT` (convenção do Maven Failsafe) — exigiria configurar e invocar um plugin de build adicional (`mvn verify`) só por causa de nomenclatura, e o teste deixaria de rodar em `mvn test`, contrariando a simplicidade pedida.
**Consequência:** qualquer regra da spec é localizável no código de teste por busca textual do próprio identificador `RN-NNN` ou `CA-NNN`; `mvn test` sozinho executa toda a suíte, sem exigir um segundo comando ou plugin para os testes de integração/CLI; ver matriz completa em §9 da v1.0 (§17 nesta versão).

#### Extensão — Dia 2

A mesma estratégia de três níveis (unitário majoritário, poucos testes de integração de pipeline completo, contrato/CLI) é adotada para RN-019 a RN-022 e CA-024 a CA-049 — nenhum nível novo é criado. A matriz de rastreabilidade estendida está em §17.

### DT-010 — Escrita atômica do arquivo de saída

**Contexto:** a garantia de que `--output` nunca é criado nem sobrescrito nos códigos `2` e `3` só é verdadeira se a escrita do resultado nunca tocar o destino antes de o resultado estar completo — escrever progressivamente e diretamente no destino deixaria um arquivo truncado no caminho oficial caso o processo falhe no meio da escrita.
**Decisão:** serializar o resultado completo para um arquivo temporário no mesmo diretório do destino (mesmo sistema de arquivos, condição para substituição atômica); fechar e concluir totalmente a escrita do temporário; só então mover/substituir o destino, preferindo `Files.move(temp, destino, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)`. Se o movimento/substituição falhar, o processo retorna código `2` e o destino anterior permanece intacto; o arquivo temporário é removido quando possível. O arquivo de destino nunca é aberto diretamente para escrever o JSON progressivamente.
**Alternativa descartada:** abrir `--output` diretamente e escrever o JSON incrementalmente — mais simples de codar, mas deixa uma janela em que uma falha no meio da escrita (processo interrompido, disco cheio) produz um arquivo parcialmente escrito exatamente no caminho que o resto do contrato promete preservar intacto.
**Consequência:** a garantia de não corromper `--output` passa a ser verdadeira por construção, não por sorte de nunca falhar no meio da escrita; o custo é uma etapa extra de escrita-e-movimentação em vez de uma escrita única, e a exigência de que o temporário fique no mesmo diretório do destino para que `ATOMIC_MOVE` seja viável na maioria dos sistemas de arquivos.

#### Extensão — Dia 2

A mesma estratégia passa a cobrir também as falhas de política/câmbio: como essas falhas retornam antes de o processo sequer tentar abrir um arquivo temporário (§3), um `--output` preexistente nunca é tocado por elas — a garantia original se estende sem exigir nenhuma mudança na mecânica de escrita em si.

### DT-011 — Política como modelo externo imutável, resolvido por centro de custo (substitui DT-007)

**Contexto:** RN-019 exige política lida de arquivo (`--politica`), com uma tabela `padrao` e um mapa de tabelas por centro de custo, cada uma com categorias dinâmicas (`limite` + `periodicidade`).
**Decisão:** `PoliticaExterna` (§5) como estrutura imutável construída inteiramente a partir do `JsonNode` do arquivo de política, após validação estrutural completa (RN-022) — nunca parcialmente populada. Resolução de tabela por centro de custo isolada num componente próprio, com API fechada `ResolutorPoliticaCentroCusto.resolver(String centroCusto, PoliticaExterna politica): TabelaPoliticaResolvida` (§6), que não conhece nem `LeitorPolitica` nem regras de negócio individuais — só resolve "qual tabela" e devolve o modelo imutável que diz o que essa tabela declara.
**Alternativa descartada:** motor de regras genérico ou DSL — RN-019 já é um contrato estrutural fechado e finito (duas periodicidades, tabela plana categoria→limite), não exige um mecanismo de regras arbitrário; construir um seria o mesmo excesso de arquitetura que DT-007 evitava, agora aplicado a um problema levemente maior, mas ainda finito.
**Consequência:** nenhuma constante financeira (`60`/`80`/`250`/`100`) permanece no código de produção; toda mudança de limite é edição de um arquivo JSON externo, nunca recompilação.

### DT-012 — Leitura e validação de política externa isolada em componente próprio

**Contexto:** RN-022 exige que política estruturalmente inválida impeça toda a apuração, com a mesma severidade de arquivo ilegível.
**Decisão:** `LeitorPolitica` (pacote `leitor`) segue o mesmo padrão já estabelecido por `ValidadorEnvelope`: uma API pública `ler(Path)` (§5) que internamente faz parsing via `JsonNode`, valida todo o contrato estrutural de §4.1.1, e devolve `PoliticaExterna` ou lança uma exceção dedicada (`PoliticaInvalidaException`) que o `Main` traduz para exit `2`.
**Alternativa descartada:** validar campo a campo dentro do próprio `Main` — replicaria a mistura de responsabilidades (I/O + regra de validação) que `ValidadorEnvelope` já evita para o envelope de despesas.
**Consequência:** o `Main` ganha mais uma chamada de leitor no início do fluxo, sem crescer em complexidade de validação — a mesma forma de `ValidadorEnvelope.validar(raiz)` já em uso.

### DT-013 — Câmbio como tabela invertida para consulta eficiente por data

**Contexto:** RN-020 exige, para cada despesa estrangeira, a cotação exata na data ou a mais recente anterior, nunca futura, sem interpolação — potencialmente muitas consultas (uma por item em moeda estrangeira) contra um arquivo estruturado por data primeiro, moeda depois.
**Decisão:** `LeitorCambio` inverte a estrutura do arquivo (data→moeda→taxa) para `Map<String, NavigableMap<LocalDate, BigDecimal>>` (moeda→data→taxa) no momento da leitura, uma única vez. A consulta usa `NavigableMap.floorEntry(data)`, que devolve a entrada de chave igual ou imediatamente anterior — nunca posterior — em tempo logarítmico.
**Alternativa descartada:** percorrer `taxas` linearmente a cada consulta, filtrando datas ≤ data da despesa e escolhendo o máximo — funcionalmente equivalente, mas repetiria o trabalho de ordenação a cada chamada em vez de uma única vez na leitura; também exigiria checagem manual de "não é futura", que `floorEntry` já garante estruturalmente.
**Consequência:** a garantia "nunca cotação futura" decorre da escolha do método (`floorEntry` nunca olha para a frente), não de uma condição escrita à mão que poderia ser esquecida ou invertida por engano.

### DT-014 — Parsing de `despesa.moeda` distinguindo ausência de chave e valor `null`

**Contexto:** RN-002 exige que a ausência da chave `moeda` resolva silenciosamente para `BRL`, mas que a chave presente com valor `null` seja `CAMPO_AUSENTE` — o único campo do contrato com essa assimetria.
**Decisão:** reaproveitar a semântica já presente em `JsonNode.get(chave)` (DT-005): referência Java `null` quando a chave não existe, instância de `NullNode` (`isNull() == true`) quando a chave existe com valor `null`. Um método de validação dedicado (`validarMoeda`, §8) checa `== null` antes de checar `.isNull()` — a ordem dos dois `if` é o que preserva a distinção. `get()`, não `path()`, é escolhido por coerência com a camada existente e por deixar o ramo de ausência explícito antes de acessar qualquer propriedade do nó (§8 detalha por que `path()` também distinguiria os casos, mas não é a escolha feita).
**Alternativa descartada:** um acessor permissivo com valor padrão (ex.: `asText("BRL")`) — colapsaria ausência, `null` e qualquer outro caso que não produza texto num único resultado `"BRL"`, destruindo a distinção antes que o código de validação pudesse vê-la.
**Consequência:** nenhuma técnica nova é introduzida além da já usada para os outros sete campos — só a ordem dos `if` e a interpretação do primeiro caso (`null` → retorno silencioso em vez de motivo) mudam.

### DT-015 — Conversão cambial com arredondamento único (estende DT-004)

**Contexto:** RN-004/RN-020 proíbem dois arredondamentos (um antes e um depois da conversão) — só um, depois da multiplicação.
**Decisão:** o estágio de resolução de câmbio (§9) produz `valorConvertidoBruto` **sem** chamar `setScale` — só o normalizador (RN-004), estágio seguinte do pipeline, arredonda. Nenhum ponto intermediário do código chama `setScale`/`round` sobre um valor monetário. Isso vale igualmente para BRL: `ResolutorCambio` copia o valor original para `valorConvertidoBruto` sem tocar sua escala, e é só o `Normalizador` — o mesmo caminho usado para moeda estrangeira — que aplica o arredondamento único (§9 detalha a divisão de responsabilidade completa entre `ValidadorItem`, `ResolutorCambio` e `Normalizador`).
**Alternativa descartada:** arredondar dentro do próprio resolutor de câmbio, antes de devolver o valor — introduziria o risco de um segundo arredondamento acidental se o normalizador também arredondasse (dupla aplicação de `HALF_UP` pode, em casos de fronteira, produzir resultado diferente de uma única aplicação sobre o produto não arredondado).
**Consequência:** o teste-canário real (`1.005 × 1.005`, §14) detecta arredondamento do valor bruto **antes** da multiplicação — produziria `1,02` em vez do `1,01` correto. Ele **não** garante, sozinho, que não exista um segundo arredondamento idempotente depois (um `setScale` redundante sobre um valor que já tem escala 2 é indetectável só pela saída); por isso essa garantia depende também da responsabilidade arquitetural desta DT (só o normalizador arredonda) e de revisão de código nas tasks futuras, não só do teste automatizado.

### DT-016 — Comparação textual exata de `centro_custo` (formaliza a técnica de RN-019)

**Contexto:** RN-019 exige comparação exata, sem trim, sem normalização de caixa ou acento — deliberadamente diferente da normalização de categoria (RN-005).
**Decisão:** `ResolutorPoliticaCentroCusto.resolver(String centroCusto, PoliticaExterna politica)` usa `Map.get(centroCusto)` diretamente (equivalente a `String.equals`) contra as chaves de `centrosCusto` — nenhuma transformação aplicada ao valor de `colaborador.centro_custo` antes da consulta; `centroCusto == null` (que já representa ausência/nulo/tipo inválido, resolvidos antes por quem lê o envelope) resolve diretamente para `padrao` sem tentar a busca no mapa.
**Alternativa descartada:** reaproveitar a mesma normalização de RN-005 "por consistência" — rejeitada porque a spec declara explicitamente que a comparação de centro de custo segue regra própria, distinta da de categoria; unificá-las seria introduzir comportamento não pedido.
**Consequência:** `"CC-COMERCIAL"` e `"cc-comercial"` são centros de custo diferentes para efeito de resolução de política — coerente com o texto normativo de RN-019.

### DT-017 — Tetos generalizados por periodicidade, não por nome de categoria

**Contexto:** RN-019/AMB-036/AMB-037 exigem que o mecanismo de teto (compartilhado vs. individual) e o motivo emitido dependam da `periodicidade` configurada, não de uma lista fixa de nomes de categoria — uma política válida pode declarar `representacao` como `"dia"` ou `estacionamento` como `"diaria"`, categorias que não existiam na v1.1.
**Decisão:** os dois agregadores de teto — `AgregadorTetoDiario` (estendido) e `AgregadorTetoIndividual` (novo, em substituição a `AgregadorTetoHospedagem` — decisão fechada, §11/§19) — consultam a `periodicidade` resolvida pelo `ResolutorPoliticaCentroCusto` para decidir a qual mecanismo um item pertence — o `Set<String>` fixo de categorias do agregador diário atual é removido. A escolha de `regra`/`codigo` do motivo (`RN_011`/`RN_012`/`RN_019` para `"dia"`; `TETO_HOSPEDAGEM_APLICADO`/`TETO_INDIVIDUAL_APLICADO` para `"diaria"`) continua sendo uma pequena tabela de exceção por nome de categoria **dentro** do agregador — porque é isso que a spec pede (nomes históricos continuam recebendo seus códigos históricos), não uma contradição com a generalização do mecanismo.
**Alternativa descartada:** manter dois conjuntos fixos de nomes e simplesmente adicionar `representacao`/`estacionamento` a eles — funcionaria para os quatro cenários do envelope, mas quebraria na primeira política externa futura que declarasse uma quinta categoria com qualquer periodicidade, exatamente o tipo de acoplamento que RN-019 elimina.
**Consequência:** o agregador de teto passa a receber, por item, a `periodicidade` e o `limite` já resolvidos (não os lê de `PoliticaReembolso`), tornando `AgregadorTetoDiario` e `AgregadorTetoIndividual` independentes de qualquer lista de nomes fixa para decidir aplicabilidade.

### DT-018 — `CLI` valida quatro flags como conjunto, não como sequência de casos (estende DT-003)

**Contexto:** o contrato de execução (§4.1.1, AMB-034) exige um subcomando fixo (`calcular`) seguido de quatro flags obrigatórias em pares `flag valor`, em qualquer ordem, cada uma exatamente uma vez, com exit `2` para subcomando ausente/incorreto, token posicional extra, flag sem valor, ausência, repetição ou flag desconhecida.
**Decisão:** o parser valida em duas etapas fechadas. (1) o primeiro token deve ser exatamente `calcular` — único posicional aceito; qualquer outro valor, ausência dele, ou um token posicional adicional depois dele, é exit `2` antes mesmo de olhar para as flags. (2) os tokens restantes são consumidos estritamente aos pares `flag valor`; uma quantidade ímpar de tokens, ou uma flag como último token (sem valor seguinte), é exit `2`. Os pares reconhecidos são acumulados num mapa (`Map<String, String>`), contando ocorrências por chave; o conjunto resultante é validado contra o conjunto fechado `{--input, --output, --politica, --cambio}` **depois** de percorrer todos os pares — nunca por posição.
**Alternativa descartada:** manter o `switch` posicional de dois casos e simplesmente adicionar dois `case` novos — não detectaria repetição (`--input a --input b`), token posicional extra, flag sem valor, nem cobriria a validação "exatamente quatro, nem mais nem menos" de forma natural.
**Consequência:** `CliContratoTest` ganha casos novos (subcomando ausente/incorreto, token posicional extra, flag sem valor, flag repetida, flag desconhecida, ordem embaralhada válida, comando antigo só com `--input`/`--output`) sem exigir um parser mais complexo que uma verificação do primeiro token seguida de uma contagem por chave.

### DT-019 — Extensão dos três enums fechados de motivo (estende DT-008)

**Contexto:** DT-008 já estabelecia que `codigo`/`regra`/`campo` são enumerações fechadas, cada uma serializando para o texto canônico num único ponto — a spec 1.2 só acrescenta valores a esse vocabulário, não muda o mecanismo.
**Decisão:** `MotivoCodigo` ganha três valores (`MOEDA_SEM_COTACAO`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, `TETO_INDIVIDUAL_APLICADO`); `RegraNegocio` ganha quatro (`RN_019` a `RN_022`); `CampoCanonico` ganha um (`MOEDA`), inserido na posição correta da ordem canônica — nenhum enum existente perde um valor ou muda de nome.
**Alternativa descartada:** nenhuma — é a aplicação direta e sem alternativa do mecanismo já decidido em DT-008 a um vocabulário maior.
**Consequência:** a tabela de estágios de `CompositorSaida` (`ESTAGIO_POR_CODIGO`, `ORDEM_CAMPO`) precisa de entradas novas para os valores novos — sem isso, o compositor lançaria `IllegalArgumentException` ("fora do vocabulário fechado de precedência") ao encontrar um motivo novo, o que é o comportamento correto de falha rápida caso a extensão seja esquecida em algum ponto.

---

## 16. Regressão e compatibilidade

Não há modo legado, flag especial ou política interna nesta versão. A compatibilidade com o comportamento histórico é determinada **inteiramente pelos arquivos fornecidos em cada execução** — nunca por um caminho de código diferente:

1. **Política externa equivalente à baseline histórica** (`padrao`: alimentação R$60/dia, transporte R$80/dia, hospedagem R$250/diária; gatilho de nota R$100) processando `exemplos/despesas-exemplo.json` → `585.43` (`§12.1` da spec, CA-037).
2. **Política oficial v4** (`politica-v4.json`) com `CC-ENG-PLATAFORMA` processando o mesmo arquivo → `351.43` (`§12.2`, CA-038).
3. **Rafael Nkemelu**, `CC-COMERCIAL`, moeda estrangeira (`despesas-envelope.json`) → `1143.26` (`§12.3`).
4. **Dani Okonkwo**, centro de custo desconhecido (`despesas-envelope-cc-desconhecido.json`) → `373.76` (`§12.4`).

A política histórica do cenário 1 é uma **fixture externa de teste** (um arquivo JSON no formato de `politica-v4.json`, mas com os quatro valores históricos, mais um `cambio.json` mínimo ou vazio, já que o arquivo de exemplo original não tem despesas em moeda estrangeira) — versionada em `tests/resources/`, nunca uma constante nem um fallback interno de produção. Isso é uma mudança de forma em relação à v1.0 deste plano, onde `PoliticaReembolso.padrao()` *era* essa mesma baseline histórica, embutida no código: a partir desta versão, o motor não tem mais noção de "política padrão de fábrica" — toda execução, inclusive a de regressão, depende de um `--politica` real.

---

## 17. Estratégia de testes (planejada — nenhum teste criado nesta tarefa)

Esta seção restaura a estratégia e a matriz completas da v1.0 deste plano (equivalente ao antigo §9), e as estende para a spec 1.2. Nenhum arquivo de teste é criado ou modificado nesta tarefa — a criação é trabalho de `tasks.md`, a partir de `T-022`.

- **Nível e proporção:** majoritariamente unitário (uma regra de negócio = um grupo de testes isolado, sem I/O real); testes de integração de pipeline completo contra `exemplos/despesas-exemplo.json` **e**, a partir da spec 1.2, contra os dois arquivos de `exemplos/envelope/`; testes de contrato/CLI cobrindo códigos de saída, comportamento de stdout/stderr e a escrita atômica de `--output`.
- **Cada `RN-NNN` tem teste?** Garantido pela matriz de rastreabilidade abaixo — todo `RN-001` a `RN-022` aparece em pelo menos uma linha.
- **Casos de borda da seção 7:** cobertos pelos mesmos grupos de teste da matriz, via os `CA-NNN` correspondentes, que derivam diretamente da tabela de casos de borda e, a partir da spec 1.2, também de `§12`.
- **Nomenclatura:** classe/grupo nomeado pelo identificador da regra (`RN004NormalizacaoMonetariaTest`); método nomeado pelo cenário e resultado esperado, carregando também o `CA-NNN` aplicável — por nome de método (`rn004_ca009_100_005_arredondaParaCima_100_01()`), `@DisplayName` (`"RN-004 / CA-009 — 100.005 arredonda para 100,01"`) ou comentário imediatamente acima do caso. O objetivo é permitir busca textual direta tanto por `RN-004` quanto por `CA-009` e chegar ao mesmo teste.
- **Fixture de saída esperada:** o fixture usado por `ExemploCompletoTest` é **escrito e revisado manualmente** a partir do schema completo das seções 4.3 a 4.5 da spec — nunca gerado pelo próprio motor em teste, o que tornaria o teste circular. Usa a tabela 4.7 (ou, para os cenários da política v4, `§12`) como fonte de decisões e valores, e RN-017 mais a ordem de 8.3 para montar o objeto completo de cada motivo (`codigo`, `regra`, `campo`). A comparação é **estrutural** (JSON contra JSON, campo a campo), nunca textual.
- **Comando único de execução:** `mvn test` executa **todos** os testes planejados desta matriz, inclusive os de integração e os de contrato/CLI — todos seguem o sufixo `*Test` (Maven Surefire), nunca `*IT` (que exigiria o Maven Failsafe). `mvn package` executa esses mesmos testes antes de gerar o JAR, porque a fase `test` precede `package` no ciclo de vida padrão do Maven.

### Matriz de rastreabilidade — restaurada da v1.0 (RN-001 a RN-018, CA-001 a CA-023)

| RN / CA | Grupo de teste planejado | Nível |
|---|---|---|
| RN-001 · CA-020 | `EnvelopeValidoTest` — período invertido, `despesas` vazia, bloco `colaborador` malformado tolerado | Unidade |
| RN-002 · CA-021 · CA-022 · CA-023 | `ContratoDoItemTest` — `CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` por campo, `ITEM_TIPO_INVALIDO`, ordem canônica de múltiplos motivos | Unidade |
| RN-002 (valor_informado) | `ValorInformadoTest` — preservação do valor bruto para tipos válidos e inválidos (string, booleano, ausente, elemento não-objeto) | Unidade |
| RN-003 · CA-019 | `IdDuplicadoTest` — todas as ocorrências recusadas, ID inválido não entra na verificação | Unidade |
| RN-004 · CA-009 · CA-018 | `NormalizacaoMonetariaTest` (parametrizado) — `33.333`, `33.335`, `33.345`, `100.004`, `100.005` | Unidade |
| RN-005 · CA-015 | `NormalizacaoCategoriaTest` — caixa, acento, espaço nas pontas; `transporte urbano` não reconhecido | Unidade |
| RN-006 · CA-017 | `ValorNaoPositivoTest` — negativo, zero, valor que normaliza para zero; total do período não reduz | Unidade |
| RN-007 · CA-016 | `CategoriaForaPoliticaTest` — `coworking` recusado antes de qualquer teto | Unidade |
| RN-008 · CA-011 · CA-012 | `CompetenciaTest` — bordas inclusivas do período, data fora da janela | Unidade |
| RN-009 · CA-008 · CA-009 | `NotaFiscalTest` (parametrizado) — `100,00` elegível, `100,01` recusado, deslocamento de fronteira por arredondamento | Unidade |
| RN-010 · CA-013 · CA-014 | `DuplicidadeEconomicaTest` — chave exata, primeira ocorrência mantida, `100.00`/`100.01` não são duplicata | Unidade |
| RN-011 · RN-012 · CA-004 | `TetoDiarioTest` — agregação por data e categoria (alimentação e transporte urbano) | Unidade |
| RN-013 · CA-007 | `TetoHospedagemTest` — teto por lançamento, independente de descrição, duas hospedagens no mesmo dia somando até R$ 500,00 | Unidade |
| RN-014 · CA-005 | `ReembolsoParcialTest` — corte no teto, nunca recusa integral por ultrapassagem | Unidade |
| RN-015 · CA-006 | `DistribuicaoTetoTest` — consumo de saldo em ordem de `indice_entrada`, estado `NAO_REEMBOLSADO_TETO_ESGOTADO` distinto de `RECUSADO` | Unidade |
| RN-016 · CA-010 | `RegraViagemEfeitoNuloTest` — troca de descrição/campo `em_viagem` desconhecido não altera resultado, item único para isolar de RN-010 | Unidade |
| — (campos desconhecidos) | `CamposDesconhecidosTest` — campo fora do contrato em `despesa` e em `colaborador` é ignorado silenciosamente | Unidade |
| RN-017 · CA-002 | `ComposicaoSaidaTest` — toda posição produz um registro, ordem da entrada preservada, nenhum item desaparece | Unidade |
| — (ordem de apresentação, 8.3/8.4) | `OrdemMotivosTest` — os dois exemplos normativos de 8.4 (três motivos simultâneos; exclusão de nota por valor não positivo) | Unidade |
| RN-018 · CA-001 · CA-003 | `TotalPeriodoTest` — soma dos `valor_reembolsavel` apresentados igual ao total | Unidade/Integração |
| CA-001 · CA-002 · CA-003 (integral) | `ExemploCompletoTest` — os 14 itens de `exemplos/despesas-exemplo.json` processados de ponta a ponta, comparados estruturalmente contra o fixture esperado (escrito à mão a partir de 4.3–4.5); `total_reembolsavel` = R$ 585,43 | Integração |
| — (contrato de execução) | `CliContratoTest` — código `0` em sucesso; código `2` para argumento ausente/arquivo inexistente/JSON sintaticamente inválido/falha de escrita; código `3` para envelope inválido; mensagem em stderr, nada em stdout | Contrato/CLI |
| — (escrita atômica de `--output`, DT-010) | `EscritaAtomicaSaidaTest` — envelope inválido, JSON sintaticamente inválido e falha simulada antes da substituição não alteram um arquivo preexistente; sucesso substitui o destino; nenhum temporário remanescente | Contrato/CLI |

Todo identificador de `RN-001` a `RN-018` e de `CA-001` a `CA-023` aparece em pelo menos uma linha da tabela acima.

### Extensão da matriz — spec 1.2 (RN-019 a RN-022, CA-024 a CA-049)

| RN / CA | Grupo de teste planejado | Nível |
|---|---|---|
| RN-021, RN-022 (política) · CA-035, CA-036, CA-045 | `LeitorPoliticaTest` — arquivo ausente/ilegível/JSON inválido; cada uma das dezesseis validações estruturais de §5 (raiz não objeto, `vigencia` ausente/malformada, `moeda_base` diferente de `"BRL"`, `limite` zero em `padrao`, `periodicidade` fora de `"dia"`/`"diaria"`, etc.); `observacao` de categoria presente como texto → válido, ignorado; `observacao` presente com tipo não textual → política inválida; um arquivo que satisfaz integralmente o contrato (CA-045) é aceito | Unidade |
| RN-022 (câmbio) · CA-036, CA-046 | `LeitorCambioTest` — arquivo ausente/ilegível/JSON inválido; `taxas: {}` válido; `moeda_base` errada, data/moeda/taxa inválidas → falha; `fonte`/`observacao` ausentes → válido; presentes como texto → válido, ignorado; presentes com tipo não textual → câmbio inválido; uma chave malformada dentro de `taxas` (data não-ISO ou moeda fora de `[A-Z]{3}`) não é tratada como campo desconhecido — invalida o arquivo; um arquivo que satisfaz integralmente o contrato (CA-046) é aceito | Unidade |
| RN-019 · CA-024, CA-025, CA-026, CA-027 | `ResolutorPoliticaCentroCustoTest` — centro cadastrado, desconhecido, ausente, nulo, tipo inválido; comparação textual exata (sem trim/caixa/acento); categoria ausente do centro cadastrado não recebe o limite de `padrao` (CA-025); `representacao` reembolsável só onde declarada (CA-026); limite `0,00` em centro cadastrado → `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (CA-027) | Unidade |
| RN-020 · CA-029, CA-030, CA-031 | `ResolucaoCambioTest` — cotação exata, fallback para a mais recente anterior (CA-029), proibição de cotação futura, `MOEDA_SEM_COTACAO` para moeda nunca cotada (CA-030); mantém **os dois** casos de arredondamento: `40,00 × 5,50 = 220,00` como cenário normativo de CA-031 (exemplo funcional, não distingue ordem de arredondamento) **e**, adicionalmente, `1.005 × 1.005` como teste técnico de ordem de arredondamento (§14) — resultado correto `1,01`, incorreto `1,02` se o valor bruto for arredondado antes da multiplicação | Unidade |
| RN-020 (motivo, 8.4 item 14) | `MoedaSemCotacaoTest` — o motivo `MOEDA_SEM_COTACAO` carrega `campo = despesa.moeda` (§10); coexistência de `MOEDA_SEM_COTACAO` com `CATEGORIA_FORA_POLITICA`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e/ou `FORA_COMPETENCIA` na mesma posição — nunca com `VALOR_NAO_POSITIVO`/`NOTA_FISCAL_AUSENTE`/`DUPLICIDADE`/teto | Unidade |
| RN-002 (moeda) · CA-048 | `CampoMoedaTest` — ausência de chave vs. `null` explícito vs. tipo inválido vs. formato inválido | Unidade |
| RN-009 (atualizada) · CA-032 | `NotaFiscalConvertidaTest` — gatilho aplicado sobre o valor já convertido (USD 40,00 → R$220,00 sem nota → recusado), não o valor original na moeda da despesa | Unidade |
| RN-010 (atualizada) · CA-033 | `DuplicidadeEntreMoedasTest` — mesma data/categoria/fornecedor/descrição/valor convertido, moedas diferentes → **não** são duplicatas | Unidade |
| RN-016 (extensão a moeda) · CA-028 | `RegraViagemEfeitoNuloTest` (estendido) — `despesa.moeda` diferente de `BRL` não amplia teto algum nem afeta outros itens do mesmo dia/período; RN-016 continua sem efeito | Unidade |
| RN-017 (atualizada) · CA-034 | `SaidaCambioTest` — os quatro formatos de `moeda`/`taxa_cambio_aplicada`/`data_cotacao_utilizada` (BRL; estrangeira convertida; estruturalmente inválida; válida sem cotação) | Unidade |
| RN-019 (periodicidade) · CA-047, CA-049 | `TetoPorPeriodicidadeTest` — quatro cenários, provando que o mecanismo depende de `periodicidade`, não do nome histórico da categoria: (1) categoria externa `representacao` com `"dia"`, saldo compartilhado (CA-047); (2) categoria externa `estacionamento` com `"diaria"`, teto individual, `TETO_INDIVIDUAL_APLICADO` (CA-049); (3) `hospedagem` reconfigurada com `"dia"` — teto **compartilhado** entre lançamentos da mesma data, `TETO_DIARIO_APLICADO`/`TETO_DIARIO_ESGOTADO`, `regra = RN-019` (não `RN-013`); (4) `alimentacao` reconfigurada com `"diaria"` — teto **individual** por lançamento, `TETO_INDIVIDUAL_APLICADO`, `regra = RN-019` (não `RN-011`) | Unidade |
| — (contrato de execução) · CA-041 · CA-042 · CA-043 · CA-044 | `CliContratoTest` (estendido) — sucesso `0` do comando completo com as quatro flags; ordem embaralhada válida; subcomando ausente ou incorreto; token posicional extra; flag ausente, repetida ou desconhecida; flag sem valor (quantidade ímpar de tokens); comando antigo só `--input`/`--output`; política/câmbio inválidos; preservação byte a byte de um `--output` preexistente em qualquer cenário de falha; stdout vazio e mensagem em stderr em qualquer falha | Contrato/CLI |
| — (regressão histórica) · CA-037, CA-038 | `RegressaoHistoricaTest` (extensão de `ExemploCompletoTest`) — `exemplos/despesas-exemplo.json` sob política externa histórica (`585.43`, CA-037) e sob `politica-v4.json`/`CC-ENG-PLATAFORMA` (`351.43`, CA-038), incluindo as quatro mudanças de item declaradas em `§12.2` | Integração |
| — (integração do envelope) · CA-039, CA-040 | `IntegracaoEnvelopeTest` — `despesas-envelope.json`/Rafael/`CC-COMERCIAL` com `politica-v4.json`+`cambio.json` reais (`1143.26`, CA-039) e `despesas-envelope-cc-desconhecido.json`/Dani/`CC-SUPORTE-N2` (`373.76`, CA-040), ambos comparados contra `§12.3`/`§12.4` | Integração |

Todo identificador de `RN-019` a `RN-022` e de `CA-024` a `CA-049` aparece em pelo menos uma linha da tabela acima — conferido linha a linha ao escrever esta seção: `CA-024`–`027` (ResolutorPoliticaCentroCustoTest), `CA-028` (RegraViagemEfeitoNuloTest), `CA-029`–`031` (ResolucaoCambioTest), `CA-032` (NotaFiscalConvertidaTest), `CA-033` (DuplicidadeEntreMoedasTest), `CA-034` (SaidaCambioTest), `CA-035`/`036`/`045` (LeitorPoliticaTest), `CA-036`/`046` (LeitorCambioTest), `CA-037`/`038` (RegressaoHistoricaTest), `CA-039`/`040` (IntegracaoEnvelopeTest), `CA-041`–`044` (CliContratoTest), `CA-047`/`049` (TetoPorPeriodicidadeTest), `CA-048` (CampoMoedaTest).

**Declaração final, só depois da conferência acima:** todo identificador de `RN-001` a `RN-022` e de `CA-001` a `CA-049` aparece em pelo menos uma linha desta matriz (as duas tabelas desta seção, juntas).

---

## 18. Riscos

Esta seção restaura os **oito** riscos da v1.0 deste plano (não sete) e os estende. Dois deles — o de "excesso de arquitetura" e o de "mudança do Dia 2 exigir I/O" — se materializaram de fato nesta revisão e são reformulados abaixo, sem apagar o registro original.

### Riscos da v1.0 — preservados, com dois marcados como materializados

| Risco | Probabilidade | O que faço se acontecer |
|---|---|---|
| Parsing de número via `double` reintroduzido em algum ponto (ex. troca de biblioteca, refactor apressado) | Média | Teste-canário de `100.005` (RN-004) falha imediatamente e aponta o ponto exato da regressão. |
| Coerção implícita de tipo pelo Jackson (`asBoolean()`/`asInt()` em vez de checagem explícita de `JsonNodeType`) | Média | Revisão de código restrita a essa camada de leitura antes de fechar RN-002; teste dedicado por campo cobrindo booleano-como-número e o inverso. |
| Regra parando no primeiro motivo encontrado (short-circuit indevido) | Média | `OrdemMotivosTest` e os testes de RN-002 com múltiplos motivos (`CA-021`, `CA-023`) capturam isso diretamente. |
| **[MATERIALIZADO]** Excesso de arquitetura (motor de regras genérico, configuração externa) sob tentação de "preparar para o Dia 2" | Era "baixa mas real" na v1.0 — o Dia 2 realmente introduziu política externa por centro de custo (RN-019), então a premissa original ("a spec atual não tem essa necessidade") deixou de valer tal como escrita | O risco remanescente é mais estreito, não desapareceu: mesmo com a política agora externa e dinâmica, **não construir** um motor de regras genérico ou DSL além do contrato fechado que RN-019 já define (duas periodicidades, tabela plana categoria→limite) — DT-011 documenta essa fronteira explicitamente; qualquer generalização além disso só quando um requisito real (não hipotético) a exigir, nunca antes. |
| **[MATERIALIZADO]** Mudança de requisito do Dia 2 exigir tocar código de I/O além do núcleo | Era "desconhecida — não antecipada" na v1.0 — o Dia 2 realmente exigiu isso: dois leitores externos novos e extensão do parser de CLI | A fronteira núcleo/CLI (§2) absorveu a mudança como previsto: `LeitorPolitica`/`LeitorCambio` (DT-012/DT-013) isolam a leitura externa, e o `Main` estendido (DT-018) isola a extensão de flags — nenhuma regra de negócio migrou para a camada de I/O, confirmando que a separação original era a decisão correta. |
| Teste de integração não ser descoberto pelo Maven por nomenclatura inadequada (ex. sufixo `*IT` sem o Maven Failsafe configurado) | Média — foi um problema real encontrado na auditoria deste plano | Todo teste, inclusive integração e CLI, usa o sufixo `*Test` (DT-009/DT-010); `mvn test` sozinho é o comando de verificação antes de qualquer commit, nunca um plugin adicional só por nomenclatura. |
| Escrita direta e progressiva em `--output` corromper ou truncar o arquivo em caso de falha no meio da serialização | Média | Escrever sempre em arquivo temporário no mesmo diretório do destino e mover atomicamente (DT-010); o destino nunca é aberto diretamente para escrita incremental. |
| Categoria estruturalmente válida, mas fora do vocabulário fechado, ser descartada (virar nula) antes de RN-007 conseguir compará-la | Média — foi um erro real encontrado na auditoria deste plano | Modelo corrigido em §4: a categoria normalizada é sempre produzida quando o campo é estruturalmente válido, inclusive quando o resultado é `coworking` ou qualquer outro valor fora da política; só RN-007/RN-019 decidem o motivo de recusa. |

### Riscos novos, específicos da política v4

| Risco | Probabilidade | O que faço se acontecer |
|---|---|---|
| Confundir "ausência de chave" com "`null` explícito" no campo `moeda`, tratando os dois igualmente | Média — é o único campo do contrato com essa assimetria, fácil de esquecer sob pressão de copiar o padrão dos outros sete campos | `CampoMoedaTest` cobre os quatro casos (§17) separadamente; DT-014 documenta a ordem exata dos `if` que preserva a distinção. |
| Arredondar duas vezes na conversão cambial (uma vez no resolutor de câmbio, outra no normalizador), ou arredondar o valor bruto antes da multiplicação | Média | O teste `1.005 × 1.005` detecta arredondamento prematuro antes da multiplicação: resultado correto `1,01`; resultado incorreto `1,02`. A existência de um único ponto de arredondamento também é garantida por DT-015 e por revisão de código, porque um segundo `setScale` idempotente pode não ser observável na saída. |
| Reintroduzir um `Set<String>` fixo de categorias nos agregadores de teto, quebrando a generalização por `periodicidade` exigida por RN-019 | Média — os agregadores atuais já têm esse `Set` hardcoded, e generalizá-los é a mudança mais invasiva desta revisão | DT-017 documenta a decisão; `TetoPorPeriodicidadeTest` exercita uma categoria externa (não `alimentacao`/`transporte_urbano`/`hospedagem`) sob cada periodicidade. |
| Misturar `padrao` com a tabela de um centro cadastrado (fallback por categoria) | Média — é o erro mais fácil de cometer ao "ajudar" um centro cadastrado incompleto | RN-019/DT-011 são explícitos: nunca a união das duas tabelas; `ResolutorPoliticaCentroCustoTest` cobre categoria ausente do centro cadastrado mas presente em `padrao`, esperando `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não o limite de `padrao`. |
| `floorEntry` usado incorretamente (ex.: `ceilingEntry`, que permitiria cotação futura) | Baixa, mas gravidade alta se ocorrer | DT-013 documenta o método exato exigido; `ResolucaoCambioTest` inclui um caso de data sem cotação exata cuja única cotação disponível é posterior — deve resultar em `MOEDA_SEM_COTACAO`, nunca numa cotação futura usada por engano. |
| Reintroduzir constantes financeiras (`60`/`80`/`250`/`100`) em código de produção "temporariamente", sob pressão de prazo | Baixa mas real — é exatamente o padrão que `PoliticaReembolso.java` já tem hoje | DT-011/§5 são explícitos: toda política vem de arquivo; a fixture histórica (§16) é externa, nunca embutida. |

---

## 19. Impacto em arquivos

Nomes e pacotes reais verificados no repositório nesta sessão (`src/main/java/com/desafio/reembolso/...`). Nenhuma alteração foi feita — esta tabela é só levantamento para orientar `tasks.md`.

**Criados (novos, ainda não existem):**

| Arquivo planejado | Papel |
|---|---|
| `leitor/LeitorPolitica.java` | Lê e valida `--politica`, devolve `PoliticaExterna` (§5). |
| `leitor/LeitorCambio.java` | Lê e valida `--cambio`, devolve `TabelaCambio` (§7). |
| `modelo/PoliticaExterna.java` | Modelo imutável da política (§5). |
| `modelo/TabelaCategoria.java` | `limite` + `periodicidade` de uma categoria numa tabela. |
| `modelo/Periodicidade.java` | Enum fechado `DIA`/`DIARIA`. |
| `modelo/TabelaCambio.java` | Modelo imutável de cotações, já invertido para consulta eficiente (§7). |
| `modelo/TabelaPoliticaResolvida.java` | Modelo imutável devolvido por `ResolutorPoliticaCentroCusto.resolver(...)` (§6): `categorias`, `origem` (`PADRAO`/`CENTRO_CUSTO`), `nomeCentroCusto`. |
| `pipeline/ResolutorPoliticaCentroCusto.java` | API pública única: `resolver(String centroCusto, PoliticaExterna politica): TabelaPoliticaResolvida` (§6). |
| `pipeline/ResolutorCambio.java` | Estágio novo do pipeline: consome `ItemValidado.moeda` (já populado por `ValidadorItem`, não recalculado aqui) e enriquece `ItemValidado` apenas com `taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto`; grava `MOEDA_SEM_COTACAO` (`campo = CampoCanonico.MOEDA`) quando aplicável (§9). Nome e responsabilidade definitivos — não há alternativa em aberto para `tasks.md`. |
| `pipeline/AgregadorTetoIndividual.java` | Substitui `AgregadorTetoHospedagem.java` (ver "Substituídos" abaixo): processa qualquer categoria com `periodicidade: "diaria"`, não só `hospedagem` (§11, DT-017). |

**Substituídos:**

| Arquivo | Motivo |
|---|---|
| `modelo/PoliticaReembolso.java` | Estrutura de quatro constantes fixas (DT-007) — substituída por `PoliticaExterna` + `TabelaCategoria` (DT-011). Todo consumidor atual (`AvaliadorRegrasIndividuais`, `AgregadorTetoDiario`) precisa trocar a fonte do limite/gatilho. |
| `pipeline/AgregadorTetoHospedagem.java` | Exclusivo de `hospedagem` (nome e escopo fixos) — substituído por `pipeline/AgregadorTetoIndividual.java` (ver "Criados" acima), que processa qualquer categoria com `periodicidade: "diaria"`, escolhendo `TETO_HOSPEDAGEM_APLICADO` para `hospedagem` e `TETO_INDIVIDUAL_APLICADO` para as demais (§11, DT-017). |

**Estendidos (arquivo existente, lógica adicionada sem reescrita total):**

| Arquivo | Extensão |
|---|---|
| `Main.java` | Parser de argumentos para quatro flags (§3, DT-018); chamadas a `LeitorPolitica`/`LeitorCambio` antes de `ValidadorEnvelope`. |
| `modelo/ItemValidado.java` | Decisão fechada (§4, §9): ganha quatro campos — `moeda` (populado por `ValidadorItem`, junto com os sete campos já existentes), `taxaCambioAplicada`, `dataCotacaoUtilizada`, `valorConvertidoBruto` (estes três, e só estes três, populados por `ResolutorCambio`). Nenhuma estrutura intermediária alternativa é criada. |
| `pipeline/ValidadorItem.java` | Método `validarMoeda` (§8, DT-014) — popula `ItemValidado.moeda`; não popula os três campos de câmbio. |
| `pipeline/Normalizador.java` | Passa a normalizar **exclusivamente** sobre `valorConvertidoBruto` — o mesmo caminho para BRL e moeda estrangeira, sem `if` de BRL dentro do `Normalizador` (§9). |
| `pipeline/AvaliadorRegrasIndividuais.java` | RN-019 (categoria via `ResolutorPoliticaCentroCusto`, não conjunto fixo), RN-009 (gatilho de `PoliticaExterna`, não `PoliticaReembolso`), exclusão por `MOEDA_SEM_COTACAO`. |
| `pipeline/DetectorDuplicidadeEconomica.java` | Chave estendida com `moeda` (§12, CA-033). |
| `pipeline/AgregadorTetoDiario.java` | Generalização por `periodicidade` em vez de `Set<String>` fixo (§11, DT-017); limite vindo da tabela resolvida, não de `PoliticaReembolso`. |
| `pipeline/CompositorSaida.java` | Três campos novos no `ResultadoItem`; `ESTAGIO_POR_CODIGO`/`ORDEM_CAMPO` estendidos (§10, DT-019) — inclui `CampoCanonico.MOEDA` já disponível para o `campo` de `MOEDA_SEM_COTACAO`, sem exigir mecanismo novo além dos mapas já estendidos. |
| `escritor/EscritorResultado.java` | Serialização de `moeda`, `taxa_cambio_aplicada`, `data_cotacao_utilizada` (§9). |
| `modelo/MotivoCodigo.java` | Três valores novos (§10, DT-019). |
| `modelo/RegraNegocio.java` | Quatro valores novos (§10, DT-019). |
| `modelo/CampoCanonico.java` | Um valor novo, `MOEDA`, na posição correta (§10, DT-019). |

**Mantidos sem alteração:**

| Arquivo | Por quê |
|---|---|
| `leitor/ValidadorEnvelope.java` | RN-001 não muda na spec 1.2. |
| `modelo/Envelope.java` | `colaborador.centro_custo` já existe e já é preservado — só passa a ser *usado* por um componente novo (`ResolutorPoliticaCentroCusto`), não a mudar de forma. |
| `modelo/Decisao.java` | Vocabulário de decisão (4.4) não muda. |
| `pipeline/DetectorIdDuplicado.java` | RN-003 não muda. |
| `pipeline/SeletorElegiveis.java` | Mecanismo de seleção de elegíveis não muda — só passa a filtrar mais motivos de recusa possíveis. |
| `pipeline/SomadorTotal.java` | RN-018 não muda. |
| Estrutura `tests/java` / `tests/resources`, `pom.xml` (`testSourceDirectory`, `testResources`) | DT-009/DT-010 preservadas — nenhuma mudança de convenção de teste é exigida pela política v4. |

---

## 20. Item C — fora de escopo (confirmação técnica)

A fila de aprovação manual (item C do comunicado, `AGUARDANDO_APROVACAO` para reembolsável acima de R$500) permanece fora de escopo nesta revisão, confirmando `spec.md` §3/AMB-033: nenhum novo estado de decisão, nenhuma fila, nenhum serviço de aprovação e nenhuma task são planejados para ela nesta versão do plano. Caso venha a ser especificada em versão futura, exigirá pelo menos um novo valor em `Decisao` (hoje fechado em quatro valores) e um componente de fila — nenhum dos dois é antecipado ou esboçado aqui.

---

## 21. Preparação para tasks (blocos técnicos futuros, sem numeração)

`tasks.md` não é alterado nesta tarefa e continuará a numeração a partir de `T-022`. Os blocos abaixo descrevem, em nível técnico, o que cada task futura precisará cobrir — sem atribuir números, sem estimar commits e sem criar arquivo de teste algum:

- **CLI:** estender `Main.java` para quatro flags (§3, DT-018); testes de exit `2` para flag ausente/repetida/desconhecida e para política/câmbio inválidos.
- **Política externa:** `LeitorPolitica` + `PoliticaExterna` + `TabelaCategoria` + `Periodicidade` (§5, DT-011/DT-012); testes de estrutura válida/inválida, incluindo `limite: 0` em `padrao` vs. em `centros_custo`.
- **Câmbio externo:** `LeitorCambio` + `TabelaCambio` invertida (§7, DT-013); testes de cotação exata, fallback anterior, proibição de futura, `taxas: {}`.
- **Contrato do campo `moeda`:** `validarMoeda` em `ValidadorItem` (§8, DT-014); testes dos quatro casos (ausente, `null`, tipo inválido, formato inválido).
- **Conversão:** `ResolutorCambio`, estágio novo do pipeline entre detector de ID duplicado e normalizador (§9, DT-015) — consome `ItemValidado.moeda` já populado por `ValidadorItem`, sem recalculá-lo, e só acrescenta `taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto`; teste-canário real de ordem de arredondamento (`1.005 × 1.005`, §14) — não o exemplo funcional `40,00 × 5,50`.
- **Resolução por centro de custo:** `ResolutorPoliticaCentroCusto` (§6, DT-016); testes de centro cadastrado/desconhecido/ausente/nulo/tipo inválido e de comparação textual exata.
- **Periodicidade e tetos:** extensão de `AgregadorTetoDiario` e criação de `AgregadorTetoIndividual` em substituição a `AgregadorTetoHospedagem` (§11, DT-017); testes de categoria externa sob cada periodicidade.
- **Novos motivos e saída:** extensão de `MotivoCodigo`/`RegraNegocio`/`CampoCanonico`, `CompositorSaida`, `EscritorResultado` (§10, §4, DT-019); testes de ordem de apresentação com os motivos novos.
- **Regressões:** fixture histórica externa (§16) e os quatro cenários financeiros do envelope como testes de integração.
- **Integração:** teste ponta a ponta com `politica-v4.json` + `cambio.json` + os dois arquivos de despesas do envelope, comparando contra `§12` da spec.
- **Documentação:** atualização do `README.md` com o novo contrato de CLI de quatro flags (trabalho de task, não desta tarefa).

---

## 22. Estrutura física do projeto

Sem alteração em relação à v1.0 deste plano — `pom.xml`, `src/main/java`, `tests/java`, `tests/resources`, `target/`. Os dois arquivos de política/câmbio (`--politica`, `--cambio`) são argumentos de linha de comando, não arquivos de projeto fixos — nenhuma nova pasta de nível superior é exigida pela política v4. As fixtures de teste da política histórica e dos cenários do envelope (§16, §17) pertencem a `tests/resources/`, seguindo a mesma convenção já configurada em `pom.xml` (`testResources` apontando para `tests/resources`).
