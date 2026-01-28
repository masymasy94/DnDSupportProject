package com.dndplatform.chat.view.model;

import com.dndplatform.chat.view.model.vm.ConversationViewModel;

public interface ConversationFindByIdResource {
    ConversationViewModel findById(Long id, Long userId);
}
