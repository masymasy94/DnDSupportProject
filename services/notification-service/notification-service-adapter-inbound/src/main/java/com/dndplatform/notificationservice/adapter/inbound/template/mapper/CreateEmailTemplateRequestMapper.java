package com.dndplatform.notificationservice.adapter.inbound.template.mapper;

import com.dndplatform.notificationservice.domain.model.EmailTemplate;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface CreateEmailTemplateRequestMapper extends Function<CreateEmailTemplateRequestViewModel, EmailTemplate> {

    @Override
    EmailTemplate apply(CreateEmailTemplateRequestViewModel request);
}
