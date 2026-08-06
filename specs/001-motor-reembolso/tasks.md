# Tasks — Motor de Cálculo de Reembolso

> Cada task é pequena o bastante para virar **um commit**. Se você não consegue
> descrever o critério de aceite como "o teste X passa", a task está grande demais.
>
> Marque `[x]` conforme conclui — ao longo do caminho, não tudo no fim. O histórico
> de quando cada task foi marcada é lido na correção.

**Versão:** 1.1 · **Status:** aprovado · **Última alteração:** 2026-08-05
**Baseado em:** spec `1.2` (aprovada) · plan `1.1` (aprovado)
**Total de tasks:** 58 (`T-001` a `T-058`) — `T-001` a `T-021` concluídas (Dia 1, spec 1.1/plan 1.0); `T-022` a `T-058` pendentes (Dia 2, política v4, spec 1.2/plan 1.1).
**Estimativa de commits — Dia 1:** 22 (já realizados, ver histórico abaixo). **Dia 2:** 37 (uma task, um commit — nenhuma exceção nesta rodada).

---

## Regra geral de sessão e commit

Vale para toda task deste arquivo, sem exceção:

1. Trabalhar em **uma task por vez** — não abrir a task seguinte antes de a atual estar concluída.
2. Rodar o **teste específico da task** (`mvn test -Dtest=<Classe>`) antes de considerar a task pronta.
3. Rodar `mvn test` (suíte completa) antes de qualquer commit — não só o teste da task.
4. Revisar `git diff` e `git status` antes de `git add` — conferir que só os arquivos previstos na task foram tocados.
5. Gerar o `/export` da(s) sessão(ões) que executaram a task. Se a task exigiu mais de uma sessão de trabalho — inclusive uma sessão de auditoria ou correção sobre a própria task —, gerar **todos** os exports pendentes dessas sessões antes de prosseguir.
6. Quando tudo estiver aprovado — testes verdes, critérios de aceite atendidos, `git diff` revisado, export(s) já gerado(s) —, **imediatamente antes de `git add`**, alterar simultaneamente na task:
   - o checkbox do título, de `[ ]` para `[x]`;
   - a linha **Status** da mesma task, de `[ ] pendente` para `[x] concluída`.
7. Incluir no **mesmo commit** da task: a implementação/documentação da task, a mudança de status acima, e o(s) export(s) da(s) sessão(ões) que a executaram. Os exports de uma task pertencem exclusivamente ao commit dela — não há postergação para outro commit.
8. Executar `git add` e `git commit`.
9. Toda mensagem de commit referencia `T-NNN`, no formato `tipo(T-NNN): descrição` — exceto commits de documentação normativa, que usam `docs(spec):`, `docs(plan):`, `docs(tasks):`, `docs(readme):` com `[T-NNN]` citado no corpo da mensagem.
10. A task só é **oficialmente concluída** depois que o commit for criado com sucesso. Se o commit falhar (ex.: hook rejeitando o conteúdo), corrigir o problema e tentar novamente — sem iniciar a task seguinte.
11. **Não iniciar a task seguinte enquanto os exports da task atual não estiverem versionados** no commit dela.

**Regra de commit único por task:** implementação e os testes que a acompanham entram **no mesmo commit**, e somente depois de `mvn test` passar por inteiro — nunca um commit `feat` seguido de um commit `test` seco seguido. Não se commitam testes falhando. `T-002` é a única exceção autorizada (dois commits, ver task). Não criar commits artificiais só para engordar o `git log`.

**Qualificação específica para `T-022` em diante (Dia 2):** tasks explicitamente dedicadas a testes, regressão, auditoria ou verificação manual (ex.: T-031, T-033, T-039, T-045, T-050, T-054, T-058) podem produzir um commit de teste/documentação sem alteração de código de produção — isso **não** é um "teste seco" omitido da task anterior, porque cada uma dessas tasks cobre um comportamento, uma fronteira ou uma evidência própria, declarada neste backlog, que a task anterior não cobria. Caso uma dessas tasks encontre um defeito real durante a escrita dos testes, código e teste entram juntos no **único** commit daquela task, e a mensagem muda de `test(T-NNN)` para `fix(T-NNN)` — nunca um commit de correção seguido por outro commit de teste para a mesma task (essa possibilidade, aberta para `T-015` na v1.0 deste arquivo, não se repete para nenhuma task do Dia 2). Toda task desta fase, inclusive as de verificação manual, gera **exatamente um commit**, sem exceção — nunca "task concluída sem commit" —, contendo no mínimo: a atualização do checkbox/status da task; o export da sessão que a executou; e qualquer evidência ou arquivo alterado aplicável ao escopo dela.

---

## Fase 0 — Baseline

- [x] **T-001** — Consolidar a baseline SDD
  - **O que faz:** primeiro commit real de trabalho do repositório. Registra, num único commit documental, a spec 1.1 já aprovada, o plan 1.0 já aprovado, este `tasks.md` completo e as quatro sessões exportadas até aqui (incluindo a que gerou este próprio arquivo).
  - **Requisitos atendidos (RN/CA/DT):** nenhum diretamente — é consolidação documental, não regra de negócio. É o marco zero sobre o qual toda a rastreabilidade das tasks seguintes se apoia.
  - **Seções do plan:** nenhuma (task fora do escopo técnico do plan).
  - **Dependências:** nenhuma.
  - **Arquivos que cria/modifica:**
    - `specs/001-motor-reembolso/spec.md`
    - `specs/001-motor-reembolso/DECISIONS.md`
    - `specs/001-motor-reembolso/plan.md`
    - `specs/001-motor-reembolso/tasks.md`
    - `docs/sessions/01-validacao-export.md`
    - `docs/sessions/02-especificacao-inicial.md`
    - `docs/sessions/03-planejamento.md`
    - `docs/sessions/04-tarefas.md` — **ainda não existe no momento em que este `tasks.md` está sendo escrito**; será gerado via `/export` ao final desta sessão de planejamento, antes do commit de `T-001`. Isso é esperado, não uma pendência.
  - **Testes:** nenhum automatizado — task documental.
  - **Critério de aceite:**
    - spec 1.1 aprovada;
    - plan 1.0 aprovado;
    - `tasks.md` completo, sem placeholders `<...>`;
    - exports `01` a `04` presentes em `docs/sessions/` e nenhum deles vazio;
    - nenhum código, `pom.xml`, `src/` ou `tests/` criado nesta task;
    - documentos normativos sem placeholders remanescentes;
    - `git diff` revisado manualmente antes do `git add`;
    - staging contém **somente** os oito arquivos documentais listados acima — nenhum outro arquivo.
  - **Comandos de verificação:**
    ```
    git diff --check -- specs/001-motor-reembolso/spec.md specs/001-motor-reembolso/DECISIONS.md specs/001-motor-reembolso/plan.md specs/001-motor-reembolso/tasks.md
    Get-ChildItem .\docs\sessions\*.md | Select-Object Name, Length, LastWriteTime
    git status --short
    git diff --stat -- specs/001-motor-reembolso/ docs/sessions/
    ```
    Os **quatro documentos normativos** (`spec.md`, `DECISIONS.md`, `plan.md`, `tasks.md`) devem passar em `git diff --check` sem erro real. `git diff --check` **não** é executado sobre `docs/sessions/`: exports são evidência bruta da conversa com o Claude Code e podem conter espaço em branco produzido pela própria ferramenta — isso não é um defeito a corrigir. Os exports são verificados apenas por **presença** e por **tamanho maior que zero** (`Get-ChildItem ... Length`), nunca reformatados ou "limpos" manualmente só para satisfazer estilo.
  - **Commit:** `docs(tasks): [T-001] consolida baseline SDD e backlog executavel` (1 commit).
  - **Status:** [x] concluída.

---

## Fase 1 — Fundação técnica

- [x] **T-002** — Estrutura Maven, empacotamento e CLI básica
  - **O que faz:** projeto Java 21/Maven compila e empacota em `target/motor-reembolso.jar` via `mvn package`; a CLI reconhece `--input`/`--output`, lê o arquivo e recusa corretamente uso/infraestrutura inválidos (argumento ausente, arquivo inexistente, JSON sintaticamente inválido) com exit `2` — sem ainda validar nenhuma regra de negócio.
  - **Requisitos atendidos (RN/CA/DT):** nenhuma RN/CA nova (contrato de execução, não regra de negócio).
  - **DT/seções do plan:** DT-001 (linguagem e ambiente), DT-002 (empacotamento via Shade Plugin), DT-003 (parcial — apenas exit `2`), DT-009 (convenção `*Test` adotada desde o primeiro teste).
  - **Dependências:** T-001.
  - **Arquivos que cria/modifica:**
    - `pom.xml` (Shade Plugin, `<finalName>motor-reembolso</finalName>`, `testSourceDirectory=tests/java`, `testResources` apontando para `tests/resources`)
    - `src/main/java/.../Main.java` (parsing de argumentos, leitura de arquivo, `ObjectMapper` com parsing sintático JSON)
    - `tests/java/.../CliContratoTest.java` (casos iniciais de exit `2`)
  - **Testes:** `CliContratoTest` — argumento ausente; arquivo de entrada inexistente; JSON sintaticamente inválido. Todos: exit `2`, mensagem em stderr, nada em stdout, `--output` não criado.
  - **Critério de aceite:** `mvn package` produz exatamente `target/motor-reembolso.jar`; os três cenários acima retornam exit `2` sem criar `--output`; `mvn test` verde.
  - **Comandos de verificação:**
    ```
    mvn test
    mvn package
    ```
  - **Commits (exceção autorizada — únicos 2 commits deste backlog):**
    - `chore(T-002): configura Maven e estrutura de testes`
    - `feat(T-002): adiciona CLI basica e erros de uso com testes`
  - **Status:** [x] concluída

- [x] **T-003** — Vocabulários fechados e modelo de domínio
  - **O que faz:** cria as enumerações fechadas `MotivoCodigo`, `RegraNegocio`, `CampoCanonico` e `Decisao`, cada uma serializando para o texto canônico exigido pela spec (4.4/4.5) num único ponto por enum.
  - **Requisitos atendidos:** nenhuma RN/CA diretamente — é a infraestrutura de vocabulário que RN-002 em diante consome.
  - **DT/seções do plan:** DT-008; plan §4 (Modelo de dados) e §6.
  - **Dependências:** T-002.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../modelo/MotivoCodigo.java`
    - `src/main/java/.../modelo/RegraNegocio.java`
    - `src/main/java/.../modelo/CampoCanonico.java`
    - `src/main/java/.../modelo/Decisao.java`
    - `tests/java/.../VocabularioMotivoTest.java`
  - **Testes:** `VocabularioMotivoTest` — cada valor de `MotivoCodigo` serializa para o texto exato de 4.5; cada `RegraNegocio` para `"RN-NNN"`; cada `CampoCanonico` para `"despesa.<campo>"`.
  - **Critério de aceite:** `VocabularioMotivoTest` passa. Verificação de string canônica limitada **exclusivamente a `src/main/java`**: em código de produção, textos como `"RN-004"` ou `"despesa.valor"` não podem estar espalhados fora dos enums acima — cada um existe em um único ponto (o enum correspondente). Essa restrição **não** se aplica a `tests/` nem a fixtures: strings iguais a essas aparecerão legitimamente ali como valor esperado do teste, e isso é esperado, não uma violação.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=VocabularioMotivoTest
    ```
  - **Commit:** `feat(T-003): implementa vocabularios fechados com testes` (1 commit).
  - **Status:** [x] concluída

---

## Fase 2 — Envelope e contrato do item

- [x] **T-004** — Validação do envelope (RN-001) e metadados opcionais
  - **O que faz:** arquivo com envelope inválido (4.1) é recusado com exit `3`, sem escrever nada em `--output`; envelope válido segue com `colaborador` e `periodo.competencia` tolerantes, conforme a tabela de tolerância de 4.1.
  - **RN atendidas:** RN-001.
  - **CA atendidos:** CA-020.
  - **DT/seções do plan:** DT-003 (introdução do exit `3`), DT-004 (setup do `ObjectMapper` com `USE_BIG_DECIMAL_FOR_FLOATS`, usado a partir daqui em diante), DT-005 (leitura via `JsonNode`); plan §3, §4.1.
  - **Dependências:** T-002.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../leitor/ValidadorEnvelope.java`
    - `src/main/java/.../modelo/Envelope.java` (metadados de `colaborador` e `periodo`)
    - `tests/java/.../EnvelopeValidoTest.java`
  - **Testes:** `EnvelopeValidoTest` — `periodo.inicio` posterior a `periodo.fim` (exit `3`, `--output` não criado); `despesas: []` (resultado vazio, total `0,00`); `colaborador` recebido como texto (arquivo válido, três metadados nulos na saída).
  - **Critério de aceite:** os três cenários de `EnvelopeValidoTest` passam; exit `3` é distinto de exit `2` (T-002) e de exit `0` (ainda não implementado).
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=EnvelopeValidoTest
    ```
  - **Commit:** `feat(T-004): valida envelope e metadados opcionais com testes` (1 commit).
  - **Status:** [x] concluída

- [x] **T-005** — Contrato estrutural do item (RN-002) e preservação de `valor_informado`
  - **O que faz:** cada elemento de `despesas` vira um registro estruturalmente validado: `indice_entrada` atribuído (base 1, antes de qualquer validação, imutável); classificação fechada entre `CAMPO_AUSENTE`, `CAMPO_TIPO_INVALIDO` e `CAMPO_FORMATO_INVALIDO` por campo, conforme a regra fechada de 4.2; `ITEM_TIPO_INVALIDO` como motivo único quando o elemento não é objeto; motivos estruturais múltiplos emitidos na ordem canônica de contrato (id, data, categoria, descricao, fornecedor, valor, tem_nota_fiscal); `valor_informado` preservado exatamente como recebido, inclusive quando o tipo é inválido.
  - **RN atendidas:** RN-002.
  - **CA atendidos:** CA-021, CA-022, CA-023.
  - **DT/seções do plan:** DT-005 (parsing por árvore, sem coerção via acessores permissivos); plan §4 ("Item de entrada", "Campos estruturalmente validados"), §6.
  - **Dependências:** T-003 (enums de motivo/campo), T-004 (envelope validado, `indice_entrada` só faz sentido dentro de um arquivo já processável).
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/ValidadorItem.java`
    - `src/main/java/.../modelo/ItemValidado.java`
    - `tests/java/.../ContratoDoItemTest.java`
    - `tests/java/.../ValorInformadoTest.java`
  - **Testes:**
    - `ContratoDoItemTest` — `data: "31/07/2026"` + `valor: "72,50"` → dois motivos, nesta ordem (`CAMPO_FORMATO_INVALIDO`/`despesa.data`, depois `CAMPO_TIPO_INVALIDO`/`despesa.valor`); `categoria` como número → `CAMPO_TIPO_INVALIDO`; `id` como texto vazio → `CAMPO_FORMATO_INVALIDO`; elemento de `despesas` que não é objeto → `ITEM_TIPO_INVALIDO` único, `campo` nulo; cenário completo de CA-023 (cinco campos malformados, cinco motivos na ordem canônica).
    - `ValorInformadoTest` — preservação do valor bruto para string, booleano, valor ausente/nulo e elemento não-objeto.
  - **Critério de aceite:** `ContratoDoItemTest` e `ValorInformadoTest` verdes; nenhum short-circuit — um item com cinco campos malformados produz cinco motivos, na ordem canônica exigida por CA-023.
  - **Comandos de verificação:**
    ```
    mvn test "-Dtest=ContratoDoItemTest,ValorInformadoTest"
    ```
  - **Commit:** `feat(T-005): valida contrato estrutural dos itens com testes` (1 commit).
  - **Status:** [x] concluída

---

## Fase 3 — Regras individuais de elegibilidade

- [x] **T-006** — Unicidade de `despesa.id` (RN-003)
  - **O que faz:** todas as ocorrências de um `despesa.id` estruturalmente válido e repetido são recusadas com `ID_DUPLICADO` — sem preservar "primeira ocorrência". ID estruturalmente inválido não participa da verificação.
  - **RN atendidas:** RN-003.
  - **CA atendidos:** CA-019.
  - **DT/seções do plan:** plan §8.2 (linha "Unicidade de `despesa.id`" da matriz de dependências, adicionada na auditoria da spec 1.1).
  - **Dependências:** T-005.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/DetectorIdDuplicado.java`
    - `tests/java/.../IdDuplicadoTest.java`
  - **Testes:** `IdDuplicadoTest` — três itens com `"d-100"` → todos três recusados com `ID_DUPLICADO`; um `id` estruturalmente inválido (ex.: texto vazio) não entra na verificação de repetição.
  - **Critério de aceite:** `IdDuplicadoTest` verde; nenhuma ocorrência é preservada como "primeira" — todas recebem o motivo.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=IdDuplicadoTest
    ```
  - **Commit:** `feat(T-006): recusa todas as ocorrencias de id duplicado com testes` (1 commit).
  - **Status:** [x] concluída

- [x] **T-007** — Normalização monetária e de categoria (RN-004, RN-005)
  - **O que faz:** todo `despesa.valor` estruturalmente válido normaliza para duas casas decimais com arredondamento `HALF_UP`; toda `despesa.categoria` estruturalmente válida normaliza por trim + insensibilidade a caixa/acento, e o resultado é **sempre produzido** — inclusive quando fica fora do vocabulário fechado (`coworking` normaliza para `coworking`, não para nulo), para que RN-007 (T-009) tenha texto para comparar.
  - **RN atendidas:** RN-004, RN-005.
  - **CA atendidos:** CA-009 (parcial — a fronteira de arredondamento monetário; a parte que envolve nota fiscal fecha em T-011), CA-015, CA-018.
  - **DT/seções do plan:** DT-004 (aplicação de `setScale(2, RoundingMode.HALF_UP)`, comparação via `compareTo`); plan §4 (correção da auditoria: "categoria normalizada... nunca converte categoria desconhecida em nulo").
  - **Dependências:** T-005 (campos estruturalmente validados disponíveis).
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/Normalizador.java`
    - `tests/java/.../NormalizacaoMonetariaTest.java`
    - `tests/java/.../NormalizacaoCategoriaTest.java`
  - **Testes:**
    - `NormalizacaoMonetariaTest` (parametrizado) — `33.333→33,33`; `33.335→33,34`; `33.345→33,35`; `100.004→100,00`; `100.005→100,01` (teste-canário decimal-exato).
    - `NormalizacaoCategoriaTest` — `ALIMENTACAO`, `Alimentação`, ` alimentacao ` → `alimentacao`; `transporte urbano` não reconhece `transporte_urbano` **mas não vira nulo**.
  - **Critério de aceite:** ambos os testes verdes, incluindo o teste-canário `100.005`.
  - **Comandos de verificação:**
    ```
    mvn test "-Dtest=NormalizacaoMonetariaTest,NormalizacaoCategoriaTest"
    ```
  - **Commit:** `feat(T-007): normaliza valores e categorias com testes de fronteira` (1 commit).
  - **Status:** [x] concluída

- [x] **T-008** — Valor não positivo (RN-006)
  - **O que faz:** item cujo valor normalizado seja menor ou igual a zero é recusado com `VALOR_NAO_POSITIVO` e fica marcado inelegível para as etapas seguintes (duplicidade econômica e agregação de tetos, ainda não implementadas nesta task).
  - **RN atendidas:** RN-006.
  - **CA atendidos:** CA-017 — **apenas a parte verificável nesta task**: recusa com `VALOR_NAO_POSITIVO` e `valor_reembolsavel` `0,00`. A parte de CA-017 que afirma que **o total do período não é reduzido** não é verificável aqui porque o total (`RN-018`) só existe a partir de `T-017`; essa parte do critério é reexercida em `T-017` (unidade) e confirmada ponta a ponta em `T-020` (integração).
  - **DT/seções do plan:** plan §8.2 (dependência: `despesa.valor`).
  - **Dependências:** T-007.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/AvaliadorRegrasIndividuais.java` (início da classe)
    - `tests/java/.../ValorNaoPositivoTest.java`
  - **Testes:** `ValorNaoPositivoTest` — `-45.00` recusado com `VALOR_NAO_POSITIVO`, `valor_reembolsavel` `0,00`; item marcado como não elegível para as etapas seguintes do pipeline (verificado por não participar de uma população de itens elegíveis simulada no teste). **Não** exige soma de período nem execução do agregador de tetos.
  - **Critério de aceite:** `ValorNaoPositivoTest` verde nos dois aspectos acima; nenhuma asserção sobre total do período nesta task.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=ValorNaoPositivoTest
    ```
  - **Commit:** `feat(T-008): recusa valores nao positivos com testes` (1 commit).
  - **Status:** [x] concluída

- [x] **T-009** — Categoria fora da política (RN-007)
  - **O que faz:** categoria normalizada fora do conjunto fechado (`alimentacao`, `transporte_urbano`, `hospedagem`) é recusada com `CATEGORIA_FORA_POLITICA` e marcada inelegível para a agregação de tetos.
  - **RN atendidas:** RN-007.
  - **CA atendidos:** CA-016 — **apenas a parte verificável nesta task**: recusa com `CATEGORIA_FORA_POLITICA` e `valor_reembolsavel` `0,00`, e marcação do item como inelegível para agregação. Esta task **não** exige motivo de teto nem execução real do agregador — o agregador de tetos ainda não existe (é criado em `T-013`/`T-014`). A confirmação ponta a ponta de que o item de fato não alcança a etapa de teto fica em `T-020`.
  - **Dependências:** T-007.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/AvaliadorRegrasIndividuais.java` (mesma classe de T-008)
    - `tests/java/.../CategoriaForaPoliticaTest.java`
  - **Testes:** `CategoriaForaPoliticaTest` — `coworking` de R$ 89,00 com nota fiscal, recusado, `valor_reembolsavel` `0,00`, marcado como inelegível para a etapa de agregação (sem invocar um agregador real, que ainda não existe).
  - **Critério de aceite:** `CategoriaForaPoliticaTest` verde; nenhuma dependência de código do agregador de tetos.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=CategoriaForaPoliticaTest
    ```
  - **Commit:** `feat(T-009): recusa categorias fora da politica com testes` (1 commit).
  - **Status:** [x] concluída

- [x] **T-010** — Elegibilidade temporal (RN-008)
  - **O que faz:** item com `data` fora de `[periodo.inicio, periodo.fim]` é recusado com `FORA_COMPETENCIA`; ambas as bordas são inclusivas.
  - **RN atendidas:** RN-008.
  - **CA atendidos:** CA-011, CA-012.
  - **Dependências:** T-004 (período do envelope disponível), T-005 (campo `data` estruturalmente validado).
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/AvaliadorRegrasIndividuais.java` (mesma classe de T-008/T-009)
    - `tests/java/.../CompetenciaTest.java`
  - **Testes:** `CompetenciaTest` — `2026-04-15` fora da janela de julho → `FORA_COMPETENCIA`; `data` igual a `periodo.inicio` e `data` igual a `periodo.fim` → ambas elegíveis (bordas inclusivas).
  - **Critério de aceite:** `CompetenciaTest` verde, incluindo as duas bordas.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=CompetenciaTest
    ```
  - **Commit:** `feat(T-010): aplica competencia com bordas inclusivas e testes` (1 commit).
  - **Status:** [x] concluída

- [x] **T-011** — Nota fiscal obrigatória (RN-009) e `PoliticaReembolso`
  - **O que faz:** introduz a estrutura imutável `PoliticaReembolso` (limites de 60/80/250 e gatilho de nota fiscal de 100, todos fixados pela spec 1.1) e aplica RN-009: valor normalizado estritamente maior que R$ 100,00 sem `tem_nota_fiscal` é recusado com `NOTA_FISCAL_AUSENTE`, comparado sempre pelo valor individual normalizado, antes de qualquer corte por teto.
  - **RN atendidas:** RN-009.
  - **CA atendidos:** CA-008, CA-009 (parte de nota fiscal — completa a cobertura de CA-009 iniciada em T-007).
  - **DT/seções do plan:** DT-007 (estrutura imutável simples, sem mecanismo genérico de regras).
  - **Dependências:** T-005 (campos validados), T-007 (`valor_normalizado` disponível).
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../modelo/PoliticaReembolso.java` (novo)
    - `src/main/java/.../pipeline/AvaliadorRegrasIndividuais.java` (mesma classe de T-008/T-009/T-010)
    - `tests/java/.../NotaFiscalTest.java`
  - **Testes:** `NotaFiscalTest` (parametrizado) — `100,00` sem nota → elegível; `100,01` sem nota → recusado; `100.004` sem nota → elegível (normaliza para `100,00`); `100.005` sem nota → recusado (normaliza para `100,01`).
  - **Critério de aceite:** `NotaFiscalTest` verde nos quatro casos; a comparação usa o valor individual normalizado, verificável isoladamente sem depender de nenhum agregador de teto (ainda não existente).
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=NotaFiscalTest
    ```
  - **Commit:** `feat(T-011): aplica obrigatoriedade de nota fiscal com testes` (1 commit).
  - **Status:** [x] concluída

---

## Fase 4 — Elegibilidade coletiva e tetos

