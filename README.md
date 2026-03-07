
# 🏛️ ExLibris

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-brightgreen?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-Enabled-brightgreen?style=for-the-badge&logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Enabled-red?style=for-the-badge&logo=flyway&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth0-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

<br>

**A modern RESTful API for library management and social discovery.**
<br>
*Inspired by Letterboxd, ExLibris combines robust inventory management with social features like reviews, ratings, and reading tracking.*

</div>

---

## ✨ Features

- **📚 Book Management** – Full CRUD operations for books with ISBN validation and average rating.
- **✍️ Author Management** – Create and manage authors and link them to their bibliographies.
- **🔐 Authentication & Authorization** – Stateless JWT authentication with role-based access control.
- **⭐ Social & Discovery** – Rate books, write reviews, and track reading status.
- **👤 User Management** – Registration with role differentiation and encrypted passwords.
- **🔍 API Documentation** – Interactive Swagger UI with OpenAPI 3.0 integration.
- **🗄️ Database Migrations** – Version-controlled schema management using Flyway.
- **🌱 Database Seeder** – Automatic admin user creation for first-time setup.

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.0.1 |
| **Security** | Spring Security + JWT (Auth0 `java-jwt`) |
| **Database** | PostgreSQL |
| **ORM** | Spring Data JPA / Hibernate |
| **Migrations** | Flyway |
| **Documentation** | SpringDoc OpenAPI 3.0 |
| **Validation** | Jakarta Bean Validation |
| **Utilities** | Lombok |
| **Testes** | JUnit 5, Mockito, JaCoCo |

---

## 🔐 Segurança

A API utiliza autenticação **Stateless via JWT** (JSON Web Token), implementada com a biblioteca **Auth0 `java-jwt`**. Não há sessão no servidor — cada requisição autenticada carrega seu próprio token no header `Authorization`.

### Como funciona

1. O cliente faz `POST /api/auth/login` com e-mail e senha.
2. A API valida as credenciais via `AuthenticationManager` do Spring Security.
3. Se válido, um token JWT é gerado pelo `TokenService`, assinado com HMAC256 e com expiração de **2 horas**.
4. O cliente envia esse token nas requisições seguintes como `Authorization: Bearer <token>`.
5. O `SecurityFilter` intercepta cada request, extrai e valida o token, e injeta o usuário autenticado no `SecurityContext`.

### Roles (Níveis de Acesso)

O sistema possui **três** níveis de acesso definidos no enum `UserRole`:

| Role | Descrição |
|------|-----------|
| `ADMIN` | Acesso total. Pode registrar usuários com qualquer Role. |
| `LIBRARIAN` | Acesso intermediário. |
| `READER` | Papel padrão atribuído no registro público (`/api/auth/register`). |

As roles são aplicadas via `@PreAuthorize` do Spring Method Security. Endpoints públicos (login, register, Swagger) são configurados no `SecurityConfiguration`.

---

## 🔑 Endpoints de Autenticação

### `POST /api/auth/login`

Autentica um usuário e retorna um token JWT.

**Request Body:**
```json
{
  "email": "usuario@email.com",
  "password": "senha123"
}
```

**Response (`200 OK`):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "type": "Bearer",
  "role": "READER"
}
```

---

### `POST /api/auth/register`

Registra um novo usuário. A role é **sempre `READER`**, independentemente do que for enviado no body.

**Request Body:**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123"
}
```

**Response (`201 Created`):** retorna os dados do usuário criado.

---

### `POST /api/auth/admin/register-user`

🔒 **Requer `ROLE_ADMIN`.**

Permite que um Admin registre novos usuários **escolhendo a Role** (ADMIN, LIBRARIAN ou READER). O campo `role` é **obrigatório** nesta rota.

**Request Body:**
```json
{
  "name": "Maria Admin",
  "email": "maria@email.com",
  "password": "senha123",
  "role": "LIBRARIAN"
}
```

**Response (`201 Created`):** retorna os dados do usuário criado.

---

## 🔗 Demais Endpoints

### 📕 Books

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/books` | Retrieve all books |
| `GET` | `/api/books/{id}` | Retrieve a book by ID |
| `POST` | `/api/books` | Register a new book |
| `PUT` | `/api/books/{id}` | Update book details |
| `DELETE` | `/api/books/{id}` | Remove a book |
| `PATCH` | `/api/books/{bookId}/author/{authorId}` | Assign an author to a book |

### ✍️ Authors

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/author` | Register a new author |
| `GET` | `/api/author/{id}` | Retrieve an author by ID |
| `PUT` | `/api/author/{id}` | Update author details |
| `DELETE` | `/api/author/{id}` | Remove an author |
| `GET` | `/api/author` | Retrieve all authors |

### ⭐ Reviews

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/books/{id}/reviews` | Add a review and rating |
| `GET` | `/api/books/{bookId}/reviews` | Get reviews for a book |
| `DELETE` | `/api/reviews/{reviewId}` | Delete a review |

### 👤 Users

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{userId}/reviews` | Get all reviews from a user |

---

## 🧪 Testes

O projeto possui cobertura de testes unitários na **camada de Service**, utilizando **JUnit 5** e **Mockito**, com enforcement via **JaCoCo** (mínimo de **90% de cobertura de linha**).

### Estrutura dos Testes

