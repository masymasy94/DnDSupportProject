package com.dndplatform.user.adapter.inbound.validate.mapper;

import com.dndplatform.user.domain.model.UserCredentialsValidate;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface UserCredentialsValidateMapper extends Function<UserCredentialsValidateViewModel, UserCredentialsValidate> {

    @Override
    UserCredentialsValidate apply(UserCredentialsValidateViewModel userCredentialsValidateViewModel);
}
