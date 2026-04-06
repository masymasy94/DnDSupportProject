package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.EncounterUpdate;
import com.dndplatform.combat.domain.model.EncounterUpdateBuilder;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.domain.model.ParticipantCreateBuilder;
import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.combat.view.model.vm.CreateParticipantRequest;
import com.dndplatform.combat.view.model.vm.UpdateEncounterRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class UpdateEncounterMapper implements Function<UpdateEncounterRequest, EncounterUpdate> {

    @Override
    public EncounterUpdate apply(UpdateEncounterRequest request) {
        List<ParticipantCreate> participants = request.participants() != null
                ? request.participants().stream().map(this::toParticipantCreate).toList()
                : null;

        return EncounterUpdateBuilder.builder()
                .withName(request.name())
                .withDescription(request.description())
                .withPartyLevel(request.partyLevel())
                .withPartySize(request.partySize())
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
