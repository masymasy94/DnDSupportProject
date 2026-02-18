package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonbPropertyOrder({"id", "campaignId", "userId", "characterId", "role", "joinedAt"})
@Schema(description = "Campaign member details")
@Builder
public record CampaignMemberViewModel(

        @Schema(description = "Member record ID", example = "1")
        Long id,

        @Schema(description = "Campaign ID", example = "1")
        Long campaignId,

        @Schema(description = "User ID", example = "5")
        Long userId,

        @Schema(description = "Linked character ID", example = "12")
        Long characterId,

        @Schema(description = "Member role", example = "PLAYER")
        String role,

        @Schema(description = "When the member joined")
        LocalDateTime joinedAt
) {
}
