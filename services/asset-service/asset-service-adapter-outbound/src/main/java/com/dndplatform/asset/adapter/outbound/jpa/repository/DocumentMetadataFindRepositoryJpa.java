package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.domain.repository.DocumentMetadataFindRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class DocumentMetadataFindRepositoryJpa implements DocumentMetadataFindRepository {

    @Override
    public Optional<RagStatus> findRagStatus(String documentId) {
        DocumentMetadataEntity entity = DocumentMetadataEntity.findById(documentId);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(RagStatus.valueOf(entity.ragStatus));
    }
}
