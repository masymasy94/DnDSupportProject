package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationMessageEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ConversationMessagePanacheRepository implements PanacheRepository<ConversationMessageEntity> {
    public List<ConversationMessageEntity> findByConversationId(Long conversationId) {
        return find("conversation.id", Sort.ascending("createdAt"), conversationId).list();
    }
}
