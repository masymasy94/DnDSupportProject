# Auth Service

Authentication microservice for the DnD Platform, providing JWT-based authentication with access and refresh token management.

## Overview

The Auth Service handles user authentication by validating credentials against the User Service and issuing JWT tokens. It supports token refresh functionality and session management through revocable refresh tokens.

### Key Features

- **JWT Authentication**: Issues access tokens and refresh tokens
- **Token Refresh**: Secure token rotation with refresh tokens
- **Session Management**: Single session logout and logout from all devices
- **Secure Secret Management**: Integration with HashiCorp Vault

## Architecture

This service follows a hexagonal (ports and adapters) architecture with the following modules:

| Module | Description |
|--------|-------------|
| `auth-service-domain` | Core business logic and domain models |
| `auth-service-view-model` | API contracts and view models |
| `auth-service-adapter-inbound` | REST API controllers |
| `auth-service-adapter-outbound` | External integrations (database, REST clients) |
| `auth-service-client` | Client library for other services |
| `auth-service` | Main application module |

## Tech Stack

- **Framework**: Quarkus 3.30.5
- **Language**: Java 21
- **Database**: PostgreSQL
- **Caching**: Redis
- **Messaging**: RabbitMQ
- **Secrets**: HashiCorp Vault
- **Migrations**: Flyway
- **Mapping**: MapStruct
- **Observability**: OpenTelemetry, Jaeger, Prometheus

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/login-tokens` | Create login tokens (authenticate) |
| `POST` | `/auth/login-tokens/refreshed` | Refresh access token |
| `DELETE` | `/auth/login-tokens/{refreshToken}?userId={userId}` | Logout (revoke single token) |
| `DELETE` | `/auth/login-tokens?userId={userId}` | Logout all sessions |

### Request/Response Examples

#### Login
```bash
POST /auth/login-tokens
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

**Response (200 OK)**:
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...",
  "accessTokenExpiresAt": 1704067200,
  "refreshTokenExpiresAt": 1706659200,
  "tokenType": "Bearer"
}
```

#### Refresh Token
```bash
POST /auth/login-tokens/refreshed
Content-Type: application/json

{
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4..."
}
```

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose (for local development)
- Running instances of:
  - PostgreSQL
  - Redis
  - RabbitMQ
  - HashiCorp Vault
  - User Service

### Local Development

1. **Start infrastructure services**:
   ```bash
   docker-compose up -d postgres redis rabbitmq vault
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run in development mode**:
   ```bash
   cd auth-service
   mvn quarkus:dev
   ```

The service starts on `http://localhost:8081`.

### Configuration

Key configuration properties (configured via Vault in production):

| Property | Description |
|----------|-------------|
| `jwt.issuer` | JWT token issuer |
| `jwt.access-token-expiry-seconds` | Access token expiration time |
| `jwt.refresh-token-expiry-days` | Refresh token expiration time |
| `db.username` / `db.password` | Database credentials |
| `redis.hosts` | Redis connection string |

## API Documentation

Once running, API documentation is available at:

- **Swagger UI**: http://localhost:8081/q/swagger-ui
- **OpenAPI Spec**: http://localhost:8081/openapi

## Health & Metrics

- **Health Check**: http://localhost:8081/q/health
- **Metrics (Prometheus)**: http://localhost:8081/q/metrics

## Building for Production

### Build JAR
```bash
mvn clean package -DskipTests
```

### Build Native Image
```bash
mvn clean package -Pnative -DskipTests
```

### Build Docker Image
```bash
mvn clean package -Dquarkus.container-image.build=true
```

## Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify -DskipITs=false
```

## Project Structure

```
auth-service/
├── auth-service-domain/          # Business logic
│   └── src/main/java/
│       └── com/dndplatform/auth/domain/
│           ├── model/            # Domain models
│           ├── repository/       # Repository interfaces
│           └── impl/             # Service implementations
├── auth-service-view-model/      # API contracts
├── auth-service-adapter-inbound/ # REST controllers
├── auth-service-adapter-outbound/# External adapters
├── auth-service-client/          # Client library
└── auth-service/                 # Main application
    └── src/main/resources/
        ├── application.properties
        └── db/migration/         # Flyway migrations
```

## Related Services

- **User Service**: Provides user credential validation
