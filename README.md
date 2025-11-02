# 👤 Sistema de Gerenciamento de Usuários (USU-Backend)

Aplicação backend desenvolvida em **Java com Spring Boot** para gerenciar o cadastro, autenticação e controle de usuários.  
O sistema fornece endpoints REST seguros e documentados para realizar operações de **CRUD de usuários**, **login** e **validação de permissões**.

---

## ⚙️ Tecnologias

O projeto utiliza o seguinte stack:

- **Java 21**
- **Spring Boot 3**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring Security
- **PostgreSQL 16**
- **Docker e Docker Compose**
- **Maven**
- **Swagger (Springdoc OpenAPI 3)**
- **Postman (para testes manuais)**

---

## 🏗️ Arquitetura

O sistema segue o padrão **MVC (Model-View-Controller)**, organizado em camadas para manter a coesão e separação de responsabilidades:

- **Controller:** Responsável por expor os endpoints REST e receber as requisições.
- **Service:** Contém as regras de negócio e orquestra a comunicação entre camadas.
- **Repository:** Realiza o acesso e manipulação dos dados no banco via **Spring Data JPA**.
- **DTOs:** Utilizados para transferir dados entre as camadas, garantindo segurança e clareza na comunicação.
- **Entities:** Mapeiam as tabelas do banco de dados.
- **Exception Handling:** Implementação de tratamento de erros global e mensagens padronizadas.
- **Swagger:** Documentação automática dos endpoints.

---

## 🌱 Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
DB_NAME=usu
DB_USER=postgres
DB_PASSWORD=postgres
DB_PORT=5432
SPRING_PROFILES_ACTIVE=dev
```

Essas variáveis são utilizadas no `docker-compose.yml` e no `application.yml`.

---

## 🚀 Instruções para execução local

O projeto pode ser executado de duas formas distintas: **localmente** (sem Docker) ou **utilizando containers via Docker Compose**.  
Abaixo estão os procedimentos para cada abordagem.

---

### ⚙️ Execução Local (sem Docker)

1. **Instalação da versão Java**  
   Caso o **Java 21** não esteja instalado na máquina, é necessário realizar sua instalação.  
   Você pode verificar com:
   ```bash
   java -version
   ```

2. **Instalação do PostgreSQL**  
   Caso o PostgreSQL não esteja instalado, é necessário realizar sua instalação.  
   Como alternativa, é possível executar o banco de dados em um container Docker:
   ```bash
   docker run --name my-postgres -e POSTGRES_PASSWORD=fiap -p 5432:5432 -d postgres:16-alpine
   ```

3. **Criação das tabelas e dados iniciais**  
   Após a instalação do banco, execute o script `init.sql` localizado na pasta `/postgres` do projeto.  
   Esse script é responsável por criar as tabelas, índices e o **usuário padrão** da aplicação.

4. **Configuração do arquivo `application.properties`**  
   Atualize as variáveis relacionadas à conexão com o banco e ao segredo utilizado para geração do token JWT.  
   Exemplo:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/usu
   spring.datasource.username=postgres
   spring.datasource.password=fiap
   jwt.secret=chave_secreta_segura
   ```

5. **Execução da aplicação**  
   Execute o projeto diretamente pela sua IDE (ex: IntelliJ, Eclipse) ou via terminal:
   ```bash
   mvn spring-boot:run
   ```

Após a execução, a aplicação estará disponível em:  
👉 [http://localhost:8080](http://localhost:8080)

---

### 🐳 Execução via Docker Compose

1. **Instalação do Docker e Docker Compose**  
   Caso o Docker não esteja instalado, é necessário realizar sua instalação.  
   Verifique com:
   ```bash
   docker --version
   docker compose version
   ```

2. **Abrir o terminal na pasta do projeto**  
   Navegue até o diretório raiz onde se encontra o arquivo `docker-compose.yml`.

3. **Subir os containers da aplicação**  
   Execute o seguinte comando para construir a imagem e iniciar os containers do backend e do banco de dados:
   ```bash
   docker compose up --build
   ```

Após a inicialização:
- **Aplicação:** [http://localhost:8080](http://localhost:8080)  
- **Banco de dados PostgreSQL:** porta `5432`

> 💡 Por se tratar de um projeto acadêmico, tanto a execução via Docker quanto a execução local utilizam as mesmas portas padrão:
> - Aplicação: **8080**
> - Banco de dados: **5432**

---

## 📄 Acesso ao Swagger

A documentação dos endpoints pode ser acessada após iniciar o projeto:

👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

O Swagger lista todos os endpoints disponíveis, incluindo:
- Criação de usuários  
- Login e autenticação  
- Atualização e exclusão de usuários  
- Consulta paginada de usuários  

---

## 🧪 Testes com Postman

Os endpoints podem ser testados através da **collection Postman** disponível em:
```
source/postman/usu-backend-collection.json
```

### ⚙️ Instruções

1. Importe a collection no Postman.  
2. Configure o ambiente `Local` com a variável base:
   ```
   base_url = http://localhost:8080
   ```
3. Execute os testes na ordem recomendada.

### 🧾 Observações

- **Usuário padrão para testes:**
  ```
  login: admin
  senha: 123456
  ```
- O header `Accept-Language` pode ser alterado para ajustar o idioma das mensagens:
  - `en-US` → Inglês (padrão)
  - `pt-BR` → Português

---

## 🗄️ Estrutura do Banco de Dados

Tabela principal: **users**

| Campo       | Tipo        | Descrição                          |
|--------------|-------------|------------------------------------|
| id           | UUID        | Identificador único do usuário     |
| name         | VARCHAR     | Nome completo                      |
| email        | VARCHAR     | E-mail do usuário (único)          |
| login        | VARCHAR     | Nome de login                      |
| password     | VARCHAR     | Senha criptografada                |
| status       | BOOLEAN     | Indica se o usuário está ativo     |
| created_at   | TIMESTAMP   | Data de criação                    |
| updated_at   | TIMESTAMP   | Data da última atualização         |

---
