# Auth Service

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.31-blue?style=flat-square&logo=quarkus)](https://quarkus.io/)
[![Port](https://img.shields.io/badge/Port-8081-green?style=flat-square)]()

The **Auth Service** handles all authentication and credential management for the DnD Platform. It provides JWT-based login, token refresh, OTP (one-time password) login via email, and password reset flows. It does **not** store user data directly — it delegates user lookups and password updates to the [User Service](../user-service) via REST client calls.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Operative Flows](#operative-flows)
  - [Login (JWT)](#1-login-jwt-token-creation)
  - [Token Refresh](#2-token-refresh)
  - [Logout (Single)](#3-logout-single-session)
  - [Logout (All)](#4-logout-all-sessions)
  - [OTP Login Request](#5-otp-login-request)
  - [OTP Login Validation](#6-otp-login-validation)
  - [Password Reset Request](#7-password-reset-request)
  - [Password Reset Execution](#8-password-reset-execution)
- [Design Patterns](#design-patterns)
- [Security Details](#security-details)
- [Inter-Service Communication](#inter-service-communication)
- [Data Model](#data-model)

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Auth Service (:8081)                         │
│                                                                     │
│  ┌─────────────────┐    ┌──────────────┐    ┌───────────────────┐  │
│  │ Adapter-Inbound │    │    Domain     │    │ Adapter-Outbound  │  │
│  │                 │    │              │    │                   │  │
│  │ ResourceImpl ──►│───►│  Service ────│───►│ JPA Repository    │  │
│  │   Delegate      │    │  Interface   │    │ REST Client       │  │
│  │   Mapper        │    │  Impl        │    │ RabbitMQ Publisher│  │
│  └─────────────────┘    └──────────────┘    └────────┬──────────┘  │
│                                                       │             │
└───────────────────────────────────────────────────────┼─────────────┘
                                                        │
                    ┌───────────────────────────────────┼──────────┐
                    │                                   │          │
                    ▼                                   ▼          ▼
             ┌─────────────┐                    ┌────────────┐ ┌────────┐
             │ User Service│                    │ PostgreSQL │ │RabbitMQ│
             │   (:8089)   │                    │  (auth_db) │ │(emails)│
             └─────────────┘                    └────────────┘ └────────┘
```

---

## Module Structure

Follows **Hexagonal Architecture** (Ports & Adapters):

```
auth-service/
├── auth-service-domain/              # Business logic & domain models
│   ├── model/                        # Records: User, RefreshToken, OtpLoginToken, PasswordResetToken
│   ├── CreateLoginTokensService      # Login orchestration
│   ├── RefreshLoginTokensService     # Token refresh logic
│   ├── LogoutService / LogoutAllService
│   ├── RequestOtpLoginService        # OTP generation & email dispatch
│   ├── ValidateOtpLoginService       # OTP validation & JWT issuance
│   ├── RequestPasswordResetService   # Reset token generation
│   └── ResetPasswordService          # Password update & session revocation
│
├── auth-service-view-model/          # DTOs & Resource interfaces
│   └── vm/                           # Request/Response ViewModels
│
├── auth-service-adapter-inbound/     # REST controllers (Driving Adapters)
│   ├── login/                        # Login endpoint + Delegate
│   ├── refresh/                      # Token refresh endpoint + Delegate
│   ├── logout/                       # Logout endpoints + Delegates
│   ├── otplogin/                     # OTP login endpoints + Delegates
│   └── passwordreset/                # Password reset endpoints + Delegates
│
├── auth-service-adapter-outbound/    # Infrastructure (Driven Adapters)
│   ├── jpa/                          # Panache entities & JPA repositories
│   ├── rest/                         # User Service REST client
│   └── messaging/                    # RabbitMQ email publishers
│
├── auth-service-client/              # REST client interface for other services
└── auth-service/                     # Quarkus bootstrap & configuration
```

---

## API Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/auth/login-tokens` | Public | Login with username/password |
| `POST` | `/auth/login-tokens/refreshed` | Public | Refresh an access token |
| `DELETE` | `/auth/login-tokens/{refreshToken}` | Bearer | Logout single session |
| `DELETE` | `/auth/login-tokens` | Bearer | Logout all sessions |
| `POST` | `/auth/otp-login-requests` | Public | Request OTP code via email |
| `POST` | `/auth/otp-login-tokens` | Public | Validate OTP and get tokens |
| `POST` | `/auth/password-resets` | Public | Request password reset email |
| `PUT` | `/auth/password-resets` | Public | Reset password with token |

---

## Operative Flows

### 1. Login (JWT Token Creation)

**`POST /auth/login-tokens`** — Creates a JWT access token and refresh token after validating user credentials.

```
Client                    Auth Service                          User Service       Database
  │                           │                                      │                │
  │  POST /auth/login-tokens  │                                      │                │
  │  {username, password}     │                                      │                │
  │──────────────────────────►│                                      │                │
  │                           │                                      │                │
  │                           │  ┌──────────────────────────────┐    │                │
  │                           │  │ ResourceImpl                 │    │                │
  │                           │  │  └► Delegate (Delegate Pattern)   │                │
  │                           │  │       └► Mapper (ViewModel→Domain)│                │
  │                           │  └──────────────────────────────┘    │                │
  │                           │                                      │                │
  │                           │  POST /users/credentials-validation  │                │
  │                           │─────────────────────────────────────►│                │
  │                           │         User (validated)             │                │
  │                           │◄─────────────────────────────────────│                │
  │                           │                                      │                │
  │                           │  createRefreshToken(userId)                           │
  │                           │──────────────────────────────────────────────────────►│
  │                           │         RefreshToken (UUID, 30d TTL)                  │
  │                           │◄──────────────────────────────────────────────────────│
  │                           │                                      │                │
  │                           │  ┌──────────────────────────────┐    │                │
  │                           │  │ JwtGenerationRepository      │    │                │
  │                           │  │  SmallRye JWT (RS256)        │    │                │
  │                           │  │  Claims: sub, upn, role,     │    │                │
  │                           │  │  username, groups             │    │                │
  │                           │  └──────────────────────────────┘    │                │
  │                           │                                      │                │
  │  201 Created              │                                      │                │
  │  {accessToken,            │                                      │                │
  │   refreshToken,           │                                      │                │
  │   expiresAt, userId}      │                                      │                │
  │◄──────────────────────────│                                      │                │
```

The **Delegate Pattern** separates HTTP concerns (`ResourceImpl`) from business logic (`Delegate`). The `CreateLoginTokensMapper` transforms the ViewModel into the domain `CreateLoginTokens` record, and the `LoginResponseViewModelMapper` maps the result back.

---

### 2. Token Refresh

**`POST /auth/login-tokens/refreshed`** — Generates a new access token from a valid, non-revoked refresh token.

```
Client                       Auth Service                    Database
  │                               │                              │
  │  POST /login-tokens/refreshed │                              │
  │  {token, userId}              │                              │
  │──────────────────────────────►│                              │
  │                               │  findByTokenAndId()          │
  │                               │─────────────────────────────►│
  │                               │  RefreshToken                │
  │                               │◄─────────────────────────────│
  │                               │                              │
  │                               │  ┌────────────────────────┐  │
  │                               │  │ Validate:              │  │
  │                               │  │  - Not revoked         │  │
  │                               │  │  - Not expired         │  │
  │                               │  └────────────────────────┘  │
  │                               │                              │
  │                               │  findById(userId)            │
  │                               │──► User Service ────────────►│
  │                               │◄─────────────────────────────│
  │                               │                              │
  │                               │  Generate new JWT (SmallRye) │
  │                               │                              │
  │  201 {accessToken, expiresAt} │                              │
  │◄──────────────────────────────│                              │
```

---

### 3. Logout (Single Session)

**`DELETE /auth/login-tokens/{refreshToken}`** — Revokes a single refresh token.

```
Client                          Auth Service              Database
  │                                  │                        │
  │  DELETE /login-tokens/{token}    │                        │
  │  Authorization: Bearer <jwt>     │                        │
  │─────────────────────────────────►│                        │
  │                                  │  UPDATE revoked=true   │
  │                                  │  WHERE token AND user  │
  │                                  │───────────────────────►│
  │                                  │◄───────────────────────│
  │  204 No Content                  │                        │
  │◄─────────────────────────────────│                        │
```

---

### 4. Logout (All Sessions)

**`DELETE /auth/login-tokens`** — Revokes all refresh tokens for the user.

```
Client                          Auth Service              Database
  │                                  │                        │
  │  DELETE /login-tokens            │                        │
  │  Authorization: Bearer <jwt>     │                        │
  │─────────────────────────────────►│                        │
  │                                  │  UPDATE revoked=true   │
  │                                  │  WHERE userId=X        │
  │                                  │───────────────────────►│
  │                                  │◄───────────────────────│
  │  204 No Content                  │                        │
  │◄─────────────────────────────────│                        │
```

---

### 5. OTP Login Request

**`POST /auth/otp-login-requests`** — Generates a 6-digit OTP code and sends it to the user's email. The OTP is **SHA-256 hashed before storage** (Hash-before-store pattern).

```
Client                    Auth Service               User Service     Database      RabbitMQ
  │                           │                           │               │              │
  │  POST /otp-login-requests │                           │               │              │
  │  {email}                  │                           │               │              │
  │──────────────────────────►│                           │               │              │
  │                           │  findByEmail(email)       │               │              │
  │                           │──────────────────────────►│               │              │
  │                           │  Optional<User>           │               │              │
  │                           │◄──────────────────────────│               │              │
  │                           │                           │               │              │
  │                           │  ┌──────────────────────────────────┐     │              │
  │                           │  │ If user not found → return 202  │     │              │
  │                           │  │ (silent fail — prevents user    │     │              │
  │                           │  │  enumeration attacks)           │     │              │
  │                           │  └──────────────────────────────────┘     │              │
  │                           │                           │               │              │
  │                           │  SecureRandom → 6-digit   │               │              │
  │                           │  SHA-256(otp) → persist   │               │              │
  │                           │───────────────────────────────────────────►│              │
  │                           │                           │               │              │
  │                           │  Publish email event      │               │              │
  │                           │  (templateId: 3)          │               │              │
  │                           │──────────────────────────────────────────────────────────►│
  │                           │                           │               │              │
  │  202 Accepted             │                           │               │              │
  │◄──────────────────────────│                           │               │              │
```

---

### 6. OTP Login Validation

**`POST /auth/otp-login-tokens`** — Validates the OTP code, marks it as used, and returns JWT tokens.

```
Client                    Auth Service                User Service     Database
  │                           │                            │               │
  │  POST /otp-login-tokens   │                            │               │
  │  {email, otpCode}         │                            │               │
  │──────────────────────────►│                            │               │
  │                           │  findByEmail(email)        │               │
  │                           │───────────────────────────►│               │
  │                           │  User                      │               │
  │                           │◄───────────────────────────│               │
  │                           │                            │               │
  │                           │  SHA-256(otpCode) → lookup │               │
  │                           │────────────────────────────────────────────►│
  │                           │  OtpLoginToken             │               │
  │                           │◄────────────────────────────────────────────│
  │                           │                            │               │
  │                           │  ┌─────────────────────┐   │               │
  │                           │  │ Validate:            │   │               │
  │                           │  │  - Not used          │   │               │
  │                           │  │  - Not expired       │   │               │
  │                           │  │  - Correct userId    │   │               │
  │                           │  └─────────────────────┘   │               │
  │                           │                            │               │
  │                           │  markUsed + createRefresh  │               │
  │                           │  + generateJWT             │               │
  │                           │────────────────────────────────────────────►│
  │                           │                            │               │
  │  201 {accessToken,        │                            │               │
  │       refreshToken, ...}  │                            │               │
  │◄──────────────────────────│                            │               │
```

---

### 7. Password Reset Request

**`POST /auth/password-resets`** — Generates a password reset token (SHA-256 hashed) and sends an email.

```
Client                    Auth Service               User Service     Database      RabbitMQ
  │                           │                           │               │              │
  │  POST /password-resets    │                           │               │              │
  │  {email}                  │                           │               │              │
  │──────────────────────────►│                           │               │              │
  │                           │  findByEmail(email)       │               │              │
  │                           │──────────────────────────►│               │              │
  │                           │  Optional<User>           │               │              │
  │                           │◄──────────────────────────│               │              │
  │                           │                           │               │              │
  │                           │  ┌──────────────────────────────────┐     │              │
  │                           │  │ Silent fail if user not found   │     │              │
  │                           │  └──────────────────────────────────┘     │              │
  │                           │                           │               │              │
  │                           │  Generate token + SHA-256 │               │              │
  │                           │───────────────────────────────────────────►│              │
  │                           │                           │               │              │
  │                           │  Publish email event      │               │              │
  │                           │  (templateId: 2)          │               │              │
  │                           │──────────────────────────────────────────────────────────►│
  │                           │                           │               │              │
  │  202 Accepted             │                           │               │              │
  │◄──────────────────────────│                           │               │              │
```

---

### 8. Password Reset Execution

**`PUT /auth/password-resets`** — Validates the reset token, updates the password, and revokes all sessions.

```
Client                    Auth Service               User Service     Database
  │                           │                           │               │
  │  PUT /password-resets     │                           │               │
  │  {token, newPassword}     │                           │               │
  │──────────────────────────►│                           │               │
  │                           │  SHA-256(token) → lookup  │               │
  │                           │───────────────────────────────────────────►│
  │                           │  PasswordResetToken       │               │
  │                           │◄───────────────────────────────────────────│
  │                           │                           │               │
  │                           │  ┌─────────────────────┐  │               │
  │                           │  │ Validate:            │  │               │
  │                           │  │  - Not used          │  │               │
  │                           │  │  - Not expired       │  │               │
  │                           │  └─────────────────────┘  │               │
  │                           │                           │               │
  │                           │  PUT /users/{id}/password │               │
  │                           │──────────────────────────►│               │
  │                           │                           │               │
  │                           │  markUsed(token)          │               │
  │                           │───────────────────────────────────────────►│
  │                           │                           │               │
  │                           │  revokeAllTokens(userId)  │               │
  │                           │───────────────────────────────────────────►│
  │                           │                           │               │
  │  204 No Content           │                           │               │
  │◄──────────────────────────│                           │               │
```

---

## Design Patterns

### Hexagonal Architecture (Ports & Adapters)

The domain core defines **repository interfaces** (ports) and is completely free of infrastructure dependencies. Adapters implement these ports:

```
                    ┌───────────────────────────────┐
   Driving Ports    │         DOMAIN CORE            │   Driven Ports
   (Inbound)        │                                 │   (Outbound)
  ──────────────────│  Service Interfaces             │──────────────────
                    │  Domain Records (@Builder)      │
  ResourceImpl ────►│  Business Rules                 │────► JPA Repositories (Panache)
  Delegate          │                                 │────► REST Client → User Service
  Mapper            │  CreateLoginTokensService       │────► JwtGenerationRepository
                    │  RefreshLoginTokensService      │────► RabbitMQ Publishers
                    │  Logout[All]Service             │
                    │  RequestOtpLoginService          │
                    │  ValidateOtpLoginService         │
                    │  Request/ResetPasswordService    │
                    └───────────────────────────────┘
```

### Delegate Pattern

Each REST endpoint is split into two classes to separate concerns:

| Class | Responsibility |
|-------|---------------|
| **ResourceImpl** | JAX-RS annotations, `@RolesAllowed`, `@PermitAll`, HTTP status codes |
| **Delegate** (`@Delegate`) | Business orchestration, service calls, DTO mapping |

The `@Delegate` annotation (from the `common` module) marks the delegate as the CDI-injected implementation of the resource interface.

### Mapper Pattern (Function\<A, B\>)

All data transformations use MapStruct mappers implementing `Function<A, B>`, operating at each architectural boundary:

```
ViewModel ──[InboundMapper]──► Domain Record ──[OutboundMapper]──► JPA Entity
    ◄──[ResponseMapper]────            ◄──[EntityMapper]──────
```

### Repository Pattern (Fine-Grained Ports)

The domain defines **one interface per operation** for maximum flexibility:

| Domain Interface | Outbound Adapter |
|-----------------|------------------|
| `UserCredentialsValidateRepository` | REST client → User Service |
| `RefreshTokenCreateRepository` | JPA (Panache) |
| `JwtGenerationRepository` | SmallRye JWT (RS256) |
| `OtpLoginEmailSendRepository` | RabbitMQ publisher |
| `PasswordResetEmailSendRepository` | RabbitMQ publisher |

---

## Security Details

| Mechanism | Implementation |
|-----------|---------------|
| **JWT Algorithm** | RS256 (RSA + SHA-256) via SmallRye JWT |
| **JWT Claims** | `sub` (userId), `upn` (email), `username`, `role`, `groups` |
| **Access Token TTL** | 3600 seconds (1 hour) |
| **Refresh Token TTL** | 30 days (configurable) |
| **OTP TTL** | 5 minutes (configurable) |
| **Password Reset TTL** | 15 minutes (configurable) |
| **Token Hashing** | SHA-256 for OTP and reset tokens — plaintext never stored |
| **OTP Generation** | `SecureRandom`, 6-digit zero-padded code |
| **Enumeration Prevention** | Email-based endpoints return `202` regardless of user existence |

---

## Inter-Service Communication

| Direction | Target | Protocol | Purpose |
|-----------|--------|----------|---------|
| **Outbound (sync)** | User Service `:8089` | REST | Credential validation, user lookup, password update |
| **Outbound (async)** | Notification Service | RabbitMQ (`email-requests-out`) | OTP codes and reset links |

---

## Data Model

```
┌──────────────────────────┐
│     refresh_tokens       │
├──────────────────────────┤
│ id          BIGSERIAL PK │
│ token       VARCHAR  UQ  │
│ user_id     BIGINT       │
│ expires_at  TIMESTAMP    │
│ revoked     BOOLEAN      │
│ created_at  TIMESTAMP    │
└──────────────────────────┘

┌──────────────────────────┐
│       login_otps         │
├──────────────────────────┤
│ id          BIGSERIAL PK │
│ token       VARCHAR  UQ  │  ← SHA-256 hash
│ user_id     BIGINT       │
│ expires_at  TIMESTAMP    │
│ used        BOOLEAN      │
│ created_at  TIMESTAMP    │
└──────────────────────────┘

┌──────────────────────────┐
│    password_resets        │
├──────────────────────────┤
│ id          BIGSERIAL PK │
│ token       VARCHAR  UQ  │  ← SHA-256 hash
│ user_id     BIGINT       │
│ expires_at  TIMESTAMP    │
│ used        BOOLEAN      │
│ created_at  TIMESTAMP    │
└──────────────────────────┘
```
