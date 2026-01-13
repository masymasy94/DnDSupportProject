package com.dndplatform.notificationservice.view.model;

import com.dndplatform.notificationservice.view.model.vm.SendEmailRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.SendEmailResponseViewModel;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;

public interface SendEmailResource {
    Response syncSend(@Valid SendEmailRequestViewModel request);
}
