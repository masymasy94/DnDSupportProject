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
-- Monster Reference Tables
-- =============================================

-- Monster Types (14 types in D&D 5E)
CREATE TABLE IF NOT EXISTS monster_types (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

-- Monster Sizes (6 sizes in D&D 5E)
CREATE TABLE IF NOT EXISTS monster_sizes (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(15) NOT NULL UNIQUE,
    space VARCHAR(20) NOT NULL
);

-- Damage Types (for resistances/immunities/vulnerabilities)
CREATE TABLE IF NOT EXISTS damage_types (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

-- Condition Types (for immunities)
CREATE TABLE IF NOT EXISTS condition_types (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

-- =============================================
-- Magic Item Reference Tables
-- =============================================

-- Magic Item Rarity (6 rarities)
CREATE TABLE IF NOT EXISTS magic_item_rarities (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Magic Item Types
CREATE TABLE IF NOT EXISTS magic_item_types (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

-- Equipment Categories
CREATE TABLE IF NOT EXISTS equipment_categories (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
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

-- Monsters (with homebrew support)
CREATE TABLE IF NOT EXISTS monsters (
    id SERIAL PRIMARY KEY,
    index VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    size_id SMALLINT NOT NULL REFERENCES monster_sizes(id),
    type_id SMALLINT NOT NULL REFERENCES monster_types(id),
    subtype VARCHAR(50),
    alignment VARCHAR(30),
    armor_class SMALLINT NOT NULL,
    armor_type VARCHAR(50),
    hit_points SMALLINT NOT NULL,
    hit_dice VARCHAR(20) NOT NULL,
    speed JSONB NOT NULL DEFAULT '{"walk": "0 ft."}',
    strength SMALLINT NOT NULL DEFAULT 10,
    dexterity SMALLINT NOT NULL DEFAULT 10,
    constitution SMALLINT NOT NULL DEFAULT 10,
    intelligence SMALLINT NOT NULL DEFAULT 10,
    wisdom SMALLINT NOT NULL DEFAULT 10,
    charisma SMALLINT NOT NULL DEFAULT 10,
    saving_throws JSONB,
    skills JSONB,
    senses JSONB,
    languages VARCHAR(255),
    challenge_rating VARCHAR(10) NOT NULL,
    xp INTEGER NOT NULL DEFAULT 0,
    proficiency_bonus SMALLINT NOT NULL DEFAULT 2,
    special_abilities JSONB,
    actions JSONB,
    reactions JSONB,
    legendary_actions JSONB,
    legendary_desc TEXT,
    lair_actions JSONB,
    regional_effects JSONB,
    description TEXT,
    source VARCHAR(10) NOT NULL DEFAULT 'OFFICIAL',
    owner_id BIGINT,
    campaign_id BIGINT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_monster_index_owner ON monsters(index, COALESCE(owner_id, -1::BIGINT));
CREATE INDEX IF NOT EXISTS idx_monsters_name ON monsters(name);
CREATE INDEX IF NOT EXISTS idx_monsters_type ON monsters(type_id);
CREATE INDEX IF NOT EXISTS idx_monsters_size ON monsters(size_id);
CREATE INDEX IF NOT EXISTS idx_monsters_cr ON monsters(challenge_rating);
CREATE INDEX IF NOT EXISTS idx_monsters_source ON monsters(source);
CREATE INDEX IF NOT EXISTS idx_monsters_owner ON monsters(owner_id);
CREATE INDEX IF NOT EXISTS idx_monsters_campaign ON monsters(campaign_id);

-- Monster Damage Vulnerabilities (many-to-many)
CREATE TABLE IF NOT EXISTS monster_damage_vulnerabilities (
    monster_id INTEGER NOT NULL REFERENCES monsters(id) ON DELETE CASCADE,
    damage_type_id SMALLINT NOT NULL REFERENCES damage_types(id),
    notes VARCHAR(100),
    PRIMARY KEY (monster_id, damage_type_id)
);

-- Monster Damage Resistances (many-to-many)
CREATE TABLE IF NOT EXISTS monster_damage_resistances (
    monster_id INTEGER NOT NULL REFERENCES monsters(id) ON DELETE CASCADE,
    damage_type_id SMALLINT NOT NULL REFERENCES damage_types(id),
    notes VARCHAR(100),
    PRIMARY KEY (monster_id, damage_type_id)
);

-- Monster Damage Immunities (many-to-many)
CREATE TABLE IF NOT EXISTS monster_damage_immunities (
    monster_id INTEGER NOT NULL REFERENCES monsters(id) ON DELETE CASCADE,
    damage_type_id SMALLINT NOT NULL REFERENCES damage_types(id),
    notes VARCHAR(100),
    PRIMARY KEY (monster_id, damage_type_id)
);

-- Monster Condition Immunities (many-to-many)
CREATE TABLE IF NOT EXISTS monster_condition_immunities (
    monster_id INTEGER NOT NULL REFERENCES monsters(id) ON DELETE CASCADE,
    condition_type_id SMALLINT NOT NULL REFERENCES condition_types(id),
    PRIMARY KEY (monster_id, condition_type_id)
);

-- Feats (with homebrew support)
CREATE TABLE IF NOT EXISTS feats (
    id SERIAL PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    description TEXT NOT NULL,
    prerequisite VARCHAR(255),
    prerequisite_ability VARCHAR(3),
    prerequisite_level SMALLINT,
    benefit TEXT,
    grants_ability_increase BOOLEAN DEFAULT FALSE,
    source VARCHAR(10) NOT NULL DEFAULT 'OFFICIAL',
    owner_id BIGINT,
    campaign_id BIGINT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_feat_name_owner ON feats(name, COALESCE(owner_id, -1::BIGINT));
CREATE INDEX IF NOT EXISTS idx_feats_name ON feats(name);
CREATE INDEX IF NOT EXISTS idx_feats_source ON feats(source);
CREATE INDEX IF NOT EXISTS idx_feats_prereq_ability ON feats(prerequisite_ability);

-- Magic Items (with homebrew support)
CREATE TABLE IF NOT EXISTS magic_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    rarity_id SMALLINT NOT NULL REFERENCES magic_item_rarities(id),
    type_id SMALLINT NOT NULL REFERENCES magic_item_types(id),
    requires_attunement BOOLEAN DEFAULT FALSE,
    attunement_requirements VARCHAR(100),
    description TEXT,
    properties JSONB,
    source VARCHAR(10) NOT NULL DEFAULT 'OFFICIAL',
    owner_id BIGINT,
    campaign_id BIGINT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_magic_item_name_owner ON magic_items(name, COALESCE(owner_id, -1::BIGINT));
CREATE INDEX IF NOT EXISTS idx_magic_items_rarity ON magic_items(rarity_id);
CREATE INDEX IF NOT EXISTS idx_magic_items_type ON magic_items(type_id);
CREATE INDEX IF NOT EXISTS idx_magic_items_source ON magic_items(source);
CREATE INDEX IF NOT EXISTS idx_magic_items_owner ON magic_items(owner_id);
CREATE INDEX IF NOT EXISTS idx_magic_items_name ON magic_items(name);
CREATE INDEX IF NOT EXISTS idx_magic_items_attunement ON magic_items(requires_attunement);

-- Equipment (with homebrew support)
CREATE TABLE IF NOT EXISTS equipment (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category_id SMALLINT NOT NULL REFERENCES equipment_categories(id),
    cost_gp DECIMAL(10, 2),
    cost_display VARCHAR(30),
    weight_lb DECIMAL(8, 2),
    description TEXT,
    properties JSONB,
    source VARCHAR(10) NOT NULL DEFAULT 'OFFICIAL',
    owner_id BIGINT,
    campaign_id BIGINT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_equipment_name_owner ON equipment(name, COALESCE(owner_id, -1::BIGINT));
CREATE INDEX IF NOT EXISTS idx_equipment_category ON equipment(category_id);
CREATE INDEX IF NOT EXISTS idx_equipment_source ON equipment(source);
CREATE INDEX IF NOT EXISTS idx_equipment_owner ON equipment(owner_id);
CREATE INDEX IF NOT EXISTS idx_equipment_name ON equipment(name);
