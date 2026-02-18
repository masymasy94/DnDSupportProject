package com.dndplatform.campaign.adapter.inbound.note.mapper;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class CampaignNoteViewModelMapper implements Function<CampaignNote, CampaignNoteViewModel> {

    @Override
    public CampaignNoteViewModel apply(CampaignNote note) {
        return CampaignNoteViewModelBuilder.builder()
                .withId(note.id())
                .withCampaignId(note.campaignId())
                .withAuthorId(note.authorId())
                .withTitle(note.title())
                .withContent(note.content())
                .withVisibility(note.visibility().name())
                .withCreatedAt(note.createdAt())
                .withUpdatedAt(note.updatedAt())
                .build();
    }
}
