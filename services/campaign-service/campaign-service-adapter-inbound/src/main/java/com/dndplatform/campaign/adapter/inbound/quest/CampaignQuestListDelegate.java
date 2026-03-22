package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.adapter.inbound.quest.mapper.CampaignQuestViewModelMapper;
import com.dndplatform.campaign.domain.CampaignQuestFindByCampaignService;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.view.model.CampaignQuestListResource;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignQuestListDelegate implements CampaignQuestListResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignQuestFindByCampaignService service;
    private final CampaignQuestViewModelMapper viewModelMapper;

    @Inject
    public CampaignQuestListDelegate(CampaignQuestFindByCampaignService service,
                                     CampaignQuestViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public List<CampaignQuestViewModel> list(Long campaignId, Long userId, String status) {
        log.info(() -> "Listing quests for campaign %d, user %d, status %s".formatted(campaignId, userId, status));

        QuestStatus questStatus = status != null ? QuestStatus.valueOf(status) : null;

        return service.findByCampaign(campaignId, questStatus).stream()
                .map(viewModelMapper)
                .toList();
    }
}
