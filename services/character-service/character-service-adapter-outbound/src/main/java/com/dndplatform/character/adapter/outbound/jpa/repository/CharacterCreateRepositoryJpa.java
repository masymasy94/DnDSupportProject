package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.*;
import com.dndplatform.character.adapter.outbound.jpa.mapper.CharacterEntityMapper;
import com.dndplatform.character.domain.CharacterCalculatorService;
import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.repository.CharacterCreateRepository;
import com.dndplatform.common.exception.NotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterCreateRepositoryJpa implements CharacterCreateRepository, PanacheRepository<CharacterEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CharacterEntityMapper mapper;
    private final CharacterCalculatorService calculatorService;

    @Inject
    public CharacterCreateRepositoryJpa(CharacterEntityMapper mapper,
                                        CharacterCalculatorService calculatorService) {
        this.mapper = mapper;
        this.calculatorService = calculatorService;
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
        persist(entity);

        // Add related entities
        addLanguages(entity, input.languages());
        addSkills(entity, input.skillProficiencies());
        addSavingThrows(entity, input.savingThrowProficiencies());
        addProficiencies(entity, input.proficiencies());
        addEquipment(entity, input.equipment());
        addSpells(entity, input.spells(), spellcastingAbility);
        addSpellSlots(entity, input.characterClass(), input.level());

        // Flush to ensure all related entities are persisted
        getEntityManager().flush();

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
        entity.armorClass = 10 + calculatorService.calculateModifier(input.abilityScores().dexterity());
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

    private void addLanguages(CharacterEntity character, List<String> languageNames) {
        if (languageNames == null || languageNames.isEmpty()) {
            return;
        }

        for (String languageName : languageNames) {
            LanguageEntity language = LanguageEntity.find("name", languageName).firstResult();
            if (language == null) {
                log.warning(() -> "Language not found: %s".formatted(languageName));
                continue;
            }

            CharacterLanguageEntity charLang = new CharacterLanguageEntity();
            charLang.character = character;
            charLang.language = language;
            charLang.source = "character_creation";
            charLang.persist();
            character.languages.add(charLang);
        }
    }

    private void addSkills(CharacterEntity character, List<String> skillNames) {
        if (skillNames == null || skillNames.isEmpty()) {
            return;
        }

        for (String skillName : skillNames) {
            SkillEntity skill = SkillEntity.find("name", skillName).firstResult();
            if (skill == null) {
                log.warning(() -> "Skill not found: %s".formatted(skillName));
                continue;
            }

            CharacterSkillEntity charSkill = new CharacterSkillEntity();
            charSkill.character = character;
            charSkill.skill = skill;
            charSkill.proficient = true;
            charSkill.expertise = false;
            charSkill.persist();
            character.skills.add(charSkill);
        }
    }

    private void addSavingThrows(CharacterEntity character, List<String> abilityCodes) {
        if (abilityCodes == null || abilityCodes.isEmpty()) {
            return;
        }

        for (String abilityCode : abilityCodes) {
            AbilityEntity ability = AbilityEntity.find("code", abilityCode).firstResult();
            if (ability == null) {
                log.warning(() -> "Ability not found: %s".formatted(abilityCode));
                continue;
            }

            CharacterSavingThrowEntity charSave = new CharacterSavingThrowEntity();
            charSave.character = character;
            charSave.ability = ability;
            charSave.proficient = true;
            charSave.persist();
            character.savingThrows.add(charSave);
        }
    }

    private void addProficiencies(CharacterEntity character, List<Proficiency> proficiencies) {
        if (proficiencies == null || proficiencies.isEmpty()) {
            return;
        }

        for (Proficiency prof : proficiencies) {
            ProficiencyTypeEntity profType = ProficiencyTypeEntity.find("name", prof.type()).firstResult();
            if (profType == null) {
                log.warning(() -> "Proficiency type not found: %s".formatted(prof.type()));
                continue;
            }

            CharacterProficiencyEntity charProf = new CharacterProficiencyEntity();
            charProf.character = character;
            charProf.proficiencyType = profType;
            charProf.name = prof.name();
            charProf.persist();
            character.proficiencies.add(charProf);
        }
    }

    private void addEquipment(CharacterEntity character, List<Equipment> equipmentList) {
        if (equipmentList == null || equipmentList.isEmpty()) {
            return;
        }

        for (Equipment equip : equipmentList) {
            CharacterEquipmentEntity charEquip = new CharacterEquipmentEntity();
            charEquip.character = character;
            charEquip.name = equip.name();
            charEquip.quantity = equip.quantity() != null ? equip.quantity() : 1;
            charEquip.equipped = equip.equipped() != null ? equip.equipped() : false;
            charEquip.persist();
            character.equipment.add(charEquip);
        }
    }

    private void addSpells(CharacterEntity character, List<String> spellNames, String spellcastingAbility) {
        if (spellNames == null || spellNames.isEmpty() || spellcastingAbility == null) {
            return;
        }

        for (String spellName : spellNames) {
            SpellEntity spell = SpellEntity.find("name", spellName).firstResult();
            if (spell == null) {
                log.warning(() -> "Spell not found: %s".formatted(spellName));
                continue;
            }

            CharacterSpellEntity charSpell = new CharacterSpellEntity();
            charSpell.character = character;
            charSpell.spell = spell;
            charSpell.prepared = spell.level == 0; // Cantrips are always prepared
            charSpell.source = "Class";
            charSpell.persist();
            character.spells.add(charSpell);
        }
    }

    private void addSpellSlots(CharacterEntity character, String className, int level) {
        List<SpellSlotAllocation> slots = calculatorService.calculateSpellSlots(className, level);

        for (SpellSlotAllocation slot : slots) {
            CharacterSpellSlotEntity charSlot = new CharacterSpellSlotEntity();
            charSlot.character = character;
            charSlot.spellLevel = slot.spellLevel().shortValue();
            charSlot.slotsTotal = slot.slotsTotal().shortValue();
            charSlot.slotsUsed = 0;
            charSlot.persist();
            character.spellSlots.add(charSlot);
        }
    }
}
