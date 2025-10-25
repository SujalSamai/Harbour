

# 🛳️ Harbour - Personal Portfolio Management

## 📘 Overview

**Harbour Portfolio Management** is a microservice-based investment tracking system designed to let users register, authenticate, and securely manage their investment portfolios.

Currently the system is split into three main microservices:

1. **Auth Service** — Handles user registration, login, and JWT generation.
2. **Portfolio Service** — Manages user portfolios and holdings, secured via JWT authentication.
3. **Equity Tracker Service** - Has an endpoint which accepts a ticker symbol and gets current data for that stock by scraping data from Screener.in

---

## 🧩 System Architecture

```
               ┌─────────────────────────-─┐
               │        Auth Service       │
               │---------------------------│
               │ /api/auth/register        │
               │ /api/auth/login           │
               │                           │
               │ Generates JWT with userId │
               └────────────┬──────────────┘
                            │
                            ▼
                Authorization: Bearer <JWT>
                            │
                            ▼
┌────────────────────────────────────────────────────┐
│                Portfolio Service                   │
│----------------------------------------------------│
│ /api/portfolio/** (secured endpoints)              │
│                                                    │
│ JWT validated by JwtTokenFilter                    │
│ Extracts userId → @AuthenticationPrincipal Long    │
│                                                    │
│ Handles portfolio CRUD                             |   
| Holdings CRUD management                           │
└────────────────────────────────────────────────────┘
                            │
                            |
                            |
                            ▼
┌────────────────────────────────────────────────────┐
│                Stock Tracker Service               │
│----------------------------------------------------│
│ /api/stocks/** (unsecured endpoints)               │
│                                                    │
│                                                    │
│ Expects a ticker symbol &                          |   
| Scraps data from Web                               │
└────────────────────────────────────────────────────┘


```

---

## ⚙️ Tech Stack

| Layer          | Technology                                          |
| -------------- | --------------------------------------------------- |
| **Language**   | Java 21                                             |
| **Framework**  | Spring Boot 3.5.x                                   |
| **Build Tool** | Maven                                               |
| **Database**   | (Planned: PostgreSQL / MySQL)                       |
| **Security**   | Spring Security + JWT                               |
| **Validation** | Jakarta Validation API (`@Valid`, `@NotNull`, etc.) |

---

## 🔐 Security Implementation

### 1. **Auth Service**

* Handles user registration and login.
* On successful login, returns a **JWT** containing:

  ```json
  {
    "token": "<jwt_token>",
    "id": <user_id>,
    "username": <user_name>
  }
  ```
* Uses `BCryptPasswordEncoder` for password hashing.

**Key Files:**

* `SecurityConfig.java`
* `AuthController.java`
* `JwtTokenProvider.java`

**Important Config:**

```java
http.csrf(csrf -> csrf.disable())
    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .anyRequest().authenticated());
```

---

### 2. **Portfolio Service**

* Every endpoint except `/api/auth/**` is **protected**.
* Uses a custom `JwtTokenFilter` that:

  1. Extracts the token from the `Authorization` header.
  2. Validates it using the same secret as Auth Service.
  3. Retrieves the `userId` from the token’s subject.
  4. Sets the authentication context with `userId` as principal.

**OncePerRequestFilter:**
`JwtTokenFilter.java` ensures each request has a valid JWT before it reaches controllers.

**Security Chain:**

```java
http.csrf(csrf -> csrf.disable())
    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .anyRequest().authenticated())
    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
```

---

## 📦 Portfolio Service Endpoints

| Method   | Endpoint              | Description                           | Auth Required |
| -------- | --------------------- | ------------------------------------- | ------------- |
| `POST`   | `/api/portfolio`      | Create new portfolio                  | ✅             |
| `GET`    | `/api/portfolio`      | Get all portfolios for logged-in user | ✅             |
| `GET`    | `/api/portfolio/{id}` | Get portfolio by ID                   | ✅             |
| `PUT`    | `/api/portfolio/{id}` | Update existing portfolio             | ✅             |
| `DELETE` | `/api/portfolio/{id}` | Delete portfolio                      | ✅             |
| `POST`   | `/api/portfolio/{portfolioId}/holdings`      | Add new holding into portfolio                  | ✅             |
| `GET`    | `/api/portfolio/{portfolioId}/holdings`      | Get all holdings for portfolio id | ✅             |
| `GET`    | `/api/portfolio/{portfolioId}/holdings/{id}` | Get holding by holdingId & portfolioId                   | ✅             |
| `PUT`    | `/api/portfolio/{portfolioId}/holdings/{id}` | Update existing holding             | ✅             |
| `DELETE` | `/api/portfolio/{portfolioId}/holdings/{id}` | Delete a holding                      | ✅             |
---

## 🧠 Data Models

### 🧩 PortfolioRequest

```json
{
  "portfolioName": "Tech Growth Portfolio",
}
```

### 🧩 EquityHoldingRequest

```json
{
  "stockSymbol": "TCS",
  "quantity": 15,
  "averagePrice": 3845.50,
  "purchaseDate": "2025-10-20"
}
```

---

## 🔁 End-to-End Flow

### Step 1: Register

```bash
curl -X POST http://localhost:4002/api/auth/register \
-H "Content-Type: application/json" \
-d '{"email": "user@example.com", "password": "secret123"}'
```

### Step 2: Login to Get JWT

```bash
curl -X POST http://localhost:4002/api/auth/login \
-H "Content-Type: application/json" \
-d '{"email": "user@example.com", "password": "secret123"}'
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

### Step 3: Use Token in Portfolio Service

```bash
curl --location 'http://localhost:4000/api/portfolio' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...' \
--data '{
    "portfolioName": "Demo Portfolio"
}'
```

---

## 🧾 Current Progress

✅ Implemented **JWT-based stateless authentication** <br>
✅ Created **Auth Service** for registration & login <br>
✅ Created **Portfolio Service** with secured endpoints <br>
✅ Integrated **cross-service JWT validation** <br>
✅ Used **@AuthenticationPrincipal Long userId** for contextual user access<br>
✅ Defined **DTOs**, **Validation**, and **CRUD APIs**

---

## 🚀 Next Steps

* Add database persistence (JPA + MySQL/PostgreSQL)
* Implement `Holding` entity and relation to `Portfolio`
* Introduce API Gateway 
* Analysis for the portfolio holdings
* Frontend part








## Estimated Flow

<img width="1330" height="1190" alt="Untitled-2025-09-16-1454" src="https://github.com/user-attachments/assets/e40ca0a9-99a7-46f6-8058-b0c117def1f4" />

