package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignMemberFindByCampaignRepositoryJpaTest {

    @Mock
    private CampaignMemberPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignMemberFindByCampaignRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberFindByCampaignRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldReturnMappedMembersForCampaign(@Random CampaignMember expected) {
        Long campaignId = 1L;
        CampaignEntity campaign = new CampaignEntity();
        campaign.id = campaignId;
        CampaignMemberEntity entity = new CampaignMemberEntity();
        entity.campaign = campaign;

        given(panacheRepository.findByCampaignId(campaignId)).willReturn(List.of(entity));
        given(mapper.toCampaignMember(entity)).willReturn(expected);

        List<CampaignMember> result = sut.findByCampaignId(campaignId);

        assertThat(result).containsExactly(expected);
        then(panacheRepository).should().findByCampaignId(campaignId);
        then(mapper).should().toCampaignMember(entity);
    }

    @Test
    void shouldReturnEmptyListWhenNoMembers() {
        Long campaignId = 2L;
        given(panacheRepository.findByCampaignId(campaignId)).willReturn(List.of());

        List<CampaignMember> result = sut.findByCampaignId(campaignId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
