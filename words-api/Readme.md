## Projeto API de Palavras
___
#### ⚠️ Cada dupla deve criar uma branch e fazer seus commits dentro dela
#### ⚠️ Na branch da dupla deve ter um Readme com pelo menos os nomes dos integrantes da dupla e os endpoints criados
___

#### Sobre o Projeto

✔️ CRUD de Palavras 
    Apenas um atributo (termo, por exemplo)

✔️ CRUD de Etiquetas 
    Apenas um atributo (nome, por exemplo)

✔️ Relacionamento muitos-pra-muitos entre Etiqueta e Palavra

✔️ Busca de Palavras por Etiqueta

✔️ Busca de Etiqueta por Palavras

✔️ TDD

✔️ Segurança com JWT 
    Acesso admin para o CRUD completo 
    Acesso usuario apenas para leituras (o que inclui as listas)

✔️ O que mais a dupla desejar fazer

____

**⚠️ De tempos em tempos farei alguma alteração no Readme do projeto... ⚠️**

**Não deixem de atualizar sua branch**
____
#### Links úteis
- [Guia Mega Prático - Git](https://rogerdudler.github.io/git-guide/index.pt_BR.html)
- [Curso de Git Para Iniciantes - Youtube](https://www.youtube.com/watch?v=WVLhm1AMeYE&list=PLInBAd9OZCzzHBJjLFZzRl6DgUmOeG3H0)

____

# Branch orlando-luiscarlos
Nesta branch estão as atualizações realizadas pela dupla [José Orlando Ferreira do Nascimento] e [Luis Carlos Zancanela] para cumprimento das etapas do projeto acima especificado.

## Configuration
O projeto foi feito utilizando:
- [IDE Eclipse] versão 2022-03 (4.23.0).
- Iniciado com [spring initializr], com as configurações e dependências:
    - Project: [Maven] Project
    - Language: Java
    - [Spring Boot] 2.7.1
    - Packaging: Jar
    - [Java 11]
    - Dependencies:
        - Spring Boot DevTools
        - Spring Web
        - Spring Data JPA
        - Validation
        - MySQL Driver
        - Spring Security
        - Flyway (versão 7.8.0)
        - Lombok
- Dependências adicionadas manualmente ao [pom.xml]:
    - `springfox-swagger-ui` para gerar Swagger;
    - `springfox-swagger2` para gerar Swagger;
    - `modelmapper` para mapear DTO em entidade e vice-versa;
    - `java-jwt` para trabalhar com token JWT;
    - `jacoco-maven-plugin` para gerar relatório de coverage quando o teste é executado diretamente pelo terminal. _É necessário também inserir plugin específico, verifique o [pom.xml] para checar o plugin `jacoco-maven-plugin` adicionado_

### Word's API - Starter #4
Nste projeto foram realizados as seguintes tasks:
- Para a construção do banco de dados foi utilizado o flyway;
- CRUD de palavras e etiquetas;
- Relação ManyToMany entre palavras e etiquetas;
- Foi configurado o Spring Security para autenticação de usuários e autorização por token, personalizando os acessos aos endpoints;
- Foi configurado Swagger juntamente com docs para controllers e segurança para permitir autorização por token para que se possa fazer requisições por ele;
- Para a palavra, foi criado `registerWordDTO` para receber dados no POST e PUT e o `queryWordDTO` para gerar as respostas nos endpoints
- No `WordService`, para create e update:
    - Validação para evitar que seja cadastrada nova palava com nome já existente, verificação feita pelo método `findByName`;
    - Aceita receber array de etiquetas no POST e PUT contendo id ou nome, o método `checkExistingTag` checa se a tag informada, seja por id ou nome, já existe e o método `linkExistingTags` se encarrega de ligar novamente a(s) etiqueta(s) com a palavra para que a persistência funcione novamente e não ocorre o erro "detached entity passed to persist", link que auxiliou na solução: [solved spring data JPA ManyToMany relationship report detached entity passed to persist error]
- _TODO:_
    - _criar `registerTagDTO` e `queryTagDTO` semelhante ao que foi feito com Word;_
    - _atualizar `TagService`, `TagServiceTest`, `TagController`, `TagControllerDocs`, `TagControllerTest` semelhante o que foi feito com Word;_
    - _criar endpoints para consulta de etiquetas por palavra e consulta de palavras por etiqueta;_
    - _criar endpoints para adicionar e remover etiqueta de palavra e remover palavra de etiqueta;_
    - _atualizar os testes unitários para máximo coverage._

## Visuals
Spring Banner personalizado:  
![Spring Banner Personalizado](docs/spring_banner.png?raw=true "Spring Banner Personalizado") 

## Installation
Para abrir o projeto basta clonar o repositório e fazer checkout para a branch orlando-luiscarlos ou realizar o download e após:
- Importar o projeto, preferencialmente na IDE Eclipse;
- Fazer atualizações das dependências do Maven (Alt+F5 no Eclipse);
- Possuir no mínimo JDK 11 LTS instalado, sugestão de JDK: [OpenJDK Zulu];
- Possuir acesso à internet para atualização de dependências;
- Necessário possuir o banco de dados MySQL Server, caso não possua, pode visitar [MySQL Community Download] para download e instalação;
- No arquivo src/main/resources/application.properties verificar e se necessário alterar os parâmentros conforme segue:
    - URL de conexão com o banco: `spring.datasource.url=jdbc:mysql://localhost:3306/words?createDatabaseIfNotExist=True`
    - Usuário root do banco: `spring.datasource.username=root` - O usuário root padrão com permissão ao MySQL Server local, alterar somente caso seja esteja desativado ou seja diferente;
    - Usuário root do banco: `spring.datasource.password=root` - Senha do banco de dados, alterar para a senha do root utilizado localmente.

## Tests
Os testes foram feitos utilizando o [JUnit 5], Mockito e MockMVC.  
- Utilizando a [IDE Eclipse], basta executar (Run As) a pasta de teste ou escolher o arquivo. 
- Utilizando o terminal (PowerShell ou similiar), basta executar na pasta do projeto o comando abaixo:
    ```shell
    ./mvnw clean test
    ```
Após o teste finalizado, é possível verificar relatório de coverage em: target/site/jacoco/index.html

## Usage
Para iniciar o projeto, basta utilizar uma das opções abaixo: 
- Utilizando a [IDE Eclipse], basta executar (Run) a classe `WordsApiApplication` no pacote `br.com.gft`.  
- Utilizando o terminal (PowerShell ou similiar), basta executar na pasta do projeto o comando abaixo:
    ```shell
    ./mvnw clean package spring-boot:run
    ```
Ao executar a aplicação será criado o esquema do banco de dados através do flyway e será inserido **1 usuário padrão**, sendo:
- Admin com perfil ADMIN
    - username: admin@gft.com
    - senha: Gft@1234

## Endpoints:
- Autenticação: 
    | Método | URL                               | Perfil(s) Autorizado(s) | 
    | ------ | ---                               | ----------------------- |
    | GET    | http://localhost:8080/api/v1/auth | Público                 |

- Palavras:
    | Método | URL                                     | Perfil(s) Autorizado(s) | 
    | ------ | ---                                     | ----------------------- |
    | POST   | http://localhost:8080/api/v1/words      | ADMIN                   |
    | GET    | http://localhost:8080/api/v1/words      | USUARIO, ADMIN          |
    | GET    | http://localhost:8080/api/v1/words/{id} | USUARIO, ADMIN          |
    | PUT    | http://localhost:8080/api/v1/words      | ADMIN                   |
    | DELETE | http://localhost:8080/api/v1/words      | ADMIN                   |

- Etiquetas:
    | Método | URL                                    | Perfil(s) Autorizado(s) | 
    | ------ | ---                                    | ----------------------- |
    | POST   | http://localhost:8080/api/v1/tags      | ADMIN                   |
    | GET    | http://localhost:8080/api/v1/tags      | USUARIO, ADMIN          |
    | GET    | http://localhost:8080/api/v1/tags/{id} | USUARIO, ADMIN          |
    | PUT    | http://localhost:8080/api/v1/tags/{id} | ADMIN                   |
    | DELETE | http://localhost:8080/api/v1/tags/{id} | ADMIN                   |

- Usuários:
    | Método | URL                                     | Perfil(s) Autorizado(s) | 
    | ------ | ---                                     | ----------------------- |
    | POST   | http://localhost:8080/api/v1/users      | ADMIN                   |
    | GET    | http://localhost:8080/api/v1/users      | ADMIN                   |
    | GET    | http://localhost:8080/api/v1/users/{id} | ADMIN                   |
    | PUT    | http://localhost:8080/api/v1/users/{id} | ADMIN                   |
    | DELETE | http://localhost:8080/api/v1/users/{id} | ADMIN                   |


A documentação completa dos Endpoints estará disponível através do Swagger acessível pelo link: http://localhost:8080/swagger-ui.html  
  
Também existe arquivo de Coleção do Postman, basta importar o arquivo [Word's API.postman_collection.json] que está na pasta docs.  
  
**IMPORTANTE:** Como é uma aplicação que possui checagem de permissão de acesso, é necessário que se utilize token para as requisições através do Endpoint descrito na tabela de Autenticação.  

### Autenticando e utilizando o token no Swagger
Para fazer a autenticação e utilizar o token no Swagger siga os passos:  
1- Na página do Swagger (http://localhost:8080/swagger-ui.html ) procure por "authentication-controller", depois "/api/v1/auth
Token creation operation" e clique em "Try it out"  
![Swagger Authentication Step 01](docs/swagger_auth01.png?raw=true "Swagger Authentication Step 01")  
2- Irá ter um campo de texto (textarea) com um JSON de modelo, altere o email e password para email e senha de usuário já cadastrado (exemplo: admin@gft.com), depois é só clicar em "Execute"  
![Swagger Authentication Step 02](docs/swagger_auth02.png?raw=true "Swagger Authentication Step 02")  
3- Após a execução irá aparecer a resposta abaixo na seção "Server response", o token estará no "Response body", copiar somente o token sem as aspas (Caso usuário e senha seja inválido, será retornado erro 401.)  
![Swagger Authentication Step 03](docs/swagger_auth03.png?raw=true "Swagger Authentication Step 03")  
4- Clicar no botão "Authorize" que fica logo abaixo do cabeçalho da página  
![Swagger Authentication Step 04](docs/swagger_auth04.png?raw=true "Swagger Authentication Step 04")  
5- Na janela que abrir, informar o token gerado e copiado no passo 3.  
**IMPORTANTE:** É necessário a adição do prefixo "Bearer " (sem aspas), com espaço entre o Bearer e o token para que ele funcione corretamente. É só clicar em "authorize" e seguir para o Swagger normalmente que já estará autorizado conforme o perfil do usuário que usou no passo 2 para gerar o token. 
![Swagger Authentication Step 05](docs/swagger_auth05.png?raw=true "Swagger Authentication Step 05")  

### Autenticando e utilizando o token no Postman
Para fazer a autenticação e utilizar o token no Postman siga os passos:  
1- No aplicativo Postman é necessário fazer a requisição com método POST na URL http://localhost:8080/api/v1/auth com o email (usuário) e senha enviados no body da requisição. A configuração da requisição está na Coleção "Word's API / Authentication / Authenticate"  
![Postman Authentication Step 01](docs/postman_auth01.png?raw=true "Postman Authentication Step 01")  
2- A requisição bem sucedida irá retornar resposta 200 OK e o token, copie o token  
![Postman Authentication Step 02](docs/postman_auth02.png?raw=true "Postman Authentication Step 02")  
3- Após o token gerado e copiado, é só ir em qualquer requisição, ir na aba Authorization, escolher a opção "Bearer Token" e colar o token. Diferente do Swagger não é necessário adicionar prefixo já que escolher a opção de Bearer Token.  
![Postman Authentication Step 03](docs/postman_auth03.png?raw=true "Postman Authentication Step 03")  

## Support and Contributing
Para dúvidas, sugestões, feedbacks ou correção de bugs: abrir Issue ou Merge Request.

## Authors and acknowledgment
Desafio proposto por Michel, Programa Start da GFT.  
Feito pela [José Orlando Ferreira do Nascimento] e [Luis Carlos Zancanela]  

## Project status
_Doing_


[Java 11]: https://docs.oracle.com/en/java/javase/11/docs/api/index.html
[JUnit 5]: https://junit.org/junit5/docs/current/user-guide/index.html
[IDE Eclipse]: https://www.eclipse.org/ide/
[spring initializr]: https://start.spring.io/
[Spring Boot]: https://spring.io/projects/spring-boot
[Maven]: https://maven.apache.org/
[MySQL]: https://www.mysql.com/
[MySQL Community Download]: https://dev.mysql.com/downloads/
[OpenJDK Zulu]: https://www.azul.com/downloads/

[José Orlando Ferreira do Nascimento]: https://git.gft.com/jofh
[Luis Carlos Zancanela]: https://git.gft.com/lsza

[solved spring data JPA ManyToMany relationship report detached entity passed to persist error]: https://debugah.com/solved-spring-data-jpa-many-to-many-relationship-report-detached-entity-passed-to-persist-error-10804/

[pom.xml]: https://git.gft.com/mlep/api-palavras/-/blob/orlando-luiscarlos/pom.xml
[Word's API.postman_collection.json]: https://git.gft.com/mlep/api-palavras/-/blob/orlando-luiscarlos/docs/Word's%20API.postman_collection.json