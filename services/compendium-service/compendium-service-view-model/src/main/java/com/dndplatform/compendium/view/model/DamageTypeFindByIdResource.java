package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;

public interface DamageTypeFindByIdResource {
    DamageTypeViewModel findById(int id);
}
