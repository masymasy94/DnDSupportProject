package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.AddParticipantRequest;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import jakarta.validation.Valid;

public interface ParticipantAddResource {
    ParticipantViewModel add(Long encounterId, @Valid AddParticipantRequest request);
}
