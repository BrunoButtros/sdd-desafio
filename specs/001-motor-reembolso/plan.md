# Plano Técnico — Motor de Cálculo de Reembolso

**Versão:** 1.0 · **Baseado na spec:** 1.1

> Aqui mora o COMO. Este arquivo pode e deve falar de linguagem, biblioteca e
> arquitetura. O que ele **não** pode é introduzir regra de negócio nova — se
> apareceu uma, ela pertence à `spec.md`.

---

## 1. Stack

| Escolha | O quê | Por quê | O que descartei e por quê |
|---|---|---|---|
| Linguagem | Java 21 (LTS) | Familiaridade declarada de desenvolvedor back-end Java; JDK 21.0.2 já verificado funcionando nesta máquina sem instalação adicional; `java.math.BigDecimal` nativo resolve a exigência de aritmética decimal exata sem biblioteca externa. | Python e Node — nenhum dos dois está instalado nesta máquina (verificado via `python --version` e `node --version`); instalar agora consome prazo curto e adiciona risco de "funciona aqui, não funciona na correção". Kotlin — mesma JVM, mas introduziria sintaxe nova sob pressão de tempo sem ganho que a spec exija. |
| Build | Maven 3.9 ou superior | Maven 3.9.6 já verificado funcionando, casado ao JDK 21 instalado; convenção de projeto padrão para quem já é back-end Java. | Gradle — funcionalmente equivalente, mas sem motivo para trocar de ferramenta já dominada e já disponível. |
| Testes | JUnit 5 (Jupiter) | Parametrização nativa (`@ParameterizedTest`) essencial para as matrizes de fronteira monetária e de nota fiscal; integração direta com Maven Surefire, sem configuração extra. | TestNG — capacidade equivalente, sem motivo para introduzir dependência adicional. |
| Parsing/validação | Jackson Databind, lido via árvore `JsonNode` na camada de entrada | `JsonNode` permite inspecionar o tipo JSON bruto de cada campo (necessário para distinguir `CAMPO_AUSENTE`/`CAMPO_TIPO_INVALIDO`/`CAMPO_FORMATO_INVALIDO` conforme RN-002) e preservar `valor_informado` exatamente como recebido, inclusive quando o tipo é inválido. Suporta leitura de números como `BigDecimal` exato via configuração (ver §7). | Gson — exigiria a mesma configuração de números decimais, porém menos familiar ao perfil declarado. `org.json` — comportamento de conversão numérica para `BigDecimal` historicamente menos documentado/confiável. Parser JSON escrito à mão — risco desnecessário de bugs de parsing sob prazo de dois dias, sem ganho que a spec exija. |
| Aritmética monetária | `java.math.BigDecimal`, construído sempre a partir de texto/`decimalValue()`, nunca de `double` | É exatamente o mecanismo que garante `100.005 → 100,01` (RN-004): ponto flutuante binário representaria `100.005` como `100.00499999999999...` e arredondaria para o lado errado. Nativo do JDK, sem dependência extra. | `double`/`float` — fonte de bug previsível e documentada pelo próprio `CLAUDE.md`. Bibliotecas de dinheiro de terceiros (ex. Joda-Money) — desnecessárias; `BigDecimal` com escala e modo de arredondamento explícitos já cobre tudo que a spec exige. |
| Empacotamento | Maven Shade Plugin, produzindo um único JAR executável | Gera `target/motor-reembolso.jar` autocontido (dependências + `Main-Class` no manifesto), executável só com `java -jar`, sem exigir classpath externo na máquina do avaliador. | `maven-assembly-plugin` — resultado equivalente, Shade é mais direto para o caso de um único fat jar sem relocations. Spring Boot — framework pesado e servidor HTTP desnecessários para uma CLI que lê um arquivo e escreve outro. Wrapper `.sh`/`.bat` — descartado por decisão explícita: mais uma superfície para manter e testar em duas plataformas sem necessidade, já que `java -jar` funciona identicamente em qualquer sistema com JDK. |

---

## 2. Arquitetura

A arquitetura é um pipeline linear que segue **literalmente** os onze passos da seção 8.1 da spec. Cada passo é um estágio que recebe a lista de itens (na ordem da entrada) e devolve a mesma lista enriquecida — nenhum estágio reordena a lista mestra.

