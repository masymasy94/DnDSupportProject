package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.*;
import com.dndplatform.character.domain.CharacterSpellSlotsCalculator;
import com.dndplatform.character.domain.model.Equipment;
import com.dndplatform.character.domain.model.Proficiency;
import com.dndplatform.character.domain.model.SpellSlotAllocation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterRelationsBuilder {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final LanguagePanacheRepository languageRepository;
    private final SkillPanacheRepository skillRepository;
    private final AbilityPanacheRepository abilityRepository;
    private final ProficiencyTypePanacheRepository proficiencyTypeRepository;
    private final SpellPanacheRepository spellRepository;
    private final CharacterSpellSlotsCalculator spellSlotsCalculator;

    @Inject
    public CharacterRelationsBuilder(LanguagePanacheRepository languageRepository,
                                     SkillPanacheRepository skillRepository,
                                     AbilityPanacheRepository abilityRepository,
                                     ProficiencyTypePanacheRepository proficiencyTypeRepository,
                                     SpellPanacheRepository spellRepository,
                                     CharacterSpellSlotsCalculator spellSlotsCalculator) {
        this.languageRepository = languageRepository;
        this.skillRepository = skillRepository;
        this.abilityRepository = abilityRepository;
        this.proficiencyTypeRepository = proficiencyTypeRepository;
        this.spellRepository = spellRepository;
        this.spellSlotsCalculator = spellSlotsCalculator;
    }

    public void addLanguages(CharacterEntity character, List<String> languageNames) {
        if (languageNames == null || languageNames.isEmpty()) {
            return;
        }

        for (String languageName : languageNames) {
            var language = languageRepository.findByName(languageName).orElse(null);
            if (language == null) {
                log.warning(() -> "Language not found: %s".formatted(languageName));
                continue;
            }

            CharacterLanguageEntity charLang = new CharacterLanguageEntity();
            charLang.character = character;
            charLang.language = language;
            charLang.source = "character_creation";
            character.languages.add(charLang);
        }
    }

    public void addSkills(CharacterEntity character, List<String> skillNames) {
        if (skillNames == null || skillNames.isEmpty()) {
            return;
        }

        for (String skillName : skillNames) {
            var skill = skillRepository.findByName(skillName).orElse(null);
            if (skill == null) {
                log.warning(() -> "Skill not found: %s".formatted(skillName));
                continue;
            }

            CharacterSkillEntity charSkill = new CharacterSkillEntity();
            charSkill.character = character;
            charSkill.skill = skill;
            charSkill.proficient = true;
            charSkill.expertise = false;
            character.skills.add(charSkill);
        }
    }

    public void addSavingThrows(CharacterEntity character, List<String> abilityCodes) {
        if (abilityCodes == null || abilityCodes.isEmpty()) {
            return;
        }

        for (String abilityCode : abilityCodes) {
            var ability = abilityRepository.findByCode(abilityCode).orElse(null);
            if (ability == null) {
                log.warning(() -> "Ability not found: %s".formatted(abilityCode));
                continue;
            }

            CharacterSavingThrowEntity charSave = new CharacterSavingThrowEntity();
            charSave.character = character;
            charSave.ability = ability;
            charSave.proficient = true;
            character.savingThrows.add(charSave);
        }
    }

    public void addProficiencies(CharacterEntity character, List<Proficiency> proficiencies) {
        if (proficiencies == null || proficiencies.isEmpty()) {
            return;
        }

        for (Proficiency prof : proficiencies) {
            var profType = proficiencyTypeRepository.findByName(prof.type()).orElse(null);
            if (profType == null) {
                log.warning(() -> "Proficiency type not found: %s".formatted(prof.type()));
                continue;
            }

            CharacterProficiencyEntity charProf = new CharacterProficiencyEntity();
            charProf.character = character;
            charProf.proficiencyType = profType;
            charProf.name = prof.name();
            character.proficiencies.add(charProf);
        }
    }

    public void addEquipment(CharacterEntity character, List<Equipment> equipmentList) {
        if (equipmentList == null || equipmentList.isEmpty()) {
            return;
        }

        for (Equipment equip : equipmentList) {
            CharacterEquipmentEntity charEquip = new CharacterEquipmentEntity();
            charEquip.character = character;
            charEquip.name = equip.name();
            charEquip.quantity = equip.quantity() != null ? equip.quantity() : 1;
            charEquip.equipped = equip.equipped() != null ? equip.equipped() : false;
            character.equipment.add(charEquip);
        }
    }

    public void addSpells(CharacterEntity character, List<String> spellNames, String spellcastingAbility) {
        if (spellNames == null || spellNames.isEmpty() || spellcastingAbility == null) {
            return;
        }

        for (String spellName : spellNames) {
            var spell = spellRepository.findByName(spellName).orElse(null);
            if (spell == null) {
                log.warning(() -> "Spell not found: %s".formatted(spellName));
                continue;
            }

            CharacterSpellEntity charSpell = new CharacterSpellEntity();
            charSpell.character = character;
            charSpell.spell = spell;
            charSpell.prepared = spell.level == 0; // Cantrips are always prepared
            charSpell.source = "Class";
            character.spells.add(charSpell);
        }
    }

    public void addSpellSlots(CharacterEntity character, String className, int level) {
        List<SpellSlotAllocation> slots = spellSlotsCalculator.calculateSpellSlots(className, level);

        for (SpellSlotAllocation slot : slots) {
            CharacterSpellSlotEntity charSlot = new CharacterSpellSlotEntity();
            charSlot.character = character;
            charSlot.spellLevel = slot.spellLevel().shortValue();
            charSlot.slotsTotal = slot.slotsTotal().shortValue();
            charSlot.slotsUsed = 0;
            character.spellSlots.add(charSlot);
        }
    }
}
