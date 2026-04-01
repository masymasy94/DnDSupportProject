# Campaign Service

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.31-blue?style=flat-square&logo=quarkus)](https://quarkus.io/)
[![Port](https://img.shields.io/badge/Port-8083-green?style=flat-square)]()

The **Campaign Service** manages D&D campaigns, their members, and campaign notes with visibility controls. The campaign creator is automatically assigned the **Dungeon Master** role and has elevated permissions for managing members and content.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Operative Flows](#operative-flows)
  - [Create Campaign](#1-create-campaign)
  - [Find Campaign by ID](#2-find-campaign-by-id)
  - [List Campaigns (Paginated)](#3-list-campaigns-paginated)
  - [Update Campaign](#4-update-campaign)
  - [Delete Campaign](#5-delete-campaign)
  - [Add Member](#6-add-member)
  - [List Members](#7-list-members)
  - [Remove Member](#8-remove-member)
  - [Create Note](#9-create-note)
  - [List Notes (Visibility-Filtered)](#10-list-notes-visibility-filtered)
  - [Update Note](#11-update-note)
  - [Delete Note](#12-delete-note)
- [Design Patterns](#design-patterns)
- [Authorization Model](#authorization-model)
- [Data Model](#data-model)

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────────────┐
│                     Campaign Service (:8083)                         │
│                                                                      │
│  ┌─────────────────┐    ┌───────────────────┐    ┌───────────────┐  │
│  │ Adapter-Inbound │    │      Domain        │    │Adapter-Outbound│ │
│  │                 │    │                    │    │               │  │
│  │ ResourceImpl    │    │ Campaign Services  │    │ JPA Repos     │  │
│  │ Delegate        │───►│ Member Services    │───►│ (Panache)     │  │
│  │ Mapper          │    │ Note Services      │    │               │  │
│  └─────────────────┘    └───────────────────┘    └───────┬───────┘  │
│                                                           │          │
└───────────────────────────────────────────────────────────┼──────────┘
                                                            │
                                                     ┌────────────┐
                                                     │ PostgreSQL │
                                                     │(campaign_db)│
                                                     └────────────┘
```

---

## Module Structure

```
campaign-service/
├── campaign-service-domain/          # Business logic & domain models
│   ├── model/                        # Records: Campaign, CampaignMember, CampaignNote, etc.
│   ├── CampaignCreateService         # Create campaign + auto-add DM
│   ├── CampaignFindByIdService       # Single campaign lookup
│   ├── CampaignFindByUserService     # Paginated campaigns for user
│   ├── CampaignUpdateService         # DM-only update
│   ├── CampaignDeleteService         # DM-only delete
│   ├── CampaignMember*Service        # Add, list, remove members
│   └── CampaignNote*Service          # Create, find, list, update, delete notes
│
├── campaign-service-view-model/      # DTOs & Resource interfaces
│   └── vm/                           # Request/Response ViewModels
│
├── campaign-service-adapter-inbound/ # REST controllers (Driving Adapters)
│   ├── campaign/                     # Campaign CRUD endpoints + Delegates
│   ├── member/                       # Member management endpoints + Delegates
│   └── note/                         # Note CRUD endpoints + Delegates
│
├── campaign-service-adapter-outbound/# Infrastructure (Driven Adapters)
│   └── jpa/                          # Panache entities & JPA repositories
│
├── campaign-service-client/          # REST client interface
└── campaign-service/                 # Quarkus bootstrap & configuration
```

---

## API Endpoints

### Campaign Management

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/campaigns` | Bearer | Create campaign (caller becomes DM) |
| `GET` | `/campaigns/{id}` | Bearer | Get campaign by ID |
| `GET` | `/campaigns` | Bearer | List user's campaigns (paginated) |
| `PUT` | `/campaigns/{id}` | Bearer | Update campaign (DM only) |
| `DELETE` | `/campaigns/{id}` | Bearer | Delete campaign (DM only) |

### Member Management

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/campaigns/{campaignId}/members` | Bearer | Add member (DM only) |
| `GET` | `/campaigns/{campaignId}/members` | Bearer | List campaign members |
| `DELETE` | `/campaigns/{campaignId}/members/{userId}` | Bearer | Remove member (DM or self) |

### Note Management

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/campaigns/{campaignId}/notes` | Bearer | Create a note |
| `GET` | `/campaigns/{campaignId}/notes` | Bearer | List visible notes |
| `GET` | `/campaigns/{campaignId}/notes/{noteId}` | Bearer | Get note by ID |
| `PUT` | `/campaigns/{campaignId}/notes/{noteId}` | Bearer | Update note (author only) |
| `DELETE` | `/campaigns/{campaignId}/notes/{noteId}` | Bearer | Delete note (author or DM) |

---

## Operative Flows

### 1. Create Campaign

**`POST /campaigns`** — Creates a campaign and automatically adds the creator as `DUNGEON_MASTER`.

```
Client                    Campaign Service                          Database
  │                            │                                       │
  │  POST /campaigns           │                                       │
  │  {name, description,       │                                       │
  │   maxPlayers, imageUrl}    │                                       │
  │───────────────────────────►│                                       │
  │                            │                                       │
  │                            │  ┌──────────────────────────────────┐  │
  │                            │  │ CampaignCreateDelegate           │  │
  │                            │  │  └► CampaignCreateMapper         │  │
  │                            │  │      (userId → dungeonMasterId)  │  │
  │                            │  └──────────────────────────────────┘  │
  │                            │                                       │
  │                            │  ┌──────────────────────────────────┐  │
  │                            │  │ CampaignCreateServiceImpl        │  │
  │                            │  │                                  │  │
  │                            │  │ 1. CampaignCreateRepository      │  │
  │                            │  │    .save(campaignCreate)         │  │
  │                            │  │    → status=DRAFT, default       │  │
  │                            │  │      maxPlayers=6                │  │
  │                            │  │                                  │  │
  │                            │  │ 2. CampaignMemberAddRepository   │  │
  │                            │  │    .add(campaignId, userId,      │  │
  │                            │  │         null, DUNGEON_MASTER)    │  │
  │                            │  │    → auto-add creator as DM     │  │
  │                            │  └──────────────────────────────────┘  │
  │                            │                                       │
  │                            │  INSERT campaign + INSERT member      │
  │                            │──────────────────────────────────────►│
  │                            │                                       │
  │  201 Created               │                                       │
  │  {id, name, description,   │                                       │
  │   status: "DRAFT",         │                                       │
  │   maxPlayers, ...}         │                                       │
  │◄───────────────────────────│                                       │
```

---

### 2. Find Campaign by ID

**`GET /campaigns/{id}`**

```
Client                    Campaign Service           Database
  │                            │                         │
  │  GET /campaigns/{id}       │                         │
  │───────────────────────────►│                         │
  │                            │  find("id", id)         │
  │                            │────────────────────────►│
  │                            │  CampaignEntity         │
  │                            │◄────────────────────────│
  │                            │                         │
  │                            │  CampaignEntityMapper   │
  │                            │  → CampaignViewModelMapper
  │                            │                         │
  │  200 OK {CampaignVM}      │                         │
  │◄───────────────────────────│                         │
  │  (or 404 Not Found)        │                         │
```

---

### 3. List Campaigns (Paginated)

**`GET /campaigns?page=0&size=20`** — Returns campaigns where the user is a member.

```
Client                    Campaign Service                   Database
  │                            │                                 │
  │  GET /campaigns            │                                 │
  │  ?page=0&size=20           │                                 │
  │───────────────────────────►│                                 │
  │                            │  Subquery: campaigns WHERE      │
  │                            │  user is a member               │
  │                            │  + count + page                 │
  │                            │────────────────────────────────►│
  │                            │  List<CampaignEntity>           │
  │                            │◄────────────────────────────────│
  │                            │                                 │
  │                            │  CampaignSummaryMapper          │
  │                            │  (id, name, status, playerCount)│
  │                            │                                 │
  │  200 OK                    │                                 │
  │  {content: [...],          │                                 │
  │   page, size,              │                                 │
  │   totalElements, totalPages}│                                │
  │◄───────────────────────────│                                 │
```

The `playerCount` is computed from `members.size()` by the `CampaignEntityMapper`.

---

### 4. Update Campaign

**`PUT /campaigns/{id}`** — DM-only. Supports status transitions.

```
Client                    Campaign Service                   Database
  │                            │                                 │
  │  PUT /campaigns/{id}       │                                 │
  │  {name, description,       │                                 │
  │   status, maxPlayers}      │                                 │
  │───────────────────────────►│                                 │
  │                            │  ┌────────────────────────────┐ │
  │                            │  │ CampaignUpdateServiceImpl  │ │
  │                            │  │  1. Find campaign           │ │
  │                            │  │  2. Validate: userId ==     │ │
  │                            │  │     dungeonMasterId         │ │
  │                            │  │     → ForbiddenException    │ │
  │                            │  │  3. Selective field update   │ │
  │                            │  │  4. Set updatedAt           │ │
  │                            │  └────────────────────────────┘ │
  │                            │                                 │
  │  200 OK {CampaignVM}      │                                 │
  │◄───────────────────────────│                                 │
```

**Campaign Statuses:** `DRAFT` → `ACTIVE` → `PAUSED` → `COMPLETED` → `ARCHIVED`

---

### 5. Delete Campaign

**`DELETE /campaigns/{id}`** — DM-only. Cascades to members and notes.

```
Client                    Campaign Service           Database
  │                            │                         │
  │  DELETE /campaigns/{id}    │                         │
  │───────────────────────────►│                         │
  │                            │  Validate DM ownership  │
  │                            │  delete(entity)         │
  │                            │  (cascades to members   │
  │                            │   and notes)            │
  │                            │────────────────────────►│
  │  204 No Content            │                         │
  │◄───────────────────────────│                         │
```

---

### 6. Add Member

**`POST /campaigns/{campaignId}/members`** — DM-only. Adds a player with `PLAYER` role.

```
Client                    Campaign Service                     Database
  │                            │                                   │
  │  POST /.../members         │                                   │
  │  {requesterId, userId,     │                                   │
  │   characterId}             │                                   │
  │───────────────────────────►│                                   │
  │                            │  ┌──────────────────────────────┐ │
  │                            │  │ CampaignMemberAddServiceImpl │ │
  │                            │  │  1. Validate requester is DM  │ │
  │                            │  │  2. Add member (role=PLAYER)  │ │
  │                            │  └──────────────────────────────┘ │
  │                            │                                   │
  │                            │  INSERT campaign_members          │
  │                            │──────────────────────────────────►│
  │                            │                                   │
  │  201 Created               │                                   │
  │  {id, campaignId, userId,  │                                   │
  │   characterId, role,       │                                   │
  │   joinedAt}                │                                   │
  │◄───────────────────────────│                                   │
```

---

### 7. List Members

**`GET /campaigns/{campaignId}/members`**

```
Client                    Campaign Service              Database
  │                            │                            │
  │  GET /.../members          │                            │
  │───────────────────────────►│                            │
  │                            │  find("campaign.id", id)   │
  │                            │───────────────────────────►│
  │                            │  List<CampaignMemberEntity>│
  │                            │◄───────────────────────────│
  │                            │                            │
  │  200 OK                    │                            │
  │  [{id, userId, role,       │                            │
  │    characterId, joinedAt}] │                            │
  │◄───────────────────────────│                            │
```

---

### 8. Remove Member

**`DELETE /campaigns/{campaignId}/members/{userId}`** — DM can remove anyone; players can only remove themselves.

```
Client                    Campaign Service                     Database
  │                            │                                   │
  │  DELETE /.../members/{uid} │                                   │
  │  ?requesterId=X            │                                   │
  │───────────────────────────►│                                   │
  │                            │  ┌──────────────────────────────┐ │
  │                            │  │ CampaignMemberRemoveService  │ │
  │                            │  │  Validate:                    │ │
  │                            │  │   requester == DM             │ │
  │                            │  │   OR requester == target      │ │
  │                            │  │   → ForbiddenException        │ │
  │                            │  └──────────────────────────────┘ │
  │                            │                                   │
  │                            │  DELETE FROM campaign_members     │
  │                            │  WHERE campaign + userId          │
  │                            │──────────────────────────────────►│
  │  204 No Content            │                                   │
  │◄───────────────────────────│                                   │
```

---

### 9. Create Note

**`POST /campaigns/{campaignId}/notes`** — Any campaign member can create a note with `PUBLIC` or `PRIVATE` visibility.

```
Client                    Campaign Service                Database
  │                            │                              │
  │  POST /.../notes           │                              │
  │  {title, content,          │                              │
  │   visibility: "PUBLIC"}    │                              │
  │───────────────────────────►│                              │
  │                            │  ┌────────────────────────┐  │
  │                            │  │ CreateNoteMapper:       │  │
  │                            │  │  campaignId + userId    │  │
  │                            │  │  + request → NoteCreate │  │
  │                            │  └────────────────────────┘  │
  │                            │                              │
  │                            │  INSERT campaign_notes       │
  │                            │─────────────────────────────►│
  │                            │                              │
  │  201 Created               │                              │
  │  {id, campaignId, authorId,│                              │
  │   title, content,          │                              │
  │   visibility, createdAt}   │                              │
  │◄───────────────────────────│                              │
```

---

### 10. List Notes (Visibility-Filtered)

**`GET /campaigns/{campaignId}/notes`** — Returns `PUBLIC` notes from all members + the requesting user's own `PRIVATE` notes.

```
Client                    Campaign Service                        Database
  │                            │                                      │
  │  GET /.../notes            │                                      │
  │  ?userId=X                 │                                      │
  │───────────────────────────►│                                      │
  │                            │  ┌────────────────────────────────┐  │
  │                            │  │ CampaignNoteFindVisibleService │  │
  │                            │  │                                │  │
  │                            │  │ Query:                          │  │
  │                            │  │  visibility = 'PUBLIC'          │  │
  │                            │  │  OR authorId = userId           │  │
  │                            │  └────────────────────────────────┘  │
  │                            │                                      │
  │                            │  SELECT ... WHERE visibility=       │
  │                            │  'PUBLIC' OR author_id=X             │
  │                            │─────────────────────────────────────►│
  │                            │  List<CampaignNoteEntity>           │
  │                            │◄─────────────────────────────────────│
  │                            │                                      │
  │  200 OK                    │                                      │
  │  [{title, content,         │                                      │
  │    visibility, ...}]       │                                      │
  │◄───────────────────────────│                                      │
```

---

### 11. Update Note

**`PUT /campaigns/{campaignId}/notes/{noteId}`** — Author-only.

```
Client                    Campaign Service                  Database
  │                            │                                │
  │  PUT /.../notes/{noteId}   │                                │
  │  {title, content,          │                                │
  │   visibility}              │                                │
  │───────────────────────────►│                                │
  │                            │  Validate: userId == authorId  │
  │                            │  Selective field update         │
  │                            │  Set updatedAt                 │
  │                            │───────────────────────────────►│
  │  200 OK {NoteVM}           │                                │
  │◄───────────────────────────│                                │
```

---

### 12. Delete Note

**`DELETE /campaigns/{campaignId}/notes/{noteId}`** — Author or DM can delete.

```
Client                    Campaign Service                  Database
  │                            │                                │
  │  DELETE /.../notes/{id}    │                                │
  │  ?userId=X                 │                                │
  │───────────────────────────►│                                │
  │                            │  Validate:                     │
  │                            │   userId == authorId            │
  │                            │   OR userId == dungeonMasterId  │
  │                            │  delete(entity)                 │
  │                            │───────────────────────────────►│
  │  204 No Content            │                                │
  │◄───────────────────────────│                                │
```

---

## Design Patterns

### Hexagonal Architecture (Ports & Adapters)

```
                    ┌───────────────────────────────────┐
   Driving Ports    │         DOMAIN CORE                │   Driven Ports
  ──────────────────│                                     │──────────────────
                    │  Campaign CRUD Services             │
  ResourceImpl ────►│  CampaignMember Services           │────► CampaignCreateRepository (JPA)
  Delegate          │  CampaignNote Services             │────► CampaignMemberAddRepository (JPA)
  Mapper            │                                     │────► CampaignNoteFindVisibleRepo (JPA)
                    │  Enums: CampaignStatus,             │────► ...
                    │    MemberRole, NoteVisibility       │
                    └───────────────────────────────────┘
```

### Delegate Pattern

ResourceImpl handles JAX-RS annotations and JWT extraction; `@Delegate` handles service orchestration and DTO mapping.

### Mapper Chain (Boundary Transformations)

Each layer has dedicated MapStruct mappers:

```
CreateCampaignRequest ──[CampaignCreateMapper]──► CampaignCreate (domain)
                                                        │
                                                  ServiceImpl
                                                        │
                                                        ▼
       CampaignViewModel ◄──[CampaignViewModelMapper]── Campaign (domain)
                                                        │
                  Campaign ◄──[CampaignEntityMapper]── CampaignEntity (JPA)
```

### Aggregate Root Pattern

`CampaignEntity` acts as the **aggregate root**. Members and notes reference it via `@ManyToOne` with cascading deletes — deleting a campaign removes all members and notes.

---

## Authorization Model

| Action | Who Can Perform |
|--------|----------------|
| Create campaign | Any authenticated user |
| Update campaign | `DUNGEON_MASTER` only |
| Delete campaign | `DUNGEON_MASTER` only |
| Add member | `DUNGEON_MASTER` only |
| Remove member | `DUNGEON_MASTER` (anyone) or self |
| Create note | Any campaign member |
| View notes | PUBLIC = all members; PRIVATE = author only |
| Update note | Author only |
| Delete note | Author or `DUNGEON_MASTER` |

---

## Data Model

```
┌─────────────────────────┐
│       campaigns          │
├─────────────────────────┤         ┌──────────────────────────┐
│ id         BIGSERIAL PK │         │   campaign_members       │
│ name       VARCHAR       │         ├──────────────────────────┤
│ description TEXT         │    ┌───►│ id         BIGSERIAL PK │
│ dungeon_master_id BIGINT│    │    │ campaign_id BIGINT FK    │
│ status     VARCHAR(20)  │    │    │ user_id    BIGINT        │
│ max_players INT         │    │    │ character_id BIGINT      │
│ image_url  VARCHAR       │    │    │ role       VARCHAR(20)  │
│ created_at TIMESTAMP    │    │    │ joined_at  TIMESTAMP    │
│ updated_at TIMESTAMP    │────┘    └──────────────────────────┘
│                         │
│                         │────┐    ┌──────────────────────────┐
└─────────────────────────┘    │    │   campaign_notes         │
                               │    ├──────────────────────────┤
                               └───►│ id         BIGSERIAL PK │
                                    │ campaign_id BIGINT FK    │
                                    │ author_id  BIGINT        │
                                    │ title      VARCHAR       │
                                    │ content    TEXT          │
                                    │ visibility VARCHAR(10)  │
                                    │ created_at TIMESTAMP    │
                                    │ updated_at TIMESTAMP    │
                                    └──────────────────────────┘
```
