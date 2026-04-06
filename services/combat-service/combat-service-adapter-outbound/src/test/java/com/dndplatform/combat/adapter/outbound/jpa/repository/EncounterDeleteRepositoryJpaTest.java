package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterDeleteRepositoryJpaTest {

    @Mock
    private EncounterPanacheRepository panacheRepository;

    private EncounterDeleteRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterDeleteRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldDeleteEntityWhenFound(@Random Long id, @Random EncounterEntity entity) {
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));
        willDoNothing().given(panacheRepository).delete(entity);

        sut.deleteById(id);

        then(panacheRepository).should().findByIdOptional(id);
        then(panacheRepository).should().delete(entity);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenEntityMissing(@Random Long id) {
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.deleteById(id))
                .isInstanceOf(NotFoundException.class);

        then(panacheRepository).should().findByIdOptional(id);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }
}
