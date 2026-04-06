package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.domain.repository.ParticipantUpdateLastReadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantUpdateLastReadRepositoryImpl implements ParticipantUpdateLastReadRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantPanacheRepository participantPanacheRepository;

    @Inject
    public ParticipantUpdateLastReadRepositoryImpl(ParticipantPanacheRepository participantPanacheRepository) {
        this.participantPanacheRepository = participantPanacheRepository;
    }

    @Override
    @Transactional
    public void updateLastReadAt(Long conversationId, Long userId) {
        log.info(() -> "Updating last read: conversationId=%d, userId=%d"
                .formatted(conversationId, userId));

        participantPanacheRepository.update("lastReadAt = ?1 where conversation.id = ?2 and userId = ?3",
                LocalDateTime.now(), conversationId, userId);
    }
}
