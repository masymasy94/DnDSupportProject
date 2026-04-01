package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonbPropertyOrder({"content", "page", "size", "totalElements", "totalPages"})
@Schema(description = "Paginated list of campaigns")
@Builder
public record PagedCampaignsViewModel(

        @Schema(description = "List of campaign summaries")
        List<CampaignSummaryViewModel> content,

        @Schema(description = "Current page number (0-indexed)", example = "0")
        int page,

        @Schema(description = "Page size", example = "20")
        int size,

        @Schema(description = "Total number of campaigns", example = "12")
        long totalElements,

        @Schema(description = "Total number of pages", example = "1")
        int totalPages
) {
}
