package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;

public interface CharacterUpdateService {
    Character update(Long characterId, CharacterCreate input);
}
