package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.domain.CampaignMemberRemoveService;
import com.dndplatform.campaign.view.model.CampaignMemberRemoveResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignMemberRemoveDelegate implements CampaignMemberRemoveResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignMemberRemoveService service;

    @Inject
    public CampaignMemberRemoveDelegate(CampaignMemberRemoveService service) {
        this.service = service;
    }

    @Override
    public void removeMember(Long campaignId, Long userId, Long requesterId) {
        log.info(() -> "Removing user %d from campaign %d".formatted(userId, campaignId));
        service.remove(campaignId, userId, requesterId);
    }
}
