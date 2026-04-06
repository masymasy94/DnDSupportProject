package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.ParticipantViewModelMapper;
import com.dndplatform.combat.adapter.inbound.mapper.UpdateParticipantMapper;
import com.dndplatform.combat.domain.ParticipantUpdateService;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantUpdate;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.combat.view.model.vm.UpdateParticipantRequest;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantUpdateDelegateTest {

    @Mock
    private ParticipantUpdateService service;

    @Mock
    private UpdateParticipantMapper updateMapper;

    @Mock
    private ParticipantViewModelMapper viewModelMapper;

    private ParticipantUpdateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantUpdateDelegate(service, updateMapper, viewModelMapper);
    }

    @Test
    void shouldDelegateToService(@Random Long encounterId,
                                 @Random Long participantId,
                                 @Random UpdateParticipantRequest request,
                                 @Random ParticipantUpdate baseUpdate,
                                 @Random EncounterParticipant participant,
                                 @Random ParticipantViewModel expected) {
        given(updateMapper.apply(request)).willReturn(baseUpdate);
        given(service.update(eq(encounterId), eq(request.userId()), any(ParticipantUpdate.class))).willReturn(participant);
        given(viewModelMapper.apply(participant)).willReturn(expected);

        var actual = sut.update(encounterId, participantId, request);

        assertThat(actual).isEqualTo(expected);

        ArgumentCaptor<ParticipantUpdate> captor = ArgumentCaptor.forClass(ParticipantUpdate.class);
        then(service).should().update(eq(encounterId), eq(request.userId()), captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(participantId);
        then(viewModelMapper).should().apply(participant);
    }
}
