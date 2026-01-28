package com.dndplatform.chat.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonbPropertyOrder({"type", "name", "participantIds"})
@Schema(description = "Request to create a new conversation")
public record CreateConversationViewModel(
        @NotNull
        @Schema(description = "Conversation type (DIRECT or GROUP)", example = "DIRECT", required = true)
        String type,

        @Schema(description = "Conversation name (required for GROUP, ignored for DIRECT)", example = "Party Chat")
        String name,

        @NotNull
        @Schema(description = "List of user IDs to add as participants", example = "[2, 3]", required = true)
        List<Long> participantIds
) {}
