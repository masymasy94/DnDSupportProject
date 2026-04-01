package com.dndplatform.documentqa.domain;

import com.dndplatform.documentqa.domain.model.DocumentIngestion;

public interface IngestionStatusService {

    DocumentIngestion getStatus(String documentId);
}
