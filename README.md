
# 🏛️ ExLibris

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-brightgreen?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Enabled-red?style=for-the-badge&logo=flyway&logoColor=white)

<br>

**A modern RESTful API for library management and social discovery.**
<br>
*Inspired by Letterboxd, ExLibris combines robust inventory management with social features like reviews, ratings, and reading tracking.*


</div>

---

## ✨ Features

- **📚 Book Management** – Full CRUD operations for books with ISBN validation.
- **✍️ Author Management** – Create and manage authors and link them to their bibliographies.
- **👤 User Management** –(In Progress) User registration system with email validation.
- **📅 Loan System** –(In Progress) Track book loans, manage expiration dates, and calculate fees.
- **⭐ Social & Discovery** – *(In Progress)* Rate books, write reviews, and track reading status.
- **🔍 API Documentation** – Interactive Swagger UI with OpenAPI 3.0 integration.
- **🗄️ Database Migrations** – Version-controlled schema management using Flyway.

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.0.1 |
| **Database** | PostgreSQL |
| **ORM** | Spring Data JPA / Hibernate |
| **Migrations** | Flyway |
| **Documentation** | SpringDoc OpenAPI 3.0 |
| **Validation** | Jakarta Bean Validation |
| **Utilities** | Lombok |

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

* **Java 21+**
* **PostgreSQL 15+**
* **Maven 3.9+**

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/wirsindesgeyers/ExLibris.git
cd ExLibris

```

### 2. Configure the database

Open `src/main/resources/application.properties` and update your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=your_username
spring.datasource.password=your_password

```

### 3. Run the application

You can run the application using the Maven wrapper:

```bash
./mvnw spring-boot:run

```

The server will start at `http://localhost:8081`.

## 📖 API Documentation

The API is fully documented using Swagger/OpenAPI. Once the application is running, you can access:

* **Swagger UI (Interactive):** [http://localhost:8081/swagger-ui.html](https://www.google.com/search?q=http://localhost:8081/swagger-ui.html)
* **OpenAPI JSON:** [http://localhost:8081/v3/api-docs](https://www.google.com/search?q=http://localhost:8081/v3/api-docs)

## 🔗 Main Endpoints

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

### 👤 Reviews (Roadmap)

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/books/{id}/reviews` | Add a review and rating |
| `GET` | `/api/books/{id}/reviews` | Get reviews for a book |

*(Full list of endpoints available in Swagger UI)*

## 🏗️ Project Structure

The project follows a clean architecture pattern with separated concerns:

```
src/main/java/com/biblioteca_api/biblioteca/
├── BibliotecaApplication.java      # Application entry point
├── controller/                     # REST Controllers (API Layer)
│   
├── service/                        # Business Logic Layer
├── repository/                     # Data Access Layer (Spring Data JPA)
├── entities/                       # JPA Entities (Database Models)
│   
├── dto/                            # Data Transfer Objects
│
└── infra/                          # Infrastructure & Configs

```

## 📝 Data Models

<details>
<summary><strong>Click to view Entity details</strong></summary>

### Book

* `id`: Unique identifier
* `title`: Book title (max 150 chars)
* `isbn`: ISBN-13 (Unique)
* `price`: Monetary value
* `publishedDate`: Date of publication
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
* `password`: Encrypted password

### Loan

* `id`: Unique identifier
* `user`: Borrower
* `book`: Borrowed item
* `loanPrice`: Fee charged
* `loanDate`: Start date
* `expirationDate`: Due date
* `returnDate`: Actual return date

</details>

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

```

```
