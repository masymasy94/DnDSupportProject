# DnD Platform

[![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.30-blue?style=for-the-badge&logo=quarkus&logoColor=white)](https://quarkus.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=for-the-badge&logo=docker&logoColor=white)](https://docs.docker.com/compose/)

A **production-ready microservices platform** for Dungeons & Dragons campaign management, built with modern cloud-native technologies. This project demonstrates enterprise-grade architecture patterns, security best practices, and full observability in a distributed system.

> **Portfolio Project** — This project showcases my expertise in designing and implementing scalable microservices architectures using Java 25, Quarkus, and cloud-native tooling.

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
- [CI/CD Pipeline](#cicd-pipeline)
- [Frontend](#frontend)
- [Roadmap](#roadmap)
- [License](#license)

---

## About The Project

DnD Platform is a comprehensive backend system designed to support Dungeons & Dragons gameplay and campaign management. It provides RESTful APIs for:

- **User Management** — Registration, authentication, and profile management
- **Character Management** — Create and manage D&D character sheets, import from official WotC 5e fillable PDFs, generate downloadable PDF sheets
- **Campaign Management** — Create/manage campaigns, invite and manage members, and maintain campaign notes with visibility controls
- **Compendium** — Reference data including monsters, spells, classes, and races
- **Combat Simulation** — Initiative tracking, damage calculation, and encounter management
- **Real-time Chat** — In-game messaging with WebSocket support
- **Asset Management** — Upload and manage character art, maps, and tokens (single and batch upload)
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
- **PDF Character Import** — Import characters from official D&D 5e fillable PDF sheets via Apache PDFBox
- **PDF Character Sheet Generation** — Auto-generate downloadable D&D 5e character sheets with computed stats (modifiers, HP, spell slots, proficiency)
- **CI/CD Pipeline** — GitHub Actions for automated build and deployment to self-hosted infrastructure
- **Frontend** — Static web client served via Nginx, deployed from a separate repository

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

### Docker Networking

The platform uses isolated Docker networks for security and separation of concerns:

| Network | Purpose |
|---------|---------|
| `dnd-frontend` | Frontend and public-facing services |
| `dnd-backend` | Inter-service communication |
| `dnd-database` | Database tier (PostgreSQL access) |
| `dnd-messaging` | Message broker tier (RabbitMQ access) |

---

## Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Language (OpenJDK Temurin) |
| Quarkus | 3.30.5 | Cloud-native Java framework |
| Maven | 3.9+ | Build automation |
| Hibernate ORM | 6.x | Object-relational mapping |
| SmallRye JWT | - | JWT token handling |
| MapStruct | 1.6.3 | DTO mapping |
| Apache PDFBox | 3.0.4 | PDF character sheet parsing |
| JavaPoet | 1.13.0 | Annotation-based code generation (common module) |

### Testing
| Technology | Version | Purpose |
|------------|---------|---------|
| JUnit | 5.11.4 | Unit testing |
| Mockito | 5.14.2 | Mocking framework |
| AssertJ | 3.26.3 | Fluent assertions |
| ArchUnit | 1.3.0 | Architecture testing |
| REST-Assured | - | REST API integration testing |

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
| Nginx | Alpine | Frontend static file serving |
| GitHub Actions | - | CI/CD pipeline (self-hosted runner) |

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
| **character-service** | 8082 | D&D character management | Character CRUD, PDF import, sheet generation/download |
| **campaign-service** | 8083 | Campaign & session management | Campaign CRUD, member management, campaign notes |
| **combat-service** | 8084 | Combat encounter simulation | Initiative, damage, turns |
| **compendium-service** | 8090 | D&D reference data (SRD) | Monsters, spells, classes, races |
| **asset-service** | 8085 | File/media management | Upload (single/batch), retrieve assets |
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

#### Create a Character

Create a new D&D 5e character with full details. A PDF character sheet is auto-generated on creation using the official WotC 5e template, with all derived stats computed automatically.

```bash
POST /characters
Host: localhost:8082
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "name": "Thorin Ironforge",
  "species": "Dwarf",
  "subrace": "Hill Dwarf",
  "characterClass": "Cleric",
  "subclass": "Life Domain",
  "background": "Acolyte",
  "alignment": "Lawful Good",
  "level": 1,
  "abilityScores": {
    "strength": 14,
    "dexterity": 12,
    "constitution": 16,
    "intelligence": 10,
    "wisdom": 16,
    "charisma": 8
  },
  "skillProficiencies": ["Insight", "Religion"],
  "savingThrowProficiencies": ["WIS", "CHA"],
  "languages": ["Common", "Dwarvish", "Celestial"],
  "proficiencies": [
    {"name": "Light Armor", "type": "ARMOR"},
    {"name": "Simple Weapons", "type": "WEAPON"}
  ],
  "equipment": [
    {"name": "Mace", "quantity": 1},
    {"name": "Scale Mail", "quantity": 1}
  ],
  "spells": ["Guidance", "Sacred Flame", "Bless", "Cure Wounds"],
  "personalityTraits": "I quote sacred texts in almost every situation.",
  "ideals": "Faith. I trust that my deity will guide my actions.",
  "bonds": "I will do anything to protect the temple where I served.",
  "flaws": "I judge others harshly, and myself even more severely."
}
```

**Response (200):**
```json
{
  "id": 1,
  "name": "Thorin Ironforge",
  "species": "Dwarf",
  "subrace": "Hill Dwarf",
  "characterClass": "Cleric",
  "subclass": "Life Domain",
  "background": "Acolyte",
  "alignment": "Lawful Good",
  "level": 1,
  "experiencePoints": 0,
  "abilityScores": {
    "strength": 14,
    "dexterity": 12,
    "constitution": 16,
    "intelligence": 10,
    "wisdom": 16,
    "charisma": 8
  },
  "hitPointsCurrent": 11,
  "hitPointsMax": 11,
  "hitPointsTemp": 0,
  "armorClass": 16,
  "speed": 25,
  "hitDiceTotal": 1,
  "hitDiceType": "d8",
  "hitDiceUsed": 0,
  "proficiencyBonus": 2,
  "inspiration": false,
  "spellcastingAbility": "WIS",
  "spellSaveDc": 13,
  "spellAttackBonus": 5,
  "skills": [{"name": "Insight", "modifier": 5, "proficient": true}],
  "savingThrows": [{"ability": "WIS", "modifier": 5, "proficient": true}],
  "spellSlots": [{"level": 1, "total": 2, "used": 0}],
  "createdAt": "2026-02-18T10:30:00",
  "updatedAt": null
}
```

**Computed stats** (auto-calculated by the server):
- **Proficiency bonus** — derived from character level
- **Ability modifiers** — `(score - 10) / 2`
- **Max HP** — hit die max + CON modifier (+ hit die avg per additional level)
- **Hit dice** — type and total based on class and level
- **Spellcasting** — ability, save DC (`8 + proficiency + ability mod`), attack bonus (class-dependent; `null` for non-casters)
- **Spell slots** — per-level allocation based on class and character level
- **Base speed** — based on species

| Error Code | Cause |
|------------|-------|
| 400 | Validation failed (missing required fields, invalid values) |
| 401 | Missing or invalid JWT token |
| 404 | Compendium validation failed (invalid species, class, background, or alignment) |

#### Get Characters (Paginated)

```bash
GET /characters?page=0&size=20
Host: localhost:8082
Authorization: Bearer <accessToken>
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Thorin Ironforge",
      "species": "Dwarf",
      "characterClass": "Cleric",
      "level": 1,
      "hitPointsCurrent": 11,
      "hitPointsMax": 11,
      "armorClass": 16
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

#### Import Character from PDF

Import a character by uploading an official WotC D&D 5e fillable character sheet PDF. The backend extracts form field data using Apache PDFBox and creates the character automatically.

```bash
POST /characters/import-sheet
Host: localhost:8082
Authorization: Bearer <accessToken>
Content-Type: multipart/form-data

# file: the filled PDF (max 10 MB)
```

```bash
# Example with curl
curl -X POST 'http://localhost:8082/characters/import-sheet' \
  -H "Authorization: Bearer $TOKEN" \
  -F 'file=@my-character-sheet.pdf'
```

| Error Code | Cause |
|------------|-------|
| 400 | No file, non-PDF file, empty file, missing AcroForm, or missing required fields |
| 401 | Missing or invalid JWT token |
| 403 | User does not have PLAYER role |
| 404 | Compendium validation failed (invalid species, class, background, or alignment) |

#### Download Character Sheet (PDF)

Download a generated D&D 5e PDF character sheet with all computed stats (modifiers, proficiency bonus, max HP, spell slots, etc.). Sheets are auto-generated on character creation.

```bash
GET /characters/{id}/sheet
Host: localhost:8082
Authorization: Bearer <accessToken>
```

```bash
# Example with curl
curl -X GET 'http://localhost:8082/characters/1/sheet' \
  -H "Authorization: Bearer $TOKEN" \
  -o character-sheet.pdf
```

**Response:** `200 OK` with `application/pdf` body and `Content-Disposition: attachment` header.

---

### Campaign Service API

#### Campaign Management

```bash
# Create a campaign
POST /campaigns
Host: localhost:8083
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "name": "Curse of Strahd",
  "description": "A gothic horror adventure in Barovia",
  "maxPlayers": 5,
  "imageUrl": "https://example.com/strahd.jpg"
}
```

```bash
# Get campaign by ID
GET /campaigns/{id}
Host: localhost:8083
Authorization: Bearer <accessToken>

# List user's campaigns (paginated)
GET /campaigns?page=0&size=20
Host: localhost:8083
Authorization: Bearer <accessToken>

# Update campaign (DM only)
PUT /campaigns/{id}
Host: localhost:8083
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "name": "Curse of Strahd — Chapter 2",
  "status": "ACTIVE"
}

# Delete campaign (DM only)
DELETE /campaigns/{id}
Host: localhost:8083
Authorization: Bearer <accessToken>
```

**Campaign Statuses:** `DRAFT`, `ACTIVE`, `PAUSED`, `COMPLETED`, `ARCHIVED`

#### Campaign Members

```bash
# Add member to campaign (DM only)
POST /campaigns/{campaignId}/members
Content-Type: application/json
{"userId": 2, "characterId": 5}

# List campaign members
GET /campaigns/{campaignId}/members

# Remove member (DM removes anyone; players remove themselves)
DELETE /campaigns/{campaignId}/members/{userId}
```

**Member Roles:** `DUNGEON_MASTER` (campaign creator), `PLAYER`

#### Campaign Notes

Campaign members can create notes with visibility controls — `PUBLIC` notes are visible to all members, `PRIVATE` notes only to the author.

```bash
# Create a note
POST /campaigns/{campaignId}/notes
Content-Type: application/json
{"title": "Session 1 Recap", "content": "The party entered Barovia...", "visibility": "PUBLIC"}

# List notes (returns public + user's private notes)
GET /campaigns/{campaignId}/notes

# Get note by ID
GET /campaigns/{campaignId}/notes/{noteId}

# Update note (author only)
PUT /campaigns/{campaignId}/notes/{noteId}
Content-Type: application/json
{"title": "Updated Title", "visibility": "PRIVATE"}

# Delete note (author or DM)
DELETE /campaigns/{campaignId}/notes/{noteId}
```

---

### Asset Service API

```bash
# Upload a single document
POST /api/assets/documents
Host: localhost:8085
Authorization: Bearer <accessToken>
Content-Type: multipart/form-data

# Upload multiple documents (batch)
POST /api/assets/documents/batch
Host: localhost:8085
Authorization: Bearer <accessToken>
Content-Type: multipart/form-data
```

---

## Getting Started

### Prerequisites

- **Java 25** — [Download OpenJDK Temurin](https://adoptium.net/)
- **Maven 3.9+** — [Download Maven](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose** — [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)

Verify installations:
```bash
java --version    # Should show 25+
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
| Frontend | http://localhost:4000 |
| Auth Service | http://localhost:8081 |
| User Service | http://localhost:8089 |
| Compendium Service | http://localhost:8090 |
| Character Service | http://localhost:8082 |
| Campaign Service | http://localhost:8083 |
| Asset Service | http://localhost:8085 |
| Traefik Dashboard | http://localhost:8080 |
| Grafana Dashboard | http://localhost:3000 |
| Jaeger Tracing | http://localhost:16686 |
| RabbitMQ Management | http://localhost:15672 |
| MinIO Console | http://localhost:9001 |
| Vault | http://localhost:8200 |
| Portainer | http://localhost:9002 |

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

### Create a Character

```bash
# Create a character (auto-generates a PDF sheet with computed stats)
curl -s -X POST 'http://localhost:8082/characters' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Thorin Ironforge",
    "species": "Dwarf",
    "characterClass": "Cleric",
    "background": "Acolyte",
    "alignment": "Lawful Good",
    "level": 1,
    "abilityScores": {"strength":14,"dexterity":12,"constitution":16,"intelligence":10,"wisdom":16,"charisma":8},
    "skillProficiencies": ["Insight","Religion"],
    "savingThrowProficiencies": ["WIS","CHA"],
    "languages": ["Common","Dwarvish","Celestial"],
    "spells": ["Guidance","Sacred Flame","Bless","Cure Wounds"]
  }' | jq

# Download the auto-generated PDF character sheet
curl -X GET 'http://localhost:8082/characters/1/sheet' \
  -H "Authorization: Bearer $TOKEN" \
  -o thorin-ironforge.pdf

# Or import from an existing WotC 5e fillable PDF
curl -X POST 'http://localhost:8082/characters/import-sheet' \
  -H "Authorization: Bearer $TOKEN" \
  -F 'file=@my-character-sheet.pdf'
```

### Manage a Campaign

```bash
# Create a campaign (you become the Dungeon Master)
curl -s -X POST 'http://localhost:8083/campaigns' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Lost Mine of Phandelver","description":"A classic starter adventure","maxPlayers":5}' | jq

# List your campaigns
curl -s 'http://localhost:8083/campaigns?page=0&size=20' \
  -H "Authorization: Bearer $TOKEN" | jq

# Add a player to the campaign
curl -s -X POST 'http://localhost:8083/campaigns/1/members' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"userId":2,"characterId":1}' | jq

# Create a session note
curl -s -X POST 'http://localhost:8083/campaigns/1/notes' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Session 1","content":"The party met at the Yawning Portal...","visibility":"PUBLIC"}' | jq
```

---

## Project Structure

```
dnd-platform/
├── services/
│   ├── common/                     # Shared library (annotation processors, code generation)
│   ├── auth-service/               # Authentication microservice
│   ├── user-service/               # User management microservice
│   ├── character-service/          # Character management microservice (incl. PDF import)
│   ├── campaign-service/           # Campaign management microservice
│   ├── combat-service/             # Combat simulation microservice
│   ├── compendium-service/         # D&D reference data microservice
│   ├── asset-service/              # File management microservice
│   ├── chat-service/               # Real-time chat microservice
│   ├── notification-service/       # Notification microservice
│   ├── search-service/             # Search microservice
│   └── frontend-deploy/            # Frontend deployment (Nginx + git clone)
├── infrastructure/
│   ├── postgres/                   # Database initialization scripts
│   ├── vault/                      # Vault configuration
│   ├── monitoring/                 # Prometheus, Grafana, Loki configs
│   └── portainer/                  # Portainer initialization scripts
├── .github/
│   └── workflows/                  # CI/CD pipeline (GitHub Actions)
├── docker-compose.yml              # Backend container orchestration
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

## CI/CD Pipeline

The project uses **GitHub Actions** for continuous deployment to a self-hosted homeserver.

- **Trigger:** Commits containing `RELEASE` in the message (e.g., `git commit -m "v1.2 RELEASE - new feature"`)
- **Runner:** Self-hosted on the target homeserver
- **Flow:** Setup JDK 25 + Maven 3.9.9 → Execute `ci-deploy.sh` (clone/pull → Maven build → Docker deploy)
- **Output:** GitHub Step Summary with deployment status and container health

---

## Frontend

The frontend is a static web client hosted in a [separate repository](https://github.com/MaryHsn93/DnD-platform-FE) and deployed via its own Docker Compose configuration (`services/frontend-deploy/docker-compose.yml`).

- **Serving:** Nginx (Alpine) on port 4000
- **Deployment:** An init container clones/pulls the `release` branch from the frontend repository, then Nginx serves the static files
- **Features:** In-browser PDF character sheet viewer and import (using PDF.js)

---

## Roadmap

- [x] User authentication with JWT
- [x] Compendium service with monsters and spells
- [x] Redis caching for reference data
- [x] Full observability stack
- [x] PDF character sheet import
- [x] PDF character sheet generation & download
- [x] Campaign management (CRUD, members, notes)
- [x] CI/CD pipeline with GitHub Actions
- [x] Frontend deployment
- [ ] Real-time combat tracker
- [ ] WebSocket-based chat
- [ ] Mobile-friendly API responses

---

## Technical Highlights

This project demonstrates proficiency in:

| Skill | Implementation |
|-------|----------------|
| **Microservices Architecture** | 10 independent services with clear boundaries |
| **Domain-Driven Design** | Hexagonal architecture with domain isolation |
| **API Design** | RESTful APIs with proper pagination and filtering |
| **Security** | JWT authentication, RBAC, Vault secrets management |
| **Database Design** | Database-per-service pattern with isolated PostgreSQL databases |
| **Caching Strategies** | Redis caching with TTL for reference data |
| **Event-Driven Architecture** | RabbitMQ for async communication |
| **Observability** | Prometheus metrics, Jaeger tracing, Loki centralized logging |
| **Containerization** | Docker Compose orchestration, multi-layer networking, health checks |
| **CI/CD** | GitHub Actions pipeline with self-hosted runner deployment |
| **Testing** | JUnit 5, Mockito, AssertJ, ArchUnit architecture tests |
| **Modern Java** | Java 25, Quarkus framework, annotation processing |

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
