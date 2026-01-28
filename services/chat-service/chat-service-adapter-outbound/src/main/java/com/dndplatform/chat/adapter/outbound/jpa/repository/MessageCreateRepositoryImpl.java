package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.chat.adapter.outbound.jpa.mapper.MessageMapper;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.repository.MessageCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class MessageCreateRepositoryImpl implements MessageCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MessagePanacheRepository panacheRepository;
    private final MessageMapper mapper;
    private final EntityManager entityManager;

    @Inject
    public MessageCreateRepositoryImpl(MessagePanacheRepository panacheRepository,
                                        MessageMapper mapper,
                                        EntityManager entityManager) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Message create(Message message) {
        log.info(() -> "Creating message: conversationId=%d, senderId=%d"
                .formatted(message.conversationId(), message.senderId()));

        var conversationRef = entityManager.getReference(ConversationEntity.class, message.conversationId());

        var entity = new MessageEntity();
        entity.conversation = conversationRef;
        entity.senderId = message.senderId();
        entity.content = message.content();
        entity.messageType = message.messageType().name();
        entity.createdAt = message.createdAt();

        panacheRepository.persist(entity);

        // Update conversation's updated_at timestamp
        entityManager.createQuery("""
                UPDATE ConversationEntity c SET c.updatedAt = :now WHERE c.id = :id
                """)
                .setParameter("now", LocalDateTime.now())
                .setParameter("id", message.conversationId())
                .executeUpdate();

        return mapper.apply(entity);
    }
}
