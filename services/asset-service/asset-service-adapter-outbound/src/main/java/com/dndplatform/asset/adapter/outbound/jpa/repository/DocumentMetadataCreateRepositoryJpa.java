package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.repository.DocumentMetadataCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ApplicationScoped
public class DocumentMetadataCreateRepositoryJpa implements DocumentMetadataCreateRepository {

    @Override
    @Transactional
    public void save(Document document) {
        DocumentMetadataEntity entity = new DocumentMetadataEntity();
        entity.id = document.id();
        entity.fileName = document.fileName();
        entity.contentType = document.contentType();
        entity.size = document.size();
        entity.uploadedBy = document.uploadedBy();
        entity.uploadedAt = document.uploadedAt() != null
                ? LocalDateTime.ofInstant(document.uploadedAt(), ZoneOffset.UTC)
                : LocalDateTime.now();
        entity.ragStatus = "PENDING";
        entity.persist();
    }
}
