package com.dndplatform.asset.adapter.inbound.messaging;

import com.dndplatform.asset.view.model.vm.DocumentIngestedEvent;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class DocumentIngestedEventConverterTest {

    @Mock
    private Message<?> message;

    private DocumentIngestedEventConverter sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentIngestedEventConverter();
    }

    @Test
    void shouldReturnTrueWhenPayloadIsJsonObjectAndTargetIsDocumentIngestedEvent() {
        given(message.getPayload()).willReturn(new JsonObject());

        assertThat(sut.canConvert(message, DocumentIngestedEvent.class)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenPayloadIsNotJsonObject() {
        given(message.getPayload()).willReturn("not a json object");

        assertThat(sut.canConvert(message, DocumentIngestedEvent.class)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenTargetIsNotDocumentIngestedEvent() {
        given(message.getPayload()).willReturn(new JsonObject());

        assertThat(sut.canConvert(message, String.class)).isFalse();
    }
}
