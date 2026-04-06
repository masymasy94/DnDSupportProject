package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class CharacterFieldMapper {

    private static final String[] ABILITY_CODES = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};

    private static final String[] SKILL_NAMES = {
            "Acrobatics", "Animal Handling", "Arcana", "Athletics",
            "Deception", "History", "Insight", "Intimidation",
            "Investigation", "Medicine", "Nature", "Perception",
            "Performance", "Persuasion", "Religion", "Sleight of Hand",
            "Stealth", "Survival"
    };

    private static final String[] SKILL_CHECKBOX_FIELDS = {
            "Check Box 23", "Check Box 24", "Check Box 25", "Check Box 26",
            "Check Box 27", "Check Box 28", "Check Box 29", "Check Box 30",
            "Check Box 31", "Check Box 32", "Check Box 33", "Check Box 34",
            "Check Box 35", "Check Box 36", "Check Box 37", "Check Box 38",
            "Check Box 39", "Check Box 40"
    };

    private static final String[] SKILL_MODIFIER_FIELDS = {
            "Acrobatics", "Animal", "Arcana", "Athletics",
            "Deception", "History", "Insight", "Intimidation",
            "Investigation", "Medicine", "Nature", "Perception",
            "Performance", "Persuasion", "Religion",
            "SleightofHand", "Stealth", "Survival"
    };

    private static final String[] SAVING_THROW_CHECKBOX_FIELDS = {
            "Check Box 11", "Check Box 12", "Check Box 13",
            "Check Box 14", "Check Box 15", "Check Box 16"
    };

    private static final String[] SAVING_THROW_MODIFIER_FIELDS = {
            "ST Strength", "ST Dexterity", "ST Constitution",
            "ST Intelligence", "ST Wisdom", "ST Charisma"
    };

    private static final String[] SAVING_THROW_ABILITY_CODES = {
            "STR", "DEX", "CON", "INT", "WIS", "CHA"
    };

    private static final String[] ABILITY_MODIFIER_FIELDS = {
            "STRmod", "DEXmod", "CONmod", "INTmod", "WISmod", "CHamod"
    };

    public CharacterFieldMap mapFields(Character character) {
        Map<String, String> textFields = new LinkedHashMap<>();
        Map<String, Boolean> checkboxFields = new LinkedHashMap<>();

        fillCharacterInfo(textFields, character);
        fillAbilityScores(textFields, character);
        fillAbilityModifiers(textFields, character);
        fillSavingThrows(textFields, checkboxFields, character);
        fillSkills(textFields, checkboxFields, character);
        fillCombatStats(textFields, character);
        fillSpellcasting(textFields, character);
        fillPersonality(textFields, character);
        fillPhysicalCharacteristics(textFields, character);
        fillEquipment(textFields, character);
        fillSpellSlots(textFields, character);
        fillProficienciesAndLanguages(textFields, character);

        return new CharacterFieldMap(Map.copyOf(textFields), Map.copyOf(checkboxFields));
    }

    private void fillCharacterInfo(Map<String, String> fields, Character character) {
        putIfNotBlank(fields, "CharacterName", character.name());
        putIfNotBlank(fields, "CharacterName 2", character.name());
        putIfNotBlank(fields, "Race", character.species());
        putIfNotBlank(fields, "Background", character.background());
        putIfNotBlank(fields, "Alignment", character.alignment());
        putIfNotBlank(fields, "XP", str(character.experiencePoints()));

        String classLevel = character.characterClass();
        if (classLevel != null && character.level() != null) {
            classLevel = classLevel + " " + character.level();
        }
        putIfNotBlank(fields, "ClassLevel", classLevel);

        putIfNotBlank(fields, "ProfBonus", formatModifier(character.proficiencyBonus()));
    }

    private void fillAbilityScores(Map<String, String> fields, Character character) {
        AbilityScores scores = character.abilityScores();
        if (scores == null) return;

        putIfNotBlank(fields, "STR", str(scores.strength()));
        putIfNotBlank(fields, "DEX", str(scores.dexterity()));
        putIfNotBlank(fields, "CON", str(scores.constitution()));
        putIfNotBlank(fields, "INT", str(scores.intelligence()));
        putIfNotBlank(fields, "WIS", str(scores.wisdom()));
        putIfNotBlank(fields, "CHA", str(scores.charisma()));
    }

    private void fillAbilityModifiers(Map<String, String> fields, Character character) {
        AbilityScores scores = character.abilityScores();
        if (scores == null) return;

        for (int i = 0; i < ABILITY_CODES.length; i++) {
            Integer modifier = scores.getModifier(ABILITY_CODES[i]);
            putIfNotBlank(fields, ABILITY_MODIFIER_FIELDS[i], formatModifier(modifier));
        }
    }

    private void fillSavingThrows(Map<String, String> fields, Map<String, Boolean> checkboxes, Character character) {
        List<SavingThrowProficiency> savingThrows = character.savingThrows();
        if (savingThrows == null) return;

        Map<String, SavingThrowProficiency> stMap = savingThrows.stream()
                .collect(Collectors.toMap(SavingThrowProficiency::ability, st -> st, (a, b) -> a));

        for (int i = 0; i < SAVING_THROW_ABILITY_CODES.length; i++) {
            SavingThrowProficiency st = stMap.get(SAVING_THROW_ABILITY_CODES[i]);
            if (st != null) {
                if (Boolean.TRUE.equals(st.proficient())) {
                    checkboxes.put(SAVING_THROW_CHECKBOX_FIELDS[i], true);
                }
                putIfNotBlank(fields, SAVING_THROW_MODIFIER_FIELDS[i], formatModifier(st.modifier()));
            }
        }
    }

    private void fillSkills(Map<String, String> fields, Map<String, Boolean> checkboxes, Character character) {
        List<SkillProficiency> skills = character.skills();
        if (skills == null) return;

        Map<String, SkillProficiency> skillMap = skills.stream()
                .collect(Collectors.toMap(SkillProficiency::name, s -> s, (a, b) -> a));

        for (int i = 0; i < SKILL_NAMES.length; i++) {
            SkillProficiency skill = skillMap.get(SKILL_NAMES[i]);
            if (skill != null) {
                if (Boolean.TRUE.equals(skill.proficient())) {
                    checkboxes.put(SKILL_CHECKBOX_FIELDS[i], true);
                }
                putIfNotBlank(fields, SKILL_MODIFIER_FIELDS[i], formatModifier(skill.modifier()));
            }
        }
    }

    private void fillCombatStats(Map<String, String> fields, Character character) {
        putIfNotBlank(fields, "AC", str(character.armorClass()));
        putIfNotBlank(fields, "Speed", str(character.speed()));
        putIfNotBlank(fields, "HPMax", str(character.hitPointsMax()));
        putIfNotBlank(fields, "HPCurrent", str(character.hitPointsCurrent()));
        putIfNotBlank(fields, "HPTemp", str(character.hitPointsTemp()));
        putIfNotBlank(fields, "HDTotal", str(character.hitDiceTotal()));
        putIfNotBlank(fields, "HD", character.hitDiceType());
        putIfNotBlank(fields, "Passive", str(calculatePassivePerception(character)));

        if (character.abilityScores() != null) {
            Integer initiative = character.abilityScores().getModifier("DEX");
            putIfNotBlank(fields, "Initiative", formatModifier(initiative));
        }
    }

    private void fillSpellcasting(Map<String, String> fields, Character character) {
        putIfNotBlank(fields, "SpellcastingAbility 2", character.spellcastingAbility());
        putIfNotBlank(fields, "Spellcasting Class 2", character.characterClass());
        putIfNotBlank(fields, "SpellSaveDC  2", str(character.spellSaveDc()));
        putIfNotBlank(fields, "SpellAtkBonus 2", formatModifier(character.spellAttackBonus()));
    }

    private void fillPersonality(Map<String, String> fields, Character character) {
        putIfNotBlank(fields, "PersonalityTraits", character.personalityTraits());
        putIfNotBlank(fields, "Ideals", character.ideals());
        putIfNotBlank(fields, "Bonds", character.bonds());
        putIfNotBlank(fields, "Flaws", character.flaws());
    }

    private void fillPhysicalCharacteristics(Map<String, String> fields, Character character) {
        PhysicalCharacteristics phys = character.physicalCharacteristics();
        if (phys == null) return;

        putIfNotBlank(fields, "Age", phys.age());
        putIfNotBlank(fields, "Height", phys.height());
        putIfNotBlank(fields, "Weight", phys.weight());
        putIfNotBlank(fields, "Eyes", phys.eyes());
        putIfNotBlank(fields, "Skin", phys.skin());
        putIfNotBlank(fields, "Hair", phys.hair());
    }

    private void fillEquipment(Map<String, String> fields, Character character) {
        List<Equipment> equipment = character.equipment();
        if (equipment == null || equipment.isEmpty()) return;

        String text = equipment.stream()
                .map(e -> e.quantity() != null && e.quantity() > 1
                        ? e.name() + " (x" + e.quantity() + ")"
                        : e.name())
                .collect(Collectors.joining("\n"));
        putIfNotBlank(fields, "Equipment", text);
    }

    private void fillSpellSlots(Map<String, String> fields, Character character) {
        List<SpellSlotAllocation> slots = character.spellSlots();
        if (slots == null) return;

        for (SpellSlotAllocation slot : slots) {
            if (slot.spellLevel() != null && slot.spellLevel() >= 1 && slot.spellLevel() <= 9) {
                int fieldNum = 18 + slot.spellLevel();
                putIfNotBlank(fields, "SlotsTotal " + fieldNum, str(slot.slotsTotal()));
                putIfNotBlank(fields, "SlotsRemaining " + fieldNum, str(slot.slotsTotal()));
            }
        }
    }

    private void fillProficienciesAndLanguages(Map<String, String> fields, Character character) {
        List<String> parts = new ArrayList<>();

        if (character.languages() != null && !character.languages().isEmpty()) {
            parts.add("Languages: " + String.join(", ", character.languages()));
        }

        if (character.proficiencies() != null && !character.proficiencies().isEmpty()) {
            Map<String, List<String>> grouped = character.proficiencies().stream()
                    .collect(Collectors.groupingBy(
                            p -> p.type() != null ? p.type() : "Other",
                            Collectors.mapping(Proficiency::name, Collectors.toList())));

            grouped.forEach((type, names) ->
                    parts.add(type + ": " + String.join(", ", names)));
        }

        if (!parts.isEmpty()) {
            putIfNotBlank(fields, "ProficienciesLang", String.join("\n", parts));
        }
    }

    private Integer calculatePassivePerception(Character character) {
        if (character.skills() == null) return 10;

        return character.skills().stream()
                .filter(s -> "Perception".equals(s.name()))
                .findFirst()
                .map(s -> 10 + (s.modifier() != null ? s.modifier() : 0))
                .orElse(10);
    }

    private void putIfNotBlank(Map<String, String> map, String key, String value) {
        if (value != null && !value.isBlank()) {
            map.put(key, value);
        }
    }

    private String formatModifier(Integer value) {
        if (value == null) return null;
        return value >= 0 ? "+" + value : String.valueOf(value);
    }

    private String str(Integer value) {
        return value != null ? String.valueOf(value) : null;
    }
}
