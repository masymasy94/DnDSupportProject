package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "rarity", "type", "requiresAttunement", "attunementRequirements", "description", "source", "isPublic"})
@Schema(description = "Magic item information")
@Builder
public record MagicItemViewModel(
        @Schema(description = "Magic item ID", example = "1")
        Integer id,

        @Schema(description = "Magic item name", example = "Bag of Holding")
        String name,

        @Schema(description = "Item rarity", example = "Uncommon")
        String rarity,

        @Schema(description = "Item type", example = "Wondrous Item")
        String type,

        @Schema(description = "Whether the item requires attunement", example = "false")
        Boolean requiresAttunement,

        @Schema(description = "Attunement requirements if any", example = "by a spellcaster")
        String attunementRequirements,

        @Schema(description = "Item description")
        String description,

        @Schema(description = "Source type", example = "OFFICIAL")
        String source,

        @Schema(description = "Whether homebrew is publicly visible")
        Boolean isPublic
) {}
