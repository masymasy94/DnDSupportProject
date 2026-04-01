package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.IngestionStatusService;
import com.dndplatform.documentqa.domain.model.DocumentIngestion;
import com.dndplatform.documentqa.domain.repository.IngestionTrackingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IngestionStatusServiceImpl implements IngestionStatusService {

    private final IngestionTrackingRepository repository;

    @Inject
    public IngestionStatusServiceImpl(IngestionTrackingRepository repository) {
        this.repository = repository;
    }

    @Override
    public DocumentIngestion getStatus(String documentId) {
        return repository.findByDocumentId(documentId)
                .orElseThrow(() -> new NotFoundException("Ingestion not found for document: " + documentId));
    }
}
