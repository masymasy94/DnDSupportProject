package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.EncounterFindByCampaignService;
import com.dndplatform.combat.view.model.EncounterFindByCampaignResource;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class EncounterFindByCampaignDelegate implements EncounterFindByCampaignResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterFindByCampaignService service;
    private final EncounterViewModelMapper viewModelMapper;

    @Inject
    public EncounterFindByCampaignDelegate(EncounterFindByCampaignService service,
                                           EncounterViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public List<EncounterViewModel> findByCampaign(Long campaignId, Long userId) {
        log.info(() -> "Finding encounters for campaign %d".formatted(campaignId));
        return service.findByCampaign(campaignId).stream()
                .map(viewModelMapper)
                .toList();
    }
}