```
entrada JSON
  │
  ▼
[Leitor]  → JsonNode da árvore completa
  │
  ▼
[1] Validador de envelope (RN-001)              — fatal se falhar, nada mais executa
  │
  ▼
[2] Validador de item / classificador estrutural (RN-002)
  │      atribui indice_entrada (base 1, imutável) antes de qualquer validação
  ▼
[3] Detector de despesa.id duplicado (RN-003)
  │
  ▼
[4] Normalizador (RN-004 valor · RN-005 categoria)
  │
  ▼
[5] Avaliador de regras individuais (RN-006 · RN-007 · RN-008 · RN-009)
  │
  ▼
[6] Seletor de itens aprovados em todas as validações individuais
  │
  ▼
[7] Detector de duplicidade econômica (RN-010)
  │
  ▼
[8] Seletor de itens elegíveis após a duplicidade
  │
  ▼
[9] Agregador de tetos (RN-011 · RN-012 · RN-013 · RN-014 · RN-015)
  │
  ▼
[10] Compositor de saída — decisão final + motivos na ordem de apresentação (8.3)
  │
  ▼
[11] Somador do total (RN-018)
  │
  ▼
[Escritor] → JSON de saída
```

**Como a avaliação de regras funciona (evita parar no primeiro motivo, sem executar tudo indiscriminadamente):**

- Cada regra é avaliada quando **todos os campos de que ela depende** estão estruturalmente válidos, conforme a matriz 8.2 — não quando o item inteiro está livre de qualquer defeito.
- Um erro em um campo que a regra **não usa** não impede essa regra de rodar. Exemplo: `despesa.data` malformada não impede a avaliação de `NOTA_FISCAL_AUSENTE`, que depende só de `despesa.valor` e `despesa.tem_nota_fiscal`.
- Motivos aplicáveis são **acumulados** num único acumulador por item ao longo do pipeline — nenhuma etapa substitui ou descarta motivos de etapas anteriores.
- Somente as **exclusões expressamente listadas em 8.4** interrompem etapas posteriores (ex.: item com `ID_DUPLICADO` não entra na detecção de duplicidade econômica nem na agregação; item recusado nas validações individuais não entra na duplicidade econômica). Fora dessas exclusões fechadas, nenhuma outra é inferida.
- A **ordem de processamento** (8.1, quando cada regra roda) e a **ordem de apresentação dos motivos** (8.3, como a lista final de motivos de um item é ordenada antes de serializar) são duas ordens distintas. O acumulador guarda os motivos na ordem em que foram detectados; o compositor (passo 10) os reordena conforme 8.3 só no momento de montar a saída.

**Fronteiras:**

- **Núcleo puro** (passos 1 a 11, exceto leitura/escrita de arquivo): opera inteiramente sobre estruturas em memória. "Puro" aqui significa **sem leitura/escrita de arquivo, sem CLI, sem estado global e sem efeitos colaterais** — não significa ausência física do tipo `JsonNode` no modelo. O `JsonNode` de cada item pode acompanhar o item como fotografia de auditoria de `valor_informado` (ver §6) sem que isso quebre a pureza do núcleo.
- **I/O** (Leitor, Escritor, CLI/Main): isolado nas pontas do pipeline. O CLI/Main só orquestra — chama o leitor, invoca o núcleo, chama o escritor, e traduz o resultado (ou a falha) em código de saída (§3).

Essa separação entre regra de negócio e CLI/I/O é mantida deliberadamente para a versão atual, sem antecipar ou supor o conteúdo de qualquer mudança futura de requisito.

---

## 3. Contrato da CLI

```
java -jar target/motor-reembolso.jar calcular --input <arquivo> --output <arquivo>
```

**Códigos de saída:**

| Código | Significado |
|---|---|
| `0` | Processamento concluído e arquivo de resultado escrito em `--output`. |
| `2` | Erro de uso ou de infraestrutura: argumento ausente, arquivo de entrada inexistente, arquivo ilegível, JSON sintaticamente inválido, ou falha ao escrever `--output`. |
| `3` | JSON sintaticamente legível, mas envelope inválido conforme RN-001 (ex.: `periodo` ausente, `periodo.inicio` posterior a `periodo.fim`, `despesas` não é lista). |

Para os códigos `2` e `3`:

- uma mensagem curta em **texto simples** é escrita em **stderr** — não em stdout, e não como JSON estruturado. Não existe vocabulário JSON de erro nesta versão porque a spec não define um; inventar um agora seria vazar decisão de produto para o plano sem base na spec.
- nada é escrito em stdout;
- o arquivo indicado em `--output` **não é criado nem sobrescrito** — inclusive quando já existe um resultado anterior nesse caminho, ele permanece intacto.

