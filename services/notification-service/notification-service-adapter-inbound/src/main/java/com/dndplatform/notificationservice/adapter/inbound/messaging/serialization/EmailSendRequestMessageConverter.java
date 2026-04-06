package com.dndplatform.notificationservice.adapter.inbound.messaging.serialization;

import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModelBuilder;
import io.smallrye.reactive.messaging.MessageConverter;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailSendRequestMessageConverter implements MessageConverter {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public boolean canConvert(Message<?> in, Type target) {
        return in.getPayload() instanceof JsonObject
                && target.equals(EmailSendRequestViewModel.class);
    }

    @Override
    public Message<?> convert(Message<?> in, Type target) {
        JsonObject json = (JsonObject) in.getPayload();
        log.fine(() -> "Raw RabbitMQ JSON: " + json.encode());

        EmailSendRequestViewModel base = json.mapTo(EmailSendRequestViewModel.class);

        // Manually extract templateVariables — Jackson may not map it for records
        Map<String, String> templateVariables = base.templateVariables();
        if (templateVariables == null && json.containsKey("templateVariables")) {
            JsonObject vars = json.getJsonObject("templateVariables");
            if (vars != null) {
                templateVariables = new HashMap<>();
                for (String key : vars.fieldNames()) {
                    templateVariables.put(key, vars.getString(key));
                }
                log.info(() -> "Manually extracted templateVariables: " + vars.encode());
            }
        }

        EmailSendRequestViewModel payload = EmailSendRequestViewModelBuilder.toBuilder(base)
                .withTemplateVariables(templateVariables)
                .build();

        return in.withPayload(payload);
    }
}
