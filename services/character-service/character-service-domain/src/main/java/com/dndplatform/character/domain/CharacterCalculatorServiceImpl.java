package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.SpellSlotAllocation;
import com.dndplatform.character.domain.model.SpellSlotAllocationBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterCalculatorServiceImpl implements CharacterProficiencyBonusCalculator,
        CharacterModifierCalculator, CharacterMaxHpCalculator, CharacterSpellcastingAbilityProvider,
        CharacterHitDieProvider, CharacterBaseSpeedProvider, CharacterSpellSlotsCalculator {

    private final Logger log = Logger.getLogger(getClass().getName());

    private static final Map<String, String> CLASS_HIT_DIE = Map.ofEntries(
            Map.entry("Barbarian", "d12"),
            Map.entry("Bard", "d8"),
            Map.entry("Cleric", "d8"),
            Map.entry("Druid", "d8"),
            Map.entry("Fighter", "d10"),
            Map.entry("Monk", "d8"),
            Map.entry("Paladin", "d10"),
            Map.entry("Ranger", "d10"),
            Map.entry("Rogue", "d8"),
            Map.entry("Sorcerer", "d6"),
            Map.entry("Warlock", "d8"),
            Map.entry("Wizard", "d6")
    );

    private static final Map<String, String> CLASS_SPELLCASTING_ABILITY = Map.ofEntries(
            Map.entry("Bard", "CHA"),
            Map.entry("Cleric", "WIS"),
            Map.entry("Druid", "WIS"),
            Map.entry("Paladin", "CHA"),
            Map.entry("Ranger", "WIS"),
            Map.entry("Sorcerer", "CHA"),
            Map.entry("Warlock", "CHA"),
            Map.entry("Wizard", "INT")
    );

    private static final Map<String, Integer> SPECIES_BASE_SPEED = Map.ofEntries(
            Map.entry("Human", 30),
            Map.entry("Elf", 30),
            Map.entry("High Elf", 30),
            Map.entry("Wood Elf", 35),
            Map.entry("Dark Elf", 30),
            Map.entry("Dwarf", 25),
            Map.entry("Hill Dwarf", 25),
            Map.entry("Mountain Dwarf", 25),
            Map.entry("Halfling", 25),
            Map.entry("Lightfoot Halfling", 25),
            Map.entry("Stout Halfling", 25),
            Map.entry("Dragonborn", 30),
            Map.entry("Gnome", 25),
            Map.entry("Forest Gnome", 25),
            Map.entry("Rock Gnome", 25),
            Map.entry("Half-Elf", 30),
            Map.entry("Half-Orc", 30),
            Map.entry("Tiefling", 30)
    );

    // Full caster spell slots by level (Bard, Cleric, Druid, Sorcerer, Wizard)
    private static final int[][] FULL_CASTER_SLOTS = {
            {2, 0, 0, 0, 0, 0, 0, 0, 0},  // Level 1
            {3, 0, 0, 0, 0, 0, 0, 0, 0},  // Level 2
            {4, 2, 0, 0, 0, 0, 0, 0, 0},  // Level 3
            {4, 3, 0, 0, 0, 0, 0, 0, 0},  // Level 4
            {4, 3, 2, 0, 0, 0, 0, 0, 0},  // Level 5
            {4, 3, 3, 0, 0, 0, 0, 0, 0},  // Level 6
            {4, 3, 3, 1, 0, 0, 0, 0, 0},  // Level 7
            {4, 3, 3, 2, 0, 0, 0, 0, 0},  // Level 8
            {4, 3, 3, 3, 1, 0, 0, 0, 0},  // Level 9
            {4, 3, 3, 3, 2, 0, 0, 0, 0},  // Level 10
            {4, 3, 3, 3, 2, 1, 0, 0, 0},  // Level 11
            {4, 3, 3, 3, 2, 1, 0, 0, 0},  // Level 12
            {4, 3, 3, 3, 2, 1, 1, 0, 0},  // Level 13
            {4, 3, 3, 3, 2, 1, 1, 0, 0},  // Level 14
            {4, 3, 3, 3, 2, 1, 1, 1, 0},  // Level 15
            {4, 3, 3, 3, 2, 1, 1, 1, 0},  // Level 16
            {4, 3, 3, 3, 2, 1, 1, 1, 1},  // Level 17
            {4, 3, 3, 3, 3, 1, 1, 1, 1},  // Level 18
            {4, 3, 3, 3, 3, 2, 1, 1, 1},  // Level 19
            {4, 3, 3, 3, 3, 2, 2, 1, 1}   // Level 20
    };

    // Half caster spell slots by level (Paladin, Ranger)
    private static final int[][] HALF_CASTER_SLOTS = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},  // Level 1
            {2, 0, 0, 0, 0, 0, 0, 0, 0},  // Level 2
            {3, 0, 0, 0, 0, 0, 0, 0, 0},  // Level 3
            {3, 0, 0, 0, 0, 0, 0, 0, 0},  // Level 4
            {4, 2, 0, 0, 0, 0, 0, 0, 0},  // Level 5
            {4, 2, 0, 0, 0, 0, 0, 0, 0},  // Level 6
            {4, 3, 0, 0, 0, 0, 0, 0, 0},  // Level 7
            {4, 3, 0, 0, 0, 0, 0, 0, 0},  // Level 8
            {4, 3, 2, 0, 0, 0, 0, 0, 0},  // Level 9
            {4, 3, 2, 0, 0, 0, 0, 0, 0},  // Level 10
            {4, 3, 3, 0, 0, 0, 0, 0, 0},  // Level 11
            {4, 3, 3, 0, 0, 0, 0, 0, 0},  // Level 12
            {4, 3, 3, 1, 0, 0, 0, 0, 0},  // Level 13
            {4, 3, 3, 1, 0, 0, 0, 0, 0},  // Level 14
            {4, 3, 3, 2, 0, 0, 0, 0, 0},  // Level 15
            {4, 3, 3, 2, 0, 0, 0, 0, 0},  // Level 16
            {4, 3, 3, 3, 1, 0, 0, 0, 0},  // Level 17
            {4, 3, 3, 3, 1, 0, 0, 0, 0},  // Level 18
            {4, 3, 3, 3, 2, 0, 0, 0, 0},  // Level 19
            {4, 3, 3, 3, 2, 0, 0, 0, 0}   // Level 20
    };

    // Warlock pact magic slots
    private static final int[][] WARLOCK_SLOTS = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0},  // Level 1
            {2, 0, 0, 0, 0, 0, 0, 0, 0},  // Level 2
            {0, 2, 0, 0, 0, 0, 0, 0, 0},  // Level 3
            {0, 2, 0, 0, 0, 0, 0, 0, 0},  // Level 4
            {0, 0, 2, 0, 0, 0, 0, 0, 0},  // Level 5
            {0, 0, 2, 0, 0, 0, 0, 0, 0},  // Level 6
            {0, 0, 0, 2, 0, 0, 0, 0, 0},  // Level 7
            {0, 0, 0, 2, 0, 0, 0, 0, 0},  // Level 8
            {0, 0, 0, 0, 2, 0, 0, 0, 0},  // Level 9
            {0, 0, 0, 0, 2, 0, 0, 0, 0},  // Level 10
            {0, 0, 0, 0, 3, 0, 0, 0, 0},  // Level 11
            {0, 0, 0, 0, 3, 0, 0, 0, 0},  // Level 12
            {0, 0, 0, 0, 3, 0, 0, 0, 0},  // Level 13
            {0, 0, 0, 0, 3, 0, 0, 0, 0},  // Level 14
            {0, 0, 0, 0, 3, 0, 0, 0, 0},  // Level 15
            {0, 0, 0, 0, 3, 0, 0, 0, 0},  // Level 16
            {0, 0, 0, 0, 4, 0, 0, 0, 0},  // Level 17
            {0, 0, 0, 0, 4, 0, 0, 0, 0},  // Level 18
            {0, 0, 0, 0, 4, 0, 0, 0, 0},  // Level 19
            {0, 0, 0, 0, 4, 0, 0, 0, 0}   // Level 20
    };

    @Override
    public int calculateProficiencyBonus(int level) {
        if (level < 1) return 2;
        if (level <= 4) return 2;
        if (level <= 8) return 3;
        if (level <= 12) return 4;
        if (level <= 16) return 5;
        return 6;
    }

    @Override
    public int calculateModifier(int abilityScore) {
        return (abilityScore - 10) / 2;
    }

    @Override
    public int calculateMaxHp(String hitDie, int level, int constitutionModifier) {
        int dieMax = switch (hitDie) {
            case "d6" -> 6;
            case "d8" -> 8;
            case "d10" -> 10;
            case "d12" -> 12;
            default -> 8;
        };

        int dieAverage = (dieMax / 2) + 1;

        // At level 1: max hit die + CON mod
        // At levels 2+: average hit die + CON mod per level
        int hp = dieMax + constitutionModifier;
        if (level > 1) {
            hp += (level - 1) * (dieAverage + constitutionModifier);
        }

        return Math.max(1, hp);
    }

    @Override
    public String getSpellcastingAbility(String className) {
        return CLASS_SPELLCASTING_ABILITY.get(className);
    }

    @Override
    public String getHitDie(String className) {
        return CLASS_HIT_DIE.getOrDefault(className, "d8");
    }

    @Override
    public int getBaseSpeed(String speciesName) {
        return SPECIES_BASE_SPEED.getOrDefault(speciesName, 30);
    }

    @Override
    public List<SpellSlotAllocation> calculateSpellSlots(String className, int level) {
        log.fine(() -> "Calculating spell slots for %s at level %d".formatted(className, level));

        if (level < 1 || level > 20) {
            return List.of();
        }

        int[][] slotTable = getSlotTable(className);
        if (slotTable == null) {
            return List.of();
        }

        List<SpellSlotAllocation> slots = new ArrayList<>();
        int[] levelSlots = slotTable[level - 1];

        for (int spellLevel = 0; spellLevel < 9; spellLevel++) {
            if (levelSlots[spellLevel] > 0) {
                slots.add(SpellSlotAllocationBuilder.builder()
                        .withSpellLevel(spellLevel + 1)
                        .withSlotsTotal(levelSlots[spellLevel])
                        .build());
            }
        }

        return slots;
    }

    private int[][] getSlotTable(String className) {
        return switch (className) {
            case "Bard", "Cleric", "Druid", "Sorcerer", "Wizard" -> FULL_CASTER_SLOTS;
            case "Paladin", "Ranger" -> HALF_CASTER_SLOTS;
            case "Warlock" -> WARLOCK_SLOTS;
            default -> null;
        };
    }
}
