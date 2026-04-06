package com.dndplatform.campaign.adapter.inbound.quest;

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
class CampaignQuestUpdateResourceImplTest {

    @Mock
    private CampaignQuestUpdateDelegate delegate;

    private CampaignQuestUpdateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestUpdateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpdate(@Random UpdateQuestRequest request,
                              @Random CampaignQuestViewModel expected) {
        Long campaignId = 10L;
        Long questId = 30L;
        given(delegate.update(campaignId, questId, request)).willReturn(expected);

        CampaignQuestViewModel result = sut.update(campaignId, questId, request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).update(campaignId, questId, request);
        inOrder.verifyNoMoreInteractions();
    }
}
