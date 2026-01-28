package com.dndplatform.chat.adapter.inbound.websocket;

public record ChatWebSocketIncomingMessage(
        String type,
        Long conversationId,
        String content,
        String messageType
) {}
