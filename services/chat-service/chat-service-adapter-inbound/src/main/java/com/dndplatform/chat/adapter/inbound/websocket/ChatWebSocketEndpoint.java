package com.dndplatform.chat.adapter.inbound.websocket;

import com.dndplatform.chat.domain.repository.ParticipantFindByConversationRepository;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnError;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebSocket(path = "/ws/chat")
public class ChatWebSocketEndpoint {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Jsonb jsonb = JsonbBuilder.create();

    private final Map<String, Long> connectionUserMap = new ConcurrentHashMap<>();

    private final ChatSessionManager sessionManager;
    private final JWTParser jwtParser;
    private final ChatWebSocketMessageHandler messageHandler;
    private final ParticipantFindByConversationRepository participantRepository;

    @Inject
    public ChatWebSocketEndpoint(ChatSessionManager sessionManager,
                                 JWTParser jwtParser,
                                 ChatWebSocketMessageHandler messageHandler,
                                 ParticipantFindByConversationRepository participantRepository) {
        this.sessionManager = sessionManager;
        this.jwtParser = jwtParser;
        this.messageHandler = messageHandler;
        this.participantRepository = participantRepository;
    }

    @OnOpen
    public void onOpen(WebSocketConnection connection) {
        log.info(() -> "WebSocket connection opened: " + connection.id());

        String token = parseQueryParam(connection.handshakeRequest().query(), "token");

        if (token == null || token.isBlank()) {
            connection.sendTextAndAwait(jsonb.toJson(ChatWebSocketMessage.error("Missing authentication token")));
            return;
        }

        try {
            JsonWebToken jwt = jwtParser.parse(token);
            Long userId = extractUserId(jwt);

            if (userId == null) {
                connection.sendTextAndAwait(jsonb.toJson(ChatWebSocketMessage.error("Invalid token: unable to extract user ID")));
                return;
            }

            connectionUserMap.put(connection.id(), userId);
            sessionManager.addConnection(userId, connection);
            connection.sendTextAndAwait(jsonb.toJson(ChatWebSocketMessage.connected()));

        } catch (ParseException e) {
            log.warning(() -> "Failed to parse JWT: " + e.getMessage());
            connection.sendTextAndAwait(jsonb.toJson(ChatWebSocketMessage.error("Invalid authentication token")));
        }
    }

    @OnClose
    public void onClose(WebSocketConnection connection) {
        log.info(() -> "WebSocket connection closed: " + connection.id());
        Long userId = connectionUserMap.remove(connection.id());
        if (userId != null) {
            sessionManager.removeConnection(userId, connection);
        }
    }

    @OnError
    public void onError(WebSocketConnection connection, Throwable throwable) {
        log.log(Level.WARNING, "WebSocket error for connection " + connection.id(), throwable);
        Long userId = connectionUserMap.remove(connection.id());
        if (userId != null) {
            sessionManager.removeConnection(userId, connection);
        }
    }

    @OnTextMessage
    @Blocking
    public String onMessage(String message, WebSocketConnection connection) {
        Long userId = connectionUserMap.get(connection.id());
        if (userId == null) {
            return jsonb.toJson(ChatWebSocketMessage.error("Session not authenticated"));
        }

        try {
            ChatWebSocketIncomingMessage incoming = jsonb.fromJson(message, ChatWebSocketIncomingMessage.class);

            ChatWebSocketMessage result;
            if ("SEND_MESSAGE".equals(incoming.type())) {
                result = messageHandler.handleSendMessage(userId, incoming.conversationId(), incoming.content(), incoming.messageType());
            } else if ("ROLL_DICE".equals(incoming.type())) {
                result = messageHandler.handleRollDice(userId, incoming.conversationId(), incoming.content());
            } else {
                result = ChatWebSocketMessage.error("Unknown message type: " + incoming.type());
            }

            String resultJson = jsonb.toJson(result);

            // Broadcast successful messages to all conversation participants
            if ("NEW_MESSAGE".equals(result.type()) && incoming.conversationId() != null) {
                broadcastToParticipants(incoming.conversationId(), resultJson);
            }

            return resultJson;
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to process message", e);
            return jsonb.toJson(ChatWebSocketMessage.error("Failed to process message: " + e.getMessage()));
        }
    }

    private void broadcastToParticipants(Long conversationId, String messageJson) {
        List<Long> participantIds = participantRepository.findByConversationId(conversationId)
                .stream()
                .map(p -> p.userId())
                .toList();
        sessionManager.broadcastToUsers(participantIds, messageJson);
    }

    private Long extractUserId(JsonWebToken jwt) {
        Object userIdClaim = jwt.getClaim("userId");
        if (userIdClaim != null) {
            if (userIdClaim instanceof Number) {
                return ((Number) userIdClaim).longValue();
            }
            return Long.parseLong(userIdClaim.toString());
        }

        String subject = jwt.getSubject();
        if (subject != null) {
            try {
                return Long.parseLong(subject);
            } catch (NumberFormatException e) {
                // Subject might be username, not user ID
            }
        }
        return null;
    }

    private String parseQueryParam(String queryString, String paramName) {
        if (queryString == null || queryString.isBlank()) {
            return null;
        }
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
