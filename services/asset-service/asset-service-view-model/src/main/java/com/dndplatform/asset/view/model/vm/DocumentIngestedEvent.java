package com.dndplatform.asset.view.model.vm;

public record DocumentIngestedEvent(
        String documentId,
        String status,
        String errorMessage,
        Integer chunkCount
) {
}
