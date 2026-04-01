package com.dndplatform.asset.domain.repository;

import com.dndplatform.asset.domain.model.Document;

public interface DocumentMetadataCreateRepository {

    void save(Document document);
}
