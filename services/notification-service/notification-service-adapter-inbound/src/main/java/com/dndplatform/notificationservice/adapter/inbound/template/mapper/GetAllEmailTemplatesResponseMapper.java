package com.dndplatform.notificationservice.adapter.inbound.template.mapper;

import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.view.model.vm.EmailTemplateItemViewModel;
import com.dndplatform.notificationservice.view.model.vm.GetAllEmailTemplatesResponseViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Mapper
public interface GetAllEmailTemplatesResponseMapper extends Function<List<EmailTemplateResult>, GetAllEmailTemplatesResponseViewModel> {

    @Override
    default GetAllEmailTemplatesResponseViewModel apply(List<EmailTemplateResult> results) {
        return new GetAllEmailTemplatesResponseViewModel(mapToItems(results));
    }

    List<EmailTemplateItemViewModel> mapToItems(List<EmailTemplateResult> results);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localDateTimeToString")
    EmailTemplateItemViewModel mapToItem(EmailTemplateResult result);

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
