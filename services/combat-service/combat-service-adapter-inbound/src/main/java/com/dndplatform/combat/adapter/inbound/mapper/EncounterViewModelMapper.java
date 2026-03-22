package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.view.model.vm.EncounterViewModel;
import com.dndplatform.combat.view.model.vm.EncounterViewModelBuilder;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class EncounterViewModelMapper implements Function<Encounter, EncounterViewModel> {

    private final ParticipantViewModelMapper participantMapper;

    @Inject
    public EncounterViewModelMapper(ParticipantViewModelMapper participantMapper) {
        this.participantMapper = participantMapper;
    }

    @Override
    public EncounterViewModel apply(Encounter encounter) {
        List<ParticipantViewModel> participants = encounter.participants() != null
                ? encounter.participants().stream().map(participantMapper).toList()
                : List.of();

        return EncounterViewModelBuilder.builder()
                .withId(encounter.id())
                .withCampaignId(encounter.campaignId())
                .withCreatedByUserId(encounter.createdByUserId())
                .withName(encounter.name())
                .withDescription(encounter.description())
                .withStatus(encounter.status() != null ? encounter.status().name() : null)
                .withPartyLevel(encounter.partyLevel())
                .withPartySize(encounter.partySize())
                .withDifficultyRating(encounter.difficultyRating() != null ? encounter.difficultyRating().name() : null)
                .withParticipants(participants)
                .withCreatedAt(encounter.createdAt())
                .withUpdatedAt(encounter.updatedAt())
                .build();
    }
}
