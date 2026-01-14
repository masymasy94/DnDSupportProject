package com.dndplatform.notificationservice.adapter.inbound.messaging;

import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;

public interface EmailSendRequestConsumer {
    void consumeEmailRequest(EmailSendRequestViewModel request);
}
