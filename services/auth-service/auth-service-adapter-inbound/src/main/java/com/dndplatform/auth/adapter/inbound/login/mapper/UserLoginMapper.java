package com.dndplatform.auth.adapter.inbound.login.mapper;

import com.dndplatform.auth.domain.model.UserLogin;
import com.dndplatform.auth.view.model.vm.UserLoginViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface UserLoginMapper extends Function<UserLoginViewModel, UserLogin> {

    @Override
    UserLogin apply(UserLoginViewModel loginCredentialsViewModel);
}
