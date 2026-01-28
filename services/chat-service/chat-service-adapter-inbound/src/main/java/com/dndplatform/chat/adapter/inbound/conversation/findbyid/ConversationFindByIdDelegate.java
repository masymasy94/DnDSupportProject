package com.dndplatform.chat.adapter.inbound.conversation.findbyid;

import com.dndplatform.chat.adapter.inbound.conversation.mapper.ConversationViewModelMapper;
import com.dndplatform.chat.domain.ConversationFindByIdService;
import com.dndplatform.chat.view.model.ConversationFindByIdResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@Delegate
@RequestScoped
public class ConversationFindByIdDelegate implements ConversationFindByIdResource {

    private final ConversationFindByIdService service;
    private final ConversationViewModelMapper mapper;

    @Inject
    public ConversationFindByIdDelegate(ConversationFindByIdService service,
                                        ConversationViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ConversationViewModel findById(Long id, Long userId) {
        return service.findById(id, userId)
                .map(mapper)
                .orElseThrow(() -> new NotFoundException("Conversation not found: " + id));
    }
}
