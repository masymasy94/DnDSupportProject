package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterFindByIdRepositoryJpaTest {

    @Mock
    private EncounterEntityMapper mapper;

    @Mock
    private EncounterPanacheRepository panacheRepository;

    private EncounterFindByIdRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterFindByIdRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void shouldReturnMappedEncounterWhenFound(@Random Long id,
                                              @Random EncounterEntity entity,
                                              @Random Encounter expected) {
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));
        given(mapper.toEncounter(entity)).willReturn(expected);

        var result = sut.findById(id);

        assertThat(result).isPresent().contains(expected);
        then(panacheRepository).should().findByIdOptional(id);
        then(mapper).should().toEncounter(entity);
    }

    @Test
    void shouldReturnEmptyWhenNotFound(@Random Long id) {
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

        var result = sut.findById(id);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}
