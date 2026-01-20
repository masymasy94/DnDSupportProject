package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Monster(
        Integer id,
        String index,
        String name,
        String size,
        String type,
        String subtype,
        String alignment,
        int armorClass,
        String armorType,
        int hitPoints,
        String hitDice,
        String speed,
        int strength,
        int dexterity,
        int constitution,
        int intelligence,
        int wisdom,
        int charisma,
        String savingThrows,
        String skills,
        String senses,
        String languages,
        String challengeRating,
        int xp,
        int proficiencyBonus,
        String specialAbilities,
        String actions,
        String reactions,
        String legendaryActions,
        String legendaryDesc,
        String lairActions,
        String regionalEffects,
        String description,
        SourceType source,
        Long ownerId,
        Long campaignId,
        boolean isPublic
) {}
