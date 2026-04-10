package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantFindByEncounterRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class TurnOrderFindServiceImpl implements TurnOrderFindService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final ParticipantFindByEncounterRepository repository;
    private final EncounterFindByIdRepository encounterFindByIdRepository;

    @Inject
    public TurnOrderFindServiceImpl(ParticipantFindByEncounterRepository repository,
                                    EncounterFindByIdRepository encounterFindByIdRepository) {
        this.repository = repository;
        this.encounterFindByIdRepository = encounterFindByIdRepository;
    }

    @Override
    public List<EncounterParticipant> getTurnOrder(Long encounterId) {
        log.info(() -> "Getting turn order for encounter %d".formatted(encounterId));

        encounterFindByIdRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        return repository.findByEncounterId(encounterId);
    }
}
