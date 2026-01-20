package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "category"})
@Schema(description = "D&D tool type")
@Builder
public record ToolTypeViewModel(
        @Schema(description = "Tool type ID", example = "1")
        Short id,

        @Schema(description = "Tool type name", example = "Smith's Tools")
        String name,

        @Schema(description = "Tool category", example = "ARTISAN")
        String category
) {}
