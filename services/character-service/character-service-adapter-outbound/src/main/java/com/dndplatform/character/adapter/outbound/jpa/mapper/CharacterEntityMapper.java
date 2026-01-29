package com.dndplatform.character.adapter.outbound.jpa.mapper;

import com.dndplatform.character.adapter.outbound.jpa.entity.*;
import com.dndplatform.character.domain.CharacterCalculatorService;
import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class CharacterEntityMapper {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterCalculatorService calculatorService;

    @Inject
    public CharacterEntityMapper(CharacterCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public Character toCharacter(CharacterEntity entity) {
        log.fine(() -> "Mapping CharacterEntity to Character: %d".formatted(entity.id));

        AbilityScores abilityScores = AbilityScoresBuilder.builder()
                .withStrength(entity.strength)
                .withDexterity(entity.dexterity)
                .withConstitution(entity.constitution)
                .withIntelligence(entity.intelligence)
                .withWisdom(entity.wisdom)
                .withCharisma(entity.charisma)
                .build();

        PhysicalCharacteristics physicalCharacteristics = null;
        if (entity.age != null || entity.height != null || entity.weight != null ||
                entity.eyes != null || entity.skin != null || entity.hair != null) {
            physicalCharacteristics = PhysicalCharacteristicsBuilder.builder()
                    .withAge(entity.age)
                    .withHeight(entity.height)
                    .withWeight(entity.weight)
                    .withEyes(entity.eyes)
                    .withSkin(entity.skin)
                    .withHair(entity.hair)
                    .build();
        }

        List<String> languages = entity.languages.stream()
                .map(cl -> cl.language.name)
                .collect(Collectors.toList());

        List<SkillProficiency> skills = entity.skills.stream()
                .map(this::toSkillProficiency)
                .collect(Collectors.toList());

        List<SavingThrowProficiency> savingThrows = entity.savingThrows.stream()
                .map(st -> toSavingThrowProficiency(st, abilityScores, entity.proficiencyBonus))
                .collect(Collectors.toList());

        List<Proficiency> proficiencies = entity.proficiencies.stream()
                .map(p -> ProficiencyBuilder.builder()
                        .withType(p.proficiencyType.name)
                        .withName(p.name)
                        .build())
                .collect(Collectors.toList());

        List<Equipment> equipment = entity.equipment.stream()
                .map(e -> EquipmentBuilder.builder()
                        .withName(e.name)
                        .withQuantity(e.quantity)
                        .withEquipped(e.equipped)
                        .build())
                .collect(Collectors.toList());

        List<CharacterSpell> spells = entity.spells.stream()
                .map(s -> CharacterSpellBuilder.builder()
                        .withName(s.spell.name)
                        .withLevel(s.spell.level.intValue())
                        .withSchool(s.spell.school.name)
                        .withPrepared(s.prepared)
                        .withSource(s.source)
                        .build())
                .collect(Collectors.toList());

        List<SpellSlotAllocation> spellSlots = entity.spellSlots.stream()
                .map(ss -> SpellSlotAllocationBuilder.builder()
                        .withSpellLevel(ss.spellLevel.intValue())
                        .withSlotsTotal(ss.slotsTotal.intValue())
                        .build())
                .collect(Collectors.toList());

        return CharacterBuilder.builder()
                .withId(entity.id)
                .withUserId(entity.userId)
                .withName(entity.name)
                .withSpecies(entity.speciesName)
                .withSubrace(entity.subraceName)
                .withCharacterClass(entity.className)
                .withSubclass(entity.subclassName)
                .withBackground(entity.backgroundName)
                .withAlignment(entity.alignmentName)
                .withLevel(entity.level)
                .withExperiencePoints(entity.experiencePoints)
                .withAbilityScores(abilityScores)
                .withHitPointsCurrent(entity.hitPointsCurrent)
                .withHitPointsMax(entity.hitPointsMax)
                .withHitPointsTemp(entity.hitPointsTemp)
                .withArmorClass(entity.armorClass)
                .withSpeed(entity.speed)
                .withHitDiceTotal(entity.hitDiceTotal)
                .withHitDiceType(entity.hitDiceType)
                .withHitDiceUsed(entity.hitDiceUsed)
                .withProficiencyBonus(entity.proficiencyBonus)
                .withInspiration(entity.inspiration)
                .withSpellcastingAbility(entity.spellcastingAbility)
                .withSpellSaveDc(entity.spellSaveDc)
                .withSpellAttackBonus(entity.spellAttackBonus)
                .withPhysicalCharacteristics(physicalCharacteristics)
                .withLanguages(languages)
                .withSkills(skills)
                .withSavingThrows(savingThrows)
                .withProficiencies(proficiencies)
                .withEquipment(equipment)
                .withSpells(spells)
                .withSpellSlots(spellSlots)
                .withPersonalityTraits(entity.personalityTraits)
                .withIdeals(entity.ideals)
                .withBonds(entity.bonds)
                .withFlaws(entity.flaws)
                .withCreatedAt(entity.createdAt)
                .withUpdatedAt(entity.updatedAt)
                .build();
    }

    private SkillProficiency toSkillProficiency(CharacterSkillEntity skillEntity) {
        return SkillProficiencyBuilder.builder()
                .withName(skillEntity.skill.name)
                .withAbility(skillEntity.skill.ability.code)
                .withProficient(skillEntity.proficient)
                .withExpertise(skillEntity.expertise)
                .withModifier(0) // Modifier would be calculated at service layer
                .build();
    }

    private SavingThrowProficiency toSavingThrowProficiency(CharacterSavingThrowEntity stEntity,
                                                            AbilityScores abilityScores,
                                                            int proficiencyBonus) {
        int abilityModifier = abilityScores.getModifier(stEntity.ability.code);
        int totalModifier = abilityModifier + (Boolean.TRUE.equals(stEntity.proficient) ? proficiencyBonus : 0);

        return SavingThrowProficiencyBuilder.builder()
                .withAbility(stEntity.ability.code)
                .withProficient(stEntity.proficient)
                .withModifier(totalModifier)
                .build();
    }
}
