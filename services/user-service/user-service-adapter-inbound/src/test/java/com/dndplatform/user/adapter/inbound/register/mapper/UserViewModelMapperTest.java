package com.dndplatform.user.adapter.inbound.register.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class UserViewModelMapperTest {

    private final UserViewModelMapper sut = Mappers.getMapper(UserViewModelMapper.class);

    @Test
    void shouldMapUserToViewModel(@Random User user) {
        var result = sut.apply(user);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(user.id());
        assertThat(result.username()).isEqualTo(user.username());
        assertThat(result.email()).isEqualTo(user.email());
        assertThat(result.role()).isEqualTo(user.role());
        assertThat(result.active()).isEqualTo(user.active());
        assertThat(result.createdAt()).isEqualTo(user.createdAt());
    }

    @Test
    void shouldReturnNull_whenInputIsNull() {
        var result = sut.apply(null);

        assertThat(result).isNull();
    }
}
