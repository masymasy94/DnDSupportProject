package com.dndplatform.auth.view.model;

import com.dndplatform.auth.view.model.vm.RequestPasswordResetViewModel;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;

public interface RequestPasswordResetResource {
    Response requestPasswordReset(@Valid RequestPasswordResetViewModel vm);
}
