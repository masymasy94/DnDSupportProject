package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.ConversationFindByUserService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.repository.ConversationFindByUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ConversationFindByUserServiceImpl implements ConversationFindByUserService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConversationFindByUserRepository repository;

    @Inject
    public ConversationFindByUserServiceImpl(ConversationFindByUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Conversation> findByUserId(Long userId) {
        log.info(() -> "Finding conversations for user: %d".formatted(userId));
        return repository.findByUserId(userId);
    }
}
