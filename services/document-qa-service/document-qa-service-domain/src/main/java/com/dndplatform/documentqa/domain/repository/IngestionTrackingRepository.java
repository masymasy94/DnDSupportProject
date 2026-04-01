package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.DocumentIngestion;
import com.dndplatform.documentqa.domain.model.IngestionStatus;

import java.util.Optional;

public interface IngestionTrackingRepository {

    void create(String documentId, String fileName, String contentType, String uploadedBy);

    void updateStatus(String documentId, IngestionStatus status, Integer chunkCount, String errorMessage);

    void delete(String documentId);

    Optional<DocumentIngestion> findByDocumentId(String documentId);
}
