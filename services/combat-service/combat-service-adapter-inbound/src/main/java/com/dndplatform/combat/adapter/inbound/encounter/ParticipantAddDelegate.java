package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.AddParticipantMapper;
import com.dndplatform.combat.adapter.inbound.mapper.ParticipantViewModelMapper;
import com.dndplatform.combat.domain.ParticipantAddService;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.view.model.ParticipantAddResource;
import com.dndplatform.combat.view.model.vm.AddParticipantRequest;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class ParticipantAddDelegate implements ParticipantAddResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantAddService service;
    private final AddParticipantMapper addMapper;
    private final ParticipantViewModelMapper viewModelMapper;

    @Inject
    public ParticipantAddDelegate(ParticipantAddService service,
                                  AddParticipantMapper addMapper,
                                  ParticipantViewModelMapper viewModelMapper) {
        this.service = service;
        this.addMapper = addMapper;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public ParticipantViewModel add(Long encounterId, AddParticipantRequest request) {
        log.info(() -> "Adding participant to encounter %d by user %d".formatted(encounterId, request.userId()));

        ParticipantCreate create = addMapper.apply(request);
        EncounterParticipant participant = service.add(encounterId, request.userId(), create);
        return viewModelMapper.apply(participant);
    }
}
