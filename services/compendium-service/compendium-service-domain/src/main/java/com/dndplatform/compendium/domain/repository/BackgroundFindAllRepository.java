package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Background;

import java.util.List;

public interface BackgroundFindAllRepository {
    List<Background> findAllBackgrounds();
}
