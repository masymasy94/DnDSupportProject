package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record SkillProficiency(
        String name,
        String ability,
        Boolean proficient,
        Boolean expertise,
        Integer modifier
) {
}
