package com.dndplatform.character.adapter.inbound.create.mapper;

import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.view.model.vm.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class CharacterViewModelMapper implements Function<Character, CharacterViewModel> {

    @Override
    public CharacterViewModel apply(Character character) {
        return CharacterViewModelBuilder.builder()
                .withId(character.id())
                .withName(character.name())
                .withSpecies(character.species())
                .withSubrace(character.subrace())
                .withCharacterClass(character.characterClass())
                .withSubclass(character.subclass())
                .withBackground(character.background())
                .withAlignment(character.alignment())
                .withLevel(character.level())
                .withExperiencePoints(character.experiencePoints())
                .withAbilityScores(mapAbilityScores(character.abilityScores()))
                .withHitPointsCurrent(character.hitPointsCurrent())
                .withHitPointsMax(character.hitPointsMax())
                .withHitPointsTemp(character.hitPointsTemp())
                .withArmorClass(character.armorClass())
                .withSpeed(character.speed())
                .withHitDiceTotal(character.hitDiceTotal())
                .withHitDiceType(character.hitDiceType())
                .withHitDiceUsed(character.hitDiceUsed())
                .withProficiencyBonus(character.proficiencyBonus())
                .withInspiration(character.inspiration())
                .withSpellcastingAbility(character.spellcastingAbility())
                .withSpellSaveDc(character.spellSaveDc())
                .withSpellAttackBonus(character.spellAttackBonus())
                .withPhysicalCharacteristics(mapPhysicalCharacteristics(character.physicalCharacteristics()))
                .withLanguages(character.languages())
                .withSkills(mapSkills(character.skills()))
                .withSavingThrows(mapSavingThrows(character.savingThrows()))
                .withProficiencies(mapProficiencies(character.proficiencies()))
                .withEquipment(mapEquipment(character.equipment()))
                .withSpells(mapSpells(character.spells()))
                .withSpellSlots(mapSpellSlots(character.spellSlots()))
                .withPersonalityTraits(character.personalityTraits())
                .withIdeals(character.ideals())
                .withBonds(character.bonds())
                .withFlaws(character.flaws())
                .withCreatedAt(character.createdAt())
                .withUpdatedAt(character.updatedAt())
                .build();
    }

    private AbilityScoresViewModel mapAbilityScores(AbilityScores abilityScores) {
        if (abilityScores == null) {
            return null;
        }
        return AbilityScoresViewModelBuilder.builder()
                .withStrength(abilityScores.strength())
                .withDexterity(abilityScores.dexterity())
                .withConstitution(abilityScores.constitution())
                .withIntelligence(abilityScores.intelligence())
                .withWisdom(abilityScores.wisdom())
                .withCharisma(abilityScores.charisma())
                .build();
    }

    private PhysicalCharacteristicsViewModel mapPhysicalCharacteristics(PhysicalCharacteristics pc) {
        if (pc == null) {
            return null;
        }
        return PhysicalCharacteristicsViewModelBuilder.builder()
                .withAge(pc.age())
                .withHeight(pc.height())
                .withWeight(pc.weight())
                .withEyes(pc.eyes())
                .withSkin(pc.skin())
                .withHair(pc.hair())
                .build();
    }

    private List<SkillViewModel> mapSkills(List<SkillProficiency> skills) {
        if (skills == null) {
            return List.of();
        }
        return skills.stream()
                .map(s -> SkillViewModelBuilder.builder()
                        .withName(s.name())
                        .withAbility(s.ability())
                        .withProficient(s.proficient())
                        .withExpertise(s.expertise())
                        .withModifier(s.modifier())
                        .build())
                .collect(Collectors.toList());
    }

    private List<SavingThrowViewModel> mapSavingThrows(List<SavingThrowProficiency> savingThrows) {
        if (savingThrows == null) {
            return List.of();
        }
        return savingThrows.stream()
                .map(st -> SavingThrowViewModelBuilder.builder()
                        .withAbility(st.ability())
                        .withProficient(st.proficient())
                        .withModifier(st.modifier())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ProficiencyViewModel> mapProficiencies(List<Proficiency> proficiencies) {
        if (proficiencies == null) {
            return List.of();
        }
        return proficiencies.stream()
                .map(p -> ProficiencyViewModelBuilder.builder()
                        .withType(p.type())
                        .withName(p.name())
                        .build())
                .collect(Collectors.toList());
    }

    private List<EquipmentViewModel> mapEquipment(List<Equipment> equipment) {
        if (equipment == null) {
            return List.of();
        }
        return equipment.stream()
                .map(e -> EquipmentViewModelBuilder.builder()
                        .withName(e.name())
                        .withQuantity(e.quantity())
                        .withEquipped(e.equipped())
                        .build())
                .collect(Collectors.toList());
    }

    private List<SpellViewModel> mapSpells(List<CharacterSpell> spells) {
        if (spells == null) {
            return List.of();
        }
        return spells.stream()
                .map(s -> SpellViewModelBuilder.builder()
                        .withName(s.name())
                        .withLevel(s.level())
                        .withSchool(s.school())
                        .withPrepared(s.prepared())
                        .withSource(s.source())
                        .build())
                .collect(Collectors.toList());
    }

    private List<SpellSlotViewModel> mapSpellSlots(List<SpellSlotAllocation> spellSlots) {
        if (spellSlots == null) {
            return List.of();
        }
        return spellSlots.stream()
                .map(ss -> SpellSlotViewModelBuilder.builder()
                        .withLevel(ss.spellLevel())
                        .withTotal(ss.slotsTotal())
                        .withUsed(0) // Fresh character, no slots used
                        .build())
                .collect(Collectors.toList());
    }
}
