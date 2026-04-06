package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.view.model.vm.UpdateParticipantRequest;
import com.dndplatform.combat.view.model.vm.UpdateParticipantRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateParticipantMapperTest {

    private UpdateParticipantMapper sut;

    @BeforeEach
    void setUp() {
        sut = new UpdateParticipantMapper();
    }

    @Test
    void shouldMapAllFields() {
        var request = UpdateParticipantRequestBuilder.builder()
                .withUserId(1L)
                .withCurrentHp(5)
                .withConditions(List.of("Poisoned", "Stunned"))
                .withIsActive(true)
                .build();

        var result = sut.apply(request);

        assertThat(result.currentHp()).isEqualTo(5);
        assertThat(result.conditions()).containsExactly("Poisoned", "Stunned");
        assertThat(result.isActive()).isTrue();
    }

    @Test
    void shouldMapNullFields() {
        var request = UpdateParticipantRequestBuilder.builder()
                .withUserId(1L)
                .withCurrentHp(null)
                .withConditions(null)
                .withIsActive(null)
                .build();

        var result = sut.apply(request);

        assertThat(result.currentHp()).isNull();
        assertThat(result.conditions()).isNull();
        assertThat(result.isActive()).isNull();
    }
}
