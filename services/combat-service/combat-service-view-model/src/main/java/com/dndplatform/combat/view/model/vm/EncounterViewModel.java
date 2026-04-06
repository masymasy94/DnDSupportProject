package com.dndplatform.combat.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@JsonbPropertyOrder({"id", "campaignId", "createdByUserId", "name", "description", "status", "partyLevel", "partySize", "difficultyRating", "participants", "createdAt", "updatedAt"})
@Schema(description = "Full encounter details")
@Builder
public record EncounterViewModel(

        @Schema(description = "Encounter ID", example = "1")
        Long id,

        @Schema(description = "Campaign ID", example = "1")
        Long campaignId,

        @Schema(description = "User ID of the creator (DM)", example = "42")
        Long createdByUserId,

        @Schema(description = "Encounter name", example = "Goblin Ambush")
        String name,

        @Schema(description = "Encounter description")
        String description,

        @Schema(description = "Encounter status", example = "DRAFT")
        String status,

        @Schema(description = "Party level", example = "5")
        int partyLevel,

        @Schema(description = "Party size", example = "4")
        int partySize,

        @Schema(description = "Difficulty rating", example = "MEDIUM")
        String difficultyRating,

        @Schema(description = "List of participants")
        List<ParticipantViewModel> participants,

        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {
}
