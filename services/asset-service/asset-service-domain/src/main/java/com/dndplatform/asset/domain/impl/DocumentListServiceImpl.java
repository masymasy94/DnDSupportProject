package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.DocumentListService;
import com.dndplatform.asset.domain.model.DocumentListItem;
import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.domain.repository.DocumentListRepository;
import com.dndplatform.asset.domain.repository.DocumentMetadataFindRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class DocumentListServiceImpl implements DocumentListService {

    private final DocumentListRepository repository;
    private final DocumentMetadataFindRepository metadataRepository;

    @Inject
    public DocumentListServiceImpl(DocumentListRepository repository,
                                   DocumentMetadataFindRepository metadataRepository) {
        this.repository = repository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<DocumentListItem> listAll() {
        return repository.listAll().stream()
                .map(item -> {
                    RagStatus ragStatus = metadataRepository.findRagStatus(item.id()).orElse(null);
                    return new DocumentListItem(item.id(), item.fileName(), ragStatus);
                })
                .toList();
    }
}