**A validação do envelope acontece antes de qualquer escrita no caminho de `--output`.** O leitor primeiro faz o parsing sintático (falha → código `2`) e o validador de envelope roda em seguida (falha → código `3`), ambos antes de o processo tocar no destino. Só depois de o envelope ser confirmado válido o resultado completo é serializado para um **arquivo temporário no mesmo diretório do destino**; o destino em si só é tocado no passo final, por substituição atômica (ver DT-010). Uma falha nesse passo final — caminho não gravável, disco cheio — é infraestrutura, não regra de negócio, e também cai no código `2`, com o destino anterior preservado intacto.

---

## 4. Modelo de dados

Estruturas internas do núcleo (descrição de responsabilidade, não implementação):

| Estrutura | Conteúdo |
|---|---|
| **Item de entrada** | `indiceEntrada` (inteiro base 1, atribuído antes de qualquer validação, imutável) + `raw` (o `JsonNode` do elemento original, usado somente como fotografia de auditoria para `valor_informado`, nunca consultado por regra alguma). |
| **Campos estruturalmente validados** | Um mapa/estrutura com os sete campos canônicos de 4.2, populado apenas para os campos que passaram na validação de RN-002. Campo inválido fica ausente aqui — nenhuma coerção, nenhum valor padrão. |
| **`valor_informado`** | O valor JSON bruto de `despesa.valor` exatamente como recebido (número, texto, booleano, lista, objeto ou nulo), extraído do `JsonNode`. Nenhuma regra financeira o consulta — existe só para reaparecer na saída (4.3). |
| **`valor_normalizado`** | `BigDecimal` de escala 2, resultado de RN-004. Nulo quando `despesa.valor` não é um número estruturalmente válido. |
| **Categoria normalizada** | Texto resultante de RN-005 (trim, minúsculas, sem acento), produzido **sempre** que `despesa.categoria` é estruturalmente válido — inclusive quando o resultado é `coworking` ou qualquer outro valor fora da política. Nula **apenas** quando `despesa.categoria` é estruturalmente inválido, ausente ou nulo. RN-007 compara esse texto normalizado contra o conjunto fechado `alimentacao`/`transporte_urbano`/`hospedagem` e produz `CATEGORIA_FORA_POLITICA` quando não há correspondência — a normalização nunca converte uma categoria desconhecida em nulo; se convertesse, RN-007 não teria texto algum para comparar. |
| **Acumulador de motivos** | Lista de `Motivo`, que só cresce ao longo do pipeline — nenhuma etapa remove um motivo já acumulado por outra. Reordenada apenas no passo 10, conforme 8.3. |
| **`Motivo`** | Três campos, cada um representado por um tipo fechado (enumeração), não por texto livre espalhado pelo código: `codigo` (enumeração com os treze valores de 4.5 — `ITEM_TIPO_INVALIDO`, `CAMPO_AUSENTE`, `CAMPO_TIPO_INVALIDO`, `CAMPO_FORMATO_INVALIDO`, `ID_DUPLICADO`, `VALOR_NAO_POSITIVO`, `CATEGORIA_FORA_POLITICA`, `FORA_COMPETENCIA`, `NOTA_FISCAL_AUSENTE`, `DUPLICIDADE`, `TETO_DIARIO_APLICADO`, `TETO_DIARIO_ESGOTADO`, `TETO_HOSPEDAGEM_APLICADO`), `regra` (enumeração `RN_001`..`RN_018`, cada valor carregando o texto canônico `"RN-NNN"`), `campo` (enumeração dos sete nomes canônicos de 4.2, cada valor carregando o texto canônico `"despesa.<campo>"`, mais a possibilidade de nulo). A serialização de cada enum para o texto exigido pela spec é feita num único ponto por enum — não há string `"RN-004"` ou `"despesa.valor"` repetida em vários lugares do código. |
| **Decisão final** | Enumeração com os quatro valores de 4.4 (`INTEGRALMENTE_REEMBOLSADO`, `PARCIALMENTE_REEMBOLSADO`, `NAO_REEMBOLSADO_TETO_ESGOTADO`, `RECUSADO`). |
| **`valor_reembolsavel`** | `BigDecimal` de escala 2, sempre `0,00` para item recusado ou esgotado. |
| **Resultado por item** | Agrega `indiceEntrada`, `id` (ou nulo), `valor_informado`, `valor_normalizado`, `valor_reembolsavel`, decisão final e a lista de motivos já ordenada conforme 8.3. |
| **Resultado geral** | `colaborador` (três campos texto-ou-nulo), `periodo` (competência texto-ou-nulo, início e fim), a lista de resultados por item na ordem da entrada, e `total_reembolsavel` (RN-018). |

---

## 5. Como a política é representada

