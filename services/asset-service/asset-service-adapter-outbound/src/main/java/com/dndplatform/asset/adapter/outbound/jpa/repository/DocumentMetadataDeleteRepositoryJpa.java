package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.repository.DocumentMetadataDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DocumentMetadataDeleteRepositoryJpa implements DocumentMetadataDeleteRepository {

    private final DocumentMetadataPanacheRepository panacheRepository;

    @Inject
    public DocumentMetadataDeleteRepositoryJpa(DocumentMetadataPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void delete(String documentId) {
        DocumentMetadataEntity entity = panacheRepository.findById(documentId);
        if (entity != null) {
            panacheRepository.delete(entity);
        }
    }
}
