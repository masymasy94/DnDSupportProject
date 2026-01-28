package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.ConversationCreateService;
import com.dndplatform.chat.domain.DirectConversationCreateService;
import com.dndplatform.chat.domain.GroupConversationCreateService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ConversationCreateServiceImpl implements ConversationCreateService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final DirectConversationCreateService directConversationCreateService;
    private final GroupConversationCreateService groupConversationCreateService;

    @Inject
    public ConversationCreateServiceImpl(DirectConversationCreateService directConversationCreateService,
                                         GroupConversationCreateService groupConversationCreateService) {
        this.directConversationCreateService = directConversationCreateService;
        this.groupConversationCreateService = groupConversationCreateService;
    }

    @Override
    public Conversation create(ConversationType type, String name, Long createdBy, List<Long> participantIds) {
        log.info(() -> "Creating conversation: type=%s, createdBy=%d, participants=%s"
                .formatted(type, createdBy, participantIds));

        if (type == ConversationType.DIRECT) {
            return directConversationCreateService.create(createdBy, participantIds);
        } else {
            return groupConversationCreateService.create(name, createdBy, participantIds);
        }
    }
}
