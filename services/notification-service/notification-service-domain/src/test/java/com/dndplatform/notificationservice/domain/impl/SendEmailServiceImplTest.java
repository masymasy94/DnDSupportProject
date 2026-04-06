package com.dndplatform.notificationservice.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailTemplateDetails;
import com.dndplatform.notificationservice.domain.repository.EmailSendRepository;
import com.dndplatform.notificationservice.domain.repository.FindEmailTemplateByIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SendEmailServiceImplTest {

    @Mock
    private EmailSendRepository emailSendRepository;

    @Mock
    private FindEmailTemplateByIdRepository findEmailTemplateByIdRepository;

    private SendEmailServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new SendEmailServiceImpl(emailSendRepository, findEmailTemplateByIdRepository);
    }

    @Test
    void shouldSendEmailWhenTemplateFound(@Random Email email,
                                          @Random EmailTemplateDetails template) {
        var emailWithTemplateId = new Email(
                email.to(),
                email.cc(),
                email.bcc(),
                email.subject(),
                email.textBody(),
                email.htmlBody(),
                template.id(),
                email.templateVariables(),
                email.attachments()
        );

        given(findEmailTemplateByIdRepository.findById(template.id()))
                .willReturn(java.util.Optional.of(template));

        sut.send(emailWithTemplateId);

        var inOrder = inOrder(findEmailTemplateByIdRepository, emailSendRepository);
        then(findEmailTemplateByIdRepository).should(inOrder).findById(template.id());
        then(emailSendRepository).should(inOrder).send(any(Email.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReplaceTemplateVariablesInContent(@Random Email email,
                                                  @Random EmailTemplateDetails template) {
        var templateWithPlaceholders = new EmailTemplateDetails(
                template.id(),
                template.name(),
                template.subject(),
                "Hello {name}, welcome to {place}!"
        );

        var variables = Map.of("name", "John", "place", "Wonderland");

        var emailWithTemplateId = new Email(
                email.to(),
                email.cc(),
                email.bcc(),
                email.subject(),
                email.textBody(),
                email.htmlBody(),
                templateWithPlaceholders.id(),
                variables,
                email.attachments()
        );

        given(findEmailTemplateByIdRepository.findById(templateWithPlaceholders.id()))
                .willReturn(java.util.Optional.of(templateWithPlaceholders));

        sut.send(emailWithTemplateId);

        then(emailSendRepository).should().send(any(Email.class));
    }

    @Test
    void shouldSendEmailWhenNoTemplateVariables(@Random Email email,
                                                @Random EmailTemplateDetails template) {
        var emailWithTemplateId = new Email(
                email.to(),
                email.cc(),
                email.bcc(),
                email.subject(),
                email.textBody(),
                email.htmlBody(),
                template.id(),
                null,
                email.attachments()
        );

        given(findEmailTemplateByIdRepository.findById(template.id()))
                .willReturn(java.util.Optional.of(template));

        sut.send(emailWithTemplateId);

        then(emailSendRepository).should().send(any(Email.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTemplateNotFound(@Random Email email) {
        var templateId = 999L;
        var emailWithTemplateId = new Email(
                email.to(),
                email.cc(),
                email.bcc(),
                email.subject(),
                email.textBody(),
                email.htmlBody(),
                templateId,
                email.templateVariables(),
                email.attachments()
        );

        given(findEmailTemplateByIdRepository.findById(templateId))
                .willReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> sut.send(emailWithTemplateId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.valueOf(templateId));

        then(emailSendRepository).shouldHaveNoInteractions();
    }
}