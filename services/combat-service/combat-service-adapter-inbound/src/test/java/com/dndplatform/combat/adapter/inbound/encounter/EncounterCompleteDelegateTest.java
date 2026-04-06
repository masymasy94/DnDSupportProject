package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.EncounterCompleteService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterCompleteDelegateTest {

    @Mock
    private EncounterCompleteService service;

    @Mock
    private EncounterViewModelMapper viewModelMapper;

    private EncounterCompleteDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterCompleteDelegate(service, viewModelMapper);
    }

    @Test
    void shouldDelegateToService(@Random Long encounterId,
                                 @Random Long userId,
                                 @Random Encounter encounter,
                                 @Random EncounterViewModel expected) {
        given(service.complete(encounterId, userId)).willReturn(encounter);
        given(viewModelMapper.apply(encounter)).willReturn(expected);

        var actual = sut.complete(encounterId, userId);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).complete(encounterId, userId);
        then(viewModelMapper).should(inOrder).apply(encounter);
    }

    @Test
    void shouldThrowWhenEncounterNotFound(@Random Long encounterId, @Random Long userId) {
        willThrow(new NotFoundException("Encounter not found"))
                .given(service).complete(encounterId, userId);

        assertThatThrownBy(() -> sut.complete(encounterId, userId))
                .isInstanceOf(NotFoundException.class);
    }
}
