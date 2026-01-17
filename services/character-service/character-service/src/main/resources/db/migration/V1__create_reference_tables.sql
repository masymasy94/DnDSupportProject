-- =============================================
-- D&D 5E Reference Tables
-- Must be created BEFORE characters tables
-- =============================================

-- Alignments (9 alignments)
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

-- Species/Races
CREATE TABLE IF NOT EXISTS species (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    description TEXT
);

-- Character Classes
CREATE TABLE IF NOT EXISTS character_classes (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    hit_die VARCHAR(3) NOT NULL,
    description TEXT
);

-- Backgrounds
CREATE TABLE IF NOT EXISTS backgrounds (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    description TEXT
);

-- Skills (18 skills linked to abilities)
CREATE TABLE IF NOT EXISTS skills (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    ability_id SMALLINT NOT NULL REFERENCES abilities(id)
);

CREATE INDEX IF NOT EXISTS idx_skills_ability_id ON skills(ability_id);

-- Proficiency Types (ARMOR, WEAPON, TOOL, LANGUAGE)
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
    name VARCHAR(40) NOT NULL UNIQUE,
    category VARCHAR(20) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_tool_types_category ON tool_types(category);

-- Languages
CREATE TABLE IF NOT EXISTS languages (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    script VARCHAR(20),
    type VARCHAR(10) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_languages_type ON languages(type);

-- =============================================
-- Spellcasting Reference Tables
-- =============================================

-- Spell Schools (8 schools of magic)
CREATE TABLE IF NOT EXISTS spell_schools (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Spells (D&D 5E spell compendium)
CREATE TABLE IF NOT EXISTS spells (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    level SMALLINT NOT NULL,  -- 0 for cantrips, 1-9 for leveled spells
    school_id SMALLINT NOT NULL REFERENCES spell_schools(id),
    casting_time VARCHAR(30) NOT NULL,
    spell_range VARCHAR(30) NOT NULL,
    components VARCHAR(10) NOT NULL,  -- V, S, M combinations
    material_components TEXT,
    duration VARCHAR(30) NOT NULL,
    concentration BOOLEAN DEFAULT FALSE,
    ritual BOOLEAN DEFAULT FALSE,
    description TEXT
);

CREATE INDEX IF NOT EXISTS idx_spells_level ON spells(level);
CREATE INDEX IF NOT EXISTS idx_spells_school_id ON spells(school_id);
