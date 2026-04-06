package com.dndplatform.compendium.adapter.inbound.weapontypes.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class WeaponTypeViewModelMapperTest {

    private final WeaponTypeViewModelMapper sut = Mappers.getMapper(WeaponTypeViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random WeaponType weaponType) {
        WeaponTypeViewModel result = sut.apply(weaponType);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(weaponType.id());
        assertThat(result.name()).isEqualTo(weaponType.name());
        assertThat(result.category()).isEqualTo(weaponType.category());
    }
}
