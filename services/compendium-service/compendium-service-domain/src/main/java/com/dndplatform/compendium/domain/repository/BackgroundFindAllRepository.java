package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.PagedBackgroundResult;
import com.dndplatform.compendium.domain.model.SourceType;

public interface BackgroundFindAllRepository {
    PagedBackgroundResult findAll(int page, int size, String search, SourceType source, Long userId);
}
