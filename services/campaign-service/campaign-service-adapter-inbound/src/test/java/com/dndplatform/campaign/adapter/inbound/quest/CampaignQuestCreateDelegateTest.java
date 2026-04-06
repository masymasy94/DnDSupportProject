package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.adapter.inbound.quest.mapper.CampaignQuestViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.quest.mapper.CreateQuestMapper;
import com.dndplatform.campaign.domain.CampaignQuestCreateService;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestCreate;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.CreateQuestRequest;
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
class CampaignQuestCreateDelegateTest {

    @Mock
    private CampaignQuestCreateService service;

    @Mock
    private CampaignQuestViewModelMapper viewModelMapper;

    @Mock
    private CreateQuestMapper createMapper;

    private CampaignQuestCreateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestCreateDelegate(service, viewModelMapper, createMapper);
    }

    @Test
    void shouldReturnQuestViewModel(
            @Random CampaignQuestCreate campaignQuestCreate,
            @Random CampaignQuest campaignQuest,
            @Random CampaignQuestViewModel questViewModel) {

        Long campaignId = 1L;
        Long userId = 42L;
        CreateQuestRequest request = new CreateQuestRequest(userId, "Find the Artifact", "Locate it in the ruins", "ACTIVE", "MAIN");

        given(createMapper.apply(campaignId, request, userId)).willReturn(campaignQuestCreate);
        given(service.create(campaignQuestCreate)).willReturn(campaignQuest);
        given(viewModelMapper.apply(campaignQuest)).willReturn(questViewModel);

        CampaignQuestViewModel result = sut.create(campaignId, request);

        assertThat(result).isEqualTo(questViewModel);

        var inOrder = inOrder(createMapper, service, viewModelMapper);
        then(createMapper).should(inOrder).apply(campaignId, request, userId);
        then(service).should(inOrder).create(campaignQuestCreate);
        then(viewModelMapper).should(inOrder).apply(campaignQuest);
        inOrder.verifyNoMoreInteractions();
    }
}
