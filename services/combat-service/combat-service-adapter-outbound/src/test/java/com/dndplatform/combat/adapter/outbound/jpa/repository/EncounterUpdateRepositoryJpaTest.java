package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterUpdate;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterUpdateRepositoryJpaTest {

    @Mock
    private EncounterEntityMapper mapper;

    @Mock
    private EncounterPanacheRepository panacheRepository;

    private EncounterUpdateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterUpdateRepositoryJpa(mapper, panacheRepository);
    }

    @Test
    void shouldUpdateFieldsAndReturnMappedEncounter(@Random Long encounterId,
                                                     @Random Encounter expected) {
        var entity = new EncounterEntity();
        entity.participants = new ArrayList<>();
        var input = new EncounterUpdate(encounterId, "New Name", "New Desc", 5, 6, null);

        given(panacheRepository.findByIdOptional(encounterId)).willReturn(Optional.of(entity));
        willDoNothing().given(panacheRepository).persist(any(EncounterEntity.class));
        given(mapper.toEncounter(entity)).willReturn(expected);

        var result = sut.update(input);

        assertThat(result).isEqualTo(expected);
        assertThat(entity.name).isEqualTo("New Name");
        assertThat(entity.description).isEqualTo("New Desc");
        assertThat(entity.partyLevel).isEqualTo(5);
        assertThat(entity.partySize).isEqualTo(6);
        then(panacheRepository).should().findByIdOptional(encounterId);
        then(panacheRepository).should().persist(entity);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenEncounterMissing(@Random Long encounterId) {
        given(panacheRepository.findByIdOptional(encounterId)).willReturn(Optional.empty());
        var input = new EncounterUpdate(encounterId, null, null, null, null, null);

        assertThatThrownBy(() -> sut.update(input))
                .isInstanceOf(NotFoundException.class);

        then(panacheRepository).should().findByIdOptional(encounterId);
        then(panacheRepository).shouldHaveNoMoreInteractions();
    }
}
