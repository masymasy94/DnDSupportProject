package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.domain.model.CharacterSheetData;
import com.dndplatform.character.domain.repository.CharacterSheetFindRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CharacterSheetFindRepositoryJpa implements CharacterSheetFindRepository {

    private final CharacterSheetPanacheRepository panacheRepository;

    @Inject
    public CharacterSheetFindRepositoryJpa(CharacterSheetPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<CharacterSheetData> findByCharacterId(Long characterId) {
        return panacheRepository.findByCharacterId(characterId)
                .map(entity -> new CharacterSheetData(
                        entity.fileName,
                        entity.contentType,
                        entity.fileSize,
                        entity.pdfData
                ));
    }
}
