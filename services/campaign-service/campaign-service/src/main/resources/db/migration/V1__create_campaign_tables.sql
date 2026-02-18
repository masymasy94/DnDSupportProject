-- =============================================
-- Campaign Service - Initial Schema
-- =============================================

-- Campaigns table
CREATE TABLE IF NOT EXISTS campaigns (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    dungeon_master_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    max_players INTEGER NOT NULL DEFAULT 6,
    image_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_campaigns_dungeon_master_id ON campaigns(dungeon_master_id);
CREATE INDEX IF NOT EXISTS idx_campaigns_status ON campaigns(status);

-- Campaign members table
CREATE TABLE IF NOT EXISTS campaign_members (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL REFERENCES campaigns(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    character_id BIGINT,
    role VARCHAR(20) NOT NULL DEFAULT 'PLAYER',
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_campaign_member UNIQUE (campaign_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_campaign_members_campaign_id ON campaign_members(campaign_id);
CREATE INDEX IF NOT EXISTS idx_campaign_members_user_id ON campaign_members(user_id);

-- Campaign notes table
CREATE TABLE IF NOT EXISTS campaign_notes (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL REFERENCES campaigns(id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT,
    visibility VARCHAR(10) NOT NULL DEFAULT 'PUBLIC',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_campaign_notes_campaign_id ON campaign_notes(campaign_id);
CREATE INDEX IF NOT EXISTS idx_campaign_notes_author_id ON campaign_notes(author_id);
CREATE INDEX IF NOT EXISTS idx_campaign_notes_visibility ON campaign_notes(campaign_id, visibility);
