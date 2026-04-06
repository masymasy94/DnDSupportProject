package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.EncounterViewModel;

public interface EncounterCompleteResource {
    EncounterViewModel complete(Long encounterId, Long userId);
}
