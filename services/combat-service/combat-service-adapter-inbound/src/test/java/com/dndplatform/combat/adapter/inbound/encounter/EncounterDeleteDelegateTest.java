package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.domain.EncounterDeleteService;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterDeleteDelegateTest {

    @Mock
    private EncounterDeleteService service;

    private EncounterDeleteDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterDeleteDelegate(service);
    }

    @Test
    void shouldDelegateToService(@Random Long id, @Random Long userId) {
        willDoNothing().given(service).delete(id, userId);

        sut.delete(id, userId);

        var inOrder = inOrder(service);
        then(service).should(inOrder).delete(id, userId);
    }

    @Test
    void shouldThrowWhenUserNotOwner(@Random Long id, @Random Long userId) {
        willThrow(new ForbiddenException("Not allowed"))
                .given(service).delete(id, userId);

        assertThatThrownBy(() -> sut.delete(id, userId))
                .isInstanceOf(ForbiddenException.class);
    }
}
