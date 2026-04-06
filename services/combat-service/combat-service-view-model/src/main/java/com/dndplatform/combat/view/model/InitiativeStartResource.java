package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.EncounterViewModel;

public interface InitiativeStartResource {
    EncounterViewModel start(Long encounterId, Long userId);
}
