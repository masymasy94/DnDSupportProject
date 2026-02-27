# Character Service

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.31-blue?style=flat-square&logo=quarkus)](https://quarkus.io/)
[![Port](https://img.shields.io/badge/Port-8082-green?style=flat-square)]()

The **Character Service** manages D&D 5e character creation, editing, listing, PDF import from official WotC character sheets, and PDF sheet generation/download. It automatically computes all derived stats (ability modifiers, proficiency bonus, max HP, spell slots, spellcasting stats) and generates a filled PDF character sheet on every create or update.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Operative Flows](#operative-flows)
  - [Create Character](#1-create-character)
  - [Update Character](#2-update-character)
  - [List Characters (Paginated)](#3-list-characters-paginated)
  - [Import Character from PDF](#4-import-character-from-pdf)
  - [Download Character Sheet (PDF)](#5-download-character-sheet-pdf)
- [Design Patterns](#design-patterns)
- [Stat Calculation Engine](#stat-calculation-engine)
- [PDF Processing](#pdf-processing)
- [Data Model](#data-model)

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────────────────┐
│                      Character Service (:8082)                           │
│                                                                          │
│  ┌─────────────────┐    ┌───────────────────┐    ┌───────────────────┐  │
│  │ Adapter-Inbound │    │      Domain        │    │ Adapter-Outbound  │  │
│  │                 │    │                    │    │                   │  │
│  │ ResourceImpl    │    │ CharacterCreate    │    │ JPA Repositories  │  │
│  │ Delegate        │───►│ Service            │───►│  (Panache)        │  │
│  │ Mapper          │    │                    │    │                   │  │
│  │ PdfParser       │    │ Validation Service │    │ CharacterSheet    │  │
│  │ PdfFieldMapper  │    │ Calculator Services│    │  SaveRepository   │  │
│  └─────────────────┘    └───────────────────┘    └────────┬──────────┘  │
│                                                            │             │
│  ┌───────────────────────────────────────────────────────┐ │             │
│  │ PdfCharacterSheetGenerator (Apache PDFBox)            │ │             │
│  │  Fills WotC 5e template with computed stats           │ │             │
│  └───────────────────────────────────────────────────────┘ │             │
└────────────────────────────────────────────────────────────┼─────────────┘
                                                              │
                                                       ┌────────────┐
                                                       │ PostgreSQL │
                                                       │(character_db)│
                                                       └────────────┘
```

---

## Module Structure

```
character-service/
├── character-service-domain/         # Business logic & domain models
│   ├── model/                        # Records: Character, CharacterCreate, AbilityScores, etc.
│   ├── CharacterCreateService        # Create with stat computation
│   ├── CharacterUpdateService        # Update with recalculation
│   ├── CharacterFindAllService       # Paginated listing
│   ├── CharacterValidationService    # Compendium validation
│   └── calculator/                   # Proficiency, HP, spell slots, modifiers
│
├── character-service-view-model/     # DTOs & Resource interfaces
│   └── vm/                           # Request/Response ViewModels
│
├── character-service-adapter-inbound/# REST controllers (Driving Adapters)
│   ├── create/                       # POST /characters
│   ├── update/                       # PUT /characters/{id}
│   ├── findall/                      # GET /characters
│   ├── importsheet/                  # POST /characters/import-sheet
│   └── sheetdownload/                # GET /characters/{id}/sheet
│
├── character-service-adapter-outbound/# Infrastructure (Driven Adapters)
│   ├── jpa/entity/                   # Panache entities + join tables
│   ├── jpa/repository/               # JPA repository implementations
│   └── pdf/                          # PDF sheet generator (PDFBox)
│
├── character-service-client/         # REST client interface
└── character-service/                # Quarkus bootstrap & configuration
```

---

## API Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/characters` | Bearer (`PLAYER`) | Create a new character |
| `PUT` | `/characters/{id}` | Bearer (`PLAYER`) | Update an existing character |
| `GET` | `/characters` | Bearer (`PLAYER`) | List characters (paginated) |
| `POST` | `/characters/import-sheet` | Bearer (`PLAYER`) | Import from WotC 5e PDF |
| `GET` | `/characters/{id}/sheet` | Bearer (`PLAYER`) | Download generated PDF sheet |

---

## Operative Flows

### 1. Create Character

**`POST /characters`** — Creates a character with full stat computation and automatic PDF sheet generation.

```
Client                  Character Service                                     Database
  │                          │                                                    │
  │  POST /characters        │                                                    │
  │  {name, species,         │                                                    │
  │   class, level,          │                                                    │
  │   abilityScores, ...}    │                                                    │
  │─────────────────────────►│                                                    │
  │                          │                                                    │
  │                          │  ┌──────────────────────────────────────────────┐   │
  │                          │  │ Delegate (Delegate Pattern)                  │   │
  │                          │  │  └► CharacterCreateMapper (ViewModel→Domain) │   │
  │                          │  │  └► Inject userId from JWT                   │   │
  │                          │  └──────────────────────────────────────────────┘   │
  │                          │                                                    │
  │                          │  ┌──────────────────────────────────────────────┐   │
  │                          │  │ CharacterCreateServiceImpl                   │   │
  │                          │  │                                              │   │
  │                          │  │ 1. CharacterValidationService.validate()     │   │
  │                          │  │    → returns ValidatedCompendiumData         │   │
  │                          │  │      (hitDie, baseSpeed)                     │   │
  │                          │  │                                              │   │
  │                          │  │ 2. Compute derived stats:                    │   │
  │                          │  │    ┌─────────────────────────────────────┐   │   │
  │                          │  │    │ proficiencyBonus(level)             │   │   │
  │                          │  │    │ hitDie = getHitDie(class)           │   │   │
  │                          │  │    │ maxHP = hitDieMax + CON mod         │   │   │
  │                          │  │    │       + (level-1)*(avgDie + CON mod)│   │   │
  │                          │  │    │ spellcastingAbility(class)          │   │   │
  │                          │  │    │ spellSaveDC = 8 + prof + mod       │   │   │
  │                          │  │    │ spellAttackBonus = prof + mod       │   │   │
  │                          │  │    └─────────────────────────────────────┘   │   │
  │                          │  │                                              │   │
  │                          │  │ 3. CharacterCreateRepository.save()          │   │
  │                          │  └──────────────────────────────────────────────┘   │
  │                          │                                                    │
  │                          │  save(entity + join tables)                        │
  │                          │───────────────────────────────────────────────────►│
  │                          │  CharacterEntity                                  │
  │                          │◄───────────────────────────────────────────────────│
  │                          │                                                    │
  │                          │  ┌──────────────────────────────────────────────┐   │
  │                          │  │ Post-creation (non-blocking):                │   │
  │                          │  │  PdfCharacterSheetGenerator.generate()       │   │
  │                          │  │  CharacterSheetSaveRepository.saveSheet()    │   │
  │                          │  │  (fire-and-forget — failure doesn't block)   │   │
  │                          │  └──────────────────────────────────────────────┘   │
  │                          │                                                    │
  │  200 OK                  │                                                    │
  │  {id, name, level,       │                                                    │
  │   hitPointsMax: 11,      │                                                    │
  │   proficiencyBonus: 2,   │                                                    │
  │   spellSaveDc: 13, ...}  │                                                    │
  │◄─────────────────────────│                                                    │
```

**Key: Join Table Persistence** — The repository persists the main `CharacterEntity` first, then creates all related entities (languages, skills, saving throws, proficiencies, equipment, spells, spell slots) via their respective join tables.

---

### 2. Update Character

**`PUT /characters/{id}`** — Updates a character, recalculates all derived stats, and regenerates the PDF sheet.

```
Client                  Character Service                           Database
  │                          │                                          │
  │  PUT /characters/{id}    │                                          │
  │  {name, level, ...}      │                                          │
  │─────────────────────────►│                                          │
  │                          │                                          │
  │                          │  ┌────────────────────────────────────┐   │
  │                          │  │ CharacterUpdateServiceImpl         │   │
  │                          │  │  1. Validate (same as create)      │   │
  │                          │  │  2. Recalculate all derived stats  │   │
  │                          │  │  3. Update entity + clear & re-add │   │
  │                          │  │     all collections (orphanRemoval)│   │
  │                          │  └────────────────────────────────────┘   │
  │                          │                                          │
  │                          │  UPDATE + flush + re-add collections     │
  │                          │─────────────────────────────────────────►│
  │                          │                                          │
  │                          │  Regenerate PDF (non-blocking)           │
  │                          │─────────────────────────────────────────►│
  │                          │                                          │
  │  200 OK {CharacterVM}    │                                          │
  │◄─────────────────────────│                                          │
```

The update validates ownership (`userId` must match) and uses the **orphanRemoval** pattern: clears all collection entities, flushes, then re-adds them from the updated input.

---

### 3. List Characters (Paginated)

**`GET /characters?page=0&size=20`** — Returns a paginated list of character summaries.

```
Client                   Character Service              Database
  │                           │                             │
  │  GET /characters          │                             │
  │  ?page=0&size=20          │                             │
  │──────────────────────────►│                             │
  │                           │  Panache: count + page()    │
  │                           │────────────────────────────►│
  │                           │  List<CharacterEntity>      │
  │                           │◄────────────────────────────│
  │                           │                             │
  │                           │  ┌────────────────────────┐ │
  │                           │  │ CharacterSummaryMapper  │ │
  │                           │  │ Entity → CharacterSummary│ │
  │                           │  │ (id, name, class,       │ │
  │                           │  │  level, HP, AC)         │ │
  │                           │  └────────────────────────┘ │
  │                           │                             │
  │  200 OK                   │                             │
  │  {content: [...],         │                             │
  │   page, size,             │                             │
  │   totalElements,          │                             │
  │   totalPages}             │                             │
  │◄──────────────────────────│                             │
```

---

### 4. Import Character from PDF

**`POST /characters/import-sheet`** — Imports a character by parsing form fields from an official WotC D&D 5e fillable PDF sheet using Apache PDFBox.

```
Client                  Character Service                                    Database
  │                          │                                                   │
  │  POST /import-sheet      │                                                   │
  │  Content-Type:           │                                                   │
  │  multipart/form-data     │                                                   │
  │  (file: *.pdf)           │                                                   │
  │─────────────────────────►│                                                   │
  │                          │                                                   │
  │                          │  ┌──────────────────────────────────────────────┐  │
  │                          │  │ CharacterImportSheetDelegate                 │  │
  │                          │  │                                              │  │
  │                          │  │ 1. Validate file (PDF, non-empty)            │  │
  │                          │  │                                              │  │
  │                          │  │ 2. PdfCharacterSheetParser (PDFBox)          │  │
  │                          │  │    └► Load PDF → extract AcroForm fields     │  │
  │                          │  │    └► Returns Map<String, String>            │  │
  │                          │  │                                              │  │
  │                          │  │ 3. PdfFieldToCharacterMapper                 │  │
  │                          │  │    ┌─────────────────────────────────────┐   │  │
  │                          │  │    │ Map WotC field names to domain:    │   │  │
  │                          │  │    │  "CharacterName" → name            │   │  │
  │                          │  │    │  "ClassLevel"    → parse class+lvl │   │  │
  │                          │  │    │  "Race"          → species         │   │  │
  │                          │  │    │  "STR"/"DEX"/... → abilityScores   │   │  │
  │                          │  │    │  "Check Box 23-40" → skills        │   │  │
  │                          │  │    │  "Check Box 11-16" → saves         │   │  │
  │                          │  │    │  "Spells *"      → spell list      │   │  │
  │                          │  │    └─────────────────────────────────────┘   │  │
  │                          │  │    └► Returns CharacterCreate               │  │
  │                          │  │                                              │  │
  │                          │  │ 4. CharacterCreateService.create()           │  │
  │                          │  │    (reuses the full creation flow with       │  │
  │                          │  │     validation + stat computation)           │  │
  │                          │  │                                              │  │
  │                          │  │ 5. Save original PDF as sheet                │  │
  │                          │  └──────────────────────────────────────────────┘  │
  │                          │                                                   │
  │                          │  persist character + save PDF bytes               │
  │                          │──────────────────────────────────────────────────►│
  │                          │                                                   │
  │  200 OK {CharacterVM}    │                                                   │
  │◄─────────────────────────│                                                   │
```

---

### 5. Download Character Sheet (PDF)

**`GET /characters/{id}/sheet`** — Downloads the auto-generated PDF character sheet.

```
Client                   Character Service              Database
  │                           │                             │
  │  GET /characters/{id}/    │                             │
  │      sheet                │                             │
  │──────────────────────────►│                             │
  │                           │  findByCharacterId(id)      │
  │                           │────────────────────────────►│
  │                           │  CharacterSheetData         │
  │                           │  {fileName, contentType,    │
  │                           │   fileSize, pdfData}        │
  │                           │◄────────────────────────────│
  │                           │                             │
  │  200 OK                   │                             │
  │  Content-Type:            │                             │
  │    application/pdf        │                             │
  │  Content-Disposition:     │                             │
  │    attachment              │                             │
  │  <PDF bytes>              │                             │
  │◄──────────────────────────│                             │
```

---

## Design Patterns

### Hexagonal Architecture (Ports & Adapters)

Domain defines service and repository interfaces. Adapters provide the implementations:

```
                    ┌───────────────────────────────────┐
   Driving Ports    │         DOMAIN CORE                │   Driven Ports
  ──────────────────│                                     │──────────────────
                    │  CharacterCreateService             │
  ResourceImpl ────►│  CharacterUpdateService             │────► CharacterCreateRepository (JPA)
  Delegate          │  CharacterFindAllService            │────► CharacterUpdateRepository (JPA)
  PdfParser         │  CharacterValidationService         │────► CharacterFindAllRepository (JPA)
  PdfFieldMapper    │  Calculator Services (6 interfaces) │────► CharacterSheetSaveRepository (JPA)
                    └───────────────────────────────────┘
```

### Delegate Pattern

`ResourceImpl` handles JAX-RS annotations; the `@Delegate` handles business logic, including **non-blocking PDF generation** after creation/update (fire-and-forget with try-catch).

### Strategy Pattern (Calculator Services)

Stat calculation is split into focused **strategy interfaces**, all implemented by `CharacterCalculatorServiceImpl`:

| Interface | Responsibility |
|-----------|---------------|
| `CharacterProficiencyBonusCalculator` | Level → proficiency bonus (+2 to +6) |
| `CharacterModifierCalculator` | Score → modifier `(score - 10) / 2` |
| `CharacterMaxHpCalculator` | Hit die + CON mod + level scaling |
| `CharacterHitDieProvider` | Class → hit die (d6–d12) |
| `CharacterSpellcastingAbilityProvider` | Class → spellcasting ability (INT/WIS/CHA) |
| `CharacterBaseSpeedProvider` | Species → base speed (25/30 ft) |
| `CharacterSpellSlotsCalculator` | Class + level → spell slot allocations |

### Collection Management (Orphan Removal)

Character updates use JPA's `orphanRemoval=true` pattern:
1. Clear all child collections (skills, spells, equipment, etc.)
2. Flush to execute DELETEs
3. Re-add all updated collections
4. Flush to execute INSERTs

This ensures clean replacement without complex diffing logic.

---

## Stat Calculation Engine

```
                        CharacterCreate Input
                               │
                ┌──────────────┼──────────────┐
                │              │              │
                ▼              ▼              ▼
        ┌─────────────┐ ┌──────────┐ ┌──────────────┐
        │ Proficiency  │ │ Hit Die  │ │ Spellcasting │
        │ Bonus        │ │ Provider │ │ Ability      │
        │ Calculator   │ │          │ │ Provider     │
        └──────┬──────┘ └────┬─────┘ └──────┬───────┘
               │             │              │
               ▼             ▼              ▼
        ┌─────────────────────────────────────────┐
        │          Max HP Calculator               │
        │  hitDieMax + CON mod                     │
        │  + (level-1) * (avgDie + CON mod)        │
        └─────────────────────────────────────────┘
               │
               ▼
        ┌─────────────────────────────────────────┐
        │  spellSaveDC = 8 + profBonus + mod       │
        │  spellAttackBonus = profBonus + mod       │
        │  AC = 10 + DEX mod                       │
        └─────────────────────────────────────────┘
               │
               ▼
          Persisted Character
```

---

## PDF Processing

### PDF Import (Apache PDFBox)

The `PdfCharacterSheetParser` loads the PDF, extracts `AcroForm` fields, and `PdfFieldToCharacterMapper` maps WotC field names to the `CharacterCreate` domain model:

| PDF Form Field | Domain Field |
|---------------|-------------|
| `CharacterName` | `name` |
| `ClassLevel` | Parsed → `characterClass` + `level` |
| `Race` | `species` |
| `STR`, `DEX`, `CON`, `INT`, `WIS`, `CHA` | `abilityScores` |
| `Check Box 23–40` | `skillProficiencies` (18 skills, alphabetical) |
| `Check Box 11–16` | `savingThrowProficiencies` |
| `Equipment` | `equipment` (split by newline) |
| `Spells *` | `spells` |

### PDF Generation

The `PdfCharacterSheetGenerator` loads the WotC 5e template (`META-INF/resources/wotc-5e-sheet.pdf`) and fills all form fields with computed values — ability modifiers, skill modifiers, spell slots, combat stats, and personality traits.

---

## Data Model

```
┌─────────────────────────┐       ┌──────────────────────────┐
│      characters          │       │   character_sheets       │
├─────────────────────────┤       ├──────────────────────────┤
│ id         BIGSERIAL PK │       │ id           BIGSERIAL PK│
│ user_id    BIGINT        │       │ character_id BIGINT FK   │
│ name       VARCHAR(100)  │       │ file_name    VARCHAR     │
│ species    VARCHAR(50)   │       │ content_type VARCHAR     │
│ character_class VARCHAR  │       │ file_size    BIGINT      │
│ level      INT           │       │ pdf_data     BYTEA       │
│ strength   INT           │       └──────────────────────────┘
│ dexterity  INT           │
│ constitution INT         │       ┌──────────────────────────┐
│ intelligence INT         │       │  character_languages     │
│ wisdom     INT           │       │  character_skills        │
│ charisma   INT           │       │  character_saving_throws │
│ hp_current INT           │       │  character_proficiencies │
│ hp_max     INT           │       │  character_equipment     │
│ hp_temp    INT           │       │  character_spells        │
│ armor_class INT          │       │  character_spell_slots   │
│ speed      INT           │       │  character_features      │
│ hit_dice_total INT       │       └──────────────────────────┘
│ hit_dice_type VARCHAR    │            ▲ OneToMany (cascade,
│ proficiency_bonus INT    │              orphanRemoval=true)
│ spell_ability VARCHAR    │
│ spell_save_dc INT        │
│ spell_attack_bonus INT   │
│ created_at TIMESTAMP     │
│ updated_at TIMESTAMP     │
└─────────────────────────┘
```
