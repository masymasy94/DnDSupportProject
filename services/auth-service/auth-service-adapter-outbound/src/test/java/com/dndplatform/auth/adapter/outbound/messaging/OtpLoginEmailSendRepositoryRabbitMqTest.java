package com.dndplatform.auth.adapter.outbound.messaging;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class OtpLoginEmailSendRepositoryRabbitMqTest {

    @Mock
    private Emitter<EmailSendRequestViewModel> emitter;

    private OtpLoginEmailSendRepositoryRabbitMq sut;

    @BeforeEach
    void setUp() {
        sut = new OtpLoginEmailSendRepositoryRabbitMq(emitter);
    }

    @Test
    void shouldSendEmailRequestWithCorrectPayload(@Random String email, @Random String otpCode) {
        sut.sendOtpLoginEmail(email, otpCode);

        var captor = ArgumentCaptor.forClass(EmailSendRequestViewModel.class);
        then(emitter).should().send(captor.capture());

        var payload = captor.getValue();
        assertThat(payload.to()).isEqualTo(email);
        assertThat(payload.templateId()).isEqualTo(3L);
        assertThat(payload.templateVariables()).containsEntry("otpCode", otpCode);
    }
}
