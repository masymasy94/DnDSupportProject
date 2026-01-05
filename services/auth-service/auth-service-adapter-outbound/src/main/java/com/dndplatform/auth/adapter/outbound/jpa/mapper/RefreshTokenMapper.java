package com.dndplatform.auth.adapter.outbound.jpa.mapper;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface RefreshTokenMapper extends Function<RefreshTokenEntity, RefreshToken> {
    @Override
    @Mapping(source = "user.id", target = "userId")
    RefreshToken apply(RefreshTokenEntity refreshTokenEntity);
}
