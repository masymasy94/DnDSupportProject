package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantSetInitiativeRepositoryJpaTest {

    @Mock
    private EncounterPanacheRepository encounterRepository;

    private ParticipantSetInitiativeRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantSetInitiativeRepositoryJpa(encounterRepository);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenEncounterMissing(@Random Long encounterId) {
        given(encounterRepository.findByIdOptional(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.setInitiatives(encounterId, Map.of()))
                .isInstanceOf(NotFoundException.class);

        then(encounterRepository).should().findByIdOptional(encounterId);
        then(encounterRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldSetInitiativesAndSortParticipants(@Random Long encounterId) {
        var pe1 = new EncounterParticipantEntity();
        pe1.id = 1L;
        pe1.initiative = 0;

        var pe2 = new EncounterParticipantEntity();
        pe2.id = 2L;
        pe2.initiative = 0;

        var encounter = new EncounterEntity();
        encounter.participants = new ArrayList<>();
        encounter.participants.add(pe1);
        encounter.participants.add(pe2);

        given(encounterRepository.findByIdOptional(encounterId)).willReturn(Optional.of(encounter));
        willDoNothing().given(encounterRepository).persist(any(EncounterEntity.class));

        // pe2 gets higher initiative
        sut.setInitiatives(encounterId, Map.of(1L, 5, 2L, 20));

        // After sort by initiative descending, pe2 should be first (sortOrder=0, isActive=true)
        assertThat(encounter.participants.get(0).id).isEqualTo(2L);
        assertThat(encounter.participants.get(0).isActive).isTrue();
        assertThat(encounter.participants.get(0).sortOrder).isEqualTo(0);
        assertThat(encounter.participants.get(1).id).isEqualTo(1L);
        assertThat(encounter.participants.get(1).isActive).isFalse();
        assertThat(encounter.participants.get(1).sortOrder).isEqualTo(1);
        then(encounterRepository).should().persist(encounter);
    }
}
