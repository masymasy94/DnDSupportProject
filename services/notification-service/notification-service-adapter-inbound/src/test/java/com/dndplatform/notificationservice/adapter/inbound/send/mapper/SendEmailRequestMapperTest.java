package com.dndplatform.notificationservice.adapter.inbound.send.mapper;

import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.view.model.vm.EmailAttachmentViewModel;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModelBuilder;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class SendEmailRequestMapperTest {

    private SendEmailRequestMapper sut;

    @BeforeEach
    void setUp() {
        sut = new SendEmailRequestMapperImpl();
    }

    @Test
    void shouldMapViewModelToEmail() {
        var request = EmailSendRequestViewModelBuilder.builder()
                .withTo("user@example.com")
                .withCc(List.of("cc@example.com"))
                .withBcc(List.of("bcc@example.com"))
                .withTemplateId(1L)
                .withTextBody("Hello")
                .withTemplateVariables(java.util.Map.of("name", "Gandalf"))
                .withAttachments(null)
                .build();

        Email result = sut.apply(request);

        assertThat(result.to()).isEqualTo("user@example.com");
        assertThat(result.cc()).containsExactly("cc@example.com");
        assertThat(result.bcc()).containsExactly("bcc@example.com");
        assertThat(result.templateId()).isEqualTo(1L);
        assertThat(result.textBody()).isEqualTo("Hello");
        assertThat(result.templateVariables()).containsEntry("name", "Gandalf");
        assertThat(result.subject()).isNull();
        assertThat(result.htmlBody()).isNull();
    }

    @Test
    void shouldDecodeBase64AttachmentData() {
        byte[] originalBytes = "Hello World".getBytes();
        String encoded = Base64.getEncoder().encodeToString(originalBytes);

        var attachment = new EmailAttachmentViewModel("file.txt", "text/plain", encoded);
        var request = EmailSendRequestViewModelBuilder.builder()
                .withTo("user@example.com")
                .withTemplateId(1L)
                .withAttachments(List.of(attachment))
                .withCc(null)
                .withBcc(null)
                .withTextBody(null)
                .withTemplateVariables(null)
                .build();

        Email result = sut.apply(request);

        assertThat(result.attachments()).hasSize(1);
        assertThat(result.attachments().get(0).fileName()).isEqualTo("file.txt");
        assertThat(result.attachments().get(0).contentType()).isEqualTo("text/plain");
        assertThat(result.attachments().get(0).data()).isEqualTo(originalBytes);
    }

    @Test
    void shouldReturnEmptyByteArrayWhenBase64DataIsNull() {
        SendEmailRequestMapperImpl mapperImpl = (SendEmailRequestMapperImpl) sut;

        byte[] result = mapperImpl.decodeBase64(null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyByteArrayWhenBase64DataIsBlank() {
        SendEmailRequestMapperImpl mapperImpl = (SendEmailRequestMapperImpl) sut;

        byte[] result = mapperImpl.decodeBase64("   ");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldDecodeValidBase64() {
        SendEmailRequestMapperImpl mapperImpl = (SendEmailRequestMapperImpl) sut;
        byte[] expectedBytes = "test data".getBytes();
        String encoded = Base64.getEncoder().encodeToString(expectedBytes);

        byte[] result = mapperImpl.decodeBase64(encoded);

        assertThat(result).isEqualTo(expectedBytes);
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        Email result = sut.apply(null);

        assertThat(result).isNull();
    }
}
