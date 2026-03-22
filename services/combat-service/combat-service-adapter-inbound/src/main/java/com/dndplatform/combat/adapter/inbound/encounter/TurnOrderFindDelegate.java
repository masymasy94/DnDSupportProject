package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.ParticipantViewModelMapper;
import com.dndplatform.combat.domain.TurnOrderFindService;
import com.dndplatform.combat.view.model.TurnOrderFindResource;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class TurnOrderFindDelegate implements TurnOrderFindResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final TurnOrderFindService service;
    private final ParticipantViewModelMapper viewModelMapper;

    @Inject
    public TurnOrderFindDelegate(TurnOrderFindService service,
                                 ParticipantViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public List<ParticipantViewModel> getTurnOrder(Long encounterId) {
        log.info(() -> "Getting turn order for encounter %d".formatted(encounterId));
        return service.getTurnOrder(encounterId).stream()
                .map(viewModelMapper)
                .toList();
    }
}
