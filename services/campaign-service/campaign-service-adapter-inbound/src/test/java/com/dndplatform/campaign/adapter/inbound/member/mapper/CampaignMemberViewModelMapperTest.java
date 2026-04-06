package com.dndplatform.campaign.adapter.inbound.member.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.campaign.domain.model.CampaignMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CampaignMemberViewModelMapperTest {

    private CampaignMemberViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberViewModelMapper();
    }

    @Test
    void shouldMapAllFields(@Random CampaignMember member) {
        var result = sut.apply(member);

        assertThat(result.id()).isEqualTo(member.id());
        assertThat(result.campaignId()).isEqualTo(member.campaignId());
        assertThat(result.userId()).isEqualTo(member.userId());
        assertThat(result.characterId()).isEqualTo(member.characterId());
        assertThat(result.role()).isEqualTo(member.role().name());
        assertThat(result.joinedAt()).isEqualTo(member.joinedAt());
    }
}
