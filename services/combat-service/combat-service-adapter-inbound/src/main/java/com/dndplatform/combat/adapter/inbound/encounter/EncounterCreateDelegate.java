package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.CreateEncounterMapper;
import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.EncounterCreateService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterCreate;
import com.dndplatform.combat.view.model.EncounterCreateResource;
import com.dndplatform.combat.view.model.vm.CreateEncounterRequest;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class EncounterCreateDelegate implements EncounterCreateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterCreateService service;
    private final CreateEncounterMapper createMapper;
    private final EncounterViewModelMapper viewModelMapper;

    @Inject
    public EncounterCreateDelegate(EncounterCreateService service,
                                   CreateEncounterMapper createMapper,
                                   EncounterViewModelMapper viewModelMapper) {
        this.service = service;
        this.createMapper = createMapper;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public EncounterViewModel create(CreateEncounterRequest request) {
        log.info(() -> "Creating encounter for campaign %d by user %d".formatted(request.campaignId(), request.userId()));

        EncounterCreate domainCreate = createMapper.apply(request);
        Encounter encounter = service.create(domainCreate);
        return viewModelMapper.apply(encounter);
    }
}
