package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.ResetPasswordViewModel;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;

public interface ResetPasswordResource {
    Response resetPassword(@Valid ResetPasswordViewModel vm);
}
