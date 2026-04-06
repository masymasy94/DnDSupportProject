# Coverage 90%+ Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Bring chat-service/adapter-inbound and character-service/adapter-inbound to 90%+ instruction coverage by adding tests for uncovered classes.

**Architecture:** Pure test additions — no production code changes. Unit tests with Mockito mocks for framework classes (WebSocketConnection, JWTParser, HandshakeRequest). Integration tests using real PDF template for parser.

**Tech Stack:** JUnit 5, Mockito, AssertJ, common-test RandomExtension, Apache PDFBox 3.0.4

**Maven command prefix:** `export JAVA_HOME="/Users/marybookpro/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home" && bash "/Users/marybookpro/Library/Application Support/JetBrains/IntelliJIdea2025.3/plugins/maven/lib/maven3/bin/mvn"`

---

## File Structure

| Action | File |
|--------|------|
| Create | `services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketEndpointTest.java` |
| Modify | `services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/conversation/create/ConversationCreateDelegateTest.java` |
| Modify | `services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/conversation/findbyid/ConversationFindByIdDelegateTest.java` |
| Create | `services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/importsheet/parser/PdfCharacterSheetParserTest.java` |
| Modify | `services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/importsheet/CharacterImportSheetResourceImplTest.java` |

---

### Task 1: ChatWebSocketEndpoint tests

**Files:**
- Create: `services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketEndpointTest.java`

- [ ] **Step 1: Write the test class**

```java
package com.dndplatform.chat.adapter.inbound.websocket;

import com.dndplatform.chat.domain.repository.ParticipantFindByConversationRepository;
import com.dndplatform.chat.domain.model.ConversationParticipant;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
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
    void shouldSendErrorWhenTokenMissing() {
        given(handshakeRequest.query()).willReturn(null);

        sut.onOpen(connection);

        then(connection).should().sendTextAndAwait(contains("Missing authentication token"));
        then(sessionManager).should(never()).addConnection(anyString() != null ? 0L : 0L, connection);
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
        then(sessionManager).should(never()).addConnection(anyLong(), connection);
    }

    @Test
    void shouldSendErrorWhenUserIdCannotBeExtracted() throws ParseException {
        given(handshakeRequest.query()).willReturn("token=valid-jwt");
        given(jwtParser.parse("valid-jwt")).willReturn(jwt);
        // jwt.getClaim("userId") returns null, jwt.getSubject() returns non-numeric
        given(jwt.getSubject()).willReturn("not-a-number");

        sut.onOpen(connection);

        then(connection).should().sendTextAndAwait(contains("Invalid token: unable to extract user ID"));
    }

    // --- onClose tests ---

    @Test
    void shouldRemoveConnectionOnClose() throws ParseException {
        // First authenticate
        given(handshakeRequest.query()).willReturn("token=t");
        given(jwtParser.parse("t")).willReturn(jwt);
        given(jwt.getSubject()).willReturn("1");
        sut.onOpen(connection);

        sut.onClose(connection);

        then(sessionManager).should().removeConnection(1L, connection);
    }

    @Test
    void shouldHandleCloseForUnauthenticatedConnection() {
        sut.onClose(connection);

        then(sessionManager).should(never()).removeConnection(anyLong(), connection);
    }

    // --- onError tests ---

    @Test
    void shouldRemoveConnectionOnError() throws ParseException {
        given(handshakeRequest.query()).willReturn("token=t");
        given(jwtParser.parse("t")).willReturn(jwt);
        given(jwt.getSubject()).willReturn("1");
        sut.onOpen(connection);

        sut.onError(connection, new RuntimeException("test error"));

        then(sessionManager).should().removeConnection(1L, connection);
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

        then(sessionManager).should().broadcastToUsers(List.of(participant.userId()), anyString());
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

    private long anyLong() {
        return org.mockito.ArgumentMatchers.anyLong();
    }
}
```

- [ ] **Step 2: Run tests**

Run: `{mvn} test -pl services/chat-service/chat-service-adapter-inbound -Dtest=ChatWebSocketEndpointTest -am -Dsurefire.failIfNoSpecifiedTests=false`

Expected: All tests PASS.

- [ ] **Step 3: Commit**

```bash
git add services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/websocket/ChatWebSocketEndpointTest.java
git commit -m "test: add ChatWebSocketEndpoint unit tests"
```

---

