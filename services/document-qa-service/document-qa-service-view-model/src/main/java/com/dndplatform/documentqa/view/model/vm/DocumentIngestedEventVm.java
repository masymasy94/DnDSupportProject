package com.dndplatform.documentqa.view.model.vm;

public record DocumentIngestedEventVm(
        String documentId,
        String status,
        String errorMessage,
        Integer chunkCount
) {
}
