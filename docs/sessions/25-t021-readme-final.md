 ▐▛███▜▌   Claude Code v2.1.222
▝▜█████▛▘  Sonnet 5 · Claude Pro
  ▘▘ ▝▝    ~\Desktop\sdd-desafio


> Vamos executar exclusivamente a T-021 — README final.                         
                                                                                
  Antes de alterar arquivos, leia:                                              
                                                                                
  * `CLAUDE.md`;                                                                
  * `README.md`;                                                                
  * `DESAFIO.md`;                                                               
  * T-021 em `specs/001-motor-reembolso/tasks.md`;                              
  * `pom.xml`;                                                                  
  * `Main.java`;                                                                
  * `exemplos/despesas-exemplo.json`.                                           
                                                                                
  Confirme primeiro:                                                            
                                                                                
  * T-020 concluída e commitada;                                                
  * branch `main` sincronizada;                                                 
  * working tree limpo.                                                         
                                                                                
  ## Arquivo autorizado                                                         
                                                                                
  Altere somente:                                                               
                                                                                
  `README.md`                                                                   
                                                                                
  Não altere `tasks.md` ainda.                                                  
  Não altere código, testes, spec, plan ou DECISIONS.                           
                                                                                
  ## Conteúdo obrigatório                                                       
                                                                                
  O README deve permitir que outra pessoa execute o projeto sem conhecer nosso  
  ambiente.                                                                     
                                                                                
  Inclua:                                                                       
                                                                                
  * objetivo resumido do projeto;                                               
  * requisitos: JDK 21 e Maven;                                                 
  * como clonar e entrar no diretório;                                          
  * como compilar;                                                              
  * como executar;                                                              
  * como rodar os testes;                                                       
  * contrato básico da CLI;                                                     
  * códigos de saída `0`, `2` e `3`;                                            
  * estrutura resumida do projeto;                                              
  * referência à spec e ao relatório;                                           
  * observação de que valores monetários usam JSON decimal com duas casas.      
                                                                                
  Use comandos em linhas separadas, sem `&&`.                                   
                                                                                
  Comandos principais:                                                          
                                                                                
  ```text                                                                       
  mvn package                                                                   
  ```                                                                           
                                                                                
  ```text                                                                       
  java -jar target/motor-reembolso.jar calcular --input                         
  exemplos/despesas-exemplo.json --output resultado.json                        
  ```                                                                           
                                                                                
  ```text                                                                       
  mvn test                                                                      
  ```                                                                           
                                                                                
  Use caminhos relativos e comandos compatíveis com PowerShell, CMD, Bash e     
  outros terminais.                                                             
                                                                                
  Não use caminhos específicos como:                                            
                                                                                
  ```text                                                                       
  C:\Users\...                                                                  
  /tmp/...                                                                      
  ```                                                                           
                                                                                
  ## Verificação real                                                           
                                                                                
  Depois de escrever o README, execute exatamente os comandos documentados:     
                                                                                
  ```powershell                                                                 
  mvn package                                                                   
  java -jar target/motor-reembolso.jar calcular --input                         
  exemplos/despesas-exemplo.json --output resultado.json                        
  mvn test                                                                      
  ```                                                                           
                                                                                
  Verifique também:                                                             
                                                                                
  * `target/motor-reembolso.jar` existe;                                        
  * o comando retorna exit `0`;                                                 
  * `resultado.json` é JSON válido;                                             
  * `total_reembolsavel` é `585.43`;                                            
  * suíte completa com 363 testes verdes;                                       
  * stdout e stderr do processamento permanecem vazios.                         
                                                                                
  Após verificar, remova somente o arquivo gerado localmente:                   
                                                                                
  ```powershell                                                                 
  Remove-Item resultado.json                                                    
  ```                                                                           
                                                                                
  Não remova arquivos versionados.                                              
                                                                                
  Depois execute:                                                               
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- README.md                                                         
  ```                                                                           
                                                                                
  Ao final, informe:                                                            
                                                                                
  * se os três comandos do README funcionaram;                                  
  * existência e nome do JAR;                                                   
  * exit code da execução;                                                      
  * total encontrado no JSON;                                                   
  * resultado da suíte;                                                         
  * conteúdo e seções criadas no README;                                        
  * `git status --short`;                                                       
  * confirmação de que apenas `README.md` foi alterado;                         
  * confirmação de que `resultado.json` foi removido;                           
  * confirmação de que não houve commit.                                        
                                                                                
  Pare para revisão.                                                            

● Vou ler os arquivos indicados antes de qualquer alteração.

  Searched for 1 pattern, read 7 files, ran 2 shell commands (ctrl+o to expand)

● Confirmações antes de alterar:

  - T-020: concluída e commitada (36d5d9b test(T-020): valida exemplo completo e
  total de 585,43).
  - Branch main: local está 1 commit à frente de origin/main (não há
  divergência/atraso — apenas commits locais ainda não enviados). Não há
  necessidade de push para esta task.
  - Working tree: limpo (git status --short sem saída).

  Vou agora escrever o README.md.

● Write(README.md)
  ⎿  Added 61 lines, removed 69 lines
        1 -# Desafio Prático — Spec Driven Development                   
        1 +# Motor de Reembolso                                          
        2  
        3 -Aula bônus de SDD, fechando a trilha:                         
        3 +CLI que calcula o valor reembolsável de despesas corporativas 
          +de um colaborador                                             
        4 +num período: lê um JSON de despesas, aplica a política de reem
          +bolso e escreve                                               
        5 +um JSON com a decisão e a justificativa de cada item, mais o t
          +otal do período.                                              
        6  
        5 -`AI Fluency` → `Claude 101` → `Claude Code 101` → `Building wi
          -th the Claude API` → `Claude Code in Action` → `Módulo SDD` → 
          -**Desafio**                                                   
        7 +## Requisitos                                                 
        8  
        7 -**Individual · 2 dias · Claude Code**                         
        9 +- JDK 21                                                      
       10 +- Maven (testado com 3.9+)                                    
       11  
        9 ----                                                           
       12 +## Obter o projeto                                            
       13  
       11 -## Comece por aqui                                            
       14 +```                                                           
       15 +git clone <url-do-repositorio>                                
       16 +```                                                           
       17  
       13 -1. **[`DESAFIO.md`](DESAFIO.md)** — o enunciado. Leia inteiro 
          -antes de escrever qualquer coisa.                             
       14 -2. **[`RUBRICA.md`](RUBRICA.md)** — como você é avaliado. É pú
          -blica de propósito; leia antes de começar.                    
       15 -3. **[`exemplos/despesas-exemplo.json`](exemplos/despesas-exem
          -plo.json)** — a entrada de referência. Não é decoração: percor
          -ra item por item antes de escrever a spec.                    
       16 -4. **[`FAQ.md`](FAQ.md)** — travou? Comece por aqui. **O instr
          -utor está fora durante o desafio**, então o FAQ é o canal de s
          -uporte.                                                       
       17 -                                                              
       18 ----                                                           
       19 -                                                              
       20 -## Como participar                                            
       21 -                                                              
       22 -**1. Faça um fork deste repositório.** Ele precisa ser público
          -, ou você não conseguirá compartilhar depois.                 
       23 -                                                              
       24 -**2. Clone o seu fork e prepare a estrutura de trabalho:**    
       25 -                                                              
       26 -```bash                                                       
       27 -git clone https://github.com/<seu-usuario>/sdd-desafio.git    
       18 +```                                                           
       19  cd sdd-desafio
       29 -cp template/CLAUDE.md .                                       
       30 -cp -r template/specs .                                        
       31 -cp -r template/docs .                                         
       32 -git add -A && git commit -m "chore: estrutura inicial a partir
          - do template"                                                 
       20  ```
       21  
       35 -<details>                                                     
       36 -<summary>PowerShell</summary>                                 
       22 +## Compilar                                                   
       23  
       38 -```powershell                                                 
       39 -git clone https://github.com/<seu-usuario>/sdd-desafio.git    
       40 -cd sdd-desafio                                                
       41 -Copy-Item template\CLAUDE.md .                                
       42 -Copy-Item template\specs . -Recurse                           
       43 -Copy-Item template\docs . -Recurse                            
       44 -git add -A; git commit -m "chore: estrutura inicial a partir d
          -o template"                                                   
       24  ```
       46 -</details>                                                    
       25 +mvn package                                                   
       26 +```                                                           
       27  
       48 -Os arquivos em `template/` são esqueletos com as perguntas que
          - cada documento precisa responder. Deixe a pasta `template/` o
          -nde está — ela serve de referência.                           
       28 +Gera `target/motor-reembolso.jar`.                            
       29  
       50 -**3. Trabalhe no seu fork**, seguindo as três regras do jogo d
          -escritas no [`DESAFIO.md`](DESAFIO.md):                       
       30 +## Executar                                                   
       31  
       52 -- Nenhum commit sem task                                      
       53 -- Explicação no chat que não está na spec é bug de spec       
       54 -- Interações exportadas (`/export`) e commitadas em `docs/sess
          -ions/`                                                        
       32 +```                                                           
       33 +java -jar target/motor-reembolso.jar calcular --input exemplos
          +/despesas-exemplo.json --output resultado.json                
       34 +```                                                           
       35  
       56 -**4. No Dia 2, às 10h**, você recebe uma mudança de requisito 
          -pelo canal da turma. Ela é obrigatória e vale 20 pontos. Chegu
          -e nesse momento com o sistema base funcionando e testado.     
       36 +Lê `exemplos/despesas-exemplo.json` e escreve o resultado em `
          +resultado.json`,                                              
       37 +no diretório atual.                                           
       38  
       58 -> Durante os dois dias o instrutor está de férias e não respon
          -de mensagens. Dúvida de processo: [`FAQ.md`](FAQ.md). Dúvida s
          -obre o que a política do RH significa não tem resposta — decid
          -ir isso é o exercício.                                        
       39 +## Rodar os testes                                            
       40  
       60 -**5. Entregue** enviando o link do seu fork no formulário. Pra
          -zo: **Dia 2, 18h**.                                           
       41 +```                                                           
       42 +mvn test                                                      
       43 +```                                                           
       44  
       62 ----                                                           
       45 +Executa a suíte completa (unidade e integração).              
       46  
       64 -## O que o seu fork precisa conter ao final                   
       47 +## Contrato da CLI                                            
       48  
       49  ```
       67 -seu-fork/                                                     
       68 -├── CLAUDE.md                     # convenções do projeto para
          - o agente                                                     
       69 -├── README.md                     # como rodar e como testar o
          - SEU projeto                                                  
       70 -├── specs/                                                    
       71 -│   └── 001-motor-reembolso/                                  
       72 -│       ├── spec.md               # o QUÊ e o PORQUÊ          
       73 -│       ├── plan.md               # o COMO                    
       74 -│       ├── tasks.md              # T-001..T-0NN, com critério
          - de aceite                                                    
       75 -│       └── DECISIONS.md          # log de mudanças de spec   
       76 -├── src/                                                      
       77 -├── tests/                                                    
       78 -└── docs/                                                     
       79 -    ├── sessions/                 # exports das suas conversas
          - com o Claude                                                 
       80 -    └── RELATORIO.md              # o relatório final         
       50 +<jar> calcular --input <arquivo-entrada.json> --output <arquiv
          +o-saida.json>                                                 
       51  ```
       52  
       83 -Sobre o `README.md`: substitua este arquivo pelo README do **s
          -eu** projeto — como rodar, como testar, o que você construiu. 
          -Um README que não permite rodar o projeto custa pontos.       
       53 +- `--input`: caminho para o JSON de despesas (formato descrito
          + em                                                           
       54 +  `exemplos/despesas-exemplo.json`).                          
       55 +- `--output`: caminho onde o JSON de resultado é escrito. A es
          +crita é                                                       
       56 +  atômica: em caso de falha, um arquivo de saída preexistente 
          +permanece                                                     
       57 +  intacto.                                                    
       58  
       85 ----                                                           
       59 +### Códigos de saída                                          
       60  
       87 -## Antes de começar, confirme que o `/export` funciona        
       61 +| Código | Significado |                                      
       62 +|---|---|                                                     
       63 +| `0` | Sucesso. Resultado escrito em `--output`. |           
       64 +| `2` | Erro de uso ou de infraestrutura — argumento ausente/d
          +esconhecido, arquivo de entrada inexistente ou JSON sintaticam
          +ente inválido, falha ao escrever a saída. Nada é escrito em `-
          +-output`. |                                                   
       65 +| `3` | Envelope de entrada estruturalmente inválido (ex.: per
          +íodo com início posterior ao fim). Nada é escrito em `--output
          +`. |                                                          
       66  
       89 -Abra o Claude Code, troque duas mensagens, rode `/export` e co
          -nfirme que o arquivo foi gerado.                              
       67 +Em qualquer código diferente de `0`, a mensagem de erro vai pa
          +ra stderr e                                                   
       68 +stdout permanece vazio.                                       
       69  
       91 -Faça isso **agora**, não no Dia 2. Sem `docs/sessions/`, o cri
          -tério de relatório vale zero — e já aconteceu de gente que fez
          - tudo certo descobrir no último dia que não tinha registro nen
          -hum do trabalho.                                              
       70 +## Estrutura do projeto                                       
       71  
       93 -Exporte ao final de **cada** sessão, nomeando `docs/sessions/0
          -1-descricao-curta.md`, `02-...`, e assim por diante.          
       72 +```                                                           
       73 +src/main/java/...     # código de produção (pipeline de cálcul
          +o, CLI)                                                       
       74 +tests/java/...         # testes automatizados (JUnit)         
       75 +tests/resources/...     # fixtures usados pelos testes        
       76 +exemplos/                # entrada de referência              
       77 +specs/001-motor-reembolso/ # spec, plano e tasks — fonte da ve
          +rdade do comportamento                                        
       78 +docs/RELATORIO.md         # relatório final do desafio        
       79 +docs/sessions/             # exports das sessões de trabalho c
          +om o Claude Code                                              
       80 +```                                                           
       81  
       95 ----                                                           
       82 +## Onde estão as regras de negócio                            
       83  
       97 -## O resumo em um parágrafo                                   
       84 +O comportamento do sistema — regras de reembolso, ambiguidades
          + resolvidas e                                                 
       85 +critérios de aceite — está documentado em                     
       86 +[`specs/001-motor-reembolso/spec.md`](specs/001-motor-reembols
          +o/spec.md).                                                   
       87 +O relatório do desafio está em [`docs/RELATORIO.md`](docs/RELA
          +TORIO.md).                                                    
       88  
       99 -Você vai receber uma política de reembolso escrita por um RH, 
          -com a redação ruim que uma política de RH real tem. Ela é ambí
          -gua em vários pontos, e você não tem acesso a ninguém para tir
          -ar dúvida. O trabalho não é implementar — é **especificar**: e
          -ncontrar cada ambiguidade, decidir explicitamente, justificar 
          -e registrar. O produto funcionando vale **10 dos 100 pontos**.
          - Os outros 90 estão na spec, na rastreabilidade `spec → tasks 
          -→ commits → testes`, na resposta à mudança de requisito do Dia
          - 2 e no relatório.                                            
       89 +## Valores monetários                                         
       90  
      101 -Isso é deliberado. Um projeto que roda perfeitamente com spec 
          -fraca tira nota baixa; um projeto com bug conhecido, spec impe
          -cável e trilha limpa tira nota alta.                          
       91 +No JSON de saída, valores monetários são números decimais (não
          + strings),                                                    
       92 +sempre com exatamente duas casas decimais e nunca em notação c
          +ientífica —                                                   
       93 +por exemplo `585.43`, nunca `"585.43"` nem `585.4`.           

● Agora vou executar exatamente os comandos documentados para verificar.

  Ran 1 shell command (ctrl+o to expand)

● BUILD SUCCESS. Agora executar o comando de exemplo e verificar o exit code,
  stdout/stderr.

  Ran 1 shell command (ctrl+o to expand)

● Exit 0, stdout e stderr vazios. Vou validar o JSON gerado e o total.

  Ran 2 shell commands (ctrl+o to expand)

● JSON válido, total_reembolsavel = 585.43, 14 registros. Agora rodo a suíte de
  testes completa.

  Ran 1 shell command (ctrl+o to expand)

● Suíte completa: 363 testes, 0 falhas, 0 erros. Agora removo apenas
  resultado.json e os logs temporários, e verifico o diff final.

  Ran 1 shell command (ctrl+o to expand)

● git diff --check não reportou nenhum erro real (apenas o warning informativo
  de LF→CRLF do Git no Windows, que não é um problema de conteúdo). Apenas
  README.md foi modificado.

  Resumo da verificação

  Pré-condições confirmadas:
  - T-020 concluída e commitada (36d5d9b)
  - main local sem divergência problemática (1 commit à frente de origin/main,
  não enviado — esperado, não é sync issue)
  - Working tree limpo antes de começar

  Os três comandos do README funcionaram:
  1. mvn package → BUILD SUCCESS, gerou target/motor-reembolso.jar (existe, nome
  correto).
  2. java -jar target/motor-reembolso.jar calcular --input 
  exemplos/despesas-exemplo.json --output resultado.json → exit 0, stdout e
  stderr vazios; resultado.json gerado é JSON válido com 14 registros e
  total_reembolsavel = 585.43.
  3. mvn test → 363 testes, 0 falhas, 0 erros, BUILD SUCCESS.

  Conteúdo do README criado: objetivo do projeto, requisitos (JDK 21 + Maven),
  clonar/entrar no diretório, compilar, executar, rodar testes, contrato da CLI,
  tabela de exit codes (0/2/3), estrutura do projeto, referências a spec.md e
  RELATORIO.md, e nota sobre valores monetários em JSON decimal com duas casas.
  Todos os comandos em blocos separados, sem &&, caminhos relativos, compatíveis
  com PowerShell/Bash/CMD.

  Limpeza: resultado.json e os logs temporários (stdout.log, stderr.log) foram
  removidos após a verificação.

  Estado final:
  git status --short
   M README.md
  Apenas README.md foi alterado. git diff --check sem erros reais. Nenhum commit
  foi criado.

  Parado para revisão — aguardando aprovação antes de marcar T-021 como
  concluída em tasks.md ou commitar.

✻ Baked for 1m 17s

> Faça uma correção pontual somente no README da T-021.                         
                                                                                
  Altere apenas:                                                                
                                                                                
  `README.md`                                                                   
                                                                                
  Substitua:                                                                    
                                                                                
  ```text                                                                       
  git clone <url-do-repositorio>                                                
  ```                                                                           
                                                                                
  por:                                                                          
                                                                                
  ```text                                                                       
  git clone https://github.com/BrunoButtros/sdd-desafio.git                     
  ```                                                                           
                                                                                
  Não altere nenhuma outra parte do README.                                     
  Não altere `tasks.md`.                                                        
  Não faça commit.                                                              
                                                                                
  Depois execute:                                                               
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git diff -- README.md                                                         
  git status --short                                                            
  ```                                                                           
                                                                                
  Confirme que:                                                                 
                                                                                
  * somente a linha do clone foi corrigida;                                     
  * apenas `README.md` aparece modificado;                                      
  * nenhum outro arquivo foi alterado.                                          
                                                                                
  Pare para revisão.                                                            

