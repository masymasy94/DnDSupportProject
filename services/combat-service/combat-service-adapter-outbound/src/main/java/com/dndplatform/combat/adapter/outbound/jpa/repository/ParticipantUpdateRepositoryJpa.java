package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantUpdate;
import com.dndplatform.combat.domain.repository.ParticipantUpdateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantUpdateRepositoryJpa implements ParticipantUpdateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterEntityMapper mapper;
    private final ParticipantPanacheRepository participantRepository;

    @Inject
    public ParticipantUpdateRepositoryJpa(EncounterEntityMapper mapper,
                                          ParticipantPanacheRepository participantRepository) {
        this.mapper = mapper;
        this.participantRepository = participantRepository;
    }

    @Override
    @Transactional
    public EncounterParticipant update(ParticipantUpdate input) {
        log.info(() -> "Updating participant: %d".formatted(input.id()));

        var entity = participantRepository.findByIdOptional(input.id())
                .orElseThrow(() -> new NotFoundException("Participant not found with ID: %d".formatted(input.id())));

        if (input.currentHp() != null) entity.currentHp = input.currentHp();
        if (input.conditions() != null) entity.conditions = mapper.serializeConditions(input.conditions());
        if (input.isActive() != null) entity.isActive = input.isActive();

        participantRepository.persist(entity);

        log.info(() -> "Participant %d updated successfully".formatted(input.id()));
        return mapper.toParticipant(entity);
    }
}
