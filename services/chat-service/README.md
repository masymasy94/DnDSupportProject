# Chat Service

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.31-blue?style=flat-square&logo=quarkus)](https://quarkus.io/)
[![Port](https://img.shields.io/badge/Port-8086-green?style=flat-square)]()

The **Chat Service** provides real-time messaging for the DnD Platform via both REST APIs and **WebSocket** connections. It supports direct (1-to-1) and group conversations, message history with pagination, and read-receipt tracking. WebSocket connections authenticate via JWT tokens passed as query parameters.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Operative Flows](#operative-flows)
  - [Create Conversation (Direct)](#1-create-direct-conversation)
  - [Create Conversation (Group)](#2-create-group-conversation)
  - [Send Message (REST)](#3-send-message-rest)
  - [Send Message (WebSocket)](#4-send-message-websocket)
  - [List Conversations](#5-list-conversations)
  - [Get Messages (Paginated)](#6-get-messages-paginated)
  - [Mark as Read](#7-mark-conversation-as-read)
  - [WebSocket Lifecycle](#8-websocket-lifecycle)
- [Design Patterns](#design-patterns)
- [WebSocket Protocol](#websocket-protocol)
- [Data Model](#data-model)

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────────────────┐
│                        Chat Service (:8086)                              │
│                                                                          │
│  ┌─────────────────┐    ┌───────────────────┐    ┌───────────────────┐  │
│  │ Adapter-Inbound │    │      Domain        │    │ Adapter-Outbound  │  │
│  │                 │    │                    │    │                   │  │
│  │ REST Resources  │    │ Conversation       │    │ JPA Repositories  │  │
│  │ Delegates       │───►│  Create/Find Svc   │───►│  (Panache)        │  │
│  │                 │    │                    │    │                   │  │
│  │ WebSocket       │    │ Message            │    │                   │  │
│  │  Endpoint       │───►│  Send/Find Svc     │    │                   │  │
│  │  SessionManager │    │                    │    │                   │  │
│  └─────────────────┘    └───────────────────┘    └────────┬──────────┘  │
│                                                            │             │
└────────────────────────────────────────────────────────────┼─────────────┘
                                                              │
                                                       ┌────────────┐
                                                       │ PostgreSQL │
                                                       │ (chat_db)  │
                                                       └────────────┘
```

---

## Module Structure

```
chat-service/
├── chat-service-domain/               # Business logic & domain models
│   ├── model/                         # Records: Conversation, Message, ConversationParticipant
│   ├── ConversationCreateService      # Routes to Direct or Group creation
│   ├── DirectConversationCreateService# Idempotent direct conversation creation
│   ├── GroupConversationCreateService # Group conversation with name
│   ├── ConversationFindByIdService    # Single conversation lookup
│   ├── ConversationFindByUserService  # User's conversation list
│   ├── ConversationUpdateReadByIdService # Mark as read
│   ├── MessageSendService             # Send message (REST + WebSocket)
│   └── MessageFindByConversationService # Paginated message history
│
├── chat-service-view-model/           # DTOs & Resource interfaces
│   └── vm/                            # ViewModels + WebSocket message types
│
├── chat-service-adapter-inbound/      # Driving Adapters
│   ├── conversation/                  # REST: create, find, list, mark-read
│   ├── message/                       # REST: send, history
│   ├── websocket/                     # WebSocket endpoint + session manager
│   └── exception/                     # ChatExceptionMapper
│
├── chat-service-adapter-outbound/     # Driven Adapters
│   └── jpa/                           # Panache entities & JPA repositories
│
├── chat-service-client/               # REST client interface
└── chat-service/                      # Quarkus bootstrap & configuration
```

---

## API Endpoints

### REST Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/chat/conversations` | Bearer | Create conversation (direct or group) |
| `GET` | `/api/chat/conversations` | Bearer | List user's conversations |
| `GET` | `/api/chat/conversations/{id}` | Bearer | Get conversation by ID |
| `PUT` | `/api/chat/conversations/{id}/read` | Bearer | Mark conversation as read |
| `POST` | `/api/chat/conversations/{id}/messages` | Bearer | Send a message |
| `GET` | `/api/chat/conversations/{id}/messages` | Bearer | Get paginated message history |

### WebSocket

| Protocol | Path | Auth | Description |
|----------|------|------|-------------|
| `WS` | `/ws/chat?token=<jwt>` | JWT query param | Real-time messaging |

---

## Operative Flows

### 1. Create Direct Conversation

**`POST /api/chat/conversations`** with `type: "DIRECT"` — Creates a 1-to-1 conversation. **Idempotent**: if a direct conversation already exists between the two users, returns the existing one.

```
Client                     Chat Service                              Database
  │                             │                                        │
  │  POST /conversations       │                                        │
  │  {type: "DIRECT",          │                                        │
  │   participantIds: [2]}     │                                        │
  │  ?userId=1                 │                                        │
  │────────────────────────────►│                                        │
  │                             │                                        │
  │                             │  ┌──────────────────────────────────┐   │
  │                             │  │ ConversationCreateServiceImpl    │   │
  │                             │  │  → routes to DirectConversation  │   │
  │                             │  │    CreateServiceImpl             │   │
  │                             │  └──────────────────────────────────┘   │
  │                             │                                        │
  │                             │  ┌──────────────────────────────────┐   │
  │                             │  │ DirectConversationCreateService  │   │
  │                             │  │                                  │   │
  │                             │  │ 1. findDirectConversation(1, 2)  │   │
  │                             │  │    → check if already exists     │   │
  │                             │  │                                  │   │
  │                             │  │ 2a. EXISTS → return existing     │   │
  │                             │  │                                  │   │
  │                             │  │ 2b. NOT EXISTS →                 │   │
  │                             │  │    Build Conversation:           │   │
  │                             │  │     type=DIRECT, createdBy=1     │   │
  │                             │  │    Build 2 participants:         │   │
  │                             │  │     userId=1 (MEMBER)            │   │
  │                             │  │     userId=2 (MEMBER)            │   │
  │                             │  │    ConversationCreateRepo.create │   │
  │                             │  └──────────────────────────────────┘   │
  │                             │                                        │
  │                             │  INSERT conversation + participants    │
  │                             │───────────────────────────────────────►│
  │                             │                                        │
  │  201 Created                │                                        │
  │  {id, type: "DIRECT",      │                                        │
  │   participants: [...]}     │                                        │
  │◄────────────────────────────│                                        │
```

---

### 2. Create Group Conversation

**`POST /api/chat/conversations`** with `type: "GROUP"` — Creates a group chat. Creator gets `ADMIN` role; others get `MEMBER`.

```
Client                     Chat Service                              Database
  │                             │                                        │
  │  POST /conversations       │                                        │
  │  {type: "GROUP",           │                                        │
  │   name: "Party Chat",     │                                        │
  │   participantIds: [2, 3]}  │                                        │
  │  ?userId=1                 │                                        │
  │────────────────────────────►│                                        │
  │                             │                                        │
  │                             │  ┌──────────────────────────────────┐   │
  │                             │  │ GroupConversationCreateService   │   │
  │                             │  │                                  │   │
  │                             │  │ 1. Validate name not blank       │   │
  │                             │  │ 2. Build participants:           │   │
  │                             │  │    userId=1 → ADMIN              │   │
  │                             │  │    userId=2 → MEMBER             │   │
  │                             │  │    userId=3 → MEMBER             │   │
  │                             │  │ 3. Create conversation           │   │
  │                             │  └──────────────────────────────────┘   │
  │                             │                                        │
  │  201 Created                │                                        │
  │  {id, type: "GROUP",       │                                        │
  │   name: "Party Chat",     │                                        │
  │   participants: [...]}     │                                        │
  │◄────────────────────────────│                                        │
```

---

### 3. Send Message (REST)

**`POST /api/chat/conversations/{conversationId}/messages`**

```
Client                     Chat Service                         Database
  │                             │                                   │
  │  POST /.../messages        │                                   │
  │  {content: "Hello!",       │                                   │
  │   messageType: "TEXT"}     │                                   │
  │  ?userId=1                 │                                   │
  │────────────────────────────►│                                   │
  │                             │                                   │
  │                             │  ┌────────────────────────────┐   │
  │                             │  │ MessageSendServiceImpl      │   │
  │                             │  │                              │   │
  │                             │  │ 1. Find conversation         │   │
  │                             │  │ 2. Verify sender is active   │   │
  │                             │  │    participant (leftAt=NULL) │   │
  │                             │  │ 3. Build Message record      │   │
  │                             │  │ 4. MessageCreateRepository   │   │
  │                             │  │    .create()                 │   │
  │                             │  │ 5. Update conversation       │   │
  │                             │  │    .updatedAt = now          │   │
  │                             │  └────────────────────────────┘   │
  │                             │                                   │
  │                             │  INSERT message + UPDATE conv     │
  │                             │──────────────────────────────────►│
  │                             │                                   │
  │  201 Created                │                                   │
  │  {id, conversationId,      │                                   │
  │   senderId, content,       │                                   │
  │   messageType, createdAt}  │                                   │
  │◄────────────────────────────│                                   │
```

---

### 4. Send Message (WebSocket)

Real-time messaging via WebSocket with broadcast to all conversation participants.

```
Client A                     Chat Service                    Client B    Client C
  │                               │                              │           │
  │  WS: {"type":"SEND_MESSAGE", │                              │           │
  │       "conversationId": 1,   │                              │           │
  │       "content": "Hello!"}   │                              │           │
  │─────────────────────────────►│                              │           │
  │                               │                              │           │
  │                               │  ┌────────────────────┐     │           │
  │                               │  │ ChatWebSocketEndpoint    │           │
  │                               │  │                    │     │           │
  │                               │  │ 1. Parse JSON      │     │           │
  │                               │  │ 2. Extract senderId│     │           │
  │                               │  │    from JWT mapping│     │           │
  │                               │  │ 3. MessageSendService   │           │
  │                               │  │    .send()         │     │           │
  │                               │  │ 4. Get participants│     │           │
  │                               │  │ 5. Build broadcast │     │           │
  │                               │  │    message         │     │           │
  │                               │  └────────────────────┘     │           │
  │                               │                              │           │
  │                               │  ┌──────────────────────┐   │           │
  │                               │  │ ChatSessionManager   │   │           │
  │                               │  │  broadcastToUsers()  │   │           │
  │                               │  │  → sends to ALL open │   │           │
  │                               │  │    connections per    │   │           │
  │                               │  │    participant       │   │           │
  │                               │  └──────────────────────┘   │           │
  │                               │                              │           │
  │  WS: {"type":"NEW_MESSAGE",  │  {"type":"NEW_MESSAGE",...}  │           │
  │       "conversationId": 1,   │──────────────────────────────►│           │
  │       "messageId": 42,       │  {"type":"NEW_MESSAGE",...}  │           │
  │       "senderId": 1,         │──────────────────────────────────────────►│
  │       "content": "Hello!"}   │                              │           │
  │◄──────────────────────────────│                              │           │
```

---

### 5. List Conversations

**`GET /api/chat/conversations?userId=1`** — Returns all conversations where the user is an active participant, ordered by most recently updated.

```
Client                      Chat Service                        Database
  │                              │                                  │
  │  GET /conversations          │                                  │
  │  ?userId=1                   │                                  │
  │─────────────────────────────►│                                  │
  │                              │                                  │
  │                              │  JPQL: SELECT DISTINCT c         │
  │                              │  JOIN FETCH participants          │
  │                              │  WHERE p.userId=1                │
  │                              │    AND p.leftAt IS NULL          │
  │                              │  ORDER BY updatedAt DESC,        │
  │                              │    createdAt DESC                │
  │                              │─────────────────────────────────►│
  │                              │  List<ConversationEntity>        │
  │                              │◄─────────────────────────────────│
  │                              │                                  │
  │  200 OK                      │                                  │
  │  [{id, name, type,           │                                  │
  │    participants: [...]}]     │                                  │
  │◄─────────────────────────────│                                  │
```

---

### 6. Get Messages (Paginated)

**`GET /api/chat/conversations/{id}/messages?page=0&pageSize=50`**

```
Client                      Chat Service                        Database
  │                              │                                  │
  │  GET /.../messages           │                                  │
  │  ?page=0&pageSize=50         │                                  │
  │─────────────────────────────►│                                  │
  │                              │  ┌────────────────────────────┐  │
  │                              │  │ Verify conversation exists │  │
  │                              │  │ Verify user is participant │  │
  │                              │  │ (leftAt IS NULL)           │  │
  │                              │  └────────────────────────────┘  │
  │                              │                                  │
  │                              │  WHERE conversation_id = X       │
  │                              │    AND deletedAt IS NULL         │
  │                              │  ORDER BY createdAt DESC         │
  │                              │  LIMIT 50 OFFSET 0              │
  │                              │─────────────────────────────────►│
  │                              │                                  │
  │  200 OK                      │                                  │
  │  {content: [{message}...],   │                                  │
  │   page, size,                │                                  │
  │   totalElements, totalPages} │                                  │
  │◄─────────────────────────────│                                  │
```

---

### 7. Mark Conversation as Read

**`PUT /api/chat/conversations/{id}/read?userId=1`** — Updates the participant's `lastReadAt` timestamp.

```
Client                      Chat Service                     Database
  │                              │                               │
  │  PUT /conversations/{id}/    │                               │
  │      read?userId=1           │                               │
  │─────────────────────────────►│                               │
  │                              │  Verify user is participant   │
  │                              │  UPDATE lastReadAt = now()    │
  │                              │  WHERE conversation_id AND    │
  │                              │    userId                     │
  │                              │──────────────────────────────►│
  │  204 No Content              │                               │
  │◄─────────────────────────────│                               │
```

---

### 8. WebSocket Lifecycle

```
Client                       Chat Service
  │                               │
  │  WS Connect                   │
  │  /ws/chat?token=<jwt>         │
  │──────────────────────────────►│
  │                               │  ┌────────────────────────────┐
  │                               │  │ @OnOpen                    │
  │                               │  │  1. Parse JWT from ?token= │
  │                               │  │  2. Extract userId from    │
  │                               │  │     claims ("userId"/"sub")│
  │                               │  │  3. Map connection→userId  │
  │                               │  │  4. Register in            │
  │                               │  │     ChatSessionManager     │
  │                               │  │  5. Send CONNECTED msg     │
  │                               │  └────────────────────────────┘
  │                               │
  │  ◄── {"type":"CONNECTED"} ───│
  │                               │
  │  ─── SEND_MESSAGE ──────────►│  → @OnTextMessage
  │  ◄── NEW_MESSAGE ───────────│     (broadcast to participants)
  │                               │
  │  WS Disconnect                │
  │──────────────────────────────►│
  │                               │  ┌────────────────────────────┐
  │                               │  │ @OnClose                   │
  │                               │  │  Remove from SessionManager│
  │                               │  └────────────────────────────┘
```

The `ChatSessionManager` supports **multiple connections per user** (e.g., desktop + mobile) using a `userId → List<WebSocketConnection>` map.

---

## Design Patterns

### Hexagonal Architecture (Ports & Adapters)

```
                     ┌───────────────────────────────────┐
    Driving Ports    │         DOMAIN CORE                │   Driven Ports
  ───────────────────│                                     │──────────────────
                     │  ConversationCreate Services        │
  REST Resources ───►│   ├ DirectConversationCreateService │────► ConversationCreateRepo (JPA)
  WebSocket ────────►│   └ GroupConversationCreateService  │────► ConversationFindDirectRepo (JPA)
  Delegates          │  ConversationFind Services          │────► MessageCreateRepo (JPA)
                     │  MessageSend/Find Services          │────► ParticipantExistsRepo (JPA)
                     └───────────────────────────────────┘
```

### Delegate Pattern

REST endpoints use ResourceImpl + `@Delegate`, identical to other services. The WebSocket endpoint (`ChatWebSocketEndpoint`) calls domain services directly since it manages its own connection lifecycle.

### Strategy Pattern (Conversation Creation)

`ConversationCreateServiceImpl` acts as a **router**, delegating to the correct strategy based on `ConversationType`:

```
ConversationCreateService.create(type, ...)
        │
        ├── DIRECT → DirectConversationCreateService (idempotent)
        │              └► Check existing → reuse OR create new
        │
        └── GROUP  → GroupConversationCreateService
                       └► Validate name → create with ADMIN + MEMBERs
```

### Observer Pattern (WebSocket Broadcasting)

The `ChatSessionManager` maintains a registry of active connections. When a message is sent, it broadcasts to all participant connections:

```
ChatSessionManager
  userId → [Connection1, Connection2, ...]  (ConcurrentHashMap)

broadcastToUsers(participantIds, message):
  for each userId:
    for each open connection:
      connection.sendTextAndAwait(message)
```

### Idempotency Pattern (Direct Conversations)

Creating a direct conversation between the same two users always returns the same conversation, preventing duplicates.

---

## WebSocket Protocol

### Incoming Messages (Client → Server)

```json
{
  "type": "SEND_MESSAGE",
  "conversationId": 1,
  "content": "Hello party!",
  "messageType": "TEXT"
}
```

### Outgoing Messages (Server → Client)

**New Message:**
```json
{
  "type": "NEW_MESSAGE",
  "conversationId": 1,
  "messageId": 42,
  "senderId": 1,
  "content": "Hello party!",
  "messageType": "TEXT",
  "timestamp": "2026-02-27T14:30:00"
}
```

**Connected:**
```json
{ "type": "CONNECTED" }
```

**Error:**
```json
{
  "type": "ERROR",
  "content": "Conversation not found"
}
```

### Message Types

| Type | Description |
|------|-------------|
| `TEXT` | Standard text message (default) |
| `SYSTEM` | System-generated message |
| `IMAGE` | Image attachment reference |

### Participant Roles

| Role | Description |
|------|-------------|
| `ADMIN` | Group creator — can manage the conversation |
| `MEMBER` | Regular participant |

---

## Data Model

```
┌─────────────────────────┐
│     conversations        │
├─────────────────────────┤         ┌───────────────────────────────┐
│ id         BIGSERIAL PK │         │  conversation_participants    │
│ name       VARCHAR       │         ├───────────────────────────────┤
│ type       VARCHAR(20)  │    ┌───►│ id             BIGSERIAL PK  │
│ created_by BIGINT        │    │    │ conversation_id BIGINT FK    │
│ created_at TIMESTAMP    │    │    │ user_id        BIGINT        │
│ updated_at TIMESTAMP    │────┘    │ role           VARCHAR(20)   │
│                         │         │ joined_at      TIMESTAMP     │
│                         │         │ left_at        TIMESTAMP     │  ← NULL = active
│                         │         │ last_read_at   TIMESTAMP     │
└─────────────────────────┘         └───────────────────────────────┘

                                    ┌───────────────────────────────┐
                                    │         messages              │
                                    ├───────────────────────────────┤
                                    │ id             BIGSERIAL PK  │
                                    │ conversation_id BIGINT FK    │
                                    │ sender_id      BIGINT        │
                                    │ content        TEXT          │
                                    │ message_type   VARCHAR(20)   │
                                    │ created_at     TIMESTAMP     │
                                    │ edited_at      TIMESTAMP     │
                                    │ deleted_at     TIMESTAMP     │  ← soft delete
                                    └───────────────────────────────┘
```
