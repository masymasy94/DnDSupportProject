package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignCreate;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignCreateRepositoryJpaTest {

    @Mock
    private CampaignPanacheRepository panacheRepository;
    @Mock
    private CampaignEntityMapper mapper;

    private CampaignCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignCreateRepositoryJpa(panacheRepository, mapper);
    }

    @Test
    void shouldPersistAndReturnMappedCampaign(@Random CampaignCreate input, @Random Campaign expected) {
        willDoNothing().given(panacheRepository).persist(any(CampaignEntity.class));
        given(mapper.toCampaign(any(CampaignEntity.class))).willReturn(expected);

        Campaign result = sut.save(input);

        assertThat(result).isEqualTo(expected);
        then(panacheRepository).should().persist(any(CampaignEntity.class));
        then(mapper).should().toCampaign(any(CampaignEntity.class));
    }

    @Test
    void shouldDefaultMaxPlayersToSixWhenInputIsNull(@Random Campaign expected) {
        CampaignCreate input = new CampaignCreate(1L, "Test", "desc", null, null);
        willDoNothing().given(panacheRepository).persist(any(CampaignEntity.class));
        given(mapper.toCampaign(any(CampaignEntity.class))).willReturn(expected);

        sut.save(input);

        then(panacheRepository).should().persist(any(CampaignEntity.class));
    }
}
