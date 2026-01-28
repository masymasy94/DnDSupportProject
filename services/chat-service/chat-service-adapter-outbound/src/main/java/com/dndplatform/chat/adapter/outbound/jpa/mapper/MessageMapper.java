package com.dndplatform.chat.adapter.outbound.jpa.mapper;

import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper(componentModel = "cdi")
public interface MessageMapper extends Function<MessageEntity, Message> {

    @Override
    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(target = "messageType", expression = "java(mapMessageType(entity.getMessageType()))")
    Message apply(MessageEntity entity);

    default MessageType mapMessageType(String type) {
        return type != null ? MessageType.valueOf(type) : MessageType.TEXT;
    }
}
