package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.ValidatedCompendiumData;
import com.dndplatform.character.domain.repository.CharacterUpdateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CharacterUpdateServiceImpl implements CharacterUpdateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CharacterValidationService validationService;
    private final CharacterProficiencyBonusCalculator proficiencyBonusCalculator;
    private final CharacterModifierCalculator modifierCalculator;
    private final CharacterMaxHpCalculator maxHpCalculator;
    private final CharacterHitDieProvider hitDieProvider;
    private final CharacterSpellcastingAbilityProvider spellcastingAbilityProvider;
    private final CharacterUpdateRepository repository;

    @Inject
    public CharacterUpdateServiceImpl(CharacterValidationService validationService,
                                      CharacterProficiencyBonusCalculator proficiencyBonusCalculator,
                                      CharacterModifierCalculator modifierCalculator,
                                      CharacterMaxHpCalculator maxHpCalculator,
                                      CharacterHitDieProvider hitDieProvider,
                                      CharacterSpellcastingAbilityProvider spellcastingAbilityProvider,
                                      CharacterUpdateRepository repository) {
        this.validationService = validationService;
        this.proficiencyBonusCalculator = proficiencyBonusCalculator;
        this.modifierCalculator = modifierCalculator;
        this.maxHpCalculator = maxHpCalculator;
        this.hitDieProvider = hitDieProvider;
        this.spellcastingAbilityProvider = spellcastingAbilityProvider;
        this.repository = repository;
    }

    @Override
    public Character update(Long characterId, CharacterCreate input) {
        log.info(() -> "Updating character %d: %s".formatted(characterId, input.name()));

        // Validate compendium references
        ValidatedCompendiumData compendiumData = validationService.validate(input);

        // Calculate derived stats
        int proficiencyBonus = proficiencyBonusCalculator.calculateProficiencyBonus(input.level());

        String hitDie = hitDieProvider.getHitDie(input.characterClass());
        int conModifier = modifierCalculator.calculateModifier(input.abilityScores().constitution());
        int hitPointsMax = maxHpCalculator.calculateMaxHp(hitDie, input.level(), conModifier);

        // Calculate spellcasting stats if applicable
        String spellcastingAbility = spellcastingAbilityProvider.getSpellcastingAbility(input.characterClass());
        Integer spellSaveDc = null;
        Integer spellAttackBonus = null;

        if (spellcastingAbility != null) {
            int spellcastingModifier = input.abilityScores().getModifier(spellcastingAbility);
            spellSaveDc = 8 + proficiencyBonus + spellcastingModifier;
            spellAttackBonus = proficiencyBonus + spellcastingModifier;
        }

        // Update character
        Character character = repository.update(characterId, input, compendiumData, proficiencyBonus,
                hitPointsMax, spellcastingAbility, spellSaveDc, spellAttackBonus);

        log.info(() -> "Character updated with ID: %d".formatted(character.id()));
        return character;
    }
}
