package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "code", "name"})
@Schema(description = "D&D alignment")
@Builder
public record AlignmentViewModel(
        @Schema(description = "Alignment ID", example = "1")
        Short id,

        @Schema(description = "Alignment code", example = "LG")
        String code,

        @Schema(description = "Alignment name", example = "Lawful Good")
        String name
) {}
