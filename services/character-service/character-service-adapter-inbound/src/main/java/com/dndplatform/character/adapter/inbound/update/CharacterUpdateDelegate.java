package com.dndplatform.character.adapter.inbound.update;

import com.dndplatform.character.adapter.inbound.create.mapper.CharacterViewModelMapper;
import com.dndplatform.character.adapter.inbound.update.mapper.CharacterUpdateMapper;
import com.dndplatform.character.domain.CharacterSheetGenerator;
import com.dndplatform.character.domain.CharacterUpdateService;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.CharacterCreateBuilder;
import com.dndplatform.character.domain.repository.CharacterSheetSaveRepository;
import com.dndplatform.character.view.model.CharacterUpdateResource;
import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.character.view.model.vm.UpdateCharacterRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CharacterUpdateDelegate implements CharacterUpdateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterUpdateService service;
    private final CharacterUpdateMapper updateMapper;
    private final CharacterViewModelMapper viewModelMapper;
    private final CharacterSheetGenerator sheetGenerator;
    private final CharacterSheetSaveRepository sheetRepository;

    @Inject
    public CharacterUpdateDelegate(CharacterUpdateService service,
                                   CharacterUpdateMapper updateMapper,
                                   CharacterViewModelMapper viewModelMapper,
                                   CharacterSheetGenerator sheetGenerator,
                                   CharacterSheetSaveRepository sheetRepository) {
        this.service = service;
        this.updateMapper = updateMapper;
        this.viewModelMapper = viewModelMapper;
        this.sheetGenerator = sheetGenerator;
        this.sheetRepository = sheetRepository;
    }

    @Override
    public CharacterViewModel update(Long id, UpdateCharacterRequest request) {
        Long userId = request.userId();
        log.info(() -> "Updating character %d for user %d: %s".formatted(id, userId, request.name()));

        // Map request to domain model and add userId
        CharacterCreate baseCreate = updateMapper.apply(request);
        CharacterCreate domainCreate = getCharacter(userId, baseCreate);

        // Update character
        Character character = service.update(id, domainCreate);

        // Generate and save PDF character sheet (non-blocking: failure is logged but doesn't fail update)
        generateAndSaveSheet(character);

        // Map to view model
        return viewModelMapper.apply(character);
    }

    @Nonnull
    private static CharacterCreate getCharacter(Long userId, CharacterCreate baseCreate) {
        return CharacterCreateBuilder.builder()
                .withUserId(userId)
                .withName(baseCreate.name())
                .withSpecies(baseCreate.species())
                .withSubrace(baseCreate.subrace())
                .withCharacterClass(baseCreate.characterClass())
                .withSubclass(baseCreate.subclass())
                .withBackground(baseCreate.background())
                .withAlignment(baseCreate.alignment())
                .withLevel(baseCreate.level())
                .withAbilityScores(baseCreate.abilityScores())
                .withSkillProficiencies(baseCreate.skillProficiencies())
                .withSavingThrowProficiencies(baseCreate.savingThrowProficiencies())
                .withLanguages(baseCreate.languages())
                .withProficiencies(baseCreate.proficiencies())
                .withEquipment(baseCreate.equipment())
                .withSpells(baseCreate.spells())
                .withPhysicalCharacteristics(baseCreate.physicalCharacteristics())
                .withPersonalityTraits(baseCreate.personalityTraits())
                .withIdeals(baseCreate.ideals())
                .withBonds(baseCreate.bonds())
                .withFlaws(baseCreate.flaws())
                .build();
    }

    private void generateAndSaveSheet(Character character) {
        try {
            byte[] pdfBytes = sheetGenerator.generate(character);
            String fileName = character.name() + ".pdf";
            sheetRepository.saveSheet(character.id(), fileName, "application/pdf", pdfBytes);
            log.info(() -> "PDF character sheet regenerated and saved for character ID: %d".formatted(character.id()));
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to generate PDF character sheet for character ID: %d"
                    .formatted(character.id()), e);
        }
    }
}
