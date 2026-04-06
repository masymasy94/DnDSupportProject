package com.dndplatform.compendium.adapter.inbound.spells.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class SpellViewModelMapperTest {

    private final SpellViewModelMapper sut = Mappers.getMapper(SpellViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Spell spell) {
        SpellViewModel result = sut.apply(spell);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(spell.id());
        assertThat(result.name()).isEqualTo(spell.name());
        assertThat(result.school()).isEqualTo(spell.school());
        assertThat(result.description()).isEqualTo(spell.description());
        assertThat(result.source()).isEqualTo(spell.source().name());
    }
}