- [x] **T-012** — Duplicidade econômica (RN-010) e seleção de itens elegíveis
  - **O que faz:** entre os itens sem nenhum motivo de recusa anterior (etapas 6 e 8 de 8.1 — seleção pós-validações individuais e seleção pós-duplicidade —, agrupadas nesta task por não terem, isoladamente, uma capacidade observável própria fora da duplicidade), detecta duplicidade econômica exata (mesma `data`, categoria normalizada, `valor` normalizado, `fornecedor` e `descricao` como recebidos) e mantém apenas a primeira ocorrência em ordem de `indice_entrada`; as posteriores recebem `DUPLICIDADE`.
  - **RN atendidas:** RN-010.
  - **CA atendidos:** CA-013 (cobertura estrutural/parcial — o fechamento do resultado final acontece em T-020), CA-014.
  - **Dependências:** T-006, T-008, T-009, T-010, T-011 — a população elegível para duplicidade exige que todas as recusas individuais já tenham sido decididas.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/DetectorDuplicidadeEconomica.java`
    - `src/main/java/.../pipeline/SeletorElegiveis.java`
    - `tests/java/.../DuplicidadeEconomicaTest.java`
  - **Testes:** `DuplicidadeEconomicaTest` — nesta task, os tetos e a composição final da saída ainda não existem (são criados só a partir de T-013/T-016), então o teste **não** exige que a primeira ocorrência apareça "integralmente reembolsada com R$ 54,90". O teste verifica, no nível de duplicidade:
    - a primeira ocorrência (dois itens idênticos de R$ 54,90 em `2026-07-09`) **permanece elegível** e não recebe `DUPLICIDADE`;
    - a ocorrência posterior recebe `DUPLICIDADE` e fica marcada inelegível para as etapas seguintes;
    - itens de R$ 100,00 e R$ 100,01 do mesmo dia/fornecedor **não** são tratados como duplicata;
    - um item já recusado por outra regra não entra na verificação (não gera falso `DUPLICIDADE` nem é contaminado por ela).
  - **Critério de aceite:** `DuplicidadeEconomicaTest` verde nos quatro cenários acima. O **resultado financeiro final** desse mesmo par de itens (primeiro `INTEGRALMENTE_REEMBOLSADO` com `54,90`, segundo `RECUSADO` com `0,00`) só é verificável depois que tetos (T-013/T-014) e composição de saída (T-016) existirem — fica declarado e confirmado em T-020, via `ExemploCompletoTest`, sobre o par `d-006`/`d-007` do arquivo de exemplo.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=DuplicidadeEconomicaTest
    ```
  - **Commit:** `feat(T-012): trata duplicidade economica e selecao de elegiveis` (1 commit).
  - **Status:** [x] concluída

- [x] **T-013** — Tetos diários com distribuição do saldo e corte parcial (RN-011, RN-012, RN-014, RN-015)
  - **O que faz:** para `alimentacao` e `transporte_urbano`, agrega o saldo elegível por `data` e categoria, consome-o em ordem crescente de `indice_entrada`, corta no teto (nunca recusa o agregado por ultrapassagem) e marca os itens posteriores ao esgotamento como `NAO_REEMBOLSADO_TETO_ESGOTADO` (distinto de `RECUSADO`).
  - **RN atendidas:** RN-011, RN-012, RN-014, RN-015.
  - **CA atendidos:** CA-004, CA-005, CA-006.
  - **DT/seções do plan:** DT-007 (consumo dos limites `limiteDiarioAlimentacao`/`limiteDiarioTransporteUrbano` de `PoliticaReembolso`, criada em T-011).
  - **Dependências:** T-012 (população elegível pós-duplicidade).
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/AgregadorTetoDiario.java`
    - `tests/java/.../TetoDiarioTest.java`
    - `tests/java/.../ReembolsoParcialTest.java`
    - `tests/java/.../DistribuicaoTetoTest.java`
  - **Testes:**
    - `TetoDiarioTest` — R$ 72,50 e R$ 38,00 de alimentação na mesma data → R$ 60,00 no total da data; item de transporte de R$ 100,00 sozinho na data → R$ 80,00.
    - `ReembolsoParcialTest` — R$ 61,00 de alimentação, sozinho na data → R$ 60,00 (nunca R$ 0,00 por ultrapassagem).
    - `DistribuicaoTetoTest` — R$ 72,50 seguido de R$ 38,00 no mesmo dia, nesta ordem de `indice_entrada` → o primeiro rende R$ 60,00 (`PARCIALMENTE_REEMBOLSADO`), o segundo rende R$ 0,00 com `NAO_REEMBOLSADO_TETO_ESGOTADO` (não `RECUSADO`).
  - **Critério de aceite:** os três testes verdes; o estado `NAO_REEMBOLSADO_TETO_ESGOTADO` é distinto e verificável separadamente de `RECUSADO`.
  - **Comandos de verificação:**
    ```
    mvn test "-Dtest=TetoDiarioTest,ReembolsoParcialTest,DistribuicaoTetoTest"
    ```
  - **Commit:** `feat(T-013): aplica tetos diarios e distribuicao do saldo com testes` (1 commit).
  - **Status:** [x] concluída

- [x] **T-014** — Teto individual de hospedagem (RN-013)
  - **O que faz:** hospedagem é avaliada por lançamento, sem saldo compartilhado entre lançamentos — cada item de `hospedagem` tem teto próprio de R$ 250,00, independentemente do conteúdo da `descricao`.
  - **RN atendidas:** RN-013 (reaproveita o mecanismo de corte parcial de RN-014, já implementado em T-013).
  - **CA atendidos:** CA-007.
  - **Dependências:** T-012 (população elegível), T-013 (mecanismo de corte parcial reaproveitado).
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/AgregadorTetoHospedagem.java`
    - `tests/java/.../TetoHospedagemTest.java`
  - **Testes:** `TetoHospedagemTest` — lançamento de R$ 480,00 descrito como "2 diarias" → R$ 250,00; alterar o texto da `descricao` não altera o teto; duas hospedagens elegíveis na mesma data podem render até R$ 500,00 no total (nunca compartilham saldo, ao contrário de alimentação/transporte).
  - **Critério de aceite:** `TetoHospedagemTest` verde; hospedagem nunca produz `NAO_REEMBOLSADO_TETO_ESGOTADO` (conforme 8.5).
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=TetoHospedagemTest
    ```
  - **Commit:** `feat(T-014): aplica teto individual de hospedagem com testes` (1 commit).
  - **Status:** [x] concluída

- [x] **T-015** — Viagem sem efeito e campos desconhecidos (RN-016)
  - **O que faz:** confirma, com teste de regressão, que nenhuma inferência de condição de viagem ocorre (nem por `descricao`, `fornecedor`, categoria, existência de hospedagem, nem por um eventual campo `em_viagem`) e que campos fora do contrato — tanto em `despesa` quanto em `colaborador` — são ignorados sem qualquer efeito no resultado.
  - **RN atendidas:** RN-016.
  - **CA atendidos:** CA-010.
  - **Dependências:** T-013 (precisa de um item elegível processado ponta a ponta, através de pelo menos um teto, para comparar `valor_reembolsavel` antes/depois da troca de texto).
  - **Arquivos que cria/modifica (planejado):**
    - `tests/java/.../RegraViagemEfeitoNuloTest.java`
    - `tests/java/.../CamposDesconhecidosTest.java`
    - Nenhum arquivo de produção é esperado — esta task é predominantemente uma verificação de ausência de comportamento sobre o pipeline já construído nas tasks anteriores.
  - **Testes:**
    - `RegraViagemEfeitoNuloTest` — numa entrada com um único item elegível, trocar na `descricao` um texto neutro por "aeroporto"/"hotel" não altera `valor_reembolsavel`; um campo `em_viagem: true` na entrada também não altera nada.
    - `CamposDesconhecidosTest` — campo fora do contrato dentro de `despesa` e dentro de `colaborador` é ignorado silenciosamente.
  - **Critério de aceite e contingência:**
    - Cenário esperado (comportamento já correto por construção das tasks anteriores): **um único commit** `test(T-015)`.
    - Cenário de exceção: se a execução do teste revelar um defeito real (por exemplo, um acessor do Jackson vazando `em_viagem` ou coagindo algo implicitamente), é permitido um commit `fix(T-015)` **antes** do commit `test(T-015)`. Este commit de correção é contingência, não trabalho planejado, e **não entra na estimativa principal de 22 commits** deste backlog.
  - **Comandos de verificação:**
    ```
    mvn test "-Dtest=RegraViagemEfeitoNuloTest,CamposDesconhecidosTest"
    ```
  - **Commit (planejado):** `test(T-015): comprova efeito nulo de viagem e campos desconhecidos` (1 commit; ver contingência acima).
  - **Status:** [x] concluída

---

## Fase 5 — Composição da saída e total

- [x] **T-016** — Composição da saída e ordenação de motivos (RN-017)
  - **O que faz:** toda posição da lista `despesas` produz exatamente um registro de saída, na ordem da entrada, com decisão final e motivos ordenados conforme 8.3 (não conforme a ordem em que foram detectados no pipeline), respeitando as exclusões fechadas de 8.4.
  - **RN atendidas:** RN-017.
  - **CA atendidos:** CA-002.
  - **DT/seções do plan:** DT-006 (materialização, aqui, do último passo de composição do pipeline linear de 8.1 — os passos anteriores já materializam 8.1 desde T-004).
  - **Dependências:** T-006 a T-015 — precisa de todos os motivos, de todas as regras, já acumulados por item.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/CompositorSaida.java`
    - `tests/java/.../ComposicaoSaidaTest.java`
    - `tests/java/.../OrdemMotivosTest.java`
  - **Testes:**
    - `ComposicaoSaidaTest` — 14 posições de entrada produzem 14 registros de saída, na ordem da entrada; nenhum item desaparece, inclusive os recusados.
    - `OrdemMotivosTest` — os dois exemplos normativos de 8.4: item de R$ 500,00, `coworking`, sem nota, fora da janela → três motivos, na ordem `CATEGORIA_FORA_POLITICA`, `FORA_COMPETENCIA`, `NOTA_FISCAL_AUSENTE`; item de −R$ 500,00, `coworking`, sem nota → `VALOR_NAO_POSITIVO` e `CATEGORIA_FORA_POLITICA`, mas **não** `NOTA_FISCAL_AUSENTE`.
  - **Critério de aceite:** ambos os testes verdes.
  - **Comandos de verificação:**
    ```
    mvn test "-Dtest=ComposicaoSaidaTest,OrdemMotivosTest"
    ```
  - **Commit:** `feat(T-016): compoe saida e ordena motivos com testes` (1 commit).
  - **Status:** [x] concluída

- [x] **T-017** — Total do período (RN-018)
  - **O que faz:** `total_reembolsavel` é exatamente a soma dos `valor_reembolsavel` apresentados nos registros de saída. Esta task também fecha, em nível de unidade, a parte de CA-017 que só faz sentido quando o total existe: um item recusado por `VALOR_NAO_POSITIVO` (T-008) não reduz o total do período.
  - **RN atendidas:** RN-018.
  - **CA atendidos:** CA-003; fecha, em unidade, a parte pendente de CA-017 (total não reduzido por valor não positivo) — a confirmação ponta a ponta dessa mesma parte de CA-017 acontece em T-020.
  - **Dependências:** T-016.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../pipeline/SomadorTotal.java`
    - `tests/java/.../TotalPeriodoTest.java`
  - **Testes:** `TotalPeriodoTest` — soma de uma lista de resultados arbitrária (não o arquivo de exemplo) confirma `total_reembolsavel` == soma dos `valor_reembolsavel`; uma lista contendo um item com `VALOR_NAO_POSITIVO` confirma que o total não é reduzido por ele.
  - **Critério de aceite:** `TotalPeriodoTest` verde nos dois aspectos.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=TotalPeriodoTest
    ```
  - **Commit:** `feat(T-017): calcula total reembolsavel com testes` (1 commit).
  - **Status:** [x] concluída

---

## Fase 6 — CLI final e integração

- [x] **T-018** — Escritor JSON de saída
  - **O que faz:** serializa o resultado geral conforme 4.3 — valores monetários como números JSON decimais em notação simples, com exatamente duas casas, nunca em notação científica e nunca como string.
  - **Requisitos atendidos:** nenhuma RN/CA nova isoladamente (é I/O, não regra de negócio) — é o que torna 4.3 observável na prática; contribui para CA-001/CA-002/CA-003 quando exercitado ponta a ponta em T-020.
  - **DT/seções do plan:** plan §7 (serialização monetária).
  - **Dependências:** T-017.
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../escritor/EscritorResultado.java`
    - `tests/java/.../EscritorResultadoTest.java`
  - **Testes:** `EscritorResultadoTest` — valor `60.00` serializado como `60.00` (nunca `60.0` nem notação científica); `33.33` preservado com duas casas.
  - **Critério de aceite:** `EscritorResultadoTest` verde; o JSON gerado é parseável e revalida manualmente contra o schema de 4.3.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=EscritorResultadoTest
    ```
  - **Commit:** `feat(T-018): serializa resultado monetario conforme a spec` (1 commit).
  - **Status:** [x] concluída

- [x] **T-019** — Conclusão da CLI: escrita atômica do destino e contrato final (DT-010)
  - **O que faz:** fecha o contrato de execução por completo. O resultado é serializado para um arquivo temporário no mesmo diretório do destino e só então movido/substituído atomicamente (`Files.move` com `ATOMIC_MOVE` + `REPLACE_EXISTING`) sobre `--output`; qualquer falha nos códigos `2` ou `3` preserva um `--output` preexistente intacto; sucesso (exit `0`) substitui o destino pelo resultado completo.
  - **Requisitos atendidos:** nenhuma RN/CA nova isoladamente.
  - **DT/seções do plan:** DT-003 (fechamento — os três exit codes `0`/`2`/`3` verificados juntos pela primeira vez), DT-010.
  - **Dependências:** T-002 (CLI básica), T-004 (exit `3`), T-018 (escritor pronto).
  - **Arquivos que cria/modifica:**
    - `src/main/java/.../Main.java` (orquestração final: arquivo temporário + movimentação atômica)
    - `tests/java/.../EscritaAtomicaSaidaTest.java`
    - `tests/java/.../CliContratoTest.java` (complementado com o cenário de sucesso, exit `0` — a mesma classe criada em T-002)
  - **Testes:**
    - `EscritaAtomicaSaidaTest` — envelope inválido, JSON sintaticamente inválido e uma falha simulada antes da substituição final não alteram um `--output` preexistente; um processamento com sucesso substitui o destino pelo resultado completo; nenhum arquivo temporário/parcial permanece no caminho oficial de `--output` após qualquer cenário.
    - `CliContratoTest` — cenário de sucesso adicionado: exit `0`, arquivo de resultado escrito, nada em stderr.
  - **Critério de aceite:** `EscritaAtomicaSaidaTest` verde nos quatro cenários; `CliContratoTest` cobre `0`, `2` e `3` num único comando `mvn test`.
  - **Comandos de verificação:**
    ```
    mvn test "-Dtest=EscritaAtomicaSaidaTest,CliContratoTest"
    ```
  - **Commit:** `feat(T-019): conclui CLI com escrita atomica e testes de contrato` (1 commit).
  - **Status:** [x] concluída

- [x] **T-020** — Teste de integração completo (14 itens, total R$ 585,43)
  - **O que faz:** executa o pipeline inteiro, de ponta a ponta, contra `exemplos/despesas-exemplo.json`, comparando o resultado **estruturalmente** (JSON contra JSON, campo a campo — nunca comparação textual) contra um fixture de saída esperada.
  - **Requisitos atendidos:** nenhuma RN nova — é a verificação cruzada de todas; fecha formalmente CA-001, CA-002, CA-003, e confirma ponta a ponta as partes de CA-013, CA-016 e CA-017 que dependiam de execução completa do pipeline (duplicidade econômica com resultado final; item fora de política de fato não alcança teto; item de valor não positivo de fato não reduz o total).
  - **CA atendidos:** CA-001, CA-002, CA-003 (fechamento); confirmação ponta a ponta de CA-013, CA-016 e CA-017. Em particular, `ExemploCompletoTest` declara explicitamente, sobre o par `d-006`/`d-007` do arquivo de exemplo, que a **primeira ocorrência** (`d-006`) é `INTEGRALMENTE_REEMBOLSADO` com `valor_reembolsavel` `54,90`, e a **ocorrência posterior** (`d-007`) é `RECUSADO` com `valor_reembolsavel` `0,00` e motivo `DUPLICIDADE` — fechando o resultado final que T-012 deixou apenas estruturalmente verificado.
  - **DT/seções do plan:** DT-006 (verificação final de que o pipeline segue os onze passos de 8.1 de ponta a ponta), DT-009 (comando único `mvn test` executando toda a suíte, inclusive este teste de integração).
  - **Dependências:** T-019.
  - **Arquivos que cria/modifica:**
    - `tests/resources/fixtures/despesas-exemplo-esperado.json` — **escrito manualmente**, nunca gerado pelo próprio motor (o que tornaria o teste circular). Construído a partir do schema completo de 4.3 a 4.5 (estrutura de cada registro e de cada objeto de motivo), usando a tabela 4.7 como fonte de decisões e valores, e RN-017 + a ordem de 8.3 para montar o objeto completo de cada motivo (`codigo`, `regra`, `campo`) — a tabela 4.7 é uma representação abreviada (só o `codigo`) e não basta sozinha para montar o fixture.
    - `tests/java/.../ExemploCompletoTest.java`
  - **Testes:** `ExemploCompletoTest` — os 14 registros de `resultados`, todos os campos de cada um (`indice_entrada`, `id`, `valor_informado`, `valor_normalizado`, `valor_reembolsavel`, `decisao`, `motivos` completos), e `total_reembolsavel` igual a `585.43`, comparados estruturalmente contra o fixture manual.
  - **Critério de aceite:** `ExemploCompletoTest` verde; as 14 decisões, valores e motivos coincidem exatamente com a tabela 4.7; total igual a R$ 585,43.
  - **Comandos de verificação:**
    ```
    mvn test -Dtest=ExemploCompletoTest
    ```
  - **Commit:** `test(T-020): valida exemplo completo e total de 585,43` (1 commit).
  - **Status:** [x] concluída

---

## Fase 7 — Documentação final

- [x] **T-021** — README com instruções reais de build, execução e testes
  - **O que faz:** documenta, em comandos reais e testados manualmente, como compilar, executar e testar o projeto — de forma multiplataforma, sem depender de um caminho específico de um sistema operacional.
  - **Requisitos atendidos:** nenhuma RN/CA/DT diretamente — atende ao critério "Produto funciona" da rubrica e evita a penalidade transversal "README não permite rodar o projeto".
  - **Dependências:** T-020 (documenta um sistema já testado de ponta a ponta).
  - **Arquivos que cria/modifica:**
    - `README.md`
  - **Testes:** nenhum automatizado — verificação manual, seguindo o próprio README do zero, em mais de um terminal.
  - **Critério de aceite:** seguindo somente o README:
    ```
    mvn package
    ```
    gera o jar; depois
    ```
    java -jar target/motor-reembolso.jar calcular --input exemplos/despesas-exemplo.json --output resultado.json
    ```
    produz `resultado.json` com `total_reembolsavel` igual a `585.43`; e
    ```
    mvn test
    ```
    roda a suíte inteira sem falhas. Os três comandos são apresentados em linhas separadas (sem `&&`) e usam caminho relativo (`resultado.json`, não `/tmp/out.json`), funcionando tanto em PowerShell quanto em outros terminais.
  - **Comandos de verificação:** os três comandos do critério de aceite, executados manualmente em sequência, cada um em sua própria linha.
  - **Commit:** `docs(readme): [T-021] documenta build execucao e testes` (1 commit).
  - **Status:** [x] concluída

---

## Fase 8 — Envelope do Dia 2 (política v4)

Base normativa: `spec.md` `1.2` (aprovada) e `plan.md` `1.1` (aprovado), ambos já registrando RN-019 a RN-022 e as decisões técnicas DT-011 a DT-019. Nenhuma regra de negócio é decidida aqui — cada task abaixo só decompõe em passos executáveis o que a spec e o plano já fecharam. Numeração contínua a partir de `T-022`, sem lacuna nem duplicata. Todas as tasks desta fase nascem **pendentes**; nenhuma é implementada nesta sessão.

**Convenção de nomes usada abaixo** (verificada em `src/main/java/com/desafio/reembolso/...` nesta sessão): pacotes `modelo`, `leitor`, `pipeline`, `escritor`; `Main.java` na raiz do pacote `com.desafio.reembolso`. Os testes espelham a mesma árvore sob `tests/java/com/desafio/reembolso/...`, com sufixo `*Test` (Maven Surefire, DT-009).

**Estratégia de compatibilidade adotada nesta fase (decisão de decomposição, não de negócio):** várias classes hoje têm assinatura pública fixa (`ItemValidado`, `AvaliadorRegrasIndividuais.avaliar(...)`, `AgregadorTetoDiario.aplicar(...)`) e são consumidas por dezenas de testes já verdes (T-001 a T-021). Para que **cada task deixe a suíte inteira compilando e passando** (regra geral de sessão, topo deste arquivo) sem forçar uma reescrita massiva de testes históricos num único commit, as tasks que estendem essas classes preservam as assinaturas antigas (sobrecarga, nunca remoção) e só acrescentam as novas — a migração dos consumidores para as APIs novas e a remoção do que ficou superado (`PoliticaReembolso`, `AgregadorTetoHospedagem`) é o próprio Bloco K, no fim desta fase, conforme `CLAUDE.md` §6.

---

### Bloco A — Modelos e vocabulários fundamentais

- [x] **T-022** — Estender `CampoCanonico` com `MOEDA`
  - **O que faz:** acrescenta o valor `MOEDA` ao vocabulário fechado de campos canônicos, serializando para `"despesa.moeda"` (mesmo mecanismo de `textoCanonico()` já existente — nome do enum em minúsculo, prefixado por `despesa.`). No **mesmo commit**, `CompositorSaida.criarOrdemCampo()` é atualizado para reconhecer o valor novo — nunca se deixa um intervalo entre "o enum existe" e "o compositor sabe ordená-lo", porque a partir de T-036 um motivo estrutural com `campo = CampoCanonico.MOEDA` já pode chegar ao pipeline, e `ordemCampo(...)` lançaria `IllegalArgumentException` ("campo fora do vocabulário fechado de precedência") sobre um motivo perfeitamente válido se o mapa não reconhecesse `MOEDA` desde já.
  - **RN atendidas:** RN-002 (7º campo do contrato).
  - **CA atendidos:** base para CA-048 (fechado em T-036).
  - **DT/seções do plan:** DT-019; plan §4 (`CampoCanonico` ganha `MOEDA`), plan §10.
  - **Dependências:** nenhuma (extensão isolada de enum já existente, T-003).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/modelo/CampoCanonico.java`
    - `src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java`
    - `tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java`
  - **Passos de implementação:**
    1. Adicionar `MOEDA` à enumeração, na posição que reflete a ordem canônica de contrato (após `VALOR`, antes de `TEM_NOTA_FISCAL`).
    2. Atualizar imediatamente `CompositorSaida.criarOrdemCampo()` para a ordem canônica final de oito campos, definitiva desde esta task — não uma tabela provisória de sete campos à espera de T-048: `ID=0, DATA=1, CATEGORIA=2, DESCRICAO=3, FORNECEDOR=4, VALOR=5, MOEDA=6, TEM_NOTA_FISCAL=7`.
    3. Confirmar que `OrdemMotivosTest` (T-016) continua verde sem alteração — a mudança é aditiva e preserva a posição relativa dos sete campos históricos (`TEM_NOTA_FISCAL` continua sendo o último, só desloca de `6` para `7`), então nenhum cenário já testado muda de resultado.
  - **Testes obrigatórios:** `VocabularioMotivoTest` — parametrizar (ou acrescentar caso) confirmando que `CampoCanonico.MOEDA.textoCanonico()` é exatamente `"despesa.moeda"`. `OrdemMotivosTest` (T-016, sem alteração de código de teste) — roda como regressão, confirmando que a nova posição de `TEM_NOTA_FISCAL` não muda nenhuma ordem já verificada.
  - **Critério de conclusão:** teste novo verde; `OrdemMotivosTest` (T-016) continua verde sem modificação; `mvn test` completo verde.
  - **Comando de verificação:**
    ```
    mvn -q test "-Dtest=VocabularioMotivoTest,OrdemMotivosTest"
    ```
  - **Commit sugerido:** `feat(T-022): estende CampoCanonico com despesa.moeda e atualiza ordem de campo no CompositorSaida`
  - **Status:** [x] concluída

- [x] **T-023** — Estender `MotivoCodigo` com os três códigos novos
  - **O que faz:** acrescenta `MOEDA_SEM_COTACAO`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` e `TETO_INDIVIDUAL_APLICADO` ao vocabulário fechado de códigos de motivo (spec 4.5), cada um serializando para o próprio nome do enum (mesmo mecanismo já existente). No **mesmo commit**, `CompositorSaida.criarEstagios()` é atualizado para reconhecer os três códigos — não se pode esperar até T-048, porque `MOEDA_SEM_COTACAO` já é produzido em T-037 e integrado ao pipeline em T-038, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` já é produzido em T-041 e integrado em T-042, e `TETO_INDIVIDUAL_APLICADO` já é produzido em T-044 e integrado em T-046 — todos antes de T-048. Sem esta atualização imediata, um motivo válido produzido por qualquer uma dessas tasks faria `CompositorSaida.estagioDe(...)` lançar `IllegalArgumentException` ("código de motivo fora do vocabulário fechado de precedência").
  - **RN atendidas:** RN-019, RN-020.
  - **CA atendidos:** base para CA-027, CA-030, CA-049 (fechados nos blocos G/F/H).
  - **DT/seções do plan:** DT-019; plan §10.
  - **Dependências:** nenhuma.
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/modelo/MotivoCodigo.java`
    - `src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java`
    - `tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java`
  - **Passos de implementação:**
    1. Adicionar os três valores ao enum, sem remover nem renomear nenhum dos treze já existentes.
    2. Atualizar imediatamente `CompositorSaida.criarEstagios()` para a tabela final de onze estágios (0 a 10), definitiva desde esta task: `0` `ITEM_TIPO_INVALIDO`; `1` motivos estruturais; `2` `ID_DUPLICADO`; `3` `MOEDA_SEM_COTACAO`; `4` `VALOR_NAO_POSITIVO`; `5` `CATEGORIA_FORA_POLITICA`; `6` `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`; `7` `FORA_COMPETENCIA`; `8` `NOTA_FISCAL_AUSENTE`; `9` `DUPLICIDADE`; `10` todos os motivos de teto, incluindo `TETO_INDIVIDUAL_APLICADO`. A renumeração preserva a ordem relativa de todos os códigos históricos.
    3. Confirmar que `OrdemMotivosTest` (T-016) continua verde sem alteração — nenhum dos códigos históricos muda de posição relativa, só os três novos ganham lugar entre eles.
  - **Testes obrigatórios:** `VocabularioMotivoTest` — cada um dos três novos valores serializa para o próprio nome (`"MOEDA_SEM_COTACAO"`, `"CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO"`, `"TETO_INDIVIDUAL_APLICADO"`). `OrdemMotivosTest` (T-016, sem alteração de código de teste) — roda como regressão da renumeração de estágios.
  - **Critério de conclusão:** teste novo verde; `OrdemMotivosTest` (T-016) continua verde sem modificação; `mvn test` completo continua verde.
  - **Comando de verificação:**
    ```
    mvn -q test "-Dtest=VocabularioMotivoTest,OrdemMotivosTest"
    ```
  - **Commit sugerido:** `feat(T-023): estende MotivoCodigo com os tres codigos da politica v4 e atualiza estagios do CompositorSaida`
  - **Status:** [x] concluída

