package com.dndplatform.character.adapter.inbound.update.mapper;

import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.view.model.vm.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class CharacterUpdateMapper implements Function<UpdateCharacterRequest, CharacterCreate> {

    @Override
    public CharacterCreate apply(UpdateCharacterRequest request) {
        return CharacterCreateBuilder.builder()
                .withName(request.name())
                .withSpecies(request.species())
                .withSubrace(request.subrace())
                .withCharacterClass(request.characterClass())
                .withSubclass(request.subclass())
                .withBackground(request.background())
                .withAlignment(request.alignment())
                .withLevel(request.level())
                .withAbilityScores(mapAbilityScores(request.abilityScores()))
                .withSkillProficiencies(request.skillProficiencies())
                .withSavingThrowProficiencies(request.savingThrowProficiencies())
                .withLanguages(request.languages())
                .withProficiencies(mapProficiencies(request.proficiencies()))
                .withEquipment(mapEquipment(request.equipment()))
                .withSpells(request.spells())
                .withPhysicalCharacteristics(mapPhysicalCharacteristics(request.physicalCharacteristics()))
                .withPersonalityTraits(request.personalityTraits())
                .withIdeals(request.ideals())
                .withBonds(request.bonds())
                .withFlaws(request.flaws())
                .build();
    }

    private AbilityScores mapAbilityScores(AbilityScoresRequest request) {
        if (request == null) {
            return null;
        }
        return AbilityScoresBuilder.builder()
                .withStrength(request.strength())
                .withDexterity(request.dexterity())
                .withConstitution(request.constitution())
                .withIntelligence(request.intelligence())
                .withWisdom(request.wisdom())
                .withCharisma(request.charisma())
                .build();
    }

    private List<Proficiency> mapProficiencies(List<ProficiencyRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(r -> ProficiencyBuilder.builder()
                        .withType(r.type())
                        .withName(r.name())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Equipment> mapEquipment(List<EquipmentRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(r -> EquipmentBuilder.builder()
                        .withName(r.name())
                        .withQuantity(r.quantity())
                        .withEquipped(r.equipped())
                        .build())
                .collect(Collectors.toList());
    }

    private PhysicalCharacteristics mapPhysicalCharacteristics(PhysicalCharacteristicsRequest request) {
        if (request == null) {
            return null;
        }
        return PhysicalCharacteristicsBuilder.builder()
                .withAge(request.age())
                .withHeight(request.height())
                .withWeight(request.weight())
                .withEyes(request.eyes())
                .withSkin(request.skin())
                .withHair(request.hair())
                .build();
    }
}
