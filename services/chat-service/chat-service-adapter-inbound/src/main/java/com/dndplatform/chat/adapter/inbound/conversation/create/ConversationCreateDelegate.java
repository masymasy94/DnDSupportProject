package com.dndplatform.chat.adapter.inbound.conversation.create;

import com.dndplatform.chat.adapter.inbound.conversation.mapper.ConversationViewModelMapper;
import com.dndplatform.chat.domain.ConversationCreateService;
import com.dndplatform.chat.domain.model.ConversationType;
import com.dndplatform.chat.view.model.ConversationCreateResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.chat.view.model.vm.CreateConversationViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class ConversationCreateDelegate implements ConversationCreateResource {

    private final ConversationCreateService conversationCreateService;
    private final ConversationViewModelMapper mapper;

    @Inject
    public ConversationCreateDelegate(ConversationCreateService conversationCreateService,
                                      ConversationViewModelMapper mapper) {
        this.conversationCreateService = conversationCreateService;
        this.mapper = mapper;
    }

    @Override
    public ConversationViewModel create(Long userId, CreateConversationViewModel request) {
        ConversationType type = ConversationType.valueOf(request.type());

        if (type == ConversationType.DIRECT) {
            validateDirectConversationParticipants(request.participantIds());
        }

        var conversation = conversationCreateService.create(type, request.name(), userId, request.participantIds());
        return mapper.apply(conversation);
    }

    private void validateDirectConversationParticipants(java.util.List<Long> participantIds) {
        if (participantIds == null || participantIds.isEmpty()) {
            throw new IllegalArgumentException("Direct conversation requires at least one other participant");
        }
    }
}
