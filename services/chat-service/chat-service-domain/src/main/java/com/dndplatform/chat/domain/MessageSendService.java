package com.dndplatform.chat.domain;

import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;

public interface MessageSendService {
    Message send(Long conversationId, Long senderId, String content, MessageType messageType);
}
