package com.dndplatform.notificationservice.adapter.inbound.send;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.adapter.inbound.send.mapper.SendEmailRequestMapper;
import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
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
class SyncSendEmailDelegateTest {

    @Mock
    private SendEmailRequestMapper requestMapper;

    @Mock
    private SendEmailService sendEmailService;

    private SyncSendEmailDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new SyncSendEmailDelegate(requestMapper, sendEmailService);
    }

    @Test
    void shouldSendEmailAndReturn202Accepted(@Random EmailSendRequestViewModel request,
                                              @Random Email domainModel) {
        given(requestMapper.apply(request)).willReturn(domainModel);

        var result = sut.syncSend(request);

        assertThat(result).isNotNull();

        var inOrder = inOrder(requestMapper, sendEmailService);
        then(requestMapper).should(inOrder).apply(request);
        then(sendEmailService).should(inOrder).send(domainModel);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldCallMapperBeforeService(@Random EmailSendRequestViewModel request,
                                        @Random Email domainModel) {
        given(requestMapper.apply(request)).willReturn(domainModel);

        sut.syncSend(request);

        var inOrder = inOrder(requestMapper, sendEmailService);
        then(requestMapper).should(inOrder).apply(request);
        then(sendEmailService).should(inOrder).send(any(Email.class));
    }
}