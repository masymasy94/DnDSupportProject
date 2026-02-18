package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterSheetEntity;
import com.dndplatform.character.domain.model.CharacterSheetData;
import com.dndplatform.character.domain.repository.CharacterSheetFindRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CharacterSheetFindRepositoryJpa implements CharacterSheetFindRepository {

    @Override
    public Optional<CharacterSheetData> findByCharacterId(Long characterId) {
        return CharacterSheetEntity.<CharacterSheetEntity>find("characterId", characterId)
                .firstResultOptional()
                .map(entity -> new CharacterSheetData(
                        entity.fileName,
                        entity.contentType,
                        entity.fileSize,
                        entity.pdfData
                ));
    }
}
