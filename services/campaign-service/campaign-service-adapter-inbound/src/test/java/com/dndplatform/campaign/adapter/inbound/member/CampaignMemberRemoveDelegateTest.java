package com.dndplatform.campaign.adapter.inbound.member;

import com.dndplatform.campaign.domain.CampaignMemberRemoveService;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignMemberRemoveDelegateTest {

    @Mock
    private CampaignMemberRemoveService service;

    private CampaignMemberRemoveDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberRemoveDelegate(service);
    }

    @Test
    void shouldDelegateToService() {
        Long campaignId = 1L;
        Long userId = 5L;
        Long requesterId = 1L;

        willDoNothing().given(service).remove(campaignId, userId, requesterId);

        sut.removeMember(campaignId, userId, requesterId);

        var inOrder = inOrder(service);
        then(service).should(inOrder).remove(campaignId, userId, requesterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenRequesterIsNotAuthorized() {
        Long campaignId = 1L;
        Long userId = 5L;
        Long requesterId = 99L;

        willThrow(new ForbiddenException("Not authorized to remove members")).given(service).remove(campaignId, userId, requesterId);

        assertThatThrownBy(() -> sut.removeMember(campaignId, userId, requesterId))
                .isInstanceOf(ForbiddenException.class);

        then(service).should().remove(campaignId, userId, requesterId);
    }
}
