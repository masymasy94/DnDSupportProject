package com.dndplatform.character.view.model;

import com.dndplatform.character.view.model.vm.PagedCharactersViewModel;

public interface CharacterFindAllResource {
    PagedCharactersViewModel findAll(int page, int size);
}
