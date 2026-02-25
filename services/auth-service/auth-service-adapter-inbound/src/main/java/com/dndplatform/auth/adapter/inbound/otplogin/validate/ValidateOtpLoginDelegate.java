package com.dndplatform.auth.adapter.inbound.otplogin.validate;

import com.dndplatform.auth.adapter.inbound.login.mapper.LoginResponseViewModelMapper;
import com.dndplatform.auth.adapter.inbound.otplogin.validate.mapper.ValidateOtpLoginMapper;
import com.dndplatform.auth.domain.ValidateOtpLoginService;
import com.dndplatform.auth.view.model.ValidateOtpLoginResource;
import com.dndplatform.auth.view.model.vm.ValidateOtpLoginViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class ValidateOtpLoginDelegate implements ValidateOtpLoginResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ValidateOtpLoginMapper mapper;
    private final LoginResponseViewModelMapper loginResponseViewModelMapper;
    private final ValidateOtpLoginService service;

    @Inject
    public ValidateOtpLoginDelegate(ValidateOtpLoginMapper mapper,
                                    LoginResponseViewModelMapper loginResponseViewModelMapper,
                                    ValidateOtpLoginService service) {
        this.mapper = mapper;
        this.loginResponseViewModelMapper = loginResponseViewModelMapper;
        this.service = service;
    }

    @Override
    public Response validateOtpLogin(ValidateOtpLoginViewModel vm) {
        log.info(() -> "OTP login validation for email: %s".formatted(vm.email()));

        var model = mapper.apply(vm);
        var response = service.validateOtpLogin(model);
        var responseBody = loginResponseViewModelMapper.apply(response);

        log.info(() -> "OTP login successful for email: %s".formatted(vm.email()));
        return Response.status(Response.Status.CREATED).entity(responseBody).build();
    }
}
