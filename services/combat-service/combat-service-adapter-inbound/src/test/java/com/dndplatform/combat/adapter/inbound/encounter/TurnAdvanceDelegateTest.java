package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.ParticipantViewModelMapper;
import com.dndplatform.combat.domain.TurnAdvanceService;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
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
class TurnAdvanceDelegateTest {

    @Mock
    private TurnAdvanceService service;

    @Mock
    private ParticipantViewModelMapper viewModelMapper;

    private TurnAdvanceDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new TurnAdvanceDelegate(service, viewModelMapper);
    }

    @Test
    void shouldReturnMappedParticipants(@Random Long encounterId,
                                        @Random Long userId,
                                        @Random EncounterParticipant p1,
                                        @Random EncounterParticipant p2,
                                        @Random ParticipantViewModel vm1,
                                        @Random ParticipantViewModel vm2) {
        List<EncounterParticipant> participants = List.of(p1, p2);
        given(service.advance(encounterId, userId)).willReturn(participants);
        given(viewModelMapper.apply(p1)).willReturn(vm1);
        given(viewModelMapper.apply(p2)).willReturn(vm2);

        var result = sut.advance(encounterId, userId);

        assertThat(result).containsExactly(vm1, vm2);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).advance(encounterId, userId);
        then(viewModelMapper).should(inOrder).apply(p1);
        then(viewModelMapper).should(inOrder).apply(p2);
    }
}
