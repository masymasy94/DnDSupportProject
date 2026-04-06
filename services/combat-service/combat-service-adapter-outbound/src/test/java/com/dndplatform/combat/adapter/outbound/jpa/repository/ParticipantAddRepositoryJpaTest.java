package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantAddRepositoryJpaTest {

    @Mock
    private EncounterEntityMapper mapper;

    @Mock
    private EncounterPanacheRepository encounterRepository;

    private ParticipantAddRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantAddRepositoryJpa(mapper, encounterRepository);
    }

    @Test
    void shouldAddParticipantToEncounterAndReturnMapped(@Random Long encounterId,
                                                         @Random EncounterParticipant expected) {
        var encounter = new EncounterEntity();
        encounter.participants = new ArrayList<>();
        var input = new ParticipantCreate("Goblin", ParticipantType.MONSTER, 10, 10, null, null);

        given(encounterRepository.findByIdOptional(encounterId)).willReturn(Optional.of(encounter));
        willDoNothing().given(encounterRepository).persist(any(EncounterEntity.class));
        given(mapper.toParticipant(any(EncounterParticipantEntity.class))).willReturn(expected);

        var result = sut.add(encounterId, input);

        assertThat(result).isEqualTo(expected);
        assertThat(encounter.participants).hasSize(1);
        then(encounterRepository).should().findByIdOptional(encounterId);
        then(encounterRepository).should().persist(encounter);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenEncounterMissing(@Random Long encounterId) {
        given(encounterRepository.findByIdOptional(encounterId)).willReturn(Optional.empty());
        var input = new ParticipantCreate("Goblin", ParticipantType.MONSTER, 10, 10, null, null);

        assertThatThrownBy(() -> sut.add(encounterId, input))
                .isInstanceOf(NotFoundException.class);

        then(encounterRepository).should().findByIdOptional(encounterId);
        then(encounterRepository).shouldHaveNoMoreInteractions();
    }
}
