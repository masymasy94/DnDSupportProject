package com.dndplatform.chat.adapter.inbound.message.send;

import com.dndplatform.chat.adapter.inbound.message.mapper.MessageViewModelMapper;
import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.chat.view.model.MessageSendResource;
import com.dndplatform.chat.view.model.vm.MessageViewModel;
import com.dndplatform.chat.view.model.vm.SendMessageViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class MessageSendDelegate implements MessageSendResource {

    private final MessageSendService service;
    private final MessageViewModelMapper mapper;

    @Inject
    public MessageSendDelegate(MessageSendService service,
                               MessageViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public MessageViewModel send(Long conversationId, Long userId, SendMessageViewModel request) {
        MessageType messageType = request.messageType() != null
                ? MessageType.valueOf(request.messageType())
                : MessageType.TEXT;

        var message = service.send(conversationId, userId, request.content(), messageType);
        return mapper.apply(message);
    }
}
