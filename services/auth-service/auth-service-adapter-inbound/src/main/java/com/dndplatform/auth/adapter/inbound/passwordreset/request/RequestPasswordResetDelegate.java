package com.dndplatform.auth.adapter.inbound.passwordreset.request;

import com.dndplatform.auth.adapter.inbound.passwordreset.request.mapper.RequestPasswordResetMapper;
import com.dndplatform.auth.domain.RequestPasswordResetService;
import com.dndplatform.auth.view.model.RequestPasswordResetResource;
import com.dndplatform.auth.view.model.vm.RequestPasswordResetViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class RequestPasswordResetDelegate implements RequestPasswordResetResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RequestPasswordResetMapper mapper;
    private final RequestPasswordResetService service;

    @Inject
    public RequestPasswordResetDelegate(RequestPasswordResetMapper mapper,
                                        RequestPasswordResetService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public Response requestPasswordReset(RequestPasswordResetViewModel vm) {
        log.info(() -> "Password reset requested for email: %s".formatted(vm.email()));
        service.requestPasswordReset(mapper.apply(vm));
        return Response.accepted().build();
    }
}
