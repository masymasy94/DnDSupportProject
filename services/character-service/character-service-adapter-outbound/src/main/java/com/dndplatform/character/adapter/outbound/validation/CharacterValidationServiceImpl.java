package com.dndplatform.character.adapter.outbound.validation;

import com.dndplatform.character.domain.CharacterBaseSpeedProvider;
import com.dndplatform.character.domain.CharacterHitDieProvider;
import com.dndplatform.character.domain.CharacterValidationService;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.ValidatedCompendiumData;
import com.dndplatform.character.domain.model.ValidatedCompendiumDataBuilder;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterValidationServiceImpl implements CharacterValidationService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterHitDieProvider hitDieProvider;
    private final CharacterBaseSpeedProvider baseSpeedProvider;

    private static final Set<String> VALID_SPECIES = Set.of(
            "Human", "Elf", "High Elf", "Wood Elf", "Dark Elf", "Drow",
            "Dwarf", "Hill Dwarf", "Mountain Dwarf",
            "Halfling", "Lightfoot Halfling", "Stout Halfling",
            "Dragonborn", "Gnome", "Forest Gnome", "Rock Gnome",
            "Half-Elf", "Half-Orc", "Tiefling"
    );

    private static final Set<String> VALID_CLASSES = Set.of(
            "Barbarian", "Bard", "Cleric", "Druid", "Fighter",
            "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer",
            "Warlock", "Wizard"
    );

    private static final Set<String> VALID_BACKGROUNDS = Set.of(
            "Acolyte", "Charlatan", "Criminal", "Entertainer", "Folk Hero",
            "Guild Artisan", "Hermit", "Noble", "Outlander", "Sage",
            "Sailor", "Soldier", "Urchin"
    );

    private static final Set<String> VALID_ALIGNMENTS = Set.of(
            "Lawful Good", "Neutral Good", "Chaotic Good",
            "Lawful Neutral", "True Neutral", "Chaotic Neutral",
            "Lawful Evil", "Neutral Evil", "Chaotic Evil",
            "Unaligned"
    );

    @Inject
    public CharacterValidationServiceImpl(CharacterHitDieProvider hitDieProvider,
                                            CharacterBaseSpeedProvider baseSpeedProvider) {
        this.hitDieProvider = hitDieProvider;
        this.baseSpeedProvider = baseSpeedProvider;
    }

    @Override
    public ValidatedCompendiumData validate(CharacterCreate input) {
        log.info(() -> "Validating character creation data for: %s".formatted(input.name()));

        // Validate species
        if (!VALID_SPECIES.contains(input.species())) {
            throw new NotFoundException("Invalid species: " + input.species());
        }

        // Validate class
        if (!VALID_CLASSES.contains(input.characterClass())) {
            throw new NotFoundException("Invalid class: " + input.characterClass());
        }

        // Validate background if provided
        if (input.background() != null && !input.background().isBlank()
                && !VALID_BACKGROUNDS.contains(input.background())) {
            throw new NotFoundException("Invalid background: " + input.background());
        }

        // Validate alignment if provided
        if (input.alignment() != null && !input.alignment().isBlank()
                && !VALID_ALIGNMENTS.contains(input.alignment())) {
            throw new NotFoundException("Invalid alignment: " + input.alignment());
        }

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
