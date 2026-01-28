package com.dndplatform.chat.adapter.outbound.jpa.mapper;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationParticipantEntity;
import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.model.ParticipantRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper(componentModel = "cdi")
public interface ConversationParticipantMapper extends Function<ConversationParticipantEntity, ConversationParticipant> {

    @Override
    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(target = "role", expression = "java(mapRole(entity.getRole()))")
    ConversationParticipant apply(ConversationParticipantEntity entity);

    default ParticipantRole mapRole(String role) {
        return role != null ? ParticipantRole.valueOf(role) : ParticipantRole.MEMBER;
    }
}
