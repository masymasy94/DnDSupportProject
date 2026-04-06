package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterSheetEntity;
import com.dndplatform.character.domain.repository.CharacterSheetSaveRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class CharacterSheetSaveRepositoryJpa implements CharacterSheetSaveRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterSheetPanacheRepository panacheRepository;

    @Inject
    public CharacterSheetSaveRepositoryJpa(CharacterSheetPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void saveSheet(Long characterId, String fileName, String contentType, byte[] pdfData) {
        log.info(() -> "Saving character sheet for character ID: %d".formatted(characterId));

        // Delete existing sheet if any (upsert behavior)
        panacheRepository.deleteByCharacterId(characterId);

        CharacterSheetEntity entity = new CharacterSheetEntity();
        entity.characterId = characterId;
        entity.fileName = fileName;
        entity.contentType = contentType;
        entity.fileSize = (long) pdfData.length;
        entity.pdfData = pdfData;
        panacheRepository.persist(entity);
    }
}
