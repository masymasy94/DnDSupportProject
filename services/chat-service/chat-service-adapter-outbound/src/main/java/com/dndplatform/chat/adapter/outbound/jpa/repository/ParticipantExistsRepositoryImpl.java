package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.domain.repository.ParticipantExistsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantExistsRepositoryImpl implements ParticipantExistsRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantPanacheRepository panacheRepository;

    @Inject
    public ParticipantExistsRepositoryImpl(ParticipantPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public boolean existsByConversationIdAndUserId(Long conversationId, Long userId) {
        log.info(() -> "Checking if user is participant: conversationId=%d, userId=%d"
                .formatted(conversationId, userId));

        return panacheRepository.count("conversation.id = ?1 AND userId = ?2 AND leftAt IS NULL",
                conversationId, userId) > 0;
    }
}
