package com.dndplatform.character.view.model;

import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.character.view.model.vm.CreateCharacterRequest;
import jakarta.validation.Valid;

public interface CharacterCreateResource {
    CharacterViewModel create(@Valid CreateCharacterRequest request);
}
