package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.adapter.inbound.quest.mapper.CampaignQuestViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.quest.mapper.UpdateQuestMapper;
import com.dndplatform.campaign.domain.CampaignQuestUpdateService;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;
import com.dndplatform.campaign.view.model.CampaignQuestUpdateResource;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateQuestRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignQuestUpdateDelegate implements CampaignQuestUpdateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignQuestUpdateService service;
    private final CampaignQuestViewModelMapper viewModelMapper;
    private final UpdateQuestMapper updateMapper;

    @Inject
    public CampaignQuestUpdateDelegate(CampaignQuestUpdateService service,
                                       CampaignQuestViewModelMapper viewModelMapper,
                                       UpdateQuestMapper updateMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
        this.updateMapper = updateMapper;
    }

    @Override
    public CampaignQuestViewModel update(Long campaignId, Long questId, UpdateQuestRequest request) {
        Long userId = request.userId();
        log.info(() -> "Updating quest %d in campaign %d by user %d".formatted(questId, campaignId, userId));

        CampaignQuestUpdate domainUpdate = updateMapper.apply(questId, request);
        CampaignQuest quest = service.update(domainUpdate, campaignId, userId);
        return viewModelMapper.apply(quest);
    }
}
