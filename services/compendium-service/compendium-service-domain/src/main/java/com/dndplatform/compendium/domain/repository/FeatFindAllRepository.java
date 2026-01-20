package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Feat;

import java.util.List;

public interface FeatFindAllRepository {
    List<Feat> findAllFeats();
}
