# Desafio AZ Tecnologia em Gestão: To-do list

Este projeto é destinado a criação de um projeto full-stack como solução a um teste técnico proposto pela AZ Tecnologia em Gestão. A seguir será evidenciado quais tecnologias foram utilizadas para a construção do projeto e também quais os passos necessários para rodar o front-end, back-end e integrá-los para testar localmente o funcionamento.

# Tecnologias utilizadas

1. Spring boot 3
2. Java 17
3. Swagger
4. PostgreSQL
5. Docker Compose
6. Vue 3
7. Vuetify
8. Vue3-toastify
9. Insomnia


# Pré-requisitos para rodar o projeto
1. Node.js v17 >=
2. pnpm v9.14 >= (ou npm, nvm, yarn)
2. Docker v20.10.x >=
3. Docker Compose v2.6.x >=
4. HTTP Client (Insomnia, Postman, APIDog)
5. Visual Studio Code
6. IntelliJ IDEA CE

# Como executar o projeto
## Como executar o back-end localmente

Após fazer o download do repositório, abra o IntelliJ, clique em Open Project e navegue até a pasta de download do repositório e selecione a pasta api-todolist. No terminal, rode o comando: **docker-compose up -d** e então o banco de dados será criado. Em seguida navegue até a classe TodolistAziApplication.java em  **api-todolist/src/main/java/br/com/azi/todolist_azi/TodolistAziApplication.java** e execute a classe, a API vai subir na porta 8080. 

Para realizar requisições a API, com o back-end funcionando, vá até o navegador e digite: http://localhost:8080/swagger-ui/index.html#/ e então será mostrado o swagger com cada requisição, seu request body necessário e response em variados cenários.

Swagger da API Spring

<img width="1190" alt="Captura de Tela 2025-01-21 às 00 33 51" src="https://github.com/user-attachments/assets/9fb32af2-67d3-47c1-908f-b2f12d67b3a9" />

## Como rodar o front-end localmente 

Abra o VSCode, navegue até a pasta do projeto e selecione a pasta front-todolist para ser aberta. Então no terminal, digite: pnpm install para adicionar as dependências. Após isso, digitar pnpm run dev e o projeto vai subir na porta 3000 e poderá ser visualizado no navegador, já em integrado com o back-end (caso ele esteja de pé). 

Listagem de tarefas <img width="1190" alt="listagem" src="https://github.com/user-attachments/assets/d9e553aa-9b7a-4119-8f01-167d587c89d7" />

Tarefa atualizada <img width="1190" alt="atualizacao" src="https://github.com/user-attachments/assets/a45d0268-a39d-458d-bbf0-c9603a4339b8" />

Editando uma tarefa <img width="1188" alt="edicao" src="https://github.com/user-attachments/assets/3faf5390-c67e-4ecd-b173-3a9c13b171be" />

Tarefa deletada <img width="1190" alt="Captura de Tela 2025-01-21 às 00 32 58" src="https://github.com/user-attachments/assets/c54a6dbd-bff3-49e5-a5b4-0294eaf5836a" />

Buscando uma tarefa por id <img width="1193" alt="buscaid" src="https://github.com/user-attachments/assets/c2eb66a1-bd5f-49fb-9212-a46264a806ed" />
