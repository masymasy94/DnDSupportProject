package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.adapter.inbound.quest.mapper.CampaignQuestViewModelMapper;
import com.dndplatform.campaign.domain.CampaignQuestFindByIdService;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignQuestFindByIdDelegateTest {

    @Mock
    private CampaignQuestFindByIdService service;

    @Mock
    private CampaignQuestViewModelMapper viewModelMapper;

    private CampaignQuestFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestFindByIdDelegate(service, viewModelMapper);
    }

    @Test
    void shouldReturnQuestViewModel(
            @Random CampaignQuest campaignQuest,
            @Random CampaignQuestViewModel questViewModel) {

        Long campaignId = 1L;
        Long questId = 20L;
        Long userId = 42L;

        given(service.findById(questId)).willReturn(campaignQuest);
        given(viewModelMapper.apply(campaignQuest)).willReturn(questViewModel);

        CampaignQuestViewModel result = sut.findById(campaignId, questId, userId);

        assertThat(result).isEqualTo(questViewModel);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findById(questId);
        then(viewModelMapper).should(inOrder).apply(campaignQuest);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenQuestNotFound() {
        Long campaignId = 1L;
        Long questId = 999L;
        Long userId = 42L;

        willThrow(new NotFoundException("Quest not found")).given(service).findById(questId);

        assertThatThrownBy(() -> sut.findById(campaignId, questId, userId))
                .isInstanceOf(NotFoundException.class);

        then(service).should().findById(questId);
        then(viewModelMapper).shouldHaveNoInteractions();
    }
}
