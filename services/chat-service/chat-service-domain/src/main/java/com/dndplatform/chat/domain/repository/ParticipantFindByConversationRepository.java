package com.dndplatform.chat.domain.repository;

import com.dndplatform.chat.domain.model.ConversationParticipant;

import java.util.List;

public interface ParticipantFindByConversationRepository {
    List<ConversationParticipant> findByConversationId(Long conversationId);
}
