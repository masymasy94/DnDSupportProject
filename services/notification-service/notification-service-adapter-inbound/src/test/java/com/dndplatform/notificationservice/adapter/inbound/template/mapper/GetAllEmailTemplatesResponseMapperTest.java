package com.dndplatform.notificationservice.adapter.inbound.template.mapper;

import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResultBuilder;
import com.dndplatform.notificationservice.view.model.vm.GetAllEmailTemplatesResponseViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class GetAllEmailTemplatesResponseMapperTest {

    private GetAllEmailTemplatesResponseMapper sut;

    @BeforeEach
    void setUp() {
        sut = new GetAllEmailTemplatesResponseMapperImpl();
    }

    @Test
    void shouldMapListOfResultsToResponseViewModel() {
        LocalDateTime now = LocalDateTime.of(2026, 1, 13, 10, 30, 0);
        var result1 = EmailTemplateResultBuilder.builder()
                .withId(1L)
                .withName("welcome-email")
                .withCreatedAt(now)
                .build();
        var result2 = EmailTemplateResultBuilder.builder()
                .withId(2L)
                .withName("password-reset")
                .withCreatedAt(now.plusDays(1))
                .build();

        GetAllEmailTemplatesResponseViewModel viewModel = sut.apply(List.of(result1, result2));

        assertThat(viewModel.templates()).hasSize(2);
        assertThat(viewModel.templates().get(0).id()).isEqualTo(1L);
        assertThat(viewModel.templates().get(0).name()).isEqualTo("welcome-email");
        assertThat(viewModel.templates().get(0).createdAt()).isEqualTo("2026-01-13T10:30");
        assertThat(viewModel.templates().get(1).id()).isEqualTo(2L);
        assertThat(viewModel.templates().get(1).name()).isEqualTo("password-reset");
    }

    @Test
    void shouldReturnEmptyTemplatesListWhenInputIsEmpty() {
        GetAllEmailTemplatesResponseViewModel viewModel = sut.apply(List.of());

        assertThat(viewModel.templates()).isEmpty();
    }

    @Test
    void shouldHandleNullCreatedAt() {
        var result = EmailTemplateResultBuilder.builder()
                .withId(3L)
                .withName("no-date-template")
                .withCreatedAt(null)
                .build();

        GetAllEmailTemplatesResponseViewModel viewModel = sut.apply(List.of(result));

        assertThat(viewModel.templates()).hasSize(1);
        assertThat(viewModel.templates().get(0).createdAt()).isNull();
    }
}
