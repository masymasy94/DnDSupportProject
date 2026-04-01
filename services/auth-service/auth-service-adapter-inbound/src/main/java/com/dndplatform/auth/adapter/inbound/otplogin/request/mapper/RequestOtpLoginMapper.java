package com.dndplatform.auth.adapter.inbound.otplogin.request.mapper;

import com.dndplatform.auth.domain.model.RequestOtpLogin;
import com.dndplatform.auth.view.model.vm.RequestOtpLoginViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface RequestOtpLoginMapper extends Function<RequestOtpLoginViewModel, RequestOtpLogin> {

    @Override
    RequestOtpLogin apply(RequestOtpLoginViewModel vm);
}
