package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.adapter.inbound.quest.mapper.CampaignQuestViewModelMapper;
import com.dndplatform.campaign.domain.CampaignQuestFindByIdService;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.view.model.CampaignQuestFindByIdResource;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignQuestFindByIdDelegate implements CampaignQuestFindByIdResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignQuestFindByIdService service;
    private final CampaignQuestViewModelMapper viewModelMapper;

    @Inject
    public CampaignQuestFindByIdDelegate(CampaignQuestFindByIdService service,
                                         CampaignQuestViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public CampaignQuestViewModel findById(Long campaignId, Long questId, Long userId) {
        log.info(() -> "Finding quest %d in campaign %d for user %d".formatted(questId, campaignId, userId));

        CampaignQuest quest = service.findById(questId);
        return viewModelMapper.apply(quest);
    }
}
