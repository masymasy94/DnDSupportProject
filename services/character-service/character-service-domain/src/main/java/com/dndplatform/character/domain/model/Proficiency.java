package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Proficiency(
        String type,  // ARMOR, WEAPON, TOOL, LANGUAGE
        String name
) {
}
