package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterUpdate;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.domain.repository.EncounterUpdateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class EncounterUpdateRepositoryJpa implements EncounterUpdateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterEntityMapper mapper;
    private final EncounterPanacheRepository panacheRepository;

    @Inject
    public EncounterUpdateRepositoryJpa(EncounterEntityMapper mapper,
                                        EncounterPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public Encounter update(EncounterUpdate input) {
        log.info(() -> "Updating encounter: %d".formatted(input.id()));

        var entity = panacheRepository.findByIdOptional(input.id())
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(input.id())));

        if (input.name() != null) entity.name = input.name();
        if (input.description() != null) entity.description = input.description();
        if (input.partyLevel() != null) entity.partyLevel = input.partyLevel();
        if (input.partySize() != null) entity.partySize = input.partySize();
        entity.updatedAt = LocalDateTime.now();

        if (input.participants() != null) {
            entity.participants.clear();
            panacheRepository.flush();

            for (ParticipantCreate pc : input.participants()) {
                EncounterParticipantEntity pe = new EncounterParticipantEntity();
                pe.encounter = entity;
                pe.name = pc.name();
                pe.participantType = pc.type().name();
                pe.currentHp = pc.maxHp();
                pe.maxHp = pc.maxHp();
                pe.armorClass = pc.armorClass();
                pe.monsterId = pc.monsterId();
                pe.sourceJson = pc.sourceJson();
                entity.participants.add(pe);
            }
        }

        panacheRepository.persist(entity);

        log.info(() -> "Encounter %d updated successfully".formatted(input.id()));
        return mapper.toEncounter(entity);
    }
}
