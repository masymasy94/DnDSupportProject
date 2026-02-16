package com.dndplatform.chat.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonbPropertyOrder({"id", "conversationId", "senderId", "content", "messageType", "createdAt", "editedAt"})
@Schema(description = "Chat message information")
public record MessageViewModel(
        @Schema(description = "Message ID", example = "1")
        Long id,

        @Schema(description = "Conversation ID", example = "1")
        Long conversationId,

        @Schema(description = "Sender user ID", example = "1")
        Long senderId,

        @Schema(description = "Message content", example = "Hello, everyone!")
        String content,

        @Schema(description = "Message type", example = "TEXT")
        String messageType,

        @Schema(description = "When the message was sent")
        LocalDateTime createdAt,

        @Schema(description = "When the message was edited (if applicable)")
        LocalDateTime editedAt
) {}
