package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.MagicItem;

import java.util.Optional;

public interface MagicItemFindByIdRepository {
    Optional<MagicItem> findById(int id);
}
