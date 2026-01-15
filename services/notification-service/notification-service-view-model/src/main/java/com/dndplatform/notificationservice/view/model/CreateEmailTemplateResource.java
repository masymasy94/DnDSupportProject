package com.dndplatform.notificationservice.view.model;

import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModel;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;

public interface CreateEmailTemplateResource {
    Response create(@Valid CreateEmailTemplateRequestViewModel request);
}
