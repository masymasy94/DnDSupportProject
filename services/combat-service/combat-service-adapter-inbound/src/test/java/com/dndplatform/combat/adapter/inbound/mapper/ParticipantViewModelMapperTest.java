package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.EncounterParticipantBuilder;
import com.dndplatform.combat.domain.model.ParticipantType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParticipantViewModelMapperTest {

    private ParticipantViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantViewModelMapper();
    }

    @Test
    void shouldMapAllFields() {
        var participant = EncounterParticipantBuilder.builder()
                .withId(1L)
                .withEncounterId(10L)
                .withName("Goblin")
                .withType(ParticipantType.MONSTER)
                .withInitiative(14)
                .withCurrentHp(5)
                .withMaxHp(7)
                .withArmorClass(15)
                .withConditions(List.of("Poisoned"))
                .withIsActive(true)
                .withSortOrder(1)
                .withMonsterId(42L)
                .withSourceJson("{\"cr\":\"1/4\"}")
                .build();

        var result = sut.apply(participant);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.encounterId()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("Goblin");
        assertThat(result.type()).isEqualTo(ParticipantType.MONSTER.name());
        assertThat(result.initiative()).isEqualTo(14);
        assertThat(result.currentHp()).isEqualTo(5);
        assertThat(result.maxHp()).isEqualTo(7);
        assertThat(result.armorClass()).isEqualTo(15);
        assertThat(result.conditions()).containsExactly("Poisoned");
        assertThat(result.isActive()).isTrue();
        assertThat(result.sortOrder()).isEqualTo(1);
        assertThat(result.monsterId()).isEqualTo(42L);
        assertThat(result.sourceJson()).isEqualTo("{\"cr\":\"1/4\"}");
    }

    @Test
    void shouldMapNullTypeToNull() {
        var participant = EncounterParticipantBuilder.builder()
                .withId(1L)
                .withEncounterId(10L)
                .withName("Unknown")
                .withType(null)
                .withInitiative(0)
                .withCurrentHp(10)
                .withMaxHp(10)
                .withArmorClass(10)
                .withConditions(List.of())
                .withIsActive(false)
                .withSortOrder(0)
                .build();

        var result = sut.apply(participant);

        assertThat(result.type()).isNull();
    }
}
