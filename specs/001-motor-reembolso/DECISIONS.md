# Log de Decisões e Mudanças de Spec

> Uma entrada **toda vez** que a spec mudar. Este arquivo é a prova de que a spec
> foi tratada como artefato vivo e não como cerimônia de abertura.
>
> Spec que não muda em dois dias é spec que ninguém consultou. Mudança não é
> demérito — mudança não registrada é.

Ordem cronológica inversa: a mais recente primeiro.

---

## D-002 — Ajustes de contrato após auditoria independente · `2026-07-30`

**Gatilho:** auditoria independente da spec 1.0 aprovada, realizada em sessão separada da elaboração original, com recomputação manual dos 14 itens de `exemplos/despesas-exemplo.json` a partir das regras (não da tabela 4.7) e leitura cruzada de `spec.md` com este arquivo. A auditoria confirmou o total de R$ 585,43 e as 14 decisões, sem nenhuma divergência de cálculo, mas encontrou três problemas de gravidade média, todos de precisão de contrato — nenhum deles altera resultado do arquivo de exemplo.

**O que mudou na spec:**

1. **Classificação dos erros estruturais (4.2, RN-002, 4.5, §7).** A spec distinguia `CAMPO_TIPO_INVALIDO` de `CAMPO_FORMATO_INVALIDO` sem enunciar o critério geral — só havia dois exemplos pontuais. Passa a existir regra fechada: ausência ou nulo → `CAMPO_AUSENTE`; tipo JSON errado → `CAMPO_TIPO_INVALIDO`; tipo certo com conteúdo que viola restrição → `CAMPO_FORMATO_INVALIDO`. Booleano nunca é aceito como número, nem o inverso. Sete exemplos normativos foram adicionados em 4.2, e os casos de borda (§7) ganharam quatro linhas novas (categoria numérica, data com formato certo e calendário inexistente, nota fiscal como texto, valor booleano).
2. **`valor_informado` (4.3).** O tipo era descrito como "número, conteúdo recebido ou nulo" — união pouco clara que não dizia o que acontece quando `despesa.valor` chega com tipo inválido. Passa a ser "qualquer valor JSON ou nulo": preserva exatamente o que foi recebido em `despesa.valor`, mesmo com tipo errado (`"72,50"` continua `"72,50"`, `true` continua `true`); é nulo quando a chave está ausente, nula, ou quando o elemento de `despesas` não é objeto. `valor_normalizado` continua nulo sempre que `despesa.valor` não for número válido — isso não mudou, só ficou explícito que é independente de `valor_informado`.
3. **Matriz de dependências (8.2).** Faltava a linha de RN-003 (unicidade de `despesa.id`), apesar de a dependência já estar implícita em 8.4, item 6. Adicionada.

Ajustes de clareza que acompanharam a correção, sem mudar comportamento: a coluna "Vazio permitido" de 4.2 virou "Restrição adicional", com o texto da coluna "Tipo" simplificado para o tipo JSON puro; a exclusão de nota fiscal por valor não positivo (8.4, item 10) ganhou uma frase justificando por que é mantida mesmo sendo derivável da própria condição de RN-009 — fecha a porta para uma leitura por valor absoluto (ex.: −R$ 500,00 não exigir nota). Acrescentado `CA-023`, cobrindo simultaneamente a classificação estrutural e a preservação de `valor_informado` num único item com cinco campos malformados.

**Por quê:** os três problemas médios eram pontos onde duas implementações igualmente fiéis ao texto anterior da spec podiam divergir no código de saída — exatamente o tipo de fronteira que casos ocultos de avaliação costumam explorar. Fechar agora custa uma revisão textual; deixar aberto arriscava pontos no critério de produto por um motivo que não é erro de cálculo.

**O que isso invalidou:** nenhum cálculo dos 14 itens de `exemplos/despesas-exemplo.json` mudou — decisão, valor reembolsável e motivos de cada um permanecem idênticos aos da linha de base, e o total continua R$ 585,43. Nenhuma AMB, subdecisão ou RN foi criada, removida ou renumerada. A contagem de critérios de aceite passa de 22 (`CA-001` a `CA-022`, como registrado em D-001) para 23 (`CA-001` a `CA-023`) nesta versão 1.1.

**Tasks afetadas:** nenhuma — `tasks.md` ainda não foi elaborado; esta auditoria antecede o Dia 1 tarde.

**Custo:** alteração documental em `spec.md` (seções 4.2, 4.3, 5/RN-002, 4.5, 7, 8.2, 8.4, 9 e 10) e nesta entrada de `DECISIONS.md`. Nenhum arquivo de código tocado.

---

## D-001 — Linha de base da spec 1.0 · `2026-07-30`

**Gatilho:** análise da política de reembolso v3 cruzada com `exemplos/despesas-exemplo.json`, item a item, antes de qualquer linha de spec ser escrita. A política é ambígua em pontos que alteram o valor pago, e não havia interlocutor no RH para esclarecer.

