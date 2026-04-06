package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.domain.repository.ParticipantAddRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantAddRepositoryJpa implements ParticipantAddRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterEntityMapper mapper;
    private final EncounterPanacheRepository encounterRepository;

    @Inject
    public ParticipantAddRepositoryJpa(EncounterEntityMapper mapper,
                                       EncounterPanacheRepository encounterRepository) {
        this.mapper = mapper;
        this.encounterRepository = encounterRepository;
    }

    @Override
    @Transactional
    public EncounterParticipant add(Long encounterId, ParticipantCreate input) {
        log.info(() -> "Adding participant '%s' to encounter %d".formatted(input.name(), encounterId));

        var encounter = encounterRepository.findByIdOptional(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        EncounterParticipantEntity pe = new EncounterParticipantEntity();
        pe.encounter = encounter;
        pe.name = input.name();
        pe.participantType = input.type().name();
        pe.currentHp = input.maxHp();
        pe.maxHp = input.maxHp();
        pe.armorClass = input.armorClass();
        pe.monsterId = input.monsterId();
        pe.sourceJson = input.sourceJson();

        encounter.participants.add(pe);
        encounterRepository.persist(encounter);

        log.info(() -> "Participant '%s' added with ID: %d".formatted(input.name(), pe.id));
        return mapper.toParticipant(pe);
    }
}