- [x] **T-024** — Estender `RegraNegocio` com RN-019 a RN-022
  - **O que faz:** acrescenta `RN_019`, `RN_020`, `RN_021`, `RN_022` ao vocabulário fechado de regras, cada uma serializando para `"RN-NNN"` (mesmo mecanismo já existente: `name().replace('_', '-')`).
  - **RN atendidas:** RN-019 a RN-022 (o próprio vocabulário que as representa).
  - **CA atendidos:** base para todos os CA do Dia 2 que citam essas RN em `motivo.regra`.
  - **DT/seções do plan:** DT-019; plan §10.
  - **Dependências:** nenhuma.
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/modelo/RegraNegocio.java`
    - `tests/java/com/desafio/reembolso/modelo/VocabularioMotivoTest.java`
  - **Passos de implementação:**
    1. Adicionar os quatro valores ao enum, na sequência `RN_019, RN_020, RN_021, RN_022`, sem tocar os dezoito já existentes.
  - **Testes obrigatórios:** `VocabularioMotivoTest` — os quatro novos valores serializam para `"RN-019"`, `"RN-020"`, `"RN-021"`, `"RN-022"`.
  - **Critério de conclusão:** teste novo verde; `mvn test` completo continua verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=VocabularioMotivoTest
    ```
  - **Commit sugerido:** `feat(T-024): estende RegraNegocio com RN-019 a RN-022`
  - **Status:** [x] concluída

- [x] **T-025** — Criar `Periodicidade` e `TabelaCategoria`
  - **O que faz:** cria o enum fechado `Periodicidade` (`DIA`, `DIARIA` — AMB-036) e a estrutura imutável `TabelaCategoria` (`limite: BigDecimal`, `periodicidade: Periodicidade`), que representa uma categoria dentro de uma tabela de política (`padrao` ou de um centro de custo). Agrupadas na mesma task por serem pequenas e fortemente acopladas: `TabelaCategoria` não existe sem `Periodicidade`.
  - **RN atendidas:** RN-019.
  - **CA atendidos:** base estrutural para CA-024 a CA-027, CA-045, CA-047, CA-049.
  - **DT/seções do plan:** DT-011; plan §5 (modelo `TabelaCategoria`).
  - **Dependências:** nenhuma.
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/modelo/Periodicidade.java`
    - `src/main/java/com/desafio/reembolso/modelo/TabelaCategoria.java`
    - `tests/java/com/desafio/reembolso/modelo/TabelaCategoriaTest.java`
  - **Passos de implementação:**
    1. `Periodicidade`: enum com dois valores, `DIA` e `DIARIA` — sem serialização JSON própria (é consumido internamente, nunca escrito na saída).
    2. `TabelaCategoria`: adotado definitivamente como `record`, com construtor compacto que rejeita nulos — sem alternativa em aberto:
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
       ```
  - **Testes obrigatórios:** `TabelaCategoriaTest` — construção válida preserva os dois campos; `limite` ou `periodicidade` nulos lançam `NullPointerException` (falha rápida, sem estado parcial).
  - **Critério de conclusão:** teste novo verde; nenhuma outra classe referencia `TabelaCategoria` ainda (isso começa em T-026).
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=TabelaCategoriaTest
    ```
  - **Commit sugerido:** `feat(T-025): cria Periodicidade e TabelaCategoria`
  - **Status:** [x] concluída

- [x] **T-026** — Criar `PoliticaExterna`
  - **O que faz:** cria a estrutura imutável `PoliticaExterna` (`vigencia: LocalDate`, `moedaBase: String`, `notaFiscalObrigatoriaAcimaDe: BigDecimal`, `padrao: Map<String, TabelaCategoria>`, `centrosCusto: Map<String, Map<String, TabelaCategoria>>`), construída sempre com cópias defensivas imutáveis dos dois mapas (`Map.copyOf`). Nesta task o modelo é só a estrutura de dados — a leitura e validação a partir de `politica.json` é `LeitorPolitica` (T-030).
  - **RN atendidas:** RN-019, RN-021.
  - **CA atendidos:** base estrutural para CA-024 a CA-027, CA-035, CA-045.
  - **DT/seções do plan:** DT-011; plan §5.
  - **Dependências:** T-025 (`TabelaCategoria`).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/modelo/PoliticaExterna.java`
    - `tests/java/com/desafio/reembolso/modelo/PoliticaExternaTest.java`
  - **Passos de implementação:**
    1. Construtor recebe os cinco campos e aplica `Map.copyOf` em `padrao` e em cada valor de `centrosCusto` (mapa de mapas — cada tabela interna também precisa ser copiada defensivamente, não só o mapa externo).
    2. Getters simples, sem lógica de resolução (isso é `ResolutorPoliticaCentroCusto`, T-040).
  - **Testes obrigatórios:** `PoliticaExternaTest` — construção preserva os cinco campos; tentar modificar `padrao` ou uma tabela de `centrosCusto` obtida via getter lança `UnsupportedOperationException` (garante DT-011, "nenhuma referência mutável escapa").
  - **Critério de conclusão:** teste novo verde; `mvn test` completo continua verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=PoliticaExternaTest
    ```
  - **Commit sugerido:** `feat(T-026): cria modelo imutavel PoliticaExterna`
  - **Status:** [x] concluída

- [x] **T-027** — Criar `TabelaCambio`
  - **O que faz:** cria a estrutura imutável `TabelaCambio` (`moedaBase: String`, `cotacoesPorMoeda: Map<String, NavigableMap<LocalDate, BigDecimal>>`), já na forma invertida de consulta eficiente (moeda → data → taxa, DT-013), com uma API de consulta fechada que devolve, num único objeto, a data da cotação efetivamente usada **e** a taxa correspondente — nunca a taxa isolada, porque `data_cotacao_utilizada` (spec 4.3) é campo de auditoria própria e não pode ser recalculada fora do ponto de consulta que já sabe qual entrada foi usada. Nesta task o modelo é só a estrutura — a leitura/inversão a partir de `cambio.json` é `LeitorCambio` (T-032).
  - **RN atendidas:** RN-020.
  - **CA atendidos:** base estrutural para CA-029, CA-030, CA-046.
  - **DT/seções do plan:** DT-013; plan §7.
  - **Dependências:** nenhuma.
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/modelo/TabelaCambio.java` (inclui o `record` aninhado `CotacaoResolvida` — nenhum arquivo separado é criado só para ele)
    - `tests/java/com/desafio/reembolso/modelo/TabelaCambioTest.java`
  - **Passos de implementação:**
    1. Construtor recebe `moedaBase` e `Map<String, NavigableMap<LocalDate, BigDecimal>>`, copiando defensivamente o mapa externo e cada `NavigableMap` interno para uma implementação imutável (ex.: `Collections.unmodifiableNavigableMap` sobre uma cópia em `TreeMap`).
    2. `record` aninhado, público e definitivo, sem alternativa em aberto:
       ```java
       public record CotacaoResolvida(
           LocalDate data,
           BigDecimal taxa
       ) {}
       ```
    3. Método de consulta definitivo, também sem alternativa em aberto:
       ```java
       public Optional<CotacaoResolvida> cotacaoEm(
           String moeda,
           LocalDate dataDespesa
       )
       ```
       Implementação: busca o `NavigableMap<LocalDate, BigDecimal>` de `moeda` em `cotacoesPorMoeda` — se ausente, `Optional.empty()`; caso contrário, `NavigableMap.floorEntry(dataDespesa)` — se `null` (nenhuma cotação igual ou anterior disponível, inclusive quando a única cotação existente é posterior a `dataDespesa`), `Optional.empty()`; caso contrário, `Optional.of(new CotacaoResolvida(entry.getKey(), entry.getValue()))`, com os dois valores vindos da **mesma** entrada — nunca recalculados separadamente.
  - **Testes obrigatórios:** `TabelaCambioTest` — cotação exata devolve `CotacaoResolvida` com a **própria data** da despesa; fallback devolve a **data anterior efetivamente usada** (não a data da despesa consultada) junto com a taxa correspondente; cotação exclusivamente futura (a única cotação disponível para a moeda é posterior à data consultada) devolve `Optional.empty()`; moeda ausente de `cotacoesPorMoeda` devolve `Optional.empty()`; o mapa externo e cada `NavigableMap` interno são imutáveis (tentativa de modificação lança `UnsupportedOperationException`).
  - **Critério de conclusão:** os cinco cenários acima verdes.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=TabelaCambioTest
    ```
  - **Commit sugerido:** `feat(T-027): cria TabelaCambio com CotacaoResolvida via floorEntry`
  - **Status:** [x] concluída

- [x] **T-028** — Criar `TabelaPoliticaResolvida`
  - **O que faz:** cria a estrutura imutável `TabelaPoliticaResolvida` (`categorias: Map<String, TabelaCategoria>`, `origem: Origem` — enum interno `PADRAO`/`CENTRO_CUSTO` —, `nomeCentroCusto: String`, nulo quando `origem == PADRAO`). É o tipo de retorno de `ResolutorPoliticaCentroCusto.resolver(...)` (T-040) — nesta task só a estrutura.
  - **RN atendidas:** RN-019.
  - **CA atendidos:** base estrutural para CA-024 a CA-027.
  - **DT/seções do plan:** DT-011; plan §4, §6.
  - **Dependências:** T-025 (`TabelaCategoria`).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/modelo/TabelaPoliticaResolvida.java`
    - `tests/java/com/desafio/reembolso/modelo/TabelaPoliticaResolvidaTest.java`
  - **Passos de implementação:**
    1. Enum interno `Origem { PADRAO, CENTRO_CUSTO }`.
    2. Construtor valida: `origem == CENTRO_CUSTO` exige `nomeCentroCusto` não nulo; `origem == PADRAO` exige `nomeCentroCusto == null` — falha rápida (`IllegalArgumentException`) em qualquer combinação inconsistente, porque essa invariante nunca deve ser violada por quem constrói o objeto (T-040).
    3. `categorias` copiado defensivamente (`Map.copyOf`).
  - **Testes obrigatórios:** `TabelaPoliticaResolvidaTest` — construção válida para `PADRAO` (sem nome) e para `CENTRO_CUSTO` (com nome); as duas combinações inconsistentes lançam `IllegalArgumentException`; `categorias` é imutável.
  - **Critério de conclusão:** teste novo verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=TabelaPoliticaResolvidaTest
    ```
  - **Commit sugerido:** `feat(T-028): cria TabelaPoliticaResolvida`
  - **Status:** [x] concluída

- [x] **T-029** — Estender `ItemValidado` com campos de moeda e câmbio
  - **O que faz:** `ItemValidado` ganha quatro campos novos (plan §4, §9): `moeda` (populado por `ValidadorItem`, T-036), `taxaCambioAplicada`, `dataCotacaoUtilizada`, `valorConvertidoBruto` (estes três, e só estes três, populados por `ResolutorCambio`, T-037). Para não quebrar `ValidadorItem` nem os ~20 arquivos de teste que hoje constroem `ItemValidado` pelo construtor de dez argumentos, o construtor **antigo é preservado** e passa a delegar para um construtor novo de catorze argumentos, assumindo `moeda = "BRL"`, `taxaCambioAplicada = BigDecimal.ONE`, `dataCotacaoUtilizada = null` e `valorConvertidoBruto = valor` (cópia do próprio parâmetro `valor` recebido) — exatamente o comportamento correto para um item BRL sem conversão (spec 4.3, "BRL: taxa 1, data nula"), então nenhum teste histórico muda de resultado.
  - **RN atendidas:** RN-002 (campo `moeda`), RN-020 (campos de câmbio).
  - **CA atendidos:** base estrutural para CA-034, CA-048.
  - **DT/seções do plan:** DT-014, DT-015; plan §4 (`ItemValidado` enriquecido pelo `ResolutorCambio`), §9.
  - **Dependências:** nenhuma (extensão de classe já existente desde T-005).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/modelo/ItemValidado.java`
    - `tests/java/com/desafio/reembolso/modelo/ItemValidadoCambioTest.java`
  - **Passos de implementação:**
    1. Adicionar os quatro campos privados finais e os getters correspondentes (`getMoeda()`, `getTaxaCambioAplicada()`, `getDataCotacaoUtilizada()`, `getValorConvertidoBruto()`).
    2. Criar o construtor de catorze argumentos (os dez atuais + os quatro novos).
    3. Reescrever o construtor de dez argumentos para delegar (`this(...)`) no de catorze, com os valores padrão de BRL descritos acima.
    4. Não alterar `ValidadorItem` nesta task — ele continua chamando o construtor de dez argumentos até T-036.
  - **Testes obrigatórios:** `ItemValidadoCambioTest` — construtor de dez argumentos produz `moeda = "BRL"`, `taxaCambioAplicada = 1`, `dataCotacaoUtilizada = null`, `valorConvertidoBruto` igual ao `valor` recebido (inclusive quando `valor` é `null`, caso em que `valorConvertidoBruto` também é `null`); construtor de catorze argumentos preserva exatamente os quatro valores recebidos.
  - **Critério de conclusão:** teste novo verde; `mvn test` completo continua 100% verde sem nenhuma outra classe tocada (confirma que a compatibilidade funciona).
  - **Comando de verificação:**
    ```
    mvn -q test
    ```
  - **Commit sugerido:** `feat(T-029): estende ItemValidado com campos de cambio preservando compatibilidade`
  - **Status:** [x] concluída

---

### Bloco B — Leitor de política externa

- [x] **T-030** — Implementar `LeitorPolitica.ler(Path)`
  - **O que faz:** lê e valida integralmente `politica.json` (spec 4.1.1, RN-021, RN-022, AMB-035), aplicando as dezesseis validações estruturais de `plan.md` §5 antes de construir qualquer `TabelaCategoria`. Sucesso devolve `PoliticaExterna`; qualquer falha (arquivo inexistente, ilegível, JSON sintaticamente inválido, ou violação de qualquer uma das dezesseis regras) lança `PoliticaInvalidaException` — classe estática aninhada em `LeitorPolitica`, no mesmo padrão já usado por `ValidadorEnvelope.EnvelopeInvalidoException` (respondendo à pergunta de `CLAUDE.md`: o padrão atual do projeto já é o de exceção aninhada com `codigoSaida()`, então `PoliticaInvalidaException` segue esse mesmo molde, com `CODIGO_SAIDA = 2`).
  - **RN atendidas:** RN-021, RN-022.
  - **CA atendidos:** CA-035 (parcial — vigência), CA-036 (parcial — política), CA-045.
  - **DT/seções do plan:** DT-012; plan §5.
  - **Dependências:** T-026 (`PoliticaExterna`), T-025 (`TabelaCategoria`, `Periodicidade`).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/leitor/LeitorPolitica.java`
    - `tests/java/com/desafio/reembolso/leitor/LeitorPoliticaTest.java`
    - `tests/resources/fixtures/politica-valida-teste.json` (fixture mínima válida, para o caso de sucesso)
  - **Passos de implementação:**
    1. `ler(Path caminho): PoliticaExterna` — abre o arquivo, faz parsing sintático com `ObjectMapper` (`USE_BIG_DECIMAL_FOR_FLOATS`, mesma técnica de DT-005), captura `IOException`/`JsonProcessingException` e relança como `PoliticaInvalidaException`.
    2. Validar, nesta ordem, os **dezesseis** pontos de `plan.md` §5 — a validação completa e integral, sem nenhum ponto adiado para outra task: raiz objeto; `vigencia` obrigatória/formato/data real; `moeda_base` exatamente `"BRL"`; `nota_fiscal_obrigatoria_acima_de` obrigatório/numérico/não negativo; `padrao`/`centros_custo` obrigatórios e objetos; cada tabela de `centros_custo` é objeto; nome de categoria não vazio; configuração de categoria é objeto; `limite` obrigatório/numérico, estritamente `> 0` em `padrao` e `>= 0` em `centros_custo`; `periodicidade` obrigatória e exatamente `"dia"`/`"diaria"`; `observacao`, dentro de uma configuração de categoria — **ausente** (válido), **presente como texto** (válido, lido e descartado, nunca populado em `TabelaCategoria`), **presente com valor `null` explícito** (política estruturalmente inválida — `observacao` é opcional quanto à ausência da chave, não quanto a um `null` explícito quando ela existe), **presente com qualquer outro tipo** — número, booleano, lista, objeto — (política estruturalmente inválida); campos verdadeiramente desconhecidos — na raiz (`versao`, `acrescimo_em_viagem_percentual`) e dentro de uma configuração de categoria (além de `limite`/`periodicidade`/`observacao`) — continuam ignorados sem invalidar o arquivo; cópias defensivas imutáveis do modelo final (ponto 16).
    3. Construir `PoliticaExterna` só depois de toda a validação passar — nenhuma `TabelaCategoria` é criada antes da validação completa terminar.
    4. Não há passo adiado: `observacao` e os campos desconhecidos já estão integralmente cobertos no passo 2 — T-031 amplia a matriz de testes sobre este mesmo comportamento, já implementado aqui, não introduz validação nova.
  - **Testes obrigatórios:** `LeitorPoliticaTest` — arquivo inexistente; arquivo ilegível; JSON sintaticamente inválido; raiz não objeto; `vigencia` ausente/tipo errado/formato errado/data inexistente; `moeda_base` diferente de `"BRL"`; `padrao` ausente ou não objeto; `centros_custo` ausente ou não objeto; `nota_fiscal_obrigatoria_acima_de` ausente/negativo; categoria com `limite` ausente ou não numérico; `periodicidade` fora de `"dia"`/`"diaria"`; `observacao` ausente (válido); `observacao` como texto (válido e descartado); `observacao` com `null` explícito (arquivo inválido); `observacao` como número (arquivo inválido); `observacao` como booleano (arquivo inválido); `observacao` como lista (arquivo inválido); `observacao` como objeto (arquivo inválido); um arquivo que satisfaz integralmente o contrato é aceito e produz o `PoliticaExterna` esperado (CA-045).
  - **Critério de conclusão:** todos os cenários acima verdes, inclusive os sete de `observacao` (ausente, texto, `null` explícito, número, booleano, lista, objeto); nenhuma `TabelaCategoria` construída num arquivo rejeitado (verificável indiretamente pela exceção lançada antes de qualquer retorno parcial).
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=LeitorPoliticaTest
    ```
  - **Commit sugerido:** `feat(T-030): implementa LeitorPolitica com validacao estrutural completa`
  - **Status:** [x] concluída