● Update(README.md)
  ⎿  Added 1 line, removed 1 line
      12  ## Obter o projeto
      13  
      14  ```
      15 -git clone <url-do-repositorio>                                 
      15 +git clone https://github.com/BrunoButtros/sdd-desafio.git      
      16  ```
      17  
      18  ```

  Searched for 2 patterns, ran 1 shell command (ctrl+o to expand)

● Correção aplicada e verificada.

  O que mudou nesta rodada: apenas a linha 15, de git clone <url-do-repositorio>
  para git clone https://github.com/BrunoButtros/sdd-desafio.git. Confirmado —
  só há uma ocorrência de git clone no arquivo, e é a linha corrigida.

  git diff --check: sem erros reais (apenas o mesmo warning informativo de
  LF→CRLF do Git no Windows, não é erro de conteúdo).

  git diff -- README.md: mostra o diff acumulado desde o último commit
  (36d5d9b), pois nada foi commitado ainda nesta task — inclui tanto a reescrita
  completa do README feita na rodada anterior quanto esta correção pontual da
  linha do clone. Não há um commit intermediário para isolar apenas esta mudança
  no diff.

  git status --short: M README.md — apenas este arquivo modificado. tasks.md
  intocado.

  Nenhum commit foi feito.

  Parado para revisão.

✻ Cogitated for 20s

> Finalize exclusivamente a T-021.                                              
                                                                                
  O README foi executado manualmente e revisado. Os comandos de build, execução 
  e testes funcionaram.                                                         
                                                                                
  Não altere novamente `README.md`.                                             
                                                                                
  Altere somente:                                                               
                                                                                
  `specs/001-motor-reembolso/tasks.md`                                          
                                                                                
  Na T-021:                                                                     
                                                                                
  * marque o título como `[x]`;                                                 
  * altere o Status para `[x] concluída`.                                       
                                                                                
  Não altere nenhuma outra task.                                                
  Não crie novas tasks ainda.                                                   
  Não altere código, testes, spec, plan ou DECISIONS.                           
  Não execute `/export`, git add, commit ou push.                               
                                                                                
  Execute:                                                                      
                                                                                
  ```powershell                                                                 
  mvn package                                                                   
  java -jar target/motor-reembolso.jar calcular --input                         
  exemplos/despesas-exemplo.json --output resultado.json                        
  mvn test                                                                      
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * `target/motor-reembolso.jar` foi gerado;                                    
  * execução retornou exit `0`;                                                 
  * stdout e stderr ficaram vazios;                                             
  * `resultado.json` tem 14 resultados;                                         
  * `total_reembolsavel` é `585.43`;                                            
  * suíte completa tem 363 testes verdes.                                       
                                                                                
  Depois remova somente o arquivo gerado:                                       
                                                                                
  ```powershell                                                                 
  Remove-Item resultado.json                                                    
  ```                                                                           
                                                                                
  Execute também:                                                               
                                                                                
  ```powershell                                                                 
  git diff --check                                                              
  git status --short                                                            
  git diff --stat                                                               
  git diff -- specs/001-motor-reembolso/tasks.md                                
  ```                                                                           
                                                                                
  Confirme:                                                                     
                                                                                
  * somente `README.md` e `tasks.md` aparecem modificados;                      
  * somente as duas linhas da T-021 foram alteradas em `tasks.md`;              
  * T-001 a T-021 estão marcadas como concluídas;                               
  * `resultado.json` foi removido;                                              
  * nenhum commit ou push foi executado nesta rodada.                           
                                                                                
  Pare para revisão.                                                            

