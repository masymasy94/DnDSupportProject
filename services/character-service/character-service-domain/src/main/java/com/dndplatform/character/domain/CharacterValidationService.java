package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.ValidatedCompendiumData;

public interface CharacterValidationService {
    ValidatedCompendiumData validate(CharacterCreate input);
}
