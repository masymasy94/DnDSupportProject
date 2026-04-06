package com.dndplatform.notificationservice.adapter.inbound.template;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.inbound.template.mapper.GetAllEmailTemplatesResponseMapper;
import com.dndplatform.notificationservice.domain.GetAllEmailTemplatesService;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.view.model.vm.GetAllEmailTemplatesResponseViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class GetAllEmailTemplatesDelegateTest {

    @Mock
    private GetAllEmailTemplatesResponseMapper responseMapper;

    @Mock
    private GetAllEmailTemplatesService getAllEmailTemplatesService;

    private GetAllEmailTemplatesDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new GetAllEmailTemplatesDelegate(responseMapper, getAllEmailTemplatesService);
    }

    @Test
    void shouldReturnAllTemplates(@Random EmailTemplateResult t1, @Random EmailTemplateResult t2,
                                   @Random GetAllEmailTemplatesResponseViewModel response) {
        var templates = List.of(t1, t2);
        given(getAllEmailTemplatesService.getAll()).willReturn(templates);
        given(responseMapper.apply(templates)).willReturn(response);

        var result = sut.getAll();

        assertThat(result).isEqualTo(response);

        var inOrder = inOrder(getAllEmailTemplatesService, responseMapper);
        then(getAllEmailTemplatesService).should(inOrder).getAll();
        then(responseMapper).should(inOrder).apply(templates);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnEmptyResponseWhenNoTemplates(@Random GetAllEmailTemplatesResponseViewModel response) {
        given(getAllEmailTemplatesService.getAll()).willReturn(List.of());
        given(responseMapper.apply(List.of())).willReturn(response);

        var result = sut.getAll();

        assertThat(result).isEqualTo(response);
        then(getAllEmailTemplatesService).should().getAll();
    }
}