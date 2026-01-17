package com.dndplatform.character.domain.repository;

import com.dndplatform.character.domain.model.PagedResult;

public interface CharacterFindAllRepository {
    PagedResult findAll(int page, int size);
}
