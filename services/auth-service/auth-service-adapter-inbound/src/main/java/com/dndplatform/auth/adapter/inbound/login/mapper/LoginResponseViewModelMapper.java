package com.dndplatform.auth.adapter.inbound.login.mapper;

import com.dndplatform.auth.domain.model.LoginResponse;
import com.dndplatform.auth.view.model.vm.LoginResponseViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface LoginResponseViewModelMapper extends Function<LoginResponse, LoginResponseViewModel> {
    @Override
    LoginResponseViewModel apply(LoginResponse loginResponse);
}
