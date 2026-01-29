package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record SavingThrowProficiency(
        String ability,
        Boolean proficient,
        Integer modifier
) {
}
