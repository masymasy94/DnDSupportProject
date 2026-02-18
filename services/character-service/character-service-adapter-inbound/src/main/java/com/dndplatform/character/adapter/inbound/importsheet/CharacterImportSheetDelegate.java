package com.dndplatform.character.adapter.inbound.importsheet;

import com.dndplatform.character.adapter.inbound.create.mapper.CharacterViewModelMapper;
import com.dndplatform.character.adapter.inbound.importsheet.mapper.PdfFieldToCharacterMapper;
import com.dndplatform.character.adapter.inbound.importsheet.parser.PdfCharacterSheetParser;
import com.dndplatform.character.domain.CharacterCreateService;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.repository.CharacterSheetSaveRepository;
import com.dndplatform.character.view.model.CharacterImportSheetResource;
import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CharacterImportSheetDelegate implements CharacterImportSheetResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final PdfCharacterSheetParser parser;
    private final PdfFieldToCharacterMapper fieldMapper;
    private final CharacterCreateService createService;
    private final CharacterSheetSaveRepository sheetRepository;
    private final CharacterViewModelMapper viewModelMapper;

    @Inject
    public CharacterImportSheetDelegate(PdfCharacterSheetParser parser,
                                        PdfFieldToCharacterMapper fieldMapper,
                                        CharacterCreateService createService,
                                        CharacterSheetSaveRepository sheetRepository,
                                        CharacterViewModelMapper viewModelMapper) {
        this.parser = parser;
        this.fieldMapper = fieldMapper;
        this.createService = createService;
        this.sheetRepository = sheetRepository;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public CharacterViewModel importSheet(InputStream pdfData, String fileName) {
        throw new UnsupportedOperationException("Use importSheetWithUserId instead");
    }

    public CharacterViewModel importSheetWithUserId(byte[] pdfBytes, String fileName, String contentType, Long userId) {
        log.info(() -> "Importing character sheet '%s' for user %d".formatted(fileName, userId));

        // 1. Extract form fields from PDF
        Map<String, String> fields = parser.extractFormFields(pdfBytes);
        log.info(() -> "Extracted %d fields from PDF".formatted(fields.size()));

        // 2. Map fields to domain model
        CharacterCreate characterCreate = fieldMapper.mapToCharacterCreate(fields, userId);
        log.info(() -> "Mapped PDF to character: %s".formatted(characterCreate.name()));

        // 3. Create character via existing service (reuses validation + calculation)
        Character character = createService.create(characterCreate);
        log.info(() -> "Character created with ID: %d".formatted(character.id()));

        // 4. Save the original PDF
        sheetRepository.saveSheet(character.id(), fileName, contentType, pdfBytes);
        log.info(() -> "PDF sheet saved for character ID: %d".formatted(character.id()));

        // 5. Return view model
        return viewModelMapper.apply(character);
    }
}