```
src/test/java/com/biblioteca_api/biblioteca/
├── factories/                  # Object Mothers / Factories para dados de teste
│   ├── AuthorFactory.java
│   ├── BookFactory.java
│   ├── ReviewFactory.java
│   └── UserFactory.java
└── service/                    # Testes unitários dos Services
    ├── AuthServiceTest.java
    ├── AuthorServiceTest.java
    ├── AuthorizationServiceTest.java
    ├── BookServiceTest.java
    ├── ReviewServiceTest.java
    └── UserServiceTest.java
```

### Padrões e Práticas

- **`@ExtendWith(MockitoExtension.class)`**: Integração JUnit 5 + Mockito sem Spring Context.
- **`@Mock` / `@InjectMocks`**: Injeção de dependências mockadas para isolamento total.
- **Factory Pattern**: Uso de classes Factory (`BookFactory`, `UserFactory`, etc.) para criar entidades válidas de teste, evitando duplicação.
- **`@DisplayName` em português**: Cada teste possui uma descrição legível que documenta o comportamento esperado.

### Executar os Testes

```bash
./mvnw test
```

Para gerar o relatório de cobertura JaCoCo:

```bash
./mvnw verify
# Relatório disponível em: target/site/jacoco/index.html
```

---

## 🌱 Database Seeder

O projeto inclui um `DatabaseSeeder` que roda automaticamente na inicialização da aplicação. Ele cria um **usuário Admin padrão** caso ainda não exista no banco, facilitando o primeiro acesso de novos contribuidores.

| Campo | Valor |
|-------|-------|
| **Email** | `admin@exlibris.com` |
| **Senha** | `admin123` |
| **Role** | `ADMIN` |

> ⚠️ **Importante:** Altere a senha do Admin padrão em ambientes que não sejam de desenvolvimento.

---

## 📋 Prerequisites

* **Java 21+**
* **PostgreSQL 15+**
* **Maven 3.9+**

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/wirsindesgeyers/ExLibris.git
cd ExLibris
```

### 2. Configure as variáveis de ambiente

O projeto utiliza variáveis de ambiente para configuração. Crie um arquivo `.env` na raiz ou exporte as variáveis no seu shell:

```env
SERVER_PORT=8081
DB_URL=jdbc:postgresql://localhost:5432/bibliotecadb
DB_USER=your_username
DB_PASSWORD=your_password
SECRET=sua-chave-secreta-para-jwt
```

| Variável | Descrição |
|----------|-----------|
| `SERVER_PORT` | Porta onde a API será executada |
| `DB_URL` | URL JDBC de conexão com o PostgreSQL |
| `DB_USER` | Usuário do PostgreSQL |
| `DB_PASSWORD` | Senha do PostgreSQL |
| `SECRET` | **Chave secreta** utilizada para assinar e validar os tokens JWT (HMAC256) |

> ⚠️ A variável `SECRET` é **obrigatória**. Sem ela, a aplicação não conseguirá gerar nem validar tokens JWT.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

O servidor iniciará em `http://localhost:8081` (ou na porta configurada em `SERVER_PORT`).

Na primeira execução, o `DatabaseSeeder` criará o Admin padrão automaticamente — você verá a confirmação no log do console.

## 📖 API Documentation

The API is fully documented using Swagger/OpenAPI. Once the application is running, you can access:

* **Swagger UI (Interactive):** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
* **OpenAPI JSON:** [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

---

## 🏗️ Project Structure

```
src/main/java/com/biblioteca_api/biblioteca/
├── BibliotecaApplication.java      # Application entry point
├── controller/                     # REST Controllers (API Layer)
│   ├── AuthenticationController    # Login, Register, Admin Register
│   ├── BookController              # CRUD de Books
│   ├── AuthorController            # CRUD de Authors
│   ├── ReviewController            # Delete de Reviews
│   └── UserController              # Listagem de Users e Reviews por User
├── service/                        # Business Logic Layer
├── repository/                     # Data Access Layer (Spring Data JPA)
├── entities/                       # JPA Entities (Database Models)
│   └── UserRole.java               # Enum: ADMIN, LIBRARIAN, READER
├── dto/                            # Data Transfer Objects
└── infra/                          # Infrastructure & Configs
    ├── config/                     # Configurações gerais
    ├── dbseeding/                  # DatabaseSeeder (Admin padrão)
    ├── exceptions/                 # Exceções customizadas
    └── security/                   # SecurityConfiguration, SecurityFilter, TokenService
```

---

## 📝 Data Models

<details>
<summary><strong>Click to view Entity details</strong></summary>

### Book

* `id`: Unique identifier
* `title`: Book title (max 150 chars)
* `isbn`: ISBN-13 (Unique)
* `price`: Monetary value
* `publishedDate`: Date of publication
* `averageRating`: Average rating based on reviews
* `author`: Relationship with Author entity

### Author

* `id`: Unique identifier
* `name`: Full name (max 100 chars)
* `birthdate`: Date of birth
* `books`: List of authored books

### User

* `id`: Unique identifier
* `email`: User email (Unique)
* `name`: Full name
* `password`: Encrypted password (BCrypt)
* `role`: `ADMIN`, `LIBRARIAN` ou `READER`
* `loans`: List of loans
* `reviews`: List of reviews

### Review

* `id`: Unique identifier
* `user`: Reviewer
* `book`: Reviewed book
* `rating`: Numeric rating
* `comment`: Review text

### Loan

* `id`: Unique identifier
* `user`: Borrower
* `book`: Borrowed item
* `loanPrice`: Fee charged
* `loanDate`: Start date
* `expirationDate`: Due date
* `returnDate`: Actual return date

</details>

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## 📄 License

This project is licensed under the **MIT License**.

---

<div align="center">

**Made with 💜 by Kauan**

</div>
