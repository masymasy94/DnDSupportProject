package com.dndplatform.auth.adapter.inbound.login.mapper;

import com.dndplatform.auth.domain.model.CreateLoginTokens;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface CreateLoginTokensMapper extends Function<CreateLoginTokensViewModel, CreateLoginTokens> {

    @Override
    CreateLoginTokens apply(CreateLoginTokensViewModel createLoginCredentialsViewModel);
}