● Vou editar somente as duas linhas da T-021 em tasks.md.

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      467  
      468  ## Fase 7 — Documentação final
      469  
      470 -- [ ] **T-021** — README com instruções reais de build, execuç
          -ão e testes                                                   
      470 +- [x] **T-021** — README com instruções reais de build, execuç
          +ão e testes                                                   
      471    - **O que faz:** documenta, em comandos reais e testados man
           ualmente, como compilar, executar e testar o projeto — de form
           a multiplataforma, sem depender de um caminho específico de um
            sistema operacional.
      472    - **Requisitos atendidos:** nenhuma RN/CA/DT diretamente — a
           tende ao critério "Produto funciona" da rubrica e evita a pena
           lidade transversal "README não permite rodar o projeto".
      473    - **Dependências:** T-020 (documenta um sistema já testado d
           e ponta a ponta).

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      489      roda a suíte inteira sem falhas. Os três comandos são apre
           sentados em linhas separadas (sem `&&`) e usam caminho relativ
           o (`resultado.json`, não `/tmp/out.json`), funcionando tanto e
           m PowerShell quanto em outros terminais.
      490    - **Comandos de verificação:** os três comandos do critério 
           de aceite, executados manualmente em sequência, cada um em sua
            própria linha.
      491    - **Commit:** `docs(readme): [T-021] documenta build execuca
           o e testes` (1 commit).
      492 -  - **Status:** [ ] pendente                                  
      492 +  - **Status:** [x] concluída                                 
      493  
      494  ---
      495  

