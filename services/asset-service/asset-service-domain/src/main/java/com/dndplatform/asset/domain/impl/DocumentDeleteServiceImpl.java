package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.DocumentDeleteService;
import com.dndplatform.asset.domain.repository.DocumentDeleteRepository;
import com.dndplatform.asset.domain.repository.DocumentMetadataDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DocumentDeleteServiceImpl implements DocumentDeleteService {

    private final DocumentDeleteRepository storageRepository;
    private final DocumentMetadataDeleteRepository metadataRepository;

    @Inject
    public DocumentDeleteServiceImpl(DocumentDeleteRepository storageRepository,
                                     DocumentMetadataDeleteRepository metadataRepository) {
        this.storageRepository = storageRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public void delete(String documentId) {
        storageRepository.delete(documentId);
        metadataRepository.delete(documentId);
    }
}
