package com.dndplatform.character.domain.repository;

public interface CharacterSheetSaveRepository {
    void saveSheet(Long characterId, String fileName, String contentType, byte[] pdfData);
}
