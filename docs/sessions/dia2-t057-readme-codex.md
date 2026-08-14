# Sessão T-057 — README final do Dia 2

> **Registro da sessão no ChatGPT Codex:** esta evidência documenta a execução da T-057 realizada no ChatGPT Codex para preservar a evidência do processo. Não é um `/export` nativo do Claude Code e não pretende reconstruir uma transcrição literal.

## Objetivo

Atualizar o `README.md` para o contrato final da política v4 e da CLI com quatro flags obrigatórias.

## Alteração realizada

O README passou a documentar:

- as quatro flags `--input`, `--output`, `--politica` e `--cambio`;
- o arquivo externo de política de reembolso;
- a tabela externa de câmbio;
- os quatro cenários financeiros validados no Dia 2;
- os códigos de saída `0`, `2` e `3`;
- a execução dos testes e as regressões `RegressaoHistoricaTest` e `IntegracaoEnvelopeTest`;
- `docs/sessions/` como diretório de registros das sessões de trabalho e evidências do processo SDD, sem atribuí-los exclusivamente ao Claude Code.

Nenhuma flag adicional foi introduzida e a instrução ativa da CLI antiga, com apenas `--input` e `--output`, foi substituída pelo contrato final.

## Verificação

Foram executados:

```text
mvn -q package
mvn -q test
```

O empacotamento gerou `target/motor-reembolso.jar` e a suíte completa concluiu sem falhas.

Também foram executados os quatro comandos documentados no README:

```text
java -jar target/motor-reembolso.jar calcular --input exemplos/despesas-exemplo.json --output target/verificacao-585.json --politica tests/resources/fixtures/politica-historica.json --cambio tests/resources/fixtures/cambio-historico.json
```

Resultado real: `total_reembolsavel = 585.43`.

```text
java -jar target/motor-reembolso.jar calcular --input tests/resources/fixtures/envelope-cc-eng-plataforma.json --output target/verificacao-351.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
```

Resultado real: `total_reembolsavel = 351.43`.

```text
java -jar target/motor-reembolso.jar calcular --input exemplos/envelope/despesas-envelope.json --output target/verificacao-rafael.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
```

Resultado real: `total_reembolsavel = 1143.26`.

```text
java -jar target/motor-reembolso.jar calcular --input exemplos/envelope/despesas-envelope-cc-desconhecido.json --output target/verificacao-dani.json --politica exemplos/envelope/politica-v4.json --cambio exemplos/envelope/cambio.json
```

Resultado real: `total_reembolsavel = 373.76`.

A suíte completa permaneceu verde.

## Resultado

O README foi validado seguindo suas próprias instruções, sem alteração de código de produção ou testes.

## Fim da sessão
