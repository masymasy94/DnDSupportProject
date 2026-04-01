package com.dndplatform.auth.adapter.outbound.jpa.mapper;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
import com.dndplatform.auth.domain.model.OtpLoginToken;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface OtpLoginTokenMapper extends Function<OtpLoginEntity, OtpLoginToken> {

    @Override
    OtpLoginToken apply(OtpLoginEntity entity);
}
