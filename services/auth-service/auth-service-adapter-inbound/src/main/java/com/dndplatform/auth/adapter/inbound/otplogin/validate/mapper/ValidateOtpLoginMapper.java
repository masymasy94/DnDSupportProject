package com.dndplatform.auth.adapter.inbound.otplogin.validate.mapper;

import com.dndplatform.auth.domain.model.ValidateOtpLogin;
import com.dndplatform.auth.view.model.vm.ValidateOtpLoginViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ValidateOtpLoginMapper extends Function<ValidateOtpLoginViewModel, ValidateOtpLogin> {

    @Override
    ValidateOtpLogin apply(ValidateOtpLoginViewModel vm);
}
