-- =============================================
-- Decouple reference tables for compendium-service
-- Reference data now lives in compendium-service
-- =============================================

-- Drop foreign key constraints from characters table
ALTER TABLE characters DROP CONSTRAINT IF EXISTS characters_species_id_fkey;
ALTER TABLE characters DROP CONSTRAINT IF EXISTS characters_class_id_fkey;
ALTER TABLE characters DROP CONSTRAINT IF EXISTS characters_background_id_fkey;
ALTER TABLE characters DROP CONSTRAINT IF EXISTS characters_alignment_id_fkey;

-- Add denormalized name columns for display purposes (cached from compendium)
ALTER TABLE characters ADD COLUMN IF NOT EXISTS species_name VARCHAR(50);
ALTER TABLE characters ADD COLUMN IF NOT EXISTS class_name VARCHAR(30);

-- Populate denormalized columns from existing reference tables
UPDATE characters c SET
    species_name = s.name
FROM species s
WHERE c.species_id = s.id AND c.species_name IS NULL;

UPDATE characters c SET
    class_name = cc.name
FROM character_classes cc
WHERE c.class_id = cc.id AND c.class_name IS NULL;

-- Rename columns for clarity (store as integer IDs)
ALTER TABLE characters RENAME COLUMN species_id TO compendium_species_id;
ALTER TABLE characters RENAME COLUMN class_id TO compendium_class_id;

-- Drop the reference tables (data now in compendium-service)
DROP TABLE IF EXISTS species CASCADE;
DROP TABLE IF EXISTS character_classes CASCADE;
DROP TABLE IF EXISTS backgrounds CASCADE;
DROP TABLE IF EXISTS alignments CASCADE;

-- Note: abilities, skills, spells tables are kept for character_saving_throws,
-- character_skills, and character_spells relationships.
-- These can be migrated in a future iteration.
