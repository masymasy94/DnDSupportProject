package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.domain.repository.ParticipantSetInitiativeRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Comparator;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantSetInitiativeRepositoryJpa implements ParticipantSetInitiativeRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void setInitiatives(Long encounterId, Map<Long, Integer> initiativeMap) {
        log.info(() -> "Setting initiatives for encounter %d".formatted(encounterId));

        EncounterEntity encounter = EncounterEntity.findById(encounterId);
        if (encounter == null) {
            throw new NotFoundException("Encounter not found with ID: %d".formatted(encounterId));
        }

        for (EncounterParticipantEntity pe : encounter.participants) {
            Integer initiative = initiativeMap.get(pe.id);
            if (initiative != null) {
                pe.initiative = initiative;
            }
        }

        encounter.participants.sort(Comparator.comparingInt((EncounterParticipantEntity pe) -> pe.initiative).reversed());

        for (int i = 0; i < encounter.participants.size(); i++) {
            EncounterParticipantEntity pe = encounter.participants.get(i);
            pe.sortOrder = i;
            pe.isActive = (i == 0);
        }

        encounter.persist();
        log.info(() -> "Initiatives set for encounter %d".formatted(encounterId));
    }
}
