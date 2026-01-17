package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "description", "source", "isPublic"})
@Schema(description = "Species/Race information")
@Builder
public record SpeciesViewModel(
        @Schema(description = "Species ID", example = "1")
        Integer id,

        @Schema(description = "Species name", example = "Elf")
        String name,

        @Schema(description = "Species description")
        String description,

        @Schema(description = "Source type", example = "OFFICIAL")
        String source,

        @Schema(description = "Whether homebrew is publicly visible")
        Boolean isPublic
) {}
