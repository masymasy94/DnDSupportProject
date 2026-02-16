package com.dndplatform.character.domain.repository;

import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.ValidatedCompendiumData;

public interface CharacterCreateRepository {
    Character save(CharacterCreate input, ValidatedCompendiumData compendiumData, int proficiencyBonus,
                   int hitPointsMax, String spellcastingAbility, Integer spellSaveDc, Integer spellAttackBonus);
}
