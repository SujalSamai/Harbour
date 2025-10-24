## Estimated Flow
                        
                        ┌──────────────────────────────┐
                        │        Frontend (UI)         │
                        │  React / Angular / Vue.js    │
                        │  - Login/Register            │
                        │  - Portfolio Dashboard       │
                        │  - Stock Search              │
                        │  - Alerts & Analytics Views  │
                        └──────────────┬───────────────┘
                                       │  (REST API + JWT)
                                       ▼
                 ┌────────────────────────────────────────────────┐
                 │             API Gateway / Backend API          │
                 │ (Spring Boot 3.x + Spring Security + JWT)      │
                 │------------------------------------------------│
                 │ Routes requests to microservices via REST APIs │
                 └────────────────────────────────────────────────┘
                                       │
              ┌────────────────────────┼────────────────────────────┐
              │                        │                            │
              ▼                        ▼                            ▼
 ┌──────────────────────┐   ┌──────────────────────┐     ┌──────────────────────┐
 │  User/Auth Service   │   │ Portfolio Service    │     │   Stock Data Service │
 │----------------------│   │----------------------│     │----------------------│
 │ - User Registration  │   │ - Create Portfolio   │     │ - Fetch Live Stock   │
 │ - JWT Login          │   │ - Add Holdings       │     │   Data (WebClient)   │
 │ - Token Validation   │   │ - Compute P&L        │     │ - Scrape Screener.in │
 │                      │   │ - Portfolio CRUD     │     │ - Cache prices       │
 │ **Spring Security**  │   │ **Spring Data JPA**  │     │ **WebClient**        │
 └─────────┬────────────┘   └──────────┬───────────┘     └──────────┬──────────┘
           │                           │                              │
           │                           │                              │
           ▼                           ▼                              ▼
  ┌───────────────────┐     ┌───────────────────┐          ┌───────────────────┐
  │ Authentication DB │     │ Portfolio DB      │          │ External APIs /   │
  │ (Users table)     │     │ (Portfolios,      │          │ Web Scraping from │
  │                   │     │ Holdings, Alerts) │          │ Screener.in/NSE   │
  └───────────────────┘     └───────────────────┘          └───────────────────┘

              ┌────────────────────────┐
              │  Analytics Service     │
              │------------------------│
              │ - Portfolio P&L calc   │
              │ - Sector/Cap exposure  │
              │ - Risk profiling       │
              │ - Aggregation reports  │
              │ **Spring Boot + JPA**  │
              └───────────┬────────────┘
                          │
                          ▼
                ┌──────────────────────┐
                │  Analytics DB        │
                │  (Derived Data,      │
                │   Historical Records)│
                └──────────────────────┘

              ┌────────────────────────┐
              │  Alerts Service        │
              │------------------------│
              │ - Watch price triggers │
              │ - Send notifications   │
              │ - Scheduled jobs       │
              │ **Spring Scheduler**   │
              └───────────┬────────────┘
                          │
                          ▼
                ┌──────────────────────┐
                │ Notification Service │
                │ - Email/SMS (later)  │
                │ - Logs for now       │
                │ **JavaMail / Twilio**│
                └──────────────────────┘
