package com.dndplatform.user.adapter.inbound.register.mapper;

import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.view.model.vm.UserViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface UserViewModelMapper extends Function<User, UserViewModel> {

    @Override
    UserViewModel apply(User user);
}
