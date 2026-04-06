package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.DocumentUploadBatchService;
import com.dndplatform.asset.domain.event.DocumentUploadedEventPublisher;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.asset.domain.repository.DocumentMetadataCreateRepository;
import com.dndplatform.asset.domain.repository.DocumentUploadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DocumentUploadBatchServiceImpl implements DocumentUploadBatchService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final DocumentUploadRepository repository;
    private final DocumentMetadataCreateRepository metadataRepository;
    private final DocumentUploadedEventPublisher eventPublisher;

    @Inject
    public DocumentUploadBatchServiceImpl(DocumentUploadRepository repository,
                                          DocumentMetadataCreateRepository metadataRepository,
                                          DocumentUploadedEventPublisher eventPublisher) {
        this.repository = repository;
        this.metadataRepository = metadataRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<Document> uploadMultiple(List<DocumentContent> documents, String uploadedBy) {
        List<Document> uploadedDocuments = new ArrayList<>();
        for (DocumentContent doc : documents) {
            Document uploaded = repository.upload(
                    doc.fileName(),
                    doc.contentType(),
                    doc.inputStream(),
                    doc.size(),
                    uploadedBy
            );
            metadataRepository.save(uploaded);
            eventPublisher.publish(uploaded);
            log.info(() -> "Uploaded and published event for document: %s".formatted(uploaded.id()));
            uploadedDocuments.add(uploaded);
        }
        return uploadedDocuments;
    }
}
