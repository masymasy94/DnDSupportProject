package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignMemberEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignMemberFindByUserRepositoryJpaTest {

    @Mock
    private CampaignMemberPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignMemberFindByUserRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberFindByUserRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedMemberWhenFound(@Random CampaignMember expected) {
        Long campaignId = 1L;
        Long userId = 2L;
        CampaignMemberEntity entity = new CampaignMemberEntity();

        given(panacheRepository.findByCampaignIdAndUserId(campaignId, userId)).willReturn(Optional.of(entity));
        given(mapper.toCampaignMember(entity)).willReturn(expected);

        Optional<CampaignMember> result = sut.findByCampaignIdAndUserId(campaignId, userId);

        assertThat(result).isPresent().contains(expected);
        then(panacheRepository).should().findByCampaignIdAndUserId(campaignId, userId);
        then(mapper).should().toCampaignMember(entity);
    }

    @Test
    void shouldReturnEmptyWhenMemberNotFound() {
        Long campaignId = 1L;
        Long userId = 99L;
        given(panacheRepository.findByCampaignIdAndUserId(campaignId, userId)).willReturn(Optional.empty());

        Optional<CampaignMember> result = sut.findByCampaignIdAndUserId(campaignId, userId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
