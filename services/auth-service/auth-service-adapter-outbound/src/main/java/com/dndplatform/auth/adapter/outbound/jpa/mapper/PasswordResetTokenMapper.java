package com.dndplatform.auth.adapter.outbound.jpa.mapper;

import com.dndplatform.auth.adapter.outbound.jpa.entity.PasswordResetEntity;
import com.dndplatform.auth.domain.model.PasswordResetToken;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface PasswordResetTokenMapper extends Function<PasswordResetEntity, PasswordResetToken> {

    @Override
    PasswordResetToken apply(PasswordResetEntity entity);
}
