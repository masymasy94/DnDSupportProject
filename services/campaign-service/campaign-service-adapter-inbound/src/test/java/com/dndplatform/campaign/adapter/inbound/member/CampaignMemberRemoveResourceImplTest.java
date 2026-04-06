package com.dndplatform.campaign.adapter.inbound.member;

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
class CampaignMemberRemoveResourceImplTest {

    @Mock
    private CampaignMemberRemoveDelegate delegate;

    private CampaignMemberRemoveResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberRemoveResourceImpl(delegate);
    }

    @Test
    void shouldDelegateRemoveMember() {
        Long campaignId = 10L;
        Long userId = 2L;
        Long requesterId = 1L;
        willDoNothing().given(delegate).removeMember(campaignId, userId, requesterId);

        sut.removeMember(campaignId, userId, requesterId);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).removeMember(campaignId, userId, requesterId);
        inOrder.verifyNoMoreInteractions();
    }
}
