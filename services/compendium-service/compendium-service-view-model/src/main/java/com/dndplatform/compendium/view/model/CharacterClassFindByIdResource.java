package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;

public interface CharacterClassFindByIdResource {
    CharacterClassViewModel findById(int id);
}
