package com.dndplatform.auth.adapter.outbound.rest.mapper;

import com.dndplatform.auth.domain.model.User;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.function.Function;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCredentialsValidateViewModelMapper extends Function<UserViewModel, User> {

    @Override
    User apply(UserViewModel userViewModel);
}
