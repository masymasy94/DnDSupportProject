package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.domain.ParticipantDeleteService;
import com.dndplatform.combat.view.model.ParticipantDeleteResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class ParticipantDeleteDelegate implements ParticipantDeleteResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantDeleteService service;

    @Inject
    public ParticipantDeleteDelegate(ParticipantDeleteService service) {
        this.service = service;
    }

    @Override
    public void delete(Long encounterId, Long participantId, Long userId) {
        log.info(() -> "Deleting participant %d from encounter %d by user %d".formatted(participantId, encounterId, userId));
        service.delete(encounterId, participantId, userId);
    }
}
