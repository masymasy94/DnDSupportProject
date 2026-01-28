package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.GroupConversationCreateService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationBuilder;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.ConversationParticipantBuilder;
import com.dndplatform.chat.domain.model.ConversationType;
import com.dndplatform.chat.domain.model.ParticipantRole;
import com.dndplatform.chat.domain.repository.ConversationCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class GroupConversationCreateServiceImpl implements GroupConversationCreateService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConversationCreateRepository conversationCreateRepository;

    @Inject
    public GroupConversationCreateServiceImpl(ConversationCreateRepository conversationCreateRepository) {
        this.conversationCreateRepository = conversationCreateRepository;
    }

    @Override
    public Conversation create(String name, Long createdBy, List<Long> participantIds) {
        log.info(() -> "Creating group conversation: name=%s, createdBy=%d, participants=%s"
                .formatted(name, createdBy, participantIds));

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Group conversation requires a name");
        }

        List<ConversationParticipant> participants = new ArrayList<>();
        participants.add(createParticipant(createdBy, ParticipantRole.ADMIN));

        if (participantIds != null) {
            for (Long participantId : participantIds) {
                if (!participantId.equals(createdBy)) {
                    participants.add(createParticipant(participantId, ParticipantRole.MEMBER));
                }
            }
        }

        var conversation = ConversationBuilder.builder()
                .withName(name)
                .withType(ConversationType.GROUP)
                .withCreatedBy(createdBy)
                .withCreatedAt(LocalDateTime.now())
                .withParticipants(participants)
                .build();

        return conversationCreateRepository.create(conversation);
    }

    private ConversationParticipant createParticipant(Long userId, ParticipantRole role) {
        return ConversationParticipantBuilder.builder()
                .withUserId(userId)
                .withRole(role)
                .withJoinedAt(LocalDateTime.now())
                .build();
    }
}
