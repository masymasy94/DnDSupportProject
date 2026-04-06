package com.dndplatform.character.view.model;

import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.character.view.model.vm.UpdateCharacterRequest;
import jakarta.validation.Valid;

public interface CharacterUpdateResource {
    CharacterViewModel update(Long id, @Valid UpdateCharacterRequest request);
}
