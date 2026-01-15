package com.dndplatform.notificationservice.adapter.inbound.messaging;

import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

public interface EmailSendRequestConsumer {
    CompletionStage<Void> consumeEmailRequest(Message<EmailSendRequestViewModel> message);
}
