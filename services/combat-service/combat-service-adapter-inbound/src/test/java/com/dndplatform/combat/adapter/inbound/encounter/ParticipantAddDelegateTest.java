package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.AddParticipantMapper;
import com.dndplatform.combat.adapter.inbound.mapper.ParticipantViewModelMapper;
import com.dndplatform.combat.domain.ParticipantAddService;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.view.model.vm.AddParticipantRequest;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantAddDelegateTest {

    @Mock
    private ParticipantAddService service;

    @Mock
    private AddParticipantMapper addMapper;

    @Mock
    private ParticipantViewModelMapper viewModelMapper;

    private ParticipantAddDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantAddDelegate(service, addMapper, viewModelMapper);
    }

    @Test
    void shouldDelegateToService(@Random Long encounterId,
                                 @Random AddParticipantRequest request,
                                 @Random ParticipantCreate create,
                                 @Random EncounterParticipant participant,
                                 @Random ParticipantViewModel expected) {
        given(addMapper.apply(request)).willReturn(create);
        given(service.add(encounterId, request.userId(), create)).willReturn(participant);
        given(viewModelMapper.apply(participant)).willReturn(expected);

        var actual = sut.add(encounterId, request);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(addMapper, service, viewModelMapper);
        then(addMapper).should(inOrder).apply(request);
        then(service).should(inOrder).add(encounterId, request.userId(), create);
        then(viewModelMapper).should(inOrder).apply(participant);
    }
}
