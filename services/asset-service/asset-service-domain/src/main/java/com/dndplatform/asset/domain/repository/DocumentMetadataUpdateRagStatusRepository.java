package com.dndplatform.asset.domain.repository;

import com.dndplatform.asset.domain.model.RagStatus;

public interface DocumentMetadataUpdateRagStatusRepository {

    void updateRagStatus(String documentId, RagStatus status, String errorMessage);
}
