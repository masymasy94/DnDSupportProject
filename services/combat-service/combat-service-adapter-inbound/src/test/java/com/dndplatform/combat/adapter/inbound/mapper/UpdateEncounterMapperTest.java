package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.view.model.vm.CreateParticipantRequest;
import com.dndplatform.combat.view.model.vm.UpdateEncounterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateEncounterMapperTest {

    private UpdateEncounterMapper sut;

    @BeforeEach
    void setUp() {
        sut = new UpdateEncounterMapper();
    }

    @Test
    void shouldMapAllFields() {
        var participant = new CreateParticipantRequest("Goblin", "MONSTER", 7, 15, null, null);
        var request = new UpdateEncounterRequest(1L, "Updated Name", "Updated desc", 5, 6, List.of(participant));

        var result = sut.apply(request);

        assertThat(result.name()).isEqualTo("Updated Name");
        assertThat(result.description()).isEqualTo("Updated desc");
        assertThat(result.partyLevel()).isEqualTo(5);
        assertThat(result.partySize()).isEqualTo(6);
        assertThat(result.participants()).hasSize(1);
        assertThat(result.participants().get(0).name()).isEqualTo("Goblin");
    }

    @Test
    void shouldReturnNullParticipantsWhenNull() {
        var request = new UpdateEncounterRequest(1L, "Test", null, null, null, null);

        var result = sut.apply(request);

        assertThat(result.participants()).isNull();
    }
}
