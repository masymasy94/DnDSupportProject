package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterBuilder;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.EncounterParticipantBuilder;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.model.DifficultyRating;
import com.dndplatform.combat.domain.model.ParticipantType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EncounterViewModelMapperTest {

    private EncounterViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterViewModelMapper(new ParticipantViewModelMapper());
    }

    @Test
    void shouldMapAllFields() {
        var now = LocalDateTime.now();
        var participant = EncounterParticipantBuilder.builder()
                .withId(1L).withEncounterId(10L).withName("Goblin")
                .withType(ParticipantType.MONSTER).withInitiative(12).withCurrentHp(7)
                .withMaxHp(7).withArmorClass(15).withConditions(List.of())
                .withIsActive(true).withSortOrder(1).build();

        var encounter = EncounterBuilder.builder()
                .withId(10L)
                .withCampaignId(5L)
                .withCreatedByUserId(1L)
                .withName("Goblin Ambush")
                .withDescription("An ambush")
                .withStatus(EncounterStatus.ACTIVE)
                .withPartyLevel(3)
                .withPartySize(4)
                .withDifficultyRating(DifficultyRating.MEDIUM)
                .withParticipants(List.of(participant))
                .withCreatedAt(now)
                .withUpdatedAt(now)
                .build();

        var result = sut.apply(encounter);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.campaignId()).isEqualTo(5L);
        assertThat(result.createdByUserId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Goblin Ambush");
        assertThat(result.description()).isEqualTo("An ambush");
        assertThat(result.status()).isEqualTo(EncounterStatus.ACTIVE.name());
        assertThat(result.partyLevel()).isEqualTo(3);
        assertThat(result.partySize()).isEqualTo(4);
        assertThat(result.difficultyRating()).isEqualTo(DifficultyRating.MEDIUM.name());
        assertThat(result.participants()).hasSize(1);
        assertThat(result.createdAt()).isEqualTo(now);
        assertThat(result.updatedAt()).isEqualTo(now);
    }

    @Test
    void shouldMapNullStatusAndRatingToNull() {
        var encounter = EncounterBuilder.builder()
                .withId(1L).withCampaignId(1L).withCreatedByUserId(1L)
                .withName("Test").withStatus(null).withDifficultyRating(null)
                .withPartyLevel(1).withPartySize(4).withParticipants(null)
                .build();

        var result = sut.apply(encounter);

        assertThat(result.status()).isNull();
        assertThat(result.difficultyRating()).isNull();
        assertThat(result.participants()).isEmpty();
    }
}
