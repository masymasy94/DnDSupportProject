package com.dndplatform.character.adapter.inbound.importsheet.mapper;

import com.dndplatform.character.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class PdfFieldToCharacterMapper {

    private final Logger log = Logger.getLogger(getClass().getName());

    // WotC 5e character sheet skill checkboxes (Check Box 23 through Check Box 40)
    // Order matches the PDF form layout (alphabetical by skill name)
    private static final String[] SKILL_CHECKBOX_FIELDS = {
            "Check Box 23", "Check Box 24", "Check Box 25", "Check Box 26",
            "Check Box 27", "Check Box 28", "Check Box 29", "Check Box 30",
            "Check Box 31", "Check Box 32", "Check Box 33", "Check Box 34",
            "Check Box 35", "Check Box 36", "Check Box 37", "Check Box 38",
            "Check Box 39", "Check Box 40"
    };

    private static final String[] SKILL_NAMES = {
            "Acrobatics", "Animal Handling", "Arcana", "Athletics",
            "Deception", "History", "Insight", "Intimidation",
            "Investigation", "Medicine", "Nature", "Perception",
            "Performance", "Persuasion", "Religion", "Sleight of Hand",
            "Stealth", "Survival"
    };

    // WotC 5e saving throw checkboxes (Check Box 11 through Check Box 16)
    private static final String[] SAVING_THROW_CHECKBOX_FIELDS = {
            "Check Box 11", "Check Box 12", "Check Box 13",
            "Check Box 14", "Check Box 15", "Check Box 16"
    };

    private static final String[] SAVING_THROW_ABILITY_CODES = {
            "STR", "DEX", "CON", "INT", "WIS", "CHA"
    };

    private static final Pattern CLASS_LEVEL_PATTERN = Pattern.compile(
            "^\\s*(.+?)\\s+(\\d+)\\s*$"
    );

    public CharacterCreate mapToCharacterCreate(Map<String, String> fields, Long userId) {
        log.info(() -> "Mapping %d PDF fields to CharacterCreate for user %d".formatted(fields.size(), userId));

        String classLevel = getField(fields, "ClassLevel");
        String characterClass = "";
        int level = 1;
        if (classLevel != null && !classLevel.isBlank()) {
            Matcher matcher = CLASS_LEVEL_PATTERN.matcher(classLevel);
            if (matcher.matches()) {
                characterClass = matcher.group(1).trim();
                level = parseIntSafe(matcher.group(2), 1);
            } else {
                characterClass = classLevel.trim();
            }
        }

        return CharacterCreateBuilder.builder()
                .withUserId(userId)
                .withName(getField(fields, "CharacterName"))
                .withSpecies(getField(fields, "Race"))
                .withCharacterClass(characterClass)
                .withBackground(getField(fields, "Background"))
                .withAlignment(getField(fields, "Alignment"))
                .withLevel(level)
                .withAbilityScores(mapAbilityScores(fields))
                .withSkillProficiencies(mapSkillProficiencies(fields))
                .withSavingThrowProficiencies(mapSavingThrowProficiencies(fields))
                .withEquipment(mapEquipment(fields))
                .withSpells(mapSpells(fields))
                .withPhysicalCharacteristics(mapPhysicalCharacteristics(fields))
                .withPersonalityTraits(getField(fields, "PersonalityTraits"))
                .withIdeals(getField(fields, "Ideals"))
                .withBonds(getField(fields, "Bonds"))
                .withFlaws(getField(fields, "Flaws"))
                .build();
    }

    private AbilityScores mapAbilityScores(Map<String, String> fields) {
        // WotC 5e PDF uses "STR", "DEX", etc. for scores; some sheets use "STRscore" instead
        return AbilityScoresBuilder.builder()
                .withStrength(parseAbilityScore(fields, "STR", "STRscore"))
                .withDexterity(parseAbilityScore(fields, "DEX", "DEXscore"))
                .withConstitution(parseAbilityScore(fields, "CON", "CONscore"))
                .withIntelligence(parseAbilityScore(fields, "INT", "INTscore"))
                .withWisdom(parseAbilityScore(fields, "WIS", "WISscore"))
                .withCharisma(parseAbilityScore(fields, "CHA", "CHAscore"))
                .build();
    }

    private Integer parseAbilityScore(Map<String, String> fields, String primaryField, String alternateField) {
        // Try primary field first (WotC official: "STR"), then alternate ("STRscore")
        String scoreValue = getField(fields, primaryField);
        if (scoreValue != null && !scoreValue.isBlank()) {
            return parseIntSafe(scoreValue, 10);
        }
        String altValue = getField(fields, alternateField);
        if (altValue != null && !altValue.isBlank()) {
            return parseIntSafe(altValue, 10);
        }
        return 10;
    }

    private List<String> mapSkillProficiencies(Map<String, String> fields) {
        List<String> proficiencies = new ArrayList<>();
        for (int i = 0; i < SKILL_CHECKBOX_FIELDS.length; i++) {
            if (isChecked(fields, SKILL_CHECKBOX_FIELDS[i])) {
                proficiencies.add(SKILL_NAMES[i]);
            }
        }
        return proficiencies;
    }

    private List<String> mapSavingThrowProficiencies(Map<String, String> fields) {
        List<String> proficiencies = new ArrayList<>();
        for (int i = 0; i < SAVING_THROW_CHECKBOX_FIELDS.length; i++) {
            if (isChecked(fields, SAVING_THROW_CHECKBOX_FIELDS[i])) {
                proficiencies.add(SAVING_THROW_ABILITY_CODES[i]);
            }
        }
        return proficiencies;
    }

    private List<Equipment> mapEquipment(Map<String, String> fields) {
        List<Equipment> equipment = new ArrayList<>();
        String equipmentText = getField(fields, "Equipment");
        if (equipmentText == null || equipmentText.isBlank()) {
            return equipment;
        }

        for (String line : equipmentText.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            equipment.add(EquipmentBuilder.builder()
                    .withName(trimmed)
                    .withQuantity(1)
                    .withEquipped(false)
                    .build());
        }
        return equipment;
    }

    private List<String> mapSpells(Map<String, String> fields) {
        List<String> spells = new ArrayList<>();
        // WotC 5e sheet uses fields like "Spells 1014", "Spells 1015", etc.
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getKey().startsWith("Spells") && entry.getValue() != null && !entry.getValue().isBlank()) {
                spells.add(entry.getValue().trim());
            }
        }
        return spells;
    }

    private PhysicalCharacteristics mapPhysicalCharacteristics(Map<String, String> fields) {
        return PhysicalCharacteristicsBuilder.builder()
                .withAge(getField(fields, "Age"))
                .withHeight(getField(fields, "Height"))
                .withWeight(getField(fields, "Weight"))
                .withEyes(getField(fields, "Eyes"))
                .withSkin(getField(fields, "Skin"))
                .withHair(getField(fields, "Hair"))
                .build();
    }

    private String getField(Map<String, String> fields, String fieldName) {
        String value = fields.get(fieldName);
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private boolean isChecked(Map<String, String> fields, String fieldName) {
        String value = fields.get(fieldName);
        // PDF checkbox values are typically "Yes", "On", or "1" when checked
        return value != null && (value.equalsIgnoreCase("Yes")
                || value.equalsIgnoreCase("On")
                || value.equals("1"));
    }

    private int parseIntSafe(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            log.warning(() -> "Could not parse integer from '%s', using default %d".formatted(value, defaultValue));
            return defaultValue;
        }
    }
}