● Agora executo os comandos de verificação.

  Ran 4 shell commands (ctrl+o to expand)

● Todos os critérios confirmados. Removo o arquivo gerado e verifico o estado do
  git.
  Searched for 2 patterns, ran 1 shell command (ctrl+o to expand)

● Esse é apenas o texto explicativo da regra geral (linha 25), não uma task
  pendente. Nenhuma task de T-001 a T-021 restou com checkbox [ ].

  Confirmações finais

  Verificação técnica:
  - target/motor-reembolso.jar gerado (mvn package → BUILD SUCCESS)
  - Execução: exit 0, stdout e stderr vazios
  - resultado.json: 14 resultados, total_reembolsavel = 585.43
  - mvn test: 363 testes, 0 falhas, 0 erros
  - resultado.json (e logs temporários) removidos

  Verificação git:
  - git diff --check: sem erros reais (só warning informativo de LF→CRLF)
  - git status --short: apenas README.md e specs/001-motor-reembolso/tasks.md
  modificados
  - git diff -- specs/001-motor-reembolso/tasks.md: confirma que somente as duas
  linhas da T-021 foram alteradas (checkbox do título [ ]→[x] e Status [ ] 
  pendente→[x] concluída); nenhuma outra task tocada
  - Busca por [ ] pendente em tasks.md: nenhuma ocorrência real de checkbox
  aberto — só o texto de exemplo da regra geral na linha 25. T-001 a T-021 estão
  todas [x] concluídas.
  - Nenhum git add, commit ou push executado.

  Parado para revisão.

✻ Churned for 46s