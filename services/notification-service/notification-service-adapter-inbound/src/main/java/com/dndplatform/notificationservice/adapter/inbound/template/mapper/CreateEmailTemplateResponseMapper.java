package com.dndplatform.notificationservice.adapter.inbound.template.mapper;

import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateResponseViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.function.Function;

@Mapper
public interface CreateEmailTemplateResponseMapper extends Function<EmailTemplateResult, CreateEmailTemplateResponseViewModel> {

    @Override
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localDateTimeToString")
    CreateEmailTemplateResponseViewModel apply(EmailTemplateResult result);

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
