package com.dndplatform.notificationservice.adapter.inbound.messaging.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.inbound.send.mapper.SendEmailRequestMapper;
import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EmailSendRequestConsumerDelegateTest {

    @Mock
    private SendEmailRequestMapper requestMapper;

    @Mock
    private SendEmailService sendEmailService;

    @Mock
    private Message<EmailSendRequestViewModel> message;

    private EmailSendRequestConsumerDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EmailSendRequestConsumerDelegate(requestMapper, sendEmailService);
    }

    @Test
    void shouldConsumeEmailRequestAndSendEmail(@Random EmailSendRequestViewModel payload,
                                                 @Random Email domainModel) {
        given(message.getPayload()).willReturn(payload);
        given(requestMapper.apply(payload)).willReturn(domainModel);

        CompletionStage<Void> result = sut.consumeEmailRequest(message);

        assertThat(result).isNotNull();

        var inOrder = inOrder(requestMapper, sendEmailService);
        then(requestMapper).should(inOrder).apply(payload);
        then(sendEmailService).should(inOrder).send(domainModel);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenPayloadIsNull() {
        given(message.getPayload()).willReturn(null);

        assertThatThrownBy(() -> sut.consumeEmailRequest(message))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("payload");

        then(requestMapper).shouldHaveNoInteractions();
        then(sendEmailService).shouldHaveNoInteractions();
    }

    @Test
    void shouldCallMapperBeforeService(@Random EmailSendRequestViewModel payload,
                                        @Random Email domainModel) {
        given(message.getPayload()).willReturn(payload);
        given(requestMapper.apply(payload)).willReturn(domainModel);

        sut.consumeEmailRequest(message);

        var inOrder = inOrder(requestMapper, sendEmailService);
        then(requestMapper).should(inOrder).apply(any(EmailSendRequestViewModel.class));
        then(sendEmailService).should(inOrder).send(any(Email.class));
    }
}