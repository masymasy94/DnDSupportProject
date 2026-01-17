-- =============================================
-- Compendium Service Database Schema
-- D&D 5E Reference Data with Homebrew Support
-- =============================================

-- =============================================
-- Static Reference Tables (no homebrew)
-- =============================================

-- Alignments (9 D&D alignments)
CREATE TABLE IF NOT EXISTS alignments (
    id SMALLSERIAL PRIMARY KEY,
    code VARCHAR(2) NOT NULL UNIQUE,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Abilities (6 ability scores)
CREATE TABLE IF NOT EXISTS abilities (
    id SMALLSERIAL PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(15) NOT NULL UNIQUE
);

-- Skills (18 skills linked to abilities)
CREATE TABLE IF NOT EXISTS skills (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    ability_id SMALLINT NOT NULL REFERENCES abilities(id)
);

CREATE INDEX IF NOT EXISTS idx_skills_ability_id ON skills(ability_id);

-- Proficiency Types
CREATE TABLE IF NOT EXISTS proficiency_types (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Armor Types
CREATE TABLE IF NOT EXISTS armor_types (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

-- Weapon Types
CREATE TABLE IF NOT EXISTS weapon_types (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    category VARCHAR(10) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_weapon_types_category ON weapon_types(category);

-- Tool Types
CREATE TABLE IF NOT EXISTS tool_types (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    category VARCHAR(20) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_tool_types_category ON tool_types(category);

-- Languages
CREATE TABLE IF NOT EXISTS languages (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    script VARCHAR(20),
    type VARCHAR(10) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_languages_type ON languages(type);

-- Spell Schools (8 schools of magic)
CREATE TABLE IF NOT EXISTS spell_schools (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- =============================================
-- Tables with Homebrew Support
-- =============================================

-- Species/Races (with homebrew support)
CREATE TABLE IF NOT EXISTS species (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    source VARCHAR(10) NOT NULL DEFAULT 'OFFICIAL',
    owner_id BIGINT,
    campaign_id BIGINT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_species_name_owner ON species(name, COALESCE(owner_id, -1::BIGINT));
CREATE INDEX IF NOT EXISTS idx_species_source ON species(source);
CREATE INDEX IF NOT EXISTS idx_species_owner ON species(owner_id);
CREATE INDEX IF NOT EXISTS idx_species_campaign ON species(campaign_id);
CREATE INDEX IF NOT EXISTS idx_species_name ON species(name);

-- Character Classes (with homebrew support)
CREATE TABLE IF NOT EXISTS character_classes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    hit_die VARCHAR(3) NOT NULL,
    description TEXT,
    source VARCHAR(10) NOT NULL DEFAULT 'OFFICIAL',
    owner_id BIGINT,
    campaign_id BIGINT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_class_name_owner ON character_classes(name, COALESCE(owner_id, -1::BIGINT));
CREATE INDEX IF NOT EXISTS idx_classes_source ON character_classes(source);
CREATE INDEX IF NOT EXISTS idx_classes_owner ON character_classes(owner_id);
CREATE INDEX IF NOT EXISTS idx_classes_name ON character_classes(name);

-- Backgrounds (with homebrew support)
CREATE TABLE IF NOT EXISTS backgrounds (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    source VARCHAR(10) NOT NULL DEFAULT 'OFFICIAL',
    owner_id BIGINT,
    campaign_id BIGINT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_background_name_owner ON backgrounds(name, COALESCE(owner_id, -1::BIGINT));
CREATE INDEX IF NOT EXISTS idx_backgrounds_source ON backgrounds(source);
CREATE INDEX IF NOT EXISTS idx_backgrounds_owner ON backgrounds(owner_id);
CREATE INDEX IF NOT EXISTS idx_backgrounds_name ON backgrounds(name);

-- Spells (with homebrew support)
CREATE TABLE IF NOT EXISTS spells (
    id SERIAL PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    level SMALLINT NOT NULL CHECK (level BETWEEN 0 AND 9),
    school_id SMALLINT NOT NULL REFERENCES spell_schools(id),
    casting_time VARCHAR(50) NOT NULL,
    spell_range VARCHAR(50) NOT NULL,
    components VARCHAR(15) NOT NULL,
    material_components TEXT,
    duration VARCHAR(50) NOT NULL,
    concentration BOOLEAN DEFAULT FALSE,
    ritual BOOLEAN DEFAULT FALSE,
    description TEXT,
    higher_levels TEXT,
    source VARCHAR(10) NOT NULL DEFAULT 'OFFICIAL',
    owner_id BIGINT,
    campaign_id BIGINT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_spell_name_owner ON spells(name, COALESCE(owner_id, -1::BIGINT));
CREATE INDEX IF NOT EXISTS idx_spells_level ON spells(level);
CREATE INDEX IF NOT EXISTS idx_spells_school ON spells(school_id);
CREATE INDEX IF NOT EXISTS idx_spells_source ON spells(source);
CREATE INDEX IF NOT EXISTS idx_spells_owner ON spells(owner_id);
CREATE INDEX IF NOT EXISTS idx_spells_name ON spells(name);
CREATE INDEX IF NOT EXISTS idx_spells_concentration ON spells(concentration);
CREATE INDEX IF NOT EXISTS idx_spells_ritual ON spells(ritual);

-- Spell class associations (which classes can cast which spells)
CREATE TABLE IF NOT EXISTS spell_class_lists (
    spell_id INTEGER NOT NULL REFERENCES spells(id) ON DELETE CASCADE,
    class_id INTEGER NOT NULL REFERENCES character_classes(id) ON DELETE CASCADE,
    PRIMARY KEY (spell_id, class_id)
);

CREATE INDEX IF NOT EXISTS idx_spell_class_spell ON spell_class_lists(spell_id);
CREATE INDEX IF NOT EXISTS idx_spell_class_class ON spell_class_lists(class_id);
