package com.dndplatform.auth.adapter.inbound.otplogin.request;

import com.dndplatform.auth.adapter.inbound.otplogin.request.mapper.RequestOtpLoginMapper;
import com.dndplatform.auth.domain.RequestOtpLoginService;
import com.dndplatform.auth.view.model.RequestOtpLoginResource;
import com.dndplatform.auth.view.model.vm.RequestOtpLoginViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class RequestOtpLoginDelegate implements RequestOtpLoginResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RequestOtpLoginMapper mapper;
    private final RequestOtpLoginService service;

    @Inject
    public RequestOtpLoginDelegate(RequestOtpLoginMapper mapper,
                                   RequestOtpLoginService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public Response requestOtpLogin(RequestOtpLoginViewModel vm) {
        log.info(() -> "OTP login requested for email: %s".formatted(vm.email()));
        service.requestOtpLogin(mapper.apply(vm));
        return Response.accepted().build();
    }
}
