package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"userId", "title", "description", "status", "priority"})
@Schema(description = "Request to create a campaign quest")
@Builder
public record CreateQuestRequest(

        @Schema(description = "ID of the user creating the quest", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Quest title", example = "Find the Lost Artifact")
        @NotBlank @Size(min = 1, max = 100)
        String title,

        @Schema(description = "Quest description", example = "The party must locate the ancient artifact hidden in the ruins...")
        String description,

        @Schema(description = "Quest status (ACTIVE, COMPLETED, or FAILED)", example = "ACTIVE")
        String status,

        @Schema(description = "Quest priority (MAIN or SIDE)", example = "MAIN")
        String priority
) {
}
