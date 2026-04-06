package com.dndplatform.chat.adapter.inbound.message.mapper;

import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageBuilder;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.chat.view.model.vm.MessageViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class MessageViewModelMapperTest {

    private MessageViewModelMapperImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MessageViewModelMapperImpl();
    }

    @Test
    void shouldMapMessageToViewModel(@Random Message message) {
        MessageViewModel result = sut.apply(message);

        assertThat(result.id()).isEqualTo(message.id());
        assertThat(result.conversationId()).isEqualTo(message.conversationId());
        assertThat(result.senderId()).isEqualTo(message.senderId());
        assertThat(result.content()).isEqualTo(message.content());
        assertThat(result.messageType()).isEqualTo(message.messageType().name());
        assertThat(result.createdAt()).isEqualTo(message.createdAt());
        assertThat(result.editedAt()).isEqualTo(message.editedAt());
    }

    @Test
    void shouldMapNullMessageTypeToNull() {
        Message message = MessageBuilder.builder()
                .withId(1L)
                .withConversationId(10L)
                .withSenderId(5L)
                .withContent("Hello")
                .withMessageType(null)
                .withCreatedAt(LocalDateTime.now())
                .withEditedAt(null)
                .withDeletedAt(null)
                .build();

        MessageViewModel result = sut.apply(message);

        assertThat(result.messageType()).isNull();
        assertThat(result.content()).isEqualTo("Hello");
    }

    @Test
    void shouldMapTextMessageTypeCorrectly() {
        Message message = MessageBuilder.builder()
                .withId(1L)
                .withConversationId(10L)
                .withSenderId(5L)
                .withContent("Hello everyone")
                .withMessageType(MessageType.TEXT)
                .withCreatedAt(LocalDateTime.now())
                .withEditedAt(null)
                .withDeletedAt(null)
                .build();

        MessageViewModel result = sut.apply(message);

        assertThat(result.messageType()).isEqualTo("TEXT");
    }

    @Test
    void shouldMapDiceRollMessageTypeCorrectly() {
        Message message = MessageBuilder.builder()
                .withId(2L)
                .withConversationId(10L)
                .withSenderId(5L)
                .withContent("Rolled 4d6: [3,4,5,6]")
                .withMessageType(MessageType.DICE_ROLL)
                .withCreatedAt(LocalDateTime.now())
                .withEditedAt(null)
                .withDeletedAt(null)
                .build();

        MessageViewModel result = sut.apply(message);

        assertThat(result.messageType()).isEqualTo("DICE_ROLL");
    }

    @Test
    void shouldReturnNullWhenMessageIsNull() {
        MessageViewModel result = sut.apply(null);

        assertThat(result).isNull();
    }
}
