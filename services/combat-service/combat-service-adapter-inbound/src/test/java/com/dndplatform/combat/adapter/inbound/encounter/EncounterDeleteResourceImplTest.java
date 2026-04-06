package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterDeleteResourceImplTest {

    @Mock
    private EncounterDeleteDelegate delegate;

    private EncounterDeleteResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterDeleteResourceImpl(delegate);
    }

    @Test
    void shouldDelegateDelete(@Random Long id, @Random Long userId) {
        willDoNothing().given(delegate).delete(id, userId);

        sut.delete(id, userId);

        then(delegate).should().delete(id, userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
