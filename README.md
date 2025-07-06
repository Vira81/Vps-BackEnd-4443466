>>>Requisitos

> Spring Tools Suite [https://spring.io/tools]
> 
> PostGreSQL [https://www.postgresql.org/download/]
> 
> Lombok [https://projectlombok.org/setup/eclipse]
> 
> Postman [https://www.postman.com/]

>>> Como rodar o projeto

>Clone o repositório.
>
>No Spring Tools, vá em File > Import > Maven > Existing Maven Projects
>
>Selecione a pasta do projeto.
>
>
>Configure o PostGreSQL
>Edite o arquivo com os dados do PostGreSQL: src/main/resources/application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/{nome_do_banco}

spring.datasource.username={usuario}

spring.datasource.password={senha}


>Rode o projeto como Spring Boot App

>>> Endpoints

>O sistema usa JWT (JSON Web Token) para autenticação, sendo necessário realizar login
para receber o token.
>Use o token no Postman na aba Authentication > Bearer Token

>>> Endpoints públicos
>
>POST /usuarios/cadastro    - Cadastro de novo Usuário.
>
>POST /auth/login           - Login e criação do Token.

>>> Usuarios de Testes
>
>Para facilitar os testes, faça login com esse usuário com permissão de admin.
>
>{"username": "admin@vida.com", "password": "admin"}
