package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.domain.repository.ParticipantAdvanceTurnRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantAdvanceTurnRepositoryJpa implements ParticipantAdvanceTurnRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantPanacheRepository participantRepository;

    @Inject
    public ParticipantAdvanceTurnRepositoryJpa(ParticipantPanacheRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    @Transactional
    public void advanceTurn(Long encounterId) {
        log.info(() -> "Advancing turn for encounter %d".formatted(encounterId));

        var participants = participantRepository.findByEncounterIdOrderBySortOrder(encounterId);

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
            participantRepository.persist(participants.get(activeIndex));
        }

        int nextIndex = (activeIndex + 1) % participants.size();
        participants.get(nextIndex).isActive = true;
        participantRepository.persist(participants.get(nextIndex));

        log.info(() -> "Turn advanced for encounter %d".formatted(encounterId));
    }
}
