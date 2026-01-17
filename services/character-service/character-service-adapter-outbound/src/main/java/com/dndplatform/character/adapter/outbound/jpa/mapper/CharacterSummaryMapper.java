package com.dndplatform.character.adapter.outbound.jpa.mapper;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterEntity;
import com.dndplatform.character.domain.model.CharacterSummary;
import com.dndplatform.character.domain.model.CharacterSummaryBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class CharacterSummaryMapper implements Function<CharacterEntity, CharacterSummary> {

    @Override
    public CharacterSummary apply(CharacterEntity entity) {
        return CharacterSummaryBuilder.builder()
                .withId(entity.id)
                .withName(entity.name)
                .withSpecies(entity.getSpeciesName())
                .withCharacterClass(entity.getClassName())
                .withLevel(entity.level)
                .withHitPointsCurrent(entity.hitPointsCurrent)
                .withHitPointsMax(entity.hitPointsMax)
                .withArmorClass(entity.armorClass)
                .build();
    }
}
