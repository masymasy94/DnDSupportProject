package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.combat.view.model.vm.UpdateEncounterRequest;
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
class EncounterUpdateResourceImplTest {

    @Mock
    private EncounterUpdateDelegate delegate;

    private EncounterUpdateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterUpdateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpdate(@Random Long id, @Random UpdateEncounterRequest request, @Random EncounterViewModel expected) {
        given(delegate.update(id, request)).willReturn(expected);

        var result = sut.update(id, request);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().update(id, request);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
