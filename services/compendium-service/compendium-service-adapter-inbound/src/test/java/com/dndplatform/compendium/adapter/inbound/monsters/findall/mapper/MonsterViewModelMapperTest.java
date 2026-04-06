package com.dndplatform.compendium.adapter.inbound.monsters.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.view.model.vm.MonsterViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class MonsterViewModelMapperTest {

    private final MonsterViewModelMapper sut = Mappers.getMapper(MonsterViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Monster monster) {
        MonsterViewModel result = sut.apply(monster);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(monster.id());
        assertThat(result.name()).isEqualTo(monster.name());
        assertThat(result.type()).isEqualTo(monster.type());
        assertThat(result.source()).isEqualTo(monster.source().name());
    }
}
