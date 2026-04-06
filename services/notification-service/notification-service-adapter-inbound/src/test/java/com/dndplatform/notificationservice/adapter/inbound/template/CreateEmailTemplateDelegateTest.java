package com.dndplatform.notificationservice.adapter.inbound.template;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.inbound.template.mapper.CreateEmailTemplateRequestMapper;
import com.dndplatform.notificationservice.adapter.inbound.template.mapper.CreateEmailTemplateResponseMapper;
import com.dndplatform.notificationservice.domain.CreateEmailTemplateService;
import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateResponseViewModel;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CreateEmailTemplateDelegateTest {

    @Mock
    private CreateEmailTemplateRequestMapper requestMapper;

    @Mock
    private CreateEmailTemplateResponseMapper responseMapper;

    @Mock
    private CreateEmailTemplateService createEmailTemplateService;

    private CreateEmailTemplateDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CreateEmailTemplateDelegate(requestMapper, responseMapper, createEmailTemplateService);
    }

    @Test
    void shouldCreateTemplateAndReturn201Response(@Random CreateEmailTemplateRequestViewModel request,
                                                   @Random EmailTemplate domainModel,
                                                   @Random EmailTemplateResult serviceResult,
                                                   @Random CreateEmailTemplateResponseViewModel responseViewModel) {
        given(requestMapper.apply(request)).willReturn(domainModel);
        given(createEmailTemplateService.create(domainModel)).willReturn(serviceResult);
        given(responseMapper.apply(serviceResult)).willReturn(responseViewModel);

        var result = sut.create(request);

        assertThat(result).isNotNull();

        var inOrder = inOrder(requestMapper, createEmailTemplateService, responseMapper);
        then(requestMapper).should(inOrder).apply(request);
        then(createEmailTemplateService).should(inOrder).create(domainModel);
        then(responseMapper).should(inOrder).apply(serviceResult);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldCallMappersAndServiceInOrder(@Random CreateEmailTemplateRequestViewModel request,
                                              @Random EmailTemplate domainModel,
                                              @Random EmailTemplateResult serviceResult,
                                              @Random CreateEmailTemplateResponseViewModel responseViewModel) {
        given(requestMapper.apply(request)).willReturn(domainModel);
        given(createEmailTemplateService.create(domainModel)).willReturn(serviceResult);
        given(responseMapper.apply(serviceResult)).willReturn(responseViewModel);

        sut.create(request);

        var inOrder = inOrder(requestMapper, createEmailTemplateService, responseMapper);
        then(requestMapper).should(inOrder).apply(request);
        then(createEmailTemplateService).should(inOrder).create(any(EmailTemplate.class));
        then(responseMapper).should(inOrder).apply(any(EmailTemplateResult.class));
    }
}