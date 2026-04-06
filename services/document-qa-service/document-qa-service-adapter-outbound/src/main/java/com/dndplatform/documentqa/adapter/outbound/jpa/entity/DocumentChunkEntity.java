package com.dndplatform.documentqa.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_chunks")
public class DocumentChunkEntity extends PanacheEntity {

    @Column(name = "document_id", length = 255, nullable = false)
    public String documentId;

    @Column(name = "chunk_index", nullable = false)
    public Integer chunkIndex;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    public String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    public DocumentChunkEntity() {
        this.createdAt = LocalDateTime.now();
    }
}
