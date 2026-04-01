CREATE EXTENSION IF NOT EXISTS vector;

-- Ingestion tracking
CREATE TABLE document_ingestions (
    document_id VARCHAR(255) PRIMARY KEY,
    file_name VARCHAR(500) NOT NULL,
    content_type VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    chunk_count INTEGER DEFAULT 0,
    error_message TEXT,
    uploaded_by VARCHAR(255),
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Document chunks with vector embeddings (384 dimensions for in-process all-minilm-l6-v2)
CREATE TABLE document_chunks (
    id BIGSERIAL PRIMARY KEY,
    document_id VARCHAR(255) NOT NULL,
    chunk_index INTEGER NOT NULL,
    content TEXT NOT NULL,
    embedding vector(384),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_chunks_document_id ON document_chunks(document_id);
CREATE INDEX idx_chunks_embedding ON document_chunks USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);

-- Conversations
CREATE TABLE conversations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    campaign_id BIGINT,
    title VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_conversations_user_id ON conversations(user_id);

-- Conversation messages
CREATE TABLE conversation_messages (
    id BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_messages_conversation_id ON conversation_messages(conversation_id);

-- LLM configurations
CREATE TABLE llm_configurations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    name VARCHAR(100) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    model_name VARCHAR(100) NOT NULL,
    base_url VARCHAR(500),
    api_key_encrypted VARCHAR(1000),
    embedding_provider VARCHAR(50),
    embedding_model VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_llm_config_user_id ON llm_configurations(user_id);

-- Default system configuration: Groq (free tier)
INSERT INTO llm_configurations (user_id, name, provider, model_name, base_url, embedding_provider, embedding_model, active)
VALUES (NULL, 'Groq Free', 'GROQ', 'llama-3.3-70b-versatile', 'https://api.groq.com/openai/v1', 'IN_PROCESS', 'all-minilm-l6-v2', TRUE);

-- Hibernate/Panache sequences (SEQUENCE strategy, allocationSize=50)
CREATE SEQUENCE IF NOT EXISTS document_chunks_SEQ START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS conversations_SEQ START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS conversation_messages_SEQ START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS llm_configurations_SEQ START WITH 100 INCREMENT BY 50;
