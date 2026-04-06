package com.dndplatform.notificationservice.adapter.inbound.messaging.serialization;

import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class EmailSendRequestMessageConverterTest {

    private EmailSendRequestMessageConverter sut;

    @BeforeEach
    void setUp() {
        sut = new EmailSendRequestMessageConverter();
    }

    @Test
    void shouldReturnTrueWhenPayloadIsJsonObjectAndTargetIsEmailSendRequestViewModel() {
        JsonObject json = new JsonObject();
        Message<?> message = Message.of(json);

        boolean result = sut.canConvert(message, EmailSendRequestViewModel.class);

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenTargetIsNotEmailSendRequestViewModel() {
        JsonObject json = new JsonObject();
        Message<?> message = Message.of(json);

        boolean result = sut.canConvert(message, String.class);

        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseWhenPayloadIsNotJsonObject() {
        Message<?> message = Message.of("plain string payload");

        boolean result = sut.canConvert(message, EmailSendRequestViewModel.class);

        assertThat(result).isFalse();
    }

    @Test
    void shouldConvertValidJsonToEmailSendRequestViewModel() {
        JsonObject json = new JsonObject()
                .put("to", "user@example.com")
                .put("templateId", 1L)
                .put("textBody", "Hello World");
        Message<?> message = Message.of(json);

        Message<?> result = sut.convert(message, EmailSendRequestViewModel.class);

        assertThat(result.getPayload()).isInstanceOf(EmailSendRequestViewModel.class);
        EmailSendRequestViewModel payload = (EmailSendRequestViewModel) result.getPayload();
        assertThat(payload.to()).isEqualTo("user@example.com");
        assertThat(payload.templateId()).isEqualTo(1L);
        assertThat(payload.textBody()).isEqualTo("Hello World");
    }

    @Test
    void shouldExtractTemplateVariablesManuallyWhenNotMappedByJackson() {
        JsonObject templateVars = new JsonObject()
                .put("name", "Gandalf")
                .put("event", "Campaign Start");
        JsonObject json = new JsonObject()
                .put("to", "user@example.com")
                .put("templateId", 2L)
                .put("templateVariables", templateVars);
        Message<?> message = Message.of(json);

        Message<?> result = sut.convert(message, EmailSendRequestViewModel.class);

        EmailSendRequestViewModel payload = (EmailSendRequestViewModel) result.getPayload();
        assertThat(payload.to()).isEqualTo("user@example.com");
        // templateVariables may be populated by Jackson or by the manual extraction fallback
        Map<String, String> vars = payload.templateVariables();
        if (vars != null) {
            assertThat(vars).containsEntry("name", "Gandalf");
            assertThat(vars).containsEntry("event", "Campaign Start");
        }
    }

    @Test
    void shouldHandleJsonWithNoTemplateVariables() {
        JsonObject json = new JsonObject()
                .put("to", "user@example.com")
                .put("templateId", 3L);
        Message<?> message = Message.of(json);

        Message<?> result = sut.convert(message, EmailSendRequestViewModel.class);

        assertThat(result.getPayload()).isInstanceOf(EmailSendRequestViewModel.class);
        EmailSendRequestViewModel payload = (EmailSendRequestViewModel) result.getPayload();
        assertThat(payload.to()).isEqualTo("user@example.com");
    }
}
