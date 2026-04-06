package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterCreate;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.domain.repository.EncounterCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class EncounterCreateRepositoryJpa implements EncounterCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterEntityMapper mapper;
    private final EncounterPanacheRepository panacheRepository;

    @Inject
    public EncounterCreateRepositoryJpa(EncounterEntityMapper mapper,
                                        EncounterPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public Encounter save(EncounterCreate input) {
        log.info(() -> "Saving encounter: %s".formatted(input.name()));

        EncounterEntity entity = new EncounterEntity();
        entity.campaignId = input.campaignId();
        entity.createdByUserId = input.createdByUserId();
        entity.name = input.name();
        entity.description = input.description();
        entity.status = "DRAFT";
        entity.partyLevel = input.partyLevel();
        entity.partySize = input.partySize();
        entity.createdAt = LocalDateTime.now();

        if (input.participants() != null) {
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

        log.info(() -> "Encounter saved with ID: %d".formatted(entity.id));
        return mapper.toEncounter(entity);
    }
}
