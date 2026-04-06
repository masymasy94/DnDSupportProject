package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.domain.model.ConversationType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.List;

@ApplicationScoped
public class ConversationPanacheRepository implements PanacheRepository<ConversationEntity> {

    public Optional<ConversationEntity> findByIdWithParticipants(Long id) {
        return find("""
                SELECT c FROM ConversationEntity c
                LEFT JOIN FETCH c.participants
                WHERE c.id = ?1
                """, id).firstResultOptional();
    }

    public List<ConversationEntity> findActiveByUserId(Long userId) {
        return find("""
                SELECT DISTINCT c FROM ConversationEntity c
                JOIN FETCH c.participants p
                WHERE p.userId = ?1 AND p.leftAt IS NULL
                ORDER BY c.updatedAt DESC NULLS LAST, c.createdAt DESC
                """, userId).list();
    }

    public Optional<ConversationEntity> findDirectConversation(Long userId1, Long userId2) {
        return find("""
                SELECT c FROM ConversationEntity c
                LEFT JOIN FETCH c.participants
                WHERE c.type = ?1
                AND EXISTS (SELECT 1 FROM ConversationParticipantEntity p1 WHERE p1.conversation = c AND p1.userId = ?2 AND p1.leftAt IS NULL)
                AND EXISTS (SELECT 1 FROM ConversationParticipantEntity p2 WHERE p2.conversation = c AND p2.userId = ?3 AND p2.leftAt IS NULL)
                """, ConversationType.DIRECT.name(), userId1, userId2).firstResultOptional();
    }
}
