package com.dndplatform.campaign.adapter.inbound.note.mapper;

import com.dndplatform.campaign.domain.model.CampaignNoteCreate;
import com.dndplatform.campaign.domain.model.CampaignNoteCreateBuilder;
import com.dndplatform.campaign.domain.model.NoteVisibility;
import com.dndplatform.campaign.view.model.vm.CreateNoteRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateNoteMapper {

    public CampaignNoteCreate apply(Long campaignId, CreateNoteRequest request, Long authorId) {
        NoteVisibility visibility = request.visibility() != null
                ? NoteVisibility.valueOf(request.visibility())
                : NoteVisibility.PUBLIC;

        return CampaignNoteCreateBuilder.builder()
                .withCampaignId(campaignId)
                .withAuthorId(authorId)
                .withTitle(request.title())
                .withContent(request.content())
                .withVisibility(visibility)
                .build();
    }
}
