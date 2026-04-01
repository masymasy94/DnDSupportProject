package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantAdvanceTurnRepository;
import com.dndplatform.combat.domain.repository.ParticipantFindByEncounterRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class TurnAdvanceServiceImpl implements TurnAdvanceService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository findRepository;
    private final ParticipantAdvanceTurnRepository advanceTurnRepository;
    private final ParticipantFindByEncounterRepository participantFindRepository;

    @Inject
    public TurnAdvanceServiceImpl(EncounterFindByIdRepository findRepository,
                                  ParticipantAdvanceTurnRepository advanceTurnRepository,
                                  ParticipantFindByEncounterRepository participantFindRepository) {
        this.findRepository = findRepository;
        this.advanceTurnRepository = advanceTurnRepository;
        this.participantFindRepository = participantFindRepository;
    }

    @Override
    public List<EncounterParticipant> advance(Long encounterId, Long userId) {
        log.info(() -> "Advancing turn for encounter %d by user %d".formatted(encounterId, userId));

        Encounter encounter = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        if (!encounter.createdByUserId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can advance turns in this encounter");
        }

        if (encounter.status() != EncounterStatus.ACTIVE) {
            throw new IllegalStateException("Turns can only be advanced for encounters in ACTIVE status");
        }

        advanceTurnRepository.advanceTurn(encounterId);

        List<EncounterParticipant> participants = participantFindRepository.findByEncounterId(encounterId);
        log.info(() -> "Turn advanced for encounter %d".formatted(encounterId));
        return participants;
    }
}
