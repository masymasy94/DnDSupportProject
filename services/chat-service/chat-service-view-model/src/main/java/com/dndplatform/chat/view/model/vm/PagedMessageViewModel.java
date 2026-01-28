package com.dndplatform.chat.view.model.vm;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonbPropertyOrder({"content", "page", "size", "totalElements", "totalPages"})
@Schema(description = "Paginated message list")
public record PagedMessageViewModel(
        @Schema(description = "List of messages")
        List<MessageViewModel> content,

        @Schema(description = "Current page number (0-indexed)", example = "0")
        int page,

        @Schema(description = "Page size", example = "50")
        int size,

        @Schema(description = "Total number of elements", example = "100")
        long totalElements,

        @Schema(description = "Total number of pages", example = "2")
        int totalPages
) {}
