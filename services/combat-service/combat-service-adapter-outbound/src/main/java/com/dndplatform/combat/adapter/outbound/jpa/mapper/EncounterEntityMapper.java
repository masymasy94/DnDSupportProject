package com.dndplatform.combat.adapter.outbound.jpa.mapper;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EncounterEntityMapper {

    public Encounter toEncounter(EncounterEntity entity) {
        List<EncounterParticipant> participants = entity.participants != null
                ? entity.participants.stream().map(this::toParticipant).toList()
                : List.of();

        return EncounterBuilder.builder()
                .withId(entity.id)
                .withCampaignId(entity.campaignId)
                .withCreatedByUserId(entity.createdByUserId)
                .withName(entity.name)
                .withDescription(entity.description)
                .withStatus(entity.status != null ? EncounterStatus.valueOf(entity.status) : null)
                .withPartyLevel(entity.partyLevel != null ? entity.partyLevel : 1)
                .withPartySize(entity.partySize != null ? entity.partySize : 4)
                .withDifficultyRating(entity.difficultyRating != null ? DifficultyRating.valueOf(entity.difficultyRating) : null)
                .withParticipants(participants)
                .withCreatedAt(entity.createdAt)
                .withUpdatedAt(entity.updatedAt)
                .build();
    }

    public EncounterParticipant toParticipant(EncounterParticipantEntity entity) {
        return EncounterParticipantBuilder.builder()
                .withId(entity.id)
                .withEncounterId(entity.encounter != null ? entity.encounter.id : null)
                .withName(entity.name)
                .withType(entity.participantType != null ? ParticipantType.valueOf(entity.participantType) : null)
                .withInitiative(entity.initiative != null ? entity.initiative : 0)
                .withCurrentHp(entity.currentHp != null ? entity.currentHp : 0)
                .withMaxHp(entity.maxHp != null ? entity.maxHp : 0)
                .withArmorClass(entity.armorClass != null ? entity.armorClass : 10)
                .withConditions(parseConditions(entity.conditions))
                .withIsActive(entity.isActive != null && entity.isActive)
                .withSortOrder(entity.sortOrder != null ? entity.sortOrder : 0)
                .withMonsterId(entity.monsterId)
                .withSourceJson(entity.sourceJson)
                .build();
    }

    private List<String> parseConditions(String json) {
        if (json == null || json.isBlank() || "[]".equals(json.trim())) return List.of();
        String inner = json.trim();
        if (inner.startsWith("[")) inner = inner.substring(1);
        if (inner.endsWith("]")) inner = inner.substring(0, inner.length() - 1);
        if (inner.isBlank()) return List.of();
        return Arrays.stream(inner.split(","))
                .map(s -> s.trim().replace("\"", ""))
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public String serializeConditions(List<String> conditions) {
        if (conditions == null || conditions.isEmpty()) return "[]";
        return "[" + conditions.stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(",")) + "]";
    }
}
