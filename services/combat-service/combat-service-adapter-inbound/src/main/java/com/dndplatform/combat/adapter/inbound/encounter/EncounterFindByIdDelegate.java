package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.EncounterFindByIdService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.view.model.EncounterFindByIdResource;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class EncounterFindByIdDelegate implements EncounterFindByIdResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterFindByIdService service;
    private final EncounterViewModelMapper viewModelMapper;

    @Inject
    public EncounterFindByIdDelegate(EncounterFindByIdService service,
                                     EncounterViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public EncounterViewModel findById(Long id) {
        log.info(() -> "Finding encounter by ID: %d".formatted(id));
        Encounter encounter = service.findById(id);
        return viewModelMapper.apply(encounter);
    }
}
