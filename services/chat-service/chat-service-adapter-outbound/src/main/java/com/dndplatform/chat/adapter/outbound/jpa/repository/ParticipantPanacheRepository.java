package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ParticipantPanacheRepository implements PanacheRepository<ConversationParticipantEntity> {

    public boolean existsByConversationIdAndUserId(Long conversationId, Long userId) {
        return count("conversation.id = ?1 AND userId = ?2 AND leftAt IS NULL",
                conversationId, userId) > 0;
    }

    public List<ConversationParticipantEntity> findActiveByConversationId(Long conversationId) {
        return find("conversation.id = ?1 AND leftAt IS NULL", conversationId).list();
    }
}
