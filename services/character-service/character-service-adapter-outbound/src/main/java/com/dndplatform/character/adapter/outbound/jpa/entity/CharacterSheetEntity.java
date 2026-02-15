package com.dndplatform.character.adapter.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "character_sheets")
public class CharacterSheetEntity extends PanacheEntity {

    @Column(name = "character_id", nullable = false, unique = true)
    public Long characterId;

    @Column(name = "file_name", length = 255, nullable = false)
    public String fileName;

    @Column(name = "content_type", length = 100, nullable = false)
    public String contentType;

    @Column(name = "file_size", nullable = false)
    public Long fileSize;

    @Column(name = "pdf_data", columnDefinition = "BYTEA", nullable = false)
    public byte[] pdfData;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    public LocalDateTime uploadedAt;

    public CharacterSheetEntity() {
        this.uploadedAt = LocalDateTime.now();
    }
}
