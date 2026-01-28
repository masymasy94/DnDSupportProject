package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.ConversationUpdateReadByIdService;
import com.dndplatform.chat.domain.repository.ParticipantExistsRepository;
import com.dndplatform.chat.domain.repository.ParticipantUpdateLastReadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class ConversationUpdateReadByIdServiceImpl implements ConversationUpdateReadByIdService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantUpdateLastReadRepository participantUpdateLastReadRepository;
    private final ParticipantExistsRepository participantExistsRepository;

    @Inject
    public ConversationUpdateReadByIdServiceImpl(ParticipantUpdateLastReadRepository participantUpdateLastReadRepository,
                                                 ParticipantExistsRepository participantExistsRepository) {
        this.participantUpdateLastReadRepository = participantUpdateLastReadRepository;
        this.participantExistsRepository = participantExistsRepository;
    }

    @Override
    public void updateReadById(Long conversationId, Long userId) {
        log.info(() -> "Marking conversation as read: conversationId=%d, userId=%d"
                .formatted(conversationId, userId));

        if (!participantExistsRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new IllegalArgumentException("User is not a participant in this conversation");
        }

        participantUpdateLastReadRepository.updateLastReadAt(conversationId, userId);
    }
}
