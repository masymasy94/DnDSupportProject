package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.ParticipantUpdate;
import com.dndplatform.combat.domain.model.ParticipantUpdateBuilder;
import com.dndplatform.combat.view.model.vm.UpdateParticipantRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class UpdateParticipantMapper implements Function<UpdateParticipantRequest, ParticipantUpdate> {

    @Override
    public ParticipantUpdate apply(UpdateParticipantRequest request) {
        return ParticipantUpdateBuilder.builder()
                .withCurrentHp(request.currentHp())
                .withConditions(request.conditions())
                .withIsActive(request.isActive())
                .build();
    }
}
