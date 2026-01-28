package com.dndplatform.chat.adapter.outbound.jpa.mapper;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper(componentModel = "cdi", uses = ConversationParticipantMapper.class)
public interface ConversationMapper extends Function<ConversationEntity, Conversation> {

    @Override
    @Mapping(target = "type", expression = "java(mapType(entity.getType()))")
    Conversation apply(ConversationEntity entity);

    default ConversationType mapType(String type) {
        return type != null ? ConversationType.valueOf(type) : ConversationType.DIRECT;
    }
}