### Task 2: ConversationCreateDelegate missing branches

**Files:**
- Modify: `services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/conversation/create/ConversationCreateDelegateTest.java`

- [ ] **Step 4: Add missing test cases**

Add these tests to the existing file after the existing `shouldDelegateToService` test:

```java
    @Test
    void shouldCreateDirectConversationWithValidParticipants(@Random Long userId,
                                                              @Random Conversation conversation,
                                                              @Random ConversationViewModel expected) {
        List<Long> participantIds = List.of(2L);
        CreateConversationViewModel request = new CreateConversationViewModel("DIRECT", null, participantIds);

        given(conversationCreateService.create(ConversationType.DIRECT, null, userId, participantIds))
                .willReturn(conversation);
        given(mapper.apply(conversation)).willReturn(expected);

        var actual = sut.create(userId, request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldThrowWhenDirectConversationHasNullParticipants(@Random Long userId) {
        CreateConversationViewModel request = new CreateConversationViewModel("DIRECT", null, null);

        assertThatThrownBy(() -> sut.create(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one other participant");
    }

    @Test
    void shouldThrowWhenDirectConversationHasEmptyParticipants(@Random Long userId) {
        CreateConversationViewModel request = new CreateConversationViewModel("DIRECT", null, List.of());

        assertThatThrownBy(() -> sut.create(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one other participant");
    }
```

Also add import at top: `import static org.assertj.core.api.Assertions.assertThatThrownBy;`

- [ ] **Step 5: Run tests**

Run: `{mvn} test -pl services/chat-service/chat-service-adapter-inbound -Dtest=ConversationCreateDelegateTest -am -Dsurefire.failIfNoSpecifiedTests=false`

Expected: All 4 tests PASS.

- [ ] **Step 6: Commit**

```bash
git add services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/conversation/create/ConversationCreateDelegateTest.java
git commit -m "test: add ConversationCreateDelegate DIRECT + validation tests"
```

---

### Task 3: ConversationFindByIdDelegate missing branch

**Files:**
- Modify: `services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/conversation/findbyid/ConversationFindByIdDelegateTest.java`

- [ ] **Step 7: Add not-found test**

Add after existing test:

```java
    @Test
    void shouldThrowNotFoundWhenConversationMissing(@Random Long id, @Random Long userId) {
        given(service.findById(id, userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(id, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Conversation not found");
    }
```

Also add imports: `import static org.assertj.core.api.Assertions.assertThatThrownBy;` and `import jakarta.ws.rs.NotFoundException;`

- [ ] **Step 8: Run tests**

Run: `{mvn} test -pl services/chat-service/chat-service-adapter-inbound -Dtest=ConversationFindByIdDelegateTest -am -Dsurefire.failIfNoSpecifiedTests=false`

Expected: All 2 tests PASS.

- [ ] **Step 9: Commit**

```bash
git add services/chat-service/chat-service-adapter-inbound/src/test/java/com/dndplatform/chat/adapter/inbound/conversation/findbyid/ConversationFindByIdDelegateTest.java
git commit -m "test: add ConversationFindByIdDelegate not-found test"
```

---

### Task 4: PdfCharacterSheetParser tests

**Files:**
- Create: `services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/importsheet/parser/PdfCharacterSheetParserTest.java`

The parser extracts form fields from a PDF. We can use the real WotC template (already on classpath via `src/main/resources`) to test the happy path, and bad bytes for error paths.

- [ ] **Step 10: Write the test class**

```java
package com.dndplatform.character.adapter.inbound.importsheet.parser;

import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfCharacterSheetParserTest {

    private PdfCharacterSheetParser sut;

    @BeforeEach
    void setUp() {
        sut = new PdfCharacterSheetParser();
    }

    @Test
    void shouldExtractFormFieldsFromValidPdf() throws IOException {
        byte[] pdfBytes = loadTemplate();

        Map<String, String> fields = sut.extractFormFields(pdfBytes);

        assertThat(fields).isNotEmpty();
        assertThat(fields).containsKey("CharacterName");
        assertThat(fields).containsKey("STR");
        assertThat(fields).containsKey("ClassLevel");
    }

    @Test
    void shouldThrowBadRequestForInvalidPdfBytes() {
        byte[] garbage = "not a pdf".getBytes();

        assertThatThrownBy(() -> sut.extractFormFields(garbage))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Failed to parse PDF");
    }

    @Test
    void shouldThrowBadRequestForEmptyPdfWithNoForm() {
        // A minimal valid PDF with no AcroForm — use truncated bytes to trigger IOException
        byte[] truncated = new byte[]{0x25, 0x50, 0x44, 0x46, 0x2D}; // %PDF-

        assertThatThrownBy(() -> sut.extractFormFields(truncated))
                .isInstanceOf(BadRequestException.class);
    }

    private byte[] loadTemplate() throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/resources/wotc-5e-sheet.pdf")) {
            if (is == null) {
                throw new RuntimeException("PDF template not found");
            }
            return is.readAllBytes();
        }
    }
}
```

