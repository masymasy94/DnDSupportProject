package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.adapter.inbound.quest.mapper.CampaignQuestViewModelMapper;
import com.dndplatform.campaign.domain.CampaignQuestFindByCampaignService;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.QuestPriority;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignQuestListDelegateTest {

    @Mock
    private CampaignQuestFindByCampaignService service;

    @Mock
    private CampaignQuestViewModelMapper viewModelMapper;

    private CampaignQuestListDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestListDelegate(service, viewModelMapper);
    }

    @Test
    void shouldReturnQuestViewModels(
            @Random CampaignQuestViewModel vm1,
            @Random CampaignQuestViewModel vm2) {

        Long campaignId = 1L;
        Long userId = 42L;

        CampaignQuest quest1 = new CampaignQuest(1L, campaignId, userId, "Quest One", "Desc one", QuestStatus.ACTIVE, QuestPriority.MAIN, null, null);
        CampaignQuest quest2 = new CampaignQuest(2L, campaignId, userId, "Quest Two", "Desc two", QuestStatus.ACTIVE, QuestPriority.SIDE, null, null);

        given(service.findByCampaign(campaignId, QuestStatus.ACTIVE)).willReturn(List.of(quest1, quest2));
        given(viewModelMapper.apply(quest1)).willReturn(vm1);
        given(viewModelMapper.apply(quest2)).willReturn(vm2);

        List<CampaignQuestViewModel> result = sut.list(campaignId, userId, "ACTIVE");

        assertThat(result).containsExactly(vm1, vm2);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findByCampaign(campaignId, QuestStatus.ACTIVE);
        then(viewModelMapper).should(inOrder).apply(quest1);
        then(viewModelMapper).should(inOrder).apply(quest2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldPassNullStatusWhenNoStatusProvided() {
        Long campaignId = 1L;
        Long userId = 42L;

        given(service.findByCampaign(campaignId, null)).willReturn(List.of());

        List<CampaignQuestViewModel> result = sut.list(campaignId, userId, null);

        assertThat(result).isEmpty();

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findByCampaign(campaignId, null);
        inOrder.verifyNoMoreInteractions();
    }
}
