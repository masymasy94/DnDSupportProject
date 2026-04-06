package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.combat.view.model.vm.AddParticipantRequest;
import com.dndplatform.combat.view.model.vm.AddParticipantRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddParticipantMapperTest {

    private AddParticipantMapper sut;

    @BeforeEach
    void setUp() {
        sut = new AddParticipantMapper();
    }

    @Test
    void shouldMapAllFields() {
        var request = AddParticipantRequestBuilder.builder()
                .withUserId(1L)
                .withName("Goblin")
                .withType("MONSTER")
                .withMaxHp(7)
                .withArmorClass(15)
                .withMonsterId(42L)
                .withSourceJson("{\"cr\":\"1/4\"}")
                .build();

        var result = sut.apply(request);

        assertThat(result.name()).isEqualTo("Goblin");
        assertThat(result.type()).isEqualTo(ParticipantType.MONSTER);
        assertThat(result.maxHp()).isEqualTo(7);
        assertThat(result.armorClass()).isEqualTo(15);
        assertThat(result.monsterId()).isEqualTo(42L);
        assertThat(result.sourceJson()).isEqualTo("{\"cr\":\"1/4\"}");
    }

    @Test
    void shouldDefaultArmorClassTo10WhenNull() {
        var request = AddParticipantRequestBuilder.builder()
                .withUserId(1L)
                .withName("Goblin")
                .withType("MONSTER")
                .withMaxHp(7)
                .withArmorClass(null)
                .build();

        var result = sut.apply(request);

        assertThat(result.armorClass()).isEqualTo(10);
    }

    @Test
    void shouldMapPcType() {
        var request = AddParticipantRequestBuilder.builder()
                .withUserId(1L)
                .withName("Aragorn")
                .withType("PC")
                .withMaxHp(50)
                .withArmorClass(16)
                .build();

        var result = sut.apply(request);

        assertThat(result.type()).isEqualTo(ParticipantType.PC);
    }
}