Uma única estrutura simples e imutável, `PoliticaReembolso`, com quatro valores fixos:

```
PoliticaReembolso {
  limiteDiarioAlimentacao        = 60.00
  limiteDiarioTransporteUrbano   = 80.00
  limiteIndividualHospedagem     = 250.00
  gatilhoNotaFiscal              = 100.00   // estritamente maior que
}
```

Instanciada uma vez, e passada por construtor comum aos componentes do núcleo que precisam desses valores (avaliador de regras individuais, agregador de tetos). Sem mecanismo genérico de regras, sem DSL, sem banco de dados, sem framework de injeção de dependência, sem arquivo externo de configuração. A política tem quatro números fixos definidos pela spec 1.1; mudá-los é editar uma estrutura, não uma feature. Construir generalidade para requisitos futuros desconhecidos seria exatamente o excesso de arquitetura que o FAQ do desafio adverte a evitar.

---

## 6. Fronteira entre Jackson e o núcleo de regras

- A camada de **entrada** (leitor) usa `JsonNode` para reconhecer o tipo JSON bruto de cada campo — é o que permite distinguir `CAMPO_AUSENTE` de `CAMPO_TIPO_INVALIDO` de `CAMPO_FORMATO_INVALIDO` (RN-002) e preservar `valor_informado` tal como recebido, inclusive quando o tipo é inválido.
- O valor monetário efetivamente usado por qualquer regra é sempre `BigDecimal` — nunca `JsonNode`, nunca `double`.
- O `JsonNode` do item pode acompanhar o item ao longo do modelo **somente** como a fotografia de auditoria que produz `valor_informado` na saída. Isso não compromete a pureza do núcleo (§2): pureza aqui é sobre I/O e efeitos colaterais, não sobre quais tipos aparecem no modelo de dados.
- **Nenhuma regra financeira consulta `valor_informado`.** As regras (RN-004 em diante) consultam exclusivamente os campos já validados e normalizados (`valor_normalizado`, categoria normalizada, etc.).
- Não será criada uma abstração genérica de "valor JSON" só para eliminar o tipo `JsonNode` do modelo de auditoria — seria complexidade extra sem necessidade, dado que o uso do `JsonNode` já está contido a um único propósito (auditoria) e nunca vaza para decisão financeira.

---

## 7. Estratégia monetária

- `ObjectMapper` configurado para ler números JSON como `BigDecimal` exato (habilitar `USE_BIG_DECIMAL_FOR_FLOATS` na leitura), de modo que a árvore (`JsonNode`) represente números decimais como `DecimalNode`/`BigDecimal`, nunca como `double`.
- Números são obtidos via `decimalValue()` — nunca via `doubleValue()`.
- Nunca construir um `BigDecimal` a partir de um `double` (isso reintroduziria o erro binário que a configuração acima existe para evitar).
- Normalização (RN-004): `valor.setScale(2, RoundingMode.HALF_UP)`.
- Toda comparação de valores monetários usa `compareTo` — nunca `equals` (que também compara escala) nem `==`.
- Na serialização de saída, valores monetários são escritos como **números JSON decimais em notação simples**, com exatamente duas casas — nunca em notação científica e nunca como string.
- **Testes obrigatórios de fronteira monetária** (detalhados na matriz de rastreabilidade, §9): `33.333` → `33,33`; `33.335` → `33,34`; `33.345` → `33,35`; `100.004` → `100,00`; `100.005` → `100,01`. Este último é o teste-canário que comprova que a leitura é decimal-exata e não passou por `double` em nenhum ponto do caminho.

---

## 8. Decisões técnicas

### DT-001 — Linguagem e ambiente de execução

**Contexto:** prazo de dois dias, desenvolvedor com familiaridade declarada em Java, ambiente desta máquina já inspecionado (Java 21.0.2 e Maven 3.9.6 funcionando; Python e Node ausentes).
**Decisão:** Java 21 como linguagem única do projeto.
**Alternativa descartada:** Python/Node — não instalados nesta máquina, custariam tempo de setup sob prazo curto; Kotlin — mesma JVM, mas sintaxe nova sem necessidade.
**Consequência:** compilar o projeto exige JDK 21 e Maven 3.9+ instalados na máquina; executar o JAR já compilado exige apenas um Java 21 (JRE ou JDK), porque o fat jar já contém as dependências. Na primeira compilação, o Maven pode precisar baixar dependências (Jackson, JUnit) de um repositório remoto — exige acesso à rede nessa primeira vez; builds subsequentes reaproveitam o cache local do Maven.

### DT-002 — Empacotamento em JAR único via Maven Shade Plugin

