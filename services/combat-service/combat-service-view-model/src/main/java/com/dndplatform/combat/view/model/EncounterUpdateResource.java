package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.combat.view.model.vm.UpdateEncounterRequest;
import jakarta.validation.Valid;

public interface EncounterUpdateResource {
    EncounterViewModel update(Long id, @Valid UpdateEncounterRequest request);
}