- [x] **T-031** — Testes de fronteira do `LeitorPolitica`
  - **O que faz:** amplia exaustivamente a matriz de testes de fronteira sobre comportamentos que `LeitorPolitica` (T-030) **já implementa integralmente** — `limite: 0` em `padrao` (inválido, arquivo inteiro rejeitado) versus `limite: 0` numa tabela de `centros_custo` (estruturalmente válido, produz `TabelaCategoria` com `limite = 0.00`); campo desconhecido dentro de uma configuração de categoria; e a garantia de imutabilidade/isolamento de `PoliticaExterna` a partir do `JsonNode`/mapas mutáveis intermediários do leitor (ponto 16 de `plan.md` §5). Esta task **não** introduz nenhuma validação normativa pela primeira vez — a validação de `observacao` e os demais quinze pontos de `plan.md` §5 já estão fechados desde T-030; aqui só se comprova exaustivamente esse comportamento já existente, sob mais combinações de entrada.
  - **RN atendidas:** RN-019, RN-022.
  - **CA atendidos:** CA-045.
  - **DT/seções do plan:** DT-011, DT-012; plan §5, pontos 11/12 e 16.
  - **Dependências:** T-030.
  - **Arquivos que cria/modifica:**
    - `tests/java/com/desafio/reembolso/leitor/LeitorPoliticaTest.java` (mesma classe de T-030, casos adicionais)
  - **Passos de implementação:**
    1. Escrever os casos de teste listados abaixo, comprovando o comportamento já implementado em T-030 — se algum cenário revelar um defeito real em `LeitorPolitica` (comportamento diferente do que T-030 já deveria garantir), corrigir `LeitorPolitica.java` e o teste **no mesmo e único commit** desta task, e nesse caso a mensagem passa de `test(T-031)` para `fix(T-031)` — nunca dois commits para a mesma task (ver a qualificação de commit único de tasks de teste, no topo deste arquivo).
  - **Testes obrigatórios:** `LeitorPoliticaTest` — `limite: 0` em `padrao` (arquivo inválido — CA-045 negativo); `limite: 0` numa tabela de `centros_custo` (arquivo válido; acessado como `politica.getCentrosCusto().get("CENTRO").get("categoria").limite() == 0.00`, já que `TabelaCategoria` é um `record` cujo único acessor de limite é `limite()`); campo desconhecido dentro de uma configuração de categoria (ignorado, arquivo válido); campo desconhecido na raiz, além de `versao`/`acrescimo_em_viagem_percentual` (ignorado, arquivo válido); um `PoliticaExterna` construído a partir de um arquivo válido tem `padrao`/`centrosCusto` imutáveis (tentativa de modificação via getter lança `UnsupportedOperationException`).
  - **Critério de conclusão:** todos os cenários acima verdes; `mvn test` completo verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=LeitorPoliticaTest
    ```
  - **Commit sugerido:** `test(T-031): cobre limites, campos desconhecidos e imutabilidade do LeitorPolitica`
  - **Status:** [x] concluída

---

### Bloco C — Leitor de câmbio externo

- [x] **T-032** — Implementar `LeitorCambio.ler(Path)`
  - **O que faz:** lê e valida integralmente `cambio.json` (spec 4.1.1, RN-020, RN-022, AMB-035), invertendo a estrutura `data → moeda → taxa` para `moeda → NavigableMap<data, taxa>` (DT-013) na própria leitura. Sucesso devolve `TabelaCambio`; qualquer falha lança `CambioInvalidoException` (aninhada em `LeitorCambio`, mesmo padrão de `EnvelopeInvalidoException`/`PoliticaInvalidaException`, `CODIGO_SAIDA = 2`).
  - **RN atendidas:** RN-020, RN-022.
  - **CA atendidos:** CA-036 (parcial — câmbio), CA-046.
  - **DT/seções do plan:** DT-013; plan §7.
  - **Dependências:** T-027 (`TabelaCambio`).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/leitor/LeitorCambio.java`
    - `tests/java/com/desafio/reembolso/leitor/LeitorCambioTest.java`
    - `tests/resources/fixtures/cambio-valido-teste.json`
  - **Passos de implementação:**
    1. `ler(Path caminho): TabelaCambio` — parsing sintático (mesma técnica de T-030), erro de I/O ou JSON malformado vira `CambioInvalidoException`.
    2. Validar os pontos 1 a 9 de `plan.md` §7: raiz objeto; `moeda_base` exatamente `"BRL"`; `taxas` obrigatório e objeto (pode ser `{}`); cada chave de primeiro nível de `taxas` é data `AAAA-MM-DD` real; valor associado é objeto; cada chave interna casa com `[A-Z]{3}`; cada taxa é numérica e estritamente positiva; `fonte`/`observacao` opcionais e, quando presentes, texto.
    3. Construir a estrutura invertida (`Map<String, NavigableMap<LocalDate, BigDecimal>>`) só depois da validação completa — percorrer `taxas` uma vez para validar, e (na mesma passada ou numa segunda) popular o mapa invertido.
    4. Devolver `TabelaCambio` com cópias defensivas (reaproveita o construtor de T-027).
  - **Testes obrigatórios:** `LeitorCambioTest` — arquivo inexistente/ilegível/JSON inválido; raiz não objeto; `moeda_base` errada; `taxas` ausente ou não objeto; `taxas: {}` (válido); data malformada como chave de primeiro nível; moeda fora de `[A-Z]{3}` como chave interna; taxa zero ou negativa; um arquivo que satisfaz integralmente o contrato é aceito e a consulta invertida devolve os valores esperados (CA-046).
  - **Critério de conclusão:** todos os cenários acima verdes.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=LeitorCambioTest
    ```
  - **Commit sugerido:** `feat(T-032): implementa LeitorCambio com inversao para consulta por data`
  - **Status:** [x] concluída

- [x] **T-033** — Testes de fronteira do `LeitorCambio`
  - **O que faz:** completa a cobertura de `plan.md` §7, ponto 9: `fonte`/`observacao` presentes com tipo não textual invalidam o arquivo (são campos **conhecidos**, não "desconhecidos"); uma chave malformada dentro de `taxas` — data fora de `AAAA-MM-DD` ou moeda fora de `[A-Z]{3}` — **não** é tratada como campo desconhecido, e invalida o arquivo inteiro, mesmo que a tolerância a campos desconhecidos valha na raiz; e a garantia de imutabilidade da estrutura invertida.
  - **RN atendidas:** RN-020, RN-022.
  - **CA atendidos:** CA-046.
  - **DT/seções do plan:** DT-013; plan §7, ponto 9.
  - **Dependências:** T-032.
  - **Arquivos que cria/modifica:**
    - `tests/java/com/desafio/reembolso/leitor/LeitorCambioTest.java` (mesma classe de T-032, casos adicionais)
  - **Passos de implementação:**
    1. Nenhuma alteração de produção é esperada. Se os novos testes revelarem um defeito real em `LeitorCambio`, a correção de produção e os testes entram juntos no único commit de T-033, cuja mensagem muda de `test(T-033)` para `fix(T-033)`. Nunca são criados dois commits para esta task.
    2. Escrever os casos de teste listados abaixo.
  - **Testes obrigatórios:** `LeitorCambioTest` — `fonte`/`observacao`, cada um nos sete cenários fechados: ausente (válido); presente como texto (válido, valor descartado); presente com `null` explícito (arquivo inválido); presente como número (arquivo inválido); presente como booleano (arquivo inválido); presente como lista (arquivo inválido); presente como objeto (arquivo inválido). Além disso: campo desconhecido na raiz, ao lado de `moeda_base`/`fonte`/`observacao`/`taxas` (ignorado, arquivo válido); chave de data malformada dentro de `taxas` (ex.: `"2026/07/13"`) — arquivo inválido, não um "campo desconhecido"; chave de moeda malformada dentro de uma data (ex.: `"US"` ou `"usd"`) — arquivo inválido; a `TabelaCambio` resultante de um arquivo válido tem o mapa externo e cada `NavigableMap` interno imutáveis.
  - **Critério de conclusão:** todos os cenários acima verdes, inclusive os sete de `fonte` e os sete de `observacao` (ausente, texto, `null` explícito, número, booleano, lista, objeto); `mvn test` completo verde. A implementação normativa desses sete cenários pertence a `LeitorCambio` desde T-032 (plan §7, ponto 8 — "quando presentes, devem ser texto"); esta task só amplia exaustivamente a prova de fronteira sobre esse comportamento já implementado.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=LeitorCambioTest
    ```
  - **Commit sugerido:** `test(T-033): cobre fonte/observacao, chaves malformadas e imutabilidade do LeitorCambio`
  - **Status:** [x] concluída

---

### Bloco D — CLI de quatro flags

- [x] **T-034** — `Main.java`: parser de quatro flags
  - **O que faz:** reescreve o parser de argumentos de `Main.run(...)` conforme `plan.md` §3/DT-018: primeiro token deve ser exatamente `"calcular"` (único posicional aceito); tokens restantes consumidos aos pares `flag valor`; pares acumulados num `Map<String, String>` contando ocorrências por chave; ao final, valida que as quatro chaves `--input`/`--output`/`--politica`/`--cambio` estão presentes, cada uma exatamente uma vez, e que nenhuma chave desconhecida apareceu. Qualquer violação → exit `2`. **Não** carrega `--politica`/`--cambio` ainda (isso é T-035) — nesta task, as duas flags só são reconhecidas e armazenadas. Como o contrato de execução muda de duas para quatro flags obrigatórias, **todo** consumidor histórico de `Main.run(...)` que pretende alcançar leitura, envelope, pipeline ou escrita — não só `CliContratoTest` — precisa passar a fornecer as quatro flags nesta mesma task, ou seu cenário passa a falhar no parser por um motivo alheio ao que o teste pretende verificar.
  - **RN atendidas:** RN-022 (parcial — contrato de execução).
  - **CA atendidos:** CA-041, CA-042.
  - **DT/seções do plan:** DT-003 (extensão), DT-018; plan §3.
  - **Dependências:** nenhuma (`Main.java` já existe desde T-002/T-019).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/Main.java`
    - `tests/java/com/desafio/reembolso/CliContratoTest.java`
    - `tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTest.java`
    - `tests/java/com/desafio/reembolso/ExemploCompletoTest.java`
  - **Passos de implementação:**
    1. Substituir o `switch` de dois casos por: (a) checagem do primeiro token == `"calcular"`, com exit `2` se ausente, diferente, ou se sobrar um token posicional depois das flags; (b) laço que consome os tokens restantes aos pares — quantidade ímpar, ou uma flag como último token, é exit `2`.
    2. Popular um `Map<String, String>` com os pares reconhecidos; uma chave repetida ou uma flag fora do conjunto `{--input, --output, --politica, --cambio}` é exit `2`.
    3. Depois de percorrer todos os pares, verificar que as quatro chaves obrigatórias estão presentes — qualquer ausência é exit `2`.
    4. Extrair `inputPath`/`outputPath`/`politicaPath`/`cambioPath` do mapa; **manter o restante do método exatamente como está** (leitura de `--input`, validação de envelope, pipeline, escrita) — `politicaPath`/`cambioPath` ficam sem uso nesta task (variável local não referenciada além da extração, ou passada adiante sem efeito ainda).
    5. Atualizar a constante `USO` de `Main.java` para o contrato completo: `"Uso: java -jar motor-reembolso.jar calcular --input <arquivo> --output <arquivo> --politica <arquivo> --cambio <arquivo>"`.
    6. Rodar o inventário `git grep "Main.run" -- tests/java` e migrar toda invocação histórica que pretenda alcançar leitura do envelope, pipeline ou escrita para fornecer as quatro flags, usando `exemplos/envelope/politica-v4.json` e `exemplos/envelope/cambio.json` como arquivos externos válidos (já existem no repositório; a tabela `padrao` de `politica-v4.json` preserva os mesmos limites da baseline histórica — R$60/R$80/R$250/R$100 — então nenhum resultado financeiro de um teste BRL muda por causa dessa migração). Regras da migração:
       - testes de parser que **intencionalmente** verificam flag ausente, comando antigo ou argumentos inválidos **continuam** construindo a linha de comando incompleta necessária ao próprio cenário — não são migrados;
       - testes de entrada inexistente, envelope inválido, escrita atômica e sucesso passam a fornecer `--politica`/`--cambio` válidos, para que, a partir de T-035, a falha observada continue vindo exatamente da camada que o teste pretende verificar (arquivo de entrada, envelope, ou escrita), não do parser;
       - `ExemploCompletoTest` (T-020) passa a invocar com as quatro flags;
       - `EscritaAtomicaSaidaTest` (T-019) passa a invocar com as quatro flags em todos os cenários que pretendem ultrapassar o parser (ou seja, todos exceto os que testam o próprio contrato de argumentos, se houver).
  - **Testes obrigatórios:** `CliContratoTest` — sucesso com as quatro flags em ordem arbitrária (ainda sem `LeitorPolitica`/`LeitorCambio` reais entrando no caminho de erro — resultado de negócio é irrelevante aqui, só o exit code); subcomando ausente; subcomando diferente de `calcular`; token posicional extra depois de `calcular`; flag sem valor (última posição); quantidade ímpar de tokens; flag repetida; flag desconhecida; cada uma das quatro flags obrigatórias faltando isoladamente; comando antigo só com `--input`/`--output` (exit `2`, porque faltam `--politica`/`--cambio` — este é o único cenário autorizado a continuar usando o comando de duas flags, porque é exatamente ele que prova a rejeição). `EscritaAtomicaSaidaTest` e `ExemploCompletoTest` — suíte completa (`mvn test`) confirma que nenhum dos dois quebrou com a migração para quatro flags.
  - **Critério de conclusão:** todos os cenários acima com o exit code esperado; nenhuma escrita em `--output` nos cenários de exit `2`; nenhum teste histórico continua dependendo do comando antigo de duas flags, salvo o teste específico que confirma sua rejeição (CA-042); `mvn test` completo verde.
  - **Comando de verificação:**
    ```
    mvn -q test
    ```
  - **Commit sugerido:** `feat(T-034): reescreve parser da CLI para quatro flags obrigatorias e migra consumidores historicos de Main.run`
  - **Status:** [x] concluída

- [x] **T-035** — `Main.java`: carregar e validar política e câmbio antes do envelope
  - **O que faz:** `Main.run(...)` passa a chamar `LeitorPolitica.ler(politicaPath)` e `LeitorCambio.ler(cambioPath)` **antes** de ler e validar o envelope de despesas (spec 8.1, passo 1 antes do passo 2) — qualquer `PoliticaInvalidaException`/`CambioInvalidoException` retorna exit `2`, sem sequer abrir o arquivo de entrada. A própria construção dos `Path` (`Path.of(politicaPath)`, `Path.of(cambioPath)`, e também `Path.of(inputPath)`/`Path.of(outputPath)`) acontece **dentro** do mesmo bloco protegido — `Path.of(...)` pode lançar `InvalidPathException` quando o texto recebido não é um caminho válido no sistema operacional (ex.: caracteres proibidos no Windows), e esse cenário é tratado exatamente como as demais falhas de arquivo: exit `2`, mensagem apenas em stderr, stdout vazio, e um `--output` preexistente preservado intacto (nenhuma tentativa de escrita ocorre antes da validação de política/câmbio). Os objetos `PoliticaExterna`/`TabelaCambio` resultantes ainda não são usados pelo pipeline de regras nesta task (isso começa em T-038/T-042/T-046) — aqui o objetivo é só o contrato de execução (AMB-034) e a ordem de validação.
  - **RN atendidas:** RN-021, RN-022.
  - **CA atendidos:** CA-043, CA-044.
  - **DT/seções do plan:** DT-003 (extensão), DT-010 (extensão); plan §3.
  - **Dependências:** T-030 (`LeitorPolitica`), T-032 (`LeitorCambio`), T-034 (flags reconhecidas).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/Main.java`
    - `tests/java/com/desafio/reembolso/CliContratoTest.java`
    - `tests/java/com/desafio/reembolso/EscritaAtomicaSaidaTest.java`
  - **Passos de implementação:**
    1. Logo após extrair os quatro caminhos (T-034) e antes de qualquer leitura do arquivo de entrada, envolver a construção dos quatro `Path` **e** as chamadas a `LeitorPolitica.ler(...)`/`LeitorCambio.ler(...)` no mesmo bloco `try/catch`, capturando tanto `InvalidPathException` (da construção do `Path`) quanto `PoliticaInvalidaException`/`CambioInvalidoException` (da leitura), todos traduzidos em `err.println(...)` + `return 2`.
    2. Preservar o restante do fluxo inalterado (leitura de `--input`, `ValidadorEnvelope`, pipeline, escrita atômica) — a ordem "política/câmbio antes do envelope" é garantida só pela posição das chamadas novas no método.
  - **Testes obrigatórios:** `CliContratoTest` — `--politica` apontando para arquivo inexistente/ilegível/JSON inválido/estruturalmente inválido → exit `2`, mesmo com `--input` perfeitamente válido; mesmo conjunto de cenários para `--cambio`; `--politica` ou `--cambio` recebendo um valor que não é um caminho válido no sistema operacional (`InvalidPathException` na construção do `Path`) → exit `2`, stdout vazio, mensagem em stderr, `--output` preexistente intacto; um `--output` preexistente permanece intacto byte a byte em qualquer um desses cenários (estendendo `EscritaAtomicaSaidaTest` ou o próprio `CliContratoTest`, verificando conteúdo antes/depois); stdout vazio e mensagem em stderr em todos os casos de falha.
  - **Critério de conclusão:** todos os cenários acima verdes, inclusive o de `InvalidPathException`; `mvn test` completo verde.
  - **Comando de verificação:**
    ```
    mvn -q test "-Dtest=CliContratoTest,EscritaAtomicaSaidaTest"
    ```
  - **Commit sugerido:** `feat(T-035): carrega politica e cambio antes do envelope na CLI`
  - **Status:** [x] concluída

---

### Bloco E — Campo `despesa.moeda`

- [x] **T-036** — `validarMoeda` em `ValidadorItem`
  - **O que faz:** implementa a validação do sétimo campo do contrato (spec 4.2, RN-002, DT-014): chave `moeda` ausente do objeto → `"BRL"`, sem motivo; chave presente com valor `null` → `CAMPO_AUSENTE`; tipo não textual → `CAMPO_TIPO_INVALIDO`; texto fora de `[A-Z]{3}` → `CAMPO_FORMATO_INVALIDO` (sem trim, sem conversão de caixa). A responsabilidade de `ValidadorItem` é **exclusivamente** popular `ItemValidado.moeda` — nunca os três campos derivados de câmbio. Em **todo** caminho de produção de `ValidadorItem` (objeto válido, objeto com campos inválidos, e elemento que não é objeto), o construtor de catorze argumentos (T-029) passa a ser chamado explicitamente com `taxaCambioAplicada = null`, `dataCotacaoUtilizada = null` e `valorConvertidoBruto = null` — **inclusive para BRL**, informado ou assumido por ausência de chave: só `ResolutorCambio` (T-037) preenche os três derivados, mesmo para BRL (`taxaCambioAplicada = 1`, `valorConvertidoBruto = valor`). Para o elemento que **não é objeto** (`ITEM_TIPO_INVALIDO`), os quatro campos ficam todos nulos — `moeda = null`, `taxaCambioAplicada = null`, `dataCotacaoUtilizada = null`, `valorConvertidoBruto = null` —, nunca `"BRL"`, porque não há despesa estruturada da qual inferir moeda alguma (spec 4.2, tabela de "Elemento que não é objeto"). O construtor de dez argumentos criado em T-029 permanece existindo, mas **exclusivamente como compatibilidade para os testes históricos** (T-001 a T-021) que ainda o chamam diretamente — `ValidadorItem` não volta a usá-lo depois desta task.
  - **RN atendidas:** RN-002 (7º campo).
  - **CA atendidos:** CA-048.
  - **DT/seções do plan:** DT-014; plan §8, §9 (divisão de responsabilidade entre `ValidadorItem` e `ResolutorCambio`).
  - **Dependências:** T-022 (`CampoCanonico.MOEDA`), T-029 (construtor de catorze argumentos).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/ValidadorItem.java`
    - `tests/java/com/desafio/reembolso/pipeline/CampoMoedaTest.java`
  - **Passos de implementação:**
    1. Método privado `validarMoeda(JsonNode elemento, List<Motivo> motivos): String`, seguindo exatamente a ordem de `if` de `plan.md` §8: `elemento.get("moeda") == null` (referência Java nula, chave ausente) → devolve `"BRL"` sem tocar `motivos`; `valor.isNull()` → adiciona `CAMPO_AUSENTE`/`CampoCanonico.MOEDA`, devolve `null`; tipo != `STRING` → `CAMPO_TIPO_INVALIDO`; texto que não casa com o padrão `[A-Z]{3}` (regex, sem `trim()`/`toUpperCase()`) → `CAMPO_FORMATO_INVALIDO`; caso contrário devolve o texto como recebido.
    2. Chamar `validarMoeda` dentro de `validarItem`, na posição correspondente ao 7º campo (entre `valor` e `tem_nota_fiscal`).
    3. No retorno de `validarItem` para um elemento **objeto** (válido ou com campos inválidos), trocar a chamada do construtor de dez argumentos pelo de catorze, passando `moeda` (resultado de `validarMoeda`) e **explicitamente** `null` para `taxaCambioAplicada`, `dataCotacaoUtilizada` e `valorConvertidoBruto` — nunca os valores de compatibilidade de BRL de T-029.
    4. No retorno de `validarItem` para um elemento que **não é objeto** (`ITEM_TIPO_INVALIDO`), trocar a chamada do construtor de dez argumentos pelo de catorze, passando `moeda = null` (não `"BRL"`) e os três derivados também `null` — reflete exatamente a tabela de `spec.md` 4.2 para essa situação.
    5. Não alterar a ordem em que os motivos estruturais são adicionados à lista além da inserção de `moeda` na posição correta — isso é o que faz a ordem canônica de contrato já refletir o 7º campo. A apresentação já ordena `MOEDA` corretamente desde T-022 (`CompositorSaida.criarOrdemCampo()` já reconhece o valor) — esta task não precisa (nem deve) tocar `CompositorSaida`.
  - **Testes obrigatórios:** `CampoMoedaTest` — os quatro cenários fechados de `moeda` (ausência de chave → `"BRL"`, sem motivo; `null` explícito → `CAMPO_AUSENTE`; tipo não textual → `CAMPO_TIPO_INVALIDO`; formato fora de `[A-Z]{3}`, incluindo caixa baixa `"usd"` → `CAMPO_FORMATO_INVALIDO`); **e**, adicionalmente: um item BRL assumido por ausência de chave tem `taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto` todos `null` **antes** de `ResolutorCambio` rodar (prova de que `ValidadorItem` não antecipa os derivados, nem para BRL); um item com moeda estrangeira estruturalmente válida (ex.: `"EUR"`) também tem os três derivados `null` neste estágio; um elemento que não é objeto tem `moeda`, `taxaCambioAplicada`, `dataCotacaoUtilizada` e `valorConvertidoBruto` todos `null`.
  - **Critério de conclusão:** os quatro cenários de `moeda` verdes (CA-048), mais os três cenários novos de derivados nulos verdes; `mvn test` completo continua verde — os testes históricos que constroem `ItemValidado` diretamente pelo construtor de dez argumentos (sem passar por `ValidadorItem`) continuam intactos, porque a mudança é exclusivamente dentro de `ValidadorItem.validarItem`.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=CampoMoedaTest
    ```
  - **Commit sugerido:** `feat(T-036): implementa validarMoeda com uso exclusivo do construtor de catorze argumentos`
  - **Status:** [x] concluída

---

### Bloco F — Conversão cambial

- [x] **T-037** — Implementar `ResolutorCambio`
  - **O que faz:** cria o novo estágio de pipeline `ResolutorCambio` (spec 8.1, passo 5; plan §9), que consome os três campos de que RN-020 depende — `ItemValidado.getValor()`, `ItemValidado.getMoeda()` **e** `ItemValidado.getData()` (plan §9: "o estágio é avaliado quando os três campos de que RN-020 depende estiverem estruturalmente utilizáveis") — e uma `TabelaCambio`, devolvendo um novo `ItemValidado` com `taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto` recalculados — **sem arredondar** `valorConvertidoBruto` (DT-015, o arredondamento é responsabilidade exclusiva do `Normalizador`, T-038). Se **qualquer um** dos três — `valor`, `moeda` ou `data` — for `null`, o item é devolvido sem tentativa de resolução e sem motivo novo: não basta checar só `moeda`, porque um item com `despesa.valor` ou `despesa.data` estruturalmente inválidos, mesmo com `moeda` válida, também não tem como ser convertido. BRL: taxa `1`, data nula, `valorConvertidoBruto` igual ao valor original. Moeda estrangeira com cotação resolvida (via `TabelaCambio.cotacaoEm(...)`, que devolve `Optional<TabelaCambio.CotacaoResolvida>`, T-027): `valorConvertidoBruto = valor × taxa` (produto exato, sem `setScale`), com `taxaCambioAplicada`/`dataCotacaoUtilizada` vindos da mesma `CotacaoResolvida`. Sem cotação utilizável: os três campos ficam nulos e o motivo `MOEDA_SEM_COTACAO` (`campo = CampoCanonico.MOEDA`) é acrescentado aos motivos do item. Erro estrutural em `categoria`/`descricao`/`fornecedor`/`tem_nota_fiscal` **não** impede a conversão — só `valor`, `moeda` e `data` são checados.
  - **RN atendidas:** RN-020.
  - **CA atendidos:** CA-029, CA-030.
  - **DT/seções do plan:** DT-013, DT-015; plan §9.
  - **Dependências:** T-023 (`MotivoCodigo.MOEDA_SEM_COTACAO`), T-024 (`RegraNegocio.RN_020`), T-027 (`TabelaCambio.cotacaoEm(...)`/`CotacaoResolvida`), T-029 (`ItemValidado` de catorze argumentos), T-036 (`moeda` já populado, derivados nulos).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/ResolutorCambio.java`
    - `tests/java/com/desafio/reembolso/pipeline/ResolucaoCambioTest.java`
  - **Passos de implementação:**
    1. `resolver(ItemValidado item, TabelaCambio cambio): ItemValidado` — se `item.getValor() == null` **ou** `item.getMoeda() == null` **ou** `item.getData() == null`, devolve o item sem alteração e sem motivo novo (os três campos derivados permanecem `null`, como `ValidadorItem`/T-036 já os deixou). Checar os três, nunca só `moeda`.
    2. Se os três passaram no passo 1 e `"BRL".equals(item.getMoeda())`, devolve uma cópia do item com `taxaCambioAplicada = BigDecimal.ONE`, `dataCotacaoUtilizada = null`, `valorConvertidoBruto = item.getValor()` — mesmo caminho para BRL informado e para BRL assumido por ausência de chave.
    3. Caso contrário, `Optional<TabelaCambio.CotacaoResolvida> cotacao = cambio.cotacaoEm(item.getMoeda(), item.getData())`; se presente, `taxaCambioAplicada = cotacao.get().taxa()`, `dataCotacaoUtilizada = cotacao.get().data()`, `valorConvertidoBruto = item.getValor().multiply(cotacao.get().taxa())` (sem `setScale`); se `Optional.empty()`, os três campos derivados ficam nulos e o motivo `MOEDA_SEM_COTACAO` é acrescentado à lista de motivos do item (nova lista, item permanece imutável).
    4. `resolverLista(List<ItemValidado> itens, TabelaCambio cambio): List<ItemValidado>` — aplica `resolver` a cada item, preservando ordem.
    5. Nenhuma chamada a `Main.java` ainda — o wiring no pipeline é T-038.
  - **Testes obrigatórios:** `ResolucaoCambioTest` — BRL informado e BRL por ausência de chave (idêntico); cotação exata na data da despesa (`CotacaoResolvida.data()` devolvida é a própria data da despesa); fallback para a cotação mais recente anterior (CA-029, ex.: EUR de sábado sem cotação própria usa a de sexta, verificando que `CotacaoResolvida.data()` é a data de sexta, não a do sábado consultado); proibição de cotação futura; moeda nunca presente em `cambio.json` → `MOEDA_SEM_COTACAO` (CA-030); `item.getValor() == null` (campo `valor` estruturalmente inválido) não causa `NullPointerException` — item devolvido sem resolução e sem motivo novo; `item.getData() == null` (campo `data` estruturalmente inválido) não causa `NullPointerException` — mesmo comportamento; erro estrutural em `categoria`, `descricao`, `fornecedor` ou `tem_nota_fiscal` **não** impede a conversão — um item com esses campos inválidos, mas `valor`/`moeda`/`data` válidos, ainda tem os três derivados calculados normalmente; o motivo `MOEDA_SEM_COTACAO` carrega `campo = CampoCanonico.MOEDA`; `valorConvertidoBruto` preserva a escala plena do produto, sem arredondamento — verificado diretamente comparando o `BigDecimal` resultante de `1.005 × 1.005` contra o literal exato `"1.010025"`.
  - **Critério de conclusão:** todos os cenários acima verdes, inclusive os dois de `NullPointerException` e o de campo não-financeiro inválido; nenhuma chamada a `setScale`/`round` em `ResolutorCambio.java` (confirmável por leitura, já que não há teste automatizado que prove ausência de arredondamento em todos os caminhos possíveis — DT-015).
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=ResolucaoCambioTest
    ```
  - **Commit sugerido:** `feat(T-037): implementa ResolutorCambio com verificacao de valor/moeda/data e CotacaoResolvida`
  - **Status:** [x] concluída

