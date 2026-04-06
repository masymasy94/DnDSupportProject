package com.dndplatform.notificationservice.adapter.inbound.template.mapper;

import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResultBuilder;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateResponseViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CreateEmailTemplateResponseMapperTest {

    private CreateEmailTemplateResponseMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CreateEmailTemplateResponseMapperImpl();
    }

    @Test
    void shouldMapEmailTemplateResultToResponseViewModel(@Random EmailTemplateResult result) {
        CreateEmailTemplateResponseViewModel viewModel = sut.apply(result);

        assertThat(viewModel.id()).isEqualTo(result.id());
        assertThat(viewModel.name()).isEqualTo(result.name());
        assertThat(viewModel.createdAt()).isEqualTo(result.createdAt().toString());
    }

    @Test
    void shouldConvertNullCreatedAtToNull() {
        EmailTemplateResult result = EmailTemplateResultBuilder.builder()
                .withId(1L)
                .withName("welcome-email")
                .withCreatedAt(null)
                .build();

        CreateEmailTemplateResponseViewModel viewModel = sut.apply(result);

        assertThat(viewModel.id()).isEqualTo(1L);
        assertThat(viewModel.name()).isEqualTo("welcome-email");
        assertThat(viewModel.createdAt()).isNull();
    }

    @Test
    void shouldFormatCreatedAtAsIsoString() {
        LocalDateTime now = LocalDateTime.of(2026, 1, 13, 10, 30, 0);
        EmailTemplateResult result = EmailTemplateResultBuilder.builder()
                .withId(5L)
                .withName("test-template")
                .withCreatedAt(now)
                .build();

        CreateEmailTemplateResponseViewModel viewModel = sut.apply(result);

        assertThat(viewModel.createdAt()).isEqualTo("2026-01-13T10:30");
    }

    @Test
    void shouldReturnNullWhenResultIsNull() {
        CreateEmailTemplateResponseViewModel viewModel = sut.apply(null);

        assertThat(viewModel).isNull();
    }
}
