package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.vm.EncounterViewModel;
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

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class InitiativeStartResourceImplTest {

    @Mock
    private InitiativeStartDelegate delegate;

    private InitiativeStartResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new InitiativeStartResourceImpl(delegate);
    }

    @Test
    void shouldDelegateStart(@Random Long encounterId, @Random Long userId, @Random EncounterViewModel expected) {
        given(delegate.start(encounterId, userId)).willReturn(expected);

        var result = sut.start(encounterId, userId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().start(encounterId, userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
