package com.dndplatform.compendium.adapter.inbound.spellschools.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class SpellSchoolViewModelMapperTest {

    private final SpellSchoolViewModelMapper sut = Mappers.getMapper(SpellSchoolViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random SpellSchool spellSchool) {
        SpellSchoolViewModel result = sut.apply(spellSchool);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(spellSchool.id());
        assertThat(result.name()).isEqualTo(spellSchool.name());
    }
}
