package com.dndplatform.asset.view.model.vm;

public record DocumentUploadedEvent(
        String documentId,
        String fileName,
        String contentType,
        String uploadedBy
) {
}
