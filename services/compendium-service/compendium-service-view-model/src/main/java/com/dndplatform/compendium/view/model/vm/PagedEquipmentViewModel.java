package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonbPropertyOrder({"content", "page", "size", "totalElements", "totalPages"})
@Schema(description = "Paginated equipment list")
@Builder
public record PagedEquipmentViewModel(
        @Schema(description = "List of equipment")
        List<EquipmentViewModel> content,

        @Schema(description = "Current page number (0-indexed)", example = "0")
        int page,

        @Schema(description = "Page size", example = "50")
        int size,

        @Schema(description = "Total number of elements", example = "100")
        long totalElements,

        @Schema(description = "Total number of pages", example = "2")
        int totalPages
) {}
