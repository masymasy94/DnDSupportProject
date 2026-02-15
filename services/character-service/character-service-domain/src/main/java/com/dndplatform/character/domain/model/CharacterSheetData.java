package com.dndplatform.character.domain.model;

public record CharacterSheetData(
        String fileName,
        String contentType,
        long fileSize,
        byte[] pdfData
) {
}
