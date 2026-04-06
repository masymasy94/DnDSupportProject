package com.dndplatform.chat.adapter.inbound.websocket;

import com.dndplatform.chat.domain.model.ConversationParticipant;
import com.dndplatform.chat.domain.repository.ParticipantFindByConversationRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import io.quarkus.websockets.next.HandshakeRequest;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class ChatWebSocketEndpointTest {

    @Mock
    private ChatSessionManager sessionManager;

    @Mock
    private JWTParser jwtParser;

    @Mock
    private ChatWebSocketMessageHandler messageHandler;

    @Mock
    private ParticipantFindByConversationRepository participantRepository;

    @Mock
    private WebSocketConnection connection;

    @Mock
    private HandshakeRequest handshakeRequest;

    @Mock
    private JsonWebToken jwt;

    private ChatWebSocketEndpoint sut;

    @BeforeEach
    void setUp() {
        sut = new ChatWebSocketEndpoint(sessionManager, jwtParser, messageHandler, participantRepository);
        given(connection.id()).willReturn("conn-1");
        given(connection.handshakeRequest()).willReturn(handshakeRequest);
    }

    // --- onOpen tests ---

    @Test
    void shouldAuthenticateAndConnectWithValidToken() throws ParseException {
        given(handshakeRequest.query()).willReturn("token=valid-jwt");
        given(jwtParser.parse("valid-jwt")).willReturn(jwt);
        given(jwt.getSubject()).willReturn("42");

        sut.onOpen(connection);

        then(sessionManager).should().addConnection(42L, connection);
        then(connection).should().sendTextAndAwait(contains("CONNECTED"));
    }

    @Test
    void shouldAuthenticateViaUserIdClaim() throws ParseException {
        given(handshakeRequest.query()).willReturn("token=valid-jwt");
        given(jwtParser.parse("valid-jwt")).willReturn(jwt);
        given(jwt.getClaim("userId")).willReturn(99L);

        sut.onOpen(connection);

        then(sessionManager).should().addConnection(99L, connection);
    }

    @Test
    void shouldAuthenticateViaStringUserIdClaim() throws ParseException {
        given(handshakeRequest.query()).willReturn("token=valid-jwt");
        given(jwtParser.parse("valid-jwt")).willReturn(jwt);
        given(jwt.getClaim("userId")).willReturn("77");

        sut.onOpen(connection);

        then(sessionManager).should().addConnection(77L, connection);
    }

    @Test
    void shouldSendErrorWhenQueryNull() {
        given(handshakeRequest.query()).willReturn(null);

        sut.onOpen(connection);

        then(connection).should().sendTextAndAwait(contains("Missing authentication token"));
        then(sessionManager).should(never()).addConnection(anyLong(), eq(connection));
    }

    @Test
    void shouldSendErrorWhenTokenBlank() {
        given(handshakeRequest.query()).willReturn("token=");

        sut.onOpen(connection);

        then(connection).should().sendTextAndAwait(contains("Missing authentication token"));
    }

    @Test
    void shouldSendErrorWhenQueryHasNoTokenParam() {
        given(handshakeRequest.query()).willReturn("other=value");

        sut.onOpen(connection);

        then(connection).should().sendTextAndAwait(contains("Missing authentication token"));
    }

    @Test
    void shouldSendErrorWhenJwtParsingFails() throws ParseException {
        given(handshakeRequest.query()).willReturn("token=bad-jwt");
        given(jwtParser.parse("bad-jwt")).willThrow(new ParseException("Invalid"));

        sut.onOpen(connection);

        then(connection).should().sendTextAndAwait(contains("Invalid authentication token"));
        then(sessionManager).should(never()).addConnection(anyLong(), eq(connection));
    }

    @Test
    void shouldSendErrorWhenUserIdCannotBeExtracted() throws ParseException {
        given(handshakeRequest.query()).willReturn("token=valid-jwt");
        given(jwtParser.parse("valid-jwt")).willReturn(jwt);
        given(jwt.getSubject()).willReturn("not-a-number");

        sut.onOpen(connection);

        then(connection).should().sendTextAndAwait(contains("Invalid token: unable to extract user ID"));
    }

    // --- onClose tests ---

    @Test
    void shouldRemoveConnectionOnClose() throws ParseException {
        authenticateConnection();

        sut.onClose(connection);

        then(sessionManager).should().removeConnection(1L, connection);
    }

    @Test
    void shouldHandleCloseForUnauthenticatedConnection() {
        sut.onClose(connection);

        then(sessionManager).should(never()).removeConnection(anyLong(), eq(connection));
    }

    // --- onError tests ---

    @Test
    void shouldRemoveConnectionOnError() throws ParseException {
        authenticateConnection();

        sut.onError(connection, new RuntimeException("test error"));

        then(sessionManager).should().removeConnection(1L, connection);
    }

    @Test
    void shouldHandleErrorForUnauthenticatedConnection() {
        sut.onError(connection, new RuntimeException("test"));

        then(sessionManager).should(never()).removeConnection(anyLong(), eq(connection));
    }

    // --- onMessage tests ---

    @Test
    void shouldReturnErrorForUnauthenticatedMessage() {
        String result = sut.onMessage("{}", connection);

        assertThat(result).contains("Session not authenticated");
    }

    @Test
    void shouldDelegateSendMessage() throws ParseException {
        authenticateConnection();
        ChatWebSocketMessage response = ChatWebSocketMessage.error("test");
        given(messageHandler.handleSendMessage(1L, 10L, "hello", null)).willReturn(response);

        String result = sut.onMessage("{\"type\":\"SEND_MESSAGE\",\"conversationId\":10,\"content\":\"hello\"}", connection);

        assertThat(result).contains("ERROR");
    }

    @Test
    void shouldDelegateRollDice() throws ParseException {
        authenticateConnection();
        ChatWebSocketMessage response = ChatWebSocketMessage.error("test");
        given(messageHandler.handleRollDice(1L, 10L, "2d6")).willReturn(response);

        String result = sut.onMessage("{\"type\":\"ROLL_DICE\",\"conversationId\":10,\"content\":\"2d6\"}", connection);

        assertThat(result).contains("ERROR");
    }

    @Test
    void shouldReturnErrorForUnknownMessageType() throws ParseException {
        authenticateConnection();

        String result = sut.onMessage("{\"type\":\"UNKNOWN\"}", connection);

        assertThat(result).contains("Unknown message type");
    }

    @Test
    void shouldBroadcastOnSuccessfulSendMessage(@Random ConversationParticipant participant) throws ParseException {
        authenticateConnection();
        ChatWebSocketMessage success = ChatWebSocketMessage.newMessage(10L, 1L, 1L, "hello", "TEXT", null);
        given(messageHandler.handleSendMessage(1L, 10L, "hello", null)).willReturn(success);
        given(participantRepository.findByConversationId(10L)).willReturn(List.of(participant));

        sut.onMessage("{\"type\":\"SEND_MESSAGE\",\"conversationId\":10,\"content\":\"hello\"}", connection);

        then(sessionManager).should().broadcastToUsers(eq(List.of(participant.userId())), anyString());
    }

    @Test
    void shouldHandleMalformedJson() throws ParseException {
        authenticateConnection();

        String result = sut.onMessage("not json", connection);

        assertThat(result).contains("Failed to process message");
    }

    private void authenticateConnection() throws ParseException {
        given(handshakeRequest.query()).willReturn("token=t");
        given(jwtParser.parse("t")).willReturn(jwt);
        given(jwt.getSubject()).willReturn("1");
        sut.onOpen(connection);
    }
}
