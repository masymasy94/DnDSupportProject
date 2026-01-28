package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.ConversationFindByIdService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.repository.ConversationFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ConversationFindByIdServiceImpl implements ConversationFindByIdService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConversationFindByIdRepository repository;

    @Inject
    public ConversationFindByIdServiceImpl(ConversationFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Conversation> findById(Long id, Long userId) {
        log.info(() -> "Finding conversation: id=%d, userId=%d".formatted(id, userId));

        var conversation = repository.findById(id);
        return conversation.filter(c -> isUserParticipant(c, userId));
    }

    private boolean isUserParticipant(Conversation conversation, Long userId) {
        return conversation.participants()!= null &&
                conversation.participants().stream()
                .anyMatch(p -> p.userId().equals(userId) && p.leftAt() == null);
    }
}
