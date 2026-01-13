package com.dndplatform.user.domain.event;

import com.dndplatform.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface UserRegisteredEventMapper extends Function<User, UserRegisteredEvent> {

    @Override
    @Mapping(source = "id", target = "userId")
    UserRegisteredEvent apply(User user);
}
