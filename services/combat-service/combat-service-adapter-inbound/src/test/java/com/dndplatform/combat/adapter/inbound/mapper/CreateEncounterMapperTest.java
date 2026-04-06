package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.combat.view.model.vm.CreateEncounterRequest;
import com.dndplatform.combat.view.model.vm.CreateParticipantRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateEncounterMapperTest {

    private CreateEncounterMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CreateEncounterMapper();
    }

    @Test
    void shouldMapAllFieldsWithParticipants() {
        var participant = new CreateParticipantRequest("Goblin", "MONSTER", 7, 15, null, null);
        var request = new CreateEncounterRequest(10L, 1L, "Goblin Ambush", "A goblin ambush", 3, 4, List.of(participant));

        var result = sut.apply(request);

        assertThat(result.campaignId()).isEqualTo(10L);
        assertThat(result.createdByUserId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Goblin Ambush");
        assertThat(result.description()).isEqualTo("A goblin ambush");
        assertThat(result.partyLevel()).isEqualTo(3);
        assertThat(result.partySize()).isEqualTo(4);
        assertThat(result.participants()).hasSize(1);
        assertThat(result.participants().get(0).name()).isEqualTo("Goblin");
        assertThat(result.participants().get(0).type()).isEqualTo(ParticipantType.MONSTER);
        assertThat(result.participants().get(0).maxHp()).isEqualTo(7);
        assertThat(result.participants().get(0).armorClass()).isEqualTo(15);
    }

    @Test
    void shouldDefaultPartyLevelAndSizeWhenNull() {
        var request = new CreateEncounterRequest(10L, 1L, "Test", null, null, null, null);

        var result = sut.apply(request);

        assertThat(result.partyLevel()).isEqualTo(1);
        assertThat(result.partySize()).isEqualTo(4);
        assertThat(result.participants()).isEmpty();
    }

    @Test
    void shouldDefaultParticipantArmorClassTo10WhenNull() {
        var participant = new CreateParticipantRequest("Goblin", "MONSTER", 7, null, null, null);
        var request = new CreateEncounterRequest(10L, 1L, "Test", null, 1, 4, List.of(participant));

        var result = sut.apply(request);

        assertThat(result.participants().get(0).armorClass()).isEqualTo(10);
    }
}
