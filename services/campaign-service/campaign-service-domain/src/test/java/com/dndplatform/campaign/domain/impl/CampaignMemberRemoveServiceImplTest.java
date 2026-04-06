package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignMemberRemoveService;
import com.dndplatform.campaign.domain.CampaignMemberRemoveServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignMemberRemoveRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignMemberRemoveServiceImplTest {

    @Mock
    private CampaignFindByIdRepository campaignFindRepository;
    @Mock
    private CampaignMemberRemoveRepository memberRemoveRepository;

    private CampaignMemberRemoveService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberRemoveServiceImpl(campaignFindRepository, memberRemoveRepository);
    }

    @Test
    void shouldRemoveMemberWhenUserIsDungeonMaster(@Random Campaign campaign,
                                                    @Random Long campaignId,
                                                    @Random Long userId,
                                                    @Random Long requesterId) {
        campaign = new Campaign(campaignId, "Test", "Desc", requesterId, CampaignStatus.ACTIVE, 5, null, null, null);
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        sut.remove(campaignId, userId, requesterId);

        var inOrder = inOrder(campaignFindRepository, memberRemoveRepository);
        then(campaignFindRepository).should(inOrder).findById(campaignId);
        then(memberRemoveRepository).should(inOrder).remove(campaignId, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldRemoveMemberWhenUserIsRemovingThemselves(@Random Campaign campaign,
                                                          @Random Long campaignId,
                                                          @Random Long requesterId) {
        campaign = new Campaign(campaignId, "Test", "Desc", 999L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        sut.remove(campaignId, requesterId, requesterId);

        var inOrder = inOrder(campaignFindRepository, memberRemoveRepository);
        then(campaignFindRepository).should(inOrder).findById(campaignId);
        then(memberRemoveRepository).should(inOrder).remove(campaignId, requesterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist(@Random Long campaignId,
                                                               @Random Long userId,
                                                               @Random Long requesterId) {
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.remove(campaignId, userId, requesterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Campaign not found");

        then(memberRemoveRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsNotDmOrSelf(@Random Campaign campaign,
                                                              @Random Long campaignId,
                                                              @Random Long userId,
                                                              @Random Long requesterId) {
        campaign = new Campaign(campaignId, "Test", "Desc", 999L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        assertThatThrownBy(() -> sut.remove(campaignId, userId, requesterId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(memberRemoveRepository).shouldHaveNoInteractions();
    }
}
