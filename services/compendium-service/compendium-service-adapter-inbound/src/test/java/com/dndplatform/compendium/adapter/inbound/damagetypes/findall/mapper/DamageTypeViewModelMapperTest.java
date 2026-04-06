package com.dndplatform.compendium.adapter.inbound.damagetypes.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class DamageTypeViewModelMapperTest {

    private final DamageTypeViewModelMapper sut = Mappers.getMapper(DamageTypeViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random DamageType damageType) {
        DamageTypeViewModel result = sut.apply(damageType);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(damageType.id());
        assertThat(result.name()).isEqualTo(damageType.name());
    }
}
