package com.dndplatform.chat.adapter.inbound.conversation.mapper;

import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper(componentModel = "cdi", uses = ParticipantViewModelMapper.class)
public interface ConversationViewModelMapper extends Function<Conversation, ConversationViewModel> {

    @Override
    @Mapping(target = "type", expression = "java(conversation.type() != null ? conversation.type().name() : null)")
    ConversationViewModel apply(Conversation conversation);
}
