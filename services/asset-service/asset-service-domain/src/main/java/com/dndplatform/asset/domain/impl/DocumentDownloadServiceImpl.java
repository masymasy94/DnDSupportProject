package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.DocumentDownloadService;
import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.asset.domain.repository.DocumentDownloadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DocumentDownloadServiceImpl implements DocumentDownloadService {

    private final DocumentDownloadRepository repository;

    @Inject
    public DocumentDownloadServiceImpl(DocumentDownloadRepository repository) {
        this.repository = repository;
    }

    @Override
    public DocumentContent download(String documentId) {
        return repository.download(documentId);
    }
}
