package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.combat.view.model.vm.UpdateParticipantRequest;
import jakarta.validation.Valid;

public interface ParticipantUpdateResource {
    ParticipantViewModel update(Long encounterId, Long participantId, @Valid UpdateParticipantRequest request);
}
