package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterUpdateStatusRepositoryJpaTest {

    @Mock
    private EncounterPanacheRepository panacheRepository;

    private EncounterUpdateStatusRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterUpdateStatusRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldUpdateStatusWhenEncounterFound(@Random Long id) {
        var entity = new EncounterEntity();
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));
        willDoNothing().given(panacheRepository).persist(any(EncounterEntity.class));

        sut.updateStatus(id, EncounterStatus.ACTIVE);

        assertThat(entity.status).isEqualTo("ACTIVE");
        then(panacheRepository).should().findByIdOptional(id);
        then(panacheRepository).should().persist(entity);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenEncounterMissing(@Random Long id) {
        given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.updateStatus(id, EncounterStatus.ACTIVE))
                .isInstanceOf(NotFoundException.class);

        then(panacheRepository).should().findByIdOptional(id);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }
}
