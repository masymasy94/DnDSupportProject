package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;

public interface MagicItemFindAllRepository {
    PagedResult<MagicItem> findAllMagicItems(MagicItemFilterCriteria criteria);
}
