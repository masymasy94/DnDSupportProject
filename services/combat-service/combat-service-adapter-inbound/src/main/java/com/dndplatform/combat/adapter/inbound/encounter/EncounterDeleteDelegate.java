package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.domain.EncounterDeleteService;
import com.dndplatform.combat.view.model.EncounterDeleteResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class EncounterDeleteDelegate implements EncounterDeleteResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterDeleteService service;

    @Inject
    public EncounterDeleteDelegate(EncounterDeleteService service) {
        this.service = service;
    }

    @Override
    public void delete(Long id, Long userId) {
        log.info(() -> "Deleting encounter %d by user %d".formatted(id, userId));
        service.delete(id, userId);
    }
}
