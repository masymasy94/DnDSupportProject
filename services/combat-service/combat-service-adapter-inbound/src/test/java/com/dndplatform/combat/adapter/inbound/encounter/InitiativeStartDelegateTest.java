package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.adapter.inbound.mapper.EncounterViewModelMapper;
import com.dndplatform.combat.domain.InitiativeStartService;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.common.exception.ForbiddenException;
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
class InitiativeStartDelegateTest {

    @Mock
    private InitiativeStartService service;

    @Mock
    private EncounterViewModelMapper viewModelMapper;

    private InitiativeStartDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new InitiativeStartDelegate(service, viewModelMapper);
    }

    @Test
    void shouldDelegateToService(@Random Long encounterId,
                                 @Random Long userId,
                                 @Random Encounter encounter,
                                 @Random EncounterViewModel expected) {
        given(service.start(encounterId, userId)).willReturn(encounter);
        given(viewModelMapper.apply(encounter)).willReturn(expected);

        var actual = sut.start(encounterId, userId);

        assertThat(actual).isEqualTo(expected);

        var inOrder = inOrder(service, viewModelMapper);
        then(service).should(inOrder).start(encounterId, userId);
        then(viewModelMapper).should(inOrder).apply(encounter);
    }

    @Test
    void shouldThrowWhenUserNotAuthorized(@Random Long encounterId, @Random Long userId) {
        willThrow(new ForbiddenException("Not the DM"))
                .given(service).start(encounterId, userId);

        assertThatThrownBy(() -> sut.start(encounterId, userId))
                .isInstanceOf(ForbiddenException.class);
    }
}
