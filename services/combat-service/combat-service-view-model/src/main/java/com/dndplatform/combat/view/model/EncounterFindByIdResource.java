package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.EncounterViewModel;

public interface EncounterFindByIdResource {
    EncounterViewModel findById(Long id);
}