**O que mudou na spec:** primeira versão. Não havia spec anterior — este é o marco zero, não uma alteração.

Foram identificadas **18 ambiguidades** (`AMB-001` a `AMB-018`) e 15 subdecisões qualificadas, distribuídas pelos quatro tipos:

| Tipo | Ambiguidades |
|---|---|
| Unidade de aplicação | AMB-001, AMB-005, AMB-007, AMB-011, AMB-017 |
| Fronteira | AMB-003, AMB-010, AMB-014, AMB-015 |
| Dado ausente | AMB-006, AMB-008, AMB-009, AMB-013 |
| Outra | AMB-002, AMB-004, AMB-012, AMB-016, AMB-018 |

As 15 subdecisões são `AMB-006/JANELA`, `AMB-008/FORMATO`, `AMB-008/LOCAL`, `AMB-009/FONTE`, `AMB-011/POPULACAO`, `AMB-012/CLASSIFICACAO`, `AMB-013/PISO`, `AMB-013/GATILHO`, `AMB-013/DUPLICIDADE`, `AMB-015/ESCOPO`, `AMB-015/CONTRATO`, `AMB-015/ENVELOPE`, `AMB-016/NF`, `AMB-016/EXCLUSOES` e `AMB-018/ESCOPO`.

Delas nasceram 18 regras de negócio (`RN-001` a `RN-018`) e 22 critérios de aceite (`CA-001` a `CA-022`).

**Por quê:** a política v3 enuncia condições sem enunciar consequências, usa unidades que a entrada não representa e não declara precedência entre as nove regras. Sem decidir explicitamente cada ponto, dois processamentos corretos da mesma entrada produziriam valores diferentes — que é exatamente o problema que o motor existe para resolver.

Três princípios transversais governaram as decisões e valem registro porque se repetem em várias ambiguidades:

1. **O conteúdo semântico de texto livre não é interpretado.** `descricao` e `fornecedor` não são lidos para inferir viagem, quantidade de diárias, estorno, categoria ou qualquer outro tratamento financeiro; são usados somente em comparação literal de igualdade na chave de duplicidade. Aplicado em AMB-006 (viagem não é inferida de "aeroporto"/"hotel"), AMB-008 (diárias não são extraídas de "2 diarias"), AMB-013 (estorno não é reconhecido pela palavra "estorno") e AMB-015/ESCOPO (`descricao` e `fornecedor` não são normalizados). A fronteira é essa: comparar duas descrições exatamente iguais para detectar duplicidade é permitido; interpretar o significado de uma palavra não é.
2. **Campo estruturado ausente torna a regra inerte, não inventada.** Aplicado em AMB-006 e AMB-007, que deixam a regra 6 da política sem efeito em vez de criar um proxy.
3. **Nenhum lançamento desaparece do resultado.** Aplicado em AMB-018: todo item da entrada tem exatamente um registro de saída, inclusive os recusados.

**O que isso invalidou:** nada. Não havia artefato anterior.

**Tasks afetadas:** nenhuma ainda — `tasks.md` será escrito a partir desta linha de base.

**Custo:** dois arquivos preenchidos a partir dos templates copiados, `spec.md` e este log. A análise que os precedeu percorreu os 14 itens do arquivo de exemplo e resolveu as ambiguidades em dez rodadas de decisão.

### Revisões durante a elaboração

As decisões abaixo mudaram **durante** a análise, antes de a spec existir como arquivo. Não houve versão anterior publicada e não há entrada retroativa fingindo que houve — mas o registro do que mudou e por quê é a evidência de que as decisões foram testadas contra os dados em vez de escolhidas de primeira.

**R-1 · Escopo de AMB-001 restringido a duas categorias.**
Originalmente a agregação por categoria e data valia para as três categorias. Ao decidir AMB-008, ficou evidente que hospedagem tem limite "por diária" e não "por dia" — unidades diferentes. A agregação passou a valer somente para alimentação e transporte urbano; hospedagem é avaliada por lançamento. *Efeito:* forçou ajuste de redação em AMB-002, AMB-017 e AMB-018, que haviam sido escritas presumindo que toda categoria agregava por dia.

**R-2 · AMB-002 e AMB-017 se contradiziam na letra.**
AMB-002 afirmava que "a despesa não será recusada integralmente"; AMB-017 determinava valor zero para itens posteriores ao esgotamento do teto. Aplicadas a `d-001` e `d-002`, produziam zero em `d-002` — violando AMB-002. *Correção:* AMB-002 passou a garantir o **agregado diário**, e AMB-017 passou a declarar que zero por esgotamento **não é recusa**. Foi o que criou os estados distintos `NAO_REEMBOLSADO_TETO_ESGOTADO` e `RECUSADO` no vocabulário de saída.

