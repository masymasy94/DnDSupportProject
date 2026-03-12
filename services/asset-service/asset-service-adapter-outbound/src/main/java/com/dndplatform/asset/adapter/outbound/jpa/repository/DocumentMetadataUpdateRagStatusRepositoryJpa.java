package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.domain.repository.DocumentMetadataUpdateRagStatusRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class DocumentMetadataUpdateRagStatusRepositoryJpa implements DocumentMetadataUpdateRagStatusRepository {

    @Override
    @Transactional
    public void updateRagStatus(String documentId, RagStatus status, String errorMessage) {
        DocumentMetadataEntity entity = DocumentMetadataEntity.findById(documentId);
        if (entity != null) {
            entity.ragStatus = status.name();
            entity.ragErrorMessage = errorMessage;
            if (status == RagStatus.COMPLETED || status == RagStatus.FAILED) {
                entity.ragProcessedAt = LocalDateTime.now();
            }
        }
    }
}
