package com.dndplatform.auth.adapter.inbound.refresh.mapper;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.view.model.vm.RefreshLoginTokenResponseViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.function.Function;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RefreshLoginTokenResponseViewModelMapper extends Function<CreateLoginTokenResponse, RefreshLoginTokenResponseViewModel> {

    @Override
    RefreshLoginTokenResponseViewModel apply(CreateLoginTokenResponse createLoginTokenResponse);

}
