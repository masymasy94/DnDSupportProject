package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterSheetEntity;
import com.dndplatform.character.domain.repository.CharacterSheetSaveRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class CharacterSheetSaveRepositoryJpa implements CharacterSheetSaveRepository, PanacheRepository<CharacterSheetEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void saveSheet(Long characterId, String fileName, String contentType, byte[] pdfData) {
        log.info(() -> "Saving character sheet for character ID: %d".formatted(characterId));

        // Delete existing sheet if any (upsert behavior)
        delete("characterId", characterId);

        CharacterSheetEntity entity = new CharacterSheetEntity();
        entity.characterId = characterId;
        entity.fileName = fileName;
        entity.contentType = contentType;
        entity.fileSize = (long) pdfData.length;
        entity.pdfData = pdfData;
        persist(entity);
    }
}
