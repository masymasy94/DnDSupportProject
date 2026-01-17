package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "hitDie", "description", "source", "isPublic"})
@Schema(description = "Character class information")
@Builder
public record CharacterClassViewModel(
        @Schema(description = "Class ID", example = "1")
        Integer id,

        @Schema(description = "Class name", example = "Fighter")
        String name,

        @Schema(description = "Hit die for the class", example = "d10")
        String hitDie,

        @Schema(description = "Class description")
        String description,

        @Schema(description = "Source type", example = "OFFICIAL")
        String source,

        @Schema(description = "Whether homebrew is publicly visible")
        Boolean isPublic
) {}
