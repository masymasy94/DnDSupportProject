package com.dndplatform.user.adapter.inbound.register.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class UserRegisterMapperTest {

    private final UserRegisterMapper sut = Mappers.getMapper(UserRegisterMapper.class);

    @Test
    void shouldMapViewModelToUserRegister(@Random UserRegisterViewModel vm) {
        var result = sut.apply(vm);

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(vm.username());
        assertThat(result.email()).isEqualTo(vm.email());
        assertThat(result.password()).isEqualTo(vm.password());
    }

    @Test
    void shouldReturnNull_whenInputIsNull() {
        var result = sut.apply(null);

        assertThat(result).isNull();
    }
}