**R-3 · AMB-005 redefinida sobre o valor normalizado.**
A decisão original falava em "valor bruto individual", significando o valor como veio no arquivo. Ao decidir AMB-014, a normalização monetária passou a ocorrer antes de todas as validações, e "bruto" mudou de sentido: passou a significar *após a normalização de contrato, antes de qualquer corte por teto*. *Efeito:* deslocou a fronteira efetiva da nota fiscal de `100.000` para `100.005` no valor informado.

**R-4 · AMB-018 ampliada duas vezes.**
Escrita inicialmente com uma justificativa por item. AMB-016 exigiu que todos os motivos aplicáveis fossem reportados, e a decisão sobre unicidade de `despesa.id` exigiu um identificador de saída que sobrevivesse a ID ausente, inválido ou repetido. *Resultado:* a saída passou a ter uma decisão final e **uma ou mais** justificativas, mais o campo `indice_entrada` de base 1.

**R-5 · Ordem canônica ampliada com a unicidade de ID.**
A ordem inicial tinha os passos estruturais seguidos diretamente das regras individuais de negócio. A verificação de `despesa.id` repetido — descoberta ao revisar o contrato de entrada, não ao ler a política — exigiu um passo próprio entre as duas etapas, porque é violação de rastreabilidade e não de identidade econômica. *Efeito:* a ordem de processamento e a ordem de apresentação dos motivos deixaram de coincidir, e a spec passou a declará-las separadamente na seção 8.

**R-6 · Correção de identificadores.**
Durante a análise, três identificadores provisórios foram atribuídos incorretamente porque o número da pergunta foi confundido com o identificador da ambiguidade — as três decisões consumiram identificadores reservados a outras ambiguidades. Corrigidas, tornaram-se as subdecisões qualificadas `AMB-006/JANELA`, `AMB-008/FORMATO` e `AMB-008/LOCAL`, devolvendo os identificadores que hoje pertencem a AMB-010, AMB-014 e AMB-016. A partir daí, toda subdecisão passou a usar sufixo qualificado em vez de consumir identificador novo. Registro aqui porque a numeração é o eixo da rastreabilidade e um erro nela se propaga por tasks, commits e testes.

**R-7 · Omissão encontrada na conferência final.**
A decisão sobre metadados do colaborador cobria `colaborador.nome` e `colaborador.centro_custo`, mas o arquivo de exemplo tem um terceiro campo, `colaborador.id`, que ficara sem destino. Foi incluído como metadado opcional sem exigência de unicidade, e a spec passou a usar nomes qualificados — `colaborador.id` contra `despesa.id` — porque a regra `ID_DUPLICADO` alcança apenas o segundo.

**R-8 · Correções da revisão da linha de base.**
A leitura integral da spec 1.0, antes de qualquer commit, encontrou quatro defeitos de conteúdo que foram corrigidos ainda dentro desta linha de base. **(a)** O princípio "texto livre não determina resultado financeiro" era absoluto demais e contradizia AMB-011, porque `descricao` e `fornecedor` integram a chave exata de duplicidade e podem, por essa via, alterar o resultado; foi reescrito para restringir a proibição ao conteúdo semântico. **(b)** RN-016 e CA-010 afirmavam que duas entradas idênticas exceto pela descrição produzem os mesmos valores — o que é falso quando há duplicidade em jogo; passaram a testar a ausência de inferência de viagem num cenário de item único. **(c)** O aceite de RN-002 listava os motivos estruturais em ordem inversa à do contrato, com `valor` antes de `data`. **(d)** O contrato exigia que cada elemento de `despesas` fosse objeto, mas não dizia o que acontece quando não é; nasceu daí o motivo `ITEM_TIPO_INVALIDO` e o critério CA-022. Na mesma revisão, `motivos` deixou de ser lista de códigos e passou a ser lista de objetos com `codigo`, `regra` e `campo`, para satisfazer a exigência de AMB-018 de vincular cada justificativa à regra que a produziu, e o comportamento do bloco `colaborador` malformado foi fechado.

Três ajustes finais fecharam a linha de base, ainda sem publicação: **(e)** a recusa por erro estrutural passou a enumerar as quatro condições — campo ausente, nulo, de tipo inválido e de **formato inválido** —, que antes apareciam incompletas em 4.2, RN-002, AMB-015/CONTRATO e na lista de exclusões; **(f)** o registro de saída de uma posição que não é objeto passou a ser declarado campo a campo, incluindo `valor_informado` nulo, que não estava explícito; **(g)** `motivo.campo` ganhou lista fechada de sete valores canônicos na forma `despesa.<campo>`, eliminando a oscilação entre `valor` e `despesa.valor` nos exemplos, na ordem canônica, na matriz de dependências, em RN-002, CA-021 e CA-022. Com isso a spec passou de "em revisão" para **aprovada**, na mesma versão 1.0.
