package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import com.dndplatform.asset.domain.repository.DocumentMetadataDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DocumentMetadataDeleteRepositoryJpa implements DocumentMetadataDeleteRepository {

    @Override
    @Transactional
    public void delete(String documentId) {
        DocumentMetadataEntity entity = DocumentMetadataEntity.findById(documentId);
        if (entity != null) {
            entity.delete();
        }
    }
}
