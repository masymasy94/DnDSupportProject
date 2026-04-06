package com.dndplatform.campaign.adapter.inbound.quest;

import com.dndplatform.campaign.domain.CampaignQuestDeleteService;
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
class CampaignQuestDeleteDelegateTest {

    @Mock
    private CampaignQuestDeleteService service;

    private CampaignQuestDeleteDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignQuestDeleteDelegate(service);
    }

    @Test
    void shouldDelegateToService() {
        Long campaignId = 1L;
        Long questId = 20L;
        Long userId = 42L;

        willDoNothing().given(service).delete(campaignId, questId, userId);

        sut.delete(campaignId, questId, userId);

        var inOrder = inOrder(service);
        then(service).should(inOrder).delete(campaignId, questId, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenUserIsNotAuthorized() {
        Long campaignId = 1L;
        Long questId = 20L;
        Long userId = 99L;

        willThrow(new ForbiddenException("Not the dungeon master")).given(service).delete(campaignId, questId, userId);

        assertThatThrownBy(() -> sut.delete(campaignId, questId, userId))
                .isInstanceOf(ForbiddenException.class);

        then(service).should().delete(campaignId, questId, userId);
    }
}
