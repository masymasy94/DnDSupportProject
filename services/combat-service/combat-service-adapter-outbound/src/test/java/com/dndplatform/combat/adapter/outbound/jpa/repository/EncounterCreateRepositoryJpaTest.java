package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterCreate;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterCreateRepositoryJpaTest {

    @Mock
    private EncounterEntityMapper mapper;

    @Mock
    private EncounterPanacheRepository panacheRepository;

    private EncounterCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterCreateRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void shouldPersistEntityAndReturnMappedEncounter(@Random Encounter expected) {
        willDoNothing().given(panacheRepository).persist(any(EncounterEntity.class));
        given(mapper.toEncounter(any(EncounterEntity.class))).willReturn(expected);

        var input = new EncounterCreate(1L, 1L, "Test Encounter", "Desc", 5, 4, List.of());

        var result = sut.save(input);

        assertThat(result).isEqualTo(expected);
        then(panacheRepository).should().persist(any(EncounterEntity.class));
        then(mapper).should().toEncounter(any(EncounterEntity.class));
    }
}
