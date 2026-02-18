package com.dndplatform.campaign.adapter.inbound.note.mapper;

import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdateBuilder;
import com.dndplatform.campaign.domain.model.NoteVisibility;
import com.dndplatform.campaign.view.model.vm.UpdateNoteRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UpdateNoteMapper {

    public CampaignNoteUpdate apply(Long noteId, UpdateNoteRequest request) {
        NoteVisibility visibility = request.visibility() != null
                ? NoteVisibility.valueOf(request.visibility())
                : null;

        return CampaignNoteUpdateBuilder.builder()
                .withId(noteId)
                .withTitle(request.title())
                .withContent(request.content())
                .withVisibility(visibility)
                .build();
    }
}
