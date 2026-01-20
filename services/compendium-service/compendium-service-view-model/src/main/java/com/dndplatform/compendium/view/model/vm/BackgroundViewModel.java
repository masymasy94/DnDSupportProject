package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "description", "source"})
@Schema(description = "D&D background")
@Builder
public record BackgroundViewModel(
        @Schema(description = "Background ID", example = "1")
        Integer id,

        @Schema(description = "Background name", example = "Acolyte")
        String name,

        @Schema(description = "Background description", example = "You have spent your life in the service of a temple...")
        String description,

        @Schema(description = "Source type", example = "OFFICIAL")
        String source
) {}
