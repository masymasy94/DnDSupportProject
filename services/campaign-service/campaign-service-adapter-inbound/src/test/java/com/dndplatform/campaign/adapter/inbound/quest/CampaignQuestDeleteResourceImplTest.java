package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignQuestDeleteResourceImplTest {

    @Mock
    private CampaignQuestDeleteDelegate delegate;

    private CampaignQuestDeleteResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestDeleteResourceImpl(delegate);
    }

    @Test
    void shouldDelegateDelete() {
        Long campaignId = 10L;
        Long questId = 30L;
        Long userId = 1L;
        willDoNothing().given(delegate).delete(campaignId, questId, userId);

        sut.delete(campaignId, questId, userId);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).delete(campaignId, questId, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
