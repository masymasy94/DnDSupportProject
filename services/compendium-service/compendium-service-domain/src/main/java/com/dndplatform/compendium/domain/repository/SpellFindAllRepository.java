package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.PagedSpellResult;
import com.dndplatform.compendium.domain.model.SourceType;

public interface SpellFindAllRepository {
    PagedSpellResult findAll(int page, int size, String search, Integer level, String school,
                             Boolean ritual, Boolean concentration, SourceType source, Long userId);
}
