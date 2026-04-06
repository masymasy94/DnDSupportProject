package com.dndplatform.auth.adapter.inbound.passwordreset.reset.mapper;

import com.dndplatform.auth.domain.model.ResetPassword;
import com.dndplatform.auth.view.model.vm.ResetPasswordViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ResetPasswordMapper extends Function<ResetPasswordViewModel, ResetPassword> {

    @Override
    ResetPassword apply(ResetPasswordViewModel vm);
}
