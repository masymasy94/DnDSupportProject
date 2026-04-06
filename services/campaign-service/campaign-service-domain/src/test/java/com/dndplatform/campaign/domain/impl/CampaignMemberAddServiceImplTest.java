package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignMemberAddService;
import com.dndplatform.campaign.domain.CampaignMemberAddServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.model.MemberRole;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignMemberAddRepository;
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
class CampaignMemberAddServiceImplTest {

    @Mock
    private CampaignFindByIdRepository campaignFindRepository;
    @Mock
    private CampaignMemberAddRepository memberAddRepository;

    private CampaignMemberAddService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberAddServiceImpl(campaignFindRepository, memberAddRepository);
    }

    @Test
    void shouldAddMemberToCampaignWhenUserIsDungeonMaster(@Random Campaign campaign,
                                                          @Random CampaignMember expected,
                                                          @Random Long campaignId,
                                                          @Random Long userId,
                                                          @Random Long characterId,
                                                          @Random Long requesterId) {
        campaign = new Campaign(campaignId, "Test", "Desc", requesterId, CampaignStatus.ACTIVE, 5, null, null, null);
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));
        given(memberAddRepository.add(campaignId, userId, characterId, MemberRole.PLAYER)).willReturn(expected);

        CampaignMember result = sut.add(campaignId, userId, characterId, requesterId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(campaignFindRepository, memberAddRepository);
        then(campaignFindRepository).should(inOrder).findById(campaignId);
        then(memberAddRepository).should(inOrder).add(campaignId, userId, characterId, MemberRole.PLAYER);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist(@Random Long campaignId,
                                                               @Random Long userId,
                                                               @Random Long characterId,
                                                               @Random Long requesterId) {
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.add(campaignId, userId, characterId, requesterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Campaign not found");

        then(memberAddRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserIsNotDungeonMaster(@Random Campaign campaign,
                                                                  @Random Long campaignId,
                                                                  @Random Long userId,
                                                                  @Random Long characterId,
                                                                  @Random Long requesterId) {
        campaign = new Campaign(campaignId, "Test", "Desc", 999L, CampaignStatus.ACTIVE, 5, null, null, null);
        given(campaignFindRepository.findById(campaignId)).willReturn(Optional.of(campaign));

        assertThatThrownBy(() -> sut.add(campaignId, userId, characterId, requesterId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(memberAddRepository).shouldHaveNoInteractions();
    }
}
