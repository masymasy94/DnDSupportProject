package com.dndplatform.chat.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"content", "messageType"})
@Schema(description = "Request to send a new message")
public record SendMessageViewModel(
        @NotBlank
        @Schema(description = "Message content", example = "Hello, everyone!", required = true)
        String content,

        @Schema(description = "Message type (TEXT, SYSTEM, IMAGE)", example = "TEXT")
        String messageType
) {}
