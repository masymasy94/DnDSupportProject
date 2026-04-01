package com.dndplatform.chat.adapter.inbound.onlineusers;

import com.dndplatform.chat.adapter.inbound.websocket.ChatSessionManager;
import com.dndplatform.chat.view.model.OnlineUsersFindResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.Set;

@Delegate
@RequestScoped
public class OnlineUsersFindDelegate implements OnlineUsersFindResource {

    private final ChatSessionManager sessionManager;

    @Inject
    public OnlineUsersFindDelegate(ChatSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Set<Long> findOnlineUsers() {
        return sessionManager.getOnlineUserIds();
    }
}
