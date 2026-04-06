package com.dndplatform.combat.adapter.inbound.encounter;

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

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterFindByCampaignResourceImplTest {

    @Mock
    private EncounterFindByCampaignDelegate delegate;

    private EncounterFindByCampaignResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterFindByCampaignResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindByCampaign(@Random Long campaignId, @Random Long userId, @Random EncounterViewModel vm1, @Random EncounterViewModel vm2) {
        var expected = List.of(vm1, vm2);
        given(delegate.findByCampaign(campaignId, userId)).willReturn(expected);

        var result = sut.findByCampaign(campaignId, userId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().findByCampaign(campaignId, userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
