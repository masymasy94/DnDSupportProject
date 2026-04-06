package com.dndplatform.user.adapter.inbound.validate.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class UserCredentialsValidateMapperTest {

    private final UserCredentialsValidateMapper sut = Mappers.getMapper(UserCredentialsValidateMapper.class);

    @Test
    void shouldMapViewModelToUserCredentialsValidate(@Random UserCredentialsValidateViewModel vm) {
        var result = sut.apply(vm);

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(vm.username());
        assertThat(result.password()).isEqualTo(vm.password());
    }

    @Test
    void shouldReturnNull_whenInputIsNull() {
        var result = sut.apply(null);

        assertThat(result).isNull();
    }
}
