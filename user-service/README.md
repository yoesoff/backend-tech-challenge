# 🏬 Real Estate App — Modern Java Spring Boot Backend for Shop & Warehouse Management

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![Docker](https://img.shields.io/badge/Docker-ready-blue?logo=docker)
![Liquibase](https://img.shields.io/badge/DB%20Migration-Liquibase-lightgrey?logo=databricks)
![Build](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven)
![Security](https://img.shields.io/badge/Security-JWT%20Auth-green?logo=jsonwebtokens)
![License](https://img.shields.io/badge/License-MIT-blue)

---
## 🚀 Running the Project

You can run this project in multiple ways depending on your setup and preference.

---

### 🐳 1. Run with Docker Compose (Recommended)

This is the easiest and most consistent way to run the backend in a production-like environment.

Make sure you have:
- 🧩 [Docker](https://www.docker.com/get-started)
- 🧩 [Docker Compose](https://docs.docker.com/compose/install/)

Then simply run:

```bash
docker-compose up --build
```

---

## 📖 Overview

**Real Estate App** is a clean, secure, and scalable **Java Spring Boot backend** built for **Shop and Warehouse Management**.  
It demonstrates production-grade design, strong separation of concerns, and modern engineering practices such as **containerization**, **API documentation**, and **test coverage**.

Originally developed as a **technical assessment**, it reflects a professional standard in:
- Backend architecture & clean code
- Secure authentication using JWT
- Robust validation and error handling
- Well-structured domain-driven design

---

## ✨ Features

**Authentication & Authorization**
- JWT-based login & registration
- Role-based access control (Admin/User)
- BCrypt password hashing

**Database Migrations**
- Liquibase XML changelogs ensure versioned schema changes

**API Documentation**
- Auto-generated Swagger / OpenAPI UI

**Containerization**
- Ready-to-run Docker setup with MySQL + Spring Boot

**Logging & Monitoring**
- SLF4J structured logging
- Spring Actuator endpoints for health checks

---

## Architecture & Design Patterns

This project follows **Clean Architecture** and **Layered Design**, making it highly maintainable and extensible.

### Key Design Concepts

- **DTO Pattern** → separates internal entities from API responses.  
- **Service Pattern** → keeps controllers lightweight and business rules centralized.  
- **Repository Pattern** → abstracts persistence layer using Spring Data JPA.  
- **Exception Handling Pattern** → custom exceptions like `BadRequestException`, `ResourceNotFoundException`, with meaningful messages and HTTP status codes.  
- **Transaction Management** → handled by `@Transactional` to ensure data consistency when performing sales or stock updates.  
- **Dependency Injection** → powered by Spring’s IoC container for clear decoupling.  
- **Single Responsibility Principle (SRP)** → every class has one reason to change.  
- **Open/Closed Principle (OCP)** → the system is easy to extend (e.g., adding new modules like Purchase, Supplier, or Inventory Audit) without modifying core logic.

---

## Additional Features & Validations

### Robust Validation
- Prevents negative or zero quantities in sales and stock.
- Ensures prices are non-negative using `BigDecimal.compareTo()`.
- Handles `null` safety gracefully for DTO fields.
- Throws domain-specific exceptions with user-friendly English messages.

### Transaction Safety
- Uses `@Transactional` for atomic operations during sales creation.
- Rolls back automatically if any stock update or validation fails.

### Error Handling
Unified global exception responses such as:
```json
{
  "timestamp": "2025-11-11T07:30:00Z",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Variant not found with id: 123e4567..."
}


