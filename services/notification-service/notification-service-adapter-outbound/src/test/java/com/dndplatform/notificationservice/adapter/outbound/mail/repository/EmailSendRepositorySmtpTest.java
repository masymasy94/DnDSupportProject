package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.domain.model.Email;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EmailSendRepositorySmtpTest {

    @Mock
    private ReactiveMailer mailer;

    @Mock
    private EmailToMailMapper emailToMailMapper;

    private EmailSendRepositorySmtp sut;

    @BeforeEach
    void setUp() {
        sut = new EmailSendRepositorySmtp(mailer, emailToMailMapper);
    }

    @Test
    void shouldMapEmailAndSendViaMail(@Random Email email) {
        var mail = new Mail();
        given(emailToMailMapper.apply(email)).willReturn(mail);
        given(mailer.send(mail)).willReturn(Uni.createFrom().voidItem());

        sut.send(email);

        var inOrder = inOrder(emailToMailMapper, mailer);
        then(emailToMailMapper).should(inOrder).apply(email);
        then(mailer).should(inOrder).send(mail);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldContinueEvenWhenMailerFails(@Random Email email) {
        var mail = new Mail();
        given(emailToMailMapper.apply(email)).willReturn(mail);
        given(mailer.send(any(Mail.class))).willReturn(Uni.createFrom().failure(new RuntimeException("SMTP error")));

        // Should not throw — failure is handled via subscribe callback (logging only)
        sut.send(email);

        then(emailToMailMapper).should().apply(email);
        then(mailer).should().send(any(Mail.class));
    }
}
