package com.dndplatform.campaign.adapter.inbound.quest;

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
class CampaignQuestCreateResourceImplTest {

    @Mock
    private CampaignQuestCreateDelegate delegate;

    private CampaignQuestCreateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestCreateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCreate(@Random CreateQuestRequest request,
                              @Random CampaignQuestViewModel expected) {
        Long campaignId = 10L;
        given(delegate.create(campaignId, request)).willReturn(expected);

        CampaignQuestViewModel result = sut.create(campaignId, request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).create(campaignId, request);
        inOrder.verifyNoMoreInteractions();
    }
}
