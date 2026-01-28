package com.dndplatform.chat.adapter.inbound.conversation.findbyuser;

import com.dndplatform.chat.adapter.inbound.conversation.mapper.ConversationViewModelMapper;
import com.dndplatform.chat.domain.ConversationFindByUserService;
import com.dndplatform.chat.view.model.ConversationFindByUserResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class ConversationFindByUserDelegate implements ConversationFindByUserResource {

    private final ConversationFindByUserService service;
    private final ConversationViewModelMapper mapper;

    @Inject
    public ConversationFindByUserDelegate(ConversationFindByUserService service,
                                          ConversationViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<ConversationViewModel> findByUser(long userId) {
        return service.findByUserId(userId).stream()
                .map(mapper)
                .toList();
    }
}
