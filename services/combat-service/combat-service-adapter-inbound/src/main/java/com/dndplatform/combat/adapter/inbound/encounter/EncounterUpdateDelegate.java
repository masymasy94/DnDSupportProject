package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.adapter.inbound.mapper.UpdateEncounterMapper;
import com.dndplatform.combat.domain.EncounterUpdateService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterUpdate;
import com.dndplatform.combat.domain.model.EncounterUpdateBuilder;
import com.dndplatform.combat.view.model.EncounterUpdateResource;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.combat.view.model.vm.UpdateEncounterRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class EncounterUpdateDelegate implements EncounterUpdateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterUpdateService service;
    private final UpdateEncounterMapper updateMapper;
    private final EncounterViewModelMapper viewModelMapper;

    @Inject
    public EncounterUpdateDelegate(EncounterUpdateService service,
                                   UpdateEncounterMapper updateMapper,
                                   EncounterViewModelMapper viewModelMapper) {
        this.service = service;
        this.updateMapper = updateMapper;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public EncounterViewModel update(Long id, UpdateEncounterRequest request) {
        log.info(() -> "Updating encounter %d by user %d".formatted(id, request.userId()));

        EncounterUpdate baseUpdate = updateMapper.apply(request);
        EncounterUpdate domainUpdate = EncounterUpdateBuilder.builder()
                .withId(id)
                .withName(baseUpdate.name())
                .withDescription(baseUpdate.description())
                .withPartyLevel(baseUpdate.partyLevel())
                .withPartySize(baseUpdate.partySize())
                .withParticipants(baseUpdate.participants())
                .build();

        Encounter encounter = service.update(request.userId(), domainUpdate);
        return viewModelMapper.apply(encounter);
    }
}
