package com.dndplatform.asset.domain.impl;

import com.dndplatform.asset.domain.DocumentUploadBatchService;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.asset.domain.repository.DocumentUploadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DocumentUploadBatchServiceImpl implements DocumentUploadBatchService {

    private final DocumentUploadRepository repository;

    @Inject
    public DocumentUploadBatchServiceImpl(DocumentUploadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Document> uploadMultiple(List<DocumentContent> documents, String uploadedBy) {
        List<Document> uploadedDocuments = new ArrayList<>();
        for (DocumentContent doc : documents) {
            Document uploaded = repository.upload(
                    doc.fileName(),
                    doc.contentType(),
                    doc.inputStream(),
                    doc.size(),
                    uploadedBy
            );
            uploadedDocuments.add(uploaded);
        }
        return uploadedDocuments;
    }
}
