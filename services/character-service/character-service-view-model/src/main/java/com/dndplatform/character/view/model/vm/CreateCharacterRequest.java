package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonbPropertyOrder({
        "name", "species", "subrace", "characterClass", "subclass", "background", "alignment", "level",
        "abilityScores", "skillProficiencies", "savingThrowProficiencies", "languages",
        "proficiencies", "equipment", "spells", "physicalCharacteristics",
        "personalityTraits", "ideals", "bonds", "flaws"
})
@Schema(description = "Request to create a new character")
@Builder
public record CreateCharacterRequest(

        @Schema(description = "Character name", example = "Thorin Ironforge")
        @NotBlank @Size(min = 1, max = 100)
        String name,

        @Schema(description = "Species/race name", example = "Dwarf")
        @NotBlank @Size(max = 50)
        String species,

        @Schema(description = "Subrace name (optional)", example = "Hill Dwarf")
        @Size(max = 50)
        String subrace,

        @Schema(description = "Class name", example = "Cleric")
        @NotBlank @Size(max = 30)
        String characterClass,

        @Schema(description = "Subclass name (optional)", example = "Life Domain")
        @Size(max = 50)
        String subclass,

        @Schema(description = "Background name", example = "Acolyte")
        @Size(max = 50)
        String background,

        @Schema(description = "Alignment", example = "Lawful Good")
        @Size(max = 30)
        String alignment,

        @Schema(description = "Character level", example = "1", minimum = "1", maximum = "20")
        @NotNull @Min(1) @Max(20)
        Integer level,

        @Schema(description = "Ability scores")
        @NotNull @Valid
        AbilityScoresRequest abilityScores,

        @Schema(description = "Skill proficiencies", example = "[\"Insight\", \"Religion\"]")
        List<@NotBlank String> skillProficiencies,

        @Schema(description = "Saving throw proficiencies (ability codes)", example = "[\"WIS\", \"CHA\"]")
        List<@NotBlank @Pattern(regexp = "STR|DEX|CON|INT|WIS|CHA") String> savingThrowProficiencies,

        @Schema(description = "Known languages", example = "[\"Common\", \"Dwarvish\", \"Celestial\"]")
        List<@NotBlank String> languages,

        @Schema(description = "Other proficiencies (armor, weapons, tools)")
        List<@Valid ProficiencyRequest> proficiencies,

        @Schema(description = "Starting equipment")
        List<@Valid EquipmentRequest> equipment,

        @Schema(description = "Known spells (spell names)", example = "[\"Guidance\", \"Sacred Flame\", \"Bless\", \"Cure Wounds\"]")
        List<@NotBlank String> spells,

        @Schema(description = "Physical characteristics")
        @Valid
        PhysicalCharacteristicsRequest physicalCharacteristics,

        @Schema(description = "Personality traits", example = "I quote sacred texts in almost every situation.")
        String personalityTraits,

        @Schema(description = "Ideals", example = "Faith. I trust that my deity will guide my actions.")
        String ideals,

        @Schema(description = "Bonds", example = "I will do anything to protect the temple where I served.")
        String bonds,

        @Schema(description = "Flaws", example = "I judge others harshly, and myself even more severely.")
        String flaws
) {
}
