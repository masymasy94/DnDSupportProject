package com.dndplatform.chat.adapter.inbound.conversation.updatereadbyid;

import com.dndplatform.chat.domain.ConversationUpdateReadByIdService;
import com.dndplatform.chat.view.model.ConversationUpdateReadByIdResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class ConversationUpdateReadByIdDelegate implements ConversationUpdateReadByIdResource {

    private final ConversationUpdateReadByIdService service;

    @Inject
    public ConversationUpdateReadByIdDelegate(ConversationUpdateReadByIdService service) {
        this.service = service;
    }

    @Override
    public void updateReadById(Long conversationId, Long userId) {
        service.updateReadById(conversationId, userId);
    }
}
