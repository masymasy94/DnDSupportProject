package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.chat.adapter.outbound.jpa.mapper.MessageMapper;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.repository.MessageCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class MessageCreateRepositoryImpl implements MessageCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MessagePanacheRepository panacheRepository;
    private final ConversationPanacheRepository conversationPanacheRepository;
    private final MessageMapper mapper;

    @Inject
    public MessageCreateRepositoryImpl(MessagePanacheRepository panacheRepository,
                                        ConversationPanacheRepository conversationPanacheRepository,
                                        MessageMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.conversationPanacheRepository = conversationPanacheRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Message create(Message message) {
        log.info(() -> "Creating message: conversationId=%d, senderId=%d"
                .formatted(message.conversationId(), message.senderId()));

        var conversationRef = conversationPanacheRepository.findById(message.conversationId());

        var entity = new MessageEntity();
        entity.conversation = conversationRef;
        entity.senderId = message.senderId();
        entity.content = message.content();
        entity.messageType = message.messageType().name();
        entity.createdAt = message.createdAt();

        panacheRepository.persist(entity);

        // Update conversation's updated_at timestamp
        conversationPanacheRepository.update("updatedAt = ?1 where id = ?2",
                LocalDateTime.now(), message.conversationId());

        return mapper.apply(entity);
    }
}
