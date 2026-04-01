package com.dndplatform.asset.adapter.inbound.messaging;

import com.dndplatform.asset.view.model.vm.DocumentIngestedEvent;
import io.smallrye.reactive.messaging.MessageConverter;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.lang.reflect.Type;

@ApplicationScoped
public class DocumentIngestedEventConverter implements MessageConverter {

    @Override
    public boolean canConvert(Message<?> in, Type target) {
        return in.getPayload() instanceof JsonObject
                && target.equals(DocumentIngestedEvent.class);
    }

    @Override
    public Message<?> convert(Message<?> in, Type target) {
        JsonObject json = (JsonObject) in.getPayload();
        DocumentIngestedEvent event = json.mapTo(DocumentIngestedEvent.class);
        return in.withPayload(event);
    }
}
