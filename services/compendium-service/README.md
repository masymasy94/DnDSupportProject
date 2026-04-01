# Compendium Service

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.31-blue?style=flat-square&logo=quarkus)](https://quarkus.io/)
[![Port](https://img.shields.io/badge/Port-8090-green?style=flat-square)]()

The **Compendium Service** provides the D&D 5e reference data layer (SRD content). It exposes read-only APIs for monsters, spells, classes, species, equipment, feats, magic items, and other reference entities. Frequently accessed reference lists are cached at the REST layer using **Quarkus Cache** (backed by Redis).

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Operative Flows](#operative-flows)
  - [List Reference Data (Cached)](#1-list-reference-data-cached)
  - [Find by ID](#2-find-by-id)
  - [Search Monsters (Filtered + Paginated)](#3-search-monsters-filtered--paginated)
  - [Search Spells (Filtered + Paginated)](#4-search-spells-filtered--paginated)
- [Design Patterns](#design-patterns)
- [Caching Strategy](#caching-strategy)
- [Dynamic Query Filtering](#dynamic-query-filtering)
- [Entity Catalog](#entity-catalog)

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────────────────┐
│                     Compendium Service (:8090)                           │
│                                                                          │
│  ┌─────────────────┐    ┌───────────────────┐    ┌───────────────────┐  │
│  │ Adapter-Inbound │    │      Domain        │    │ Adapter-Outbound  │  │
│  │                 │    │                    │    │                   │  │
│  │ ResourceImpl    │    │ FindAll / FindById │    │ JPA Repositories  │  │
│  │  @CacheResult   │───►│ Service Interfaces │───►│  (Panache)        │  │
│  │ Delegate        │    │ FilterCriteria     │    │ MapStruct Mappers │  │
│  │ Mapper          │    │ Domain Records     │    │                   │  │
│  └─────────────────┘    └───────────────────┘    └────────┬──────────┘  │
│                                                            │             │
└────────────────────────────────────────────────────────────┼─────────────┘
                                                              │
                                               ┌──────────────┼────────┐
                                               │              │        │
                                               ▼              ▼        │
                                        ┌────────────┐  ┌────────┐    │
                                        │ PostgreSQL │  │ Redis  │    │
                                        │(compendium)│  │(cache) │    │
                                        └────────────┘  └────────┘    │
```

---

## Module Structure

```
compendium-service/
├── compendium-service-domain/         # Business logic & domain models
│   ├── model/                         # Records: Monster, Spell, CharacterClass, Species, etc.
│   ├── filter/                        # SpellFilterCriteria, MonsterFilterCriteria
│   ├── repository/                    # Repository interfaces (ports)
│   └── impl/                          # Service implementations
│
├── compendium-service-view-model/     # DTOs & Resource interfaces
│   └── vm/                            # ViewModels with @Schema (OpenAPI)
│
├── compendium-service-adapter-inbound/# REST controllers (Driving Adapters)
│   ├── alignment/                     # GET /api/compendium/alignments
│   ├── monster/                       # GET /api/compendium/monsters (filtered)
│   ├── spell/                         # GET /api/compendium/spells (filtered)
│   ├── characterclass/                # GET /api/compendium/character-classes
│   ├── species/                       # GET /api/compendium/species
│   ├── background/                    # GET /api/compendium/backgrounds
│   ├── equipment/                     # GET /api/compendium/equipment
│   ├── feat/                          # GET /api/compendium/feats
│   ├── magicitem/                     # GET /api/compendium/magic-items
│   ├── skill/                         # GET /api/compendium/skills
│   └── ... (15+ entity types)
│
├── compendium-service-adapter-outbound/# Infrastructure (Driven Adapters)
│   └── jpa/                           # Panache entities, repos, MapStruct mappers
│
├── compendium-service-client/         # REST client interface
└── compendium-service/                # Quarkus bootstrap & configuration
```

---

## API Endpoints

### Reference Data (Cached Lists)

| Method | Path | Cache | Description |
|--------|------|-------|-------------|
| `GET` | `/api/compendium/alignments` | `alignments-cache` | All alignments |
| `GET` | `/api/compendium/character-classes` | `character-classes-cache` | All character classes |
| `GET` | `/api/compendium/species` | `species-cache` | All playable species |
| `GET` | `/api/compendium/backgrounds` | `backgrounds-cache` | All backgrounds |
| `GET` | `/api/compendium/skills` | `skills-cache` | All skills |
| `GET` | `/api/compendium/languages` | `languages-cache` | All languages |
| `GET` | `/api/compendium/damage-types` | `damage-types-cache` | All damage types |
| `GET` | `/api/compendium/conditions` | `conditions-cache` | All conditions |
| `GET` | `/api/compendium/spell-schools` | `spell-schools-cache` | All spell schools |
| `GET` | `/api/compendium/armor-types` | `armor-types-cache` | All armor types |
| `GET` | `/api/compendium/weapon-types` | `weapon-types-cache` | All weapon types |
| `GET` | `/api/compendium/tool-types` | `tool-types-cache` | All tool types |
| `GET` | `/api/compendium/proficiency-types` | `proficiency-types-cache` | All proficiency types |

### Filtered & Paginated Queries

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/compendium/monsters` | Monsters (filtered by name, type, size, CR, alignment) |
| `GET` | `/api/compendium/spells` | Spells (filtered by search, level, school, concentration, ritual) |
| `GET` | `/api/compendium/equipment` | Equipment (paginated) |
| `GET` | `/api/compendium/feats` | Feats (paginated) |
| `GET` | `/api/compendium/magic-items` | Magic items (paginated) |

### Single Entity Lookups

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/compendium/alignments/{id}` | Alignment by ID |
| `GET` | `/api/compendium/monsters/{id}` | Monster by ID |
| `GET` | `/api/compendium/spells/{id}` | Spell by ID |
| `GET` | `/api/compendium/character-classes/{id}` | Class by ID |
| `GET` | `/api/compendium/species/{id}` | Species by ID |

> All endpoints require **Bearer JWT** with `PLAYER` role.

---

## Operative Flows

### 1. List Reference Data (Cached)

**`GET /api/compendium/alignments`** — Returns all alignments. The result is cached at the REST layer, avoiding database hits on subsequent requests.

```
Client                   Compendium Service                        Redis        Database
  │                           │                                       │              │
  │  GET /alignments          │                                       │              │
  │  Authorization: Bearer    │                                       │              │
  │──────────────────────────►│                                       │              │
  │                           │                                       │              │
  │                           │  ┌──────────────────────────────────┐  │              │
  │                           │  │ AlignmentFindAllResourceImpl     │  │              │
  │                           │  │  @CacheResult("alignments-cache")│  │              │
  │                           │  └──────────────────────────────────┘  │              │
  │                           │                                       │              │
  │                           │  Cache lookup                         │              │
  │                           │──────────────────────────────────────►│              │
  │                           │                                       │              │
  │                           │  ┌─── Cache HIT ───┐  ┌─── Cache MISS ───┐          │
  │                           │  │ Return cached    │  │                   │          │
  │                           │  │ response         │  │ Delegate.findAll()│          │
  │                           │  └─────────────────┘  │  └► Service        │          │
  │                           │                        │      └► Repository │          │
  │                           │                        │         .findAll() │          │
  │                           │                        │────────────────────────────►│
  │                           │                        │  List<AlignmentEntity>      │
  │                           │                        │◄────────────────────────────│
  │                           │                        │                   │          │
  │                           │                        │ AlignmentMapper   │          │
  │                           │                        │ Entity → Domain   │          │
  │                           │                        │ AlignmentVMMapper │          │
  │                           │                        │ Domain → ViewModel│          │
  │                           │                        │                   │          │
  │                           │                        │ Store in cache    │          │
  │                           │                        │──────────────────►│          │
  │                           │                        └───────────────────┘          │
  │                           │                                       │              │
  │  200 OK                   │                                       │              │
  │  [{id, code, name}, ...]  │                                       │              │
  │◄──────────────────────────│                                       │              │
```

This pattern applies to all simple list endpoints (alignments, classes, species, skills, languages, etc.). The **Cache-Aside Pattern** is implemented via `@CacheResult` on the `ResourceImpl` — the cache is checked before the delegate is invoked.

---

### 2. Find by ID

**`GET /api/compendium/spells/{id}`** — Direct lookup without caching.

```
Client                   Compendium Service               Database
  │                           │                               │
  │  GET /spells/{id}         │                               │
  │──────────────────────────►│                               │
  │                           │  Delegate → Service           │
  │                           │  → Repository.findById(id)    │
  │                           │──────────────────────────────►│
  │                           │  Optional<SpellEntity>        │
  │                           │◄──────────────────────────────│
  │                           │                               │
  │                           │  SpellMapper (Entity→Domain)  │
  │                           │  SpellViewModelMapper         │
  │                           │  (Domain→ViewModel)           │
  │                           │                               │
  │  200 OK {SpellVM}         │                               │
  │◄──────────────────────────│                               │
  │  (or 404 Not Found)       │                               │
```

---

### 3. Search Monsters (Filtered + Paginated)

**`GET /api/compendium/monsters?type=Dragon&size=Huge&page=0&pageSize=10`** — Dynamic query filtering with the **Criteria Pattern**.

```
Client                    Compendium Service                              Database
  │                            │                                              │
  │  GET /monsters             │                                              │
  │  ?type=Dragon&size=Huge    │                                              │
  │  &page=0&pageSize=10       │                                              │
  │───────────────────────────►│                                              │
  │                            │                                              │
  │                            │  ┌────────────────────────────────────────┐   │
  │                            │  │ MonsterFindAllDelegate                 │   │
  │                            │  │  Construct MonsterFilterCriteria:      │   │
  │                            │  │   name, type, size, CR, alignment,    │   │
  │                            │  │   page, pageSize                      │   │
  │                            │  └────────────────────────────────────────┘   │
  │                            │                                              │
  │                            │  ┌────────────────────────────────────────┐   │
  │                            │  │ MonsterFindAllRepositoryImpl          │   │
  │                            │  │                                        │   │
  │                            │  │ 1. QueryFilterUtils.create(criteria)   │   │
  │                            │  │    → builds dynamic HQL + params       │   │
  │                            │  │    → maps "type" → "monsterType.name"  │   │
  │                            │  │    → maps "size" → "monsterSize.name"  │   │
  │                            │  │                                        │   │
  │                            │  │ 2. find(query, Sort, params)           │   │
  │                            │  │ 3. .count() for totalElements          │   │
  │                            │  │ 4. .page(page, pageSize).list()        │   │
  │                            │  │ 5. Stream through MonsterMapper        │   │
  │                            │  └────────────────────────────────────────┘   │
  │                            │                                              │
  │                            │  SELECT + COUNT with dynamic WHERE           │
  │                            │─────────────────────────────────────────────►│
  │                            │  List<MonsterEntity> + count                 │
  │                            │◄─────────────────────────────────────────────│
  │                            │                                              │
  │  200 OK                    │                                              │
  │  {content: [{name,         │                                              │
  │    type, CR, HP, ...}],    │                                              │
  │   page, size,              │                                              │
  │   totalElements,           │                                              │
  │   totalPages}              │                                              │
  │◄───────────────────────────│                                              │
```

---

### 4. Search Spells (Filtered + Paginated)

**`GET /api/compendium/spells?search=fire&level=3&school=Evocation&concentration=true`**

```
Client                    Compendium Service                              Database
  │                            │                                              │
  │  GET /spells               │                                              │
  │  ?search=fire&level=3      │                                              │
  │  &concentration=true       │                                              │
  │───────────────────────────►│                                              │
  │                            │                                              │
  │                            │  ┌────────────────────────────────────────┐   │
  │                            │  │ SpellFindAllDelegate                  │   │
  │                            │  │  Construct SpellFilterCriteria:       │   │
  │                            │  │   search, levels[], schools[],       │   │
  │                            │  │   concentration, ritual, page, size  │   │
  │                            │  └────────────────────────────────────────┘   │
  │                            │                                              │
  │                            │  ┌────────────────────────────────────────┐   │
  │                            │  │ SpellFindAllRepositoryImpl            │   │
  │                            │  │  QueryFilterUtils.create(criteria)    │   │
  │                            │  │   → "search" uses LIKE (name ILIKE)  │   │
  │                            │  │   → "levels" uses IN clause          │   │
  │                            │  │   → "schools" uses IN clause         │   │
  │                            │  │   → booleans use equality            │   │
  │                            │  │                                        │   │
  │                            │  │  Sort by level ASC, name ASC           │   │
  │                            │  └────────────────────────────────────────┘   │
  │                            │                                              │
  │                            │  Dynamic HQL query                          │
  │                            │─────────────────────────────────────────────►│
  │                            │                                              │
  │  200 OK                    │                                              │
  │  {content: [{name,         │                                              │
  │    level, school,          │                                              │
  │    concentration, ...}],   │                                              │
  │   page, totalElements}     │                                              │
  │◄───────────────────────────│                                              │
```

---

## Design Patterns

### Hexagonal Architecture (Ports & Adapters)

```
                     ┌───────────────────────────────────┐
    Driving Ports    │         DOMAIN CORE                │   Driven Ports
  ───────────────────│                                     │──────────────────
                     │  FindAll/FindById Service Interfaces│
  ResourceImpl ─────►│  FilterCriteria Records             │────► FindAll/FindById Repos (JPA)
  @CacheResult       │  Domain Model Records (@Builder)    │
  Delegate           │  QueryFilterUtils                   │
  ViewModelMapper    │                                     │
                     └───────────────────────────────────┘
```

### Delegate Pattern

Each endpoint pair (ResourceImpl + Delegate) follows the same split:
- **ResourceImpl**: `@Path`, `@GET`, `@RolesAllowed("PLAYER")`, `@CacheResult`
- **Delegate** (`@Delegate`, `@RequestScoped`): Builds criteria, calls service, maps result

### Mapper Chain (Two-Tier)

```
JPA Entity ──[OutboundMapper]──► Domain Record ──[InboundMapper]──► ViewModel
 SpellEntity    SpellMapper         Spell       SpellViewModelMapper  SpellViewModel
```

All mappers implement `Function<A, B>` and handle relationships (e.g., `spellSchool.name` → `school`).

### Criteria Pattern (Dynamic Query Building)

`FilterCriteria` records define filter fields. `QueryFilterUtils.create(criteria)` dynamically constructs HQL queries:

```
MonsterFilterCriteria { type="Dragon", size="Huge" }
          │
          ▼
QueryFilterUtils.create()
          │
          ▼
Query:  "monsterType.name = :type AND monsterSize.name = :size"
Params: { type: "Dragon", size: "Huge" }
```

### Cache-Aside Pattern

Simple list endpoints use `@CacheResult(cacheName = "X-cache")` on the ResourceImpl. The cache is checked **before** the delegate is called. Cache invalidation is TTL-based (Redis).

---

## Caching Strategy

| Endpoint | Cache Name | Rationale |
|----------|-----------|-----------|
| Alignments | `alignments-cache` | Static data, rarely changes |
| Character Classes | `character-classes-cache` | Static data |
| Species | `species-cache` | Static data |
| Skills | `skills-cache` | Static data |
| Languages | `languages-cache` | Static data |
| Damage Types | `damage-types-cache` | Static data |
| Conditions | `conditions-cache` | Static data |
| Spell Schools | `spell-schools-cache` | Static data |

> Monsters and spells are **not cached** because they support dynamic filters and pagination.

---

## Dynamic Query Filtering

### Monster Filters

| Parameter | Query Mapping | Example |
|-----------|-------------|---------|
| `name` | `name ILIKE :name` | `?name=dragon` |
| `type` | `monsterType.name = :type` | `?type=Dragon` |
| `size` | `monsterSize.name = :size` | `?size=Huge` |
| `challengeRating` | `challengeRating = :cr` | `?challengeRating=17` |
| `alignment` | `alignment ILIKE :alignment` | `?alignment=chaotic evil` |

### Spell Filters

| Parameter | Query Mapping | Example |
|-----------|-------------|---------|
| `search` | `name ILIKE :search` | `?search=fire` |
| `level` | `level IN :levels` | `?level=3` |
| `school` | `spellSchool.name IN :schools` | `?school=Evocation` |
| `concentration` | `concentration = :conc` | `?concentration=true` |
| `ritual` | `ritual = :ritual` | `?ritual=true` |

---

## Entity Catalog

| Entity | Fields | Source |
|--------|--------|--------|
| **Monster** | name, size, type, alignment, AC, HP, speed, ability scores, CR, XP, actions (JSONB) | SRD + Homebrew |
| **Spell** | name, level, school, casting time, range, components, duration, concentration, ritual, description | SRD + Homebrew |
| **CharacterClass** | name, hit die, description | SRD |
| **Species** | name, base speed, traits | SRD |
| **Equipment** | name, category, cost, weight, properties | SRD |
| **MagicItem** | name, rarity, type, attunement, description | SRD |
| **Feat** | name, prerequisite, description | SRD |
| **Alignment** | code (e.g., "LG"), name (e.g., "Lawful Good") | SRD |
| **Skill** | name, ability | SRD |

All complex entities support `source` (OFFICIAL / HOMEBREW), `ownerId`, `campaignId`, and `isPublic` fields for future homebrew content support.
