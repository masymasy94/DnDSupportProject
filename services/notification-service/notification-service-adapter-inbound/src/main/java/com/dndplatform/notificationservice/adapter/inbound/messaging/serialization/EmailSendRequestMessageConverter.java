package com.dndplatform.notificationservice.adapter.inbound.messaging.serialization;

import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import io.smallrye.reactive.messaging.MessageConverter;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.lang.reflect.Type;

@ApplicationScoped
public class EmailSendRequestMessageConverter implements MessageConverter {

    @Override
    public boolean canConvert(Message<?> in, Type target) {
        return in.getPayload() instanceof JsonObject
                && target.equals(EmailSendRequestViewModel.class);
    }

    @Override
    public Message<?> convert(Message<?> in, Type target) {
        JsonObject json = (JsonObject) in.getPayload();
        EmailSendRequestViewModel payload = json.mapTo(EmailSendRequestViewModel.class);
        return in.withPayload(payload);
    }
}