- [x] **T-038** — `Normalizador` sobre `valorConvertidoBruto` + wiring da conversão no pipeline + migração dos pipelines de teste históricos
  - **O que faz:** `Normalizador` passa a normalizar `item.getValorConvertidoBruto()` em vez de `item.getValor()` diretamente — mesmo caminho para BRL e moeda estrangeira, sem `if` de BRL dentro do `Normalizador` (plan §9), e **sem nenhum fallback** para `item.getValor()`. `Main.executarPipeline` ganha o estágio `ResolutorCambio.resolverLista(...)`, inserido entre `DetectorIdDuplicado.detectar(...)` e `Normalizador.normalizarLista(...)` (spec 8.1, passo 5), usando a `TabelaCambio` já carregada em T-035. Como T-036 já deixa `valorConvertidoBruto` nulo em todo `ItemValidado` produzido por `ValidadorItem` (só `ResolutorCambio` o preenche, inclusive para BRL), **qualquer teste histórico** que exercite `ValidadorItem` → (opcionalmente `DetectorIdDuplicado`) → `Normalizador` sem passar por `ResolutorCambio` quebraria ou passaria a obter `valor_normalizado` nulo assim que esta task mudar a leitura do `Normalizador`. Por isso, esta task também migra esses pipelines de teste e cria o helper `CambioTesteSupport`, no mesmo commit — não é aceitável trocar a fonte de leitura do `Normalizador` e deixar a suíte histórica quebrada até uma task futura arrumar.
  - **RN atendidas:** RN-004 (atualizada), RN-009 (atualizada), RN-016 (extensão a moeda), RN-020.
  - **CA atendidos:** CA-028, CA-031, CA-032.
  - **DT/seções do plan:** DT-004 (extensão), DT-015; plan §9, §14.
  - **Dependências:** T-035 (`TabelaCambio` carregada no `Main`), T-036 (`valorConvertidoBruto` nulo até `ResolutorCambio` — motivo da migração), T-037 (`ResolutorCambio`).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/Normalizador.java`
    - `src/main/java/com/desafio/reembolso/Main.java`
    - `tests/java/com/desafio/reembolso/pipeline/ConversaoCambialIntegracaoTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/RegraViagemEfeitoNuloTest.java` (estendido)
    - `tests/java/com/desafio/reembolso/pipeline/CambioTesteSupport.java` (novo — helper de teste)
    - `tests/java/com/desafio/reembolso/pipeline/CamposDesconhecidosTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/CategoriaForaPoliticaTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/CompetenciaTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/ComposicaoSaidaTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/DistribuicaoTetoTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/DuplicidadeEconomicaTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/NormalizacaoCategoriaTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/NormalizacaoMonetariaTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/NotaFiscalTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/OrdemMotivosTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/ReembolsoParcialTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/TetoDiarioTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/TetoHospedagemTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/ValorNaoPositivoTest.java`

    (a lista acima reflete o estado atual do repositório nesta sessão de planejamento; o inventário do passo 5 abaixo, executado dentro da própria task, é a fonte da verdade final sobre quais arquivos precisam de fato ser tocados.)
  - **Passos de implementação:**
    1. Em `Normalizador.normalizarValor(...)`, trocar a leitura de `item.getValor()` por `item.getValorConvertidoBruto()` como entrada do `setScale(2, RoundingMode.HALF_UP)` — único ponto de arredondamento de todo o pipeline (DT-015). Nenhum `if`/fallback que leia `item.getValor()` diretamente permanece no método.
    2. Em `Main.executarPipeline`, inserir `List<ItemValidado> comCambio = ResolutorCambio.resolverLista(idsVerificados, tabelaCambio);` entre a chamada a `DetectorIdDuplicado.detectar(...)` e `Normalizador.normalizarLista(...)`, e passar `comCambio` (não mais `idsVerificados`) ao normalizador.
    3. `tabelaCambio` precisa estar acessível em `executarPipeline` — repassar como parâmetro do método (assinatura muda de `executarPipeline(Envelope envelope)` para `executarPipeline(Envelope envelope, TabelaCambio cambio)`, com a instância obtida em T-035).
    4. Criar `tests/java/com/desafio/reembolso/pipeline/CambioTesteSupport.java` — classe/helper `package-private`, usada somente por testes do pacote `pipeline`, sem nenhuma regra de produção: constrói uma `TabelaCambio` válida com `moedaBase = "BRL"` e mapa de cotações vazio (suficiente para qualquer cenário BRL, já que `ResolutorCambio` não consulta a tabela de taxas para BRL — só preenche `taxaCambioAplicada = 1`, `dataCotacaoUtilizada = null`, `valorConvertidoBruto = valor`); oferece métodos que aplicam `ResolutorCambio.resolver(...)`/`resolverLista(...)` sobre um `ItemValidado` (ou uma `List<ItemValidado>`) antes de repassar ao `Normalizador`, poupando cada teste de repetir esse encadeamento manualmente.
    5. Rodar o inventário `git grep "Normalizador.normalizar" -- tests/java/com/desafio/reembolso/pipeline` e, para cada ocorrência: se o `ItemValidado`/`ItemNormalizado` de entrada vier de `ValidadorItem` (isto é, o teste exercita o pipeline desde a validação estrutural), inserir `ResolutorCambio` (via `CambioTesteSupport`) entre `DetectorIdDuplicado` (quando o teste também o usa) e `Normalizador` — migrando o encadeamento para `ValidadorItem` → `DetectorIdDuplicado` (quando aplicável) → `ResolutorCambio` → `Normalizador`; se o teste constrói diretamente um `ItemValidado` já preparado para exercitar o `Normalizador` isoladamente (unidade), **não** precisa passar pelo pipeline inteiro — só precisa que o `ItemValidado` de entrada já tenha `valorConvertidoBruto` preenchido corretamente (via o construtor de catorze argumentos, T-029).
    6. Antes de finalizar a task, repetir o grep do passo 5 e confirmar manualmente: (a) toda chamada a `Normalizador` cuja entrada venha de `ValidadorItem` passou antes por `ResolutorCambio`; (b) testes que constroem diretamente um `ItemValidado` já preparado para a unidade isolada do `Normalizador` continuam sem executar o pipeline inteiro, e isso é aceitável; (c) nenhum fallback para `item.getValor()` foi adicionado ao `Normalizador`; (d) `mvn test` completo está verde.
  - **Testes obrigatórios:** `ConversaoCambialIntegracaoTest` — teste-canário real de ordem de arredondamento (`valor = 1.005`, `taxa = 1.005`, esperado `1,01` via pipeline completo `ResolutorCambio` + `Normalizador`, nunca `1,02`); cenário normativo USD `40,00 × 5,50 = 220,00` (CA-031); nota fiscal aplicada sobre o valor convertido — USD `40,00` (abaixo de R$100 na moeda original) convertido para `220,00`, sem nota fiscal, recusado com `NOTA_FISCAL_AUSENTE` (CA-032). `RegraViagemEfeitoNuloTest` (estendido) — um item elegível em moeda estrangeira não amplia teto algum nem afeta outros itens do mesmo dia/período; trocar a moeda de um item de `BRL` para `EUR` (com cotação válida) não altera o comportamento de RN-016 (CA-028). Toda a suíte histórica listada em "Arquivos que cria/modifica", migrada para usar `CambioTesteSupport` onde exercita o pipeline desde `ValidadorItem` — nenhuma delas fica com `valor_normalizado` nulo por engano.
  - **Critério de conclusão:** todos os cenários acima verdes; toda chamada a `Normalizador` cuja entrada venha de `ValidadorItem` passa antes por `ResolutorCambio` (confirmado pelo grep repetido do passo 6); nenhum fallback para `item.getValor()` existe em `Normalizador.java`; `mvn test` completo verde, incluindo toda a suíte histórica de T-001 a T-021 migrada (regressão: para despesas BRL, `valorConvertidoBruto == valor`, então o resultado normalizado não muda).
  - **Comando de verificação:**
    ```
    git grep "Normalizador.normalizar" -- tests/java/com/desafio/reembolso/pipeline
    mvn -q test
    ```
  - **Commit sugerido:** `feat(T-038): normaliza sobre valorConvertidoBruto e integra ResolutorCambio no pipeline`
  - **Status:** [x] concluída

- [ ] **T-039** — `MoedaSemCotacaoTest` — coexistência de motivos (8.4, item 14)
  - **O que faz:** confirma, **operacionalmente**, a exclusão de dependência declarada em `spec.md` 8.4 item 14, no ponto do backlog em que esta task acontece — **antes** de a política por centro de custo estar integrada ao avaliador (Bloco G, T-040/T-041). Nesta altura do backlog, T-039 ainda utiliza os agregadores e sobrecargas históricas (`SeletorElegiveis`, `DetectorDuplicidadeEconomica`, `AgregadorTetoDiario`, `AgregadorTetoHospedagem`), porque os componentes novos dos blocos G e H ainda não foram implementados. Para os cenários de coexistência de motivos individuais, o teste executa até `AvaliadorRegrasIndividuais` — mas, para comprovar ausência de duplicidade e de teto, o teste **continua** pelos estágios reais: `SeletorElegiveis` → `DetectorDuplicidadeEconomica` → `SeletorElegiveis` → `AgregadorTetoDiario`/`AgregadorTetoHospedagem`. Um item com `MOEDA_SEM_COTACAO` pode coexistir com `CATEGORIA_FORA_POLITICA` e/ou `FORA_COMPETENCIA` (produzidos pela sobrecarga histórica de `AvaliadorRegrasIndividuais`, que não dependem de `valor_normalizado`), mas **nunca** recebe `VALOR_NAO_POSITIVO`, `NOTA_FISCAL_AUSENTE`, `DUPLICIDADE` ou qualquer motivo de teto — e essas ausências são comprovadas fazendo o item atravessar de fato os estágios reais de seleção, duplicidade e agregação, não apenas inspecionando a lista de motivos parada em `AvaliadorRegrasIndividuais`. Esta task **não** cobre a coexistência com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — esse motivo só existe a partir de T-041; a coexistência com ele é fechada em T-041, depois que o avaliador de centro de custo passar a existir.
  - **RN atendidas:** RN-020.
  - **CA atendidos:** base de rastreabilidade para o item 14 de 8.4 (não tem CA numerado próprio — é comportamento de composição, já coberto indiretamente por CA-030 e pelos cenários de `§12.3`/`§12.4`).
  - **DT/seções do plan:** plan §2 (exclusão nova, item 14 da lista de 8.4), §9.
  - **Dependências:** T-038 (pipeline de câmbio completo).
  - **Arquivos que cria/modifica:**
    - `tests/java/com/desafio/reembolso/pipeline/MoedaSemCotacaoTest.java`
  - **Passos de implementação:**
    1. Para cada cenário, construir o item pelo pipeline real (`ValidadorItem` → `DetectorIdDuplicado` → `ResolutorCambio` → `Normalizador` → `AvaliadorRegrasIndividuais`, sobrecarga histórica `avaliar(item, envelope)`), e verificar ali a coexistência com `CATEGORIA_FORA_POLITICA`/`FORA_COMPETENCIA` e a ausência de `VALOR_NAO_POSITIVO`/`NOTA_FISCAL_AUSENTE`.
    2. Passar o resultado por `SeletorElegiveis.selecionar(...)` e confirmar que o item com `MOEDA_SEM_COTACAO` **não** está na população elegível devolvida (ele tem motivos, logo não é elegível).
    3. Passar essa população (sem o item de `MOEDA_SEM_COTACAO`, já excluído no passo 2) por `DetectorDuplicidadeEconomica.detectar(...)` e confirmar que o item nunca recebe `DUPLICIDADE`, precisamente porque nunca chega a entrar na comparação de chave econômica.
    4. Passar o resultado por um segundo `SeletorElegiveis.selecionar(...)` e pelos dois agregadores de teto históricos (`AgregadorTetoDiario.aplicar(...)`, `AgregadorTetoHospedagem.aplicar(...)`) e confirmar que o item com `MOEDA_SEM_COTACAO` nunca aparece nas listas de `ResultadoTeto` produzidas — nenhum motivo de teto (`TETO_DIARIO_APLICADO`, `TETO_DIARIO_ESGOTADO`, `TETO_HOSPEDAGEM_APLICADO`) é atribuído a ele.
    5. Se algum cenário revelar um defeito real, corrigir o código de produção **no único commit** desta task — ver a qualificação de commit único de tasks de teste, no topo deste arquivo: nunca `fix(T-039)` seguido de um segundo commit `test(T-039)`.
  - **Testes obrigatórios:** `MoedaSemCotacaoTest` — comprova, executando os estágios reais do pipeline (`SeletorElegiveis`, `DetectorDuplicidadeEconomica`, `SeletorElegiveis`, `AgregadorTetoDiario`, `AgregadorTetoHospedagem`, não apenas a saída de `AvaliadorRegrasIndividuais`): item com `MOEDA_SEM_COTACAO` não entra na população entregue ao `DetectorDuplicidadeEconomica`; não recebe `DUPLICIDADE`; não entra na população entregue aos agregadores de teto; não recebe qualquer motivo de teto; pode coexistir com `CATEGORIA_FORA_POLITICA`; pode coexistir com `FORA_COMPETENCIA`; não recebe `VALOR_NAO_POSITIVO`; não recebe `NOTA_FISCAL_AUSENTE`.
  - **Critério de conclusão:** todos os cenários acima verdes, com a execução chegando de fato aos estágios de duplicidade e teto (nunca parando em `AvaliadorRegrasIndividuais` para essas duas asserções); `mvn test` completo verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=MoedaSemCotacaoTest
    ```
  - **Commit sugerido:** `test(T-039): comprova coexistencia e exclusao de motivos com MOEDA_SEM_COTACAO` — único commit da task; se um defeito real exigir correção, a mensagem passa a `fix(T-039)`, sem um segundo commit `test(T-039)` depois.
  - **Status:** [ ] pendente

---

### Bloco G — Política por centro de custo

- [ ] **T-040** — Implementar `ResolutorPoliticaCentroCusto`
  - **O que faz:** cria `ResolutorPoliticaCentroCusto.resolver(String centroCusto, PoliticaExterna politica): TabelaPoliticaResolvida` (RN-019, DT-011, DT-016): `centroCusto == null` (já representando ausência/nulo/tipo inválido, reduzidos pela camada de envelope) ou não presente em `politica.centrosCusto` → resolve para `padrao`; presente → resolve exclusivamente para a tabela daquele centro. Comparação textual exata (`Map.get`, sem `trim`/`toLowerCase`/normalização de acento) — nunca a união das duas tabelas.
  - **RN atendidas:** RN-019.
  - **CA atendidos:** CA-024, CA-025, CA-026, CA-027.
  - **DT/seções do plan:** DT-011, DT-016; plan §6.
  - **Dependências:** T-026 (`PoliticaExterna`), T-028 (`TabelaPoliticaResolvida`).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/ResolutorPoliticaCentroCusto.java`
    - `tests/java/com/desafio/reembolso/pipeline/ResolutorPoliticaCentroCustoTest.java`
  - **Passos de implementação:**
    1. `centroCusto == null` → devolve `TabelaPoliticaResolvida` com `origem = PADRAO`, `categorias = politica.getPadrao()`, `nomeCentroCusto = null`.
    2. `politica.getCentrosCusto().get(centroCusto)` — se presente, devolve `origem = CENTRO_CUSTO`, `categorias` = a tabela exclusiva daquele centro, `nomeCentroCusto = centroCusto`; se ausente (centro desconhecido), mesmo caminho do passo 1 (`padrao`).
    3. Nenhuma lógica de comparação aproximada, trim ou normalização — `Map.get` puro.
  - **Testes obrigatórios:** `ResolutorPoliticaCentroCustoTest` — centro cadastrado (usa exclusivamente a tabela dele); centro desconhecido (usa `padrao`); `centroCusto == null` (usa `padrao`); comparação sensível a caixa (`"CC-COMERCIAL"` != `"cc-comercial"`, o segundo cai em `padrao`); categoria ausente da tabela de um centro cadastrado **não** recebe fallback para `padrao` mesmo quando `padrao` a declara (CA-025); `representacao` reembolsável só onde a tabela aplicável a declara (CA-026); categoria com `limite == 0` numa tabela de centro cadastrado é preservada em `categorias` (a decisão de recusar por limite zero é de `AvaliadorRegrasIndividuais`, T-041 — aqui só se confirma que `TabelaCategoria` chega intacta ao resolvedor de regras, CA-027).
  - **Critério de conclusão:** todos os cenários acima verdes.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=ResolutorPoliticaCentroCustoTest
    ```
  - **Commit sugerido:** `feat(T-040): implementa ResolutorPoliticaCentroCusto`
  - **Status:** [ ] pendente

- [ ] **T-041** — `AvaliadorRegrasIndividuais` consome política externa
  - **O que faz:** acrescenta a `AvaliadorRegrasIndividuais` uma nova sobrecarga que recebe `TabelaPoliticaResolvida` e `PoliticaExterna` (para o gatilho de nota fiscal, RN-009 atualizada) e avalia categoria **exclusivamente** a partir da tabela resolvida — nunca a partir do `Set<String> CATEGORIAS_REEMBOLSAVEIS` fixo do Dia 1, que não reconhece categorias dinâmicas como `representacao`. Regra fechada da nova sobrecarga: categoria ausente da tabela + `origem == PADRAO` → `CATEGORIA_FORA_POLITICA`/`RN-007`; categoria ausente da tabela + `origem == CENTRO_CUSTO` → `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`RN-019`; categoria presente com `configuracao.limite() == 0` → `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`RN-019` (só ocorre com `origem == CENTRO_CUSTO`, porque `padrao` com limite zero já foi rejeitado na leitura, T-030); categoria presente com limite positivo → nenhum motivo de categoria. Uma categoria dinâmica válida, presente na tabela resolvida com limite positivo (ex.: `representacao` em `CC-COMERCIAL`), nunca pode ser recusada pelo conjunto histórico fixo, porque a nova sobrecarga não o consulta. Como o método histórico `avaliarRn006ERn007` mistura RN-006 e RN-007 e consulta `CATEGORIAS_REEMBOLSAVEIS` internamente, a nova sobrecarga **não** o chama — ele permanece exclusivo das sobrecargas históricas (`avaliar(item)`, `avaliar(item, envelope)`), preservadas intactas para a suíte de T-006 a T-021 (migração para T-055). Um método novo e separado — `avaliarRn006(...)`, ou nome semântico equivalente — copia os motivos já existentes do item e avalia **somente** `VALOR_NAO_POSITIVO`, sem consultar `CATEGORIAS_REEMBOLSAVEIS` e sem produzir `RN-007`; a categoria é decidida integralmente pela lógica de `TabelaPoliticaResolvida` descrita acima, dentro da própria nova sobrecarga. Esta task também **fecha** a coexistência de motivos que T-039 deixou pendente: com o avaliador de centro de custo agora existindo, um item com `MOEDA_SEM_COTACAO` **e** categoria ausente da tabela de um centro de custo cadastrado coexiste com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, pelo mesmo princípio de 8.4 item 14 — categoria e competência não dependem de `valor_normalizado`, então continuam avaliadas normalmente mesmo quando o câmbio falha.
  - **RN atendidas:** RN-019, RN-009 (atualizada — gatilho de `PoliticaExterna`, não de `PoliticaReembolso`), RN-020 (coexistência de `MOEDA_SEM_COTACAO` com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` — independência das regras de categoria e competência, que não dependem de `valor_normalizado`).
  - **CA atendidos:** CA-024 a CA-027 (uso real dentro do avaliador de regras).
  - **DT/seções do plan:** DT-011; plan §6, §19.
  - **Dependências:** T-023 (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`), T-024 (`RN_019`), T-038 (pipeline de câmbio completo, necessário para o cenário de coexistência com `MOEDA_SEM_COTACAO`), T-040 (`ResolutorPoliticaCentroCusto`).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/AvaliadorRegrasIndividuais.java`
    - `tests/java/com/desafio/reembolso/pipeline/CategoriaCentroCustoTest.java`
  - **Passos de implementação:**
    1. Nova sobrecarga `avaliar(ItemNormalizado item, Envelope envelope, TabelaPoliticaResolvida tabela, PoliticaExterna politica): ItemAvaliado` e a correspondente `avaliarLista(...)`.
    2. Dentro dela, chamar um novo método `avaliarRn006(ItemNormalizado item): List<Motivo>` — copia `item.item().getMotivos()` e avalia somente `VALOR_NAO_POSITIVO` (mesma lógica de valor já usada em `avaliarRn006ERn007`, extraída para não depender de categoria); **nunca** chama `avaliarRn006ERn007` nem consulta `CATEGORIAS_REEMBOLSAVEIS`.
    3. Avaliar a categoria separadamente, exclusivamente pela `TabelaPoliticaResolvida`: `TabelaCategoria configuracao = tabela.getCategorias().get(item.categoriaNormalizada());` — se `configuracao == null`, motivo conforme `tabela.getOrigem()` (`CATEGORIA_FORA_POLITICA`/`RN_007` para `PADRAO`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`RN_019` para `CENTRO_CUSTO`); se `configuracao.limite().compareTo(BigDecimal.ZERO) == 0`, `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`/`RN_019`; caso contrário, nenhum motivo de categoria.
    4. Substituir `politica.getGatilhoNotaFiscal()` (o `PoliticaReembolso` fixo) por `politica.getNotaFiscalObrigatoriaAcimaDe()` (o `PoliticaExterna` recebido) na avaliação de RN-009, dentro desta nova sobrecarga.
    5. Manter `avaliarRn006ERn007` exatamente como está, sem nenhuma alteração — usado exclusivamente pelas sobrecargas históricas (`avaliar(item)`, `avaliar(item, envelope)`).
  - **Testes obrigatórios:** `CategoriaCentroCustoTest` — os quatro cenários de CA-024 a CA-027 exercitados diretamente contra a nova sobrecarga de `AvaliadorRegrasIndividuais` (não só contra `ResolutorPoliticaCentroCusto` isoladamente): centro desconhecido usa `padrao` e produz `CATEGORIA_FORA_POLITICA` para categoria ausente; centro cadastrado sem a categoria produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`; `limite == 0` num centro cadastrado produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` com `valor_reembolsavel` `0,00` (nunca parcial); uma categoria dinâmica válida (`representacao`, presente na tabela com limite positivo) **não** é recusada — prova de que a nova sobrecarga não consulta `CATEGORIAS_REEMBOLSAVEIS`; gatilho de nota fiscal lido de `PoliticaExterna.getNotaFiscalObrigatoriaAcimaDe()`, não de `PoliticaReembolso`; **e** um caso de coexistência — moeda estrangeira sem cotação (`MOEDA_SEM_COTACAO`, via `ResolutorCambio`) combinada com categoria ausente da tabela de um centro de custo cadastrado (`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`) produz os dois motivos simultaneamente no mesmo item.
  - **Critério de conclusão:** todos os cenários acima verdes, inclusive o de `representacao` e o de coexistência; `mvn test` completo verde (sobrecargas antigas e `avaliarRn006ERn007` intocados, suíte histórica passa sem alteração).
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=CategoriaCentroCustoTest
    ```
  - **Commit sugerido:** `feat(T-041): AvaliadorRegrasIndividuais aplica RN-019 com politica externa`
  - **Status:** [ ] pendente

