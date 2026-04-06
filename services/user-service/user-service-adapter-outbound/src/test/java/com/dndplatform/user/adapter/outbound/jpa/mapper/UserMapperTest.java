package com.dndplatform.user.adapter.outbound.jpa.mapper;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper sut;

    @BeforeEach
    void setUp() {
        sut = new UserMapperImpl();
    }

    @Test
    void apply_shouldMapAllFieldsFromEntityToDomain() {
        UserEntity entity = new UserEntity();
        entity.id = 42L;
        entity.setUsername("bilbo_baggins");
        entity.setEmail("bilbo@shire.com");
        entity.setPasswordHash("$2a$hash");
        entity.setRole("DUNGEON_MASTER");
        entity.setActive(true);
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 6, 1, 12, 30);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        User result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(42L);
        assertThat(result.username()).isEqualTo("bilbo_baggins");
        assertThat(result.email()).isEqualTo("bilbo@shire.com");
        assertThat(result.passwordHash()).isEqualTo("$2a$hash");
        assertThat(result.role()).isEqualTo("DUNGEON_MASTER");
        assertThat(result.active()).isTrue();
        assertThat(result.createdAt()).isEqualTo(createdAt);
        assertThat(result.updatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void apply_shouldReturnNull_whenEntityIsNull() {
        User result = sut.apply(null);

        assertThat(result).isNull();
    }

    @Test
    void apply_shouldPreserveNullOptionalFields_whenNotSet() {
        UserEntity entity = new UserEntity();
        entity.id = 1L;
        entity.setUsername("frodo");
        entity.setEmail("frodo@shire.com");
        entity.setPasswordHash("hash");
        entity.setRole("PLAYER");
        entity.setActive(false);
        entity.setUpdatedAt(null);

        User result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.updatedAt()).isNull();
        assertThat(result.active()).isFalse();
    }
}
