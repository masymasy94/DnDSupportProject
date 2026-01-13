package com.dndplatform.user.adapter.outbound.jpa.mapper;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface UserMapper extends Function<UserEntity, User> {

    @Override
    @Mapping(target = "id", source = "id")
    User apply(UserEntity userEntity);
}
