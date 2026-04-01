package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.DocumentUploadService;
import com.dndplatform.asset.domain.event.DocumentUploadedEventPublisher;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.repository.DocumentMetadataCreateRepository;
import com.dndplatform.asset.domain.repository.DocumentUploadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.util.logging.Logger;

@ApplicationScoped
public class DocumentUploadServiceImpl implements DocumentUploadService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final DocumentUploadRepository repository;
    private final DocumentMetadataCreateRepository metadataRepository;
    private final DocumentUploadedEventPublisher eventPublisher;

    @Inject
    public DocumentUploadServiceImpl(DocumentUploadRepository repository,
                                     DocumentMetadataCreateRepository metadataRepository,
                                     DocumentUploadedEventPublisher eventPublisher) {
        this.repository = repository;
        this.metadataRepository = metadataRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Document upload(String fileName, String contentType, InputStream inputStream, long size, String uploadedBy) {
        Document document = repository.upload(fileName, contentType, inputStream, size, uploadedBy);
        metadataRepository.save(document);
        eventPublisher.publish(document);
        log.info(() -> "Uploaded and published event for document: %s".formatted(document.id()));
        return document;
    }
}
