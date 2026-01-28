package com.dndplatform.chat.view.model;

import com.dndplatform.chat.view.model.vm.MessageViewModel;
import com.dndplatform.chat.view.model.vm.SendMessageViewModel;
import jakarta.validation.Valid;

public interface MessageSendResource {
    MessageViewModel send(Long conversationId, Long userId, @Valid SendMessageViewModel request);
}
