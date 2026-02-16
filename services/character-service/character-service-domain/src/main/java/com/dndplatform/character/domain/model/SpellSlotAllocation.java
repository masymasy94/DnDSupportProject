package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record SpellSlotAllocation(
        Integer spellLevel,
        Integer slotsTotal
) {
}
