package com.dndplatform.compendium.adapter.inbound.armortypes.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ArmorTypeViewModelMapperTest {

    private final ArmorTypeViewModelMapper sut = Mappers.getMapper(ArmorTypeViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random ArmorType armorType) {
        ArmorTypeViewModel result = sut.apply(armorType);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(armorType.id());
        assertThat(result.name()).isEqualTo(armorType.name());
    }
}
