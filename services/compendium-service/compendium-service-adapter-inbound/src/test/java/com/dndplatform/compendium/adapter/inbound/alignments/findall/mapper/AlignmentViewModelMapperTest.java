package com.dndplatform.compendium.adapter.inbound.alignments.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class AlignmentViewModelMapperTest {

    private final AlignmentViewModelMapper sut = Mappers.getMapper(AlignmentViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Alignment alignment) {
        AlignmentViewModel result = sut.apply(alignment);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(alignment.id());
        assertThat(result.code()).isEqualTo(alignment.code());
        assertThat(result.name()).isEqualTo(alignment.name());
    }
}
