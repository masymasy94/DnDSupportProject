package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.SpellSlotAllocation;

import java.util.List;

public interface CharacterSpellSlotsCalculator {
    List<SpellSlotAllocation> calculateSpellSlots(String className, int level);
}
