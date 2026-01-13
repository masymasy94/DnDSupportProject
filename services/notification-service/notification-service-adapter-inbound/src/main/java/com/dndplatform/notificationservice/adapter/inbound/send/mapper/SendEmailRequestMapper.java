package com.dndplatform.notificationservice.adapter.inbound.send.mapper;

import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailAttachment;
import com.dndplatform.notificationservice.view.model.vm.EmailAttachmentViewModel;
import com.dndplatform.notificationservice.view.model.vm.SendEmailRequestViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Base64;
import java.util.function.Function;

@Mapper
public interface SendEmailRequestMapper extends Function<SendEmailRequestViewModel, Email> {

    @Override
    @Mapping(target = "attachments", source = "attachments")
    Email apply(SendEmailRequestViewModel request);


    @Mapping(target = "data", source = "data", qualifiedByName = "decodeBase64")
    EmailAttachment toAttachment(EmailAttachmentViewModel attachment);

    @Named("decodeBase64")
    default byte[] decodeBase64(String data) {
        if (data == null || data.isBlank()) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(data);
    }
}
