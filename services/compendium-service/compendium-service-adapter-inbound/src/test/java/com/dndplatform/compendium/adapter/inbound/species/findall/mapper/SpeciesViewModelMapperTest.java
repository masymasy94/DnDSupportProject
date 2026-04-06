package com.dndplatform.compendium.adapter.inbound.species.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class SpeciesViewModelMapperTest {

    private final SpeciesViewModelMapper sut = Mappers.getMapper(SpeciesViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Species species) {
        SpeciesViewModel result = sut.apply(species);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(species.id());
        assertThat(result.name()).isEqualTo(species.name());
        assertThat(result.description()).isEqualTo(species.description());
        assertThat(result.source()).isEqualTo(species.source().name());
    }
}
