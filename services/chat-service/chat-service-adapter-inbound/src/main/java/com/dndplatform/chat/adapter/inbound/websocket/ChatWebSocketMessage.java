package com.dndplatform.chat.adapter.inbound.websocket;

import java.time.LocalDateTime;

public record ChatWebSocketMessage(
        String type,
        Long conversationId,
        Long messageId,
        Long senderId,
        String content,
        String messageType,
        LocalDateTime timestamp
) {
    public static ChatWebSocketMessage newMessage(Long conversationId, Long messageId, Long senderId,
                                                   String content, String messageType, LocalDateTime timestamp) {
        return new ChatWebSocketMessage("NEW_MESSAGE", conversationId, messageId, senderId, content, messageType, timestamp);
    }

    public static ChatWebSocketMessage error(String content) {
        return new ChatWebSocketMessage("ERROR", null, null, null, content, null, LocalDateTime.now());
    }

    public static ChatWebSocketMessage connected() {
        return new ChatWebSocketMessage("CONNECTED", null, null, null, "Successfully connected", null, LocalDateTime.now());
    }
}
