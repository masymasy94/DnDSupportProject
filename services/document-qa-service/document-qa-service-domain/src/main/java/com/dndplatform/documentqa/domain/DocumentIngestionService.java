package com.dndplatform.documentqa.domain;

import com.dndplatform.documentqa.domain.model.DocumentIngestionEvent;

public interface DocumentIngestionService {

    void ingest(DocumentIngestionEvent event);
}
