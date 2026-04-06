package com.dndplatform.campaign.adapter.inbound.create.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.campaign.domain.model.Campaign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CampaignViewModelMapperTest {

    private CampaignViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignViewModelMapper();
    }

    @Test
    void shouldMapAllFields(@Random Campaign campaign) {
        var result = sut.apply(campaign);

        assertThat(result.id()).isEqualTo(campaign.id());
        assertThat(result.name()).isEqualTo(campaign.name());
        assertThat(result.description()).isEqualTo(campaign.description());
        assertThat(result.dungeonMasterId()).isEqualTo(campaign.dungeonMasterId());
        assertThat(result.status()).isEqualTo(campaign.status().name());
        assertThat(result.maxPlayers()).isEqualTo(campaign.maxPlayers());
        assertThat(result.imageUrl()).isEqualTo(campaign.imageUrl());
        assertThat(result.createdAt()).isEqualTo(campaign.createdAt());
        assertThat(result.updatedAt()).isEqualTo(campaign.updatedAt());
    }
}
