# Motor de Reembolso

CLI que calcula o valor reembolsável de despesas corporativas de um colaborador
num período: lê um JSON de despesas, aplica a política de reembolso e escreve
um JSON com a decisão e a justificativa de cada item, mais o total do período.

## Requisitos

- JDK 21
- Maven (testado com 3.9+)

## Obter o projeto

```
git clone https://github.com/BrunoButtros/sdd-desafio.git
```

```
cd sdd-desafio
```

## Compilar

```
mvn package
```

Gera `target/motor-reembolso.jar`.

## Executar

```
java -jar target/motor-reembolso.jar calcular --input exemplos/despesas-exemplo.json --output resultado.json
```

Lê `exemplos/despesas-exemplo.json` e escreve o resultado em `resultado.json`,
no diretório atual.

## Rodar os testes

```
mvn test
```

Executa a suíte completa (unidade e integração).

## Contrato da CLI

```
<jar> calcular --input <arquivo-entrada.json> --output <arquivo-saida.json>
```

- `--input`: caminho para o JSON de despesas (formato descrito em
  `exemplos/despesas-exemplo.json`).
- `--output`: caminho onde o JSON de resultado é escrito. A escrita é
  atômica: em caso de falha, um arquivo de saída preexistente permanece
  intacto.

### Códigos de saída

| Código | Significado |
|---|---|
| `0` | Sucesso. Resultado escrito em `--output`. |
| `2` | Erro de uso ou de infraestrutura — argumento ausente/desconhecido, arquivo de entrada inexistente ou JSON sintaticamente inválido, falha ao escrever a saída. Nada é escrito em `--output`. |
| `3` | Envelope de entrada estruturalmente inválido (ex.: período com início posterior ao fim). Nada é escrito em `--output`. |

Em qualquer código diferente de `0`, a mensagem de erro vai para stderr e
stdout permanece vazio.

## Estrutura do projeto

```
src/main/java/...     # código de produção (pipeline de cálculo, CLI)
tests/java/...         # testes automatizados (JUnit)
tests/resources/...     # fixtures usados pelos testes
exemplos/                # entrada de referência
specs/001-motor-reembolso/ # spec, plano e tasks — fonte da verdade do comportamento
docs/RELATORIO.md         # relatório final do desafio
docs/sessions/             # exports das sessões de trabalho com o Claude Code
```

## Onde estão as regras de negócio

O comportamento do sistema — regras de reembolso, ambiguidades resolvidas e
critérios de aceite — está documentado em
[`specs/001-motor-reembolso/spec.md`](specs/001-motor-reembolso/spec.md).
O relatório do desafio está em [`docs/RELATORIO.md`](docs/RELATORIO.md).

## Valores monetários

No JSON de saída, valores monetários são números decimais (não strings),
sempre com exatamente duas casas decimais e nunca em notação científica —
por exemplo `585.43`, nunca `"585.43"` nem `585.4`.