- [ ] **T-042** — Wiring da política externa no `Main`
  - **O que faz:** `Main.executarPipeline` passa a resolver a `TabelaPoliticaResolvida` a partir de `envelope.getColaboradorCentroCusto()` e da `PoliticaExterna` carregada em T-035, e a chamar a nova sobrecarga de `AvaliadorRegrasIndividuais` (T-041) em vez da antiga. `centro_custo` pertence ao envelope, não ao item — por isso uma **única** `TabelaPoliticaResolvida` é calculada por execução, e essa mesma instância é reutilizada para todos os itens do envelope, nunca recalculada item a item. Agregação de tetos (Bloco H) ainda usa `PoliticaReembolso` nesta task — só a elegibilidade de categoria e o gatilho de nota fiscal passam a vir da política externa real.
  - **RN atendidas:** RN-019, RN-009 (atualizada).
  - **CA atendidos:** confirma CA-024 a CA-027 ponta a ponta via CLI.
  - **DT/seções do plan:** plan §2 (passo 7), §6.
  - **Dependências:** T-038 (câmbio já integrado ao pipeline), T-041 (nova sobrecarga do avaliador).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/Main.java`
    - `tests/java/com/desafio/reembolso/CliContratoTest.java`
  - **Passos de implementação:**
    1. `Main.executarPipeline` ganha o parâmetro `PoliticaExterna politica` (junto com `TabelaCambio cambio`, já adicionado em T-038).
    2. Resolver `TabelaPoliticaResolvida tabelaResolvida = ResolutorPoliticaCentroCusto.resolver(envelope.getColaboradorCentroCusto(), politica)` **uma única vez**, antes de chamar `avaliarLista(...)` — nunca dentro de um laço por item, porque o centro de custo pertence ao envelope, não ao item, e a mesma instância de `tabelaResolvida` vale para todos os itens dessa execução.
    3. Chamar `AvaliadorRegrasIndividuais.avaliarLista(normalizados, envelope, tabelaResolvida, politica)` (nova sobrecarga).
  - **Testes obrigatórios:** `CliContratoTest` — um cenário de integração leve confirmando que uma execução real com `--politica` apontando para uma política de teste com um centro de custo cadastrado produz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` (não `CATEGORIA_FORA_POLITICA`) para uma categoria ausente daquele centro — prova de que o `Main` está de fato usando a política carregada, não mais o `Set` fixo.
  - **Critério de conclusão:** cenário acima verde; `mvn test` completo verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=CliContratoTest
    ```
  - **Commit sugerido:** `feat(T-042): liga politica externa ao pipeline real via Main`
  - **Status:** [ ] pendente

---

### Bloco H — Periodicidade e tetos

- [ ] **T-043** — Generalizar `AgregadorTetoDiario` por periodicidade
  - **O que faz:** acrescenta a `AgregadorTetoDiario` uma nova sobrecarga que recebe, por item, a `TabelaPoliticaResolvida` aplicável, e decide participação no teto compartilhado consultando `periodicidade == DIA` na categoria resolvida — não mais o `Set<String> CATEGORIAS_TETO_DIARIO` fixo (DT-017). O motivo `TETO_DIARIO_APLICADO` carrega `RN_011` para `alimentacao`, `RN_012` para `transporte_urbano`, `RN_019` para qualquer outra categoria (pequena tabela de exceção por nome, dentro do agregador — não contradiz a generalização do mecanismo). A sobrecarga antiga (`Set` fixo + `PoliticaReembolso`) permanece intacta para a suíte histórica.
  - **RN atendidas:** RN-011, RN-012, RN-015, RN-019.
  - **CA atendidos:** CA-047 (parcial — mecanismo compartilhado).
  - **DT/seções do plan:** DT-017; plan §11.
  - **Dependências:** T-028 (`TabelaPoliticaResolvida`), T-025 (`Periodicidade`).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/AgregadorTetoDiario.java`
    - `tests/java/com/desafio/reembolso/pipeline/TetoPorPeriodicidadeTest.java`
  - **Passos de implementação:**
    1. Nova sobrecarga `aplicar(List<ItemAvaliado> itens, TabelaPoliticaResolvida tabela): List<ResultadoTeto>` — para cada item, obter explicitamente `TabelaCategoria configuracao = tabela.getCategorias().get(categoria);` e filtrar os elegíveis cujo `configuracao != null && configuracao.periodicidade() == Periodicidade.DIA` (nunca `tabela.getCategorias()` inspecionado sem passar por essa variável nomeada).
    2. Limite inicial do saldo por `(data, categoria)` vem de `configuracao.limite()`, não mais de `PoliticaReembolso`.
    3. Escolha do `RegraNegocio` do motivo `TETO_DIARIO_APLICADO`: `RN_011` se `"alimentacao".equals(categoria)`, `RN_012` se `"transporte_urbano".equals(categoria)`, `RN_019` caso contrário.
    4. Reaproveitar `aplicarCorte(...)` sem alteração (já não conhece categoria, DT-017 confirma que continua reaproveitável).
  - **Testes obrigatórios:** `TetoPorPeriodicidadeTest` — categoria externa `representacao` (`CC-COMERCIAL`, `periodicidade: "dia"`, limite R$300,00) com dois itens na mesma data compartilhando o mesmo saldo, consumido em ordem de `indiceEntrada`, motivo `TETO_DIARIO_APLICADO`/`RN_019` (CA-047).
  - **Critério de conclusão:** cenário acima verde; `mvn test` completo verde (sobrecarga antiga intocada).
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=TetoPorPeriodicidadeTest
    ```
  - **Commit sugerido:** `feat(T-043): generaliza AgregadorTetoDiario por periodicidade`
  - **Status:** [ ] pendente

- [ ] **T-044** — Criar `AgregadorTetoIndividual`
  - **O que faz:** cria `pipeline/AgregadorTetoIndividual.java`, que processa qualquer categoria com `periodicidade == DIARIA` na tabela resolvida (não só `hospedagem`): teto individual por lançamento, sem saldo compartilhado, reaproveitando `AgregadorTetoDiario.aplicarCorte(...)`. `hospedagem` produz `TETO_HOSPEDAGEM_APLICADO`/`RN_013`; qualquer outra categoria produz `TETO_INDIVIDUAL_APLICADO`/`RN_019` (AMB-037). `AgregadorTetoHospedagem.java` **não é removido** nesta task — continua existindo e sendo usado pela suíte histórica até T-055/T-056.
  - **RN atendidas:** RN-013, RN-019.
  - **CA atendidos:** CA-049.
  - **DT/seções do plan:** DT-017; plan §11, §19.
  - **Dependências:** T-023 (`TETO_INDIVIDUAL_APLICADO`), T-028 (`TabelaPoliticaResolvida`), T-043 (`aplicarCorte` já reaproveitado por outra generalização — confirma o padrão).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/AgregadorTetoIndividual.java`
    - `tests/java/com/desafio/reembolso/pipeline/AgregadorTetoIndividualTest.java`
  - **Passos de implementação:**
    1. `aplicar(List<ItemAvaliado> itens, TabelaPoliticaResolvida tabela): List<ResultadoTeto>` — para cada item, obter explicitamente `TabelaCategoria configuracao = tabela.getCategorias().get(categoria);` e filtrar os elegíveis cujo `configuracao != null && configuracao.periodicidade() == Periodicidade.DIARIA`.
    2. Para cada item aplicável, chama `AgregadorTetoDiario.aplicarCorte(item, configuracao.limite(), motivo)`, com `motivo` escolhido por `"hospedagem".equals(categoria) ? TETO_HOSPEDAGEM_APLICADO/RN_013 : TETO_INDIVIDUAL_APLICADO/RN_019`.
    3. Nunca produz `NAO_REEMBOLSADO_TETO_ESGOTADO` — não há saldo compartilhado a esgotar (cada item é avaliado isoladamente).
  - **Testes obrigatórios:** `AgregadorTetoIndividualTest` — hospedagem sob `periodicidade: "diaria"` reproduz o comportamento histórico (R$480,00 → R$250,00, `TETO_HOSPEDAGEM_APLICADO`/`RN_013`, descrição não altera o teto); categoria externa `estacionamento` (limite R$50,00, `periodicidade: "diaria"`) com despesa elegível de R$80,00 → parcial R$50,00, motivo único `TETO_INDIVIDUAL_APLICADO`/`RN_019` (CA-049); duas hospedagens elegíveis na mesma data rendem até o dobro do limite individual (nunca compartilham saldo).
  - **Critério de conclusão:** todos os cenários acima verdes.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=AgregadorTetoIndividualTest
    ```
  - **Commit sugerido:** `feat(T-044): cria AgregadorTetoIndividual para periodicidade diaria`
  - **Status:** [ ] pendente

- [ ] **T-045** — `TetoPorPeriodicidadeTest` — categoria externa sob cada periodicidade
  - **O que faz:** completa a prova de que o mecanismo de teto depende exclusivamente da `periodicidade` declarada na política, não do nome histórico da categoria (AMB-036), reunindo na mesma classe `TetoPorPeriodicidadeTest` os **quatro** cenários que demonstram essa independência: `representacao` com `periodicidade: "dia"`; `estacionamento` com `periodicidade: "diaria"`; `hospedagem` reconfigurada com `periodicidade: "dia"` — usa o teto **compartilhado** de `AgregadorTetoDiario` (`TETO_DIARIO_APLICADO`/`TETO_DIARIO_ESGOTADO`, `regra = RN_019`, não `RN_013`); `alimentacao` reconfigurada com `periodicidade: "diaria"` — usa o teto **individual** de `AgregadorTetoIndividual` (`TETO_INDIVIDUAL_APLICADO`, `regra = RN_019`, não `RN_011`). O cenário de `estacionamento` é efetivamente **acrescentado** a `TetoPorPeriodicidadeTest` nesta task — mesmo já existindo cobertura unitária semelhante em `AgregadorTetoIndividualTest` (T-044), essa cobertura vive numa classe diferente e não substitui a comprovação cruzada exigida aqui, onde os quatro cenários precisam existir lado a lado na mesma classe para demonstrar a independência do mecanismo em relação ao nome da categoria.
  - **RN atendidas:** RN-019.
  - **CA atendidos:** CA-047 e CA-049 — comprovação cruzada de que o algoritmo é escolhido pela periodicidade, não pelo nome da categoria.
  - **DT/seções do plan:** DT-017; plan §11, §17 (`TetoPorPeriodicidadeTest`, quatro cenários).
  - **Dependências:** T-043, T-044.
  - **Arquivos que cria/modifica:**
    - `tests/java/com/desafio/reembolso/pipeline/TetoPorPeriodicidadeTest.java` (mesma classe de T-043, casos adicionais)
  - **Passos de implementação:**
    1. Confirmar que o cenário de `representacao` com `"dia"` (T-043) já está presente na classe.
    2. Acrescentar efetivamente, nesta task, o cenário de `estacionamento` com `"diaria"` (limite R$50,00, despesa elegível de R$80,00 → parcial R$50,00, `TETO_INDIVIDUAL_APLICADO`/`RN_019`) — ainda que `AgregadorTetoIndividualTest` (T-044) já exercite o mesmo agregador isoladamente, o cenário precisa existir também aqui, lado a lado com os outros três, para a comprovação cruzada de CA-047/CA-049.
    3. Acrescentar o cenário de `hospedagem` reconfigurada com `"dia"` — teto compartilhado, `TETO_DIARIO_APLICADO`/`RN_019`.
    4. Acrescentar o cenário de `alimentacao` reconfigurada com `"diaria"` — teto individual, `TETO_INDIVIDUAL_APLICADO`/`RN_019`.
    5. Sem alteração de código de produção esperada — os dois agregadores já leem `periodicidade` da tabela resolvida (T-043/T-044); esta task só monta, na mesma classe, os quatro cenários que provam que o nome da categoria não é hardcoded em lugar nenhum além da pequena tabela de escolha do código do motivo. Se algum cenário revelar um defeito real, a correção entra no único commit desta task, e a mensagem passa de `test(T-045)` para `fix(T-045)`.
  - **Testes obrigatórios:** `TetoPorPeriodicidadeTest` — os quatro cenários **efetivamente presentes** na classe ao final da task: (1) `representacao` com `"dia"`; (2) `estacionamento` com `"diaria"`; (3) `hospedagem` com `"dia"` — teto compartilhado, `TETO_DIARIO_APLICADO`/`RN_019`; (4) `alimentacao` com `"diaria"` — teto individual, `TETO_INDIVIDUAL_APLICADO`/`RN_019`.
  - **Critério de conclusão:** os quatro cenários existem e passam na mesma classe `TetoPorPeriodicidadeTest`, não apenas referenciados em outra classe; `mvn test` completo verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=TetoPorPeriodicidadeTest
    ```
  - **Commit sugerido:** `test(T-045): prova que o mecanismo de teto depende de periodicidade, nao do nome da categoria`
  - **Status:** [ ] pendente

- [ ] **T-046** — Wiring dos agregadores por periodicidade no `Main`
  - **O que faz:** `Main.executarPipeline` passa a chamar as novas sobrecargas de `AgregadorTetoDiario.aplicar(elegiveis, tabelaResolvida)` e `AgregadorTetoIndividual.aplicar(elegiveis, tabelaResolvida)`, em vez de `AgregadorTetoDiario`(antigo)/`AgregadorTetoHospedagem` — usando a mesma `TabelaPoliticaResolvida` já calculada em T-042. Como esta task é o ponto em que os dois agregadores novos passam a ser efetivamente exercitados pelo `Main` real, `CliContratoTest` precisa exercer os **dois** caminhos (`"dia"` e `"diaria"`) **por `Main.run(...)`**, não só o cenário de recusa por limite zero já coberto. Estratégia de dados fechada, sem alternativa: nenhum fixture permanente novo é criado em `tests/resources` — `CliContratoTest` usa `@TempDir`; a política e o envelope de cada um dos dois cenários novos (`representacao`/`"dia"` e `estacionamento`/`"diaria"`) são escritos em arquivos temporários pelo próprio teste, dentro do `@TempDir`; `--output` também aponta para um arquivo dentro do `@TempDir`; `--cambio` usa `exemplos/envelope/cambio.json` (arquivo real já existente, mesmo quando o cenário não tem despesa em moeda estrangeira). A execução real do JAR empacotado continua pertencendo exclusivamente a T-054 — esta task só usa `Main.run(...)` in-process.
  - **RN atendidas:** RN-011 a RN-015, RN-019.
  - **CA atendidos:** confirma CA-027, CA-047 e CA-049 ponta a ponta via CLI.
  - **DT/seções do plan:** DT-017; plan §2 (passo 9), §11.
  - **Dependências:** T-042 (`TabelaPoliticaResolvida` disponível no `Main`), T-043, T-044.
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/Main.java`
    - `tests/java/com/desafio/reembolso/CliContratoTest.java`
  - **Passos de implementação:**
    1. Substituir, em `executarPipeline`, as chamadas a `AgregadorTetoDiario.aplicar(elegiveisParaTetos)` e `AgregadorTetoHospedagem.aplicar(elegiveisParaTetos)` pelas novas sobrecargas, passando `tabelaResolvida`.
    2. `CompositorSaida.compor(...)` continua recebendo duas listas de `ResultadoTeto` (uma do agregador de `"dia"`, outra do de `"diaria"`) — a assinatura de `compor` não muda, só a origem das listas.
    3. No cenário `"dia"` de `CliContratoTest`: escrever, dentro de `@TempDir`, uma política de teste com categoria dinâmica `representacao`, `periodicidade: "dia"`, `limite: 300.00`, e um envelope de teste com dois itens elegíveis dessa categoria na mesma data, exercitando o saldo compartilhado.
    4. No cenário `"diaria"` de `CliContratoTest`: escrever, dentro de `@TempDir`, uma política de teste com categoria dinâmica `estacionamento`, `periodicidade: "diaria"`, `limite: 50.00`, e um envelope de teste com um item elegível de `80.00` nessa categoria.
    5. Em ambos os cenários novos, invocar `Main.run(...)` (nunca `executarPipeline` diretamente — o objetivo é provar que o wiring real do `Main` está correto) com as quatro flags reais, `--input`/`--politica` apontando para os arquivos temporários escritos pelo próprio teste, `--cambio` apontando para `exemplos/envelope/cambio.json`, e `--output` apontando para um arquivo dentro do mesmo `@TempDir`; ler o JSON de saída produzido para as asserções.
  - **Testes obrigatórios:** `CliContratoTest` — **três** cenários:
    1. (já existente) limite zero em `CC-ENG-PLATAFORMA`: execução real com `--politica` apontando para essa tabela (hospedagem limite `0,00`) recusa o item de hospedagem com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` **antes** de qualquer agregador de teto ser exercitado (spec 8.4, item 15).
    2. Cenário `"dia"`: `representacao` (`periodicidade: "dia"`, limite `300.00`), dois itens na mesma data no envelope, saldo compartilhado — o JSON de saída, lido depois de `Main.run(...)` com as quatro flags, comprova que `AgregadorTetoDiario` (sobrecarga nova) foi de fato chamado: motivo `TETO_DIARIO_APLICADO` ou `TETO_DIARIO_ESGOTADO`, regra `RN-019` (CA-047).
    3. Cenário `"diaria"`: `estacionamento` (`periodicidade: "diaria"`, limite `50.00`), item de `80.00` no envelope — o JSON de saída, lido depois de `Main.run(...)` com as quatro flags, comprova reembolso parcial de `50.00`, motivo `TETO_INDIVIDUAL_APLICADO`, regra `RN-019` (CA-049).
  - **Critério de conclusão:** limite zero recusado antes dos tetos (cenário 1); caminho `"dia"` funcionando pelo `Main` real (cenário 2); caminho `"diaria"` funcionando pelo `Main` real (cenário 3); `mvn test` completo verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=CliContratoTest
    ```
  - **Commit sugerido:** `feat(T-046): liga agregadores por periodicidade ao pipeline real via Main`
  - **Status:** [ ] pendente

---

### Bloco I — Duplicidade e saída

- [ ] **T-047** — Estender `DetectorDuplicidadeEconomica` com `moeda` na chave
  - **O que faz:** `ChaveDuplicidade` ganha o campo `moeda` (RN-010 atualizada, AMB-028): dois itens com mesma `data`/categoria normalizada/valor normalizado (já convertido, desde T-038)/fornecedor/descrição, mas em moedas diferentes, nunca são tratados como duplicata. Itens com `MOEDA_SEM_COTACAO` já chegam inelegíveis a este estágio (T-037/T-039 garantem isso) — nenhuma exclusão adicional é necessária aqui.
  - **RN atendidas:** RN-010 (atualizada).
  - **CA atendidos:** CA-033.
  - **DT/seções do plan:** plan §12.
  - **Dependências:** T-029 (`ItemValidado.getMoeda()`), T-038 (valor normalizado já convertido).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/DetectorDuplicidadeEconomica.java`
    - `tests/java/com/desafio/reembolso/pipeline/DuplicidadeEntreMoedasTest.java`
  - **Passos de implementação:**
    1. Adicionar `String moeda` ao `record ChaveDuplicidade`, populado a partir de `item.itemNormalizado().item().getMoeda()`.
    2. Nenhuma outra mudança de mecanismo — `equals`/`hashCode` do `record` já passam a diferenciar por moeda automaticamente.
  - **Testes obrigatórios:** `DuplicidadeEntreMoedasTest` — dois itens com mesma data/categoria/fornecedor/descrição e valor convertido coincidente, mas em moedas diferentes (ex.: EUR e BRL), **não** são tratados como duplicata (CA-033); o comportamento histórico (mesma moeda, chave idêntica) permanece inalterado.
  - **Critério de conclusão:** cenário acima verde; `mvn test` completo verde (suíte histórica de `DuplicidadeEconomicaTest`, T-012, usa BRL implícito em todos os itens, então a chave estendida não muda nenhum resultado existente).
  - **Comando de verificação:**
    ```
    mvn -q test "-Dtest=DuplicidadeEntreMoedasTest,DuplicidadeEconomicaTest"
    ```
  - **Commit sugerido:** `feat(T-047): adiciona moeda a chave de duplicidade economica`
  - **Status:** [ ] pendente

