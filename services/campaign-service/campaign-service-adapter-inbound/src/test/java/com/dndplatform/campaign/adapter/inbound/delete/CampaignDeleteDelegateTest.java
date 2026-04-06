package com.dndplatform.campaign.adapter.inbound.delete;

import com.dndplatform.campaign.domain.CampaignDeleteService;
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
class CampaignDeleteDelegateTest {

    @Mock
    private CampaignDeleteService service;

    private CampaignDeleteDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignDeleteDelegate(service);
    }

    @Test
    void shouldDelegateToService() {
        Long campaignId = 1L;
        Long userId = 42L;

        willDoNothing().given(service).delete(campaignId, userId);

        sut.delete(campaignId, userId);

        var inOrder = inOrder(service);
        then(service).should(inOrder).delete(campaignId, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenUserIsNotOwner() {
        Long campaignId = 1L;
        Long userId = 99L;

        willThrow(new ForbiddenException("Not the dungeon master")).given(service).delete(campaignId, userId);

        assertThatThrownBy(() -> sut.delete(campaignId, userId))
                .isInstanceOf(ForbiddenException.class);

        then(service).should().delete(campaignId, userId);
    }
}
