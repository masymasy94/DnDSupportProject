package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.Character;

public interface CharacterSheetGenerator {
    byte[] generate(Character character);
}
