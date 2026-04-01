package com.dndplatform.asset.domain.event;

import com.dndplatform.asset.domain.model.Document;

public interface DocumentUploadedEventPublisher {

    void publish(Document document);
}
