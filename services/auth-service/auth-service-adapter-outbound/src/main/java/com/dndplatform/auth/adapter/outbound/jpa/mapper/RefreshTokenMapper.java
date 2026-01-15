package com.dndplatform.auth.adapter.outbound.jpa.mapper;

import com.dndplatform.auth.adapter.outbound.jpa.entity.RefreshTokenEntity;
import com.dndplatform.auth.domain.model.RefreshToken;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface RefreshTokenMapper extends Function<RefreshTokenEntity, RefreshToken> {
    @Override
    RefreshToken apply(RefreshTokenEntity refreshTokenEntity);
}
