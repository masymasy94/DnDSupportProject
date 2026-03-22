package com.dndplatform.combat.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request to update a participant")
@Builder
public record UpdateParticipantRequest(

        @Schema(description = "User ID of the requester (DM)", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Current hit points", example = "3")
        Integer currentHp,

        @Schema(description = "Active conditions")
        List<String> conditions,

        @Schema(description = "Whether this participant has the current turn")
        Boolean isActive
) {
}
