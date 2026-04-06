package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.EncounterCompleteService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.view.model.EncounterCompleteResource;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class EncounterCompleteDelegate implements EncounterCompleteResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterCompleteService service;
    private final EncounterViewModelMapper viewModelMapper;

    @Inject
    public EncounterCompleteDelegate(EncounterCompleteService service,
                                     EncounterViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public EncounterViewModel complete(Long encounterId, Long userId) {
        log.info(() -> "Completing encounter %d by user %d".formatted(encounterId, userId));
        Encounter encounter = service.complete(encounterId, userId);
        return viewModelMapper.apply(encounter);
    }
}
