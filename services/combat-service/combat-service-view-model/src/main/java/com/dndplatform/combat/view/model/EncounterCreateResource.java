package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.CreateEncounterRequest;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import jakarta.validation.Valid;

public interface EncounterCreateResource {
    EncounterViewModel create(@Valid CreateEncounterRequest request);
}
