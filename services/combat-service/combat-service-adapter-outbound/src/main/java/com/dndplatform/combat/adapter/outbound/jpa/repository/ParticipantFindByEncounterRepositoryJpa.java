package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
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

    @Inject
    public ParticipantFindByEncounterRepositoryJpa(EncounterEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<EncounterParticipant> findByEncounterId(Long encounterId) {
        log.info(() -> "Finding participants for encounter: %d".formatted(encounterId));

        List<EncounterParticipantEntity> entities = EncounterParticipantEntity
                .find("encounter.id = ?1 order by sortOrder asc", encounterId)
                .list();

        return entities.stream().map(mapper::toParticipant).toList();
    }
}
