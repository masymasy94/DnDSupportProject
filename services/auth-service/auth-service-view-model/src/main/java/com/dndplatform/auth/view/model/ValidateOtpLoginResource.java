package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.ValidateOtpLoginViewModel;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;

public interface ValidateOtpLoginResource {
    Response validateOtpLogin(@Valid ValidateOtpLoginViewModel vm);
}
