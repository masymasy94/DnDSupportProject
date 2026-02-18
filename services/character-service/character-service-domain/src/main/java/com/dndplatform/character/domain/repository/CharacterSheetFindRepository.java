package com.dndplatform.character.domain.repository;

import com.dndplatform.character.domain.model.CharacterSheetData;

import java.util.Optional;

public interface CharacterSheetFindRepository {
    Optional<CharacterSheetData> findByCharacterId(Long characterId);
}
