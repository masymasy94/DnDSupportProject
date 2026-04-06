package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ProficiencyTypeViewModelMapperTest {

    private final ProficiencyTypeViewModelMapper sut = Mappers.getMapper(ProficiencyTypeViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random ProficiencyType proficiencyType) {
        ProficiencyTypeViewModel result = sut.apply(proficiencyType);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(proficiencyType.id());
        assertThat(result.name()).isEqualTo(proficiencyType.name());
    }
}
