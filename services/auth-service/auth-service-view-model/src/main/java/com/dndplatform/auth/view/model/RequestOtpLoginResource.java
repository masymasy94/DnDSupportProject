package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.RequestOtpLoginViewModel;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;

public interface RequestOtpLoginResource {
    Response requestOtpLogin(@Valid RequestOtpLoginViewModel vm);
}
