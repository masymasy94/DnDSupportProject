package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationMapper;
import com.dndplatform.chat.domain.model.Conversation;
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

        return panacheRepository.findDirectConversation(userId1, userId2).map(mapper);
    }
}
