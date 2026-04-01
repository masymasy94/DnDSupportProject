package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.domain.repository.ParticipantAdvanceTurnRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantAdvanceTurnRepositoryJpa implements ParticipantAdvanceTurnRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void advanceTurn(Long encounterId) {
        log.info(() -> "Advancing turn for encounter %d".formatted(encounterId));

        List<EncounterParticipantEntity> participants = EncounterParticipantEntity
                .find("encounter.id = ?1 order by sortOrder asc", encounterId)
                .list();

        if (participants.isEmpty()) return;

        int activeIndex = -1;
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).isActive) {
                activeIndex = i;
                break;
            }
        }

        if (activeIndex >= 0) {
            participants.get(activeIndex).isActive = false;
            participants.get(activeIndex).persist();
        }

        int nextIndex = (activeIndex + 1) % participants.size();
        participants.get(nextIndex).isActive = true;
        participants.get(nextIndex).persist();

        log.info(() -> "Turn advanced for encounter %d".formatted(encounterId));
    }
}
