package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.WeaponType;

import java.util.List;

public interface WeaponTypeFindAllRepository {
    List<WeaponType> findAllWeaponTypes(String category);
}
