package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.ParticipantViewModelMapper;
import com.dndplatform.combat.domain.TurnAdvanceService;
import com.dndplatform.combat.view.model.TurnAdvanceResource;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class TurnAdvanceDelegate implements TurnAdvanceResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final TurnAdvanceService service;
    private final ParticipantViewModelMapper viewModelMapper;

    @Inject
    public TurnAdvanceDelegate(TurnAdvanceService service,
                               ParticipantViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public List<ParticipantViewModel> advance(Long encounterId, Long userId) {
        log.info(() -> "Advancing turn for encounter %d by user %d".formatted(encounterId, userId));
        return service.advance(encounterId, userId).stream()
                .map(viewModelMapper)
                .toList();
    }
}
