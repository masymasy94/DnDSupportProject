package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.domain.model.ParticipantCreateBuilder;
import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.combat.view.model.vm.AddParticipantRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class AddParticipantMapper implements Function<AddParticipantRequest, ParticipantCreate> {

    @Override
    public ParticipantCreate apply(AddParticipantRequest request) {
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
