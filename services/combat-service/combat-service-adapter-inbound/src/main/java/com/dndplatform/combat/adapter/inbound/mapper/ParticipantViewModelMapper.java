package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.combat.view.model.vm.ParticipantViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class ParticipantViewModelMapper implements Function<EncounterParticipant, ParticipantViewModel> {

    @Override
    public ParticipantViewModel apply(EncounterParticipant participant) {
        return ParticipantViewModelBuilder.builder()
                .withId(participant.id())
                .withEncounterId(participant.encounterId())
                .withName(participant.name())
                .withType(participant.type() != null ? participant.type().name() : null)
                .withInitiative(participant.initiative())
                .withCurrentHp(participant.currentHp())
                .withMaxHp(participant.maxHp())
                .withArmorClass(participant.armorClass())
                .withConditions(participant.conditions())
                .withIsActive(participant.isActive())
                .withSortOrder(participant.sortOrder())
                .withMonsterId(participant.monsterId())
                .withSourceJson(participant.sourceJson())
                .build();
    }
}
