package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessagePanacheRepository implements PanacheRepository<MessageEntity> {

    public PanacheQuery<MessageEntity> queryByConversationId(Long conversationId) {
        return find("conversation.id = ?1 AND deletedAt IS NULL",
                Sort.by("createdAt").descending(),
                conversationId);
    }
}
