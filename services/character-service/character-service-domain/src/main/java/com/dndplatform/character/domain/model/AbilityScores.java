package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record AbilityScores(
        Integer strength,
        Integer dexterity,
        Integer constitution,
        Integer intelligence,
        Integer wisdom,
        Integer charisma
) {
    public Integer getModifier(String ability) {
        Integer score = switch (ability.toUpperCase()) {
            case "STR", "STRENGTH" -> strength;
            case "DEX", "DEXTERITY" -> dexterity;
            case "CON", "CONSTITUTION" -> constitution;
            case "INT", "INTELLIGENCE" -> intelligence;
            case "WIS", "WISDOM" -> wisdom;
            case "CHA", "CHARISMA" -> charisma;
            default -> 10;
        };
        return (score - 10) / 2;
    }
}
