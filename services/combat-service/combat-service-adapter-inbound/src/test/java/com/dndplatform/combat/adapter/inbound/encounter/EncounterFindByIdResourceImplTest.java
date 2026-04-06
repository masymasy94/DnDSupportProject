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
class EncounterFindByIdResourceImplTest {

    @Mock
    private EncounterFindByIdDelegate delegate;

    private EncounterFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterFindByIdResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindById(@Random Long id, @Random EncounterViewModel expected) {
        given(delegate.findById(id)).willReturn(expected);

        var result = sut.findById(id);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().findById(id);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