**Contexto:** o contrato de execução exige `java -jar target/motor-reembolso.jar ...` funcionando sem passos adicionais e sem wrapper de shell.
**Decisão:** configurar o Maven Shade Plugin para produzir, a partir de `mvn package`, exatamente `target/motor-reembolso.jar` — um único artefato contendo todas as dependências (Jackson) e o `Main-Class` no manifesto. Fixar `<finalName>motor-reembolso</finalName>` para que o nome do artefato não dependa da versão do projeto.
**Alternativa descartada:** `maven-assembly-plugin` (resultado equivalente, Shade é mais direto para este caso de fat jar simples sem relocations); wrapper `.sh`/`.bat` (descartado por instrução explícita — superfície de manutenção extra em duas plataformas sem necessidade, já que `java -jar` funciona igual em qualquer SO com JDK).
**Consequência:** um único comando de build (`mvn package`) e um único comando de execução, sem classpath manual, sem script adicional para manter ou testar.

### DT-003 — Contrato de CLI e códigos de saída

**Contexto:** a interface é fixa (`--input`/`--output`), mas a spec não define o que acontece na CLI quando o processamento não pode ocorrer — isso é contrato de execução, não regra de negócio.
**Decisão:** três códigos de saída (`0`, `2`, `3`), mensagem em texto simples em stderr para os códigos de erro, nenhuma escrita em `--output` quando o código não é `0`, e validação de envelope executada antes de qualquer abertura do arquivo de saída.
**Alternativa descartada:** vocabulário de erro em JSON estruturado (introduziria um esquema que a spec não define); escrever um JSON de erro no próprio `--output` (arriscaria confundir "resultado" com "estado de erro" no mesmo arquivo, e violaria a garantia de não sobrescrever `--output` em falha).
**Consequência:** contrato simples e verificável por teste de CLI; separa claramente "processamento não ocorreu" de "processamento ocorreu com itens recusados" — este último ainda é código `0`, porque recusa de item é resultado válido, não falha de processo.

### DT-004 — `BigDecimal` com parsing decimal-exato

**Contexto:** a spec exige `100.005 → 100,01`; ponto flutuante binário (`double`) representaria `100.005` como `100.00499999999999...` e arredondaria para o lado errado sob `HALF_UP`.
**Decisão:** `BigDecimal` de ponta a ponta, com `ObjectMapper` configurado para `USE_BIG_DECIMAL_FOR_FLOATS`, valores obtidos via `decimalValue()`, nunca via `doubleValue()` ou construção a partir de `double`.
**Alternativa descartada:** `double`/`float` (fonte de bug documentada); bibliotecas de dinheiro de terceiros (desnecessárias — `BigDecimal` nativo já cobre a exigência).
**Consequência:** os cinco valores de fronteira exigidos (33.333, 33.335, 33.345, 100.004, 100.005) arredondam corretamente por construção, não por sorte; qualquer regressão futura que reintroduza `double` em algum ponto do caminho é detectável pelo teste-canário de 100.005.

### DT-005 — Parsing por árvore (`JsonNode`) na camada de entrada

**Contexto:** RN-002 exige distinguir três classes de erro estrutural por campo (ausência, tipo, formato) e 4.3 exige preservar `valor_informado` exatamente como recebido, mesmo com tipo inválido — isso exige inspecionar o JSON bruto, não um objeto já desserializado e coagido.
**Decisão:** ler cada item como `JsonNode`, inspecionar `JsonNodeType` explicitamente por campo (nunca usar acessores permissivos como `asBoolean()`/`asInt()`, que fariam coerção que RN-002 proíbe), e só então produzir os campos validados e normalizados que o núcleo consome.
**Alternativa descartada:** desserialização direta para um objeto POJO tipado (perderia a distinção entre "campo ausente" e "campo de tipo errado", e perderia o valor bruto para `valor_informado` quando o tipo é inválido).
**Consequência:** classificação estrutural fiel à spec, ao custo de uma camada de leitura mais verbosa que uma desserialização direta.

### DT-006 — Arquitetura em pipeline linear seguindo a seção 8.1

**Contexto:** a ordem de processamento é normativa (seção 8 da spec) e distinta da ordem de apresentação de motivos (8.3); uma arquitetura que não espelhe isso arrisca produzir resultados corretos por acidente, não por construção.
**Decisão:** onze estágios lineares, um por passo de 8.1, cada um operando sobre a lista completa de itens sem jamais reordená-la; motivos acumulados por item ao longo dos estágios; reordenação para apresentação isolada no passo 10.
**Alternativa descartada:** um único método monolítico avaliando tudo por item em qualquer ordem interna conveniente — mais difícil de auditar linha a linha contra a seção 8, e mais fácil de violar sem perceber a regra "erros em campos não usados não impedem outras regras".
**Consequência:** cada estágio é testável isoladamente e mapeia 1:1 para uma linha da seção 8.1, o que facilita tanto a implementação quanto a auditoria da rastreabilidade.

