package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"userId", "title", "content", "visibility"})
@Schema(description = "Request to create a campaign note")
@Builder
public record CreateNoteRequest(

        @Schema(description = "ID of the user creating the note", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Note title", example = "Session 1 Recap")
        @NotBlank @Size(min = 1, max = 100)
        String title,

        @Schema(description = "Note content", example = "The party arrived at the village of Barovia...")
        String content,

        @Schema(description = "Note visibility (PUBLIC or PRIVATE)", example = "PUBLIC")
        String visibility
) {
}
