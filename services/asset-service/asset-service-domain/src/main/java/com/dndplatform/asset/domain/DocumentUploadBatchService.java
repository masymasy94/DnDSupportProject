package com.dndplatform.asset.domain;

import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.model.DocumentContent;

import java.util.List;

public interface DocumentUploadBatchService {

    List<Document> uploadMultiple(List<DocumentContent> documents, String uploadedBy);
}