### DT-007 — Representação da política como estrutura imutável simples

**Contexto:** a política tem quatro valores numéricos fixos nesta versão da spec.
**Decisão:** uma estrutura única e imutável, `PoliticaReembolso`, sem mecanismo de configuração externa.
**Alternativa descartada:** motor de regras genérico, DSL, arquivo de configuração externo, banco de dados, framework de injeção de dependência — todos resolveriam um problema de flexibilidade que a spec atual não tem.
**Consequência:** mudar um teto é uma edição de quatro linhas; em troca, qualquer flexibilidade não prevista pela spec 1.1 exigiria refatoração explícita, o que é aceitável porque não há evidência de que essa flexibilidade seja necessária agora.

### DT-008 — `Motivo` como três enumerações fechadas

**Contexto:** 4.5 define um vocabulário fechado para `codigo`, `regra` e `campo`; strings livres repetidas pelo código são uma fonte comum de divergência de grafia entre o motivo emitido e o exigido pela spec.
**Decisão:** três enumerações (`MotivoCodigo`, `RegraNegocio`, `CampoCanonico`), cada uma com o texto canônico correspondente definido em um único lugar.
**Alternativa descartada:** strings soltas (`"RN-004"`, `"despesa.valor"`) espalhadas pelas classes que emitem motivos — funciona, mas cada ocorrência é uma chance de erro de digitação não detectado por compilação.
**Consequência:** erro de grafia num código de motivo vira erro de compilação, não uma divergência silenciosa só visível em teste ou na correção.

### DT-009 — Estratégia de testes em três níveis

**Contexto:** a rubrica avalia rastreabilidade `spec → tasks → commits → testes`; cada RN e cada CA precisa de destino verificável.
**Decisão:** testes unitários por regra (maioria), poucos testes de integração de pipeline completo (o arquivo de exemplo e fixtures adicionais), e um teste de contrato/CLI cobrindo códigos de saída e comportamento de arquivo. Nomenclatura de classe/método referenciando o `RN-NNN` e o `CA-NNN` correspondentes. Todo teste — inclusive o de integração (`ExemploCompletoTest`) e os de contrato/CLI (`CliContratoTest`, `EscritaAtomicaSaidaTest`) — usa o sufixo `*Test`, reconhecido pelo Maven Surefire por padrão, para que `mvn test` execute a suíte inteira num único comando.
**Alternativa descartada:** cobertura só por teste de integração ponta a ponta — esconderia qual regra especificamente falhou e dificultaria o grep de rastreabilidade que a rubrica valoriza. Nomear o teste de integração com o sufixo `*IT` (convenção do Maven Failsafe) — exigiria configurar e invocar um plugin de build adicional (`mvn verify`) só por causa de nomenclatura, e o teste deixaria de rodar em `mvn test`, contrariando a simplicidade pedida.
**Consequência:** qualquer regra da spec é localizável no código de teste por busca textual do próprio identificador `RN-NNN` ou `CA-NNN`; `mvn test` sozinho executa toda a suíte, sem exigir um segundo comando ou plugin para os testes de integração/CLI; ver matriz completa em §9.

### DT-010 — Escrita atômica do arquivo de saída

**Contexto:** a garantia de que `--output` nunca é criado nem sobrescrito nos códigos `2` e `3` só é verdadeira se a escrita do resultado nunca tocar o destino antes de o resultado estar completo — escrever progressivamente e diretamente no destino deixaria um arquivo truncado no caminho oficial caso o processo falhe no meio da escrita.
**Decisão:** serializar o resultado completo para um arquivo temporário no mesmo diretório do destino (mesmo sistema de arquivos, condição para substituição atômica); fechar e concluir totalmente a escrita do temporário; só então mover/substituir o destino, preferindo `Files.move(temp, destino, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)`. Se o movimento/substituição falhar, o processo retorna código `2` e o destino anterior permanece intacto; o arquivo temporário é removido quando possível. O arquivo de destino nunca é aberto diretamente para escrever o JSON progressivamente.
**Alternativa descartada:** abrir `--output` diretamente e escrever o JSON incrementalmente — mais simples de codar, mas deixa uma janela em que uma falha no meio da escrita (processo interrompido, disco cheio) produz um arquivo parcialmente escrito exatamente no caminho que o resto do contrato promete preservar intacto.
**Consequência:** a garantia de não corromper `--output` passa a ser verdadeira por construção, não por sorte de nunca falhar no meio da escrita; o custo é uma etapa extra de escrita-e-movimentação em vez de uma escrita única, e a exigência de que o temporário fique no mesmo diretório do destino para que `ATOMIC_MOVE` seja viável na maioria dos sistemas de arquivos.

