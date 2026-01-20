# DnD Platform

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.30-blue?style=for-the-badge&logo=quarkus&logoColor=white)](https://quarkus.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=for-the-badge&logo=docker&logoColor=white)](https://docs.docker.com/compose/)

A **production-ready microservices platform** for Dungeons & Dragons campaign management, built with modern cloud-native technologies. This project demonstrates enterprise-grade architecture patterns, security best practices, and full observability in a distributed system.

> **Portfolio Project** — This project showcases my expertise in designing and implementing scalable microservices architectures using Java 21, Quarkus, and cloud-native tooling.

---

## Table of Contents

- [About The Project](#about-the-project)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Microservices](#microservices)
- [API Documentation](#api-documentation)
- [Getting Started](#getting-started)
- [Usage Examples](#usage-examples)
- [Project Structure](#project-structure)
- [Monitoring & Observability](#monitoring--observability)
- [Roadmap](#roadmap)
- [License](#license)

---

## About The Project

DnD Platform is a comprehensive backend system designed to support Dungeons & Dragons gameplay and campaign management. It provides RESTful APIs for:

- **User Management** — Registration, authentication, and profile management
- **Character Management** — Create and manage D&D character sheets
- **Campaign Management** — Organize campaigns, sessions, and player groups
- **Compendium** — Reference data including monsters, spells, classes, and races
- **Combat Simulation** — Initiative tracking, damage calculation, and encounter management
- **Real-time Chat** — In-game messaging with WebSocket support
- **Asset Management** — Upload and manage character art, maps, and tokens
- **Search** — Full-text search across campaigns, characters, and monsters
- **Notifications** — Event-driven alerts and email notifications

### Key Features

- **Microservices Architecture** — 10 independent, loosely-coupled services
- **Hexagonal Architecture** — Clean separation between domain logic and infrastructure
- **JWT Authentication** — Secure token-based authentication with refresh token rotation
- **Role-Based Access Control** — Fine-grained authorization with role permissions
- **API Gateway** — Traefik for intelligent routing and service discovery
- **Caching Layer** — Redis caching for frequently accessed reference data
- **Event-Driven Communication** — RabbitMQ for asynchronous service messaging
- **Full Observability** — Prometheus metrics, Grafana dashboards, Jaeger tracing, Loki logs
- **Secret Management** — HashiCorp Vault for secure credential storage
- **Database Per Service** — Isolated PostgreSQL databases for data sovereignty

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              API Gateway (Traefik)                          │
│                                   :80/:443                                  │
└─────────────────────────────────────┬───────────────────────────────────────┘
                                      │
        ┌─────────────────────────────┼─────────────────────────────┐
        │                             │                             │
        ▼                             ▼                             ▼
┌───────────────┐           ┌───────────────┐           ┌───────────────┐
│  Auth Service │           │  User Service │           │   Compendium  │
│     :8081     │◄─────────►│     :8089     │           │    :8090      │
└───────┬───────┘           └───────────────┘           └───────┬───────┘
        │                                                       │
        │  JWT Validation                                       │ Cache
        ▼                                                       ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│   Character   │  │   Campaign    │  │    Combat     │  │     Redis     │
│    :8082      │  │    :8083      │  │    :8084      │  │     :6379     │
└───────────────┘  └───────────────┘  └───────────────┘  └───────────────┘
        │                   │                  │
        └───────────────────┼──────────────────┘
                            ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│     Asset     │  │     Chat      │  │ Notification  │  │    Search     │
│    :8085      │  │    :8086      │  │    :8088      │  │    :8087      │
└───────┬───────┘  └───────┬───────┘  └───────┬───────┘  └───────┬───────┘
        │                  │                  │                  │
        ▼                  ▼                  ▼                  ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│     MinIO     │  │   RabbitMQ    │  │     SMTP      │  │ Elasticsearch │
│  (S3 Storage) │  │   (Messaging) │  │   (Mailer)    │  │   (Search)    │
└───────────────┘  └───────────────┘  └───────────────┘  └───────────────┘

                            ┌───────────────┐
                            │  PostgreSQL   │
                            │   (9 DBs)     │
                            └───────────────┘
```

### Service Communication

| Pattern | Use Case | Technology |
|---------|----------|------------|
| **Synchronous** | Request/Response APIs | REST (Quarkus REST Client) |
| **Asynchronous** | Event Broadcasting | RabbitMQ |
| **Caching** | Reference Data | Redis |
| **Search** | Full-text Queries | Elasticsearch |

---

## Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Language (LTS with virtual threads) |
| Quarkus | 3.30.5 | Cloud-native Java framework |
| Maven | 3.9+ | Build automation |
| Hibernate ORM | 6.x | Object-relational mapping |
| SmallRye JWT | - | JWT token handling |
| MapStruct | 1.5+ | DTO mapping |

### Data Layer
| Technology | Version | Purpose |
|------------|---------|---------|
| PostgreSQL | 16 | Primary database |
| Redis | 7 | Caching layer |
| Elasticsearch | 8.11 | Full-text search |
| Flyway | - | Database migrations |

### Infrastructure
| Technology | Version | Purpose |
|------------|---------|---------|
| Docker | 24+ | Containerization |
| Docker Compose | 2.x | Container orchestration |
| Traefik | 3.0 | API Gateway / Load Balancer |
| HashiCorp Vault | 1.15 | Secrets management |
| MinIO | - | S3-compatible object storage |
| RabbitMQ | 3.13 | Message broker |

### Observability
| Technology | Purpose |
|------------|---------|
| Prometheus | Metrics collection |
| Grafana | Dashboards & visualization |
| Jaeger | Distributed tracing |
| Loki + Promtail | Log aggregation |
| Portainer | Container management UI |

---

## Microservices

| Service | Port | Description | Key Endpoints |
|---------|------|-------------|---------------|
| **auth-service** | 8081 | Authentication & JWT management | Login, logout, token refresh |
| **user-service** | 8089 | User registration & profiles | Register, get user, validate credentials |
| **character-service** | 8082 | D&D character management | Character CRUD, summaries |
| **campaign-service** | 8083 | Campaign & session management | Campaign CRUD, player management |
| **combat-service** | 8084 | Combat encounter simulation | Initiative, damage, turns |
| **compendium-service** | 8090 | D&D reference data (SRD) | Monsters, spells, classes, races |
| **asset-service** | 8085 | File/media management | Upload, retrieve assets |
| **chat-service** | 8086 | Real-time messaging | WebSocket chat, message history |
| **notification-service** | 8088 | Event notifications | Alerts, email notifications |
| **search-service** | 8087 | Full-text search | Search across entities |

### Hexagonal Architecture

Each microservice follows a **hexagonal (ports & adapters) architecture** with a multi-module Maven structure:

```
service-name/
├── service-name-domain/           # Business logic, domain models, services
├── service-name-view-model/       # DTOs, API request/response models
├── service-name-adapter-inbound/  # REST controllers (driving adapters)
├── service-name-adapter-outbound/ # Repositories, external clients (driven adapters)
├── service-name-client/           # REST client for inter-service calls
└── service-name/                  # Main application, Quarkus bootstrap
```

---

## API Documentation

### Authentication Flow

#### 1. Register a New User
```bash
POST /users
Host: localhost:8089
Content-Type: application/json

{
  "username": "player1",
  "email": "player1@example.com",
  "password": "SecureP@ss123"
}
```

> **Password Requirements:** Must contain uppercase, lowercase, digit, and special character (`@$!%*?&`)

#### 2. Login (Get JWT Token)
```bash
POST /auth/login-tokens
Host: localhost:8081
Content-Type: application/json

{
  "username": "player1",
  "password": "SecureP@ss123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4..."
}
```

#### 3. Access Protected Resources
```bash
GET /api/compendium/monsters
Host: localhost:8090
Authorization: Bearer <accessToken>
```

#### 4. Refresh Token
```bash
POST /auth/login-tokens/refreshed
Host: localhost:8081
Content-Type: application/json

{
  "refreshToken": "<refreshToken>"
}
```

#### 5. Logout
```bash
# Logout single session
DELETE /auth/login-tokens/{refreshToken}
Host: localhost:8081
Authorization: Bearer <accessToken>

# Logout all sessions
DELETE /auth/login-tokens
Host: localhost:8081
Authorization: Bearer <accessToken>
```

---

### Compendium Service API

The Compendium Service provides D&D 5e reference data with filtering and pagination.

#### Get Monsters
```bash
GET /api/compendium/monsters
Host: localhost:8090
Authorization: Bearer <accessToken>
```

**Query Parameters:**
| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `name` | string | Partial name match (case-insensitive) | `dragon` |
| `type` | string | Monster type | `Dragon`, `Undead`, `Beast` |
| `size` | string | Monster size | `Tiny`, `Medium`, `Huge` |
| `challengeRating` | string | CR value | `1/4`, `1`, `17` |
| `alignment` | string | Alignment | `chaotic evil` |
| `page` | int | Page number (0-indexed) | `0` |
| `pageSize` | int | Items per page | `20` |

**Example Requests:**
```bash
# Get all dragons
GET /api/compendium/monsters?type=Dragon

# Search by name
GET /api/compendium/monsters?name=vampire

# Filter by challenge rating
GET /api/compendium/monsters?challengeRating=17

# Combined filters with pagination
GET /api/compendium/monsters?type=Undead&size=Medium&page=0&pageSize=10
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Adult Red Dragon",
      "size": "Huge",
      "type": "Dragon",
      "alignment": "chaotic evil",
      "challengeRating": "17",
      "armorClass": 19,
      "hitPoints": 256,
      "speed": "40 ft., climb 40 ft., fly 80 ft.",
      "strength": 27,
      "dexterity": 10,
      "constitution": 25,
      "intelligence": 16,
      "wisdom": 13,
      "charisma": 21
    }
  ],
  "page": 0,
  "size": 50,
  "totalElements": 36,
  "totalPages": 1
}
```

#### Get Spells
```bash
GET /api/compendium/spells
Host: localhost:8090
Authorization: Bearer <accessToken>
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `level` | int | Spell level (0-9, where 0 = cantrip) |
| `school` | string | Magic school (Evocation, Necromancy, etc.) |
| `concentration` | boolean | Requires concentration |
| `ritual` | boolean | Can be cast as ritual |

#### Get Reference Data
```bash
# Character Classes
GET /api/compendium/character-classes

# Playable Races/Species
GET /api/compendium/species

# Alignments
GET /api/compendium/alignments

# Abilities
GET /api/compendium/abilities
```

---

### Character Service API

```bash
# Get characters (paginated)
GET /characters?page=0&size=20
Host: localhost:8082
Authorization: Bearer <accessToken>
```

**Response:**
```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Thorin Stoneshield",
      "species": "Dwarf",
      "characterClass": "Fighter",
      "level": 5,
      "currentHitPoints": 45,
      "maxHitPoints": 52,
      "armorClass": 18
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 5,
  "totalPages": 1
}
```

---

## Getting Started

### Prerequisites

- **Java 21** — [Download OpenJDK](https://adoptium.net/)
- **Maven 3.9+** — [Download Maven](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose** — [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)

Verify installations:
```bash
java --version    # Should show 21+
mvn --version     # Should show 3.9+
docker --version  # Should show 24+
```

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/masymasy94/DnDSupportProject.git
   cd dnd-platform
   ```

2. **Create environment file**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Start infrastructure services**
   ```bash
   docker-compose up -d postgres redis rabbitmq vault elasticsearch minio
   ```

4. **Build all services**
   ```bash
   mvn clean install -DskipTests
   ```

5. **Run the platform**

   Option A — Using the startup script:
   ```bash
   ./start.sh
   ```

   Option B — Using Docker Compose:
   ```bash
   docker-compose up -d
   ```

6. **Verify services are running**
   ```bash
   # Check service health
   curl http://localhost:8081/q/health
   curl http://localhost:8089/q/health
   curl http://localhost:8090/q/health
   ```

### Service URLs

| Service | URL |
|---------|-----|
| Auth Service | http://localhost:8081 |
| User Service | http://localhost:8089 |
| Compendium Service | http://localhost:8090 |
| Character Service | http://localhost:8082 |
| Grafana Dashboard | http://localhost:3000 |
| Jaeger Tracing | http://localhost:16686 |
| RabbitMQ Management | http://localhost:15672 |
| Portainer | http://localhost:9000 |

---

## Usage Examples

### Complete Authentication Flow

```bash
# 1. Register a new user
curl -X POST 'http://localhost:8089/users' \
  -H 'Content-Type: application/json' \
  -d '{"username":"hero1","email":"hero1@example.com","password":"Dragon$layer1"}'

# 2. Login and get token
TOKEN=$(curl -s -X POST 'http://localhost:8081/auth/login-tokens' \
  -H 'Content-Type: application/json' \
  -d '{"username":"hero1","password":"Dragon$layer1"}' | jq -r '.accessToken')

# 3. Access protected API
curl -s 'http://localhost:8090/api/compendium/monsters?type=Dragon' \
  -H "Authorization: Bearer $TOKEN" | jq

# 4. Search for high-CR monsters
curl -s 'http://localhost:8090/api/compendium/monsters?challengeRating=17' \
  -H "Authorization: Bearer $TOKEN" | jq '.content[].name'
```

### Query Spell Database

```bash
# Get all 3rd level evocation spells
curl -s 'http://localhost:8090/api/compendium/spells?level=3&school=Evocation' \
  -H "Authorization: Bearer $TOKEN" | jq

# Get concentration spells
curl -s 'http://localhost:8090/api/compendium/spells?concentration=true' \
  -H "Authorization: Bearer $TOKEN" | jq
```

---

## Project Structure

```
dnd-platform/
├── services/
│   ├── common/                     # Shared library (annotations, utilities)
│   ├── auth-service/               # Authentication microservice
│   ├── user-service/               # User management microservice
│   ├── character-service/          # Character management microservice
│   ├── campaign-service/           # Campaign management microservice
│   ├── combat-service/             # Combat simulation microservice
│   ├── compendium-service/         # D&D reference data microservice
│   ├── asset-service/              # File management microservice
│   ├── chat-service/               # Real-time chat microservice
│   ├── notification-service/       # Notification microservice
│   └── search-service/             # Search microservice
├── infrastructure/
│   ├── postgres/                   # Database initialization scripts
│   ├── vault/                      # Vault configuration
│   └── monitoring/                 # Prometheus, Grafana, Loki configs
├── docker-compose.yml              # Container orchestration
├── start.sh                        # Startup automation script
└── README.md
```

---

## Monitoring & Observability

### Metrics (Prometheus + Grafana)

Access Grafana at `http://localhost:3000` (default: admin/admin)

Pre-configured dashboards include:
- JVM metrics (heap, GC, threads)
- HTTP request rates and latencies
- Database connection pools
- Custom business metrics

### Distributed Tracing (Jaeger)

Access Jaeger UI at `http://localhost:16686`

Trace requests across all microservices to identify bottlenecks and debug issues.

### Centralized Logging (Loki)

All services emit JSON-structured logs collected by Promtail and queryable via Grafana.

```bash
# Example log query in Grafana
{service="compendium-service"} |= "ERROR"
```

---

## Roadmap

- [x] User authentication with JWT
- [x] Compendium service with monsters and spells
- [x] Redis caching for reference data
- [x] Full observability stack
- [ ] Character creation wizard
- [ ] Campaign session management
- [ ] Real-time combat tracker
- [ ] WebSocket-based chat
- [ ] Mobile-friendly API responses
- [ ] GraphQL API layer

---

## Technical Highlights

This project demonstrates proficiency in:

| Skill | Implementation |
|-------|----------------|
| **Microservices Architecture** | 10 independent services with clear boundaries |
| **Domain-Driven Design** | Hexagonal architecture with domain isolation |
| **API Design** | RESTful APIs with proper pagination and filtering |
| **Security** | JWT authentication, RBAC, Vault secrets management |
| **Database Design** | Database-per-service pattern, Flyway migrations |
| **Caching Strategies** | Redis caching with TTL for reference data |
| **Event-Driven Architecture** | RabbitMQ for async communication |
| **Observability** | Metrics, tracing, and centralized logging |
| **Containerization** | Docker Compose orchestration, health checks |
| **Modern Java** | Java 21 features, Quarkus framework |

---

## License

Distributed under the MIT License. See `LICENSE` for more information.

---

## Contact

**Your Name** — [your.email@example.com](mailto:your.email@example.com)

Project Link: [https://github.com/masymasy94/DnDSupportProject](https://github.com/masymasy94/DnDSupportProject)

---

<p align="center">
  <i>Built with passion for D&D and clean architecture</i>
</p>
