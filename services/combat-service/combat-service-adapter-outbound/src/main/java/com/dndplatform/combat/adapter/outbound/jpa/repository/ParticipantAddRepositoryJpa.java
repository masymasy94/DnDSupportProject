package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
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

    @Inject
    public ParticipantAddRepositoryJpa(EncounterEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public EncounterParticipant add(Long encounterId, ParticipantCreate input) {
        log.info(() -> "Adding participant '%s' to encounter %d".formatted(input.name(), encounterId));

        EncounterEntity encounter = EncounterEntity.findById(encounterId);
        if (encounter == null) {
            throw new NotFoundException("Encounter not found with ID: %d".formatted(encounterId));
        }

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
        encounter.persist();

        log.info(() -> "Participant '%s' added with ID: %d".formatted(input.name(), pe.id));
        return mapper.toParticipant(pe);
    }
}