---

## 9. Estratégia de testes

- **Nível e proporção:** majoritariamente unitário (uma regra de negócio = um grupo de testes isolado, sem I/O real); um teste de integração de pipeline completo contra `exemplos/despesas-exemplo.json`; testes de contrato/CLI cobrindo códigos de saída, comportamento de stdout/stderr e a escrita atômica de `--output`.
- **Cada `RN-NNN` tem teste?** Garantido pela matriz de rastreabilidade abaixo — todo RN-001 a RN-018 aparece em pelo menos uma linha; conferência manual cruzada com a spec ao fechar o Dia 1, antes do envelope do Dia 2.
- **Casos de borda da seção 7:** cobertos pelos mesmos grupos de teste da matriz, via os `CA-NNN` correspondentes, que derivam diretamente da tabela de casos de borda.
- **Nomenclatura:** classe/grupo nomeado pelo identificador da regra (`RN004NormalizacaoMonetariaTest`); método nomeado pelo cenário e resultado esperado, carregando também o `CA-NNN` aplicável — por nome de método (`rn004_ca009_100_005_arredondaParaCima_100_01()`), `@DisplayName` (`"RN-004 / CA-009 — 100.005 arredonda para 100,01"`) ou comentário imediatamente acima do caso. O objetivo é permitir busca textual direta tanto por `RN-004` quanto por `CA-009` e chegar ao mesmo teste.
- **Fixture de saída esperada:** o fixture usado por `ExemploCompletoTest` é **escrito e revisado manualmente** a partir do schema completo das seções 4.3 a 4.5 da spec — nunca gerado pelo próprio motor em teste, o que tornaria o teste circular. Usa a tabela 4.7 como fonte de decisões e valores, e RN-017 mais a ordem de 8.3 para montar o objeto completo de cada motivo (`codigo`, `regra`, `campo`) — a tabela 4.7 é uma representação abreviada (só o `codigo`) e não basta sozinha para montar o fixture. O fixture contém metadados do envelope, `valor_informado`, `valor_normalizado`, decisão, motivos completos e `total_reembolsavel`. A comparação é **estrutural** (JSON contra JSON, campo a campo), nunca textual — não depende de espaços, indentação ou ordem de chaves na serialização.
- **Comando único de execução:** `mvn test` executa **todos** os testes planejados desta matriz, inclusive o de integração (`ExemploCompletoTest`) e os de contrato/CLI (`CliContratoTest`, `EscritaAtomicaSaidaTest`) — todos seguem o sufixo `*Test`, reconhecido pelo Maven Surefire por padrão, em vez do sufixo `*IT` (que exigiria o Maven Failsafe e uma fase de build adicional só por causa de nomenclatura). `mvn package` executa esses mesmos testes antes de gerar o JAR, porque a fase `test` precede `package` no ciclo de vida padrão do Maven; pular essa etapa exige a flag explícita e não recomendada `-DskipTests`.

### Matriz de rastreabilidade

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
| CA-001 · CA-002 · CA-003 (integral) | `ExemploCompletoTest` — os 14 itens de `exemplos/despesas-exemplo.json` processados de ponta a ponta, comparados estruturalmente contra o fixture esperado descrito acima (escrito à mão a partir de 4.3–4.5, não gerado pelo motor); `total_reembolsavel` = R$ 585,43 | Integração (executado por `mvn test`) |
| — (contrato de execução) | `CliContratoTest` — código `0` em sucesso; código `2` para argumento ausente/arquivo inexistente/JSON sintaticamente inválido/falha de escrita; código `3` para envelope inválido; mensagem em stderr, nada em stdout | Contrato/CLI |
| — (escrita atômica de `--output`, DT-010) | `EscritaAtomicaSaidaTest` — envelope inválido não altera um arquivo preexistente em `--output`; JSON sintaticamente inválido não altera um arquivo preexistente; falha simulada antes da substituição final não altera um arquivo preexistente; sucesso substitui o destino pelo resultado completo; nenhum arquivo temporário/parcial permanece no caminho oficial de `--output` após qualquer cenário | Contrato/CLI |

