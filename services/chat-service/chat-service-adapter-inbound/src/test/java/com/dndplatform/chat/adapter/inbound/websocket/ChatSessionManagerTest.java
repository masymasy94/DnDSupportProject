package com.dndplatform.chat.adapter.inbound.websocket;

import io.quarkus.websockets.next.WebSocketConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ChatSessionManagerTest {

    @Mock
    private WebSocketConnection connection1;

    @Mock
    private WebSocketConnection connection2;

    private ChatSessionManager sut;

    @BeforeEach
    void setUp() {
        sut = new ChatSessionManager();
        given(connection1.id()).willReturn("conn-1");
        given(connection2.id()).willReturn("conn-2");
    }

    @Test
    void shouldAddConnectionForUser() {
        sut.addConnection(1L, connection1);

        assertThat(sut.getConnectionsForUser(1L)).containsExactly(connection1);
    }

    @Test
    void shouldSupportMultipleConnectionsForSameUser() {
        sut.addConnection(1L, connection1);
        sut.addConnection(1L, connection2);

        assertThat(sut.getConnectionsForUser(1L)).containsExactlyInAnyOrder(connection1, connection2);
    }

    @Test
    void shouldReturnEmptyListForUnknownUser() {
        assertThat(sut.getConnectionsForUser(99L)).isEmpty();
    }

    @Test
    void shouldRemoveConnectionAndKeepOthers() {
        sut.addConnection(1L, connection1);
        sut.addConnection(1L, connection2);

        sut.removeConnection(1L, connection1);

        assertThat(sut.getConnectionsForUser(1L)).containsExactly(connection2);
    }

    @Test
    void shouldRemoveUserEntryWhenLastConnectionRemoved() {
        sut.addConnection(1L, connection1);

        sut.removeConnection(1L, connection1);

        assertThat(sut.getOnlineUserIds()).doesNotContain(1L);
    }

    @Test
    void shouldReturnOnlineUserIds() {
        sut.addConnection(1L, connection1);
        sut.addConnection(2L, connection2);

        assertThat(sut.getOnlineUserIds()).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void shouldBroadcastToOpenConnections() {
        given(connection1.isOpen()).willReturn(true);
        sut.addConnection(1L, connection1);

        sut.broadcastToUsers(List.of(1L), "Hello");

        then(connection1).should().sendTextAndAwait("Hello");
    }

    @Test
    void shouldNotSendToClosedConnections() {
        given(connection1.isOpen()).willReturn(false);
        sut.addConnection(1L, connection1);

        sut.broadcastToUsers(List.of(1L), "Hello");

        then(connection1).should(never()).sendTextAndAwait(anyString());
    }

    @Test
    void shouldHandleRemoveConnectionWhenUserNotTracked() {
        // Should not throw
        sut.removeConnection(99L, connection1);
    }
}
