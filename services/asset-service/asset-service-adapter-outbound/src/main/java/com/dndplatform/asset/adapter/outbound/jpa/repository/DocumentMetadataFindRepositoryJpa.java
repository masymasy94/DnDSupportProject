package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.domain.repository.DocumentMetadataFindRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class DocumentMetadataFindRepositoryJpa implements DocumentMetadataFindRepository {

    private final DocumentMetadataPanacheRepository panacheRepository;

    @Inject
    public DocumentMetadataFindRepositoryJpa(DocumentMetadataPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<RagStatus> findRagStatus(String documentId) {
        DocumentMetadataEntity entity = panacheRepository.findById(documentId);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(RagStatus.valueOf(entity.ragStatus));
    }
}
