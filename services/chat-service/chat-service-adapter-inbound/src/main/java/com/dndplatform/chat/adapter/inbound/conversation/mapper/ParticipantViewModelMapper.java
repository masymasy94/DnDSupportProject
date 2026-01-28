package com.dndplatform.chat.adapter.inbound.conversation.mapper;

import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.view.model.vm.ParticipantViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper(componentModel = "cdi")
public interface ParticipantViewModelMapper extends Function<ConversationParticipant, ParticipantViewModel> {

    @Override
    @Mapping(target = "role", expression = "java(participant.role() != null ? participant.role().name() : null)")
    ParticipantViewModel apply(ConversationParticipant participant);
}
