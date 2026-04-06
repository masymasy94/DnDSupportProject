package com.dndplatform.campaign.adapter.inbound.update.mapper;

import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CampaignUpdateMapperTest {

    private CampaignUpdateMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignUpdateMapper();
    }

    @Test
    void shouldMapAllFields() {
        var request = new UpdateCampaignRequest(1L, "Updated Name", "Updated desc", "ACTIVE", 10, "http://img.url");

        var result = sut.apply(3L, request);

        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.name()).isEqualTo("Updated Name");
        assertThat(result.description()).isEqualTo("Updated desc");
        assertThat(result.status()).isEqualTo(CampaignStatus.ACTIVE);
        assertThat(result.maxPlayers()).isEqualTo(10);
        assertThat(result.imageUrl()).isEqualTo("http://img.url");
    }

    @Test
    void shouldMapNullStatusToNull() {
        var request = new UpdateCampaignRequest(1L, "Name", "Desc", null, 6, null);

        var result = sut.apply(3L, request);

        assertThat(result.status()).isNull();
    }
}
