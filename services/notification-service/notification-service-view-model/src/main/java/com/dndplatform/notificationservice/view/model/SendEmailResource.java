package com.dndplatform.notificationservice.view.model;

import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;

public interface SendEmailResource {
    Response syncSend(@Valid EmailSendRequestViewModel request);
}
