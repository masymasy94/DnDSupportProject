package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationParticipantMapper;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.repository.ParticipantFindByConversationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantFindByConversationRepositoryImpl implements ParticipantFindByConversationRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantPanacheRepository panacheRepository;
    private final ConversationParticipantMapper mapper;

    @Inject
    public ParticipantFindByConversationRepositoryImpl(ParticipantPanacheRepository panacheRepository,
                                                        ConversationParticipantMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ConversationParticipant> findByConversationId(Long conversationId) {
        log.info(() -> "Finding participants: conversationId=%d".formatted(conversationId));

        return panacheRepository.find("conversation.id = ?1 AND leftAt IS NULL", conversationId)
                .list()
                .stream()
                .map(mapper)
                .toList();
    }
}
