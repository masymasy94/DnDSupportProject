package com.dndplatform.chat.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonbPropertyOrder({"userId", "role", "joinedAt", "lastReadAt"})
@Schema(description = "Conversation participant information")
public record ParticipantViewModel(
        @Schema(description = "User ID", example = "1")
        Long userId,

        @Schema(description = "Participant role", example = "MEMBER")
        String role,

        @Schema(description = "When the user joined the conversation")
        LocalDateTime joinedAt,

        @Schema(description = "When the user last read the conversation")
        LocalDateTime lastReadAt
) {}
