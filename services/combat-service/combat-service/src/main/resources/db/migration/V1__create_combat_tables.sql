CREATE TABLE encounters (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    party_level INTEGER NOT NULL DEFAULT 1,
    party_size INTEGER NOT NULL DEFAULT 4,
    difficulty_rating VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE INDEX idx_encounters_campaign ON encounters(campaign_id);

CREATE TABLE encounter_participants (
    id BIGSERIAL PRIMARY KEY,
    encounter_id BIGINT NOT NULL REFERENCES encounters(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    participant_type VARCHAR(10) NOT NULL,
    initiative INTEGER NOT NULL DEFAULT 0,
    current_hp INTEGER NOT NULL,
    max_hp INTEGER NOT NULL,
    armor_class INTEGER NOT NULL DEFAULT 10,
    conditions TEXT NOT NULL DEFAULT '[]',
    is_active BOOLEAN NOT NULL DEFAULT false,
    sort_order INTEGER NOT NULL DEFAULT 0,
    monster_id BIGINT,
    source_json TEXT
);
CREATE INDEX idx_participants_encounter ON encounter_participants(encounter_id);
