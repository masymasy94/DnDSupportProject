package com.dndplatform.auth.adapter.outbound.rest.mapper;

import com.dndplatform.auth.domain.model.User;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface UserMapper extends Function<UserViewModel, User> {

    @Override
    User apply(UserViewModel user);
}
