package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.WeaponType;

import java.util.List;

public interface WeaponTypeFindAllService {
    List<WeaponType> findAll(String category);
}
