package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.adapter.inbound.member.mapper.CampaignMemberViewModelMapper;
import com.dndplatform.campaign.domain.CampaignMemberFindService;
import com.dndplatform.campaign.view.model.CampaignMemberListResource;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignMemberListDelegate implements CampaignMemberListResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignMemberFindService service;
    private final CampaignMemberViewModelMapper mapper;

    @Inject
    public CampaignMemberListDelegate(CampaignMemberFindService service,
                                      CampaignMemberViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<CampaignMemberViewModel> listMembers(Long campaignId) {
        log.info(() -> "Listing members for campaign %d".formatted(campaignId));

        return service.findByCampaignId(campaignId).stream()
                .map(mapper)
                .toList();
    }
}
