package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.SpellViewModel;

import java.util.List;

public interface SpellFindAllResource {
    List<SpellViewModel> findAll(Integer level, String school, Boolean concentration, Boolean ritual);
}
