package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.domain.repository.ParticipantUpdateLastReadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantUpdateLastReadRepositoryImpl implements ParticipantUpdateLastReadRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EntityManager entityManager;

    @Inject
    public ParticipantUpdateLastReadRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void updateLastReadAt(Long conversationId, Long userId) {
        log.info(() -> "Updating last read: conversationId=%d, userId=%d"
                .formatted(conversationId, userId));

        entityManager.createQuery("""
                UPDATE ConversationParticipantEntity p
                SET p.lastReadAt = :now
                WHERE p.conversation.id = :conversationId AND p.userId = :userId
                """)
                .setParameter("now", LocalDateTime.now())
                .setParameter("conversationId", conversationId)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
