package com.dndplatform.chat.adapter.inbound.websocket;

import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@ApplicationScoped
public class ChatSessionManager {

    private final Logger log = Logger.getLogger(getClass().getName());

    // Maps userId to their active connections (user can have multiple connections)
    private final Map<Long, List<WebSocketConnection>> userConnections = new ConcurrentHashMap<>();

    public void addConnection(Long userId, WebSocketConnection connection) {
        userConnections.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(connection);
        log.info(() -> "User %d connected with connection %s. Total connections for user: %d"
                .formatted(userId, connection.id(), userConnections.get(userId).size()));
    }

    public void removeConnection(Long userId, WebSocketConnection connection) {
        List<WebSocketConnection> connections = userConnections.get(userId);
        if (connections != null) {
            connections.remove(connection);
            if (connections.isEmpty()) {
                userConnections.remove(userId);
            }
        }
        log.info(() -> "User %d disconnected connection %s".formatted(userId, connection.id()));
    }

    public List<WebSocketConnection> getConnectionsForUser(Long userId) {
        return userConnections.getOrDefault(userId, List.of());
    }

    public void broadcastToUsers(List<Long> userIds, String message) {
        for (Long userId : userIds) {
            List<WebSocketConnection> connections = getConnectionsForUser(userId);
            for (WebSocketConnection connection : connections) {
                if (connection.isOpen()) {
                    connection.sendTextAndAwait(message);
                }
            }
        }
    }
}
