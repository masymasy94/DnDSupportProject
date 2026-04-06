package com.dndplatform.auth.adapter.inbound.login.mapper;

import com.dndplatform.auth.view.model.vm.CreateLoginTokensViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CreateLoginTokensMapperTest {

    private final CreateLoginTokensMapper sut = Mappers.getMapper(CreateLoginTokensMapper.class);

    @Test
    void shouldMapToCreateLoginTokens(@Random CreateLoginTokensViewModel vm) {
        var result = sut.apply(vm);

        assertThat(result.username()).isEqualTo(vm.username());
        assertThat(result.password()).isEqualTo(vm.password());
    }
}
