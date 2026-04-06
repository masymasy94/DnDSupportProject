package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellSchoolEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class SpellMapperTest {

    private final SpellMapper sut = Mappers.getMapper(SpellMapper.class);

    @Test
    void shouldMapToSpell(@Random SpellEntity entity) {
        var school = new SpellSchoolEntity();
        school.name = "Evocation";
        entity.spellSchool = school;
        entity.source = null;

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.school()).isEqualTo("Evocation");
        assertThat(result.castingTime()).isEqualTo(entity.castingTime);
        assertThat(result.components()).isEqualTo(entity.components);
        assertThat(result.source()).isNull();
    }

    @Test
    void shouldDefaultConcentrationToFalseWhenNull(@Random SpellEntity entity) {
        var school = new SpellSchoolEntity();
        school.name = "Abjuration";
        entity.spellSchool = school;
        entity.source = null;
        entity.concentration = null;

        var result = sut.apply(entity);

        assertThat(result.concentration()).isFalse();
    }

    @Test
    void shouldDefaultRitualToFalseWhenNull(@Random SpellEntity entity) {
        var school = new SpellSchoolEntity();
        school.name = "Divination";
        entity.spellSchool = school;
        entity.source = null;
        entity.ritual = null;

        var result = sut.apply(entity);

        assertThat(result.ritual()).isFalse();
    }
}
