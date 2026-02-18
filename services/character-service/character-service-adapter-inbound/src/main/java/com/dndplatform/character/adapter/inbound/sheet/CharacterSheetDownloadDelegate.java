package com.dndplatform.character.adapter.inbound.sheet;

import com.dndplatform.character.domain.model.CharacterSheetData;
import com.dndplatform.character.domain.repository.CharacterSheetFindRepository;
import com.dndplatform.character.view.model.CharacterSheetDownloadResource;
import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CharacterSheetDownloadDelegate implements CharacterSheetDownloadResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterSheetFindRepository sheetRepository;

    @Inject
    public CharacterSheetDownloadDelegate(CharacterSheetFindRepository sheetRepository) {
        this.sheetRepository = sheetRepository;
    }

    @Override
    public Response downloadSheet(Long characterId) {
        log.info(() -> "Downloading character sheet for character ID: %d".formatted(characterId));

        CharacterSheetData sheet = sheetRepository.findByCharacterId(characterId)
                .orElseThrow(() -> new NotFoundException("Character sheet not found for character ID: " + characterId));

        return Response.ok(sheet.pdfData())
                .type(sheet.contentType())
                .header("Content-Disposition", "attachment; filename=\"" + sheet.fileName() + "\"")
                .header("Content-Length", sheet.fileSize())
                .build();
    }
}
