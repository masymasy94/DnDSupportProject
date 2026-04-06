package com.dndplatform.compendium.adapter.inbound.tooltypes.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ToolTypeViewModelMapperTest {

    private final ToolTypeViewModelMapper sut = Mappers.getMapper(ToolTypeViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random ToolType toolType) {
        ToolTypeViewModel result = sut.apply(toolType);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(toolType.id());
        assertThat(result.name()).isEqualTo(toolType.name());
        assertThat(result.category()).isEqualTo(toolType.category());
    }
}
