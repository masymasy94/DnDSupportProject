CREATE TABLE IF NOT EXISTS document_metadata (
    id VARCHAR(255) PRIMARY KEY,
    file_name VARCHAR(500) NOT NULL,
    content_type VARCHAR(100),
    size BIGINT,
    uploaded_by VARCHAR(255),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rag_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    rag_error_message TEXT,
    rag_processed_at TIMESTAMP
);

CREATE INDEX idx_doc_metadata_rag_status ON document_metadata(rag_status);
CREATE INDEX idx_doc_metadata_uploaded_by ON document_metadata(uploaded_by);
