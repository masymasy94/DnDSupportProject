package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@JsonbPropertyOrder({
        "id", "name", "species", "subrace", "characterClass", "subclass", "background", "alignment",
        "level", "experiencePoints", "abilityScores",
        "hitPointsCurrent", "hitPointsMax", "hitPointsTemp", "armorClass", "speed",
        "hitDiceTotal", "hitDiceType", "hitDiceUsed",
        "proficiencyBonus", "inspiration",
        "spellcastingAbility", "spellSaveDc", "spellAttackBonus",
        "physicalCharacteristics", "languages", "skills", "savingThrows",
        "proficiencies", "equipment", "spells", "spellSlots",
        "personalityTraits", "ideals", "bonds", "flaws",
        "createdAt", "updatedAt"
})
@Schema(description = "Full character details")
@Builder
public record CharacterViewModel(

        @Schema(description = "Character ID", example = "1")
        Long id,

        @Schema(description = "Character name", example = "Thorin Ironforge")
        String name,

        @Schema(description = "Species/race", example = "Dwarf")
        String species,

        @Schema(description = "Subrace", example = "Hill Dwarf")
        String subrace,

        @Schema(description = "Class", example = "Cleric")
        String characterClass,

        @Schema(description = "Subclass", example = "Life Domain")
        String subclass,

        @Schema(description = "Background", example = "Acolyte")
        String background,

        @Schema(description = "Alignment", example = "Lawful Good")
        String alignment,

        @Schema(description = "Character level", example = "1")
        Integer level,

        @Schema(description = "Experience points", example = "0")
        Integer experiencePoints,

        @Schema(description = "Ability scores")
        AbilityScoresViewModel abilityScores,

        @Schema(description = "Current hit points", example = "11")
        Integer hitPointsCurrent,

        @Schema(description = "Maximum hit points", example = "11")
        Integer hitPointsMax,

        @Schema(description = "Temporary hit points", example = "0")
        Integer hitPointsTemp,

        @Schema(description = "Armor class", example = "16")
        Integer armorClass,

        @Schema(description = "Speed in feet", example = "25")
        Integer speed,

        @Schema(description = "Total hit dice", example = "1")
        Integer hitDiceTotal,

        @Schema(description = "Hit die type", example = "d8")
        String hitDiceType,

        @Schema(description = "Hit dice used", example = "0")
        Integer hitDiceUsed,

        @Schema(description = "Proficiency bonus", example = "2")
        Integer proficiencyBonus,

        @Schema(description = "Has inspiration", example = "false")
        Boolean inspiration,

        @Schema(description = "Spellcasting ability (null if not a spellcaster)", example = "WIS")
        String spellcastingAbility,

        @Schema(description = "Spell save DC (null if not a spellcaster)", example = "13")
        Integer spellSaveDc,

        @Schema(description = "Spell attack bonus (null if not a spellcaster)", example = "5")
        Integer spellAttackBonus,

        @Schema(description = "Physical characteristics")
        PhysicalCharacteristicsViewModel physicalCharacteristics,

        @Schema(description = "Known languages")
        List<String> languages,

        @Schema(description = "Skills with modifiers")
        List<SkillViewModel> skills,

        @Schema(description = "Saving throws with modifiers")
        List<SavingThrowViewModel> savingThrows,

        @Schema(description = "Other proficiencies")
        List<ProficiencyViewModel> proficiencies,

        @Schema(description = "Equipment")
        List<EquipmentViewModel> equipment,

        @Schema(description = "Known spells")
        List<SpellViewModel> spells,

        @Schema(description = "Spell slots")
        List<SpellSlotViewModel> spellSlots,

        @Schema(description = "Personality traits")
        String personalityTraits,

        @Schema(description = "Ideals")
        String ideals,

        @Schema(description = "Bonds")
        String bonds,

        @Schema(description = "Flaws")
        String flaws,

        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {
}
