package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.ArmorType;

import java.util.List;

public interface ArmorTypeFindAllRepository {
    List<ArmorType> findAllArmorTypes();
}
