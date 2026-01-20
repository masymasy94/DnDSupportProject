package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "script", "type"})
@Schema(description = "D&D language")
@Builder
public record LanguageViewModel(
        @Schema(description = "Language ID", example = "1")
        Short id,

        @Schema(description = "Language name", example = "Common")
        String name,

        @Schema(description = "Writing script", example = "Common")
        String script,

        @Schema(description = "Language type", example = "Standard")
        String type
) {}
