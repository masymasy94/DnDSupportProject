package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.model.PagedResult;

public interface MonsterFindAllService {
    PagedResult<Monster> findAll(MonsterFilterCriteria criteria);
}
