package com.dndplatform.chat.view.model;

import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.chat.view.model.vm.CreateConversationViewModel;
import jakarta.validation.Valid;

public interface ConversationCreateResource {
    ConversationViewModel create(Long userId, @Valid CreateConversationViewModel request);
}
