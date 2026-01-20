package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.model.PagedResult;

public interface MonsterFindAllRepository {
    PagedResult<Monster> findAllMonsters(MonsterFilterCriteria criteria);
}
