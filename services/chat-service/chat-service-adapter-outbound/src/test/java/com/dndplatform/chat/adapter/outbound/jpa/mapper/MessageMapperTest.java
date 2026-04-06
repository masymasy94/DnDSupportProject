package com.dndplatform.chat.adapter.outbound.jpa.mapper;

import com.dndplatform.chat.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.chat.adapter.outbound.jpa.entity.MessageEntity;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MessageMapperTest {

    private final MessageMapper sut = new MessageMapperImpl();

    @Test
    void shouldMapEntityToDomain() {
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.id = 20L;

        MessageEntity entity = new MessageEntity();
        entity.id = 1L;
        entity.conversation = conversationEntity;
        entity.senderId = 5L;
        entity.content = "Hello World";
        entity.messageType = "TEXT";
        entity.createdAt = LocalDateTime.of(2024, 1, 10, 9, 30);
        entity.editedAt = LocalDateTime.of(2024, 1, 10, 9, 45);
        entity.deletedAt = null;

        Message result = sut.apply(entity);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.conversationId()).isEqualTo(20L);
        assertThat(result.senderId()).isEqualTo(5L);
        assertThat(result.content()).isEqualTo("Hello World");
        assertThat(result.messageType()).isEqualTo(MessageType.TEXT);
        assertThat(result.createdAt()).isEqualTo(entity.createdAt);
        assertThat(result.editedAt()).isEqualTo(entity.editedAt);
        assertThat(result.deletedAt()).isNull();
    }

    @Test
    void shouldMapSystemMessageType() {
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.id = 30L;

        MessageEntity entity = new MessageEntity();
        entity.id = 2L;
        entity.conversation = conversationEntity;
        entity.senderId = 1L;
        entity.content = "User joined";
        entity.messageType = "SYSTEM";

        Message result = sut.apply(entity);

        assertThat(result.messageType()).isEqualTo(MessageType.SYSTEM);
    }

    @Test
    void shouldMapImageMessageType() {
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.id = 30L;

        MessageEntity entity = new MessageEntity();
        entity.id = 3L;
        entity.conversation = conversationEntity;
        entity.senderId = 1L;
        entity.content = "image.png";
        entity.messageType = "IMAGE";

        Message result = sut.apply(entity);

        assertThat(result.messageType()).isEqualTo(MessageType.IMAGE);
    }

    @Test
    void shouldMapDiceRollMessageType() {
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.id = 30L;

        MessageEntity entity = new MessageEntity();
        entity.id = 4L;
        entity.conversation = conversationEntity;
        entity.senderId = 1L;
        entity.content = "1d20=17";
        entity.messageType = "DICE_ROLL";

        Message result = sut.apply(entity);

        assertThat(result.messageType()).isEqualTo(MessageType.DICE_ROLL);
    }

    @Test
    void shouldDefaultToTextWhenMessageTypeIsNull() {
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.id = 40L;

        MessageEntity entity = new MessageEntity();
        entity.id = 5L;
        entity.conversation = conversationEntity;
        entity.senderId = 2L;
        entity.content = "some content";
        entity.messageType = null;

        Message result = sut.apply(entity);

        assertThat(result.messageType()).isEqualTo(MessageType.TEXT);
    }

    @Test
    void shouldMapNullConversationToNullConversationId() {
        MessageEntity entity = new MessageEntity();
        entity.id = 6L;
        entity.conversation = null;
        entity.senderId = 3L;
        entity.content = "test";
        entity.messageType = "TEXT";

        Message result = sut.apply(entity);

        assertThat(result.conversationId()).isNull();
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        Message result = sut.apply(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldDefaultToTextWhenMapMessageTypeCalledWithNull() {
        MessageType type = sut.mapMessageType(null);

        assertThat(type).isEqualTo(MessageType.TEXT);
    }

    @Test
    void shouldMapMessageTypeWhenMapMessageTypeCalledWithValue() {
        MessageType type = sut.mapMessageType("SYSTEM");

        assertThat(type).isEqualTo(MessageType.SYSTEM);
    }
}
