CREATE TABLE campaign_quests (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL REFERENCES campaigns(id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    priority VARCHAR(10) NOT NULL DEFAULT 'MAIN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE INDEX idx_campaign_quests_campaign ON campaign_quests(campaign_id);
CREATE INDEX idx_campaign_quests_campaign_status ON campaign_quests(campaign_id, status);
