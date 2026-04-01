package com.dndplatform.auth.adapter.inbound.passwordreset.reset;

import com.dndplatform.auth.adapter.inbound.passwordreset.reset.mapper.ResetPasswordMapper;
import com.dndplatform.auth.domain.ResetPasswordService;
import com.dndplatform.auth.view.model.ResetPasswordResource;
import com.dndplatform.auth.view.model.vm.ResetPasswordViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class ResetPasswordDelegate implements ResetPasswordResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ResetPasswordMapper mapper;
    private final ResetPasswordService service;

    @Inject
    public ResetPasswordDelegate(ResetPasswordMapper mapper,
                                 ResetPasswordService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public Response resetPassword(ResetPasswordViewModel vm) {
        log.info("Processing password reset with token");
        service.resetPassword(mapper.apply(vm));
        return Response.noContent().build();
    }
}
