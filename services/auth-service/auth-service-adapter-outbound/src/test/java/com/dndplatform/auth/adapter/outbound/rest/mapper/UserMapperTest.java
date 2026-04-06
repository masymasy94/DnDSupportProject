package com.dndplatform.auth.adapter.outbound.rest.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class UserMapperTest {

    private final UserMapper sut = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapToUser(@Random UserViewModel vm) {
        var result = sut.apply(vm);

        assertThat(result.id()).isEqualTo(vm.id());
        assertThat(result.username()).isEqualTo(vm.username());
        assertThat(result.email()).isEqualTo(vm.email());
        assertThat(result.role()).isEqualTo(vm.role());
        assertThat(result.active()).isEqualTo(vm.active());
        assertThat(result.createdAt()).isEqualTo(vm.createdAt());
    }
}
