package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.ParticipantViewModel;

import java.util.List;

public interface TurnAdvanceResource {
    List<ParticipantViewModel> advance(Long encounterId, Long userId);
}
