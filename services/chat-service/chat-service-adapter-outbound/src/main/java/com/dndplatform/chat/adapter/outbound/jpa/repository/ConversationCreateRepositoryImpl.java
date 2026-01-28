package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationMapper;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.repository.ConversationCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ConversationCreateRepositoryImpl implements ConversationCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConversationPanacheRepository panacheRepository;
    private final ConversationMapper mapper;

    @Inject
    public ConversationCreateRepositoryImpl(ConversationPanacheRepository panacheRepository,
                                            ConversationMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Conversation create(Conversation conversation) {
        log.info(() -> "Creating conversation: type=%s".formatted(conversation.type()));

        var entity = new ConversationEntity();
        entity.name = conversation.name();
        entity.type = conversation.type().name();
        entity.createdBy = conversation.createdBy();
        entity.createdAt = conversation.createdAt();

        panacheRepository.persist(entity);

        if (conversation.participants() != null) {
            for (ConversationParticipant participant : conversation.participants()) {
                var participantEntity = new ConversationParticipantEntity();
                participantEntity.conversation = entity;
                participantEntity.userId = participant.userId();
                participantEntity.role = participant.role().name();
                participantEntity.joinedAt = participant.joinedAt();
                entity.participants.add(participantEntity);
            }
        }

        panacheRepository.flush();
        return mapper.apply(entity);
    }
}
