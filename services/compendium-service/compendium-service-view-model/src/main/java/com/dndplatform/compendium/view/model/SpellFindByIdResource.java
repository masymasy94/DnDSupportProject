package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.SpellViewModel;

public interface SpellFindByIdResource {
    SpellViewModel findById(int id);
}