- [ ] **T-048** — Estender `ResultadoItem` com campos de câmbio, migrar construtores diretos e consolidar a ordem final do `CompositorSaida`
  - **O que faz:** `ResultadoItem` ganha três campos (`moeda`, `taxaCambioAplicada`, `dataCotacaoUtilizada`), populados sem recálculo a partir do `ItemValidado` de cada posição, dentro de `componentesDoRegistro(...)`. `ORDEM_CAMPO` e `ESTAGIO_POR_CODIGO` **já foram atualizados** para a ordem final desde T-022/T-023 respectivamente — esta task não os introduz pela primeira vez; ela só **revisa e amplia** `OrdemMotivosTest` para comprovar, com os motivos novos já em uso desde os blocos F/G/H, que a tabela completa de 8.3 está correta de ponta a ponta. Como `ResultadoItem` é um `record` cuja assinatura muda de sete para dez componentes, **todo** `new ResultadoItem(...)` já existente na suíte precisa ser migrado no mesmo commit. Um `record` Java pode, tecnicamente, declarar construtores adicionais que deleguem ao construtor canônico — não se trata de uma limitação da linguagem —, mas esta task **decide deliberadamente não criar** um construtor de compatibilidade de sete argumentos para `ResultadoItem`: é uma escolha de migração imediata e contrato único, não uma restrição técnica, e por isso todos os consumidores diretos são migrados no mesmo commit desta task.
  - **RN atendidas:** RN-017 (atualizada).
  - **CA atendidos:** CA-034.
  - **DT/seções do plan:** DT-019; plan §10.
  - **Dependências:** T-022 (`CampoCanonico.MOEDA` e `ORDEM_CAMPO` já atualizados), T-023 (três `MotivoCodigo` novos e `ESTAGIO_POR_CODIGO` já atualizado), T-029 (getters de câmbio em `ItemValidado`), T-038 (conversão cambial e `Normalizador` já integrados ao pipeline — necessário porque `SaidaCambioTest` exige BRL resolvido, moeda estrangeira convertida, moeda sem cotação, e conversão preservada mesmo com outro campo estruturalmente inválido).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/pipeline/CompositorSaida.java`
    - `tests/java/com/desafio/reembolso/pipeline/OrdemMotivosTest.java` (estendido)
    - `tests/java/com/desafio/reembolso/pipeline/SaidaCambioTest.java`
    - `tests/java/com/desafio/reembolso/escritor/EscritorResultadoTest.java`
    - `tests/java/com/desafio/reembolso/pipeline/TotalPeriodoTest.java`
  - **Passos de implementação:**
    1. Adicionar os três campos ao `record ResultadoItem`, com construção populada em `componentesDoRegistro(...)` a partir de `itemValidado.getMoeda()`/`getTaxaCambioAplicada()`/`getDataCotacaoUtilizada()` — nos três pontos de retorno do método (recusado antes da duplicidade, recusado por duplicidade, resultado de teto).
    2. Rodar o inventário `git grep "new ResultadoItem(" -- tests/java` e migrar **todos** os construtores diretos para a assinatura nova (dez componentes) — inclui, no mínimo, `EscritorResultadoTest.java` e `TotalPeriodoTest.java`. Nos cenários históricos sintéticos em que os campos de câmbio não são o objeto do teste, preencher explicitamente `moeda = "BRL"`, `taxaCambioAplicada = BigDecimal.ONE`, `dataCotacaoUtilizada = null` — nunca deixar um construtor de compatibilidade de sete argumentos em `ResultadoItem` para adiar essa migração.
    3. Confirmar, por leitura, que `criarOrdemCampo()` e `criarEstagios()` já refletem a tabela final (herdada de T-022/T-023) — nenhuma alteração nesses dois métodos é esperada nesta task, salvo se a conferência revelar uma divergência real, caso em que a correção entra no mesmo commit.
    4. Ampliar `OrdemMotivosTest` para exercitar, com os motivos e o `ResultadoItem` de dez componentes já disponíveis, a tabela completa de 8.3 de ponta a ponta — não apenas os sete campos/motivos históricos.
  - **Testes obrigatórios:** `OrdemMotivosTest` (estendido) — um item com `MOEDA_SEM_COTACAO` e `CATEGORIA_FORA_POLITICA` simultâneos aparece nessa ordem; um item com erro estrutural em `despesa.moeda` aparece na posição correta da ordem canônica de campo (entre `valor` e `tem_nota_fiscal`); a tabela completa de estágios (0 a 10) é exercitada com pelo menos um motivo de cada estágio. `EscritorResultadoTest` e `TotalPeriodoTest` — migrados para a assinatura de dez componentes de `ResultadoItem`, continuam verdes sem mudança de comportamento observável (a serialização dos três campos novos só é implementada em T-049; nesta task, `EscritorResultadoTest` ainda testa exclusivamente o que já testava antes, só com o construtor atualizado). `SaidaCambioTest` — os quatro formatos fechados de `moeda`/`taxa_cambio_aplicada`/`data_cotacao_utilizada` no `ResultadoItem`:
    - **BRL válido:** `moeda = "BRL"`, `taxa_cambio_aplicada = 1`, `data_cotacao_utilizada = null`.
    - **Moeda estrangeira convertida:** `moeda` = código original válido (ex.: `"EUR"`), `taxa_cambio_aplicada` = taxa efetivamente utilizada, `data_cotacao_utilizada` = data exata ou anterior efetivamente utilizada.
    - **Campo `moeda` estruturalmente inválido:** `moeda = null`, `taxa_cambio_aplicada = null`, `data_cotacao_utilizada = null`.
    - **Moeda válida sem cotação:** `moeda` = código original válido (ex.: `"GBP"`), `taxa_cambio_aplicada = null`, `data_cotacao_utilizada = null`, `valor_normalizado = null`, `valor_reembolsavel = 0.00`, motivo `MOEDA_SEM_COTACAO` com `campo = despesa.moeda` (CA-034) — os campos nulos deste cenário são verificados individualmente, não descritos genericamente como "os três nulos", porque o conjunto de campos afetados é maior que o do cenário de moeda estruturalmente inválida (inclui também `valor_normalizado` e `valor_reembolsavel`).

    **E**, adicionalmente: um cenário em que um campo **não utilizado por RN-020** — `descricao`, `fornecedor`, `categoria` ou `tem_nota_fiscal` — está estruturalmente inválido, enquanto `valor`, `moeda` e `data` são válidos; a taxa, a data da cotação e a conversão continuam sendo resolvidas normalmente nesse item, confirmando que o erro estrutural num campo alheio a RN-020 não interrompe a resolução cambial nem sua propagação até `ResultadoItem`.
  - **Critério de conclusão:** todos os cenários acima verdes, inclusive o de campo não-financeiro inválido; nenhum `new ResultadoItem(...)` na suíte usa a assinatura antiga de sete componentes (confirmado pelo grep repetido); nenhum construtor de compatibilidade foi criado em `ResultadoItem`; `mvn -q test` completo verde — inclusive antes de T-049 alterar a serialização JSON, já que a mudança desta task é inteiramente sobre o modelo `ResultadoItem` e o `CompositorSaida`, não sobre `EscritorResultado`.
  - **Comando de verificação:**
    ```
    git grep "new ResultadoItem(" -- tests/java
    mvn -q test
    ```
  - **Commit sugerido:** `feat(T-048): estende ResultadoItem com campos de cambio e migra construtores diretos`
  - **Status:** [ ] pendente

- [ ] **T-049** — Serializar campos de câmbio em `EscritorResultado` e migrar o fixture histórico para o schema 1.2
  - **O que faz:** `EscritorResultado.registro(...)` passa a escrever `moeda`, `taxa_cambio_aplicada` e `data_cotacao_utilizada` no JSON de saída, entre `valor_informado` e `valor_normalizado` (spec 4.3). `taxa_cambio_aplicada` é sempre número JSON (nunca texto), preservando a precisão do arquivo de câmbio. Como a saída real passa a ter três campos que `tests/resources/fixtures/despesas-exemplo-esperado.json` (T-020) ainda não conhece, este mesmo fixture é atualizado **manualmente** nesta task — nunca gerado pelo próprio motor — para incluir os três campos novos nas 14 posições, conforme o contrato de 4.3 (BRL em todas: `moeda: "BRL"`, `taxa_cambio_aplicada: 1`, `data_cotacao_utilizada: null`, já que `exemplos/despesas-exemplo.json` não tem despesas em moeda estrangeira). Nenhuma decisão, valor reembolsável ou motivo histórico muda — só o schema de auditoria ganha os três campos da spec 1.2.
  - **RN atendidas:** RN-017 (atualizada).
  - **CA atendidos:** CA-034 (nível de serialização); confirma CA-001/CA-002/CA-003 sob o schema 1.2.
  - **DT/seções do plan:** plan §9, §13.
  - **Dependências:** T-048 (`ResultadoItem` com os três campos).
  - **Arquivos que cria/modifica:**
    - `src/main/java/com/desafio/reembolso/escritor/EscritorResultado.java`
    - `tests/java/com/desafio/reembolso/escritor/EscritorResultadoTest.java` (estendido)
    - `tests/resources/fixtures/despesas-exemplo-esperado.json` (migrado manualmente para o schema 1.2, campo a campo, nas 14 posições)
  - **Passos de implementação:**
    1. Em `registro(ResultadoItem resultado)`, adicionar `no.put("moeda", resultado.moeda())` (nulo-seguro — Jackson serializa `String` nulo como `null` JSON), `no.set("taxa_cambio_aplicada", resultado.taxaCambioAplicada() == null ? null : MAPPER.getNodeFactory().numberNode(resultado.taxaCambioAplicada()))` (ou equivalente, preservando a escala do `BigDecimal` sem `setScale` forçado), `no.put("data_cotacao_utilizada", resultado.dataCotacaoUtilizada() == null ? null : resultado.dataCotacaoUtilizada().toString())`.
    2. Posicionar as três chamadas entre `valor_informado` e `valor_normalizado`, respeitando a ordem de campos de 4.3.
    3. Editar manualmente `tests/resources/fixtures/despesas-exemplo-esperado.json`, inserindo `"moeda": "BRL"`, `"taxa_cambio_aplicada": 1`, `"data_cotacao_utilizada": null` em cada uma das 14 posições de `resultados` — nunca gerar o arquivo executando o motor.
    4. Rodar `ExemploCompletoTest` (T-020) contra o fixture migrado, confirmando que as 14 decisões, valores reembolsáveis e motivos permanecem idênticos aos já registrados, e que `total_reembolsavel` continua `585.43` — só o schema de auditoria mudou.
  - **Testes obrigatórios:** `EscritorResultadoTest` (estendido) — os quatro formatos de saída (mesmos cenários de `SaidaCambioTest`, agora verificados no JSON serializado); `taxa_cambio_aplicada` nunca aparece como string nem em notação científica. `ExemploCompletoTest` — executado contra o fixture migrado, confirmando que nenhuma decisão, valor ou motivo histórico mudou, e que `total_reembolsavel = 585.43` se mantém.
  - **Critério de conclusão:** `EscritorResultadoTest` e `ExemploCompletoTest` verdes; o fixture migrado tem os três campos novos nas 14 posições, e nenhum outro campo do fixture foi alterado.
  - **Comando de verificação:**
    ```
    mvn -q test "-Dtest=EscritorResultadoTest,ExemploCompletoTest"
    ```
  - **Commit sugerido:** `feat(T-049): serializa campos de cambio e migra fixture historico para o schema 1.2`
  - **Status:** [ ] pendente

---

### Bloco J — Regressões e integração

- [ ] **T-050** — Fixture externa de política histórica + regressão R$585,43
  - **O que faz:** cria `tests/resources/fixtures/politica-historica.json` (equivalente aos valores hardcoded pré-Dia 2: `alimentacao` R$60/dia, `transporte_urbano` R$80/dia, `hospedagem` R$250/diária, `nota_fiscal_obrigatoria_acima_de` R$100, `vigencia` qualquer data real válida) e `tests/resources/fixtures/cambio-historico.json` (um `cambio.json` mínimo/vazio, já que `despesas-exemplo.json` não tem despesas em moeda estrangeira). Processa `exemplos/despesas-exemplo.json` de ponta a ponta com esses dois arquivos externos, invocando **definitivamente** `Main.run(...)` — não `executarPipeline` diretamente, porque o objetivo desta task é testar a cadeia inteira (leitores, parser de CLI, pipeline e escritor) sob política e câmbio externos reais, não só o núcleo isolado — e confirma `total_reembolsavel = 585.43` (CA-037), comparando estruturalmente contra `tests/resources/fixtures/despesas-exemplo-esperado.json`, **já migrado para o schema 1.2 em T-049**: esta task reutiliza esse fixture tal como T-049 o deixou, sem alterá-lo de novo.
  - **RN atendidas:** RN-019 (confirmação de que a política externa equivalente reproduz o comportamento histórico).
  - **CA atendidos:** CA-037.
  - **DT/seções do plan:** plan §16.
  - **Dependências:** T-042, T-046 (pipeline real usando política e câmbio externos de ponta a ponta), T-030/T-032 (leitores), T-049 (fixture já migrado para o schema 1.2).
  - **Arquivos que cria/modifica:**
    - `tests/resources/fixtures/politica-historica.json`
    - `tests/resources/fixtures/cambio-historico.json`
    - `tests/java/com/desafio/reembolso/RegressaoHistoricaTest.java`
  - **Passos de implementação:**
    1. Escrever os dois arquivos JSON de fixture, manualmente, seguindo o contrato de `politica.json`/`cambio.json` de spec 4.1.1.
    2. Escrever `RegressaoHistoricaTest`, invocando `Main.run(String[] args, PrintStream out, PrintStream err)` com `{"calcular", "--input", "exemplos/despesas-exemplo.json", "--politica", <fixture>, "--cambio", <fixture>, "--output", <temp>}`.
    3. Comparar o JSON de saída, campo a campo, contra `despesas-exemplo-esperado.json` (já no schema 1.2 desde T-049) — nenhuma edição adicional nesse fixture dentro desta task.
  - **Testes obrigatórios:** `RegressaoHistoricaTest` — os 14 registros (já no schema 1.2) e `total_reembolsavel = 585.43` coincidem com o fixture histórico migrado (CA-037).
  - **Critério de conclusão:** teste verde; nenhuma edição adicional em `despesas-exemplo-esperado.json` dentro desta task (a migração para o schema 1.2 já ocorreu em T-049); nenhuma alteração em código de produção.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=RegressaoHistoricaTest
    ```
  - **Commit sugerido:** `test(T-050): fixture de politica historica e regressao dos 585,43`
  - **Status:** [ ] pendente

- [ ] **T-051** — Regressão política v4 / `CC-ENG-PLATAFORMA` — R$351,43
  - **O que faz:** processa o mesmo `exemplos/despesas-exemplo.json` com `politica-v4.json` real e um envelope cujo `colaborador.centro_custo` é `"CC-ENG-PLATAFORMA"` (cadastrado na tabela), confirmando `total_reembolsavel = 351.43` (CA-038) e as quatro mudanças de item declaradas em `spec.md` §12.2 (`d-001` integral `72,50`; `d-002` parcial `2,50`; `d-010` recusado `0,00`/`CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`; `d-014` integral `61,00`), comparado estruturalmente contra um fixture novo escrito manualmente a partir de §12.2.
  - **RN atendidas:** RN-019.
  - **CA atendidos:** CA-038.
  - **DT/seções do plan:** plan §16.
  - **Dependências:** T-050 (mesma classe de teste, `RegressaoHistoricaTest`, estendida).
  - **Arquivos que cria/modifica:**
    - `tests/resources/fixtures/despesas-exemplo-v4-esperado.json`
    - `tests/resources/fixtures/envelope-cc-eng-plataforma.json` (fixture próprio: mesmo array `despesas` de `exemplos/despesas-exemplo.json`, com um bloco `colaborador.centro_custo = "CC-ENG-PLATAFORMA"` adicionado — nunca uma alteração do arquivo original em `exemplos/`, que é lido por T-050/T-021 e não deve mudar)
    - `tests/java/com/desafio/reembolso/RegressaoHistoricaTest.java` (mesma classe de T-050, caso adicional)
  - **Passos de implementação:**
    1. Criar `tests/resources/fixtures/envelope-cc-eng-plataforma.json` com o mesmo array `despesas` de `exemplos/despesas-exemplo.json` e `colaborador.centro_custo = "CC-ENG-PLATAFORMA"` — arquivo de fixture definitivo, sem alterar o arquivo original em `exemplos/`.
    2. Escrever `despesas-exemplo-v4-esperado.json` manualmente, a partir da tabela de `spec.md` §12.2 combinada com os dez itens inalterados de §4.7.
    3. Escrever o caso de teste, na mesma classe `RegressaoHistoricaTest`, processando o fixture de envelope com `politica-v4.json` real.
  - **Testes obrigatórios:** `RegressaoHistoricaTest` — `total_reembolsavel = 351.43`; os quatro itens mudados coincidem exatamente com §12.2; os dez itens restantes coincidem com o cenário histórico (CA-038).
  - **Critério de conclusão:** teste verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=RegressaoHistoricaTest
    ```
  - **Commit sugerido:** `test(T-051): regressao da politica v4 sobre CC-ENG-PLATAFORMA — total 351,43`
  - **Status:** [ ] pendente

- [ ] **T-052** — Integração envelope — Rafael / `CC-COMERCIAL` — R$1.143,26
  - **O que faz:** processa `exemplos/envelope/despesas-envelope.json` (Rafael Nkemelu, `CC-COMERCIAL`) com `exemplos/envelope/politica-v4.json` e `exemplos/envelope/cambio.json` reais, confirmando `total_reembolsavel = 1143.26` (CA-039), comparado estruturalmente contra um fixture manual construído a partir da tabela de `spec.md` §12.3 (os dez itens `e-001` a `e-010`, incluindo as três conversões cambiais e o motivo `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO` de `e-009`).
  - **RN atendidas:** RN-019, RN-020.
  - **CA atendidos:** CA-039.
  - **DT/seções do plan:** plan §16, §17.
  - **Dependências:** T-042, T-046 (pipeline completo com política e câmbio reais).
  - **Arquivos que cria/modifica:**
    - `tests/resources/fixtures/despesas-envelope-esperado.json`
    - `tests/java/com/desafio/reembolso/IntegracaoEnvelopeTest.java`
  - **Passos de implementação:**
    1. Escrever o fixture manualmente, campo a campo, a partir da tabela de §12.3 (moeda, taxa aplicada, data de cotação, decisão, reembolsável e motivo de cada um dos dez itens).
    2. Escrever o caso de teste, processando `exemplos/envelope/despesas-envelope.json` com os dois arquivos reais de `exemplos/envelope/`.
  - **Testes obrigatórios:** `IntegracaoEnvelopeTest` — os dez registros e `total_reembolsavel = 1143.26` coincidem com o fixture (CA-039); em particular, `e-009` (`coworking`) traz `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`, não `CATEGORIA_FORA_POLITICA`.
  - **Critério de conclusão:** teste verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=IntegracaoEnvelopeTest
    ```
  - **Commit sugerido:** `test(T-052): integra despesas-envelope.json (Rafael/CC-COMERCIAL) — total 1.143,26`
  - **Status:** [ ] pendente

- [ ] **T-053** — Integração envelope — Dani / centro de custo desconhecido — R$373,76
  - **O que faz:** processa `exemplos/envelope/despesas-envelope-cc-desconhecido.json` (Dani Okonkwo, `CC-SUPORTE-N2`, fora da tabela) com `exemplos/envelope/politica-v4.json` e `exemplos/envelope/cambio.json` reais, confirmando `total_reembolsavel = 373.76` (CA-040), comparado estruturalmente contra um fixture manual construído a partir de `spec.md` §12.4 (quatro itens `f-001` a `f-004`, todos sob a política `padrao`).
  - **RN atendidas:** RN-019, RN-020.
  - **CA atendidos:** CA-040.
  - **DT/seções do plan:** plan §16, §17.
  - **Dependências:** T-042, T-046.
  - **Arquivos que cria/modifica:**
    - `tests/resources/fixtures/despesas-envelope-cc-desconhecido-esperado.json`
    - `tests/java/com/desafio/reembolso/IntegracaoEnvelopeTest.java` (mesma classe de T-052, caso adicional)
  - **Passos de implementação:**
    1. Escrever o fixture manualmente a partir de §12.4.
    2. Escrever o caso de teste, processando o segundo arquivo de envelope.
  - **Testes obrigatórios:** `IntegracaoEnvelopeTest` — os quatro registros e `total_reembolsavel = 373.76` coincidem com o fixture (CA-040); confirma que `CC-SUPORTE-N2` usa integralmente `padrao`.
  - **Critério de conclusão:** teste verde.
  - **Comando de verificação:**
    ```
    mvn -q test -Dtest=IntegracaoEnvelopeTest
    ```
  - **Commit sugerido:** `test(T-053): integra despesas-envelope-cc-desconhecido.json (Dani) — total 373,76`
  - **Status:** [ ] pendente

- [ ] **T-054** — Execução real da suíte e do JAR com as quatro flags
  - **O que faz:** verificação manual, documentada, de que a suíte inteira passa (`mvn test`) e que o JAR empacotado (`mvn package`) executa de ponta a ponta com as quatro flags reais contra os **quatro** cenários financeiros do Dia 2 — (1) baseline histórica, 585.43; (2) política v4/`CC-ENG-PLATAFORMA`, 351.43, usando o fixture de envelope criado em T-051; (3) Rafael, 1143.26; (4) Dani, 373.76 —, produzindo os totais já confirmados em T-050 a T-053 também pelo binário real, não só pelos testes JUnit. As quatro saídas são escritas dentro de `target/` (diretório de build, não versionado), nunca como arquivo solto na raiz do repositório — assim nenhum resultado manual fica pendente como arquivo não rastreado em `git status`. Esta task **sempre** gera um commit, sem exceção: não há cenário de "task concluída sem commit". Se algo divergir entre o teste automatizado e a execução real do JAR, o defeito é corrigido nesta mesma task, e a mensagem de commit passa de `test(T-054)` para `fix(T-054)`.
  - **RN atendidas:** nenhuma nova — é verificação, não regra de negócio.
  - **CA atendidos:** confirma CA-037 a CA-040 via binário real, fora do JUnit.
  - **DT/seções do plan:** DT-002, DT-009; plan §16.
  - **Dependências:** T-050, T-051, T-052, T-053.
  - **Arquivos que cria/modifica:** nenhum arquivo de produção ou teste no caminho normal — os quatro arquivos de saída de verificação ficam em `target/` (`target/verificacao-585.json`, `target/verificacao-351.json`, `target/verificacao-rafael.json`, `target/verificacao-dani.json`), fora do controle de versão. Se um defeito real for encontrado, o arquivo de produção afetado é corrigido e listado no commit.
  - **Passos de implementação:**
    1. `mvn package` e confirmar que `target/motor-reembolso.jar` é produzido.
    2. Executar o comando do cenário 1 (baseline histórica, 585.43), com saída em `target/verificacao-585.json`.
    3. Executar o comando do cenário 2 (política v4/`CC-ENG-PLATAFORMA`, 351.43), usando `tests/resources/fixtures/envelope-cc-eng-plataforma.json` (T-051) como `--input` e `exemplos/envelope/politica-v4.json`/`exemplos/envelope/cambio.json` reais, com saída em `target/verificacao-351.json`.
    4. Executar o comando do cenário 3 (Rafael/`CC-COMERCIAL`, 1143.26), com saída em `target/verificacao-rafael.json`.
    5. Executar o comando do cenário 4 (Dani/centro de custo desconhecido, 373.76), com saída em `target/verificacao-dani.json`.
    6. Ler `total_reembolsavel` diretamente de cada um dos quatro arquivos em `target/` e conferir contra os valores esperados.
  - **Testes obrigatórios:** nenhum teste JUnit novo — a verificação é a execução manual documentada acima, com os quatro totais lidos diretamente dos quatro arquivos gerados em `target/`.
  - **Critério de conclusão:** os quatro totais conferem, lidos diretamente de `target/verificacao-585.json`, `target/verificacao-351.json`, `target/verificacao-rafael.json` e `target/verificacao-dani.json`; `mvn test` inteiro verde; um commit é criado ao final, sem exceção.
  - **Comando de verificação:**
    ```
    mvn -q test
    mvn -q package
    java -jar target/motor-reembolso.jar calcular --input exemplos/despesas-exemplo.json --output target/verificacao-585.json --politica tests/resources/fixtures/politica-historica.json --cambio tests/resources/fixtures/cambio-historico.json
    java -jar target/motor-reembolso.jar calcular --input tests/resources/fixtures/envelope-cc-eng-plataforma.json --output target/verificacao-351.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
    java -jar target/motor-reembolso.jar calcular --input exemplos/envelope/despesas-envelope.json --output target/verificacao-rafael.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
    java -jar target/motor-reembolso.jar calcular --input exemplos/envelope/despesas-envelope-cc-desconhecido.json --output target/verificacao-dani.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
    ```
  - **Commit sugerido:** `test(T-054): confirma execucao real do jar com as quatro flags nos quatro cenarios` — sempre gerado, contendo no mínimo a atualização do checkbox/status da task e o export da sessão (os quatro arquivos de evidência ficam em `target/`, fora do commit, exatamente para não deixar nenhum arquivo não rastreado pendente); se um defeito real exigiu correção, a mensagem passa a `fix(T-054)`.
  - **Status:** [ ] pendente

---

### Bloco K — Remoção do legado e documentação final

