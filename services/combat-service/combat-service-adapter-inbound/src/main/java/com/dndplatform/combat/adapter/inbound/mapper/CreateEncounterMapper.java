package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.EncounterCreate;
import com.dndplatform.combat.domain.model.EncounterCreateBuilder;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.domain.model.ParticipantCreateBuilder;
import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.combat.view.model.vm.CreateEncounterRequest;
import com.dndplatform.combat.view.model.vm.CreateParticipantRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class CreateEncounterMapper implements Function<CreateEncounterRequest, EncounterCreate> {

    @Override
    public EncounterCreate apply(CreateEncounterRequest request) {
        List<ParticipantCreate> participants = request.participants() != null
                ? request.participants().stream().map(this::toParticipantCreate).toList()
                : List.of();

        return EncounterCreateBuilder.builder()
                .withCampaignId(request.campaignId())
                .withCreatedByUserId(request.userId())
                .withName(request.name())
                .withDescription(request.description())
                .withPartyLevel(request.partyLevel() != null ? request.partyLevel() : 1)
                .withPartySize(request.partySize() != null ? request.partySize() : 4)
                .withParticipants(participants)
                .build();
    }

    private ParticipantCreate toParticipantCreate(CreateParticipantRequest request) {
        return ParticipantCreateBuilder.builder()
                .withName(request.name())
                .withType(ParticipantType.valueOf(request.type()))
                .withMaxHp(request.maxHp())
                .withArmorClass(request.armorClass() != null ? request.armorClass() : 10)
                .withMonsterId(request.monsterId())
                .withSourceJson(request.sourceJson())
                .build();
    }
}
