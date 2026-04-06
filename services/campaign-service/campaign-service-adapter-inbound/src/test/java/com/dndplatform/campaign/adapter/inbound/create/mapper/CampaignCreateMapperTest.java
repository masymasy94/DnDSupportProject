package com.dndplatform.campaign.adapter.inbound.create.mapper;

import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CampaignCreateMapperTest {

    private CampaignCreateMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignCreateMapper();
    }

    @Test
    void shouldMapAllFields() {
        var request = new CreateCampaignRequest(1L, "Test Campaign", "A description", 8, "http://img.url");

        var result = sut.apply(request);

        assertThat(result.name()).isEqualTo("Test Campaign");
        assertThat(result.description()).isEqualTo("A description");
        assertThat(result.maxPlayers()).isEqualTo(8);
        assertThat(result.imageUrl()).isEqualTo("http://img.url");
    }

    @Test
    void shouldDefaultMaxPlayersToSixWhenNull() {
        var request = new CreateCampaignRequest(1L, "Test Campaign", null, null, null);

        var result = sut.apply(request);

        assertThat(result.maxPlayers()).isEqualTo(6);
    }
}