- [ ] **T-055** — Migrar consumidores restantes de `PoliticaReembolso`, `AgregadorTetoHospedagem` e do construtor de compatibilidade de `ItemValidado`
  - **O que faz:** primeiro dos quatro passos exigidos por `CLAUDE.md` §6 antes de remover os dois componentes superados — **e** a migração final do construtor de dez argumentos de `ItemValidado`, criado em T-029 exclusivamente como compatibilidade incremental. A migração de `PoliticaReembolso`/`AgregadorTetoHospedagem` é **obrigatória e completa** — não uma escolha caso a caso: todo teste que hoje consome, direta ou indiretamente, `PoliticaReembolso` (inclusive via as sobrecargas antigas de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` que o usam internamente) é migrado para `PoliticaExterna`/`TabelaPoliticaResolvida` (usando a fixture de política histórica de T-050 no lugar de `PoliticaReembolso.padrao()`); todo teste que hoje chama `AgregadorTetoHospedagem.aplicar(...)` diretamente é migrado para `AgregadorTetoIndividual`. Da mesma forma, `ItemValidado` não pode manter permanentemente o construtor de dez argumentos: ele preenche `taxaCambioAplicada`/`valorConvertidoBruto` fora de `ResolutorCambio`, o único lugar autorizado a decidir esses valores (T-037) — por isso, todo teste que ainda o chama é migrado para o construtor completo de catorze argumentos nesta task. Ao final desta task, nenhum teste em `tests/java` referencia `PoliticaReembolso`, `AgregadorTetoHospedagem`, nem o construtor de dez argumentos de `ItemValidado` — as únicas ocorrências restantes de `PoliticaReembolso`/`AgregadorTetoHospedagem` em todo o repositório, dentro de `src/main/java`, são os dois arquivos-alvo e as sobrecargas de compatibilidade que ainda os chamam internamente (a serem removidas em T-056, junto do próprio construtor de dez argumentos).
  - **RN atendidas:** nenhuma nova — é migração de consumidores internos, não regra de negócio.
  - **CA atendidos:** nenhum novo — migração de consumidores internos sem alteração de comportamento normativo.
  - **DT/seções do plan:** DT-011 (nota de supersessão), DT-014/DT-015 (construtor completo de `ItemValidado`); plan §19 ("Substituídos").
  - **Dependências:** T-041 (sobrecargas novas de `AvaliadorRegrasIndividuais`), T-043 (sobrecargas novas de `AgregadorTetoDiario`), T-044 (`AgregadorTetoIndividual`), T-050 (fixture de política histórica).
  - **Arquivos que cria/modifica:**
    - Todos os arquivos apontados pelo inventário completo de sete comandos (levantado no passo 1 abaixo) — inclui, no mínimo, `TetoHospedagemTest.java`, qualquer teste de T-006 a T-021 que ainda construa `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` via as sobrecargas históricas, e `tests/java/com/desafio/reembolso/modelo/ItemValidadoCambioTest.java`.
  - **Passos de implementação:**
    1. Rodar o inventário completo, antes de qualquer migração:
       ```
       git grep "AvaliadorRegrasIndividuais.avaliar" -- tests/java
       git grep "AvaliadorRegrasIndividuais.avaliarLista" -- tests/java
       git grep "AgregadorTetoDiario.aplicar" -- tests/java
       git grep "AgregadorTetoHospedagem.aplicar" -- tests/java
       git grep "PoliticaReembolso" -- tests/java
       git grep "AgregadorTetoHospedagem" -- tests/java
       git grep "new ItemValidado(" -- tests/java
       ```
       Para cada resultado dos seis primeiros comandos: identificar qual sobrecarga está sendo utilizada; migrar as chamadas das sobrecargas históricas de `AvaliadorRegrasIndividuais`/`AgregadorTetoDiario` para as sobrecargas que recebem `PoliticaExterna`/`TabelaPoliticaResolvida` (usando a fixture de política histórica de T-050 no lugar de `PoliticaReembolso.padrao()`); preservar sem alteração as chamadas que já usam as APIs novas; migrar toda chamada de `AgregadorTetoHospedagem.aplicar(...)` para `AgregadorTetoIndividual.aplicar(...)`.
    2. Para cada resultado de `git grep "new ItemValidado(" -- tests/java`: identificar se a chamada usa o construtor antigo de dez argumentos; migrar para o construtor completo de catorze. Em testes que representam uma etapa **posterior** ao câmbio no pipeline (ex.: entrada já resolvida para `Normalizador`/`AvaliadorRegrasIndividuais`), preencher explicitamente `moeda`/`taxaCambioAplicada`/`dataCotacaoUtilizada`/`valorConvertidoBruto` conforme o cenário. Em testes que representam uma etapa **anterior** ao câmbio (ex.: saída de `ValidadorItem`/`DetectorIdDuplicado` isolada), preencher `moeda` conforme o cenário do teste e deixar os três campos derivados `null` — o mesmo estado que `ValidadorItem` produziria depois de T-036.
    3. Atualizar `ItemValidadoCambioTest` (T-029) para não exigir mais a **permanência** do construtor de dez argumentos — os casos que hoje provam a delegação de compatibilidade são removidos ou reescritos para provar diretamente o comportamento do construtor de catorze argumentos, já que a compatibilidade deixa de existir a partir de T-056.
    4. Rodar `mvn test` completo após a migração.
    5. Confirmar que `git grep "PoliticaReembolso" -- tests/java`, `git grep "AgregadorTetoHospedagem" -- tests/java` e `git grep "new ItemValidado(" -- tests/java` (restrito ao construtor de dez argumentos) não retornam mais nenhuma ocorrência do padrão antigo.
  - **Testes obrigatórios:** suíte completa (`mvn test`) verde após a migração; nenhum teste órfão (que testava algo específico de `AgregadorTetoHospedagem` e não tem equivalente em `AgregadorTetoIndividual`) foi perdido silenciosamente; nenhum teste continua dependendo do construtor de dez argumentos de `ItemValidado`.
  - **Critério de conclusão:** `git grep "PoliticaReembolso" -- tests/java` e `git grep "AgregadorTetoHospedagem" -- tests/java` não retornam nenhuma ocorrência; nenhum teste depende mais do construtor de dez argumentos de `ItemValidado`; as únicas ocorrências remanescentes de `PoliticaReembolso`/`AgregadorTetoHospedagem` em `src/main/java` são os dois arquivos-alvo e as sobrecargas de compatibilidade que os chamam internamente; `mvn test` verde. Registra-se aqui, para orientar T-056: a remoção das sobrecargas históricas e do construtor de dez argumentos em T-056, seguida por `mvn -q -DskipTests compile` e `mvn -q test`, é a verificação **definitiva** de que não ficou nenhum consumidor indireto — o inventário desta task é a primeira camada de garantia, não a única.
  - **Comando de verificação:**
    ```
    git grep "AvaliadorRegrasIndividuais.avaliar" -- tests/java
    git grep "AvaliadorRegrasIndividuais.avaliarLista" -- tests/java
    git grep "AgregadorTetoDiario.aplicar" -- tests/java
    git grep "AgregadorTetoHospedagem.aplicar" -- tests/java
    git grep "PoliticaReembolso" -- tests/java
    git grep "AgregadorTetoHospedagem" -- tests/java
    git grep "new ItemValidado(" -- tests/java
    mvn -q test
    ```
  - **Commit sugerido:** `refactor(T-055): migra todos os consumidores de PoliticaReembolso, AgregadorTetoHospedagem e do construtor de compatibilidade de ItemValidado`
  - **Status:** [ ] pendente

- [ ] **T-056** — Remover `PoliticaReembolso`, `AgregadorTetoHospedagem` e o construtor de compatibilidade de `ItemValidado`
  - **O que faz:** segundo, terceiro e quarto passos de `CLAUDE.md` §6: com todos os consumidores já migrados (T-055), remove `src/main/java/com/desafio/reembolso/modelo/PoliticaReembolso.java` e `src/main/java/com/desafio/reembolso/pipeline/AgregadorTetoHospedagem.java`, e **obrigatoriamente** também todas as sobrecargas e imports que dependem deles — nenhuma sobrecarga de compatibilidade que dependa de uma classe removida é mantida: as sobrecargas antigas de `AvaliadorRegrasIndividuais.avaliar(item)`/`avaliar(item, envelope)` e `AgregadorTetoDiario.aplicar(itens)` são removidas nesta task, porque T-055 já garantiu que nenhum teste as usa mais. Nesta mesma task, remove-se também o construtor de dez argumentos de `ItemValidado` (criado em T-029, exclusivamente como compatibilidade incremental) e qualquer delegação criada só para sustentá-lo — `ItemValidado` passa a ter um único construtor, o completo de catorze argumentos, porque T-055 já garantiu que nenhum teste depende mais do antigo.
  - **RN atendidas:** nenhuma nova — remoção de código superado (DT-007 → DT-011).
  - **CA atendidos:** nenhum novo — remoção controlada de componentes superados após a migração.
  - **DT/seções do plan:** DT-007 (supersessão), DT-011, DT-014 (compatibilidade de `ItemValidado` encerrada); plan §19.
  - **Dependências:** T-055.
  - **Arquivos que cria/modifica:**
    - Remove `src/main/java/com/desafio/reembolso/modelo/PoliticaReembolso.java`
    - Remove `src/main/java/com/desafio/reembolso/pipeline/AgregadorTetoHospedagem.java`
    - Remove as sobrecargas de compatibilidade de `AvaliadorRegrasIndividuais` e `AgregadorTetoDiario` que dependiam exclusivamente de `PoliticaReembolso`, e qualquer import órfão resultante.
    - `src/main/java/com/desafio/reembolso/modelo/ItemValidado.java` — remove o construtor de dez argumentos criado em T-029.
  - **Passos de implementação:**
    1. Rodar `git grep "PoliticaReembolso" -- src/main/java tests/java` e `git grep "AgregadorTetoHospedagem" -- src/main/java tests/java` — confirmar que a única ocorrência restante de cada um é o próprio arquivo-alvo (mais, no caso de `PoliticaReembolso`, as sobrecargas que serão removidas neste mesmo passo).
    2. Remover os dois arquivos.
    3. Remover as sobrecargas de compatibilidade que dependiam deles e qualquer import não utilizado resultante — nenhuma sobrecarga dependente de uma classe removida permanece.
    4. Remover o construtor de dez argumentos de `ItemValidado.java` — a classe passa a expor exclusivamente o construtor de catorze argumentos.
    5. Rodar `mvn -q -DskipTests compile` para confirmar que a remoção não quebra a compilação.
    6. Rodar `mvn -q test` completo.
    7. Rodar novamente `git grep "PoliticaReembolso" -- src/main/java tests/java`, `git grep "AgregadorTetoHospedagem" -- src/main/java tests/java` e `git grep "new ItemValidado(" -- src/main/java tests/java` (conferir manualmente que todas as ocorrências restantes usam catorze argumentos) — os dois primeiros devem retornar **zero** ocorrências.
  - **Testes obrigatórios:** nenhum teste novo — a garantia é a compilação limpa e a suíte inteira verde após a remoção.
  - **Critério de conclusão:** `git grep "PoliticaReembolso" -- src/main/java tests/java` e `git grep "AgregadorTetoHospedagem" -- src/main/java tests/java` não retornam nenhuma ocorrência; `ItemValidado.java` tem um único construtor; `mvn -q -DskipTests compile` e `mvn -q test` verdes — a compilação e a suíte completa após a remoção são a prova definitiva de que não restou consumidor indireto de nenhum dos três componentes removidos. Os greps são sempre escopados a `src/main/java tests/java` — nunca executados sem pathspec, porque `plan.md`, `tasks.md`, `DECISIONS.md` e os exports de sessão preservam referências históricas legítimas a essas classes, que não são defeito.
  - **Comando de verificação:**
    ```
    git grep "PoliticaReembolso" -- src/main/java tests/java
    git grep "AgregadorTetoHospedagem" -- src/main/java tests/java
    git grep "new ItemValidado(" -- src/main/java tests/java
    mvn -q -DskipTests compile
    mvn -q test
    ```
  - **Commit sugerido:** `refactor(T-056): remove PoliticaReembolso, AgregadorTetoHospedagem, sobrecargas dependentes e o construtor de compatibilidade de ItemValidado`
  - **Status:** [ ] pendente

- [ ] **T-057** — README com CLI de quatro flags e documentação de política/câmbio
  - **O que faz:** atualiza `README.md` (criado em T-021) para refletir o contrato de execução do Dia 2: as quatro flags obrigatórias (`--input`/`--output`/`--politica`/`--cambio`), exemplos de execução reais contra os quatro cenários financeiros (baseline histórica, política v4, Rafael, Dani), a tabela de códigos de saída atualizada (`0`/`2`/`3`, com a nota de que `2` agora também cobre política/câmbio inválidos), e como rodar a suíte de regressão (`mvn test`, incluindo `RegressaoHistoricaTest`/`IntegracaoEnvelopeTest`).
  - **RN atendidas:** nenhuma nova — mesmo escopo de T-021, estendido.
  - **CA atendidos:** nenhum novo — documentação dos comportamentos já comprovados por CA-037 a CA-044.
  - **DT/seções do plan:** plan §3, §16.
  - **Dependências:** T-054 (execução real já verificada, para documentar comandos testados de fato).
  - **Arquivos que cria/modifica:**
    - `README.md`
  - **Passos de implementação:**
    1. Atualizar a seção de execução do README, trocando o comando de duas flags pelo de quatro, em linhas separadas (sem `&&`), com caminhos relativos.
    2. Acrescentar uma seção curta explicando `politica.json`/`cambio.json` (o que são, onde encontrar exemplos — `exemplos/envelope/`) e os códigos de saída atualizados.
    3. Seguir o próprio README do zero, manualmente, conferindo que os comandos documentados produzem os totais esperados.
  - **Testes obrigatórios:** nenhum automatizado — verificação manual, seguindo o README do zero (mesmo critério de T-021).
  - **Critério de conclusão:** seguindo somente o README, os **quatro** comandos de execução — um por cenário financeiro (585.43, 351.43, 1143.26, 373.76), os mesmos arquivos verificados em T-054 — produzem os totais corretos, e `mvn test` roda a suíte inteira sem falhas.
  - **Comando de verificação:**
    ```
    mvn -q package
    mvn -q test
    java -jar target/motor-reembolso.jar calcular --input exemplos/despesas-exemplo.json --output target/verificacao-585.json --politica tests/resources/fixtures/politica-historica.json --cambio tests/resources/fixtures/cambio-historico.json
    java -jar target/motor-reembolso.jar calcular --input tests/resources/fixtures/envelope-cc-eng-plataforma.json --output target/verificacao-351.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
    java -jar target/motor-reembolso.jar calcular --input exemplos/envelope/despesas-envelope.json --output target/verificacao-rafael.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
    java -jar target/motor-reembolso.jar calcular --input exemplos/envelope/despesas-envelope-cc-desconhecido.json --output target/verificacao-dani.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
    ```
  - **Commit sugerido:** `docs(readme): [T-057] documenta CLI de quatro flags e politica/cambio externos`
  - **Status:** [ ] pendente

- [ ] **T-058** — Revisão final de rastreabilidade e confirmação do item C fora de escopo
  - **O que faz:** revisão documental de fechamento do Dia 2: confere, identificador por identificador, que RN-019 a RN-022 e CA-024 a CA-049 aparecem em pelo menos uma task deste arquivo (matriz ao final deste documento) e em pelo menos um teste executável; confirma, por leitura de `spec.md` §3/AMB-033 e `plan.md` §20, que o item C (fila de aprovação manual) permanece fora de escopo — nenhum código, task ou teste o antecipa. Task documental, sem código de produção.
  - **RN atendidas:** nenhuma nova — é auditoria de rastreabilidade.
  - **CA atendidos:** nenhum novo — auditoria final da cobertura de CA-024 a CA-049.
  - **DT/seções do plan:** plan §17 (matriz de rastreabilidade), §20.
  - **Dependências:** todas as tasks anteriores desta fase (T-022 a T-057).
  - **Arquivos que cria/modifica:**
    - `specs/001-motor-reembolso/tasks.md` (só a matriz de cobertura ao final do arquivo, se algum identificador precisar de correção — a matriz já é criada nesta mesma revisão de planejamento, então esta task é sobretudo conferência).
  - **Passos de implementação:**
    1. Percorrer a matriz "Cobertura — RN-019 a RN-022" e "Cobertura — CA-024 a CA-049" (ao final deste arquivo) e confirmar, para cada linha, que a task e o teste citados realmente existem e passam.
    2. Confirmar, por leitura, que nenhuma task deste arquivo criou `Decisao.AGUARDANDO_APROVACAO`, fila de aprovação, ou qualquer menção a "R$500" como gatilho de comportamento.
  - **Testes obrigatórios:** nenhum novo — a suíte completa (`mvn test`) já cobre todos os identificadores citados na matriz.
  - **Critério de conclusão:** matriz de cobertura sem lacunas; `git grep -i "AGUARDANDO_APROVACAO" -- src/main/java` não encontra nenhuma ocorrência — o critério é ausência de implementação em produção; menções históricas/documentais em `spec.md`, `plan.md` ou `tasks.md` não são defeito, e por isso o grep é sempre escopado a `src/main/java`, nunca executado sem pathspec.
  - **Comando de verificação:**
    ```
    mvn -q test
    git grep -i "AGUARDANDO_APROVACAO" -- src/main/java
    ```
  - **Commit sugerido:** `docs(tasks): [T-058] confirma rastreabilidade completa do Dia 2 e item C fora de escopo`
  - **Status:** [ ] pendente

---

## Cobertura — RN-001 a RN-018

| Regra | Task dona | Teste |
|---|---|---|
| RN-001 | T-004 | `EnvelopeValidoTest` |
| RN-002 | T-005 | `ContratoDoItemTest`, `ValorInformadoTest` |
| RN-003 | T-006 | `IdDuplicadoTest` |
| RN-004 | T-007 | `NormalizacaoMonetariaTest` |
| RN-005 | T-007 | `NormalizacaoCategoriaTest` |
| RN-006 | T-008 | `ValorNaoPositivoTest` |
| RN-007 | T-009 | `CategoriaForaPoliticaTest` |
| RN-008 | T-010 | `CompetenciaTest` |
| RN-009 | T-011 | `NotaFiscalTest` |
| RN-010 | T-012 | `DuplicidadeEconomicaTest` |
| RN-011 | T-013 | `TetoDiarioTest` |
| RN-012 | T-013 | `TetoDiarioTest` |
| RN-013 | T-014 | `TetoHospedagemTest` |
| RN-014 | T-013 | `ReembolsoParcialTest` |
| RN-015 | T-013 | `DistribuicaoTetoTest` |
| RN-016 | T-015 | `RegraViagemEfeitoNuloTest` |
| RN-017 | T-016 | `ComposicaoSaidaTest`, `OrdemMotivosTest` |
| RN-018 | T-017 | `TotalPeriodoTest` |

## Cobertura — CA-001 a CA-023

| Critério | Task(s) | Teste |
|---|---|---|
| CA-001 | T-020 | `ExemploCompletoTest` |
| CA-002 | T-016, T-020 | `ComposicaoSaidaTest`, `ExemploCompletoTest` |
| CA-003 | T-017, T-020 | `TotalPeriodoTest`, `ExemploCompletoTest` |
| CA-004 | T-013 | `TetoDiarioTest` |
| CA-005 | T-013 | `ReembolsoParcialTest` |
| CA-006 | T-013 | `DistribuicaoTetoTest` |
| CA-007 | T-014 | `TetoHospedagemTest` |
| CA-008 | T-011 | `NotaFiscalTest` |
| CA-009 | T-007, T-011 | `NormalizacaoMonetariaTest`, `NotaFiscalTest` |
| CA-010 | T-015 | `RegraViagemEfeitoNuloTest` |
| CA-011 | T-010 | `CompetenciaTest` |
| CA-012 | T-010 | `CompetenciaTest` |
| CA-013 | T-012 (duplicidade), T-020 (resultado final) | `DuplicidadeEconomicaTest`, `ExemploCompletoTest` |
| CA-014 | T-012 | `DuplicidadeEconomicaTest` |
| CA-015 | T-007 | `NormalizacaoCategoriaTest` |
| CA-016 | T-009 (parcial), T-020 (fechamento ponta a ponta) | `CategoriaForaPoliticaTest`, `ExemploCompletoTest` |
| CA-017 | T-008 (parcial), T-017 (total, unidade), T-020 (fechamento ponta a ponta) | `ValorNaoPositivoTest`, `TotalPeriodoTest`, `ExemploCompletoTest` |
| CA-018 | T-007 | `NormalizacaoMonetariaTest` |
| CA-019 | T-006 | `IdDuplicadoTest` |
| CA-020 | T-004 | `EnvelopeValidoTest` |
| CA-021 | T-005 | `ContratoDoItemTest` |
| CA-022 | T-005 | `ContratoDoItemTest` |
| CA-023 | T-005 | `ContratoDoItemTest` |

## Cobertura — DT-001 a DT-010

| Decisão técnica | Task(s) | Evidência de materialização |
|---|---|---|
| DT-001 | T-002 | `pom.xml` com Java 21 configurado; `mvn package` funcional na máquina de desenvolvimento |
| DT-002 | T-002 | Maven Shade Plugin configurado; `mvn package` produz exatamente `target/motor-reembolso.jar` |
| DT-003 | T-002 (exit `2`), T-004 (exit `3`), T-019 (exit `0` + fechamento conjunto) | `CliContratoTest` cobrindo os três códigos de saída |
| DT-004 | T-004 (setup `ObjectMapper`/`USE_BIG_DECIMAL_FOR_FLOATS`), T-007 (aplicação de `HALF_UP`) | `NormalizacaoMonetariaTest`, incluindo o teste-canário `100.005` |
| DT-005 | T-004 (leitura via `JsonNode`), T-005 (classificação estrutural por `JsonNodeType`) | `ContratoDoItemTest` |
| DT-006 | T-004 a T-016 (cada task materializa um passo de 8.1), T-020 (verificação ponta a ponta da ordem completa) | `ExemploCompletoTest` |
| DT-007 | T-011 (criação de `PoliticaReembolso`), T-013 e T-014 (consumo dos limites) | `NotaFiscalTest`, `TetoDiarioTest`, `TetoHospedagemTest` |
| DT-008 | T-003 | `VocabularioMotivoTest` |
| DT-009 | T-002 (convenção `*Test` adotada desde o primeiro teste), T-020 (`mvn test` executa toda a suíte, inclusive integração) | Suíte completa executada por `mvn test` |
| DT-010 | T-019 | `EscritaAtomicaSaidaTest` |

---

## Fase 8 (Dia 2) — Matrizes de cobertura

> As três tabelas abaixo são conferidas identificador por identificador (não por amostragem) — regra explícita desta rodada: "não afirme cobertura total sem conferir identificador por identificador". `PoliticaReembolso.java`/`AgregadorTetoHospedagem.java` permanecem em uso até T-055/T-056 (migração e remoção controladas, `CLAUDE.md` §6) — por isso aparecem como dependência em várias tasks intermediárias sem que isso seja uma inconsistência.

## Cobertura — RN-019 a RN-022

| Regra | Task(s) dona(s) | Teste(s) |
|---|---|---|
| RN-019 | T-025, T-026, T-028 (modelos) · T-040 (`ResolutorPoliticaCentroCusto`) · T-041, T-042 (avaliador + wiring) · T-043 a T-046 (tetos por periodicidade) | `TabelaCategoriaTest`, `PoliticaExternaTest`, `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest`, `TetoPorPeriodicidadeTest`, `AgregadorTetoIndividualTest`, `CliContratoTest` |
| RN-020 | T-027 (`TabelaCambio`) · T-029 (campos de câmbio em `ItemValidado`) · T-032, T-033 (`LeitorCambio`) · T-036 (campo `moeda`) · T-037, T-038, T-039 (`ResolutorCambio` + integração) · T-041 (coexistência com `CATEGORIA_NAO_REEMBOLSAVEL_CENTRO_CUSTO`) | `TabelaCambioTest`, `ItemValidadoCambioTest`, `LeitorCambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`, `ConversaoCambialIntegracaoTest`, `MoedaSemCotacaoTest`, `CategoriaCentroCustoTest` |
| RN-021 | T-026 (`PoliticaExterna.vigencia`) · T-030 (validação de `vigencia` no `LeitorPolitica`) | `PoliticaExternaTest`, `LeitorPoliticaTest` |
| RN-022 | T-030, T-031 (`LeitorPolitica`) · T-032, T-033 (`LeitorCambio`) · T-035 (CLI trata falha como exit `2`) | `LeitorPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |

## Cobertura — CA-024 a CA-049

| Critério | Task(s) | Teste(s) |
|---|---|---|
| CA-024 | T-040, T-041 | `ResolutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest` |
| CA-025 | T-040, T-041 | `ResolutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest` |
| CA-026 | T-040, T-041 | `ResolutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest` |
| CA-027 | T-040, T-041, T-046 (confirmação ponta a ponta) | `ResolutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest`, `CliContratoTest` |
| CA-028 | T-038 | `RegraViagemEfeitoNuloTest` (estendido) |
| CA-029 | T-037 | `ResolucaoCambioTest` |
| CA-030 | T-037 | `ResolucaoCambioTest` |
| CA-031 | T-038 | `ConversaoCambialIntegracaoTest` |
| CA-032 | T-038 | `ConversaoCambialIntegracaoTest` |
| CA-033 | T-047 | `DuplicidadeEntreMoedasTest` |
| CA-034 | T-048 (modelo), T-049 (serialização e migração do fixture histórico para o schema 1.2) | `SaidaCambioTest`, `EscritorResultadoTest`, `ExemploCompletoTest` |
| CA-035 | T-030 | `LeitorPoliticaTest` |
| CA-036 | T-030, T-031 (política), T-032, T-033 (câmbio), T-035 (CLI) | `LeitorPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |
| CA-037 | T-050 | `RegressaoHistoricaTest` |
| CA-038 | T-051 | `RegressaoHistoricaTest` |
| CA-039 | T-052 | `IntegracaoEnvelopeTest` |
| CA-040 | T-053 | `IntegracaoEnvelopeTest` |
| CA-041 | T-034 | `CliContratoTest` |
| CA-042 | T-034 | `CliContratoTest` |
| CA-043 | T-035 | `CliContratoTest` |
| CA-044 | T-035 | `CliContratoTest`, `EscritaAtomicaSaidaTest` |
| CA-045 | T-030, T-031 | `LeitorPoliticaTest` |
| CA-046 | T-032, T-033 | `LeitorCambioTest` |
| CA-047 | T-043, T-045, T-046 | `TetoPorPeriodicidadeTest`, `CliContratoTest` |
| CA-048 | T-036 | `CampoMoedaTest` |
| CA-049 | T-044, T-045, T-046 | `AgregadorTetoIndividualTest`, `TetoPorPeriodicidadeTest`, `CliContratoTest` |

Todo identificador de `RN-019` a `RN-022` e de `CA-024` a `CA-049` aparece em pelo menos uma linha das duas tabelas acima — conferido linha a linha ao escrever esta seção (a mesma conferência é reexecutada em T-058, depois que as tasks tiverem sido implementadas, sobre o estado real do código e da suíte, não só sobre o planejamento).

## Cobertura — DT-011 a DT-019

| Decisão técnica | Task(s) | Evidência de materialização planejada |
|---|---|---|
| DT-011 | T-026, T-028, T-040 | `PoliticaExternaTest`, `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentroCustoTest` |
| DT-012 | T-030 | `LeitorPoliticaTest` |
| DT-013 | T-027, T-032 | `TabelaCambioTest`, `LeitorCambioTest` |
| DT-014 | T-029, T-036 | `ItemValidadoCambioTest`, `CampoMoedaTest` |
| DT-015 | T-037, T-038 | `ResolucaoCambioTest` (produto sem arredondar), `ConversaoCambialIntegracaoTest` (canário `1.005 × 1.005`) |
| DT-016 | T-040 | `ResolutorPoliticaCentroCustoTest` (comparação textual exata) |
| DT-017 | T-043, T-044, T-045, T-046 | `TetoPorPeriodicidadeTest`, `AgregadorTetoIndividualTest`, `CliContratoTest` |
| DT-018 | T-034 | `CliContratoTest` |
| DT-019 | T-022, T-023, T-024, T-048 | `VocabularioMotivoTest`, `OrdemMotivosTest` |

## Matriz final — RN/CA do Dia 2 → Tasks → Testes previstos

| RN/CA | Tasks responsáveis | Testes previstos |
|---|---|---|
| RN-019 | T-025, T-026, T-028, T-040, T-041, T-042, T-043, T-044, T-045, T-046 | `TabelaCategoriaTest`, `PoliticaExternaTest`, `TabelaPoliticaResolvidaTest`, `ResolutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest`, `TetoPorPeriodicidadeTest`, `AgregadorTetoIndividualTest`, `CliContratoTest` |
| RN-020 | T-027, T-029, T-032, T-033, T-036, T-037, T-038, T-039, T-041 | `TabelaCambioTest`, `LeitorCambioTest`, `ItemValidadoCambioTest`, `CampoMoedaTest`, `ResolucaoCambioTest`, `ConversaoCambialIntegracaoTest`, `MoedaSemCotacaoTest`, `CategoriaCentroCustoTest` |
| RN-021 | T-026, T-030 | `PoliticaExternaTest`, `LeitorPoliticaTest` |
| RN-022 | T-030, T-031, T-032, T-033, T-035 | `LeitorPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |
| CA-024 a CA-027 | T-040, T-041, T-046 | `ResolutorPoliticaCentroCustoTest`, `CategoriaCentroCustoTest`, `CliContratoTest` |
| CA-028 a CA-034 | T-037, T-038, T-047, T-048, T-049 | `ResolucaoCambioTest`, `ConversaoCambialIntegracaoTest`, `RegraViagemEfeitoNuloTest`, `DuplicidadeEntreMoedasTest`, `SaidaCambioTest`, `EscritorResultadoTest`, `ExemploCompletoTest` |
| CA-035, CA-036 | T-030, T-031, T-032, T-033, T-035 | `LeitorPoliticaTest`, `LeitorCambioTest`, `CliContratoTest` |
| CA-037, CA-038 | T-050, T-051 | `RegressaoHistoricaTest` |
| CA-039, CA-040 | T-052, T-053 | `IntegracaoEnvelopeTest` |
| CA-041 a CA-044 | T-034, T-035 | `CliContratoTest`, `EscritaAtomicaSaidaTest` |
| CA-045, CA-046 | T-030, T-031, T-032, T-033 | `LeitorPoliticaTest`, `LeitorCambioTest` |
| CA-047 | T-043, T-045, T-046 | `TetoPorPeriodicidadeTest`, `CliContratoTest` |
| CA-048 | T-036 | `CampoMoedaTest` |
| CA-049 | T-044, T-045, T-046 | `AgregadorTetoIndividualTest`, `TetoPorPeriodicidadeTest`, `CliContratoTest` |

**Remoção do legado (`CLAUDE.md` §6):** `PoliticaReembolso.java` e `AgregadorTetoHospedagem.java` são migrados em T-055 e removidos em T-056, com verificação por `git grep` nos comandos de ambas as tasks — nenhuma das duas classes é tocada antes de todos os consumidores terem migrado.

**Item C:** confirmado fora de escopo em T-058, sem nenhuma task deste arquivo antecipando `AGUARDANDO_APROVACAO` ou qualquer comportamento de fila de aprovação — consistente com `spec.md` §3/AMB-033 e `plan.md` §20.
