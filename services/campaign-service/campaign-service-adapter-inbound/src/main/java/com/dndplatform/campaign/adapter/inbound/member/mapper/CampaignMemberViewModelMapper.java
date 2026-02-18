package com.dndplatform.campaign.adapter.inbound.member.mapper;

import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class CampaignMemberViewModelMapper implements Function<CampaignMember, CampaignMemberViewModel> {

    @Override
    public CampaignMemberViewModel apply(CampaignMember member) {
        return CampaignMemberViewModelBuilder.builder()
                .withId(member.id())
                .withCampaignId(member.campaignId())
                .withUserId(member.userId())
                .withCharacterId(member.characterId())
                .withRole(member.role().name())
                .withJoinedAt(member.joinedAt())
                .build();
    }
}
