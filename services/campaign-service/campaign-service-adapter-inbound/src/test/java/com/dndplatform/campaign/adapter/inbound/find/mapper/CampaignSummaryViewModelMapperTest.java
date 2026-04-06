package com.dndplatform.campaign.adapter.inbound.find.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.campaign.domain.model.CampaignSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CampaignSummaryViewModelMapperTest {

    private CampaignSummaryViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignSummaryViewModelMapper();
    }

    @Test
    void shouldMapAllFields(@Random CampaignSummary summary) {
        var result = sut.apply(summary);

        assertThat(result.id()).isEqualTo(summary.id());
        assertThat(result.name()).isEqualTo(summary.name());
        assertThat(result.status()).isEqualTo(summary.status().name());
        assertThat(result.playerCount()).isEqualTo(summary.playerCount());
    }
}
