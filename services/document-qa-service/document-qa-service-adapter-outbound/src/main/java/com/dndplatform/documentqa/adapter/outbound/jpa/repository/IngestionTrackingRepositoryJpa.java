package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.DocumentIngestionEntity;
import com.dndplatform.documentqa.domain.model.DocumentIngestion;
import com.dndplatform.documentqa.domain.model.DocumentIngestionBuilder;
import com.dndplatform.documentqa.domain.model.IngestionStatus;
import com.dndplatform.documentqa.domain.repository.IngestionTrackingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class IngestionTrackingRepositoryJpa implements IngestionTrackingRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final DocumentIngestionPanacheRepository panacheRepository;

    @Inject
    public IngestionTrackingRepositoryJpa(DocumentIngestionPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void create(String documentId, String fileName, String contentType, String uploadedBy) {
        log.info(() -> "Creating ingestion tracking for document: %s".formatted(documentId));

        DocumentIngestionEntity entity = new DocumentIngestionEntity();
        entity.documentId = documentId;
        entity.fileName = fileName;
        entity.contentType = contentType;
        entity.uploadedBy = uploadedBy;
        entity.status = IngestionStatus.PENDING.name();
        entity.startedAt = LocalDateTime.now();

        panacheRepository.persist(entity);

        log.info(() -> "Ingestion tracking created for document: %s".formatted(documentId));
    }

    @Override
    @Transactional
    public void updateStatus(String documentId, IngestionStatus status, Integer chunkCount, String errorMessage) {
        log.info(() -> "Updating ingestion status for document %s to %s".formatted(documentId, status));

        DocumentIngestionEntity entity = panacheRepository.findByIdOptional(documentId).orElse(null);
        if (entity == null) {
            log.warning(() -> "Ingestion tracking not found for document: %s".formatted(documentId));
            return;
        }

        entity.status = status.name();
        entity.chunkCount = chunkCount;
        entity.errorMessage = errorMessage;

        if (status == IngestionStatus.COMPLETED || status == IngestionStatus.FAILED) {
            entity.completedAt = LocalDateTime.now();
        }

        log.info(() -> "Ingestion status updated for document: %s".formatted(documentId));
    }

    @Override
    @Transactional
    public void delete(String documentId) {
        long deleted = panacheRepository.deleteByDocumentId(documentId);
        if (deleted > 0) {
            log.info(() -> "Deleted ingestion tracking for document: %s".formatted(documentId));
        }
    }

    @Override
    public Optional<DocumentIngestion> findByDocumentId(String documentId) {
        log.info(() -> "Finding ingestion tracking for document: %s".formatted(documentId));

        return panacheRepository.findByIdOptional(documentId).map(this::toDomain);
    }

    private DocumentIngestion toDomain(DocumentIngestionEntity entity) {
        return DocumentIngestionBuilder.builder()
                .withDocumentId(entity.documentId)
                .withFileName(entity.fileName)
                .withContentType(entity.contentType)
                .withStatus(IngestionStatus.valueOf(entity.status))
                .withChunkCount(entity.chunkCount != null ? entity.chunkCount : 0)
                .withErrorMessage(entity.errorMessage)
                .withUploadedBy(entity.uploadedBy)
                .withStartedAt(entity.startedAt)
                .withCompletedAt(entity.completedAt)
                .withCreatedAt(entity.createdAt)
                .build();
    }
}
