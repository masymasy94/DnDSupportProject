# Notification Service

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.31-blue?style=flat-square&logo=quarkus)](https://quarkus.io/)
[![Port](https://img.shields.io/badge/Port-8088-green?style=flat-square)]()

The **Notification Service** handles email delivery for the DnD Platform. It supports both **synchronous** (REST API) and **asynchronous** (RabbitMQ consumer) email sending, using a **template-based** system with variable interpolation. Email templates are stored in the database and rendered at send time by replacing `{{variable}}` placeholders with actual values.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Operative Flows](#operative-flows)
  - [Async Email (RabbitMQ Consumer)](#1-async-email-sending-rabbitmq-consumer)
  - [Sync Email (REST)](#2-sync-email-sending-rest)
  - [Create Email Template](#3-create-email-template)
  - [List Email Templates](#4-list-email-templates)
- [Design Patterns](#design-patterns)
- [Template System](#template-system)
- [RabbitMQ Integration](#rabbitmq-integration)
- [Data Model](#data-model)

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────────────────┐
│                    Notification Service (:8088)                          │
│                                                                          │
│  ┌─────────────────────┐   ┌───────────────────┐   ┌─────────────────┐  │
│  │  Adapter-Inbound    │   │      Domain        │   │ Adapter-Outbound│  │
│  │                     │   │                    │   │                 │  │
│  │ REST Resources      │   │ SendEmailService   │   │ SMTP Repository │  │
│  │ Delegates           │──►│ Template Services  │──►│ JPA Repository  │  │
│  │                     │   │                    │   │ (templates)     │  │
│  │ RabbitMQ Consumer   │   │ Template Rendering │   │                 │  │
│  │ (email-requests-in) │──►│ (variable interp.) │   │                 │  │
│  └─────────────────────┘   └───────────────────┘   └────────┬────────┘  │
│                                                              │           │
└──────────────────────────────────────────────────────────────┼───────────┘
                                                                │
                          ┌─────────────────────────────────────┼────────┐
                          │                                     │        │
                          ▼                                     ▼        │
                   ┌────────────┐                        ┌────────────┐  │
                   │ PostgreSQL │                        │    SMTP    │  │
                   │(notif_db)  │                        │  (Mailtrap │  │
                   │(templates) │                        │   / SES)   │  │
                   └────────────┘                        └────────────┘  │
                                                                         │
  Auth Service ─── RabbitMQ ("email-requests-out") ─────────────────────►│
  User Service ─── RabbitMQ ("email-requests-out") ─────────────────────►│
```

---

## Module Structure

```
notification-service/
├── notification-service-domain/        # Business logic & domain models
│   ├── model/                          # Records: SendEmailRequest, EmailTemplate, etc.
│   ├── SendEmailService                # Core email sending logic
│   ├── CreateEmailTemplateService      # Template creation
│   ├── GetAllEmailTemplatesService     # Template listing
│   └── repository/                     # Port interfaces (SMTP, JPA)
│
├── notification-service-view-model/    # DTOs & Resource interfaces
│   └── vm/                             # EmailSendRequestViewModel, etc.
│
├── notification-service-adapter-inbound/# Driving Adapters
│   ├── send/                           # POST /emails (sync send)
│   ├── template/                       # POST/GET /email-templates
│   └── messaging/                      # RabbitMQ consumer (async)
│
├── notification-service-adapter-outbound/# Driven Adapters
│   ├── smtp/                           # Quarkus Mailer SMTP sender
│   └── jpa/                            # Panache template repository
│
├── notification-service-client/        # REST client interface
└── notification-service/               # Quarkus bootstrap & configuration
```

---

## API Endpoints

### REST

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/emails` | Public | Send email synchronously |
| `POST` | `/email-templates` | Public | Create an email template |
| `GET` | `/email-templates` | Public | List all email templates |

### Messaging

| Channel | Direction | Description |
|---------|-----------|-------------|
| `email-requests-in` | Inbound (RabbitMQ) | Async email send consumer |

---

## Operative Flows

### 1. Async Email Sending (RabbitMQ Consumer)

The primary email sending path. Other services (Auth, User) publish email requests to RabbitMQ; the Notification Service consumes and processes them.

```
Auth Service                   RabbitMQ                   Notification Service           SMTP         Database
  │                                │                              │                        │              │
  │  Publish email request         │                              │                        │              │
  │  (channel: email-requests-out) │                              │                        │              │
  │  {to, templateId,             │                              │                        │              │
  │   variables: {otpCode:"123"}} │                              │                        │              │
  │───────────────────────────────►│                              │                        │              │
  │                                │                              │                        │              │
  │                                │  ┌────────────────────────┐  │                        │              │
  │                                │  │ EmailSendRequestConsumer│  │                        │              │
  │                                │  │ @Incoming("email-       │  │                        │              │
  │                                │  │   requests-in")         │  │                        │              │
  │                                │  └────────────────────────┘  │                        │              │
  │                                │                              │                        │              │
  │                                │  Deliver message             │                        │              │
  │                                │─────────────────────────────►│                        │              │
  │                                │                              │                        │              │
  │                                │                              │  ┌──────────────────┐  │              │
  │                                │                              │  │ ConsumerDelegate  │  │              │
  │                                │                              │  │                  │  │              │
  │                                │                              │  │ 1. Deserialize   │  │              │
  │                                │                              │  │    message        │  │              │
  │                                │                              │  │ 2. Map to domain │  │              │
  │                                │                              │  │    SendEmailReq   │  │              │
  │                                │                              │  │ 3. Call service   │  │              │
  │                                │                              │  └──────────────────┘  │              │
  │                                │                              │                        │              │
  │                                │                              │  ┌──────────────────┐  │              │
  │                                │                              │  │ SendEmailService  │  │              │
  │                                │                              │  │                  │  │              │
  │                                │                              │  │ 1. Load template  │  │              │
  │                                │                              │  │    by templateId  │──────────────►│
  │                                │                              │  │                  │  │   Template   │
  │                                │                              │  │                  │◄──────────────│
  │                                │                              │  │                  │  │              │
  │                                │                              │  │ 2. Render:        │  │              │
  │                                │                              │  │  subject → replace│  │              │
  │                                │                              │  │   {{otpCode}}     │  │              │
  │                                │                              │  │  body → replace   │  │              │
  │                                │                              │  │   {{otpCode}}     │  │              │
  │                                │                              │  │                  │  │              │
  │                                │                              │  │ 3. Send via SMTP  │  │              │
  │                                │                              │  │    (Quarkus Mailer)  │              │
  │                                │                              │  └──────────────────┘  │              │
  │                                │                              │                        │              │
  │                                │                              │  Send email             │              │
  │                                │                              │───────────────────────►│              │
  │                                │                              │                        │              │
  │                                │                              │  4. Acknowledge msg    │              │
  │                                │  ACK                         │                        │              │
  │                                │◄─────────────────────────────│                        │              │
```

**Message acknowledgment**: The consumer explicitly acknowledges the RabbitMQ message only after successful email delivery. If sending fails, the message is negatively acknowledged (nack) for redelivery.

---

### 2. Sync Email Sending (REST)

**`POST /emails`** — Direct email send via REST (used for testing or manual sends).

```
Client                    Notification Service              Database        SMTP
  │                            │                               │              │
  │  POST /emails              │                               │              │
  │  {to, templateId,          │                               │              │
  │   variables: {...}}        │                               │              │
  │───────────────────────────►│                               │              │
  │                            │                               │              │
  │                            │  ┌──────────────────────────┐ │              │
  │                            │  │ SyncSendEmailDelegate     │ │              │
  │                            │  │  @RunOnVirtualThread       │ │              │
  │                            │  │                            │ │              │
  │                            │  │  1. Map ViewModel→domain   │ │              │
  │                            │  │  2. Call SendEmailService  │ │              │
  │                            │  └──────────────────────────┘ │              │
  │                            │                               │              │
  │                            │  ┌──────────────────────────┐ │              │
  │                            │  │ SendEmailServiceImpl      │ │              │
  │                            │  │                            │ │              │
  │                            │  │ 1. Load template by ID     │              │
  │                            │  │────────────────────────────►│              │
  │                            │  │    EmailTemplate            │              │
  │                            │  │◄────────────────────────────│              │
  │                            │  │                            │ │              │
  │                            │  │ 2. Render subject + body   │ │              │
  │                            │  │    (variable interpolation)│ │              │
  │                            │  │                            │ │              │
  │                            │  │ 3. SmtpEmailSendRepository │ │              │
  │                            │  │    .send(to, subject, body)│ │              │
  │                            │  │    (Quarkus Mailer)        │ │              │
  │                            │  └──────────────────────────┘ │              │
  │                            │                               │              │
  │                            │  Send email                   │              │
  │                            │──────────────────────────────────────────────►│
  │                            │                               │              │
  │  200 OK                    │                               │              │
  │◄───────────────────────────│                               │              │
```

---

### 3. Create Email Template

**`POST /email-templates`** — Creates a reusable email template with `{{variable}}` placeholders.

```
Client                    Notification Service              Database
  │                            │                               │
  │  POST /email-templates     │                               │
  │  {name: "OTP Login",       │                               │
  │   subject: "Your code:     │                               │
  │     {{otpCode}}",          │                               │
  │   body: "<h1>Code:         │                               │
  │     {{otpCode}}</h1>"}     │                               │
  │───────────────────────────►│                               │
  │                            │                               │
  │                            │  ┌──────────────────────────┐ │
  │                            │  │ CreateEmailTemplateDelegate│ │
  │                            │  │  Map to domain record     │ │
  │                            │  │  Call service.create()    │ │
  │                            │  └──────────────────────────┘ │
  │                            │                               │
  │                            │  INSERT email_templates       │
  │                            │──────────────────────────────►│
  │                            │  EmailTemplateEntity          │
  │                            │◄──────────────────────────────│
  │                            │                               │
  │  201 Created               │                               │
  │  {id, name, subject, body} │                               │
  │◄───────────────────────────│                               │
```

---

### 4. List Email Templates

**`GET /email-templates`** — Returns all stored templates.

```
Client                    Notification Service              Database
  │                            │                               │
  │  GET /email-templates      │                               │
  │───────────────────────────►│                               │
  │                            │  findAll()                    │
  │                            │──────────────────────────────►│
  │                            │  List<EmailTemplateEntity>    │
  │                            │◄──────────────────────────────│
  │                            │                               │
  │  200 OK                    │                               │
  │  [{id, name, subject,      │                               │
  │    body}, ...]             │                               │
  │◄───────────────────────────│                               │
```

---

## Design Patterns

### Hexagonal Architecture (Ports & Adapters)

```
                     ┌───────────────────────────────────┐
    Driving Ports    │         DOMAIN CORE                │   Driven Ports
  ───────────────────│                                     │──────────────────
                     │  SendEmailService                   │
  REST Resources ───►│   └ Template rendering              │────► SmtpEmailSendRepository
  RabbitMQ Consumer ►│   └ Variable interpolation          │       (Quarkus Mailer)
  Delegates          │                                     │
                     │  CreateEmailTemplateService         │────► EmailTemplateRepository
                     │  GetAllEmailTemplatesService        │       (JPA/Panache)
                     └───────────────────────────────────┘
```

### Delegate Pattern

REST and messaging endpoints both use delegates:
- **REST**: `SyncSendEmailDelegate` handles the sync path
- **Messaging**: `EmailSendRequestConsumerDelegate` handles the async path

Both delegates ultimately call the same `SendEmailService`, ensuring consistent template rendering and email delivery logic.

### Template Method Pattern (Email Rendering)

The `SendEmailService` follows a fixed rendering algorithm:
1. Load template by ID from repository
2. Replace `{{variable}}` placeholders in subject
3. Replace `{{variable}}` placeholders in body
4. Send via SMTP

The template content varies (different templates for welcome, OTP, password reset) but the rendering process is always the same.

### Consumer-Acknowledger Pattern (Reliable Messaging)

The RabbitMQ consumer uses explicit message acknowledgment:

```
Message received
    │
    ├── Send email SUCCESS → ack() → message removed from queue
    │
    └── Send email FAILURE → nack() → message redelivered
```

This ensures no email requests are lost — messages stay in the queue until successfully processed.

---

## Template System

### Template Structure

Templates are stored in the `email_templates` table with `{{variable}}` placeholders:

| Field | Example |
|-------|---------|
| `name` | "OTP Login Code" |
| `subject` | "Your login code: {{otpCode}}" |
| `body` | `<h1>Your OTP code is: {{otpCode}}</h1>` |

### Variable Interpolation

At send time, all `{{key}}` placeholders in both `subject` and `body` are replaced with values from the `variables` map:

```
Input:
  template.subject = "Your code: {{otpCode}}"
  variables = { "otpCode": "482901" }

Output:
  renderedSubject = "Your code: 482901"
```

### Pre-Configured Templates

| ID | Name | Variables | Used By |
|----|------|-----------|---------|
| 1 | Welcome | (none) | User Service (registration) |
| 2 | Password Reset | `{{otpCode}}` | Auth Service (password reset) |
| 3 | OTP Login | `{{otpCode}}` | Auth Service (OTP login) |

---

## RabbitMQ Integration

| Property | Value |
|----------|-------|
| **Inbound Channel** | `email-requests-in` |
| **Exchange** | (configured via application.properties) |
| **Deserialization** | Custom `EmailSendRequestMessageConverter` |
| **Acknowledgment** | Manual (ack on success, nack on failure) |
| **Publishers** | Auth Service, User Service (channel: `email-requests-out`) |

### Message Format

```json
{
  "to": "user@example.com",
  "templateId": 3,
  "variables": {
    "otpCode": "482901"
  }
}
```

---

## Data Model

```
┌──────────────────────────────┐
│       email_templates        │
├──────────────────────────────┤
│ id          BIGSERIAL PK     │
│ name        VARCHAR          │
│ subject     VARCHAR          │  ← supports {{variable}} placeholders
│ body        TEXT             │  ← supports {{variable}} placeholders
│ created_at  TIMESTAMP        │
│ updated_at  TIMESTAMP        │
└──────────────────────────────┘
```
