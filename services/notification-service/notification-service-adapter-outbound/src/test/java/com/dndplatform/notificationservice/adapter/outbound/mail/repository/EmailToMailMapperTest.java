package com.dndplatform.notificationservice.adapter.outbound.mail.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailAttachment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EmailToMailMapperTest {

    private final EmailToMailMapper sut = new EmailToMailMapper();

    @Test
    void shouldMapToAndSubject(@Random String to, @Random String subject) {
        var email = new Email(to, null, null, subject, null, null, null, null, null);

        var mail = sut.apply(email);

        assertThat(mail.getTo()).containsExactly(to);
        assertThat(mail.getSubject()).isEqualTo(subject);
    }

    @Test
    void shouldMapCcAndBcc(@Random String to, @Random String subject,
                           @Random String cc1, @Random String cc2,
                           @Random String bcc1, @Random String bcc2) {
        var cc = List.of(cc1, cc2);
        var bcc = List.of(bcc1, bcc2);
        var email = new Email(to, cc, bcc, subject, null, null, null, null, null);

        var mail = sut.apply(email);

        assertThat(mail.getCc()).containsExactlyElementsOf(cc);
        assertThat(mail.getBcc()).containsExactlyElementsOf(bcc);
    }

    @Test
    void shouldNotSetCcWhenNull(@Random String to, @Random String subject) {
        var email = new Email(to, null, null, subject, null, null, null, null, null);

        var mail = sut.apply(email);

        assertThat(mail.getCc()).isNullOrEmpty();
    }

    @Test
    void shouldMapTextBody(@Random String to, @Random String subject, @Random String textBody) {
        var email = new Email(to, null, null, subject, textBody, null, null, null, null);

        var mail = sut.apply(email);

        assertThat(mail.getText()).isEqualTo(textBody);
    }

    @Test
    void shouldMapHtmlBody(@Random String to, @Random String subject, @Random String htmlBody) {
        var email = new Email(to, null, null, subject, null, htmlBody, null, null, null);

        var mail = sut.apply(email);

        assertThat(mail.getHtml()).isEqualTo(htmlBody);
    }

    @Test
    void shouldMapAttachments(@Random String to, @Random String subject,
                              @Random String fileName, @Random String contentType) {
        var data = new byte[]{1, 2, 3};
        var attachment = new EmailAttachment(fileName, contentType, data);
        var email = new Email(to, null, null, subject, null, null, null, null, List.of(attachment));

        var mail = sut.apply(email);

        assertThat(mail.getAttachments()).hasSize(1);
        assertThat(mail.getAttachments().get(0).getName()).isEqualTo(fileName);
        assertThat(mail.getAttachments().get(0).getContentType()).isEqualTo(contentType);
    }

    @Test
    void shouldNotSetTextBodyWhenNull(@Random String to, @Random String subject) {
        var email = new Email(to, null, null, subject, null, null, null, null, null);

        var mail = sut.apply(email);

        assertThat(mail.getText()).isNull();
    }
}
