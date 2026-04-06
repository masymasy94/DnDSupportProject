# User Service

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.31-blue?style=flat-square&logo=quarkus)](https://quarkus.io/)
[![Port](https://img.shields.io/badge/Port-8089-green?style=flat-square)]()

The **User Service** manages user registration, profile retrieval, credential validation, and password updates for the DnD Platform. It serves both external client requests (registration, profile lookup) and internal service-to-service calls from the [Auth Service](../auth-service) (credential validation, email lookup, password updates).

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Operative Flows](#operative-flows)
  - [User Registration](#1-user-registration)
  - [Find User by ID](#2-find-user-by-id)
  - [Credential Validation (Internal)](#3-credential-validation-internal)
  - [Find by Email (Internal)](#4-find-by-email-internal)
  - [Update Password (Internal)](#5-update-password-internal)
- [Design Patterns](#design-patterns)
- [Security Details](#security-details)
- [Event-Driven Communication](#event-driven-communication)
- [Data Model](#data-model)

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                       User Service (:8089)                          │
│                                                                     │
│  ┌─────────────────┐    ┌──────────────┐    ┌───────────────────┐  │
│  │ Adapter-Inbound │    │    Domain     │    │ Adapter-Outbound  │  │
│  │                 │    │              │    │                   │  │
│  │ ResourceImpl ──►│───►│  Service ────│───►│ JPA Repository    │  │
│  │   Delegate      │    │  Interface   │    │ RabbitMQ Publisher│  │
│  │   Mapper        │    │  Impl        │    │                   │  │
│  └─────────────────┘    └──────────────┘    └────────┬──────────┘  │
│                                                       │             │
└───────────────────────────────────────────────────────┼─────────────┘
                                                        │
                              ┌─────────────────────────┼──────────┐
                              │                         │          │
                              ▼                         ▼          │
                       ┌────────────┐           ┌────────┐         │
                       │ PostgreSQL │           │RabbitMQ│         │
                       │ (user_db)  │           │(emails)│         │
                       └────────────┘           └────────┘         │
                                                                    │
       Auth Service (:8081) ───────────────────────────────────────►│
         (credential validation, email lookup, password update)     │
```

---

## Module Structure

```
user-service/
├── user-service-domain/              # Business logic & domain models
│   ├── model/                        # Records: User, UserRegister, UserCredentialsValidate
│   ├── util/CryptUtil                # BCrypt hashing (cost 12)
│   ├── event/                        # UserRegisteredEvent, EmailSendRepository
│   ├── UserRegisterService           # Registration with uniqueness check
│   ├── UserFindByIdService           # Profile lookup
│   ├── UserFindByEmailService        # Email-based lookup
│   ├── UserCredentialsValidateService# Credential validation
│   └── UserUpdatePasswordService     # Password update
│
├── user-service-view-model/          # DTOs & Resource interfaces
│   └── vm/                           # UserViewModel, UserRegisterViewModel, etc.
│
├── user-service-adapter-inbound/     # REST controllers (Driving Adapters)
│   ├── register/                     # POST /users
│   ├── findbyid/                     # GET /users/{id}
│   ├── findbyemail/                  # POST /users/email-lookup (internal)
│   ├── validate/                     # POST /users/credentials-validation (internal)
│   └── updatepassword/               # PUT /users/{id}/password (internal)
│
├── user-service-adapter-outbound/    # Infrastructure (Driven Adapters)
│   ├── jpa/                          # Panache entity & repositories
│   └── messaging/                    # RabbitMQ welcome email publisher
│
├── user-service-client/              # REST client interface for other services
└── user-service/                     # Quarkus bootstrap & configuration
```

---

## API Endpoints

| Method | Path | Auth | Scope | Description |
|--------|------|------|-------|-------------|
| `POST` | `/users` | Public | External | Register new user |
| `GET` | `/users/{id}` | Bearer (`PLAYER`) | External | Get user profile |
| `POST` | `/users/email-lookup` | `@PermitAll` | Internal | Find user by email |
| `POST` | `/users/credentials-validation` | `@PermitAll` | Internal | Validate credentials |
| `PUT` | `/users/{id}/password` | `@PermitAll` | Internal | Update password |

> **Internal** endpoints are called by the Auth Service via REST client and marked `@PermitAll` (no JWT required) since they run within the trusted backend network.

---

## Operative Flows

### 1. User Registration

**`POST /users`** — Registers a new user with uniqueness validation, BCrypt password hashing, and a welcome email event.

```
Client                     User Service                           Database       RabbitMQ
  │                             │                                     │              │
  │  POST /users                │                                     │              │
  │  {username, email,          │                                     │              │
  │   password}                 │                                     │              │
  │────────────────────────────►│                                     │              │
  │                             │                                     │              │
  │                             │  ┌────────────────────────────────┐ │              │
  │                             │  │ ResourceImpl                  │ │              │
  │                             │  │  └► Delegate (Delegate Pattern)│ │              │
  │                             │  │      └► UserRegisterMapper     │ │              │
  │                             │  └────────────────────────────────┘ │              │
  │                             │                                     │              │
  │                             │  ┌────────────────────────────────┐ │              │
  │                             │  │ UserRegisterServiceImpl        │ │              │
  │                             │  │                                │ │              │
  │                             │  │ 1. existsByUsernameOrEmail()   │ │              │
  │                             │  │    → ConflictException if dup  │─────────────►│
  │                             │  │                                │ │              │
  │                             │  │ 2. CryptUtil.hashPassword()   │ │              │
  │                             │  │    (BCrypt, cost 12)           │ │              │
  │                             │  │                                │ │              │
  │                             │  │ 3. Build User record           │ │              │
  │                             │  │    role=PLAYER, active=true    │ │              │
  │                             │  │                                │ │              │
  │                             │  │ 4. UserCreateRepository.create()              │
  │                             │  │    → persist(UserEntity)       │─────────────►│
  │                             │  │                                │ │              │
  │                             │  │ 5. UserRegisteredEventMapper   │ │              │
  │                             │  │    User → UserRegisteredEvent  │ │              │
  │                             │  │                                │ │              │
  │                             │  │ 6. EmailSendRepository         │ │              │
  │                             │  │    → RabbitMQ (templateId: 1)  │──────────────►│
  │                             │  └────────────────────────────────┘ │              │
  │                             │                                     │              │
  │  201 Created                │                                     │              │
  │  {id, username, email,      │                                     │              │
  │   role, active, createdAt}  │                                     │              │
  │◄────────────────────────────│                                     │              │
```

**Validation:**
- `username`: 3-50 chars, alphanumeric + underscore, unique
- `email`: valid email format, unique
- `password`: non-blank (hashed with BCrypt cost 12)

---

### 2. Find User by ID

**`GET /users/{id}`** — Retrieves a user profile. Requires Bearer JWT with `PLAYER` role.

```
Client                     User Service                 Database
  │                             │                            │
  │  GET /users/{id}            │                            │
  │  Authorization: Bearer <jwt>│                            │
  │────────────────────────────►│                            │
  │                             │                            │
  │                             │  ┌──────────────────────┐  │
  │                             │  │ Delegate              │  │
  │                             │  │  └► Service.findById()│  │
  │                             │  └──────────────────────┘  │
  │                             │                            │
  │                             │  Panache: find("id", id)   │
  │                             │───────────────────────────►│
  │                             │  Optional<UserEntity>      │
  │                             │◄───────────────────────────│
  │                             │                            │
  │                             │  ┌──────────────────────┐  │
  │                             │  │ UserMapper:           │  │
  │                             │  │ Entity → Domain       │  │
  │                             │  │ UserViewModelMapper:  │  │
  │                             │  │ Domain → ViewModel    │  │
  │                             │  └──────────────────────┘  │
  │                             │                            │
  │  200 OK                     │                            │
  │  {id, username, email,      │                            │
  │   role, active, createdAt}  │                            │
  │◄────────────────────────────│                            │
  │                             │                            │
  │  (or 404 Not Found)         │                            │
```

---

### 3. Credential Validation (Internal)

**`POST /users/credentials-validation`** — Called by Auth Service during login. Verifies username/password with BCrypt and checks the user's active status.

```
Auth Service                  User Service                   Database
  │                                │                              │
  │  POST /credentials-validation  │                              │
  │  {username, password}          │                              │
  │───────────────────────────────►│                              │
  │                                │                              │
  │                                │  findByUsername(username)     │
  │                                │  (also matches by email)     │
  │                                │─────────────────────────────►│
  │                                │  Optional<UserEntity>        │
  │                                │◄─────────────────────────────│
  │                                │                              │
  │                                │  ┌────────────────────────┐  │
  │                                │  │ CryptUtil.verifyPassword│  │
  │                                │  │ (BCrypt verification)   │  │
  │                                │  │                         │  │
  │                                │  │ If mismatch → 401       │  │
  │                                │  │ If !active  → 403       │  │
  │                                │  └────────────────────────┘  │
  │                                │                              │
  │  200 OK {UserViewModel}        │                              │
  │◄───────────────────────────────│                              │
```

> The `findByUsername` query searches both the `username` and `email` columns, allowing login with either.

---

### 4. Find by Email (Internal)

**`POST /users/email-lookup`** — Called by Auth Service for OTP login and password reset flows.

```
Auth Service                   User Service           Database
  │                                │                      │
  │  POST /users/email-lookup      │                      │
  │  {email}                       │                      │
  │───────────────────────────────►│                      │
  │                                │  find("email", email)│
  │                                │─────────────────────►│
  │                                │  Optional<User>      │
  │                                │◄─────────────────────│
  │  200 OK or 404 Not Found      │                      │
  │◄───────────────────────────────│                      │
```

---

### 5. Update Password (Internal)

**`PUT /users/{id}/password`** — Called by Auth Service after password reset token validation.

```
Auth Service                   User Service               Database
  │                                │                           │
  │  PUT /users/{id}/password      │                           │
  │  {newPassword}                 │                           │
  │───────────────────────────────►│                           │
  │                                │  CryptUtil.hashPassword() │
  │                                │  (BCrypt, cost 12)        │
  │                                │                           │
  │                                │  UPDATE passwordHash,     │
  │                                │         updatedAt         │
  │                                │  WHERE id=X               │
  │                                │──────────────────────────►│
  │                                │◄──────────────────────────│
  │  204 No Content                │                           │
  │◄───────────────────────────────│                           │
```

---

## Design Patterns

### Hexagonal Architecture (Ports & Adapters)

```
                    ┌───────────────────────────────┐
   Driving Ports    │         DOMAIN CORE            │   Driven Ports
  ──────────────────│                                 │──────────────────
                    │  UserRegisterService            │
  ResourceImpl ────►│  UserFindByIdService            │────► UserCreateRepository (JPA)
  Delegate          │  UserFindByEmailService         │────► UserValidationRepository (JPA)
  Mapper            │  UserCredentialsValidateService │────► UserFindByIdRepository (JPA)
                    │  UserUpdatePasswordService      │────► EmailSendRepository (RabbitMQ)
                    └───────────────────────────────┘
```

### Delegate Pattern

ResourceImpl handles HTTP annotations; the `@Delegate` class handles logic:

```java
// ResourceImpl — HTTP concerns only
@POST @Path("/users")
public Response register(@Valid UserRegisterViewModel vm) {
    return delegate.register(vm);  // injected via @Delegate
}

// Delegate — business logic
@Delegate @RequestScoped
public class UserRegisterDelegate {
    // Maps ViewModel → Domain → calls Service → maps Domain → ViewModel
}
```

### Event-Driven Pattern (Domain Events)

User registration emits a `UserRegisteredEvent` that triggers a welcome email:

```
UserRegisterServiceImpl
  └► UserRegisteredEventMapper: User → UserRegisteredEvent(userId, email, username)
      └► EmailSendRepository → RabbitMQ (channel: "email-requests-out", templateId: 1)
```

### Mapper Chain (Boundary Transformations)

```
UserRegisterViewModel ──[UserRegisterMapper]──► UserRegister (domain)
                                                      │
                                               UserRegisterServiceImpl
                                                      │
                                                      ▼
            UserViewModel ◄──[UserViewModelMapper]── User (domain)
                                                      │
                   User ◄──[UserMapper]── UserEntity (JPA)
```

---

## Security Details

| Mechanism | Implementation |
|-----------|---------------|
| **Password Hashing** | BCrypt with cost factor 12 (`at.favre.lib.crypto.bcrypt`) |
| **Registration** | Public (no auth required) |
| **Profile Lookup** | Bearer JWT, `PLAYER` role |
| **Internal Endpoints** | `@PermitAll` — trusted backend network only |
| **Uniqueness** | Username and email enforced at DB level (unique constraint) + app-level check |

---

## Event-Driven Communication

| Event | Channel | Template | Trigger |
|-------|---------|----------|---------|
| Welcome Email | `email-requests-out` (RabbitMQ) | ID `1` | User registration |

The `EmailSendRepositoryRabbitMq` publishes an `EmailSendRequestViewModel` with the user's email and template ID to the notification service.

---

## Data Model

```
┌──────────────────────────────┐
│           users              │
├──────────────────────────────┤
│ id            BIGSERIAL PK   │
│ username      VARCHAR(50) UQ │
│ email         VARCHAR(255) UQ│
│ password_hash VARCHAR(255)   │
│ role          VARCHAR(20)    │  ← default: "PLAYER"
│ active        BOOLEAN        │  ← default: true
│ created_at    TIMESTAMP      │
│ updated_at    TIMESTAMP      │
└──────────────────────────────┘
```
