package com.dndplatform.notificationservice.adapter.inbound.template;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.view.model.GetAllEmailTemplatesResource;
import com.dndplatform.notificationservice.view.model.vm.GetAllEmailTemplatesResponseViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class GetAllEmailTemplatesResourceImplTest {

    @Mock
    private GetAllEmailTemplatesResource delegate;

    private GetAllEmailTemplatesResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new GetAllEmailTemplatesResourceImpl(delegate);
    }

    @Test
    void shouldDelegateGetAll(@Random GetAllEmailTemplatesResponseViewModel expected) {
        given(delegate.getAll()).willReturn(expected);

        var result = sut.getAll();

        assertThat(result).isEqualTo(expected);
        then(delegate).should().getAll();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldReturnDelegateResponse(@Random GetAllEmailTemplatesResponseViewModel expected) {
        given(delegate.getAll()).willReturn(expected);

        var result = sut.getAll();

        assertThat(result).isSameAs(expected);
    }
}
