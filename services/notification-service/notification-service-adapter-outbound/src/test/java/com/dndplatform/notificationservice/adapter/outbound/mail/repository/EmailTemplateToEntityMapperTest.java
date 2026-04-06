package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EmailTemplateToEntityMapperTest {

    private final EmailTemplateToEntityMapper sut = new EmailTemplateToEntityMapper();

    @Test
    void shouldMapAllFields(@Random EmailTemplate template) {
        var entity = sut.apply(template);

        assertThat(entity.getName()).isEqualTo(template.name());
        assertThat(entity.getSubject()).isEqualTo(template.subject());
        assertThat(entity.getHtmlContent()).isEqualTo(template.htmlContent());
        assertThat(entity.getDescription()).isEqualTo(template.description());
        assertThat(entity.getActive()).isTrue();
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldSetActiveTrueAlways(@Random EmailTemplate template) {
        var entity = sut.apply(template);

        assertThat(entity.getActive()).isTrue();
    }
}
