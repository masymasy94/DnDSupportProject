package com.dndplatform.chat.adapter.inbound.message.mapper;

import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.view.model.vm.MessageViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper(componentModel = "cdi")
public interface MessageViewModelMapper extends Function<Message, MessageViewModel> {

    @Override
    @Mapping(target = "messageType", expression = "java(message.messageType() != null ? message.messageType().name() : null)")
    MessageViewModel apply(Message message);
}
