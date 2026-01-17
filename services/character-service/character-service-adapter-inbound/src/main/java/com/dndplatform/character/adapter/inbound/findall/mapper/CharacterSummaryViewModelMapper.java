package com.dndplatform.character.adapter.inbound.findall.mapper;

import com.dndplatform.character.domain.model.CharacterSummary;
import com.dndplatform.character.view.model.vm.CharacterSummaryViewModel;
import com.dndplatform.character.view.model.vm.CharacterSummaryViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class CharacterSummaryViewModelMapper implements Function<CharacterSummary, CharacterSummaryViewModel> {

    @Override
    public CharacterSummaryViewModel apply(CharacterSummary summary) {
        return CharacterSummaryViewModelBuilder.builder()
                .withId(summary.id())
                .withName(summary.name())
                .withSpecies(summary.species())
                .withCharacterClass(summary.characterClass())
                .withLevel(summary.level())
                .withHitPointsCurrent(summary.hitPointsCurrent())
                .withHitPointsMax(summary.hitPointsMax())
                .withArmorClass(summary.armorClass())
                .build();
    }
}
