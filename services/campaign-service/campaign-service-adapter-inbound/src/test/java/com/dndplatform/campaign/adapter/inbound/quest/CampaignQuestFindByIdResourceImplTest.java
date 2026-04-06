package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
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
class CampaignQuestFindByIdResourceImplTest {

    @Mock
    private CampaignQuestFindByIdDelegate delegate;

    private CampaignQuestFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestFindByIdResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindById(@Random CampaignQuestViewModel expected) {
        Long campaignId = 10L;
        Long questId = 30L;
        Long userId = 1L;
        given(delegate.findById(campaignId, questId, userId)).willReturn(expected);

        CampaignQuestViewModel result = sut.findById(campaignId, questId, userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(campaignId, questId, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
