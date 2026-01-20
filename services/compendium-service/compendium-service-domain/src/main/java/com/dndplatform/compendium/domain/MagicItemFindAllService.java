package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;

public interface MagicItemFindAllService {
    PagedResult<MagicItem> findAll(MagicItemFilterCriteria criteria);
}
