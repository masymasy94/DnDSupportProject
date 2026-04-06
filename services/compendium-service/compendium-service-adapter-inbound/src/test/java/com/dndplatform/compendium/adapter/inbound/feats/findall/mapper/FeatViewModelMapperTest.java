package com.dndplatform.compendium.adapter.inbound.feats.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class FeatViewModelMapperTest {

    private final FeatViewModelMapper sut = Mappers.getMapper(FeatViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Feat feat) {
        FeatViewModel result = sut.apply(feat);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(feat.id());
        assertThat(result.name()).isEqualTo(feat.name());
        assertThat(result.description()).isEqualTo(feat.description());
        assertThat(result.source()).isEqualTo(feat.source().name());
    }
}
