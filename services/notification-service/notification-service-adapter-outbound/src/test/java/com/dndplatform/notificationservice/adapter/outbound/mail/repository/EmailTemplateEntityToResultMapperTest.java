package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.notificationservice.adapter.outbound.mail.entity.EmailTemplateEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmailTemplateEntityToResultMapperTest {

    private final EmailTemplateEntityToResultMapper sut = new EmailTemplateEntityToResultMapper();

    @Test
    void shouldMapIdNameAndCreatedAt() {
        var entity = new EmailTemplateEntity();
        entity.id = 7L;
        entity.setName("welcome-email");
        var createdAt = LocalDateTime.of(2024, 1, 15, 10, 30);
        entity.setCreatedAt(createdAt);

        var result = sut.apply(entity);

        assertThat(result.id()).isEqualTo(7L);
        assertThat(result.name()).isEqualTo("welcome-email");
        assertThat(result.createdAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldMapNullIdWhenEntityIdIsNull() {
        var entity = new EmailTemplateEntity();
        entity.setName("test-template");
        entity.setCreatedAt(LocalDateTime.now());

        var result = sut.apply(entity);

        assertThat(result.name()).isEqualTo("test-template");
        assertThat(result.createdAt()).isNotNull();
    }
}