Todo identificador de RN-001 a RN-018 e de CA-001 a CA-023 aparece em pelo menos uma linha desta matriz.

---

## 10. Riscos

| Risco | Probabilidade | O que faço se acontecer |
|---|---|---|
| Parsing de número via `double` reintroduzido em algum ponto (ex. troca de biblioteca, refactor apressado) | Média | Teste-canário de `100.005` (RN-004) falha imediatamente e aponta o ponto exato da regressão. |
| Coerção implícita de tipo pelo Jackson (`asBoolean()`/`asInt()` em vez de checagem explícita de `JsonNodeType`) | Média | Revisão de código restrita a essa camada de leitura antes de fechar RN-002; teste dedicado por campo cobrindo booleano-como-número e o inverso. |
| Regra parando no primeiro motivo encontrado (short-circuit indevido) | Média | `OrdemMotivosTest` e os testes de RN-002 com múltiplos motivos (`CA-021`, `CA-023`) capturam isso diretamente. |
| Excesso de arquitetura (motor de regras genérico, configuração externa) sob tentação de "preparar para o Dia 2" | Baixa mas real — o FAQ do desafio avisa explicitamente contra isso | Manter `PoliticaReembolso` simples (§5) e a arquitetura de pipeline linear (§2); qualquer generalização é adicionada só quando um requisito real a exigir, nunca antes. |
| Mudança de requisito do Dia 2 exigir tocar código de I/O além do núcleo | Desconhecida — não antecipada nesta versão | A fronteira núcleo/CLI (§2) já isola regra de negócio de I/O; se a mudança for de regra, o núcleo absorve; se for de contrato de execução, o CLI absorve. Nenhum conteúdo do Dia 2 é suposto aqui. |
| Teste de integração não ser descoberto pelo Maven por nomenclatura inadequada (ex. sufixo `*IT` sem o Maven Failsafe configurado) | Média — foi um problema real encontrado na auditoria deste plano | Todo teste, inclusive integração e CLI, usa o sufixo `*Test` (DT-009/DT-010); `mvn test` sozinho é o comando de verificação antes de qualquer commit, nunca um plugin adicional só por nomenclatura. |
| Escrita direta e progressiva em `--output` corromper ou truncar o arquivo em caso de falha no meio da serialização | Média | Escrever sempre em arquivo temporário no mesmo diretório do destino e mover atomicamente (DT-010); o destino nunca é aberto diretamente para escrita incremental. |
| Categoria estruturalmente válida, mas fora do vocabulário fechado, ser descartada (virar nula) antes de RN-007 conseguir compará-la | Média — foi um erro real encontrado na auditoria deste plano | Modelo corrigido em §4: a categoria normalizada é sempre produzida quando o campo é estruturalmente válido, inclusive quando o resultado é `coworking` ou qualquer outro valor fora da política; só RN-007 decide `CATEGORIA_FORA_POLITICA`. |

---

## 11. Estrutura física do projeto

Documentada aqui para respeitar a estrutura de entrega exigida pelo `DESAFIO.md` (pasta de nível superior `tests/`, e não a convenção padrão do Maven `src/test/java`). **Os diretórios não são criados nesta etapa** — isto é só planejamento.

```
sdd-desafio/
├── pom.xml
├── src/
│   └── main/
│       └── java/...        # código de produção
├── tests/
│   ├── java/...             # testes Java (JUnit 5) — RN*Test, CA*Test, ExemploCompletoTest, CliContratoTest, EscritaAtomicaSaidaTest
│   └── resources/...        # fixtures de teste, incluindo o fixture esperado do exemplo completo
└── target/                  # artefatos gerados pelo Maven — nunca versionado, nunca escrito à mão
```

Como `tests/` não é o caminho padrão reconhecido pelo Maven, o `pom.xml` precisa apontar explicitamente para ele:

- `<build><testSourceDirectory>tests/java</testSourceDirectory></build>` — direciona o compilador de testes para `tests/java` em vez do padrão `src/test/java`.
- `<build><testResources><testResource><directory>tests/resources</directory></testResource></testResources></build>` — direciona os recursos de teste (fixtures) para `tests/resources`.

Com essa configuração, `mvn test` continua funcionando exatamente como esperado — compila e executa tudo em `tests/java`, com `tests/resources` no classpath de teste — sem exigir que os testes fiquem em `src/test/java`. Essa é também a razão pela qual nenhum teste desta spec depende do caminho padrão do Maven para ser descoberto: a descoberta depende só do sufixo `*Test` (via Surefire) e do `testSourceDirectory` configurado, não da localização convencional.
