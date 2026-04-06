package com.dndplatform.character.adapter.outbound.validation;

import com.dndplatform.character.domain.CharacterBaseSpeedProvider;
import com.dndplatform.character.domain.CharacterHitDieProvider;
import com.dndplatform.character.domain.CharacterValidationService;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.ValidatedCompendiumData;
import com.dndplatform.character.domain.model.ValidatedCompendiumDataBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CharacterValidationServiceImpl implements CharacterValidationService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterHitDieProvider hitDieProvider;
    private final CharacterBaseSpeedProvider baseSpeedProvider;

    @Inject
    public CharacterValidationServiceImpl(CharacterHitDieProvider hitDieProvider,
                                            CharacterBaseSpeedProvider baseSpeedProvider) {
        this.hitDieProvider = hitDieProvider;
        this.baseSpeedProvider = baseSpeedProvider;
    }

    @Override
    public ValidatedCompendiumData validate(CharacterCreate input) {
        log.info(() -> "Validating character creation data for: %s".formatted(input.name()));

        // Get hit die and base speed from calculator service
        String hitDie = hitDieProvider.getHitDie(input.characterClass());
        int baseSpeed = baseSpeedProvider.getBaseSpeed(input.species());

        return ValidatedCompendiumDataBuilder.builder()
                .withSpeciesName(input.species())
                .withClassName(input.characterClass())
                .withBackgroundName(input.background())
                .withAlignmentName(input.alignment())
                .withHitDie(hitDie)
                .withBaseSpeed(baseSpeed)
                .build();
    }
}
