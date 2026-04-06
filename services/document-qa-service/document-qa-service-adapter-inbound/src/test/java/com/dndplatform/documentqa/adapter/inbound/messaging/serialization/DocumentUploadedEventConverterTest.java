package com.dndplatform.documentqa.adapter.inbound.messaging.serialization;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.view.model.vm.DocumentUploadedEventVm;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentUploadedEventConverterTest {

    private DocumentUploadedEventConverter sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadedEventConverter();
    }

    // ========================
    // canConvert
    // ========================

    @Test
    void canConvert_shouldReturnTrueWhenPayloadIsJsonObjectAndTargetIsVm() {
        JsonObject json = new JsonObject();
        Message<JsonObject> message = Message.of(json);

        boolean result = sut.canConvert(message, DocumentUploadedEventVm.class);

        assertThat(result).isTrue();
    }

    @Test
    void canConvert_shouldReturnFalseWhenPayloadIsNotJsonObject() {
        Message<String> message = Message.of("{\"documentId\":\"doc-1\"}");

        boolean result = sut.canConvert(message, DocumentUploadedEventVm.class);

        assertThat(result).isFalse();
    }

    @Test
    void canConvert_shouldReturnFalseWhenTargetIsNotVmClass() {
        JsonObject json = new JsonObject();
        Message<JsonObject> message = Message.of(json);

        boolean result = sut.canConvert(message, String.class);

        assertThat(result).isFalse();
    }

    @Test
    void canConvert_shouldReturnFalseWhenPayloadIsNullObject() {
        Message<Object> message = Message.of("not-a-json-object");

        boolean result = sut.canConvert(message, DocumentUploadedEventVm.class);

        assertThat(result).isFalse();
    }

    // ========================
    // convert
    // ========================

    @Test
    void convert_shouldDeserializeJsonObjectToDocumentUploadedEventVm() {
        JsonObject json = new JsonObject()
                .put("documentId", "doc-42")
                .put("fileName", "rulebook.pdf")
                .put("contentType", "application/pdf")
                .put("uploadedBy", "99");
        Message<JsonObject> message = Message.of(json);

        Message<?> result = sut.convert(message, DocumentUploadedEventVm.class);

        assertThat(result).isNotNull();
        assertThat(result.getPayload()).isInstanceOf(DocumentUploadedEventVm.class);
        DocumentUploadedEventVm vm = (DocumentUploadedEventVm) result.getPayload();
        assertThat(vm.documentId()).isEqualTo("doc-42");
        assertThat(vm.fileName()).isEqualTo("rulebook.pdf");
        assertThat(vm.contentType()).isEqualTo("application/pdf");
        assertThat(vm.uploadedBy()).isEqualTo("99");
    }

    @Test
    void convert_shouldPreserveMessageMetadataStructure() {
        JsonObject json = new JsonObject()
                .put("documentId", "doc-1")
                .put("fileName", "spells.txt")
                .put("contentType", "text/plain")
                .put("uploadedBy", "1");
        Message<JsonObject> message = Message.of(json);

        Message<?> result = sut.convert(message, DocumentUploadedEventVm.class);

        assertThat(result.getPayload()).isNotNull();
    }

    @Test
    void convert_shouldMapAllFieldsCorrectly() {
        JsonObject json = new JsonObject()
                .put("documentId", "monster-manual-id")
                .put("fileName", "monsters.pdf")
                .put("contentType", "application/pdf")
                .put("uploadedBy", "dungeonmaster");
        Message<JsonObject> message = Message.of(json);

        Message<?> result = sut.convert(message, DocumentUploadedEventVm.class);
        DocumentUploadedEventVm vm = (DocumentUploadedEventVm) result.getPayload();

        assertThat(vm.documentId()).isEqualTo("monster-manual-id");
        assertThat(vm.fileName()).isEqualTo("monsters.pdf");
        assertThat(vm.uploadedBy()).isEqualTo("dungeonmaster");
    }
}
