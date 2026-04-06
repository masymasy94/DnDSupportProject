package com.dndplatform.user.domain.event;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class UserRegisteredEventMapperTest {

    private final UserRegisteredEventMapper sut = Mappers.getMapper(UserRegisteredEventMapper.class);

    @Test
    void shouldMapToUserRegisteredEvent(@Random User user) {
        var result = sut.apply(user);

        assertThat(result.userId()).isEqualTo(user.id());
        assertThat(result.email()).isEqualTo(user.email());
        assertThat(result.username()).isEqualTo(user.username());
    }
}
