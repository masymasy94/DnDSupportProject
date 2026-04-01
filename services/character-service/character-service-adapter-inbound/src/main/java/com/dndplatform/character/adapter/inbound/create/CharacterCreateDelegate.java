package com.dndplatform.character.adapter.inbound.create;

import com.dndplatform.character.adapter.inbound.create.mapper.CharacterCreateMapper;
import com.dndplatform.character.adapter.inbound.create.mapper.CharacterViewModelMapper;
import com.dndplatform.character.domain.CharacterCreateService;
import com.dndplatform.character.domain.CharacterSheetGenerator;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.CharacterCreateBuilder;
import com.dndplatform.character.domain.repository.CharacterSheetSaveRepository;
import com.dndplatform.character.view.model.CharacterCreateResource;
import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.character.view.model.vm.CreateCharacterRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CharacterCreateDelegate implements CharacterCreateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterCreateService service;
    private final CharacterCreateMapper createMapper;
    private final CharacterViewModelMapper viewModelMapper;
    private final CharacterSheetGenerator sheetGenerator;
    private final CharacterSheetSaveRepository sheetRepository;

    @Inject
    public CharacterCreateDelegate(CharacterCreateService service,
                                   CharacterCreateMapper createMapper,
                                   CharacterViewModelMapper viewModelMapper,
                                   CharacterSheetGenerator sheetGenerator,
                                   CharacterSheetSaveRepository sheetRepository) {
        this.service = service;
        this.createMapper = createMapper;
        this.viewModelMapper = viewModelMapper;
        this.sheetGenerator = sheetGenerator;
        this.sheetRepository = sheetRepository;
    }

    @Override
    public CharacterViewModel create(CreateCharacterRequest request) {
        Long userId = request.userId();
        log.info(() -> "Creating character for user %d: %s".formatted(userId, request.name()));

        // Map request to domain model and add userId
        CharacterCreate baseCreate = createMapper.apply(request);
        CharacterCreate domainCreate = CharacterCreateBuilder.builder()
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

        // Create character
        Character character = service.create(domainCreate);

        // Generate and save PDF character sheet (non-blocking: failure is logged but doesn't fail creation)
        generateAndSaveSheet(character);

        // Map to view model
        return viewModelMapper.apply(character);
    }

    private void generateAndSaveSheet(Character character) {
        try {
            byte[] pdfBytes = sheetGenerator.generate(character);
            String fileName = character.name() + ".pdf";
            sheetRepository.saveSheet(character.id(), fileName, "application/pdf", pdfBytes);
            log.info(() -> "PDF character sheet generated and saved for character ID: %d".formatted(character.id()));
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to generate PDF character sheet for character ID: %d"
                    .formatted(character.id()), e);
        }
    }
}
