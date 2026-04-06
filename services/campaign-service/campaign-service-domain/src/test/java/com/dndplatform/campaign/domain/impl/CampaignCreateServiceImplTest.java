package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignCreateService;
import com.dndplatform.campaign.domain.CampaignCreateServiceImpl;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignCreate;
import com.dndplatform.campaign.domain.model.MemberRole;
import com.dndplatform.campaign.domain.repository.CampaignCreateRepository;
import com.dndplatform.campaign.domain.repository.CampaignMemberAddRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignCreateServiceImplTest {

    @Mock
    private CampaignCreateRepository campaignRepository;
    @Mock
    private CampaignMemberAddRepository memberRepository;

    private CampaignCreateService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignCreateServiceImpl(campaignRepository, memberRepository);
    }

    @Test
    void shouldCreateCampaignAndAddDungeonMaster(@Random CampaignCreate input,
                                                 @Random Campaign expected) {
        given(campaignRepository.save(input)).willReturn(expected);

        Campaign result = sut.create(input);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(campaignRepository, memberRepository);
        then(campaignRepository).should(inOrder).save(input);
        then(memberRepository).should(inOrder).add(expected.id(), input.dungeonMasterId(), null, MemberRole.DUNGEON_MASTER);
        inOrder.verifyNoMoreInteractions();
    }
}
