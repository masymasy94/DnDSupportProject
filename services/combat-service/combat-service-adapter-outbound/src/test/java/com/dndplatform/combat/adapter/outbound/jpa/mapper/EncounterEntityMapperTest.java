package com.dndplatform.combat.adapter.outbound.jpa.mapper;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.domain.model.DifficultyRating;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.model.ParticipantType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EncounterEntityMapperTest {

    private final EncounterEntityMapper sut = new EncounterEntityMapper();

    // -------------------------------------------------------------------------
    // toEncounter
    // -------------------------------------------------------------------------

    @Test
    void shouldMapEncounterEntityToDomain() {
        EncounterParticipantEntity participantEntity = buildParticipantEntity(11L, "Goblin", "MONSTER", 15, 8, 8, 13, "[\"poisoned\"]", true, 1, null, null);

        EncounterEntity entity = new EncounterEntity();
        entity.id = 1L;
        entity.campaignId = 100L;
        entity.createdByUserId = 2L;
        entity.name = "Forest Ambush";
        entity.description = "Goblins everywhere";
        entity.status = "ACTIVE";
        entity.partyLevel = 5;
        entity.partySize = 4;
        entity.difficultyRating = "MEDIUM";
        entity.createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        entity.updatedAt = LocalDateTime.of(2024, 2, 1, 10, 0);
        entity.participants = List.of(participantEntity);
        participantEntity.encounter = entity;

        Encounter result = sut.toEncounter(entity);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.campaignId()).isEqualTo(100L);
        assertThat(result.createdByUserId()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo("Forest Ambush");
        assertThat(result.description()).isEqualTo("Goblins everywhere");
        assertThat(result.status()).isEqualTo(EncounterStatus.ACTIVE);
        assertThat(result.partyLevel()).isEqualTo(5);
        assertThat(result.partySize()).isEqualTo(4);
        assertThat(result.difficultyRating()).isEqualTo(DifficultyRating.MEDIUM);
        assertThat(result.createdAt()).isEqualTo(entity.createdAt);
        assertThat(result.updatedAt()).isEqualTo(entity.updatedAt);
        assertThat(result.participants()).hasSize(1);

        EncounterParticipant participant = result.participants().get(0);
        assertThat(participant.id()).isEqualTo(11L);
        assertThat(participant.encounterId()).isEqualTo(1L);
        assertThat(participant.name()).isEqualTo("Goblin");
        assertThat(participant.type()).isEqualTo(ParticipantType.MONSTER);
        assertThat(participant.initiative()).isEqualTo(15);
        assertThat(participant.currentHp()).isEqualTo(8);
        assertThat(participant.maxHp()).isEqualTo(8);
        assertThat(participant.armorClass()).isEqualTo(13);
        assertThat(participant.conditions()).containsExactly("poisoned");
        assertThat(participant.isActive()).isTrue();
        assertThat(participant.sortOrder()).isEqualTo(1);
    }

    @Test
    void shouldMapNullStatusToNullEncounterStatus() {
        EncounterEntity entity = new EncounterEntity();
        entity.id = 2L;
        entity.campaignId = 1L;
        entity.createdByUserId = 1L;
        entity.name = "Draft Encounter";
        entity.status = null;
        entity.partyLevel = 1;
        entity.partySize = 4;
        entity.participants = List.of();

        Encounter result = sut.toEncounter(entity);

        assertThat(result.status()).isNull();
    }

    @Test
    void shouldMapNullDifficultyRatingToNull() {
        EncounterEntity entity = new EncounterEntity();
        entity.id = 3L;
        entity.campaignId = 1L;
        entity.createdByUserId = 1L;
        entity.name = "Unknown Difficulty";
        entity.status = "DRAFT";
        entity.partyLevel = 1;
        entity.partySize = 4;
        entity.difficultyRating = null;
        entity.participants = List.of();

        Encounter result = sut.toEncounter(entity);

        assertThat(result.difficultyRating()).isNull();
    }

    @Test
    void shouldDefaultPartyLevelTo1WhenNull() {
        EncounterEntity entity = new EncounterEntity();
        entity.id = 4L;
        entity.campaignId = 1L;
        entity.createdByUserId = 1L;
        entity.name = "Test";
        entity.status = "DRAFT";
        entity.partyLevel = null;
        entity.partySize = 4;
        entity.participants = List.of();

        Encounter result = sut.toEncounter(entity);

        assertThat(result.partyLevel()).isEqualTo(1);
    }

    @Test
    void shouldDefaultPartySizeTo4WhenNull() {
        EncounterEntity entity = new EncounterEntity();
        entity.id = 5L;
        entity.campaignId = 1L;
        entity.createdByUserId = 1L;
        entity.name = "Test";
        entity.status = "DRAFT";
        entity.partyLevel = 3;
        entity.partySize = null;
        entity.participants = List.of();

        Encounter result = sut.toEncounter(entity);

        assertThat(result.partySize()).isEqualTo(4);
    }

    @Test
    void shouldMapNullParticipantsToEmptyList() {
        EncounterEntity entity = new EncounterEntity();
        entity.id = 6L;
        entity.campaignId = 1L;
        entity.createdByUserId = 1L;
        entity.name = "Test";
        entity.status = "DRAFT";
        entity.partyLevel = 1;
        entity.partySize = 4;
        entity.participants = null;

        Encounter result = sut.toEncounter(entity);

        assertThat(result.participants()).isEmpty();
    }

    @Test
    void shouldMapAllEncounterStatusValues() {
        for (EncounterStatus status : EncounterStatus.values()) {
            EncounterEntity entity = new EncounterEntity();
            entity.id = 10L;
            entity.campaignId = 1L;
            entity.createdByUserId = 1L;
            entity.name = "Status Test";
            entity.status = status.name();
            entity.partyLevel = 1;
            entity.partySize = 4;
            entity.participants = List.of();

            Encounter result = sut.toEncounter(entity);

            assertThat(result.status()).isEqualTo(status);
        }
    }

    @Test
    void shouldMapAllDifficultyRatings() {
        for (DifficultyRating rating : DifficultyRating.values()) {
            EncounterEntity entity = new EncounterEntity();
            entity.id = 20L;
            entity.campaignId = 1L;
            entity.createdByUserId = 1L;
            entity.name = "Difficulty Test";
            entity.status = "DRAFT";
            entity.partyLevel = 1;
            entity.partySize = 4;
            entity.difficultyRating = rating.name();
            entity.participants = List.of();

            Encounter result = sut.toEncounter(entity);

            assertThat(result.difficultyRating()).isEqualTo(rating);
        }
    }

    // -------------------------------------------------------------------------
    // toParticipant
    // -------------------------------------------------------------------------

    @Test
    void shouldMapParticipantEntityToDomain() {
        EncounterEntity encounterEntity = new EncounterEntity();
        encounterEntity.id = 50L;

        EncounterParticipantEntity entity = buildParticipantEntity(
                99L, "Fighter", "PC", 18, 30, 40, 17, "[\"stunned\",\"blinded\"]", false, 2, null, "{\"hp\":30}");
        entity.encounter = encounterEntity;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.id()).isEqualTo(99L);
        assertThat(result.encounterId()).isEqualTo(50L);
        assertThat(result.name()).isEqualTo("Fighter");
        assertThat(result.type()).isEqualTo(ParticipantType.PC);
        assertThat(result.initiative()).isEqualTo(18);
        assertThat(result.currentHp()).isEqualTo(30);
        assertThat(result.maxHp()).isEqualTo(40);
        assertThat(result.armorClass()).isEqualTo(17);
        assertThat(result.conditions()).containsExactly("stunned", "blinded");
        assertThat(result.isActive()).isFalse();
        assertThat(result.sortOrder()).isEqualTo(2);
        assertThat(result.monsterId()).isNull();
        assertThat(result.sourceJson()).isEqualTo("{\"hp\":30}");
    }

    @Test
    void shouldMapNullEncounterToNullEncounterId() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                1L, "Orc", "MONSTER", 10, 15, 15, 13, "[]", false, 0, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.encounterId()).isNull();
    }

    @Test
    void shouldDefaultInitiativeTo0WhenNull() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                2L, "Wizard", "PC", null, 10, 10, 12, "[]", false, 0, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.initiative()).isEqualTo(0);
    }

    @Test
    void shouldDefaultCurrentHpTo0WhenNull() {
        EncounterParticipantEntity entity = new EncounterParticipantEntity();
        entity.id = 3L;
        entity.name = "Ranger";
        entity.participantType = "PC";
        entity.initiative = 12;
        entity.currentHp = null;
        entity.maxHp = 20;
        entity.armorClass = 14;
        entity.conditions = "[]";
        entity.isActive = false;
        entity.sortOrder = 0;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.currentHp()).isEqualTo(0);
    }

    @Test
    void shouldDefaultMaxHpTo0WhenNull() {
        EncounterParticipantEntity entity = new EncounterParticipantEntity();
        entity.id = 4L;
        entity.name = "Rogue";
        entity.participantType = "PC";
        entity.initiative = 12;
        entity.currentHp = 10;
        entity.maxHp = null;
        entity.armorClass = 14;
        entity.conditions = "[]";
        entity.isActive = false;
        entity.sortOrder = 0;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.maxHp()).isEqualTo(0);
    }

    @Test
    void shouldDefaultArmorClassTo10WhenNull() {
        EncounterParticipantEntity entity = new EncounterParticipantEntity();
        entity.id = 5L;
        entity.name = "Cleric";
        entity.participantType = "PC";
        entity.initiative = 8;
        entity.currentHp = 20;
        entity.maxHp = 20;
        entity.armorClass = null;
        entity.conditions = "[]";
        entity.isActive = false;
        entity.sortOrder = 0;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.armorClass()).isEqualTo(10);
    }

    @Test
    void shouldDefaultSortOrderTo0WhenNull() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                6L, "Druid", "PC", 10, 18, 18, 11, "[]", true, null, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.sortOrder()).isEqualTo(0);
    }

    @Test
    void shouldDefaultIsActiveToFalseWhenNull() {
        EncounterParticipantEntity entity = new EncounterParticipantEntity();
        entity.id = 7L;
        entity.name = "Barbarian";
        entity.participantType = "PC";
        entity.initiative = 14;
        entity.currentHp = 25;
        entity.maxHp = 25;
        entity.armorClass = 15;
        entity.conditions = "[]";
        entity.isActive = null;
        entity.sortOrder = 1;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.isActive()).isFalse();
    }

    @Test
    void shouldMapNullParticipantTypeToNull() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                8L, "Unknown", null, 10, 10, 10, 10, "[]", false, 0, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.type()).isNull();
    }

    @Test
    void shouldMapMonsterWithMonsterId() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                9L, "Dragon", "MONSTER", 20, 200, 200, 18, "[]", true, 0, 42L, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.monsterId()).isEqualTo(42L);
        assertThat(result.type()).isEqualTo(ParticipantType.MONSTER);
    }

    // -------------------------------------------------------------------------
    // parseConditions / serializeConditions
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnEmptyListForNullConditions() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                10L, "Test", "PC", 5, 10, 10, 10, null, false, 0, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.conditions()).isEmpty();
    }

    @Test
    void shouldReturnEmptyListForEmptyJsonArray() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                11L, "Test", "PC", 5, 10, 10, 10, "[]", false, 0, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.conditions()).isEmpty();
    }

    @Test
    void shouldReturnEmptyListForBlankConditions() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                12L, "Test", "PC", 5, 10, 10, 10, "  ", false, 0, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.conditions()).isEmpty();
    }

    @Test
    void shouldParseSingleCondition() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                13L, "Test", "PC", 5, 10, 10, 10, "[\"poisoned\"]", false, 0, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.conditions()).containsExactly("poisoned");
    }

    @Test
    void shouldParseMultipleConditions() {
        EncounterParticipantEntity entity = buildParticipantEntity(
                14L, "Test", "PC", 5, 10, 10, 10, "[\"stunned\",\"blinded\",\"poisoned\"]", false, 0, null, null);
        entity.encounter = null;

        EncounterParticipant result = sut.toParticipant(entity);

        assertThat(result.conditions()).containsExactly("stunned", "blinded", "poisoned");
    }

    @Test
    void shouldSerializeNullConditionsToEmptyArray() {
        String result = sut.serializeConditions(null);

        assertThat(result).isEqualTo("[]");
    }

    @Test
    void shouldSerializeEmptyConditionsToEmptyArray() {
        String result = sut.serializeConditions(List.of());

        assertThat(result).isEqualTo("[]");
    }

    @Test
    void shouldSerializeSingleCondition() {
        String result = sut.serializeConditions(List.of("poisoned"));

        assertThat(result).isEqualTo("[\"poisoned\"]");
    }

    @Test
    void shouldSerializeMultipleConditions() {
        String result = sut.serializeConditions(List.of("stunned", "blinded"));

        assertThat(result).isEqualTo("[\"stunned\",\"blinded\"]");
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private EncounterParticipantEntity buildParticipantEntity(
            Long id, String name, String participantType,
            Integer initiative, Integer currentHp, Integer maxHp, Integer armorClass,
            String conditions, Boolean isActive, Integer sortOrder,
            Long monsterId, String sourceJson) {
        EncounterParticipantEntity entity = new EncounterParticipantEntity();
        entity.id = id;
        entity.name = name;
        entity.participantType = participantType;
        entity.initiative = initiative;
        entity.currentHp = currentHp;
        entity.maxHp = maxHp;
        entity.armorClass = armorClass;
        entity.conditions = conditions;
        entity.isActive = isActive;
        entity.sortOrder = sortOrder;
        entity.monsterId = monsterId;
        entity.sourceJson = sourceJson;
        return entity;
    }
}
