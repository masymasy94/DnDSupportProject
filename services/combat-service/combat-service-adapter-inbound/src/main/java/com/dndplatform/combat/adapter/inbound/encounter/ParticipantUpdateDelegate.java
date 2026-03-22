package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.ParticipantViewModelMapper;
import com.dndplatform.combat.adapter.inbound.mapper.UpdateParticipantMapper;
import com.dndplatform.combat.domain.ParticipantUpdateService;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantUpdate;
import com.dndplatform.combat.domain.model.ParticipantUpdateBuilder;
import com.dndplatform.combat.view.model.ParticipantUpdateResource;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.combat.view.model.vm.UpdateParticipantRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class ParticipantUpdateDelegate implements ParticipantUpdateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantUpdateService service;
    private final UpdateParticipantMapper updateMapper;
    private final ParticipantViewModelMapper viewModelMapper;

    @Inject
    public ParticipantUpdateDelegate(ParticipantUpdateService service,
                                     UpdateParticipantMapper updateMapper,
                                     ParticipantViewModelMapper viewModelMapper) {
        this.service = service;
        this.updateMapper = updateMapper;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public ParticipantViewModel update(Long encounterId, Long participantId, UpdateParticipantRequest request) {
        log.info(() -> "Updating participant %d in encounter %d by user %d".formatted(participantId, encounterId, request.userId()));

        ParticipantUpdate baseUpdate = updateMapper.apply(request);
        ParticipantUpdate domainUpdate = ParticipantUpdateBuilder.builder()
                .withId(participantId)
                .withCurrentHp(baseUpdate.currentHp())
                .withConditions(baseUpdate.conditions())
                .withIsActive(baseUpdate.isActive())
                .build();

        EncounterParticipant participant = service.update(encounterId, request.userId(), domainUpdate);
        return viewModelMapper.apply(participant);
    }
}
