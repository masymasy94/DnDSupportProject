package com.dndplatform.notificationservice.adapter.inbound.template;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.view.model.CreateEmailTemplateResource;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModel;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CreateEmailTemplateResourceImplTest {

    @Mock
    private CreateEmailTemplateResource delegate;

    @Mock
    private Response mockResponse;

    private CreateEmailTemplateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CreateEmailTemplateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCreate(@Random CreateEmailTemplateRequestViewModel request) {
        given(delegate.create(request)).willReturn(mockResponse);

        var result = sut.create(request);

        assertThat(result).isEqualTo(mockResponse);
        then(delegate).should().create(request);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldReturnDelegateResponse(@Random CreateEmailTemplateRequestViewModel request) {
        given(delegate.create(request)).willReturn(mockResponse);

        var result = sut.create(request);

        assertThat(result).isSameAs(mockResponse);
    }
}
