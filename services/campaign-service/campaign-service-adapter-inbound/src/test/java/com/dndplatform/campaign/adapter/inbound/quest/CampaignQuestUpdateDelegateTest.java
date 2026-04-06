package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.adapter.inbound.quest.mapper.CampaignQuestViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.quest.mapper.UpdateQuestMapper;
import com.dndplatform.campaign.domain.CampaignQuestUpdateService;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateQuestRequest;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignQuestUpdateDelegateTest {

    @Mock
    private CampaignQuestUpdateService service;

    @Mock
    private CampaignQuestViewModelMapper viewModelMapper;

    @Mock
    private UpdateQuestMapper updateMapper;

    private CampaignQuestUpdateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestUpdateDelegate(service, viewModelMapper, updateMapper);
    }

    @Test
    void shouldReturnUpdatedQuestViewModel(
            @Random CampaignQuestUpdate campaignQuestUpdate,
            @Random CampaignQuest campaignQuest,
            @Random CampaignQuestViewModel questViewModel) {

        Long campaignId = 1L;
        Long questId = 20L;
        Long userId = 42L;
        UpdateQuestRequest request = new UpdateQuestRequest(userId, "Updated Quest Title", "Updated description", "COMPLETED", "MAIN");

        given(updateMapper.apply(questId, request)).willReturn(campaignQuestUpdate);
        given(service.update(campaignQuestUpdate, campaignId, userId)).willReturn(campaignQuest);
        given(viewModelMapper.apply(campaignQuest)).willReturn(questViewModel);

        CampaignQuestViewModel result = sut.update(campaignId, questId, request);

        assertThat(result).isEqualTo(questViewModel);

        var inOrder = inOrder(updateMapper, service, viewModelMapper);
        then(updateMapper).should(inOrder).apply(questId, request);
        then(service).should(inOrder).update(campaignQuestUpdate, campaignId, userId);
        then(viewModelMapper).should(inOrder).apply(campaignQuest);
        inOrder.verifyNoMoreInteractions();
    }
}
