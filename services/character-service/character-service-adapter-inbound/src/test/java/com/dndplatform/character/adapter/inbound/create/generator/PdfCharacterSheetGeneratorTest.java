package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PdfCharacterSheetGeneratorTest {

    private PdfCharacterSheetGenerator sut;

    @BeforeEach
    void setUp() {
        sut = new PdfCharacterSheetGenerator(new CharacterFieldMapper());
    }

    @Test
    void shouldGenerateValidPdfWithCharacterData() throws IOException {
        Character character = CharacterBuilder.builder()
                .withName("Gandalf")
                .withSpecies("Human")
                .withCharacterClass("Wizard")
                .withLevel(20)
                .withBackground("Sage")
                .withAlignment("Neutral Good")
                .withExperiencePoints(355000)
                .withProficiencyBonus(6)
                .withArmorClass(15)
                .withSpeed(30)
                .withHitPointsMax(102)
                .withHitPointsCurrent(98)
                .withHitDiceTotal(20)
                .withHitDiceType("d6")
                .withAbilityScores(AbilityScoresBuilder.builder()
                        .withStrength(10).withDexterity(14).withConstitution(14)
                        .withIntelligence(20).withWisdom(16).withCharisma(12)
                        .build())
                .withPersonalityTraits("Curious and wise")
                .withIdeals("Knowledge is power")
                .withBonds("The fellowship")
                .withFlaws("Overly cryptic")
                .build();

        byte[] pdfBytes = sut.generate(character);

        assertThat(pdfBytes).isNotEmpty();

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertThat(form).isNotNull();

            assertThat(fieldValue(form, "CharacterName")).isEqualTo("Gandalf");
            assertThat(fieldValue(form, "Race")).isEqualTo("Human");
            assertThat(fieldValue(form, "ClassLevel")).isEqualTo("Wizard 20");
            assertThat(fieldValue(form, "Background")).isEqualTo("Sage");
            assertThat(fieldValue(form, "Alignment")).isEqualTo("Neutral Good");
            assertThat(fieldValue(form, "STR")).isEqualTo("10");
            assertThat(fieldValue(form, "INT")).isEqualTo("20");
            assertThat(fieldValue(form, "INTmod")).isEqualTo("+5");
            assertThat(fieldValue(form, "AC")).isEqualTo("15");
            assertThat(fieldValue(form, "HPMax")).isEqualTo("102");
            assertThat(fieldValue(form, "PersonalityTraits")).isEqualTo("Curious and wise");
        }
    }

    @Test
    void shouldGeneratePdfWithSkillsAndSavingThrows() throws IOException {
        Character character = CharacterBuilder.builder()
                .withName("TestChar")
                .withSkills(List.of(
                        SkillProficiencyBuilder.builder().withName("Arcana").withProficient(true).withModifier(9).build(),
                        SkillProficiencyBuilder.builder().withName("Perception").withProficient(true).withModifier(6).build()
                ))
                .withSavingThrows(List.of(
                        SavingThrowProficiencyBuilder.builder().withAbility("INT").withProficient(true).withModifier(9).build(),
                        SavingThrowProficiencyBuilder.builder().withAbility("WIS").withProficient(true).withModifier(6).build()
                ))
                .build();

        byte[] pdfBytes = sut.generate(character);

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();

            assertThat(fieldValue(form, "Arcana")).isEqualTo("+9");
            assertThat(fieldValue(form, "Perception")).isEqualTo("+6");
            assertThat(fieldValue(form, "ST Intelligence")).isEqualTo("+9");
            assertThat(fieldValue(form, "ST Wisdom")).isEqualTo("+6");

            assertThat(isChecked(form, "Check Box 25")).isTrue();  // Arcana
            assertThat(isChecked(form, "Check Box 14")).isTrue();  // INT save
            assertThat(isChecked(form, "Check Box 15")).isTrue();  // WIS save
        }
    }

    @Test
    void shouldGeneratePdfWithEquipmentAndSpellSlots() throws IOException {
        Character character = CharacterBuilder.builder()
                .withName("TestChar")
                .withEquipment(List.of(
                        EquipmentBuilder.builder().withName("Staff").withQuantity(1).build(),
                        EquipmentBuilder.builder().withName("Potion").withQuantity(5).build()
                ))
                .withSpellSlots(List.of(
                        SpellSlotAllocationBuilder.builder().withSpellLevel(1).withSlotsTotal(4).build(),
                        SpellSlotAllocationBuilder.builder().withSpellLevel(3).withSlotsTotal(2).build()
                ))
                .build();

        byte[] pdfBytes = sut.generate(character);

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();

            assertThat(fieldValue(form, "Equipment")).isEqualTo("Staff\nPotion (x5)");
            assertThat(fieldValue(form, "SlotsTotal 19")).isEqualTo("4");
            assertThat(fieldValue(form, "SlotsTotal 21")).isEqualTo("2");
        }
    }

    @Test
    void shouldGeneratePdfWithMinimalData() throws IOException {
        Character character = CharacterBuilder.builder()
                .withName("Minimal")
                .build();

        byte[] pdfBytes = sut.generate(character);

        assertThat(pdfBytes).isNotEmpty();
        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertThat(fieldValue(form, "CharacterName")).isEqualTo("Minimal");
        }
    }

    @Test
    void shouldGeneratePassivePerceptionFromSkills() throws IOException {
        Character character = CharacterBuilder.builder()
                .withName("Perceptive")
                .withSkills(List.of(
                        SkillProficiencyBuilder.builder().withName("Perception").withProficient(true).withModifier(5).build()
                ))
                .build();

        byte[] pdfBytes = sut.generate(character);

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertThat(fieldValue(form, "Passive")).isEqualTo("15");
        }
    }

    private String fieldValue(PDAcroForm form, String fieldName) {
        PDField field = form.getField(fieldName);
        if (field == null) {
            // Try with trailing space (WotC PDF quirk)
            for (PDField f : form.getFieldTree()) {
                if (f.getFullyQualifiedName().trim().equals(fieldName)) {
                    return f.getValueAsString();
                }
            }
            return null;
        }
        return field.getValueAsString();
    }

    private boolean isChecked(PDAcroForm form, String fieldName) {
        PDField field = form.getField(fieldName);
        if (field instanceof PDCheckBox checkbox) {
            return checkbox.isChecked();
        }
        return false;
    }
}
