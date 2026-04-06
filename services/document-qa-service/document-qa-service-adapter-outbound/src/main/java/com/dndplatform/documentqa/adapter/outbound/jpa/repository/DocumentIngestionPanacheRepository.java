package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.DocumentIngestionEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DocumentIngestionPanacheRepository implements PanacheRepositoryBase<DocumentIngestionEntity, String> {
    public long deleteByDocumentId(String documentId) {
        return delete("documentId", documentId);
    }
}
