package com.dndplatform.asset.domain;

import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.model.DocumentContent;

import java.io.InputStream;
import java.util.List;

public interface DocumentUploadService {

    Document upload(String fileName, String contentType, InputStream inputStream, long size, String uploadedBy);

    List<Document> uploadMultiple(List<DocumentContent> documents, String uploadedBy);
}
