package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonbPropertyOrder({"content", "page", "size", "totalElements", "totalPages"})
@Schema(description = "Paginated character class list")
@Builder
public record PagedCharacterClassViewModel(
        @Schema(description = "List of classes")
        List<CharacterClassViewModel> content,

        @Schema(description = "Current page number", example = "0")
        int page,

        @Schema(description = "Page size", example = "50")
        int size,

        @Schema(description = "Total number of elements", example = "13")
        long totalElements,

        @Schema(description = "Total number of pages", example = "1")
        int totalPages
) {}
