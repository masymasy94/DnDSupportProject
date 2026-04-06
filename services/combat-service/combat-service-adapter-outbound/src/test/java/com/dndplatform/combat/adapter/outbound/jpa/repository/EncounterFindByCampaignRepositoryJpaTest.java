package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.Encounter;
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
class EncounterFindByCampaignRepositoryJpaTest {

    @Mock
    private EncounterEntityMapper mapper;

    @Mock
    private EncounterPanacheRepository panacheRepository;

    private EncounterFindByCampaignRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterFindByCampaignRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void shouldReturnMappedEncountersForCampaign(@Random Long campaignId,
                                                  @Random EncounterEntity entity,
                                                  @Random Encounter expected) {
        given(panacheRepository.findByCampaignId(campaignId)).willReturn(List.of(entity));
        given(mapper.toEncounter(entity)).willReturn(expected);

        var result = sut.findByCampaign(campaignId);

        assertThat(result).containsExactly(expected);
        then(panacheRepository).should().findByCampaignId(campaignId);
        then(mapper).should().toEncounter(entity);
    }

    @Test
    void shouldReturnEmptyListWhenNoEncountersFound(@Random Long campaignId) {
        given(panacheRepository.findByCampaignId(campaignId)).willReturn(List.of());

        var result = sut.findByCampaign(campaignId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
