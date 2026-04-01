package com.dndplatform.documentqa.view.model.vm;

public record DocumentUploadedEventVm(
        String documentId,
        String fileName,
        String contentType,
        String uploadedBy
) {
}
