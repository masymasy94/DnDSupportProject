package com.dndplatform.character.adapter.outbound.jpa.mapper;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterEntity;
import com.dndplatform.character.domain.model.CharacterSummary;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterSummaryMapperTest {

    private CharacterSummaryMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterSummaryMapper();
    }

    private CharacterEntity entity(long id, String name, String species, String className,
                                   int level, int hpCurrent, int hpMax, int armorClass) {
        CharacterEntity e = new CharacterEntity();
        e.id = id;
        e.name = name;
        e.speciesName = species;
        e.className = className;
        e.level = level;
        e.hitPointsCurrent = hpCurrent;
        e.hitPointsMax = hpMax;
        e.armorClass = armorClass;
        return e;
    }

    @Test
    void apply_shouldMapAllFields() {
        CharacterEntity e = entity(7L, "Legolas", "Elf", "Ranger", 5, 38, 42, 16);

        CharacterSummary result = sut.apply(e);

        assertThat(result.id()).isEqualTo(7L);
        assertThat(result.name()).isEqualTo("Legolas");
        assertThat(result.species()).isEqualTo("Elf");
        assertThat(result.characterClass()).isEqualTo("Ranger");
        assertThat(result.level()).isEqualTo(5);
        assertThat(result.hitPointsCurrent()).isEqualTo(38);
        assertThat(result.hitPointsMax()).isEqualTo(42);
        assertThat(result.armorClass()).isEqualTo(16);
    }

    @Test
    void apply_shouldMapLevel1Character() {
        CharacterEntity e = entity(1L, "Frodo", "Hobbit", "Rogue", 1, 8, 8, 12);

        CharacterSummary result = sut.apply(e);

        assertThat(result.level()).isEqualTo(1);
        assertThat(result.hitPointsCurrent()).isEqualTo(result.hitPointsMax());
    }

    @Test
    void apply_shouldMapWoundedCharacter() {
        CharacterEntity e = entity(2L, "Aragorn", "Human", "Ranger", 20, 12, 172, 18);

        CharacterSummary result = sut.apply(e);

        assertThat(result.hitPointsCurrent()).isEqualTo(12);
        assertThat(result.hitPointsMax()).isEqualTo(172);
    }

    @Test
    void apply_shouldMapNullSpeciesAndClass() {
        CharacterEntity e = new CharacterEntity();
        e.id = 3L;
        e.name = "Unknown";
        e.speciesName = null;
        e.className = null;
        e.level = 1;
        e.hitPointsCurrent = 0;
        e.hitPointsMax = 0;
        e.armorClass = 10;

        CharacterSummary result = sut.apply(e);

        assertThat(result.species()).isNull();
        assertThat(result.characterClass()).isNull();
    }

    @Test
    void apply_shouldImplementFunctionContract() {
        CharacterEntity e = entity(5L, "Gimli", "Dwarf", "Fighter", 10, 90, 100, 20);

        CharacterSummary result = sut.apply(e);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(5L);
    }
}
