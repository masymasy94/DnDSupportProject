package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.repository.ParticipantFindByEncounterRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class TurnOrderFindServiceImpl implements TurnOrderFindService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final ParticipantFindByEncounterRepository repository;

    @Inject
    public TurnOrderFindServiceImpl(ParticipantFindByEncounterRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EncounterParticipant> getTurnOrder(Long encounterId) {
        log.info(() -> "Getting turn order for encounter %d".formatted(encounterId));
        return repository.findByEncounterId(encounterId);
    }
}
