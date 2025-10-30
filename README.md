# Bookstore API
**A Full-Featured RESTful Backend for an Online Bookstore**  
*Built with Spring Boot, Spring Security & MySQL*

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-000000?style=for-the-badge&logo=jsonwebtokens)
![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger)

---

## Inspiration & Purpose

> **"Why build another bookstore?"**  
Because every great developer needs a **real-world project** that ties together **authentication, CRUD, relationships, security, and testing** — all in one clean, scalable system.

This **Bookstore API** simulates a **fully functional e-commerce backend**, empowering users to:
- Browse & search books by title/author
- Register/login with **JWT-based authentication**
- Add books to a **persistent shopping cart**
- Place **orders** and track status
- Admins manage inventory & order fulfillment

Perfect for **job applications**, **technical interviews**, and **portfolio building**.

---

## Tech Stack & Tools

| Layer             | Technology |
|------------------|-----------|
| **Language**      | Java 17 |
| **Framework**     | Spring Boot 3.2 |
| **Security**      | Spring Security + JWT |
| **Database**      | MySQL + Testcontainers |
| **ORM**           | Spring Data JPA / Hibernate |
| **Validation**    | Bean Validation (Hibernate Validator) |
| **Testing**       | JUnit 5, Mockito, Testcontainers |
| **API Docs**      | Springdoc OpenAPI (Swagger UI) |
| **Build Tool**    | Maven |
| **Utilities**     | Lombok, MapStruct |

---

## Key Features

- **User Authentication** – Register & login with JWT
- **Role-Based Access** – `USER` & `ADMIN` roles
- **Shopping Cart** – Add, update, remove items
- **Order Management** – Place orders, view history
- **Book Search** – Filter by title, author, category
- **Category System** – Many-to-many book categorization
- **Soft Delete** – Logical deletion for data integrity
- **Pagination & Sorting** – Efficient data retrieval

---

## API Endpoints

Explore the API interactively:  
**Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
**OpenAPI Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Public Endpoints
- `POST /auth/registration` – Create account
- `POST /auth/login` – Get JWT token
- `GET /books`, `GET /categories` – Browse catalog

### User Endpoints (JWT Required)
- `GET /cart` – View cart
- `POST /cart` – Add book
- `PUT /cart/cart-items/{id}` – Update quantity
- `DELETE /cart/cart-items/{id}` – Remove item
- `POST /orders` – Checkout
- `GET /orders` – Order history

### Admin Endpoints
- `POST /books`, `PUT /books/{id}` – Manage books
- `PATCH /orders/{id}/status` – Update order status

---

## Author

**Anton Iliashenko**  
*Java Backend Developer | Spring Boot | Clean Architecture*

[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/aniliashenko)  
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/anton-iliashenko-09495430a/)  
windsweptofficial@gmail.com