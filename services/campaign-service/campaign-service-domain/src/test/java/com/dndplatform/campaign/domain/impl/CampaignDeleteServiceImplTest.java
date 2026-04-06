package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignDeleteService;
import com.dndplatform.campaign.domain.CampaignDeleteServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.repository.CampaignDeleteRepository;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignDeleteServiceImplTest {

    @Mock
    private CampaignFindByIdRepository findRepository;
    @Mock
    private CampaignDeleteRepository deleteRepository;

    private CampaignDeleteService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignDeleteServiceImpl(findRepository, deleteRepository);
    }

    @Test
    void shouldDeleteCampaignWhenUserIsDungeonMaster(@Random Long campaignId,
                                                     @Random Long userId) {
        Campaign campaign = new Campaign(campaignId, "Test", "Desc", userId, CampaignStatus.ACTIVE, 5, null, null, null);
        given(findRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        sut.delete(campaignId, userId);

        var inOrder = inOrder(findRepository, deleteRepository);
        then(findRepository).should(inOrder).findById(campaignId);
        then(deleteRepository).should(inOrder).deleteById(campaignId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist(@Random Long campaignId,
                                                               @Random Long userId) {
        given(findRepository.findById(campaignId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.delete(campaignId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Campaign not found");

        then(deleteRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsNotDungeonMaster(@Random Campaign campaign,
                                                                  @Random Long campaignId,
                                                                  @Random Long userId) {
        campaign = new Campaign(campaignId, "Test", "Desc", 999L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(findRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        assertThatThrownBy(() -> sut.delete(campaignId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(deleteRepository).shouldHaveNoInteractions();
    }
}
