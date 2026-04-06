package com.dndplatform.chat.adapter.inbound.onlineusers;

import com.dndplatform.chat.adapter.inbound.websocket.ChatSessionManager;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class OnlineUsersFindDelegateTest {

    @Mock
    private ChatSessionManager sessionManager;

    private OnlineUsersFindDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new OnlineUsersFindDelegate(sessionManager);
    }

    @Test
    void shouldReturnOnlineUserIds() {
        Set<Long> onlineUsers = Set.of(1L, 2L, 3L);
        given(sessionManager.getOnlineUserIds()).willReturn(onlineUsers);

        var result = sut.findOnlineUsers();

        assertThat(result).isEqualTo(onlineUsers);
        then(sessionManager).should().getOnlineUserIds();
    }
}
