# CLAUDE.md

> Orientação atual para quem trabalhar neste repositório daqui em diante. Este
> arquivo descreve o estado final do projeto e não pretende reconstruir as
> instruções disponíveis durante sessões históricas.

## 1. O projeto

Motor de cálculo de reembolso de despesas corporativas. A aplicação é uma CLI
que lê despesas, política externa e câmbio externo, aplica as regras de
reembolso e grava um JSON auditável com a decisão de cada item e o total.

Não há servidor HTTP nem framework web.

## 2. Stack atual

- Java 21;
- Maven;
- Jackson para leitura e escrita de JSON;
- `BigDecimal` para valores monetários;
- JUnit 5 para testes.

## 3. Comandos principais

Build:

```text
mvn -q package
```

Testes:

```text
mvn -q test
```

O build produz `target/motor-reembolso.jar`. A execução usa o contrato:

```text
java -jar target/motor-reembolso.jar calcular --input <entrada.json> --output <saida.json> --politica <politica.json> --cambio <cambio.json>
```

As quatro flags são obrigatórias. O `README.md` contém os quatro cenários de
execução verificados e os códigos de saída.

## 4. Fontes de verdade e evidências

- `specs/001-motor-reembolso/spec.md` define o comportamento e as regras de negócio.
- `specs/001-motor-reembolso/plan.md` define a arquitetura e as decisões técnicas.
- `specs/001-motor-reembolso/tasks.md` define a decomposição e a ordem do trabalho.
- `specs/001-motor-reembolso/DECISIONS.md` registra decisões e correções da especificação.
- `docs/sessions/` preserva registros de sessões e evidências do processo SDD.
- `docs/RELATORIO.md` consolida as evidências finais do desafio.

Quando código e spec divergirem, trate o código como defeito. Se a própria spec
precisar mudar, corrija primeiro a documentação normativa e registre a decisão
em `DECISIONS.md` antes de alterar o comportamento.

## 5. Convenções de trabalho e código

- Leia integralmente a task correspondente antes de implementar uma mudança.
- Mudanças seguem as tasks e nenhuma regra de negócio entra sem teste.
- Classes de teste usam o sufixo `*Test` e são executadas por `mvn -q test`.
- Valores monetários são representados com `BigDecimal`; a saída apresenta
  valores monetários com duas casas decimais.
- Preserve a rastreabilidade entre spec, decisões, plan, tasks, testes, sessões
  e commits.
- Não altere uma regra de negócio sem refletir primeiro a mudança na
  documentação normativa.

## 6. Substituição controlada de componentes legados

O estado final não contém `PoliticaReembolso` nem `AgregadorTetoHospedagem`.
Também foram removidas as APIs e sobrecargas históricas que dependiam desses
componentes e o construtor de compatibilidade de dez argumentos de
`ItemValidado`.

A migração foi concluída de forma controlada: T-055 migrou os consumidores para
`PoliticaExterna`, `TabelaPoliticaResolvida`, `AgregadorTetoIndividual` e o
construtor completo de catorze argumentos de `ItemValidado`; T-056 removeu os
componentes e APIs antigos depois da migração e confirmou compilação e suíte
completa verdes.

Em substituições futuras, preserve essa ordem: inventarie os consumidores,
migre-os e verifique a suíte antes de remover o componente superado e suas APIs
de compatibilidade.

## 7. Fora de escopo

O Item C do envelope do Dia 2 permanece fora de escopo. Não existe estado
`AGUARDANDO_APROVACAO`, fila manual nem comportamento especial de aprovação para
itens com valor reembolsável acima de R$500.
