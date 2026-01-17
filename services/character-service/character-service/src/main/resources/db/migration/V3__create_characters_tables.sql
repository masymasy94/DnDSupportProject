-- =============================================
-- Characters Tables (D&D 5E compliant)
-- With foreign keys to reference tables
-- =============================================

-- Characters main table
CREATE TABLE IF NOT EXISTS characters (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,

    -- Foreign keys to reference tables
    species_id SMALLINT NOT NULL REFERENCES species(id),
    class_id SMALLINT NOT NULL REFERENCES character_classes(id),
    background_id SMALLINT REFERENCES backgrounds(id),
    alignment_id SMALLINT REFERENCES alignments(id),

    level INTEGER NOT NULL DEFAULT 1,
    experience_points INTEGER DEFAULT 0,

    -- Ability Scores (inline for performance)
    strength INTEGER NOT NULL DEFAULT 10,
    dexterity INTEGER NOT NULL DEFAULT 10,
    constitution INTEGER NOT NULL DEFAULT 10,
    intelligence INTEGER NOT NULL DEFAULT 10,
    wisdom INTEGER NOT NULL DEFAULT 10,
    charisma INTEGER NOT NULL DEFAULT 10,

    -- Combat Stats
    hit_points_current INTEGER NOT NULL DEFAULT 0,
    hit_points_max INTEGER NOT NULL DEFAULT 0,
    hit_points_temp INTEGER DEFAULT 0,
    armor_class INTEGER NOT NULL DEFAULT 10,
    speed INTEGER NOT NULL DEFAULT 30,
    hit_dice_total INTEGER NOT NULL DEFAULT 1,
    hit_dice_type VARCHAR(4) DEFAULT 'd8',
    hit_dice_used INTEGER DEFAULT 0,
    death_save_successes INTEGER DEFAULT 0,
    death_save_failures INTEGER DEFAULT 0,

    -- Currency
    copper_pieces INTEGER DEFAULT 0,
    silver_pieces INTEGER DEFAULT 0,
    electrum_pieces INTEGER DEFAULT 0,
    gold_pieces INTEGER DEFAULT 0,
    platinum_pieces INTEGER DEFAULT 0,

    -- Text fields
    personality_traits TEXT,
    ideals TEXT,
    bonds TEXT,
    flaws TEXT,
    backstory TEXT,
    appearance TEXT,
    notes TEXT,

    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Indexes for characters table
CREATE INDEX IF NOT EXISTS idx_characters_user_id ON characters(user_id);
CREATE INDEX IF NOT EXISTS idx_characters_name ON characters(name);
CREATE INDEX IF NOT EXISTS idx_characters_species_id ON characters(species_id);
CREATE INDEX IF NOT EXISTS idx_characters_class_id ON characters(class_id);
CREATE INDEX IF NOT EXISTS idx_characters_background_id ON characters(background_id);
CREATE INDEX IF NOT EXISTS idx_characters_alignment_id ON characters(alignment_id);

-- Character saving throws (linked to abilities reference table)
CREATE TABLE IF NOT EXISTS character_saving_throws (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    ability_id SMALLINT NOT NULL REFERENCES abilities(id),
    proficient BOOLEAN DEFAULT FALSE,
    CONSTRAINT uq_character_saving_throw UNIQUE (character_id, ability_id)
);

CREATE INDEX IF NOT EXISTS idx_saving_throws_character_id ON character_saving_throws(character_id);
CREATE INDEX IF NOT EXISTS idx_saving_throws_ability_id ON character_saving_throws(ability_id);

-- Character skills (linked to skills reference table)
CREATE TABLE IF NOT EXISTS character_skills (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    skill_id SMALLINT NOT NULL REFERENCES skills(id),
    proficient BOOLEAN DEFAULT FALSE,
    expertise BOOLEAN DEFAULT FALSE,
    CONSTRAINT uq_character_skill UNIQUE (character_id, skill_id)
);

CREATE INDEX IF NOT EXISTS idx_character_skills_character_id ON character_skills(character_id);
CREATE INDEX IF NOT EXISTS idx_character_skills_skill_id ON character_skills(skill_id);

-- Character equipment/inventory
CREATE TABLE IF NOT EXISTS character_equipment (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    quantity INTEGER DEFAULT 1,
    weight DECIMAL(10,2),
    equipped BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_equipment_character_id ON character_equipment(character_id);

-- Character features and traits
CREATE TABLE IF NOT EXISTS character_features (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    source VARCHAR(50),
    description TEXT
);

CREATE INDEX IF NOT EXISTS idx_features_character_id ON character_features(character_id);

-- Character proficiencies (linked to proficiency_types reference table)
CREATE TABLE IF NOT EXISTS character_proficiencies (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    proficiency_type_id SMALLINT NOT NULL REFERENCES proficiency_types(id),
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_character_proficiency UNIQUE (character_id, proficiency_type_id, name)
);

CREATE INDEX IF NOT EXISTS idx_proficiencies_character_id ON character_proficiencies(character_id);
CREATE INDEX IF NOT EXISTS idx_proficiencies_type_id ON character_proficiencies(proficiency_type_id);

-- =============================================
-- Spellcasting Tables
-- =============================================

-- Character spell slots (tracks available spell slots per level)
CREATE TABLE IF NOT EXISTS character_spell_slots (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    spell_level SMALLINT NOT NULL CHECK (spell_level BETWEEN 1 AND 9),
    slots_total SMALLINT NOT NULL DEFAULT 0,
    slots_used SMALLINT NOT NULL DEFAULT 0,
    CONSTRAINT uq_character_spell_slot UNIQUE (character_id, spell_level),
    CONSTRAINT chk_slots_used CHECK (slots_used <= slots_total)
);

CREATE INDEX IF NOT EXISTS idx_spell_slots_character_id ON character_spell_slots(character_id);

-- Character known/prepared spells (links characters to spells)
CREATE TABLE IF NOT EXISTS character_spells (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    spell_id INTEGER NOT NULL REFERENCES spells(id),
    prepared BOOLEAN DEFAULT FALSE,  -- Whether the spell is currently prepared
    source VARCHAR(50),  -- Source of the spell (class, race, item, etc.)
    CONSTRAINT uq_character_spell UNIQUE (character_id, spell_id)
);

CREATE INDEX IF NOT EXISTS idx_character_spells_character_id ON character_spells(character_id);
CREATE INDEX IF NOT EXISTS idx_character_spells_spell_id ON character_spells(spell_id);
CREATE INDEX IF NOT EXISTS idx_character_spells_prepared ON character_spells(character_id, prepared);
