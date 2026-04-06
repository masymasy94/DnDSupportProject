package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.repository.ParticipantFindByEncounterRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantFindByEncounterRepositoryJpa implements ParticipantFindByEncounterRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterEntityMapper mapper;
    private final ParticipantPanacheRepository participantRepository;

    @Inject
    public ParticipantFindByEncounterRepositoryJpa(EncounterEntityMapper mapper,
                                                   ParticipantPanacheRepository participantRepository) {
        this.mapper = mapper;
        this.participantRepository = participantRepository;
    }

    @Override
    public List<EncounterParticipant> findByEncounterId(Long encounterId) {
        log.info(() -> "Finding participants for encounter: %d".formatted(encounterId));

        return participantRepository.findByEncounterIdOrderBySortOrder(encounterId)
                .stream()
                .map(mapper::toParticipant)
                .toList();
    }
}
