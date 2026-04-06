package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.EncounterFindByCampaignService;
import com.dndplatform.combat.domain.EncounterFindByCampaignServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.repository.EncounterFindByCampaignRepository;
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
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterFindByCampaignServiceImplTest {

    @Mock
    private EncounterFindByCampaignRepository repository;

    private EncounterFindByCampaignService sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterFindByCampaignServiceImpl(repository);
    }

    @Test
    void shouldFindEncountersByCampaign(@Random Long campaignId, @Random Encounter encounter1, @Random Encounter encounter2) {
        List<Encounter> expected = List.of(encounter1, encounter2);

        given(repository.findByCampaign(campaignId)).willReturn(expected);

        List<Encounter> result = sut.findByCampaign(campaignId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findByCampaign(campaignId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyListWhenNoEncounters(@Random Long campaignId) {
        given(repository.findByCampaign(campaignId)).willReturn(List.of());

        List<Encounter> result = sut.findByCampaign(campaignId);

        assertThat(result).isEmpty();
    }
}
