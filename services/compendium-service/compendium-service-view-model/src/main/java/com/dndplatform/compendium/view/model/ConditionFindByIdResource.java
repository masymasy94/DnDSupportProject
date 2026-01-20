package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.ConditionViewModel;

public interface ConditionFindByIdResource {
    ConditionViewModel findById(int id);
}
