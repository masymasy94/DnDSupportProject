package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.model.MemberRole;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignMemberAddRepositoryJpaTest {

    @Mock
    private CampaignPanacheRepository campaignPanacheRepository;
    @Mock
    private CampaignMemberPanacheRepository memberPanacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignMemberAddRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberAddRepositoryJpa(campaignPanacheRepository, memberPanacheRepository, mapper);
    }

    @Test
    void shouldPersistMemberAndReturnMapped(@Random CampaignMember expected) {
        Long campaignId = 1L;
        Long userId = 2L;
        Long characterId = 3L;
        MemberRole role = MemberRole.PLAYER;

        CampaignEntity campaign = new CampaignEntity();
        given(campaignPanacheRepository.findById(campaignId)).willReturn(campaign);
        willDoNothing().given(memberPanacheRepository).persist(any(CampaignMemberEntity.class));
        given(mapper.toCampaignMember(any(CampaignMemberEntity.class))).willReturn(expected);

        CampaignMember result = sut.add(campaignId, userId, characterId, role);

        assertThat(result).isEqualTo(expected);
        then(campaignPanacheRepository).should().findById(campaignId);
        then(memberPanacheRepository).should().persist(any(CampaignMemberEntity.class));
        then(mapper).should().toCampaignMember(any(CampaignMemberEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCampaignDoesNotExist() {
        Long campaignId = 99L;
        given(campaignPanacheRepository.findById(campaignId)).willReturn(null);

        assertThatThrownBy(() -> sut.add(campaignId, 1L, null, MemberRole.PLAYER))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");

        then(memberPanacheRepository).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
    }
}
