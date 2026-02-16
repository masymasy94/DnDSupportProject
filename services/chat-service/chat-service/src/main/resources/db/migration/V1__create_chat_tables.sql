-- Conversations (supports both direct and group chats)
CREATE TABLE conversations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),                          -- NULL for DMs, set for groups
    type VARCHAR(20) NOT NULL DEFAULT 'DIRECT', -- DIRECT or GROUP
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT chk_conversation_type CHECK (type IN ('DIRECT', 'GROUP'))
);

-- Conversation participants
CREATE TABLE conversation_participants (
    id BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP,
    last_read_at TIMESTAMP,
    CONSTRAINT chk_participant_role CHECK (role IN ('ADMIN', 'MEMBER')),
    CONSTRAINT uk_conversation_user UNIQUE (conversation_id, user_id)
);

-- Messages
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    message_type VARCHAR(20) NOT NULL DEFAULT 'TEXT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    edited_at TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT chk_message_type CHECK (message_type IN ('TEXT', 'SYSTEM', 'IMAGE'))
);

-- Indexes
CREATE INDEX idx_participants_user ON conversation_participants(user_id);
CREATE INDEX idx_participants_conversation ON conversation_participants(conversation_id);
CREATE INDEX idx_messages_conversation_created ON messages(conversation_id, created_at DESC);
CREATE INDEX idx_messages_sender ON messages(sender_id);
