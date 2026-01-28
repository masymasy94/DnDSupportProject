package com.dndplatform.chat.adapter.inbound.websocket;

import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageType;
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

    // Store connection -> userId mapping
    private final Map<String, Long> connectionUserMap = new ConcurrentHashMap<>();

    private final ChatSessionManager sessionManager;
    private final MessageSendService messageSendService;
    private final ParticipantFindByConversationRepository participantRepository;
    private final JWTParser jwtParser;

    @Inject
    public ChatWebSocketEndpoint(ChatSessionManager sessionManager,
                                 MessageSendService messageSendService,
                                 ParticipantFindByConversationRepository participantRepository,
                                 JWTParser jwtParser) {
        this.sessionManager = sessionManager;
        this.messageSendService = messageSendService;
        this.participantRepository = participantRepository;
        this.jwtParser = jwtParser;
    }

    @OnOpen
    public void onOpen(WebSocketConnection connection) {
        log.info(() -> "WebSocket connection opened: " + connection.id());

        // Extract token from query parameter (query() returns raw query string like "token=xxx")
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

            if ("SEND_MESSAGE".equals(incoming.type())) {
                return handleSendMessage(userId, incoming);
            } else {
                return jsonb.toJson(ChatWebSocketMessage.error("Unknown message type: " + incoming.type()));
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to process message", e);
            return jsonb.toJson(ChatWebSocketMessage.error("Failed to process message: " + e.getMessage()));
        }
    }

    private String handleSendMessage(Long senderId, ChatWebSocketIncomingMessage incoming) {
        if (incoming.conversationId() == null) {
            return jsonb.toJson(ChatWebSocketMessage.error("conversationId is required"));
        }
        if (incoming.content() == null || incoming.content().isBlank()) {
            return jsonb.toJson(ChatWebSocketMessage.error("content is required"));
        }

        try {
            MessageType messageType = incoming.messageType() != null
                    ? MessageType.valueOf(incoming.messageType())
                    : MessageType.TEXT;

            Message savedMessage = messageSendService.send(
                    incoming.conversationId(),
                    senderId,
                    incoming.content(),
                    messageType
            );

            // Broadcast to all participants
            List<Long> participantIds = participantRepository.findByConversationId(incoming.conversationId())
                    .stream()
                    .map(p -> p.userId())
                    .toList();

            ChatWebSocketMessage outgoing = ChatWebSocketMessage.newMessage(
                    savedMessage.conversationId(),
                    savedMessage.id(),
                    savedMessage.senderId(),
                    savedMessage.content(),
                    savedMessage.messageType().name(),
                    savedMessage.createdAt()
            );

            String outgoingJson = jsonb.toJson(outgoing);
            sessionManager.broadcastToUsers(participantIds, outgoingJson);

            return outgoingJson;

        } catch (IllegalArgumentException e) {
            return jsonb.toJson(ChatWebSocketMessage.error(e.getMessage()));
        }
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
