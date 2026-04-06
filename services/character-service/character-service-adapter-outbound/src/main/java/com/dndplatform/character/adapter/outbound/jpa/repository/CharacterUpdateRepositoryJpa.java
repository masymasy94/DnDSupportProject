package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.*;
import com.dndplatform.character.adapter.outbound.jpa.mapper.CharacterEntityMapper;
import com.dndplatform.character.domain.CharacterModifierCalculator;
import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.repository.CharacterUpdateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterUpdateRepositoryJpa implements CharacterUpdateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CharacterEntityMapper mapper;
    private final CharacterModifierCalculator modifierCalculator;
    private final CharacterPanacheRepository panacheRepository;
    private final CharacterRelationsBuilder relationsBuilder;

    @Inject
    public CharacterUpdateRepositoryJpa(CharacterEntityMapper mapper,
                                        CharacterModifierCalculator modifierCalculator,
                                        CharacterPanacheRepository panacheRepository,
                                        CharacterRelationsBuilder relationsBuilder) {
        this.mapper = mapper;
        this.modifierCalculator = modifierCalculator;
        this.panacheRepository = panacheRepository;
        this.relationsBuilder = relationsBuilder;
    }

    @Override
    @Transactional
    public Character update(Long characterId, CharacterCreate input, ValidatedCompendiumData compendiumData,
                            int proficiencyBonus, int hitPointsMax,
                            String spellcastingAbility, Integer spellSaveDc, Integer spellAttackBonus) {

        log.info(() -> "Updating character %d: %s".formatted(characterId, input.name()));

        CharacterEntity entity = panacheRepository.findByIdAndUserId(characterId, input.userId())
                .orElseThrow(() -> new NotFoundException("Character not found with id %d for user id %d".formatted(characterId, input.userId())));

        // Update scalar fields
        updateScalarFields(entity, input, compendiumData, proficiencyBonus, hitPointsMax,
                spellcastingAbility, spellSaveDc, spellAttackBonus);

        // Clear and re-add all collections (orphanRemoval handles deletion)
        entity.languages.clear();
        entity.skills.clear();
        entity.savingThrows.clear();
        entity.proficiencies.clear();
        entity.equipment.clear();
        entity.spells.clear();
        entity.spellSlots.clear();

        // Flush the clears so orphanRemoval deletes happen before re-adding
        panacheRepository.flush();

        // Re-add collections
        relationsBuilder.addLanguages(entity, input.languages());
        relationsBuilder.addSkills(entity, input.skillProficiencies());
        relationsBuilder.addSavingThrows(entity, input.savingThrowProficiencies());
        relationsBuilder.addProficiencies(entity, input.proficiencies());
        relationsBuilder.addEquipment(entity, input.equipment());
        relationsBuilder.addSpells(entity, input.spells(), spellcastingAbility);
        relationsBuilder.addSpellSlots(entity, input.characterClass(), input.level());

        // Flush to ensure all related entities are persisted
        panacheRepository.flush();

        log.info(() -> "Character updated with ID: %d".formatted(entity.id));
        return mapper.toCharacter(entity);
    }

    private void updateScalarFields(CharacterEntity entity, CharacterCreate input,
                                     ValidatedCompendiumData compendiumData,
                                     int proficiencyBonus, int hitPointsMax,
                                     String spellcastingAbility, Integer spellSaveDc,
                                     Integer spellAttackBonus) {
        entity.name = input.name();
        entity.speciesName = compendiumData.speciesName();
        entity.className = compendiumData.className();
        entity.backgroundName = compendiumData.backgroundName();
        entity.alignmentName = compendiumData.alignmentName();
        entity.subraceName = input.subrace();
        entity.subclassName = input.subclass();
        entity.level = input.level();

        // Ability scores
        entity.strength = input.abilityScores().strength();
        entity.dexterity = input.abilityScores().dexterity();
        entity.constitution = input.abilityScores().constitution();
        entity.intelligence = input.abilityScores().intelligence();
        entity.wisdom = input.abilityScores().wisdom();
        entity.charisma = input.abilityScores().charisma();

        // Combat stats
        entity.hitPointsMax = hitPointsMax;
        entity.hitPointsCurrent = Math.min(entity.hitPointsCurrent, hitPointsMax);
        entity.armorClass = 10 + modifierCalculator.calculateModifier(input.abilityScores().dexterity());
        entity.speed = compendiumData.baseSpeed();
        entity.hitDiceTotal = input.level();
        entity.hitDiceType = compendiumData.hitDie();

        // Derived stats
        entity.proficiencyBonus = proficiencyBonus;

        // Spellcasting
        entity.spellcastingAbility = spellcastingAbility;
        entity.spellSaveDc = spellSaveDc;
        entity.spellAttackBonus = spellAttackBonus;

        // Physical characteristics
        if (input.physicalCharacteristics() != null) {
            entity.age = input.physicalCharacteristics().age();
            entity.height = input.physicalCharacteristics().height();
            entity.weight = input.physicalCharacteristics().weight();
            entity.eyes = input.physicalCharacteristics().eyes();
            entity.skin = input.physicalCharacteristics().skin();
            entity.hair = input.physicalCharacteristics().hair();
        } else {
            entity.age = null;
            entity.height = null;
            entity.weight = null;
            entity.eyes = null;
            entity.skin = null;
            entity.hair = null;
        }

        // Personality
        entity.personalityTraits = input.personalityTraits();
        entity.ideals = input.ideals();
        entity.bonds = input.bonds();
        entity.flaws = input.flaws();

        // Audit
        entity.updatedAt = LocalDateTime.now();
    }
}
