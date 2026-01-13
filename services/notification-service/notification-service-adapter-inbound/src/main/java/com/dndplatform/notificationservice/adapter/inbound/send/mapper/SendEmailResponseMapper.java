package com.dndplatform.notificationservice.adapter.inbound.send.mapper;

import com.dndplatform.notificationservice.domain.model.EmailResult;
import com.dndplatform.notificationservice.view.model.vm.SendEmailResponseViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.function.Function;

@Mapper
public interface SendEmailResponseMapper extends Function<EmailResult, SendEmailResponseViewModel> {

    @Override
    @Mapping(target = "status", expression = "java(result.status().name())")
    @Mapping(target = "sentAt", source = "sentAt", qualifiedByName = "instantToString")
    SendEmailResponseViewModel apply(EmailResult result);

    @Named("instantToString")
    default String instantToString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }
}
