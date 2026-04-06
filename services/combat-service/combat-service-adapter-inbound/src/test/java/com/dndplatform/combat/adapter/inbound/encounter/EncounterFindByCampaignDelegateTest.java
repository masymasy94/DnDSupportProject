package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.EncounterFindByCampaignService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
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
class EncounterFindByCampaignDelegateTest {

    @Mock
    private EncounterFindByCampaignService service;

    @Mock
    private EncounterViewModelMapper viewModelMapper;

    private EncounterFindByCampaignDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterFindByCampaignDelegate(service, viewModelMapper);
    }

    @Test
    void shouldReturnMappedEncounters(@Random Long campaignId,
                                      @Random Long userId,
                                      @Random Encounter encounter1,
                                      @Random Encounter encounter2,
                                      @Random EncounterViewModel vm1,
                                      @Random EncounterViewModel vm2) {
        List<Encounter> encounters = List.of(encounter1, encounter2);
        given(service.findByCampaign(campaignId)).willReturn(encounters);
        given(viewModelMapper.apply(encounter1)).willReturn(vm1);
        given(viewModelMapper.apply(encounter2)).willReturn(vm2);

        var result = sut.findByCampaign(campaignId, userId);

        assertThat(result).containsExactly(vm1, vm2);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).findByCampaign(campaignId);
        then(viewModelMapper).should(inOrder).apply(encounter1);
        then(viewModelMapper).should(inOrder).apply(encounter2);
    }

    @Test
    void shouldReturnEmptyListWhenNoEncounters(@Random Long campaignId, @Random Long userId) {
        given(service.findByCampaign(campaignId)).willReturn(List.of());

        var result = sut.findByCampaign(campaignId, userId);

        assertThat(result).isEmpty();
    }
}
