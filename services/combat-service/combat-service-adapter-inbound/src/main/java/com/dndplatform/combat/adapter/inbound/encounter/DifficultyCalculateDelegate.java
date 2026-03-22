package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.DifficultyResultViewModelMapper;
import com.dndplatform.combat.domain.DifficultyCalculateService;
import com.dndplatform.combat.domain.model.DifficultyResult;
import com.dndplatform.combat.view.model.DifficultyCalculateResource;
import com.dndplatform.combat.view.model.vm.DifficultyResultViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class DifficultyCalculateDelegate implements DifficultyCalculateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final DifficultyCalculateService service;
    private final DifficultyResultViewModelMapper viewModelMapper;

    @Inject
    public DifficultyCalculateDelegate(DifficultyCalculateService service,
                                       DifficultyResultViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public DifficultyResultViewModel calculate(Long encounterId) {
        log.info(() -> "Calculating difficulty for encounter %d".formatted(encounterId));
        DifficultyResult result = service.calculate(encounterId);
        return viewModelMapper.apply(result);
    }
}
