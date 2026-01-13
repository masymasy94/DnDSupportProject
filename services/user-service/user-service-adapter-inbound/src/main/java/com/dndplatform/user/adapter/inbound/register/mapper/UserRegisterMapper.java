package com.dndplatform.user.adapter.inbound.register.mapper;

import com.dndplatform.user.domain.model.UserRegister;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface UserRegisterMapper extends Function<UserRegisterViewModel, UserRegister> {

    @Override
    UserRegister apply(UserRegisterViewModel userRegisterViewModel);
}
