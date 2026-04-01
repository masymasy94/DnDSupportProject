package com.dndplatform.auth.adapter.inbound.passwordreset.request.mapper;

import com.dndplatform.auth.domain.model.RequestPasswordReset;
import com.dndplatform.auth.view.model.vm.RequestPasswordResetViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface RequestPasswordResetMapper extends Function<RequestPasswordResetViewModel, RequestPasswordReset> {

    @Override
    RequestPasswordReset apply(RequestPasswordResetViewModel vm);
}
