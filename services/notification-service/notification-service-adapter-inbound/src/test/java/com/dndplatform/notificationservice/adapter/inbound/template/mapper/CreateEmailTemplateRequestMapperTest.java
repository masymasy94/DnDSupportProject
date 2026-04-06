package com.dndplatform.notificationservice.adapter.inbound.template.mapper;

import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CreateEmailTemplateRequestMapperTest {

    private CreateEmailTemplateRequestMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CreateEmailTemplateRequestMapperImpl();
    }

    @Test
    void shouldMapRequestToEmailTemplate(@Random CreateEmailTemplateRequestViewModel request) {
        EmailTemplate result = sut.apply(request);

        assertThat(result.name()).isEqualTo(request.name());
        assertThat(result.subject()).isEqualTo(request.subject());
        assertThat(result.htmlContent()).isEqualTo(request.htmlContent());
        assertThat(result.description()).isEqualTo(request.description());
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        EmailTemplate result = sut.apply(null);

        assertThat(result).isNull();
    }
}
