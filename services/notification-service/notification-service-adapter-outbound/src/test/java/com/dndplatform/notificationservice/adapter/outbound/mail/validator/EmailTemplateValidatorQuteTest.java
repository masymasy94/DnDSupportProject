package com.dndplatform.notificationservice.adapter.outbound.mail.validator;

import com.dndplatform.common.exception.InvalidTemplateException;
import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class EmailTemplateValidatorQuteTest {

    @Mock
    private Engine engine;

    private EmailTemplateValidatorQute sut;

    @BeforeEach
    void setUp() {
        sut = new EmailTemplateValidatorQute(engine);
    }

    @Test
    void shouldNotThrowWhenTemplateIsValid() {
        var html = "<p>Hello {name}</p>";
        given(engine.parse(html)).willReturn(mock(Template.class));

        assertThatNoException().isThrownBy(() -> sut.validateSyntax(html));

        then(engine).should().parse(html);
    }

    @Test
    void shouldThrowInvalidTemplateExceptionWhenSyntaxIsInvalid() {
        var html = "<p>{unclosed</p>";
        given(engine.parse(anyString())).willThrow(TemplateException.builder().message("Syntax error").build());

        assertThatThrownBy(() -> sut.validateSyntax(html))
                .isInstanceOf(InvalidTemplateException.class)
                .hasMessageContaining("Invalid template syntax");

        then(engine).should().parse(html);
    }
}
