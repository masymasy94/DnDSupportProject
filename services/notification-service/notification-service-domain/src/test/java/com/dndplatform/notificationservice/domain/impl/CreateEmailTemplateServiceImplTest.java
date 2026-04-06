package com.dndplatform.notificationservice.domain.impl;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateCreateRepository;
import com.dndplatform.notificationservice.domain.repository.EmailTemplateExistsByNameRepository;
import com.dndplatform.notificationservice.domain.validator.EmailTemplateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CreateEmailTemplateServiceImplTest {

    @Mock
    private EmailTemplateValidator emailTemplateValidator;

    @Mock
    private EmailTemplateExistsByNameRepository emailTemplateExistsByNameRepository;

    @Mock
    private EmailTemplateCreateRepository emailTemplateCreateRepository;

    private CreateEmailTemplateServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CreateEmailTemplateServiceImpl(
                emailTemplateValidator,
                emailTemplateExistsByNameRepository,
                emailTemplateCreateRepository
        );
    }

    @Test
    void shouldCreateTemplateWhenValidInput(@Random EmailTemplate template,
                                            @Random EmailTemplateResult expected) {
        given(emailTemplateCreateRepository.save(any(EmailTemplate.class))).willReturn(expected);

        var result = sut.create(template);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(emailTemplateValidator, emailTemplateExistsByNameRepository, emailTemplateCreateRepository);
        then(emailTemplateValidator).should(inOrder).validateSyntax(template.htmlContent());
        then(emailTemplateExistsByNameRepository).should(inOrder).checkNameNotExists(template.name());
        then(emailTemplateCreateRepository).should(inOrder).save(template);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowConflictExceptionWhenNameAlreadyExists(@Random EmailTemplate template) {
        willThrow(new ConflictException("Email template with name '" + template.name() + "' already exists"))
                .given(emailTemplateExistsByNameRepository).checkNameNotExists(template.name());

        assertThatThrownBy(() -> sut.create(template))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining(template.name());

        then(emailTemplateValidator).should().validateSyntax(template.htmlContent());
        then(emailTemplateCreateRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldCallValidatorBeforeCheckingNameExists(@Random EmailTemplate template,
                                                     @Random EmailTemplateResult expected) {
        given(emailTemplateCreateRepository.save(any(EmailTemplate.class))).willReturn(expected);

        sut.create(template);

        var inOrder = inOrder(emailTemplateValidator, emailTemplateExistsByNameRepository);
        then(emailTemplateValidator).should(inOrder).validateSyntax(template.htmlContent());
        then(emailTemplateExistsByNameRepository).should(inOrder).checkNameNotExists(template.name());
    }
}