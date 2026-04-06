package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignMemberFindService;
import com.dndplatform.campaign.domain.CampaignMemberFindServiceImpl;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.repository.CampaignMemberFindByCampaignRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignMemberFindServiceImplTest {

    @Mock
    private CampaignMemberFindByCampaignRepository repository;

    private CampaignMemberFindService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberFindServiceImpl(repository);
    }

    @Test
    void shouldReturnMembersForCampaign(@Random CampaignMember member1,
                                        @Random CampaignMember member2,
                                        @Random Long campaignId) {
        List<CampaignMember> expected = List.of(member1, member2);
        given(repository.findByCampaignId(campaignId)).willReturn(expected);

        List<CampaignMember> result = sut.findByCampaignId(campaignId);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findByCampaignId(campaignId);
    }
}
