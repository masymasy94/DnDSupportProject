package com.dndplatform.auth.adapter.outbound.jpa.mapper;

import com.dndplatform.auth.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.auth.domain.model.User;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface UserMapper extends Function<UserEntity, User> {
    @Override
    User apply(UserEntity userEntity);
}