- [ ] **Step 11: Run tests**

Run: `{mvn} test -pl services/character-service/character-service-adapter-inbound -Dtest=PdfCharacterSheetParserTest -am -Dsurefire.failIfNoSpecifiedTests=false`

Expected: All 3 tests PASS.

- [ ] **Step 12: Commit**

```bash
git add services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/importsheet/parser/PdfCharacterSheetParserTest.java
git commit -m "test: add PdfCharacterSheetParser unit tests"
```

---

### Task 5: CharacterImportSheetResourceImpl missing branches

**Files:**
- Modify: `services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/importsheet/CharacterImportSheetResourceImplTest.java`

- [ ] **Step 13: Add missing test cases**

Add after existing tests:

```java
    @Test
    void shouldAcceptNullContentType(@Random Long userId, @Random CharacterViewModel expected, @TempDir Path tempDir) throws IOException {
        var pdfFile = tempDir.resolve("sheet.pdf");
        var pdfBytes = new byte[]{37, 80, 68, 70, 45}; // %PDF-
        Files.write(pdfFile, pdfBytes);

        given(fileUpload.contentType()).willReturn(null);
        given(fileUpload.uploadedFile()).willReturn(pdfFile);
        given(fileUpload.fileName()).willReturn("sheet.pdf");
        given(delegate.importSheetWithUserId(pdfBytes, "sheet.pdf", "application/pdf", userId)).willReturn(expected);

        var result = sut.importSheet(fileUpload, userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldUseDefaultFileNameWhenNull(@Random Long userId, @Random CharacterViewModel expected, @TempDir Path tempDir) throws IOException {
        var pdfFile = tempDir.resolve("sheet.pdf");
        var pdfBytes = new byte[]{37, 80, 68, 70, 45};
        Files.write(pdfFile, pdfBytes);

        given(fileUpload.contentType()).willReturn("application/pdf");
        given(fileUpload.uploadedFile()).willReturn(pdfFile);
        given(fileUpload.fileName()).willReturn(null);
        given(delegate.importSheetWithUserId(pdfBytes, "character-sheet.pdf", "application/pdf", userId)).willReturn(expected);

        var result = sut.importSheet(fileUpload, userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrowUnsupportedOperationForStreamEndpoint() {
        assertThatThrownBy(() -> sut.importSheet(null, "file.pdf"))
                .isInstanceOf(UnsupportedOperationException.class);
    }
```

- [ ] **Step 14: Run tests**

Run: `{mvn} test -pl services/character-service/character-service-adapter-inbound -Dtest=CharacterImportSheetResourceImplTest -am -Dsurefire.failIfNoSpecifiedTests=false`

Expected: All 7 tests PASS.

- [ ] **Step 15: Commit**

```bash
git add services/character-service/character-service-adapter-inbound/src/test/java/com/dndplatform/character/adapter/inbound/importsheet/CharacterImportSheetResourceImplTest.java
git commit -m "test: add CharacterImportSheetResourceImpl missing branch tests"
```

---

### Task 6: Full test suite verification

- [ ] **Step 16: Run chat-service adapter-inbound full suite**

Run: `{mvn} test -pl services/chat-service/chat-service-adapter-inbound -am -Dsurefire.failIfNoSpecifiedTests=false`

Expected: All tests PASS.

- [ ] **Step 17: Run character-service adapter-inbound full suite**

Run: `{mvn} test -pl services/character-service/character-service-adapter-inbound -am -Dsurefire.failIfNoSpecifiedTests=false`

Expected: All tests PASS.

- [ ] **Step 18: Verify coverage meets 90%+ target**

Check JaCoCo CSV reports for both modules. Chat should be ~90%+, Character should be ~95%+.
