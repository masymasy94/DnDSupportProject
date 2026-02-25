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

-- =============================================
-- Populate D&D 5E Official Reference Data
-- =============================================

-- =============================================
-- SECTION 1: Core Reference Data
-- =============================================

-- Alignments (9)
INSERT INTO alignments (code, name) VALUES
    ('LG', 'Lawful Good'),
    ('NG', 'Neutral Good'),
    ('CG', 'Chaotic Good'),
    ('LN', 'Lawful Neutral'),
    ('TN', 'True Neutral'),
    ('CN', 'Chaotic Neutral'),
    ('LE', 'Lawful Evil'),
    ('NE', 'Neutral Evil'),
    ('CE', 'Chaotic Evil');

-- Abilities (6)
INSERT INTO abilities (code, name) VALUES
    ('STR', 'Strength'),
    ('DEX', 'Dexterity'),
    ('CON', 'Constitution'),
    ('INT', 'Intelligence'),
    ('WIS', 'Wisdom'),
    ('CHA', 'Charisma');

-- Skills (18)
INSERT INTO skills (name, ability_id) VALUES
    ('Athletics', (SELECT id FROM abilities WHERE code = 'STR')),
    ('Acrobatics', (SELECT id FROM abilities WHERE code = 'DEX')),
    ('Sleight of Hand', (SELECT id FROM abilities WHERE code = 'DEX')),
    ('Stealth', (SELECT id FROM abilities WHERE code = 'DEX')),
    ('Arcana', (SELECT id FROM abilities WHERE code = 'INT')),
    ('History', (SELECT id FROM abilities WHERE code = 'INT')),
    ('Investigation', (SELECT id FROM abilities WHERE code = 'INT')),
    ('Nature', (SELECT id FROM abilities WHERE code = 'INT')),
    ('Religion', (SELECT id FROM abilities WHERE code = 'INT')),
    ('Animal Handling', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Insight', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Medicine', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Perception', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Survival', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Deception', (SELECT id FROM abilities WHERE code = 'CHA')),
    ('Intimidation', (SELECT id FROM abilities WHERE code = 'CHA')),
    ('Performance', (SELECT id FROM abilities WHERE code = 'CHA')),
    ('Persuasion', (SELECT id FROM abilities WHERE code = 'CHA'));

-- Proficiency Types (4)
INSERT INTO proficiency_types (name) VALUES
    ('ARMOR'),
    ('WEAPON'),
    ('TOOL'),
    ('LANGUAGE');

-- Armor Types (4)
INSERT INTO armor_types (name) VALUES
    ('Light Armor'),
    ('Medium Armor'),
    ('Heavy Armor'),
    ('Shields');

-- Weapon Types - Simple
INSERT INTO weapon_types (name, category) VALUES
    ('Simple Weapons', 'SIMPLE'),
    ('Club', 'SIMPLE'),
    ('Dagger', 'SIMPLE'),
    ('Greatclub', 'SIMPLE'),
    ('Handaxe', 'SIMPLE'),
    ('Javelin', 'SIMPLE'),
    ('Light Hammer', 'SIMPLE'),
    ('Mace', 'SIMPLE'),
    ('Quarterstaff', 'SIMPLE'),
    ('Sickle', 'SIMPLE'),
    ('Spear', 'SIMPLE'),
    ('Light Crossbow', 'SIMPLE'),
    ('Dart', 'SIMPLE'),
    ('Shortbow', 'SIMPLE'),
    ('Sling', 'SIMPLE');

-- Weapon Types - Martial
INSERT INTO weapon_types (name, category) VALUES
    ('Martial Weapons', 'MARTIAL'),
    ('Battleaxe', 'MARTIAL'),
    ('Flail', 'MARTIAL'),
    ('Glaive', 'MARTIAL'),
    ('Greataxe', 'MARTIAL'),
    ('Greatsword', 'MARTIAL'),
    ('Halberd', 'MARTIAL'),
    ('Lance', 'MARTIAL'),
    ('Longsword', 'MARTIAL'),
    ('Maul', 'MARTIAL'),
    ('Morningstar', 'MARTIAL'),
    ('Pike', 'MARTIAL'),
    ('Rapier', 'MARTIAL'),
    ('Scimitar', 'MARTIAL'),
    ('Shortsword', 'MARTIAL'),
    ('Trident', 'MARTIAL'),
    ('War Pick', 'MARTIAL'),
    ('Warhammer', 'MARTIAL'),
    ('Whip', 'MARTIAL'),
    ('Blowgun', 'MARTIAL'),
    ('Hand Crossbow', 'MARTIAL'),
    ('Heavy Crossbow', 'MARTIAL'),
    ('Longbow', 'MARTIAL'),
    ('Net', 'MARTIAL');

-- Tool Types
INSERT INTO tool_types (name, category) VALUES
    ('Alchemist''s Supplies', 'ARTISAN'),
    ('Brewer''s Supplies', 'ARTISAN'),
    ('Calligrapher''s Supplies', 'ARTISAN'),
    ('Carpenter''s Tools', 'ARTISAN'),
    ('Cartographer''s Tools', 'ARTISAN'),
    ('Cobbler''s Tools', 'ARTISAN'),
    ('Cook''s Utensils', 'ARTISAN'),
    ('Glassblower''s Tools', 'ARTISAN'),
    ('Jeweler''s Tools', 'ARTISAN'),
    ('Leatherworker''s Tools', 'ARTISAN'),
    ('Mason''s Tools', 'ARTISAN'),
    ('Painter''s Supplies', 'ARTISAN'),
    ('Potter''s Tools', 'ARTISAN'),
    ('Smith''s Tools', 'ARTISAN'),
    ('Tinker''s Tools', 'ARTISAN'),
    ('Weaver''s Tools', 'ARTISAN'),
    ('Woodcarver''s Tools', 'ARTISAN'),
    ('Dice Set', 'GAMING'),
    ('Playing Card Set', 'GAMING'),
    ('Dragonchess Set', 'GAMING'),
    ('Three-Dragon Ante Set', 'GAMING'),
    ('Bagpipes', 'INSTRUMENT'),
    ('Drum', 'INSTRUMENT'),
    ('Dulcimer', 'INSTRUMENT'),
    ('Flute', 'INSTRUMENT'),
    ('Lute', 'INSTRUMENT'),
    ('Lyre', 'INSTRUMENT'),
    ('Horn', 'INSTRUMENT'),
    ('Pan Flute', 'INSTRUMENT'),
    ('Shawm', 'INSTRUMENT'),
    ('Viol', 'INSTRUMENT'),
    ('Disguise Kit', 'OTHER'),
    ('Forgery Kit', 'OTHER'),
    ('Herbalism Kit', 'OTHER'),
    ('Navigator''s Tools', 'OTHER'),
    ('Poisoner''s Kit', 'OTHER'),
    ('Thieves'' Tools', 'OTHER'),
    ('Vehicles (Land)', 'OTHER'),
    ('Vehicles (Water)', 'OTHER');

-- Languages - Standard
INSERT INTO languages (name, script, type) VALUES
    ('Common', 'Common', 'STANDARD'),
    ('Dwarvish', 'Dwarvish', 'STANDARD'),
    ('Elvish', 'Elvish', 'STANDARD'),
    ('Giant', 'Dwarvish', 'STANDARD'),
    ('Gnomish', 'Dwarvish', 'STANDARD'),
    ('Goblin', 'Dwarvish', 'STANDARD'),
    ('Halfling', 'Common', 'STANDARD'),
    ('Orc', 'Dwarvish', 'STANDARD');

-- Languages - Exotic
INSERT INTO languages (name, script, type) VALUES
    ('Abyssal', 'Infernal', 'EXOTIC'),
    ('Celestial', 'Celestial', 'EXOTIC'),
    ('Draconic', 'Draconic', 'EXOTIC'),
    ('Deep Speech', NULL, 'EXOTIC'),
    ('Infernal', 'Infernal', 'EXOTIC'),
    ('Primordial', 'Dwarvish', 'EXOTIC'),
    ('Sylvan', 'Elvish', 'EXOTIC'),
    ('Undercommon', 'Elvish', 'EXOTIC');

-- Spell Schools (8)
INSERT INTO spell_schools (name) VALUES
    ('Abjuration'),
    ('Conjuration'),
    ('Divination'),
    ('Enchantment'),
    ('Evocation'),
    ('Illusion'),
    ('Necromancy'),
    ('Transmutation');

-- Monster Types (14)
INSERT INTO monster_types (name) VALUES
    ('Aberration'),
    ('Beast'),
    ('Celestial'),
    ('Construct'),
    ('Dragon'),
    ('Elemental'),
    ('Fey'),
    ('Fiend'),
    ('Giant'),
    ('Humanoid'),
    ('Monstrosity'),
    ('Ooze'),
    ('Plant'),
    ('Undead');

-- Monster Sizes (6)
INSERT INTO monster_sizes (name, space) VALUES
    ('Tiny', '2.5 x 2.5 ft'),
    ('Small', '5 x 5 ft'),
    ('Medium', '5 x 5 ft'),
    ('Large', '10 x 10 ft'),
    ('Huge', '15 x 15 ft'),
    ('Gargantuan', '20 x 20 ft');

-- Damage Types (14)
INSERT INTO damage_types (name) VALUES
    ('Acid'),
    ('Bludgeoning'),
    ('Cold'),
    ('Fire'),
    ('Force'),
    ('Lightning'),
    ('Necrotic'),
    ('Piercing'),
    ('Poison'),
    ('Psychic'),
    ('Radiant'),
    ('Slashing'),
    ('Thunder'),
    ('Nonmagical Attacks');

-- Condition Types (15)
INSERT INTO condition_types (name) VALUES
    ('Blinded'),
    ('Charmed'),
    ('Deafened'),
    ('Exhaustion'),
    ('Frightened'),
    ('Grappled'),
    ('Incapacitated'),
    ('Invisible'),
    ('Paralyzed'),
    ('Petrified'),
    ('Poisoned'),
    ('Prone'),
    ('Restrained'),
    ('Stunned'),
    ('Unconscious');

-- Magic Item Rarities (6)
INSERT INTO magic_item_rarities (name) VALUES
    ('Common'),
    ('Uncommon'),
    ('Rare'),
    ('Very Rare'),
    ('Legendary'),
    ('Artifact');

-- Magic Item Types (10)
INSERT INTO magic_item_types (name) VALUES
    ('Armor'),
    ('Potion'),
    ('Ring'),
    ('Rod'),
    ('Scroll'),
    ('Staff'),
    ('Wand'),
    ('Weapon'),
    ('Wondrous Item'),
    ('Other');

-- Equipment Categories (8)
INSERT INTO equipment_categories (name) VALUES
    ('Adventuring Gear'),
    ('Armor'),
    ('Weapon'),
    ('Tools'),
    ('Mounts and Vehicles'),
    ('Trade Goods'),
    ('Ammunition'),
    ('Container');

-- =============================================
-- SECTION 2: Character Options
-- =============================================

-- Species/Races
INSERT INTO species (name, description, source) VALUES
    ('Aasimar', 'Celestial heritage with divine power', 'OFFICIAL'),
    ('Dragonborn', 'Draconic humanoids with breath weapons', 'OFFICIAL'),
    ('Dwarf', 'Stout folk known for craftsmanship', 'OFFICIAL'),
    ('Elf', 'Graceful people with keen senses', 'OFFICIAL'),
    ('Gnome', 'Small, clever inventors and illusionists', 'OFFICIAL'),
    ('Goliath', 'Towering warriors of the mountains', 'OFFICIAL'),
    ('Halfling', 'Small, nimble, and lucky folk', 'OFFICIAL'),
    ('Human', 'Versatile and ambitious people', 'OFFICIAL'),
    ('Orc', 'Fierce warriors with primal strength', 'OFFICIAL'),
    ('Tiefling', 'Infernal heritage with fiendish traits', 'OFFICIAL');

-- Character Classes (13)
INSERT INTO character_classes (name, hit_die, description, source) VALUES
    ('Barbarian', 'd12', 'Fierce warriors who channel primal rage', 'OFFICIAL'),
    ('Bard', 'd8', 'Inspiring performers and jack-of-all-trades', 'OFFICIAL'),
    ('Cleric', 'd8', 'Divine spellcasters serving their deity', 'OFFICIAL'),
    ('Druid', 'd8', 'Nature magic wielders and shapeshifters', 'OFFICIAL'),
    ('Fighter', 'd10', 'Masters of martial combat', 'OFFICIAL'),
    ('Monk', 'd8', 'Martial artists harnessing ki energy', 'OFFICIAL'),
    ('Paladin', 'd10', 'Holy warriors bound by sacred oaths', 'OFFICIAL'),
    ('Ranger', 'd10', 'Skilled hunters and trackers', 'OFFICIAL'),
    ('Rogue', 'd8', 'Stealthy experts in precise strikes', 'OFFICIAL'),
    ('Sorcerer', 'd6', 'Innate spellcasters with raw magic', 'OFFICIAL'),
    ('Warlock', 'd8', 'Spellcasters empowered by patrons', 'OFFICIAL'),
    ('Wizard', 'd6', 'Scholarly masters of arcane magic', 'OFFICIAL'),
    ('Artificer', 'd8', 'Inventors who infuse magic into items', 'OFFICIAL');

-- Backgrounds (13)
INSERT INTO backgrounds (name, description, source) VALUES
    ('Acolyte', 'Served in a temple, gaining religious knowledge', 'OFFICIAL'),
    ('Charlatan', 'Master of deception and false identities', 'OFFICIAL'),
    ('Criminal', 'Experienced in breaking the law', 'OFFICIAL'),
    ('Entertainer', 'Performer who thrives in the spotlight', 'OFFICIAL'),
    ('Folk Hero', 'Common person who became a local legend', 'OFFICIAL'),
    ('Guild Artisan', 'Member of an artisan guild', 'OFFICIAL'),
    ('Hermit', 'Lived in seclusion, seeking enlightenment', 'OFFICIAL'),
    ('Noble', 'Born into wealth and privilege', 'OFFICIAL'),
    ('Outlander', 'Grew up in the wilds, far from civilization', 'OFFICIAL'),
    ('Sage', 'Dedicated scholar and researcher', 'OFFICIAL'),
    ('Sailor', 'Experienced on ships and the sea', 'OFFICIAL'),
    ('Soldier', 'Trained military combatant', 'OFFICIAL'),
    ('Urchin', 'Grew up on the streets, surviving by wits', 'OFFICIAL');

-- =============================================
-- SECTION 3: Feats
-- =============================================

INSERT INTO feats (name, description, prerequisite, prerequisite_ability, prerequisite_level, benefit, grants_ability_increase, source) VALUES
('Alert', 'Always on the lookout for danger, you gain the following benefits: You gain a +5 bonus to initiative. You can''t be surprised while you are conscious. Other creatures don''t gain advantage on attack rolls against you as a result of being unseen by you.', NULL, NULL, NULL, '+5 initiative, can''t be surprised, hidden attackers don''t gain advantage', FALSE, 'OFFICIAL'),
('Athlete', 'You have undergone extensive physical training to gain the following benefits: Increase your Strength or Dexterity score by 1, to a maximum of 20. When you are prone, standing up uses only 5 feet of your movement. Climbing doesn''t cost you extra movement. You can make a running long jump or a running high jump after moving only 5 feet on foot, rather than 10 feet.', NULL, NULL, NULL, '+1 STR or DEX, improved climbing and jumping', TRUE, 'OFFICIAL'),
('Actor', 'Skilled at mimicry and dramatics, you gain the following benefits: Increase your Charisma score by 1, to a maximum of 20. You have advantage on Charisma (Deception) and Charisma (Performance) checks when trying to pass yourself off as a different person.', NULL, NULL, NULL, '+1 CHA, advantage on deception/performance for disguises', TRUE, 'OFFICIAL'),
('Charger', 'When you use your action to Dash, you can use a bonus action to make one melee weapon attack or to shove a creature.', NULL, NULL, NULL, 'Bonus action attack after Dash with +5 damage or 10 ft push', FALSE, 'OFFICIAL'),
('Crossbow Expert', 'Thanks to extensive practice with the crossbow, you gain the following benefits: You ignore the loading quality of crossbows with which you are proficient.', NULL, NULL, NULL, 'Ignore crossbow loading, no disadvantage in melee, bonus action hand crossbow attack', FALSE, 'OFFICIAL'),
('Defensive Duelist', 'When you are wielding a finesse weapon with which you are proficient and another creature hits you with a melee attack, you can use your reaction to add your proficiency bonus to your AC for that attack.', 'Dexterity 13 or higher', 'DEX', NULL, 'Reaction to add proficiency bonus to AC against melee attack', FALSE, 'OFFICIAL'),
('Dual Wielder', 'You master fighting with two weapons, gaining the following benefits: You gain a +1 bonus to AC while you are wielding a separate melee weapon in each hand.', NULL, NULL, NULL, '+1 AC with two weapons, two-weapon fighting with non-light weapons', FALSE, 'OFFICIAL'),
('Dungeon Delver', 'Alert to the hidden traps and secret doors found in many dungeons, you gain the following benefits: You have advantage on Wisdom (Perception) and Intelligence (Investigation) checks made to detect the presence of secret doors.', NULL, NULL, NULL, 'Advantage to detect secret doors and resist traps, trap damage resistance', FALSE, 'OFFICIAL'),
('Durable', 'Hardy and resilient, you gain the following benefits: Increase your Constitution score by 1, to a maximum of 20.', NULL, NULL, NULL, '+1 CON, minimum HP from Hit Dice is 2x CON modifier', TRUE, 'OFFICIAL'),
('Elemental Adept', 'When you gain this feat, choose one of the following damage types: acid, cold, fire, lightning, or thunder. Spells you cast ignore resistance to damage of the chosen type.', 'The ability to cast at least one spell', NULL, NULL, 'Spells ignore resistance to chosen element, treat 1s as 2s on damage', FALSE, 'OFFICIAL'),
('Grappler', 'You''ve developed the skills necessary to hold your own in close-quarters grappling. You gain the following benefits: You have advantage on attack rolls against a creature you are grappling.', 'Strength 13 or higher', 'STR', NULL, 'Advantage on attacks vs grappled creatures, can pin to restrain both', FALSE, 'OFFICIAL'),
('Great Weapon Master', 'You''ve learned to put the weight of a weapon to your advantage. On your turn, when you score a critical hit with a melee weapon or reduce a creature to 0 hit points with one, you can make one melee weapon attack as a bonus action.', NULL, NULL, NULL, 'Bonus action attack on crit/kill, -5 to hit for +10 damage with heavy weapons', FALSE, 'OFFICIAL'),
('Healer', 'You are an able physician, allowing you to mend wounds quickly and get your allies back in the fight.', NULL, NULL, NULL, 'Healer''s kit stabilizes and heals 1 HP, action to heal 1d6+4+HD', FALSE, 'OFFICIAL'),
('Heavily Armored', 'You have trained to master the use of heavy armor, gaining the following benefits: Increase your Strength score by 1, to a maximum of 20. You gain proficiency with heavy armor.', 'Proficiency with medium armor', NULL, NULL, '+1 STR, heavy armor proficiency', TRUE, 'OFFICIAL'),
('Heavy Armor Master', 'You can use your armor to deflect strikes that would kill others. Increase your Strength score by 1, to a maximum of 20.', 'Proficiency with heavy armor', NULL, NULL, '+1 STR, reduce nonmagical B/P/S damage by 3 in heavy armor', TRUE, 'OFFICIAL'),
('Inspiring Leader', 'You can spend 10 minutes inspiring your companions, shoring up their resolve to fight.', 'Charisma 13 or higher', 'CHA', NULL, '10 min speech grants 6 creatures temp HP = level + CHA mod', FALSE, 'OFFICIAL'),
('Keen Mind', 'You have a mind that can track time, direction, and detail with uncanny precision. Increase your Intelligence score by 1, to a maximum of 20.', NULL, NULL, NULL, '+1 INT, always know north and time, perfect recall for 1 month', TRUE, 'OFFICIAL'),
('Linguist', 'You have studied languages and codes, gaining the following benefits: Increase your Intelligence score by 1, to a maximum of 20. You learn three languages of your choice.', NULL, NULL, NULL, '+1 INT, learn 3 languages, create unbreakable ciphers', TRUE, 'OFFICIAL'),
('Lucky', 'You have inexplicable luck that seems to kick in at just the right moment. You have 3 luck points.', NULL, NULL, NULL, '3 luck points per long rest to reroll d20s or force enemy rerolls', FALSE, 'OFFICIAL'),
('Mage Slayer', 'You have practiced techniques useful in melee combat against spellcasters.', NULL, NULL, NULL, 'Reaction attack when adjacent creature casts, disadvantage on concentration saves', FALSE, 'OFFICIAL'),
('Magic Initiate', 'Choose a class: bard, cleric, druid, sorcerer, warlock, or wizard. You learn two cantrips of your choice from that class''s spell list.', NULL, NULL, NULL, 'Learn 2 cantrips and 1 1st-level spell from any class', FALSE, 'OFFICIAL'),
('Martial Adept', 'You have martial training that allows you to perform special combat maneuvers. You learn two maneuvers of your choice.', NULL, NULL, NULL, 'Learn 2 Battle Master maneuvers with 1 d6 superiority die', FALSE, 'OFFICIAL'),
('Medium Armor Master', 'You have practiced moving in medium armor to gain the following benefits.', 'Proficiency with medium armor', NULL, NULL, 'No stealth disadvantage in medium armor, +3 DEX to AC instead of +2', FALSE, 'OFFICIAL'),
('Mobile', 'You are exceptionally speedy and agile. Your speed increases by 10 feet.', NULL, NULL, NULL, '+10 ft speed, Dash ignores difficult terrain, no opportunity attacks from melee targets', FALSE, 'OFFICIAL'),
('Mounted Combatant', 'You are a dangerous foe to face while mounted.', NULL, NULL, NULL, 'Advantage vs smaller unmounted creatures, redirect attacks to self, mount evasion', FALSE, 'OFFICIAL'),
('Observant', 'Quick to notice details of your environment, you gain the following benefits: Increase your Intelligence or Wisdom score by 1, to a maximum of 20.', NULL, NULL, NULL, '+1 INT or WIS, lip reading, +5 passive Perception and Investigation', TRUE, 'OFFICIAL'),
('Polearm Master', 'You can keep your enemies at bay with reach weapons.', NULL, NULL, NULL, 'Bonus action d4 attack with polearm, opportunity attack when enemies enter reach', FALSE, 'OFFICIAL'),
('Resilient', 'Choose one ability score. Increase the chosen ability score by 1, to a maximum of 20. You gain proficiency in saving throws using the chosen ability.', NULL, NULL, NULL, '+1 to chosen ability, proficiency in that ability''s saving throw', TRUE, 'OFFICIAL'),
('Ritual Caster', 'You have learned a number of spells that you can cast as rituals.', 'Intelligence or Wisdom 13 or higher', NULL, NULL, 'Ritual book with 2 1st-level ritual spells, can copy more', FALSE, 'OFFICIAL'),
('Savage Attacker', 'Once per turn when you roll damage for a melee weapon attack, you can reroll the weapon''s damage dice and use either total.', NULL, NULL, NULL, 'Reroll melee weapon damage once per turn', FALSE, 'OFFICIAL'),
('Sentinel', 'You have mastered techniques to take advantage of every drop in any enemy''s guard.', NULL, NULL, NULL, 'Opportunity attacks reduce speed to 0, ignore Disengage, reaction attack when allies targeted', FALSE, 'OFFICIAL'),
('Sharpshooter', 'You have mastered ranged weapons and can make shots that others find impossible.', NULL, NULL, NULL, 'No long range disadvantage, ignore cover, -5 to hit for +10 damage', FALSE, 'OFFICIAL'),
('Shield Master', 'You use shields not just for protection but also for offense.', NULL, NULL, NULL, 'Bonus action shove with shield, add shield AC to DEX saves, reaction evasion', FALSE, 'OFFICIAL'),
('Skilled', 'You gain proficiency in any combination of three skills or tools of your choice.', NULL, NULL, NULL, 'Gain 3 skill or tool proficiencies', FALSE, 'OFFICIAL'),
('Skulker', 'You are expert at slinking through shadows.', 'Dexterity 13 or higher', 'DEX', NULL, 'Hide when lightly obscured, missed ranged attacks don''t reveal, see in dim light', FALSE, 'OFFICIAL'),
('Spell Sniper', 'You have learned techniques to enhance your attacks with certain kinds of spells.', 'The ability to cast at least one spell', NULL, NULL, 'Double range on attack roll spells, ignore cover, learn 1 attack cantrip', FALSE, 'OFFICIAL'),
('Tavern Brawler', 'Accustomed to rough-and-tumble fighting using whatever weapons happen to be at hand. Increase your Strength or Constitution score by 1, to a maximum of 20.', NULL, NULL, NULL, '+1 STR or CON, improvised weapon proficiency, d4 unarmed, bonus action grapple', TRUE, 'OFFICIAL'),
('Tough', 'Your hit point maximum increases by an amount equal to twice your level when you gain this feat.', NULL, NULL, NULL, '+2 HP per level', FALSE, 'OFFICIAL'),
('War Caster', 'You have practiced casting spells in the midst of combat.', 'The ability to cast at least one spell', NULL, NULL, 'Advantage on concentration saves, somatic components with full hands, spell opportunity attacks', FALSE, 'OFFICIAL'),
('Weapon Master', 'You have practiced extensively with a variety of weapons. Increase your Strength or Dexterity score by 1, to a maximum of 20.', NULL, NULL, NULL, '+1 STR or DEX, proficiency with 4 weapons', TRUE, 'OFFICIAL');

-- =============================================
-- SECTION 4: Spells
-- =============================================

WITH schools AS (
    SELECT id, name FROM spell_schools
),
spell_data (name, level, school_name, casting_time, spell_range, components, material_components, duration, concentration, ritual, description, higher_levels, source) AS (
    VALUES
    -- Cantrips (Level 0)
    ('Acid Splash', 0::SMALLINT, 'Conjuration', '1 action', '60 feet', 'V S', NULL::TEXT, 'Instantaneous', FALSE, FALSE, 'You hurl a bubble of acid. Choose one creature you can see within range, or choose two creatures you can see within range that are within 5 feet of each other. A target must succeed on a Dexterity saving throw or take 1d6 acid damage.', 'This spell''s damage increases by 1d6 when you reach 5th level (2d6), 11th level (3d6), and 17th level (4d6).', 'OFFICIAL'),
    ('Blade Ward', 0, 'Abjuration', '1 action', 'Self', 'V S', NULL, '1 round', FALSE, FALSE, 'You extend your hand and trace a sigil of warding in the air. Until the end of your next turn, you have resistance against bludgeoning, piercing, and slashing damage dealt by weapon attacks.', NULL, 'OFFICIAL'),
    ('Chill Touch', 0, 'Necromancy', '1 action', '120 feet', 'V S', NULL, '1 round', FALSE, FALSE, 'You create a ghostly, skeletal hand in the space of a creature within range. Make a ranged spell attack against the creature to assail it with the chill of the grave.', 'This spell''s damage increases by 1d8 when you reach 5th level (2d8), 11th level (3d8), and 17th level (4d8).', 'OFFICIAL'),
    ('Dancing Lights', 0, 'Evocation', '1 action', '120 feet', 'V S M', 'a bit of phosphorus or wychwood', '1 minute', TRUE, FALSE, 'You create up to four torch-sized lights within range, making them appear as torches, lanterns, or glowing orbs.', NULL, 'OFFICIAL'),
    ('Druidcraft', 0, 'Transmutation', '1 action', '30 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'Whispering to the spirits of nature, you create one of the following effects within range.', NULL, 'OFFICIAL'),
    ('Eldritch Blast', 0, 'Evocation', '1 action', '120 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'A beam of crackling energy streaks toward a creature within range. Make a ranged spell attack against the target.', 'The spell creates more than one beam when you reach higher levels.', 'OFFICIAL'),
    ('Fire Bolt', 0, 'Evocation', '1 action', '120 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'You hurl a mote of fire at a creature or object within range. Make a ranged spell attack against the target.', 'This spell''s damage increases by 1d10 when you reach 5th level (2d10), 11th level (3d10), and 17th level (4d10).', 'OFFICIAL'),
    ('Guidance', 0, 'Divination', '1 action', 'Touch', 'V S', NULL, '1 minute', TRUE, FALSE, 'You touch one willing creature. Once before the spell ends, the target can roll a d4 and add the number rolled to one ability check.', NULL, 'OFFICIAL'),
    ('Light', 0, 'Evocation', '1 action', 'Touch', 'V M', 'a firefly or phosphorescent moss', '1 hour', FALSE, FALSE, 'You touch one object that is no larger than 10 feet in any dimension. Until the spell ends, the object sheds bright light in a 20-foot radius.', NULL, 'OFFICIAL'),
    ('Mage Hand', 0, 'Conjuration', '1 action', '30 feet', 'V S', NULL, '1 minute', FALSE, FALSE, 'A spectral, floating hand appears at a point you choose within range.', NULL, 'OFFICIAL'),
    ('Mending', 0, 'Transmutation', '1 minute', 'Touch', 'V S M', 'two lodestones', 'Instantaneous', FALSE, FALSE, 'This spell repairs a single break or tear in an object you touch.', NULL, 'OFFICIAL'),
    ('Message', 0, 'Transmutation', '1 action', '120 feet', 'V S M', 'a short piece of copper wire', '1 round', FALSE, FALSE, 'You point your finger toward a creature within range and whisper a message.', NULL, 'OFFICIAL'),
    ('Minor Illusion', 0, 'Illusion', '1 action', '30 feet', 'S M', 'a bit of fleece', '1 minute', FALSE, FALSE, 'You create a sound or an image of an object within range that lasts for the duration.', NULL, 'OFFICIAL'),
    ('Poison Spray', 0, 'Conjuration', '1 action', '10 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'You extend your hand toward a creature you can see within range and project a puff of noxious gas from your palm.', 'This spell''s damage increases by 1d12 when you reach 5th level (2d12), 11th level (3d12), and 17th level (4d12).', 'OFFICIAL'),
    ('Prestidigitation', 0, 'Transmutation', '1 action', '10 feet', 'V S', NULL, '1 hour', FALSE, FALSE, 'This spell is a minor magical trick that novice spellcasters use for practice.', NULL, 'OFFICIAL'),
    ('Produce Flame', 0, 'Conjuration', '1 action', 'Self', 'V S', NULL, '10 minutes', FALSE, FALSE, 'A flickering flame appears in your hand.', 'This spell''s damage increases by 1d8 when you reach 5th level (2d8), 11th level (3d8), and 17th level (4d8).', 'OFFICIAL'),
    ('Ray of Frost', 0, 'Evocation', '1 action', '60 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'A frigid beam of blue-white light streaks toward a creature within range.', 'The spell''s damage increases by 1d8 when you reach 5th level (2d8), 11th level (3d8), and 17th level (4d8).', 'OFFICIAL'),
    ('Resistance', 0, 'Abjuration', '1 action', 'Touch', 'V S M', 'a miniature cloak', '1 minute', TRUE, FALSE, 'You touch one willing creature. Once before the spell ends, the target can roll a d4 and add the number rolled to one saving throw.', NULL, 'OFFICIAL'),
    ('Sacred Flame', 0, 'Evocation', '1 action', '60 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'Flame-like radiance descends on a creature that you can see within range.', 'The spell''s damage increases by 1d8 when you reach 5th level (2d8), 11th level (3d8), and 17th level (4d8).', 'OFFICIAL'),
    ('Shillelagh', 0, 'Transmutation', '1 bonus action', 'Touch', 'V S M', 'mistletoe and a club or quarterstaff', '1 minute', FALSE, FALSE, 'The wood of a club or quarterstaff you are holding is imbued with nature''s power.', NULL, 'OFFICIAL'),
    ('Shocking Grasp', 0, 'Evocation', '1 action', 'Touch', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'Lightning springs from your hand to deliver a shock to a creature you try to touch.', 'The spell''s damage increases by 1d8 when you reach 5th level (2d8), 11th level (3d8), and 17th level (4d8).', 'OFFICIAL'),
    ('Spare the Dying', 0, 'Necromancy', '1 action', 'Touch', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'You touch a living creature that has 0 hit points. The creature becomes stable.', NULL, 'OFFICIAL'),
    ('Thaumaturgy', 0, 'Transmutation', '1 action', '30 feet', 'V', NULL, '1 minute', FALSE, FALSE, 'You manifest a minor wonder, a sign of supernatural power.', NULL, 'OFFICIAL'),
    ('Thorn Whip', 0, 'Transmutation', '1 action', '30 feet', 'V S M', 'the stem of a plant with thorns', 'Instantaneous', FALSE, FALSE, 'You create a long, vine-like whip covered in thorns that lashes out at your command.', 'This spell''s damage increases by 1d6 when you reach 5th level (2d6), 11th level (3d6), and 17th level (4d6).', 'OFFICIAL'),
    ('True Strike', 0, 'Divination', '1 action', '30 feet', 'S', NULL, '1 round', TRUE, FALSE, 'You point a finger at a target in range. Your magic grants you a brief insight into the target''s defenses.', NULL, 'OFFICIAL'),
    ('Vicious Mockery', 0, 'Enchantment', '1 action', '60 feet', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'You unleash a string of insults laced with subtle enchantments at a creature you can see within range.', 'This spell''s damage increases by 1d4 when you reach 5th level (2d4), 11th level (3d4), and 17th level (4d4).', 'OFFICIAL'),
    -- 1st Level Spells
    ('Alarm', 1, 'Abjuration', '1 minute', '30 feet', 'V S M', 'a tiny bell and a piece of fine silver wire', '8 hours', FALSE, TRUE, 'You set an alarm against unwanted intrusion.', NULL, 'OFFICIAL'),
    ('Bless', 1, 'Enchantment', '1 action', '30 feet', 'V S M', 'a sprinkling of holy water', '1 minute', TRUE, FALSE, 'You bless up to three creatures of your choice within range.', 'When you cast this spell using a spell slot of 2nd level or higher, you can target one additional creature for each slot level above 1st.', 'OFFICIAL'),
    ('Burning Hands', 1, 'Evocation', '1 action', 'Self (15-foot cone)', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'As you hold your hands with thumbs touching and fingers spread, a thin sheet of flames shoots forth.', 'When you cast this spell using a spell slot of 2nd level or higher, the damage increases by 1d6 for each slot level above 1st.', 'OFFICIAL'),
    ('Charm Person', 1, 'Enchantment', '1 action', '30 feet', 'V S', NULL, '1 hour', FALSE, FALSE, 'You attempt to charm a humanoid you can see within range.', 'When you cast this spell using a spell slot of 2nd level or higher, you can target one additional creature for each slot level above 1st.', 'OFFICIAL'),
    ('Cure Wounds', 1, 'Evocation', '1 action', 'Touch', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'A creature you touch regains a number of hit points equal to 1d8 + your spellcasting ability modifier.', 'When you cast this spell using a spell slot of 2nd level or higher, the healing increases by 1d8 for each slot level above 1st.', 'OFFICIAL'),
    ('Detect Magic', 1, 'Divination', '1 action', 'Self', 'V S', NULL, '10 minutes', TRUE, TRUE, 'For the duration, you sense the presence of magic within 30 feet of you.', NULL, 'OFFICIAL'),
    ('Disguise Self', 1, 'Illusion', '1 action', 'Self', 'V S', NULL, '1 hour', FALSE, FALSE, 'You make yourself look different until the spell ends.', NULL, 'OFFICIAL'),
    ('Faerie Fire', 1, 'Evocation', '1 action', '60 feet', 'V', NULL, '1 minute', TRUE, FALSE, 'Each object in a 20-foot cube within range is outlined in blue, green, or violet light.', NULL, 'OFFICIAL'),
    ('Feather Fall', 1, 'Transmutation', '1 reaction', '60 feet', 'V M', 'a small feather or piece of down', '1 minute', FALSE, FALSE, 'Choose up to five falling creatures within range. A falling creature''s rate of descent slows to 60 feet per round.', NULL, 'OFFICIAL'),
    ('Find Familiar', 1, 'Conjuration', '1 hour', '10 feet', 'V S M', '10 gp worth of charcoal incense and herbs consumed', 'Instantaneous', FALSE, TRUE, 'You gain the service of a familiar, a spirit that takes an animal form you choose.', NULL, 'OFFICIAL'),
    ('Fog Cloud', 1, 'Conjuration', '1 action', '120 feet', 'V S', NULL, '1 hour', TRUE, FALSE, 'You create a 20-foot-radius sphere of fog centered on a point within range.', 'When you cast this spell using a spell slot of 2nd level or higher, the radius of the fog increases by 20 feet for each slot level above 1st.', 'OFFICIAL'),
    ('Guiding Bolt', 1, 'Evocation', '1 action', '120 feet', 'V S', NULL, '1 round', FALSE, FALSE, 'A flash of light streaks toward a creature of your choice within range.', 'When you cast this spell using a spell slot of 2nd level or higher, the damage increases by 1d6 for each slot level above 1st.', 'OFFICIAL'),
    ('Healing Word', 1, 'Evocation', '1 bonus action', '60 feet', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'A creature of your choice that you can see within range regains hit points equal to 1d4 + your spellcasting ability modifier.', 'When you cast this spell using a spell slot of 2nd level or higher, the healing increases by 1d4 for each slot level above 1st.', 'OFFICIAL'),
    ('Hex', 1, 'Enchantment', '1 bonus action', '90 feet', 'V S M', 'the petrified eye of a newt', '1 hour', TRUE, FALSE, 'You place a curse on a creature that you can see within range.', 'When you cast this spell using a spell slot of 3rd or 4th level, you can maintain concentration for up to 8 hours.', 'OFFICIAL'),
    ('Hunter''s Mark', 1, 'Divination', '1 bonus action', '90 feet', 'V', NULL, '1 hour', TRUE, FALSE, 'You choose a creature you can see within range and mystically mark it as your quarry.', 'When you cast this spell using a spell slot of 3rd or 4th level, you can maintain concentration for up to 8 hours.', 'OFFICIAL'),
    ('Identify', 1, 'Divination', '1 minute', 'Touch', 'V S M', 'a pearl worth at least 100 gp and an owl feather', 'Instantaneous', FALSE, TRUE, 'You choose one object that you must touch throughout the casting of the spell.', NULL, 'OFFICIAL'),
    ('Mage Armor', 1, 'Abjuration', '1 action', 'Touch', 'V S M', 'a piece of cured leather', '8 hours', FALSE, FALSE, 'You touch a willing creature who isn''t wearing armor, and a protective magical force surrounds it until the spell ends.', NULL, 'OFFICIAL'),
    ('Magic Missile', 1, 'Evocation', '1 action', '120 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'You create three glowing darts of magical force. Each dart hits a creature of your choice that you can see within range.', 'When you cast this spell using a spell slot of 2nd level or higher, the spell creates one more dart for each slot level above 1st.', 'OFFICIAL'),
    ('Shield', 1, 'Abjuration', '1 reaction', 'Self', 'V S', NULL, '1 round', FALSE, FALSE, 'An invisible barrier of magical force appears and protects you. Until the start of your next turn, you have a +5 bonus to AC.', NULL, 'OFFICIAL'),
    ('Sleep', 1, 'Enchantment', '1 action', '90 feet', 'V S M', 'a pinch of fine sand rose petals or a cricket', '1 minute', FALSE, FALSE, 'This spell sends creatures into a magical slumber.', 'When you cast this spell using a spell slot of 2nd level or higher, roll an additional 2d8 for each slot level above 1st.', 'OFFICIAL'),
    ('Thunderwave', 1, 'Evocation', '1 action', 'Self (15-foot cube)', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'A wave of thunderous force sweeps out from you.', 'When you cast this spell using a spell slot of 2nd level or higher, the damage increases by 1d8 for each slot level above 1st.', 'OFFICIAL'),
    -- 2nd Level Spells
    ('Aid', 2, 'Abjuration', '1 action', '30 feet', 'V S M', 'a tiny strip of white cloth', '8 hours', FALSE, FALSE, 'Your spell bolsters your allies with toughness and resolve.', 'When you cast this spell using a spell slot of 3rd level or higher, a target''s hit points increase by an additional 5 for each slot level above 2nd.', 'OFFICIAL'),
    ('Blur', 2, 'Illusion', '1 action', 'Self', 'V', NULL, '1 minute', TRUE, FALSE, 'Your body becomes blurred, shifting and wavering to all who can see you.', NULL, 'OFFICIAL'),
    ('Darkness', 2, 'Evocation', '1 action', '60 feet', 'V M', 'bat fur and a drop of pitch', '10 minutes', TRUE, FALSE, 'Magical darkness spreads from a point you choose within range to fill a 15-foot-radius sphere.', NULL, 'OFFICIAL'),
    ('Hold Person', 2, 'Enchantment', '1 action', '60 feet', 'V S M', 'a small straight piece of iron', '1 minute', TRUE, FALSE, 'Choose a humanoid that you can see within range. The target must succeed on a Wisdom saving throw or be paralyzed.', 'When you cast this spell using a spell slot of 3rd level or higher, you can target one additional humanoid for each slot level above 2nd.', 'OFFICIAL'),
    ('Invisibility', 2, 'Illusion', '1 action', 'Touch', 'V S M', 'an eyelash encased in gum arabic', '1 hour', TRUE, FALSE, 'A creature you touch becomes invisible until the spell ends.', 'When you cast this spell using a spell slot of 3rd level or higher, you can target one additional creature for each slot level above 2nd.', 'OFFICIAL'),
    ('Lesser Restoration', 2, 'Abjuration', '1 action', 'Touch', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'You touch a creature and can end either one disease or one condition afflicting it.', NULL, 'OFFICIAL'),
    ('Mirror Image', 2, 'Illusion', '1 action', 'Self', 'V S', NULL, '1 minute', FALSE, FALSE, 'Three illusory duplicates of yourself appear in your space.', NULL, 'OFFICIAL'),
    ('Misty Step', 2, 'Conjuration', '1 bonus action', 'Self', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'Briefly surrounded by silvery mist, you teleport up to 30 feet to an unoccupied space that you can see.', NULL, 'OFFICIAL'),
    ('Scorching Ray', 2, 'Evocation', '1 action', '120 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'You create three rays of fire and hurl them at targets within range.', 'When you cast this spell using a spell slot of 3rd level or higher, you create one additional ray for each slot level above 2nd.', 'OFFICIAL'),
    ('Shatter', 2, 'Evocation', '1 action', '60 feet', 'V S M', 'a chip of mica', 'Instantaneous', FALSE, FALSE, 'A sudden loud ringing noise, painfully intense, erupts from a point of your choice within range.', 'When you cast this spell using a spell slot of 3rd level or higher, the damage increases by 1d8 for each slot level above 2nd.', 'OFFICIAL'),
    ('Spiritual Weapon', 2, 'Evocation', '1 bonus action', '60 feet', 'V S', NULL, '1 minute', FALSE, FALSE, 'You create a floating, spectral weapon within range that lasts for the duration.', 'When you cast this spell using a spell slot of 3rd level or higher, the damage increases by 1d8 for every two slot levels above 2nd.', 'OFFICIAL'),
    ('Web', 2, 'Conjuration', '1 action', '60 feet', 'V S M', 'a bit of spiderweb', '1 hour', TRUE, FALSE, 'You conjure a mass of thick, sticky webbing at a point of your choice within range.', NULL, 'OFFICIAL'),
    -- 3rd Level Spells
    ('Counterspell', 3, 'Abjuration', '1 reaction', '60 feet', 'S', NULL, 'Instantaneous', FALSE, FALSE, 'You attempt to interrupt a creature in the process of casting a spell.', 'When you cast this spell using a spell slot of 4th level or higher, the interrupted spell has no effect if its level is less than or equal to the level of the spell slot you used.', 'OFFICIAL'),
    ('Dispel Magic', 3, 'Abjuration', '1 action', '120 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'Choose one creature, object, or magical effect within range. Any spell of 3rd level or lower on the target ends.', 'When you cast this spell using a spell slot of 4th level or higher, you automatically end the effects of a spell on the target if the spell''s level is equal to or less than the level of the spell slot you used.', 'OFFICIAL'),
    ('Fireball', 3, 'Evocation', '1 action', '150 feet', 'V S M', 'a tiny ball of bat guano and sulfur', 'Instantaneous', FALSE, FALSE, 'A bright streak flashes from your pointing finger to a point you choose within range and then blossoms with a low roar into an explosion of flame.', 'When you cast this spell using a spell slot of 4th level or higher, the damage increases by 1d6 for each slot level above 3rd.', 'OFFICIAL'),
    ('Fly', 3, 'Transmutation', '1 action', 'Touch', 'V S M', 'a wing feather from any bird', '10 minutes', TRUE, FALSE, 'You touch a willing creature. The target gains a flying speed of 60 feet for the duration.', 'When you cast this spell using a spell slot of 4th level or higher, you can target one additional creature for each slot level above 3rd.', 'OFFICIAL'),
    ('Haste', 3, 'Transmutation', '1 action', '30 feet', 'V S M', 'a shaving of licorice root', '1 minute', TRUE, FALSE, 'Choose a willing creature that you can see within range. Until the spell ends, the target''s speed is doubled.', NULL, 'OFFICIAL'),
    ('Lightning Bolt', 3, 'Evocation', '1 action', 'Self (100-foot line)', 'V S M', 'a bit of fur and a rod of amber crystal', 'Instantaneous', FALSE, FALSE, 'A stroke of lightning forming a line 100 feet long and 5 feet wide blasts out from you in a direction you choose.', 'When you cast this spell using a spell slot of 4th level or higher, the damage increases by 1d6 for each slot level above 3rd.', 'OFFICIAL'),
    ('Revivify', 3, 'Necromancy', '1 action', 'Touch', 'V S M', 'diamonds worth 300 gp consumed', 'Instantaneous', FALSE, FALSE, 'You touch a creature that has died within the last minute. That creature returns to life with 1 hit point.', NULL, 'OFFICIAL'),
    ('Spirit Guardians', 3, 'Conjuration', '1 action', 'Self (15-foot radius)', 'V S M', 'a holy symbol', '10 minutes', TRUE, FALSE, 'You call forth spirits to protect you. They flit around you to a distance of 15 feet for the duration.', 'When you cast this spell using a spell slot of 4th level or higher, the damage increases by 1d8 for each slot level above 3rd.', 'OFFICIAL'),
    -- 4th Level Spells
    ('Banishment', 4, 'Abjuration', '1 action', '60 feet', 'V S M', 'an item distasteful to the target', '1 minute', TRUE, FALSE, 'You attempt to send one creature that you can see within range to another plane of existence.', 'When you cast this spell using a spell slot of 5th level or higher, you can target one additional creature for each slot level above 4th.', 'OFFICIAL'),
    ('Dimension Door', 4, 'Conjuration', '1 action', '500 feet', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'You teleport yourself from your current location to any other spot within range.', NULL, 'OFFICIAL'),
    ('Greater Invisibility', 4, 'Illusion', '1 action', 'Touch', 'V S', NULL, '1 minute', TRUE, FALSE, 'You or a creature you touch becomes invisible until the spell ends.', NULL, 'OFFICIAL'),
    ('Polymorph', 4, 'Transmutation', '1 action', '60 feet', 'V S M', 'a caterpillar cocoon', '1 hour', TRUE, FALSE, 'This spell transforms a creature that you can see within range into a new form.', NULL, 'OFFICIAL'),
    ('Wall of Fire', 4, 'Evocation', '1 action', '120 feet', 'V S M', 'a small piece of phosphorus', '1 minute', TRUE, FALSE, 'You create a wall of fire on a solid surface within range.', 'When you cast this spell using a spell slot of 5th level or higher, the damage increases by 1d8 for each slot level above 4th.', 'OFFICIAL'),
    -- 5th Level Spells
    ('Cone of Cold', 5, 'Evocation', '1 action', 'Self (60-foot cone)', 'V S M', 'a small crystal or glass cone', 'Instantaneous', FALSE, FALSE, 'A blast of cold air erupts from your hands. Each creature in a 60-foot cone must make a Constitution saving throw.', 'When you cast this spell using a spell slot of 6th level or higher, the damage increases by 1d8 for each slot level above 5th.', 'OFFICIAL'),
    ('Greater Restoration', 5, 'Abjuration', '1 action', 'Touch', 'V S M', 'diamond dust worth at least 100 gp consumed', 'Instantaneous', FALSE, FALSE, 'You imbue a creature you touch with positive energy to undo a debilitating effect.', NULL, 'OFFICIAL'),
    ('Hold Monster', 5, 'Enchantment', '1 action', '90 feet', 'V S M', 'a small straight piece of iron', '1 minute', TRUE, FALSE, 'Choose a creature that you can see within range. The target must succeed on a Wisdom saving throw or be paralyzed.', 'When you cast this spell using a spell slot of 6th level or higher, you can target one additional creature for each slot level above 5th.', 'OFFICIAL'),
    ('Mass Cure Wounds', 5, 'Evocation', '1 action', '60 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'A wave of healing energy washes out from a point of your choice within range.', 'When you cast this spell using a spell slot of 6th level or higher, the healing increases by 1d8 for each slot level above 5th.', 'OFFICIAL'),
    ('Raise Dead', 5, 'Necromancy', '1 hour', 'Touch', 'V S M', 'a diamond worth at least 500 gp consumed', 'Instantaneous', FALSE, FALSE, 'You return a dead creature you touch to life, provided that it has been dead no longer than 10 days.', NULL, 'OFFICIAL'),
    ('Telekinesis', 5, 'Transmutation', '1 action', '60 feet', 'V S', NULL, '10 minutes', TRUE, FALSE, 'You gain the ability to move or manipulate creatures or objects by thought.', NULL, 'OFFICIAL'),
    ('Wall of Force', 5, 'Evocation', '1 action', '120 feet', 'V S M', 'a pinch of powder made by crushing a clear gemstone', '10 minutes', TRUE, FALSE, 'An invisible wall of force springs into existence at a point you choose within range.', NULL, 'OFFICIAL'),
    -- 6th Level Spells
    ('Chain Lightning', 6, 'Evocation', '1 action', '150 feet', 'V S M', 'a bit of fur, a piece of amber, and three silver pins', 'Instantaneous', FALSE, FALSE, 'You create a bolt of lightning that arcs toward a target of your choice that you can see within range.', 'When you cast this spell using a spell slot of 7th level or higher, one additional bolt leaps from the first target to another target for each slot level above 6th.', 'OFFICIAL'),
    ('Disintegrate', 6, 'Transmutation', '1 action', '60 feet', 'V S M', 'a lodestone and a pinch of dust', 'Instantaneous', FALSE, FALSE, 'A thin green ray springs from your pointing finger to a target that you can see within range.', 'When you cast this spell using a spell slot of 7th level or higher, the damage increases by 3d6 for each slot level above 6th.', 'OFFICIAL'),
    ('Heal', 6, 'Evocation', '1 action', '60 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'Choose a creature that you can see within range. A surge of positive energy washes through the creature, causing it to regain 70 hit points.', 'When you cast this spell using a spell slot of 7th level or higher, the amount of healing increases by 10 for each slot level above 6th.', 'OFFICIAL'),
    ('Sunbeam', 6, 'Evocation', '1 action', 'Self (60-foot line)', 'V S M', 'a magnifying glass', '1 minute', TRUE, FALSE, 'A beam of brilliant light flashes out from your hand in a 5-foot-wide, 60-foot-long line.', NULL, 'OFFICIAL'),
    ('True Seeing', 6, 'Divination', '1 action', 'Touch', 'V S M', 'an ointment for the eyes worth 25 gp consumed', '1 hour', FALSE, FALSE, 'This spell gives the willing creature you touch the ability to see things as they actually are.', NULL, 'OFFICIAL'),
    -- 7th Level Spells
    ('Finger of Death', 7, 'Necromancy', '1 action', '60 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'You send negative energy coursing through a creature that you can see within range, causing it searing pain.', NULL, 'OFFICIAL'),
    ('Plane Shift', 7, 'Conjuration', '1 action', 'Touch', 'V S M', 'a forked metal rod worth at least 250 gp', 'Instantaneous', FALSE, FALSE, 'You and up to eight willing creatures who link hands in a circle are transported to a different plane of existence.', NULL, 'OFFICIAL'),
    ('Regenerate', 7, 'Transmutation', '1 minute', 'Touch', 'V S M', 'a prayer wheel and holy water', '1 hour', FALSE, FALSE, 'You touch a creature and stimulate its natural healing ability.', NULL, 'OFFICIAL'),
    ('Resurrection', 7, 'Necromancy', '1 hour', 'Touch', 'V S M', 'a diamond worth at least 1000 gp consumed', 'Instantaneous', FALSE, FALSE, 'You touch a dead creature that has been dead for no more than a century.', NULL, 'OFFICIAL'),
    ('Teleport', 7, 'Conjuration', '1 action', '10 feet', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'This spell instantly transports you and up to eight willing creatures of your choice.', NULL, 'OFFICIAL'),
    -- 8th Level Spells
    ('Dominate Monster', 8, 'Enchantment', '1 action', '60 feet', 'V S', NULL, '1 hour', TRUE, FALSE, 'You attempt to beguile a creature that you can see within range.', 'When you cast this spell with a 9th-level spell slot, the duration is concentration, up to 8 hours.', 'OFFICIAL'),
    ('Earthquake', 8, 'Evocation', '1 action', '500 feet', 'V S M', 'a pinch of dirt, a piece of rock, and a lump of clay', '1 minute', TRUE, FALSE, 'You create a seismic disturbance at a point on the ground that you can see within range.', NULL, 'OFFICIAL'),
    ('Mind Blank', 8, 'Abjuration', '1 action', 'Touch', 'V S', NULL, '24 hours', FALSE, FALSE, 'Until the spell ends, one willing creature you touch is immune to psychic damage.', NULL, 'OFFICIAL'),
    ('Power Word Stun', 8, 'Enchantment', '1 action', '60 feet', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'You speak a word of power that can overwhelm the mind of one creature you can see within range.', NULL, 'OFFICIAL'),
    ('Sunburst', 8, 'Evocation', '1 action', '150 feet', 'V S M', 'fire and a piece of sunstone', 'Instantaneous', FALSE, FALSE, 'Brilliant sunlight flashes in a 60-foot radius centered on a point you choose within range.', NULL, 'OFFICIAL'),
    -- 9th Level Spells
    ('Foresight', 9, 'Divination', '1 minute', 'Touch', 'V S M', 'a hummingbird feather', '8 hours', FALSE, FALSE, 'You touch a willing creature and bestow a limited ability to see into the immediate future.', NULL, 'OFFICIAL'),
    ('Gate', 9, 'Conjuration', '1 action', '60 feet', 'V S M', 'a diamond worth at least 5000 gp', '1 minute', TRUE, FALSE, 'You conjure a portal linking an unoccupied space you can see within range to a precise location on a different plane.', NULL, 'OFFICIAL'),
    ('Mass Heal', 9, 'Evocation', '1 action', '60 feet', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'A flood of healing energy flows from you into injured creatures around you. You restore up to 700 hit points.', NULL, 'OFFICIAL'),
    ('Meteor Swarm', 9, 'Evocation', '1 action', '1 mile', 'V S', NULL, 'Instantaneous', FALSE, FALSE, 'Blazing orbs of fire plummet to the ground at four different points you can see within range.', NULL, 'OFFICIAL'),
    ('Power Word Kill', 9, 'Enchantment', '1 action', '60 feet', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'You utter a word of power that can compel one creature you can see within range to die instantly.', NULL, 'OFFICIAL'),
    ('Time Stop', 9, 'Transmutation', '1 action', 'Self', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'You briefly stop the flow of time for everyone but yourself.', NULL, 'OFFICIAL'),
    ('True Resurrection', 9, 'Necromancy', '1 hour', 'Touch', 'V S M', 'holy water and diamonds worth at least 25000 gp consumed', 'Instantaneous', FALSE, FALSE, 'You touch a creature that has been dead for no longer than 200 years and that died for any reason except old age.', NULL, 'OFFICIAL'),
    ('Wish', 9, 'Conjuration', '1 action', 'Self', 'V', NULL, 'Instantaneous', FALSE, FALSE, 'Wish is the mightiest spell a mortal creature can cast. By simply speaking aloud, you can alter the very foundations of reality.', NULL, 'OFFICIAL')
)
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, material_components, duration, concentration, ritual, description, higher_levels, source)
SELECT
    sd.name, sd.level, s.id, sd.casting_time, sd.spell_range, sd.components, sd.material_components,
    sd.duration, sd.concentration, sd.ritual, sd.description, sd.higher_levels, sd.source
FROM spell_data sd
JOIN schools s ON s.name = sd.school_name;

-- =============================================
-- SECTION 5: Monsters
-- =============================================

WITH
sizes AS (SELECT id, name FROM monster_sizes),
types AS (SELECT id, name FROM monster_types),
monster_data (index, name, size_name, type_name, subtype, alignment, armor_class, armor_type, hit_points, hit_dice, speed, strength, dexterity, constitution, intelligence, wisdom, charisma, saving_throws, skills, senses, languages, challenge_rating, xp, proficiency_bonus, special_abilities, actions, reactions, legendary_actions, legendary_desc, source) AS (
    VALUES
    ('aboleth', 'Aboleth', 'Large', 'Aberration', NULL::VARCHAR, 'lawful evil', 17::SMALLINT, 'natural armor', 135::SMALLINT, '18d10+36', '{"walk": "10 ft.", "swim": "40 ft."}', 21::SMALLINT, 9::SMALLINT, 15::SMALLINT, 18::SMALLINT, 15::SMALLINT, 18::SMALLINT, '{"constitution": 6, "intelligence": 8, "wisdom": 6}', '{"history": 12, "perception": 10}', '{"darkvision": "120 ft.", "passive_perception": 20}', 'Deep Speech, telepathy 120 ft.', '10', 5900, 4::SMALLINT, '[{"name": "Amphibious", "desc": "The aboleth can breathe air and water."}]', '[{"name": "Multiattack", "desc": "The aboleth makes three tentacle attacks."}]', NULL::TEXT, '[{"name": "Detect", "desc": "The aboleth makes a Wisdom (Perception) check."}]', 'The aboleth can take 3 legendary actions.', 'OFFICIAL'),
    ('beholder', 'Beholder', 'Large', 'Aberration', NULL, 'lawful evil', 18, 'natural armor', 180, '19d10+76', '{"walk": "0 ft.", "fly": "20 ft. (hover)"}', 10, 14, 18, 17, 15, 17, '{"intelligence": 7, "wisdom": 7, "charisma": 7}', '{"perception": 12}', '{"darkvision": "120 ft.", "passive_perception": 22}', 'Deep Speech, Undercommon', '13', 10000, 5, '[{"name": "Antimagic Cone", "desc": "The beholder''s central eye creates an area of antimagic in a 150-foot cone."}]', '[{"name": "Bite", "desc": "Melee Weapon Attack: +5 to hit, reach 5 ft. Hit: 14 (4d6) piercing damage."}, {"name": "Eye Rays", "desc": "The beholder shoots three of its magical eye rays at random."}]', NULL, '[{"name": "Eye Ray", "desc": "The beholder uses one random eye ray."}]', 'The beholder can take 3 legendary actions.', 'OFFICIAL'),
    ('wolf', 'Wolf', 'Medium', 'Beast', NULL, 'unaligned', 13, 'natural armor', 11, '2d8+2', '{"walk": "40 ft."}', 12, 15, 12, 3, 12, 6, NULL, '{"perception": 3, "stealth": 4}', '{"passive_perception": 13}', '-', '1/4', 50, 2, '[{"name": "Keen Hearing and Smell", "desc": "The wolf has advantage on Wisdom (Perception) checks that rely on hearing or smell."}, {"name": "Pack Tactics", "desc": "The wolf has advantage on attack rolls against a creature if at least one of the wolf''s allies is within 5 feet of the creature."}]', '[{"name": "Bite", "desc": "Melee Weapon Attack: +4 to hit, reach 5 ft., one target. Hit: 7 (2d4 + 2) piercing damage. DC 11 Strength save or be knocked prone."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('dire-wolf', 'Dire Wolf', 'Large', 'Beast', NULL, 'unaligned', 14, 'natural armor', 37, '5d10+10', '{"walk": "50 ft."}', 17, 15, 15, 3, 12, 7, NULL, '{"perception": 3, "stealth": 4}', '{"passive_perception": 13}', '-', '1', 200, 2, '[{"name": "Keen Hearing and Smell", "desc": "The dire wolf has advantage on Wisdom (Perception) checks that rely on hearing or smell."}, {"name": "Pack Tactics", "desc": "The dire wolf has advantage on attack rolls against a creature if at least one of the wolf''s allies is within 5 feet of the creature."}]', '[{"name": "Bite", "desc": "Melee Weapon Attack: +5 to hit, reach 5 ft., one target. Hit: 10 (2d6 + 3) piercing damage. DC 13 Strength save or be knocked prone."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('adult-red-dragon', 'Adult Red Dragon', 'Huge', 'Dragon', NULL, 'chaotic evil', 19, 'natural armor', 256, '19d12+133', '{"walk": "40 ft.", "climb": "40 ft.", "fly": "80 ft."}', 27, 10, 25, 16, 13, 21, '{"dexterity": 6, "constitution": 13, "wisdom": 7, "charisma": 11}', '{"perception": 13, "stealth": 6}', '{"blindsight": "60 ft.", "darkvision": "120 ft.", "passive_perception": 23}', 'Common, Draconic', '17', 18000, 6, '[{"name": "Legendary Resistance (3/Day)", "desc": "If the dragon fails a saving throw, it can choose to succeed instead."}]', '[{"name": "Multiattack", "desc": "The dragon can use its Frightful Presence. It then makes three attacks."}, {"name": "Fire Breath (Recharge 5-6)", "desc": "60-foot cone. DC 21 Dexterity save, 63 (18d6) fire damage."}]', NULL, '[{"name": "Detect", "desc": "The dragon makes a Wisdom (Perception) check."}, {"name": "Tail Attack", "desc": "The dragon makes a tail attack."}, {"name": "Wing Attack (Costs 2 Actions)", "desc": "DC 22 Dexterity save or take 15 (2d6 + 8) bludgeoning damage and be knocked prone."}]', 'The dragon can take 3 legendary actions.', 'OFFICIAL'),
    ('goblin', 'Goblin', 'Small', 'Humanoid', 'goblinoid', 'neutral evil', 15, 'leather armor, shield', 7, '2d6', '{"walk": "30 ft."}', 8, 14, 10, 10, 8, 8, NULL, '{"stealth": 6}', '{"darkvision": "60 ft.", "passive_perception": 9}', 'Common, Goblin', '1/4', 50, 2, '[{"name": "Nimble Escape", "desc": "The goblin can take the Disengage or Hide action as a bonus action on each of its turns."}]', '[{"name": "Scimitar", "desc": "Melee Weapon Attack: +4 to hit, reach 5 ft. Hit: 5 (1d6 + 2) slashing damage."}, {"name": "Shortbow", "desc": "Ranged Weapon Attack: +4 to hit, range 80/320 ft. Hit: 5 (1d6 + 2) piercing damage."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('orc', 'Orc', 'Medium', 'Humanoid', 'orc', 'chaotic evil', 13, 'hide armor', 15, '2d8+6', '{"walk": "30 ft."}', 16, 12, 16, 7, 11, 10, NULL, '{"intimidation": 2}', '{"darkvision": "60 ft.", "passive_perception": 10}', 'Common, Orc', '1/2', 100, 2, '[{"name": "Aggressive", "desc": "As a bonus action, the orc can move up to its speed toward a hostile creature that it can see."}]', '[{"name": "Greataxe", "desc": "Melee Weapon Attack: +5 to hit, reach 5 ft. Hit: 9 (1d12 + 3) slashing damage."}, {"name": "Javelin", "desc": "Melee or Ranged Weapon Attack: +5 to hit, reach 5 ft. or range 30/120 ft. Hit: 6 (1d6 + 3) piercing damage."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('skeleton', 'Skeleton', 'Medium', 'Undead', NULL, 'lawful evil', 13, 'armor scraps', 13, '2d8+4', '{"walk": "30 ft."}', 10, 14, 15, 6, 8, 5, NULL, NULL, '{"darkvision": "60 ft.", "passive_perception": 9}', 'understands languages it knew in life', '1/4', 50, 2, NULL, '[{"name": "Shortsword", "desc": "Melee Weapon Attack: +4 to hit, reach 5 ft. Hit: 5 (1d6 + 2) piercing damage."}, {"name": "Shortbow", "desc": "Ranged Weapon Attack: +4 to hit, range 80/320 ft. Hit: 5 (1d6 + 2) piercing damage."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('zombie', 'Zombie', 'Medium', 'Undead', NULL, 'neutral evil', 8, NULL, 22, '3d8+9', '{"walk": "20 ft."}', 13, 6, 16, 3, 6, 5, '{"wisdom": 0}', NULL, '{"darkvision": "60 ft.", "passive_perception": 8}', 'understands languages it knew in life', '1/4', 50, 2, '[{"name": "Undead Fortitude", "desc": "If damage reduces the zombie to 0 hit points, it must make a Constitution saving throw."}]', '[{"name": "Slam", "desc": "Melee Weapon Attack: +3 to hit, reach 5 ft. Hit: 4 (1d6 + 1) bludgeoning damage."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('vampire', 'Vampire', 'Medium', 'Undead', 'shapechanger', 'lawful evil', 16, 'natural armor', 144, '17d8+68', '{"walk": "30 ft."}', 18, 18, 18, 17, 15, 18, '{"dexterity": 9, "wisdom": 7, "charisma": 9}', '{"perception": 7, "stealth": 9}', '{"darkvision": "120 ft.", "passive_perception": 17}', 'the languages it knew in life', '13', 10000, 5, '[{"name": "Shapechanger", "desc": "The vampire can use its action to polymorph into a Tiny bat or a Medium cloud of mist."}, {"name": "Legendary Resistance (3/Day)", "desc": "If the vampire fails a saving throw, it can choose to succeed instead."}, {"name": "Regeneration", "desc": "The vampire regains 20 hit points at the start of its turn."}]', '[{"name": "Multiattack", "desc": "The vampire makes two attacks, only one of which can be a bite attack."}, {"name": "Bite", "desc": "Melee Weapon Attack: +9 to hit, reach 5 ft. Hit: 7 (1d6 + 4) piercing damage plus 10 (3d6) necrotic damage."}]', NULL, '[{"name": "Move", "desc": "The vampire moves up to its speed without provoking opportunity attacks."}, {"name": "Bite (Costs 2 Actions)", "desc": "The vampire makes one bite attack."}]', 'The vampire can take 3 legendary actions.', 'OFFICIAL'),
    ('lich', 'Lich', 'Medium', 'Undead', NULL, 'any evil alignment', 17, 'natural armor', 135, '18d8+54', '{"walk": "30 ft."}', 11, 16, 16, 20, 14, 16, '{"constitution": 10, "intelligence": 12, "wisdom": 9}', '{"arcana": 18, "history": 12, "insight": 9, "perception": 9}', '{"truesight": "120 ft.", "passive_perception": 19}', 'Common plus up to five other languages', '21', 33000, 7, '[{"name": "Legendary Resistance (3/Day)", "desc": "If the lich fails a saving throw, it can choose to succeed instead."}, {"name": "Rejuvenation", "desc": "If it has a phylactery, a destroyed lich gains a new body in 1d10 days."}, {"name": "Turn Resistance", "desc": "The lich has advantage on saving throws against any effect that turns undead."}]', '[{"name": "Paralyzing Touch", "desc": "Melee Spell Attack: +12 to hit, reach 5 ft. Hit: 10 (3d6) cold damage. DC 18 Constitution save or be paralyzed for 1 minute."}]', NULL, '[{"name": "Cantrip", "desc": "The lich casts a cantrip."}, {"name": "Paralyzing Touch (Costs 2 Actions)", "desc": "The lich uses its Paralyzing Touch."}, {"name": "Disrupt Life (Costs 3 Actions)", "desc": "Each non-undead creature within 20 feet must make a DC 18 Constitution saving throw."}]', 'The lich can take 3 legendary actions.', 'OFFICIAL'),
    ('owlbear', 'Owlbear', 'Large', 'Monstrosity', NULL, 'unaligned', 13, 'natural armor', 59, '7d10+21', '{"walk": "40 ft."}', 20, 12, 17, 3, 12, 7, NULL, '{"perception": 3}', '{"darkvision": "60 ft.", "passive_perception": 13}', '-', '3', 700, 2, '[{"name": "Keen Sight and Smell", "desc": "The owlbear has advantage on Wisdom (Perception) checks that rely on sight or smell."}]', '[{"name": "Multiattack", "desc": "The owlbear makes two attacks: one with its beak and one with its claws."}, {"name": "Beak", "desc": "Melee Weapon Attack: +7 to hit, reach 5 ft. Hit: 10 (1d10 + 5) piercing damage."}, {"name": "Claws", "desc": "Melee Weapon Attack: +7 to hit, reach 5 ft. Hit: 14 (2d8 + 5) slashing damage."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('mimic', 'Mimic', 'Medium', 'Monstrosity', 'shapechanger', 'neutral', 12, 'natural armor', 58, '9d8+18', '{"walk": "15 ft."}', 17, 12, 15, 5, 13, 8, NULL, '{"stealth": 5}', '{"darkvision": "60 ft.", "passive_perception": 11}', '-', '2', 450, 2, '[{"name": "Shapechanger", "desc": "The mimic can use its action to polymorph into an object or back into its true, amorphous form."}, {"name": "Adhesive", "desc": "The mimic adheres to anything that touches it."}, {"name": "False Appearance", "desc": "While the mimic remains motionless, it is indistinguishable from an ordinary object."}]', '[{"name": "Pseudopod", "desc": "Melee Weapon Attack: +5 to hit, reach 5 ft. Hit: 7 (1d8 + 3) bludgeoning damage."}, {"name": "Bite", "desc": "Melee Weapon Attack: +5 to hit, reach 5 ft. Hit: 7 (1d8 + 3) piercing damage plus 4 (1d8) acid damage."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('tarrasque', 'Tarrasque', 'Gargantuan', 'Monstrosity', 'titan', 'unaligned', 25, 'natural armor', 676, '33d20+330', '{"walk": "40 ft."}', 30, 11, 30, 3, 11, 11, '{"intelligence": 5, "wisdom": 9, "charisma": 9}', NULL, '{"blindsight": "120 ft.", "passive_perception": 10}', '-', '30', 155000, 9, '[{"name": "Legendary Resistance (3/Day)", "desc": "If the tarrasque fails a saving throw, it can choose to succeed instead."}, {"name": "Magic Resistance", "desc": "The tarrasque has advantage on saving throws against spells and other magical effects."}, {"name": "Reflective Carapace", "desc": "Any time the tarrasque is targeted by a magic missile spell, a line spell, or a spell that requires a ranged attack roll, roll a d6."}]', '[{"name": "Multiattack", "desc": "The tarrasque can use its Frightful Presence. It then makes five attacks."}, {"name": "Bite", "desc": "Melee Weapon Attack: +19 to hit, reach 10 ft. Hit: 36 (4d12 + 10) piercing damage."}]', NULL, '[{"name": "Attack", "desc": "The tarrasque makes one claw attack or tail attack."}, {"name": "Move", "desc": "The tarrasque moves up to half its speed."}, {"name": "Chomp (Costs 2 Actions)", "desc": "The tarrasque makes one bite attack or uses its Swallow."}]', 'The tarrasque can take 3 legendary actions.', 'OFFICIAL'),
    ('troll', 'Troll', 'Large', 'Giant', NULL, 'chaotic evil', 15, 'natural armor', 84, '8d10+40', '{"walk": "30 ft."}', 18, 13, 20, 7, 9, 7, NULL, NULL, '{"darkvision": "60 ft.", "passive_perception": 12}', 'Giant', '5', 1800, 3, '[{"name": "Keen Smell", "desc": "The troll has advantage on Wisdom (Perception) checks that rely on smell."}, {"name": "Regeneration", "desc": "The troll regains 10 hit points at the start of its turn."}]', '[{"name": "Multiattack", "desc": "The troll makes three attacks: one with its bite and two with its claws."}, {"name": "Bite", "desc": "Melee Weapon Attack: +7 to hit, reach 5 ft. Hit: 7 (1d6 + 4) piercing damage."}, {"name": "Claw", "desc": "Melee Weapon Attack: +7 to hit, reach 5 ft. Hit: 11 (2d6 + 4) slashing damage."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('imp', 'Imp', 'Tiny', 'Fiend', 'devil, shapechanger', 'lawful evil', 13, NULL, 10, '3d4+3', '{"walk": "20 ft.", "fly": "40 ft."}', 6, 17, 13, 11, 12, 14, NULL, '{"deception": 4, "insight": 3, "persuasion": 4, "stealth": 5}', '{"darkvision": "120 ft.", "passive_perception": 11}', 'Infernal, Common', '1', 200, 2, '[{"name": "Shapechanger", "desc": "The imp can use its action to polymorph into a beast form."}, {"name": "Devil''s Sight", "desc": "Magical darkness doesn''t impede the imp''s darkvision."}, {"name": "Magic Resistance", "desc": "The imp has advantage on saving throws against spells."}]', '[{"name": "Sting", "desc": "Melee Weapon Attack: +5 to hit, reach 5 ft. Hit: 5 (1d4 + 3) piercing damage plus 10 (3d6) poison damage."}, {"name": "Invisibility", "desc": "The imp magically turns invisible until it attacks."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('balor', 'Balor', 'Huge', 'Fiend', 'demon', 'chaotic evil', 19, 'natural armor', 262, '21d12+126', '{"walk": "40 ft.", "fly": "80 ft."}', 26, 15, 22, 20, 16, 22, '{"strength": 14, "constitution": 12, "wisdom": 9, "charisma": 12}', NULL, '{"truesight": "120 ft.", "passive_perception": 13}', 'Abyssal, telepathy 120 ft.', '19', 22000, 6, '[{"name": "Death Throes", "desc": "When the balor dies, it explodes. Each creature within 30 feet must make a DC 20 Dexterity saving throw."}, {"name": "Fire Aura", "desc": "At the start of each of the balor''s turns, each creature within 5 feet takes 10 (3d6) fire damage."}, {"name": "Magic Resistance", "desc": "The balor has advantage on saving throws against spells."}]', '[{"name": "Multiattack", "desc": "The balor makes two attacks: one with its longsword and one with its whip."}, {"name": "Longsword", "desc": "Melee Weapon Attack: +14 to hit, reach 10 ft. Hit: 21 (3d8 + 8) slashing damage plus 13 (3d8) lightning damage."}]', NULL, NULL, NULL, 'OFFICIAL'),
    ('gelatinous-cube', 'Gelatinous Cube', 'Large', 'Ooze', NULL, 'unaligned', 6, NULL, 84, '8d10+40', '{"walk": "15 ft."}', 14, 3, 20, 1, 6, 1, NULL, NULL, '{"blindsight": "60 ft. (blind beyond this radius)", "passive_perception": 8}', '-', '2', 450, 2, '[{"name": "Ooze Cube", "desc": "The cube takes up its entire space."}, {"name": "Transparent", "desc": "Even when the cube is in plain sight, it takes a successful DC 15 Wisdom (Perception) check to spot."}]', '[{"name": "Pseudopod", "desc": "Melee Weapon Attack: +4 to hit, reach 5 ft. Hit: 10 (3d6) acid damage."}, {"name": "Engulf", "desc": "The cube moves up to its speed. A creature whose space the cube enters must make a DC 12 Dexterity saving throw."}]', NULL, NULL, NULL, 'OFFICIAL')
)
INSERT INTO monsters (index, name, size_id, type_id, subtype, alignment, armor_class, armor_type, hit_points, hit_dice, speed, strength, dexterity, constitution, intelligence, wisdom, charisma, saving_throws, skills, senses, languages, challenge_rating, xp, proficiency_bonus, special_abilities, actions, reactions, legendary_actions, legendary_desc, source)
SELECT
    md.index, md.name, s.id, t.id, md.subtype, md.alignment, md.armor_class, md.armor_type, md.hit_points, md.hit_dice,
    md.speed::JSONB, md.strength, md.dexterity, md.constitution, md.intelligence, md.wisdom, md.charisma,
    md.saving_throws::JSONB, md.skills::JSONB, md.senses::JSONB, md.languages, md.challenge_rating, md.xp, md.proficiency_bonus,
    md.special_abilities::JSONB, md.actions::JSONB, md.reactions::JSONB, md.legendary_actions::JSONB, md.legendary_desc, md.source
FROM monster_data md
JOIN sizes s ON s.name = md.size_name
JOIN types t ON t.name = md.type_name;

-- =============================================
-- SECTION 6: Magic Items
-- =============================================

INSERT INTO magic_items (name, rarity_id, type_id, requires_attunement, attunement_requirements, description, source) VALUES
    ('Potion of Healing', (SELECT id FROM magic_item_rarities WHERE name = 'Common'), (SELECT id FROM magic_item_types WHERE name = 'Potion'), FALSE, NULL, 'You regain 2d4 + 2 hit points when you drink this potion.', 'OFFICIAL'),
    ('+1 Weapon', (SELECT id FROM magic_item_rarities WHERE name = 'Uncommon'), (SELECT id FROM magic_item_types WHERE name = 'Weapon'), FALSE, NULL, 'You have a +1 bonus to attack and damage rolls made with this magic weapon.', 'OFFICIAL'),
    ('+1 Armor', (SELECT id FROM magic_item_rarities WHERE name = 'Uncommon'), (SELECT id FROM magic_item_types WHERE name = 'Armor'), FALSE, NULL, 'You have a +1 bonus to AC while wearing this armor.', 'OFFICIAL'),
    ('Bag of Holding', (SELECT id FROM magic_item_rarities WHERE name = 'Uncommon'), (SELECT id FROM magic_item_types WHERE name = 'Wondrous Item'), FALSE, NULL, 'This bag has an interior space considerably larger than its outside dimensions.', 'OFFICIAL'),
    ('Cloak of Protection', (SELECT id FROM magic_item_rarities WHERE name = 'Uncommon'), (SELECT id FROM magic_item_types WHERE name = 'Wondrous Item'), TRUE, NULL, 'You gain a +1 bonus to AC and saving throws while you wear this cloak.', 'OFFICIAL'),
    ('Gauntlets of Ogre Power', (SELECT id FROM magic_item_rarities WHERE name = 'Uncommon'), (SELECT id FROM magic_item_types WHERE name = 'Wondrous Item'), TRUE, NULL, 'Your Strength score is 19 while you wear these gauntlets.', 'OFFICIAL'),
    ('Ring of Protection', (SELECT id FROM magic_item_rarities WHERE name = 'Uncommon'), (SELECT id FROM magic_item_types WHERE name = 'Ring'), TRUE, NULL, 'You gain a +1 bonus to AC and saving throws while wearing this ring.', 'OFFICIAL'),
    ('+2 Weapon', (SELECT id FROM magic_item_rarities WHERE name = 'Rare'), (SELECT id FROM magic_item_types WHERE name = 'Weapon'), FALSE, NULL, 'You have a +2 bonus to attack and damage rolls made with this magic weapon.', 'OFFICIAL'),
    ('Amulet of Health', (SELECT id FROM magic_item_rarities WHERE name = 'Rare'), (SELECT id FROM magic_item_types WHERE name = 'Wondrous Item'), TRUE, NULL, 'Your Constitution score is 19 while you wear this amulet.', 'OFFICIAL'),
    ('Flame Tongue', (SELECT id FROM magic_item_rarities WHERE name = 'Rare'), (SELECT id FROM magic_item_types WHERE name = 'Weapon'), TRUE, NULL, 'You can use a bonus action to speak this magic sword''s command word, causing flames to erupt from the blade.', 'OFFICIAL'),
    ('Sun Blade', (SELECT id FROM magic_item_rarities WHERE name = 'Rare'), (SELECT id FROM magic_item_types WHERE name = 'Weapon'), TRUE, NULL, 'This item appears to be a longsword hilt. While grasping the hilt, you can use a bonus action to cause a blade of pure radiance to spring into existence.', 'OFFICIAL'),
    ('+3 Weapon', (SELECT id FROM magic_item_rarities WHERE name = 'Very Rare'), (SELECT id FROM magic_item_types WHERE name = 'Weapon'), FALSE, NULL, 'You have a +3 bonus to attack and damage rolls made with this magic weapon.', 'OFFICIAL'),
    ('Staff of Power', (SELECT id FROM magic_item_rarities WHERE name = 'Very Rare'), (SELECT id FROM magic_item_types WHERE name = 'Staff'), TRUE, 'sorcerer, warlock, or wizard', 'This staff can be wielded as a magic quarterstaff that grants a +2 bonus to attack and damage rolls made with it.', 'OFFICIAL'),
    ('Holy Avenger', (SELECT id FROM magic_item_rarities WHERE name = 'Legendary'), (SELECT id FROM magic_item_types WHERE name = 'Weapon'), TRUE, 'paladin', 'You gain a +3 bonus to attack and damage rolls made with this magic weapon.', 'OFFICIAL'),
    ('Staff of the Magi', (SELECT id FROM magic_item_rarities WHERE name = 'Legendary'), (SELECT id FROM magic_item_types WHERE name = 'Staff'), TRUE, 'sorcerer, warlock, or wizard', 'This staff can be wielded as a magic quarterstaff that grants a +2 bonus to attack and damage rolls made with it.', 'OFFICIAL'),
    ('Vorpal Sword', (SELECT id FROM magic_item_rarities WHERE name = 'Legendary'), (SELECT id FROM magic_item_types WHERE name = 'Weapon'), TRUE, NULL, 'You gain a +3 bonus to attack and damage rolls made with this magic weapon. In addition, the weapon ignores resistance to slashing damage.', 'OFFICIAL');

-- =============================================
-- SECTION 7: Equipment
-- =============================================

INSERT INTO equipment (name, category_id, cost_gp, cost_display, weight_lb, description, source) VALUES
    ('Backpack', (SELECT id FROM equipment_categories WHERE name = 'Adventuring Gear'), 2, '2 gp', 5, 'A backpack can hold one cubic foot or 30 pounds of gear.', 'OFFICIAL'),
    ('Bedroll', (SELECT id FROM equipment_categories WHERE name = 'Adventuring Gear'), 1, '1 gp', 7, 'A cloth sleep roll.', 'OFFICIAL'),
    ('Crowbar', (SELECT id FROM equipment_categories WHERE name = 'Adventuring Gear'), 2, '2 gp', 5, 'Using a crowbar grants advantage to Strength checks where leverage can be applied.', 'OFFICIAL'),
    ('Healer''s Kit', (SELECT id FROM equipment_categories WHERE name = 'Adventuring Gear'), 5, '5 gp', 3, 'This kit is a leather pouch containing bandages, salves, and splints.', 'OFFICIAL'),
    ('Rope, Hempen (50 feet)', (SELECT id FROM equipment_categories WHERE name = 'Adventuring Gear'), 1, '1 gp', 10, 'Rope has 2 hit points and can be burst with a DC 17 Strength check.', 'OFFICIAL'),
    ('Torch', (SELECT id FROM equipment_categories WHERE name = 'Adventuring Gear'), 0.01, '1 cp', 1, 'A torch burns for 1 hour, providing bright light in a 20-foot radius and dim light for an additional 20 feet.', 'OFFICIAL'),
    ('Leather Armor', (SELECT id FROM equipment_categories WHERE name = 'Armor'), 10, '10 gp', 10, 'Light armor. AC 11 + Dex modifier.', 'OFFICIAL'),
    ('Chain Mail', (SELECT id FROM equipment_categories WHERE name = 'Armor'), 75, '75 gp', 55, 'Heavy armor. AC 16. Requires Str 13. Disadvantage on stealth.', 'OFFICIAL'),
    ('Plate Armor', (SELECT id FROM equipment_categories WHERE name = 'Armor'), 1500, '1,500 gp', 65, 'Heavy armor. AC 18. Requires Str 15. Disadvantage on stealth.', 'OFFICIAL'),
    ('Shield', (SELECT id FROM equipment_categories WHERE name = 'Armor'), 10, '10 gp', 6, '+2 AC bonus.', 'OFFICIAL'),
    ('Arrows (20)', (SELECT id FROM equipment_categories WHERE name = 'Ammunition'), 1, '1 gp', 1, 'Ammunition for bows.', 'OFFICIAL'),
    ('Crossbow Bolts (20)', (SELECT id FROM equipment_categories WHERE name = 'Ammunition'), 1, '1 gp', 1.5, 'Ammunition for crossbows.', 'OFFICIAL'),
    ('Horse, Riding', (SELECT id FROM equipment_categories WHERE name = 'Mounts and Vehicles'), 75, '75 gp', NULL, 'A riding horse with a carrying capacity of 480 pounds.', 'OFFICIAL'),
    ('Warhorse', (SELECT id FROM equipment_categories WHERE name = 'Mounts and Vehicles'), 400, '400 gp', NULL, 'A trained war mount with a carrying capacity of 540 pounds.', 'OFFICIAL');
