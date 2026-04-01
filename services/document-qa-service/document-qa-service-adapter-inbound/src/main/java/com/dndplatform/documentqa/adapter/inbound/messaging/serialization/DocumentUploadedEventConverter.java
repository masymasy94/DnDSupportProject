package com.dndplatform.documentqa.adapter.inbound.messaging.serialization;

import com.dndplatform.documentqa.view.model.vm.DocumentUploadedEventVm;
import io.smallrye.reactive.messaging.MessageConverter;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.lang.reflect.Type;

@ApplicationScoped
public class DocumentUploadedEventConverter implements MessageConverter {

    @Override
    public boolean canConvert(Message<?> in, Type target) {
        return in.getPayload() instanceof JsonObject
                && target.equals(DocumentUploadedEventVm.class);
    }

    @Override
    public Message<?> convert(Message<?> in, Type target) {
        JsonObject json = (JsonObject) in.getPayload();
        DocumentUploadedEventVm event = json.mapTo(DocumentUploadedEventVm.class);
        return in.withPayload(event);
    }
}
