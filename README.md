# 👤 Sistema de Gerenciamento de Usuários (USU-Backend)

Aplicação backend desenvolvida em **Java com Spring Boot** para gerenciar o cadastro, autenticação e controle de usuários.  
O sistema fornece endpoints REST seguros e documentados para realizar operações de **CRUD de usuários**, **login** e **validação de permissões**.

---

## 📘 Sumário
1. [Tecnologias](#-tecnologias)
2. [Arquitetura](#-arquitetura)
3. [Pré-requisitos](#-pré-requisitos)
4. [Configuração de variáveis de ambiente](#-variáveis-de-ambiente)
5. [Como executar o projeto](#-como-executar-o-projeto)
6. [Acesso ao Swagger](#-acesso-ao-swagger)
7. [Testes com Postman](#-testes-com-postman)
8. [Estrutura do Banco de Dados](#-estrutura-do-banco-de-dados)
9. [Autor](#-autor)
10. [Licença](#-licença)

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

## 🧰 Pré-requisitos

Antes de rodar o projeto, é necessário ter instalado:

- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/)
- [PostgreSQL 16+](https://www.postgresql.org/download/)
- (Opcional) [Postman](https://www.postman.com/)

---

## 🌱 Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
DB_NAME=usu
DB_USER=postgres
DB_PASSWORD=postgres
DB_PORT=5432
SPRING_PROFILES_ACTIVE=dev
