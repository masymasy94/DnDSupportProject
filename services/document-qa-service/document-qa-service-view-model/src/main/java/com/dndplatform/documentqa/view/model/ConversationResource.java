package com.dndplatform.documentqa.view.model;

import com.dndplatform.documentqa.view.model.vm.ConversationMessageViewModel;
import com.dndplatform.documentqa.view.model.vm.ConversationViewModel;

import java.util.List;

public interface ConversationResource {

    List<ConversationViewModel> listConversations(Long userId);

    ConversationViewModel getConversation(Long id, Long userId);

    List<ConversationMessageViewModel> getMessages(Long id, Long userId);

    void deleteConversation(Long id, Long userId);
}
