package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationMapper;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationType;
import com.dndplatform.chat.domain.repository.ConversationFindDirectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ConversationFindDirectRepositoryImpl implements ConversationFindDirectRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConversationPanacheRepository panacheRepository;
    private final ConversationMapper mapper;

    @Inject
    public ConversationFindDirectRepositoryImpl(ConversationPanacheRepository panacheRepository,
                                                 ConversationMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Conversation> findDirectConversation(Long userId1, Long userId2) {
        log.info(() -> "Finding direct conversation between users: %d and %d".formatted(userId1, userId2));

        return panacheRepository.find("""
                SELECT c FROM ConversationEntity c
                LEFT JOIN FETCH c.participants
                WHERE c.type = ?1
                AND EXISTS (SELECT 1 FROM ConversationParticipantEntity p1 WHERE p1.conversation = c AND p1.userId = ?2 AND p1.leftAt IS NULL)
                AND EXISTS (SELECT 1 FROM ConversationParticipantEntity p2 WHERE p2.conversation = c AND p2.userId = ?3 AND p2.leftAt IS NULL)
                """, ConversationType.DIRECT.name(), userId1, userId2)
                .firstResultOptional()
                .map(mapper);
    }
}
