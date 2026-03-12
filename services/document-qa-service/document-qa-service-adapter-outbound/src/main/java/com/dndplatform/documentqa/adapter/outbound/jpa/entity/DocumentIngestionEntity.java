package com.dndplatform.documentqa.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_ingestions")
public class DocumentIngestionEntity extends PanacheEntityBase {

    @Id
    @Column(name = "document_id", length = 255)
    public String documentId;

    @Column(name = "file_name", length = 500, nullable = false)
    public String fileName;

    @Column(name = "content_type", length = 100)
    public String contentType;

    @Column(name = "status", length = 30, nullable = false)
    public String status;

    @Column(name = "chunk_count")
    public Integer chunkCount;

    @Column(name = "error_message", columnDefinition = "TEXT")
    public String errorMessage;

    @Column(name = "uploaded_by", length = 255)
    public String uploadedBy;

    @Column(name = "started_at")
    public LocalDateTime startedAt;

    @Column(name = "completed_at")
    public LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    public DocumentIngestionEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
        this.chunkCount = 0;
    }
}
