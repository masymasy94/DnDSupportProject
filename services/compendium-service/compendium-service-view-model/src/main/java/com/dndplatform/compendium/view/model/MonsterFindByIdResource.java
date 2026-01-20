package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.MonsterViewModel;

public interface MonsterFindByIdResource {
    MonsterViewModel findById(int id);
}
