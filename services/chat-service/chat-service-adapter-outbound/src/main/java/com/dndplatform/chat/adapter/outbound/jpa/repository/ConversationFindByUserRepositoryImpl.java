package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationMapper;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.repository.ConversationFindByUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ConversationFindByUserRepositoryImpl implements ConversationFindByUserRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConversationPanacheRepository panacheRepository;
    private final ConversationMapper mapper;

    @Inject
    public ConversationFindByUserRepositoryImpl(ConversationPanacheRepository panacheRepository,
                                                 ConversationMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Conversation> findByUserId(Long userId) {
        log.info(() -> "Finding conversations for user: %d".formatted(userId));

        return panacheRepository.find("""
                SELECT DISTINCT c FROM ConversationEntity c
                JOIN FETCH c.participants p
                WHERE p.userId = ?1 AND p.leftAt IS NULL
                ORDER BY c.updatedAt DESC NULLS LAST, c.createdAt DESC
                """, userId)
                .list()
                .stream()
                .map(mapper)
                .toList();
    }
}
