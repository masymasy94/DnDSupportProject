package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.adapter.inbound.member.mapper.CampaignMemberViewModelMapper;
import com.dndplatform.campaign.domain.CampaignMemberAddService;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.view.model.CampaignMemberAddResource;
import com.dndplatform.campaign.view.model.vm.AddMemberRequest;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignMemberAddDelegate implements CampaignMemberAddResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignMemberAddService service;
    private final CampaignMemberViewModelMapper mapper;

    @Inject
    public CampaignMemberAddDelegate(CampaignMemberAddService service,
                                     CampaignMemberViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public CampaignMemberViewModel addMember(Long campaignId, AddMemberRequest request) {
        log.info(() -> "Adding user %d to campaign %d".formatted(request.userId(), campaignId));

        CampaignMember member = service.add(campaignId, request.userId(), request.characterId(), request.requesterId());
        return mapper.apply(member);
    }
}
