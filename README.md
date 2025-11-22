# Bookstore API

**A Full-Featured RESTful Backend for an Online Bookstore**\
*Built with Spring Boot, Spring Security & MySQL*

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring
Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-000000?style=for-the-badge&logo=jsonwebtokens)
![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger)

------------------------------------------------------------------------

## 🚀 Overview

The **Bookstore API** is a complete e-commerce backend built with
**clean architecture, layered design, JWT-based authentication, testing,
and database migrations**.

It's designed as a **production-like portfolio project**, demonstrating:

-   Authentication & authorization\
-   CRUD operations\
-   JPA relationships\
-   Shopping cart & orders\
-   Pagination, filtering, mapping\
-   Testcontainers-based integration tests

This project is ideal for **job applications** and showcases real
backend engineering skills.

------------------------------------------------------------------------

## 🧠 Motivation

> **"Why build a bookstore?"**\
> Because it's a perfect real-world domain combining **security, domain
> logic, data modeling, and user workflows** --- everything, in one
> project.

Users can:

-   Browse and search books\
-   Register/login via JWT\
-   Add items to a persistent cart\
-   Place orders & track status\
-   Admins manage books, categories & orders

------------------------------------------------------------------------

## 🛠 Tech Stack

Layer            Technology
  ---------------- ----------------------------------------------------
**Language**     Java 17
**Framework**    Spring Boot 3.2
**Security**     Spring Security + JWT
**Database**     MySQL, Liquibase, Testcontainers
**ORM**          Spring Data JPA
**Validation**   Hibernate Validator
**Mapping**      MapStruct
**Testing**      JUnit 5, Mockito, Testcontainers, Spring Boot Test
**Docs**         Springdoc OpenAPI / Swagger UI
**Build**        Maven

------------------------------------------------------------------------

## ✨ Key Features

-   **JWT Authentication & Authorization**
-   **Role-Based Access (`USER`, `ADMIN`)**
-   **Book Management & Search**
-   **Category System (many-to-many)**
-   **Shopping Cart & Cart Items**
-   **Order Placement & Status Updates**
-   **Soft Delete for Books**
-   **Pagination & Sorting**
-   **Global Exception Handling**
-   **DTO + MapStruct Mapping Layer**

------------------------------------------------------------------------

## 📂 Project Structure (Clean Architecture)

    src/main/java/com.bookstore
     ├── config          # Security, JWT, Swagger
     ├── controller      # REST endpoints
     ├── dto             # Request/response DTOs
     ├── exception       # Global exception handling
     ├── mapper          # MapStruct mappers
     ├── model           # Entities
     ├── repository      # Spring Data JPA repositories
     ├── service         # Business logic
     └── util            # Helpers & utilities

------------------------------------------------------------------------

## 🧪 Testing

The project includes:

-   **Unit tests** (services, mappers)
-   **Integration tests with Testcontainers** (MySQL)
-   **MockMvc tests** for controllers

Example:

``` java
@Test
void getAllBooks_ShouldReturn200() throws Exception {
    mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3));
}
```

------------------------------------------------------------------------

## 🗄 ERD & Domain Model

### Entity Relationships

    User (1) — (1) ShoppingCart — (M) CartItem — (1) Book
    User (1) — (M) Order — (M) OrderItem — (1) Book
    Book (M) — (M) Category

### Mermaid Diagram

``` mermaid
classDiagram
    class User {
      Long id
      String email
      String password
      Role role
    }

    class Book {
      Long id
      String title
      String author
      String isbn
      BigDecimal price
      String description
      String coverImage
    }

    class Category {
      Long id
      String name
    }

    class ShoppingCart {
      Long id
      User user
    }

    class CartItem {
      Long id
      Book book
      Integer quantity
    }

    class Order {
      Long id
      User user
      OrderStatus status
      LocalDateTime orderDate
    }

    class OrderItem {
      Long id
      Book book
      Integer quantity
      BigDecimal price
    }

    User --> ShoppingCart
    ShoppingCart --> CartItem
    Book --> CartItem
    Book --> Category
    User --> Order
    Order --> OrderItem
    Book --> OrderItem
```

------------------------------------------------------------------------

## 📥 Installation & Setup

### 1. Clone the repository

``` bash
git clone https://github.com/aniliashenko/bookstore-api.git
cd bookstore-api
```

### 2. Create MySQL database

``` sql
CREATE DATABASE bookstore;
```

### 3. Configure environment

`application.properties`:

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
spring.liquibase.enabled=true
```

### 4. Run the application

``` bash
mvn clean install
mvn spring-boot:run
```

------------------------------------------------------------------------

## 📘 API Documentation

After launch:

-   Swagger UI → `http://localhost:8080/swagger-ui/index.html`
-   OpenAPI JSON → `http://localhost:8080/v3/api-docs`

------------------------------------------------------------------------

## 🔌 Example API Requests

### Register

``` http
POST /auth/registration
```

``` json
{
  "email": "user@example.com",
  "password": "password123",
  "repeatPassword": "password123"
}
```

### Login

``` http
POST /auth/login
```

``` json
{
  "email": "user@example.com",
  "password": "password123"
}
```

### Add item to cart

``` http
POST /cart
```

``` json
{
  "bookId": 2,
  "quantity": 1
}
```

------------------------------------------------------------------------

## 📦 Postman Collection

The repo includes:

    postman/
     └── bookstore-api.postman_collection.json

Import via:

**Postman → Import → File**

------------------------------------------------------------------------

## 👤 Author

**Anton Iliashenko**\
*Java Backend Developer \| Spring Boot \| Clean Architecture*

-   GitHub: https://github.com/aniliashenko\
-   LinkedIn: https://www.linkedin.com/in/anton-iliashenko-09495430a/\
-   Email: windsweptofficial@gmail.com
