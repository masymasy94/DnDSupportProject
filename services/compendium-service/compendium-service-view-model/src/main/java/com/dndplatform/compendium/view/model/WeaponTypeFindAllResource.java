package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;

import java.util.List;

public interface WeaponTypeFindAllResource {
    List<WeaponTypeViewModel> findAll(String category);
}
