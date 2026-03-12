package com.dndplatform.asset.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_metadata")
public class DocumentMetadataEntity extends PanacheEntityBase {

    @Id
    @Column(name = "id", length = 255)
    public String id;

    @Column(name = "file_name", length = 500, nullable = false)
    public String fileName;

    @Column(name = "content_type", length = 100)
    public String contentType;

    @Column(name = "size")
    public Long size;

    @Column(name = "uploaded_by", length = 255)
    public String uploadedBy;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    public LocalDateTime uploadedAt;

    @Column(name = "rag_status", length = 20, nullable = false)
    public String ragStatus;

    @Column(name = "rag_error_message", columnDefinition = "TEXT")
    public String ragErrorMessage;

    @Column(name = "rag_processed_at")
    public LocalDateTime ragProcessedAt;

    public DocumentMetadataEntity() {
        this.uploadedAt = LocalDateTime.now();
        this.ragStatus = "PENDING";
    }
}
