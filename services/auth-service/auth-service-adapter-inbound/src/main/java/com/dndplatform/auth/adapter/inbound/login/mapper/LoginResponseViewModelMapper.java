package com.dndplatform.auth.adapter.inbound.login.mapper;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensResponseViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface LoginResponseViewModelMapper extends Function<CreateLoginTokenResponse, CreateLoginTokensResponseViewModel> {
    @Override
    CreateLoginTokensResponseViewModel apply(CreateLoginTokenResponse createLoginTokenResponse);
}
