package com.dndplatform.user.adapter.outbound.messaging;

import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import com.dndplatform.user.domain.event.UserRegisteredEvent;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EmailSendRepositoryRabbitMqTest {

    @Mock
    private Emitter<EmailSendRequestViewModel> emitter;

    private EmailSendRepositoryRabbitMq sut;

    @BeforeEach
    void setUp() {
        sut = new EmailSendRepositoryRabbitMq(emitter);
    }

    @Test
    void sendEmail_shouldEmitPayloadWithCorrectRecipient() {
        UserRegisteredEvent event = new UserRegisteredEvent(1L, "gandalf@middleearth.com", "gandalf_grey");

        sut.sendEmail(event);

        ArgumentCaptor<EmailSendRequestViewModel> captor = ArgumentCaptor.forClass(EmailSendRequestViewModel.class);
        then(emitter).should().send(captor.capture());
        EmailSendRequestViewModel payload = captor.getValue();
        assertThat(payload.to()).isEqualTo("gandalf@middleearth.com");
    }

    @Test
    void sendEmail_shouldEmitPayloadWithWelcomeTemplateId() {
        UserRegisteredEvent event = new UserRegisteredEvent(2L, "frodo@shire.com", "frodo_baggins");

        sut.sendEmail(event);

        ArgumentCaptor<EmailSendRequestViewModel> captor = ArgumentCaptor.forClass(EmailSendRequestViewModel.class);
        then(emitter).should().send(captor.capture());
        EmailSendRequestViewModel payload = captor.getValue();
        assertThat(payload.templateId()).isEqualTo(1L);
    }

    @Test
    void sendEmail_shouldDelegateToEmitter_exactlyOnce() {
        UserRegisteredEvent event = new UserRegisteredEvent(3L, "legolas@mirkwood.com", "legolas");

        sut.sendEmail(event);

        then(emitter).should().send(org.mockito.ArgumentMatchers.any(EmailSendRequestViewModel.class));
        then(emitter).shouldHaveNoMoreInteractions();
    }
}
