package com.dndplatform.chat.adapter.inbound.websocket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatWebSocketMessage")
class ChatWebSocketMessageTest {

    @Test
    @DisplayName("newMessage should build message with NEW_MESSAGE type")
    void newMessageShouldBuildMessageWithCorrectFields() {
        Long conversationId = 10L;
        Long messageId = 5L;
        Long senderId = 2L;
        String content = "Hello, adventurer!";
        String messageType = "TEXT";
        LocalDateTime timestamp = LocalDateTime.of(2026, 1, 1, 12, 0);

        ChatWebSocketMessage msg = ChatWebSocketMessage.newMessage(
                conversationId, messageId, senderId, content, messageType, timestamp);

        assertThat(msg.type()).isEqualTo("NEW_MESSAGE");
        assertThat(msg.conversationId()).isEqualTo(conversationId);
        assertThat(msg.messageId()).isEqualTo(messageId);
        assertThat(msg.senderId()).isEqualTo(senderId);
        assertThat(msg.content()).isEqualTo(content);
        assertThat(msg.messageType()).isEqualTo(messageType);
        assertThat(msg.timestamp()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("error should build message with ERROR type and null ids")
    void errorShouldBuildMessageWithErrorTypeAndNullIds() {
        String errorContent = "Something went wrong";

        ChatWebSocketMessage msg = ChatWebSocketMessage.error(errorContent);

        assertThat(msg.type()).isEqualTo("ERROR");
        assertThat(msg.content()).isEqualTo(errorContent);
        assertThat(msg.conversationId()).isNull();
        assertThat(msg.messageId()).isNull();
        assertThat(msg.senderId()).isNull();
        assertThat(msg.messageType()).isNull();
        assertThat(msg.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("connected should build CONNECTED message with success content")
    void connectedShouldBuildConnectedMessage() {
        ChatWebSocketMessage msg = ChatWebSocketMessage.connected();

        assertThat(msg.type()).isEqualTo("CONNECTED");
        assertThat(msg.content()).isEqualTo("Successfully connected");
        assertThat(msg.conversationId()).isNull();
        assertThat(msg.messageId()).isNull();
        assertThat(msg.senderId()).isNull();
    }

    @Test
    @DisplayName("record constructor should store all fields")
    void recordConstructorShouldStoreAllFields() {
        LocalDateTime now = LocalDateTime.now();
        ChatWebSocketMessage msg = new ChatWebSocketMessage(
                "CUSTOM", 1L, 2L, 3L, "content", "DICE_ROLL", now);

        assertThat(msg.type()).isEqualTo("CUSTOM");
        assertThat(msg.conversationId()).isEqualTo(1L);
        assertThat(msg.messageId()).isEqualTo(2L);
        assertThat(msg.senderId()).isEqualTo(3L);
        assertThat(msg.content()).isEqualTo("content");
        assertThat(msg.messageType()).isEqualTo("DICE_ROLL");
        assertThat(msg.timestamp()).isEqualTo(now);
    }
}
