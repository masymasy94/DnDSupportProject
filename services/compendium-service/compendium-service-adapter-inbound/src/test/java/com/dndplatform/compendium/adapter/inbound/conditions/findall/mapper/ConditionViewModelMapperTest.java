package com.dndplatform.compendium.adapter.inbound.conditions.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ConditionViewModelMapperTest {

    private final ConditionViewModelMapper sut = Mappers.getMapper(ConditionViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Condition condition) {
        ConditionViewModel result = sut.apply(condition);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(condition.id());
        assertThat(result.name()).isEqualTo(condition.name());
    }
}
