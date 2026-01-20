package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "index", "name", "size", "type", "subtype", "alignment",
        "armorClass", "armorType", "hitPoints", "hitDice", "speed",
        "strength", "dexterity", "constitution", "intelligence", "wisdom", "charisma",
        "savingThrows", "skills", "senses", "languages",
        "challengeRating", "xp", "proficiencyBonus",
        "specialAbilities", "actions", "reactions", "legendaryActions", "legendaryDesc",
        "description", "source", "isPublic"})
@Schema(description = "Monster information")
@Builder
public record MonsterViewModel(
        @Schema(description = "Monster ID", example = "1")
        Integer id,

        @Schema(description = "Monster index/slug", example = "adult-red-dragon")
        String index,

        @Schema(description = "Monster name", example = "Adult Red Dragon")
        String name,

        @Schema(description = "Monster size", example = "Huge")
        String size,

        @Schema(description = "Monster type", example = "Dragon")
        String type,

        @Schema(description = "Monster subtype", example = "chromatic")
        String subtype,

        @Schema(description = "Monster alignment", example = "chaotic evil")
        String alignment,

        @Schema(description = "Armor class", example = "19")
        Integer armorClass,

        @Schema(description = "Armor type", example = "natural armor")
        String armorType,

        @Schema(description = "Hit points", example = "256")
        Integer hitPoints,

        @Schema(description = "Hit dice", example = "17d12+136")
        String hitDice,

        @Schema(description = "Speed by movement type (JSON)")
        String speed,

        @Schema(description = "Strength score", example = "27")
        Integer strength,

        @Schema(description = "Dexterity score", example = "10")
        Integer dexterity,

        @Schema(description = "Constitution score", example = "25")
        Integer constitution,

        @Schema(description = "Intelligence score", example = "16")
        Integer intelligence,

        @Schema(description = "Wisdom score", example = "13")
        Integer wisdom,

        @Schema(description = "Charisma score", example = "21")
        Integer charisma,

        @Schema(description = "Saving throw bonuses (JSON)")
        String savingThrows,

        @Schema(description = "Skill bonuses (JSON)")
        String skills,

        @Schema(description = "Senses (JSON)")
        String senses,

        @Schema(description = "Languages known", example = "Common, Draconic")
        String languages,

        @Schema(description = "Challenge rating", example = "17")
        String challengeRating,

        @Schema(description = "Experience points", example = "18000")
        Integer xp,

        @Schema(description = "Proficiency bonus", example = "6")
        Integer proficiencyBonus,

        @Schema(description = "Special abilities (JSON)")
        String specialAbilities,

        @Schema(description = "Actions (JSON)")
        String actions,

        @Schema(description = "Reactions (JSON)")
        String reactions,

        @Schema(description = "Legendary actions (JSON)")
        String legendaryActions,

        @Schema(description = "Legendary actions description")
        String legendaryDesc,

        @Schema(description = "Monster description/lore")
        String description,

        @Schema(description = "Source type", example = "OFFICIAL")
        String source,

        @Schema(description = "Whether homebrew is publicly visible")
        Boolean isPublic
) {}
