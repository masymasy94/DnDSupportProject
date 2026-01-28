package com.dndplatform.chat.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@JsonbPropertyOrder({"id", "name", "type", "createdBy", "createdAt", "updatedAt", "participants"})
@Schema(description = "Conversation information")
public record ConversationViewModel(
        @Schema(description = "Conversation ID", example = "1")
        Long id,

        @Schema(description = "Conversation name (null for direct messages)", example = "Party Chat")
        String name,

        @Schema(description = "Conversation type", example = "GROUP")
        String type,

        @Schema(description = "User ID who created the conversation", example = "1")
        Long createdBy,

        @Schema(description = "When the conversation was created")
        LocalDateTime createdAt,

        @Schema(description = "When the conversation was last updated")
        LocalDateTime updatedAt,

        @Schema(description = "List of participants")
        List<ParticipantViewModel> participants
) {}
