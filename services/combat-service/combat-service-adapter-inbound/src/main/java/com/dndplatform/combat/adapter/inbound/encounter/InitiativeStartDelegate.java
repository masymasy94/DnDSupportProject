package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.InitiativeStartService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.view.model.InitiativeStartResource;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class InitiativeStartDelegate implements InitiativeStartResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final InitiativeStartService service;
    private final EncounterViewModelMapper viewModelMapper;

    @Inject
    public InitiativeStartDelegate(InitiativeStartService service,
                                   EncounterViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public EncounterViewModel start(Long encounterId, Long userId) {
        log.info(() -> "Starting initiative for encounter %d by user %d".formatted(encounterId, userId));
        Encounter encounter = service.start(encounterId, userId);
        return viewModelMapper.apply(encounter);
    }
}
