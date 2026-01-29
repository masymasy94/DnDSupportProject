package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CharacterSpell(
        String name,
        Integer level,
        String school,
        Boolean prepared,
        String source
) {
}
