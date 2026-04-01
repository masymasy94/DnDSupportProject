package com.dndplatform.asset.domain.repository;

import com.dndplatform.asset.domain.model.RagStatus;

import java.util.Optional;

public interface DocumentMetadataFindRepository {

    Optional<RagStatus> findRagStatus(String documentId);
}
