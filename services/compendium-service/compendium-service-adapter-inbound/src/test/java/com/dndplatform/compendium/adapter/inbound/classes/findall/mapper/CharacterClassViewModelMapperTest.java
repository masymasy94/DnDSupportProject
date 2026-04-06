package com.dndplatform.compendium.adapter.inbound.classes.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CharacterClassViewModelMapperTest {

    private final CharacterClassViewModelMapper sut = Mappers.getMapper(CharacterClassViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random CharacterClass characterClass) {
        CharacterClassViewModel result = sut.apply(characterClass);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(characterClass.id());
        assertThat(result.name()).isEqualTo(characterClass.name());
        assertThat(result.hitDie()).isEqualTo(characterClass.hitDie());
        assertThat(result.description()).isEqualTo(characterClass.description());
        assertThat(result.source()).isEqualTo(characterClass.source().name());
    }
}
