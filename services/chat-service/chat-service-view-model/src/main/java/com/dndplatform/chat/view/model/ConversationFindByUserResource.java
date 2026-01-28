package com.dndplatform.chat.view.model;

import com.dndplatform.chat.view.model.vm.ConversationViewModel;

import java.util.List;

public interface ConversationFindByUserResource {
    List<ConversationViewModel> findByUser(long userId);
}
