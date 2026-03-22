package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.domain.CampaignQuestDeleteService;
import com.dndplatform.campaign.view.model.CampaignQuestDeleteResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignQuestDeleteDelegate implements CampaignQuestDeleteResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignQuestDeleteService service;

    @Inject
    public CampaignQuestDeleteDelegate(CampaignQuestDeleteService service) {
        this.service = service;
    }

    @Override
    public void delete(Long campaignId, Long questId, Long userId) {
        log.info(() -> "Deleting quest %d from campaign %d by user %d".formatted(questId, campaignId, userId));
        service.delete(campaignId, questId, userId);
    }
}
