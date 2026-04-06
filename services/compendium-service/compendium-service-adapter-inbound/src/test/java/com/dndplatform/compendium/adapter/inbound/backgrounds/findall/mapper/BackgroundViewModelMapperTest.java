package com.dndplatform.compendium.adapter.inbound.backgrounds.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class BackgroundViewModelMapperTest {

    private final BackgroundViewModelMapper sut = Mappers.getMapper(BackgroundViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Background background) {
        BackgroundViewModel result = sut.apply(background);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(background.id());
        assertThat(result.name()).isEqualTo(background.name());
        assertThat(result.description()).isEqualTo(background.description());
        assertThat(result.source()).isEqualTo(background.source().name());
    }
}
