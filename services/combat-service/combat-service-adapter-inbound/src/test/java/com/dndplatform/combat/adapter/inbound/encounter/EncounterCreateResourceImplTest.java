package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.vm.CreateEncounterRequest;
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
class EncounterCreateResourceImplTest {

    @Mock
    private EncounterCreateDelegate delegate;

    private EncounterCreateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterCreateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCreate(@Random CreateEncounterRequest request, @Random EncounterViewModel expected) {
        given(delegate.create(request)).willReturn(expected);

        var result = sut.create(request);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().create(request);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
