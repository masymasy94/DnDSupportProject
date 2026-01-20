package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.PagedResult;

public interface CharacterFindAllService {
    PagedResult findAll(int page, int size);
}
