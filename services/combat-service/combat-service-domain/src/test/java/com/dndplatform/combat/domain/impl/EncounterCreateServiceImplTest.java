package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.EncounterCreateService;
import com.dndplatform.combat.domain.EncounterCreateServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterCreate;
import com.dndplatform.combat.domain.repository.EncounterCreateRepository;
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
class EncounterCreateServiceImplTest {

    @Mock
    private EncounterCreateRepository repository;

    private EncounterCreateService sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterCreateServiceImpl(repository);
    }

    @Test
    void shouldCreateEncounter(@Random EncounterCreate input, @Random Encounter expected) {
        given(repository.save(input)).willReturn(expected);

        Encounter result = sut.create(input);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).save(input);
        inOrder.verifyNoMoreInteractions();
    }
}
