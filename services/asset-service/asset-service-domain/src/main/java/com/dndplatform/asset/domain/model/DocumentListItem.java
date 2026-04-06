package com.dndplatform.asset.domain.model;

public record DocumentListItem(
        String id,
        String fileName,
        RagStatus ragStatus
) {
    public DocumentListItem(String id, String fileName) {
        this(id, fileName, null);
    }
}
