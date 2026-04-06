package com.dndplatform.asset.adapter.outbound.jpa.repository;

import com.dndplatform.asset.adapter.outbound.jpa.entity.DocumentMetadataEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DocumentMetadataPanacheRepository implements PanacheRepositoryBase<DocumentMetadataEntity, String> {
}
