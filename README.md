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
java -jar target/motor-reembolso.jar calcular --input <despesas.json> --output <resultado.json> --politica <politica.json> --cambio <cambio.json>
```

As quatro flags são obrigatórias e podem aparecer em qualquer ordem:

- `--input`: arquivo JSON com o colaborador, período e despesas.
- `--output`: caminho onde o resultado JSON será escrito.
- `--politica`: arquivo JSON com a política externa de reembolso.
- `--cambio`: arquivo JSON com a tabela externa de câmbio.

### Política externa

O arquivo informado em `--politica` define a tabela `padrao` e pode definir
tabelas específicas por centro de custo. Cada categoria possui um limite e
uma periodicidade. O arquivo também contém o gatilho de obrigatoriedade da
nota fiscal. Um exemplo completo está em
`exemplos/envelope/politica-v4.json`.

### Câmbio externo

O arquivo informado em `--cambio` contém as cotações utilizadas para despesas
em moedas diferentes de BRL. Despesas em BRL não dependem de cotação externa.
Um exemplo está em `exemplos/envelope/cambio.json`.

## Cenários verificados

Os comandos abaixo usam caminhos relativos à raiz do projeto e escrevem as
saídas no diretório de build `target/`.

### 1. Baseline histórica — 585.43

```
java -jar target/motor-reembolso.jar calcular --input exemplos/despesas-exemplo.json --output target/verificacao-585.json --politica tests/resources/fixtures/politica-historica.json --cambio tests/resources/fixtures/cambio-historico.json
```

Resultado esperado: `total_reembolsavel = 585.43`.

### 2. CC-ENG-PLATAFORMA — 351.43

```
java -jar target/motor-reembolso.jar calcular --input tests/resources/fixtures/envelope-cc-eng-plataforma.json --output target/verificacao-351.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
```

Resultado esperado: `total_reembolsavel = 351.43`.

### 3. Rafael / CC-COMERCIAL — 1143.26

```
java -jar target/motor-reembolso.jar calcular --input exemplos/envelope/despesas-envelope.json --output target/verificacao-rafael.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
```

Resultado esperado: `total_reembolsavel = 1143.26`.

### 4. Dani / centro de custo desconhecido — 373.76

```
java -jar target/motor-reembolso.jar calcular --input exemplos/envelope/despesas-envelope-cc-desconhecido.json --output target/verificacao-dani.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
```

Resultado esperado: `total_reembolsavel = 373.76`.

## Rodar os testes

```
mvn test
```

Executa a suíte completa (unidade e integração).

A suíte inclui `RegressaoHistoricaTest`, com a baseline histórica e sua
regressão sob a política v4, e `IntegracaoEnvelopeTest`, com os cenários do
envelope do Dia 2.

## Contrato da CLI

```
<jar> calcular --input <arquivo-entrada.json> --output <arquivo-saida.json> --politica <arquivo-politica.json> --cambio <arquivo-cambio.json>
```

Em qualquer falha, nada é escrito em stdout; a mensagem vai para stderr. A
escrita de `--output` é atômica: em caso de falha, um arquivo de saída
preexistente permanece intacto.

### Códigos de saída

| Código | Significado |
|---|---|
| `0` | Processamento concluído com sucesso; resultado escrito em `--output`. |
| `2` | Erro de uso, infraestrutura ou configuração global: argumentos inválidos; arquivo necessário inexistente, ilegível ou com JSON sintaticamente inválido; política ou câmbio inválidos; ou falha ao escrever a saída. Nada é escrito em `--output`. |
| `3` | Envelope de despesas estruturalmente inválido, como período com início posterior ao fim. Nada é escrito em `--output`. |

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
docs/sessions/             # registros das sessões de trabalho e evidências do processo SDD
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
