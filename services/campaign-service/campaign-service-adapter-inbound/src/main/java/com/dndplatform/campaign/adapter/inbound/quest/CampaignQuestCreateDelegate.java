package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.adapter.inbound.quest.mapper.CampaignQuestViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.quest.mapper.CreateQuestMapper;
import com.dndplatform.campaign.domain.CampaignQuestCreateService;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestCreate;
import com.dndplatform.campaign.view.model.CampaignQuestCreateResource;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.CreateQuestRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignQuestCreateDelegate implements CampaignQuestCreateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignQuestCreateService service;
    private final CampaignQuestViewModelMapper viewModelMapper;
    private final CreateQuestMapper createMapper;

    @Inject
    public CampaignQuestCreateDelegate(CampaignQuestCreateService service,
                                       CampaignQuestViewModelMapper viewModelMapper,
                                       CreateQuestMapper createMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
        this.createMapper = createMapper;
    }

    @Override
    public CampaignQuestViewModel create(Long campaignId, CreateQuestRequest request) {
        Long userId = request.userId();
        log.info(() -> "Creating quest for campaign %d by user %d".formatted(campaignId, userId));

        CampaignQuestCreate domainCreate = createMapper.apply(campaignId, request, userId);
        CampaignQuest quest = service.create(domainCreate);
        return viewModelMapper.apply(quest);
    }
}
