package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.DocumentListService;
import com.dndplatform.asset.domain.model.DocumentListItem;
import com.dndplatform.asset.domain.repository.DocumentListRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class DocumentListServiceImpl implements DocumentListService {

    private final DocumentListRepository repository;

    @Inject
    public DocumentListServiceImpl(DocumentListRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DocumentListItem> listAll() {
        return repository.listAll();
    }
}
