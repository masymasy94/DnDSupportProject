package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.*;
import com.dndplatform.character.adapter.outbound.jpa.mapper.CharacterEntityMapper;
import com.dndplatform.character.domain.CharacterModifierCalculator;
import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.repository.CharacterCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterCreateRepositoryJpa implements CharacterCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CharacterEntityMapper mapper;
    private final CharacterModifierCalculator modifierCalculator;
    private final CharacterPanacheRepository panacheRepository;
    private final CharacterRelationsBuilder relationsBuilder;

    @Inject
    public CharacterCreateRepositoryJpa(CharacterEntityMapper mapper,
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
    public Character save(CharacterCreate input, ValidatedCompendiumData compendiumData,
                          int proficiencyBonus, int hitPointsMax,
                          String spellcastingAbility, Integer spellSaveDc, Integer spellAttackBonus) {

        log.info(() -> "Saving character: %s".formatted(input.name()));

        CharacterEntity entity = createCharacterEntity(input, compendiumData, proficiencyBonus,
                hitPointsMax, spellcastingAbility, spellSaveDc, spellAttackBonus);

        // Persist the main entity first
        panacheRepository.persist(entity);

        // Add related entities
        relationsBuilder.addLanguages(entity, input.languages());
        relationsBuilder.addSkills(entity, input.skillProficiencies());
        relationsBuilder.addSavingThrows(entity, input.savingThrowProficiencies());
        relationsBuilder.addProficiencies(entity, input.proficiencies());
        relationsBuilder.addEquipment(entity, input.equipment());
        relationsBuilder.addSpells(entity, input.spells(), spellcastingAbility);
        relationsBuilder.addSpellSlots(entity, input.characterClass(), input.level());

        // Flush to ensure all related entities are persisted via cascade
        panacheRepository.flush();

        log.info(() -> "Character saved with ID: %d".formatted(entity.id));
        return mapper.toCharacter(entity);
    }

    private CharacterEntity createCharacterEntity(CharacterCreate input, ValidatedCompendiumData compendiumData,
                                                   int proficiencyBonus, int hitPointsMax,
                                                   String spellcastingAbility, Integer spellSaveDc,
                                                   Integer spellAttackBonus) {
        CharacterEntity entity = new CharacterEntity();
        entity.userId = input.userId();
        entity.name = input.name();
        entity.speciesName = compendiumData.speciesName();
        entity.className = compendiumData.className();
        entity.backgroundName = compendiumData.backgroundName();
        entity.alignmentName = compendiumData.alignmentName();
        entity.subraceName = input.subrace();
        entity.subclassName = input.subclass();
        entity.level = input.level();
        entity.experiencePoints = 0;

        // Ability scores
        entity.strength = input.abilityScores().strength();
        entity.dexterity = input.abilityScores().dexterity();
        entity.constitution = input.abilityScores().constitution();
        entity.intelligence = input.abilityScores().intelligence();
        entity.wisdom = input.abilityScores().wisdom();
        entity.charisma = input.abilityScores().charisma();

        // Combat stats
        entity.hitPointsCurrent = hitPointsMax;
        entity.hitPointsMax = hitPointsMax;
        entity.hitPointsTemp = 0;
        entity.armorClass = 10 + modifierCalculator.calculateModifier(input.abilityScores().dexterity());
        entity.speed = compendiumData.baseSpeed();
        entity.hitDiceTotal = input.level();
        entity.hitDiceType = compendiumData.hitDie();
        entity.hitDiceUsed = 0;

        // Derived stats
        entity.proficiencyBonus = proficiencyBonus;
        entity.inspiration = false;

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
        }

        // Personality
        entity.personalityTraits = input.personalityTraits();
        entity.ideals = input.ideals();
        entity.bonds = input.bonds();
        entity.flaws = input.flaws();

        // Audit
        entity.createdAt = LocalDateTime.now();

        return entity;
    }
}
