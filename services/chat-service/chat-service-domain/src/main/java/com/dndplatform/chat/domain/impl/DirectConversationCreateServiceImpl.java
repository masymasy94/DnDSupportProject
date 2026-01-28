package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.DirectConversationCreateService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationBuilder;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.ConversationParticipantBuilder;
import com.dndplatform.chat.domain.model.ConversationType;
import com.dndplatform.chat.domain.model.ParticipantRole;
import com.dndplatform.chat.domain.repository.ConversationCreateRepository;
import com.dndplatform.chat.domain.repository.ConversationFindDirectRepository;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DirectConversationCreateServiceImpl implements DirectConversationCreateService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConversationCreateRepository conversationCreateRepository;
    private final ConversationFindDirectRepository findDirectRepository;

    @Inject
    public DirectConversationCreateServiceImpl(ConversationCreateRepository conversationCreateRepository,
                                               ConversationFindDirectRepository findDirectRepository) {
        this.conversationCreateRepository = conversationCreateRepository;
        this.findDirectRepository = findDirectRepository;
    }

    @Override
    public Conversation create(Long createdBy, List<Long> participantIds) {
        Long otherUserId = participantIds.getFirst();
        log.info(() -> "Creating direct conversation: createdBy=%d, otherUser=%d".formatted(createdBy, otherUserId));

        return findDirectRepository.findDirectConversation(createdBy, otherUserId)
                .map(conversation -> {
                    log.info(() -> "Direct conversation already exists: id=%d".formatted(conversation.id()));
                    return conversation;
                })
                .orElseGet(() -> {
                    var conversation = getConversation(createdBy, getParticipants(createdBy, otherUserId));
                    return conversationCreateRepository.create(conversation);
                });
    }


    private List<ConversationParticipant> getParticipants(Long createdBy, Long otherUserId) {
        return List.of(
                createParticipant(createdBy, ParticipantRole.MEMBER),
                createParticipant(otherUserId, ParticipantRole.MEMBER)
        );
    }


    private static Conversation getConversation(Long createdBy, List<ConversationParticipant> participants) {
        return ConversationBuilder.builder()
                .withType(ConversationType.DIRECT)
                .withCreatedBy(createdBy)
                .withCreatedAt(LocalDateTime.now())
                .withParticipants(participants)
                .build();
    }

    private ConversationParticipant createParticipant(Long userId, ParticipantRole role) {
        return ConversationParticipantBuilder.builder()
                .withUserId(userId)
                .withRole(role)
                .withJoinedAt(LocalDateTime.now())
                .build();
    }
}
