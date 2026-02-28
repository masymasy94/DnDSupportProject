package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.CharacterSheetGenerator;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class PdfCharacterSheetGenerator implements CharacterSheetGenerator {

    private static final Logger log = Logger.getLogger(PdfCharacterSheetGenerator.class.getName());

    private static final String TEMPLATE_PATH = "META-INF/resources/wotc-5e-sheet.pdf";

    private static final String[] ABILITY_CODES = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};

    private static final String[] SKILL_NAMES = {
            "Acrobatics", "Animal Handling", "Arcana", "Athletics",
            "Deception", "History", "Insight", "Intimidation",
            "Investigation", "Medicine", "Nature", "Perception",
            "Performance", "Persuasion", "Religion", "Sleight of Hand",
            "Stealth", "Survival"
    };

    // Checkbox field names for skill proficiencies (Check Box 23 through Check Box 40)
    private static final String[] SKILL_CHECKBOX_FIELDS = {
            "Check Box 23", "Check Box 24", "Check Box 25", "Check Box 26",
            "Check Box 27", "Check Box 28", "Check Box 29", "Check Box 30",
            "Check Box 31", "Check Box 32", "Check Box 33", "Check Box 34",
            "Check Box 35", "Check Box 36", "Check Box 37", "Check Box 38",
            "Check Box 39", "Check Box 40"
    };

    // PDF field names for skill modifier values (some have trailing spaces in the WotC PDF)
    private static final String[] SKILL_MODIFIER_FIELDS = {
            "Acrobatics", "Animal", "Arcana", "Athletics",
            "Deception", "History", "Insight", "Intimidation",
            "Investigation", "Medicine", "Nature", "Perception",
            "Performance", "Persuasion", "Religion",
            "SleightofHand", "Stealth", "Survival"
    };

    // Checkbox field names for saving throw proficiencies (Check Box 11 through Check Box 16)
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

    // Ability score modifier PDF field names (note: CHA is "CHamod" in the WotC PDF)
    private static final String[] ABILITY_MODIFIER_FIELDS = {
            "STRmod", "DEXmod", "CONmod", "INTmod", "WISmod", "CHamod"
    };

    @Override
    public byte[] generate(Character character) {
        byte[] templateBytes = loadTemplate();

        try (PDDocument document = Loader.loadPDF(templateBytes)) {
            // Use getAcroForm(null) to bypass PDFBox 3 fixups that auto-generate
            // appearance streams on form access.
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm(null);
            if (acroForm == null) {
                throw new IOException("PDF template does not contain an AcroForm");
            }

            // Build a lookup map: trimmed field name -> PDField (handles trailing spaces)
            Map<String, PDField> fieldMap = buildFieldMap(acroForm);

            fillCharacterInfo(fieldMap, character);
            fillAbilityScores(fieldMap, character);
            fillAbilityModifiers(fieldMap, character);
            fillSavingThrows(fieldMap, character);
            fillSkills(fieldMap, character);
            fillCombatStats(fieldMap, character);
            fillSpellcasting(fieldMap, character);
            fillPersonality(fieldMap, character);
            fillPhysicalCharacteristics(fieldMap, character);
            fillEquipment(fieldMap, character);
            fillSpells(fieldMap, character);
            fillSpellSlots(fieldMap, character);
            fillProficienciesAndLanguages(fieldMap, character);

            // Remove any pre-existing /AP from all widgets to prevent doubled text,
            // then set NeedAppearances so the viewer generates appearances from /V.
            removeAppearanceStreams(acroForm);
            acroForm.getCOSObject().setBoolean(COSName.NEED_APPEARANCES, true);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate character sheet PDF", e);
        }
    }

    private byte[] loadTemplate() {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(TEMPLATE_PATH)) {
            if (is == null) {
                throw new RuntimeException("PDF template not found on classpath: " + TEMPLATE_PATH);
            }
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load PDF template", e);
        }
    }

    private void removeAppearanceStreams(PDAcroForm acroForm) throws IOException {
        for (PDField field : acroForm.getFieldTree()) {
            field.getCOSObject().removeItem(COSName.AP);
            for (PDAnnotationWidget widget : field.getWidgets()) {
                widget.getCOSObject().removeItem(COSName.AP);
            }
        }
    }

    private Map<String, PDField> buildFieldMap(PDAcroForm acroForm) {
        Map<String, PDField> map = new HashMap<>();
        for (PDField field : acroForm.getFieldTree()) {
            String name = field.getFullyQualifiedName();
            // Store both the original name and the trimmed name for lookup
            map.put(name, field);
            String trimmed = name.trim();
            if (!trimmed.equals(name)) {
                map.putIfAbsent(trimmed, field);
            }
        }
        return map;
    }

    private void fillCharacterInfo(Map<String, PDField> fieldMap, Character character) {
        setTextField(fieldMap, "CharacterName", character.name());
        setTextField(fieldMap, "CharacterName 2", character.name());
        setTextField(fieldMap, "Race", character.species());
        setTextField(fieldMap, "Background", character.background());
        setTextField(fieldMap, "Alignment", character.alignment());
        setTextField(fieldMap, "XP", character.experiencePoints() != null
                ? String.valueOf(character.experiencePoints()) : null);

        // ClassLevel: combine class and level
        String classLevel = character.characterClass();
        if (classLevel != null && character.level() != null) {
            classLevel = classLevel + " " + character.level();
        }
        setTextField(fieldMap, "ClassLevel", classLevel);

        setTextField(fieldMap, "ProfBonus", formatModifier(character.proficiencyBonus()));
    }

    private void fillAbilityScores(Map<String, PDField> fieldMap, Character character) {
        AbilityScores scores = character.abilityScores();
        if (scores == null) return;

        setTextField(fieldMap, "STR", str(scores.strength()));
        setTextField(fieldMap, "DEX", str(scores.dexterity()));
        setTextField(fieldMap, "CON", str(scores.constitution()));
        setTextField(fieldMap, "INT", str(scores.intelligence()));
        setTextField(fieldMap, "WIS", str(scores.wisdom()));
        setTextField(fieldMap, "CHA", str(scores.charisma()));
    }

    private void fillAbilityModifiers(Map<String, PDField> fieldMap, Character character) {
        AbilityScores scores = character.abilityScores();
        if (scores == null) return;

        for (int i = 0; i < ABILITY_CODES.length; i++) {
            Integer modifier = scores.getModifier(ABILITY_CODES[i]);
            setTextField(fieldMap, ABILITY_MODIFIER_FIELDS[i], formatModifier(modifier));
        }
    }

    private void fillSavingThrows(Map<String, PDField> fieldMap, Character character) {
        List<SavingThrowProficiency> savingThrows = character.savingThrows();
        if (savingThrows == null) return;

        // Build a map from ability code to saving throw data
        Map<String, SavingThrowProficiency> stMap = savingThrows.stream()
                .collect(Collectors.toMap(SavingThrowProficiency::ability, st -> st, (a, b) -> a));

        for (int i = 0; i < SAVING_THROW_ABILITY_CODES.length; i++) {
            SavingThrowProficiency st = stMap.get(SAVING_THROW_ABILITY_CODES[i]);
            if (st != null) {
                if (Boolean.TRUE.equals(st.proficient())) {
                    setCheckBox(fieldMap, SAVING_THROW_CHECKBOX_FIELDS[i], true);
                }
                setTextField(fieldMap, SAVING_THROW_MODIFIER_FIELDS[i], formatModifier(st.modifier()));
            }
        }
    }

    private void fillSkills(Map<String, PDField> fieldMap, Character character) {
        List<SkillProficiency> skills = character.skills();
        if (skills == null) return;

        // Build a map from skill name to skill data
        Map<String, SkillProficiency> skillMap = skills.stream()
                .collect(Collectors.toMap(SkillProficiency::name, s -> s, (a, b) -> a));

        for (int i = 0; i < SKILL_NAMES.length; i++) {
            SkillProficiency skill = skillMap.get(SKILL_NAMES[i]);
            if (skill != null) {
                if (Boolean.TRUE.equals(skill.proficient())) {
                    setCheckBox(fieldMap, SKILL_CHECKBOX_FIELDS[i], true);
                }
                setTextField(fieldMap, SKILL_MODIFIER_FIELDS[i], formatModifier(skill.modifier()));
            }
        }
    }

    private void fillCombatStats(Map<String, PDField> fieldMap, Character character) {
        setTextField(fieldMap, "AC", str(character.armorClass()));
        setTextField(fieldMap, "Speed", str(character.speed()));
        setTextField(fieldMap, "HPMax", str(character.hitPointsMax()));
        setTextField(fieldMap, "HPCurrent", str(character.hitPointsCurrent()));
        setTextField(fieldMap, "HPTemp", str(character.hitPointsTemp()));
        setTextField(fieldMap, "HDTotal", str(character.hitDiceTotal()));
        setTextField(fieldMap, "HD", character.hitDiceType());
        setTextField(fieldMap, "Passive", str(calculatePassivePerception(character)));

        // Initiative = DEX modifier
        if (character.abilityScores() != null) {
            Integer initiative = character.abilityScores().getModifier("DEX");
            setTextField(fieldMap, "Initiative", formatModifier(initiative));
        }
    }

    private void fillSpellcasting(Map<String, PDField> fieldMap, Character character) {
        setTextField(fieldMap, "SpellcastingAbility 2", character.spellcastingAbility());
        setTextField(fieldMap, "Spellcasting Class 2", character.characterClass());

        setTextField(fieldMap, "SpellSaveDC  2", str(character.spellSaveDc()));
        setTextField(fieldMap, "SpellAtkBonus 2", formatModifier(character.spellAttackBonus()));
    }

    private void fillPersonality(Map<String, PDField> fieldMap, Character character) {
        setTextField(fieldMap, "PersonalityTraits", character.personalityTraits());
        setTextField(fieldMap, "Ideals", character.ideals());
        setTextField(fieldMap, "Bonds", character.bonds());
        setTextField(fieldMap, "Flaws", character.flaws());
    }

    private void fillPhysicalCharacteristics(Map<String, PDField> fieldMap, Character character) {
        PhysicalCharacteristics phys = character.physicalCharacteristics();
        if (phys == null) return;

        setTextField(fieldMap, "Age", phys.age());
        setTextField(fieldMap, "Height", phys.height());
        setTextField(fieldMap, "Weight", phys.weight());
        setTextField(fieldMap, "Eyes", phys.eyes());
        setTextField(fieldMap, "Skin", phys.skin());
        setTextField(fieldMap, "Hair", phys.hair());
    }

    private void fillEquipment(Map<String, PDField> fieldMap, Character character) {
        List<Equipment> equipment = character.equipment();
        if (equipment == null || equipment.isEmpty()) return;

        String text = equipment.stream()
                .map(e -> e.quantity() != null && e.quantity() > 1
                        ? e.name() + " (x" + e.quantity() + ")"
                        : e.name())
                .collect(Collectors.joining("\n"));
        setTextField(fieldMap, "Equipment", text);
    }

    private void fillSpells(Map<String, PDField> fieldMap, Character character) {
        List<CharacterSpell> spells = character.spells();
        if (spells == null || spells.isEmpty()) return;

        // Sort spells by level, then by name
        List<CharacterSpell> sorted = spells.stream()
                .sorted(Comparator.comparingInt((CharacterSpell s) -> s.level() != null ? s.level() : 0)
                        .thenComparing(s -> s.name() != null ? s.name() : ""))
                .toList();

        // Collect all "Spells *" text fields from the map, sorted numerically
        List<String> spellFieldNames = fieldMap.keySet().stream()
                .filter(name -> name.startsWith("Spells "))
                .distinct()
                .sorted(Comparator.comparingInt(this::extractSpellFieldNumber))
                .toList();

        // Fill spell fields sequentially
        int fieldIndex = 0;
        for (CharacterSpell spell : sorted) {
            if (fieldIndex >= spellFieldNames.size()) break;
            setTextField(fieldMap, spellFieldNames.get(fieldIndex), spell.name());
            fieldIndex++;
        }
    }

    private void fillSpellSlots(Map<String, PDField> fieldMap, Character character) {
        List<SpellSlotAllocation> slots = character.spellSlots();
        if (slots == null) return;

        // SlotsTotal 19 = level 1, SlotsTotal 20 = level 2, ... SlotsTotal 27 = level 9
        for (SpellSlotAllocation slot : slots) {
            if (slot.spellLevel() != null && slot.spellLevel() >= 1 && slot.spellLevel() <= 9) {
                int fieldNum = 18 + slot.spellLevel(); // level 1 -> 19, level 9 -> 27
                setTextField(fieldMap, "SlotsTotal " + fieldNum, str(slot.slotsTotal()));
                setTextField(fieldMap, "SlotsRemaining " + fieldNum, str(slot.slotsTotal()));
            }
        }
    }

    private void fillProficienciesAndLanguages(Map<String, PDField> fieldMap, Character character) {
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
            setTextField(fieldMap, "ProficienciesLang", String.join("\n", parts));
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

    private int extractSpellFieldNumber(String fieldName) {
        // "Spells 1014" -> 1014
        try {
            return Integer.parseInt(fieldName.substring("Spells ".length()).trim());
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    private void setTextField(Map<String, PDField> fieldMap, String name, String value) {
        if (value == null || value.isBlank()) return;

        PDField field = fieldMap.get(name);
        if (field == null) {
            log.fine(() -> "PDF field not found: '%s'".formatted(name));
            return;
        }

        // Set /V directly on the COS dictionary to avoid PDFBox generating
        // appearance streams that cause doubled text in some PDF viewers.
        field.getCOSObject().setString(COSName.V, value);
    }

    private void setCheckBox(Map<String, PDField> fieldMap, String name, boolean checked) {
        PDField field = fieldMap.get(name);
        if (field == null) {
            log.fine(() -> "PDF checkbox not found: '%s'".formatted(name));
            return;
        }

        if (field instanceof PDCheckBox checkbox) {
            // Set checkbox value at COS level: "Yes" = checked, "Off" = unchecked
            COSName val = checked ? COSName.YES : COSName.OFF;
            checkbox.getCOSObject().setItem(COSName.V, val);
            checkbox.getCOSObject().setItem(COSName.AS, val);
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
