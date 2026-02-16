package com.dndplatform.character.domain.repository;

import com.dndplatform.character.domain.model.CharacterSheetData;

import java.util.Optional;

public interface CharacterSheetRepository {

    void saveSheet(Long characterId, String fileName, String contentType, byte[] pdfData);

    Optional<CharacterSheetData> findByCharacterId(Long characterId);
}
