package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.chat.adapter.outbound.jpa.mapper.ConversationMapper;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.repository.ConversationFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ConversationFindByIdRepositoryImpl implements ConversationFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConversationPanacheRepository panacheRepository;
    private final ConversationMapper mapper;

    @Inject
    public ConversationFindByIdRepositoryImpl(ConversationPanacheRepository panacheRepository,
                                              ConversationMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Conversation> findById(Long id) {
        log.info(() -> "Finding conversation by id: %d".formatted(id));

        return panacheRepository.find("""
                SELECT c FROM ConversationEntity c
                LEFT JOIN FETCH c.participants
                WHERE c.id = ?1
                """, id)
                .firstResultOptional()
                .map(mapper);
    }
}
