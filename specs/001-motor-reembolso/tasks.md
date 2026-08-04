# Tasks — Motor de Cálculo de Reembolso

> Cada task é pequena o bastante para virar **um commit**. Se você não consegue
> descrever o critério de aceite como "o teste X passa", a task está grande demais.
>
> Marque `[x]` conforme conclui — ao longo do caminho, não tudo no fim. O histórico
> de quando cada task foi marcada é lido na correção.

**Baseado em:** spec `1.1` (aprovada) · plan `1.0` (aprovado)
**Total de tasks:** 21 (`T-001` a `T-021`) · **Estimativa principal de commits:** 22

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

- [ ] **T-008** — Valor não positivo (RN-006)
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
  - **Status:** [ ] pendente

- [ ] **T-009** — Categoria fora da política (RN-007)
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
  - **Status:** [ ] pendente

- [ ] **T-010** — Elegibilidade temporal (RN-008)
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
  - **Status:** [ ] pendente

- [ ] **T-011** — Nota fiscal obrigatória (RN-009) e `PoliticaReembolso`
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
  - **Status:** [ ] pendente

---

## Fase 4 — Elegibilidade coletiva e tetos

- [ ] **T-012** — Duplicidade econômica (RN-010) e seleção de itens elegíveis
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
  - **Status:** [ ] pendente

- [ ] **T-013** — Tetos diários com distribuição do saldo e corte parcial (RN-011, RN-012, RN-014, RN-015)
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
  - **Status:** [ ] pendente

- [ ] **T-014** — Teto individual de hospedagem (RN-013)
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
  - **Status:** [ ] pendente

- [ ] **T-015** — Viagem sem efeito e campos desconhecidos (RN-016)
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
  - **Status:** [ ] pendente

---

## Fase 5 — Composição da saída e total

- [ ] **T-016** — Composição da saída e ordenação de motivos (RN-017)
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
  - **Status:** [ ] pendente

- [ ] **T-017** — Total do período (RN-018)
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
  - **Status:** [ ] pendente

---

## Fase 6 — CLI final e integração

- [ ] **T-018** — Escritor JSON de saída
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
  - **Status:** [ ] pendente

- [ ] **T-019** — Conclusão da CLI: escrita atômica do destino e contrato final (DT-010)
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
  - **Status:** [ ] pendente

- [ ] **T-020** — Teste de integração completo (14 itens, total R$ 585,43)
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
  - **Status:** [ ] pendente

---

## Fase 7 — Documentação final

- [ ] **T-021** — README com instruções reais de build, execução e testes
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
  - **Status:** [ ] pendente

---

## Fase 8 — Envelope (Dia 2)

Nenhuma task desta fase é criada agora. Quando a mudança de requisito oficial do Dia 2 chegar, novas tasks continuam a numeração a partir de **`T-022`** — a numeração nunca reinicia nem renumeia `T-001` a `T-021`, porque a numeração é o eixo da rastreabilidade. Nada do conteúdo dessa mudança é antecipado ou suposto neste arquivo.

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
